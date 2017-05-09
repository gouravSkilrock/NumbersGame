package com.skilrock.lms.api.lmsWrapper.beans;

import java.util.HashMap;
import java.util.Map;


public class LmsWrapperCollectionReportOverAllBean {
	String agentName;
	double cash;
	double cheque;
	double chequeReturn;
	double credit;
	double debit;
	double dgCancel;
	double dgDirPlyPwt;
	double dgPwt;
	double dgSale;
	double iwCancel;
	double iwDirPlyPwt;
	double iwPwt;
	double iwSale;
	HashMap<String, LmsWrapperCompleteCollectionBean> gameBeanMap = null;
	Map<String, String> gameMap=null;
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
	int agtId;
	
	private String systemUserName;
	private String systemPassword;
	private boolean isSuccess;
	private String errorCode;
	private String reportType;
	private String reportTime;
	private String startDate;
	private String endDate;
	private String lmsDateFormat;
	private LmsWrapperCompleteCollectionBean lmsWrapperCompleteCollectionBean;
	
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

	public String getSystemUserName() {
		return systemUserName;
	}

	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}

	public String getSystemPassword() {
		return systemPassword;
	}

	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
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

	public String getLmsDateFormat() {
		return lmsDateFormat;
	}

	public void setLmsDateFormat(String lmsDateFormat) {
		this.lmsDateFormat = lmsDateFormat;
	}

	public HashMap<String, LmsWrapperCompleteCollectionBean> getGameBeanMap() {
		return gameBeanMap;
	}

	public void setGameBeanMap(
			HashMap<String, LmsWrapperCompleteCollectionBean> gameBeanMap) {
		this.gameBeanMap = gameBeanMap;
	}

	public Map<String, String> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<String, String> gameMap) {
		this.gameMap = gameMap;
	}

	public LmsWrapperCompleteCollectionBean getLmsWrapperCompleteCollectionBean() {
		return lmsWrapperCompleteCollectionBean;
	}

	public void setLmsWrapperCompleteCollectionBean(
			LmsWrapperCompleteCollectionBean lmsWrapperCompleteCollectionBean) {
		this.lmsWrapperCompleteCollectionBean = lmsWrapperCompleteCollectionBean;
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
	
	
	
	
}
