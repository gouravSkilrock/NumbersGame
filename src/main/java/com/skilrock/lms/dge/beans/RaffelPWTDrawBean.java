package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class RaffelPWTDrawBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<DrawIdBean> drawWinList;
	private String gameDispName;
	private int gameId;
	private int gameNo;
	private boolean isHighPrize;
	private boolean isReprint = false;
	private boolean isValid = true;
	private boolean isWinTkt;
	private int partyId;
	private String partyType;
	private Object purchaseBean;
	private String pwtStatus;
	private String refMerchantId;
	private String reprintCount;
	private String status;
	private String ticketNo;
	private double totalAmount;
	private int userId;

	public List<DrawIdBean> getDrawWinList() {
		return drawWinList;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public int getGameId() {
		return gameId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public Object getPurchaseBean() {
		return purchaseBean;
	}

	public String getPwtStatus() {
		return pwtStatus;
	}

	public String getRefMerchantId() {
		return refMerchantId;
	}

	public String getReprintCount() {
		return reprintCount;
	}

	public String getStatus() {
		return status;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public int getUserId() {
		return userId;
	}

	public boolean isHighPrize() {
		return isHighPrize;
	}

	public boolean isReprint() {
		return isReprint;
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isWinTkt() {
		return isWinTkt;
	}

	public void setDrawWinList(List<DrawIdBean> drawWinList) {
		this.drawWinList = drawWinList;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setHighPrize(boolean isHighPrize) {
		this.isHighPrize = isHighPrize;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setPurchaseBean(Object purchaseBean) {
		this.purchaseBean = purchaseBean;
	}

	public void setPwtStatus(String pwtStatus) {
		this.pwtStatus = pwtStatus;
	}

	public void setRefMerchantId(String refMerchantId) {
		this.refMerchantId = refMerchantId;
	}

	public void setReprint(boolean isReprint) {
		this.isReprint = isReprint;
	}

	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setWinTkt(boolean isWinTkt) {
		this.isWinTkt = isWinTkt;
	}

}