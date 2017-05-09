package com.skilrock.lms.beans;

import java.util.Date;

public class OLAPlayerCommissionBean {
  private int retOrgId;
  private String refUserId;
  private String plrId;
  private double plrNetGaming;
  private double retCommissionAmt;
  private double retPlrCommRate;
  private Date commDate;
public int getRetOrgId() {
	return retOrgId;
}
public void setRetOrgId(int retOrgId) {
	this.retOrgId = retOrgId;
}
public String getRefUserId() {
	return refUserId;
}
public void setRefUserId(String refUserId) {
	this.refUserId = refUserId;
}
public String getPlrId() {
	return plrId;
}
public void setPlrId(String plrId) {
	this.plrId = plrId;
}
public double getPlrNetGaming() {
	return plrNetGaming;
}
public void setPlrNetGaming(double plrNetGaming) {
	this.plrNetGaming = plrNetGaming;
}
public double getRetCommissionAmt() {
	return retCommissionAmt;
}
public void setRetCommissionAmt(double retCommissionAmt) {
	this.retCommissionAmt = retCommissionAmt;
}
public double getRetPlrCommRate() {
	return retPlrCommRate;
}
public void setRetPlrCommRate(double retPlrCommRate) {
	this.retPlrCommRate = retPlrCommRate;
}
public Date getCommDate() {
	return commDate;
}
public void setCommDate(Date commDate) {
	this.commDate = commDate;
}
  
  
}
