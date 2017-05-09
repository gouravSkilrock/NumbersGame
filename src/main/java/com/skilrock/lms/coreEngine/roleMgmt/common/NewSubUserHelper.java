package com.skilrock.lms.coreEngine.roleMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.admin.SetResetUserPasswordAction;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.coreEngine.userMgmt.common.LoginTimeIPValidationHelper;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.MessageInboxDaoImpl;
import com.skilrock.lms.rolemgmt.beans.userListBean;
import com.skilrock.lms.rolemgmt.beans.userPrivBean;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.common.SLEUtils;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.MenuDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.MenuItemDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RolePrivilegeBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.SubUserDataBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class NewSubUserHelper {
	static Log logger = LogFactory.getLog(NewSubUserHelper.class);

	public static void main(String[] args) throws Exception {
		NewSubUserHelper x = new NewSubUserHelper();
		x.test();
	}

	public String assignGroups(int userOrgId, String[] groupNames,
			int parentUserId, int roleId, UserDetailsBean usrdetBean,
			int[] mappingId, int[] privCount) throws LMSException {
		return assignGroups(userOrgId, groupNames, parentUserId, roleId, usrdetBean, mappingId, privCount, null, "");
	}
	public String assignGroups(int userOrgId, String[] groupNames,
			int parentUserId, int roleId, UserDetailsBean usrdetBean,
			int[] mappingId, int[] privCount, List<PrivilegeDataBean> privilegeListSLE, String requestIp) throws LMSException {

		
		Connection con = DBConnect.getConnection();

		// int userId=getUserIdByuserName(userName);
		int userId = 0;
		String password = null;
		String orgType = usrdetBean.getOrgType();
		password = com.skilrock.lms.web.loginMgmt.AutoGenerate.autoPassword();

		String status = null;
		try {
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			con.setAutoCommit(false);

			// insert data into st user master

			stmt1
					.executeUpdate("insert into st_lms_user_master(organization_id,role_id,parent_user_id,organization_type,registration_date,auto_password,user_name,password,status,secret_ques,secret_ans,isrolehead, register_by_id) values("
							+ usrdetBean.getOrgId()
							+ ","
							+ usrdetBean.getRoleId()
							+ ","
							+ parentUserId
							+ ",'"
							+ usrdetBean.getOrgType()
							+ "','"
							+ new java.sql.Timestamp(new Date().getTime())
							+ "',1,'"
							+ usrdetBean.getUserName().trim().toLowerCase()
							+ "','"
							+ MD5Encoder.encode(password)
							+ "','"
							+ usrdetBean.getStatus()
							+ "','"
							+ usrdetBean.getSecQues()
							+ "','"
							+ usrdetBean.getSecAns() + "','N', "+usrdetBean.getRegisterByUserId()+")");
			ResultSet rs = stmt1.getGeneratedKeys();
			while (rs.next()) {
				userId = rs.getInt(1);

			}
			stmt1
					.executeUpdate("INSERT into st_lms_user_contact_details(user_id,first_name,last_name,email_id,phone_nbr) VALUES('"
							+ userId
							+ "','"
							+ usrdetBean.getFirstName()
							+ "','"
							+ usrdetBean.getLastName()
							+ "','"
							+ usrdetBean.getEmailId()
							+ "','"
							+ usrdetBean.getPhoneNbr() + "')");
			stmt1
					.executeUpdate("INSERT into st_lms_password_history (user_id,password,date_changed,type)VALUES('"
							+ userId
							+ "','"
							+ MD5Encoder.encode(password)
							+ "','"
							+ new java.sql.Timestamp(new Date().getTime())
							+ "','1')");

			//	Insert Data In st_lms_user_ip_time_mapping Table.
			LoginTimeIPValidationHelper helper = new LoginTimeIPValidationHelper();
			helper.insertUserTimeLimitData(userId, con);

			// insert data into user priviledges master

			System.out
					.println("***Start Insertion into st_lms_user_priv_mapping***");

			Statement serviceStmt = con.createStatement();

			String fetchService = "select srm.id,role_id,interface,service_display_name,priv_rep_table from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id="
					+ roleId
					+ " and organization_id="
					+ userOrgId
					+ " and sm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id";
			logger.debug("fetchService====" + fetchService);
			ResultSet serviceRS = serviceStmt.executeQuery(fetchService);

			String currentTime = Util.getCurrentTimeString();
			while (serviceRS.next()) {
				String insertintoUserPrivMapQuery = "insert into st_lms_user_priv_mapping (user_id, role_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip) "
						+ "select "
						+ userId
						+ ","
						+ roleId
						+ ",pr.priv_id,"
						+ serviceRS.getInt("id")
						+ ",'NA', '"+currentTime+"', "+parentUserId+", '"+requestIp+"' from st_lms_user_priv_mapping upm,"
						+ serviceRS.getString("priv_rep_table")
						+ " as pr,st_lms_role_priv_mapping rpm where upm.user_id="
						+ parentUserId
						+ " and upm.role_id="
						+ roleId
						+ "  and upm.service_mapping_id="
						+ serviceRS.getInt("id")
						+ " and upm.role_id=rpm.role_id and upm.priv_id = rpm.priv_id  and upm.service_mapping_id = rpm.service_mapping_id and upm.priv_id=pr.priv_id group by pr.priv_id";
				logger.debug(insertintoUserPrivMapQuery);
				stmt.executeUpdate(insertintoUserPrivMapQuery);
			}
			System.out
					.println("***End Insertion into st_lms_user_priv_mapping***");
			logger.debug("MAPPING ID********************" + mappingId);
			HashMap<Integer, String> headUserPrivMap = new HashMap<Integer, String>();
			for (int element : mappingId) {
				String selectQuery = "select distinct(priv_id) from st_lms_user_priv_mapping where user_id="
						+ parentUserId
						+ " and role_id="
						+ roleId
						+ " and status='ACTIVE' and service_mapping_id="
						+ element;
				logger.debug("*****" + selectQuery);
				PreparedStatement pstmt = con.prepareStatement(selectQuery);
				ResultSet selectQueryRS = pstmt.executeQuery();
				StringBuilder str = new StringBuilder("");
				while (selectQueryRS.next()) {
					str.append(selectQueryRS.getString("priv_id"));
					str.append(",");
				}
				if(str.length() > 0){
					str.deleteCharAt(str.length() - 1);
					headUserPrivMap.put(element, str.toString());
				}
			}

			for (int element : mappingId) {
				String updateUserPriv = "update st_lms_user_priv_mapping set status='INACTIVE' where user_id="
						+ userId
						+ " and role_id="
						+ roleId
						+ " and service_mapping_id="
						+ element
						+ " and priv_id in ("
						+ headUserPrivMap.get(element)
						+ ")";
				logger.debug("INACTIVE UPDATE::" + updateUserPriv);
				int rowsEffected = stmt.executeUpdate(updateUserPriv);
				System.out.println("**rowsEffected*****"+rowsEffected+"*****");
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
						
						grpName.append(groupNames[j].contains("|") ? "'" + groupNames[j].substring(0, groupNames[j].lastIndexOf("|")) + "'," : "'" + groupNames[j] + "'," ); 
						
						privIdFrm++;
					}
					grpNameStr = grpName.substring(0, grpName.length() - 1);
					activeMapIds = activeMapIds + mappingId[i] + ",";
					privMap.put(mappingId[i], grpNameStr);
				}
				logger.debug("**" + privCount[i]);
				strMappingId.append(mappingId[i] + ",");

			}
			strMappingId.deleteCharAt(strMappingId.length() - 1);

			logger.debug("privMap*****" + privMap);

			String fetchPrivTable = null;
			ResultSet fetchPrivTabRS = null;
			String updateRolePriv = null;
			Statement stmtMappingId = con.createStatement();

			fetchPrivTable = "select service_delivery_master_id,priv_rep_table from st_lms_service_delivery_master where service_delivery_master_id in("
					+ strMappingId.toString() + ")";
			logger.debug("*****" + fetchPrivTable);
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
				logger.debug("ACTIVE UPDATE::" + updateRolePriv);
				stmt.executeUpdate(updateRolePriv);
			}

			//	Insert Registration Messages
			MessageInboxDaoImpl.getSingleInstance().addRegistrationMessage(userId, usrdetBean.getOrgType(), "WEB", con);

			boolean flag = true;
			if(ServicesBean.isSLE()) {
				if(privilegeListSLE != null) {
					flag = false;
					for(PrivilegeDataBean privilegeBean : privilegeListSLE) {
						for(MenuDataBean menuBean : privilegeBean.getMenuList()) {
							for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
								menuItemBean.setIsAssign(false);
								for(String privilege : groupNames) {
									if(privilege.contains("Sports Lottery"))
									{
									if(privilege.substring(0, privilege.lastIndexOf("|")).equals(menuItemBean.getMenuItemName())) {
										menuItemBean.setIsAssign(true);
									}
									}
								}
							}
						}
					}

					try {
						SubUserDataBean userDataBean = new SubUserDataBean();
						userDataBean.setCreatorUserId(parentUserId);
						userDataBean.setRoleId(roleId);
						userDataBean.setUserId(userId);
						userDataBean.setUserName(usrdetBean.getUserName().trim().toLowerCase());
						userDataBean.setUserType(usrdetBean.getOrgType());
						userDataBean.setFirstName(usrdetBean.getFirstName());
						userDataBean.setLastName(usrdetBean.getLastName());
						userDataBean.setMobileNbr(String.valueOf(usrdetBean.getPhoneNbr()));
						userDataBean.setEmailId(usrdetBean.getEmailId());
						userDataBean.setSecretQues(usrdetBean.getSecQues());
						userDataBean.setSecretAns(usrdetBean.getSecAns());
						userDataBean.setRequestIp(requestIp);
						userDataBean.setPrivilegeList(privilegeListSLE);
	
						NotifySLE notifySLE = new NotifySLE(SLE.Activity.SUB_USER_REGISTRATION, userDataBean);
						notifySLE.asyncCall(notifySLE);
						flag = true;
					} catch (Exception ex) {
						ex.printStackTrace();
						flag = false;
					}
				}
			}

			if(flag) {
			con.commit();
			if (usrdetBean.isMailSend()) {
				String msgFor = "Thanks for registration to our gaming system  Your Login details are";
				String emailMsgTxt = "<html><table><tr><td>Hi "
						+ usrdetBean.getFirstName() + " "
						+ usrdetBean.getLastName() + "</td></tr><tr><td>" + msgFor
						+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
						+ usrdetBean.getUserName()
						+ "</td></tr><tr><td>password :: </td><td>"
						+ password.toString()
						+ "</td></tr><tr><td>log on </td><td>"
						+ LMSFilterDispatcher.webLink + "/"
						+ LMSFilterDispatcher.mailProjName
						+ "/</td></tr></table></html>";
				MailSend mailSend = new MailSend(usrdetBean.getEmailId(),
						emailMsgTxt);
				mailSend.setDaemon(true);
				mailSend.start();
				}
			} else {
				con.rollback();
				throw new LMSException("exception occured");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql exception ", e);
		} finally {
			DBConnect.closeCon(con);
		}
		return password;
	}

	public String assignPriviledges(String userName, int[] rolePid,
			ArrayList<userPrivBean> headPrivList, int roleId,
			UserDetailsBean usrdetBean) throws LMSException {
		String projectName = ServletActionContext.getServletContext()
				.getContextPath();
		logger.debug("user name and pids are " + userName + rolePid);
		 
		Connection con = DBConnect.getConnection();

		// int userId=getUserIdByuserName(userName);
		int userId = 0;
		String password = password = com.skilrock.lms.web.loginMgmt.AutoGenerate
				.autoPassword();
		;
		String orgType = usrdetBean.getOrgType();
		logger.debug("Org Type : " + orgType);

		logger.debug("Generated Password is :*************************"
				+ password);
		String status = null;
		try {
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			con.setAutoCommit(false);

			// insert data into st user master

			stmt1
					.executeUpdate("insert into st_lms_user_master(organization_id,role_id,organization_type,registration_date,auto_password,user_name,password,status,secret_ques,secret_ans,isrolehead) values("
							+ usrdetBean.getOrgId()
							+ ","
							+ usrdetBean.getRoleId()
							+ ",'"
							+ usrdetBean.getOrgType()
							+ "','"
							+ new java.sql.Timestamp(new Date().getTime())
							+ "',1,'"
							+ usrdetBean.getUserName().trim().toLowerCase()
							+ "','"
							+ MD5Encoder.encode(password)
							+ "','"
							+ usrdetBean.getStatus()
							+ "','"
							+ usrdetBean.getSecQues()
							+ "','"
							+ usrdetBean.getSecAns() + "','N')");
			ResultSet rs = stmt1.getGeneratedKeys();
			while (rs.next()) {
				userId = rs.getInt(1);

			}
			logger.info("************insert 1*************");
			stmt1
					.executeUpdate("INSERT into st_lms_user_contact_details(user_id,first_name,last_name,email_id,phone_nbr) VALUES('"
							+ userId
							+ "','"
							+ usrdetBean.getFirstName()
							+ "','"
							+ usrdetBean.getLastName()
							+ "','"
							+ usrdetBean.getEmailId()
							+ "','"
							+ usrdetBean.getPhoneNbr() + "')");
			logger.info("************insert 2*************");
			stmt1
					.executeUpdate("INSERT into st_lms_password_history (user_id,password,date_changed,type)VALUES('"
							+ userId
							+ "','"
							+ MD5Encoder.encode(password)
							+ "','"
							+ new java.sql.Timestamp(new Date().getTime())
							+ "','1')");
			logger.info("************insert 3*************");
			logger.debug("array length is " + rolePid.length + "value is  "
					+ rolePid[0]);

			// insert data into user priviledges master
			String insertintoUserPrivMapQuery = null;
			for (int i = 0; i < headPrivList.size(); i++) {
				int headPid = headPrivList.get(i).getPid();
				status = "INACTIVE";
				for (int element : rolePid) {
					// logger.debug(rolePid[j] + "gggggg"+
					// headPrivList.get(i).getPid());
					if (element == headPid) {
						status = "ACTIVE";
						break;
					}

				}

				insertintoUserPrivMapQuery = "insert into user_priv_master values("
						+ userId
						+ ","
						+ roleId
						+ ","
						+ headPrivList.get(i).getPid() + ",'" + status + "')";
				// logger.debug("query to insert is : " + "insert into
				// user_priv_master
				// values("+userId+","+roleId+","+headPrivList.get(i).getPid()+",'"+status+"')");
				stmt.executeUpdate(insertintoUserPrivMapQuery);

				// con.commit();
			}
			con.commit();
			String msgFor = "Thanks for registration to our gaming system  Your Login details are *********************neeraj Wadhwa..............";
			// MailSend.sendMailToUser(usrdetBean.getEmailId(),
			// password,usrdetBean.getUserName());
			String emailMsgTxt = "<html><table><tr><td>Hi "
					+ usrdetBean.getFirstName() + " "
					+ usrdetBean.getLastName() + "</td></tr><tr><td>" + msgFor
					+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
					+ usrdetBean.getUserName()
					+ "</td></tr><tr><td>password :: </td><td>"
					+ password.toString()
					+ "</td></tr><tr><td>log on </td><td>"
					+ LMSFilterDispatcher.webLink + "/"
					+ LMSFilterDispatcher.mailProjName
					+ "/</td></tr></table></html>";
			MailSend mailSend = new MailSend(usrdetBean.getEmailId(),
					emailMsgTxt);
			mailSend.setDaemon(true);
			mailSend.start();
			// MailSend.sendMailToUser(usrdetBean.getEmailId(),
			// password,usrdetBean.getUserName(),usrdetBean.getFirstName(),usrdetBean.getLastName(),msgFor);

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql exception ", e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return password;
	}

	public Map createPrivMap() throws LMSException {
		Map relate_to_Map = new HashMap();
		Map parentPrivMap = null;

		Iterator relMapItr = relate_to_Map.entrySet().iterator();

		 
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();
			String relatedTo = "";
			String getParentPriv = "select distinct(group_name),related_to from priviledge_rep where priv_owner='BO' order by related_to,group_name";
			ResultSet rs = stmt.executeQuery(getParentPriv);

			while (rs.next()) {
				if (!relatedTo.equals(rs.getString("related_to"))) {
					parentPrivMap = new HashMap();
					relatedTo = rs.getString("related_to");
					relate_to_Map.put(relatedTo, parentPrivMap);

				}
				String groupName = rs.getString("group_name");
				parentPrivMap.put(groupName, fetchGroupPriv(groupName, con));

			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("Error During Privilege Fetching", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
			}
		}

		return relate_to_Map;
	}

	public void editBOPriv(String userName, String[] groupNames,
			int[] mappingId, int[] privCount, List<PrivilegeDataBean> privilegeListSLE) throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		int userId = 0, roleId = 0, userOrgId = 0;
		try {
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();
			ResultSet userRs = stmt
					.executeQuery("select user_id,role_id,organization_id,organization_type from st_lms_user_master where user_name='"
							+ userName + "'");
			while (userRs.next()) {
				userId = userRs.getInt("user_id");
				roleId = userRs.getInt("role_id");
				userOrgId = userRs.getInt("organization_id");
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
						grpName.append(groupNames[j].contains("|") ? "'" + groupNames[j].substring(0, groupNames[j].lastIndexOf("|")) + "'," : "'" + groupNames[j] + "'," );
						//grpName.append("'" + groupNames[j].substring(0, groupNames[j].lastIndexOf("|")) + "',");
						privIdFrm++;
					}
					grpNameStr = grpName.substring(0, grpName.length() - 1);
					activeMapIds = activeMapIds + mappingId[i] + ",";
					privMap.put(mappingId[i], grpNameStr);
				}
				logger.debug("**" + privCount[i]);
				strMappingId.append(mappingId[i] + ",");

			}
			strMappingId.deleteCharAt(strMappingId.length() - 1);

			logger.debug("privMap*****" + privMap);

			String fetchPrivTable = null;
			ResultSet fetchPrivTabRS = null;
			String updateRolePriv = null;
			Statement stmtMappingId = con.createStatement();

			fetchPrivTable = "select service_delivery_master_id,priv_rep_table from st_lms_service_delivery_master where service_delivery_master_id in("
					+ strMappingId.toString() + ")";
			logger.debug("*****" + fetchPrivTable);
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
				logger.debug(updateRolePriv);
				stmt.executeUpdate(updateRolePriv);
				stmt
						.executeUpdate("update st_lms_user_priv_mapping set status='NA' where user_id!="
								+ userId
								+ " and role_id="
								+ roleId
								+ " and service_mapping_id="
								+ fetchPrivTabRS
										.getInt("service_delivery_master_id")
								+ " and priv_id in (select a.priv_id from (select inTab.priv_id from st_lms_user_priv_mapping as inTab where inTab.user_id="
								+ userId
								+ " and inTab.status='INACTIVE' and service_mapping_id="
								+ fetchPrivTabRS
										.getInt("service_delivery_master_id")
								+ " ) a )");
				stmt
						.executeUpdate("update st_lms_user_priv_mapping set status='INACTIVE' where user_id!="
								+ userId
								+ " and role_id="
								+ roleId
								+ " and service_mapping_id="
								+ fetchPrivTabRS
										.getInt("service_delivery_master_id")
								+ " and status='NA' and priv_id in (select a.priv_id from (select inTab.priv_id from st_lms_user_priv_mapping as inTab where inTab.user_id="
								+ userId
								+ " and inTab.status='ACTIVE' and service_mapping_id="
								+ fetchPrivTabRS
										.getInt("service_delivery_master_id")
								+ " ) a )");
			}

			boolean flag = true;
			if(ServicesBean.isSLE()) {
				if(privilegeListSLE != null && privilegeListSLE.size() > 0) {
					flag = false;
					for(PrivilegeDataBean privilegeBean : privilegeListSLE) {
						for(MenuDataBean menuBean : privilegeBean.getMenuList()) {
							for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
								menuItemBean.setIsAssign(false);
								for(String privilege : groupNames) {
									if(privilege.contains("Sports Lottery"))
									{
									if(privilege.substring(0, privilege.lastIndexOf("|")).equals(menuItemBean.getMenuItemName())) {
										menuItemBean.setIsAssign(true);
									}
									}
								}
							}
						}
					}

					try {
						SubUserDataBean userDataBean = new SubUserDataBean();
						userDataBean.setRoleId(roleId);
						userDataBean.setPrivilegeList(privilegeListSLE);
						NotifySLE notifySLE = new NotifySLE(SLE.Activity.SUB_USER_EDIT, userDataBean);
						notifySLE.asyncCall(notifySLE);
						flag = true;
					} catch (Exception ex) {
						ex.printStackTrace();
						flag = false;
						logger.debug("Error in Edit Role.");
						throw new SQLException("Error in Edit Role.");
					}
				} else {
					logger.debug("privilegeListSLE is Not Applicable.");
					throw new SQLException("Error in Creating Role.");
				}
			}

			if(flag) {
				con.commit();
			} else {
				con.rollback();
			}

			con.commit();
		} catch (SQLException e) {
			logger.error("Exception: " + e);

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);

			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				logger.error("Exception: " + see);
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}
	}

	public void editOrgPriv(int orgId, String orgType, String[] groupNames, int[] mappingId, int[] privCount, RolePrivilegeBean rolePrivilegeBean, Map<Integer, String> sleService, List<Integer> serviceDeliveryList) throws LMSException {
		Connection con = DBConnect.getConnection();
		int roleId = 0, userId = 0, userHead = 0;
		String isRoleMaster = null, isUserHead = null;
		String updateQurery = null;
		String userName = null;
		StringBuilder grpName = null;
		String grpNameStr = null;
		int privIdFrm = 0;
		int privIdTo = 0;
		String activeMapIds = "";
		HashMap<Integer, String> privMap = null;

		String fetchPrivTable = null;
		ResultSet fetchPrivTabRS = null;
		String updateRolePriv = null;
		Statement stmtMappingId = null;
		Statement fetchChildren = null;
		try {
			Statement stmt = con.createStatement();
			Statement stmtUpdate = con.createStatement();
			Statement stmtUpdateOther = con.createStatement();
			con.setAutoCommit(false);

			String qurery = "select roletable.role_id,roletable.is_master,usertable.user_id,usertable.user_name,usertable.isrolehead from (select user_name,user_id,isrolehead,role_id from st_lms_user_master where organization_id = "
					+ orgId
					+ ") as usertable, (select role_id,is_master from st_lms_role_master where (tier_id=(select tier_id from st_lms_tier_master where tier_code='"
					+ orgType
					+ "') and is_master='Y') or (owner_org_id = "
					+ orgId
					+ ")) as roletable where usertable.role_id = roletable.role_id";

			ResultSet roleTierId = stmt.executeQuery(qurery);

			while (roleTierId.next()) {
				roleId = roleTierId.getInt("role_id");
				userId = roleTierId.getInt("user_id");
				userName = roleTierId.getString("user_name");
				isRoleMaster = roleTierId.getString("is_master");
				isUserHead = roleTierId.getString("isrolehead");

				if (isRoleMaster.equalsIgnoreCase("Y")
						&& isUserHead.equalsIgnoreCase("Y")) {
					userHead = userId;
					for (int smId : mappingId) {
						String qry = "select priv_rep_table from st_lms_service_delivery_master where service_delivery_master_id = " + smId;
						Statement st = con.createStatement();
						ResultSet rs = st.executeQuery(qry);
						String privTable = null;
						while(rs.next()){
							privTable = rs.getString("priv_rep_table");
						}
						updateQurery = "update st_lms_user_priv_mapping set status='INACTIVE' where user_id="
								+ userId
								+ " and role_id="
								+ roleId
								+ " and service_mapping_id=" + smId
								+ " and priv_id in (select distinct priv_id from "
								+ privTable + " where not hidden <=> 'Y')";
						logger.debug("for inactive  head user" + updateQurery);
						stmtUpdate.executeUpdate(updateQurery);
					}

					privMap = new HashMap<Integer, String>();
					for (int i = 0; i < mappingId.length; i++) {
						if (privCount[i] != 0) {
							grpName = new StringBuilder("'Miscellaneous',");
							privIdTo = privIdTo + privCount[i];
							for (int j = privIdFrm; j < privIdTo; j++) {
								grpName.append("'" + groupNames[j] + "',");
								privIdFrm++;
							}
							grpNameStr = grpName.substring(0,
									grpName.length() - 1);
							activeMapIds = activeMapIds + mappingId[i] + ",";
							privMap.put(mappingId[i], grpNameStr);
						}
						logger.debug("**" + privCount[i]);
					}
					logger.debug("privMap*****" + privMap);

					stmtMappingId = con.createStatement();
					if(!activeMapIds.equalsIgnoreCase("")){
					fetchPrivTable = "select service_delivery_master_id,priv_rep_table from st_lms_service_delivery_master where service_delivery_master_id in("
							+ activeMapIds.substring(0,
									activeMapIds.length() - 1) + ")";
					logger.debug("*****" + fetchPrivTable);
					fetchPrivTabRS = stmtMappingId.executeQuery(fetchPrivTable);

					while (fetchPrivTabRS.next()) {
						updateRolePriv = "update st_lms_user_priv_mapping set status='ACTIVE' where user_id="
								+ userId
								+ " and role_id="
								+ roleId
								+ " and service_mapping_id="
								+ fetchPrivTabRS
										.getInt("service_delivery_master_id")
								+ " and priv_id in (select distinct(priv_id) from "
								+ fetchPrivTabRS.getString("priv_rep_table")
								+ " pr where group_name in ("
								+ privMap.get(fetchPrivTabRS
										.getInt("service_delivery_master_id"))
								+ ") and pr.status='ACTIVE') ";
						logger.debug("for active  " + updateRolePriv);
						stmtUpdate.executeUpdate(updateRolePriv);
					}
				}
				} else {
					for (int smId : mappingId) {
						updateQurery = "update st_lms_user_priv_mapping upm, (select priv_id from st_lms_user_priv_mapping where user_id="
								+ userHead
								+ " and service_mapping_id="
								+ smId
								+ " and status ='INACTIVE') temp set upm.status='INACTIVE' where user_id="
								+ userId
								+ "  and service_mapping_id="
								+ smId
								+ "  and upm.priv_id=temp.priv_id";
						logger.debug("for inactive other user " + updateQurery);
						stmtUpdateOther.executeUpdate(updateQurery);
					}

				}
				new SetResetUserPasswordAction().logOutRet(userName);
			}

			String fetchChildrensQuery = "select user_id,user_name,isrolehead from st_lms_user_master where organization_id="
					+ orgId;
			fetchChildren = con.createStatement();
			ResultSet rsChildrenFeching = fetchChildren
					.executeQuery(fetchChildrensQuery);
			Map<Integer, String> childIdMap = new HashMap<Integer, String>();
			int parentUserId = 0;
			while (rsChildrenFeching.next()) {
				if ("Y".equalsIgnoreCase(rsChildrenFeching
						.getString("isrolehead"))) {
					parentUserId = rsChildrenFeching.getInt("user_id");
				} else {
					childIdMap.put(rsChildrenFeching.getInt("user_id"), rsChildrenFeching.getString("user_name"));
				}
			}

			String fetchPriIdQuery = "select priv_id,service_mapping_id,status from st_lms_user_priv_mapping "
					+ "where user_id=" + parentUserId;

			rsChildrenFeching = fetchChildren.executeQuery(fetchPriIdQuery);
			Map<Integer, List<Integer>> privSerActiveMap = new HashMap<Integer, List<Integer>>();
			Map<Integer, List<Integer>> privSerINActiveMap = new HashMap<Integer, List<Integer>>();
			while (rsChildrenFeching.next()) {
				List<Integer> tempPrivList = null;
				if ("ACTIVE".equalsIgnoreCase(rsChildrenFeching
						.getString("status"))) {
					if (privSerActiveMap.containsKey(rsChildrenFeching
							.getInt("service_mapping_id"))) {
						tempPrivList = privSerActiveMap.get(rsChildrenFeching
								.getInt("service_mapping_id"));
					} else {
						tempPrivList = new ArrayList<Integer>();
					}
					tempPrivList.add(rsChildrenFeching.getInt("priv_id"));
					privSerActiveMap.put(rsChildrenFeching
							.getInt("service_mapping_id"), tempPrivList);
				} else if ("INACTIVE".equalsIgnoreCase(rsChildrenFeching
						.getString("status"))) {
					if (privSerINActiveMap.containsKey(rsChildrenFeching
							.getInt("service_mapping_id"))) {
						tempPrivList = privSerINActiveMap.get(rsChildrenFeching
								.getInt("service_mapping_id"));
					} else {
						tempPrivList = new ArrayList<Integer>();
					}
					tempPrivList.add(rsChildrenFeching.getInt("priv_id"));
					privSerINActiveMap.put(rsChildrenFeching
							.getInt("service_mapping_id"), tempPrivList);
				}
			}
			logger.debug("Active MAP   :" + privSerActiveMap + "\n\n\n"
					+ "INactive MAP:  " + privSerINActiveMap + "\n\n");
			for (int childId : childIdMap.keySet()) {
				//int childId = childIdMap.get(i);
				Iterator<Map.Entry<Integer, List<Integer>>> iter = privSerINActiveMap
						.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<Integer, List<Integer>> pair = (Map.Entry<Integer, List<Integer>>) iter
							.next();
					String childStatusFetchquery = "update st_lms_user_priv_mapping set status='NA' where user_id="
							+ childId
							+ " and role_id="
							+ roleId
							+ " and priv_id in ("
							+ pair.getValue().toString().replace(", ", ",")
									.replace("[", "").replace("]", "")
							+ ") and service_mapping_id=" + pair.getKey();

					logger.debug("NA Subret query**************"
							+ childStatusFetchquery);
					fetchChildren.executeUpdate(childStatusFetchquery);

				}

				iter = privSerActiveMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<Integer, List<Integer>> pair = (Map.Entry<Integer, List<Integer>>) iter
							.next();
					String childStatusFetchquery = "update st_lms_user_priv_mapping set status='INACTIVE' where user_id="
							+ childId
							+ " and status='NA' and role_id="
							+ roleId
							+ " and priv_id in ("
							+ pair.getValue().toString().replace(", ", ",")
									.replace("[", "").replace("]", "")
							+ ") and service_mapping_id=" + pair.getKey();

					logger.debug("INACTIVE Subret query**************"
							+ childStatusFetchquery);
					fetchChildren.executeUpdate(childStatusFetchquery);
				}
				new SetResetUserPasswordAction().logOutRet(userName);
			}
			
			boolean flag = true;
			
			if (sleService != null && sleService.size() > 0) {
				privIdTo = 0; privIdFrm = 0;
				int iLoop = 0;
				List<String> sleGroupName = new ArrayList<String>();
				for (Integer iValue : serviceDeliveryList) {
					privIdTo += privCount[iLoop];
					if (sleService.containsKey(iValue)) {
						for (int jLoop = privIdFrm; jLoop < privIdTo; jLoop++) {
							sleGroupName.add(groupNames[jLoop]);
						}
						for (PrivilegeDataBean privilegeDataBean : rolePrivilegeBean.getPrivilegeList()) {
							if (sleService.get(iValue).equals(privilegeDataBean.getInterfaceDevName())) {
								for (MenuDataBean menuDataBean : privilegeDataBean.getMenuList()) {
									for (MenuItemDataBean menuItemDataBean : menuDataBean.getMenuItems()) {
										if (sleGroupName.contains(menuItemDataBean.getMenuItemName())) {
											menuItemDataBean.setIsAssign(true);
										} else {
											menuItemDataBean.setIsAssign(false);
										}
									}
								}
							}
						}
					} else
						privIdFrm += privCount[iLoop];
					iLoop++;
				}
				
				try {
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.UPDATE_RETAILER_PRIVILEGES, rolePrivilegeBean);
					notifySLE.asyncCall(notifySLE);
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
					logger.debug("Error in Edit Role.");
					throw new SQLException("Error in Edit Role.");
				}
			}
			
			if(flag)
				con.commit();
			else
				con.rollback();
			
			con.commit();
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("Error During Rollback", e);
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				logger.error("Exception: " + see);
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}

	}

	// changes will be made in this file
	public void editUserPriv(String userName, String[] groupNames,
			int[] mappingId, int[] privCount, List<PrivilegeDataBean> privilegeListSLE, int doneByUserId, String requestIp) throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		int userId = 0, roleId = 0, userOrgId = 0;
		try {
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();
			ResultSet userRs = stmt
					.executeQuery("select user_id,role_id,organization_id,organization_type from st_lms_user_master where user_name='"
							+ userName + "'");
			while (userRs.next()) {
				userId = userRs.getInt("user_id");
				roleId = userRs.getInt("role_id");
				userOrgId = userRs.getInt("organization_id");
			}

			/*	Insert User Priviledge Data in History Start	*/
			String insertHistoryData = "INSERT INTO st_lms_user_priv_history (user_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip) SELECT user_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip FROM st_lms_user_priv_mapping WHERE role_id="+roleId+" AND user_id="+userId+";";
			logger.info("insertHistoryData - "+insertHistoryData);
			stmt.executeUpdate(insertHistoryData);
			/*	Insert User Priviledge Data in History End		*/

			String currentTime = Util.getCurrentTimeString();
			for (int element : mappingId) {
				String updateUserPriv = "update st_lms_user_priv_mapping set status='INACTIVE', change_date='"+currentTime+"', change_by="+doneByUserId+", request_ip='"+requestIp+"' where user_id="
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
						grpName.append(groupNames[j].contains("|") ? "'" + groupNames[j].substring(0, groupNames[j].lastIndexOf("|")) + "'," : "'" + groupNames[j] + "'," );
							//grpName.append("'" + groupNames[j].substring(0, groupNames[j].lastIndexOf("|")) + "',");
						
						privIdFrm++;
					}
					grpNameStr = grpName.substring(0, grpName.length() - 1);
					activeMapIds = activeMapIds + mappingId[i] + ",";
					privMap.put(mappingId[i], grpNameStr);
				}
				logger.debug("**" + privCount[i]);
				strMappingId.append(mappingId[i] + ",");

			}
			strMappingId.deleteCharAt(strMappingId.length() - 1);

			logger.debug("privMap*****" + privMap);

			String fetchPrivTable = null;
			ResultSet fetchPrivTabRS = null;
			String updateRolePriv = null;
			Statement stmtMappingId = con.createStatement();

			fetchPrivTable = "select service_delivery_master_id,priv_rep_table from st_lms_service_delivery_master where service_delivery_master_id in("
					+ strMappingId.toString() + ")";
			logger.debug("*****" + fetchPrivTable);
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
				logger.debug(updateRolePriv);
				stmt.executeUpdate(updateRolePriv);
			}

			boolean flag = true;
			if(ServicesBean.isSLE()) {
				if(privilegeListSLE != null && privilegeListSLE.size() > 0) {
					flag = false;
					for(PrivilegeDataBean privilegeBean : privilegeListSLE) {
						for(MenuDataBean menuBean : privilegeBean.getMenuList()) {
							for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
								menuItemBean.setIsAssign(false);
								for(String privilege : groupNames) {
									if(privilege.contains("Sports Lottery"))
									{
									if(privilege.substring(0, privilege.lastIndexOf("|")).equals(menuItemBean.getMenuItemName())) {
										menuItemBean.setIsAssign(true);
									}
									}
								}
							}
						}
					}

					try {
						SubUserDataBean userDataBean = new SubUserDataBean();
						userDataBean.setUserId(userId);
						userDataBean.setCreatorUserId(doneByUserId);
						userDataBean.setRequestIp(requestIp);
						userDataBean.setPrivilegeList(privilegeListSLE);
						NotifySLE notifySLE = new NotifySLE(SLE.Activity.SUB_USER_EDIT, userDataBean);
						notifySLE.asyncCall(notifySLE);
						flag = true;
					} catch (Exception ex) {
						ex.printStackTrace();
						flag = false;
						logger.debug("Error in Edit Role.");
						throw new SQLException("Error in Edit Role.");
					}
				}
			}

			if(flag) {
				con.commit();
			} else {
				con.rollback();
				throw new LMSException("Error Occured.");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);

			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				logger.error("Exception: " + see);
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}
	}

	public Map fetchChildPriv(int privId, Connection con) throws SQLException {
		Map childMap = new HashMap();
		Statement stmt = con.createStatement();
		String privTitle = null;
		String getChildPriv = "select pid,priv_title from priviledge_rep where parent_priv_id="
				+ privId;
		ResultSet rs = stmt.executeQuery(getChildPriv);
		while (rs.next()) {
			privId = rs.getInt("pid");
			privTitle = rs.getString("priv_title");
			childMap.put(privTitle, fetchChildPriv(privId, con));

		}
		return childMap;
	}

	public Map fetchGroupPriv(String groupName, Connection con)
			throws SQLException {
		Map groupMap = new HashMap();
		Statement stmt = con.createStatement();
		String privTitle = null;
		int privId = 0;
		String getPriv = "select distinct(priv_id),priv_title,related_to from priviledge_rep where group_name='"
				+ groupName
				+ "' and parent_priv_id=0 order by related_to,priv_id";
		ResultSet rs = stmt.executeQuery(getPriv);
		while (rs.next()) {
			privId = rs.getInt("priv_id");
			privTitle = rs.getString("priv_title");

			groupMap.put(privTitle, fetchChildPriv(privId, con));

		}
		return groupMap;
	}

	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> getBOMasPriviledges(
			String userName, int roleHeadId, List<PrivilegeDataBean> privilegeList) throws LMSException {

		 
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
			logger.debug("fetchService====" + fetchService);
			ResultSet serviceRS = serviceStmt.executeQuery(fetchService);
			ResultSet privRS = null;
			TreeMap<String, TreeMap<String, List<userPrivBean>>> interfaceMap = null;
			while (serviceRS.next()) {
				rolePrivQuery = "select distinct(group_name),upm.status,pr.related_to from "
						+ serviceRS.getString("priv_rep_table")
						+ " as pr ,st_lms_role_priv_mapping as rpm, st_lms_user_priv_mapping upm where upm.user_id="
						+ userId
						+ " and  (upm.status='ACTIVE') and upm.priv_id=rpm.priv_id and (rpm.service_mapping_id="
						+ serviceRS.getString("id")
						+ " and upm.service_mapping_id="
						+ serviceRS.getString("id")
						+ ") and (rpm.role_id="
						+ roleId
						+ " and upm.role_id="
						+ roleId
						+ ") and  rpm.priv_id=pr.priv_id order by related_to,group_name";
				logger.debug("rolePrivQuery====" + rolePrivQuery);
				
				
				String rolePrivQuery2 = "select distinct(group_name),upm.status,pr.related_to from "
					+ serviceRS.getString("priv_rep_table")
					+ " as pr ,st_lms_role_priv_mapping as rpm, st_lms_user_priv_mapping upm where upm.user_id="
					+ roleHeadId
					+ " and  (upm.status='ACTIVE')  and rpm.status = 'ACTIVE' and pr.status = 'ACTIVE'  and upm.priv_id=rpm.priv_id and (rpm.service_mapping_id="
					+ serviceRS.getString("id")
					+ " and upm.service_mapping_id="
					+ serviceRS.getString("id")
					+ ") and (rpm.role_id="
					+ roleId
					+ " and upm.role_id="
					+ roleId
					+ ") and  rpm.priv_id=pr.priv_id order by related_to,group_name";
			logger.debug("rolePrivQuery====" + rolePrivQuery2);
			privRS = privStmt.executeQuery(rolePrivQuery);
			Map<String,String> rolePrivMap = new HashMap<String, String>();
			while (privRS.next()) {
				rolePrivMap.put(privRS.getString("group_name"), privRS.getString("status"));
			}
			
			
				privRS = privStmt.executeQuery(rolePrivQuery2);
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
					relatedTo = privRS.getString("related_to");
					privBean = new userPrivBean();
					// privBean.setPid(Privrs.getInt("pid"));
					String grpName = privRS.getString("group_name");
					privBean.setPrivTitle(grpName);
					logger.debug("Prin Bean*******************: "
							+ privBean.getPrivTitle());
					
					String status = rolePrivMap.get(grpName);
					
					if(status == null){
						privBean.setStatus("INACTIVE");
					} else {
						privBean.setStatus(privRS.getString("status"));
					}
					privBean.setPrivRelatedTo(relatedTo);
					// logger.debug(privRS.getString("group_name")+"***"+privRS.getString("status"));
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
									"BO: Role Head Registration")
							&& !privRS.getString("group_name").equals(
									"Role Head Registration")	
							&& !privRS.getString("group_name").equals(
									"Add Sub User")
							&& !privRS.getString("group_name").equals(
									"Edit Sub User Privileges")
							&& !privRS.getString("group_name").equals(
									"User Priviledge Report")) {
						
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

			if(ServicesBean.isSLE() && headPriviledgeMap.get("Sports Lottery") != null) {
				RolePrivilegeBean roleBean = null;
				try {
					roleBean = new RolePrivilegeBean();
					roleBean.setUserId(userId);
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.GET_USER_PRIVILEGES, roleBean);
					roleBean = (RolePrivilegeBean) notifySLE.asyncCall(notifySLE);
					List<PrivilegeDataBean> tempList = roleBean.getPrivilegeList();
					privilegeList.addAll(tempList);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				SLEUtils.editSLEPrivileges(headPriviledgeMap, privilegeList);
			}

			logger.debug("*****headPriviledgeMap****\n" + headPriviledgeMap
					+ "\n********");
			return headPriviledgeMap;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
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

	public Map<String, TreeMap<String, TreeMap<String, List<String>>>> getHeadsGroupNames(
			int userId, int roleId, int userOrgId) throws LMSException {

		Map<String, TreeMap<String, TreeMap<String, List<String>>>> headPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<String>>>>();

		 
		Connection con = DBConnect.getConnection();
		try {
			Statement privStmt = con.createStatement();
			Statement serviceStmt = con.createStatement();
			// get heads priviledges from role_priv_mapping table
			String rolePrivQuery = null;
			// String fetchService = "select
			// srm.id,role_id,service_display_name,priv_rep_table,interface
			// from st_lms_service_role_mapping srm,st_lms_service_master sm
			// where srm.status='ACTIVE' and srm.role_id="+roleId+" and
			// sm.status='ACTIVE' and srm.service_id=sm.service_id";
			String fetchService = "select srm.id,role_id,interface,service_display_name,priv_rep_table from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id="
					+ roleId
					+ " and organization_id="
					+ userOrgId
					+ " and srm.status='ACTIVE' and sm.status='ACTIVE' and sdm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id";
			logger.debug("fetchService====" + fetchService);
			ResultSet serviceRS = serviceStmt.executeQuery(fetchService);
			ResultSet privRS = null;
			TreeMap<String, TreeMap<String, List<String>>> interfaceMap = null;
			while (serviceRS.next()) {
				rolePrivQuery = "select distinct(pr.group_name),pr.related_to from st_lms_user_priv_mapping upm,"
						+ serviceRS.getString("priv_rep_table")
						+ " as pr,(select distinct(upm.priv_id) from st_lms_role_priv_mapping rpm,st_lms_user_priv_mapping upm where upm.user_id="
						+ userId
						+ " and upm.role_id="
						+ roleId
						+ " and rpm.status='ACTIVE' and upm.status='ACTIVE'and upm.role_id=rpm.role_id and upm.service_mapping_id="
						+ serviceRS.getInt("id")
						+ ") result where upm.user_id="
						+ userId
						+ " and upm.priv_id=pr.priv_id and pr.status='ACTIVE' and result.priv_id=pr.priv_id order by related_to,group_name";
				logger.debug("rolePrivQuery====" + rolePrivQuery);
				privRS = privStmt.executeQuery(rolePrivQuery);
				String relatedTo = "";
				List<String> groupNameList = null;
				if (!headPriviledgeMap.containsKey(serviceRS
						.getString("service_display_name"))) {
					interfaceMap = new TreeMap<String, TreeMap<String, List<String>>>();
				}
				TreeMap<String, List<String>> privMap = new TreeMap<String, List<String>>();
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
									"BO: Role Head Registration")
							&& !privRS.getString("group_name").equals(
									"Add Sub User")
							&& !privRS.getString("group_name").equals(
									"Role Head Registration")
							&& !privRS.getString("group_name").equals(
									"Edit Sub User Privileges")
							&& !privRS.getString("group_name").equals(
									"User Priviledge Report")) {
						if (!relatedTo.equals(privRS.getString("related_to"))) {
							groupNameList = new ArrayList<String>();
							relatedTo = privRS.getString("related_to");
							privMap.put(relatedTo, groupNameList);
						}
						groupNameList.add(privRS.getString("group_name"));
					}
				}
				interfaceMap.put(serviceRS.getString("interface") + "-"
						+ serviceRS.getString("id"), privMap);
				headPriviledgeMap.put(serviceRS
						.getString("service_display_name"), interfaceMap);
			}

			return headPriviledgeMap;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public Map<String, List<userPrivBean>> getHeadsPriviledges(int userId)
			throws LMSException {

		Map<String, List<userPrivBean>> headPriviledgeMap = new HashMap<String, List<userPrivBean>>();

		 
		Connection con = DBConnect.getConnection();
		try {
			Statement privStmt = con.createStatement();

			// get heads priviledges from role_priv_mapping table
			String rolePrivQuery = "select pr.pid,pr.priv_title,pr.related_to from user_priv_master upm,priviledge_rep as pr where upm.user_id="
					+ userId
					+ " and upm.priviledge_rep=pr.pid and upm.status='ACTIVE' order by related_to,item_order";

			ResultSet Privrs = privStmt.executeQuery(rolePrivQuery);

			userPrivBean privBean;
			String oldRelatedTo = "";
			List<userPrivBean> titleList = null;
			while (Privrs.next()) {
				if (!Privrs.getString("group_name").equals("Miscellaneous")
						&& !Privrs.getString("group_name")
								.equals("Create Role")
						&& !Privrs.getString("group_name").equals("Edit Role")
						&& !Privrs.getString("group_name").equals(
								"Add Agent Sub user")
						&& !Privrs.getString("group_name").equals(
								"Edit Agent Sub User Priviledges")
						&& !Privrs.getString("Priv_title").equals(
								"Add BO Sub user")
						&& !Privrs.getString("Priv_title").equals(
								"Edit BO Sub User Priviledges")
						&& !Privrs.getString("Priv_title").equals(
								"User Priviledge Report")) {
					String relatedTo = Privrs.getString("related_to");
					privBean = new userPrivBean();
					privBean.setPid(Privrs.getInt("pid"));
					privBean.setPrivTitle(Privrs.getString("Priv_title"));
					privBean.setPrivRelatedTo(Privrs.getString("related_to"));

					if ("".equals(oldRelatedTo)) {
						logger.info("first time ");
						titleList = new ArrayList<userPrivBean>();
						titleList.add(privBean);
						oldRelatedTo = relatedTo;
					} else if (!relatedTo.equals(oldRelatedTo)) {
						headPriviledgeMap.put(oldRelatedTo, titleList);
						logger.debug(oldRelatedTo + ":: " + titleList.size());
						titleList = new ArrayList<userPrivBean>();
						titleList.add(privBean);
						oldRelatedTo = relatedTo;
					} else {
						titleList.add(privBean);
						oldRelatedTo = relatedTo;
					}
					if (Privrs.isLast()) {
						headPriviledgeMap.put(oldRelatedTo, titleList);
						logger.debug(oldRelatedTo + ":: " + titleList.size());
					}
				}
				// headPriviledgeMap.put(oldRelatedTo, titleList);
				// logger.debug(headPriviledgeMap);
			}

			return headPriviledgeMap;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> getOrgGroupNames(
			int orgId, String orgType) throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		int roleId = 0, tierId = 0;

		// ArrayList<userPrivBean> userPrivList=new ArrayList<userPrivBean>();
		Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> headPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>>();
		try {
			Statement stmt = con.createStatement();
			// int userId=getUserIdByuserName(userName);
			// logger.debug("user id is sss " + userId + "user name is " +
			// userName );
			String qurery = "select role_id,tier_id from st_lms_role_master where tier_id=(select tier_id from st_lms_tier_master where tier_code='"
					+ orgType + "') and is_master='Y'";
			ResultSet roleTierId = stmt.executeQuery(qurery);

			while (roleTierId.next()) {
				roleId = roleTierId.getInt("role_id");
				tierId = roleTierId.getInt("tier_id");
			}

			Statement privStmt = con.createStatement();
			Statement serviceStmt = con.createStatement();

			String rolePrivQuery = null;
			// String fetchService = "select
			// srm.id,role_id,service_display_name,priv_rep_table,interface
			// from st_lms_service_role_mapping srm,st_lms_service_master sm
			// where srm.status='ACTIVE' and srm.role_id="+roleId+" and
			// sm.status='ACTIVE' and srm.service_id=sm.service_id";
			String fetchService = "select sdm.service_delivery_master_id,sdm.priv_rep_table,sdm.interface,sm.service_display_name from st_lms_service_delivery_master sdm,st_lms_service_master sm,st_lms_service_role_mapping srm where sdm.service_id=sm.service_id and sm.status='ACTIVE' and sdm.status='ACTIVE' and tier_id="
					+ tierId
					+ " and srm.role_id="
					+ roleId
					+ " and srm.organization_id="
					+ orgId
					+ " and srm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id";
			ResultSet serviceRS = serviceStmt.executeQuery(fetchService);
			ResultSet privRS = null;
			TreeMap<String, TreeMap<String, List<userPrivBean>>> interfaceMap = null;
			while (serviceRS.next()) {
				rolePrivQuery = "select distinct(group_name),upm.status,pr.related_to from "
						+ serviceRS.getString("priv_rep_table")
						+ " as pr ,st_lms_role_priv_mapping as rpm, st_lms_user_priv_mapping upm where upm.user_id=(select user_id from st_lms_user_master where organization_id="
						+ orgId
						+ " and isrolehead='Y' and role_id ="
						+ roleId
						+ ") and upm.priv_id=rpm.priv_id and rpm.service_mapping_id="
						+ serviceRS.getInt("service_delivery_master_id")
						+ " and rpm.role_id="
						+ roleId
						+ " and  rpm.priv_id=pr.priv_id and upm.service_mapping_id="
						+ serviceRS.getInt("service_delivery_master_id")
						+ " and upm.role_id="
						+ roleId
						+ " and pr.status='ACTIVE' and not pr.hidden <=> 'Y' order by related_to,group_name";
				logger.debug(rolePrivQuery);
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
						+ serviceRS.getString("service_delivery_master_id"),
						privMap);
				headPriviledgeMap.put(serviceRS
						.getString("service_display_name"), interfaceMap);
			}
			logger.debug("*****headPriviledgeMap****\n" + headPriviledgeMap
					+ "\n********");
			if ("RETAILER".equals(orgType)) {
				if (ServicesBean.isSLE() && headPriviledgeMap.get("Sports Lottery") != null) {
					List<PrivilegeDataBean> privilegeList = new ArrayList<PrivilegeDataBean>();
					RolePrivilegeBean roleBean = null;
					try {
						roleBean = new RolePrivilegeBean();
						roleBean.setUserId(Util.fetchUserIdFormOrgId(orgId));
						NotifySLE notifySLE = new NotifySLE(SLE.Activity.GET_RETAILER_PRIVILEGES, roleBean);
						roleBean = (RolePrivilegeBean) notifySLE.asyncCall(notifySLE);
						List<PrivilegeDataBean> tempList = roleBean.getPrivilegeList();
						ServletActionContext.getRequest().getSession().setAttribute("SLEPRIV", roleBean);
						privilegeList.addAll(tempList);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					SLEUtils.editSLEPrivileges(headPriviledgeMap, privilegeList);
				}
			}
			return headPriviledgeMap;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("Error During Rollback", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
			}
		}

	}

	public String getOrgName(int orgId) throws LMSException {
		 
		Connection con = DBConnect.getConnection();
		String name = null;
		try {
			PreparedStatement ps = con
					.prepareStatement("select name from st_lms_organization_master where organization_id = "
							+ orgId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				name = rs.getString("name");
			}
		DBConnect.closeCon(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> getUserGroupNames(
			String userName) throws LMSException {

		int roleId = 0, userOrgId = 0, userId = 0;
		 
		Connection con = DBConnect.getConnection();
		// ArrayList<userPrivBean> userPrivList=new ArrayList<userPrivBean>();
		Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> headPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>>();
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
			logger.debug("fetchService====" + fetchService);
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
						+ ") and  rpm.priv_id=pr.priv_id  and pr.status='ACTIVE'  order by related_to,group_name";
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
						logger.debug("Prin Bean*******************: "
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

			logger.debug("*****headPriviledgeMap****\n" + headPriviledgeMap
					+ "\n********");
			return headPriviledgeMap;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Error During Rollback", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
			}
		}

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
			logger.debug("fetchService====" + fetchService);
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
				logger.debug("rolePrivQuery====" + rolePrivQuery);
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
						logger.debug("Prin Bean*******************: "
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

			logger.debug("*****headPriviledgeMap****\n" + headPriviledgeMap
					+ "\n********");
			return headPriviledgeMap;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
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

	public List getUsers(int roleId, int userId, int orgId) throws LMSException {
		 
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			// Statement stmt1=con.createStatement();
			// con.setAutoCommit(false);
			// get users list for editing from user and user priv mapping table

			String userQuery = "select distinct um.user_name,um.user_id from st_lms_user_master as um ,st_lms_user_priv_mapping as upm where upm.role_id="
					+ roleId
					+ " and upm.user_id=um.user_id and um.organization_id="
					+ orgId + " and um.isrolehead='N' order by um.user_name";
			logger.debug("userQuery: " + userQuery);
			ResultSet rs = stmt.executeQuery(userQuery);
			ArrayList<userListBean> userList = new ArrayList<userListBean>();
			userListBean userBean;
			while (rs.next()) {
				userBean = new userListBean();
				userBean.setUserName(rs.getString("user_name"));
				userList.add(userBean);
			}

			return userList;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sql Exception", e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public String newUserReg(String userName) throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		try {

			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select user_name from st_lms_user_master where user_name='"
							+ userName + "'");
			while (rs.next()) {
				String userNamedb = rs.getString("user_name");
				if (userNamedb.equalsIgnoreCase(userName)) {
					return "ERROR";
				}

			}
			if("admin".equalsIgnoreCase(userName)){
				return "ERROR";
			}
			
			return "SUCCESS";
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
				throw new LMSException("sqlException", se);
			}
		}

	}
	
	public Map<Integer, String> fetchSLEServiceDeliveryMasterId(int[] mappingId) {
		Map<Integer, String> sleServiceDeliveryMasterList = new HashMap<Integer, String>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String str = "";

		for (Integer i : mappingId) {
			str += i + ",";
		}
		if (str.length() > 0)
			str = str.substring(0, str.length() - 1);

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();

			rs = stmt.executeQuery("select sdm.service_delivery_master_id, sdm.interface from st_lms_service_delivery_master sdm inner join st_lms_service_master sm on sm.service_id = sdm.service_id where sm.service_code = 'SLE' and sdm.service_delivery_master_id in (" + str + ")");
			while (rs.next()) {
				sleServiceDeliveryMasterList.put(rs.getInt("service_delivery_master_id"), rs.getString("interface"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return sleServiceDeliveryMasterList;
	}

	public void test() throws SQLException {
		 
		Connection con = DBConnect.getConnection();
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		String getPriv = "select action_mapping,pid from priviledge_rep";
		ResultSet rs = stmt.executeQuery(getPriv);
		while (rs.next()) {
			stmt1.executeUpdate("update priviledge_rep set pid ="
					+ rs.getInt("pid") + " where action_mapping='"
					+ rs.getString("action_mapping") + "'");
		}

	}

	/*
	 * while (relMapItr.hasNext()) {
	 * 
	 * Map.Entry relMappair = (Map.Entry)relMapItr.next();
	 * 
	 * String related_to = (String)relMappair.getKey();
	 * 
	 * Map privMap = (HashMap)relMappair.getValue();
	 * 
	 * Iterator privMapItr = privMap.entrySet().iterator();
	 * 
	 * while (privMapItr.hasNext()) {
	 * 
	 * Map.Entry privMappair = (Map.Entry)privMapItr.next();
	 * 
	 * String parentPrivId = (String)privMappair.getKey();
	 * 
	 * List parentChildPriv = (ArrayList)privMappair.getValue();
	 * 
	 * Iterator childPrivItr = parentChildPriv.iterator();
	 * 
	 * while (childPrivItr.hasNext()) { } }
	 * 
	 * return related_to_map;
	 */

}