package com.skilrock.lms.web.reportsMgmt.common;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DailyLedgerBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetDailyLedgerHelper;
import com.skilrock.lms.embedded.reportsMgmt.common.LedgerAction;
//import com.sun.jmx.snmp.Timestamp;

public class AgtDailyLedgerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static Log logger = LogFactory.getLog(LedgerAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date frDate;
	String query = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int retOrgId;
	private String retOrgName;
	private HttpSession session = null;
	private Date tDate;

	private String type;

	public String getDailyLedger() throws Exception {

		ServletContext sc = ServletActionContext.getServletContext();
		SimpleDateFormat sDF = new SimpleDateFormat((String) sc
				.getAttribute("date_format"));
		SimpleDateFormat dateformat = new SimpleDateFormat(
		"yyyy-MM-dd");
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
		session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		logger.debug(" user name is " + userBean.getUserName());
		DateBeans dateBeans = null;
		DateBeans dateBean = new DateBeans();
		

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
		/*dateBean.setFirstdate(dateBeans.getFirstdate());
		dateBean.setLastdate(dateBeans.getLastdate());*/
		//frDate = dateBeans.getFirstdate();
		frDate= dateformat.parse(dateBeans.getFirstdate().toString());
		tDate = dateformat.parse(dateBeans.getLastdate().toString());
		// query = QueryManager.getST6RetWiseLedgerAgt();
		// query = QueryManager.getRetDaliyLadger();

		DailyLedgerBean CRBTemp = new DailyLedgerBean();
		DailyLedgerBean CRB = new DailyLedgerBean();
		RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
		retOrgName = helper.getRetName(retOrgId);
		dateBean.setStartTime(new java.sql.Timestamp(deplDate.getTime()));
		dateBean.setEndTime(new java.sql.Timestamp(frDate.getTime()));
		CRBTemp = helper.getRetLegderDetail(dateBean, isScratch, isDraw, isCS,
				retOrgId);
		dateBean.setStartTime(new java.sql.Timestamp(frDate.getTime()));
		dateBean.setEndTime(new java.sql.Timestamp(tDate.getTime()));
		CRB = helper.getRetLegderDetail(dateBean, isScratch, isDraw, isCS, retOrgId);
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
		
		 //FIXME to this. Should variables be moved to request scope instead
		session.setAttribute("ledgerType", type);
		session.setAttribute("retName", retOrgName);
		session.setAttribute("todayDate", new java.sql.Date(
				(new java.util.Date()).getTime()).toString());
		session.setAttribute("todayTime", hour + ":" + min + ":" + sec);
		session.setAttribute("openBal", CRB.getOpenBal());
		session.setAttribute("purchase", CRB.getPurchase());
		session.setAttribute("netsale", CRB.getNetsale());
		session.setAttribute("netPwt", CRB.getNetPwt());
		session.setAttribute("netPayment", CRB.getNetPayment());
		session.setAttribute("sleSale", CRB.getSleSale());
		session.setAttribute("slePwt", CRB.getSlePwt());
		session.setAttribute("clrBal", CRB.getClrBal());
		session.setAttribute("cashCol", CRB.getCashCol());
		session.setAttribute("scratchSale", CRB.getScratchSale());		
		session.setAttribute("profit", CRB.getProfit());
		session.setAttribute("netSaleCS", CRB.getNetSaleCS());
		session.setAttribute("olaDeposit", CRB.getOlaDeposit());
		session.setAttribute("olaWithdrawal", CRB.getOlaWithdrawal());
		return SUCCESS;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void getRetailerListAjax() throws Exception {
		PrintWriter out = response.getWriter();
		RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
		session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		Map<Integer, String> selectMap = helper.getRetailerList(userBean
				.getUserOrgId());
		String html = "<select onchange=\"clearResult()\" class=\"option\" id=\"retOrgId\" name=\"retOrgId\"><OPTION VALUE=-1>--Please Select--";

		Iterator<Map.Entry<Integer, String>> it = selectMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, String> pair = it.next();
			html += "<option class=\"option\" value=\"" + pair.getKey() + "\">"
					+ pair.getValue() + "</option>";
		}
		html += "</select>";
		response.setContentType("text/html");

		out.print(html);
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public String getType() {
		return type;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setType(String type) {
		this.type = type;
	}
}
