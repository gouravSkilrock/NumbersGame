/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;

/**
 * This class is a helper class having methods used to Direct Player PWT ticket
 * verification.
 * 
 * @author Skilrock Technologies
 * 
 */
public class PwtTicketDirectPlayerHelper {
	static Log logger = LogFactory.getLog(PwtTicketDirectPlayerHelper.class);
	Connection connection;

	/**
	 * This method is used to get Active Games.
	 * 
	 * @return
	 * @throws LMSException
	 */
	public List<ActiveGameBean> getActiveGames() throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {
			logger.debug("inside try............");
			ActiveGameBean gameBean = null;
			List<ActiveGameBean> searchResults = new ArrayList<ActiveGameBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST4ActiveGamesQuery();
			logger.debug(query);
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
			logger.debug(searchResults);
			return searchResults;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

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
	}

	/**
	 * This method is used to get book number from Ticket Number.
	 * 
	 * @param ticket_nbr
	 * @return String
	 */
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
		// logger.debug(book_nbr);
		return book_nbr;
	}

	public Connection getConnectrion() {
		return connection;
	}

	/**
	 * method to get game id for selected game from the active game List.
	 * 
	 * @param activeGameList
	 * @param gameNbr_Name
	 * @return int
	 */
	public int getGameId(List<ActiveGameBean> activeGameList,
			String gameNbr_Name) {
		ActiveGameBean bean = null;
		if (activeGameList != null) {

			for (int i = 0; i < activeGameList.size(); i++) {
				bean = activeGameList.get(i);
				if (gameNbr_Name.equals(bean.getGameNbr_Name())) {
					return bean.getGameId();
				}
			}
		}

		return 0;
	}

	/**
	 * This method is used to get game Id for the game.
	 * 
	 * @param gameNbr_Name
	 * @return int.
	 * @throws LMSException
	 */
	public int getGameIdFromDataBase(String gameNbr_Name) throws LMSException {
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
		logger.debug("Game Name Is at saved time : " + game_name);

		try {
			 
			connection = DBConnect.getConnection();
			Pstmt = connection.prepareStatement(query);
			Pstmt.setString(1, game_name);
			resultSet = Pstmt.executeQuery();
			while (resultSet.next()) {
				game_id = resultSet.getInt("game_id");
				return game_id;
			}
			logger.debug("Game id is: " + game_id);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException();
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
				throw new LMSException();
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

	// added by yogesh to get game digit information
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

			try {
				 
				connection = DBConnect.getConnection();

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
				logger.error("Exception: " + e);
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
					logger.error("Exception: " + e);
					e.printStackTrace();
				}
			}

		}
		return gameFormatList;
	}

	/**
	 * This method is used to get required formatted ticket number from ticket
	 * number(GameNumber-PacknumberBookNumber-TicketNumber).
	 * 
	 * @param ticket_nbr
	 * @return String
	 */
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

	/**
	 * This method is used to get List verified tickets.
	 * 
	 * @param ticketBean
	 * @param game_id
	 * @return List
	 * @throws LMSException
	 */
	public List<TicketBean> getVerifiedTickets(List<TicketBean> ticketBean,
			int game_id) throws LMSException {
		List<TicketBean> verifyResults = new ArrayList<TicketBean>();
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		try {
			logger.debug("inside try............");
			 

			connection = DBConnect.getConnection();

			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			// String query2=
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
					int gameNbr = getGameNbrFromTicketNo(ticket_nbr);
					String actualTicketNbrFromTicketNbr = getTicketNbrFromTicketNo(ticket_nbr);
					int actual_tkt_nbr = Integer
							.parseInt(actualTicketNbrFromTicketNbr);
					logger.debug("Book No is: " + book_nbr);

					// check here for ticket in temporary table
					preparedStatement4 = connection.prepareStatement(query4);
					preparedStatement4.setString(1, ticket_nbr);
					resultSet4 = preparedStatement4.executeQuery();
					if (!resultSet4.next()) {
						preparedStatement1 = connection
								.prepareStatement(query1);
						preparedStatement1.setInt(1, game_id);
						preparedStatement1.setString(2, book_nbr);
						resultSet1 = preparedStatement1.executeQuery();

						if (resultSet1.next()) {

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

									if (ticketStatus
											.equalsIgnoreCase("MISSING")) {
										bean.setValid(false);
										bean
												.setStatus("Ticket staus is MISSING in DATABASE");
										bean.setMessageCode("222010");
										bean.setValidity("InValid Ticket");
										logger.debug("Ticket is MISSING.");
									} else if (ticketStatus
											.equalsIgnoreCase("CLAIM_RET")) {
										bean.setValid(false);
										bean
												.setStatus("Already Verified by Agetnt for a Retailer it is to be paid as PWt from Agent Not as Direct Player PWT");
										bean.setMessageCode("122105");
										verifyResults.add(bean);
										logger
												.debug("Ticket hsa Bean Claimed By Agent To Retailer.");
									} else if (ticketStatus
											.equalsIgnoreCase("RETURN")) {
										preparedStatement3 = connection
												.prepareStatement(query3);
										preparedStatement3.setInt(1, game_id);
										resultSet3 = preparedStatement3
												.executeQuery();
										if (resultSet3.next()) {
											if (resultSet3
													.getInt("nbr_of_tickets_per_book") <= actual_tkt_nbr) {
												bean.setValid(false);
												bean
														.setStatus("Number Format Error/Out of Range Please Check");
												bean.setMessageCode("122102");
												// bean.setStatus("Ticket Is
												// Fake.");
												verifyResults.add(bean);
												logger
														.debug("Ticket number is fake.");
											} else {
												bean.setValid(true);
												bean
														.setStatus("Register player for further Process");
												bean.setMessageCode("121103");
												verifyResults.add(bean);
												logger
														.debug("Ticket Is valid for pwt. (Means not fake and not in pwt table)");
											}
										}
									} else if (ticketStatus
											.equalsIgnoreCase("CLAIM_AGT")) {
										bean.setValid(false);
										bean.setStatus("Already Paid to Agent");
										bean.setMessageCode("122104");
										verifyResults.add(bean);
										logger
												.debug("Ticket's PWT Has Bean Paid By BO to Agent.");
									} else if (ticketStatus
											.equalsIgnoreCase("CLAIM_PLR")) {
										bean.setValid(false);
										bean
												.setStatus("Already paid as Direct player PWT");
										bean.setMessageCode("122106");
										verifyResults.add(bean);
										logger
												.debug("Ticket Is High Prize Ticket, Ticket's PWT Has Been Paid. ");
									} else if (ticketStatus
											.equalsIgnoreCase("SUB_GOV")) {
										bean.setValid(false);
										bean
												.setStatus("Undefined Status of Ticket's PWT");
										verifyResults.add(bean);
										logger
												.debug("Undefined Status of Ticket's PWT");
									} else {
										bean.setValid(false);
										bean
												.setStatus("Undefined Status of Ticket's PWT");
										verifyResults.add(bean);
										logger
												.debug("Undefined Status of Ticket's PWT");
									}
								} else {
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
											bean.setMessageCode("122101");
											verifyResults.add(bean);
											logger
													.debug("Ticket number is fake.");
										} else {
											bean.setValid(true);
											bean.setStatus("Accept ticket");
											bean.setMessageCode("121102");
											verifyResults.add(bean);
											logger
													.debug("Ticket Is valid for pwt. (Means not fake and not in pwt table)");
										}
									}
								}
							} else {
								bean.setValid(false);
								bean
										.setStatus("Ticket is still in stock of BO");
								bean.setMessageCode("122103");
								verifyResults.add(bean);
								logger.debug("Ticket owner is not Retailer.");
							}
						} else {
							bean.setValid(false);
							bean
									.setStatus("Number Format Error/Out of Range Please Check");
							bean.setMessageCode("122102");
							verifyResults.add(bean);
							logger.debug("Ticket Is not of the company.");
						}
					} else {
						preparedStatement2 = connection
								.prepareStatement(query2);
						preparedStatement2.setInt(1, gameNbr);
						preparedStatement2.setString(2, ticket_nbr);
						resultSet2 = preparedStatement2.executeQuery();
						if (resultSet2.next()) {
							if (resultSet2.getString("status").equals("RETURN")) {
								bean.setValid(true);
								bean
										.setStatus("Register player for further Process");
								bean.setMessageCode("121103");
								verifyResults.add(bean);
								logger
										.debug("Ticket Is valid for pwt. (Means ticket in bulk table and return status in main ticket table)");

							} else {
								bean.setValid(false);
								bean
										.setStatus("Already Verified in Bulk Receipt at BO Please Return the ticket");
								bean.setMessageCode("122108");
								verifyResults.add(bean);
								logger
										.debug("Ticket has been verified for temporary table");

							}
						} else {

							bean.setValid(false);
							bean
									.setStatus("Already Verified in Bulk Receipt at BO Please Return the ticket");
							bean.setMessageCode("122108");
							verifyResults.add(bean);
							logger
									.debug("Ticket has been verified for temporary table");
						}

					}

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			verifyResults = null;
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
				throw new LMSException();
			}
		}
		logger.debug("The verified List Is: " + verifyResults);
		return verifyResults;

	}

	/**
	 * This class is used to save data after validation of the ticket.
	 * 
	 * @param game_id
	 * @param verifiedTicketList
	 * @return List
	 * @throws LMSException
	 */

	public List<TicketBean> saveTicketsData(int game_id,
			List<TicketBean> verifiedTicketList, int userId, int userOrgId)
			throws LMSException {
		List<TicketBean> savedResults = new ArrayList<TicketBean>();
		Connection connection = null;

		PreparedStatement Pstmt = null;
		PreparedStatement Pstmt1 = null;
		String query = QueryManager.getST4UpdatePwtTicketStatusToPLR();
		// String query1=QueryManager.updateIntoPwtTicketsInv();
		String query1 = QueryManager.insertInPwtTicketDetailsUsingGameNbr();
		logger.debug("Query1 is : " + query1);
		try {
			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			setConnection(connection);
			Pstmt = connection.prepareStatement(query);
			Pstmt1 = connection.prepareStatement(query1);
			Iterator<TicketBean> iterator = verifiedTicketList.iterator();
			while (iterator.hasNext()) {
				String ticket_nbr = null;
				boolean isValid = false;
				String status = null;
				TicketBean ticketBean = iterator.next();
				isValid = ticketBean.getIsValid();
				status = ticketBean.getStatus();
				ticket_nbr = ticketBean.getTicketNumber();
				int gameNbr = getGameNbrFromTicketNo(ticket_nbr);
				if (isValid == true) {
					if (status
							.equalsIgnoreCase("Register player for further Process")) {
						// ticket_nbr=ticketBean.getTicketNumber();
						Pstmt.setInt(1, gameNbr);
						Pstmt.setInt(2, userId);
						Pstmt.setInt(3, userOrgId);
						Pstmt.setInt(4, game_id);
						Pstmt.setString(5, ticket_nbr);
						Pstmt.executeUpdate();
					} else {
						// ticket_nbr=ticketBean.getTicketNumber();
						String book_nbr = getBookNbrFromTicketNo(ticket_nbr);
						Pstmt1.setInt(1, gameNbr);
						Pstmt1.setString(2, ticket_nbr);
						Pstmt1.setInt(3, game_id);
						logger.debug("BookNumber is: " + book_nbr);
						Pstmt1.setString(4, book_nbr);
						Pstmt1.setString(5, "CLAIM_PLR");
						Pstmt1.setInt(6, userId);
						Pstmt1.setInt(7, userOrgId);
						Pstmt1.executeUpdate();
					}

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					bean.setValid(isValid);
					savedResults.add(bean);
				}
			}
			logger.debug("savedResults : " + savedResults);
			return savedResults;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException();
		} finally {
			try {
				if (Pstmt1 != null) {
					Pstmt1.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setConnection(Connection conn) {
		this.connection = conn;
	}

}
