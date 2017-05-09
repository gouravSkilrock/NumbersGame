package com.skilrock.lms.beans;

import java.util.Date;

public class PWSaleRespBean {
	private String cardSerialNum;
	private String cardPinNum;
	private Date expiryDate;
	public String getCardSerialNum() {
		return cardSerialNum;
	}
	public void setCardSerialNum(String cardSerialNum) {
		this.cardSerialNum = cardSerialNum;
	}
	public String getCardPinNum() {
		return cardPinNum;
	}
	public void setCardPinNum(String cardPinNum) {
		this.cardPinNum = cardPinNum;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
