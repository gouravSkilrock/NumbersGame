package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class LmsWrapperOrganizationRegShiftDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LinkedList<LmsWrapperOrgRegShiftBean> orgRegShiftBean;
	private String agentName;
	private boolean isSuccess;
	private String errorCode;
	private String userName;
	private String password;
	
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
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
	public LinkedList<LmsWrapperOrgRegShiftBean> getOrgRegShiftBean() {
		return orgRegShiftBean;
	}
	public void setOrgRegShiftBean(
			LinkedList<LmsWrapperOrgRegShiftBean> orgRegShiftBean) {
		this.orgRegShiftBean = orgRegShiftBean;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
	
}
