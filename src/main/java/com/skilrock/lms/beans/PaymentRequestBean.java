package com.skilrock.lms.beans;

import java.io.Serializable;

public class PaymentRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int agentOrgId;
	private int retailerOrgId;
	private String startDate;
	private String endDate;

	private boolean cashReq;
	private boolean chequeReq;
	private boolean chequeBounceReq;
	private boolean debitReq;
	private boolean creditReq;
	private boolean bankDepositReq;
	private boolean allDataReq;

	public PaymentRequestBean() {
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public int getRetailerOrgId() {
		return retailerOrgId;
	}

	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isCashReq() {
		return cashReq;
	}

	public void setCashReq(boolean cashReq) {
		this.cashReq = cashReq;
	}

	public boolean isChequeReq() {
		return chequeReq;
	}

	public void setChequeReq(boolean chequeReq) {
		this.chequeReq = chequeReq;
	}

	public boolean isChequeBounceReq() {
		return chequeBounceReq;
	}

	public void setChequeBounceReq(boolean chequeBounceReq) {
		this.chequeBounceReq = chequeBounceReq;
	}

	public boolean isDebitReq() {
		return debitReq;
	}

	public void setDebitReq(boolean debitReq) {
		this.debitReq = debitReq;
	}

	public boolean isCreditReq() {
		return creditReq;
	}

	public void setCreditReq(boolean creditReq) {
		this.creditReq = creditReq;
	}

	public boolean isBankDepositReq() {
		return bankDepositReq;
	}

	public void setBankDepositReq(boolean bankDepositReq) {
		this.bankDepositReq = bankDepositReq;
	}

	public boolean isAllDataReq() {
		return allDataReq;
	}

	public void setAllDataReq(boolean allDataReq) {
		this.allDataReq = allDataReq;
	}
}