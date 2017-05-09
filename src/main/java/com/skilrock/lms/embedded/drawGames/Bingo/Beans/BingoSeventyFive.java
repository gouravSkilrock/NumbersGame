package com.skilrock.lms.embedded.drawGames.Bingo.Beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BingoSeventyFive implements Serializable {

	private static final long serialVersionUID = 1L;
	Map<String, List<String>> advMsg;
	private int[] betAmountMultiple;
	private String bonus;
	private List<String> drawDateTime;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private List<String> drawNameList;
	private int game_no;
	private int gameId;
	private String gameDispName;
	private int isAdvancedPlay;
	private boolean isPromotkt;
	private int[] isQuickPick;
	private boolean isRaffelAssociated;
	private int noOfDraws;
	private int[] noOfLines;
	private String LastSoldTicketNo;
	private int noOfPanel;
	private String[] noPicked;
	private int partyId;
	private String partyType;
	private String[] playerData;

	private List<String>[] playerPicked;
	private String[] playType;
	private String[] betDispName;
	private Object PromoPurchaseBean;
	private String promoSaleStatus = "SUCCESS";
	private String purchaseChannel;
	private String purchaseTime;
	private String barcodeType;
	private Map<Integer, Map<Integer, String>> raffleDrawIdTableMap;
	private int raffleNo;

	private String refMerchantId;

	private String refTransId;
	private String reprintCount;
	private String saleStatus;

	private String ticket_no;
	private double totalPurchaseAmt;
	private double[] unitPrice;
	private int userId;
	private int userMappingId;
	private int serviceId;
	private String plrMobileNumber;
	private short barcodeCount;
	private String actionName;
	private int lastGameId;
	private String deviceType;
	private int noofDrawsForPromo;
	private boolean[] QPPreGenerated;

	public boolean[] getQPPreGenerated() {
		return QPPreGenerated;
	}

	public void setQPPreGenerated(boolean[] qPPreGenerated) {
		QPPreGenerated = qPPreGenerated;
	}

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public int[] getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public String getBonus() {
		return bonus;
	}

	public List<String> getDrawDateTime() {
		return drawDateTime;
	}

	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}

	public int getGame_no() {
		return game_no;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public int[] getIsQuickPick() {
		return isQuickPick;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public int[] getNoOfLines() {
		return noOfLines;
	}

	public int getNoOfPanel() {
		return noOfPanel;
	}

	public String[] getNoPicked() {
		return noPicked;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public String[] getPlayerData() {
		return playerData;
	}

	public List<String>[] getPlayerPicked() {
		return playerPicked;
	}

	public String[] getPlayType() {
		return playType;
	}

	public Object getPromoPurchaseBean() {
		return PromoPurchaseBean;
	}

	public String getPromoSaleStatus() {
		return promoSaleStatus;
	}

	public String getPurchaseChannel() {
		return purchaseChannel;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public Map<Integer, Map<Integer, String>> getRaffleDrawIdTableMap() {
		return raffleDrawIdTableMap;
	}

	public int getRaffleNo() {
		return raffleNo;
	}

	public String getRefMerchantId() {
		return refMerchantId;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public String getReprintCount() {
		return reprintCount;
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public String getTicket_no() {
		return ticket_no;
	}

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public double[] getUnitPrice() {
		return unitPrice;
	}

	public int getUserId() {
		return userId;
	}

	public boolean isPromotkt() {
		return isPromotkt;
	}

	public boolean isRaffelAssociated() {
		return isRaffelAssociated;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public void setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public void setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawIdTableMap(Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}

	public void setGame_no(int game_no) {
		this.game_no = game_no;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setIsQuickPick(int[] isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setNoOfLines(int[] noOfLines) {
		this.noOfLines = noOfLines;
	}

	public void setNoOfPanel(int noOfPanel) {
		this.noOfPanel = noOfPanel;
	}

	public void setNoPicked(String[] noPicked) {
		this.noPicked = noPicked;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setPlayerData(String[] playerData) {
		this.playerData = playerData;
	}

	public void setPlayerPicked(List<String>[] playerPicked) {
		this.playerPicked = playerPicked;
	}

	public void setPlayType(String[] playType) {
		this.playType = playType;
	}

	public void setPromoPurchaseBean(Object promoPurchaseBean) {
		PromoPurchaseBean = promoPurchaseBean;
	}

	public void setPromoSaleStatus(String promoSaleStatus) {
		this.promoSaleStatus = promoSaleStatus;
	}

	public void setPromotkt(boolean isPromotkt) {
		this.isPromotkt = isPromotkt;
	}

	public void setPurchaseChannel(String purchaseChannel) {
		this.purchaseChannel = purchaseChannel;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public void setRaffelAssociated(boolean isRaffelAssociated) {
		this.isRaffelAssociated = isRaffelAssociated;
	}

	public void setRaffleDrawIdTableMap(Map<Integer, Map<Integer, String>> raffleDrawIdTableMap) {
		this.raffleDrawIdTableMap = raffleDrawIdTableMap;
	}

	public void setRaffleNo(int raffleNo) {
		this.raffleNo = raffleNo;
	}

	public void setRefMerchantId(String refMerchantId) {
		this.refMerchantId = refMerchantId;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}

	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}

	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}

	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public void setUnitPrice(double[] unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLastSoldTicketNo() {
		return LastSoldTicketNo;
	}

	public void setLastSoldTicketNo(String lastSoldTicketNo) {
		LastSoldTicketNo = lastSoldTicketNo;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public short getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(short barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	public String getBarcodeType() {
		return barcodeType;
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

	public List<String> getDrawNameList() {
		return drawNameList;
	}

	public void setDrawNameList(List<String> drawNameList) {
		this.drawNameList = drawNameList;
	}

	public int getNoofDrawsForPromo() {
		return noofDrawsForPromo;
	}

	public void setNoofDrawsForPromo(int noofDrawsForPromo) {
		this.noofDrawsForPromo = noofDrawsForPromo;
	}

	public String[] getBetDispName() {
		return betDispName;
	}

	public void setBetDispName(String[] betDispName) {
		this.betDispName = betDispName;
	}
}