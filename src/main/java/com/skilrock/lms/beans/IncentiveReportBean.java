package com.skilrock.lms.beans;

import java.util.Map;

public class IncentiveReportBean {

	String agentName;
	String retailerName;
	String address;
	double gameTotal;
	int agentOrgId;
	Map<String, CompleteCollectionBean> gameBeanMap;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Map<String, CompleteCollectionBean> getGameBeanMap() {
		return gameBeanMap;
	}
	public void setGameBeanMap(Map<String, CompleteCollectionBean> gameBeanMap) {
		this.gameBeanMap = gameBeanMap;
	}
	public double getGameTotal() {
		return gameTotal;
	}
	public void setGameTotal(double gameTotal) {
		this.gameTotal = gameTotal;
	}
	public int getAgentOrgId() {
		return agentOrgId;
	}
	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}
	
}
