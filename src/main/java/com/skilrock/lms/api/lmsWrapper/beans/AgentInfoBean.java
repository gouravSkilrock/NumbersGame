package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class AgentInfoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
	private String agentName;
	private int agentOrgId;
	private String balance;
	private int userId;
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public int getAgentOrgId() {
		return agentOrgId;
	}
	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
