package com.skilrock.lms.coreEngine.virtualSport.beans;

import java.io.Serializable;

import com.google.gson.JsonArray;

public class TPTxRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String engineTxId;
	private double txAmount;
	private int unitId;
	private String ticketNumber;
	private double estimatedMaxWin;
	private JsonArray eventInfoArray;
	
	public double getEstimatedMaxWin() {
		return estimatedMaxWin;
	}
	public void setEstimatedMaxWin(double estimatedMaxWin) {
		this.estimatedMaxWin = estimatedMaxWin;
	}
	public String getEngineTxId() {
		return engineTxId;
	}
	public void setEngineTxId(String engineTxId) {
		this.engineTxId = engineTxId;
	}
	public double getTxAmount() {
		return txAmount;
	}
	public void setTxAmount(double txAmount) {
		this.txAmount = txAmount;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	
	public JsonArray getEventInfoArray() {
		return eventInfoArray;
	}
	public void setEventInfoArray(JsonArray eventInfoArray) {
		this.eventInfoArray = eventInfoArray;
	}
	@Override
	public String toString() {
		return "TPTxRequestBean [engineTxId=" + engineTxId + ", txAmount="
				+ txAmount + ", unitId=" + unitId + ", ticketNumber="
				+ ticketNumber + ", estimatedMaxWin=" + estimatedMaxWin
				+ ", eventInfoArray=" + eventInfoArray + "]";
	}

	
	
	
	

}
