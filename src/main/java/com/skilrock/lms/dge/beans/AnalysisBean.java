package com.skilrock.lms.dge.beans;

import java.io.Serializable;

import java.util.List;
import java.util.Set;

public class AnalysisBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String gameNo;
	private String drawName;
	private String drawTime;
	private String startDate;
	private String endDate;
	private String partyId;
	private String retailerName;
	private double saleAmount;
	private int saleCount;
	private double pwtAmount;
	private int pwtCount;
	private double prizePayoutRatio;

	String totalSaleAmount;
	String totalPwtAmount;
	String totalPprAmount;
	int totalSaleCount;
	int totalPwtCount;
	private String merchantId ;
	private Set salePartyIdList;
	private int userID;
	private int gameId;
	private List<String> partyIdList;
	private String drawStatus;

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getGameNo() {
		return gameNo;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public int getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}

	public double getPwtAmount() {
		return pwtAmount;
	}

	public void setPwtAmount(double pwtAmount) {
		this.pwtAmount = pwtAmount;
	}

	public int getPwtCount() {
		return pwtCount;
	}

	public void setPwtCount(int pwtCount) {
		this.pwtCount = pwtCount;
	}

	public double getPrizePayoutRatio() {
		return prizePayoutRatio;
	}

	public void setPrizePayoutRatio(double prizePayoutRatio) {
		this.prizePayoutRatio = prizePayoutRatio;
	}

	public String getTotalSaleAmount() {
		return totalSaleAmount;
	}

	public void setTotalSaleAmount(String totalSaleAmount) {
		this.totalSaleAmount = totalSaleAmount;
	}

	public String getTotalPwtAmount() {
		return totalPwtAmount;
	}

	public void setTotalPwtAmount(String totalPwtAmount) {
		this.totalPwtAmount = totalPwtAmount;
	}

	

	public String getTotalPprAmount() {
		return totalPprAmount;
	}

	public void setTotalPprAmount(String totalPprAmount) {
		this.totalPprAmount = totalPprAmount;
	}

	public int getTotalSaleCount() {
		return totalSaleCount;
	}

	public void setTotalSaleCount(int totalSaleCount) {
		this.totalSaleCount = totalSaleCount;
	}

	public int getTotalPwtCount() {
		return totalPwtCount;
	}

	public void setTotalPwtCount(int totalPwtCount) {
		this.totalPwtCount = totalPwtCount;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Set getSalePartyIdList() {
		return salePartyIdList;
	}

	public void setSalePartyIdList(Set salePartyIdList) {
		this.salePartyIdList = salePartyIdList;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public List<String> getPartyIdList() {
		return partyIdList;
	}

	public void setPartyIdList(List<String> partyIdList) {
		this.partyIdList = partyIdList;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	
}
