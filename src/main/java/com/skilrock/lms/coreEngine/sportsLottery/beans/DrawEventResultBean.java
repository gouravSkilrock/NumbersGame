package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;
import java.util.Map;

public class DrawEventResultBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private int gameTypeId;
	private int drawId;
	private Map<Integer, String> eventOptionResult;

	public DrawEventResultBean() {
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public Map<Integer, String> getEventOptionResult() {
		return eventOptionResult;
	}

	public void setEventOptionResult(Map<Integer, String> eventOptionResult) {
		this.eventOptionResult = eventOptionResult;
	}
}