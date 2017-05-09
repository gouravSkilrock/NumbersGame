package com.skilrock.lms.embedded.sportsLottery.reportsMgmt.action;

import java.io.IOException;
import java.util.List;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.WinningResultReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.reportsMgmt.controllerImpl.WinningResultReportControllerImpl;
import com.skilrock.lms.embedded.sportsLottery.common.SportsLotteryResponseData;

public class WinningResultReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public WinningResultReportAction() {
		super(WinningResultReportAction.class);
	}

	private String userName;
	private int gameId;
	private int gameTypeId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public void slWinningResultReport() {

		WinningResultReportControllerImpl controllerImpl = new WinningResultReportControllerImpl();
		List<WinningResultReportBean> winningResultReportList = null;
		try {
			winningResultReportList = controllerImpl.winningResultReportSearch(gameId, gameTypeId);
			String responseDate = SportsLotteryResponseData.generateWinningResultReportData(userName, winningResultReportList);
			logger.info("WinningResultReportData - "+responseDate);
			response.getOutputStream().write(responseDate.getBytes());
		} catch (SLEException e) {
			try {
				response.getOutputStream().write(("ErrorMsg:"+e.getErrorMessage()).getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}
}