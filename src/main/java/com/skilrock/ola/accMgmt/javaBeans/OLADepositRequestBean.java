package com.skilrock.ola.accMgmt.javaBeans;

public class OLADepositRequestBean {
	
	private String depositAnyWhere;
	private int plrId;
	private double depositAmt;
	private double tgDepositAmt;
	private int walletId; 
	private String refCode;
	private String walletDevName;
	private long transactionId;
	private String deviceType;
	
	
	public double getTgDepositAmt() {
		return tgDepositAmt;
	}
	public void setTgDepositAmt(double tgDepositAmt) {
		this.tgDepositAmt = tgDepositAmt;
	}
	public String getDepositAnyWhere() {
		return depositAnyWhere;
	}
	public void setDepositAnyWhere(String depositAnyWhere) {
		this.depositAnyWhere = depositAnyWhere;
	}
	public int getPlrId() {
		return plrId;
	}
	public void setPlrId(int plrId) {
		this.plrId = plrId;
	}
	public double getDepositAmt() {
		return depositAmt;
	}
	public void setDepositAmt(double depositAmt) {
		this.depositAmt = depositAmt;
	}
	public int getWalletId() {
		return walletId;
	}
	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}
	public String getRefCode() {
		return refCode;
	}
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	public void setWalletDevName(String walletDevName) {
		this.walletDevName = walletDevName;
	}
	public String getWalletDevName() {
		return walletDevName;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public long getTransactionId() {
		return transactionId;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	
}
