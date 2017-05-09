package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class LottoPurchaseBean extends CommonPurchaseBean implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Map<String, List<String>> advMsg;
	//private int[] betAmountMultiple;
	//private List drawDateTime;
	//Map<Integer, Map<Integer, String>> drawIdTableMap;
	private List<String> drawNameList;
	//private int game_no;
	//private String gameDispName;
	//private int isAdvancedPlay;
	//private boolean isPromotkt;
	private short barcodeCount;
	private String barcodeType;
	private int gameId;
	public List<LottoPurchaseBean> getPromoPurchaseBeanList() {
		return promoPurchaseBeanList;
	}

	public void setPromoPurchaseBeanList(
			List<LottoPurchaseBean> promoPurchaseBeanList) {
		this.promoPurchaseBeanList = promoPurchaseBeanList;
	}

	private Integer[] isQuickPick;
	//private boolean isRaffelAssociated;
	//private int noOfDraws;
	private int noOfLines;
	private int noPicked;
	private int[] panel_id;
	//private int partyId;
	//private String partyType;
	private String pickedNumbers;
	private String[] picknumbers;
	private List<String> playerPicked;
	// For Zimlotto Two
	private String playType;
	//private Object PromoPurchaseBean;
	private List<LottoPurchaseBean> promoPurchaseBeanList;
	//private String purchaseChannel;
	//private String purchaseTime;
	private int betAmtMultiple; // added for bonusball two
	private String LastSoldTicketNo;
	//Map<Integer, Map<Integer, String>> raffleDrawIdTableMap;

	//private int raffleNo;

	//private List<RafflePurchaseBean> rafflePurchaseBeanList;
	//private String refMerchantId;
	//private String refTransId;

	//private String reprintCount;
	//private String saleStatus;

	//private String ticket_no;
	//private double totalPurchaseAmt;
	private double unitPrice;
	//private int userId;
	private String promoSaleStatus = "SUCCESS";
	private String plrMobileNumber;
	private int userMappingId;
	private int serviceId;
	private String actionName;
	private int lastGameId;
	private String deviceType;
	private boolean QPPreGenerated;
	
	public List<String> getDrawNameList() {
		return drawNameList;
	}

	public void setDrawNameList(List<String> drawNameList) {
		this.drawNameList = drawNameList;
	}

	public short getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(short barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public String getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public Integer[] getIsQuickPick() {
		return isQuickPick;
	}

	public void setIsQuickPick(Integer[] isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

	public int getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(int noPicked) {
		this.noPicked = noPicked;
	}

	public int[] getPanel_id() {
		return panel_id;
	}

	public void setPanel_id(int[] panelId) {
		panel_id = panelId;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public String[] getPicknumbers() {
		return picknumbers;
	}

	public void setPicknumbers(String[] picknumbers) {
		this.picknumbers = picknumbers;
	}

	public List<String> getPlayerPicked() {
		return playerPicked;
	}

	public void setPlayerPicked(List<String> playerPicked) {
		this.playerPicked = playerPicked;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public int getBetAmtMultiple() {
		return betAmtMultiple;
	}

	public void setBetAmtMultiple(int betAmtMultiple) {
		this.betAmtMultiple = betAmtMultiple;
	}

	public String getLastSoldTicketNo() {
		return LastSoldTicketNo;
	}

	public void setLastSoldTicketNo(String lastSoldTicketNo) {
		LastSoldTicketNo = lastSoldTicketNo;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getPromoSaleStatus() {
		return promoSaleStatus;
	}

	public void setPromoSaleStatus(String promoSaleStatus) {
		this.promoSaleStatus = promoSaleStatus;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public int getUserMappingId() {
		return userMappingId;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public int getLastGameId() {
		return lastGameId;
	}

	public void setLastGameId(int lastGameId) {
		this.lastGameId = lastGameId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isQPPreGenerated() {
		return QPPreGenerated;
	}

	public void setQPPreGenerated(boolean qPPreGenerated) {
		QPPreGenerated = qPPreGenerated;
	}
	
}