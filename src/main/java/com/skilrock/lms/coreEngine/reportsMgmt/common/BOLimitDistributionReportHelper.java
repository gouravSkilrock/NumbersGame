package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.LimitDistributionBean;
import com.skilrock.lms.beans.LimitDistributionReportBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGSaleReportsHelper;

public class BOLimitDistributionReportHelper {

	static Log logger = LogFactory.getLog(DGSaleReportsHelper.class);
	private Connection con = null;

	public Map<String, LimitDistributionReportBean> LimitDistributionOfAgentsForBO()
			throws LMSException {
		Map<String, LimitDistributionReportBean> map = new LinkedHashMap<String, LimitDistributionReportBean>();
		con = DBConnect.getConnection();
		LimitDistributionReportBean tempBean = null;
		
	
		
		String query = "select organization_id,limitTable.orgCode orgCode, credit_limit, extended_credit_limit,extends_credit_limit_upto, live_balance, distributed ,distributable,ifnull(count,0) terminal from (select organization_id,orgCode, credit_limit, extended_credit_limit,extends_credit_limit_upto, live_balance, ifnull(distributed,0) as distributed ,ifnull((live_balance - ifnull(distributed,0)),0) as distributable from (select organization_id, "
				+ QueryManager.getOrgCodeQuery()
				+ " , organization_type, credit_limit, extended_credit_limit,extends_credit_limit_upto,(available_credit-claimable_bal)as live_balance from st_lms_organization_master where organization_type='AGENT')main left outer join (select parent_id, ifnull(sum(if((available_credit-claimable_bal)>0,(available_credit-claimable_bal),0)),0) as distributed from st_lms_organization_master where organization_type = 'RETAILER' and parent_id in (select organization_id from st_lms_organization_master where organization_type= 'AGENT')group by parent_id)sub on main.organization_id = sub.parent_id  ) limitTable left join "
				+ "(select a.orgCode,a.model_name,a.organization_id orgId,a.inv_model_id,sum(a.count) count,a.current_owner_type from(select (select  "
				+ QueryManager.getOrgCodeQuery()
				+ " from st_lms_organization_master where organization_id=invTlb.organization_id) orgCode,"
				+ "organization_id,inv_model_id,sum(count) count, (select model_name from st_lms_inv_model_master where model_id=invTlb.inv_model_id) model_name,current_owner_type "
				+ "from (select  "
				+ QueryManager.getOrgCodeQuery()
				+ ", organization_id,inv_model_id,count(serial_no) count,current_owner_type from st_lms_organization_master inner join st_lms_inv_status on current_owner_type=organization_type"
				+ " and current_owner_id=organization_id where organization_type='AGENT' group by current_owner_id,inv_model_id union all select  "
				+ QueryManager.getOrgCodeQuery()
				+ ", parent_id,inv_model_id,count(inv_model_id)"
				+ " count,current_owner_type from st_lms_organization_master inner join st_lms_inv_status on current_owner_type=organization_type and current_owner_id=organization_id where organization_type='RETAILER'"
				+ " group by current_owner_id,inv_model_id) invTlb group by organization_id,inv_model_id) a inner join st_lms_inv_model_master b on b.model_id = a.inv_model_id where b.inv_id=1 group by a.orgCode) terCount on limitTable.organization_id=terCount.orgId order by "
				+ QueryManager.getAppendOrgOrder();

		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tempBean = new LimitDistributionReportBean();
				tempBean.setOrgId(rs.getInt("organization_id"));
				tempBean.setName(rs.getString("orgCode"));
				tempBean.setCrLimit(rs.getDouble("credit_limit"));
				tempBean.setXcrLimit(rs.getDouble("extended_credit_limit"));
				tempBean.setLiveBal(rs.getDouble("live_balance"));
				tempBean.setDistributedBal(rs.getDouble("distributed"));
				tempBean.setDistributableBal(rs.getDouble("distributable"));
				tempBean.setXcrLimitUpto(calculateExtendsCreditLimitUpto(rs
						.getDate("extends_credit_limit_upto")));
				tempBean.setTerminalCount(rs.getInt("terminal"));
				map.put(rs.getInt("organization_id") + "", tempBean);
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
	private int calculateExtendsCreditLimitUpto(java.sql.Date date) {
		if (date == null) {
			return 0;
		}
		long days = 0, hours = 0;

		Calendar today = Calendar.getInstance();
		today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today
				.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		Calendar extendedDate = Calendar.getInstance();
		extendedDate.setTimeInMillis(date.getTime());
		extendedDate.set(extendedDate.get(Calendar.YEAR), extendedDate
				.get(Calendar.MONTH), extendedDate.get(Calendar.DAY_OF_MONTH),
				0, 0, 1);

		long timeDiff = extendedDate.getTimeInMillis()
				- today.getTimeInMillis();
		if (timeDiff > 0) {
			days = timeDiff / (1000 * 60 * 60 * 24);
			hours = timeDiff / (1000 * 60 * 60);
		}
		// System.out.println(" dd days : "+days +" hours = "+hours);
		// System.out.println(date +", extendedDate = "+extendedDate.getTime()
		// +" ,today : "+today.getTime());

		return (int) days;
	}
	
	public Map<String, List<OrganizationBean>> fetchRetCreditUpdateReport(DateBeans dbean, int agtOrgId)throws LMSException{
		Map<String, List<OrganizationBean>> map = new HashMap<String, List<OrganizationBean>>();
		con = DBConnect.getConnection();
		OrganizationBean tempBean = null;

		String orgCodeQry = "om.name  orgCode";

		
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " om.org_code orgCode ";


		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(om.org_code,'_',om.name)  orgCode ";
		

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(om.name,'_',om.org_code)  orgCode ";
		

		}
		//String query = "select um.user_name,"+orgCodeQry+", omh.organization_id, omh.credit_limit, reason, comment, omh.organization_status, date_changed from st_lms_organization_master_history omh, st_lms_organization_master om, st_lms_organization_master oom, st_lms_user_master um where um.user_id = omh.user_id and um.organization_id = oom.organization_id and om.organization_id = omh.organization_id and om.organization_type='RETAILER' and om.parent_id = ?  and date(omh.date_changed)>=?and date(omh.date_changed) <= ? and omh.reason like 'CREDIT_CHANGED%'";
		String  query ="select  "+orgCodeQry+", omh.organization_id, omh.amount, date_time from st_lms_cl_xcl_update_history omh, st_lms_organization_master om where  om.organization_id = omh.organization_id and om.organization_type='RETAILER' and om.parent_id = ?  and date(omh.date_time)>=? and date(omh.date_time) <= ? and omh.type=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, agtOrgId);
			pstmt.setDate(2, dbean.getFirstdate());
			pstmt.setDate(3, new java.sql.Date(dbean.getLastdate().getTime()));
			pstmt.setString(4,"CL");
			logger.debug("org credit update report qry "+pstmt);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tempBean = new OrganizationBean();
				tempBean.setOrgId(rs.getInt("organization_id"));
				tempBean.setOrgName(rs.getString("orgCode"));
				//	tempBean.setOrgCountry(rs.getString("user_name"));
			//	tempBean.setScrapLimit(rs.getString("reason"));
			//	tempBean.setOrgCity(rs.getString("date_changed"));
				
				tempBean.setOrgCity(rs.getString("date_time"));
			//	tempBean.setOrgAddr1(rs.getString("doneOrgCode"));
	
					if(rs.getDouble("amount")<=0){
						tempBean.setOrgStatus("decreased");
					}else{
						tempBean.setOrgStatus("increased");
					}
					tempBean.setOrgCreditLimit(rs.getDouble("amount"));
					// tempBean.setOrgState(rs.getString("comment"));
				
				if(!map.containsKey(rs.getString("orgCode"))){
					List<OrganizationBean> tempList = new ArrayList<OrganizationBean>();
					tempList.add(tempBean);
					map.put(rs.getString("orgCode"), tempList);
				}
				else{
					map.get(rs.getString("orgCode")).add(tempBean);
				}
				
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
		logger.debug("org credit update report data: "+map);
		return map;
	}
}
