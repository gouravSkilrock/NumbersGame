package com.skilrock.lms.web.sportsLottery.common.serviceImpl;

import java.sql.Connection;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils;

public class CommonMethodServiceImpl {
	public Map<Integer, GameMasterBean> getGameMap() throws SLEException {
		Connection connection = DBConnect.getConnection();
		Map<Integer, GameMasterBean> gameMap = SportsLotteryUtils.getGameMap(connection);
		DBConnect.closeCon(connection);
		return gameMap;
	}
}