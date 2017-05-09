package com.skilrock.lms.api.reports.beans;

public class LmsApiReportDGLMSSaleBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalNoTickets;
	private double totalSaleValue;
	private int totalWinningTickets;
	private double totalWinningAmount;
	private int totalClaimedTickets;
	private double claimedAmount;
	private int totalUnclaimedTickets;
	
	public int getTotalNoTickets() {
		return totalNoTickets;
	}
	public double getTotalSaleValue() {
		return totalSaleValue;
	}
	public int getTotalWinningTickets() {
		return totalWinningTickets;
	}
	public double getTotalWinningAmount() {
		return totalWinningAmount;
	}
	public int getTotalClaimedTickets() {
		return totalClaimedTickets;
	}
	public double getClaimedAmount() {
		return claimedAmount;
	}
	public void setTotalNoTickets(int totalNoTickets) {
		this.totalNoTickets = totalNoTickets;
	}
	public void setTotalSaleValue(double totalSaleValue) {
		this.totalSaleValue = totalSaleValue;
	}
	public void setTotalWinningTickets(int totalWinningTickets) {
		this.totalWinningTickets = totalWinningTickets;
	}
	public void setTotalWinningAmount(double totalWinningAmount) {
		this.totalWinningAmount = totalWinningAmount;
	}
	public void setTotalClaimedTickets(int totalClaimedTickets) {
		this.totalClaimedTickets = totalClaimedTickets;
	}
	public void setClaimedAmount(double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public int getTotalUnclaimedTickets() {
		return totalUnclaimedTickets;
	}
	public void setTotalUnclaimedTickets(int totalUnclaimedTickets) {
		this.totalUnclaimedTickets = totalUnclaimedTickets;
	}

}
