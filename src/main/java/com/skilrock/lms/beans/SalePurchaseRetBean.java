package com.skilrock.lms.beans;

import java.sql.Timestamp;

public class SalePurchaseRetBean {

	private double agtpurchPrice = 0.0;
	private String bookNo;
	private double comm;
	private int gameId;
	private String gameName;
	private double mrp;
	private int remTickets;
	private double retpurchPrice = 0.0;
	private int soldTickets;
	private Timestamp transactionDate;

	public double getAgtpurchPrice() {
		return agtpurchPrice;
	}

	public String getBookNo() {
		return bookNo;
	}

	public double getComm() {
		return comm;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public double getMrp() {
		return mrp;
	}

	public int getRemTickets() {
		return remTickets;
	}

	public double getRetpurchPrice() {
		return retpurchPrice;
	}

	public int getSoldTickets() {
		return soldTickets;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setAgtpurchPrice(double agtpurchPrice) {
		this.agtpurchPrice = agtpurchPrice;
	}

	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}

	public void setComm(double comm) {
		this.comm = comm;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public void setRemTickets(int remTickets) {
		this.remTickets = remTickets;
	}

	public void setRetpurchPrice(double retpurchPrice) {
		this.retpurchPrice = retpurchPrice;
	}

	public void setSoldTickets(int soldTickets) {
		this.soldTickets = soldTickets;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

}
