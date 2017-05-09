package com.skilrock.lms.coreEngine.sportsLottery.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameDataReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.RetGameDataReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;
import com.skilrock.lms.coreEngine.sportsLottery.reportsMgmt.daoImpl.GameDataReportDaoImpl;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class GameDataReportControllerImpl {
	public List<GameDataReportBean> gameDataReportSearch(int gameId, int gameTypeId, String startDate, String endDate, String reportType, String merchantName) {

		List<GameDataReportBean> gameDataReportList = null;
		try {
			gameDataReportList = SportLotteryServiceIntegration.gameDataReportSearch(gameId, gameTypeId, startDate, endDate, reportType, merchantName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return gameDataReportList;
	}

	public Map<String, List<RetGameDataReportBean>> gameDataReportRetailerWise(int retailerOrgId, Date startDate, Date endDate) throws SLEException {
		Connection connection = null;
		GameDataReportDaoImpl daoImpl = new GameDataReportDaoImpl();
		Map<String, List<RetGameDataReportBean>> gameDataReportMap = null;
		Timestamp startTime = null;
		Timestamp endTime = null;
		try {
			startTime = new Timestamp(ReportUtility.getZeroTimeDate(startDate).getTime());
			endTime = new Timestamp(ReportUtility.getLastTimeDate(endDate).getTime());

			connection = DBConnect.getConnection();
			gameDataReportMap = daoImpl.gameDataReportRetailerWise(retailerOrgId, startTime, endTime, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return gameDataReportMap;
	}
}