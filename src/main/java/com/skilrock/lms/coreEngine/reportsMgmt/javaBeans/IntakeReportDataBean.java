package com.skilrock.lms.coreEngine.reportsMgmt.javaBeans;

import java.util.Date;

public class IntakeReportDataBean {
	private String agtOrgCode;
	private double pwtAmount;
	private String transDate;
	private String retOrgCode;
	private String city;
	private String areaName;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAgtOrgCode() {
		return agtOrgCode;
	}
	public void setAgtOrgCode(String agtOrgCode) {
		this.agtOrgCode = agtOrgCode;
	}
	public double getPwtAmount() {
		return pwtAmount;
	}
	public void setPwtAmount(double pwtAmount) {
		this.pwtAmount = pwtAmount;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getRetOrgCode() {
		return retOrgCode;
	}
	public void setRetOrgCode(String retOrgCode) {
		this.retOrgCode = retOrgCode;
	}
}