package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class RetPwtDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double totalWinAmt;
	private double claimedAmt;
	private boolean isUnclaimedAftr7days;
	private double unclaimedAmtAftr7Days;
	private int retOrgId;
	
	
	
	public int getRetOrgId() {
		return retOrgId;
	}
	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTotalWinAmt() {
		return totalWinAmt;
	}
	public void setTotalWinAmt(double totalWinAmt) {
		this.totalWinAmt = totalWinAmt;
	}
	public double getClaimedAmt() {
		return claimedAmt;
	}
	public void setClaimedAmt(double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	public boolean isUnclaimedAftr7days() {
		return isUnclaimedAftr7days;
	}
	public void setUnclaimedAftr7days(boolean isUnclaimedAftr7days) {
		this.isUnclaimedAftr7days = isUnclaimedAftr7days;
	}
	public double getUnclaimedAmtAftr7Days() {
		return unclaimedAmtAftr7Days;
	}
	public void setUnclaimedAmtAftr7Days(double unclaimedAmtAftr7Days) {
		this.unclaimedAmtAftr7Days = unclaimedAmtAftr7Days;
	}
	
	

}
