package com.skilrock.lms.beans;

public class PWTPaymentsBean {

	private String gameName = null;
	private int gameNo = 0;
	private double netCommAmt = 0.0;
	private double netPWT = 0.0;
	private double totalAmt = 0.0;

	public String getGameName() {
		return gameName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public double getNetCommAmt() {
		return netCommAmt;
	}

	public double getNetPWT() {
		return netPWT;
	}

	public double getTotalAmt() {
		return totalAmt;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setNetCommAmt(double netCommAmt) {
		this.netCommAmt = netCommAmt;
	}

	public void setNetPWT(double netPWT) {
		this.netPWT = netPWT;
	}

	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}

}
