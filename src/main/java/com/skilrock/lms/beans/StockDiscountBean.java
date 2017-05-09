package com.skilrock.lms.beans;

public class StockDiscountBean {

	private int gameId;
	private int noOfBooks;
	private double price;

	public int getGameId() {
		return gameId;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public double getPrice() {
		return price;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
