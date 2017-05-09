package com.skilrock.lms.beans;

import java.util.Date;

public class chequeForClearanceBean {

	private int agtOrgId;
	private String agtOrgName;
	private Double chequeAmt;
	private Date chequeDate;
	private String chequeNbr;
	private Date chequeReceivingDate;
	private String chequeStatus;
	private Date clearanceDate;
	private String draweeBank;
	private String issuingPartyName;
	private String taskId;

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public Double getChequeAmt() {
		return chequeAmt;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public String getChequeNbr() {
		return chequeNbr;
	}

	public Date getChequeReceivingDate() {
		return chequeReceivingDate;
	}

	public String getChequeStatus() {
		return chequeStatus;
	}

	public Date getClearanceDate() {
		return clearanceDate;
	}

	public String getDraweeBank() {
		return draweeBank;
	}

	public String getIssuingPartyName() {
		return issuingPartyName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setChequeAmt(Double chequeAmt) {
		this.chequeAmt = chequeAmt;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNbr(String chequeNbr) {
		this.chequeNbr = chequeNbr;
	}

	public void setChequeReceivingDate(Date chequeReceivingDate) {
		this.chequeReceivingDate = chequeReceivingDate;
	}

	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}

	public void setClearanceDate(Date clearanceDate) {
		this.clearanceDate = clearanceDate;
	}

	public void setDraweeBank(String draweeBank) {
		this.draweeBank = draweeBank;
	}

	public void setIssuingPartyName(String issuingPartyName) {
		this.issuingPartyName = issuingPartyName;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}