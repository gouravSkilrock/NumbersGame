package com.skilrock.lms.api.ola.beans;

import java.io.Serializable;
import java.sql.Date;

public class OlaWithdrwalDetailsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String plrId;
	private String plrEmail;
	private double plrAmount;
	private String requestId;
	private String walletName;
	private String plrPhoneNbr;
	private Date   requestDate;
	private Date   approveDate;
	private String userName;
	private boolean isSuccess;
	private int errorCode ;
	private String errorMsg;
	public String getPlrId() {
		return plrId;
	}
	public String getPlrEmail() {
		return plrEmail;
	}
	public double getPlrAmount() {
		return plrAmount;
	}
	public String getRequestId() {
		return requestId;
	}
	public String getWalletName() {
		return walletName;
	}
	public String getPlrPhoneNbr() {
		return plrPhoneNbr;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	
	public void setPlrId(String plrId) {
		this.plrId = plrId;
	}
	public void setPlrEmail(String plrEmail) {
		this.plrEmail = plrEmail;
	}
	public void setPlrAmount(double plrAmount) {
		this.plrAmount = plrAmount;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
	public void setPlrPhoneNbr(String plrPhoneNbr) {
		this.plrPhoneNbr = plrPhoneNbr;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	
	public String getUserName() {
		return userName;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


}
