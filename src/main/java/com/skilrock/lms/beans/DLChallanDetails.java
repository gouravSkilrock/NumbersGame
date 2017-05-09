package com.skilrock.lms.beans;

import java.io.Serializable;

public class DLChallanDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private int challanID;
	private String dlChallanNumber;
	private String dlDate;
	
	public int getChallanID() {
		return challanID;
	}
	public void setChallanID(int challanID) {
		this.challanID = challanID;
	}
	public String getDlChallanNumber() {
		return dlChallanNumber;
	}
	public void setDlChallanNumber(String dlChallanNumber) {
		this.dlChallanNumber = dlChallanNumber;
	}
	public String getDlDate() {
		return dlDate;
	}
	public void setDlDate(String dlDate) {
		this.dlDate = dlDate;
	}
	
	
	
}
