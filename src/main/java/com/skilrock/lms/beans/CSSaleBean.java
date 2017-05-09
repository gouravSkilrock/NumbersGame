package com.skilrock.lms.beans;

import java.util.Date;

public class CSSaleBean {
	private String userName;
	private int retOrgId;
	private double balance;
	private String prodCode;
	private String operatorCode;
	private String circleCode;
	private double denomination;
	private int prodId;
	private int categoryId;
	private String categoryCode;
	private double unitPrice;
	private int Mult;
	private double MrpAmt;
	private double NetAmt;
	private int CSRefTxIdForRefund;
	private int CSRefTxId;
	private int RMSRefIdForRefund;
	private int RMSRefId;
	private Date TransTime;
	private CSOrgBean orgBean = new CSOrgBean();
	private String AuthCode;
	private String Status;
	private int errorCode;
	private String ReasonForCancel;

	public String getUserName() {
		return userName;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public double getBalance() {
		return balance;
	}

	public String getProdCode() {
		return prodCode;
	}

	public int getProdId() {
		return prodId;
	}
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public int getMult() {
		return Mult;
	}

	public double getMrpAmt() {
		return MrpAmt;
	}

	public double getNetAmt() {
		return NetAmt;
	}

	public int getCSRefTxId() {
		return CSRefTxId;
	}

	public int getRMSRefId() {
		return RMSRefId;
	}

	public CSOrgBean getOrgBean() {
		return orgBean;
	}

	public String getAuthCode() {
		return AuthCode;
	}

	public String getStatus() {
		return Status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getReasonForCancel() {
		return ReasonForCancel;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setMult(int mult) {
		Mult = mult;
	}

	public void setMrpAmt(double mrpAmt) {
		MrpAmt = mrpAmt;
	}

	public void setNetAmt(double netAmt) {
		NetAmt = netAmt;
	}

	public void setCSRefTxId(int cSRefTxId) {
		CSRefTxId = cSRefTxId;
	}

	public void setRMSRefId(int rMSRefId) {
		RMSRefId = rMSRefId;
	}

	public void setOrgBean(CSOrgBean orgBean) {
		this.orgBean = orgBean;
	}

	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setReasonForCancel(String reasonForCancel) {
		ReasonForCancel = reasonForCancel;
	}

	public int getCSRefTxIdForRefund() {
		return CSRefTxIdForRefund;
	}

	public int getRMSRefIdForRefund() {
		return RMSRefIdForRefund;
	}

	public void setCSRefTxIdForRefund(int cSRefTxIdForRefund) {
		CSRefTxIdForRefund = cSRefTxIdForRefund;
	}

	public void setRMSRefIdForRefund(int rMSRefIdForRefund) {
		RMSRefIdForRefund = rMSRefIdForRefund;
	}

	public Date getTransTime() {
		return TransTime;
	}

	public void setTransTime(Date transTime) {
		TransTime = transTime;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getCircleCode() {
		return circleCode;
	}

	public void setCircleCode(String circleCode) {
		this.circleCode = circleCode;
	}

	public double getDenomination() {
		return denomination;
	}

	public void setDenomination(double denomination) {
		this.denomination = denomination;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
}
