package com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans;

import java.io.Serializable;

public class BoardTicketDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int boardId;
	private double panelWinAmt;
	private double winningAmt;
	private int betAmtMultiple;
	private String status;
	private String pickedData;
	private String claimedAt;
	private String playType;
	private int noOfLines;
	private String ticketStatus;
	
	
	
	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getPickedData() {
		return pickedData;
	}

	public double getPanelWinAmt() {
		return panelWinAmt;
	}

	public void setPanelWinAmt(double panelWinAmt) {
		this.panelWinAmt = panelWinAmt;
	}

	public void setPickedData(String pickedData) {
		this.pickedData = pickedData;
	}

	public String getClaimedAt() {
		return claimedAt;
	}

	public void setClaimedAt(String claimedAt) {
		this.claimedAt = claimedAt;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public int getBoardId() {
		return boardId;
	}

	public int getBetAmtMultiple() {
		return betAmtMultiple;
	}

	public void setBetAmtMultiple(int betAmtMultiple) {
		this.betAmtMultiple = betAmtMultiple;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}
	
	


}