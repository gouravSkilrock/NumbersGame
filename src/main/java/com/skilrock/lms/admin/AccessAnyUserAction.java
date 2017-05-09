package com.skilrock.lms.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.admin.common.AccessAnyUserHelper;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.AuthenticationHelperforBOMaster;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.rolemgmt.beans.userPrivBean;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;

public class AccessAnyUserAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String userData;
	private String userName = null;
	private Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> userPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>>();
	private int[] mappingId;
	private String[] groupName;
	private int[] privCount;
	@Override
	public String execute() throws Exception {
		AccessAnyUserHelper user = new AccessAnyUserHelper();
		userData = user.getAllLMSUser().toString();
		return SUCCESS;
	}

	public String getUserLogin() throws ServletException, IOException{
		LoginBean loginBean = null;
		String uname = getUserName().toLowerCase(); // stores the username
		
		HttpSession session = null;
		session = getRequest().getSession();
		session.setMaxInactiveInterval(3600); // adde by yogesh
		session.setAttribute("ROOT_PATH", ServletActionContext
				.getServletContext().getRealPath("/").toString());
		session.setAttribute("date_format", ServletActionContext
				.getServletContext().getAttribute("date_format"));

		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());

		/*AccessAnyUserHelper loginAuth = new AccessAnyUserHelper();*/
		AuthenticationHelperforBOMaster loginAuth = new AuthenticationHelperforBOMaster();
		loginBean = loginAuth.loginAuthentication(uname,"WEB",session.getId());
		String returntype = loginBean.getStatus();

		if (returntype.equals("ERROR_TIME_LIMIT")) {
			errorMap(session.getId(), "Login Not Allowed.");
			return ERROR;
		}
		if (returntype.equals("success") || returntype.equals("FirstTime")) {
			loggedInUser(uname, session);
			UserInfoBean userInfo = loginBean.getUserInfo();
			HashMap actionServiceMap = loginBean.getActionServiceMap();
			ArrayList<String> userActionList = loginBean.getUserActionList();

			if ("Yes".equalsIgnoreCase((String) ServletActionContext
					.getServletContext().getAttribute("RET_OFFLINE"))) {
				if (DrawGameOfflineHelper
						.checkOfflineUser(userInfo.getUserId())) {
					errorMap(session.getId(), "USER status is set to OFFLINE");
					return ERROR;

				}
			}

			session.setAttribute("USER_INFO", userInfo);
			session.setAttribute("PRIV_MAP", actionServiceMap);
			session.setAttribute("ACTION_LIST", userActionList);

			// check priv on web
			if (actionServiceMap.size() == 0) {
				errorMap(session.getId(),
						"You are not Authorized to access WEB");
				return ERROR;
			}
			// check responsible gaming limits
			if ("RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
				
				String rgResult = ResponsibleGaming.chkRGCriteOnLogIn(userInfo);
				if (!"SUCCESS".equalsIgnoreCase(rgResult)) {
					errorMap(session.getId(), rgResult);
					return ERROR;
				}
				
				long ticketNumber;
				try {
					ticketNumber = CookieMgmtForTicketNumber.getTicketNumberFromCookie(request,userInfo.getUserName());
					CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, userInfo.getUserName(), String.valueOf(ticketNumber));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}

			if (returntype.equals("FirstTime")) {
				session.setAttribute("FIRST", true);
				return "SuccessFirstTime";
			}
			String isMailAlert = LMSFilterDispatcher.loginMailAlert;
			// String isMailSend = LMSFilterDispatcher.isMailSend;
			if (((UserInfoBean) session.getAttribute("USER_INFO"))
					.getUserType().equalsIgnoreCase("BO")
					&& isMailAlert.equalsIgnoreCase("YES")) {
				UserInfoBean userBean = (UserInfoBean) session
						.getAttribute("USER_INFO");
				String msgFor = "Login into LMS by "
						+ UserAuthenticationHelper
								.fetchUserFirstLastName(userBean.getUserId());
				String loginTime = "" + new Date().toString();
				String local = request.getLocalAddr() + "/"
						+ request.getLocalName();
				String remote = request.getRemoteAddr() + " OrgName "
						+ userBean.getOrgName();
				String userName = userBean.getUserName();
				String emailMsgTxt = "<html><table><tr><td>" + msgFor
						+ "</td><tr><td>UserName: </td><td>" + userName
						+ "</td><tr><td>LoginTime: </td><td>" + loginTime
						+ "</td></tr><td>Local: </td><td>" + local
						+ "</td></tr><td>Remote:</td><td>" + remote
						+ "</td></tr></table></html>";
				MailSend mailSend = new MailSend(UserAuthenticationHelper
						.fetchOrgMasterUserEmail(userBean.getUserOrgId()),
						emailMsgTxt);
				mailSend.setDaemon(true);
				mailSend.start();
			}
			return "success";
		} else if (returntype.equals("BALANCE_NOT_POSITIVE")) {
			errorMap(session.getId(),
					"Your Balance is Negative.Please Contact BO");
			return ERROR;
		} else if (returntype.equals("ERROR")
				|| returntype.equals("USER_NAME_NOT_MATCH")
				|| returntype.equals("PASS_NOT_MATCH")) {
			// addActionError("Please Enter Correct Login Name and Password
			// !!");
			errorMap(session.getId(),
					"Please Enter Correct Login Name and Password !!");
			return ERROR;
		} else if (returntype.equals("ERRORINACTIVE")) {
			// addActionError("Your status has been set to Inactive or Terminate
			// Please contact Back Office immediately");
			errorMap(
					session.getId(),
					"Your status has been set to Inactive or Terminate Please contact  Back Office immediately");
			return ERROR;
		} else if (returntype.equals("ALREADY_LOGGED_IN")) {
			return "ALREADY_LOGGED_IN";
		} else if (returntype.equals("TIER_INACTIVE")) {
			return "TIER_INACTIVE";
		}

		addActionError("Enter Correct name password");
		errorMap(session.getId(), "Enter Correct name password");
		return ERROR;
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
	public void errorMap(String sessionId, String error) {
		ServletContext sc = ServletActionContext.getServletContext();
		Map<String, String> errorSessionMap = (Map<String, String>) sc.getAttribute("ERROR_SESSION_MAP");
		if (errorSessionMap == null) {
			errorSessionMap = new HashMap<String, String>();
		}
		errorSessionMap.put(sessionId, error);
		sc.setAttribute("ERROR_SESSION_MAP", errorSessionMap);
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
	
	public String getUserPriviledges() throws LMSException{
		AccessAnyUserHelper helper = new AccessAnyUserHelper();
		setUserPriviledgeMap(helper.getUserPriviledges(getUserName()));
		return SUCCESS;
	}
	
	public String saveUserPriviledges() throws LMSException {
		HttpSession session = getRequest().getSession();
		AccessAnyUserHelper userHelper = new AccessAnyUserHelper();
		userHelper.saveUserPriv(getUserName(), getGroupName(),
					mappingId, privCount);
		session.setAttribute("USER_NAME", getUserName());
		return SUCCESS;
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

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> getUserPriviledgeMap() {
		return userPriviledgeMap;
	}

	public void setUserPriviledgeMap(
			Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> userPriviledgeMap) {
		this.userPriviledgeMap = userPriviledgeMap;
	}

	public int[] getMappingId() {
		return mappingId;
	}

	public void setMappingId(int[] mappingId) {
		this.mappingId = mappingId;
	}

	public String[] getGroupName() {
		return groupName;
	}

	public void setGroupName(String[] groupName) {
		this.groupName = groupName;
	}

	public int[] getPrivCount() {
		return privCount;
	}

	public void setPrivCount(int[] privCount) {
		this.privCount = privCount;
	}
	
}
