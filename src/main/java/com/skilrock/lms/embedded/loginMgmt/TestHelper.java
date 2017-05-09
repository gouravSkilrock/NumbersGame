package com.skilrock.lms.embedded.loginMgmt;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;

public class TestHelper extends BaseAction{

	public TestHelper() {
		super(TestHelper.class.getName());
	}
	private static final long serialVersionUID = 1L;

	public void storeData(String iP, int port, String projectName, String date, String time, String cID,String lAC, String responseTime, String signalLevel, String requestCounter, String responseCounter, String sIM_1, String sIM_2, String requestLength, String responseLength, String gPRSEnableTime) {
		Connection con =null;
		Statement stmt=null;
		String query=null;
		try {
			con=DBConnect.getConnection();
			stmt=con.createStatement();
			query="insert into st_lms_test_details (ip,port,project_name,date,time,cid,lac,response_time ,sigal_level,request_counter,response_counter,sim_1,sim_2,request_length,response_length,time_received,gprs_enable_time) values('"+iP+"','"+port+"','"+projectName+"','"+date+"','"+time+"','"+cID+"','"+lAC+"','"+responseTime+"','"+signalLevel+"','"+requestCounter+"','"+responseCounter+"','"+sIM_1+"','"+sIM_2+"','"+requestLength+"','"+responseLength+"','"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()))+"','"+gPRSEnableTime+"');";
			logger.info("Query for test:"+query);
			stmt.executeUpdate(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}