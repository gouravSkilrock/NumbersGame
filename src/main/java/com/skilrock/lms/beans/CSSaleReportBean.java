package com.skilrock.lms.beans;

public class CSSaleReportBean {
	private int productNum;
	private int productId;
	private int categoryId;
	private String categoryCode;
	private String productCode;
	private String partyName;
	private String provider;
	private String denomination;
	private double mrpAmt;
	private double netAmt;
	private double buyCost;
	
	public int getProductNum() {
		return productNum;
	}
	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public double getMrpAmt() {
		return mrpAmt;
	}
	public void setMrpAmt(double mrpAmt) {
		this.mrpAmt = mrpAmt;
	}
	public double getNetAmt() {
		return netAmt;
	}
	public void setNetAmt(double netAmt) {
		this.netAmt = netAmt;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public double getBuyCost() {
		return buyCost;
	}
	public void setBuyCost(double buyCost) {
		this.buyCost = buyCost;
	}
}
