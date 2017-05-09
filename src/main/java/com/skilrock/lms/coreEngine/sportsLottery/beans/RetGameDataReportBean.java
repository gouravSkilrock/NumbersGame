package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;

public class RetGameDataReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int retailerId;
	private int agentId;
	private String gameName;
	private String gameTypeName;
	private double saleAmount;
	private double pwtAmount;

	public RetGameDataReportBean() {
	}

	public int getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(int retailerId) {
		this.retailerId = retailerId;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public double getPwtAmount() {
		return pwtAmount;
	}

	public void setPwtAmount(double pwtAmount) {
		this.pwtAmount = pwtAmount;
	}

	@Override
	public String toString() {
		return "RetGameDataReportBean [agentId=" + agentId + ", gameName="
				+ gameName + ", gameTypeName=" + gameTypeName + ", pwtAmount="
				+ pwtAmount + ", retailerId=" + retailerId + ", saleAmount="
				+ saleAmount + "]";
	}
}