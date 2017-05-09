package com.skilrock.lms.rest.services.bean;

import java.util.Date;

public class DaoBean {

	private int userOrgId;
	private int userId;
	private String ticketNbr;
	private Date dateTime;
	private String txnId;
	private String tpTransactionId;
	private String status;
	private int gameId;
	private int ticketPrice;
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getTicketNbr() {
		return ticketNbr;
	}
	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTpTransactionId() {
		return tpTransactionId;
	}
	public void setTpTransactionId(String tpTransactionId) {
		this.tpTransactionId = tpTransactionId;
	}
	public int getUserOrgId() {
		return userOrgId;
	}
	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

}
