package com.skilrock.lms.api.lmsWrapper.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;

public class WrapperUtility {

	public static Map<String,String> wrapperAuthenticateDataMap=null;
	
	public static String changeStatusFromRemovedToBo(List<String> serNoList,String modelId){
		Connection con=DBConnect.getConnection();
		String status="FAILED";
		String assignBoInvQry="update st_lms_inv_status set current_owner_type='BO'  where serial_no in ('"+serNoList.toString().substring(1, serNoList.toString().length()-1).replaceAll(",","','").replaceAll(" ","")+"') and inv_model_id='"+modelId+"';";

		try {
			Statement stmt=con.createStatement();
			stmt.executeUpdate(assignBoInvQry);
			System.out.println("Update BO qry"+assignBoInvQry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return status;
		}
		status="SUCCESS";
		return status;
	}
	
	public static String changeStatusFromBoToRemoved(List<String> serNoList,int modelId,Connection con){
		
		String status="FAILED";
		String assignRemovedInvQry="update st_lms_inv_status set current_owner_type='REMOVED'  where serial_no in ('"+serNoList.toString().substring(1, serNoList.toString().length()-1).replaceAll(",","','").replaceAll(" ","")+"') and inv_model_id='"+modelId+"';";

		try {
			Statement stmt=con.createStatement();
			stmt.executeUpdate(assignRemovedInvQry);
			System.out.println("Update BO qry"+assignRemovedInvQry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return status;
		}
		status="SUCCESS";
		return status;
	}
	
	public static List<String> getSerialNoFromAgentOrgId(String agentOrgId){
		List<String> serialNoList=new ArrayList<String>();
		Connection con=DBConnect.getConnection();
		
		try{
			String fetchTerminalQry="select serial_no from st_lms_inv_status where current_owner_id='"+agentOrgId+"'";
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(fetchTerminalQry);
			while(rs.next()){
				serialNoList.add(rs.getString("serial_no"));
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return serialNoList;
	}
	public static String getModelNameFromModelId(String modelId){
		Connection con=DBConnect.getConnection();
		String getModelName="select model_name from st_lms_inv_model_master where model_id= "+modelId;
		String modelName="";
		try {
			Statement stmt =con.createStatement();
			ResultSet rs=stmt.executeQuery(getModelName);
			if(rs.next()){
				modelName=rs.getString("model_name");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return modelName;
	}
	
	public static String getAgentNameFromAgentUserId(String agentUserId){
		String agentName="";
		Connection con=DBConnect.getConnection();
		String agentQry="select name from st_lms_organization_master om,st_lms_user_master um where user_id= "+agentUserId+" and um.organization_id = om.organization_id and um.isrolehead='Y'";
		System.out.println("get Agent Name From AgentId Qry::"+agentQry);
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(agentQry);
			while(rs.next()){
				agentName=rs.getString("name");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return agentName;
	}
	
	public static String getAgentOrgIdFromAgentUserId(String agentUserId){
		String agentOrgId="";
		Connection con=DBConnect.getConnection();
		String agentQry="select organization_id from st_lms_user_master um where user_id= "+agentUserId+" and isrolehead='Y'";
		
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(agentQry);
			while(rs.next()){
				agentOrgId=rs.getString("organization_id");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return agentOrgId;
	}
	
	public static int getAgentIdFromAgentName(String agentName){
		int agentOrgId=0;
		Connection con=DBConnect.getConnection();
		String agentQry="select organization_id from st_lms_organization_master where name='"+agentName+"';";
		System.out.println("Org Id Query::"+agentQry);
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(agentQry);
			while(rs.next()){
				agentOrgId=rs.getInt("organization_id");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return agentOrgId;
	}
	
	public static String setPassword(String userName,String password,int autoPassword){
		String status="FAILED";
		Connection con=DBConnect.getConnection();
		String updatePasswordQry="update st_lms_user_master set password='"+password+"',auto_password="+autoPassword+" where user_name='"+userName+"'";
		try{
			Statement stmt=con.createStatement();
			stmt.executeUpdate(updatePasswordQry);
			
		}catch (Exception e) {
			e.printStackTrace();
			return status;
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
				return status;
			}
		}
		status="SUCCESS";
		return status;
	}
	
	public static int getUserIdFromUserName(String userName, Connection connection){
		int userId=0;
		String userIdQry="select user_id from st_lms_user_master um where user_name= '"+userName+"'";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs=stmt.executeQuery(userIdQry);
			while(rs.next()){
				userId=rs.getInt("user_id");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		return userId;
	}
	
	public static int getUserIdFromUserName(String userName){
		int userId=0;
		Connection con=DBConnect.getConnection();
		try {
			userId = getUserIdFromUserName(userName, con);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return userId;
	}
	
	public static boolean validateUser(String ip,String systemUsername,String systemPassword){
		
		System.out.println("Validate system user username="+systemUsername+"::ip"+ip);
		if(WrapperUtility.wrapperAuthenticateDataMap != null){
			if(WrapperUtility.wrapperAuthenticateDataMap.get(ip) !=null){
				if(systemUsername.equals(WrapperUtility.wrapperAuthenticateDataMap.get(ip).split(":")[0]) && systemPassword.equals(WrapperUtility.wrapperAuthenticateDataMap.get(ip).split(":")[1])){
					return true;
				}
			}
			
		}
		System.out.println("INVALID System username or password");
		return false;
	}
	
public static boolean isTerminateAgent(String agentName){
		
		System.out.println("Validate Agent Name="+agentName);
		Connection con=DBConnect.getConnection();
		String getAgentQry="select name from st_lms_organization_master where name='"+agentName+"' and organization_status !='TERMINATE'";
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(getAgentQry);
			while(rs.next()){
				return false;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
				
		System.out.println("Terminate Agent Name");
		return true;
	}
	
public static int getOrgIdFromOrgType(String orgType){
	int orgId=-1;
	Connection con=DBConnect.getConnection();
	String orgQry="select organization_id from st_lms_organization_master where organization_type='"+orgType+"'";
	System.out.println("get Org Id From Org Type Qry::"+orgQry);
	try {
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(orgQry);
		while(rs.next()){
			orgId=rs.getInt("organization_id");
		}
	} catch (SQLException e) {
		
		e.printStackTrace();
	}finally{
		try{
			con.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	return orgId;
}


public static boolean isAgentExist(int agentUserId){
	
	System.out.println("Validate Agent Name="+agentUserId);
	Connection con=DBConnect.getConnection();
	
	try {
		PreparedStatement pstmt=con.prepareStatement("select organization_id from st_lms_user_master where user_id=?");
		pstmt.setInt(1, agentUserId);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next())
			return true;
		else
			return false;
	} catch (SQLException e) {
		
		e.printStackTrace();
	}finally{
		try{
			con.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
			
	System.out.println("Terminate Agent Name");
	return false;
}


}
