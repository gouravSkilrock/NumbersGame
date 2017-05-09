package com.skilrock.lms.keba.drawGames.javaBeans;

import java.io.Serializable;
import java.util.List;

public class SaleResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean isSuccess;
	private String errorMessage;
	private String balance;
	private String ticketNo;
	private int barcodeCount;
	private String gameName;
	private String purchaseTime;
	private double purchaseAmt;
	private List<PanelResponseBean> panelData;

	public SaleResponseBean() {
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public int getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(int barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public double getPurchaseAmt() {
		return purchaseAmt;
	}

	public void setPurchaseAmt(double purchaseAmt) {
		this.purchaseAmt = purchaseAmt;
	}

	public List<PanelResponseBean> getPanelData() {
		return panelData;
	}

	public void setPanelData(List<PanelResponseBean> panelData) {
		this.panelData = panelData;
	}
}