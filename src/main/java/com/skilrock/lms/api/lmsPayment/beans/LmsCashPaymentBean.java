package com.skilrock.lms.api.lmsPayment.beans;

import java.io.Serializable;

public class LmsCashPaymentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bankName;
	private String branchName;
	private String organizationCode;
	private String refTransId;
	private double amount;
	private String systemUserName;
	private String systemPassword;
	private String cashierName;
	private String regionName;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getSystemUserName() {
		return systemUserName;
	}

	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}

	public String getSystemPassword() {
		return systemPassword;
	}

	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Override
	public String toString() {
		return "LmsCashPaymentBean [bankName=" + bankName + ", branchName="
				+ branchName + ", organizationCode=" + organizationCode
				+ ", refTransId=" + refTransId + ", amount=" + amount
				+ ", systemUserName=" + systemUserName + ", systemPassword="
				+ systemPassword + ", cashierName=" + cashierName
				+ ", regionName=" + regionName + "]";
	}

}
