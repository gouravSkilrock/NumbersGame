package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class ReprintBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isPwt;
	private String ticketNumber;
    private int gameId;
	public String getTicketNumber() {
		return ticketNumber;
	}

	public boolean isPwt() {
		return isPwt;
	}

	public void setPwt(boolean isPwt) {
		this.isPwt = isPwt;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

}
