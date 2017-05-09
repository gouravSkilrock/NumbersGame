package com.skilrock.lms.admin.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rolemgmt.beans.userPrivBean;

public class AccessAnyUserHelper {
	ArrayList<String> userActionList = new ArrayList<String>();
	private UserInfoBean userInfo;
	LoginBean loginBean = new LoginBean();
	public static void main(String[] args) {
		AccessAnyUserHelper user = new AccessAnyUserHelper();
		System.out.println(user.getAllLMSUser());
	}

	public Map<String, List<String>> getAllLMSUser() {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, List<String>> userMap = new HashMap<String, List<String>>();
		List<String> userList = null;
		try {
			String qry = "Select organization_type,user_name,status from st_lms_user_master  where status !='TERMINATE'  order by organization_type";
			pstmt = con.prepareStatement(qry);
			rs = pstmt.executeQuery();
			String orgType;
			String userName;
			while (rs.next()) {
				orgType = rs.getString("organization_type");
				userName = rs.getString("user_name");
				if (userMap.containsKey(orgType)) {
					userMap.get(orgType).add(userName);
				} else {
					userList = new ArrayList<String>();
					userList.add(userName);
					userMap.put(orgType, userList);
				}
			}

		} catch (Exception e) {
			System.out.println("---error in get all lms user---");
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return userMap;
	}

	/*public LoginBean loginAuthentication(String username, String interface_type) {
		Connection con=null;
		try {
			con = DBConnect.getConnection();

			int autoGenerate = 0;
			String role = "";
			int roleId = 0;
			String status = "";
			int uid = -1;
			String reason="";
			String orgStatus = "";
			String orgName = "";
			String orgId = "";
			int userMapid = 0;
			int parentOrgId = 0;
			int tierId = 0;
			String parentOrgName = "";
			System.out.println("inside Auth Helper");

			String getUserDetailsQuery = "select if(aa.organization_type = 'RETAILER' , (select user_mapping_id from st_lms_user_random_id_mapping where user_id = aa.user_id) , 0) user_mapping_id, aa.user_id, aa.organization_id,aa.isrolehead,cc.is_master, aa.role_id,cc.tier_id, aa.user_name, aa.password, aa.status, aa.organization_type,bb.organization_status , aa.auto_password"
					+ ", bb.name, bb.organization_status, bb.current_credit_amt, bb.available_credit, bb.claimable_bal, bb.unclaimable_bal, bb.parent_id, bb.pwt_scrap, cc.role_name "
					+ "from st_lms_user_master aa, st_lms_organization_master bb, st_lms_role_master cc "
					+ "where aa.organization_id = bb.organization_id  and aa.user_name =?and aa.role_id = cc.role_id";

			PreparedStatement pstmt = con.prepareStatement(getUserDetailsQuery);
			pstmt.setString(1, username.trim());

			ResultSet rs = pstmt.executeQuery();
			System.out.println(pstmt);

			// check More Then One Users Exist in the Database or not with Same
			// user_name
			if (rs.getFetchSize() > 1) {
				System.out.println("More Then One User Exist in the Database with Same  user_name"
								+ username);
				loginBean.setStatus("ERROR");
				return loginBean;
			}

			// getting the user details from database
			if (rs.next()) {
				role = rs.getString(TableConstants.ORG_TYPE);// This is tier
				// Code
				tierId = rs.getInt(TableConstants.TIER_ID);
				roleId = rs.getInt(TableConstants.ROLE_ID);
				status = rs.getString(TableConstants.USER_STATUS);
				username = rs.getString(TableConstants.USER_NAME);
				uid = rs.getInt(TableConstants.USER_ID);
				orgId = rs.getString(TableConstants.ORG_ID);
				orgName = rs.getString(TableConstants.ORGANIZATION_NAME);
				autoGenerate = rs.getInt("auto_password");
				parentOrgId = rs.getInt("parent_id");
				orgStatus = rs.getString("organization_status");
				
				pstmt = con.prepareStatement("select tier_code from st_lms_tier_master where tier_id = "+tierId);
				ResultSet rs2 = pstmt.executeQuery();
				if(rs2.next()){
					if(rs2.getString("tier_code").equalsIgnoreCase("RETAILER")){
						pstmt = con.prepareStatement("select user_mapping_id from st_lms_user_random_id_mapping where user_id = "+uid);
						ResultSet rs3 = pstmt.executeQuery();
						if(rs3.next()){
							userMapid = rs3.getInt(TableConstants.MAPPING_ID);
						}
					}
				}

				userInfo = new UserInfoBean();
				userInfo.setRoleName(rs.getString(TableConstants.ROLE_NAME));
				userInfo.setRoleId(roleId);
				userInfo.setUserId(uid);
				userInfo.setUserName(username);
				userInfo.setUserOrgId(rs.getInt(TableConstants.ORG_ID));
				userInfo.setUserType(role);
				userInfo.setTierId(tierId);
				userInfo.setOrgName(orgName);
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
				userInfo.setCurrentUserMappingId(rs.getInt("user_mapping_id"));
				userInfo.setIsMasterRole(rs.getString("is_master"));
				userInfo.setIsRoleHeadUser(rs.getString("isrolehead"));
				userInfo.setLoginChannel(interface_type);
			}
			// check the user's info before login
			else {
				loginBean.setStatus("USER_NAME_NOT_MATCH");
				return loginBean;
			}

			
				// checking the user status
			if (status.equals("BLOCK") || status.equals("TERMINATE")
					|| orgStatus.equals("TERMINATE") || orgStatus.equals("BLOCK") ) {
				
			
				loginBean.setStatus("ERRORINACTIVE");
				return loginBean;
			}
			
			/*	if (status.equals("INACTIVE") || status.equals("TERMINATE")
						|| orgStatus.equals("INACTIVE")
						|| orgStatus.equals("TERMINATE")) {
					loginBean.setStatus("ERRORINACTIVE");
					return loginBean;
				}else if("INACTIVE".equalsIgnoreCase(orgStatus)){
					
				PreparedStatement pstmt2 = con
						.prepareStatement("select reason from st_lms_organization_master_history where organization_status='INACTIVE' and organization_id=? order by date_changed desc limit 1");
				pstmt2.setString(1, orgId);
				ResultSet rs2 = pstmt2.executeQuery();

				if (rs2.next()) {
					reason = rs2.getString("reason");
				}

				if ("INACTIVE_MANUAL_BO".equals(reason)) {
					loginBean.setStatus("ERRORINACTIVE");
					return loginBean;
				}
				}*/

				/*rs.close();
				pstmt.close();

				LinkedHashMap<String, List<PriviledgeBean>> actionServiceMap = new LinkedHashMap<String, List<PriviledgeBean>>();
				PriviledgeBean privBean = null;
				String getService = "select srm.id,role_id,interface,service_display_name,service_code,ref_merchant_id,priv_rep_table,menu_master_table,service_deligate_url from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id=? and organization_id=? and srm.status='ACTIVE' and sm.status='ACTIVE' and sdm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id and sdm.interface=?";
				String getPrivId = "select distinct(upm.priv_id) from st_lms_role_priv_mapping rpm,st_lms_user_priv_mapping upm where upm.user_id=? and upm.role_id=? and rpm.status='ACTIVE' and upm.status='ACTIVE'and upm.role_id=rpm.role_id and upm.service_mapping_id=?";
				String getAction = null;
				String getMenuTitle = null;
				pstmt = con.prepareStatement(getService);
				pstmt.setInt(1, roleId);
				pstmt.setInt(2, Integer.parseInt(orgId));
				pstmt.setString(3, interface_type);
				System.out.println(pstmt);
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
					PreparedStatement pstmtPriv = con.prepareStatement(getAction);
					pstmtPriv.setInt(1, uid);
					pstmtPriv.setInt(2, roleId);
					pstmtPriv.setInt(3, rs.getInt("id"));
					// System.out.println(pstmtPriv);
					ResultSet rsPriv = pstmtPriv.executeQuery();
					while (rsPriv.next()) {
						userActionList.add(rsPriv.getString("action_mapping"));
					}
					pstmtPriv = con.prepareStatement(getMenuTitle);
					pstmtPriv.setInt(1, uid);
					pstmtPriv.setInt(2, roleId);
					pstmtPriv.setInt(3, rs.getInt("id"));
					System.out.println(pstmtPriv);
					rsPriv = pstmtPriv.executeQuery();
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
				loginBean.setActionServiceMap(actionServiceMap);
				loginBean.setUserInfo(userInfo);
				loginBean.setUserActionList(userActionList);

				String insertLoginDate = QueryManager.insertST3LoginDate()
						+ " where user_name='" + username + "'";
				pstmt = con.prepareStatement(insertLoginDate);
				pstmt.setTimestamp(1, new java.sql.Timestamp(new Date()
						.getTime()));
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

//				if (autoGenerate == 1) {
//					loginBean.setStatus("FirstTime");
//					return loginBean;
//				}

				if (!loggedInUser(username)) {

				}
			

			double userBal = userInfo.getAvailableCreditLimit()
					- userInfo.getClaimableBal();
			if (userBal < 0 && !"BO".equals(userInfo.getUserType())) {
				loginBean.setStatus("BALANCE_NOT_POSITIVE");
				;
				return loginBean;
			}
		} catch (SQLException e) {
			System.err.println("SQl Error: " + e);
			e.printStackTrace();

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.err.println("SQl Error: " + e);
				e.printStackTrace();
			}
		}
		loginBean.setStatus("success");
		return loginBean;
	}*/
	@SuppressWarnings("unchecked")
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
	
	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> getUserPriviledges(
			String userName) throws LMSException {

		Connection con = DBConnect.getConnection();
		// ArrayList<userPrivBean> userPrivList=new ArrayList<userPrivBean>();
		Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> headPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>>();
		int roleId = 0, userOrgId = 0, userId = 0;
		try {
			Statement stmt = con.createStatement();
			Statement privStmt = con.createStatement();
			Statement serviceStmt = con.createStatement();
			String rolePrivQuery = null;

			ResultSet userRs = stmt
					.executeQuery("select user_id,role_id,organization_id,organization_type from st_lms_user_master where user_name='"
							+ userName + "'");
			while (userRs.next()) {
				userId = userRs.getInt("user_id");
				roleId = userRs.getInt("role_id");
				userOrgId = userRs.getInt("organization_id");
			}

			String fetchService = "select srm.id,role_id,interface,service_display_name,priv_rep_table,srm.status from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id="
					+ roleId
					+ " and organization_id="
					+ userOrgId
					+ " and srm.status='ACTIVE' and sm.status='ACTIVE' and sdm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id";
			System.out.println("fetchService====" + fetchService);
			ResultSet serviceRS = serviceStmt.executeQuery(fetchService);
			ResultSet privRS = null;
			TreeMap<String, TreeMap<String, List<userPrivBean>>> interfaceMap = null;
			while (serviceRS.next()) {
				rolePrivQuery = "select distinct(group_name),upm.status,pr.related_to from "
						+ serviceRS.getString("priv_rep_table")
						+ " as pr ,st_lms_role_priv_mapping as rpm, st_lms_user_priv_mapping upm where upm.user_id="
						+ userId
						+ " and  (upm.status='ACTIVE' or upm.status='INACTIVE') and upm.priv_id=rpm.priv_id and (rpm.service_mapping_id="
						+ serviceRS.getString("id")
						+ " and upm.service_mapping_id="
						+ serviceRS.getString("id")
						+ ") and (rpm.role_id="
						+ roleId
						+ " and upm.role_id="
						+ roleId
						+ ") and  rpm.priv_id=pr.priv_id order by related_to,group_name";
				System.out.println("rolePrivQuery====" + rolePrivQuery);
				privRS = privStmt.executeQuery(rolePrivQuery);
				String relatedTo = "";
				userPrivBean privBean;
				String oldRelatedTo = "";
				List<userPrivBean> groupNameList = null;
				if (!headPriviledgeMap.containsKey(serviceRS
						.getString("service_display_name"))) {
					interfaceMap = new TreeMap<String, TreeMap<String, List<userPrivBean>>>();
				}
				TreeMap<String, List<userPrivBean>> privMap = new TreeMap<String, List<userPrivBean>>();
				// logger.debug(rolePrivQuery);
				while (privRS.next()) {
					if (!privRS.getString("group_name").equals("Miscellaneous")
							&& !privRS.getString("group_name").equals(
									"Create Role")
							&& !privRS.getString("group_name").equals(
									"Edit Role")
							&& !privRS.getString("group_name").equals(
									"BO User Registration")
							&& !privRS.getString("group_name").equals(
									"BO: Edit Role")
							&& !privRS.getString("group_name").equals(
									"BO: Create Role")
							&& !privRS.getString("group_name").equals(
									"BO: Role Head Registration")) {
						relatedTo = privRS.getString("related_to");
						privBean = new userPrivBean();
						// privBean.setPid(Privrs.getInt("pid"));
						privBean.setPrivTitle(privRS.getString("group_name"));
						System.out.println("Prin Bean*******************: "
								+ privBean.getPrivTitle());
						privBean.setStatus(privRS.getString("status"));
						privBean.setPrivRelatedTo(relatedTo);
						// logger.debug(privRS.getString("group_name")+"***"+privRS.getString("status"));
						if (!relatedTo.equals(oldRelatedTo)) {
							groupNameList = new ArrayList<userPrivBean>();
							oldRelatedTo = relatedTo;
							privMap.put(oldRelatedTo, groupNameList);

						}
						groupNameList.add(privBean);
					}
				}

				interfaceMap.put(serviceRS.getString("interface") + "-"
						+ serviceRS.getString("id"), privMap);
				headPriviledgeMap.put(serviceRS
						.getString("service_display_name"), interfaceMap);

			}

			System.out.println("*****headPriviledgeMap****\n" + headPriviledgeMap
					+ "\n********");
			return headPriviledgeMap;
		} catch (SQLException e) {
			System.out.println("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("Error During Rollback", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}
	
	public void saveUserPriv(String userName, String[] groupNames,
			int[] mappingId, int[] privCount) throws LMSException {

		Connection con = DBConnect.getConnection();
		int userId = 0, roleId = 0;
		try {
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();
			ResultSet userRs = stmt
					.executeQuery("select user_id,role_id,organization_id,organization_type from st_lms_user_master where user_name='"
							+ userName + "'");
			while (userRs.next()) {
				userId = userRs.getInt("user_id");
				roleId = userRs.getInt("role_id");
			}

			for (int element : mappingId) {
				String updateUserPriv = "update st_lms_user_priv_mapping set status='INACTIVE' where user_id="
						+ userId
						+ " and role_id="
						+ roleId
						+ " and status!='NA' and service_mapping_id=" + element;
				stmt.executeUpdate(updateUserPriv);
			}

			StringBuilder grpName = null;
			StringBuilder strMappingId = new StringBuilder("");
			String grpNameStr = null;
			int privIdFrm = 0;
			int privIdTo = 0;
			String activeMapIds = "";
			HashMap<Integer, String> privMap = new HashMap<Integer, String>();
			for (int i = 0; i < mappingId.length; i++) {
				if (privCount[i] != 0) {
					grpName = new StringBuilder("'Miscellaneous',");
					privIdTo = privIdTo + privCount[i];
					for (int j = privIdFrm; j < privIdTo; j++) {
						grpName.append("'" + groupNames[j] + "',");
						privIdFrm++;
					}
					grpNameStr = grpName.substring(0, grpName.length() - 1);
					activeMapIds = activeMapIds + mappingId[i] + ",";
					privMap.put(mappingId[i], grpNameStr);
				}
				strMappingId.append(mappingId[i] + ",");

			}
			strMappingId.deleteCharAt(strMappingId.length() - 1);

			
			String fetchPrivTable = null;
			ResultSet fetchPrivTabRS = null;
			String updateRolePriv = null;
			Statement stmtMappingId = con.createStatement();

			fetchPrivTable = "select service_delivery_master_id,priv_rep_table from st_lms_service_delivery_master where service_delivery_master_id in("
					+ strMappingId.toString() + ")";
			fetchPrivTabRS = stmtMappingId.executeQuery(fetchPrivTable);

			while (fetchPrivTabRS.next()) {
				updateRolePriv = "update st_lms_user_priv_mapping set status='ACTIVE' where user_id="
						+ userId
						+ " and role_id="
						+ roleId
						+ " and service_mapping_id="
						+ fetchPrivTabRS.getInt("service_delivery_master_id")
						+ " and priv_id in (select distinct(priv_id) from "
						+ fetchPrivTabRS.getString("priv_rep_table")
						+ " pr where group_name in ("
						+ privMap.get(fetchPrivTabRS
								.getInt("service_delivery_master_id"))
						+ ") and pr.status='ACTIVE') ";
				stmt.executeUpdate(updateRolePriv);
			}
			con.commit();
		} catch (SQLException e) {
			
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(con);
		}
	}
}
