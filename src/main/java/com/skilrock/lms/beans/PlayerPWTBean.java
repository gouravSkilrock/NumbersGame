package com.skilrock.lms.beans;

import java.io.Serializable;

public class PlayerPWTBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bookNbr;
	private String chequeDate;
	private String chequeNbr;
	private String draweeBank;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private String issuingPartyName;
	private double netAmt;
	private String paymentType;
	private String playerFirstName;
	private int playerId;
	private String playerLastName;

	private double pwtAmt;
	private String status;

	private int taskId;
	private double tax;
	private String ticketNbr;
	private String trancDate;

	private int transactionId;
	private String virnCode;

	public String getBookNbr() {
		return bookNbr;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public String getChequeNbr() {
		return chequeNbr;
	}

	public String getDraweeBank() {
		return draweeBank;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public String getIssuingPartyName() {
		return issuingPartyName;
	}

	public double getNetAmt() {
		return netAmt;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getPlayerFirstName() {
		return playerFirstName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getPlayerLastName() {
		return playerLastName;
	}

	public double getPwtAmt() {
		return pwtAmt;
	}

	public String getStatus() {
		return status;
	}

	public int getTaskId() {
		return taskId;
	}

	public double getTax() {
		return tax;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public String getTrancDate() {
		return trancDate;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public String getVirnCode() {
		return virnCode;
	}

	public void setBookNbr(String bookNbr) {
		this.bookNbr = bookNbr;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNbr(String chequeNbr) {
		this.chequeNbr = chequeNbr;
	}

	public void setDraweeBank(String draweeBank) {
		this.draweeBank = draweeBank;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setIssuingPartyName(String issuingPartyName) {
		this.issuingPartyName = issuingPartyName;
	}

	public void setNetAmt(double netAmt) {
		this.netAmt = netAmt;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}

	public void setPwtAmt(double pwtAmt) {
		this.pwtAmt = pwtAmt;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setTrancDate(String trancDate) {
		this.trancDate = trancDate;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public void setVirnCode(String virnCode) {
		this.virnCode = virnCode;
	}

}
