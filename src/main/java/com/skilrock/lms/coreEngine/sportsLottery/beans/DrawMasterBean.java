package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;
import java.util.List;

public class DrawMasterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int drawId;
	private int gameTypeId;
	private int merchantId;
	private int drawNo;
	private String drawName;
	private String drawDisplayType;
	private String drawDateTime;
	private String drawFreezeTime;
	private String saleStartTime;
	private String claimStartDate;
	private String claimEndDate;
	private String verificationDate;
	private String drawStatus;
	private int purchaseTableName;
	private List<EventMasterBean> eventMasterList;

	public DrawMasterBean() {
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(int drawNo) {
		this.drawNo = drawNo;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawDisplayType() {
		return drawDisplayType;
	}

	public void setDrawDisplayType(String drawDisplayType) {
		this.drawDisplayType = drawDisplayType;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}

	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}

	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	public String getClaimStartDate() {
		return claimStartDate;
	}

	public void setClaimStartDate(String claimStartDate) {
		this.claimStartDate = claimStartDate;
	}

	public String getClaimEndDate() {
		return claimEndDate;
	}

	public void setClaimEndDate(String claimEndDate) {
		this.claimEndDate = claimEndDate;
	}

	public String getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(String verificationDate) {
		this.verificationDate = verificationDate;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public int getPurchaseTableName() {
		return purchaseTableName;
	}

	public void setPurchaseTableName(int purchaseTableName) {
		this.purchaseTableName = purchaseTableName;
	}

	public List<EventMasterBean> getEventMasterList() {
		return eventMasterList;
	}

	public void setEventMasterList(List<EventMasterBean> eventMasterList) {
		this.eventMasterList = eventMasterList;
	}

	@Override
	public String toString() {
		return "DrawMasterBean [claimEndDate=" + claimEndDate
				+ ", claimStartDate=" + claimStartDate + ", drawDateTime="
				+ drawDateTime + ", drawDisplayType=" + drawDisplayType
				+ ", drawFreezeTime=" + drawFreezeTime + ", drawId=" + drawId
				+ ", drawName=" + drawName + ", drawNo=" + drawNo
				+ ", drawStatus=" + drawStatus + ", eventMasterList="
				+ eventMasterList + ", gameTypeId=" + gameTypeId
				+ ", merchantId=" + merchantId + ", purchaseTableName="
				+ purchaseTableName + ", saleStartTime=" + saleStartTime
				+ ", verificationDate=" + verificationDate + "]";
	}
}