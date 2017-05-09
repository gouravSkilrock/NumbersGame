package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;

public class DrawTicketDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int drawId;
	private long taskId;
	private long transactionId;
	private double taxAmt;
	private double netAmt;
	private double winningAmt;

	public DrawTicketDataBean() {
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public double getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(double netAmt) {
		this.netAmt = netAmt;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}
}