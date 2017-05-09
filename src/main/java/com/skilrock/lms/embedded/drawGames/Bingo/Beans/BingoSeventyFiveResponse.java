package com.skilrock.lms.embedded.drawGames.Bingo.Beans;

import java.io.Serializable;
import java.util.List;

public class BingoSeventyFiveResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String saleStatus;
	private String ticketNo;
	private short barcodeCount;
	private int noOfDraws;
	private String purchaseTime;
	private String reprintCount;
	private String[] playerData;
	private String[] betDispName;
	private double totalPurchaseAmt;
	private List<String> drawDateTime;
	private Boolean isSuccess;
	private String[] pickedNumbers;
	private String errorCode;
	private int[] noOfLines;
	
	

	

	public int[] getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int[] noOfLines) {
		this.noOfLines = noOfLines;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(String[] pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public BingoSeventyFiveResponse() {
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

	public String[] getBetDispName() {
		return betDispName;
	}

	public void setBetDispName(String[] betDispName) {
		this.betDispName = betDispName;
	}
}