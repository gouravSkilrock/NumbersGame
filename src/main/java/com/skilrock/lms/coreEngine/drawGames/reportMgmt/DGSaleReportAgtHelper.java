package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;

public class DGSaleReportAgtHelper {
	static Log logger = LogFactory.getLog(DGSaleReportAgtHelper.class);
	private Connection con = null;
	private Date endDate;
	private int OrgId;
	private Date startDate;

	public DGSaleReportAgtHelper() {

	}

	public DGSaleReportAgtHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.OrgId = userInfoBean.getUserOrgId();
		logger.debug(this.startDate + "  " + this.endDate);
	}

	public List<SaleReportBean> fetchDGSaleDetailGameWise() throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT_GAME_WISE_AGT());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setInt(3, OrgId);
			pstmt.setDate(4, startDate);
			pstmt.setDate(5, endDate);
			pstmt.setInt(6, OrgId);
			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();
			double mrpSale = 0.0;
			double mrpSaleRef = 0.0;
			double mrpAmt = 0.0;
			double netSale = 0.0;
			double netSaleRef = 0.0;
			double netAmt = 0.0;
			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setGamename(rs.getString("game_name"));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("SaleMrp")));
				mrpSale += rs.getDouble("SaleMrp");
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("RefundMrp")));
				mrpSaleRef += rs.getDouble("RefundMrp");
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("NetMrp")));
				mrpAmt += rs.getDouble("NetMrp");
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleNet")));
				netSale += rs.getDouble("SaleNet");
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("RefundNet")));
				netSaleRef += rs.getDouble("RefundNet");
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("NetNet")));
				netAmt += rs.getDouble("NetNet");
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();
			reportbean = new SaleReportBean();
			reportbean.setGamename("Total");
			reportbean.setSaleMrp(FormatNumber.formatNumber(mrpSale));
			reportbean.setSaleReturnMrp(FormatNumber.formatNumber(mrpSaleRef));
			reportbean.setNetMrpAmt(FormatNumber.formatNumber(mrpAmt));
			reportbean.setSaleAmt(FormatNumber.formatNumber(netSale));
			reportbean.setReturnAmt(FormatNumber.formatNumber(netSaleRef));
			reportbean.setNetAmt(FormatNumber.formatNumber(netAmt));
			list.add(reportbean);
			logger.debug("size of list = " + list);

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return list;
	}

	public List<SaleReportBean> fetchDGSaleDetailRetailerWise()
			throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT_RETAILER_WISE_AGT());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setInt(5, OrgId);
			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();
			double mrpSale = 0.0;
			double mrpSaleRef = 0.0;
			double mrpAmt = 0.0;
			double netSale = 0.0;
			double netSaleRef = 0.0;
			double netAmt = 0.0;
			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setRetName(rs.getString("ret_name"));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("SaleMrp")));
				mrpSale += rs.getDouble("SaleMrp");
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("RefundMrp")));
				mrpSaleRef += rs.getDouble("RefundMrp");
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("NetMrp")));
				mrpAmt += rs.getDouble("NetMrp");
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleNet")));
				netSale += rs.getDouble("SaleNet");
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("RefundNet")));
				netSaleRef += rs.getDouble("RefundNet");
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("NetNet")));
				netAmt += rs.getDouble("NetNet");
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();
			reportbean = new SaleReportBean();
			reportbean.setRetName("Total");
			reportbean.setSaleMrp(FormatNumber.formatNumber(mrpSale));
			reportbean.setSaleReturnMrp(FormatNumber.formatNumber(mrpSaleRef));
			reportbean.setNetMrpAmt(FormatNumber.formatNumber(mrpAmt));
			reportbean.setSaleAmt(FormatNumber.formatNumber(netSale));
			reportbean.setReturnAmt(FormatNumber.formatNumber(netSaleRef));
			reportbean.setNetAmt(FormatNumber.formatNumber(netAmt));
			list.add(reportbean);
			logger.debug("size of list = " + list);
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return list;
	}

	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgAdd;
	}

}
