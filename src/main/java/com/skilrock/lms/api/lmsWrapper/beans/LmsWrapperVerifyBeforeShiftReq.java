package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;

public class LmsWrapperVerifyBeforeShiftReq implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sysUserName;
	private String sysUserPass;
	private List<LmsWrapperVerifyOrgBean> orgListToValidate;
	
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
	public String getSysUserPass() {
		return sysUserPass;
	}
	public void setSysUserPass(String sysUserPass) {
		this.sysUserPass = sysUserPass;
	}
	public List<LmsWrapperVerifyOrgBean> getOrgListToValidate() {
		return orgListToValidate;
	}
	public void setOrgListToValidate(List<LmsWrapperVerifyOrgBean> orgListToValidate) {
		this.orgListToValidate = orgListToValidate;
	}
	
	
	
}
