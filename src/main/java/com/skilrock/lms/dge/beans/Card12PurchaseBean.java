package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Card12PurchaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] betAmountMultiple;
	private List drawDateTime;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int game_no;
	private int isAdvancedPlay;
	private Integer[] isQuickPick;
	private int noOfDraws;
	private int[] panel_id;
	private int partyId;
	private String partyType;
	private List<Integer> pickedNumbers;
	private String purchaseChannel;
	private String purchaseTime;
	private String refTransId;
	private String reprintCount;
	private String saleStatus;
	private String ticket_no;
	private double totalPurchaseAmt;

	public int[] getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public List getDrawDateTime() {
		return drawDateTime;
	}

	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}

	public int getGame_no() {
		return game_no;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public Integer[] getIsQuickPick() {
		return isQuickPick;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public int[] getPanel_id() {
		return panel_id;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public List<Integer> getPickedNumbers() {
		return pickedNumbers;
	}

	public String getPurchaseChannel() {
		return purchaseChannel;
	}

	public String getPurchaseTime() {
		return purchaseTime;
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

	public void setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public void setDrawDateTime(List drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawIdTableMap(
			Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}

	public void setGame_no(int game_no) {
		this.game_no = game_no;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setIsQuickPick(Integer[] isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setPanel_id(int[] panel_id) {
		this.panel_id = panel_id;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setPickedNumbers(List<Integer> pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public void setPurchaseChannel(String purchaseChannel) {
		this.purchaseChannel = purchaseChannel;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
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

}
