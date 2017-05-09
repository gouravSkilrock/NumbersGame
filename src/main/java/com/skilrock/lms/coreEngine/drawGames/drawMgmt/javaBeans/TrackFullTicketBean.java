package com.skilrock.lms.coreEngine.drawGames.drawMgmt.javaBeans;

import java.io.Serializable;

public class TrackFullTicketBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ticketNumber;
	private String ticketFormat;
	private String idGenDate;;
	private int gameId;
	private String remarks;
	private short reprintCount;
	private short barcodeCount;
	private String status;

	public TrackFullTicketBean() {
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getTicketFormat() {
		return ticketFormat;
	}

	public void setTicketFormat(String ticketFormat) {
		this.ticketFormat = ticketFormat;
	}

	public String getIdGenDate() {
		return idGenDate;
	}

	public void setIdGenDate(String idGenDate) {
		this.idGenDate = idGenDate;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public short getReprintCount() {
		return reprintCount;
	}

	public void setReprintCount(short reprintCount) {
		this.reprintCount = reprintCount;
	}

	public short getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(short barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}