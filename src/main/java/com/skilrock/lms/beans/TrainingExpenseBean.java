package com.skilrock.lms.beans;

public class TrainingExpenseBean {
	private int taskId;
	private int orgId;
	private String orgName;
	private double saleAmt;
	private double trainingPer;
	private double trainingPerVariance;
	private double trainingAmt;
	private double timeSlottedMrpSale;
	private double extraTrainingPer;
	private double extraTrainingPerVariance;
	private double extraTrainingAmt;
	private String status;
	private String crNote;
	private String updateDate;
	private double incentiveMrp;
	private double incentivePer;
	private double incentivePerVariance;
	private double incentiveAmt;
	private String incentiveCrNote;
	
	
	
	public double getIncentiveMrp() {
		return incentiveMrp;
	}
	public void setIncentiveMrp(double incentiveMrp) {
		this.incentiveMrp = incentiveMrp;
	}
	public double getTrainingPerVariance() {
		return trainingPerVariance;
	}
	public void setTrainingPerVariance(double trainingPerVariance) {
		this.trainingPerVariance = trainingPerVariance;
	}
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
	public double getSaleAmt() {
		return saleAmt;
	}
	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}
	public double getTrainingPer() {
		return trainingPer;
	}
	public void setTrainingPer(double trainingPer) {
		this.trainingPer = trainingPer;
	}
	public double getTrainingAmt() {
		return trainingAmt;
	}
	public void setTrainingAmt(double trainingAmt) {
		this.trainingAmt = trainingAmt;
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
	public double getExtraTrainingPer() {
		return extraTrainingPer;
	}
	public void setExtraTrainingPer(double extraTrainingPer) {
		this.extraTrainingPer = extraTrainingPer;
	}
	public double getExtraTrainingPerVariance() {
		return extraTrainingPerVariance;
	}
	public void setExtraTrainingPerVariance(double extraTrainingPerVariance) {
		this.extraTrainingPerVariance = extraTrainingPerVariance;
	}
	public double getExtraTrainingAmt() {
		return extraTrainingAmt;
	}
	public void setExtraTrainingAmt(double extraTrainingAmt) {
		this.extraTrainingAmt = extraTrainingAmt;
	}
	public double getTimeSlottedMrpSale() {
		return timeSlottedMrpSale;
	}
	public void setTimeSlottedMrpSale(double timeSlottedMrpSale) {
		this.timeSlottedMrpSale = timeSlottedMrpSale;
	}
	public double getIncentivePer() {
		return incentivePer;
	}
	public void setIncentivePer(double incentivePer) {
		this.incentivePer = incentivePer;
	}
	public double getIncentivePerVariance() {
		return incentivePerVariance;
	}
	public void setIncentivePerVariance(double incentivePerVariance) {
		this.incentivePerVariance = incentivePerVariance;
	}
	public double getIncentiveAmt() {
		return incentiveAmt;
	}
	public void setIncentiveAmt(double incentiveAmt) {
		this.incentiveAmt = incentiveAmt;
	}
	public String getIncentiveCrNote() {
		return incentiveCrNote;
	}
	public void setIncentiveCrNote(String incentiveCrNote) {
		this.incentiveCrNote = incentiveCrNote;
	}

}
