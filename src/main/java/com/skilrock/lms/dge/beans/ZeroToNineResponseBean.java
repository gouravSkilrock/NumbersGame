package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class ZeroToNineResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String saleStatus;
	private String ticketNo;
	private short barcodeCount;
	private int noOfDraws;
	private String purchaseTime;
	private String reprintCount;
	private String[] playerData;
	private double totalPurchaseAmt;
	private List<String> drawDateTime;
	private Boolean isSuccess;
	private int noOfLines[];
	private List<Integer> pickedNumbers;
	private int[] betAmountMultiple;
	private int[] isQuickPick;
    private String claimEndTime;
	public String getClaimEndTime() {
		return claimEndTime;
	}

	public void setClaimEndTime(String claimEndTime) {
		this.claimEndTime = claimEndTime;
	}

	public ZeroToNineResponseBean() {
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public short getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(short barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public String getReprintCount() {
		return reprintCount;
	}

	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}

	public String[] getPlayerData() {
		return playerData;
	}

	public void setPlayerData(String[] playerData) {
		this.playerData = playerData;
	}

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public List<String> getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public int[] getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int[] noOfLines) {
		this.noOfLines = noOfLines;
	}

	public List<Integer> getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(List<Integer> pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public int[] getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public void setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public int[] getIsQuickPick() {
		return isQuickPick;
	}

	public void setIsQuickPick(int[] isQuickPick) {
		this.isQuickPick = isQuickPick;
	}
}