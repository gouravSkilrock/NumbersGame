package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This is a helper class for Player Verification.
 * 
 * @author Skilrock Technologies
 * 
 */
public class PlayerPWTVerifyHelper {
	/**
	 * Search Player
	 * 
	 * @param searchMap
	 * @return List
	 * @throws LMSException
	 */
	static Log logger = LogFactory.getLog(PlayerPWTVerifyHelper.class);

	// this methos is commented because it is not refrenced any where in LMS
	/*
	 * public int donePWTProcess(int gameId, int playerId, List pwtList,
	 * Connection connection) throws LMSException{ List<PwtBean> pwt = pwtList;
	 * 
	 * //Connection connection = null; Statement statement = null; int
	 * transaction_id = -1; double net_amt = 0; double pwtAmt = 0; boolean flag =
	 * false; int id=-1; ResultSet resultSet = null;
	 * 
	 * Statement st = null; PreparedStatement prepare1 = null; PreparedStatement
	 * prepare2 = null; PreparedStatement prepare3 = null; PreparedStatement
	 * prepare4 = null; PreparedStatement prepare5 = null; PreparedStatement
	 * prepare6 = null;
	 * 
	 * String query1 = QueryManager.getST5CashTransactionQuery(); String query2 =
	 * QueryManager.getST5PlrPWTCommRateQuery() + gameId; String query3 =
	 * QueryManager.getST5DirectPlayerTransactionQuery(); // update pwt status
	 * String query4 = QueryManager.getST5PwtBODetailQuery();
	 * 
	 * 
	 * String query5 = QueryManager.getST4InsertBoReceipts(); String query6 =
	 * QueryManager.getST4InsertBoReceiptsTrnMapping();
	 * 
	 * try {
	 * 
	 * if (connection == null) { System.out .println("Connection in the
	 * donePWTProcess method without session " + connection);
	 * 
	 *   connection =
	 * dbConnect.getConnection(); connection.setAutoCommit(false); } System.out
	 * .println("Connection in the donePWTProcess method by session " +
	 * connection); //java.util.Date current_date = new java.util.Date();
	 * //java.sql.Date CURRENT_DATE = new java.sql.Date(current_date //
	 * .getTime());
	 * 
	 * prepare1 = connection.prepareStatement(query1);
	 * 
	 * prepare1.setString(1, "PLAYER"); prepare1.setInt(2, playerId);
	 * prepare1.setTimestamp(3, new java.sql.Timestamp(new java.util.Date()
	 * .getTime())); prepare1.setString(4, "PWT_PLR"); prepare1.executeUpdate();
	 * resultSet = prepare1.getGeneratedKeys(); resultSet.next(); transaction_id =
	 * resultSet.getInt(1); if(!(transaction_id>0)){
	 * 
	 * return -1; } ServletContext sc=ServletActionContext.getServletContext();
	 * String govt_pwt_comm_rate1=(String)sc.getAttribute("GOVT_COMM_RATE");
	 * //String govt_pwt_comm_rate1 =
	 * properties.getProperty("govt_pwt_comm_rate");
	 * 
	 * int govt_pwt_comm_rate2 = Integer.parseInt(govt_pwt_comm_rate1);
	 * 
	 * String virnCode = pwt.get(0).getVirnCode(); int virnCode1 =
	 * Integer.parseInt(virnCode); String pwtamt = pwt.get(0).getPwtAmount();
	 * pwtAmt = Double.parseDouble(pwtamt); TaxCalculation tx=new
	 * TaxCalculation(); double income_tax=0.0; double calculatedincomeTax=0.0;
	 * income_tax = tx.returnTaxAmount(pwtAmt);
	 * calculatedincomeTax=(double)(pwtAmt * income_tax / 100); net_amt =
	 * (double) (pwtAmt - calculatedincomeTax); prepare2 =
	 * connection.prepareStatement(query3); prepare2.setString(1,
	 * getEncodedVirnCode(virnCode)); prepare2.setInt(2, transaction_id);
	 * prepare2.setInt(3, gameId); prepare2.setInt(4, playerId);
	 * prepare2.setDouble(5, pwtAmt); prepare2.setDouble(6,
	 * calculatedincomeTax); prepare2.setDouble(7, net_amt);
	 * prepare2.setTimestamp(8, new java.sql.Timestamp(new java.util.Date()
	 * .getTime())); //prepare2.setDate(8, CURRENT_DATE);
	 * prepare2.executeUpdate(); ////
	 * 
	 * 
	 * //update total prize remaining of that game
	 * GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt, "CLAIM_PLR",
	 * getEncodedVirnCode(virnCode), connection );
	 * 
	 * //update pwt status = 'CLAIM_PLR' in st_pwt_inv table prepare3 =
	 * connection.prepareStatement(query4); prepare3.setInt(1, gameId);
	 * prepare3.setString(2, getEncodedVirnCode(virnCode));
	 * prepare3.executeUpdate();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * prepare4 = connection.prepareStatement(query5); prepare4.setString(1,
	 * "RECEIPT"); prepare4.setString(2, null); prepare4.executeUpdate();
	 * ResultSet rss = prepare4.getGeneratedKeys(); rss.next(); id =
	 * rss.getInt(1);
	 * 
	 * prepare5 = connection.prepareStatement(query6); prepare5.setInt(1, id);
	 * prepare5.setInt(2, transaction_id); prepare5.executeUpdate();
	 * 
	 * connection.commit(); flag = true; }
	 * 
	 * catch (SQLException e) { logger.error("Exception: "+e);
	 * e.printStackTrace(); throw new LMSException(e); } catch (IOException ee) {
	 * 
	 * ee.printStackTrace(); throw new LMSException(ee); }
	 * 
	 * 
	 * finally {
	 * 
	 * try {
	 * 
	 * if (statement != null) { statement.close(); } if (connection != null) {
	 * connection.close(); } } catch (SQLException se) { se.printStackTrace();
	 * throw new LMSException(se); } }
	 * 
	 * return id; }
	 */
	/**
	 * In this method the net payed PWT will be committed.
	 * 
	 * @param gameId
	 * @param playerId
	 * @param pwtAmt
	 * @param tax
	 * @param virnCode
	 * @param tempTransactionId
	 * @return int
	 * @throws LMSException
	 */
	public int CommitPWTProcess(int gameId, int playerId, double pwtAmt,
			double tax, String virnCode, int tempTransactionId,
			String chequeNbr, String draweeBank, String issuingParty,
			java.sql.Date chqDate, int userOrgId, int userId, int gameNbr)
			throws LMSException {

		// Connection connection = null;
		Statement statement = null;
		int transaction_id = -1;
		double net_amt = 0.0;

		int id = -1;
		String autoGeneRecieptNo = null;
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement prepare1 = null;
		PreparedStatement prepare2 = null;
		PreparedStatement prepare3 = null;
		PreparedStatement prepare4 = null;
		PreparedStatement prepare5 = null;
		PreparedStatement prepare6 = null;
		String query1 = QueryManager.insertInBOTransactionMaster();
		String LMSTransQuery = QueryManager.insertInLMSTransactionMaster();

		String query2 = QueryManager.getST5PlrPWTCommRateQuery() + gameId;

		String query3 = QueryManager.getST5DirectPlayerTransactionQuery();

		String query4 = QueryManager.getST5PwtBODetailQuery();
		// String query5 = QueryManager.getST4InsertBoReceipts();
		// String query6 = QueryManager.getST4InsertBoReceiptsTrnMapping();

		String query7 = QueryManager.getST5UpdateSTBOtempTranction();

		try {

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			// insert into LMS transaction master

			prepare1 = connection.prepareStatement(LMSTransQuery);
			prepare1.setString(1, "BO");
			prepare1.executeUpdate();

			resultSet = prepare1.getGeneratedKeys();
			resultSet.next();
			transaction_id = resultSet.getInt(1);
			logger.debug("Generated New Transaction Id " + transaction_id);

			// //PWT Entry against the player into bo transaction master

			prepare1 = connection.prepareStatement(query1);

			prepare1.setInt(1, transaction_id);
			prepare1.setInt(2, userId);
			prepare1.setInt(3, userOrgId);
			prepare1.setString(4, "PLAYER");
			prepare1.setInt(5, playerId);
			prepare1.setTimestamp(6, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			prepare1.setString(7, "PWT_PLR");

			/*
			 * prepare1.setString(1, "PLAYER"); prepare1.setInt(2, playerId);
			 * prepare1.setTimestamp(3, new java.sql.Timestamp(new
			 * java.util.Date().getTime())); prepare1.setString(4, "PWT_PLR");
			 */

			prepare1.executeUpdate();

			net_amt = pwtAmt - tax;

			prepare2 = connection.prepareStatement(query3);

			prepare2.setString(1, virnCode);
			prepare2.setInt(2, transaction_id);
			prepare2.setInt(3, gameId);
			prepare2.setInt(4, playerId);
			prepare2.setDouble(5, pwtAmt);
			prepare2.setDouble(6, tax);
			prepare2.setDouble(7, net_amt);
			prepare2.setTimestamp(8, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			prepare2.setString(9, chequeNbr);
			prepare2.setDate(10, chqDate);
			prepare2.setString(11, draweeBank);
			prepare2.setString(12, issuingParty);
			// prepare2.setDate(8, CURRENT_DATE);
			prepare2.executeUpdate();
			// //

			// update total prize remaining of that game
			GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt, "CLAIM_PLR",
					virnCode, connection, gameNbr);

			// update the status = 'CLAIM_PLR' of virn code in st_pwt_inv table
			prepare3 = connection.prepareStatement(query4);
			prepare3.setInt(1, gameNbr);
			prepare3.setInt(2, gameId);
			prepare3.setString(3, virnCode);
			logger.debug("query  for update pwt table is " + prepare3);
			prepare3.executeUpdate();

			logger.debug("--------" + query7 + "--------------tax " + tax
					+ " net_amt " + net_amt);
			prepare6 = connection.prepareStatement(query7);
			prepare6.setString(1, "CLAIM_PLR");
			prepare6.setDouble(2, tax);
			prepare6.setDouble(3, net_amt);
			prepare6.setInt(4, tempTransactionId);
			prepare6.executeUpdate();

			// get auto generated treciept number
			PreparedStatement autoGenPstmt = null;
			// String getLatestRecieptNumber="SELECT * from
			// st_bo_receipt_gen_mapping where receipt_type=? ORDER BY
			// generated_id DESC LIMIT 1 ";
			autoGenPstmt = connection.prepareStatement(QueryManager
					.getBOLatestReceiptNb());
			autoGenPstmt.setString(1, "RECEIPT");
			ResultSet recieptRs = autoGenPstmt.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo("RECEIPT",
					lastRecieptNoGenerated, "BO");

			// insert in receipt master table
			prepare4 = connection.prepareStatement(QueryManager
					.insertInReceiptMaster());
			prepare4.setString(1, "BO");
			prepare4.executeUpdate();

			ResultSet rss = prepare4.getGeneratedKeys();
			rss.next();
			id = rss.getInt(1);

			prepare4 = connection.prepareStatement(QueryManager
					.insertInBOReceipts());

			prepare4.setInt(1, id);
			prepare4.setString(2, "RECEIPT");
			prepare4.setInt(3, playerId);
			prepare4.setString(4, "PLAYER");
			prepare4.setString(5, autoGeneRecieptNo);
			prepare4.setTimestamp(6, Util.getCurrentTimeStamp());

			/*
			 * //prepare4.setString(1, autoGeneRecieptNo); prepare4.setString(1,
			 * "RECEIPT"); prepare4.setString(2, null);
			 */
			prepare4.executeUpdate();

			prepare5 = connection.prepareStatement(QueryManager
					.insertBOReceiptTrnMapping());
			prepare5.setInt(1, id);
			prepare5.setInt(2, transaction_id);
			prepare5.executeUpdate();

			/*
			 * //insert into recipt gen reciept mapping String
			 * updateBoRecieptGenMapping=QueryManager.updateST5BOReceiptGenMapping();
			 * prepare7=connection.prepareStatement(updateBoRecieptGenMapping);
			 * prepare7.setInt(1,id); prepare7.setString(2,autoGeneRecieptNo);
			 * prepare7.setString(3,"RECEIPT"); prepare7.executeUpdate();
			 */

			/*
			 * logger.debug("--------"+query7 +"--------------tax "+tax+"
			 * net_amt "+net_amt); prepare6 =
			 * connection.prepareStatement(query7); prepare6.setString(1,
			 * "CLAIM_PLR"); prepare6.setDouble(2, tax); prepare6.setDouble(3,
			 * net_amt); prepare6.setInt(4, tempTransactionId);
			 * prepare6.executeUpdate();
			 */

			connection.commit();

		}

		catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

		finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
				throw new LMSException(se);
			}

		}

		return id;
	}

	public boolean denyPWTProcess(int transactionId, String virnCode,
			int gameId, String ticketNbr, String denyPwtStatus, int gameNbr)
			throws LMSException {

		boolean statusChange = false;
		Connection connection = null;
		 
		connection = DBConnect.getConnection();
		try {
			connection.setAutoCommit(false);

			// update direct playe temp table
			// 'CANCELLED_PERMANENT':'Permanent Cancellation',
			// 'UNCLM_CANCELLED':'Temporary Cancellation'
			String tempPwtStatus = "";
			String pwtStatus = "";
			int isPwtTicketDelete = 0;

			if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus.trim())) {
				tempPwtStatus = "CANCEL";
				pwtStatus = "CANCELLED_PERMANENT";
			} else if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())) {
				tempPwtStatus = "CANCEL";
				pwtStatus = "UNCLM_CANCELLED";
				// deleted entry in case of Temporary Cancellation
				PreparedStatement pstmtPwtTicketUpdate = connection
						.prepareStatement("delete from st_se_pwt_tickets_inv_? where game_id=? and ticket_nbr=?");
				pstmtPwtTicketUpdate.setInt(1, gameNbr);
				pstmtPwtTicketUpdate.setInt(2, gameId);
				pstmtPwtTicketUpdate.setString(3, ticketNbr);
				logger.debug("update ticket  table:: " + pstmtPwtTicketUpdate);
				isPwtTicketDelete = pstmtPwtTicketUpdate.executeUpdate();
				logger.debug("query is  " + pstmtPwtTicketUpdate);
			}

			PreparedStatement pstmt = connection
					.prepareStatement("update st_se_direct_player_pwt_temp_receipt set status=? where pwt_receipt_id=?");
			pstmt.setString(1, tempPwtStatus);
			pstmt.setInt(2, transactionId);
			int isUpdate = pstmt.executeUpdate();
			logger.debug("update st_se_direct_player_pwt_temp_receipt =="
					+ pstmt);

			// update pwt inv table status
			PreparedStatement pstmtPwtInvUpdate = connection
					.prepareStatement("update st_se_pwt_inv_? set status=? where virn_code=? and game_id=?");
			pstmtPwtInvUpdate.setInt(1, gameNbr);
			pstmtPwtInvUpdate.setString(2, pwtStatus);
			pstmtPwtInvUpdate.setString(3, virnCode);
			pstmtPwtInvUpdate.setInt(4, gameId);
			int isPwtupdate = pstmtPwtInvUpdate.executeUpdate();
			logger.debug("update st_pwt_inv ==" + pstmtPwtInvUpdate);

			if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus.trim())
					&& isUpdate == 1 && isPwtupdate == 1
					&& isPwtTicketDelete == 1) {
				statusChange = true;
				connection.commit();
			} else if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())
					&& isUpdate == 1 && isPwtupdate == 1) {
				statusChange = true;
				connection.commit();
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
				throw new LMSException(se);
			}

		}

		return statusChange;

	}

	/**
	 * To get country code for the country.
	 * 
	 * @param key
	 * @param connection
	 * @return String
	 * @throws SQLException
	 */
	public String getCountryCode(String key, Connection connection)
			throws SQLException {
		String Country_code = null;
		Statement statement = null;
		ResultSet rs = null;

		statement = connection.createStatement();
		// String query1="select country_code from st_lms_country_master where
		// country_name='"+key+"'";
		String query1 = QueryManager.getST5CountryCodeQuery() + " where name='"
				+ key + "'";

		rs = statement.executeQuery(query1);
		rs.next();
		Country_code = rs.getString("country_code");

		return Country_code;
	}

	private String getEncodedVirnCode(String virnCode) {

		StringBuffer encodedVirnCode = new StringBuffer("");

		encodedVirnCode.append(MD5Encoder.encode(virnCode));

		return encodedVirnCode.toString();
	}

	/**
	 * This method is used to commit the data for Direct Player Pwt Receive but
	 * at this time user player will not get the PWT amount.
	 * 
	 * @param gameId
	 * @param playerId
	 * @param pwtList
	 * @param connection
	 * @return
	 * @throws LMSException
	 */

	/**
	 * To Get State Code
	 * 
	 * @param key
	 * @param countryCode
	 * @param connection
	 * @return String
	 * @throws SQLException
	 */
	public String getStateCode(String key, String countryCode,
			Connection connection) throws SQLException {
		String state_code = null;
		Statement statement = null;
		ResultSet rs = null;

		statement = connection.createStatement();
		// String query1="select state_code from st_lms_state_master WHERE
		// name='DELHI' and country_code='3'";
		String query1 = QueryManager.getST5StateCodeQuery() + " where name='"
				+ key + "' and country_code='" + countryCode + "'";

		rs = statement.executeQuery(query1);
		rs.next();
		state_code = rs.getString("state_code");

		return state_code;
	}

	/**
	 * This method is used to make the entry in the
	 * "st_se_direct_player_pwt_temp_receipt". This table has the Direct Player
	 * Entry against the player for the Virn Code.At this this is Pending PWT.
	 * Later on at the time of net pay this table would be used to display
	 * pending PWTs which have been processed but not yet payyed.
	 * 
	 * @param gameId
	 * @param playerId
	 * @param pwtList
	 * @param connection
	 * @return
	 * @throws LMSException
	 */

	public int partiallyPWTProcess(int gameId, int playerId, List pwtList,
			Connection connection, String ticket_nbr, double pwtApprovalLimit,
			int gameNbr) throws LMSException {
		List<PwtBean> pwt = pwtList;

		// Connection connection = null;
		// Statement statement = null;
		int transaction_id = 0;

		double pwtAmt = 0;
		String pwtStatus = "PND_PWT";
		// ResultSet resultSet = null;
		PreparedStatement prepare1 = null;
		PreparedStatement pstmtPwt = null;

		String query1 = QueryManager.getST5DirectPlrTempTransactionQuery();
		String updatePwtTableQuery = "update st_se_pwt_inv_? set status=? where virn_code=? and game_id=? ";

		try {

			if (connection == null) {
				System.out
						.println("Connection in the donePWTProcess method without session  "
								+ connection);

				 
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);
			}
			System.out
					.println("Connection in the donePWTProcess method by session "
							+ connection);
			String virnCode = pwt.get(0).getVirnCode();

			String pwtamt = pwt.get(0).getPwtAmount();
			pwtAmt = Double.parseDouble(pwtamt);
			if (pwtAmt >= pwtApprovalLimit) {
				pwtStatus = "PND_ADM";
			}

			prepare1 = connection.prepareStatement(query1);
			prepare1.setInt(1, playerId);
			prepare1.setTimestamp(2, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			prepare1.setString(3, getEncodedVirnCode(virnCode));
			prepare1.setInt(4, gameId);
			prepare1.setDouble(5, pwtAmt);
			prepare1.setString(6, pwtStatus);
			prepare1.setString(7, ticket_nbr);
			prepare1.executeUpdate();

			ResultSet resultSet = prepare1.getGeneratedKeys();
			resultSet.next();
			transaction_id = resultSet.getInt(1);

			pstmtPwt = connection.prepareStatement(updatePwtTableQuery);
			pstmtPwt.setInt(1, gameNbr);
			pstmtPwt.setString(2, "CLAIM_PLR_TEMP");
			pstmtPwt.setString(3, getEncodedVirnCode(virnCode));
			pstmtPwt.setInt(4, gameId);
			pstmtPwt.executeUpdate();

			connection.commit();

		}

		catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

		finally {

			try {
				/*
				 * if (statement != null) { statement.close(); }
				 */
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
				throw new LMSException(se);
			}

		}

		return transaction_id;
	}

	/**
	 * Register Player
	 * 
	 * @param searchMap
	 * @param connection
	 * @return
	 * @throws LMSException
	 */
	public int registerPlayer(Map searchMap, Connection connection)
			throws LMSException {
		// This connection object is same for whole transaction of the Direct
		// Player.

		if (connection == null) {
			return -1;
		}

		// Connection connection = null;
		PreparedStatement prepareState = null;
		ResultSet resultSet = null;
		String countryCode = null;
		String stateCode = null;
		boolean insetInto = false;
		int playerId = 0;
		try {

			List<PlayerBean> searchResults = new ArrayList<PlayerBean>();

			//  
			// connection = DBConnect.getConnection();
			logger.debug("Player Contry"
					+ (String) searchMap.get(TableConstants.PLAYER_COUNTRY));
			logger.debug("Player State"
					+ (String) searchMap.get(TableConstants.PLAYER_STATE));
			countryCode = getCountryCode((String) searchMap
					.get(TableConstants.PLAYER_COUNTRY), connection);
			stateCode = getStateCode((String) searchMap
					.get(TableConstants.PLAYER_STATE), countryCode, connection);
			logger.debug("Country Code..." + countryCode + " and  StateCode"
					+ stateCode);

			String query = QueryManager.getST5PlayerEntryQuery();

			logger.debug("-----Query----::" + query);

			prepareState = connection.prepareStatement(query);

			// select
			// a.first_name,a.last_name,a.email_id,a.phone_nbr,a.addr_line1,a.city,d.name
			// 'country',e.name 'state',a.pin_code from st_lms_player_master
			// a,st_lms_country_master d, st_lms_state_master e where
			// a.country_code=d.country_code and a.state_code=e.state_code and
			// a.first_name=? and a.last_name=? and a.photo_id_type=? and
			// a.photo_id_nbr=?
			// first_name,last_name,email_id,phone_nbr,addr_line1,addr_line2,city,state_code,country_code,pin_code,photo_id_type,photo_id_nbr)

			prepareState.setString(1, (String) searchMap
					.get(TableConstants.PLAYER_FIRSTNAME));
			prepareState.setString(2, (String) searchMap
					.get(TableConstants.PLAYER_LASTNAME));
			prepareState.setString(3, (String) searchMap
					.get(TableConstants.PLAYER_EMAIL));
			prepareState.setString(4, (String) searchMap
					.get(TableConstants.PLAYER_PHONE));
			prepareState.setString(5, (String) searchMap
					.get(TableConstants.PLAYER_ADDR1));
			prepareState.setString(6, (String) searchMap
					.get(TableConstants.PLAYER_ADDR2));
			prepareState.setString(7, (String) searchMap
					.get(TableConstants.PLAYER_CITY));
			prepareState.setString(8, stateCode);
			prepareState.setString(9, countryCode);
			prepareState.setLong(10, Long.parseLong((String) searchMap
					.get(TableConstants.PLAYER_PIN)));
			prepareState.setString(11, (String) searchMap
					.get(TableConstants.PLAYER_IDTYPE));
			prepareState.setString(12, (String) searchMap
					.get(TableConstants.PLAYER_IDNUMBER));

			insetInto = prepareState.execute();

			ResultSet rs = prepareState.getGeneratedKeys();
			rs.next();
			playerId = rs.getInt(1);

			System.out
					.println("SucessFully Inserted into st_lms_player_master Table");

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (prepareState != null) {
					prepareState.close();
				}

			} catch (SQLException se) {
				logger.error("Exception: " + se);
				throw new LMSException(se);
			}
		}
		return playerId;
	}

	public List searchPlayer(Map searchMap) throws LMSException {
		// / searchMap parameter is having all the search criteria parameters
		// coming from the user end.
		Connection connection = null;
		PreparedStatement prepareState = null;
		ResultSet resultSet = null;

		try {

			PlayerBean plrBean = null;
			List<PlayerBean> searchResults = new ArrayList<PlayerBean>();

			 
			connection = DBConnect.getConnection();

			String query = QueryManager.getST5PlayerDetailQuery();

			logger.debug("-----Query----::" + query);

			prepareState = connection.prepareStatement(query);

			// select
			// a.first_name,a.last_name,a.email_id,a.phone_nbr,a.addr_line1,a.city,d.name
			// 'country',e.name 'state',a.pin_code from st_lms_player_master
			// a,st_lms_country_master d, st_lms_state_master e where
			// a.country_code=d.country_code and a.state_code=e.state_code and
			// a.first_name=? and a.last_name=? and a.photo_id_type=? and
			// a.photo_id_nbr=?

			prepareState.setString(1, (String) searchMap
					.get(TableConstants.PLAYER_FIRSTNAME));
			prepareState.setString(2, (String) searchMap
					.get(TableConstants.PLAYER_LASTNAME));
			prepareState.setString(3, (String) searchMap
					.get(TableConstants.PLAYER_IDTYPE));
			prepareState.setString(4, (String) searchMap
					.get(TableConstants.PLAYER_IDNUMBER));

			resultSet = prepareState.executeQuery();

			logger.debug("Result Set forPlayer Search" + resultSet);
			while (resultSet.next()) {
				logger.debug("State   " + resultSet.getString("state"));
				plrBean = new PlayerBean();
				// userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				plrBean.setPlrId(resultSet.getInt("player_id"));
				plrBean.setFirstName((String) searchMap
						.get(TableConstants.PLAYER_FIRSTNAME));
				plrBean.setLastName((String) searchMap
						.get(TableConstants.PLAYER_LASTNAME));
				plrBean.setIdType((String) searchMap
						.get(TableConstants.PLAYER_IDTYPE));
				plrBean.setIdNumber((String) searchMap
						.get(TableConstants.PLAYER_IDNUMBER));
				plrBean.setEmailId(resultSet.getString("email_id"));
				plrBean.setPhone(resultSet.getString("phone_nbr"));
				plrBean.setPlrAddr1(resultSet.getString("addr_line1"));
				plrBean.setPlrAddr2(resultSet.getString("addr_line2"));
				plrBean.setPlrCity(resultSet.getString("city"));
				plrBean.setPlrState(resultSet.getString("state"));
				plrBean.setPlrCountry(resultSet.getString("country"));
				plrBean.setPlrPin(resultSet.getLong("pin_code"));

				searchResults.add(plrBean);
			}

			return searchResults;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (prepareState != null) {
					prepareState.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				throw new LMSException(se);
			}
		}

	}

}
