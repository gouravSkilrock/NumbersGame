package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperResultSubmissionDrawDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String systemUserName;
	private String systemPassword;
	private boolean isSuccess;
	private String errorCode;
	private String userName;
	private String[] performStatus;
	private String gameNo;
	private int drawId;
	private String fromDate;
	private String fromHour;
	private String fromMin;
	private String fromSec;
	private String toDate;
	private String toHour;
	private String toMin;
	private String toSec;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String[] getPerformStatus() {
		return performStatus;
	}
	public void setPerformStatus(String[] performStatus) {
		this.performStatus = performStatus;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getFromHour() {
		return fromHour;
	}
	public void setFromHour(String fromHour) {
		this.fromHour = fromHour;
	}
	public String getFromMin() {
		return fromMin;
	}
	public void setFromMin(String fromMin) {
		this.fromMin = fromMin;
	}
	public String getFromSec() {
		return fromSec;
	}
	public void setFromSec(String fromSec) {
		this.fromSec = fromSec;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getToHour() {
		return toHour;
	}
	public void setToHour(String toHour) {
		this.toHour = toHour;
	}
	public String getToMin() {
		return toMin;
	}
	public void setToMin(String toMin) {
		this.toMin = toMin;
	}
	public String getToSec() {
		return toSec;
	}
	public void setToSec(String toSec) {
		this.toSec = toSec;
	}
	
	
}
