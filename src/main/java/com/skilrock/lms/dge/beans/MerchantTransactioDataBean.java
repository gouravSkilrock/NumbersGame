package com.skilrock.lms.dge.beans;

public class MerchantTransactioDataBean {
	private String engineTxnStatus;
	private String merchantTxnId;
	private String txnDate;
	private String transactionAmount;

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getEngineTxnStatus() {
		return engineTxnStatus;
	}

	public void setEngineTxnStatus(String engineTxnStatus) {
		this.engineTxnStatus = engineTxnStatus;
	}

	public String getMerchantTxnId() {
		return merchantTxnId;
	}

	public void setMerchantTxnId(String merchantTxnId) {
		this.merchantTxnId = merchantTxnId;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

}
