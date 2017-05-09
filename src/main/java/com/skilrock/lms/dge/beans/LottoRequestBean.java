package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LottoRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int game_no;
	private int gameId;
	private int isAdvancedPlay;
	private int noOfDraws;
	private int noPicked;
	private int partyId;
	private String partyType;
	private String playerData;
	private String playType;
	private String purchaseChannel;
	private String refMerchantId;
	private String refTransId;
	private double totalPurchaseAmt;
	private double unitPrice;
	private int userId;
	private int userMappingId;
	private int serviceId;
	private boolean isPromotkt;
	private List<String> drawDateTime;
	private int noOfPanel;
	String[] picknumbers;
	String pickedNumbers;

	public LottoRequestBean() {
	}

	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}

	public void setDrawIdTableMap(Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}

	public int getGame_no() {
		return game_no;
	}

	public void setGame_no(int gameNo) {
		game_no = gameNo;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public int getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(int noPicked) {
		this.noPicked = noPicked;
	}

	public int getPartyId() {
		return partyId;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public String getPlayerData() {
		return playerData;
	}

	public void setPlayerData(String playerData) {
		this.playerData = playerData;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getPurchaseChannel() {
		return purchaseChannel;
	}

	public void setPurchaseChannel(String purchaseChannel) {
		this.purchaseChannel = purchaseChannel;
	}

	public String getRefMerchantId() {
		return refMerchantId;
	}

	public void setRefMerchantId(String refMerchantId) {
		this.refMerchantId = refMerchantId;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public boolean isPromotkt() {
		return isPromotkt;
	}

	public void setPromotkt(boolean isPromotkt) {
		this.isPromotkt = isPromotkt;
	}

	public List<String> getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public int getNoOfPanel() {
		return noOfPanel;
	}

	public void setNoOfPanel(int noOfPanel) {
		this.noOfPanel = noOfPanel;
	}

	public String[] getPicknumbers() {
		return picknumbers;
	}

	public void setPicknumbers(String[] picknumbers) {
		this.picknumbers = picknumbers;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}
}