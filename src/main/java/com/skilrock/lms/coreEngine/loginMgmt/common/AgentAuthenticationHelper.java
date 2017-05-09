package com.skilrock.lms.coreEngine.loginMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.MD5Encoder;

public class AgentAuthenticationHelper {
	Log logger = LogFactory.getLog(AgentAuthenticationHelper.class);
	
	public String authenticateAgentAndTerminalId(String userName, String password, String deviceType, String brandName, String modelName, String terminalId){
		if(userName != null && password != null){
			Connection con = DBConnect.getConnection();
			try{
				PreparedStatement pstmt = con.prepareStatement("SELECT password FROM st_lms_user_master WHERE organization_type='AGENT' AND user_name=? and password=?");
				pstmt.setString(1, userName);
				pstmt.setString(2, MD5Encoder.encode(password));
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()){
					pstmt = con.prepareStatement("SELECT current_owner_id FROM st_lms_inv_detail a inner join st_lms_inv_model_master b inner join st_lms_inv_brand_master c inner join st_lms_inv_master d on a.inv_model_id=b.model_id and b.brand_id=c.brand_id and c.inv_id=d.inv_id where serial_no like '%"+terminalId+"'  and model_name=? and brand_name=? and inv_name=? and current_owner_id in(select organization_id from st_lms_user_master where user_name = ? and isrolehead = 'Y' union all select organization_id from st_lms_organization_master where parent_id=(select organization_id from st_lms_user_master where user_name = ? and isrolehead = 'Y')) order by task_id  desc limit 1");
					pstmt.setString(1, modelName);
					pstmt.setString(2, brandName);
					pstmt.setString(3, deviceType);
					pstmt.setString(4, userName);
					pstmt.setString(5, userName);
					logger.debug("***pstmt***"+pstmt+"***");
					rs = pstmt.executeQuery();
					
					if(rs.next()){
						return "Agent:Y|Terminal:Y|";
					} else{
						return "Agent:Y|Terminal:N|";
					}
				} else{
					return "Agent:N|Terminal:N|";
				}
			} catch(Exception e){
				e.printStackTrace();
				logger.debug(e);
			} finally{
				DBConnect.closeCon(con);
			}
		}
		return "Agent:N|Terminal:N|";
	}
}
