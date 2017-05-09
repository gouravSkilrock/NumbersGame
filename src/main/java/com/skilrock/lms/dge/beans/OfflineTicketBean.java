package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class OfflineTicketBean implements Serializable {

	private static final long serialVersionUID = 1L;
	String ticketNo;
	String purchaseTime;
	String gameData;
	double tktCost;
	int isAdvancePlay;
	List<String> drawIdList;
	String promoCheck;
	int saleTrxId;
	OfflineTicketBean promoBean = null;

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public String getGameData() {
		return gameData;
	}

	public void setGameData(String gameData) {
		this.gameData = gameData;
	}

	public double getTktCost() {
		return tktCost;
	}

	public void setTktCost(double tktCost) {
		this.tktCost = tktCost;
	}

	public int getIsAdvancePlay() {
		return isAdvancePlay;
	}

	public void setIsAdvancePlay(int isAdvancePlay) {
		this.isAdvancePlay = isAdvancePlay;
	}

	public List<String> getDrawIdList() {
		return drawIdList;
	}

	public void setDrawIdList(List<String> drawIdList) {
		this.drawIdList = drawIdList;
	}

	public String getPromoCheck() {
		return promoCheck;
	}

	public void setPromoCheck(String promoCheck) {
		this.promoCheck = promoCheck;
	}

	public OfflineTicketBean getPromoBean() {
		return promoBean;
	}

	public void setPromoBean(OfflineTicketBean promoBean) {
		this.promoBean = promoBean;
	}

	public int getSaleTrxId() {
		return saleTrxId;
	}

	public void setSaleTrxId(int saleTrxId) {
		this.saleTrxId = saleTrxId;
	}
}
