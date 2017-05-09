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
import org.apache.commons.collections.CollectionUtils;

import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;


public class DrawSaleReportHelperSP implements IDrawSaleReportHelper{
	Log logger = LogFactory.getLog(DrawSaleReportHelperSP.class);

	public List<SalePwtReportsBean> drawSaleAgentWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		PreparedStatement pstmtGame = null;
		PreparedStatement iwPstmtGame = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = null;
		Map<Integer, SalePwtReportsBean> beanMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			cstmt = con.prepareCall("call drawGameSaleAgentWisePerGame(?,?,?,?,?)");
			String gameQry = "select game_id from st_dg_game_master";
			String iwGameQry = "select game_id from st_iw_game_master";
			pstmtGame = con.prepareStatement(gameQry);
			iwPstmtGame = con.prepareStatement(iwGameQry);
			ResultSet rsGame = pstmtGame.executeQuery();
		
			while (rsGame.next()) {
				int gameId = rsGame.getInt("game_id");
				cstmt.setTimestamp(1, startDate);
				cstmt.setTimestamp(2, endDate);
				cstmt.setInt(3, gameId);
				cstmt.setString(4, cityCode);
				cstmt.setString(5, stateCode);
				logger.info("----Agent Qry---" + cstmt);
				rs = cstmt.executeQuery();
				while (rs.next()) {
					int orgId=rs.getInt("organization_id");
					if(!beanMap.containsKey(orgId)){
						beanMap.put(orgId, new SalePwtReportsBean());
					}	
					reportsBean = beanMap.get(orgId);
					reportsBean.setGameName(rs.getString("orgCode"));
					reportsBean.setGameNo(orgId);
					reportsBean.setSaleMrpAmt(reportsBean.getSaleMrpAmt()+rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(reportsBean.getSaleNetAmt()+rs.getDouble("netAmt"));
					reportsBean.setStateCode(rs.getString("state_name"));
					reportsBean.setCityCode(rs.getString("city_name"));
				}
			}
			
			ResultSet iwGame = iwPstmtGame.executeQuery();
			cstmt = con.prepareCall("call iwGameSaleAgentWisePerGame(?,?,?,?,?)");
			
			while (iwGame.next()) {
				int gameId = iwGame.getInt("game_id");
				cstmt.setTimestamp(1, startDate);
				cstmt.setTimestamp(2, endDate);
				cstmt.setInt(3, gameId);
				cstmt.setString(4, cityCode);
				cstmt.setString(5, stateCode);
				logger.info("----Agent Qry---" + cstmt);
				iwResultSet = cstmt.executeQuery();
				while (iwResultSet.next()) {
					int orgId=iwResultSet.getInt("organization_id");
					if(!beanMap.containsKey(orgId)){
						beanMap.put(orgId, new SalePwtReportsBean());
					}	
					reportsBean = beanMap.get(orgId);
					reportsBean.setGameName(iwResultSet.getString("orgCode"));
					reportsBean.setGameNo(orgId);
					reportsBean.setSaleMrpAmt(reportsBean.getSaleMrpAmt()+iwResultSet.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(reportsBean.getSaleNetAmt()+iwResultSet.getDouble("netAmt"));
					reportsBean.setStateCode(iwResultSet.getString("state_name"));
					reportsBean.setCityCode(iwResultSet.getString("city_name"));
				}
			}
			
			beanList=new ArrayList<SalePwtReportsBean>(beanMap.values());
			CommonFunctionsHelper.sortListForOrgOrder(beanList);	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(pstmtGame, iwPstmtGame, cstmt, rs, iwResultSet, con);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleRetailerWise(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		Connection con = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		SalePwtReportsBean tempBean = new SalePwtReportsBean();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			pstmt = con.prepareCall("call drawSaleRetailerWise(?,?,?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			pstmt.setString(4, cityCode);
			pstmt.setString(5, stateCode);
			logger.info("----Retailer Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setStateCode(rs.getString("state_name"));
				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwSaleRetailerWise(?,?,?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			pstmt.setString(4, cityCode);
			pstmt.setString(5, stateCode);
			logger.info("----Retailer Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				int orgId = iwResultSet.getInt("organization_id") ;
				tempBean.setGameNo(orgId);
				reportsBean=(SalePwtReportsBean)CollectionUtils.find(beanList, tempBean);
				if(reportsBean == null){
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("orgCode"));
				reportsBean.setGameNo(orgId);
				reportsBean.setSaleMrpAmt(iwResultSet.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(iwResultSet.getDouble("netAmt"));
				reportsBean.setStateCode(iwResultSet.getString("state_name"));
				reportsBean.setCityCode(iwResultSet.getString("city_name"));
				beanList.add(reportsBean);
				}
				else{
					reportsBean.setSaleMrpAmt(reportsBean.getSaleMrpAmt() + iwResultSet.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(reportsBean.getSaleNetAmt() + iwResultSet.getDouble("netAmt"));
				}
			}
			
			CommonFunctionsHelper.sortListForOrgOrder(beanList);	
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, rs, iwResultSet, pstmt);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean, String stateCode, String cityCode)
			throws SQLException {
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet iwResultSet = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			cstmt = con.prepareCall("call drawGameSaleAgentWiseExpend(?,?,?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agentOrgId);
			cstmt.setString(4, stateCode);
			cstmt.setString(5, cityCode);
			logger.info("----Game Expand Qry---" + cstmt);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setStateCode(rs.getString("state_name"));
				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			cstmt = con.prepareCall("call iwGameSaleAgentWiseExpand(?,?,?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agentOrgId);
			cstmt.setString(4, stateCode) ;
			cstmt.setString(5, cityCode);
			logger.info("----Game Expand Qry---" + cstmt);
			iwResultSet = cstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setSaleMrpAmt(iwResultSet.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(iwResultSet.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(iwResultSet.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(iwResultSet.getInt("noOfTkt"));
				reportsBean.setStateCode(iwResultSet.getString("state_name"));
				reportsBean.setCityCode(iwResultSet.getString("city_name"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, cstmt, rs, iwResultSet);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean, String stateCode, String cityCode)
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

			pstmt = con.prepareCall("call drawSaleRetailerWiseExpand(?,?,?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agentOrgId);
			pstmt.setString(4, stateCode);
			pstmt.setString(5, cityCode);
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setStateCode(rs.getString("state_name"));
				reportsBean.setCityCode(rs.getString("city_name"));
				beanList.add(reportsBean);
			}
			
			pstmt = con.prepareCall("call iwSaleRetailerWiseExpand(?,?,?,?,?)");
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agentOrgId);
			pstmt.setString(4, stateCode);
			pstmt.setString(5, cityCode);
			logger.info("----Game Expand Qry---" + pstmt);
			iwResultSet = pstmt.executeQuery();
			while (iwResultSet.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwResultSet.getString("gameName"));
				reportsBean.setGameNo(iwResultSet.getInt("gameId"));
				reportsBean.setSaleMrpAmt(iwResultSet.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(iwResultSet.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(iwResultSet.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(iwResultSet.getInt("noOfTkt"));
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

	public List<SalePwtReportsBean> drawSaleGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Helper---");
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			cstmt = con.prepareCall("call drawSaleGameWise(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, 0);
			logger.info("----Game Qry---" + cstmt);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(cstmt, con, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Helper---");
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet iwRs = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			cstmt = con.prepareCall("call drawSaleGameWise(?,?,?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, 0);
			cstmt.setString(4, cityCode);
			cstmt.setString(5, stateCode);
			logger.info("----Game Qry---" + cstmt);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
			
			cstmt = con.prepareCall("call iwSaleGameWise(?,?,?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, 0);
			cstmt.setString(4, cityCode);
			cstmt.setString(5, stateCode);
			logger.info("----Game Qry---" + cstmt);
			iwRs = cstmt.executeQuery();
			while (iwRs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwRs.getString("gameName"));
				reportsBean.setGameNo(iwRs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(iwRs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(iwRs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, cstmt, rs, iwRs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		logger.info("---Draw Sale Report Game Wise Helper---");
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet iwRs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			cstmt = con.prepareCall("call drawSaleGameWise(?,?,?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			cstmt.setString(4, "ALL");
			cstmt.setString(5, "ALL");
			logger.info("----Game Qry---" + cstmt);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
			
			cstmt = con.prepareCall("call iwSaleGameWise(?,?,?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			cstmt.setString(4, "ALL");
			cstmt.setString(5, "ALL");
			logger.info("----IW Game Qry---" + cstmt);
			iwRs = cstmt.executeQuery();
			while (iwRs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwRs.getString("gameName"));
				reportsBean.setGameNo(iwRs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(iwRs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(iwRs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, cstmt, rs,iwRs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWiseExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Expand Helper---");
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet iwRs = null ;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			cstmt = con.prepareCall("call drawSaleGameWiseExpand(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, 0);
			logger.info("----Game Expand Qry---" + cstmt);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
			
			
			cstmt = con.prepareCall("call iwSaleGameWiseExpand(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, 0);
			logger.info("----Game Expand Qry---" + cstmt);
			iwRs = cstmt.executeQuery();
			while (iwRs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwRs.getString("gameName"));
				reportsBean.setGameNo(iwRs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(iwRs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(iwRs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(iwRs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(iwRs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, cstmt, rs);
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		logger.info("---Draw Sale Report Game Wise Expand Helper---");
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet iwRs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			cstmt = con.prepareCall("call drawSaleGameWiseExpand(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			logger.info("----Game Expand Qry---" + cstmt);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
			
			cstmt = con.prepareCall("call iwSaleGameWiseExpand(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			logger.info("----IW Game Expand Qry---" + cstmt);
			iwRs = cstmt.executeQuery();
			while (iwRs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(iwRs.getString("gameName"));
				reportsBean.setGameNo(iwRs.getInt("gameId"));
				reportsBean.setSaleMrpAmt(iwRs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(iwRs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(iwRs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(iwRs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con, cstmt, rs,iwRs);
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

	public Map<Integer, List<String>> fetchOrgAddMap(String orgType,
			Integer agtOrgId) throws LMSException {
		Map<Integer, List<String>> map = new TreeMap<Integer, List<String>>();
		Connection con = null;
		String orgAdd = "";
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> tempList = null;
		try {
			if (orgType.equalsIgnoreCase("AGENT")) {
				pstmt = con
						.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = '"
								+ orgType + "'");
			} else {
				pstmt = con
						.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = '"
								+ orgType + "' and parent_id = " + agtOrgId);
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
			DBConnect.closeResource(pstmt, con, rs);
		}
		return map;
	}

}
