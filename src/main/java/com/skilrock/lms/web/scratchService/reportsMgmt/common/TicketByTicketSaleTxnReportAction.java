package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.AgentWiseTktByTktSaleTxnBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.TicketByTicketSaleTxnReportHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class TicketByTicketSaleTxnReportAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	
	private static final String RETAILER = "RETAILER";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String SUCCESS_STATUS = "SUCCESS";
	private static final String RESULT_TIMING_RESTRICTION = "RESULT_TIMING_RESTRICTION";
	/**
	 * @author Bahadur Singh Sandhu
	 * Date : 07-02-17
	 */
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(TicketByTicketSaleTxnReportAction.class);
	private int agtId;
	private String agtOrgName;
	private String dateType;
	private String end_Date;
	private String reportType;
	private HttpServletRequest request;
	private String start_date;
	private String orgType;
	
	public String tktByTktSaleReport_RetailerWise() throws LMSException{
		logger.debug("----------Retailer Wise---Agent Id--" + agtId);
		HttpSession session = request.getSession();
		Timestamp startDate = null;
		Timestamp endDate = null;
		String organizationType="";
		UserInfoBean userInfoBean= (UserInfoBean) session.getAttribute("USER_INFO");
		organizationType = userInfoBean.getUserType();
		try{
			
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);
			if(SUCCESS_STATUS.equals(reportStatusBean.getReportStatus())){
				startDate = new Timestamp((new SimpleDateFormat(DATE_FORMAT)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(DATE_FORMAT)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				
				logger.debug("******Start Date Timestamp*****" + startDate);
				logger.debug("******End Date Timestamp*****" + endDate);
				
				
			//	TicketByTicketSaleTxnReportHelper helper = new TicketByTicketSaleTxnReportHelper();
				
				Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> reportMap = TicketByTicketSaleTxnReportHelper
						.reportForRetailerTicketByTktTxn(startDate, endDate, agtId, reportStatusBean,organizationType);
				Map<String, String> orgMap = ReportUtility.getOrgMap(RETAILER);
				session.setAttribute("resultService", reportMap);
				session.setAttribute("orgMap", orgMap);
			
			} else {
				return RESULT_TIMING_RESTRICTION;
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;
	}
	public String execute(){
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if(SUCCESS_STATUS.equals(reportStatusBean.getReportStatus())) {
			HttpSession session = request.getSession();
			session.setAttribute("isSE",ReportUtility.isSE);
		} else
			return RESULT_TIMING_RESTRICTION;

		return SUCCESS;
	}
	
	
	
	
	public int getAgtId() {
		return agtId;
	}
	public void setAgtId(int agtId) {
		this.agtId = agtId;
	}
	public String getAgtOrgName() {
		return agtOrgName;
	}
	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public String getEnd_Date() {
		return end_Date;
	}
	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
}
