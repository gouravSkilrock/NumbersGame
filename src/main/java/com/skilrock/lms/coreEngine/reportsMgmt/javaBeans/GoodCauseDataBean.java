package com.skilrock.lms.coreEngine.reportsMgmt.javaBeans;

public class GoodCauseDataBean {
	private int gameId;
	private String gameName;
	private double saleUnapprovedAmount;
	private double winningUnapprovedAmount;
	private double saleApprovedAmount;
	private double winningApprovedAmount;
	private double saleDoneAmount;
	private double winningDoneAmount;
	private String transactionDate;
	private String serviceCode;

	public GoodCauseDataBean() {
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public double getSaleUnapprovedAmount() {
		return saleUnapprovedAmount;
	}

	public void setSaleUnapprovedAmount(double saleUnapprovedAmount) {
		this.saleUnapprovedAmount = saleUnapprovedAmount;
	}

	public double getWinningUnapprovedAmount() {
		return winningUnapprovedAmount;
	}

	public void setWinningUnapprovedAmount(double winningUnapprovedAmount) {
		this.winningUnapprovedAmount = winningUnapprovedAmount;
	}

	public double getSaleApprovedAmount() {
		return saleApprovedAmount;
	}

	public void setSaleApprovedAmount(double saleApprovedAmount) {
		this.saleApprovedAmount = saleApprovedAmount;
	}

	public double getWinningApprovedAmount() {
		return winningApprovedAmount;
	}

	public void setWinningApprovedAmount(double winningApprovedAmount) {
		this.winningApprovedAmount = winningApprovedAmount;
	}

	public double getSaleDoneAmount() {
		return saleDoneAmount;
	}

	public void setSaleDoneAmount(double saleDoneAmount) {
		this.saleDoneAmount = saleDoneAmount;
	}

	public double getWinningDoneAmount() {
		return winningDoneAmount;
	}

	public void setWinningDoneAmount(double winningDoneAmount) {
		this.winningDoneAmount = winningDoneAmount;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
}