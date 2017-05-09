package com.skilrock.lms.beans;

/**
 * It generally a bean class that contain only setter & getter methods.
 * 
 * @author Arun Upadhyay
 * 
 */
public class ReportMailBean {

	private String emailId;
	private String name;
	private int orgId;
	private String ReportFileName;

	public String getEmailId() {
		return emailId;
	}

	public String getName() {
		return name;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getReportFileName() {
		return ReportFileName;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setReportFileName(String reportFileName) {
		ReportFileName = reportFileName;
	}

}
