package com.skilrock.lms.beans;

public class LimitDistributionReportBean
{
	private double crLimit;
	private double distributableBal;
	private double distributedBal;
	private double liveBal;
	private String name;
	private int orgId;
	private double xcrLimit;
	private int xcrLimitUpto;
	private int terminalCount;

	public LimitDistributionReportBean()
	{
	}

	public double getCrLimit() {
		return crLimit;
	}

	public void setCrLimit(double crLimit) {
		this.crLimit = crLimit;
	}

	public double getDistributableBal() {
		return distributableBal;
	}

	public void setDistributableBal(double distributableBal) {
		this.distributableBal = distributableBal;
	}

	public double getDistributedBal() {
		return distributedBal;
	}

	public void setDistributedBal(double distributedBal) {
		this.distributedBal = distributedBal;
	}

	public double getLiveBal() {
		return liveBal;
	}

	public void setLiveBal(double liveBal) {
		this.liveBal = liveBal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public double getXcrLimit() {
		return xcrLimit;
	}

	public void setXcrLimit(double xcrLimit) {
		this.xcrLimit = xcrLimit;
	}

	public int getXcrLimitUpto() {
		return xcrLimitUpto;
	}

	public void setXcrLimitUpto(int xcrLimitUpto) {
		this.xcrLimitUpto = xcrLimitUpto;
	}

	public int getTerminalCount() {
		return terminalCount;
	}

	public void setTerminalCount(int terminalCount) {
		this.terminalCount = terminalCount;
	}
	
	
}