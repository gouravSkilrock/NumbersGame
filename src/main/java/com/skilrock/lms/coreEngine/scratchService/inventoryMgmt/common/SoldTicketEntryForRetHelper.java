package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.dao.InventoryDao;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.daoImpl.InventoryDaoImpl;
import com.skilrock.lms.rest.services.bean.DaoBean;

public class SoldTicketEntryForRetHelper {

	private static Log logger = LogFactory.getLog(SoldTicketEntryForRetHelper.class);
	private InventoryDao inventory;
	
	public SoldTicketEntryForRetHelper(){
		this.inventory = new InventoryDaoImpl();
	}
	public SoldTicketEntryForRetHelper(InventoryDao inventory){
		this.inventory = inventory;
	}
	
	public String fetchBooksDetails(int orgId) throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		StringBuilder strBuilder = new StringBuilder("");
		try {
			String fetchBooksDetQuery = "select aa.book_nbr, aa.cur_rem_tickets, bb.nbr_of_tickets_per_book, "
					+ "concat(bb.game_nbr, '-', game_name) 'game_name' from st_se_game_inv_status aa,st_se_game_master"
					+ " bb where aa.game_id = bb.game_id and (aa.book_status = 'ACTIVE' or aa.book_status = "
					+ "'CLAIMED') and aa.current_owner_id = ? and (aa.cur_rem_tickets>0 and aa.cur_rem_tickets "
					+ "is not null)";
			pstmt = con.prepareStatement(fetchBooksDetQuery);
			pstmt.setInt(1, orgId);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("getgameList");
			String preGameName = null;
			while (rs.next()) {
				String bookNbr = rs.getString("book_nbr");
				String gameName = rs.getString("game_name");
				int curRemTkt = rs.getInt("cur_rem_tickets");
				int nbrOfTktPerBook = rs.getInt("nbr_of_tickets_per_book");
				if (gameName != null && gameName.equalsIgnoreCase(preGameName)) {
					strBuilder.append(";").append(bookNbr).append("=").append(
							curRemTkt).append("=").append(nbrOfTktPerBook);
				} else {
					strBuilder.append(",").append(gameName).append(";").append(
							bookNbr).append("=").append(curRemTkt).append("=")
							.append(nbrOfTktPerBook);
					preGameName = gameName;
				}
			}
			// System.out.println(strBuilder.toString()+"--------");
			if (!"".equals(strBuilder.toString().trim())) {
				strBuilder.deleteCharAt(strBuilder.indexOf(","));
			}

			System.out.println("String is === " + strBuilder.toString());
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return strBuilder.toString();

	}

	/**
	 * This method is used for the Sold ticket entry from Terminal/Web by Retailer.
	 * @param bookNbr
	 * @param currRem
	 * @param userInfo
	 * @param updCurrRem
	 * @param tktInBook
	 * @return
	 * @throws LMSException
	 */

	public static boolean saveSoldTicketEntry(String[] bookNbr, String[] currRem,
			UserInfoBean userInfo, String[] updCurrRem, String tktInBook[])
			throws LMSException {
		logger.info(bookNbr.length + ", " + currRem.length + ", "+ updCurrRem.length + ",  " + tktInBook);
		/*System.out.println(bookNbr.length + ", " + currRem.length + ", "
				+ updCurrRem.length + ",  " + tktInBook);*/

		Connection con =null;
		PreparedStatement hisPstmt = null, statusPstmt = null,lastRecPstmt=null;
		ResultSet rs =null;
		String getlastRecQry ="select aa.game_id, aa.book_nbr, aa.current_owner, aa.current_owner_id, (bb.cur_rem_tickets-?) as sold_tickets , concat(bb.book_status, '_CLOSE') 'book_status' from (SELECT date,game_id, book_nbr,current_owner,current_owner_id FROM st_se_game_ticket_inv_history where current_owner_id=? and book_nbr = ? FOR UPDATE )aa inner join (SELECT cur_rem_tickets,book_status,book_nbr FROM st_se_game_inv_status where book_nbr = ? and current_owner_id=? FOR UPDATE ) bb on aa.book_nbr=bb.book_nbr order by date desc limit 1";
		String insSoldTktHistory = " insert into st_se_game_ticket_inv_history (game_id, book_nbr, current_owner,  current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, active_tickets_upto,  sold_tickets, status) values(?,?,?,?,?,?,?,?,?,?,?) ";
		
		
		/* String insSoldTktHistory = " insert into st_se_game_ticket_inv_history (game_id, book_nbr, current_owner,  current_owner_id, date, done_by_oid, " +
		"done_by_uid, cur_rem_tickets, active_tickets_upto, " 
		+ " sold_tickets, status) values(?,?,?,?,?,?,?,?,?,?,?) select aa.game_id, aa.book_nbr, aa.current_owner, aa.current_owner_id, ?, "
		+ userInfo.getUserOrgId()
		+ ", "
		+ userInfo.getUserId()
		+ ", ?, ?, (bb.cur_rem_tickets-?) as sold_tickets , concat(bb.book_status, '_CLOSE') 'book_status' from st_se_game_ticket_inv_history"
		+ " aa, st_se_game_inv_status bb where aa.book_nbr = bb.book_nbr and aa.current_owner_id="
		+ userInfo.getUserOrgId()
		+ " and aa.book_nbr = ? order by date desc limit 1";*/

		String updStatusTblQuery = " update st_se_game_inv_status set cur_rem_tickets = ?, active_tickets_upto = ?  where  current_owner_id=? and book_nbr = ?";
		try {
			con= DBConnect.getConnection();
			con.setAutoCommit(false);
			hisPstmt = con.prepareStatement(insSoldTktHistory);
			statusPstmt = con.prepareStatement(updStatusTblQuery);
			lastRecPstmt=con.prepareStatement(getlastRecQry);
			String updCurrRemTkt = null;
			for (int i = 0; i < updCurrRem.length; i++) {
				updCurrRemTkt = updCurrRem[i].trim();
				logger.info("updCurrRemBook = " + updCurrRemTkt);

				if (updCurrRemTkt != null && !"".equals(updCurrRemTkt.trim())) {
					
					lastRecPstmt.setInt(1,Integer.parseInt(updCurrRemTkt) );
					lastRecPstmt.setInt(2,userInfo.getUserOrgId());
					lastRecPstmt.setString(3, bookNbr[i].trim());
					lastRecPstmt.setString(4, bookNbr[i].trim());
					lastRecPstmt.setInt(5,userInfo.getUserOrgId());
					rs=lastRecPstmt.executeQuery();
					rs.last();
					int recs = rs.getRow();
					rs.beforeFirst();
					//more than one Record Fetched
					if(recs!=1){
					
						throw new LMSException(LMSErrors.MORE_THAN_ONE_RECORD_CODE,LMSErrors.MORE_THAN_ONE_RECORD_MESSAGE);
					
						}	
					if(rs.next()){
						
						
						// insert data into st_se_game_ticket_inv_history table
						hisPstmt.setInt(1, rs.getInt("game_id"));
						hisPstmt.setString(2, rs.getString("book_nbr"));
						hisPstmt.setString(3,rs.getString("current_owner"));
						hisPstmt.setInt(4,rs.getInt("current_owner_id"));
						hisPstmt.setTimestamp(5,new Timestamp(new Date().getTime()));
						hisPstmt.setInt(6,userInfo.getUserOrgId());
						hisPstmt.setInt(7,userInfo.getUserId());
						hisPstmt.setInt(8, Integer.parseInt(updCurrRemTkt));
						hisPstmt.setInt(9, Integer.parseInt(tktInBook[i].trim())-Integer.parseInt(updCurrRemTkt));
						hisPstmt.setInt(10, rs.getInt("sold_tickets")); // used
						hisPstmt.setString(11, rs.getString("book_status"));
						// to
						// calculate
						// sold
						// tickethisPstmt.setString(5, bookNbr[i]);
						
						
					}
					

					// update st_se_game_inv_status table
					statusPstmt.setInt(1, Integer.parseInt(updCurrRemTkt));
					statusPstmt.setInt(2, Integer.parseInt(tktInBook[i].trim())-Integer.parseInt(updCurrRemTkt));
					statusPstmt.setInt(3,userInfo.getUserOrgId() );
					statusPstmt.setString(4, bookNbr[i].trim());
					logger.debug("hisPstmt== " + hisPstmt+ "\n statusPstmt = " + statusPstmt);
					/*System.out.println("hisPstmt== " + hisPstmt
							+ "\n statusPstmt = " + statusPstmt);*/
					hisPstmt.executeUpdate();
					statusPstmt.executeUpdate();
				}

			}
			con.commit();
			return true;
		} catch (SQLException e) {
			logger.error("SQL Exception",e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(lastRecPstmt);
			DBConnect.closePstmt(statusPstmt);
			DBConnect.closePstmt(hisPstmt);
			DBConnect.closeCon(con);
			
		}

	}

	
	public String updateSellTicketStatus(String ticketNbr,UserInfoBean userInfoBean,String tpTransactionId) throws LMSException{
		Connection con=null;
		PreparedStatement pstmt=null;
		PreparedStatement pstmt1=null;
		ResultSet rs=null;
		String query = null;
		int gameId=0;
		int ticketPrice=0;
		try {
			con = DBConnect.getConnection();
			query = "SELECT game_id,ticket_price from st_se_game_master WHERE game_nbr=?";

			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(ticketNbr.substring(0, 3)));
			rs = pstmt.executeQuery();

			if (rs.next()) {
				gameId = rs.getInt("game_id");
				ticketPrice=rs.getInt("ticket_price");
			}else{
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
			pstmt.clearParameters();
			
			TicketBean tktBean = new CommonFunctionsHelper().isTicketFormatValid(ticketNbr,gameId, con);
			ticketNbr = tktBean.getTicketNumber();
			if (tktBean.getIsValid()) {
				String getlastRecQry ="select aa.game_id, aa.book_nbr, aa.current_owner, aa.current_owner_id,(cur_rem_tickets+active_tickets_upto) totalTkt ,cur_rem_tickets, 1 as sold_tickets , concat(bb.book_status, '_CLOSE') 'book_status' from (SELECT date,game_id, book_nbr,current_owner,current_owner_id FROM st_se_game_ticket_inv_history where current_owner_id=? and book_nbr = ? FOR UPDATE )aa inner join (SELECT cur_rem_tickets,book_status,book_nbr,active_tickets_upto FROM st_se_game_inv_status where book_nbr = ? and current_owner_id=? and (book_status='ACTIVE' OR  book_status='CLAIMED')  and game_id=? FOR UPDATE ) bb on aa.book_nbr=bb.book_nbr order by date desc limit 1";
				pstmt = con.prepareStatement(getlastRecQry);
				pstmt.setInt(1,userInfoBean.getUserOrgId());
				pstmt.setString(2, ticketNbr.substring(0, 10));
				pstmt.setString(3, ticketNbr.substring(0, 10));
				pstmt.setInt(4,userInfoBean.getUserOrgId());
				pstmt.setInt(5,gameId);
				rs=pstmt.executeQuery();
				
				if(rs.next()){
					con.setAutoCommit(false);
					
					query="UPDATE st_se_pwt_inv_?  SET ticket_status='SOLD' WHERE id1=? AND ticket_status='ACTIVE'";
					pstmt1 = con.prepareStatement(query);
					pstmt1.setInt(1, Integer.parseInt(ticketNbr.substring(0, 3)));
					pstmt1.setString(2, MD5Encoder.encode(ticketNbr));
					int isUpdated=pstmt1.executeUpdate();
					if(isUpdated<=0){
						throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.ACTIVE_TICKET_ERROR_MESSAGE);
					}
					
					
					String insSoldTktHistory = "insert into st_se_game_ticket_inv_history (game_id, book_nbr, current_owner,  current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, active_tickets_upto,  sold_tickets, status) values(?,?,?,?,?,?,?,?,?,?,?) ";
					
					pstmt1=con.prepareStatement(insSoldTktHistory);
					pstmt1.setInt(1, rs.getInt("game_id"));
					pstmt1.setString(2, rs.getString("book_nbr"));
					pstmt1.setString(3,rs.getString("current_owner"));
					pstmt1.setInt(4,rs.getInt("current_owner_id"));
					pstmt1.setTimestamp(5,new Timestamp(new Date().getTime()));
					pstmt1.setInt(6,userInfoBean.getUserOrgId());
					pstmt1.setInt(7,userInfoBean.getUserOrgId());
					pstmt1.setInt(8, rs.getInt("cur_rem_tickets")-rs.getInt("sold_tickets"));
					pstmt1.setInt(9, rs.getInt("totalTkt")- (rs.getInt("cur_rem_tickets")-rs.getInt("sold_tickets")));
					pstmt1.setInt(10, rs.getInt("sold_tickets")); 
					pstmt1.setString(11, rs.getString("book_status"));
					
					int isUpdated1=pstmt1.executeUpdate();
					
					
					query="UPDATE st_se_game_inv_status  SET cur_rem_tickets=cur_rem_tickets-1,active_tickets_upto=active_tickets_upto+1  where game_id=? and book_nbr=? and (book_status='ACTIVE' OR  book_status='CLAIMED') and current_owner_id=? and cur_rem_tickets <> 0 ";
					pstmt1 = con.prepareStatement(query);
					pstmt1.setInt(1, gameId);
					pstmt1.setString(2, ticketNbr.substring(0,10));
					pstmt1.setInt(3, userInfoBean.getUserOrgId());
					
					int isUpdated2 = pstmt1.executeUpdate();
					
					Date date = new Date();
					String txnId = userInfoBean.getUserOrgId()+date.getTime()+"";
					
					DaoBean daoBean = new DaoBean();
					daoBean.setDateTime(date);
					daoBean.setTpTransactionId(tpTransactionId);
					daoBean.setTicketNbr(ticketNbr);
					daoBean.setTxnId(txnId);
					daoBean.setUserId(userInfoBean.getUserId());
					daoBean.setUserOrgId(userInfoBean.getUserOrgId());
					daoBean.setStatus("SOLD");
					daoBean.setGameId(gameId);
					daoBean.setTicketPrice(ticketPrice);
					
					
					int isUpdated3 = getInventory().insertTicketByTicketSaleTxn(con,daoBean);
					
					if(isUpdated1>0 && isUpdated2>0 && isUpdated3>0){
						con.commit();
						return txnId;
					}else{
						throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}
				}else{
					throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE_SELL_TICKET);
				}
			
			}else{
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE_SELL_TICKET);
			}
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
			DBConnect.closeConnection(con, pstmt, rs);
			DBConnect.closePstmt(pstmt1);
		}
		
	}

	public InventoryDao getInventory() {
		return inventory;
	}

	

}
