package com.skilrock.lms.instantWin.javaBeans;


public class CancelTicketBean {
	private String serviceCode;
	private String interfaceType;	
	private String userName;
	private String sessionId;
	private String txType;
	private int gameId;
	private int gameTypeId;
	private long ticketNumber;
	private String engineTxId;
	private double txAmount;
	private int tktMerchantUserId;
	private boolean isPromoTicket;
	private long merTxId;
	private int statusCode;
	
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public long getMerTxId() {
		return merTxId;
	}
	public void setMerTxId(long merTxId) {
		this.merTxId = merTxId;
	}
	public boolean isPromoTicket() {
		return isPromoTicket;
	}
	public void setPromoTicket(boolean isPromoTicket) {
		this.isPromoTicket = isPromoTicket;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getGameTypeId() {
		return gameTypeId;
	}
	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}
	public long getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(long ticketNumber) {
		this.ticketNumber = ticketNumber;
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
	public int getTktMerchantUserId() {
		return tktMerchantUserId;
	}
	public void setTktMerchantUserId(int tktMerchantUserId) {
		this.tktMerchantUserId = tktMerchantUserId;
	}
	
	
	
}