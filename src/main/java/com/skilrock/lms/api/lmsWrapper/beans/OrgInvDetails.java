package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class OrgInvDetails implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String invModelId;
	private String serialNo;

	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getInvModelId() {
		return invModelId;
	}
	public void setInvModelId(String invModelId) {
		this.invModelId = invModelId;
	}
}
