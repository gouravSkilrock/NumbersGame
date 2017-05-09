package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;
import java.util.List;

public class GameReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private String gameName;
	private int noOfSale;
	private double saleAmount;
	private int noOfWinning;
	private double winningAmount;
	private List<GameTypeReportBean> gameTypeReportList;

	public GameReportBean() {
	}

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

	public List<GameTypeReportBean> getGameTypeReportList() {
		return gameTypeReportList;
	}

	public void setGameTypeReportList(List<GameTypeReportBean> gameTypeReportList) {
		this.gameTypeReportList = gameTypeReportList;
	}
}