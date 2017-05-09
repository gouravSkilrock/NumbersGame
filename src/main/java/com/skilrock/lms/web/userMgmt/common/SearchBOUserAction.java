package com.skilrock.lms.web.userMgmt.common;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.AutoGenerate;
import com.skilrock.lms.coreEngine.userMgmt.common.SearchBOUserHelper;

public class SearchBOUserAction extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String department;
	private String emailId;
	private String end = null;
	private String firstName;
	private String lastName;
	private String phoneNbr;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String comment;

	private int roleId;
	private Map roleMap = new TreeMap();
	int start = 0;
	private String status;
	private String type;

	private int user_id;
	UserDetailsBean userDetailsBean;

	private String userName;

	public String dispSearchBoUser() throws LMSException {
		SearchBOUserHelper searchHelper = new SearchBOUserHelper();
		roleMap = searchHelper.dispSearchBoUser();
		return SUCCESS;
	}

	public String editBOUserDetails() throws LMSException {
		HttpSession session = getRequest().getSession();
		int doneByUserId = 0;
		String requestIp = null;

		if (status == null) {
			status = "INACTIVE";
		}
		if ("BLOCK".equalsIgnoreCase(status) || "TERMINATE".equalsIgnoreCase(status)) {
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserSessionMap.remove(userName);
		}

		doneByUserId = ((UserInfoBean)session.getAttribute("USER_INFO")).getUserId();
		requestIp = request.getRemoteAddr();
		SearchBOUserHelper searchHelper = new SearchBOUserHelper();
		searchHelper
				.editBOUserDetails(user_id, emailId.trim(), phoneNbr.trim(), status, type, doneByUserId, comment, requestIp);
		System.out.println("--------fn:" + firstName);
		System.out.println("--------ln:" + lastName);
		session.setAttribute("BO_USER_NAME", firstName.toUpperCase() + " "
				+ lastName.toUpperCase());
		return SUCCESS;
	}

	public String getDepartment() {
		return department;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getEnd() {
		return end;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNbr() {
		return phoneNbr;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getRoleId() {
		return roleId;
	}

	public Map getRoleMap() {
		return roleMap;
	}

	public int getStart() {
		return start;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public int getUser_id() {
		return user_id;
	}

	public UserDetailsBean getUserDetailsBean() {
		return userDetailsBean;
	}

	public String getUserName() {
		return userName;
	}

	public void resetPassBO() throws Exception {

		PrintWriter out = getResponse().getWriter();
		String autoPass = AutoGenerate.autoPassword();
		SearchBOUserHelper resetPass = new SearchBOUserHelper();
		resetPass.resetPassBO(user_id, autoPass, emailId, userName, firstName,
				lastName);

		response.setContentType("text/html");
		out.print(true);

	}
	
	public String resetLoginAttemptsBOUser() throws Exception {
		SearchBOUserHelper helper = new SearchBOUserHelper();
		helper.resetLoginAttemptsForBOUser(user_id);
		return SUCCESS;
	}

	/**
	 * This method is used for pagination of user search Results .
	 * 
	 * @return SUCCESS
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		HttpSession session = getRequest().getSession();
		List<UserDetailsBean> ajaxList = (List) session
				.getAttribute("USER_LIST1");
		List<UserDetailsBean> ajaxSearchList = new ArrayList();
		System.out.println("end " + ajaxList);
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// System.out.println("end "+end);
			startValue = (Integer) session.getAttribute("startValueUserSearch");
			if (end.equals("first")) {
				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 10;

				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				System.out.println("i m in Previous");
				startValue = startValue - 10;
				if (startValue < 10) {
					startValue = 0;
				}

				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				System.out.println("i m in Next");
				startValue = startValue + 10;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 10;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 10;

			}
			if (startValue == endValue) {
				startValue = endValue - 10;
			}
			// System.out.println("End value"+endValue);
			// System.out.println("Start Value"+startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("USER_LIST", ajaxSearchList);
			session.setAttribute("startValueUserSearch", startValue);
		}

		System.out
				.println("============================================= before return");
		return SUCCESS;
	}

	public String searchBousers() throws LMSException {

		HttpSession session = getRequest().getSession();
		session.setAttribute("USER_LIST1", null);
		session.setAttribute("USER_LIST", null);
		SearchBOUserHelper searchHelper = new SearchBOUserHelper();
		List<UserDetailsBean> userList = searchHelper.searchBousers(userName,
				roleId, type, status);
		if (userList.size() > 0) {
			session.setAttribute("USER_LIST1", userList);
			session.setAttribute("USER_LIST", userList);
			session.setAttribute("startValueUserSearch", new Integer(0));
			searchAjax();
		}
		
		return SUCCESS;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setRoleMap(Map roleMap) {
		this.roleMap = roleMap;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setUserDetailsBean(UserDetailsBean userDetailsBean) {
		this.userDetailsBean = userDetailsBean;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String showBOUserDetails() throws LMSException {
		SearchBOUserHelper searchHelper = new SearchBOUserHelper();
		userDetailsBean = searchHelper.showBOUserDetails(user_id, type);
		return SUCCESS;
	}

	public String verifyEmail() throws Exception {
		PrintWriter out = getResponse().getWriter();

		System.out.println("email id is " + emailId);

		// verify email Address from mail report table whose refrence user id is
		// null
		SearchBOUserHelper searchHelper = new SearchBOUserHelper();
		boolean mailVerified = searchHelper.verifyEmail(emailId, user_id);

		response.setContentType("text/html");
		out.print(mailVerified);
		return null;
	}

}