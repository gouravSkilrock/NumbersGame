package com.skilrock.lms.beans;

import java.sql.Date;

public class OlaPinStatusBean {
private Date generationDate;
private String distributor;
private int amount;
private int pinGenerated ;
private int pinRedeemed ;
private String redeemedAt;
private double saleCommRate;
private Date expiryDate;
public Date getGenerationDate() {
	return generationDate;
}
public void setGenerationDate(Date generationDate) {
	this.generationDate = generationDate;
}
public String getDistributor() {
	return distributor;
}
public void setDistributor(String distributor) {
	this.distributor = distributor;
}
public int getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}
public int getPinGenerated() {
	return pinGenerated;
}
public void setPinGenerated(int pinGenerated) {
	this.pinGenerated = pinGenerated;
}
public int getPinRedeemed() {
	return pinRedeemed;
}
public void setPinRedeemed(int pinRedeemed) {
	this.pinRedeemed = pinRedeemed;
}
public String getRedeemedAt() {
	return redeemedAt;
}
public void setRedeemedAt(String redeemedAt) {
	this.redeemedAt = redeemedAt;
}
public double getSaleCommRate() {
	return saleCommRate;
}
public void setSaleCommRate(double saleCommRate) {
	this.saleCommRate = saleCommRate;
}
public Date getExpiryDate() {
	return expiryDate;
}
public void setExpiryDate(Date expiryDate) {
	this.expiryDate = expiryDate;
}
}
