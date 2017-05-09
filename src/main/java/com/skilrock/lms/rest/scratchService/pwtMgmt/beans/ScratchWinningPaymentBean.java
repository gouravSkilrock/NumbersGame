package com.skilrock.lms.rest.scratchService.pwtMgmt.beans;

public class ScratchWinningPaymentBean {

	private double winAmt;
	private String ticketNumber;
	private String virnNumber;
	private String winStatus;

	public double getWinAmt() {
		return winAmt;
	}

	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getVirnNumber() {
		return virnNumber;
	}

	public void setVirnNumber(String virnNumber) {
		this.virnNumber = virnNumber;
	}

	public String getWinStatus() {
		return winStatus;
	}

	public void setWinStatus(String winStatus) {
		this.winStatus = winStatus;
	}

}
