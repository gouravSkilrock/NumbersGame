package com.skilrock.lms.beans;

import java.io.Serializable;

public class OlaPendingWithdrawalBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pendingWithdrawalCode = null;
	private Double amount;
	private String withdrawRequestDate = null;
	public String getPendingWithdrawalCode() {
		return pendingWithdrawalCode;
	}
	public void setPendingWithdrawalCode(String pendingWithdrawalCode) {
		this.pendingWithdrawalCode = pendingWithdrawalCode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getWithdrawRequestDate() {
		return withdrawRequestDate;
	}
	public void setWithdrawRequestDate(String withdrawRequestDate) {
		this.withdrawRequestDate = withdrawRequestDate;
	}
}
