package com.skilrock.lms.web.sportsLottery.gameMgmt.common;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.TeamMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.gameMgmt.controllerImpl.EventInsertionControllerImpl;
import com.skilrock.lms.web.sportsLottery.common.serviceImpl.CommonMethodServiceImpl;

public class SLEventInsertionAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public SLEventInsertionAction() {
		super(SLEventInsertionAction.class);
	}

	private int gameId;
	private String homeTeam;
	private String awayTeam;
	private String[] optionSet;
	private String eventStartDate;
	private String eventStartHr;
	private String eventStartMin;
	private String eventStartSec;
	private String eventEndDate;
	private String eventEndHr;
	private String eventEndMin;
	private String eventEndSec;
	private Map<Integer, GameMasterBean> gameMap;
	private List<TeamMasterBean> teamMasterList;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public String[] getOptionSet() {
		return optionSet;
	}

	public void setOptionSet(String[] optionSet) {
		this.optionSet = optionSet;
	}

	public String getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public String getEventStartHr() {
		return eventStartHr;
	}

	public void setEventStartHr(String eventStartHr) {
		this.eventStartHr = eventStartHr;
	}

	public String getEventStartMin() {
		return eventStartMin;
	}

	public void setEventStartMin(String eventStartMin) {
		this.eventStartMin = eventStartMin;
	}

	public String getEventStartSec() {
		return eventStartSec;
	}

	public void setEventStartSec(String eventStartSec) {
		this.eventStartSec = eventStartSec;
	}

	public String getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public String getEventEndHr() {
		return eventEndHr;
	}

	public void setEventEndHr(String eventEndHr) {
		this.eventEndHr = eventEndHr;
	}

	public String getEventEndMin() {
		return eventEndMin;
	}

	public void setEventEndMin(String eventEndMin) {
		this.eventEndMin = eventEndMin;
	}

	public String getEventEndSec() {
		return eventEndSec;
	}

	public void setEventEndSec(String eventEndSec) {
		this.eventEndSec = eventEndSec;
	}

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
	}

	public List<TeamMasterBean> getTeamMasterList() {
		return teamMasterList;
	}

	public void setTeamMasterList(List<TeamMasterBean> teamMasterList) {
		this.teamMasterList = teamMasterList;
	}

	public String eventInsertionMenu() {
		CommonMethodServiceImpl serviceImpl = new CommonMethodServiceImpl();
		try {
			gameMap = serviceImpl.getGameMap();
		} catch (SLEException ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public String eventInsertionTeamSearch() {
		EventInsertionControllerImpl controllerImpl = new EventInsertionControllerImpl();
		try {
			teamMasterList = controllerImpl.getTeamMasterData(gameId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String eventInsertionSubmit() {
		EventInsertionControllerImpl controllerImpl = new EventInsertionControllerImpl();
		SimpleDateFormat simpleDateFormat = null;
		try {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long startTime = simpleDateFormat.parse(eventStartDate+" "+eventStartHr+":"+eventStartMin+":"+eventStartSec).getTime();
			long endTime = simpleDateFormat.parse(eventEndDate+" "+eventEndHr+":"+eventEndMin+":"+eventEndSec).getTime();
			controllerImpl.eventInsertionSubmit(gameId, homeTeam, awayTeam, optionSet, startTime, endTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}