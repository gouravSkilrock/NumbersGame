package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.PlayerPWTBean;
import com.skilrock.lms.beans.PwtApproveRequestNPlrBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

public class AgentPwtProcessHelper {

	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet result;

	public String approvePendingPwts(int taskId, double pwtAmount,
			int requestedById, int agentorgId, int agtUserId, int gameNbr,
			String virnNbr, String ticketNbr, int gameId, int boOrgId,
			String channel, String interfaceType) throws LMSException {
		 
		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet payLimitDetails;
		connection = DBConnect.getConnection();
		double agtPayLimit = 0.0;
		double retPayLimit = 0.0;
		String status;
		String payReqForOrgType;
		int payReqForOrgId;
		String remarks;

		try {
			connection.setAutoCommit(false);
			String getPayLimits = "select organization_id,pay_limit from st_lms_oranization_limits where organization_id in(?,?)";
			pstmt = connection.prepareStatement(getPayLimits);
			pstmt.setInt(1, agentorgId);
			pstmt.setInt(2, requestedById);
			payLimitDetails = pstmt.executeQuery();
			while (payLimitDetails.next()) {
				if (payLimitDetails.getInt("organization_id") == agentorgId) {
					agtPayLimit = payLimitDetails.getDouble("pay_limit");
				}
				if (payLimitDetails.getInt("organization_id") == requestedById) {
					retPayLimit = payLimitDetails.getDouble("pay_limit");
				}
			}

			// check for pwt payments done by
			if (pwtAmount <= retPayLimit) { // go for retailer's payment
				status = "PND_PAY";
				payReqForOrgType = "RETAILER";
				payReqForOrgId = requestedById;
			} else if (pwtAmount <= agtPayLimit) { // go for agent's payment
				status = "PND_PAY";
				payReqForOrgType = "AGENT";
				payReqForOrgId = agentorgId;
			} else { // go for BO's pending pwt
				status = "PND_PAY";
				payReqForOrgType = "BO";
				payReqForOrgId = boOrgId; // Bo's organization id
			}

			// update the ticket_inv_detail table
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateTicketInvTable(ticketNbr, ticketNbr.split("-")[0]
					+ "-" + ticketNbr.split("-")[1], gameNbr, gameId, status,
					agtUserId, agentorgId, "UPDATE", requestedById, channel,
					interfaceType, connection);

			// update pwt inventory table
			boolean isPwtUpdate = commHelper.updateVirnStatus(gameNbr, status,
					gameId, virnNbr, connection,MD5Encoder.encode(ticketNbr));
			if (!isPwtUpdate) {
				throw new LMSException("Error ::: pwt table not updated ");
			}

			// update the request table for pwt payments
			remarks = "Payment should be done at " + payReqForOrgType;
			String updateRequestStatus = "update st_se_pwt_approval_request_master set req_status=?,approved_by_type=?,approved_by_user_id=?,approved_by_org_id=?,pay_req_for_org_type=?,pay_request_for_org_id=?,approval_date=?,remarks=? where task_id=?";
			pstmt = connection.prepareStatement(updateRequestStatus);
			pstmt.setString(1, status);
			pstmt.setString(2, "AGENT");
			pstmt.setInt(3, agtUserId);
			pstmt.setInt(4, agentorgId);
			pstmt.setString(5, payReqForOrgType);
			pstmt.setInt(6, payReqForOrgId);
			pstmt.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(8, remarks);
			pstmt.setInt(9, taskId);
			int updatedRow = pstmt.executeUpdate();
			if (updatedRow == 1) {
				System.out
						.println("Request status updated by Agent for Approval");
			} else {
				throw new LMSException(
						"Exception while sending request Approval By Agent");
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

					if (ticketStatus.equalsIgnoreCase("MISSING")) {
						bean.setValid(false);
						bean.setStatus("Ticket staus is MISSING in DATABASE");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out.println("Ticket is MISSING.");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_RET")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Retailer");
						bean.setMessageCode("222010");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By Agent.");
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
						bean.setStatus(" ");
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
						System.out.println("Ticket's PWT Has Bean Paid By BO.");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent as auto scrap.");
						bean.setMessageCode("222005");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket's PWT Has Bean Paid By BO. as Auto Scrap");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent AS Bulk by bO");
						bean.setMessageCode("222005");
						bean.setValidity("InValid Ticket");
						System.out.println("Ticket's PWT Has Bean Paid By BO.");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_BO")) {
						bean.setValid(false);
						bean
								.setStatus("Already paid as Direct Player PWT By BO");
						bean.setMessageCode("222006");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Already paid as Direct Player PWT By BO ");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")
							&& !isDirPlr) {
						bean.setValid(true);
						bean.setBook_nbr(bookNbr);
						bean.setGameNbr(Integer.parseInt(gameNbr));
						bean.setTicketGameId(gameId);
						bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
						bean.setStatus("");
						bean.setMessageCode("Undefined");
						bean.setUpdateTicketType("UPDATE");
						bean.setValidity("Valid Ticket");
						System.out
								.println("Ticket IS Not valid for pwt. (Claimed by retailer in Unclaimed "
										+ "Balanced)");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")
							&& isDirPlr) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as Ticket Verification.");
						bean.setMessageCode("222006");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Already paid as Direct Player PWT By BO ");

					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as auto scrap. ");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket IS valid for pwt. (Ticket has been paid to player by retailer so retailer should be paid by agent)");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as auto scrap. ");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket IS valid for pwt. (Ticket has been paid to player by retailer so retailer should be paid by agent)");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")
							&& !isDirPlr) {
						bean.setValid(true);
						bean.setBook_nbr(bookNbr);
						bean.setGameNbr(Integer.parseInt(gameNbr));
						bean.setTicketGameId(gameId);
						bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
						bean.setStatus("");
						bean.setMessageCode("Undefined");
						bean.setUpdateTicketType("UPDATE");
						bean.setValidity("Valid Ticket");
						System.out
								.println("Ticket IS Not valid for pwt. (Claimed by retailer To A Player in Unclaimed "
										+ "Balanced)");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")
							&& isDirPlr) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as Ticket Verification. ");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Player by Agent and pending to claim at BO by Agent.");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent AS Bulk by BO");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent AS Bulk by BO");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_CLM")) {
						bean.setValid(false);
						bean.setStatus("Paid to Retailer as Claimable");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Retailer as Claimable by auto scrap");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
						bean.setValid(false);
						bean
								.setStatus("Paid to Retailer pending to claim at bo as Ticket Verification.");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
						bean.setValid(false);
						bean.setStatus("Paid to player by Agent");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("PND_MAS")) {
						bean.setValid(false);
						bean.setStatus("Requested to BO Master for Approval");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					} else if (ticketStatus.equalsIgnoreCase("PND_PAY")) {
						bean.setValid(false);
						bean.setStatus("Pending for Payments");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					} else if (ticketStatus.equalsIgnoreCase("REQUESTED")) {
						bean.setValid(false);
						bean.setStatus("Requested for Approval");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

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
						bean.setStatus("Undefined Status of Ticket's PWT");
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
					bean.setStatus(" ");
					bean.setMessageCode("221001");
					bean.setUpdateTicketType("INSERT");
					bean.setValidity("Valid Ticket");
					System.out
							.println("Ticket IS valid for pwt. (Means not fake and not in pwt table)");
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
			String ticketNbr, String denyPwtStatus, int gameNbr,
			UserInfoBean userBean) throws LMSException {

		System.out.println("virn to deny " + virnCode);
		boolean statusChange = false;
		// Connection connection=null;
		 
		connection = DBConnect.getConnection();
		try {
			connection.setAutoCommit(false);
			CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
			// update direct playe temp table
			// 'CANCELLED_PERMANENT':'Permanent Cancellation',
			// 'UNCLM_CANCELLED':'Temporary Cancellation'
			String tempPwtStatus = "";
			String pwtStatus = "";
			int isPwtTicketDelete = 0;

			if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus.trim())) {
				tempPwtStatus = "CANCEL";
				pwtStatus = "CANCELLED_PERMANENT";
				commonHelper.updateTicketInvTable(ticketNbr, "", gameNbr,
						gameId, "CANCELLED_PERMANENT", userBean.getUserId(),
						userBean.getUserOrgId(), "UPDATE", 0, userBean
								.getChannel(), userBean.getInterfaceType(),
						connection);
			} else if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())) {
				tempPwtStatus = "CANCEL";
				pwtStatus = "UNCLM_CANCELLED";
				// deleted entry in case of Temporary Cancellation
				isPwtTicketDelete = commonHelper.updateTicketInvTable(
						ticketNbr, "", gameNbr, gameId, "", userBean
								.getUserId(), userBean.getUserOrgId(),
						"DELETE", 0, userBean.getChannel(), userBean
								.getInterfaceType(), connection);

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
			pstmt.setString(2, "AGENT");
			pstmt.setInt(3, userBean.getUserId());
			pstmt.setInt(4, userBean.getUserOrgId());
			pstmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(6, "Request Denied By Agent As "
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

			System.out.println("Temporary Cancellation"
					.equalsIgnoreCase(denyPwtStatus.trim())
					&& isUpdate == 1
					&& isPwtupdate == 1
					&& isPwtTicketDelete == 1);
			System.out.println("status denyPwtStatus :: "
					+ "Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
							.trim()) + isUpdate + isPwtupdate
					+ isPwtTicketDelete);
			if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus.trim())
					&& isUpdate == 1 && isPwtupdate == 1
					&& isPwtTicketDelete == 1) {
				statusChange = true;
				connection.commit();
			} else if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())
					&& isUpdate == 1 && isPwtupdate == 1) {
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

	public PwtApproveRequestNPlrBean getPendingPwtDetails(int taskId,
			String partyType) throws LMSException {
		 
		// Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet resultFromDb;
		connection = DBConnect.getConnection();
		try {

			String getPwtDetailsQuery = null;

			if ("RETAILER".equals(partyType)) {
				getPwtDetailsQuery = "select a.task_id,a.party_type,a.party_id,a.request_id,a.pwt_amt,a.tax_amt,a.net_amt,a.req_status,a.ticket_nbr,a.virn_code,a.remarks,a.game_id,d.game_name,d.game_nbr,c.name from st_se_pwt_approval_request_master a ,st_se_game_master d,st_lms_organization_master c  where 1=1 and d.game_id=a.game_id   and task_id=? and party_type='RETAILER' and c.organization_id=a.party_id";
			} else if ("PLAYER".equals(partyType)) {
				getPwtDetailsQuery = "select a.task_id,a.party_type,a.request_id,a.party_id,a.pwt_amt,a.tax_amt,a.net_amt,a.req_status,a.ticket_nbr,a.virn_code,a.remarks,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,b.player_id,b.bank_acc_nbr,b.bank_name,b.bank_branch,b.location_city,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a , st_lms_player_master b ,st_se_game_master d where 1=1 and d.game_id=a.game_id   and a.task_id=? and party_type='PLAYER' and a.party_id=b.player_id";
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
			PwtApproveRequestNPlrBean plePwtBean = new PwtApproveRequestNPlrBean();
			while (resultFromDb.next()) {
				// collect player info
				if (resultFromDb.getString("party_type").equals("PLAYER")) {
					// plrBean.setFirstName(resultFromDb.getString("first_name"));
					// plrBean.setLastName(resultFromDb.getString("last_name"));
					plePwtBean.setPartyName(resultFromDb
							.getString("first_name")
							+ " " + resultFromDb.getString("last_name"));
					plePwtBean
							.setPhone_nbr(resultFromDb.getString("phone_nbr"));
					plePwtBean.setCity(resultFromDb.getString("city"));
					plePwtBean.setBankActNbr(resultFromDb
							.getString("bank_acc_nbr"));
					plePwtBean.setBankBranch(resultFromDb
							.getString("bank_branch"));
					plePwtBean.setBankCity(resultFromDb
							.getString("location_city"));
					plePwtBean.setBankName(resultFromDb.getString("bank_name"));
					// plrBean.setPlrCity(resultFromDb.getString("city"));
					// plrBean.setPlrId(resultFromDb.getInt("player_id"));
				} else if (resultFromDb.getString("party_type").equals(
						"RETAILER")) {
					plePwtBean.setPartyName(resultFromDb.getString("name"));
				}

				// collect pwt info
				plePwtBean.setPartyType(resultFromDb.getString("party_type"));
				plePwtBean.setPartyId(resultFromDb.getInt("party_id"));
				plePwtBean.setPwt_amt(resultFromDb.getDouble("pwt_amt"));
				plePwtBean.setComm_amt(resultFromDb.getDouble("tax_amt"));
				plePwtBean.setNet_amt(resultFromDb.getDouble("net_amt"));
				plePwtBean.setTicket_nbr(resultFromDb.getString("ticket_nbr"));
				plePwtBean.setVirn_nbr(resultFromDb.getString("virn_code"));
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
			se.printStackTrace();
			throw new LMSException();
		}
		// return null;

	}

	/**
	 * This method is used to get unapproved pwts from request table
	 */
	public List<PwtApproveRequestNPlrBean> getRequestsPwtsToPay(
			String requestId, int requestedById, String firstName,
			String lastName, String status, int requestedToOrgId,
			String partyType) throws LMSException {

		 
		// Connection con=null;
		Statement stmtGetReqDetails = null;
		ResultSet reqDetails;
		connection = DBConnect.getConnection();

		StringBuilder getRequestDetailsQuery = null;

		if ("PLAYER".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.task_id,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id and a.party_type='PLAYER' ");
			if (firstName != null && !"".equals(firstName)) {
				getRequestDetailsQuery.append(" and b.first_name='" + firstName
						+ "'");
			}
			if (lastName != null && !"".equals(lastName)) {
				getRequestDetailsQuery.append(" and b.last_name='" + lastName
						+ "'");
			}
		}

		else if ("RETAILER".equals(partyType)) {
			getRequestDetailsQuery = new StringBuilder(
					"select a.task_id,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a ,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.party_id and d.game_id=a.game_id and a.party_type='RETAILER'");
			if (requestedById != 0) {
				getRequestDetailsQuery.append(" and a.requested_by_org_id="
						+ requestedToOrgId);
			}
		}

		getRequestDetailsQuery.append(" and a.pay_request_for_org_id="
				+ requestedToOrgId);

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
				} else if ("RETAILER".equals(partyType)) {
					pwtAppReqPlrBean.setPartyName(reqDetails.getString("name"));
				}
				pwtAppReqPlrBean.setPartyType(partyType);
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
	 * This method is used to get unapproved pwts from request table
	 */
	public List<PwtApproveRequestNPlrBean> getUnapprovePwts(String requestId,
			int requestedById, int approvedById, String firstName,
			String lastName, String status, int requestedToOrgId)
			throws LMSException {

		 
		// Connection con=null;
		Statement stmtGetReqDetails = null;
		ResultSet reqDetails;
		connection = DBConnect.getConnection();

		StringBuilder getRequestDetailsQuery = new StringBuilder(
				"select a.task_id,a.party_type,a.pwt_amt,a.requested_by_org_id,a.request_id,a.req_status,a.request_date,a.remarks,a.ticket_nbr,a.virn_code,a.game_id,b.first_name,b.last_name,b.city,b.phone_nbr,c.name,d.game_name,d.game_nbr from st_se_pwt_approval_request_master a left join st_lms_player_master b on a.party_id=b.player_id,st_lms_organization_master c,st_se_game_master d  where 1=1 and c.organization_id=a.requested_by_org_id and d.game_id=a.game_id  ");

		getRequestDetailsQuery.append(" and a.requested_to_org_id="
				+ requestedToOrgId);
		if (requestId != null && !"".equals(requestId)) {
			getRequestDetailsQuery.append(" and a.request_id='" + requestId
					+ "'");
		}
		if (requestedById > 0) {
			getRequestDetailsQuery.append(" and a.requested_by_org_id="
					+ requestedById);
		}
		if (approvedById != 0) {
			getRequestDetailsQuery.append(" and a.approved_by_org_id="
					+ approvedById);
		}
		if (status != null && !"".equals(status)) {
			getRequestDetailsQuery.append(" and a.req_status='" + status + "'");
		}
		if (firstName != null && !"".equals(firstName)) {
			getRequestDetailsQuery.append(" and b.first_name='" + firstName
					+ "'");
		}
		if (lastName != null && !"".equals(lastName)) {
			getRequestDetailsQuery
					.append(" and b.last_name='" + lastName + "'");
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
				if ("PLAYER".equals(reqDetails.getString("party_type"))) {
					pwtAppReqPlrBean.setPartyName(reqDetails
							.getString("first_name")
							+ " " + reqDetails.getString("last_name"));
					pwtAppReqPlrBean.setCity(reqDetails.getString("city"));
					pwtAppReqPlrBean.setPhone_nbr(reqDetails
							.getString("phone_nbr"));
				} else if ("ANONYMOUS".equals(reqDetails
						.getString("party_type"))) {
					pwtAppReqPlrBean.setPartyName("ANONYMOUS");
				}
				pwtAppReqPlrBean.setPwt_amt(reqDetails.getDouble("pwt_amt"));
				pwtReqDetailsList.add(pwtAppReqPlrBean);
			}
			return pwtReqDetailsList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql Exception", e);
		}

	}

	public String payPendingPwt(int taskId, double pwtAmount, double taxAmt,
			double netAmt, int partyId, String partyType, String ticketNbr,
			String virnNbr, int gameId, int payByOrgId, int payByUserId,
			String paymentType, String chqNbr, String draweeBank,
			String chequeDate, String issuiningParty, int gameNbr,
			String payByOrgName, String rootPath) throws LMSException {

		 
		// Connection con=null;
		// PreparedStatement pstmt=null;
		// ResultSet resultFromDb;
		connection = DBConnect.getConnection();
		String agtReceipt = null;
		// int agtTransId=0;
		double partyPwtCommRate = 0.0;
		double agtPwtCommRate = 0.0;
		try {
			connection.setAutoCommit(false);
			// this field will be removed from st agent pwt table so no need to
			// pass this variable for time being put it as zero instead of
			// fetching userid from database
			int partyUserId = 0;
			List<PwtBean> pwtList = new ArrayList<PwtBean>();
			PwtBean pwtBean = new PwtBean();
			pwtBean.setPwtAmount(String.valueOf(pwtAmount));
			pwtBean.setEncVirnCode(virnNbr);
			pwtBean.setValid(true);
			pwtList.add(pwtBean);

			PlayerPWTBean requestDetailsBean = new PlayerPWTBean();
			requestDetailsBean.setTax(taxAmt);
			requestDetailsBean.setNetAmt(netAmt);
			requestDetailsBean.setVirnCode(virnNbr);
			requestDetailsBean.setTicketNbr(ticketNbr);
			requestDetailsBean.setTaskId(taskId);
			requestDetailsBean.setPaymentType(paymentType);
			requestDetailsBean.setChequeNbr(chqNbr);
			requestDetailsBean.setChequeDate(chequeDate);
			requestDetailsBean.setIssuingPartyName(issuiningParty);
			requestDetailsBean.setDraweeBank(draweeBank);

			CommonFunctionsHelper common = new CommonFunctionsHelper();

			if ("RETAILER".equals(partyType)) {
				partyPwtCommRate = common.fetchCommOfOrganization(gameId,
						partyId, "PWT", partyType, connection);
			}

			agtPwtCommRate = common.fetchCommOfOrganization(gameId, payByOrgId,
					"PWT", "AGENT", connection);

			OrgPwtLimitBean orgPwtLimit = common.fetchPwtLimitsOfOrgnization(
					payByOrgId, connection);

			long transactionId = common.agtEndPWTPaymentProcess(pwtList, gameId,
					payByUserId, payByOrgId, partyId, partyUserId,
					partyPwtCommRate, agtPwtCommRate, gameNbr, orgPwtLimit,
					connection, partyType, requestDetailsBean);
			List<Long> transIdList = new ArrayList<Long>();
			if (transactionId > 0) {
				transIdList.add(transactionId);
			}

			if (transIdList.size() > 0) {
				PwtAgentHelper agtHepper = new PwtAgentHelper();
				agtReceipt = agtHepper.generateReciptForPwt(transIdList,
						connection, payByOrgId, partyId, partyType,
						"SCRATCH_GAME");
				if (agtReceipt != null) {
					connection.commit();
				} else {
					throw new LMSException(
							"Error during generating receipts for PWT :: ");
				}
				// System.out.println("transIdList for agent size is " +
				// transIdList.size() + ":: receipt number " + agtReceiptId);
				if (agtReceipt != null) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					if ("PLAYER".equalsIgnoreCase(partyType)) {
						graphReportHelper.createTextReportPlayer(taskId,
								rootPath, "SCRATCH_GAME");
					} else if ("RETAILER".equals(partyType)) {
						graphReportHelper.createTextReportAgent(Integer
								.parseInt(agtReceipt.split("-")[0]), rootPath,
								payByOrgId, payByOrgName);
					}
				}
			}
			return agtReceipt.split("-")[1];

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	public Map plrRegistrationAndApproval(UserInfoBean userInfoBean,
			Map pwtDetails, String playerType, int playerId,
			PlayerBean plrBean, String rootPath) throws LMSException {
		Map pwtAppMap = new TreeMap();
		TicketBean tktBean = (TicketBean) pwtDetails.get("tktBean");
		PwtBean pwtBean = (PwtBean) pwtDetails.get("pwtBean");

		System.out.println("book number is ****** " + tktBean.getBook_nbr());
		try {
			connection = DBConnect.getConnection();
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
			// otherwise request would be sent to agent itself
			// get the current organization pay limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean limitBean = commonFunction
					.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),
							connection);
			boolean inPayLimit = Double.parseDouble(pwtBean.getPwtAmount()) <= limitBean
					.getPayLimit();

			int reqToOrgId = 0;
			String reqToOrgType = null;
			String remarks = null;
			String reqStatus = null;
			String approvedByType = null;
			int approvedByUserId = 0;
			int approvedByOrgId = 0;
			String payByType = null;
			int payByOrgId = 0;

			System.out.println("in agents pay limit " + inPayLimit);
			System.out.println("is approval " + pwtBean.getAppReq());
			if (pwtBean.getAppReq() || !inPayLimit) { // means approval from
				// BO is required
				// get Back office organization and user id
				reqToOrgId = userInfoBean.getParentOrgId();
				reqToOrgType = "BO";
				remarks = "requested to BO For Approval";
				reqStatus = "REQUESTED";

			} else if (inPayLimit) {
				reqToOrgId = userInfoBean.getUserOrgId();
				reqToOrgType = userInfoBean.getUserType();
				remarks = "Auto Approved By Agent";
				reqStatus = "PND_PAY";
				approvedByType = "AGENT";
				approvedByUserId = userInfoBean.getUserId();
				approvedByOrgId = reqToOrgId;
				payByType = userInfoBean.getUserType();
				payByOrgId = userInfoBean.getUserOrgId();
			}

			System.out.println("Approval requested to orgId = " + reqToOrgId
					+ "  and user type = " + reqToOrgType);

			// generate request Number
			String recIdForApp = GenerateRecieptNo.generateRequestId("REQUEST");
			if (recIdForApp == null || "".equals(recIdForApp)) {
				throw new LMSException("Request Id is not generated properly");
			}

			// insert into Approval table
			String insertAppQuery = "insert into  st_se_pwt_approval_request_master (party_type ,request_id,party_id,ticket_nbr,virn_code,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,approval_date,request_date,pay_req_for_org_type,pay_request_for_org_id, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

			if (pwtBean.getAppReq() || !inPayLimit) {
				pstmt.setString(16, approvedByType);
				pstmt.setObject(17, null);
				pstmt.setObject(18, null);
				pstmt.setObject(19, null);
				pstmt.setObject(21, null);
				pstmt.setObject(22, null);
				;
			} else if (inPayLimit) {
				System.out.println("in agents pay limit");
				pstmt.setString(16, approvedByType);
				pstmt.setInt(17, approvedByUserId);
				pstmt.setInt(18, approvedByOrgId);
				pstmt.setTimestamp(19, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(21, payByType);
				pstmt.setInt(22, payByOrgId);
			} else {
				throw new LMSException(
						"Limits are not set properly or not followed strictly");
			}

			pstmt.setTimestamp(20, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			pstmt.setString(23, remarks);

			System.out.println("insertAppQuery pppppp = " + pstmt);
			pstmt.executeUpdate();

			System.out.println("insertion into pwt temp table = " + pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			int reqId = 0;
			if (rs.next()) {
				reqId = rs.getInt(1);
			} else {
				throw new LMSException(
						"NO Data Inserted in st_se_pwt_approval_request_master table");
			}
			System.out.println("book number " + tktBean.getBook_nbr());
			// update main ticket table
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
			commonFunction.updatePwtDateAndTierTickets(tktBean, connection);

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
			graphHelpwr.createTextReportTempPlayerReceipt(reqId + "", "AGENT",
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

	public Map<String, Object> verifyAndSaveTicketNVirn(String ticketNbr,
			String virnNbr, int gameId, String gameNbr, String gameName,
			UserInfoBean userInfoBean) throws LMSException {

		try {
			connection = DBConnect.getConnection();
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
			if (!new RetailerPwtProcessHelper().canClaimAgent(ticketNbr, userInfoBean.getUserOrgId())) {
				TicketBean bean = new TicketBean();
				bean.setValid(false);
				bean.setStatus("UNAUTHORIZED USER");
				bean.setMessageCode("221001");
				bean.setValidity("InValid Ticket");
				bean.setTicketNumber(ticketNbr);
				detailMap.put("returnType", "input");
				bean.setTicketNumber(ticketNbr);
				detailMap.put("tktBean", bean);
				return detailMap;
			}
			// tktArr[0]='game NBR', tktArr[1]='Book NBR', tktArr[2]='Ticket NBR
			String tktArr[] = tktBean.getTicketNumber().split("-");
			tktBean = checkTicketStatus(tktArr[0], tktArr[0] + "-" + tktArr[1],
					tktArr[2], gameId, connection, true);
			if (tktBean != null && !tktBean.getIsValid()) {
				detailMap.put("returnType", "input");
				tktBean.setTicketNumber(ticketNbr);
				tktBean.setGameNbr(Integer.parseInt(gameNbr));
				detailMap.put("tktBean", tktBean);
				return detailMap;
			}

			tktBean.setTicketGameId(gameId);
			tktBean.setTicketNumber(tktArr[0] + "-" + tktArr[1] + "-"
					+ tktArr[2]);
			tktBean.setGameNbr(Integer.parseInt(tktArr[0]));
			tktBean.setBook_nbr(tktArr[0] + "-" + tktArr[1]);
			detailMap.put("tktBean", tktBean);

			System.out.println("book number set &&&&&&&&&&&&&& "
					+ tktBean.getBook_nbr());
			// validate VIRN nbr
			PwtBean pwtBean = verifyPwtVirn(gameId, virnNbr, Integer
					.parseInt(gameNbr), connection, userInfoBean, tktBean);
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

	private PwtBean verifyPwtVirn(int gameId, String virnCode, int gameNbr,
			Connection connection, UserInfoBean userInfoBean, TicketBean tktBean)
			throws LMSException {

		PwtBean pwtBean = new PwtBean();
		pwtBean.setVirnCode(virnCode);
		pwtBean.setMessageCode("212013");
		pwtBean.setMessage("Invalid VIRN or Can Not Verify");
		pwtBean.setVerificationStatus("InValid Virn");

		String encodedVirnCode = MD5Encoder.encode(virnCode);
		String encodedTktNbr = MD5Encoder.encode(tktBean.getTicketNumber());
		pwtBean.setEncVirnCode(encodedVirnCode);
		System.out.println("Encoded virn == " + encodedVirnCode);

		try {
			Statement statement = connection.createStatement();

			StringBuffer query = new StringBuffer();
			query.append(QueryManager.getST1PWTBOCheckQuery()).append(
					" st_se_pwt_inv_" + gameNbr + " where ");
			query.append(" game_id = " + gameId).append(" and virn_code in ('");
			query.append(encodedVirnCode).append("') and id1='");
			if("YES".equals(Utility.getPropertyValue("IS_SCRATCH_NEW_FLOW_ENABLED"))){
				query.append( encodedTktNbr).append("'").append(" and ticket_status in ('SOLD')");
			} else{
				query.append(encodedTktNbr).append("'").append(" and ticket_status in ('SOLD', 'ACTIVE')");
			}
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

				if (MD5Encoder.encode(pwtBean.getVirnCode()).equals(vCode)) {

					if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
							|| prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
						// get the retailer PWT Limits
						CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
						OrgPwtLimitBean orgPwtLimit = commonFunction
								.fetchPwtLimitsOfOrgnization(userInfoBean
										.getUserOrgId(), connection);
						if (orgPwtLimit == null) {
							// send mail to backoffice
							throw new LMSException(
									"PWT Limits Are Not defined Properly!!");
						} else { // test PWT amount with limit process
							pwtBean.setPwtAmount(pwtAmount);
							double pwtAmt = Double.parseDouble(pwtBean
									.getPwtAmount());
							if (pwtAmt <= orgPwtLimit.getVerificationLimit()) { // test
								// retailer
								// verification
								// limit
								System.out
										.println("inside verification limit == ");
								// check for HIGH Prize Or is Approval Required
								// by yogesh-- here we need not to check high
								// prize criteria because, we have to pay if it
								// is in agents pay limit
								if (pwtAmt > orgPwtLimit.getApprovalLimit()) {
									System.out
											.println("inside out of approval limit== and out of agents approval limit so go for Bo's approval");
									pwtBean.setValid(true);
									pwtBean
											.setVerificationStatus("InValid Virn");
									pwtBean
											.setMessage("Out of Agents Approval Limit ans sent to bos approval");
									pwtBean.setMessageCode("Undefined");
									pwtBean.setAppReq(pwtAmt > orgPwtLimit
											.getApprovalLimit());
									return pwtBean;
								} else {// if PWT amount is in agents payment
									// limit
									double highPrize = Double.parseDouble(Utility.getPropertyValue("HIGH_PRIZE_AMT"));
									if(pwtAmt > highPrize) {
										pwtBean.setHighLevel(true);
									}
									System.out
											.println("inside agents approval limit and request will be made for agent itself");
									pwtBean.setValid(true);
									pwtBean.setVerificationStatus("Valid Virn");
									pwtBean
											.setMessage("Should pay to retailer after registration by agent");
									/*int transId = commonFunction.agtEndPWTPaymentProcess(new ArrayList<PwtBean>().add(pwtBean), gameId, userInfoBean.getUserId(), 
											userInfoBean.getUserOrgId(), partyId, partyUserId,
											partyPwtCommRate, agtComm, gameNbr, orgPwtLimit, connection, "AGENT", null);*/
									// int transId =
									// agtEndPWTPaymentProcessAsDirectPlr(userInfoBean,
									// pwtBean, tktBean, orgPwtLimit, false,
									// null, connection);
									return pwtBean;
								}
							} else { // Out of Range of Retailer Verification
								// Limit.
								System.out
										.println("Out of Range of agents Verification Limit.");
								pwtBean.setValid(false);
								pwtBean.setVerificationStatus("InValid Virn");
								pwtBean
										.setMessage("Out of Range of Agents Verification Limit.");
								pwtBean.setMessageCode("Undefined");
								return pwtBean;
							}
						}
					} else if (prizeStatus.equalsIgnoreCase("NO_PRIZE_PWT")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("Valid Virn");
						pwtBean.setMessage("No Prize");
						pwtBean.setMessageCode("");

					} else if (prizeStatus.equalsIgnoreCase("MISSING")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("VIRN is from MISSING Status");
						pwtBean.setMessageCode("");

					} else if (prizeStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Already claimed to Player By Retailer");
						pwtBean.setMessageCode("Unknown");
					} else if (prizeStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("In Retailer Claimable Balance");
						pwtBean.setMessageCode("212001");
					} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {

						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						pwtBean.setMessageCode("212004");
					} else if (prizeStatus
							.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						pwtBean.setMessageCode("212005");
					} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Paid to an agent ");
						pwtBean.setMessageCode("212006");
					} else if (prizeStatus.equalsIgnoreCase("REQUESTED")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("This VIRN has Beean Already claimed and requested for Approval");
						pwtBean.setMessageCode("TBD");
					} else if (prizeStatus.equalsIgnoreCase("PND_MAS")) {
						// details of by and to and date and voucher
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("This VIRN has Beean requested for BO Master Approval");
						pwtBean.setMessageCode("112009");
					} else if (prizeStatus.equalsIgnoreCase("PND_PAY")) {
						// show details for payment by and to
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("This VIRN has Beean Approved And Pending to Payment");
						pwtBean.setMessageCode("112009");
					} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Already Paid As Auto Scrap to Agent");
						pwtBean.setMessageCode("112003");
					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_BO")) {
						pwtBean.setValid(false);
						pwtBean
								.setMessage("Already Paid By BO as Direct Player PWT to Player");
						pwtBean.setMessageCode("112005");
						pwtBean.setVerificationStatus("InValid Virn");
					} else if (prizeStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Paid to Player By Agent");
						pwtBean.setMessageCode("TBD");
					} else if (prizeStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Paid to Player By Agent In Claimable Balance");
						pwtBean.setMessageCode("TBD");
					} else if (prizeStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						pwtBean.setMessageCode("112002");
					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_CLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Paid to retailer By Agent and pending to claim at bo by agent as uato sarap");
						pwtBean.setMessageCode("TBD");
					} else if (prizeStatus
							.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Paid to retailer As Auto Scrap and pending to claim at bo as AUTO Scrap");
						pwtBean.setMessageCode("TBD");
					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("VIRN Already Claimed To Retailer");
						pwtBean.setMessageCode("TBD");
					} else if (prizeStatus
							.equalsIgnoreCase("CANCELLED_PERMANENT")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean
								.setMessage("Tampered/Damaged/Defaced VIRN as noted at BO");
						pwtBean.setMessageCode("212010");
					} else {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("UNDEFINED STATUS OF PWT:: "
								+ prizeStatus);
						pwtBean.setMessageCode("TBD");
					}

					/*-----------------------------------------------------------
					else 
					   if(prizeStatus.equalsIgnoreCase("CLAIM_PLR")){
						   pwtBean.setValid(false);
						   pwtBean.setVerificationStatus("InValid Virn");
						   pwtBean.setMessage("Already paid as Direct Player PWT ");
						   pwtBean.setMessageCode("212001");
					}else 
					   if(prizeStatus.equalsIgnoreCase("CLAIM_RET_TEMP")) {
						   pwtBean.setValid(false);
						   pwtBean.setVerificationStatus("InValid Virn");
						   pwtBean.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");	  
						   pwtBean.setMessageCode("212002");
					}else 
					   if(prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
							pwtBean.setValid(false);
							pwtBean.setVerificationStatus("InValid Virn");
					         pwtBean.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");	  
					         pwtBean.setMessageCode("212003");		   
					}
					else 
						if(prizeStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")){
							pwtBean.setValid(false);
							pwtBean.setVerificationStatus("InValid Virn");
					         pwtBean.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
					         pwtBean.setMessageCode("212004");	                    
					}else 
						if(prizeStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")){
							pwtBean.setValid(false);
							pwtBean.setVerificationStatus("InValid Virn");
					         pwtBean.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
					         pwtBean.setMessageCode("212005");
					 }else 
					 	if(prizeStatus.equalsIgnoreCase("CLAIM_AGT")){
							pwtBean.setValid(false);
							pwtBean.setVerificationStatus("InValid Virn");
					         pwtBean.setMessage("Already Paid to an agent ");
					         pwtBean.setMessageCode("212006");
					 }else 
					 	if(prizeStatus.equalsIgnoreCase("CLAIM_RET")){
							pwtBean.setValid(false);
							pwtBean.setVerificationStatus("InValid Virn");
					         pwtBean.setMessage("Already Paid to retailer");
					         pwtBean.setMessageCode("212007");
					 }else 
					 	if(prizeStatus.equalsIgnoreCase("CLAIM_PLR_TEMP")){
							pwtBean.setValid(false);
							pwtBean.setVerificationStatus("InValid Virn");
					         pwtBean.setMessage("Already in Process for Direct Player PWT");
					         pwtBean.setMessageCode("212008");
					 }else 
					 	 if(prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET")){
								isVerified = true;
								pwtBean.setValid(true);
								pwtBean.setVerificationStatus("Valid Virn");
								pwtBean.setPwtAmount(pwtAmount);
								pwtBean.setMessage("This VIRN No. is Valid To Pay to retailer");
								pwtBean.setMessageCode("212002");
					  								
					}*/

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

		return pwtBean;

	}

}