package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;

public class GameTypeReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameTypeId;
	private String gameTypeName;
	private int noOfSale;
	private double saleAmount;
	private int noOfWinning;
	private double winningAmount;

	public GameTypeReportBean() {
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
	}

	public int getNoOfSale() {
		return noOfSale;
	}

	public void setNoOfSale(int noOfSale) {
		this.noOfSale = noOfSale;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public int getNoOfWinning() {
		return noOfWinning;
	}

	public void setNoOfWinning(int noOfWinning) {
		this.noOfWinning = noOfWinning;
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}
}