package com.skilrock.lms.coreEngine.virtualSport.beans;

import com.google.gson.annotations.SerializedName;

public class TPSaleRequestBean {
	private int gameId;
	@SerializedName("ext_id")private int extId;
	@SerializedName("unit_id") private int unitId;
	@SerializedName("staff_id")private int staffId;
	@SerializedName("tmp_id")private String tmpId;
	private double amount;
	private String currency;
	@SerializedName("event_id")private long eventId;
	@SerializedName("event_type")private String eventType;
	private double taxes;
	private double jackpot;
	private double megajackpot;
	private double retBalanceBeforeSale;
	private double retBalanceAfterSale;
	private String ticketNumber;
	private String txnId;
	
	public int getExtId() {
		return extId;
	}
	public void setExtId(int extId) {
		this.extId = extId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getStaffId() {
		return staffId;
	}
	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
	public String getTmpId() {
		return tmpId;
	}
	public void setTmpId(String tmpId) {
		this.tmpId = tmpId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public double getTaxes() {
		return taxes;
	}
	public void setTaxes(double taxes) {
		this.taxes = taxes;
	}
	public double getJackpot() {
		return jackpot;
	}
	public void setJackpot(double jackpot) {
		this.jackpot = jackpot;
	}
	public double getMegajackpot() {
		return megajackpot;
	}
	public void setMegajackpot(double megajackpot) {
		this.megajackpot = megajackpot;
	}
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public double getRetBalanceBeforeSale() {
		return retBalanceBeforeSale;
	}
	public void setRetBalanceBeforeSale(double retBalanceBeforeSale) {
		this.retBalanceBeforeSale = retBalanceBeforeSale;
	}
	public double getRetBalanceAfterSale() {
		return retBalanceAfterSale;
	}
	public void setRetBalanceAfterSale(double retBalanceAfterSale) {
		this.retBalanceAfterSale = retBalanceAfterSale;
	}
	
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	
	
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	@Override
	public String toString() {
		return "TPSaleRequestBean [amount=" + amount + ", currency=" + currency
				+ ", eventId=" + eventId + ", eventType=" + eventType
				+ ", extId=" + extId + ", gameId=" + gameId + ", jackpot="
				+ jackpot + ", megajackpot=" + megajackpot
				+ ", retBalanceAfterSale=" + retBalanceAfterSale
				+ ", retBalanceBeforeSale=" + retBalanceBeforeSale
				+ ", staffId=" + staffId + ", taxes=" + taxes
				+ ", ticketNumber=" + ticketNumber + ", tmpId=" + tmpId
				+ ", txnId=" + txnId + ", unitId=" + unitId + "]";
	}
	
	
	
	
	
	
	

}
