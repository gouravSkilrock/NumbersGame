package com.skilrock.lms.beans;

public class SalePurchaseBean {
	private double comm;
	private int gameId;
	private String gameName;
	private double mrp;
	private double purchprice;
	private double salePrice;
	private String transactionType;

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

	public double getPurchprice() {
		return purchprice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public String getTransactionType() {
		return transactionType;
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

	public void setPurchprice(double purchprice) {
		this.purchprice = purchprice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
