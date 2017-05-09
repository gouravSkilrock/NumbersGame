package com.skilrock.lms.api.beans;

import java.io.Serializable;

public class CancelBean implements Serializable {
	
	private double refundAmount;
	private String ticketNumber;
	private double balance;
	private String lmsTransId;
	private String errorCode;
	private String lmsTranxIdToRefund;
	private String refTransId;
	private boolean isSuccess;
	
	public double getRefundAmount() {
		return refundAmount;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public double getBalance() {
		return balance;
	}
	public String getLmsTransId() {
		return lmsTransId;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public String getLmsTranxIdToRefund() {
		return lmsTranxIdToRefund;
	}
	public String getRefTransId() {
		return refTransId;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void setLmsTransId(String lmsTransId) {
		this.lmsTransId = lmsTransId;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setLmsTranxIdToRefund(String lmsTranxIdToRefund) {
		this.lmsTranxIdToRefund = lmsTranxIdToRefund;
	}
	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	

}
