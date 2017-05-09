package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.skilrock.lms.beans.AvailableServiceBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This class is used to fetch active country country list from the database.
 * 
 * @author SkilRockTechnologies
 * 
 */
public class CountryOrgHelper {

	 
	private HttpServletRequest request;

	/**
	 * Thismethod used to fetch active country country list from the database.
	 * 
	 * @return List
	 * @throws LMSException
	 */
	public List getAvlSerInterface(String tierCode) {

		List serviceInterfaceList = new ArrayList();
		Statement stmt = null;
		Connection con = null;
		con = DBConnect.getConnection();

		AvailableServiceBean serviceBean = null;

		ResultSet rs = null;

		String fetchSerIntMap = "select sdm.service_delivery_master_id,sdm.service_id,sm.service_name,sdm.channel,sm.status as serviceStatus,sdm.interface,"
				+ "sdm.status as interfaceStatus,sdm.priv_rep_table from st_lms_service_delivery_master sdm,st_lms_service_master sm, "
				+ "st_lms_tier_master tm where tm.tier_code = '"
				+ tierCode
				+ "' and tm.tier_status = 'ACTIVE' "
				+ "and tm.tier_id = sdm.tier_id  and sm.service_id = sdm.service_id and sm.status='ACTIVE'";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(fetchSerIntMap);

			while (rs.next()) {
				serviceBean = new AvailableServiceBean();

				serviceBean.setMappingId(rs
						.getInt("service_delivery_master_id"));
				serviceBean.setServiceId(rs.getInt("service_id"));
				serviceBean.setServiceName(rs.getString("service_name"));
				serviceBean.setChannel(rs.getString("channel"));
				serviceBean.setInterfaceType(rs.getString("interface"));
				serviceBean.setStatusInterface(rs.getString("interfaceStatus"));
				serviceBean.setStatusService(rs.getString("serviceStatus"));
				serviceBean.setPrivRepTable(rs.getString("priv_rep_table"));
				serviceInterfaceList.add(serviceBean);

			}
			DBConnect.closeCon(con);
			return serviceInterfaceList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	public List getCountry() throws LMSException {

		List countryList = new ArrayList();

		Statement stmt1 = null;
		Connection con = null;
		con = DBConnect.getConnection();

		ResultSet rs = null;

		try {
			stmt1 = con.createStatement();
			String countryNames = QueryManager.getST3Country();
			rs = stmt1.executeQuery(countryNames);
			// rs = stmt1.executeQuery("select * from st_lms_country_master");
			while (rs.next()) {
				String country = rs.getString(TableConstants.SOM_ORG_NAME);

				countryList.add(country);

			}

			return countryList;

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRoleMasterName(String tierCode) {
		Statement stmt = null;
		Connection con = null;
		con = DBConnect.getConnection();

		ResultSet rs = null;

		String fetchRoleMasterName = "select role_name from st_lms_role_master as a,st_lms_tier_master as b where a.tier_id=b.tier_id and tier_code='"
				+ tierCode + "'";
		String roleMasterName = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(fetchRoleMasterName);

			while (rs.next()) {
				roleMasterName = rs.getString("role_name");
			}
			DBConnect.closeCon(con);
			return roleMasterName;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}