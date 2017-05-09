package com.skilrock.lms.coreEngine.virtualSport.playMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPSaleRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPTxRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class VirtualSportGamePlayDaoImpl {
	private static VirtualSportGamePlayDaoImpl classInstance = null;
	private static Logger logger = LoggerFactory
			.getLogger(VirtualSportGamePlayDaoImpl.class);

	public static VirtualSportGamePlayDaoImpl getInstance() {
		if (classInstance == null)
			classInstance = new VirtualSportGamePlayDaoImpl();
		return classInstance;
	}

	/**
	 * 
	 * @param TPSaleRequestBean
	 * @param UserInfoBean
	 * @param Connection
	 * @author Nikhil K. Bansal
	 */
	public static synchronized long virtualBettingPurchaseTicket(TPSaleRequestBean gamePlayBean, UserInfoBean userBean,Connection con) throws VSException, LMSException, SQLException {
		logger.info("inside instantWinPurchaseTicketDaoImpl...");
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement insertPstmt = null;
		ResultSet insertRs = null;
		ResultSet rsTrns = null;
		long transId = 0;
		try {
			double vat = 0.0;
			double govt_comm = 0.0;
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double prize_payout_ratio = 0.0;
			double ticketMrp = gamePlayBean.getAmount();
			boolean isValid = false;
			double retNet=0.0;

			stmt = con.createStatement();

			rs = stmt.executeQuery("select SQL_CACHE vat_amt, gov_comm_rate, prize_payout_ratio from st_vs_game_master where game_id = "+ gamePlayBean.getGameId());
			if (rs.next()) {
				govt_comm = rs.getDouble("gov_comm_rate");
				vat = rs.getDouble("vat_amt");
				prize_payout_ratio = rs.getDouble("prize_payout_ratio");
			} else {
				throw new VSException(VSErrors.GAME_NOT_AVAILABLE_ERROR_CODE,VSErrors.GAME_NOT_AVAILABLE_ERROR_MESSAGE);
			}

			retCommRate = Util.getVSSaleCommVariance(userBean.getUserOrgId(),gamePlayBean.getGameId());
			agtCommRate=Util.getVSSaleCommVariance(userBean.getParentOrgId(),gamePlayBean.getGameId());
			

			if (gamePlayBean.getAmount() > 0) {
				// check with ACA
				// if online sale amt > ACA then return ERROR
				pstmt = con.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal, organization_status from st_lms_organization_master where organization_id=?");
				pstmt.setInt(1, userBean.getUserOrgId());
				rsTrns = pstmt.executeQuery();
				if (rsTrns.next()) {
					if (!"ACTIVE".equals(rsTrns.getString("organization_status"))) {
						logger.info(VSErrors.INACTIVE_RETAILER_ERROR_MESSAGE);
						throw new VSException(VSErrors.INACTIVE_RETAILER_ERROR_CODE,VSErrors.INACTIVE_RETAILER_ERROR_MESSAGE);
					}
					gamePlayBean.setRetBalanceBeforeSale(rsTrns.getDouble("availbale_sale_bal"));
					if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp- ticketMrp * retCommRate * .01)) {
						logger.info(VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
						throw new VSException(VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
					}
				} else {
					throw new VSException(VSErrors.INVALID_USER_NAME_CODE,VSErrors.INVALID_USER_NAME_MESSAGE);
				}

				// check for agents ACA and claimable balance
				if (!"GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))) {
					pstmt = con
							.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal, organization_status from st_lms_organization_master where organization_id=?");
					pstmt.setInt(1, userBean.getParentOrgId());
					rsTrns = pstmt.executeQuery();
					// check for >= ?? needs to be confirmed
					if (rsTrns.next()) {
						if (!"ACTIVE".equals(rsTrns.getString("organization_status"))) {
							logger.info(VSErrors.INACTIVE_AGENT_ERROR_MESSAGE);
							throw new VSException(VSErrors.INACTIVE_AGENT_ERROR_CODE,VSErrors.INACTIVE_AGENT_ERROR_MESSAGE);
						}

						if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp- ticketMrp * agtCommRate * .01)) {
							logger.info(VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
							throw new VSException(VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE,VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
						}
					} else {
						throw new VSException(VSErrors.INVALID_USER_NAME_CODE,VSErrors.INVALID_USER_NAME_MESSAGE);
					}
				}
			}

			insertPstmt = con.prepareStatement(	"INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)",
			PreparedStatement.RETURN_GENERATED_KEYS);
			insertPstmt.setString(1, userBean.getUserType());
			insertPstmt.setString(2, "VS");
			insertPstmt.setString(3, "WEB");
			insertPstmt.executeUpdate();
			insertRs = insertPstmt.getGeneratedKeys();

			if (insertRs.next()) {
				transId = insertRs.getLong(1);

				double saleCommRate = 0.0;
				double goodCauseAmt = 0.0;

				// calculate vat
				double vatAmount = CommonMethods.calculateDrawGameVatPlr(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm,vat);
				double taxableSale = CommonMethods.calTaxableSale(ticketMrp,saleCommRate, prize_payout_ratio, govt_comm, vat);

				retNet = CommonMethods.fmtToTwoDecimal(ticketMrp)- CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate* .01);
				double agtNet = CommonMethods.fmtToTwoDecimal(ticketMrp)- CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate* .01);
				goodCauseAmt = CommonMethods.fmtToTwoDecimal(ticketMrp* govt_comm * .01);

				// insert into retailer transaction master
				insertPstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				insertPstmt.setLong(1, transId);
				insertPstmt.setInt(2, userBean.getUserId());
				insertPstmt.setInt(3, userBean.getUserOrgId());
				insertPstmt.setInt(4, gamePlayBean.getGameId());
				insertPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
				insertPstmt.setString(6, "VS_SALE");
				insertPstmt.executeUpdate();

				
				// insert into retailer sale table
				insertPstmt = con.prepareStatement("insert into st_vs_ret_sale(transaction_id, engine_tx_id, game_id, retailer_org_id,mrp_amt, retailer_comm_amt, retailer_net_amt, agent_comm_amt, agent_net_amt, good_cause_amt, vat_amt, taxable_sale,claim_status, transaction_date, is_cancel,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				insertPstmt.setLong(1, transId);
				insertPstmt.setString(2, gamePlayBean.getTmpId());
				insertPstmt.setInt(3, gamePlayBean.getGameId());
				insertPstmt.setInt(4, userBean.getUserOrgId());
				insertPstmt.setDouble(5, CommonMethods.fmtToTwoDecimal(ticketMrp));
				insertPstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate * .01));
				insertPstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(retNet));
				insertPstmt.setDouble(8, CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate * .01));
				insertPstmt.setDouble(9, CommonMethods.fmtToTwoDecimal(agtNet));
				insertPstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(goodCauseAmt));
				insertPstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(vatAmount));
				insertPstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(taxableSale));
				insertPstmt.setString(13, "DONE_CLAIM");
				insertPstmt.setTimestamp(14, Util.getCurrentTimeStamp());
				insertPstmt.setString(15, "N");
				insertPstmt.setString(16, "PENDING");
				insertPstmt.executeUpdate();
				
				

				// Now make payment update method only one
				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(retNet, "TRANSACTION", "VS_SALE",userBean.getUserOrgId(), userBean.getParentOrgId(),"RETAILER", 0, con);
				if (!isValid) {
					throw new VSException(	VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agtNet, "TRANSACTION", "VS_SALE",	userBean.getParentOrgId(), 0, "AGENT", 0, con);
				if (!isValid) {
					throw new VSException(VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE,VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
			} else {
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			gamePlayBean.setRetBalanceAfterSale(gamePlayBean.getRetBalanceBeforeSale()-retNet);
		} catch(VSException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closeConnection(insertPstmt, rsTrns);
			DBConnect.closeConnection(pstmt, insertRs);
		}
		return transId;
	}
	
	
	
	public static synchronized long virtualBettingRefundTicket(TPSaleRequestBean gamePlayBean, UserInfoBean userBean,Connection con) throws VSException, LMSException, SQLException {
		logger.info("Inside VS Refund Dao Impl");
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement insertPstmt = null;
		ResultSet insertRs = null;
		long transId = 0;
		try {
			double vat = 0.0;
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double ticketMrp = 0.0;
			double goodCauseAmt = 0.0;
			boolean isValid = false;
			double retNet = 0.0;
			double agtNet = 0.0;
			double taxableSale = 0.0;

			stmt = con.createStatement();
			String tktNbr = "";
			String engTxnId = "" ;
			long refTransId = 0;
			rs = stmt.executeQuery("select transaction_id, engine_tx_id, mrp_amt, vat_amt, good_cause_amt, retailer_comm_amt, agent_comm_amt, ticket_nbr,retailer_net_amt,agent_net_amt,taxable_sale from st_vs_ret_sale where "+("0".equalsIgnoreCase(gamePlayBean.getTicketNumber()) ? "engine_tx_id = '"+gamePlayBean.getTxnId()+"';" : " ticket_nbr = '"+gamePlayBean.getTicketNumber()+"';"));
			if (rs.next()) {
				tktNbr = rs.getString("ticket_nbr");
				refTransId = rs.getLong("transaction_id");
				engTxnId = rs.getString("engine_tx_id");
				ticketMrp = rs.getDouble("mrp_amt");
				vat = rs.getDouble("vat_amt");
				goodCauseAmt = rs.getDouble("good_cause_amt");
				retCommRate = rs.getDouble("retailer_comm_amt");
				agtCommRate=rs.getDouble("agent_comm_amt");
				retNet = rs.getDouble("retailer_net_amt");
				agtNet = rs.getDouble("agent_net_amt");
				taxableSale = rs.getDouble("taxable_sale");
			}

			insertPstmt = con.prepareStatement(	"INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)",
			PreparedStatement.RETURN_GENERATED_KEYS);
			insertPstmt.setString(1, userBean.getUserType());
			insertPstmt.setString(2, "VS");
			insertPstmt.setString(3, "WEB");
			insertPstmt.executeUpdate();
			insertRs = insertPstmt.getGeneratedKeys();

			if (insertRs.next()) {
				transId = insertRs.getLong(1);
				insertPstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				insertPstmt.setLong(1, transId);
				insertPstmt.setInt(2, userBean.getUserId());
				insertPstmt.setInt(3, userBean.getUserOrgId());
				insertPstmt.setInt(4, gamePlayBean.getGameId());
				insertPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
				insertPstmt.setString(6, "VS_SALE_REFUND");
				insertPstmt.executeUpdate();
				
					pstmt = con.prepareStatement("insert into st_vs_ret_sale_refund(transaction_id, engine_tx_id, game_id, retailer_org_id, " +
							"ticket_nbr, mrp_amt, retailer_comm_amt, retailer_net_amt, agent_comm_amt, agent_net_amt, good_cause_amt," +
							" vat_amt, taxable_sale, cancellation_charges, claim_status, sale_ref_transaction_id, transaction_date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

					pstmt.setLong(1, transId);
				pstmt.setString(2, engTxnId);
					pstmt.setInt(3, gamePlayBean.getGameId());
					pstmt.setInt(4, userBean.getUserOrgId());
					pstmt.setString(5, tktNbr);
					pstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(ticketMrp));
					pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(retCommRate));
					pstmt.setDouble(8, CommonMethods.fmtToTwoDecimal(retNet));
					pstmt.setDouble(9, CommonMethods.fmtToTwoDecimal(agtCommRate));
					pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(agtNet));
					pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(goodCauseAmt));
					pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(vat));
					pstmt.setDouble(13, CommonMethods.fmtToTwoDecimal(taxableSale));
					pstmt.setDouble(14, 0);
					pstmt.setString(15, "DONE_CLAIM");
					pstmt.setLong(16, refTransId);
					pstmt.setTimestamp(17, Util.getCurrentTimeStamp());
					pstmt.executeUpdate();
				
				failTxnStatus(engTxnId, con);

				// Now make payment update method only one
				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(retNet, "TRANSACTION", "VS_CANCEL",userBean.getUserOrgId(), userBean.getParentOrgId(),"RETAILER", 0, con);
				if (!isValid) {
					throw new VSException(	VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agtNet, "TRANSACTION", "VS_CANCEL",	userBean.getParentOrgId(), 0, "AGENT", 0, con);
				if (!isValid) {
					throw new VSException(VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE,VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
			} else {
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			//balance = CommonMethodsDaoImpl.getInstance().fetchVSUserCredit(gamePlayBean.getUnitId(), con);
			gamePlayBean.setRetBalanceAfterSale(userBean.getAvailableCreditLimit()-userBean.getClaimableBal());
			gamePlayBean.setRetBalanceBeforeSale(gamePlayBean.getRetBalanceAfterSale()-retNet);
			
		} catch(VSException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closePstmt(insertPstmt);
			DBConnect.closeConnection(pstmt, insertRs);
		}
		return transId;
	}

	public String getUserNameFromRetPrinterId(int printerId, Connection con)
			throws VSException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String userName = null;
		try {
			stmt = con.createStatement();
			query = "SELECT user_name FROM st_lms_user_master um INNER JOIN  st_lms_ret_offline_master rom on um.user_id=rom.user_id WHERE rom.vs_printer_entity_id="+printerId+" or vs_retailer_entiry_id="+printerId;
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				userName = rs.getString("user_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return userName;
	}
	
	public void updateTicketInfo(TPTxRequestBean reqBean, Connection con) throws VSException{
		PreparedStatement pStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		int isUpdated = 0;
		PreparedStatement pstmt=null;
		long lmsTransactionId=0;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select transaction_id,status from st_vs_ret_sale where engine_tx_id = "+reqBean.getEngineTxId());
			if(rs.next()){
				lmsTransactionId=rs.getLong("transaction_id");
				if("PENDING".equalsIgnoreCase(rs.getString("status"))){
					pStmt = con.prepareStatement("update st_vs_ret_sale set ticket_nbr = ?, status = ?  where engine_tx_id = ?");
					pStmt.setString(1, reqBean.getTicketNumber());
					pStmt.setString(2, "DONE");
					pStmt.setString(3, reqBean.getEngineTxId());
					isUpdated = pStmt.executeUpdate();
					if (isUpdated == 0){
						throw new VSException(VSErrors.INVALID_TICKET_NUMBER_ERROR_CODE, VSErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
					}
				}
			}
			
			if(reqBean.getEventInfoArray()!=null && reqBean.getEventInfoArray().size()>0){
				String insertTicketInfo="INSERT INTO st_vs_game_ticket_info (lms_trans_id,event_id,event_type,event_start_date,event_end_date,estimated_max_win,event_selection_info) VALUES (?,?,?,?,?,?,?);";
				pstmt=con.prepareStatement(insertTicketInfo);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(int i=0;i<reqBean.getEventInfoArray().size();i++){
					JsonObject js=reqBean.getEventInfoArray().get(i).getAsJsonObject();
					pstmt.setLong(1, lmsTransactionId);
					pstmt.setLong(2, js.get("event_id").getAsLong());
					pstmt.setString(3, js.get("game").getAsString());
					pstmt.setString(4,sdf.format(new Date(js.get("start").getAsLong()*1000)));
					pstmt.setString(5,sdf.format(new Date(js.get("end").getAsLong()*1000)));
					pstmt.setDouble(6, reqBean.getEstimatedMaxWin());
					pstmt.setString(7, js.get("selections").getAsJsonArray().toString());
					pstmt.executeUpdate();
					pstmt.clearParameters();
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(pStmt, stmt, rs);
			DBConnect.closePstmt(pstmt);
		}
	}
	
	public static void failTxnStatus(String engineTransId, Connection con) throws VSException{
		PreparedStatement pStmt = null;
		int isUpdated = 0;
		try {
			pStmt = con.prepareStatement("update st_vs_ret_sale set is_cancel = ?,status= ? where engine_tx_id = ?");
			pStmt.setString(1, "Y");
			pStmt.setString(2, "FAILED");
			pStmt.setString(3, engineTransId);
			isUpdated = pStmt.executeUpdate();

			if (isUpdated == 0)
				throw new VSException(VSErrors.INVALID_TICKET_NUMBER_ERROR_CODE, VSErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
		} catch(VSException ve){
			ve.printStackTrace();
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(pStmt);
		}
	}

}
