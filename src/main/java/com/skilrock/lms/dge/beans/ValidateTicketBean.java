package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class ValidateTicketBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String datePurchased;
	private String gameName;
	private int gameId;
	private int gameNo;
	private boolean isTicketExpired;
	private boolean isValid;
	private List<String> raflleTicketinDBList;
	private String reprintCount;
	private int retailerId;
	private String status;
    private int dayOfTicket;
	private String ticketNo;

	private String ticketNumInDB;
	private int ticketExpiryPeriod;

	public String getDatePurchased() {
		return datePurchased;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public List<String> getRaflleTicketinDBList() {
		return raflleTicketinDBList;
	}

	public String getReprintCount() {
		return reprintCount;
	}

	public int getRetailerId() {
		return retailerId;
	}

	public String getStatus() {
		return status;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public String getTicketNumInDB() {
		return ticketNumInDB;
	}

	public boolean isTicketExpired() {
		return isTicketExpired;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setDatePurchased(String datePurchased) {
		this.datePurchased = datePurchased;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setRaflleTicketinDBList(List<String> raflleTicketinDBList) {
		this.raflleTicketinDBList = raflleTicketinDBList;
	}

	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}

	public void setRetailerId(int retailerId) {
		this.retailerId = retailerId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicketExpired(boolean isTicketExpired) {
		this.isTicketExpired = isTicketExpired;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public void setTicketNumInDB(String ticketNumInDB) {
		this.ticketNumInDB = ticketNumInDB;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public int getTicketExpiryPeriod() {
		return ticketExpiryPeriod;
	}

	public void setTicketExpiryPeriod(int ticketExpiryPeriod) {
		this.ticketExpiryPeriod = ticketExpiryPeriod;
	}
	
	
	public void setDayOfTicket(int dayOfTicket) {
		this.dayOfTicket = dayOfTicket;
	}

	public int getDayOfTicket() {
		return dayOfTicket;
	}

}