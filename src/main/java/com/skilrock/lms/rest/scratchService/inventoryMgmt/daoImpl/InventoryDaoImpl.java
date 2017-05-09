package com.skilrock.lms.rest.scratchService.inventoryMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.dao.InventoryDao;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.serviceImpl.BookActivationServiceImpl;
import com.skilrock.lms.rest.services.bean.DaoBean;


public class InventoryDaoImpl implements InventoryDao{
	
	private static Logger logger = LoggerFactory.getLogger(BookActivationServiceImpl.class);

	private CommonFunctionsHelper helper;
	
	public InventoryDaoImpl(){
		this.helper = new CommonFunctionsHelper();
	}
	
	public InventoryDaoImpl(CommonFunctionsHelper helper){
		this.helper = helper;
	}
	
	public int insertTicketByTicketSaleTxn(Connection con, DaoBean daoBean) throws SQLException {
		PreparedStatement pstmt1;
		String insTicketByTicketSaleTXn = "insert into st_se_ticket_by_ticket_sale_txn values(?,?,?,?,?,?,?,?,?)";
		pstmt1 = con.prepareStatement(insTicketByTicketSaleTXn);
		pstmt1.setString(1,daoBean.getTxnId());
		pstmt1.setString(2, daoBean.getTicketNbr());
		pstmt1.setInt(3, daoBean.getUserOrgId());
		pstmt1.setInt(4, daoBean.getUserId());
		pstmt1.setTimestamp(5,new Timestamp(daoBean.getDateTime().getTime()));
		pstmt1.setString(6,daoBean.getTpTransactionId());
		pstmt1.setString(7,daoBean.getStatus());
		pstmt1.setInt(8, daoBean.getGameId());
		pstmt1.setInt(9,daoBean.getTicketPrice());
		return pstmt1.executeUpdate();
	}
	
	public String updateSellTicketStatusAsUnSold(DaoBean daoBean) throws LMSException{
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		int gameId = 0;
		int ticketPrice= 0;
		try {
			con = DBConnect.getConnection();
			gameId = getGameIdFromGameNbr(con,daoBean);
			ticketPrice = getTicketPriceFromGameNbr(con,daoBean);
			daoBean.setGameId(gameId);
			daoBean.setTicketPrice(ticketPrice);
			return updateTicketStatusToUnSold(con,daoBean);
		} catch (LMSException e) {
			logger.error("Exception",e);
			throw e;
		}catch (SQLException e) {
			logger.error("SQL Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con, pstmt);
			DBConnect.closePstmt(pstmt1);
		}
	}
	
	public int getGameIdFromGameNbr(Connection con,DaoBean bean) throws NumberFormatException, SQLException, LMSException{
		PreparedStatement pstmt = null;
		String query = null;
		ResultSet rs = null;
		query = "SELECT game_id from st_se_game_master WHERE game_nbr=?";
		pstmt = con.prepareStatement(query);
		pstmt.setInt(1, Integer.parseInt(bean.getTicketNbr().substring(0, 3)));
		rs = pstmt.executeQuery();

		if (rs.next()) {
			int gameId = rs.getInt("game_id");
			pstmt.clearParameters();
			return gameId;
		}else{
			throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
		}
	}
	
	public int getTicketPriceFromGameNbr(Connection con,DaoBean bean) throws NumberFormatException, SQLException, LMSException{
		PreparedStatement pstmt = null;
		String query = null;
		ResultSet rs = null;
		query = "SELECT ticket_price from st_se_game_master WHERE game_nbr=?";
		pstmt = con.prepareStatement(query);
		pstmt.setInt(1, Integer.parseInt(bean.getTicketNbr().substring(0, 3)));
		rs = pstmt.executeQuery();

		if (rs.next()) {
			int ticketPrice = rs.getInt("ticket_price");
			pstmt.clearParameters();
			return ticketPrice;
		}else{
			throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
		}
	}
	
	private String updateTicketStatusToUnSold(Connection con,DaoBean daoBean) throws SQLException, LMSException{
		ResultSet rs = null;
		TicketBean tktBean = helper.isTicketFormatValid(daoBean.getTicketNbr(),daoBean.getGameId(), con);
		String ticketNbr = tktBean.getTicketNumber();
		daoBean.setTicketNbr(ticketNbr);
		if (tktBean.getIsValid()) {
			con.setAutoCommit(false);
			rs = lastRecordInGameInvStatusAndGameInvHistoryForGivenTikcetNbr(con,daoBean);
			if(rs.next()){
				String refTxnId = tktBeanIsValidUpdateTicketStatus(con,rs,daoBean);
				return refTxnId;
			}else{
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE_SELL_TICKET);
			}
		}else{
			throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE_SELL_TICKET);
		}
	}
	private ResultSet lastRecordInGameInvStatusAndGameInvHistoryForGivenTikcetNbr(Connection con,DaoBean bean) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String getlastRecQry ="select aa.game_id, aa.book_nbr, aa.current_owner, aa.current_owner_id,(cur_rem_tickets+active_tickets_upto) totalTkt ,cur_rem_tickets, 1 as sold_tickets , concat(bb.book_status, '_CLOSE') 'book_status' from (SELECT date,game_id, book_nbr,current_owner,current_owner_id FROM st_se_game_ticket_inv_history where current_owner_id=? and book_nbr = ? FOR UPDATE )aa inner join (SELECT cur_rem_tickets,book_status,book_nbr,active_tickets_upto FROM st_se_game_inv_status where book_nbr = ? and current_owner_id=? and book_status='ACTIVE' and game_id=? FOR UPDATE ) bb on aa.book_nbr=bb.book_nbr order by date desc limit 1";
		pstmt = con.prepareStatement(getlastRecQry);
		pstmt.setInt(1,bean.getUserOrgId());
		pstmt.setString(2, bean.getTicketNbr().substring(0, 10));
		pstmt.setString(3, bean.getTicketNbr().substring(0, 10));
		pstmt.setInt(4,bean.getUserOrgId());
		pstmt.setInt(5,bean.getGameId());
		rs = pstmt.executeQuery();
		return rs;
	}
	
	private String tktBeanIsValidUpdateTicketStatus(Connection con,ResultSet rs,DaoBean daoBean) throws SQLException, LMSException{
		if(updateSoldTicketStatusToActive(con,daoBean) <= 0){
			throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.ACTIVE_TICKET_ERROR_MESSAGE);
		}
		int isUpdated1 = insertIntoGameTicketInvHistory(con,rs,daoBean);
		int isUpdated2 = updateGameInvStatus(con,daoBean);
		String txnId = prepareDataAndInsertIntoTicketByTicketSaleTxn(con,daoBean);
		
		if(isUpdated1 > 0 && isUpdated2 > 0 && txnId != null && !"".equalsIgnoreCase(txnId)){
			con.commit();
			return txnId;
		}else{
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	
	}
	
	private int insertIntoGameTicketInvHistory(Connection con,ResultSet rs,DaoBean daoBean) throws SQLException{
		PreparedStatement pstmt1 = null;
		String insSoldTktHistory = "insert into st_se_game_ticket_inv_history (game_id, book_nbr, current_owner,  current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, active_tickets_upto,  sold_tickets, status) values(?,?,?,?,?,?,?,?,?,?,?) ";
		pstmt1 = con.prepareStatement(insSoldTktHistory);
		pstmt1.setInt(1, rs.getInt("game_id"));
		pstmt1.setString(2, rs.getString("book_nbr"));
		pstmt1.setString(3,rs.getString("current_owner"));
		pstmt1.setInt(4,rs.getInt("current_owner_id"));
		pstmt1.setTimestamp(5,new Timestamp(new Date().getTime()));
		pstmt1.setInt(6,daoBean.getUserOrgId());
		pstmt1.setInt(7,daoBean.getUserOrgId());
		pstmt1.setInt(8, rs.getInt("cur_rem_tickets")+1);
		pstmt1.setInt(9, rs.getInt("totalTkt")- rs.getInt("cur_rem_tickets")-rs.getInt("sold_tickets"));
		pstmt1.setInt(10, 0); 
		pstmt1.setString(11, "INACTIVE");
		
		return pstmt1.executeUpdate();
	}
	
	private int updateGameInvStatus(Connection con,DaoBean daoBean) throws SQLException{
		PreparedStatement pstmt1 = null;
		String query="UPDATE st_se_game_inv_status  SET cur_rem_tickets=cur_rem_tickets+1,active_tickets_upto=active_tickets_upto-1  where game_id=? and book_nbr=? and book_status='ACTIVE' and current_owner_id=?";
		pstmt1 = con.prepareStatement(query);
		pstmt1.setInt(1, daoBean.getGameId());
		pstmt1.setString(2, daoBean.getTicketNbr().substring(0,10));
		pstmt1.setInt(3, daoBean.getUserOrgId());
		return pstmt1.executeUpdate();
	}
	
	private String prepareDataAndInsertIntoTicketByTicketSaleTxn(Connection con,DaoBean daoBean) throws SQLException{
		Date date = new Date();
		String txnId = daoBean.getUserOrgId()+date.getTime()+"";
		daoBean.setDateTime(date);
		daoBean.setTxnId(txnId);
		daoBean.setStatus("UNSOLD");
		int isUpdated = insertTicketByTicketSaleTxn(con,daoBean);
		if(isUpdated > 0){
			return txnId;
		}
		return null;
	}
	
	private int updateSoldTicketStatusToActive(Connection con,DaoBean bean) throws SQLException{
		PreparedStatement pstmt = null;
		String query = "UPDATE st_se_pwt_inv_?  SET ticket_status='ACTIVE' WHERE id1=? AND ticket_status='SOLD'";
		pstmt = con.prepareStatement(query);
		pstmt.setInt(1, Integer.parseInt(bean.getTicketNbr().substring(0, 3)));
		pstmt.setString(2, MD5Encoder.encode(bean.getTicketNbr()));
		return  pstmt.executeUpdate();
	}
	
}
