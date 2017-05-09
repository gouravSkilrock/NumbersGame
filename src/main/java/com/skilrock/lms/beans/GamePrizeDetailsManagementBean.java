package com.skilrock.lms.beans;

public class GamePrizeDetailsManagementBean {

	private long noOfPrizeCancel;
	private long noOfPrizeRem;
	private long noOfWinners;
	private double odds;
	private double percentage;
	private double prize;
	private double prizeAmt;
	private double prizeFund;
	private String prizeLevel;
	private long totalNoOfPrize;
	private double totalPrizeAmount;

	public long getNoOfPrizeCancel() {
		return noOfPrizeCancel;
	}

	public long getNoOfPrizeRem() {
		return noOfPrizeRem;
	}

	public long getNoOfWinners() {
		return noOfWinners;
	}

	public double getOdds() {
		return odds;
	}

	public double getPercentage() {
		return percentage;
	}

	public double getPrize() {
		return prize;
	}

	public double getPrizeAmt() {
		return prizeAmt;
	}

	public double getPrizeFund() {
		return prizeFund;
	}

	public String getPrizeLevel() {
		return prizeLevel;
	}

	public long getTotalNoOfPrize() {
		return totalNoOfPrize;
	}

	public double getTotalPrizeAmount() {
		return totalPrizeAmount;
	}

	public void setNoOfPrizeCancel(long noOfPrizeCancel) {
		this.noOfPrizeCancel = noOfPrizeCancel;
	}

	public void setNoOfPrizeRem(long noOfPrizeRem) {
		this.noOfPrizeRem = noOfPrizeRem;
	}

	public void setNoOfWinners(long noOfWinners) {
		this.noOfWinners = noOfWinners;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public void setPrize(double prize) {
		this.prize = prize;
	}

	public void setPrizeAmt(double prizeAmt) {
		this.prizeAmt = prizeAmt;
	}

	public void setPrizeFund(double prizeFund) {
		this.prizeFund = prizeFund;
	}

	public void setPrizeLevel(String prizeLevel) {
		this.prizeLevel = prizeLevel;
	}

	public void setTotalNoOfPrize(long totalNoOfPrize) {
		this.totalNoOfPrize = totalNoOfPrize;
	}

	public void setTotalPrizeAmount(double totalPrizeAmount) {
		this.totalPrizeAmount = totalPrizeAmount;
	}

}
