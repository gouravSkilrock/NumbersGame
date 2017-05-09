package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.MessageInboxDaoImpl;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RoleHeadDataBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.loginMgmt.AutoGenerate;

public class CreateOrgUserHelper {

	private static final String projectName = ServletActionContext
			.getServletContext().getContextPath();
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Statement stmt = null;

	private boolean assignEmailPriviledges(int userId, String orgType,
			String email, String[] emailPrivId, Connection con)
			throws SQLException {

		// insert details into st_lms_report_email_user_master
		String insertEmailQuery = "insert into st_lms_report_email_user_master ("
				+ " ref_user_id, organization_id, organization_type, first_name, last_name, email_id, mob_no,	registration_date, status)"
				+ " select  aa.user_id 'ref_user_id', organization_id, organization_type, first_name, last_name,	email_id, phone_nbr, CURRENT_TIMESTAMP, 'ACTIVE'"
				+ " from st_lms_user_master aa, st_lms_user_contact_details bb"
				+ " where aa.user_id=bb.user_id and aa.user_id=" + userId;
		System.out.println("insertEmailQuery == " + insertEmailQuery);
		pstmt = con.prepareStatement(insertEmailQuery);
		int updateRow = pstmt.executeUpdate();
		rs = pstmt.getGeneratedKeys();

		if (rs.next()) {
			int newUserId = rs.getInt(1);
			System.out
					.println("insertion into st_lms_report_email_user_master table is done & user_id is == "
							+ newUserId);
			pstmt = con
					.prepareStatement("insert into st_lms_report_email_priv_master (user_id, email_pid, status) "
							+ "select "
							+ newUserId
							+ ", email_pid, 'INACTIVE' from st_lms_report_email_priviledge_rep where "
							+ "priv_owner='" + orgType + "' ");
			updateRow = pstmt.executeUpdate();
			System.out
					.println("total row inserted as inactive == " + updateRow);

			if (emailPrivId != null && emailPrivId.length > 0) {
				for (String emailPid : emailPrivId) {
					pstmt = con
							.prepareStatement(" update st_lms_report_email_priv_master set status = 'ACTIVE' where user_id = ? and email_pid = ?");
					pstmt.setInt(1, newUserId);
					pstmt.setInt(2, Integer.parseInt(emailPid.trim()));
					updateRow = pstmt.executeUpdate();
					System.out.println("total row active == " + updateRow);
				}
			}
			return true;
		} else {
			throw new SQLException(
					"Key is not generated for email_user_master table.");
		}

	}

	public String createBoUser(UserInfoBean userInfoBean, String userName,
			String status, String secQues, String secAns, String firstName,
			String lastName, String email, long phone, String idType,
			String idNo, String roleIdStr, String emailPrivId[], String requestIp)
			throws LMSException {

		try {
			con = DBConnect.getConnection();

			// check is 'user_name' already exists in st_lms_user_master table
			stmt = con.createStatement();
			String getUsersName = QueryManager.getST3UserName()
					+ "where user_name= '" + userName.trim() + "'";
			rs = stmt.executeQuery(getUsersName);
			if ("admin".equalsIgnoreCase(userName) || rs.next()) {
				System.out.println("user already exists !!");
				return "INPUT";
			}
			rs.close();
			stmt.close();

			// get the role id from database
			int roleId = -1;
			roleId = Integer.parseInt(roleIdStr.trim());
			if (roleId <= 0) {
				new LMSException("roleId is not in database");
			}

			// generate the auto password
			String autoPass = AutoGenerate.autoPassword();

			con.setAutoCommit(false);

			// insert data into st_lms_user_master table
			String userReg = QueryManager.insertST3AgentDetails();
			pstmt = con.prepareStatement(userReg);
			pstmt.setInt(1, userInfoBean.getUserOrgId());
			pstmt.setInt(2, roleId);
			pstmt.setString(3, userInfoBean.getUserType());
			pstmt.setString(4, "1");
			pstmt.setString(5, userName.trim().toLowerCase());
			pstmt.setString(6, MD5Encoder.encode(autoPass));
			pstmt.setString(7, status);
			pstmt.setString(8, secQues);
			pstmt.setString(9, secAns);
			pstmt
					.setTimestamp(10, new java.sql.Timestamp(new Date()
							.getTime()));
			pstmt.setString(11, "Y");
			pstmt.setInt(12, userInfoBean.getUserId());
			pstmt.setInt(13, userInfoBean.getUserId());
			pstmt.setString(14, null);

			System.out.println("bo user created query : " + pstmt);
			pstmt.executeUpdate();

			// get the generated user_id
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int userId = rs.getInt(1);
				System.out.println("bo user created and user id : " + userId);

				// insert data into st_lms_user_contact_details
				String insertContactDetail = QueryManager
						.insertST3ContactsDetails();
				pstmt = con.prepareStatement(insertContactDetail);
				pstmt.setInt(1, userId);
				pstmt.setString(2, firstName);
				pstmt.setString(3, lastName);
				pstmt.setString(4, email);
				pstmt.setLong(5, phone);
				pstmt.setString(6, idType);
				pstmt.setString(7, idNo);
				pstmt.setLong(8, phone);
				pstmt.execute();

				// insert password details into st_lms_password_history table
				String passwordDetails = QueryManager
						.insertST3PasswordDetails();
				pstmt = con.prepareStatement(passwordDetails);
				pstmt.setInt(1, userId);
				pstmt.setString(2, MD5Encoder.encode(autoPass));
				int passHistoryRows = pstmt.executeUpdate();
				System.out.println("total rows update in passHistory table == "
						+ passHistoryRows);
				rs.close();
				pstmt.close();

				//	Insert Data In st_lms_user_ip_time_mapping Table.
				LoginTimeIPValidationHelper helper = new LoginTimeIPValidationHelper();
				helper.insertUserTimeLimitData(userId, con);

				// insert into st_st_lms_user_priv_mapping
				String currentTime = Util.getCurrentTimeString();
				String insertUserPrivQuery = "insert into st_lms_user_priv_mapping (user_id, role_id, priv_id, service_mapping_id, status, change_date, change_by, request_ip) select "
						+ userId
						+ ","
						+ roleId
						+ ", rpm.priv_id,rpm.service_mapping_id,rpm.status, '"+currentTime+"', "+userInfoBean.getUserId()+", '"+requestIp+"' from st_lms_role_priv_mapping rpm where rpm.role_id = "
						+ roleId;
				pstmt = con.prepareStatement(insertUserPrivQuery);
				int userPrivMasterRows = pstmt.executeUpdate();
				System.out
						.println("total rows = "
								+ userPrivMasterRows
								+ " data inserted into st_st_lms_user_priv_mapping query : "
								+ pstmt);

				// assign privilege to AGENT
				assignEmailPriviledges(userId, userInfoBean.getUserType(),
						email, emailPrivId, con);

				//	Insert Registration Messages
				MessageInboxDaoImpl.getSingleInstance().addRegistrationMessage(userId, userInfoBean.getUserType(), "WEB", con);

				boolean flag = true;
				if(ServicesBean.isSLE()) {
					flag = false;
					try {
						RoleHeadDataBean roleDataBean = new RoleHeadDataBean();
						roleDataBean.setCreatorUserId(userInfoBean.getUserId());
						roleDataBean.setRoleId(roleId);
						roleDataBean.setUserId(userId);
						roleDataBean.setUserName(userName);
						roleDataBean.setUserType("BO");
						roleDataBean.setFirstName(firstName);
						roleDataBean.setLastName(lastName);
						roleDataBean.setMobileNbr(String.valueOf(phone));
						roleDataBean.setEmailId(email);
						roleDataBean.setRequestIp(requestIp);
						NotifySLE notifySLE = new NotifySLE(SLE.Activity.ROLE_HEAD_REGISTRATION, roleDataBean);
						notifySLE.asyncCall(notifySLE);
						flag = true;
					} catch (Exception ex) {
						ex.printStackTrace();
						flag = false;
					}
				}

				if(flag) {
					// send mail to user
					con.commit();

					String msgFor = "Thanks for registration to our gaming system  Your Login details are";
					String emailMsgTxt = "<html><table><tr><td>Hi "
							+ firstName
							+ " "
							+ lastName
							+ "</td></tr><tr><td>"
							+ msgFor
							+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
							+ userName.trim()
							+ "</td></tr><tr><td>password :: </td><td>"
							+ autoPass.toString()
							+ "</td></tr><tr><td>log on </td><td>"
							+ LMSFilterDispatcher.webLink + "/"
							+ LMSFilterDispatcher.mailProjName
							+ "/</td></tr></table></html>";
					MailSend mailSend = new MailSend(email, emailMsgTxt);
					mailSend.setDaemon(true);
					mailSend.start();
					// MailSend.sendMailToUser(email, autoPass, userName.trim(),
					// firstName, lastName, msgFor);
					System.out.println("mail sent after commit");

					return "MASSUCCESS";
				} else {
					con.rollback();
					return "INPUT";
				}
			} else {
				return "INPUT";
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sql exception", se);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * public String createNewOrgUser(UserInfoBean userInfoBean,
	 * OrganizationAction orgData, String userName, String status, String
	 * secQues, String secAns, String firstName, String lastName, String email,
	 * long phone, String idType, String idNo, String role, String autoScrap,
	 * String emailPrivId[], Set inActivePrivSet) throws LMSException {
	 * 
	 * 
	 * try { con = DBConnect.getConnection(); con.setAutoCommit(false); //
	 * check is 'user_name' already exists in st_lms_user_master table
	 * stmt=con.createStatement(); String
	 * getUsersName=QueryManager.getST3UserName() + "where user_name= '" +
	 * userName.trim()+"'"; rs = stmt.executeQuery(getUsersName); if( rs.next()) {
	 * System.out.println("user already exists !!"); return "ERROR"; }
	 * rs.close(); stmt.close(); // get the role id from database String
	 * getRoleIdQuery = null; if(orgData.getOrgType().equals("AGENT")){
	 * getRoleIdQuery = QueryManager.getST3RoleId() + " where
	 * role_name='AGT_MAS' " ; } else
	 * if(orgData.getOrgType().equals("RETAILER")) { getRoleIdQuery =
	 * QueryManager.getST3RoleId() + " where role_name='RET_MAS' " ; }
	 * stmt=con.createStatement(); rs=stmt.executeQuery(getRoleIdQuery);
	 * System.out.println("get the role id from database : "+getRoleIdQuery);
	 * int roleId = -1; if(rs.next()) {
	 * roleId=rs.getInt(TableConstants.ROLE_ID); } rs.close(); stmt.close();
	 * 
	 * if(roleId == -1) new LMSException("roleId is not in database"); // get
	 * the state and country code from DB stmt=con.createStatement(); String
	 * getCountryStateCode=QueryManager.getCountryAndStateCode()+ " where
	 * cont.name='"+orgData.getCountry() +" ' and
	 * stat.country_code=cont.country_code and
	 * stat.name='"+orgData.getState()+"'";
	 * rs=stmt.executeQuery(getCountryStateCode); String countryCode = null;
	 * String stateCode = null; if(rs.next()) { countryCode=
	 * rs.getString(TableConstants.COUNTRY_CODE); stateCode=
	 * rs.getString(TableConstants.STATE_CODE); System.out.println(" countryCode =
	 * "+countryCode+" state codde = "+stateCode); } // create the new
	 * organization into DB if(autoScrap==null ||
	 * "".equalsIgnoreCase(autoScrap.trim())) autoScrap = "NO";
	 * 
	 * String insertOrg=QueryManager.insertST3OrganizationAgent() + " values (
	 * '"+ orgData.getOrgType()+ "','" + orgData.getOrgName()+ "',
	 * '"+userInfoBean.getUserOrgId()+"','"+ orgData.getAddrLine1() + "','"+
	 * orgData.getAddrLine2()+
	 * "','"+orgData.getCity()+"',"+orgData.getPin()+",'"
	 * +stateCode+"','"+countryCode+"', 0,
	 * '"+orgData.getCreditLimit()+"','"+orgData.getSecurity()
	 * +"','"+orgData.getStatusorg()+"',
	 * '"+orgData.getCreditLimit()+"','"+orgData.getVatRegNo() +"',
	 * '"+autoScrap+"') "; pstmt = con.prepareStatement(insertOrg); int
	 * orgCreateRows = pstmt.executeUpdate(); System.out.println("rows updated =
	 * "+orgCreateRows+" , org creation query = "+pstmt); ResultSet res =
	 * pstmt.getGeneratedKeys(); // when organization creation completed
	 * if(res.next()) {
	 * 
	 * int genOrgId=res.getInt(1); // generate the auto password String
	 * autoPass=AutoGenerate.autoPassword();
	 * 
	 * pstmt.close(); // insert data into st_lms_user_master table String
	 * userReg=QueryManager.insertST3AgentDetails() ; pstmt =
	 * con.prepareStatement(userReg); pstmt.setInt(1, genOrgId); pstmt.setInt(2,
	 * roleId); pstmt.setString(3, orgData.getOrgType()); pstmt.setString(4,
	 * "1"); pstmt.setString(5, userName.trim()); pstmt.setString(6,
	 * MD5Encoder.encode(autoPass)); pstmt.setString(7, status);
	 * pstmt.setString(8, secQues); pstmt.setString(9, secAns);
	 * pstmt.setTimestamp(10, new java.sql.Timestamp( new Date().getTime()));
	 * pstmt.setString(11, "Y"); pstmt.executeUpdate(); System.out.println("bo
	 * user created query : "+pstmt); // get the generated user_id
	 * rs=pstmt.getGeneratedKeys(); if(rs.next()) { int userId = rs.getInt(1);
	 * System.out.println("bo user created and user id : "+userId);
	 * 
	 * //insert data into st_lms_user_contact_details String
	 * insertContactDetail=QueryManager.insertST3ContactsDetails(); pstmt =
	 * con.prepareStatement(insertContactDetail); pstmt.setInt(1, userId);
	 * pstmt.setString(2, firstName); pstmt.setString(3, lastName );
	 * pstmt.setString(4, email); pstmt.setLong(5, phone); pstmt.setString(6,
	 * idType); pstmt.setString(7, idNo); int contactDetailRows =
	 * pstmt.executeUpdate(); System.out.println("total rows update in
	 * st_lms_user_contact_details table == "+contactDetailRows+" pstmt :
	 * "+pstmt); // insert password details into st_lms_password_history table
	 * String passwordDetails=QueryManager.insertST3PasswordDetails(); pstmt =
	 * con.prepareStatement(passwordDetails); pstmt.setInt(1,userId );
	 * pstmt.setString(2, MD5Encoder.encode(autoPass)); int passHistoryRows =
	 * pstmt.executeUpdate(); System.out.println("total rows update in
	 * passHistory table == "+passHistoryRows); pstmt.close();
	 * 
	 * 
	 * //insert organization history into st_lms_organization_master_history
	 * table String insertOrgHistory=QueryManager.insertST3OrganizationHistory() + "
	 * values( " +userInfoBean.getUserId()+", "+genOrgId+",
	 * '"+orgData.getAddrLine1()+"', '"+orgData.getAddrLine2()+"',
	 * '"+orgData.getCity()+"', '"+orgData.getPin()+"',
	 * "+orgData.getCreditLimit()+", '"+orgData.getStatusorg()+"', '"+new
	 * java.sql.Timestamp( new Date().getTime())+"',
	 * "+orgData.getSecurity()+")"; stmt = con.createStatement(); int
	 * historyRows = stmt.executeUpdate(insertOrgHistory);
	 * System.out.println(historyRows+" rows updated and Query to update history :: " +
	 * insertOrgHistory); // insert into st_st_lms_user_priv_mapping String
	 * insertUserPrivQuery="insert into st_lms_user_priv_mapping select
	 * "+userId+","+roleId+", aa.pid, bb.status from role_privl_mapping aa,
	 * priviledge_rep bb where aa.pid = bb.pid and aa.role_id = "+roleId; pstmt =
	 * con.prepareStatement(insertUserPrivQuery); int userPrivMasterRows =
	 * pstmt.executeUpdate(); System.out.println("total rows =
	 * "+userPrivMasterRows+" data inserted into st_st_lms_user_priv_mapping
	 * query : "+pstmt); // send mail to user
	 * if((orgData.getOrgType()).equals("AGENT")) { // assign privilege to AGENT
	 * assignEmailPriviledges(userId, orgData.getOrgType(), email, emailPrivId,
	 * con); }
	 * 
	 * con.commit(); if((orgData.getOrgType()).equals("AGENT")) { String
	 * msgFor="Thanks for registration to our gaming system Your Login details
	 * are"; String emailMsgTxt="<html><table><tr><td>Hi "+firstName + "
	 * "+lastName +"</td></tr><tr><td>"+msgFor+"</td></tr></table><table><tr><td>User
	 * Name :: </td><td>"+userName.trim()+"</td></tr><tr><td>password ::
	 * </td><td>"+autoPass.toString()+"</td></tr><tr><td>log on </td><td>"+LMSFilterDispatcher.webLink+"/"+LMSFilterDispatcher.mailProjName+"/</td></tr></table></html>";
	 * MailSend mailSend=new MailSend(email,emailMsgTxt);
	 * mailSend.setDaemon(true); mailSend.start();
	 * //MailSend.sendMailToUser(email, autoPass, userName.trim(), firstName,
	 * lastName, msgFor); System.out.println("mail sent after commit"); } return
	 * "SUCCESS"; } else { System.out.println("user is not created "); return
	 * "ERROR"; } }else { System.out.println("organization is not created ");
	 * return "ERROR"; } }catch(SQLException se){ se.printStackTrace(); throw
	 * new LMSException("sql exception",se); }finally{ try{ if(con!=null)
	 * con.close(); }catch(SQLException e){ e.printStackTrace(); } } }
	 * 
	 */

}
