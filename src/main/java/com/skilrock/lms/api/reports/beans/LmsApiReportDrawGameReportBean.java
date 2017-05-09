package com.skilrock.lms.api.reports.beans;

public class LmsApiReportDrawGameReportBean {

	private LmsApiReportConsolidateGameDataBean lmsApiReportConsolidateGameDataBean;
	private String errorCode;
	private boolean isSuccess;
	public LmsApiReportConsolidateGameDataBean getLmsApiReportConsolidateGameDataBean() {
		return lmsApiReportConsolidateGameDataBean;
	}
	public void setLmsApiReportConsolidateGameDataBean(
			LmsApiReportConsolidateGameDataBean lmsApiReportConsolidateGameDataBean) {
		this.lmsApiReportConsolidateGameDataBean = lmsApiReportConsolidateGameDataBean;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
