package com.skilrock.lms.beans;

import java.util.List;

public class chequeAgentBean {

	private String agtOrgName;
	private List<ChequeBeanClearance> chequeDetails;
	private String agentName;
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public List<ChequeBeanClearance> getChequeDetails() {
		return chequeDetails;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setChequeDetails(List<ChequeBeanClearance> chequeDetails) {
		this.chequeDetails = chequeDetails;
	}

}