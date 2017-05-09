package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetailerPwtProcessHelper {

	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet result;

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
			String ticketNbrDigit, int gameId, Connection connection)
			throws SQLException {
		TicketBean bean = new TicketBean();
		PreparedStatement pstmt2 = null;
		ResultSet resultSet2 = null;
		bean.setTicketNumber(bookNbr + "-" + ticketNbrDigit);
		System.out.println("Book No is: " + bookNbr	+ "  actual ticket number is " + ticketNbrDigit);

		String query1 = QueryManager.getST4CurrentOwnerDetailsUsingGameBookNbr();
		pstmt = connection.prepareStatement(query1);
		pstmt.setInt(1, gameId);
		pstmt.setString(2, bookNbr);
		result = pstmt.executeQuery();
		System.out.println("pstmt == " + pstmt);
		if (result.next()) {
			String ownerType = result.getString("current_owner");
			String bookStatus = result.getString("book_status");
			if (("RETAILER".equalsIgnoreCase(ownerType.trim()) || "AGENT".equalsIgnoreCase(ownerType.trim()))
					&& ("ACTIVE".equalsIgnoreCase(bookStatus) || "CLAIMED"
							.equalsIgnoreCase(bookStatus))) {
				bean.setBookStatus(bookStatus);
				
//				pstmt2 = connection.prepareStatement(QueryManager
//						.getST4PwtTicketDetailsFromPwtInvUsingGameNbr());
//				pstmt2.setInt(1, Integer.parseInt(gameNbr));
//				pstmt2.setString(2, MD5Encoder.encode(bookNbr + "-" + ticketNbrDigit));
//				resultSet2 = pstmt2.executeQuery();
//				if(!resultSet2.next()) {
//					bean.setValid(false);
//					bean.setStatus("Ticket is Not ACTIVE or SOLD");
//					bean.setMessageCode("222020");
//					bean.setValidity("InValid Ticket");
//					return bean;
//				}
				
				// check the status of ticket
				String query2 = QueryManager.getST4PwtTicketDetailsUsingGameNbr();
				pstmt2 = connection.prepareStatement(query2);
				pstmt2.setInt(1, Integer.parseInt(gameNbr));
				pstmt2.setString(2, bookNbr + "-" + ticketNbrDigit);
				resultSet2 = pstmt2.executeQuery();
				System.out.println("inside if = " + pstmt2);
				// required to be reviewed
				if (resultSet2.next()) {
					String ticketStatus = resultSet2.getString("status");

					// -----------------------------------------------

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
						System.out.println("Ticket's PWT Has Bean Paid By Agent.");
					} else if (ticketStatus.equalsIgnoreCase("RETURN")) {
						bean.setValid(false);
						bean.setStatus("Ticket once Verified and Return to Player for Verification as Direct Player PWT at BO");
						bean.setMessageCode("222007");
						bean.setValidity("InValid Ticket");
						System.out
								.println("Ticket Is valid for pwt. But Ticket Is High Prize Ticket, So Go for Direct PWT.  ");
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
						System.out.println("Ticket's PWT Has Bean Paid By BO. as Auto Scrap");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent AS Bulk by bO");
						bean.setMessageCode("222005");
						bean.setValidity("InValid Ticket");
						System.out.println("Ticket's PWT Has Bean Paid By BO.");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_BO")) {
						bean.setValid(false);
						bean.setStatus("Already paid as Direct Player PWT By BO");
						bean.setMessageCode("222006");
						bean.setValidity("InValid Ticket");
						System.out.println("Already paid as Direct Player PWT By BO ");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
						bean.setValid(false);
						bean.setStatus("Already paid To Player By Retailer");
						bean.setMessageCode("222006");
						bean.setValidity("InValid Ticket");

					} else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
						bean.setValid(false);
						bean.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as auto scrap. ");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
						System.out.println("Ticket IS valid for pwt. (Ticket has been paid to player by retailer so retailer should be paid by agent)");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
						bean.setValid(false);
						bean.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as auto scrap. ");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
						System.out.println("Ticket IS valid for pwt. (Ticket has been paid to player by retailer so retailer should be paid by agent)");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
						bean.setValid(false);
						bean.setStatus("Paid to Player by Retailer and pending to claim by Retailer at Agent as Ticket Verification. ");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
						bean.setValid(false);
						bean.setStatus("Paid to Player by Agent and pending to claim at BO by Agent.");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
						bean.setValid(false);
						bean.setStatus("Already paid to Agent AS Bulk by BO");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
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

					else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
						bean.setValid(false);
						bean.setStatus("Paid to Retailer as Claimable by auto scrap");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
						bean.setValid(false);
						bean.setStatus("Paid to Retailer pending to claim at bo as Ticket Verification.");
						bean.setMessageCode("221002");
						bean.setValidity("InValid Ticket");
					}

					else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
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

					// -----------------------------------------------

					/*
					 * if(ticketStatus.equalsIgnoreCase("CLAIM_RET")){
					 * bean.setValid(false); bean.setStatus("Already paid to
					 * Retailer"); bean.setMessageCode("222010");
					 * bean.setValidity("InValid Ticket");
					 * System.out.println("Ticket's PWT Has Bean Paid By
					 * Agent."); } else
					 * if(ticketStatus.equalsIgnoreCase("RETURN") ){
					 * bean.setValid(false); bean.setStatus("Ticket once
					 * Verified and Return to Player for Verification as Direct
					 * Player PWT at BO"); bean.setMessageCode("222007");
					 * bean.setValidity("InValid Ticket");
					 * System.out.println("Ticket Is valid for pwt. But Ticket
					 * Is High Prize Ticket, So Go for Direct PWT. "); } else
					 * if(ticketStatus.equalsIgnoreCase("CLAIM_AGT")){
					 * bean.setValid(false); bean.setStatus("Already paid to
					 * Agent"); bean.setMessageCode("222005");
					 * bean.setValidity("InValid Ticket");
					 * System.out.println("Ticket's PWT Has Bean Paid By BO."); }
					 * else if(ticketStatus.equalsIgnoreCase("CLAIM_PLR")){
					 * bean.setValid(false); bean.setStatus("Already paid as
					 * Direct Player PWT"); bean.setMessageCode("222006");
					 * bean.setValidity("InValid Ticket");
					 * System.out.println("Ticket Is High Prize Ticket, Ticket's
					 * PWT Has Been Paid. "); } else
					 * if(ticketStatus.equalsIgnoreCase("CLAIM_PLR_RET")) {
					 * bean.setValid(false); bean.setStatus(" ");
					 * bean.setMessageCode("221002"); bean.setValidity("InValid
					 * Ticket"); System.out.println("Ticket IS valid for pwt.
					 * (Ticket has been paid to player by retailer so retailer
					 * should be paid by agent)"); } else {
					 * bean.setValid(false); bean.setStatus("Undefined Status of
					 * Ticket's PWT"); bean.setValidity("InValid Ticket");
					 * System.out.println("Undefined Status of Ticket's PWT"); }
					 */
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
					bean.setStatus("Ticket Is Valid");
					bean.setMessageCode("221001");
					bean.setUpdateTicketType("INSERT");
					bean.setValidity("Valid Ticket");
					System.out.println("Ticket IS valid for pwt. (Means not fake and not in pwt table)");
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
					System.out.println("Ticket status is not 'ACTIVE' or 'CLAIMED'");
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

	private String createQueryForApp(boolean inPayLimit) {
		StringBuilder insertAppQuery = new StringBuilder(
				"insert into  st_se_pwt_approval_request_master (party_type , "
						+ "request_id , party_id , ticket_nbr , virn_code , game_id , pwt_amt , tax_amt , net_amt , "
						+ "req_status , requester_type , requested_by_user_id , requested_by_org_id , requested_to_org_id ,"
						+ " requested_to_type , request_date ,  remarks ");
		if (inPayLimit) {
			insertAppQuery
					.append(", approved_by_type, approved_by_user_id , approved_by_org_id , "
							+ "pay_req_for_org_type, pay_request_for_org_id, approval_date  ");
		}
		insertAppQuery.append(" ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ");

		if (inPayLimit) {
			insertAppQuery.append(",?,?,?,?,?,? ");
		}
		insertAppQuery.append(")");
		return insertAppQuery.toString();
	}

	// fetch the game details for PWT on Retailer End
	public String fetchPwtGameDetails() throws LMSException {
		try {
			connection = DBConnect.getConnection();

			StringBuilder nbrFormat = new StringBuilder();
			Statement stmt1 = connection.createStatement();
			result = stmt1.executeQuery("select res.game_name,res.game_nbr,res.game_id,sgtnf.pack_nbr_digits,sgtnf.book_nbr_digits,sgtnf.ticket_nbr_digits,sgtnf.game_nbr_digits,sgtnf.game_virn_digits from st_se_game_ticket_nbr_format sgtnf,(select game_name,game_nbr,game_id from st_se_game_master where game_status='OPEN' OR game_status='SALE_CLOSE' OR game_status='SALE_HOLD') res where sgtnf.game_id = res.game_id");
			boolean flag = false;
			while (result.next()) {
				nbrFormat.append("Nx*" + result.getString("game_id") + ":");
				nbrFormat.append(result.getString("game_nbr") + ":");
				nbrFormat.append(result.getString("game_name") + ":");
				nbrFormat.append(result.getInt("pack_nbr_digits") + ":");
				nbrFormat.append(result.getInt("book_nbr_digits") + ":");
				nbrFormat.append(result.getInt("ticket_nbr_digits") + ":");
				nbrFormat.append(result.getInt("game_nbr_digits") + ":");
				nbrFormat.append(result.getInt("game_virn_digits"));
				flag = true;
			}
			System.out.println("result of pwt " + nbrFormat.toString());
			String nbrFormatStr = "";
			if (flag) {
				nbrFormatStr = nbrFormat.delete(0, 3).toString();
			}
			System.out.println("returned string = " + nbrFormatStr);
			return nbrFormatStr;
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
	public String fetchPwtGameDetailsNewWinning(String tktNbr, String virnNbr) throws LMSException {
	      	String gameNbr ="";
	    	String game_id="";
	        String game_name="";
	        String gameIdNbrName="";
		try {
			gameNbr = tktNbr.substring(0, Math.min(tktNbr.length(), 3));
			connection = DBConnect.getConnection();
			String query2 = "select game_name,game_id,pwt_end_date from st_se_game_master where game_nbr=?";
			pstmt = connection.prepareStatement(query2);
			pstmt.setInt(1, Integer.parseInt(gameNbr));
			result=pstmt.executeQuery();
			if(result.next()) {
				if(Util.getCurrentTimeStamp().after(result.getDate("pwt_end_date"))){
					throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.TICKET_EXPIRE_ERROR_MESSAGE);
				}
				game_id = result.getString(2);
				game_name = result.getString(1);
				gameIdNbrName = game_id+"-"+gameNbr+"-"+game_name;
			}
			else{
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GAME_NOT_AVAILABLE_ERROR_MESSAGE);
			}
			
		    }catch (SQLException e) {
		    	e.printStackTrace();
		    	throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		   }catch(LMSException e) {
				throw e;
			}catch (Exception e) {
				e.printStackTrace();
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
		return gameIdNbrName;
	}

	 
	private Map<String, Object> getOrganizationForApp(int orgId, double pwtAmt,
			Connection connection) throws SQLException, LMSException {

		Map<String, Object> map = new TreeMap<String, Object>();
		String getParentOrgId = "select parent_id, (select organization_type from st_lms_organization_master bb "
				+ " where  organization_id = aa.parent_id) 'organization_type' from (select parent_id,organization_type "
				+ " from st_lms_organization_master where organization_id = ?)aa";
		pstmt = connection.prepareStatement(getParentOrgId);
		pstmt.setInt(1, orgId);
		result = pstmt.executeQuery();
		System.out.println("getOrganizationForApp = " + pstmt);
		if (result.next()) {
			int parentOrgId = result.getInt("parent_id");
			String organizationType = result.getString("organization_type");
			// int parentUserId = result.getInt("user_id");
			// added by yogesh because this function is written in common
			// functions of pwt
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean limitBean = commonFunction
					.fetchPwtLimitsOfOrgnization(parentOrgId, connection);

			if (!"BO".equalsIgnoreCase(organizationType)
					&& pwtAmt > limitBean.getApprovalLimit()) {
				System.out
						.println("getOrganizationForApp is called again because PWT Amt is "
								+ " greator then parent ('"
								+ organizationType
								+ "') approval limit ");
				map = getOrganizationForApp(parentOrgId, pwtAmt, connection);
			} else {
				map.put("parentOrgId", parentOrgId);
				map.put("organizationType", organizationType);
				// map.put("parentUserId", parentUserId);
				map.put("limitBean", limitBean);
			}
			return map;

		} else {
			System.out.println("No Organization found");
			throw new LMSException("No Organization found");
		}

	}

	private Map insIntoPwtAppReqMasByRet(UserInfoBean userInfoBean,
			PwtBean pwtBean, TicketBean tktBean, String playerType,
			String playerId, PlayerBean plrBean, String recIdForApp)
			throws LMSException, SQLException {

		Map map = new TreeMap();

		// get the current organization pay limit
		CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
		OrgPwtLimitBean limitBean = commonFunction.fetchPwtLimitsOfOrgnization(
				userInfoBean.getUserOrgId(), connection);
		
		boolean isUserBO = "BO".equalsIgnoreCase(userInfoBean.getUserType());
		boolean isAppReq = isUserBO ? false : Double.parseDouble(pwtBean.getPwtAmount()) > limitBean
				.getApprovalLimit();
		boolean inPayLimit = isUserBO ? true : Double.parseDouble(pwtBean.getPwtAmount()) <= limitBean
				.getPayLimit();

		int reqToOrgId = userInfoBean.getUserOrgId();
		String reqToOrgType = userInfoBean.getUserType();

		if (isAppReq || pwtBean.getIsHighLevel() && !inPayLimit) {
			// get the organization details whose Approval is required
			Map<String, Object> orgForAppDetail = getOrganizationForApp(
					userInfoBean.getUserOrgId(), Double.parseDouble(pwtBean
							.getPwtAmount()), connection);
			reqToOrgId = (Integer) orgForAppDetail.get("parentOrgId");
			reqToOrgType = (String) orgForAppDetail.get("organizationType");
		}
		boolean isAutoApp = !(isAppReq || pwtBean.getIsHighLevel()
				&& !inPayLimit);
		System.out.println("Approval requested to orgId = " + reqToOrgId
				+ "  and user type = " + reqToOrgType);

		String status = isAutoApp ? "PND_PAY" : "REQUESTED";

		// insert into Approval table
		String insertAppQuery = createQueryForApp(isAutoApp);
		System.out.println("insertAppQuery = " + insertAppQuery);
		pstmt = connection.prepareStatement(insertAppQuery);
		pstmt.setString(1, playerType.toUpperCase());
		pstmt.setString(2, recIdForApp);

		boolean isPlr = "player".equalsIgnoreCase(playerType.trim());
		if (isPlr) {
			pstmt.setInt(3, Integer.parseInt(playerId));
		} else {
			pstmt.setObject(3, null);
		}

		pstmt.setString(4, tktBean.getTicketNumber());
		pstmt.setString(5, pwtBean.getEncVirnCode());
		pstmt.setInt(6, tktBean.getTicketGameId());
		pstmt.setDouble(7, Double.parseDouble(pwtBean.getPwtAmount()));
		pstmt.setDouble(8, 0.0);
		pstmt.setDouble(9, Double.parseDouble(pwtBean.getPwtAmount()));
		pstmt.setString(10, status);
		pstmt.setString(11, userInfoBean.getUserType());
		pstmt.setInt(12, userInfoBean.getUserId());
		pstmt.setInt(13, userInfoBean.getUserOrgId());
		pstmt.setInt(14, reqToOrgId);
		pstmt.setString(15, reqToOrgType);
		pstmt.setTimestamp(16, new java.sql.Timestamp(new java.util.Date()
				.getTime()));
		pstmt.setString(17, "request For PWT Approval");
		if (isAutoApp) {
			pstmt.setString(17, "Auto Approved. ");
			pstmt.setString(18, userInfoBean.getUserType());
			pstmt.setInt(19, userInfoBean.getUserId());
			pstmt.setInt(20, userInfoBean.getUserOrgId());
			pstmt.setString(21, userInfoBean.getUserType());
			pstmt.setInt(22, userInfoBean.getUserOrgId());
			pstmt.setTimestamp(23, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
		}
		System.out.println("insertAppQuery pppppp = " + pstmt);
		pstmt.executeUpdate();

		System.out.println("insertion into pwt temp table = " + pstmt);
		ResultSet rs = pstmt.getGeneratedKeys();
		Integer reqId = null;
		if (rs.next()) {
			reqId = rs.getInt(1);

			// update the ticket_inv_detail table

			if (playerId == null) {
				playerId = "0";
			}
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateTicketInvTable(tktBean.getTicketNumber(), tktBean
					.getBook_nbr(), tktBean.getGameNbr(), tktBean
					.getTicketGameId(), status, userInfoBean.getUserId(),
					userInfoBean.getUserOrgId(), tktBean.getUpdateTicketType(),
					Integer.parseInt(playerId), userInfoBean.getChannel(),
					userInfoBean.getInterfaceType(), connection);

			// update VIRN inventory status table
			commHelper.updateVirnStatus(tktBean.getGameNbr(), status, tktBean
					.getTicketGameId(), pwtBean.getEncVirnCode(), connection,MD5Encoder.encode(tktBean.getTicketNumber()));

			map.put("reqId", reqId);
			map.put("isAutoApp", isAutoApp);
			map.put("reqToOrgType", reqToOrgType);
			return map;
		} else {
			throw new LMSException(
					"NO Data Inserted in st_se_pwt_approval_request_master table");
		}

		// return reqId;

	}

	public Map plrRegistrationAndApproval(UserInfoBean userInfoBean,
			Map pwtDetails, String playerType, String playerId,
			PlayerBean plrBean, String root) throws LMSException {
		Map pwtAppMap = new TreeMap();
		TicketBean tktBean = (TicketBean) pwtDetails.get("tktBean");
		PwtBean pwtBean = (PwtBean) pwtDetails.get("pwtBean");

		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			// register player
			if (plrBean != null && "PLAYER".equalsIgnoreCase(playerType.trim())) {
				playerId = new PlayerVerifyHelperForApp().registerPlayer(
						plrBean, connection)
						+ "";
			}

			System.out.println("Player id is " + playerId
					+ " for that approval required");

			System.out.println("is Approval required = '" + pwtBean.getAppReq()
					+ "' , to  '" + userInfoBean.getUserType()
					+ "' of orgId = " + userInfoBean.getUserOrgId()
					+ " and user id = " + userInfoBean.getUserId());

			// generate TEMP receipt for Approval
			// String recIdForApp = "PWT12345678";
			// receipt generation done by yogesh
			String recIdForApp = GenerateRecieptNo.generateRequestId("REQUEST");
			if (recIdForApp == null || "".equals(recIdForApp)) {
				throw new LMSException("Request Id is not generated properly");
			}

			pwtAppMap = insIntoPwtAppReqMasByRet(userInfoBean, pwtBean,
					tktBean, playerType, playerId, plrBean, recIdForApp);

			connection.commit();

			int reqId = (Integer) pwtAppMap.get("reqId");
			GraphReportHelper graphReportHelper = new GraphReportHelper();
			graphReportHelper.createTextReportTempPlayerReceipt(reqId + "",
					"RETAILER", root, "SCRATCH_GAME");

			pwtAppMap.put("recId", recIdForApp);
			pwtAppMap.put("reqId", reqId);
			pwtAppMap.putAll(pwtDetails);

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

	/*
	 * // common method for all organization private OrgPwtLimitBean
	 * fetchPwtLimitsOfOrgnization(int userOrgId, Connection connection) throws
	 * SQLException { OrgPwtLimitBean bean = null; String query = "select
	 * aa.organization_id, verification_limit, approval_limit, pay_limit,
	 * scrap_limit, bb.pwt_scrap from st_lms_oranization_limits aa,
	 * st_lms_organization_master bb where aa.organization_id =
	 * bb.organization_id and aa.organization_id = ?"; pstmt =
	 * connection.prepareStatement(query); pstmt.setInt(1, userOrgId); result =
	 * pstmt.executeQuery(); System.out.println("query that fetch limit details =
	 * "+pstmt); if(result.next()) { bean = new OrgPwtLimitBean();
	 * bean.setOrganizationId(userOrgId);
	 * bean.setVerificationLimit(result.getDouble("verification_limit"));
	 * bean.setApprovalLimit(result.getDouble("approval_limit"));
	 * bean.setPayLimit(result.getDouble("pay_limit"));
	 * bean.setScrapLimit(result.getDouble("scrap_limit"));
	 * bean.setIsPwtAutoScrap(result.getString("pwt_scrap")); } return bean; }
	 */

	public int retDirectPlrPWTPaymentProcess(UserInfoBean userInfo,
			PwtBean bean, TicketBean tktBean, OrgPwtLimitBean limitBean,
			String playerId, String pendingReqId, Connection connection)
			throws SQLException, LMSException {
		PwtBean pwtBean = bean;
		int gameId = tktBean.getTicketGameId();
		double pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
		int transId = -1;

		if (pwtAmt < limitBean.getPayLimit()) {

			// insert data into main transaction master
			System.out.println("insert data into transaction master ");
			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = connection
					.prepareStatement(transMasQuery);
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				transId = rs.getInt(1);

				// insert into retailer transaction master
				String retTransMasterQuery = "insert into  st_lms_retailer_transaction_master ( transaction_id , retailer_user_id , retailer_org_id , transaction_date , transaction_type ) values (?,?,?,?,?)";
				System.out.println("retTransMasterQuery = "
						+ retTransMasterQuery);
				pstmt = connection.prepareStatement(retTransMasterQuery);
				pstmt.setInt(1, transId);
				pstmt.setInt(2, userInfo.getUserId());
				pstmt.setInt(3, userInfo.getUserOrgId());
				pstmt.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(5, "PWT_PLR_RET");
				pstmt.executeUpdate();
				System.out.println("insert into retailer transaction master = "
						+ pstmt);

				// insert into st_se_retailer_pwt when comes pwtAmt<Aproval
				// required
				String retPwtEntry = "insert into  Direct player ( retailer_user_id , retailer_org_id , virn , ticket_nbr ,"
						+ " game_id , transaction_id , pwt_amt , comm_amt , net_amt , status ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pstmt = connection.prepareStatement(retPwtEntry);
				pstmt.setInt(1, userInfo.getUserId());
				pstmt.setInt(2, userInfo.getUserOrgId());
				pstmt.setString(3, pwtBean.getEncVirnCode());
				pstmt.setString(4, tktBean.getTicketNumber());
				pstmt.setInt(5, gameId);
				pstmt.setInt(6, transId);
				pstmt.setDouble(7, pwtAmt);
				pstmt.setDouble(8, 0.0);
				pstmt.setDouble(9, pwtAmt);
				if ("YES".equalsIgnoreCase(limitBean.getIsPwtAutoScrap())
						&& pwtAmt <= limitBean.getScrapLimit()) {
					pstmt.setString(10, "CLAIM_BAL");
				} else {
					pstmt.setString(10, "UNCLAIM_BAL");
				}
				pstmt.executeUpdate();
				System.out.println("insert into st_se_retailer_pwt = " + pstmt);

				// update status of of requested id entries into
				// st_se_pwt_approval_request_master
				String updateAppTable = "update  st_se_pwt_approval_request_master  set req_status ='DONE', "
						+ "remarks ='Payment Done', payment_done_by_type =?, payment_done_by =? where  task_id = ?";
				pstmt = connection.prepareStatement(updateAppTable);
				pstmt.setString(1, userInfo.getUserType());
				pstmt.setInt(2, userInfo.getUserOrgId());
				pstmt.setInt(3, Integer.parseInt(pendingReqId));
				int i = pstmt.executeUpdate();
				System.out.println("total row updated = " + i
						+ " ,  requested id " + pendingReqId
						+ " status updated = " + pstmt);
				if (i < 1) {
					throw new LMSException(
							"st_se_pwt_approval_request_master table not updated.");
				}

				// update the remaining prize list
				GameUtilityHelper.updateNoOfPrizeRemNewZim(gameId, pwtAmt,
						"PWT_PLR_RET", pwtBean.getEncVirnCode(), connection,
						tktBean.getGameNbr(),MD5Encoder.encode(tktBean.getTicketNumber()));

				// update ticket inventory status table
				String updateTktInvQuery = "update st_se_pwt_inv_? set status = ? where game_id = ? and virn_code = ?";
				PreparedStatement invPstmt = connection
						.prepareStatement(updateTktInvQuery);
				invPstmt.setInt(1, tktBean.getGameNbr());
				invPstmt.setString(2, "PWT_PLR_RET");
				invPstmt.setInt(3, gameId);
				invPstmt.setString(4, pwtBean.getEncVirnCode());
				int row = invPstmt.executeUpdate();
				System.out.println("total row = " + row
						+ "   ,update ticket inventory status table = "
						+ invPstmt);
				if (row < 1) {
					throw new LMSException(
							"update ticket inventory status table not updated.");
				}

				// update the ticket_inv_detail table
				CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				commHelper.updateTicketInvTable(tktBean.getTicketNumber(),
						tktBean.getBook_nbr(), tktBean.getGameNbr(), gameId,
						"TKT_PLR_RET", userInfo.getUserId(), userInfo
								.getUserOrgId(), tktBean.getUpdateTicketType(),
						0, userInfo.getChannel(), userInfo.getInterfaceType(),
						connection);

				// update retailer UNCLAIM_BAL payment
				String updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal+?) where organization_id = ?";
				if ("YES".equalsIgnoreCase(limitBean.getIsPwtAutoScrap())
						&& pwtAmt <= limitBean.getScrapLimit()) {
					updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal+?) "
							+ " , organization_status = if((available_credit-claimable_bal)>0, 'ACTIVE', 'INACTIVE')"
							+ " where organization_id = ?";
					// updateRetBalQuery = "update st_lms_organization_master
					// set claimable_bal = (claimable_bal+?) where
					// organization_id = ?";
				}
				PreparedStatement updateRetBal = connection
						.prepareStatement(updateRetBalQuery);
				updateRetBal.setDouble(1, pwtAmt);
				updateRetBal.setInt(2, userInfo.getUserOrgId());
				row = updateRetBal.executeUpdate();
				System.out.println("updateRetBalQuery = " + updateRetBal);

				// receipt entries are required to be inserted into receipt
				// table

			} else {
				throw new LMSException(
						"no data insert into main transaction master");
			}

		} else { // PWT amount is Greater then Retailer pay limit
			pwtBean.setValid(false);
			pwtBean.setVerificationStatus("InValid Virn");
			pwtBean.setMessage("Undefined");
			pwtBean
					.setMessageCode("PWT amount is Greater then Retailer pay limit");
			System.out.println("PWT amount is Greater then Retailer pay limit");
		}

		return transId;

	}

	/**
	 * retailer PWT payment process implementation
	 * 
	 * @param bean
	 * @param tktBean
	 * @param limitBean,
	 *            OrgPwtLimitBean which contain all the limit of login
	 *            organization
	 * @param highPrizeAmt,
	 * @param highPrizeCriteria,
	 * @param prizeLevel
	 * @param playerBean,
	 *            if comes from player registration process else NULL
	 * @return
	 * @throws SQLException
	 * @throws LMSException
	 */

	private String retEndPWTPaymentProcess(UserInfoBean userInfo, PwtBean bean,
			TicketBean tktBean, OrgPwtLimitBean limitBean, boolean isAnonymous,
			String pendingReqId, Connection connection,String entktNbr) throws SQLException,
			LMSException {
		PwtBean pwtBean = bean;
		int gameId = tktBean.getTicketGameId();
		double pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
		String recId = null;
		String pwtStatus = "";

		boolean isAutoScrap = "YES".equalsIgnoreCase(limitBean
				.getIsPwtAutoScrap())
				&& pwtAmt <= limitBean.getScrapLimit();

		if (pwtAmt <= limitBean.getPayLimit()) {
			System.out.println("mmmmmmm  " + gameId);
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();

			// insert data into main transaction master
			System.out.println("insert data into transaction master ");
			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = connection.prepareStatement(transMasQuery);
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int transId = rs.getInt(1);

				// insert into retailer transaction master
				String retTransMasterQuery = "insert into  st_lms_retailer_transaction_master ( transaction_id , retailer_user_id , retailer_org_id , transaction_date , transaction_type,game_id ) values (?,?,?,?,?,?)";
				System.out.println("retTransMasterQuery = "	+ retTransMasterQuery);
				pstmt = connection.prepareStatement(retTransMasterQuery);
				pstmt.setInt(1, transId);
				pstmt.setInt(2, userInfo.getUserId());
				pstmt.setInt(3, userInfo.getUserOrgId());
				pstmt.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				pstmt.setString(5, "PWT");
				pstmt.setInt(6, gameId);
				pstmt.executeUpdate();
				System.out.println("insert into retailer transaction master = "	+ pstmt);

				// insert into st_se_retailer_pwt when comes pwtAmt<Aproval
				// required
				String retPwtEntry = "insert into  st_se_retailer_pwt ( retailer_user_id , retailer_org_id , virn_code , ticket_nbr ,"
						+ " game_id , transaction_id , pwt_amt , claim_comm , agt_claim_comm, status ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pstmt = connection.prepareStatement(retPwtEntry);
				pstmt.setInt(1, userInfo.getUserId());
				pstmt.setInt(2, userInfo.getUserOrgId());
				pstmt.setString(3, pwtBean.getEncVirnCode());
				pstmt.setString(4, tktBean.getTicketNumber());
				pstmt.setInt(5, gameId);
				pstmt.setInt(6, transId);
				pstmt.setDouble(7, pwtAmt);
				// put retailer commission
				double retComm = commHelper.fetchCommOfOrganization(gameId,	userInfo.getUserOrgId(), "PWT", "RETAILER", connection);
				pstmt.setDouble(8, retComm);
				// put Agent commission
				double agtComm = commHelper.fetchCommOfOrganization(gameId,
						userInfo.getParentOrgId(), "PWT", "AGENT", connection);
				pstmt.setDouble(9, agtComm);
				System.out.println("---qurgfffqqqqqqqqqqqqqqqqqq--------"+ pstmt);
				pwtStatus = isAutoScrap ? "CLAIM_BAL" : "UNCLAIM_BAL";
				pstmt.setString(10, pwtStatus);

				pstmt.executeUpdate();
				System.out.println("insert into st_se_retailer_pwt = " + pstmt);

				if (pendingReqId != null && !isAnonymous) {
					// update status of of requested id entries into
					// st_se_pwt_approval_request_master
					String updateAppTable = "update  st_se_pwt_approval_request_master  set req_status ='DONE', "
							+ "remarks ='Payment Done', payment_done_by_type =?, payment_done_by =? where  task_id = ?";
					pstmt = connection.prepareStatement(updateAppTable);
					pstmt.setString(1, userInfo.getUserType());
					pstmt.setInt(2, userInfo.getUserOrgId());
					pstmt.setInt(3, Integer.parseInt(pendingReqId));
					int i = pstmt.executeUpdate();
					System.out.println("total row updated = " + i
							+ " ,  requested id " + pendingReqId
							+ " status updated = " + pstmt);
					if (i < 1) {
						throw new LMSException(
								"st_se_pwt_approval_request_master table not updated.");
					}
				}

				// update the remaining prize list
				pwtStatus = isAutoScrap ? "CLAIM_PLR_RET_CLM": "CLAIM_PLR_RET_UNCLM";
				GameUtilityHelper.updateNoOfPrizeRemNewZim(gameId, pwtAmt, pwtStatus,
						pwtBean.getEncVirnCode(), connection, tktBean
								.getGameNbr(),entktNbr);
				System.out.println("yogesh testing::  " + pwtBean.getVirnCode()	+ " :encodes virn : " + pwtBean.getEncVirnCode());

				// update VIRN inventory status table
				commHelper.updateVirnStatus(tktBean.getGameNbr(), pwtStatus,gameId, pwtBean.getEncVirnCode(), connection,entktNbr);

				// update the ticket_inv_detail table
				commHelper.updateTicketInvTable(tktBean.getTicketNumber(),
						tktBean.getBook_nbr(), tktBean.getGameNbr(), gameId,
						pwtStatus, userInfo.getUserId(), userInfo.getUserOrgId(), tktBean.getUpdateTicketType(),
						0, userInfo.getChannel(), userInfo.getInterfaceType(),
						connection);

				// update book status from active to claimed in table
				// st_se_game_inv_status table here
				if ("ACTIVE".equalsIgnoreCase(tktBean.getBookStatus())) {
					commHelper.updateBookStatus(gameId, tktBean.getBook_nbr(),
							connection, "CLAIMED");
				}
				commHelper.updatePwtDateAndTierTickets(tktBean,connection);

				// update retailer UNCLAIM_BAL payment
				if (isAutoScrap) {
					// commHelper.updateOrgBalance("CLAIM_BAL",
					// pwtAmt+(pwtAmt*.01*retComm),
					// userInfo.getUserOrgId(),"CREDIT", connection);
					// now retailer claimable balance DEBITED
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt + pwtAmt
							* .01 * retComm, "CLAIM_BAL", "DEBIT", userInfo.getUserOrgId(),
							userInfo.getParentOrgId(), "RETAILER", 0, connection);
					/*commHelper.updateOrgBalance("CLAIM_BAL", pwtAmt + pwtAmt
							* .01 * retComm, userInfo.getUserOrgId(), "DEBIT",
							connection);*/
					// agent claimable balance DEBITED
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt + pwtAmt
							* .01 * agtComm, "CLAIM_BAL", "DEBIT",userInfo.getParentOrgId(),
							0, "AGENT", 0, connection);
					
					/*commHelper.updateOrgBalance("CLAIM_BAL", pwtAmt + pwtAmt
							* .01 * agtComm, userInfo.getParentOrgId(),
							"DEBIT", connection);*/
				} else {
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt, "UNCLAIM_BAL", "CREDIT", userInfo.getUserOrgId(),
							userInfo.getParentOrgId(), "RETAILER", 0, connection);
					
					/*commHelper.updateOrgBalance("UNCLAIM_BAL", pwtAmt, userInfo
							.getUserOrgId(), "CREDIT", connection);*/
				}

				// receipt entries are required to be inserted into receipt
				// table

				return recId;

			} else {
				throw new LMSException(
						"no data insert into main transaction master");
			}

		} else {
			return null;
		}

	}
	public Map<String, Object> verifyTicketAndVirnNumber(String ticketNbr, String virnNbr, 
			int gameId, String gameNbr,UserInfoBean userInfoBean, String highPrizeCriteria, 
			String highPrizeAmount) throws LMSException{
		
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			Map<String, Object> detailMap = new TreeMap<String, Object>();
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			TicketBean tktBean = commonFunction.isTicketFormatValid(ticketNbr,gameId, connection);
			if (tktBean != null && !tktBean.getIsValid()) {
				detailMap.put("returnType", "input");
				tktBean.setTicketNumber(ticketNbr);
				detailMap.put("tktBean", tktBean);
				return detailMap;
			}
			
			String tktArr[] = tktBean.getTicketNumber().split("-");
			tktBean = checkTicketStatus(tktArr[0], tktArr[0] + "-" + tktArr[1],tktArr[2], gameId, connection);
			if (tktBean != null && !tktBean.getIsValid()) {
				detailMap.put("returnType", "input");
				tktBean.setTicketNumber(ticketNbr);
				detailMap.put("tktBean", tktBean);
				return detailMap;
			}
			
			tktBean.setTicketGameId(gameId);
			tktBean.setTicketNumber(tktArr[0] + "-" + tktArr[1] + "-"+ tktArr[2]);
			tktBean.setBook_nbr(tktArr[0] + "-" + tktArr[1]);
			tktBean.setGameNbr(Integer.parseInt(tktArr[0]));
			detailMap.put("tktBean", tktBean);
			PwtBean pwtBean = verifyPwtVirn(gameId, virnNbr, Integer
					.parseInt(gameNbr), connection, userInfoBean,
					highPrizeCriteria, highPrizeAmount, tktBean,true);
			if (pwtBean != null && pwtBean.getIsValid()) {
				if (pwtBean.getAppReq() || pwtBean.getIsHighLevel()) {
					detailMap.put("tktBean", tktBean);
					detailMap.put("pwtBean", pwtBean);
					detailMap.put("returnType", "registration");
					System.out.println("go to registration limit");
				} else { // changed ticket status
					System.out.println("change ticket status");
					detailMap.put("tktBean", tktBean);
					detailMap.put("pwtBean", pwtBean);
					detailMap.put("returnType", "success");
				}

				connection.commit();
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
	
	public Map<String, Object> verifyAndSaveTicketNVirn(String ticketNbr,
			String virnNbr, int gameId, String gameNbr,
			UserInfoBean userInfoBean, String highPrizeCriteria,
			String highPrizeAmt) throws LMSException {

		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			// validate the ticket
			Map<String, Object> detailMap = new TreeMap<String, Object>();

			// check ticket format
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			TicketBean tktBean = commonFunction.isTicketFormatValid(ticketNbr,gameId, connection);
			if (tktBean != null && !tktBean.getIsValid()) {
				detailMap.put("returnType", "input");
				tktBean.setTicketNumber(ticketNbr);
				detailMap.put("tktBean", tktBean);
				return detailMap;
			}
			if (!new RetailerPwtProcessHelper().canClaimRetailer(ticketNbr, userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId())) {
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
			tktBean = checkTicketStatus(tktArr[0], tktArr[0] + "-" + tktArr[1],tktArr[2], gameId, connection);
			if (tktBean != null && !tktBean.getIsValid()) {
				detailMap.put("returnType", "input");
				tktBean.setTicketNumber(ticketNbr);
				detailMap.put("tktBean", tktBean);
				return detailMap;
			}

			tktBean.setTicketGameId(gameId);
			tktBean.setTicketNumber(tktArr[0] + "-" + tktArr[1] + "-"+ tktArr[2]);
			tktBean.setBook_nbr(tktArr[0] + "-" + tktArr[1]);
			tktBean.setGameNbr(Integer.parseInt(tktArr[0]));
			detailMap.put("tktBean", tktBean);

			// validate VIRN nbr
			PwtBean pwtBean = verifyPwtVirn(gameId, virnNbr, Integer
					.parseInt(gameNbr), connection, userInfoBean,
					highPrizeCriteria, highPrizeAmt, tktBean,false);
			if (pwtBean != null && pwtBean.getIsValid()) {
				if (pwtBean.getAppReq() || pwtBean.getIsHighLevel()) {
					detailMap.put("tktBean", tktBean);
					detailMap.put("pwtBean", pwtBean);
					detailMap.put("returnType", "registration");
					System.out.println("go to registration limit");
				} else { // changed ticket status
					System.out.println("change ticket status");
					detailMap.put("tktBean", tktBean);
					detailMap.put("pwtBean", pwtBean);
					detailMap.put("returnType", "success");
				}

				connection.commit();
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
			Connection connection, UserInfoBean userInfoBean,
			String highPrizeCriteria, String highPrizeAmt, TicketBean tktBean,boolean ifOnlyVerification)
			throws LMSException {

		PwtBean pwtBean = new PwtBean();
		pwtBean.setVirnCode(virnCode);
		pwtBean.setMessageCode("212013");
		pwtBean.setMessage("Invalid VIRN or Can Not Verify");
		pwtBean.setVerificationStatus("InValid Virn");

		String encodedVirnCode = MD5Encoder.encode(virnCode);
		pwtBean.setEncVirnCode(encodedVirnCode);
		System.out.println("Encoded virn == " + encodedVirnCode);

		String encodedTktNo = MD5Encoder.encode(tktBean.getTicketNumber());
		pwtBean.setEnctktNumber(encodedTktNo);
		System.out.println("Encoded Ticket Number == " + encodedTktNo);
		try {
			Statement statement = connection.createStatement();
			StringBuffer query = new StringBuffer();
			query.append(QueryManager.getST1PWTBOCheckQuery()).append(" st_se_pwt_inv_" + gameNbr + " where ");
			query.append(" game_id = " + gameId).append(" and virn_code='");
			query.append(encodedVirnCode).append("'").append(" and id1='");
			if("YES".equals(Utility.getPropertyValue("IS_SCRATCH_NEW_FLOW_ENABLED"))){
				query.append(encodedTktNo).append("'").append(" and ticket_status in ('SOLD')");
			} else{
				query.append(encodedTktNo).append("'").append(" and ticket_status in ('SOLD', 'ACTIVE')");
			}
			
			System.out.println("GameId:" + gameId + "\nQuery:: " + query);

			ResultSet resultSet = statement.executeQuery(query.toString());
			System.out.println("ResultSet:" + resultSet + "---"	+ resultSet.getFetchSize());

			if (resultSet.next()) {
				String vCode = resultSet.getString(TableConstants.SPI_VIRN_CODE);
				String pwtAmount = resultSet.getString(TableConstants.SPI_PWT_AMT);
				String prizeLevel = resultSet.getString(TableConstants.SPI_PRIZE_LEVEL);
				String prizeStatus = resultSet.getString("status");
				System.out.println("Vcode : " + vCode + "\nPWT Amt : "+ pwtAmount + "\nPrize level : " + prizeLevel	+ "\nstatus : " + prizeStatus);
				
				if (MD5Encoder.encode(pwtBean.getVirnCode()).equals(vCode)) {
					System.out.println("Inside Valid VIRN Code");
					if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")|| prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
						// get the retailer PWT Limits
						// added by yogesh because this function is written in
						// common functions of pwt
						CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
						OrgPwtLimitBean orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(), connection);
						if (orgPwtLimit == null) { // send mail to backoffice
							throw new LMSException("PWT Limits Are Not defined Properly!!");
						} else { // test PWT amount with limit process
							pwtBean.setPwtAmount(pwtAmount);
							double pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
							if (pwtAmt <= orgPwtLimit.getVerificationLimit()) { // test
								// retailer
								// verification
								// limit
								System.out.println("inside verification limit == ");
								boolean isHighPrizeFlag = highPrizeCriteria.equals("level")
										&& prizeLevel.equals("HIGH")
										|| highPrizeCriteria.equals("amt")
										&& pwtAmt > Double.parseDouble(highPrizeAmt);
								// check for HIGH Prize Or is Approval Required
								if (pwtAmt > orgPwtLimit.getApprovalLimit()	|| isHighPrizeFlag) {
									System.out.println("inside high prize==");
									pwtBean.setValid(false);
									pwtBean.setVerificationStatus("Valid Virn");
									pwtBean.setMessage("High Prize Ticket, Can Not Be Claimed");
									pwtBean.setMessageCode("Undefined");
									pwtBean.setHighLevel(isHighPrizeFlag);
									pwtBean.setAppReq(pwtAmt > orgPwtLimit.getApprovalLimit());
									return pwtBean;
								} else if (pwtAmt <= orgPwtLimit.getPayLimit()) {// if
									// PWT
									// amount
									// is
									// in
									// retailer
									// payment
									// limit
									System.out.println("normal payment to player");
									pwtBean.setValid(true);
									pwtBean.setVerificationStatus("Valid Virn");
									pwtBean.setMessage("Credited To Concerned Party");
									if (!ifOnlyVerification) {
										String recId = retEndPWTPaymentProcess(
												userInfoBean, pwtBean, tktBean,
												orgPwtLimit, false, null,
												connection,encodedTktNo);
									}
									// insert into receipt create

									return pwtBean;
								} else {
									System.out.println("Out of Range of Retailer Pay Limit.");
									pwtBean.setValid(false);
									pwtBean.setVerificationStatus("InValid Virn");
									pwtBean.setMessage("Out of Range of Retailer Pay Limit.");
									pwtBean.setMessageCode("Undefined");
								}

							} else { // Out of Range of Retailer Verification
								// Limit.
								System.out.println("Out of Range of Retailer Verification Limit.");
								pwtBean.setValid(false);
								pwtBean.setVerificationStatus("InValid Virn");
								pwtBean.setMessage("Out of Range of Retailer Verification Limit.");
								pwtBean.setMessageCode("Undefined");
								return pwtBean;
							}

						}

					}

					// ------------------------------------------

					else if (prizeStatus.equalsIgnoreCase("NO_PRIZE_PWT")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("Valid Virn");
						pwtBean.setMessage("No Prize");
						pwtBean.setMessageCode("");

					} else if (prizeStatus.equalsIgnoreCase("MISSING")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("VIRN is from MISSING Status");
						pwtBean.setMessageCode("");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Paid to Player By Retailer");
						pwtBean.setMessageCode("Unknown");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("In Retailer Claimable Balance");
						pwtBean.setMessageCode("212001");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already paid as Direct Player PWT ");
						pwtBean.setMessageCode("212001");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_TEMP")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");
						pwtBean.setMessageCode("212002");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");
						pwtBean.setMessageCode("212003");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {

						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						pwtBean.setMessageCode("212004");
					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						pwtBean.setMessageCode("212005");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Paid to an agent ");
						pwtBean.setMessageCode("212006");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET")) {

						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Paid to retailer");
						pwtBean.setMessageCode("212007");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_TEMP")) {

						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already in Process for Direct Player PWT");
						pwtBean.setMessageCode("212008");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Paid To Player By REtailer");
						pwtBean.setMessageCode("212008");

					} else if (prizeStatus.equalsIgnoreCase("REQUESTED")) {
						// show request details from request table with history
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("This VIRN has Beean Already claimed and requested for Approval");
						pwtBean.setMessageCode("TBD");

					} else if (prizeStatus.equalsIgnoreCase("PND_MAS")) {
						// details of by and to and date and voucher
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("This VIRN has Beean requested for BO Master Approval");
						pwtBean.setMessageCode("112009");

					} else if (prizeStatus.equalsIgnoreCase("PND_PAY")) {
						// show details for payment by and to
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("This VIRN has Beean Approved And Pending to Payment");
						pwtBean.setMessageCode("112009");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Paid As Auto Scrap to Agent");
						pwtBean.setMessageCode("112003");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_BO")) {
						pwtBean.setValid(false);
						pwtBean.setMessage("Already Paid By BO as Direct Player PWT to Player");
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
						pwtBean.setMessage("Paid to Player By Agent In Claimable Balance");
						pwtBean.setMessageCode("TBD");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
						pwtBean.setMessageCode("112002");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_CLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Paid to retailer By Agent and pending to claim at bo by agent as uato sarap");
						pwtBean.setMessageCode("TBD");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Paid to retailer As Auto Scrap and pending to claim at bo as AUTO Scrap");
						pwtBean.setMessageCode("TBD");

					} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("VIRN Already Claimed To Retailer");
						pwtBean.setMessageCode("TBD");

					} else if (prizeStatus.equalsIgnoreCase("CANCELLED_PERMANENT")) {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("Tampered/Damaged/Defaced VIRN as noted at BO");
						pwtBean.setMessageCode("212010");

					} else {
						pwtBean.setValid(false);
						pwtBean.setVerificationStatus("InValid Virn");
						pwtBean.setMessage("UNDEFINED STATUS OF PWT:: "	+ prizeStatus);
						pwtBean.setMessageCode("TBD");
					}

					// ------------------------------------------

					/*
					 * else if(prizeStatus.equalsIgnoreCase("CLAIM_PLR")){
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already paid as Direct Player PWT ");
					 * pwtBean.setMessageCode("212001"); }else
					 * if(prizeStatus.equalsIgnoreCase("CLAIM_RET_TEMP")) {
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already Verified in Bulk Receipt at
					 * Agent, Fianl Payment Pending");
					 * pwtBean.setMessageCode("212002"); }else
					 * if(prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already Verified in Bulk Receipt at
					 * Agent, Fianl Payment Pending");
					 * pwtBean.setMessageCode("212003"); } else
					 * if(prizeStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")){
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already Verified in Bulk Receipt at
					 * BO, Fianl Payment Pending");
					 * pwtBean.setMessageCode("212004"); }else
					 * if(prizeStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")){
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already Verified in Bulk Receipt at
					 * BO, Fianl Payment Pending");
					 * pwtBean.setMessageCode("212005"); }else
					 * if(prizeStatus.equalsIgnoreCase("CLAIM_AGT")){
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already Paid to an agent ");
					 * pwtBean.setMessageCode("212006"); }else
					 * if(prizeStatus.equalsIgnoreCase("CLAIM_RET")){
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already Paid to retailer");
					 * pwtBean.setMessageCode("212007"); }else
					 * if(prizeStatus.equalsIgnoreCase("CLAIM_PLR_TEMP")){	`
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Already in Process for Direct Player
					 * PWT"); pwtBean.setMessageCode("212008"); }else
					 * if(prizeStatus.equalsIgnoreCase("CANCELLED_PERMANENT")){
					 * pwtBean.setValid(false);
					 * pwtBean.setVerificationStatus("InValid Virn");
					 * pwtBean.setMessage("Tampered/Damaged/Defaced VIRN as
					 * noted at BO"); pwtBean.setMessageCode("212010"); }
					 */

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

		return pwtBean;

	}
	public  int  getUserIdFromTicketNumber(String ticketNumber,Connection connection) throws LMSException{
		int retUserId = 0;
		try {
			String query2 = "select * from (select  ret_user_id, REPLACE(ticket_nbr, '-', '') as ticket_nbr  from st_se_ticket_by_ticket_sale_txn)aa  where ticket_nbr = ?;";
			pstmt = connection.prepareStatement(query2);
			pstmt.setString(1, ticketNumber.replace("-", ""));
			result=pstmt.executeQuery();
			if(result.next()) {
				retUserId = result.getInt("ret_user_id");
			}
			else{
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GAME_NOT_AVAILABLE_ERROR_MESSAGE);
			}
		
	    	}catch (Exception e) {
	    		e.printStackTrace();
	    		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	    	} finally {
	    		DBConnect.closeResource(pstmt,result);
	    	}
		return retUserId;
	}
	public  boolean canClaimRetailer(String ticketNumber, int retOrgId, int parentOrgId) {
		boolean canClaim = false;
		try {
			connection = DBConnect.getConnection();
			int userId = new RetailerPwtProcessHelper().getUserIdFromTicketNumber(ticketNumber, connection);
			canClaim = Util.canClaimRetailer(userId, retOrgId, parentOrgId, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return canClaim;
	}
	public  boolean canClaimAgent(String ticketNumber, int agtOrgId) {
		boolean canClaim = false;
		try {
			connection = DBConnect.getConnection();
			int userId = new RetailerPwtProcessHelper().getUserIdFromTicketNumber(ticketNumber, connection);
			canClaim = Util.canClaimAgent(userId, agtOrgId, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return canClaim;
	}
	public  boolean canClaimBO(String ticketNumber, int roleId) {
		boolean canClaim = false;
		try {
			connection = DBConnect.getConnection();
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			int userId = helper.getUserIdFromTicketNumber(ticketNumber, connection);
			canClaim = helper.canClaimBO(userId, roleId, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeResource(connection);
		}

		return canClaim;
	}
	public static boolean canClaimBO(int userId,int roleId, Connection connection) {
		int claimAtBo = 0;
		Statement stmt = null;
		ResultSet rs = null;
		boolean canClaim = false;
		try {
			stmt = connection.createStatement();
			String query = "select role_id from st_lms_role_agent_mapping where agent_id = (select b.parent_id  from st_lms_user_master a inner join st_lms_organization_master b on a.organization_id = b.organization_id where a.user_id = "+userId+")order by role_id desc limit 1;";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				claimAtBo = rs.getInt("role_id");
			}
			
			System.out.println(claimAtBo);
			canClaim = (claimAtBo == 1 || claimAtBo == roleId) ? true : false;
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return canClaim;
	}
	
	public  Map validateRequest(String gameId) {
		Map<String, Object> detailMap = new TreeMap<String, Object>();
		TicketBean bean = new TicketBean();
		bean.setValid(false);
		bean.setStatus("UNAUTHORIZED USER");
		bean.setMessageCode("221001");
		bean.setValidity("InValid Ticket");
		bean.setTicketNumber(gameId);
		detailMap.put("returnType", "input");
		bean.setTicketNumber(gameId);
		detailMap.put("tktBean", bean);
		return detailMap;
	}
}
