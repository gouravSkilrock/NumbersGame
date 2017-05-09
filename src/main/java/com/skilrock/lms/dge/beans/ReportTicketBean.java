package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ReportTicketBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int agentId;
	private int agentName;

	private String claimStatus; // Added for 'Keno'
	private List<DrawIdBean> drawWinList;
	private String gameName;

	private int gameNo;
	private int noOfDraws;

	private int noOfLines; // Added for 'Keno'
	private HashMap<Integer, String> partyId;

	private String playType; // Added for 'Keno'
	private int retailerId;
	private String retailerName;
	private double saleAmt;
	private String saleDate;
	private String status;

	public int getAgentId() {
		return agentId;
	}

	public int getAgentName() {
		return agentName;
	}

	public String getClaimStatus() {
		return claimStatus;
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

	public int getNoOfLines() {
		return noOfLines;
	}

	public HashMap<Integer, String> getPartyId() {
		return partyId;
	}

	public String getPlayType() {
		return playType;
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

	public String getStatus() {
		return status;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public void setAgentName(int agentName) {
		this.agentName = agentName;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
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

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

	public void setPartyId(HashMap<Integer, String> partyId) {
		this.partyId = partyId;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
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

	public void setStatus(String status) {
		this.status = status;
	}

}
