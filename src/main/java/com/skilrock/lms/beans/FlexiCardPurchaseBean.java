package com.skilrock.lms.beans;

public class FlexiCardPurchaseBean {
	private long pinNbr;
	private long serialNumber;
	private double amount;
	private String denomiationType;	
	private String returnType;
	private String  partyId;
	private boolean isSuccess;
	private long transactionId ; 
	private String purchaseDate;
	private String playerName ;
	private String plrPassword ;
	
	public String getPlrPassword() {
		return plrPassword;
	}
	public void setPlrPassword(String plrPassword) {
		this.plrPassword = plrPassword;
	}
	public long getPinNbr() {
		return pinNbr;
	}
	public void setPinNbr(long pinNbr) {
		this.pinNbr = pinNbr;
	}
	public Long getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDenomiationType() {
		return denomiationType;
	}
	public void setDenomiationType(String denomiationType) {
		this.denomiationType = denomiationType;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
}
