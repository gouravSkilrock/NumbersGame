package com.skilrock.lms.coreEngine.accMgmt.javaBeans;

public class AgentAutoBlockBean {
	private boolean eligibleActive;
	private boolean eligibleBlock;
	private int orgId;
	private int userId;
	private String orgName;
	private double blockAmount;
	private int blockDays;
	private String blockAction;
	private String orgStatus;
	private String userStatus;
	private double firstClosingBalance;
	private double closingBalance;
	private int negitiveFromDays;

	public AgentAutoBlockBean() {
	}

	public boolean isEligibleActive() {
		return eligibleActive;
	}

	public void setEligibleActive(boolean eligibleActive) {
		this.eligibleActive = eligibleActive;
	}

	public boolean isEligibleBlock() {
		return eligibleBlock;
	}

	public void setEligibleBlock(boolean eligibleBlock) {
		this.eligibleBlock = eligibleBlock;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public double getBlockAmount() {
		return blockAmount;
	}

	public void setBlockAmount(double blockAmount) {
		this.blockAmount = blockAmount;
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

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public double getFirstClosingBalance() {
		return firstClosingBalance;
	}

	public void setFirstClosingBalance(double firstClosingBalance) {
		this.firstClosingBalance = firstClosingBalance;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public int getNegitiveFromDays() {
		return negitiveFromDays;
	}

	public void setNegitiveFromDays(int negitiveFromDays) {
		this.negitiveFromDays = negitiveFromDays;
	}
}