package com.skilrock.lms.web.bankMgmt.beans;

public class BranchDetailsBean {

	private String branchName ;
	private String branchFullName;
	private String branchAddLine1;
	private String branchAddLine2;
	private String country;
	private String state;
	private String city;
	private int bankId;
	private String bankName ;
	private int branchId;
	public String getBranchName() {
		return branchName;
	}
	public String getBranchFullName() {
		return branchFullName;
	}
	public String getBranchAddLine1() {
		return branchAddLine1;
	}
	public String getBranchAddLine2() {
		return branchAddLine2;
	}
	public String getCountry() {
		return country;
	}
	public String getState() {
		return state;
	}
	public String getCity() {
		return city;
	}
	public int getBankId() {
		return bankId;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public void setBranchFullName(String branchFullName) {
		this.branchFullName = branchFullName;
	}
	public void setBranchAddLine1(String branchAddLine1) {
		this.branchAddLine1 = branchAddLine1;
	}
	public void setBranchAddLine2(String branchAddLine2) {
		this.branchAddLine2 = branchAddLine2;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setCity(String city) {
		this.city = city;
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
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	
}
