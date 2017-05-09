package com.skilrock.lms.coreEngine.loginMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.userMgmt.common.VirtualSportsControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSResponseBean;

/**
 * This class provides methods for change password
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChangePasswordHelper {

	static Log logger = LogFactory.getLog(ChangePasswordHelper.class);

	private Connection con;
	private PreparedStatement pstmt;
	private HttpServletRequest request;
	private ResultSet rs;

	/**
	 * This method verifies old password and user name and allows user to change
	 * password
	 * 
	 * @param username
	 *            stands for user's Login Name
	 * @param pass
	 *            stands for user's Old Password
	 * @param newpass
	 *            stands for New Password
	 * @param verifypass
	 *            is duplicate of New Password entered by User
	 * @return String
	 * @throws LMSException
	 */
	public String changePassword(String username, String pass, String newpass,
			String verifypass, boolean isWebRetailer) throws LMSException {
		logger.debug("Inside change password helper.............");
		try {
			con = DBConnect.getConnection();
			String pws = MD5Encoder.encode(pass);
			String newPws = MD5Encoder.encode(newpass);
			String dbPass = "";

			// read the current password of user from database
			pstmt = con.prepareStatement(QueryManager.getST3Password());
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			int uid = -1;
			while (rs.next()) {
				dbPass = rs.getString(TableConstants.USER_PASSWORD);
				uid = rs.getInt(TableConstants.USER_ID);
			}

			rs.close();
			pstmt.close();

			if (newpass.equals(verifypass) && dbPass.equals(pws)) {
				// retrieve the last 3 password from history tables
				pstmt = con.prepareStatement(QueryManager
						.getST3PasswordHistory());
				pstmt.setInt(1, uid);
				logger.debug("querty11:: " + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (newPws.equals(rs.getString("password"))) {
						return "NEW_OLD_SAME";
					}
				}
				rs.close();
				pstmt.close();
				con.setAutoCommit(false);
				// insert new password into history table
				pstmt = con.prepareStatement(QueryManager.insertST3PasswordHistory());
				pstmt.setInt(1, uid);
				pstmt.setString(2, newPws);
				pstmt.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(4, "0");
				pstmt.executeUpdate();
				pstmt.close();
				// update password as new password into user_master table
				pstmt = con
						.prepareStatement(QueryManager.updateST3UserMaster());
				pstmt.setString(1, newPws);
				pstmt.setInt(2, uid);
				pstmt.executeUpdate();

				if (isWebRetailer && ServicesBean.isVS()) {
					VSRequestBean vsRequestBean = new VSRequestBean(uid, newPws);
					VSResponseBean vsResponseBean = VirtualSportsControllerImpl.Single.INSTANCE.getInstance().resetPassword(vsRequestBean);
					if(!"success".equals(vsResponseBean.getVsCommonResponseBean().getResult()))
						return "ERROR";
				}
				con.commit();
				return "SUCCESS";
			}
			if (!dbPass.equals(pws)) {
				return "INCORRECT";
			}
			return "ERROR";
		} catch (SQLException se) {
			logger.error("Exception: " + se);
			se.printStackTrace();
			throw new LMSException(se);
		} catch (Exception se) {
			logger.error("Exception: " + se);
			se.printStackTrace();
			throw new LMSException(se);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
	}

	public boolean authenticatePassword(String username, String pass,int loginAttempts) throws LMSException {
        boolean isLogin=false;
		logger.debug("Inside authenticate password helper.............");
		try {
			con = DBConnect.getConnection();
     		
			pstmt = con.prepareStatement("select password, user_id,if(login_attempts<"
					+ loginAttempts
					+ ",'ALLOW','BLOCK') logginAttempt  from st_lms_user_master where user_name =?");
			pstmt.setString(1, username);
			
			rs = pstmt.executeQuery();
		if (rs.next()) {
			isLogin= true;
			}else
			{
		     
		      isLogin= false;
		      return isLogin;
			}
		if ("BLOCK".equals(rs.getString("logginAttempt"))) {
			throw new LMSException("LOGIN_LIMIT_REACHED");
			
			
		}
		if (! MD5Encoder.encode(pass).equals(rs.getString("password"))){
			  pstmt = con.prepareStatement("update st_lms_user_master set login_attempts = login_attempts+1 where user_name = ?");
		      pstmt.setString(1, username);
		      pstmt.executeUpdate();
			throw new LMSException("PASS_NOT_MATCH");
		}else{
			pstmt = con
				.prepareStatement("update st_lms_user_master set login_attempts = 0 where user_name = ?");
			pstmt.setString(1, username);
			pstmt.executeUpdate();
		}
		
		return isLogin;
		}catch (SQLException se) {
			logger.error("Exception: " + se);
			se.printStackTrace();
			throw new LMSException("SQL_ERROR");
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
		
	}
	
	
	public boolean verifyPasswordChars(String passwd, boolean isRetailer){
		if(!isRetailer){
			boolean isUpperThr = false;
			boolean isLowerThr = false;
			boolean isDigitThr = false;
			char[] passArr = passwd.toCharArray();
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
		else{
			return true;
		}
	}
	
	public static void main(String args[]){
		ChangePasswordHelper ch = new ChangePasswordHelper();
		System.out.println(ch.verifyPasswordChars("Ab3",false));
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}