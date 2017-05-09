package com.skilrock.lms.coreEngine.roleMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.AutoGenerate;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.rolemgmt.beans.userListBean;
import com.skilrock.lms.rolemgmt.beans.userPrivBean;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.MenuDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.MenuItemDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RolePrivilegeBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class RoleManagementHelper

{
	static Log logger = LogFactory.getLog(RoleManagementHelper.class);

	/*
	 * public void createSuperUser(String superUser,String firstName,String
	 * lastName,String secQues,String secAns,Date dateOfBirth,String
	 * gender,String email,String addr1, String addr2,String city,long
	 * phone,long mobile) { int userId; short x=1; long postalCode=321601; Date
	 * dd= new Date(new java.util.Date().getTime()); //Date ddd=new
	 * Date(dateOfBirth.getTime()); Set igsInfo = new HashSet();
	 * 
	 * 
	 * Session session=HibernateSessionFactory.getSession(); Transaction
	 * tx=null; tx=session.beginTransaction();
	 * 
	 * IgsRoleMaster roleMas=new IgsRoleMaster(); // entry into igs role master
	 * roleMas.setRoleName("SUP_USER"); session.save(roleMas);
	 * logger.debug(roleMas.getRoleId()); // insert into igs user master
	 * 
	 * IgsUserMaster ium=new IgsUserMaster(); ium.setIsrolehead(x);
	 * ium.setPassword(MD5Encoder.encode(AutoGenerate.autoPassword()));
	 * ium.setRegistrationDate(new java.sql.Timestamp(dd.getTime()));
	 * ium.setSecAns(secAns); ium.setSecQues(secQues); ium.setStatus("ACTIVE");
	 * ium.setUserName(superUser); // insert into user info table
	 * 
	 * IgsUserInfo iui=new IgsUserInfo(); IgsUserInfoId iuiId=new
	 * IgsUserInfoId(); ium.setRoleId(roleMas.getRoleId()); /// this is added
	 * later iuiId.setIgsUserMaster(ium);
	 * 
	 * iuiId.setAddressLine1(addr1); iuiId.setAddressLine2(addr2);
	 * iuiId.setCity(city); iuiId.setCountryCode("1");
	 * iuiId.setDateOfBirth(dateOfBirth); iuiId.setEmailId(email);
	 * iuiId.setFirstName(firstName); iuiId.setLastName(lastName);
	 * iuiId.setGender(gender); iuiId.setMobileNum(mobile);
	 * iuiId.setPhoneNum(phone); iuiId.setPostalCode(postalCode);
	 * iuiId.setStateCode("fr");
	 * 
	 * iui.setId(iuiId); igsInfo.add(iui); ium.setIgsUserInfos(igsInfo);
	 * iui.setIgsUserMaster(ium);
	 * 
	 * 
	 * 
	 * 
	 * session.save(ium); session.save(iui); userId=ium.getUserId(); /* // entry
	 * into user role mapping userRoleMap.setRoleId(roleMas.getRoleId());
	 * userRoleMap.setRoleHead("YES"); userRoleMap.setUserId(userId);
	 * userRoleMap.setUserName("superUser"); session.save(userRoleMap);
	 */
	/*
	 * String query="select new "+PriviledgeBeanRole.class.getName()+"(pr.pid)
	 * from PriviledgeRep as pr"; Query q=session.createQuery(query); List<PriviledgeBeanRole>
	 * list =q.list(); // insert into role priviledges table
	 * 
	 * RolePrivlMapping rolePrivMap=new RolePrivlMapping(); //IgsRoleMaster
	 * irm=new IgsRoleMaster(); RolePrivlMappingId rpmId=new
	 * RolePrivlMappingId();
	 * 
	 * for(int i=0;i<list.size();i++) { // irm.setRoleId(roleMas.getRoleId());
	 * rpmId.setIgsRoleMaster(roleMas); logger.debug("inside for");
	 * rpmId.setPid(list.get(i).getPid());
	 * 
	 * rolePrivMap.setId(rpmId); // rolePrivMap.setRoleId(roleMas.getRoleId()); //
	 * rolePrivMap.setPid(list.get(i).getPid()); //
	 * rolePrivMap.setRoleId(roleMas.getRoleId());
	 * logger.debug("111111111111111111111111111111111111111");
	 * session.save(rolePrivMap); // session.flush(); //session.clear();
	 * logger.debug("1111111111111111111111111111111111"); }
	 * logger.debug("22222222222222"); // insert into user priv master table
	 * UserPrivMaster upm=new UserPrivMaster(); UserPrivMasterId upmId=new
	 * UserPrivMasterId(); for(int i=0;i<list.size();i++) {
	 * 
	 * logger.debug("inside for");
	 * 
	 * 
	 * upmId.setPid(list.get(i).getPid()); upmId.setRoleId(roleMas.getRoleId()) ;
	 * upmId.setStatus("ACTIVE"); upmId.setUserId(userId);
	 * 
	 * upm.setId(upmId); session.save(upm); //session.flush(); //
	 * session.clear(); }
	 * 
	 * tx.commit(); }
	 */

	public static void main(String[] args) throws SQLException, LMSException {
		RoleManagementHelper rmh = new RoleManagementHelper();
		String isNewService="No";
		/*
		 * rmh.newPrivForMaster("AGENT"); rmh.newPrivForMaster("BO");
		 * rmh.newPrivForMaster("RETAILER");
		 */
		// logger.debug(rmh.getHeadsGroupNames(1,1,1));
		// logger.debug(rmh.fetchRoles(1, "BO",1));
		// logger.debug(rmh.fetchRolePriv(1,"BO",1,1));
		/*
		 * String rolePriv[]={"Close Games","Game Details","New Game"}; int
		 * mappingId[]={7,1}; int privCount[]={3,0};
		 */
		// rmh.createRole("TEST", "TEST ROLE","BO", rolePriv, mappingId,
		// privCount, 1, 1, 1);
		// rmh.deletePriv();
		rmh.managePriv("BO", "",isNewService);
		rmh.managePriv("AGENT", "",isNewService);
		rmh.managePriv("RETAILER", "",isNewService);
		// new menuBuilder().createMenu();
		logger.info("Done---------");

	}

	public void assignPriviledges(String userName, int[] rolePid,
			ArrayList<userPrivBean> headPrivList, int roleId,
			UserDetailsBean usrdetBean) throws SQLException {
		String projectName = ServletActionContext.getServletContext()
				.getContextPath();
		logger.debug("user name and pids are " + userName + rolePid);
		 
		Connection con = DBConnect.getConnection();

		// int userId=getUserIdByuserName(userName);
		int userId = 0;
		String password = AutoGenerate.autoPassword();
		String status = null;

		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		con.setAutoCommit(false);

		// insert data into st user master

		stmt1
				.executeUpdate("insert into st_user_master(organization_id,role_id,organization_type,registration_date,auto_password,user_name,password,status,secret_ques,secret_ans,isrolehead) values("
						+ usrdetBean.getOrgId()
						+ ","
						+ usrdetBean.getRoleId()
						+ ",'"
						+ usrdetBean.getOrgType()
						+ "',CURRENT_DATE,1,'"
						+ usrdetBean.getUserName().trim()
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
		stmt1
				.executeUpdate("INSERT into st_user_contact_details(user_id,first_name,last_name,email_id,phone_nbr) VALUES('"
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
						+ "',CURRENT_DATE,'1')");

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

			insertintoUserPrivMapQuery = "insert into st_lms_user_priv_mapping values("
					+ userId
					+ ","
					+ roleId
					+ ","
					+ headPrivList.get(i).getPid() + ",'" + status + "')";

			// logger.debug("query to insert is : " + "insert into
			// st_lms_user_priv_mapping
			// values("+userId+","+roleId+","+headPrivList.get(i).getPid()+",'"+status+"')");
			stmt.executeUpdate(insertintoUserPrivMapQuery);

			// con.commit();
		}
		con.commit();
		if (con != null) {
			con.close();
		}
		String msgFor = "Thanks for registration to our gaming system  Your Login details are";
		// MailSend.sendMailToUser(usrdetBean.getEmailId(),
		// password,usrdetBean.getUserName());
		String emailMsgTxt = "<html><table><tr><td>Hi "
				+ usrdetBean.getFirstName() + " " + usrdetBean.getLastName()
				+ "</td></tr><tr><td>" + msgFor
				+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
				+ usrdetBean.getUserName()
				+ "</td></tr><tr><td>password :: </td><td>"
				+ password.toString() + "</td></tr><tr><td>log on </td><td>"
				+ LMSFilterDispatcher.webLink + "/"
				+ LMSFilterDispatcher.mailProjName
				+ "/</td></tr></table></html>";
		MailSend mailSend = new MailSend(usrdetBean.getEmailId(), emailMsgTxt);
		mailSend.setDaemon(true);
		mailSend.start();
		// MailSend.sendMailToUser(usrdetBean.getEmailId(),
		// password,usrdetBean.getUserName(),usrdetBean.getFirstName(),usrdetBean.getLastName(),msgFor);
	}

	public void chintanInsertQueryForHeadDeployment(String tierCode) {
		 
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);

			Statement tierStmt = con.createStatement();
			Statement serDelStmt = con.createStatement();
			Statement privStmt = con.createStatement();
			Statement insRPMStmt = con.createStatement();
			Statement orgStmt = con.createStatement();
			Statement userStmt = con.createStatement();
			Statement srmStmt = con.createStatement();

			ResultSet rsTier = null;
			ResultSet rsServDel = null;
			ResultSet rsPriv = null;
			ResultSet rsOrg = null;

			String selTier = "select tier_id as tierId,tier_status as tierStatus,role_id as roleId  from st_lms_tier_master tm, st_lms_role_master  rm where tm.tier_code='"
					+ tierCode
					+ "' and tm.tier_id = rm.tier_id and rm.is_master = 'Y'";
			String selServDel = null;
			String selPriv = null;
			String selOrg = "select organization_id from st_lms_organization_master where organization_type = '"
					+ tierCode + "'";
			String insSRM = null;
			String selUser = null;
			logger.debug("Org -- " + selOrg);
			rsOrg = orgStmt.executeQuery(selOrg);

			rsTier = tierStmt.executeQuery(selTier);
			while (rsTier.next()) {
				int tierId = rsTier.getInt("tierId");
				String tierStatus = rsTier.getString("tierStatus");
				int roleId = rsTier.getInt("roleId");

				selServDel = "select  dm.service_delivery_master_id as mapId, dm.status as delStatus, dm.priv_rep_table, sm.service_id as servId, sm.status as serStatus from st_lms_service_delivery_master dm, st_lms_service_master sm where dm.tier_id = "
						+ tierId + " and dm.service_id = sm.service_id";
				rsServDel = serDelStmt.executeQuery(selServDel);
				while (rsServDel.next()) {
					int mapId = rsServDel.getInt("mapId");
					int serId = rsServDel.getInt("servId");
					String delStatus = rsServDel.getString("delStatus");
					String servStatus = rsServDel.getString("serStatus");
					String privTable = rsServDel
							.getString("privilege_rep_table");
					StringBuilder insRPM = new StringBuilder(
							"insert into st_lms_role_priv_mapping values ");
					selPriv = "select distinct(priv_id), status as privStatus from "
							+ privTable
							+ " where priv_owner = '"
							+ tierCode
							+ "'";
					// selPriv="select distinct(pid), status as privStatus from
					// "+privTable+" where priv_owner = '"+tierCode+"' and pid
					// not in (select pid from st_lms_role_priv_mapping where
					// service_mapping_id="+mapId+" and role_id="+roleId+")";

					rsPriv = privStmt.executeQuery(selPriv);
					boolean status = tierStatus.equals("ACTIVE")
							&& delStatus.equals("ACTIVE")
							&& servStatus.equals("ACTIVE");
					boolean flag = false;
					while (rsPriv.next()) {
						String privStatus = rsPriv.getString("privStatus");
						int pid = rsPriv.getInt("priv_id");
						String insStatus = "INACTIVE";
						if (status && privStatus.equals("ACTIVE")) {
							insStatus = "ACTIVE";
						}
						flag = true;
						insRPM.append("(" + roleId + "," + pid + "," + mapId
								+ ",'" + insStatus + "'),");
					}
					logger.debug("************" + insRPM);
					if (flag) {
						insRPMStmt.executeUpdate(insRPM.substring(0, insRPM
								.length() - 1));

						while (rsOrg.next()) {
							// logger.debug("ORG*********"+"insert into
							// st_lms_service_role_mapping values
							// ("+mapId+","+roleId+","+rsOrg.getInt("organization_id")+","+(status?"ACTIVE":"INACTIVE")+")");
							srmStmt
									.executeUpdate("insert into st_lms_service_role_mapping values ("
											+ mapId
											+ ","
											+ roleId
											+ ","
											+ rsOrg.getInt("organization_id")
											+ ","
											+ (status ? "'ACTIVE'"
													: "'INACTIVE'") + ")");
						}
						rsOrg.beforeFirst();
					}
				}

				selUser = "insert into st_lms_user_priv_mapping select user_id,rpm.role_id,priv_id,service_mapping_id,rpm.status from st_lms_role_priv_mapping rpm,st_lms_user_master um where um.role_id="
						+ roleId
						+ " and um.organization_type='"
						+ tierCode
						+ "' and isrolehead='Y' and rpm.role_id=" + roleId + "";
				userStmt.executeUpdate(selUser);
			}
			con.commit();

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createRole(String roleName, String roleDesc, UserInfoBean userBean, String[] rolePriv,
			int[] mappingId, int[] privCount, List<PrivilegeDataBean> privilegeListSLE) throws SQLException {
		String userType = userBean.getUserType();
		int creatorRoleId = userBean.getRoleId();
		int tierId = userBean.getTierId();
		int ownerOrgId = userBean.getUserOrgId();
		
		logger.debug("role name is : " + roleName);
		String activeMapIds = "";
		StringBuilder grpName = null;
		// Miscellaneous will give all the Miscellaneous privileges
		String grpNameStr = null;
		int privIdFrm = 0;
		int privIdTo = 0;
		String updateRolePriv = null;
		HashMap<Integer, String> privMap = new HashMap<Integer, String>();
		for (int i = 0; i < mappingId.length; i++) {
			if (privCount[i] != 0) {
				grpName = new StringBuilder("'Miscellaneous',");
				privIdTo = privIdTo + privCount[i];
				for (int j = privIdFrm; j < privIdTo; j++) {
					grpName.append("'" + rolePriv[j] + "',");
					privIdFrm++;
				}
				grpNameStr = grpName.substring(0, grpName.length() - 1);
				activeMapIds = activeMapIds + mappingId[i] + ",";
				privMap.put(mappingId[i], grpNameStr);
			}
		}

		 
		Connection con = DBConnect.getConnection();
		Statement stmt = con.createStatement();
		PreparedStatement pstmt = null;
		ResultSet rs1 =null;
		Statement stmtUpdate = con.createStatement();
		con.setAutoCommit(false);
		String query1 ="select role_name from st_lms_role_master  where  role_name=? or role_description=?";
		pstmt=con.prepareStatement(query1);
		pstmt.setString(1,roleName);
		pstmt.setString(2,roleDesc);
		logger.info("check role query is ---" + pstmt);
		rs1=pstmt.executeQuery();
		if(rs1.next()){
			logger.debug("Role  Alrady Exists");
			throw new SQLException("Role  Alrady Exists With These Details");
		}
		String query = "insert into st_lms_role_master(role_name,role_description,tier_id,owner_org_id,is_master) values('"
				+ roleName
				+ "','"
				+ roleDesc
				+ "','"
				+ tierId
				+ "',"
				+ ownerOrgId + ",'N')";
		logger.info("query is ---" + query);
		stmt.executeUpdate(query);

		int roleId = 0;
		ResultSet rs = stmt.getGeneratedKeys();
		while (rs.next()) {
			roleId = rs.getInt(1);
		}
		String insertSerRolePriv = "insert into st_lms_service_role_mapping(id,role_id,organization_id,status) (select service_delivery_master_id,"
				+ roleId
				+ ","
				+ ownerOrgId
				+ ",'INACTIVE' from st_lms_service_delivery_master where  tier_id="
				+ tierId + ")";
		stmt.executeUpdate(insertSerRolePriv);

		String updateService = "update st_lms_service_role_mapping set status='ACTIVE' where id in ("
				+ activeMapIds.substring(0, activeMapIds.length() - 1) + ")";
		stmt.executeUpdate(updateService);

		String insRolePriv = "insert into st_lms_role_priv_mapping(role_id,priv_id,service_mapping_id,status) select "
				+ roleId
				+ ",priv_id,service_mapping_id,'INACTIVE' from st_lms_role_priv_mapping where role_id="
				+ creatorRoleId + "";
		stmt.executeUpdate(insRolePriv);

		String fetchPrivTable = "select service_delivery_master_id,priv_rep_table from st_lms_service_delivery_master where tier_id ="
				+ tierId + "";
		ResultSet fetchPrivTabRS = stmt.executeQuery(fetchPrivTable);
		while (fetchPrivTabRS.next()) {
			updateRolePriv = "update st_lms_role_priv_mapping set status='ACTIVE' where role_id="
					+ roleId
					+ " and service_mapping_id="
					+ fetchPrivTabRS.getInt("service_delivery_master_id")
					+ " and priv_id in (select distinct(priv_id) from "
					+ fetchPrivTabRS.getString("priv_rep_table")
					+ " pr where group_name in ("
					+ privMap.get(fetchPrivTabRS
							.getInt("service_delivery_master_id"))
					+ ") and pr.status='ACTIVE') ";
			logger.debug("updateRolePriv: " + updateRolePriv);
			stmtUpdate.executeUpdate(updateRolePriv);
		}

		boolean flag = true;
		if(ServicesBean.isSLE()) {
			if(privilegeListSLE != null && privilegeListSLE.size() > 0) {
				flag = false;
				for(PrivilegeDataBean privilegeBean : privilegeListSLE) {
					for(MenuDataBean menuBean : privilegeBean.getMenuList()) {
						for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
							menuItemBean.setIsAssign(false);
							for(String privilege : rolePriv) {
								if(privilege.equals(menuItemBean.getMenuItemName())) {
									menuItemBean.setIsAssign(true);
								}
							}
						}
					}
				}

				try {
					RolePrivilegeBean roleBean = new RolePrivilegeBean();
					roleBean.setCreatorUserId(userBean.getUserId());
					roleBean.setRoleId(roleId);
					roleBean.setRoleName(roleName);
					roleBean.setRoleDescription(roleDesc);
					roleBean.setPrivilegeList(privilegeListSLE);
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.ROLE_REGISTRATION, roleBean);
					notifySLE.asyncCall(notifySLE);
					flag = true;
				} catch (Exception ex) {
					ex.printStackTrace();
					flag = false;
					logger.debug("Error in Creating Role.");
					throw new SQLException("Error in Creating Role.");
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
		DBConnect.closeCon(con);

	}

	public void deletePriv() {
		 
		Connection con = DBConnect.getConnection();
		try {
			// con.setAutoCommit(false);

			Statement tierStmt = con.createStatement();
			tierStmt.execute("delete from st_lms_role_priv_mapping");
			tierStmt.execute("delete from st_lms_service_role_mapping");
			tierStmt.execute("delete from st_lms_user_priv_mapping");
			con.close();
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void editRolePriv(int roleId, String[] rolePriv, int[] mappingId,
			int[] privCount, UserInfoBean userBean, List<PrivilegeDataBean> privilegeListSLE, String requestIp)
			throws SQLException {

		int creatorRoleId = userBean.getRoleId();
		int tierId = userBean.getTierId();
		int ownerOrgId = userBean.getUserOrgId();

		String activeMapIds = "";
		StringBuilder grpName = null;
		// Miscellaneous will give all the Miscellaneous privileges
		String grpNameStr = null;
		int privIdFrm = 0;
		int privIdTo = 0;
		String updateRolePriv = null;
		HashMap<Integer, String> privMap = new HashMap<Integer, String>();
		for (int i = 0; i < mappingId.length; i++) {
			if (privCount[i] != 0) {
				grpName = new StringBuilder("'Miscellaneous',");
				privIdTo = privIdTo + privCount[i];
				for (int j = privIdFrm; j < privIdTo; j++) {
					grpName.append("'" + rolePriv[j] + "',");
					privIdFrm++;
				}
				grpNameStr = grpName.substring(0, grpName.length() - 1);
				activeMapIds = activeMapIds + mappingId[i] + ",";
				privMap.put(mappingId[i], grpNameStr);

			}
		}

		 
		Connection con = DBConnect.getConnection();
		Statement stmt = con.createStatement();
		Statement stmtUpdate = con.createStatement();
		con.setAutoCommit(false);

		// logger.debug(tierId+"==tier******activeid*"+activeMapIds);
		String updService = "update st_lms_service_role_mapping set status ='INACTIVE' where role_id="
				+ roleId + " and organization_id=" + ownerOrgId + "";
		stmt.executeUpdate(updService);

		String updateService = "update st_lms_service_role_mapping set status='ACTIVE' where role_id="
				+ roleId
				+ " and organization_id="
				+ ownerOrgId
				+ " and id in ("
				+ activeMapIds.substring(0, activeMapIds.length() - 1) + ")";
		stmt.executeUpdate(updateService);

		String updRolePriv = "update st_lms_role_priv_mapping set status ='INACTIVE' where role_id="
				+ roleId + "";
		stmt.executeUpdate(updRolePriv);

		String fetchPrivTable = "select service_delivery_master_id,priv_rep_table from st_lms_service_delivery_master where tier_id ="
				+ tierId + " and status='ACTIVE'";
		ResultSet fetchPrivTabRS = stmt.executeQuery(fetchPrivTable);
		while (fetchPrivTabRS.next()) {
			if (privMap
					.get(fetchPrivTabRS.getInt("service_delivery_master_id")) != null) {
				updateRolePriv = "update st_lms_role_priv_mapping set status='ACTIVE' where role_id="
						+ roleId
						+ " and service_mapping_id="
						+ fetchPrivTabRS.getInt("service_delivery_master_id")
						+ " and priv_id in (select distinct(priv_id) from "
						+ fetchPrivTabRS.getString("priv_rep_table")
						+ " pr where group_name in ("
						+ privMap.get(fetchPrivTabRS
								.getInt("service_delivery_master_id"))
						+ ") and pr.status='ACTIVE') ";
				stmtUpdate.executeUpdate(updateRolePriv);
			}
		}

		/*	Insert User Priviledge Data in History Start	*/
		String insertHistoryData = "INSERT INTO st_lms_user_priv_history (user_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip) SELECT user_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip FROM st_lms_user_priv_mapping WHERE role_id="+roleId+";";
		logger.info("insertHistoryData - "+insertHistoryData);
		stmt.executeUpdate(insertHistoryData);
		/*	Insert User Priviledge Data in History End		*/

		String currentTime = Util.getCurrentTimeString();
		String updRolePrivInActive = "update st_lms_user_priv_mapping upm,st_lms_role_priv_mapping rpm set upm.status='INACTIVE', change_date='"+currentTime+"', change_by="+userBean.getUserId()+", request_ip='"+requestIp+"' where upm.role_id="
				+ roleId
				+ " and upm.role_id=rpm.role_id and upm.service_mapping_id=rpm.service_mapping_id and upm.priv_id=rpm.priv_id and  rpm.status='INACTIVE'";
		stmt.executeUpdate(updRolePrivInActive);

		String updRoleHeadPrivActive = "update st_lms_user_priv_mapping upm,st_lms_role_priv_mapping rpm set upm.status='ACTIVE', change_date='"+currentTime+"', change_by="+userBean.getUserId()+", request_ip='"+requestIp+"' where upm.role_id="
				+ roleId
				+ " and upm.user_id in (select user_id from st_lms_user_master where isrolehead='Y') and upm.role_id=rpm.role_id and upm.service_mapping_id=rpm.service_mapping_id and upm.priv_id=rpm.priv_id and  rpm.status='ACTIVE'";
		stmt.executeUpdate(updRoleHeadPrivActive);

		boolean flag = true;
		if(ServicesBean.isSLE()) {
			if(privilegeListSLE != null && privilegeListSLE.size() > 0) {
				flag = false;
				for(PrivilegeDataBean privilegeBean : privilegeListSLE) {
					for(MenuDataBean menuBean : privilegeBean.getMenuList()) {
						for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
							menuItemBean.setIsAssign(false);
							for(String privilege : rolePriv) {
								if(privilege.equals(menuItemBean.getMenuItemName())) {
									menuItemBean.setIsAssign(true);
								}
							}
						}
					}
				}

				try {
					RolePrivilegeBean roleBean = new RolePrivilegeBean();
					roleBean.setRoleId(roleId);
					roleBean.setCreatorUserId(userBean.getUserId());
					roleBean.setRequestIp(requestIp);
					roleBean.setPrivilegeList(privilegeListSLE);
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.ROLE_EDIT, roleBean);
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

		DBConnect.closeCon(con);
	}

	public void editUserPriv(String userName, int roleId, int[] rolePriv)
			throws LMSException {
		// logger.debug("priv id is " + rolePriv[0] );

		 
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			int userId = getUserIdByuserName(userName);
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			ArrayList pidList = new ArrayList();

			String getHeadsPidsQuery = "select pid from role_privl_mapping where role_id="
					+ roleId + "";
			ResultSet rs = stmt.executeQuery(getHeadsPidsQuery);
			while (rs.next()) {
				// logger.debug("helllo");
				pidList.add(rs.getInt("pid"));

			}
			String status = null;
			for (int i = 0; i < pidList.size(); i++) {
				int headPid = (Integer) pidList.get(i);
				status = "INACTIVE";
				for (int element : rolePriv) {
					if (element == headPid) {
						// logger.debug("from jsp is " + rolePriv[j] + "from
						// back is "+ (Integer)pidList.get(i));

						status = "ACTIVE";
						break;

					}
				}
				// logger.debug("from jsp is " + rolePriv[j] + "from back is "+
				// (Integer)pidList.get(i));
				// logger.debug("status is " + status);
				String updateUserPrivMapQuery = "update st_lms_user_priv_mapping set status='"
						+ status
						+ "' where user_id="
						+ userId
						+ " and role_id="
						+ roleId
						+ " and priv_id="
						+ (Integer) pidList.get(i) + "";
				// logger.debug("query is " +updateUserPrivMapQuery );
				stmt.executeUpdate(updateUserPrivMapQuery);

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

	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> fetchRolePriv(
			int roleId, String userType, int userOrgId, int userId)
			throws LMSException {

		Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> headPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>>();

		 
		Connection con = DBConnect.getConnection();
		try {
			Statement privStmt = con.createStatement();
			Statement serviceStmt = con.createStatement();
			// get heads priviledges from role_priv_mapping table
			String rolePrivQuery = null;
			// String fetchService = "select
			// srm.id,role_id,service_display_name,privilege_rep_table,interface
			// from st_lms_service_role_mapping srm,st_lms_service_master sm
			// where srm.status='ACTIVE' and srm.role_id="+roleId+" and
			// sm.status='ACTIVE' and srm.service_id=sm.service_id";
			String fetchService = "select srm.id,role_id,interface,service_display_name,priv_rep_table,srm.status from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id="
					+ roleId
					+ " and organization_id="
					+ userOrgId
					+ " and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id and sdm.status='ACTIVE'";

			ResultSet serviceRS = serviceStmt.executeQuery(fetchService);
			ResultSet privRS = null;
			TreeMap<String, TreeMap<String, List<userPrivBean>>> interfaceMap = null;
			while (serviceRS.next()) {
				rolePrivQuery = "select distinct(group_name),rpm.status,pr.related_to from "
						+ serviceRS.getString("priv_rep_table")
						+ " as pr ,st_lms_role_priv_mapping as rpm, st_lms_user_priv_mapping upm where upm.user_id="
						+ userId
						+ " and  upm.status='ACTIVE' and upm.priv_id=rpm.priv_id and rpm.service_mapping_id="
						+ serviceRS.getString("id")
						+ " and rpm.role_id="
						+ roleId
						+ " and  rpm.priv_id=pr.priv_id  and pr.status='ACTIVE'  order by related_to,group_name";
				// logger.debug(rolePrivQuery);
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
									"BO: Role Head Registration")
							&& !privRS.getString("group_name").equals(
									"Role Head Registration")		) {
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

	public Map<Integer, String> fetchRoles(int roleId, String userType,
			int userOrgId) throws LMSException {
		Map<Integer, String> roleMap = new TreeMap<Integer, String>();

		 
		Connection con = DBConnect.getConnection();
		try {
			Statement privStmt = con.createStatement();

			// get heads priviledges from role_priv_mapping table
			String rolePrivQuery = "select role_id,role_name from st_lms_role_master where owner_org_id="
					+ userOrgId
					+ " and is_master='N' and role_id !="
					+ roleId
					+ "";
			ResultSet privRS = privStmt.executeQuery(rolePrivQuery);

			while (privRS.next()) {
				roleMap.put(privRS.getInt("role_id"), privRS
						.getString("role_name"));

			}

			return roleMap;

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
			// srm.id,role_id,service_display_name,privilege_rep_table,interface
			// from st_lms_service_role_mapping srm,st_lms_service_master sm
			// where srm.status='ACTIVE' and srm.role_id="+roleId+" and
			// sm.status='ACTIVE' and srm.service_id=sm.service_id";
			String fetchService = "select srm.id,role_id,interface,service_display_name,priv_rep_table from st_lms_service_role_mapping srm,st_lms_service_master sm,st_lms_service_delivery_master sdm where srm.role_id="
					+ roleId
					+ " and organization_id="
					+ userOrgId
					+ " and srm.status='ACTIVE' and sm.status='ACTIVE' and sdm.status='ACTIVE' and srm.id=sdm.service_delivery_master_id and sdm.service_id=sm.service_id";
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
				// logger.debug(rolePrivQuery);
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
									"Role Head Registration")		) {
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

	/*
	 * public List createRoleHead() { //logger.debug("role name is : "+
	 * roleName); Session session=HibernateSessionFactory.getSession();
	 * Transaction tx=null; tx=session.beginTransaction();
	 * 
	 * IgsUserMaster igsUserMas= new IgsUserMaster();
	 * 
	 * String query ="select new
	 * "+PriviledgeBeanRole.class.getName()+"(ium.userName) from IgsUserMaster
	 * as ium "; Query q=session.createQuery(query); List<PriviledgeBeanRole>
	 * userList=new ArrayList<PriviledgeBeanRole>(); userList =q.list();
	 * logger.debug("hello "+ userList.get(0).getUserName()); //ArrayList<PriviledgeBeanRole>
	 * userList =q.list(); List userRoleList=new ArrayList();
	 * userRoleList.add(userList);
	 * 
	 * String roleQuery ="select new
	 * "+PriviledgeBeanRole.class.getName()+"(irm.roleName,irm.roleId) from
	 * IgsRoleMaster as irm "; Query q1=session.createQuery(roleQuery);
	 * 
	 * List<PriviledgeBeanRole> roleList=new ArrayList<PriviledgeBeanRole>(); //
	 * List<PriviledgeBeanRole> roleList =q1.list();
	 * 
	 * roleList =q1.list(); logger.debug("role list is :"+roleList );
	 * 
	 * 
	 * userRoleList.add(roleList); //List<PriviledgeBeanRole> userList
	 * =q.list(); logger.debug("inside helper class create role head method");
	 * 
	 * logger.debug("user role list is "+ userRoleList); return userRoleList; }
	 * 
	 * public void insertRoleHeadInsideDb(int roleId,int userId,String userName) {
	 * Session session=HibernateSessionFactory.getSession(); Transaction
	 * tx=null; tx=session.beginTransaction(); logger.debug("rolr id is " +
	 * roleId); RolePrivlMapping rolePrivMap=new RolePrivlMapping(); String
	 * rolePrivQuery="select rpm from RolePrivlMapping as rpm where
	 * rpm.id.igsRoleMaster.roleId=?"; Query
	 * q=session.createQuery(rolePrivQuery); q.setParameter(0, roleId);
	 * logger.debug(((RolePrivlMapping)(q.list().get(0))).getId().getPid());
	 * 
	 * UserPrivMaster upm= new UserPrivMaster(); UserPrivMasterId upmId=new
	 * UserPrivMasterId(); for(int i=0;i<q.list().size();i++) {
	 * 
	 * 
	 * upmId.setUserId(userId); upmId.setRoleId(roleId);
	 * upmId.setPid(((RolePrivlMapping)(q.list().get(i))).getId().getPid());
	 * upmId.setStatus("ACTIVE"); upm.setId(upmId); session.save(upm);
	 * session.flush(); session.clear(); }
	 * 
	 * 
	 * tx.commit(); }
	 * 
	 */
	public List getHeadsPriv(int roleId, int userId) throws SQLException {

		logger.debug("role id and user id is " + roleId + userId);
		 
		Connection con = DBConnect.getConnection();
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		con.setAutoCommit(false);

		String getUsersQuery = "select user_name,user_Id  from st_user_master where role_id="
				+ roleId
				+ " and isrolehead='N' and  user_id not in(select user_id from st_lms_user_priv_mapping)";
		ResultSet rs = stmt.executeQuery(getUsersQuery);
		ArrayList<userListBean> userList = new ArrayList<userListBean>();
		userListBean userBean;
		while (rs.next()) {

			userBean = new userListBean();
			userBean.setUserName(rs.getString("user_name"));
			userList.add(userBean);

		}

		// get heads priviledges from role_priv_mapping table

		String rolePrivQuery = "select pr.pid,pr.priv_title from st_lms_user_priv_mapping upm,priviledge_rep as pr where upm.user_id="
				+ userId + " and upm.priv_id=pr.pid and pr.status='ACTIVE'";
		ResultSet rs1 = stmt1.executeQuery(rolePrivQuery);

		userPrivBean privBean;
		ArrayList<userPrivBean> userPrivList = new ArrayList<userPrivBean>();
		while (rs1.next()) {
			// logger.debug("priv title is-----:: "+rs1.getString("Priv_title")
			// );
			if (!rs1.getString("Priv_title").equals("Add Sub Users")
					&& !rs1.getString("Priv_title").equals(
							"Edit User Priviledges")
					&& !rs1.getString("Priv_title").equals(
							"BO: Edit Sub User Privileges")
					&& !rs1.getString("Priv_title").equals("BO: Add Sub User")) {
				// logger.debug("#############inside if
				// loop#############################");
				privBean = new userPrivBean();
				privBean.setPid(rs1.getInt("pid"));
				privBean.setPrivTitle(rs1.getString("Priv_title"));
				userPrivList.add(privBean);
			}
		}

		List userandPrivList = new ArrayList();
		userandPrivList.add(userList);
		userandPrivList.add(userPrivList);

		if (con != null) {
			con.close();
		}
		return userandPrivList;

	}

	public int getUserIdByuserName(String userName) throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		int userId = 0;
		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("select user_id from st_user_master where user_name='"
							+ userName + "'");
			while (rs.next()) {
				userId = rs.getInt("user_id");

			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("error in sql connection", e);
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

		return userId;
	}

	public ArrayList<userPrivBean> getUserPriviledges(String userName)
			throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		ArrayList<userPrivBean> userPrivList = new ArrayList<userPrivBean>();
		try {
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			con.setAutoCommit(false);

			int userId = getUserIdByuserName(userName);
			logger.debug("user id is sss " + userId + "user name is  "
					+ userName);
			String privQuery = "select pr.priv_title,pr.pid,upm.status from priviledge_rep as pr ,st_lms_user_priv_mapping as upm where upm.priv_id=pr.pid and upm.user_id="
					+ userId + "";
			ResultSet rs = stmt.executeQuery(privQuery);
			userPrivBean privBean;

			while (rs.next()) {
				privBean = new userPrivBean();
				privBean.setPid(rs.getInt("pid"));
				privBean.setPrivTitle(rs.getString("Priv_title"));
				privBean.setStatus(rs.getString("status"));
				userPrivList.add(privBean);

			}

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
		return userPrivList;

	}

	public List getUsers(int roleId, int userId) throws SQLException {

		 
		Connection con = DBConnect.getConnection();
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		con.setAutoCommit(false);
		// get users list for editing from user and user priv mapping table

		String userQuery = "select distinct um.user_name,um.user_id from st_user_master as um ,st_lms_user_priv_mapping as upm where upm.role_id="
				+ roleId
				+ " and upm.user_id=um.user_id and um.isrolehead='N' order by um.user_name";
		ResultSet rs = stmt.executeQuery(userQuery);
		ArrayList<userListBean> userList = new ArrayList<userListBean>();
		userListBean userBean;
		while (rs.next()) {

			userBean = new userListBean();
			userBean.setUserName(rs.getString("user_name"));
			userList.add(userBean);
		}

		// get heads priviledges list

		String headPrivQuery = "select pr.priv_title,pr.pid from priviledge_rep as pr ,role_privl_mapping as rpm where rpm.pid=pr.pid and rpm.role_id="
				+ roleId + "";
		ResultSet rs1 = stmt1.executeQuery(headPrivQuery);
		userPrivBean privBean;
		ArrayList<userPrivBean> userPrivList = new ArrayList<userPrivBean>();
		while (rs1.next()) {
			if (!rs1.getString("Priv_title").equals("Add Sub Users")
					&& !rs1.getString("Priv_title").equals(
							"Edit User Priviledges")
					&& !rs1.getString("Priv_title").equals(
							"BO: Edit Sub User Privileges")
					&& !rs1.getString("Priv_title").equals("BO: Add Sub User")) {
				privBean = new userPrivBean();
				privBean.setPid(rs1.getInt("pid"));
				privBean.setPrivTitle(rs1.getString("Priv_title"));
				userPrivList.add(privBean);
			}
		}

		List userPrivListHead = new ArrayList();
		userPrivListHead.add(userList);
		userPrivListHead.add(userPrivList);

		con.close();
		return userPrivListHead;

	}

	public void managePriv(String tierCode, String requestIp,String isNewService) {
		 
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);

			Statement tierStmt = con.createStatement();
			Statement serDelStmt = con.createStatement();
			Statement privStmt = con.createStatement();
			Statement insRPMStmt = con.createStatement();
			Statement orgStmt = con.createStatement();
			Statement userStmt = con.createStatement();
			Statement srmStmt = con.createStatement();
			Statement menuStmt = con.createStatement();
			Statement selectUser = con.createStatement();

			ResultSet rsTier = null;
			ResultSet rsServDel = null;
			ResultSet rsPriv = null;
			ResultSet rsOrg = null;
			ResultSet userRs = null;

			String currentTime = Util.getCurrentTimeString();

			if("BO".equals(tierCode)) {
				String query = "INSERT INTO st_lms_user_priv_history (user_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip) SELECT user_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip FROM st_lms_user_priv_mapping WHERE role_id=(SELECT role_id FROM st_lms_role_master WHERE tier_id=(SELECT tier_id FROM st_lms_tier_master WHERE tier_code='"+tierCode+"') AND is_master='Y');";
				logger.debug("Insert History Query - "+query);
				con.createStatement().executeUpdate(query);

				query = "UPDATE st_lms_user_priv_mapping SET change_date='"+currentTime+"', change_by=11001, request_ip='"+requestIp+"' WHERE role_id=(SELECT role_id FROM st_lms_role_master WHERE tier_id=(SELECT tier_id FROM st_lms_tier_master WHERE tier_code='BO') AND is_master='Y');";
				logger.debug("Update Mapping Table Query - "+query);
				con.createStatement().executeUpdate(query);
			}

			logger.debug("Tier Code -- " + tierCode);

			String selTier = "select tm.tier_id as tierId,tier_status as tierStatus,role_id as roleId, rm.is_master "
					+ " from st_lms_tier_master tm, st_lms_role_master  rm where tm.tier_code='"
					+ tierCode + "' and " + " tm.tier_id = rm.tier_id ";
			String selServDel = null;
			String selPriv = null;
			String selOrg = "select organization_id from st_lms_organization_master where "
					+ " organization_type = '" + tierCode + "'";
			String insSRM = null;
			String selUser = null;

			logger.debug("Org -- " + selOrg);
			rsOrg = orgStmt.executeQuery(selOrg);

			logger.debug("Tier and Roles-- " + selTier);
			rsTier = tierStmt.executeQuery(selTier);
			while (rsTier.next()) {
				logger.info("TIER - ROLE LOOP ");

				int tierId = rsTier.getInt("tierId");
				String tierStatus = rsTier.getString("tierStatus");
				int roleId = rsTier.getInt("roleId");
				String isRoleMaster = rsTier.getString("is_master");

				selServDel = "select  dm.service_delivery_master_id as mapId, dm.status as delStatus, dm.priv_rep_table, dm.menu_master_table, "
						+ " sm.service_id as servId, sm.status as serStatus, interface from st_lms_service_delivery_master dm, "
						+ " st_lms_service_master sm where dm.tier_id = "
						+ tierId + " and " + " dm.service_id = sm.service_id";
				logger.debug("Service Delivery ===" + selServDel);

				rsServDel = serDelStmt.executeQuery(selServDel);
				while (rsServDel.next()) {
					logger.debug("SERVICE DELIVERY LOOP");
					int mapId = rsServDel.getInt("mapId");
					int serId = rsServDel.getInt("servId");
					String delStatus = rsServDel.getString("delStatus");
					String servStatus = rsServDel.getString("serStatus");
					String privTable = rsServDel.getString("priv_rep_table");
					String menuTable = rsServDel.getString("menu_master_table");
					String userInterface = rsServDel.getString("interface");
					StringBuilder insRPM = new StringBuilder(
							"insert into st_lms_role_priv_mapping values ");
					selPriv = "select distinct(priv_id), status as privStatus from "
							+ privTable
							+ " where "
							+ " priv_owner = '"
							+ tierCode
							+ "' and channel = '"
							+ userInterface
							+ "' and priv_id not in (select distinct(priv_id) from "
							+ " st_lms_role_priv_mapping where role_id = "
							+ roleId
							+ " and service_mapping_id = "
							+ mapId
							+ ")";
					// selPriv="select distinct(pid), status as privStatus from
					// "+privTable+" where priv_owner = '"+tierCode+"' and pid
					// not in (select pid from st_lms_role_priv_mapping where
					// service_mapping_id="+mapId+" and role_id="+roleId+")";

					String menuInsert = "insert into "
							+ menuTable
							+ " (action_id,menu_name,menu_disp_name,menu_disp_name_en,menu_disp_name_fr,parent_menu_id,item_order) select action_id,group_name ,group_name,group_name_en,group_name_fr,0,0 from "
							+ privTable
							+ " where is_start='Y' and action_id not in (select action_id from "
							+ menuTable + ")";
					logger.debug("menu insert -- " + menuInsert);
					menuStmt.executeUpdate(menuInsert);

					rsPriv = privStmt.executeQuery(selPriv);
					boolean status = tierStatus.equals("ACTIVE")
							&& delStatus.equals("ACTIVE")
							&& servStatus.equals("ACTIVE");
					boolean flag = false;
					logger.debug(selPriv);
					while (rsPriv.next()) {
						String privStatus = rsPriv.getString("privStatus");
						int pid = rsPriv.getInt("priv_id");
						String insStatus = "INACTIVE";
						if (status && privStatus.equals("ACTIVE")
								&& isRoleMaster.equalsIgnoreCase("Y")) {
							insStatus = "ACTIVE";
						}
						flag = true;
						insRPM.append("(" + roleId + "," + pid + "," + mapId
								+ ",'" + insStatus + "'),");
					}

					logger.debug(roleId + "Role Priv MapTest************"
							+ insRPM);

					if (flag) {

						insRPMStmt.executeUpdate(insRPM.substring(0, insRPM
								.length() - 1));

					}
					String userSelect = null;

					while (rsOrg.next()) {
						logger.info("USER PRIV LOP - org loop");
						String userStatus = "INACTIVE";
						userSelect = "select user_id,isrolehead from st_lms_user_master um where um.role_id="
								+ roleId
								+ " and "
								+ " um.organization_type='"
								+ tierCode
								+ "' and um.organization_id="
								+ rsOrg.getInt("organization_id");

						logger.debug("USer id -- " + userSelect);
						userRs = selectUser.executeQuery(userSelect);
						while (userRs.next()) {
							if (userRs.getString("isrolehead")
									.equalsIgnoreCase("N")) {
								selUser = "insert into st_lms_user_priv_mapping (user_id, role_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip) select "
										+ userRs.getString("user_id")
										+ ", "
										+ roleId
										+ ", priv_id, "
										+ mapId
										+ ", 'INACTIVE', '"+currentTime+"', 11001, '"+requestIp+"' from st_lms_role_priv_mapping where role_id = "
										+ roleId
										+ " and service_mapping_id = "
										+ mapId
										+ "  and"
										+ "  priv_id not in (select distinct(priv_id) from st_lms_user_priv_mapping where role_id = "
										+ roleId
										+ " and service_mapping_id = "
										+ mapId
										+ " and user_id = "
										+ userRs.getString("user_id") + " )";

								logger.debug("user insert -- " + selUser);
							} else {
								selUser = "insert into st_lms_user_priv_mapping (user_id, role_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip) select "
										+ userRs.getString("user_id")
										+ ", "
										+ roleId
										+ ", priv_id, "
										+ mapId
										+ ", status, '"+currentTime+"', 11001, '"+requestIp+"' from st_lms_role_priv_mapping where role_id = "
										+ roleId
										+ " and service_mapping_id = "
										+ mapId
										+ "  and"
										+ "  priv_id not in (select distinct(priv_id) from st_lms_user_priv_mapping where role_id = "
										+ roleId
										+ " and service_mapping_id = "
										+ mapId
										+ " and user_id = "
										+ userRs.getString("user_id") + " )";
								logger.debug("user insert -- " + selUser);

							}

							userStmt.executeUpdate(selUser);

						}
					
						if (isNewService.equalsIgnoreCase("Yes")){
						userStmt .executeUpdate("insert into st_lms_service_role_mapping values (" + mapId + "," +
						roleId + "," + rsOrg.getInt("organization_id") + "," +
						(status ? "'ACTIVE'" : "'INACTIVE'") + ")");
						}
						
					}
					rsOrg.beforeFirst();

				}
			}

			con.commit();

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void newPrivForMaster(String ownerType) throws SQLException {
		 
		Connection con = DBConnect.getConnection();
		con.setAutoCommit(false);
		Statement stmt = con.createStatement();
		Statement stmtUser = con.createStatement();
		Statement insertStmt = con.createStatement();
		ResultSet rs = null;
		ResultSet rsRole = null;
		int tierId = 0;
		int roleId = 0;
		List<Integer> nonRoleHeadId = new ArrayList<Integer>();
		StringBuilder activePrivilege = new StringBuilder("");
		StringBuilder inActivePrivilege = new StringBuilder("");

		// ROLE PRIVILEGE STARTS

		// This Query will fetch role_id of (BO,Agent,Retailer)
		String fetchOwnerRoleId = "select role_id,is_master,tier_id from st_lms_role_master where tier_id = (select tier_id from"
				+ " st_lms_tier_master where tier_code = '" + ownerType + "'";
		rs = stmt.executeQuery(fetchOwnerRoleId);
		while (rs.next()) {
			if (rs.getString("is_master").equals("Y")) {
				roleId = rs.getInt("role_id");
				tierId = rs.getInt("tier_id");
			} else {
				nonRoleHeadId.add(rs.getInt("role_id"));
			}
		}
		/*----------- Assumption -------------*/
		// SERVICES ARE INSERTED IN SERVICE MASTER
		// SERVICE_DELIVERY_MAPPING HAS ENOUGH ENTRY
		Statement insertSerRoleStmt = con.createStatement();
		ResultSet rsdelivery = null;
		Statement stmt2 = con.createStatement();
		ResultSet rs2 = null;

		String fetchDeliverdId = "select dm.service_delivery_master_id,dm.priv_rep_table,dm.status as dmstatus, sm.status as smstatus "
				+ "from st_lms_service_delivery_master dm ,st_lms_service_master sm where tier_id = '"
				+ tierId + "' and" + " dm.service_id =  sm.service_id ";
		String fetchRoleOrg = "select organization_id from st_lms_organization_master om where "
				+ "organization_type = '" + ownerType + "'";
		rs2 = stmt2.executeQuery(fetchRoleOrg);
		rsdelivery = insertSerRoleStmt.executeQuery(fetchDeliverdId);
		int orgId = 0;
		while (rs2.next()) {
			orgId = rs2.getInt("organization_id");
			while (rsdelivery.next()) {
				String status = null;
				// status =
				// (("".equalsIgnoreCase(rsdelivery.getString("smstatus"))),"ACTIVE","INACTIVE");

			}

		}

		// This Query will fetch all the active privileges from
		// priviledge_rep which are not present in role_mapping for
		// role_head_owner and will Insert into role mapping as Active which are
		// not matched

		String insertActivePriv = "insert into role_privl_mapping (pid,role_id,status) select distinct(pid),"
				+ roleId
				+ ",'ACTIVE' from priviledge_rep where priv_owner='"
				+ ownerType
				+ "' and status='ACTIVE' and pid not in (select distinct(pid) from role_privl_mapping where role_id="
				+ roleId + ")";
		logger.debug("Active**" + insertActivePriv + "***\n" + roleId);
		stmt.execute(insertActivePriv);

		// This query will update the inactive privilege to active in
		// role_mapping for master role.
		String updateActivePriv = "update role_privl_mapping set status = 'ACTIVE' where role_id="
				+ roleId
				+ " and pid in (select distinct(pid) from priviledge_rep where priv_owner='"
				+ ownerType + "' and status='ACTIVE')";

		stmt.execute(updateActivePriv);

		Iterator<Integer> nonroleHeadItr = nonRoleHeadId.iterator();
		while (nonroleHeadItr.hasNext()) {
			// This query will insert privileges as Inactive to the non head
			// roles.
			int nonHeadId = nonroleHeadItr.next();
			String insertRolePriv = "insert into role_privl_mapping (pid,role_id,status) select distinct(pid),"
					+ nonHeadId
					+ ",'INACTIVE' from priviledge_rep where priv_owner='"
					+ ownerType
					+ "' and status='ACTIVE' and pid not in (select distinct(pid) from role_privl_mapping where role_id ="
					+ nonHeadId + ")";
			logger.debug("InActive**" + insertRolePriv + "***\n");
			stmt.execute(insertRolePriv);
		}

		// This query wil fetch inactive privileges from priviledge_rep and
		// set inactive in all the roles of the owner.
		String updateInactivePriv = "update role_privl_mapping set status='INACTIVE' where role_id in (select role_id from st_lms_role_master where role_owner='"
				+ ownerType
				+ "') and pid in (select distinct(pid) from priviledge_rep where priv_owner='"
				+ ownerType + "' and status='INACTIVE')";

		stmt.execute(updateInactivePriv);
		// ROLE PRIVILEGE ENDS

		// User Privilege Starts

		// This Query will fetch role_id of (BO,Agent,Retailer)
		String fetchRoleId = "select role_id from st_lms_role_master where role_owner='"
				+ ownerType + "'";
		rsRole = stmt.executeQuery(fetchRoleId);
		while (rsRole.next()) {
			int roleName = rsRole.getInt("role_id");

			logger.debug(roleName + "*********");
			// This Query updates the user's privilege id to Active those are
			// Role Head
			String updRoleHeadPrivActive = "update st_lms_user_priv_mapping set status='ACTIVE' where role_id="
					+ roleName
					+ " and user_id in (select user_id from st_user_master where role_id="
					+ roleName
					+ " and isrolehead='Y') and pid in (select pid from role_privl_mapping where role_id="
					+ roleName + " and status='ACTIVE')";

			// This query check for any new Privilege id to be added to the role
			String fetchActivePriv = "select pid from role_privl_mapping where role_id="
					+ roleName
					+ " and status='ACTIVE' and pid not in (select distinct(priv_id) from st_lms_user_priv_mapping where user_id in (select user_id from st_user_master where role_id="
					+ roleName + " and isrolehead='Y'))";
			ResultSet rsUser = stmtUser.executeQuery(fetchActivePriv);
			while (rsUser.next()) {
				// This query insert new privilege which is added to the role
				// into the st_lms_user_priv_mapping
				insertStmt
						.execute("insert into st_lms_user_priv_mapping (user_id,role_id,priv_id,status) select user_id,"
								+ roleName
								+ ","
								+ rsUser.getInt("pid")
								+ ",'ACTIVE' from st_user_master where role_id="
								+ roleName + " and isrolehead='Y'");
				insertStmt
						.execute("insert into st_lms_user_priv_mapping (user_id,role_id,priv_id,status) select user_id,"
								+ roleName
								+ ","
								+ rsUser.getInt("pid")
								+ ",'INACTIVE' from st_user_master where role_id="
								+ roleName + " and isrolehead='N'");

			}

			String updUserPrivInActive = "update st_lms_user_priv_mapping set status='INACTIVE' where role_id="
					+ roleName
					+ " and priv_id in (select pid from role_privl_mapping where role_id="
					+ roleName + " and status='INACTIVE')";

			stmtUser.executeUpdate(updRoleHeadPrivActive);
			stmtUser.executeUpdate(updUserPrivInActive);

		}
		con.commit();

		// User Privilege Ends
	}

}