package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class GameMasterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int checkPpr;
	private String defaultDrawMode;
	private int gameId;
	private String gameName;
	private String gameNameDev;
	private int gameNo;
	private int jackpotCounter;
	private double jackpotLimit;
	private double operatorProfit;
	private Map<String, BetDetailsBean> priceMap;// Map only until we are not using
	private double taxPercentage;
	private int ticketExpiryPeriod;
    private String gameStatus;
	// bonus in our code.

	// When Bonus is implemented the map will be saving
	// the bean for price master

	public int getCheckPpr() {
		return checkPpr;
	}

	public String getDefaultDrawMode() {
		return defaultDrawMode;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameNameDev() {
		return gameNameDev;
	}

	public int getGameNo() {
		return gameNo;
	}

	public int getJackpotCounter() {
		return jackpotCounter;
	}

	public double getJackpotLimit() {
		return jackpotLimit;
	}

	public double getOperatorProfit() {
		return operatorProfit;
	}

	public Map<String, BetDetailsBean> getPriceMap() {
		return priceMap;
	}

	public double getTaxPercentage() {
		return taxPercentage;
	}

	public int getTicketExpiryPeriod() {
		return ticketExpiryPeriod;
	}

	public void setCheckPpr(int checkPpr) {
		this.checkPpr = checkPpr;
	}

	public void setDefaultDrawMode(String defaultDrawMode) {
		this.defaultDrawMode = defaultDrawMode;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNameDev(String gameNameDev) {
		this.gameNameDev = gameNameDev;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setJackpotCounter(int jackpotCounter) {
		this.jackpotCounter = jackpotCounter;
	}

	public void setJackpotLimit(double jackpotLimit) {
		this.jackpotLimit = jackpotLimit;
	}

	public void setOperatorProfit(double operatorProfit) {
		this.operatorProfit = operatorProfit;
	}

	public void setPriceMap(Map<String, BetDetailsBean> priceMap) {
		this.priceMap = priceMap;
	}

	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public void setTicketExpiryPeriod(int expiryPeriod) {
		this.ticketExpiryPeriod = expiryPeriod;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public String getGameStatus() {
		return gameStatus;
	}

}
