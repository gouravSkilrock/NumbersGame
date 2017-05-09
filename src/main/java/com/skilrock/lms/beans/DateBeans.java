package com.skilrock.lms.beans;

import java.sql.Date;
import java.sql.Timestamp;

public class DateBeans {

	private java.util.Date endDate;
	private Timestamp endTime;
	private Date firstdate;
	private Date lastdate;
	private java.util.Date reportday;
	private String reportType;
	private java.util.Date startDate;
	private Timestamp startTime;
	private String strDateString;
	private String endDateString; 

	/**
	 * @return the endDate
	 */
	public java.util.Date getEndDate() {
		return endDate;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	/**
	 * @return the firstdate
	 */
	public Date getFirstdate() {
		return firstdate;
	}

	/**
	 * @return the lastdate
	 */
	public Date getLastdate() {
		return lastdate;
	}

	/**
	 * @return the reportday
	 */
	public java.util.Date getReportday() {
		return reportday;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @return the startDate
	 */
	public java.util.Date getStartDate() {
		return startDate;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	/**
	 * @param firstdate
	 *            the firstdate to set
	 */
	public void setFirstdate(Date firstdate) {
		this.firstdate = firstdate;
	}

	/**
	 * @param lastdate
	 *            the lastdate to set
	 */
	public void setLastdate(Date lastdate) {
		this.lastdate = lastdate;
	}

	/**
	 * @param reportday
	 *            the reportday to set
	 */
	public void setReportday(java.util.Date reportday) {
		this.reportday = reportday;
	}

	/**
	 * @param reportType
	 *            the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public String getStrDateString() {
		return strDateString;
	}

	public void setStrDateString(String strDateString) {
		this.strDateString = strDateString;
	}

	public String getEndDateString() {
		return endDateString;
	}

	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

}
