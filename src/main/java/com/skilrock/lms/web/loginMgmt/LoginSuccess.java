package com.skilrock.lms.web.loginMgmt;

import java.util.Locale;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.rest.common.TransactionManager;

public class LoginSuccess extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String loginSuccess() {
		String selectedLocale = CommonMethods.getSelectedLocale();
		if(selectedLocale != null && selectedLocale.length() > 0){
			Locale locale = new Locale(selectedLocale);  
			Map<String, Object> session = ActionContext.getContext().getSession();
			session.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, locale); 
		}
		TransactionManager.setResponseData("true");
		return "success";
	}
}
