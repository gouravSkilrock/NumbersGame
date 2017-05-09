package com.skilrock.lms.api.reports.beans;

public class LmsApiReportRequestDataBean {
	
	private String gameCode;
	private String systemUserName;
	private String systemUserPassword;
	private String startDate;
	private String endDate;
	private boolean isDateWise;
	private String noOfDaysForCurrentDate;
	
	
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getSystemUserName() {
		return systemUserName;
	}
	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}
	public String getSystemUserPassword() {
		return systemUserPassword;
	}
	public void setSystemUserPassword(String systemUserPassword) {
		this.systemUserPassword = systemUserPassword;
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
	public boolean getIsDateWise() {
		return isDateWise;
	}
	public void setIsDateWise(boolean isDateWise) {
		this.isDateWise = isDateWise;
	}
	public String getNoOfDaysForCurrentDate() {
		return noOfDaysForCurrentDate;
	}
	public void setNoOfDaysForCurrentDate(String noOfDaysForCurrentDate) {
		this.noOfDaysForCurrentDate = noOfDaysForCurrentDate;
	}
	
	
	
}
