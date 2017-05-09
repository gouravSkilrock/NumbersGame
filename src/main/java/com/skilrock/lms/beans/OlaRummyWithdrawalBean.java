package com.skilrock.lms.beans;

import java.sql.Date;

public class OlaRummyWithdrawalBean {
private int taskId;
private String accountId ;	
private String name ;
private String address;
private double amount;
private String bankAcNumber;
private String bankName ;
private String transferMode ;
private String status ;
private String returnType ;
private Date requestDate;

public Date getRequestDate() {
	return requestDate;
}
public void setRequestDate(Date requestDate) {
	this.requestDate = requestDate;
}
public String getAccountId() {
	return accountId;
}
public void setAccountId(String accountId) {
	this.accountId = accountId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public double getAmount() {
	return amount;
}
public void setAmount(double amount) {
	this.amount = amount;
}
public String getBankAcNumber() {
	return bankAcNumber;
}
public void setBankAcNumber(String bankAcNumber) {
	this.bankAcNumber = bankAcNumber;
}
public String getBankName() {
	return bankName;
}
public void setBankName(String bankName) {
	this.bankName = bankName;
}
public String getTransferMode() {
	return transferMode;
}
public void setTransferMode(String transferMode) {
	this.transferMode = transferMode;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getReturnType() {
	return returnType;
}
public void setReturnType(String returnType) {
	this.returnType = returnType;
}
public int getTaskId() {
	return taskId;
}
public void setTaskId(int taskId) {
	this.taskId = taskId;
}


	

}
