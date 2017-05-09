package com.skilrock.lms.beans;

import java.io.Serializable;

public class TmpPwtReceiptBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agtOrgName;
	private String receiptId;
	private String recievedDate;
	private String status;
	private String tickReceived;
	private String verifiedTickNum;
	private String verifiedVIRN;

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public String getRecievedDate() {
		return recievedDate;
	}

	public String getStatus() {
		return status;
	}

	public String getTickReceived() {
		return tickReceived;
	}

	public String getVerifiedTickNum() {
		return verifiedTickNum;
	}

	public String getVerifiedVIRN() {
		return verifiedVIRN;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public void setRecievedDate(String recievedDate) {
		this.recievedDate = recievedDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTickReceived(String tickReceived) {
		this.tickReceived = tickReceived;
	}

	public void setVerifiedTickNum(String verifiedTickNum) {
		this.verifiedTickNum = verifiedTickNum;
	}

	public void setVerifiedVIRN(String verifiedVIRN) {
		this.verifiedVIRN = verifiedVIRN;
	}

}