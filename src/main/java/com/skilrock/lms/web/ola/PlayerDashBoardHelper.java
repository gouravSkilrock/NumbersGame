package com.skilrock.lms.web.ola;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.common.db.DBConnect;

public class PlayerDashBoardHelper {

	public String getPlrList(String retName) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String query = null;
		StringBuilder plrString=new StringBuilder(""); 
		String responseString="";
		try {
			query = "select player_id from st_ola_affiliate_plr_mapping where ref_user_id='"
					+ retName+"'";
			con = DBConnect.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) { 
				plrString.append(rs.getString("player_id")).append(",");
			}
			if(plrString.toString()!=""){
				responseString=plrString.toString().substring(0,plrString.toString().lastIndexOf(','));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}
}
