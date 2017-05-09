package com.skilrock.lms.dge.beans;


import java.util.HashMap;
import java.util.Map;

public class MtnCustomerCenterBean {
	private String ticketNumber;
	Map<String,MerchantTransactioDataBean> transactionMap= new HashMap<String, MerchantTransactioDataBean>(); 
	private double ticketAmount;
	private String txnType;
	private String gameTicketNo;
	private String walletName;
	
	
	public String getWalletName() {
		return walletName;
	}
	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
	public String getGameTicketNo() {
		return gameTicketNo;
	}
	public void setGameTicketNo(String gameTicketNo) {
		this.gameTicketNo = gameTicketNo;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public Map<String, MerchantTransactioDataBean> getTransactionMap() {
		return transactionMap;
	}
	public void setTransactionMap(
			Map<String, MerchantTransactioDataBean> transactionMap) {
		this.transactionMap = transactionMap;
	}
	public double getTicketAmount() {
		return ticketAmount;
	}
	public void setTicketAmount(double ticketAmount) {
		this.ticketAmount = ticketAmount;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	
}
