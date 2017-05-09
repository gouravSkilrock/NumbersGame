package com.skilrock.lms.beans;

public class DashBoardBean {

	private String activityStatus;
	private String balance;
	private String deviceName;// LOGIN DEVICE NAME
	private String errorMsg;
	private String location;
	private String offlineStatus;
	private int orgId;
	private String orgName;
	private String orgStatus;
	private String rgStatus;
	private String webType; // ONLINE OR OFFLINE
	private String xclAmt;
	private String xclDays;
	private double closingBalance;
	private String noOfDays;
	

	
	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getXclAmt() {
		return xclAmt;
	}

	public void setXclAmt(String xclAmt) {
		this.xclAmt = xclAmt;
	}

	public String getXclDays() {
		return xclDays;
	}

	public void setXclDays(String xclDays) {
		this.xclDays = xclDays;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public String getBalance() {
		return balance;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getLocation() {
		return location;
	}

	public String getOfflineStatus() {
		return offlineStatus;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public String getRgStatus() {
		return rgStatus;
	}

	public String getWebType() {
		return webType;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setOfflineStatus(String offlineStatus) {
		this.offlineStatus = offlineStatus;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public void setRgStatus(String rgStatus) {
		this.rgStatus = rgStatus;
	}

	public void setWebType(String webType) {
		this.webType = webType;
	}

}
