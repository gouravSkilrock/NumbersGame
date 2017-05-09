package com.skilrock.lms.web.drawGames.reportsMgmt.beans;

import java.util.*;

public class PendingWinningTransferDataBean {private String gameName ;
private int gameId ;
private int drawId;
private String drawName ;
private String drawDateTime ;
private Map<String, Double> winnintAmtMap = new HashMap<String, Double>();
public String getGameName() {
	return gameName;
}
public void setGameName(String gameName) {
	this.gameName = gameName;
}
public String getDrawName() {
	return drawName;
}
public void setDrawName(String drawName) {
	this.drawName = drawName;
}
public String getDrawDateTime() {
	return drawDateTime;
}
public void setDrawDateTime(String drawDateTime) {
	this.drawDateTime = drawDateTime;
}
public Map<String, Double> getWinnintAmtMap() {
	return winnintAmtMap;
}
public void setWinnintAmtMap(Map<String, Double> winnintAmtMap) {
	this.winnintAmtMap = winnintAmtMap;
}
public int getGameId() {
	return gameId;
}
public void setGameId(int gameId) {
	this.gameId = gameId;
}
public int getDrawId() {
	return drawId;
}
public void setDrawId(int drawId) {
	this.drawId = drawId;
}}
