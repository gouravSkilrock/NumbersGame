package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class BetDetailsBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String betType;
	private String betDispName;
	private double unitPrice;
	private int maxBetAmtMultiple;
	private String betStatus;
	private int betCode;
	private int betOrder;

	public BetDetailsBean() {
	}

	public String getBetType() {
		return betType;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	public String getBetDispName() {
		return betDispName;
	}

	public void setBetDispName(String betDispName) {
		this.betDispName = betDispName;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getMaxBetAmtMultiple() {
		return maxBetAmtMultiple;
	}

	public void setMaxBetAmtMultiple(int maxBetAmtMultiple) {
		this.maxBetAmtMultiple = maxBetAmtMultiple;
	}

	public String getBetStatus() {
		return betStatus;
	}

	public void setBetStatus(String betStatus) {
		this.betStatus = betStatus;
	}

	public int getBetCode() {
		return betCode;
	}

	public void setBetCode(int betCode) {
		this.betCode = betCode;
	}

	public int getBetOrder() {
		return betOrder;
	}

	public void setBetOrder(int betOrder) {
		this.betOrder = betOrder;
	}
}