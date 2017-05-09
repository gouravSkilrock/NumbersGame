package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.DirPlrPwtRepBean;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;

public class DGPwtReportAgtHelper {
	static Log logger = LogFactory.getLog(DGPwtReportAgtHelper.class);
	private Connection con = null;
	private Timestamp end;
	private Date endDate;
	private int OrgId;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Timestamp start;
	private Date startDate;

	public DGPwtReportAgtHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		start = new Timestamp(startDate.getTime());
		this.endDate = dateBeans.getLastdate();
		end = new Timestamp(endDate.getTime());
		this.OrgId = userInfoBean.getUserOrgId();
	}

	public List<DirPlrPwtRepBean> getDirectPlrPwtGameWise() {
		List<DirPlrPwtRepBean> amtList = new ArrayList<DirPlrPwtRepBean>();
		con = DBConnect.getConnection();
		DirPlrPwtRepBean bean = null;
		DirPlrPwtRepBean totalBean = null;
		double totMrpPwt = 0.0;
		double totNetPwt = 0.0;
		double totProfit = 0.0;
		try {
			// String dirPlrRepQuery =
			// QueryManager.getST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT();
			String dirPlrRepQuery = QueryManager
					.getST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT_NEW();
			PreparedStatement pstmt = con.prepareStatement(dirPlrRepQuery);
			pstmt.setDate(1, new java.sql.Date(start.getTime()));
			pstmt.setDate(2, new java.sql.Date(end.getTime()));
			pstmt.setInt(3, OrgId);
			/*
			 * pstmt.setDate(4, new java.sql.Date(start.getTime()));
			 * pstmt.setDate(5, new java.sql.Date(end.getTime()));
			 * pstmt.setInt(6, OrgId); pstmt.setDate(7, new
			 * java.sql.Date(start.getTime())); pstmt.setDate(8, new
			 * java.sql.Date(end.getTime())); pstmt.setInt(9, OrgId);
			 * pstmt.setDate(10, new java.sql.Date(start.getTime()));
			 * pstmt.setDate(11, new java.sql.Date(end.getTime()));
			 * pstmt.setInt(12, OrgId);
			 */
			ResultSet rs1 = pstmt.executeQuery();
			logger.debug("get Direct Player PWT Report Game Wise for Agent"
					+ pstmt);

			while (rs1.next()) {
				bean = new DirPlrPwtRepBean();
				/*
				 * bean.setGameName(rs1.getString("name"));
				 * bean.setPwtAmtClm(rs1.getDouble("tpc"));
				 * bean.setNetAmtClm(rs1.getDouble("tnc"));
				 * bean.setProfitClm(rs1.getDouble("tpc") -
				 * rs1.getDouble("tnc"));
				 * bean.setPwtAmtUnclm(rs1.getDouble("tpu"));
				 * bean.setNetAmtUnclm(rs1.getDouble("tnu"));
				 * bean.setProfitUnclm(rs1.getDouble("tpu") -
				 * rs1.getDouble("tnu"));
				 */
				bean.setGameName(rs1.getString("game_name"));
				bean.setPwtAmtClm(rs1.getDouble("MrpPwt"));
				totMrpPwt += rs1.getDouble("MrpPwt");
				bean.setNetAmtClm(rs1.getDouble("NetPwt"));
				totNetPwt += rs1.getDouble("NetPwt");
				bean.setProfitClm(rs1.getDouble("NetPwt")
						- rs1.getDouble("MrpPwt"));
				totProfit += rs1.getDouble("NetPwt") - rs1.getDouble("MrpPwt");
				amtList.add(bean);
			}
			totalBean = new DirPlrPwtRepBean();
			totalBean.setGameName("Total");
			totalBean.setPwtAmtClm(totMrpPwt);
			totalBean.setNetAmtClm(totNetPwt);
			totalBean.setProfitClm(totProfit);
			amtList.add(totalBean);

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
		return amtList;
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

	public List<PwtReportBean> getPwtDetailGameWise() {

		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		con = DBConnect.getConnection();
		PwtReportBean reportbean = null;
		PwtReportBean totalBean = null;
		double totMrpPwt = 0.0;
		double totNetPwt = 0.0;
		double totProfit = 0.0;
		try {
			// PreparedStatement
			// pst=con.prepareStatement(QueryManager.getST_DG_PWT_REPORT_GAME_WISE_AGT());
			PreparedStatement pst = con.prepareStatement(QueryManager
					.getST_DG_PWT_REPORT_GAME_WISE_AGT_NEW());
			pst.setDate(1, startDate);
			pst.setDate(2, endDate);
			pst.setInt(3, OrgId);
			/*
			 * pst.setDate(4, startDate); pst.setDate(5, endDate); pst.setInt(6,
			 * OrgId); pst.setDate(7, startDate); pst.setDate(8, endDate);
			 * pst.setInt(9, OrgId); pst.setDate(10, startDate); pst.setDate(11,
			 * endDate); pst.setInt(12, OrgId);
			 */
			rs = pst.executeQuery();
			logger.debug("get Player PWT query : == " + pst);
			while (rs.next()) {
				reportbean = new PwtReportBean();
				/*
				 * reportbean.setClmMrp((FormatNumber.formatNumber(rs.getDouble("agtMrpClaimed"))));
				 * reportbean.setClmNet((FormatNumber.formatNumber(rs.getDouble("agtNetClaimed"))));
				 * reportbean.setClmProfit((FormatNumber.formatNumber(rs.getDouble("agtNetClaimed") -
				 * rs.getDouble("agtMrpClaimed"))));
				 * reportbean.setUnclmMrp((FormatNumber.formatNumber(rs.getDouble("agtMrpUnclm"))));
				 * reportbean.setUnclmNet((FormatNumber.formatNumber(rs.getDouble("agtNetUnclm"))));
				 * reportbean.setUnclmProfit((FormatNumber.formatNumber(rs.getDouble("agtNetUnclm") -
				 * rs.getDouble("agtMrpUnclm"))));
				 * reportbean.setGameName(rs.getString("gamename"));
				 */
				reportbean.setGameName(rs.getString("gamename"));
				reportbean.setClmMrp(FormatNumber.formatNumber(rs
						.getDouble("MrpPwt")));
				totMrpPwt += rs.getDouble("MrpPwt");
				reportbean.setClmNet(FormatNumber.formatNumber(rs
						.getDouble("NetPwt")));
				totNetPwt += rs.getDouble("NetPwt");
				reportbean.setClmProfit(FormatNumber.formatNumber(rs
						.getDouble("NetPwt")
						- rs.getDouble("MrpPwt")));
				totProfit += rs.getDouble("NetPwt") - rs.getDouble("MrpPwt");
				list.add(reportbean);
			}
			totalBean = new PwtReportBean();
			totalBean.setGameName("Total");
			totalBean.setClmMrp(FormatNumber.formatNumber(totMrpPwt));
			totalBean.setClmNet(FormatNumber.formatNumber(totNetPwt));
			totalBean.setClmProfit(FormatNumber.formatNumber(totProfit));
			list.add(totalBean);

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<PwtReportBean> getPwtDetailRetailerWise() {

		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		PwtReportBean reportbean = null;
		PwtReportBean totalBean = null;
		double totMrpPwt = 0.0;
		double totNetPwt = 0.0;
		double totProfit = 0.0;
		con = DBConnect.getConnection();

		try {

			/* pstmt=con.prepareStatement(QueryManager.getST_DG_PWT_REPORT_RETAILER_WISE_AGT()); */
			pstmt = con.prepareStatement(QueryManager
					.getST_DG_PWT_REPORT_RETAILER_WISE_AGT_NEW());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setInt(3, OrgId);
			/*
			 * pstmt.setDate(4, startDate); pstmt.setDate(5, endDate);
			 * pstmt.setInt(6, OrgId);
			 */
			rs = pstmt.executeQuery();
			logger.debug(" get pwt details query- ==== -" + pstmt);
			while (rs.next()) {
				reportbean = new PwtReportBean();
				/*
				 * reportbean.setClmMrp((FormatNumber.formatNumber(rs.getDouble("pwtMrpClm"))));
				 * reportbean.setClmNet((FormatNumber.formatNumber(rs.getDouble("pwtNetClm"))));
				 * reportbean.setClmProfit((FormatNumber.formatNumber(rs.getDouble("pwtNetClm") -
				 * rs.getDouble("pwtMrpClm"))));
				 * reportbean.setUnclmMrp((FormatNumber.formatNumber(rs.getDouble("pwtMrpUnclm"))));
				 * reportbean.setUnclmNet((FormatNumber.formatNumber(rs.getDouble("pwtNetUnclm"))));
				 * reportbean.setUnclmProfit((FormatNumber.formatNumber(rs.getDouble("pwtNetUnclm") -
				 * rs.getDouble("pwtMrpUnclm"))));
				 */
				reportbean.setRetName(rs.getString("name"));
				reportbean.setClmMrp(FormatNumber.formatNumber(rs
						.getDouble("pwtMrp")));
				totMrpPwt += rs.getDouble("pwtMrp");
				reportbean.setClmNet(FormatNumber.formatNumber(rs
						.getDouble("pwtNet")));
				totNetPwt += rs.getDouble("pwtNet");
				reportbean.setClmProfit(FormatNumber.formatNumber(rs
						.getDouble("pwtNet")
						- rs.getDouble("pwtMrp")));
				totProfit += rs.getDouble("pwtNet") - rs.getDouble("pwtMrp");
				list.add(reportbean);
			}
			totalBean = new PwtReportBean();
			totalBean.setRetName("Total");
			totalBean.setClmMrp(FormatNumber.formatNumber(totMrpPwt));
			totalBean.setClmNet(FormatNumber.formatNumber(totNetPwt));
			totalBean.setClmProfit(FormatNumber.formatNumber(totProfit));
			list.add(totalBean);

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
		return list;
	}
}
