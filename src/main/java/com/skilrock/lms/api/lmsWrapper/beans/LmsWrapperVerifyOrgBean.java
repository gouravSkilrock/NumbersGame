package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;

public class LmsWrapperVerifyOrgBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String orgCode;
	private String orgName;
	private String userName;
	private List<OrgInvDetails> orgInvList;
	private String idType;
	private String idNumber;
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<OrgInvDetails> getOrgInvList() {
		return orgInvList;
	}
	public void setOrgInvList(List<OrgInvDetails> orgInvList) {
		this.orgInvList = orgInvList;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
}
