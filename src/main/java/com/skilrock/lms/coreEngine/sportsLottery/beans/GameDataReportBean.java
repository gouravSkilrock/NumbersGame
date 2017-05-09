package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;

public class GameDataReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private String gameName;
	private int gameTypeId;
	private String gameTypeName;
	private int drawId;
	private String drawName;
	private String drawTime;
	private String drawFreezeTime;
	private String drawStatus;
	private int noOfSale;
	private double saleAmount;
	private int noOfWinning;
	private double winningAmount;

	public GameDataReportBean() {
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

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}

	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
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

	@Override
	public String toString() {
		return "GameDataReportBean [drawId=" + drawId + ", drawName="
				+ drawName + ", gameId=" + gameId + ", gameName=" + gameName
				+ ", gameTypeId=" + gameTypeId + ", gameTypeName="
				+ gameTypeName + ", noOfSale=" + noOfSale + ", noOfWinning="
				+ noOfWinning + ", saleAmount=" + saleAmount
				+ ", winningAmount=" + winningAmount + "]";
	}
}