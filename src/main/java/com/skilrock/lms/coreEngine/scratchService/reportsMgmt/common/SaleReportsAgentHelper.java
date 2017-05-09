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

public class SaleReportsAgentHelper implements ISaleReportsAgentHelper {

	private int agentOrgId;
	private int agentUserId;
	private Connection con = null;
	private Date endDate;
	private PreparedStatement pstmt = null;
	private Date startDate;

	public SaleReportsAgentHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.agentUserId = userInfoBean.getUserId();
		this.agentOrgId = userInfoBean.getUserOrgId();
		System.out.println("collecting dates : first --- " + startDate
				+ "  last date -- " + endDate);
	}

	public String getGameDetail(Connection conn, int gameid,
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

	public List<Integer> getGameId() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			con = DBConnect.getConnection();
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_AGENT_SALE_REPORT_GET_GAME_ID());
			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			pstmt.setInt(4, agentOrgId);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setInt(7, agentOrgId);
			pstmt.setDate(8, startDate);
			pstmt.setDate(9, endDate);
			pstmt.setInt(10, agentOrgId);
			pstmt.setDate(11, startDate);
			pstmt.setDate(12, endDate);
			
			ResultSet rss = pstmt.executeQuery();
			while (rss.next()) {
				list.add(rss.getInt("game_id"));
				// System.out.println("Game Id ::::" +rss.getInt("game_id"));
			}
			rss.close();
			pstmt.close();
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
		System.out.println("game id list : " + list);
		return list;
	}

	public SaleReportBean getPurchaseDetailWithBo() {
		SaleReportBean reportbean = null;
		try {
			con = DBConnect.getConnection();
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_SALE_REPORT_AGENT_WISE());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setInt(3, agentOrgId);
			pstmt.setDate(4, startDate);
			pstmt.setDate(5, endDate);
			pstmt.setInt(6, agentOrgId);
			
			pstmt.setDate(7, startDate);
			pstmt.setDate(8, endDate);
			pstmt.setInt(9, agentOrgId);
			pstmt.setDate(10, startDate);
			pstmt.setDate(11, endDate);
			pstmt.setInt(12, agentOrgId);
			
			
			System.out.println("query -- " + pstmt);
			ResultSet rs = pstmt.executeQuery();
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

			}
			rs.close();
			pstmt.close();
			System.out.println(reportbean);
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

		return reportbean;
	}

	public List<Integer> getRetailerId() {
		List<Integer> retailerList = new ArrayList<Integer>(0);
		try {
			con = DBConnect.getConnection();
			// con=DBConnect.getConnection();
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_SALE_REPORT_GET_RETAILER_ORG_ID());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setInt(3, agentOrgId);
			pstmt.setDate(4, startDate);
			pstmt.setDate(5, endDate);
			pstmt.setInt(6, agentOrgId);

			ResultSet rss = pstmt.executeQuery();
			System.out.println("get retailer id query is = " + pstmt);
			while (rss.next()) {
				retailerList.add(rss.getInt("retailer_org_id"));
				// System.out.println("Retailer Id ::::"
				// +rss.getInt("retailer_org_id"));
			}
			rss.close();
			pstmt.close();
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
		System.out.println("Retailer org id list : " + retailerList);
		return retailerList;
	}

	public String getRetailerName(Connection conn, int retailerOrgId) {
		String aname = null, query = QueryManager.getST_GET_ORG_NAME();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, retailerOrgId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				aname = rs.getString("name");
			}
		} catch (SQLException e) {
			try {
				pstmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		}

		return aname;
	}

	public List<SaleReportBean> getSaleDetailGameWise(List<Integer> idlist) {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		// con=DBConnect.getConnection();

		try {
			for (Integer gameId : idlist) {
				PreparedStatement pstmt = con.prepareStatement(QueryManager
						.getST_AGENT_SALE_REPORT_GAME_WISE());
				pstmt.setInt(1, agentOrgId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				pstmt.setInt(4, gameId);

				pstmt.setInt(5, agentOrgId);
				pstmt.setDate(6, startDate);
				pstmt.setDate(7, endDate);
				pstmt.setInt(8, gameId);

				pstmt.setInt(9, agentOrgId);
				pstmt.setDate(10, startDate);
				pstmt.setDate(11, endDate);
				pstmt.setInt(12, gameId);

				pstmt.setInt(13, agentOrgId);
				pstmt.setDate(14, startDate);
				pstmt.setDate(15, endDate);
				pstmt.setInt(16, gameId);

				pstmt.setInt(17, agentOrgId);
				pstmt.setDate(18, startDate);
				pstmt.setDate(19, endDate);
				pstmt.setInt(20, gameId);

				pstmt.setInt(21, agentOrgId);
				pstmt.setDate(22, startDate);
				pstmt.setDate(23, endDate);
				pstmt.setInt(24, gameId);

				pstmt.setInt(25, agentOrgId);
				pstmt.setDate(26, startDate);
				pstmt.setDate(27, endDate);
				pstmt.setInt(28, gameId);

				pstmt.setInt(29, agentOrgId);
				pstmt.setDate(30, startDate);
				pstmt.setDate(31, endDate);
				pstmt.setInt(32, gameId);
				
				pstmt.setInt(33, agentOrgId);
				pstmt.setInt(34, gameId);

				System.out.println("query -- " + pstmt);

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					reportbean = new SaleReportBean();
					reportbean.setSale(rs.getInt("books_purchase_from_bo"));
					reportbean.setSaleToRetailer(rs
							.getInt("books_sale_to_retailer"));
					reportbean.setSaleReturnByRetailer(rs
							.getInt("books_returned_by_retailer"));
					reportbean.setSalereturn(rs.getInt("books_returned_to_bo"));
					reportbean.setAgentsRemBooks(rs.getInt("remaining_books"));
					reportbean.setLooseSale(rs.getInt("tickets_purchase_from_bo"));
					reportbean.setLooseSaleReturn(rs.getInt("tickets_returned_to_bo"));
					reportbean.setLooseSaleToRetailer(rs.getInt("tickets_sale_to_retailer"));
					reportbean.setLooseSaleReturnByRetailer(rs.getInt("tickets_returned_by_retailer"));
					
					int currentRemBooks = reportbean.getSale()
							- reportbean.getSalereturn()
							- reportbean.getSaleToRetailer()
							+ reportbean.getSaleReturnByRetailer();
					reportbean.setExistingBooks(reportbean.getAgentsRemBooks()
							- currentRemBooks);
					// System.out.println("remaing books with
					// agent=======================
					// "+reportbean.getAgentsRemBooks());
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

	public List<SaleReportBean> getSaleDetailRetailerWise(List<Integer> idlist) {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();

		// getSale detail of retailer
		try {
			for (Integer RetailerOrgId : idlist) {
				PreparedStatement pstmt = con.prepareStatement(QueryManager
						.getST_SALE_REPORT_RETAILER_WISE());
				pstmt.setDate(1, startDate);
				pstmt.setDate(2, endDate);
				pstmt.setInt(3, agentOrgId);
				pstmt.setInt(4, RetailerOrgId);

				pstmt.setDate(5, startDate);
				pstmt.setDate(6, endDate);
				pstmt.setInt(7, agentOrgId);
				pstmt.setInt(8, RetailerOrgId);
				
				pstmt.setDate(9, startDate);
				pstmt.setDate(10, endDate);
				pstmt.setInt(11, agentOrgId);
				pstmt.setInt(12, RetailerOrgId);
				
				pstmt.setDate(13, startDate);
				pstmt.setDate(14, endDate);
				pstmt.setInt(15, agentOrgId);
				pstmt.setInt(16, RetailerOrgId);
				
				
				
				System.out.println("query -- " + pstmt);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					// reportbean=new SaleReportBean();
					// reportbean.setSale(rs.getInt("books_sold"));
					// reportbean.setSaleAmt(FormatNumber.formatNumber(rs.getDouble("net_sale_amt")));
					// reportbean.setReturnAmt(FormatNumber.formatNumber(rs.getDouble("net_books_returned_amt")));
					// reportbean.setSalereturn(rs.getInt("books_returned"));
					// reportbean.setNetAmt(FormatNumber.formatNumber(rs.getDouble("net_sale_amt")-rs.getDouble("net_books_returned_amt")));
					//					

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
					reportbean.setName(getRetailerName(con, RetailerOrgId));

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
				e.printStackTrace();
			}
		}

		return list;
	}

}
