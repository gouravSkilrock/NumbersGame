package com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans;

import java.io.Serializable;

public class ScratchGameDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int gameId;
	private int gameNbr;
	private String gameName;
	private double ticketPrice;
	private String gameDescription;
	private String gameImagePath;

	public int getGameNbr() {
		return gameNbr;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameDescription() {
		return gameDescription;
	}

	public void setGameDescription(String gameDescription) {
		this.gameDescription = gameDescription;
	}

	public String getGameImagePath() {
		return gameImagePath;
	}

	public void setGameImagePath(String gameImagePath) {
		this.gameImagePath = gameImagePath;
	}
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public String toString() {
		return "ScratchGameDataBean [gameId=" + gameId + ", gameNbr=" + gameNbr + ", gameName=" + gameName
				+ ", ticketPrice=" + ticketPrice + ", gameDescription=" + gameDescription + ", gameImagePath="
				+ gameImagePath + "]";
	}

	
}
