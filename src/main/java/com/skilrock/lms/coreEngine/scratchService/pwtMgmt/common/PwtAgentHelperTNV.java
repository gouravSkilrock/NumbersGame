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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PlayerPWTBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This is a helper class providing methods for handing PWT at Agent end
 * 
 * @author Skilrock Technologies
 * 
 */
public class PwtAgentHelperTNV {

	/*
	 * private List<Integer> savePwtTickets(List<PwtBean> pwtList, int gameId,
	 * int agentUserId, int agtOrgId, int retOrgId, int retUserId, double
	 * retPwtCommRate, double agtPwtCommRate, String rootPath, String
	 * loggedInUserOrgName,int gameNbr,OrgPwtLimitBean orgPwtLimit,Connection
	 * connection) throws LMSException {
	 * 
	 * int userOrgID=agtOrgId; List<Integer> transIdList=new ArrayList<Integer>();
	 * //Connection connection = null; PreparedStatement masterPstmt = null;
	 * PreparedStatement LMSMasterPstmt = null; PreparedStatement detailPstmt =
	 * null; PreparedStatement invPstmt = null;
	 * 
	 * PreparedStatement boMasterPstmt = null; PreparedStatement boDetailPstmt =
	 * null; PreparedStatement boInvPstmt = null; ResultSet resultSet = null;
	 * int trnId = -1; int boTrnId = -1;
	 * 
	 * //int agtReceiptId = -1; int boReceiptId = -1;
	 * 
	 * //int agtOrgId = -1;
	 * 
	 * if (pwtList != null) { int size = pwtList.size(); QueryManager
	 * queryManager = null;
	 * 
	 * PwtBean pwtBean = null;
	 * 
	 * String masterQuery = null; String LMSMasterQuery = null; String
	 * detailQuery = null; String invQuery = null;
	 * 
	 * String boMasterQuery = null; String boDetailQuery = null; String
	 * boInvQuery = null;
	 * 
	 * 
	 * 
	 * double pwtAmt = 0.0; double commAmt = 0.0; double agtNetAmt = 0.0; double
	 * retNetAmt = 0.0; double agtCreditAmt = 0.0; double retCreditAmt = 0.0;
	 * double agtClmAmt =0.0; double agtUnClmAmt =0.0;
	 * 
	 * if (size > 0) { try {
	 * 
	 * 
	 * boolean isBOAutoUpdation = false;
	 * 
	 * //code changed to get the auto scrap details from
	 * st_lms_organization_master PreparedStatement autoScrapDetailPstmt = null;
	 * String orgScrapDetails="select pwt_scrap,pwt_scrap_limit from
	 * st_lms_organization_master where organization_id=?";
	 * autoScrapDetailPstmt=connection.prepareStatement(orgScrapDetails);
	 * autoScrapDetailPstmt.setInt(1,agtOrgId); ResultSet
	 * scrapDetails=autoScrapDetailPstmt.executeQuery(); String
	 * autoPWTUpdate=null; double autScrapLimit=0.0; while(scrapDetails.next()){
	 * autoPWTUpdate=scrapDetails.getString("pwt_scrap");
	 * autScrapLimit=scrapDetails.getDouble("pwt_scrap_limit"); }
	 * 
	 * 
	 * String autoPWTUpdate = (String)
	 * ServletActionContext.getServletContext().getAttribute("BO_AUTO_PWT_UPDATE");
	 * 
	 * 
	 * if("YES".equals(orgPwtLimit.getIsPwtAutoScrap())){ isBOAutoUpdation =
	 * true; }
	 * 
	 * //boolean isBOAutoUpdation = ConfigurationVariables.BO_AUTO_PWT_UPDATION;
	 * 
	 * masterQuery = QueryManager.insertInAgentTransactionMaster(); masterPstmt =
	 * connection.prepareStatement(masterQuery);
	 * 
	 * LMSMasterQuery = QueryManager.insertInLMSTransactionMaster();
	 * LMSMasterPstmt = connection.prepareStatement(LMSMasterQuery);
	 * 
	 * detailQuery = QueryManager.getST1PwtAgentDetailQuery(); detailPstmt =
	 * connection.prepareStatement(detailQuery);
	 * 
	 * invQuery = QueryManager.getST1PWTUpdateQuery(); invPstmt =
	 * connection.prepareStatement(invQuery);
	 * 
	 * //insert into LMS transaction master LMSMasterPstmt.setString(1,"AGENT");
	 * LMSMasterPstmt.executeUpdate();
	 * 
	 * resultSet = LMSMasterPstmt.getGeneratedKeys();
	 * 
	 * if (resultSet.next()) { trnId = resultSet.getInt(1);
	 * 
	 * //insert into agent transaction master
	 * 
	 * masterPstmt.setInt(1,trnId); masterPstmt.setInt(2,agentUserId);
	 * masterPstmt.setInt(3,agtOrgId); masterPstmt.setString(4,"RETAILER");
	 * masterPstmt.setInt(5,retOrgId); masterPstmt.setString(6,"PWT");
	 * masterPstmt.setTimestamp(7,new java.sql.Timestamp(new Date().getTime()));
	 * masterPstmt.execute();
	 * 
	 * System.out.println("OrderId::" + trnId);
	 * 
	 * for (int i = 0; i < size; i++) { pwtBean = (PwtBean) pwtList.get(i);
	 * 
	 * if (pwtBean.getIsValid()) {
	 * 
	 * String encodedVirn = MD5Encoder.encode(pwtBean .getVirnCode());
	 * 
	 * detailPstmt.setString(1, encodedVirn); detailPstmt.setInt(2, trnId);
	 * detailPstmt.setInt(3, gameId); detailPstmt.setInt(4, agentUserId);
	 * detailPstmt.setInt(5, retUserId); detailPstmt.setInt(6, retOrgId);
	 * 
	 * pwtAmt = Double.parseDouble(pwtBean.getPwtAmount()); commAmt = pwtAmt *
	 * retPwtCommRate * 0.01; retNetAmt = pwtAmt + commAmt; // for
	 * creditUpdation retCreditAmt += retNetAmt;
	 * 
	 * detailPstmt.setDouble(7, pwtAmt); detailPstmt.setDouble(8, commAmt);
	 * detailPstmt.setDouble(9, retNetAmt); detailPstmt.setInt(10,agtOrgId);
	 * detailPstmt.execute();
	 * 
	 * //String nextStatus = "";
	 * 
	 * if (isBOAutoUpdation && pwtAmt <= orgPwtLimit.getScrapLimit()) { commAmt =
	 * pwtAmt * agtPwtCommRate * 0.01; agtNetAmt = pwtAmt + commAmt; // for agt
	 * credit updation agtCreditAmt += agtNetAmt; agtClmAmt+= agtNetAmt; } else { //
	 * arun's update //update the remaining prize list
	 * GameUtilityHelper.updateNoOfPrizeRem(gameId, pwtAmt, "CLAIM_RET",
	 * encodedVirn, connection,gameNbr);
	 * 
	 * invPstmt.setInt(1,gameNbr); invPstmt.setInt(2, gameId);
	 * invPstmt.setString(3, encodedVirn); invPstmt.execute(); } } } // for
	 * retailer credit updation
	 * OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId, "PWT",
	 * retCreditAmt, connection); // for agent Credit Updation if
	 * (isBOAutoUpdation) {
	 * OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId, "PWT",
	 * agtCreditAmt, connection); } }} catch (SQLException e) { try {
	 * connection.rollback(); } catch (SQLException e1) { // TODO Auto-generated
	 * catch block e1.printStackTrace(); } e.printStackTrace(); throw new
	 * LMSException(e); } finally {
	 * 
	 * try { if (masterPstmt != null) { masterPstmt.close(); } if (detailPstmt !=
	 * null) { detailPstmt.close(); }
	 * 
	 * if (invPstmt != null) { invPstmt.close(); } } catch (SQLException se) {
	 * se.printStackTrace(); } } } } return transIdList; }
	 */
	public synchronized String generateReciptForPwt(List<Long> transIdList,
			Connection connection, int userOrgID, int partyId,
			String partyType, String recType) {
		int agtReceiptId = -1;
		String receipts = null;
		// int boReceiptId=-1;
		PreparedStatement agtReceiptPstmt = null;
		PreparedStatement agtReceiptMappingPstmt = null;
		String receiptType = null;

		try {
			// for generating receipt********************
			// insert in receipt master
			agtReceiptPstmt = connection.prepareStatement(QueryManager
					.insertInReceiptMaster());
			agtReceiptPstmt.setString(1, "AGENT");
			agtReceiptPstmt.executeUpdate();

			ResultSet agtRSet = agtReceiptPstmt.getGeneratedKeys();
			while (agtRSet.next()) {
				agtReceiptId = agtRSet.getInt(1);
				System.out.println("Receipt Id:" + agtReceiptId);
			}
			if ("DRAWGAME".equalsIgnoreCase(recType)) {
				receiptType = "DG_RECEIPT";
			} else {
				receiptType = "RECEIPT";
			}
			PreparedStatement autoGenRecptPstmt = null;
			autoGenRecptPstmt = connection.prepareStatement(QueryManager
					.getAGENTLatestReceiptNb());
			autoGenRecptPstmt.setString(1, receiptType);
			autoGenRecptPstmt.setInt(2, userOrgID);
			ResultSet recieptRs = autoGenRecptPstmt.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNoAgt = GenerateRecieptNo.getRecieptNoAgt(
					receiptType, lastRecieptNoGenerated, "AGENT", userOrgID);

			// insert in agent receipts
			// agtReceiptQuery = QueryManager.getST1AgtReceiptsQuery();
			agtReceiptPstmt = connection.prepareStatement(QueryManager
					.insertInAgentReceipts());
			agtReceiptPstmt.setInt(1, agtReceiptId);
			agtReceiptPstmt.setString(2, receiptType);
			agtReceiptPstmt.setInt(3, userOrgID);
			agtReceiptPstmt.setInt(4, partyId);
			agtReceiptPstmt.setString(5, partyType);
			agtReceiptPstmt.setString(6, autoGeneRecieptNoAgt);
			agtReceiptPstmt.setTimestamp(7, Util.getCurrentTimeStamp());
			agtReceiptPstmt.execute();

			// insert agetn receipt trn mapping

			// agtReceiptMappingQuery =
			// QueryManager.getST1AgtReceiptsMappingQuery();
			agtReceiptMappingPstmt = connection.prepareStatement(QueryManager
					.insertAgentReceiptTrnMapping());

			for (int i = 0; i < transIdList.size(); i++) {
				agtReceiptMappingPstmt.setInt(1, agtReceiptId);
				agtReceiptMappingPstmt.setLong(2, transIdList.get(i));
				agtReceiptMappingPstmt.execute();
			}
			receipts = agtReceiptId + "-" + autoGeneRecieptNoAgt;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return receipts;
	}

	/**
	 * This method returns a list of the active games
	 * 
	 * @return List
	 * @throws LMSException
	 */
	public List getActiveGames(int agtOrgId) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		String delimiter = new String("-");

		try {

			ActiveGameBean gameBean = null;
			List<ActiveGameBean> searchResults = new ArrayList<ActiveGameBean>();

			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();
			statement = connection.createStatement();
			statement1 = connection.createStatement();
			String query = QueryManager.getST1ActiveGamesQuery()
					+ " order by game_nbr";

			resultSet = statement.executeQuery(query);
			int gameNbr;
			String gameName;

			while (resultSet.next()) {
				int game_id = resultSet.getInt(TableConstants.SGM_GAME_ID);
				double agtPwtCommvariance = 0.0;
				gameBean = new ActiveGameBean();
				gameBean
						.setGameId(resultSet.getInt(TableConstants.SGM_GAME_ID));
				// get agt comm variance for each game
				resultSet1 = statement1
						.executeQuery("select pwt_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id="
								+ agtOrgId + " and game_id=" + game_id);
				while (resultSet1.next()) {
					agtPwtCommvariance = resultSet1
							.getDouble("pwt_comm_variance");
				}

				gameBean.setRetailerPwtCommRate(resultSet
						.getDouble(TableConstants.SGM_RET_PWT_RATE));
				gameBean.setAgentPwtCommRate(resultSet
						.getDouble(TableConstants.SGM_AGT_PWT_RATE)
						+ agtPwtCommvariance);
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

	private String getEncodedVirnCode(String[] virnCode) {

		StringBuffer encodedVirnCode = new StringBuffer("");

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

		StringBuffer encodedVirnCode = new StringBuffer("");

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


	/**
	 * This method returns a list of retailers for the passed agent
	 * 
	 * @param agtOrgId
	 * @return List
	 * @throws LMSException
	 */
	public List getRetailers(int agtOrgId) throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {

			OrgBean retailerOrgBean = null;
			List<OrgBean> searchResults = new ArrayList<OrgBean>();

			DBConnect dbConnect = new DBConnect();
			connection = dbConnect.getConnection();

			// String query = QueryManager.getST1RetOrgQuery();// +" order by
			// name";

			String query = "select so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit from st_lms_organization_master so,st_lms_user_master su  where so.organization_type = 'RETAILER' and (so.organization_status='ACTIVE' or so.organization_status='INACTIVE') and so.organization_id = su.organization_id  and so.parent_id = ?  order by name";

			statement = connection.prepareStatement(query);
			statement.setInt(1, agtOrgId);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				retailerOrgBean = new OrgBean();
				retailerOrgBean.setOrgId(resultSet
						.getInt(TableConstants.SOM_ORG_ID));
				retailerOrgBean.setOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
				retailerOrgBean.setUserId(resultSet
						.getInt(TableConstants.SUM_USER_ID));

				searchResults.add(retailerOrgBean);

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

	public Map<String, List<PwtBean>> savePwtTicketsDataNew(
			String[] gameNbr_Name, List<String> allVirnCode,
			List<ActiveGameBean> activeGameList, int retUserId, int retOrgId,
			double HighPrizeAmount, String highPrizeCriteria, int[] inpCount,
			int agentUserId, int agtOrgId, String rootPath,
			String loggedInUserOrgName, List<String> allTicketList)
			throws LMSException {
		// boolean saveTicketFlag=false;

		// List<Integer> transIdListAgt=new ArrayList<Integer>();
		// List<Integer> transIdListBO=new ArrayList<Integer>();
		List<Long> transIdList = new ArrayList<Long>();

		Map<String, List<PwtBean>> gameVirnMap = new HashMap<String, List<PwtBean>>();
		int gameId = -1;
		double retPwtCommRate = 0.0;
		double agtPwtCommRate = 0.0;
		ActiveGameBean activeGameBean = null;

		int startVirnCount = 0;
		int endVirnCount = 0;
		String[] gameVirnCode;
		String[] gameTicketNumber;

		Connection connection = null;
		DBConnect dbConnect = new DBConnect();
		connection = dbConnect.getConnection();
		try {
			connection.setAutoCommit(false);
			// get organizations limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(agtOrgId, connection);
			if (orgPwtLimit == null) { // send mail to backoffice
				throw new LMSException("PWT Limits Are Not defined Properly!!");
			}

			for (int i = 0; i < gameNbr_Name.length; i++) {
				System.out.println("Game Name--" + gameNbr_Name[i]);
				boolean isTickedVerified = false;
				;

				if (!gameNbr_Name[i].equals("-1")) {
					// we appending game id with gameId-gameNbr-Name on JSP
					if (activeGameList != null) {
						for (int k = 0; k < activeGameList.size(); k++) {
							activeGameBean = activeGameList.get(k);
							if (gameNbr_Name[i].equals(activeGameBean
									.getGameNbr_Name())) {
								gameId = activeGameBean.getGameId();
								retPwtCommRate = activeGameBean
										.getRetailerPwtCommRate();
								agtPwtCommRate = activeGameBean
										.getAgentPwtCommRate();
								break;

							}
						}
					}

					String[] gameNameNbrArr = gameNbr_Name[i].split("-");
					int gameNbr = Integer.parseInt(gameNameNbrArr[0]);

					int inputVirnCount = inpCount[i];
					if (inputVirnCount % 2 == 0) {
						inputVirnCount = inputVirnCount / 2;
					}
					endVirnCount = startVirnCount + inputVirnCount;
					int inc = 0;
					gameVirnCode = new String[endVirnCount - startVirnCount];
					gameTicketNumber = new String[endVirnCount - startVirnCount];
					List<PwtBean> pwtList = new ArrayList<PwtBean>();
					for (int j = startVirnCount; j < endVirnCount; j++) {
						PwtBean pwtBean = null;
						pwtBean = new PwtBean();
						pwtBean.setVirnCode(allVirnCode.get(j));
						pwtBean.setMessageCode("212013");
						pwtBean.setMessage("No PriZe");
						pwtBean.setVerificationStatus("InValid Virn");

						// add ticket entries also after the ticket verification
						// here -----by yogesh----
						String tktNbrArr[] = null;
						// check the format of these tickets
						CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
						AgentPwtProcessHelper agtPwtHelper = new AgentPwtProcessHelper();
						TicketBean ticketBean = new TicketBean();
						ticketBean = commHelper.isTicketFormatValid(
								allTicketList.get(j), gameId, connection);

						if (ticketBean.getIsValid()) {
							tktNbrArr = ticketBean.getTicketNumber().split("-");
							ticketBean = agtPwtHelper.checkTicketStatus(gameNbr
									+ "", tktNbrArr[0] + "-" + tktNbrArr[1],
									tktNbrArr[2], gameId, connection, false);
						}

						if (ticketBean.getIsValid()) {
							isTickedVerified = true;
						}
						pwtBean.setTicketValid(ticketBean.getIsValid());
						// pwtBean.setTicketNumber(allTicketList.get(j));
						pwtBean.setTicketNumber(ticketBean.getTicketNumber());
						pwtBean.setTicketMessage(ticketBean.getStatus());
						pwtBean.setTicketVerificationStatus(ticketBean
								.getValidity());
						pwtBean.setUpdateTicketType(ticketBean
								.getUpdateTicketType());
						// add ticket entries also after the ticket verification						

						pwtList.add(pwtBean);
						gameVirnCode[inc] = allVirnCode.get(j);
						//gameTicketNumber[inc] = allTicketList.get(j);
						gameTicketNumber[inc] = ticketBean.getTicketNumber();
						System.out.println(gameNbr_Name[i] + "*-*-*"
								+ gameVirnCode[inc]);
						inc++;
						startVirnCount++;
					}
					System.out.println("Game Name****" + gameNbr_Name[i]
							+ " Virn " + gameVirnCode.length);

					System.out.println("game name: " + gameNbr_Name[i]
							+ " :ticket numbers: " + gameTicketNumber.length
							+ " :virn numbers :" + gameVirnCode.length);

					if (gameVirnCode.length > 0 ) {

						/*
						 * commented by yogesh // we appending game id with
						 * gameId-gameNbr-Name on JSP if (activeGameList !=
						 * null) { for (int k = 0; k < activeGameList.size();
						 * k++) { activeGameBean = activeGameList.get(k); if
						 * (gameNbr_Name[i].equals(activeGameBean
						 * .getGameNbr_Name())) { gameId =
						 * activeGameBean.getGameId(); retPwtCommRate =
						 * activeGameBean .getRetailerPwtCommRate();
						 * agtPwtCommRate = activeGameBean
						 * .getAgentPwtCommRate(); break; } } }
						 * 
						 * String[] gameNameNbrArr = gameNbr_Name[i].split("-");
						 * int gameNbr = Integer.parseInt(gameNameNbrArr[0]);
						 * 
						 */

						/*
						 * boolean isVerified =verifyPwtTickets(gameId,
						 * gameVirnCode,
						 * pwtList,HighPrizeAmount,highPrizeCriteria,gameNbr);
						 * if(isVerified) saveTicketFlag=true;
						 */
						boolean isVerified = verifyPwtTickets(retOrgId,
								orgPwtLimit, gameId, gameVirnCode, pwtList,
								HighPrizeAmount, highPrizeCriteria, gameNbr,
								connection,gameTicketNumber);

						if (isVerified) {
							PlayerPWTBean requestDetailsBean = null;
							long transactionId = commonFunction
									.agtEndPWTPaymentProcess(pwtList, gameId,
											agentUserId, agtOrgId, retOrgId,
											retUserId, retPwtCommRate,
											agtPwtCommRate, gameNbr,
											orgPwtLimit, connection,
											"RETAILER", requestDetailsBean);

							if (transactionId > 0) {
								transIdList.add(transactionId);
							}

						}

						// update ticket status
						CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
						for (PwtBean pwtTicketBean : pwtList) {
							if (pwtTicketBean.isTicketValid()
									&& pwtTicketBean.getIsValid()) {
								// update ticket status into ticket inventory
								// table
								boolean isClaimmable = false;
								double pwtAmt = Double
										.parseDouble(pwtTicketBean
												.getPwtAmount());
								if ("YES".equalsIgnoreCase(orgPwtLimit
										.getIsPwtAutoScrap())
										&& pwtAmt <= orgPwtLimit
												.getScrapLimit()) {
									isClaimmable = true;
								} else {
									isClaimmable = false;
								}

								String tktNbrArr[] = pwtTicketBean
										.getTicketNumber().split("-");
								int updRow = commHelper.updateTicketInvTable(
										pwtTicketBean.getTicketNumber(),
										tktNbrArr[0] + "-" + tktNbrArr[1],
										gameNbr, gameId,
										(isClaimmable ? "CLAIM_RET_CLM"
												: "CLAIM_RET_UNCLM"),
										agentUserId, agtOrgId, pwtTicketBean
												.getUpdateTicketType(),
										retOrgId, "RETAIL", "WEB", connection);
								commHelper.updateBookStatus(gameId,
										tktNbrArr[0] + "-" + tktNbrArr[1],
										connection, "CLAIMED");
								/*
								 * if (updRow == 1) { tktBean .setStatus("Ticket
								 * Is Saved For PWT");
								 * tktBean.setValidity("Valid Ticket");
								 * saveTktBeanList.add(tktBean); }
								 */

							}
						}

						// ---update ticket status end

						if (gameVirnMap.containsKey(gameNbr_Name[i])) {
							List<PwtBean> oldPwtList = gameVirnMap
									.get(gameNbr_Name[i]);
							oldPwtList.addAll(pwtList);
							gameVirnMap.put(gameNbr_Name[i], oldPwtList);
						} else {
							gameVirnMap.put(gameNbr_Name[i], pwtList);
						}
						// amount = amount+getPwtAmount(pwtList);
					}
				}
			}

			// generate receipt for all games
			if (transIdList.size() > 0) {
				String agtReceipts = generateReciptForPwt(transIdList,
						connection, agtOrgId, retOrgId, "RETAILER",
						"SCRATCH_GAME");
				int agtReceiptId = Integer.parseInt(agtReceipts.split("-")[0]);
				connection.commit();
				System.out.println("transIdList for agent size is "
						+ transIdList.size() + ":: receipt number "
						+ agtReceiptId);
				if (agtReceiptId > 0) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportAgent(agtReceiptId,
							rootPath, agtOrgId, loggedInUserOrgName);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return gameVirnMap;
	}

	private boolean verifyPwtTickets(int retOrgId, OrgPwtLimitBean orgPwtLimit,
			int gameId, String[] virnCode, List<PwtBean> pwtList,
			double HighPrizeAmount, String highPrizeCriteria, int gameNbr,
			Connection connection,String[] tktNbr) throws LMSException {

		//String encodedVirnCode = getEncodedVirnCode(virnCode);
		String encodedVirnCode = getEncodedTktNbr(tktNbr);
		System.out.println("Encoded Ticket List::" + encodedVirnCode);
		Statement statement = null;
		boolean isVerified = false;
		ResultSet resultSet = null;		

		// here we can itarate all the games
		
		if (pwtList != null) {
			//int size = pwtList.size();
			// QueryManager queryManager = null;
			PwtBean pwtBean = null;
			StringBuffer query = new StringBuffer();
			if (pwtList.size() > 0) {
				try {
					// test PWT amount with limit process
					statement = connection.createStatement();

					query.append(QueryManager.getST1PWTBOCheckQuery());
					query.append(" st_se_pwt_inv_" + gameNbr + " where ");
					query.append(" game_id = ");
					query.append("" + gameId);
					query.append(" and id1 in (");
					query.append(encodedVirnCode);
					query.append(")");

					System.out.println("GameId:" + gameId);
					System.out.println("Query to get virn from DB :: " + query);
					resultSet = statement.executeQuery(query.toString());					

					String vCode = null;
					String pwtAmount = null;
					String enVirnCode = null;
					String prizeLevel = null;
					String prizeStatus = null;
					String tNumber = null;
					String enticketNumber = null;
					while (resultSet.next()) {

						vCode = resultSet
								.getString(TableConstants.SPI_VIRN_CODE);
						pwtAmount = resultSet
								.getString(TableConstants.SPI_PWT_AMT);
						prizeLevel = resultSet
								.getString(TableConstants.SPI_PRIZE_LEVEL);
						prizeStatus = resultSet.getString("status");
						tNumber = resultSet.getString("id1");
						System.out.println("Vcode : " + vCode + "\nPWT Amt : "
								+ pwtAmount + "\nPrize level : " + prizeLevel
								+ "\nstatus : " + prizeStatus + "\nticket :"
								+ tNumber);

						for (int j = 0; j < pwtList.size(); j++) {

							pwtBean = (PwtBean) pwtList.get(j);
							enVirnCode = MD5Encoder.encode(pwtBean
									.getVirnCode());
							enticketNumber = MD5Encoder.encode(pwtBean
									.getTicketNumber());
							if(enticketNumber.equalsIgnoreCase(tNumber)){
								
								if (pwtBean.isTicketValid()) {
									if (enVirnCode.equalsIgnoreCase(vCode)) {
										double pwtAmt = Double.parseDouble(pwtAmount);
										if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")) {
											if (pwtAmt <= orgPwtLimit.getVerificationLimit()) {
												if (highPrizeCriteria.equals("level")&& prizeLevel.equals("HIGH")|| highPrizeCriteria.equals("amt")&& Double.parseDouble(pwtAmount) > HighPrizeAmount) {
													System.out.println("inside high prize");
													System.out.println("criteria is "+ highPrizeCriteria);
													pwtBean.setHighLevel(true);
													pwtBean.setValid(false);
													pwtBean.setVerificationStatus("InValid Virn");
													pwtBean.setMessage("High prize VIRN can't be Paid to Retailer.It is to be paid as Direct Player PWT at BO");
													pwtBean.setMessageCode("212009");
												} else if (pwtAmt > orgPwtLimit
														.getApprovalLimit()) {
													System.out.println("inside approval request so return ticket");
													pwtBean.setValid(false);
													pwtBean.setVerificationStatus("InValid Virn");
													pwtBean.setMessage("Approval required from BO so return ticket");
													pwtBean.setMessageCode("Undefined");
													pwtBean.setAppReq(pwtAmt > orgPwtLimit
																	.getApprovalLimit());
												} else if (pwtAmt <= orgPwtLimit
														.getPayLimit()) {
													isVerified = true;
													pwtBean.setValid(true);
													pwtBean.setEncVirnCode(enVirnCode);
													pwtBean.setVerificationStatus("Valid Virn");
													pwtBean.setPwtAmount(pwtAmount);
													pwtBean.setMessage("Credited to Concerned Party");
													pwtBean.setMessageCode("211001");
												} else {
													pwtBean.setValid(false);
													pwtBean
															.setVerificationStatus("InValid Virn");
													pwtBean
															.setPwtAmount(pwtAmount);
													pwtBean
															.setMessage("this virn is not in this agents pay limit");
													pwtBean
															.setMessageCode("211001");
												}
												break;
											} else { // Out of Range of
												// Retailer
												// Verification Limit.
												System.out
														.println("Out of Range of Retailer Verification Limit.");
												pwtBean.setValid(false);
												pwtBean
														.setVerificationStatus("InValid Virn");
												pwtBean
														.setMessage("Out of Range of Agents Verification Limit.");
												pwtBean
														.setMessageCode("Undefined");
											}
										} else if (prizeStatus
												.equalsIgnoreCase("NO_PRIZE_PWT")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("Valid Virn but Non winning");
											pwtBean.setMessage("Non winning");
											pwtBean.setMessageCode("212001");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("UNCLM_CANCELLED")) {
											if (pwtAmt <= orgPwtLimit
													.getVerificationLimit()) {
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
															.setMessage("High prize VIRN can't be Paid to Retailer.It is to be paid as Direct Player PWT at BO");
													pwtBean
															.setMessageCode("212012");
												} else if (pwtAmt > orgPwtLimit
														.getApprovalLimit()) {
													System.out
															.println("inside approval request so return ticket");
													pwtBean.setValid(false);
													pwtBean
															.setVerificationStatus("Valid Virn");
													pwtBean
															.setMessage("Approval required from BO");
													pwtBean
															.setMessageCode("Undefined");
													pwtBean
															.setAppReq(pwtAmt > orgPwtLimit
																	.getApprovalLimit());
												} else if (pwtAmt <= orgPwtLimit
														.getPayLimit()) {
													isVerified = true;
													pwtBean.setValid(true);
													pwtBean
															.setEncVirnCode(enVirnCode);
													pwtBean
															.setVerificationStatus("Valid Virn");
													pwtBean
															.setPwtAmount(pwtAmount);
													pwtBean
															.setMessage("Credited to Concerned party");
													pwtBean
															.setMessageCode("211002");
												} else {

													pwtBean.setValid(false);
													pwtBean
															.setVerificationStatus("InValid Virn");
													pwtBean
															.setPwtAmount(pwtAmount);
													pwtBean
															.setMessage("this virn is not in this agents pay limit");
													pwtBean
															.setMessageCode("211002");
												}
												break;
											} else { // Out of Range of
												// Retailer
												// Verification Limit.
												System.out
														.println("Out of Range of Retailer Verification Limit.");
												pwtBean.setValid(false);
												pwtBean
														.setVerificationStatus("InValid Virn");
												pwtBean
														.setMessage("Out of Range of Retailer Verification Limit.");
												pwtBean
														.setMessageCode("Undefined");
											}

										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
											CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
											String flag = commHelper
													.verifyOrgForUnClaimedVirn(
															retOrgId,
															"RETAILER",
															enVirnCode,
															"UNCLAIM_BAL",
															gameId, connection);
											if (!"NONE".equalsIgnoreCase(flag)) {
												isVerified = true;
												pwtBean.setValid(true);
												pwtBean
														.setEncVirnCode(enVirnCode);
												pwtBean
														.setVerificationStatus("Valid Virn");
												pwtBean.setPwtAmount(pwtAmount);
												pwtBean
														.setMessage("Credited to Concerned party");
												pwtBean
														.setMessageCode("211002");
												pwtBean.setInUnclmed(flag);
											} else {
												pwtBean.setValid(false);
												pwtBean
														.setVerificationStatus("InValid Virn");
												pwtBean
														.setMessage("VIRN To be Claimed by Another Retailer.");
												pwtBean
														.setMessageCode("Unknown");
											}
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("In Retailer Claimable Balance");
											pwtBean.setMessageCode("212001");
											break;

										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already paid as Direct Player PWT ");
											pwtBean.setMessageCode("212001");
											break;

										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_TEMP")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");
											pwtBean.setMessageCode("212002");
											break;

										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at Agent, Fianl Payment Pending");
											pwtBean.setMessageCode("212003");
											break;

										} else if (prizeStatus
												.equalsIgnoreCase("MISSING")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("VIRN is from MISSING Status");
											pwtBean.setMessageCode("");

										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_AGT_TEMP")) {

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
											pwtBean.setMessageCode("212004");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
											pwtBean.setMessageCode("212005");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_AGT")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Paid to an agent ");
											pwtBean.setMessageCode("212006");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET")) {

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Paid to retailer");
											pwtBean.setMessageCode("212007");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_TEMP")) {

											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already in Process for Direct Player PWT");
											pwtBean.setMessageCode("212008");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_RET")) {
											isVerified = true;
											pwtBean.setValid(true);
											pwtBean.setEncVirnCode(enVirnCode);
											pwtBean
													.setVerificationStatus("Valid Virn");
											pwtBean.setPwtAmount(pwtAmount);
											pwtBean
													.setMessage("This VIRN No. is Valid To Pay to retailer");
											pwtBean.setMessageCode("212002");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("REQUESTED")) {
											// show request details from request
											// table
											// with history
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN has Beean Already claimed and requested for Approval");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("PND_MAS")) {
											// details of by and to and date and
											// voucher
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN has Beean requested for BO Master Approval");
											pwtBean.setMessageCode("112009");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("PND_PAY")) {
											// show details for payment by and
											// to
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("This VIRN has Beean Approved And Pending to Payment");
											pwtBean.setMessageCode("112009");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Paid As Auto Scrap to Agent");
											pwtBean.setMessageCode("112003");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_BO")) {
											pwtBean.setValid(false);
											pwtBean
													.setMessage("Already Paid By BO as Direct Player PWT to Player");
											pwtBean.setMessageCode("112005");
											pwtBean
													.setVerificationStatus("InValid Virn");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Paid to Player By Agent");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Paid to Player By Agent In Claimable Balance");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Already Verified in Bulk Receipt at BO, Fianl Payment Pending");
											pwtBean.setMessageCode("112002");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_CLM")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Paid to retailer By Agent and pending to claim at bo by agent as uato sarap");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Paid to retailer As Auto Scrap and pending to claim at bo as AUTO Scrap");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("VIRN Already Claimed To Retailer");
											pwtBean.setMessageCode("TBD");
											break;
										} else if (prizeStatus
												.equalsIgnoreCase("CANCELLED_PERMANENT")) {
											pwtBean.setValid(false);
											pwtBean
													.setVerificationStatus("InValid Virn");
											pwtBean
													.setMessage("Tampered/Damaged/Defaced VIRN as noted at BO");
											pwtBean.setMessageCode("212010");
											break;
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

									} else {
										
										
										System.out.println("Virn is not valid : "+ pwtBean.getTicketNumber());
										// Ticket is not valid
										pwtBean.setValid(false);
										pwtBean.setVerificationStatus("InValid Virn");
										pwtBean.setMessage("Ticket is valid but virn not valid");
										pwtBean.setMessageCode("TBD");
										break;
										
										/*// Ticket and virn mapping is not
										// correct
										pwtBean.setValid(false);
										pwtBean
												.setVerificationStatus("Ticket Virn Mapping not ok");
										pwtBean
												.setMessage("Ticket and Virn mapping is ot correct");
										pwtBean.setMessageCode("TBD");

										pwtBean.setTicketValid(false);
										break;*/

									}
								} else {
									System.out.println("Ticket is not valid : "+ pwtBean.getTicketNumber());
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
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}

			}
		}

		return isVerified;

	}

	/**
	 * This method returns the sum of PWT amount for the valid entries
	 * 
	 * @param pwtList
	 * @return double
	 */
	/*
	 * public double getPwtAmount(List<PwtBean> pwtList) {
	 * 
	 * double amount = 0.0;
	 * 
	 * if (pwtList != null) { for (PwtBean pwtBean : pwtList) { if
	 * (pwtBean.getIsValid()) { amount +=
	 * Double.parseDouble(pwtBean.getPwtAmount()); } } } return amount; }
	 */

}
