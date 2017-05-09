package com.skilrock.lms.keba.drawGames.javaBeans;

import java.io.Serializable;

public class FFTCancelTicketBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean success;
	private String errorMessage;
	private String ticketNumber;
	private double refundAmount;
	private double balance;

	public FFTCancelTicketBean() {
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}