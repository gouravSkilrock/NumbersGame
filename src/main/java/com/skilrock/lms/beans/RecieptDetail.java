package com.skilrock.lms.beans;

import java.sql.Date;

public class RecieptDetail {

	private String genratedId;
	private String orgName;
	private String orgType;
	private String ownerName;
	private String receiptId;
	private String recieptType;
	private Date transactionDate;
	private double amount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getGenratedId() {
		return genratedId;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public String getRecieptType() {
		return recieptType;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setGenratedId(String genratedId) {
		this.genratedId = genratedId;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	/**
	 * @param ownerName
	 *            the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public void setRecieptType(String recieptType) {
		this.recieptType = recieptType;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

}
