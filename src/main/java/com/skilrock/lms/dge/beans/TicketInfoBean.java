package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class TicketInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ticketNo;
	private double totalPwtAmt;
	private int partyId;
	private String ticketStatus;
	public Long getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(Long ticketNo) {
		this.ticketNo = ticketNo;
	}
	public double getTotalPwtAmt() {
		return totalPwtAmt;
	}
	public void setTotalPwtAmt(double totalPwtAmt) {
		this.totalPwtAmt = totalPwtAmt;
	}
	public int getPartyId() {
		return partyId;
	}
	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	public String getTicketStatus() {
		return ticketStatus;
	}
	
	
}
