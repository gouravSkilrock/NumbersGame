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

package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.GameDetailsBean;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;

/**
 * This is a helper class providing methods for fetching the game details
 * 
 * @author Skilrock Technologies
 * 
 */
public class GameDetailsHelper {

	public int fetchAgentBooksWithRetailer(int gameId, String agentOrgId) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			String query = "SELECT COUNT(*) 'COUNT' FROM st_se_game_inv_status WHERE current_owner='RETAILER' AND current_owner_id IN (SELECT organization_id FROM st_lms_organization_master WHERE parent_id=(SELECT organization_id FROM st_lms_organization_master WHERE name='"+agentOrgId+"')) AND game_id="+gameId+";";

			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return count;
	}

	/**
	 * This method fetches the game details for the passed game and agent
	 * 
	 * @param gameId
	 * @param agentOrgId
	 * @return GameDetailsBean
	 * @throws LMSException
	 */
	public GameDetailsBean fetchAgentGameDetails(int gameId, int agentOrgId)
			throws LMSException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		PreparedStatement totalBooksPstmt = null;
		PreparedStatement appBooksPstmt = null;

		String totalBooksQuery = null;
		String appBooksQuery = null;

		try {

			GameDetailsBean gameDetailsBean = null;

			// create database connection
			 
			connection = DBConnect.getConnection();

			// get fetch game details query

			String query = QueryManager.getST1GameDetailsFetchQuery();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, gameId);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				gameDetailsBean = new GameDetailsBean();
				gameDetailsBean.setGameId(resultSet
						.getInt(TableConstants.SGM_GAME_ID));
				gameDetailsBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));
				gameDetailsBean.setGameNbr(resultSet
						.getInt(TableConstants.SGM_GAME_NBR));
				gameDetailsBean.setTicketPrice(resultSet
						.getDouble(TableConstants.SGM_TICKET_PRICE));
				gameDetailsBean.setStartDate(resultSet
						.getDate(TableConstants.SGM_START_DATE));
				gameDetailsBean.setSaleEndDate(resultSet
						.getDate(TableConstants.SGM_SALE_END_DATE));
				gameDetailsBean.setPwtEndDate(resultSet
						.getDate(TableConstants.SGM_PWT_END_DATE));
				gameDetailsBean.setBooksPerPack(resultSet
						.getInt(TableConstants.SGM_NBR_OF_BOOKS_PER_PACK));
				gameDetailsBean.setTicketsPerBook(resultSet
						.getInt(TableConstants.SGM_NBR_OF_TICKETS_PER_BOOK));
				gameDetailsBean.setNbrOfBooks(resultSet
						.getInt(TableConstants.SGM_NBR_OF_BOOKS));

			}

			// fetch and set total books for the game
			totalBooksQuery = QueryManager.getST1AgentTotalBooks();
			totalBooksPstmt = connection.prepareStatement(totalBooksQuery);
			totalBooksPstmt.setInt(1, agentOrgId);
			totalBooksPstmt.setInt(2, gameId);

			ResultSet rs = totalBooksPstmt.executeQuery();
			while (rs.next()) {

				if (gameDetailsBean != null) {
					gameDetailsBean.setNbrOfBooksAvailable(rs
							.getInt(TableConstants.TOTAL));
				}
			}

			/*
			 * // fetch and set approved books for the game appBooksQuery =
			 * QueryManager.getST1AgentAppBooks(); appBooksPstmt =
			 * connection.prepareStatement(appBooksQuery);
			 * appBooksPstmt.setInt(1,gameId);
			 * 
			 * ResultSet rSet = appBooksPstmt.executeQuery();
			 * while(rSet.next()){ if(gameDetailsBean != null){
			 * 
			 * gameDetailsBean.setNbrOfBooksApp(rSet.getInt(TableConstants.NO_OF_BOOKS_APP)); } }
			 */

			// edited by yogesh to change number of books approved
			// fetch and set approved books for the game
			// appBooksQuery = QueryManager.getST1AgentAppBooks();
			appBooksQuery = " select  (ifnull(sum(a.nbr_of_books_appr),0)  - ifnull(sum(a.nbr_of_books_dlvrd),0)) 'remaining_books_approved'  from st_se_agent_ordered_games a,st_se_agent_order c where c.order_status in ('APPROVED','SEMI_PROCESSED') and  a.order_id=c.order_id and a.game_id = ? and  c.retailer_org_id in(select organization_id from st_lms_organization_master where parent_id=?)";

			appBooksPstmt = connection.prepareStatement(appBooksQuery);
			appBooksPstmt.setInt(1, gameId);
			appBooksPstmt.setInt(2, agentOrgId);
			System.out
					.println("remaining books approved ***  " + appBooksPstmt);
			ResultSet rSet = appBooksPstmt.executeQuery();
			while (rSet.next()) {
				if (gameDetailsBean != null) {

					gameDetailsBean.setNbrOfBooksApp(rSet
							.getInt("remaining_books_approved"));
				}
			}

			return gameDetailsBean;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	public List<Double> fetchAgentRetailerAccDetail(String retailerOrgName)
			throws LMSException {
		System.out.println("hello 2222222");
		Connection connection = null;
		Statement pstmt = null;
		ResultSet resultSet = null;
		List<Double> agentBOAccDetailList = null;

		try {

			// create database connection
			 
			connection = DBConnect.getConnection();

			// fetch game details
			String query = QueryManager.getST1AgentRetailerAccFetchQuery();
			pstmt = connection.createStatement();

			resultSet = pstmt.executeQuery(query + "'" + retailerOrgName + "'");
			System.out.println("hdkvfjkvf" + resultSet);

			while (resultSet.next()) {

				agentBOAccDetailList = new ArrayList<Double>();
				agentBOAccDetailList.add(resultSet
						.getDouble(TableConstants.SOM_CREDIT_LIMIT));
				agentBOAccDetailList.add(resultSet
						.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
				agentBOAccDetailList.add(resultSet
						.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
				agentBOAccDetailList.add(resultSet.getDouble("claimable_bal"));// added
				// by
				// amit
				// 22/09/10
			}

			return agentBOAccDetailList;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	/**
	 * This class is used to get the Account details of the Agent to whom BO is
	 * giving Order.
	 * 
	 * @param agentOrgId
	 * @return List throws LMSException
	 */

	public List<Double> fetchBOAgentAccDetail(String agentOrgName)
			throws LMSException {

		Connection connection = null;
		Statement pstmt = null;
		ResultSet resultSet = null;
		List<Double> agentBOAccDetailList = null;

		try {

			// create database connection
			 
			connection = DBConnect.getConnection();

			// fetch game details
			String query = QueryManager.getST1AgentBOAccFetchQuery();
			pstmt = connection.createStatement();

			resultSet = pstmt.executeQuery(query + "'" + agentOrgName + "'");
			System.out.println("hdkvfjkvf" + resultSet);

			while (resultSet.next()) {

				agentBOAccDetailList = new ArrayList<Double>();
				agentBOAccDetailList
						.add(resultSet
								.getDouble(TableConstants.SOM_CREDIT_LIMIT)
								+ resultSet
										.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
				System.out
						.println("extended credit limit is -------- "
								+ resultSet
										.getDouble(TableConstants.SOM_CREDIT_LIMIT)
								+ resultSet
										.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
				agentBOAccDetailList.add(resultSet
						.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
				agentBOAccDetailList.add(resultSet
						.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
				agentBOAccDetailList.add(resultSet.getDouble("claimable_bal"));// added
				// by
				// amit
				// 22/09/10
			}

			return agentBOAccDetailList;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	/**
	 * This method is used to find the total books for the game having by the
	 * Agent.
	 * 
	 * @param gameId
	 * @param agentName
	 * @return count
	 * @throws LMSException
	 */

	public int fetchBOBooksWithAgent(int gameId, String agtOrgName) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			String query = "SELECT COUNT(*) 'COUNT' FROM st_se_game_inv_status WHERE current_owner='AGENT' AND current_owner_id=(SELECT organization_id FROM st_lms_organization_master WHERE name='"+agtOrgName+"') AND game_id="+gameId+";";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return count;
	}

	/**
	 * This method fetches the game details for the passed game
	 * 
	 * @param gameId
	 * @return GameDetailsBean
	 * @throws LMSException
	 */
	public GameDetailsBean fetchBOGameDetails(int gameId) throws LMSException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		PreparedStatement totalBooksPstmt = null;
		PreparedStatement appBooksPstmt = null;

		String totalBooksQuery = null;
		String appBooksQuery = null;

		try {

			GameDetailsBean gameDetailsBean = null;

			// create database connection
			 
			connection = DBConnect.getConnection();

			// fetch game details
			String query = QueryManager.getST1GameDetailsFetchQuery();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, gameId);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				gameDetailsBean = new GameDetailsBean();
				gameDetailsBean.setGameId(resultSet
						.getInt(TableConstants.SGM_GAME_ID));
				gameDetailsBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));
				gameDetailsBean.setGameNbr(resultSet
						.getInt(TableConstants.SGM_GAME_NBR));
				gameDetailsBean.setTicketPrice(resultSet
						.getDouble(TableConstants.SGM_TICKET_PRICE));
				gameDetailsBean.setStartDate(resultSet
						.getDate(TableConstants.SGM_START_DATE));
				gameDetailsBean.setSaleEndDate(resultSet
						.getDate(TableConstants.SGM_SALE_END_DATE));
				gameDetailsBean.setPwtEndDate(resultSet
						.getDate(TableConstants.SGM_PWT_END_DATE));
				gameDetailsBean.setBooksPerPack(resultSet
						.getInt(TableConstants.SGM_NBR_OF_BOOKS_PER_PACK));
				gameDetailsBean.setTicketsPerBook(resultSet
						.getInt(TableConstants.SGM_NBR_OF_TICKETS_PER_BOOK));
				gameDetailsBean.setNbrOfBooks(resultSet
						.getInt(TableConstants.SGM_NBR_OF_BOOKS));

			}

			// fetch and set total books for the game
			totalBooksQuery = QueryManager.getST1BOTotalBooks();
			totalBooksPstmt = connection.prepareStatement(totalBooksQuery);
			totalBooksPstmt.setInt(1, gameId);

			ResultSet rs = totalBooksPstmt.executeQuery();
			while (rs.next()) {

				if (gameDetailsBean != null) {
					gameDetailsBean.setNbrOfBooksAvailable(rs
							.getInt(TableConstants.TOTAL));
				}
			}

			// fetch and set approved books for the game
			appBooksQuery = QueryManager.getST1BOAppBooks();
			appBooksPstmt = connection.prepareStatement(appBooksQuery);
			appBooksPstmt.setInt(1, gameId);

			ResultSet rSet = appBooksPstmt.executeQuery();
			while (rSet.next()) {
				if (gameDetailsBean != null) {

					gameDetailsBean.setNbrOfBooksApp(rSet
							.getInt(TableConstants.NO_OF_BOOKS_APP));
				}
			}

			return gameDetailsBean;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	public int fetchBooksWithRetailerFromAgent(int gameId, String agentOrgName) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			connection = DBConnect.getConnection();
			String query = "SELECT COUNT(*) 'COUNT' FROM st_se_game_inv_status WHERE current_owner='RETAILER' AND current_owner_id IN (SELECT organization_id FROM st_lms_organization_master WHERE parent_id=(SELECT organization_id FROM st_lms_organization_master WHERE name='"+agentOrgName+"')) AND game_id="+gameId+";";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return count;
	}

	/**
	 * This method fetches the game details for the passed game
	 * 
	 * @param gameId
	 * @return GameDetailsBean
	 * @throws LMSException
	 */
	public GameDetailsBean fetchGameDetails(int gameId) throws LMSException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {

			GameDetailsBean gameDetailsBean = null;

			// create database connection
			 
			connection = DBConnect.getConnection();

			// get fetch game details query
			String query = QueryManager.getST1GameDetailsFetchQuery();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, gameId);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				gameDetailsBean = new GameDetailsBean();
				gameDetailsBean.setGameId(resultSet
						.getInt(TableConstants.SGM_GAME_ID));
				gameDetailsBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));
				gameDetailsBean.setGameNbr(resultSet
						.getInt(TableConstants.SGM_GAME_NBR));
				gameDetailsBean.setTicketPrice(resultSet
						.getDouble(TableConstants.SGM_TICKET_PRICE));
				gameDetailsBean.setStartDate(resultSet
						.getDate(TableConstants.SGM_START_DATE));
				gameDetailsBean.setSaleEndDate(resultSet
						.getDate(TableConstants.SGM_SALE_END_DATE));
				gameDetailsBean.setPwtEndDate(resultSet
						.getDate(TableConstants.SGM_PWT_END_DATE));
				gameDetailsBean.setBooksPerPack(resultSet
						.getInt(TableConstants.SGM_NBR_OF_BOOKS_PER_PACK));
				gameDetailsBean.setTicketsPerBook(resultSet
						.getInt(TableConstants.SGM_NBR_OF_TICKETS_PER_BOOK));
				gameDetailsBean.setNbrOfBooks(resultSet
						.getInt(TableConstants.SGM_NBR_OF_BOOKS));

			}

			return gameDetailsBean;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);

		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	/**
	 * This method returns the list of remaining prizes for the passed game
	 * 
	 * @param gameId
	 * @return List
	 * @throws LMSException
	 */
	public List<PrizeStatusBean> fetchRemainingPrizeList(int gameId)
			throws LMSException {
		return GameUtilityHelper.fetchRemainingPrizeList(gameId);

		/*
		 * Connection connection = null; PreparedStatement pstmt = null;
		 * PreparedStatement prizePstmt = null; ResultSet resultSet = null;
		 * ResultSet prizeRSet = null;
		 * 
		 * List<PrizeStatusBean> prizeStatusList = new ArrayList<PrizeStatusBean>();
		 * 
		 * 
		 * double pwtAmt = 0.0; int nbrOfPrizeLeft = 0;
		 * 
		 * 
		 * try {
		 * 
		 * PrizeStatusBean bean = null; // create database connection DBConnect
		 *   connection = DBConnect.getConnection(); //
		 * fetch remaining prizes for the passed game String prizeQuery =
		 * QueryManager.getST1DistinctPrizeQuery(); prizePstmt =
		 * connection.prepareStatement(prizeQuery); prizePstmt.setInt(1,gameId);
		 * 
		 * prizeRSet = prizePstmt.executeQuery();
		 * 
		 * while(prizeRSet.next()){
		 * 
		 * bean = new PrizeStatusBean();
		 * bean.setPrizeAmt(prizeRSet.getDouble(TableConstants.SPI_PWT_AMT));
		 * //bean.setNbrOfPrizeLeft(0);
		 * 
		 * prizeStatusList.add(bean); }
		 * 
		 * 
		 * String query = QueryManager.getST1PrizeListQuery(); pstmt =
		 * connection.prepareStatement(query); pstmt.setInt(1, gameId);
		 * 
		 * resultSet = pstmt.executeQuery();
		 * 
		 * 
		 * while(resultSet.next()){ //isPrizeRemaining = true;
		 * 
		 * pwtAmt = resultSet.getDouble(TableConstants.SPI_PWT_AMT);
		 * nbrOfPrizeLeft = resultSet.getInt(TableConstants.PRIZES_REMAINING);
		 * 
		 * for(int i=0; i< prizeStatusList.size(); i++){ bean =
		 * prizeStatusList.get(i); if (pwtAmt == bean.getPrizeAmt()){
		 * bean.setNbrOfPrizeLeft(nbrOfPrizeLeft); break; } } }
		 * 
		 * 
		 * 
		 * 
		 * return prizeStatusList; } catch (SQLException e) {
		 * 
		 * e.printStackTrace(); throw new LMSException(e); }finally {
		 * 
		 * try {
		 * 
		 * if (pstmt != null) { pstmt.close(); } if (connection != null) {
		 * connection.close(); } } catch (SQLException se) {
		 * se.printStackTrace(); } }
		 * 
		 * 
		 * //return null;
		 */

	}

}
