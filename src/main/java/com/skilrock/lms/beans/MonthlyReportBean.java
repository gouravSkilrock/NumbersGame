package com.skilrock.lms.beans;

import java.sql.Date;

public class MonthlyReportBean {
	private int month;
	private double mrpAmount;
	private double netAmount;
	private Date paidDate;
	private int paidUserId;
	private String status;

	public MonthlyReportBean() {
	}

	public MonthlyReportBean(int month, int netAmount, Date paidDate,
			int paidUserId, String status) {
		this.month = month;
		this.netAmount = netAmount;
		this.paidDate = paidDate;
		this.paidUserId = paidUserId;
		this.status = status;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public double getMrpAmount() {
		return mrpAmount;
	}

	public void setMrpAmount(double mrpAmount) {
		this.mrpAmount = mrpAmount;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public int getPaidUserId() {
		return paidUserId;
	}

	public void setPaidUserId(int paidUserId) {
		this.paidUserId = paidUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MonthlyReportBean [month=" + month + ", mrpAmount=" + mrpAmount
				+ ", netAmount=" + netAmount + ", paidDate=" + paidDate
				+ ", paidUserId=" + paidUserId + ", status=" + status + "]";
	}
}