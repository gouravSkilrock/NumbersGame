package com.skilrock.lms.embedded.loginMgmt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.controller.userMgmtController.UserMgmtController;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.UserManagementHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;
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

	private String data;
	private String deviceType;
	private String password = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String terminalId;
	private int userId;
	private String username = null;
	private double version;
	private String profile;
	private String simType;
	private double CAPP;
	private double CDLL;
	private double CCONF;
	private long LSTktNo;
	private int lastMsgId;
	private String imsi;
	private int CID;
	private int LAC;
	
	public void authentication() throws Exception {
		logger.info("--------In  Authentication Action for EMBEDDED--------");
		
	try{
		ServletContext sc = ServletActionContext.getServletContext();

		/*	System.out.println(" request OBJECT---" + request);
		System.out.println(" request session OBJECT---" + request.getSession());
		System.out.println(" request session id ---"
				+ request.getSession().getId());*/
		HttpSession session = request.getSession();
		logger.info("Session"+session+"With Id"+session.getId());
		//String fileSize = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED+"ErrorCode:"+EmbeddedErrors.SESSION_EXPIRED_ERROR_CODE+"|")
									.getBytes());
			return;
		}
		if (LMSFilterDispatcher.stopLogInUsers) {
			response.getOutputStream().write(
					("ErrorMsg: Log In Block By Admin"+"|ErrorCode:"+EmbeddedErrors.LOGIN_BLOCK_ERROR_CODE+"|").getBytes());
			return;
		}
		String uname = getUsername(); // lowercase userName 
		String password = getPassword();
		//String dateFormat = ((String) sc.getAttribute("date_format")).toLowerCase();
		session.setMaxInactiveInterval(3600);
		session.setAttribute("ROOT_PATH", sc.getRealPath("/").toString());
		session.setAttribute("date_format",(String) sc.getAttribute("date_format"));
		String loginAttempts = (String)sc.getAttribute("LOGIN_ATTEMPTS");
		LoginBean loginBean = new LoginBean();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		Map frzTimeMap =null;
		Map<Integer, Map<Integer, String>> drawIdTableMap=null;
		Map<Integer, List<List>> drawTimeMap=null;
		
		TransactionManager.setResponseData("true");
	
		if(ServicesBean.isDG()){
			frzTimeMap= (Map) sc.getAttribute("FREEZE_TIME_MAP_NEW");
			drawIdTableMap = (Map<Integer, Map<Integer, String>>) Util.drawIdTableMap;  //  (TreeMap<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
			drawTimeMap =	(Map<Integer, List<List>>) sc.getAttribute("GAME_DATA");
		}
		StringBuilder finalData = new StringBuilder("");
		AuthenticationHelper helper = new AuthenticationHelper();
		loginBean =helper.authentication(imsi,simType,terminalId,deviceType,profile,version,simType,CAPP,CCONF,CDLL,uname,password,loginAttempts,refMerchantId,LSTktNo
										,ActionContext.getContext().getName(),frzTimeMap,drawIdTableMap,drawTimeMap, lastMsgId, CID, LAC, finalData , session.getId());
		int errorCode =loginBean.getErrorCode();
		if(errorCode==100){
			response.getOutputStream().write(finalData.toString().getBytes());
			loginBean.getUserInfo().setTerminalBuildVersion(version);
			session.setAttribute("FIRST", "false");
			session.setAttribute("USER_INFO", loginBean.getUserInfo());
			session.setAttribute("PRIV_MAP", loginBean.getActionServiceMap());
			session.setAttribute("ACTION_LIST", loginBean.getUserActionList());
			loggedInUser(uname, session);

			if(ServicesBean.isSLE()) {
				if(ServicesBean.isSLE()) {
					UserDataBean dataBean = UserMgmtController.getInstance().getUserInfo(uname.trim());
//					UserDataBean dataBean = new UserDataBean();
//					dataBean.setSessionId(session.getId());

					NotifySLE notifySLE = new NotifySLE(SLE.Activity.NOTIFY_ON_LOGIN, dataBean);
					notifySLE.start();
				}
			}

			return;
		}else if(errorCode==0){
			
			String returntype =loginBean.getStatus();
			if (returntype.equals("BALANCE_NOT_POSITIVE")) {
				logger.info("Error in balance");
					response.getOutputStream().write(
							("ErrorMsg:Your Balance is Negative.Please Contact PA/BO|"+"ErrorCode:"+EmbeddedErrors.NEGATIVE_BALANCE_ERROR_CODE+"|")
									.getBytes());
					return;
				} else if (returntype.equals("ERROR")
						|| returntype.equals("USER_NAME_NOT_MATCH")
						|| returntype.equals("PASS_NOT_MATCH")) {
					// addActionError("Please Enter Correct Login Name and Password
					// !!");
					// errorMap(session.getId(),"Please Enter Correct Login Name and
					// Password !!");
					logger.info("inside error block");
					// System.out.println("ErrorMsg:" +
					// EmbeddedErrors.LOGIN_INVALID_USERNAME_PASSWORD + "|");
					response
							.getOutputStream()
							.write(
									("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_USERNAME_PASSWORD+"ErrorCode:"+EmbeddedErrors.LOGIN_INVALID_USERNAME_PASSWORD_ERROR_CODE+"|")
											.getBytes());
					// getRequest().setAttribute("ErrorMsg", "ErrorMsg:Invalid UserName
					// or Password|");
					return;

				} else if (returntype.equals("ERRORINACTIVE")) {
					// addActionError("Your status has been set to Inactive or Terminate
					// Please contact Back Office immediately");
					// errorMap(session.getId(),"Your status has been set to Inactive or
					// Terminate Please contact Back Office immediately");
					logger.info("inside error block");
					logger.info("ErrorMsg:"
							+ EmbeddedErrors.LOGIN_ERROR_INACTIVE);
					response.getOutputStream().write(
							("ErrorMsg:" + EmbeddedErrors.LOGIN_ERROR_INACTIVE+"ErrorCode:"+EmbeddedErrors.LOGIN_ERROR_INACTIVE_ERROR_CODE+"|")
									.getBytes());
					return;

				} else if (returntype.equals("ALREADY_LOGGED_IN")) {
					// response.getOutputStream().write(("ALREADY_LOGGED_IN").getBytes());
					logger.info("ALREADY_LOGGED_IN");
					response.getOutputStream().write(
							("ErrorMsg:" + EmbeddedErrors.LOGIN_ALREADY_LOGGED_IN+"ErrorCode:"+EmbeddedErrors.LOGIN_ALREADY_LOGGED_IN_ERROR_CODE+"|")
									.getBytes());
					return;
				} else if (returntype.equals("TIER_INACTIVE")) {
					// logger.debug("TIER Status is set to INACTIVE");
					// return "TIER_INACTIVE";
					logger.info("TIER_INACTIVE");
					response.getOutputStream().write(
							("ErrorMsg:" + EmbeddedErrors.LOGIN_TIER_INACTIVE+"ErrorCode:"+EmbeddedErrors.LOGIN_TIER_INACTIVE_ERROR_CODE+"|")
									.getBytes());
					return;
				} else if (returntype.equals("LOGIN_LIMIT_REACHED")) {
					logger.info("LOGIN_LIMIT_REACHED");
					response.getOutputStream().write(
							("ErrorMsg:" + EmbeddedErrors.LOGIN_LIMIT_REACHED+"ErrorCode:"+EmbeddedErrors.LOGIN_LIMIT_REACHED_ERROR_CODE+"|")
									.getBytes());
					return;
				} else if (returntype.equals("ERROR_TIME_LIMIT")) {
					logger.info("ERROR_TIME_LIMIT");
					response.getOutputStream().write(
							("ErrorMsg:" + EmbeddedErrors.ERROR_TIME_LIMIT+"ErrorCode:"+EmbeddedErrors.ERROR_TIME_LIMIT_ERROR_CODE+"|")
									.getBytes());
					return;
				} 
			
			
		}else if(errorCode==200){
			 if(simType==null || simType==""){
					simType="MTN";
				}
			 logger.info("Invalid Terminal");
				response
				.getOutputStream()
				.write(
						("ErrorMsg:" + getCorrectIP((String) sc
								.getAttribute("WRAPPER_IP"),simType)+"|" + "ErrorCode:04|")
								.getBytes());
				return;
		 }else if(errorCode==201){
			 logger.info("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_TERMINAL_ID);
			  response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_TERMINAL_ID+"ErrorCode:"+EmbeddedErrors.LOGIN_INVALID_TERMINAL_ID_ERROR_CODE+"|").getBytes());
			 return ;
		 }else if(errorCode==202){
			 logger.info("Errormsg:" + EmbeddedErrors.LOGIN_ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY);
			 response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.LOGIN_ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY+"ErrorCode:"+EmbeddedErrors.LOGIN_ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY_ERROR_CODE+"|").getBytes());
			 return;
		 }else if(errorCode==203){
			 logger.info("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_TERMINAL_ID);
			 response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_TERMINAL_ID+"ErrorCode:"+EmbeddedErrors.LOGIN_INVALID_TERMINAL_ID_ERROR_CODE+"|").getBytes());
			 return;
		 }else if(errorCode==204){
			 logger.info("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_SIM);
			 response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_SIM+"ErrorCode:"+EmbeddedErrors.LOGIN_INVALID_SIM_ERROR_CODE+"|").getBytes());
			 return;
		 }else{
			 logger.info("ErrorMsg:" + EmbeddedErrors.LOGIN_ERROR);
			 response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.LOGIN_ERROR+"ErrorCode:"+EmbeddedErrors.LOGIN_ERROR_ERROR_CODE+"|").getBytes());
			 return;
		}
		
				
	}catch(Exception e){
		logger.error("Exception in Action", e);
		//e.printStackTrace();
	}
		
	response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.ERROR_MSG+"ErrorCode:"+EmbeddedErrors.ERROR_MSG_ERROR_CODE+"|").getBytes());
	return;
		
	}

	
	public String getCorrectIP(String wrapperIp, String simType){
		String correctIP="";
		
		String [] wrapperIps=wrapperIp.split("&");
		for(int i=0;i<wrapperIps.length;i++){
			if(wrapperIps[i].split("-")[0].equalsIgnoreCase(simType)){
				correctIP=wrapperIps[i].split("-")[1];
				break;
			}
		}
		return correctIP;
	}
	
	public String createCookie(String user) {
		boolean found = false;
		Cookie userSessionId = null;
		String oldUser = null;
		if (request.getCookies() != null) {
			Cookie[] cookies = request.getCookies();
			for (Cookie element : cookies) {
				userSessionId = element;
				System.out.println("In create Cookies and Cookies Found are"
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
			System.out
					.println("In create Cookies IF and Cookies Not Found Created New Cookie"
							+ userSessionId);
		} else {
			oldUser = userSessionId.getValue();
			userSessionId.setMaxAge(24 * 60 * 60);
			userSessionId.setValue(user);
			userSessionId.setPath("/");
			response.addCookie(userSessionId);
			System.out
					.println("In create Cookies Else and Cookies Found and oldUser is "
							+ oldUser + "--New User is " + user);
		}
		return oldUser;

	}

	public void engineerLogin() throws Exception {
		UserAuthenticationHelper loginAuth = new UserAuthenticationHelper();
		// boolean flag = loginAuth.validateUser(username, password);
		boolean flag = false;
		if (username.equalsIgnoreCase("skilrock")
				&& password.equals("64646789")) {
			flag = true;
		}
		if (flag) {
			response.getOutputStream().write("Success".getBytes());
		} else {
			response.getOutputStream().write("Failed".getBytes());
		}
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

	public void fetchRetailerList() throws Exception {
		String retIdAndNameList = new UserManagementHelper()
				.offlineFileUploadViaApplet(userId);
		System.out.println("<><><><><><><>" + retIdAndNameList);
		response.getOutputStream().write(retIdAndNameList.getBytes());
	}

	public String getData() {
		return data;
	}

	// ADDED BY UMESH FOR VALIDATING AGENT FOR OFFLINE FILE UPLOAD VIA APPLET.

	public String getDeviceType() {
		return deviceType;
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

	public String getTerminalId() {
		return terminalId;
	}

	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public double getVersion() {
		return version;
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
			if (currentUserSessionMap == null || currentUserSessionMap.size()==0 ) {
				oldUser = createCookie(user);
				System.out.println("I am in if");
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
				System.out.println("In If User is --" + user + " Session Id --"
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
					System.out.println("In Else If User is --" + user
							+ " Session Id --" + session.getId());
				} else {
					oldUser = createCookie(user);
					if (oldUser.equals(user)) {
						currentUserSessionMap.put(user, session);
					} else {
						currentUserSessionMap.put(user, session);
						currentUserSessionMap.put(oldUser, session);
					}
					System.out.println("In Else else User is --" + user
							+ " Session Id --" + session.getId());

				}
			}

		}

	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUsername(String value) {
		username = value;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public void validateUser() {
		String message = null;
		try {
			if (new UserAuthenticationHelper().validateUser(username, password)) {
				message = "true";
			} else {
				message = "false";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (message == null) {
					response.getOutputStream().write("error".getBytes());
				} else {
					response.getOutputStream().write(message.getBytes());
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public double getCAPP() {
		return CAPP;
	}

	public void setCAPP(double capp) {
		CAPP = capp;
	}

	public double getCDLL() {
		return CDLL;
	}

	public void setCDLL(double cdll) {
		CDLL = cdll;
	}

	public double getCCONF() {
		return CCONF;
	}

	public void setCCONF(double cconf) {
		CCONF = cconf;
	}

	public String getSimType() {
		return simType;
	}

	public void setSimType(String simType) {
		this.simType = simType;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public int getLastMsgId() {
		return lastMsgId;
	}

	public void setLastMsgId(int lastMsgId) {
		this.lastMsgId = lastMsgId;
	}


	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public int getCID() {
		return CID;
	}

	public void setCID(int cID) {
		CID = cID;
	}

	public int getLAC() {
		return LAC;
	}

	public void setLAC(int lAC) {
		LAC = lAC;
	}
}