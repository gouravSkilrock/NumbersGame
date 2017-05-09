package com.skilrock.lms.embedded.sportsLottery.reportsMgmt.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.RetGameDataReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.reportsMgmt.controllerImpl.GameDataReportControllerImpl;
import com.skilrock.lms.embedded.sportsLottery.common.SportsLotteryResponseData;

public class GameDataReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public GameDataReportAction() {
		super(GameDataReportAction.class);
	}

	private String userName;
	private String type;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void slGameDataReport() {

		GameDataReportControllerImpl controllerImpl = new GameDataReportControllerImpl();
		Map<String, List<RetGameDataReportBean>> gameDataReportMap = null;
		SimpleDateFormat simpleDateFormat = null;
		Date date = null;
		try {
			UserInfoBean userBean = getUserBean(userName);
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if ("CURRENT_DAY".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
				date = new Date();
			} else if ("LAST_DAY".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
				date = new Date(new Date().getTime() - 24*60*60*1000);
			} else {
				date = simpleDateFormat.parse(type);
			}

			gameDataReportMap = controllerImpl.gameDataReportRetailerWise(userBean.getUserOrgId(), date, date);
			String responseDate = SportsLotteryResponseData.generateGameDataReportDate(userName, gameDataReportMap, simpleDateFormat.format(date));
			logger.info("GameReportDate - "+responseDate);
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