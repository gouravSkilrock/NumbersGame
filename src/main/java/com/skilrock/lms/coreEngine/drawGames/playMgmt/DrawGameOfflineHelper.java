package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.MailSender;

public class DrawGameOfflineHelper {
	static Log logger = LogFactory.getLog(DrawGameOfflineHelper.class);

	public static boolean checkOfflineUser(int userId) {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String getUserQuery = "select * from st_lms_ret_offline_master where user_id="+ userId + "  and is_offline='YES'";
			pstmt = con.prepareStatement(getUserQuery);
			logger.debug("getUserQuery::::::" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return false;
	}

	public static void checkUserAFUStatus(int gameNo) {
		Timestamp AFUTime = null;
		int offFrzTime = 0;
		StringBuffer inactiveRet = new StringBuffer("0,");
		String fetchOffFrzTime = "select offline_freeze_time from st_dg_game_master where game_nbr="
				+ gameNo;
		String fetchOffRet = "select user_id from st_lms_ret_offline_master where login_status='LOGIN' and last_AFU_time<?";

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(fetchOffFrzTime);
			rs = pstmt.executeQuery();
			logger.debug("******checkUserAFUStatus****1******" + pstmt);
			if (rs.next()) {
				offFrzTime = rs.getInt("offline_freeze_time");
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, -offFrzTime);
			AFUTime = new Timestamp(cal.getTimeInMillis());

			pstmt = con.prepareStatement(fetchOffRet);
			pstmt.setTimestamp(1, AFUTime);
			logger.debug("******checkUserAFUStatus****2******" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				inactiveRet.append(rs.getInt("user_id") + ",");
			}
			pstmt = con
					.prepareStatement("update st_lms_ret_offline_master set offline_status='INACTIVE' where user_id in ("
							+ inactiveRet
									.deleteCharAt(inactiveRet.length() - 1)
									.toString() + ")");
			logger.debug("******checkUserAFUStatus****3******" + pstmt);
			pstmt.executeUpdate();
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

	}

	public static boolean fetchLoginStatus(int orgId) {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isLogin = false;
		try {
			String loginQry = "select login_status from st_lms_ret_offline_master where organization_id= ? and is_offline='YES'";
			pstmt = con.prepareStatement(loginQry);
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("login_status") != null
						&& rs.getString("login_status").equals("LOGIN")) {
					isLogin = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return isLogin;
	}

	public static String fetchOfflineUserStatus(int orgId) {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String offStatus="INACTIVE";
		try {
			String loginQry = "select offline_status from st_lms_ret_offline_master where organization_id= ? and is_offline='YES'";
			pstmt = con.prepareStatement(loginQry);
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				offStatus=rs.getString("offline_status");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return offStatus;
	}
	
	public static UserInfoBean fillUserBeanData(UserInfoBean userBean) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select om.organization_id,om.organization_type,om.parent_id from st_lms_organization_master om,st_lms_user_master um where om.organization_id=um.organization_id and um.user_id=?");
			pstmt.setInt(1, userBean.getUserId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				userBean.setUserOrgId(rs.getInt("organization_id"));
				userBean.setParentOrgId(rs.getInt("parent_id"));
				userBean.setUserType(rs.getString("organization_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return userBean;
	}

	// method added for inserting information about offline retailer

	public static void sendMailToBo(File file, String errorMsg, String fileName) {
		String FROM = "lms.user@skilrock.com";
		String PASSWORD = "skilrock";
		List<String> to = new ArrayList<String>();
		to.add("support.wgrl@skilrock.com");
		to.add("error.wgrl@skilrock.com");
		String subject = "Offline File Upload Having Problem: " + errorMsg;
		String bodyText = errorMsg;
		try {
			MailSender ms = new MailSender(FROM, PASSWORD, to, subject,
					bodyText, file, fileName);
			ms.setDaemon(true);
			ms.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	@SuppressWarnings("unchecked")
	public static String setAFUTime(UserInfoBean userInfoBean, ServletContext sc) {
		String saleStartTime = (String) sc.getAttribute("SALE_START_TIME");
		String saleEndTime = (String) sc.getAttribute("SALE_END_TIME");

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		boolean updateOffStatus = true;
		try {
			con.setAutoCommit(false);

			String selQuery = "select game_nbr from st_dg_game_master where is_offline='Y'";
			pstmt = con.prepareStatement(selQuery);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				selQuery = "select status from st_dg_offline_files_? where retailer_user_id=? and status in('UPLOADED','LATE_UPLOAD','ERROR')";
				PreparedStatement newPstmt = con.prepareStatement(selQuery);
				newPstmt.setInt(1, rs.getInt("game_nbr"));
				newPstmt.setInt(2, userInfoBean.getUserId());
				ResultSet newRs = newPstmt.executeQuery();
				if (newRs.next()) {
					updateOffStatus = false;
					break;
				}
			}
			String addStatusQry = "";
			if (updateOffStatus) {
				addStatusQry = " ,offline_status='ACTIVE' ";
			}
			String getUserQuery = "update st_lms_ret_offline_master set last_AFU_time=? "
					+ addStatusQry + " where user_id=?";
			pstmt = con.prepareStatement(getUserQuery);
			pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
			pstmt.setInt(2, userInfoBean.getUserId());
			pstmt.executeUpdate();
			con.commit();

			String isOffline = "";
			if (DrawGameOfflineHelper
					.checkOfflineUser(userInfoBean.getUserId())) {
				isOffline = "Y";
			} else {
				isOffline = "N";
			}

			AjaxRequestHelper ajaxRequestHelper = new AjaxRequestHelper();
			ajaxRequestHelper.getAvlblCreditAmt(userInfoBean);

			Double bal1 = userInfoBean.getAvailableCreditLimit();
			double bal2 = userInfoBean.getClaimableBal();
			if (bal1 == null) {
				bal1 = 0.0;
			}
			double bal = bal1 - bal2;
			NumberFormat nFormat = NumberFormat.getInstance();
			nFormat.setMinimumFractionDigits(2);
			String balance = nFormat.format(bal).replace(",", "");

			//String balance = bal + "00";
			balance = balance.substring(0, balance.indexOf(".") + 3);

			return "|isOffline:"
					+ isOffline
					+ "|balance:"
					+ balance
					+ "|SALE_ST:"
					+ saleStartTime
					+ "|SALE_ET:"
					+ saleEndTime
					+ "|status:"
					+ fetchOfflineUserStatus(userInfoBean.getUserOrgId())
					+ "|userId:"
					+ userInfoBean.getUserId()
					+ "|GameInfo:"
					+ DrawGameRPOSHelper.embdDgData(true,
							(TreeMap<Integer, Map<Integer, String>>) sc
									.getAttribute("drawIdTableMap"),
							userInfoBean.getUserId(),userInfoBean.getUserOrgId(), 5.7) + "|";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return null;
	}

	public static boolean setInactiveRetailer(int userId) {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);

			String getUserQuery = "update st_lms_ret_offline_master set offline_status='INACTIVE' where user_id="
					+ userId;
			pstmt = con.prepareStatement(getUserQuery);
			pstmt.executeUpdate();
			con.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return false;
	}
/**
 * @deprecated use updateLoginStatus(int userId,Connection con)
 * @param userId
 * @return
 */
	public static String updateLoginStatus(int userId) {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String loginStatus = null;
		try {
			String loginQry = "select login_status from st_lms_ret_offline_master where user_id= ?";
			pstmt = con.prepareStatement(loginQry);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				loginStatus = rs.getString("login_status");
			}
			if (loginStatus != null && loginStatus.equals("LOGIN")) {
				return "LOGIN";
			} else if (loginStatus != null && loginStatus.equals("LOGOUT")) {
				String updLoginQuery = "update st_lms_ret_offline_master set login_status='LOGIN', last_AFU_time = ? where user_id= ?";
				pstmt = con.prepareStatement(updLoginQuery);
				pstmt.setTimestamp(1, new Timestamp((new Date()).getTime()));
				pstmt.setInt(2, userId);

				if (pstmt.executeUpdate() == 1) {
					return "LOGOUT";
				}
			}
			logger.debug("loginQuery........" + pstmt);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return "INACTIVE";
	}

	public static boolean updateLogoutSuccess(String userName) {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("update st_lms_ret_offline_master set login_status='LOGOUT' where user_id=(select user_id from st_lms_user_master where user_name=?)");
			pstmt.setString(1, userName);
			if (pstmt.executeUpdate() == 1) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return false;
	}

	public int saveOfflineRetData(int userId, int orgId, String isOffline,
			String deviceType, String terminalId, Connection connection,
			String latLon, String cityName,String isLoginBinding,String []sim,String []simModelName,boolean isSimBind) throws SQLException {
		boolean loginBind = "YES".equalsIgnoreCase(isLoginBinding) ? true : false;
		PreparedStatement psmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String cityCode = null;
		int retOfflineId = 0;
		String maxVerAvailable = null ;
		stmt = connection.createStatement();
		ArrayList<String> simColList = new ArrayList<String>();
		ArrayList<String> simNbrList = new ArrayList<String>();
		if(loginBind){
			String query ="select serial_number from st_lms_ret_offline_master where  serial_number ='"+terminalId+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()){
				logger.info("terminal Already Assigned !!"+query);
				return retOfflineId;
				
			}
			String versionFinder = "select max(cast(`version` as decimal(5,2))) as exp_version  from st_lms_htpos_version_master where device_id = (select device_id from st_lms_htpos_device_master where device_type = '"+deviceType+"')  and status = 'ACTIVE';" ;
			logger.info("Max Ver for "+deviceType + " : " + versionFinder);
			rs = stmt.executeQuery(versionFinder);
			if(rs.next())
				maxVerAvailable = rs.getString("exp_version") ;

			logger.info("maxVerAvailable - " + maxVerAvailable);
		}
		if(isSimBind){
		
			for(int i=0;i<simModelName.length;i++){
				
				if(simModelName[i].trim()!="-1"){
					String query ="select inv_column_name from st_lms_inv_model_master where model_name='"+simModelName[i]+"'";
					rs = stmt.executeQuery(query);
					if(rs.next()){
						simColList.add(rs.getString("inv_column_name"));
						simNbrList.add(sim[i]);
						String simQry ="select "+rs.getString("inv_column_name")+" from st_lms_ret_offline_master where  "+rs.getString("inv_column_name")+" ='"+sim[i]+"'";
						rs1 = stmt.executeQuery(simQry);
						if(rs1.next()){
							logger.info("sim Already Assigned !!"+simQry);
							return retOfflineId;
							
						}
						rs1.close();
					}
					rs.close();
					
				}
				
				
			}
			
			
		}

		String queryAppender = (loginBind) ? ",expected_version, is_download_available" : "" ;
		String parameterAppender = (loginBind) ? ",?,?" : "" ;

		rs = stmt.executeQuery("select city_code from st_lms_city_master where city_name='"+cityName+"' ");
		while (rs.next()) {
			cityCode = rs.getString("city_code");
			String insOfflineRet = "insert into st_lms_ret_offline_master(user_id,organization_id,offline_status,login_status,last_AFU_time,is_offline, serial_number,device_type ,lat,lon,city_code"+ queryAppender +" ) values(?,?,?,?,?,?,?,?,?,?,?"+ parameterAppender +")";

			psmt = connection.prepareStatement(insOfflineRet);
			psmt.setInt(1, userId);
			psmt.setInt(2, orgId);
			psmt.setString(3, "ACTIVE");
			psmt.setString(4, "LOGOUT");
			psmt.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
			psmt.setString(6, isOffline);
			psmt.setString(7, terminalId);
			psmt.setString(8, deviceType);
			psmt.setString(9, latLon.split(":")[0]);
			psmt.setString(10, latLon.split(":")[1]);
			psmt.setString(11, cityCode);
			if(loginBind)
			{
				psmt.setString(12, maxVerAvailable);
				psmt.setString(13, "YES");
			}
			retOfflineId = psmt.executeUpdate();
			if(isSimBind){
				
				StringBuilder  updateStrBuider = new StringBuilder();
				updateStrBuider.append(" update st_lms_ret_offline_master set ");
				for(int i=0;i<simColList.size();i++){
					if(i==0)updateStrBuider.append(simColList.get(i)+"='"+simNbrList.get(i)+"' ");
					else  updateStrBuider.append(" ,"+simColList.get(i)+"='"+simNbrList.get(i)+"' "); 
				}
				updateStrBuider.append(" where organization_id="+orgId);
				psmt =connection.prepareStatement(updateStrBuider.toString());
				logger.debug(" update sim nbrs qry"+psmt);
				psmt.executeUpdate();
				
				
			}
		
		}
		rs.close();
		stmt.close();
		
		
		return retOfflineId;
	}
	public static boolean checkOfflineUser(int userId,Connection con) {

		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String getUserQuery = "select * from st_lms_ret_offline_master where user_id="+userId+" and is_offline='YES'";
			stmt = con.createStatement();
			logger.info("getUserQuery::::::" + getUserQuery);
			rs = stmt.executeQuery(getUserQuery);
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Exception ",e);
			e.printStackTrace();
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}
		return false;
	}
	
	public static String fetchOfflineUserStatus(int orgId,Connection con) {

		//	Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String offStatus="INACTIVE";
		try {
			String loginQry = "select offline_status from st_lms_ret_offline_master where organization_id= ? and is_offline='YES'";
			pstmt = con.prepareStatement(loginQry);
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				offStatus=rs.getString("offline_status");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//DBConnect.closeCon(con);
		}
		return offStatus;
	}
	public static String updateLoginStatus(int userId,Connection con) {

		//	Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String loginStatus = null;
		try {
			String loginQry = "select login_status from st_lms_ret_offline_master where user_id= ?";
			pstmt = con.prepareStatement(loginQry);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				loginStatus = rs.getString("login_status");
			}
			if (loginStatus != null && loginStatus.equals("LOGIN")) {
				return "LOGIN";
			} else if (loginStatus != null && loginStatus.equals("LOGOUT")) {
				String updLoginQuery = "update st_lms_ret_offline_master set login_status='LOGIN', last_AFU_time = ? where user_id= ?";
				pstmt = con.prepareStatement(updLoginQuery);
				pstmt.setTimestamp(1, new Timestamp((new Date()).getTime()));
				pstmt.setInt(2, userId);

				if (pstmt.executeUpdate() == 1) {
					return "LOGOUT";
				}
			}
			logger.debug("loginQuery........" + pstmt);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//DBConnect.closeCon(con);
		}
		return "INACTIVE";
	}

}
