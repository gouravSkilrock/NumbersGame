package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class CommonPurchaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Map<String, List<String>> advMsg;
	private int[] betAmountMultiple;
	
	private List<String> drawDateTime;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int game_no;
	private String gameDispName;
	private int isAdvancedPlay;
	private boolean isPromotkt;
	
	private boolean isRaffelAssociated;
	private int noOfDraws;
	
	
	private int partyId;
	private String partyType;
	
	private Object PromoPurchaseBean;
	private String purchaseChannel;
	private String purchaseTime;
	Map<Integer, Map<Integer, String>> raffleDrawIdTableMap;
	private int raffleNo;
	private List<RafflePurchaseBean> rafflePurchaseBeanList;
	private String refMerchantId;
	private String refTransId;
	private String reprintCount;
	private String saleStatus;
	private String ticket_no;
	private double totalPurchaseAmt;
	private int userId;
	
	
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
	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}
	public void setDrawIdTableMap(Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}
	public int getGame_no() {
		return game_no;
	}
	public void setGame_no(int game_no) {
		this.game_no = game_no;
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
	public String getTicket_no() {
		return ticket_no;
	}
	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}
	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}
	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<String> getDrawDateTime() {
		return drawDateTime;
	}
	public void setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	
}
