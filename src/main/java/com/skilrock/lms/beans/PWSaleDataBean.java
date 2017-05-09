package com.skilrock.lms.beans;

import java.util.Date;
import java.util.List;

public class PWSaleDataBean {
	 private String RMStransId;
	 private String PWtransId;
	 private int prodId;
	 private int categoryId;
	 private Date transDateTime;
	 private String retailerId;
	 private double orgBalance;
	 private String operatorCode;
	 private String cirCode;
	 private String product;
	 private double denomination;
	 private int bulkQty;
	 private String narration;
	 private List<PWSaleRespBean> respDataList;
	
	public String getRMStransId() {
		return RMStransId;
	}
	public void setRMStransId(String rMStransId) {
		RMStransId = rMStransId;
	}
	public String getPWtransId() {
		return PWtransId;
	}
	public double getOrgBalance() {
		return orgBalance;
	}
	public void setOrgBalance(double orgBalance) {
		this.orgBalance = orgBalance;
	}
	public void setPWtransId(String pWtransId) {
		PWtransId = pWtransId;
	}
	public String getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(String retailerId) {
		this.retailerId = retailerId;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getCirCode() {
		return cirCode;
	}
	public void setCirCode(String cirCode) {
		this.cirCode = cirCode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public double getDenomination() {
		return denomination;
	}
	public void setDenomination(double denomination) {
		this.denomination = denomination;
	}
	public int getBulkQty() {
		return bulkQty;
	}
	public void setBulkQty(int bulkQty) {
		this.bulkQty = bulkQty;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public List<PWSaleRespBean> getRespDataList() {
		return respDataList;
	}
	public void setRespDataList(List<PWSaleRespBean> respDataList) {
		this.respDataList = respDataList;
	}
	public Date getTransDateTime() {
		return transDateTime;
	}
	public void setTransDateTime(Date transDateTime) {
		this.transDateTime = transDateTime;
	}
	public int getProdId() {
		return prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
}
