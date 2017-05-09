package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;
import java.util.Map;

public class WinningResultReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String gameName;
	private String gameTypeName;
	private int drawId;
	private String drawName;
	private String drawDate;
	private String drawTime;
	private Map<String, String> eventOptionMap;

	public WinningResultReportBean() {
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public Map<String, String> getEventOptionMap() {
		return eventOptionMap;
	}

	public void setEventOptionMap(Map<String, String> eventOptionMap) {
		this.eventOptionMap = eventOptionMap;
	}
}