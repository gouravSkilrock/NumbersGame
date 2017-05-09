package com.skilrock.lms.beans;

public class ScratchBookDataBean {
	private String bookNbr;
	private String packNbr;
	private String bookStatus;
	private String ownerName;
	private String ownerType;
	private int currentRemainingTkts;
	private String bookActivationDate;
	private String bookReceiveDate;
	private String warehouseName;
	private int soldTkts;
	private int totalTkts;
	private String gameNbr;
	private String gameName;
	private String ticketPrice;
	private String bookPrice;
	private String retailerInvoiceId;
	private String retailerInvoiceDate;
	private String retailerInvoiceMethod;
	private int totalClaimedTkts;
	private int orgId;

	public String getBookNbr() {
		return bookNbr;
	}

	public void setBookNbr(String bookNbr) {
		this.bookNbr = bookNbr;
	}

	public String getPackNbr() {
		return packNbr;
	}

	public void setPackNbr(String packNbr) {
		this.packNbr = packNbr;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public int getCurrentRemainingTkts() {
		return currentRemainingTkts;
	}

	public void setCurrentRemainingTkts(int currentRemainingTkts) {
		this.currentRemainingTkts = currentRemainingTkts;
	}

	public String getBookActivationDate() {
		return bookActivationDate;
	}

	public void setBookActivationDate(String bookActivationDate) {
		this.bookActivationDate = bookActivationDate;
	}

	public String getBookReceiveDate() {
		return bookReceiveDate;
	}

	public void setBookReceiveDate(String bookReceiveDate) {
		this.bookReceiveDate = bookReceiveDate;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getSoldTkts() {
		return soldTkts;
	}

	public void setSoldTkts(int soldTkts) {
		this.soldTkts = soldTkts;
	}

	public int getTotalTkts() {
		return totalTkts;
	}

	public void setTotalTkts(int totalTkts) {
		this.totalTkts = totalTkts;
	}

	public String getGameNbr() {
		return gameNbr;
	}

	public void setGameNbr(String gameNbr) {
		this.gameNbr = gameNbr;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(String bookPrice) {
		this.bookPrice = bookPrice;
	}

	public String getRetailerInvoiceId() {
		return retailerInvoiceId;
	}

	public void setRetailerInvoiceId(String retailerInvoiceId) {
		this.retailerInvoiceId = retailerInvoiceId;
	}

	public String getRetailerInvoiceDate() {
		return retailerInvoiceDate;
	}

	public void setRetailerInvoiceDate(String retailerInvoiceDate) {
		this.retailerInvoiceDate = retailerInvoiceDate;
	}

	public String getRetailerInvoiceMethod() {
		return retailerInvoiceMethod;
	}

	public void setRetailerInvoiceMethod(String retailerInvoiceMethod) {
		this.retailerInvoiceMethod = retailerInvoiceMethod;
	}

	public int getTotalClaimedTkts() {
		return totalClaimedTkts;
	}

	public void setTotalClaimedTkts(int totalClaimedTkts) {
		this.totalClaimedTkts = totalClaimedTkts;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "ScratchBookDataBean [bookNbr=" + bookNbr + ", packNbr="
				+ packNbr + ", bookStatus=" + bookStatus + ", ownerName="
				+ ownerName + ", ownerType=" + ownerType
				+ ", currentRemainingTkts=" + currentRemainingTkts
				+ ", bookActivationDate=" + bookActivationDate
				+ ", bookReceiveDate=" + bookReceiveDate + ", warehouseName="
				+ warehouseName + ", soldTkts=" + soldTkts + ", totalTkts="
				+ totalTkts + ", gameNbr=" + gameNbr + ", gameName=" + gameName
				+ ", ticketPrice=" + ticketPrice + ", bookPrice=" + bookPrice
				+ ", retailerInvoiceId=" + retailerInvoiceId
				+ ", retailerInvoiceDate=" + retailerInvoiceDate
				+ ", retailerInvoiceMethod=" + retailerInvoiceMethod
				+ ", totalClaimedTkts=" + totalClaimedTkts + ", orgId=" + orgId
				+ "]";
	}

}
