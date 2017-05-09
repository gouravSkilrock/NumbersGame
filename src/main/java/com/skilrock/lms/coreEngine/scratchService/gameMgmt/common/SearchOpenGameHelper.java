package com.skilrock.lms.coreEngine.scratchService.gameMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.OpenGameBean;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;

/**
 * This helper class provides methods to maintain game life cycle and to get
 * game details
 * 
 * @author Skilrock Technologies
 * 
 */
public class SearchOpenGameHelper {
	/**
	 * This method inside helper class is used to provide list of remaining
	 * prizes for a game
	 * 
	 * @param gameId
	 * @return List of remaining prizes
	 * @throws LMSException
	 */

	public List<PrizeStatusBean> fetchRemainingPrizeList(int gameId)
			throws LMSException {
		return GameUtilityHelper.fetchRemainingPrizeList(gameId);

		/*
		 * Connection connection = null;
		 * 
		 * PreparedStatement pstmt = null; PreparedStatement prizePstmt = null;
		 * 
		 * ResultSet resultSet = null; ResultSet prizeRSet = null;
		 * 
		 * 
		 * List<PrizeStatusBean> prizeStatusList = new ArrayList<PrizeStatusBean>();
		 * 
		 * 
		 * double pwtAmt = 0.0; int nbrOfPrizeLeft = 0;
		 * 
		 * 
		 * try {
		 * 
		 * PrizeStatusBean bean = null;  
		 * connection = DBConnect.getConnection();
		 * 
		 * String prizeQuery = QueryManager.getST1DistinctPrizeQuery();
		 * prizePstmt = connection.prepareStatement(prizeQuery);
		 * prizePstmt.setInt(1,gameId);
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
		 * e.printStackTrace(); throw new LMSException(e);
		 * 
		 * }finally {
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

	/**
	 * This method is used to set the status of OPEN game to SALE_HOLD
	 * 
	 * @param gameid
	 *            is game's ID
	 * @throws LMSException
	 */
	public void saleOpenStatus(int gameid) throws LMSException {

		int gameId = gameid;
		Connection connection = null;
		Statement statement = null;

		try {

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			String updateQueryManager = QueryManager.updateST3CloseSaleStatus()
					+ " where game_id=" + gameId + " ";
			statement.executeUpdate(updateQueryManager);
			// stmt7.executeUpdate("update st_se_game_master set
			// game_status='TERMINATE' where game_id="+y+"");

			connection.commit();
			connection.close();

		} catch (SQLException e) {

			try {
				connection.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	/**
	 * This method inside helper class is used to search sale close games
	 * 
	 * @return List of games
	 * @throws LMSException
	 */
	public List searcCloseSale() throws LMSException {

		int gameId;
		Connection connection = null;
		Statement statement = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		DateFormat df = null;
		try {

			OpenGameBean gameBean = null;
			List<OpenGameBean> searchResults = new ArrayList<OpenGameBean>();

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			statement = connection.createStatement();
			stmt1 = connection.createStatement();
			stmt2 = connection.createStatement();
			System.out.println("heeeeeee");

			String saleCloseGames = QueryManager.getST3SaleCloseGames();
			rs = stmt1.executeQuery(saleCloseGames);
			// rs = stmt1.executeQuery("select game_id from st_se_game_master
			// where game_status='OPEN' and CURRENT_DATE > pwt_end_date ");

			while (rs.next()) {

				gameId = rs.getInt(TableConstants.GAME_ID);
				String holdGames = QueryManager.holdST3SaleGames()
						+ "  where game_id='" + gameId + "'   ";
				stmt2.executeUpdate(holdGames);
				// stmt2.executeUpdate("update st_se_game_master set
				// game_status='CLOSE' where game_id='"+gameId+"'");

			}

			String selectHoldGames = QueryManager.getST3SaleHoldGames();
			resultSet = statement.executeQuery(selectHoldGames);
			// resultSet = statement.executeQuery("select * from
			// st_se_game_master where game_status='CLOSE'");

			while (resultSet.next()) {
				System.out.println("Helper Result Set"
						+ resultSet.getInt(TableConstants.GAME_ID));
				gameBean = new OpenGameBean();
				gameBean.setGameId(resultSet.getInt(TableConstants.GAME_ID));
				gameBean.setGameNbr(resultSet.getInt(TableConstants.GAME_NBR));
				gameBean.setGameName(resultSet
						.getString(TableConstants.GAME_NAME));
				gameBean.setGameStartDate(df.format(resultSet
								.getDate(TableConstants.START_DATE)));
				gameBean.setGameStatus(resultSet
						.getString(TableConstants.GAME_STATUS));
				gameBean.setPwt_end_date(df.format(resultSet
								.getDate(TableConstants.PWT_END_DATE)));
				gameBean.setSaleEndDate(df.format(resultSet
						.getDate(TableConstants.SALE_END_DATE)));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(gameBean);

			}

			connection.commit();
			connection.close();
			return searchResults;

		} catch (SQLException e) {

			try {
				connection.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);
		}

		// return null;

	}

	// this method is commented to remove duplicacy from the code and method
	// searchGameDetails() is used at the place of it

	/**
	 * This method inside helper class is used to provide game details
	 * 
	 * @param gameid
	 * @return List
	 * @throws LMSException
	 */
	/*
	 * 
	 * public List saleCloseGameDetails(int gameid) throws LMSException{
	 * 
	 * 
	 * int gameId=gameid; Connection connection = null; Statement statement =
	 * null; //Statement stmt1 = null; //Statement stmt2 = null; ResultSet
	 * resultSet = null; //ResultSet rs = null; try {
	 * 
	 * OpenGameBean gameBean = null; List<OpenGameBean> searchResults = new
	 * ArrayList<OpenGameBean>();
	 * 
	 *   connection =
	 * dbConnect.getConnection(); statement = connection.createStatement();
	 * //stmt1 = connection.createStatement(); //stmt2 =
	 * connection.createStatement(); System.out.println("heeeeeee"); String
	 * selectGameDetails=QueryManager.getST3GamesDetails() + " where
	 * game_id='"+gameId+"' "; resultSet =
	 * statement.executeQuery(selectGameDetails); System.out.println("game id is " +
	 * gameId);
	 * 
	 * 
	 * while(resultSet.next()){ System.out.println("Helper game
	 * Set"+resultSet.getInt(TableConstants.GAME_ID) ); gameBean = new
	 * OpenGameBean();
	 * gameBean.setGameId(resultSet.getInt(TableConstants.GAME_ID));
	 * 
	 * gameBean.setGameName(resultSet.getString(TableConstants.GAME_NAME));
	 * gameBean.setGameStartDate(resultSet.getString(TableConstants.START_DATE));
	 * gameBean.setGameStatus(resultSet.getString(TableConstants.GAME_STATUS));
	 * gameBean.setPwt_end_date(resultSet.getString(TableConstants.PWT_END_DATE));
	 * gameBean.setNbrTickets(resultSet.getInt(TableConstants.NO_OF_BOOKS));
	 * gameBean.setNbrBooks(resultSet.getInt(TableConstants.NO_OF_TICKETS));
	 * gameBean.setGameNbr(resultSet.getInt(TableConstants.GAME_NBR));
	 * gameBean.setSaleEndDate(resultSet.getString(TableConstants.SALE_END_DATE));
	 * //userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
	 * searchResults.add(gameBean); } connection.close();
	 * System.out.println(gameBean.getSaleEndDate()); return searchResults; }
	 * catch (SQLException e) {
	 * 
	 * e.printStackTrace(); throw new LMSException(e); }
	 * 
	 * //return null; }
	 * 
	 */

	/**
	 * This method is used to get the game details
	 * 
	 * @param gameid
	 *            is game's Id
	 * @return List of game details
	 * @throws LMSException
	 */

	public List searchGameDetails(int gameid) throws LMSException {

		int gameId = gameid;
		Connection connection = null;
		Statement statement = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		ResultSet rs1 = null;
		DateFormat df = null;
		try {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			OpenGameBean gameBean = null;
			List<OpenGameBean> searchResults = new ArrayList<OpenGameBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			stmt = connection.createStatement();
			System.out.println("heeeeeee");
			gameBean = new OpenGameBean();

			rs1 = stmt
					.executeQuery(QueryManager.getRemainingBooksAtBO()
							+ " where game_id='" + gameId
							+ "' and current_owner='BO' ");

			while (rs1.next()) {
				// bookRemaining=bookRemaining+1;
				gameBean.setBookRemaining(rs1.getInt(1));

			}
			System.out.println("book number remaining are   "
					+ gameBean.getBookRemaining());
			String selectGameDetails = QueryManager.getST3GamesDetails()
					+ "  where game_id='" + gameId + "'   ";
			resultSet = statement.executeQuery(selectGameDetails);
			System.out.println("game id is  " + gameId);

			while (resultSet.next()) {
				System.out.println("Helper game  Set"
						+ resultSet.getInt(TableConstants.GAME_ID));
				// gameBean = new OpenGameBean();
				gameBean.setGameId(resultSet.getInt(TableConstants.GAME_ID));

				gameBean.setGameName(resultSet
						.getString(TableConstants.GAME_NAME));
				gameBean.setGameStartDate(df.format(resultSet
						.getDate(TableConstants.START_DATE)));
				gameBean.setGameStatus(resultSet
						.getString(TableConstants.GAME_STATUS));
				gameBean.setPwt_end_date(df.format(resultSet
						.getDate(TableConstants.PWT_END_DATE)));
				gameBean.setSaleEndDate(df.format(resultSet
						.getDate(TableConstants.SALE_END_DATE)));
				gameBean.setNbrTickets(resultSet
						.getInt(TableConstants.NO_OF_TICKETS));
				gameBean.setNbrBooks(resultSet
						.getInt(TableConstants.NO_OF_BOOKS));
				gameBean.setGameNbr(resultSet.getInt(TableConstants.GAME_NBR));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(gameBean);

			}

			return searchResults;

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

		// return null;

	}

	/**
	 * This method is used to search closed games
	 * 
	 * @return List of closed games
	 * @throws LMSException
	 */
	public List searchUser() throws LMSException {

		int gameId;
		Connection connection = null;
		Statement statement = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		DateFormat df = null;
		try {

			OpenGameBean gameBean = null;
			List<OpenGameBean> searchResults = new ArrayList<OpenGameBean>();

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			statement = connection.createStatement();
			stmt1 = connection.createStatement();
			stmt2 = connection.createStatement();
			System.out.println("hhhhhhhhhh");

			String expireGames = QueryManager.selectST3ExpireGames();
			rs = stmt1.executeQuery(expireGames);
			// rs = stmt1.executeQuery("select game_id from st_se_game_master
			// where game_status='OPEN' and CURRENT_DATE > pwt_end_date ");

			while (rs.next()) {

				gameId = rs.getInt(TableConstants.GAME_ID);
				String closeExpireGames = QueryManager.closeST3ExpireGames()
						+ "  where game_id='" + gameId + "'   ";
				stmt2.executeUpdate(closeExpireGames);
				// stmt2.executeUpdate("update st_se_game_master set
				// game_status='CLOSE' where game_id='"+gameId+"'");

			}

			String selectClosedGames = QueryManager.selectST3ClosedGames();
			resultSet = statement.executeQuery(selectClosedGames);
			// resultSet = statement.executeQuery("select * from
			// st_se_game_master where game_status='CLOSE'");

			while (resultSet.next()) {
				System.out.println("Helper Result Set"
						+ resultSet.getInt(TableConstants.GAME_ID));
				gameBean = new OpenGameBean();
				gameBean.setGameId(resultSet.getInt(TableConstants.GAME_ID));
				gameBean.setGameNbr(resultSet.getInt(TableConstants.GAME_NBR));
				gameBean.setGameName(resultSet
						.getString(TableConstants.GAME_NAME));
				gameBean.setGameStartDate(df.format(resultSet
								.getDate(TableConstants.START_DATE)));
				gameBean.setGameStatus(resultSet
						.getString(TableConstants.GAME_STATUS));
				gameBean.setPwt_end_date(df.format(resultSet
						.getDate(TableConstants.PWT_END_DATE)));
				gameBean.setSaleEndDate(df.format(resultSet
						.getDate(TableConstants.SALE_END_DATE)));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(gameBean);

			}

			connection.commit();
			connection.close();

			return searchResults;

		} catch (SQLException e) {

			try {
				connection.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);
		}

		// return null;

	}

}
