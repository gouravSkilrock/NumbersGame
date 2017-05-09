package com.skilrock.lms.coreEngine.sportsLottery.beans;


public class SportsLotteryGamePlayBean {

	private int gameId;
	private int gameTypeId;
	private int noOfBoard;
	private double unitPrice;
	private double totalPurchaseAmt;
	private SportsLotteryGameDrawDataBean[]  gameDrawDataBeanArray;
	private String serviceCode;
	private String interfaceType;
	private String merchantName;
	private int userId;
	private String userType;
	private String refTransId;
	private Integer[] drawIdArray;
	private long ticketNumber;
	private String purchaseTime;
	private int reprintCount;
	private String plrMobNo;
	private int barcodeCount;
	private boolean isPromoTicket;
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
	public SportsLotteryGameDrawDataBean[] getGameDrawDataBeanArray() {
		return gameDrawDataBeanArray;
	}
	public void setGameDrawDataBeanArray(
			SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray) {
		this.gameDrawDataBeanArray = gameDrawDataBeanArray;
	}
	public int getNoOfBoard() {
		return noOfBoard;
	}
	public void setNoOfBoard(int noOfBoard) {
		this.noOfBoard = noOfBoard;
	}
	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}
	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
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
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getRefTransId() {
		return refTransId;
	}
	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}
	
	public Integer[] getDrawIdArray() {
		return drawIdArray;
	}
	public void setDrawIdArray(Integer[] drawIdArray) {
		this.drawIdArray = drawIdArray;
	}
	public long getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(long ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public int getReprintCount() {
		return reprintCount;
	}
	public void setReprintCount(int reprintCount) {
		this.reprintCount = reprintCount;
	}
	public String getPlrMobNo() {
		return plrMobNo;
	}
	public void setPlrMobNo(String plrMobNo) {
		this.plrMobNo = plrMobNo;
	}
	public int getBarcodeCount() {
		return barcodeCount;
	}
	public void setBarcodeCount(int barcodeCount) {
		this.barcodeCount = barcodeCount;
	}
	public boolean getIsPromoTicket() {
		return isPromoTicket;
	}
	public void setIsPromoTicket(boolean isPromoTicket) {
		this.isPromoTicket = isPromoTicket;
	}
	
	
	
}
