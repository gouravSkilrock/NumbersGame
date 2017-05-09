package com.skilrock.ipe.Bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;




public class TicketPurchaseLMSBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int gameId;
	private int gameNo;
	private String gameName;
	private String ticketNo;
	private int partyId;
	private String partyType;
	private int userId;
	private String refMerId;
	
	private Timestamp purchaseTime;
	private int refTransId;
	private String purChannel;
	private double totalAmt;
	private String virnNo;
	private String imgList;
	private String saleStatus;
	private String prizeCode;
	private Map<String, ArrayList<String>> advMsg;
	private boolean isSale;

	public boolean isSale() {
		return isSale;
	}

	public void setSale(boolean isSale) {
		this.isSale = isSale;
	}

	public Map<String, ArrayList<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(Map<String, ArrayList<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public String getPrizeCode() {
		return prizeCode;
	}

	public void setPrizeCode(String prizeCode) {
		this.prizeCode = prizeCode;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRefMerId() {
		return refMerId;
	}

	public void setRefMerId(String refMerId) {
		this.refMerId = refMerId;
	}

	public Timestamp getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Timestamp purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public int getRefTransId() {
		return refTransId;
	}

	public void setRefTransId(int lastTransId) {
		this.refTransId = lastTransId;
	}

	public String getPurChannel() {
		return purChannel;
	}

	public void setPurChannel(String purChannel) {
		this.purChannel = purChannel;
	}

	public double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getVirnNo() {
		return virnNo;
	}

	public void setVirnNo(String virnNo) {
		this.virnNo = virnNo;
	}

	public String getImgList() {
		return imgList;
	}

	public void setImgList(String imgList) {
		this.imgList = imgList;
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}
	
}
