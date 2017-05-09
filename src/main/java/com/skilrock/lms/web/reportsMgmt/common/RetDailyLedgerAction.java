package com.skilrock.lms.web.reportsMgmt.common;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DailyLedgerBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetDailyLedgerHelper;
import com.skilrock.lms.embedded.reportsMgmt.common.LedgerAction;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetDailyLedgerAction extends ActionSupport implements
		ServletRequestAware {
	private static Log logger = LogFactory.getLog(LedgerAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date frDate;
	private String OrgName;
	String query = null;
	private HttpServletRequest request;
	private HttpSession session = null;
	private Date tDate;

	private String type;

	public String getDailyLedger() throws Exception {

		ServletContext sc = ServletActionContext.getServletContext();
		SimpleDateFormat sDF = new SimpleDateFormat((String) sc
				.getAttribute("date_format"));
		SimpleDateFormat dateformat = new SimpleDateFormat(
		"yyyy-MM-dd");
		session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

		int gameId=0;
		long lastPrintedTicket=0;
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

		boolean isScratch = false;
		boolean isDraw = false;

		if (((String) sc.getAttribute("IS_SCRATCH")).equalsIgnoreCase("yes")) {
			isScratch = true;
		}
		;
		if (((String) sc.getAttribute("IS_DRAW")).equalsIgnoreCase("yes")) {
			isDraw = true;
		}
		boolean isCS = "YES".equalsIgnoreCase((String) LMSUtility.sc
				.getAttribute("IS_CS"));
		Date deplDate = sDF.parse((String) sc.getAttribute("DEPLOYMENT_DATE"));
		// ServletContext sc = ServletActionContext.getServletContext();
		
		logger.debug(" user name is " + userBean.getUserName());
		DateBeans dateBeans = null;

		if ("Current Day".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = GetDate.getDate(URLDecoder.decode(type, "UTF-8"));
		} else if ("Last Day"
				.equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime()));
		}
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		frDate= dateformat.parse(dateBeans.getFirstdate().toString());
		tDate = dateformat.parse(dateBeans.getLastdate().toString());
		String orgName = userBean.getOrgName();
		OrgName = orgName;
		int orgId = userBean.getUserOrgId();
		// query = QueryManager.getST6RetWiseLedgerAgt();
		// query = QueryManager.getRetDaliyLadger();

		DailyLedgerBean CRBTemp = new DailyLedgerBean();
		DailyLedgerBean CRB = new DailyLedgerBean();
		DateBeans dateBean = new DateBeans();
		RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
		dateBean.setStartTime(new java.sql.Timestamp(deplDate.getTime()));
		dateBean.setEndTime(new java.sql.Timestamp(frDate.getTime()));
		CRBTemp = helper.getRetLegderDetail(dateBean, isScratch, isDraw, isCS, orgId);
		dateBean.setStartTime(new java.sql.Timestamp(frDate.getTime()));
		dateBean.setEndTime(new java.sql.Timestamp(tDate.getTime()));
		CRB = helper.getRetLegderDetail(dateBean, isScratch, isDraw, isCS, orgId);
		CRB.setOpenBal(CRBTemp.getClrBal());
		CRB.setClrBal(FormatNumber.formatNumber(Double.parseDouble(CRB
				.getOpenBal())
				+ Double.parseDouble(CRB.getClrBal())));

		Calendar cd = Calendar.getInstance();
		cd.setTime(new java.util.Date());
		String hour = cd.get(Calendar.HOUR_OF_DAY) + "";
		String min = cd.get(Calendar.MINUTE) + "";
		String sec = cd.get(Calendar.SECOND) + "";
		if (hour.length() <= 1) {
			hour = "0" + hour;
		}
		if (min.length() <= 1) {
			min = "0" + min;
		}
		if (sec.length() <= 1) {
			sec = "0" + sec;
		}
		session.setAttribute("ledgerType", type);
		session.setAttribute("retName", OrgName);
		session.setAttribute("todayDate", new java.sql.Date(
				(new java.util.Date()).getTime()).toString());
		session.setAttribute("todayTime", hour + ":" + min + ":" + sec);
		session.setAttribute("openBal", CRB.getOpenBal());
		session.setAttribute("purchase", CRB.getPurchase());
		session.setAttribute("netsale", CRB.getNetsale());
		session.setAttribute("netPwt", CRB.getNetPwt());
		session.setAttribute("netPayment", CRB.getNetPayment());
		session.setAttribute("clrBal", CRB.getClrBal());
		session.setAttribute("cashCol", CRB.getCashCol());
		session.setAttribute("scratchSale", CRB.getScratchSale());
		session.setAttribute("profit", CRB.getProfit());
		session.setAttribute("netSaleCS", CRB.getNetSaleCS());
		session.setAttribute("olaDeposit", CRB.getOlaDeposit());
		session.setAttribute("olaWithdrawal", CRB.getOlaWithdrawal());
		session.setAttribute("sleSale", CRB.getSleSale());
		session.setAttribute("slePwt", CRB.getSlePwt());
		session.setAttribute("iwSale", CRB.getIwSale());
		session.setAttribute("iwPwt", CRB.getIwPwt());
		session.setAttribute("vsSale", CRB.getVsSale());
		session.setAttribute("vsPwt", CRB.getVsPwt());
		return SUCCESS;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getType() {
		return type;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * private String generateReport(DailyLedgerBean CRB) throws LMSException {
	 * 
	 * Calendar cd = Calendar.getInstance(); cd.setTime(new java.util.Date());
	 * String hour = cd.get(Calendar.HOUR_OF_DAY) +""; String min =
	 * cd.get(Calendar.MINUTE) +""; String sec = cd.get(Calendar.SECOND) +"";
	 * if(hour.length() <= 1){ hour="0"+hour; } if(min.length() <= 1){
	 * min="0"+min; } if(sec.length() <= 1){ sec="0"+sec; }
	 * 
	 * String finalData = "Date:" + new java.sql.Date((new
	 * java.util.Date()).getTime()).toString() + "|Time:" + hour + ":" + min +
	 * ":" + sec; finalData += "|OB:" + CRB.getOpenBal() + "|NSales:" +
	 * CRB.getNetsale() + ",NPWT:" + CRB.getNetPwt() +",NPayment:" +
	 * CRB.getNetPayment() + ",ClosingBal:" + CRB.getClrBal(); finalData +=
	 * "|CashColl:" + CRB.getCashCol() + "|Profit:"+ CRB.getProfit() +"|retOrg:" +
	 * OrgName + "|"; logger.debug("Opening Balance:-"+CRB.getOpenBal()+"
	 * MrpSale:-"+CRB.getMrpSale()+" MrpPwt:-"+CRB.getMrpPwt()+"
	 * NetSale:-"+CRB.getNetsale()+" NetPwt:-"+CRB.getNetPwt()+"
	 * Payment:-"+CRB.getNetPayment()+" CashCol:-"+CRB.getCashCol()+"
	 * profit:-"+CRB.getProfit()); return SUCCESS; }
	 */
}
