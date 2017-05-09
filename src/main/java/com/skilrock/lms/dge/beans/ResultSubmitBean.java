package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class ResultSubmitBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String drawDateTime;
	private int drawId;
	private int gameId;
	private String gameName;
	private int gameNo;
	private String returnStatus;
	private String updateTime;
	private String updateTime2;
	private int userId;
	private int userId2;
	
	private int[] userIdArr;

	private String userName;
	private String userName2;

	private String winResult;
	private String winResult2;
	
	private String firstMacResult;
	private String secondMacResult;
	private int eventId ;
	private String drawName;
	
	public String getDrawDateTime() {
		return drawDateTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public String getUpdateTime2() {
		return updateTime2;
	}

	public int getUserId() {
		return userId;
	}

	public int getUserId2() {
		return userId2;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserName2() {
		return userName2;
	}

	public String getWinResult() {
		return winResult;
	}

	public String getWinResult2() {
		return winResult2;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public void setUpdateTime2(String updateTime2) {
		this.updateTime2 = updateTime2;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserId2(int userId2) {
		this.userId2 = userId2;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}

	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}

	public void setWinResult2(String winResult2) {
		this.winResult2 = winResult2;
	}

	public int[] getUserIdArr() {
		return userIdArr;
	}

	public void setUserIdArr(int[] userIdArr) {
		this.userIdArr = userIdArr;
	}

	public String getFirstMacResult() {
		return firstMacResult;
	}

	public void setFirstMacResult(String firstMacResult) {
		this.firstMacResult = firstMacResult;
	}

	public String getSecondMacResult() {
		return secondMacResult;
	}

	public void setSecondMacResult(String secondMacResult) {
		this.secondMacResult = secondMacResult;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

}