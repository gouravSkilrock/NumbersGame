package com.skilrock.lms.keba.common;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;

public class SessionInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(SessionInterceptor.class);

	@Override
	public void init() {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		JSONObject responseObject = new JSONObject();
		response.setContentType("application/json");
		String result = null;

		JSONObject requestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
		String userName = requestData.getString("userName").trim();

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			responseObject.put("isSuccess", false);
			responseObject.put("errorCode", LMSErrors.SESSION_EXPIRED_ERROR_CODE);
			responseObject.put("errorMsg", LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
			response.getOutputStream().write(responseObject.toString().getBytes());
			return result;
		}

		logger.info(" user name is " + userName);

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			responseObject.put("isSuccess", false);
			responseObject.put("errorCode", LMSErrors.SESSION_EXPIRED_ERROR_CODE);
			responseObject.put("errorMsg", LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
			response.getOutputStream().write(responseObject.toString().getBytes());
			return result;
		}

		/*
		UserInfoBean userBean = (UserInfoBean)session.getAttribute("USER_INFO");
		if(!userBean.getLoginChannel().equalsIgnoreCase("TERMINAL")){
			responseObject.put("isSuccess", false);
			responseObject.put("errorCode", LMSErrors.SESSION_EXPIRED_ERROR_CODE);
			responseObject.put("errorMsg", LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
			response.getOutputStream().write(responseObject.toString().getBytes());
			return result;
		}

		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			if (!isSessionValid(session)) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorCode", LMSErrors.SESSION_EXPIRED_ERROR_CODE);
				responseObject.put("errorMsg", LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
				response.getOutputStream().write(responseObject.toString().getBytes());
				return result;
			}
		}
		*/

		return invocation.invoke();
	}

	@SuppressWarnings("unchecked")
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
		logger.debug("In Else If New is --" + sessionNew + " Session Current --" + session);
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

	@SuppressWarnings("unchecked")
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