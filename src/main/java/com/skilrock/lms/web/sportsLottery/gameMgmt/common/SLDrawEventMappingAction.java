package com.skilrock.lms.web.sportsLottery.gameMgmt.common;

import java.util.List;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.EventMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.gameMgmt.controllerImpl.DrawEventMappingControllerImpl;
import com.skilrock.lms.web.sportsLottery.common.serviceImpl.CommonMethodServiceImpl;

public class SLDrawEventMappingAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public SLDrawEventMappingAction() {
		super(SLDrawEventMappingAction.class);
	}

	private Map<Integer, GameMasterBean> gameMap;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	List<DrawMasterBean> drawMasterList;
	List<EventMasterBean> eventMasterList;
	private int noOfEvents;
	private String eventSelected;

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
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

	public List<DrawMasterBean> getDrawMasterList() {
		return drawMasterList;
	}

	public void setDrawMasterList(List<DrawMasterBean> drawMasterList) {
		this.drawMasterList = drawMasterList;
	}

	public List<EventMasterBean> getEventMasterList() {
		return eventMasterList;
	}

	public void setEventMasterList(List<EventMasterBean> eventMasterList) {
		this.eventMasterList = eventMasterList;
	}

	public int getNoOfEvents() {
		return noOfEvents;
	}

	public void setNoOfEvents(int noOfEvents) {
		this.noOfEvents = noOfEvents;
	}

	public String getEventSelected() {
		return eventSelected;
	}

	public void setEventSelected(String eventSelected) {
		this.eventSelected = eventSelected;
	}

	public String drawEventMappingMenu() {
		CommonMethodServiceImpl serviceImpl = new CommonMethodServiceImpl();
		try {
			gameMap = serviceImpl.getGameMap();
		} catch (SLEException ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String drawEventMappingSearch() {
		DrawEventMappingControllerImpl controllerImpl = new DrawEventMappingControllerImpl();
		try {
			drawMasterList = controllerImpl.getDrawMasterList(gameId, gameTypeId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String drawEventMappingEventsSearch() {
		DrawEventMappingControllerImpl controllerImpl = new DrawEventMappingControllerImpl();
		try {
			eventMasterList = controllerImpl.getEventMasterList(gameId, gameTypeId, drawId);
			if(eventMasterList.size()>0) {
				noOfEvents = eventMasterList.get(0).getNoOfEvents();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String drawEventMappingEventsSubmit() {
		DrawEventMappingControllerImpl controllerImpl = new DrawEventMappingControllerImpl();
		try {
			controllerImpl.drawEventMappingSubmit(gameId, gameTypeId, drawId, eventSelected);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}