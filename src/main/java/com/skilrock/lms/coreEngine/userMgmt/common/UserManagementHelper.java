package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.UserBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;

public class UserManagementHelper {

	public boolean editOfflineUserDetails(int userid, String offlineStatus,
			String isOffline, boolean isSession) throws LMSException {
		Connection con = null;
		 
		PreparedStatement pstmt = null;
		String updateQuery = null;
		String addQry = "";
		boolean checkUpdate = false, addQryChk = false;
		String preOfflineStatus = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String selQry = "select is_offline,login_status,offline_status from st_lms_ret_offline_master where user_id=?";
			pstmt = con.prepareStatement(selQry);
			pstmt.setInt(1, userid);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				preOfflineStatus = rs.getString("offline_status");
				if ("NO".equalsIgnoreCase(rs.getString("is_offline"))) {
					addQry = ",is_offline='" + isOffline + "'";
					addQryChk = true;
					if (isSession) {
						return false;
					}
				} else if ("YES".equalsIgnoreCase(rs.getString("is_offline"))
						&& "LOGOUT".equalsIgnoreCase(rs
								.getString("login_status"))) {
					addQry = ",is_offline='" + isOffline + "'";
					addQryChk = true;
				}
			}
			if (addQryChk == true
					|| !preOfflineStatus.equalsIgnoreCase(offlineStatus)) {
				updateQuery = "update st_lms_ret_offline_master set offline_status=?"
						+ addQry + " where user_id=?";
				pstmt = con.prepareStatement(updateQuery);
				pstmt.setString(1, offlineStatus);
				pstmt.setInt(2, userid);
				int i = pstmt.executeUpdate();
				if (i > 0) {
					checkUpdate = true;
				}
			}
			con.commit();

		} catch (SQLException e) {

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return checkUpdate;
	}

	/*
	 * public void editUserDetailsByBO(int userid,long phonenbr,String
	 * emailid,String status) throws LMSException{
	 * 
	 * int userId=userid; String emailId =emailid; long phoneNbr=phonenbr;
	 * String userStatus=status; System.out.println("useer id is " + userId);
	 * Connection con = null;
	 * 
	 * try {
	 * 
	 *   Statement stmt1 = null; //Statement
	 * stmt2 = null; con = DBConnect.getConnection(); con.setAutoCommit(false);
	 * stmt1 = con.createStatement(); //stmt2 = con.createStatement();
	 * 
	 * String updateUserDetails=QueryManager.updateST3UserDetails() + "
	 * email_id='"+emailId+"' ,phone_nbr='"+phoneNbr+"' where
	 * user_id='"+userId+"' " ; stmt1.executeUpdate(updateUserDetails);
	 * //stmt1.executeUpdate("update st_user_contact_details set
	 * email_id='"+emailId+"' ,phone_nbr='"+phoneNbr+"' where
	 * user_id='"+userId+"' ");
	 * 
	 * String updateStatus= QueryManager.updateST3UserStatus() + "
	 * status='"+userStatus+"' where user_id='"+userId+"' " ;
	 * stmt1.executeUpdate(updateStatus); //stmt2.executeUpdate("update
	 * st_user_master set status='"+userStatus+"' where user_id='"+userId+"' "); //
	 * update report_email_user_master tables stmt1.executeUpdate("update
	 * st_report_email_user_master set email_id = '"+emailId+"' where
	 * ref_user_id = "+userId);
	 * 
	 * con.commit(); } catch (SQLException e) {
	 * 
	 * 
	 * try { if(con!=null) { con.rollback(); } } catch (SQLException se) { //
	 * TODO Auto-generated catch block se.printStackTrace(); throw new
	 * LMSException("Error During Rollback",se); } e.printStackTrace(); throw
	 * new LMSException(e); } finally{ try{ if(con!=null) { con.close(); }
	 * }catch(SQLException see){ see.printStackTrace(); throw new
	 * LMSException("Error During closing connection",see); } } }
	 */
	public boolean editUserDetails(int userId, long phoneNbr, long mobileNbr, String emailId, String status, int doneByUserId, String comments, String requestIp) throws LMSException {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String prevEmailId = null;
		String prevPhoneNbr = null;
		String prevMobileNbr = null;
		String prevStatus = null;
		int organizationId = 0;
		String organizationStatus = null;
		String isRoleHead = null;
		HistoryBean historyBean = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT email_id, phone_nbr, mobile_nbr, status, om.organization_id, organization_status, isrolehead FROM st_lms_user_master um INNER JOIN st_lms_user_contact_details ucd ON um.user_id=ucd.user_id INNER JOIN st_lms_organization_master om ON om.organization_id=um.organization_id WHERE um.user_id="+userId+";");
			if(rs.next()) {
				prevEmailId = (rs.getString("email_id")==null)?"":rs.getString("email_id").trim();
				prevPhoneNbr = (rs.getString("phone_nbr")==null)?"":rs.getString("phone_nbr").trim();
				prevMobileNbr = (rs.getString("mobile_nbr")==null)?"":rs.getString("mobile_nbr").trim();
				prevStatus = rs.getString("status");
				isRoleHead = rs.getString("isrolehead");
				organizationId = rs.getInt("organization_id");
				organizationStatus = rs.getString("organization_status");
			}

			historyBean = new HistoryBean(userId, doneByUserId, comments, requestIp);
			if(!prevEmailId.equals(emailId)) {
				historyBean.setChangeType("EMAIL_ID");
				historyBean.setChangeValue(prevEmailId);
				historyBean.setUpdatedValue(emailId);
				CommonMethods.insertUpdateUserHistory(historyBean, con);
			}
			if(!prevPhoneNbr.equals(String.valueOf(phoneNbr))) {
				historyBean.setChangeType("PHONE_NUMBER");
				historyBean.setChangeValue(prevPhoneNbr);
				historyBean.setUpdatedValue(String.valueOf(phoneNbr));
				CommonMethods.insertUpdateUserHistory(historyBean, con);
			}
			if(!prevMobileNbr.equals(String.valueOf(mobileNbr))) {
				historyBean.setChangeType("MOBILE_NUMBER");
				historyBean.setChangeValue(prevMobileNbr);
				historyBean.setUpdatedValue(String.valueOf(mobileNbr));
				CommonMethods.insertUpdateUserHistory(historyBean, con);
			}
			if(!prevStatus.equals(status)) {
				historyBean.setChangeType("USER_STATUS");
				historyBean.setChangeValue(prevStatus);
				historyBean.setUpdatedValue(status);
				CommonMethods.insertUpdateUserHistory(historyBean, con);
				if("Y".equalsIgnoreCase(isRoleHead) && ("ACTIVE".equals(status) || "BLOCK".equals(status) || "TERMINATE".equals(status))) {
					historyBean.setOrganizationId(organizationId);
					historyBean.setChangeType("ORGANIZATION_STATUS");
					historyBean.setChangeValue(organizationStatus);
					historyBean.setUpdatedValue(status);
					CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
				}
			}

			stmt.executeUpdate("UPDATE st_lms_report_email_user_master SET email_id='"+emailId+"', status='"+status+"' WHERE ref_user_id="+userId);

			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(con);
		}

		return true;
	}

	public String offlineFileUploadViaApplet(int userId) throws LMSException {
		Connection con = DBConnect.getConnection();
		StringBuilder sb = new StringBuilder("");
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select user_id, user_name from st_lms_user_master where parent_user_id = ? and user_id in (select user_id from st_lms_ret_offline_master where is_offline = 'YES')");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("Offlien Upload*****" + pstmt);
			while (rs.next()) {
				sb.append(rs.getString("user_id"));
				sb.append(",");
				sb.append(rs.getString("user_name"));
				sb.append("|");
			}
			if (sb.length() > 1) {
				sb.deleteCharAt(sb.length() - 1);
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
		return sb.toString();
	}

	public String offlineFileUploadViaAppletAtBO() {
		Connection con = DBConnect.getConnection();
		StringBuilder sb = new StringBuilder("");
		try {
			PreparedStatement ps = con
					.prepareStatement("select user_id, user_name from st_lms_user_master where organization_type = 'AGENT'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sb.append(rs.getString("user_id"));
				sb.append(",");
				sb.append(rs.getString("user_name"));
				sb.append("|");
			}
			if (sb.length() > 1) {
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}

	public UserBean offlineUserDetails(int userId) throws LMSException {

		Connection con = null;
		try {

			UserBean userBean = new UserBean();
			 
			Statement stmt = null;
			ResultSet rs = null;
			con = DBConnect.getConnection();
			stmt = con.createStatement();

			userBean.setUserId(userId);
			String userDetailQuery = "select a.user_id,a.user_name,c.name,d.name 'parent',b.offline_status,"
					+ "b.login_status,b.is_offline from st_lms_user_master a,st_lms_ret_offline_master b,"
					+ "st_lms_organization_master c,st_lms_organization_master d where a.user_id=b.user_id "
					+ "and c.organization_id=a.organization_id and c.organization_id=b.organization_id and "
					+ "d.organization_id=c.parent_id and a.user_id="
					+ userId
					+ "";

			System.out.println(userDetailQuery);
			rs = stmt.executeQuery(userDetailQuery);
			if (rs.next()) {
				userBean.setUserName(rs.getString(TableConstants.USER_NAME));
				userBean.setUserOrgName(rs.getString(TableConstants.ORG_NAME));
				userBean.setParentOrgName(rs.getString("parent"));
				userBean.setOfflineStatus(rs.getString("offline_status"));
				userBean.setLoginStatus(rs.getString("login_status"));
				userBean.setIsOffline(rs.getString("is_offline"));
			}
			return userBean;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
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

	public void resetPassword(int userid, String autoPass, String email,
			String userName, String firstName, String lastName)
			throws LMSException {
		String projectName = ServletActionContext.getServletContext()
				.getContextPath();
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();
			System.out
					.println("auto password is" + MD5Encoder.encode(autoPass));
			String updatePass = QueryManager.updateST3UserPass()
					+ " auto_password='1', password='"
					+ MD5Encoder.encode(autoPass) + "'where user_id='" + userid
					+ "'";
			stmt.executeUpdate(updatePass);

			// reset password
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.insertST3PasswordHistory());
			pstmt.setInt(1, userid);
			pstmt.setString(2, MD5Encoder.encode(autoPass));
			pstmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			pstmt.setString(4, "1");
			pstmt.execute();
			con.commit();
			String msgFor = "Welcome to our gaming system Your password has been reset your login details are";
			String emailMsgTxt = "<html><table><tr><td>Hi " + firstName + " "
					+ lastName + "</td></tr><tr><td>" + msgFor
					+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
					+ userName + "</td></tr><tr><td>password :: </td><td>"
					+ autoPass.toString()
					+ "</td></tr><tr><td>log on </td><td>"
					+ LMSFilterDispatcher.webLink + "/"
					+ LMSFilterDispatcher.mailProjName
					+ "/</td></tr></table></html>";
			MailSend mailSend = new MailSend(email, emailMsgTxt);
			mailSend.setDaemon(true);
			mailSend.start();
			// MailSend.sendMailToUser(email,
			// autoPass,userName,firstName,lastName,msgFor);

		} catch (SQLException e) {

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
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
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}

	}

	public UserDetailsBean UserDetails(int userid, boolean chkOffline)
			throws LMSException {

		int userId = userid;
		int orgId = 0;
		Connection con = null;
		try {

			UserDetailsBean userBean = null;
			List<UserDetailsBean> searchResults = new ArrayList<UserDetailsBean>();
			userBean = new UserDetailsBean();
			System.out.println("heeeeeee");
			 

			Statement stmt1 = null;
			Statement stmt2 = null;
			Statement stmt3 = null;
			Statement stmt4 = null;
			Statement stmt5 = null;
			Statement stmt6 = null;

			con = DBConnect.getConnection();
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			stmt4 = con.createStatement();
			stmt5 = con.createStatement();
			stmt6 = con.createStatement();
			ResultSet rs1;
			ResultSet rs3;
			ResultSet rs4;

			System.out.println(" user id is  " + userId);
			userBean.setUserId(userId);
			System.out.println("set id to bean   " + userBean.getUserId());
			// String getorgID=QueryManager.getST3OrgId() + " where
			// user_id="+userId+"("+QueryManager.getST3OrgDetails()+ " where
			// organization_id="+orgId+")" ;
			String getorgID = QueryManager.getUserAndOrgDetails() + " user_id="
					+ userId + "";

			System.out.println(getorgID);
			rs1 = stmt1.executeQuery(getorgID);
			// rs1=stmt1.executeQuery("select organization_id from
			// st_lms_user_master where user_id='"+userId+"'");

			while (rs1.next()) {
				orgId = rs1.getInt(TableConstants.ORG_ID);
				userBean.setOrgId(orgId);
				userBean.setUserName(rs1.getString(TableConstants.USER_NAME));
				userBean.setStatus(rs1.getString(TableConstants.STATUS));
				userBean.setOrgName(rs1.getString(TableConstants.ORG_NAME));
				userBean.setOrgType(rs1.getString(TableConstants.ORG_TYPE));
				userBean.setOrgAddr1(rs1.getString(TableConstants.ORG_ADDR1));
				userBean.setOrgAddr2(rs1.getString(TableConstants.ORG_ADDR2));
				userBean.setOrgCity(rs1.getString(TableConstants.ORG_CITY));
				String countryCode = rs1.getString(TableConstants.ORG_COUNTRY);
				String stateCode = rs1.getString(TableConstants.ORG_STATE);
				userBean.setOrgPin(rs1.getLong(TableConstants.ORG_PIN));
				userBean.setOrgCreditLimit(rs1
						.getDouble(TableConstants.CREDIT_LIMIT));
				userBean.setOrgStatus(rs1.getString(TableConstants.ORG_STATUS));

				String countryName = QueryManager.getCountryAndStateName()
						+ " cont.country_code='" + countryCode
						+ "' and stat.state_code='" + stateCode + "'";
				ResultSet countryList = stmt5.executeQuery(countryName);
				while (countryList.next()) {
					userBean.setOrgCountry(countryList.getString("country"));
					userBean.setOrgState(countryList.getString("state"));
				}
			}

			/*
			 * System.out.println(" org id is " + orgId); String getOrgDetails =
			 * QueryManager.getST3OrgDetails() + " where
			 * organization_id='"+orgId+"' " ;
			 * rs2=stmt2.executeQuery(getOrgDetails);
			 * //rs2=stmt2.executeQuery("select * from
			 * st_lms_organization_master where organization_id='"+orgId+"'");
			 * while(rs2.next()) {
			 * userBean.setOrgName(rs2.getString(TableConstants.ORG_NAME));
			 * userBean.setOrgType(rs2.getString(TableConstants.ORG_TYPE));
			 * userBean.setOrgAddr1(rs2.getString(TableConstants.ORG_ADDR1));
			 * userBean.setOrgAddr2(rs2.getString(TableConstants.ORG_ADDR2));
			 * userBean.setOrgCity(rs2.getString(TableConstants.ORG_CITY));
			 * String countryCode=rs2.getString(TableConstants.ORG_COUNTRY);
			 * String stateCode=rs2.getString(TableConstants.ORG_STATE); String
			 * countryName=QueryManager.selectST3Country() + " where
			 * country_code='"+countryCode+"' " ; ResultSet
			 * countryList=stmt5.executeQuery(countryName);
			 * 
			 * //ResultSet countryList=stmt3.executeQuery("select name from
			 * st_lms_country_master where country_code='"+countryCode+"'");
			 * while(countryList.next()) {
			 * userBean.setOrgCountry(countryList.getString(TableConstants.NAME)); }
			 * 
			 * String stateName=QueryManager.selectST3State() + " where
			 * state_code='"+stateCode+"' " ; ResultSet
			 * stateList=stmt6.executeQuery(stateName); //ResultSet
			 * stateList=stmt3.executeQuery("select name from
			 * st_lms_state_master where state_code='"+stateCode+"'");
			 * 
			 * while(stateList.next()) {
			 * userBean.setOrgState(stateList.getString(TableConstants.NAME));
			 * System.out.println("state is "
			 * +stateList.getString(TableConstants.NAME)); }
			 * //userBean.setOrgCountry(rs2.getString(TableConstants.ORG_COUNTRY));
			 * //userBean.setOrgState(rs2.getString(TableConstants.ORG_STATE));
			 * System.out.println("state code is "+
			 * rs2.getString(TableConstants.ORG_STATE));
			 * userBean.setOrgPin(rs2.getInt(TableConstants.ORG_PIN));
			 * userBean.setOrgCreditLimit(rs2.getDouble(TableConstants.CREDIT_LIMIT));
			 * userBean.setOrgStatus(rs2.getString(TableConstants.ORG_STATUS)); }
			 * 
			 */

			String contactDetails = QueryManager.getST3ContactDetails()
					+ " where user_id='" + userId + "'  ";
			rs3 = stmt3.executeQuery(contactDetails);
			// rs3=stmt3.executeQuery("select * from st_lms_user_contact_details
			// where user_id='"+userId+"'");
			while (rs3.next()) {
				userBean.setFirstName(rs3.getString(TableConstants.FIRST_NAME));
				userBean.setLastName(rs3.getString(TableConstants.LAST_NAME));
				userBean.setEmailId(rs3.getString(TableConstants.EMAIL));
				userBean.setMobileNbr(rs3.getLong(TableConstants.MOBILE));
				userBean.setPhoneNbr(rs3.getLong(TableConstants.PHONE));
				// status=rs3.getString("status");

			}

			/*
			 * String userStatus=QueryManager.getST3UserSearchQuery() + " where
			 * user_id='"+userId+"' " ; rs4=stmt4.executeQuery(userStatus);
			 * //rs4=stmt4.executeQuery("select status from st_lms_user_master
			 * where user_id='"+userId+"'"); while(rs4.next()) {
			 * userBean.setStatus(rs4.getString(TableConstants.STATUS)); }
			 */

			// searchResults.add(userBean);
			if (chkOffline) {
				String offDetails = "select is_offline from st_lms_ret_offline_master where user_id= "
						+ userid + " and organization_id= " + orgId;
				System.out.println(offDetails);
				rs4 = stmt6.executeQuery(offDetails);
				if (rs4.next()) {
					userBean.setIsOffline(rs4.getString("is_offline"));
				} else {
					userBean.setIsOffline("NO");
				}
			}
			return userBean;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}
	
	public boolean resetUserLoginAttempts(int userId){
		Connection con = DBConnect.getConnection();
		int rows = 0;
		boolean status = false;
		try {
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement("update st_lms_user_master set login_attempts = 0 where user_id = ?");
			pstmt.setInt(1,userId);
			rows = pstmt.executeUpdate();
			if(rows != 0){
				status = true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return status;
	}
}
