package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperGameDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gameName;
	private String gameDevName;
	private String gameNo;
	private int gameId;
	private String gameStatus;
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameDevName() {
		return gameDevName;
	}
	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}
			
}
