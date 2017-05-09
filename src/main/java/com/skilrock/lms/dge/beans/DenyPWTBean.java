package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class DenyPWTBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private int gameNo;
	private String panelId;
	private String status;
	private String ticketNo;
	private int gameId ;
	public int getDrawId() {
		return drawId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String getPanelId() {
		return panelId;
	}

	public String getStatus() {
		return status;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
}
