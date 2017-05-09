package com.skilrock.lms.coreEngine.drawGames.pwtMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetPWTProcessHelper {

	static Log logger = LogFactory.getLog(RetPWTProcessHelper.class);

	public Long retDirPlrPwtPayment(int userId, int userOrgId, int parentOrgId,
			int gameNbr, int gameId, boolean isAutoScrap, double pwtAmt,
			int drawId, int playerId, double taxAmt, double netAmt,
			String paymentType, String chqNbr, Date chqDate, String draweeBank,
			String issuingPartyName, String ticketNbr, Connection connection,
			Object panelId, String schemeType, int taskId) throws LMSException {

		try {

			// insert data into main transaction master
			logger.debug("insert data into transaction master ");
			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = connection
					.prepareStatement(transMasQuery);
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				long transId = rs.getLong(1);

				CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				// insert into retailer transaction master
				String retTransMasterQuery = "insert into  st_lms_retailer_transaction_master ( transaction_id , retailer_user_id , retailer_org_id ,game_id, transaction_date , transaction_type ) values (?,?,?,?,?,?)";
				logger.debug("retTransMasterQuery = " + retTransMasterQuery);
				pstmt = connection.prepareStatement(retTransMasterQuery);
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, userOrgId);
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(6, "DG_PWT_PLR");
				pstmt.executeUpdate();
				logger.debug("insert into retailer transaction master = "
						+ pstmt);

				// fetch Agent And Retailer PWT commission
				double agtComm = CommonFunctionsHelper
						.fetchDGCommOfOrganization(gameId, parentOrgId, "PWT",
								"AGENT", connection);
				double retComm = CommonFunctionsHelper
						.fetchDGCommOfOrganization(gameId, userOrgId, "PWT",
								"RETAILER", connection);

				// direct player PWT payment
				String directPlrPayment = "insert into st_dg_ret_direct_plr_pwt (retailer_user_id, "
						+ "retailer_org_id, draw_id, transaction_id, transaction_date, game_id, player_id,"
						+ " pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank,"
						+ " issuing_party_name, pwt_claim_status, retailer_claim_comm, agt_claim_comm, panel_id,task_id)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
				pstmt = connection.prepareStatement(directPlrPayment);
				pstmt.setInt(1, userId);
				pstmt.setInt(2, userOrgId);
				pstmt.setInt(3, drawId);
				pstmt.setLong(4, transId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setInt(6, gameId);
				pstmt.setInt(7, playerId);
				pstmt.setDouble(8, pwtAmt);
				pstmt.setDouble(9, taxAmt);
				pstmt.setDouble(10, netAmt);
				pstmt.setString(11, paymentType);

				if ("cash".equalsIgnoreCase(paymentType)
						|| "TPT".equalsIgnoreCase(paymentType)) {
					pstmt.setObject(12, null);
					pstmt.setObject(13, null);
					pstmt.setObject(14, null);
					pstmt.setObject(15, null);
				} else if ("cheque".equalsIgnoreCase(paymentType)) {
					pstmt.setString(12, chqNbr);
					pstmt.setDate(13, chqDate);
					pstmt.setString(14, draweeBank);
					pstmt.setString(15, issuingPartyName);
				}

				pstmt.setString(16, isAutoScrap ? "CLAIM_BAL" : "UNCLAIM_BAL");
				pstmt.setDouble(17, pwtAmt * .01 * retComm);
				pstmt.setDouble(18, pwtAmt * .01 * agtComm);
				pstmt.setObject(19, panelId);
				pstmt.setInt(20, taskId);
				pstmt.executeUpdate();
				logger.debug("insert into st_dg_ret_direct_pwt = " + pstmt);

				// update ticket details into st_dg_pwt_inv_? table
				String insIntoDGPwtInvQuery = null;
				if ("DRAW_WISE".equalsIgnoreCase(schemeType.trim())) {
					insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  retailer_trn_id = ? "
							+ " where ticket_nbr = ? and draw_id = ?";
				} else {
					insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  retailer_trn_id = ? "
							+ " where ticket_nbr = ? and draw_id = ? and panel_id="
							+ panelId;
				}
				PreparedStatement insIntoDGPwtInvPstmt = connection
						.prepareStatement(insIntoDGPwtInvQuery);
				insIntoDGPwtInvPstmt.setInt(1, gameNbr);
				insIntoDGPwtInvPstmt.setString(2,
						isAutoScrap ? "CLAIM_PLR_RET_CLM_DIR"
								: "CLAIM_PLR_RET_UNCLM_DIR");
				insIntoDGPwtInvPstmt.setLong(3, transId);
				insIntoDGPwtInvPstmt.setString(4, ticketNbr);
				insIntoDGPwtInvPstmt.setInt(5, drawId);
				insIntoDGPwtInvPstmt.executeUpdate();
				logger.debug("insIntoDGPwtInvPstmt = " + insIntoDGPwtInvPstmt);

				// update retailer UNCLAIM_BAL payment
				if (isAutoScrap) {
					
					//Now make payment updte method only one
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt + pwtAmt
							* .01 * retComm, "CLAIM_BAL", "DEBIT", userOrgId, parentOrgId, "RETAILER", 0, connection);
					
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt + pwtAmt
							* .01 * agtComm, "CLAIM_BAL", "DEBIT", parentOrgId,0, "AGENT", 0, connection);
					
					
					
					/*// now retailer claimable balance DEBITED
					commHelper.updateOrgBalance("CLAIM_BAL", pwtAmt + pwtAmt
							* .01 * retComm, userOrgId, "DEBIT", connection);
					// agent claimable balance DEBITED
					commHelper.updateOrgBalance("CLAIM_BAL", pwtAmt + pwtAmt
							* .01 * agtComm, parentOrgId, "DEBIT", connection);*/
				} else {
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt, "UNCLAIM_BAL", "CREDIT",
	                           userOrgId,parentOrgId, "RETAILER", 0, connection);
					/*commHelper.updateOrgBalance("UNCLAIM_BAL", pwtAmt,
							userOrgId, "CREDIT", connection);*/
				}

				// receipt entries are required to be inserted into receipt
				// table
				return transId;

			} else {
				throw new LMSException(
						"no data insert into main transaction master");
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	public long retPwtPayment(int userId, int userOrgId, int parentOrgId,
			 int gameId, boolean isAutoScrap, double pwtAmt,
			int drawId, Object panelId, String ticketNbr,
			Connection connection, boolean isAgent,boolean isGovtTaxDeduct) throws LMSException {
		logger.debug("auto scrap is **********8 " + isAutoScrap);
		try {
			Double fmtPwtAmt=CommonMethods.fmtToTwoDecimal(pwtAmt);
			// insert data into main transaction master
			logger.debug("insert data into transaction master ");
			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = connection
					.prepareStatement(transMasQuery);
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				long transId = rs.getLong(1);

				CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				// insert into retailer transaction master
				String retTransMasterQuery = "insert into  st_lms_retailer_transaction_master ( transaction_id , retailer_user_id , retailer_org_id ,game_id, transaction_date , transaction_type ) values (?,?,?,?,?,?)";
				logger.debug("retTransMasterQuery = " + retTransMasterQuery);
				pstmt = connection.prepareStatement(retTransMasterQuery);
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, userOrgId);
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(6, "DG_PWT");
				pstmt.executeUpdate();
				logger.debug("insert into retailer transaction master = "
						+ pstmt);

				GameMasterLMSBean gameMasterLMSBean =Util.getGameMasterLMSBean(gameId);
				// fetch Agent And Retailer PWT commission
				double agtComm = CommonFunctionsHelper
						.fetchDGCommOfOrganization(gameId, parentOrgId, "PWT",
								"AGENT", connection);
				double retComm = CommonFunctionsHelper
						.fetchDGCommOfOrganization(gameId, userOrgId, "PWT",
								"RETAILER", connection);
				
				double govtCommPwt =0;
				if(isGovtTaxDeduct)
					govtCommPwt = gameMasterLMSBean.getGovtCommPwt();
				
				// insert into st_retailer_pwt when comes pwtAmt<Aproval
				// required
				String retPwtEntry = "insert into  st_dg_ret_pwt_"
						+ gameId
						+ " ( retailer_user_id , "
						+ " retailer_org_id , draw_id , game_id , transaction_id , pwt_amt , retailer_claim_comm ,"
						+ " agt_claim_comm,govt_claim_comm, status, panel_id) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
				pstmt = connection.prepareStatement(retPwtEntry);
				pstmt.setInt(1, userId);
				pstmt.setInt(2, userOrgId);
				pstmt.setInt(3, drawId);
				pstmt.setInt(4, gameId);
				pstmt.setLong(5, transId);
				pstmt.setDouble(6, fmtPwtAmt);
				pstmt.setDouble(7, fmtPwtAmt * .01 * retComm);
				pstmt.setDouble(8, fmtPwtAmt * .01 * agtComm);
				pstmt.setDouble(9, fmtPwtAmt * .01 * govtCommPwt);
				pstmt.setString(10, isAutoScrap ? "CLAIM_BAL" : "UNCLAIM_BAL");
				pstmt.setObject(11, panelId);
				logger.info("insert into st_dg_ret_pwt = " + pstmt);
				pstmt.executeUpdate();
				

				// insert ticket details into st_dg_pwt_inv_? table
				String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id, pwt_amt, "
						+ " status, retailer_transaction_id, is_direct_plr, panel_id) values (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement insIntoDGPwtInvPstmt = connection
						.prepareStatement(insIntoDGPwtInvQuery);
				insIntoDGPwtInvPstmt.setInt(1, gameId);
				insIntoDGPwtInvPstmt.setString(2, ticketNbr);
				insIntoDGPwtInvPstmt.setInt(3, drawId);
				insIntoDGPwtInvPstmt.setDouble(4, fmtPwtAmt);
				if (isAgent) {
					insIntoDGPwtInvPstmt.setString(5, "CLAIM_AGT");
				} else {
					insIntoDGPwtInvPstmt.setString(5,
							isAutoScrap ? "CLAIM_PLR_RET_CLM"
									: "CLAIM_PLR_RET_UNCLM");
				}
				insIntoDGPwtInvPstmt.setLong(6, transId);
				insIntoDGPwtInvPstmt.setString(7, "N");
				insIntoDGPwtInvPstmt.setObject(8, panelId);
				insIntoDGPwtInvPstmt.executeUpdate();
				logger.debug("insIntoDGPwtInvPstmt = " + insIntoDGPwtInvPstmt);

				// update retailer UNCLAIM_BAL payment
				if (isAutoScrap) {
					// now retailer claimable balance DEBITED
					
					//Now make payment updte method only one
					OrgCreditUpdation.updateOrganizationBalWithValidate(fmtPwtAmt + fmtPwtAmt
							* .01 * retComm - (fmtPwtAmt
									* .01 * govtCommPwt), "CLAIM_BAL", "DEBIT", userOrgId, parentOrgId, "RETAILER", 0, connection);
					
					OrgCreditUpdation.updateOrganizationBalWithValidate(fmtPwtAmt + fmtPwtAmt
							* .01 * agtComm - (fmtPwtAmt
									* .01 * govtCommPwt), "CLAIM_BAL", "DEBIT", parentOrgId,0, "AGENT", 0, connection);
					
					/*
					commHelper.updateOrgBalance("CLAIM_BAL", fmtPwtAmt + fmtPwtAmt
							* .01 * retComm, userOrgId, "DEBIT", connection);
					// agent claimable balance DEBITED
					commHelper.updateOrgBalance("CLAIM_BAL", fmtPwtAmt + fmtPwtAmt
							* .01 * agtComm, parentOrgId, "DEBIT", connection);*/
				} else {
					OrgCreditUpdation.updateOrganizationBalWithValidate(fmtPwtAmt- (fmtPwtAmt
							* .01 * govtCommPwt), "UNCLAIM_BAL", "CREDIT",userOrgId,parentOrgId, "RETAILER", 0, connection);

					/*commHelper.updateOrgBalance("UNCLAIM_BAL", fmtPwtAmt,
							userOrgId, "CREDIT", connection);*/
				}

				// receipt entries are required to be inserted into receipt
				// table
				return transId;

			} else {
				throw new LMSException(
						"no data insert into main transaction master");
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	/*
	 * public static void main(String[] args) { try { Connection connection =
	 * DBConnect.getConnection(); connection.setAutoCommit(false);
	 * RetPWTProcessHelper helper = new RetPWTProcessHelper(); Map dgVirnPwtMap =
	 * helper.fetchDGPwtDetailsOfRetOrg(171, "RETAILER", "CLAIM_BAL",
	 * connection); logger.debug("dgVirnPwtMap = "+dgVirnPwtMap);
	 * if(dgVirnPwtMap!=null && !dgVirnPwtMap.isEmpty()) {
	 * logger.debug("CLAIMABLE for RETAILER"); String dgReceiptId =
	 * helper.updateClmableBalOfRetOrg(542, 171, 541, 170, dgVirnPwtMap,
	 * connection); logger.debug("dgReceiptId = "+dgReceiptId); }
	 * connection.commit(); }catch (Exception e) { e.printStackTrace(); } }
	 */

}
