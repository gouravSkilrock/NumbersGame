package com.skilrock.lms.beans;

import java.sql.Timestamp;

public class AgtLedAccDetailsBean {
	private double accBalance = 0.0;
	private double amount = 0.0;
	private String ledTransType = null;
	private String transactionID = null;
	private String transactionWith = null;
	private Timestamp trDate = null;
	private String trDateStr = null;

	public double getAccBalance() {
		return accBalance;
	}

	public double getAmount() {
		return amount;
	}

	public String getLedTransType() {
		return ledTransType;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public String getTransactionWith() {
		return transactionWith;
	}

	public Timestamp getTrDate() {
		return trDate;
	}

	public String getTrDateStr() {
		return trDateStr;
	}

	public void setAccBalance(double accBalance) {
		this.accBalance = accBalance;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setLedTransType(String ledTransType) {
		this.ledTransType = ledTransType;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public void setTransactionWith(String transactionWith) {
		this.transactionWith = transactionWith;
	}

	public void setTrDate(Timestamp trDate) {
		this.trDate = trDate;
	}

	public void setTrDateStr(String trDateStr) {
		this.trDateStr = trDateStr;
	}

}
