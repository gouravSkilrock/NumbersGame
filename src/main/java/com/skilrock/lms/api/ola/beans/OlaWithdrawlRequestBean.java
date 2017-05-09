package com.skilrock.lms.api.ola.beans;

import java.util.ArrayList;

public class OlaWithdrawlRequestBean {

	private String userName;
	private String password;
	private ArrayList<OlaWithdrwalDetailsBean> withdrawalDetailList ;
	private int errorCode ;
	private String errorMsg;
	private boolean isSuccess;
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	public ArrayList<OlaWithdrwalDetailsBean> getWithdrawalDetailList() {
		return withdrawalDetailList;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setWithdrawalDetailList(
			ArrayList<OlaWithdrwalDetailsBean> withdrawalDetailList) {
		this.withdrawalDetailList = withdrawalDetailList;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	
}
