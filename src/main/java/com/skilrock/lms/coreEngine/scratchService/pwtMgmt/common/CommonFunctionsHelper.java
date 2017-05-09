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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.beans.ClmDrawSaleBean;
import com.skilrock.lms.beans.ClmPwtBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PlayerPWTBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.exception.handler.LMSExceptionInterceptor;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class CommonFunctionsHelper {
	
	private PreparedStatement pstmt;
	private ResultSet result;

	// common method for all organization to get the organizations various
	// limits
	public OrgPwtLimitBean fetchPwtLimitsOfOrgnization(int organizationId,
			Connection connection) throws SQLException {
		OrgPwtLimitBean bean = null;
		String query = "select aa.organization_id, verification_limit, approval_limit, pay_limit, scrap_limit, bb.pwt_scrap from st_lms_oranization_limits aa, st_lms_organization_master bb where  aa.organization_id = bb.organization_id and  aa.organization_id = ?";
		pstmt = connection.prepareStatement(query);
		pstmt.setInt(1, organizationId);
		result = pstmt.executeQuery();
		System.out.println("query that fetch limit details = " + pstmt);
		if (result.next()) {
			bean = new OrgPwtLimitBean();
			bean.setOrganizationId(organizationId);
			bean.setVerificationLimit(result.getDouble("verification_limit"));
			bean.setApprovalLimit(result.getDouble("approval_limit"));
			bean.setPayLimit(result.getDouble("pay_limit"));
			bean.setScrapLimit(result.getDouble("scrap_limit"));
			bean.setIsPwtAutoScrap(result.getString("pwt_scrap"));
		}
		return bean;
	}

	public TicketBean isTicketFormatValid(String ticketNbr, int gameId,
			Connection connection) throws SQLException {
		TicketBean bean = new TicketBean();
		String getTicketFormatQuery = QueryManager
				.getST4GameTicketDetailsUsingGameId();
		pstmt = connection.prepareStatement(getTicketFormatQuery);
		pstmt.setInt(1, gameId);
		result = pstmt.executeQuery();
		System.out.println("getTicketFormatQuery = " + pstmt);
		if (result.next()) {
			int noOfTktPerBook = result.getInt("nbr_of_tickets_per_book");
			int tktNoDigit = result.getInt("ticket_nbr_digits");
			int gameNbrDigits = result.getInt("game_nbr_digits");
			int packNbrDigits = result.getInt("pack_nbr_digits");
			int bookNbrDigits = result.getInt("book_nbr_digits");

			if ((ticketNbr.indexOf("-") == -1)
					&& (ticketNbr.length() == (gameNbrDigits + packNbrDigits
							+ bookNbrDigits + tktNoDigit))) {
				ticketNbr = ticketNbr.substring(0, gameNbrDigits) + "-"
						+ ticketNbr.substring(gameNbrDigits);
				ticketNbr = ticketNbr.substring(0, gameNbrDigits
						+ packNbrDigits + bookNbrDigits + 1)
						+ "-"
						+ ticketNbr.substring(gameNbrDigits + packNbrDigits
								+ bookNbrDigits + 1);
			} else if (ticketNbr.split("-").length < 3) {
				bean.setValid(false);
				bean.setStatus("Number Format Error/Out of Range Please Check");
				bean.setMessageCode("221001");
				bean.setValidity("InValid Ticket");
				bean.setTicketNumber(ticketNbr);
				System.out
						.println("Ticket number is fake or not in valid format.");
				return bean;
			}

			String tktArr[] = ticketNbr.split("-");
			if (noOfTktPerBook < Integer.parseInt(tktArr[2])
					|| (Integer.parseInt(tktArr[2]) == 0)
					|| (tktNoDigit != tktArr[2].length())) {
				bean.setValid(false);
				bean.setStatus("Number Format Error/Out of Range Please Check");
				bean.setMessageCode("222001");
				bean.setValidity("InValid Ticket");
				bean.setTicketNumber(ticketNbr);
				System.out.println("Ticket number is fake or not in valid format.");
				return bean;
			}
			bean.setValid(true);
			bean.setTicketNumber(ticketNbr);
			// System.out.println("Ticket Format is Valid.");

		} else {
			bean.setValid(false);
			bean.setStatus("Game Not Found.");
			bean.setMessageCode("221001");
			bean.setValidity("InValid Ticket");
			bean.setTicketNumber(ticketNbr);
			System.out.println("Ticket IS Not valid for pwt. (Game Not found. Here query not return any result)");
		}
		result.close();
		pstmt.close();
		return bean;
	}
	
	public TicketBean isBookNbrFormatValid(String bookNbr,int gameNbr) throws SQLException {
		Connection con = null;
		con = DBConnect.getConnection();
		TicketBean bean = null;
		String getBookFormatQuery = "select  b.pack_nbr_digits, "
				+ " b.book_nbr_digits, b.game_nbr_digits, game_virn_digits, game_rank_digits, a.game_id  from st_se_game_master a, "
				+ " st_se_game_ticket_nbr_format b where a.game_nbr=? and a.game_id=b.game_id";
		pstmt = con.prepareStatement(getBookFormatQuery);
		pstmt.setInt(1, gameNbr);
		result = pstmt.executeQuery();
		if (result.next()) {
			int gameNbrDigits = result.getInt("game_nbr_digits");
			int packNbrDigits = result.getInt("pack_nbr_digits");
			int bookNbrDigits = result.getInt("book_nbr_digits");
			int gameId = result.getInt("game_id");
			bean = new TicketBean();
			if ((bookNbr.indexOf("-") == -1)
					&& (bookNbr.length() == (gameNbrDigits + packNbrDigits + bookNbrDigits))) {
				bookNbr = bookNbr.substring(0, gameNbrDigits) + "-"
						+ bookNbr.substring(gameNbrDigits);
				bean.setValid(true);
			} else if (bookNbr.split("-").length < 3) {
				bean.setValid(false);
				bean.setStatus("Number Format Error/Out of Range Please Check");
				bean.setMessageCode("221001");
				bean.setValidity("InValid Ticket");
				System.out.println("Ticket number is fake or not in valid format.");
			}
			bean.setTicketGameId(gameId);
			bean.setBook_nbr(bookNbr);
		} else {
			bean = new TicketBean();
			bean.setBook_nbr(bookNbr);
			bean.setValid(false);
			bean.setStatus("Game Not Found.");
			bean.setMessageCode("221001");
			bean.setValidity("InValid Book");
			System.out.println("Book Nbr IS Not valid. (Game Not found. Here query not return any result)");
		}
		result.close();
		pstmt.close();
		con.close();
		return bean;
	}

	public List<TicketBean> isTicketFormatValid(List<String> ticketNbrList,
			int gameNbr, Connection connection) throws SQLException {
		List<TicketBean> tktBeanList = new ArrayList<TicketBean>();
		TicketBean bean = new TicketBean();
		String getTicketFormatQuery = "select a.nbr_of_tickets_per_book, b.ticket_nbr_digits, b.pack_nbr_digits, "
				+ " b.book_nbr_digits, b.game_nbr_digits, game_virn_digits, game_rank_digits, a.game_id  from st_se_game_master a, "
				+ " st_se_game_ticket_nbr_format b where a.game_nbr=? and a.game_id=b.game_id";
		pstmt = connection.prepareStatement(getTicketFormatQuery);
		pstmt.setInt(1, gameNbr);
		result = pstmt.executeQuery();
		// System.out.println("getTicketFormatQuery = " + pstmt);
		if (result.next()) {
			int noOfTktPerBook = result.getInt("nbr_of_tickets_per_book");
			int tktNoDigit = result.getInt("ticket_nbr_digits");
			int gameNbrDigits = result.getInt("game_nbr_digits");
			int packNbrDigits = result.getInt("pack_nbr_digits");
			int bookNbrDigits = result.getInt("book_nbr_digits");
			int gameId = result.getInt("game_id");

			for (String ticketNbr : ticketNbrList) {
				bean = new TicketBean();
				if ((ticketNbr.indexOf("-") == -1)
						&& (ticketNbr.length() == (gameNbrDigits
								+ packNbrDigits + bookNbrDigits + tktNoDigit))) {
					ticketNbr = ticketNbr.substring(0, gameNbrDigits) + "-"
							+ ticketNbr.substring(gameNbrDigits);
					ticketNbr = ticketNbr.substring(0, gameNbrDigits
							+ packNbrDigits + bookNbrDigits + 1)
							+ "-"
							+ ticketNbr.substring(gameNbrDigits + packNbrDigits
									+ bookNbrDigits + 1);
				} else if (ticketNbr.split("-").length < 3) {
					bean.setValid(false);
					bean.setTicketGameId(gameId);
					bean.setTicketNumber(ticketNbr);// added by yogesh
					bean
							.setStatus("Number Format Error/Out of Range Please Check");
					bean.setMessageCode("221001");
					bean.setValidity("InValid Ticket");
					System.out
							.println("Ticket number is fake or not in valid format.");
					tktBeanList.add(bean);
					continue;
				}

				String tktArr[] = ticketNbr.split("-");
				if (noOfTktPerBook < Integer.parseInt(tktArr[2])
						|| (Integer.parseInt(tktArr[2]) == 0)
						|| (tktNoDigit != tktArr[2].length())) {
					bean.setValid(false);
					bean.setTicketGameId(gameId);
					bean.setTicketNumber(ticketNbr);// added by yogesh
					bean
							.setStatus("Number Format Error/Out of Range Please Check");
					bean.setMessageCode("222001");
					bean.setValidity("InValid Ticket");
					System.out
							.println("Ticket number is fake or not in valid format.");
					tktBeanList.add(bean);
					continue;
				}
				bean.setValid(true);
				bean.setTicketGameId(gameId);
				bean.setTicketNumber(ticketNbr);
				tktBeanList.add(bean);
				// System.out.println("Ticket Format is Valid.");
			}

		} else {
			for (String ticketNbr : ticketNbrList) {
				bean = new TicketBean();
				bean.setTicketNumber(ticketNbr);
				bean.setValid(false);
				bean.setStatus("Game Not Found.");
				bean.setMessageCode("221001");
				bean.setValidity("InValid Ticket");
				tktBeanList.add(bean);
				System.out
						.println("Ticket IS Not valid for pwt. (Game Not found. Here query not return any result)");
			}
		}
		result.close();
		pstmt.close();
		return tktBeanList;
	}

	public int updateTicketInvTable(String tktNo, String bookNo, int gameNbr,
			int gameId, String status, int userId, int userOrgId,
			String updateType, int partyOrgId, String channel,
			String interfaceType, Connection connection) throws SQLException,
			LMSException {

		int tktRow = 0;
		// updated by yogesh now not to execute select query

		if ("update".equalsIgnoreCase(updateType)) { // ticket already exist
			// in st_pwt_tickets_inv
			// table
			String updateTktInvQuery = "update st_se_pwt_tickets_inv_? set status = ?, verify_by_user=?, verify_by_org=?, channel = ?, interface = ?, party_org_id = ?, date = ?  where game_id = ? and ticket_nbr = ?";
			PreparedStatement upTktInvPstmt = connection
					.prepareStatement(updateTktInvQuery);
			upTktInvPstmt.setInt(1, gameNbr);
			upTktInvPstmt.setString(2, status);
			upTktInvPstmt.setInt(3, userId);
			upTktInvPstmt.setInt(4, userOrgId);
			upTktInvPstmt.setString(5, channel);
			upTktInvPstmt.setString(6, interfaceType);
			upTktInvPstmt.setInt(7, partyOrgId);
			upTktInvPstmt.setTimestamp(8, new Timestamp(new java.util.Date()
					.getTime()));
			upTktInvPstmt.setInt(9, gameId);
			upTktInvPstmt.setString(10, tktNo);
			tktRow = upTktInvPstmt.executeUpdate();
			System.out.println("total row = " + tktRow
					+ "   ,update ticket inventory status table = "
					+ upTktInvPstmt);
			if (tktRow < 1)
				throw new LMSException("ticket Number " + tktNo
						+ " status not updated in st_pwt_tickets_inv table.");
			if (tktRow > 1) {
				LMSExceptionInterceptor.sendMail("Ticket Number " + tktNo
						+ "is duplicate in DATABASE");
				throw new LMSException("Ticket Number " + tktNo
						+ "is duplicate in DATABASE");
			}
		} else if ("insert".equalsIgnoreCase(updateType)) { // if ticket not
			// exist in
			// st_pwt_tickets_inv
			// table
			String updateTktInvQuery = "insert into  st_se_pwt_tickets_inv_? ( ticket_nbr , game_id , book_nbr , status , verify_by_user , verify_by_org, channel, interface, party_org_id, date ) values (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement inTktInvPstmt = connection
					.prepareStatement(updateTktInvQuery);
			inTktInvPstmt.setInt(1, gameNbr);
			inTktInvPstmt.setString(2, tktNo);
			inTktInvPstmt.setInt(3, gameId);
			inTktInvPstmt.setString(4, bookNo);
			inTktInvPstmt.setString(5, status);
			inTktInvPstmt.setInt(6, userId);
			inTktInvPstmt.setInt(7, userOrgId);
			inTktInvPstmt.setString(8, channel);
			inTktInvPstmt.setString(9, interfaceType);
			inTktInvPstmt.setInt(10, partyOrgId);
			inTktInvPstmt.setTimestamp(11, new Timestamp(new java.util.Date()
					.getTime()));
			tktRow = inTktInvPstmt.executeUpdate();
			System.out.println("total row = " + tktRow
					+ "   ,insert ticket inventory details into table = "
					+ inTktInvPstmt);
			if (tktRow < 1)
				throw new LMSException("ticket Number " + tktNo
						+ " is not inserted in st_pwt_tickets_inv table.");
		} else if ("delete".equalsIgnoreCase(updateType)) { // in case of ticket
			// temporary
			// cancellation
			String updateTktInvQuery = "delete from st_se_pwt_tickets_inv_? where game_id=? and ticket_nbr=? ";
			PreparedStatement inTktInvPstmt = connection
					.prepareStatement(updateTktInvQuery);
			inTktInvPstmt.setInt(1, gameNbr);
			inTktInvPstmt.setInt(2, gameId);
			inTktInvPstmt.setString(3, tktNo);
			tktRow = inTktInvPstmt.executeUpdate();
			System.out.println("total row = " + tktRow
					+ "   ,delete form ticket inventory  table = "
					+ inTktInvPstmt);
			if (tktRow < 1)
				throw new LMSException("ticket Number " + tktNo
						+ " is not deleted from st_pwt_tickets_inv table.");
		} else {
			throw new LMSException(
					"Exception occured while updating ticket inventory table updation type is :: "
							+ updateType);
		}

		return tktRow;

	}

	public Boolean updateVirnStatus(int gameNbr, String pwtStatus, int gameId,
			String encVirnCode, Connection con,String entktNbr) throws SQLException,
			LMSException {
		String updateVirnInvQuery = "update st_se_pwt_inv_? set status = ? where game_id = ? and virn_code = ? and id1=?";
		PreparedStatement invPstmt = con.prepareStatement(updateVirnInvQuery);
		invPstmt.setInt(1, gameNbr);
		invPstmt.setString(2, pwtStatus);
		invPstmt.setInt(3, gameId);
		invPstmt.setString(4, encVirnCode);
		invPstmt.setString(5, entktNbr);
		int virnRow = invPstmt.executeUpdate();
		System.out.println("total row = " + virnRow
				+ "   ,update ticket inventory status table = " + invPstmt);
		if (virnRow < 1)
			throw new LMSException(
					"update ticket inventory status table not updated.");
		return (virnRow > 0);
	}

	public boolean updateOrgBalance(String claimType, double amount, int orgId,
			String amtUpdateType, Connection connection) throws SQLException {

		// check whether amount type is debit or credit
		amount = "DEBIT".equals(amtUpdateType) ? -1 * amount : amount;
		System.out.println("claimType " + claimType + " ::amtUpdateType "
				+ amtUpdateType + " amount = " + amount);
		String updateRetBalQuery = null;
		if ("UNCLAIM_BAL".equalsIgnoreCase(claimType))
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal+?) where organization_id = ?";
		else if ("CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal+?) "
					+ " , organization_status = if((available_credit-claimable_bal)>0, 'ACTIVE', 'INACTIVE')"
					+ " where organization_id = ?";
		} else if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		} else if ("ACA_N_UNCLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		}
		PreparedStatement updateRetBal = connection
				.prepareStatement(updateRetBalQuery);
		updateRetBal.setDouble(1, amount);
		if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBal.setDouble(2, amount);
			updateRetBal.setInt(3, orgId);
		} else {
			updateRetBal.setInt(2, orgId);
		}
		int retBalRow = updateRetBal.executeUpdate();
		System.out.println(retBalRow + " row updated,  updateRetBalQuery = "
				+ updateRetBal);

		// OrgCreditUpdation.

		return (retBalRow > 0);

	}

	public boolean updateBookStatus(int gameId, String bookNbr, Connection con,
			String status) throws SQLException {
		String updateBookStatus = "update st_se_game_inv_status set book_status = ? where game_id = ? and book_nbr=?";
		PreparedStatement updateBookStatusPstmt = con
				.prepareStatement(updateBookStatus);
		updateBookStatusPstmt.setString(1, status);
		updateBookStatusPstmt.setInt(2, gameId);
		updateBookStatusPstmt.setString(3, bookNbr);
		int retBalRow = updateBookStatusPstmt.executeUpdate();
		System.out.println(retBalRow + " row updated,  updateBookStatus = "
				+ updateBookStatusPstmt);
		return (retBalRow > 0);

	}

	public double fetchCommOfOrganization(int gameId, int orgId,
			String commType, String orgType, Connection con)
			throws SQLException {
		String fetCommAmount = "";
		if ("PWT".equalsIgnoreCase(commType)) {

			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.game_id, a.retailer_pwt_comm_rate, b.pwt_comm_variance , "
						+ "(ifnull(b.pwt_comm_variance, 0)+ a.retailer_pwt_comm_rate) 'total_comm' from "
						+ " (select game_id ,retailer_pwt_comm_rate from st_se_game_master where game_id = ?) a "
						+ " left join (select retailer_org_id, pwt_comm_variance, game_id from "
						+ " st_se_agent_retailer_sale_pwt_comm_variance where game_id = ? and  retailer_org_id = ?) b "
						+ "on a.game_id = b.game_id ";
				System.out.println("PWT Commision Variance.");
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select (a.agent_pwt_comm_rate+ifnull(b.pwt_comm_variance,0)) total_comm from("
						+ "select agent_pwt_comm_rate,game_id from st_se_game_master  where game_id=?)a left join"
						+ "(select pwt_comm_variance,game_id from st_se_bo_agent_sale_pwt_comm_variance "
						+ "where  game_id=? and agent_org_id=?) b on a.game_id=b.game_id";
				System.out.println("PWT Commision Variance.");
			} else {
				System.out
						.println("ERROR:: Org type is not Defined properly :: "
								+ orgType);
			}

		} else {
			System.out.println("SALE Commision Variance.");
		}
		PreparedStatement fetCommAmountPstmt = con
				.prepareStatement(fetCommAmount);
		fetCommAmountPstmt.setInt(1, gameId);
		fetCommAmountPstmt.setInt(2, gameId);
		fetCommAmountPstmt.setInt(3, orgId);
		ResultSet rs = fetCommAmountPstmt.executeQuery();
		double commAmt = 0.0;
		if (rs.next()) {
			commAmt = rs.getDouble("total_comm");
		}
		System.out.println(" commAmt = " + commAmt + " ,   fetCommAmount = "
				+ fetCommAmountPstmt);
		return commAmt;

	}

	public String verifyOrgForUnClaimedVirn(int orgId, String orgType,
			String enVirnCode, String virnStatus, int gameId,
			Connection connection) throws SQLException {
		String flag = "NONE";
		String fetchVirnDetailQuery = "";
		if ("RETAILER".equalsIgnoreCase(orgType)) {
			fetchVirnDetailQuery = "select virn_code,'RETAILER' as tableType from st_se_retailer_pwt where game_id = ? and virn_code = ? and retailer_org_id = ? and status = ? UNION select virn_code, 'PLAYER' as tableType from st_se_retailer_direct_player_pwt where game_id = ? and virn_code = ? and retailer_org_id = ? and pwt_claim_status = ?";
			// fetchVirnDetailQuery = "select * from st_se_retailer_pwt where
			// game_id = ? and virn_code = ? and retailer_org_id = ? and status
			// = ?";
		} else if ("AGENT".equalsIgnoreCase(orgType)) {

			fetchVirnDetailQuery = "select virn_code,'AGENT' as tableType from st_se_agent_pwt where game_id = ? and virn_code = ? and agent_org_id = ? and status = ? UNION select virn_code, 'PLAYER' as tableType from st_se_agt_direct_player_pwt where game_id = ? and virn_code = ? and agent_org_id = ? and pwt_claim_status=?";
			// fetchVirnDetailQuery = "select * from st_se_agent_pwt where
			// game_id = ? and virn_code = ? and agent_org_id = ? and status =
			// ?";
		}
		pstmt = connection.prepareStatement(fetchVirnDetailQuery);
		pstmt.setInt(1, gameId);
		pstmt.setString(2, enVirnCode);
		pstmt.setInt(3, orgId);
		pstmt.setString(4, virnStatus);
		pstmt.setInt(5, gameId);
		pstmt.setString(6, enVirnCode);
		pstmt.setInt(7, orgId);
		pstmt.setString(8, virnStatus);
		result = pstmt.executeQuery();
		System.out.println(" get detail of virn from " + orgType
				+ " pwt table pstmt = " + pstmt);
		if (result.next()) {
			if ("PLAYER".equalsIgnoreCase(result.getString("tableType")))
				flag = "IN_PLR_UNCLM";
			else
				flag = "IN_" + orgType + "_UNCLM";
		} else {
			// we require to shoot a mail
			// System.out.println("status not matched in "+orgType+" pwt table
			// and st_pwt_inv table for virn = "+enVirnCode);
			// CommonMethods.sendMail("status not matched in "+orgType+" pwt
			// table and st_pwt_inv table for virn = "+enVirnCode);
		}
		return flag;
	}

	public boolean updateOrgForUnClaimedVirn(int orgId, String orgType,
			String enVirnCode, String virnStatus, int gameId, String tableType,
			Connection connection,String tktNbr) throws SQLException, LMSException {
		boolean flag = false;
		//String fetchVirnDetailQuery = "";
		String updateVirnDetailQuery = "";
		if ("RETAILER".equalsIgnoreCase(orgType)) {

			// update according to the retailer

		} else if ("AGENT".equalsIgnoreCase(orgType)) {
			if ("AGENT".equalsIgnoreCase(tableType))
				updateVirnDetailQuery = "update st_se_agent_pwt set status=? where game_id = ? and virn_code = ? and agent_org_id = ? and ticket_nbr=? ";
			else if ("PLAYER".equalsIgnoreCase(tableType))
				updateVirnDetailQuery = "update st_se_agt_direct_player_pwt set pwt_claim_status=? where game_id = ? and virn_code = ? and agent_org_id = ? and ticket_nbr=? ";
			else
				throw new LMSException(
						"ERROR OCCURED while updating status in AGENT or PLR table for UNCLAIMMABLE balance  for this tableType is "
								+ tableType);
			// fetchVirnDetailQuery = "select * from st_se_agent_pwt where
			// game_id = ? and virn_code = ? and agent_org_id = ? and status =
			// ?";
			// fetchVirnDetailQuery = "select * from st_se_agent_pwt where
			// game_id = ? and virn_code = ? and agent_org_id = ? and status =
			// ?";
		}
		pstmt = connection.prepareStatement(updateVirnDetailQuery);
		pstmt.setString(1, virnStatus);
		pstmt.setInt(2, gameId);
		pstmt.setString(3, enVirnCode);
		pstmt.setInt(4, orgId);
		pstmt.setString(5, tktNbr);
		int isupdate = pstmt.executeUpdate();
		System.out.println(" update  virn for " + orgType
				+ " pwt table pstmt = " + pstmt);
		if (isupdate == 1) {
			flag = true;
		} else
			throw new LMSException(
					"ERROR OCCURED PWT NOT Found for this status " + virnStatus);
		return flag;
	}

	public Long agtEndPWTPaymentProcess(List<PwtBean> pwtList, int gameId,
			int agentUserId, int agtOrgId, int partyId, int partyUserId,
			double partyPwtCommRate, double agtPwtCommRate, int gameNbr,
			OrgPwtLimitBean orgPwtLimit, Connection connection,
			String partyType, PlayerPWTBean requestDetailsBean)
			throws SQLException, LMSException {

		PwtBean pwtBean;
		int pwtListSize = pwtList.size();
		double pwtAmt = 0.0;
		double commAmt = 0.0;
		double retNetAmt = 0.0;
		double retCreditAmt = 0.0;
		double totalClmBal = 0.0;
		double totalUnClmBal = 0.0;
		double retTotalUnclmBal = 0.0;
		long  transId = 0;
		boolean isClaimmable;

		// insert data into main transaction master
		System.out.println("insert data into transaction master " + "type  is"
				+ partyType);
		String transMasQuery = QueryManager.insertInLMSTransactionMaster();
		PreparedStatement pstmt = connection.prepareStatement(transMasQuery);
		pstmt.setString(1, "AGENT");
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		if (rs.next()) {
			transId = rs.getLong(1);

			// insert into agent transaction master
			// String retTransMasterQuery = "insert into
			// st_lms_retailer_transaction_master ( transaction_id ,
			// retailer_user_id , retailer_org_id , transaction_date ,
			// transaction_type ) values (?,?,?,?,?)";
			String agtTransMasterQuery = QueryManager
					.insertInAgentTransactionMaster();
			System.out.println("agtTransMasterQuery = " + agtTransMasterQuery);
			pstmt = connection.prepareStatement(agtTransMasterQuery);
			pstmt.setLong(1, transId);
			pstmt.setInt(2, agentUserId);
			pstmt.setInt(3, agtOrgId);

			if ("RETAILER".equals(partyType)) {
				pstmt.setString(4, "RETAILER");
				pstmt.setInt(5, partyId);
				pstmt.setString(6, "PWT");
			} else if ("PLAYER".equals(partyType)) {
				pstmt.setString(4, "PLAYER");
				pstmt.setInt(5, partyId);
				pstmt.setString(6, "PWT_PLR");
			}
			System.out.println("111111111111111111 ===== " + partyId);
			pstmt.setTimestamp(7, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			System.out.println("insert into agent transaction master = "
					+ pstmt);
			pstmt.executeUpdate();

			// insert into st_se_agent_pwt when comes pwtAmt is in payment and
			// approval limit
			String agtPwtEntry = "insert into  st_se_agent_pwt ( virn_code, transaction_id ,game_id,agent_user_id,retailer_user_id,retailer_org_id,pwt_amt,comm_amt,net_amt,agent_org_id,status,claim_comm,ticket_nbr ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
			pstmt = connection.prepareStatement(agtPwtEntry);

			// update st_se_retailer_pwt in case if retailer brings uncalimed
			// PWTs to agent
			String retPwtUpdateQuery = "update  st_se_retailer_pwt set status=? where game_id = ? and virn_code = ? and retailer_org_id = ? and ticket_nbr=?";
			PreparedStatement retPwtUpdatePstmt = connection
					.prepareStatement(retPwtUpdateQuery);

			// insert in st direct player table in case of player
			String insertAgtDirectPlr = "insert into st_se_agt_direct_player_pwt(task_id,agent_user_id,agent_org_id,ticket_nbr,virn_code,transaction_id,game_id,player_id,pwt_amt,tax_amt,net_amt,payment_type,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,pwt_claim_status,claim_comm,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement dirPlrPstmt = connection
					.prepareStatement(insertAgtDirectPlr);

			//String updateVirnInvQuery = "update st_se_pwt_inv_? set status = ? where game_id = ? and virn_code = ?";
			//PreparedStatement invPstmt = connection
			//		.prepareStatement(updateVirnInvQuery);

			for (int i = 0; i < pwtListSize; i++) {
				pwtBean = (PwtBean) pwtList.get(i);

				if (pwtBean.getIsValid()
						&& Double.parseDouble(pwtBean.getPwtAmount()) > orgPwtLimit
								.getPayLimit()) {
					pwtBean.setValid(false);
					pwtBean.setMessage("Pwt Amount is not agents Pay Limit");
					pwtBean.setVerificationStatus("Invalid");
				} else if (pwtBean.getIsValid()) {
					pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
					String encodedVirn = pwtBean.getEncVirnCode();
					String encodedTktNbr=null;
					// pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
					if ("YES".equalsIgnoreCase(orgPwtLimit.getIsPwtAutoScrap())
							&& pwtAmt <= orgPwtLimit.getScrapLimit())
						isClaimmable = true;
					else
						isClaimmable = false;

					if ("RETAILER".equals(partyType)) {
						encodedTktNbr=pwtBean.getTicketNumber();
						pstmt.setString(1, encodedVirn);
						pstmt.setLong(2, transId);
						pstmt.setInt(3, gameId);
						pstmt.setInt(4, agentUserId);
						pstmt.setInt(5, partyUserId);
						pstmt.setInt(6, partyId);

						// pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
						commAmt = pwtAmt * partyPwtCommRate * 0.01;
						retNetAmt = pwtAmt + commAmt;

						// for creditUpdation
						retCreditAmt += retNetAmt;

						if (!"NONE".equalsIgnoreCase(pwtBean.getInUnclmed())) {
							// update retailer pwt status for unclaimed pwt as
							// done
							retPwtUpdatePstmt.setString(1, "DONE_UNCLM");
							retPwtUpdatePstmt.setInt(2, gameId);
							retPwtUpdatePstmt.setString(3, encodedVirn);
							retPwtUpdatePstmt.setInt(4, partyId);
							retPwtUpdatePstmt.setString(5, encodedTktNbr);
							int row = retPwtUpdatePstmt.executeUpdate();
							if (row == 1)
								retTotalUnclmBal += pwtAmt;

						}
						pstmt.setDouble(7, pwtAmt);
						pstmt.setDouble(8, commAmt);
						pstmt.setDouble(9, retNetAmt);
						pstmt.setInt(10, agtOrgId);

						if (isClaimmable) {
							pstmt.setString(11, "CLAIM_BAL");
							totalClmBal += (pwtAmt + (pwtAmt * .01 * agtPwtCommRate));
						} else {
							pstmt.setString(11, "UNCLAIM_BAL");
							totalUnClmBal += pwtAmt;
						}
						pstmt.setDouble(12, agtPwtCommRate);
						pstmt.setString(13,encodedTktNbr);

						pstmt.executeUpdate();
					} else if ("PLAYER".equals(partyType)) {
						encodedTktNbr=requestDetailsBean.getTicketNbr();
						dirPlrPstmt.setInt(1, requestDetailsBean.getTaskId());
						dirPlrPstmt.setInt(2, agentUserId);
						dirPlrPstmt.setInt(3, agtOrgId);
						dirPlrPstmt.setString(4, requestDetailsBean
								.getTicketNbr());
						dirPlrPstmt.setString(5, requestDetailsBean
								.getVirnCode());
						dirPlrPstmt.setLong(6, transId);
						dirPlrPstmt.setInt(7, gameId);
						dirPlrPstmt.setInt(8, partyId);
						dirPlrPstmt.setDouble(9, pwtAmt);
						dirPlrPstmt.setDouble(10, requestDetailsBean.getTax());
						dirPlrPstmt.setDouble(11, requestDetailsBean
								.getNetAmt());
						dirPlrPstmt.setString(12, requestDetailsBean
								.getPaymentType());
						dirPlrPstmt.setTimestamp(13, new java.sql.Timestamp(
								new java.util.Date().getTime()));

						if ("cash".equalsIgnoreCase(requestDetailsBean
								.getPaymentType())
								|| "TPT".equalsIgnoreCase(requestDetailsBean
										.getPaymentType())) {
							dirPlrPstmt.setObject(13, null);
							dirPlrPstmt.setObject(14, null);
							dirPlrPstmt.setObject(15, null);
							dirPlrPstmt.setObject(16, null);
						} else if ("CHEQUE".equalsIgnoreCase(requestDetailsBean
								.getPaymentType())) {
							DateFormat dateFormat = new SimpleDateFormat(
									"dd-MM-yyyy");
							java.sql.Date chqDate = null;
							try {
								if (!"".equalsIgnoreCase(requestDetailsBean
										.getChequeDate())
										&& requestDetailsBean.getChequeDate() != null)
									chqDate = new java.sql.Date(
											(dateFormat
													.parse(requestDetailsBean
															.getChequeDate())
													.getTime()));
							} catch (ParseException e) {
								e.printStackTrace();
								throw new LMSException(
										"Exception date parsing  while pwt payments at Agent end ",
										e);
							}

							dirPlrPstmt.setString(13, requestDetailsBean
									.getChequeNbr());
							dirPlrPstmt.setDate(14, chqDate);
							dirPlrPstmt.setString(15, requestDetailsBean
									.getDraweeBank());
							dirPlrPstmt.setString(16, requestDetailsBean
									.getIssuingPartyName());
						} else {
							throw new LMSException(
									"Exception or Error No payment type specified "
											+ requestDetailsBean
													.getPaymentType());
						}

						if (isClaimmable) {
							dirPlrPstmt.setString(17, "CLAIM_BAL");
							// pstmt.setString(11, "CLAIM_BAL");
							totalClmBal += (pwtAmt + (pwtAmt * .01 * agtPwtCommRate));
						} else {
							dirPlrPstmt.setString(17, "UNCLAIM_BAL");
							// pstmt.setString(11, "UNCLAIM_BAL");
							totalUnClmBal += pwtAmt;
						}
						dirPlrPstmt.setDouble(18, agtPwtCommRate);
						dirPlrPstmt.setTimestamp(19, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						dirPlrPstmt.executeUpdate();
						System.out.println("insert in direct player table "
								+ dirPlrPstmt);
						// update status of of requested id entries into
						// st_se_pwt_approval_request_master
						String updateAppTable = "update  st_se_pwt_approval_request_master  set req_status ='PAID', "
								+ "remarks ='Payment Done', payment_done_by_type =?, payment_done_by =?, transaction_id=? where  task_id = ?";
						pstmt = connection.prepareStatement(updateAppTable);
						pstmt.setString(1, "AGENT");
						pstmt.setInt(2, agtOrgId);
						pstmt.setLong(3, transId);
						pstmt.setInt(4, requestDetailsBean.getTaskId());
						int j = pstmt.executeUpdate();
						System.out.println("total row updated = " + j
								+ " ,  requested id "
								+ requestDetailsBean.getTaskId()
								+ " status updated = " + pstmt);
						if (j < 1)
							throw new LMSException(
									"st_se_pwt_approval_request_master table not updated.");

					}

					// insert in ticket table
					if ("RETAILER".equals(partyType)
							&& requestDetailsBean != null) {
						// update the ticket_inv_detail table
						CommonFunctionsHelper commHelper = new CommonFunctionsHelper(); // partyId,
						// channel,
						// interfaceType
						commHelper.updateTicketInvTable(requestDetailsBean
								.getTicketNbr(), requestDetailsBean
								.getBookNbr(), gameNbr, gameId,
								(isClaimmable ? "CLAIM_RET_CLM"
										: "CLAIM_RET_UNCLM"), agentUserId,
								agtOrgId, "UPDATE", 0, null, null, connection);
					} else if ("PLAYER".equals(partyType)) {
						CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
						commHelper.updateTicketInvTable(requestDetailsBean
								.getTicketNbr(), requestDetailsBean
								.getBookNbr(), gameNbr, gameId,
								(isClaimmable ? "CLAIM_PLR_AGT_CLM_DIR"
										: "CLAIM_PLR_AGT_UNCLM_DIR"),
								agentUserId, agtOrgId, "UPDATE", 0, null, null,
								connection);
					}

					String pwtStatus = "";
					if ("RETAILER".equals(partyType) && isClaimmable) {
						pwtStatus = "CLAIM_RET_CLM";
					} else if ("RETAILER".equals(partyType) && !isClaimmable) {
						pwtStatus = "CLAIM_RET_UNCLM";
					} else if ("PLAYER".equals(partyType) && isClaimmable) {
						pwtStatus = "CLAIM_PLR_AGT_CLM_DIR";
					} else if ("PLAYER".equals(partyType) && !isClaimmable) {
						pwtStatus = "CLAIM_PLR_AGT_UNCLM_DIR";
					}

					// update the remaining prize list
					/*GameUtilityHelper.updateNoOfPrizeRemNewZim(gameId, pwtAmt,
							pwtStatus, encodedVirn, connection, gameNbr,MD5Encoder.encode(requestDetailsBean
									.getTicketNbr()));*/
									GameUtilityHelper.updateNoOfPrizeRemNewZim(gameId, pwtAmt,
											pwtStatus, encodedVirn, connection, gameNbr,MD5Encoder.encode(encodedTktNbr));

					// update VIRN status
					/*updateVirnStatus(gameNbr, pwtStatus, gameId, pwtBean
							.getEncVirnCode(), connection,MD5Encoder.encode(requestDetailsBean
								.getTicketNbr()));*/
									
									updateVirnStatus(gameNbr, pwtStatus, gameId, pwtBean
											.getEncVirnCode(), connection,MD5Encoder.encode(encodedTktNbr));

				}
			}
			System.out.println("insert into st_se_agent_pwt = " + pstmt);

			// update agent CLAIM_BAL payment
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			if (totalClmBal > 0.0) {
				// commHelper.updateOrgBalance("CLAIM_BAL", totalClmBal,
				// agtOrgId,"CREDIT", connection);
				// changed by ARUN when draw game sale come in picture
				OrgCreditUpdation.updateOrganizationBalWithValidate(totalClmBal, "CLAIM_BAL", "DEBIT", agtOrgId,
						0, "AGENT", 0, connection);
				/*commHelper.updateOrgBalance("CLAIM_BAL", totalClmBal, agtOrgId,
						"DEBIT", connection);*/
			}

			// update agent UNCLAIM_BAL payment
			if (totalUnClmBal > 0.0)
				OrgCreditUpdation.updateOrganizationBalWithValidate(totalUnClmBal, "UNCLAIM_BAL", "CREDIT", agtOrgId,
						0, "AGENT", 0, connection);
				/*commHelper.updateOrgBalance("UNCLAIM_BAL", totalUnClmBal,
						agtOrgId, "CREDIT", connection);*/

			// for retailer credit update
			if ("RETAILER".equals(partyType)) {
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retCreditAmt, "TRANSACTION", "PWT", partyId,
						agtOrgId, "RETAILER", 0, connection);
				
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(partyId, "PWT",
						retCreditAmt, connection);*/
				// update retailer UNCLAIM_BAL payment
				if(!isValid)
					throw new LMSException();
				System.out
						.println("inside retailer yogesh " + retTotalUnclmBal);
				if (retTotalUnclmBal > 0.0)
					OrgCreditUpdation.updateOrganizationBalWithValidate(retTotalUnclmBal, "UNCLAIM_BAL", "DEBIT", partyId,
							agtOrgId, "RETAILER", 0, connection);
				
					/*commHelper.updateOrgBalance("UNCLAIM_BAL",
							retTotalUnclmBal, partyId, "DEBIT", connection);*/
			}

			// receipt entries are required to be inserted into receipt table

			return transId;

		} else
			throw new LMSException(
					"no data insert into main transaction master");

	}

	public Map fetchPwtDetailsOfOrg(int retOrgId, String orgType,
			String status, Connection con) throws LMSException {

		Map map = new TreeMap();
		Map clmPwtDetailMap = new TreeMap();
		ClmPwtBean pwtBean = null;
		try {
			String fetchClmBalOfOrgQuery = "";
			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select  aaa.game_id, bbb.game_nbr, bbb.game_name, ticket_nbr, aaa.agt_claim_comm , "
						+ "aaa.virn_code, aaa.pwt_amt,  aaa.pwt_type, aaa.claim_comm, aaa.name from((select "
						+ "virn_code, ticket_nbr, pwt_amt, 'Anonymous' as  'pwt_type' , game_id, claim_comm , agt_claim_comm , "
						+ " 'Anonymous' as name from st_se_retailer_pwt where status = ?  and  retailer_org_id = ?) "
						+ "union (select  aa.virn_code, aa.ticket_nbr, aa.pwt_amt , 'direct_plr' as  'pwt_type', "
						+ "aa.game_id,  aa.claim_comm, agt_claim_comm , concat(bb.first_name, ' ', bb.last_name) 'name' from  "
						+ "st_se_retailer_direct_player_pwt aa, st_lms_player_master bb where  pwt_claim_status = ? "
						+ "and retailer_org_id = ? and aa.player_id = bb.player_id ))aaa, st_se_game_master bbb "
						+ "where aaa.game_id = bbb.game_id order by aaa.game_id";
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select  aa.game_id, bb.game_nbr, 'No Tkt' as 'ticket_nbr', bb.game_name,"
						+ " aa.virn_code, aa.pwt_amt, aa.pwt_type, aa.claim_comm, aa.name from((select a.virn_code,"
						+ " a.pwt_amt, 'Anonymous' as 'pwt_type' ,  a.game_id, a.claim_comm, b.name  from "
						+ "st_se_agent_pwt a, st_lms_organization_master b where a.retailer_org_id = b.organization_id "
						+ "and ( a.status = ? "
						+ ("UNCLAIM_BAL".equalsIgnoreCase(status) ? " or a.status ='BULK_BO' "
								: "")
						+ " )and a.agent_org_id = ?) union (select virn_code, pwt_amt 'dir_pwt_amt', "
						+ "'direct_plr' as 'pwt_type', game_id, claim_comm, concat(b.first_name, ' ', b.last_name) 'name'"
						+ "  from  st_se_agt_direct_player_pwt a, st_lms_player_master b  where a.player_id = b.player_id and "
						+ " ( pwt_claim_status = ? "
						+ ("UNCLAIM_BAL".equalsIgnoreCase(status) ? " or pwt_claim_status ='BULK_BO' "
								: "")
						+ " )and agent_org_id = ?))aa, st_se_game_master bb where aa.game_id = bb.game_id  order by aa.game_id";

			}
			System.out.println(fetchClmBalOfOrgQuery);
			PreparedStatement pstmt = con
					.prepareStatement(fetchClmBalOfOrgQuery);
			pstmt.setString(1, status);
			pstmt.setInt(2, retOrgId);
			pstmt.setString(3, status);
			pstmt.setInt(4, retOrgId);
			System.out.println("Fetch " + status + " PWT details for "
					+ orgType + " and orgId is " + retOrgId + " query is "
					+ pstmt);
			ResultSet result = pstmt.executeQuery();
			List<ClmPwtBean> beanList = new ArrayList<ClmPwtBean>();
			int gameId = -1;
			double totalClmBal = 0.0;
			int count = 0;
			while (result.next()) {
				count = count + 1;
				pwtBean = new ClmPwtBean();

				double pwtAmt = result.getDouble("pwt_amt");
				int game_id = result.getInt("game_id");

				totalClmBal = totalClmBal + pwtAmt;

				pwtBean.setVirnCode(result.getString("virn_code"));
				pwtBean.setTktNbr(result.getString("ticket_nbr"));
				pwtBean.setPwtAmt(result.getDouble("pwt_amt"));
				pwtBean.setPwtType(result.getString("pwt_type"));
				pwtBean.setClaimComm(result.getDouble("claim_comm"));
				pwtBean.setGameId(result.getInt("game_id"));
				pwtBean.setGameName(result.getString("game_name"));
				pwtBean.setGameNbr(result.getInt("game_nbr"));
				pwtBean.setClaimedBy(result.getString("name"));
				if ("RETAILER".equalsIgnoreCase(orgType)) {
					pwtBean.setAgtClaimComm(result.getDouble("agt_claim_comm"));
				}

				if (gameId != game_id) {
					if (gameId == -1)
						beanList.add(pwtBean);
					else {
						// System.out.println("inside else game_id = "+gameId+"
						// and list size = "+beanList.size());
						clmPwtDetailMap.put(gameId, beanList);
						beanList = new ArrayList<ClmPwtBean>();
						gameId = game_id;
						beanList.add(pwtBean);
					}
					gameId = game_id;
				} else {
					beanList.add(pwtBean);
					gameId = game_id;
				}

				// if result set row is last
				if (result.isLast()) {
					// System.out.println("inside if when result is last game_id
					// = "+gameId+" and list size = "+beanList.size());
					clmPwtDetailMap.put(gameId, beanList);
				}

			}
			// System.out.println("result row size = "+count);
			if (!clmPwtDetailMap.isEmpty()) {
				map.put("clmPwtDetails", clmPwtDetailMap);
				map.put("totalClmBal", totalClmBal);
			}
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	/**
	 * this method is depricated and not used commented on 10 july 2013 by sumit
	 * 
	 * @param userOrgId
	 * @param orgType
	 * @param status
	 * @param con
	 * @return
	 * @throws LMSException
	 */
/*	public Map fetchDrawSaleDetailsOfOrg(int userOrgId, String orgType,
			String status, Connection con) throws LMSException {

		Map map = new TreeMap();
		ClmDrawSaleBean darwSaleBean = null;
		PreparedStatement pstmtRefund = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtGameNbr = null;
		try {
			String fetchClmBalOfOrgQuery = "";
			String fetchDrawRefundClmBal = "";
			String getGameNbrFromGameMaster = "";

			Map<String, List<ClmDrawSaleBean>> clmDrawSaleDetailMap = new TreeMap();
			Map<String, List<ClmDrawSaleBean>> clmDrawSaleRefundDetailMap = new TreeMap();

			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select transaction_id ,mrp_amt ,retailer_comm,agent_comm,good_cause_amt,"
						+ "vat_amt,taxable_sale,game_id,(retailer_comm*100/mrp_amt) ret_comm_rate,(agent_comm*100/mrp_amt)"
						+ " agt_comm_rate,(good_cause_amt*100/mrp_amt) good_cause_rate from st_dg_ret_sale_? "
						+ "where retailer_org_id=? and claim_status=? order by game_id,ret_comm_rate,agt_comm_rate,"
						+ "good_cause_rate";

				fetchDrawRefundClmBal = "select a.transaction_id ,b.transaction_type,a.mrp_amt ,a.retailer_comm,a.agent_comm,a.good_cause_amt,"
						+ "a.vat_amt,a.taxable_sale,a.game_id,(a.retailer_comm*100/a.mrp_amt) ret_comm_rate,(a.agent_comm*100/a.mrp_amt) "
						+ "agt_comm_rate,(a.good_cause_amt*100/a.mrp_amt) good_cause_rate,a.cancellation_charges from st_dg_ret_sale_refund_? a,st_lms_retailer_transaction_master b "
						+ "where a.retailer_org_id=? and a.claim_status=? and b.transaction_id=a.transaction_id order by a.game_id,ret_comm_rate,agt_comm_rate,"
						+ "good_cause_rate,b.transaction_type";

				getGameNbrFromGameMaster = "select game_nbr,prize_payout_ratio,vat_amt from st_dg_game_master";
				pstmtGameNbr = con.prepareStatement(getGameNbrFromGameMaster);
				ResultSet resultGameNbr = pstmtGameNbr.executeQuery();
				int gameNbr = 0;
				double ppr = 0.0;
				double vatAmt = 0.0;
				while (resultGameNbr.next()) {

					gameNbr = resultGameNbr.getInt("game_nbr");
					ppr = resultGameNbr.getInt("prize_payout_ratio");
					vatAmt = resultGameNbr.getDouble("vat_amt");
					pstmt = con.prepareStatement(fetchClmBalOfOrgQuery);
					pstmt.setInt(1, gameNbr);
					pstmt.setInt(2, userOrgId);
					pstmt.setString(3, status);
					ResultSet result = pstmt.executeQuery();
					List<ClmDrawSaleBean> beanList = new ArrayList<ClmDrawSaleBean>();

					// String key="-1";
					while (result.next()) {
						darwSaleBean = new ClmDrawSaleBean();
						double mrpAmt = result.getDouble("mrp_amt");
						double retComm = result.getDouble("retailer_comm");
						double agentComm = result.getDouble("agent_comm");
						double goodCauseAmt = result
								.getDouble("good_cause_amt");
						int game_id = result.getInt("game_id");
						if (mrpAmt != 0) {
							String keyGen = game_id + ":"
									+ (retComm * 100 / mrpAmt) + ":"
									+ (agentComm * 100 / mrpAmt) + ":"
									+ (goodCauseAmt * 100 / mrpAmt);
							darwSaleBean.setPricePayRatio(ppr);
							darwSaleBean.setVatAmt(vatAmt);
							darwSaleBean.setMrpAmt(mrpAmt);
							darwSaleBean.setRetComm(retComm);
							darwSaleBean.setAgtComm(agentComm);
							darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
							darwSaleBean.setGameId(game_id);
							darwSaleBean.setTransactinId(result
									.getLong("transaction_id"));
							darwSaleBean.setGameNbr(gameNbr);
							// modify the below code so as to improve
							// readability . Use
							// map.contains method
							// remove duplicacy in variable declaration
							// modified by yogesh as suggested by reviewer
							// vinnet

							if (clmDrawSaleDetailMap.containsKey(keyGen)) {
								beanList = (clmDrawSaleDetailMap.get(keyGen));
								beanList.add(darwSaleBean);
								clmDrawSaleDetailMap.put(keyGen, beanList);
							} else {
								beanList = new ArrayList<ClmDrawSaleBean>();
								beanList.add(darwSaleBean);
								clmDrawSaleDetailMap.put(keyGen, beanList);
							}
						}
					}

					// get data from draw sale refund table
					pstmtRefund = con.prepareStatement(fetchDrawRefundClmBal);
					pstmtRefund.setInt(1, gameNbr);
					pstmtRefund.setInt(2, userOrgId);
					pstmtRefund.setString(3, status);
					ResultSet resultRefund = pstmtRefund.executeQuery();
					List<ClmDrawSaleBean> beanRefundList = new ArrayList<ClmDrawSaleBean>();
					// Map clmDrawSaleRefundDetailMap = new TreeMap();

					// String keyRefund="-1";
					while (resultRefund.next()) {
						darwSaleBean = new ClmDrawSaleBean();
						double mrpAmt = resultRefund.getDouble("mrp_amt");
						double retComm = resultRefund
								.getDouble("retailer_comm");
						double agentComm = resultRefund.getDouble("agent_comm");
						double goodCauseAmt = resultRefund
								.getDouble("good_cause_amt");
						int game_id = resultRefund.getInt("game_id");
						double cancellationCharges = resultRefund
								.getDouble("cancellation_charges");
						String refundType = resultRefund
								.getString("transaction_type");
						if (mrpAmt != 0) {
							String keyGenRefund = game_id + ":"
									+ (retComm * 100 / mrpAmt) + ":"
									+ (agentComm * 100 / mrpAmt) + ":"
									+ (goodCauseAmt * 100 / mrpAmt) + ":"
									+ refundType;
							darwSaleBean.setPricePayRatio(ppr);
							darwSaleBean.setVatAmt(vatAmt);
							darwSaleBean.setMrpAmt(mrpAmt);
							darwSaleBean.setRetComm(retComm);
							darwSaleBean.setAgtComm(agentComm);
							darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
							darwSaleBean.setGameId(game_id);
							darwSaleBean.setTransactinId(resultRefund
									.getLong("transaction_id"));
							darwSaleBean.setGameNbr(gameNbr);
							darwSaleBean
									.setCancellationCharges(cancellationCharges);
							// modify same as sale

							if (clmDrawSaleRefundDetailMap
									.containsKey(keyGenRefund)) {
								beanRefundList = (clmDrawSaleRefundDetailMap
										.get(keyGenRefund));
								beanRefundList.add(darwSaleBean);
								clmDrawSaleRefundDetailMap.put(keyGenRefund,
										beanRefundList);
							} else {
								beanRefundList = new ArrayList<ClmDrawSaleBean>();
								beanRefundList.add(darwSaleBean);
								clmDrawSaleRefundDetailMap.put(keyGenRefund,
										beanRefundList);
							}

						}

					}
				}
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select transaction_id ,mrp_amt ,retailer_comm,agent_comm,govt_comm,"
						+ "vat_amt,taxable_sale,game_id,(agent_comm*100/mrp_amt) agt_comm_rate,(govt_comm*100/mrp_amt) "
						+ "good_cause_rate from st_dg_agt_sale where agent_org_id=? and claim_status=? "
						+ "order by game_id,agt_comm_rate,good_cause_rate";

				fetchDrawRefundClmBal = "select a.transaction_id ,b.transaction_type,a.mrp_amt ,a.retailer_comm,a.agent_comm,govt_comm,a.vat_amt,"
						+ "a.taxable_sale,a.game_id,(a.agent_comm*100/a.mrp_amt) agt_comm_rate,(a.govt_comm*100/a.mrp_amt) "
						+ "good_cause_rate,a.cancellation_charges from st_dg_agt_sale_refund a,st_lms_agent_transaction_master b where a.agent_org_id=? and a.claim_status=? and a.transaction_id=b.transaction_id "
						+ " order by a.game_id,agt_comm_rate,good_cause_rate,b.transaction_type";

				pstmt = con.prepareStatement(fetchClmBalOfOrgQuery);
				pstmt.setInt(1, userOrgId);
				pstmt.setString(2, status);
				ResultSet result = pstmt.executeQuery();
				List<ClmDrawSaleBean> beanList = new ArrayList<ClmDrawSaleBean>();
				// String key="-1";
				while (result.next()) {
					darwSaleBean = new ClmDrawSaleBean();
					double mrpAmt = result.getDouble("mrp_amt");
					double agentComm = result.getDouble("agent_comm");
					double goodCauseAmt = result.getDouble("govt_comm");
					int game_id = result.getInt("game_id");
					if (mrpAmt != 0) {
						String keyGen = game_id + ":"
								+ (agentComm * 100 / mrpAmt) + ":"
								+ (goodCauseAmt * 100 / mrpAmt);

						darwSaleBean.setMrpAmt(mrpAmt);
						darwSaleBean.setAgtComm(agentComm);
						darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
						darwSaleBean.setGameId(game_id);
						darwSaleBean.setTransactinId(result
								.getLong("transaction_id"));

						// modify same as retailer

						if (clmDrawSaleDetailMap.containsKey(keyGen)) {
							beanList = (clmDrawSaleDetailMap.get(keyGen));
							beanList.add(darwSaleBean);
							clmDrawSaleDetailMap.put(keyGen, beanList);
						} else {
							beanList = new ArrayList<ClmDrawSaleBean>();
							beanList.add(darwSaleBean);
							clmDrawSaleDetailMap.put(keyGen, beanList);
						}
					}

				}

				// get data from draw sale refund table
				pstmtRefund = con.prepareStatement(fetchDrawRefundClmBal);
				pstmtRefund.setInt(1, userOrgId);
				pstmtRefund.setString(2, status);
				ResultSet resultRefund = pstmtRefund.executeQuery();
				List<ClmDrawSaleBean> beanRefundList = new ArrayList<ClmDrawSaleBean>();

				// String keyRefund="-1";
				while (resultRefund.next()) {
					darwSaleBean = new ClmDrawSaleBean();
					double mrpAmt = resultRefund.getDouble("mrp_amt");
					double agentComm = resultRefund.getDouble("agent_comm");
					double goodCauseAmt = resultRefund.getDouble("govt_comm");
					int game_id = resultRefund.getInt("game_id");
					double cancellationCharges = resultRefund
							.getDouble("cancellation_charges");
					String refundType = resultRefund
							.getString("transaction_type");
					if (mrpAmt != 0) {
						String keyGenRefund = game_id + ":"
								+ (agentComm * 100 / mrpAmt) + ":"
								+ (goodCauseAmt * 100 / mrpAmt) + ":"
								+ refundType;

						darwSaleBean.setMrpAmt(mrpAmt);
						darwSaleBean.setAgtComm(agentComm);
						darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
						darwSaleBean.setGameId(game_id);
						darwSaleBean.setTransactinId(resultRefund
								.getLong("transaction_id"));
						darwSaleBean
								.setCancellationCharges(cancellationCharges);
						// modify same as retailer

						if (clmDrawSaleRefundDetailMap
								.containsKey(keyGenRefund)) {
							beanRefundList = (clmDrawSaleRefundDetailMap
									.get(keyGenRefund));
							beanRefundList.add(darwSaleBean);
							clmDrawSaleRefundDetailMap.put(keyGenRefund,
									beanRefundList);
						} else {
							beanRefundList = new ArrayList<ClmDrawSaleBean>();
							beanRefundList.add(darwSaleBean);
							clmDrawSaleRefundDetailMap.put(keyGenRefund,
									beanRefundList);
						}
					}
				}

			}

			if (clmDrawSaleDetailMap != null && !clmDrawSaleDetailMap.isEmpty())
				map.put("drawSaleMap", clmDrawSaleDetailMap);
			if (clmDrawSaleRefundDetailMap != null
					&& !clmDrawSaleRefundDetailMap.isEmpty())
				map.put("drawSaleRefundMap", clmDrawSaleRefundDetailMap);

			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}*/

	public static void main(String[] args) {
		try {

			DBConnect co = new DBConnect();
			Connection con = co.getConnection();
			CommonFunctionsHelper t = new CommonFunctionsHelper();
			// t.updateClmableBalOfOrg(533,162, "AGENT");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * this method is depricated and not used commented on 10 july 2013 by sumit
	 * 
	 * @param drawMap
	 * @param retOrgId
	 * @param retParentUserId
	 * @param retParentOrgId
	 * @param con
	 * @return
	 * @throws LMSException
	 */
/*	public String updateClaimableForDrawSaleRet(Map drawMap, int retOrgId,
			int retParentUserId, int retParentOrgId, Connection con)
			throws LMSException {
		System.out.println("insideeeeeeeeeeeeeeeeeeeee");
		double retDebitAmount = 0.0;// total amount of retailer that would be
		// deducted from retailers ACA
		List<Long> trnIdListInvoice = new ArrayList<Long>();
		List<Long> trnIdListCRNote = new ArrayList<Long>();
		String autoGeneRecieptNo = null;
		try {

			Map<String, List<ClmDrawSaleBean>> drawSaleMap = (Map) drawMap
					.get("drawSaleMap");
			Map<String, List<ClmDrawSaleBean>> drawSaleRefundMap = (Map) drawMap
					.get("drawSaleRefundMap");
			List<ClmDrawSaleBean> drawSaleBeanList = null;

			if (drawSaleMap != null) {
				Set keySet = drawSaleMap.keySet();
				for (Object key : keySet) {
					drawSaleBeanList = drawSaleMap.get(key);
					double totalMrpAmt = 0.0;
					double totalRetComm = 0.0;
					double totalAgtComm = 0.0;
					double totalretNetAmt = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
						totalMrpAmt = totalMrpAmt + clmDrawSaleBean.getMrpAmt();
						totalRetComm = totalRetComm
								+ clmDrawSaleBean.getRetComm();
						totalAgtComm = totalAgtComm
								+ clmDrawSaleBean.getAgtComm();
						totalGoodCauseAmt = totalGoodCauseAmt
								+ clmDrawSaleBean.getAgtGoodCauseAmt();
					}
					totalretNetAmt = totalMrpAmt - totalRetComm;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm;

					// calculate agt vat , agt taxable sale and agt good cause
					double retCommRate = Double.parseDouble(((String) key)
							.split(":")[1]);
					double goodCauserate = Double.parseDouble(((String) key)
							.split(":")[3]);

					double totalAgtVat = CommonMethods.calculateDrawGameVatRet(
							totalMrpAmt, retCommRate, ppr, goodCauserate,
							vatAmt);
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, retCommRate, ppr, goodCauserate,
							vatAmt);

					retDebitAmount = retDebitAmount + totalretNetAmt;

					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "AGENT");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						trnIdListInvoice.add(transId);
						// insert in agent transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInAgentTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentUserId);
						pstmt.setInt(3, retParentOrgId);
						pstmt.setString(4, "RETAILER");
						pstmt.setInt(5, retOrgId);
						pstmt.setString(6, "DG_SALE");
						pstmt.setTimestamp(7, new java.sql.Timestamp(new Date()
								.getTime()));
						pstmt.executeUpdate();

						// insert in agent draw game sale table
						pstmt = con
								.prepareStatement("insert into st_dg_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentOrgId);
						pstmt.setInt(3, retOrgId);
						pstmt.setInt(4, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(5, totalMrpAmt);
						pstmt.setDouble(6, totalRetComm);
						pstmt.setDouble(7, totalretNetAmt);
						pstmt.setDouble(8, totalAgtComm);
						pstmt.setDouble(9, totalAgtNetAmt);
						pstmt.setString(10, "CLAIM_BAL");
						pstmt.setDouble(11, totalAgtVat);
						pstmt.setDouble(12, tatalAgtTaxableSale);
						pstmt.setDouble(13, totalGoodCauseAmt);
						pstmt.executeUpdate();

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_ret_sale_? set claim_status=?,agent_ref_transaction_id=? where transaction_id=?");
							pstmt.setInt(1, clmDrawSaleBean.getGameNbr());
							pstmt.setString(2, "DONE_CLAIM");
							pstmt.setLong(3, transId);
							pstmt.setLong(4, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate receipt here
				// int invoiceId = -1;
				// String autoGeneRecieptNo=null;
				if (trnIdListInvoice.size() > 0 && trnIdListInvoice != null) {
					PreparedStatement agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.getAGENTLatestReceiptNb());
					agtReceiptNoGenStmt.setString(1, "INVOICE");
					agtReceiptNoGenStmt.setInt(2, retParentOrgId);
					ResultSet recieptRs = agtReceiptNoGenStmt.executeQuery();
					String lastRecieptNoGenerated = null;
					while (recieptRs.next()) {
						lastRecieptNoGenerated = recieptRs
								.getString("generated_id");
					}

					// String autoGeneRecieptNo=null;
					autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
							"INVOICE", lastRecieptNoGenerated, "AGENT",
							retParentOrgId);

					// insert in receipt master for invoice

					agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertInReceiptMaster());
					agtReceiptNoGenStmt.setString(1, "AGENT");
					agtReceiptNoGenStmt.executeUpdate();

					ResultSet rs = agtReceiptNoGenStmt.getGeneratedKeys();
					int invoiceId = -1;
					while (rs.next()) {
						invoiceId = rs.getInt(1);
					}

					// insert into agent receipt table

					agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertInAgentReceipts());
					agtReceiptNoGenStmt.setInt(1, invoiceId);
					agtReceiptNoGenStmt.setString(2, "INVOICE");
					agtReceiptNoGenStmt.setInt(3, retParentOrgId);
					agtReceiptNoGenStmt.setInt(4, retOrgId);
					agtReceiptNoGenStmt.setString(5, "RETAILER");
					agtReceiptNoGenStmt.setString(6, autoGeneRecieptNo);
					agtReceiptNoGenStmt.setTimestamp(7, Util.getCurrentTimeStamp());
					agtReceiptNoGenStmt.execute();

					agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertAgentReceiptTrnMapping());
					for (int i = 0; i < trnIdListInvoice.size(); i++) {
						agtReceiptNoGenStmt.setInt(1, invoiceId);
						agtReceiptNoGenStmt.setLong(2, trnIdListInvoice.get(i));
						agtReceiptNoGenStmt.execute();

					}

				}

			}
			// update for refund sale
			if (drawSaleRefundMap != null) {
				Set keySetRefund = drawSaleRefundMap.keySet();
				for (Object key : keySetRefund) {
					drawSaleBeanList = drawSaleRefundMap.get(key);
					double totalMrpAmt = 0.0;
					double totalRetComm = 0.0;
					double totalAgtComm = 0.0;
					double totalretNetAmt = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double totalCancellationCharges = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {

						// totalMrpAmt=clmDrawSaleBean.getMrpAmt();
						totalMrpAmt += clmDrawSaleBean.getMrpAmt();

						// totalRetComm=clmDrawSaleBean.getRetComm();
						totalRetComm += clmDrawSaleBean.getRetComm();

						totalCancellationCharges += clmDrawSaleBean
								.getCancellationCharges();

						// totalAgtComm = clmDrawSaleBean.getAgtComm();
						totalAgtComm += clmDrawSaleBean.getAgtComm();

						// totalGoodCauseAmt =
						// clmDrawSaleBean.getAgtGoodCauseAmt();
						totalGoodCauseAmt += clmDrawSaleBean
								.getAgtGoodCauseAmt();

					}
					totalretNetAmt = totalMrpAmt - totalRetComm
							- totalCancellationCharges;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm
							- totalCancellationCharges;

					// calculate agt vat , agt taxable sale and agt good cause

					double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
							totalMrpAmt, Double.parseDouble(((String) key)
									.split(":")[1]), ppr, Double
									.parseDouble(((String) key).split(":")[3]),
							vatAmt);
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, Double.parseDouble(((String) key)
									.split(":")[1]), ppr, Double
									.parseDouble(((String) key).split(":")[3]),
							vatAmt);

					retDebitAmount = retDebitAmount - totalretNetAmt;

					// insert in main transaction master
					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "AGENT");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						Long transId = rsTrns.getLong(1);
						trnIdListCRNote.add(transId);
						// insert in agent transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInAgentTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentUserId);
						pstmt.setInt(3, retParentOrgId);
						pstmt.setString(4, "RETAILER");
						pstmt.setInt(5, retOrgId);
						pstmt.setString(6, ((String) key).split(":")[4]);
						pstmt.setTimestamp(7, new java.sql.Timestamp(new Date()
								.getTime()));
						pstmt.executeUpdate();

						// insert in agent draw game sale table
						// double retNetAmt=clmDrawSaleBean.getMrpAmt() -
						// clmDrawSaleBean.getRetComm();
						// retDebitAmount+=retNetAmt;
						pstmt = con
								.prepareStatement("insert into st_dg_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentOrgId);
						pstmt.setInt(3, retOrgId);
						pstmt.setInt(4, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(5, totalMrpAmt);
						pstmt.setDouble(6, totalRetComm);
						pstmt.setDouble(7, totalretNetAmt);
						pstmt.setDouble(8, totalAgtComm);
						pstmt.setDouble(9, totalAgtNetAmt);
						pstmt.setString(10, "CLAIM_BAL");
						pstmt.setDouble(11, totalAgtVat);
						pstmt.setDouble(12, tatalAgtTaxableSale);
						pstmt.setDouble(13, totalGoodCauseAmt);
						pstmt.setDouble(14, totalCancellationCharges);
						pstmt.executeUpdate();
						System.out.println("%%%%%insert " + pstmt);

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_ret_sale_refund_? set claim_status=?,agent_ref_transaction_id=? where transaction_id=?");
							pstmt.setInt(1, clmDrawSaleBean.getGameNbr());
							pstmt.setString(2, "DONE_CLAIM");
							pstmt.setLong(3, transId);
							pstmt.setLong(4, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate receipt here
				// int credit note = -1;
				// String autoGeneRecieptNo=null;
				if (trnIdListCRNote.size() > 0 && trnIdListCRNote != null) {
					PreparedStatement agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.getAGENTLatestReceiptNb());
					agtReceiptNoGenStmt.setString(1, "CR_NOTE");
					agtReceiptNoGenStmt.setInt(2, retParentOrgId);
					ResultSet recieptRs = agtReceiptNoGenStmt.executeQuery();
					String lastRecieptNoGenerated = null;
					while (recieptRs.next()) {
						lastRecieptNoGenerated = recieptRs
								.getString("generated_id");
					}

					// String autoGeneRecieptNo=null;
					autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
							"CR_NOTE", lastRecieptNoGenerated, "AGENT",
							retParentOrgId);

					// insert in receipt master for invoice

					agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertInReceiptMaster());
					agtReceiptNoGenStmt.setString(1, "AGENT");
					agtReceiptNoGenStmt.executeUpdate();

					ResultSet rs = agtReceiptNoGenStmt.getGeneratedKeys();
					int crNoteId = -1;
					while (rs.next()) {
						crNoteId = rs.getInt(1);
					}

					// insert into agent receipt table

					agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertInAgentReceipts());
					agtReceiptNoGenStmt.setInt(1, crNoteId);
					agtReceiptNoGenStmt.setString(2, "CR_NOTE");
					agtReceiptNoGenStmt.setInt(3, retParentOrgId);
					agtReceiptNoGenStmt.setInt(4, retOrgId);
					agtReceiptNoGenStmt.setString(5, "RETAILER");
					agtReceiptNoGenStmt.setString(6, autoGeneRecieptNo);
					agtReceiptNoGenStmt.execute();

					agtReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertAgentReceiptTrnMapping());
					for (int i = 0; i < trnIdListCRNote.size(); i++) {
						agtReceiptNoGenStmt.setInt(1, crNoteId);
						agtReceiptNoGenStmt.setLong(2, trnIdListCRNote.get(i));
						agtReceiptNoGenStmt.execute();

					}

				}

			}

			System.out.println("&&&&&77777777 " + retDebitAmount);
			// update organization claimable balance
			OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
					"DRAW_GAME_SALE", retDebitAmount, connection);
			updateOrgBalance("CLAIM_BAL", retDebitAmount, retOrgId, "DEBIT",
					connection);
			return autoGeneRecieptNo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}

	}*/
/**
 * 
 * this method is depricated and not used commented on 10 july 2013 by sumit
 *
 * @param drawMap
 * @param agtOrgId
 * @param agtParentUserId
 * @param agtParentOrgId
 * @param con
 * @return
 * @throws LMSException
 */
/*	public String updateClaimableForDrawSaleAgt(Map drawMap, int agtOrgId,
			int agtParentUserId, int agtParentOrgId, Connection con)
			throws LMSException {
		System.out.println("insideeeeeeeeeeeeeeeeeeeee agt updation ");
		double agtDebitAmount = 0.0;// total amount of retailer that would be
		// deducted from retailers ACA
		List<Long> trnIdListInvoice = new ArrayList<Long>();
		List<Long> trnIdListCRNote = new ArrayList<Long>();
		String autoGeneRecieptNo = null;
		try {

			Map<String, List<ClmDrawSaleBean>> drawSaleMap = (Map) drawMap
					.get("drawSaleMap");
			Map<String, List<ClmDrawSaleBean>> drawSaleRefundMap = (Map) drawMap
					.get("drawSaleRefundMap");
			List<ClmDrawSaleBean> drawSaleBeanList = null;
			if (drawSaleMap != null) {
				Set keySet = drawSaleMap.keySet();
				for (Object key : keySet) {
					drawSaleBeanList = drawSaleMap.get(key);
					double totalMrpAmt = 0.0;
					double totalAgtComm = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						getVatAndPpr(con, drawSaleBeanList.get(0), Integer
								.parseInt(((String) key).split(":")[0]));
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
						totalMrpAmt += clmDrawSaleBean.getMrpAmt();
						totalAgtComm += clmDrawSaleBean.getAgtComm();
						totalGoodCauseAmt += clmDrawSaleBean
								.getAgtGoodCauseAmt();
					}

					totalAgtNetAmt = totalMrpAmt - totalAgtComm;

					// calculate agt vat , agt taxable sale and agt good cause
					double agtCommRate = Double.parseDouble(((String) key)
							.split(":")[1]);
					double goodcauseRate = Double.parseDouble(((String) key)
							.split(":")[2]);
					double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);

					agtDebitAmount += totalAgtNetAmt;

					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "BO");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						trnIdListInvoice.add(transId);
						// insert in bo transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInBOTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtParentUserId);
						pstmt.setInt(3, agtParentOrgId);
						pstmt.setString(4, "AGENT");
						pstmt.setInt(5, agtOrgId);
						pstmt.setTimestamp(6, new java.sql.Timestamp(new Date()
								.getTime()));
						pstmt.setString(7, "DG_SALE");
						pstmt.executeUpdate();

						// insert in bo draw game sale table
						pstmt = con
								.prepareStatement("insert into st_dg_bo_sale(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?)");
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtOrgId);
						pstmt.setInt(3, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(4, totalMrpAmt);
						pstmt.setDouble(5, totalAgtComm);
						pstmt.setDouble(6, totalAgtNetAmt);
						pstmt.setDouble(7, totalAgtVat);
						pstmt.setDouble(8, tatalAgtTaxableSale);
						pstmt.setDouble(9, totalGoodCauseAmt);
						pstmt.executeUpdate();

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_agt_sale set claim_status=?,bo_ref_transaction_id=? where transaction_id=?");
							pstmt.setString(1, "DONE_CLAIM");
							pstmt.setLong(2, transId);
							pstmt.setLong(3, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate INVOICE at BO End here
				// int invoiceId = -1;
				// String autoGeneRecieptNo = null;
				if (trnIdListInvoice.size() > 0 && trnIdListInvoice != null) {
					PreparedStatement boReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.getBOLatestReceiptNb());
					boReceiptNoGenStmt.setString(1, "INVOICE");
					ResultSet recieptRs = boReceiptNoGenStmt.executeQuery();
					String lastRecieptNoGenerated = null;

					while (recieptRs.next()) {
						lastRecieptNoGenerated = recieptRs
								.getString("generated_id");
					}

					// String autoGeneRecieptNo = null;
					autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
							"INVOICE", lastRecieptNoGenerated, "BO");

					boReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertInReceiptMaster());
					boReceiptNoGenStmt.setString(1, "BO");
					boReceiptNoGenStmt.executeUpdate();

					ResultSet rs = boReceiptNoGenStmt.getGeneratedKeys();
					int invoiceId = -1;
					while (rs.next()) {
						invoiceId = rs.getInt(1);
					}

					// insert in bo receipts
					boReceiptNoGenStmt = connection
							.prepareStatement(QueryManager.insertInBOReceipts());
					boReceiptNoGenStmt.setInt(1, invoiceId);
					boReceiptNoGenStmt.setString(2, "INVOICE");
					boReceiptNoGenStmt.setInt(3, agtOrgId);
					boReceiptNoGenStmt.setString(4, "AGENT");
					boReceiptNoGenStmt.setString(5, autoGeneRecieptNo);
					boReceiptNoGenStmt.setTimestamp(6, Util.getCurrentTimeStamp());
					boReceiptNoGenStmt.execute();

					boReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertBOReceiptTrnMapping());

					for (int i = 0; i < trnIdListInvoice.size(); i++) {
						boReceiptNoGenStmt.setInt(1, invoiceId);
						boReceiptNoGenStmt.setLong(2, trnIdListInvoice.get(i));
						boReceiptNoGenStmt.execute();

					}
				}

			}
			// update for refund sale
			if (drawSaleRefundMap != null) {
				Set keySetRefund = drawSaleRefundMap.keySet();
				for (Object key : keySetRefund) {
					drawSaleBeanList = drawSaleRefundMap.get(key);
					double totalMrpAmt = 0.0;
					double totalAgtComm = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double totalCancellationCharges = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
						totalMrpAmt += clmDrawSaleBean.getMrpAmt();
						totalAgtComm += clmDrawSaleBean.getAgtComm();
						totalCancellationCharges += clmDrawSaleBean
								.getCancellationCharges();
						totalGoodCauseAmt += clmDrawSaleBean
								.getAgtGoodCauseAmt();
					}

					totalAgtNetAmt = totalMrpAmt - totalAgtComm
							- totalCancellationCharges;

					// calculate agt vat , agt taxable sale and agt good cause
					double agtCommRate = Double.parseDouble(((String) key)
							.split(":")[1]);
					double goodcauseRate = Double.parseDouble(((String) key)
							.split(":")[2]);
					double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);
					// double tatalAgtTaxableSale =
					// (((totalMrpAmt*(100-(Double.parseDouble(((String)key).split(":")[1])*100/totalMrpAmt
					// + 50
					// +Double.parseDouble(((String)key).split(":")[2])*100/totalMrpAmt)))/100)*100)/(100+16);
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);
					agtDebitAmount -= totalAgtNetAmt;

					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "BO");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						trnIdListCRNote.add(transId);
						// insert in bo transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInBOTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtParentUserId);
						pstmt.setInt(3, agtParentOrgId);
						pstmt.setString(4, "AGENT");
						pstmt.setInt(5, agtOrgId);
						pstmt.setTimestamp(6, new java.sql.Timestamp(new Date()
								.getTime()));
						pstmt.setString(7, ((String) key).split(":")[3]);
						pstmt.executeUpdate();

						// insert in bo draw game sale table
						pstmt = con
								.prepareStatement("insert into st_dg_bo_sale_refund(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?)");
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtOrgId);
						pstmt.setInt(3, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(4, totalMrpAmt);
						pstmt.setDouble(5, totalAgtComm);
						pstmt.setDouble(6, totalAgtNetAmt);
						pstmt.setDouble(7, totalAgtVat);
						pstmt.setDouble(8, tatalAgtTaxableSale);
						pstmt.setDouble(9, totalGoodCauseAmt);
						pstmt.setDouble(10, totalCancellationCharges);
						pstmt.executeUpdate();
						System.out.println("%%%%%insert " + pstmt);

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_agt_sale_refund set claim_status=?,bo_ref_transaction_id=? where transaction_id=?");
							pstmt.setString(1, "DONE_CLAIM");
							pstmt.setLong(2, transId);
							pstmt.setLong(3, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate INVOICE at BO End here
				// int invoiceId = -1;
				// String autoGeneRecieptNo = null;
				if (trnIdListCRNote.size() > 0 && trnIdListCRNote != null) {
					PreparedStatement boReceiptNoGenStmt = connection
							.prepareStatement("SELECT generated_id from st_lms_bo_receipts where receipt_type like ('CR_NOTE%')  ORDER BY generated_id DESC LIMIT 1");
					// boReceiptNoGenStmt.setString(1, "INVOICE");
					ResultSet recieptRs = boReceiptNoGenStmt.executeQuery();
					String lastRecieptNoGenerated = null;

					while (recieptRs.next()) {
						lastRecieptNoGenerated = recieptRs
								.getString("generated_id");
					}

					// String autoGeneRecieptNo = null;
					autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
							"CR_NOTE", lastRecieptNoGenerated, "BO");

					boReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertInReceiptMaster());
					boReceiptNoGenStmt.setString(1, "BO");
					boReceiptNoGenStmt.executeUpdate();

					ResultSet rs = boReceiptNoGenStmt.getGeneratedKeys();
					int crNoteId = -1;
					while (rs.next()) {
						crNoteId = rs.getInt(1);
					}

					// insert in bo receipts
					boReceiptNoGenStmt = connection
							.prepareStatement(QueryManager.insertInBOReceipts());
					boReceiptNoGenStmt.setInt(1, crNoteId);
					boReceiptNoGenStmt.setString(2, "CR_NOTE");
					boReceiptNoGenStmt.setInt(3, agtOrgId);
					boReceiptNoGenStmt.setString(4, "AGENT");
					boReceiptNoGenStmt.setString(5, autoGeneRecieptNo);
					boReceiptNoGenStmt.execute();

					boReceiptNoGenStmt = connection
							.prepareStatement(QueryManager
									.insertBOReceiptTrnMapping());

					for (int i = 0; i < trnIdListCRNote.size(); i++) {
						boReceiptNoGenStmt.setInt(1, crNoteId);
						boReceiptNoGenStmt.setLong(2, trnIdListCRNote.get(i));
						boReceiptNoGenStmt.execute();

					}
				}

			}

			System.out.println("Agent Debit Amount " + agtDebitAmount);
			// update organization claimable balance
			OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
					"DRAW_GAME_SALE", agtDebitAmount, connection);
			updateOrgBalance("CLAIM_BAL", agtDebitAmount, agtOrgId, "DEBIT",
					connection);
			return autoGeneRecieptNo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}

	}
*/
	
	/**
	 * this method is depricated and not used commented on 10 july 2013 by sumit
	 * 
	 * 
	 */
/*	public List<UserInfoBean> getOrgIdNClmAmtList(Connection con)
			throws SQLException {
		List<UserInfoBean> orgList = new ArrayList<UserInfoBean>();
		String getOrgIdNClmListQuery = "select aa.organization_id, aa.organization_type, aa.parent_id, aa.name, "
				+ " aa.claimable_bal, bb.user_id from st_lms_organization_master aa, st_lms_user_master bb where "
				+ " aa.organization_id = bb.organization_id and bb.isrolehead = 'Y' and "
				+ " (aa.organization_type = 'AGENT' or aa.organization_type = 'RETAILER') and "
				+ " aa.claimable_bal>0 and aa.organization_status!='TERMINATE' order by organization_type desc";
		PreparedStatement pstmt = con.prepareStatement(getOrgIdNClmListQuery);
		ResultSet rs = pstmt.executeQuery();
		System.out.println("getOrgIdNClmAmtListQuery is = " + pstmt);
		UserInfoBean userBean = null;
		while (rs.next()) {
			userBean = new UserInfoBean();
			userBean.setUserOrgId(rs.getInt("organization_id"));
			userBean.setUserId(rs.getInt("user_id"));
			userBean.setClaimableBal(rs.getDouble("claimable_bal"));
			userBean.setUserType(rs.getString("organization_type"));
			userBean.setOrgName(rs.getString("name"));
			userBean.setParentOrgId(rs.getInt("parent_id"));
			orgList.add(userBean);
		}
		return orgList;
	}
*/
	public String boEndAgtPWTPaymentProcess(List<PwtBean> pwtList, int gameId,
			String boOrgName, int userOrgID, int agentOrgId, int agtUserId,
			String rootPath, int userId, int gameNbr) throws LMSException {

		Connection connection = null;
		PreparedStatement masterPstmt = null;
		PreparedStatement LMSMasterPstmt = null;
		PreparedStatement detailPstmt = null;
		PreparedStatement invPstmt = null;
	//	PreparedStatement agtPwtUpdatePstmt = null;

		ResultSet resultSet = null;

		long trnId = -1;
		int boReceiptId = -1;
		// String receiptArr []=null;
		String receipts = null;
		try {
			if (pwtList != null) {
				int size = pwtList.size();
				PwtBean pwtBean = null;

				String masterQuery = null;
				String LMSMasterQuery = null;
				String detailQuery = null;
				String invQuery = null;

				double pwtAmt = 0.0;
				double commAmt = 0.0;
				double netAmt = 0.0;
				double creditAmt = 0.0;
				double debitUnclaim = 0.0;

				if (size > 0) {

					DBConnect dbConnect = new DBConnect();
					connection = dbConnect.getConnection();
					connection.setAutoCommit(false);

					// get agents game pwtCommRate
					CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
					double agtPwtCommRate = commonHelper
							.fetchCommOfOrganization(gameId, agentOrgId, "PWT",
									"AGENT", connection);

					masterQuery = QueryManager.insertInBOTransactionMaster();
					masterPstmt = connection.prepareStatement(masterQuery);

					detailQuery = QueryManager.getST1PwtBODetailQuery();
					detailPstmt = connection.prepareStatement(detailQuery);

					invQuery = QueryManager.getST1PWTBOUpdateQuery();
					invPstmt = connection.prepareStatement(invQuery);

					// update st_se_agent_pwt in case if agent brings uncalimed
					// PWTs to BO
				//	String agtPwtUpdateQuery = "update  st_se_agent_pwt set status=? where game_id = ? and virn_code = ? and agent_org_id = ? ";
				//  agtPwtUpdatePstmt = connection
				//			.prepareStatement(agtPwtUpdateQuery);

					// insert into transaction master
					LMSMasterQuery = QueryManager
							.insertInLMSTransactionMaster();
					LMSMasterPstmt = connection
							.prepareStatement(LMSMasterQuery);
					LMSMasterPstmt.setString(1, "BO");
					LMSMasterPstmt.executeUpdate();
					resultSet = LMSMasterPstmt.getGeneratedKeys();
					if (resultSet.next())
						trnId = resultSet.getLong(1);
					else
						throw new LMSException(
								"Transaction id is not generated");

					// insert into BO transaction master
					masterPstmt.setLong(1, trnId);
					masterPstmt.setInt(2, userId);
					masterPstmt.setInt(3, userOrgID);
					masterPstmt.setString(4, "AGENT");
					masterPstmt.setInt(5, agentOrgId);
					masterPstmt.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					masterPstmt.setString(7, "PWT");
					masterPstmt.execute();
					System.out.println("transaction Id::" + trnId);

					for (int i = 0; i < size; i++) {
						pwtBean = (PwtBean) pwtList.get(i);

						if (pwtBean.getIsValid()) {

							String encodedVirn = pwtBean.getEncVirnCode();
							detailPstmt.setString(1, encodedVirn);
							detailPstmt.setLong(2, trnId);
							detailPstmt.setInt(3, gameId);
							detailPstmt.setInt(4, agtUserId);
							detailPstmt.setInt(5, agentOrgId);

							pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
							commAmt = pwtAmt * agtPwtCommRate * 0.01;
							netAmt = pwtAmt + commAmt;
							// for current credit amount update
							creditAmt += netAmt;

							detailPstmt.setDouble(6, pwtAmt);
							detailPstmt.setDouble(7, commAmt);
							detailPstmt.setDouble(8, netAmt);
							detailPstmt.execute();

							if ("IN_PLR_UNCLM".equalsIgnoreCase(pwtBean
									.getInUnclmed())
									|| "IN_AGENT_UNCLM"
											.equalsIgnoreCase(pwtBean
													.getInUnclmed())) {
								String tableType;
								if ("IN_PLR_UNCLM".equalsIgnoreCase(pwtBean
										.getInUnclmed()))
									tableType = "PLAYER";
								else
									tableType = "AGENT";

								commonHelper.updateOrgForUnClaimedVirn(
										agentOrgId, "AGENT", encodedVirn,
										"DONE_UNCLM", gameId, tableType,
										connection,pwtBean.getTicketNumber());
								debitUnclaim += pwtAmt;
							}

							// update the remaining prize list
							GameUtilityHelper.updateNoOfPrizeRem(gameId,
									pwtAmt, "CLAIM_AGT", encodedVirn,
									connection, gameNbr);

							invPstmt.setInt(1, gameNbr);
							invPstmt.setInt(2, gameId);
							invPstmt.setString(3, encodedVirn);
							invPstmt.execute();
							System.out
									.println("BO PWT table update Query is = "
											+ invPstmt);
						}

					}

					// for current credit amount update
					
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "PWT", agentOrgId,
							0, "AGENT", 0, connection);
					
					if(!isValid)
						throw new LMSException();
					/*boolean isUpdateDone = OrgCreditUpdation
							.updateCreditLimitForAgent(agentOrgId, "PWT",
									creditAmt, connection);*/
					// update retailer UNCLAIM_BAL payment
					
					
					

					// update AGENT's UNCLAIM_BAL Balance
					if (creditAmt > 0.0)
						OrgCreditUpdation.updateOrganizationBalWithValidate(debitUnclaim, "UNCLAIM_BAL", "DEBIT", agentOrgId,
								0, "AGENT", 0, connection);
					
						/*commonHelper.updateOrgBalance("UNCLAIM_BAL",
								debitUnclaim, agentOrgId, "DEBIT", connection);*/

					// generate receipt for BO
					ArrayList<Long> transList = new ArrayList<Long>();
					transList.add(trnId);
					receipts = generateReceiptBo(connection, agentOrgId,
							"AGENT", transList);
					// receiptArr = receipts.split("-");

					boReceiptId = Integer.parseInt(receipts.split("-")[0]);
					if (boReceiptId > 0) {
						connection.commit();
						GraphReportHelper graphReportHelper = new GraphReportHelper();
						graphReportHelper.createTextReportBO(boReceiptId,
								boOrgName, userOrgID, rootPath);
					}

				}
			}
			return receipts;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new LMSException(e);
			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (masterPstmt != null)
					masterPstmt.close();
				if (detailPstmt != null)
					detailPstmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}
	}

	// This function is overloaded because connection is passed whenever it is
	// called

	public String boEndAgtPWTPaymentProcess(List<PwtBean> pwtList, int gameId,
			String boOrgName, int userOrgID, int agentOrgId, int agtUserId,
			String rootPath, int userId, int gameNbr, Connection connection)
			throws LMSException {

		// Connection connection = null;
		PreparedStatement masterPstmt = null;
		PreparedStatement LMSMasterPstmt = null;
		PreparedStatement detailPstmt = null;
		PreparedStatement invPstmt = null;
		//PreparedStatement agtPwtUpdatePstmt = null;

		ResultSet resultSet = null;

		long trnId = -1;
		int boReceiptId = -1;
		// String receiptArr []=null;
		String receipts = null;
		try {
			if (pwtList != null) {
				int size = pwtList.size();
				PwtBean pwtBean = null;

				String masterQuery = null;
				String LMSMasterQuery = null;
				String detailQuery = null;
				String invQuery = null;

				double pwtAmt = 0.0;
				double commAmt = 0.0;
				double netAmt = 0.0;
				double creditAmt = 0.0;
				double debitUnclaim = 0.0;

				if (size > 0) {

					// DBConnect dbConnect = new DBConnect();
					// connection = dbConnect.getConnection();
					connection.setAutoCommit(false);

					// get agents game pwtCommRate
					CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
					double agtPwtCommRate = commonHelper
							.fetchCommOfOrganization(gameId, agentOrgId, "PWT",
									"AGENT", connection);

					masterQuery = QueryManager.insertInBOTransactionMaster();
					masterPstmt = connection.prepareStatement(masterQuery);

					detailQuery = QueryManager.getST1PwtBODetailQuery();
					detailPstmt = connection.prepareStatement(detailQuery);

					invQuery = QueryManager.getST1PWTBOUpdateQuery();
					invPstmt = connection.prepareStatement(invQuery);

					// update st_se_agent_pwt in case if agent brings uncalimed
					// PWTs to BO
					//String agtPwtUpdateQuery = "update  st_se_agent_pwt set status=? where game_id = ? and virn_code = ? and agent_org_id = ? ";
					//agtPwtUpdatePstmt = connection
					//		.prepareStatement(agtPwtUpdateQuery);

					// insert into transaction master
					LMSMasterQuery = QueryManager
							.insertInLMSTransactionMaster();
					LMSMasterPstmt = connection
							.prepareStatement(LMSMasterQuery);
					LMSMasterPstmt.setString(1, "BO");
					LMSMasterPstmt.executeUpdate();
					resultSet = LMSMasterPstmt.getGeneratedKeys();
					if (resultSet.next())
						trnId = resultSet.getLong(1);
					else
						throw new LMSException(
								"Transaction id is not generated");

					// insert into BO transaction master
					masterPstmt.setLong(1, trnId);
					masterPstmt.setInt(2, userId);
					masterPstmt.setInt(3, userOrgID);
					masterPstmt.setString(4, "AGENT");
					masterPstmt.setInt(5, agentOrgId);
					masterPstmt.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					masterPstmt.setString(7, "PWT");
					masterPstmt.execute();
					System.out.println("transaction Id::" + trnId);

					for (int i = 0; i < size; i++) {
						pwtBean = (PwtBean) pwtList.get(i);

						if (pwtBean.getIsValid() && pwtBean.isTicketValid()) {

							String encodedVirn = pwtBean.getEncVirnCode();
							detailPstmt.setString(1, encodedVirn);
							detailPstmt.setLong(2, trnId);
							detailPstmt.setInt(3, gameId);
							detailPstmt.setInt(4, agtUserId);
							detailPstmt.setInt(5, agentOrgId);

							pwtAmt = Double.parseDouble(pwtBean.getPwtAmount());
							commAmt = pwtAmt * agtPwtCommRate * 0.01;
							netAmt = pwtAmt + commAmt;
							// for current credit amount update
							creditAmt += netAmt;

							detailPstmt.setDouble(6, pwtAmt);
							detailPstmt.setDouble(7, commAmt);
							detailPstmt.setDouble(8, netAmt);
							detailPstmt.execute();

							if ("IN_PLR_UNCLM".equalsIgnoreCase(pwtBean
									.getInUnclmed())
									|| "IN_AGENT_UNCLM"
											.equalsIgnoreCase(pwtBean
													.getInUnclmed())) {
								String tableType;
								if ("IN_PLR_UNCLM".equalsIgnoreCase(pwtBean
										.getInUnclmed()))
									tableType = "PLAYER";
								else
									tableType = "AGENT";

								commonHelper.updateOrgForUnClaimedVirn(
										agentOrgId, "AGENT", encodedVirn,
										"DONE_UNCLM", gameId, tableType,
										connection,pwtBean.getTicketNumber());
								debitUnclaim += pwtAmt;
							}

							// update the remaining prize list
							GameUtilityHelper.updateNoOfPrizeRem(gameId,
									pwtAmt, "CLAIM_AGT", encodedVirn,
									connection, gameNbr);

							invPstmt.setInt(1, gameNbr);
							invPstmt.setInt(2, gameId);
							invPstmt.setString(3, encodedVirn);
							invPstmt.setString(4, MD5Encoder.encode(pwtBean.getTicketNumber()));
							invPstmt.execute();
							System.out
									.println("BO PWT table update Query is = "
											+ invPstmt);
						}

					}

					// for current credit amount update
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "PWT", agentOrgId,
							0, "AGENT", 0, connection);
					
					if(!isValid)
						throw new LMSException();
					
					/*boolean isUpdateDone = OrgCreditUpdation
							.updateCreditLimitForAgent(agentOrgId, "PWT",
									creditAmt, connection);*/

					// update AGENT's UNCLAIM_BAL Balance
					if (creditAmt > 0.0)
						OrgCreditUpdation.updateOrganizationBalWithValidate(debitUnclaim, "UNCLAIM_BAL", "DEBIT", agentOrgId,
								0, "AGENT", 0, connection);
					
						/*commonHelper.updateOrgBalance("UNCLAIM_BAL",
								debitUnclaim, agentOrgId, "DEBIT", connection);*/

					// generate receipt for BO
					ArrayList<Long> transList = new ArrayList<Long>();
					transList.add(trnId);
					receipts = generateReceiptBo(connection, agentOrgId,
							"AGENT", transList);
					// receiptArr = receipts.split("-");

					boReceiptId = Integer.parseInt(receipts.split("-")[0]);
					if (boReceiptId > 0) {
						connection.commit();
						GraphReportHelper graphReportHelper = new GraphReportHelper();
						graphReportHelper.createTextReportBO(boReceiptId,
								boOrgName, userOrgID, rootPath);
					}

				}
			}
			return receipts;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new LMSException(e);
			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (masterPstmt != null)
					masterPstmt.close();
				if (detailPstmt != null)
					detailPstmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}
	}

	public String generateReceiptBo(Connection connection, int partyId,
			String partyType, List<Long> transIdList) throws LMSException {
		try {

			// get latest generated receipt number
			PreparedStatement autoGenRecptPstmtBO = connection
					.prepareStatement(QueryManager.getBOLatestReceiptNb());
			autoGenRecptPstmtBO.setString(1, "RECEIPT");
			ResultSet recieptRsBO = autoGenRecptPstmtBO.executeQuery();

			String autoGeneRecieptNoBO = null;
			if (recieptRsBO.next()) {
				String lastRecieptNoGeneratedBO = recieptRsBO
						.getString("generated_id");
				autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo("RECEIPT",
						lastRecieptNoGeneratedBO, "BO");
			} else {
				autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo("RECEIPT",
						null, "BO");
			}

			// for generating receipt********************

			PreparedStatement boReceiptPstmt = connection
					.prepareStatement(QueryManager.insertInReceiptMaster());
			boReceiptPstmt.setString(1, "BO");
			boReceiptPstmt.executeUpdate();
			int boReceiptId = -1;
			ResultSet boRSet = boReceiptPstmt.getGeneratedKeys();
			if (boRSet.next()) {
				boReceiptId = boRSet.getInt(1);

				boReceiptPstmt = connection.prepareStatement(QueryManager
						.insertInBOReceipts());
				boReceiptPstmt.setInt(1, boReceiptId);
				boReceiptPstmt.setString(2, "RECEIPT");
				boReceiptPstmt.setInt(3, partyId);
				boReceiptPstmt.setString(4, partyType);
				boReceiptPstmt.setString(5, autoGeneRecieptNoBO);
				boReceiptPstmt.setTimestamp(6, Util.getCurrentTimeStamp());
				boReceiptPstmt.execute();

				PreparedStatement boReceiptMappingPstmt = connection
						.prepareStatement(QueryManager
								.insertBOReceiptTrnMapping());
				for (Long transId : transIdList) {
					boReceiptMappingPstmt.setInt(1, boReceiptId);
					boReceiptMappingPstmt.setLong(2, transId);
					boReceiptMappingPstmt.execute();
				}

			}

			return boReceiptId + "-" + autoGeneRecieptNoBO;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public ClmDrawSaleBean getVatAndPpr(Connection con,
			ClmDrawSaleBean saleBean, int gameId) throws SQLException {
		String getGameNbrFromGameMaster = "select game_nbr,prize_payout_ratio,vat_amt from st_dg_game_master where game_id ="
				+ gameId;
		PreparedStatement pstmtGameNbr = con
				.prepareStatement(getGameNbrFromGameMaster);
		ResultSet resultGameNbr = pstmtGameNbr.executeQuery();
		while (resultGameNbr.next()) {
			saleBean.setPricePayRatio(resultGameNbr
					.getDouble("prize_payout_ratio"));
			saleBean.setVatAmt(resultGameNbr.getDouble("vat_amt"));
		}
		return saleBean;
	}
	
	public void updateGameInvDetail(int gameId,String packNbr,String bookNbr,String currentOwner,int orgId,int userId,String bookStatus,int warehouseId,Connection con){
		Statement stmt=null;
		String query=null;
		try{
			stmt=con.createStatement();
			query="insert into st_se_game_inv_detail (game_id,pack_nbr,book_nbr,current_owner,current_owner_id,transaction_date,transaction_at,changed_by_user_id,book_status,warehouse_id) values ("+gameId+",'"+packNbr+"','"+bookNbr+"','"+currentOwner+"',"+orgId+",'"+new Timestamp(Calendar.getInstance().getTimeInMillis())+"','"+currentOwner+"',"+userId+",'"+bookStatus+"',"+warehouseId+")";
			stmt.executeUpdate(query);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Map<Integer, String> fetchWarehouseMap() throws ScratchException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, String> warehouseMap = new HashMap<Integer, String>();

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select warehouse_id, warehouse_name from st_se_warehouse_master where warehouse_type='FIXED';");
			while (rs.next()) {
				warehouseMap.put(rs.getInt("warehouse_id"), rs.getString("warehouse_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return warehouseMap;
	}
	
	public void updatePwtDateAndTierTickets(TicketBean tktBean, Connection con) {
		Statement stmt=null;
		String query=null;
		try {
			stmt=con.createStatement();
			query="update st_se_game_inv_status set total_low_win_tier_tickets_claimed=total_low_win_tier_tickets_claimed+1,first_ticket_claim_date = ifnull(first_ticket_claim_date, '"+new Timestamp(Calendar.getInstance().getTimeInMillis())+"') where book_nbr='"+tktBean.getBook_nbr()+"';";
			stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMail(List<String> bookNumberList, String firstName, String lastName, String emailId, String invoiceReceipt){
		try{
			String books=null;
			for (String bookNbr : bookNumberList) 
				 books=bookNbr+",";

			StringBuilder emailMsgTxt = new StringBuilder("<html><table><tr><td colspan=2>Hi ");
				emailMsgTxt.append(firstName)
				.append(" ")
				.append(lastName)
				.append("</td></tr><tr><td colspan=2>")
				.append("Your invoice is successfully generated.<br /></td></tr><tr>")
				.append("<td>Book Number : </td><td><b>")
				.append(books.substring(0, books.length()-1))
				.append("</b></td></tr><tr><td>Invoice Receipt Id : </td><td><b>")
				.append(invoiceReceipt)
				.append("</b></td>")
				.append("</td></tr></table></html>");

				MailSend mailSend = new MailSend(emailId, emailMsgTxt.toString());
				mailSend.setDaemon(true);
				mailSend.start();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}