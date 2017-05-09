package com.skilrock.lms.beans;

import java.util.Map;

public class CollectionReportOverAllBean {
	String agentName;
	String parentName;
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

	double sleCancel;
	double sleDirPlyPwt;
	double slePwt;
	double sleSale;

	private double clLimit;
	private double netPayments;
	int agtId;
	private String noOfDays;
	private double closingBalance;

	double iwCancel;
	double iwDirPlyPwt;
	double iwPwt;
	double iwSale;

	double vsCancel;
	double vsDirPlyPwt;
	double vsPwt;
	double vsSale;
	
	private double training;
	private double incentive;
	private double others;
	
	
	public double getTraining() {
		return training;
	}

	public void setTraining(double training) {
		this.training = training;
	}

	public double getIncentive() {
		return incentive;
	}

	public void setIncentive(double incentive) {
		this.incentive = incentive;
	}

	public double getOthers() {
		return others;
	}

	public void setOthers(double others) {
		this.others = others;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public double getBankDep() {
		return bankDep;
	}

	public void setBankDep(double bankDep) {
		this.bankDep = bankDep;
	}

	public String getAgentName() {
		return agentName;
	}

	public double getCash() {
		return cash;
	}

	public double getCheque() {
		return cheque;
	}

	public double getChequeReturn() {
		return chequeReturn;
	}

	public double getCredit() {
		return credit;
	}

	public double getDebit() {
		return debit;
	}

	public double getDgCancel() {
		return dgCancel;
	}

	public double getDgDirPlyPwt() {
		return dgDirPlyPwt;
	}

	public double getDgPwt() {
		return dgPwt;
	}

	public double getDgSale() {
		return dgSale;
	}

	public Map<String, CompleteCollectionBean> getGameBeanMap() {
		return gameBeanMap;
	}

	public double getOpeningBal() {
		return openingBal;
	}

	public double getSeDirPlyPwt() {
		return seDirPlyPwt;
	}

	public double getSePwt() {
		return sePwt;
	}

	public double getSeSale() {
		return seSale;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public void setCheque(double cheque) {
		this.cheque = cheque;
	}

	public void setChequeReturn(double chequeReturn) {
		this.chequeReturn = chequeReturn;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public void setDgCancel(double dgCancel) {
		this.dgCancel = dgCancel;
	}

	public void setDgDirPlyPwt(double dgDirPlyPwt) {
		this.dgDirPlyPwt = dgDirPlyPwt;
	}

	public void setDgPwt(double dgPwt) {
		this.dgPwt = dgPwt;
	}

	public void setDgSale(double dgSale) {
		this.dgSale = dgSale;
	}

	public void setGameBeanMap(Map<String, CompleteCollectionBean> gameBeanMap) {
		this.gameBeanMap = gameBeanMap;
	}

	public void setOpeningBal(double openingBal) {
		this.openingBal = openingBal;
	}

	public void setSeDirPlyPwt(double seDirPlyPwt) {
		this.seDirPlyPwt = seDirPlyPwt;
	}

	public void setSePwt(double sePwt) {
		this.sePwt = sePwt;
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

	public double getSeCancel() {
		return seCancel;
	}

	public void setSeCancel(double seCancel) {
		this.seCancel = seCancel;
	}

	public int getAgtId() {
		return agtId;
	}

	public void setAgtId(int agtId) {
		this.agtId = agtId;
	}

	public void setClLimit(double clLimit) {
		this.clLimit = clLimit;
	}

	public double getClLimit() {
		return clLimit;
	}

	public double getNetPayments() {
		return netPayments;
	}

	public void setNetPayments(double netPayments) {
		this.netPayments = netPayments;
	}

	public double getSleCancel() {
		return sleCancel;
	}

	public void setSleCancel(double sleCancel) {
		this.sleCancel = sleCancel;
	}

	public double getSleDirPlyPwt() {
		return sleDirPlyPwt;
	}

	public void setSleDirPlyPwt(double sleDirPlyPwt) {
		this.sleDirPlyPwt = sleDirPlyPwt;
	}

	public double getSlePwt() {
		return slePwt;
	}

	public void setSlePwt(double slePwt) {
		this.slePwt = slePwt;
	}

	public double getSleSale() {
		return sleSale;
	}

	public void setSleSale(double sleSale) {
		this.sleSale = sleSale;
	}

	public double getIwCancel() {
		return iwCancel;
	}

	public void setIwCancel(double iwCancel) {
		this.iwCancel = iwCancel;
	}

	public double getIwDirPlyPwt() {
		return iwDirPlyPwt;
	}

	public void setIwDirPlyPwt(double iwDirPlyPwt) {
		this.iwDirPlyPwt = iwDirPlyPwt;
	}

	public double getIwPwt() {
		return iwPwt;
	}

	public void setIwPwt(double iwPwt) {
		this.iwPwt = iwPwt;
	}

	public double getIwSale() {
		return iwSale;
	}

	public void setIwSale(double iwSale) {
		this.iwSale = iwSale;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public double getVsCancel() {
		return vsCancel;
	}

	public void setVsCancel(double vsCancel) {
		this.vsCancel = vsCancel;
	}

	public double getVsDirPlyPwt() {
		return vsDirPlyPwt;
	}

	public void setVsDirPlyPwt(double vsDirPlyPwt) {
		this.vsDirPlyPwt = vsDirPlyPwt;
	}

	public double getVsPwt() {
		return vsPwt;
	}

	public void setVsPwt(double vsPwt) {
		this.vsPwt = vsPwt;
	}

	public double getVsSale() {
		return vsSale;
	}

	public void setVsSale(double vsSale) {
		this.vsSale = vsSale;
	}

	@Override
	public String toString() {
		return "CollectionReportOverAllBean [agentName=" + agentName
				+ ", parentName=" + parentName + ", cash=" + cash + ", cheque="
				+ cheque + ", chequeReturn=" + chequeReturn + ", credit="
				+ credit + ", debit=" + debit + ", dgCancel=" + dgCancel
				+ ", dgDirPlyPwt=" + dgDirPlyPwt + ", dgPwt=" + dgPwt
				+ ", dgSale=" + dgSale + ", gameBeanMap=" + gameBeanMap
				+ ", openingBal=" + openingBal + ", seDirPlyPwt=" + seDirPlyPwt
				+ ", sePwt=" + sePwt + ", seSale=" + seSale + ", CSSale="
				+ CSSale + ", CSCancel=" + CSCancel + ", withdrawal="
				+ withdrawal + ", deposit=" + deposit + ", withdrawalRefund="
				+ withdrawalRefund + ", depositRefund=" + depositRefund
				+ ", netGamingComm=" + netGamingComm + ", bankDep=" + bankDep
				+ ", seCancel=" + seCancel + ", sleCancel=" + sleCancel
				+ ", sleDirPlyPwt=" + sleDirPlyPwt + ", slePwt=" + slePwt
				+ ", sleSale=" + sleSale + ", clLimit=" + clLimit
				+ ", netPayments=" + netPayments + ", agtId=" + agtId
				+ ", noOfDays=" + noOfDays + ", closingBalance="
				+ closingBalance + ", iwCancel=" + iwCancel + ", iwDirPlyPwt="
				+ iwDirPlyPwt + ", iwPwt=" + iwPwt + ", iwSale=" + iwSale
				+ ", vsCancel=" + vsCancel + ", vsDirPlyPwt=" + vsDirPlyPwt
				+ ", vsPwt=" + vsPwt + ", vsSale=" + vsSale + ", training="
				+ training + ", incentive=" + incentive + ", others=" + others
				+ "]";
	}

	

}
