package com.skilrock.lms.userMgmt.javaBeans;

public class UpdateClaimCriteriaBean {
	private int retOrgId;
	private String retOrgName;
	private boolean selfRetailer;
	private boolean selfAgent;
	private boolean otherRetailerSameAgent;
	private boolean otherRetailer;
	private boolean otherAgent;
	private boolean atBO;

	public UpdateClaimCriteriaBean() {
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public boolean isSelfRetailer() {
		return selfRetailer;
	}

	public void setSelfRetailer(boolean selfRetailer) {
		this.selfRetailer = selfRetailer;
	}

	public boolean isSelfAgent() {
		return selfAgent;
	}

	public void setSelfAgent(boolean selfAgent) {
		this.selfAgent = selfAgent;
	}

	public boolean isOtherRetailerSameAgent() {
		return otherRetailerSameAgent;
	}

	public void setOtherRetailerSameAgent(boolean otherRetailerSameAgent) {
		this.otherRetailerSameAgent = otherRetailerSameAgent;
	}

	public boolean isOtherRetailer() {
		return otherRetailer;
	}

	public void setOtherRetailer(boolean otherRetailer) {
		this.otherRetailer = otherRetailer;
	}

	public boolean isOtherAgent() {
		return otherAgent;
	}

	public void setOtherAgent(boolean otherAgent) {
		this.otherAgent = otherAgent;
	}

	public boolean isAtBO() {
		return atBO;
	}

	public void setAtBO(boolean atBO) {
		this.atBO = atBO;
	}
}