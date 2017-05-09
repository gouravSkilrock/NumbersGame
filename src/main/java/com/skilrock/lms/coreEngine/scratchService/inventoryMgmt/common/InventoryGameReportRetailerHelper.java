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

public class InventoryGameReportRetailerHelper {

	public static void main(String[] args) {

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
			System.out.println("hdkjkd");
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
				// bean.setPwtenddate(resultset.getDate(6));

				gamebean.add(bean);

			}

		} catch (Exception e) {
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

		return gamebean;
	}

	public List<InventoryGameReportBean> getInventoryGameReport(
			List<InventoryGameReportBean> gamebean, int ownerId) {
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

					pstmt = con.prepareStatement(QueryManager
							.getST_INVENTORY_GAME_REPORT_FOR_RETAILER());
					pstmt.setInt(1, ownerId);
					pstmt.setInt(2, gameIdBean.getGameid());
					pstmt.setInt(3, ownerId);
					pstmt.setInt(4, gameIdBean.getGameid());
					resultset = pstmt.executeQuery();
					while (resultset.next()) {
						inventoryBean = new InventoryGameReportBean();
						inventoryBean.setGamenbr(gameIdBean.getGamenbr());
						inventoryBean.setSaleenddate(gameIdBean
								.getSaleenddate());
						inventoryBean.setGamename(gameIdBean.getGamename());
						inventoryBean.setBookretailer(resultset
								.getInt("a.ret_count"));
						inventoryBean.setActivebooks(resultset
								.getInt("b.active_count"));

						System.out.print(inventoryBean.getGamenbr() + "\t");
						System.out.print(inventoryBean.getGamename() + "\t");
						System.out.print(inventoryBean.getSaleenddate() + "\t");
						System.out
								.print(inventoryBean.getBookretailer() + "\t");
						System.out.println(inventoryBean.getActivebooks());
						if (inventoryBean.getBookretailer() > 0) {
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
