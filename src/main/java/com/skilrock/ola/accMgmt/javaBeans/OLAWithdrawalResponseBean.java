package com.skilrock.ola.accMgmt.javaBeans;

import java.sql.Timestamp;

public class OLAWithdrawalResponseBean {
	
	private boolean isSuccess;
	private long txnId;
	private Timestamp txnDate;
	private String errorMsg;
	private String isBinding;
	
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
	public String getIsBinding() {
		return isBinding;
	}
	public void setIsBinding(String isBinding) {
		this.isBinding = isBinding;
	}
}
