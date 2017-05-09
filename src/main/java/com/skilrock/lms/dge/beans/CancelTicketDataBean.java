package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class CancelTicketDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private String fromDate;
	private long ticketNumber;

	public CancelTicketDataBean() {
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public long getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(long ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
}