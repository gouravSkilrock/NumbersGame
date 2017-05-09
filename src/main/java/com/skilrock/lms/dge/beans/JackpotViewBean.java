package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class JackpotViewBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double carriedOverJackpot;
	private double carriedOverRSR;
	private String drawDate;
	private int drawId;
	private int eventId;
	private double fixedPrizesFund;
	private double jackpotForThisDraw;
	private double remaningPoolFund;
	private double rolledOverJackpotAmt;
	private double rolledOverRSR;
	private double RSRForThisDraw;
	private double RSRUtilized;
	private double totalAvailableJackpotAmt;
	private double totalAvailableRSR;
	private double totalPrizeFund;
	private double totalSaleAmt;

	private double totalWinningAmt;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public double getCarriedOverJackpot() {
		return carriedOverJackpot;
	}

	public double getCarriedOverRSR() {
		return carriedOverRSR;
	}

	public String getDrawDate() {
		return drawDate;
	}

	public int getDrawId() {
		return drawId;
	}

	public double getFixedPrizesFund() {
		return fixedPrizesFund;
	}

	public double getJackpotForThisDraw() {
		return jackpotForThisDraw;
	}

	public double getRemaningPoolFund() {
		return remaningPoolFund;
	}

	public double getRolledOverJackpotAmt() {
		return rolledOverJackpotAmt;
	}

	public double getRolledOverRSR() {
		return rolledOverRSR;
	}

	public double getRSRForThisDraw() {
		return RSRForThisDraw;
	}

	public double getRSRUtilized() {
		return RSRUtilized;
	}

	public double getTotalAvailableJackpotAmt() {
		return totalAvailableJackpotAmt;
	}

	public double getTotalAvailableRSR() {
		return totalAvailableRSR;
	}

	public double getTotalPrizeFund() {
		return totalPrizeFund;
	}

	public double getTotalSaleAmt() {
		return totalSaleAmt;
	}

	public double getTotalWinningAmt() {
		return totalWinningAmt;
	}

	public void setCarriedOverJackpot(double carriedOverJackpot) {
		this.carriedOverJackpot = carriedOverJackpot;
	}

	public void setCarriedOverRSR(double carriedOverRSR) {
		this.carriedOverRSR = carriedOverRSR;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setFixedPrizesFund(double fixedPrizesFund) {
		this.fixedPrizesFund = fixedPrizesFund;
	}

	public void setJackpotForThisDraw(double jackpotForThisDraw) {
		this.jackpotForThisDraw = jackpotForThisDraw;
	}

	public void setRemaningPoolFund(double remaningPoolFund) {
		this.remaningPoolFund = remaningPoolFund;
	}

	public void setRolledOverJackpotAmt(double rolledOverJackpotAmt) {
		this.rolledOverJackpotAmt = rolledOverJackpotAmt;
	}

	public void setRolledOverRSR(double rolledOverRSR) {
		this.rolledOverRSR = rolledOverRSR;
	}

	public void setRSRForThisDraw(double forThisDraw) {
		RSRForThisDraw = forThisDraw;
	}

	public void setRSRUtilized(double utilized) {
		RSRUtilized = utilized;
	}

	public void setTotalAvailableJackpotAmt(double totalAvailableJackpotAmt) {
		this.totalAvailableJackpotAmt = totalAvailableJackpotAmt;
	}

	public void setTotalAvailableRSR(double totalAvailableRSR) {
		this.totalAvailableRSR = totalAvailableRSR;
	}

	public void setTotalPrizeFund(double totalPrizeFund) {
		this.totalPrizeFund = totalPrizeFund;
	}

	public void setTotalSaleAmt(double totalSaleAmt) {
		this.totalSaleAmt = totalSaleAmt;
	}

	public void setTotalWinningAmt(double totalWinningAmt) {
		this.totalWinningAmt = totalWinningAmt;
	}

}
