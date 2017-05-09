package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class DrawResultReportBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double prizeAmount;
	private int nbrOfWinners;
	private String nbrOfMatch;
	public double getPrizeAmount() {
		return prizeAmount;
	}
	public void setPrizeAmount(double prizeAmount) {
		this.prizeAmount = prizeAmount;
	}
	
	public int getNbrOfWinners() {
		return nbrOfWinners;
	}
	public void setNbrOfWinners(int nbrOfWinners) {
		this.nbrOfWinners = nbrOfWinners;
	}
	public String getNbrOfMatch() {
		return nbrOfMatch;
	}
	public void setNbrOfMatch(String nbrOfMatch) {
		this.nbrOfMatch = nbrOfMatch;
	}
}
