package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class AgentPayOutCenterHelper {
	static Log logger=LogFactory.getLog(AgentPayOutCenterHelper.class);

	public List<OrgPwtLimitBean> getAgentPwtLimitBeanList() throws LMSException {
		List<OrgPwtLimitBean> agentPwtLimitList = null;
		OrgPwtLimitBean agentPwtLimitBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs=stmt.executeQuery("select ol.organization_id,"
					+ QueryManager.getOrgCodeQuery()
					+ ",verification_limit,approval_limit,pay_limit,scrap_limit,ola_deposit_limit,ola_withdrawal_limit,max_daily_claim_amt, self_claim, other_claim, min_claim_per_ticket, max_claim_per_ticket, block_amt, block_days, block_action from st_lms_oranization_limits ol inner join st_lms_organization_master om on ol.organization_id=om.organization_id where om.organization_type='AGENT' and om.organization_status != 'TERMINATE' order by "
					+ QueryManager.getAppendOrgOrder());
					
			
			agentPwtLimitList = new ArrayList<OrgPwtLimitBean>();
			while (rs.next()) {
				agentPwtLimitBean = new OrgPwtLimitBean();
				agentPwtLimitBean.setOrganizationId(rs
						.getInt("organization_id"));
				agentPwtLimitBean.setOrgCode(rs.getString("orgCode"));
				agentPwtLimitBean.setVerificationLimit(rs
						.getDouble("verification_limit"));
				agentPwtLimitBean.setApprovalLimit(rs
						.getDouble("approval_limit"));
				agentPwtLimitBean.setPayLimit(rs.getDouble("pay_limit"));
				agentPwtLimitBean.setScrapLimit(rs.getDouble("scrap_limit"));
				agentPwtLimitBean.setOlaDepositLimit(rs
						.getDouble("ola_deposit_limit"));
				agentPwtLimitBean.setOlaWithdrawlLimit(rs
						.getDouble("ola_withdrawal_limit"));
				agentPwtLimitBean.setMaxDailyClaim(rs
						.getDouble("max_daily_claim_amt"));

				agentPwtLimitBean.setSelfClaim(rs.getString("self_claim"));
				agentPwtLimitBean.setOtherClaim(rs.getString("other_claim"));
				agentPwtLimitBean.setMinClaimPerTicket(rs.getDouble("min_claim_per_ticket"));
				agentPwtLimitBean.setMaxClaimPerTicket(rs.getDouble("max_claim_per_ticket"));
				agentPwtLimitBean.setBlockAmt(rs.getDouble("block_amt"));
				agentPwtLimitBean.setBlockDays(rs.getInt("block_days"));
				agentPwtLimitBean.setBlockAction(rs.getString("block_action"));
				agentPwtLimitList.add(agentPwtLimitBean);
			}

		}catch (SQLException se) {
			   logger.error("Exception",se);
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
				logger.error("Exception",e);
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
				try {
					DBConnect.closeConnection(con,stmt,rs);
					}
				 catch (Exception ee) {
					
					logger.error("Exception",ee);
					throw new LMSException(LMSErrors.CONNECTION_CLOSE_ERROR_CODE,LMSErrors.CONNECTION_CLOSE_ERROR_MESSAGE);

				}
		}
		return agentPwtLimitList;
	}

	public List<OrgPwtLimitBean> updateOrgnizationLimit(List<OrgPwtLimitBean> OrgPwtLimitBeanList,int userId) throws LMSException{

		PreparedStatement pstmt = null;

		Connection con = null;
		OrgPwtLimitBean OrgPwtLimitBean = null;
		List<Integer> orgIdList = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			StringBuilder selectQuery = new StringBuilder();

			String selectQry = "SELECT ? organization_id, ? block_amt, ? block_days, ? block_action UNION ";
			orgIdList = new ArrayList<Integer>();
			for (int j = 0; j < OrgPwtLimitBeanList.size(); j++) {
				selectQuery.append(selectQry);
				orgIdList.add(OrgPwtLimitBeanList.get(j).getOrganizationId());
			}
			selectQuery.delete(selectQuery.lastIndexOf("UNION"), selectQuery.length());

			pstmt = con.prepareStatement("INSERT INTO st_lms_oranization_limits_history (date_changed, change_by_user_id, organization_id, verification_limit, approval_limit, pay_limit, scrap_limit, ola_deposit_limit, ola_withdrawal_limit, max_daily_claim_amt, block_amt, block_days, block_action) SELECT ?, ?, organization_id, verification_limit, approval_limit, pay_limit, scrap_limit, ola_deposit_limit, ola_withdrawal_limit, max_daily_claim_amt, block_amt, block_days, block_action FROM st_lms_oranization_limits WHERE FIND_IN_SET (organization_id,?);");
			pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
			pstmt.setInt(2, userId);
			pstmt.setString(3, orgIdList.toString().replace("[", "").replace(
					"]", "").replace(" ", ""));
			logger.info("insert Limit history Table:"+pstmt);
			pstmt.executeUpdate();

			String updateQuery = "UPDATE st_lms_oranization_limits ol INNER JOIN ("+selectQuery.toString()+") main ON ol.organization_id=main.organization_id SET ol.block_amt=main.block_amt, ol.block_days=main.block_days, ol.block_action=main.block_action WHERE ol.organization_id=main.organization_id;";
			pstmt = con.prepareStatement(updateQuery);
			int count = 0;

			for (int j = 0; j < OrgPwtLimitBeanList.size(); j++) {
				OrgPwtLimitBean = new OrgPwtLimitBean();
				OrgPwtLimitBean = OrgPwtLimitBeanList.get(j);
				pstmt.setLong(++count, OrgPwtLimitBean.getOrganizationId());
				pstmt.setDouble(++count, OrgPwtLimitBean.getBlockAmt());
				pstmt.setInt(++count, OrgPwtLimitBean.getBlockDays());
				pstmt.setString(++count, OrgPwtLimitBean.getBlockAction());
			}
			logger.info("update Pwt Limit Data :" + pstmt);

			int isUpdate = pstmt.executeUpdate();
			logger.info("Update No Of Organizations:"+isUpdate);
			con.commit();
			
			OrgPwtLimitBeanList=getUpdatedAgentPwtLimitBeanList(orgIdList,con);
			
			
		}catch (SQLException se) {
			   logger.error("Exception",se);
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
				logger.error("Exception",e);
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
				try {
					DBConnect.closeConnection(con,pstmt);
					}
				 catch (Exception ee) {
					
					logger.error("Exception",ee);
					throw new LMSException(LMSErrors.CONNECTION_CLOSE_ERROR_CODE,LMSErrors.CONNECTION_CLOSE_ERROR_MESSAGE);

				}
		}
		return OrgPwtLimitBeanList;
	}
	
	public List<OrgPwtLimitBean> getUpdatedAgentPwtLimitBeanList(List<Integer> orgIdList,Connection con) throws LMSException {
		List<OrgPwtLimitBean> agentPwtLimitList = null;
		OrgPwtLimitBean agentPwtLimitBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			stmt = con.createStatement();
			//rs=stmt.executeQuery("SELECT ol.organization_id, "+QueryManager.getOrgCodeQuery()+", verification_limit, approval_limit, pay_limit, scrap_limit, ola_deposit_limit, ola_withdrawal_limit, max_daily_claim_amt, claim_any_ticket, is_acting_as_bo_for_pwt, block_amt, block_days, block_action FROM st_lms_oranization_limits ol INNER JOIN st_lms_organization_master om ON ol.organization_id=om.organization_id WHERE om.organization_type='AGENT' AND om.organization_status!='TERMINATE' ORDER BY "+QueryManager.getAppendOrgOrder());
			rs=stmt.executeQuery("SELECT ol.organization_id, "+QueryManager.getOrgCodeQuery()+", block_amt, block_days, block_action FROM st_lms_oranization_limits ol INNER JOIN st_lms_organization_master om ON ol.organization_id=om.organization_id WHERE om.organization_type='AGENT' AND om.organization_status!='TERMINATE' ORDER BY "+QueryManager.getAppendOrgOrder());

			agentPwtLimitList = new ArrayList<OrgPwtLimitBean>();
			while (rs.next()) {
				agentPwtLimitBean = new OrgPwtLimitBean();
				agentPwtLimitBean.setOrganizationId(rs
						.getInt("organization_id"));
				agentPwtLimitBean.setOrgCode(rs.getString("orgCode"));
				agentPwtLimitBean.setBlockAmt(rs.getDouble("block_amt"));
				agentPwtLimitBean.setBlockDays(rs.getInt("block_days"));
				agentPwtLimitBean.setBlockAction(rs.getString("block_action"));

				if(orgIdList.contains(rs.getInt("organization_id")))
					agentPwtLimitBean.setStatus("Update Successfully.");

				agentPwtLimitList.add(agentPwtLimitBean);
			}

		}catch (SQLException se) {
			   logger.error("Exception",se);
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
				logger.error("Exception",e);
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} 
		return agentPwtLimitList;
	}

	
}
