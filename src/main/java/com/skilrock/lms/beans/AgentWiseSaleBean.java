package com.skilrock.lms.beans;

import java.io.Serializable;

public class AgentWiseSaleBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String agentName;
	private double mrpAmt;
	private double agentComm;
	private double govtComm;
	private double agentLevyComm;
	private String dateChanged;
	private double oldLevyRate;
	private double newLevyRate;
	private double fixLevyRate;
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public double getMrpAmt() {
		return mrpAmt;
	}
	public void setMrpAmt(double mrpAmt) {
		this.mrpAmt = mrpAmt;
	}
	public double getAgentComm() {
		return agentComm;
	}
	public void setAgentComm(double agentComm) {
		this.agentComm = agentComm;
	}
	public double getGovtComm() {
		return govtComm;
	}
	public void setGovtComm(double govtComm) {
		this.govtComm = govtComm;
	}
	public double getAgentLevyComm() {
		return agentLevyComm;
	}
	public void setAgentLevyComm(double agentLevyComm) {
		this.agentLevyComm = agentLevyComm;
	}
	public String getDateChanged() {
		return dateChanged;
	}
	public void setDateChanged(String dateChanged) {
		this.dateChanged = dateChanged;
	}
	public double getOldLevyRate() {
		return oldLevyRate;
	}
	public void setOldLevyRate(double oldLevyRate) {
		this.oldLevyRate = oldLevyRate;
	}
	public double getNewLevyRate() {
		return newLevyRate;
	}
	public void setNewLevyRate(double newLevyRate) {
		this.newLevyRate = newLevyRate;
	}
	public double getFixLevyRate() {
		return fixLevyRate;
	}
	public void setFixLevyRate(double fixLevyRate) {
		this.fixLevyRate = fixLevyRate;
	}
	
}
