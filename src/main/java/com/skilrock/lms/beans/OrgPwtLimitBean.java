package com.skilrock.lms.beans;

public class OrgPwtLimitBean {

	private double approvalLimit;
	private String isPwtAutoScrap;
	private int organizationId;
	private String orgCode;
	private double payLimit;
	private double scrapLimit;
	private double verificationLimit;
	private double olaDepositLimit;
	private double olaWithdrawlLimit;
	private boolean claimAanyTicket;
	private boolean actingAspwt;
	private double maxDailyClaim;
	private String status;
	private double levyRate;
	private double securityDepositRate;
	private String selfClaim;
	private String otherClaim;
	private double minClaimPerTicket;
	private double maxClaimPerTicket;
	private double blockAmt;
	private int blockDays;
	private String blockAction;

	public double getOlaDepositLimit() {
		return olaDepositLimit;
	}

	public void setOlaDepositLimit(double olaDepositLimit) {
		this.olaDepositLimit = olaDepositLimit;
	}

	public double getOlaWithdrawlLimit() {
		return olaWithdrawlLimit;
	}

	public void setOlaWithdrawlLimit(double olaWithdrawlLimit) {
		this.olaWithdrawlLimit = olaWithdrawlLimit;
	}

	public double getApprovalLimit() {
		return approvalLimit;
	}

	public String getIsPwtAutoScrap() {
		return isPwtAutoScrap;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public double getPayLimit() {
		return payLimit;
	}

	public double getScrapLimit() {
		return scrapLimit;
	}

	public double getVerificationLimit() {
		return verificationLimit;
	}

	public void setApprovalLimit(double approvalLimit) {
		this.approvalLimit = approvalLimit;
	}

	public void setIsPwtAutoScrap(String isPwtAutoScrap) {
		this.isPwtAutoScrap = isPwtAutoScrap;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public void setPayLimit(double payLimit) {
		this.payLimit = payLimit;
	}

	public void setScrapLimit(double scrapLimit) {
		this.scrapLimit = scrapLimit;
	}

	public void setVerificationLimit(double verificationLimit) {
		this.verificationLimit = verificationLimit;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setClaimAanyTicket(boolean claimAanyTicket) {
		this.claimAanyTicket = claimAanyTicket;
	}

	public boolean isClaimAanyTicket() {
		return claimAanyTicket;
	}

	public void setMaxDailyClaim(double maxDailyClaim) {
		this.maxDailyClaim = maxDailyClaim;
	}

	public double getMaxDailyClaim() {
		return maxDailyClaim;
	}

	public void setActingAspwt(boolean actingAspwt) {
		this.actingAspwt = actingAspwt;
	}

	public boolean getActingAspwt() {
		return actingAspwt;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public double getLevyRate() {
		return levyRate;
	}

	public void setLevyRate(double levyRate) {
		this.levyRate = levyRate;
	}

	public double getSecurityDepositRate() {
		return securityDepositRate;
	}

	public void setSecurityDepositRate(double securityDepositRate) {
		this.securityDepositRate = securityDepositRate;
	}
	public String getSelfClaim() {
		return selfClaim;
	}

	public void setSelfClaim(String selfClaim) {
		this.selfClaim = selfClaim;
	}

	public String getOtherClaim() {
		return otherClaim;
	}

	public void setOtherClaim(String otherClaim) {
		this.otherClaim = otherClaim;
	}

	public double getMinClaimPerTicket() {
		return minClaimPerTicket;
	}

	public void setMinClaimPerTicket(double minClaimPerTicket) {
		this.minClaimPerTicket = minClaimPerTicket;
	}

	public double getMaxClaimPerTicket() {
		return maxClaimPerTicket;
	}

	public void setMaxClaimPerTicket(double maxClaimPerTicket) {
		this.maxClaimPerTicket = maxClaimPerTicket;
	}

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
}