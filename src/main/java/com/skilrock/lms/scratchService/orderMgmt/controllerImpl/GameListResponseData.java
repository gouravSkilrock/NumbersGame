package com.skilrock.lms.scratchService.orderMgmt.controllerImpl;

public class GameListResponseData {

	private int gameId;
	private String gameName;
	private int noOfTktsPerBook;
	private double ticketPrice;
	
	
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public int getNoOfTktsPerBook() {
		return noOfTktsPerBook;
	}
	public void setNoOfTktsPerBook(int noOfTktsPerBook) {
		this.noOfTktsPerBook = noOfTktsPerBook;
	}
	public double getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
}
