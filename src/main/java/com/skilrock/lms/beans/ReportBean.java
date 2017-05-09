package com.skilrock.lms.beans;

import java.util.List;
import java.util.Map;

public class ReportBean {

	private List<CashChqPmntBean> paymentList;
	private List<PWTPaymentsBean> pwtList;
	private String reconReportType;
	private List<SalePurchaseBean> saleList;
	private List<SalePurchaseRetBean> saleTktList;
	private List<StockMrpDiscountBean> stockMrpDiscList;
	private Map<String, StockMrpDiscountBean> stockMrpDiscMap;
	private Map<String, StockMrpDiscountBean> stockMrpDiscRetMap;

	public List<CashChqPmntBean> getPaymentList() {
		return paymentList;
	}

	public List<PWTPaymentsBean> getPwtList() {
		return pwtList;
	}

	public String getReconReportType() {
		return reconReportType;
	}

	public List<SalePurchaseBean> getSaleList() {
		return saleList;
	}

	public List<SalePurchaseRetBean> getSaleTktList() {
		return saleTktList;
	}

	public List<StockMrpDiscountBean> getStockMrpDiscList() {
		return stockMrpDiscList;
	}

	public Map<String, StockMrpDiscountBean> getStockMrpDiscMap() {
		return stockMrpDiscMap;
	}

	public Map<String, StockMrpDiscountBean> getStockMrpDiscRetMap() {
		return stockMrpDiscRetMap;
	}

	public void setPaymentList(List<CashChqPmntBean> paymentList) {
		this.paymentList = paymentList;
	}

	public void setPwtList(List<PWTPaymentsBean> pwtList) {
		this.pwtList = pwtList;
	}

	public void setReconReportType(String reconReportType) {
		this.reconReportType = reconReportType;
	}

	public void setSaleList(List<SalePurchaseBean> saleList) {
		this.saleList = saleList;
	}

	public void setSaleTktList(List<SalePurchaseRetBean> saleTktList) {
		this.saleTktList = saleTktList;
	}

	public void setStockMrpDiscList(List<StockMrpDiscountBean> stockMrpDiscList) {
		this.stockMrpDiscList = stockMrpDiscList;
	}

	public void setStockMrpDiscMap(
			Map<String, StockMrpDiscountBean> stockMrpDiscMap) {
		this.stockMrpDiscMap = stockMrpDiscMap;
	}

	public void setStockMrpDiscRetMap(
			Map<String, StockMrpDiscountBean> stockMrpDiscRetMap) {
		this.stockMrpDiscRetMap = stockMrpDiscRetMap;
	}

}
