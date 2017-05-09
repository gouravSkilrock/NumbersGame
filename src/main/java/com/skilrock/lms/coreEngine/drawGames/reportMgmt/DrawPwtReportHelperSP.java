package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;

public class DrawPwtReportHelperSP implements IDrawPwtReportHelper{
	Log logger = LogFactory.getLog(DrawPwtReportHelperSP.class);

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			pstmt = con.prepareCall("call drawBODirPlyPwtGameWise(?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawBODirPlyPwtGameWise(?,?);");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
//			pstmt.setString(3, stateCode);
//			pstmt.setString(4, cityCode);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwBODirPlyPwtGameWise(?,?);");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawAgentDirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawAgentDirPlyPwtGameWise(?, ?, ?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
//			pstmt.setString(4, stateCode);
//			pstmt.setString(5, cityCode);

			logger.info("----Agent Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwAgentDirPlyPwtGameWise(?, ?, ?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);

			logger.info("----Agent Dircet Ply Pwt Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawBODirPlyPwtGameWiseExpand(?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
//			pstmt.setString(3, stateCode);
//			pstmt.setString(4, cityCode);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwBODirPlyPwtGameWiseExpand(?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setPriceAmt(iwResultSet.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(iwResultSet.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawAgentDirPlyPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawAgentDirPlyPwtGameWiseExpand(?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
//			pstmt.setString(4, stateCode);
//			pstmt.setString(5, cityCode);
			logger.info("----Agent Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwAgentDirPlyPwtGameWiseExpand(?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			logger.info("----Agent Dircet Ply Pwt Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameNo"));
				reportsBean.setPriceAmt(iwResultSet.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(iwResultSet.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtAgentWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode, ReportStatusBean reportStatusBean) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		Map<Integer, SalePwtReportsBean> beanMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawPwtAgentWise(?,?, ?, ?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setString(3, stateCode);
			pstmt.setString(4, cityCode);
			logger.info("----Agent Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int orgId=rs.getInt("organization_id");
				if(!beanMap.containsKey(orgId)){
					beanMap.put(orgId, new SalePwtReportsBean());
				}
				reportsBean = beanMap.get(orgId);
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(orgId);
				reportsBean.setPwtMrpAmt(reportsBean.getPwtMrpAmt()+ rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(reportsBean.getPwtNetAmt() + rs.getDouble("netAmt"));
				reportsBean.setStateCode(rs.getString("state_name"));
				reportsBean.setCityCode(rs.getString("city_name"));
			}
			
			pstmt = con.prepareCall("call iwPwtAgentWise(?,?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setString(3, stateCode);
			pstmt.setString(4, cityCode);
			logger.info("----Agent Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				int orgId=iwResultSet.getInt("organization_id");
				if(!beanMap.containsKey(orgId)){
					beanMap.put(orgId, new SalePwtReportsBean());
				}
				reportsBean = beanMap.get(orgId);
				reportsBean.setGameName(iwResultSet.getString("orgCode"));
				reportsBean.setGameNo(orgId);
				reportsBean.setPwtMrpAmt(reportsBean.getPwtMrpAmt() + iwResultSet.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(reportsBean.getPwtNetAmt() + iwResultSet.getDouble("netAmt"));
				reportsBean.setStateCode(iwResultSet.getString("state_name"));
				reportsBean.setCityCode(iwResultSet.getString("city_name"));
			}
			beanList=new ArrayList<SalePwtReportsBean>(beanMap.values());
			CommonFunctionsHelper.sortListForOrgOrder(beanList);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtAgentWiseExpand(Timestamp startDate,
			Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();
			pstmt = con.prepareCall("call drawPwtAgentWiseExpand(?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agentOrgId);
//			pstmt.setString(4, stateCode);
//			pstmt.setString(5, cityCode);
			logger.info("----Agent Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwPwtAgentWiseExpand(?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agentOrgId);
			logger.info("----Agent Expand Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setPriceAmt(iwResultSet.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(iwResultSet.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(iwResultSet.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			pstmt = con.prepareCall("call drawPwtGameWise(?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawPwtGameWise(?,?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setString(3, stateCode);
			pstmt.setString(4, cityCode);
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwPwtGameWise(?,?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setString(3, stateCode);
			pstmt.setString(4, cityCode);
			logger.info("----Game Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(iwResultSet.getDouble("netAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawPwtGameWiseExpand(?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
//			pstmt.setString(3, stateCode);
//			pstmt.setString(4, cityCode);
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwPwtGameWiseExpand(?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
//			pstmt.setString(3, stateCode);
//			pstmt.setString(4, cityCode);
			logger.info("----Game Expand Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setPriceAmt(iwResultSet.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(iwResultSet.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(iwResultSet.getDouble("netAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
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
			DBConnect.closeResource(con, pstmt, rs);
		}
		return orgAdd;
	}

	public Map<Integer, List<String>> fetchOrgAddMap(String orgType, Integer agtOrgId) throws LMSException {
		Map<Integer, List<String>> map = new TreeMap<Integer, List<String>>();
		Connection con = null;
		String orgAdd = "";
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> tempList = null;
		try {
			if(orgType.equalsIgnoreCase("AGENT")){
				pstmt = con.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = '"+ orgType +"'");
			}else{
				pstmt = con.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = '"+ orgType +"' and parent_id = "+ agtOrgId);
			}
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
			DBConnect.closeResource(con, pstmt, rs);
		}
		return map;
	}

	public List<SalePwtReportsBean> drawPwtRetailerWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode, int agtOrgId, ReportStatusBean reportStatusBean) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		Map<Integer, SalePwtReportsBean> beanMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawPwtRetailerWise(?, ?, ?, ?, ?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			pstmt.setString(4, stateCode);
			pstmt.setString(5, cityCode);
			logger.info("----Retailer Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int orgId = rs.getInt("organization_id");
				if(!beanMap.containsKey(orgId))
				{
					beanMap.put(orgId, new SalePwtReportsBean());
				}
				reportsBean = beanMap.get(orgId);
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(orgId);
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
				reportsBean.setStateCode(rs.getString("state_name"));
				reportsBean.setCityCode(rs.getString("city_name"));
			}
			
			pstmt = con.prepareCall("call iwPwtRetailerWise(?, ?, ?, ?, ?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			pstmt.setString(4, stateCode);
			pstmt.setString(5, cityCode);
			logger.info("----Retailer Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				int orgId = iwResultSet.getInt("organization_id");
				if(!beanMap.containsKey(orgId))
				{
					beanMap.put(orgId, new SalePwtReportsBean());
				}
				reportsBean = beanMap.get(orgId);
				reportsBean.setGameName(iwResultSet.getString("orgCode"));
				reportsBean.setGameNo(orgId);
				reportsBean.setPwtMrpAmt(reportsBean.getPwtMrpAmt() + iwResultSet.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(reportsBean.getPwtNetAmt() + iwResultSet.getDouble("netAmt"));
				reportsBean.setStateCode(iwResultSet.getString("state_name"));
				reportsBean.setCityCode(iwResultSet.getString("city_name"));
			}
			beanList = new ArrayList<SalePwtReportsBean>(beanMap.values());
			CommonFunctionsHelper.sortListForOrgOrder(beanList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int retOrgId, ReportStatusBean reportStatusBean, String stateCode, String cityCode)
			throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawPwtRetailerWiseExpand(?,?,?, ?, ?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, retOrgId);
			pstmt.setString(4, stateCode);
			pstmt.setString(5, cityCode);
			logger.info("----Agent Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
				reportsBean.setStateCode(rs.getString("state_name"));
				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwPwtRetailerWiseExpand(?,?,?, ?, ?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, retOrgId);
			pstmt.setString(4, stateCode);
			pstmt.setString(5, cityCode);
			logger.info("----Agent Expand Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setPriceAmt(iwResultSet.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(iwResultSet.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(iwResultSet.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(iwResultSet.getDouble("netAmt"));
				reportsBean.setStateCode(iwResultSet.getString("state_name"));
				reportsBean.setCityCode(iwResultSet.getString("city_name"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId)
			throws SQLException {
		Connection con = DBConnect.getConnection();
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			pstmt = con.prepareCall("call drawPwtGameWiseForAgent(?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawPwtGameWiseForAgent(?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawPwtGameWiseExpandForAgent(?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
//			pstmt.setString(4, stateCode);
//			pstmt.setString(5, cityCode);
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
//				reportsBean.setStateCode(rs.getString("state_name"));
//				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, pstmt, rs);
		}
		return beanList;
	}

	
}
