package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.NewRetActivityBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.RetActivityColumnStatusBean;
import com.skilrock.lms.beans.RetailerActivityHistoryBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;


public class NewRetActivityReportHelper {

	static Log logger = LogFactory.getLog(NewRetActivityReportHelper.class);

	public Map<Integer, NewRetActivityBean> fetchActivityTrx(int agentOrgId,
			String selectMode, ReportStatusBean reportStatusBean) throws LMSException {
		Connection con = null;
		Map<Integer, NewRetActivityBean> map = new LinkedHashMap<Integer, NewRetActivityBean>();
		NewRetActivityBean retActBean = null;
		String orgNameQry = null;
		StringBuilder address = null;
		Statement stmt = null;
		try {
			if ("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();
			orgNameQry = getOrgNameQuery(agentOrgId, selectMode);
			if (orgNameQry == null)
				throw new LMSException("Some Internal Error Occur : ");

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(orgNameQry);
			Calendar calLastSaleDate = Calendar.getInstance();
			Calendar calCurrentDate =Calendar.getInstance();
			while (rs.next()) {
				address = new StringBuilder();
				retActBean = new NewRetActivityBean();
				retActBean.setRetOrgId(rs.getInt("organization_id"));
				retActBean.setLogin(rs.getTimestamp("last_login_time"));
				retActBean.setRetName(rs.getString("orgCode"));
				retActBean.setRetParentName(rs.getString("parentorgCode"));
				retActBean.setOrgStatus(rs.getString("organization_status"));
				retActBean.setCurrentVersion(rs.getString("current_version"));
				if(rs.getString("serial_number").equalsIgnoreCase("-1")||rs.getString("device_type").equalsIgnoreCase("-1")){
					retActBean.setTerminalId("NA-NA");
				}else{
					retActBean.setTerminalId(rs.getString("device_type")+"-"+rs.getString("serial_number"));
				}
			
				retActBean.setRetUserId(rs.getInt("user_id"));
				//retActBean.setRetName(rs.getString("name"));
				retActBean.setLoginStatus(rs.getString("login_status"));
				//retActBean.setRetParentName(rs.getString("parentName"));
				retActBean.setLocation(rs.getString("city"));
				if (rs.getString("lat")!=null && !rs.getString("lat").equals("0.000000"))
					address = address.append(rs.getString("lat")).append("Nxt")
							.append(rs.getString("lon"));
				else
					address = new StringBuilder("0.000000");
				retActBean.setRetCoordinate(address.toString());
				retActBean.setRetAddress(rs.getString("city"));
				retActBean.setLastOlaDepositTime(rs
						.getTimestamp("ola_last_deposit_time"));
				retActBean.setLastOlaWithdrawalTime(rs
						.getTimestamp("ola_last_withdrawal_time"));
				Timestamp dgLastSale =rs.getTimestamp("dg_last_sale_time");
				if(dgLastSale!=null){
					calLastSaleDate.setTimeInMillis(dgLastSale.getTime());
					retActBean.setDays(ReportUtility.getDaysBetweenDate(calLastSaleDate,calCurrentDate));
				}else {
					
					retActBean.setDays(-1);
				}
				Timestamp sleLastSale =rs.getTimestamp("sle_last_sale_time");

				if(sleLastSale!=null){
					calLastSaleDate.setTimeInMillis(sleLastSale.getTime());
					retActBean.setSleDays(ReportUtility.getDaysBetweenDate(calLastSaleDate,calCurrentDate));
				}else {
					retActBean.setSleDays(-1);
				}
				Timestamp iwLastSale =rs.getTimestamp("iw_last_sale_time");
				if(iwLastSale!=null){
					calLastSaleDate.setTimeInMillis(iwLastSale.getTime());
					retActBean.setIwDays(ReportUtility.getDaysBetweenDate(calLastSaleDate,calCurrentDate));
				}else {
					retActBean.setIwDays(-1);
				}
				
				Timestamp vsLastSale = rs.getTimestamp("vs_last_sale_time");
				if (vsLastSale != null) {
					calLastSaleDate.setTimeInMillis(vsLastSale.getTime());
					retActBean.setVsDays(ReportUtility.getDaysBetweenDate(
							calLastSaleDate, calCurrentDate));
				} else {
					retActBean.setVsDays(-1);
				}

				retActBean.setDrawSale(dgLastSale);
				retActBean.setDrawPwt(rs.getTimestamp("dg_last_pwt_time"));
				retActBean.setIwSale(rs.getTimestamp("iw_last_sale_time"));
				retActBean.setIwPwt(rs.getTimestamp("iw_last_pwt_time"));
				retActBean.setVsSale(rs.getTimestamp("vs_last_sale_time"));
				retActBean.setVsPwt(rs.getTimestamp("vs_last_pwt_time"));
				retActBean.setScratchSale(rs.getTimestamp("se_last_sale_time"));
				retActBean.setScratchPwt(rs.getTimestamp("se_last_pwt_time"));
				retActBean.setSleSale(rs.getTimestamp("sle_last_sale_time"));
				retActBean.setSlePwt(rs.getTimestamp("sle_last_pwt_time"));
				retActBean.setLastConDevice(rs
						.getString("last_connected_through"));
				retActBean.setLastCSSaleTime(rs
						.getTimestamp("cs_last_sale_time"));
				retActBean.setLastHeartBeatTime(rs
						.getTimestamp("last_HBT_time"));
				map.put(rs.getInt("organization_id"), retActBean);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
				DBConnect.closeCon(con);
			}
		logger.debug("Size of data map :  " + map.size());
		return map;
	}

	public RetActivityColumnStatusBean setActReportColumnStatus(String actType,
			String selectMode) {

		RetActivityColumnStatusBean statusBean = new RetActivityColumnStatusBean();
		statusBean.setDG(LMSFilterDispatcher.getIsDraw()
				.equalsIgnoreCase("YES"));
		statusBean.setSE(LMSFilterDispatcher.getIsScratch().equalsIgnoreCase(
				"YES"));
		statusBean.setOLA(LMSFilterDispatcher.getIsOLA()
				.equalsIgnoreCase("YES"));
		statusBean.setCS(LMSFilterDispatcher.getIsCS().equalsIgnoreCase("YES"));
		
		statusBean.setSLE(LMSFilterDispatcher.getIsSLE().equalsIgnoreCase("YES"));
		statusBean.setIW(LMSFilterDispatcher.getIsIW().equalsIgnoreCase("YES"));
		statusBean.setVS(LMSFilterDispatcher.getIsVS().equalsIgnoreCase("YES"));
		if (actType.equalsIgnoreCase("SALE"))
			statusBean.setSale(true);
		if (actType.equalsIgnoreCase("PWT"))
			statusBean.setPwt(true);
		if (actType.equalsIgnoreCase("LOGIN"))
			statusBean.setLogin(true);
		if (selectMode.equalsIgnoreCase("HEARTBEAT"))
			statusBean.setHeartBeat(true);

		return statusBean;
	}

	public String getOrgNameQuery(int agentOrgId, String selectMode) {
		String orgNameQry = null;
		
		
		String appendOrder =QueryManager.getAppendOrgOrder();
		String orgCodeQry = " om.name orgCode,parent.name parentorgCode ";
		//String parentorgCodeQry = " name parentorgCode ";
		
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " om.org_code orgCode,parent.org_code parentorgCode ";
			//parentorgCodeQry="org_code parentorgCode ";

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(om.org_code,'_',om.name)  orgCode,concat(parent.org_code,'_',parent.name)parentorgCode";
			// parentorgCodeQry = " concat(org_code,'_',name) parentorgCode ";

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = "concat(om.name,'_',om.org_code)  orgCode,concat(parent.name,'_',parent.org_code)parentorgCode ";
			//parentorgCodeQry = " concat(name,'_',org_code) parentorgCode ";

		}
		/*String orgCodeQry = " om.name orgCode,parent.parentorgCode ";
		String parentorgCodeQry = " name parentorgCode ";
		
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " om.org_code orgCode,parent.parentorgCode ";
			parentorgCodeQry="org_code parentorgCode ";

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(om.org_code,'_',om.name)  orgCode,parent.parentorgCode ";
			parentorgCodeQry = " concat(org_code,'_',name) parentorgCode ";

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(om.name,'_',om.org_code)  orgCode,parent.parentorgCode ";
			parentorgCodeQry = " concat(name,'_',org_code) parentorgCode ";

		}*/
		
		
		if (selectMode.equalsIgnoreCase("ACTIVITY")
				|| selectMode.equalsIgnoreCase("HEARTBEAT")) {
			if (agentOrgId == -1) {
				 //  orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time, last_connected_through, last_login_time,city,organization_status,lat,lon from st_lms_ret_offline_master rm,(select "+parentorgCodeQry+",organization_id from st_lms_organization_master where organization_type='AGENT')parent,st_lms_organization_master om where rm.organization_id=om.organization_id and parent.organization_id=om.parent_id  and organization_status!='TERMINATE' and serial_number!=-1 order by parentorgCode,lat,"+appendOrder;
				orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,iw_last_sale_time,iw_last_pwt_time,vs_last_sale_time,vs_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time,sle_last_sale_time,sle_last_pwt_time, last_connected_through, last_login_time,om.city,om.organization_status,lat,lon from st_lms_ret_offline_master rm inner join st_lms_organization_master om on rm.organization_id=om.organization_id inner join st_lms_organization_master parent on om.parent_id =parent.organization_id where om.organization_status not in ('TERMINATE', 'BLOCK') and serial_number!=-1 order by parentorgCode,lat,"+appendOrder;
			
			} else {
				/*orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status,login_status,serial_number, current_version, device_type, profile,last_HBT_time,dg_last_sale_time,dg_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time, last_connected_through,last_login_time,addr_line1,addr_line2,city,organization_status,lat,lon from st_lms_ret_offline_master rm,(select "+parentorgCodeQry+",organization_id from st_lms_organization_master where organization_id="
						+ agentOrgId
						+ ") parent,st_lms_organization_master om where rm.organization_id=om.organization_id and parent.organization_id=om.parent_id  and organization_status!='TERMINATE' and serial_number!=-1 order by parentorgCode,"+appendOrder;*/
				orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,iw_last_sale_time,iw_last_pwt_time,vs_last_sale_time,vs_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time,sle_last_sale_time,sle_last_pwt_time, last_connected_through, last_login_time,om.city,om.organization_status,lat,lon from st_lms_ret_offline_master rm inner join st_lms_organization_master om on rm.organization_id=om.organization_id inner join st_lms_organization_master parent on om.parent_id =parent.organization_id where om.organization_status not in ('TERMINATE', 'BLOCK') and serial_number!=-1 and parent.organization_id="+agentOrgId+" order by parentorgCode,lat,"+appendOrder;
			
			
			}
		} else if (selectMode.equalsIgnoreCase("TERMINATE")) {
			if (agentOrgId == -1) {
				// orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time, last_connected_through, last_login_time,addr_line1,addr_line2,city,organization_status,lat,lon from st_lms_ret_offline_master rm,(select "+parentorgCodeQry+",organization_id from st_lms_organization_master where organization_type='AGENT')parent,st_lms_organization_master om where rm.organization_id=om.organization_id and parent.organization_id=om.parent_id  and organization_status='TERMINATE' order by parentorgCode,"+appendOrder;
				orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,iw_last_sale_time,iw_last_pwt_time,vs_last_sale_time,vs_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time,sle_last_sale_time,sle_last_pwt_time, last_connected_through, last_login_time,om.city,om.organization_status,lat,lon from st_lms_ret_offline_master rm inner join st_lms_organization_master om on rm.organization_id=om.organization_id inner join st_lms_organization_master parent on om.parent_id =parent.organization_id where om.organization_status in ('TERMINATE', 'BLOCK') order by parentorgCode,lat,"+appendOrder;
			
			} else {
				/*orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status,login_status,serial_number, current_version, device_type, profile,last_HBT_time,dg_last_sale_time,dg_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time, last_connected_through,last_login_time,addr_line1,addr_line2,city,organization_status,lat,lon from st_lms_ret_offline_master rm,(select "+parentorgCodeQry+",organization_id from st_lms_organization_master where organization_id="
						+ agentOrgId
						+ ") parent,st_lms_organization_master om where rm.organization_id=om.organization_id and parent.organization_id=om.parent_id  and organization_status='TERMINATE' order by parentorgCode,"+appendOrder;*/
				orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,iw_last_sale_time,iw_last_pwt_time,vs_last_sale_time,vs_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time,sle_last_sale_time,sle_last_pwt_time, last_connected_through, last_login_time,om.city,om.organization_status,lat,lon from st_lms_ret_offline_master rm inner join st_lms_organization_master om on rm.organization_id=om.organization_id inner join st_lms_organization_master parent on om.parent_id =parent.organization_id where om.organization_status in ('TERMINATE', 'BLOCK')  and parent.organization_id="+agentOrgId+" order by parentorgCode,lat,"+appendOrder;	
			}
		} else {
			if (agentOrgId == -1) { // for Select Mode "Not Terminal"
					//orgNameQry = "select 	"+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time, last_connected_through, last_login_time,addr_line1,addr_line2,city,organization_status,lat,lon from st_lms_ret_offline_master rm,(select "+parentorgCodeQry+",organization_id from st_lms_organization_master where organization_type='AGENT')parent,st_lms_organization_master om where rm.organization_id=om.organization_id and parent.organization_id=om.parent_id  and serial_number=-1 order by parentorgCode,"+appendOrder;
				orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,iw_last_sale_time,iw_last_pwt_time,vs_last_sale_time,vs_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time,sle_last_sale_time,sle_last_pwt_time, last_connected_through, last_login_time,om.city,om.organization_status,lat,lon from st_lms_ret_offline_master rm inner join st_lms_organization_master om on rm.organization_id=om.organization_id inner join st_lms_organization_master parent on om.parent_id =parent.organization_id where serial_number=-1 order by parentorgCode,lat,"+appendOrder;
			} else {
				/*orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status,login_status,serial_number, current_version, device_type, profile,last_HBT_time,dg_last_sale_time,dg_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time, last_connected_through,last_login_time,addr_line1,addr_line2,city,organization_status,lat,lon from st_lms_ret_offline_master rm,(select "+parentorgCodeQry+",organization_id from st_lms_organization_master where organization_id="
						+ agentOrgId
						+ ") parent,st_lms_organization_master om where rm.organization_id=om.organization_id and parent.organization_id=om.parent_id  and serial_number=-1 order by parentorgCode,"+appendOrder;*/
				
				orgNameQry = "select "+orgCodeQry+",user_id, rm.organization_id, offline_status, login_status, serial_number, current_version, device_type, profile, last_HBT_time,dg_last_sale_time,dg_last_pwt_time,iw_last_sale_time,iw_last_pwt_time,vs_last_sale_time,vs_last_pwt_time,se_last_sale_time,se_last_pwt_time,ola_last_deposit_time,ola_last_withdrawal_time,cs_last_sale_time,sle_last_sale_time,sle_last_pwt_time, last_connected_through, last_login_time,om.city,om.organization_status,lat,lon from st_lms_ret_offline_master rm inner join st_lms_organization_master om on rm.organization_id=om.organization_id inner join st_lms_organization_master parent on om.parent_id =parent.organization_id where serial_number=-1 and parent.organization_id="+agentOrgId+" order by parentorgCode,lat,"+appendOrder;
			}
		}
		return orgNameQry;
	}

	public Map<String, RetailerActivityHistoryBean> fetchActRepHistoryForDrawGame(
			String startDate, String endDate, ReportStatusBean reportStatusBean) throws LMSException {
		Connection con = null;

		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String query = "";
		Map<String, RetailerActivityHistoryBean> hMap = new LinkedHashMap<String, RetailerActivityHistoryBean>();
		RetailerActivityHistoryBean hBean = null;
		try {
			query = "select date,ifnull(dg_RC,0) dg_RC,ifnull(dg_sale_RC,0) dg_sale_RC,ifnull(dg_pwt_RC,0) dg_pwt_RC,ifnull(sl_RC,0) sl_RC,ifnull(sl_sale_RC,0) sl_sale_RC,ifnull(sl_pwt_RC,0) sl_pwt_RC,ifnull(iw_RC,0) iw_RC,ifnull(iw_sale_RC,0) iw_sale_RC,ifnull(iw_pwt_RC,0) iw_pwt_RC, ifnull(vs_RC,0) vs_RC,ifnull(vs_sale_RC,0) vs_sale_RC,ifnull(vs_pwt_RC,0) vs_pwt_RC,ifnull(se_RC,0) se_RC,ifnull(se_sale_RC,0) se_sale_RC,ifnull(se_pwt_RC,0) se_pwt_RC,ifnull(ola_RC,0) ola_RC,ifnull(ola_deposit_RC,0) ola_deposit_RC,ifnull(ola_wd_RC,0) ola_wd_RC,ifnull(cs_RC,0) cs_RC,ifnull(cs_sale_RC,0) cs_sale_RC,ifnull(login_RC,0) login_RC,ifnull(heartBeat_RC,0) heartBeat_RC,ifnull(inactive_retailers,0) inactive_retailers,ifnull(terminated_retailers,0) terminated_retailers,ifnull(total_RC,0)  total_RC from st_lms_new_ret_activity_history where date>=? and date<?;";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				hBean = new RetailerActivityHistoryBean();
				hBean.setDgCount(rs.getInt("dg_RC"));
				hBean.setDgSaleCount(rs.getInt("dg_sale_RC"));
				hBean.setDgPwtCount(rs.getInt("dg_pwt_RC"));
				hBean.setSlCount(rs.getInt("sl_RC"));
				hBean.setSlSaleCount(rs.getInt("sl_sale_RC"));
				hBean.setSlPwtCount(rs.getInt("sl_pwt_RC"));
				hBean.setIwCount(rs.getInt("iw_RC"));
				hBean.setIwSaleCount(rs.getInt("iw_sale_RC"));
				hBean.setIwPwtCount(rs.getInt("iw_pwt_RC"));
				hBean.setVsCount(rs.getInt("vs_RC"));
				hBean.setVsSaleCount(rs.getInt("vs_sale_RC"));
				hBean.setVsPwtCount(rs.getInt("vs_pwt_RC"));
				hBean.setSeCount(rs.getInt("se_RC"));
				hBean.setSeSaleCount(rs.getInt("se_sale_RC"));
				hBean.setSePwtCount(rs.getInt("se_pwt_RC"));
				hBean.setOlaCount(rs.getInt("ola_RC"));
				hBean.setOlaDepoCount(rs.getInt("ola_deposit_RC"));
				hBean.setOlaWdlCount(rs.getInt("ola_wd_RC"));
				hBean.setCsCount(rs.getInt("cs_RC"));
				hBean.setCsSaleCount(rs.getInt("cs_sale_RC"));
				hBean.setLoginCount(rs.getInt("login_RC"));
				hBean.setHeartBeatCount(rs.getInt("heartBeat_RC"));
				hBean.setInactiveRetCount(rs.getInt("inactive_retailers"));
				hBean.setTerRetCount(rs.getInt("terminated_retailers"));
				hBean.setTotalCount(rs.getInt("total_RC"));
				hMap.put(df.format(rs.getDate("date")), hBean);
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
		return hMap;
	}

	public Map<String, RetailerActivityHistoryBean> fetchRetTransactionVolumeHistory(
			String startDate, String endDate, ReportStatusBean reportStatusBean) throws LMSException {
		Connection con = null;

		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String query = "";
		Map<String, RetailerActivityHistoryBean> hMap = new LinkedHashMap<String, RetailerActivityHistoryBean>();
		RetailerActivityHistoryBean hBean = null;
		try {
			query = "select date,ifnull(dg_total_sales,0.0) dg_total_sales,ifnull(dg_total_pwt,0.0) dg_total_pwt,ifnull(sl_total_sales,0.0) sl_total_sales,ifnull(sl_total_pwt,0.0) sl_total_pwt,ifnull(iw_total_sales,0.0) iw_total_sales,ifnull(iw_total_pwt,0.0) iw_total_pwt,ifnull(vs_total_sales,0.0) vs_total_sales,ifnull(vs_total_pwt,0.0) vs_total_pwt,ifnull(se_total_sales,0.0) se_total_sales,ifnull(se_total_pwt,0.0) se_total_pwt,ifnull(ola_total_deposit,0.0) ola_total_deposit,ifnull(ola_total_wd,0.0) ola_total_wd,ifnull(cs_total_sale,0.0) cs_total_sale,ifnull(dg_tkt_count,0) dg_tkt_count,ifnull(dg_pwt_count,0) dg_pwt_count,ifnull(dg_avg_sale_per_ret,0.0) dg_avg_sale_per_ret,ifnull(sl_tkt_count,0) sl_tkt_count,ifnull(sl_pwt_count,0) sl_pwt_count,ifnull(sl_avg_sale_per_ret,0.0) sl_avg_sale_per_ret,ifnull(iw_tkt_count,0) iw_tkt_count,ifnull(iw_pwt_count,0) iw_pwt_count,ifnull(iw_avg_sale_per_ret,0.0) iw_avg_sale_per_ret, ifnull(vs_tkt_count,0) vs_tkt_count, ifnull(vs_pwt_count,0) vs_pwt_count, ifnull(vs_avg_sale_per_ret,0.0) vs_avg_sale_per_ret from st_lms_new_ret_activity_history where date>=? and date<?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				hBean = new RetailerActivityHistoryBean();
				hBean.setDgTotalSale(rs.getDouble("dg_total_sales"));
				hBean.setDgTotalPwt(rs.getDouble("dg_total_pwt"));
				hBean.setSlTotalSale(rs.getDouble("sl_total_sales"));
				hBean.setSlTotalPwt(rs.getDouble("sl_total_pwt"));
				hBean.setIwTotalSale(rs.getDouble("iw_total_sales"));
				hBean.setIwTotalPwt(rs.getDouble("iw_total_pwt"));
				hBean.setVsTotalSale(rs.getDouble("vs_total_sales"));
				hBean.setVsTotalPwt(rs.getDouble("vs_total_pwt"));
				hBean.setSeTotalSale(rs.getDouble("se_total_sales"));
				hBean.setSeTotalPwt(rs.getDouble("se_total_pwt"));
				hBean.setOlaTotalDepo(rs.getDouble("ola_total_deposit"));
				hBean.setOlaTotalWdrl(rs.getDouble("ola_total_wd"));
				hBean.setCsTotalSale(rs.getDouble("cs_total_sale"));
				hBean.setDgTicketCount(rs.getInt("dg_tkt_count"));
				hBean.setDgPwtTotalCount(rs.getInt("dg_pwt_count"));
				hBean.setDgAvgSalePerRet(rs.getDouble("dg_avg_sale_per_ret"));
				hBean.setSlTicketCount(rs.getInt("sl_tkt_count"));
				hBean.setSlPwtTotalCount(rs.getInt("sl_pwt_count"));
				hBean.setSlAvgSalePerRet(rs.getDouble("sl_avg_sale_per_ret"));
				hBean.setIwTicketCount(rs.getInt("iw_tkt_count"));
				hBean.setIwPwtTotalCount(rs.getInt("iw_pwt_count"));
				hBean.setIwAvgSalePerRet(rs.getDouble("iw_avg_sale_per_ret"));
				
				hBean.setVsTicketCount(rs.getInt("vs_tkt_count"));
				hBean.setVsPwtTotalCount(rs.getInt("vs_pwt_count"));
				hBean.setVsAvgSalePerRet(rs.getDouble("vs_avg_sale_per_ret"));
				
				hMap.put(df.format(rs.getDate("date")), hBean);
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
		return hMap;
	}

	public Map<String, List<Integer>> fetchRetVersionHistory(String startDate,
			String selectDevice, List<String> list) throws LMSException {
		Connection con = null;
		con = DBConnect.getConnection();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//StringBuilder sb = new StringBuilder();
		//StringBuilder sb1 = new StringBuilder();
		
		List<Integer> tempList = null;
		Map<String, List<Integer>> hMap = new LinkedHashMap<String, List<Integer>>();
		try {
			//if (list.size() > 0) {
				/*sb.append("select date,");
				for (int j = 1; j <= list.size(); j++) {
					sb.append(
							"sum(version_" + j + "_count) version_" + j
									+ "_count").append(",");
					sb1.append(
							"if(version='" + list.get(j - 1)
									+ "',ret_count,0) version_" + j + "_count")
							.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb1.deleteCharAt(sb1.length() - 1);
				sb.append(" from (select date,");
				sb.append(sb1);
				sb
						.append(" from (select device_type,current_version version,date,sum(ret_count) ret_count from st_lms_pos_version_history ch where date=? and ch.device_type = ? and date is not null group by date,current_version) mm group by date,version) gg group by date");
				//PreparedStatement pstmt = con.prepareStatement(sb.toString());
*/				PreparedStatement pstmt = con.prepareStatement("select device_type,current_version version,date,sum(ret_count) ret_count from st_lms_pos_version_history ch where date=? and ch.device_type =? and date is not null group by date,current_version");
				pstmt.setString(1, startDate);
				pstmt.setString(2, selectDevice);
				logger.info("version list query"+pstmt);
				ResultSet rs = pstmt.executeQuery();
				tempList = new LinkedList<Integer>();
				while (rs.next()) {
				
				//	for (int i = 1; i <= list.size(); i++) {
					
							tempList.add(rs.getInt("ret_count"));
							list.add(rs.getString("version"));
						
						
				//	}
				
				}
				hMap.put(startDate, tempList);
			//}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} catch(Exception e){
			logger.error("Exception: " + e);
			e.printStackTrace();
			
		}finally {
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
		return hMap;
	}

	public Map<String, List<String>> fetchRetDeviceHistory(String startDate,
			String endDate, List<String> list, ReportStatusBean reportStatusBean) throws LMSException {
		Connection con = null;

		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		List<String> tempList = null;
		Map<String, List<String>> hMap = new LinkedHashMap<String, List<String>>();
		try {
			if (list.size() > 0) {
				sb.append("select date,");
				for (int j = 1; j <= list.size(); j++) {
					sb.append(
							"sum(device_" + j + "_count) device_" + j
									+ "_count").append(",");
					sb1.append(
							"if(device_type='" + list.get(j - 1)
									+ "',ret_count,0) device_" + j + "_count")
							.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb1.deleteCharAt(sb1.length() - 1);
				sb.append(" from (select date,");
				sb.append(sb1);
				sb
						.append(" from (select cm.device_type,date,sum(ret_count) ret_count from st_lms_htpos_device_master cm left join st_lms_pos_version_history ch on cm.device_type = ch.device_type where date>=? and date<? and date is not null group by date,cm.device_type) mm group by date,device_type) gg group by date");
				PreparedStatement pstmt = con.prepareStatement(sb.toString());
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					tempList = new LinkedList<String>();
					for (int i = 1; i <= list.size(); i++) {
						StringBuilder sb2 = new StringBuilder();
						sb2.append(rs.getInt("device_" + i + "_count")).append(
								"Nxt").append(list.get(i - 1));
						tempList.add(sb2.toString());
					}
					hMap.put(df.format(rs.getDate("date")), tempList);
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
		return hMap;
	}
	

	public Map<String, List<Integer>> fetchRetConnModeHistory(String startDate,
			String endDate, List<String> list, ReportStatusBean reportStatusBean) throws LMSException {
		Connection con = null;

		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		List<Integer> tempList = null;
		Map<String, List<Integer>> hMap = new LinkedHashMap<String, List<Integer>>();
		try {
			if (list.size() > 0) {
				sb.append("select date,");
				for (int j = 1; j <= list.size(); j++) {
					sb.append("sum(sim_" + j + "_count) sim_" + j + "_count")
							.append(",");
					sb1.append(
							"if(id=" + j + ",ret_count,0) sim_" + j + "_count")
							.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb1.deleteCharAt(sb1.length() - 1);
				sb.append(" from (select date,");
				sb.append(sb1);
				sb
						.append(" from (select cm.id,date,sim_name,sum(ret_count) ret_count from st_lms_con_device_master cm left join st_lms_daily_connectivity_history ch on cm.id = ch.sim_id  where date>=? and date<? and date is not null group by date,id) mm group by date,id) gg group by date");
				PreparedStatement pstmt = con.prepareStatement(sb.toString());
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					tempList = new LinkedList<Integer>();
					for (int i = 1; i <= list.size(); i++) {
						tempList.add(rs.getInt("sim_" + i + "_count"));
					}
					hMap.put(df.format(rs.getDate("date")), tempList);
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
		return hMap;
	}

	public Map<String, List<Integer>> fetchRetLocationHistory(String startDate,	String endDate,
			List<String> list, String selectService, ReportStatusBean reportStatusBean)
			throws LMSException {
		Connection con = null;

		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		List<Integer> tempList = null;
		Map<String, List<Integer>> hMap = new LinkedHashMap<String, List<Integer>>();
		try {
			if (list!=null && list.size() > 0) {
				sb.append("select date,");
				for (int j = 1; j <= list.size(); j++) {
					sb.append("sum(city_" + j + "_count) city_" + j + "_count")
							.append(",");
					sb1.append(
							"if(city_name='" + list.get(j-1).split("\\(")[0] + "',ret_count,0) city_" + j + "_count")
							.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb1.deleteCharAt(sb1.length() - 1);
				sb.append(" from (select date,");
				sb.append(sb1);
				sb
						.append(" from (select cm.city_code,date,city_name,");
				if (selectService.equals("Draw Game")) {
					sb.append("sum(dg_RC)");
				}
				if (selectService.equals("Draw Game Sale")) {
						sb.append("sum(dg_sale_RC)");
					} 
				if (selectService.equals("Draw Game Pwt")) {
						sb.append("sum(dg_pwt_RC)");
					}
				if (selectService.equals("Sports Lottery")) {
					sb.append("sum(sl_RC)");
				}
				if (selectService.equals("Sports Lottery Sale")) {
						sb.append("sum(sl_sale_RC)");
					} 
				if (selectService.equals("Sports Lottery Pwt")) {
						sb.append("sum(sl_pwt_RC)");
					}
				if (selectService.equals("Instant Win")) {
					sb.append("sum(iw_RC)");
				}
				if (selectService.equals("Instant Win Sale")) {
						sb.append("sum(iw_sale_RC)");
					} 
				if (selectService.equals("Instant Win Pwt")) {
						sb.append("sum(iw_pwt_RC)");
					}
				
				if (selectService.equals("Virtual Sports")) {
					sb.append("sum(vs_RC)");
				}
				if (selectService.equals("Virtual Sports Sale")) {
					sb.append("sum(vs_sale_RC)");
				}
				if (selectService.equals("Virtual Sports Pwt")) {
					sb.append("sum(vs_pwt_RC)");
				}
				
				if (selectService.equals("Scratch Game")) {
					sb.append("sum(se_RC)");
				}
				if (selectService.equals("Scratch Game Sale")) {
						sb.append("sum(se_sale_RC)");
					} 
				if (selectService.equals("Scratch Game Pwt")) {
						sb.append("sum(se_pwt_RC)");
					} 
				if (selectService.equals("Offline Affiliates")) {
					sb.append("sum(ola_RC)");
				}
				if (selectService.equals("Offline Affiliates Deposit")) {
						sb.append("sum(ola_deposit_RC)");
					} 
				if (selectService.equals("Offline Affiliates Withdrawal")) {
						sb.append("sum(ola_wd_RC)");
					}
				if (selectService.equals("Commercial Service")) {
					sb.append("sum(cs_RC)");
				}
				if (selectService.equals("Commercial Service Sale")) {
						sb.append("sum(cs_sale_RC)");
					}
				if (selectService.equals("Login")) {
					sb.append("sum(login_RC)");
				}
				if (selectService.equals("HeartBeat")) {
					sb.append("sum(heartbeat_RC)");
				}
				if (selectService.equals("Total")) {
					sb.append("sum(total_RC)");
				}
				sb.append(" ret_count from st_lms_city_master cm left join st_lms_location_wise_history ch on cm.city_code = ch.city_code  where date>=? and date<? and date is not null group by date,cm.city_code) mm group by date,city_code) gg group by date");
				PreparedStatement pstmt = con.prepareStatement(sb.toString());
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					tempList = new LinkedList<Integer>();
					for (int i = 1; i <= list.size(); i++) {
						tempList.add(rs.getInt("city_" + i + "_count"));
					}
					hMap.put(df.format(rs.getDate("date")), tempList);
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
		return hMap;
	}
}
