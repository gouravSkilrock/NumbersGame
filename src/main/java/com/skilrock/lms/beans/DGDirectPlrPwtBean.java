package com.skilrock.lms.beans;

import java.sql.Date;

public class DGDirectPlrPwtBean extends DrawPWTBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date chqDate;
	private String chqNbr;
	private String draweeBank;
	private String issuingPartyName;
	private double netAmt;
	private String paymentType;
	private int playerId;
	private double taxAmt;

	public Date getChqDate() {
		return chqDate;
	}

	public String getChqNbr() {
		return chqNbr;
	}

	public String getDraweeBank() {
		return draweeBank;
	}

	public String getIssuingPartyName() {
		return issuingPartyName;
	}

	public double getNetAmt() {
		return netAmt;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public int getPlayerId() {
		return playerId;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public void setChqDate(Date chqDate) {
		this.chqDate = chqDate;
	}

	public void setChqNbr(String chqNbr) {
		this.chqNbr = chqNbr;
	}

	public void setDraweeBank(String draweeBank) {
		this.draweeBank = draweeBank;
	}

	public void setIssuingPartyName(String issuingPartyName) {
		this.issuingPartyName = issuingPartyName;
	}

	public void setNetAmt(double netAmt) {
		this.netAmt = netAmt;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}

}
