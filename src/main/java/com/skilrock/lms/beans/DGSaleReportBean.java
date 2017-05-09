package com.skilrock.lms.beans;

public class DGSaleReportBean {
	private String gameName;
	private double sumRefundMrp;
	private double sumSaleMrp;
	private String userName;

	public String getGameName() {
		return gameName;
	}

	public double getSumRefundMrp() {
		return sumRefundMrp;
	}

	public double getSumSaleMrp() {
		return sumSaleMrp;
	}

	public String getUserName() {
		return userName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setSumRefundMrp(double sumRefundMrp) {
		this.sumRefundMrp = sumRefundMrp;
	}

	public void setSumSaleMrp(double sumSaleMrp) {
		this.sumSaleMrp = sumSaleMrp;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
