package com.skilrock.cs.beans;

import java.math.BigInteger;

import com.cssl.ctp.il.xsd.infra_v1.EntryMethodType;
import com.cssl.ctp.il.xsd.infra_v1.PaymentMethodType;
import com.cssl.ctp.il.xsd.infra_v1.TransactionTypeType;

public class CamlotSOAPHeaderBean {
	private String uniqueID;
	private String messageTypeID;
	private String CTPOutletID;
	private String retailerStoreID;
	private String timeStamp;
	private String clientRequestID;
	private TransactionTypeType TransactionType;
	private String messageSequenceID;
	private String originalTransactionID;
	private PaymentMethodType paymentMethod;
	private EntryMethodType EntryMethod;
	private String locale;
	
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	public String getMessageTypeID() {
		return messageTypeID;
	}
	public void setMessageTypeID(String messageTypeID) {
		this.messageTypeID = messageTypeID;
	}
	public String getCTPOutletID() {
		return CTPOutletID;
	}
	public void setCTPOutletID(String cTPOutletID) {
		CTPOutletID = cTPOutletID;
	}
	public String getRetailerStoreID() {
		return retailerStoreID;
	}
	public void setRetailerStoreID(String retailerStoreID) {
		this.retailerStoreID = retailerStoreID;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getClientRequestID() {
		return clientRequestID;
	}
	public void setClientRequestID(String clientRequestID) {
		this.clientRequestID = clientRequestID;
	}
	public TransactionTypeType getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(TransactionTypeType transactionType) {
		TransactionType = transactionType;
	}
	public String getMessageSequenceID() {
		return messageSequenceID;
	}
	public void setMessageSequenceID(String messageSequenceID) {
		this.messageSequenceID = messageSequenceID;
	}
	public String getOriginalTransactionID() {
		return originalTransactionID;
	}
	public void setOriginalTransactionID(String originalTransactionID) {
		this.originalTransactionID = originalTransactionID;
	}
	public PaymentMethodType getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethodType paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public EntryMethodType getEntryMethod() {
		return EntryMethod;
	}
	public void setEntryMethod(EntryMethodType entryMethod) {
		EntryMethod = entryMethod;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
