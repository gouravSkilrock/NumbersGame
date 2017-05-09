package com.skilrock.lms.api.lmsWrapper.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperUserDetailsBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.roleMgmt.common.NewSubUserHelper;

public class LmsWrapperUserMgmtHelper {

	public String RegisterNewSubUser(LmsWrapperUserDetailsBean wrapperUserDetailsBean){
		String status="FAILED";
		UserDetailsBean usrdetBean = new UserDetailsBean();
		usrdetBean.setFirstName(wrapperUserDetailsBean.getFirstName());
		usrdetBean.setLastName(wrapperUserDetailsBean.getLastName());
		usrdetBean.setEmailId(wrapperUserDetailsBean.getEmailId());
		usrdetBean.setPhoneNbr(wrapperUserDetailsBean.getPhoneNbr());
		usrdetBean.setUserName(wrapperUserDetailsBean.getUserName());
		usrdetBean.setStatus(wrapperUserDetailsBean.getStatus());
		usrdetBean.setSecQues(wrapperUserDetailsBean.getSecQues());
		usrdetBean.setSecAns(wrapperUserDetailsBean.getSecAns());
		usrdetBean.setOrgId(wrapperUserDetailsBean.getOrgId());
		usrdetBean.setRoleId(wrapperUserDetailsBean.getRoleId());
		usrdetBean.setOrgType(wrapperUserDetailsBean.getOrgType());
		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		Connection con=DBConnect.getConnection();
		try{
			status=subUserHelper.newUserReg(wrapperUserDetailsBean.getUserName().trim());
			if("ERROR".equalsIgnoreCase(status)){
				return "FAILED";
			}
		String getBoDetailQry="select role_id,organization_id,user_id from st_lms_user_master where user_name='bomaster'";
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(getBoDetailQry);
		if(rs.next()){
			usrdetBean.setOrgId(rs.getInt("organization_id"));
			usrdetBean.setRoleId(rs.getInt("role_id"));
			usrdetBean.setMailSend(false);
			status= subUserHelper.assignGroups(rs.getInt("organization_id"),
						wrapperUserDetailsBean.getGroupName(), rs.getInt("user_id"), rs.getInt("role_id"), usrdetBean,
						wrapperUserDetailsBean.getMappingId(), wrapperUserDetailsBean.getPrivCount());
		}
		
		   status=getUserIdFromUserName(wrapperUserDetailsBean.getUserName())+"";
		
		}catch (SQLException e) {
			e.printStackTrace();
			return status;
			// TODO: handle exception
		} catch (LMSException e) {
			
			e.printStackTrace();
			return status;
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				return status;
			}
		}
		System.out.println("Return Status:"+status);
		return status;
	}
	
	public static int getUserIdFromUserName(String userName){
		int userId = 0;
		
		Connection con=DBConnect.getConnection();
		try{
		
		String getBoDetailQry="select user_id from st_lms_user_master where user_name='"+userName+"'";
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(getBoDetailQry);
		if(rs.next()){
			userId=rs.getInt("user_id");
         }
				
		}catch (SQLException e) {
			e.printStackTrace();
			
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				
			}
		}
		
		return userId;
	}
}
