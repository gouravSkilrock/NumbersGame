package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.CreditDebitNoteReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CreditDebitNoteReportHelper;

/**
 * This class acts as Action class for CR-DR Report
 * 
 * @author umesh
 * 
 */

public class CreditDebitNoteReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 7787881593977946982L;

	private HttpServletRequest request;
	private HttpServletResponse response;

	private String agtName;
	private int agtOrgId;
	private String end_Date;
	private boolean noCash;
	private String reportType;
	private String serviceName;
	private String start_date;

	public String fetchMenuData() throws LMSException {
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		session.setAttribute("stDate", new java.sql.Date(new java.util.Date()
				.getTime()));
		/*	AjaxRequestHelper requestHelper = new AjaxRequestHelper();
		String orgType = null;
		if ("BO".equalsIgnoreCase(userInfoBean.getUserType())) {
			orgType = "AGENT";
		} else if ("AGENT".equalsIgnoreCase(userInfoBean.getUserType())) {
			orgType = "RETAILER";
		} else {
			throw new LMSException("ERROR in Live Report");
		}
		
		String orgStr = requestHelper.getOrgIdList(userInfoBean.getUserOrgId(),
				orgType);*/
		
		/*Map<String, String> orgMap = new HashMap<String, String>();
		String orgArr[] = orgStr.split(":");
		for (String string : orgArr) {
			String org[] = string.split("\\|");
			orgMap.put(org[1], org[0]);
		}*/

		// session.setAttribute("orgMap", orgMap);
		return SUCCESS;
	}

	public String fetchCreditDebitNoteReportData() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		DateBeans datebeans = new DateBeans();

		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			datebeans.setStartDate(new java.util.Date(startDate.getTime()));
			datebeans.setEndDate(new java.util.Date(endDate.getTime()));
			if (reportType.equalsIgnoreCase("Current Day")) {
				datebeans.setReportday(new java.util.Date(startDate.getTime()
						- 24 * 60 * 60 * 1000));
			}
			datebeans.setReportType(reportType);
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		List<CreditDebitNoteReportBean> list = new CreditDebitNoteReportHelper()
				.fetchCreditDebitNoteReportData(agtOrgId, startDate, endDate);
		session.setAttribute("CREDIT_DEBIT_NOTE_LIST", list);
		return SUCCESS;

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getAgtName() {
		return agtName;
	}

	public void setAgtName(String agtName) {
		this.agtName = agtName;
	}

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public boolean isNoCash() {
		return noCash;
	}

	public void setNoCash(boolean noCash) {
		this.noCash = noCash;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

}
