package com.skilrock.lms.coreEngine.sportsLottery.gameMgmt.controllerImpl;

import java.util.List;

import com.skilrock.lms.coreEngine.sportsLottery.beans.TeamMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;

public class EventInsertionControllerImpl {

	public List<TeamMasterBean> getTeamMasterData(int gameId) {

		List<TeamMasterBean> teamMasterList = null;
		try {
			teamMasterList = SportLotteryServiceIntegration.getTeamMasterData(gameId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return teamMasterList;
	}

	public String eventInsertionSubmit(int gameId, String homeTeam, String awayTeam, String[] optionSet, long startTime, long endTime) {

		String status = null;
		try {
			status = SportLotteryServiceIntegration.eventInsertionSubmit(gameId, homeTeam, awayTeam, optionSet, startTime, endTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return status;
	}
}