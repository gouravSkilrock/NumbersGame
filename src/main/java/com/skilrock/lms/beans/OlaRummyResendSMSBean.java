package com.skilrock.lms.beans;

public class OlaRummyResendSMSBean {
private long srNbr;
private String pinNbr;
private String plrName ;
private double amount;
private String pinType;
private String status;
private String date;
private String userPhone;
private int id;
private String retOrgName;
private String refWithdrawalCode;
public String getRetOrgName() {
	return retOrgName;
}
public void setRetOrgName(String retOrgName) {
	this.retOrgName = retOrgName;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getPlrName() {
	return plrName;
}
public void setPlrName(String plrName) {
	this.plrName = plrName;
}
public double getAmount() {
	return amount;
}
public void setAmount(double amount) {
	this.amount = amount;
}
public String getPinType() {
	return pinType;
}
public void setPinType(String pinType) {
	this.pinType = pinType;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getUserPhone() {
	return userPhone;
}
public void setUserPhone(String userPhone) {
	this.userPhone = userPhone;
}
public long getSrNbr() {
	return srNbr;
}
public void setSrNbr(long srNbr) {
	this.srNbr = srNbr;
}
public String getPinNbr() {
	return pinNbr;
}
public void setPinNbr(String pinNbr) {
	this.pinNbr = pinNbr;
}
public String getRefWithdrawalCode() {
	return refWithdrawalCode;
}
public void setRefWithdrawalCode(String refWithdrawalCode) {
	this.refWithdrawalCode = refWithdrawalCode;
}


}
