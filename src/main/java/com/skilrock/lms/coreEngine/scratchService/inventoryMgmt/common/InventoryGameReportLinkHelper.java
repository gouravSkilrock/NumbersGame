package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.InventoryGameReportLinkBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;

public class InventoryGameReportLinkHelper {

	public List<InventoryGameReportLinkBean> getDetails(int gameid,
			String gamename, int gamenumber, String saleenddate) {
		ArrayList<InventoryGameReportLinkBean> gamebean = new ArrayList<InventoryGameReportLinkBean>();
		InventoryGameReportLinkBean inventoryBean = null;
		Connection con = DBConnect.getConnection();
		try {

			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_INVENTORY_GAME_SEARCH_LINK());

			pstmt.setInt(1, gameid);
			pstmt.setInt(2, gameid);
			pstmt.setInt(3, gameid);
			pstmt.setInt(4, gameid);

			ResultSet resultset = pstmt.executeQuery();
			while (resultset.next()) {

				inventoryBean = new InventoryGameReportLinkBean();
				inventoryBean.setGamenbr(gamenumber);
				inventoryBean.setGamename(gamename);
				inventoryBean.setSaleenddate(saleenddate);
				inventoryBean.setPurchaseFromBo(resultset
						.getInt("a.sold_by_bo"));
				inventoryBean.setReturnToBo(resultset
						.getInt("b.returned_to_bo")); // b.returned_to_bo,c.sold_by_agents,d.returned_to_agents
				inventoryBean.setSoldByAgent(resultset
						.getInt("c.sold_by_agents"));
				inventoryBean.setReturnToAgent(resultset
						.getInt("d.returned_to_agents"));
				gamebean.add(inventoryBean);
			}
			if (resultset != null) {
				resultset.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gamebean;
	}

}
