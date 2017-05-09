package com.skilrock.lms.beans;

public class OlaExPinSummaryBean {
private int totalPin;
private int amount ;
private String expiryDate;
private boolean success;

public int getTotalPin() {
	return totalPin;
}
public void setTotalPin(int totalPin) {
	this.totalPin = totalPin;
}
public int getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}
public String getExpiryDate() {
	return expiryDate;
}
public void setExpiryDate(String expiryDate) {
	this.expiryDate = expiryDate;
}
public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}

}
