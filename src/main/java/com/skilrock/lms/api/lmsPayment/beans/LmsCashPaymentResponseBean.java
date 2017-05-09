package com.skilrock.lms.api.lmsPayment.beans;

import java.io.Serializable;

public class LmsCashPaymentResponseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorCode;
	private String lmsTranxId;
	private boolean isSuccess;
	private double amount;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getLmsTranxId() {
		return lmsTranxId;
	}
	public void setLmsTranxId(String lmsTranxId) {
		this.lmsTranxId = lmsTranxId;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getAmount() {
		return amount;
	}
	@Override
	public String toString() {
		return "LmsCashPaymentResponseBean [amount=" + amount + ", errorCode="
				+ errorCode + ", isSuccess=" + isSuccess + ", lmsTranxId="
				+ lmsTranxId + "]";
	}
	
	
}
