package com.skilrock.lms.embedded.scratchService.reportsMgmt.common;

public class ConsolidatedBean {

	private double closingLedBal = 0.0;
	private double netPayable = 0.0;
	private double netPayments = 0.0;
	private double netPWT = 0.0;
	private double openingLedBal = 0.0;
	private double purchPrcRemStk = 0.0;
	private String userName = null;
	private double weeklyClosingBal = 0.0;
	private double weeklyOpeningBal = 0.0;

	public double getClosingLedBal() {
		return closingLedBal;
	}

	public double getNetPayable() {
		return netPayable;
	}

	public double getNetPayments() {
		return netPayments;
	}

	public double getNetPWT() {
		return netPWT;
	}

	public double getOpeningLedBal() {
		return openingLedBal;
	}

	public double getPurchPrcRemStk() {
		return purchPrcRemStk;
	}

	public String getUserName() {
		return userName;
	}

	public double getWeeklyClosingBal() {
		return weeklyClosingBal;
	}

	public double getWeeklyOpeningBal() {
		return weeklyOpeningBal;
	}

	public void setClosingLedBal(double closingLedBal) {
		this.closingLedBal = closingLedBal;
	}

	public void setNetPayable(double netPayable) {
		this.netPayable = netPayable;
	}

	public void setNetPayments(double netPayments) {
		this.netPayments = netPayments;
	}

	public void setNetPWT(double netPWT) {
		this.netPWT = netPWT;
	}

	public void setOpeningLedBal(double openingLedBal) {
		this.openingLedBal = openingLedBal;
	}

	public void setPurchPrcRemStk(double purchPrcRemStk) {
		this.purchPrcRemStk = purchPrcRemStk;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setWeeklyClosingBal(double weeklyClosingBal) {
		this.weeklyClosingBal = weeklyClosingBal;
	}

	public void setWeeklyOpeningBal(double weeklyOpeningBal) {
		this.weeklyOpeningBal = weeklyOpeningBal;
	}

}
