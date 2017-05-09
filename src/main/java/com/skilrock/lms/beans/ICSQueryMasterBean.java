package com.skilrock.lms.beans;

import java.io.Serializable;

public class ICSQueryMasterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int queryId;
	private String queryTitle;
	private String mainQuery;
	private String isSP;
	private String isDateRequired;
	private String lastSuccessfulDate;
	private String queryResult;
	private String queryDescription;
	private String relatedTo;
	private String tierType;
	private String errorMessage;
	private String isCritical;
	private String queryStatus;
	private String lastUpdatedDate;
	private String updatedBy;

	public ICSQueryMasterBean() {
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

	public String getMainQuery() {
		return mainQuery;
	}

	public void setMainQuery(String mainQuery) {
		this.mainQuery = mainQuery;
	}

	public String getIsSP() {
		return isSP;
	}

	public void setIsSP(String isSP) {
		this.isSP = isSP;
	}

	public String getIsDateRequired() {
		return isDateRequired;
	}

	public void setIsDateRequired(String isDateRequired) {
		this.isDateRequired = isDateRequired;
	}

	public String getLastSuccessfulDate() {
		return lastSuccessfulDate;
	}

	public void setLastSuccessfulDate(String lastSuccessfulDate) {
		this.lastSuccessfulDate = lastSuccessfulDate;
	}

	public String getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(String queryResult) {
		this.queryResult = queryResult;
	}

	public String getQueryDescription() {
		return queryDescription;
	}

	public void setQueryDescription(String queryDescription) {
		this.queryDescription = queryDescription;
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

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}