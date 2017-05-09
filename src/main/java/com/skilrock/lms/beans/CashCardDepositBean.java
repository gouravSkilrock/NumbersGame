package com.skilrock.lms.beans;

import java.util.ArrayList;

public class CashCardDepositBean {
	
private ArrayList<CashCardPurchaseDataBean> cashCardList = new ArrayList<CashCardPurchaseDataBean> ();
private long transactionId ; 
private double totalAmount ;
private String  partyId;
private boolean isSuccess;
private String returnType;

public ArrayList<CashCardPurchaseDataBean> getCashCardList() {
	return cashCardList;
}
public void setCashCardList(ArrayList<CashCardPurchaseDataBean> cashCardList) {
	this.cashCardList = cashCardList;
}
public long getTransactionId() {
	return transactionId;
}
public void setTransactionId(long transactionId) {
	this.transactionId = transactionId;
}
public double getTotalAmount() {
	return totalAmount;
}
public void setTotalAmount(double totalAmount) {
	this.totalAmount = totalAmount;
}
public boolean isSuccess() {
	return isSuccess;
}
public void setSuccess(boolean isSuccess) {
	this.isSuccess = isSuccess;
}
public String getReturnType() {
	return returnType;
}
public void setReturnType(String returnType) {
	this.returnType = returnType;
}
public String getPartyId() {
	return partyId;
}
public void setPartyId(String partyId) {
	this.partyId = partyId;
}


}
