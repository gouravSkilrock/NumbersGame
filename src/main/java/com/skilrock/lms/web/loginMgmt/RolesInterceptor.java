/*
 * $Id: RolesInterceptor.java,v 1.2 2016/10/31 09:45:57 neeraj Exp $
 *
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.skilrock.lms.web.loginMgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;

/**
 * <!-- START SNIPPET: description --> This interceptor ensures that the action
 * will only be executed if the user has the correct role. <!-- END SNIPPET:
 * description -->
 * 
 * <p/> <u>Interceptor parameters:</u>
 * 
 * <!-- START SNIPPET: parameters -->
 * 
 * <ul>
 * 
 * <li>allowedRoles - a comma-separated list of roles to allow</li>
 * 
 * <li>disallowedRoles - a comma-separated list of roles to disallow</li>
 * 
 * </ul>
 * 
 * <!-- END SNIPPET: parameters -->
 * 
 * <!-- START SNIPPET: extending --> There are two extensions to the existing
 * interceptor:
 * <ul>
 * <li>isAllowed(HttpServletRequest,Object) - whether or not to allow the
 * passed action execution with this request</li>
 * <li>handleRejection(ActionInvocation) - handles an unauthorized request.</li>
 * </ul>
 * <!-- END SNIPPET: extending -->
 * 
 * <pre>
 *  &lt;!-- START SNIPPET: example --&gt;
 *  &lt;!-- only allows the admin and member roles --&gt;
 *  &lt;action name=&quot;someAction&quot; class=&quot;com.examples.SomeAction&quot;&gt;
 *      &lt;interceptor-ref name=&quot;completeStack&quot;/&gt;
 *      &lt;interceptor-ref name=&quot;roles&quot;&gt;
 *        &lt;param name=&quot;allowedRoles&quot;&gt;admin,member&lt;/param&gt;
 *      &lt;/interceptor-ref&gt;
 *      &lt;result name=&quot;success&quot;&gt;good_result.ftl&lt;/result&gt;
 *  &lt;/action&gt;
 *  &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
public class RolesInterceptor extends AbstractInterceptor {
	static Log logger = LogFactory.getLog(RolesInterceptor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> allowedRoles = new ArrayList<String>();
	private List<String> disallowedRoles = new ArrayList<String>();
	private HttpServletRequest request;
	private HttpServletResponse response;

	public void createCookie() {
		boolean found = false;
		Cookie userSessionId = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie element : cookies) {
			userSessionId = element;
			if (userSessionId.getName().equals("LMSCookie")) {
				found = true;
				break;
			}
			if (!found) {
				userSessionId = new Cookie("LMSCookie", "");
				userSessionId.setMaxAge(24 * 60 * 60);
				userSessionId.setPath("/");
				response.addCookie(userSessionId);
			} else {
				userSessionId.setMaxAge(24 * 60 * 60);
				userSessionId.setPath("/");
				response.addCookie(userSessionId);
			}

		}

	}

	/**
	 * Handles a rejection by sending a 403 HTTP error
	 * 
	 * @param invocation
	 *            The invocation
	 * @return The result code
	 * @throws Exception
	 */
	protected String handleRejection(ActionInvocation invocation,
			HttpServletResponse response) throws Exception {
		// logger.debug("I am in Allowed roles444444444444444444444");
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
		return null;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		// logger.debug("1");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			if (isSessionTimeOut(request.getSession())) {

				return "SESSION_TIME_OUT";
			}
			if (!isSessionValid(request.getSession())) {
				request.getSession().setAttribute("isSessionValid", "NO");
				return "ALREADY_LOGGED_IN";
			}

		}
		String result = null;
		if (!isAllowed(request, invocation.getAction())) {
			result = handleRejection(invocation, response);
		} else {
			result = invocation.invoke();

		}
		request.getSession().setAttribute("isSessionValid", "YES");
		return result;
	}

	/**
	 * Determines if the request should be allowed for the action
	 * 
	 * @param request
	 *            The request
	 * @param action
	 *            The action object
	 * @return True if allowed, false otherwise
	 */
	protected boolean isAllowed(HttpServletRequest request, Object action) {
		HttpSession session = request.getSession();
		// logger.debug("allowedRoles::" + allowedRoles);
		// allowedRoles.add("AGT_MAS");
		// logger.debug("allowedRoles::" + allowedRoles);

		if (allowedRoles.size() > 0) {
			boolean result = false;

			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			String userRole = userBean.getRoleName();
			for (String role : allowedRoles) {
				if (role.equals(userRole)) {
					result = true;
				}// logger.debug("I am in Allowed roles"+role);
			}

			// logger.debug("I am in Allowed roles"+result);
			return result;
		} else if (disallowedRoles.size() > 0) {
			for (String role : disallowedRoles) {
				UserInfoBean userBean = (UserInfoBean) session
						.getAttribute("USER_INFO");
				String userRole = userBean.getRoleName();
				if (role.equals(userRole)) {
					return false;
				}// logger.debug("I am in disAllowed
				// roles222222222222222"+disallowedRoles);
			}
		}
		// logger.debug("I am in Allowed roles33333333333333333333");
		return true;
	}

	public boolean isSessionTimeOut(HttpSession session) {
		HttpSession sessionNew = null;
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (userBean == null) {
			return true;
		}
		if (currentUserSessionMap != null && userBean != null) {
			sessionNew = (HttpSession) currentUserSessionMap.get(userBean
					.getUserName());
		}
		// logger.debug("In Else If New is --"+sessionNew+" Session Current
		// --"+session);
		// logger.debug("The User in Map are"+currentUserSessionMap );
		if (sessionNew == null) {
			return true;
		}
		return false;

	}

	public boolean isSessionValid(HttpSession session) {
		HttpSession sessionNew = null;
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (userBean == null) {

			return false;
		}
		if (currentUserSessionMap != null && userBean != null) {
			sessionNew = (HttpSession) currentUserSessionMap.get(userBean
					.getUserName());
		}
		// logger.debug("In Else If New is --"+sessionNew+" Session Current
		// --"+session);
		// logger.debug("The User in Map are"+currentUserSessionMap );
		if (sessionNew != null) {
			if (!sessionNew.equals(session)) {
				session.removeAttribute("USER_INFO");
				session.invalidate();
				session = null;
				return false;
			}
		}
		return true;

	}

	public void setAllowedRoles(String roles) {
		this.allowedRoles = stringToList(roles);
		// logger.debug("I am in Allowed roles111111111");
	}

	public void setDisallowedRoles(String roles) {
		this.disallowedRoles = stringToList(roles);
		// logger.debug("I am in disAllowed roles");
	}

	/**
	 * Splits a string into a List
	 */
	protected List<String> stringToList(String val) {
		if (val != null) {
			String[] list = val.split("[ ]*,[ ]*");
			// logger.debug("I am in Allowed roles"+val);
			return Arrays.asList(list);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

}