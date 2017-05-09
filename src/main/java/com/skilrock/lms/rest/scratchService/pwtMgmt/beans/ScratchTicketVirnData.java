package com.skilrock.lms.rest.scratchService.pwtMgmt.beans;

public class ScratchTicketVirnData {
	private String vCode;
	private String pwtAmount;
	private String prizeLevel;
	private String prizeStatus;
	
	public String getvCode() {
		return vCode;
	}
	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	public String getPwtAmount() {
		return pwtAmount;
	}
	public void setPwtAmount(String pwtAmount) {
		this.pwtAmount = pwtAmount;
	}
	public String getPrizeLevel() {
		return prizeLevel;
	}
	public void setPrizeLevel(String prizeLevel) {
		this.prizeLevel = prizeLevel;
	}
	public String getPrizeStatus() {
		return prizeStatus;
	}
	public void setPrizeStatus(String prizeStatus) {
		this.prizeStatus = prizeStatus;
	}
	@Override
	public String toString() {
		return "ScratchTicketVirnData [vCode=" + vCode + ", pwtAmount=" + pwtAmount + ", prizeLevel=" + prizeLevel
				+ ", prizeStatus=" + prizeStatus + "]";
	}
	
	

}
