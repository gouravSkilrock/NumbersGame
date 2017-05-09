package com.skilrock.lms.beans;

import java.io.Serializable;

public class ICSQueryStatusReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int queryId;
	private String queryTitle;
	private String queryDescription;
	private String expectedResult;
	private String actualResult;
	private String icsRunDate;
	private String relatedTo;
	private String tierType;
	private String errorMessage;
	private String lastSuccessDate;
	private String isCritical;
	private String status;
	private String runBy;

	public ICSQueryStatusReportBean() {
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public String getQueryTitle() {
		return queryTitle;
	}

	public void setQueryTitle(String queryTitle) {
		this.queryTitle = queryTitle;
	}

	public String getQueryDescription() {
		return queryDescription;
	}

	public void setQueryDescription(String queryDescription) {
		this.queryDescription = queryDescription;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getActualResult() {
		return actualResult;
	}

	public void setActualResult(String actualResult) {
		this.actualResult = actualResult;
	}

	public String getIcsRunDate() {
		return icsRunDate;
	}

	public void setIcsRunDate(String icsRunDate) {
		this.icsRunDate = icsRunDate;
	}

	public String getRelatedTo() {
		return relatedTo;
	}

	public void setRelatedTo(String relatedTo) {
		this.relatedTo = relatedTo;
	}

	public String getTierType() {
		return tierType;
	}

	public void setTierType(String tierType) {
		this.tierType = tierType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(String isCritical) {
		this.isCritical = isCritical;
	}

	public String getLastSuccessDate() {
		return lastSuccessDate;
	}

	public void setLastSuccessDate(String lastSuccessDate) {
		this.lastSuccessDate = lastSuccessDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRunBy() {
		return runBy;
	}

	public void setRunBy(String runBy) {
		this.runBy = runBy;
	}
}