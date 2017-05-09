package com.skilrock.lms.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.admin.common.AdminAuthHelper;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;

public class AdminAuthAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private String data;
	private String password = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String newPassword = null;
	private String verifynewPassword = null;
	private String username = null;

	@SuppressWarnings("unchecked")
	public String authentication() {
		System.out.println("---------Admin Login--------");
		LoginBean loginBean = null;
		getHttpsSession();
		String uname = getUsername().toLowerCase();
		String password = getPassword();
		HttpSession session = null;
		session = getRequest().getSession();
		session.setMaxInactiveInterval(3600);
		session.setAttribute("ROOT_PATH", ServletActionContext
				.getServletContext().getRealPath("/").toString());
		session.setAttribute("date_format", ServletActionContext
				.getServletContext().getAttribute("date_format"));
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());

		AdminAuthHelper loginAuth = new AdminAuthHelper();
		loginBean = loginAuth.loginAuthentication(uname, password, "WEB");
		String returntype = loginBean.getStatus();
		if (returntype.equals("success") || returntype.equals("FirstTime")) {
			loggedInUser(uname, session);
			UserInfoBean userInfo = loginBean.getUserInfo();
			HashMap<String, List<PriviledgeBean>> actionServiceMap = loginBean
					.getActionServiceMap();
			ArrayList<String> userActionList = loginBean.getUserActionList();
			session.setAttribute("USER_INFO", userInfo);
			session.setAttribute("PRIV_MAP", actionServiceMap);
			session.setAttribute("ACTION_LIST", userActionList);

			if (actionServiceMap.size() == 0) {
				errorMap(session.getId(),
						"You are not Authorized to access WEB");
				return ERROR;
			}

			if (returntype.equals("FirstTime")) {
				session.setAttribute("FIRST", true);
				return "SuccessFirstTime";
			}
			return SUCCESS;
		} else if (returntype.equals("USER_NAME_NOT_MATCH")
				|| returntype.equals("PASS_NOT_MATCH")) {
			errorMap(session.getId(),
					"Please Enter Correct User Name and Password !!");
			return ERROR;
		} else if (returntype.equals("ERROR")) {
			errorMap(session.getId(), "Contact To Skilrock!!");
			return ERROR;
		} else if (returntype.equals("ERRORINACTIVE")) {
			errorMap(session.getId(),
					"Your Status is INACTIVE!! Please Contact to Skilrock");
			return ERROR;
		}
		addActionError("Enter Correct User Name and password");
		errorMap(session.getId(), "Enter Correct User Name and password");
		return ERROR;
	}

	public String saveChangePassword() throws Exception {
		System.out.println("inside change pass action");
		HttpSession session = request.getSession();
		UserInfoBean bean = (UserInfoBean) session.getAttribute("USER_INFO");
		String returntype = "";
		if (bean.getUserName().equalsIgnoreCase("ADMIN")) {
					if (!newPassword.equals(verifynewPassword)) {
			System.out.println("**Both Password not Matched**");
			addActionError("Please Enter Correct Passwords !!");
			return "wrongpass";
		}

		if (bean == null) {
			addActionError("You have to login to change your password ");
			return "NOTLOGIN";
		}

		String uname = bean.getUserName();
		System.out.println(session.getAttribute("FIRST") + "****************");
		boolean first = false;
		if (session.getAttribute("FIRST") != null) {
			first = (Boolean) session.getAttribute("FIRST");
		}

		AdminAuthHelper changepass = new AdminAuthHelper();
		if (uname != null && password != null && newPassword != null
				&& verifynewPassword != null) {
			if (changepass.verifyPasswordChars(newPassword)) {
				returntype = changepass.changePassword(uname, password,newPassword, verifynewPassword);
			} else {
				returntype = "PASSWORD INAPPROPRIATE";
			}
		}

		System.out.println("****" + returntype);
		if (returntype.equals("ERROR")) {
			addActionError("New Password is not Verified !!");
			return ERROR;
		} else if (returntype.equals("INPUT")) {
			addActionError("You Have Used This Password Recently");
			return INPUT;
		} else if (returntype.equals("SUCCESS")) {
			if (first) {
				session.setAttribute("FIRST", false);
				return "UserFirstSuccess";
			}
			return "UserSuccess";
		} else if (returntype.equals("wrongpass")
				|| returntype.equals("INCORRECT")) {
			addActionError("Enter Correct Old Password !!");
			return "wrongpass";
		} else if (returntype.equals("PASSWORD INAPPROPRIATE")) {
			addActionError("Password inappropriate: Password Should Contain AtLeast one digit, one lower and one uppercase alphabet!!");
		}
		}else{
			addActionError("You have to login from admin ");			
		}
		// addActionError("Enter Correct Old Password");
		return ERROR;
	}

	public void fetchNamenLimit() throws IOException {
		PrintWriter out = getResponse().getWriter();
		StringBuffer userDetails = new StringBuffer("");
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		userDetails.append(uib.getUserName());
		userDetails.append(":");
		userDetails.append("N");
		userDetails.append(":");
		userDetails.append("BO");
		userDetails.append("cacheBreakPoint" + fetchCachedPages());
		userDetails.append("cacheBreakPoint" + createMenu());
		out.print(userDetails);
	}

	@SuppressWarnings("unchecked")
	public StringBuffer fetchCachedPages() {
		ServletContext sc = ServletActionContext.getServletContext();
		StringBuffer cachedDetails = new StringBuffer("");
		cachedDetails.append((HashMap) sc.getAttribute("CACHED_FILES_MAP"));
		return cachedDetails;
	}

	@SuppressWarnings("unchecked")
	public StringBuffer createMenu() {
		StringBuffer menu = new StringBuffer("");
		PriviledgeBean privBean = null;
		LinkedHashMap<String, ArrayList<PriviledgeBean>> actionServiceMap = (LinkedHashMap<String, ArrayList<PriviledgeBean>>) request
				.getSession().getAttribute("PRIV_MAP");
		Iterator itrMap = actionServiceMap.entrySet().iterator();
		while (itrMap.hasNext()) {
			Map.Entry<String, ArrayList<PriviledgeBean>> pairs = (Map.Entry<String, ArrayList<PriviledgeBean>>) itrMap
					.next();
			List<PriviledgeBean> privList = (List<PriviledgeBean>) pairs
					.getValue();
			menu.append("TABS-" + pairs.getKey());
			for (int i = 0; i < privList.size(); i++) {
				privBean = (PriviledgeBean) privList.get(i);
				menu.append("RT-" + privBean.getRelatedTo());
				menu.append(";" + privBean.getPrivTitle());
				menu.append(";" + privBean.getActionMapping());
			}
		}
		return menu;
	}

	@SuppressWarnings("unchecked")
	public void getHttpsSession() {
		ServletContext sc = ServletActionContext.getServletContext();
		Map<String, HttpSession> httpsSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("HTTPS_SESSION_MAP");
		HttpSession httpsSession = (HttpSession) httpsSessionMap.get(data);
		setUsername(((String) httpsSession.getAttribute("HTTPS_USERNAME"))
				.toLowerCase());
		setPassword((String) httpsSession.getAttribute("HTTPS_PASSWORD"));
		httpsSessionMap.remove(data);
		httpsSession.removeAttribute("HTTPS_USERNAME");
		httpsSession.removeAttribute("HTTPS_PASSWORD");
		httpsSession.invalidate();
		sc.setAttribute("HTTPS_SESSION_MAP", httpsSessionMap);

	}

	@SuppressWarnings("unchecked")
	public void loggedInUser(String user, HttpSession session) {
		String oldUser = null;
		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();

			Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				oldUser = createCookie(user);
				currentUserSessionMap = new HashMap<String, HttpSession>();
				if (oldUser.equals(user)) {
					currentUserSessionMap.put(user, session);
				} else {
					currentUserSessionMap.put(user, session);
					currentUserSessionMap.put(oldUser, session);
				}
				sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
			} else {
				if (currentUserSessionMap.containsKey(user)) {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.put(user, session);
					} else {
						currentUserSessionMap.put(user, session);
						currentUserSessionMap.put(oldUser, session);
					}

				} else {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.put(user, session);
					} else {
						currentUserSessionMap.put(user, session);
						currentUserSessionMap.put(oldUser, session);
					}
				}
			}

		}

	}

	public String createCookie(String user) {
		boolean found = false;
		Cookie userSessionId = null;
		String oldUser = null;
		if (request.getCookies() != null) {
			Cookie[] cookies = request.getCookies();
			for (Cookie element : cookies) {
				userSessionId = element;
				if (userSessionId.getName().equals("LMSCookie")) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			userSessionId = new Cookie("LMSCookie", user);
			oldUser = user;
			userSessionId.setMaxAge(24 * 60 * 60);
			userSessionId.setPath("/");
			response.addCookie(userSessionId);
		} else {
			oldUser = userSessionId.getValue();
			userSessionId.setMaxAge(24 * 60 * 60);
			userSessionId.setValue(user);
			userSessionId.setPath("/");
			response.addCookie(userSessionId);
		}
		return oldUser;

	}

	@SuppressWarnings("unchecked")
	public void errorMap(String sessionId, String error) {
		ServletContext sc = ServletActionContext.getServletContext();
		Map<String, String> errorSessionMap = (Map<String, String>) sc
				.getAttribute("ERROR_SESSION_MAP");
		if (errorSessionMap == null) {
			errorSessionMap = new HashMap<String, String>();
		}
		errorSessionMap.put(sessionId, error);
		sc.setAttribute("ERROR_SESSION_MAP", errorSessionMap);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getVerifynewPassword() {
		return verifynewPassword;
	}

	public void setVerifynewPassword(String verifynewPassword) {
		this.verifynewPassword = verifynewPassword;
	}

}
