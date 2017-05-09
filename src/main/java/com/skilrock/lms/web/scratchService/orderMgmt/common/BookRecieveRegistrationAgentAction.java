package com.skilrock.lms.web.scratchService.orderMgmt.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BookRecieveRegistrationAgentHelper;

public class BookRecieveRegistrationAgentAction extends ActionSupport implements ServletRequestAware,
	ServletResponseAware  {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(BookRecieveRegistrationAgentAction.class);

	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;
	protected HttpServletResponse response;
	private HttpSession session;
	private String challanId;
	private String[] bookNumber;


	public String[] getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(String[] bookNumber) {
		this.bookNumber = bookNumber;
	}

	public String getChallanId() {
		return challanId;
	}

	public void setChallanId(String challanId) {
		this.challanId = challanId;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		 	this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	
	public String getBooks(){
		logger.info("--inside activateBooks--");
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		BookRecieveRegistrationAgentHelper helper = new BookRecieveRegistrationAgentHelper();
		Map<String, List<String>> gameBookMap = helper.getBooks(userInfoBean.getUserOrgId(),challanId);
		session.setAttribute("GAME_BOOK_MAP", gameBookMap);
		return SUCCESS;
		
	}
	public String submitBooks() throws SQLException{
		logger.info("--inside submitBooks--");
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		BookRecieveRegistrationAgentHelper helper = new BookRecieveRegistrationAgentHelper();
		List<String> bookNumberList = new ArrayList<String>();
		for (String str : getBookNumber()) {
			bookNumberList.add(str);
		}
		if (helper.updateBooks(userInfoBean.getUserOrgId(),userInfoBean.getUserId(), bookNumberList)) {
			return SUCCESS;
		}

		return ERROR;
		
	}
	
	}
