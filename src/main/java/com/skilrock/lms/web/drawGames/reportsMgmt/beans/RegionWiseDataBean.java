package com.skilrock.lms.web.drawGames.reportsMgmt.beans;

public class RegionWiseDataBean {
private String stateName;
private String stateCode;
private String cityName;
private String cityCode;
private String areaName;
private String areaCode;
private int  	orgId;
private double pwtAmt;
private double saleAmt;
private double ppratio;
private double totalCashAmt;
private String startDate;
private String endDate;
private int gameId;
private String orgName;
private String parentOrgName;
public String getStateName() {
	return stateName;
}
public void setStateName(String stateName) {
	this.stateName = stateName;
}
public String getStateCode() {
	return stateCode;
}
public void setStateCode(String stateCode) {
	this.stateCode = stateCode;
}
public double getPwtAmt() {
	return pwtAmt;
}
public void setPwtAmt(double pwtAmt) {
	this.pwtAmt = pwtAmt;
}
public double getSaleAmt() {
	return saleAmt;
}
public void setSaleAmt(double saleAmt) {
	this.saleAmt = saleAmt;
}
public double getPpratio() {
	return ppratio;
}
public void setPpratio(double ppratio) {
	this.ppratio = ppratio;
}
public double getTotalCashAmt() {
	return totalCashAmt;
}
public void setTotalCashAmt(double totalCashAmt) {
	this.totalCashAmt = totalCashAmt;
}
public String getCityName() {
	return cityName;
}
public String getAreaName() {
	return areaName;
}
public void setCityName(String cityName) {
	this.cityName = cityName;
}
public void setAreaName(String areaName) {
	this.areaName = areaName;
}
public String getCityCode() {
	return cityCode;
}
public String getAreaCode() {
	return areaCode;
}
public int getOrgId() {
	return orgId;
}
public void setCityCode(String cityCode) {
	this.cityCode = cityCode;
}
public void setAreaCode(String areaCode) {
	this.areaCode = areaCode;
}
public void setOrgId(int orgId) {
	this.orgId = orgId;
}
public String getStartDate() {
	return startDate;
}
public String getEndDate() {
	return endDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public void setEndDate(String endDate) {
	this.endDate = endDate;
}
public int getGameId() {
	return gameId;
}
public void setGameId(int gameId) {
	this.gameId = gameId;
}
public String getOrgName() {
	return orgName;
}
public String getParentOrgName() {
	return parentOrgName;
}
public void setOrgName(String orgName) {
	this.orgName = orgName;
}
public void setParentOrgName(String parentOrgName) {
	this.parentOrgName = parentOrgName;
}



}
