package com.skilrock.lms.dge.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.Predicate;


public class UssdSubscriberDataBean implements Predicate{
	private String ticketNumber;
	Map<String,UssdSubscriberWinningDataBean> transactionMap= new HashMap<String, UssdSubscriberWinningDataBean>(); 
	private String saleTransDate;
	private String gameName;
	private String drawName;
	private double saleAmt;
	private String saleWallet;
	private String mobileNbr;
	
	
	public String getMobileNbr() {
		return mobileNbr;
	}
	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public Map<String, UssdSubscriberWinningDataBean> getTransactionMap() {
		return transactionMap;
	}
	public void setTransactionMap(
			Map<String, UssdSubscriberWinningDataBean> transactionMap) {
		this.transactionMap = transactionMap;
	}
	public String getSaleTransDate() {
		return saleTransDate;
	}
	public void setSaleTransDate(String saleTransDate) {
		this.saleTransDate = saleTransDate;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public double getSaleAmt() {
		return saleAmt;
	}
	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}
	public String getSaleWallet() {
		return saleWallet;
	}
	public void setSaleWallet(String saleWallet) {
		this.saleWallet = saleWallet;
	}
	public String getDrawName() {
		return drawName;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	
	public boolean evaluate(Object object1) {
		UssdSubscriberDataBean bean = (UssdSubscriberDataBean) object1;
		return ((String.valueOf(bean.getTicketNumber()).equals(String.valueOf(this.ticketNumber))));
	}

}
