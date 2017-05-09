package com.skilrock.lms.beans;

import java.sql.Date;

public class OlaExPinBean {
private String serialNumber;
private String pinNumber;
private int amount;
private Date expiryDate;
public String getSerialNumber() {
	return serialNumber;
}
public void setSerialNumber(String serialNumber) {
	this.serialNumber = serialNumber;
}
public String getPinNumber() {
	return pinNumber;
}
public void setPinNumber(String pinNumber) {
	this.pinNumber = pinNumber;
}
public int getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}
public Date getExpiryDate() {
	return expiryDate;
}
public void setExpiryDate(Date expiryDate) {
	this.expiryDate = expiryDate;
}


}
