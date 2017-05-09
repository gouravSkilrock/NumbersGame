package com.skilrock.lms.beans;

public class StockMrpDiscountBean {

	private int stCountBooks = 0;
	private int stCountTickets = 0;
	private double stDiscPrice = 0.0;
	private String stGameName = null;
	private int stGameNo = 0;
	private double stMrp = 0.0;
	private double stPercent = 0.0;
	private String stTransactionType = null;

	public int getStCountBooks() {
		return stCountBooks;
	}

	public int getStCountTickets() {
		return stCountTickets;
	}

	public double getStDiscPrice() {
		return stDiscPrice;
	}

	public String getStGameName() {
		return stGameName;
	}

	public int getStGameNo() {
		return stGameNo;
	}

	public double getStMrp() {
		return stMrp;
	}

	public double getStPercent() {
		return stPercent;
	}

	public String getStTransactionType() {
		return stTransactionType;
	}

	public void setStCountBooks(int stCountBooks) {
		this.stCountBooks = stCountBooks;
	}

	public void setStCountTickets(int stCountTickets) {
		this.stCountTickets = stCountTickets;
	}

	public void setStDiscPrice(double stDiscPrice) {
		this.stDiscPrice = stDiscPrice;
	}

	public void setStGameName(String stGameName) {
		this.stGameName = stGameName;
	}

	public void setStGameNo(int stGameNo) {
		this.stGameNo = stGameNo;
	}

	public void setStMrp(double stMrp) {
		this.stMrp = stMrp;
	}

	public void setStPercent(double stPercent) {
		this.stPercent = stPercent;
	}

	public void setStTransactionType(String stTransactionType) {
		this.stTransactionType = stTransactionType;
	}

}
