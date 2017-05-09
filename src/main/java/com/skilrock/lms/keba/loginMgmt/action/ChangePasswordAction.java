package com.skilrock.lms.keba.loginMgmt.action;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.ChangePasswordHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

public class ChangePasswordAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public ChangePasswordAction() {
		super(ChangePasswordAction.class);
	}

	@SuppressWarnings("unchecked")
	public void changePassword() {
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		HttpSession session = null;
		PrintWriter out = null;
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
			logger.info("Change Password Request Data - "+requestData);
			String userName = requestData.getString("userName");
			String oldPassword = requestData.getString("oldPassword");
			String newPassword = requestData.getString("newPassword");
			String verifyNewPassword = requestData.getString("verifyNewPassword");
			long lastTktNo = Long.parseLong(requestData.getString("lastTktNo").trim());

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				responseObject.put("errorMsg", "Time Out. Login Again.");
				responseObject.put("isSuccess", false);
				return;
			}
			session = getSession(userName);

			currentUserSessionMap.remove(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				responseObject.put("errorMsg", "Time Out. Login Again.");
				responseObject.put("isSuccess", false);
				return;
			}

			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			String isDraw = (String) sc.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				responseObject.put("errorMsg", "Draw Game Not Available.");
				responseObject.put("isSuccess", false);
				return;
			}

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays = Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			long lastPrintedTicket = 0;
			int gameId = 0;
			if(lastTktNo != 0) {
				lastPrintedTicket = lastTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(lastTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTktNo)));
			}

			String actionName = ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			String ticketNo = String.valueOf(lastPrintedTicket);
			if(ticketNo.length() > 5) {
				ticketNo = ticketNo.substring(0, 5);
				if(userBean.getUserId() == Integer.parseInt(ticketNo))
					drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
			}

			String first = (String) session.getAttribute("FIRST");

			String returntype = "";

			ChangePasswordHelper changepass = new ChangePasswordHelper();
			if (userName != null && oldPassword != null && newPassword != null && verifyNewPassword != null) {
				returntype = changepass.changePassword(userName, oldPassword, newPassword, verifyNewPassword, false);
			}

			if (returntype.equals("ERROR")) {
				responseObject.put("errorMsg", "Error in Retype Password.");
				responseObject.put("isSuccess", false);
				return;
			} else if (returntype.equals("INPUT")) {
				responseObject.put("errorMsg", "Password Used Previously.");
				responseObject.put("isSuccess", false);
				return;
			} else if (returntype.equals("SUCCESS")) {
				if ("true".equals(first)) {
					session.setAttribute("FIRST", false);
					responseObject.put("msg", "Password Changed Successfully.");
					responseObject.put("isSuccess", true);
					return;
				} else {
					responseObject.put("msg", "Password Changed Successfully.");
					responseObject.put("isSuccess", true);
					return;
				}
			} else if (returntype.equals("wrongpass")) {
				responseObject.put("errorMsg", "Incorrect Password.");
				responseObject.put("isSuccess", false);
				return;
			} else {
				responseObject.put("errorMsg", "Incorrect Password.");
				responseObject.put("isSuccess", false);
				return;
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
}