package com.skilrock.lms.coreEngine.roleMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ServiceDataBean;
import com.skilrock.lms.beans.ServiceInterfaceBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class AgentServiceManagementHelper {
	static Log logger = LogFactory.getLog(AgentServiceManagementHelper.class);

	private static StringBuffer tierIDbuffer;

	public static Map<String, String> getChildOrg(int parentOrgId)
			throws LMSException {
		 
		Connection con = null;
		Statement stmt = null;
		Map<String, String> childOrgList = new TreeMap<String, String>();

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			String query = "select organization_id,name from st_lms_organization_master where parent_id="
					+ parentOrgId;
			logger.debug("Query for Child Org :: " + query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				childOrgList.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}
		return childOrgList;
	}

	public static List<ServiceInterfaceBean> getInterfaceList(int serviceId,
			String tierLevel, int userID) throws LMSException {
		ServiceInterfaceBean serviceInterfaceBean = null;
		 
		Connection con = null;
		Statement stmt = null;
		String subQery = "";
		List<ServiceInterfaceBean> interfaceList = new ArrayList<ServiceInterfaceBean>();
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			String query = "select tm.tier_code, sdm.channel ,sdm.interface,sdm.status,sdm.service_delivery_master_id from st_lms_tier_master tm,st_lms_service_delivery_master sdm where (tm.tier_id = sdm.tier_id) and tm.tier_id in "
					+ tierIDbuffer + " and sdm.service_id =" + serviceId;
			logger.debug("***channel interface qyery" + query);
			if (!tierLevel.equalsIgnoreCase("All")) {
				subQery = " and tm.tier_code='" + tierLevel
						+ "' order by sdm.service_delivery_master_id";
			}

			query = query + subQery;
			logger.debug("query*******" + query);
			ResultSet rsTierInter = stmt.executeQuery(query);

			while (rsTierInter.next()) {
				serviceInterfaceBean = new ServiceInterfaceBean();
				serviceInterfaceBean.setTier_id(rsTierInter
						.getString("tier_code"));
				serviceInterfaceBean.setChannel(rsTierInter
						.getString("channel"));
				serviceInterfaceBean.setTier_interface(rsTierInter
						.getString("interface"));
				serviceInterfaceBean.setStatus(rsTierInter.getString("status"));
				interfaceList.add(serviceInterfaceBean);
				logger.debug("interfaceList********" + interfaceList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Exception: " + e);
			throw new LMSException("sql exception", e);
		}
		return interfaceList;
	}

	public static Map<String, String> getParentOrg(String tierLevel,
			String userType, int orgId) throws LMSException {
		 
		Connection con = null;
		Statement stmt = null;
		Map<String, String> parentOrgList = new TreeMap<String, String>();
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();

			String selectOrgType = null;
			String queryOrgType = "select tm1.tier_code as parent_tier_code from st_lms_tier_master tm1,st_lms_tier_master tm2 where tm1.tier_id=tm2.parent_tier_id and tm2.tier_code='"
					+ tierLevel + "'";
			ResultSet rsOrgType = stmt.executeQuery(queryOrgType);
			if (rsOrgType.next()) {
				selectOrgType = rsOrgType.getString(1);
			}

			String query = "select organization_id,name from st_lms_organization_master where organization_type='"
					+ selectOrgType + "'";
			if (userType.equalsIgnoreCase(selectOrgType)) {
				query = query + " and organization_id=" + orgId;
			}
			logger.debug("Query for Parent Org :: " + query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				// parentOrgList.add(rs.getString(1));
				parentOrgList.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}
		return parentOrgList;

	}

	public static ServiceDataBean getServiceData(int tierID)
			throws LMSException {

		Map<String, String> serviceDataMap = new HashMap<String, String>();
		List<String> tierList = new ArrayList<String>();
		ServiceDataBean serviceDataBean = new ServiceDataBean();
		 
		Connection con = null;
		Statement stmt = null;
		tierIDbuffer = new StringBuffer("( 0");

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();

			ResultSet rsSer = stmt
					.executeQuery("select service_name,service_id from st_lms_service_master where status='ACTIVE'");
			while (rsSer.next()) {
				serviceDataMap.put(rsSer.getString("service_name"), rsSer
						.getString("service_id"));
			}

			serviceDataBean.setServiceDataMap(serviceDataMap);

			String tiercodequery = "select tm1.tier_id, tm1.tier_code,tm1.parent_tier_id from st_lms_tier_master tm1,st_lms_tier_master tm2 where tm1.parent_tier_id = tm2.tier_id and tm1.tier_status = 'ACTIVE' and( tm2.tier_id="
					+ tierID
					+ " or (tm1.parent_tier_id in(select tm2.tier_id from st_lms_tier_master tm1 inner join st_lms_tier_master tm2 on tm1.parent_tier_id = tm2.tier_id where tm2.parent_tier_id>="
					+ tierID + ")))";
			logger.debug("tierquery" + tiercodequery);
			ResultSet rsTier = stmt.executeQuery(tiercodequery);

			tierList.add("All");

			while (rsTier.next()) {
				tierList.add(rsTier.getString("tier_code"));
				tierIDbuffer.append("," + rsTier.getInt("tier_id"));
			}
			serviceDataBean.setTierList(tierList);

			tierIDbuffer.append(")");
			logger.debug("tieridbuffer" + tierIDbuffer);

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}

		return serviceDataBean;
	}

	public static void updateServiceNew(int serviceId, String tierLevel,
			String interfaceStatus[], String interfaceStatusPrev[],
			String tierLevelValue, int[] selectedOrg, int parentOrgId)
			throws LMSException {

		 
		Connection con = null;
		Statement stmt = null;

		boolean activeBool = false;
		boolean inActiveBool = false;
		StringBuffer activebuffer = new StringBuffer("( 0");
		StringBuffer inactivebuffer = new StringBuffer("( 0");
		StringBuffer tierIdbuffer = new StringBuffer("(0");
		StringBuffer roleIdactivebuffer = new StringBuffer("(0");

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();

			con.setAutoCommit(false);

			String query = "select sdm.service_delivery_master_id,sdm.tier_id from st_lms_tier_master tm,st_lms_service_delivery_master sdm where (tm.tier_id = sdm.tier_id) and tm.tier_id in "
					+ tierIDbuffer + " and sdm.service_id =" + serviceId;
			if (!"All".equalsIgnoreCase(tierLevel)) {
				query = query + " and  tm.tier_code = '" + tierLevel
						+ "' order by sdm.service_delivery_master_id";
			}

			logger.debug("Query For Service Mapping id :: " + query);
			ResultSet r1 = stmt.executeQuery(query);
			int i = 0;
			while (r1.next()) {
				if ("ACTIVE".equals(interfaceStatus[i])) {
					activeBool = true;
					activebuffer.append("," + r1.getInt(1));
					tierIdbuffer.append("," + r1.getInt(2));
				} else if (interfaceStatus[i].equals("INACTIVE")) {
					inActiveBool = true;
					inactivebuffer.append("," + r1.getInt(1));
					tierIdbuffer.append("," + r1.getInt(2));
				}
				i++;
			}
			activebuffer.append(")");
			inactivebuffer.append(")");
			tierIdbuffer.append(")");

			ResultSet rsroleid = stmt
					.executeQuery("select role_id from st_lms_role_master where tier_id in "
							+ tierIdbuffer + " and is_master='y' ");
			while (rsroleid.next()) {
				roleIdactivebuffer.append("," + rsroleid.getInt(1));
			}
			roleIdactivebuffer.append(")");
			logger.debug("RoleId Array :: " + roleIdactivebuffer);

			String srmActiveUpdateQuery = "update st_lms_service_role_mapping set status ='ACTIVE' where id in "
					+ activebuffer + " and role_id in " + roleIdactivebuffer;
			String srmInActiveUpdateQuery = "update st_lms_service_role_mapping set status ='INACTIVE' where id in "
					+ inactivebuffer + " and role_id in " + roleIdactivebuffer;

			StringBuffer selectedOrgIDBuffer = new StringBuffer("( 0");
			if ("Specific".equalsIgnoreCase(tierLevelValue)) {
				logger.debug("********IN SPECIFIC*****");
				for (int element : selectedOrg) {
					selectedOrgIDBuffer.append("," + element);
				}
			} else if ("All".equalsIgnoreCase(tierLevel)
					|| "All".equalsIgnoreCase(tierLevelValue)) {
				logger.debug("********IN All*****");
				String queryAllOrgId = "select organization_id from st_lms_organization_master where parent_id="
						+ parentOrgId;
				ResultSet rs = stmt.executeQuery(queryAllOrgId);

				while (rs.next()) {
					selectedOrgIDBuffer.append("," + rs.getString(1));
				}

			}

			selectedOrgIDBuffer.append(")");

			srmActiveUpdateQuery = srmActiveUpdateQuery
					+ " and organization_id in " + selectedOrgIDBuffer;
			srmInActiveUpdateQuery = srmInActiveUpdateQuery
					+ " and organization_id in " + selectedOrgIDBuffer;
			System.out
					.println("Update Query for Service Role Mapping for Active:: "
							+ srmActiveUpdateQuery);
			System.out
					.println("Update Query for Service Role Mapping for InActive:: "
							+ srmInActiveUpdateQuery);

			if (activeBool) {
				stmt.executeUpdate(srmActiveUpdateQuery);
			}
			if (inActiveBool) {
				stmt.executeUpdate(srmInActiveUpdateQuery);
			}

			con.commit();

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}
	}
}
