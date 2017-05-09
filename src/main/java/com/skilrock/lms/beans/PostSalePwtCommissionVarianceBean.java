package com.skilrock.lms.beans;

import java.io.Serializable;

public class PostSalePwtCommissionVarianceBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orgId;
	private String orgName;
	private double defaultCommVar;
	private double commVar;
	private double totalCommVar;
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public double getDefaultCommVar() {
		return defaultCommVar;
	}
	public void setDefaultCommVar(double defaultCommVar) {
		this.defaultCommVar = defaultCommVar;
	}
	public double getCommVar() {
		return commVar;
	}
	public void setCommVar(double commVar) {
		this.commVar = commVar;
	}
	public double getTotalCommVar() {
		return totalCommVar;
	}
	public void setTotalCommVar(double totalCommVar) {
		this.totalCommVar = totalCommVar;
	}
	@Override
	public String toString() {
		return "PostSalePwtCommissionVarianceBean [commVar=" + commVar
				+ ", defaultCommVar=" + defaultCommVar + ", orgId=" + orgId
				+ ", orgName=" + orgName + ", totalCommVar=" + totalCommVar
				+ "]";
	}
	
	
	
}
