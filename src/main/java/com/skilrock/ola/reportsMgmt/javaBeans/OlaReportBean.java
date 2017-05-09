package com.skilrock.ola.reportsMgmt.javaBeans;

import java.io.Serializable;

public class OlaReportBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fromDate;
	private String toDate;
	private String playerName;
	private double depositAmt;
	private double withdrawlAmt;
	private double retailerNetGaming;
	private double retailerDepositCommission;
	private double retailerWithdrawlCommission;
	private double commissionCalculated;
	
	private double retailerCommissionCalculated;
	private String retailerName;
	private double totalDepositAmount;
	private double totalWithdrawlAmount ;
	private double totalDepositCommission;
	private double totalWithdrawlCommission;
	private double totalPlayerNetGaming;
	private double totalCommissionCalculated ;
	private String playerType;
	private String trnDate;
	

	public String getTrnDate() {
		return trnDate;
	}

	public void setTrnDate(String trnDate) {
		this.trnDate = trnDate;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public double getRetailerCommissionCalculated() {
		return retailerCommissionCalculated;
	}

	public void setRetailerCommissionCalculated(double retailerCommissionCalculated) {
		this.retailerCommissionCalculated = retailerCommissionCalculated;
	}

	public double getTotalDepositAmount() {
		return totalDepositAmount;
	}

	public void setTotalDepositAmount(double totalDepositAmount) {
		this.totalDepositAmount = totalDepositAmount;
	}

	public double getTotalWithdrawlAmount() {
		return totalWithdrawlAmount;
	}

	public void setTotalWithdrawlAmount(double totalWithdrawlAmount) {
		this.totalWithdrawlAmount = totalWithdrawlAmount;
	}

	public double getTotalDepositCommission() {
		return totalDepositCommission;
	}

	public void setTotalDepositCommission(double totalDepositCommission) {
		this.totalDepositCommission = totalDepositCommission;
	}

	public double getTotalWithdrawlCommission() {
		return totalWithdrawlCommission;
	}

	public void setTotalWithdrawlCommission(double totalWithdrawlCommission) {
		this.totalWithdrawlCommission = totalWithdrawlCommission;
	}

	public double getTotalPlayerNetGaming() {
		return totalPlayerNetGaming;
	}

	public void setTotalPlayerNetGaming(double totalPlayerNetGaming) {
		this.totalPlayerNetGaming = totalPlayerNetGaming;
	}

	public double getTotalCommissionCalculated() {
		return totalCommissionCalculated;
	}

	public void setTotalCommissionCalculated(double totalCommissionCalculated) {
		this.totalCommissionCalculated = totalCommissionCalculated;
	}

	public double getCommissionCalculated() {
		return commissionCalculated;
	}

	public void setCommissionCalculated(double commissionCalculated) {
		this.commissionCalculated = commissionCalculated;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public double getDepositAmt() {
		return depositAmt;
	}

	public void setDepositAmt(double depositAmt) {
		this.depositAmt = depositAmt;
	}

	public double getWithdrawlAmt() {
		return withdrawlAmt;
	}

	public void setWithdrawlAmt(double withdrawlAmt) {
		this.withdrawlAmt = withdrawlAmt;
	}

	

	public double getRetailerDepositCommission() {
		return retailerDepositCommission;
	}

	public void setRetailerDepositCommission(double retailerDepositCommission) {
		this.retailerDepositCommission = retailerDepositCommission;
	}

	public double getRetailerWithdrawlCommission() {
		return retailerWithdrawlCommission;
	}

	public void setRetailerWithdrawlCommission(double retailerWithdrawlCommission) {
		this.retailerWithdrawlCommission = retailerWithdrawlCommission;
	}

	public double getRetailerNetGaming() {
		return retailerNetGaming;
	}

	public void setRetailerNetGaming(double retailerNetGaming) {
		this.retailerNetGaming = retailerNetGaming;
	}

	
}
