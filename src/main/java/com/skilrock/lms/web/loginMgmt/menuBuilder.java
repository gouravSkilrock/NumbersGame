package com.skilrock.lms.web.loginMgmt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;

public class menuBuilder {
	static Log logger = LogFactory.getLog(menuBuilder.class);

	public static void main(String[] args) throws SQLException {
		menuBuilder mB = new menuBuilder();
		String resultop = mB.createMenu();
		logger.debug(resultop);
	}

	public String createMenu() throws SQLException {
		String menuTable[] = { "st_lms_menu_master", "st_se_menu_master",
				"st_dg_menu_master", "st_iw_menu_master" };
		String privTable[] = { "st_lms_priviledge_rep", "st_se_priviledge_rep",
				"st_dg_priviledge_rep", "st_iw_menu_master" };
		String fetchMenuData = null;
		String updateMenu = null;
		 
		Connection con = DBConnect.getConnection();
		Statement stmt = con.createStatement();
		for (int i = 0; i < menuTable.length; i++) {
			fetchMenuData = "insert into "
					+ menuTable[i]
					+ " (action_id,menu_name,menu_disp_name,menu_disp_name_en,menu_disp_name_fr,parent_menu_id,item_order) select action_id,group_name ,group_name,group_name_en,group_name_fr,0,0 from "
					+ privTable[i]
					+ " where is_start='Y' and status='ACTIVE' and action_id not in (select action_id from "
					+ menuTable[i] + ")";
			logger.debug(fetchMenuData);
			stmt.execute(fetchMenuData);
			updateMenu = "delete from " + menuTable[i]
					+ " where action_id not in (select action_id from "
					+ privTable[i] + " where is_start='Y' and status='ACTIVE')";
			stmt.execute(updateMenu);
		}

		return "SUCCESS";
	}
}
