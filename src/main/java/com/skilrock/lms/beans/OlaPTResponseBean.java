package com.skilrock.lms.beans;

import java.io.Serializable;
public class OlaPTResponseBean implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private long imsDepositTransactionId;
private long imsWithdrawalTransactionId;
private String depositStatus;
private String withdrawalStatus;
private String depositError;
private String withdrawalError;
private String status;
private String setPlayerInfoErrorCode;
private int errorCode;
private boolean isSuccess;


public int getErrorCode() {
	return errorCode;
}

public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
}

public String getSetPlayerInfoErrorCode() {
	return setPlayerInfoErrorCode;
}

public void setSetPlayerInfoErrorCode(String setPlayerInfoErrorCode) {
	this.setPlayerInfoErrorCode = setPlayerInfoErrorCode;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}



public long getImsDepositTransactionId() {
	return imsDepositTransactionId;
}

public void setImsDepositTransactionId(long imsDepositTransactionId) {
	this.imsDepositTransactionId = imsDepositTransactionId;
}

public long getImsWithdrawalTransactionId() {
	return imsWithdrawalTransactionId;
}

public void setImsWithdrawalTransactionId(long imsWithdrawalTransactionId) {
	this.imsWithdrawalTransactionId = imsWithdrawalTransactionId;
}

public String getDepositStatus() {
	return depositStatus;
}

public void setDepositStatus(String depositStatus) {
	this.depositStatus = depositStatus;
}

public String getWithdrawalStatus() {
	return withdrawalStatus;
}

public void setWithdrawalStatus(String withdrawalStatus) {
	this.withdrawalStatus = withdrawalStatus;
}

public String getDepositError() {
	return depositError;
}

public void setDepositError(String depositError) {
	this.depositError = depositError;
}

public String getWithdrawalError() {
	return withdrawalError;
}

public void setWithdrawalError(String withdrawalError) {
	this.withdrawalError = withdrawalError;
}

public void setSuccess(boolean isSuccess) {
	this.isSuccess = isSuccess;
}

public boolean isSuccess() {
	return isSuccess;
}

}
