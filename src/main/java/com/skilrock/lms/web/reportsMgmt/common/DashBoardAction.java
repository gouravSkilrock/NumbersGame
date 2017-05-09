package com.skilrock.lms.web.reportsMgmt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.DashBoardAgentHelper;

public class DashBoardAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int agentOrgId;

	private String[] balance;
	Log logger = LogFactory.getLog(DashBoardAction.class);

	private String[] orgStatus;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String[] retOrgId;

	public String fetchMenuData() throws LMSException {
		DashBoardAgentHelper helper = new DashBoardAgentHelper();
		HttpSession session = request.getSession();
		String countryDeployed = Utility.getPropertyValue("COUNTRY_DEPLOYED");
		
		if("BENIN".equals(countryDeployed))
			session.setAttribute("DashBoardData", helper.fetchMenuDataDaysLimit(agentOrgId));
		else
			session.setAttribute("DashBoardData", helper.fetchMenuData(agentOrgId));
		
		return SUCCESS;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public String[] getBalance() {
		return balance;
	}

	public String[] getOrgStatus() {
		return orgStatus;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String[] getRetOrgId() {
		return retOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setBalance(String[] balance) {
		this.balance = balance;
	}

	public void setOrgStatus(String[] orgStatus) {
		this.orgStatus = orgStatus;
	}

	public void setRetOrgId(String[] retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public String updateAgentData() throws LMSException {
		DashBoardAgentHelper helper = new DashBoardAgentHelper();
		HttpSession session = request.getSession();
		System.out.println(retOrgId + "**************Test****");
		System.out.println("DashBoard Data:*agentOrgId**" + agentOrgId
				+ "***retOrgId***" + retOrgId + "***balance****" + balance
				+ "******");
		session.setAttribute("DashBoardData", helper.updateAgentData(
				agentOrgId, retOrgId, balance, orgStatus,
				(UserInfoBean) session.getAttribute("USER_INFO"), request
						.getRemoteAddr()));

		return SUCCESS;
	}

}
