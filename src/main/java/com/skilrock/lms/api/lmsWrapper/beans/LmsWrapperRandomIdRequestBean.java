package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperRandomIdRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String systemUserName;
	private String systemUserPassword;
	private LmsWrapperUserIdMappingBean lmsWrapperUserIdMappingBean;
	public String getSystemUserName() {
		return systemUserName;
	}
	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}
	public String getSystemUserPassword() {
		return systemUserPassword;
	}
	public void setSystemUserPassword(String systemUserPassword) {
		this.systemUserPassword = systemUserPassword;
	}
	public LmsWrapperUserIdMappingBean getLmsWrapperUserIdMappingBean() {
		return lmsWrapperUserIdMappingBean;
	}
	public void setLmsWrapperUserIdMappingBean(
			LmsWrapperUserIdMappingBean lmsWrapperUserIdMappingBean) {
		this.lmsWrapperUserIdMappingBean = lmsWrapperUserIdMappingBean;
	}
}
