package com.skilrock.lms.coreEngine.drawGames.pwtMgmt;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DrawPwtApproveRequestNPlrBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DenyPWTBean;

public class RetPwtPayProcessHelper {
	static Log logger = LogFactory.getLog(RetPwtPayProcessHelper.class);
	private Connection connection;

	public boolean denyPWTProcess(int taskId, int drawId, int gameId,
			String ticketNbr, String denyPwtStatus, int gameNbr,
			UserInfoBean userBean, String panelId) throws LMSException {

		logger.debug("ticketNbr to deny " + ticketNbr + " for draw " + drawId);
		boolean statusChange = false;
		// Connection connection=null;
		 
		connection = DBConnect.getConnection();

		try {
			connection.setAutoCommit(false);
			String tempPwtStatus = "CANCEL";
			String pwtStatus = "UNCLM_CANCELLED";

			PreparedStatement pstmt = connection
					.prepareStatement("update st_dg_approval_req_master set req_status=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,approval_date=?,remarks=? where task_id=?");
			pstmt.setString(1, tempPwtStatus);
			pstmt.setString(2, "RETAILER");
			pstmt.setInt(3, userBean.getUserId());
			pstmt.setInt(4, userBean.getUserOrgId());
			pstmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(6, "Request Denied By Retailer As "
					+ denyPwtStatus.trim());
			pstmt.setInt(7, taskId);
			int isUpdate = pstmt.executeUpdate();
			logger.debug("update request temporary table ==" + pstmt);

			/*
			 * //update pwt inv table status PreparedStatement
			 * pstmtPwtInvUpdate=connection.prepareStatement("update
			 * st_dg_pwt_inv_? set status=? where ticket_nbr=? and draw_id=? and
			 * panel_id=?"); pstmtPwtInvUpdate.setInt(1,gameNbr);
			 * pstmtPwtInvUpdate.setString(2, pwtStatus);
			 * pstmtPwtInvUpdate.setString(3,ticketNbr);
			 * pstmtPwtInvUpdate.setInt(4,drawId);
			 * if("NA".equalsIgnoreCase(panelId))
			 * pstmtPwtInvUpdate.setObject(5,null); else
			 * pstmtPwtInvUpdate.setInt(5,Integer.parseInt(panelId));
			 */

			// update pwt inv table status
			PreparedStatement pstmtPwtInvUpdate = null;
			if ("NA".equalsIgnoreCase(panelId)) {
				pstmtPwtInvUpdate = connection
						.prepareStatement("delete from  st_dg_pwt_inv_?  where ticket_nbr=? and draw_id=? and panel_id is null");
			} else {
				pstmtPwtInvUpdate = connection
						.prepareStatement("delete from  st_dg_pwt_inv_?  where ticket_nbr=? and draw_id=? and panel_id="
								+ Integer.parseInt(panelId));
			}

			pstmtPwtInvUpdate.setInt(1, gameId);
			pstmtPwtInvUpdate.setString(2, ticketNbr);
			pstmtPwtInvUpdate.setInt(3, drawId);

			int isPwtupdate = pstmtPwtInvUpdate.executeUpdate();

			logger.debug("update st_pwt_inv ==" + pstmtPwtInvUpdate);
			/*
			 * if ("Temporary
			 * Cancellation".equalsIgnoreCase(denyPwtStatus.trim()) &&
			 * isUpdate==1 && isPwtupdate==1) { statusChange=true;
			 * connection.commit(); }
			 */
			DenyPWTBean denyBean = new DenyPWTBean();

			denyBean.setDrawId(drawId);
			denyBean.setGameNo(gameNbr);
			denyBean.setPanelId(panelId.equals("NA") ? null : panelId); // we
			// have
			denyBean.setTicketNo(ticketNbr);
			denyBean.setStatus(pwtStatus);
			denyBean.setGameId(gameId);
			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.DRAWGAME);
			sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CHANGE_PWT_STATUS);
			sReq.setServiceData(denyBean);

			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);

			if (sRes.getIsSuccess()) {
				if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
						.trim())
						&& isUpdate == 1 && isPwtupdate == 1) {
					statusChange = true;
					connection.commit();
				}
			}

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

	public synchronized String generateReciptForPwt(List<Long> transIdList,
			Connection connection, int userOrgID, int partyId, String partyType) {
		int retReceiptId = -1;
		String receipts = null;
		// int boReceiptId=-1;
		PreparedStatement retReceiptPstmt = null;
		PreparedStatement retReceiptMappingPstmt = null;

		try {
			// for generating receipt********************
			// insert in receipt master
			retReceiptPstmt = connection.prepareStatement(QueryManager
					.insertInReceiptMaster());
			retReceiptPstmt.setString(1, "RETAILER");
			retReceiptPstmt.executeUpdate();

			ResultSet retRSet = retReceiptPstmt.getGeneratedKeys();
			while (retRSet.next()) {
				retReceiptId = retRSet.getInt(1);
				logger.debug("Receipt Id:" + retReceiptId);
			}

			PreparedStatement autoGenRecptPstmt = null;
			autoGenRecptPstmt = connection.prepareStatement(QueryManager
					.getRETLatestReceiptNb());
			autoGenRecptPstmt.setString(1, "RECEIPT");
			autoGenRecptPstmt.setInt(2, userOrgID);
			ResultSet recieptRs = autoGenRecptPstmt.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNoAgt = GenerateRecieptNo.getRecieptNoAgt(
					"RECEIPT", lastRecieptNoGenerated, "RETAILER", userOrgID);

			// insert in agent receipts
			// agtReceiptQuery = QueryManager.getST1AgtReceiptsQuery();
			retReceiptPstmt = connection.prepareStatement(QueryManager
					.insertInRETReceipts());
			retReceiptPstmt.setInt(1, retReceiptId);
			retReceiptPstmt.setString(2, "RECEIPT");

			retReceiptPstmt.setInt(3, userOrgID);
			retReceiptPstmt.setInt(4, partyId);
			retReceiptPstmt.setString(5, partyType);
			retReceiptPstmt.setString(6, autoGeneRecieptNoAgt);
			retReceiptPstmt.execute();

			// insert agetn receipt trn mapping

			// agtReceiptMappingQuery =
			// QueryManager.getST1AgtReceiptsMappingQuery();
			retReceiptMappingPstmt = connection.prepareStatement(QueryManager
					.insertRETReceiptTrnMapping());

			for (int i = 0; i < transIdList.size(); i++) {
				retReceiptMappingPstmt.setInt(1, retReceiptId);
				retReceiptMappingPstmt.setLong(2, transIdList.get(i));
				retReceiptMappingPstmt.execute();
			}
			receipts = retReceiptId + "-" + autoGeneRecieptNoAgt;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}

		return receipts;
	}

	public DrawPwtApproveRequestNPlrBean getPendingPwtDetails(int taskId,
			String partyType) throws LMSException {
		 
		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet resultFromDb;
		connection = DBConnect.getConnection();
		try {

			String getPwtDetailsQuery = null;

			if ("PLAYER".equals(partyType)) {
				getPwtDetailsQuery = "select a.task_id,a.party_type,a.request_id,a.party_id,a.pwt_amt,a.tax_amt,a.net_amt,a.req_status,a.ticket_nbr,ifnull(a.draw_id,'NA') draw_id,a.panel_id ,a.remarks,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,b.player_id,b.bank_acc_nbr,b.bank_name,b.bank_branch,b.location_city,d.game_name,d.game_nbr from st_dg_approval_req_master a , st_lms_player_master b ,st_dg_game_master d where 1=1 and d.game_id=a.game_id   and a.task_id=? and party_type='PLAYER' and a.party_id=b.player_id";
			} else {
				throw new LMSException("Error because party type is "
						+ partyType);
			}

			logger.debug("query to get pwt details :" + getPwtDetailsQuery);
			pstmt = connection.prepareStatement(getPwtDetailsQuery);
			pstmt.setInt(1, taskId);
			resultFromDb = pstmt.executeQuery();
			// PlayerBean plrBean=new PlayerBean();
			DrawPwtApproveRequestNPlrBean plePwtBean = new DrawPwtApproveRequestNPlrBean();
			while (resultFromDb.next()) {
				// collect player info

				// plrBean.setFirstName(resultFromDb.getString("first_name"));
				// plrBean.setLastName(resultFromDb.getString("last_name"));
				plePwtBean.setPartyName(resultFromDb.getString("first_name")
						+ " " + resultFromDb.getString("last_name"));
				plePwtBean.setPhone_nbr(resultFromDb.getString("phone_nbr"));
				plePwtBean.setCity(resultFromDb.getString("city"));
				plePwtBean
						.setBankActNbr(resultFromDb.getString("bank_acc_nbr"));
				plePwtBean.setBankBranch(resultFromDb.getString("bank_branch"));
				plePwtBean.setBankCity(resultFromDb.getString("location_city"));
				plePwtBean.setBankName(resultFromDb.getString("bank_name"));
				// plrBean.setPlrCity(resultFromDb.getString("city"));
				// plrBean.setPlrId(resultFromDb.getInt("player_id"));

				// collect pwt info
				plePwtBean.setPartyType(resultFromDb.getString("party_type"));
				plePwtBean.setPartyId(resultFromDb.getInt("party_id"));
				plePwtBean.setPwt_amt(resultFromDb.getDouble("pwt_amt"));
				plePwtBean.setComm_amt(resultFromDb.getDouble("tax_amt"));
				plePwtBean.setNet_amt(resultFromDb.getDouble("net_amt"));
				plePwtBean.setTicket_nbr(resultFromDb.getString("ticket_nbr"));
				plePwtBean.setDrawId(resultFromDb.getInt("draw_id"));
				plePwtBean.setPanelId(resultFromDb.getString("panel_id"));
				plePwtBean.setRemarks(resultFromDb.getString("remarks"));
				plePwtBean.setTask_id(resultFromDb.getInt("task_id"));
				plePwtBean.setRequest_id(resultFromDb.getString("request_id"));
				plePwtBean.setGame_id(resultFromDb.getInt("game_id"));
				plePwtBean.setGame_nbr(resultFromDb.getInt("game_nbr"));

			}
			// List plrPwtDetails=new ArrayList();
			// plrPwtDetails.add(plrBean);
			// plrPwtDetails.add(plePwtBean);

			return plePwtBean;

		} catch (SQLException se) {
			logger.error("Exception: " + se);
			se.printStackTrace();
			throw new LMSException();
		}
		// return null;

	}

	/**
	 * This method is used to get unapproved pwts from request table
	 */
	public List<DrawPwtApproveRequestNPlrBean> getRequestsPwtsToPay(
			String requestId, String firstName, String lastName, String status,
			int payByOrgId) throws LMSException {

		 
		// Connection con=null;
		Statement stmtGetReqDetails = null;
		ResultSet reqDetails;
		connection = DBConnect.getConnection();
		StringBuilder getRequestDetailsQuery = null;

		getRequestDetailsQuery = new StringBuilder(
				"select a.task_id,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.draw_id,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_dg_approval_req_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_dg_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id and a.party_type='PLAYER' ");
		if (firstName != null && !"".equals(firstName)) {
			getRequestDetailsQuery.append(" and b.first_name='" + firstName
					+ "'");
		}
		if (lastName != null && !"".equals(lastName)) {
			getRequestDetailsQuery
					.append(" and b.last_name='" + lastName + "'");
		}
		if (requestId != null && !"".equals(requestId)) {
			getRequestDetailsQuery.append(" and a.request_id='" + requestId
					+ "'");
		}
		if (status != null && !"".equals(status)) {
			getRequestDetailsQuery.append(" and a.req_status='" + status + "'");
		}

		getRequestDetailsQuery.append(" and a.pay_request_for_org_id="
				+ payByOrgId + "");

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
				pwtAppReqPlrBean.setPartyName(reqDetails
						.getString("first_name")
						+ " " + reqDetails.getString("last_name"));
				pwtAppReqPlrBean.setCity(reqDetails.getString("city"));
				pwtAppReqPlrBean
						.setPhone_nbr(reqDetails.getString("phone_nbr"));
				pwtAppReqPlrBean.setPartyType("PLAYER");
				pwtAppReqPlrBean.setPwt_amt(reqDetails.getDouble("pwt_amt"));

				pwtReqDetailsList.add(pwtAppReqPlrBean);
			}
			return pwtReqDetailsList;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql Exception", e);
		}

	}

	public String payPendingPwt(int taskId, double pwtAmount, double taxAmt,
			double netAmt, int partyId, String partyType, String ticketNbr,
			int drawId, String panelId, int gameId, UserInfoBean userBean,
			String paymentType, String chqNbr, String draweeBank,
			String chequeDate, String issuiningParty, int gameNbr,
			String rootPath) throws LMSException {

		 
		connection = DBConnect.getConnection();
		String retReceipt = null;
		// int agtTransId=0;
		double partyPwtCommRate = 0.0;
		double agtPwtCommRate = 0.0;
		try {
			connection.setAutoCommit(false);
			// this field will be removed from st agent pwt table so no need to
			// pass this variable for time being put it as zero instead of
			// fetching userid from database
			/*
			 * int partyUserId=0; List<PwtBean> pwtList=new ArrayList<PwtBean>();
			 * PwtBean pwtBean=new PwtBean();
			 * pwtBean.setPwtAmount(String.valueOf(pwtAmount));
			 * pwtBean.setEncVirnCode(virnNbr); pwtBean.setValid(true);
			 * pwtList.add(pwtBean);
			 * 
			 * PlayerPWTBean requestDetailsBean=new PlayerPWTBean();
			 * requestDetailsBean.setTax(taxAmt);
			 * requestDetailsBean.setNetAmt(netAmt);
			 * requestDetailsBean.setVirnCode(virnNbr);
			 * requestDetailsBean.setTicketNbr(ticketNbr);
			 * requestDetailsBean.setTaskId(taskId);
			 * requestDetailsBean.setPaymentType(paymentType);
			 * requestDetailsBean.setChequeNbr(chqNbr);
			 * requestDetailsBean.setChequeDate(chequeDate);
			 * requestDetailsBean.setIssuingPartyName(issuiningParty);
			 * requestDetailsBean.setDraweeBank(draweeBank);
			 */

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			java.sql.Date chqDate = null;
			try {
				if (!"".equalsIgnoreCase(chequeDate) && chequeDate != null) {
					chqDate = new java.sql.Date(dateFormat.parse(chequeDate)
							.getTime());
				}
			} catch (ParseException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(
						"Exception date parsing  while pwt payments at Agent end ",
						e);
			}

			/*
			 * DGDirectPlrPwtBean bean = new DGDirectPlrPwtBean();
			 * bean.setDrawId(drawId); bean.setChqDate(chqDate);
			 * bean.setChqNbr(chqNbr); bean.setDraweeBank(draweeBank);
			 * bean.setGameId(gameId); bean.setGameNbr(gameNbr);
			 * bean.setIssuingPartyName(issuiningParty); bean.setNetAmt(netAmt);
			 * bean.setPaymentType(paymentType); bean.setPlayerId(partyId);
			 * bean.setPwtAmount(String.valueOf(pwtAmount));
			 * bean.setTaxAmt(taxAmt); bean.setTicketNbr(ticketNbr);
			 */

			RetPWTProcessHelper retHelper = new RetPWTProcessHelper();
			// get the retailer PWT Limits
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(userBean.getUserOrgId(),
							connection);
			if (orgPwtLimit == null) { // send mail to backoffice
				throw new LMSException("PWT Limits Are Not defined Properly!!");
			}
			long transactionId = 0;
			boolean isAutoScrap = "YES".equalsIgnoreCase(orgPwtLimit
					.getIsPwtAutoScrap())
					&& netAmt <= orgPwtLimit.getScrapLimit() ? true : false;
			if ("NA".equalsIgnoreCase(panelId)) {// panel id to sent is null
				transactionId = retHelper.retDirPlrPwtPayment(userBean
						.getUserId(), userBean.getUserOrgId(), userBean
						.getParentOrgId(), gameNbr, gameId, isAutoScrap,
						pwtAmount, drawId, partyId, taxAmt, netAmt,
						paymentType, chqNbr, chqDate, draweeBank,
						issuiningParty, ticketNbr, connection, null,
						"DRAW_WISE", taskId);
				// transactionId = retHelper.retPwtPayment(userBean, bean,
				// gameNbr, gameId,true, connection);
			} else if (Integer.parseInt(panelId) > 0) { // panel id will be the
				// actual panel id
				transactionId = retHelper.retDirPlrPwtPayment(userBean
						.getUserId(), userBean.getUserOrgId(), userBean
						.getParentOrgId(), gameNbr, gameId, isAutoScrap,
						pwtAmount, drawId, partyId, taxAmt, netAmt,
						paymentType, chqNbr, chqDate, draweeBank,
						issuiningParty, ticketNbr, connection, panelId,
						"PANEL_WISE", taskId);
				// transactionId = retHelper.retPwtPayment(userBean, bean,
				// gameNbr, gameId,true, connection);
			}
			logger.debug("  retailer transaction id for player   "
					+ transactionId);
			List<Long> transIdList = new ArrayList<Long>();
			transIdList.add(transactionId);

			// update status of of requested id entries into
			// st_pwt_approval_request_master
			String updateAppTable = "update  st_dg_approval_req_master  set req_status ='PAID', "
					+ "remarks ='Payment Done', payment_done_by_type =?, payment_done_by =?, transaction_id=? where  task_id = ?";
			PreparedStatement pstmt = connection
					.prepareStatement(updateAppTable);
			pstmt.setString(1, "RETAILER");
			pstmt.setInt(2, userBean.getUserOrgId());
			pstmt.setLong(3, transactionId);
			pstmt.setInt(4, taskId);
			pstmt.executeUpdate();

			// generate receipt here for player

			retReceipt = generateReciptForPwt(transIdList, connection, userBean
					.getUserOrgId(), partyId, partyType);
			if (retReceipt != null) {
				connection.commit();
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportPlayer(taskId, rootPath,
						"DRAW_GAME");
			} else {
				throw new LMSException(
						"Error during generating receipts for Player  by Retailer:: ");
			}

			/*
			 * CommonFunctionsHelper common=new CommonFunctionsHelper();
			 * 
			 * if("RETAILER".equals(partyType))
			 * partyPwtCommRate=common.fetchCommOfOrganization(gameId, partyId,
			 * "PWT",partyType, connection);
			 * 
			 * agtPwtCommRate=common.fetchCommOfOrganization(gameId, payByOrgId,
			 * "PWT","AGENT", connection);
			 * 
			 * OrgPwtLimitBean
			 * orgPwtLimit=common.fetchPwtLimitsOfOrgnization(payByOrgId,
			 * connection);
			 * 
			 * int transactionId=common.agtEndPWTPaymentProcess(pwtList, gameId,
			 * payByUserId, payByOrgId, partyId, partyUserId,
			 * partyPwtCommRate,agtPwtCommRate, gameNbr, orgPwtLimit,
			 * connection, partyType, requestDetailsBean); List<Integer>
			 * transIdList=new ArrayList<Integer>(); if(transactionId > 0)
			 * transIdList.add(transactionId);
			 * 
			 * if(transIdList.size() > 0) { PwtAgentHelper agtHepper=new
			 * PwtAgentHelper();
			 * agtReceipt=agtHepper.generateReciptForPwt(transIdList,connection,payByOrgId,partyId,partyType);
			 * if(agtReceipt!=null) connection.commit(); else throw new
			 * LMSException("Error during generating receipts for PWT :: ");
			 * //logger.debug("transIdList for agent size is " +
			 * transIdList.size() + ":: receipt number " + agtReceiptId); if
			 * (agtReceipt != null) { GraphReportHelper graphReportHelper = new
			 * GraphReportHelper(); if("PLAYER".equalsIgnoreCase(partyType))
			 * graphReportHelper.createTextReportPlayer(taskId, rootPath);
			 * 
			 * else if("RETAILER".equals(partyType))
			 * graphReportHelper.createTextReportAgent(Integer.parseInt(agtReceipt.split("-")[0]),
			 * rootPath, payByOrgId,payByOrgName); } }
			 */
			return retReceipt;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

}