package com.skilrock.lms.api.ola.beans;

import java.io.Serializable;

public class OlaRummyDepositBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;	
	private String playerId;
	private double depositeAmount;
	private boolean isValidDeposit;
	private int errorCode;
	private String refTransId;
	private String depositType;
	private String olaPIN;
	private Long olaTranxId;
	private String serialNumber ;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public double getDepositeAmount() {
		return depositeAmount;
	}
	public void setDepositeAmount(double depositeAmount) {
		this.depositeAmount = depositeAmount;
	}
	public boolean isValidDeposit() {
		return isValidDeposit;
	}
	public void setValidDeposit(boolean isValidDeposit) {
		this.isValidDeposit = isValidDeposit;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getRefTransId() {
		return refTransId;
	}
	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}
	public String getDepositType() {
		return depositType;
	}
	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}
	public String getOlaPIN() {
		return olaPIN;
	}
	public void setOlaPIN(String olaPIN) {
		this.olaPIN = olaPIN;
	}
	public Long getOlaTranxId() {
		return olaTranxId;
	}
	public void setOlaTranxId(Long olaTranxId) {
		this.olaTranxId = olaTranxId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	

	

}
