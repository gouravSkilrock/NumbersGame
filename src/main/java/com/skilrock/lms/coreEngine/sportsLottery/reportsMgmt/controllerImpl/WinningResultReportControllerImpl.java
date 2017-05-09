package com.skilrock.lms.coreEngine.sportsLottery.reportsMgmt.controllerImpl;

import java.util.List;

import com.skilrock.lms.coreEngine.sportsLottery.beans.WinningResultReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;

public class WinningResultReportControllerImpl {

	public List<WinningResultReportBean> winningResultReportSearch(int gameId, int gameTypeId) throws SLEException {

		List<WinningResultReportBean> winningResultReportList = null;
		try {
			winningResultReportList = SportLotteryServiceIntegration.winningResultReportSearch(gameId, gameTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return winningResultReportList;
	}
}