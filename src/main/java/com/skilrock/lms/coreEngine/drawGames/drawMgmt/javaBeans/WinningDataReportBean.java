package com.skilrock.lms.coreEngine.drawGames.drawMgmt.javaBeans;

import java.io.Serializable;

public class WinningDataReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private int drawId;
	private int eventId;
	private String drawDateTime;
	private String ticketNumber;
	private String fullTicketNumber;
	private String stakeData;
	private double purchaseAmount;
	private double winningAmount;

	public WinningDataReportBean() {
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getFullTicketNumber() {
		return fullTicketNumber;
	}

	public void setFullTicketNumber(String fullTicketNumber) {
		this.fullTicketNumber = fullTicketNumber;
	}

	public String getStakeData() {
		return stakeData;
	}

	public void setStakeData(String stakeData) {
		this.stakeData = stakeData;
	}

	public double getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}
}