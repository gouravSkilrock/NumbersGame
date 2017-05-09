package com.skilrock.lms.api.beans;

import java.io.Serializable;

public class TicketHeaderInfoBean implements Serializable {

	private String companyName;
	private String isLogoDisplayOnTkt;
	private String ticketType;
	private String errorCode;
	private boolean isSuccess;
	
		
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
	public String getCompanyName() {
		return companyName;
	}
	public String getIsLogoDisplayOnTkt() {
		return isLogoDisplayOnTkt;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setIsLogoDisplayOnTkt(String isLogoDisplayOnTkt) {
		this.isLogoDisplayOnTkt = isLogoDisplayOnTkt;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	
	
}
