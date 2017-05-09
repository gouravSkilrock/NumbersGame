package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.Map;

public class RankWiseWinningReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int retailerOrgId;
	private int agentOrgId;
	private Map<Integer, Integer> winningTktMap;
	private double totalSaleAmt;
	private double totalWinningAmt;
	private String retailerName;
	private String agentName;
	private int sum;
	
	public String getRetailerName() {
		return retailerName;
	}
	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}	
	public int getRetailerOrgId() {
		return retailerOrgId;
	}
	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}
	public int getAgentOrgId() {
		return agentOrgId;
	}
	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}
	public Map<Integer, Integer> getWinningTktMap() {
		return winningTktMap;
	}
	public void setWinningTktMap(Map<Integer, Integer> winningTktMap) {
		this.winningTktMap = winningTktMap;
	}
	public double getTotalSaleAmt() {
		return totalSaleAmt;
	}
	public void setTotalSaleAmt(double totalSaleAmt) {
		this.totalSaleAmt = totalSaleAmt;
	}
	public double getTotalWinningAmt() {
		return totalWinningAmt;
	}
	public void setTotalWinningAmt(double totalWinningAmt) {
		this.totalWinningAmt = totalWinningAmt;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getSum() {
		return sum;
	}
}