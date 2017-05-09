package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class OfflineRetailerBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int userId;
	private boolean statusWithDrawInfo;
	private Map<Integer, StringBuilder> gameData;
	Map<Integer, Integer> offlineFzTimeMap;
	Map<Integer, Double> retSaleComm;
	double version;
	private boolean isOffline;
	private String partyType;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Map<Integer, StringBuilder> getGameData() {
		return gameData;
	}
	public void setGameData(Map<Integer, StringBuilder> gameData) {
		this.gameData = gameData;
	}
	public boolean isOffline() {
		return isOffline;
	}
	public void setOffline(boolean isOffline) {
		this.isOffline = isOffline;
	}
	public Map<Integer, Integer> getOfflineFzTimeMap() {
		return offlineFzTimeMap;
	}
	public void setOfflineFzTimeMap(Map<Integer, Integer> offlineFzTimeMap) {
		this.offlineFzTimeMap = offlineFzTimeMap;
	}
	public Map<Integer, Double> getRetSaleComm() {
		return retSaleComm;
	}
	public void setRetSaleComm(Map<Integer, Double> retSaleComm) {
		this.retSaleComm = retSaleComm;
	}
	public double getVersion() {
		return version;
	}
	public void setVersion(double version) {
		this.version = version;
	}
	public String getPartyType() {
		return partyType;
	}
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}
	public boolean isStatusWithDrawInfo() {
		return statusWithDrawInfo;
	}
	public void setStatusWithDrawInfo(boolean statusWithDrawInfo) {
		this.statusWithDrawInfo = statusWithDrawInfo;
	}
	
}
