package com.skilrock.cs.beans;

public class CamlotRefundBean {
	private CamlotSOAPHeaderBean header;
	private String productId;
	private String originalClientRequestId;
	private String originalTransactionID;
	private double amount;
	private CamlotFaultBean fault;
	
	public CamlotSOAPHeaderBean getHeader() {
		return header;
	}
	public void setHeader(CamlotSOAPHeaderBean header) {
		this.header = header;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getOriginalClientRequestId() {
		return originalClientRequestId;
	}
	public void setOriginalClientRequestId(String originalClientRequestId) {
		this.originalClientRequestId = originalClientRequestId;
	}
	public String getOriginalTransactionID() {
		return originalTransactionID;
	}
	public void setOriginalTransactionID(String originalTransactionID) {
		this.originalTransactionID = originalTransactionID;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public CamlotFaultBean getFault() {
		return fault;
	}
	public void setFault(CamlotFaultBean fault) {
		this.fault = fault;
	}
}
