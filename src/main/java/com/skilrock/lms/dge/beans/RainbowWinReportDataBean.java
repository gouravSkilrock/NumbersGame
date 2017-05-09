package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class RainbowWinReportDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int totalTkts;
	private double totalSale;

	private int totalFirstPrizeTkts;
	private double totalFirstPrizeAmt;
	private int totalFirstPrizeClaimedTkts;
	private double totalFirstPrizeClaimedAmt;

	private int totalSecondPrizeTkts;
	private double totalSecondPrizeAmt;
	private int totalSecondPrizeClaimedTkts;
	private double totalSecondPrizeClaimedAmt;

	private int totalSpecialPrizeTkts;
	private double totalSpecialPrizeAmt;
	private int totalSpecialPrizeClaimedTkts;
	private double totalSpecialPrizeClaimedAmt;
	
	public int getTotalTkts() {
		return totalTkts;
	}

	public void setTotalTkts(int totalTkts) {
		this.totalTkts = totalTkts;
	}

	public double getTotalSale() {
		return totalSale;
	}

	public void setTotalSale(double totalSale) {
		this.totalSale = totalSale;
	}

	public int getTotalFirstPrizeTkts() {
		return totalFirstPrizeTkts;
	}

	public void setTotalFirstPrizeTkts(int totalFirstPrizeTkts) {
		this.totalFirstPrizeTkts = totalFirstPrizeTkts;
	}

	public double getTotalFirstPrizeAmt() {
		return totalFirstPrizeAmt;
	}

	public void setTotalFirstPrizeAmt(double totalFirstPrizeAmt) {
		this.totalFirstPrizeAmt = totalFirstPrizeAmt;
	}

	public int getTotalFirstPrizeClaimedTkts() {
		return totalFirstPrizeClaimedTkts;
	}

	public void setTotalFirstPrizeClaimedTkts(int totalFirstPrizeClaimedTkts) {
		this.totalFirstPrizeClaimedTkts = totalFirstPrizeClaimedTkts;
	}

	public double getTotalFirstPrizeClaimedAmt() {
		return totalFirstPrizeClaimedAmt;
	}

	public void setTotalFirstPrizeClaimedAmt(double totalFirstPrizeClaimedAmt) {
		this.totalFirstPrizeClaimedAmt = totalFirstPrizeClaimedAmt;
	}

	public int getTotalSecondPrizeTkts() {
		return totalSecondPrizeTkts;
	}

	public void setTotalSecondPrizeTkts(int totalSecondPrizeTkts) {
		this.totalSecondPrizeTkts = totalSecondPrizeTkts;
	}

	public double getTotalSecondPrizeAmt() {
		return totalSecondPrizeAmt;
	}

	public void setTotalSecondPrizeAmt(double totalSecondPrizeAmt) {
		this.totalSecondPrizeAmt = totalSecondPrizeAmt;
	}

	public int getTotalSecondPrizeClaimedTkts() {
		return totalSecondPrizeClaimedTkts;
	}

	public void setTotalSecondPrizeClaimedTkts(int totalSecondPrizeClaimedTkts) {
		this.totalSecondPrizeClaimedTkts = totalSecondPrizeClaimedTkts;
	}

	public double getTotalSecondPrizeClaimedAmt() {
		return totalSecondPrizeClaimedAmt;
	}

	public void setTotalSecondPrizeClaimedAmt(double totalSecondPrizeClaimedAmt) {
		this.totalSecondPrizeClaimedAmt = totalSecondPrizeClaimedAmt;
	}

	public int getTotalSpecialPrizeTkts() {
		return totalSpecialPrizeTkts;
	}

	public void setTotalSpecialPrizeTkts(int totalSpecialPrizeTkts) {
		this.totalSpecialPrizeTkts = totalSpecialPrizeTkts;
	}

	public double getTotalSpecialPrizeAmt() {
		return totalSpecialPrizeAmt;
	}

	public void setTotalSpecialPrizeAmt(double totalSpecialPrizeAmt) {
		this.totalSpecialPrizeAmt = totalSpecialPrizeAmt;
	}

	public int getTotalSpecialPrizeClaimedTkts() {
		return totalSpecialPrizeClaimedTkts;
	}

	public void setTotalSpecialPrizeClaimedTkts(int totalSpecialPrizeClaimedTkts) {
		this.totalSpecialPrizeClaimedTkts = totalSpecialPrizeClaimedTkts;
	}

	public double getTotalSpecialPrizeClaimedAmt() {
		return totalSpecialPrizeClaimedAmt;
	}

	public void setTotalSpecialPrizeClaimedAmt(
			double totalSpecialPrizeClaimedAmt) {
		this.totalSpecialPrizeClaimedAmt = totalSpecialPrizeClaimedAmt;
	}

	@Override
	public String toString() {
		return "RainbowWinReportDataBean [totalTkts=" + totalTkts
				+ ", totalSale=" + totalSale + ", totalFirstPrizeTkts="
				+ totalFirstPrizeTkts + ", totalFirstPrizeAmt="
				+ totalFirstPrizeAmt + ", totalFirstPrizeClaimedTkts="
				+ totalFirstPrizeClaimedTkts + ", totalFirstPrizeClaimedAmt="
				+ totalFirstPrizeClaimedAmt + ", totalSecondPrizeTkts="
				+ totalSecondPrizeTkts + ", totalSecondPrizeAmt="
				+ totalSecondPrizeAmt + ", totalSecondPrizeClaimedTkts="
				+ totalSecondPrizeClaimedTkts + ", totalSecondPrizeClaimedAmt="
				+ totalSecondPrizeClaimedAmt + ", totalSpecialPrizeTkts="
				+ totalSpecialPrizeTkts + ", totalSpecialPrizeAmt="
				+ totalSpecialPrizeAmt + ", totalSpecialPrizeClaimedTkts="
				+ totalSpecialPrizeClaimedTkts
				+ ", totalSpecialPrizeClaimedAmt="
				+ totalSpecialPrizeClaimedAmt + "]";
	}

}
