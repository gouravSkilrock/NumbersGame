package com.skilrock.lms.userMgmt.javaBeans;

public class UpdatePayoutCenterBean {
	private int orgId;
	private String orgName;
	private boolean claimAtSelf;
	private boolean claimAtOther;
	private String verificationLimit;
	private String claimLimit;
	private String minClaimAmount;
	private String maxClaimAmount;

	public UpdatePayoutCenterBean() {
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean isClaimAtSelf() {
		return claimAtSelf;
	}

	public void setClaimAtSelf(boolean claimAtSelf) {
		this.claimAtSelf = claimAtSelf;
	}

	public boolean isClaimAtOther() {
		return claimAtOther;
	}

	public void setClaimAtOther(boolean claimAtOther) {
		this.claimAtOther = claimAtOther;
	}

	public String getVerificationLimit() {
		return verificationLimit;
	}

	public void setVerificationLimit(String verificationLimit) {
		this.verificationLimit = verificationLimit;
	}

	public String getClaimLimit() {
		return claimLimit;
	}

	public void setClaimLimit(String claimLimit) {
		this.claimLimit = claimLimit;
	}

	public String getMinClaimAmount() {
		return minClaimAmount;
	}

	public void setMinClaimAmount(String minClaimAmount) {
		this.minClaimAmount = minClaimAmount;
	}

	public String getMaxClaimAmount() {
		return maxClaimAmount;
	}

	public void setMaxClaimAmount(String maxClaimAmount) {
		this.maxClaimAmount = maxClaimAmount;
	}
}