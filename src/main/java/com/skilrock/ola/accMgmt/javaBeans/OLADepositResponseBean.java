package com.skilrock.ola.accMgmt.javaBeans;

import java.sql.Timestamp;

public class OLADepositResponseBean {

	private boolean isSuccess;
	private long txnId;
	private Timestamp txnDate;
	private String errorMsg;
	private boolean isBinding;
	private String refTxnId;
	private int reponseCode;

	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public long getTxnId() {
		return txnId;
	}
	public void setTxnId(long txnId) {
		this.txnId = txnId;
	}
	public Timestamp getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Timestamp txnDate) {
		this.txnDate = txnDate;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public void setRefTxnId(String refTxnId) {
		this.refTxnId = refTxnId;
	}
	public String getRefTxnId() {
		return refTxnId;
	}
	public void setBinding(boolean isBinding) {
		this.isBinding = isBinding;
	}
	public boolean isBinding() {
		return isBinding;
	}
	public int getReponseCode() {
		return reponseCode;
	}
	public void setReponseCode(int reponseCode) {
		this.reponseCode = reponseCode;
	}
	
}
