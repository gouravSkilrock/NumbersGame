package com.skilrock.lms.embedded.loginMgmt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.loginMgmt.common.AgentAuthenticationHelper;

public class AgentAuthenticationAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(AgentAuthenticationAction.class);

	private HttpServletRequest request;
	private HttpServletResponse response;

	private String userName;
	private String password;
	private String deviceType;
	private String brandName;
	private String modelName;
	private String terminalId; // -- LAST 8 DIGITS OF ACTUAL SERIAL NO.

	public void authenticateAgentAndTerminalId() throws Exception {
		String returnMsg = null;
		if (userName != null && password != null && deviceType != null && brandName != null && modelName != null && terminalId != null) {
			returnMsg = new AgentAuthenticationHelper().authenticateAgentAndTerminalId(userName, password, deviceType, brandName, modelName, terminalId);
		} else {
			returnMsg = "ErrorMsg:Invalid Data Supplied|";
		}
		response.getOutputStream().write(returnMsg.getBytes());
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

}
