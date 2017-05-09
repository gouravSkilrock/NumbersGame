package com.skilrock.lms.coreEngine.sportsLottery.beans;

public class SportsLotteryGameEventDataBean {

	private int eventId;
	private String[] selectedOption;
	private String eventDescription;
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String[] getSelectedOption() {
		return selectedOption;
	}
	public void setSelectedOption(String[] selectedOption) {
		this.selectedOption = selectedOption;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	
}
