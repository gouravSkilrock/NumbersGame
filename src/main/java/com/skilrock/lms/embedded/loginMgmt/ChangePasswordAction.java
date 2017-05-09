package com.skilrock.lms.embedded.loginMgmt;

import java.io.IOException;
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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.ChangePasswordHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This class provides method for change password
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChangePasswordAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String newPassword = null;
	private String password = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String userName;
	private String verifynewPassword = null;
	private long LSTktNo;

	/**
	 * This method is used to change password
	 * 
	 * @return String
	 * @throws Exception
	 */
	public void authentication() throws Exception {
		System.out.println("inside change pass action");

		if (!newPassword.equals(verifynewPassword)) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.PASSWORD_INCORRECT)
							.getBytes());
			return;
		}

		ServletContext sc = ServletActionContext.getServletContext();

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			try {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE)
							.getBytes());
			return;
		}
		//System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);

		System.out.println(" user name is " + userName);

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);

		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}

		UserInfoBean bean = (UserInfoBean) session.getAttribute("USER_INFO");

		if (bean == null) {
			response.getOutputStream().write(
					"You have to login to change your password".getBytes());
			return;
		}

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
		String ticketNo = String.valueOf(lastPrintedTicket);
		if(ticketNo.length() > 5)
		{
			ticketNo = ticketNo.substring(0, 5);
			if(bean.getUserId() == Integer.parseInt(ticketNo))
				drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(bean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		}

		String uname = bean.getUserName();
		String first = (String) session.getAttribute("FIRST");

		String returntype = "";

		ChangePasswordHelper changepass = new ChangePasswordHelper();
		if (uname != null && password != null && newPassword != null
				&& verifynewPassword != null) {
			returntype = changepass.changePassword(uname, password,
					newPassword, verifynewPassword, false);
		}

		if (returntype.equals("ERROR")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.PASSWORD_ERROR+"ErrorCode:"+EmbeddedErrors.PASSWORD_ERROR_CODE+"|").getBytes());
		} else if (returntype.equals("INPUT")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.PASSWORD_INPUT+"ErrorCode:"+EmbeddedErrors.PASSWORD_INPUT_ERROR_CODE+"|").getBytes());
		} else if (returntype.equals("NEW_OLD_SAME")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.NEW_OLD_PASSWORD_SAME_ERROR_MESSAGE+"ErrorCode:"+EmbeddedErrors.NEW_OLD_PASSWORD_SAME_ERROR_CODE+"|").getBytes());
		} else if (returntype.equals("SUCCESS")) {
			if (first.equals("true")) {
				session.setAttribute("FIRST", false);
				response.getOutputStream().write(
						"Password Changed Successfully.".getBytes());
			} else {
				response.getOutputStream().write(
						"Password Changed Successfully.".getBytes());
			}
		} else if (returntype.equals("wrongpass")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.PASSWORD_WRONG_PASS+"ErrorCode:"+EmbeddedErrors.PASSWORD_INCORRECT_ERROR_CODE+"|")
							.getBytes());
		} else if (returntype.equals("INCORRECT")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.PASSWORD_WRONG_PASS+"ErrorCode:"+EmbeddedErrors.PASSWORD_INCORRECT_ERROR_CODE+"|")
							.getBytes());
		} else {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.PASSWORD_INCORRECT+"ErrorCode:"+EmbeddedErrors.PASSWORD_INCORRECT_ERROR_CODE+"|")
							.getBytes());
		}
	}
	
	
	public void authenticatePassword() {
		System.out.println("inside change pass action");

		
		ServletContext sc = ServletActionContext.getServletContext();
		try {
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			
		}
		
		System.out.println(" user name is " + userName);

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);

		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}

		UserInfoBean bean = (UserInfoBean) session.getAttribute("USER_INFO");
		if (bean == null) {
			response.getOutputStream().write(
					"You have to login to change your password".getBytes());
			return;
		}
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
		drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(bean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);

		ChangePasswordHelper changepass = new ChangePasswordHelper();
		int loginAttempts=Integer.parseInt((String) sc
				.getAttribute("LOGIN_ATTEMPTS")) ;
			boolean returntype = changepass.authenticatePassword(userName, password,loginAttempts);
		

	
		if(returntype){
			response.getOutputStream().write(
					("SUCCESS")
							.getBytes());
		} else {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.PASSWORD_INCORRECT)
							.getBytes());
		}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LMSException e) {
			String status=e.getMessage();
			try {
			 if (status.equals("LOGIN_LIMIT_REACHED")) {
					
						response.getOutputStream().write(
								("ErrorMsg:" + EmbeddedErrors.LOGIN_LIMIT_REACHED)
										.getBytes());
					
				}else if (status.equals("PASS_NOT_MATCH")) {
					response.getOutputStream().write(
							("ErrorMsg:" + EmbeddedErrors.PASSWORD_INCORRECT)
									.getBytes());
				
			} else {
				/*
				 * response.getOutputStream().write( ("Enter Correct name
				 * password").getBytes());
				 */
				response.getOutputStream().write(
						("ErrorMsg:" + EmbeddedErrors.LOGIN_ERROR).getBytes());
				return;
			}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
	}
	
	

	public String getNewPassword() {
		return newPassword;
	}

	public String getPassword() {
		return password;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getUserName() {
		return userName;
	}

	public String getVerifynewPassword() {
		return verifynewPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setVerifynewPassword(String verifynewPassword) {
		this.verifynewPassword = verifynewPassword;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}
}