package com.skilrock.ipe.Bean;

import java.io.Serializable;

public class PwtLMSBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String TicketNo;
	int gameNo;
	int gameId;
	String gameName;
	private String virnNo;
	private String claimStatus;
	private String status;
	private double prizeAmt;
	private boolean isHighPrize = false;
	private boolean isRegRequired = false;
	private String returnType;
	private String isSold;
	private String message;
	private String messageCode;
	private String ticketMessage;
	private String ticketVerificationStatus;
	private String updateTicketType;
	private String verificationStatus;
	private String validity = null;
	private boolean isValid;
	private String tktvalidity = null;
	private String virnvalidity = null;
	
	//added for payment through mPesa
	private boolean ismPesaEnable;
	private String refNumber;
	private String mobileNumber;
	private boolean success;
	
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean ismPesaEnable() {
		return ismPesaEnable;
	}

	public void setIsmPesaEnable(boolean ismPesaEnable) {
		this.ismPesaEnable = ismPesaEnable;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getIsSold() {
		return isSold;
	}

	public void setIsSold(String isSold) {
		this.isSold = isSold;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getTicketMessage() {
		return ticketMessage;
	}

	public void setTicketMessage(String ticketMessage) {
		this.ticketMessage = ticketMessage;
	}

	public String getTicketVerificationStatus() {
		return ticketVerificationStatus;
	}

	public void setTicketVerificationStatus(String ticketVerificationStatus) {
		this.ticketVerificationStatus = ticketVerificationStatus;
	}

	public String getUpdateTicketType() {
		return updateTicketType;
	}

	public void setUpdateTicketType(String updateTicketType) {
		this.updateTicketType = updateTicketType;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getTktvalidity() {
		return tktvalidity;
	}

	public void setTktvalidity(String tktvalidity) {
		this.tktvalidity = tktvalidity;
	}

	public String getVirnvalidity() {
		return virnvalidity;
	}

	public void setVirnvalidity(String virnvalidity) {
		this.virnvalidity = virnvalidity;
	}

	public String getTicketNo() {
		return TicketNo;
	}

	public void setTicketNo(String ticketNo) {
		TicketNo = ticketNo;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getVirnNo() {
		return virnNo;
	}

	public void setVirnNo(String virnNo) {
		this.virnNo = virnNo;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getPrizeAmt() {
		return prizeAmt;
	}

	public void setPrizeAmt(double prizeAmt) {
		this.prizeAmt = prizeAmt;
	}

	public boolean isHighPrize() {
		return isHighPrize;
	}

	public void setHighPrize(boolean isHighPrize) {
		this.isHighPrize = isHighPrize;
	}

	public boolean isRegRequired() {
		return isRegRequired;
	}

	public void setRegRequired(boolean isRegRequired) {
		this.isRegRequired = isRegRequired;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

}
