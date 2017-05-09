package com.skilrock.lms.web.scratchService.customerSpecificReport.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.scratchService.customerSpecificReport.beans.TicketByTicketSalePwt;
import com.skilrock.lms.web.scratchService.customerSpecificReport.service.TicketByTicketSalePwtService;

public class CustomersReportAction extends ActionSupport implements  ServletRequestAware, ServletResponseAware {
	
	/**
	 * @author Mukesh Sharma
	 * Date : 07-02-17
	 */
	static Log logger = LogFactory.getLog(CustomersReportAction.class);
	private static final long serialVersionUID = 1L;
	
	private static final String RESULT_TIMING_RESTRICTION = "RESULT_TIMING_RESTRICTION";

    private String responseData;
	
	private String start_date;
	private String end_Date;

	private String reportType;
	private int agentOrgId;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private TicketByTicketSalePwtService ticketByTicketSalePwtService = null;
	public Map<String, Map<Integer, TicketByTicketSalePwt>> orgWiseTicketByTicketSalePwt = null;
	public Map<Integer, String> gameMap = null;
	private String reportData;
	private String reportName;
	private String showRegionalWise ;
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public CustomersReportAction(){
		this.ticketByTicketSalePwtService = new TicketByTicketSalePwtService(); 
	}
	
	public CustomersReportAction(TicketByTicketSalePwtService ticketByTicketSalePwtService){
		this.ticketByTicketSalePwtService = ticketByTicketSalePwtService; 
	}
	
	
	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	
	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getReportType() {
		return reportType;
	}

	public String getStart_date() {
		return start_date;
	}

	
	public void setEnd_Date(String end_Date) {
		System.out.println("end date called" + end_Date);
		if (end_Date != null) {
			this.end_Date = GetDate.getSqlToUtilFormatStr(end_Date);
		} else {
			this.end_Date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}


	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public void setServletRequest(HttpServletRequest req) {
		request = req;

	}

	public void setStart_date(String start_date) {
		logger.info("first date called" + start_date);
		if (start_date != null) {
			this.start_date = GetDate.getSqlToUtilFormatStr(start_date);
		} else {
			this.start_date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}


	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	
	
	public String customerSpecificReport() {
		String actionName = ActionContext.getContext().getName();
		showRegionalWise=((UserInfoBean)request.getSession().getAttribute("USER_INFO")).getRoleId()==1?"true":"false";
		System.out.println("isRegionalWiseEnabled"+showRegionalWise);
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);
		if(SUCCESS.equalsIgnoreCase(reportStatusBean.getReportStatus())) {
			request.getSession().setAttribute("stDate", new java.sql.Date(new java.util.Date().getTime()));			
		} else
			return RESULT_TIMING_RESTRICTION;
		return SUCCESS;
	}
	
	public String generateCustomerReport() {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		UserInfoBean userInfoBean=(UserInfoBean)session.getAttribute("USER_INFO");
		int roleId = userInfoBean.getRoleId();
		
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			if(!validateData()){
				return ERROR;
			}
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(getStart_date()).getTime());
			endDate =   new Timestamp((new SimpleDateFormat(dateFormat)).parse( getEnd_Date()).getTime()+ 24 * 60 * 60 * 1000 - 1000);
			gameMap = new HashMap<Integer, String>();
			gameMap = ticketByTicketSalePwtService.getGameMap();
			System.out.println("reportType"+reportType);
			orgWiseTicketByTicketSalePwt = ticketByTicketSalePwtService.getTicketByTicketSaleNPwt(reportType, getAgentOrgId(), startDate, endDate,roleId);
		    setResponseData(orgWiseTicketByTicketSalePwt.toString());
		} catch (LMSException e) {
			logger.error(e);
		} catch (ParseException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return SUCCESS;
	}
	
   private boolean validateData(){
	   if(getStart_date() == null || getEnd_Date() == null || getAgentOrgId() == 0 || getReportType() == null)
		   return false;
	 return true;	   
   }

public String getShowRegionalWise() {
	return showRegionalWise;
}

public void setShowRegionalWise(String showRegionalWise) {
	this.showRegionalWise = showRegionalWise;
}


	
}
