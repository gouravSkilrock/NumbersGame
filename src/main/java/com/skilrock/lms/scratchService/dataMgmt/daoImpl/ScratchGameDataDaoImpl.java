package com.skilrock.lms.scratchService.dataMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;

public class ScratchGameDataDaoImpl {

	static Log logger = LogFactory.getLog(ScratchGameDataDaoImpl.class);

	private static ScratchGameDataDaoImpl singleInstance;
	private ScratchGameDataDaoImpl(){}
	 public static ScratchGameDataDaoImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (ScratchGameDataDaoImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new ScratchGameDataDaoImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	 
	 
	public List<ScratchGameDataBean> getScratchGameData(Connection con) throws SQLException{
		logger
				.info("***** Inside getScratchGameData Method of ScratchGameDataDaoImpl Class");

		List<ScratchGameDataBean> scratchGameList = null;
		ScratchGameDataBean scratchGameBean = null;

		Statement stmt = null;
		ResultSet rs = null;

		String query = "select game_name, ticket_price, game_description, game_image_path"
				+ " from st_se_game_master where game_status in ('OPEN', 'SALE_CLOSE', 'SALE_HOLD')";

		
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
						.getString("game_image_path"));

				scratchGameList.add(scratchGameBean);
			}
			logger.info("Fetched Scrach Game List from db is "
					+ scratchGameList.toString());
		
		return scratchGameList;
	}
	
	public int getWarehouseNbrForBook(Connection connection, int gameId, String bookNbr) throws SQLException {
		String bookQuery = null;
		PreparedStatement bookStmt = null;
		int warehouseNbr = -1;

		bookQuery = QueryManager.getST1WarehouseForBook();
		bookStmt = connection.prepareStatement(bookQuery);

		bookStmt.setInt(1, gameId);
		bookStmt.setString(2, bookNbr);

		ResultSet rs = bookStmt.executeQuery();
		while (rs.next()) {
			warehouseNbr = rs.getInt(TableConstants.SGIS_WAREHOUSE_ID);
		}
		return warehouseNbr;
	}
	
}
