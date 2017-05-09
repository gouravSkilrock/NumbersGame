package com.skilrock.lms.beans;


import java.util.List;

public class AgentWiseRetActivityBean {

	private String agentName;
	private String orgId;
	private String orgStatus;
	private String city;
	private int activeRet;
	private double activeRetPercentage;
	private int newLoginRet;
	private int assignedTotal;
	private int notAssignedTotal;
	private List<GameSaleDetailsBean> gameSaleDataList ;
	
	public String getAgentName() {
		return agentName;
	}
	public String getOrgStatus() {
		return orgStatus;
	}
	public String getCity() {
		return city;
	}
	public int getActiveRet() {
		return activeRet;
	}
	public int getNewLoginRet() {
		return newLoginRet;
	}
	public int getAssignedTotal() {
		return assignedTotal;
	}
	public int getNotAssignedTotal() {
		return notAssignedTotal;
	}
	public List<GameSaleDetailsBean> getGameSaleDataList() {
		return gameSaleDataList;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setActiveRet(int activeRet) {
		this.activeRet = activeRet;
	}
	public void setNewLoginRet(int newLoginRet) {
		this.newLoginRet = newLoginRet;
	}
	public void setAssignedTotal(int assignedTotal) {
		this.assignedTotal = assignedTotal;
	}
	public void setNotAssignedTotal(int notAssignedTotal) {
		this.notAssignedTotal = notAssignedTotal;
	}
	public void setGameSaleDataList(List<GameSaleDetailsBean> gameSaleDataList) {
		this.gameSaleDataList = gameSaleDataList;
	}
	public double getActiveRetPercentage() {
		return activeRetPercentage;
	}
	public void setActiveRetPercentage(double activeRetPercentage) {
		this.activeRetPercentage = activeRetPercentage;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
