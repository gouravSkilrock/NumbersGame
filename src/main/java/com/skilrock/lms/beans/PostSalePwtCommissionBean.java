package com.skilrock.lms.beans;

public class PostSalePwtCommissionBean {
	
	private String date;
	private double depAmount;
	private double depCommRate;
	private double commAmount;
	private double taxCharges;
	private double charges_1;
	private double charges_2;
	private double netAmount;
	private String status;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getDepAmount() {
		return depAmount;
	}
	public void setDepAmount(double depAmount) {
		this.depAmount = depAmount;
	}
	public double getDepCommRate() {
		return depCommRate;
	}
	public void setDepCommRate(double depCommRate) {
		this.depCommRate = depCommRate;
	}
	
	public double getCommAmount() {
		return commAmount;
	}
	public void setCommAmount(double commAmount) {
		this.commAmount = commAmount;
	}
	public double getTaxCharges() {
		return taxCharges;
	}
	public void setTaxCharges(double taxCharges) {
		this.taxCharges = taxCharges;
	}
	public double getCharges_1() {
		return charges_1;
	}
	public void setCharges_1(double charges_1) {
		this.charges_1 = charges_1;
	}
	public double getCharges_2() {
		return charges_2;
	}
	public void setCharges_2(double charges_2) {
		this.charges_2 = charges_2;
	}
	public double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "PostSalePwtCommissionBean [commAmount=" + commAmount
				+ ", charges_1=" + charges_1 + ", charges_2=" + charges_2
				+ ", date=" + date + ", depAmount=" + depAmount
				+ ", depCommRate=" + depCommRate + ", netAmount=" + netAmount
				+ ", status=" + status + ", taxCharges=" + taxCharges + "]";
	}
	
	
	
	

}
