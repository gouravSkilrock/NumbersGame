package com.skilrock.lms.keba.drawGames.javaBeans;

import java.io.Serializable;

public class PanelResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int noOfLines;
	private double unitPrice;
	private int panelAmt;
	private String pickedNumbers;
	private int noPicked;
	private String playType;
	private boolean isQp;
	private int betAmtMultiple;
	
	public int getNoOfLines() {
		return noOfLines;
	}
	
	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public int getPanelAmt() {
		return panelAmt;
	}
	
	public void setPanelAmt(int panelAmt) {
		this.panelAmt = panelAmt;
	}
	
	public String getPickedNumbers() {
		return pickedNumbers;
	}
	
	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}
	
	public int getNoPicked() {
		return noPicked;
	}
	
	public void setNoPicked(int noPicked) {
		this.noPicked = noPicked;
	}
	
	public String getPlayType() {
		return playType;
	}
	
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	
	public boolean isQp() {
		return isQp;
	}
	
	public void setQp(boolean isQp) {
		this.isQp = isQp;
	}
	
	public int getBetAmtMultiple() {
		return betAmtMultiple;
	}
	
	public void setBetAmtMultiple(int betAmtMultiple) {
		this.betAmtMultiple = betAmtMultiple;
	}
	
}