package com.skilrock.lms.admin.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;

public class AdminAuthHelper {

	public LoginBean loginAuthentication(String username, String password,
			String interface_type) {
		Connection con = null;
		String dbPass = "";
		int autoGenerate = 0;
		String status = "";
		int uid = -1;
		LoginBean loginBean = new LoginBean();
		UserInfoBean userInfo;
		ArrayList<String> userActionList = new ArrayList<String>();
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String getUserDetailsQuery = "select user_id, user_name, password,status,auto_password,if(login_attempt<3,'ALLOW','BLOCK') login_attempt from st_admin_user_master where user_name =?";

			PreparedStatement pstmt = con.prepareStatement(getUserDetailsQuery);
			pstmt.setString(1, username.trim());

			ResultSet rs = pstmt.executeQuery();
			if (rs.getFetchSize() > 1) {
				loginBean.setStatus("ERROR");
				return loginBean;
			}

			if (rs.next()) {
				dbPass = rs.getString("password");
				status = rs.getString("status");
				username = rs.getString("user_name");
				uid = rs.getInt("user_id");
				autoGenerate = rs.getInt("auto_password");

				userInfo = new UserInfoBean();
				userInfo.setUserId(uid);
				userInfo.setUserName(username);
				userInfo.setStatus(status);
				userInfo.setUserType("ADMIN");
			} else {
				loginBean.setStatus("USER_NAME_NOT_MATCH");
				return loginBean;
			}

			// Login Password SkilRock@123
			if ("BLOCK".equals(rs.getString("login_attempt"))) {
				String updatePass = "update st_admin_user_master set auto_password='1', password='Y8+x74/0W7gFNGWXunNeEA==' where user_id='"
						+ uid + "'";
				pstmt = con.prepareStatement(updatePass);
				pstmt.executeUpdate();
			}

			if (MD5Encoder.encode(password).equals(dbPass)) {
				if (status.equals("INACTIVE") || status.equals("TERMINATE")) {
					loginBean.setStatus("ERRORINACTIVE");
					return loginBean;
				}
				String countLoginAttempt = "update st_admin_user_master set  login_attempt=0 where user_name='"
						+ username + "'";
				pstmt = con.prepareStatement(countLoginAttempt);
				pstmt.executeUpdate();

				rs.close();
				pstmt.close();

				LinkedHashMap<String, ArrayList<PriviledgeBean>> actionServiceMap = new LinkedHashMap<String, ArrayList<PriviledgeBean>>();
				PriviledgeBean privBean = null;
				String getAction = null;
				String getMenuTitle = null;

				ArrayList<PriviledgeBean> privList = new ArrayList<PriviledgeBean>();
				getAction = "select distinct(action_mapping) from st_admin_priviledge_rep pr where status='ACTIVE'";
				getMenuTitle = "select menu_disp_name,item_order,related_to,action_mapping from st_admin_menu_master smm,st_admin_priviledge_rep pr where  pr.status='ACTIVE' and smm.action_id=pr.action_id order by related_to,item_order,menu_disp_name";
				PreparedStatement pstmtPriv = con.prepareStatement(getAction);
				ResultSet rsPriv = pstmtPriv.executeQuery();
				while (rsPriv.next()) {
					userActionList.add(rsPriv.getString("action_mapping"));
				}

				pstmtPriv = con.prepareStatement(getMenuTitle);
				rsPriv = pstmtPriv.executeQuery();
				while (rsPriv.next()) {
					privBean = new PriviledgeBean();
					privBean.setPrivTitle(rsPriv.getString("menu_disp_name"));
					privBean.setActionMapping(rsPriv
							.getString("action_mapping"));
					privBean.setRelatedTo(rsPriv.getString("related_to"));
					privList.add(privBean);
				}
				if (privList.size() > 0) {
					actionServiceMap.put("ADMIN-ADMIN", privList);
				}

				// Add Inactive privilege Of All other type users

				String getService = "select distinct service_display_name,service_code,priv_rep_table,menu_master_table from st_lms_service_master sm,st_lms_service_delivery_master sdm where sm.status='ACTIVE' and sdm.status='ACTIVE' and sdm.service_id=sm.service_id";
				// String getPrivId = "select distinct(upm.priv_id) from
				// st_lms_role_priv_mapping rpm,st_lms_user_priv_mapping upm
				// where upm.user_id=? and upm.role_id=? and rpm.status='ACTIVE'
				// and upm.status='ACTIVE'and upm.role_id=rpm.role_id and
				// upm.service_mapping_id=?";

				pstmt = con.prepareStatement(getService);
				System.out.println(pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ArrayList<PriviledgeBean> privListSec = new ArrayList<PriviledgeBean>();
					getAction = "select distinct(action_mapping) from "
							+ rs.getString("priv_rep_table")
							+ " pr where pr.status='INACTIVE' and pr.priv_owner='BO'";
					getMenuTitle = "select menu_disp_name,item_order,related_to,action_mapping from "
							+ rs.getString("menu_master_table")
							+ " smm,"
							+ rs.getString("priv_rep_table")
							+ " pr where pr.status='INACTIVE' and pr.priv_owner='BO' and smm.action_id=pr.action_id order by related_to,item_order,menu_disp_name";
					pstmtPriv = con.prepareStatement(getAction);
					System.out.println(pstmtPriv);
					rsPriv = pstmtPriv.executeQuery();
					while (rsPriv.next()) {
						userActionList.add(rsPriv.getString("action_mapping"));
					}
					pstmtPriv = con.prepareStatement(getMenuTitle);
					System.out.println(pstmtPriv);
					rsPriv = pstmtPriv.executeQuery();
					while (rsPriv.next()) {
						privBean = new PriviledgeBean();
						privBean.setPrivTitle(rsPriv
								.getString("menu_disp_name"));
						privBean.setActionMapping(rsPriv
								.getString("action_mapping"));
						privBean.setRelatedTo(rsPriv.getString("related_to"));
						privListSec.add(privBean);
					}
					if (privListSec.size() > 0) {
						actionServiceMap.put(rs
								.getString("service_display_name")
								+ "-" + rs.getString("service_code"),
								privListSec);
					}
				}

				loginBean.setActionServiceMap(actionServiceMap);
				loginBean.setUserInfo(userInfo);
				loginBean.setUserActionList(userActionList);

				String insertLoginDate = "update st_admin_user_master set  last_login_date=? where user_name='"
						+ username + "'";
				pstmt = con.prepareStatement(insertLoginDate);
				pstmt.setTimestamp(1, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.executeUpdate();

				if (autoGenerate == 1) {
					loginBean.setStatus("FirstTime");
					return loginBean;
				}
				loginBean.setStatus("success");
			} else {
				loginBean.setStatus("PASS_NOT_MATCH");
				String countLoginAttempt = "update st_admin_user_master set login_attempt=login_attempt+1 where user_name='"
						+ username + "'";
				pstmt = con.prepareStatement(countLoginAttempt);
				pstmt.executeUpdate();
			}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return loginBean;
	}

	public boolean verifyPasswordChars(String newPassword) {
		boolean isUpperThr = false;
		boolean isLowerThr = false;
		boolean isDigitThr = false;
		char[] passArr = newPassword.toCharArray();
		for(int i=0;i<passArr.length;i++){
			if(Character.isUpperCase(passArr[i])){
				isUpperThr = true;
			}
			if(Character.isLowerCase(passArr[i])){
				isLowerThr = true;
			}
			if(Character.isDigit(passArr[i])){
				isDigitThr = true;
			}
		}
		return (isUpperThr && isLowerThr & isDigitThr);
	}

	public String changePassword(String username, String pass, String newpass,
			String verifypass) throws LMSException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("Inside change password helper.............");
		try {

			con = DBConnect.getConnection();

			String pws = MD5Encoder.encode(pass);
			String newPws = MD5Encoder.encode(newpass);
			String dbPass = "";

			// read the current password of user from database
			pstmt = con
					.prepareStatement("select password, user_id from st_admin_user_master where user_name =?");
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			int uid = -1;
			while (rs.next()) {
				dbPass = rs.getString("password");
				uid = rs.getInt("user_id");
			}

			rs.close();
			pstmt.close();

			if (newpass.equals(verifypass) && dbPass.equals(pws)) {
				// retrieve the last 3 password from history tables
				// pstmt = con.prepareStatement(""); //check in history
				// pstmt.setInt(1, uid);
				// System.out.println("querty11:: " + pstmt);
				// rs = pstmt.executeQuery();
				// while (rs.next()) {
				// if (newPws.equals(rs.getString("password"))) {
				// return "INPUT";
				// }
				// }
				// rs.close();
				// pstmt.close();

				con.setAutoCommit(false);
				// insert new password into history table
				// pstmt = con.prepareStatement("INSERT into
				// st_lms_password_history(user_id, password, date_changed,
				// type) VALUES (?, ?, ?, ?)");
				// pstmt.setInt(1, uid);
				// pstmt.setString(2, newPws);
				// pstmt.setTimestamp(3, new java.sql.Timestamp(
				// new java.util.Date().getTime()));
				// pstmt.setString(4, "0");
				// pstmt.executeUpdate();
				// pstmt.close();
				// update password as new password into user_master table
				pstmt = con
						.prepareStatement("update st_admin_user_master set  password = ? ,auto_password = 0 where user_id = ?");
				pstmt.setString(1, newPws);
				pstmt.setInt(2, uid);
				pstmt.executeUpdate();
				con.commit();
				return "SUCCESS";
			}
			if (!dbPass.equals(pws)) {
				return "INCORRECT";
			}
			return "ERROR";

		}

		catch (SQLException se) {
			System.err.println("Exception: " + se);
			se.printStackTrace();
			throw new LMSException(se);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.err.println("Exception: " + e);
				e.printStackTrace();
			}
		}
	}

}
