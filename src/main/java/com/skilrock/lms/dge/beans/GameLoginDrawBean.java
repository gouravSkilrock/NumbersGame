package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GameLoginDrawBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int gameId;
	private String gameName;
	private String gameNameDev;
	private int gameNo;
	private Map<String, BetDetailsBean> priceMap;
	private int ticketExpiryPeriod;
	private List<DrawDetailsBean> drawDetailsBeanList;
	private int freezeTime;
	private double jackpotLimit;
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
	public String getGameNameDev() {
		return gameNameDev;
	}
	public void setGameNameDev(String gameNameDev) {
		this.gameNameDev = gameNameDev;
	}
	public int getGameNo() {
		return gameNo;
	}
	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}
	public Map<String, BetDetailsBean> getPriceMap() {
		return priceMap;
	}
	public void setPriceMap(Map<String, BetDetailsBean> priceMap) {
		this.priceMap = priceMap;
	}
	public int getTicketExpiryPeriod() {
		return ticketExpiryPeriod;
	}
	public void setTicketExpiryPeriod(int ticketExpiryPeriod) {
		this.ticketExpiryPeriod = ticketExpiryPeriod;
	}
	public void setDrawDetailsBeanList(List<DrawDetailsBean> drawDetailsBeanList) {
		this.drawDetailsBeanList = drawDetailsBeanList;
	}
	public List<DrawDetailsBean> getDrawDetailsBeanList() {
		return drawDetailsBeanList;
	}
	public void setFreezeTime(int freezeTime) {
		this.freezeTime = freezeTime;
	}
	public int getFreezeTime() {
		return freezeTime;
	}
	public void setJackpotLimit(double jackpotLimit) {
		this.jackpotLimit = jackpotLimit;
	}
	public double getJackpotLimit() {
		return jackpotLimit;
	}
	
	
}
