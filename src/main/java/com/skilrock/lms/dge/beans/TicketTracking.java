package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class TicketTracking implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int agentId;
	private int agentName;
	private String calimStatus; // Added for 'Keno'
	private List<DrawIdBean> drawWinList;
	private String gameName;
	private int gameNo;
	private int noOfDraws;
	private List<Integer> noOfLines; // Added for 'Keno'
	private HashMap<Integer, String> partyId;
	private List<String> playType; // Added for 'Keno'
	private HashMap<Integer, String> pwtTimeMap;
	private int retailerId;
	private String retailerName;
	private double saleAmt;
	private String saleDate;
	private List<RafflePurchaseBean> raffleList;
private String tktNumber;
	private String saleMode; // ONLINE or OFFLINE
	private String status;

	private String statusCheck = "";
	private boolean isRaffle;
	private int gameId;
	

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getAgentId() {
		return agentId;
	}

	public int getAgentName() {
		return agentName;
	}

	public String getCalimStatus() {
		return calimStatus;
	}

	public List<DrawIdBean> getDrawWinList() {
		return drawWinList;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public HashMap<Integer, String> getPartyId() {
		return partyId;
	}

	public HashMap<Integer, String> getPwtTimeMap() {
		return pwtTimeMap;
	}

	public int getRetailerId() {
		return retailerId;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public double getSaleAmt() {
		return saleAmt;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public String getSaleMode() {
		return saleMode;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusCheck() {
		return statusCheck;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public void setAgentName(int agentName) {
		this.agentName = agentName;
	}

	public void setCalimStatus(String calimStatus) {
		this.calimStatus = calimStatus;
	}

	public void setDrawWinList(List<DrawIdBean> drawWinList) {
		this.drawWinList = drawWinList;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setPartyId(HashMap<Integer, String> partyId) {
		this.partyId = partyId;
	}

	public void setPwtTimeMap(HashMap<Integer, String> pwtTimeMap) {
		this.pwtTimeMap = pwtTimeMap;
	}

	public void setRetailerId(int retailerId) {
		this.retailerId = retailerId;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	public void setSaleMode(String saleMode) {
		this.saleMode = saleMode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusCheck(String statusCheck) {
		this.statusCheck = statusCheck;
	}

	public List<Integer> getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(List<Integer> noOfLines) {
		this.noOfLines = noOfLines;
	}

	public List<String> getPlayType() {
		return playType;
	}

	public void setPlayType(List<String> playType) {
		this.playType = playType;
	}

	public List<RafflePurchaseBean> getRaffleList() {
		return raffleList;
	}

	public void setRaffleList(List<RafflePurchaseBean> raffleList) {
		this.raffleList = raffleList;
	}
	
	public void setIsRaffle(boolean isRaffle){
	this.isRaffle=isRaffle;
		}
public boolean getIsRaffle(){
	return isRaffle;
}

public String getTktNumber() {
	return tktNumber;
}

public void setTktNumber(String tktNumber) {
	this.tktNumber = tktNumber;
}

}
