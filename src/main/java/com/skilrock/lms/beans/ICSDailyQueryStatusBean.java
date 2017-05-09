package com.skilrock.lms.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class ICSDailyQueryStatusBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int queryId;
	private String queryTitle;
	private String queryDescription;
	private String expectedResult;
	private String actualResult;
	private Timestamp icsRunDate;
	private long queryExecutionTime;
	private String isCritical;
	private String isSuccess;
	private String runBy;

	public ICSDailyQueryStatusBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Timestamp getIcsRunDate() {
		return icsRunDate;
	}

	public void setIcsRunDate(Timestamp icsRunDate) {
		this.icsRunDate = icsRunDate;
	}

	public long getQueryExecutionTime() {
		return queryExecutionTime;
	}

	public void setQueryExecutionTime(long queryExecutionTime) {
		this.queryExecutionTime = queryExecutionTime;
	}

	public String getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(String isCritical) {
		this.isCritical = isCritical;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getRunBy() {
		return runBy;
	}

	public void setRunBy(String runBy) {
		this.runBy = runBy;
	}
}