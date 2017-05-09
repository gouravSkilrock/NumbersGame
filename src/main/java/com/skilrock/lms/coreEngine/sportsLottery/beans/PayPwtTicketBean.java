package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.util.Map;

public class PayPwtTicketBean {

	private long ticketNumber;
	private int userId;
	private String userType;
	private String merchantName;
	private String winningChannel;
	Map<Integer,Map<Integer, String>> drawRefTransMap;
	public long getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(long ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public String getWinningChannel() {
		return winningChannel;
	}
	public void setWinningChannel(String winningChannel) {
		this.winningChannel = winningChannel;
	}
	public Map<Integer, Map<Integer, String>> getDrawRefTransMap() {
		return drawRefTransMap;
	}
	public void setDrawRefTransMap(
			Map<Integer, Map<Integer, String>> drawRefTransMap) {
		this.drawRefTransMap = drawRefTransMap;
	}
	
	
	
	
}
