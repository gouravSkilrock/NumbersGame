package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class DrawDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String drawStatus;
	private String drawTime;
	private String fromDate;
	private String gameName;
	private String gameDevName;
	private int gameNo;
	private String toDate;
	private int totalNoTickets;
	private double totalSaleValue;
	private double totalWinningAmount;
	private String winningResult;
	private String lastDrawStatus;
	private String lastDrawtime;
	private double lastDrawSaleValue;
	private double lastDrawWinningAmt;
	private int drawId;
	private int lastDrawId;
	private String drawName;
	private int agentOrgId;
	private String merchantId;
	private String walletName;


	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	private ArrayList<Integer> retailerUserIdList;

	public String getDrawStatus() {
		return drawStatus;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String getToDate() {
		return toDate;
	}

	public int getTotalNoTickets() {
		return totalNoTickets;
	}

	public double getTotalSaleValue() {
		return totalSaleValue;
	}

	public double getTotalWinningAmount() {
		return totalWinningAmount;
	}

	public String getWinningResult() {
		return winningResult;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setTotalNoTickets(int totalNoTickets) {
		this.totalNoTickets = totalNoTickets;
	}

	public void setTotalSaleValue(double totalSaleValue) {
		this.totalSaleValue = totalSaleValue;
	}

	public void setTotalWinningAmount(double totalWinningAmount) {
		this.totalWinningAmount = totalWinningAmount;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

	public String getLastDrawStatus() {
		return lastDrawStatus;
	}

	public void setLastDrawStatus(String lastDrawStatus) {
		this.lastDrawStatus = lastDrawStatus;
	}

	public String getLastDrawtime() {
		return lastDrawtime;
	}

	public void setLastDrawtime(String lastDrawtime) {
		this.lastDrawtime = lastDrawtime;
	}

	public double getLastDrawSaleValue() {
		return lastDrawSaleValue;
	}

	public void setLastDrawSaleValue(double lastDrawSaleValue) {
		this.lastDrawSaleValue = lastDrawSaleValue;
	}

	public double getLastDrawWinningAmt() {
		return lastDrawWinningAmt;
	}

	public void setLastDrawWinningAmt(double lastDrawWinningAmt) {
		this.lastDrawWinningAmt = lastDrawWinningAmt;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getLastDrawId() {
		return lastDrawId;
	}

	public void setLastDrawId(int lastDrawId) {
		this.lastDrawId = lastDrawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	
	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public ArrayList<Integer> getRetailerUserIdList() {
		return retailerUserIdList;
	}

	public void setRetailerUserIdList(ArrayList<Integer> retailerUserIdList) {
		this.retailerUserIdList = retailerUserIdList;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	
}