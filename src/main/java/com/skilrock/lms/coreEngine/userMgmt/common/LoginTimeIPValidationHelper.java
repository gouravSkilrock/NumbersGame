package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class LoginTimeIPValidationHelper {

	public String[] getAgentWiseOrRetWiseData(String[] retName, String orgType) {
		String[] result = null;

		StringBuilder query = new StringBuilder();
		for(String name : retName)
			query.append(name).append(",");

		String qry = "SELECT user_id, user_name FROM st_lms_user_master WHERE ";
		if(orgType.equals("RETAILER"))
			qry += "organization_id IN ("+query.substring(0, query.length()-1)+");";
		else
			qry += "parent_user_id IN ("+query.substring(0, query.length()-1)+") AND organization_type='AGENT' AND isrolehead='n' ORDER BY user_name;";

		//String qry = "SELECT user_id, user_name FROM st_lms_user_master WHERE "+criteria+" IN ("+query.substring(0, query.length()-2)+") AND organization_type='AGENT' AND isrolehead='y' ORDER BY user_name;";
		Connection con = null;
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(qry);
			rs = pstmt.executeQuery();
			int i = 0;
			
			if(orgType.equals("AGENT"))
				result = new String[retName.length+Util.getRowCount(rs)];
			else
				result = new String[retName.length];

			while(rs.next()) {
				result[i++] = rs.getString("user_id");
			}
			if(orgType.equals("AGENT"))
				for(String name : retName)
					result[i++] = name;
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}

		return result;
	}

	public void insertUserTimeLimitData(int userId, Connection con) throws LMSException {
		SimpleDateFormat format = null;
		Time startTime = null;
		Time endTime = null;

		PreparedStatement pstmt = null;
		try {
			format = new SimpleDateFormat("HH:mm");
			startTime = new Time(format.parse("00:00").getTime());
			endTime = new Time(format.parse("23:59").getTime());
			String userIpMapping = QueryManager.insertUserTimeLimitMapping();
			pstmt = con.prepareStatement(userIpMapping);
			pstmt.setLong(1, userId);
			pstmt.setString(2, null);
			pstmt.setTime(3, startTime);
			pstmt.setTime(4, endTime);
			pstmt.setTime(5, startTime);
			pstmt.setTime(6, endTime);
			pstmt.setTime(7, startTime);
			pstmt.setTime(8, endTime);
			pstmt.setTime(9, startTime);
			pstmt.setTime(10, endTime);
			pstmt.setTime(11, startTime);
			pstmt.setTime(12, endTime);
			pstmt.setTime(13, startTime);
			pstmt.setTime(14, endTime);
			pstmt.setTime(15, startTime);
			pstmt.setTime(16, endTime);
			pstmt.setString(17, "ACTIVE");
			pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sql exception", se);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new LMSException("Parse exception", e);
		}
	}

	public void updateUserTimeLimitData(String[] retName, String allowedIPs, String[] timeLimit) throws Exception {
		SimpleDateFormat format = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			format = new SimpleDateFormat("HH:mm");
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("UPDATE st_lms_user_ip_time_mapping SET allowed_ip=?, monday_start_time=?, monday_end_time=?, tuesday_start_time=?, tuesday_end_time=?, wednesday_start_time=?, wednesday_end_time=?, thursday_start_time=?, thursday_end_time=?, friday_start_time=?, friday_end_time=?, saturday_start_time=?, saturday_end_time=?, sunday_start_time=?, sunday_end_time=? WHERE user_id=?;");
			int length = retName.length;
			for(int i=0; i<length; i++)
			{
				pstmt.setString(1, allowedIPs);
				pstmt.setTime(2, new Time(format.parse(timeLimit[0]).getTime()));
				pstmt.setTime(3, new Time(format.parse(timeLimit[1]).getTime()));
				pstmt.setTime(4, new Time(format.parse(timeLimit[2]).getTime()));
				pstmt.setTime(5, new Time(format.parse(timeLimit[3]).getTime()));
				pstmt.setTime(6, new Time(format.parse(timeLimit[4]).getTime()));
				pstmt.setTime(7, new Time(format.parse(timeLimit[5]).getTime()));
				pstmt.setTime(8, new Time(format.parse(timeLimit[6]).getTime()));
				pstmt.setTime(9, new Time(format.parse(timeLimit[7]).getTime()));
				pstmt.setTime(10, new Time(format.parse(timeLimit[8]).getTime()));
				pstmt.setTime(11, new Time(format.parse(timeLimit[9]).getTime()));
				pstmt.setTime(12, new Time(format.parse(timeLimit[10]).getTime()));
				pstmt.setTime(13, new Time(format.parse(timeLimit[11]).getTime()));
				pstmt.setTime(14, new Time(format.parse(timeLimit[12]).getTime()));
				pstmt.setTime(15, new Time(format.parse(timeLimit[13]).getTime()));
				pstmt.setLong(16, Long.parseLong(retName[i]));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}		
	}

	public String getUserIPTimeLimitByUserId(int userId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		StringBuilder timeLimitData = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("SELECT user_id, allowed_ip, monday_start_time, monday_end_time, tuesday_start_time, tuesday_end_time, wednesday_start_time, wednesday_end_time, thursday_start_time, thursday_end_time, friday_start_time, friday_end_time, saturday_start_time, saturday_end_time, sunday_start_time, sunday_end_time, status FROM `st_lms_user_ip_time_mapping` WHERE user_id=?;");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				timeLimitData.append(rs.getInt("user_id")).append("~");
				timeLimitData.append(rs.getString("allowed_ip")).append("~");
				timeLimitData.append(rs.getTime("monday_start_time")).append("~");
				timeLimitData.append(rs.getTime("monday_end_time")).append("~");
				timeLimitData.append(rs.getTime("tuesday_start_time")).append("~");
				timeLimitData.append(rs.getTime("tuesday_end_time")).append("~");
				timeLimitData.append(rs.getTime("wednesday_start_time")).append("~");
				timeLimitData.append(rs.getTime("wednesday_end_time")).append("~");
				timeLimitData.append(rs.getTime("thursday_start_time")).append("~");
				timeLimitData.append(rs.getTime("thursday_end_time")).append("~");
				timeLimitData.append(rs.getTime("friday_start_time")).append("~");
				timeLimitData.append(rs.getTime("friday_end_time")).append("~");
				timeLimitData.append(rs.getTime("saturday_start_time")).append("~");
				timeLimitData.append(rs.getTime("saturday_end_time")).append("~");
				timeLimitData.append(rs.getTime("sunday_start_time")).append("~");
				timeLimitData.append(rs.getTime("sunday_end_time")).append("~");
				timeLimitData.append(rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}

		return timeLimitData.toString();
	}

	public static void main(String[] args) {
		System.out.println(new LoginTimeIPValidationHelper().getUserIPTimeLimitByUserId(15315));
	}

	public boolean getIPValidationStatus(int userId, String ipAddress)
	{
		return true;

		/*
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet set = null;

		String deviceStr = null;
		String status = null;
		Time startTime = null;
		Time endTime = null;
		Time nowTime = null;

		String[] days = new String[]{"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};
		Calendar calendar = Calendar.getInstance();
		String day = days[calendar.get(Calendar.DAY_OF_WEEK)];

		String query = "SELECT allowed_ip, "+day+"_start_time as startTime, "+day+"_end_time as endTime, STATUS FROM `st_lms_user_ip_time_mapping` WHERE user_id=?;";
		try
		{
			String time = ((calendar.get(Calendar.HOUR)<10)?"0"+calendar.get(Calendar.HOUR):calendar.get(Calendar.HOUR))+":"+calendar.get(Calendar.MINUTE);
			SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
			Date date = format.parse(time+":00");
			nowTime = new Time(date.getTime());

			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, userId);
			set = pstmt.executeQuery();
			if(set.next())
			{
				deviceStr = set.getString("allowed_ip");
				status = set.getString("status");
				startTime = set.getTime("startTime");
				endTime = set.getTime("endTime");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}

		if("INACTIVE".equals(status))
			return true;

		if(nowTime.getTime()>startTime.getTime() && nowTime.getTime()<=endTime.getTime())
		{
			if(deviceStr != null)
			{
				String[] devices = deviceStr.split(",");
				for(String device : devices)
				{
					if(device.equals(ipAddress))
						return true;
				}
			}
		}

		return false;
		*/
	}
}