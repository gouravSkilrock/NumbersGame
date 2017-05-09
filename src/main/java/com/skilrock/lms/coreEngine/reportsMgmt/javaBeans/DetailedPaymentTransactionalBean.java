package com.skilrock.lms.coreEngine.reportsMgmt.javaBeans;

public class DetailedPaymentTransactionalBean {
	private int totalTransactions;
	private double totalSaleAmt;
	private double claimedPwtAmt;
	private int eventId;
	private String drawName;
	private String userId;
	private String partyId;
	private String saleDateTime;
	private double saleAmt;
	private double winAmt;
	private String refTxnId;
	private String userType;
	private String retOrgCode;
	private String agtOrgCode;
	private String winTransDate;
	private String ticketNbr;

	public String getTicketNbr() {
		return ticketNbr;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public String getRetOrgCode() {
		return retOrgCode;
	}

	public void setRetOrgCode(String retOrgCode) {
		this.retOrgCode = retOrgCode;
	}

	public String getAgtOrgCode() {
		return agtOrgCode;
	}

	public void setAgtOrgCode(String agtOrgCode) {
		this.agtOrgCode = agtOrgCode;
	}

	public String getWinTransDate() {
		return winTransDate;
	}

	public void setWinTransDate(String winTransDate) {
		this.winTransDate = winTransDate;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getSaleDateTime() {
		return saleDateTime;
	}

	public void setSaleDateTime(String saleDateTime) {
		this.saleDateTime = saleDateTime;
	}

	public double getSaleAmt() {
		return saleAmt;
	}

	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}

	public double getWinAmt() {
		return winAmt;
	}

	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}

	public String getRefTxnId() {
		return refTxnId;
	}

	public void setRefTxnId(String refTxnId) {
		this.refTxnId = refTxnId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public DetailedPaymentTransactionalBean() {
	}

	public int getTotalTransactions() {
		return totalTransactions;
	}

	public void setTotalTransactions(int totalTransactions) {
		this.totalTransactions = totalTransactions;
	}

	public double getTotalSaleAmt() {
		return totalSaleAmt;
	}

	public void setTotalSaleAmt(double totalSaleAmt) {
		this.totalSaleAmt = totalSaleAmt;
	}

	public double getClaimedPwtAmt() {
		return claimedPwtAmt;
	}

	public void setClaimedPwtAmt(double claimedPwtAmt) {
		this.claimedPwtAmt = claimedPwtAmt;
	}

	@Override
	public String toString() {
		return "DetailedPaymentTransactionalBean [agtOrgCode=" + agtOrgCode
				+ ", claimedPwtAmt=" + claimedPwtAmt + ", drawName=" + drawName
				+ ", eventId=" + eventId + ", partyId=" + partyId
				+ ", refTxnId=" + refTxnId + ", retOrgCode=" + retOrgCode
				+ ", saleAmt=" + saleAmt + ", saleDateTime=" + saleDateTime
				+ ", ticketNbr=" + ticketNbr + ", totalSaleAmt=" + totalSaleAmt
				+ ", totalTransactions=" + totalTransactions + ", userId="
				+ userId + ", userType=" + userType + ", winAmt=" + winAmt
				+ ", winTransDate=" + winTransDate + "]";
	}

}