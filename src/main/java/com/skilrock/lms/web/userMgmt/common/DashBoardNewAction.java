package com.skilrock.lms.web.userMgmt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.DashBoardNewHelper;

public class DashBoardNewAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = -4042875110032819684L;
	Log logger = LogFactory.getLog(DashBoardNewAction.class);

	private HttpServletRequest request;
	private HttpServletResponse response;

	private int agentOrgId;
	private String[] balance;
	private String[] xclAmt;
	private String[] xclDays;
	private String[] orgStatus;
	private String[] retOrgId;

	public String fetchMenuData() throws LMSException {
		DashBoardNewHelper helper = new DashBoardNewHelper();
		HttpSession session = request.getSession();
		session.setAttribute("DashBoardNewData", helper.fetchMenuData());
		return SUCCESS;
	}

	public String updateAgentData() throws LMSException {
		DashBoardNewHelper helper = new DashBoardNewHelper();
		HttpSession session = request.getSession();
		//System.out.println((String)request.getHeader("User-Agent"));
		
		session.setAttribute("DashBoardNewData", helper.updateAgentData(
				retOrgId, balance, orgStatus, (UserInfoBean) session
						.getAttribute("USER_INFO"), xclAmt, xclDays));

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

	public String[] getXclAmt() {
		return xclAmt;
	}

	public void setXclAmt(String[] xclAmt) {
		this.xclAmt = xclAmt;
	}

	public String[] getXclDays() {
		return xclDays;
	}

	public void setXclDays(String[] xclDays) {
		this.xclDays = xclDays;
	}

}
