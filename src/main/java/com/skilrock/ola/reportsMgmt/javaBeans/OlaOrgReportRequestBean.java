package com.skilrock.ola.reportsMgmt.javaBeans;

import java.io.Serializable;

public class OlaOrgReportRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int orgId;
	private String orgType;
	private String fromDate;
	private String toDate;
	private int walletId;

	public OlaOrgReportRequestBean() {
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public int getWalletId() {
		return walletId;
	}

	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}
}