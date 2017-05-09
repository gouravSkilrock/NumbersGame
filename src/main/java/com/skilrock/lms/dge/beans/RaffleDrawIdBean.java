package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class RaffleDrawIdBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimedTime;
	private String drawDateTime;
	private int drawId;
	private boolean isAppReq;
	private boolean isHighLevel;
	private boolean isRaffleTickets;
	private boolean isResAwaited;

	private boolean isValid;
	// added by yogesh for BO direct player pwt
	private String message;
	private String messageCode;
	private String pwtStatus;
	private int raffleGameno;
	private String raffleTicketNumberInDB;

	private String status;
	private String tableName;
	private String verificationStatus;

	private String winningAmt;
	private String winResult;
	private boolean isHighPrize;
	private boolean isWinTkt;
	private int  raffleGameId;
	// private boolean isRetPayLimit;

	public boolean isWinTkt() {
		return isWinTkt;
	}

	public void setWinTkt(boolean isWinTkt) {
		this.isWinTkt = isWinTkt;
	}

	public String getClaimedTime() {
		return claimedTime;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public String getPwtStatus() {
		return pwtStatus;
	}

	public int getRaffleGameno() {
		return raffleGameno;
	}

	public String getRaffleTicketNumberInDB() {
		return raffleTicketNumberInDB;
	}

	public String getStatus() {
		return status;
	}

	public String getTableName() {
		return tableName;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public String getWinningAmt() {
		return winningAmt;
	}

	public String getWinResult() {
		return winResult;
	}

	public boolean isAppReq() {
		return isAppReq;
	}

	public boolean isHighLevel() {
		return isHighLevel;
	}

	public boolean isRaffleTickets() {
		return isRaffleTickets;
	}

	public boolean isResAwaited() {
		return isResAwaited;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setAppReq(boolean isAppReq) {
		this.isAppReq = isAppReq;
	}

	public void setClaimedTime(String claimedTime) {
		this.claimedTime = claimedTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setHighLevel(boolean isHighLevel) {
		this.isHighLevel = isHighLevel;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public void setPwtStatus(String pwtStatus) {
		this.pwtStatus = pwtStatus;
	}

	public void setRaffleGameno(int raffleGameno) {
		this.raffleGameno = raffleGameno;
	}

	public void setRaffleTicketNumberInDB(String raffleTicketNumberInDB) {
		this.raffleTicketNumberInDB = raffleTicketNumberInDB;
	}

	public void setRaffleTickets(boolean isRaffleTickets) {
		this.isRaffleTickets = isRaffleTickets;
	}

	public void setResAwaited(boolean isResAwaited) {
		this.isResAwaited = isResAwaited;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public void setWinningAmt(String winningAmt) {
		this.winningAmt = winningAmt;
	}

	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}

	public boolean isHighPrize() {
		return isHighPrize;
	}

	public void setHighPrize(boolean isHighPrize) {
		this.isHighPrize = isHighPrize;
	}

	public int getRaffleGameId() {
		return raffleGameId;
	}

	public void setRaffleGameId(int raffleGameId) {
		this.raffleGameId = raffleGameId;
	}

	

}