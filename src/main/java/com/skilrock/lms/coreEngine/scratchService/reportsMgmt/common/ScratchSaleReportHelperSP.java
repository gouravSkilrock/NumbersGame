package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class ScratchSaleReportHelperSP implements IScratchSaleReportHelper{
	Log logger = LogFactory.getLog(ScratchSaleReportHelperSP.class);

	public List<SalePwtReportsBean> scratchSaleAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			logger.info("----Type Select ---"
					+ LMSFilterDispatcher.seSaleReportType);
			pstmt = con.prepareCall("{call scratchSaleAgentWise(?,?)}");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			logger.info("----Agent Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> scratchSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId)
			throws SQLException {
		Connection con = DBConnect.getConnection();
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			logger.info("----Type Select ---"
					+ LMSFilterDispatcher.seSaleReportType);
			
			pstmt = con.prepareCall("{call scratchSaleAgentWiseExpand(?,?,?)}");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agentOrgId);
			logger.info("----Agent Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)){
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					reportsBean.setNoOfLosTkt(rs.getInt("noOfLosTkts"));
					beanList.add(reportsBean);
				}
			}else if ("TICKET_WISE"
					.equals(LMSFilterDispatcher.seSaleReportType)){
				
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					beanList.add(reportsBean);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> scratchSaleGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		logger.info("---Scratch Sale Report Game Wise Helper---");
		Connection con = DBConnect.getConnection();
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			logger.info("----Type Select ---"
					+ LMSFilterDispatcher.seSaleReportType);
			pstmt = con.prepareCall("{call scratchSaleGameWise(?,?)}");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> scratchSaleGameWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		logger.info("---Scratch Sale Report Game Wise Expand Helper---");
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			logger.info("----Type Select ---"+ LMSFilterDispatcher.seSaleReportType);
			pstmt = con.prepareCall("{call scratchSaleGameWiseExpand(?,?)}");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2,endDate);
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			
			if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)){
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					reportsBean.setNoOfLosTkt(rs.getInt("noOfLosTkts"));
					beanList.add(reportsBean);
				}
			}else if ("TICKET_WISE".equals(LMSFilterDispatcher.seSaleReportType)){
				
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					beanList.add(reportsBean);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		CallableStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareCall("{call getAddressFromOrganizationMaster(?)}");
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

	public Map<Integer, List<String>> fetchOrgAddMap() throws LMSException {
		Map<Integer, List<String>> map = new TreeMap<Integer, List<String>>();
		Connection con = null;
		String orgAdd = "";
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> tempList = null;
		try {
			pstmt = con
					.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = 'AGENT'");
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				tempList = new ArrayList<String>();
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
				tempList.add(rs.getString("name"));
				tempList.add(orgAdd);
				map.put(rs.getInt("organization_id"), tempList);
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
		return map;
	}
}
