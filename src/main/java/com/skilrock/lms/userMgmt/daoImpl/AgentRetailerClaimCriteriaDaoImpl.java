package com.skilrock.lms.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.userMgmt.javaBeans.UpdateClaimCriteriaBean;
import com.skilrock.lms.userMgmt.javaBeans.UpdatePayoutCenterBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class AgentRetailerClaimCriteriaDaoImpl {
	final static Log logger = LogFactory.getLog(AgentRetailerClaimCriteriaDaoImpl.class);

	private static AgentRetailerClaimCriteriaDaoImpl instance;

	private AgentRetailerClaimCriteriaDaoImpl() {
	}

	public static AgentRetailerClaimCriteriaDaoImpl getInstance() {
		if (instance == null) {
			synchronized (AgentRetailerClaimCriteriaDaoImpl.class) {
				if (instance == null) {
					instance = new AgentRetailerClaimCriteriaDaoImpl();
				}
			}
		}
		return instance;
	}

	public List<UpdateClaimCriteriaBean> fetchRetailerCriteriaList(int agentOrgId, Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		List<UpdateClaimCriteriaBean> retCriteriaList = new ArrayList<UpdateClaimCriteriaBean>();
		UpdateClaimCriteriaBean criteriaBean = null;
		try {
			stmt = connection.createStatement();
			String query = "SELECT scc.organization_id, name, claim_at_self_ret, claim_at_self_agt, claim_at_other_ret_same_agt, claim_at_other_ret, claim_at_other_agt, claim_at_bo FROM st_lms_ret_sold_claim_criteria scc INNER JOIN st_lms_organization_master om ON scc.organization_id=om.organization_id WHERE om.parent_id="+agentOrgId+" AND organization_status IN ('ACTIVE','INACTIVE','BLOCK') ORDER BY name;";
			logger.info("fetchRetailerCriteriaList Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				criteriaBean = new UpdateClaimCriteriaBean();
				criteriaBean.setRetOrgId(rs.getInt("organization_id"));
				criteriaBean.setRetOrgName(rs.getString("name"));
				criteriaBean.setSelfRetailer("YES".equals(rs.getString("claim_at_self_ret")) ? true : false);
				criteriaBean.setSelfAgent("YES".equals(rs.getString("claim_at_self_agt")) ? true : false);
				criteriaBean.setOtherRetailerSameAgent("YES".equals(rs.getString("claim_at_other_ret_same_agt")) ? true : false);
				criteriaBean.setOtherRetailer("YES".equals(rs.getString("claim_at_other_ret")) ? true : false);
				criteriaBean.setOtherAgent("YES".equals(rs.getString("claim_at_other_agt")) ? true : false);
				criteriaBean.setAtBO("YES".equals(rs.getString("claim_at_bo")) ? true : false);
				retCriteriaList.add(criteriaBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return retCriteriaList;
	}

	public void retailerClaimCriteriaUpdate(List<UpdateClaimCriteriaBean> retCriteriaList, int doneByUserId, String requestIp, Connection connection) throws LMSException {
		PreparedStatement insertPstmt = null;
		PreparedStatement updatePstmt = null;
		try {
			String insertQuery = "INSERT INTO st_lms_ret_sold_claim_criteria_history (organization_id, claim_at_self_ret, claim_at_self_agt, claim_at_other_ret_same_agt, claim_at_other_ret, claim_at_other_agt, claim_at_bo, update_time, done_by_user_id, request_ip) SELECT organization_id, claim_at_self_ret, claim_at_self_agt, claim_at_other_ret_same_agt, claim_at_other_ret, claim_at_other_agt, claim_at_bo, '"+Util.getCurrentTimeString()+"', "+doneByUserId+", '"+requestIp+"' FROM st_lms_ret_sold_claim_criteria WHERE organization_id=?;";
			logger.info("retailerClaimCriteriaUpdate Insert History Query - "+insertQuery);
			String updateQuery = "UPDATE st_lms_ret_sold_claim_criteria SET claim_at_self_ret=?, claim_at_self_agt=?, claim_at_other_ret_same_agt=?, claim_at_other_ret=?, claim_at_other_agt=?, claim_at_bo=? WHERE organization_id=?;";
			logger.info("retailerClaimCriteriaUpdate Update Query - "+updateQuery);

			insertPstmt = connection.prepareStatement(insertQuery);
			updatePstmt = connection.prepareStatement(updateQuery);
			for(UpdateClaimCriteriaBean criteriaBean : retCriteriaList) {
				insertPstmt.setInt(1, criteriaBean.getRetOrgId());
				insertPstmt.addBatch();

				updatePstmt.setString(1, criteriaBean.isSelfRetailer() ? "YES" : "NO");
				updatePstmt.setString(2, criteriaBean.isSelfAgent() ? "YES" : "NO");
				updatePstmt.setString(3, criteriaBean.isOtherRetailerSameAgent() ? "YES" : "NO");
				updatePstmt.setString(4, criteriaBean.isOtherRetailer() ? "YES" : "NO");
				updatePstmt.setString(5, criteriaBean.isOtherAgent() ? "YES" : "NO");
				updatePstmt.setString(6, criteriaBean.isAtBO() ? "YES" : "NO");
				updatePstmt.setInt(7, criteriaBean.getRetOrgId());
				updatePstmt.addBatch();
			}
			insertPstmt.executeBatch();
			updatePstmt.executeBatch();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(insertPstmt);
			DBConnect.closePstmt(updatePstmt);
		}
	}

	public List<UpdatePayoutCenterBean> fetchOrgPayoutList(int agentOrgId, Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		List<UpdatePayoutCenterBean> payoutCenterList = new ArrayList<UpdatePayoutCenterBean>();
		UpdatePayoutCenterBean payoutCenterBean = null;
		try {
			String query = "SELECT om.organization_id, name, self_claim, other_claim, verification_limit, max_daily_claim_amt, min_claim_per_ticket, max_claim_per_ticket FROM st_lms_oranization_limits ol INNER JOIN st_lms_organization_master om ON ol.organization_id=om.organization_id WHERE organization_status IN ('ACTIVE','INACTIVE','BLOCK') AND " + ((agentOrgId == 0) ? "organization_type='AGENT' ORDER BY name;" : "om.parent_id="+agentOrgId+" AND organization_type='RETAILER' ORDER BY name;");
			logger.info("fetchOrgPayoutList Query - "+query);

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				payoutCenterBean = new UpdatePayoutCenterBean();
				payoutCenterBean.setOrgId(rs.getInt("organization_id"));
				payoutCenterBean.setOrgName(rs.getString("name"));
				payoutCenterBean.setClaimAtSelf("YES".equals(rs.getString("self_claim")) ? true : false);
				payoutCenterBean.setClaimAtOther("YES".equals(rs.getString("other_claim")) ? true : false);
				payoutCenterBean.setVerificationLimit(rs.getString("verification_limit"));
				payoutCenterBean.setClaimLimit(rs.getString("max_daily_claim_amt"));
				payoutCenterBean.setMinClaimAmount(rs.getString("min_claim_per_ticket"));
				payoutCenterBean.setMaxClaimAmount(rs.getString("max_claim_per_ticket"));
				payoutCenterList.add(payoutCenterBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return payoutCenterList;
	}

	public void payoutCenterUpdate(List<UpdatePayoutCenterBean> payoutCenterList, int doneByUserId, Connection connection) throws LMSException {
		PreparedStatement insertPstmt = null;
		PreparedStatement updatePstmt = null;
		try {
			String insertQuery = "INSERT INTO st_lms_oranization_limits_history (date_changed, change_by_user_id, organization_id, verification_limit, approval_limit, pay_limit, scrap_limit, ola_deposit_limit, ola_withdrawal_limit, max_daily_claim_amt, self_claim, other_claim, min_claim_per_ticket, max_claim_per_ticket, block_amt, block_days, block_action) SELECT '"+Util.getCurrentTimeString()+"', "+doneByUserId+", organization_id, verification_limit, approval_limit, pay_limit, scrap_limit, ola_deposit_limit, ola_withdrawal_limit, max_daily_claim_amt, self_claim, other_claim, min_claim_per_ticket, max_claim_per_ticket, block_amt, block_days, block_action FROM st_lms_oranization_limits WHERE organization_id=?;";
			logger.info("retailerClaimCriteriaUpdate Insert In History Query - "+insertQuery);
			String updateQuery = "UPDATE st_lms_oranization_limits SET self_claim=?, other_claim=?, verification_limit=?, max_daily_claim_amt=?, min_claim_per_ticket=?, max_claim_per_ticket=? WHERE organization_id=?;";
			logger.info("retailerClaimCriteriaUpdate Update Query - "+updateQuery);

			insertPstmt = connection.prepareStatement(insertQuery);
			updatePstmt = connection.prepareStatement(updateQuery);
			for(UpdatePayoutCenterBean payoutCenterBean : payoutCenterList) {
				insertPstmt.setInt(1, payoutCenterBean.getOrgId());
				insertPstmt.addBatch();

				updatePstmt.setString(1, payoutCenterBean.isClaimAtSelf() ? "YES" : "NO");
				updatePstmt.setString(2, payoutCenterBean.isClaimAtOther() ? "YES" : "NO");
				updatePstmt.setString(3, payoutCenterBean.getVerificationLimit());
				updatePstmt.setString(4, payoutCenterBean.getClaimLimit());
				updatePstmt.setString(5, payoutCenterBean.getMinClaimAmount());
				updatePstmt.setString(6, payoutCenterBean.getMaxClaimAmount());
				updatePstmt.setInt(7, payoutCenterBean.getOrgId());
				updatePstmt.addBatch();
			}
			insertPstmt.executeBatch();
			updatePstmt.executeBatch();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(insertPstmt);
			DBConnect.closePstmt(updatePstmt);
		}
	}
}