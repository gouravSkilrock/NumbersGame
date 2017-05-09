package com.skilrock.lms.web.sportsLottery.reportsMgmt.common;

import java.util.List;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameDataReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.reportsMgmt.controllerImpl.GameDataReportControllerImpl;
import com.skilrock.lms.web.sportsLottery.common.serviceImpl.CommonMethodServiceImpl;

public class SLGameDataReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public SLGameDataReportAction() {
		super(SLGameDataReportAction.class);
	}

	private int gameId;
	private int gameTypeId;
	private String startDate;
	private String endDate;
	private String reportType;
	private Map<Integer, GameMasterBean> gameMap;
	private List<GameDataReportBean> gameDataReportList;

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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
	}

	public List<GameDataReportBean> getGameDataReportList() {
		return gameDataReportList;
	}

	public void setGameDataReportList(List<GameDataReportBean> gameDataReportList) {
		this.gameDataReportList = gameDataReportList;
	}

	public String slGameDataReportMenu() {

		CommonMethodServiceImpl serviceImpl = new CommonMethodServiceImpl();
		try {
			gameMap = serviceImpl.getGameMap();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String slGameDataReportSearch() {
		GameDataReportControllerImpl controllerImpl = new GameDataReportControllerImpl();
		try {
			gameDataReportList = controllerImpl.gameDataReportSearch(gameId, gameTypeId, startDate, endDate, reportType, "WGRL");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}