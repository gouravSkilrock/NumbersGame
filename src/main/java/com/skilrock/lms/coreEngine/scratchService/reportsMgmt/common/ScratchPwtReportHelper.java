package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

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
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

public class ScratchPwtReportHelper implements IScratchPwtReportHelper {
	Log logger = LogFactory.getLog(ScratchPwtReportHelper.class);
	public List<SalePwtReportsBean> scratchBODirPlyPwtGameWise(Timestamp startDate,Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String pwtQry="select game_nbr gameNo, game_name gameName,mrpAmt from st_se_game_master gm,(select game_id,sum(pwt_amt) mrpAmt from st_se_direct_player_pwt where transaction_date>=? and transaction_date<=? group by game_id)pwtTlb where gm.game_id=pwtTlb.game_id";
			pstmt = con.prepareStatement(pwtQry.toString());
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
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
	
	public List<SalePwtReportsBean> scratchBODirPlyPwtGameWiseExpand(Timestamp startDate,Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String pwtQry="select game_nbr gameNo, game_name gameName,priceAmt,noOfTkt,mrpAmt from st_se_game_master gm,(select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt from st_se_direct_player_pwt where transaction_date>=? and transaction_date<=? group by pwt_amt,game_id) pwtTlb where gm.game_id=pwtTlb.game_id order by game_nbr,priceAmt";
			pstmt = con.prepareStatement(pwtQry.toString());
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);

			logger.info("----BO Dircet Ply Pwt Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
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
	public List<SalePwtReportsBean> scratchPwtAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String pwtQry = "select pq.organization_id,"+QueryManager.getOrgCodeQuery()+",mrpAmt,netAmt from (select agent_org_id organization_id,ifnull(sum(pwt_amt),0) mrpAmt,ifnull(sum(net_amt),0) netAmt from st_se_bo_pwt bpwt, st_lms_organization_master  where bpwt.transaction_id in (select btm.transaction_id from st_lms_bo_transaction_master btm where ( transaction_type='PWT' or transaction_type='PWT_AUTO') and  ( btm.transaction_date>=? and btm.transaction_date<?)) and organization_id = agent_org_id group by agent_org_id )pq left outer join (select organization_id,name from st_lms_organization_master where organization_type='AGENT') om on pq.organization_id=om.organization_id  order by "+QueryManager.getAppendOrgOrder();
			pstmt = con.prepareStatement(pwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			logger.info("----Agent Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
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

	public List<SalePwtReportsBean> scratchPwtAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId)
			throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String pwtQry = "select game_nbr gameNo,game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_se_game_master gm,(select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from ((select game_id,pwt_amt priceAmt,count(*) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+(pwt_amt*(agt_claim_comm/100))) netAmt from st_se_retailer_pwt where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT') and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=?) group by pwt_amt,game_id order by game_id,pwt_amt)union all(select game_id,pwt_amt priceAmt,count(*) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+(pwt_amt*(claim_comm/100))) netAmt from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? and agent_org_id=? group by pwt_amt,game_id order by game_id,pwt_amt)) pwtTlb group by game_id,priceAmt) pwtTlb where gm.game_id=pwtTlb.game_id";
			pstmt = con.prepareStatement(pwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agentOrgId);
			pstmt.setTimestamp(4, startDate);
			pstmt.setTimestamp(5, endDate);
			pstmt.setInt(6, agentOrgId);
			logger.info("----Agent Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
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

	public List<SalePwtReportsBean> scratchPwtGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String pwtQry = "select gameNo,gameName,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from ( select game_nbr gameNo, game_name gameName,mrpAmt,netAmt from st_se_game_master gm,(select game_id,sum(pwt_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_pwt where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>=? and transaction_date<?) group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id  union all select game_nbr gameNo, game_name gameName,mrpAmt,netAmt from st_se_game_master gm,(select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt) netAmt from st_se_direct_player_pwt where transaction_date>=? and transaction_date<? group by game_id) pwtTlb1 where gm.game_id=pwtTlb1.game_id ) ab group by ab.gameNo";
			pstmt = con.prepareStatement(pwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setTimestamp(3, startDate);
			pstmt.setTimestamp(4, endDate);
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
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

	public List<SalePwtReportsBean> scratchPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String pwtQry = "select game_nbr gameNo,game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_se_game_master gm,(select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from ((select game_id,pwt_amt priceAmt,count(*) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+(pwt_amt*(agt_claim_comm/100))) netAmt from st_se_retailer_pwt where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT') group by pwt_amt,game_id order by game_id,pwt_amt) union all (select game_id,pwt_amt priceAmt,count(*) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+(pwt_amt*(claim_comm/100))) netAmt from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? group by game_id,pwt_amt order by game_id,pwt_amt)union all (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt) netAmt from st_se_direct_player_pwt where transaction_date>=? and transaction_date<=? group by game_id,pwt_amt order by game_id,pwt_amt) ) pwtTlb group by game_id,priceAmt) pwtTlb where gm.game_id=pwtTlb.game_id";
			pstmt = con.prepareStatement(pwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setTimestamp(3, startDate);
			pstmt.setTimestamp(4, endDate);
			pstmt.setTimestamp(5, startDate);
			pstmt.setTimestamp(6, endDate);
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
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
	
	public Map<Integer, List<String>> fetchOrgAddMap()throws LMSException{
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
