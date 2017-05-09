package com.skilrock.lms.beans;

import java.io.Serializable;

public class RegionWiseBankDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String region;
	private String bankName;
	private String branchName;
	private String txnRef;
	private String txnDate;
	private String cashierName;
	private String customerName;
	private String terminalId;
	private String lmcId;
	private String transactionType;
	private String amount;

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getTxnRef() {
		return txnRef;
	}

	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getLmcId() {
		return lmcId;
	}

	public void setLmcId(String lmcId) {
		this.lmcId = lmcId;
	}

	public String getAmount() {
		return amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "RegionWiseBankDetailBean [region=" + region + ", bankName="
				+ bankName + ", branchName=" + branchName + ", txnRef="
				+ txnRef + ", txnDate=" + txnDate + ", cashierName="
				+ cashierName + ", customerName=" + customerName
				+ ", terminalId=" + terminalId + ", lmcId=" + lmcId
				+ ", transactionType=" + transactionType + ", amount=" + amount
				+ "]";
	}

}
