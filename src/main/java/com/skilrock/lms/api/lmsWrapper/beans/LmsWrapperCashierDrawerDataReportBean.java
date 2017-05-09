package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperCashierDrawerDataReportBean implements Serializable {

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
	private String systemUserName;
	private String systemPassword;
	private boolean isSuccess;
	private String errorCode;
	private int userId; 
	private String userName;
	private String totalWinningAmt;
	private String reportType;
	private String reportTime;
	private String startDate;
	private String endDate;
	private String date;
	private double paymentAmount;
	private String paymentType;
	private String voucherNo;
	private String bankName;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTotalWinningAmt() {
		return totalWinningAmt;
	}

	public void setTotalWinningAmt(String totalWinningAmt) {
		this.totalWinningAmt = totalWinningAmt;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	
	
}
