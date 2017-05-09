package com.skilrock.lms.coreEngine.sportsLottery.beans;


public class PwtVerifyTicketBean {

	private String merchantName;
	private long ticketNumber;
	private double totalWinAmt;
	private int noOfDraws;
	private PwtVerifyTicketDrawDataBean[] verifyTicketDrawDataBeanArray;
	private String gameName;
	private String gameTypename;
	private int gameId;
	private int gameTypeId;
	
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public long getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(long ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public double getTotalWinAmt() {
		return totalWinAmt;
	}
	public void setTotalWinAmt(double totalWinAmt) {
		this.totalWinAmt = totalWinAmt;
	}
	
	public PwtVerifyTicketDrawDataBean[] getVerifyTicketDrawDataBeanArray() {
		return verifyTicketDrawDataBeanArray;
	}
	public void setVerifyTicketDrawDataBeanArray(
			PwtVerifyTicketDrawDataBean[] verifyTicketDrawDataBeanArray) {
		this.verifyTicketDrawDataBeanArray = verifyTicketDrawDataBeanArray;
	}
	public int getNoOfDraws() {
		return noOfDraws;
	}
	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameTypename() {
		return gameTypename;
	}
	public void setGameTypename(String gameTypename) {
		this.gameTypename = gameTypename;
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
	
	
	
	
}
