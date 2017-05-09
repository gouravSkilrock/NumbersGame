package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OpenGameBean;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.beans.TaskBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This helper class provides methods to submit Unclaim Pwt,Govt comm and
 * methods to get game details
 * 
 * @author ABC
 * 
 */
public class TDSSubmitHelper {
	static Log logger = LogFactory.getLog(TDSSubmitHelper.class);

	/**
	 * This method is used to get the list of TDS Approved
	 * 
	 * @return List of TDS Approved
	 * @throws LMSException
	 */
	int receiptId = 0;

	/**
	 * This method is used to get the list of remaining prizes
	 * 
	 * @param gameId
	 *            is game's Id
	 * @return List of remaining prizes
	 * @throws LMSException
	 */
	public List<PrizeStatusBean> fetchRemainingPrizeList(int gameId)
			throws LMSException {

		return new GameUtilityHelper().fetchRemainingPrizeList(gameId);

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
		 * 
		 */
	}

	public List<PrizeStatusBean> fetchRemainingPrizeListUnclm(int gameId)
			throws LMSException {
		return new GameUtilityHelper().fetchRemainingPrizeList(gameId);

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
		 * String query = QueryManager.getST3PrizeListQuery(); pstmt =
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
		 * 
		 */
	}

	// this method is commented to remove code duplicity at the time of self
	// review
	/*
	 * public void submitGovtComm(int id,String transType,int gameid,double
	 * Amount) throws LMSException{ int taskId=id; String
	 * transactionType=transType; int gameId=gameid; double amount=Amount; int
	 * transId=0;   Connection con= null;
	 * try {
	 * 
	 * con=dbConnect.getConnection(); con.setAutoCommit(false); Statement
	 * stmt5=con.createStatement(); Statement stmt6=con.createStatement();
	 * Statement stmt7=con.createStatement();
	 * 
	 * logger.debug("task id is " + taskId); String queryForGovtCommtSubmit =
	 * QueryManager.submitST3UnclmPwtApproved()+ "where task_id='"+taskId+"' ";
	 * stmt5.executeUpdate(queryForGovtCommtSubmit); String
	 * insertTransMaster=QueryManager.insertST3TransactionMaster() + "
	 * values('GOVT',CURRENT_TIMESTAMP,'"+transactionType+"') " ;
	 * stmt6.executeUpdate(insertTransMaster); ResultSet
	 * rs=stmt6.getGeneratedKeys(); while(rs.next()) { transId=rs.getInt(1);
	 * logger.debug("trans id is "+transId); }
	 * 
	 * 
	 * String insertGovtTrans=QueryManager.insertST3GovtTransaction() + "
	 * values("+transId+","+amount+","+gameId+",'"+transactionType+"') " ;
	 * stmt7.executeUpdate(insertGovtTrans); logger.debug( "okkkkkkkkk...");
	 * logger.debug("gameid is " + gameId ); con.commit(); } catch (SQLException
	 * e) {
	 * 
	 * 
	 * try { con.rollback(); } catch (SQLException se) { // TODO Auto-generated
	 * catch block se.printStackTrace(); throw new LMSException("Error During
	 * Rollback",se); } e.printStackTrace(); throw new LMSException(e); } }
	 * 
	 */
	/**
	 * This method is used to get the game details for a given game Id
	 * 
	 * @param gameid
	 *            is game's Id
	 * @return List of game details
	 * @throws LMSException
	 */
	public List getGameDetails(int gameid, String serviceCode)
			throws LMSException {

		int gameId = gameid;

		 
		Connection con = null;
		OpenGameBean gameBean = null;
		List<OpenGameBean> gameDetailsResults = new ArrayList<OpenGameBean>();
		try {

			con = DBConnect.getConnection();
			Statement stmt = con.createStatement();
			logger.debug("Game id is  " + gameId);
			if (serviceCode.equalsIgnoreCase("SE")) {
				String gameDetails = QueryManager.getST3GamesDetails()
						+ " where game_id= " + gameId + " ";
				ResultSet rs = stmt.executeQuery(gameDetails);
				while (rs.next()) {
					gameBean = new OpenGameBean();
					logger.debug(rs.getString("game_name"));
					logger.debug(rs.getString(TableConstants.GAME_NAME));

					gameBean
							.setGameName(rs.getString(TableConstants.GAME_NAME));
					gameBean.setGameNbr(rs.getInt(TableConstants.GAME_NBR));
					gameBean.setGameStartDate(rs
							.getString(TableConstants.START_DATE));
					gameBean.setGameStatus(rs
							.getString(TableConstants.GAME_STATUS));
					gameBean.setSaleEndDate(rs
							.getString(TableConstants.SALE_END_DATE));
					gameBean.setTicketPrice(rs
							.getDouble(TableConstants.TICKET_PRICE));
					gameBean.setPwt_end_date(rs
							.getString(TableConstants.PWT_END_DATE));
					gameDetailsResults.add(gameBean);
				}

				con.close();
				return gameDetailsResults;
			} else if (serviceCode.equalsIgnoreCase("DG")) {
				String gameDetails = "select * from st_dg_game_master where game_id= "
						+ gameId + " ";
				ResultSet rs = stmt.executeQuery(gameDetails);
				while (rs.next()) {
					gameBean = new OpenGameBean();
					gameBean.setGameName(rs.getString("game_name"));
					gameBean.setGameNbr(rs.getInt("game_nbr"));
					gameBean.setGameStatus(rs.getString("game_status"));
					gameDetailsResults.add(gameBean);
				}

				con.close();
				return gameDetailsResults;
			} else if (serviceCode.equalsIgnoreCase("SLE")) {
				String gameDetails = "SELECT * FROM st_sle_game_type_master WHERE game_type_id="+gameId+";";
				ResultSet rs = stmt.executeQuery(gameDetails);
				while (rs.next()) {
					gameBean = new OpenGameBean();
					gameBean.setGameName(rs.getString("type_disp_name"));
					gameBean.setGameNbr(rs.getInt("game_type_id"));
					gameBean.setGameStatus(rs.getString("type_status"));
					gameDetailsResults.add(gameBean);
				}
				con.close();
				return gameDetailsResults;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
		// return null;
		return gameDetailsResults;
	}

	private int insertRcptMapping(Connection con, long transId, int id)
			throws SQLException {

		// START INSERTION OF RECEIPT ID FOR GOVERNMENT RECEIPTS

		String insBoRcpt = QueryManager.insertInBOReceipts();
		String insRcpTrMap = QueryManager.insertBOReceiptTrnMapping();

		PreparedStatement preBoRcpt = null;
		PreparedStatement preRcpTrMap = null;
		// PreparedStatement preRcpGenMap = null;

		PreparedStatement autoGenPstmt = null;
		// String getLatestRecieptNumber="SELECT * from
		// st_bo_receipt_gen_mapping where receipt_type=? ORDER BY generated_id
		// DESC LIMIT 1 ";
		autoGenPstmt = con
				.prepareStatement(QueryManager.getBOLatestReceiptNb());
		autoGenPstmt.setString(1, "GOVT_RCPT");
		ResultSet recieptRs = autoGenPstmt.executeQuery();
		String lastRecieptNoGenerated = null;

		while (recieptRs.next()) {
			lastRecieptNoGenerated = recieptRs.getString("generated_id");
		}

		String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo("GOVT_RCPT",
				lastRecieptNoGenerated, "BO");

		// insert receipt master

		preBoRcpt = con.prepareStatement(QueryManager.insertInReceiptMaster());
		preBoRcpt.setString(1, "BO");
		preBoRcpt.executeUpdate();

		ResultSet rss = preBoRcpt.getGeneratedKeys();
		rss.next();
		id = rss.getInt(1);

		// insert into bo receipts

		preBoRcpt = con.prepareStatement(insBoRcpt);
		preBoRcpt.setInt(1, id);
		preBoRcpt.setString(2, "GOVT_RCPT");
		preBoRcpt.setObject(3, null);
		preBoRcpt.setString(4, "GOVT");
		preBoRcpt.setString(5, autoGeneRecieptNo);
		preBoRcpt.setTimestamp(6, Util.getCurrentTimeStamp());
		/*
		 * //prepare4.setString(1, autoGeneRecieptNo); preBoRcpt.setString(1,
		 * "GOVT_RCPT"); preBoRcpt.setString(2, null);
		 */

		preBoRcpt.executeUpdate();

		preRcpTrMap = con.prepareStatement(insRcpTrMap);
		preRcpTrMap.setInt(1, id);
		preRcpTrMap.setLong(2, transId);
		preRcpTrMap.executeUpdate();

		/*
		 * //insert into recipt gen reciept mapping String
		 * updateBoRecieptGenMapping=QueryManager.updateST5BOReceiptGenMapping();
		 * preRcpGenMap=con.prepareStatement(updateBoRecieptGenMapping);
		 * preRcpGenMap.setInt(1,id);
		 * preRcpGenMap.setString(2,autoGeneRecieptNo);
		 * preRcpGenMap.setString(3,"GOVT_RCPT"); preRcpGenMap.executeUpdate();
		 */

		// END INSERTION OF RECEIPT ID FOR GOVERNMENT RECEIPTS
		return id;
	}

	private int insertRcptMappingAgt(Connection con, long  transId, int id,
			int agtOrgId) throws SQLException {

		// START INSERTION OF RECEIPT ID FOR GOVERNMENT RECEIPTS For AGENTS

		// String insAgtRcpt = QueryManager.getST4InsertAgentReceipts();
		// String insRcpTrMap =
		// QueryManager.getST4InsertAgentReceiptsTrnMapping();

		PreparedStatement preAgtRcpt = null;
		PreparedStatement preRcpTrMap = null;
		// PreparedStatement preRcpGenMap = null;

		PreparedStatement autoGenPstmt = null;
		// String getLatestRecieptNumber="SELECT * from
		// st_lms_agent_receipts_gen_mapping where receipt_type=? ORDER BY
		// generated_id DESC LIMIT 1 ";
		autoGenPstmt = con.prepareStatement(QueryManager
				.getAGENTLatestReceiptNb());
		autoGenPstmt.setString(1, "GOVT_RCPT");
		autoGenPstmt.setInt(2, agtOrgId);
		ResultSet recieptRs = autoGenPstmt.executeQuery();
		String lastRecieptNoGenerated = null;

		while (recieptRs.next()) {
			lastRecieptNoGenerated = recieptRs.getString("generated_id");
		}

		String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
				"GOVT_RCPT", lastRecieptNoGenerated, "AGENT", agtOrgId);

		// insert in receipt master
		preAgtRcpt = con.prepareStatement(QueryManager.insertInReceiptMaster());
		preAgtRcpt.setString(1, "AGENT");
		preAgtRcpt.executeUpdate();

		ResultSet rss = preAgtRcpt.getGeneratedKeys();
		rss.next();
		id = rss.getInt(1);

		// insert in agent receipts
		preAgtRcpt = con.prepareStatement(QueryManager.insertInAgentReceipts());

		preAgtRcpt.setInt(1, id);
		preAgtRcpt.setString(2, "GOVT_RCPT");
		preAgtRcpt.setInt(3, agtOrgId);
		preAgtRcpt.setObject(4, null);
		preAgtRcpt.setString(5, "GOVT");
		preAgtRcpt.setString(6, autoGeneRecieptNo);
		preAgtRcpt.setTimestamp(7, Util.getCurrentTimeStamp());

		/*
		 * //prepare4.setString(1, autoGeneRecieptNo); preAgtRcpt.setString(1,
		 * "GOVT_RCPT"); preAgtRcpt.setInt(2, agtOrgId); preAgtRcpt.setString(3,
		 * null);
		 */

		preAgtRcpt.executeUpdate();

		// insert in agent trn mapping

		preRcpTrMap = con.prepareStatement(QueryManager
				.insertAgentReceiptTrnMapping());
		preRcpTrMap.setInt(1, id);
		preRcpTrMap.setLong(2, transId);
		preRcpTrMap.executeUpdate();

		/*
		 * //insert into recipt gen reciept mapping String
		 * updateBoRecieptGenMapping=QueryManager.updateST5AGENTReceiptGenMappimg();
		 * preRcpGenMap=con.prepareStatement(updateBoRecieptGenMapping);
		 * preRcpGenMap.setInt(1,id);
		 * preRcpGenMap.setString(2,autoGeneRecieptNo);
		 * preRcpGenMap.setString(3,"GOVT_RCPT");
		 * preRcpGenMap.setInt(4,agtOrgId); preRcpGenMap.executeUpdate();
		 */

		// END INSERTION OF RECEIPT ID FOR GOVERNMENT RECEIPTS For AGENTS
		return id;
	}

	public void submitTdsDG(Integer id, String transType, String Month,
			Double Amount, Integer userOrgId, Integer userId, String rootPath,
			String userOrgName) throws LMSException {
		int taskId = id;
		String transactionType = transType;
		String monthTds = Month;
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		Date month = null;

		try {
			month = d.parse(monthTds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double amount = Amount;
		//int transId = 0;
		long transId=0;
		logger.debug("month is   " + month);
		 
		Connection con = null;
		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt4 = con.createStatement();
			Statement stmt11 = con.createStatement();

			logger.debug("task id is  " + taskId);

			// set the status as done in st_bo_task table
			String queryForTdstSubmit = QueryManager
					.submitST3UnclmPwtApproved()
					+ "where task_id='" + taskId + "' ";
			stmt4.executeUpdate(queryForTdstSubmit);

			// insert in LMS Transaction Master
			/*String insertLMSTrans = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement LMSMasPstmt = con
					.prepareStatement(insertLMSTrans);
			LMSMasPstmt.setString(1, "BO");
			LMSMasPstmt.executeUpdate();*/
			PreparedStatement LMSMasPstmt = con.prepareStatement("insert into st_lms_transaction_master (user_type, service_code, interface) values('BO','DG','WEB')");
			LMSMasPstmt.executeUpdate();
			ResultSet rs = LMSMasPstmt.getGeneratedKeys();
			while (rs.next()) {
				transId = rs.getLong(1);
				logger.debug("trans id is " + transId);

			}

			// String
			// insertTransMaster=QueryManager.insertST3TransactionMaster() + "
			// values('GOVT','"+new java.sql.Timestamp( new
			// Date().getTime())+"','"+transactionType+"') " ;
			String insertTransMaster = QueryManager
					.insertInBOTransactionMaster();
			PreparedStatement BOMasPstmt = con
					.prepareStatement(insertTransMaster);
			BOMasPstmt.setLong(1, transId);
			BOMasPstmt.setInt(2, userId);
			BOMasPstmt.setInt(3, userOrgId);
			BOMasPstmt.setString(4, "GOVT");
			BOMasPstmt.setObject(5, null);
			BOMasPstmt.setTimestamp(6, new java.sql.Timestamp(new Date()
					.getTime()));
			BOMasPstmt.setString(7, transactionType);

			BOMasPstmt.executeUpdate();

			// done by arun
			// String
			// insertGovtTrans=QueryManager.insertST3GovtTransactionForTDS() + "
			// values("+transId+","+amount+",'"+new
			// java.sql.Date(month.getTime())+"','"+transactionType+"') " ;
			String insertGovtTrans = QueryManager
					.insertST3GovtTransactionForTDS()
					+ "   select "
					+ transId
					+ ", 'DG', amount, month, transaction_type, start_date, end_date from st_lms_bo_tasks where task_id="
					+ taskId;
			stmt11.executeUpdate(insertGovtTrans);
			logger.debug("executed query is " + insertGovtTrans);
			// logger.debug("month is " + month );

			receiptId = insertRcptMapping(con, transId, id);

			con.commit();

			GraphReportHelper reportHelper = new GraphReportHelper();
			reportHelper.createTextReportBO(receiptId, userOrgName, userOrgId,
					rootPath);

		} catch (SQLException e) {

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);

			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}

	}

	public void submitTdsSE(Integer id, String transType, String Month,
			Double Amount, Integer userOrgId, Integer userId, String rootPath,
			String userOrgName) throws LMSException {
		int taskId = id;
		String transactionType = transType;
		String monthTds = Month;
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		Date month = null;

		try {
			month = d.parse(monthTds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double amount = Amount;
		int transId = 0;
		logger.debug("month is   " + month);
		 
		Connection con = null;
		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt4 = con.createStatement();
			Statement stmt11 = con.createStatement();

			logger.debug("task id is  " + taskId);

			// set the status as done in st_bo_task table
			String queryForTdstSubmit = QueryManager
					.submitST3UnclmPwtApproved()
					+ "where task_id='" + taskId + "' ";
			stmt4.executeUpdate(queryForTdstSubmit);

			// insert in LMS Transaction Master
			/*String insertLMSTrans = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement LMSMasPstmt = con
					.prepareStatement(insertLMSTrans);
			LMSMasPstmt.setString(1, "BO");
			LMSMasPstmt.executeUpdate();*/
			PreparedStatement LMSMasPstmt = con.prepareStatement("insert into st_lms_transaction_master (user_type, service_code, interface) values('BO','SE','WEB')");
			LMSMasPstmt.executeUpdate();
			ResultSet rs = LMSMasPstmt.getGeneratedKeys();
			while (rs.next()) {
				transId = rs.getInt(1);
				logger.debug("trans id is " + transId);

			}

			// String
			// insertTransMaster=QueryManager.insertST3TransactionMaster() + "
			// values('GOVT','"+new java.sql.Timestamp( new
			// Date().getTime())+"','"+transactionType+"') " ;
			String insertTransMaster = QueryManager
					.insertInBOTransactionMaster();
			PreparedStatement BOMasPstmt = con
					.prepareStatement(insertTransMaster);
			BOMasPstmt.setInt(1, transId);
			BOMasPstmt.setInt(2, userId);
			BOMasPstmt.setInt(3, userOrgId);
			BOMasPstmt.setString(4, "GOVT");
			BOMasPstmt.setObject(5, null);
			BOMasPstmt.setTimestamp(6, new java.sql.Timestamp(new Date()
					.getTime()));
			BOMasPstmt.setString(7, transactionType);

			BOMasPstmt.executeUpdate();

			// done by arun
			// String
			// insertGovtTrans=QueryManager.insertST3GovtTransactionForTDS() + "
			// values("+transId+","+amount+",'"+new
			// java.sql.Date(month.getTime())+"','"+transactionType+"') " ;
			String insertGovtTrans = QueryManager
					.insertST3GovtTransactionForTDS()
					+ "   select "
					+ transId
					+ ", 'SE', amount, month, transaction_type, start_date, end_date from st_lms_bo_tasks where task_id="
					+ taskId;
			stmt11.executeUpdate(insertGovtTrans);
			logger.debug("executed query is " + insertGovtTrans);
			// logger.debug("month is " + month );

			receiptId = insertRcptMapping(con, transId, id);

			con.commit();

			GraphReportHelper reportHelper = new GraphReportHelper();
			reportHelper.createTextReportBO(receiptId, userOrgName, userOrgId,
					rootPath);

		} catch (SQLException e) {

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);

			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}

	}
	
	public void submitTdsIW(Integer id, String transType, String Month,
			Double Amount, Integer userOrgId, Integer userId, String rootPath,
			String userOrgName) throws LMSException {
		int taskId = id;
		String transactionType = transType;
		String monthTds = Month;
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		Date month = null;

		try {
			month = d.parse(monthTds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double amount = Amount;
		int transId = 0;
		logger.debug("month is   " + month);
		 
		Connection con = null;
		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt4 = con.createStatement();
			Statement stmt11 = con.createStatement();

			logger.debug("task id is  " + taskId);

			// set the status as done in st_bo_task table
			String queryForTdstSubmit = QueryManager
					.submitST3UnclmPwtApproved()
					+ "where task_id='" + taskId + "' ";
			stmt4.executeUpdate(queryForTdstSubmit);

			// insert in LMS Transaction Master
			/*String insertLMSTrans = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement LMSMasPstmt = con
					.prepareStatement(insertLMSTrans);
			LMSMasPstmt.setString(1, "BO");
			LMSMasPstmt.executeUpdate();*/
			PreparedStatement LMSMasPstmt = con.prepareStatement("insert into st_lms_transaction_master (user_type, service_code, interface) values('BO','IW','WEB')");
			LMSMasPstmt.executeUpdate();
			ResultSet rs = LMSMasPstmt.getGeneratedKeys();
			while (rs.next()) {
				transId = rs.getInt(1);
				logger.debug("trans id is " + transId);

			}

			// String
			// insertTransMaster=QueryManager.insertST3TransactionMaster() + "
			// values('GOVT','"+new java.sql.Timestamp( new
			// Date().getTime())+"','"+transactionType+"') " ;
			String insertTransMaster = QueryManager
					.insertInBOTransactionMaster();
			PreparedStatement BOMasPstmt = con
					.prepareStatement(insertTransMaster);
			BOMasPstmt.setInt(1, transId);
			BOMasPstmt.setInt(2, userId);
			BOMasPstmt.setInt(3, userOrgId);
			BOMasPstmt.setString(4, "GOVT");
			BOMasPstmt.setObject(5, null);
			BOMasPstmt.setTimestamp(6, new java.sql.Timestamp(new Date()
					.getTime()));
			BOMasPstmt.setString(7, transactionType);

			BOMasPstmt.executeUpdate();

			// done by arun
			// String
			// insertGovtTrans=QueryManager.insertST3GovtTransactionForTDS() + "
			// values("+transId+","+amount+",'"+new
			// java.sql.Date(month.getTime())+"','"+transactionType+"') " ;
			String insertGovtTrans = QueryManager
					.insertST3GovtTransactionForTDS()
					+ "   select "
					+ transId
					+ ", 'IW', amount, month, transaction_type, start_date, end_date from st_lms_bo_tasks where task_id="
					+ taskId;
			stmt11.executeUpdate(insertGovtTrans);
			logger.debug("executed query is " + insertGovtTrans);
			// logger.debug("month is " + month );

			receiptId = insertRcptMapping(con, transId, id);

			con.commit();

			GraphReportHelper reportHelper = new GraphReportHelper();
			reportHelper.createTextReportBO(receiptId, userOrgName, userOrgId,
					rootPath);

		} catch (SQLException e) {

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);

			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}

	}

	/**
	 * This method inside helper class is used to submit unclaimed pwt for game
	 * 
	 * @param id
	 *            is the task id from task table
	 * @param transType
	 *            is the transaction type
	 * @param gameid
	 *            is game's Id
	 * @param Amount
	 * @param userOrgName
	 * @param rootPath
	 * @throws LMSException
	 */
	public void submitUnclmPwt(int id, String transType, int gameid,
			double Amount, int userOrgId, int userId, String rootPath,
			String userOrgName, String serviceCode) throws LMSException {
		int taskId = id;
		String transactionType = transType;
		int gameId = gameid;
		double amount = Amount;
		//int transId = 0;
		 long transId =0;
		Connection con = null;

		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt3 = con.createStatement();
			// Statement stmt8=con.createStatement();
			// Statement stmt9=con.createStatement();

			// update bo_task table
			logger.debug("task id is  " + taskId);
			String queryForUnclmPwtSubmit = QueryManager
					.submitST3UnclmPwtApproved()
					+ "where task_id='" + taskId + "' ";
			stmt3.executeUpdate(queryForUnclmPwtSubmit);

			// insert in LMS transaction master
			/*PreparedStatement LMSMasPstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			LMSMasPstmt.setString(1, "BO");
			LMSMasPstmt.executeUpdate();*/
			PreparedStatement LMSMasPstmt = con.prepareStatement("insert into st_lms_transaction_master (user_type, service_code, interface) values('BO','"+ serviceCode +"','WEB')");
			System.out.println("serviceCode: "+serviceCode + "***  sewfw: "+LMSMasPstmt);
			LMSMasPstmt.executeUpdate();
			ResultSet rs = LMSMasPstmt.getGeneratedKeys();
			
			while (rs.next()) {
				transId = rs.getLong(1);
			}

			// insert details into transaction master

			/*
			 * String
			 * insertTransMaster=QueryManager.insertST3TransactionMaster() + "
			 * values('GOVT','"+new java.sql.Timestamp( new
			 * Date().getTime())+"','"+transactionType+"') " ;
			 * stmt8.executeUpdate(insertTransMaster);
			 */

			PreparedStatement BOMasPstmt = con.prepareStatement(QueryManager
					.insertInBOTransactionMaster());
			BOMasPstmt.setLong(1, transId);
			BOMasPstmt.setInt(2, userId);
			BOMasPstmt.setInt(3, userOrgId);
			BOMasPstmt.setString(4, "GOVT");
			BOMasPstmt.setObject(5, null);
			BOMasPstmt.setTimestamp(6, new java.sql.Timestamp(new Date()
					.getTime()));
			BOMasPstmt.setString(7, transactionType);

			BOMasPstmt.executeUpdate();

			// insert details into bo_gov_transaction
			// String insertGovtTrans=QueryManager.insertST3GovtTransaction() +
			// " values("+transId+","+amount+","+gameId+",'"+transactionType+"')
			// " ;
			// stmt9.executeUpdate(insertGovtTrans);

			PreparedStatement pstmt = con
					.prepareStatement("insert into st_lms_bo_govt_transaction (transaction_id, amount, game_id, transaction_type, start_date, end_date, service_code)  select ?, amount, game_id, transaction_type, start_date, end_date, service_code from  st_lms_bo_tasks where task_id = ?");
			pstmt.setLong(1, transId);
			pstmt.setInt(2, taskId);
			int updateRow = pstmt.executeUpdate();
			logger.debug("gov comm  insertion  query " + pstmt);
			logger.debug("okkkkkkkkk..." + updateRow);
			logger.debug("gameid is " + gameId);

			receiptId = insertRcptMapping(con, transId, id);

			con.commit();

			GraphReportHelper reportHelper = new GraphReportHelper();
			reportHelper.createTextReportBO(receiptId, userOrgName, userOrgId,
					rootPath);

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void submitVatAgtDG(Integer id, String transType, String Month,
			Double Amount, Integer agtId, Integer agtOrgId, String rootPath,
			String userOrgName) throws LMSException {
		logger.debug("submitVatAgtDG called");
		int taskId = id;
		String transactionType = transType;
		String monthTds = Month;
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		Date month = null;
		try {
			month = d.parse(monthTds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double amount = Amount;
		long  transId = 0;
		logger.debug("month is   " + month);
		 
		Connection con = null;
		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt4 = con.createStatement();
			Statement stmt11 = con.createStatement();

			logger.debug("task id is  " + taskId);
			String queryForTdstSubmit = QueryManager
					.submitST3UnclmPwtApprovedAgt()
					+ "where task_id='" + taskId + "' ";
			stmt4.executeUpdate(queryForTdstSubmit);

			// insert in LMS transaction master
			String insertLMSTransMas = QueryManager
					.insertInLMSTransactionMaster();
			PreparedStatement LMSMaspstmt = con
					.prepareStatement(insertLMSTransMas);
			LMSMaspstmt.setString(1, "AGENT");
			LMSMaspstmt.executeUpdate();
			ResultSet rs = LMSMaspstmt.getGeneratedKeys();
			while (rs.next()) {
				transId = rs.getLong(1);
				logger.debug("trans id is " + transId);
			}

			// insert into agent transaction master
			// String
			// insertTransMaster=QueryManager.insertST3TransactionMasterAgt() +
			// " values("+agtId+",'"+transactionType+"','"+new
			// java.sql.Timestamp( new Date().getTime())+"') " ;
			String insertTransMaster = QueryManager
					.insertInAgentTransactionMaster();
			PreparedStatement AgtTransMaspstmt = con
					.prepareStatement(insertTransMaster);

			AgtTransMaspstmt.setLong(1, transId);
			AgtTransMaspstmt.setInt(2, agtId);
			AgtTransMaspstmt.setInt(3, agtOrgId);
			AgtTransMaspstmt.setString(4, "GOVT");
			AgtTransMaspstmt.setObject(5, null);
			AgtTransMaspstmt.setString(6, transactionType);
			AgtTransMaspstmt.setTimestamp(7, new java.sql.Timestamp(new Date()
					.getTime()));

			AgtTransMaspstmt.executeUpdate();

			String insertGovtTrans = QueryManager
					.insertST3GovtTransactionForTDSAgt()
					+ " values("
					+ transId
					+ ","
					+ amount
					+ ",'"
					+ new java.sql.Date(month.getTime())
					+ "','"
					+ transactionType + "','DG') ";
			stmt11.executeUpdate(insertGovtTrans);
			receiptId = insertRcptMappingAgt(con, transId, id, agtOrgId);
			con.commit();

			GraphReportHelper reportHelper = new GraphReportHelper();
			reportHelper.createTextReportAgent(receiptId, rootPath, agtOrgId,
					userOrgName);

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);

			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException see) {

				see.printStackTrace();
				throw new LMSException("Error During Rollback", see);

			}
		}

	}

	public void submitVatAgtSE(Integer id, String transType, String Month,
			Double Amount, Integer agtId, Integer agtOrgId, String rootPath,
			String userOrgName) throws LMSException {
		logger.debug("submitVatAgtSE called");
		int taskId = id;
		String transactionType = transType;
		String monthTds = Month;
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		Date month = null;
		try {
			month = d.parse(monthTds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double amount = Amount;
		int transId = 0;
		logger.debug("month is   " + month);
		 
		Connection con = null;
		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt4 = con.createStatement();
			Statement stmt11 = con.createStatement();

			logger.debug("task id is  " + taskId);
			String queryForTdstSubmit = QueryManager
					.submitST3UnclmPwtApprovedAgt()
					+ "where task_id='" + taskId + "' ";
			stmt4.executeUpdate(queryForTdstSubmit);

			// insert in LMS transaction master
			String insertLMSTransMas = QueryManager
					.insertInLMSTransactionMaster();
			PreparedStatement LMSMaspstmt = con
					.prepareStatement(insertLMSTransMas);
			LMSMaspstmt.setString(1, "AGENT");
			LMSMaspstmt.executeUpdate();
			ResultSet rs = LMSMaspstmt.getGeneratedKeys();
			while (rs.next()) {
				transId = rs.getInt(1);
				logger.debug("trans id is " + transId);
			}

			// insert into agent transaction master
			// String
			// insertTransMaster=QueryManager.insertST3TransactionMasterAgt() +
			// " values("+agtId+",'"+transactionType+"','"+new
			// java.sql.Timestamp( new Date().getTime())+"') " ;
			String insertTransMaster = QueryManager
					.insertInAgentTransactionMaster();
			PreparedStatement AgtTransMaspstmt = con
					.prepareStatement(insertTransMaster);

			AgtTransMaspstmt.setInt(1, transId);
			AgtTransMaspstmt.setInt(2, agtId);
			AgtTransMaspstmt.setInt(3, agtOrgId);
			AgtTransMaspstmt.setString(4, "GOVT");
			AgtTransMaspstmt.setObject(5, null);
			AgtTransMaspstmt.setString(6, transactionType);
			AgtTransMaspstmt.setTimestamp(7, new java.sql.Timestamp(new Date()
					.getTime()));

			AgtTransMaspstmt.executeUpdate();

			String insertGovtTrans = QueryManager
					.insertST3GovtTransactionForTDSAgt()
					+ " values("
					+ transId
					+ ","
					+ amount
					+ ",'"
					+ new java.sql.Date(month.getTime())
					+ "','"
					+ transactionType + "','SE') ";
			stmt11.executeUpdate(insertGovtTrans);
			receiptId = insertRcptMappingAgt(con, transId, id, agtOrgId);
			con.commit();

			GraphReportHelper reportHelper = new GraphReportHelper();
			reportHelper.createTextReportAgent(receiptId, rootPath, agtOrgId,
					userOrgName);

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);

			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException see) {

				see.printStackTrace();
				throw new LMSException("Error During Rollback", see);

			}
		}

	}

	/**
	 * This method inside helper class is used to get the list of govt comm
	 * Approved for the games
	 * 
	 * @return List of govt comm
	 * @throws LMSException
	 */
	public List taskGovtCommSearch() throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		try {

			Statement stmt2 = con.createStatement();
			Statement stmt4 = con.createStatement();
			TaskBean taskBean = null;
			List<TaskBean> GovtCommSearchResults = new ArrayList<TaskBean>();
			ResultSet rs = null;

			String queryForGovtComm = QueryManager.getST3GovtCommApprovedSE();
			rs = stmt2.executeQuery(queryForGovtComm);
			logger.debug("query to search approved Gov Comm  == "
					+ queryForGovtComm);
			while (rs.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs.getDouble(TableConstants.TASK_AMOUNT));

				taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs.getInt(TableConstants.TASK_ID));
				taskBean.setGameId(rs.getInt(TableConstants.GAME_ID));
				// add game nbr into task bean here
				logger.debug("after optimizing queries");
				taskBean.setGameNbr(rs.getInt(TableConstants.GAME_NBR));

				// added by arun
				taskBean.setGameName(rs.getString("game_name"));
				taskBean.setStartDate(rs.getDate("start_date").toString());
				taskBean.setEndDate(rs.getDate("end_date").toString());
				// join the queries to select data from task table and from
				// st_se_game_master

				/*
				 * String gameDet=QueryManager.getST3GamesDetails() + " where
				 * game_id="+rs.getInt(TableConstants.GAME_ID)+" " ; ResultSet
				 * gameDetails=stmt4.executeQuery(gameDet);
				 * while(gameDetails.next()) {
				 * taskBean.setGameNbr(gameDetails.getInt(TableConstants.GAME_NBR)); }
				 */

				logger.debug("task id is   "
						+ rs.getInt(TableConstants.TASK_ID));

				GovtCommSearchResults.add(taskBean);
			}

			return GovtCommSearchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return null;

	}

	public List taskTDSSearchDG() throws LMSException {
		logger.debug(" taskTDSSearchDG() called");
		 
		Connection con = DBConnect.getConnection();
		logger
				.debug("1234567896543214569875463214785963.........................");
		String serviceName = "";
		try {

			Statement stmt1 = con.createStatement();
			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();

			ResultSet rs = null;
			rs = stmt1
					.executeQuery("select service_display_name from st_lms_service_master where service_code='DG' and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}
			logger.debug("hello      ");
			String queryForTDS = QueryManager.getST3TDSApprovedDG();
			rs = stmt1.executeQuery(queryForTDS);

			while (rs.next()) {

				taskBean = new TaskBean();
				taskBean.setAmount(rs.getDouble(TableConstants.TASK_AMOUNT));
				// SimpleDateFormat dateFormat=new
				// SimpleDateFormat("dd-MM-yyyy");
				// taskBean.setMonth(dateFormat.format(rs.getDate(TableConstants.MONTH_TASK)));
				logger.debug("haaaaaaaannnnnnnnnnnnnn "
						+ rs.getString(TableConstants.MONTH_TASK));
				taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs.getInt(TableConstants.TASK_ID));
				taskBean.setGameId(rs.getInt(TableConstants.GAME_ID));
				taskBean.setServiceCode("DG");
				taskBean.setServiceName(serviceName);
				logger.debug("task id id   "
						+ rs.getInt(TableConstants.TASK_ID));
				searchResults.add(taskBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return null;

	}

	public List taskTDSSearchSE() throws LMSException {
		logger.debug(" taskTDSSearchSE() called");
		 
		Connection con = DBConnect.getConnection();
		logger
				.debug("1234567896543214569875463214785963.........................");
		String serviceName = "";
		try {

			Statement stmt1 = con.createStatement();
			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();

			ResultSet rs = null;
			rs = stmt1
					.executeQuery("select service_display_name from st_lms_service_master where service_code='SE' and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}
			logger.debug("hello      ");
			String queryForTDS = QueryManager.getST3TDSApprovedSE();
			rs = stmt1.executeQuery(queryForTDS);

			while (rs.next()) {

				taskBean = new TaskBean();
				taskBean.setAmount(rs.getDouble(TableConstants.TASK_AMOUNT));
				// SimpleDateFormat dateFormat=new
				// SimpleDateFormat("dd-MM-yyyy");
				// taskBean.setMonth(dateFormat.format(rs.getDate(TableConstants.MONTH_TASK)));
				logger.debug("haaaaaaaannnnnnnnnnnnnn "
						+ rs.getString(TableConstants.MONTH_TASK));
				taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs.getInt(TableConstants.TASK_ID));
				taskBean.setGameId(rs.getInt(TableConstants.GAME_ID));
				taskBean.setServiceCode("SE");
				taskBean.setServiceName(serviceName);
				logger.debug("task id id   "
						+ rs.getInt(TableConstants.TASK_ID));
				searchResults.add(taskBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return null;

	}

	/**
	 * This method is used to get the list of unclaimed Pwt for the games
	 * 
	 * @return List of Unclaimed Pwt
	 * @throws LMSException
	 */
	public List taskUnclmPwtSearch() throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		try {

			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			TaskBean taskBean = null;
			List<TaskBean> UnclmPwtSearchResults = new ArrayList<TaskBean>();
			ResultSet rs = null;

			String queryForUnclmPwt = QueryManager.getST3UnclmPwtApproved();
			rs = stmt3.executeQuery(queryForUnclmPwt);

			while (rs.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs.getDouble(TableConstants.TASK_AMOUNT));

				taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs.getInt(TableConstants.TASK_ID));
				taskBean.setApprovedate(rs
						.getString(TableConstants.TASK_APPROVE_DATE));
				taskBean.setGameId(rs.getInt(TableConstants.GAME_ID));
				taskBean.setGameNbr(rs.getInt(TableConstants.GAME_NBR));
				// it is commented to optimize the queries
				/*
				 * String gameDet=QueryManager.getST3GamesDetails() + " where
				 * game_id="+rs.getInt(TableConstants.GAME_ID)+" " ; ResultSet
				 * gameDetails=stmt4.executeQuery(gameDet);
				 * while(gameDetails.next()) {
				 * taskBean.setGameNbr(gameDetails.getInt(TableConstants.GAME_NBR)); }
				 */
				logger.debug("task id is   "
						+ rs.getInt(TableConstants.TASK_ID));

				UnclmPwtSearchResults.add(taskBean);
			}

			return UnclmPwtSearchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		}
		// return null;

	}

	public List taskVATSearchDG(String userType, Integer userOrgid)
			throws LMSException {
		logger.debug("taskVATSearchDG called");
		 
		Connection con = DBConnect.getConnection();
		logger
				.debug("1234567896543214569875463214785963.........................");
		String serviceName = "";
		try {

			Statement stmt1 = con.createStatement();
			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();

			ResultSet rs = null;
			rs = stmt1
					.executeQuery("select service_display_name from st_lms_service_master where service_code='DG' and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}

			logger.debug("hello      ");
			String queryForVAT = null;
			if (userType.equals("BO")) {
				queryForVAT = QueryManager.getST3VATApprovedDG();
			} else if (userType.equals("AGENT")) {
				queryForVAT = QueryManager.getST3VATApprovedAgtDG() + userOrgid;
			}

			logger.debug("aaaa " + queryForVAT);
			rs = stmt1.executeQuery(queryForVAT);

			while (rs.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs.getDouble(TableConstants.TASK_AMOUNT));
				// SimpleDateFormat dateFormat=new
				// SimpleDateFormat("dd-MM-yyyy");
				// taskBean.setMonth(dateFormat.format(rs.getDate(TableConstants.MONTH_TASK)));
				logger.debug("haaaaaaaannnnnnnnnnnnnn "
						+ rs.getString(TableConstants.MONTH_TASK));
				taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs.getInt(TableConstants.TASK_ID));
				taskBean.setServiceCode("DG");
				taskBean.setServiceName(serviceName);
				// taskBean.setGameId(rs.getInt(TableConstants.GAME_ID));
				logger.debug("task id id   "
						+ rs.getInt(TableConstants.TASK_ID));
				searchResults.add(taskBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return null;

	}

	public List taskVATSearchSE(String userType, Integer userOrgid)
			throws LMSException {
		logger.debug("taskVATSearchSE called");
		 
		Connection con = DBConnect.getConnection();
		logger
				.debug("1234567896543214569875463214785963.........................");
		String serviceName = "";
		try {

			Statement stmt1 = con.createStatement();
			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();

			ResultSet rs = null;
			rs = stmt1
					.executeQuery("select service_display_name from st_lms_service_master where service_code='SE' and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}

			logger.debug("hello      ");
			String queryForVAT = null;
			if (userType.equals("BO")) {
				queryForVAT = QueryManager.getST3VATApprovedSE();
			} else if (userType.equals("AGENT")) {
				queryForVAT = QueryManager.getST3VATApprovedAgtSE() + userOrgid;
			}

			logger.debug("aaaa " + queryForVAT);
			rs = stmt1.executeQuery(queryForVAT);

			while (rs.next()) {

				taskBean = new TaskBean();
				taskBean.setAmount(rs.getDouble(TableConstants.TASK_AMOUNT));
				// SimpleDateFormat dateFormat=new
				// SimpleDateFormat("dd-MM-yyyy");
				// taskBean.setMonth(dateFormat.format(rs.getDate(TableConstants.MONTH_TASK)));
				logger.debug("haaaaaaaannnnnnnnnnnnnn "
						+ rs.getString(TableConstants.MONTH_TASK));
				taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs.getInt(TableConstants.TASK_ID));
				taskBean.setServiceCode("SE");
				taskBean.setServiceName(serviceName);
				// taskBean.setGameId(rs.getInt(TableConstants.GAME_ID));
				logger.debug("task id id   "
						+ rs.getInt(TableConstants.TASK_ID));
				searchResults.add(taskBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return null;

	}
	
	public List taskVATSearchIW(String userType, Integer userOrgid)
	throws LMSException {
logger.debug("taskVATSearchIW called");
 
Connection con = DBConnect.getConnection();
logger
		.debug("1234567896543214569875463214785963.........................");
String serviceName = "";
try {

	Statement stmt1 = con.createStatement();
	TaskBean taskBean = null;

	List<TaskBean> searchResults = new ArrayList<TaskBean>();

	ResultSet rs = null;
	rs = stmt1
			.executeQuery("select service_display_name from st_lms_service_master where service_code='IW' and status='ACTIVE'");
	if (rs.next()) {
		serviceName = rs.getString("service_display_name");
	}

	logger.debug("hello      ");
	String queryForVAT = null;
	if (userType.equals("BO")) {
		queryForVAT = QueryManager.getST3VATApprovedIW();
	} else if (userType.equals("AGENT")) {
		queryForVAT = QueryManager.getST3VATApprovedAgtIW() + userOrgid;
	}

	logger.debug("aaaa " + queryForVAT);
	rs = stmt1.executeQuery(queryForVAT);

	while (rs.next()) {

		taskBean = new TaskBean();
		taskBean.setAmount(rs.getDouble(TableConstants.TASK_AMOUNT));
		// SimpleDateFormat dateFormat=new
		// SimpleDateFormat("dd-MM-yyyy");
		// taskBean.setMonth(dateFormat.format(rs.getDate(TableConstants.MONTH_TASK)));
		logger.debug("haaaaaaaannnnnnnnnnnnnn "
				+ rs.getString(TableConstants.MONTH_TASK));
		taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
		taskBean.setTransactionType(rs
				.getString(TableConstants.TASK_TYPE));
		taskBean.setStatus(rs.getString(TableConstants.TASK_STATUS));
		taskBean.setTaskId(rs.getInt(TableConstants.TASK_ID));
		taskBean.setServiceCode("IW");
		taskBean.setServiceName(serviceName);
		// taskBean.setGameId(rs.getInt(TableConstants.GAME_ID));
		logger.debug("task id id   "
				+ rs.getInt(TableConstants.TASK_ID));
		searchResults.add(taskBean);

	}

	return searchResults;

} catch (SQLException e) {

	e.printStackTrace();
	throw new LMSException(e);
} finally {
	try {
		if (con != null) {
			con.close();
		}
	} catch (SQLException se) {
		se.printStackTrace();
	}
}
// return null;

}

}
