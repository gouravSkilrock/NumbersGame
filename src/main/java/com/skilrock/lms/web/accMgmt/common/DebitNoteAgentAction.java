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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.accMgmt.common.DebitNoteAgentHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.userMgmt.common.CommonFunctions;

public class DebitNoteAgentAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double amount;
	private String partyType;
	private String remarks;
	private HttpServletRequest request;
	private List retList;
	//private String retName;
	private int  orgName;// Org Id

	private String orgNameValue;

	public String debitNoteAgt() throws LMSException {

		// get the agents list
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtOrgId = userBean.getUserOrgId();
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		try {
			retList = new ArrayList();
			stmt = con.createStatement();
			String retListQuery = "select name from st_lms_organization_master where organization_type='RETAILER' and parent_id="
					+ agtOrgId + " order by name";
			ResultSet rs = stmt.executeQuery(retListQuery);
			while (rs.next()) {

				String orgName = rs.getString(TableConstants.SOM_ORG_NAME);
				retList.add(orgName);

			}
			return SUCCESS;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

		finally {
			try {
				con.close();
			} catch (Exception ee) {
				System.out.println("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

	}

	public String debitNoteAgtDesciption() {

		return SUCCESS;
	}

	public String doDebitNoteAgt() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String parentOrgName = userBean.getOrgName();
		int agtOrgId = userBean.getUserOrgId();
		int agtId = userBean.getUserId();
		Connection con = DBConnect.getConnection();
		
		try{
			con.setAutoCommit(false);
		/*int retOrgId = 0;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement LMSTransMaspstmt = null;
		try {
			stmt = con.createStatement();
			con.setAutoCommit(false);

			String getAgentOrgId = "select organization_id from st_lms_organization_master where name='"
					+ retName + "'";
			String updateLMSTransmaster = QueryManager
					.insertInLMSTransactionMaster();
			String updateAgtMaster = QueryManager
					.insertInAgentTransactionMaster();
			// String updateAgtMaster="insert into
			// st_lms_agent_transaction_master(agent_id,retailer_org_id,transaction_type,transaction_date)
			// values(?,?,?,?)";
			String updateDebitNote = "insert into st_lms_agent_debit_note(transaction_id,retailer_org_id,amount,transaction_type,remarks,agent_user_id,agent_org_id) values(?,?,?,?,?,?,?)";
			// String updateAgtReciepts="insert into
			// st_lms_agent_receipts(receipt_type,agent_id,retailer_org_id)
			// values(?,?,?)";
			// String updateAgtRecieptmapping="insert into
			// st_lms_agent_receipts_trn_mapping(id,transaction_id)
			// values(?,?)";
			String updateAgtRecieptGenMapping = QueryManager
					.updateST5AGENTReceiptGenMappimg();

			ResultSet rs = stmt.executeQuery(getAgentOrgId);
			while (rs.next()) {
				retOrgId = rs.getInt(TableConstants.SOM_ORG_ID);
			}
			if (!DrawGameOfflineHelper.fetchLoginStatus(retOrgId)) {
				// insert into LMS transaction master
				LMSTransMaspstmt = con.prepareStatement(updateLMSTransmaster);
				LMSTransMaspstmt.setString(1, "AGENT");
				LMSTransMaspstmt.executeUpdate();

				ResultSet rs1 = LMSTransMaspstmt.getGeneratedKeys();
				long transaction_id = 0;
				if (rs1.next()) {
					transaction_id = rs1.getLong(1);
				}

				// insert into agent transaction master

				pstmt1 = con.prepareStatement(updateAgtMaster);
				pstmt1.setLong(1, transaction_id);
				pstmt1.setInt(2, agtId);
				pstmt1.setInt(3, agtOrgId);
				pstmt1.setString(4, "RETAILER");
				pstmt1.setInt(5, retOrgId);
				pstmt1.setString(6, "DR_NOTE_CASH");
				pstmt1.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				
				 * pstmt1.setInt(1,agtId); pstmt1.setInt(2,retOrgId);
				 * pstmt1.setString(3,"DR_NOTE_CASH"); pstmt1.setTimestamp(4,
				 * new java.sql.Timestamp(new java.util.Date().getTime()));
				 

				System.out.println(pstmt1);
				pstmt1.executeUpdate();
				System.out.println(pstmt1);

				pstmt = con.prepareStatement(updateDebitNote);
				pstmt.setLong(1, transaction_id);
				pstmt.setInt(2, retOrgId);
				pstmt.setDouble(3, amount);
				pstmt.setString(4, "DR_NOTE_CASH");
				pstmt.setString(5, remarks);
				pstmt.setInt(6, agtId);
				pstmt.setInt(7, agtOrgId);
				pstmt.executeUpdate();

				// get auto generated treciept number
				// String getLatestRecieptNumber="SELECT * from
				// st_lms_agent_receipts where receipt_type=? or receipt_type=?
				// ORDER BY id DESC LIMIT 1 ";
				// String getLatestRecieptNumber="SELECT * from
				// st_lms_agent_receipts_gen_mapping where (receipt_type=? or
				// receipt_type=?) and agt_org_id=? ORDER BY generated_id DESC
				// LIMIT
				// 1";
				pstmt4 = con.prepareStatement(QueryManager
						.getAgentLatestDRNoteNb());
				pstmt4.setString(1, "DR_NOTE_CASH");
				pstmt4.setString(2, "DR_NOTE");
				pstmt4.setInt(3, agtOrgId);
				ResultSet recieptRs = pstmt4.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
						"DR_NOTE_CASH", lastRecieptNoGenerated, userBean
								.getUserType(), agtOrgId);

				// insert in receipt master

				pstmt2 = con.prepareStatement(QueryManager
						.insertInReceiptMaster());
				pstmt2.setString(1, "AGENT");
				pstmt2.executeUpdate();

				ResultSet rs2 = pstmt2.getGeneratedKeys();
				int id = 0;
				if (rs2.next()) {
					id = rs2.getInt(1);
				}

				// insert into reciept table

				pstmt2 = con.prepareStatement(QueryManager
						.insertInAgentReceipts());

				pstmt2.setInt(1, id);
				pstmt2.setString(2, "DR_NOTE_CASH");
				pstmt2.setInt(3, agtOrgId);
				pstmt2.setInt(4, retOrgId);
				pstmt2.setString(5, "RETAILER");
				pstmt2.setString(6, autoGeneRecieptNo);

				
				 * pstmt2.setString(1,"DR_NOTE_CASH"); pstmt2.setInt(2, agtId);
				 * pstmt2.setInt(3, retOrgId);
				 

				pstmt2.executeUpdate();

				// insert into reciept transaction mapping
				pstmt3 = con.prepareStatement(QueryManager
						.insertAgentReceiptTrnMapping());
				pstmt3.setInt(1, id);
				pstmt3.setLong(2, transaction_id);
				pstmt3.executeUpdate();

				
				 * // insert into receipt gen mapping
				 * pstmt5=con.prepareStatement(updateAgtRecieptGenMapping);
				 * pstmt5.setInt(1,id); pstmt5.setString(2,autoGeneRecieptNo);
				 * pstmt5.setString(3,"DR_NOTE_CASH");
				 * pstmt5.setInt(4,agtOrgId); pstmt5.executeUpdate();
				 

				// update organization details of retailer
				OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
						"DR_NOTE_CASH", amount, con);*/
			DebitNoteAgentHelper agentHelper=new DebitNoteAgentHelper();
			 String autoGeneRecieptNoAndId= agentHelper.doDebitNoteAgtHelper(orgName, amount, remarks, agtOrgId, agtId, userBean.getUserType(), con);
			if("LOGIN".equals(autoGeneRecieptNoAndId)){
				session.setAttribute("amount", autoGeneRecieptNoAndId);
				return SUCCESS;
			}
			 String[] autoGeneReceipt=autoGeneRecieptNoAndId.split("#");
				String autoGeneRecieptNo=autoGeneReceipt[0];
				int id=Integer.parseInt(autoGeneReceipt[1]);
			
				con.commit();
				session.setAttribute("amount", amount);
				GraphReportHelper graphReportHelper = new GraphReportHelper();

				graphReportHelper.createTextReportAgent(id, (String) session
						.getAttribute("ROOT_PATH"), agtOrgId, parentOrgName);
				System.out
						.println("AAAAAAAGGGGGGGGGGGGTttttttttttttttttttttttttttttttttt");
				
				new CommonFunctions().logoutAnyUserForcefully(orgName);
			/*} else {
				session.setAttribute("amount", "LOGIN");
			}*/
			return SUCCESS;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return null;

	}

	public double getAmount() {
		return amount;
	}

	public String getPartyType() {
		return partyType;
	}

	public String getRemarks() {
		return remarks;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public List getRetList() {
		return retList;
	}



	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setRetList(List retList) {
		this.retList = retList;
	}

	

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public int getOrgName() {
		return orgName;
	}

	public void setOrgName(int orgName) {
		this.orgName = orgName;
	}


	public String getOrgNameValue() {
		return orgNameValue;
	}

	public void setOrgNameValue(String orgNameValue) {
		this.orgNameValue = orgNameValue;
	}

}
