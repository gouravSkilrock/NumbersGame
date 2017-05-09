package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ZeroToNineRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int[] betAmountMultiple;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int game_no;
	private int gameId;
	private int isAdvancedPlay;
	private int isQP;
	private int[] isQuickPick;
	private int noOfDraws;
	private String[] noPicked;
	private int partyId;
	private String partyType;
	private String[] playerData;
	private String[] playType;
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
	private int totalNoOfPanels;
	private int[] noOfLines;

	public ZeroToNineRequestBean() {
	}

	public int[] getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public void setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
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

	public int getIsQP() {
		return isQP;
	}

	public void setIsQP(int isQP) {
		this.isQP = isQP;
	}

	public int[] getIsQuickPick() {
		return isQuickPick;
	}

	public void setIsQuickPick(int[] isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public String[] getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(String[] noPicked) {
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

	public String[] getPlayerData() {
		return playerData;
	}

	public void setPlayerData(String[] playerData) {
		this.playerData = playerData;
	}

	public String[] getPlayType() {
		return playType;
	}

	public void setPlayType(String[] playType) {
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

	public int getTotalNoOfPanels() {
		return totalNoOfPanels;
	}

	public void setTotalNoOfPanels(int totalNoOfPanels) {
		this.totalNoOfPanels = totalNoOfPanels;
	}

	public int[] getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int[] noOfLines) {
		this.noOfLines = noOfLines;
	}
}