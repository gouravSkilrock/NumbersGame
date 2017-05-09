package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.utility.FormatNumber;

public class SaleReportRetHelper {

	private Connection con = null;
	private Date endDate;
	private int parOrgId;
	private int retOrgId;
	private Date startDate;

	public SaleReportRetHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.retOrgId = userInfoBean.getUserOrgId();
		this.parOrgId = userInfoBean.getParentOrgId();
		System.out.println("collecting dates : first --- " + startDate
				+ "  last date -- " + endDate);
	}

	private String getGameDetail(Connection conn, int gameid,
			SaleReportBean bean) {
		String gname = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(QueryManager
					.getST_GAME_NAME());
			pstmt.setInt(1, gameid);
			System.out.println("query in game id--" + pstmt);
			ResultSet rss = pstmt.executeQuery();
			while (rss.next()) {
				bean.setGamename(rss.getString("game_name"));
				bean.setTicketCost(rss.getDouble("ticket_price"));
				bean.setBookCost(FormatNumber.formatNumber(rss
						.getDouble("book_price")));
				bean.setTicketsPerBook(rss.getInt("nbr_of_tickets_per_book"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return gname;
	}

	public List<Integer> getGameId(Connection conn) {
		List<Integer> list = new ArrayList<Integer>();
		try {
			String getGameIdList = "select distinct bo.game_id from st_se_agent_retailer_transaction bo, "
					+ " st_lms_agent_transaction_master btm where btm.transaction_id = bo.transaction_id  "
					+ " and btm.user_org_id=?  and ( btm.transaction_date>=? and btm.transaction_date<?) "
					+ " and bo.retailer_org_id = ? order by bo.game_id ";
			PreparedStatement pstmt = conn.prepareStatement(getGameIdList);
			pstmt.setInt(1, parOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			pstmt.setInt(4, retOrgId);
			ResultSet rss = pstmt.executeQuery();
			while (rss.next()) {
				list.add(rss.getInt("game_id"));
			}
			rss.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("game id list : " + list);
		return list;
	}

	public List<SaleReportBean> getSaleDetailGameWise() {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();

		List<Integer> idlist = getGameId(con);

		String gameWiseReportForRet = "select ifnull(e.ee,0) 'remaining_books', ifnull(c.cc,0) 'books_sale_to_retailer'"
				+ ", ifnull(d.dd,0) 'books_returned_by_retailer' from ( ( select sum(nbr_of_books) cc from "
				+ " st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id = "
				+ " bo.transaction_id and bo.transaction_type ='SALE' and btm.user_org_id=? and retailer_org_id = ? "
				+ " and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =? ) c, (select sum(nbr_of_books)"
				+ " dd from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id = "
				+ " bo.transaction_id and bo.transaction_type ='SALE_RET' and btm.user_org_id=? and retailer_org_id = ? "
				+ " and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =? )d, ( select count(book_nbr)"
				+ " ee from st_se_game_inv_status where current_owner='RETAILER' and current_owner_id=? and game_id=?) e)";
		try {
			for (Integer gameId : idlist) {
				PreparedStatement pstmt = con
						.prepareStatement(gameWiseReportForRet);
				pstmt.setInt(1, parOrgId);
				pstmt.setInt(2, retOrgId);
				pstmt.setDate(3, startDate);
				pstmt.setDate(4, endDate);
				pstmt.setInt(5, gameId);

				pstmt.setInt(6, parOrgId);
				pstmt.setInt(7, retOrgId);
				pstmt.setDate(8, startDate);
				pstmt.setDate(9, endDate);
				pstmt.setInt(10, gameId);

				pstmt.setInt(11, retOrgId);
				pstmt.setInt(12, gameId);

				System.out.println("query -- " + pstmt);

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					reportbean = new SaleReportBean();
					reportbean.setSaleToRetailer(rs
							.getInt("books_sale_to_retailer"));
					reportbean.setSaleReturnByRetailer(rs
							.getInt("books_returned_by_retailer"));
					reportbean.setAgentsRemBooks(rs.getInt("remaining_books"));
					int currentRemBooks = -reportbean.getSaleToRetailer()
							+ reportbean.getSaleReturnByRetailer();
					reportbean.setExistingBooks(reportbean.getAgentsRemBooks()
							- currentRemBooks);
					getGameDetail(con, gameId, reportbean);
					list.add(reportbean);
				}
				rs.close();
				pstmt.close();
				System.out.println(list);
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

		return list;
	}

}
