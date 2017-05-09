package com.skilrock.lms.coreEngine.virtualSport.beans;

import java.sql.Timestamp;
import java.util.Date;

public class VSRequestBean {
	private String orgName;
	private int orgId;
	private String terminalId;
	private int userId;
	private String userName;
	private String password;
	private String txnId;
	private int gameId;
	private Timestamp startDate;
	private Timestamp endDate;
	private double mrpAmt;
	private String ticketNumber;
	private String lmsTxnId;
	private String grTxnId;
	private String lmsStatus;
	private String grStatus;
	private Timestamp transactionDate;

	public VSRequestBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VSRequestBean(String txnId) {
		super();
		this.txnId = txnId;
	}
	
	

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public VSRequestBean(String orgName, int orgId, String terminalId,
			int userId, String userName) {
		super();
		this.orgName = orgName;
		this.orgId = orgId;
		this.terminalId = terminalId;
		this.userId = userId;
		this.userName = userName;
	}
	
	
	

	

	public VSRequestBean(Timestamp startDate, Timestamp endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public VSRequestBean(int userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public double getMrpAmt() {
		return mrpAmt;
	}

	public void setMrpAmt(double mrpAmt) {
		this.mrpAmt = mrpAmt;
	}
	
	

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getLmsTxnId() {
		return lmsTxnId;
	}

	public void setLmsTxnId(String lmsTxnId) {
		this.lmsTxnId = lmsTxnId;
	}

	public String getGrTxnId() {
		return grTxnId;
	}

	public void setGrTxnId(String grTxnId) {
		this.grTxnId = grTxnId;
	}

	public String getLmsStatus() {
		return lmsStatus;
	}

	public void setLmsStatus(String lmsStatus) {
		this.lmsStatus = lmsStatus;
	}

	public String getGrStatus() {
		return grStatus;
	}

	public void setGrStatus(String grStatus) {
		this.grStatus = grStatus;
	}
	
	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "VSRequestBean [endDate=" + endDate + ", gameId=" + gameId
				+ ", grStatus=" + grStatus + ", grTxnId=" + grTxnId
				+ ", lmsStatus=" + lmsStatus + ", lmsTxnId=" + lmsTxnId
				+ ", mrpAmt=" + mrpAmt + ", orgId=" + orgId + ", orgName="
				+ orgName + ", password=" + password + ", startDate="
				+ startDate + ", terminalId=" + terminalId + ", ticketNumber="
				+ ticketNumber + ", transactionDate=" + transactionDate
				+ ", txnId=" + txnId + ", userId=" + userId + ", userName="
				+ userName + "]";
	}
	
	

	
}
