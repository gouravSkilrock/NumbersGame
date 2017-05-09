package com.skilrock.lms.coreEngine.loginMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.MD5Encoder;

public class AuthenticationHelperforBOMaster extends LocalizedTextUtil{
	static Log logger = LogFactory
			.getLog(AuthenticationHelperforBOMaster.class);
	

	public static String getOrgUserList() {
		StringBuilder user = new StringBuilder();
		Locale locale = new Locale(CommonMethods.getSelectedLocale());
		user
				.append("<select name=\"username\" class=\"option\" id=\"username\" style=\"width:100px;\" onchange=\"confirmLogin()\"><option class=\"option\" value=\"-1\">"+findDefaultText("label.login.as", locale)+"</option>");

		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select if(organization_type='AGENT','A','B') organization_type,user_name from st_lms_user_master where organization_type!='RETAILER' and isrolehead='Y' and status !='TERMINATE' order by organization_type desc,user_name");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user
						.append("<option class=\"option\" value=\""
								+ rs.getString("user_name")
								+ "\">"
								+ rs.getString("organization_type")
										.toUpperCase() + "-"
								+ rs.getString("user_name").toUpperCase()
								+ "</option>");
			}
			user.append("</select>");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user.toString();
	}

	private Connection con;
	boolean loggedInResult = false;

	LoginBean loginBean = new LoginBean();

	private PreparedStatement pstmt;
	private PreparedStatement pstmtPriv;
	private ResultSet rs;

	private ResultSet rsPriv;

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

	public LoginBean loginAuthentication(String username, String interface_type,String sessionId) {
		try {
			con = DBConnect.getConnection();

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
			String orgName = "";
			String orgId = "";
			int parentOrgId = 0;
			int tierId = 0;
			String parentOrgName = "";
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
			String getUserDetailsQuery = "select aa.user_id, aa.organization_id,aa.isrolehead,cc.is_master, aa.role_id,cc.tier_id, aa.user_name, aa.password, aa.status, aa.organization_type,bb.organization_status , aa.auto_password"
					+ ", bb.name,"+orgCodeQry+", bb.organization_status, bb.current_credit_amt, bb.available_credit, bb.claimable_bal, bb.unclaimable_bal, bb.parent_id, bb.pwt_scrap, cc.role_name "
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
				userInfo.setUserName(username);
				userInfo.setUserOrgId(rs.getInt(TableConstants.ORG_ID));
				userInfo.setUserType(role);
				userInfo.setTierId(tierId);
				userInfo.setOrgName(orgName);
				userInfo.setOrgCode(rs.getString("orgCode"));
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
				userInfo.setLoginChannel(interface_type);
			}
			// check the user's info before login
			else {
				loginBean.setStatus("USER_NAME_NOT_MATCH");
				return loginBean;
			}

			// Matching Password

			// checking the user status
			if (status.equals("TERMINATE") || orgStatus.equals("TERMINATE")) {
				loginBean.setStatus("ERRORINACTIVE");
				return loginBean;
			}
			
			//String reason = null;
			
		/*	if("INACTIVE".equalsIgnoreCase(orgStatus)){
				PreparedStatement pstmt2 = con.prepareStatement("select reason from st_lms_organization_master_history where organization_status='INACTIVE' and organization_id=? order by date_changed limit 1");
				pstmt2.setString(1, orgId);
				ResultSet rs2 = pstmt2.executeQuery();
				
				if(rs2.next()){
					reason = rs2.getString("reason");
				}
				
				if("INACTIVE_MANUAL_BO".equalsIgnoreCase(reason)){
					loginBean.setStatus("ERRORINACTIVE");
					return loginBean;
				}
			}*/

			rs.close();
			pstmt.close();

			LinkedHashMap actionServiceMap = new LinkedHashMap();
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
			String requestedLocale = CommonMethods.getSelectedLocale();
			
			String menuColumnName = "menu_disp_name";
			if("en".equalsIgnoreCase(requestedLocale) || "fr".equalsIgnoreCase(requestedLocale)){
					menuColumnName = "menu_disp_name_"+requestedLocale;
			}
			while (rs.next()) {
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
				pstmtPriv = con.prepareStatement(getMenuTitle);
				pstmtPriv.setInt(1, uid);
				pstmtPriv.setInt(2, roleId);
				pstmtPriv.setInt(3, rs.getInt("id"));
				logger.debug(pstmtPriv);
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
					actionServiceMap.put(rs.getString("service_display_name")
							+ "-" + rs.getString("service_code"), privList);
				}
			}
			if(ServicesBean.isSLE() && ("BO".equalsIgnoreCase(userInfo.getUserType()) || "RETAILER".equalsIgnoreCase(userInfo.getUserType()))) {
				actionServiceMap.put("Sports Lottery-sle", new ArrayList<PriviledgeBean>());
			}
			loginBean.setActionServiceMap(actionServiceMap);
			loginBean.setUserInfo(userInfo);
			loginBean.setUserActionList(userActionList);

			String insertLoginDate = QueryManager.insertST3LoginDate()
					+ " where user_name='" + username + "'";
			pstmt = con.prepareStatement(insertLoginDate);
			pstmt.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
			pstmt.executeUpdate();

			if ("AGENT".equalsIgnoreCase(userInfo.getUserType())
					|| "RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
				PreparedStatement ps = con
						.prepareStatement("select name from st_lms_organization_master where organization_id = ?");
				ps.setInt(1, parentOrgId);
				rs = ps.executeQuery();

				if (rs.next()) {
					parentOrgName = rs.getString("name");
				}

				userInfo.setParentOrgName(parentOrgName);
			}

//			if (autoGenerate == 1) {
//				loginBean.setStatus("FirstTime");
//				return loginBean;
//			}

			if (!loggedInUser(username)) {

			}
			/*double userBal = userInfo.getAvailableCreditLimit()
					- userInfo.getClaimableBal();
			if (userBal < 0 && !"BO".equals(userInfo.getUserType())) {
				if(!"INACTIVE_AUTO_ACT".equalsIgnoreCase(reason)){
					loginBean.setStatus("BALANCE_NOT_POSITIVE");
					return loginBean;
				} else{
					loginBean.setStatus("success");
				}
				;
				return loginBean;
			}*/
		} catch (SQLException e) {
			logger.error("SQl Error: " + e);
			e.printStackTrace();

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("SQl Error: " + e);
				e.printStackTrace();
			}
		}
		loginBean.setStatus("success");
		return loginBean;
	}

	public boolean validateTerminalId(int userId, String terminalId) {
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select terminal_id from st_lms_ret_offline_master where user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();

			String dbTerminalId = "";

			if (rs.next()) {
				dbTerminalId = rs.getString("terminal_id");
			}
			if (dbTerminalId.length() > 8) {
				dbTerminalId = dbTerminalId.substring(
						dbTerminalId.length() - 8, dbTerminalId.length());
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
}
