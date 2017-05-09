package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class WinningTicketInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ticketNo;
	private double winAmt;
	private String purchasedByRet;
	private String isClaimed;
	private String claimedByUser;
	private String claimedAt;
	private String rpcCount;
	
	public String getClaimedAt() {
		return claimedAt;
	}
	public void setClaimedAt(String claimedAt) {
		this.claimedAt = claimedAt;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public double getWinAmt() {
		return winAmt;
	}
	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}
	public String getPurchasedByRet() {
		return purchasedByRet;
	}
	public void setPurchasedByRet(String purchasedByRet) {
		this.purchasedByRet = purchasedByRet;
	}
	public String getIsClaimed() {
		return isClaimed;
	}
	public void setIsClaimed(String isClaimed) {
		this.isClaimed = isClaimed;
	}
	public String getClaimedByUser() {
		return claimedByUser;
	}
	public void setClaimedByUser(String claimedByUser) {
		this.claimedByUser = claimedByUser;
	}
	public String getRpcCount() {
		return rpcCount;
	}
	public void setRpcCount(String rpcCount) {
		this.rpcCount = rpcCount;
	}
	
}