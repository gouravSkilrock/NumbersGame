package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.OrgDO;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class AgentRegHelper {

	 

	List orgList = null;

	public List userReg() throws Exception {

		OrgDO odb = null;

		orgList = new ArrayList();

		Connection con = null;
		Statement stmt1 = null;

		ResultSet rs = null;

		con = DBConnect.getConnection();
		stmt1 = con.createStatement();

		String orgDetails = QueryManager.getST3OrgDetails()
				+ " where organization_type='AGENT'  ";
		rs = stmt1.executeQuery(orgDetails);
		// rs = stmt1.executeQuery("select
		// name,organization_type,organization_id from
		// st_lms_organization_master where organization_type='AGENT'");
		while (rs.next()) {
			odb = new OrgDO();
			odb.setName(rs.getString("name"));
			odb.setOrgId(rs.getInt("organization_id"));
			odb.setOrgType(rs.getString("organization_type"));
			orgList.add(odb);

		}

		// session.setAttribute("list",orgList );

		return orgList;
	}

}