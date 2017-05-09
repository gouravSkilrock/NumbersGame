package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.RetActivityBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class RetActivityReportHelper {

	static Log logger = LogFactory.getLog(RetActivityReportHelper.class);

	public static Map<String, Map<String, String>> fetchSoldBookEntry(
			int retOrgId, String curRemaining) throws LMSException {

		Connection con = null;
		Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
		con = DBConnect.getConnection();
		Map<String, String> bookMap = null;
		ResultSet rs2 = null;
		if (curRemaining == null) {
			curRemaining = "";
		}
		String gameNameQry = "select game_name,game_id from st_se_game_master";
		String query = "select book_nbr,cur_rem_tickets from st_se_game_inv_status where current_owner_id="
				+ retOrgId
				+ " and cur_rem_tickets"
				+ curRemaining
				+ "=0 and game_id=? order by book_nbr";
		try {
			PreparedStatement pstmt = con.prepareStatement(gameNameQry);
			ResultSet rs = pstmt.executeQuery();
			PreparedStatement pstmt2 = con.prepareStatement(query);

			while (rs.next()) {
				pstmt2.setInt(1, rs.getInt("game_id"));
				rs2 = pstmt2.executeQuery();
				bookMap = new LinkedHashMap<String, String>();
				while (rs2.next()) {
					bookMap.put(rs2.getString("book_nbr"), rs2
							.getInt("cur_rem_tickets")
							+ "");
				}
				map.put(rs.getString("game_name"), bookMap);

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
		System.out.println(map);
		return map;
	}

	public Map<String, String> getServiceList() {
		Map<String, String> serviceNameMap = new TreeMap<String, String>();
		Connection con = null;
		PreparedStatement  pstmt = null;
		con = DBConnect.getConnection();
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("Select * from st_lms_service_master where service_code <>'MGMT' and status='ACTIVE' and service_code <>'SE'");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				serviceNameMap.put(rs.getString("service_code"), rs
						.getString("service_display_name"));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.debug("service Name map  ===== " + serviceNameMap);
		return serviceNameMap;
	}
	public static void main(String[] args) throws LMSException {
		RetActivityReportHelper.fetchSoldBookEntry(26, "!=");
	}

	public Map<String, RetActivityBean> fetchActivityTrx(int agentOrgId,
			boolean isOffline) throws LMSException {
		Connection con = null;
		Map<String, RetActivityBean> map = new LinkedHashMap<String, RetActivityBean>();
		con = DBConnect.getConnection();
		RetActivityBean tempBean = null;
		String trxType = null;
		String orgNameQry = null;
		String query = null;
		if (agentOrgId == -1) {
			if (isOffline) {
				orgNameQry = "select slom.name,parent.name as parentName,slom.organization_id,last_login_date,slom.organization_status,slom.city, rom.current_version, rom.device_type, rom.is_offline from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where parent_id=1) parent, st_lms_user_master slum, st_lms_ret_offline_master rom where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id and rom.organization_id = slum.organization_id order by parentName,name";
			} else {
				orgNameQry = "select slom.name,parent.name as parentName,slom.organization_id,last_login_date,slom.organization_status,slom.city from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where parent_id=1) parent, st_lms_user_master slum where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id   order by parentName,name";
			}
			query = "select retailer_org_id,max(transaction_date) trxDate,transaction_type from st_lms_retailer_transaction_master where transaction_type in ('DG_SALE','PWT','DG_PWT','DG_SALE_OFFLINE','DG_REFUND_CANCEL')  group by retailer_org_id,transaction_type order by retailer_org_id,transaction_type";

		} else {
			if (isOffline) {
				orgNameQry = "select slom.name,slom.parent_id,parent.name as parentName,slom.organization_id,last_login_date,slom.organization_status,slom.city, rom.current_version, rom.device_type, rom.is_offline from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where organization_id="
						+ +agentOrgId
						+ ") parent, st_lms_user_master slum, st_lms_ret_offline_master rom where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id and slom.organization_id = rom.organization_id  order by parentName,name";
			} else {
				orgNameQry = "select slom.name,slom.parent_id,parent.name as parentName,slom.organization_id,last_login_date,slom.organization_status,slom.city from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where organization_id="
						+ agentOrgId
						+ ") parent, st_lms_user_master slum where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id  order by parentName,name";
			}
//			query = "select retailer_org_id,max(transaction_date) trxDate,transaction_type from st_lms_retailer_transaction_master where transaction_type in ('DG_SALE','PWT','DG_PWT','DG_SALE_OFFLINE','DG_REFUND_CANCEL') and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id ="
//					+ agentOrgId
//					+ ") group by retailer_org_id,transaction_type order by retailer_org_id,transaction_type";
// In query optimized by inner join - 13 march 11
			query = "select retailer_org_id,max(transaction_date) trxDate,transaction_type from st_lms_retailer_transaction_master rm inner join st_lms_organization_master om on retailer_org_id=organization_id where transaction_type in ('DG_SALE','PWT','DG_PWT','DG_SALE_OFFLINE','DG_REFUND_CANCEL') and parent_id ="
					+ agentOrgId
					+ " group by retailer_org_id,transaction_type order by retailer_org_id,transaction_type";
		}
		try {
			PreparedStatement pstmt = con.prepareStatement(orgNameQry);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tempBean = new RetActivityBean();
				tempBean.setRetOrgId(rs.getInt("organization_id"));
				if (rs.getTimestamp("last_login_date") != null) {
					tempBean.setLogin(rs.getTimestamp("last_login_date")
							.getTime());
				}
				tempBean.setRetName(rs.getString("name"));
				tempBean.setRetParentName(rs.getString("parentName"));
				tempBean.setOrgStatus(rs.getString("organization_status"));
				tempBean.setLocation(rs.getString("city"));
				if (isOffline) {
					tempBean.setCurrentVersion(rs.getString("current_version")
							+ "version");
					tempBean.setOfflineStatus(rs.getString("is_offline"));
					if (rs.getString("device_type").equals("-1")) {
						tempBean.setTerminalId("N.A.");
					} else {
						tempBean.setTerminalId(rs.getString("device_type"));
					}
				}
				map.put(rs.getInt("organization_id") + "", tempBean);
			}
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tempBean = map.get(rs.getInt("retailer_org_id") + "");
				trxType = rs.getString("transaction_type");
				tempBean.setRetOrgId(rs.getInt("retailer_org_id"));
				fillActivityBean(trxType, tempBean, rs.getTimestamp("trxDate")
						.getTime());
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
		System.out.println(map);
		return map;
	}

	public List<String> fetchActRepHistoryForDrawGame(DateBeans dBean) throws LMSException {
		Connection con = null;
		List<String> list = new ArrayList<String>();
		con = DBConnect.getConnection();
		String query = "";
		query = "select date, live_retailers, noSale_retailers, inactive_retailers, terminated_retailers, total_sales, total_pwt, total_tkt_count, total_pwt_count, avg_sale_per_ret  from st_lms_ret_activity_history where date>=? and date<?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setDate(1, dBean.getFirstdate());
			pstmt.setDate(2, dBean.getLastdate());
			ResultSet rs = pstmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				list.add(i + "," + rs.getDate("date") + ","
						+ rs.getString("live_retailers") + ","
						+ rs.getString("noSale_retailers") + ","
						+ rs.getString("inactive_retailers") + ","
						+ rs.getString("terminated_retailers") + ","
						+ rs.getDouble("total_sales") + ","
						+ rs.getDouble("total_pwt") + ","
						+ rs.getInt("total_tkt_count") + ","
						+ rs.getInt("total_pwt_count") + ","
						+ rs.getDouble("avg_sale_per_ret"));
				i++;
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
		logger.debug(list);
		return list;
	}
	
	public List<String> fetchActRepHistoryForSportsLottery(DateBeans dBean) throws LMSException {
		Connection con = null;
		List<String> list = new ArrayList<String>();
		con = DBConnect.getConnection();
		String query = "";
		query = "select date, live_retailers, noSale_retailers, inactive_retailers, terminated_retailers, total_sales, total_pwt, total_tkt_count, total_pwt_count, avg_sale_per_ret  from st_sle_ret_activity_history where date>=? and date<?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setDate(1, dBean.getFirstdate());
			pstmt.setDate(2, dBean.getLastdate());
			ResultSet rs = pstmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				list.add(i + "," + rs.getDate("date") + ","
						+ rs.getString("live_retailers") + ","
						+ rs.getString("noSale_retailers") + ","
						+ rs.getString("inactive_retailers") + ","
						+ rs.getString("terminated_retailers") + ","
						+ rs.getDouble("total_sales") + ","
						+ rs.getDouble("total_pwt") + ","
						+ rs.getInt("total_tkt_count") + ","
						+ rs.getInt("total_pwt_count") + ","
						+ rs.getDouble("avg_sale_per_ret"));
				i++;
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
		logger.debug(list);
		return list;
	}
	public List<String> fetchActRepHistoryForCS(DateBeans dBean) throws LMSException {
		Connection con = null;
		List<String> list = new ArrayList<String>();
		con = DBConnect.getConnection();
		String query = "";
		query = "select date, live_retailers, noSale_retailers, inactive_retailers, terminated_retailers, total_sales,avg_sale_per_ret  from st_cs_ret_activity_history where date>=? and date<?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setDate(1, dBean.getFirstdate());
			pstmt.setDate(2, dBean.getLastdate());
			ResultSet rs = pstmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				list.add(i + "," + rs.getDate("date") + ","
						+ rs.getString("live_retailers") + ","
						+ rs.getString("noSale_retailers") + ","
						+ rs.getString("inactive_retailers") + ","
						+ rs.getString("terminated_retailers") + ","
						+ rs.getDouble("total_sales") + ","
						+ rs.getDouble("avg_sale_per_ret"));
				i++;
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
		logger.debug(list);
		return list;
	}

	private void fillActivityBean(String trxType, RetActivityBean tempBean,
			long time) {
		if (trxType.equals("DG_SALE")) {
			tempBean.setDrawSale(time);
		} else if (trxType.equals("DG_PWT")) {
			tempBean.setDrawPwt(time);
		} else if (trxType.equals("DG_SALE_OFFLINE")) {
			tempBean.setDrawSale(time);
		} else if (trxType.equals("DG_REFUND_CANCEL")) {
			tempBean.setDrawCancel(time);
		} else if (trxType.equals("PWT")) {
			tempBean.setScratchPwt(time);
		}
	}

	public Object fetchActRepHistoryForInstantWin(DateBeans dBean) throws LMSException {
		Connection con = null;
		List<String> list = new ArrayList<String>();
		con = DBConnect.getConnection();
		String query = "";
		query = "select date, live_retailers, noSale_retailers, inactive_retailers, terminated_retailers, total_sales, total_pwt, total_tkt_count, total_pwt_count, avg_sale_per_ret  from st_iw_ret_activity_history where date>=? and date<?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setDate(1, dBean.getFirstdate());
			pstmt.setDate(2, dBean.getLastdate());
			ResultSet rs = pstmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				list.add(i + "," + rs.getDate("date") + ","
						+ rs.getString("live_retailers") + ","
						+ rs.getString("noSale_retailers") + ","
						+ rs.getString("inactive_retailers") + ","
						+ rs.getString("terminated_retailers") + ","
						+ rs.getDouble("total_sales") + ","
						+ rs.getDouble("total_pwt") + ","
						+ rs.getInt("total_tkt_count") + ","
						+ rs.getInt("total_pwt_count") + ","
						+ rs.getDouble("avg_sale_per_ret"));
				i++;
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
		logger.debug(list);
		return list;
	}

}
