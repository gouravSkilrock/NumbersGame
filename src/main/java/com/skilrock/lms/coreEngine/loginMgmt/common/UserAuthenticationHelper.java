package com.skilrock.lms.coreEngine.loginMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.web.drawGames.common.Util;

public class UserAuthenticationHelper {
	static Log logger = LogFactory.getLog(UserAuthenticationHelper.class);

	public static String fetchOrgMasterUserEmail(int orgId) {
		String email = null;
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select email_id, first_name, last_name from st_lms_user_contact_details slucd,(select user_id from st_lms_user_master slum, st_lms_role_master slrm where slum.organization_id = ? and slum.isrolehead = 'Y' and slum.role_id = slrm.role_id and slrm.is_master = 'Y' )sub where sub.user_id = slucd.user_id");
			ps.setInt(1, orgId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				email = rs.getString("email_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return email;
	}

	public static String fetchUserFirstLastName(int userId) {
		String name = null;
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select first_name, last_name from st_lms_user_contact_details where user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				name = rs.getString("first_name") + " "
						+ rs.getString("last_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return name;
	}

	/*public static void main(String[] args) {
		LoginBean lb = new UserAuthenticationHelper().loginAuthentication("va",
				"12345678", "WEB", 3 + "","EWQQW12123113ASDADSDA",true);
		System.out.println(lb.getActionServiceMap());
	}*/

	
	boolean loggedInResult = false;

	LoginBean loginBean = new LoginBean();


	ArrayList<String> userActionList = new ArrayList<String>();

	private UserInfoBean userInfo;

	public boolean loggedInUser(String user) {
		if (!ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();
			List<String> currentUserList = (List<String>) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserList == null) {
				currentUserList = new ArrayList<String>();
				sc.setAttribute("LOGGED_IN_USERS", currentUserList);
				currentUserList.add(user);
			} else {
				if (currentUserList.contains(user)) {
					return false;
				} else {
					currentUserList.add(user);
				}
			}
		}
		return true;
	}

	
	public LoginBean loginAuthentication(String username, String password,
			String interface_type, String loginLimit,Connection con,String sessionId,boolean isIpTimingCheck) throws LMSException {
		//Connection con=null;
		PreparedStatement pstmt=null;
		PreparedStatement pstmtPriv=null;
		PreparedStatement pstmtPriv1=null;
		ResultSet rs=null;
		ResultSet rsPriv=null;
		try {
			//con = DBConnect.getConnection();
            //here check the terminal staus on this server
			String dbPass = ""; // stores the password retrieved from the
			// database.
			int autoGenerate = 0;
			String role = "";
			int roleId = 0;
			String roleName = "";
			String unam = "";
			String status = "";
			int uid = -1;
			int userMapid = 0;
			String orgStatus = "";
			String orgName = "";
			String orgId = "";
			int parentOrgId = 0;
			int tierId = 0;
			String parentOrgName = "";
			String parentOrgCode="";
			logger.debug("inside Auth Helper");
			String orgCodeQry = "  bb.name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "  bb.org_code orgCode  ";


			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(bb.org_code,'_',bb.name)  orgCode  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(bb.name,'_',bb.org_code)  orgCode ";
			

			}
			String getUserDetailsQuery = "select user_session , if(aa.organization_type = 'RETAILER' , (select user_mapping_id from st_lms_user_random_id_mapping where user_id = aa.user_id) , 0) user_mapping_id, aa.user_id, aa.organization_id,aa.isrolehead,cc.is_master, aa.role_id,cc.tier_id, aa.user_name, aa.password, aa.status, aa.organization_type,bb.organization_status , aa.auto_password, aa.login_attempts"
				+ ", bb.name,bb.org_code,"+orgCodeQry+" , bb.organization_status, bb.current_credit_amt, bb.available_credit, bb.claimable_bal, bb.unclaimable_bal, bb.parent_id, bb.pwt_scrap, cc.role_name,if(login_attempts<"
				+ loginLimit
				+ ",'ALLOW','BLOCK') logginAttempt ,bb.tp_organization istp "
				+ "from st_lms_user_master aa, st_lms_organization_master bb, st_lms_role_master cc "
				+ "where aa.organization_id = bb.organization_id	and aa.user_name =? and aa.role_id = cc.role_id";

			// UPDATE SESSION FOR USER
			Statement stmt = con.createStatement();
			int count = stmt.executeUpdate("update st_lms_user_master set user_session = '"+sessionId +"' where user_name = '"+username.trim()+"'");
			logger.info(count + " Rows updated");
			
			
			pstmt = con.prepareStatement(getUserDetailsQuery);
			pstmt.setString(1, username.trim());

			rs = pstmt.executeQuery();
			logger.debug(pstmt);

			// check More Then One Users Exist in the Database or not with Same
			// user_name
			if (rs.getFetchSize() > 1) {
				logger
						.debug("More Then One User Exist in the Database with Same  user_name"
								+ username);
				loginBean.setStatus("ERROR");
				return loginBean;
			}

			// getting the user details from database
			if (rs.next()) {
				dbPass = rs.getString(TableConstants.USER_PASSWORD);
				role = rs.getString(TableConstants.ORG_TYPE);// This is tier
				// Code
				tierId = rs.getInt(TableConstants.TIER_ID);
				roleId = rs.getInt(TableConstants.ROLE_ID);
				status = rs.getString(TableConstants.USER_STATUS);
				unam = rs.getString(TableConstants.USER_NAME);
				uid = rs.getInt(TableConstants.USER_ID);
				userMapid = rs.getInt(TableConstants.MAPPING_ID);
				orgId = rs.getString(TableConstants.ORG_ID);
				orgName = rs.getString(TableConstants.ORGANIZATION_NAME);
				roleName = rs.getString(TableConstants.ROLE_NAME);
				autoGenerate = rs.getInt("auto_password");
				parentOrgId = rs.getInt("parent_id");
				orgStatus = rs.getString("organization_status");

				userInfo = new UserInfoBean();
				userInfo.setRoleName(rs.getString(TableConstants.ROLE_NAME));
				userInfo.setRoleId(roleId);
				userInfo.setUserId(uid);
				userInfo.setCurrentUserMappingId(userMapid);
				userInfo.setUserName(username);
				userInfo.setUserOrgId(rs.getInt(TableConstants.ORG_ID));
				userInfo.setUserType(role);
				userInfo.setTierId(tierId);
				userInfo.setOrgName(orgName);
				userInfo.setOrgCode(rs.getString("orgCode"));
				userInfo.setUserOrgCode(rs.getString("org_code"));
				userInfo.setAvailableCreditLimit(rs
						.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
				userInfo.setClaimableBal(rs.getDouble("claimable_bal"));
				userInfo.setUnclaimableBal(rs.getDouble("unclaimable_bal"));
				userInfo.setCurrentCreditAmt(rs
						.getDouble(TableConstants.CURRENT_CREDIT_AMT));
				userInfo.setStatus(status);
				userInfo.setOrgStatus(rs.getString(TableConstants.ORG_STATUS));
				userInfo.setPwtSacrap(rs.getString("pwt_scrap"));
				userInfo.setParentOrgId(parentOrgId);
				userInfo.setIsMasterRole(rs.getString("is_master"));
				userInfo.setIsRoleHeadUser(rs.getString("isrolehead"));
				userInfo.setTPUser("YES".equalsIgnoreCase(rs.getString("istp")));
				userInfo.setLoginChannel(interface_type);
				userInfo.setUserSession(rs.getString("user_session"));
			}
			// check the user's info before login
			else {
				loginBean.setStatus("USER_NAME_NOT_MATCH");
				return loginBean;
			}

			if ("BLOCK".equals(rs.getString("logginAttempt"))) {
				loginBean.setStatus("LOGIN_LIMIT_REACHED");
				return loginBean;
			}
			
			//Here matching Time of Login 		
			if(isIpTimingCheck && !getTimeLimitStatus(uid,con))
			{
				logger.info("TIME LIMIT STATUS - Time Limit is exceeded for this user");
				loginBean.setStatus("ERROR_TIME_LIMIT");
				return loginBean;
			}

			String reason = null;
			con.setAutoCommit(false);
			// Matching Password
			if (MD5Encoder.encode(password).equals(dbPass) || userInfo.isTPUser()) {
				// checking the user status
				if (status.equals("BLOCK") || status.equals("TERMINATE")
						|| orgStatus.equals("TERMINATE") || orgStatus.equals("BLOCK") ) {
					
				/*	if("TERMINATE".equals(orgStatus)){
						
						loginBean.setStatus("TERMINAT_WRAPPER");
						return loginBean;
					}*/
					
					loginBean.setStatus("ERRORINACTIVE");
					return loginBean;
				}
				
				if("RETAILER".equalsIgnoreCase(userInfo.getUserType())){			
					String parentOrgStatus = null;
					pstmt = con.prepareStatement("select organization_status from st_lms_organization_master where organization_id=?");
					pstmt.setInt(1, parentOrgId);
					rs = pstmt.executeQuery();
					if(rs.next()){
						parentOrgStatus = rs.getString("organization_status");
						userInfo.setParentOrgStatus(parentOrgStatus);
					}
					if ("BLOCK".equals(parentOrgStatus) || "TERMINATE".equals(parentOrgStatus)) {
						
						loginBean.setStatus("ERRORINACTIVE");
						return loginBean;
					}
				}
				
			/*	if ("INACTIVE".equalsIgnoreCase(orgStatus)) {
					PreparedStatement pstmt2 = con
							.prepareStatement("select reason from st_lms_organization_master_history where organization_status='INACTIVE' and organization_id=? order by date_changed desc limit 1");
					pstmt2.setString(1, orgId);
					ResultSet rs2 = pstmt2.executeQuery();

					if (rs2.next()) {
						reason = rs2.getString("reason");
					}

					//if (!"INACTIVE_AUTO_ACT".equalsIgnoreCase(reason)) {
						if("INACTIVE_MANUAL_BO".equalsIgnoreCase(reason)){
						loginBean.setStatus("ERRORINACTIVE");
						return loginBean;
						//}
					}
				}*/
               //update login attempts and last_login_date together and where condition user_id is used in stead of user_name
				pstmt = con
						.prepareStatement("update st_lms_user_master set login_attempts = 0,last_login_date=? where user_id = ?");
				pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
				pstmt.setInt(2, uid);
				pstmt.executeUpdate();

				
				
				rs.close();
				pstmt.close();
				LinkedHashMap actionServiceMap = new LinkedHashMap();
				if(!"TERMINAL".equals(interface_type)){
				
				PriviledgeBean privBean = null;
				// String getService = "select
				// srm.id,role_id,service_display_name,ref_merchant_id,privilege_rep_table,menu_master_table,service_deligate_url
				// from st_lms_service_role_mapping srm,st_lms_service_master sm
				// where srm.status='ACTIVE' and srm.role_id=? and
				// sm.status='ACTIVE' and srm.service_id=sm.service_id and
				// srm.interface='WEB'";
				String getService = "select srm.id,role_id,interface,service_display_name,service_code,ref_merchant_id,priv_rep_table,menu_master_table,service_deligate_url from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id=? and organization_id=? and srm.status='ACTIVE' and sm.status='ACTIVE' and sdm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id and sdm.interface=?";
				String getPrivId = "select distinct(upm.priv_id) from st_lms_role_priv_mapping rpm,st_lms_user_priv_mapping upm where upm.user_id=? and upm.role_id=? and rpm.status='ACTIVE' and upm.status='ACTIVE'and upm.role_id=rpm.role_id and upm.service_mapping_id=?";
				String getAction = null;
				String getMenuTitle = null;
				pstmt = con.prepareStatement(getService);
				pstmt.setInt(1, roleId);
				pstmt.setInt(2, Integer.parseInt(orgId));
				pstmt.setString(3, interface_type);
				logger.debug(pstmt);
				rs = pstmt.executeQuery();
				String localeName = CommonMethods.getSelectedLocale();
				String menuColumnName = "menu_disp_name";
				if("en".equalsIgnoreCase(localeName) || "fr".equalsIgnoreCase(localeName)){
						menuColumnName = "menu_disp_name_"+localeName;
				}
				while (rs.next()) {
					if("st_sle_priviledge_rep".equals(rs.getString("priv_rep_table"))) {
						continue;
					}
					ArrayList<PriviledgeBean> privList = new ArrayList<PriviledgeBean>();
					getAction = "select distinct(action_mapping) from "
							+ rs.getString("priv_rep_table")
							+ " pr,("
							+ getPrivId
							+ ") result where pr.priv_id=result.priv_id and pr.status='ACTIVE'";
					getMenuTitle = "select "+menuColumnName+" menu_disp_name,item_order,related_to,action_mapping from "
							+ rs.getString("menu_master_table")
							+ " smm,"
							+ rs.getString("priv_rep_table")
							+ " pr,("
							+ getPrivId
							+ ") result where  pr.priv_id=result.priv_id and pr.status='ACTIVE' and smm.action_id=pr.action_id order by related_to,item_order,menu_disp_name";
					pstmtPriv = con.prepareStatement(getAction);
					pstmtPriv.setInt(1, uid);
					pstmtPriv.setInt(2, roleId);
					pstmtPriv.setInt(3, rs.getInt("id"));
					// logger.debug(pstmtPriv);
					rsPriv = pstmtPriv.executeQuery();
					while (rsPriv.next()) {
						userActionList.add(rsPriv.getString("action_mapping"));
					}
					pstmtPriv1 = con.prepareStatement(getMenuTitle);
					pstmtPriv1.setInt(1, uid);
					pstmtPriv1.setInt(2, roleId);
					pstmtPriv1.setInt(3, rs.getInt("id"));
					logger.debug(pstmtPriv1);
					rsPriv = pstmtPriv1.executeQuery();
					while (rsPriv.next()) {
						privBean = new PriviledgeBean();
						privBean.setPrivTitle(rsPriv
								.getString("menu_disp_name"));
						privBean.setActionMapping(rsPriv
								.getString("action_mapping"));
						privBean.setRelatedTo(rsPriv.getString("related_to"));
						privList.add(privBean);
					}
					if (privList.size() > 0) {
						actionServiceMap.put(rs
								.getString("service_display_name")
								+ "-" + rs.getString("service_code"), privList);
					}
				}
				
				if(ServicesBean.isSLE() && ("BO".equalsIgnoreCase(userInfo.getUserType()) || "RETAILER".equalsIgnoreCase(userInfo.getUserType()))) {
					actionServiceMap.put("Sports Lottery-sle", new ArrayList<PriviledgeBean>());
				}
				if(ServicesBean.isVS() && ("RETAILER".equalsIgnoreCase(userInfo.getUserType()))) {
					actionServiceMap.put("Virtual Sports-vs", new ArrayList<PriviledgeBean>());
				}
				if(ServicesBean.isIW() && ("RETAILER".equalsIgnoreCase(userInfo.getUserType()))) {
					actionServiceMap.put("Instant Win-iw", new ArrayList<PriviledgeBean>());
				}
				}

				loginBean.setActionServiceMap(actionServiceMap);
				
				loginBean.setUserActionList(userActionList);
				loginBean.setUserInfo(userInfo);
				/*String insertLoginDate = QueryManager.insertST3LoginDate()
						+ " where user_name='" + username + "'";
				pstmt = con.prepareStatement(insertLoginDate);
				pstmt.setTimestamp(1, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.executeUpdate();*/

				if ("AGENT".equalsIgnoreCase(userInfo.getUserType())
						|| "RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
					PreparedStatement ps = con
							.prepareStatement("select name,org_code from st_lms_organization_master where organization_id = ?");
					ps.setInt(1, parentOrgId);
					rs = ps.executeQuery();

					if (rs.next()) {
						parentOrgName = rs.getString("name");
						parentOrgCode=rs.getString("org_code");
					}

					userInfo.setParentOrgName(parentOrgName);
					userInfo.setParentOrgCode(parentOrgCode);
				     
					// added to reflect web users last login in ret offline master 
					if("RETAILER".equalsIgnoreCase(userInfo.getUserType()) && interface_type.equalsIgnoreCase("WEB")){
						
						
						String updateRetOfflineQuery = "update st_lms_ret_offline_master set login_status=?,last_login_time=?,last_HBT_time=? where user_id = ?";
						pstmt = con.prepareStatement(updateRetOfflineQuery);
						pstmt.setString(1,"LOGIN");
						pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
						pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
						pstmt.setInt(4, userInfo.getUserId());
						logger.info("updateRetOfflineQuery"+pstmt);
						int update=pstmt.executeUpdate();
						logger.info("updated updateRetOffline "+update);
					}
						
				}
				
				

				if (autoGenerate == 1) {
					loginBean.setStatus("FirstTime");
					con.commit();
					return loginBean;
				}

				if (!loggedInUser(username)) {

				}
			} else {
				loginBean.setStatus("PASS_NOT_MATCH");
				pstmt = con
						.prepareStatement("update st_lms_user_master set login_attempts = login_attempts+1 where user_name = ?");
				pstmt.setString(1, username);
				pstmt.executeUpdate();
				con.commit();
				return loginBean;
				
			}
			con.commit();
			
			//double userBal = userInfo.getAvailableCreditLimit()- userInfo.getClaimableBal();
			/*if (userBal < 0 && !"BO".equals(userInfo.getUserType())) {
				if (!"INACTIVE_AUTO_ACT".equalsIgnoreCase(reason)) {
					loginBean.setStatus("BALANCE_NOT_POSITIVE");
					return loginBean;
				} else {
					loginBean.setStatus("success");
				}
				;
				return loginBean;
			}*/
		} catch (SQLException e) {
			logger.error("SQl Error: " + e);
			throw new LMSException();
			//	e.printStackTrace();

		} catch (Exception e) {
			logger.error(" Error: " + e);
			throw new LMSException();
			//e.printStackTrace();

		}finally {
			try {
				/*if (con != null) {
					con.close();
				}*/
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmtPriv != null) {
					pstmtPriv.close();
				}
				if (pstmtPriv1 != null) {
					pstmtPriv1.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (rsPriv != null) {
					rsPriv .close();
				}
			} catch (SQLException e) {
				logger.error("SQl Error: " + e);
				e.printStackTrace();
			}
		}
		loginBean.setStatus("success");
		return loginBean;
	}
	
	
	
	public LoginBean loginAuthentication(String username, String password,
			String interface_type, String loginLimit,String sessionId,boolean isIpTimingCheck) {
		Connection con=null;
	/*	PreparedStatement pstmt=null;
		PreparedStatement pstmtPriv=null;
		PreparedStatement pstmtPriv1=null;
		ResultSet rs=null;
		ResultSet rsPriv=null;*/
		try {
			con = DBConnect.getConnection();
			loginAuthentication(username, password, interface_type, loginLimit, con , sessionId,isIpTimingCheck);
		}catch(Exception e){
			logger.error("Error: " + e);
		}finally{
			DBConnect.closeCon(con);
		}
			
            /*//here check the terminal staus on this server
			String dbPass = ""; // stores the password retrieved from the
			// database.
			int autoGenerate = 0;
			String role = "";
			int roleId = 0;
			String roleName = "";
			String unam = "";
			String status = "";
			int uid = -1;
			String orgStatus = "";
			int userMapid = 0;
			String orgName = "";
			String orgId = "";
			int parentOrgId = 0;
			int tierId = 0;
			String parentOrgName = "";
			String parentOrgCode="";
			logger.debug("inside Auth Helper");
			String orgCodeQry = "  bb.name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "  bb.org_code orgCode  ";


			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(bb.org_code,'_',bb.name)  orgCode  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(bb.name,'_',bb.org_code)  orgCode ";
			

			}
			String getUserDetailsQuery = "select if(aa.organization_type = 'RETAILER' , (select user_mapping_id from st_lms_user_random_id_mapping where user_id = aa.user_id) , 0) user_mapping_id, aa.user_id, aa.organization_id,aa.isrolehead,cc.is_master, aa.role_id,cc.tier_id, aa.user_name, aa.password, aa.status, aa.organization_type,bb.organization_status , aa.auto_password, aa.login_attempts"
					+ ", bb.name,bb.org_code,"+orgCodeQry+" , bb.organization_status, bb.current_credit_amt, bb.available_credit, bb.claimable_bal, bb.unclaimable_bal, bb.parent_id, bb.pwt_scrap, cc.role_name,if(login_attempts<"
					+ loginLimit
					+ ",'ALLOW','BLOCK') logginAttempt ,bb.tp_organization istp "
					+ "from st_lms_user_master aa, st_lms_organization_master bb, st_lms_role_master cc "
					+ "where aa.organization_id = bb.organization_id	and aa.user_name =? and aa.role_id = cc.role_id";

			pstmt = con.prepareStatement(getUserDetailsQuery);
			pstmt.setString(1, username.trim());

			rs = pstmt.executeQuery();
			logger.debug(pstmt);

			// check More Then One Users Exist in the Database or not with Same
			// user_name
			if (rs.getFetchSize() > 1) {
				logger
						.debug("More Then One User Exist in the Database with Same  user_name"
								+ username);
				loginBean.setStatus("ERROR");
				return loginBean;
			}

			// getting the user details from database
			if (rs.next()) {
				dbPass = rs.getString(TableConstants.USER_PASSWORD);
				role = rs.getString(TableConstants.ORG_TYPE);// This is tier
				// Code
				tierId = rs.getInt(TableConstants.TIER_ID);
				roleId = rs.getInt(TableConstants.ROLE_ID);
				status = rs.getString(TableConstants.USER_STATUS);
				unam = rs.getString(TableConstants.USER_NAME);
				uid = rs.getInt(TableConstants.USER_ID);
				orgId = rs.getString(TableConstants.ORG_ID);
				userMapid =  rs.getInt(TableConstants.MAPPING_ID);
				orgName = rs.getString(TableConstants.ORGANIZATION_NAME);
				roleName = rs.getString(TableConstants.ROLE_NAME);
				autoGenerate = rs.getInt("auto_password");
				parentOrgId = rs.getInt("parent_id");
				orgStatus = rs.getString("organization_status");

				userInfo = new UserInfoBean();
				userInfo.setRoleName(rs.getString(TableConstants.ROLE_NAME));
				userInfo.setRoleId(roleId);
				userInfo.setUserId(uid);
				userInfo.setUserName(username);
				userInfo.setUserOrgId(rs.getInt(TableConstants.ORG_ID));
				userInfo.setUserType(role);
				userInfo.setTierId(tierId);
				userInfo.setOrgName(orgName);
				userInfo.setOrgCode(rs.getString("orgCode"));
				userInfo.setUserOrgCode(rs.getString("org_code"));
				userInfo.setAvailableCreditLimit(rs
						.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
				userInfo.setClaimableBal(rs.getDouble("claimable_bal"));
				userInfo.setUnclaimableBal(rs.getDouble("unclaimable_bal"));
				userInfo.setCurrentCreditAmt(rs
						.getDouble(TableConstants.CURRENT_CREDIT_AMT));
				userInfo.setStatus(status);
				userInfo.setOrgStatus(rs.getString(TableConstants.ORG_STATUS));
				userInfo.setPwtSacrap(rs.getString("pwt_scrap"));
				userInfo.setParentOrgId(parentOrgId);
				userInfo.setCurrentUserMappingId(userMapid);
				userInfo.setIsMasterRole(rs.getString("is_master"));
				userInfo.setIsRoleHeadUser(rs.getString("isrolehead"));
				userInfo.setTPUser("YES".equalsIgnoreCase(rs.getString("istp")));
				userInfo.setLoginChannel(interface_type);
			}
			// check the user's info before login
			else {
				loginBean.setStatus("USER_NAME_NOT_MATCH");
				return loginBean;
			}

			if ("BLOCK".equals(rs.getString("logginAttempt"))) {
				loginBean.setStatus("LOGIN_LIMIT_REACHED");
				return loginBean;
			}
			
			//Here matching Time of Login			
			if(!getTimeLimitStatus(uid,con))
			{
				logger.info("TIME LIMIT STATUS - Time Limit is exceeded for this user");
				loginBean.setStatus("ERROR_TIME_LIMIT");
				return loginBean;
			}

			String reason = null;
			con.setAutoCommit(false);
			// Matching Password
			if (MD5Encoder.encode(password).equals(dbPass) || userInfo.isTPUser()) {
				// checking the user status
				if (status.equals("BLOCK") || status.equals("TERMINATE")
						|| orgStatus.equals("TERMINATE") || orgStatus.equals("BLOCK")) {
					
					if("TERMINATE".equals(orgStatus)){
						
						loginBean.setStatus("TERMINAT_WRAPPER");
						return loginBean;
					}
					
					loginBean.setStatus("ERRORINACTIVE");
					return loginBean;
				}

	/*			if ("INACTIVE".equalsIgnoreCase(orgStatus)) {
					PreparedStatement pstmt2 = con
							.prepareStatement("select reason from st_lms_organization_master_history where organization_status='INACTIVE' and organization_id=? order by date_changed desc limit 1");
					pstmt2.setString(1, orgId);
					ResultSet rs2 = pstmt2.executeQuery();

					if (rs2.next()) {
						reason = rs2.getString("reason");
					}

					//if (!"INACTIVE_AUTO_ACT".equalsIgnoreCase(reason)) {
						if("INACTIVE_MANUAL_BO".equalsIgnoreCase(reason)){
						loginBean.setStatus("ERRORINACTIVE");
						return loginBean;
						//}
					}
				}
               //update login attempts and last_login_date together and where condition user_id is used in stead of user_name
				pstmt = con
						.prepareStatement("update st_lms_user_master set login_attempts = 0,last_login_date=? where user_id = ?");
				pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
				pstmt.setInt(2, uid);
				pstmt.executeUpdate();

				
				
				rs.close();
				pstmt.close();
				LinkedHashMap actionServiceMap = new LinkedHashMap();
				if(!"TERMINAL".equals(interface_type)){
				
				PriviledgeBean privBean = null;
				// String getService = "select
				// srm.id,role_id,service_display_name,ref_merchant_id,privilege_rep_table,menu_master_table,service_deligate_url
				// from st_lms_service_role_mapping srm,st_lms_service_master sm
				// where srm.status='ACTIVE' and srm.role_id=? and
				// sm.status='ACTIVE' and srm.service_id=sm.service_id and
				// srm.interface='WEB'";
				String getService = "select srm.id,role_id,interface,service_display_name,service_code,ref_merchant_id,priv_rep_table,menu_master_table,service_deligate_url from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id=? and organization_id=? and srm.status='ACTIVE' and sm.status='ACTIVE' and sdm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id and sdm.interface=?";
				String getPrivId = "select distinct(upm.priv_id) from st_lms_role_priv_mapping rpm,st_lms_user_priv_mapping upm where upm.user_id=? and upm.role_id=? and rpm.status='ACTIVE' and upm.status='ACTIVE'and upm.role_id=rpm.role_id and upm.service_mapping_id=?";
				String getAction = null;
				String getMenuTitle = null;
				pstmt = con.prepareStatement(getService);
				pstmt.setInt(1, roleId);
				pstmt.setInt(2, Integer.parseInt(orgId));
				pstmt.setString(3, interface_type);
				logger.debug(pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ArrayList<PriviledgeBean> privList = new ArrayList<PriviledgeBean>();
					getAction = "select distinct(action_mapping) from "
							+ rs.getString("priv_rep_table")
							+ " pr,("
							+ getPrivId
							+ ") result where pr.priv_id=result.priv_id and pr.status='ACTIVE'";
					getMenuTitle = "select menu_disp_name,item_order,related_to,action_mapping from "
							+ rs.getString("menu_master_table")
							+ " smm,"
							+ rs.getString("priv_rep_table")
							+ " pr,("
							+ getPrivId
							+ ") result where  pr.priv_id=result.priv_id and pr.status='ACTIVE' and smm.action_id=pr.action_id order by related_to,item_order,menu_disp_name";
					pstmtPriv = con.prepareStatement(getAction);
					pstmtPriv.setInt(1, uid);
					pstmtPriv.setInt(2, roleId);
					pstmtPriv.setInt(3, rs.getInt("id"));
					// logger.debug(pstmtPriv);
					rsPriv = pstmtPriv.executeQuery();
					while (rsPriv.next()) {
						userActionList.add(rsPriv.getString("action_mapping"));
					}
					pstmtPriv1 = con.prepareStatement(getMenuTitle);
					pstmtPriv1.setInt(1, uid);
					pstmtPriv1.setInt(2, roleId);
					pstmtPriv1.setInt(3, rs.getInt("id"));
					logger.debug(pstmtPriv1);
					rsPriv = pstmtPriv1.executeQuery();
					while (rsPriv.next()) {
						privBean = new PriviledgeBean();
						privBean.setPrivTitle(rsPriv
								.getString("menu_disp_name"));
						privBean.setActionMapping(rsPriv
								.getString("action_mapping"));
						privBean.setRelatedTo(rsPriv.getString("related_to"));
						privList.add(privBean);
					}
					if (privList.size() > 0) {
						actionServiceMap.put(rs
								.getString("service_display_name")
								+ "-" + rs.getString("service_code"), privList);
					}
				}
				
				}
				loginBean.setActionServiceMap(actionServiceMap);
				
				loginBean.setUserActionList(userActionList);
				loginBean.setUserInfo(userInfo);
				String insertLoginDate = QueryManager.insertST3LoginDate()
						+ " where user_name='" + username + "'";
				pstmt = con.prepareStatement(insertLoginDate);
				pstmt.setTimestamp(1, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.executeUpdate();

				if ("AGENT".equalsIgnoreCase(userInfo.getUserType())
						|| "RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
					PreparedStatement ps = con
							.prepareStatement("select name,org_code from st_lms_organization_master where organization_id = ?");
					ps.setInt(1, parentOrgId);
					rs = ps.executeQuery();

					if (rs.next()) {
						parentOrgName = rs.getString("name");
						parentOrgCode=rs.getString("org_code");
					}

					userInfo.setParentOrgName(parentOrgName);
					userInfo.setParentOrgCode(parentOrgCode);
				     
					// added to reflect web users last login in ret offline master 
					if("RETAILER".equalsIgnoreCase(userInfo.getUserType()) && interface_type.equalsIgnoreCase("WEB")){
						
						
						String updateRetOfflineQuery = "update st_lms_ret_offline_master set login_status=?,last_login_time=?,last_HBT_time=? where user_id = ?";
						pstmt = con.prepareStatement(updateRetOfflineQuery);
						pstmt.setString(1,"LOGIN");
						pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
						pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
						pstmt.setInt(4, userInfo.getUserId());
						logger.info("updateRetOfflineQuery"+pstmt);
						int update=pstmt.executeUpdate();
						logger.info("updated updateRetOffline "+update);
					}
						
				}
				
				

				if (autoGenerate == 1) {
					loginBean.setStatus("FirstTime");
					con.commit();
					return loginBean;
				}

				if (!loggedInUser(username)) {

				}
			} else {
				loginBean.setStatus("PASS_NOT_MATCH");
				pstmt = con
						.prepareStatement("update st_lms_user_master set login_attempts = login_attempts+1 where user_name = ?");
				pstmt.setString(1, username);
				pstmt.executeUpdate();
				con.commit();
				return loginBean;
				
			}
			con.commit();
			
			double userBal = userInfo.getAvailableCreditLimit()- userInfo.getClaimableBal();
			if (userBal < 0 && !"BO".equals(userInfo.getUserType())) {
				if (!"INACTIVE_AUTO_ACT".equalsIgnoreCase(reason)) {
					loginBean.setStatus("BALANCE_NOT_POSITIVE");
					return loginBean;
				} else {
					loginBean.setStatus("success");
				}
				;
				return loginBean;
			}
		} catch (SQLException e) {
			logger.error("SQl Error: " + e);
			//	e.printStackTrace();

		} catch (Exception e) {
			logger.error(" Error: " + e);
			//e.printStackTrace();

		}finally {
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmtPriv != null) {
					pstmtPriv.close();
				}
				if (pstmtPriv1 != null) {
					pstmtPriv1.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (rsPriv != null) {
					rsPriv .close();
				}
			} catch (SQLException e) {
				logger.error("SQl Error: " + e);
				e.printStackTrace();
			}
		}*/
		//loginBean.setStatus("success");
		return loginBean;
	}
/**This Method validate terminal id for user 
 * @deprecated Use validateTerminalId(int userId, String terminalId,String deviceType,Connection con)
 * @param userId 
 * @param terminalId
 * @param deviceType
 * @return true/false 
 */
	public boolean validateTerminalId(int userId, String terminalId,String deviceType) {
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select serial_number from st_lms_ret_offline_master where user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			System.out.println("Validate Terminal Query "+ps+" deviceType: "+deviceType);	
			String dbTerminalId = "";

			if (rs.next()) {
				dbTerminalId = rs.getString("serial_number");
			}
		
			if(deviceType.equalsIgnoreCase("TPS") || "TPS300".equals(deviceType) || "TPS800".equals(deviceType)){
				if(dbTerminalId.length()>15){
					dbTerminalId = dbTerminalId.substring(
							dbTerminalId.length() - 15, dbTerminalId.length());
							}
			}
			else{
				if (dbTerminalId.length() > 8) {
					dbTerminalId = dbTerminalId.substring(
							dbTerminalId.length() - 8, dbTerminalId.length());
				}
			}
			

			if (terminalId.equalsIgnoreCase(dbTerminalId)) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean checkTerminalId(String terminalId,Connection con) throws LMSException {
		PreparedStatement ps =null;
		ResultSet rs =null;
		try {
			
			ps = con
					.prepareStatement("select current_owner_type from st_lms_inv_status where serial_no like ? and current_owner_type != 'REMOVED'");
			ps.setString(1,"%"+terminalId);
			logger.info("checkTerminalId Query:"+ps);
			rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			logger.error("SQL Exception",e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(" Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			try {
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("SQL Exception",e);
			}
		}
		return false;
	}
	
	
	public boolean validateUser(String userName, String password) {
		Connection con = DBConnect.getConnection();
		boolean bRet = false;
		try {
			System.out.println("******" + userName + "******" + password);
			PreparedStatement pstmt = con
					.prepareStatement("select user_name from st_lms_user_master where user_name = ? and password = ?");
			pstmt.setString(1, userName);
			pstmt.setString(2, MD5Encoder.encode(password));
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bRet = true;
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return bRet;
	}

	public static void resetAll() throws LMSException {
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = null;
			pstmt = con
					.prepareStatement("update st_lms_user_master set login_attempts = 0");
			pstmt.executeUpdate();
		}catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException("SQL Exception "+e.getMessage());
		}catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		} finally {
			DBConnect.closeCon(con);
		}
	}

/**
 * @deprecated Use newTerminalInfo(String deviceType, String profile,
				boolean isCs, boolean isDownload, double version, int userId,Connection con)	
 * @param deviceType
 * @param profile
 * @param isCs
 * @param isDownload
 * @param version
 * @param userId
 * @return
 */
	public static String newTerminalInfo(String deviceType, String profile,
			boolean isCs, boolean isDownload, double version, int userId) {
		StringBuilder returnData = new StringBuilder("");
		String expVersion = null;
		Connection con = DBConnect.getConnection();
		try {
			boolean isKenya = "Kenya".equalsIgnoreCase((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED"));
			Statement st = con.createStatement();
			ResultSet innerRS = st.executeQuery("select expected_version from st_lms_ret_offline_master where user_id = " + userId);
			if(innerRS.next()){
				expVersion = innerRS.getString("expected_version");
			}
			PreparedStatement pstmt = con
					.prepareStatement("SELECT item_name,isMandatory,fileSize,fileSize_adf FROM st_lms_htpos_download hd,  st_lms_htpos_device_master dm WHERE status='ACTIVE' AND hd.device_id = dm.device_id AND device_type=? AND profile=? AND version=? LIMIT 4");
			pstmt.setString(1, deviceType);
			pstmt.setString(2, profile);
			pstmt.setString(3, ""+expVersion);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				if(isDownload){
					returnData.append(rs.getString("item_name") + ":"
							+ expVersion + ","
							+ rs.getString("isMandatory") + ","
							+ rs.getString("fileSize_adf")+","
							+ rs.getString("fileSize") +
					"|");
				} else{
					returnData.append(rs.getString("item_name") + ":"
							+ version + ","
							+ "NO" + ","
							+ -1 +","+ 0 + "|");
				}
				
				if (!isCs)
					break;
				
				if(!isKenya)
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return returnData.toString();
	}

	
/**
 * @deprecated Use terminalInfo(String deviceType, String profile,
	            boolean isCs, boolean isDownload, double version, int userId,Connection con)	
 * @param deviceType
 * @param profile
 * @param isCs
 * @param isDownload
 * @param version
 * @param userId
 * @return
 */
	
	public static String terminalInfo(String deviceType, String profile,
            boolean isCs, boolean isDownload, double version, int userId) {
        StringBuilder returnData = new StringBuilder("");
        String expVersion = null;
        Connection con = DBConnect.getConnection();
        try {
            boolean isKenya = "Kenya".equalsIgnoreCase((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED"));
            Statement st = con.createStatement();
            ResultSet innerRS = st.executeQuery("select expected_version from st_lms_ret_offline_master where user_id = " + userId);
            if(innerRS.next()){
                expVersion = innerRS.getString("expected_version");
            }
            PreparedStatement pstmt = con
                    .prepareStatement("SELECT item_name,isMandatory,fileSize FROM st_lms_htpos_download hd,  st_lms_htpos_device_master dm WHERE status='ACTIVE' AND hd.device_id = dm.device_id AND device_type=? AND profile=? AND version=? LIMIT 4");
            pstmt.setString(1, deviceType);
            pstmt.setString(2, profile);
            pstmt.setString(3, ""+expVersion);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                if(isDownload){
                    returnData.append(rs.getString("item_name") + ":"
                            + expVersion + ","
                            + rs.getString("isMandatory") + ","
                            + rs.getString("fileSize") + "|");
                } else{
                    returnData.append(rs.getString("item_name") + ":"
                            + version + ","
                            + "NO" + ","
                            + 0 + "|");
                }
                
                if (!isCs)
                    break;
                
                if(!isKenya)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnect.closeCon(con);
        }

        return returnData.toString();
    }
/**
 * @deprecated Use terminalInfoForLessVersion(String deviceType, String profile, boolean isDownload, double version, int userId,Connection con)
 * @param deviceType
 * @param profile
 * @param isDownload
 * @param version
 * @param userId
 * @return
 */
	public static String terminalInfoForLessVersion(String deviceType, String profile, boolean isDownload, double version, int userId) {
		StringBuilder returnData = new StringBuilder("");
		String expVersion = null;
		if(isDownload){
			Connection con = DBConnect.getConnection();
			try {
			Statement st = con.createStatement();
			ResultSet innerRS = st.executeQuery("select expected_version from st_lms_ret_offline_master where user_id = " + userId);
			if(innerRS.next()){
				expVersion = innerRS.getString("expected_version");
			}
			
				PreparedStatement pstmt = con.prepareStatement("SELECT item_name,isMandatory,fileSize FROM st_lms_htpos_download hd,  st_lms_htpos_device_master dm WHERE status='ACTIVE' AND hd.device_id = dm.device_id AND device_type=? AND profile=? AND item_name=? AND version=?");
				pstmt.setString(1, deviceType);
				pstmt.setString(2, profile);
				pstmt.setString(3, "SApp");
				pstmt.setString(4, ""+expVersion);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					returnData.append("version:" + expVersion
							+ "|is mandatory:" + rs.getString("isMandatory") + "|fSize:" + rs.getString("fileSize") + "|");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBConnect.closeCon(con);
			}
		} else {
			returnData.append("version:" + version
					+ "|is mandatory:" + "NO" + "|fSize:" + 0 + "|");
		}
		return returnData.toString();
	}
	
	public boolean isValidMacId(int userId, String macId) {
		if (macId == null) {
			return false;
		}
		macId = macId.trim();
		String terminalId = null;

		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select serial_number from st_lms_ret_offline_master where user_id=?");
			pstmt.setInt(1, userId);
			System.out.println("pstmt:" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				terminalId = rs.getString("serial_number");
			}

			if (terminalId == null) {
				System.out.println("return false");
				return false;
			}
			
			System.out.println("macId:" + macId);
			System.out.println("terminalId:" + terminalId + "*****");

			if (macId.equalsIgnoreCase(terminalId.trim())) {
				System.out.println("return true");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return false;
	}
/**
 * @deprecated Use chkDownloadAvailable(int userId,Connection con)	
 * @param userId
 * @return
 */
	public static boolean chkDownloadAvailable(int userId){
		boolean flag = false;
		String qry = "select current_version, expected_version, is_download_available from st_lms_ret_offline_master where user_id = " + userId;
		Connection con = DBConnect.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(qry);
			while(rs.next()){
				flag = ("YES".equalsIgnoreCase(rs.getString("is_download_available")) 
							&& !rs.getString("current_version").equalsIgnoreCase(rs.getString("expected_version")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}
		return flag;
	}
/**
 * @deprecated	 Use updateDownloadDetails(int userId, double version,Connection con)
 * @param userId
 * @param version
 */
	public static void updateDownloadDetails(int userId, double version){
		
		String selectQry="select is_download_available from st_lms_ret_offline_master where user_id = "+userId+" and expected_version = "+version;
	
	    String qry = "update st_lms_ret_offline_master set is_download_available = 'NO', downloaded_on = '" 
		              + (new Timestamp(new Date().getTime()).toString()).split("\\.")[0] + "' where user_id = " + userId + " and" 
		              + " expected_version = " + version ; 
	    Connection con = DBConnect.getConnection();
	   try {
		    Statement st = con.createStatement();
		
		    ResultSet rs=st.executeQuery(selectQry);
		   if(rs.next()){
			             if("YES".equalsIgnoreCase(rs.getString("is_download_available"))){
				                        int i = st.executeUpdate(qry);
				                        if(i > 0){
				                                  System.out.println("Updated download details for userId " + userId);
				                         }
			              }
		  }
								
            /*			if(i > 0){
			            System.out.println("Updated download details for userId " + userId);
		                }
		    */
	       } catch (SQLException e) {
		     e.printStackTrace();
	       } finally{
		     DBConnect.closeCon(con);
	       }
	   }
	
	public static void updateLoginStatus(int userId, String status){
		Connection con = DBConnect.getConnection();
		//PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);
			updateLoginStatus( userId, status,con);
			con.commit();
		/*pstmt = con.prepareStatement("update st_lms_ret_offline_master set login_status=?,last_login_time=? where user_id=?");
		pstmt.setString(1, status);
		pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
		pstmt.setInt(3, userId);
		pstmt.executeUpdate();*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}		
	}
	
	public boolean getTimeLimitStatus(int userId,Connection con) {

		//Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet set = null;

		Time nowTime = null;
		Time startTime = null;
		Time endTime = null;
		String status = null;

		String[] days = new String[]{"sunday", "monday", "tuesday", "wednesday", "thursday", "friday","saturday"};
		Calendar calendar = Calendar.getInstance();
		String day = days[calendar.get(Calendar.DAY_OF_WEEK)-1];

		try
		{
			//con = DBConnect.getConnection();
			nowTime = new Time(new SimpleDateFormat("HH:mm:ss").parse(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":00").getTime());

			pstmt = con.prepareStatement("SELECT "+day+"_start_time as startTime, "+day+"_end_time as endTime, status FROM st_lms_user_ip_time_mapping WHERE user_id=?;");
			pstmt.setInt(1, userId);
			set = pstmt.executeQuery();
			if(set.next())
			{
				status = set.getString("status");
				startTime = set.getTime("startTime");
				endTime = set.getTime("endTime");
			}
			else
				return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			//DBConnect.closeCon(con);
		}

		if("INACTIVE".equals(status))
			return true;
		if(nowTime.getTime()>=startTime.getTime() && nowTime.getTime()<=endTime.getTime())
			return true;
			
		return false;
	}
	
	/**This Method validate terminal id for user 
	 * @param userId 
	 * @param terminalId
	 * @param deviceType
	 * @param con 
	 * @return true/false 
	 */
		public boolean validateTerminalId(String dbTerminalId,String terminalId,String deviceType, int binding_length) {
			//PreparedStatement ps =null;
			//ResultSet rs =null;
			try {
				//ps= con.prepareStatement("select serial_number from st_lms_ret_offline_master where user_id = ?");
				//ps.setInt(1, userId);
				//rs= ps.executeQuery();
				//logger.info("Validate Terminal Query "+ps+" deviceType: "+deviceType);	
				//String dbTerminalId = "";

				//if (rs.next()) {
				//	dbTerminalId = rs.getString("serial_number");
				////}
			
				//if(deviceType.equalsIgnoreCase("TPS") || "TPS300".equals(deviceType) || "TPS800".equals(deviceType)){
					//if(dbTerminalId.length()>15){
						dbTerminalId = dbTerminalId.substring(dbTerminalId.length() - binding_length, dbTerminalId.length());
				/*}
				}else{
					if (dbTerminalId.length() > 8) {
						dbTerminalId = dbTerminalId.substring(
								dbTerminalId.length() - 8, dbTerminalId.length());
					}*/
						
				if (terminalId.equalsIgnoreCase(dbTerminalId)) {
					return true;
				}
			//} catch (SQLException e) {
				//logger.error("SQL Exception ", e);
			}catch(Exception e){
				logger.error("Exception ",e);
			} 
			//finally {
			//	DBConnect.closeRs(rs);
			//	DBConnect.closePstmt(ps);
			//}
			return false;
		}	
		public static void updateDownloadDetails(int userId, double version,double expectedVersion,String isDownloadAvailable,Connection con) throws LMSException {
			
			//String selectQry="select is_download_available from st_lms_ret_offline_master where user_id = "+userId+" and expected_version = "+version;
		
		   /* String qry = "update st_lms_ret_offline_master set is_download_available = 'NO', downloaded_on = '" 
			              + (new Timestamp(new Date().getTime()).toString()).split("\\.")[0] + "' where user_id = " + userId + " and" 
			              + " expected_version = " + version ; */
		    PreparedStatement pstmt=null;
		    //ResultSet rs=null;
		  
		   try {
			   	//st = con.createStatement();
			   // logger.info("Update Download Details selectQry"+selectQry);
			  //  logger.info("Update Download Details updateQry"+qry);
			   // rs=st.executeQuery(selectQry);
			    if("YES".equalsIgnoreCase(isDownloadAvailable) && version == expectedVersion){
				             //if("YES".equalsIgnoreCase(isDownloadAvailable)){
				            	 pstmt=con.prepareStatement("update st_lms_ret_offline_master set is_download_available = 'NO', downloaded_on = ? where user_id = ? and  expected_version = ?");
				            	 pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
				            	 pstmt.setInt(2, userId);
				            	 pstmt.setDouble(3, version);
				            	// st = con.createStatement();
					                        int i = pstmt.executeUpdate();
					                        if(i > 0){
					                                  System.out.println("Updated download details for userId " + userId);
					                         }
				              //}
			   			}
									
	            /*			if(i > 0){
				            System.out.println("Updated download details for userId " + userId);
			                }
			    */
			   con.commit();
		       } catch (SQLException e) {
			     logger.error("SQL Exception "+e);
			     throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		       }catch(Exception e){
		    	   logger.error(" Exception "+e);
		    	   throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		       } finally{
		    	  // DBConnect.closeRs(rs);
		    	   DBConnect.closePstmt(pstmt);
		       }
		   }
		
		
		public static boolean chkDownloadAvailable(int userId,Connection con){
			boolean flag = false;
			String qry = "select current_version, expected_version, is_download_available from st_lms_ret_offline_master where user_id = " + userId;
			Statement st=null;
			ResultSet rs =null;
			try {
				 st = con.createStatement();
				 rs = st.executeQuery(qry);
				while(rs.next()){
					flag = ("YES".equalsIgnoreCase(rs.getString("is_download_available")) 
								&& !rs.getString("current_version").equalsIgnoreCase(rs.getString("expected_version")));
				}
			} catch (SQLException e) {
				logger.error("SQL Exception ",e);
			}catch(Exception e){
				logger.error("Exception ",e);
			} finally{
				DBConnect.closeRs(rs);
				DBConnect.closeStmt(st);
			}
			return flag;
		}
		
		public static String terminalInfo(String deviceType, String profile,
	            boolean isCs, boolean isDownload, double version, int userId,Connection con) {
	        StringBuilder returnData = new StringBuilder("");
	        String expVersion = null;
	      
	        try {
	            boolean isKenya = "KENYA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"));
	            Statement st = con.createStatement();
	            ResultSet innerRS = st.executeQuery("select expected_version from st_lms_ret_offline_master where user_id = " + userId);
	            if(innerRS.next()){
	                expVersion = innerRS.getString("expected_version");
	            }
	            PreparedStatement pstmt = con
	                    .prepareStatement("SELECT item_name,isMandatory,fileSize FROM st_lms_htpos_download hd,  st_lms_htpos_device_master dm WHERE status='ACTIVE' AND hd.device_id = dm.device_id AND device_type=? AND profile=? AND version=? LIMIT 4");
	            pstmt.setString(1, deviceType);
	            pstmt.setString(2, profile);
	            pstmt.setString(3, ""+expVersion);
	            ResultSet rs = pstmt.executeQuery();
	            
	            while (rs.next()) {
	                if(isDownload){
	                    returnData.append(rs.getString("item_name") + ":"
	                            + expVersion + ","
	                            + rs.getString("isMandatory") + ","
	                            + rs.getString("fileSize") + "|");
	                } else{
	                    returnData.append(rs.getString("item_name") + ":"
	                            + version + ","
	                            + "NO" + ","
	                            + 0 + "|");
	                }
	                
	                if (!isCs)
	                    break;
	                
	                if(!isKenya)
	                    break;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            //DBConnect.closeCon(con);
	        }

	        return returnData.toString();
	    }
		public static String newTerminalInfo(String deviceType, String profile,
				boolean isCs, boolean isDownload, double version,String expectedVersion, int userId,Connection con) throws LMSException {
			StringBuilder returnData = new StringBuilder("");
			//String expVersion = null;
			//Statement st =null;
			ResultSet innerRS =null;
			PreparedStatement pstmt =null;
			ResultSet rs =null;
		//	Connection con = DBConnect.getConnection();
			try {
				boolean isKenya = "KENYA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"));
				/*st = con.createStatement();
				innerRS = st.executeQuery("select expected_version from st_lms_ret_offline_master where user_id = " + userId);
				if(innerRS.next()){
					expVersion = innerRS.getString("expected_version");
				}*/
				pstmt = con.prepareStatement("SELECT item_name,isMandatory,fileSize,fileSize_adf FROM st_lms_htpos_download hd,  st_lms_htpos_device_master dm WHERE status='ACTIVE' AND hd.device_id = dm.device_id AND device_type=? AND profile=? AND version=? LIMIT 4");
				pstmt.setString(1, deviceType);
				pstmt.setString(2, profile);
				pstmt.setString(3, ""+expectedVersion);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					if(isDownload){
						returnData.append(rs.getString("item_name") + ":"
								+ expectedVersion + ","
								+ rs.getString("isMandatory") + ","
								+ rs.getString("fileSize_adf")+","
								+ rs.getString("fileSize") +
						"|");
					} else{
						returnData.append(rs.getString("item_name") + ":"
								+ version + ","
								+ "NO" + ","
								+ -1 +","+ 0 + "|");
					}
					
					if (!isCs)
						break;
					
					if(!isKenya)
						break;
				}
			} catch (SQLException e) {
			     logger.error("SQL Exception "+e);
			     throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		    }catch(Exception e){
		    	   logger.error(" Exception "+e);
		    	   throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		    } finally{
				DBConnect.closeRs(rs);
				DBConnect.closeRs(innerRS);
				//DBConnect.closeStmt(st);
				DBConnect.closePstmt(pstmt);
			}

			return returnData.toString();
		}	
		public static String terminalInfoForLessVersion(String deviceType, String profile, boolean isDownload, double version, int userId,Connection con) {
			StringBuilder returnData = new StringBuilder("");
			String expVersion = null;
			if(isDownload){
				//Connection con = DBConnect.getConnection();
				try {
				Statement st = con.createStatement();
				ResultSet innerRS = st.executeQuery("select expected_version from st_lms_ret_offline_master where user_id = " + userId);
				if(innerRS.next()){
					expVersion = innerRS.getString("expected_version");
				}
				
					PreparedStatement pstmt = con.prepareStatement("SELECT item_name,isMandatory,fileSize FROM st_lms_htpos_download hd,  st_lms_htpos_device_master dm WHERE status='ACTIVE' AND hd.device_id = dm.device_id AND device_type=? AND profile=? AND item_name=? AND version=?");
					pstmt.setString(1, deviceType);
					pstmt.setString(2, profile);
					pstmt.setString(3, "SApp");
					pstmt.setString(4, ""+expVersion);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						returnData.append("version:" + expVersion
								+ "|is mandatory:" + rs.getString("isMandatory") + "|fSize:" + rs.getString("fileSize") + "|");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					//DBConnect.closeCon(con);
				}
			} else {
				returnData.append("version:" + version
						+ "|is mandatory:" + "NO" + "|fSize:" + 0 + "|");
			}
			return returnData.toString();
		}
		
		public static void updateLoginStatus(int userId, String status,Connection con) throws LMSException{
			//Connection con = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			try {
			pstmt = con.prepareStatement("update st_lms_ret_offline_master set login_status=?,last_login_time=? where user_id=?");
			pstmt.setString(1, status);
			pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
			pstmt.setInt(3, userId);
			pstmt.executeUpdate();
			// con.commit();
			}catch (Exception e) {	
				logger.error("Exception ",e);
				 //e.printStackTrace();
			
				try {
					con.rollback();
				} catch (SQLException er) {
					logger.error("SQLException ",er);
					//er.printStackTrace();
				}
				throw new LMSException("SQL_ERROR");
			}finally{
				
				DBConnect.closePstmt(pstmt);
				
			}		
		}
		
		public static void updateUserSession(String userName , String userSession , Connection con) throws LMSException{
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				logger.info(stmt.executeUpdate("update st_lms_user_master set user_session = '"+userSession+"' where user_name = '"+userName+"'"));
			}catch (Exception e) {	
				e.printStackTrace();
				throw new LMSException("SQL_ERROR");
			}finally{
				DBConnect.closeStmt(stmt);
			}		
		}
		
}