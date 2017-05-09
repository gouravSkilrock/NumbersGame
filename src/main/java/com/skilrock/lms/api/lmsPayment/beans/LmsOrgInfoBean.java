package com.skilrock.lms.api.lmsPayment.beans;

import java.io.Serializable;

public class LmsOrgInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String retOrgName;
	private String agtOrgName;
	private String retOrgCode;
	private String agtOrgCode;
	private double retCurrentBal;
	private String retUserName;
	private boolean isSuccess;
	private String errorCode;
	public String getRetOrgName() {
		return retOrgName;
	}
	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}
	public String getAgtOrgName() {
		return agtOrgName;
	}
	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}
	public String getRetOrgCode() {
		return retOrgCode;
	}
	public void setRetOrgCode(String retOrgCode) {
		this.retOrgCode = retOrgCode;
	}
	public String getAgtOrgCode() {
		return agtOrgCode;
	}
	public void setAgtOrgCode(String agtOrgCode) {
		this.agtOrgCode = agtOrgCode;
	}
	public double getRetCurrentBal() {
		return retCurrentBal;
	}
	public void setRetCurrentBal(double retCurrentBal) {
		this.retCurrentBal = retCurrentBal;
	}
	public String getRetUserName() {
		return retUserName;
	}
	public void setRetUserName(String retUserName) {
		this.retUserName = retUserName;
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
	@Override
	public String toString() {
		return "LmsOrgInfoBean [agtOrgCode=" + agtOrgCode + ", agtOrgName="
				+ agtOrgName + ", errorCode=" + errorCode + ", isSuccess="
				+ isSuccess + ", retCurrentBal=" + retCurrentBal
				+ ", retOrgCode=" + retOrgCode + ", retOrgName=" + retOrgName
				+ ", retUserName=" + retUserName + "]";
	}
	
	
}
