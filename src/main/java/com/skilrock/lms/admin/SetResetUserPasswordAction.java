package com.skilrock.lms.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.admin.common.SetResetUserPasswordHelper;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.web.loginMgmt.AutoGenerate;

public class SetResetUserPasswordAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(SetResetUserPasswordAction.class);
	
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String orgType;
	private String email;
	private int userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String autoPass;
	private String stopLogIn;

	@Override
	public String execute() throws Exception {
		if (!LMSFilterDispatcher.stopLogInUsers) {
			setStopLogIn("Stop LogIn All Users");
		} else {
			setStopLogIn("Allow LogIn All Users");
		}
		return SUCCESS;
	}

	public String search() {
		HttpSession session = getRequest().getSession();
		SetResetUserPasswordHelper helper = new SetResetUserPasswordHelper();
		List<UserDetailsBean> list = helper.fetchUsers(orgType);
		session.setAttribute("USER_LIST", list);
		return SUCCESS;
	}

	public void resetUserPassword() throws LMSException, IOException {
		boolean reset = false;
		if (autoPass == null) {
			reset = true;
			if ("RETAILER".equalsIgnoreCase(orgType)
					&& "integer"
							.equalsIgnoreCase(((String) ServletActionContext
									.getServletContext().getAttribute(
											"RETAILER_PASS")).trim())) {
				autoPass = AutoGenerate.autoPasswordInt() + "";
			} else {
				autoPass = AutoGenerate.autoPassword();
			}
		}
		SetResetUserPasswordHelper helper = new SetResetUserPasswordHelper();
		helper.resetPassword(userId, autoPass, email, userName, firstName,
				lastName);
		PrintWriter out = getResponse().getWriter();
		if (reset) {
			out.print("Reset Successfully.");
		} else {
			out.print("Set Successfully.");
		}
	}

	@SuppressWarnings("unchecked")
	public void logOut() throws IOException {
		System.out.println("I am in Logout");
		HttpSession session = null;
		ServletContext sc = ServletActionContext.getServletContext();
		PrintWriter out = getResponse().getWriter();
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		session = currentUserSessionMap.get(userName);
		if (session == null) {
			out.print("Already Logout");
			return;
		}
		currentUserSessionMap.remove(userName);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userId = userBean.getUserId();
		if (userBean != null) {
			loggedOutUser(userBean.getUserName());
		}
		session.removeAttribute("USER_INFO");
		session.removeAttribute("ACTION_LIST");
		session.removeAttribute("PRIV_MAP");
		session.invalidate();
		session = null;
		System.out.println("Log out Successfully and Session is " + session);
		if (DrawGameOfflineHelper.checkOfflineUser(userId)) {
			if (DrawGameOfflineHelper.updateLogoutSuccess(userName)) {
				out.print("Logout Successfully");
			} else {
				out.print("Already Logout");
			}
			return;
		} else {
			out.print("Logout Successfully");
		}
	}

	@SuppressWarnings("unchecked")
	public void logOutAll() throws IOException {
		System.out.println("I am in Logout All");
		HttpSession session = null;
		ServletContext sc = ServletActionContext.getServletContext();
		PrintWriter out = getResponse().getWriter();
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		int forcedOut = 0;
		if (currentUserSessionMap != null) {
			Iterator<Map.Entry<String, HttpSession>> iter = currentUserSessionMap
					.entrySet().iterator();
			int alreadyOut = 0;
			
			while (iter.hasNext()) {
				Map.Entry<String, HttpSession> pair = (Map.Entry<String, HttpSession>) iter
						.next();
				userName = pair.getKey();
				session = pair.getValue();
				if("admin".equalsIgnoreCase(userName)){
					continue;
				}
				if (session == null || !CommonFunctionsHelper.isSessionValid(session)) {
					alreadyOut++;
					continue;
				}
				
//				UserInfoBean userBean = (UserInfoBean) session
//						.getAttribute("USER_INFO");
//				int userId = userBean.getUserId();
//				if (userBean != null) {
//					loggedOutUser(userBean.getUserName());
//				}
				session.removeAttribute("USER_INFO");
				session.removeAttribute("ACTION_LIST");
				session.removeAttribute("PRIV_MAP");
				session.invalidate();
				session = null;
				System.out.println("Log out Successfully and Session is "
						+ session);
				if (DrawGameOfflineHelper.checkOfflineUser(userId)) {
					if (DrawGameOfflineHelper.updateLogoutSuccess(userName)) {
						forcedOut++;
					} else {
						alreadyOut++;
					}
					return;
				} else {
					forcedOut++;
				}
				// iter.remove();
			}
			
			session = currentUserSessionMap.get("admin");
			currentUserSessionMap = new HashMap<String, HttpSession>();
			currentUserSessionMap.put("admin", session);
			sc.setAttribute("LOGGED_IN_USERS",currentUserSessionMap);
		}
		System.out.println(sc.getAttribute("LOGGED_IN_USERS"));
		out.print(forcedOut + " Users Logout Successfully.");
	}

	public void stopLoginAllUsers() throws IOException {
		LMSFilterDispatcher.stopLogInUsers = (!LMSFilterDispatcher.stopLogInUsers);
		if (!LMSFilterDispatcher.stopLogInUsers) {
			System.out.println("All users login allowed.");
			setStopLogIn("Stop LogIn All Users");
		} else {
			System.out.println("All users login blocked.");
			setStopLogIn("Allow LogIn All Users");
		}
		if(getResponse() != null){
			PrintWriter out = getResponse().getWriter();
			out.print("Successfully Done!!");
		}
	}

	@SuppressWarnings("unchecked")
	private void loggedOutUser(String user) {

		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();
			Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserSessionMap.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
		} else {
			ServletContext sc = ServletActionContext.getServletContext();
			List<String> currentUserList = (List<String>) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserList.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserList);
		}
	}
	
	public void logOutAllRets(){
		logger.info("I am in Logout All Retailers");
		HttpSession session = null;
		Map<String, HttpSession> currentUserSessionMap=null;
		//ServletContext sc = ServletActionContext.getServletContext();
		ServletContext sc = LMSUtility.sc;
		Connection con = DBConnect.getConnection();
		
	try{
			if(sc.getAttribute("LOGGED_IN_USERS")==null)
			{	logger.info(" LOGGED_IN_USERS maps is " + sc.getAttribute("LOGGED_IN_USERS"));
				throw new LMSException();
			}
			currentUserSessionMap = (Map<String, HttpSession>) sc
			.getAttribute("LOGGED_IN_USERS");
			logger.info(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
			if(!currentUserSessionMap.isEmpty())
			{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select user_name from st_lms_user_master where organization_type = 'RETAILER'");
			while(rs.next()){
				String username = rs.getString("user_name").toLowerCase();
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
			}
			sc.setAttribute("LOGGED_IN_USERS",currentUserSessionMap);
			logger.info(sc.getAttribute("LOGGED_IN_USERS"));
			}else{
				logger.info("NO_LOGGED_IN_USERS");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}catch (LMSException e){
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
	
	@SuppressWarnings("unchecked")
	public void logOutRetFromUserId(int userId ,Connection con) throws Exception{

		logger.info("Logout specific retailer with user id "+ userId);
		String username = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ServletContext sc = null;
		HttpSession session = null;
		Map<String, HttpSession> currentUserSessionMap = null;
		try{
			if(currentUserSessionMap != null && !currentUserSessionMap.isEmpty())
			{
			sc = ServletActionContext.getServletContext();
			currentUserSessionMap = (Map<String, HttpSession>) sc.getAttribute("LOGGED_IN_USERS");
			pstmt = con.prepareStatement("select user_name from st_lms_user_master where organization_type = 'RETAILER' and user_id = ?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				username = rs.getString("user_name").toLowerCase();
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
			}
			sc.setAttribute("LOGGED_IN_USERS",currentUserSessionMap);
			logger.info(username + " has been removed from the current user session map");
			logger.info(sc.getAttribute("LOGGED_IN_USERS"));
			}else{
				logger.info("NO_LOGGED_IN_USERS");
			}
		}catch (SQLException e){
			throw e;
		}catch (Exception e){
			throw e;
		}finally{
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(pstmt);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void logOutRet(String userName) throws IOException {
		System.out.println("I am in Logout");
		HttpSession session = null;
		ServletContext sc = ServletActionContext.getServletContext();
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		session = currentUserSessionMap.get(userName);
		if (session == null) {
			return;
		}
		currentUserSessionMap.remove(userName);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userId = userBean.getUserId();
		if (userBean != null) {
			loggedOutUser(userBean.getUserName());
		}
		session.removeAttribute("USER_INFO");
		session.removeAttribute("ACTION_LIST");
		session.removeAttribute("PRIV_MAP");
		session.invalidate();
		session = null;
		System.out.println("Log out Successfully and Session is " + session);
		DrawGameOfflineHelper.checkOfflineUser(userId);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAutoPass() {
		return autoPass;
	}

	public void setAutoPass(String autoPass) {
		this.autoPass = autoPass;
	}

	public String getStopLogIn() {
		return stopLogIn;
	}

	public void setStopLogIn(String stopLogIn) {
		this.stopLogIn = stopLogIn;
	}

}
