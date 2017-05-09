package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;
import java.util.List;

public class EventMasterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int eventId;
	private int gameId;
	private String eventDisplay;
	private String eventDescription;
	private String startTime;
	private String endTime;
	private String entryTime;
	private List<String> eventOptionsList;
	private int noOfEvents;

	public EventMasterBean() {
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getEventDisplay() {
		return eventDisplay;
	}

	public void setEventDisplay(String eventDisplay) {
		this.eventDisplay = eventDisplay;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public List<String> getEventOptionsList() {
		return eventOptionsList;
	}

	public void setEventOptionsList(List<String> eventOptionsList) {
		this.eventOptionsList = eventOptionsList;
	}

	public int getNoOfEvents() {
		return noOfEvents;
	}

	public void setNoOfEvents(int noOfEvents) {
		this.noOfEvents = noOfEvents;
	}

	@Override
	public String toString() {
		return "EventMasterBean [endTime=" + endTime + ", entryTime="
				+ entryTime + ", eventDescription=" + eventDescription
				+ ", eventDisplay=" + eventDisplay + ", eventId=" + eventId
				+ ", eventOptionsList=" + eventOptionsList + ", gameId="
				+ gameId + ", noOfEvents=" + noOfEvents + ", startTime="
				+ startTime + "]";
	}
}