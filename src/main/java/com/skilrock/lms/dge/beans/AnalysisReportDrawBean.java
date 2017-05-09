package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class AnalysisReportDrawBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double totalAmount;
	private String drawFreezeTime;
	// Draw Specific Information
	//private String drawStatus;

	private String playerPickData;	
	
	private String purchaseTime;
	private String ticketNumber;
	private String playType;
	private Double Winningamount ;
	private String partyId;
	private String partyName;
	private String ticketStatus;
	private String tktPurchaseTime;

	
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}
	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}
	public String getPlayerPickData() {
		return playerPickData;
	}
	public void setPlayerPickData(String playerPickData) {
		this.playerPickData = playerPickData;
	}
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public Double getWinningamount() {
		return Winningamount;
	}
	public void setWinningamount(Double winningamount) {
		this.Winningamount = winningamount;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	public String getTktPurchaseTime() {
		return tktPurchaseTime;
	}
	public void setTktPurchaseTime(String tktPurchaseTime) {
		this.tktPurchaseTime = tktPurchaseTime;
	}
	
}
