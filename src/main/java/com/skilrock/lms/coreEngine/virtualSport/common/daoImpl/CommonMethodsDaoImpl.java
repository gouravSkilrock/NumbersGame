package com.skilrock.lms.coreEngine.virtualSport.common.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.beans.VSRegistrationDataBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.virtualSport.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSResponseBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;

public class CommonMethodsDaoImpl {
	private static final Logger logger = LoggerFactory.getLogger("CommonMethodsDaoImpl");

	private static CommonMethodsDaoImpl instance;

	private CommonMethodsDaoImpl() {
	}

	public static CommonMethodsDaoImpl getInstance() {
		if (instance == null) {
			synchronized (CommonMethodsDaoImpl.class) {
				if (instance == null) {
					instance = new CommonMethodsDaoImpl();
				}
			}
		}
		return instance;
	}

	public Map<Integer, GameMasterBean> getGameMap(Connection connection) {
		Map<Integer, GameMasterBean> gameInfoMap = new TreeMap<Integer, GameMasterBean>();
		GameMasterBean gameBean = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			query = "SELECT game_id, game_no, game_dev_name, game_disp_name,govt_comm_pwt_rate FROM st_vs_game_master WHERE game_status='SALE_OPEN';";
			logger.info("getGameMap Query - " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				gameBean = new GameMasterBean();
				gameBean.setGameId(rs.getInt("game_id"));
				gameBean.setGameNo(rs.getInt("game_no"));
				gameBean.setGameDevName(rs.getString("game_dev_name"));
				gameBean.setGameDispName(rs.getString("game_disp_name"));
				gameBean.setGovtClaimComm(rs.getDouble("govt_comm_pwt_rate"));
				gameInfoMap.put(rs.getInt("game_id"), gameBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gameInfoMap;
	}

	public Map<Integer, Map<Integer, List<MessageDetailsBean>>> getVSAdvMessageMap(Connection connection) {
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		List<MessageDetailsBean> tempList = null;
		MessageDetailsBean messageDetailsBean = null;
		Map<Integer, Map<Integer, List<MessageDetailsBean>>> orgMsgDetailMap = null;
		try {
			stmt = connection.createStatement();
			query = "SELECT advMap.org_id, advMap.game_id, advMas.msg_id, advMas.date, advMas.creator_user_id, advMas.msg_text, advMas.status, advMas.editable, advMas.msg_for, advMas.msg_location, advMas.activity FROM st_dg_adv_msg_org_mapping advMap INNER JOIN st_dg_adv_msg_master advMas ON advMap.msg_id=advMas.msg_id AND advMas.status='ACTIVE' AND advMas.activity IN ('SALE', 'PWT', 'ALL') AND advMas.msg_for='PLAYER' AND advMap.service_id=(SELECT service_id FROM st_lms_service_master WHERE service_code='VS') ORDER BY game_id,org_id;";
			logger.info("getIWAdvMessageMap Query - " + query);
			rs = stmt.executeQuery(query);
			orgMsgDetailMap = new HashMap<Integer, Map<Integer, List<MessageDetailsBean>>>();
			while (rs.next()) {
				messageDetailsBean = new MessageDetailsBean();
				messageDetailsBean.setMessageId(rs.getInt("msg_id"));
				messageDetailsBean.setDate(rs.getTimestamp("date"));
				messageDetailsBean.setCreatorUserId(rs.getInt("creator_user_id"));
				messageDetailsBean.setMessageText(rs.getString("msg_text"));
				messageDetailsBean.setStatus(rs.getString("status"));
				messageDetailsBean.setEditable(rs.getString("editable"));
				messageDetailsBean.setMessageFor(rs.getString("msg_for"));
				messageDetailsBean.setMessageLocation(rs.getString("msg_location"));
				messageDetailsBean.setActivity(rs.getString("activity"));

				int orgId = rs.getInt("org_id");
				int gameId = rs.getInt("game_id");

				if (orgMsgDetailMap.containsKey(orgId)) {
					Map<Integer, List<MessageDetailsBean>> gameMsgDetailMap = orgMsgDetailMap.get(orgId);
					if (gameMsgDetailMap.containsKey(gameId)) {
						gameMsgDetailMap.get(gameId).add(messageDetailsBean);
					} else {
						tempList = new ArrayList<MessageDetailsBean>();
						tempList.add(messageDetailsBean);
						gameMsgDetailMap.put(gameId, tempList);
					}
				} else {
					Map<Integer, List<MessageDetailsBean>> gameMsgDetailMap = new HashMap<Integer, List<MessageDetailsBean>>();
					tempList = new ArrayList<MessageDetailsBean>();
					tempList.add(messageDetailsBean);
					gameMsgDetailMap.put(gameId, tempList);
					orgMsgDetailMap.put(orgId, gameMsgDetailMap);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return orgMsgDetailMap;
	}

/**
	 * 
	 * @param userName
	 * @param conn
	 * @return resposnseString
	 * @throws VSException
	 * @author Rishi
	 */
	public VSRegistrationDataBean verifyAndFetchVSUser(String userName, Connection conn) throws VSException{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		VSRegistrationDataBean bean = null ;
		try{
			query = "Select rom.vs_shop_entity_id, rom.vs_printer_id, rom.vs_printer_entity_id, rom.vs_retailer_entiry_id, um.password From st_lms_ret_offline_master rom INNER JOIN st_lms_user_master um ON rom.user_id = um.user_id Where um.user_name='"+userName+"'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				/*if(rs.getString("vs_printer_id") == null || rs.getString("vs_printer_id").trim().length() < 1){
					throw new VSException(VSErrors.UNAUTHORIZED_VB_USER_ERROR_CODE,VSErrors.UNAUTHORIZED_VB_USER_ERROR_MESSAGE);
				}*/
				
				bean = new VSRegistrationDataBean.Builder().vsShopEntityId(rs.getString("vs_shop_entity_id"))
							.vsPrinterEntityId(rs.getString("vs_printer_entity_id")).vsPrinterId(rs.getString("vs_printer_id"))
								.vsRetailerEntityId(rs.getString("vs_retailer_entiry_id")).password(rs.getString("password")).build() ;
				
			} else {
				throw new VSException(VSErrors.UNAUTHORIZED_VB_USER_ERROR_CODE,VSErrors.UNAUTHORIZED_VB_USER_ERROR_MESSAGE);
			}
		} catch(VSException vb){
			throw vb;
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.INTERNAL_SYSTEM_ERROR_CODE, VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return bean;
	}
	
	/**
	 * 
	 * @param shopId
	 * @param conn
	 * @return credit
	 * @throws VSException
	 * @author Rishi
	 */
	public double fetchVSUserCredit(int unitId, Connection conn) throws VSException{
		double credit= 0.0;
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			query = "Select (om.available_credit - om.claimable_bal) available_bal From st_lms_organization_master om INNER JOIN st_lms_ret_offline_master rom ON om.organization_id = rom.organization_id Where rom.vs_printer_entity_id ="+unitId;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				credit = rs.getDouble("available_bal");
			} else {
				throw new VSException(VSErrors.NO_SHOP_FOUND_ERROR_CODE, VSErrors.NO_SHOP_FOUND_ERROR_MESSAGE);
			}
		} catch(VSException vs){
			throw vs;
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.INTERNAL_SYSTEM_ERROR_CODE, VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return credit;
	}
	
	public void updateVSShopId(String shopEntityId, int retailerId) throws LMSException {
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			int updateCount = stmt.executeUpdate("update st_lms_ret_offline_master set vs_shop_entity_id = '" + shopEntityId + "' where user_id = " + retailerId);
			if(updateCount == 0)
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}
	
	public void updateVSPrinterData(String printerId, String printerEntityId, int retailerId) throws LMSException {
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			int updateCount = stmt
					.executeUpdate("update st_lms_ret_offline_master set vs_printer_id = '" + printerId + "', vs_printer_entity_id = '" + printerEntityId + "' where user_id = " + retailerId);
			if(updateCount == 0)
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public void updateVsRetailerEntityId(String retailerEntityId, int retailerId) throws LMSException {
		Connection con = null;
		Statement stmt = null;

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			int updateCount = stmt
					.executeUpdate("update st_lms_ret_offline_master set vs_retailer_entiry_id = '" + retailerEntityId + "' where user_id = " + retailerId);
			if (updateCount == 0)
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public String fetchVsRetailerEntityId(int retailerId) throws LMSException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select vs_retailer_entiry_id from st_lms_ret_offline_master where user_id = " + retailerId);
			if (rs.next())
				return rs.getString("vs_retailer_entiry_id");
			else
				return null;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}
	public int fetchVsPrinterEntityId(int retailerId) throws LMSException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select vs_printer_entity_id from st_lms_ret_offline_master where user_id = " + retailerId);
			if (rs.next())
				return rs.getInt("vs_printer_entity_id");
			else
				return 0;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}
	
	/**
	 * 
	 * @param ticketNumber
	 * @param conn
	 * @return gameId
	 * @throws VSException
	 * @author Rishi
	 */
	public int verifyTktAndFetchGameId(String ticketNumber,Connection conn) throws VSException{
		int gameId = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try{
			query = "Select game_id from st_vs_ret_sale Where ticket_nbr = '"+ticketNumber+"'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				gameId = rs.getInt("game_id");
			} else{
				throw new VSException(VSErrors.INVALID_TICKET_NUMBER_ERROR_CODE, VSErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
		} catch (VSException ve) {
			throw ve;
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return gameId;
	}

	public Map<String, VSRequestBean> getPendingSaleTxns(VSRequestBean vsRequestBean, Connection conn, String status) throws VSException {
		
		Map<String, VSRequestBean> pendingSaleMap = null;
		Statement stmt = null ;
		ResultSet rs = null ;
		String query = null ;
		
		try{
			pendingSaleMap = new HashMap<String, VSRequestBean>();
			query = "select  mrp_amt, user_name, game_id, user_id, ticket_nbr, transaction_id lmsTxnId, engine_tx_id grTxnId, rs.status lmsStatus, 'N.A.' grStatus,transaction_date from st_vs_ret_sale rs inner join st_lms_user_master on organization_id = retailer_org_id   where rs.status = '"+status+"'" +
				" and transaction_date > '"+vsRequestBean.getStartDate()+"' and transaction_date < '"+vsRequestBean.getEndDate()+"' order by transaction_date desc;" ;
			stmt = conn.createStatement() ;
			rs = stmt.executeQuery(query) ;
			while(rs.next()){
				vsRequestBean = new VSRequestBean() ;
				vsRequestBean.setUserName(rs.getString("user_name"));
				vsRequestBean.setGameId(rs.getInt("game_id"));
				vsRequestBean.setUserId(rs.getInt("user_id"));
				vsRequestBean.setMrpAmt(rs.getDouble("mrp_amt"));
				vsRequestBean.setLmsTxnId(rs.getString("lmsTxnId"));
				vsRequestBean.setTxnId(rs.getString("grTxnId"));
				vsRequestBean.setLmsStatus(rs.getString("lmsStatus"));
				vsRequestBean.setGrStatus(rs.getString("grStatus"));
				vsRequestBean.setTransactionDate(rs.getTimestamp("transaction_date"));
				vsRequestBean.setTicketNumber(rs.getString("ticket_nbr"));
				pendingSaleMap.put(rs.getString("grTxnId"), vsRequestBean);
			}
			
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		
		return pendingSaleMap;
	}
}
