package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.TmpPwtReceiptBean;
import com.skilrock.lms.beans.TmpReceiptDetailBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

public class PwtVerfiyHelper {

	private static final DateFormat utilDateFormatter = new SimpleDateFormat(
			"dd-MM-yyyy");

	List<ActiveGameBean> activeGameList;

	// copied from pwt bo helper
	String receiptNum = null;

	// StringBuilder virn = null;
	Map virnMap = null;

	private Date add(Date date, int value) {

		Date newDate = null;
		try {
			java.util.Date utilDate = (java.util.Date) utilDateFormatter
					.parse(utilDateFormatter.format(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(utilDate);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			newDate = new Date(cal.getTime().getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Date after addition Date : " + newDate);
		return newDate;
	}

	public OrgBean agentOrgName(String receiptNum) {

		OrgBean agentOrgBean = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = "select so.organization_id,so.name,su.user_id from st_lms_organization_master so,st_lms_user_master su where so.organization_id = su.organization_id and  so.organization_id=(select agent_org_id from st_se_tmp_pwt_receipt where receipt_id='"
					+ receiptNum + "')";

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				agentOrgBean = new OrgBean();
				agentOrgBean.setOrgId(resultSet
						.getInt(TableConstants.SOM_ORG_ID));
				agentOrgBean.setOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
				agentOrgBean.setUserId(resultSet
						.getInt(TableConstants.SUM_USER_ID));

			}

			System.out.println("** " + query);

			return agentOrgBean;
		} catch (SQLException e) {
			System.out.println("In Generate Receipt Exception " + e);
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

	public void closeReceipt(String receiptNum, int userId, int userOrgID,
			List verifiedData, List<ActiveGameBean> activeGameList,
			Connection connection, String channel, String interfaceType) {
		System.out.println("inside close receipttssssssssssssssssssss ");
		// Connection connection = null;
		Statement statement = null;
		String game_nbr = null;
		ActiveGameBean activeGameBean = null;
		String insertPwtTktQry = null;
		String updPwtTktQry = null;
		String tktNbrStr = null;
		int game_id = 0;
		try {

			//  
			// connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String tmpTicketUpdateQuery = "update st_se_tmp_pwt_tickets_inv set status='CLOSE',final_receipt_num='NO RECEIPT' where receipt_id='"
					+ receiptNum + "' and final_receipt_num is null";
			String tmpReceiptUpdateQuery = "update st_se_tmp_pwt_receipt set status='CLOSE' where receipt_id='"
					+ receiptNum + "'";

			statement.executeUpdate(tmpTicketUpdateQuery);
			statement.executeUpdate(tmpReceiptUpdateQuery);
			tktNbrStr = ((List) verifiedData.get(0)).toString().replace("[",
					"'").replace("]", "'").replaceAll(",", "','").replaceAll(
					" ", "");
			/*
			 * if (activeGameList != null) { for (int i = 0; i <
			 * activeGameList.size(); i++) { activeGameBean =
			 * activeGameList.get(i); if (game_id==activeGameBean.getGameId()) {
			 * game_nbr=activeGameBean.getGameNbr_Name().split("-")[0]; break; } } }
			 */
			Map gameTickNum = (Map) verifiedData.get(2);
			Iterator itTkt = gameTickNum.entrySet().iterator();
			while (itTkt.hasNext()) {
				Map.Entry pairsTkt = (Map.Entry) itTkt.next();
				game_id = (Integer) pairsTkt.getKey();
				System.out.println("Tkt Map--game_id" + game_id);
				if (activeGameList != null) {
					for (int i = 0; i < activeGameList.size(); i++) {
						activeGameBean = activeGameList.get(i);
						if (game_id == activeGameBean.getGameId()) {
							game_nbr = activeGameBean.getGameNbr_Name().split(
									"-")[0];
							break;
						}
					}
				}
				updPwtTktQry = "update st_se_pwt_tickets_inv_"
						+ game_nbr
						+ " aa,  st_se_tmp_pwt_tickets_inv bb set aa.status='CLAIM_AGT', aa.verify_by_user='"
						+ userId
						+ "', aa.verify_by_org='"
						+ userOrgID
						+ "'  where aa.status='CLAIM_RET' and aa.ticket_nbr in ("
						+ tktNbrStr + ") and aa.ticket_nbr = bb.ticket_nbr";
				System.out.println("********PWT Update********" + updPwtTktQry);
				statement.executeUpdate(updPwtTktQry);
				// "insert into st_se_pwt_tickets_inv_"+game_nbr+" select
				// ticket_nbr,game_id,book_nbr,'CLAIM_AGT' as status
				// ,'"+userId+"' as verify_by_user,'"+userOrgID+"' as
				// 'verify_by_org from st_se_tmp_pwt_tickets_inv where
				// receipt_id='"+receiptNum+"' and final_receipt_num='NO
				// RECEIPT' and game_id="+game_id+" and ticket_nbr in
				// ("+tktNbrStr+")";

				// insertPwtTktQry ="insert into
				// st_se_pwt_tickets_inv_"+game_nbr+" select bb.ticket_nbr,
				// bb.game_id, bb.book_nbr,'CLAIM_AGT' as status ,'"+userId+"'
				// as verify_by_user,'"+userOrgID+"' as verify_by_org from
				// st_se_tmp_pwt_tickets_inv bb where
				// bb.receipt_id='"+receiptNum+"' and bb.final_receipt_num='NO
				// RECEIPT' and bb.game_id="+game_id+" and bb.ticket_nbr in
				// ("+tktNbrStr+") and bb.ticket_nbr not in (select ticket_nbr
				// from st_se_pwt_tickets_inv_"+game_nbr+")";
				insertPwtTktQry = "insert into st_se_pwt_tickets_inv_"
						+ game_nbr
						+ " select bb.ticket_nbr, bb.game_id, bb.book_nbr,'CLAIM_AGT' as status ,'"
						+ userId
						+ "' as verify_by_user,'"
						+ userOrgID
						+ "' as verify_by_org,'"
						+ channel
						+ "','"
						+ interfaceType
						+ "',stpr.agent_org_id,stpr.date_entered from st_se_tmp_pwt_tickets_inv bb ,st_se_tmp_pwt_receipt stpr where bb.receipt_id='"
						+ receiptNum
						+ "' and bb.game_id="
						+ game_id
						+ " and bb.receipt_id=stpr.receipt_id and bb.ticket_nbr in ("
						+ tktNbrStr
						+ ") and bb.ticket_nbr not in (select ticket_nbr from st_se_pwt_tickets_inv_"
						+ game_nbr + ")";

				System.out.println("********PWT Insert********"
						+ insertPwtTktQry);
				statement.executeUpdate(insertPwtTktQry);
				;
			}

		} catch (SQLException e) {
			System.out.println("In Generate Receipt Exception " + e);
			e.printStackTrace();
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

	public int fetchOrgId(String orgName, Connection con) throws SQLException {

		Statement stmt = con.createStatement();
		ResultSet rs = stmt
				.executeQuery("select organization_id from st_lms_organization_master where name = '"
						+ orgName + "'");
		int orgId = 0;
		if (rs.next()) {
			orgId = rs.getInt("organization_id");
		}
		return orgId;
	}

	public List generateReceipt(String receiptNum) {
		// List<TicketBean> VerifyTicketList = new ArrayList<TicketBean>();

		Map gameTickNum = new HashMap();
		Map gameVirnCode = new HashMap();
		List verifiedData = new ArrayList();
		List tickNumberList = new ArrayList();

		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();

		Connection connection = null;
		Statement statement = null;
		Statement statementTick = null;
		ResultSet resultSet = null;
		ResultSet resultSetTick = null;
		TicketBean ticketBean = null;
		int game_id = 0;

		try {
			System.out.println("inside try............");

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statementTick = connection.createStatement();

			String query = "select distinct(game_id) from st_se_tmp_pwt_tickets_inv where receipt_id='"
					+ receiptNum + "'";
			String getTicketListQuery = "select ticket_nbr from st_se_tmp_pwt_tickets_inv where receipt_id='"
					+ receiptNum + "' and status='OPEN' and game_id=";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				game_id = Integer.parseInt(resultSet.getString("game_id"));
				resultSetTick = statementTick.executeQuery(getTicketListQuery
						+ game_id);
				List<TicketBean> ticketList = new ArrayList<TicketBean>();
				while (resultSetTick.next()) {
					ticketBean = new TicketBean();
					ticketBean.setTicketNumber(resultSetTick
							.getString("ticket_nbr"));
					tickNumberList.add(resultSetTick.getString("ticket_nbr"));
					ticketList.add(ticketBean);
				}
				gameTickNum.put(game_id, ticketList);
			}
			System.out.println("Ticket Map--" + gameTickNum);

			query = "select distinct(game_id) from st_se_tmp_pwt_inv where receipt_id='"
					+ receiptNum + "'";
			getTicketListQuery = "select virn_code from st_se_tmp_pwt_inv where receipt_id='"
					+ receiptNum + "' and status='OPEN' and game_id=";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				game_id = Integer.parseInt(resultSet.getString("game_id"));
				resultSetTick = statementTick.executeQuery(getTicketListQuery
						+ game_id);
				List virnList = new ArrayList();
				while (resultSetTick.next()) {
					virnList.add(resultSetTick.getString("virn_code"));
				}
				gameVirnCode.put(game_id, virnList);
			}

			System.out.println("VIRN Map--" + gameVirnCode);

			verifiedData.add(tickNumberList);
			verifiedData.add(gameVirnCode);
			verifiedData.add(gameTickNum);
			connection.commit();
			return verifiedData;
		} catch (SQLException e) {
			System.out.println("In Generate Receipt Exception " + e);
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

	public List<ActiveGameBean> getActiveGameList() {
		return activeGameList;
	}

	private String getEncodedVirnCodeTmpReceipt(String[] virnCode) {

		StringBuffer encodedVirnCode = new StringBuffer("");

		for (String element : virnCode) {

			encodedVirnCode.append("'");
			encodedVirnCode.append(element);
			encodedVirnCode.append("'");
			encodedVirnCode.append(",");

		}
		int length = encodedVirnCode.length();
		encodedVirnCode.deleteCharAt(length - 1);

		return encodedVirnCode.toString();
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public String getTmpReceiptId(Map gameNumMap, String agtOrgName,
			String boOrgName, int userOrgID, String root_path) {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement tmpPwtInsert = null;

		try {
			System.out.println("inside try............");
			int agentOrgId = 0;
			String receiptId = null;
			int gameId = 0;

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			PreparedStatement autoGenRecptPstmtBO = null;
			String getLatestRecieptNumberBO = "SELECT * from st_se_tmp_pwt_receipt ORDER BY receipt_id DESC LIMIT 1";
			autoGenRecptPstmtBO = connection
					.prepareStatement(getLatestRecieptNumberBO);
			ResultSet recieptRsBO = autoGenRecptPstmtBO.executeQuery();
			String lastRecieptNoGeneratedBO = null;

			while (recieptRsBO.next()) {
				lastRecieptNoGeneratedBO = recieptRsBO.getString("receipt_id");
			}

			String autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
					"TPWTRECEIPT", lastRecieptNoGeneratedBO, "BO");

			String getAgentIdQuery = "select organization_id from st_lms_organization_master where name='"
					+ agtOrgName + "'";
			String getGameIdQuery = "select game_id from st_se_game_master where game_nbr=";

			String tmpPwtInsertquery = "insert into st_se_tmp_pwt_receipt(receipt_id,agent_org_id,status,date_entered) values (?,?,?,?)";
			String tmpPwtDetailInsertquery = "insert into st_se_tmp_pwt_receipt_detail values ";

			resultSet = statement.executeQuery(getAgentIdQuery);
			while (resultSet.next()) {
				agentOrgId = resultSet.getInt("organization_id");
			}
			Timestamp dateEntered = new Timestamp(new java.util.Date()
					.getTime());
			System.out.println("777777777777777 b  " + dateEntered);
			tmpPwtInsert = connection.prepareStatement(tmpPwtInsertquery);
			tmpPwtInsert.setString(1, autoGeneRecieptNoBO);
			tmpPwtInsert.setInt(2, agentOrgId);
			tmpPwtInsert.setString(3, "OPEN");
			tmpPwtInsert.setTimestamp(4, dateEntered);
			// tmpPwtInsertquery =
			// tmpPwtInsertquery+"('"+autoGeneRecieptNoBO+"',"+agentOrgId+",'OPEN','"+new
			// Timestamp(new java.util.Date().getTime())+"')";
			System.out.println(lastRecieptNoGeneratedBO + "--"
					+ autoGeneRecieptNoBO + "--insert Query--"
					+ tmpPwtInsertquery);
			// statement.executeUpdate(tmpPwtInsertquery);
			tmpPwtInsert.executeUpdate();
			receiptId = autoGeneRecieptNoBO;

			StringBuilder valuesData = new StringBuilder();
			Iterator gameNumItr = gameNumMap.entrySet().iterator();

			while (gameNumItr.hasNext()) {
				Map.Entry gameNumpair = (Map.Entry) gameNumItr.next();

				Map przMap = (HashMap) gameNumpair.getValue();

				if (!przMap.isEmpty()) {
					resultSet = statement.executeQuery(getGameIdQuery
							+ gameNumpair.getKey());

					while (resultSet.next()) {
						gameId = resultSet.getInt("game_id");
					}

					Iterator it = przMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						String przAmtData = pairs.getValue() + ","
								+ pairs.getKey();
						valuesData.append("('" + receiptId + "'," + gameId
								+ "," + przAmtData + "),");
					}
				}

			}

			statement = connection.createStatement();
			statement.executeUpdate(tmpPwtDetailInsertquery
					+ valuesData.substring(0, valuesData.length() - 1));

			connection.commit();

			GraphReportHelper reportHelper = new GraphReportHelper();
			reportHelper.generateTempPWTReceipt(receiptId, boOrgName,
					userOrgID, root_path, agentOrgId);

			return receiptId + "";

		} catch (SQLException e) {
			System.out.println("In PwtVerifyHelper Exception " + e);
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	private String getWhereClause(String agtOrgName, String receiptNum,
			String fromDate, String toDate, String status, String pwtGen)
			throws ParseException {
		StringBuilder whereClause = new StringBuilder();
		if (receiptNum != null & !receiptNum.equals("")) {
			if (pwtGen.equals("pwtRcptGen")) {
				whereClause.append("searchResult.receipt_id='" + receiptNum
						+ "' and ");
			} else {
				whereClause.append("receipt_id='" + receiptNum + "' and ");
			}
		}
		if (agtOrgName != null & !agtOrgName.equals("-1")) {
			whereClause
					.append("agent_org_id=(select organization_id from st_lms_organization_master where name='"
							+ agtOrgName + "') and ");
		}
		if (fromDate != null & !fromDate.equals("")) {
			whereClause.append("date_entered between '" + fromDate + "' and ");
		} else {
			try {
				whereClause.append("date_entered between '"
						+ new Timestamp((new SimpleDateFormat("yyyy-MM-dd"))
								.parse("1900-01-01").getTime()) + "' and ");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (toDate != null & !toDate.equals("")) {
			whereClause.append("'"
					+ add(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd")
							.parse(toDate).getTime()), 1) + "'");
		} else {
			whereClause.append("'"
					+ new Timestamp(new java.util.Date().getTime()) + "'");
		}
		if (status != null & !status.equals("") & !status.equals("-1")) {
			whereClause.append("and status = '" + status + "'");
		}

		return whereClause.toString();
	}

	public List pwtRcptGenSearchRes(String agtOrgName, String receiptNum,

	String fromDate, String toDate, String status) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		List tmpRcptSearchRes = new ArrayList();
		String whereClause = " ";
		try {
			whereClause = getWhereClause(agtOrgName, receiptNum, fromDate,
					toDate, status, "pwtRcptGen");
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Where Clause--" + whereClause);

		try {
			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			String searchQuery = "select status,no_of_tickets,date_entered,verified_tickets,verified_virn,agent_org_id,noOfTicket.receipt_id,name from (select status,date_entered,verified_tickets,verified_virn,agent_org_id,result.receipt_id,name from st_lms_organization_master, st_se_tmp_pwt_receipt stpr,(select distinct(receipt_id),SUM(verified_tickets) as verified_tickets,SUM(verified_virn) as verified_virn  from (select 0 verified_tickets,0 verified_virn,receipt_id from st_se_tmp_pwt_receipt stpr union select count(ticket.receipt_id) as verified_tickets ,0 verified_virn,receipt_id from st_se_tmp_pwt_tickets_inv ticket group by receipt_id  union select 0 verified_tickets,count(virn.receipt_id) as verified_virn ,receipt_id from st_se_tmp_pwt_inv virn group by receipt_id) c group by receipt_id) result where stpr.receipt_id=result.receipt_id and agent_org_id=organization_id) searchResult , (select sum(no_of_tickets) no_of_tickets ,receipt_id from st_se_tmp_pwt_receipt_detail group by receipt_id) noOfTicket where searchResult.receipt_id = noOfTicket.receipt_id";

			System.out.println(searchQuery + " and " + whereClause);
			resultSet = statement.executeQuery(searchQuery + " and "
					+ whereClause);

			while (resultSet.next()) {
				TmpPwtReceiptBean receiptBean = new TmpPwtReceiptBean();
				receiptBean.setAgtOrgName(resultSet.getString("name"));
				receiptBean.setReceiptId(resultSet.getString("receipt_id"));
				receiptBean.setRecievedDate(resultSet.getTimestamp(
						"date_entered").toString());
				receiptBean.setTickReceived(resultSet
						.getString("no_of_tickets"));
				receiptBean.setVerifiedTickNum(resultSet
						.getString("verified_tickets"));
				receiptBean.setVerifiedVIRN(resultSet
						.getString("verified_virn"));
				receiptBean.setStatus(resultSet.getString("status"));
				tmpRcptSearchRes.add(receiptBean);
			}

			return tmpRcptSearchRes;
		} catch (SQLException e) {
			System.out.println("In PwtVerifyHelper Exception " + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	public List<PwtBean> saveBOPwtTicketsDataTmpReceipt(String boOrgName,
			int userID, int userOrgID, OrgBean agtOrgBean,
			List<ActiveGameBean> activeGameList, String rootPath,
			double HighPrizeAmount, String highPrizeCriteria,
			List verifiedData, String receiptNum, String closeReceipt,
			String channel, String interfaceType) throws LMSException {

		setReceiptNum(receiptNum);
		setActiveGameList(activeGameList);
		// virn = new StringBuilder();
		virnMap = new HashMap<Integer, StringBuilder>();
		Connection connection = null;
		boolean isCommitable = false;
		List<PwtBean> pwtCompleteReceiptList = new ArrayList<PwtBean>();

		PreparedStatement boReceiptPstmt = null;
		PreparedStatement boReceiptMappingPstmt = null;
		String boReceiptQuery = null;
		String boReceiptMappingQuery = null;

		int boReceiptId = 0;

		double agtPwtCommRate = 0.0;
		int gameNbr = 0;
		;

		ActiveGameBean activeGameBean = null;

		int agentOrgId = agtOrgBean.getOrgId();
		int agtUserId = agtOrgBean.getUserId();

		System.out.println(agtOrgBean.getOrgName()
				+ "-saveBOPwtTicketsDataTmpReceipt--agtOrgId-**" + agentOrgId);
		try {
			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			List transactionId = new ArrayList();
			// String receiptArr=null;

			Map gameVirnCode = (Map) verifiedData.get(1);
			PwtBean pwtBean = null;
			int game_id = 0;
			Iterator itVirn = gameVirnCode.entrySet().iterator();
			while (itVirn.hasNext()) {
				StringBuilder virn = new StringBuilder();
				List<PwtBean> pwtList = new ArrayList<PwtBean>();
				Map.Entry pairsVirn = (Map.Entry) itVirn.next();
				game_id = (Integer) pairsVirn.getKey();
				System.out.println("VIRN Map--game_id" + game_id);
				List virnList = (List) pairsVirn.getValue();
				if (!virnList.isEmpty()) {
					String[] virnArray = (String[]) virnList
							.toArray(new String[virnList.size()]);

					for (int i = 0; i < virnArray.length; i++) {
						if (virnArray[i] != null
								&& !virnArray[i].trim().equals("")) {
							pwtBean = new PwtBean();
							pwtBean.setVirnCode(virnArray[i]);
							pwtBean.setValid(true);
							pwtList.add(pwtBean);
						}
					}

					if (activeGameList != null) {
						for (int i = 0; i < activeGameList.size(); i++) {
							activeGameBean = activeGameList.get(i);
							if (game_id == activeGameBean.getGameId()) {
								// agtPwtCommRate =
								// activeGameBean.getAgentPwtCommRate();
								gameNbr = Integer.parseInt(activeGameBean
										.getGameNbr_Name().split("-")[0]);
								break;

							}
						}
					}
					// pwt verification and check for umclaimed
					boolean isVerified = verifyPwtTicketsTmpReceipt(game_id,
							virnArray, pwtList, HighPrizeAmount,
							highPrizeCriteria, gameNbr, agentOrgId);
					if (isVerified) {
						long transactionID = saveBOPwtTicketsTmpReceipt(pwtList,
								game_id, boOrgName, userID, userOrgID,
								agentOrgId, agtUserId, connection, virn,
								gameNbr);
						if (transactionID != 0 && transactionID != -1) {
							transactionId.add(transactionID);
						}
						pwtCompleteReceiptList.addAll(pwtList);

						virn.append("''");
						virnMap.put(game_id, virn);

					}
				}
			}
			System.out.println("---List of Tx Id--" + transactionId);
			System.out.println("---virn Map game Wise-" + virnMap);

			// get auto generated reciept number
			if (transactionId != null && transactionId.size() > 0) {
				CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
				String generatedReceiptNumber = commonHelper.generateReceiptBo(
						connection, agentOrgId, "AGENT", transactionId);
				if (generatedReceiptNumber != null) {
					isCommitable = true;
				} else {
					throw new LMSException(
							"Exception during generating final receipt while generating receipt");
				}
				boReceiptId = Integer.parseInt(generatedReceiptNumber
						.split("-")[0]);

				if (isCommitable) {
					// virn.append("''");
					// verifiedData.add(virn);
					verifiedData.add(virnMap);
					System.out.println("---verifiedData-" + verifiedData);
					// Temporary receipt tables entries
					updateTempReceiptTables(verifiedData, boReceiptId + "",
							connection, userID, userOrgID, channel,
							interfaceType);

					List<TicketBean> VerifyTicketList = new ArrayList<TicketBean>();
					int game_id_tk = 0;
					PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
					System.out.println("Ticket Map--" + verifiedData.get(2));
					Map gameTickNum = (HashMap) verifiedData.get(2);
					Iterator it = gameTickNum.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						game_id_tk = (Integer) pairs.getKey();
						updateTicketsStatus(
								(List<TicketBean>) pairs.getValue(), userID,
								userOrgID, game_id_tk, connection, agentOrgId,
								channel, interfaceType);
					}
				} else {
					throw new LMSException(
							"There are no Valid Transaction done for these specified PWT");
				}

			}
			// closing receipt
			if (closeReceipt != null && closeReceipt.equals("Yes")) {
				closeReceipt(receiptNum, userID, userOrgID, verifiedData,
						activeGameList, connection, channel, interfaceType);
			} else {
				System.out.println("ALERT :: receipt not closed");
			}
			connection.commit();

			if (boReceiptId > 0) {
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportBO(boReceiptId, boOrgName,
						userOrgID, rootPath);
			}

		} catch (SQLException e) {
			try {
				connection.rollback();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

		return pwtCompleteReceiptList;

	}

	private long saveBOPwtTicketsTmpReceipt(List<PwtBean> pwtList, int gameId,
			String boOrgName, int userID, int userOrgID, int agentOrgId,
			int agtUserId, Connection connection, StringBuilder virn,
			int gameNbr) throws LMSException {
		try {
			PreparedStatement masterPstmt = null;
			PreparedStatement detailPstmt = null;
			PreparedStatement invPstmt = null;
			// PreparedStatement agtGameCommVarPstmt = null;
			PreparedStatement agtPwtUpdatePstmt = null;

			ResultSet resultSet = null;
			// ResultSet resultSetCommVar = null;

			long trnId = -1;

			if (pwtList != null) {
				int size = pwtList.size();
				PwtBean pwtBean = null;
				String masterQuery = null;
				String detailQuery = null;
				String invQuery = null;

				double pwtAmt = 0.0;
				double commAmt = 0.0;
				double netAmt = 0.0;
				double creditAmt = 0.0;
				double debitUnclaim = 0.0;

				if (size > 0) {

					// get agents game pwtCommRate
					CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
					double agtPwtCommRate = commonHelper
							.fetchCommOfOrganization(gameId, agentOrgId, "PWT",
									"AGENT", connection);

					masterPstmt = connection.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					masterPstmt.setString(1, "AGENT");

					masterPstmt.executeUpdate();
					resultSet = masterPstmt.getGeneratedKeys();
					if (resultSet.next()) {
						trnId = resultSet.getLong(1);
						System.out.println("Transaction Id:" + trnId);

						masterQuery = QueryManager.getST1BOMasterQuery();
						masterPstmt = connection.prepareStatement(masterQuery);

						detailQuery = QueryManager.getST1PwtBODetailQuery();
						detailPstmt = connection.prepareStatement(detailQuery);

						invQuery = QueryManager.getST1PWTBOUpdateQuery();
						invPstmt = connection.prepareStatement(invQuery);
						System.out.println(userOrgID + "--userOrg---userId-"
								+ userID
								+ "-saveBOPwtTicketsTmpReceipt--agtOrgId-***"
								+ agentOrgId);

						// update st_se_agent_pwt in case if agent brings
						// uncalimed PWTs to BO
						String agtPwtUpdateQuery = "update  st_se_agent_pwt set status=? where game_id = ? and virn_code = ? and agent_org_id = ? ";
						agtPwtUpdatePstmt = connection
								.prepareStatement(agtPwtUpdateQuery);

						masterPstmt.setLong(1, trnId);
						masterPstmt.setString(2, "AGENT");
						masterPstmt.setInt(3, agentOrgId);
						masterPstmt.setTimestamp(4, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						masterPstmt.setString(5, "PWT");
						masterPstmt.setInt(6, userID);
						masterPstmt.setInt(7, userOrgID);
						masterPstmt.executeUpdate();

						System.out.println("transaction ID ::" + trnId);

						for (int i = 0; i < size; i++) {
							pwtBean = (PwtBean) pwtList.get(i);

							if (pwtBean.getIsValid()) {

								String encodedVirn = pwtBean.getVirnCode();
								detailPstmt.setString(1, encodedVirn);
								detailPstmt.setLong(2, trnId);
								detailPstmt.setInt(3, gameId);
								detailPstmt.setInt(4, agtUserId);
								detailPstmt.setInt(5, agentOrgId);

								pwtAmt = Double.parseDouble(pwtBean
										.getPwtAmount());
								commAmt = pwtAmt * agtPwtCommRate * 0.01;
								netAmt = pwtAmt + commAmt;
								// for current credit amt updation
								creditAmt += netAmt;

								detailPstmt.setDouble(6, pwtAmt);
								detailPstmt.setDouble(7, commAmt);
								detailPstmt.setDouble(8, netAmt);
								detailPstmt.execute();

								if ("IN_PLR_UNCLM".equalsIgnoreCase(pwtBean
										.getInUnclmed())
										|| "IN_AGENT_UNCLM"
												.equalsIgnoreCase(pwtBean
														.getInUnclmed())) {
									String tableType;
									if ("IN_PLR_UNCLM".equalsIgnoreCase(pwtBean
											.getInUnclmed())) {
										tableType = "PLAYER";
									} else {
										tableType = "AGENT";
									}

									commonHelper.updateOrgForUnClaimedVirn(
											agentOrgId, "AGENT", encodedVirn,
											"DONE_UNCLM", gameId, tableType,
											connection,pwtBean.getTicketNumber());
									debitUnclaim += pwtAmt;

								}

								// by yogesh game nbr
								GameUtilityHelper.updateNoOfPrizeRem(gameId,
										pwtAmt, "CLAIM_AGT", encodedVirn,
										connection, gameNbr);

								invPstmt.setInt(1, gameNbr);
								invPstmt.setInt(2, gameId);
								invPstmt.setString(3, encodedVirn);
								invPstmt.execute();
								virn.append("'" + encodedVirn + "',");

							}

						}

						boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "PWT", agentOrgId,
								0, "AGENT", 0, connection);
						
						if(!isValid)
							throw new LMSException();
						
						// for current credit amt updation
						/*boolean isUpdateDone = OrgCreditUpdation
								.updateCreditLimitForAgent(agentOrgId, "PWT",
										creditAmt, connection);*/
						System.out
								.println("---saveBOPwtTicketsTmpReceipt--virn-"
										+ virn);

						// update AGENT's UNCLAIM_BAL Balance
						if (creditAmt > 0.0) {
							OrgCreditUpdation.updateOrganizationBalWithValidate(debitUnclaim, "UNCLAIM_BAL", "DEBIT", agentOrgId,
									0, "AGENT", 0, connection);
							/*commonHelper.updateOrgBalance("UNCLAIM_BAL",
									debitUnclaim, agentOrgId, "DEBIT",
									connection);*/
						}
					} else {
						throw new LMSException(
								"Exception while generating final receipt Transaction id not generated: "
										+ trnId);
					}
				}

			}

			return trnId;
		} catch (SQLException sqle) {
			throw new LMSException(sqle);
		}
	}

	/*
	 * private boolean verifyPwtTicketsTmpReceipt(int gameId, String[] virnCode,
	 * List<PwtBean> pwtList,double HighPrizeAmount,String
	 * highPrizeCriteria,int gameNbr,int agtOrgId) throws LMSException {
	 * System.out.println("verify fumction called "); String encodedVirnCode =
	 * getEncodedVirnCodeTmpReceipt(virnCode); System.out.println("---((((((::" +
	 * encodedVirnCode);
	 * 
	 * Connection connection = null; Statement statement = null; Statement
	 * statement2 = null; Statement statement3 = null; Statement statement4 =
	 * null;
	 * 
	 * ResultSet resultSet = null; ResultSet resultSet2 = null; ResultSet
	 * resultSet3 = null; ResultSet resultSet4 = null;
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
	 * if (size > 0) { try {   connection =
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
	 * (prizeStatus.equalsIgnoreCase("CLAIM_RET_UNCLM")) { CommonFunctionsHelper
	 * commHelper = new CommonFunctionsHelper(); boolean flag =
	 * commHelper.verifyOrgForUnClaimedVirn(agtOrgId, "AGENT", enVirnCode,
	 * "UNCLAIM_BAL", gameId, connection); if(flag){ String orgname=null; String
	 * receiptNumber=null; Timestamp receiptTime=null;
	 * statement4=connection.createStatement(); //String agtDetailsQuery="select
	 * name from st_lms_organization_master where organization_id in (select
	 * agent_org_id from st_se_bo_pwt where virn_code='"+enVirnCode+"' and
	 * game_id="+gameId+")"; String retDetailsQuery="select
	 * a.name,c.generated_id,e.transaction_date from st_lms_organization_master
	 * a,st_se_agent_pwt b,st_lms_agent_receipts
	 * c,st_lms_agent_transaction_master e where b.virn_code='"+enVirnCode+"'
	 * and b.game_id="+gameId+" and a.organization_id=b.retailer_org_id and
	 * b.transaction_id=e.transaction_id and c.receipt_id=(select receipt_id
	 * from st_lms_agent_receipts_trn_mapping where
	 * transaction_id=e.transaction_id)";
	 * 
	 * System.out.println("query for get org name " + retDetailsQuery);
	 * resultSet4=statement4.executeQuery(retDetailsQuery);
	 * while(resultSet4.next()){ orgname= resultSet4.getString("name");
	 * receiptNumber=resultSet4.getString("generated_id");
	 * receiptTime=resultSet4.getTimestamp("transaction_date"); }
	 * 
	 * isVerified = true; pwtBean.setValid(true);
	 * pwtBean.setVerificationStatus("Valid Virn");
	 * pwtBean.setPwtAmount(pwtAmount); //pwtBean.setMessage("Credited to
	 * Concerned Party"); pwtBean.setMessage("Already Paid to Retailer: " +
	 * orgname+" on Voucher Number: "+receiptNumber+" at "+receiptTime);
	 * pwtBean.setIsUnclmed(true); pwtBean.setMessageCode("111001"); }else{
	 * pwtBean.setValid(false); pwtBean.setVerificationStatus("InValid Virn");
	 * pwtBean.setMessage("VIRN To be Claimed by Another Agent.");
	 * pwtBean.setMessageCode("Undefined"); } break; }else if
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

	private void saveTicketsData(int game_id,
			List<TicketBean> verifiedTicketList, Connection connection)
			throws SQLException {

		ServletContext servletContext = ServletActionContext
				.getServletContext();
		String retOnline = (String) servletContext.getAttribute("RET_ONLINE");
		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();

		PreparedStatement Pstmt = null;

		String query = null;

		if (retOnline.equals("NO")) {
			query = QueryManager.updateIntoPwtTicketsInv();
		} else {
			query = "update st_pwt_tickets_inv set status=? where ticket_nbr=? and  game_id =?";
		}

		Pstmt = connection.prepareStatement(query);
		Iterator<TicketBean> iterator = verifiedTicketList.iterator();
		while (iterator.hasNext()) {
			String ticket_nbr = null;
			boolean isValid = false;
			TicketBean ticketBean = (TicketBean) iterator.next();
			isValid = ticketBean.getIsValid();
			if (isValid == true) {
				ticket_nbr = ticketBean.getTicketNumber();
				String book_nbr = pwtTicketHelper
						.getBookNbrFromTicketNo(ticket_nbr);
				if (retOnline.equals("NO")) {
					Pstmt.setString(1, ticket_nbr);
					Pstmt.setInt(2, game_id);
					Pstmt.setString(3, book_nbr);
					Pstmt.setString(4, "CLAIM_RET");
					Pstmt.executeUpdate();
				} else {

					Pstmt.setString(1, "CLAIM_RET");
					Pstmt.setString(2, ticket_nbr);
					Pstmt.setInt(3, game_id);
					Pstmt.executeUpdate();

				}
			}
		}
	}

	public List<TicketBean> saveTmpTicketsData(
			List<TicketBean> verifiedTicketList, String receiptNum, int userId,
			int userOrgId, String agtOrgName, String channel,
			String interfaceType) throws LMSException {
		List<TicketBean> savedResults = new ArrayList<TicketBean>();
		Connection connection = null;
		// PreparedStatement Pstmt = null;
		PreparedStatement Pstmt1 = null;
		String query = QueryManager.getST4UpdatePwtTicketStatusToAGT();
		try {
			 
			connection = DBConnect.getConnection();
			// Pstmt = connection.prepareStatement(query);
			Timestamp dateEntered = new Timestamp(new java.util.Date()
					.getTime());
			Pstmt1 = connection
					.prepareStatement("insert into st_se_tmp_pwt_tickets_inv(ticket_nbr,book_nbr,game_id,receipt_id,user_id,status,date_entered) values(?,?,?,?,?,?,?)");
			Iterator<TicketBean> iterator = verifiedTicketList.iterator();
			System.out.println("Verified Tickets list: " + verifiedTicketList);
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			while (iterator.hasNext()) {
				String ticket_nbr = null;
				boolean isValid = false;
				// String status=null;
				int size = 0;
				TicketBean ticketBean = (TicketBean) iterator.next();
				isValid = ticketBean.getIsValid();
				// status=ticketBean.getStatus();
				if (isValid == true) {
					ticket_nbr = ticketBean.getTicketNumber();
					Pstmt1.setString(1, ticket_nbr);
					Pstmt1.setString(2, ticketBean.getBook_nbr());
					Pstmt1.setInt(3, ticketBean.getTicketGameId());
					Pstmt1.setString(4, receiptNum);
					Pstmt1.setInt(5, userId);
					Pstmt1.setString(6, "OPEN");
					Pstmt1.setTimestamp(7, dateEntered);
					Pstmt1.executeUpdate();
					int agtOrgId = fetchOrgId(agtOrgName, connection);
					// insert in pwt ticket main table
					commonFunction.updateTicketInvTable(ticket_nbr, ticketBean
							.getBook_nbr(), ticketBean.getGameNbr(), ticketBean
							.getTicketGameId(), "CLAIM_AGT_TEMP", userId,
							userOrgId, ticketBean.getUpdateTicketType(),
							agtOrgId, channel, interfaceType, connection);

					// update book status from active to claimed in table
					// st_se_game_inv_status table here
					if ("ACTIVE".equalsIgnoreCase(ticketBean.getBookStatus())) {
						commonFunction.updateBookStatus(ticketBean
								.getTicketGameId(), ticketBean.getBook_nbr(),
								connection, "CLAIMED");
					}

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					bean.setValid(isValid);
					bean.setStatus("Ticket Is Saved For PWT");
					bean.setValidity("Ticket Is Valid");
					savedResults.add(bean);
				}
			}
			System.out.println("SavedTickets List: " + savedResults);
			return savedResults;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				/*
				 * if (Pstmt != null) Pstmt.close();
				 */
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				// throw new LMSException();
			}
		}

		return null;
	}

	// Temporary virn to final receipt

	public void setActiveGameList(List<ActiveGameBean> activeGameList) {
		this.activeGameList = activeGameList;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	/*
	 * public String getBookNbrFromTicketNo(String ticket_nbr) { String book_nbr =
	 * ""; StringTokenizer st = new StringTokenizer(ticket_nbr, "-"); for (int i =
	 * 0; i < 2; i++) { if (st.hasMoreTokens()) { book_nbr = book_nbr +
	 * st.nextToken(); if (i == 0) { book_nbr = book_nbr + "-"; } } } //
	 * System.out.println(book_nbr); return book_nbr; }
	 */
	public List tmpRcptDetail(String receiptNum) {
		List tmpRcptList = new ArrayList();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			System.out.println("inside try............");
			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String tmpPwtDetailquery = "select game_name,user_name,verified_tickets,verified_virn ,generated_id ,final_receipt_num from st_lms_bo_receipts sr right join (select * from (select game_name,user_name,SUM(verified_tickets) as verified_tickets,SUM(verified_virn) as verified_virn ,final_receipt_num from (select game_id,user_id , count(ticket.user_id) as verified_tickets ,0 verified_virn ,final_receipt_num from st_se_tmp_pwt_tickets_inv ticket where receipt_id ='"
					+ receiptNum
					+ "'  group by final_receipt_num,user_id,game_id union select game_id,user_id ,0 as verified_tickets , count(virn.user_id) verified_virn ,final_receipt_num from st_se_tmp_pwt_inv virn where receipt_id ='"
					+ receiptNum
					+ "' group by final_receipt_num,user_id,game_id ) c , st_se_game_master game,st_lms_user_master userm where c.game_id=game.game_id and userm.user_id=c.user_id group by final_receipt_num,c.user_id,c.game_id order by c.game_id)res )rest on sr.receipt_id = rest.final_receipt_num order by generated_id,game_name";
			System.out.println("query  :: ------- " + tmpPwtDetailquery);
			// without final receipt num "select
			// game_name,user_name,SUM(verified_tickets) as
			// verified_tickets,SUM(verified_virn) as verified_virn from (select
			// game_id,user_id , count(ticket.user_id) as verified_tickets ,0
			// verified_virn from st_se_tmp_pwt_tickets_inv ticket where
			// receipt_id ="+receiptNum+" group by user_id,game_id union select
			// game_id,user_id ,0 as verified_tickets , count(virn.user_id)
			// verified_virn from st_se_tmp_pwt_inv virn where receipt_id
			// ="+receiptNum+" group by user_id,game_id ) c , st_se_game_master
			// game,st_lms_user_master userm where c.game_id=game.game_id and
			// userm.user_id=c.user_id group by c.user_id,c.game_id order by
			// c.game_id"
			// with receiptid without join "select
			// game_name,user_name,SUM(verified_tickets) as
			// verified_tickets,SUM(verified_virn) as verified_virn
			// ,final_receipt_num from (select game_id,user_id ,
			// count(ticket.user_id) as verified_tickets ,0 verified_virn
			// ,final_receipt_num from st_se_tmp_pwt_tickets_inv ticket where
			// receipt_id ="+receiptNum+" group by
			// final_receipt_num,user_id,game_id union select game_id,user_id ,0
			// as verified_tickets , count(virn.user_id) verified_virn
			// ,final_receipt_num from st_se_tmp_pwt_inv virn where receipt_id
			// ="+receiptNum+" group by final_receipt_num,user_id,game_id ) c ,
			// st_se_game_master game,st_lms_user_master userm where
			// c.game_id=game.game_id and userm.user_id=c.user_id group by
			// final_receipt_num,c.user_id,c.game_id order by c.game_id"
			resultSet = statement.executeQuery(tmpPwtDetailquery);
			while (resultSet.next()) {
				TmpReceiptDetailBean detailBean = new TmpReceiptDetailBean();
				detailBean.setGameNum(resultSet.getString("game_name"));
				detailBean.setUserName(resultSet.getString("user_name"));
				detailBean.setVerifiedTickNum(resultSet
						.getString("verified_tickets"));
				detailBean
						.setVerifiedVIRN(resultSet.getString("verified_virn"));
				detailBean.setGeneratedId(resultSet.getString("generated_id"));
				detailBean.setFinalReceiptNum(resultSet
						.getString("final_receipt_num"));
				tmpRcptList.add(detailBean);
			}
			System.out.println("-tmpRcptDetail---Helper--" + tmpRcptList);
			// return tmpRcptList;

		} catch (SQLException e) {
			System.out.println("In Temp Receipt Detail Exception " + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
		return tmpRcptList;
	}

	public List tmpRcptSearchRes(String agtOrgName, String receiptNum,
			String fromDate, String toDate) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		List tmpRcptSearchRes = new ArrayList();
		String whereClause = " ";
		try {
			whereClause = getWhereClause(agtOrgName, receiptNum, fromDate,
					toDate, "", "pwtTmp");
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Where Clause--" + whereClause);

		try {
			 
			connection = DBConnect.getConnection();
			// connection.setAutoCommit(false);
			statement = connection.createStatement();

			resultSet = statement
					.executeQuery("select tmpRcpt.receipt_id,name,tmpRcpt.status,tmpRcpt.date_entered from (select receipt_id,status,date_entered,agent_org_id from st_se_tmp_pwt_receipt where "
							+ whereClause
							+ ") tmpRcpt, st_lms_organization_master where organization_id = tmpRcpt.agent_org_id");

			while (resultSet.next()) {
				TmpPwtReceiptBean receiptBean = new TmpPwtReceiptBean();
				receiptBean.setAgtOrgName(resultSet.getString("name"));
				receiptBean.setReceiptId(resultSet.getString("receipt_id"));
				receiptBean.setRecievedDate(resultSet.getTimestamp(
						"date_entered").toString());
				receiptBean.setStatus(resultSet.getString("status"));
				tmpRcptSearchRes.add(receiptBean);
			}

			return tmpRcptSearchRes;
		} catch (SQLException e) {
			System.out.println("In PwtVerifyHelper Exception " + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	private void updateTempReceiptTables(List verifiedData,
			String autoGeneRecieptNoBO, Connection connection, int userID,
			int userOrgID, String channel, String interfaceType)
			throws SQLException {
		Statement statementTmpTable = null;

		// StringBuilder virn = new StringBuilder();
		StringBuilder ticket = new StringBuilder();

		statementTmpTable = connection.createStatement();

		System.out.println("---in updateTempReceiptTables" + verifiedData);
		ticket.append(((List) verifiedData.get(0)).toString().replace("[", "'")
				.replace("]", "'").replaceAll(",", "','").replaceAll(" ", ""));
		System.out.println(verifiedData.get(3) + "Buffer---" + ticket);

		Map virnMap = (Map) verifiedData.get(3);
		String tmpVirnUpdateQuery = null;

		for (Integer gameId : (Set<Integer>) virnMap.keySet()) {
			System.out.println("Buffer--virn-" + virnMap.get(gameId));
			tmpVirnUpdateQuery = "update st_se_tmp_pwt_inv set status='CLOSE',final_receipt_num='"
					+ autoGeneRecieptNoBO
					+ "' where virn_code in ("
					+ virnMap.get(gameId).toString()
					+ ") and game_id="
					+ gameId;
			System.out.println("--tmpVirnUpdateQuery-" + tmpVirnUpdateQuery);
			statementTmpTable.executeUpdate(tmpVirnUpdateQuery);
		}

		// String tmpVirnUpdateQuery = "update st_se_tmp_pwt_inv set
		// status='CLOSE',final_receipt_num='"+autoGeneRecieptNoBO+"' where
		// virn_code in ("+virn.toString()+")";
		String tmpTicketUpdateQuery = "update st_se_tmp_pwt_tickets_inv set status='CLOSE',final_receipt_num='"
				+ autoGeneRecieptNoBO
				+ "' where ticket_nbr in ("
				+ ticket.toString() + ")";
		String insertTmpReceiptMaping = "insert into st_se_tmp_pwt_receipt_mapping values ('"
				+ getReceiptNum() + "','" + autoGeneRecieptNoBO + "')";

		// System.out.println("--tmpVirnUpdateQuery-"+tmpVirnUpdateQuery);
		System.out.println("--tmpTicketUpdateQuery-" + tmpTicketUpdateQuery);
		System.out.println("--insertTmpReceiptMaping--"
				+ insertTmpReceiptMaping);

		// statementTmpTable.executeUpdate(tmpVirnUpdateQuery);
		statementTmpTable.executeUpdate(tmpTicketUpdateQuery);
		statementTmpTable.executeUpdate(insertTmpReceiptMaping);

		String game_nbr = null;
		ActiveGameBean activeGameBean = null;
		int game_id = 0;

		String insertPwtTktQry = null;
		String updPwtTktQry = null;
		String tktNbrStr = null;
		tktNbrStr = ((List) verifiedData.get(0)).toString().replace("[", "'")
				.replace("]", "'").replaceAll(",", "','").replaceAll(" ", "");
		Map gameTickNum = (Map) verifiedData.get(2);
		Iterator itTkt = gameTickNum.entrySet().iterator();
		while (itTkt.hasNext()) {
			Map.Entry pairsTkt = (Map.Entry) itTkt.next();
			game_id = (Integer) pairsTkt.getKey();
			System.out.println("Tkt Map--game_id" + game_id);
			if (activeGameList != null) {
				for (int i = 0; i < activeGameList.size(); i++) {
					activeGameBean = activeGameList.get(i);
					if (game_id == activeGameBean.getGameId()) {
						game_nbr = activeGameBean.getGameNbr_Name().split("-")[0];
						break;
					}
				}
			}

			updPwtTktQry = "update st_se_pwt_tickets_inv_"
					+ game_nbr
					+ " aa,  st_se_tmp_pwt_tickets_inv bb set aa.status='CLAIM_AGT', aa.verify_by_user='"
					+ userID
					+ "', aa.verify_by_org='"
					+ userOrgID
					+ "'  where aa.status='CLAIM_AGT_TEMP' and aa.ticket_nbr in ("
					+ tktNbrStr + ") and aa.ticket_nbr = bb.ticket_nbr";
			System.out.println("********PWT Update********" + updPwtTktQry);
			statementTmpTable.executeUpdate(updPwtTktQry);
			// insertPwtTktQry ="insert into st_se_pwt_tickets_inv_"+game_nbr+"
			// select bb.ticket_nbr, bb.game_id, bb.book_nbr,'CLAIM_AGT' as
			// status ,'"+userID+"' as verify_by_user,'"+userOrgID+"' as
			// verify_by_org from st_se_tmp_pwt_tickets_inv bb where
			// bb.receipt_id='"+receiptNum+"' and bb.game_id="+game_id+" and
			// bb.ticket_nbr in ("+tktNbrStr+") and bb.ticket_nbr not in (select
			// ticket_nbr from st_se_pwt_tickets_inv_"+game_nbr+")";
			insertPwtTktQry = "insert into st_se_pwt_tickets_inv_"
					+ game_nbr
					+ " select bb.ticket_nbr, bb.game_id, bb.book_nbr,'CLAIM_AGT' as status ,'"
					+ userID
					+ "' as verify_by_user,'"
					+ userOrgID
					+ "' as verify_by_org,'"
					+ channel
					+ "','"
					+ interfaceType
					+ "',stpr.agent_org_id,stpr.date_entered from st_se_tmp_pwt_tickets_inv bb ,st_se_tmp_pwt_receipt stpr where bb.receipt_id='"
					+ receiptNum
					+ "' and bb.game_id="
					+ game_id
					+ " and bb.receipt_id=stpr.receipt_id and bb.ticket_nbr in ("
					+ tktNbrStr
					+ ") and bb.ticket_nbr not in (select ticket_nbr from st_se_pwt_tickets_inv_"
					+ game_nbr + ")";
			// insertPwtTktQry = "insert into st_se_pwt_tickets_inv_"+game_nbr+"
			// select ticket_nbr,game_id,book_nbr,'CLAIM_AGT' as status
			// ,'"+userID+"' as verify_by_user,'"+userOrgID+"'verify_by_org from
			// st_se_tmp_pwt_tickets_inv where receipt_id='"+getReceiptNum()+"'
			// and game_id="+game_id+" and ticket_nbr in
			// ("+ticket.toString()+")";

			System.out.println("********PWT Insert********" + insertPwtTktQry);
			statementTmpTable.executeUpdate(insertPwtTktQry);
		}

	}

	public List<TicketBean> updateTicketsStatus(
			List<TicketBean> verifiedTicketList, int userId, int userOrgId,
			int game_id, Connection connection, int agentOrgId, String channel,
			String interfaceType) throws LMSException {

		try {
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			for (TicketBean ticketBean : verifiedTicketList) {
				if (ticketBean.getIsValid()) {
					commHelper.updateTicketInvTable(ticketBean
							.getTicketNumber(), ticketBean.getTicketNumber()
							.split("-")[0]
							+ "-" + ticketBean.getTicketNumber().split("-")[1],
							Integer.parseInt(ticketBean.getTicketNumber()
									.split("-")[0]), game_id, "CLAIM_AGT",
							userId, userOrgId, "UPDATE", agentOrgId, channel,
							interfaceType, connection);

					/*
					 * //update book status from active to claimed in table
					 * st_se_game_inv_status table here
					 * if("ACTIVE".equalsIgnoreCase(ticketBean.getBookStatus()))
					 * commHelper.updateBookStatus(ticketBean.getTicketGameId(),
					 * ticketBean.getBook_nbr(), connection, "CLAIMED");
					 */
				}
			}
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
		return null;
	}

	private boolean verifyPwtTicketsTmpReceipt(int gameId, String[] virnCode,
			List<PwtBean> pwtList, double HighPrizeAmount,
			String highPrizeCriteria, int gameNbr, int agtOrgId)
			throws LMSException {
		System.out.println("verify fumction called ");
		String encodedVirnCode = getEncodedVirnCodeTmpReceipt(virnCode);
		System.out.println("---((((((::" + encodedVirnCode);

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		boolean isVerified = false;
		// int orderId = -1;

		if (pwtList != null) {
			int size = pwtList.size();

			PwtBean pwtBean = null;

			StringBuffer query = new StringBuffer();

			if (size > 0) {
				try {
					 
					connection = DBConnect.getConnection();

					statement = connection.createStatement();
					query.append(QueryManager.getST1PWTBOCheckQuery());
					query.append(" st_se_pwt_inv_" + gameNbr + " where ");
					query.append("  game_id = ");
					query.append("" + gameId);
					query.append(" and virn_code in (");
					query.append(encodedVirnCode);
					query.append(")");

					System.out.println("GameId:" + gameId);
					System.out.println("Query:: " + query);
					resultSet = statement.executeQuery(query.toString());
					System.out.println("ResultSet:" + resultSet + "---"
							+ resultSet.getFetchSize());

					String vCode = null;
					String pwtAmount = null;
					String enVirnCode = null;
					String prizeStatus = null;

					while (resultSet.next()) {

						vCode = resultSet
								.getString(TableConstants.SPI_VIRN_CODE);
						System.out.println("Vcode:" + vCode);
						pwtAmount = resultSet
								.getString(TableConstants.SPI_PWT_AMT);
						prizeStatus = resultSet.getString("status");

						for (int j = 0; j < pwtList.size(); j++) {
							pwtBean = (PwtBean) pwtList.get(j);
							enVirnCode = pwtBean.getVirnCode();
							if (enVirnCode.equals(vCode)) {
								if (prizeStatus
										.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
									isVerified = true;
									pwtBean.setValid(true);
									pwtBean.setPwtAmount(pwtAmount);
									pwtBean
											.setMessage("AAlready Verified in Bulk Receipt at BO, Fianl Payment Pending");
									pwtBean.setMessageCode("112001");
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
										.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
									isVerified = true;
									pwtBean.setValid(true);
									pwtBean.setPwtAmount(pwtAmount);
									pwtBean.setInUnclmed("IN_AGENT_UNCLM");
									pwtBean
											.setMessage("AAlready Verified in Bulk Receipt at BO as unclaimed, Fianl Payment Pending");
									pwtBean.setMessageCode("112001");
									break;
								} else if (prizeStatus
										.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
									isVerified = true;
									pwtBean.setValid(true);
									pwtBean.setPwtAmount(pwtAmount);
									pwtBean.setInUnclmed("IN_PLR_UNCLM");
									pwtBean
											.setMessage("AAlready Verified in Bulk Receipt at BO as unclaimed, Fianl Payment Pending");
									pwtBean.setMessageCode("112001");
									break;
								} else {
									// shoot a mail
									System.out
											.println("undefined status for VIRN at the time of Final receipt generation virn number is  "
													+ enVirnCode);
									CommonMethods
											.sendMail("ERROR :::: undefined status for VIRN at the time of Final receipt generation  or  virn status is modified manually for VIRN Number"
													+ enVirnCode
													+ "Expected status are CLAIM_AGT_TEMP ,CLAIM_RET_AGT_TEMP,CLAIM_PLR_AGT_TEMP");
									throw new LMSException(
											"ERROR :::: undefined status for VIRN at the time of Final receipt generation  or  virn status is modified manually for VIRN Number"
													+ enVirnCode
													+ "Expected status are CLAIM_AGT_TEMP ,CLAIM_RET_AGT_TEMP,CLAIM_PLR_AGT_TEMP");
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
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}

			}
		}
		return isVerified;

	}

}
