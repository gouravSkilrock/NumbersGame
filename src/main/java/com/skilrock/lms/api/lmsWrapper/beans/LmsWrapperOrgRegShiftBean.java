package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class LmsWrapperOrgRegShiftBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LmsWrapperOrgRegistrationBean orgRegistrationBean;
	private LmsWrapperUserRegistrationBean userRegistrationBean;
	private ArrayList<LmsWrapperInventoryDetailBean> invDeatilBeanList;
	private String orgType;
	private boolean isSuccess;
	private String errorCode;
	private String systemUserName;
	private String systemPassword;
	private String orgName;
	public LmsWrapperOrgRegistrationBean getOrgRegistrationBean() {
		return orgRegistrationBean;
	}
	public void setOrgRegistrationBean(
			LmsWrapperOrgRegistrationBean orgRegistrationBean) {
		this.orgRegistrationBean = orgRegistrationBean;
	}
	public LmsWrapperUserRegistrationBean getUserRegistrationBean() {
		return userRegistrationBean;
	}
	public void setUserRegistrationBean(
			LmsWrapperUserRegistrationBean userRegistrationBean) {
		this.userRegistrationBean = userRegistrationBean;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
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
	
	public String getSystemUserName() {
		return systemUserName;
	}
	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}
	public String getSystemPassword() {
		return systemPassword;
	}
	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}
	public ArrayList<LmsWrapperInventoryDetailBean> getInvDeatilBeanList() {
		return invDeatilBeanList;
	}
	public void setInvDeatilBeanList(
			ArrayList<LmsWrapperInventoryDetailBean> invDeatilBeanList) {
		this.invDeatilBeanList = invDeatilBeanList;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}
