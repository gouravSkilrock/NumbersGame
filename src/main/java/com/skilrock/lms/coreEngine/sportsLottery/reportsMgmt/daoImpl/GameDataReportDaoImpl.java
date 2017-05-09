package com.skilrock.lms.coreEngine.sportsLottery.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.coreEngine.sportsLottery.beans.GameTypeMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.RetGameDataReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils;

public class GameDataReportDaoImpl {
	static Log logger = LogFactory.getLog(GameDataReportDaoImpl.class);

	public Map<String, List<RetGameDataReportBean>> gameDataReportRetailerWise(int retailerOrgId, Timestamp startTime, Timestamp endTime, Connection connection) throws SLEException {

		Statement stmt = null;
		ResultSet rs = null;
		final Map<String, List<RetGameDataReportBean>> gameDataReportMap;
		RetGameDataReportBean gameDataReportBean = null;
		final StringBuilder queryBuilder = new StringBuilder();
		final String query;
		try {
			Map<Integer, GameTypeMasterBean> GameTypeMasterMap = SportsLotteryUtils.gameTypeInfoMap;
			int gameId = 0;
			int gameTypeId = 0;
			queryBuilder.append("SELECT aaa.retailer_org_id, (SELECT game_disp_name FROM st_sle_game_master WHERE game_id=aaa.game_id) gameName, (SELECT type_disp_name FROM st_sle_game_type_master WHERE game_type_id=aaa.game_type_id) gameTypeName, SUM(saleAmount) saleAmount, SUM(pwtAmount) pwtAmount FROM ( ");
			for(GameTypeMasterBean gameTypeMasterBean : GameTypeMasterMap.values()) {
				gameId = gameTypeMasterBean.getGameId();
				gameTypeId = gameTypeMasterBean.getGameTypeId();
				queryBuilder
						.append("SELECT "+retailerOrgId+" retailer_org_id, "+gameId+" game_id, "+gameTypeId+" game_type_id, IFNULL(SUM(mrp_amt),0) saleAmount, 0.0 pwtAmount FROM st_sle_ret_sale_").append(gameId).append(" WHERE game_id=").append(gameId).append(" AND game_type_id=").append(gameTypeId).append(" AND retailer_org_id=").append(retailerOrgId).append(" AND is_cancel='N' AND transaction_date>='").append(startTime).append("' AND transaction_date<='").append(endTime).append("'")
						.append(" UNION ALL ")
						.append("SELECT "+retailerOrgId+" retailer_org_id, "+gameId+" game_id, "+gameTypeId+" game_type_id, 0.0 saleAmount, IFNULL(SUM(pwt_amt),0) pwtAmount FROM st_sle_ret_pwt_").append(gameId).append(" WHERE game_id=").append(gameId).append(" AND game_type_id=").append(gameTypeId).append(" AND retailer_org_id=").append(retailerOrgId).append(" AND transaction_date>='").append(startTime).append("' AND transaction_date<='").append(endTime).append("'")
						.append(" UNION ALL ");
			}
			query = queryBuilder.substring(0, queryBuilder.lastIndexOf(" UNION ALL ")).concat(")aaa RIGHT JOIN (SELECT "+retailerOrgId+" retailer_org_id)bbb ON aaa.retailer_org_id=bbb.retailer_org_id GROUP BY game_id, game_type_id;");

			gameDataReportMap = new HashMap<String, List<RetGameDataReportBean>>();
			stmt = connection.createStatement();
			logger.info("GameDataReportQuery - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				String gameName = rs.getString("gameName");
				gameDataReportBean = new RetGameDataReportBean();
				gameDataReportBean.setRetailerId(rs.getInt("retailer_org_id"));
				gameDataReportBean.setGameName(gameName);
				gameDataReportBean.setGameTypeName(rs.getString("gameTypeName"));
				gameDataReportBean.setSaleAmount(rs.getDouble("saleAmount"));
				gameDataReportBean.setPwtAmount(rs.getDouble("pwtAmount"));
				if(!gameDataReportMap.containsKey(gameName)) {
					gameDataReportMap.put(gameName, new ArrayList<RetGameDataReportBean>());
				}
				gameDataReportMap.get(gameName).add(gameDataReportBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return gameDataReportMap;
	}
}