package com.skilrock.lms.beans;

import java.io.Serializable;

public class SecurityDepositAndLevyBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentName;
	private String retailerName;
	private String initialSD;
	private String expectedSD;
	private String collectedSD;
	private String remainingSD;
	private String levyRate;

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public String getInitialSD() {
		return initialSD;
	}

	public void setInitialSD(String initialSD) {
		this.initialSD = initialSD;
	}

	public String getExpectedSD() {
		return expectedSD;
	}

	public void setExpectedSD(String expectedSD) {
		this.expectedSD = expectedSD;
	}

	public String getCollectedSD() {
		return collectedSD;
	}

	public void setCollectedSD(String collectedSD) {
		this.collectedSD = collectedSD;
	}

	public String getRemainingSD() {
		return remainingSD;
	}

	public void setRemainingSD(String remainingSD) {
		this.remainingSD = remainingSD;
	}

	public String getLevyRate() {
		return levyRate;
	}

	public void setLevyRate(String levyRate) {
		this.levyRate = levyRate;
	}
}
