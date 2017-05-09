package com.skilrock.lms.beans;

public class NetGamingExpenseBean {
	private int taskId;
	private int orgId;
	private String orgName;
	private double NetGamingAmt;
	private double netGamingCommissionPer;
	private double NetGamingCommissionAmt;
	private String status;
	private String crNote;
	private String updateDate;
	
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
	public double getNetGamingCommissionAmt() {
		return NetGamingCommissionAmt;
	}
	public void setNetGamingCommissionAmt(double netGamingCommissionAmt) {
		NetGamingCommissionAmt = netGamingCommissionAmt;
	}
	public double getNetGamingAmt() {
		return NetGamingAmt;
	}
	public void setNetGamingAmt(double netGamingAmt) {
		NetGamingAmt = netGamingAmt;
	}
	public double getNetGamingCommissionPer() {
		return netGamingCommissionPer;
	}
	public void setNetGamingCommissionPer(double netGamingCommissionPer) {
		this.netGamingCommissionPer = netGamingCommissionPer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCrNote() {
		return crNote;
	}
	public void setCrNote(String crNote) {
		this.crNote = crNote;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

}
