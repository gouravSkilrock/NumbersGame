package com.skilrock.lms.beans;

public class ScratchTicketStatusBean {

	
	private String retailerOrgName;
	private String gameName;
	private String bookNumber;
	private int totalTickets;
	private int ticketsSold;
	private int ticketsRemaining;
	public String getRetailerOrgName() {
		return retailerOrgName;
	}
	public void setRetailerOrgName(String retailerOrgName) {
		this.retailerOrgName = retailerOrgName;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getBookNumber() {
		return bookNumber;
	}
	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
	}
	public int getTotalTickets() {
		return totalTickets;
	}
	public void setTotalTickets(int totalTickets) {
		this.totalTickets = totalTickets;
	}
	public int getTicketsSold() {
		return ticketsSold;
	}
	public void setTicketsSold(int ticketsSold) {
		this.ticketsSold = ticketsSold;
	}
	public int getTicketsRemaining() {
		return ticketsRemaining;
	}
	public void setTicketsRemaining(int ticketsRemaining) {
		this.ticketsRemaining = ticketsRemaining;
	}
	
	
	
}
