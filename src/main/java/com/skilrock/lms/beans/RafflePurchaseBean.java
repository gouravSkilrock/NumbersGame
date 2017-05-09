package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RafflePurchaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int[] betAmountMultiple;
	private String drawDateTime;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int gameId;
	private int game_no;
	private String gameDispName;
	private boolean isPromotkt;
	private int noOfDraws;
	private String parentTktNo;
	private String virnCode;
	private int partyId;
	private String partyType;
	private String purchaseChannel;
	private String purchaseTime;
	private int raffle_no;
	private String raffleTicket_no;
	private String raffleTicketNoBarcode;
	private String raffleTicketType;
	private String refMerchantId;
	private String refTransId;
	private String reprintCount;
	private String saleStatus;
	private double totalPurchaseAmt;
	private int userId;
	private int userMappingId;
	private String plrMobileNumber;
	private int serviceId;
	private String serviceName;
	private String barcodeType;
	private short barcodeCount;
	private int noOfDrawPlayedFor;
	private boolean isAdvancedPlay;
	private double unitPrice;
	private double ticketPrice;
	private Map<String, List<String>> advMsg;

	public RafflePurchaseBean() {
	}

	public int[] getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public void setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}

	public void setDrawIdTableMap(Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
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

	public boolean isPromotkt() {
		return isPromotkt;
	}

	public void setPromotkt(boolean isPromotkt) {
		this.isPromotkt = isPromotkt;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public String getParentTktNo() {
		return parentTktNo;
	}

	public void setParentTktNo(String parentTktNo) {
		this.parentTktNo = parentTktNo;
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

	public int getRaffle_no() {
		return raffle_no;
	}

	public void setRaffle_no(int raffleNo) {
		raffle_no = raffleNo;
	}

	public String getRaffleTicket_no() {
		return raffleTicket_no;
	}

	public void setRaffleTicket_no(String raffleTicketNo) {
		raffleTicket_no = raffleTicketNo;
	}

	public String getRaffleTicketNoBarcode() {
		return raffleTicketNoBarcode;
	}

	public void setRaffleTicketNoBarcode(String raffleTicketNoBarcode) {
		this.raffleTicketNoBarcode = raffleTicketNoBarcode;
	}

	public String getRaffleTicketType() {
		return raffleTicketType;
	}

	public void setRaffleTicketType(String raffleTicketType) {
		this.raffleTicketType = raffleTicketType;
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

	public int getUserMappingId() {
		return userMappingId;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	public short getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(short barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public int getNoOfDrawPlayedFor() {
		return noOfDrawPlayedFor;
	}

	public void setNoOfDrawPlayedFor(int noOfDrawPlayedFor) {
		this.noOfDrawPlayedFor = noOfDrawPlayedFor;
	}

	public boolean isAdvancedPlay() {
		return isAdvancedPlay;
	}

	public void setAdvancedPlay(boolean isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public String getVirnCode() {
		return virnCode;
	}

	public void setVirnCode(String virnCode) {
		this.virnCode = virnCode;
	}
}