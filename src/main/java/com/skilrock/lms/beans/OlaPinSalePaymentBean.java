package com.skilrock.lms.beans;

import java.sql.Date;



public class OlaPinSalePaymentBean {
private int taskId;	
private int walletId;
private String distributor;
private Date startDate;
private Date endDate;
private Double totalAmount;
private Double netAmount;
private String walletName;
private Double commRate;
public int getTaskId() {
	return taskId;
}
public void setTaskId(int taskId) {
	this.taskId = taskId;
}
public Date getStartDate() {
	return startDate;
}
public void setStartDate(Date startDate) {
	this.startDate = startDate;
}
public Date getEndDate() {
	return endDate;
}
public void setEndDate(Date endDate) {
	this.endDate = endDate;
}
public Double getTotalAmount() {
	return totalAmount;
}
public void setTotalAmount(Double totalAmount) {
	this.totalAmount = totalAmount;
}
public Double getNetAmount() {
	return netAmount;
}
public void setNetAmount(Double netAmount) {
	this.netAmount = netAmount;
}
public String getWalletName() {
	return walletName;
}
public void setWalletName(String walletName) {
	this.walletName = walletName;
}
public int getWalletId() {
	return walletId;
}
public void setWalletId(int walletId) {
	this.walletId = walletId;
}
public String getDistributor() {
	return distributor;
}
public void setDistributor(String distributor) {
	this.distributor = distributor;
}
public Double getCommRate() {
	return commRate;
}
public void setCommRate(Double commRate) {
	this.commRate = commRate;
}


}
