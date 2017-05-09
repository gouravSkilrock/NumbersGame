package com.skilrock.lms.rest.scratchService.pwtMgmt.beans;

public class ScratchWinningDataBean {
	private double winningAmount;
	private String status;

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ScratchWinningDataBean [winningAmount=" + winningAmount + ", status=" + status + "]";
	}

}
