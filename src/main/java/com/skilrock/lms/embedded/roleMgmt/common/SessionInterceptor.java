package com.skilrock.lms.embedded.roleMgmt.common;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class SessionInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(SessionInterceptor.class);

	@Override
	public void init() {
		
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

		//logger.info(" user name is " + userName);

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return result;
		}
		
		UserInfoBean userBean = (UserInfoBean)session.getAttribute("USER_INFO");
		if(!userBean.getLoginChannel().equalsIgnoreCase("TERMINAL")){
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
						.getBytes());
			return result;
		}

		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			if (!isSessionValid(session)) {
				response.getOutputStream().write("ErrorMsg:Already Logged In|".getBytes());
				return result;
			}
		}

		return invocation.invoke();
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
		//logger.debug("In Else If New is --" + sessionNew + " Session Current --" + session);
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

	@Override
	public void destroy() {
		
	}
}