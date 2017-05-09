package com.skilrock.lms.web.userMgmt.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.userMgmt.common.LoginTimeIPValidationHelper;

public class LoginTimeIPValidationAction extends ActionSupport implements ServletResponseAware {

	private static final long serialVersionUID = 1L;

	private HttpServletResponse response = null;

	private int userId;
	private String orgType;
	private String[] retName;
	private String allowedIps;
	private String timing;
	private String status;
	private String type;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String[] getRetName() {
		return retName;
	}

	public void setRetName(String[] retName) {
		this.retName = retName;
	}

	public String getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(String allowedIps) {
		this.allowedIps = allowedIps;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String timeLimitAssign() throws Exception {

		LoginTimeIPValidationHelper helper = new LoginTimeIPValidationHelper();
		if("SU".equals(type)) {
			orgType = orgType.split(",")[0];
		}
		if("MU".equals(type)) {
			orgType = orgType.split(",")[1];
			/*
			int length = retName.length;
			for(int i=0; i<length; i++)
				retName[i] = retName[i].split("~")[0];
			*/
			if(orgType.equals("AGENT") || orgType.equals("RETAILER"))
				retName = helper.getAgentWiseOrRetWiseData(retName, orgType);
		}

		String[] timeLimit = timing.split("_");
		helper.updateUserTimeLimitData(retName, null, timeLimit);

		return SUCCESS;
	}

	public void getUserIPTimeLimit() throws Exception {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		LoginTimeIPValidationHelper helper = new LoginTimeIPValidationHelper();
		String timeLimitData = helper.getUserIPTimeLimitByUserId(userId);
		System.out.println("timeLimitData - "+timeLimitData);
		out.print(timeLimitData);
		out.flush();
		out.close();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
}