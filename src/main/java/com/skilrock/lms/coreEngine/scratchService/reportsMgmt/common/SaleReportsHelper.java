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
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;

public class SaleReportsHelper {

	private Connection con = null;
	private Date endDate;
	private Date startDate;

	public SaleReportsHelper(DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		System.out.println(this.startDate + "  " + this.endDate);
	}

	public List<Integer> getAgentId() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			con = DBConnect.getConnection();
			// con=DBConnect.getConnection();
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_SALE_REPORT_GET_AGENT_ID());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			ResultSet rss = pstmt.executeQuery();
			System.out.println(" get agent id query : " + pstmt);
			while (rss.next()) {
				list.add(rss.getInt("agent_org_id"));
				// System.out.println("Agent Id ::::"
				// +rss.getInt("agent_org_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public String getAgentName(Connection conn, int agentid) {
		String aname = null;
		try {
			System.out.println("query in agentid--"
					+ QueryManager.getST_GET_ORG_NAME());
			PreparedStatement pstmt = conn.prepareStatement(QueryManager
					.getST_GET_ORG_NAME());
			pstmt.setInt(1, agentid);
			ResultSet rss = pstmt.executeQuery();
			while (rss.next()) {
				aname = rss.getString(TableConstants.NAME);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return aname;
	}

	private String getGameDetail(Connection conn, int gameid,
			SaleReportBean bean) throws SQLException {
		String gname = null;

		PreparedStatement pstmt = conn.prepareStatement(QueryManager
				.getST_GAME_NAME());
		pstmt.setInt(1, gameid);
		System.out.println("get game details by game id --" + pstmt);
		ResultSet rss = pstmt.executeQuery();
		while (rss.next()) {
			bean.setGamename(rss.getString("game_name"));
			bean.setTicketCost(rss.getDouble("ticket_price"));
			bean.setBookCost(FormatNumber.formatNumber(rss
					.getDouble("book_price")));
			bean.setTicketsPerBook(rss.getInt("nbr_of_tickets_per_book"));
		}

		return gname;
	}

	public List<Integer> getGameId() throws LMSException {
		List<Integer> list = new ArrayList<Integer>();
		try {
			con = DBConnect.getConnection();
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_SALE_REPORT_GET_GAME_ID());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			ResultSet rss = pstmt.executeQuery();
			System.out.println("ge Game id for sale details == " + pstmt);
			while (rss.next()) {
				list.add(rss.getInt("game_id"));
				// System.out.println("Game Id ::::" +rss.getInt("game_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<SaleReportBean> getSaleDetailAgentWise(List<Integer> idlist) {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		// con=DBConnect.getConnection();

		// System.out.println("query--"+QueryManager.getST_SALE_REPORT_GET_SALE_DETAIL());
		try {
			for (Integer agentOrgId : idlist) {
				PreparedStatement pstmt = con.prepareStatement(QueryManager
						.getST_SALE_REPORT_AGENT_WISE());
				pstmt.setDate(1, startDate);
				pstmt.setDate(2, endDate);
				pstmt.setInt(3, agentOrgId);
				pstmt.setDate(4, startDate);
				pstmt.setDate(5, endDate);
				pstmt.setInt(6, agentOrgId);

				ResultSet rs = pstmt.executeQuery();
				System.out.println("query -- " + pstmt);
				while (rs.next()) {
					reportbean = new SaleReportBean();
					reportbean.setSale(rs.getInt("books_sold"));
					reportbean.setSaleAmt(FormatNumber.formatNumber(rs
							.getDouble("net_sale_amt")));
					reportbean.setSaleMrp(FormatNumber.formatNumber(rs
							.getDouble("sale_books_mrp")));
					reportbean.setReturnAmt(FormatNumber.formatNumber(rs
							.getDouble("net_books_returned_amt")));
					reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
							.getDouble("return_books_mrp")));
					reportbean.setSalereturn(rs.getInt("books_returned"));
					reportbean.setNetAmt(FormatNumber.formatNumber(rs
							.getDouble("net_sale_amt")
							- rs.getDouble("net_books_returned_amt")));
					reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
							.getDouble("sale_books_mrp")
							- rs.getDouble("return_books_mrp")));
					reportbean.setName(getAgentName(con, agentOrgId));
					list.add(reportbean);
				}
				rs.close();
				pstmt.close();
			}
			System.out.println(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return list;
	}

	public List<SaleReportBean> getSaleDetailGameWise(List<Integer> idlist)
			throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		// con=DBConnect.getConnection();

		// System.out.println("
		// query--"+QueryManager.getST_SALE_REPORT_GET_SALE_DETAIL());
		try {
			for (Integer gameId : idlist) {
				PreparedStatement pstmt = con.prepareStatement(QueryManager
						.getST_SALE_REPORT_GAME_WISE());
				pstmt.setDate(1, startDate);
				pstmt.setDate(2, endDate);
				pstmt.setInt(3, gameId);
				pstmt.setDate(4, startDate);
				pstmt.setDate(5, endDate);
				pstmt.setInt(6, gameId);
				pstmt.setInt(7, gameId);
				System.out.println("get the sale detail query -- " + pstmt);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					reportbean = new SaleReportBean();
					reportbean.setSale(rs.getInt("books_sold"));
					// reportbean.setSaleAmt(FormatNumber.formatNumber(rs.getDouble("net_sale_amt")));
					// reportbean.setReturnAmt(FormatNumber.formatNumber(rs.getDouble("net_books_returned_amt")));
					reportbean.setSalereturn(rs.getInt("books_returned"));
					reportbean.setBoRemBooks(rs.getInt("remaining_books"));
					int currentRemBooks = reportbean.getSale()
							- reportbean.getSalereturn();
					// reportbean.setExistingBooks(reportbean.getBoRemBooks()+currentRemBooks);
					getGameDetail(con, gameId, reportbean);
					list.add(reportbean);
				}
				rs.close();
				pstmt.close();
			}
			System.out.println(list);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return list;
	}

}