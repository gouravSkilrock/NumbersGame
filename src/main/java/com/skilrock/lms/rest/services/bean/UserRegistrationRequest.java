package com.skilrock.lms.rest.services.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.skilrock.lms.beans.RetailerRegistrationBean;

public class UserRegistrationRequest extends RetailerRegistrationBean{
	

	private int requestId;
	private String partyName;
	@NotNull(message="User type should not be null")
	@Size(min=1,message="User Type should not be empty")
	private String userType;
	@Min(message="Parent User ID not be empty or null",value=1 )
	private int parentUserId;
	
	public int getParentUserId() {
		return parentUserId;
	}
	public void setParentUserId(int parentUserId) {
		this.parentUserId = parentUserId;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

}
