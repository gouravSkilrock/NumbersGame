package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

public class PwtTicketAgentHelper {

	private String PWTAgent = "Invalid";

	public Map agtTicketVerifyNSave(String[] ticketNumber,
			String[] gameNbr_Name, int[] inpCount, UserInfoBean userInfo,
			int partyOrgId) throws LMSException {

		Map map = new TreeMap();
		Connection connection = DBConnect.getConnection();

		try {

			connection.setAutoCommit(false);

			// Create the NON Empty tickets into List
			List<String> allTktNum = new ArrayList<String>();
			for (int i = 0; i < ticketNumber.length; i++) {
				if (ticketNumber[i] != null
						&& !ticketNumber[i].trim().equals("")) {
					allTktNum.add(ticketNumber[i]);
				}
			}

			int startTktCount = 0, endTktCount = 0;
			String gameTktNumber[] = null;

			Map<String, List<TicketBean>> gameTktMap = new HashMap<String, List<TicketBean>>();
			List<TicketBean> saveTktBeanList = new ArrayList<TicketBean>();
			List<TicketBean> totalTktList = new ArrayList<TicketBean>();
			for (int i = 0; i < gameNbr_Name.length; i++) {

				// if Any Game is selected
				if (!gameNbr_Name[i].equals("-1")) {

					endTktCount = startTktCount + inpCount[i];
					gameTktNumber = new String[endTktCount - startTktCount];

					int inc = 0;
					for (int j = startTktCount; j < endTktCount; j++) {
						gameTktNumber[inc] = allTktNum.get(j);
						inc++;
						startTktCount++;
					}
					System.out.println("Game Name is = " + gameNbr_Name[i]
							+ " And Ticket length = " + gameTktNumber.length);

					if (gameTktNumber.length > 0) {

						String[] gameNameNbrArr = gameNbr_Name[i].split("-");
						int gameNbr = Integer.parseInt(gameNameNbrArr[0]);

						// validate the ticket based on their status in
						// st_inv_tiket table
						List<TicketBean> ticketList = getGameWiseVerifiedTickets(
								gameTktNumber, gameNbr, connection);
						totalTktList.addAll(ticketList);

						CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
						for (TicketBean tktBean : ticketList) {
							if (tktBean.getIsValid()) {
								// update ticket status into ticket inventory
								// table
								int updRow = commHelper
										.updateTicketInvTable(tktBean
												.getTicketNumber(), tktBean
												.getBook_nbr(), gameNbr,
												tktBean.getTicketGameId(),
												"CLAIM_RET", userInfo
														.getUserId(), userInfo
														.getUserOrgId(),
												tktBean.getUpdateTicketType(),
												partyOrgId, userInfo
														.getChannel(), userInfo
														.getInterfaceType(),
												connection);
								commHelper.updateBookStatus(tktBean
										.getTicketGameId(), tktBean
										.getBook_nbr(), connection, "CLAIMED");
								if (updRow == 1) {
									tktBean
											.setStatus("Ticket Is Saved For PWT");
									tktBean.setValidity("Valid Ticket");
									saveTktBeanList.add(tktBean);
								}

							}
						}

						if (gameTktMap.containsKey(gameNbr_Name[i])) {
							List<TicketBean> oldPwtList = gameTktMap
									.get(gameNbr_Name[i]);
							oldPwtList.addAll(ticketList);
							gameTktMap.put(gameNbr_Name[i], oldPwtList);
						} else {
							gameTktMap.put(gameNbr_Name[i], ticketList);
						}
					}
				}
			}
			connection.commit();

			StringBuilder jsString = new StringBuilder();
			Iterator itTkt = gameTktMap.entrySet().iterator();
			Map msgCode = new HashMap();
			while (itTkt.hasNext()) {
				Map.Entry pairsTkt = (Map.Entry) itTkt.next();

				jsString.append(pairsTkt.getKey() + ":");
				List<TicketBean> tktList = (List<TicketBean>) pairsTkt
						.getValue();
				Iterator<TicketBean> itTktList = tktList.iterator();
				while (itTktList.hasNext()) {
					TicketBean bean = itTktList.next();
					jsString.append(bean.getTicketNumber() + "*M*"
							+ bean.getMessageCode() + ":");
					msgCode.put(bean.getMessageCode(), bean.getValidity() + ":"
							+ bean.getStatus());
				}
				jsString.append("Nx*");
			}
			Iterator itMsgCode = msgCode.entrySet().iterator();
			while (itMsgCode.hasNext()) {
				Map.Entry pairsTkt = (Map.Entry) itMsgCode.next();
				jsString.append(pairsTkt.getKey() + "-" + pairsTkt.getValue()
						+ "Msg");
			}

			map.put("VERIFIED_TICKET_JSSTRING", jsString);
			map.put("VERIFIED_TICKET_MAP", gameTktMap);
			map.put("SAVED_TICKET_LIST", saveTktBeanList);
			map.put("totalTktList", totalTktList);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Problem in Ticket validation");
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

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
			statement = connection.createStatement();

			String query = QueryManager.getST4ActiveGamesQuery()
					+ " order by game_nbr";
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

	public int getGameId(List<ActiveGameBean> activeGameList,
			String gameNbr_Name) {
		ActiveGameBean bean = null;
		if (activeGameList != null) {
			// System.out.println("Game List Size="+activeGameList.size());
			for (int i = 0; i < activeGameList.size(); i++) {
				bean = activeGameList.get(i);
				if (gameNbr_Name.equals(bean.getGameNbr_Name())) {
					return bean.getGameId();
				}
			}
		}

		return 0;
	}

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
			throw new LMSException("sql exception", e);
		} finally {
			try {
				if (Pstmt != null) {
					Pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException(
						"sql exception during closing connection", se);
			}
		}
		return 0;
	}

	public List<GameTicketFormatBean> getGameTicketFormatList(
			List<ActiveGameBean> activeGameList) {

		StringBuffer gameIdList = new StringBuffer();
		for (int i = 0; i < activeGameList.size(); i++) {
			gameIdList.append(activeGameList.get(i).getGameId()).append(",");
		}

		List<GameTicketFormatBean> gameFormatList = null;
		Connection connection = null;

		if (gameIdList.length() > 0) {

			gameIdList.deleteCharAt(gameIdList.length() - 1);
			gameFormatList = new ArrayList<GameTicketFormatBean>();

			try {
				connection = DBConnect.getConnection();
				String query = QueryManager.getGameFormatInformation()
						+ gameIdList.toString() + " )";

				Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(query);
				GameTicketFormatBean gameFormatBean = null;
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

	private List<TicketBean> getGameWiseVerifiedTickets(String[] tktArr,
			int gameNbr, Connection connection) throws LMSException {
		List<TicketBean> verifyResults = new ArrayList<TicketBean>();
		try {
			String tktNbrArr[] = null;
			TicketBean bean = new TicketBean();

			// check the format of these tickets
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			List<TicketBean> tktBeanList = commHelper.isTicketFormatValid(
					Arrays.asList(tktArr), gameNbr, connection);

			AgentPwtProcessHelper agtPwtHelper = new AgentPwtProcessHelper();
			for (TicketBean tktBean : tktBeanList) {
				if (tktBean.getIsValid()) {
					tktNbrArr = tktBean.getTicketNumber().split("-");
					bean = agtPwtHelper.checkTicketStatus(gameNbr + "",
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
			throw new LMSException("Problem in Ticket validation");
		}

	}

	public String getPWTAgent() {
		return PWTAgent;
	}

	public void setPWTAgent(String agent) {
		PWTAgent = agent;
	}

}