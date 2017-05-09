package com.skilrock.lms.beans;

import java.io.Serializable;

public class TallyXmlVariablesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String xmlFileType;
	private String gameName;
	private String startDate;
	private String endDate;
	private String voucherType;
	private String isDeemedForLedger;
	private String isPartyForLedger;
	private String isDeemedForTotal;
	private String isPartyForTotal;
	private String locationOfTotal;
	private String isLedgerAmountPositive;

	public String getXmlFileType() {
		return xmlFileType;
	}

	public void setXmlFileType(String xmlFileType) {
		this.xmlFileType = xmlFileType;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}

	public String getIsDeemedForLedger() {
		return isDeemedForLedger;
	}

	public void setIsDeemedForLedger(String isDeemedForLedger) {
		this.isDeemedForLedger = isDeemedForLedger;
	}

	public String getIsPartyForLedger() {
		return isPartyForLedger;
	}

	public void setIsPartyForLedger(String isPartyForLedger) {
		this.isPartyForLedger = isPartyForLedger;
	}

	public String getIsDeemedForTotal() {
		return isDeemedForTotal;
	}

	public void setIsDeemedForTotal(String isDeemedForTotal) {
		this.isDeemedForTotal = isDeemedForTotal;
	}

	public String getIsPartyForTotal() {
		return isPartyForTotal;
	}

	public void setIsPartyForTotal(String isPartyForTotal) {
		this.isPartyForTotal = isPartyForTotal;
	}

	public String getLocationOfTotal() {
		return locationOfTotal;
	}

	public void setLocationOfTotal(String locationOfTotal) {
		this.locationOfTotal = locationOfTotal;
	}

	public String getIsLedgerAmountPositive() {
		return isLedgerAmountPositive;
	}

	public void setIsLedgerAmountPositive(String isLedgerAmountPositive) {
		this.isLedgerAmountPositive = isLedgerAmountPositive;
	}

	public void setSaleBean() {
		this.voucherType = "Journal";
		this.isDeemedForLedger = "Yes";
		this.isDeemedForTotal = "No";
		this.isLedgerAmountPositive = "No";
		this.isPartyForLedger = "Yes";
		this.isPartyForTotal = "No";
		this.locationOfTotal = "Bottom";
		this.xmlFileType = "Sale";
	}

	public void setSaleConsolidatedBean() {
		this.voucherType = "Journal";
		this.isDeemedForLedger = "Yes";
		this.isDeemedForTotal = "No";
		this.isLedgerAmountPositive = "No";
		this.isPartyForLedger = "Yes";
		this.isPartyForTotal = "No";
		this.locationOfTotal = "Bottom";
		this.xmlFileType = "SaleConsolidated";
	}

	public void setPwtBean() {
		this.voucherType = "Journal";
		this.isDeemedForLedger = "No";
		this.isDeemedForTotal = "Yes";
		this.isLedgerAmountPositive = "Yes";
		this.isPartyForLedger = "Yes";
		this.isPartyForTotal = "No";
		this.locationOfTotal = "Top";
		this.xmlFileType = "Pwt";
	}

	public void setBankBean() {
		this.voucherType = "Receipt";
		this.isDeemedForLedger = "No";
		this.isDeemedForTotal = "Yes";
		this.isLedgerAmountPositive = "Yes";
		this.isPartyForLedger = "Yes";
		this.isPartyForTotal = "Yes";
		this.locationOfTotal = "Bottom";
		this.xmlFileType = "Bank";
	}

	public void setCashBean() {
		this.voucherType = "Journal";
		this.isDeemedForLedger = "No";
		this.isDeemedForTotal = "Yes";
		this.isLedgerAmountPositive = "Yes";
		this.isPartyForLedger = "Yes";
		this.isPartyForTotal = "No";
		this.locationOfTotal = "Top";
		this.xmlFileType = "Cash";
	}

	public void setTrainingBean() {
		this.voucherType = "Journal";
		this.isDeemedForLedger = "No";
		this.isDeemedForTotal = "Yes";
		this.isLedgerAmountPositive = "Yes";
		this.isPartyForLedger = "Yes";
		this.isPartyForTotal = "No";
		this.locationOfTotal = "Top";
		this.xmlFileType = "Training";
	}

	@Override
	public String toString() {
		return "TallyXmlVariablesBean [xmlFileType=" + xmlFileType
				+ ", gameName=" + gameName + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", voucherType=" + voucherType
				+ ", isDeemedForLedger=" + isDeemedForLedger
				+ ", isPartyForLedger=" + isPartyForLedger
				+ ", isDeemedForTotal=" + isDeemedForTotal
				+ ", isPartyForTotal=" + isPartyForTotal + ", locationOfTotal="
				+ locationOfTotal + ", isLedgerAmountPositive="
				+ isLedgerAmountPositive + "]";
	}

}
