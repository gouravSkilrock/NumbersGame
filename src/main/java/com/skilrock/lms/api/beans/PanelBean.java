package com.skilrock.lms.api.beans;

import java.io.Serializable;

public class PanelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String noPicked;
	private String pickedNumbers;
	private String playType;
	private String isQp;
	private String betAmountMultiple;
	private String message;
	private String noOfPanels;
	private String noOfLines;
	private String unitPrice;
	public String getNoPicked() {
		return noPicked;
	}
	public String getPickedNumbers() {
		return pickedNumbers;
	}
	public String getPlayType() {
		return playType;
	}
	public String getIsQp() {
		return isQp;
	}
	public String getBetAmountMultiple() {
		return betAmountMultiple;
	}
	public String getMessage() {
		return message;
	}
	public String getNoOfPanels() {
		return noOfPanels;
	}
	public String getNoOfLines() {
		return noOfLines;
	}
	public String getUnitPrice() {
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
	public void setIsQp(String isQp) {
		this.isQp = isQp;
	}
	public void setBetAmountMultiple(String betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setNoOfPanels(String noOfPanels) {
		this.noOfPanels = noOfPanels;
	}
	public void setNoOfLines(String noOfLines) {
		this.noOfLines = noOfLines;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
}
