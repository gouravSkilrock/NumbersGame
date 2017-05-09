package com.skilrock.lms.beans;

public class CashChqReportBean {

	private String cheqBounce;
	private String credit;
	private String debit;
	private String name;
	private String netAmt;
	private int orgId;
	private String totalCash;
	private String totalChq;
	private String bankDeposit;
	private String claimAmt;
	private String totalClaim ;
	private String state;
	private String city;
	private double totalUserClaimAmount;
	private double currentBal;
	
	public String getBankDeposit() {
		return bankDeposit;
	}

	public void setBankDeposit(String bankDeposit) {
		this.bankDeposit = bankDeposit;
	}

	public String getCheqBounce() {
		return cheqBounce;
	}

	public String getCredit() {
		return credit;
	}

	public String getDebit() {
		return debit;
	}

	public String getName() {
		return name;
	}

	public String getNetAmt() {
		return netAmt;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getTotalCash() {
		return totalCash;
	}

	public String getTotalChq() {
		return totalChq;
	}

	public void setCheqBounce(String cheqBounce) {
		this.cheqBounce = cheqBounce;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public void setName(String Name) {
		this.name = Name;
	}

	public void setNetAmt(String netAmt) {
		this.netAmt = netAmt;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}

	public void setTotalChq(String totalChq) {
		this.totalChq = totalChq;
	}

	public String getClaimAmt() {
		return claimAmt;
	}

	public void setClaimAmt(String claimAmt) {
		this.claimAmt = claimAmt;
	}

	public String getTotalClaim() {
		return totalClaim;
	}

	public void setTotalClaim(String totalClaim) {
		this.totalClaim = totalClaim;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getTotalUserClaimAmount() {
		return totalUserClaimAmount;
	}

	public void setTotalUserClaimAmount(double totalUserClaimAmount) {
		this.totalUserClaimAmount = totalUserClaimAmount;
	}

	public double getCurrentBal() {
		return currentBal;
	}

	public void setCurrentBal(double currentBal) {
		this.currentBal = currentBal;
	}
	
	

}
