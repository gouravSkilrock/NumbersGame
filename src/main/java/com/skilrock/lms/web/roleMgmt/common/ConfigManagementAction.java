package com.skilrock.lms.web.roleMgmt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.roleMgmt.common.ConfigManagementHelper;

public class ConfigManagementAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(ConfigManagementAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] code;
	private String[] description;
	private String[] name;
	HttpServletRequest request;
	private String[] status;
	private String[] value;

	public String fetchProperty() throws LMSException {
		ConfigManagementHelper configMgmtHelper = new ConfigManagementHelper();
		HttpSession session = getRequest().getSession();
		session.setAttribute("CONFIG_LIST", configMgmtHelper.fetchProperty());
		return SUCCESS;
	}

	public String[] getCode() {
		return code;
	}

	public String[] getDescription() {
		return description;
	}

	public String[] getName() {
		return name;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String[] getStatus() {
		return status;
	}

	public String[] getValue() {
		return value;
	}

	public void setCode(String[] code) {
		this.code = code;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public void setValue(String[] value) {
		this.value = value;
	}

	public String updateProperty() throws LMSException {
		ConfigManagementHelper configMgmtHelper = new ConfigManagementHelper();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		configMgmtHelper.updateProperty(userBean, code, status, value,
				description);
		return SUCCESS;
	}

}