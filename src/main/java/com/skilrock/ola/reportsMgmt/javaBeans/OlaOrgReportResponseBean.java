package com.skilrock.ola.reportsMgmt.javaBeans;

import java.io.Serializable;

public class OlaOrgReportResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int orgId;
	private double mrpDepositAmt;
	private double netDepositAmt;
	private double mrpWithdrawalAmt;
	private double netWithdrawalAmt;

	public OlaOrgReportResponseBean() {
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public double getMrpDepositAmt() {
		return mrpDepositAmt;
	}

	public void setMrpDepositAmt(double mrpDepositAmt) {
		this.mrpDepositAmt = mrpDepositAmt;
	}

	public double getNetDepositAmt() {
		return netDepositAmt;
	}

	public void setNetDepositAmt(double netDepositAmt) {
		this.netDepositAmt = netDepositAmt;
	}

	public double getMrpWithdrawalAmt() {
		return mrpWithdrawalAmt;
	}

	public void setMrpWithdrawalAmt(double mrpWithdrawalAmt) {
		this.mrpWithdrawalAmt = mrpWithdrawalAmt;
	}

	public double getNetWithdrawalAmt() {
		return netWithdrawalAmt;
	}

	public void setNetWithdrawalAmt(double netWithdrawalAmt) {
		this.netWithdrawalAmt = netWithdrawalAmt;
	}
}