package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class GameDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int game_id;
	private String gameDisplayName;
	private String gameDevName;

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int gameId) {
		game_id = gameId;
	}

	public String getGameDisplayName() {
		return gameDisplayName;
	}

	public void setGameDisplayName(String gameDisplayName) {
		this.gameDisplayName = gameDisplayName;
	}

	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	@Override
	public String toString() {
		return "GameDataBean [gameDevName=" + gameDevName
				+ ", gameDisplayName=" + gameDisplayName + ", game_id="
				+ game_id + "]";
	}
}
