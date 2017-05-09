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
 * This class is used to process the cheque bounce .
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChequeBounce extends ActionSupport implements ServletRequestAware {
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
	private String orgName = "";
	private String orgType = "AGENT";
	private HttpServletRequest request = null;
	private double totalPay;
	private long transaction_id;
	private String varFromChequeBounce;

	/**
	 * This Method is used to process the cheque bounce process.
	 * 
	 * @author Skilrock Technologies @ throws LMSException
	 * 
	 */

	@Override
	public String execute() throws Exception {
		System.out.println("i am in Execute");
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = null;
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		List<ChequeBean> searchResults = (List<ChequeBean>) session
				.getAttribute("ChequeDetails");

		//String boOrgName = userBean.getOrgName();
		String autoGeneRecieptNo = null;
		int orgId = Integer.parseInt(orgName);//ReportUtility.getOrgIdFromOrgName(orgName, "AGENT");

		 
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
		// added on 06/07/10
		String chequeNumberTransactionId = chequeNumber;
		long newTransactionId = Long.parseLong(chequeNumberTransactionId
				.split(":")[1]);

		// String query1="INSERT INTO
		// st_lms_bo_sale_chq(transaction_id,agent_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt)
		// VALUES (?,?,?,?,?,?,?)";
		// String query2="INSERT INTO st_lms_bo_receipts_trn_mapping
		// (id,transaction_id) VALUES (?,?)";
		// String query3="INSERT INTO st_lms_bo_receipts
		// (receipt_type,agent_org_id) VALUES (?,?)";
		String query1 = QueryManager.getST5BOSaleChequeQuery();
		String query2 = QueryManager.insertBOReceiptTrnMapping();
		// String query3=QueryManager.getST5BOReceiptIdQuery();
		String query4 = "INSERT INTO st_lms_bo_sale_chq(transaction_id,agent_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt,transaction_type) VALUES (?,?,?,?,?,?,?,?)";
		String query5 = "insert into st_lms_bo_debit_note(transaction_id ,agent_org_id,amount,transaction_type,remarks) values(?,?,?,?,?)";

		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			// String query = QueryManager.getST5CashTransactionQuery();
			String query = QueryManager.insertInBOTransactionMaster();
			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			preState = conn.prepareStatement(query);
			LMSTranspreState = conn.prepareStatement(queryLMSTrans);

			// java.util.Date current_date=new java.util.Date();
			// java.sql.Date CURRENT_DATE=new
			// java.sql.Date(current_date.getTime());
			for (int i = 0; i < searchResults.size(); i++) {

				LMSTranspreState.setString(1, "BO");
				LMSTranspreState.executeUpdate();

				ResultSet rs = LMSTranspreState.getGeneratedKeys();
				rs.next();
				transaction_id = rs.getLong(1);

				preState.setLong(1, transaction_id);
				preState.setInt(2, userBean.getUserId());
				preState.setInt(3, userBean.getUserOrgId());
				preState.setString(4, orgType);
				preState.setInt(5, orgId);
				preState.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preState.setString(7, "CHQ_BOUNCE");

				/*
				 * preState.setString(1, orgType); preState.setInt(2, orgId);
				 * preState.setTimestamp(3, new java.sql.Timestamp(new
				 * java.util.Date() .getTime()));
				 * preState.setString(4,"CHQ_BOUNCE");
				 */
				preState.executeUpdate();

				LMSTranspreState.clearParameters();
				LMSTranspreState.setString(1, "BO");
				LMSTranspreState.executeUpdate();
				rs = LMSTranspreState.getGeneratedKeys();

				rs.next();
				long transaction_id1 = rs.getLong(1);

				preState.setLong(1, transaction_id1);
				preState.setInt(2, userBean.getUserId());
				preState.setInt(3, userBean.getUserOrgId());
				preState.setString(4, orgType);
				preState.setInt(5, orgId);
				preState.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preState.setString(7, "DR_NOTE");

				/*
				 * preState.setString(1, orgType); preState.setInt(2, orgId);
				 * preState.setTimestamp(3, new java.sql.Timestamp(new
				 * java.util.Date() .getTime()));
				 * preState.setString(4,"DR_NOTE_CASH");
				 */
				preState.executeUpdate();

				String newQuery1 = query1 + " and transaction_id=?";

				preState2 = conn.prepareStatement(newQuery1);// edited query
				// with txn id

				preState2.setString(1, "CLOSED");
				preState2.setString(2, searchResults.get(i).getChequeNumber()
						.split(":")[0]);
				preState2.setString(3, searchResults.get(i).getBankName());
				preState2.setLong(4, newTransactionId);
				preState2.executeUpdate();
				// System.out.println("new query is for cheque status updation
				// :"+preState2); //amit cp
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				dateFormat.setCalendar(cal);
				Date chqDate = dateFormat.parse(chequeDate);

				java.sql.Date chequeDate1 = new java.sql.Date(chqDate.getTime());
				System.out.println("Chq Date" + chequeDate1);
				preState5 = conn.prepareStatement(query4);
				preState5.setLong(1, transaction_id);
				preState5.setInt(2, orgId);
				preState5.setString(3, chequeNumberTransactionId.split(":")[0]);
				preState5.setDate(4, chequeDate1);
				preState5.setString(5, issuePartyname);
				preState5.setString(6, bankName);
				preState5.setDouble(7, chequeAmount);
				preState5.setString(8, "CHQ_BOUNCE");
				preState5.executeUpdate();

				preState6 = conn.prepareStatement(query5);
				preState6.setLong(1, transaction_id1);
				preState6.setInt(2, orgId);
				preState6.setDouble(3, chequeBounceCharges);
				preState6.setString(4, "DR_NOTE");
				preState6.setString(5, "cheque bounce charges("
						+ chequeNumberTransactionId.split(":")[0] + ")");
				preState6.executeUpdate();

				// get auto generated treciept number
				// String getLatestRecieptNumber="SELECT * from
				// st_bo_receipt_gen_mapping where receipt_type=? or
				// receipt_type=? ORDER BY generated_id DESC LIMIT 1 ";
				preState7 = conn.prepareStatement(QueryManager
						.getBOLatestDRNoteNb());
				preState7.setString(1, "DR_NOTE");
				preState7.setString(2, "DR_NOTE_CASH");
				ResultSet recieptRs = preState7.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo("DR_NOTE",
						lastRecieptNoGenerated, userBean.getUserType());

				// insert into reciept tables

				// insert in receipt master
				preState4 = conn.prepareStatement(QueryManager
						.insertInReceiptMaster());
				preState4.setString(1, "BO");
				preState4.executeUpdate();

				ResultSet rs1 = preState4.getGeneratedKeys();
				rs1.next();
				id = rs1.getInt(1);

				// insert in agent receipts
				preState4 = conn.prepareStatement(QueryManager
						.insertInBOReceipts());
				preState4.setInt(1, id);
				preState4.setString(2, "DR_NOTE");
				preState4.setInt(3, orgId);
				preState4.setString(4, "AGENT");
				preState4.setString(5, autoGeneRecieptNo);
				preState4.setTimestamp(6, Util.getCurrentTimeStamp());

				/*
				 * preState4.setString(1, "DR_NOTE"); preState4.setInt(2,
				 * orgId);
				 */
				preState4.executeUpdate();

				// insert into recpipt mapping table
				preState3 = conn.prepareStatement(query2);
				preState3.setInt(1, id);
				preState3.setLong(2, transaction_id);
				preState3.executeUpdate();

				preState3 = conn.prepareStatement(query2);
				preState3.setInt(1, id);
				preState3.setLong(2, transaction_id1);
				preState3.executeUpdate();

				/*
				 * //insert into recipt gen reciept mapping String
				 * updateBoRecieptGenMapping=QueryManager.updateST5BOReceiptGenMapping();
				 * preState8=conn.prepareStatement(updateBoRecieptGenMapping);
				 * preState8.setInt(1,id);
				 * preState8.setString(2,autoGeneRecieptNo);
				 * preState8.setString(3,"DR_NOTE"); preState8.executeUpdate();
				 */

				totalPay = totalPay + searchResults.get(0).getChequeAmount()
						+ chequeBounceCharges;
				session.setAttribute("Receipt_Id", autoGeneRecieptNo);
				session.setAttribute("totalPay", getTotalPay());
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(searchResults.get(0).getChequeAmount()
						+ chequeBounceCharges, "TRANSACTION", "CHQ_BOUNCE", orgId,
						0, "AGENT", 0, conn);
				if(!isValid)
					throw new LMSException();
					
				/*OrgCreditUpdation.updateCreditLimitForAgent(orgId,
						"CHQ_BOUNCE", searchResults.get(0).getChequeAmount()
								+ chequeBounceCharges, conn);*/
			}
			conn.commit();
			// session.setAttribute("Receipt_Id", autoGeneRecieptNo);

			setVarFromChequeBounce("Yes");
			GraphReportHelper graphReportHelper = new GraphReportHelper();
			// UserInfoBean userBean=null;
			String parentOrgName = null;
			int userOrgID = 0;
			// userBean= (UserInfoBean) session.getAttribute("USER_INFO");
			parentOrgName = userBean.getOrgName();
			userOrgID = userBean.getUserOrgId();
			graphReportHelper.createTextReportBO(id, parentOrgName, userOrgID,
					(String) session.getAttribute("ROOT_PATH"));
			new CommonFunctions().logoutAnyUserForcefully(orgId);
			return SUCCESS;

		}

		catch (SQLException se) {

			setVarFromChequeBounce("No");
			conn.rollback();
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

	public String getVarFromChequeBounce() {
		return varFromChequeBounce;
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

	public void setVarFromChequeBounce(String varFromChequeBounce) {
		this.varFromChequeBounce = varFromChequeBounce;
	}

}