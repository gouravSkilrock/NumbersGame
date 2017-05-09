package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.sql.Date;

public class JackpotBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date endDate;
	private String gameName;
	private String jackpotQuery;
	private Date startDate;
	private int gameId;
	

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getGameName() {
		return gameName;
	}

	public String getJackpotQuery() {
		return jackpotQuery;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setJackpotQuery(String jackpotQuery) {
		this.jackpotQuery = jackpotQuery;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
