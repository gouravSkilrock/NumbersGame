package com.skilrock.lms.beans;

import java.io.Serializable;

public class PaymentReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int agentOrgId;
	private int retailerOrgId;
	private String name;

	private double cashAmt;
	private double chequeAmt;
	private double chequeBounceAmt;
	private double creditAmt;
	private double debitAmt;
	private double bankDepositAmt;
	private double netPayment;

	public PaymentReportBean() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCashAmt() {
		return cashAmt;
	}

	public void setCashAmt(double cashAmt) {
		this.cashAmt = cashAmt;
	}

	public double getChequeAmt() {
		return chequeAmt;
	}

	public void setChequeAmt(double chequeAmt) {
		this.chequeAmt = chequeAmt;
	}

	public double getChequeBounceAmt() {
		return chequeBounceAmt;
	}

	public void setChequeBounceAmt(double chequeBounceAmt) {
		this.chequeBounceAmt = chequeBounceAmt;
	}

	public double getCreditAmt() {
		return creditAmt;
	}

	public void setCreditAmt(double creditAmt) {
		this.creditAmt = creditAmt;
	}

	public double getDebitAmt() {
		return debitAmt;
	}

	public void setDebitAmt(double debitAmt) {
		this.debitAmt = debitAmt;
	}

	public double getBankDepositAmt() {
		return bankDepositAmt;
	}

	public void setBankDepositAmt(double bankDepositAmt) {
		this.bankDepositAmt = bankDepositAmt;
	}

	public double getNetPayment() {
		return netPayment;
	}

	public void setNetPayment(double netPayment) {
		this.netPayment = netPayment;
	}

	@Override
	public String toString() {
		return "AgentOrgId - "+agentOrgId+" RetailerOrgId - "+retailerOrgId+" Name - "+name+" Cash - "+cashAmt+" Cheque - "+chequeAmt+" ChequeBounce - "+chequeBounceAmt+" Debit - "+debitAmt+" Credit - "+creditAmt+" BankDeposit - "+bankDepositAmt+" NetPayment - "+netPayment;
	}
}