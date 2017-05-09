package com.skilrock.lms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;

public class TruncateTable {

	static Log logger = LogFactory.getLog(TruncateTable.class);

	public static void main(String[] args) {
		TruncateTable tt = new TruncateTable();
		try {
			tt.truncateTable("ken_dep_2", "root", "192.168.123.113", "st");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}

	List<String> notTruncTblList = null;

	public TruncateTable() {
		notTruncTblList = new ArrayList<String>();
		notTruncTblList.add("priviledge_rep");
		notTruncTblList.add("st_lms_report_email_priv_master");
		notTruncTblList.add("st_lms_report_email_priviledge_rep");
		notTruncTblList.add("role_privl_mapping");
		notTruncTblList.add("st_lms_country_master");
		notTruncTblList.add("st_lms_state_master");
		notTruncTblList.add("st_menu_master");
		notTruncTblList.add("st_lms_organization_master");
		notTruncTblList.add("st_lms_oranization_limits");
		notTruncTblList.add("st_lms_role_master");
		notTruncTblList.add("st_lms_state_master");
		notTruncTblList.add("st_lms_user_contact_details");
		notTruncTblList.add("st_lms_user_master");
		// notTruncTblList.add("st_lms_user_contact_details");
		notTruncTblList.add("st_lms_user_priv_mapping");
		notTruncTblList.add("lms_terminal_mapping");
		notTruncTblList.add("st_dg_game_master");
		notTruncTblList.add("st_rg_criteria_limit");
	}

	public void truncateTable(String db, String userName, String hostAdd,
			String passWrd) throws SQLException {

		String fetchTables = "show table status from " + db
				+ " where engine is not NULL";

		Connection con = DBConnect.getConnection();

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(fetchTables);
		Statement stmt2 = con.createStatement();

		while (rs.next()) {
			String tabName = rs.getString("Name");
			if (notTruncTblList.contains(tabName)) {
				System.out.println(tabName + "-- Do Nothing");
			} else {
				stmt2.executeUpdate("truncate table " + tabName);
				System.out.println(tabName + "*** Truncated");
			}
		}

		// return null;
	}
}
