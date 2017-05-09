package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class LocationSaleBean implements Serializable{
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalTicketSold;
	private List<Integer> retOrgList;
	private int noOfPrizeToDistribute;
	public int getTotalTicketSold() {
		return totalTicketSold;
	}
	public void setTotalTicketSold(int totalTicketSold) {
		this.totalTicketSold = totalTicketSold;
	}
	public List<Integer> getRetOrgList() {
		return retOrgList;
	}
	public void setRetOrgList(List<Integer> retOrgList) {
		this.retOrgList = retOrgList;
	}
	public int getNoOfPrizeToDistribute() {
		return noOfPrizeToDistribute;
	}
	public void setNoOfPrizeToDistribute(int noOfPrizeToDistribute) {
		this.noOfPrizeToDistribute = noOfPrizeToDistribute;
	}
	
	
}
