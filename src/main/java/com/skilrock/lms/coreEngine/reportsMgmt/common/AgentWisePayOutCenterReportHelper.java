package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.PayoutBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.web.reportsMgmt.common.AgentWisePayOutCenterReportAction;


public class AgentWisePayOutCenterReportHelper {
	Log logger = LogFactory.getLog(AgentWisePayOutCenterReportAction.class);
	
	public List<PayoutBean> fetchPayoutDataAgentWise() {
		logger.info("Inside fetchPayoutDataAgentWise");
		
		String orgCodeQuery = null;
		String queryOrderAppender = null;
		
		ResultSet rs = null;
		Statement stmt = null;
		Connection con = null;
		
		PayoutBean payoutBean = null;
		List<PayoutBean> payoutBeanList = null;
		try {
			
			orgCodeQuery=QueryManager.getOrgCodeQuery().replace("orgCode", "");
			queryOrderAppender=QueryManager.getAppendOrgOrder();
			
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select ol.organization_id,upper("+orgCodeQuery+") orgCode,block_amt,block_days,block_action from st_lms_oranization_limits ol inner join st_lms_organization_master om on ol.organization_id=om.organization_id and om.organization_status!='TERMINATE' and organization_type like 'AGENT'  order by "+queryOrderAppender);
			payoutBeanList = new ArrayList<PayoutBean>();
				while (rs.next()) {
					payoutBean = new PayoutBean();
					payoutBean.setOrganizationId(rs.getInt("organization_id"));
					payoutBean.setOrganizationName(rs.getString("orgCode"));
					payoutBean.setBlockAmt(rs.getDouble("block_amt"));
					payoutBean.setBlockDays(rs.getInt("block_days"));
					payoutBean.setBlockAction(rs.getString("block_action"));
					payoutBeanList.add(payoutBean);
				}
		} catch (Exception e) {
			logger.info("EXCEPTION :- " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}
		return payoutBeanList;
	}

}
