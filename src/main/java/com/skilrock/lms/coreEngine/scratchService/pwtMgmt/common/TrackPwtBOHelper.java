package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.TrackTicketBean;
import com.skilrock.lms.beans.TrackVirnBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.MD5Encoder;

public class TrackPwtBOHelper {
	private TrackTicketBean checkTicketStatus(String gameNbr, String bookNbr,
			String ticketNbrDigit, int gameId, Connection connection,
			TrackTicketBean trackTicketBean) throws LMSException {
		// TicketBean bean=new TicketBean();
		// TrackTicketBean trackTicketBean = new TrackTicketBean();
		// bean.setTicketNumber(bookNbr+"-"+ticketNbrDigit);
		// System.out.println("Book No is: "+bookNbr+" actual ticket number is "
		// + ticketNbrDigit);

		PreparedStatement pstmt;
		ResultSet result;
		try {
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			pstmt = connection.prepareStatement(query1);
			pstmt.setInt(1, gameId);
			pstmt.setString(2, bookNbr);
			result = pstmt.executeQuery();
			System.out.println("pstmt == " + pstmt);
			if (result.next()) {

				String query2 = QueryManager
						.getST4PwtTicketDetailsUsingGameNbr();
				PreparedStatement pstmt2 = connection.prepareStatement(query2);
				pstmt2.setInt(1, Integer.parseInt(gameNbr));
				pstmt2.setString(2, bookNbr + "-" + ticketNbrDigit);
				ResultSet resultSet2 = pstmt2.executeQuery();

				// required to be reviewed
				if (resultSet2.next()) {
					String ticketStatus = resultSet2.getString("status");

					if (ticketStatus.equalsIgnoreCase("MISSING")) {
						trackTicketBean.setClaimed_by("NONE");
						trackTicketBean.setStatus("MISSING");
						trackTicketBean.setRemarks("Ticket is MISSING ");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_RET")) {
						trackTicketBean.setClaimed_by("RETAILER");
						trackTicketBean.setStatus("CLAIMED");
					} else if (ticketStatus.equalsIgnoreCase("RETURN")) {
						trackTicketBean.setStatus("UNCLAIMED");
						trackTicketBean
								.setRemarks("Ticket once Verified and Return to Player for Verification as Direct Player PWT");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT")) {
						trackTicketBean.setClaimed_by("AGENT");
						trackTicketBean.setClaimed_at("BO");
						trackTicketBean.setStatus("CLAIMED");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
						trackTicketBean.setClaimed_by("AGENT");
						trackTicketBean.setClaimed_at("BO as Bulk");
						trackTicketBean.setStatus("UNCLAIMED");
						trackTicketBean
								.setRemarks("Already Verified in Bulk Receipt at BO");
					} else if (ticketStatus.equalsIgnoreCase("CLAIM_PLR")) {
						trackTicketBean.setStatus("CLAIMED To Player");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
						trackTicketBean.setClaimed_by("PLAYER");
						trackTicketBean.setClaimed_at("RETAILER as Unclaimed");
						trackTicketBean.setStatus("CLAIMED");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
						trackTicketBean.setClaimed_by("PLAYER");
						trackTicketBean.setClaimed_at("AGENT as Unclaimed");
						trackTicketBean.setStatus("CLAIMED");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
						trackTicketBean.setClaimed_by("PLAYER");
						trackTicketBean.setClaimed_at("AGENT");
						trackTicketBean.setStatus("CLAIMED");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
						trackTicketBean.setClaimed_by("PLAYER");
						trackTicketBean.setClaimed_at("RETAILER");
						trackTicketBean.setStatus("CLAIMED");
					} else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
						trackTicketBean.setClaimed_by("PLAYER");
						trackTicketBean.setClaimed_at("RETAILER");
						trackTicketBean.setStatus("CLAIMED");
					}

					else if (ticketStatus
							.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
						trackTicketBean.setClaimed_by("PLAYER");
						trackTicketBean.setClaimed_at("RETAILER As Unclimed");
						trackTicketBean.setStatus("CLAIMED");
					}

					else {
						System.out
								.println("Ticket found in ticket table but Undefined Status of Ticket::  "
										+ ticketStatus);
						trackTicketBean
								.setRemarks("Ticket found in ticket table but Undefined Status of Ticket::  "
										+ ticketStatus);
					}

				} else {
					System.out.println("Ticket Not Found in Ticket Table ");
					trackTicketBean.setRemarks("UNDISCLOSED");
				}
			} else {
				System.out.println("Ticket Is not of the company.");
				trackTicketBean.setRemarks("UNDISCLOSED");
			}
			return trackTicketBean;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	/*
	 * private int gatGameIdFromGameNbr(int gameNbr) throws LMSException{
	 * Connection connection = null; DBConnect dbConnect = new DBConnect();
	 * connection = dbConnect.getConnection(); PreparedStatement pstmt;
	 * ResultSet result; int game_id=0; try{ pstmt =
	 * connection.prepareStatement("select game_id from st_se_game_master where
	 * game_nbr=?"); pstmt.setInt(1, gameNbr); result = pstmt.executeQuery();
	 * if(result.next()) game_id= result.getInt("game_id"); return game_id;
	 * }catch(SQLException e){ e.printStackTrace(); throw new LMSException(); } }
	 */

	/**
	 * It is used to validate the format of ticket enter by user. This is common
	 * method for all('BO', 'AGENT', 'RETAILER')
	 * 
	 * @param ticketNbrDigit
	 * @param gameId
	 * @param connection
	 * @return TicketBean, return bean if format is not valid otherwise 'NULL'.
	 * @throws SQLException
	 */
	private TrackTicketBean isTicketFormatValid(
			TrackTicketBean trackTicketBean, int gameId, Connection connection)
			throws LMSException {

		String ticketNbr = trackTicketBean.getTicket_nbr();
		String getTicketFormatQuery = QueryManager
				.getST4GameTicketDetailsUsingGameId();
		PreparedStatement pstmt;
		ResultSet result;
		try {
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

				if (ticketNbr.indexOf("-") == -1
						&& ticketNbr.length() == gameNbrDigits + packNbrDigits
								+ bookNbrDigits + tktNoDigit) {
					ticketNbr = ticketNbr.substring(0, gameNbrDigits) + "-"
							+ ticketNbr.substring(gameNbrDigits);
					ticketNbr = ticketNbr.substring(0, gameNbrDigits
							+ packNbrDigits + bookNbrDigits + 1)
							+ "-"
							+ ticketNbr.substring(gameNbrDigits + packNbrDigits
									+ bookNbrDigits + 1);
					trackTicketBean.setTicket_nbr(ticketNbr);
				} else if (ticketNbr.split("-").length < 3) {
					trackTicketBean.setIsTicketFormatValid("false");
					return trackTicketBean;
				}

				String tktArr[] = ticketNbr.split("-");
				if (noOfTktPerBook < Integer.parseInt(tktArr[2])
						|| Integer.parseInt(tktArr[2]) == 0
						|| tktNoDigit != tktArr[2].length()) {
					trackTicketBean.setIsTicketFormatValid("false");
					return trackTicketBean;
				}
				trackTicketBean.setIsTicketFormatValid("true");
				return trackTicketBean;

			} else {
				trackTicketBean.setIsTicketFormatValid("false");
				return trackTicketBean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	private TrackTicketBean trackTicket(int gameNbr, int game_id,
			TrackTicketBean trackTicketBean) throws LMSException {
		// check the format of these tickets

		Connection connection = null;
		DBConnect dbConnect = new DBConnect();
		connection = dbConnect.getConnection();
		// TrackTicketBean trackTicketBean = new TrackTicketBean();
		trackTicketBean = isTicketFormatValid(trackTicketBean, game_id,
				connection);

		String tktNbrArr[] = null;
		if ("true".equalsIgnoreCase(trackTicketBean.getIsTicketFormatValid())) {
			tktNbrArr = trackTicketBean.getTicket_nbr().split("-");
			trackTicketBean.setBook_nbr(tktNbrArr[0] + "-" + tktNbrArr[1]);
			// trackTicketBean.setIsTicketFormatValid("true");
			trackTicketBean.setGame_id(game_id);
			trackTicketBean = checkTicketStatus(gameNbr + "", tktNbrArr[0]
					+ "-" + tktNbrArr[1], tktNbrArr[2], game_id, connection,
					trackTicketBean);

		} else {
			trackTicketBean.setRemarks("Ticket Format Error");
		}
		return trackTicketBean;
	}

	public List trackTicketNVirn(String ticketNumber, String virnNumber,
			int gameNbr, int game_id, String gameName, String orgName)
			throws LMSException {

		List ticketNVirnBeanList = new ArrayList();
		// int game_id = gatGameIdFromGameNbr(gameNbr);
		TrackTicketBean trackTicketBean = new TrackTicketBean();
		TrackVirnBean trackVirnBean = new TrackVirnBean();

		trackTicketBean.setTicket_nbr(ticketNumber);
		trackTicketBean.setGame_name(gameName);

		trackVirnBean.setVirn_code(virnNumber);
		trackVirnBean.setGame_name(gameName);

		if (!"".equalsIgnoreCase(ticketNumber) && ticketNumber != null) {
			trackTicketBean = trackTicket(gameNbr, game_id, trackTicketBean);
		}

		System.out.println(trackTicketBean.getTicket_nbr());

		if (!"".equalsIgnoreCase(virnNumber) && virnNumber != null && !"".equalsIgnoreCase(ticketNumber) && ticketNumber != null) {
			trackVirnBean = trackVirn(virnNumber, gameNbr, game_id,
					trackVirnBean, orgName,trackTicketBean.getTicket_nbr());
		}
		ticketNVirnBeanList.add(trackTicketBean);
		ticketNVirnBeanList.add(trackVirnBean);
		System.out.println(ticketNVirnBeanList.size());
		return ticketNVirnBeanList;
	}

	private TrackVirnBean trackVirn(String virnNumber, int gameNbr,
			int game_id, TrackVirnBean trackVirnBean, String orgnazationName,String tktNumber)
			throws LMSException {

		Connection connection = null;
		PreparedStatement pstmt = null;

		Statement statement = null;
		ResultSet resultSet = null;
		ResultSet resDetails = null;
		// TrackVirnBean trackVirnBean = new TrackVirnBean();
		try {
			String encodedVirn = MD5Encoder.encode(virnNumber);
			String encodedTktNbr = MD5Encoder.encode(tktNumber);
			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();
			String queryGetVirnDetails = "select a.virn_code,a.status,a.pwt_amt from st_se_pwt_inv_? a where a.game_id=? and a.virn_code=? and a.id1=?";
			pstmt = connection.prepareStatement(queryGetVirnDetails);
			pstmt.setInt(1, gameNbr);
			pstmt.setInt(2, game_id);
			pstmt.setString(3, encodedVirn);
			pstmt.setString(4, encodedTktNbr);
			resultSet = pstmt.executeQuery();
			String prizeStatus = null;

			if (resultSet.next()) {
				// trackVirnBean.setGame_name(resultSet.getString("game_name"));
				// trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
				prizeStatus = resultSet.getString("status");
				if (prizeStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
					String orgname = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
					String retDetailsQuery = "select a.name,c.generated_id,c.receipt_id,e.transaction_date from st_lms_organization_master a,st_se_agent_pwt b,st_lms_agent_receipts c,st_lms_agent_transaction_master e where b.virn_code='"
							+ encodedVirn
							+ "' and b.game_id="
							+ game_id
							+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=b.transaction_id)";
					System.out.println("query for get org name "
							+ retDetailsQuery);
					resDetails = statement.executeQuery(retDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");

					}
					trackVirnBean.setClaimed_at("AGENT");
					trackVirnBean.setClaimed_by("RETAILER: " + orgname);
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);

					// trackVirnBean.setRemarks(receiptNumber);
					trackVirnBean.setRemarks("Already Paid to Retailer: "
							+ orgname + " By Agent on Voucher Number:  "
							+ receiptNumber + " at " + receiptTime);
					// pwtBean.setMessage("Already Paid to Retailer: " +
					// orgname+" on Voucher Number: "+receiptNumber+" at
					// "+receiptTime);

				} else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
					String agtOrgNAme = null;
					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
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
					String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id,c.receipt_id,d.name from st_se_agt_direct_player_pwt a,st_lms_player_master b,st_lms_agent_receipts c,st_lms_organization_master d where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=a.transaction_id) and d.organization_id=a.agent_org_id";
					System.out.println("query for get player name "
							+ plrDetailsQuery);
					resDetails = statement.executeQuery(plrDetailsQuery);
					while (resultSet.next()) {
						playerFirstName = resDetails.getString("first_name");
						playerLastName = resDetails.getString("last_name");
						playercity = resDetails.getString("city");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");
						agtOrgNAme = resDetails.getString("name");

					}
					trackVirnBean.setClaimed_at("AGENT: " + agtOrgNAme);
					trackVirnBean.setClaimed_by("PLAYER: " + playerFirstName
							+ " " + playerLastName + " " + playercity);
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);

					trackVirnBean.setRemarks("Already Paid to Player: "
							+ playerFirstName + " " + playerLastName + " "
							+ playercity + " By Agent: " + agtOrgNAme
							+ " </br> on Voucher Number: " + receiptNumber
							+ " </br>at " + receiptTime);

					// pwtBean.setMessage("Already Paid to Retailer: " +
					// orgname+" on Voucher Number: "+receiptNumber+" at
					// "+receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
					String orgname = null;
					String receiptNumber = null;
					String receiptTime = null;
					statement = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";

					String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
							+ encodedVirn
							+ "' and game_id="
							+ game_id
							+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";

					System.out.println("query for get org name "
							+ partyDetailsQuery);
					resDetails = statement.executeQuery(partyDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptNumber = resDetails.getString("receipt_id");
						receiptTime = resDetails.getString("date_entered");
					}

					trackVirnBean
							.setClaimed_at("BO in Bulk, Final Payment Pending");
					trackVirnBean.setClaimed_by("AGENT: " + orgname);
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED As Bulk");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);
					trackVirnBean
							.setRemarks("Already Verified in Bulk Receipt at BO for agent: "
									+ orgname
									+ "</br> on Bulk Receipt Number: "
									+ receiptNumber
									+ "</br> at "
									+ receiptTime
									+ ",</br> Final Payment Pending");
					// pwtBean.setMessage("Already Verified in Bulk Receipt at
					// BO for agent: "+orgname+" on Bulk Receipt Number:
					// "+receiptNumber+" at "+receiptTime+", Final Payment
					// Pending");

				} /*
					 * else if
					 * (prizeStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) { }
					 */else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT")) {
					String orgname = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";

					String agtDetailsQuery = "select a.name,c.generated_id,c.receipt_id,e.transaction_date from st_lms_organization_master a,st_se_bo_pwt b,st_lms_bo_receipts c,st_lms_bo_transaction_master e where b.virn_code='"
							+ encodedVirn
							+ "' and b.game_id="
							+ game_id
							+ " and a.organization_id=b.agent_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=b.transaction_id)";

					System.out.println("query for get org name "
							+ agtDetailsQuery);
					resDetails = statement.executeQuery(agtDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");

					}
					trackVirnBean.setClaimed_at("BO");
					trackVirnBean.setClaimed_by("AGENT: " + orgname);
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);

					trackVirnBean.setRemarks("Already Paid to Agent: "
							+ orgname + " By BO </br>on Voucher Number: "
							+ receiptNumber + " </br>at " + receiptTime);
					// pwtBean.setMessage("Already Paid to Agent: " + orgname+"
					// on Voucher Number: "+receiptNumber+" at "+receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
					String orgname = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";

					String agtDetailsQuery = "select a.name,c.generated_id,c.receipt_id,e.transaction_date from st_lms_organization_master a,st_se_bo_pwt b,st_lms_bo_receipts c,st_lms_bo_transaction_master e where b.virn_code='"
							+ encodedVirn
							+ "' and b.game_id="
							+ game_id
							+ " and a.organization_id=b.agent_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=b.transaction_id)";

					System.out.println("query for get org name "
							+ agtDetailsQuery);
					resDetails = statement.executeQuery(agtDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");

					}
					trackVirnBean.setClaimed_at("BO");
					trackVirnBean.setClaimed_by("AGENT: " + orgname);
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED as Uto Scrap");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);

					trackVirnBean
							.setRemarks("Already Paid as Auto Scrap to Agent: "
									+ orgname
									+ " By BO </br>on Voucher Number: "
									+ receiptNumber + " </br>at " + receiptTime);

					// pwtBean.setMessage("Already Paid to Agent: " + orgname+"
					// on Voucher Number: "+receiptNumber+" at "+receiptTime);

				}/*
					 * else if (prizeStatus
					 * .equalsIgnoreCase("CLAIM_PLR_TEMP")){ String
					 * playerFirstName=null; String playerLastName=null; String
					 * playercity=null; String receiptNumber=null; Timestamp
					 * receiptTime=null; statement=connection.createStatement();
					 * //String plrDetailsQuery="select
					 * first_name,last_name,city from st_lms_player_master where
					 * player_id in (select player_id from
					 * st_se_direct_player_pwt where virn_code='"+enVirnCode+"'
					 * and game_id="+gameId+")"; String plrDetailsQuery="select
					 * a.pwt_receipt_id,a.transaction_date,b.first_name,b.last_name,b.city
					 * from st_se_direct_player_pwt_temp_receipt
					 * a,st_lms_player_master b where
					 * a.virn_code='"+encodedVirn+"' and a.game_id="+game_id+"
					 * and b.player_id=a.player_id"; System.out.println("query
					 * for get player name " + plrDetailsQuery);
					 * resOrgDetails=statement.executeQuery(plrDetailsQuery);
					 * while(resOrgDetails.next()){ playerFirstName=
					 * resOrgDetails.getString("first_name"); playerLastName=
					 * resOrgDetails.getString("last_name"); playercity=
					 * resOrgDetails.getString("city");
					 * receiptNumber=resOrgDetails.getString("pwt_receipt_id");
					 * receiptTime=resOrgDetails.getTimestamp("transaction_date"); }
					 * //pwtBean.setMessage("Already in Process for Direct
					 * Player PWT for Player: "+playerFirstName+"
					 * "+playerLastName+","+playercity+" on Temporary Receipt
					 * Number: "+receiptNumber+" issued on
					 * "+receiptTime+",Payment/Approval Pending");
					 * 
					 * }else if(prizeStatus .equalsIgnoreCase("CLAIM_PLR")){
					 * String playerFirstName=null; String playerLastName=null;
					 * String playercity=null; String receiptNumber=null;
					 * Timestamp receiptTime=null;
					 * statement=connection.createStatement(); //String
					 * plrDetailsQuery="select first_name,last_name,city from
					 * st_lms_player_master where player_id in (select player_id
					 * from st_se_direct_player_pwt where
					 * virn_code='"+enVirnCode+"' and game_id="+gameId+")";
					 * String plrDetailsQuery="select
					 * b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id
					 * from st_se_direct_player_pwt a,st_lms_player_master
					 * b,st_lms_bo_receipts c where
					 * a.virn_code='"+encodedVirn+"' and a.game_id="+game_id+"
					 * and a.player_id=b.player_id and c.receipt_id=(select
					 * receipt_id from st_lms_bo_receipts_trn_mapping where
					 * transaction_id=a.transaction_id)";
					 * System.out.println("query for get player name " +
					 * plrDetailsQuery);
					 * resOrgDetails=statement.executeQuery(plrDetailsQuery);
					 * while(resOrgDetails.next()){ playerFirstName=
					 * resOrgDetails.getString("first_name"); playerLastName=
					 * resOrgDetails.getString("last_name"); playercity=
					 * resOrgDetails.getString("city");
					 * receiptNumber=resOrgDetails.getString("generated_id");
					 * receiptTime=resOrgDetails.getTimestamp("transaction_date"); }
					 * //pwtBean.setMessage("Already Paid as Direct Player PWT
					 * to Player: "+playerFirstName+" "+playerLastName+ ","
					 * +playercity+" on Voucher Number "+receiptNumber+" at
					 * "+receiptTime); }
					 */else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
					// show the agent details and player details

					String agtOrgNAme = null;
					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
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
					String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id,c.receipt_id,d.name from st_se_agt_direct_player_pwt a,st_lms_player_master b,st_lms_agent_receipts c,st_lms_organization_master d where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=a.transaction_id) and d.organization_id=a.agent_org_id";
					System.out.println("query for get player name "
							+ plrDetailsQuery);
					resDetails = statement.executeQuery(plrDetailsQuery);
					while (resDetails.next()) {
						playerFirstName = resDetails.getString("first_name");
						playerLastName = resDetails.getString("last_name");
						playercity = resDetails.getString("city");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");
						agtOrgNAme = resDetails.getString("name");

					}
					trackVirnBean.setClaimed_at("AGENT: " + agtOrgNAme);
					trackVirnBean.setClaimed_by("PLAYER: " + playerFirstName
							+ " " + playerLastName + " " + playercity);
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);

					trackVirnBean.setRemarks("Already Paid to Player: "
							+ playerFirstName + " " + playerLastName + " "
							+ playercity + " By Agent: " + agtOrgNAme
							+ " </br> on Voucher Number: " + receiptNumber
							+ " </br>at " + receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {

					String orgname = null;
					String receiptNumber = null;
					String receiptTime = null;
					statement = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";

					String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
							+ encodedVirn
							+ "' and game_id="
							+ game_id
							+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";

					System.out.println("query for get org name "
							+ partyDetailsQuery);
					resDetails = statement.executeQuery(partyDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptNumber = resDetails.getString("receipt_id");
						receiptTime = resDetails.getString("date_entered");
					}

					trackVirnBean.setClaimed_at("BO");
					trackVirnBean.setClaimed_by("AGENT: " + orgname);
					trackVirnBean.setStatus("CLAIMED as Bulk");
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);
					trackVirnBean
							.setRemarks("Already Verified in Bulk By BO For Agent: "
									+ orgname
									+ " </br> on Voucher Number: "
									+ receiptNumber + " </br>at " + receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {

					String orgname = null;
					String receiptNumber = null;
					String receiptTime = null;
					statement = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";

					String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
							+ encodedVirn
							+ "' and game_id="
							+ game_id
							+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";

					System.out.println("query for get org name "
							+ partyDetailsQuery);
					resDetails = statement.executeQuery(partyDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptNumber = resDetails.getString("receipt_id");
						receiptTime = resDetails.getString("date_entered");
					}

					trackVirnBean.setClaimed_at("BO");
					trackVirnBean.setClaimed_by("AGENT: " + orgname);
					trackVirnBean.setStatus("CLAIMED as Bulk");
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);
					trackVirnBean
							.setRemarks("Already Verified in Bulk By BO For Agent: "
									+ orgname
									+ " </br> on Voucher Number: "
									+ receiptNumber + " </br>at " + receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_BO")) {
					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
					// String plrDetailsQuery="select first_name,last_name,city
					// from st_lms_player_master where player_id in (select
					// player_id from st_se_direct_player_pwt where
					// virn_code='"+enVirnCode+"' and game_id="+gameId+")";
					String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id,c.receipt_id from st_se_direct_player_pwt a,st_lms_player_master b,st_lms_bo_receipts c where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=a.transaction_id)";
					System.out.println("query for get player name "
							+ plrDetailsQuery);
					resDetails = statement.executeQuery(plrDetailsQuery);
					while (resDetails.next()) {
						playerFirstName = resDetails.getString("first_name");
						playerLastName = resDetails.getString("last_name");
						playercity = resDetails.getString("city");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");

					}
					trackVirnBean.setClaimed_at("BO");
					trackVirnBean.setClaimed_by("PLAYER" + playerFirstName
							+ " " + playerLastName + " " + playercity);
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);

					trackVirnBean.setRemarks("Already Paid to Player: "
							+ playerFirstName + " " + playerLastName + " "
							+ playercity + " By BO </br> on Voucher Number: "
							+ receiptNumber + " </br>at " + receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
					// show info of retailer if available from at retailer PWT

					String orgname = null;
					String receiptTime = null;
					statement = connection.createStatement();
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
							+ encodedVirn
							+ "' and b.game_id="
							+ game_id
							+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id";

					System.out.println("query for get org name "
							+ retDetailsQuery);
					resDetails = statement.executeQuery(retDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptTime = resDetails.getString("transaction_date");
					}

					trackVirnBean.setClaimed_at("RETAILER: " + orgname);
					trackVirnBean.setClaimed_by("PLAYER");
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("CLAIMED as unclaimmable");
					trackVirnBean
							.setRemarks("Already Paid to Player:  By Retailer </br> on Voucher Number:  </br>at "
									+ receiptTime);
					// pwtBean.setMessage("This VIRN No. has been paid to Player
					// by retailer but not claimed by retailer to agent ");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
					String orgname = null;
					String receiptTime = null;
					statement = connection.createStatement();
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
							+ encodedVirn
							+ "' and b.game_id="
							+ game_id
							+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id";

					System.out.println("query for get org name "
							+ retDetailsQuery);
					resDetails = statement.executeQuery(retDetailsQuery);
					while (resDetails.next()) {
						orgname = resDetails.getString("name");
						receiptTime = resDetails.getString("transaction_date");
					}

					trackVirnBean.setClaimed_at("RETAILER: " + orgname);
					trackVirnBean.setClaimed_by("PLAYER");
					trackVirnBean.setClaimed_on_date(receiptTime);
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("CLAIMED As Claimmable");
					trackVirnBean
							.setRemarks("Already Paid to Player by Retailer: "
									+ orgname + " as Clammable");

				} else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
					// show info of retailer if available from at retailer
					// direct player
					trackVirnBean.setClaimed_at("RETAILER");
					trackVirnBean.setClaimed_by("PLAYER");
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("CLAIM_PLR_RET_UNCLM_DIR");
					// pwtBean.setMessage("This VIRN No. has been paid to Player
					// by retailer but not claimed by retailer to agent ");

				} else if (prizeStatus
						.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
					// show info of retailer if available from at retailer
					// derect palyer PWT
					trackVirnBean.setClaimed_at("RETAILER");
					trackVirnBean.setClaimed_by("PLAYER");
					trackVirnBean.setStatus("CLAIMED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("CLAIM_PLR_RET_CLM_DIR");
					// pwtBean.setMessage("This VIRN No. has been paid to Player
					// by retailer As Claimmable ");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_CLM")) {
					String agtOrgname = null;
					String retOrgName = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
					String agtDetailsQuery = "select  b.transaction_date,c.generated_id,c.receipt_id,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'agt_name' ,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'ret_name'  from st_se_agent_pwt a,st_lms_agent_transaction_master b,st_lms_agent_receipts c where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.transaction_id=b.transaction_id  and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=b.transaction_id)";

					System.out.println("query for get org name "
							+ agtDetailsQuery);
					resDetails = statement.executeQuery(agtDetailsQuery);
					while (resDetails.next()) {
						agtOrgname = resDetails.getString("agt_name");
						retOrgName = resDetails.getString("ret_name");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");

					}
					trackVirnBean.setClaimed_at("AGENT: " + agtOrgname);
					trackVirnBean.setClaimed_by("RETAILER: " + retOrgName);
					trackVirnBean.setStatus("CLAIMED As Clammiable");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks(receiptNumber);

					trackVirnBean.setRemarks("Paid to retailer: " + retOrgName
							+ " By Agent: " + agtOrgname + "<br> on vocher "
							+ receiptNumber + " on date " + receiptTime);
					// pwtBean.setMessage("Paid to retailer By Agent and pending
					// to claim at bo by agent as uato sarap");

				} else if (prizeStatus.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
					String agtOrgname = null;
					String retOrgName = null;
					String receiptNumber = null;
					String receiptTime = null;

					statement = connection.createStatement();
					String agtDetailsQuery = "select  b.transaction_date,c.generated_id,c.receipt_id,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'agt_name' ,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'ret_name'  from st_se_agent_pwt a,st_lms_agent_transaction_master b,st_lms_agent_receipts c where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.transaction_id=b.transaction_id  and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=b.transaction_id)";

					System.out.println("query for get org name "
							+ agtDetailsQuery);
					resDetails = statement.executeQuery(agtDetailsQuery);
					while (resDetails.next()) {
						agtOrgname = resDetails.getString("agt_name");
						retOrgName = resDetails.getString("ret_name");
						receiptNumber = resDetails.getString("generated_id");
						receiptTime = resDetails.getString("transaction_date");

					}
					trackVirnBean.setClaimed_at("AGENT: " + agtOrgname);
					trackVirnBean.setClaimed_by("RETAILER: " + retOrgName);
					trackVirnBean.setStatus("CLAIMED As Auto Scrap");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);

					trackVirnBean.setRemarks(receiptNumber);
					trackVirnBean.setRemarks("Claimed  to retailer: "
							+ retOrgName + " by agent: " + agtOrgname
							+ " with receipt: " + receiptNumber + " on "
							+ receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("REQUESTED")) {
					String reqByOrgName = null;
					String reqToOrgName = null;
					String reqByType = null;
					String reqToType = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement = connection.createStatement();
					String reqDetailsQuery = "select a.requester_type,a.requested_to_type,a.request_id,a.request_date,(select name from st_lms_organization_master where organization_id=a.requested_by_org_id) as reqByName,(select name from st_lms_organization_master where organization_id=a.requested_to_org_id) as reqToName from st_se_pwt_approval_request_master a where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.req_status='REQUESTED'";

					System.out.println("query for get org name "
							+ reqDetailsQuery);
					resDetails = statement.executeQuery(reqDetailsQuery);
					while (resDetails.next()) {
						reqByOrgName = resDetails.getString("reqByName");
						reqToOrgName = resDetails.getString("reqToName");
						reqByType = resDetails.getString("requester_type");
						reqToType = resDetails.getString("requested_to_type");
						receiptNumber = resDetails.getString("request_id");
						receiptTime = resDetails.getTimestamp("request_date");
					}
					trackVirnBean.setStatus("REQUESTED");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("REQUESTED");

					trackVirnBean.setRemarks("This VIRN is requested by "
							+ reqByType + ": " + reqByOrgName + "To "
							+ reqToType + ": " + reqToOrgName
							+ " for Approval <br> Voucher id: " + receiptNumber
							+ " on " + receiptTime);

				} else if (prizeStatus.equalsIgnoreCase("PND_MAS")) {
					// details of by and to and date and voucher

					String reqByOrgName = null;
					String reqToOrgName = null;
					String reqByType = null;
					String reqToType = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement = connection.createStatement();
					String reqDetailsQuery = "select a.requester_type,a.requested_to_type,a.request_id,a.request_date,(select name from st_lms_organization_master where organization_id=a.requested_by_org_id) as reqByName,(select name from st_lms_organization_master where organization_id=a.requested_to_org_id) as reqToName from st_se_pwt_approval_request_master a where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.req_status='PND_MAS'";

					System.out.println("query for get org name "
							+ reqDetailsQuery);
					resDetails = statement.executeQuery(reqDetailsQuery);
					while (resDetails.next()) {
						reqByOrgName = resDetails.getString("reqByName");
						reqToOrgName = resDetails.getString("reqToName");
						reqByType = resDetails.getString("requester_type");
						reqToType = resDetails.getString("requested_to_type");
						receiptNumber = resDetails.getString("request_id");
						receiptTime = resDetails.getTimestamp("request_date");
					}
					trackVirnBean.setStatus("PND_MAS");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("PND_MAS");
					trackVirnBean.setRemarks("This VIRN is requested by "
							+ reqByType + ": " + reqByOrgName + "To "
							+ reqToType + ": " + reqToOrgName
							+ " for master Approval <br> Voucher id: "
							+ receiptNumber + " on " + receiptTime);
					// pwtBean.setMessage("This VIRN has Beean requested for BO
					// Master Approval");
				} else if (prizeStatus.equalsIgnoreCase("PND_PAY")) {
					String reqToOrgName = null;
					String reqToType = null;
					String receiptNumber = null;
					statement = connection.createStatement();
					String reqDetailsQuery = "select a.request_id,a.pay_req_for_org_type,(select name from st_lms_organization_master where organization_id=a.pay_request_for_org_id) as payByName from st_se_pwt_approval_request_master a where a.virn_code='"
							+ encodedVirn
							+ "' and a.game_id="
							+ game_id
							+ " and a.req_status='PND_PAY'";

					System.out.println("query for get org name "
							+ reqDetailsQuery);
					resDetails = statement.executeQuery(reqDetailsQuery);
					while (resDetails.next()) {

						reqToOrgName = resDetails.getString("payByName");
						reqToType = resDetails
								.getString("pay_req_for_org_type");
						receiptNumber = resDetails.getString("request_id");
					}
					trackVirnBean.setStatus("PND_PAY");
					trackVirnBean.setPwt_amt(resultSet.getString("pwt_amt"));
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("PND_PAY");
					trackVirnBean.setRemarks("This VIRN is requested To "
							+ reqToType + ": " + reqToOrgName
							+ " for payment <br> Voucher id: " + receiptNumber);
					// pwtBean.setMessage("This VIRN has Beean Approved And
					// Pending to Payment");
				} else if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")) {
					trackVirnBean.setClaimed_at("NA");
					trackVirnBean.setClaimed_by("NA");
					trackVirnBean.setClaimed_on_date("NA");
					trackVirnBean.setStatus("NA");
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("UNDISCLOSED");

				} else if (prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
					trackVirnBean.setClaimed_at("NA");
					trackVirnBean.setClaimed_by("NA");
					trackVirnBean.setClaimed_on_date("NA");
					trackVirnBean.setStatus("NA");
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean.setRemarks("UNDISCLOSED");
				} else if (prizeStatus.equalsIgnoreCase("CANCELLED_PERMANENT")) {
					trackVirnBean.setClaimed_at("NA");
					trackVirnBean.setClaimed_by("NA");
					trackVirnBean.setClaimed_on_date("NA");
					trackVirnBean.setStatus("UNCLAIMED");
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean
							.setRemarks("This VIRN is cancelled Permanently");

				}else if (prizeStatus.equalsIgnoreCase("NO_PRIZE_PWT")) {
					trackVirnBean.setClaimed_at("NA");
					trackVirnBean.setClaimed_by("NA");
					trackVirnBean.setClaimed_on_date("NA");
					trackVirnBean.setStatus("NO_PRIZE_PWT");
					trackVirnBean.setVirn_code(virnNumber);
					trackVirnBean
							.setRemarks("NON WINNING");

				} else {
					System.out.println("UNdefined status for PWT");
					trackVirnBean.setStatus("UNDefined Status For PWT"
							+ prizeStatus);
					CommonMethods
							.sendMail("ERROR :: status not matched in pwt inv table  virn =  "
									+ encodedVirn);
				}
			} else {
				trackVirnBean.setRemarks(" UNDISCLOSED");
			}
			return trackVirnBean;

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
			}
		}

	}

}