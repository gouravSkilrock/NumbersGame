package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;

public class TeamMasterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int teamId;
	private int gameId;
	private String teamName;
	private String teamCode;
	private String status;

	public TeamMasterBean() {
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}