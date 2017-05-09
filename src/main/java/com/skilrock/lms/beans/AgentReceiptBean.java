package com.skilrock.lms.beans;

import java.util.List;

public class AgentReceiptBean {

	private int agtOrgId;
	private String agtOrgName;
	private double amountCreditedBy;
	private double amountDebitedBy;
	private String generatedRectIdCancel;
	private String generatedRectIdClear;
	private int receiptIdForCancel;
	private int receiptIdForClear;
	private List<Integer> transIdCancelList;
	private List<Integer> transIdClearList;

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public double getAmountCreditedBy() {
		return amountCreditedBy;
	}

	public double getAmountDebitedBy() {
		return amountDebitedBy;
	}

	public String getGeneratedRectIdCancel() {
		return generatedRectIdCancel;
	}

	public String getGeneratedRectIdClear() {
		return generatedRectIdClear;
	}

	public int getReceiptIdForCancel() {
		return receiptIdForCancel;
	}

	public int getReceiptIdForClear() {
		return receiptIdForClear;
	}

	public List<Integer> getTransIdCancelList() {
		return transIdCancelList;
	}

	public List<Integer> getTransIdClearList() {
		return transIdClearList;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setAmountCreditedBy(double amountCreditedBy) {
		this.amountCreditedBy = amountCreditedBy;
	}

	public void setAmountDebitedBy(double amountDebitedBy) {
		this.amountDebitedBy = amountDebitedBy;
	}

	public void setGeneratedRectIdCancel(String generatedRectIdCancel) {
		this.generatedRectIdCancel = generatedRectIdCancel;
	}

	public void setGeneratedRectIdClear(String generatedRectIdClear) {
		this.generatedRectIdClear = generatedRectIdClear;
	}

	public void setReceiptIdForCancel(int receiptIdForCancel) {
		this.receiptIdForCancel = receiptIdForCancel;
	}

	public void setReceiptIdForClear(int receiptIdForClear) {
		this.receiptIdForClear = receiptIdForClear;
	}

	public void setTransIdCancelList(List<Integer> transIdCancelList) {
		this.transIdCancelList = transIdCancelList;
	}

	public void setTransIdClearList(List<Integer> transIdClearList) {
		this.transIdClearList = transIdClearList;
	}

}