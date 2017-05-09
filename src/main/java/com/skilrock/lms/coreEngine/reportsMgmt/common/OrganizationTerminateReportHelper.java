package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import com.skilrock.lms.common.db.DBConnect;

public class OrganizationTerminateReportHelper {

	
	public static List<Integer> RetailerOrgIdIntTypeList=null;
	public static List<Integer> AgentOrgIdIntTypeList=null;
	public static List<String> RetailerOrgIdStringTypeList=null;
	public static List<String> AgentOrgIdStringTypeList=null;
	public static void getTerminateAgentListForRep(Timestamp startDate,Timestamp endDate) throws ParseException{
		AgentOrgIdIntTypeList=new ArrayList<Integer>();
		AgentOrgIdStringTypeList=new ArrayList<String>();
		Connection con=DBConnect.getConnection();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Statement stmt=con.createStatement();
			String fetchTerminateAgentQry="select organization_id,registration_date,termination_date from st_lms_user_master  where (termination_date < '"+new Timestamp(sdf.parse(startDate.toString()).getTime())+"' or registration_date> '"+endDate+"') and isrolehead='Y' and organization_type='AGENT'";
			System.out.println("Fetch Terminate Agent Qry::"+fetchTerminateAgentQry);
			ResultSet rs=stmt.executeQuery(fetchTerminateAgentQry);
			
			while(rs.next()){
				AgentOrgIdIntTypeList.add(rs.getInt("organization_id"));
				AgentOrgIdStringTypeList.add(rs.getString("organization_id"));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	public static void getTerminateRetailerListForRep(Timestamp startDate,Timestamp endDate,int agtOrgId){
		 RetailerOrgIdIntTypeList=new ArrayList<Integer>();
		 RetailerOrgIdStringTypeList=new ArrayList<String>();
		 
		Connection con=DBConnect.getConnection();
		try {
			Statement stmt=con.createStatement();
			String fetchTerminateAgentQry="select organization_id,registration_date,termination_date from st_lms_user_master  where (termination_date < '"+startDate+"' or registration_date> '"+endDate+"') and isrolehead='Y' and parent_user_id="+agtOrgId+" and organization_type='RETAILER'";
			ResultSet rs=stmt.executeQuery(fetchTerminateAgentQry);
			while(rs.next()){
				RetailerOrgIdIntTypeList.add(rs.getInt("organization_id"));
				RetailerOrgIdStringTypeList.add(rs.getString("organization_id"));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
