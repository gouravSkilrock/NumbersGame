/*
 * $Id: PrivsInterceptor.java,v 1.3 2016/10/31 09:47:00 neeraj Exp $
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
package com.skilrock.lms.embedded.roleMgmt.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.lms.android.drawGames.common.AndroidErrors;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class PrivsInterceptor extends AbstractInterceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(PrivsInterceptor.class);
	private String actionName;
	private List<String> disallowedRoles = new ArrayList<String>();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String saleStatus = null;

	private String interfaceType;

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

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

	public String getActionName() {
		return actionName;
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
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
		return null;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String result = null;
		ServletContext sc = ServletActionContext.getServletContext();
		String userName = request.getParameter("userName");

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return result;
		}

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);

		if (!isAllowed(session, invocation.getAction())) {
			if (saleStatus != null) {
				String message = null;
				if("ANDROID".equals(interfaceType)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("responseCode", AndroidErrors.SALE_TIME_EXPIRED_ERROR_CODE);
					jsonObject.put("responseMsg", AndroidErrors.SALE_TIME_EXPIRED_ERROR_MESSAGE);
					message = jsonObject.toString();
				} else
					message = "ErrorMsg:Sale Time Expired|";

				response.getOutputStream().write(message.getBytes());
			} else {
				result = handleRejection(invocation, response);
			}
		} else {
			result = invocation.invoke();
		}

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
	 * @throws SQLException
	 */
	protected boolean isAllowed(HttpSession session, Object action)
			throws SQLException {

		//logger.info("i am inside priv interceptor");
		setActionName(ActionContext.getContext().getName());
		//logger.info("allowed privs are " + getActionName());

		boolean result = false;
		/*ArrayList<String> userActionList = new ArrayList<String>();
		userActionList = (ArrayList<String>) session
				.getAttribute("ACTION_LIST");*/
		//logger.info("ACTION LIST " + userActionList);
		
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ServletContext sc = ServletActionContext.getServletContext();
		if (userBean.getUserType().equals("RETAILER")
				&& !isSaleDuration(userBean)) {
			List<String> disableTabs;
			disableTabs = Arrays.asList(((String) sc
					.getAttribute("RET_SALE_BOUND")).split(","));
			for (int i = 0; i < disableTabs.size(); i++) {
				if (getActionName().contains(disableTabs.get(i))) {
					saleStatus = "SALE_STOP";
					return false;
				}
			}
		}
		result = true;
		return result;
	}

	public boolean isSaleDuration(UserInfoBean userInfoBean) {
		Date d = new Date();
		ServletContext sc = ServletActionContext.getServletContext();
		if (d.toString().split(" ")[3].compareTo((String) sc
				.getAttribute("SALE_START_TIME")) >= 0
				&& d.toString().split(" ")[3].compareTo((String) sc
						.getAttribute("SALE_END_TIME")) <= 0) {
			return true;
		}
		return false;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setAllowedPrivs(String privs) {
	}

	public void setDisallowedRoles(String roles) {
		this.disallowedRoles = stringToList(roles);
	}

	/**
	 * Splits a string into a List
	 */
	protected List<String> stringToList(String val) {
		if (val != null) {
			String[] list = val.split("[ ]*,[ ]*");
			return Arrays.asList(list);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

}