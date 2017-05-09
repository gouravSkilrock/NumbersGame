package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class DGOkPosSaleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalNoTickets;
	private int totalPromoNoTickets;
	private double totalSaleValue;
	private double totalPromoSaleValue;
	private double avgSalePerRet;
	private double avgPromoSalePerRet;
	private int totalWinningTickets;
	private int totalPromoWinningTickets;
	private double totalWinningAmount;
	private double totalPromoWinningAmount;
	private int totalClaimedTickets;
	private int totalPromoClaimedTickets;
	private double claimedAmount;
	private double promoClaimedAmount;
	private double avgSalePerTkt;
	private double avgPromoSalePerTkt;
	private int retCount;
	private int promoRetCount;

	public int getTotalNoTickets() {
		return totalNoTickets;
	}

	public void setTotalNoTickets(int totalNoTickets) {
		this.totalNoTickets = totalNoTickets;
	}

	public int getTotalPromoNoTickets() {
		return totalPromoNoTickets;
	}

	public void setTotalPromoNoTickets(int totalPromoNoTickets) {
		this.totalPromoNoTickets = totalPromoNoTickets;
	}

	public double getTotalSaleValue() {
		return totalSaleValue;
	}

	public void setTotalSaleValue(double totalSaleValue) {
		this.totalSaleValue = totalSaleValue;
	}

	public double getTotalPromoSaleValue() {
		return totalPromoSaleValue;
	}

	public void setTotalPromoSaleValue(double totalPromoSaleValue) {
		this.totalPromoSaleValue = totalPromoSaleValue;
	}

	public double getAvgSalePerRet() {
		return avgSalePerRet;
	}

	public void setAvgSalePerRet(double avgSalePerRet) {
		this.avgSalePerRet = avgSalePerRet;
	}

	public double getAvgPromoSalePerRet() {
		return avgPromoSalePerRet;
	}

	public void setAvgPromoSalePerRet(double avgPromoSalePerRet) {
		this.avgPromoSalePerRet = avgPromoSalePerRet;
	}

	public int getTotalWinningTickets() {
		return totalWinningTickets;
	}

	public void setTotalWinningTickets(int totalWinningTickets) {
		this.totalWinningTickets = totalWinningTickets;
	}

	public int getTotalPromoWinningTickets() {
		return totalPromoWinningTickets;
	}

	public void setTotalPromoWinningTickets(int totalPromoWinningTickets) {
		this.totalPromoWinningTickets = totalPromoWinningTickets;
	}

	public double getTotalWinningAmount() {
		return totalWinningAmount;
	}

	public void setTotalWinningAmount(double totalWinningAmount) {
		this.totalWinningAmount = totalWinningAmount;
	}

	public double getTotalPromoWinningAmount() {
		return totalPromoWinningAmount;
	}

	public void setTotalPromoWinningAmount(double totalPromoWinningAmount) {
		this.totalPromoWinningAmount = totalPromoWinningAmount;
	}

	public int getTotalClaimedTickets() {
		return totalClaimedTickets;
	}

	public void setTotalClaimedTickets(int totalClaimedTickets) {
		this.totalClaimedTickets = totalClaimedTickets;
	}

	public int getTotalPromoClaimedTickets() {
		return totalPromoClaimedTickets;
	}

	public void setTotalPromoClaimedTickets(int totalPromoClaimedTickets) {
		this.totalPromoClaimedTickets = totalPromoClaimedTickets;
	}

	public double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public double getPromoClaimedAmount() {
		return promoClaimedAmount;
	}

	public void setPromoClaimedAmount(double promoClaimedAmount) {
		this.promoClaimedAmount = promoClaimedAmount;
	}

	public double getAvgSalePerTkt() {
		return avgSalePerTkt;
	}

	public void setAvgSalePerTkt(double avgSalePerTkt) {
		this.avgSalePerTkt = avgSalePerTkt;
	}

	public double getAvgPromoSalePerTkt() {
		return avgPromoSalePerTkt;
	}

	public void setAvgPromoSalePerTkt(double avgPromoSalePerTkt) {
		this.avgPromoSalePerTkt = avgPromoSalePerTkt;
	}

	public int getRetCount() {
		return retCount;
	}

	public void setRetCount(int retCount) {
		this.retCount = retCount;
	}

	public int getPromoRetCount() {
		return promoRetCount;
	}

	public void setPromoRetCount(int promoRetCount) {
		this.promoRetCount = promoRetCount;
	}

	@Override
	public String toString() {
		return "DGLMSSaleBean [totalNoTickets=" + totalNoTickets
				+ ", totalPromoNoTickets=" + totalPromoNoTickets
				+ ", totalSaleValue=" + totalSaleValue
				+ ", totalPromoSaleValue=" + totalPromoSaleValue
				+ ", avgSalePerRet=" + avgSalePerRet + ", avgPromoSalePerRet="
				+ avgPromoSalePerRet + ", totalWinningTickets="
				+ totalWinningTickets + ", totalPromoWinningTickets="
				+ totalPromoWinningTickets + ", totalWinningAmount="
				+ totalWinningAmount + ", totalPromoWinningAmount="
				+ totalPromoWinningAmount + ", totalClaimedTickets="
				+ totalClaimedTickets + ", totalPromoClaimedTickets="
				+ totalPromoClaimedTickets + ", claimedAmount=" + claimedAmount
				+ ", promoClaimedAmount=" + promoClaimedAmount
				+ ", avgSalePerTkt=" + avgSalePerTkt + ", avgPromoSalePerTkt="
				+ avgPromoSalePerTkt + ", retCount=" + retCount
				+ ", promoRetCount=" + promoRetCount + "]";
	}

}
