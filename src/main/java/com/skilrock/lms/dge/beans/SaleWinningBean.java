package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class SaleWinningBean implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double saleAmt;
	private double winAmt;
	
	public double getSaleAmt() {
		return saleAmt;
	}
	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}
	public double getWinAmt() {
		return winAmt;
	}
	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}
}
