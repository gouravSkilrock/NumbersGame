package com.skilrock.lms.web.reportsMgmt.common;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportForRetHelper;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class CashCheqReportForRet extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end_Date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	String[] type = { "Daily", "Weekly", "Monthly" };

	public String cashChqReportForRet() throws Exception {

		HttpSession session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		int gameId=0;
		long lastPrintedTicket=0;
		ServletContext sc =  LMSUtility.sc;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(infoBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try{
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, infoBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, infoBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		DateBeans dateBeans = null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBeans = GetDate.getDate(start_date, end_Date);
		} else {
			dateBeans = GetDate.getDate(totaltime);
		}

		CashChqReportForRetHelper agentHelper = new CashChqReportForRetHelper(
				infoBean, dateBeans);
		CashChqReportBean cashChqBean = agentHelper.getCashChqDetail();
		session.setAttribute("retCashChqDet", cashChqBean);
		session.setAttribute("datebean", dateBeans);

		return SUCCESS;

	}

	@Override
	public String execute() {
		request.getSession().setAttribute("stDate",	new java.sql.Date(new java.util.Date().getTime()));
		UserInfoBean infoBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		int gameId=0;
		long lastPrintedTicket=0;
		ServletContext sc =  LMSUtility.sc;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		try {
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, infoBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(infoBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, infoBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
		return SUCCESS;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public void setEnd_Date(String end_Date) {
		if (end_Date != null) {
			this.end_Date = end_Date;
		} else {
			this.end_Date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setServletRequest(HttpServletRequest req) {
		request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public void setStart_date(String start_date) {
		if (start_date != null) {
			this.start_date = start_date;
		} else {
			this.start_date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

}
