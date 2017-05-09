package com.skilrock.lms.beans;

public class IWUserIncentiveBean {

	private int organizationId;
	private String organizationName;
	private int userId;
	private String userName;
	private double winning;
	private double sale;
	private double incentiveAmount;
	private int parentId;
	private String parentOrgName;

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getWinning() {
		return winning;
	}

	public void setWinning(double winning) {
		this.winning = winning;
	}

	public double getSale() {
		return sale;
	}

	public void setSale(double sale) {
		this.sale = sale;
	}

	public double getIncentiveAmount() {
		return incentiveAmount;
	}

	public void setIncentiveAmount(double incentiveAmount) {
		this.incentiveAmount = incentiveAmount;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

}
