package com.skilrock.lms.rest.scratchService.orderMgmt.controller;

import com.google.gson.annotations.SerializedName;

public class OrderHistory {

	@SerializedName("orderReferenceId")
    private int orderReferenceId;
	
	@SerializedName("gameName")
	private String gameName;
	
	@SerializedName("orderStatus")
	private String orderStatus;
	
	@SerializedName("noOfBookOrdered")
	private int noOfBookOrdered;
	
	@SerializedName("noOfBookApproved")
	private int noOfBookApproved;
	
	@SerializedName("noOfBookDispatched")
	private int noOfBookDispatched;

	public int getOrderReferenceId() {
		return orderReferenceId;
	}

	public void setOrderReferenceId(int orderReferenceId) {
		this.orderReferenceId = orderReferenceId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getNoOfBookOrdered() {
		return noOfBookOrdered;
	}

	public void setNoOfBookOrdered(int noOfBookOrdered) {
		this.noOfBookOrdered = noOfBookOrdered;
	}

	public int getNoOfBookApproved() {
		return noOfBookApproved;
	}

	public void setNoOfBookApproved(int noOfBookApproved) {
		this.noOfBookApproved = noOfBookApproved;
	}

	public int getNoOfBookDispatched() {
		return noOfBookDispatched;
	}

	public void setNoOfBookDispatched(int noOfBookDispatched) {
		this.noOfBookDispatched = noOfBookDispatched;
	}
	
	

}
