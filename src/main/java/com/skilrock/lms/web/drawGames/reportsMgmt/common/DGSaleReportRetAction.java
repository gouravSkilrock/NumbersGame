package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGSaleReportRetHelper;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class DGSaleReportRetAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(DGSaleReportRetAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Calendar c1 = Calendar.getInstance();
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1
				.get(Calendar.DATE), 0, 0, 0);
		logger.debug("Inside MAin: " + calendarNew.getTime());
		// logger.debug(calendarNew.getTime());
	}

	List<SaleReportBean> dgSaleDetail;
	private String end_Date;
	private String PartyName;
	private HttpServletRequest request;
	private String start_date;

	private String totaltime;

	public String createReport() throws Exception {
		DateBeans dateBean1 = new DateBeans();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		int gameId=0;
		long lastPrintedTicket=0;
		ServletContext sc =  LMSUtility.sc;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try{
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBean1 = GetDate.getDate(start_date, end_Date);
		} else {
			dateBean1 = GetDate.getDate(totaltime);
		}
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DGSaleReportRetHelper dgSale = new DGSaleReportRetHelper(userInfoBean,
				dateBean1);
		dgSaleDetail = dgSale.getDGSaleDetailGameWise();
		logger.debug("dgSaleDetail :" + dgSaleDetail);
		logger.debug("presentDate :" + session.getAttribute("presentDate"));
		logger.debug("date_format :" + session.getAttribute("date_format"));
		// logger.debug(dgSaleDetail);
		// logger.debug(session.getAttribute("presentDate" ));
		// logger.debug(session.getAttribute("date_format"));
		session.setAttribute("datebean", dateBean1);
		session.setAttribute("searchResultRet", dgSaleDetail);
		return SUCCESS;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getPartyName() {
		return PartyName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setPartyName(String partyName) {
		PartyName = partyName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}
}
