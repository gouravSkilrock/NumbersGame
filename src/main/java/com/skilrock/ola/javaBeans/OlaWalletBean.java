package com.skilrock.ola.javaBeans;

public class OlaWalletBean {
	
	private int walletId;
	private String walletDisplayName;
	private String walletDevName;
	private String verificationType;
	private double minDeposit;
	private String registrationType;
	
    
	
	public String getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}
	public int getWalletId() {
		return walletId;
	}
	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}
	public String getWalletDisplayName() {
		return walletDisplayName;
	}
	public void setWalletDisplayName(String walletDisplayName) {
		this.walletDisplayName = walletDisplayName;
	}
	public String getWalletDevName() {
		return walletDevName;
	}
	public void setWalletDevName(String walletDevName) {
		this.walletDevName = walletDevName;
	}
	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}
	public String getVerificationType() {
		return verificationType;
	}
	public double getMinDeposit() {
		return minDeposit;
	}
	public void setMinDeposit(double minDeposit) {
		this.minDeposit = minDeposit;
	}
	
	
	@Override
	public String toString() {
		return "OlaWalletBean [walletId=" + walletId + ", walletDisplayName="
				+ walletDisplayName + ", walletDevName=" + walletDevName
				+ ", verificationType=" + verificationType + ", minDeposit="
				+ minDeposit + ", registrationType=" + registrationType + "]";
	}
	
	

	
}
