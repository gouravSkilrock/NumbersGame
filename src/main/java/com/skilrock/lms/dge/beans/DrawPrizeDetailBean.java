package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class DrawPrizeDetailBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private int prizeRank;
	private double prizeAmt;
	private int nbrWinners;

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getPrizeRank() {
		return prizeRank;
	}

	public void setPrizeRank(int prizeRank) {
		this.prizeRank = prizeRank;
	}

	public double getPrizeAmt() {
		return prizeAmt;
	}

	public void setPrizeAmt(double prizeAmt) {
		this.prizeAmt = prizeAmt;
	}

	public int getNbrWinners() {
		return nbrWinners;
	}

	public void setNbrWinners(int nbrWinners) {
		this.nbrWinners = nbrWinners;
	}

	@Override
	public String toString() {
		return "DrawPrizeDetailBean [drawId=" + drawId + ", nbrWinners="
				+ nbrWinners + ", prizeAmt=" + prizeAmt + ", prizeRank="
				+ prizeRank + "]";
	}
}
