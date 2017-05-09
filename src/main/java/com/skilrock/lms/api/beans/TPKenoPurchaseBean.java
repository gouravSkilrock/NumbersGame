package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;

public class TPKenoPurchaseBean implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	private String gameCode;
	private String errorCode;
	private String balance ;
	private String password;
	private List<PanelBean> panelList; 
	private String isAdvancePlay;
	private String[] drawId;
	private String totalPurchaseAmt;
	private String noOfDraws;
	private String userName;
	private String refTransId;
	private String lmsTranxId;
	private String ticketNumber;
	private String LastSoldTicketNo;
	private String purchaseTime;
	private String ticketCost;
	private List<String> drawDateTimeList;
	private boolean isRaffle;
	private List<String> topAdMessageList;
	private List<String> bottomAdMessageList;
	private RaffleBean raffleData;
	private boolean isSuccess;
	private String mobileNumber;
	public String getGameCode() {
		return gameCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public String getBalance() {
		return balance;
	}
	public String getPassword() {
		return password;
	}
	public List<PanelBean> getPanelList() {
		return panelList;
	}
	public String getIsAdvancePlay() {
		return isAdvancePlay;
	}
	public String[] getDrawId() {
		return drawId;
	}
	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}
	public String getNoOfDraws() {
		return noOfDraws;
	}
	public String getUserName() {
		return userName;
	}
	public String getRefTransId() {
		return refTransId;
	}
	public String getLmsTranxId() {
		return lmsTranxId;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public String getTicketCost() {
		return ticketCost;
	}
	public List<String> getDrawDateTimeList() {
		return drawDateTimeList;
	}
	public boolean isRaffle() {
		return isRaffle;
	}
	public List<String> getTopAdMessageList() {
		return topAdMessageList;
	}
	public List<String> getBottomAdMessageList() {
		return bottomAdMessageList;
	}
	public RaffleBean getRaffleData() {
		return raffleData;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPanelList(List<PanelBean> panelList) {
		this.panelList = panelList;
	}
	public void setIsAdvancePlay(String isAdvancePlay) {
		this.isAdvancePlay = isAdvancePlay;
	}
	public void setDrawId(String[] drawId) {
		this.drawId = drawId;
	}
	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}
	public void setNoOfDraws(String noOfDraws) {
		this.noOfDraws = noOfDraws;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}
	public void setLmsTranxId(String lmsTranxId) {
		this.lmsTranxId = lmsTranxId;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public void setTicketCost(String ticketCost) {
		this.ticketCost = ticketCost;
	}
	public void setDrawDateTimeList(List<String> drawDateTimeList) {
		this.drawDateTimeList = drawDateTimeList;
	}
	public void setRaffle(boolean isRaffle) {
		this.isRaffle = isRaffle;
	}
	public void setTopAdMessageList(List<String> topAdMessageList) {
		this.topAdMessageList = topAdMessageList;
	}
	public void setBottomAdMessageList(List<String> bottomAdMessageList) {
		this.bottomAdMessageList = bottomAdMessageList;
	}
	public void setRaffleData(RaffleBean raffleData) {
		this.raffleData = raffleData;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getLastSoldTicketNo() {
		return LastSoldTicketNo;
	}
	public void setLastSoldTicketNo(String lastSoldTicketNo) {
		LastSoldTicketNo = lastSoldTicketNo;
	}	
	
	
}
