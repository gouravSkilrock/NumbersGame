package com.skilrock.lms.web.loginMgmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.controller.userMgmtController.UserMgmtController;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class AuthenticationAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	static Log logger = LogFactory.getLog(AuthenticationAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private String varRolePage;
	private String data;
	private String password = null;
	private HttpServletRequest request;
	private String wrapperLogout = null ;
	private HttpServletResponse response;

	private String username = null;
	private String wrapperURL = null ;
	private String macId;
	private String browser = null;
	private long LSTktNo;

	/*
	 * private static ResourceBundle rb1, rb2, rb3; private static Locale
	 * locale1, locale2, locale3;
	 * 
	 * static { locale1 = Locale.ENGLISH; rb1 =
	 * ResourceBundle.getBundle("config/Bundle", locale1);
	 * 
	 * locale2 = new Locale("ru", "RU"); rb2 =
	 * ResourceBundle.getBundle("config/Bundle", locale2);
	 * 
	 * locale3 = Locale.FRENCH; rb3 = ResourceBundle.getBundle("config/Bundle",
	 * locale3); }
	 */

	public String authentication() throws Exception {
		LoginBean loginBean = null;
		int sessionTimeOut = 3600;
		logger.debug("in authentication action1111111");
		if(!"YES".equalsIgnoreCase(wrapperLogout))
			getHttpsSession();
		
		String uname = getUsername().toLowerCase(); // stores the username
		// entered by the
		// user.
		String password = getPassword(); // stores the password entered by
		// the user.
		String detectedBrowser = getBrowser();
		HttpSession session = null;
		session = getRequest().getSession();
		if("RETAILER".equalsIgnoreCase(Util.getUserTypeFromUsername(uname)))
			sessionTimeOut = 28800;
		session.setMaxInactiveInterval(sessionTimeOut); // added by yogesh
		session.setAttribute("WRAPPER_LOGOUT", wrapperURL);
		
		ServletContext sc = ServletActionContext.getServletContext();
		sc.setAttribute("WRAPPER_LOGOUT", wrapperURL);
		
		String agentLoginAllowed = (String) sc.getAttribute("AGENT_LOGIN_ALLOWED");
		
		if(wrapperLogout == null // null means that call is not from wrapper.
				&& "NO".equalsIgnoreCase(agentLoginAllowed) // Property tells whether agent login is allowed or not. 
					&& "AGENT".equalsIgnoreCase(Util.getUserTypeFromUsername(uname))){ // to check that user is of AGENT type or not.
			errorMap(session.getId(), getText("msg.login.error"));
			return ERROR ;
		}

		session.setAttribute("ROOT_PATH", ServletActionContext
				.getServletContext().getRealPath("/").toString());
		session.setAttribute("date_format", ServletActionContext
				.getServletContext().getAttribute("date_format"));

		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());

		if (LMSFilterDispatcher.stopLogInUsers) {
			errorMap(session.getId(), getText("msg.login.block"));
			if(wrapperURL != null)
				wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.block"));
			return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
		}
		UserAuthenticationHelper loginAuth = new UserAuthenticationHelper();
		loginBean = loginAuth.loginAuthentication(uname, password, "WEB",
				(String) ServletActionContext.getServletContext().getAttribute(
						"LOGIN_ATTEMPTS"),session.getId(),true);
		String returntype = loginBean.getStatus();

		logger.debug("The user login is " + returntype);

		if (returntype.equals("LOGIN_LIMIT_REACHED")) {
			errorMap(session.getId(), getText("msg.login.limit"));
			if(wrapperURL != null)
				wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.limit"));
			return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
		}
		if (returntype.equals("ERROR_TIME_LIMIT")) {
			errorMap(session.getId(), getText("msg.login.allowed"));
			if(wrapperURL != null)
				wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.allowed"));
			return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
		}
		if (returntype.equals("success") || returntype.equals("FirstTime")) {
			UserInfoBean userInfo = loginBean.getUserInfo();
			HashMap actionServiceMap = loginBean.getActionServiceMap();
			ArrayList<String> userActionList = loginBean.getUserActionList();
			logger.debug(userInfo.getUserType());

			if (userInfo.getUserType().equalsIgnoreCase("Retailer")) {
				if (!("SAFARIBET".equalsIgnoreCase((String) sc.getAttribute("COUNTRY_DEPLOYED")) || "ZIMBABWE".equalsIgnoreCase((String) sc.getAttribute("COUNTRY_DEPLOYED")) || "SIKKIM".equalsIgnoreCase((String) sc.getAttribute("COUNTRY_DEPLOYED"))  || "INDIA".equalsIgnoreCase((String) sc.getAttribute("COUNTRY_DEPLOYED"))) && !LMSFilterDispatcher.isDGActiveAtRetWeb) {		
					errorMap(session.getId(),"Retailer Login Not Allowed Through WEB");		
					return ERROR;		
				}
				String authBrowser = (String) sc
						.getAttribute("ACTIVE_BROWSER_FOR_RETAILER");
				System.out.println("Retailer Login Through" + detectedBrowser
						+ " browser.Allowed Browser for User is : "
						+ authBrowser);
				String authBrowsersArr[] = authBrowser.split(",");
				boolean chkBrowser = false;
				if (authBrowsersArr.length >= 1) {
					for (int i = 0; i < authBrowsersArr.length; i++) {
						if (detectedBrowser.equalsIgnoreCase(authBrowsersArr[i]
								.trim())||detectedBrowser.equalsIgnoreCase("ALL")) {
							chkBrowser = true;
						}
					}
				} else {
					System.out
							.println("No active browser is defined in property master table.");
					chkBrowser = true;
				}

				if (!chkBrowser) {
					errorMap(session.getId(),
							getText("msg.login.invalidBrowser1")
									+ authBrowser.toUpperCase()
									+ getText("msg.login.invalidBrowser2"));
					if(wrapperURL != null)
						wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.invalidBrowser1")+ authBrowser.toUpperCase()+ getText("msg.login.invalidBrowser2"));
					return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
				} else {
					long lastPrintedTicket = 0;
					int lstGameId = 0;
					int autoCancelHoldDays = Integer.parseInt((String) sc
							.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
					String refMerchantId = (String) sc
							.getAttribute("REF_MERCHANT_ID");
					String actionName = ActionContext.getContext().getName();
					DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
					// helper.checkLastPrintedTicketStatusAndUpdate(userInfo,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
					try {
						LSTktNo = CookieMgmtForTicketNumber
								.getTicketNumberFromCookie(request, userInfo
										.getUserName());
						if (LSTktNo != 0) {
							lastPrintedTicket = LSTktNo
									/ Util.getDivValueForLastSoldTkt(String
											.valueOf(LSTktNo).length());
							lstGameId = Util.getGameIdFromGameNumber(Util
									.getGamenoFromTktnumber(String
											.valueOf(LSTktNo)));
						}
						helper.insertEntryIntoPrintedTktTableForWeb(lstGameId,
								userInfo.getUserOrgId(), lastPrintedTicket,
								"WEB", Util.getCurrentTimeStamp(), actionName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}

				}
			}

			System.out.println("---PC-Binding starts---");
			System.out.println("LOGIN_BINDING_PC"
					+ (String) sc.getAttribute("LOGIN_BINDING_PC"));
			System.out.println("user type:" + userInfo.getUserType());

			if ("YES".equalsIgnoreCase((String) sc
					.getAttribute("LOGIN_BINDING_PC"))
					&& "RETAILER".equalsIgnoreCase(userInfo.getUserType())
					&& !loginAuth
							.isValidMacId(userInfo.getUserId(), getMacId())) {
				System.out.println("---PC-Binding finishes---");
				errorMap(session.getId(), getText("msg.login.invalidPos"));
				if(wrapperURL != null)
					wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.invalidPos"));
				return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
			}
			System.out.println("---PC-Binding ends---");
			loggedInUser(uname, session);

			if ("Yes".equalsIgnoreCase((String) ServletActionContext
					.getServletContext().getAttribute("RET_OFFLINE"))) {
				if (DrawGameOfflineHelper
						.checkOfflineUser(userInfo.getUserId())) {
					logger.debug("USER status is set to OFFLINE");
					errorMap(session.getId(), getText("msg.login.userOffline"));
					if(wrapperURL != null)
						wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.userOffline"));
					return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";

				}
			}
			session.setAttribute("USER_INFO", userInfo);
			session.setAttribute("PRIV_MAP", actionServiceMap);
			session.setAttribute("ACTION_LIST", userActionList);

			logger.debug("******actionServiceMap" + actionServiceMap);
			// check priv on web
			if (actionServiceMap.size() == 0) {
				errorMap(session.getId(), getText("msg.login.unauthWeb"));
				if(wrapperURL != null)
					wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.unauthWeb"));
				return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
			}
			// check responsible gaming limits
			if ("RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
				logger.debug("********check RG on LogIn");

				String rgResult = ResponsibleGaming.chkRGCriteOnLogIn(userInfo);
				logger.debug("*****RG RESULT*******\n" + rgResult);
				if (!"SUCCESS".equalsIgnoreCase(rgResult)) {
					errorMap(session.getId(), rgResult);
					if(wrapperURL != null)
						wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+rgResult);
					return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
				}

				try {
					long ticketNumber = CookieMgmtForTicketNumber
							.getTicketNumberFromCookie(request, userInfo
									.getUserName());
					CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(
							request, response, userInfo.getUserName(), String
									.valueOf(ticketNumber));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(ServicesBean.isSLE()) {
				if(ServicesBean.isSLE()) {
					UserDataBean dataBean = UserMgmtController.getInstance().getUserInfo(uname.trim());
//					UserDataBean dataBean = new UserDataBean();
//					dataBean.setSessionId(session.getId());
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.NOTIFY_ON_LOGIN, dataBean);
					notifySLE.start();
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
			if ("RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
				return "successRet";
			}else{
				return "success";	
			}
			
		} else if (returntype.equals("BALANCE_NOT_POSITIVE")) {
			errorMap(session.getId(), getText("msg.login.balance"));
			if(wrapperURL != null)
				wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.balance"));
			return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
		} else if (returntype.equals("ERROR")
				|| returntype.equals("USER_NAME_NOT_MATCH")
				|| returntype.equals("PASS_NOT_MATCH")) {
			// addActionError("Please Enter Correct Login Name and Password
			// !!");

			// Locale locale1 = Locale.ENGLISH;
			// Locale locale2 = new Locale("ru", "RU");
			// Locale locale3 = Locale.FRENCH;
			//
			// ResourceBundle rb1 = ResourceBundle.getBundle("config/Bundle",
			// locale1);
			// ResourceBundle rb2 = ResourceBundle.getBundle("config/Bundle",
			// locale2);
			// ResourceBundle rb3 = ResourceBundle.getBundle("config/Bundle",
			// locale3);

			// errorMap(session.getId(), rb1.getString("msg.login.error")
			// + "<br/>" + rb2.getString("msg.login.error") + "<br/>"
			// + rb3.getString("msg.login.error"));
			errorMap(session.getId(), getText("msg.login.error"));

			// errorMap(
			// session.getId(),
			// "Please Enter Correct Login Name and Password!!<br/>Пожалуйста, введите правильный Имя и пароль!!");
			// errorMap(session.getId(),
			// "Please Enter Correct Login Name and Password!!<br/>请输入正确的登录名和密码!!");

			logger.debug("inside error block");
			if(wrapperURL != null){
				wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.error"));
			}
			return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
		} else if (returntype.equals("ERRORINACTIVE")) {
			// addActionError("Your status has been set to Inactive or Terminate
			// Please contact Back Office immediately");
			errorMap(session.getId(), getText("msg.login.inactive"));
			if(wrapperURL != null)
				wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.inactive"));
			return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";
		} else if (returntype.equals("ALREADY_LOGGED_IN")) {
			return "ALREADY_LOGGED_IN";
		} else if (returntype.equals("TIER_INACTIVE")) {
			logger.debug("TIER Status is set to INACTIVE");
			return "TIER_INACTIVE";
		}

		addActionError(getText("msg.login.credential"));
		errorMap(session.getId(), getText("msg.login.credential"));
		wrapperURL = wrapperURL.concat("&errorMsgFromLMS="+getText("msg.login.credential"));
		return wrapperURL == null ? ERROR : "WRAPPER_REDIRECT";

	}

	public String createCookie(String user) {
		boolean found = false;
		Cookie userSessionId = null;
		String oldUser = null;
		if (request.getCookies() != null) {
			Cookie[] cookies = request.getCookies();
			for (Cookie element : cookies) {
				userSessionId = element;
				logger.debug("In create Cookies and Cookies Found are"
						+ userSessionId);
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
			logger
					.debug("In create Cookies IF and Cookies Not Found Created New Cookie"
							+ userSessionId);
		} else {
			oldUser = userSessionId.getValue();
			userSessionId.setMaxAge(24 * 60 * 60);
			userSessionId.setValue(user);
			userSessionId.setPath("/");
			response.addCookie(userSessionId);
			logger
					.debug("In create Cookies Else and Cookies Found and oldUser is "
							+ oldUser + "--New User is " + user);
		}
		return oldUser;

	}

	public void errorMap(String sessionId, String error) {
		ServletContext sc = ServletActionContext.getServletContext();
		Map errorSessionMap = (Map) sc.getAttribute("ERROR_SESSION_MAP");
		if (errorSessionMap == null) {
			errorSessionMap = new HashMap();
		}
		errorSessionMap.put(sessionId, error);
		sc.setAttribute("ERROR_SESSION_MAP", errorSessionMap);
	}

	public String getData() {
		return data;
	}

	/**
	 * This method is used for user authentication like correct Login Name and
	 * Password
	 * 
	 * @return String
	 * @throws Exception
	 * @author Skilrock Technologies
	 */

	public void getHttpsSession() {
		ServletContext sc = ServletActionContext.getServletContext();
		Map httpsSessionMap = (Map) sc.getAttribute("HTTPS_SESSION_MAP");
		HttpSession httpsSession = (HttpSession) httpsSessionMap.get(data);
		setUsername(((String) httpsSession.getAttribute("HTTPS_USERNAME"))
				.toLowerCase());
		setPassword((String) httpsSession.getAttribute("HTTPS_PASSWORD"));
		setMacId((String) httpsSession.getAttribute("HTTPS_MACHINE"));
		setBrowser((String) httpsSession.getAttribute("BROWSER"));
		httpsSessionMap.remove(data);
		httpsSession.removeAttribute("HTTPS_USERNAME");
		httpsSession.removeAttribute("HTTPS_PASSWORD");
		httpsSession.removeAttribute("HTTPS_MACHINE");
		httpsSession.removeAttribute("BROWSER");
		httpsSession.invalidate();
		sc.setAttribute("HTTPS_SESSION_MAP", httpsSessionMap);

	}

	public String getPassword() {
		return password;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * This method is for tracking the Number of Current users Logged In and
	 * maintaining session using Map and allowing the User to LogIN at other
	 * place simultaneously and timing out the Session at previous place user
	 * has logged in.
	 * 
	 * @param user
	 *            stand's for Login Name
	 * @return void
	 */
	public void loggedInUser(String user, HttpSession session) {
		String oldUser = null;
		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();

			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				oldUser = createCookie(user);
				logger.debug("I am in if");
				currentUserSessionMap = new HashMap();
				if (oldUser.equals(user)) {
					currentUserSessionMap.put(user, session);
				} else {
					currentUserSessionMap.put(user, session);
					currentUserSessionMap.put(oldUser, session);
				}
				sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
				System.out.println(" LOGGED_IN_USERS maps is "
						+ currentUserSessionMap);
				logger.debug("In If User is --" + user + " Session Id --"
						+ session.getId());
			} else {
				if (currentUserSessionMap.containsKey(user)) {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.remove(user);
						currentUserSessionMap.put(user, session);
					} else {
						currentUserSessionMap.put(user, session);
						currentUserSessionMap.put(oldUser, session);
					}
					logger.debug("In Else If User is --" + user
							+ " Session Id --" + session.getId());
				} else {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.put(user, session);
					} else {
						currentUserSessionMap.put(user, session);
						currentUserSessionMap.put(oldUser, session);
					}
					logger.debug("In Else else User is --" + user
							+ " Session Id --" + session.getId());

				}
			}

		}

	}

	public void setData(String data) {
		this.data = data;
	}

	public void setPassword(String value) {
		password = value;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setUsername(String value) {
		username = value;
	}

	public String getMacId() {
		return macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}
	
	public String getWrapperLogout() {
		return wrapperLogout;
	}

	public void setWrapperLogout(String wrapperLogout) {
		this.wrapperLogout = wrapperLogout;
	}

	public String getWrapperURL() {
		return wrapperURL;
	}

	public void setWrapperURL(String wrapperURL) {
		this.wrapperURL = wrapperURL;
	}
	
	

}