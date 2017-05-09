package com.skilrock.lms.coreEngine.loginMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.MD5Encoder;

public class WebAuthenticationHelper {
	static Log logger = LogFactory.getLog(WebAuthenticationHelper.class);

	boolean loggedInResult = false;
	LoginBean loginBean = new LoginBean();
	ArrayList<String> userActionList = new ArrayList<String>();
	private UserInfoBean userInfo;
	
	public boolean loggedInUser(String user) {
		if (!ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();
			List<String> currentUserList = (List<String>) sc.getAttribute("LOGGED_IN_USERS");
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

	
	public LoginBean loginAuthentication(String username, String password, String interface_type, String loginLimit,String sessionId,boolean isIpTimingCheck) throws LMSException {
		PreparedStatement pstmt=null;
		PreparedStatement pstmtPriv = null;
		PreparedStatement pstmtPriv1 = null;
		Connection con=null;
		ResultSet rs=null;
		ResultSet rsPriv =null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String dbPass = ""; // stores the password retrieved from the database
			String role = "";
			String orgId = "";
			int roleId = 0;
			String status = "";
			int uid = -1;
			int userMapid = 0;
			String orgStatus = "";
			String orgName = "";
			int parentOrgId = 0;
			int tierId = 0;
			String parentOrgName = "";
			String parentOrgCode="";
			String orgCodeQry = "  bb.name orgCode ";

			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "  bb.org_code orgCode  ";
			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(bb.org_code,'_',bb.name)  orgCode  ";
			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(bb.name,'_',bb.org_code)  orgCode ";
			}
			StringBuilder getUserDetailsQuery = new StringBuilder("select user_session , if(aa.organization_type = 'RETAILER' , (select user_mapping_id from st_lms_user_random_id_mapping where user_id = aa.user_id) , 0) user_mapping_id, aa.user_id, aa.organization_id,aa.isrolehead,cc.is_master, aa.role_id,cc.tier_id, aa.user_name, aa.password, aa.status, aa.organization_type,bb.organization_status , aa.auto_password, aa.login_attempts"
				+ ", bb.name,bb.org_code,"+orgCodeQry+" , bb.organization_status, bb.current_credit_amt, bb.available_credit, bb.claimable_bal, bb.unclaimable_bal, bb.parent_id, bb.pwt_scrap, cc.role_name,if(login_attempts<"
				+ loginLimit + ",'ALLOW','BLOCK') logginAttempt ,bb.tp_organization istp "
				+ "from st_lms_user_master aa, st_lms_organization_master bb, st_lms_role_master cc "
				+ "where aa.organization_id = bb.organization_id	and aa.user_name =? and aa.role_id = cc.role_id");
		
			// UPDATE SESSION FOR USER
			Statement stmt = con.createStatement();
			int count = stmt.executeUpdate("update st_lms_user_master set user_session = '"+sessionId +"' where user_name = '"+username.trim()+"'");
			logger.info(count + " Rows updated");
			
			pstmt = con.prepareStatement(getUserDetailsQuery.toString());
			pstmt.setString(1, username.trim());
			rs = pstmt.executeQuery();
			logger.info(pstmt);

			// Check if more than one user exists in the database with same username
			if (rs.getFetchSize() > 1) {
				logger.debug("More Then One User Exist in the Database with Same  user_name" + username);
				loginBean.setStatus("ERROR");
				return loginBean;
			}
			if (rs.next()) {
				dbPass = rs.getString(TableConstants.USER_PASSWORD);
				role = rs.getString(TableConstants.ORG_TYPE);// This is tier code
				tierId = rs.getInt(TableConstants.TIER_ID);
				roleId = rs.getInt(TableConstants.ROLE_ID);
				status = rs.getString(TableConstants.USER_STATUS);
				uid = rs.getInt(TableConstants.USER_ID);
				userMapid = rs.getInt(TableConstants.MAPPING_ID);
				orgName = rs.getString(TableConstants.ORGANIZATION_NAME);
				orgId = rs.getString(TableConstants.ORG_ID);
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
				userInfo.setAvailableCreditLimit(rs.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
				userInfo.setClaimableBal(rs.getDouble("claimable_bal"));
				userInfo.setUnclaimableBal(rs.getDouble("unclaimable_bal"));
				userInfo.setCurrentCreditAmt(rs.getDouble(TableConstants.CURRENT_CREDIT_AMT));
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
			else {
				loginBean.setStatus("USER_NAME_NOT_MATCH");
				return loginBean;
			}
			if ("BLOCK".equals(rs.getString("logginAttempt"))) {
				loginBean.setStatus("LOGIN_LIMIT_REACHED");
				return loginBean;
			}
		
			// Matching Password
			if (MD5Encoder.encode(password).equals(dbPass) || userInfo.isTPUser()) {
				// checking the user status
				if (status.equals("BLOCK") || status.equals("TERMINATE") || orgStatus.equals("TERMINATE") || orgStatus.equals("BLOCK") ) {
					loginBean.setStatus("ERRORINACTIVE");
					return loginBean;
				}
				LinkedHashMap actionServiceMap = new LinkedHashMap();
				PriviledgeBean privBean = null;
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
					getAction = "select distinct(action_mapping) from "+ rs.getString("priv_rep_table")+ " pr,("+ getPrivId+ ") result where pr.priv_id=result.priv_id and pr.status='ACTIVE'";
					getMenuTitle = "select "+menuColumnName+" menu_disp_name,item_order,related_to,action_mapping from "+ rs.getString("menu_master_table")+ " smm,"+ rs.getString("priv_rep_table")+ " pr,("+ getPrivId+ ") result where  pr.priv_id=result.priv_id and pr.status='ACTIVE' and smm.action_id=pr.action_id order by related_to,item_order,menu_disp_name";
					pstmtPriv = con.prepareStatement(getAction);
					pstmtPriv.setInt(1, uid);
					pstmtPriv.setInt(2, roleId);
					pstmtPriv.setInt(3, rs.getInt("id"));
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
						privBean.setPrivTitle(rsPriv.getString("menu_disp_name"));
						privBean.setActionMapping(rsPriv.getString("action_mapping"));
						privBean.setRelatedTo(rsPriv.getString("related_to"));
						privList.add(privBean);
					}
					if (privList.size() > 0) {
						actionServiceMap.put(rs.getString("service_display_name")+ "-" + rs.getString("service_code"), privList);
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
				loginBean.setActionServiceMap(actionServiceMap);
				loginBean.setUserActionList(userActionList);
				loginBean.setUserInfo(userInfo);
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
				loginBean.setUserInfo(userInfo);
				if ("AGENT".equalsIgnoreCase(userInfo.getUserType()) || "RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
					PreparedStatement ps = con.prepareStatement("select name,org_code from st_lms_organization_master where organization_id = ?");
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
						pstmt.executeUpdate();
					}
				}

				if (!loggedInUser(username)) {}
			} else {
				loginBean.setStatus("PASS_NOT_MATCH");
				pstmt = con.prepareStatement("update st_lms_user_master set login_attempts = login_attempts+1 where user_name = ?");
				pstmt.setString(1, username);
				pstmt.executeUpdate();
				con.commit();
				return loginBean;
			}
			con.commit();
		} catch (SQLException e) {
			logger.error("SQl Error: " + e);
			throw new LMSException();
		} catch (Exception e) {
			logger.error(" Error: " + e);
			throw new LMSException();
		}finally {
			DBConnect.closeResource(pstmt,pstmtPriv,pstmtPriv1,rs,rsPriv,con) ;
		}
		loginBean.setStatus("success");
		return loginBean;
	}
	
	
}