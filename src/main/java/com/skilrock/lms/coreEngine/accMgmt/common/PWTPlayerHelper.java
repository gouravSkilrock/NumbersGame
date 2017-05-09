package com.skilrock.lms.coreEngine.accMgmt.common;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;

/**
 * This is helper class to process ticket validation for direct player PWT
 * receive.
 * 
 * @author Skilrock Technologies
 * 
 */
public class PWTPlayerHelper {
	static Log logger = LogFactory.getLog(PWTPlayerHelper.class);
	private List<PwtBean> pwtList = null;

	/**
	 * This method is used to get active games for which Direct player PWT can
	 * be received.
	 * 
	 * @return List of active games
	 * @throws LMSException
	 */
	public List getActiveGames() throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {

			ActiveGameBean gameBean = null;
			List<ActiveGameBean> searchResults = new ArrayList<ActiveGameBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST1ActiveGamesQuery();
			resultSet = statement.executeQuery(query);
			int gameNbr;
			String gameName;

			while (resultSet.next()) {

				gameBean = new ActiveGameBean();
				gameBean
						.setGameId(resultSet.getInt(TableConstants.SGM_GAME_ID));
				gameBean.setPlayerPwtCommRate(resultSet
						.getDouble(TableConstants.SGM_AGT_PWT_RATE));
				gameNbr = resultSet.getInt(TableConstants.SGM_GAME_NBR);
				gameName = resultSet.getString(TableConstants.SGM_GAME_NAME);
				gameBean.setGameNbr_Name(gameNbr + delimiter + gameName);

				searchResults.add(gameBean);

			}

			return searchResults;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
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
				logger.error("Exception: " + se);
				se.printStackTrace();
				throw new LMSException(se);
			}
		}

	}

	private String getEncodedVirnCode(String virnCode) {

		StringBuffer encodedVirnCode = new StringBuffer("");

		encodedVirnCode.append(MD5Encoder.encode(virnCode));

		return encodedVirnCode.toString();
	}

	public List getPwtList() {
		return pwtList;

	}

	/**
	 * This method is used to get verified Tickets for the VIRN code.
	 * 
	 * @param virnCode
	 * @param gameId
	 * @return boolean
	 * @throws LMSException
	 */

	public boolean getverifyPwtTickets(String virnCode, int gameId, int gameNbr)
			throws LMSException {

		List<PwtBean> pwtList = new ArrayList();

		String encodedVirnCode = getEncodedVirnCode(virnCode);
		logger.debug("---((((((::" + encodedVirnCode);

		Connection connection = null;
		// Statement statement = null;
		PreparedStatement statement = null;
		Statement statement1 = null;
		// Statement statement2 = null;
		// Statement statement3 = null;

		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		// ResultSet resultSet2 = null;
		// ResultSet resultSet3 = null;

		boolean isVerified = false;
		PwtBean pwtBean = new PwtBean();
		// StringBuffer query = new StringBuffer();

		try {
			 
			connection = DBConnect.getConnection();

			/*
			 * //to check virn for temporary table
			 * 
			 * String queryTOChqPwtTemptable="select * from st_se_tmp_pwt_inv
			 * where virn_code='"+encodedVirnCode+"' and game_id="+gameId;
			 * logger.debug("Query for pwt temporary table::
			 * "+queryTOChqPwtTemptable);
			 * statement2=connection.createStatement();
			 * resultSet2=statement2.executeQuery(queryTOChqPwtTemptable);
			 * 
			 * if(!resultSet2.next()){
			 */

			/*
			 * statement = connection.createStatement();
			 * query.append(QueryManager.getST1PWTCheckQuery()); query.append("
			 * game_id = "); query.append("" + gameId); query.append(" and
			 * virn_code='"); query.append(encodedVirnCode); query.append("'");
			 */

			statement = connection.prepareStatement(QueryManager
					.getST1PWTCheckQuery());
			statement.setInt(1, gameNbr);
			statement.setInt(2, gameId);
			statement.setString(3, encodedVirnCode);

			logger.debug("GameId:" + gameId);
			// logger.debug("Query:: " + query);
			resultSet = statement.executeQuery();
			logger.debug("ResultSet:" + resultSet + "---"
					+ resultSet.getFetchSize());

			String vCode = null;
			String pwtAmount = null;
			String prizeLevel = null;
			String prizeStaus = null;
			String directPlrTempTableStatus = null;
			boolean directPlrTempTableFlag = false;
			if (resultSet.next()) {

				vCode = resultSet.getString(TableConstants.SPI_VIRN_CODE);
				logger.debug("Vcode:" + vCode);
				pwtAmount = resultSet.getString(TableConstants.SPI_PWT_AMT);
				prizeLevel = resultSet
						.getString(TableConstants.SPI_PRIZE_LEVEL);
				prizeStaus = resultSet.getString("status");

				if (prizeStaus.equalsIgnoreCase("CLAIM_RET")) {

					String orgnameRet = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement1 = connection.createStatement();
					// String retDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select retailer_org_id from st_se_agent_pwt where
					// virn_code='"+vCode+"' and game_id="+gameId+")";
					String retDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_agent_pwt b,st_lms_agent_receipts c,st_lms_agent_transaction_master e where b.virn_code='"
							+ vCode
							+ "' and b.game_id="
							+ gameId
							+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=e.transaction_id)";
					logger.debug("query for get org name " + retDetailsQuery);
					resultSet1 = statement1.executeQuery(retDetailsQuery);
					while (resultSet1.next()) {
						orgnameRet = resultSet1.getString("name");
						receiptNumber = resultSet1.getString("generated_id");
						receiptTime = resultSet1
								.getTimestamp("transaction_date");
					}

					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					// pwtBean.setMessage("Already Paid by Agent for retailer
					// "+orgnameRet+".it is to be paid as PWT from Agent,not as
					// Direct Player PWT");
					// pwtBean.setMessage("Already Paid to Retailer :" +
					// orgnameRet+" with receipt number :"+receiptNumber+" on
					// "+receiptTime);
					pwtBean.setMessage("Already Paid to Retailer: "
							+ orgnameRet + " on Voucher Number: "
							+ receiptNumber + " at " + receiptTime);
					pwtBean.setMessageCode("112101");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;

				} else if (prizeStaus.equalsIgnoreCase("CLAIM_AGT_TEMP")) {

					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement1 = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+vCode+"' and game_id="+gameId+")";
					String agtDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
							+ vCode
							+ "' and game_id="
							+ gameId
							+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";
					logger.debug("query for get org name " + agtDetailsQuery);
					resultSet1 = statement1.executeQuery(agtDetailsQuery);
					while (resultSet1.next()) {
						orgname = resultSet1.getString("name");
						receiptNumber = resultSet1.getString("receipt_id");
						receiptTime = resultSet1.getTimestamp("date_entered");
					}

					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					// pwtBean.setMessage("Already Verified in Bulk Receipt at
					// BO, Fianl Payment Pending ");
					// pwtBean.setMessage("Already Verified in Bulk Receipt at
					// BO for agent: "+orgname+",Receipt number:
					// "+receiptNumber+" on "+receiptTime+", Fianl Payment
					// Pending");
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at BO for agent: "
									+ orgname
									+ " on Bulk Receipt Number: "
									+ receiptNumber
									+ " at "
									+ receiptTime
									+ ", Final Payment Pending");
					pwtBean.setMessageCode("112102");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;

				} else if (prizeStaus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {

					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
					pwtBean.setMessageCode("112103");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;

				} else if (prizeStaus.equalsIgnoreCase("CLAIM_AGT")) {

					String orgname = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement1 = connection.createStatement();
					// String agtDetailsQuery="select name from
					// st_lms_organization_master where organization_id in
					// (select agent_org_id from st_se_bo_pwt where
					// virn_code='"+vCode+"' and game_id="+gameId+")";
					String agtDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_bo_pwt b,st_lms_bo_receipts c,st_lms_bo_transaction_master e where b.virn_code='"
							+ vCode
							+ "' and b.game_id="
							+ gameId
							+ " and a.organization_id=b.agent_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=e.transaction_id)";
					logger.debug("query for get org name " + agtDetailsQuery);
					resultSet1 = statement1.executeQuery(agtDetailsQuery);
					while (resultSet1.next()) {
						orgname = resultSet1.getString("name");
						receiptNumber = resultSet1.getString("generated_id");
						receiptTime = resultSet1
								.getTimestamp("transaction_date");
					}

					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					// pwtBean.setMessage("Already paid to Agent:: "+orgname);
					// pwtBean.setMessage("Already Paid to Agent :" + orgname+"
					// with receipt number :"+receiptNumber+" on "+receiptTime);
					pwtBean.setMessage("Already Paid to Agent: " + orgname
							+ " on Voucher Number: " + receiptNumber + " at "
							+ receiptTime);
					pwtBean.setMessageCode("112104");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;
				} else if (prizeStaus.equalsIgnoreCase("CLAIM_PLR_TEMP")) {

					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement1 = connection.createStatement();
					// String plrDetailsQuery="select first_name,last_name,city
					// from st_lms_player_master where player_id in (select
					// player_id from st_se_direct_player_pwt where
					// virn_code='"+vCode+"' and game_id="+gameId+")";
					String plrDetailsQuery = "select a.pwt_receipt_id,a.transaction_date,b.first_name,b.last_name,b.city from st_se_direct_player_pwt_temp_receipt a,st_lms_player_master b where a.virn_code='"
							+ vCode
							+ "' and a.game_id="
							+ gameId
							+ " and b.player_id=a.player_id";
					logger
							.debug("query for get player name "
									+ plrDetailsQuery);
					resultSet1 = statement1.executeQuery(plrDetailsQuery);
					while (resultSet1.next()) {
						playerFirstName = resultSet1.getString("first_name");
						playerLastName = resultSet1.getString("last_name");
						playercity = resultSet1.getString("city");
						receiptNumber = resultSet1.getString("pwt_receipt_id");
						receiptTime = resultSet1
								.getTimestamp("transaction_date");
					}

					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					// pwtBean.setMessage("Already Verified and in process for
					// Direct Player PWT");
					// pwtBean.setMessage("Already in Process for Direct Player
					// PWT for Player: "+playerFirstName+" "+playerLastName+"
					// "+playercity+" with temporary Receipt Number:
					// "+receiptNumber+" issued on
					// "+receiptTime+",Payment/Approval Pending");
					pwtBean
							.setMessage("Already in Process for Direct Player PWT for Player: "
									+ playerFirstName
									+ " "
									+ playerLastName
									+ ","
									+ playercity
									+ " on Temporary Receipt Number: "
									+ receiptNumber
									+ " issued on "
									+ receiptTime + ",Payment/Approval Pending");
					pwtBean.setMessageCode("112105");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;

				} else if (prizeStaus.equalsIgnoreCase("CLAIM_PLR")) {
					String playerFirstName = null;
					String playerLastName = null;
					String playercity = null;
					String receiptNumber = null;
					Timestamp receiptTime = null;
					statement1 = connection.createStatement();
					// String plrDetailsQuery="select first_name,last_name,city
					// from st_lms_player_master where player_id in (select
					// player_id from st_se_direct_player_pwt where
					// virn_code='"+vCode+"' and game_id="+gameId+")";
					String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id from st_se_direct_player_pwt a,st_lms_player_master b,st_lms_bo_receipts c where a.virn_code='"
							+ vCode
							+ "' and a.game_id="
							+ gameId
							+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=a.transaction_id)";
					logger
							.debug("query for get player name "
									+ plrDetailsQuery);
					resultSet1 = statement1.executeQuery(plrDetailsQuery);
					while (resultSet1.next()) {
						playerFirstName = resultSet1.getString("first_name");
						playerLastName = resultSet1.getString("last_name");
						playercity = resultSet1.getString("city");
						receiptNumber = resultSet1.getString("generated_id");
						receiptTime = resultSet1
								.getTimestamp("transaction_date");
					}

					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					// pwtBean.setMessage("Already paid as Direct Player PWT to
					// Player " +playerFirstName+ " " +playerLastName+" "+
					// playercity);
					// pwtBean.setMessage("Already Paid as Direct Player PWT to
					// Player: "+playerFirstName+" "+playerLastName+ " "
					// +playercity+" with receipt "+receiptNumber+" on
					// "+receiptTime);
					pwtBean
							.setMessage("Already Paid as Direct Player PWT to Player: "
									+ playerFirstName
									+ " "
									+ playerLastName
									+ ","
									+ playercity
									+ " on Voucher Number "
									+ receiptNumber + " at " + receiptTime);
					pwtBean.setMessageCode("112106");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;

				} else if (prizeStaus.equalsIgnoreCase("CLAIM_PLR_RET")) {
					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					pwtBean
							.setMessage("This Virn has been paid to Player but not claimed by retailer ");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;

				} else if (prizeStaus.equalsIgnoreCase("CLAIM_RET_TEMP")) {
					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");
					pwtBean.setMessageCode("112107");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;
				} else if (prizeStaus.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					pwtBean
							.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");
					pwtBean.setMessageCode("112108");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;
				} else if (prizeStaus.equalsIgnoreCase("UNCLM_PWT")) {

					pwtBean.setValid(true);
					pwtBean.setPwtAmount(pwtAmount);
					pwtBean.setHighLevel(true);
					pwtBean.setVirnCode(virnCode);
					pwtBean.setMessage("Register Player for further Process");
					pwtBean.setMessageCode("111101");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = true;

				} else if (prizeStaus.equalsIgnoreCase("UNCLM_CANCELLED")) {

					pwtBean.setValid(true);
					pwtBean.setPwtAmount(pwtAmount);
					pwtBean.setHighLevel(true);
					pwtBean.setVirnCode(virnCode);
					pwtBean.setMessage("Register Player for further Process");
					pwtBean.setMessageCode("111103");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = true;

				} else if (prizeStaus.equalsIgnoreCase("CANCELLED_PERMANEMT")) {
					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					pwtBean
							.setMessage("Tampered/Damaged/Defaced VIRN as noted at BO");
					pwtBean.setMessageCode("112109");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;
				} else {
					pwtBean.setValid(false);
					pwtBean.setHighLevel(false);
					pwtBean.setVirnCode(virnCode);
					pwtBean.setMessage("This Virn is Fake ");
					pwtList.add(pwtBean);
					setPwtList(pwtList);
					isVerified = false;

				}

			} else {
				pwtBean.setValid(false);
				pwtBean.setHighLevel(false);
				pwtBean.setVirnCode(virnCode);
				pwtBean.setMessage("No Prize");
				pwtBean.setMessageCode("112111");
				pwtList.add(pwtBean);
				setPwtList(pwtList);
				isVerified = false;
			}

			/*
			 * 
			 * if(prizeStaus.equalsIgnoreCase("UNCLM_PWT")){
			 * 
			 * //check if it is in direct player temp table String
			 * directPlrTempTableChq="select status from
			 * st_se_direct_player_pwt_temp_receipt where virn_code='"+vCode+"'
			 * and game_id="+gameId; logger.debug("Query for direct player temp
			 * table table:: "+directPlrTempTableChq);
			 * statement3=connection.createStatement();
			 * resultSet3=statement3.executeQuery(directPlrTempTableChq);
			 * while(resultSet3.next()){ directPlrTempTableFlag=true;
			 * directPlrTempTableStatus=resultSet3.getString("status"); }
			 * if(directPlrTempTableFlag==true &&
			 * directPlrTempTableStatus.equals("CANCEL")){
			 * pwtBean.setValid(true); pwtBean.setPwtAmount(pwtAmount);
			 * pwtBean.setHighLevel(true); pwtBean.setVirnCode(virnCode);
			 * pwtBean.setMessage("Valid Ticket && this ticket has been
			 * cancelled by BO previously So Please Check it Again");
			 * pwtList.add(pwtBean); setPwtList(pwtList); isVerified=true;
			 * 
			 * }else if(directPlrTempTableStatus==null){ pwtBean.setValid(true);
			 * pwtBean.setPwtAmount(pwtAmount); pwtBean.setHighLevel(true);
			 * pwtBean.setVirnCode(virnCode); pwtBean.setMessage("Valid
			 * Ticket"); pwtList.add(pwtBean); setPwtList(pwtList);
			 * isVerified=true; }else
			 * if(directPlrTempTableStatus.equals("PND_PWT")){
			 * pwtBean.setValid(false); pwtBean.setHighLevel(false);
			 * pwtBean.setVirnCode(virnCode); pwtBean.setMessage("This Ticket
			 * has been verified for Direct Player PWT table so go for PWT
			 * Payments"); pwtList.add(pwtBean); setPwtList(pwtList);
			 * isVerified=false; }
			 * 
			 * 
			 * }else if(prizeStaus.equalsIgnoreCase("CLAIM_PLR")){
			 * 
			 * 
			 * String playerFirstName=null; String playerLastName=null; String
			 * playercity=null; statement1=connection.createStatement(); String
			 * plrDetailsQuery="select first_name,last_name,city from
			 * st_lms_player_master where player_id in (select player_id from
			 * st_se_direct_player_pwt where virn_code='"+vCode+"' and
			 * game_id="+gameId+")"; logger.debug("query for get player name " +
			 * plrDetailsQuery);
			 * resultSet1=statement1.executeQuery(plrDetailsQuery);
			 * while(resultSet1.next()){ playerFirstName=
			 * resultSet1.getString("first_name"); playerLastName=
			 * resultSet1.getString("last_name"); playercity=
			 * resultSet1.getString("city"); }
			 * 
			 * 
			 * pwtBean.setValid(false); pwtBean.setHighLevel(false);
			 * pwtBean.setVirnCode(virnCode); pwtBean.setMessage("This Virn has
			 * been paid By BO to Player:: " +playerFirstName+ " "
			 * +playerLastName+" "+ playercity); pwtList.add(pwtBean);
			 * setPwtList(pwtList); isVerified=false; }else
			 * if(prizeStaus.equalsIgnoreCase("CLAIM_AGT")){
			 * 
			 * 
			 * String orgname=null; statement1=connection.createStatement();
			 * String agtDetailsQuery="select name from
			 * st_lms_organization_master where organization_id in (select
			 * agent_org_id from st_se_bo_pwt where virn_code='"+vCode+"' and
			 * game_id="+gameId+")"; logger.debug("query for get org name " +
			 * agtDetailsQuery);
			 * resultSet1=statement1.executeQuery(agtDetailsQuery);
			 * while(resultSet1.next()){ orgname= resultSet1.getString("name"); }
			 * 
			 * 
			 * pwtBean.setValid(false); pwtBean.setHighLevel(false);
			 * pwtBean.setVirnCode(virnCode); pwtBean.setMessage("This Virn has
			 * been paid to Agent:: "+orgname+ " by BO"); pwtList.add(pwtBean);
			 * setPwtList(pwtList); isVerified=false;
			 * 
			 * }else if(prizeStaus.equalsIgnoreCase("CLAIM_RET")){
			 * 
			 * String orgnameRet=null; statement1=connection.createStatement();
			 * String retDetailsQuery="select name from
			 * st_lms_organization_master where organization_id in (select
			 * retailer_org_id from st_se_agent_pwt where virn_code='"+vCode+"'
			 * and game_id="+gameId+")"; logger.debug("query for get org name " +
			 * retDetailsQuery);
			 * resultSet1=statement1.executeQuery(retDetailsQuery);
			 * while(resultSet1.next()){ orgnameRet=
			 * resultSet1.getString("name"); }
			 * 
			 * 
			 * pwtBean.setValid(false); pwtBean.setHighLevel(false);
			 * pwtBean.setVirnCode(virnCode); pwtBean.setMessage("This Virn has
			 * been paid to Retailer:: "+orgnameRet+" by Agent");
			 * pwtList.add(pwtBean); setPwtList(pwtList); isVerified=false; } } }
			 * else{ pwtBean.setValid(false); pwtBean.setHighLevel(false);
			 * pwtBean.setVirnCode(virnCode); pwtBean.setMessage("This Virn has
			 * been verified for temporary table"); pwtList.add(pwtBean);
			 * setPwtList(pwtList); isVerified=false; }
			 */

		}

		catch (SQLException e) {
			logger.error("Exception: " + e);
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
				logger.error("Exception: " + se);
				se.printStackTrace();
				throw new LMSException(se);
			}
		}

		return isVerified;

	}

	void setPwtList(List pwtList) {
		this.pwtList = pwtList;

	}
}
