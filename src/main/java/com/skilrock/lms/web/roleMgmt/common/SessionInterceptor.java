package com.skilrock.lms.web.roleMgmt.common;

import java.util.Locale;
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
import com.opensymphony.xwork2.interceptor.I18nInterceptor;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.utility.CommonMethods;

public class SessionInterceptor extends BaseAction implements Interceptor {

	public SessionInterceptor() {
		super(SessionInterceptor.class.getName());
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(SessionInterceptor.class);
	private String wrapperURL ;

	@Override
	public void init() {
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ServletContext sc = ServletActionContext.getServletContext();
		String json = request.getParameter("json");
		UserInfoBean userBean = null;
		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			String userName = null;
			if(json != null){
				JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
				userName = (String) requestData.get("userName");
			}
			if(userName == null){
				userBean = (UserInfoBean) request.getSession(false).getAttribute("USER_INFO");
			} else{
				userBean = getUserBean(userName);
			}
			
			if (!isSessionValid(userBean)) {
				wrapperURL = (String) sc.getAttribute("WRAPPER_LOGOUT") ;
				if(!(wrapperURL == null))
					response.sendRedirect(request.getContextPath()+"/testRedirect.jsp");
				return "ALREADY_LOGGED_IN";
			}
		}

		return invocation.invoke();
	}

	@SuppressWarnings("unchecked")
	private boolean isSessionValid(UserInfoBean userBean) {
		HttpSession sessionNew = null;
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (userBean == null) {
			return false;
		}
		HttpSession session = (HttpSession) currentUserSessionMap.get(userBean.getUserName());
		if (currentUserSessionMap != null && userBean != null) {
			sessionNew = (HttpSession) currentUserSessionMap.get(userBean
					.getUserName().toLowerCase());
		}
		logger.debug("In Else If New is --" + sessionNew
				+ " Session Current --" + session);

		if (sessionNew != null) {
			if (!sessionNew.equals(session)) {
				session.removeAttribute("USER_INFO");
				session.invalidate();
				session = null;
				return false;
			} else {
				String selectedLocale = CommonMethods.getSelectedLocale();
                if(selectedLocale != null && selectedLocale.length() > 0){
                    Locale locale = new Locale(selectedLocale); 
                    session.setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, locale);
                }
			}
		} else {
			return false;
		}

		return true;
	}
	@Override
	public void destroy() {
		
	}

	public String getWrapperURL() {
		return wrapperURL;
	}

	public void setWrapperURL(String wrapperURL) {
		this.wrapperURL = wrapperURL;
	}
	
	
}