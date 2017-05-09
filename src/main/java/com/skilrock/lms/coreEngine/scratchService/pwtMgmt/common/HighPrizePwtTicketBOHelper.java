package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

public class HighPrizePwtTicketBOHelper {
	String HighPrizeAgent = "Invalid";

	public List<ActiveGameBean> getActiveGames() {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {
			System.out.println("inside try............");
			ActiveGameBean gameBean = null;
			List<ActiveGameBean> searchResults = new ArrayList<ActiveGameBean>();

			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();
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
			System.out.println(searchResults);
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

	// public String getBookNbrFromTicketNo(String ticket_nbr){
	// String book_nbr="";
	// StringTokenizer st = new StringTokenizer(ticket_nbr,"-");
	// for(int i=0;i<2;i++){
	// if(st.hasMoreTokens()){
	// book_nbr = book_nbr + st.nextToken();
	// if(i==0){
	// book_nbr=book_nbr+"-";
	// }
	// }
	// }
	// //System.out.println(book_nbr);
	// return book_nbr;
	// }

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
			DBConnect connect = new DBConnect();
			connection = connect.getConnection();
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

	// added by yogesh to get game digit information
	public GameTicketFormatBean getGameTicketFormat(int gameId) {

		StringBuffer gameIdList = new StringBuffer();
		GameTicketFormatBean gameFormatBean = null;

		Connection connection = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		String query = null;

		try {
			DBConnect connect = new DBConnect();
			connection = connect.getConnection();

			query = QueryManager.getGameFormatInformation();
			query = query + gameId + " )";

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

		return gameFormatBean;
	}

	public String getHighPrizeAgent() {
		return HighPrizeAgent;
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
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {
			System.out.println("inside try............");
			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			// String query2=
			// QueryManager.getST4PwtTicketDetailsUsingTicketNbr();
			String query2 = QueryManager.getST4PwtTicketDetailsUsingGameNbr();
			String query3 = QueryManager.getST4GameDetailsUsingGameId();
			Iterator<TicketBean> iterator = ticketBean.iterator();
			while (iterator.hasNext()) {
				String ticket_nbr = null;
				int size = 0;
				ticket_nbr = iterator.next().getTicketNumber();
				size = ticket_nbr.length();

				if (size != 0) {

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					String tktArr[] = ticket_nbr.split("-");
					String book_nbr = tktArr[0] + "-" + tktArr[1];
					int actual_tkt_nbr = Integer.parseInt(tktArr[2]);
					// String book_nbr=getBookNbrFromTicketNo(ticket_nbr);
					// int gameNbr=getGameNbrFromTicketNo(ticket_nbr);
					// int
					// actual_tkt_nbr=Integer.parseInt(getTicketNbrFromTicketNo(ticket_nbr));
					System.out.println("Book No is:::::::::: " + book_nbr);

					preparedStatement1 = connection.prepareStatement(query1);
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

								if (ticketStatus.equalsIgnoreCase("MISSING")) {
									bean.setValid(false);
									bean
											.setStatus("Ticket staus is MISSING in DATABASE");
									bean.setMessageCode("222010");
									bean.setValidity("InValid Ticket");
									System.out.println("Ticket is MISSING.");
								} else if (ticketStatus
										.equalsIgnoreCase("CLAIM_AGT")) {
									preparedStatement3 = connection
											.prepareStatement(query3);
									preparedStatement3.setInt(1, game_id);
									resultSet3 = preparedStatement3
											.executeQuery();
									if (resultSet3.next()) {
										if (resultSet3
												.getInt("nbr_of_tickets_per_book") <= actual_tkt_nbr) {
											bean.setValid(false);
											bean.setStatus("Ticket Is Fake.");
											bean.setValidity("InValid Ticket");
											verifyResults.add(bean);
											System.out
													.println("Ticket number is fake.");
										} else {
											bean.setValid(true);
											bean
													.setStatus("Ticket Is Valid For Change The PWT Status.");
											bean.setValidity("Ticket Is Valid");
											setHighPrizeAgent("Valid");
											verifyResults.add(bean);
											System.out
													.println("Ticket Is valid for Change The PWT Status.");
										}
									}
								} else if (ticketStatus
										.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
									preparedStatement3 = connection
											.prepareStatement(query3);
									preparedStatement3.setInt(1, game_id);
									resultSet3 = preparedStatement3
											.executeQuery();
									if (resultSet3.next()) {
										if (resultSet3
												.getInt("nbr_of_tickets_per_book") <= actual_tkt_nbr) {
											bean.setValid(false);
											bean.setStatus("Ticket Is Fake.");
											bean.setValidity("InValid Ticket");
											verifyResults.add(bean);
											System.out
													.println("Ticket number is fake.");
										} else {
											bean.setValid(true);
											bean
													.setStatus("Ticket Is Valid For Change The PWT Status.");
											bean
													.setValidity("Ticket was Accepted as bulk ticket");
											bean.setBook_nbr(book_nbr);
											setHighPrizeAgent("Valid");
											verifyResults.add(bean);
											System.out
													.println("Ticket Is valid for Change The PWT Status because it is in bulk table but not in ticket table");
										}
									}
								} else if (ticketStatus
										.equalsIgnoreCase("RETURN")) {
									bean.setValid(false);
									bean
											.setStatus("Ticket Is High Prize Ticket");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									System.out
											.println("Ticket Is High Prize Ticket");
								} else if (ticketStatus
										.equalsIgnoreCase("CLAIM_RET")) {
									bean.setValid(true);
									bean
											.setStatus("Ticket's PWT Has Been Verified by Agent, If this is high prize please return it.");
									bean.setValidity("Valid Ticket");
									// added by yogesh
									System.out
											.println("hello inside yogeshhhhhh");
									setHighPrizeAgent("Valid");
									verifyResults.add(bean);
									System.out
											.println("Ticket's PWT Has Been Verified by Agent, If this is high prize please return it.");
								} else if (ticketStatus
										.equalsIgnoreCase("CLAIM_PLR")) {
									bean.setValid(false);
									bean
											.setStatus("Ticket Is High Prize Ticket, Ticket's PWT Has Been Paid.");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									System.out
											.println("Ticket Is High Prize Ticket, Ticket's PWT Has Been Paid. ");
								} else {
									bean.setValid(false);
									bean
											.setStatus("Undefined Status of Ticket's PWT");
									bean.setValidity("InValid Ticket");
									verifyResults.add(bean);
									System.out
											.println("Undefined Status of Ticket's PWT");
								}
							} else {/*
									 * // Now we are not checking in bulk as
									 * when any ticket claimed as BULK @ BO will
									 * have status = CLAIM_AGT_TEMP
									 * 
									 * //check her for ticket in bulk
									 * preparedStatement4=connection.prepareStatement(query4);
									 * preparedStatement4.setString(1,ticket_nbr);
									 * resultSet4=preparedStatement4.executeQuery();
									 * if(resultSet4.next()) {
									 * 
									 * bean.setValid(true);
									 * bean.setStatus("Ticket Is Valid For
									 * Change The PWT Status.");
									 * bean.setValidity("Ticket was Accepted as
									 * bulk ticket");
									 * bean.setBook_nbr(book_nbr);
									 * setHighPrizeAgent("Valid");
									 * verifyResults.add(bean);
									 * System.out.println("Ticket Is valid for
									 * Change The PWT Status because it is in
									 * bulk table but not in ticket table"); //
									 * update status to return in bulk table
									 * preparedStatement5=connection.prepareStatement(query5);
									 * preparedStatement5.setString(1,"RETURN");
									 * preparedStatement5.setTimestamp(2,new
									 * java.sql.Timestamp(new
									 * Date().getTime()));
									 * preparedStatement5.setString(3,ticket_nbr);
									 * preparedStatement5.executeUpdate();
									 * 
									 * //insert in ticket table with return
									 * preparedStatement5.clearParameters();
									 * preparedStatement5=connection.prepareStatement("insert
									 * into
									 * st_pwt_tickets_inv(ticket_nbr,game_id,book_nbr,status)
									 * values(?,?,?,?)");
									 * preparedStatement5.setString(1,ticket_nbr);
									 * preparedStatement5.setInt(2,game_id);
									 * preparedStatement5.setString(3,book_nbr);
									 * preparedStatement5.setString(4,"RETURN");
									 * preparedStatement5.executeUpdate();
									 * 
									 * }else{
									 */
								bean.setValid(false);
								bean
										.setStatus("This High Prize Ticket Entry is not available in DataBase, So No Need To Upadate.");
								bean.setValidity("InValid Ticket");
								verifyResults.add(bean);
								System.out
										.println("This High Prize Ticket Entry is not available in DataBase, So No Need To Upadate  ");
							}
							// }
						} else {
							bean.setValid(false);
							bean
									.setStatus("Ticket's Owner Is Not Valid. Or Ticket Is In Our Stock In Our Chain");
							bean.setValidity("InValid Ticket");
							verifyResults.add(bean);
							System.out
									.println("Ticket owner is not Retailer or agent .");
						}
					} else {
						bean.setValid(false);
						bean.setStatus("Ticket Is Not Of The Our Company.");
						bean.setValidity("InValid Ticket");
						verifyResults.add(bean);
						System.out.println("Ticket Is not of the company.");
					}
				}
			}
			System.out.println("The verified List Is: " + verifyResults);
			return verifyResults;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
			}
		}

		return null;
	}

	public List<TicketBean> saveTicketsData(String game_name,
			List<TicketBean> verifiedTicketList, int userOrgId, int userId,
			String channel, String interfaceType) throws LMSException {
		List<TicketBean> savedResults = new ArrayList<TicketBean>();
		Connection connection = null;
		PreparedStatement Pstmt = null;
		PreparedStatement Pstmt1 = null;
		ResultSet resultSet = null;
		String query1 = QueryManager.getST4GameDetailsUsingGameName();
		String query = QueryManager.getST4UpdatePwtTicketStatusToRETURN();
		try {
			DBConnect connect = new DBConnect();
			connection = connect.getConnection();
			int game_id = 0;
			int game_nbr = 0;
			Pstmt1 = connection.prepareStatement(query1);
			Pstmt1.setString(1, game_name);
			resultSet = Pstmt1.executeQuery();
			while (resultSet.next()) {
				game_id = resultSet.getInt("game_id");
				game_nbr = resultSet.getInt("game_nbr");
			}

			Pstmt = connection.prepareStatement(query);
			Iterator<TicketBean> iterator = verifiedTicketList.iterator();
			while (iterator.hasNext()) {
				String ticket_nbr = null;
				boolean isValid = false;
				// updated to check in bulk
				String isValidityBulk = null;
				TicketBean ticketBean = iterator.next();
				isValid = ticketBean.getIsValid();
				isValidityBulk = ticketBean.getValidity();
				ticket_nbr = ticketBean.getTicketNumber();
				if (isValidityBulk.equals("Ticket was Accepted as bulk ticket")
						&& isValid == true) {
					/*
					 * Commented By Gaurav because we are not maintaining the
					 * ticket or VIRN status in temporary table's.The status in
					 * temp table corresponds to the status of the temporary
					 * receipt('OPEN','CLOSE'). Need to discuss if status is to
					 * be maintaind
					 */

					// update status to return in bulk table
					/*
					 * preparedStatement5=connection.prepareStatement(query5);
					 * preparedStatement5.setString(1,"RETURN");
					 * preparedStatement5.setTimestamp(2,new
					 * java.sql.Timestamp(new Date().getTime()));
					 * preparedStatement5.setString(3,ticket_nbr);
					 * preparedStatement5.executeUpdate();
					 */

					new CommonFunctionsHelper().updateTicketInvTable(
							ticket_nbr, ticket_nbr.split("-")[0] + "-"
									+ ticket_nbr.split("-")[1], game_nbr,
							game_id, "RETURN", userId, userOrgId, "UPDATE", 0,
							channel, interfaceType, connection);

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					bean.setBook_nbr(ticket_nbr.split("-")[0] + "-"
							+ ticket_nbr.split("-")[1]);
					bean.setGameNbr(game_nbr);
					bean.setValid(isValid);
					bean.setStatus("Ticket Status Has Been Changed");
					bean.setValidity("Ticket Is Valid");

					savedResults.add(bean);

				} else if (isValid == true) {
					//					
					// String book_nbr=getBookNbrFromTicketNo(ticket_nbr);
					// Pstmt.setInt(1,game_nbr);
					// Pstmt.setInt(2,userId);
					// Pstmt.setInt(3,userOrgId);
					//					
					// Pstmt.setInt(4, game_id);
					// Pstmt.setString(5,ticket_nbr);
					// Pstmt.executeUpdate();

					new CommonFunctionsHelper().updateTicketInvTable(
							ticket_nbr, ticket_nbr.split("-")[0] + "-"
									+ ticket_nbr.split("-")[1], game_nbr,
							game_id, "RETURN", userId, userOrgId, "UPDATE", 0,
							channel, interfaceType, connection);

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					bean.setBook_nbr(ticket_nbr.split("-")[0] + "-"
							+ ticket_nbr.split("-")[1]);
					bean.setGameNbr(game_nbr);
					bean.setValid(isValid);
					bean.setStatus("Ticket Status Has Been Changed");
					bean.setValidity("Ticket Is Valid");

					savedResults.add(bean);
				}
			}
			return savedResults;
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

		return null;
	}

	public void setHighPrizeAgent(String highPrizeAgent) {
		HighPrizeAgent = highPrizeAgent;
	}

}

/*
 * public int getGameId(List<ActiveGameBean> activeGameList, String
 * gameNbr_Name) {
 * 
 * ActiveGameBean bean = null; if (activeGameList != null) { for (int i = 0; i <
 * activeGameList.size(); i++) { bean = activeGameList.get(i); if
 * (gameNbr_Name.equals(bean.getGameNbr_Name())) { return bean.getGameId(); } } }
 * 
 * return 0; }
 * 
 * 
 * public void saveTicketsData(int agentUserId, String gameNbr_Name, String
 * retOrgName, List<RetailerOrgBean> retOrgList, List<ActiveGameBean>
 * activeGameList, String[] virnCode, List<PwtBean> pwtList) {
 * 
 * int gameId = -1; double retPwtCommRate = 0.0;
 * 
 * ActiveGameBean activeGameBean = null; RetailerOrgBean retOrgBean = null;
 * 
 * if (activeGameList != null) { for (int i = 0; i < activeGameList.size(); i++) {
 * activeGameBean = activeGameList.get(i); if
 * (gameNbr_Name.equals(activeGameBean.getGameNbr_Name())) { gameId =
 * activeGameBean.getGameId(); retPwtCommRate =
 * activeGameBean.getRetailerPwtCommRate(); break; } } }
 * 
 * int retOrgId = -1; int retUserId = -1;
 * 
 * if (retOrgList != null) { for (int i = 0; i < retOrgList.size(); i++) {
 * retOrgBean = retOrgList.get(i); if
 * (retOrgName.equals(retOrgBean.getRetOrgName())) { retOrgId =
 * retOrgBean.getRetOrgId(); retUserId = retOrgBean.getRetUserId(); break; } } }
 * 
 * verifyPwtTickets(gameId, virnCode, pwtList);
 * 
 * 
 * saveTickets(pwtList,gameId,agentUserId,retOrgId,retUserId,retPwtCommRate); }
 * 
 * private void saveTickets(List<TicketBean> ticketList, int gameId, int
 * agentUserId, int retOrgId, int retUserId, double retPwtCommRate) {
 * 
 * 
 * 
 * Connection connection = null; PreparedStatement masterPstmt = null;
 * PreparedStatement detailPstmt = null; PreparedStatement invPstmt = null;
 * 
 * ResultSet resultSet = null; int trnId = -1;
 * 
 * if (ticketList != null) { int size = ticketList.size(); QueryManager
 * queryManager = null;
 * 
 * TicketBean ticketBean = null;
 * 
 * String masterQuery = null; String detailQuery = null; String invQuery = null;
 * 
 * double pwtAmt; double commAmt;
 * 
 * if (size > 0) { try { DBConnect dbConnect = new DBConnect(); connection =
 * dbConnect.getConnection(); connection.setAutoCommit(false);
 * 
 * masterQuery = QueryManager.getPwtAgentMasterQuery(); masterPstmt =
 * connection.prepareStatement(masterQuery);
 * 
 * detailQuery = QueryManager.getPwtAgentDetailQuery(); detailPstmt =
 * connection.prepareStatement(detailQuery);
 * 
 * invQuery = QueryManager.getPWTUpdateQuery(); invPstmt =
 * connection.prepareStatement(invQuery);
 * 
 * masterPstmt.setInt(1, agentUserId); masterPstmt.setInt(2, retOrgId);
 * masterPstmt.setString(3, "PWT"); masterPstmt.setDate(4, new java.sql.Date(new
 * Date() .getTime()));
 * 
 * 
 * 
 * masterPstmt.execute(); resultSet = masterPstmt.getGeneratedKeys();
 * 
 * while (resultSet.next()) { trnId = resultSet.getInt(1);
 * System.out.println("Transaction Id:" + trnId); }
 * 
 * System.out.println("OrderId::" + trnId);
 * 
 * for (int i = 0; i < size; i++) { pwtBean = (PwtBean) pwtList.get(i);
 * 
 * if (pwtBean.getIsValid()){
 * 
 * String encodedVirn = MD5Encoder.encode(pwtBean.getVirnCode());
 * detailPstmt.setString(1,encodedVirn); detailPstmt.setInt(2,trnId);
 * detailPstmt.setInt(3,gameId); detailPstmt.setInt(4,agentUserId);
 * detailPstmt.setInt(5,retUserId); detailPstmt.setInt(6,retOrgId);
 * 
 * pwtAmt = Double.parseDouble(pwtBean.getPwtAmount()); commAmt = pwtAmt *
 * retPwtCommRate *0.01;
 * 
 * detailPstmt.setDouble(7,pwtAmt); detailPstmt.setDouble(8,commAmt);
 * detailPstmt.setDouble(9,pwtAmt - commAmt);
 * 
 * detailPstmt.execute();
 * 
 * invPstmt.setInt(1,gameId); invPstmt.setString(2,encodedVirn);
 * invPstmt.execute(); } }
 * 
 * connection.commit(); } catch (SQLException e) {
 * 
 * e.printStackTrace(); } finally {
 * 
 * try { if (masterPstmt != null) { masterPstmt.close(); } if (detailPstmt !=
 * null) { detailPstmt.close(); } if (connection != null) { connection.close(); } }
 * catch (SQLException se) { se.printStackTrace(); } } } } }
 * 
 * private void verifyPwtTickets(int gameId, String[] virnCode, List<PwtBean>
 * pwtList) {
 * 
 * String encodedVirnCode = getEncodedVirnCode(virnCode);
 * System.out.println("---((((((::" + encodedVirnCode);
 * 
 * Connection connection = null; Statement statement = null;
 * 
 * ResultSet resultSet = null; int orderId = -1;
 * 
 * if (pwtList != null) { int size = pwtList.size(); QueryManager queryManager =
 * null; PwtBean pwtBean = null;
 * 
 * StringBuffer query = new StringBuffer();
 * 
 * if (size > 0) { try { DBConnect dbConnect = new DBConnect(); connection =
 * dbConnect.getConnection();
 * 
 * 
 * statement = connection.createStatement();
 * query.append(QueryManager.getPWTCheckQuery()); query.append("and game_id =
 * "); query.append("" + gameId); query.append(" and virn_code in (");
 * query.append(encodedVirnCode); query.append(")");
 * 
 * System.out.println("GameId:" + gameId); System.out.println("Query:: " +
 * query); resultSet = statement.executeQuery(query.toString());
 * System.out.println("ResultSet:" + resultSet + "---" +
 * resultSet.getFetchSize());
 * 
 * 
 * 
 * String vCode = null; String pwtAmount = null; String enVirnCode = null;
 * 
 * while (resultSet.next()) {
 * 
 * vCode = resultSet .getString(TableConstants.SPI_VIRN_CODE);
 * System.out.println("Vcode:" + vCode); pwtAmount = resultSet
 * .getString(TableConstants.SPI_PWT_AMT);
 * 
 * for (int j = 0; j < pwtList.size(); j++) {
 * 
 * 
 * pwtBean = (PwtBean) pwtList.get(j); enVirnCode = MD5Encoder.encode(pwtBean
 * .getVirnCode());
 * 
 * if (enVirnCode.equals(vCode)) { System.out.println("Matching");
 * pwtBean.setValid(true); pwtBean.setPwtAmount(pwtAmount); break; } } } } catch
 * (SQLException e) {
 * 
 * e.printStackTrace(); } finally {
 * 
 * try {
 * 
 * if (statement != null) { statement.close(); } if (connection != null) {
 * connection.close(); } } catch (SQLException se) { se.printStackTrace(); } } } } }
 * 
 * private String getEncodedVirnCode(String[] virnCode) {
 * 
 * StringBuffer encodedVirnCode = new StringBuffer("");
 * 
 * 
 * for (int i = 0; i < virnCode.length; i++) {
 * 
 * encodedVirnCode.append("'");
 * encodedVirnCode.append(MD5Encoder.encode(virnCode[i]));
 * encodedVirnCode.append("'"); encodedVirnCode.append(","); } int length =
 * encodedVirnCode.length(); encodedVirnCode.deleteCharAt(length - 1);
 * 
 * return encodedVirnCode.toString(); }
 */

