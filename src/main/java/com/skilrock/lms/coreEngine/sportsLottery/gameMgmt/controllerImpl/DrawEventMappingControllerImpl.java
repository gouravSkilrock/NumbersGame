package com.skilrock.lms.coreEngine.sportsLottery.gameMgmt.controllerImpl;

import java.util.List;

import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.EventMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;

public class DrawEventMappingControllerImpl {
	public List<DrawMasterBean> getDrawMasterList(int gameId, int gameTypeId) {
		List<DrawMasterBean> drawMasterList = null;
		try {
			drawMasterList = SportLotteryServiceIntegration.getDrawMappingDrawMasterList(gameId, gameTypeId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return drawMasterList;
	}

	public List<EventMasterBean> getEventMasterList(int gameId, int gameTypeId, int drawId) {
		List<EventMasterBean> eventMasterList = null;
		try {
			eventMasterList = SportLotteryServiceIntegration.getDrawMappingEventMasterList(gameId, gameTypeId, drawId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return eventMasterList;
	}

	public String drawEventMappingSubmit(int gameId, int gameTypeId, int drawId, String eventSelected) {
		String retValue = null;
		try {
			String[] eventSelectionArray = eventSelected.split(",");

			retValue = SportLotteryServiceIntegration.drawEventMappingSubmit(gameId, gameTypeId, drawId, eventSelectionArray);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return retValue;
	}
}