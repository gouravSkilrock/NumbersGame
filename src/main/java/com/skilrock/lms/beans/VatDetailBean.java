package com.skilrock.lms.beans;

import java.sql.Date;

public class VatDetailBean {

	private Date date;
	private String invoiceNo;
	private String name;
	private String nonTaxableGoodCause;
	private String nonTaxablePpr;
	private String taxableSale;
	private String taxSalesVatInc;
	private String totalSale;
	private String transactionType;
	private String vatAmount;

	public String getDate() {
		return date.toString();
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public String getName() {
		return name;
	}

	public String getNonTaxableGoodCause() {
		return nonTaxableGoodCause;
	}

	public String getNonTaxablePpr() {
		return nonTaxablePpr;
	}

	public String getTaxableSale() {
		return taxableSale;
	}

	public String getTaxSalesVatInc() {
		return taxSalesVatInc;
	}

	public String getTotalSale() {
		return totalSale;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String getVatAmount() {
		return vatAmount;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNonTaxableGoodCause(String nonTaxableGoodCause) {
		this.nonTaxableGoodCause = nonTaxableGoodCause;
	}

	public void setNonTaxablePpr(String nonTaxablePpr) {
		this.nonTaxablePpr = nonTaxablePpr;
	}

	public void setTaxableSale(String taxableSale) {
		this.taxableSale = taxableSale;
	}

	public void setTaxSalesVatInc(String taxSalesVatInc) {
		this.taxSalesVatInc = taxSalesVatInc;
	}

	public void setTotalSale(String totalSale) {
		this.totalSale = totalSale;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public void setVatAmount(String vatAmount) {
		this.vatAmount = vatAmount;
	}

}
