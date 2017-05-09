package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FortunePurchaseBean extends CommonPurchaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Map<String, List<String>> advMsg;
	//private int[] betAmountMultiple;
	//private List drawDateTime;
	//Map<Integer, Map<Integer, String>> drawIdTableMap;
	//private int game_no;
	private int gameId;
	//private String gameDispName;
	//private int isAdvancedPlay;
	//private boolean isPromotkt;
	private int isQP;
	private Integer[] isQuickPick;
	//private boolean isRaffelAssociated;
	//private int noOfDraws;
	private String noOfPanels;
	private int[] panel_id;
	//private int partyId;
	//private String partyType;
	private List<Integer> pickedNumbers;
	//private Object PromoPurchaseBean;
	//private String purchaseChannel;
	//private String purchaseTime;
	private String barcodeType;
	//Map<Integer, Map<Integer, String>> raffleDrawIdTableMap;

	//private int raffleNo;
	//private List<RafflePurchaseBean> rafflePurchaseBeanList;
	//private String refMerchantId;
	//private String refTransId;

	//private String reprintCount;
	//private String saleStatus;

	private String symbols;

	//private String ticket_no;
	private int totalNoOfPanels;
	//private double totalPurchaseAmt;
	private double unitPrice;		
	//private int userId;
	private String LastSoldTicketNo;
	private String plrMobileNumber;
	private short barcodeCount;
	private String playType;
	private String claimEndTime;
	
	public String getClaimEndTime() {
		return claimEndTime;
	}
	public void setClaimEndTime(String claimEndTime) {
		this.claimEndTime = claimEndTime;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getIsQP() {
		return isQP;
	}
	public void setIsQP(int isQP) {
		this.isQP = isQP;
	}
	public Integer[] getIsQuickPick() {
		return isQuickPick;
	}
	public void setIsQuickPick(Integer[] isQuickPick) {
		this.isQuickPick = isQuickPick;
	}
	public String getNoOfPanels() {
		return noOfPanels;
	}
	public void setNoOfPanels(String noOfPanels) {
		this.noOfPanels = noOfPanels;
	}
	public int[] getPanel_id() {
		return panel_id;
	}
	public void setPanel_id(int[] panelId) {
		panel_id = panelId;
	}
	public List<Integer> getPickedNumbers() {
		return pickedNumbers;
	}
	public void setPickedNumbers(List<Integer> pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}
	public String getBarcodeType() {
		return barcodeType;
	}
	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}
	public String getSymbols() {
		return symbols;
	}
	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}
	public int getTotalNoOfPanels() {
		return totalNoOfPanels;
	}
	public void setTotalNoOfPanels(int totalNoOfPanels) {
		this.totalNoOfPanels = totalNoOfPanels;
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
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	

}