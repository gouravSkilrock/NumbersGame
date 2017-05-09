package com.skilrock.lms.beans;

import java.io.Serializable;

public class CashierDrawerDataReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cheqBounce;
	private String credit;
	private String debit;
	private String name;
	private String netAmt;
	private int orgId;
	private String totalCash;
	private String totalChq;
	private String bankDeposit;
	private String winnnigAmt;
	private int userId;
	public String getCheqBounce() {
		return cheqBounce;
	}
	public void setCheqBounce(String cheqBounce) {
		this.cheqBounce = cheqBounce;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNetAmt() {
		return netAmt;
	}
	public void setNetAmt(String netAmt) {
		this.netAmt = netAmt;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	public String getTotalChq() {
		return totalChq;
	}
	public void setTotalChq(String totalChq) {
		this.totalChq = totalChq;
	}
	public String getBankDeposit() {
		return bankDeposit;
	}
	public void setBankDeposit(String bankDeposit) {
		this.bankDeposit = bankDeposit;
	}
	public String getWinnnigAmt() {
		return winnnigAmt;
	}
	public void setWinnnigAmt(String winnnigAmt) {
		this.winnnigAmt = winnnigAmt;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	
}
