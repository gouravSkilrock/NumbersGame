/*
 * $Id: PrivsInterceptor.java,v 1.5 2016/10/31 09:46:07 neeraj Exp $
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
package com.skilrock.lms.web.roleMgmt.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.LMSUtility;

public class PrivsInterceptor extends AbstractInterceptor {
	static Log logger = LogFactory.getLog(PrivsInterceptor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> disallowedRoles = new ArrayList<String>();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String saleStatus = null;

	private String actionName;
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
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

		String result = null;
		if (!isAllowed(request, invocation.getAction())) {
			if (saleStatus != null) {
				return saleStatus;
			} else {
				result = handleRejection(invocation, response);
			}
		} else {
			result = invocation.invoke();

		}
		// logger.debug("I am in disAllowed roles"+result);
		// logger.debug("I am in disAllowed roles"+request);

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
	protected boolean isAllowed(HttpServletRequest request, Object action)
			throws SQLException {
		String json = request.getParameter("json");
		HttpSession session = null;
		String userName = null;
		if(json != null){
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			userName = (String) requestData.get("userName");
		}
		
		if(userName == null){
			session = request.getSession();
		} else{
			Map currentUserSessionMap = (Map) LMSUtility.sc.getAttribute("LOGGED_IN_USERS");
			session = (HttpSession) currentUserSessionMap.get(userName);
		}
		
		// logger.debug("allowedRoles::" + allowedRoles);

		// logger.debug("i am inside priv interceptor");
		String actionName = ActionContext.getContext().getName();
		logger.debug("Action Name - " + actionName);

		boolean result = false;
		ArrayList<String> userActionList = new ArrayList<String>();
		userActionList = (ArrayList<String>) session
				.getAttribute("ACTION_LIST");
		logger.debug("userActionList: " + userActionList);
		List<String> disableTabs;
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ServletContext sc = ServletActionContext.getServletContext();
		if (userBean.getUserType().equals("RETAILER")
				&& !isSaleDuration(userBean)) {
			disableTabs = Arrays.asList(((String) sc
					.getAttribute("RET_SALE_BOUND")).split(","));
			for (int i = 0; i < disableTabs.size(); i++) {
				if (actionName.contains(disableTabs.get(i))) {
					saleStatus = "SALE_STOP";
					return false;
				}
			}
		}
		if(!userBean.getLoginChannel().equalsIgnoreCase("WEB")){
			return false;
		}
		if (userActionList.contains(actionName)) {
			HashMap actionServiceMap = (HashMap) session
					.getAttribute("PRIV_MAP");
			Iterator itrMap = actionServiceMap.entrySet().iterator();
			while (itrMap.hasNext()) {
				Map.Entry pairs = (Map.Entry) itrMap.next();
				List privList = (List) pairs.getValue();
				for (int i = 0; i < privList.size(); i++) {

					if (((PriviledgeBean) privList.get(i)).getActionMapping()
							.equals(actionName)) {
						refreshSession(session);
					}
				}
			}
			result = true;
		}

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

	public void refreshSession(HttpSession session) {
		ServletContext sc = ServletActionContext.getServletContext();
		String sesVariables = (String) sc.getAttribute("SESSION_VARIABLES");
		List sesVar = Arrays.asList(sesVariables.split(","));

		Enumeration sesEnum = session.getAttributeNames();
		while (sesEnum.hasMoreElements()) {
			Object variable = sesEnum.nextElement();
			if (!sesVar.contains(variable.toString())) {
				session.removeAttribute(variable.toString());
			}
		}
	}

	public void setAllowedPrivs(String privs) {
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