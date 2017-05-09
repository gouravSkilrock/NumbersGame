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

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.accMgmt.common.DebitNoteAgentHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.DebitNoteBoHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.userMgmt.common.CommonFunctions;

public class DebitNoteBOAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List agentList;
	private String agentName;
	private double amount;
	private String partyType;
	private String remarks;
	private HttpServletRequest request;
	private String reason;
	private String gameName;
	private String agentNameValue;
    private String orgType;
    private String retNameValue;
    private String retOrgName;
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String debitNoteBo() throws LMSException {

		// get the agents list

		//Connection con = null;
		//Statement stmt = null;
		String depCountry=null;
		List<String> reasonList =null;
		try {
			/*con = DBConnect.getConnection();
			agentList = new ArrayList();
			stmt = con.createStatement();
			String agentListQuery = "select name from st_lms_organization_master where organization_type='AGENT' order by name";
			ResultSet rs = stmt.executeQuery(agentListQuery);
			while (rs.next()) {
				String orgName = rs.getString(TableConstants.SOM_ORG_NAME);
				agentList.add(orgName);
			}*/
			reasonList = new ArrayList<String>();
			reasonList.add("OTHERS");
			depCountry=(String)request.getSession().getServletContext().getAttribute("COUNTRY_DEPLOYED");
			if(depCountry.equals("NIGERIA")){
				reasonList.add("DR_WRONG_RECEIPT_ON_CASH");
				reasonList.add("DR_WRONG_RECEIPT_ON_BD");
			}
			
			boolean isScratch = "YES"
					.equalsIgnoreCase((String) ServletActionContext
							.getServletContext().getAttribute("IS_SCRATCH"));
			if (isScratch) {
				reasonList.add("AGAINST_LOOSE_BOOKS_RETURN");
			}
			boolean isCS = "YES"
				.equalsIgnoreCase((String) ServletActionContext
						.getServletContext().getAttribute("IS_CS"));
			if (isCS) {
				reasonList.add("AGAINST_FAULTY_RECHARGE_VOUCHERS");
			}
			
			request.getSession().setAttribute("REASON_LIST", reasonList);
			return SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
		finally {
			try {
				//con.close();
			} catch (Exception ee) {
				System.out.println("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

	}

	public String debitNoteBoDesciption() {

		return SUCCESS;
	}

	public String doDebitNoteBo() throws LMSException {
		Connection con =null;
		UserInfoBean userBean = null,agentInfoBean=null;
		String parentOrgName = null;
		int userOrgID = 0;
		try {
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			HttpSession session = getRequest().getSession();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			parentOrgName = userBean.getOrgName();
			userOrgID = userBean.getUserOrgId();
			
			DebitNoteBoHelper boHelper = new DebitNoteBoHelper();	
			String autoGeneRecieptNoAndId = boHelper.doDebitNoteBoHelper(
					Integer.parseInt(agentName), amount, remarks, reason, userOrgID, userBean
							.getUserId(), userBean.getUserType(), con);
			int agentOrgId=0;
            if("RETAILER".equalsIgnoreCase(orgType)){
            	int retOrgId=Integer.parseInt(retOrgName);
            	agentOrgId=Integer.parseInt(agentName);
            	agentInfoBean=CommonMethods.fetchUserData(agentOrgId);
            	int agtId=agentInfoBean.getUserId();
                DebitNoteAgentHelper agentHelper=new DebitNoteAgentHelper();
                autoGeneRecieptNoAndId=agentHelper.doDebitNoteAgtHelper(retOrgId,amount,remarks,agentOrgId, agtId, "AGENT", con);
            }
			String[] autoGeneReceipt = autoGeneRecieptNoAndId.split("#");
			String autoGeneRecieptNo = autoGeneReceipt[0];
			int id = Integer.parseInt(autoGeneReceipt[1]);

			con.commit();
			session.setAttribute("amount", amount);
			GraphReportHelper graphReportHelper = new GraphReportHelper();
            if("AGENT".equalsIgnoreCase(orgType)){
			graphReportHelper.createTextReportBO(id, parentOrgName, userOrgID,
					(String) session.getAttribute("ROOT_PATH"));
            }else{
            	graphReportHelper.createTextReportAgent(id,(String) session.getAttribute("ROOT_PATH"), agentOrgId, agentInfoBean.getOrgName());
            }
			new CommonFunctions().logoutAnyUserForcefully(Integer.parseInt(agentName));
			return SUCCESS;

		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return null;

	}

	public List getAgentList() {
		return agentList;
	}

	public String getAgentName() {
		return agentName;
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

	public void setAgentList(List agentList) {
		this.agentList = agentList;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
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

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getAgentNameValue() {
		return agentNameValue;
	}

	public void setAgentNameValue(String agentNameValue) {
		this.agentNameValue = agentNameValue;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setRetNameValue(String retNameValue) {
		this.retNameValue = retNameValue;
	}

	public String getRetNameValue() {
		return retNameValue;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

}
