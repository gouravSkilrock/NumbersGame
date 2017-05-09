package com.skilrock.lms.coreEngine.sportsLottery.drawMgmt.controllerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawEventResultBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;

public class ResultSubmissionControllerImpl {

	public List<DrawMasterBean> resultSubmissionDrawData(int gameId, int gameTypeId, String merchantName) {

		List<DrawMasterBean> drawMasterList = null;
		try {
			drawMasterList = SportLotteryServiceIntegration.resultSubmissionDrawData(gameId, gameTypeId, merchantName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return drawMasterList;
	}

	public String resultSubmissionSubmit(int gameId, int gameTypeId, int drawId, String eventResult) {

		String response = null;
		try {
			Map<Integer, String> eventOptionResult = new HashMap<Integer, String>();
			String[] eventResultArray = eventResult.split(",");
			for(String result : eventResultArray) {
				eventOptionResult.put(Integer.parseInt(result.split("_")[0]), result.split("_")[1]);
			}

			DrawEventResultBean drawEventResultBean = new DrawEventResultBean();
			drawEventResultBean.setGameId(gameId);
			drawEventResultBean.setGameTypeId(gameTypeId);
			drawEventResultBean.setDrawId(drawId);
			drawEventResultBean.setEventOptionResult(eventOptionResult);

			response = SportLotteryServiceIntegration.resultSubmissionSubmit(drawEventResultBean);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return response;
	}
}