package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;


public class TPLottoPurchaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String noOfDraws;
	private String noPicked;
	private String[] pickedNumbers;
	private String userName;
	private String refTransId;
	private String ticketNumber;
	private String LastSoldTicketNo;
	private String isAdvancedPlay;
	
	private String totalPurchaseAmt;
	private String purchaseTime;
	private List<String> drawDateTime;
	private String errorCode;
	private String balance ;
	private String password;
	private String gameCode;
	private String ticketCost;
	private String lmsTranxId;
	private Integer[] qpStatus;
	private boolean isRaffle;
	private boolean isPromotkt;
	private List<TPLottoPurchaseBean> promoPurchaseBeanList;
	private String playType;
	private List<String> topAdMessageList;
	private List<String> bottomAdMessageList;
	private RaffleBean raffleData;
	private boolean isSuccess;
	private String mobileNumber;
	public String getNoOfDraws() {
		return noOfDraws;
	}
	public String getNoPicked() {
		return noPicked;
	}
	public String[] getPickedNumbers() {
		return pickedNumbers;
	}
	public String getUserName() {
		return userName;
	}
	public String getRefTransId() {
		return refTransId;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public String getIsAdvancedPlay() {
		return isAdvancedPlay;
	}
	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public List<String> getDrawDateTime() {
		return drawDateTime;
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
	public String getGameCode() {
		return gameCode;
	}
	public String getTicketCost() {
		return ticketCost;
	}
	public String getLmsTranxId() {
		return lmsTranxId;
	}
	public Integer[] getQpStatus() {
		return qpStatus;
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
	public void setNoOfDraws(String noOfDraws) {
		this.noOfDraws = noOfDraws;
	}
	public void setNoPicked(String noPicked) {
		this.noPicked = noPicked;
	}
	public void setPickedNumbers(String[] pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public void setIsAdvancedPlay(String isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}
	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}
	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public void setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
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
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public void setTicketCost(String ticketCost) {
		this.ticketCost = ticketCost;
	}
	public void setLmsTranxId(String lmsTranxId) {
		this.lmsTranxId = lmsTranxId;
	}
	public void setQpStatus(Integer[] qpStatus) {
		this.qpStatus = qpStatus;
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
	public boolean isPromotkt() {
		return isPromotkt;
	}
	public void setPromotkt(boolean isPromotkt) {
		this.isPromotkt = isPromotkt;
	}
	public List<TPLottoPurchaseBean> getPromoPurchaseBeanList() {
		return promoPurchaseBeanList;
	}
	public void setPromoPurchaseBeanList(
			List<TPLottoPurchaseBean> promoPurchaseBeanList) {
		this.promoPurchaseBeanList = promoPurchaseBeanList;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	
	
	
}
