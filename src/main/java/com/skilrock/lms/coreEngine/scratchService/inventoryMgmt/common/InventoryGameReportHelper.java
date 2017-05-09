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

public class InventoryGameReportHelper {

	public static void main(String[] args) {
		InventoryGameReportHelper helper = new InventoryGameReportHelper();
		System.out.println("--------------------");
		List<InventoryGameReportBean> gamereportbean = helper.getGameDetail(
				null, null, null);
		for (InventoryGameReportBean inventoryGameReportBean : gamereportbean) {
			System.out.print(inventoryGameReportBean.getGameid() + "\t");
		}
		System.out.println();
		helper.getInventoryGameReport(gamereportbean);

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
		query = query + " order by game_nbr";

		try {
			System.out.println("=======================" + query);
			con = DBConnect.getConnection();
			System.out.println("connection " + con);
			pstmt = con.prepareStatement(query);
			resultset = pstmt.executeQuery();
			InventoryGameReportBean bean = null;
			while (resultset.next()) { // distinct(game_id), game_nbr,
				// game_name, sale_end_date
				bean = new InventoryGameReportBean();
				bean.setGameid(resultset.getInt(1));
				bean.setGamenbr(resultset.getInt("game_nbr"));
				System.out.println("inside game detail : "
						+ resultset.getInt("game_nbr"));
				bean.setGamename(resultset.getString("game_name"));
				// bean.setStartdate(resultset.getDate("start_date"));
				bean.setSaleenddate(resultset.getDate("sale_end_date"));
				bean.setPwtenddate(resultset.getDate("pwt_end_date"));
				System.out.println("PWT End Date : == " + bean.getPwtenddate());
				bean.setGamestatus(resultset.getString("game_status"));
				System.out.println("gmae status ==== " + bean.getGamestatus());
				gamebean.add(bean);

			}
			resultset.close();
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
			List<InventoryGameReportBean> gamebean) {
		ArrayList<InventoryGameReportBean> gamelist = new ArrayList<InventoryGameReportBean>(
				0);

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
					inventoryBean = new InventoryGameReportBean();
					pstmt = con.prepareStatement(QueryManager
							.getST_INVENTORY_GAME_REPORT_RET_ONLINE());
					pstmt.setInt(1, gameIdBean.getGameid());
					pstmt.setInt(2, gameIdBean.getGameid());
					pstmt.setInt(3, gameIdBean.getGameid());
					pstmt.setInt(4, gameIdBean.getGameid());
					resultset = pstmt.executeQuery();
					while (resultset.next()) {
						inventoryBean.setGameid(gameIdBean.getGameid());
						System.out.print(inventoryBean.getGameid());
						inventoryBean.setGamename(gameIdBean.getGamename());
						System.out.print(inventoryBean.getGamename() + "\t");
						inventoryBean.setGamenbr(gameIdBean.getGamenbr());
						System.out.print(inventoryBean.getGamenbr() + "\t");
						inventoryBean.setBookbo(resultset.getInt("a.bo_count")); // a.bo_count,b.agt_count,c.ret_count
						System.out.print(inventoryBean.getBookbo() + "\t");
						inventoryBean.setBookagent(resultset
								.getInt("b.agt_count"));
						System.out.print(inventoryBean.getBookagent() + "\t");
						inventoryBean.setBookretailer(resultset
								.getInt("c.ret_count"));
						System.out
								.print(inventoryBean.getBookretailer() + "\t");
						inventoryBean.setActivebooks(resultset
								.getInt("d.active_count"));
						System.out.print(inventoryBean.getActivebooks());
						inventoryBean.setSaleenddate(gameIdBean
								.getSaleenddate());
						System.out.println(inventoryBean.getSaleenddate()
								+ "\t");
						inventoryBean.setPwtenddate(gameIdBean.getPwtenddate());
						System.out.println("PWT End Date 2 : == "
								+ inventoryBean.getPwtenddate());
						inventoryBean.setTotalbooks(resultset
								.getInt("a.bo_count")
								+ resultset.getInt("b.agt_count")
								+ resultset.getInt("c.ret_count"));
						System.out.print(inventoryBean.getActivebooks());
						inventoryBean.setGamestatus(gameIdBean.getGamestatus());
						System.out
								.println("game status details ===================== "
										+ inventoryBean.getGamestatus());
						if (inventoryBean.getTotalbooks() > 0) {
							gamelist.add(inventoryBean);
						}
					}
				}
			} else {
				System.out.println(" retailer online status in else == "
						+ retailerOnline);
				for (InventoryGameReportBean gameIdBean : gameIdList) {
					inventoryBean = new InventoryGameReportBean();
					pstmt = con.prepareStatement(QueryManager
							.getST_INVENTORY_GAME_REPORT());
					pstmt.setInt(1, gameIdBean.getGameid());
					pstmt.setInt(2, gameIdBean.getGameid());
					pstmt.setInt(3, gameIdBean.getGameid());
					resultset = pstmt.executeQuery();

					while (resultset.next()) {
						inventoryBean.setGameid(gameIdBean.getGameid());
						System.out.print(inventoryBean.getGameid());
						inventoryBean.setGamename(gameIdBean.getGamename());
						System.out.print(inventoryBean.getGamename() + "\t");
						inventoryBean.setGamenbr(gameIdBean.getGamenbr());
						System.out.print(inventoryBean.getGamenbr() + "\t");
						inventoryBean.setBookbo(resultset.getInt("a.bo_count")); // a.bo_count,b.agt_count,c.ret_count
						System.out.print(inventoryBean.getBookbo() + "\t");
						inventoryBean.setBookagent(resultset
								.getInt("b.agt_count"));
						System.out.print(inventoryBean.getBookagent() + "\t");
						inventoryBean.setBookretailer(resultset
								.getInt("c.ret_count"));
						System.out
								.print(inventoryBean.getBookretailer() + "\t");
						inventoryBean.setSaleenddate(gameIdBean
								.getSaleenddate());
						System.out.print(inventoryBean.getSaleenddate() + "\t");
						inventoryBean.setPwtenddate(gameIdBean.getPwtenddate());
						System.out.println("PWT End Date 2 : == "
								+ inventoryBean.getPwtenddate());
						inventoryBean.setTotalbooks(resultset
								.getInt("a.bo_count")
								+ resultset.getInt("b.agt_count")
								+ resultset.getInt("c.ret_count"));
						System.out.print(inventoryBean.getActivebooks());
						inventoryBean.setGamestatus(gameIdBean.getGamestatus());
						System.out
								.println("game status details ===================== "
										+ inventoryBean.getGamestatus());

						// inventoryBean.setSaleenddate(gameIdBean.getSaleenddate());
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
				if (resultset != null) {
					resultset.close();
				}
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
