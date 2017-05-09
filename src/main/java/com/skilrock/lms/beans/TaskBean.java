package com.skilrock.lms.beans;

import java.io.Serializable;

import com.skilrock.lms.common.utility.FormatNumber;

public class TaskBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double amount;
	private String approvedate;
	private String endDate;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private String month;
	private String serviceCode;

	private String serviceName;
	// added by arun
	private String startDate;
	private String status;
	private int taskId;
	private String transactionType;
	private String submitDate;

	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	public String getAmount() {
		return FormatNumber.formatNumber(amount);
	}

	public String getApprovedate() {
		return approvedate;
	}

	public String getEndDate() {
		return endDate;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public String getMonth() {
		return month;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStatus() {
		return status;
	}

	public int getTaskId() {
		return taskId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setApprovedate(String approvedate) {
		this.approvedate = approvedate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
