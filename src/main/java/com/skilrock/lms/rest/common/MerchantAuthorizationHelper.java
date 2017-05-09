package com.skilrock.lms.rest.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class MerchantAuthorizationHelper {

	static int merchantAuthorization(String userName,String password) throws LMSException{
		Connection con=null;
		Statement stmt=null;
		int merchantId=0;
		try{
			con=DBConnect.getConnection();
			stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select SQL_CACHE merchant_id from ge_merchant_master where vendor_user_name='"+userName+"' and vendor_password='"+password+"' and status='ACTIVE'");
			
			if(rs.next()){
				merchantId=rs.getInt("merchant_id");
			}else{
				throw new LMSException();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
		return merchantId;
	}
}
