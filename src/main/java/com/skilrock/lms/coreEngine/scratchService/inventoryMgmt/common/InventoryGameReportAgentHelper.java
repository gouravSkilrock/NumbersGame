package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.InventoryGameReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;

public class InventoryGameReportAgentHelper {

	public static void main(String[] args) {
		InventoryGameReportHelper helper = new InventoryGameReportHelper();
		System.out.println("--------------------");
		List<InventoryGameReportBean> gamereportbean = helper.getGameDetail(
				null, null, null);
		for (InventoryGameReportBean inventoryGameReportBean : gamereportbean) {
			System.out.print(inventoryGameReportBean.getGameid() + "\t");
		}
		System.out.println();
		// helper.getInventoryGameReport(gamereportbean);

	}

	private Connection con = null;
	private PreparedStatement pstmt = null;

	private ResultSet resultset = null;

	public List<InventoryGameReportBean> getGameDetail(String gamename,
			String gamenumber, String gamestatus) {
		ArrayList<InventoryGameReportBean> gamebean = new ArrayList<InventoryGameReportBean>();
		String query = QueryManager.getST_INVENTORY_GAME_SEARCH();
		if (gamename != null | gamenumber != null | gamestatus != null) {
			query = query + " where 1=1";
			if (gamename != null) {
				query = query + " and game_name LIKE '" + gamename + "%'";
			}
			if (gamenumber != null) {
				query = query + " and game_nbr LIKE '"
						+ Integer.parseInt(gamenumber) + "%'";
			}
			if (gamestatus != null) {
				query = query + " and game_status='" + gamestatus + "'";
			}
		}

		try {

			con = DBConnect.getConnection();
			System.out.println("connection " + con);
			pstmt = con.prepareStatement(query);
			resultset = pstmt.executeQuery();
			InventoryGameReportBean bean = null;
			while (resultset.next()) { // distinct(game_id), game_nbr,
				// game_name, sale_end_date
				bean = new InventoryGameReportBean();
				bean.setGameid(resultset.getInt(1));
				bean.setGamenbr(resultset.getInt(2));
				bean.setGamename(resultset.getString(3));
				// bean.setStartdate(resultset.getDate(4));
				bean.setSaleenddate(resultset.getDate(5));
				bean.setPwtenddate(resultset.getDate(6));
				bean.setGamestatus(resultset.getString("game_status"));
				System.out.println("game id : " + bean.getGameid());
				gamebean.add(bean);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return gamebean;
	}

	public List<InventoryGameReportBean> getInventoryGameReport(
			List<InventoryGameReportBean> gamebean, int agtId) {
		ArrayList<InventoryGameReportBean> gamelist = new ArrayList<InventoryGameReportBean>();

		try {
			InventoryGameReportBean inventoryBean = null;
			con = DBConnect.getConnection();
			List<InventoryGameReportBean> gameIdList = gamebean;
			String retailerOnline = (String) ServletActionContext
					.getServletContext().getAttribute("RET_ONLINE");

			if (retailerOnline != null
					&& "YES".equalsIgnoreCase(retailerOnline.trim())) {
				System.out.println(" retailer online status == "
						+ retailerOnline);
				for (InventoryGameReportBean gameIdBean : gameIdList) {

					pstmt = con
							.prepareStatement(QueryManager
									.getST_INVENTORY_GAME_REPORT_FOR_AGENT_RET_ONLINE());
					pstmt.setInt(1, agtId);
					pstmt.setInt(2, gameIdBean.getGameid());
					pstmt.setInt(3, agtId);
					pstmt.setInt(4, gameIdBean.getGameid());
					pstmt.setInt(5, agtId);
					pstmt.setInt(6, gameIdBean.getGameid());
					resultset = pstmt.executeQuery();
					System.out.println(" query========== " + pstmt);
					while (resultset.next()) {
						inventoryBean = new InventoryGameReportBean();
						inventoryBean.setGamename(gameIdBean.getGamename());
						inventoryBean.setGamenbr(gameIdBean.getGamenbr());
						// by yogesh
						inventoryBean.setGamestatus(gameIdBean.getGamestatus());
						inventoryBean.setBookagent(resultset
								.getInt("a.agt_count"));
						inventoryBean.setBookretailer(resultset
								.getInt("b.ret_count"));
						inventoryBean.setActivebooks(resultset
								.getInt("c.active_count"));
						inventoryBean.setSaleenddate(gameIdBean
								.getSaleenddate());
						inventoryBean.setPwtenddate(gameIdBean.getPwtenddate());
						System.out.println("PWT End Date 2 : == "
								+ inventoryBean.getPwtenddate());
						inventoryBean.setTotalbooks(resultset
								.getInt("a.agt_count")
								+ resultset.getInt("b.ret_count"));
						inventoryBean.setGamestatus(gameIdBean.getGamestatus());

						System.out
								.println("game No\tgame name\tsale End Date\tTotal Books\tBook Agent\tBook Retailer\tActive Book");
						System.out.print(inventoryBean.getGamenbr() + "\t");
						System.out.print(inventoryBean.getGamename() + "\t");
						System.out.print(inventoryBean.getSaleenddate() + "\t");
						System.out.print(inventoryBean.getTotalbooks() + "\t");
						System.out.print(inventoryBean.getBookagent() + "\t");
						System.out
								.print(inventoryBean.getBookretailer() + "\t");
						System.out.println(inventoryBean.getActivebooks());
						if (inventoryBean.getTotalbooks() > 0) {
							gamelist.add(inventoryBean);
						}
					}
				}
			} else {
				System.out.println(" retailer online status in else == "
						+ retailerOnline);
				for (InventoryGameReportBean gameIdBean : gameIdList) {

					pstmt = con.prepareStatement(QueryManager
							.getST_INVENTORY_GAME_REPORT_FOR_AGENT());
					pstmt.setInt(1, agtId);
					pstmt.setInt(2, gameIdBean.getGameid());
					pstmt.setInt(3, agtId);
					pstmt.setInt(4, gameIdBean.getGameid());
					resultset = pstmt.executeQuery();
					while (resultset.next()) {
						inventoryBean = new InventoryGameReportBean();
						inventoryBean.setGamename(gameIdBean.getGamename());
						inventoryBean.setGamenbr(gameIdBean.getGamenbr());
						// by yogesh
						inventoryBean.setGamestatus(gameIdBean.getGamestatus());
						inventoryBean.setPwtenddate(gameIdBean.getPwtenddate());
						System.out.println("PWT End Date 2 : == "
								+ inventoryBean.getPwtenddate());
						inventoryBean.setBookagent(resultset
								.getInt("a.agt_count")); // a.agt_count,b.ret_count
						inventoryBean.setBookretailer(resultset
								.getInt("b.ret_count"));
						// inventoryBean.setActivebooks(resultset.getInt("c.active_count"));
						inventoryBean.setSaleenddate(gameIdBean
								.getSaleenddate());
						inventoryBean.setTotalbooks(resultset
								.getInt("a.agt_count")
								+ resultset.getInt("b.ret_count"));
						System.out.println("Total books it have of '"
								+ inventoryBean.getGamename()
								+ "' game is : = "
								+ inventoryBean.getTotalbooks());
						if (inventoryBean.getTotalbooks() > 0) {
							gamelist.add(inventoryBean);
						}

					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return gamelist;
	}
}
