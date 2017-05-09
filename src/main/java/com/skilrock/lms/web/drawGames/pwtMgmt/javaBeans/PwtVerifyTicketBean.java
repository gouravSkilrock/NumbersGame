package com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PwtVerifyTicketBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int responseCode;
	private String responseMsg;
	private String merchantName;
	private String merchantCode;
	private String ticketNumber;
	private String purchaseDateTime;
	private double totalPurchaseAmt;
	private double tktPurchaseAmt;
	private double totalWinAmt;
	private int noOfDraws;
	private List<PwtVerifyTicketDrawDataBean> verifyTicketDrawDataBeanList;
	private String gameName;
	private int gameId;
	private String userName;
	private int userId;
	private String ticketStatus;
	private String phoneNumber;
	private Map<String, List<String>> advMsg;
	private String panelId;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	private String recieptNumber;
	
	public String getRecieptNumber() {
		return recieptNumber;
	}

	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getPurchaseDateTime() {
		return purchaseDateTime;
	}

	public void setPurchaseDateTime(String purchaseDateTime) {
		this.purchaseDateTime = purchaseDateTime;
	}

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public double getTotalWinAmt() {
		return totalWinAmt;
	}

	public void setTotalWinAmt(double totalWinAmt) {
		this.totalWinAmt = totalWinAmt;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public List<PwtVerifyTicketDrawDataBean> getVerifyTicketDrawDataBeanList() {
		return verifyTicketDrawDataBeanList;
	}

	public void setVerifyTicketDrawDataBeanList(
			List<PwtVerifyTicketDrawDataBean> verifyTicketDrawDataBeanList) {
		this.verifyTicketDrawDataBeanList = verifyTicketDrawDataBeanList;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getTktPurchaseAmt() {
		return tktPurchaseAmt;
	}

	public void setTktPurchaseAmt(double tktPurchaseAmt) {
		this.tktPurchaseAmt = tktPurchaseAmt;
	}

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}
}