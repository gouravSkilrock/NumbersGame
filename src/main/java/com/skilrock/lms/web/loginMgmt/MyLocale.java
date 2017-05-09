package com.skilrock.lms.web.loginMgmt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class MyLocale extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String execute() { 
		createCookie();
		return SUCCESS;
	}
	
	public void createCookie() {
		boolean found = false;
		String requestedLocale = request.getParameter("request_locale");
		Cookie userLocale = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie element : cookies) {
			userLocale = element;
			if (userLocale.getName().equals("LMSLocale") && userLocale.getValue().equals(requestedLocale)) {
				found = true;
				break;
			}

		}
		if (!found) {
			userLocale = new Cookie("LMSLocale", requestedLocale);
			userLocale.setMaxAge(24 * 60 * 60);
			userLocale.setPath("/");
			response.addCookie(userLocale);
		} else {
			userLocale.setMaxAge(24 * 60 * 60);
			userLocale.setPath("/");
			response.addCookie(userLocale);
		}

	}
}