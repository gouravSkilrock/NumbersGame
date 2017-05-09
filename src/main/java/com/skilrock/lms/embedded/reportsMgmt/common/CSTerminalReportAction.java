package com.skilrock.lms.embedded.reportsMgmt.common;

import java.util.Map;

import javax.servlet.ServletContext;
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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.CSTerminalReportHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class CSTerminalReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * @author amit aggarwal
	 * @since 27-Jul-2010
	 */
	static Log logger = LogFactory.getLog(CSTerminalReportAction.class);
	private static final long serialVersionUID = 1L;

	// private String data;
	// private String deviceType;
	// private String password = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	// private String terminalId;
	private int userId;
	private String userName = null;
	private long LSTktNo;

	// private double version;
	// private String profile;
	public void fetchCSTxnsReport() throws Exception {

		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

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

		String finalData = new CSTerminalReportHelper()
				.getRetLastTenTransaction(userBean);
		System.out.println("FINAL CS REPORT DATA:" + finalData);
		response.getOutputStream().write(finalData.getBytes());
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}
}
