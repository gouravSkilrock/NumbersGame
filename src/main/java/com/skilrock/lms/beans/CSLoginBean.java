package com.skilrock.lms.beans;

public class CSLoginBean {
	private String userName;
	private double balance;
	private int CSRefTxId;
	private int RMSRefTxId;
	private String authCode;
	private String status;
	private int ErrorCode;
	
	public String getUserName() {
		return userName;
	}
	public double getBalance() {
		return balance;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getCSRefTxId() {
		return CSRefTxId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public int getErrorCode() {
		return ErrorCode;
	}
	public void setCSRefTxId(int cSRefTxId) {
		CSRefTxId = cSRefTxId;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public void setErrorCode(int errorCode) {
		ErrorCode = errorCode;
	}
	public int getRMSRefTxId() {
		return RMSRefTxId;
	}
	public String getStatus() {
		return status;
	}
	public void setRMSRefTxId(int rMSRefTxId) {
		RMSRefTxId = rMSRefTxId;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}

