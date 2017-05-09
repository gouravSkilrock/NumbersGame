package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class KenoTicketBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String noPicked;
	private int betAmtMul;
	private boolean isQp;
	private String pickedNumbers;
	private String betName;
	private boolean QPPreGenerated;
	public String getNoPicked() {
		return noPicked;
	}
	public void setNoPicked(String noPicked) {
		this.noPicked = noPicked;
	}
	public int getBetAmtMul() {
		return betAmtMul;
	}
	public void setBetAmtMul(int betAmtMul) {
		this.betAmtMul = betAmtMul;
	}
	public boolean isQp() {
		return isQp;
	}
	public void setQp(boolean isQp) {
		this.isQp = isQp;
	}
	public String getPickedNumbers() {
		return pickedNumbers;
	}
	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}
	public String getBetName() {
		return betName;
	}
	public void setBetName(String betName) {
		this.betName = betName;
	}
	public boolean isQPPreGenerated() {
		return QPPreGenerated;
	}
	public void setQPPreGenerated(boolean qPPreGenerated) {
		QPPreGenerated = qPPreGenerated;
	}
	
	
}