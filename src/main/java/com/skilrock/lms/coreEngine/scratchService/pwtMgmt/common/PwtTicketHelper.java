/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

public class PwtTicketHelper {
	String pwtFlag = "Invalid";

	public List<ActiveGameBean> getActiveGames() {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {
			System.out.println("inside try............");
			ActiveGameBean gameBean = null;
			List<ActiveGameBean> searchResults = new ArrayList<ActiveGameBean>();

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			String query = QueryManager.getST4ActiveGamesQuery();
			System.out.println(query);
			int gameNbr;
			String gameName;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {

				gameBean = new ActiveGameBean();
				gameBean.setGameId(resultSet.getInt(TableConstants.GAME_ID));
				gameBean.setRetailerPwtCommRate(0);
				gameNbr = resultSet.getInt(TableConstants.GAME_NBR);
				gameName = resultSet.getString(TableConstants.GAME_NAME);
				gameBean.setGameNbr_Name(gameNbr + delimiter + gameName);

				searchResults.add(gameBean);

			}
			connection.commit();
			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}
		return null;
	}

	public String getBookNbrFromTicketNo(String ticket_nbr) {
		String book_nbr = "";
		StringTokenizer st = new StringTokenizer(ticket_nbr, "-");
		for (int i = 0; i < 2; i++) {
			if (st.hasMoreTokens()) {
				book_nbr = book_nbr + st.nextToken();
				if (i == 0) {
					book_nbr = book_nbr + "-";
				}
			}
		}
		// System.out.println(book_nbr);
		return book_nbr;
	}

	public int getGameId(List<ActiveGameBean> activeGameList,
			String gameNbr_Name) {
		ActiveGameBean bean = null;
		if (activeGameList != null) {
			// System.out.println("Game List Size="+activeGameList.size());
			for (int i = 0; i < activeGameList.size(); i++) {
				// System.out.println(i);
				// System.out.println(gameNbr_Name);
				bean = activeGameList.get(i);
				// System.out.println(bean);
				// System.out.println(gameNbr_Name);
				if (gameNbr_Name.equals(bean.getGameNbr_Name())) {
					return bean.getGameId();
				}
			}
		}

		return 0;
	}

	public int getGameIdFromDataBase(String gameNbr_Name) {
		Connection connection = null;
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String query = QueryManager.getST4GameDetailsUsingGameName();
		int game_id = 0;
		String game_name = null;
		StringTokenizer st = new StringTokenizer(gameNbr_Name, "-");
		for (int i = 0; i < 2; i++) {
			if (st.hasMoreTokens()) {
				game_name = st.nextToken();
			}
		}
		System.out.println("Game Name Is at saved time : " + game_name);

		try {
			 
			connection = DBConnect.getConnection();
			Pstmt = connection.prepareStatement(query);
			Pstmt.setString(1, game_name);
			resultSet = Pstmt.executeQuery();
			while (resultSet.next()) {
				game_id = resultSet.getInt("game_id");
				return game_id;
			}
			System.out.println("Game id is: " + game_id);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (Pstmt != null) {
					Pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	public int getGameNbrFromTicketNo(String ticket_nbr) {
		int gameNbr;
		String[] ticketDetailsArr = ticket_nbr.split("-");
		gameNbr = Integer.parseInt(ticketDetailsArr[0]);
		return gameNbr;
	}

	public List<GameTicketFormatBean> getGameTicketFormatList(
			List<ActiveGameBean> activeGameList) {

		StringBuffer gameIdList = new StringBuffer();
		List<GameTicketFormatBean> gameFormatList = null;
		GameTicketFormatBean gameFormatBean = null;

		Connection connection = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		String query = null;

		for (int i = 0; i < activeGameList.size(); i++) {
			gameIdList.append(activeGameList.get(i).getGameId());
			gameIdList.append(",");
		}

		if (gameIdList.length() > 0) {
			gameIdList.deleteCharAt(gameIdList.length() - 1);

			gameFormatList = new ArrayList<GameTicketFormatBean>();

			 
			connection = DBConnect.getConnection();
			try {

				query = QueryManager.getGameFormatInformation();
				query = query + gameIdList.toString() + " )";

				stmt = connection.createStatement();
				resultSet = stmt.executeQuery(query);

				while (resultSet.next()) {

					gameFormatBean = new GameTicketFormatBean();

					gameFormatBean.setGameId(resultSet
							.getInt(TableConstants.SGTNF_GAME_ID));
					gameFormatBean.setGameNbrDigits(resultSet
							.getInt(TableConstants.SGTNF_GAME_NBR_DIGITS));
					gameFormatBean.setPackDigits(resultSet
							.getInt(TableConstants.SGTNF_PACK_DIGITS));
					gameFormatBean.setBookDigits(resultSet
							.getInt(TableConstants.SGTNF_BOOK_DIGITS));

					gameFormatList.add(gameFormatBean);
				}

			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}

		}
		return gameFormatList;
	}

	public List<TicketBean> getGameWiseVerifiedTickets(List<String> tktList,
			int gameNbr) throws LMSException {
		List<TicketBean> verifyResults = new ArrayList<TicketBean>();
		Connection connection = DBConnect.getConnection();
		try {

			// check the format of these tickets
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			List<TicketBean> tktBeanList = commHelper.isTicketFormatValid(
					tktList, gameNbr, connection);

			BOPwtProcessHelper boPwtHelper = new BOPwtProcessHelper();
			String tktNbrArr[] = null;
			TicketBean bean = null;
			for (TicketBean tktBean : tktBeanList) {
				if (tktBean.getIsValid()) {
					tktNbrArr = tktBean.getTicketNumber().split("-");
					bean = boPwtHelper.checkTicketStatus(gameNbr + "",
							tktNbrArr[0] + "-" + tktNbrArr[1], tktNbrArr[2],
							tktBean.getTicketGameId(), connection, false);
				} else {
					bean = tktBean;
				}
				verifyResults.add(bean);
			}

			return verifyResults;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Problem in Ticket validation; " + e);
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

	public String getPwtFlag() {
		return pwtFlag;
	}

	public Map getTicketList(String[] ticketNumber, String ticketDetails)
			throws LMSException {

		System.out.println("ticket details is  " + ticketDetails);
		Map map = new TreeMap();
		String tktNbr = null;
		// code added by yogesh to read tickets from file
		TicketBean ticketBean = null;

		List<TicketBean> duplicateticketList = new ArrayList<TicketBean>();
		List<String> ticketListString = new ArrayList<String>();

		for (String element : ticketNumber) {
			tktNbr = element.trim();
			if (!"".equals(tktNbr) && tktNbr != null) {
				if (ticketListString.contains(tktNbr.replaceAll("-", ""))) {
					ticketBean = new TicketBean();
					ticketBean.setTicketNumber(tktNbr);
					ticketBean.setValid(false);
					ticketBean.setStatus("Duplicate Ticket Entry in File");
					ticketBean.setMessageCode("122009");
					ticketBean.setValidity("InValid Ticket");
					duplicateticketList.add(ticketBean);

				} else if (!Pattern.matches("([0-9]*-?[0-9]*-?[0-9]*)", tktNbr)) {
					ticketBean = new TicketBean();
					ticketBean.setTicketNumber(tktNbr);
					ticketBean.setValid(false);
					ticketBean
							.setStatus("Number Format Error/Out of Range Please Check");
					ticketBean.setMessageCode("122002");
					ticketBean.setValidity("InValid Ticket");
					duplicateticketList.add(ticketBean);
				} else {
					ticketListString.add(tktNbr.replaceAll("-", ""));
				}
			}
		}

		if (!"".equalsIgnoreCase(ticketDetails) && ticketDetails != null) {
			try {
				InputStreamReader fileStreamReader = new InputStreamReader(
						new FileInputStream(ticketDetails));
				BufferedReader br = new BufferedReader(fileStreamReader);
				String strTicketLine = null;
				int fileVirnLimit = 0;
				while ((strTicketLine = br.readLine()) != null) {
					if ("".equals(strTicketLine.trim())) {
						continue;
					}
					if (fileVirnLimit > 5000) {
						map.put("error", "Data In File Exceeds limit ");
						return map;
					}
					if (ticketListString.contains(strTicketLine.trim()
							.replaceAll("-", ""))) {
						ticketBean = new TicketBean();
						ticketBean.setTicketNumber(strTicketLine.trim());
						ticketBean.setValid(false);
						ticketBean.setStatus("Duplicate Ticket Entry in File");
						ticketBean.setMessageCode("122009");
						ticketBean.setValidity("InValid Ticket");
						duplicateticketList.add(ticketBean);

					} else if (!Pattern.matches("([0-9]*-?[0-9]*-?[0-9]*)",
							strTicketLine.trim())) {
						ticketBean = new TicketBean();
						ticketBean.setTicketNumber(strTicketLine.trim());
						ticketBean.setValid(false);
						ticketBean
								.setStatus("Number Format Error/Out of Range Please Check");
						ticketBean.setMessageCode("122002");
						ticketBean.setValidity("InValid Ticket");
						duplicateticketList.add(ticketBean);
					} else {
						ticketListString.add(strTicketLine.replaceAll("-", ""));
					}
					fileVirnLimit++;
				}

				System.out.println("ticketListString-- " + ticketListString);

				/*
				 * map.put("ticketListString", ticketListString);
				 * map.put("duplicateticketList", duplicateticketList);
				 */
			} catch (FileNotFoundException fe) {
				// fe.printStackTrace();
				// throw new LMSException("file not found exception",fe);
			} catch (IOException ioe) {
				// ioe.printStackTrace();
				// throw new LMSException("IO exception",ioe);
			}
		}
		map.put("ticketListString", ticketListString);
		map.put("duplicateticketList", duplicateticketList);
		System.out.println("ticket list Map " + map);
		return map;
	}

	public String getTicketNbrFromTicketNo(String ticket_nbr) {
		String tkt_nbr = "";
		StringTokenizer st = new StringTokenizer(ticket_nbr, "-");
		for (int i = 0; i < 3; i++) {
			if (st.hasMoreTokens()) {
				tkt_nbr = st.nextToken();
			}
		}
		return tkt_nbr;
	}

	public List<TicketBean> getVerifiedTickets(List<TicketBean> ticketBean,
			int game_id, int gameNbr) {
		List<TicketBean> verifyResults = new ArrayList<TicketBean>();
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		ResultSet resultSet5 = null;
		try {
			System.out.println("inside try............");
			 
			connection = DBConnect.getConnection();
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			// String query2 =
			// QueryManager.getST4PwtTicketDetailsUsingTicketNbr();
			String query2 = QueryManager.getST4PwtTicketDetailsUsingGameNbr();
			String query3 = QueryManager.getST4GameTicketDetailsUsingGameId();
			String query4 = "select * from st_se_tmp_pwt_tickets_inv where ticket_nbr = ?";

			Iterator<TicketBean> iterator = ticketBean.iterator();
			while (iterator.hasNext()) {
				String ticket_nbr = null;
				int size = 0;
				ticket_nbr = iterator.next().getTicketNumber();
				size = ticket_nbr.length();

				if (size != 0) {

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					String book_nbr = getBookNbrFromTicketNo(ticket_nbr);
					// int gameNbr=getGameNbrFromTicketNo(ticket_nbr);
					String actualTicketNbrFromTicketNbr = getTicketNbrFromTicketNo(ticket_nbr);

					// Assumption is made using the Max Length of Integer in
					// JAVA to Avoid Number Format Exception
					if (actualTicketNbrFromTicketNbr.length() > 9) {
						bean
								.setStatus("Number Format Error/Out of Range Please Check");
						bean.setMessageCode("122001");
						bean.setValidity("InValid Ticket");
						verifyResults.add(bean);
						continue;
					}

					int actual_tkt_nbr = Integer
							.parseInt(actualTicketNbrFromTicketNbr);
					// System.out.println("ticket number iss }}}}}}}}}}} " +
					// actualTicketNbrFromTicketNbr + "after parsing to int " +
					// actual_tkt_nbr);
					// System.out.println("Book No is: " + book_nbr);

					preparedStatement1 = connection.prepareStatement(query1);
					preparedStatement1.setInt(1, game_id);
					preparedStatement1.setString(2, book_nbr);
					resultSet1 = preparedStatement1.executeQuery();
					String currentOwner;
					if (resultSet1.next()) {
						currentOwner = resultSet1.getString("current_owner");
						if (resultSet1.getString("current_owner").equals(
								"RETAILER")
								|| resultSet1.getString("current_owner")
										.equals("AGENT")) {

							preparedStatement2 = connection
									.prepareStatement(query2);
							preparedStatement2.setInt(1, gameNbr);
							preparedStatement2.setString(2, ticket_nbr);
							resultSet2 = preparedStatement2.executeQuery();

							if (resultSet2.next()) {

								String ticketStatus = resultSet2
										.getString("status");
								if (ticketStatus.equalsIgnoreCase("CLAIM_RET")) {
									preparedStatement3 = connection
											.prepareStatement(query3);
									preparedStatement3.setInt(1, game_id);
									resultSet3 = preparedStatement3
											.executeQuery();
									if (resultSet3.next()) {
										if (resultSet3
												.getInt("nbr_of_tickets_per_book") < actual_tkt_nbr
												|| actual_tkt_nbr == 000
												|| resultSet3
														.getInt("ticket_nbr_digits") != actualTicketNbrFromTicketNbr
														.length()) {
											bean.setValid(false);
											bean
													.setStatus("Number Format Error /Out of Range Please Check");
											bean.setMessageCode("122001");
											bean.setValidity("InValid Ticket");
											verifyResults.add(bean);
											// System.out.println("Ticket number
											// is fake.");
										} else {

											// check if this ticket in in
											// temporary table
											preparedStatement5 = connection
													.prepareStatement(query4);
											preparedStatement5.setString(1,
													ticket_nbr);
											resultSet5 = preparedStatement5
													.executeQuery();
											if (resultSet5.next()) {
												bean.setValid(false);
												bean
														.setStatus("Already Verified in Bulk Receipt at BO");
												bean.setMessageCode("122007");
												bean
														.setValidity("InValid Ticket");
												verifyResults.add(bean);
												// System.out.println("Ticket
												// number is verified for temp
												// table");

											} else {
												bean.setValid(true);
												bean.setStatus(" ");
												bean.setMessageCode("121002");
												bean
														.setValidity("Ticket Is Valid");
												setPwtFlag("Valid");
												verifyResults.add(bean);
												System.out
														.println("Ticket IS valid for pwt. (Means not fake and not in pwt table)");
											}
										}
									}
								} else if (ticketStatus
										.equalsIgnoreCase("RETURN")) {
									bean.setValid(false);
									bean
											.setStatus("Ticket once Verified and Return to Player for Verification as Direct Player PWT");
									bean.setMessageCode("122006");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									System.out
											.println("Ticket Is valid for pwt. But Ticket Is High Prize Ticket, So Go for Direct PWT.  ");
								} else if (ticketStatus
										.equalsIgnoreCase("MISSING")) {
									bean.setValid(false);
									bean
											.setStatus("Ticket staus is MISSING in DATABASE");
									bean.setMessageCode("222010");
									bean.setValidity("InValid Ticket");
									System.out.println("Ticket is MISSING.");
								} else if (ticketStatus
										.equalsIgnoreCase("CLAIM_AGT")) {
									bean.setValid(false);
									bean.setStatus("Already Paid to Agent");
									bean.setMessageCode("122004");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									System.out
											.println("Ticket's PWT Has Bean Paid By BO.");
								} else if (ticketStatus
										.equalsIgnoreCase("CLAIM_PLR")) {
									bean.setValid(false);
									bean
											.setStatus("Already Paid as Direct Playe PWT");
									bean.setMessageCode("122005");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									System.out
											.println("Ticket Is High Prize Ticket, Ticket's PWT Has Been Paid. ");
								} else {
									bean.setValid(false);
									bean.setStatus("Undefined  Ticket's PWT");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									System.out
											.println("Undefined Status of Ticket's PWT");
								}
							} else {

								// check if this ticket in in temporary table
								preparedStatement5 = connection
										.prepareStatement(query4);
								preparedStatement5.setString(1, ticket_nbr);
								resultSet5 = preparedStatement5.executeQuery();
								if (resultSet5.next()) {
									bean.setValid(false);
									bean
											.setStatus("Already Verified in Bulk Receipt at BO");
									bean.setMessageCode("122007");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									// System.out.println("Ticket number is
									// verified for temp table");

								} else {

									preparedStatement4 = connection
											.prepareStatement(query3);
									preparedStatement4.setInt(1, game_id);
									resultSet4 = preparedStatement4
											.executeQuery();
									if (resultSet4.next()) {
										if (resultSet4
												.getInt("nbr_of_tickets_per_book") < actual_tkt_nbr
												|| actual_tkt_nbr == 000
												|| resultSet4
														.getInt("ticket_nbr_digits") != actualTicketNbrFromTicketNbr
														.length()) {
											bean.setValid(false);
											bean
													.setStatus("Number Format Error/Out of Range Please Check");
											bean.setMessageCode("122001");
											bean.setValidity("InValid Ticket");
											verifyResults.add(bean);
											System.out
													.println("Ticket number is fake.");
										} else if (currentOwner
												.equals("RETAILER")) {
											bean.setValid(true);
											bean.setStatus(" ");
											bean.setMessageCode("121003");
											bean.setValidity("Valid Ticket");
											setPwtFlag("Valid");
											verifyResults.add(bean);
											System.out
													.println("Ticket Is May Be High Prize Ticket or Low Prize Ticket, So Go for Direct PWT or At your Agent.");
										} else {
											bean.setValid(true);
											bean.setStatus(" ");
											bean.setMessageCode("121001");
											bean.setValidity("Valid Ticket");
											setPwtFlag("Valid");
											verifyResults.add(bean);
											System.out
													.println("ticket is valid if agent is not online for test");
										}

									}

									/*
									 * bean.setValid(true); bean
									 * .setStatus("ticket is valid if agent is
									 * not online for test");
									 * bean.setValidity("valid Ticket if agent
									 * is not online"); setPwtFlag("Valid");
									 * verifyResults.add(bean); System.out
									 * .println("ticket is valid if agent is not
									 * online for test");
									 * 
									 */

									/*
									 * bean.setValid(false); bean
									 * .setStatus("Ticket Is May Be High Prize
									 * Ticket or Low Prize Ticket, So Go for
									 * Direct PWT or At your Agent.");
									 * bean.setValidity("InValid Ticket");
									 * verifyResults.add(bean); System.out
									 * .println("Ticket Is May Be High Prize
									 * Ticket or Low Prize Ticket, So Go for
									 * Direct PWT or At your Agent.");
									 */
								}
							}
						} else {
							bean.setValid(false);
							bean.setStatus("Ticket is stiil in stock of BO");
							bean.setMessageCode("122003");
							bean.setValidity("InValid Ticket");
							verifyResults.add(bean);
							System.out.println("Ticket owner is not Retailer.");
						}
					} else {
						bean.setValid(false);
						bean
								.setStatus("Number Format Error/Out of Range Please Check");
						bean.setMessageCode("122002");
						bean.setValidity("InValid Ticket");
						verifyResults.add(bean);
						System.out.println("Ticket Is not of the company.");
					}
				}
			}
			System.out.println("The verified List Is: " + verifyResults);

		} catch (SQLException e) {
			System.out.println("sql exception");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Parse exception");
			e.printStackTrace();
			verifyResults = null;
			System.out.println(verifyResults);
			return verifyResults;

		} finally {
			try {
				if (preparedStatement1 != null) {
					preparedStatement1.close();
				}
				if (preparedStatement2 != null) {
					preparedStatement2.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return verifyResults;

	}

	public List<TicketBean> saveTicketsData(
			List<TicketBean> verifiedTicketList, int userId, int userOrgId,
			int partyOrgId, String channel, String interfaceType)
			throws LMSException {
		Connection connection = DBConnect.getConnection();
		try {
			connection.setAutoCommit(false);
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			for (TicketBean ticketBean : verifiedTicketList) {
				if (ticketBean.getIsValid()) {
					commHelper.updateTicketInvTable(ticketBean
							.getTicketNumber(), ticketBean.getBook_nbr(),
							ticketBean.getGameNbr(), ticketBean
									.getTicketGameId(), "CLAIM_AGT", userId,
							userOrgId, ticketBean.getUpdateTicketType(),
							partyOrgId, channel, interfaceType, connection);

					// update book status from active to claimed in table
					// st_se_game_inv_status table here
					if ("ACTIVE".equalsIgnoreCase(ticketBean.getBookStatus())) {
						commHelper.updateBookStatus(ticketBean
								.getTicketGameId(), ticketBean.getBook_nbr(),
								connection, "CLAIMED");
					}

				}
			}
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return null;
	}

	public void setPwtFlag(String pwtFlag) {
		this.pwtFlag = pwtFlag;
	}

}
