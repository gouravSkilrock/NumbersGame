package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;


public class LmsWrapperPwtApiBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private double totalTicketPwtAmount;
	
	private String message;
	private boolean isSuccess;
	private String errorCode;
	private LmsWrapperMainPWTDrawBean mainPwtDrawBean;
    private String recId;
    private String gameName;
    private String isAnonymous;
    private double netAmountPaid;
	private String status;


	
	
	
	public double getTotalTicketPwtAmount() {
		return totalTicketPwtAmount;
	}
	public void setTotalTicketPwtAmount(double totalTicketPwtAmount) {
		this.totalTicketPwtAmount = totalTicketPwtAmount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public LmsWrapperMainPWTDrawBean getMainPwtDrawBean() {
		return mainPwtDrawBean;
	}
	public void setMainPwtDrawBean(LmsWrapperMainPWTDrawBean mainPwtDrawBean) {
		this.mainPwtDrawBean = mainPwtDrawBean;
	}
	public String getRecId() {
		return recId;
	}
	public void setRecId(String recId) {
		this.recId = recId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public String getIsAnonymous() {
		return isAnonymous;
	}
	public void setIsAnonymous(String isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getNetAmountPaid() {
		return netAmountPaid;
	}
	public void setNetAmountPaid(double netAmountPaid) {
		this.netAmountPaid = netAmountPaid;
	}
	
	
	
	

}
