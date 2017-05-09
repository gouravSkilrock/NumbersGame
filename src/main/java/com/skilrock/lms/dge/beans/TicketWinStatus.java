package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class TicketWinStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String claimStatus;
	private int drawId;
	private String drawStatus;
	private String drawTime;
	private double winAmount;

	public String getClaimStatus() {
		return claimStatus;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public double getWinAmount() {
		return winAmount;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public void setWinAmount(double winAmount) {
		this.winAmount = winAmount;
	}

}