package com.skilrock.lms.web.loginMgmt;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.coreEngine.loginMgmt.common.WebAuthenticationHelper;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class WebAuthenticationAction extends BaseAction{

	public WebAuthenticationAction() {
		super(WebAuthenticationAction.class.getName());
		// TODO Auto-generated constructor stub
	}

	static Log logger = LogFactory.getLog(WebAuthenticationAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String json;
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String authentication() throws Exception {
		LoginBean loginBean = null;
		int sessionTimeOut = 3600;
		JSONObject jsonResponse = new JSONObject();
		String uname = null ;
		String password = null ;
		response.setContentType("application/json");
		PrintWriter out = null;
		out = response.getWriter();
		if(json == null){
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", "No Input Provided");
			//jsonResponse.put("data", "");
			out.print(jsonResponse);
			out.flush();
			out.close();
			return SUCCESS;
		}
		JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
		if(!requestData.containsKey("userName") || !requestData.containsKey("password")){
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", "Please provide proper credentials.");
		//	jsonResponse.put("data", "");
			out.print(jsonResponse);
			out.flush();
			out.close();
			return SUCCESS;
		}
		uname = ((String) requestData.get("userName")).toLowerCase();
		password = (String) requestData.get("password");
		
		HttpSession session = null;
		session = getRequest().getSession();
		if("RETAILER".equalsIgnoreCase(Util.getUserTypeFromUsername(uname)))
			sessionTimeOut = 28800;
		session.setMaxInactiveInterval(sessionTimeOut); // added by yogesh
		
		WebAuthenticationHelper loginAuth = new WebAuthenticationHelper();
		loginBean = loginAuth.loginAuthentication(uname, password, "WEB", (String) ServletActionContext.getServletContext().getAttribute("LOGIN_ATTEMPTS"),session.getId(),true);
		String returntype = loginBean.getStatus();
		session.setAttribute("ACTION_LIST", loginBean.getUserActionList());
		session.setAttribute("PRIV_MAP", loginBean.getActionServiceMap());
		
		logger.info("The user login is " + returntype);

		if (returntype.equals("LOGIN_LIMIT_REACHED")) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", getText("msg.login.limit"));
			//jsonResponse.put("data", "");
		}
		if (returntype.equals("ERROR_TIME_LIMIT")) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", getText("msg.login.allowed"));
		//	jsonResponse.put("data", "");
		}
		if (returntype.equals("success")) {
			UserInfoBean userInfo = loginBean.getUserInfo();
			JSONObject data = new JSONObject() ;
			data.put("retailerName", userInfo.getUserName());
			data.put("availableBalance", userInfo.getAvailableCreditLimit() - userInfo.getClaimableBal());
			data.put("retailerOrgCode", userInfo.getUserOrgId());
			data.put("sessionId", session.getId());
			jsonResponse.put("isSuccess", true) ;
			jsonResponse.put("errorMsg", "") ;
			jsonResponse.put("data", data) ;
			loggedInUser(uname, session);
			session.setAttribute("USER_INFO", userInfo);
		} else if (returntype.equals("BALANCE_NOT_POSITIVE")) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", getText("msg.login.balance"));
			//jsonResponse.put("data", "");
		} else if (returntype.equals("ERROR") || returntype.equals("USER_NAME_NOT_MATCH") || returntype.equals("PASS_NOT_MATCH")) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", getText("msg.login.error"));
			//jsonResponse.put("data", "");
		} else if (returntype.equals("ERRORINACTIVE")) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", getText("msg.login.inactive"));
			//jsonResponse.put("data", "");
		} else if (returntype.equals("ALREADY_LOGGED_IN")) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", getText("error.already.login"));
			//jsonResponse.put("data", "");
		}else{
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorMsg", getText("msg.login.credential"));
			//jsonResponse.put("data", "");
		}
		logger.info("Response Data:"+ jsonResponse.toString());
		out.print(jsonResponse);
		out.flush();
		out.close();
		return SUCCESS;
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
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				oldUser = createCookie(user);
				currentUserSessionMap = new HashMap();
				if (oldUser.equals(user)) {
					currentUserSessionMap.put(user, session);
				} else {
					currentUserSessionMap.put(user, session);
					currentUserSessionMap.put(oldUser, session);
				}
				sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
				System.out.println(" LOGGED_IN_USERS maps is "+ currentUserSessionMap);
				logger.debug("In If User is --" + user + " Session Id --"+ session.getId());
			} else {
				if (currentUserSessionMap.containsKey(user)) {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.remove(user);
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
	

}