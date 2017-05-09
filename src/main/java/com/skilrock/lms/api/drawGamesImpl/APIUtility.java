package com.skilrock.lms.api.drawGamesImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.MD5Encoder;

public class APIUtility {

	public static boolean isValidUser(String userName, String password,
			String ip) {		
		Connection connection = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbIp = null;
		String dbPass = null;
		String query = "select mer.merchant_ip,um.user_name,mer.password from st_lms_merchant_auth_master mer,st_lms_organization_master om ,st_lms_user_master um where um.user_name = '"
				+ userName
				+ "' and  om.tp_organization = 'YES' and mer.ref_agt_id = om.parent_id and um.organization_id = om.organization_id and mer.user_name = um.user_name";
		try {
			pstmt = connection.prepareStatement(query);
			System.out.println("query for validating API user:"+query);
			rs = pstmt.executeQuery();
			if(rs.next()){
				dbIp = rs.getString("merchant_ip");
				dbPass = rs.getString("password");
				if(dbIp.contains(ip) && MD5Encoder.encode(password).equals(dbPass)){
					return true;
				}else{
					System.out.println("IP and password dose not match with merchnt_auth_master...");
					return false;
				}
			}else{
				System.out.println("not a TP user...");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	public static boolean validateUser(UserInfoBean userBean,String ip,String password){
		if(userBean.getTpIp().contains(ip) && MD5Encoder.encode(password).equals(userBean.getTpTxnPassword()))
				return true;
		else
			return false;
	}
}
