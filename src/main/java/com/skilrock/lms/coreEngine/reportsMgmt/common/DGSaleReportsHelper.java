package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;

public class DGSaleReportsHelper {
	private Connection con = null;
	private Date endDate;
	Log logger = LogFactory.getLog(DGSaleReportsHelper.class);
	private Date startDate;

	public DGSaleReportsHelper(DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		logger.debug(this.startDate + "  " + this.endDate);
	}

	public List<SaleReportBean> fetchDGSaleDetail() throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setDate(7, startDate);
			pstmt.setDate(8, endDate);
			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_net")));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_mrp")));
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_refund_net")));
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("sum_refund_mrp")));
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_net")
						- rs.getDouble("sum_refund_net")));
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_mrp")
						- rs.getDouble("sum_refund_mrp")));
				reportbean.setName(rs.getString("name"));
				reportbean.setGamename(rs.getString("game_name"));
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();

			logger.debug("size of list = " + list);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return list;
	}

	public List<SaleReportBean> fetchDGSaleDetailAgentWise()
			throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT_AGENT_WISE());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setDate(7, startDate);
			pstmt.setDate(8, endDate);
			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_net")));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_mrp")));
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_refund_net")));
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("sum_refund_mrp")));
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_net")
						- rs.getDouble("sum_refund_net")));
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_mrp")
						- rs.getDouble("sum_refund_mrp")));
				reportbean.setName(rs.getString("name"));
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();

			logger.debug("size of list = " + list);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return list;
	}

	public List<SaleReportBean> fetchDGSaleDetailGameWise() throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT_GAME_WISE());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setDate(7, startDate);
			pstmt.setDate(8, endDate);
			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_net")));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_mrp")));
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_refund_net")));
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("sum_refund_mrp")));
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_net")
						- rs.getDouble("sum_refund_net")));
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("sum_sale_mrp")
						- rs.getDouble("sum_refund_mrp")));
				reportbean.setGamename(rs.getString("game_name"));
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();

			logger.debug("size of list = " + list);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return list;
	}

}