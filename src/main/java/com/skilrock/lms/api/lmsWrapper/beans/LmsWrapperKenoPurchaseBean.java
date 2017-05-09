package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LmsWrapperKenoPurchaseBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	Map<String, List<String>> advMsg;
	private int[] betAmountMultiple;
	private String bonus;
	private List<String> drawDateTime;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private List<String> drawNameList;
	
	private int game_no;
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
	private Object PromoPurchaseBean;
	private String promoSaleStatus="SUCCESS";
	private String purchaseChannel;
	private String purchaseTime;
	private Map<Integer, Map<Integer, String>> raffleDrawIdTableMap;
	private int raffleNo;
    private String retailerName;
   // private List<RafflePurchaseBean> rafflePurchaseBeanList ;
	
	private String refMerchantId;
	
	private String refTransId;
	private String reprintCount;
	private String saleStatus;
	
	private String ticket_no;
	private double totalPurchaseAmt;
	private double[] unitPrice;	
	private int userId;
	
	private boolean ticketExpiryEnabled;
	private String orgName;
	private int  expiryPeriod;
	private String orgAddress;
	private String currSymbol;
	private int barcodeCount;
	
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

	public List<String> getDrawNameList() {
		return drawNameList;
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

	public void setDrawIdTableMap(
			Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}

	public void setDrawNameList(List<String> drawNameList) {
		this.drawNameList = drawNameList;
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

	public void setRaffleDrawIdTableMap(
			Map<Integer, Map<Integer, String>> raffleDrawIdTableMap) {
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

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public boolean isTicketExpiryEnabled() {
		return ticketExpiryEnabled;
	}

	public void setTicketExpiryEnabled(boolean ticketExpiryEnabled) {
		this.ticketExpiryEnabled = ticketExpiryEnabled;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getExpiryPeriod() {
		return expiryPeriod;
	}

	public void setExpiryPeriod(int expiryPeriod) {
		this.expiryPeriod = expiryPeriod;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getCurrSymbol() {
		return currSymbol;
	}

	public void setCurrSymbol(String currSymbol) {
		this.currSymbol = currSymbol;
	}

	public int getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(int barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	
}
