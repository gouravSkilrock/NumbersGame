package com.skilrock.lms.coreEngine.drawGames.pwtMgmt;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.DrawPwtApproveRequestNPlrBean;
import com.skilrock.lms.beans.GamePlayerPWTBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DenyPWTBean;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.web.bankMgmt.Helpers.BankUtil;
import com.skilrock.lms.web.bankMgmt.beans.BranchDetailsBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class BOPwtProcessHelper {
	static Log logger = LogFactory.getLog(BOPwtProcessHelper.class);
	/*String barcodeType = (String) ServletActionContext.getServletContext()
			.getAttribute("BARCODE_TYPE"); // added by amit
*/	private Connection connection;
	private PreparedStatement pstmt;

	public String approvePendingPwtsByMas(int taskId, double pwtAmount,
			int requestedById, String requesterType, int approvedByOrgId,
			int approvedByUserId, int gameId, int gameNbr, int drawId,
			String panelId, String ticketNbr, String pwtAmtForMasterApproval)
			throws LMSException {

		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet payLimitDetails = null;
		connection = DBConnect.getConnection();
		double agtPayLimit = 0.0;
		double retPayLimit = 0.0;
		String status;
		String payReqForOrgType;
		int payReqForOrgId = 0;
		String remarks;

		try {
			connection.setAutoCommit(false);
			String getPayLimits = null;
			logger.debug("requestedById--" + requestedById + "--");
			if (requesterType.equals("RETAILER")) {
				getPayLimits = "select organization_id,pay_limit from st_oranization_limits where organization_id in(?,(select parent_id from st_lms_organization_master where organization_id=?))";
				pstmt = connection.prepareStatement(getPayLimits);
				pstmt.setInt(1, requestedById);
				pstmt.setInt(2, requestedById);
				payLimitDetails = pstmt.executeQuery();
				int payByOrgRet = 0;
				int payByOrgAgt = 0;
				while (payLimitDetails.next()) {
					if (payLimitDetails.getInt("organization_id") == requestedById) {
						retPayLimit = payLimitDetails.getDouble("pay_limit");
						// payReqForOrgId=payLimitDetails.getInt("organization_id");
						payByOrgRet = payLimitDetails.getInt("organization_id");
					} else {
						agtPayLimit = payLimitDetails.getDouble("pay_limit");
						// payReqForOrgId=payLimitDetails.getInt("organization_id");
						payByOrgAgt = payLimitDetails.getInt("organization_id");
					}
				}
				if (pwtAmount <= retPayLimit) { // go for retailer's payment
					status = "PND_PAY";
					payReqForOrgType = "RETAILER";
					payReqForOrgId = payByOrgRet;
				}

				else if (pwtAmount <= agtPayLimit) { // go for agent's
					// payment
					status = "PND_PAY";
					payReqForOrgType = "AGENT";
					payReqForOrgId = payByOrgAgt;
				} else { // go for BO's pending pwt
					status = "PND_PAY";
					payReqForOrgType = "BO";
					payReqForOrgId = approvedByOrgId; // Bo's organization id
				}

			} else if (requesterType.equals("AGENT")) {
				getPayLimits = "select organization_id,pay_limit from st_oranization_limits where organization_id=?";
				pstmt = connection.prepareStatement(getPayLimits);
				pstmt.setInt(1, requestedById);
				payLimitDetails = pstmt.executeQuery();
				int payByOrgAgt = 0;
				while (payLimitDetails.next()) {
					agtPayLimit = payLimitDetails.getDouble("pay_limit");
					// payReqForOrgId=payLimitDetails.getInt("organization_id");
					payByOrgAgt = payLimitDetails.getInt("organization_id");
				}

				if (pwtAmount <= agtPayLimit) { // go for agent's payment
					status = "PND_PAY";
					payReqForOrgType = "AGENT";
					payReqForOrgId = payByOrgAgt;
				} else { // go for BO's pending pwt
					status = "PND_PAY";
					payReqForOrgType = "BO";
					payReqForOrgId = approvedByOrgId; // Bo's organization id
				}
				logger.debug("payByOrgAgt--" + payByOrgAgt + "--" + "pwtAmount"
						+ pwtAmount + "--agtPayLimit" + agtPayLimit);

			} else { // go for BO's pending pwt
				status = "PND_PAY";
				payReqForOrgType = "BO";
				payReqForOrgId = approvedByOrgId; // Bo's organization id
			}

			// update the request table for pwt payments

			remarks = "Payment should be done at " + payReqForOrgType;
			String updateRequestStatus = "update st_dg_approval_req_master set req_status=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,pay_req_for_org_type=?,pay_request_for_org_id=?,approval_date=?,remarks=? where task_id=?";
			pstmt = connection.prepareStatement(updateRequestStatus);
			pstmt.setString(1, status);
			pstmt.setString(2, "BO");
			pstmt.setInt(3, approvedByUserId);
			pstmt.setInt(4, approvedByOrgId);
			pstmt.setString(5, payReqForOrgType);
			pstmt.setInt(6, payReqForOrgId);
			pstmt.setTimestamp(7, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			pstmt.setString(8, remarks);
			pstmt.setInt(9, taskId);
			int updatedRow = pstmt.executeUpdate();
			if (updatedRow == 1) {
				logger.debug("Request status updated by BO for Approval");
			} else {
				throw new LMSException(
						"Exception while approving request By BO");
			}

			// update pwt inventory table

			// update ticket details into st_dg_pwt_inv_? table
			String insIntoDGPwtInvQuery = null;
			if ("NA".equalsIgnoreCase(panelId)) {
				insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? where ticket_nbr = ? and draw_id = ? and panel_id is null";
			} else {
				insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? where ticket_nbr = ? and draw_id = ? and panel_id="
						+ panelId;
			}
			pstmt = connection.prepareStatement(insIntoDGPwtInvQuery);
			pstmt.setInt(1, gameId);
			pstmt.setString(2, status);
			pstmt.setString(3, Util.getTktWithoutRpcNBarCodeCount(ticketNbr, ticketNbr.length())/* ticketNbr.substring(0, ticketNbr.length() - 2)*/);// reprint count is extracted 04/04/2011
			pstmt.setInt(4, drawId);
			logger.debug("insIntoDGPwtInvPstmt = " + pstmt);
			int isPwtUpdate = pstmt.executeUpdate();

			if (isPwtUpdate != 1) {
				throw new LMSException("Error ::: pwt table not updated ");
			}

			connection.commit();
			return remarks;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}

	}

	/*
	 * public String approvePendingPwts(int taskId,double pwtAmount,int
	 * requestedById,String requesterType,int approvedByOrgId,int
	 * approvedByUserId,int gameId,int gameNbr,String virnNbr,String
	 * ticketNbr,String pwtAmtForMasterApproval) throws LMSException{ DBConnect
	 * //Connection con=null; PreparedStatement pstmt=null; ResultSet
	 * payLimitDetails=null; connection=dbDBConnect.getConnection(); double
	 * agtPayLimit=0.0; double retPayLimit=0.0; String status; String
	 * payReqForOrgType; int payReqForOrgId=0; String remarks;
	 * 
	 * try{ connection.setAutoCommit(false); String getPayLimits=null;
	 * 
	 * //check if bomaster Approval required if(pwtAmount >=
	 * Integer.parseInt(pwtAmtForMasterApproval)){ //go for master's approval
	 * remarks="Pending For BO Master Approval"; status="PND_MAS"; String
	 * updateRequestStatus="update st_pwt_approval_request_master set
	 * req_status=
	 * ?,requested_to_type=?,approved_by_type=?,approved_by_user_id=?,
	 * approved_by_org_id=?,approval_date=?,remarks=? where task_id=?";
	 * pstmt=connection.prepareStatement(updateRequestStatus);
	 * pstmt.setString(1,status); pstmt.setString(2,"BO"); pstmt.setString(3,
	 * "BO"); pstmt.setInt(4,approvedByUserId); pstmt.setInt(5,approvedByOrgId);
	 * pstmt.setDate(6,new java.sql.Date(new java.util.Date().getTime()));
	 * pstmt.setString(7,remarks); pstmt.setInt(8,taskId); int
	 * updatedRow=pstmt.executeUpdate(); if(updatedRow==1) logger.debug("Request
	 * status updated for BO Master Approval"); else throw new
	 * LMSException("Exception while sending request for master approval");
	 * }else{ if(requesterType.equals("RETAILER")){ getPayLimits="select
	 * organization_id,pay_limit from st_oranization_limits where
	 * organization_id in(?,(select parent_id from st_lms_organization_master
	 * where organization_id=?))";
	 * pstmt=connection.prepareStatement(getPayLimits);
	 * pstmt.setInt(1,requestedById); pstmt.setInt(2,requestedById);
	 * payLimitDetails=pstmt.executeQuery(); int payByOrgRet=0; int
	 * payByOrgAgt=0; while(payLimitDetails.next()){
	 * if(payLimitDetails.getInt("organization_id")==requestedById){
	 * retPayLimit=payLimitDetails.getDouble("pay_limit");
	 * //payReqForOrgId=payLimitDetails.getInt("organization_id");
	 * payByOrgRet=payLimitDetails.getInt("organization_id"); } else{
	 * agtPayLimit=payLimitDetails.getDouble("pay_limit");
	 * //payReqForOrgId=payLimitDetails.getInt("organization_id");
	 * payByOrgAgt=payLimitDetails.getInt("organization_id"); } }
	 * if(pwtAmount<=retPayLimit){ //go for retailer's payment status="PND_PAY";
	 * payReqForOrgType="RETAILER"; payReqForOrgId=payByOrgRet; } else
	 * if(pwtAmount <= agtPayLimit){ //go for agent's payment status="PND_PAY";
	 * payReqForOrgType="AGENT"; payReqForOrgId=payByOrgAgt; } else{ //go for
	 * BO's pending pwt status="PND_PAY"; payReqForOrgType="BO";
	 * payReqForOrgId=approvedByOrgId; //Bo's organization id } } else
	 * if(requesterType.equals("AGENT")){ getPayLimits="select
	 * organization_id,pay_limit from st_oranization_limits where
	 * organization_id=?"; pstmt=connection.prepareStatement(getPayLimits);
	 * pstmt.setInt(1,requestedById); payLimitDetails=pstmt.executeQuery(); int
	 * payByOrgAgt=0; while(payLimitDetails.next()){
	 * agtPayLimit=payLimitDetails.getDouble("pay_limit");
	 * //payReqForOrgId=payLimitDetails.getInt("organization_id");
	 * payByOrgAgt=payLimitDetails.getInt("organization_id"); } if(pwtAmount <=
	 * agtPayLimit){ //go for agent's payment status="PND_PAY";
	 * payReqForOrgType="AGENT"; //payReqForOrgId=approvedByOrgId;
	 * payReqForOrgId=payByOrgAgt; }else{ //go for BO's pending pwt
	 * status="PND_PAY"; payReqForOrgType="BO"; payReqForOrgId=approvedByOrgId;
	 * //Bo's organization id }
	 * 
	 * 
	 * }else{ //go for BO's pending pwt status="PND_PAY"; payReqForOrgType="BO";
	 * payReqForOrgId=approvedByOrgId; //Bo's organization id }
	 * 
	 * 
	 * //update the request table for pwt payments
	 * 
	 * remarks="Payment should be done at "+payReqForOrgType; String
	 * updateRequestStatus="update st_pwt_approval_request_master set
	 * req_status=
	 * ?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?
	 * ,pay_req_for_org_type
	 * =?,pay_request_for_org_id=?,approval_date=?,remarks=? where task_id=?";
	 * pstmt=connection.prepareStatement(updateRequestStatus);
	 * pstmt.setString(1,status); pstmt.setString(2, "BO");
	 * pstmt.setInt(3,approvedByUserId); pstmt.setInt(4,approvedByOrgId);
	 * pstmt.setString(5,payReqForOrgType); pstmt.setInt(6,payReqForOrgId);
	 * pstmt.setDate(7,new java.sql.Date(new java.util.Date().getTime()));
	 * pstmt.setString(8,remarks); pstmt.setInt(9,taskId); int
	 * updatedRow=pstmt.executeUpdate(); if(updatedRow==1) logger.debug("Request
	 * status updated by BO for Approval"); else throw new
	 * LMSException("Exception while approving request By BO"); } //update the
	 * ticket_inv_detail table CommonFunctionsHelper commHelper = new
	 * CommonFunctionsHelper(); commHelper.updateTicketInvTable(ticketNbr,
	 * ticketNbr.split("-")[0]+"-"+ticketNbr.split("-")[1],gameNbr, gameId,
	 * status,approvedByUserId , approvedByOrgId,"UPDATE", connection);
	 * 
	 * //update pwt inventory table boolean
	 * isPwtUpdate=commHelper.updateVirnStatus(gameNbr, status, gameId, virnNbr,
	 * connection); if(!isPwtUpdate) throw new LMSException("Error ::: pwt table
	 * not updated ");
	 * 
	 * connection.commit(); return remarks;
	 * 
	 * }catch(SQLException e){ e.printStackTrace(); throw new LMSException("sql
	 * exception",e); } }
	 */

	public long boDirectPlrPwtPayment(String ticketNbr, int drawId,
			int playerId, double pwtAmt, double tax, int taskId,
			String chequeNbr, String draweeBank, String issuingParty,
			java.sql.Date chqDate, String paymentType, int userOrgId,
			int userId, int gameNbr, int gameId, Connection connection,
			Object panelId, String schemeType) throws LMSException {
		try {

			// insert data into main transaction master
			logger.debug("insert data into transaction master ");
			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = connection
					.prepareStatement(transMasQuery);
			pstmt.setString(1, "BO");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				long transId = rs.getLong(1);
				// double pwtAmt = Double.parseDouble(bean.getPwtAmount());
				// DGDirectPlrPwtBean dirPlrBean = (DGDirectPlrPwtBean) bean;

				// insert in st_bo_transaction master
				pstmt = connection.prepareStatement(QueryManager
						.insertInBOTransactionMaster());
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, userOrgId);
				pstmt.setString(4, "PLAYER");
				pstmt.setInt(5, playerId);
				pstmt.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(7, "DG_PWT_PLR");
				pstmt.executeUpdate();
				logger.debug("insert into BO transaction master = " + pstmt);

				String directPlrPayment = "insert into st_dg_bo_direct_plr_pwt (bo_user_id, "
						+ "bo_org_id, draw_id, transaction_id, transaction_date, game_id, player_id,"
						+ " pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank,"
						+ " issuing_party_name, task_id,panel_id ) values (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
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
				pstmt.setDouble(9, tax);
				pstmt.setDouble(10, pwtAmt - tax);
				pstmt.setString(11, paymentType);

				if ("cash".equalsIgnoreCase(paymentType)
						|| "TPT".equalsIgnoreCase(paymentType)) {
					pstmt.setObject(12, null);
					pstmt.setObject(13, null);
					pstmt.setObject(14, null);
					pstmt.setObject(15, null);
				} else if ("cheque".equalsIgnoreCase(paymentType)) {
					pstmt.setString(12, chequeNbr);
					pstmt.setDate(13, chqDate);
					pstmt.setString(14, draweeBank);
					pstmt.setString(15, issuingParty);
				}
				// pstmt.setString(12, chequeNbr);
				// pstmt.setDate(13, chqDate);
				// pstmt.setString(14, draweeBank);
				// pstmt.setString(15, issuingParty);
				pstmt.setInt(16, taskId);
				pstmt.setObject(17, panelId);
				pstmt.executeUpdate();
				logger.debug("insert into st_dg_bo_direct_plr_pwt = " + pstmt);

				// update ticket details into st_dg_pwt_inv_? table
				String insIntoDGPwtInvQuery = null;
				if ("DRAW_WISE".equalsIgnoreCase(schemeType.trim())) {
					insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  bo_transaction_id = ? "
							+ " where ticket_nbr = ? and draw_id = ?";
				} else {
					insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  bo_transaction_id = ? "
							+ " where ticket_nbr = ? and draw_id = ? and panel_id="
							+ panelId;
				}
				PreparedStatement insIntoDGPwtInvPstmt = connection
						.prepareStatement(insIntoDGPwtInvQuery);
				insIntoDGPwtInvPstmt.setInt(1, gameId);
				insIntoDGPwtInvPstmt.setString(2, "CLAIM_PLR_BO");
				insIntoDGPwtInvPstmt.setLong(3, transId);
				insIntoDGPwtInvPstmt.setString(4, ticketNbr);
				insIntoDGPwtInvPstmt.setInt(5, drawId);
				logger.debug("insIntoDGPwtInvPstmt = " + insIntoDGPwtInvPstmt);
				insIntoDGPwtInvPstmt.executeUpdate();

				
				if(LMSFilterDispatcher.isByPassDatesRequired){
					BranchDetailsBean branchDetailsBean=BankUtil.getBankBranchDetails(userId, connection);
					if(branchDetailsBean!=null)
					BankUtil.branchTrnMapping(userId, transId, branchDetailsBean.getBankId(), branchDetailsBean.getBranchId() , "DG_PWT_PLR", "PWT @ BANK", connection);
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

	public boolean denyPWTProcess(int taskId, int drawId, int gameId,
			String ticketNbr, String denyPwtStatus, int gameNbr, int userId,
			int userOrgId, String panelId) throws LMSException {

		boolean statusChange = false;
		// Connection connection=null;

		connection = DBConnect.getConnection();
		try {
			connection.setAutoCommit(false);
			// CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
			String tempPwtStatus = "";
			String pwtStatus = "";
			// int isPwtTicketDelete =0;
			logger.debug("Deny type is  " + denyPwtStatus);
			int isPwtupdate = 0;
			if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus.trim())) {
				logger.debug("INside permanent cancellation "
						+ "denyPwtStatus " + denyPwtStatus);
				tempPwtStatus = "CANCEL";
				pwtStatus = "CANCELLED_PERMANENT";

				// update pwt inv table status
				String invUpdateQuery = null;

				if ("NA".equalsIgnoreCase(panelId)) {
					invUpdateQuery = "update st_dg_pwt_inv_? set status=? where ticket_nbr=? and draw_id=? ";
				} else {
					invUpdateQuery = "update st_dg_pwt_inv_? set status=? where ticket_nbr=? and draw_id=? and panel_id="
							+ panelId;
				}

				PreparedStatement pstmtPwtInvUpdate = connection
						.prepareStatement(invUpdateQuery);
				pstmtPwtInvUpdate.setInt(1, gameId);
				pstmtPwtInvUpdate.setString(2, pwtStatus);
				pstmtPwtInvUpdate.setLong(3, Long.parseLong(ticketNbr.substring(0,ticketNbr.length()-1)));
				pstmtPwtInvUpdate.setInt(4, drawId);
				isPwtupdate = pstmtPwtInvUpdate.executeUpdate();
				logger.debug("update st_pwt_inv ==" + pstmtPwtInvUpdate);
			} else if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())) {
				logger.debug("INside Temporary cancellation "
						+ "denyPwtStatus " + denyPwtStatus);
				tempPwtStatus = "CANCEL";
				// pwtStatus = "UNCLM_CANCELLED";
				pwtStatus = "UNCM_PWT_CANCELLED";// added by amit on 29/07/10
				// delete entry in case of Temporary Cancellation
				// update pwt inv table status
				String invUpdateQuery = null;

				if ("NA".equalsIgnoreCase(panelId)) {
					// invUpdateQuery = "delete from st_dg_pwt_inv_? where
					// ticket_nbr=? and draw_id=? and panel_id is null";
					invUpdateQuery = "update st_dg_pwt_inv_? set status=? where ticket_nbr=? and draw_id=? ";// added
					// by
					// amit
					// on
					// 29/07/10
				} else {
					// invUpdateQuery = "delete from st_dg_pwt_inv_? where
					// ticket_nbr=? and draw_id=? and panel_id="
					// + panelId;
					invUpdateQuery = "update st_dg_pwt_inv_? set status=? where ticket_nbr=? and draw_id=? and panel_id=" // added
					// by
							// amit
							// on
							// 29/07/10
							+ panelId;
				}

				PreparedStatement pstmtPwtInvUpdate = connection
						.prepareStatement(invUpdateQuery);
				pstmtPwtInvUpdate.setInt(1, gameId);
				pstmtPwtInvUpdate.setString(2, pwtStatus);// added later
				pstmtPwtInvUpdate.setLong(3, Long.parseLong(ticketNbr.substring(0,ticketNbr.length()-1)));
				pstmtPwtInvUpdate.setInt(4, drawId);
				isPwtupdate = pstmtPwtInvUpdate.executeUpdate();
				logger.debug("update for temp cancel st_pwt_inv =="
						+ pstmtPwtInvUpdate);

			}

			PreparedStatement pstmt = connection
					.prepareStatement("update st_dg_approval_req_master set req_status=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,approval_date=?,remarks=? where task_id=?");
			pstmt.setString(1, tempPwtStatus);
			pstmt.setString(2, "BO");
			pstmt.setInt(3, userId);
			pstmt.setInt(4, userOrgId);
			pstmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(6, "request Denied by BO As "
					+ denyPwtStatus.trim());
			pstmt.setInt(7, taskId);
			int isUpdate = pstmt.executeUpdate();
			logger.debug("update request temporary table ==" + pstmt);

			/*
			 * //update pwt inv table status String invUpdateQuery=null;
			 * 
			 * if("NA".equalsIgnoreCase(panelId)) invUpdateQuery = "update
			 * st_dg_pwt_inv_? set status=? where ticket_nbr=? and draw_id=? ";
			 * else invUpdateQuery = "update st_dg_pwt_inv_? set status=? where
			 * ticket_nbr=? and draw_id=? and panel_id="+panelId;
			 * 
			 * PreparedStatement
			 * pstmtPwtInvUpdate=connection.prepareStatement(invUpdateQuery);
			 * pstmtPwtInvUpdate.setInt(1,gameNbr);
			 * pstmtPwtInvUpdate.setString(2, pwtStatus);
			 * pstmtPwtInvUpdate.setLong(3,Long.parseLong(ticketNbr));
			 * pstmtPwtInvUpdate.setInt(4,drawId); int
			 * isPwtupdate=pstmtPwtInvUpdate.executeUpdate();
			 * logger.debug("update st_pwt_inv =="+pstmtPwtInvUpdate);
			 */

			DenyPWTBean denyBean = new DenyPWTBean();
			denyBean.setDrawId(drawId);
			denyBean.setGameNo(gameNbr);
			denyBean.setPanelId(panelId.equals("NA") ? null : panelId); // we
			// have
			denyBean.setTicketNo(ticketNbr);
			String status = ("CANCELLED_PERMANENT".equals(pwtStatus) || "UNCM_PWT_CANCELLED".equals(pwtStatus)) ? "CANCELLED" : pwtStatus;
			denyBean.setStatus(status);
			denyBean.setGameId(gameId);
			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.DRAWGAME);
			sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CHANGE_PWT_STATUS);
			sReq.setServiceData(denyBean);

			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);
			if (sRes.getIsSuccess()) {
				
						logger.debug("successfully updated in core engine for deny");
				if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
						.trim())
						&& isUpdate == 1 && isPwtupdate == 1) {
					logger.debug("inside temporary commit ");
					statusChange = true;
					connection.commit();
				} else if ("Permanent Cancellation"
						.equalsIgnoreCase(denyPwtStatus.trim())
						&& isUpdate == 1 && isPwtupdate == 1) {
					
							logger.debug("successfully updated in core engine for deny");
					logger.debug("inside permanent commit ");
					statusChange = true;
					connection.commit();
				}
			}

			/*
			 * if ("Temporary
			 * Cancellation".equalsIgnoreCase(denyPwtStatus.trim()) &&
			 * isUpdate==1 && isPwtupdate==1) { logger.debug("inside temporary
			 * commit "); statusChange=true; connection.commit(); } else
			 * if("Permanent
			 * Cancellation".equalsIgnoreCase(denyPwtStatus.trim()) &&
			 * isUpdate==1 && isPwtupdate==1) { logger.debug("inside permanent
			 * commit "); statusChange=true; connection.commit(); }
			 */

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
				throw new LMSException(se);
			}

		}

		return statusChange;

	}

	// this method is added by amit on 21/07/10

	public int getGameIdFromGameNbr(int gameNbr, Connection connection)
			throws LMSException {

		try {

			PreparedStatement pstmt = connection
					.prepareStatement("select game_id from st_dg_game_master where game_nbr=?");
			pstmt.setInt(1, gameNbr);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("game_id");
			} else {
				throw new LMSException();
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException();
		}

	}

	/*
	 * public void verifyDirectPlrTktNo() throws LMSException { }
	 * 
	 * public void registerDirectPlr() throws LMSException { }
	 * 
	 * public void updateDirectPlrTktNo() throws LMSException { }
	 */

	/*
	 * public String verifyAndSaveTicketDirPlr(String ticketNbr , int gameNbr,
	 * UserInfoBean userInfoBean, String pwtAmtForMasterApproval,PWTDrawBean
	 * drawWinningList) throws LMSException {
	 * 
	 * try { connection = DBConnect.getConnection();
	 * connection.setAutoCommit(false);
	 * 
	 * int gameId = getGameIdFromGameNbr(gameNbr,connection); // validate the
	 * ticket Map<String, Object> detailMap = new TreeMap<String, Object>();
	 * //add '-' if ticket nymber does not contains '-' using game format digits
	 * // validate VIRN nbr String returnType =
	 * verifyPwtTicketDirPlr(ticketNbr,gameId,gameNbr, connection, userInfoBean,
	 * pwtAmtForMasterApproval,drawWinningList); //logger.debug("********** size
	 * of list " + drawPwtBeanlist.size());
	 * 
	 * for (DrawPWTBean drawPWTBean : drawPwtBeanlist) { if(drawPWTBean!= null
	 * && drawPWTBean.getIsValid()) { detailMap.put("drawPwtBeanlist",
	 * drawPwtBeanlist); detailMap.put("returnType", "registration");
	 * logger.debug("go to registration limit"); return detailMap; } }
	 * detailMap.put("drawPwtBeanlist", drawPwtBeanlist);
	 * detailMap.put("returnType", "input"); return detailMap; return
	 * returnType;
	 * 
	 * 
	 * }catch (SQLException e) { e.printStackTrace(); throw new LMSException(e);
	 * }catch (Exception e) { e.printStackTrace(); throw new LMSException(e);
	 * }finally { try { connection.close(); } catch (SQLException e) {
	 * e.printStackTrace(); throw new LMSException(e); } } }
	 */

	public DrawPwtApproveRequestNPlrBean getRequestDetails(int taskId,
			String partyType, String raffleTktType) throws LMSException {
		logger.debug("getRequestDetails for dg called");

		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet resultFromDb;
		ResultSet rs;
		logger.debug("taskId:::::" + taskId);
		connection = DBConnect.getConnection();
		try {

			String getPwtDetailsQuery = null;

			if ("AGENT".equals(partyType)) {
				getPwtDetailsQuery = "select a.task_id,a.requested_by_org_id,a.requester_type,a.party_type,a.party_id,a.request_id,a.pwt_amt,a.tax_amt,a.net_amt,a.req_status,a.ticket_nbr,a.draw_id,ifnull(a.panel_id,'NA')panel_id,a.remarks,a.game_id,d.game_name,d.game_nbr,c.name from st_dg_approval_req_master a ,st_dg_game_master d,st_lms_organization_master c  where 1=1 and d.game_id=a.game_id   and task_id=? and party_type='AGENT' and c.organization_id=a.party_id";
			} else if ("PLAYER".equals(partyType)) {
				getPwtDetailsQuery = "select a.task_id,a.requested_by_org_id,a.requester_type,a.party_type,a.request_id,a.party_id,a.pwt_amt,a.tax_amt,a.net_amt,a.req_status,a.ticket_nbr,a.draw_id,ifnull(a.panel_id,'NA')panel_id,a.remarks,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,b.player_id,b.bank_acc_nbr,b.bank_name,b.bank_branch,b.location_city,d.game_name,d.game_nbr from st_dg_approval_req_master a , st_lms_player_master b ,st_dg_game_master d where 1=1 and d.game_id=a.game_id   and a.task_id=? and party_type='PLAYER' and a.party_id=b.player_id";
			} else {
				throw new LMSException("Error because party type is "
						+ partyType);
			}

			logger.debug("query to get pwt details :" + getPwtDetailsQuery);
			pstmt = connection.prepareStatement(getPwtDetailsQuery);
			pstmt.setInt(1, taskId);
			logger.debug("query to get pwt details :" + getPwtDetailsQuery);

			resultFromDb = pstmt.executeQuery();
			// PlayerBean plrBean=new PlayerBean();
			DrawPwtApproveRequestNPlrBean plrPwtBean = new DrawPwtApproveRequestNPlrBean();
			while (resultFromDb.next()) {
				// collect player info
				if (resultFromDb.getString("party_type").equals("PLAYER")) {
					// plrBean.setFirstName(resultFromDb.getString("first_name"));
					// plrBean.setLastName(resultFromDb.getString("last_name"));
					plrPwtBean.setPartyName(resultFromDb
							.getString("first_name")
							+ " " + resultFromDb.getString("last_name"));
					plrPwtBean
							.setPhone_nbr(resultFromDb.getString("phone_nbr"));
					plrPwtBean.setCity(resultFromDb.getString("city"));
					plrPwtBean.setBankActNbr(resultFromDb
							.getString("bank_acc_nbr"));
					plrPwtBean.setBankBranch(resultFromDb
							.getString("bank_branch"));
					plrPwtBean.setBankCity(resultFromDb
							.getString("location_city"));
					plrPwtBean.setBankName(resultFromDb.getString("bank_name"));
					// plrBean.setPlrCity(resultFromDb.getString("city"));
					// plrBean.setPlrId(resultFromDb.getInt("player_id"));
				} else if (resultFromDb.getString("party_type").equals("AGENT")) {
					plrPwtBean.setPartyName(resultFromDb.getString("name"));
				}

				// collect pwt info
				plrPwtBean.setPartyType(resultFromDb.getString("party_type"));
				plrPwtBean.setPartyId(resultFromDb.getInt("party_id"));
				plrPwtBean.setPwt_amt(resultFromDb.getDouble("pwt_amt"));
				plrPwtBean.setComm_amt(resultFromDb.getDouble("tax_amt"));
				plrPwtBean.setNet_amt(resultFromDb.getDouble("net_amt"));
				String tktNbr=resultFromDb.getString("ticket_nbr");
				if ("RAFFLE".equalsIgnoreCase(Util.getGameType(Util
						.getGamenoFromTktnumber(tktNbr)))
						&& "REFERENCE".equalsIgnoreCase(raffleTktType)) {
					pstmt = connection.prepareStatement("select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr='"+tktNbr.substring(0,
							tktNbr.length() - 2)+"'");
					rs = pstmt.executeQuery();
					if(rs.next()){
						tktNbr=rs.getString("sale_ticket_nbr");
					}
				}
				plrPwtBean.setTicket_nbr(resultFromDb.getString("ticket_nbr"));
				plrPwtBean.setDisplayTktNbr(tktNbr);
				plrPwtBean.setDrawId(resultFromDb.getInt("draw_id"));
				plrPwtBean.setPanelId(resultFromDb.getString("panel_id"));
				plrPwtBean.setRemarks(resultFromDb.getString("remarks"));
				plrPwtBean.setTask_id(resultFromDb.getInt("task_id"));
				plrPwtBean.setRequest_id(resultFromDb.getString("request_id"));
				plrPwtBean.setRequested_by_org_id(resultFromDb
						.getInt("requested_by_org_id"));
				plrPwtBean.setGame_id(resultFromDb.getInt("game_id"));
				plrPwtBean.setGame_nbr(resultFromDb.getInt("game_nbr"));
				plrPwtBean.setRequester_type(resultFromDb
						.getString("requester_type"));

			}
			// List plrPwtDetails=new ArrayList();
			// plrPwtDetails.add(plrBean);
			// plrPwtDetails.add(plePwtBean);

			return plrPwtBean;

		} catch (SQLException se) {
			logger.error("Exception: " + se);
			se.printStackTrace();
			throw new LMSException();
		}
		// return null;

	}

	/**
	 * This method is used to get requested pwts from request table
	 * 
	 */
	public List<DrawPwtApproveRequestNPlrBean> getRequestedPwts(
			String requestId, int requestedById, String requesterType,
			String firstName, String lastName, String status,
			int requestedToOrgId, String partyType) throws LMSException {

		Statement stmtGetReqDetails = null;
		ResultSet reqDetails;
		connection = DBConnect.getConnection();
		StringBuilder getRequestDetailsQuery = null;
		new StringBuilder(
				"select a.task_id,a.party_type,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.draw_id,a.game_id,a.requester_type,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_dg_approval_req_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_dg_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id  ");

		if ("PLAYER".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.task_id,a.party_type,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.draw_id,a.game_id,a.requester_type,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_dg_approval_req_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_dg_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id  ");
			if (firstName != null && !"".equals(firstName)) {
				getRequestDetailsQuery.append(" and b.first_name='" + firstName
						+ "'");
			}
			if (lastName != null && !"".equals(lastName)) {
				getRequestDetailsQuery.append(" and b.last_name='" + lastName
						+ "'");
			}

		} else if ("RETAILER".equals(partyType) || "AGENT".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.task_id,a.party_type,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.draw_id,a.game_id,a.requester_type,c.name,d.game_name,d.game_nbr from st_dg_approval_req_master a ,st_lms_organization_master c,st_dg_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id ");

		}
		// getRequestDetailsQuery.append(" and
		// a.requested_to_org_id="+requestedToOrgId);

		if (requesterType != null && !"".equalsIgnoreCase(requesterType)
				&& !"ALL".equalsIgnoreCase(requesterType)) {
			getRequestDetailsQuery.append(" and a.requester_type='"
					+ requesterType + "'");
		}

		getRequestDetailsQuery.append(" and a.party_type='" + partyType + "'");

		if (requestId != null && !"".equals(requestId)) {
			getRequestDetailsQuery.append(" and a.request_id='" + requestId
					+ "'");
		}
		if (requestedById != 0) {
			getRequestDetailsQuery.append(" and a.requested_by_org_id="
					+ requestedById);
		}

		if (status != null && !"".equals(status) && !"All".equals(status)) {
			getRequestDetailsQuery.append(" and a.req_status='" + status + "'");
		}

		logger.debug("requests Details Query:: "
				+ getRequestDetailsQuery.toString());
		try {
			List<DrawPwtApproveRequestNPlrBean> pwtReqDetailsList = new ArrayList<DrawPwtApproveRequestNPlrBean>();
			DrawPwtApproveRequestNPlrBean pwtAppReqPlrBean;
			stmtGetReqDetails = connection.createStatement();
			reqDetails = stmtGetReqDetails.executeQuery(getRequestDetailsQuery
					.toString());
			while (reqDetails.next()) {
				pwtAppReqPlrBean = new DrawPwtApproveRequestNPlrBean();
				pwtAppReqPlrBean.setRequest_id(reqDetails
						.getString("request_id"));
				pwtAppReqPlrBean.setTask_id(reqDetails.getInt("task_id"));
				pwtAppReqPlrBean.setRequested_by_org_id(reqDetails
						.getInt("requested_by_org_id"));
				pwtAppReqPlrBean.setRequester_type(reqDetails
						.getString("requester_type"));
				pwtAppReqPlrBean.setReq_status(reqDetails
						.getString("req_status"));
				pwtAppReqPlrBean.setRequest_date(reqDetails
						.getDate("request_date"));
				pwtAppReqPlrBean.setRemarks(reqDetails.getString("remarks"));
				pwtAppReqPlrBean.setTicket_nbr(reqDetails
						.getString("ticket_nbr"));
				pwtAppReqPlrBean.setDrawId(reqDetails.getInt("draw_id"));
				pwtAppReqPlrBean.setRequestedByOrgName(reqDetails
						.getString("name"));
				pwtAppReqPlrBean
						.setGame_name(reqDetails.getString("game_name"));
				pwtAppReqPlrBean.setGame_nbr(reqDetails.getInt("game_nbr"));
				pwtAppReqPlrBean.setGame_id(reqDetails.getInt("game_id"));

				if ("PLAYER".equals(partyType)) {
					pwtAppReqPlrBean.setPartyName(reqDetails
							.getString("first_name")
							+ " " + reqDetails.getString("last_name"));
					// pwtAppReqPlrBean.setFirst_name(reqDetails.getString("first_name"));
					// pwtAppReqPlrBean.setLast_name(reqDetails.getString("last_name"));
					pwtAppReqPlrBean.setCity(reqDetails.getString("city"));
					pwtAppReqPlrBean.setPhone_nbr(reqDetails
							.getString("phone_nbr"));
				} else {
					pwtAppReqPlrBean.setPartyName(reqDetails.getString("name"));
				}
				pwtAppReqPlrBean.setPartyType(reqDetails
						.getString("party_type"));
				pwtAppReqPlrBean.setPwt_amt(reqDetails.getDouble("pwt_amt"));
				pwtReqDetailsList.add(pwtAppReqPlrBean);
			}
			DBConnect.closeCon(connection);
			return pwtReqDetailsList;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql Exception", e);
		}

	}

	/**
	 * This method is used to get pending pwts to pay from request table
	 */
	public List<DrawPwtApproveRequestNPlrBean> getRequestsPwtsToPay(
			String requestId, int pendingAtAgtOrgId, String firstName,
			String lastName, String status, int payByOrgId,
			String paymentPendingAt, String partyType) throws LMSException {
		logger.debug("getPendingPwtToPay for draw games called");
		Statement stmtGetReqDetails = null;
		ResultSet reqDetails;
		connection = DBConnect.getConnection();

		StringBuilder getRequestDetailsQuery = null;

		if (partyType != null && !"".equals(partyType)
				&& "PLAYER".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.party_type,a.task_id,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.draw_id,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_dg_approval_req_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_dg_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id and a.party_type='PLAYER' ");
			if (firstName != null && !"".equals(firstName)) {
				getRequestDetailsQuery.append(" and b.first_name='" + firstName
						+ "'");
			}
			if (lastName != null && !"".equals(lastName)) {
				getRequestDetailsQuery.append(" and b.last_name='" + lastName
						+ "'");
			}
			if (!"ALL".equals(paymentPendingAt)) {
				if (payByOrgId != 0) {
					getRequestDetailsQuery
							.append(" and a.pay_request_for_org_id="
									+ payByOrgId);
				}
				getRequestDetailsQuery.append(" and a.pay_req_for_org_type='"
						+ paymentPendingAt + "'");
			}
		} else if (partyType != null && !"".equals(partyType)
				&& "AGENT".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.party_type,a.task_id,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.draw_id,a.game_id,c.name,d.game_name,d.game_nbr from st_dg_approval_req_master a ,st_lms_organization_master c,st_dg_game_master d  where 1=1 and c.organization_id=a.party_id and d.game_id=a.game_id and a.party_type='AGENT'");

			if (pendingAtAgtOrgId != 0) {
				getRequestDetailsQuery.append(" and a.party_id="
						+ pendingAtAgtOrgId + "");
			}

			if (!"ALL".equals(paymentPendingAt)) {
				if (payByOrgId != 0) {
					getRequestDetailsQuery
							.append(" and a.pay_request_for_org_id="
									+ payByOrgId);
				}
				getRequestDetailsQuery.append(" and a.pay_req_for_org_type='"
						+ paymentPendingAt + "'");
			}
		} else {
			throw new LMSException(
					"LMS EXCeption You have selected Wrong party type :: "
							+ partyType);
		}

		if (requestId != null && !"".equals(requestId)) {
			getRequestDetailsQuery.append(" and a.request_id='" + requestId
					+ "'");
		}

		if (status != null && !"".equals(status)) {
			getRequestDetailsQuery.append(" and a.req_status='" + status + "'");
		}

		logger.debug("requests Details Query:: "
				+ getRequestDetailsQuery.toString());
		try {
			List<DrawPwtApproveRequestNPlrBean> pwtReqDetailsList = new ArrayList<DrawPwtApproveRequestNPlrBean>();
			DrawPwtApproveRequestNPlrBean pwtAppReqPlrBean;
			stmtGetReqDetails = connection.createStatement();
			reqDetails = stmtGetReqDetails.executeQuery(getRequestDetailsQuery
					.toString());
			/*
			 * if (reqDetails.next()) logger.debug("returned from db with
			 * data"); else logger.debug("returned from db w/o data");
			 */
			while (reqDetails.next()) {
				logger.debug("data returned from db");
				pwtAppReqPlrBean = new DrawPwtApproveRequestNPlrBean();
				pwtAppReqPlrBean.setRequest_id(reqDetails
						.getString("request_id"));
				pwtAppReqPlrBean.setTask_id(reqDetails.getInt("task_id"));
				pwtAppReqPlrBean.setRequested_by_org_id(reqDetails
						.getInt("requested_by_org_id"));
				pwtAppReqPlrBean.setReq_status(reqDetails
						.getString("req_status"));
				pwtAppReqPlrBean.setRequest_date(reqDetails
						.getDate("request_date"));
				pwtAppReqPlrBean.setRemarks(reqDetails.getString("remarks"));
				pwtAppReqPlrBean.setTicket_nbr(reqDetails
						.getString("ticket_nbr"));
				pwtAppReqPlrBean.setDrawId(reqDetails.getInt("draw_id"));
				pwtAppReqPlrBean.setRetailer_name(reqDetails.getString("name"));
				pwtAppReqPlrBean
						.setGame_name(reqDetails.getString("game_name"));
				pwtAppReqPlrBean.setGame_nbr(reqDetails.getInt("game_nbr"));
				pwtAppReqPlrBean.setGame_id(reqDetails.getInt("game_id"));

				// pwtAppReqPlrBean.setFirst_name(reqDetails.getString("first_name"));
				// pwtAppReqPlrBean.setLast_name(reqDetails.getString("last_name"));
				if ("PLAYER".equals(partyType)) {
					pwtAppReqPlrBean.setPartyName(reqDetails
							.getString("first_name")
							+ " " + reqDetails.getString("last_name"));
					pwtAppReqPlrBean.setCity(reqDetails.getString("city"));
					pwtAppReqPlrBean.setPhone_nbr(reqDetails
							.getString("phone_nbr"));
				} else if ("AGENT".equals(partyType)) {
					pwtAppReqPlrBean.setPartyName(reqDetails.getString("name"));
				}
				pwtAppReqPlrBean.setPartyType(reqDetails
						.getString("party_type"));
				pwtAppReqPlrBean.setPwt_amt(reqDetails.getDouble("pwt_amt"));
				pwtReqDetailsList.add(pwtAppReqPlrBean);
				logger.debug("bean added to the list");
			}
			DBConnect.closeCon(connection);
			return pwtReqDetailsList;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql Exception", e);
		}

	}

	public List<GamePlayerPWTBean> getUnapprovedPwt() throws LMSException {

		Connection con = null;
		PreparedStatement pstmt;
		GamePlayerPWTBean gamePlayerBean;
		List<GamePlayerPWTBean> gamePlayerBeanList = new ArrayList<GamePlayerPWTBean>();
		try {
			con = DBConnect.getConnection();
			String getUnapprovePwtQuery = "select b.game_name,b.game_nbr,a.pwt_receipt_id,a.player_id,a.game_id,a.virn_code,a.pwt_amt,a.status,a.ticket_nbr from st_direct_player_pwt_temp_receipt a,st_game_master b where a.status=? and a.game_id=b.game_id";

			pstmt = con.prepareStatement(getUnapprovePwtQuery);
			pstmt.setString(1, "PND_ADM");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				gamePlayerBean = new GamePlayerPWTBean();
				gamePlayerBean.setPlayerReceiptId(rs.getInt("pwt_receipt_id"));
				gamePlayerBean.setPlrId(rs.getInt("player_id"));
				gamePlayerBean.setPwtAmt(rs.getDouble("pwt_amt"));
				gamePlayerBean.setTicketNbr(rs.getString("ticket_nbr"));
				gamePlayerBean.setGameId(rs.getInt("game_id"));
				gamePlayerBean.setGameName(rs.getString("game_name"));
				gamePlayerBean.setGameNbr(rs.getInt("game_nbr"));
				gamePlayerBean.setVirnCode(rs.getString("virn_code"));
				gamePlayerBeanList.add(gamePlayerBean);
			}

			return gamePlayerBeanList;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();

		} finally {
			try {

				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
			}
		}
		return null;

	}

	public String payPendingPwt(int taskId, double pwtAmount, double taxAmt,
			double netAmt, int partyId, String partyType, String ticketNbr,
			int drawId, String panelId, int gameId, int payByOrgId,
			int payByUserId, String payByOrgName, String paymentType,
			String chqNbr, String draweeBank, String chequeDate,
			String issuiningParty, int gameNbr, String rootPath)
			throws LMSException {
		logger.debug("payPendingPwt for draw game called ");
		// this field will be removed from st agent pwt table so no need to pass
		// this variable for time being put it as zero instead of fetching
		// userid from database
		String autoGeneratedReceiptNo = null;
		int partyUserId = 0;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			/*
			 * List<PwtBean> pwtList=new ArrayList<PwtBean>(); PwtBean
			 * pwtBean=new PwtBean();
			 * pwtBean.setPwtAmount(String.valueOf(pwtAmount));
			 * pwtBean.setEncVirnCode(virnNbr); pwtBean.setValid(true);
			 * pwtList.add(pwtBean);
			 */

			CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
			String receipts;
			List<Long> transIdList = new ArrayList<Long>();
			if ("AGENT".equals(partyType)) {

				// this process in not implemented at agent end so not need to
				// code here for payment to agent
				// receipts = commonHelper.boEndAgtPWTPaymentProcess(pwtList,
				// gameId,payByOrgName, payByOrgId, partyId,
				// partyUserId,rootPath,partyUserId,gameNbr);
				// autoGeneratedReceiptNo = receipts.split("-")[1];
			} else if ("PLAYER".equals(partyType)) {
				// do direct Player payment at Bo End
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				java.sql.Date chqDate = null;
				try {
					if (!"".equalsIgnoreCase(chequeDate) && chequeDate != null) {
						chqDate = new java.sql.Date(dateFormat
								.parse(chequeDate).getTime());
					}
				} catch (ParseException e) {
					logger.error("Exception: " + e);
					e.printStackTrace();
					throw new LMSException(
							"Exception date parsing  while pwt payments at BO end ",
							e);
				}

				// autoGeneratedReceiptNo = doDirectPlrPwtPayAtBO(gameId,
				// partyId, pwtAmount, taxAmt, virnNbr, taskId, chqNbr,
				// draweeBank, issuiningParty, chqDate,paymentType, payByOrgId,
				// payByUserId, gameNbr, ticketNbr, payByOrgName,rootPath);
				long transId = 0;
				logger.debug("********************* " + panelId);
				if ("NA".equalsIgnoreCase(panelId)) {// draw wise scheme
					// applicable
					// reprint count is extracted from ticket number 04/04/2011
					transId = boDirectPlrPwtPayment(Util.getTktWithoutRpcNBarCodeCount(ticketNbr, ticketNbr.length())/*ticketNbr.substring(0,
							ticketNbr.length() - 2)*/, drawId, partyId,
							pwtAmount, taxAmt, taskId, chqNbr, draweeBank,
							issuiningParty, chqDate, paymentType, payByOrgId,
							payByUserId, gameNbr, gameId, connection, null,
							"DRAW_WISE");
				} else if (Integer.parseInt(panelId) > 0) {// panel wise scheme
					// applicable
					transId = boDirectPlrPwtPayment(Util.getTktWithoutRpcNBarCodeCount(ticketNbr, ticketNbr.length())/*ticketNbr.substring(0,
							ticketNbr.length() - 2)*/, drawId, partyId,
							pwtAmount, taxAmt, taskId, chqNbr, draweeBank,
							issuiningParty, chqDate, paymentType, payByOrgId,
							payByUserId, gameNbr, gameId, connection, panelId,
							"PANEL_WISE");
				} else if (Integer.parseInt(panelId) == 0) {// raffle scheme
					transId = boDirectPlrPwtPayment(Util.getTktWithoutRpcNBarCodeCount(ticketNbr, ticketNbr.length())/*ticketNbr.substring(0,
							ticketNbr.length() - 2)*/, drawId, partyId,
							pwtAmount, taxAmt, taskId, chqNbr, draweeBank,
							issuiningParty, chqDate, paymentType, payByOrgId,
							payByUserId, gameNbr, gameId, connection, panelId,
							"PANEL_WISE");
				}

				transIdList.add(transId);

				// update inv table for draw games
				updateDgTicketReq(payByOrgId, transId, taskId, connection);

				// Generate Receipt for player by BO
				autoGeneratedReceiptNo = commonHelper.generateReceiptBo(
						connection, partyId, partyType, transIdList);
				connection.commit();
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportPlayer(taskId, rootPath,
						"DRAW_GAME");
				logger.debug("auto generated receipt number is  "
						+ autoGeneratedReceiptNo);

			} else {
				throw new LMSException(
						"Exception Ocurred :: Party Type is not Selected Properly :: Only Agent and Player can claim at BO End :: Party Type is: "
								+ partyType);
			}

		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException();
		}

		return autoGeneratedReceiptNo.split("-")[1];
	}

	/*
	 * public Map plrRegistrationAndApproval(UserInfoBean userInfoBean,
	 * PWTDrawBean pwtDrawBean, String playerType, int playerId, PlayerBean
	 * plrBean, String rootPath, boolean isAnonymous) throws LMSException { Map
	 * pwtAppMap = new TreeMap(); ServiceResponse sRes = new ServiceResponse();
	 * ServiceRequest sReq = new ServiceRequest();
	 * sReq.setServiceName(ServiceName.DRAWGAME); //
	 * sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
	 * IServiceDelegate delegate = ServiceDelegate.getInstance();
	 * 
	 * try { connection = DBConnect.getConnection();
	 * connection.setAutoCommit(false); // register player if (!isAnonymous &&
	 * plrBean != null) { playerId = new
	 * PlayerVerifyHelperForApp().registerPlayer( plrBean, connection); } else
	 * if (isAnonymous) { playerId = 1; // hard coded for Anonymous player }
	 * 
	 * logger.debug("Player id is " + playerId + " for that approval required");
	 * 
	 * if (playerId <= 0) { throw new LMSException(
	 * "error while player registration process player id is" + playerId); }
	 * String recIdForApp = GenerateRecieptNo
	 * .generateRequestIdDraw("DGREQUEST");
	 * 
	 * double netPwtAmt = 0.0; logger.debug("inside panel wise"); for
	 * (DrawIdBean drawIdBean : pwtDrawBean.getDrawWinList()) {
	 * 
	 * if (drawIdBean.getPanelWinList() != null) {
	 * 
	 * for (PanelIdBean panelIdBean : drawIdBean.getPanelWinList()) {
	 * 
	 * if (panelIdBean.isValid()) { int reqToOrgId = 0; String reqToOrgType =
	 * null; String remarks = null; String reqStatus = null; String
	 * approvedByType = null; int approvedByUserId = 0; int approvedByOrgId = 0;
	 * logger.debug("panelIdBean.isValid()====" + panelIdBean.isValid()); if
	 * (panelIdBean.isAppReq()) { // means approval from BO master is required
	 * // get Back office organization and user id
	 * 
	 * reqToOrgId = userInfoBean.getParentOrgId(); reqToOrgType = "BO"; remarks
	 * = "requested to BO master  For Approval"; reqStatus = "PND_MAS";
	 *  logger.debug("inside if panelIdBean.isAppReq()===" +
	 * panelIdBean.isAppReq() + remarks + reqStatus);
	 * 
	 * } else if (!isAnonymous) { // go for pending payments reqToOrgId =
	 * userInfoBean.getUserOrgId(); reqToOrgType = userInfoBean.getUserType();
	 * remarks = "Auto Approved By BO"; reqStatus = "PND_PAY"; approvedByType =
	 * "BO"; approvedByUserId = userInfoBean.getUserId(); approvedByOrgId =
	 * reqToOrgId;  logger.debug("inside else panelIdBean.isAppReq()===" +
	 * panelIdBean.isAppReq() + remarks + reqStatus); } else { reqToOrgId =
	 * userInfoBean.getUserOrgId(); reqToOrgType = userInfoBean.getUserType();
	 * remarks = "Paid as Anonymous Player"; reqStatus = "PAID"; approvedByType
	 * = "BO"; approvedByUserId = userInfoBean.getUserId(); approvedByOrgId =
	 * reqToOrgId;
	 * 
	 * }
	 * 
	 * logger.debug("Approval requested to orgId = " + reqToOrgId +
	 * "  and user type = " + reqToOrgType);
	 * 
	 * // generate TEMP receipt for Approval // GenerateRecieptNo.get
	 * 
	 * logger.debug("^^^^^^^^^***** panel wise " + panelIdBean.getWinningAmt());
	 * 
	 * // insert into Approval table if (playerType == null) { playerType =
	 * "anonymous"; } // code added for the case if player is anonymous // and
	 * no high prize String insertAppQuery =
	 * "insert into  st_dg_approval_req_master (party_type ,request_id,party_id,ticket_nbr,draw_id,panel_id,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	 * ; logger.debug("insertAppQuery = " + insertAppQuery + "@@@@@@@@@@@" +
	 * playerType.toUpperCase()); pstmt =
	 * connection.prepareStatement(insertAppQuery); pstmt.setString(1,
	 * playerType.toUpperCase()); pstmt.setString(2, recIdForApp);
	 * 
	 * boolean isPlr = "player" .equalsIgnoreCase(playerType.trim()); if (isPlr)
	 * { pstmt.setInt(3, playerId); } else { pstmt.setObject(3, null); }
	 * 
	 * pstmt.setObject(4, pwtDrawBean.getTicketNo()); pstmt.setInt(5,
	 * drawIdBean.getDrawId()); pstmt.setInt(6, panelIdBean.getPanelId());
	 * pstmt.setInt(7, pwtDrawBean.getGameId()); pstmt.setDouble(8,
	 * Double.parseDouble(panelIdBean .getWinningAmt())); pstmt.setDouble(9,
	 * 0.0); pstmt.setDouble(10, Double.parseDouble(panelIdBean
	 * .getWinningAmt())); pstmt.setString(11, reqStatus); pstmt.setString(12,
	 * userInfoBean.getUserType()); pstmt.setInt(13, userInfoBean.getUserId());
	 * pstmt.setInt(14, userInfoBean.getUserOrgId()); pstmt.setInt(15,
	 * reqToOrgId); pstmt.setString(16, reqToOrgType); if
	 * (drawIdBean.isAppReq()) { pstmt.setObject(17, null); pstmt.setObject(18,
	 * null); pstmt.setObject(19, null); pstmt.setObject(20, null);
	 * pstmt.setObject(21, null); pstmt.setObject(22, null); } else {
	 * pstmt.setString(17, approvedByType); pstmt.setInt(18, approvedByUserId);
	 * pstmt.setInt(19, approvedByOrgId); pstmt.setString(20, approvedByType);
	 * pstmt.setInt(21, approvedByOrgId); pstmt.setTimestamp(22, new
	 * java.sql.Timestamp( new java.util.Date().getTime())); }
	 * pstmt.setTimestamp(23, new java.sql.Timestamp( new
	 * java.util.Date().getTime())); pstmt.setString(24, remarks);
	 * logger.debug("insertAppQuery pppppp = " + pstmt); pstmt.executeUpdate();
	 * 
	 *  logger.debug("insertion into pwt temp request  table = " + pstmt);
	 * ResultSet rs = pstmt.getGeneratedKeys(); int reqId = 0; if (rs.next()) {
	 * reqId = rs.getInt(1); } else { throw new LMSException(
	 * "NO Data Inserted in st_pwt_approval_request_master table"); }
	 * 
	 * // insert in draw pwt inv table if (isAnonymous &&
	 * !"PND_MAS".equalsIgnoreCase(reqStatus)) { reqStatus = "CLAIM_PLR_BO"; }
	 * logger.debug("@@@@new status is" + reqStatus); String
	 * insIntoDGPwtInvQuery =
	 * "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr) values (?, ?, ?, ?, ?,?)"
	 * ; PreparedStatement insIntoDGPwtInvPstmt = connection
	 * .prepareStatement(insIntoDGPwtInvQuery); insIntoDGPwtInvPstmt.setInt(1,
	 * pwtDrawBean .getGameNo()); insIntoDGPwtInvPstmt.setString(2, pwtDrawBean
	 * .getTicketNo()); insIntoDGPwtInvPstmt.setInt(3, drawIdBean .getDrawId());
	 * insIntoDGPwtInvPstmt.setInt(4, panelIdBean .getPanelId());
	 * insIntoDGPwtInvPstmt.setDouble(5, Double
	 * .parseDouble(panelIdBean.getWinningAmt()));
	 * insIntoDGPwtInvPstmt.setString(6, reqStatus);
	 * insIntoDGPwtInvPstmt.setString(7, "Y");
	 * insIntoDGPwtInvPstmt.executeUpdate(); netPwtAmt = netPwtAmt +
	 * Double.parseDouble(panelIdBean .getWinningAmt());
	 * 
	 * if (isAnonymous && !"PND_MAS".equalsIgnoreCase(reqStatus)) { 
	 * logger.debug("before payin as Anonymous::::"); //
	 * tax=0.0,chequeNbr=null,draweeBank
	 * =null,issuingParty=null,chqDate=null,paymentType=CASH // hard coded for
	 * anonymous player int transId = boDirectPlrPwtPayment(pwtDrawBean
	 * .getTicketNo(), drawIdBean.getDrawId(), playerId, Double
	 * .parseDouble(panelIdBean .getWinningAmt()), 0.0, reqId, null, null, null,
	 * null, "CASH", userInfoBean.getUserOrgId(), userInfoBean.getUserId(),
	 * pwtDrawBean .getGameNo(), pwtDrawBean .getGameId(), connection,
	 * panelIdBean.getPanelId(), "PANEL_WISE"); if (transId > 0) { String
	 * updateAppTable =
	 * "update  st_dg_approval_req_master  set  payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ?"
	 * ; PreparedStatement pstmt = connection .prepareStatement(updateAppTable);
	 * pstmt.setString(1, "BO"); pstmt .setInt(2, userInfoBean .getUserOrgId());
	 * pstmt.setInt(3, transId); pstmt.setInt(4, reqId); logger
	 * .debug("update  st_dg_approval_req_master Query::::" + pstmt);
	 * pstmt.executeUpdate(); } }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } } // }
	 * 
	 * // Draw Game Updation of Ticket pwtDrawBean.setStatus("NORMAL_PAY");
	 * sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
	 * sReq.setServiceData(pwtDrawBean); sRes = delegate.getResponse(sReq); if
	 * (sRes.getIsSuccess()) { connection.commit();
	 * 
	 * // generete temp receipt here
	 * 
	 * GraphReportHelper graphReportHelper = new GraphReportHelper();
	 * graphReportHelper.createTextReportTempPlayerReceipt( recIdForApp, "BO",
	 * rootPath, "DRAW_GAME");
	 * 
	 * // connection.close(); pwtDrawBean.setStatus("SUCCESS"); } else {
	 * logger.debug(
	 * "***************************^^^^^^^^^^inside error while updating draw game "
	 * ); connection.close(); pwtDrawBean.setStatus("ERROR"); }
	 * 
	 * // pwtAppMap.put("recId", recIdForApp); pwtAppMap.put("PWT_RES_BEAN",
	 * pwtDrawBean);// added by amit pwtAppMap.put("GAME_NAME", Util
	 * .getGameName(pwtDrawBean.getGameNo()).toUpperCase() + "_PWT");
	 * pwtAppMap.put("isAnonymous", isAnonymous);
	 * pwtAppMap.put("NET_AMOUNT_PAID", netPwtAmt); // pwtAppMap.put("reqId",
	 * reqId); // pwtAppMap.putAll(pwtDetails);///////tttttttttttttttttt //
	 * pwtAppMap.put("remarks", remarks);
	 * 
	 * // now generate temporary receipt for player // GraphReportHelper
	 * graphHelpwr=new GraphReportHelper(); //
	 * graphHelpwr.createTextReportTempPlayerReceipt(reqId,"BO", // rootPath);
	 * 
	 * return pwtAppMap;
	 * 
	 * } catch (Exception e) { logger.error("Exception: " + e);
	 * e.printStackTrace(); throw new LMSException(e); } finally { if
	 * (connection != null) { try { connection.close(); } catch (SQLException e)
	 * { logger.error("Exception: " + e); e.printStackTrace(); throw new
	 * LMSException(e); } } } }
	 */

	public Map plrRegistrationAndApproval(UserInfoBean userInfoBean,
			MainPWTDrawBean mainPwtDrawBean, String playerType, int playerId,
			PlayerBean plrBean, String rootPath, boolean isAnonymous)
			throws LMSException {
		
		///int pwtGameNo = 0;
		int pwtGameId =0;
		Map pwtAppMap =null;
		
		ServiceResponse sRes = null;
		ServiceRequest sReq = null;
		IServiceDelegate delegate = null;

		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			// register player
			if (!isAnonymous && plrBean != null) {
				playerId = new PlayerVerifyHelperForApp().registerPlayer(
						plrBean, connection);
			} else if (isAnonymous) {
				playerId = 1; // hard coded for Anonymous player
			}

			logger.debug("Player id is " + playerId
					+ " for that approval required");

			if (playerId <= 0) {
				throw new LMSException(
						"error while player registration process player id is"
								+ playerId);
			}
			String recIdForApp = GenerateRecieptNo
					.generateRequestIdDraw("DGREQUEST");

			double netPwtAmt = 0.0;
			boolean isNormlpay = false;
			boolean isResAwaited = false;
			logger.debug("inside panel wise");
			List<PWTDrawBean> pwtDrawBeanList = mainPwtDrawBean
					.getWinningBeanList();
			for (PWTDrawBean pwtDrawBean : pwtDrawBeanList) {
				if (pwtDrawBean.isResAwaited())
					isResAwaited = true;
				double govtCommPwt =0;
				GameMasterLMSBean gameMasterLMSBean =Util.getGameMasterLMSBean(pwtDrawBean.getGameId());
				govtCommPwt = gameMasterLMSBean.getGovtCommPwt();
				if(pwtDrawBean.getTotalAmount()>=Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT")))
					pwtDrawBean.setGovtTaxAmount(govtCommPwt*0.01*pwtDrawBean.getTotalAmount());

				if ("DRAW".equalsIgnoreCase(pwtDrawBean.getPwtTicketType())) {
					// pwtGameNo = pwtDrawBean.getGameNo();
					pwtGameId=pwtDrawBean.getGameId();
					if (pwtDrawBean.getDrawWinList() != null
							&& pwtDrawBean.getDrawWinList().size() > 0) {
						for (DrawIdBean drawIdBean : pwtDrawBean
								.getDrawWinList()) {
							if (drawIdBean.getPanelWinList() != null) {

								for (PanelIdBean panelIdBean : drawIdBean
										.getPanelWinList()) {

									if (panelIdBean.isValid()) {
										isNormlpay = true;
										int reqToOrgId = 0;
										String reqToOrgType = null;
										String remarks = null;
										String reqStatus = null;
										String approvedByType = null;
										int approvedByUserId = 0;
										int approvedByOrgId = 0;
										logger
												.debug("panelIdBean.isValid()===="
														+ panelIdBean.isValid());
										if (panelIdBean.isAppReq()) {
											// means approval from BO master is
											// required
											// get Back office organization and
											// user id

											reqToOrgId = userInfoBean
													.getUserOrgId();
											reqToOrgType = "BO";
											remarks = "requested to BO master  For Approval";
											reqStatus = "PND_MAS";
											
													logger.debug("inside if panelIdBean.isAppReq()==="
															+ panelIdBean
																	.isAppReq()
															+ remarks
															+ reqStatus);

										} else if (!isAnonymous) {
											// go for pending payments
											reqToOrgId = userInfoBean
													.getUserOrgId();
											reqToOrgType = userInfoBean
													.getUserType();
											remarks = "Auto Approved By BO";
											reqStatus = "PND_PAY";
											approvedByType = "BO";
											approvedByUserId = userInfoBean
													.getUserId();
											approvedByOrgId = reqToOrgId;
											
													logger.debug("inside else panelIdBean.isAppReq()==="
															+ panelIdBean
																	.isAppReq()
															+ remarks
															+ reqStatus);
										} else {
											reqToOrgId = userInfoBean
													.getUserOrgId();
											reqToOrgType = userInfoBean
													.getUserType();
											remarks = "Paid as Anonymous Player";
											reqStatus = "PAID";
											approvedByType = "BO";
											approvedByUserId = userInfoBean
													.getUserId();
											approvedByOrgId = reqToOrgId;

										}

										logger
												.debug("Approval requested to orgId = "
														+ reqToOrgId
														+ "  and user type = "
														+ reqToOrgType);

										// generate TEMP receipt for Approval
										// GenerateRecieptNo.get

										logger
												.debug("^^^^^^^^^***** panel wise "
														+ panelIdBean
																.getWinningAmt());

										// insert into Approval table
										if (playerType == null) {
											playerType = "anonymous";
										}
										// code added for the case if player is
										// anonymous
										// and no high prize
										double tax =0;
										if(pwtDrawBean.getGovtTaxAmount()>0)
											tax = panelIdBean.getWinningAmt()*govtCommPwt*0.01;
										String insertAppQuery = "insert into  st_dg_approval_req_master (party_type ,request_id,party_id,ticket_nbr,draw_id,panel_id,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
										logger.debug("insertAppQuery = "
												+ insertAppQuery
												+ "@@@@@@@@@@@"
												+ playerType.toUpperCase());
										pstmt = connection
												.prepareStatement(insertAppQuery);
										pstmt.setString(1, playerType
												.toUpperCase());
										pstmt.setString(2, recIdForApp);

										boolean isPlr = "player"
												.equalsIgnoreCase(playerType
														.trim());
										if (isPlr) {
											pstmt.setInt(3, playerId);
										} else {
											pstmt.setObject(3, null);
										}
										// ticket is entered with reprint count
										// in Approval req master on 04/04/2011
										pstmt.setObject(4,pwtDrawBean.getTicketNo()+ pwtDrawBean.getReprintCount());
										pstmt.setInt(5, drawIdBean.getDrawId());
										pstmt.setInt(6, panelIdBean
												.getPanelId());
										pstmt
												.setInt(7, pwtDrawBean
														.getGameId());
										pstmt.setDouble(8, CommonMethods
												.fmtToTwoDecimal(panelIdBean
														.getWinningAmt()));
										pstmt.setDouble(9, tax);
										pstmt.setDouble(10, panelIdBean
												.getWinningAmt()-tax);
										pstmt.setString(11, reqStatus);
										pstmt.setString(12, userInfoBean
												.getUserType());
										pstmt.setInt(13, userInfoBean
												.getUserId());
										pstmt.setInt(14, userInfoBean
												.getUserOrgId());
										pstmt.setInt(15, reqToOrgId);
										pstmt.setString(16, reqToOrgType);
										if (drawIdBean.isAppReq()) {
											pstmt.setObject(17, null);
											pstmt.setObject(18, null);
											pstmt.setObject(19, null);
											pstmt.setObject(20, null);
											pstmt.setObject(21, null);
											pstmt.setObject(22, null);
										} else {
											pstmt.setString(17, approvedByType);
											pstmt.setInt(18, approvedByUserId);
											pstmt.setInt(19, approvedByOrgId);
											pstmt.setString(20, approvedByType);
											pstmt.setInt(21, approvedByOrgId);
											pstmt
													.setTimestamp(
															22,
															new java.sql.Timestamp(
																	new java.util.Date()
																			.getTime()));
										}
										pstmt.setTimestamp(23,
												new java.sql.Timestamp(
														new java.util.Date()
																.getTime()));
										pstmt.setString(24, remarks);
										logger.debug("insertAppQuery pppppp = "
												+ pstmt);
										pstmt.executeUpdate();

										
												logger.debug("insertion into pwt temp request  table = "
														+ pstmt);
										ResultSet rs = pstmt.getGeneratedKeys();
										int reqId = 0;
										if (rs.next()) {
											reqId = rs.getInt(1);
										} else {
											throw new LMSException(
													"NO Data Inserted in st_pwt_approval_request_master table");
										}

										// insert in draw pwt inv table
										if (isAnonymous
												&& !"PND_MAS"
														.equalsIgnoreCase(reqStatus)) {
											reqStatus = "CLAIM_PLR_BO";
										}
										logger.debug("@@@@new status is"
												+ reqStatus);
										String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr) values (?, ?, ?, ?, ?,?)";
										PreparedStatement insIntoDGPwtInvPstmt = connection
												.prepareStatement(insIntoDGPwtInvQuery);
										insIntoDGPwtInvPstmt.setInt(1,
												pwtDrawBean.getGameId());
										insIntoDGPwtInvPstmt.setString(2,
												pwtDrawBean.getTicketNo());
										insIntoDGPwtInvPstmt.setInt(3,
												drawIdBean.getDrawId());
										insIntoDGPwtInvPstmt.setInt(4,
												panelIdBean.getPanelId());
										insIntoDGPwtInvPstmt
												.setDouble(
														5,
														CommonMethods
																.fmtToTwoDecimal(panelIdBean
																		.getWinningAmt()));
										insIntoDGPwtInvPstmt.setString(6,
												reqStatus);
										insIntoDGPwtInvPstmt.setString(7, "Y");
										insIntoDGPwtInvPstmt.executeUpdate();
										netPwtAmt = netPwtAmt
												+ CommonMethods
														.fmtToTwoDecimal(panelIdBean
																.getWinningAmt()-tax);

										if (isAnonymous
												&& !"PND_MAS"
														.equalsIgnoreCase(reqStatus)) {
											
													logger.debug("before payin as Anonymous::::");
											// tax=0.0,chequeNbr=null,draweeBank=null,issuingParty=null,chqDate=null,paymentType=CASH
											// hard coded for anonymous player
											long transId = boDirectPlrPwtPayment(
													pwtDrawBean.getTicketNo(),
													drawIdBean.getDrawId(),
													playerId,
													CommonMethods
															.fmtToTwoDecimal(panelIdBean
																	.getWinningAmt()),
																	tax,
													reqId,
													null,
													null,
													null,
													null,
													"CASH",
													userInfoBean.getUserOrgId(),
													userInfoBean.getUserId(),
													pwtDrawBean.getGameNo(),
													pwtDrawBean.getGameId(),
													connection, panelIdBean
															.getPanelId(),
													"PANEL_WISE");
											if (transId > 0) {
												String updateAppTable = "update  st_dg_approval_req_master  set  payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ?";
												PreparedStatement pstmt = connection
														.prepareStatement(updateAppTable);
												pstmt.setString(1, "BO");
												pstmt.setInt(2, userInfoBean
														.getUserOrgId());
												pstmt.setLong(3, transId);
												pstmt.setInt(4, reqId);
												logger
														.debug("update  st_dg_approval_req_master Query::::"
																+ pstmt);
												pstmt.executeUpdate();
											}
										}

									}

								}

							}
						}
						// }

						if (isNormlpay) {
							sRes = new ServiceResponse();
							sReq = new ServiceRequest();
							sReq.setServiceName(ServiceName.PWT_MGMT);
							sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
							delegate = ServiceDelegate.getInstance();
							pwtDrawBean.setStatus("NORMAL_PAY");

							// Draw Game Updation of Ticket
							sReq.setServiceData(pwtDrawBean);
							sRes = delegate.getResponse(sReq);
							if (sRes.getIsSuccess()) {
								pwtDrawBean.setStatus("SUCCESS");
							} else {
										logger.debug("***************************^^^^^^^^^^inside error while updating draw game ");
								connection.close();
								pwtDrawBean.setStatus("ERROR");
								throw new LMSException(
										"Pwt not updated in DGE...");
							}
						}
					}
				} else if ("RAFFLE".equalsIgnoreCase(pwtDrawBean
						.getPwtTicketType())) {
					// call raffle process here
					String rafflwPwtAmtNisResAwaited = doRafflePWTPaymet(
							pwtDrawBean, userInfoBean, isAnonymous, playerType,
							recIdForApp, playerId, connection);
					double rafflwPwtAmt = Double
							.parseDouble((rafflwPwtAmtNisResAwaited.split(":")[0]));
					netPwtAmt = netPwtAmt + rafflwPwtAmt;
					if (!isResAwaited)
						isResAwaited = Boolean
								.parseBoolean(rafflwPwtAmtNisResAwaited
										.split(":")[1]);
				}

			}

			// call reprint process if ticket has to be reprint

			if (isResAwaited && netPwtAmt > 0) {
				DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
				String ticketNumber=mainPwtDrawBean.getTicketNo();
				int len = mainPwtDrawBean.getTicketNo().length();
				Object gameBean = helper.reprintTicket(userInfoBean, true,
						/*len==ConfigurationVariables.barcodeCount?ticketNumber.substring(0, len - 4):ticketNumber.substring(0, len - 2)*/
						Util.getTktWithoutRpcNBarCodeCount(ticketNumber,len),
						false,null,null);
				mainPwtDrawBean.setPurchaseBean(gameBean);
				mainPwtDrawBean.setReprint(true);
				mainPwtDrawBean.setStatus("SUCCESS");
			}

			connection.commit();
			pwtAppMap = new TreeMap();
			if ("DRAW".equalsIgnoreCase(mainPwtDrawBean.getPwtTicketType())) {
				pwtAppMap.put("recId", recIdForApp);
				// pwtAppMap.put("PWT_RES_BEAN", pwtDrawBean);// added by amit

				pwtAppMap.put("PWT_RES_BEAN", mainPwtDrawBean);
				pwtAppMap.put("GAME_NAME", Util.getGameName(pwtGameId).toUpperCase()+ "_PWT");
				pwtAppMap.put("isAnonymous", isAnonymous);
				pwtAppMap.put("NET_AMOUNT_PAID", netPwtAmt);
				// pwtAppMap.put("reqId", reqId);
				// pwtAppMap.putAll(pwtDetails);///////tttttttttttttttttt
				// pwtAppMap.put("remarks", remarks);

				// now generate temporary receipt for player
				// GraphReportHelper graphHelpwr=new GraphReportHelper();
				// graphHelpwr.createTextReportTempPlayerReceipt(reqId,"BO",
				// rootPath);
			} else if ("RAFFLE".equalsIgnoreCase(mainPwtDrawBean
					.getPwtTicketType())) {
				pwtAppMap.put("recId", recIdForApp);
				pwtAppMap.put("PWT_RES_BEAN", mainPwtDrawBean);
				pwtAppMap.put("GAME_NAME", Util.getGameName(
						Util.getGamenoFromTktnumber(mainPwtDrawBean.getTicketNo()))
						.toUpperCase()
						+ "_PWT");
				pwtAppMap.put("isAnonymous", isAnonymous);
				pwtAppMap.put("NET_AMOUNT_PAID", netPwtAmt);

			}
			return pwtAppMap;

		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					e.printStackTrace();
					throw new LMSException(e);
				}
			}
		}
	}

	public String doRafflePWTPaymet(PWTDrawBean pwtDrawBean,
			UserInfoBean userInfoBean, boolean isAnonymous, String playerType,
			String recIdForApp, int playerId, Connection connection)
			throws LMSException {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PWT_MGMT);
		// sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		double netPwtAmt = 0.0;
		boolean isNormalPay = false;
		boolean isResAwaited = false;
		try {
			if (pwtDrawBean.getRaffleDrawIdBeanList() != null) {
				List<RaffleDrawIdBean> raffleDrawIdBeanList = pwtDrawBean
						.getRaffleDrawIdBeanList();
				for (RaffleDrawIdBean raffleDrawIdBean : raffleDrawIdBeanList) {
					
					if (raffleDrawIdBean.isResAwaited())
						isResAwaited = true;

					if (raffleDrawIdBean.isValid()) {
						isNormalPay = true;
						int reqToOrgId = 0;
						String reqToOrgType = null;
						String remarks = null;
						String reqStatus = null;
						String approvedByType = null;
						int approvedByUserId = 0;
						int approvedByOrgId = 0;
						logger.debug("panelIdBean.isValid()===="
								+ raffleDrawIdBean.isValid());
						if (raffleDrawIdBean.isAppReq()) {
							// means approval from BO master is required
							// get Back office organization and user id

							reqToOrgId = userInfoBean.getParentOrgId();
							reqToOrgType = "BO";
							remarks = "requested to BO master  For Approval";
							reqStatus = "PND_MAS";
							
									logger.debug("inside if panelIdBean.isAppReq()==="
											+ raffleDrawIdBean.isAppReq()
											+ remarks + reqStatus);

						} else if (!isAnonymous) {
							// go for pending payments
							reqToOrgId = userInfoBean.getUserOrgId();
							reqToOrgType = userInfoBean.getUserType();
							remarks = "Auto Approved By BO";
							reqStatus = "PND_PAY";
							approvedByType = "BO";
							approvedByUserId = userInfoBean.getUserId();
							approvedByOrgId = reqToOrgId;
							
									logger.debug("inside else panelIdBean.isAppReq()==="
											+ raffleDrawIdBean.isAppReq()
											+ remarks + reqStatus);
						} else {
							reqToOrgId = userInfoBean.getUserOrgId();
							reqToOrgType = userInfoBean.getUserType();
							remarks = "Paid as Anonymous Player";
							reqStatus = "PAID";
							approvedByType = "BO";
							approvedByUserId = userInfoBean.getUserId();
							approvedByOrgId = reqToOrgId;

						}

						logger.debug("Approval requested to orgId = "
								+ reqToOrgId + "  and user type = "
								+ reqToOrgType);

						// generate TEMP receipt for Approval
						// GenerateRecieptNo.get

						logger.debug("^^^^^^^^^***** panel wise "
								+ raffleDrawIdBean.getWinningAmt());

						// insert into Approval table
						if (playerType == null) {
							playerType = "anonymous";
						}
						// code added for the case if player is anonymous
						// and no high prize
						String insertAppQuery = "insert into  st_dg_approval_req_master (party_type ,request_id,party_id,ticket_nbr,draw_id,panel_id,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						logger.debug("insertAppQuery = " + insertAppQuery
								+ "@@@@@@@@@@@" + playerType.toUpperCase());
						pstmt = connection.prepareStatement(insertAppQuery);
						pstmt.setString(1, playerType.toUpperCase());
						pstmt.setString(2, recIdForApp);

						boolean isPlr = "player".equalsIgnoreCase(playerType
								.trim());
						if (isPlr) {
							pstmt.setInt(3, playerId);
						} else {
							pstmt.setObject(3, null);
						}

						pstmt.setObject(4, raffleDrawIdBean
								.getRaffleTicketNumberInDB()+pwtDrawBean.getReprintCount());//ticket is inserted with reprint count in DB 14/04/2011 
						pstmt.setInt(5, raffleDrawIdBean.getDrawId());
						pstmt.setInt(6, 0);
						pstmt.setInt(7, raffleDrawIdBean.getRaffleGameno());
						pstmt.setDouble(8, Double.parseDouble(raffleDrawIdBean
								.getWinningAmt()));
						pstmt.setDouble(9, 0.0);
						pstmt.setDouble(10, Double.parseDouble(raffleDrawIdBean
								.getWinningAmt()));
						pstmt.setString(11, reqStatus);
						pstmt.setString(12, userInfoBean.getUserType());
						pstmt.setInt(13, userInfoBean.getUserId());
						pstmt.setInt(14, userInfoBean.getUserOrgId());
						pstmt.setInt(15, reqToOrgId);
						pstmt.setString(16, reqToOrgType);
						if (raffleDrawIdBean.isAppReq()) {
							pstmt.setObject(17, null);
							pstmt.setObject(18, null);
							pstmt.setObject(19, null);
							pstmt.setObject(20, null);
							pstmt.setObject(21, null);
							pstmt.setObject(22, null);
						} else {
							pstmt.setString(17, approvedByType);
							pstmt.setInt(18, approvedByUserId);
							pstmt.setInt(19, approvedByOrgId);
							pstmt.setString(20, approvedByType);
							pstmt.setInt(21, approvedByOrgId);
							pstmt.setTimestamp(22, new java.sql.Timestamp(
									new java.util.Date().getTime()));
						}
						pstmt.setTimestamp(23, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						pstmt.setString(24, remarks);
						logger.debug("insertAppQuery pppppp = " + pstmt);
						pstmt.executeUpdate();

						
								logger.debug("insertion into pwt temp request  table = "
										+ pstmt);
						ResultSet rs = pstmt.getGeneratedKeys();
						int reqId = 0;
						if (rs.next()) {
							reqId = rs.getInt(1);
						} else {
							throw new LMSException(
									"NO Data Inserted in st_pwt_approval_request_master table");
						}

						// insert in draw pwt inv table
						if (isAnonymous
								&& !"PND_MAS".equalsIgnoreCase(reqStatus)) {
							reqStatus = "CLAIM_PLR_BO";
						}
						logger.debug("@@@@new status is" + reqStatus);
						String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr) values (?, ?, ?, ?, ?,?)";
						PreparedStatement insIntoDGPwtInvPstmt = connection
								.prepareStatement(insIntoDGPwtInvQuery);
						insIntoDGPwtInvPstmt.setInt(1, raffleDrawIdBean
								.getRaffleGameId());
						insIntoDGPwtInvPstmt.setString(2, raffleDrawIdBean
								.getRaffleTicketNumberInDB());
						insIntoDGPwtInvPstmt.setInt(3, raffleDrawIdBean
								.getDrawId());
						insIntoDGPwtInvPstmt.setInt(4, 0);
						insIntoDGPwtInvPstmt.setDouble(5, Double
								.parseDouble(raffleDrawIdBean.getWinningAmt()));
						insIntoDGPwtInvPstmt.setString(6, reqStatus);
						insIntoDGPwtInvPstmt.setString(7, "Y");
						insIntoDGPwtInvPstmt.executeUpdate();
						netPwtAmt = netPwtAmt
								+ Double.parseDouble(raffleDrawIdBean
										.getWinningAmt());

						if (isAnonymous
								&& !"PND_MAS".equalsIgnoreCase(reqStatus)) {
							logger.debug("before payin as Anonymous::::");
							// tax=0.0,chequeNbr=null,draweeBank=null,issuingParty=null,chqDate=null,paymentType=CASH
							// hard coded for anonymous player
						long	transId = boDirectPlrPwtPayment(
									raffleDrawIdBean
											.getRaffleTicketNumberInDB(),
									raffleDrawIdBean.getDrawId(), playerId,
									Double.parseDouble(raffleDrawIdBean
											.getWinningAmt()), 0.0, reqId,
									null, null, null, null, "CASH",
									userInfoBean.getUserOrgId(), userInfoBean
											.getUserId(), raffleDrawIdBean
											.getRaffleGameno(),
									raffleDrawIdBean.getRaffleGameId(),
									connection, 0, "PANEL_WISE");
							if (transId > 0) {
								String updateAppTable = "update  st_dg_approval_req_master  set  payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ?";
								PreparedStatement pstmt = connection
										.prepareStatement(updateAppTable);
								pstmt.setString(1, "BO");
								pstmt.setInt(2, userInfoBean.getUserOrgId());
								pstmt.setLong(3, transId);
								pstmt.setInt(4, reqId);
								logger
										.debug("update  st_dg_approval_req_master Query::::"
												+ pstmt);
								pstmt.executeUpdate();
							}
						}

					}

				}

			}

			// }

			if (isNormalPay) { // Draw Game Updation of Ticket
				pwtDrawBean.setStatus("NORMAL_PAY");
				sReq.setServiceMethod(ServiceMethodName.RAFFLE_PWT_UPDATE);
				sReq.setServiceData(pwtDrawBean);
				sRes = delegate.getResponse(sReq);
				if (sRes.getIsSuccess()) {
					pwtDrawBean.setStatus("SUCCESS");
				} else {
					
							logger.debug("***************************^^^^^^^^^^inside error while updating draw game ");
					connection.close();
					pwtDrawBean.setStatus("ERROR");
				}
			}
			return netPwtAmt + ":" + isResAwaited;

		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public boolean updateDgTicketReq(int userOrgId, long transaction_id,
			int taskId, Connection connection) throws LMSException {
		logger.debug(" updateDgTicketReq called");
		try {
			// update status of of requested id entries into
			// st_pwt_approval_request_master
			String updateAppTable = "update  st_dg_approval_req_master  set req_status ='PAID', "
					+ "remarks ='Payment Done', payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ? and req_status='PND_PAY'";
			PreparedStatement pstmt = connection
					.prepareStatement(updateAppTable);
			pstmt.setString(1, "BO");
			pstmt.setInt(2, userOrgId);
			pstmt.setLong(3, transaction_id);
			pstmt.setInt(4, taskId);
			int j = pstmt.executeUpdate();
			logger.debug("total row updated = " + j + " ,  requested id "
					+ taskId + " status updated = " + pstmt);
			if (j < 1) {
				throw new LMSException(
						"st_dg_approval_req_master table not updated.");
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public String verifyAndSaveTicketDirPlr1(UserInfoBean userInfoBean,
			String pwtAmtForMasterApproval, PWTDrawBean pwtDrawBean,
			String highPrizeScheme) throws LMSException {

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PWT_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PRZE_WINNING_TICKET);

		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			PreparedStatement stmt = connection
					.prepareStatement("select ref_merchant_id  from st_lms_service_master where service_code='DG' and status='ACTIVE'");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				pwtDrawBean.setRefMerchantId(rs.getString("ref_merchant_id"));
			}
			sReq.setServiceData(pwtDrawBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();

			String returnType;

			sRes = delegate.getResponse(sReq);

			Type type = new TypeToken<PWTDrawBean>(){}.getType();
			pwtDrawBean = (PWTDrawBean)new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
			
//			pwtDrawBean = (PWTDrawBean) sRes.getResponseData();
			if (sRes.getIsSuccess()) {
				
				if(Boolean.parseBoolean((String)com.skilrock.lms.common.Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_PWT_AMT")))
					CommonMethods.doRoundingForPwtAmt(pwtDrawBean);
				
				int gameId = getGameIdFromGameNbr(pwtDrawBean.getGameNo(),
						connection);
				pwtDrawBean.setGameId(gameId);
				returnType = verifyPwtTicketDirPlr(gameId, connection,
						userInfoBean, pwtAmtForMasterApproval, pwtDrawBean,
						highPrizeScheme);
				pwtDrawBean.setStatus("SUCCESS");

			} else {
				returnType = "input";
				
						logger.debug("invalid   ticket  returned from draw Game engine");
			}
			return returnType;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

	}

	/*
	 * public String doDirectPlrPwtPayAtBO(int gameId, int playerId, double
	 * pwtAmt,double tax,String virnCode,int taskId,String chequeNbr,String
	 * draweeBank,String issuingParty,java.sql.Date chqDate,String
	 * paymentType,int userOrgId,int userId,int gameNbr,String ticketNbr,String
	 * boOrgName,String rootPath) throws LMSException{
	 * 
	 * int transaction_id = -1; double net_amt = 0.0; List<Integer>
	 * transIdList=new ArrayList<Integer>(); DBConnect dbConnect = new
	 * DBConnect(); String receiptIdNGeneratedNo; try { connection =
	 * dbConnect.getConnection(); connection.setAutoCommit(false); //insert into
	 * LMS transaction master pstmt =
	 * connection.prepareStatement(QueryManager.insertInLMSTransactionMaster());
	 * pstmt.setString(1,"BO"); pstmt.executeUpdate();
	 * 
	 * result = pstmt.getGeneratedKeys(); if(result.next()){ transaction_id =
	 * result.getInt(1); transIdList.add(transaction_id);
	 * 
	 * 
	 * //insert in st bo transaction master pstmt =
	 * connection.prepareStatement(QueryManager.insertInBOTransactionMaster());
	 * pstmt.setInt(1,transaction_id); pstmt.setInt(2,userId);
	 * pstmt.setInt(3,userOrgId); pstmt.setString(4, "PLAYER"); pstmt.setInt(5,
	 * playerId); pstmt.setTimestamp(6,new java.sql.Timestamp(new
	 * java.util.Date().getTime())); pstmt.setString(7, "PWT_PLR");
	 * pstmt.executeUpdate();
	 * 
	 * net_amt = pwtAmt -tax; pstmt =
	 * connection.prepareStatement(QueryManager.getST5DirectPlayerTransactionQuery
	 * ()); pstmt.setInt(1, taskId); pstmt.setString(2,ticketNbr);
	 * pstmt.setString(3,virnCode); pstmt.setInt(4, transaction_id);
	 * pstmt.setInt(5, gameId); pstmt.setInt(6, playerId); pstmt.setDouble(7,
	 * pwtAmt); pstmt.setDouble(8, tax); pstmt.setDouble(9, net_amt);
	 * pstmt.setTimestamp(10, new java.sql.Timestamp(new
	 * java.util.Date().getTime())); pstmt.setString(11, paymentType);
	 * 
	 * if("cash".equalsIgnoreCase(paymentType) ||
	 * "TPT".equalsIgnoreCase(paymentType)){ pstmt.setObject(12, null);
	 * pstmt.setObject(13, null); pstmt.setObject(14, null); pstmt.setObject(15,
	 * null); }else if("cheque".equalsIgnoreCase(paymentType)){
	 * pstmt.setString(12, chequeNbr); pstmt.setDate(13, chqDate);
	 * pstmt.setString(14, draweeBank); pstmt.setString(15, issuingParty); }
	 * pstmt.executeUpdate();
	 * 
	 * 
	 * //update total prize remaining of that game
	 * //GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt, "CLAIM_PLR",
	 * virnCode, connection,gameNbr);
	 * GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt, "CLAIM_PLR_BO",
	 * virnCode, connection,gameNbr);
	 * 
	 * //update pwt ticket inv table CommonFunctionsHelper commonHelper=new
	 * CommonFunctionsHelper(); commonHelper.updateTicketInvTable(ticketNbr,
	 * ticketNbr.split("-")[0]+"-"+ticketNbr.split("-")[1], gameNbr, gameId,
	 * "CLAIM_PLR_BO", userId, userOrgId,"UPDATE", connection);
	 * 
	 * //update pwt inv table commonHelper.updateVirnStatus(gameNbr,
	 * "CLAIM_PLR_BO", gameId, virnCode, connection); // update status of of
	 * requested id entries into st_pwt_approval_request_master String
	 * updateAppTable = "update st_pwt_approval_request_master set req_status
	 * ='PAID', " + "remarks ='Payment Done', payment_done_by_type =?,
	 * payment_done_by =? ,transaction_id=? where task_id = ?"; pstmt =
	 * connection.prepareStatement(updateAppTable); pstmt.setString(1, "BO");
	 * pstmt.setInt(2, userOrgId); pstmt.setInt(3, transaction_id);
	 * pstmt.setInt(4, taskId); int j = pstmt.executeUpdate();
	 * logger.debug("total row updated = "+j+" , requested id "+ taskId+" status
	 * updated = "+pstmt); if(j<1) throw new
	 * LMSException("st_pwt_approval_request_master table not updated.");
	 * 
	 * //now generate Recipet at BO End receiptIdNGeneratedNo =
	 * commonHelper.generateReceiptBo(connection, playerId, "PLAYER",
	 * transIdList); if(receiptIdNGeneratedNo!=null) connection.commit(); else
	 * throw new LMSException("Error during generating receipts for PWT :: ");
	 * if(Integer.parseInt((receiptIdNGeneratedNo.split("-")[0])) > 0){
	 * GraphReportHelper graphReportHelper = new GraphReportHelper();
	 * //graphReportHelper
	 * .createTextReportPlayer(Integer.parseInt((receiptIdNGeneratedNo
	 * .split("-")[0])),rootPath,boOrgName,pwtAmt,tax,playerId,userOrgId);
	 * graphReportHelper.createTextReportPlayer(taskId,rootPath,"DRAW_GAME"); }
	 * 
	 * }else throw new LMSException("Exception :: Transaction Id is not
	 * generated:: transaction Id is " + transaction_id); return
	 * receiptIdNGeneratedNo.split("-")[1]; } catch (SQLException e) {
	 * e.printStackTrace(); throw new LMSException(e); } finally { try { if
	 * (connection != null) { connection.close(); } } catch (SQLException se) {
	 * se.printStackTrace(); throw new LMSException(se); } } }
	 */

	public MainPWTDrawBean newMethod(MainPWTDrawBean mainPwtBean,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval,
			String highPrizeScheme, String refMerchantId, String highPrizeAmt) {

		ArrayList<PWTDrawBean> winningBeanList = null;
		Connection connection = null;
		String ticketNumber = null;
		boolean byPassDates=false;
		int barCodeCount=-1;
		
		try {
			
			// ADDED 3 FOR EITHER CASE FOR PWT MODE
			ticketNumber = Util.getTicketNumber(mainPwtBean.getTicketNo(), 3); 
			if (ticketNumber.equals("ERROR") || "".equals(ticketNumber)) {
				mainPwtBean.setStatus("ERROR_INVALID");
				return mainPwtBean;
			}
			// get game number from ticket number
			int gameNo = Util.getGamenoFromTktnumber(ticketNumber);
			if (gameNo <= 0) {
				mainPwtBean.setStatus("ERROR_INVALID");
				return mainPwtBean;
			}
			// get game type from ticket type
			int gameId =Util.getGameIdFromGameNumber(gameNo);
			String gameType = Util.getGameType(gameId);
			if (gameType == null) {
				mainPwtBean.setStatus("ERROR_INVALID");
				return mainPwtBean;
			}

			connection = DBConnect.getConnection();

			if(!Util.canClaimBO(ticketNumber, connection)) {
				mainPwtBean.setStatus("UN_AUTH");
				return mainPwtBean;
			}

			// Get The BarCode If Reqired
			if (mainPwtBean.getInpType() == 1 || mainPwtBean.getTicketNo()
							.length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) {
				barCodeCount = Integer
						.parseInt(Util.getBarCodeCountFromTicketNumber(mainPwtBean
								.getTicketNo()));
			}
			
			mainPwtBean.setMainTktGameNo(gameNo);
			mainPwtBean.setGameId(gameId);
			winningBeanList = new ArrayList<PWTDrawBean>();
			if (gameType.equalsIgnoreCase("RAFFLE")) {
				// call raffle verify process
				mainPwtBean.setPwtTicketType("RAFFLE");
				PWTDrawBean promoWinBean = new PWTDrawBean();
				promoWinBean.setTicketNo(mainPwtBean.getTicketNo());
				promoWinBean.setPartyId(userInfoBean.getUserOrgId());
				promoWinBean.setPartyType(userInfoBean.getUserType());
				promoWinBean.setUserId(userInfoBean.getUserId());
				promoWinBean.setRefMerchantId(refMerchantId);
				promoWinBean.setPwtTicketType("ORIGINAL");
				// here identify the type of request for raffle or for
				// draw(standard)
				ServiceResponse sRes = new ServiceResponse();
				ServiceRequest sReq = new ServiceRequest();
				IServiceDelegate delegate = ServiceDelegate.getInstance();
				sReq.setServiceName(ServiceName.DRAWGAME);
				sReq
						.setServiceMethod(ServiceMethodName.RAFFLE_PRZE_WINNING_TICKET);
				sReq.setServiceData(promoWinBean);
				sRes = delegate.getResponse(sReq);
				promoWinBean = (PWTDrawBean) sRes.getResponseData();
				List raffleDrawIdBeanList = promoWinBean
						.getRaffleDrawIdBeanList();
				if (raffleDrawIdBeanList.size() < 1) {
					promoWinBean.setStatus("ERROR");
				}
				RaffleDrawIdBean raffleWinningBean = (RaffleDrawIdBean) raffleDrawIdBeanList
						.get(0);
				if (sRes.getIsRaffleSuccess()) {

					if (raffleWinningBean.getStatus().equalsIgnoreCase("ERROR")) {
						raffleWinningBean.setStatus("ERROR");
						mainPwtBean.setStatus("ERROR");
						return mainPwtBean;
					} else if (raffleWinningBean.getStatus().equalsIgnoreCase(
							"CANCELLED")) {
						raffleWinningBean.setStatus("CANCELLED");
						mainPwtBean.setStatus("CANCELLED");
						return mainPwtBean;
					}

					verifyRafflePwtBean(promoWinBean, pwtAmtForMasterApproval,
							connection);
					promoWinBean.setStatus("SUCCESS");
				} else {
					promoWinBean.setValid(false);
					promoWinBean.setStatus("ERROR");
				}
				winningBeanList.add(promoWinBean);
				mainPwtBean.setWinningBeanList(winningBeanList);

			} else {
				if(LMSFilterDispatcher.isByPassDatesRequired)
					byPassDates=BankUtil.isBypassPWTDates(userInfoBean.getUserId(), connection);
				mainPwtBean.setPwtTicketType("DRAW");
				PWTDrawBean drawScheduleBean = new PWTDrawBean();
				drawScheduleBean.setByPassDates(byPassDates);
				drawScheduleBean.setTicketNo(ticketNumber);
				drawScheduleBean.setBarCodeCount(barCodeCount);
				drawScheduleBean.setPartyId(userInfoBean.getUserOrgId());
				drawScheduleBean.setUserId(userInfoBean.getUserId());
				drawScheduleBean.setPartyType(userInfoBean.getUserType());
				drawScheduleBean.setRefMerchantId(refMerchantId);

				drawScheduleBean = verifyAndSaveTicketNoDirPlr(
						drawScheduleBean, userInfoBean,
						pwtAmtForMasterApproval, highPrizeScheme, highPrizeAmt);
				winningBeanList.add(drawScheduleBean);

				if ("SUCCESS".equalsIgnoreCase(drawScheduleBean.getStatus())) {
					String raffleTktType = getRaffleTktTypeFromgameNbr(gameNo,
							connection);
					if ("REFERENCE".equalsIgnoreCase(raffleTktType)) {
						PWTDrawBean pwtPomoBean = null;
						pwtPomoBean = verifyRaffleTkt(
								mainPwtBean.getTicketNo(), gameNo,
								userInfoBean, refMerchantId,
								pwtAmtForMasterApproval, connection);
						if (pwtPomoBean != null) {
							winningBeanList.add(pwtPomoBean);
						}
					}
				}
				mainPwtBean.setWinningBeanList(winningBeanList);
			}
			// here verify the total ticket amount
			mainPwtBean = getTotalPWTTkeAmt(mainPwtBean, connection,
					pwtAmtForMasterApproval,highPrizeAmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
		return mainPwtBean;
	}

	private void verifyRafflePwtBean(PWTDrawBean winBean,
			String pwtAmtForMasterApproval, Connection connection) {
		try {
			boolean isMasAppReq = false;
			// if scheme is panel wise winning
			String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
			logger.debug("HIGH_PRIZE_AMT" + highPrizeAmt);

			double totlraffleAmout = 0.0;
			if (winBean.getRaffleDrawIdBeanList() != null) {
				for (Object raffleDrawidBeanObj : winBean
						.getRaffleDrawIdBeanList()) {
					RaffleDrawIdBean raffleDrawidBean = (RaffleDrawIdBean) raffleDrawidBeanObj;

					int drawId = raffleDrawidBean.getDrawId();
					int rafflegameNo = raffleDrawidBean.getRaffleGameno();
					String raffleTicketNumber = raffleDrawidBean
							.getRaffleTicketNumberInDB();
					String pwtAmount = raffleDrawidBean.getWinningAmt();
					String prizeStatus = raffleDrawidBean.getStatus();

					if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
							|| prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
						totlraffleAmout = totlraffleAmout
								+ Double.parseDouble(pwtAmount);
						isMasAppReq = Double.parseDouble(pwtAmount) > Double
								.parseDouble(pwtAmtForMasterApproval);
						if (isMasAppReq) {
							raffleDrawidBean.setAppReq(isMasAppReq);
							raffleDrawidBean.setPwtStatus("MAS_APP_REQ");
							logger.debug("MAS_APP_REQ***********");
						}
						raffleDrawidBean.setStatus("NORMAL_PAY");

						if (Double.parseDouble(pwtAmount) > Double
								.parseDouble(highPrizeAmt)) {
							raffleDrawidBean.setHighPrize(true);
							logger.debug("isHighPrize"
									+ raffleDrawidBean.isHighPrize());
						} else {
							raffleDrawidBean.setWinTkt(true);
						}
					} else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {
						raffleDrawidBean.setStatus("CLAIMED");
						PreparedStatement pstmt = connection
								.prepareStatement("select status from st_dg_pwt_inv_? where ticket_nbr=? and draw_id=? and panel_id=?");
						pstmt.setInt(1, rafflegameNo);
						pstmt.setString(2, raffleTicketNumber);
						pstmt.setInt(3, drawId);
						pstmt.setInt(4, 0);
						logger.debug(pstmt);
						ResultSet resultSet = pstmt.executeQuery();
						if (resultSet.next()) {
							prizeStatus = resultSet.getString("status");
							if (prizeStatus.equalsIgnoreCase("MISSING")) {
								raffleDrawidBean.setStatus("MISSING");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("REQUESTED")) {
								raffleDrawidBean.setStatus("REQUESTED");
							} else if (prizeStatus.equalsIgnoreCase("PND_MAS")) {
								raffleDrawidBean.setStatus("PND_MAS");
							} else if (prizeStatus.equalsIgnoreCase("PND_PAY")) {
								raffleDrawidBean.setStatus("PND_PAY");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_BO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_CLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CANCELLED_PERMANENT")) {
								raffleDrawidBean
										.setStatus("CANCELLED_PERMANENT");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT_CLM_AUTO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else {
								raffleDrawidBean.setStatus("UNDIFINED");
							}
						}

					} else if (prizeStatus.equalsIgnoreCase("NON WIN")) {
						raffleDrawidBean.setStatus("NON WIN");

					}

				}
			}

			// winBean.setGameDispName(Util.getGameDisplayName(winBean.getGameNo()));
			// winBean.setGameId(Util.getGameId(winBean.getGameNo()));
			// winBean.setTotalAmount(totPwtAmt);
			// winBean.setStatus("SUCCESS");
			winBean.setTotalAmount(totlraffleAmout);
			winBean.setStatus("SUCCESS");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public PWTDrawBean verifyRaffleTkt(String ticketNumber, int gameNo,
			UserInfoBean userInfoBean, String refMerchantId,
			String pwtAmtForMasterApproval, Connection connection) {

		int len = ticketNumber.length();
		List<String> promoTicketList = orgOnLineSaleCreditUpdation
				.getAssociatedPromoTicket(ticketNumber.substring(0,
						len - 1));
		for (int i = 0; i < promoTicketList.size(); i++) {
			PWTDrawBean promoWinBean = new PWTDrawBean();
			promoWinBean.setTicketNo(promoTicketList.get(i) + "0");
			promoWinBean.setPartyId(userInfoBean.getUserOrgId());
			promoWinBean.setPartyType(userInfoBean.getUserType());
			promoWinBean.setUserId(userInfoBean.getUserId());
			promoWinBean.setRefMerchantId(refMerchantId);
			promoWinBean.setPwtTicketType("REFERENCE");
			// here identify the type of request for raffle or for
			// draw(standard)
			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sReq.setServiceName(ServiceName.PWT_MGMT);
			sReq.setServiceMethod(ServiceMethodName.RAFFLE_PRZE_WINNING_TICKET);
			sReq.setServiceData(promoWinBean);
			sRes = delegate.getResponse(sReq);
			promoWinBean = (PWTDrawBean) new Gson().fromJson((JsonElement)sRes.getResponseData(), new TypeToken<PWTDrawBean>(){}.getType());
			List<RaffleDrawIdBean> raffleDrawIdBeanList = promoWinBean.getRaffleDrawIdBeanList();
			if (raffleDrawIdBeanList.size() < 1) {
				promoWinBean.setStatus("ERROR");
				return promoWinBean;
			}
			if (sRes.getIsSuccess()) {
				verifyRafflePwtBean(promoWinBean, pwtAmtForMasterApproval,
						connection);
				promoWinBean.setStatus("SUCCESS");
				return promoWinBean;
			} else {
				promoWinBean.setValid(false);
				promoWinBean.setStatus("ERROR");
				return promoWinBean;
			}
		}
		return null;
	}

	public PWTDrawBean verifyAndSaveTicketNoDirPlr(PWTDrawBean winningBean,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval,
			String highPrizeScheme, String highPrizeAmt) {

		double totPwtAmt = 0.0;
		boolean isMasAppReq = false;

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		ServiceResponse sRes = null;
		ServiceRequest sReq = null;
		IServiceDelegate delegate = null;
		try {
			connection = DBConnect.getConnection();
			sRes = new ServiceResponse();
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.PWT_MGMT);
			sReq
					.setServiceMethod(ServiceMethodName.DRAWGAME_PRZE_WINNING_TICKET);
			sReq.setServiceData(winningBean);
			delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);
			
			Type type = new TypeToken<PWTDrawBean>(){}.getType();
			
			winningBean = (PWTDrawBean) new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
			
//			winningBean = (PWTDrawBean) sRes.getResponseData();
			
			logger.debug("***Check If The Winning Bean Valid !!! ***"
					+ winningBean.isValid());
			if (sRes.getIsSuccess()) {
				if(Boolean.parseBoolean((String)com.skilrock.lms.common.Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_PWT_AMT")))
					CommonMethods.doRoundingForPwtAmt(winningBean);
				// if scheme is panel wise winning

				logger.debug("HIGH_PRIZE_AMT Is " + highPrizeAmt);
				for (DrawIdBean drawIdWinningBean : winningBean
						.getDrawWinList()) {
					int drawId = drawIdWinningBean.getDrawId();
					if (drawIdWinningBean.getPanelWinList() != null) {
						PanelIdBean panelIdBean = null;
						for (Object panelIdBeanObj : drawIdWinningBean
								.getPanelWinList()) {
							double panelPwtAmt = 0.0;
							logger.debug("============="
									+ (panelIdBean instanceof PanelIdBean));
							panelIdBean = (PanelIdBean) panelIdBeanObj;
							String prizeStatus = panelIdBean.getStatus();
							logger.debug(prizeStatus);
							if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
									|| prizeStatus
											.equalsIgnoreCase("UNCLM_CANCELLED")) {
								if (prizeStatus.equals("DRAW_EXPIRED"))
									panelIdBean.setStatus("DRAW_EXPIRED");
								else {
									logger.info("***Inside Unclamimed PWT***");
									panelPwtAmt = panelIdBean.getWinningAmt();
									totPwtAmt += panelPwtAmt;

									// for PND_MAS condition
									isMasAppReq = panelPwtAmt > Double
											.parseDouble(pwtAmtForMasterApproval); // PWT_APPROVAL_LIMIT
									if (isMasAppReq) {
										panelIdBean.setAppReq(isMasAppReq);
										winningBean.setPwtStatus("MAS_APP_REQ");
										logger.debug("MAS_APP_REQ***********");
									}
									panelIdBean.setStatus("NORMAL_PAY");
									if (panelPwtAmt > Double
											.parseDouble(highPrizeAmt)) {
										winningBean.setHighPrize(true);
										logger.debug("isHighPrize"
												+ winningBean.isHighPrize());
									} else {
										winningBean.setWinTkt(true);
									}
								}
							} else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {
							
								panelIdBean.setStatus("CLAIMED");
								pstmt = connection
										.prepareStatement("select status from st_dg_pwt_inv_? where ticket_nbr=? and draw_id=? and panel_id=?");
								pstmt.setInt(1, winningBean.getGameId());
								pstmt.setString(2, winningBean.getTicketNo());
								pstmt.setInt(3, drawId);
								pstmt.setInt(4, panelIdBean.getPanelId());
								logger.debug(pstmt);
								resultSet = pstmt.executeQuery();
								if (resultSet.next()) {
									prizeStatus = resultSet.getString("status");
									if (PwtStatus.contains(prizeStatus)) {
										switch (PwtStatus.valueOf(prizeStatus)
												.getStatus()) {
										case 1:
											panelIdBean.setStatus("MISSING");
											break;
										case 2:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 3:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 4:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 5:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 6:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 7:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 8:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 9:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 10:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 11:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 12:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 13:
											panelIdBean.setStatus("REQUESTED");
											break;
										case 14:
											panelIdBean.setStatus("PND_MAS");
											break;
										case 15:
											panelIdBean.setStatus("PND_PAY");
											break;
										case 16:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 17:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 18:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 19:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 20:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 21:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 22:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 23:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 24:
											panelIdBean
													.setStatus("CANCELLED_PERMANENT");
											break;
										case 25:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 26:
											panelIdBean.setStatus("CLAIMED");
											break;
										case 27:
											panelIdBean.setStatus("CLAIMED");
											break;
										default:
											panelIdBean.setStatus("UNDIFINED");
											break;
										}
									} else {
										panelIdBean.setStatus("UNDIFINED");
									}
								}
							} else if (prizeStatus.equalsIgnoreCase("NON WIN")) {
								panelIdBean.setStatus("NON WIN");
							} else if (prizeStatus.equals("CLAIM_PENDING")) {
								panelIdBean.setStatus("CLAIM_PENDING");
							}
						}
					} else if (drawIdWinningBean.getStatus().equals(
							"VERIFICATION_PENDING")) {
						drawIdWinningBean.setStatus("VERIFICATION_PENDING");
					} else if (drawIdWinningBean.getStatus().equals(
							"RES_AWAITED")) {
						drawIdWinningBean.setStatus("RES_AWAITED");
					}
				}
				
				//winningBean.setGameId(Util.getGameIdFromGameNumber(winningBean.getGameNo()));
				winningBean.setGameDispName(Util.getGameDisplayName(winningBean.getGameId()));
				winningBean.setTotalAmount(totPwtAmt);
				winningBean.setStatus("SUCCESS");
			} else {
				logger.debug("inside invalid ticket ");
				winningBean.setStatus("error");
				/*
				 * ResponsibleGaming resGaming = new ResponsibleGaming();
				 * boolean isFraud = resGaming
				 * .checkFraudDrawPwtTicket(userInfoBean.getUserOrgId());
				 * logger.debug("*****is fraud***** " + isFraud); if (!isFraud)
				 * winningBean.setStatus("FRAUD");
				 */
			}
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			winningBean.setStatus("ERROR");
		} finally {
			DBConnect.closeCon(connection);
		}
		return winningBean;
	}

	private String verifyPwtTicketDirPlr(int gameId, Connection connection,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval,
			PWTDrawBean pwtDraeBean, String highPrizeScheme)
			throws LMSException {

		// String encodedVirnCode = "";
		String returnType = "input";
		try {
			// Statement statement4 = connection.createStatement();
			// ResultSet resultSet4;

			PreparedStatement pstmt = null;
			logger.debug("highPrizeScheme " + highPrizeScheme);
			if ("DRAW_WISE".equalsIgnoreCase(highPrizeScheme)) {
				for (DrawIdBean drawIdWinningBean : pwtDraeBean
						.getDrawWinList()) {

					// String pwtAmount = drawIdWinningBean.getWinningAmt();
					String prizeStatus = drawIdWinningBean.getStatus();
					logger.debug("prizeStatus " + prizeStatus);
					if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
							|| prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
						logger.debug("insideeeee status  " + prizeStatus);
						double pwtAmt = Double.parseDouble(drawIdWinningBean
								.getWinningAmt());
						boolean isPwtAmtForMasterApproval = pwtAmt >= Double
								.parseDouble(pwtAmtForMasterApproval);

						if (isPwtAmtForMasterApproval) {
							
									logger.debug("pwt amount is greater then master approbal limit so go gor masyter approval");
							drawIdWinningBean.setValid(true);
							drawIdWinningBean
									.setVerificationStatus("Valid Virn");
							drawIdWinningBean
									.setMessage("Send for Master Approval");
							drawIdWinningBean.setMessageCode("Undefined");
							drawIdWinningBean
									.setAppReq(isPwtAmtForMasterApproval);
							returnType = "registration";
						} else {// if PWT amount is in bo payment limit without
							// master approval
							
									logger.debug("should be send to payment process and it is auto approved");
							drawIdWinningBean.setValid(true);
							drawIdWinningBean
									.setVerificationStatus("Valid Virn");
							drawIdWinningBean
									.setMessage("Should pay to player after registration by BO");
							returnType = "registration";

						}
					} else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {

						String getPwtDetails = "select status from st_dg_pwt_inv_? where ticket_nbr=?";
						pstmt = connection.prepareStatement(getPwtDetails);
						pstmt.setInt(1, pwtDraeBean.getGameNo());
						pstmt.setString(2, pwtDraeBean.getTicketNo());

						ResultSet rs = pstmt.executeQuery();
						if (rs.next()) {
							drawIdWinningBean.setValid(false);
							drawIdWinningBean.setVerificationStatus("CLAIMED");
							drawIdWinningBean.setMessage("CLAIMED");
							drawIdWinningBean.setMessageCode("112009");

						} else {
							throw new LMSException();
						}
					} else if (prizeStatus.equalsIgnoreCase("NON WIN")) {
						logger.debug("insideeeee status  " + prizeStatus);
						drawIdWinningBean.setValid(false);
						drawIdWinningBean.setVerificationStatus("Not Winning");
						drawIdWinningBean.setMessage("No winning");
						drawIdWinningBean.setMessageCode("112009");

					} else {
						
								logger.debug("insideeeee status  elseeeeeeeeeeeee"
										+ prizeStatus);
						drawIdWinningBean.setValid(false);
						drawIdWinningBean.setVerificationStatus("InValid Virn");
						drawIdWinningBean
								.setMessage("UNDEFINED STATUS OF PWT:: "
										+ prizeStatus);
						drawIdWinningBean.setMessageCode("TBD");

					}
				}

			} else if ("PANEL_WISE".equalsIgnoreCase(highPrizeScheme)) {
				double totalWinningAmt = 0.0;
				logger.debug("inside panel wise");
				for (DrawIdBean drawIdWinningBean : pwtDraeBean
						.getDrawWinList()) {
					if (drawIdWinningBean.getPanelWinList() != null) {
						for (PanelIdBean panelIdBean : drawIdWinningBean
								.getPanelWinList()) {

							// String pwtAmount = panelIdBean.getWinningAmt();
							String prizeStatus = panelIdBean.getStatus();

							if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
									|| prizeStatus
											.equalsIgnoreCase("UNCLM_CANCELLED")) {
								logger.debug("insideeeee status  "
										+ prizeStatus);
								// pwtBean.setPwtAmount(pwtAmount) ;
								double pwtAmt = panelIdBean.getWinningAmt();
								boolean isPwtAmtForMasterApproval = pwtAmt >= Double
										.parseDouble(pwtAmtForMasterApproval);

								if (isPwtAmtForMasterApproval) {
									
											logger.debug("pwt amount is greater then master approbal limit so go gor masyter approval: "
													+ pwtAmt);
									totalWinningAmt = totalWinningAmt + pwtAmt;
									logger.debug(totalWinningAmt);
									panelIdBean.setValid(true);
									panelIdBean.setStatus("HIGH_PRIZE");
									panelIdBean
											.setVerificationStatus("Valid Virn");
									panelIdBean
											.setMessage("Send for Master Approval");
									panelIdBean.setMessageCode("Undefined");
									panelIdBean
											.setAppReq(isPwtAmtForMasterApproval);
									returnType = "registration";

								} else {// if PWT amount is in bo payment limit
									// without master approval
									
											logger.debug("should be send to payment process and it is auto approved:: "
													+ pwtAmt);
									totalWinningAmt = totalWinningAmt + pwtAmt;
									logger.debug(totalWinningAmt);
									panelIdBean.setValid(true);
									panelIdBean.setStatus("HIGH_PRIZE");
									panelIdBean
											.setVerificationStatus("Valid Virn");
									panelIdBean
											.setMessage("Should pay to player after registration by BO");
									returnType = "registration";

								}
							} else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {
								logger.debug("insideeeee status  "
										+ prizeStatus);
								String getPwtDetails = "select status from st_dg_pwt_inv_? where ticket_nbr=?";
								pstmt = connection
										.prepareStatement(getPwtDetails);
								pstmt.setInt(1, pwtDraeBean.getGameNo());
								pstmt.setString(2, pwtDraeBean.getTicketNo());

								ResultSet rs = pstmt.executeQuery();
								if (rs.next()) {
									panelIdBean.setValid(false);
									panelIdBean
											.setVerificationStatus("Claimed");
									panelIdBean.setMessage("Claimed");
									panelIdBean.setMessageCode("112009");

								} else {
									throw new LMSException();
								}
							} else if (prizeStatus.equalsIgnoreCase("NON WIN")) {
								logger.debug("insideeeee status  "
										+ prizeStatus);
								panelIdBean.setValid(false);
								panelIdBean
										.setVerificationStatus("Not Winning");
								panelIdBean.setMessage("No winning");
								panelIdBean.setMessageCode("112009");
							} else {
								
										logger.debug("insideeeee status  elseeeeeeeeeeeee"
												+ prizeStatus);
								panelIdBean.setValid(false);
								panelIdBean
										.setVerificationStatus("InValid Virn");
								panelIdBean
										.setMessage("UNDEFINED STATUS OF PWT:: "
												+ prizeStatus);
								panelIdBean.setMessageCode("TBD");

							}

						}
					}
				}
				pwtDraeBean.setTotalAmount(totalWinningAmt);
				logger.debug("******* " + returnType + totalWinningAmt);
			}
			logger.debug("******* " + returnType);
			return returnType;

		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException();
		}

	}

	public String getRaffleTktTypeFromgameNbr(int gameNbr, Connection con) {
		Statement stm;
		try {
			stm = con.createStatement();
			ResultSet rs = stm
					.executeQuery("select raffle_ticket_type from st_dg_game_master where game_nbr="
							+ gameNbr);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*public int getGamenoFromTktnumber(String tktNum) {

		int retIdLen = 0;
		int gameNo = 0;
		int tktLen = 0;
		String tktBuf = null;

		if (tktNum != null
				&& (tktNum.length() == ConfigurationVariables.tktLenA || tktNum
						.length() == ConfigurationVariables.tktLenB)) {
			if (tktNum.length() == ConfigurationVariables.tktLenA) {
				tktLen = ConfigurationVariables.tktLenA;
				retIdLen = ConfigurationVariables.retIdLenA;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenA);
			} else if (tktNum.length() == ConfigurationVariables.tktLenB) {
				tktLen = ConfigurationVariables.tktLenB;
				retIdLen = ConfigurationVariables.retIdLenB;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenB);
			}
			gameNo = Integer.parseInt(tktBuf);
		}
		return gameNo;

	}*/

	public MainPWTDrawBean getTotalPWTTkeAmt(MainPWTDrawBean mainPwtBean,
			Connection connection, String pwtAmtForMasterApproval,
			String highPrizeAmt) {
		// here see the limits after adding both for draw and promo
		double totalticketAmt = 0.0;
		List<PWTDrawBean> winningBeanList = mainPwtBean.getWinningBeanList();

		/*for (int i = 0; i < winningBeanList.size(); i++) {
			PWTDrawBean pwtBean = winningBeanList.get(i);
			if(pwtBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
				if (pwtBean.getDrawWinList() != null)
					for (int j = 0; j < pwtBean.getDrawWinList().size(); j++) {
						DrawIdBean drawBean = pwtBean.getDrawWinList().get(j);
						List<PanelIdBean> panelWinList = drawBean.getPanelWinList();
						if (panelWinList != null
								&& drawBean.getStatus().equals("UNCLM_PWT"))
							totalticketAmt = totalticketAmt
									+ Double.parseDouble(drawBean.getWinningAmt());
					}
			} else if("RAFFLE".equalsIgnoreCase(pwtBean.getPwtTicketType())) {
				totalticketAmt = totalticketAmt + pwtBean.getTotalAmount();
			}
		}*/
		for (int i = 0; i < winningBeanList.size(); i++) {
			PWTDrawBean pwtBean = winningBeanList.get(i);
			totalticketAmt = totalticketAmt + pwtBean.getTotalAmount();
		}
		if (totalticketAmt > 0) {
			boolean isMasAppReq = false;
			isMasAppReq = totalticketAmt > Double
					.parseDouble(pwtAmtForMasterApproval);
			if (isMasAppReq) {
				mainPwtBean.setPwtStatus("MAS_APP_REQ");
				logger.debug("MAS_APP_REQ***********");
			}
			if (totalticketAmt > Double.parseDouble(highPrizeAmt)) {
				mainPwtBean.setHighPrize(true);
				logger.debug("isHighPrize" + mainPwtBean.isHighPrize());
			} else {
				mainPwtBean.setWinTkt(true);
			}
		} else {
			mainPwtBean.setWinTkt(false);
		}
		mainPwtBean.setTotlticketAmount(CommonMethods
				.fmtToTwoDecimal(totalticketAmt));
		return mainPwtBean;
	}
}
		