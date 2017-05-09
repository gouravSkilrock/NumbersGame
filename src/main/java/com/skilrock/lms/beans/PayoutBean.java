package com.skilrock.lms.beans;

public class PayoutBean {

	private int organizationId;
	private String organizationName;
	private double verificationLimit;
	private double dailyWinningClaimLimit;
	private String claimAnyWhere;
	private String winningRestriction;
	private double blockAmt;
	private int blockDays ;
	private String blockAction;
	
	
	public double getBlockAmt() {
		return blockAmt;
	}
	public void setBlockAmt(double blockAmt) {
		this.blockAmt = blockAmt;
	}
	public int getBlockDays() {
		return blockDays;
	}
	public void setBlockDays(int blockDays) {
		this.blockDays = blockDays;
	}
	public String getBlockAction() {
		return blockAction;
	}
	public void setBlockAction(String blockAction) {
		this.blockAction = blockAction;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public double getVerificationLimit() {
		return verificationLimit;
	}
	public void setVerificationLimit(double verificationLimit) {
		this.verificationLimit = verificationLimit;
	}
	public double getDailyWinningClaimLimit() {
		return dailyWinningClaimLimit;
	}
	public void setDailyWinningClaimLimit(double dailyWinningClaimLimit) {
		this.dailyWinningClaimLimit = dailyWinningClaimLimit;
	}
	public String getClaimAnyWhere() {
		return claimAnyWhere;
	}
	public void setClaimAnyWhere(String claimAnyWhere) {
		this.claimAnyWhere = claimAnyWhere;
	}
	public String getWinningRestriction() {
		return winningRestriction;
	}
	public void setWinningRestriction(String winningRestriction) {
		this.winningRestriction = winningRestriction;
	}
	

}
