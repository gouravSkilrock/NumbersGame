package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This class provides method to check organinization is already existing
 * 
 * @author Skilrock Technologies
 * 
 */
public class OrganizationHelper {

	public Map<Integer, String> getMailingReportTitle(String userType) {
		Map<Integer, String> mailReportTitle = new TreeMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			String mailReportTitleQuery = "select email_pid, priv_title from st_lms_report_email_priviledge_rep where priv_owner = '"
					+ userType + "' and status ='ACTIVE'";
			ResultSet rs = stmt.executeQuery(mailReportTitleQuery);
			while (rs.next()) {
				String privTitle = rs.getString("priv_title");
				int emailPid = rs.getInt("email_pid");
				mailReportTitle.put(emailPid, privTitle);
			}
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return mailReportTitle;
	}

	/**
	 * This method is used to check organization exists or not
	 * 
	 * @param orgname
	 *            name of organization entered by the user
	 * @return String
	 * @throws LMSException
	 */
	public String verifyOrgName(String orgName) throws LMSException {
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt1 = con.createStatement();
			String organizationName = QueryManager.getST3OrgName();
			ResultSet res = stmt1.executeQuery(organizationName);
			while (res.next()) {
				String org = res.getString("name");
				if (org.equalsIgnoreCase(orgName)) {
					return "ERROR";
				}
			}

			return "SUCCESS";

		} catch (SQLException se) {
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

	}

}