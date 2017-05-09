package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class OfflineGameBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, String> drawIdMap;
	private Map<Integer, String> freezeTime;// Freeze Time is ,(comma) seperated
	private int gameId;
	private String gameNameDev;
	private int gameNo;
	// first is online then is offline
	private int offFrzTimeGap;
	private double retComm;
	private int ticketCounter;
	private int ticketExpiryPeriod;
	private Map<String, Double> unitPriceMap;

	public Map<Integer, String> getDrawIdMap() {
		return drawIdMap;
	}

	public Map<Integer, String> getFreezeTime() {
		return freezeTime;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameNameDev() {
		return gameNameDev;
	}

	public int getGameNo() {
		return gameNo;
	}

	public int getOffFrzTimeGap() {
		return offFrzTimeGap;
	}

	public double getRetComm() {
		return retComm;
	}

	public int getTicketCounter() {
		return ticketCounter;
	}

	public int getTicketExpiryPeriod() {
		return ticketExpiryPeriod;
	}

	public Map<String, Double> getUnitPriceMap() {
		return unitPriceMap;
	}

	public void setDrawIdMap(Map<Integer, String> drawIdMap) {
		this.drawIdMap = drawIdMap;
	}

	public void setFreezeTime(Map<Integer, String> freezeTime) {
		this.freezeTime = freezeTime;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNameDev(String gameNameDev) {
		this.gameNameDev = gameNameDev;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setOffFrzTimeGap(int offFrzTimeGap) {
		this.offFrzTimeGap = offFrzTimeGap;
	}

	public void setRetComm(double retComm) {
		this.retComm = retComm;
	}

	public void setTicketCounter(int ticketCounter) {
		this.ticketCounter = ticketCounter;
	}

	public void setTicketExpiryPeriod(int ticketExpiryPeriod) {
		this.ticketExpiryPeriod = ticketExpiryPeriod;
	}

	public void setUnitPriceMap(Map<String, Double> unitPriceMap) {
		this.unitPriceMap = unitPriceMap;
	}

}
