package com.skilrock.lms.embedded.sportsLottery.dataMgmt.action;

import java.util.List;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.dataMgmt.controllerImpl.SportsLotteryDataControllerImpl;
import com.skilrock.lms.embedded.sportsLottery.common.SportsLotteryResponseData;

public class SportsLotteryTerminalDataAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public SportsLotteryTerminalDataAction() {
		super(SportsLotteryTerminalDataAction.class);
	}

	public void fetchLoginDrawGameData() {
		List<GameMasterBean> gameMasterList = null;
		String responseString = null;
		try {
			SportsLotteryDataControllerImpl sportsLotteryDataControllerImpl =new SportsLotteryDataControllerImpl();
			gameMasterList = sportsLotteryDataControllerImpl.getSportsLotteryGameData("WGRL");
			responseString = SportsLotteryResponseData.generateDrawGameData(gameMasterList);
			//response.getOutputStream().write("sdsds".getBytes());
			response.getOutputStream().write(responseString.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}