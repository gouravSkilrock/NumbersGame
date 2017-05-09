package com.skilrock.lms.api.beans;

import java.io.Serializable;

public class TPUserBalaanceBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String userName;
	private double balance;
	private boolean isSuccess;
	private String errorCode;
	public String getUserName() {
		return userName;
	}
	public double getBalance() {
		return balance;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setBalance(double balance) {
		this.balance = balance;
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
	
	

}
