package com.skilrock.lms.keba.loginMgmt.javaBeans;

import java.io.Serializable;

public class FFTLoginBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean isSuccess;
	private String errorMesssage;
	private double balance;
	private String currentDate;
	private String currentTime;
	
	public FFTLoginBean() {
	}

	

	public String getErrorMesssage() {
		return errorMesssage;
	}

	public void setErrorMesssage(String errorMesssage) {
		this.errorMesssage = errorMesssage;
	}

	


	public boolean getIsSuccess() {
		return isSuccess;
	}



	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}



	public double getBalance() {
		return balance;
	}



	public void setBalance(double balance) {
		this.balance = balance;
	}


	

	public String getCurrentDate() {
		return currentDate;
	}



	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}



	public String getCurrentTime() {
		return currentTime;
	}



	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}



	
	
	
}