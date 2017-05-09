package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class GameMasterLMSBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double agentPwtCommRate;
	private double agentSaleCommRate;
	private int gameId;
	private String gameName;
	private String gameNameDev;
	private int gameNo;
	private String gameStatus;
	private double govtComm;
	private double govtCommPwt;
	private double highPrizeAmount;
	private Map<String, BetDetailsBean> priceMap;// Map only until we are not using
	private double prizePayoutRatio;
	private double retPwtCommRate;
	private double retSaleCommRate;
	private int ticketExpiryPeriod;
	private double vatAmount;
	private int isDependent = 0;
	private int jackpotCounter;//changes
	private double jackpotLimit;//changes
	private String bonusBallEnable;

	// bonus in our code.

	// When Bonus is implemented the map will be saving
	// the bean for price master

	public double getGovtCommPwt() {
		return govtCommPwt;
	}

	public void setGovtCommPwt(double govtCommPwt) {
		this.govtCommPwt = govtCommPwt;
	}
	
	public double getAgentPwtCommRate() {
		return agentPwtCommRate;
	}

	public double getAgentSaleCommRate() {
		return agentSaleCommRate;
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

	public String getGameStatus() {
		return gameStatus;
	}

	public double getGovtComm() {
		return govtComm;
	}

	public double getHighPrizeAmount() {
		return highPrizeAmount;
	}

	public Map<String, BetDetailsBean> getPriceMap() {
		return priceMap;
	}

	public double getPrizePayoutRatio() {
		return prizePayoutRatio;
	}

	public double getRetPwtCommRate() {
		return retPwtCommRate;
	}

	public double getRetSaleCommRate() {
		return retSaleCommRate;
	}

	public int getTicketExpiryPeriod() {
		return ticketExpiryPeriod;
	}

	public double getVatAmount() {
		return vatAmount;
	}

	public void setAgentPwtCommRate(double agentPwtCommRate) {
		this.agentPwtCommRate = agentPwtCommRate;
	}

	public void setAgentSaleCommRate(double agentSaleCommRate) {
		this.agentSaleCommRate = agentSaleCommRate;
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

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setGovtComm(double govtComm) {
		this.govtComm = govtComm;
	}

	public void setHighPrizeAmount(double highPrizeAmount) {
		this.highPrizeAmount = highPrizeAmount;
	}

	public void setPriceMap(Map<String, BetDetailsBean> priceMap) {
		this.priceMap = priceMap;
	}

	public void setPrizePayoutRatio(double prizePayoutRatio) {
		this.prizePayoutRatio = prizePayoutRatio;
	}

	public void setRetPwtCommRate(double retPwtCommRate) {
		this.retPwtCommRate = retPwtCommRate;
	}

	public void setRetSaleCommRate(double retSaleCommRate) {
		this.retSaleCommRate = retSaleCommRate;
	}

	public void setTicketExpiryPeriod(int ticketExpiryPeriod) {
		this.ticketExpiryPeriod = ticketExpiryPeriod;
	}

	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public int getIsDependent() {
		return isDependent;
	}

	public void setIsDependent(int isDependent) {
		this.isDependent = isDependent;
	}

	public int getJackpotCounter() {
		return jackpotCounter;
	}

	public double getJackpotLimit() {
		return jackpotLimit;
	}

	public void setJackpotCounter(int jackpotCounter) {
		this.jackpotCounter = jackpotCounter;
	}

	public void setJackpotLimit(double jackpotLimit) {
		this.jackpotLimit = jackpotLimit;
	}

	public String getBonusBallEnable() {
		return bonusBallEnable;
	}

	public void setBonusBallEnable(String bonusBallEnable) {
		this.bonusBallEnable = bonusBallEnable;
	}
	
}
