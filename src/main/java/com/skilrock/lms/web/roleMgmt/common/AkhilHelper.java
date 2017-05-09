package com.skilrock.lms.web.roleMgmt.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;

public class AkhilHelper {
	static Log logger = LogFactory.getLog(AkhilHelper.class);

	public static void main(String p[]) {

		DBConnect dbConnect = null;
		Connection connection = null;

		 
		connection = DBConnect.getConnection();

		try {

			Statement stmt = connection.createStatement();

			String query = "select tm2.tier_code,tm1.tier_code as parent_tier_code from st_lms_tier_master tm1,st_lms_tier_master tm2 where tm1.tier_id=tm2.parent_tier_id";

			ResultSet rs = stmt.executeQuery(query);
			StringBuilder ss = new StringBuilder();
			StringBuilder tt = new StringBuilder();
			while (rs.next()) {
				ss.append(rs.getString("tier_code") + "-");
				ss.append(rs.getString("parent_tier_code") + ":");
			}

			int d = ss.length();
			ss.deleteCharAt(d - 1);
			logger.debug(ss);

			ss.append("=");
			query = "select organization_id,organization_type,name, parent_id  from st_lms_organization_master";
			ResultSet rs1 = stmt.executeQuery(query);
			while (rs1.next()) {
				ss.append(rs1.getInt("organization_id") + "-");
				ss.append(rs1.getString("organization_type") + "-");
				ss.append(rs1.getString("name") + ":");
				tt.append(rs1.getInt("organization_id") + "-");
				tt.append(rs1.getString("name") + "-");
				tt.append(rs1.getInt("parent_id") + ":");

			}
			d = ss.length();
			ss.deleteCharAt(d - 1);
			ss.append("=");
			ss.append(tt);
			d = ss.length();
			ss.deleteCharAt(d - 1);
			logger.debug(ss);

		} catch (Exception e) {
			logger.debug(e);
			e.printStackTrace();
		} finally {
			try {

				logger.debug(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
