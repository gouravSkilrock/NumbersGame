package com.skilrock.lms.web.sportsLottery.drawMgmt.common;

import java.util.List;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.lms.web.sportsLottery.common.serviceImpl.CommonMethodServiceImpl;

public class ResultSubmissionAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public ResultSubmissionAction() {
		super(ResultSubmissionAction.class);
	}

	private Map<Integer, GameMasterBean> gameMap;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private String eventResult;
	List<DrawMasterBean> drawMasterList;

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

	public String getEventResult() {
		return eventResult;
	}

	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
	}

	public List<DrawMasterBean> getDrawMasterList() {
		return drawMasterList;
	}

	public void setDrawMasterList(List<DrawMasterBean> drawMasterList) {
		this.drawMasterList = drawMasterList;
	}

	public String resultSubmissionMenu() throws Exception {
		CommonMethodServiceImpl serviceImpl = new CommonMethodServiceImpl();
		gameMap = serviceImpl.getGameMap();
		return SUCCESS;
	}

	public String resultSubmissionDrawEventSearch() throws Exception {

		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		try {
			drawMasterList = controllerImpl.resultSubmissionDrawData(gameId, gameTypeId, "WGRL");
			//throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "applicationException";
		}

		return SUCCESS;
	}

	public String resultSubmissionSubmit() throws Exception {

		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		try {
			String status = controllerImpl.resultSubmissionSubmit(gameId, gameTypeId, drawId, eventResult);
			System.out.println(status);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}