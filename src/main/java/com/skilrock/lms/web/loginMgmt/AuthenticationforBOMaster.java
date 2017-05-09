package com.skilrock.lms.web.loginMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.AuthenticationHelperforBOMaster;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class AuthenticationforBOMaster extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	static Log logger = LogFactory.getLog(AuthenticationforBOMaster.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;

	private HttpServletResponse response;

	private String username = null;

	public String authentication() {
		LoginBean loginBean = null;
		logger.debug("BoMASTER Logging as "+username+" **************");
		// getHttpsSession();
		String uname = getUsername().toLowerCase(); // stores the username
		HttpSession session = null;
		session = getRequest().getSession();
		session.setMaxInactiveInterval(3600); // adde by yogesh
		session.setAttribute("ROOT_PATH", ServletActionContext
				.getServletContext().getRealPath("/").toString());
		session.setAttribute("date_format", ServletActionContext
				.getServletContext().getAttribute("date_format"));

		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());

		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (!userInfo.getUserName().equalsIgnoreCase("BOMASTER")) {
			return ERROR;
		} else {
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserSessionMap.remove(userInfo.getUserName().toLowerCase());
		}
		AuthenticationHelperforBOMaster loginAuth = new AuthenticationHelperforBOMaster();
		loginBean = loginAuth.loginAuthentication(uname, "WEB", session.getId());
		String returntype = loginBean.getStatus();

		logger.debug("The user login is " + returntype);

		if (returntype.equals("ERROR_TIME_LIMIT")) {
			errorMap(session.getId(), "Login Not Allowed.");
			return ERROR;
		}
		if (returntype.equals("success") || returntype.equals("FirstTime")) {
			loggedInUser(uname, session);
			userInfo = loginBean.getUserInfo();
			HashMap actionServiceMap = loginBean.getActionServiceMap();
			ArrayList<String> userActionList = loginBean.getUserActionList();

			if ("Yes".equalsIgnoreCase((String) ServletActionContext
					.getServletContext().getAttribute("RET_OFFLINE"))) {
				if (DrawGameOfflineHelper
						.checkOfflineUser(userInfo.getUserId())) {
					logger.debug("USER status is set to OFFLINE");
					errorMap(session.getId(), "USER status is set to OFFLINE");
					return ERROR;

				}
			}

			session.setAttribute("USER_INFO", userInfo);
			session.setAttribute("PRIV_MAP", actionServiceMap);
			session.setAttribute("ACTION_LIST", userActionList);

			logger.debug("******actionServiceMap" + actionServiceMap);
			// check priv on web
			if (actionServiceMap.size() == 0) {
				errorMap(session.getId(),
						"You are not Authorized to access WEB");
				return ERROR;
			}
			// check responsible gaming limits
			if ("RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
				logger.debug("********check RG on LogIn");

				String rgResult = ResponsibleGaming.chkRGCriteOnLogIn(userInfo);
				logger.debug("*****RG RESULT*******\n" + rgResult);
				if (!"SUCCESS".equalsIgnoreCase(rgResult)) {
					errorMap(session.getId(), rgResult);
					return ERROR;
				}
			}

			if (returntype.equals("FirstTime")) {
				session.setAttribute("FIRST", true);
				return "SuccessFirstTime";
			}

			return "success";
		} else if (returntype.equals("BALANCE_NOT_POSITIVE")) {
			errorMap(session.getId(),
					"Your Balance is Negative.Please Contact BO");
			return ERROR;
		} else if (returntype.equals("ERROR")
				|| returntype.equals("USER_NAME_NOT_MATCH")
				|| returntype.equals("PASS_NOT_MATCH")) {
			// addActionError("Please Enter Correct Login Name and Password
			// !!");
			errorMap(session.getId(),
					"Please Enter Correct Login Name and Password !!");
			logger.debug("inside error block");
			return ERROR;
		} else if (returntype.equals("ERRORINACTIVE")) {
			// addActionError("Your status has been set to Inactive or Terminate
			// Please contact Back Office immediately");
			errorMap(
					session.getId(),
					"Your status has been set to Inactive or Terminate Please contact  Back Office immediately");
			return ERROR;
		} else if (returntype.equals("ALREADY_LOGGED_IN")) {
			return "ALREADY_LOGGED_IN";
		} else if (returntype.equals("TIER_INACTIVE")) {
			logger.debug("TIER Status is set to INACTIVE");
			return "TIER_INACTIVE";
		}

		addActionError("Enter Correct name password");
		errorMap(session.getId(), "Enter Correct name password");
		return ERROR;

	}

	public String createCookie(String user) {
		boolean found = false;
		Cookie userSessionId = null;
		String oldUser = null;
		if (request.getCookies() != null) {
			Cookie[] cookies = request.getCookies();
			for (Cookie element : cookies) {
				userSessionId = element;
				logger.debug("In create Cookies and Cookies Found are"
						+ userSessionId);
				if (userSessionId.getName().equals("LMSCookie")) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			userSessionId = new Cookie("LMSCookie", user);
			oldUser = user;
			userSessionId.setMaxAge(24 * 60 * 60);
			userSessionId.setPath("/");
			response.addCookie(userSessionId);
			logger
					.debug("In create Cookies IF and Cookies Not Found Created New Cookie"
							+ userSessionId);
		} else {
			oldUser = userSessionId.getValue();
			userSessionId.setMaxAge(24 * 60 * 60);
			userSessionId.setValue(user);
			userSessionId.setPath("/");
			response.addCookie(userSessionId);
			logger
					.debug("In create Cookies Else and Cookies Found and oldUser is "
							+ oldUser + "--New User is " + user);
		}
		return oldUser;

	}

	public void errorMap(String sessionId, String error) {
		ServletContext sc = ServletActionContext.getServletContext();
		Map errorSessionMap = (Map) sc.getAttribute("ERROR_SESSION_MAP");
		if (errorSessionMap == null) {
			errorSessionMap = new HashMap();
		}
		errorSessionMap.put(sessionId, error);
		sc.setAttribute("ERROR_SESSION_MAP", errorSessionMap);
	}

	public void getOrgUserNameList() throws IOException, LMSException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		if (uib.getUserName().equalsIgnoreCase("BOMASTER")) {
			String agtOrgType = AuthenticationHelperforBOMaster
					.getOrgUserList();

			logger.debug(agtOrgType);
			response.setContentType("text/html");
			out.print(agtOrgType);
		}
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * This method is for tracking the Number of Current users Logged In and
	 * maintaining session using Map and allowing the User to LogIN at other
	 * place simultaneously and timing out the Session at previous place user
	 * has logged in.
	 * 
	 * @param user
	 *            stand's for Login Name
	 * @return void
	 */
	public void loggedInUser(String user, HttpSession session) {
		String oldUser = null;
		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();

			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				oldUser = createCookie(user);
				logger.debug("I am in if");
				currentUserSessionMap = new HashMap();
				if (oldUser.equals(user)) {
					currentUserSessionMap.put(user, session);
				} else {
					currentUserSessionMap.put(user, session);
					currentUserSessionMap.put(oldUser, session);
				}
				sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);

				logger.debug("In If User is --" + user + " Session Id --"
						+ session.getId());
			} else {
				if (currentUserSessionMap.containsKey(user)) {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.put(user, session);
					} else {
						currentUserSessionMap.put(user, session);
						currentUserSessionMap.put(oldUser, session);
					}
					logger.debug("In Else If User is --" + user
							+ " Session Id --" + session.getId());
				} else {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.put(user, session);
					} else {
						currentUserSessionMap.put(user, session);
						currentUserSessionMap.put(oldUser, session);
					}
					logger.debug("In Else else User is --" + user
							+ " Session Id --" + session.getId());

				}
			}

		}

	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setUsername(String value) {
		username = value;
	}

}