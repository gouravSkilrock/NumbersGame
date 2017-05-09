/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ChequeBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryHelper;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.userMgmt.common.CommonFunctions;

/**
 * This class is used to process the cheque bounce from Retailer .
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChequeBounceRetailer extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bankName = "";

	private double chequeAmount = 0.0;
	private double chequeBounceCharges = 0.0;
	private String chequeDate = null;
	private String chequeNumber;
	private int id;
	private String issuePartyname = "";
	private String orgName = null;
	private String orgType = "RETAILER";
	private HttpServletRequest request = null;
	private double totalPay;
	private long  transaction_id;
	private String userName = null;
	private String varFromChequeBounceRetailer = null;

	/**
	 * This method is used to process the cheque bounce from Retailer .
	 * 
	 * @author Skilrock Technologies throws LMSException
	 */

	@Override
	public String execute() throws Exception {
		System.out.println("i am in Execute");
		System.out.println("Org Type:" + orgType);
		System.out.println("org Name:" + orgName);

		HttpSession session = getRequest().getSession();
		UserInfoBean userInfo = null;
		List<ChequeBean> searchResults = (List<ChequeBean>) session
				.getAttribute("ChequeDetailsRetailer");
		if (searchResults == null) {
			return ERROR;
		}
		// System.out.println(userInfo.getgetRoleName());
		userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
		int agentId = userInfo.getUserId();
		int userOrgID = userInfo.getUserOrgId();
		int orgId = Integer.parseInt(orgName);//ReportUtility.getOrgIdFromOrgName(orgName, "RETAILER");

		System.out.println("oRG Id:" + orgId);

		 
		Connection conn = null;
		PreparedStatement preState = null;
		PreparedStatement LMSTranspreState = null;
		Statement st = null;
		PreparedStatement preState2 = null;
		PreparedStatement preState3 = null;
		PreparedStatement preState4 = null;
		PreparedStatement preState5 = null;
		PreparedStatement preState6 = null;
		PreparedStatement preState7 = null;
		try {
			conn = DBConnect.getConnection();
			// conn=DBConnect.getConnection();
			conn.setAutoCommit(false);

			// String query = QueryManager.getST5CashRetailerTransactionQuery();
			String query = QueryManager.insertInAgentTransactionMaster();
			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			preState = conn.prepareStatement(query);
			LMSTranspreState = conn.prepareStatement(queryLMSTrans);
			// String query1="INSERT INTO
			// st_lms_bo_sale_chq(transaction_id,agent_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt)
			// VALUES (?,?,?,?,?,?,?)";

			// String query2="INSERT INTO st_lms_bo_receipts_trn_mapping
			// (id,transaction_id) VALUES (?,?)";
			// String query3="INSERT INTO st_lms_bo_receipts
			// (receipt_type,agent_org_id) VALUES (?,?)";

			String query1 = QueryManager.getST5AGENTSaleChequeQuery();
			String query2 = QueryManager.insertAgentReceiptTrnMapping();
			// String query3=QueryManager.getST5AGENTReceiptIdQuery();
			String query4 = "INSERT INTO st_lms_agent_sale_chq(transaction_id,agent_user_id,retailer_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt,transaction_type,agent_org_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
			String query5 = "insert into st_lms_agent_debit_note(transaction_id,retailer_org_id,amount,transaction_type,remarks,agent_user_id,agent_org_id) values(?,?,?,?,?,?,?)";
			// java.util.Date current_date=new java.util.Date();
			// java.sql.Date CURRENT_DATE=new
			// java.sql.Date(current_date.getTime());
			for (int i = 0; i < searchResults.size(); i++) {

				LMSTranspreState.setString(1, "AGENT");
				LMSTranspreState.executeUpdate();

				ResultSet rs = LMSTranspreState.getGeneratedKeys();
				rs.next();
				transaction_id = rs.getLong(1);

				preState.setLong(1, transaction_id);
				preState.setInt(2, agentId);
				preState.setInt(3, userOrgID);
				preState.setString(4, "RETAILER");
				preState.setInt(5, orgId);
				preState.setString(6, "CHQ_BOUNCE");
				preState.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				/*
				 * preState.setInt(1, agentId); preState.setInt(2, orgId);
				 * preState.setString(3,"CHQ_BOUNCE"); preState.setTimestamp(4,
				 * new java.sql.Timestamp(new java.util.Date() .getTime()));
				 */
				// preState.setDate(4,CURRENT_DATE);
				preState.executeUpdate();

				LMSTranspreState.clearParameters();
				LMSTranspreState.setString(1, "AGENT");
				LMSTranspreState.executeUpdate();
				rs = LMSTranspreState.getGeneratedKeys();
				rs.next();
				long transaction_id1 = rs.getLong(1);

				preState.setLong(1, transaction_id1);
				preState.setInt(2, agentId);
				preState.setInt(3, userOrgID);
				preState.setString(4, "RETAILER");
				preState.setInt(5, orgId);
				preState.setString(6, "DR_NOTE");
				preState.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				/*
				 * preState.setInt(1, agentId); preState.setInt(2, orgId);
				 * preState.setString(3,"DR_NOTE_CASH");
				 * preState.setTimestamp(4, new java.sql.Timestamp(new
				 * java.util.Date() .getTime()));
				 */
				// preState.setDate(4,CURRENT_DATE);
				preState.executeUpdate();

				// DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				// Calendar cal = Calendar.
				// getInstance();
				// dateFormat.setCalendar(cal);

				// Date chqDate =dateFormat.parse(chequeDate);

				// java.sql.Date chequeDate1=getDate(chequeDate);
				// transaction_id=rs.getInt(1);
				preState2 = conn.prepareStatement(query1);

				preState2.setString(1, "CLOSED");
				preState2.setString(2, searchResults.get(i).getChequeNumber());
				preState2.setString(3, searchResults.get(i).getBankName());

				preState2.executeUpdate();

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				dateFormat.setCalendar(cal);
				Date chqDate = dateFormat.parse(chequeDate);

				java.sql.Date chequeDate1 = new java.sql.Date(chqDate.getTime());
				System.out.println("Chq Date" + chequeDate1);
				preState5 = conn.prepareStatement(query4);
				preState5.setLong(1, transaction_id);
				preState5.setInt(2, agentId);
				preState5.setInt(3, orgId);
				preState5.setString(4, chequeNumber);
				preState5.setDate(5, chequeDate1);
				preState5.setString(6, issuePartyname);
				preState5.setString(7, bankName);
				preState5.setDouble(8, chequeAmount);
				preState5.setString(9, "CHQ_BOUNCE");
				preState5.setInt(10, userOrgID);
				preState5.executeUpdate();

				// make the entries into agent debit note table
				preState6 = conn.prepareStatement(query5);
				preState6.setLong(1, transaction_id1);
				preState6.setInt(2, orgId);
				preState6.setDouble(3, chequeBounceCharges);
				preState6.setString(4, "DR_NOTE");
				preState6.setString(5, "cheque bounce charges(" + chequeNumber
						+ ")");
				preState6.setInt(6, agentId);
				preState6.setInt(7, userOrgID);
				preState6.executeUpdate();

				// get auto generated treciept number
				// String getLatestRecieptNumber="SELECT * from
				// st_lms_agent_receipts_gen_mapping where (receipt_type=? or
				// receipt_type=?) and agt_org_id=? ORDER BY generated_id DESC
				// LIMIT 1 ";
				preState7 = conn.prepareStatement(QueryManager
						.getAgentLatestDRNoteNb());
				preState7.setString(1, "DR_NOTE");
				preState7.setString(2, "DR_NOTE_CASH");
				preState7.setInt(3, userOrgID);
				ResultSet recieptRs = preState7.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
						"DR_NOTE", lastRecieptNoGenerated, userInfo
								.getUserType(), userOrgID);

				// insert into receipt master
				preState4 = conn.prepareStatement(QueryManager
						.insertInReceiptMaster());
				preState4.setString(1, "AGENT");
				preState4.executeUpdate();

				ResultSet rs1 = preState4.getGeneratedKeys();
				rs1.next();
				id = rs1.getInt(1);

				// insert into agent reciept table
				preState4 = conn.prepareStatement(QueryManager
						.insertInAgentReceipts());
				preState4.setInt(1, id);
				preState4.setString(2, "DR_NOTE");
				preState4.setInt(3, userOrgID);
				preState4.setInt(4, orgId);
				preState4.setString(5, "RETAILER");
				preState4.setString(6, autoGeneRecieptNo);
				preState4.setTimestamp(7, Util.getCurrentTimeStamp());

				// preState4.setString(1, autoGeneRecieptNo);
				/*
				 * preState4.setString(1, "DR_NOTE"); preState4.setInt(2,
				 * agentId); preState4.setInt(3, orgId);
				 */

				preState4.executeUpdate();

				// insert in trn mapping

				preState3 = conn.prepareStatement(query2);
				preState3.setInt(1, id);
				preState3.setLong(2, transaction_id);
				preState3.executeUpdate();

				preState3 = conn.prepareStatement(query2);
				preState3.setInt(1, id);
				preState3.setLong(2, transaction_id1);
				preState3.executeUpdate();

				/*
				 * // insert into receipt gen mapping String
				 * updateAgtRecieptGenMapping=QueryManager.updateST5AGENTReceiptGenMappimg();
				 * preState8=conn.prepareStatement(updateAgtRecieptGenMapping);
				 * preState8.setInt(1,id);
				 * preState8.setString(2,autoGeneRecieptNo);
				 * preState8.setString(3,"DR_NOTE");
				 * preState8.setInt(4,userOrgID); preState8.executeUpdate();
				 */

				totalPay = totalPay + chequeAmount + chequeBounceCharges;
				session.setAttribute("totalPay", getTotalPay());
				session.setAttribute("Receipt_Id", autoGeneRecieptNo);
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(chequeAmount + chequeBounceCharges, "TRANSACTION", "CHQ_BOUNCE", orgId,
						userOrgID, "RETAILER", 0, conn);
				if(isValid)
					conn.commit();
				else
					throw new LMSException();
				
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
						"CHQ_BOUNCE", chequeAmount + chequeBounceCharges, conn);
				conn.commit();*/
				setVarFromChequeBounceRetailer("Yes");
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportAgent(id, (String) session
						.getAttribute("ROOT_PATH"), userOrgID, userInfo
						.getOrgName());
				
				new CommonFunctions().logoutAnyUserForcefully(orgId);
			}
			return SUCCESS;
		}

		catch (SQLException se) {
			setVarFromChequeBounceRetailer("No");

			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			throw new LMSException(se);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (preState3 != null) {
					preState3.close();
				}
				if (preState3 != null) {
					preState3.close();
				}
				if (preState4 != null) {
					preState4.close();
				}
				if (preState2 != null) {
					preState2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ee) {
				System.out.println("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

	}

	public String getBankName() {
		return bankName;
	}

	public double getChequeAmount() {
		return chequeAmount;
	}

	public double getChequeBounceCharges() {
		return chequeBounceCharges;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public String getIssuePartyname() {
		return issuePartyname;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public double getTotalPay() {
		return totalPay * -1;
	}

	public long getTransaction_id() {
		return transaction_id;
	}

	public String getUserName() {
		return userName;
	}

	public String getVarFromChequeBounceRetailer() {
		return varFromChequeBounceRetailer;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setChequeAmount(double chequeAmount) {
		this.chequeAmount = chequeAmount;
	}

	public void setChequeBounceCharges(double chequeBounceCharges) {
		this.chequeBounceCharges = chequeBounceCharges;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public void setIssuePartyname(String issuePartyname) {
		this.issuePartyname = issuePartyname;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public void setTransaction_id(long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setVarFromChequeBounceRetailer(
			String varFromChequeBounceRetailer) {
		this.varFromChequeBounceRetailer = varFromChequeBounceRetailer;
	}

}
