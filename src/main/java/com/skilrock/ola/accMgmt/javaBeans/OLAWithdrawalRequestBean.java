package com.skilrock.ola.accMgmt.javaBeans;

public class OLAWithdrawalRequestBean {
	
	private int playerId;
	private double withdrawlAmt;
	private String devWalletName;
	private int walletId;
	private String withdrawlAnyWhere;
	private String authenticationCode; 
	private String refCode;
	private long txnId;
	private String deviceType;
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public double getWithdrawlAmt() {
		return withdrawlAmt;
	}
	public void setWithdrawlAmt(double withdrawlAmt) {
		this.withdrawlAmt = withdrawlAmt;
	}
	public String getDevWalletName() {
		return devWalletName;
	}
	public void setDevWalletName(String devWalletName) {
		this.devWalletName = devWalletName;
	}
	public int getWalletId() {
		return walletId;
	}
	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}
	public String getWithdrawlAnyWhere() {
		return withdrawlAnyWhere;
	}
	public void setWithdrawlAnyWhere(String withdrawlAnyWhere) {
		this.withdrawlAnyWhere = withdrawlAnyWhere;
	}
	public String getAuthenticationCode() {
		return authenticationCode;
	}
	public void setAuthenticationCode(String authenticationCode) {
		this.authenticationCode = authenticationCode;
	}
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	public String getRefCode() {
		return refCode;
	}
	public void setTxnId(long txnId) {
		this.txnId = txnId;
	}
	public long getTxnId() {
		return txnId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
