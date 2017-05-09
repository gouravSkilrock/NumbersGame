package com.skilrock.lms.beans;

import java.io.Serializable;
import java.sql.Time;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ReportStatusBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int reportId;
	private String reportGroupName;
	private String reportDisplayName;
	private String reportingFrom;
	private Time startTime;
	private Time endTime;
	private String serviceName;
	private String interfaceType;
	private String reportStatus;

	public ReportStatusBean() {
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getReportGroupName() {
		return reportGroupName;
	}

	public void setReportGroupName(String reportGroupName) {
		this.reportGroupName = reportGroupName;
	}

	public String getReportDisplayName() {
		return reportDisplayName;
	}

	public void setReportDisplayName(String reportDisplayName) {
		this.reportDisplayName = reportDisplayName;
	}

	public String getReportingFrom() {
		return reportingFrom;
	}

	public void setReportingFrom(String reportingFrom) {
		this.reportingFrom = reportingFrom;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("Report ID", this.reportId)
		.append("Report Group Name", this.reportGroupName)
		.append("Report Display Name", this.reportDisplayName)
		.append("Reporting From", this.reportingFrom)
		.append("Start Time", this.startTime)
		.append("End Time", this.endTime)
		.append("Service Name", this.serviceName)
		.append("Interface Type", this.interfaceType)
		.append("Report Status", this.reportStatus).toString();
	}
}