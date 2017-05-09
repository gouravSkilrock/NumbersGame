package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.Map;

public class AgentCollectionReportOverAllBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String retailerName;
	double cash;
	double cheque;
	double chequeReturn;
	double credit;
	double debit;
	double dgCancel;
	double dgDirPlyPwt;
	double dgPwt;
	double dgSale;
	Map<String, CompleteCollectionBean> gameBeanMap = null;
	double openingBal;
	double seDirPlyPwt;
	double sePwt;
	double seSale;
	double CSSale;
	double CSCancel;
	double withdrawal;
	double deposit;
	double withdrawalRefund;
	double depositRefund;
	double netGamingComm;
	double bankDep;
	double seCancel;
	int retId;
	private int retailerOrgId;
	private double clLimit;
	private double xclLimit;
	private double liveOpeningBal;
	private double netTxnAmt;

	double iwSale;
	double iwCancel;
	double iwPwt;
	double iwDirPlyPwt;

	double vsSale;
	double vsCancel;
	double vsPwt;
	double vsDirPlyPwt;

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getCheque() {
		return cheque;
	}

	public void setCheque(double cheque) {
		this.cheque = cheque;
	}

	public double getChequeReturn() {
		return chequeReturn;
	}

	public void setChequeReturn(double chequeReturn) {
		this.chequeReturn = chequeReturn;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public double getDgCancel() {
		return dgCancel;
	}

	public void setDgCancel(double dgCancel) {
		this.dgCancel = dgCancel;
	}

	public double getDgDirPlyPwt() {
		return dgDirPlyPwt;
	}

	public void setDgDirPlyPwt(double dgDirPlyPwt) {
		this.dgDirPlyPwt = dgDirPlyPwt;
	}

	public double getDgPwt() {
		return dgPwt;
	}

	public void setDgPwt(double dgPwt) {
		this.dgPwt = dgPwt;
	}

	public double getDgSale() {
		return dgSale;
	}

	public void setDgSale(double dgSale) {
		this.dgSale = dgSale;
	}

	public Map<String, CompleteCollectionBean> getGameBeanMap() {
		return gameBeanMap;
	}

	public void setGameBeanMap(Map<String, CompleteCollectionBean> gameBeanMap) {
		this.gameBeanMap = gameBeanMap;
	}

	public double getOpeningBal() {
		return openingBal;
	}

	public void setOpeningBal(double openingBal) {
		this.openingBal = openingBal;
	}

	public double getSeDirPlyPwt() {
		return seDirPlyPwt;
	}

	public void setSeDirPlyPwt(double seDirPlyPwt) {
		this.seDirPlyPwt = seDirPlyPwt;
	}

	public double getSePwt() {
		return sePwt;
	}

	public void setSePwt(double sePwt) {
		this.sePwt = sePwt;
	}

	public double getSeSale() {
		return seSale;
	}

	public void setSeSale(double seSale) {
		this.seSale = seSale;
	}

	public double getCSSale() {
		return CSSale;
	}

	public void setCSSale(double cSSale) {
		CSSale = cSSale;
	}

	public double getCSCancel() {
		return CSCancel;
	}

	public void setCSCancel(double cSCancel) {
		CSCancel = cSCancel;
	}

	public double getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(double withdrawal) {
		this.withdrawal = withdrawal;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public double getWithdrawalRefund() {
		return withdrawalRefund;
	}

	public void setWithdrawalRefund(double withdrawalRefund) {
		this.withdrawalRefund = withdrawalRefund;
	}

	public double getDepositRefund() {
		return depositRefund;
	}

	public void setDepositRefund(double depositRefund) {
		this.depositRefund = depositRefund;
	}

	public double getNetGamingComm() {
		return netGamingComm;
	}

	public void setNetGamingComm(double netGamingComm) {
		this.netGamingComm = netGamingComm;
	}

	public double getBankDep() {
		return bankDep;
	}

	public void setBankDep(double bankDep) {
		this.bankDep = bankDep;
	}

	public double getSeCancel() {
		return seCancel;
	}

	public void setSeCancel(double seCancel) {
		this.seCancel = seCancel;
	}

	public int getRetId() {
		return retId;
	}

	public void setRetId(int retId) {
		this.retId = retId;
	}

	public int getRetailerOrgId() {
		return retailerOrgId;
	}

	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}

	public double getClLimit() {
		return clLimit;
	}

	public void setClLimit(double clLimit) {
		this.clLimit = clLimit;
	}

	public double getXclLimit() {
		return xclLimit;
	}

	public void setXclLimit(double xclLimit) {
		this.xclLimit = xclLimit;
	}

	public double getLiveOpeningBal() {
		return liveOpeningBal;
	}

	public void setLiveOpeningBal(double liveOpeningBal) {
		this.liveOpeningBal = liveOpeningBal;
	}

	public double getNetTxnAmt() {
		return netTxnAmt;
	}

	public void setNetTxnAmt(double netTxnAmt) {
		this.netTxnAmt = netTxnAmt;
	}

	public double getIwSale() {
		return iwSale;
	}

	public void setIwSale(double iwSale) {
		this.iwSale = iwSale;
	}

	public double getIwCancel() {
		return iwCancel;
	}

	public void setIwCancel(double iwCancel) {
		this.iwCancel = iwCancel;
	}

	public double getIwPwt() {
		return iwPwt;
	}

	public void setIwPwt(double iwPwt) {
		this.iwPwt = iwPwt;
	}

	public double getIwDirPlyPwt() {
		return iwDirPlyPwt;
	}

	public void setIwDirPlyPwt(double iwDirPlyPwt) {
		this.iwDirPlyPwt = iwDirPlyPwt;
	}

	public double getVsSale() {
		return vsSale;
	}

	public void setVsSale(double vsSale) {
		this.vsSale = vsSale;
	}

	public double getVsCancel() {
		return vsCancel;
	}

	public void setVsCancel(double vsCancel) {
		this.vsCancel = vsCancel;
	}

	public double getVsPwt() {
		return vsPwt;
	}

	public void setVsPwt(double vsPwt) {
		this.vsPwt = vsPwt;
	}

	public double getVsDirPlyPwt() {
		return vsDirPlyPwt;
	}

	public void setVsDirPlyPwt(double vsDirPlyPwt) {
		this.vsDirPlyPwt = vsDirPlyPwt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "AgentCollectionReportOverAllBean [retailerName=" + retailerName
				+ ", cash=" + cash + ", cheque=" + cheque + ", chequeReturn="
				+ chequeReturn + ", credit=" + credit + ", debit=" + debit
				+ ", dgCancel=" + dgCancel + ", dgDirPlyPwt=" + dgDirPlyPwt
				+ ", dgPwt=" + dgPwt + ", dgSale=" + dgSale + ", gameBeanMap="
				+ gameBeanMap + ", openingBal=" + openingBal + ", seDirPlyPwt="
				+ seDirPlyPwt + ", sePwt=" + sePwt + ", seSale=" + seSale
				+ ", CSSale=" + CSSale + ", CSCancel=" + CSCancel
				+ ", withdrawal=" + withdrawal + ", deposit=" + deposit
				+ ", withdrawalRefund=" + withdrawalRefund + ", depositRefund="
				+ depositRefund + ", netGamingComm=" + netGamingComm
				+ ", bankDep=" + bankDep + ", seCancel=" + seCancel
				+ ", retId=" + retId + ", retailerOrgId=" + retailerOrgId
				+ ", clLimit=" + clLimit + ", xclLimit=" + xclLimit
				+ ", liveOpeningBal=" + liveOpeningBal + ", netTxnAmt="
				+ netTxnAmt + ", iwSale=" + iwSale + ", iwCancel=" + iwCancel
				+ ", iwPwt=" + iwPwt + ", iwDirPlyPwt=" + iwDirPlyPwt
				+ ", vsSale=" + vsSale + ", vsCancel=" + vsCancel + ", vsPwt="
				+ vsPwt + ", vsDirPlyPwt=" + vsDirPlyPwt + "]";
	}

}
