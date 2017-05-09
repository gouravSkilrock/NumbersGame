package com.skilrock.lms.rest.services.bean;

import org.apache.commons.collections.Predicate;

public class TPReconciliationBean implements Predicate {

	private String engineTxnId;
	private String engineSaleTxnId;
	private String merchantTxnId;
	private long ticktNo;
	private String status;

	public String getEngineTxnId() {
		return engineTxnId;
	}

	public void setEngineTxnId(String engineTxnId) {
		this.engineTxnId = engineTxnId;
	}

	public String getEngineSaleTxnId() {
		return engineSaleTxnId;
	}

	public void setEngineSaleTxnId(String engineSaleTxnId) {
		this.engineSaleTxnId = engineSaleTxnId;
	}

	public String getMerchantTxnId() {
		return merchantTxnId;
	}

	public void setMerchantTxnId(String merchantTxnId) {
		this.merchantTxnId = merchantTxnId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTicktNo() {
		return ticktNo;
	}

	public void setTicktNo(long ticktNo) {
		this.ticktNo = ticktNo;
	}

	@Override
	public boolean evaluate(Object obj) {
		TPReconciliationBean tpReconciliationBean = (TPReconciliationBean)obj;
		return this.engineSaleTxnId.equals(tpReconciliationBean.getEngineSaleTxnId());
	}

	@Override
	public String toString() {
		return "TPReconciliationBean [engineTxnId=" + engineTxnId
				+ ", engineSaleTxnId=" + engineSaleTxnId + ", merchantTxnId="
				+ merchantTxnId + ", ticktNo=" + ticktNo + ", status=" + status
				+ "]";
	}

	

}
