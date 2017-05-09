package com.skilrock.lms.beans;

import java.io.Serializable;

public class OpenGameBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int bookRemaining = 0;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private String gameStartDate;
	private String gameStatus;
	private int nbrBooks = 0;
	private int nbrTickets = 0;
	private String pwt_end_date = null;
	private String saleEndDate = null;
	private double ticketPrice;

	public int getBookRemaining() {
		return bookRemaining;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public String getGameStartDate() {
		return gameStartDate;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public int getNbrBooks() {
		return nbrBooks;
	}

	public int getNbrTickets() {
		return nbrTickets;
	}

	public String getPwt_end_date() {
		return pwt_end_date;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setBookRemaining(int bookRemaining) {
		this.bookRemaining = bookRemaining;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setGameStartDate(String gameStartDate) {
		this.gameStartDate = gameStartDate;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setNbrBooks(int nbrBooks) {
		this.nbrBooks = nbrBooks;
	}

	public void setNbrTickets(int nbrTickets) {
		this.nbrTickets = nbrTickets;
	}

	public void setPwt_end_date(String pwt_end_date) {
		this.pwt_end_date = pwt_end_date;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

}
