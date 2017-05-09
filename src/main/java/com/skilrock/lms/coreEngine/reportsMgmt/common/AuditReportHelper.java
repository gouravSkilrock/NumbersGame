package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AuditTrailBean;
import com.skilrock.lms.beans.AuditTrailRequestBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;

public class AuditReportHelper {
	private static final Log logger = LogFactory
			.getLog(AuditReportHelper.class);

	public Map<Integer, String> fetchOrgMap() throws LMSException {
		Map<Integer, String> orgNameMap = new TreeMap<Integer, String>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("SELECT user_id, user_name FROM st_lms_user_master WHERE organization_type = 'BO' AND STATUS = 'ACTIVE';");
			while (rs.next()) {
				orgNameMap.put(rs.getInt("user_id"), rs.getString("user_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return orgNameMap;
	}

	public String fetchOrgAddress(int orgId) throws LMSException {
		String orgAddress = null;
		Connection con = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		logger.info("***** Inside fetchOrgAddress Function");
		try {
			con = DBConnect.getConnection();
			pStmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pStmt.setInt(1, orgId);
			rs = pStmt.executeQuery();
			logger.info("Fetching Org Address Query " + pStmt);
			while (rs.next()) {
				orgAddress = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
			logger.info("Ord Address is " + orgAddress);
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
			DBConnect.closePstmt(pStmt);
			DBConnect.closeRs(rs);
		}
		return orgAddress;
	}

	public List<AuditTrailBean> fetchAuditTrailReport(int userId, String startDate, String endDate) throws LMSException {
		List<AuditTrailBean> auditTrailBeans;
		AuditTrailBean auditTrailBean = null;

		AuditTrailRequestBean auditTrailRequestBean = new AuditTrailRequestBean();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		logger.info("***** Inside fetchAuditTrailReport Function");

		String query = "SELECT group_name_en, um.user_name login_name, audit.access_ip, CONCAT(ucd.first_name, ' ', ucd.last_name) NAME, audit.request_time FROM st_lms_priviledge_rep priv INNER JOIN st_lms_audit_user_access_history audit ON priv.action_mapping = audit.action_name INNER JOIN st_lms_user_master um ON um.user_id = audit.user_id INNER JOIN st_lms_user_contact_details ucd ON ucd.user_id = audit.user_id WHERE service_type in ('HOME', 'MGMT') AND audit.user_id = "
				+ userId
				+ " AND audit.is_audit_trail_display = 'Y' AND audit.request_time >= '"
				+ startDate
				+ "' AND audit.request_time <= '"
				+ endDate
				+ "' UNION ALL SELECT group_name_en, um.user_name login_name, audit.access_ip, CONCAT(ucd.first_name, ' ', ucd.last_name) NAME, audit.request_time FROM st_dg_priviledge_rep priv INNER JOIN st_lms_audit_user_access_history audit ON priv.action_mapping = audit.action_name INNER JOIN st_lms_user_master um ON um.user_id = audit.user_id INNER JOIN st_lms_user_contact_details ucd ON ucd.user_id = audit.user_id WHERE service_type = 'DG' AND audit.user_id = "
				+ userId
				+ " AND audit.is_audit_trail_display = 'Y' AND audit.request_time >= '"
				+ startDate
				+ "' AND audit.request_time <= '"
				+ endDate
				+ "'"
				+ " UNION ALL SELECT CASE audit.action_name WHEN 'LoginSuccess' THEN 'Login' WHEN 'Logout' THEN 'Logout' END AS priv_disp_name, um.user_name login_name, audit.access_ip, CONCAT(ucd.first_name, ' ', ucd.last_name) NAME, audit.request_time FROM st_lms_audit_user_access_history audit LEFT JOIN st_lms_priviledge_rep priv ON priv.priv_id = audit.priv_id INNER JOIN st_lms_user_master um ON um.user_id = audit.user_id INNER JOIN st_lms_user_contact_details ucd ON ucd.user_id = audit.user_id WHERE service_type IN ('HOME', 'MGMT') AND audit.action_name IN ('LoginSuccess', 'Logout') AND audit.user_id = "
				+ userId
				+ " AND audit.request_time >= '"
				+ startDate + "' AND audit.request_time <= '" + endDate + "'";
		auditTrailBeans = new ArrayList<AuditTrailBean>();
		try {
			auditTrailRequestBean.setMerchantId(2);
			auditTrailRequestBean.setUserId(userId);
			auditTrailRequestBean.setStartTime(startDate);
			auditTrailRequestBean.setEndTime(endDate);
			NotifySLE notifySLE = new NotifySLE(SLE.Activity.GET_AUDIT_TRAIL_DATA, auditTrailRequestBean);
			auditTrailRequestBean = (AuditTrailRequestBean) notifySLE.asyncCall(notifySLE);
			auditTrailBeans.addAll(auditTrailRequestBean.getAuditTrailBeans());

			con = DBConnect.getConnection();
			stmt = con.createStatement();
			logger.info("Query Fetching Data is " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				auditTrailBean = new AuditTrailBean();
				auditTrailBean.setActivity(rs.getString("group_name_en"));
				auditTrailBean.setLoginName(rs.getString("login_name"));
				auditTrailBean.setAccessIp(rs.getString("access_ip"));
				auditTrailBean.setUserName(rs.getString("name"));
				auditTrailBean.setAccessTime(df.format(df.parse(rs.getString("request_time"))));
				auditTrailBeans.add(auditTrailBean);
			}

			Collections.sort(auditTrailBeans);
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return auditTrailBeans;
	}
}
