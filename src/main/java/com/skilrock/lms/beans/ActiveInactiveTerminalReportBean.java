package com.skilrock.lms.beans;

public class ActiveInactiveTerminalReportBean {

	String retId;
	String agentId;
	String region;
	String idleTime;
	String agentName;
	int activePos;
	int inActivePos;
	int totalPos;
	double saleAmount;
	String zone;
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public double getSaleAmount() {
		return saleAmount;
	}
	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}
	public String getRetId() {
		return retId;
	}
	public void setRetId(String retId) {
		this.retId = retId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getIdleTime() {
		return idleTime;
	}
	public void setIdleTime(String idleTime) {
		this.idleTime = idleTime;
	}
	public int getActivePos() {
		return activePos;
	}
	public void setActivePos(int activePos) {
		this.activePos = activePos;
	}
	public int getInActivePos() {
		return inActivePos;
	}
	public void setInActivePos(int inActivePos) {
		this.inActivePos = inActivePos;
	}
	public int getTotalPos() {
		return totalPos;
	}
	public void setTotalPos(int totalPos) {
		this.totalPos = totalPos;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
}