package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ZeroToNinePurchaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, List<String>> advMsg;
	private int[] betAmountMultiple;
	private List<String> drawDateTime;
	private Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int game_no;
	private String gameDispName;
	private int isAdvancedPlay;
	private boolean isPromotkt;
	private int isQP;
	private int[] isQuickPick;
	private boolean isRaffelAssociated;
	private int noOfDraws;
	private int noOfPanel;
	private String noOfPanels;
	private String[] noPicked;
	private int[] panel_id;
	private int partyId;
	private String partyType;
	private String[] playerData;
	List<Integer> pickedNumbers;
	private String[] playType;
	private Object PromoPurchaseBean;
	private String purchaseChannel;
	private String purchaseTime;
	private Map<Integer, Map<Integer, String>> raffleDrawIdTableMap;
	private String bonus;
	private int raffleNo;
	private List<RafflePurchaseBean> rafflePurchaseBeanList;
	private String refMerchantId;
	private String refTransId;
	private String reprintCount;
	private String saleStatus;
	private String symbols;
	private String ticket_no;
	private int totalNoOfPanels;
	private double totalPurchaseAmt;
	private double unitPrice;	
	private int userId;
	private int[] noOfLines; //added for new bet types banker2 & 3
	private String LastSoldTicketNo;
	private String plrMobileNumber;
	private String barcodeType;
	private int gameId;
	private short barcodeCount;
	private int userMappingId;
	private int serviceId;
	private String actionName;
	private int lastGameId;
	private String deviceType;
    private String claimEndTime;
    
	public String getClaimEndTime() {
		return claimEndTime;
	}

	public void setClaimEndTime(String claimEndTime) {
		this.claimEndTime = claimEndTime;
	}

	public ZeroToNinePurchaseBean() {
	}

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public int[] getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public void setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public List<String> getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
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

	public String getGameDispName() {
		return gameDispName;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public boolean isPromotkt() {
		return isPromotkt;
	}

	public void setPromotkt(boolean isPromotkt) {
		this.isPromotkt = isPromotkt;
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

	public boolean isRaffelAssociated() {
		return isRaffelAssociated;
	}

	public void setRaffelAssociated(boolean isRaffelAssociated) {
		this.isRaffelAssociated = isRaffelAssociated;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public int getNoOfPanel() {
		return noOfPanel;
	}

	public void setNoOfPanel(int noOfPanel) {
		this.noOfPanel = noOfPanel;
	}

	public String getNoOfPanels() {
		return noOfPanels;
	}

	public void setNoOfPanels(String noOfPanels) {
		this.noOfPanels = noOfPanels;
	}

	public String[] getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(String[] noPicked) {
		this.noPicked = noPicked;
	}

	public int[] getPanel_id() {
		return panel_id;
	}

	public void setPanel_id(int[] panelId) {
		panel_id = panelId;
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

	public List<Integer> getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(List<Integer> pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public String[] getPlayType() {
		return playType;
	}

	public void setPlayType(String[] playType) {
		this.playType = playType;
	}

	public Object getPromoPurchaseBean() {
		return PromoPurchaseBean;
	}

	public void setPromoPurchaseBean(Object promoPurchaseBean) {
		PromoPurchaseBean = promoPurchaseBean;
	}

	public String getPurchaseChannel() {
		return purchaseChannel;
	}

	public void setPurchaseChannel(String purchaseChannel) {
		this.purchaseChannel = purchaseChannel;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public Map<Integer, Map<Integer, String>> getRaffleDrawIdTableMap() {
		return raffleDrawIdTableMap;
	}

	public void setRaffleDrawIdTableMap(
			Map<Integer, Map<Integer, String>> raffleDrawIdTableMap) {
		this.raffleDrawIdTableMap = raffleDrawIdTableMap;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public int getRaffleNo() {
		return raffleNo;
	}

	public void setRaffleNo(int raffleNo) {
		this.raffleNo = raffleNo;
	}

	public List<RafflePurchaseBean> getRafflePurchaseBeanList() {
		return rafflePurchaseBeanList;
	}

	public void setRafflePurchaseBeanList(
			List<RafflePurchaseBean> rafflePurchaseBeanList) {
		this.rafflePurchaseBeanList = rafflePurchaseBeanList;
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

	public String getReprintCount() {
		return reprintCount;
	}

	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}

	public String getSymbols() {
		return symbols;
	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

	public String getTicket_no() {
		return ticket_no;
	}

	public void setTicket_no(String ticketNo) {
		ticket_no = ticketNo;
	}

	public int getTotalNoOfPanels() {
		return totalNoOfPanels;
	}

	public void setTotalNoOfPanels(int totalNoOfPanels) {
		this.totalNoOfPanels = totalNoOfPanels;
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

	public int[] getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int[] noOfLines) {
		this.noOfLines = noOfLines;
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

	public short getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(short barcodeCount) {
		this.barcodeCount = barcodeCount;
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
}