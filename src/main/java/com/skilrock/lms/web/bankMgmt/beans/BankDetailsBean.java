package com.skilrock.lms.web.bankMgmt.beans;

public class BankDetailsBean {
	private int bankId;
	private String bankName ;
	private String bankFullName;
	private String bankAddLine1;
	private String bankAddLine2;
	private String accountNo;
	private String description;
	private String country;
	private String state;
	private String city;
	private int roleId;

	public BankDetailsBean() {
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	public String getBankAddLine1() {
		return bankAddLine1;
	}

	public void setBankAddLine1(String bankAddLine1) {
		this.bankAddLine1 = bankAddLine1;
	}

	public String getBankAddLine2() {
		return bankAddLine2;
	}

	public void setBankAddLine2(String bankAddLine2) {
		this.bankAddLine2 = bankAddLine2;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}