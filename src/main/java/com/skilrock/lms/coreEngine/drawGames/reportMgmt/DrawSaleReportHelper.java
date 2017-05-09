package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.RegionWiseDataBean;


public class DrawSaleReportHelper implements IDrawSaleReportHelper {
	private static Log logger = LogFactory.getLog(DrawSaleReportHelper.class);

	public List<SalePwtReportsBean> drawSaleAgentWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "(select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select om.organization_id,"+QueryManager.getOrgCodeQuery()+",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master,(select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry
					.append(") saleTlb group by retailer_org_id) saleTlb where retailer_org_id=organization_id group by parent_id) saleTlb on saleTlb.parent_id=om.organization_id");

			pstmt = con.prepareStatement(saleQry.toString());

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
			CommonFunctionsHelper.sortListForOrgOrder(beanList);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleRetailerWise(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "(select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select om.organization_id,"+QueryManager.getOrgCodeQuery()+",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master,(select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry
					.append(") saleTlb group by retailer_org_id) saleTlb where retailer_org_id=organization_id and parent_id="
							+ agtOrgId
							+ " group by organization_id) saleTlb on saleTlb.organization_id=om.organization_id");

			pstmt = con.prepareStatement(saleQry.toString());

			logger.info("----Retailer Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
			CommonFunctionsHelper.sortListForOrgOrder(beanList);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) saleRet on unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select gm.game_id gameId,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_nbr from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_id=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
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
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where organization_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where organization_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) saleRet on unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0 ) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select gm.game_id gameId,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_id=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
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
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Helper---");
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String indGameQry = "select sale.game_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by game_id) sale left outer join (select game_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by game_id)saleRet on sale.game_id=saleRet.game_id union all ";
			StringBuilder saleQry = new StringBuilder(
					"select game_id gameId,game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id gameId from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("gameId")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.gameId=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Helper---");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "select sale.game_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by game_id) sale left outer join (select game_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by game_id)saleRet on sale.game_id=saleRet.game_id union all ";
			StringBuilder saleQry = new StringBuilder(
					"select game_id gameId,game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id gameId from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("gameId")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.gameId=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		logger.info("---Draw Sale Report Game Wise Helper---");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "select sale.game_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") group by game_id) sale left outer join (select game_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") group by game_id)saleRet on sale.game_id=saleRet.game_id union all ";
			StringBuilder saleQry = new StringBuilder(
					"select gm.game_id gameId,game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_id=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWiseExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Expand Helper---");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by mrp_amt order by mrp_amt) saleRet on sale.game_id=saleRet.game_id and unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select game_id gameId,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_id=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
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
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawSaleGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException {
		logger.info("---Draw Sale Report Game Wise Expand Helper---");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") group by mrp_amt order by mrp_amt) saleRet on sale.game_id=saleRet.game_id and unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select gm.game_id gameId,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_id=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
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
				beanList.add(reportsBean);
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
public static Map<String,RegionWiseDataBean>  fetchRegionWiseSaleCashData(AnalysisBean anaBean,String stateCode,ReportStatusBean reportStatusBean) throws LMSException{
	Connection con =null;
	PreparedStatement pstmt =null;
	Map<String,RegionWiseDataBean> dataMap = new LinkedHashMap<String,RegionWiseDataBean>();
	ResultSet rs = null;
	try{
		
		String subQry =" ";
		if(stateCode!=null&&!stateCode.equalsIgnoreCase("-1")){
			subQry =" and sm.state_code='"+stateCode.trim()+"'";
		}
		/*String str =" select sum(ifnull(main.mrpAmtRet,0.0)-ifnull(sub.mrpAmtRet,0.0)) mrpAmtRet,area_name,city_name,sm.name state,sm.state_code from (select retailer_org_id,sum(net_amt) mrpAmtRet from (select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>=? and transaction_date<=?) rtm "+
					" inner join st_dg_ret_sale_? retSale on retSale.transaction_id=rtm.transaction_id group by retailer_org_id  ) main left join (select retailer_org_id,sum(net_amt) mrpAmtRet from  (select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>=? and transaction_date<=?) rtm "+
					"	inner join st_dg_ret_sale_refund_? retRef on retRef.transaction_id=rtm.transaction_id group by retailer_org_id  ) sub on main.retailer_org_id=sub.retailer_org_id right join st_lms_organization_master om on organization_id=main.retailer_org_id inner join st_lms_city_master cm on cm.city_name=om.city  inner join st_lms_state_master  sm on sm.state_code=om.state_code   inner join st_lms_area_master  am on  am.area_code=om.area_code"+ 
					" where organization_status!='TERMINATE'  and  om.organization_type='RETAILER'"+subQry+" group by sm.state_code "; */
		
		String str=" select  sum(mrpAmtRet) mrpAmtRet,area_name,city_name,sm.name state,sm.state_code from  ( select sum(ifnull(sale.mrpAmtRet,0.00)-ifnull(ref.mrpAmtRet,0.00)) mrpAmtRet,sale.retailer_org_id from ( select retailer_org_id,sum(mrp_amt) mrpAmtRet from  ( select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>=? and transaction_date<=? ) rtm inner join st_dg_ret_sale_? retSale on retSale.transaction_id=rtm.transaction_id group by retailer_org_id  ) sale left join (select retailer_org_id,sum(mrp_amt) mrpAmtRet from  (select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED')  and transaction_date>=? and transaction_date<=? ) rtm inner join st_dg_ret_sale_refund_? retRef on retRef.transaction_id=rtm.transaction_id group by retailer_org_id  ) ref on sale.retailer_org_id=ref.retailer_org_id  group by retailer_org_id "+
						" union all select mrpAmtRet,retailer_org_id from ( select ifnull(sum(sale_mrp-ref_sale_mrp) ,0.00) mrpAmtRet,organization_id retailer_org_id  from  st_rep_dg_retailer where finaldate>=date(?) and finaldate<=date(?) and game_id=? group by organization_id ) rep group by retailer_org_id )main right join st_lms_organization_master om on organization_id=main.retailer_org_id inner join st_lms_city_master cm on cm.city_name=om.city  inner join st_lms_state_master  sm on sm.state_code=om.state_code   inner join st_lms_area_master  am on  am.area_code=om.area_code inner join st_lms_user_master um on um.organization_id=om.organization_id  where ( (um.termination_date>=? or um.termination_date is null) and registration_date<=? )  and  om.organization_type='RETAILER' and um.isrolehead='Y' "+subQry+" group by sm.state_code" ; 
		
		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();
		pstmt =con.prepareStatement(str);
		pstmt.setString(1, anaBean.getStartDate());
		pstmt.setString(2, anaBean.getEndDate());
		pstmt.setInt(3, anaBean.getGameId());
		pstmt.setString(4, anaBean.getStartDate());
		pstmt.setString(5, anaBean.getEndDate());
		pstmt.setInt(6, anaBean.getGameId());
		pstmt.setString(7, anaBean.getStartDate());
		pstmt.setString(8, anaBean.getEndDate());
		pstmt.setInt(9, anaBean.getGameId());
		pstmt.setString(10, anaBean.getStartDate());
		pstmt.setString(11, anaBean.getStartDate());
		rs = pstmt.executeQuery();
		while(rs.next()){
			RegionWiseDataBean dataBean =null;
			String stateName = rs.getString("state_code");
			if(dataMap.containsKey(stateName)){
				dataBean =dataMap.get(stateName);
				dataBean.setSaleAmt(dataBean.getSaleAmt()+rs.getDouble("mrpAmtRet"));
				
			}else{
				dataBean = new RegionWiseDataBean();
				dataBean.setStateName(rs.getString("state"));
				dataBean.setStateCode(rs.getString("state_code"));
				dataBean.setCityName(rs.getString("city_name"));
				dataBean.setAreaName(rs.getString("area_name"));
				dataBean.setSaleAmt(rs.getDouble("mrpAmtRet"));
				dataMap.put(stateName, dataBean);
			}
			
			
		}
		DBConnect.closeRs(rs);
		DBConnect.closePstmt(pstmt);
		/*String str1 =" select ifnull(sum(amount),0.0) netAmt,area_name,city_name,sm.name state,sm.state_code from ( select ifnull(sum(cash.amount),0.0) amount,party_id from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where  ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and cash.transaction_id=btm.transaction_id  group by party_id union all"+
					" select ifnull(sum(chq.cheque_amt),0.0) amount,party_id from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<? ) and chq.transaction_id=btm.transaction_id  group by party_id union all  select ifnull(-sum(chq.cheque_amt),0.0) amount,party_id from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and chq.transaction_id=btm.transaction_id  group by party_id "+ 
					" union all select ifnull(-sum(bo.amount),0.0) amount,party_id  from st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and  ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) group by party_id union all  select ifnull(sum(bo.amount),0.0) amount,party_id  from st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' or bo.transaction_type ='CR_NOTE')  and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) group by party_id )a "+ 
					" right join st_lms_organization_master om  on organization_id=a.party_id inner join st_lms_city_master cm on cm.city_name=om.city inner join st_lms_state_master  sm on sm.state_code=om.state_code  inner join st_lms_area_master  am on  am.area_code=om.area_code where organization_status!='TERMINATE'  and  om.organization_type='RETAILER' "+subQry+" group by sm.state_code ";*/
		String str1=" select ifnull(sum(amount),0.0) netAmt,area_name,city_name,sm.name state,sm.state_code from ( select ifnull(sum(cash.amount),0.0) amount,party_id from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where  ( date(btm.transaction_date)>=? and date(btm.transaction_date)<=?) and cash.transaction_id=btm.transaction_id  group by party_id union all select ifnull(sum(chq.cheque_amt),0.0) amount,party_id from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<=? ) and chq.transaction_id=btm.transaction_id  group by party_id union all  select ifnull(-sum(chq.cheque_amt),0.0) amount,party_id from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<=?) and chq.transaction_id=btm.transaction_id  group by party_id  union all select ifnull(-sum(bo.amount),0.0) amount,party_id  from st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and  ( date(btm.transaction_date)>=? and date(btm.transaction_date)<=?) group by party_id union all  select ifnull(sum(bo.amount),0.0) amount,party_id  from st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' or bo.transaction_type ='CR_NOTE')  and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<=?) "+ 
		" group by party_id  union all 	 select sum(ifnull(cash_amt+credit_note-debit_note+cheque_amt-cheque_bounce_amt,0.00)) amount,retailer_org_id party_id  from st_rep_agent_payments where finaldate>=date(?) and finaldate<=date(?)  group by party_id )a  right join st_lms_organization_master om  on organization_id=a.party_id inner join st_lms_city_master cm on cm.city_name=om.city inner join st_lms_state_master  sm on sm.state_code=om.state_code  inner join st_lms_area_master  am on  am.area_code=om.area_code inner join st_lms_user_master um on um.organization_id=om.organization_id  where (um.termination_date is null or (um.termination_date>=?  or registration_date>= ? ))  and  om.organization_type='RETAILER' and um.isrolehead='Y' "+subQry+" group by sm.state_code";
				 	
		
		pstmt =con.prepareStatement(str1);
		pstmt.setString(1, anaBean.getStartDate());
		pstmt.setString(2, anaBean.getEndDate());
		pstmt.setString(3, anaBean.getStartDate());
		pstmt.setString(4, anaBean.getEndDate());
		pstmt.setString(5, anaBean.getStartDate());
		pstmt.setString(6, anaBean.getEndDate());
		pstmt.setString(7, anaBean.getStartDate());
		pstmt.setString(8, anaBean.getEndDate());
		pstmt.setString(9, anaBean.getStartDate());
		pstmt.setString(10, anaBean.getEndDate());
		pstmt.setString(11, anaBean.getStartDate());
		pstmt.setString(12, anaBean.getEndDate());
		pstmt.setString(13, anaBean.getStartDate());
		pstmt.setString(14, anaBean.getEndDate());
		logger.debug("Sale Query"+pstmt);
		rs = pstmt.executeQuery();
		while(rs.next()){
			RegionWiseDataBean dataBean =null;
			String stateName = rs.getString("state_code");
			if(dataMap.containsKey(stateName)){
				dataBean =dataMap.get(stateName);
				dataBean.setTotalCashAmt(dataBean.getTotalCashAmt()+rs.getDouble("netAmt"));
				
			}else{
				dataBean = new RegionWiseDataBean();
				dataBean.setStateName(rs.getString("state"));
				dataBean.setStateCode(rs.getString("state_code"));
				dataBean.setCityName(rs.getString("city_name"));
				dataBean.setAreaName(rs.getString("area_name"));
				dataBean.setTotalCashAmt(rs.getDouble("netAmt"));
				dataMap.put(stateName, dataBean);
			}
			
			
		}
	}catch (SQLException e) {
		logger.error("SQL Exception ", e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error(" Exception ", e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeRs(rs);
		DBConnect.closePstmt(pstmt);
		DBConnect.closeCon(con);
		
	}
	return dataMap;
}
public static Map<String,RegionWiseDataBean>  fetchRegionWiseRetSaleData(RegionWiseDataBean dataBean,ReportStatusBean reportStatusBean) throws LMSException{
	Connection con =null;
	PreparedStatement pstmt =null;
	Map<String,RegionWiseDataBean> dataMap = new LinkedHashMap<String,RegionWiseDataBean>();
	ResultSet rs = null;
	try{
		
	
		StringBuilder appnQry =new StringBuilder();
		String stateCode =dataBean.getStateCode();
		String cityCode =dataBean.getCityCode();
		String areaCode =dataBean.getAreaCode();
	
		String orgCodeQry=" om.name orgCode,pm.name parentOrgCode ";;
		String appendOrder ="orgCode ASC ";
		if(stateCode!=null&&!stateCode.equalsIgnoreCase("-1")){
			appnQry.append(" and sm.state_code='"+stateCode.trim()+"' ");
		
		}
		if(cityCode!=null&&!cityCode.equalsIgnoreCase("-1")){
			appnQry.append(" and cm.city_code='"+cityCode.trim()+"' ");
			
		}
		if(areaCode!=null&&!areaCode.equalsIgnoreCase("-1")){
			appnQry.append(" and find_in_set(am.area_code,'"+areaCode.trim()+"')");
			
		}
		if(dataBean.getOrgId()!=-1){
			appnQry.append(" and om.parent_id ="+dataBean.getOrgId());
			
		}
		if ("CODE".equalsIgnoreCase(Utility.getPropertyValue("ORG_LIST_TYPE"))) {
			orgCodeQry = " om.org_code orgCode,pm.org_code parentOrgCode ";
		

		} else if ("CODE_NAME".equalsIgnoreCase(Utility.getPropertyValue("ORG_LIST_TYPE"))) {
			orgCodeQry = " concat(om.org_code,'_',om.name)  orgCode , concat(pm.org_code,'_',pm.name) parentOrgCode ";
		
			
		} else if ("NAME_CODE".equalsIgnoreCase(Utility.getPropertyValue("ORG_LIST_TYPE"))) {
			orgCodeQry = " concat(om.name,'_',om.org_code)  orgCode,concat(pm.name,'_',pm.org_code)  parentOrgCode  ";
		
			
		}
		if( "ORG_ID".equalsIgnoreCase(Utility.getPropertyValue("ORG_LIST_ORDER")) ){
			appendOrder="om.organization_id";
			
		}else if( "DESC".equalsIgnoreCase(Utility.getPropertyValue("ORG_LIST_ORDER")) ){
			
			appendOrder="om.orgCode DESC ";
		}
		
	/*	String str =" select sum(ifnull(main.mrpAmtRet,0.0)-ifnull(sub.mrpAmtRet,0.0)) mrpAmtRet,area_name,city_name,sm.name state,sm.state_code from (select retailer_org_id,sum(net_amt) mrpAmtRet from (select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>=? and transaction_date<=?) rtm "+
					" inner join st_dg_ret_sale_? retSale on retSale.transaction_id=rtm.transaction_id group by retailer_org_id  ) main left join (select retailer_org_id,sum(net_amt) mrpAmtRet from  (select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>=? and transaction_date<=?) rtm "+
					"	inner join st_dg_ret_sale_refund_? retRef on retRef.transaction_id=rtm.transaction_id group by retailer_org_id  ) sub on main.retailer_org_id=sub.retailer_org_id right join st_lms_organization_master om on organization_id=main.retailer_org_id inner join st_lms_city_master cm on cm.city_name=om.city  inner join st_lms_state_master  sm on sm.state_code=om.state_code   inner join st_lms_area_master  am on  am.area_code=om.area_code"+ 
					" where organization_status!='TERMINATE'  and  om.organization_type='RETAILER'"+appnQry.toString()+" group by sm.state_code "; */
	/*	String str =" select sum((ifnull(retSale.net_amt,0.00)-ifnull(retRef.net_amt,0.00))) mrpAmtRet,"+orgCodeQry+",sm.name state,cm.city_name from (select transaction_id,retailer_org_id from st_lms_retailer_transaction_master where  transaction_type in('DG_SALE','DG_SALE_OFFLINE','DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>=? and transaction_date<=?) rtm "+
					" left join st_dg_ret_sale_? retSale on retSale.transaction_id=rtm.transaction_id left join  st_dg_ret_sale_refund_? retRef on retRef.transaction_id=rtm.transaction_id right join st_lms_organization_master om on organization_id=rtm.retailer_org_id inner join st_lms_organization_master  pm   on pm.organization_id=om.organization_id  inner join st_lms_city_master cm on cm.city_name=om.city "+ 
					"	inner join st_lms_state_master  sm on sm.state_code=om.state_code inner join st_lms_area_master  am on  am.area_code=om.area_code  where  om.organization_status!='TERMINATE'  and  om.organization_type='RETAILER'  "+appnQry+" group by om.organization_id  order by "+appendOrder ;*/
		String str =" select  ifnull(sum(mrpAmtRet),0.0) mrpAmtRet,"+orgCodeQry+",area_name,city_name,sm.name state,sm.state_code from  ( select sum(ifnull(sale.mrpAmtRet,0.00)-ifnull(ref.mrpAmtRet,0.00)) mrpAmtRet,sale.retailer_org_id from ( select retailer_org_id,sum(mrp_amt) mrpAmtRet from  ( select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>=? and transaction_date<=? ) rtm inner join st_dg_ret_sale_? retSale on retSale.transaction_id=rtm.transaction_id group by retailer_org_id  ) sale left join (select retailer_org_id,sum(mrp_amt) mrpAmtRet from  (select transaction_id from st_lms_retailer_transaction_master where  transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED')  and transaction_date>=? and transaction_date<=?) rtm inner join st_dg_ret_sale_refund_? retRef on retRef.transaction_id=rtm.transaction_id group by retailer_org_id  ) ref on sale.retailer_org_id=ref.retailer_org_id  group by retailer_org_id union all select mrpAmtRet,retailer_org_id from ( select ifnull(sum(sale_mrp-ref_sale_mrp) ,0.00) mrpAmtRet,organization_id retailer_org_id  from  st_rep_dg_retailer where finaldate>=date(?) and finaldate<=date(?) and game_id=? group by organization_id ) rep group by retailer_org_id )main right join st_lms_organization_master om on organization_id=main.retailer_org_id inner join st_lms_organization_master  pm   on pm.organization_id=om.parent_id  inner join st_lms_city_master cm on cm.city_name=om.city  inner join st_lms_state_master  sm on sm.state_code=om.state_code   inner join st_lms_area_master  am on  am.area_code=om.area_code inner join st_lms_user_master um on um.organization_id=om.organization_id where ( (um.termination_date>=? or um.termination_date is null) and registration_date<=? )  and  om.organization_type='RETAILER' and um.isrolehead='Y'  "+appnQry.toString()+" group by om.organization_id  order by "+appendOrder;
		
		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();
		pstmt =con.prepareStatement(str);
		pstmt.setString(1, dataBean.getStartDate());
		pstmt.setString(2, dataBean.getEndDate());
		pstmt.setInt(3, dataBean.getGameId());
		pstmt.setString(4, dataBean.getStartDate());
		pstmt.setString(5, dataBean.getEndDate());
		pstmt.setInt(6, dataBean.getGameId());
		pstmt.setString(7, dataBean.getStartDate());
		pstmt.setString(8, dataBean.getEndDate());
		pstmt.setInt(9, dataBean.getGameId());
		pstmt.setString(10, dataBean.getStartDate());
		pstmt.setString(11,dataBean.getStartDate());
		logger.debug("Ret Sale Query"+pstmt);
		rs = pstmt.executeQuery();
		while(rs.next()){
			RegionWiseDataBean repdataBean =null;
			repdataBean = new RegionWiseDataBean();
			repdataBean.setStateName(rs.getString("state"));
			repdataBean.setCityName(rs.getString("city_name"));
			repdataBean.setSaleAmt(rs.getDouble("mrpAmtRet"));
			repdataBean.setOrgName(rs.getString("orgCode"));
			repdataBean.setParentOrgName(rs.getString("parentOrgCode"));
			dataMap.put(rs.getString("orgCode"), repdataBean);
			
			
		}
		
	}catch (SQLException e) {
		logger.error("SQL Exception ", e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error(" Exception ", e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeCon(con);
		DBConnect.closeRs(rs);
		DBConnect.closePstmt(pstmt);
	
	}
	return dataMap;
}

@Override
public List<SalePwtReportsBean> drawSaleAgentWiseExpand(Timestamp startDate,
		Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean,
		String stateCode, String cityCode) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List<SalePwtReportsBean> drawSaleRetailerWiseExpand(Timestamp startDate,
		Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean,
		String stateCode, String cityCode) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

	
}
