package com.skilrock.lms.beans;

import java.io.Serializable;

public class DrawPendingSettlementBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int agentOrgId;
	private String agentOrgName;
	private int retailerOrgId;
	private String retailerOrgName;
	private String ticketNumber;
	private int processingTickets;
	private double processingAmount;
	private String cancelReason;
	private int delayExpTicket;
	private double delayExpAmount;
	private int unNotifyExpTicket;
	private double unNotifyExpAmount;
	private int serverErrorExpTicket;
	private double serverErrorExpAmount;
	private int totalExpTicket;
	private double totalExpAmount;

	public DrawPendingSettlementBean() {
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public int getRetailerOrgId() {
		return retailerOrgId;
	}

	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}

	public String getRetailerOrgName() {
		return retailerOrgName;
	}

	public void setRetailerOrgName(String retailerOrgName) {
		this.retailerOrgName = retailerOrgName;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public int getProcessingTickets() {
		return processingTickets;
	}

	public void setProcessingTickets(int processingTickets) {
		this.processingTickets = processingTickets;
	}

	public double getProcessingAmount() {
		return processingAmount;
	}

	public void setProcessingAmount(double processingAmount) {
		this.processingAmount = processingAmount;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public int getDelayExpTicket() {
		return delayExpTicket;
	}

	public void setDelayExpTicket(int delayExpTicket) {
		this.delayExpTicket = delayExpTicket;
	}

	public double getDelayExpAmount() {
		return delayExpAmount;
	}

	public void setDelayExpAmount(double delayExpAmount) {
		this.delayExpAmount = delayExpAmount;
	}

	public int getUnNotifyExpTicket() {
		return unNotifyExpTicket;
	}

	public void setUnNotifyExpTicket(int unNotifyExpTicket) {
		this.unNotifyExpTicket = unNotifyExpTicket;
	}

	public double getUnNotifyExpAmount() {
		return unNotifyExpAmount;
	}

	public void setUnNotifyExpAmount(double unNotifyExpAmount) {
		this.unNotifyExpAmount = unNotifyExpAmount;
	}

	public int getServerErrorExpTicket() {
		return serverErrorExpTicket;
	}

	public void setServerErrorExpTicket(int serverErrorExpTicket) {
		this.serverErrorExpTicket = serverErrorExpTicket;
	}

	public double getServerErrorExpAmount() {
		return serverErrorExpAmount;
	}

	public void setServerErrorExpAmount(double serverErrorExpAmount) {
		this.serverErrorExpAmount = serverErrorExpAmount;
	}

	public int getTotalExpTicket() {
		return totalExpTicket;
	}

	public void setTotalExpTicket(int totalExpTicket) {
		this.totalExpTicket = totalExpTicket;
	}

	public double getTotalExpAmount() {
		return totalExpAmount;
	}

	public void setTotalExpAmount(double totalExpAmount) {
		this.totalExpAmount = totalExpAmount;
	}
}