package com.skilrock.cs.beans;

import java.math.BigInteger;

public class CamlotSaleBean {
	private CamlotSOAPHeaderBean header;
	private String productId;
	private int categoryId;
	private String PANNumber;
	private String notificationNumber;
	private BigInteger PINNumber;
	private String providerMessage;
	private String providerTransactionRef;
	private String mobileNum; 
	private double amount;
	private BigInteger balance;
	private String expiryDate;
	private String currCode;
	private CamlotFaultBean fault;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public CamlotSOAPHeaderBean getHeader() {
		return header;
	}
	public void setHeader(CamlotSOAPHeaderBean header) {
		this.header = header;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getPANNumber() {
		return PANNumber;
	}
	public void setPANNumber(String pANNumber) {
		PANNumber = pANNumber;
	}
	public String getNotificationNumber() {
		return notificationNumber;
	}
	public void setNotificationNumber(String notificationNumber) {
		this.notificationNumber = notificationNumber;
	}
	public BigInteger getPINNumber() {
		return PINNumber;
	}
	public void setPINNumber(BigInteger pINNumber) {
		PINNumber = pINNumber;
	}
	public String getProviderMessage() {
		return providerMessage;
	}
	public void setProviderMessage(String providerMessage) {
		this.providerMessage = providerMessage;
	}
	public String getProviderTransactionRef() {
		return providerTransactionRef;
	}
	public void setProviderTransactionRef(String providerTransactionRef) {
		this.providerTransactionRef = providerTransactionRef;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public BigInteger getBalance() {
		return balance;
	}
	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCurrCode() {
		return currCode;
	}
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	public CamlotFaultBean getFault() {
		return fault;
	}
	public void setFault(CamlotFaultBean fault) {
		this.fault = fault;
	}
	
	
}
