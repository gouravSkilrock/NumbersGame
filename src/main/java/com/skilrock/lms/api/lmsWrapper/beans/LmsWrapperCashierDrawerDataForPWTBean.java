package com.skilrock.lms.api.lmsWrapper.beans;

public class LmsWrapperCashierDrawerDataForPWTBean {

	private int userId;
	private String ticketNumber;
	private String gameName;
	private String claimedTime;
	private String startDate;
	private String endDate;
	private double claimedAmount;
	private String cashierName;
	private String cashierType;
	private String systemUserName;
	private String systemPassword;
	private boolean isSuccess;
	private String errorCode;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getClaimedTime() {
		return claimedTime;
	}

	public void setClaimedTime(String claimedTime) {

		this.claimedTime = claimedTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public String getCashierType() {
		return cashierType;
	}

	public void setCashierType(String cashierType) {
		this.cashierType = cashierType;
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

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
