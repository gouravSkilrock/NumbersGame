package com.skilrock.lms.beans;

public class CashChqPmntBean {

	private String date;
	private double paymentAmount;
	private String paymentType;
	private String voucherNo;
	private String bankName;
	private String orgName;
	private String stateName ;
	private String cityName;
	public String getDate() {
		return date;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setVouncherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	

	
}
