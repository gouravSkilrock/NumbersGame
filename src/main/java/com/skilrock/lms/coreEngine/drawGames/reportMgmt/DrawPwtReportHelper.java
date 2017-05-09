package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

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

import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;

public class DrawPwtReportHelper implements IDrawPwtReportHelper {
	Log logger = LogFactory.getLog(DrawPwtReportHelper.class);

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String pwtQry = "select gm.game_id gameId, game_name gameName,mrpAmt from st_dg_game_master gm,(select game_id,sum(pwt_amt) mrpAmt from st_dg_bo_direct_plr_pwt where transaction_date>=? and transaction_date<=? group by game_id)pwtTlb where gm.game_id=pwtTlb.game_id";
			pstmt = con.prepareStatement(pwtQry.toString());
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
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

			String pwtQry = "select gm.game_id gameId, game_name gameName,mrpAmt from st_dg_game_master gm,(select game_id,sum(pwt_amt) mrpAmt from st_dg_bo_direct_plr_pwt where transaction_date>=? and transaction_date<=? group by game_id)pwtTlb where gm.game_id=pwtTlb.game_id";
			pstmt = con.prepareStatement(pwtQry.toString());
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawAgentDirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
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

			String pwtQry = "select gm.game_id gameId, game_name gameName,mrpAmt from st_dg_game_master gm,(select game_id,sum(pwt_amt) mrpAmt from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? and agent_org_id=? group by game_id)pwtTlb where gm.game_id=pwtTlb.game_id";
			pstmt = con.prepareStatement(pwtQry.toString());
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);

			logger.info("----Agent Dircet Ply Pwt Qry---" + pstmt);
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
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

			String pwtQry = "select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt from st_dg_game_master gm,(select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt from st_dg_bo_direct_plr_pwt where transaction_date>=? and transaction_date<=? group by pwt_amt,game_id) pwtTlb where gm.game_id=pwtTlb.game_id order by gm.game_id,priceAmt";
			pstmt = con.prepareStatement(pwtQry.toString());
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawAgentDirPlyPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
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

			String pwtQry = "select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt from st_dg_game_master gm,(select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? and agent_org_id=? group by pwt_amt,game_id) pwtTlb where gm.game_id=pwtTlb.game_id order by gm.game_id,priceAmt";
			pstmt = con.prepareStatement(pwtQry.toString());
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameId"));
				reportsBean.setPriceAmt(rs.getDouble("priceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtAgentWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select retailer_org_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry + " group by retailer_org_id union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select "+QueryManager.getOrgCodeQuery()+",organization_id,mrpAmt,netAmt from st_lms_organization_master om,(select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry.delete(pwtQry.length() - 10, pwtQry.length());
			pwtQry
					.append(") pwtTlb,(select organization_id,parent_id from  st_lms_organization_master where organization_type='RETAILER') om where retailer_org_id= organization_id group by parent_id union all select agent_org_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by agent_org_id) pwtTlb group by parent_id) pwtTlb where om.organization_id=pwtTlb.parent_id");

			pstmt = con.prepareStatement(pwtQry.toString());

			logger.info("----Agent Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
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

	public List<SalePwtReportsBean> drawPwtAgentWiseExpand(Timestamp startDate,
			Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean) throws SQLException {
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry
					+ " and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ agentOrgId
					+ ") group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and game_id=* and agent_org_id="
					+ agentOrgId
					+ " group by pwt_amt) pwtTlb group by priceAmt union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry.delete(pwtQry.length() - 10, pwtQry.length());
			pwtQry.append(") pwtTlb where gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry + " group by game_id union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry
					.append("select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by game_id union all select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt) netAmt from st_dg_bo_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by game_id) pwtTlb group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWise(Timestamp startDate, Timestamp endDate, String stateCode, String cityCode, ReportStatusBean reportStatusBean) throws SQLException {
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry + " group by game_id union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry
					.append("select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by game_id union all select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt) netAmt from st_dg_bo_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by game_id) pwtTlb group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry
					+ " group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and game_id=* group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt) netAmt from st_dg_bo_direct_plr_pwt where transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and game_id=* group by pwt_amt) pwtTlb group by priceAmt union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry.delete(pwtQry.length() - 10, pwtQry.length());
			pwtQry.append(") pwtTlb where gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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

	public List<SalePwtReportsBean> drawPwtRetailerWise(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean) throws SQLException {
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select retailer_org_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+retailer_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry
					+ " and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + ") group by retailer_org_id union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select organization_id,"+QueryManager.getOrgCodeQuery()+",sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master om,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry.delete(pwtQry.length() - 10, pwtQry.length());
			pwtQry
					.append(") pwtTlb where om.organization_id=pwtTlb.retailer_org_id group by om.organization_id");

			pstmt = con.prepareStatement(pwtQry.toString());

			logger.info("----Retailer Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setPwtMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setPwtNetAmt(rs.getDouble("netAmt"));
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

	public List<SalePwtReportsBean> drawPwtRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int retOrgId, ReportStatusBean reportStatusBean)
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+retailer_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry
					+ " and retailer_org_id="
					+ retOrgId
					+ " group by pwt_amt) pwtTlb group by priceAmt union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry.delete(pwtQry.length() - 10, pwtQry.length());
			pwtQry.append(") pwtTlb where  gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId)
			throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+retailer_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry
					+ "  and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + ") group by game_id union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select  gm.game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(");
			String gameQry = "select  game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry
					.append("select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and agent_org_id="
							+ agtOrgId
							+ " group by game_id) pwtTlb group by game_id) pwtTlb where  gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+retailer_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry
					+ "  and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + ") group by game_id union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select  gm.game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(");
			String gameQry = "select  game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry
					.append("select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and agent_org_id="
							+ agtOrgId
							+ " group by game_id) pwtTlb group by game_id) pwtTlb where  gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> drawPwtGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
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

			String tranQry = "(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate + "')";
			String indGameQry = "select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+retailer_claim_comm) netAmt from st_dg_ret_pwt_* where transaction_id in "
					+ tranQry
					+ " and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and game_id=* and agent_org_id="
					+ agtOrgId
					+ " group by pwt_amt) pwtTlb group by priceAmt union all ";
			StringBuilder pwtQry = new StringBuilder(
					"select  gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select  game_id from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				pwtQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_id")));
			}
			pwtQry.delete(pwtQry.length() - 10, pwtQry.length());
			pwtQry.append(") pwtTlb where  gm.game_id=pwtTlb.game_id");

			pstmt = con.prepareStatement(pwtQry.toString());
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
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	@Override
	public List<SalePwtReportsBean> drawPwtAgentWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode,
			ReportStatusBean reportStatusBean) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalePwtReportsBean> drawPwtGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean,
			String cityCode, String stateCode) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalePwtReportsBean> drawPwtRetailerWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode, int agtOrgId,
			ReportStatusBean reportStatusBean) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalePwtReportsBean> drawPwtRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int retOrgId,
			ReportStatusBean reportStatusBean, String stateCode, String cityCode)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}



	
}
