package com.skilrock.lms.beans;

public class CashCardPurchaseDataBean {
	
	private long pinNbr;
	private long serialNumber;
	private double amount;
	private String denomiationType;	

	public long getPinNbr() {
		return pinNbr;
	}
	public void setPinNbr(long pinNbr) {
		this.pinNbr = pinNbr;
	}
	public long getSerialNumber() {
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
	
}
