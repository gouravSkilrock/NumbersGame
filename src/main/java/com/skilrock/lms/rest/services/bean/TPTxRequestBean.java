package com.skilrock.lms.rest.services.bean;

import java.io.Serializable;

public class TPTxRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int gameId;
	private long engineTxId;
	private String iwEngineTxId;
	private long engineSaleTxId;
	private int userMappingId;
	private double txAmount;

	private String txType;
	private String userName;
	private String sessionId;
	private String ticketNumber;

	private String plrMobileNumber;

	private String serviceCode;
	private String interfaceType;
	private int gameTypeId;

	private String channel;
	private String tokenId;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public long getEngineTxId() {
		return engineTxId;
	}

	public void setEngineTxId(long engineTxId) {
		this.engineTxId = engineTxId;
	}

	public String getIwEngineTxId() {
		return iwEngineTxId;
	}

	public void setIwEngineTxId(String iwEngineTxId) {
		this.iwEngineTxId = iwEngineTxId;
	}

	public long getEngineSaleTxId() {
		return engineSaleTxId;
	}

	public void setEngineSaleTxId(long engineSaleTxId) {
		this.engineSaleTxId = engineSaleTxId;
	}

	public int getUserMappingId() {
		return userMappingId;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public double getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(double txAmount) {
		this.txAmount = txAmount;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
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

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
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

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getTokenId()
	{
		return tokenId;
	}
	
	public void setTokenId(String tokenId)
	{
		this.tokenId=tokenId;
	}

	@Override
	public String toString() {
		return "TPTxRequestBean [gameId=" + gameId + ", engineTxId="
				+ engineTxId + ", iwEngineTxId=" + iwEngineTxId
				+ ", engineSaleTxId=" + engineSaleTxId + ", userMappingId="
				+ userMappingId + ", txAmount=" + txAmount + ", txType="
				+ txType + ", userName=" + userName + ", sessionId="
				+ sessionId + ", ticketNumber=" + ticketNumber
				+ ", plrMobileNumber=" + plrMobileNumber + ", serviceCode="
				+ serviceCode + ", interfaceType=" + interfaceType
				+ ", gameTypeId=" + gameTypeId + ", channel=" + channel + ", tokenId=" + tokenId + "]";
	}

}
