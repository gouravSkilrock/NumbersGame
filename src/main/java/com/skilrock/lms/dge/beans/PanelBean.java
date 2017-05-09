package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class PanelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String noPicked;
	private String pickedNumbers;
	private String playType;
	private boolean qp;
	private int betAmtMul;
	private int noOfLines;
	private double unitPrice;
	public String getNoPicked() {
		return noPicked;
	}
	public String getPickedNumbers() {
		return pickedNumbers;
	}
	public String getPlayType() {
		return playType;
	}
	public boolean getQp() {
		return qp;
	}
	public int getBetAmtMul() {
		return betAmtMul;
	}
	public int getNoOfLines() {
		return noOfLines;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setNoPicked(String noPicked) {
		this.noPicked = noPicked;
	}
	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public void setQp(boolean qp) {
		this.qp = qp;
	}
	public void setBetAmtMul(int betAmtMul) {
		this.betAmtMul = betAmtMul;
	}
	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
	
}
