package com.skilrock.lms.beans;

public class LimitDistributionBean {
	private String crLimit;
	private String distributableBal;
	private String distributedBal;
	private String liveBal;
	private String name;
	private int orgId;
	private String xcrLimit;
	private int xcrLimitUpto;

	public String getCrLimit() {
		return crLimit;
	}

	public String getDistributableBal() {
		return distributableBal;
	}

	public String getDistributedBal() {
		return distributedBal;
	}

	public String getLiveBal() {
		return liveBal;
	}

	public String getName() {
		return name;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getXcrLimit() {
		return xcrLimit;
	}

	public void setCrLimit(String crLimit) {
		this.crLimit = crLimit;
	}

	public void setDistributableBal(String distributableBal) {
		this.distributableBal = distributableBal;
	}

	public void setDistributedBal(String distributedBal) {
		this.distributedBal = distributedBal;
	}

	public void setLiveBal(String liveBal) {
		this.liveBal = liveBal;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setXcrLimit(String xcrLimit) {
		this.xcrLimit = xcrLimit;
	}

	public int getXcrLimitUpto() {
		return xcrLimitUpto;
	}

	public void setXcrLimitUpto(int xcrLimitUpto) {
		this.xcrLimitUpto = xcrLimitUpto;
	}
	

}
