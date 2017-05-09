package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;

public class SearchBOUserHelper {
	private Log logger = LogFactory.getLog(SearchBOUserHelper.class);

	public Map dispSearchBoUser() throws LMSException {
		Map roleMap = new TreeMap();

		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select role_id,role_description from st_lms_role_master where"
							+ " tier_id = (select tier_id from st_lms_tier_master where tier_code='BO') order by role_name");
			while (rs.next()) {

				roleMap.put(rs.getInt("role_id"), rs
						.getString("role_description"));
			}
			DBConnect.closeCon(con);
		} catch (Exception e) {
			throw new LMSException(e);
		}
		return roleMap;
	}

	public void editBOUserDetails(int user_id, String emailId, String phoneNbr,
			String status, String type, int doneByUserId, String comments, String requestIp) throws LMSException {

		Connection con = DBConnect.getConnection();
		Statement stmt;
		ResultSet rs;
		String prevEmailId = null;
		String prevPhoneNbr = null;
		String prevStatus = null;
		HistoryBean historyBean = null;
		try {
			con.setAutoCommit(false);
			stmt = con.createStatement();
			logger.info("Type - "+type+" | Mailing User - "+type.equals("Mailing Users"));
			if (!type.equals("Mailing Users")) {
				String selectDetailsQuery = "SELECT email_id, phone_nbr, status FROM st_lms_user_master um INNER JOIN st_lms_user_contact_details ucd ON um.user_id=ucd.user_id WHERE um.user_id="+user_id+";";
				logger.info("Select Details Query - "+selectDetailsQuery);
				rs = stmt.executeQuery(selectDetailsQuery);
				if(rs.next()) {
					prevEmailId = (rs.getString("email_id")==null)?"":rs.getString("email_id").trim();
					prevPhoneNbr = (rs.getString("phone_nbr")==null)?"":rs.getString("phone_nbr").trim();
					prevStatus = rs.getString("status");
				}

				historyBean = new HistoryBean(user_id, doneByUserId, comments, requestIp);
				if(!prevEmailId.equals(emailId)) {
					historyBean.setChangeType("EMAIL_ID");
					historyBean.setChangeValue(prevEmailId);
					historyBean.setUpdatedValue(emailId);
					CommonMethods.insertUpdateUserHistory(historyBean, con);
				}
				if(!prevPhoneNbr.equals(phoneNbr)) {
					historyBean.setChangeType("PHONE_NUMBER");
					historyBean.setChangeValue(prevPhoneNbr);
					historyBean.setUpdatedValue(phoneNbr);
					CommonMethods.insertUpdateUserHistory(historyBean, con);
				}
				if(!prevStatus.equals(status)) {
					historyBean.setChangeType("USER_STATUS");
					historyBean.setChangeValue(prevStatus);
					historyBean.setUpdatedValue(status);
					CommonMethods.insertUpdateUserHistory(historyBean, con);
				}

				String updateEmailReport = "update st_lms_report_email_user_master set email_id='"
						+ emailId
						+ "',mob_no='"
						+ phoneNbr
						+ "',status='"
						+ status + "' where ref_user_id=" + user_id;
				logger.info("update query for report table - " + updateEmailReport);
				stmt.executeUpdate(updateEmailReport);
			} else {
				String updateContactDetailsQuery = "update st_lms_report_email_user_master set email_id='"
						+ emailId
						+ "',mob_no='"
						+ phoneNbr
						+ "',status='"
						+ status + "' where user_id=" + user_id;
				logger.info("update query for report table - " + updateContactDetailsQuery);
				stmt.executeUpdate(updateContactDetailsQuery);
			}
			con.commit();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sql Exception", se);
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public void resetPassBO(int userid, String autoPass, String email,
			String userName, String firstName, String lastName)
			throws LMSException {
		String projectName = ServletActionContext.getServletContext()
				.getContextPath();
		Connection con = DBConnect.getConnection();
		try {
			System.out.println("inside helper usr pass");
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			String updatePass = QueryManager.updateST3UserPass()
					+ " auto_password='1', password='"
					+ MD5Encoder.encode(autoPass) + "'where user_id='" + userid
					+ "'";
			stmt.executeUpdate(updatePass);
			System.out.println("query ::" + updatePass);
			// reset password
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.insertST3PasswordHistory());
			pstmt.setInt(1, userid);
			pstmt.setString(2, MD5Encoder.encode(autoPass));
			pstmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			pstmt.setString(4, "1");
			pstmt.execute();
			System.out.println("query 2 :: " + pstmt);
			con.commit();
			String msgFor = "Welcome to our gaming system Your password has been reset your login details are";
			// MailSend.sendMailToUser(email, autoPass, userName);
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
			e.printStackTrace();
			// throw new LMSException(e);

		} finally {
			try {
				if (con != null) {
					con.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("Error During closing connection", se);
			}
		}

	}
	
	public boolean resetLoginAttemptsForBOUser(int userId){
		boolean status = false;
		Connection con = DBConnect.getConnection();
		int rows = 0;
		try {
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement("update st_lms_user_master set login_attempts = 0 where user_id = ? and organization_type='BO'");
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

	public List<UserDetailsBean> searchBousers(String userName, int roleId,
			String type, String status) throws LMSException {
		 
		Connection con = DBConnect.getConnection();
		Statement stmt;
		Statement emailStmt;
		List<UserDetailsBean> userList = new ArrayList<UserDetailsBean>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		try {
			stmt = con.createStatement();
			emailStmt = con.createStatement();
			if (type != null && !"".equals(type)
					&& type.equals("Mailing Users")) {

				StringBuilder searchQuery = new StringBuilder(
						"SELECT eum.registration_date, c.user_name register_by_name, eum.first_name, eum.last_name, eum.status, eum.user_id FROM st_lms_report_email_user_master eum INNER JOIN st_lms_user_master um ON eum.ref_user_id = um.user_id inner join st_lms_user_master c on um.register_by_id = c.user_id WHERE 1=1 AND ref_user_id IS NULL ");
				if (userName != null && !"".equals(userName)) {
					searchQuery.append(" and  eum.first_name like '" + userName
							+ "%'");
				}

				if (status != null && !"".equals(status)) {
					searchQuery.append(" and  eum.status='" + status + "'");
				}

				searchQuery.append(" order by eum.first_name");

				System.out.println("Query :: " + searchQuery);

				ResultSet rs = stmt.executeQuery(searchQuery.toString());
				UserDetailsBean userBean;

				while (rs.next()) {
					userBean = new UserDetailsBean();
					userBean.setFirstName(rs.getString("first_name"));
					userBean.setLastName(rs.getString("last_name"));
					// userBean.setEmailId(rs.getString("email_id"));
					// userBean.setPhoneNbr(rs.getLong("mob_no"));
					userBean.setStatus(rs.getString("status"));
					userBean.setUserId(rs.getInt("user_id"));
					// userBean.setUserName("NA");
					userBean.setDepartment("Report Email");
					userBean.setBoUserType("Mailing Users");
					userBean.setMailingStatus("Y");
					userBean.setRegisterByUserName(rs.getString("register_by_name"));
					userBean.setRegistrationDate(df.format(df.parse(rs.getString("registration_date"))));

					/*
					 * userBean.setUserName(rs.getString("user_name"));
					 * userBean.setUserId(rs.getInt("user_id")); String
					 * isroleHead=rs.getString("isrolehead");
					 * 
					 * if(isroleHead.equals("Y"))
					 * userBean.setBoUserType("Head"); else
					 * if(isroleHead.equals("N")) userBean.setBoUserType("Not
					 * Head");
					 * 
					 * userBean.setDepartment(rs.getString("role_name"));
					 */

					userList.add(userBean);
				}

			} else {
				StringBuilder searchQuery = new StringBuilder(
						"select a.registration_date, um.user_name register_by_name, a.user_name,a.user_id,a.isrolehead,a.status,b.first_name,b.last_name,b.email_id,b.phone_nbr,c.role_name,c.role_description from st_lms_user_master a INNER JOIN st_lms_user_master um ON a.register_by_id = um.user_id,st_lms_user_contact_details b,st_lms_role_master c where 1=1 and a.user_id= b.user_id and a.role_id=c.role_id and a.organization_type='BO' ");

				if (userName != null && !"".equals(userName.trim())) {
					String nameArr[] = userName.trim().split(" ");
					boolean flag = true;
					int count = 0;
					for (int i = 1; i < nameArr.length; i++) {
						if (!"".equals(nameArr[i].trim())) {
							count += 1;
						}
						if (count > 1) {
							flag = false;
							searchQuery.append(" and b.first_name ='' ");

						}
					}
					if (flag) {
						searchQuery.append(" and (b.first_name like '%")
								.append(nameArr[0].trim()).append("%' ");
						for (int i = 1; i < nameArr.length; i++) {
							if (!"".equals(nameArr[i].trim())) {
								searchQuery.append(" and b.last_name like '%")
										.append(nameArr[i].trim())
										.append("%' ");

							}
						}
						searchQuery.append(" )");
					}

					/*
					 * + "%' or b.last_name like '%" + userName + "%')");
					 */
					System.out.println(" first11111111111111 name is "
							+ userName + " :: " + searchQuery);
				}
				if (roleId != -1) {
					searchQuery.append(" and a.role_id=" + roleId + "");
				}

				if (type != null && !"".equals(type)) {
					if (type.equals("Head")) {
						searchQuery.append(" and  a.isrolehead='Y'");
					} else if (type.equals("Sub Users")) {
						searchQuery.append(" and  a.isrolehead='N'");
					}
				}

				if (status != null && !"".equals(status)) {
					searchQuery.append(" and  a.status='" + status + "'");
				}
				searchQuery.append(" order by first_name");
				System.out.println("Query :: " + searchQuery);

				ResultSet rs = stmt.executeQuery(searchQuery.toString());
				UserDetailsBean userBean;
				String mailingStatus;
				String userDepartment;
				while (rs.next()) {
					mailingStatus = "N";
					userBean = new UserDetailsBean();
					userBean.setFirstName(rs.getString("first_name"));
					userBean.setLastName(rs.getString("last_name"));
					// userBean.setEmailId(rs.getString("email_id"));
					// userBean.setPhoneNbr(rs.getLong("phone_nbr"));
					// userBean.setUserName(rs.getString("user_name"));
					userBean.setUserId(rs.getInt("user_id"));

					// get reeport mailing status from reprt email table
					ResultSet rsMailStatus = emailStmt
							.executeQuery("select ref_user_id from st_lms_report_email_user_master where ref_user_id="
									+ rs.getInt("user_id"));
					if (rsMailStatus.next()) {
						// System.out.println("inside " +
						// rsMailStatus.getInt("ref_user_id"));
						mailingStatus = "Y";
					}
					userBean.setMailingStatus(mailingStatus);
					userBean.setStatus(rs.getString("status"));
					String isroleHead = rs.getString("isrolehead");

					if (isroleHead.equals("Y")) {
						userBean.setBoUserType("Head");
					} else if (isroleHead.equals("N")) {
						userBean.setBoUserType("Not Head");
					}
					userDepartment = rs.getString("role_description");

					userBean.setDepartment(userDepartment);
					userBean.setRegistrationDate(df.format(df.parse(rs.getString("registration_date"))));
					userBean.setRegisterByUserName(rs.getString("register_by_name"));
					userList.add(userBean);
				}

				// to search mailing users also if type All is selected
				if (type != null && !"".equals(type) && type.equals("All")) {

					StringBuilder searchMailingUsersQuery = new StringBuilder(
							"SELECT eum.registration_date, c.user_name register_by_name, eum.first_name, eum.last_name, eum.status, eum.user_id FROM st_lms_report_email_user_master eum INNER JOIN st_lms_user_master um ON eum.ref_user_id = um.user_id INNER JOIN st_lms_user_master c ON um.register_by_id = c.user_id WHERE 1=1 AND ref_user_id IS NULL ");
					if (userName != null && !"".equals(userName)) {
						searchMailingUsersQuery
								.append(" and  first_name like '" + userName
										+ "%'");
					}

					if (status != null && !"".equals(status)) {
						searchMailingUsersQuery.append(" and  eum.status='"
								+ status + "'");
					}

					searchMailingUsersQuery.append(" order by first_name");

					System.out.println("Query for mailing users also :: "
							+ searchMailingUsersQuery);

					ResultSet rsmailingUsers = stmt
							.executeQuery(searchMailingUsersQuery.toString());
					// UserDetailsBean userBean;

					while (rsmailingUsers.next()) {
						userBean = new UserDetailsBean();
						userBean.setFirstName(rsmailingUsers
								.getString("first_name"));
						userBean.setLastName(rsmailingUsers
								.getString("last_name"));
						// userBean.setEmailId(rs.getString("email_id"));
						// userBean.setPhoneNbr(rs.getLong("mob_no"));
						userBean.setStatus(rsmailingUsers.getString("status"));
						userBean.setUserId(rsmailingUsers.getInt("user_id"));
						// userBean.setUserName("NA");
						userBean.setDepartment("Report Email");
						userBean.setBoUserType("Mailing Users");
						userBean.setMailingStatus("Y");
						userBean.setRegisterByUserName(rs.getString("register_by_name"));
						userBean.setRegistrationDate(df.format(df.parse(rs.getString("registration_date"))));

						/*
						 * userBean.setUserName(rs.getString("user_name"));
						 * userBean.setUserId(rs.getInt("user_id")); String
						 * isroleHead=rs.getString("isrolehead");
						 * 
						 * if(isroleHead.equals("Y"))
						 * userBean.setBoUserType("Head"); else
						 * if(isroleHead.equals("N"))
						 * userBean.setBoUserType("Not Head");
						 * 
						 * userBean.setDepartment(rs.getString("role_name"));
						 */

						userList.add(userBean);
					}

				}

			}
			System.out.println(userList.size());
			return userList;

		} catch (Exception se) {
			se.printStackTrace();
			throw new LMSException("sql Exception", se);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("sql Exception", see);
			}
		}
		// return null;
	}

	public UserDetailsBean showBOUserDetails(int user_id, String userType)
			throws LMSException {
		 
		Connection con = DBConnect.getConnection();
		Statement stmt;
		Statement emailStmt;
		UserDetailsBean userBean = null;
		try {
			stmt = con.createStatement();
			emailStmt = con.createStatement();
			if (!userType.equals("Mailing Users")) {
				String detailsQuery = "select a.user_name,a.user_id,a.isrolehead,a.status,b.first_name,b.last_name,b.email_id,b.phone_nbr,c.role_name,c.role_description from st_lms_user_master a,st_lms_user_contact_details b,st_lms_role_master c where 1=1 and a.user_id= b.user_id and a.role_id=c.role_id and a.user_id="
						+ user_id;
				ResultSet rs = stmt.executeQuery(detailsQuery);
				String userDepartment;
				String mailingStatus = "N";
				while (rs.next()) {
					userBean = new UserDetailsBean();

					userBean.setFirstName(rs.getString("first_name"));
					userBean.setLastName(rs.getString("last_name"));
					userBean.setEmailId(rs.getString("email_id"));
					userBean.setPhoneNbr(rs.getLong("phone_nbr"));
					userBean.setUserName(rs.getString("user_name"));
					userBean.setUserId(rs.getInt("user_id"));

					// get reeport mailing status from reprt email table
					ResultSet rsMailStatus = emailStmt
							.executeQuery("select ref_user_id from st_lms_report_email_user_master where ref_user_id="
									+ rs.getInt("user_id"));
					if (rsMailStatus.next()) {
						mailingStatus = "Y";
					}

					userBean.setMailingStatus(mailingStatus);
					userBean.setStatus(rs.getString("status"));
					String isroleHead = rs.getString("isrolehead");

					if (isroleHead.equals("Y")) {
						userBean.setBoUserType("Head");
					} else if (isroleHead.equals("N")) {
						userBean.setBoUserType("Not Head");
					}

					userDepartment = rs.getString("role_description");

					/*
					 * if(userDepartment.equals("BO_ADM"))
					 * userDepartment="Admin"; else
					 * if(userDepartment.equals("BO_ACT"))
					 * userDepartment="Account"; else
					 * if(userDepartment.equals("BO_INV"))
					 * userDepartment="Inventory"; else
					 * if(userDepartment.equals("BO_MAS"))
					 * userDepartment="Master";
					 */

					userBean.setDepartment(userDepartment);

				}
			} else {
				String detailsQuery = "select a.first_name,a.user_id,a.last_name,a.email_id,a.mob_no,a.status from  st_lms_report_email_user_master a where a.user_id="
						+ user_id;
				ResultSet rs = stmt.executeQuery(detailsQuery);
				while (rs.next()) {
					userBean = new UserDetailsBean();

					userBean.setFirstName(rs.getString("first_name"));
					userBean.setLastName(rs.getString("last_name"));
					userBean.setEmailId(rs.getString("email_id"));
					userBean.setPhoneNbr(rs.getLong("mob_no"));
					// userBean.setUserName(rs.getString("user_name"));
					userBean.setUserId(rs.getInt("user_id"));
					userBean.setStatus(rs.getString("status"));
					// String isroleHead=rs.getString("isrolehead");
					userBean.setMailingStatus("Y");
					userBean.setBoUserType(userType);
					userBean.setDepartment("Report Email");

				}

			}
			return userBean;

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sql Exception", se);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("sql Exception", see);
			}
		}

		// return null;

	}

	public boolean verifyEmail(String emailId, int user_id) throws LMSException {
		 
		Connection con = DBConnect.getConnection();
		Statement stmt;

		if (emailId == null || "".equals(emailId)) {
			return false;
		}

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select email_id from st_lms_report_email_user_master where ref_user_id is null and user_id !="
							+ user_id + " and email_id='" + emailId + "'");
			if (rs.next()) {
				return false;
			} else {
				return true;
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sql Exception", se);

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("sql Exception", see);
			}
		}
	}

}