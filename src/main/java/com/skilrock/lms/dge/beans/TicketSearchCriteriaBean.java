package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class TicketSearchCriteriaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int gameId;
	private String criteria;
	private String status;
	private String ticketNumber;
	private ArrayList<Integer> userIdList;
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public ArrayList<Integer> getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(ArrayList<Integer> userIdList) {
		this.userIdList = userIdList;
	}
}
