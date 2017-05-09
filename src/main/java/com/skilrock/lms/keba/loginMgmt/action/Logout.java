package com.skilrock.lms.keba.loginMgmt.action;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class Logout extends BaseAction {
	private static final long serialVersionUID = 1L;

	public Logout() {
		super(Logout.class);
	}

	@SuppressWarnings("unchecked")
	public void logOut() {

		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		HttpSession session = null;
		PrintWriter out = null;
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
			logger.info("Logout Request Data - "+requestData);
			String userName = requestData.getString("userName");
			long lastTktNo = Long.parseLong(requestData.getString("lastTktNo").trim());

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				responseObject.put("errorMsg", "Time Out. Login Again.");
				responseObject.put("isSuccess", false);
			}
			session = getSession(userName);

			currentUserSessionMap.remove(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				responseObject.put("errorMsg", "Time Out. Login Again.");
				responseObject.put("isSuccess", false);
			}

			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			int userId = userBean.getUserId();
			if (userBean != null) {
				loggedOutUser(userBean.getUserName());

				String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
				int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
				long lastPrintedTicket=0;
				int gameId = 0;
				if(lastTktNo !=0){
					lastPrintedTicket = lastTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(lastTktNo).length());
					gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTktNo)));
				}

				String actionName=ActionContext.getContext().getName();
				DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
				drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
			}
			session.removeAttribute("USER_INFO");
			session.removeAttribute("ACTION_LIST");
			session.removeAttribute("PRIV_MAP");
			session.invalidate();
			session = null;
			UserAuthenticationHelper.updateLoginStatus(userId, "LOGOUT");
			System.out.println("Log out Successfully and Session is " + session);
			if (DrawGameOfflineHelper.checkOfflineUser(userId)) {
				if (DrawGameOfflineHelper.updateLogoutSuccess(userName)) {
					responseObject.put("msg", "Logout Successfully");
					responseObject.put("isSuccess", true);
				} else {
					responseObject.put("errorMsg", "Time Out. Login Again.");
					responseObject.put("isSuccess", false);
				}
				return;
			} else {
				responseObject.put("msg", "Logout Successfully");
				responseObject.put("isSuccess", true);
			}
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
			logger.info("Logout Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
	}		

	@SuppressWarnings("unchecked")
	private void loggedOutUser(String user) {
		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			currentUserSessionMap.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
			System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		} else {
			ServletContext sc = ServletActionContext.getServletContext();
			List<String> currentUserList = (List<String>) sc.getAttribute("LOGGED_IN_USERS");
			currentUserList.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserList);
			System.out.println(" LOGGED_IN_USERS maps is " + currentUserList);
		}
	}
}