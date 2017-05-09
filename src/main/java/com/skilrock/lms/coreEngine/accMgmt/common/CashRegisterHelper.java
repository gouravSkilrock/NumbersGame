package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;

public class CashRegisterHelper {
	public Map<Integer,String> getCashierDetails() {
		Connection con = null;
		Map<Integer, String> cashierMap = new LinkedHashMap<Integer, String>();
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String query = "select user_id,user_name from st_lms_user_master where organization_type='BO' and status='ACTIVE'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				cashierMap.put(rs.getInt("user_id"), rs.getString("user_name"));
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cashierMap;
	}
	public Map<Integer,String> getDrawerDetails() {
		Connection con = null;
		Map<Integer, String> drawerMap = new LinkedHashMap<Integer, String>();
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String query = "select drawer_id,drawer_name from st_lms_bo_cash_drawer_master";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				drawerMap.put(rs.getInt("drawer_id"), rs.getString("drawer_name"));
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return drawerMap;
		
	}
	public void assignDrawer(int cashierId,int drawerId,int userId,String[] multiples,String[] denoType)
	{
		Connection con = null;
		PreparedStatement preState7 = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			PreparedStatement pstmt = con.prepareStatement("update st_lms_bo_cash_drawer_status set cashier_id = ?,nbrOfNotes=? where drawer_id = ? ");
			pstmt.setInt(1, cashierId);
			pstmt.setInt(2,0);
			pstmt.setInt(3,drawerId);
			pstmt.executeUpdate();
			
			if (multiples != null) {
				for (int i = 0; i < multiples.length; i++) {

					if (!multiples[i].equalsIgnoreCase("")) {
						String denoType1 = denoType[i].trim();
						int multiples1 = Integer.parseInt(multiples[i].trim());

						preState7 = con
								.prepareStatement("update st_lms_bo_cash_drawer_status set nbrOfNotes=? where drawer_id=? and denomination=?");
						preState7.setInt(1, multiples1);
						preState7.setInt(2, drawerId);
						preState7.setString(3, denoType1);
						preState7.executeUpdate();
					}
				}
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public String checkDrawerAvailability(int drawerId)
	{
		StringBuilder builder = new StringBuilder();
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select user_name from st_lms_bo_cash_drawer_status ca inner join st_lms_user_master user on ca.cashier_id=user.user_id where drawer_id="+drawerId+"");
			if(rs.next())
			{
				builder.append("ASSIGNED").append(":").append(rs.getString("user_name"));
			}
			else
			{
				builder.append("Not_Assigned").append(":").append("null");
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	public void clearDrawer(int drawerId,int userId)
	{
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			

			Statement stmt1 = con.createStatement();
			ResultSet rs = stmt1.executeQuery("select cashier_id,denomination,nbrOfNotes from st_lms_bo_cash_drawer_status  where drawer_id="+drawerId+" and cashier_id!=0");
			while(rs.next())
			{
					PreparedStatement pstmtHistory = con.prepareStatement("insert into st_lms_bo_cash_drawer_status_history(cashier_id, drawer_id, denomination, nbrOfNotes, date, update_mode, doneBy)values(?, ?, ?, ?, ?, ?, ?)");
					pstmtHistory.setInt(1, rs.getInt("cashier_id"));
					pstmtHistory.setInt(2,drawerId);
					pstmtHistory.setString(3,rs.getString("denomination"));
					pstmtHistory.setInt(4, rs.getInt("nbrOfNotes"));
					pstmtHistory.setTimestamp(5, new Timestamp(new Date().getTime()));
					pstmtHistory.setString(6,"CLEAR");
					pstmtHistory.setInt(7, userId);
					pstmtHistory.executeUpdate();
			}
			
			PreparedStatement stmt = con.prepareStatement("update st_lms_bo_cash_drawer_status set cashier_id = ?,nbrOfNotes=? where drawer_id = ? ");
			stmt.setInt(1, 0);
			stmt.setInt(2, 0);
			stmt.setInt(3,drawerId);
			stmt.executeUpdate();
			
			
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void createDrawer(String drawerName,String remarks,String[] denoArray)
	{
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			PreparedStatement insmaster = con
					.prepareStatement("insert into st_lms_bo_cash_drawer_master(drawer_name, remarks)values(?,?)");
			insmaster.setString(1, drawerName);
			insmaster.setString(2, remarks);
			insmaster.executeUpdate();
			int drawerId = 0;
			ResultSet rs1 = insmaster.getGeneratedKeys();
			if (rs1.next()) {
				drawerId = rs1.getInt(1);
				for (int i = 0; i < denoArray.length; i++) {
					PreparedStatement stmt = con
							.prepareStatement("insert into st_lms_bo_cash_drawer_status(cashier_id,drawer_id,denomination,nbrOfNotes)values(?,?,?,?)");
					stmt.setInt(1, 0);
					stmt.setInt(2, drawerId);
					stmt.setString(3, denoArray[i]);
					stmt.setInt(4, 0);
					stmt.executeUpdate();
				}
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getReturnDenoList(double num, int cashierId) {
		Map<Integer, Integer> denoMap = new LinkedHashMap<Integer, Integer>();
		Map<Integer, Integer> resultMap = new LinkedHashMap<Integer, Integer>();
		//Map<Integer, Integer> resultMapNew = new LinkedHashMap<Integer, Integer>();
		Connection con = null;
		double totalSum = 0.0;
		double m = num;
		try {
			con = DBConnect.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select denomination,nbrOfNotes from st_lms_bo_cash_drawer_status where cashier_id="
							+ cashierId + "");
			while (rs.next()) {
				denoMap.put(Integer.parseInt(rs.getString("denomination")), rs
						.getInt("nbrOfNotes"));
			}
			Set<Entry<Integer, Integer>> set = denoMap.entrySet();
			Iterator<Entry<Integer, Integer>> itr = set.iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, Integer> entry = itr.next();
				if (num % entry.getKey() >= 0) {
					int d = (int) num / entry.getKey();
					if (d <= entry.getValue()) {
						num = num - (entry.getKey() * d);
					} else if (entry.getValue() > 0) {
						d = entry.getValue();
						num = num - (entry.getKey() * d);
					}
					if (d != 0) {
						if (entry.getValue() <= 0) {
							d = 0;
						}
						resultMap.put(entry.getKey(), d);
						totalSum = totalSum + (entry.getKey() * d);
					}
				}
			}
			if (totalSum < m) {
				Set<Entry<Integer, Integer>> set1 = resultMap.entrySet();
				Iterator<Entry<Integer, Integer>> itr1 = set1.iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, Integer> entry1 = itr1.next();
					if(entry1.getValue()>0)
					resultMap.put(entry1.getKey(), 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultMap.toString();
	}
	public String checkDrawerExistance(String drawerName)
	{
		String returnType = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from st_lms_bo_cash_drawer_master where drawer_name='"
							+ drawerName + "'");
			if (rs.next()) {
				returnType = "EXIST";
			} else {
				returnType = "NOT_EXIST";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnType;
	}
	public List<String> getReceivedDenoList() {
		List<String> denoList = new ArrayList<String>();
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select denomination_type from st_lms_denomination_master deno inner join st_lms_country_master coun on deno.country_code=coun.country_code ");
			while (rs.next()) {
				denoList.add(rs.getString("denomination_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return denoList;
	}
	public String getExchangeDenoList(UserInfoBean userBean) {
		List<String> denoList = new ArrayList<String>();
		Connection con = null;
		String result = null;
		try {
			con = DBConnect.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select denomination from st_lms_bo_cash_drawer_status where cashier_id="+userBean.getUserId()+" and nbrOfNotes!=0");
			while (rs.next()) {
				denoList.add(rs.getString("denomination"));
			}
			result = denoList.toString().replace("[", "").replace("]", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
