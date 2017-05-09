package com.skilrock.lms.dge.beans;

public class UssdSubscriberWinningDataBean {
	private String winTransDate;
	private double winAmt;
	private String winWallet;
	private String status;
	public String getWinTransDate() {
		return winTransDate;
	}
	public void setWinTransDate(String winTransDate) {
		this.winTransDate = winTransDate;
	}
	public double getWinAmt() {
		return winAmt;
	}
	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}
	public String getWinWallet() {
		return winWallet;
	}
	public void setWinWallet(String winWallet) {
		this.winWallet = winWallet;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


}
