package com.skilrock.lms.keba.loginMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */

public class AuthenticationAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public AuthenticationAction() {
		super(AuthenticationAction.class);
	}

	private String userName;
	private String password;
	private String macId;
	private JSONObject responseObject = new JSONObject();

	@SuppressWarnings("unchecked")
	public void authentication() throws Exception {
		logger.debug("In Authentication Action of Keba.");
		ServletContext sc = ServletActionContext.getServletContext();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			JSONObject loginRequestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));

			userName = loginRequestData.getString("userName").trim();
			password = loginRequestData.getString("password").trim();
			macId = loginRequestData.getString("macId").trim();

			HttpSession session = request.getSession();
			logger.info("Session "+session+" with ID "+session.getId());

			if (!CommonFunctionsHelper.isSessionValid(session)) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Time Out. Login Again.");
				return;
			}

			if (LMSFilterDispatcher.stopLogInUsers) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Login Block By Admin.");
				return;
			}

			session.setMaxInactiveInterval(3600);
			session.setAttribute("ROOT_PATH", sc.getRealPath("/").toString());
			session.setAttribute("date_format",(String) sc.getAttribute("date_format"));
			LoginBean loginBean = new LoginBean();

			UserAuthenticationHelper loginAuth = new UserAuthenticationHelper();
			loginBean = loginAuth.loginAuthentication(userName, password, "WEB", (String) ServletActionContext.getServletContext().getAttribute("LOGIN_ATTEMPTS"),null,false);
			String returntype = loginBean.getStatus();
			if (returntype.equals("LOGIN_LIMIT_REACHED")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Sorry, Login Attempts Limit Reached.");
				return;
			}
			if (returntype.equals("ERROR_TIME_LIMIT")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Login Not Allowed.");
				return;
			}
			if (returntype.equals("success") || returntype.equals("FirstTime")) {
		        UserInfoBean userInfo = loginBean.getUserInfo();
				HashMap actionServiceMap = loginBean.getActionServiceMap();
				ArrayList<String> userActionList = loginBean.getUserActionList();

				if ("YES".equalsIgnoreCase((String) sc.getAttribute("LOGIN_BINDING_PC"))
						&& "RETAILER".equalsIgnoreCase(userInfo.getUserType())
						&& !loginAuth.isValidMacId(userInfo.getUserId(), macId)) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Sorry, Invalid POS ID.");
					return;
				}

				loggedInUser(userName, session);
				if ("Yes".equalsIgnoreCase((String) ServletActionContext.getServletContext().getAttribute("RET_OFFLINE"))) {
					if (DrawGameOfflineHelper.checkOfflineUser(userInfo.getUserId())) {
						responseObject.put("isSuccess", false);
						responseObject.put("errorMsg", "User Status is Set to OFFLINE.");
						return;
					}
				}
				session.setAttribute("USER_INFO", userInfo);
				session.setAttribute("PRIV_MAP", actionServiceMap);
				session.setAttribute("ACTION_LIST", userActionList);
				if (actionServiceMap.size() == 0) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "You Are Not Authorized to Access FFT.");
					return;
				}
				if ("RETAILER".equalsIgnoreCase(userInfo.getUserType())) {
					String rgResult = ResponsibleGaming.chkRGCriteOnLogIn(userInfo);
					if (!"SUCCESS".equalsIgnoreCase(rgResult)) {
						responseObject.put("isSuccess", false);
						responseObject.put("errorMsg", rgResult);
						return;
					}
				}

				if (returntype.equals("FirstTime")) {
					//	SUCCESS First Time
					session.setAttribute("FIRST", true);
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "SuccessFirstTime");
					return;
				}

				String isMailAlert = LMSFilterDispatcher.loginMailAlert;
				if (((UserInfoBean) session.getAttribute("USER_INFO")).getUserType().equalsIgnoreCase("BO") && isMailAlert.equalsIgnoreCase("YES")) {
					UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
					String msgFor = "Login into LMS by "
							+ UserAuthenticationHelper.fetchUserFirstLastName(userBean.getUserId());
					String loginTime = Util.getCurrentTimeString();
					String local = request.getLocalAddr() + "/" + request.getLocalName();
					String remote = request.getRemoteAddr() + " OrgName " + userBean.getOrgName();
					String userName = userBean.getUserName();
					String emailMsgTxt = "<html><table><tr><td>" + msgFor
							+ "</td><tr><td>UserName: </td><td>" + userName
							+ "</td><tr><td>LoginTime: </td><td>" + loginTime
							+ "</td></tr><td>Local: </td><td>" + local
							+ "</td></tr><td>Remote:</td><td>" + remote
							+ "</td></tr></table></html>";
					MailSend mailSend = new MailSend(UserAuthenticationHelper.fetchOrgMasterUserEmail(userBean.getUserOrgId()),emailMsgTxt);
					mailSend.setDaemon(true);
					mailSend.start();
				}
				//	SUCCESS
				responseObject.put("isSuccess", true);
				responseObject.put("msg", "Login Successully");
				responseObject.put("balance", userInfo.getAvailableCreditLimit()-userInfo.getClaimableBal());

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

				responseObject.put("currentDate", dateFormat.format(Calendar.getInstance().getTime()));
				responseObject.put("currentTime", timeFormat.format(Calendar.getInstance().getTime()));
				responseObject.put("agentName", userInfo.getParentOrgName());

				return;
			} else if (returntype.equals("BALANCE_NOT_POSITIVE")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Your Balance is Negative.Please Contact BO.");
				return;
			} else if (returntype.equals("ERROR")
					|| returntype.equals("USER_NAME_NOT_MATCH")
					|| returntype.equals("PASS_NOT_MATCH")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Please Enter Correct Username and Password.");
				return;
			} else if (returntype.equals("ERRORINACTIVE")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Your Status Has Been Set to Inactive or Terminate. Please Contact Back Office Immediately.");
				return;
			} else if (returntype.equals("ALREADY_LOGGED_IN")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "User Already Logged In.");
				return;
			} else if (returntype.equals("TIER_INACTIVE")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Tier Status is Set to Inactive.");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "IOException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "Exception Occured.");
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorMsg", "Compile Time Error.");
				responseObject.put("isSuccess", false);
			}
			logger.info("ZimLottoBonus Sale Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
		return;
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
	public void loggedInUser(String user, HttpSession session) {
		String oldUser = null;
		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				oldUser = createCookie(user);
				currentUserSessionMap = new HashMap();
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
						currentUserSessionMap.remove(user);
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
}