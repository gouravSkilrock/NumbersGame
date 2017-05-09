package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperCashPaymentBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private String systemUserName;
	private String systemPassword;
	private double cashAmount;
	private int agentOrgId;
	private String agentName;
	private String recieptNo;
	private boolean isSuccess;
	private String errorCode;
	private int userId;
	
	
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
	public double getCashAmount() {
		return cashAmount;
	}
	public void setCashAmount(double cashAmount) {
		this.cashAmount = cashAmount;
	}
	
	public int getAgentOrgId() {
		return agentOrgId;
	}
	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getRecieptNo() {
		return recieptNo;
	}
	public void setRecieptNo(String recieptNo) {
		this.recieptNo = recieptNo;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	

}
