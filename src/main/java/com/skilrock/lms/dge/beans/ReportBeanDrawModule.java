package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class ReportBeanDrawModule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Search Specific Information
	private String fromDate;
	private String gameDispName;

	private int gameId;
	private String gameName;
	// Game Specific Information
	private int gameNo;
	private ReportGameBean repGameBean;

	private String toDate;

	public String getFromDate() {
		return fromDate;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public ReportGameBean getRepGameBean() {
		return repGameBean;
	}

	public String getToDate() {
		return toDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setRepGameBean(ReportGameBean repGameBean) {
		this.repGameBean = repGameBean;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}