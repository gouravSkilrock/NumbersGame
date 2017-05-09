package com.skilrock.lms.api.reports.beans;

public class LmsApiReportDGPMSSaleBean {

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
	public void setTotalNoTickets(int totalNoTickets) {
		this.totalNoTickets = totalNoTickets;
	}
	public double getTotalSaleValue() {
		return totalSaleValue;
	}
	public void setTotalSaleValue(double totalSaleValue) {
		this.totalSaleValue = totalSaleValue;
	}
	public int getTotalWinningTickets() {
		return totalWinningTickets;
	}
	public void setTotalWinningTickets(int totalWinningTickets) {
		this.totalWinningTickets = totalWinningTickets;
	}
	public double getTotalWinningAmount() {
		return totalWinningAmount;
	}
	public void setTotalWinningAmount(double totalWinningAmount) {
		this.totalWinningAmount = totalWinningAmount;
	}
	public int getTotalClaimedTickets() {
		return totalClaimedTickets;
	}
	public void setTotalClaimedTickets(int totalClaimedTickets) {
		this.totalClaimedTickets = totalClaimedTickets;
	}
	public double getClaimedAmount() {
		return claimedAmount;
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
