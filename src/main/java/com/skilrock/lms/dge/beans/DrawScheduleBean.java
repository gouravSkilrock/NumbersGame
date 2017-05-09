package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DrawScheduleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String draw_day;
	private int draw_id;
	private String draw_time;
	private List<Integer> drawIdList;
	private String drawStatus;
	private Timestamp endDate;
	private String freezeDateTime;
	private int freezeTime;
	private int gameNo;
	private String performStatus[];
	private Timestamp postponeDate;
	private int postponeMin;
	private Timestamp startDate;
	private String status;
	private String type;
	private String updatedFreezeTime;
	private String updateMsg;
	private String action;
	private int userId;
	private Timestamp startTimeForDraws;
	private  int gameId ;
	private int eventId;

	private ArrayList<String> tktList;

	public String getDraw_day() {
		return draw_day;
	}

	public int getDraw_id() {
		return draw_id;
	}

	public String getDraw_time() {
		return draw_time;
	}

	public List<Integer> getDrawIdList() {
		return drawIdList;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public String getFreezeDateTime() {
		return freezeDateTime;
	}

	public int getFreezeTime() {
		return freezeTime;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String[] getPerformStatus() {
		return performStatus;
	}

	public Timestamp getPostponeDate() {
		return postponeDate;
	}

	public int getPostponeMin() {
		return postponeMin;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public String getUpdatedFreezeTime() {
		return updatedFreezeTime;
	}

	public String getUpdateMsg() {
		return updateMsg;
	}

	public int getUserId() {
		return userId;
	}

	public void setDraw_day(String draw_day) {
		this.draw_day = draw_day;
	}

	public void setDraw_id(int draw_id) {
		this.draw_id = draw_id;
	}

	public void setDraw_time(String draw_time) {
		this.draw_time = draw_time;
	}

	public void setDrawIdList(List<Integer> drawIdList) {
		this.drawIdList = drawIdList;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public void setFreezeDateTime(String freezeDateTime) {
		this.freezeDateTime = freezeDateTime;
	}

	public void setFreezeTime(int freezeTime) {
		this.freezeTime = freezeTime;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setPerformStatus(String[] performStatus) {
		this.performStatus = performStatus;
	}

	public void setPostponeDate(Timestamp postponeDate) {
		this.postponeDate = postponeDate;
	}

	public void setPostponeMin(int postponeMin) {
		this.postponeMin = postponeMin;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdatedFreezeTime(String updatedFreezeTime) {
		this.updatedFreezeTime = updatedFreezeTime;
	}

	public void setUpdateMsg(String updateMsg) {
		this.updateMsg = updateMsg;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ArrayList<String> getTktList() {
		return tktList;
	}

	public void setTktList(ArrayList<String> tktList) {
		this.tktList = tktList;
	}
	
	public Timestamp getStartTimeForDraws() {
		return startTimeForDraws;
	}

	public void setStartTimeForDraws(Timestamp startTimeForDraws) {
		this.startTimeForDraws = startTimeForDraws;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
}
