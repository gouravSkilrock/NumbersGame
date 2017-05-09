package com.skilrock.lms.beans;

import java.io.Serializable;

public class RetWiseSummaryTxnBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int retOrgId;
	private String retName;
	private int dgSaleCount;
	private int dgRefundCount;
	private int dgPwtCount;
	private int totalCount;

	public RetWiseSummaryTxnBean() {
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public String getRetName() {
		return retName;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public int getDgSaleCount() {
		return dgSaleCount;
	}

	public void setDgSaleCount(int dgSaleCount) {
		this.dgSaleCount = dgSaleCount;
	}

	public int getDgRefundCount() {
		return dgRefundCount;
	}

	public void setDgRefundCount(int dgRefundCount) {
		this.dgRefundCount = dgRefundCount;
	}

	public int getDgPwtCount() {
		return dgPwtCount;
	}

	public void setDgPwtCount(int dgPwtCount) {
		this.dgPwtCount = dgPwtCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}