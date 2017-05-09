package com.skilrock.lms.admin.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;

public class SetResetUserPasswordHelper {

	public List<UserDetailsBean> fetchUsers(String orgType) {
		Connection con = DBConnect.getConnection();
		UserDetailsBean userBean=null;
		List<UserDetailsBean> beanList=new ArrayList<UserDetailsBean>();
		try {
			String qry="select um.user_id,um.organization_type,user_name,status,name,email_id,first_name,last_name from st_lms_user_master um inner join st_lms_organization_master om inner join st_lms_user_contact_details uc on um.organization_id=om.organization_id and um.user_id=uc.user_id where um.organization_type=?  and um.status !='TERMINATE'";
			PreparedStatement pstmt=con.prepareStatement(qry);
			pstmt.setString(1, orgType);
			ResultSet rs=pstmt.executeQuery();
			
			while (rs.next()) {
				userBean=new UserDetailsBean();
				userBean.setUserId(rs.getInt("user_id"));
				userBean.setUserName(rs.getString("user_name"));
				userBean.setOrgStatus(rs.getString("status"));
				userBean.setOrgName(rs.getString("name"));
				userBean.setFirstName(rs.getString("first_name"));
				userBean.setLastName(rs.getString("last_name"));
				userBean.setEmailId(rs.getString("email_id"));
				userBean.setOrgType(rs.getString("organization_type"));
				beanList.add(userBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return beanList;
	}

	public void resetPassword(int userid, String autoPass, String email,
			String userName, String firstName, String lastName)
			throws LMSException {
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
			DBConnect.closeCon(con);
		}

	}
	
	public static void logOutAllRetsOfAgent(int AgentOrgId){
		System.out.println("I am in Logout All Retailers");
		HttpSession session = null;
		ServletContext sc = ServletActionContext.getServletContext();
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) LMSUtility.sc
		.getAttribute("LOGGED_IN_USERS");
		System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		Connection con = DBConnect.getConnection();
		try{
			if(currentUserSessionMap != null){
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select user_name,um.organization_type from st_lms_user_master um,st_lms_organization_master om where um.organization_id=om.organization_id and (om.organization_id= "+AgentOrgId+" or om.parent_id= "+AgentOrgId+" )");
			while(rs.next()){
				String username = rs.getString("user_name");
				try{
				if(currentUserSessionMap.containsKey(username)){
					session = currentUserSessionMap.get(username);
					if (CommonFunctionsHelper.isSessionValid(session)) {
						session.removeAttribute("USER_INFO");
						session.removeAttribute("ACTION_LIST");
						session.removeAttribute("PRIV_MAP");
						session.invalidate();
						session = null;					
						}
					
					currentUserSessionMap.remove(username);
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			LMSUtility.sc.setAttribute("LOGGED_IN_USERS",currentUserSessionMap);
			System.out.println(LMSUtility.sc.getAttribute("LOGGED_IN_USERS"));
			}
		
		}catch (SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
