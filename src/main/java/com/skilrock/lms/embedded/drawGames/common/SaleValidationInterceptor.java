package com.skilrock.lms.embedded.drawGames.common;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.rest.common.TransactionManager;

public class SaleValidationInterceptor extends AbstractInterceptor {
	static Log logger = LogFactory.getLog(SaleValidationInterceptor.class);
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
				ServletContext sc = ServletActionContext.getServletContext();
				Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
				HttpSession session = (HttpSession) currentUserSessionMap.get(request.getParameter("userName"));
				UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
				if("INACTIVE".equals(userBean.getOrgStatus()) || "INACTIVE".equals(userBean.getParentOrgStatus())) {
					response.getOutputStream().write(("ErrorMsg:"+EmbeddedErrors.SALE_NOT_ALLOWED_ERROR).getBytes());
					return null;
				}
			invocation.invoke();
		} catch (Exception e) {
			TransactionManager.endTransaction();
			throw e;
		}
		return null;
	}
}