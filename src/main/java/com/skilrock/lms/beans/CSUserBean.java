package com.skilrock.lms.beans;

public class CSUserBean {
	private int userId;
	private int userOrgId;
	private String userName;
	private String orgName;
	private String userOrgStatus;
	private double userOrgBalance;
	private double userSaleCommVar;

	private int parentOrgUserId;
	private int parentOrgId;
	private String parentUserName;
	private String parentOrgName;
	private String parentOrgStatus;
	private double parentBalance;
	private double parentSaleCommVar;

	public int getUserId() {
		return userId;
	}

	public int getUserOrgId() {
		return userOrgId;
	}

	public String getUserName() {
		return userName;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getUserOrgStatus() {
		return userOrgStatus;
	}

	public double getUserOrgBalance() {
		return userOrgBalance;
	}

	public int getParentOrgUserId() {
		return parentOrgUserId;
	}

	public int getParentOrgId() {
		return parentOrgId;
	}

	public String getParentUserName() {
		return parentUserName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public String getParentOrgStatus() {
		return parentOrgStatus;
	}

	public double getParentBalance() {
		return parentBalance;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setUserOrgStatus(String userOrgStatus) {
		this.userOrgStatus = userOrgStatus;
	}

	public void setUserOrgBalance(double userOrgBalance) {
		this.userOrgBalance = userOrgBalance;
	}

	public void setParentOrgUserId(int parentOrgUserId) {
		this.parentOrgUserId = parentOrgUserId;
	}

	public void setParentOrgId(int parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public void setParentUserName(String parentUserName) {
		this.parentUserName = parentUserName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public void setParentOrgStatus(String parentOrgStatus) {
		this.parentOrgStatus = parentOrgStatus;
	}

	public void setParentBalance(double parentBalance) {
		this.parentBalance = parentBalance;
	}

	public double getUserSaleCommVar() {
		return userSaleCommVar;
	}

	public void setUserSaleCommVar(double userSaleCommVar) {
		this.userSaleCommVar = userSaleCommVar;
	}

	public double getParentSaleCommVar() {
		return parentSaleCommVar;
	}

	public void setParentSaleCommVar(double parentSaleCommVar) {
		this.parentSaleCommVar = parentSaleCommVar;
	}

}
