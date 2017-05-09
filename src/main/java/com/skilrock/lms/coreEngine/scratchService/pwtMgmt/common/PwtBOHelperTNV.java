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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.MD5Encoder;

/**
 * This is a helper class providing methods for handing PWT at BO's end
 * 
 * @author Skilrock Technologies
 * 
 */
public class PwtBOHelperTNV {

	List<ActiveGameBean> activeGameList;
	/**
	 * This method returns a list of the active games
	 * 
	 * @return List
	 * @throws LMSException
	 */
	String receiptNum = null;
	StringBuilder virn = null;

	public List<ActiveGameBean> getActiveGameList() {
		return activeGameList;
	}

	public List getActiveGames() throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {

			ActiveGameBean gameBean = null;
			List<ActiveGameBean> searchResults = new ArrayList<ActiveGameBean>();

			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();

			statement = connection.createStatement();

			String query = QueryManager.getST1ActiveGamesQuery();

			resultSet = statement.executeQuery(query);
			int gameNbr;
			String gameName;

			while (resultSet.next()) {

				gameBean = new ActiveGameBean();
				gameBean
						.setGameId(resultSet.getInt(TableConstants.SGM_GAME_ID));
				gameBean.setAgentPwtCommRate(resultSet
						.getDouble(TableConstants.SGM_AGT_PWT_RATE));
				gameNbr = resultSet.getInt(TableConstants.SGM_GAME_NBR);
				gameName = resultSet.getString(TableConstants.SGM_GAME_NAME);
				gameBean.setGameNbr_Name(gameNbr + delimiter + gameName);

				searchResults.add(gameBean);

			}

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

		// return null;

	}

	/**
	 * This method returns a list of all agents registered under the BO
	 * 
	 * @return List
	 * @throws LMSException
	 */
	public List getAgents(UserInfoBean userInfo) throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");
		List<OrgBean> searchResults = new ArrayList<OrgBean>();
		try {

			OrgBean agentOrgBean = null;

			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();

			String query = QueryManager.getST1AgtOrgQueryPwt();// + " group by
			// name";
			statement = connection.prepareStatement(query);
			statement.setInt(1, userInfo.getUserOrgId());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				agentOrgBean = new OrgBean();
				agentOrgBean.setOrgId(resultSet
						.getInt(TableConstants.SOM_ORG_ID));
				agentOrgBean.setOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
				agentOrgBean.setUserId(resultSet
						.getInt(TableConstants.SUM_USER_ID));

				searchResults.add(agentOrgBean);

			}

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

		return searchResults;

	}

	private String getEncodedVirnCode(String[] virnCode) {

		StringBuilder encodedVirnCode = new StringBuilder("");

		for (String element : virnCode) {

			encodedVirnCode.append("'");
			encodedVirnCode.append(MD5Encoder.encode(element));
			encodedVirnCode.append("'");
			encodedVirnCode.append(",");

		}
		int length = encodedVirnCode.length();
		encodedVirnCode.deleteCharAt(length - 1);

		return encodedVirnCode.toString();
	}

	private String getEncodedTktNbr(String[] tktNbr) {

		StringBuilder encodedVirnCode = new StringBuilder("");

		for (String element : tktNbr) {

			encodedVirnCode.append("'");
			encodedVirnCode.append(MD5Encoder.encode(element));
			encodedVirnCode.append("'");
			encodedVirnCode.append(",");

		}
		int length = encodedVirnCode.length();
		encodedVirnCode.deleteCharAt(length - 1);

		return encodedVirnCode.toString();
	}

	// Temporary Receipt ENds
	/**
	 * This method returns the sum of PWT amount for the valid entries
	 * 
	 * @param pwtList
	 * @return double
	 */
	public double getPwtAmount(List<PwtBean> pwtList) {

		double amount = 0.0;

		if (pwtList != null) {
			for (PwtBean pwtBean : pwtList) {
				if (pwtBean.getIsValid()) {
					amount += Double.parseDouble(pwtBean.getPwtAmount());
				}
			}
		}
		return amount;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	/*
	 * //Temporary virn to final receipt
	 * 
	 * private int saveBOPwtTicketsTmpReceipt(List<PwtBean> pwtList, int
	 * gameId, String boOrgName,int userID,int userOrgID,int agentOrgId, int
	 * agtUserId, double agtPwtCommRate,Connection connection, StringBuilder
	 * virn,int gameNbr) throws LMSException{ try{ PreparedStatement masterPstmt =
	 * null; PreparedStatement detailPstmt = null; PreparedStatement invPstmt =
	 * null; PreparedStatement agtGameCommVarPstmt = null;
	 * 
	 * ResultSet resultSet = null; ResultSet resultSetCommVar = null;
	 * 
	 * int trnId = -1;
	 * 
	 * if (pwtList != null) { int size = pwtList.size(); QueryManager
	 * queryManager = null;
	 * 
	 * PwtBean pwtBean = null;
	 * 
	 * String masterQuery = null; String detailQuery = null; String invQuery =
	 * null;
	 * 
	 * String agtGameCommVarQuery=null; double agtGameCommVariance=0.0;
	 * 
	 * double pwtAmt = 0.0; double commAmt = 0.0; double netAmt = 0.0; double
	 * creditAmt = 0.0;
	 * 
	 * if (size > 0) {
	 * 
	 * //added by yogesh to gat agent game commission variance
	 * agtGameCommVarQuery="select pwt_comm_variance from
	 * st_se_bo_agent_sale_pwt_comm_variance where agent_org_id=? and
	 * game_id=?";
	 * agtGameCommVarPstmt=connection.prepareStatement(agtGameCommVarQuery);
	 * agtGameCommVarPstmt.setInt(1, agentOrgId); agtGameCommVarPstmt.setInt(2,
	 * gameId); resultSetCommVar=agtGameCommVarPstmt.executeQuery();
	 * while(resultSetCommVar.next()){
	 * agtGameCommVariance=resultSetCommVar.getDouble("pwt_comm_variance"); }
	 * agtPwtCommRate=agtPwtCommRate+agtGameCommVariance;
	 * 
	 * 
	 * 
	 * 
	 * masterPstmt =
	 * connection.prepareStatement(QueryManager.insertInLMSTransactionMaster());
	 * masterPstmt.setString(1, "AGENT");
	 * 
	 * 
	 * 
	 * masterPstmt.executeUpdate(); resultSet = masterPstmt.getGeneratedKeys();
	 * if(resultSet.next()) { trnId = resultSet.getInt(1);
	 * System.out.println("Transaction Id:" + trnId);
	 * 
	 * 
	 * masterQuery = QueryManager.getST1BOMasterQuery(); masterPstmt =
	 * connection.prepareStatement(masterQuery);
	 * 
	 * detailQuery = QueryManager.getST1PwtBODetailQuery(); detailPstmt =
	 * connection.prepareStatement(detailQuery);
	 * 
	 * invQuery = QueryManager.getST1PWTBOUpdateQuery(); invPstmt =
	 * connection.prepareStatement(invQuery);
	 * System.out.println(userOrgID+"--userOrg---userId-"+userID+"-saveBOPwtTicketsTmpReceipt--agtOrgId-***"+agentOrgId);
	 * 
	 * 
	 * 
	 * masterPstmt.setInt(1, trnId); masterPstmt.setString(2, "AGENT");
	 * masterPstmt.setInt(3, agentOrgId); masterPstmt.setTimestamp(4, new
	 * java.sql.Timestamp(new Date() .getTime())); masterPstmt.setString(5,
	 * "PWT"); masterPstmt.setInt(6,userID); masterPstmt.setInt(7,userOrgID);
	 * 
	 * masterPstmt.executeUpdate();
	 * 
	 * System.out.println("OrderId::" + trnId);
	 * 
	 * for (int i = 0; i < size; i++) { pwtBean = (PwtBean) pwtList.get(i);
	 * 
	 * if (pwtBean.getIsValid()) {
	 * 
	 * String encodedVirn = pwtBean.getVirnCode(); detailPstmt.setString(1,
	 * encodedVirn); detailPstmt.setInt(2, trnId); detailPstmt.setInt(3,
	 * gameId); detailPstmt.setInt(4, agtUserId); detailPstmt.setInt(5,
	 * agentOrgId);
	 * 
	 * pwtAmt = Double.parseDouble(pwtBean.getPwtAmount()); commAmt = pwtAmt *
	 * agtPwtCommRate * 0.01; netAmt = pwtAmt + commAmt; //for current credit
	 * amt updation creditAmt += netAmt;
	 * 
	 * detailPstmt.setDouble(6, pwtAmt); detailPstmt.setDouble(7, commAmt);
	 * detailPstmt.setDouble(8, netAmt);
	 * 
	 * detailPstmt.execute(); // arun //by yogeh game nbr
	 * GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt, "CLAIM_AGT",
	 * encodedVirn, connection,gameNbr);
	 * 
	 * invPstmt.setInt(1, gameNbr); invPstmt.setInt(2, gameId);
	 * invPstmt.setString(3, encodedVirn); invPstmt.execute();
	 * virn.append("'"+encodedVirn+"',"); } }
	 * 
	 * //for current credit amt updation boolean isUpdateDone =
	 * OrgCreditUpdation .updateCreditLimitForAgent(agentOrgId, "PWT",
	 * creditAmt, connection);
	 * System.out.println("---saveBOPwtTicketsTmpReceipt--virn-"+virn); } } }
	 * 
	 * return trnId; }catch(SQLException sqle){ throw new LMSException(sqle); } }
	 * 
	 */

	/*
	 * public List<PwtBean> saveBOPwtTicketsDataTmpReceipt(String boOrgName,int
	 * userID,int userOrgID,OrgBean agtOrgBean, List<ActiveGameBean>
	 * activeGameList,String rootPath,double HighPrizeAmount,String
	 * highPrizeCriteria, List verifiedData, String receiptNum) throws
	 * LMSException{
	 * 
	 * 
	 * setReceiptNum(receiptNum); setActiveGameList(activeGameList); virn = new
	 * StringBuilder(); Connection connection = null; boolean isCommitable =
	 * false; List<PwtBean> pwtCompleteReceiptList= new ArrayList<PwtBean>();
	 * 
	 * PreparedStatement boReceiptPstmt = null; PreparedStatement
	 * boReceiptMappingPstmt = null; String boReceiptQuery = null; String
	 * boReceiptMappingQuery = null;
	 * 
	 * 
	 * int boReceiptId = -1;
	 * 
	 * double agtPwtCommRate = 0.0; int gameNbr=0;;
	 * 
	 * ActiveGameBean activeGameBean = null;
	 * 
	 * 
	 * int agentOrgId = agtOrgBean.getOrgId(); int agtUserId =
	 * agtOrgBean.getUserId();
	 * 
	 * System.out.println(agtOrgBean.getOrgName()+"-saveBOPwtTicketsDataTmpReceipt--agtOrgId-**"+agentOrgId);
	 * try{ DBConnect dbConnect = new DBConnect(); connection =
	 * dbConnect.getConnection(); connection.setAutoCommit(false);
	 * 
	 * 
	 * 
	 * 
	 * List transactionId = new ArrayList(); String receiptArr=null;
	 * 
	 * Map gameVirnCode = (Map)verifiedData.get(1); PwtBean pwtBean = null; int
	 * game_id = 0; Iterator itVirn = gameVirnCode.entrySet().iterator(); while
	 * (itVirn.hasNext()) { List<PwtBean> pwtList= new ArrayList<PwtBean>();
	 * Map.Entry pairsVirn = (Map.Entry)itVirn.next();
	 * game_id=(Integer)pairsVirn.getKey(); System.out.println("VIRN
	 * Map--game_id"+game_id); List virnList = (List)pairsVirn.getValue();
	 * if(!virnList.isEmpty()){ String[] virnArray =
	 * (String[])virnList.toArray(new String[virnList.size()]);
	 * 
	 * 
	 * for(int i=0; i<virnArray.length; i++){ if(virnArray[i]!=null &&
	 * !virnArray[i].trim().equals("")){ pwtBean= new PwtBean();
	 * pwtBean.setVirnCode(virnArray[i]); pwtBean.setValid(true);
	 * pwtList.add(pwtBean); } }
	 * 
	 * if (activeGameList != null) { for (int i = 0; i < activeGameList.size();
	 * i++) { activeGameBean = activeGameList.get(i); if
	 * (game_id==activeGameBean.getGameId()) { agtPwtCommRate =
	 * activeGameBean.getAgentPwtCommRate();
	 * gameNbr=Integer.parseInt(activeGameBean.getGameNbr_Name().split("-")[0]);
	 * break; } } }
	 * 
	 * //pwt verification not need to be done here so remove it
	 * 
	 * CommonFunctionsHelper commonHelper=new CommonFunctionsHelper(); boolean
	 * isVerified = verifyPwtTicketsTmpReceipt(game_id, virnArray,
	 * pwtList,HighPrizeAmount,highPrizeCriteria,gameNbr);
	 * 
	 * //at tje time of payment for unclaimed virn account update if
	 * (isVerified) { int transactionID = saveBOPwtTicketsTmpReceipt(pwtList,
	 * game_id,boOrgName,userID,userOrgID, agentOrgId,
	 * agtUserId,agtPwtCommRate,connection,virn,gameNbr);
	 * receiptArr=commonHelper.boEndAgtPWTPaymentProcess(pwtList, game_id,
	 * boOrgName, userOrgID, agentOrgId, agtUserId, rootPath, userID, gameNbr);
	 * if(transactionID!=0 && transactionID!=-1)
	 * transactionId.add(transactionID);
	 * 
	 * pwtCompleteReceiptList.addAll(pwtList); } } }
	 * 
	 * 
	 * System.out.println("---List of Tx Id--"+transactionId);
	 * System.out.println("---virn-"+virn); //get auto generated reciept number
	 * 
	 * if(receiptArr!=null){
	 * 
	 * String generatedReceiptNumber =
	 * commonHelper.generateReceiptBo(connection, agentOrgId, "AGENT",
	 * transactionId); if(generatedReceiptNumber!=null) isCommitable=true;
	 * boReceiptId=Integer.parseInt(receiptArr.split("-")[0]); PreparedStatement
	 * autoGenRecptPstmtBO = null; // String getLatestRecieptNumberBO="SELECT *
	 * from st_bo_receipt_gen_mapping where receipt_type=? ORDER BY generated_id
	 * DESC LIMIT 1 ";
	 * autoGenRecptPstmtBO=connection.prepareStatement(QueryManager.getBOLatestReceiptNb());
	 * autoGenRecptPstmtBO.setString(1,"RECEIPT"); ResultSet
	 * recieptRsBO=autoGenRecptPstmtBO.executeQuery(); String
	 * lastRecieptNoGeneratedBO=null;
	 * 
	 * while(recieptRsBO.next()){
	 * lastRecieptNoGeneratedBO=recieptRsBO.getString("generated_id"); }
	 * 
	 * String autoGeneRecieptNoBO=GenerateRecieptNo.getRecieptNo("RECEIPT",
	 * lastRecieptNoGeneratedBO,"BO"); // for generating
	 * receipt********************
	 * 
	 * boReceiptPstmt=connection.prepareStatement(QueryManager.insertInReceiptMaster());
	 * boReceiptPstmt.setString(1,"BO"); boReceiptPstmt.executeUpdate();
	 * 
	 * ResultSet boRSet = boReceiptPstmt.getGeneratedKeys(); while
	 * (boRSet.next()) { boReceiptId = boRSet.getInt(1); System.out.println("BO
	 * Receipt Id:" + boReceiptId); }
	 * 
	 * boReceiptQuery = QueryManager.insertInBOReceipts(); boReceiptPstmt =
	 * connection.prepareStatement(boReceiptQuery);
	 * 
	 * boReceiptPstmt.setInt(1, boReceiptId); boReceiptPstmt.setString(2,
	 * "RECEIPT"); boReceiptPstmt.setInt(3, agentOrgId);
	 * boReceiptPstmt.setString(4,"AGENT");
	 * boReceiptPstmt.setString(5,autoGeneRecieptNoBO);
	 * 
	 * //boReceiptPstmt.setString(1, autoGeneRecieptNoBO);
	 * boReceiptPstmt.setString(1, "RECEIPT"); boReceiptPstmt.setInt(2,
	 * agentOrgId);
	 * 
	 * boReceiptPstmt.execute();
	 * 
	 * 
	 * 
	 * for (int trx=0 ;trx<transactionId.size();trx++){ int transId =
	 * (Integer)transactionId.get(trx); if(transId!=0||transId!=-1){
	 * boReceiptMappingQuery = QueryManager.insertBOReceiptTrnMapping();
	 * boReceiptMappingPstmt =
	 * connection.prepareStatement(boReceiptMappingQuery);
	 * 
	 * boReceiptMappingPstmt.setInt(1, boReceiptId);
	 * boReceiptMappingPstmt.setInt(2,transId); boReceiptMappingPstmt.execute();
	 * isCommitable =true; } }
	 * 
	 * //insert into receipt gen reciept mapping
	 * 
	 * if(isCommitable){ String
	 * updateBoRecieptGenMapping=QueryManager.updateST5BOReceiptGenMapping();
	 * boReceiptGenMappingPstmt=connection.prepareStatement(updateBoRecieptGenMapping);
	 * boReceiptGenMappingPstmt.setInt(1,boReceiptId);
	 * boReceiptGenMappingPstmt.setString(2,autoGeneRecieptNoBO);
	 * boReceiptGenMappingPstmt.setString(3,"RECEIPT");
	 * boReceiptGenMappingPstmt.executeUpdate();
	 * 
	 * 
	 * 
	 * 
	 * //******************************************* virn.append("''");
	 * verifiedData.add(virn);
	 * System.out.println("---verifiedData-"+verifiedData);
	 * 
	 * 
	 * //*************************************** //Temporary receipt tables
	 * entries
	 * updateTempReceiptTables(verifiedData,boReceiptId+"",connection,userID,userOrgID);
	 * 
	 * 
	 * //*************************************** //Entries in pwt Table
	 * equivalent to PwtTicketHelper.saveTicketsData(game_id,VerifyTicketList)
	 * List<TicketBean> VerifyTicketList = new ArrayList<TicketBean>(); int
	 * game_id_tk =0; PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
	 * System.out.println("Ticket Map--"+verifiedData.get(2)); Map gameTickNum =
	 * (HashMap)verifiedData.get(2); Iterator it =
	 * gameTickNum.entrySet().iterator(); while (it.hasNext()) { Map.Entry pairs =
	 * (Map.Entry)it.next(); game_id_tk=(Integer)pairs.getKey();
	 * VerifyTicketList = pwtTicketHelper.getVerifiedTickets((List<TicketBean>)pairs.getValue(),game_id_tk,gameNbr);
	 * VerifyTicketList =
	 * pwtTicketHelper.getGameWiseVerifiedTickets(ticketListString, gameNbr);
	 * saveTicketsData(game_id_tk,VerifyTicketList,connection); }
	 * 
	 * 
	 * 
	 * connection.commit(); if (boReceiptId > -1) { GraphReportHelper
	 * graphReportHelper = new GraphReportHelper();
	 * graphReportHelper.createTextReportBO(boReceiptId,boOrgName,userOrgID,rootPath); }
	 * 
	 * }else{ throw new LMSException("There are no Valid Transaction done for
	 * these specified PWT"); } } } catch (SQLException e) { try {
	 * connection.rollback(); } catch (SQLException e1) { // TODO Auto-generated
	 * catch block e1.printStackTrace(); } e.printStackTrace(); throw new
	 * LMSException(e); } finally {
	 * 
	 * try {
	 * 
	 * if (connection != null) { connection.close(); } } catch (SQLException se) {
	 * se.printStackTrace(); } }
	 * 
	 * return pwtCompleteReceiptList; }
	 */

	/*
	 * private boolean verifyPwtTicketsTmpReceipt(int gameId, String[] virnCode,
	 * List<PwtBean> pwtList,double HighPrizeAmount,String
	 * highPrizeCriteria,int gameNbr) throws LMSException {
	 * System.out.println("verify fumction called "); String encodedVirnCode =
	 * getEncodedVirnCodeTmpReceipt(virnCode); System.out.println("---((((((::" +
	 * encodedVirnCode);
	 * 
	 * Connection connection = null; Statement statement = null; Statement
	 * statement2 = null; Statement statement3 = null;
	 * 
	 * ResultSet resultSet = null; ResultSet resultSet2 = null; ResultSet
	 * resultSet3 = null;
	 * 
	 * 
	 * 
	 * boolean isVerified = false; //int orderId = -1;
	 * 
	 * if (pwtList != null) { int size = pwtList.size();
	 * 
	 * PwtBean pwtBean = null;
	 * 
	 * StringBuffer query = new StringBuffer();
	 * 
	 * if (size > 0) { try { DBConnect dbConnect = new DBConnect(); connection =
	 * dbConnect.getConnection();
	 * 
	 * statement = connection.createStatement();
	 * query.append(QueryManager.getST1PWTBOCheckQuery()); query.append("
	 * st_se_pwt_inv_"+gameNbr+" where "); query.append(" game_id = ");
	 * query.append("" + gameId); query.append(" and virn_code in (");
	 * query.append(encodedVirnCode); query.append(")");
	 * 
	 * System.out.println("GameId:" + gameId); System.out.println("Query:: " +
	 * query); resultSet = statement.executeQuery(query.toString());
	 * System.out.println("ResultSet:" + resultSet + "---" +
	 * resultSet.getFetchSize());
	 * 
	 * String vCode = null; String pwtAmount = null; String enVirnCode = null;
	 * String prizeLevel = null; String prizeStatus=null;
	 * 
	 * while (resultSet.next()) {
	 * 
	 * vCode = resultSet .getString(TableConstants.SPI_VIRN_CODE);
	 * System.out.println("Vcode:" + vCode); pwtAmount = resultSet
	 * .getString(TableConstants.SPI_PWT_AMT); prizeLevel = resultSet
	 * .getString(TableConstants.SPI_PRIZE_LEVEL);
	 * prizeStatus=resultSet.getString("status");
	 * 
	 * for (int j = 0; j < pwtList.size(); j++) {
	 * 
	 * pwtBean = (PwtBean) pwtList.get(j); enVirnCode = pwtBean.getVirnCode();
	 * //System.out.println("for loop running " + j+ " times " +
	 * pwtBean.getVirnCode()); // check for entry into temp table
	 * 
	 * if(enVirnCode.equals(vCode)) { if
	 * (prizeStatus.equalsIgnoreCase("CLAIM_RET")) { if
	 * (enVirnCode.equals(vCode)) {
	 * 
	 * isVerified = true; pwtBean.setValid(true);
	 * pwtBean.setPwtAmount(pwtAmount); pwtBean.setMessage("Credited to
	 * Concerned Party"); pwtBean.setMessageCode("111001"); } break; }else if
	 * (prizeStatus.equalsIgnoreCase("CLAIM_AGT_TEMP")) { if
	 * (enVirnCode.equals(vCode)) { isVerified = true; pwtBean.setValid(true);
	 * pwtBean.setPwtAmount(pwtAmount); pwtBean.setMessage("AAlready Verified in
	 * Bulk Receipt at BO, Fianl Payment Pending");
	 * pwtBean.setMessageCode("112001"); } break; } else if
	 * (prizeStatus.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) { if
	 * (enVirnCode.equals(vCode)) { isVerified = true; pwtBean.setValid(true);
	 * pwtBean.setPwtAmount(pwtAmount); pwtBean.setMessage("Already Verified in
	 * Bulk Receipt at BO, Fianl Payment Pending");
	 * pwtBean.setMessageCode("112001"); } break; } else if (prizeStatus
	 * .equalsIgnoreCase("CLAIM_AGT")) { if (enVirnCode.equals(vCode)) {
	 * 
	 * String orgname=null; statement2=connection.createStatement(); String
	 * agtDetailsQuery="select name from st_lms_organization_master where
	 * organization_id in (select agent_org_id from st_se_bo_pwt where
	 * virn_code='"+enVirnCode+"' and game_id="+gameId+")";
	 * System.out.println("query for get org name " + agtDetailsQuery);
	 * resultSet2=statement2.executeQuery(agtDetailsQuery);
	 * while(resultSet2.next()){ orgname= resultSet2.getString("name"); }
	 * pwtBean.setValid(false); //pwtBean.setMessage("This Ticket has been paid
	 * to Agent:: " + orgname); pwtBean.setMessage("Already Paid to Agent[" +
	 * orgname+"]"); pwtBean.setMessageCode("112003"); } break; }else if
	 * (prizeStatus .equalsIgnoreCase("CLAIM_PLR_TEMP")){ if
	 * (enVirnCode.equals(vCode)) { isVerified = true; pwtBean.setValid(true);
	 * pwtBean.setPwtAmount(pwtAmount); pwtBean.setMessage("Already in Process
	 * for Direct Player PWT,Payment/Approval Pending");
	 * pwtBean.setMessageCode("112004"); } break; }else if(prizeStatus
	 * .equalsIgnoreCase("CLAIM_PLR")){ if (enVirnCode.equals(vCode)) {
	 * 
	 * String playerFirstName=null; String playerLastName=null; String
	 * playercity=null; statement3=connection.createStatement(); String
	 * plrDetailsQuery="select first_name,last_name,city from
	 * st_lms_player_master where player_id in (select player_id from
	 * st_se_direct_player_pwt where virn_code='"+enVirnCode+"' and
	 * game_id="+gameId+")"; System.out.println("query for get player name " +
	 * plrDetailsQuery); resultSet3=statement3.executeQuery(plrDetailsQuery);
	 * while(resultSet3.next()){ playerFirstName=
	 * resultSet3.getString("first_name"); playerLastName=
	 * resultSet3.getString("last_name"); playercity=
	 * resultSet3.getString("city"); } pwtBean.setValid(false);
	 * //pwtBean.setMessage("This Ticket has been paid to Player::
	 * "+playerFirstName+" "+playerLastName+ " " +playercity);
	 * pwtBean.setMessage("Already Paid as Direct Player PWT to Player:
	 * "+playerFirstName+" "+playerLastName+ " " +playercity);
	 * pwtBean.setMessageCode("112005"); }
	 * 
	 * break; }else if(prizeStatus .equalsIgnoreCase("CLAIM_PLR_RET")){ if
	 * (enVirnCode.equals(vCode)) { pwtBean.setValid(false);
	 * pwtBean.setMessage("This Ticket has been paid to Player but not claimed
	 * by retailer "); }
	 * 
	 * break; }else if(prizeStatus .equalsIgnoreCase("CLAIM_RET_TEMP")){ if
	 * (enVirnCode.equals(vCode)) { pwtBean.setValid(false);
	 * //pwtBean.setMessage("This Ticket is in process between Retailer and
	 * Agent "); pwtBean.setMessage("Already Verified in Bulk Receipt at
	 * Agent,Final Payment Pending"); pwtBean.setMessageCode("112006"); } break;
	 * }else if(prizeStatus .equalsIgnoreCase("CLAIM_PLR_RET_TEMP")){ if
	 * (enVirnCode.equals(vCode)) { pwtBean.setValid(false);
	 * //pwtBean.setMessage("This Ticket is in process between Retailer and
	 * Agent "); pwtBean.setMessage("Already Verified in Bulk Receipt at
	 * Agent,Final Payment Pending"); pwtBean.setMessageCode("112007"); } break;
	 * }else if(prizeStatus.equalsIgnoreCase("UNCLM_PWT")){ if
	 * (enVirnCode.equals(vCode)) {
	 * 
	 * if ((highPrizeCriteria.equals("level") && prizeLevel.equals("HIGH")) ||
	 * (highPrizeCriteria.equals("amt") &&
	 * (Double.parseDouble(pwtAmount)>=HighPrizeAmount))) {
	 * System.out.println("inside high prize"); System.out.println("criteria is " +
	 * highPrizeCriteria); pwtBean.setHighLevel(true); pwtBean.setValid(false);
	 * //pwtBean.setMessage("This ticket is high prize ticket so please go for
	 * direct player PWT"); pwtBean.setMessage("High prize VIRN can't be Paid to
	 * Agent.It is to be paid as Direct Player PWT");
	 * pwtBean.setMessageCode("112008"); }else{ isVerified = true;
	 * pwtBean.setValid(true); pwtBean.setPwtAmount(pwtAmount);
	 * //pwtBean.setMessage("This ticket is valid and should be paid to Agent");
	 * pwtBean.setMessage("Credited to Concerned Party");
	 * pwtBean.setMessageCode("111002"); } }
	 * 
	 * break; }else if(prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")){ if
	 * (enVirnCode.equals(vCode)) {
	 * 
	 * if ((highPrizeCriteria.equals("level") && prizeLevel.equals("HIGH")) ||
	 * (highPrizeCriteria.equals("amt") &&
	 * (Double.parseDouble(pwtAmount)>=HighPrizeAmount))) {
	 * System.out.println("inside high prize"); System.out.println("criteria is " +
	 * highPrizeCriteria); pwtBean.setHighLevel(true); pwtBean.setValid(false);
	 * //pwtBean.setMessage("This ticket is high prize ticket so please go for
	 * direct player PWT"); pwtBean.setMessage("High prize VIRN can't be paid to
	 * Agent.It is to be paid as Direct Player PWT");
	 * pwtBean.setMessageCode("112011"); }else{ isVerified = true;
	 * pwtBean.setValid(true); pwtBean.setPwtAmount(pwtAmount);
	 * //pwtBean.setMessage("This ticket is valid and hs been cancelled by BO so
	 * please check it"); pwtBean.setMessage("Credited to Concerned Party");
	 * pwtBean.setMessageCode("111003"); } } break; } else if(prizeStatus
	 * .equalsIgnoreCase("CANCELLED_PERMANENT")){ if (enVirnCode.equals(vCode)) {
	 * pwtBean.setValid(false); pwtBean.setMessage("Tampered/Damaged/Defaced
	 * VIRN as noted at BO"); pwtBean.setMessageCode("112009"); } break; } } } } }
	 * catch (SQLException e) {
	 * 
	 * e.printStackTrace(); throw new LMSException(e); } finally {
	 * 
	 * try {
	 * 
	 * if (statement != null) { statement.close(); } if (connection != null) {
	 * connection.close(); } } catch (SQLException se) { se.printStackTrace(); } } } }
	 * return isVerified; }
	 */

	/*
	 * private String getEncodedVirnCodeTmpReceipt(String[] virnCode) {
	 * 
	 * StringBuffer encodedVirnCode = new StringBuffer("");
	 * 
	 * for (int i = 0; i < virnCode.length; i++) {
	 * 
	 * encodedVirnCode.append("'"); encodedVirnCode.append(virnCode[i]);
	 * encodedVirnCode.append("'"); encodedVirnCode.append(","); } int length =
	 * encodedVirnCode.length(); encodedVirnCode.deleteCharAt(length - 1);
	 * 
	 * return encodedVirnCode.toString(); }
	 */

	public Map getVirnList(String[] virnCode, String virnFile)
			throws LMSException {
		Map map = new TreeMap();
		List<PwtBean> pwtList = new ArrayList<PwtBean>();
		PwtBean pwtBean = null;
		List<String> virnStringList = new ArrayList<String>();
		List<PwtBean> duplicateVirnList = new ArrayList<PwtBean>();
		for (int i = 0; i < virnCode.length; i++) {
			if (virnCode[i] != null && !virnCode[i].trim().equals("")) {
				if (virnStringList.contains(virnCode[i].trim())) {
					pwtBean = new PwtBean();
					pwtBean.setVirnCode(virnCode[i].trim());
					pwtBean.setValid(false);
					pwtBean.setVerificationStatus("InValid VIRN");
					pwtBean.setMessage("Duplicate Virn Entry in File");
					pwtBean.setMessageCode("112013");
					duplicateVirnList.add(pwtBean);

				} else {
					virnStringList.add(virnCode[i].trim());
				}
			}
		}
		// code added here by yogesh to read virn from text file also
		try {
			InputStreamReader fileStreamReader = new InputStreamReader(
					new FileInputStream(virnFile));
			BufferedReader br = new BufferedReader(fileStreamReader);
			String strVirnLine = null;
			int fileVirnLimit = 0;
			while ((strVirnLine = br.readLine()) != null) {
				if ("".equals(strVirnLine.trim())) {
					continue;
				}
				if (fileVirnLimit > 5000) {
					map.put("error", "Data In File Exceeds limit ");
					return map;
				}
				if (virnStringList.contains(strVirnLine.trim())) {
					pwtBean = new PwtBean();
					pwtBean.setVirnCode(strVirnLine.trim());
					pwtBean.setValid(false);
					pwtBean
							.setVerificationStatus("Duplicate Virn Entry in File");
					pwtBean.setMessage("InValid VIRN");
					pwtBean.setMessageCode("112013");
					duplicateVirnList.add(pwtBean);
				} else {
					virnStringList.add(strVirnLine.trim());
				}
				fileVirnLimit++;

			}

		} catch (FileNotFoundException fe) {
			// fe.printStackTrace();
			// throw new LMSException("file not found exception",fe);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		map.put("duplicateVirnList", duplicateVirnList);
		map.put("virnStringList", virnStringList);
		return map;

	}

	public String saveBOPwtTicketsData(String gameNbr_Name, String boOrgName,
			int userOrgID, String agtOrgName, List<OrgBean> agtOrgList,
			List<ActiveGameBean> activeGameList, String[] virnCode,
			List<PwtBean> pwtList, String rootPath, double HighPrizeAmount,
			String highPrizeCriteria, int userId, String enc_scheme_type,
			String pwt_verification_type) throws LMSException {

		int gameId = -1;
		int gameNbr = 0;
		String autoGeneratedReceiptNumber = null;
		// double agtPwtCommRate = 0.0;

		ActiveGameBean activeGameBean = null;
		OrgBean agtOrgBean = null;

		// we appending game id with gameId-gameNbr-Name on JSP
		if (activeGameList != null) {
			for (int i = 0; i < activeGameList.size(); i++) {
				activeGameBean = activeGameList.get(i);
				if (gameNbr_Name.equals(activeGameBean.getGameNbr_Name())) {
					gameId = activeGameBean.getGameId();
					// agtPwtCommRate = activeGameBean.getAgentPwtCommRate();
					break;

				}
			}
		}

		int agtOrgId = -1;
		int agtUserId = -1;
		// get the AGENT org id from JSP
		if (agtOrgList != null) {
			for (int i = 0; i < agtOrgList.size(); i++) {
				agtOrgBean = agtOrgList.get(i);
				if (agtOrgName.equals(agtOrgBean.getOrgName())) {
					agtOrgId = agtOrgBean.getOrgId();
					agtUserId = agtOrgBean.getUserId();
					break;

				}
			}
		}
		 String[] tktNbr=new String[pwtList.size()];
		// get game nbr from game name number
		String[] gameNameNbrArr = gameNbr_Name.split("-");
		gameNbr = Integer.parseInt(gameNameNbrArr[0]);
		try {
			DBConnect dbConnect = new DBConnect();
			Connection connection = dbConnect.getConnection();
			connection.setAutoCommit(false);
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			// verify the ticket list here
			PwtBean pwtBean = null;
			for (int i = 0; i < pwtList.size(); i++) {
				pwtBean = pwtList.get(i);

				pwtBean.setMessage("Invalid Virn");
				pwtBean.setVerificationStatus("InValid VIRN");

				// check the format of these tickets
				// CommonFunctionsHelper commHelper = new
				// CommonFunctionsHelper();
				TicketBean tktBean;

				tktBean = commHelper.isTicketFormatValid(pwtBean
						.getTicketNumber(), gameId, connection);

				BOPwtProcessHelper boPwtHelper = new BOPwtProcessHelper();
				String tktNbrArr[] = null;
				// TicketBean bean = null;

				if (tktBean.getIsValid()) {
					tktNbrArr = tktBean.getTicketNumber().split("-");
					tktBean = boPwtHelper.checkTicketStatus(gameNbr + "",
							tktNbrArr[0] + "-" + tktNbrArr[1], tktNbrArr[2],
							gameId, connection, false);
				}
				tktNbr[i] = tktBean.getTicketNumber();
				pwtBean.setTicketNumber(tktBean.getTicketNumber());
				pwtBean.setTicketValid(tktBean.getIsValid());
				pwtBean.setTicketMessage(tktBean.getStatus());
				pwtBean.setTicketVerificationStatus(tktBean.getValidity());
				pwtBean.setUpdateTicketType(tktBean.getUpdateTicketType());
				pwtBean.setBookNumber(tktBean.getBook_nbr());
				pwtBean.setBookStatus(tktBean.getBookStatus());

			}

			boolean isVerified = verifyPwtTickets(gameId, virnCode, pwtList,
					HighPrizeAmount, highPrizeCriteria, gameNbr, agtOrgId,
					connection,tktNbr);

			// updtae ticket status here by paasing teh same connection to the
			// pwt payments as well

			for (int i = 0; i < pwtList.size(); i++) {
				pwtBean = pwtList.get(i);

				// if both the ticket and virn are valid then update the ticket
				// status
				if (pwtBean.isTicketValid() && pwtBean.getIsValid()) {

					commHelper.updateTicketInvTable(pwtBean.getTicketNumber(),
							pwtBean.getBookNumber(), gameNbr, gameId,
							"CLAIM_AGT", userId, userOrgID, pwtBean
									.getUpdateTicketType(), agtOrgId, "RETAIL",
							"WEB", connection);

					// update book status from active to claimed in table
					// st_se_game_inv_status table here
					if ("ACTIVE".equalsIgnoreCase(pwtBean.getBookStatus())) {
						commHelper.updateBookStatus(gameId, pwtBean
								.getBookNumber(), connection, "CLAIMED");
					}

				}

			}

			if (isVerified) {
				/*
				 * saveBOPwtTickets(pwtList, gameId,boOrgName, userOrgID,
				 * agtOrgId, agtUserId, rootPath,userId,gameNbr);
				 */
				CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
				autoGeneratedReceiptNumber = commonHelper
						.boEndAgtPWTPaymentProcess(pwtList, gameId, boOrgName,
								userOrgID, agtOrgId, agtUserId, rootPath,
								userId, gameNbr, connection);
				// connection.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return autoGeneratedReceiptNumber;

	}

	private void saveTmpPwtVirn(List<PwtBean> pwtList, int gameId,
			String receiptId, int agtUserId, int agtOrgId, int gameNbr)
			throws LMSException {

		Connection connection = null;
		PreparedStatement detailPstmt = null;
		PreparedStatement detailPstmtPwtInv = null;
		PreparedStatement agentPstmtPwtInv = null;

		if (pwtList != null) {
			int size = pwtList.size();
			PwtBean pwtBean = null;

			if (size > 0) {
				try {
					DBConnect dbConnect = new DBConnect();
					connection = dbConnect.getConnection();
					connection.setAutoCommit(false);
					detailPstmt = connection
							.prepareStatement("insert into st_se_tmp_pwt_inv(virn_code,game_id,receipt_id,user_id,status,date_entered) values(?,?,?,?,?,?)");
					Timestamp dateEntered = new Timestamp(new java.util.Date()
							.getTime());
					CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
					for (int i = 0; i < size; i++) {
						pwtBean = (PwtBean) pwtList.get(i);

						if (pwtBean.getIsValid()) {

							String encodedVirn = MD5Encoder.encode(pwtBean
									.getVirnCode());
							// get the current pwt status of virn
							String pwtCurStatus = GameUtilityHelper
									.getPwtStatusOfVirn(gameId, encodedVirn,
											connection, gameNbr);
							// update the pwt status
							detailPstmtPwtInv = connection
									.prepareStatement("update st_se_pwt_inv_? set status=? where virn_code =? and game_id=?");

							detailPstmtPwtInv.setInt(1, gameNbr);
							if ("UNCLM_PWT".equalsIgnoreCase(pwtCurStatus)) {
								detailPstmtPwtInv
										.setString(2, "CLAIM_AGT_TEMP");
							} else if ("UNCLM_CANCELLED"
									.equalsIgnoreCase(pwtCurStatus)) {
								detailPstmtPwtInv
										.setString(2, "CLAIM_AGT_TEMP");
							} else if ("CLAIM_RET"
									.equalsIgnoreCase(pwtCurStatus.trim())) {
								detailPstmtPwtInv.setString(2,
										"CLAIM_RET_AGT_TEMP");
							} else if ("CLAIM_RET_UNCLM"
									.equalsIgnoreCase(pwtCurStatus.trim())) {
								detailPstmtPwtInv.setString(2,
										"CLAIM_RET_AGT_TEMP");
							} else if ("CLAIM_PLR_AGT_UNCLM_DIR"
									.equalsIgnoreCase(pwtCurStatus.trim())) {
								detailPstmtPwtInv.setString(2,
										"CLAIM_PLR_AGT_TEMP");
							}

							/*
							 * else
							 * if(!"NONE".equalsIgnoreCase(pwtBean.getInUnclmed()) &&
							 * "IN_PLR_UNCLM".equalsIgnoreCase(pwtBean.getInUnclmed()))
							 * detailPstmtPwtInv.setString(2,
							 * "CLAIM_AGT_TEMP_UNCLM_PLR"); else
							 * if(!"NONE".equalsIgnoreCase(pwtBean.getInUnclmed()) &&
							 * "IN_AGENT_UNCLM".equalsIgnoreCase(pwtBean.getInUnclmed()))
							 * detailPstmtPwtInv.setString(2,
							 * "CLAIM_AGT_TEMP_UNCLM_AGT");
							 */

							detailPstmtPwtInv.setString(3, encodedVirn);
							detailPstmtPwtInv.setInt(4, gameId);
							detailPstmtPwtInv.execute();

							// update status to BULK_BO in agent_pwt or agent
							// direct player PWT for which isunclaimed true
							if (!"NONE"
									.equalsIgnoreCase(pwtBean.getInUnclmed())) {// 
								String tableType;
								if ("IN_PLR_UNCLM".equalsIgnoreCase(pwtBean
										.getInUnclmed())) {
									tableType = "PLAYER";
								} else {
									tableType = "AGENT";
								}

								//commented because temp not in use
								/*commonHelper.updateOrgForUnClaimedVirn(
										agtOrgId, "AGENT", encodedVirn,
										"BULK_BO", gameId, tableType,
										connection);*/
							}

							detailPstmt.setString(1, encodedVirn);
							detailPstmt.setInt(2, gameId);
							detailPstmt.setString(3, receiptId);
							detailPstmt.setInt(4, agtUserId);
							detailPstmt.setString(5, "OPEN");
							detailPstmt.setTimestamp(6, dateEntered);

							detailPstmt.execute();

						}

					}
					connection.commit();

				} catch (SQLException e) {

					e.printStackTrace();
					throw new LMSException(e);

				} finally {

					try {
						if (detailPstmt != null) {
							detailPstmt.close();
						}
						if (detailPstmtPwtInv != null) {
							detailPstmtPwtInv.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}

			}
		}

	}

	/*
	 * private void updateTempReceiptTables(List verifiedData, String
	 * autoGeneRecieptNoBO, Connection connection,int userID,int userOrgID)
	 * throws SQLException{ Statement statementTmpTable = null;
	 * 
	 * StringBuilder virn = new StringBuilder(); StringBuilder ticket = new
	 * StringBuilder();
	 * 
	 * statementTmpTable = connection.createStatement();
	 * 
	 * System.out.println("---in updateTempReceiptTables"+verifiedData);
	 * ticket.append((((List)verifiedData.get(0)).toString().replace("[","'")).replace("]","'").replaceAll(",","','").replaceAll("
	 * ","")); System.out.println(verifiedData.get(3)+"Buffer---"+ticket); virn =
	 * ((StringBuilder)verifiedData.get(3));
	 * System.out.println("Buffer--virn-"+virn);
	 * 
	 * String tmpVirnUpdateQuery = "update st_se_tmp_pwt_inv set
	 * status='CLOSE',final_receipt_num='"+autoGeneRecieptNoBO+"' where
	 * virn_code in ("+virn.toString()+")"; String tmpTicketUpdateQuery =
	 * "update st_se_tmp_pwt_tickets_inv set
	 * status='CLOSE',final_receipt_num='"+autoGeneRecieptNoBO+"' where
	 * ticket_nbr in ("+ticket.toString()+")"; String insertTmpReceiptMaping =
	 * "insert into st_se_tmp_pwt_receipt_mapping values
	 * ('"+getReceiptNum()+"','"+autoGeneRecieptNoBO+"')";
	 * 
	 * System.out.println("--tmpVirnUpdateQuery-"+tmpVirnUpdateQuery);
	 * System.out.println("--tmpTicketUpdateQuery-"+tmpTicketUpdateQuery);
	 * System.out.println("--insertTmpReceiptMaping--"+insertTmpReceiptMaping);
	 * 
	 * statementTmpTable.executeUpdate(tmpVirnUpdateQuery);
	 * statementTmpTable.executeUpdate(tmpTicketUpdateQuery);
	 * statementTmpTable.executeUpdate(insertTmpReceiptMaping);
	 * 
	 * String game_nbr=null; ActiveGameBean activeGameBean =null; int game_id =
	 * 0;
	 * 
	 * String insertPwtTktQry = null; String updPwtTktQry = null; String
	 * tktNbrStr = null;
	 * tktNbrStr=(((List)verifiedData.get(0)).toString().replace("[","'")).replace("]","'").replaceAll(",","','").replaceAll("
	 * ",""); Map gameTickNum = (Map)verifiedData.get(2); Iterator itTkt =
	 * gameTickNum.entrySet().iterator(); while (itTkt.hasNext()) { Map.Entry
	 * pairsTkt = (Map.Entry)itTkt.next(); game_id=(Integer)pairsTkt.getKey();
	 * System.out.println("Tkt Map--game_id"+game_id); if (activeGameList !=
	 * null) { for (int i = 0; i < activeGameList.size(); i++) { activeGameBean =
	 * activeGameList.get(i); if (game_id==activeGameBean.getGameId()) {
	 * game_nbr=activeGameBean.getGameNbr_Name().split("-")[0]; break; } } }
	 * 
	 * updPwtTktQry="update st_se_pwt_tickets_inv_"+game_nbr+" aa,
	 * st_se_tmp_pwt_tickets_inv bb set aa.status='CLAIM_AGT',
	 * aa.verify_by_user='"+userID+"', aa.verify_by_org='"+userOrgID+"' where
	 * aa.status='CLAIM_RET' and aa.ticket_nbr in ("+tktNbrStr+") and
	 * aa.ticket_nbr = bb.ticket_nbr"; System.out.println("********PWT
	 * Update********"+updPwtTktQry);
	 * statementTmpTable.executeUpdate(updPwtTktQry);
	 * 
	 * insertPwtTktQry ="insert into st_se_pwt_tickets_inv_"+game_nbr+" select
	 * bb.ticket_nbr, bb.game_id, bb.book_nbr,'CLAIM_AGT' as status
	 * ,'"+userID+"' as verify_by_user,'"+userOrgID+"' as verify_by_org from
	 * st_se_tmp_pwt_tickets_inv bb where bb.receipt_id='"+receiptNum+"' and
	 * bb.game_id="+game_id+" and bb.ticket_nbr in ("+tktNbrStr+") and
	 * bb.ticket_nbr not in (select ticket_nbr from
	 * st_se_pwt_tickets_inv_"+game_nbr+")"; //insertPwtTktQry = "insert into
	 * st_se_pwt_tickets_inv_"+game_nbr+" select
	 * ticket_nbr,game_id,book_nbr,'CLAIM_AGT' as status ,'"+userID+"' as
	 * verify_by_user,'"+userOrgID+"'verify_by_org from
	 * st_se_tmp_pwt_tickets_inv where receipt_id='"+getReceiptNum()+"' and
	 * game_id="+game_id+" and ticket_nbr in ("+ticket.toString()+")";
	 * 
	 * System.out.println("********PWT Insert********"+insertPwtTktQry);
	 * statementTmpTable.executeUpdate(insertPwtTktQry); } }
	 */

	/*
	 * private void saveTicketsData(int game_id, List<TicketBean>
	 * verifiedTicketList, Connection connection) throws SQLException{
	 * 
	 * ServletContext servletContext = ServletActionContext.getServletContext();
	 * String retOnline=(String)servletContext.getAttribute("RET_ONLINE");
	 * PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
	 * 
	 * PreparedStatement Pstmt = null;
	 * 
	 * String query=null;
	 * 
	 * if(retOnline.equals("NO")) query=QueryManager.updateIntoPwtTicketsInv();
	 * else query="update st_pwt_tickets_inv set status=? where ticket_nbr=? and
	 * game_id =?";
	 * 
	 * Pstmt=connection.prepareStatement(query); Iterator<TicketBean>
	 * iterator=verifiedTicketList.iterator(); while(iterator.hasNext()){ String
	 * ticket_nbr=null; boolean isValid=false; TicketBean
	 * ticketBean=(TicketBean)iterator.next(); isValid=ticketBean.getIsValid();
	 * if(isValid==true){ ticket_nbr=ticketBean.getTicketNumber(); String
	 * book_nbr=pwtTicketHelper.getBookNbrFromTicketNo(ticket_nbr);
	 * if(retOnline.equals("NO")) { Pstmt.setString(1, ticket_nbr);
	 * Pstmt.setInt(2,game_id); Pstmt.setString(3, book_nbr); Pstmt.setString(4,
	 * "CLAIM_RET"); Pstmt.executeUpdate(); } else {
	 * 
	 * Pstmt.setString(1, "CLAIM_RET"); Pstmt.setString(2, ticket_nbr);
	 * Pstmt.setInt(3,game_id); Pstmt.executeUpdate(); } } } }
	 */

	public void saveTmpPwtVirnData(String gameNbr_Name, String receiptId,
			int agentId, String boOrgName, int userOrgID, String agtOrgName,
			List<OrgBean> agtOrgList, List<ActiveGameBean> activeGameList,
			String[] virnCode, List<PwtBean> pwtList, String rootPath,
			double HighPrizeAmount, String highPrizeCriteria)
			throws LMSException {
		System.out.println("Afterrrrrrrrr saveTmpPwtVirnData");
		int gameId = -1;
		int gameNbr = 0;
		double agtPwtCommRate = 0.0;

		ActiveGameBean activeGameBean = null;
		OrgBean agtOrgBean = null;

		if (activeGameList != null) {
			for (int i = 0; i < activeGameList.size(); i++) {
				activeGameBean = activeGameList.get(i);
				if (gameNbr_Name.equals(activeGameBean.getGameNbr_Name())) {
					gameId = activeGameBean.getGameId();
					agtPwtCommRate = activeGameBean.getAgentPwtCommRate();
					break;

				}
			}
		}
		System.out.println("Afterrrrrrrrr---if" + gameId);
		int agtOrgId = -1;
		int agtUserId = -1;

		if (agtOrgList != null) {
			for (int i = 0; i < agtOrgList.size(); i++) {
				agtOrgBean = agtOrgList.get(i);
				if (agtOrgName.equals(agtOrgBean.getOrgName())) {
					agtOrgId = agtOrgBean.getOrgId();
					agtUserId = agtOrgBean.getUserId();
					break;

				}
			}
		}
		agtUserId = agentId;

		// get game nbr from game name number
		String[] gameNameNbrArr = gameNbr_Name.split("-");
		gameNbr = Integer.parseInt(gameNameNbrArr[0]);
		DBConnect dbConnect = new DBConnect();
		Connection connection = dbConnect.getConnection();
	
		//commented because temporary not in use
		/*	boolean isVerified = verifyPwtTickets(gameId, virnCode, pwtList,
				HighPrizeAmount, highPrizeCriteria, gameNbr, agtOrgId,
				connection);

		if (isVerified) {
			saveTmpPwtVirn(pwtList, gameId, receiptId, agtUserId, agtOrgId,
					gameNbr);
		}*/
	}

	public void setActiveGameList(List<ActiveGameBean> activeGameList) {
		this.activeGameList = activeGameList;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	/**
	 * This method saves the PWT data entered by the BO user
	 * 
	 * @param gameNbr_Name
	 * @param agtOrgName
	 * @param agtOrgList
	 * @param activeGameList
	 * @param virnCode
	 * @param pwtList
	 * @param pwt_verification_type
	 * @param enc_scheme_type
	 * @param rootPath
	 * @throws LMSException
	 */

	private boolean verifyPwtTickets(int gameId, String[] virnCode,
			List<PwtBean> pwtList, double HighPrizeAmount,
			String highPrizeCriteria, int gameNbr, int agtOrgId,
			Connection connection,String[] tktNbr) throws LMSException {

		System.out.println("verify function called ");
		//String encodedVirnCode = getEncodedVirnCode(virnCode);
		String encodedTktNbr = getEncodedTktNbr(tktNbr);
		System.out.println("encoded Tkt list" + encodedTktNbr);

		// Connection connection = null;
		Statement statement = null;
		Statement statement2 = null;
		Statement statement3 = null;
		Statement statement4 = null;

		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;

		boolean isVerified = false;
		// int orderId = -1;

		if (pwtList != null) {
			//int size = pwtList.size();
			// QueryManager queryManager = null;
			PwtBean pwtBean = null;

			StringBuffer query = new StringBuffer();

			if (pwtList.size() > 0) {
				try {
					// DBConnect dbConnect = new DBConnect();
					// connection = dbConnect.getConnection();

					statement = connection.createStatement();
					// query.append(QueryManager.getST1PWTBOCheckQuery());
					System.out.println("hello       "
							+ QueryManager.getST1PWTBOCheckQuery());
					query.append(QueryManager.getST1PWTBOCheckQuery());
					query.append(" st_se_pwt_inv_" + gameNbr + " where ");
					query.append("  game_id = ");
					query.append("" + gameId);
					query.append(" and id1 in (");
					query.append(encodedTktNbr);
					query.append(")");

					System.out.println("GameId:" + gameId);
					System.out.println("Query:: " + query);
					resultSet = statement.executeQuery(query.toString());
					

					String vCode = null;
					String pwtAmount = null;
					String enVirnCode = null;
					String prizeLevel = null;
					String prizeStatus = null;
					String tNumber = null;
					String enticketNumber = null;
					// boolean pwtTempTableStatus=false;

					while (resultSet.next()) {

						vCode = resultSet
								.getString(TableConstants.SPI_VIRN_CODE);

						pwtAmount = resultSet
								.getString(TableConstants.SPI_PWT_AMT);
						prizeLevel = resultSet
								.getString(TableConstants.SPI_PRIZE_LEVEL);
						prizeStatus = resultSet.getString("status");
						tNumber = resultSet.getString("id1");
						System.out.println("DB:: Vcode:" + vCode
								+ ":ticket nbr " + tNumber);
						

						for (int j = 0; j < pwtList.size(); j++) {

							pwtBean = (PwtBean) pwtList.get(j);
							enVirnCode = MD5Encoder.encode(pwtBean.getVirnCode());
							
							pwtBean.setEncVirnCode(enVirnCode);
							enticketNumber = MD5Encoder.encode(pwtBean
									.getTicketNumber());
														
							if(enticketNumber.equalsIgnoreCase(tNumber)){
								if (pwtBean.isTicketValid()) {
									if (enticketNumber.equalsIgnoreCase(tNumber)) {
									 if (enVirnCode.equals(vCode)){										 
										if (prizeStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
											CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
											String flag = commHelper.verifyOrgForUnClaimedVirn(agtOrgId, "AGENT",enVirnCode,"UNCLAIM_BAL",gameId, connection);
											if (!"NONE".equalsIgnoreCase(flag)) {
												String orgname = null;
												String receiptNumber = null;
												Timestamp receiptTime = null;
												statement4 = connection.createStatement();
												
												String retDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_agent_pwt b,st_lms_agent_receipts c,st_lms_agent_transaction_master e where b.virn_code='"
														+ enVirnCode
														+ "' and b.game_id="
														+ gameId
														+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=e.transaction_id)";

												System.out
														.println("query for get org name "
																+ retDetailsQuery);
												resultSet4 = statement4
														.executeQuery(retDetailsQuery);
												while (resultSet4.next()) {
													orgname = resultSet4
															.getString("name");
													receiptNumber = resultSet4
															.getString("generated_id");
													receiptTime = resultSet4
															.getTimestamp("transaction_date");
												}

												isVerified = true;
												pwtBean.setValid(true);
												pwtBean
														.setVerificationStatus("Valid Virn");
												pwtBean.setPwtAmount(pwtAmount);
												// pwtBean.setMessage("Credited
												// to
												// Concerned Party");
												pwtBean
														.setMessage("Already Paid to Retailer: "
																+ orgname
																+ " on Voucher Number: "
																+ receiptNumber
																+ " at "
																+ receiptTime);
												pwtBean.setInUnclmed(flag);
												pwtBean.setMessageCode("TBD");
											} else {
												pwtBean.setValid(false);
												pwtBean
														.setVerificationStatus("InValid Virn");
												pwtBean
														.setMessage("VIRN To be Claimed by Another Agent.");
												pwtBean.setMessageCode("TBD");

											}
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("UNCLM_PWT")) {
											if (highPrizeCriteria
													.equals("level")
													&& prizeLevel
															.equals("HIGH")
													|| highPrizeCriteria
															.equals("amt")
													&& Double
															.parseDouble(pwtAmount) > HighPrizeAmount) {
												System.out
														.println("inside high prize");
												System.out
														.println("criteria is "
																+ highPrizeCriteria);
												pwtBean.setHighLevel(true);
												pwtBean.setValid(false);
												pwtBean
														.setVerificationStatus("InValid Virn");
												pwtBean
														.setMessage("High prize VIRN can't be Paid to Agent.It is to be paid as Direct Player PWT");
												pwtBean
														.setMessageCode("112008");
											} else {
												isVerified = true;
												pwtBean.setValid(true);
												pwtBean
														.setVerificationStatus("Valid Virn");
												pwtBean.setPwtAmount(pwtAmount);
												pwtBean
														.setMessage("Credited to Concerned Party");
												pwtBean
														.setMessageCode("111002");
											}
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("UNCLM_CANCELLED")) {
											if (highPrizeCriteria
													.equals("level")
													&& prizeLevel
															.equals("HIGH")
													|| highPrizeCriteria
															.equals("amt")
													&& Double
															.parseDouble(pwtAmount) > HighPrizeAmount) {
												System.out
														.println("inside high prize");
												System.out
														.println("criteria is "
																+ highPrizeCriteria);
												pwtBean.setHighLevel(true);
												pwtBean.setValid(false);
												pwtBean
														.setVerificationStatus("InValid Virn");
												pwtBean
														.setMessage("High prize VIRN can't be paid to Agent.It is to be paid as Direct Player PWT");
												pwtBean
														.setMessageCode("112011");
											} else {
												isVerified = true;
												pwtBean.setValid(true);
												pwtBean
														.setVerificationStatus("Valid Virn");
												pwtBean.setPwtAmount(pwtAmount);
												pwtBean
														.setMessage("Credited to Concerned Party");
												pwtBean
														.setMessageCode("111003");
											}
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
											CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
											String flag = commHelper
													.verifyOrgForUnClaimedVirn(
															agtOrgId, "AGENT",
															enVirnCode,
															"UNCLAIM_BAL",
															gameId, connection);
											if (!"NONE".equalsIgnoreCase(flag)) {
												// get player details from st
												// Agent
												// direct player
												// details************8

												String agtOrgNAme = null;
												String playerFirstName = null;
												String playerLastName = null;
												String playercity = null;
												String receiptNumber = null;
												Timestamp receiptTime = null;
												statement3 = connection
														.createStatement();
												// String
												// plrDetailsQuery="select
												// first_name,last_name,city
												// from
												// st_lms_player_master where
												// player_id
												// in (select player_id from
												// st_se_direct_player_pwt where
												// virn_code='"+enVirnCode+"'
												// and
												// game_id="+gameId+")";
												// String
												// plrDetailsQuery="select
												// b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id
												// from st_se_direct_player_pwt
												// a,st_lms_player_master
												// b,st_lms_bo_receipts c where
												// a.virn_code='"+enVirnCode+"'
												// and
												// a.game_id="+gameId+" and
												// a.player_id=b.player_id and
												// c.receipt_id=(select
												// receipt_id from
												// st_lms_bo_receipts_trn_mapping
												// where
												// transaction_id=a.transaction_id)";
												String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id,d.name from st_se_agt_direct_player_pwt a,st_lms_player_master b,st_lms_agent_receipts c,st_lms_organization_master d where a.virn_code='"
														+ enVirnCode
														+ "' and a.game_id="
														+ gameId
														+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=a.transaction_id) and d.organization_id=a.agent_org_id";
												System.out
														.println("query for get player name "
																+ plrDetailsQuery);
												resultSet3 = statement3
														.executeQuery(plrDetailsQuery);
												while (resultSet3.next()) {
													playerFirstName = resultSet3
															.getString("first_name");
													playerLastName = resultSet3
															.getString("last_name");
													playercity = resultSet3
															.getString("city");
													receiptNumber = resultSet3
															.getString("generated_id");
													receiptTime = resultSet3
															.getTimestamp("transaction_date");
													agtOrgNAme = resultSet3
															.getString("name");
												}

												isVerified = true;
												pwtBean.setValid(true);
												pwtBean
														.setVerificationStatus("Valid Virn");
												pwtBean.setPwtAmount(pwtAmount);
												// pwtBean.setMessage("Credited
												// to
												// Concerned Party");
												pwtBean
														.setMessage("Already Paid to Player "
																+ playerFirstName
																+ " "
																+ playerLastName
																+ " "
																+ playercity
																+ " By Agent: "
																+ agtOrgNAme
																+ " on Voucher Number: "
																+ receiptNumber
																+ " at "
																+ receiptTime);
												pwtBean.setInUnclmed(flag);
												pwtBean.setMessageCode("TBD");
											} else {
												pwtBean.setValid(false);
												pwtBean
														.setVerificationStatus("InValid Virn");
												pwtBean
														.setMessage("VIRN To be Claimed by Another Agent.");
												pwtBean.setMessageCode("TBD");
											}
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
											// show the agent details and player
											// details
											String agtOrgNAme = null;
											String playerFirstName = null;
											String playerLastName = null;
											String playercity = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement3 = connection
													.createStatement();
											// String plrDetailsQuery="select
											// first_name,last_name,city from
											// st_lms_player_master where
											// player_id in
											// (select player_id from
											// st_se_direct_player_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";
											// String plrDetailsQuery="select
											// b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id
											// from st_se_direct_player_pwt
											// a,st_lms_player_master
											// b,st_lms_bo_receipts c where
											// a.virn_code='"+enVirnCode+"' and
											// a.game_id="+gameId+" and
											// a.player_id=b.player_id and
											// c.receipt_id=(select receipt_id
											// from
											// st_lms_bo_receipts_trn_mapping
											// where
											// transaction_id=a.transaction_id)";
											String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id,d.name from st_se_agt_direct_player_pwt a,st_lms_player_master b,st_lms_agent_receipts c,st_lms_organization_master d where a.virn_code='"
													+ enVirnCode
													+ "' and a.game_id="
													+ gameId
													+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=a.transaction_id) and d.organization_id=a.agent_org_id";
											System.out
													.println("query for get player name "
															+ plrDetailsQuery);
											resultSet3 = statement3
													.executeQuery(plrDetailsQuery);
											while (resultSet3.next()) {
												playerFirstName = resultSet3
														.getString("first_name");
												playerLastName = resultSet3
														.getString("last_name");
												playercity = resultSet3
														.getString("city");
												receiptNumber = resultSet3
														.getString("generated_id");
												receiptTime = resultSet3
														.getTimestamp("transaction_date");
												agtOrgNAme = resultSet3
														.getString("name");
											}

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Paid to Player "
															+ playerFirstName
															+ " "
															+ playerLastName
															+ " "
															+ playercity
															+ " By Agent: "
															+ agtOrgNAme
															+ " In Claimable Balance on Voucher Number: "
															+ receiptNumber
															+ " at "
															+ receiptTime);
											// pwtBean.setMessage("Paid to
											// Player By
											// Agent In Claimable Balance");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
											System.out
													.println("---------inside claim_agt_ auto--------------");
											String orgname = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement4 = connection
													.createStatement();
											// String agtDetailsQuery="select
											// name from
											// st_lms_organization_master where
											// organization_id in (select
											// agent_org_id
											// from st_se_bo_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";

											String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
													+ enVirnCode
													+ "' and game_id="
													+ gameId
													+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";

											System.out
													.println("query for get org name "
															+ partyDetailsQuery);
											resultSet4 = statement4
													.executeQuery(partyDetailsQuery);
											while (resultSet4.next()) {
												orgname = resultSet4
														.getString("name");
												receiptNumber = resultSet4
														.getString("receipt_id");
												receiptTime = resultSet4
														.getTimestamp("date_entered");
											}
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at BO for agent: "
															+ orgname
															+ " on Bulk Receipt Number: "
															+ receiptNumber
															+ " at "
															+ receiptTime
															+ ", Final Payment Pending");
											pwtBean.setMessageCode("112001");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {

											String orgname = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement4 = connection
													.createStatement();
											// String agtDetailsQuery="select
											// name from
											// st_lms_organization_master where
											// organization_id in (select
											// agent_org_id
											// from st_se_bo_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";

											String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
													+ enVirnCode
													+ "' and game_id="
													+ gameId
													+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";

											System.out
													.println("query for get org name "
															+ partyDetailsQuery);
											resultSet4 = statement4
													.executeQuery(partyDetailsQuery);
											while (resultSet4.next()) {
												orgname = resultSet4
														.getString("name");
												receiptNumber = resultSet4
														.getString("receipt_id");
												receiptTime = resultSet4
														.getTimestamp("date_entered");
											}
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at BO for agent: "
															+ orgname
															+ " on Bulk Receipt Number: "
															+ receiptNumber
															+ " at "
															+ receiptTime
															+ ", Final Payment Pending");
											pwtBean.setMessageCode("112001");

											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
											String orgname = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement4 = connection
													.createStatement();
											// String agtDetailsQuery="select
											// name from
											// st_lms_organization_master where
											// organization_id in (select
											// agent_org_id
											// from st_se_bo_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";

											String partyDetailsQuery = "select a.receipt_id,a.date_entered ,b.name from st_se_tmp_pwt_inv a,st_lms_organization_master b  where virn_code='"
													+ enVirnCode
													+ "' and game_id="
													+ gameId
													+ " and organization_id=(select organization_id from st_lms_user_master where user_id=a.user_id)";

											System.out
													.println("query for get org name "
															+ partyDetailsQuery);
											resultSet4 = statement4
													.executeQuery(partyDetailsQuery);
											while (resultSet4.next()) {
												orgname = resultSet4
														.getString("name");
												receiptNumber = resultSet4
														.getString("receipt_id");
												receiptTime = resultSet4
														.getTimestamp("date_entered");
											}
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at BO for agent: "
															+ orgname
															+ " on Bulk Receipt Number: "
															+ receiptNumber
															+ " at "
															+ receiptTime
															+ ", Final Payment Pending");
											pwtBean.setMessageCode("112001");

											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_AGT")) {
											System.out
													.println("---------inside alaim_agt--------------");
											String orgname = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											// String agtDetailsQuery="select
											// name from
											// st_lms_organization_master where
											// organization_id in (select
											// agent_org_id
											// from st_se_bo_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";

											String agtDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_bo_pwt b,st_lms_bo_receipts c,st_lms_bo_transaction_master e where b.virn_code='"
													+ enVirnCode
													+ "' and b.game_id="
													+ gameId
													+ " and a.organization_id=b.agent_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=e.transaction_id)";

											System.out
													.println("query for get org name "
															+ agtDetailsQuery);
											resultSet2 = statement2
													.executeQuery(agtDetailsQuery);
											while (resultSet2.next()) {
												orgname = resultSet2
														.getString("name");
												receiptNumber = resultSet2
														.getString("generated_id");
												receiptTime = resultSet2
														.getTimestamp("transaction_date");
											}
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Paid to Agent: "
															+ orgname
															+ " on Voucher Number: "
															+ receiptNumber
															+ " at "
															+ receiptTime);
											pwtBean.setMessageCode("112003");

											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
											String orgname = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											// String agtDetailsQuery="select
											// name from
											// st_lms_organization_master where
											// organization_id in (select
											// agent_org_id
											// from st_se_bo_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";

											String agtDetailsQuery = "select a.name,c.generated_id,e.transaction_date from st_lms_organization_master a,st_se_bo_pwt b,st_lms_bo_receipts c,st_lms_bo_transaction_master e where b.virn_code='"
													+ enVirnCode
													+ "' and b.game_id="
													+ gameId
													+ " and a.organization_id=b.agent_org_id and b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=e.transaction_id)";

											System.out
													.println("query for get org name "
															+ agtDetailsQuery);
											resultSet2 = statement2
													.executeQuery(agtDetailsQuery);
											while (resultSet2.next()) {
												orgname = resultSet2
														.getString("name");
												receiptNumber = resultSet2
														.getString("generated_id");
												receiptTime = resultSet2
														.getTimestamp("transaction_date");
											}
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Paid As Auto Scrap to Agent: "
															+ orgname
															+ " on Voucher Number: "
															+ receiptNumber
															+ " at "
															+ receiptTime);
											pwtBean.setMessageCode("112003");

											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_BO")) {
											String playerFirstName = null;
											String playerLastName = null;
											String playercity = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement3 = connection
													.createStatement();
											// String plrDetailsQuery="select
											// first_name,last_name,city from
											// st_lms_player_master where
											// player_id in
											// (select player_id from
											// st_se_direct_player_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";
											String plrDetailsQuery = "select b.first_name,b.last_name,b.city,a.transaction_date,c.generated_id from st_se_direct_player_pwt a,st_lms_player_master b,st_lms_bo_receipts c where a.virn_code='"
													+ enVirnCode
													+ "' and a.game_id="
													+ gameId
													+ " and a.player_id=b.player_id and c.receipt_id=(select receipt_id from st_lms_bo_receipts_trn_mapping where transaction_id=a.transaction_id)";
											System.out
													.println("query for get player name "
															+ plrDetailsQuery);
											resultSet3 = statement3
													.executeQuery(plrDetailsQuery);
											while (resultSet3.next()) {
												playerFirstName = resultSet3
														.getString("first_name");
												playerLastName = resultSet3
														.getString("last_name");
												playercity = resultSet3
														.getString("city");
												receiptNumber = resultSet3
														.getString("generated_id");
												receiptTime = resultSet3
														.getTimestamp("transaction_date");
											}
											pwtBean.setValid(false);
											pwtBean
													.setMessage("Already Paid By BO as Direct Player PWT to Player: "
															+ playerFirstName
															+ " "
															+ playerLastName
															+ ","
															+ playercity
															+ " on Voucher Number "
															+ receiptNumber
															+ " at "
															+ receiptTime);
											pwtBean.setMessageCode("112005");
											pwtBean
													.setVerificationStatus("InValid Virn");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
											// show info of retailer if
											// available from
											// at retailer PWT
											String orgname = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											// String agtDetailsQuery="select
											// name from
											// st_lms_organization_master where
											// organization_id in (select
											// agent_org_id
											// from st_se_bo_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";

											// String agtDetailsQuery="select
											// a.name,c.generated_id,e.transaction_date
											// from st_lms_organization_master
											// a,st_se_bo_pwt
											// b,st_lms_bo_receipts
											// c,st_lms_bo_transaction_master e
											// where
											// b.virn_code='"+enVirnCode+"' and
											// b.game_id="+gameId+" and
											// a.organization_id=b.agent_org_id
											// and
											// b.transaction_id=e.transaction_id
											// and
											// c.receipt_id=(select receipt_id
											// from
											// st_lms_bo_receipts_trn_mapping
											// where
											// transaction_id=e.transaction_id)";
											String retDetailsQuery = "select a.name,e.transaction_date from st_lms_organization_master a,st_se_retailer_pwt b,st_lms_retailer_transaction_master e where b.virn_code='"
													+ enVirnCode
													+ "' and b.game_id="
													+ gameId
													+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id";

											System.out
													.println("query for get org name "
															+ retDetailsQuery);
											resultSet2 = statement2
													.executeQuery(retDetailsQuery);
											while (resultSet2.next()) {
												orgname = resultSet2
														.getString("name");
												receiptTime = resultSet2
														.getTimestamp("transaction_date");
											}

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN No. has been paid to Player by retailer:"
															+ orgname
															+ " on Voucher "
															+ receiptTime
															+ " but not claimed by retailer to agent ");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
											// show info of retailer if
											// available from
											// at retailer PWT

											String orgname = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											// String agtDetailsQuery="select
											// name from
											// st_lms_organization_master where
											// organization_id in (select
											// agent_org_id
											// from st_se_bo_pwt where
											// virn_code='"+enVirnCode+"' and
											// game_id="+gameId+")";

											// String agtDetailsQuery="select
											// a.name,c.generated_id,e.transaction_date
											// from st_lms_organization_master
											// a,st_se_bo_pwt
											// b,st_lms_bo_receipts
											// c,st_lms_bo_transaction_master e
											// where
											// b.virn_code='"+enVirnCode+"' and
											// b.game_id="+gameId+" and
											// a.organization_id=b.agent_org_id
											// and
											// b.transaction_id=e.transaction_id
											// and
											// c.receipt_id=(select receipt_id
											// from
											// st_lms_bo_receipts_trn_mapping
											// where
											// transaction_id=e.transaction_id)";
											String retDetailsQuery = "select a.name,e.transaction_date from st_lms_organization_master a,st_se_retailer_pwt b,st_lms_retailer_transaction_master e where b.virn_code='"
													+ enVirnCode
													+ "' and b.game_id="
													+ gameId
													+ " and a.organization_id=b.retailer_org_id and b.transaction_id=e.transaction_id";

											System.out
													.println("query for get org name "
															+ retDetailsQuery);
											resultSet2 = statement2
													.executeQuery(retDetailsQuery);
											while (resultSet2.next()) {
												orgname = resultSet2
														.getString("name");
												receiptTime = resultSet2
														.getTimestamp("transaction_date");
											}

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN No. has been paid to Player by retailer:"
															+ orgname
															+ " on Voucher "
															+ receiptTime
															+ "  As Claimmable ");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
											// show info of retailer if
											// available from
											// at retailer direct player
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN No. has been paid to Player by retailer but not claimed by retailer to agent ");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
											// show info of retailer if
											// available from
											// at retailer derect palyer PWT

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN No. has been paid to Player by retailer As Claimmable ");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_TEMP")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at Agent,Final Payment Pending");
											pwtBean.setMessageCode("112006");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_CLM")) {
											String agtOrgname = null;
											String retOrgName = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											String agtDetailsQuery = "select  b.transaction_date,c.generated_id,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'agt_name' ,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'ret_name'  from st_se_agent_pwt a,st_lms_agent_transaction_master b,st_lms_agent_receipts c where a.virn_code='"
													+ enVirnCode
													+ "' and a.game_id="
													+ gameId
													+ " and a.transaction_id=b.transaction_id  and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=b.transaction_id)";

											System.out
													.println("query for get org name "
															+ agtDetailsQuery);
											resultSet2 = statement2
													.executeQuery(agtDetailsQuery);
											while (resultSet2.next()) {
												agtOrgname = resultSet2
														.getString("agt_name");
												retOrgName = resultSet2
														.getString("ret_name");
												receiptNumber = resultSet2
														.getString("generated_id");
												receiptTime = resultSet2
														.getTimestamp("transaction_date");
											}
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Paid to retailer: "
															+ retOrgName
															+ " By Agent: "
															+ agtOrgname
															+ " Voucher Number:"
															+ receiptNumber
															+ " on "
															+ receiptTime
															+ " and pending to claim at bo by agent as uato sarap");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
											// show agent and retailers name
											String agtOrgname = null;
											String retOrgName = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											String agtDetailsQuery = "select  b.transaction_date,c.generated_id,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'agt_name' ,(select name from st_lms_organization_master where organization_id=a.retailer_org_id) 'ret_name'  from st_se_agent_pwt a,st_lms_agent_transaction_master b,st_lms_agent_receipts c where a.virn_code='"
													+ enVirnCode
													+ "' and a.game_id="
													+ gameId
													+ " and a.transaction_id=b.transaction_id  and c.receipt_id=(select receipt_id from st_lms_agent_receipts_trn_mapping where transaction_id=b.transaction_id)";

											System.out
													.println("query for get org name "
															+ agtDetailsQuery);
											resultSet2 = statement2
													.executeQuery(agtDetailsQuery);
											while (resultSet2.next()) {
												agtOrgname = resultSet2
														.getString("agt_name");
												retOrgName = resultSet2
														.getString("ret_name");
												receiptNumber = resultSet2
														.getString("generated_id");
												receiptTime = resultSet2
														.getTimestamp("transaction_date");
											}
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Paid to retailer: "
															+ retOrgName
															+ " By Agent: "
															+ agtOrgname
															+ " Voucher Number:"
															+ receiptNumber
															+ " on "
															+ receiptTime
															+ " As Auto Scrap and pending to claim at bo as AUTO Scrap");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at Agent,Final Payment Pending");
											pwtBean.setMessageCode("112007");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("REQUESTED")) {
											// show request details from request
											// table
											// with history

											String reqByOrgName = null;
											String reqToOrgName = null;
											String reqByType = null;
											String reqToType = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											String reqDetailsQuery = "select a.requester_type,a.requested_to_type,a.request_id,a.request_date,(select name from st_lms_organization_master where organization_id=a.requested_by_org_id) as reqByName,(select name from st_lms_organization_master where organization_id=a.requested_to_org_id) as reqToName from st_se_pwt_approval_request_master a where a.virn_code='"
													+ enVirnCode
													+ "' and a.game_id="
													+ gameId
													+ " and a.req_status='REQUESTED'";

											System.out
													.println("query for get org name "
															+ reqDetailsQuery);
											resultSet2 = statement2
													.executeQuery(reqDetailsQuery);
											while (resultSet2.next()) {
												reqByOrgName = resultSet2
														.getString("reqByName");
												reqToOrgName = resultSet2
														.getString("reqToName");
												reqByType = resultSet2
														.getString("requester_type");
												reqToType = resultSet2
														.getString("requested_to_type");
												receiptNumber = resultSet2
														.getString("request_id");
												receiptTime = resultSet2
														.getTimestamp("request_date");
											}

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN is requested by "
															+ reqByType
															+ ": "
															+ reqByOrgName
															+ "To "
															+ reqToType
															+ ": "
															+ reqToOrgName
															+ " for Approval with Voucher id: "
															+ receiptNumber
															+ " on "
															+ receiptTime);
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("PND_MAS")) {
											// details of by and to and date and
											// voucher

											String reqByOrgName = null;
											String reqToOrgName = null;
											String reqByType = null;
											String reqToType = null;
											String receiptNumber = null;
											Timestamp receiptTime = null;
											statement2 = connection
													.createStatement();
											String reqDetailsQuery = "select a.requester_type,a.requested_to_type,a.request_id,a.request_date,(select name from st_lms_organization_master where organization_id=a.requested_by_org_id) as reqByName,(select name from st_lms_organization_master where organization_id=a.requested_to_org_id) as reqToName from st_se_pwt_approval_request_master a where a.virn_code='"
													+ enVirnCode
													+ "' and a.game_id="
													+ gameId
													+ " and a.req_status='PND_MAS'";

											System.out
													.println("query for get org name "
															+ reqDetailsQuery);
											resultSet2 = statement2
													.executeQuery(reqDetailsQuery);
											while (resultSet2.next()) {
												reqByOrgName = resultSet2
														.getString("reqByName");
												reqToOrgName = resultSet2
														.getString("reqToName");
												reqByType = resultSet2
														.getString("requester_type");
												reqToType = resultSet2
														.getString("requested_to_type");
												receiptNumber = resultSet2
														.getString("request_id");
												receiptTime = resultSet2
														.getTimestamp("request_date");
											}

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN is requested by "
															+ reqByType
															+ ": "
															+ reqByOrgName
															+ "To "
															+ reqToType
															+ ": "
															+ reqToOrgName
															+ " for master Approval with Voucher id: "
															+ receiptNumber
															+ " on "
															+ receiptTime);
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("PND_PAY")) {
											// show details for payment by and
											// to

											String reqToOrgName = null;
											String reqToType = null;
											String receiptNumber = null;
											statement2 = connection
													.createStatement();
											String reqDetailsQuery = "select a.request_id,a.pay_req_for_org_type,(select name from st_lms_organization_master where organization_id=a.pay_request_for_org_id) as payByName from st_se_pwt_approval_request_master a where a.virn_code='"
													+ enVirnCode
													+ "' and a.game_id="
													+ gameId
													+ " and a.req_status='PND_PAY'";

											System.out
													.println("query for get org name "
															+ reqDetailsQuery);
											resultSet2 = statement2
													.executeQuery(reqDetailsQuery);
											while (resultSet2.next()) {

												reqToOrgName = resultSet2
														.getString("payByName");
												reqToType = resultSet2
														.getString("pay_req_for_org_type");
												receiptNumber = resultSet2
														.getString("request_id");
											}

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN is requested To "
															+ reqToType
															+ ": "
															+ reqToOrgName
															+ "for payment with Voucher id: "
															+ receiptNumber);
											pwtBean.setMessageCode("112009");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CANCELLED_PERMANENT")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Tampered/Damaged/Defaced VIRN as noted at BO");
											pwtBean.setMessageCode("112009");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("NO_PRIZE_PWT")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Non Winning PWT");
											pwtBean.setMessageCode("112009");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("MISSING")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("VIRN is from MISSING Status");
											pwtBean.setMessageCode("");

										} else {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("UNDEFINED STATUS OF PWT:: "
															+ prizeStatus);
											pwtBean.setMessageCode("TBD");
											break;
										}
										
									}else{
										pwtBean.setValid(false);
										pwtBean.setVerificationStatus("InValid Virn");
										pwtBean	.setMessage("Invalid Virn");
										pwtBean.setMessageCode("TBD");
										break;
									}		
									} else {
                                        
										
										pwtBean.setValid(false);
										pwtBean.setVerificationStatus("InValid Ticket");
										pwtBean.setMessage("Ticket Number is not correct");
										pwtBean.setTicketValid(false);
										pwtBean.setMessageCode("TBD");
										break;
										
										/*pwtBean.setValid(false);
										pwtBean
												.setVerificationStatus("InValid Virn");
										pwtBean
												.setMessage("Ticket and Virn mapping is not correct");
										pwtBean.setTicketValid(false);
										pwtBean.setMessageCode("TBD");
										break;*/
									}
									
									
									
								} else {
									System.out.println("Yogesh Ticket is not valid @ BO : "	+ pwtBean.getTicketNumber());
									// Ticket is not valid
									pwtBean.setValid(false);
									pwtBean.setVerificationStatus("InValid Virn");
									pwtBean.setMessage("Ticket is not valid and virn not verified");
									pwtBean.setMessageCode("TBD");
									break;
								}
								
							}
							
							
							

						}

					}

				} catch (SQLException e) {

					e.printStackTrace();
					throw new LMSException(e);
				} finally {

					try {

						if (statement != null) {
							statement.close();
						}
						/*
						 * if (connection != null) { connection.close(); }
						 */
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}

			}
		}

		return isVerified;

	}

}
