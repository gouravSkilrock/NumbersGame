package com.skilrock.lms.coreEngine.scratchService.orderMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;

public class ScratchGameDataDaoImpl {

	static Log logger = LogFactory.getLog(ScratchGameDataDaoImpl.class);

	public List<ScratchGameDataBean> getScratchGameData(Connection con)
			throws LMSException {
		logger
				.info("***** Inside getScratchGameData Method of ScratchGameDataDaoImpl Class");

		List<ScratchGameDataBean> scratchGameList = null;
		ScratchGameDataBean scratchGameBean = null;

		Statement stmt = null;
		ResultSet rs = null;

		String query = "select game_name, ticket_price, game_description, game_image_path"
				+ " from st_se_game_master where game_status in ('OPEN', 'SALE_CLOSE', 'SALE_HOLD')";

		try {
			stmt = con.createStatement();
			logger.info("Query is " + query);
			rs = stmt.executeQuery(query);

			scratchGameList = new ArrayList<ScratchGameDataBean>();

			while (rs.next()) {
				scratchGameBean = new ScratchGameDataBean();

				scratchGameBean.setGameName(rs.getString("game_name"));
				scratchGameBean.setTicketPrice(rs.getDouble("ticket_price"));
				scratchGameBean.setGameDescription(rs
						.getString("game_description"));
				scratchGameBean.setGameImagePath(rs
						.getString("game_description"));

				scratchGameList.add(scratchGameBean);
			}
			logger.info("Fetched Scrach Game List from db is "
					+ scratchGameList.toString());
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return scratchGameList;
	}
}
