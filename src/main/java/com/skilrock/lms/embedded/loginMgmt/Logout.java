package com.skilrock.lms.embedded.loginMgmt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.controller.userMgmtController.UserMgmtController;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
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
public class Logout extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		char a[] = { '1', '2', '3' };
		int dataLength = 0;
		int length = a.length;
		int firstData = 0;
		double secondData = 0;
		for (int i = 0; i < length; i++) {
			firstData = a[length - i - 1] - 48;
			secondData = Math.pow(10, i);
			System.out.println(" is i " + i);
			System.out.println(" first " + firstData);
			System.out.println(" secondData " + secondData);
			dataLength = (int) (dataLength + firstData * secondData);

		}
		System.out.println(" data length is " + dataLength);

	}

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String userName;
	private String varRolePage;
	private long LSTktNo;

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getUserName() {
		return userName;
	}

	public String getVarRolePage() {
		return varRolePage;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	private void loggedOutUser(String user) {

		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserSessionMap.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
		} else {
			ServletContext sc = ServletActionContext.getServletContext();
			List<String> currentUserList = (List<String>) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserList.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserList);
		}
	}

	public void logOut() throws Exception {
		System.out.println("I am in Logout");

		HttpSession session = null;
		ServletContext sc = ServletActionContext.getServletContext();
		// sc.removeAttribute(userName);
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		/*
		 * if (isDraw.equalsIgnoreCase("NO")) {
		 * response.getOutputStream().write( ("ErrorMsg:" +
		 * EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE) .getBytes()); return; }
		 */
		System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);

		System.out.println(" user name is " + userName);
		if (currentUserSessionMap == null) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		session = (HttpSession) currentUserSessionMap.get(userName);
		currentUserSessionMap.remove(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userId = userBean.getUserId();
		if (userBean != null) {
			loggedOutUser(userBean.getUserName());

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			long lastPrintedTicket=0;
			int gameId = 0;
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}

			String actionName=ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		}
		UserMgmtController.getInstance().updateUserLogout(session.getId());
		session.removeAttribute("USER_INFO");
		session.removeAttribute("ACTION_LIST");
		session.removeAttribute("PRIV_MAP");
		session.invalidate();
		session = null;
		UserAuthenticationHelper.updateLoginStatus(userId, "LOGOUT");
		System.out.println("Log out Successfully and Session is " + session);
		
		if(userBean != null) {
			if(ServicesBean.isSLE()) {
				if(ServicesBean.isSLE()) {
					UserDataBean dataBean = UserMgmtController.getInstance().getUserInfo(userBean.getUserName().trim());
					//UserDataBean dataBean = new UserDataBean();
					dataBean.setSessionId(null);	
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.NOTIFY_ON_LOGOUT, dataBean);
					notifySLE.start();
				}
			}
		}
		if (DrawGameOfflineHelper.checkOfflineUser(userId)) {
			if (DrawGameOfflineHelper.updateLogoutSuccess(userName)) {
				response.getOutputStream().write(
						"Logout Successfully".getBytes());
			} else {
				// response.getOutputStream().write("Can not logout please try
				// again".getBytes());
				response.getOutputStream().write(
						("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED+ "ErrorCode:01|")
								.getBytes());
			}
			return;
		} else {
			response.getOutputStream().write("Logout Successfully".getBytes());
		}
		TransactionManager.setResponseData("true");
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setVarRolePage(String varRolePage) {
		this.varRolePage = varRolePage;
	}

	public void test() throws IOException {
		response.getOutputStream().write("".getBytes());
	}

}