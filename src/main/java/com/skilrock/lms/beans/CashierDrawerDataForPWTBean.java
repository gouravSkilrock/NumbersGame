package com.skilrock.lms.beans;

import java.sql.Timestamp;

public class CashierDrawerDataForPWTBean {

	private int userId;
	private String ticketNumber;
	private String gameName;
	private Timestamp claimedTime;
	private Timestamp startDate;
	private Timestamp endDate;
	private double claimedAmount;
	private String cashierName;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Timestamp getClaimedTime() {
		return claimedTime;
	}

	public void setClaimedTime(Timestamp claimedTime) {
		this.claimedTime = claimedTime;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

}
