package com.skilrock.lms.instantWin.javaBeans;

public class VerifyTicketResponseBean implements IWDataFace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tktNbr;
	private String tktStatus;
	private String paymentTime;
	private double winningAmt;
	private String purchaseTime;
	private String tktData;
	private String claimTime;
	private String purchasedFrom;
	private boolean paymentAllowed;
	private String iwTransactionId;
	private int errorCode;
	private String errorMsg;
	private boolean isPlayerReg;
	private String tktTxnId;

	public String getTktNbr() {
		return tktNbr;
	}

	public void setTktNbr(String tktNbr) {
		this.tktNbr = tktNbr;
	}

	public String getTktStatus() {
		return tktStatus;
	}

	public void setTktStatus(String tktStatus) {
		this.tktStatus = tktStatus;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public String getTktData() {
		return tktData;
	}

	public void setTktData(String tktData) {
		this.tktData = tktData;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	public String getPurchasedFrom() {
		return purchasedFrom;
	}

	public void setPurchasedFrom(String purchasedFrom) {
		this.purchasedFrom = purchasedFrom;
	}

	public boolean isPaymentAllowed() {
		return paymentAllowed;
	}

	public void setPaymentAllowed(boolean paymentAllowed) {
		this.paymentAllowed = paymentAllowed;
	}

	public String getIwTransactionId() {
		return iwTransactionId;
	}

	public void setIwTransactionId(String iwTransactionId) {
		this.iwTransactionId = iwTransactionId;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isPlayerReg() {
		return isPlayerReg;
	}

	public void setPlayerReg(boolean isPlayerReg) {
		this.isPlayerReg = isPlayerReg;
	}

	public String getTktTxnId() {
		return tktTxnId;
	}

	public void setTktTxnId(String tktTxnId) {
		this.tktTxnId = tktTxnId;
	}

	@Override
	public String toString() {
		return "VerifyTicketResponseBean [tktNbr=" + tktNbr + ", tktStatus="
				+ tktStatus + ", paymentTime=" + paymentTime + ", winningAmt="
				+ winningAmt + ", purchaseTime=" + purchaseTime + ", tktData="
				+ tktData + ", claimTime=" + claimTime + ", purchasedFrom="
				+ purchasedFrom + ", paymentAllowed=" + paymentAllowed
				+ ", iwTransactionId=" + iwTransactionId + ", errorCode="
				+ errorCode + ", errorMsg=" + errorMsg + ", isPlayerReg="
				+ isPlayerReg + ", tktTxnId=" + tktTxnId + "]";
	}

}
