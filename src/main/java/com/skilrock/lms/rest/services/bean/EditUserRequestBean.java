package com.skilrock.lms.rest.services.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditUserRequestBean {

	
	private int requestId;
	@NotNull(message="Please mention TpUserId")
	@Size(min=1,message="TpUserId should not be empty")
	private String tpUserId;
	private String mobileNo;
	private String emailId;
	private String status;
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public String getTpUserId() {
		return tpUserId;
	}
	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
