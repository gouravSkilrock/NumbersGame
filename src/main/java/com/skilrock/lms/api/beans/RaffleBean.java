package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;

public class RaffleBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ticketNumber;
	private String gameCode;
	private String purchaseTime;
	private String drawDateTime;
	private String message;
	public String getTicketNumber() {
		return ticketNumber;
	}
	public String getGameCode() {
		return gameCode;
	}
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

	
}
