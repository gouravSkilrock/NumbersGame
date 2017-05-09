package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.beans.GamePlayerPWTBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.PwtApproveRequestNPlrBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

public class BOPwtProcessHelperTNV {

	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet result;

	public String approvePendingPwts(int taskId, double pwtAmount,
			int requestedById, String requesterType, int approvedByOrgId,
			int approvedByUserId, int gameId, int gameNbr, String virnNbr,
			String ticketNbr, String pwtAmtForMasterApproval, String channel,
			String interfaceType) throws LMSException {
		DBConnect dbconnect = new DBConnect();
		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet payLimitDetails = null;
		connection = dbconnect.getConnection();
		double agtPayLimit = 0.0;
		double retPayLimit = 0.0;
		String status;
		String payReqForOrgType;
		int payReqForOrgId = 0;
		String remarks;

		try {
			connection.setAutoCommit(false);
			String getPayLimits = null;

			// check if bomaster Approval required
			if (pwtAmount >= Integer.parseInt(pwtAmtForMasterApproval)) {
				// go for master's approval
				remarks = "Pending For BO Master Approval";
				status = "PND_MAS";
				String updateRequestStatus = "update st_se_pwt_approval_request_master set req_status=?,requested_to_type=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,approval_date=?,remarks=? where task_id=?";
				pstmt = connection.prepareStatement(updateRequestStatus);
				pstmt.setString(1, status);
				pstmt.setString(2, "BO");
				pstmt.setString(3, "BO");
				pstmt.setInt(4, approvedByUserId);
				pstmt.setInt(5, approvedByOrgId);
				pstmt.setDate(6, new java.sql.Date(new java.util.Date()
						.getTime()));
				pstmt.setString(7, remarks);
				pstmt.setInt(8, taskId);
				int updatedRow = pstmt.executeUpdate();
				if (updatedRow == 1) {
					System.out
							.println("Request status updated for BO Master Approval");
				} else {
					throw new LMSException(
							"Exception while sending request for master approval");
				}
			} else {
				if (requesterType.equals("RETAILER")) {
					getPayLimits = "select organization_id,pay_limit from st_lms_oranization_limits where organization_id in(?,(select parent_id from st_lms_organization_master where organization_id=?))";
					pstmt = connection.prepareStatement(getPayLimits);
					pstmt.setInt(1, requestedById);
					pstmt.setInt(2, requestedById);
					payLimitDetails = pstmt.executeQuery();
					int payByOrgRet = 0;
					int payByOrgAgt = 0;
					while (payLimitDetails.next()) {
						if (payLimitDetails.getInt("organization_id") == requestedById) {
							retPayLimit = payLimitDetails
									.getDouble("pay_limit");
							// payReqForOrgId=payLimitDetails.getInt("organization_id");
							payByOrgRet = payLimitDetails
									.getInt("organization_id");
						} else {
							agtPayLimit = payLimitDetails
									.getDouble("pay_limit");
							// payReqForOrgId=payLimitDetails.getInt("organization_id");
							payByOrgAgt = payLimitDetails
									.getInt("organization_id");
						}
					}
					if (pwtAmount <= retPayLimit) { // go for retailer's payment
						status = "PND_PAY";
						payReqForOrgType = "RETAILER";
						payReqForOrgId = payByOrgRet;
					} else if (pwtAmount <= agtPayLimit) { // go for agent's
						// payment
						status = "PND_PAY";
						payReqForOrgType = "AGENT";
						payReqForOrgId = payByOrgAgt;
					} else { // go for BO's pending pwt
						status = "PND_PAY";
						payReqForOrgType = "BO";
						payReqForOrgId = approvedByOrgId; // Bo's organization
						// id
					}

				} else if (requesterType.equals("AGENT")) {
					getPayLimits = "select organization_id,pay_limit from st_lms_oranization_limits where organization_id=?";
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
						// payReqForOrgId=approvedByOrgId;
						payReqForOrgId = payByOrgAgt;
					} else { // go for BO's pending pwt
						status = "PND_PAY";
						payReqForOrgType = "BO";
						payReqForOrgId = approvedByOrgId; // Bo's organization
						// id
					}

				} else { // go for BO's pending pwt
					status = "PND_PAY";
					payReqForOrgType = "BO";
					payReqForOrgId = approvedByOrgId; // Bo's organization id
				}

				// update the request table for pwt payments

				remarks = "Payment should be done at " + payReqForOrgType;
				String updateRequestStatus = "update st_se_pwt_approval_request_master set req_status=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,pay_req_for_org_type=?,pay_request_for_org_id=?,approval_date=?,remarks=? where task_id=?";
				pstmt = connection.prepareStatement(updateRequestStatus);
				pstmt.setString(1, status);
				pstmt.setString(2, "BO");
				pstmt.setInt(3, approvedByUserId);
				pstmt.setInt(4, approvedByOrgId);
				pstmt.setString(5, payReqForOrgType);
				pstmt.setInt(6, payReqForOrgId);
				pstmt.setDate(7, new java.sql.Date(new java.util.Date()
						.getTime()));
				pstmt.setString(8, remarks);
				pstmt.setInt(9, taskId);
				int updatedRow = pstmt.executeUpdate();
				if (updatedRow == 1) {
					System.out
							.println("Request status updated by BO for Approval");
				} else {
					throw new LMSException(
							"Exception while approving request By BO");
				}
			}
			// update the ticket_inv_detail table
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateTicketInvTable(ticketNbr, ticketNbr.split("-")[0]
					+ "-" + ticketNbr.split("-")[1], gameNbr, gameId, status,
					approvedByUserId, approvedByOrgId, "UPDATE", requestedById,
					channel, interfaceType, connection);

			// update pwt inventory table
			boolean isPwtUpdate = commHelper.updateVirnStatus(gameNbr, status,
					gameId, virnNbr, connection,MD5Encoder.encode(ticketNbr));
			if (!isPwtUpdate) {
				throw new LMSException("Error ::: pwt table not updated ");
			}

			connection.commit();
			return remarks;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}

	}

	public String approvePendingPwtsByMas(int taskId, double pwtAmount,
			int requestedById, String requesterType, int approvedByOrgId,
			int approvedByUserId, int gameId, int gameNbr, String virnNbr,
			String ticketNbr, String pwtAmtForMasterApproval, String channel,
			String interfaceType) throws LMSException {
		DBConnect dbconnect = new DBConnect();
		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet payLimitDetails = null;
		connection = dbconnect.getConnection();
		double agtPayLimit = 0.0;
		double retPayLimit = 0.0;
		String status;
		String payReqForOrgType;
		int payReqForOrgId = 0;
		String remarks;

		try {
			connection.setAutoCommit(false);
			String getPayLimits = null;
			System.out.println("requestedById--" + requestedById + "--");
			if (requesterType.equals("RETAILER")) {
				getPayLimits = "select organization_id,pay_limit from st_lms_oranization_limits where organization_id in(?,(select parent_id from st_lms_organization_master where organization_id=?))";
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
				getPayLimits = "select organization_id,pay_limit from st_lms_oranization_limits where organization_id=?";
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
				System.out.println("payByOrgAgt--" + payByOrgAgt + "--"
						+ "pwtAmount" + pwtAmount + "--agtPayLimit"
						+ agtPayLimit);

			} else { // go for BO's pending pwt
				status = "PND_PAY";
				payReqForOrgType = "BO";
				payReqForOrgId = approvedByOrgId; // Bo's organization id
			}

			// update the request table for pwt payments

			remarks = "Payment should be done at " + payReqForOrgType;
			String updateRequestStatus = "update st_se_pwt_approval_request_master set req_status=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,pay_req_for_org_type=?,pay_request_for_org_id=?,approval_date=?,remarks=? where task_id=?";
			pstmt = connection.prepareStatement(updateRequestStatus);
			pstmt.setString(1, status);
			pstmt.setString(2, "BO");
			pstmt.setInt(3, approvedByUserId);
			pstmt.setInt(4, approvedByOrgId);
			pstmt.setString(5, payReqForOrgType);
			pstmt.setInt(6, payReqForOrgId);
			pstmt.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(8, remarks);
			pstmt.setInt(9, taskId);
			int updatedRow = pstmt.executeUpdate();
			if (updatedRow == 1) {
				System.out.println("Request status updated by BO for Approval");
			} else {
				throw new LMSException(
						"Exception while approving request By BO");
			}

			// update the ticket_inv_detail table
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateTicketInvTable(ticketNbr, ticketNbr.split("-")[0]
					+ "-" + ticketNbr.split("-")[1], gameNbr, gameId, status,
					approvedByUserId, approvedByOrgId, "UPDATE", requestedById,
					channel, interfaceType, connection);

			// update pwt inventory table
			boolean isPwtUpdate = commHelper.updateVirnStatus(gameNbr, status,
					gameId, virnNbr, connection,MD5Encoder.encode(ticketNbr));
			if (!isPwtUpdate) {
				throw new LMSException("Error ::: pwt table not updated ");
			}

			connection.commit();
			return remarks;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}

	}

	/**
	 * This function is used to check the ticket validity with owner
	 * 
	 * @param gameNbr
	 * @param bookNbr
	 * @param ticketNbrDigit
	 * @param gameId
	 * @param connection
	 * @return ticketBean
	 * @throws SQLException
	 */
	public TicketBean checkTicketStatus(String gameNbr, String bookNbr,
			String ticketNbrDigit, int gameId, Connection connection,
			boolean isDirPlr) throws SQLException {
		TicketBean bean = new TicketBean();
		bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
		System.out.println("Book No is: " + bookNbr
				+ "  actual ticket number is " + ticketNbrDigit);

		String query1 = QueryManager
				.getST4CurrentOwnerDetailsUsingGameBookNbr();
		pstmt = connection.prepareStatement(query1);
		pstmt.setInt(1, gameId);
		pstmt.setString(2, bookNbr);
		result = pstmt.executeQuery();
		System.out.println("pstmt == " + pstmt);
		if (result.next()) {
			String ownerType = result.getString("current_owner");
			String bookStatus = result.getString("book_status");
			if (("RETAILER".equalsIgnoreCase(ownerType.trim()) || "AGENT"
					.equalsIgnoreCase(ownerType.trim()))
					&& ("ACTIVE".equalsIgnoreCase(bookStatus) || "CLAIMED"
							.equalsIgnoreCase(bookStatus))) {
				bean.setBookStatus(bookStatus);
				// check the status of ticket
				String query2 = QueryManager
						.getST4PwtTicketDetailsUsingGameNbr();
				PreparedStatement pstmt2 = connection.prepareStatement(query2);
				pstmt2.setInt(1, Integer.parseInt(gameNbr));
				pstmt2.setString(2, bookNbr + "-" + ticketNbrDigit);
				ResultSet resultSet2 = pstmt2.executeQuery();
				System.out.println("inside if = " + pstmt2);
				// required to be reviewed
				if (resultSet2.next()) {
					String ticketStatus = resultSet2.getString("status");

					/*
					 * if(ticketStatus.equalsIgnoreCase("CLAIM_RET") &&
					 * isDirPlr){ bean.setValid(false); bean.setStatus("Already
					 * paid to Retailer"); bean.setMessageCode("222010");
					 * bean.setValidity("InValid Ticket");
					 * System.out.println("Ticket's PWT Has Bean Paid By
					 * Agent."); }else
					 * if(ticketStatus.equalsIgnoreCase("CLAIM_RET") &&
					 * !isDirPlr){ bean.setValid(true);
					 * bean.setBook_nbr(bookNbr);
					 * bean.setGameNbr(Integer.parseInt(gameNbr));
					 * bean.setTicketGameId(gameId);
					 * bean.setTicketNumber(bookNbr+"-"+ticketNbrDigit);
					 * bean.setStatus(" "); bean.setMessageCode("221001");
					 * bean.setUpdateTicketType("UPDATE");
					 * bean.setValidity("Valid Ticket");
					 * System.out.println("Ticket IS valid to Pay"); }
					 */
					if (ticketStatus.equalsIgnoreCase("MISSING")) {
						bean.setValid(false);
						bean.setStatus("Ticket staus is MISSING in DATABASE");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out.println("Ticket is MISSING.");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_CLM")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Retailer by Agent and pending to claim by Agent at BO as auto scrap");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Paid to retailer By Agent and pending to claim at bo by agent as uato sarap");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Retailer by Agent and pending to claim by Agent at BO as auto scrap");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Paid to retailer As Auto Scrap and pending to claim at bo as AUTO Scrap");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")
							&& !isDirPlr) {
						bean.setValid(true);
						bean.setBook_nbr(bookNbr);
						bean.setGameNbr(Integer.parseInt(gameNbr));
						bean.setTicketGameId(gameId);
						bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
						bean.setStatus(" CLAIM_RET_UNCLM ");
						bean.setMessageCode("221001");
						bean.setUpdateTicketType("UPDATE");
						bean.setValidity("Valid Ticket");
						System.out
								.println("Ticket IS valid to Pay and Already paid to retailer by agent");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")
							&& isDirPlr) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Retailer by Agent and pending to claim by Agent at BO as Ticket Verification");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket IS not Valid to Pay and Already paid to retailer by agent");
					}
					if (ticketStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
						bean.setValid(false);
						bean
								.setStatus("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
					} else if (ticketStatus.equalsIgnoreCase("RETURN")
							&& !isDirPlr) {
						bean.setValid(false);
						bean
								.setStatus("Ticket once Verified and Return to Player for Verification as Direct Player PWT at BO");
						bean.setMessageCode("222007");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket Is valid for pwt. But Ticket Is High Prize Ticket, So Go for Direct PWT.  ");
					} else if (ticketStatus.equalsIgnoreCase("RETURN")
							&& isDirPlr) {
						bean.setValid(true);
						bean.setBook_nbr(bookNbr);
						bean.setGameNbr(Integer.parseInt(gameNbr));
						bean.setTicketGameId(gameId);
						bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
						bean
								.setStatus(" Ticket is Valid and has been Returned.");
						bean.setMessageCode("221001");
						bean.setUpdateTicketType("UPDATE");
						bean.setValidity("Valid Ticket");
						System.out
								.println("Ticket IS valid for direct player PWT");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent");
						bean.setMessageCode("222005");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By BO To Agent.");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent as auto scrap.");
						bean.setMessageCode("222005");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By BO To AGent as Auto Scrap");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
						bean.setValid(false);
						bean
								.setStatus("Already Verified in Bulk Receipt at BO/AGENT");
						bean.setMessageCode("222008");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket number is verified for temp table");
					}
					/*
					 * else if(ticketStatus.equalsIgnoreCase("CLAIM_PLR")){
					 * bean.setValid(false); bean.setStatus("Already paid as
					 * Direct Player PWT"); bean.setMessageCode("222006");
					 * bean.setValidity("InValid Ticket");
					 * System.out.println("Ticket Is High Prize Ticket, Ticket's
					 * PWT Has Been Paid. "); }
					 */
					else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_BO")) {
						bean.setValid(false);
						bean
								.setStatus("Already paid as Direct Player PWT To Player By BO");
						bean.setMessageCode("222006");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket Is High Prize Ticket, Ticket's PWT Has Been Paid. ");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")
							&& isDirPlr) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as Ticket Verification");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By Agent.");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")
							&& !isDirPlr) {
						bean.setValid(true);
						bean.setBook_nbr(bookNbr);
						bean.setGameNbr(Integer.parseInt(gameNbr));
						bean.setTicketGameId(gameId);
						bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
						bean
								.setStatus("Ticket is Valid and paid to player by retailer. ");
						bean.setMessageCode("221001");
						bean.setUpdateTicketType("UPDATE");
						bean.setValidity("Valid Ticket");
						System.out.println("Ticket IS valid to Pay");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Player By Retailer and inside Retailer's Claimable balance");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket IS valid for pwt. (Ticket has been paid to player by retailer so retailer should be paid by agent)");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")
							&& isDirPlr) {
						bean.setValid(false);
						bean
								.setStatus("Already paid to Player By Agent and pending to claim by Agent at BO as Ticket Verification ");
						bean.setMessageCode("TBD");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By Agent.");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")
							&& !isDirPlr) {
						bean.setValid(true);
						bean.setBook_nbr(bookNbr);
						bean.setGameNbr(Integer.parseInt(gameNbr));
						bean.setTicketGameId(gameId);
						bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
						bean
								.setStatus(" Ticket is Valid and paid to a player by Agent. ");
						bean.setMessageCode("221001");
						bean.setUpdateTicketType("UPDATE");
						bean.setValidity("Valid Ticket");
						System.out.println("Ticket IS valid to Pay");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
						bean.setValid(false);
						bean
								.setStatus("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
						bean.setValid(false);
						bean.setStatus("Agent has claimed as CLAIMABLE");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By Agent.");
					}

					/*
					 * else
					 * if(ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")){
					 * bean.setValid(true); bean.setBook_nbr(bookNbr);
					 * bean.setGameNbr(Integer.parseInt(gameNbr));
					 * bean.setTicketGameId(gameId);
					 * bean.setTicketNumber(bookNbr+"-"+ticketNbrDigit);
					 * bean.setStatus(""); bean.setMessageCode("Undefined");
					 * bean.setUpdateTicketType("UPDATE");
					 * bean.setValidity("Valid Ticket");
					 * System.out.println("Ticket IS Not valid for pwt. (Claimed
					 * by retailer in Unclaimed " + "Balanced)"); }
					 */
					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to player by retailer and inside retailer's Claimable so Ticket is Invalid.");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket IS valid for pwt. (Ticket has been paid to player by retailer so retailer should be paid by agent)");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")
							&& isDirPlr) {
						bean.setValid(false);
						bean.setStatus("Already paid to Player by Retailer");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By Agent.");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")
							&& !isDirPlr) {
						bean.setValid(true);
						bean.setBook_nbr(bookNbr);
						bean.setGameNbr(Integer.parseInt(gameNbr));
						bean.setTicketGameId(gameId);
						bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
						bean
								.setStatus(" Ticket is Valid and paid to a player by retailer. ");
						bean.setMessageCode("221001");
						bean.setUpdateTicketType("UPDATE");
						bean.setValidity("Valid Ticket");
						System.out.println("Ticket IS valid to Pay");
					} else if (ticketStatus.equalsIgnoreCase("PND_MAS")) {
						bean.setValid(false);
						bean
								.setStatus("This VIRN has Beean requested for BO Master Approval");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("This VIRN has Beean requested for BO Master Approval");
					}

					else if (ticketStatus.equalsIgnoreCase("PND_PAY")) {
						bean.setValid(false);
						bean
								.setStatus("This VIRN has Beean Approved And Pending to Payment");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("This VIRN has Beean Approved And Pending to Payment");
					}

					else if (ticketStatus.equalsIgnoreCase("REQUESTED")) {
						bean.setValid(false);
						bean
								.setStatus("This VIRN has Beean Already claimed and requested for Approval");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("This VIRN has Beean Already claimed and requested for Approval");
					}

					/*
					 * else
					 * if(ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")){
					 * bean.setValid(true); bean.setBook_nbr(bookNbr);
					 * bean.setGameNbr(Integer.parseInt(gameNbr));
					 * bean.setTicketGameId(gameId);
					 * bean.setTicketNumber(bookNbr+"-"+ticketNbrDigit);
					 * bean.setStatus(""); bean.setMessageCode("Undefined");
					 * bean.setUpdateTicketType("UPDATE");
					 * bean.setValidity("Valid Ticket");
					 * System.out.println("Ticket IS Not valid for pwt. (Claimed
					 * by retailer To A Player in Unclaimed " + "Balanced)"); }
					 */
					/*
					 * else if(ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET")) {
					 * bean.setValid(false); bean.setStatus(" ");
					 * bean.setMessageCode("221002"); bean.setValidity("InValid
					 * Ticket"); System.out.println("Ticket IS valid for pwt.
					 * (Ticket has been paid to player by retailer so retailer
					 * should be paid by agent)"); }
					 */
					else {
						bean.setValid(false);
						bean.setMessageCode("TBD");
						bean.setStatus("Undefined Status of Ticket's PWT: "
								+ ticketStatus);
						bean.setValidity("InValid Ticket");
						System.out.println("Undefined Status of Ticket's PWT");
					}

				} else { // check if this ticket is in temporary table
					/*
					 * String query4 = "select * from st_se_tmp_pwt_tickets_inv
					 * where ticket_nbr = ?"; pstmt =
					 * connection.prepareStatement(query4); pstmt.setString(1,
					 * bookNbr+"-"+ticketNbrDigit); ResultSet resultSet5 =
					 * pstmt.executeQuery(); if(resultSet5.next()){
					 * bean.setValid(false); bean.setStatus("Already Verified in
					 * Bulk Receipt at BO/AGENT");
					 * bean.setMessageCode("222008"); bean.setValidity("InValid
					 * Ticket"); System.out.println("Ticket number is verified
					 * for temp table"); } else {
					 */
					bean.setValid(true);
					bean.setBook_nbr(bookNbr);
					bean.setGameNbr(Integer.parseInt(gameNbr));
					bean.setTicketGameId(gameId);
					bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
					bean.setStatus("Ticket IS valid for pwt.");
					bean.setMessageCode("TBD");
					bean.setUpdateTicketType("INSERT");
					bean.setValidity("Valid Ticket");
					System.out
							.println("Ticket IS valid for pwt.(Means not fake and not in pwt table)");
					// }
				}

			} else { // when book owner is 'BO' OR book status is 'INACTIVE'
				bean.setValid(false);
				bean.setValidity("InValid Ticket");
				if ("BO".equalsIgnoreCase(ownerType)) {
					bean.setStatus("Ticket is in stock of BO");
					bean.setMessageCode("222003");
					System.out.println("Ticket owner is BO.");
				} else {
					bean.setStatus("Ticket is Not stock of BO");
					bean.setMessageCode("222003");
					System.out
							.println("Ticket status is not 'ACTIVE' or 'CLAIMED'");
				}
			}
		} else { // when book is not found in st_se_game_inv_status table
			bean.setValid(false);
			bean.setStatus("Number Format Error/Out of Range Please Check");
			bean.setMessageCode("222002");
			bean.setValidity("InValid Ticket");
			System.out.println("Ticket Is not of the company.");
		}

		return bean;
	}

	public boolean denyPWTProcess(int taskId, String virnCode, int gameId,
			String ticketNbr, String denyPwtStatus, int gameNbr, int userId,
			int userOrgId, String channel, String interfaceType)
			throws LMSException {

		boolean statusChange = false;
		// Connection connection=null;
		DBConnect dbConnect = new DBConnect();
		connection = dbConnect.getConnection();
		try {
			connection.setAutoCommit(false);
			CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
			// update direct playe temp table
			// 'CANCELLED_PERMANENT':'Permanent Cancellation',
			// 'UNCLM_CANCELLED':'Temporary Cancellation'
			String tempPwtStatus = "";
			String pwtStatus = "";
			int isPwtTicketDelete = 0;
			System.out.println("Deny type is  " + denyPwtStatus);
			if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus.trim())) {
				System.out.println("INside permanent cancellation "
						+ "denyPwtStatus " + denyPwtStatus);
				tempPwtStatus = "CANCEL";
				pwtStatus = "CANCELLED_PERMANENT";
				commonHelper.updateTicketInvTable(ticketNbr, "", gameNbr,
						gameId, "CANCELLED_PERMANENT", userId, userOrgId,
						"UPDATE", 0, channel, interfaceType, connection);
			} else if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())) {
				System.out.println("INside Temporary cancellation "
						+ "denyPwtStatus " + denyPwtStatus);
				tempPwtStatus = "CANCEL";
				pwtStatus = "UNCLM_CANCELLED";
				// delete entry in case of Temporary Cancellation
				isPwtTicketDelete = commonHelper.updateTicketInvTable(
						ticketNbr, "", gameNbr, gameId, "", userId, userOrgId,
						"DELETE", 0, channel, interfaceType, connection);

				/*
				 * PreparedStatement
				 * pstmtPwtTicketUpdate=connection.prepareStatement("delete from
				 * st_se_pwt_tickets_inv_? where game_id=? and ticket_nbr=?");
				 * pstmtPwtTicketUpdate.setInt(1,gameNbr);
				 * pstmtPwtTicketUpdate.setInt(2,gameId);
				 * pstmtPwtTicketUpdate.setString(3,ticketNbr);
				 * System.out.println("update ticket table:: " +
				 * pstmtPwtTicketUpdate);
				 * isPwtTicketDelete=pstmtPwtTicketUpdate.executeUpdate();
				 * System.out.println("query is " + pstmtPwtTicketUpdate);
				 */
			}

			PreparedStatement pstmt = connection
					.prepareStatement("update st_se_pwt_approval_request_master set req_status=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,approval_date=?,remarks=? where task_id=?");
			pstmt.setString(1, tempPwtStatus);
			pstmt.setString(2, "BO");
			pstmt.setInt(3, userId);
			pstmt.setInt(4, userOrgId);
			pstmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(6, "request Denied by BO As "
					+ denyPwtStatus.trim());
			pstmt.setInt(7, taskId);
			int isUpdate = pstmt.executeUpdate();
			System.out.println("update request temporary table ==" + pstmt);

			// update pwt inv table status
			PreparedStatement pstmtPwtInvUpdate = connection
					.prepareStatement("update st_se_pwt_inv_? set status=? where virn_code=? and game_id=?");
			pstmtPwtInvUpdate.setInt(1, gameNbr);
			pstmtPwtInvUpdate.setString(2, pwtStatus);
			pstmtPwtInvUpdate.setString(3, virnCode);
			pstmtPwtInvUpdate.setInt(4, gameId);
			int isPwtupdate = pstmtPwtInvUpdate.executeUpdate();
			System.out.println("update st_pwt_inv ==" + pstmtPwtInvUpdate);

			if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus.trim())
					&& isUpdate == 1 && isPwtupdate == 1
					&& isPwtTicketDelete == 1) {
				System.out.println("inside temporary commit ");
				statusChange = true;
				connection.commit();
			} else if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())
					&& isUpdate == 1 && isPwtupdate == 1) {
				System.out.println("inside permanent commit ");
				statusChange = true;
				connection.commit();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}

		}

		return statusChange;

	}

	public String doDirectPlrPwtPayAtBO(int gameId, int playerId,
			double pwtAmt, double tax, String virnCode, int taskId,
			String chequeNbr, String draweeBank, String issuingParty,
			java.sql.Date chqDate, String paymentType, int userOrgId,
			int userId, int gameNbr, String ticketNbr, String boOrgName,
			String rootPath, String channel, String interfaceType)
			throws LMSException {

		long transaction_id = -1;
		double net_amt = 0.0;
		List<Long> transIdList = new ArrayList<Long>();
		DBConnect dbConnect = new DBConnect();
		String receiptIdNGeneratedNo;
		try {
			connection = dbConnect.getConnection();
			connection.setAutoCommit(false);
			// insert into LMS transaction master
			pstmt = connection.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "BO");
			pstmt.executeUpdate();

			result = pstmt.getGeneratedKeys();
			if (result.next()) {
				transaction_id = result.getLong(1);
				transIdList.add(transaction_id);

				// insert in st bo transaction master
				pstmt = connection.prepareStatement(QueryManager
						.insertInBOTransactionMaster());
				pstmt.setLong(1, transaction_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, userOrgId);
				pstmt.setString(4, "PLAYER");
				pstmt.setInt(5, playerId);
				pstmt.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(7, "PWT_PLR");
				pstmt.executeUpdate();

				net_amt = pwtAmt - tax;
				pstmt = connection.prepareStatement(QueryManager
						.getST5DirectPlayerTransactionQuery());
				pstmt.setInt(1, taskId);
				pstmt.setString(2, ticketNbr);
				pstmt.setString(3, virnCode);
				pstmt.setLong(4, transaction_id);
				pstmt.setInt(5, gameId);
				pstmt.setInt(6, playerId);
				pstmt.setDouble(7, pwtAmt);
				pstmt.setDouble(8, tax);
				pstmt.setDouble(9, net_amt);
				pstmt.setTimestamp(10, new java.sql.Timestamp(
						new java.util.Date().getTime()));
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
				pstmt.executeUpdate();

				// update total prize remaining of that game
				// GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt,
				// "CLAIM_PLR", virnCode, connection,gameNbr);
				GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt,
						"CLAIM_PLR_BO", virnCode, connection, gameNbr);

				// update pwt ticket inv table
				CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
				commonHelper.updateTicketInvTable(ticketNbr, ticketNbr
						.split("-")[0]
						+ "-" + ticketNbr.split("-")[1], gameNbr, gameId,
						"CLAIM_PLR_BO", userId, userOrgId, "UPDATE", playerId,
						channel, interfaceType, connection);

				// update pwt inv table
				commonHelper.updateVirnStatus(gameNbr, "CLAIM_PLR_BO", gameId,
						virnCode, connection,MD5Encoder.encode(ticketNbr));

				// update status of of requested id entries into
				// st_se_pwt_approval_request_master
				String updateAppTable = "update  st_se_pwt_approval_request_master  set req_status ='PAID', "
						+ "remarks ='Payment Done', payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ?";
				pstmt = connection.prepareStatement(updateAppTable);
				pstmt.setString(1, "BO");
				pstmt.setInt(2, userOrgId);
				pstmt.setLong(3, transaction_id);
				pstmt.setInt(4, taskId);
				int j = pstmt.executeUpdate();
				System.out.println("total row updated = " + j
						+ " ,  requested id " + taskId + " status updated = "
						+ pstmt);
				if (j < 1) {
					throw new LMSException(
							"st_se_pwt_approval_request_master table not updated.");
				}

				// now generate Recipet at BO End
				receiptIdNGeneratedNo = commonHelper.generateReceiptBo(
						connection, playerId, "PLAYER", transIdList);
				if (receiptIdNGeneratedNo != null) {
					connection.commit();
				} else {
					throw new LMSException(
							"Error during generating receipts for PWT :: ");
				}
				if (Integer.parseInt(receiptIdNGeneratedNo.split("-")[0]) > 0) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					// graphReportHelper.createTextReportPlayer(Integer.parseInt((receiptIdNGeneratedNo.split("-")[0])),rootPath,boOrgName,pwtAmt,tax,playerId,userOrgId);
					graphReportHelper.createTextReportPlayer(taskId, rootPath,
							"SCRATCH_GAME");
				}

			} else {
				throw new LMSException(
						"Exception :: Transaction Id is not generated:: transaction Id is "
								+ transaction_id);
			}
			return receiptIdNGeneratedNo.split("-")[1];
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}
	}

	public PwtApproveRequestNPlrBean getRequestDetails(int taskId,
			String partyType) throws LMSException {
		DBConnect dbconnect = new DBConnect();
		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet resultFromDb;
		connection = dbconnect.getConnection();
		try {

			String getPwtDetailsQuery = null;

			if ("AGENT".equals(partyType)) {
				getPwtDetailsQuery = "select a.task_id,a.requested_by_org_id,a.requester_type,a.party_type,a.party_id,a.request_id,a.pwt_amt,a.tax_amt,a.net_amt,a.req_status,a.ticket_nbr,a.virn_code,a.remarks,a.game_id,d.game_name,d.game_nbr,c.name from st_se_pwt_approval_request_master a ,st_se_game_master d,st_lms_organization_master c  where 1=1 and d.game_id=a.game_id   and task_id=? and party_type='AGENT' and c.organization_id=a.party_id";
			} else if ("PLAYER".equals(partyType)) {
				getPwtDetailsQuery = "select a.task_id,a.requested_by_org_id,a.requester_type,a.party_type,a.request_id,a.party_id,a.pwt_amt,a.tax_amt,a.net_amt,a.req_status,a.ticket_nbr,a.virn_code,a.remarks,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,b.player_id,b.bank_acc_nbr,b.bank_name,b.bank_branch,b.location_city,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a , st_lms_player_master b ,st_se_game_master d where 1=1 and d.game_id=a.game_id   and a.task_id=? and party_type='PLAYER' and a.party_id=b.player_id";
			} else {
				throw new LMSException("Error because party type is "
						+ partyType);
			}

			System.out.println("query to get pwt details :"
					+ getPwtDetailsQuery);
			pstmt = connection.prepareStatement(getPwtDetailsQuery);
			pstmt.setInt(1, taskId);
			resultFromDb = pstmt.executeQuery();
			// PlayerBean plrBean=new PlayerBean();
			PwtApproveRequestNPlrBean plrPwtBean = new PwtApproveRequestNPlrBean();
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
				plrPwtBean.setTicket_nbr(resultFromDb.getString("ticket_nbr"));
				plrPwtBean.setVirn_nbr(resultFromDb.getString("virn_code"));
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
			se.printStackTrace();
			throw new LMSException();
		}
		// return null;

	}

	/**
	 * This method is used to get requested pwts from request table
	 * 
	 */
	public List<PwtApproveRequestNPlrBean> getRequestedPwts(String requestId,
			int requestedById, String requesterType, String firstName,
			String lastName, String status, int requestedToOrgId,
			String partyType) throws LMSException {

		DBConnect dbconnect = new DBConnect();
		// Connection con=null;
		Statement stmtGetReqDetails = null;
		ResultSet reqDetails;
		connection = dbconnect.getConnection();
		StringBuilder getRequestDetailsQuery = null;
		new StringBuilder(
				"select a.task_id,a.party_type,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,a.requester_type,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id  ");

		if ("PLAYER".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.task_id,a.party_type,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,a.requester_type,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id  ");
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
					"select a.task_id,a.party_type,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,a.requester_type,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a ,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id ");

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

		System.out.println("requests Details Query:: "
				+ getRequestDetailsQuery.toString());
		try {
			List<PwtApproveRequestNPlrBean> pwtReqDetailsList = new ArrayList<PwtApproveRequestNPlrBean>();
			PwtApproveRequestNPlrBean pwtAppReqPlrBean;
			stmtGetReqDetails = connection.createStatement();
			reqDetails = stmtGetReqDetails.executeQuery(getRequestDetailsQuery
					.toString());
			while (reqDetails.next()) {
				pwtAppReqPlrBean = new PwtApproveRequestNPlrBean();
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
				pwtAppReqPlrBean.setVirn_nbr(reqDetails.getString("virn_code"));
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
			return pwtReqDetailsList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql Exception", e);
		}

	}

	/**
	 * This method is used to get pending pwts to pay from request table
	 */
	public List<PwtApproveRequestNPlrBean> getRequestsPwtsToPay(
			String requestId, int pendingAtAgtOrgId, String firstName,
			String lastName, String status, int payByOrgId,
			String paymentPendingAt, String partyType) throws LMSException {

		DBConnect dbconnect = new DBConnect();
		// Connection con=null;
		Statement stmtGetReqDetails = null;
		ResultSet reqDetails;
		connection = dbconnect.getConnection();

		StringBuilder getRequestDetailsQuery = null;

		if (partyType != null && !"".equals(partyType)
				&& "PLAYER".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.party_type,a.task_id,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id and a.party_type='PLAYER' ");
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
					"select a.party_type,a.task_id,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a ,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.party_id and d.game_id=a.game_id and a.party_type='AGENT'");

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

		System.out.println("requests Details Query:: "
				+ getRequestDetailsQuery.toString());
		try {
			List<PwtApproveRequestNPlrBean> pwtReqDetailsList = new ArrayList<PwtApproveRequestNPlrBean>();
			PwtApproveRequestNPlrBean pwtAppReqPlrBean;
			stmtGetReqDetails = connection.createStatement();
			reqDetails = stmtGetReqDetails.executeQuery(getRequestDetailsQuery
					.toString());
			while (reqDetails.next()) {
				pwtAppReqPlrBean = new PwtApproveRequestNPlrBean();
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
				pwtAppReqPlrBean.setVirn_nbr(reqDetails.getString("virn_code"));
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
			}
			return pwtReqDetailsList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql Exception", e);
		}

	}

	public List<GamePlayerPWTBean> getUnapprovedPwt() throws LMSException {
		DBConnect connect = new DBConnect();
		Connection con = null;
		PreparedStatement pstmt;
		GamePlayerPWTBean gamePlayerBean;
		List<GamePlayerPWTBean> gamePlayerBeanList = new ArrayList<GamePlayerPWTBean>();
		try {
			con = connect.getConnection();
			String getUnapprovePwtQuery = "select b.game_name,b.game_nbr,a.pwt_receipt_id,a.player_id,a.game_id,a.virn_code,a.pwt_amt,a.status,a.ticket_nbr from st_se_direct_player_pwt_temp_receipt a,st_se_game_master b where a.status=? and a.game_id=b.game_id";

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

			e.printStackTrace();

		} finally {
			try {

				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;

	}

	public String payPendingPwt(int taskId, double pwtAmount, double taxAmt,
			double netAmt, int partyId, String partyType, String ticketNbr,
			String virnNbr, int gameId, int payByOrgId, int payByUserId,
			String payByOrgName, String paymentType, String chqNbr,
			String draweeBank, String chequeDate, String issuiningParty,
			int gameNbr, String rootPath, String channel, String interfaceType)
			throws LMSException {

		// this field will be removed from st agent pwt table so no need to pass
		// this variable for time being put it as zero instead of fetching
		// userid from database
		String autoGeneratedReceiptNo = null;
		int partyUserId = 0;
		List<PwtBean> pwtList = new ArrayList<PwtBean>();
		PwtBean pwtBean = new PwtBean();
		pwtBean.setPwtAmount(String.valueOf(pwtAmount));
		pwtBean.setEncVirnCode(virnNbr);
		pwtBean.setValid(true);
		pwtList.add(pwtBean);

		CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
		String receipts;
		if ("AGENT".equals(partyType)) {
			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();
			receipts = commonHelper.boEndAgtPWTPaymentProcess(pwtList, gameId,
					payByOrgName, payByOrgId, partyId, partyUserId, rootPath,
					partyUserId, gameNbr, connection);
			autoGeneratedReceiptNo = receipts.split("-")[1];
		} else if ("PLAYER".equals(partyType)) {
			// do direct Player payment at Bo End
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			java.sql.Date chqDate = null;
			try {
				if (!"".equalsIgnoreCase(chequeDate) && chequeDate != null) {
					chqDate = new java.sql.Date(dateFormat.parse(chequeDate)
							.getTime());
				}
			} catch (ParseException e) {
				e.printStackTrace();
				throw new LMSException(
						"Exception date parsing  while pwt payments at BO end ",
						e);
			}

			autoGeneratedReceiptNo = doDirectPlrPwtPayAtBO(gameId, partyId,
					pwtAmount, taxAmt, virnNbr, taskId, chqNbr, draweeBank,
					issuiningParty, chqDate, paymentType, payByOrgId,
					payByUserId, gameNbr, ticketNbr, payByOrgName, rootPath,
					channel, interfaceType);
			System.out.println("auto generated receipt number is  "
					+ autoGeneratedReceiptNo);
		} else {
			throw new LMSException(
					"Exception Ocurred :: Party Type is not Selected Properly :: Only Agent and Player can claim at BO End :: Party Type is: "
							+ partyType);
		}
		return autoGeneratedReceiptNo;
	}

	public Map plrRegistrationAndApproval(UserInfoBean userInfoBean,
			Map pwtDetails, String playerType, int playerId,
			PlayerBean plrBean, String rootPath) throws LMSException {
		Map pwtAppMap = new TreeMap();
		TicketBean tktBean = (TicketBean) pwtDetails.get("tktBean");
		PwtBean pwtBean = (PwtBean) pwtDetails.get("pwtBean");

		try {
			connection = new DBConnect().getConnection();
			connection.setAutoCommit(false);
			// register player
			if (plrBean != null) {
				playerId = new PlayerVerifyHelperForApp().registerPlayer(
						plrBean, connection);
			}
			System.out.println("Player id is " + playerId
					+ " for that approval required");
			if (playerId <= 0) {
				throw new LMSException(
						"error while player registration process player id is"
								+ playerId);
			}

			System.out.println("is Approval required = '" + pwtBean.getAppReq()
					+ "' , to  '" + userInfoBean.getUserType()
					+ "' of orgId = " + userInfoBean.getUserOrgId()
					+ " and user id = " + userInfoBean.getUserId());
			// if approval is required request would be sent to Back office
			// master else request would be sent to bo payments

			int reqToOrgId = 0;
			String reqToOrgType = null;
			String remarks = null;
			String reqStatus = null;
			String approvedByType = null;
			int approvedByUserId = 0;
			int approvedByOrgId = 0;

			if (pwtBean.getAppReq()) { // means approval from BO master is
				// required
				// get Back office organization and user id
				reqToOrgId = userInfoBean.getParentOrgId();
				reqToOrgType = "BO";
				remarks = "requested to BO master  For Approval";
				reqStatus = "PND_MAS";

			} else {
				// go for pending payments
				reqToOrgId = userInfoBean.getUserOrgId();
				reqToOrgType = userInfoBean.getUserType();
				remarks = "Auto Approved By BO";
				reqStatus = "PND_PAY";
				approvedByType = "BO";
				approvedByUserId = userInfoBean.getUserId();
				approvedByOrgId = reqToOrgId;
			}

			System.out.println("Approval requested to orgId = " + reqToOrgId
					+ "  and user type = " + reqToOrgType);

			// generate TEMP receipt for Approval
			// GenerateRecieptNo.get
			String recIdForApp = GenerateRecieptNo.generateRequestId("REQUEST");

			// insert into Approval table
			String insertAppQuery = "insert into  st_se_pwt_approval_request_master (party_type ,request_id,party_id,ticket_nbr,virn_code,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			System.out.println("insertAppQuery = " + insertAppQuery);
			pstmt = connection.prepareStatement(insertAppQuery);
			pstmt.setString(1, playerType.toUpperCase());
			pstmt.setString(2, recIdForApp);

			boolean isPlr = "player".equalsIgnoreCase(playerType.trim());
			if (isPlr) {
				pstmt.setInt(3, playerId);
			} else {
				pstmt.setObject(3, null);
			}

			pstmt.setString(4, tktBean.getTicketNumber());
			pstmt.setString(5, pwtBean.getEncVirnCode());
			pstmt.setInt(6, tktBean.getTicketGameId());
			pstmt.setDouble(7, Double.parseDouble(pwtBean.getPwtAmount()));
			pstmt.setDouble(8, 0.0);
			pstmt.setDouble(9, Double.parseDouble(pwtBean.getPwtAmount()));
			pstmt.setString(10, reqStatus);
			pstmt.setString(11, userInfoBean.getUserType());
			pstmt.setInt(12, userInfoBean.getUserId());
			pstmt.setInt(13, userInfoBean.getUserOrgId());
			pstmt.setInt(14, reqToOrgId);
			pstmt.setString(15, reqToOrgType);
			if (pwtBean.getAppReq()) {
				pstmt.setObject(16, null);
				pstmt.setObject(17, null);
				pstmt.setObject(18, null);
				pstmt.setObject(19, null);
				pstmt.setObject(20, null);
				pstmt.setObject(21, null);
			} else {
				pstmt.setString(16, approvedByType);
				pstmt.setInt(17, approvedByUserId);
				pstmt.setInt(18, approvedByOrgId);
				pstmt.setString(19, approvedByType);
				pstmt.setInt(20, approvedByOrgId);
				pstmt.setTimestamp(21, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			}

			pstmt.setTimestamp(22, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			pstmt.setString(23, remarks);

			System.out.println("insertAppQuery pppppp = " + pstmt);
			pstmt.executeUpdate();

			System.out.println("insertion into pwt temp request  table = "
					+ pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			int reqId = 0;
			if (rs.next()) {
				reqId = rs.getInt(1);
			} else {
				throw new LMSException(
						"NO Data Inserted in st_se_pwt_approval_request_master table");
			}

			// update main ticket table
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			commonFunction.updateTicketInvTable(tktBean.getTicketNumber(),
					tktBean.getBook_nbr(), tktBean.getGameNbr(), tktBean
							.getTicketGameId(), reqStatus, userInfoBean
							.getUserId(), userInfoBean.getUserOrgId(), tktBean
							.getUpdateTicketType(), playerId, userInfoBean
							.getChannel(), userInfoBean.getInterfaceType(),
					connection);

			// update book status from active to claimed in table
			// st_se_game_inv_status table here
			if ("ACTIVE".equalsIgnoreCase(tktBean.getBookStatus())) {
				commonFunction.updateBookStatus(tktBean.getTicketGameId(),
						tktBean.getBook_nbr(), connection, "CLAIMED");
			}

			// update main pwt(virn table)
			commonFunction.updateVirnStatus(tktBean.getGameNbr(), reqStatus,
					tktBean.getTicketGameId(), pwtBean.getEncVirnCode(),
					connection,MD5Encoder.encode(tktBean.getTicketNumber()));
			connection.commit();

			pwtAppMap.put("recId", recIdForApp);
			pwtAppMap.put("reqId", reqId);
			pwtAppMap.putAll(pwtDetails);
			pwtAppMap.put("remarks", remarks);

			// now generate temporary receipt for player
			GraphReportHelper graphHelpwr = new GraphReportHelper();
			graphHelpwr.createTextReportTempPlayerReceipt(reqId + "", "BO",
					rootPath, "SCRATCH_GAME");

			return pwtAppMap;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new LMSException(e);
				}
			}
		}
	}

	public Map<String, Object> verifyAndSaveTicketNVirnDirPlr(String ticketNbr,
			String virnNbr, int gameId, String gameNbr, String gameName,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval)
			throws LMSException {

		try {
			connection = new DBConnect().getConnection();
			connection.setAutoCommit(false);
			// validate the ticket
			Map<String, Object> detailMap = new TreeMap<String, Object>();
			// add '-' if ticket nymber does not contains '-' using game format
			// digits

			// check ticket format
			// added by yogesh
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			TicketBean tktBean = commonFunction.isTicketFormatValid(ticketNbr,
					gameId, connection);
			if (tktBean != null && !tktBean.getIsValid()) {
				detailMap.put("returnType", "input");
				tktBean.setTicketNumber(ticketNbr);
				detailMap.put("tktBean", tktBean);
				return detailMap;
			}

			// tktArr[0]='game NBR', tktArr[1]='Book NBR', tktArr[2]='Ticket NBR
			String tktArr[] = tktBean.getTicketNumber().split("-");
			tktBean = checkTicketStatus(tktArr[0], tktArr[0] + "-" + tktArr[1],
					tktArr[2], gameId, connection, true);
			if (tktBean != null && !tktBean.getIsValid()) {
				detailMap.put("returnType", "input");
				tktBean.setTicketNumber(ticketNbr);
				detailMap.put("tktBean", tktBean);
				return detailMap;
			}

			tktBean.setTicketGameId(gameId);
			tktBean.setTicketNumber(tktArr[0] + "-" + tktArr[1] + "-"
					+ tktArr[2]);
			tktBean.setBook_nbr(tktArr[0] + "-" + tktArr[1]);
			tktBean.setGameNbr(Integer.parseInt(tktArr[0]));
			detailMap.put("tktBean", tktBean);

			// validate VIRN nbr
			PwtBean pwtBean = verifyPwtVirnDirPlr(gameId, virnNbr, Integer
					.parseInt(gameNbr), connection, userInfoBean,
					pwtAmtForMasterApproval, ticketNbr);
			if (pwtBean != null && pwtBean.getIsValid()) {
				detailMap.put("tktBean", tktBean);
				detailMap.put("pwtBean", pwtBean);
				detailMap.put("returnType", "registration");
				System.out.println("go to registration limit");
				// connection.commit();
				return detailMap;

			} else {
				detailMap.put("tktBean", tktBean);
				detailMap.put("pwtBean", pwtBean);
				detailMap.put("returnType", "input");
				return detailMap;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

	}

	private PwtBean verifyPwtVirnDirPlr(int gameId, String virnCode,
			int gameNbr, Connection connection, UserInfoBean userInfoBean,
			String pwtAmtForMasterApproval, String ticketNbr)
			throws LMSException {

		PwtBean pwtBean = new PwtBean();
		pwtBean.setVirnCode(virnCode);
		pwtBean.setMessageCode("212013");
		pwtBean.setMessage("Invalid VIRN");
		pwtBean.setVerificationStatus("InValid Virn");

		String encodedVirnCode = MD5Encoder.encode(virnCode);
		pwtBean.setEncVirnCode(encodedVirnCode);
		System.out.println("Encoded virn == " + encodedVirnCode);
		String encodedTktNo = MD5Encoder.encode(ticketNbr);
		pwtBean.setEnctktNumber(encodedTktNo);
		System.out.println("Encoded Ticket Number == " + encodedTktNo);
		try {
			Statement statement = connection.createStatement();
			Statement statement4 = connection.createStatement();
			ResultSet resultSet4;
			StringBuffer query = new StringBuffer();
			// query.append(QueryManager.getST1PWTBOCheckQuery()).append(
			// " st_se_pwt_inv_" + gameNbr + " where ");
			// query.append(" game_id = " + gameId).append(" and virn_code in
			// ('");
			// query.append(encodedVirnCode).append("')").append(" and id1='");
			// query.append(encodedTktNo).append("'");

			query.append(QueryManager.getST1PWTBOCheckQuery()).append(
					" st_se_pwt_inv_" + gameNbr + " where ");
			query.append(" game_id = " + gameId).append(" and virn_code='");
			query.append(encodedVirnCode).append("'").append(" and id1='");
			query.append(encodedTktNo).append("'");

			System.out.println("GameId:" + gameId + "\nQuery:: " + query);

			ResultSet resultSet = statement.executeQuery(query.toString());
			System.out.println("ResultSet:" + resultSet + "---"
					+ resultSet.getFetchSize());
			if (resultSet.next()) {
				String vCode = resultSet
						.getString(TableConstants.SPI_VIRN_CODE);
				String pwtAmount = resultSet
						.getString(TableConstants.SPI_PWT_AMT);
				String prizeLevel = resultSet
						.getString(TableConstants.SPI_PRIZE_LEVEL);
				String prizeStatus = resultSet.getString("status");
				System.out.println("Vcode : " + vCode + "\nPWT Amt : "
						+ pwtAmount + "\nPrize level : " + prizeLevel
						+ "\nstatus : " + prizeStatus);

				if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
						|| prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
					pwtBean.setPwtAmount(pwtAmount);
					double pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
					boolean isPwtAmtForMasterApproval = pwtAmt >= Double
							.parseDouble(pwtAmtForMasterApproval);
					pwtBean.setEncVirnCode(MD5Encoder.encode(pwtBean
							.getVirnCode()));
					if (isPwtAmtForMasterApproval) {
						System.out
								.println("pwt amount is greater then master approbal limit so go gor masyter approval");
						pwtBean.setValid(true);
						pwtBean.setVerificationStatus("Valid Virn");
						pwtBean.setMessage("Send for Master Approval");
						pwtBean.setMessageCode("Undefined");
						pwtBean.setAppReq(isPwtAmtForMasterApproval);
						return pwtBean;
					} else {// if PWT amount is in agents payment limit
						System.out
								.println("should be send to payment process and it is auto approved");
						pwtBean.setValid(true);
						pwtBean.setVerificationStatus("Valid Virn");
						pwtBean
								.setMessage("Should pay to retailer after registration by agent");
						return pwtBean;
					}
				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String retDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_agent_pwt b,st_lms_agent_receipts c,st_lms_agent_transaction_master e where b.virn_code='"
							+ encodedVirnCode
							+ "' and b.game_id="
							+ gameId
							+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=e.transaction_id)";
					resultSet4 = statement4.executeQuery(retDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("Already Paid to Retailer: " + orgname
							+ " on Voucher Number: " + receiptNumber + " at "
							+ receiptTime);
					pwtBean.setMessageCode("212001");

				} else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
					// get player details from st Agent direct player
					// details************8

					String agtOrgNAme = null;
					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					// String plrDetailsQuery="select first_name,last_name,city
					// from st_lms_player_master where player_id in (select
					// player_id from st_se_direct_player_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";
					// String plrDetailsQuery="select
					// b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id
					// from st_se_direct_player_pwt a,st_lms_player_master
					// b,st_lms_bo_receipts c where a.virn_code='"+enVirnCode+"'
					// and a.game_id="+gameId+" and a.player_id=b.player_id and
					// c.receipt_id=(select receipt_id from
					// st_lms_bo_receipts_trn_mapping where
					// transaction_id=a.transaction_id)";
					String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id,d.name from st_se_agt_direct_player_pwt a,st_lms_player_master b,st_lms_agent_receipts c,st_lms_organization_master d where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=a.transaction_id) and d.organization_id=a.agent_org_id";
					System.out.println("query for get player name "
							+ plrDetailsQuery);
					resultSet4 = statement4.executeQuery(plrDetailsQuery);
					while (resultSet4.next()) {
						playerFirstName = resultSet4.getString("first_name");
						playerLastName = resultSet4.getString("last_name");
						playercity = resultSet4.getString("city");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
						agtOrgNAme = resultSet4.getString("name");
					}

					pwtBean.setMessage("Already Paid to Player: "
							+ playerFirstName + " " + playerLastName + " "
							+ playercity + " By Agent: " + agtOrgNAme
							+ " on Voucher Number: " + receiptNumber + " at "
							+ receiptTime);
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessageCode("TBD");

				} else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
					String agtOrgNAme = null;
					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					// String plrDetailsQuery="select first_name,last_name,city
					// from st_lms_player_master where player_id in (select
					// player_id from st_se_direct_player_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";
					// String plrDetailsQuery="select
					// b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id
					// from st_se_direct_player_pwt a,st_lms_player_master
					// b,st_lms_bo_receipts c where a.virn_code='"+enVirnCode+"'
					// and a.game_id="+gameId+" and a.player_id=b.player_id and
					// c.receipt_id=(select receipt_id from
					// st_lms_bo_receipts_trn_mapping where
					// transaction_id=a.transaction_id)";
					String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id,d.name from st_se_agt_direct_player_pwt a,st_lms_player_master b,st_lms_agent_receipts c,st_lms_organization_master d where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=a.transaction_id) and d.organization_id=a.agent_org_id";
					System.out.println("query for get player name "
							+ plrDetailsQuery);
					resultSet4 = statement4.executeQuery(plrDetailsQuery);
					while (resultSet4.next()) {
						playerFirstName = resultSet4.getString("first_name");
						playerLastName = resultSet4.getString("last_name");
						playercity = resultSet4.getString("city");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
						agtOrgNAme = resultSet4.getString("name");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("Already Paid to Player: "
							+ playerFirstName + " " + playerLastName + " "
							+ playercity + " By Agent: " + agtOrgNAme
							+ " on Voucher Number: " + receiptNumber + " at "
							+ receiptTime + " in claimmable balance");
					pwtBean.setMessageCode("TBD");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
							+ encodedVirnCode
							+ "' and game_id="
							+ gameId
							+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";
					resultSet4 = statement4.executeQuery(partyDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptNumber = resultSet4.getString("receipt_id");
						receiptTime = resultSet4.getTimestamp("date_entered");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at BO for agent: "
									+ orgname
									+ " on Bulk Receipt Number: "
									+ receiptNumber
									+ " at "
									+ receiptTime
									+ ", Final Payment Pending");
					pwtBean.setMessageCode("112001");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
							+ encodedVirnCode
							+ "' and game_id="
							+ gameId
							+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";
					resultSet4 = statement4.executeQuery(partyDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptNumber = resultSet4.getString("receipt_id");
						receiptTime = resultSet4.getTimestamp("date_entered");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at BO for agent: "
									+ orgname
									+ " on Bulk Receipt Number: "
									+ receiptNumber
									+ " at "
									+ receiptTime
									+ ", Final Payment Pending");
					pwtBean.setMessageCode("112001");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
							+ encodedVirnCode
							+ "' and game_id="
							+ gameId
							+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";
					resultSet4 = statement4.executeQuery(partyDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptNumber = resultSet4.getString("receipt_id");
						receiptTime = resultSet4.getTimestamp("date_entered");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at BO for agent: "
									+ orgname
									+ " on Bulk Receipt Number: "
									+ receiptNumber
									+ " at "
									+ receiptTime
									+ ", Final Payment Pending");
					pwtBean.setMessageCode("112001");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT")) {
					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String agtDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_bo_pwt b,st_lms_bo_receipts c,st_lms_bo_transaction_master e where b.virn_code='"
							+ encodedVirnCode
							+ "' and b.game_id="
							+ gameId
							+ " and a.organization_id=b.agent_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=e.transaction_id)";
					System.out.println("query for get org name "
							+ agtDetailsQuery);
					resultSet4 = statement4.executeQuery(agtDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("Already Paid to Agent: " + orgname
							+ " on Voucher Number: " + receiptNumber + " at "
							+ receiptTime);
					pwtBean.setMessageCode("112003");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String agtDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_bo_pwt b,st_lms_bo_receipts c,st_lms_bo_transaction_master e where b.virn_code='"
							+ encodedVirnCode
							+ "' and b.game_id="
							+ gameId
							+ " and a.organization_id=b.agent_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=e.transaction_id)";
					resultSet4 = statement4.executeQuery(agtDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("Already Paid As Auto Scrap to Agent: "
							+ orgname + " on Voucher Number: " + receiptNumber
							+ " at " + receiptTime);
					pwtBean.setMessageCode("112003");
				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_BO")) {
					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					// String plrDetailsQuery="select first_name,last_name,city
					// from st_lms_player_master where player_id in (select
					// player_id from st_se_direct_player_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";
					String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id from st_se_direct_player_pwt a,st_lms_player_master b,st_lms_bo_receipts c where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=a.transaction_id)";
					System.out.println("query for get player name "
							+ plrDetailsQuery);
					resultSet4 = statement4.executeQuery(plrDetailsQuery);
					while (resultSet4.next()) {
						playerFirstName = resultSet4.getString("first_name");
						playerLastName = resultSet4.getString("last_name");
						playercity = resultSet4.getString("city");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}
					pwtBean.setValid(false);
					pwtBean
							.setMessage("Already Paid By BO as Direct Player PWT to Player: "
									+ playerFirstName
									+ " "
									+ playerLastName
									+ ","
									+ playercity
									+ " on Voucher Number "
									+ receiptNumber + " at " + receiptTime);
					pwtBean.setMessageCode("112005");
					pwtBean.setVerificationStatus("InValid Virn");
				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
					// show info of retailer if available from at retailer PWT

					String orgname = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";

					// String agtDetailsQuery="select
					// a.name,c.generated_id,e.transaction_date from
					// st_lms_organization_master a,st_se_bo_pwt
					// b,st_lms_bo_receipts c,st_lms_bo_transaction_master e
					// where b.virn_code='"+enVirnCode+"' and
					// b.game_id="+gameId+" and a.organization_id=b.agent_org_id
					// and b.transaction_id=e.transaction_id and
					// c.receipt_id=(select receipt_id from
					// st_lms_bo_receipts_trn_mapping where
					// transaction_id=e.transaction_id)";
					String retDetailsQuery = "select a.name,e.transaction_date from st_lms_organization_master a,st_se_retailer_pwt b,st_lms_retailer_transaction_master e where b.virn_code='"
							+ encodedVirnCode
							+ "' and b.game_id="
							+ gameId
							+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id";

					System.out.println("query for get org name "
							+ retDetailsQuery);
					resultSet4 = statement4.executeQuery(retDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}

					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessageCode("TBD");
					pwtBean
							.setMessage("This VIRN No. has been paid to Player by retailer:"
									+ orgname
									+ " on Voucher "
									+ receiptTime
									+ " but not claimed by retailer to agent ");
					// pwtBean.setMessage("This VIRN No. has been paid to Player
					// by retailer but not claimed by retailer to agent ");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
					// show info of retailer if available from at retailer PWT
					String orgname = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";

					// String agtDetailsQuery="select
					// a.name,c.generated_id,e.transaction_date from
					// st_lms_organization_master a,st_se_bo_pwt
					// b,st_lms_bo_receipts c,st_lms_bo_transaction_master e
					// where b.virn_code='"+enVirnCode+"' and
					// b.game_id="+gameId+" and a.organization_id=b.agent_org_id
					// and b.transaction_id=e.transaction_id and
					// c.receipt_id=(select receipt_id from
					// st_lms_bo_receipts_trn_mapping where
					// transaction_id=e.transaction_id)";
					String retDetailsQuery = "select a.name,e.transaction_date from st_lms_organization_master a,st_se_retailer_pwt b,st_lms_retailer_transaction_master e where b.virn_code='"
							+ encodedVirnCode
							+ "' and b.game_id="
							+ gameId
							+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id";

					System.out.println("query for get org name "
							+ retDetailsQuery);
					resultSet4 = statement4.executeQuery(retDetailsQuery);
					while (resultSet4.next()) {
						orgname = resultSet4.getString("name");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}

					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessageCode("TBD");
					pwtBean
							.setMessage("This VIRN No. has been paid to Player by retailer:"
									+ orgname
									+ " on Voucher "
									+ receiptTime
									+ " but not claimed by retailer to agent ");

				} else if (prizeStatus.equalsIgnoreCase("MISSING")) {
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("VIRN is from MISSING Status");
					pwtBean.setMessageCode("");

				} else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
					// show info of retailer if available from at retailer
					// direct player
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessageCode("112005");
					pwtBean
							.setMessage("This VIRN No. has been paid to Player by retailer but not claimed by retailer to agent ");

				} else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
					// show info of retailer if available from at retailer
					// derect palyer PWT
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("This VIRN No. has been paid to Player by retailer As Claimmable ");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_TEMP")) {
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at Agent,Final Payment Pending");
					pwtBean.setMessageCode("112006");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_CLM")) {
					// show agent and retailers name
					String agtOrgname = null;
					String retOrgName = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String agtDetailsQuery = "select  b.transaction_date,c.generated_id,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'agt_name' ,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'ret_name'  from st_se_agent_pwt a,st_lms_agent_transaction_master b,st_lms_agent_receipts c where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.transaction_id=b.transaction_id  and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=b.transaction_id)";

					System.out.println("query for get org name "
							+ agtDetailsQuery);
					resultSet4 = statement4.executeQuery(agtDetailsQuery);
					while (resultSet4.next()) {
						agtOrgname = resultSet4.getString("agt_name");
						retOrgName = resultSet4.getString("ret_name");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Paid to retailer: "
									+ retOrgName
									+ " By Agent: "
									+ agtOrgname
									+ " Voucher Number:"
									+ receiptNumber
									+ " on "
									+ receiptTime
									+ " and pending to claim at bo by agent as uato sarap");
					pwtBean.setMessageCode("TBD");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
					// show agent and retailers name
					String agtOrgname = null;
					String retOrgName = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String agtDetailsQuery = "select  b.transaction_date,c.generated_id,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'agt_name' ,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'ret_name'  from st_se_agent_pwt a,st_lms_agent_transaction_master b,st_lms_agent_receipts c where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.transaction_id=b.transaction_id  and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=b.transaction_id)";

					System.out.println("query for get org name "
							+ agtDetailsQuery);
					resultSet4 = statement4.executeQuery(agtDetailsQuery);
					while (resultSet4.next()) {
						agtOrgname = resultSet4.getString("agt_name");
						retOrgName = resultSet4.getString("ret_name");
						receiptNumber = resultSet4.getString("generated_id");
						receiptTime = resultSet4
								.getTimestamp("transaction_date");
					}
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Paid to retailer: "
									+ retOrgName
									+ " By Agent: "
									+ agtOrgname
									+ " Voucher Number:"
									+ receiptNumber
									+ " on "
									+ receiptTime
									+ " As Auto Scrap and pending to claim at bo as AUTO Scrap");
					pwtBean.setMessageCode("TBD");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at Agent,Final Payment Pending");
					pwtBean.setMessageCode("112007");

				} else if (prizeStatus.equalsIgnoreCase("REQUESTED")) {
					// show request details from request table with history
					String reqByOrgName = null;
					String reqToOrgName = null;
					String reqByType = null;
					String reqToType = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String reqDetailsQuery = "select a.requester_type,a.requested_to_type,a.request_id,a.request_date,(select name from st_lms_organization_master where organization_id=a.requested_by_org_id) as reqByName,(select name from st_lms_organization_master where organization_id=a.requested_to_org_id) as reqToName from st_se_pwt_approval_request_master a where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.req_status='REQUESTED'";

					System.out.println("query for get org name "
							+ reqDetailsQuery);
					resultSet4 = statement4.executeQuery(reqDetailsQuery);
					while (resultSet4.next()) {
						reqByOrgName = resultSet4.getString("reqByName");
						reqToOrgName = resultSet4.getString("reqToName");
						reqByType = resultSet4.getString("requester_type");
						reqToType = resultSet4.getString("requested_to_type");
						receiptNumber = resultSet4.getString("request_id");
						receiptTime = resultSet4.getTimestamp("request_date");
					}

					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("This VIRN is requested by " + reqByType
							+ ": " + reqByOrgName + "To " + reqToType + ": "
							+ reqToOrgName + " for Approval with Voucher id: "
							+ receiptNumber + " on " + receiptTime);
					pwtBean.setMessageCode("TBD");

				} else if (prizeStatus.equalsIgnoreCase("PND_MAS")) {
					// details of by and to and date and voucher
					String reqByOrgName = null;
					String reqToOrgName = null;
					String reqByType = null;
					String reqToType = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement4 = connection.createStatement();
					String reqDetailsQuery = "select a.requester_type,a.requested_to_type,a.request_id,a.request_date,(select name from st_lms_organization_master where organization_id=a.requested_by_org_id) as reqByName,(select name from st_lms_organization_master where organization_id=a.requested_to_org_id) as reqToName from st_se_pwt_approval_request_master a where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.req_status='PND_MAS'";

					System.out.println("query for get org name "
							+ reqDetailsQuery);
					resultSet4 = statement4.executeQuery(reqDetailsQuery);
					while (resultSet4.next()) {
						reqByOrgName = resultSet4.getString("reqByName");
						reqToOrgName = resultSet4.getString("reqToName");
						reqByType = resultSet4.getString("requester_type");
						reqToType = resultSet4.getString("requested_to_type");
						receiptNumber = resultSet4.getString("request_id");
						receiptTime = resultSet4.getTimestamp("request_date");
					}

					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("This VIRN is requested by " + reqByType
							+ ": " + reqByOrgName + "To " + reqToType + ": "
							+ reqToOrgName
							+ " for master Approval with Voucher id: "
							+ receiptNumber + " on " + receiptTime);
					pwtBean.setMessageCode("TBD");

				} else if (prizeStatus.equalsIgnoreCase("PND_PAY")) {
					// show details for payment by and to
					String reqToOrgName = null;
					String reqToType = null;
					String receiptNumber = null;
					statement4 = connection.createStatement();
					String reqDetailsQuery = "select a.request_id,a.pay_req_for_org_type,(select name from st_lms_organization_master where organization_id=a.pay_request_for_org_id) as payByName from st_se_pwt_approval_request_master a where a.virn_code='"
							+ encodedVirnCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.req_status='PND_PAY'";

					System.out.println("query for get org name "
							+ reqDetailsQuery);
					resultSet4 = statement4.executeQuery(reqDetailsQuery);
					while (resultSet4.next()) {

						reqToOrgName = resultSet4.getString("payByName");
						reqToType = resultSet4
								.getString("pay_req_for_org_type");
						receiptNumber = resultSet4.getString("request_id");
					}

					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("This VIRN is requested To " + reqToType
							+ ": " + reqToOrgName
							+ "for payment with Voucher id: " + receiptNumber);
					pwtBean.setMessageCode("112009");

				} else if (prizeStatus.equalsIgnoreCase("NO_PRIZE_PWT")) {
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("Valid Virn");
					pwtBean.setMessage("No Prize");
					pwtBean.setMessageCode("");

				} else if (prizeStatus.equalsIgnoreCase("CANCELLED_PERMANENT")) {
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean
							.setMessage("Tampered/Damaged/Defaced VIRN as noted at BO");
					pwtBean.setMessageCode("112009");

				} else {
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid Virn");
					pwtBean.setMessage("UNDEFINED STATUS OF PWT:: "
							+ prizeStatus);
					pwtBean.setMessageCode("TBD");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

		return pwtBean;

	}

}