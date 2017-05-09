package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CustomTransactionReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;

import com.skilrock.lms.coreEngine.reportsMgmt.common.Transaction_5_90_ReportHelper;

public class Transaction_5_90_ReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private String end_Date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private String orgName;
	private int retOrgId;
	private int agentOrgId;
	

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String collectionTransactionWiseWithOpeningBal() throws LMSException {
		HttpSession session = request.getSession();
		
		SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
		Timestamp startDate = null;
		Timestamp endDate = null;
		System.out.println("retailer User Id>>>>>" + retOrgId);
		
		try {
			if (start_date != null ) {
				
				startDate = new Timestamp( sd
						.parse(start_date).getTime());
				endDate = new Timestamp(sd
						.parse(start_date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				int gameId=2;
				Transaction_5_90_ReportHelper helper=new Transaction_5_90_ReportHelper();
				
				Map<String, CustomTransactionReportBean> resultMap = helper
						.collectionTransactionWiseWithOpeningBal(
								startDate, endDate, retOrgId,agentOrgId,gameId);
				session.setAttribute("result", resultMap);
				session.setAttribute("orgName", ((UserInfoBean) session
						.getAttribute("USER_INFO")).getOrgName());
				session.setAttribute("orgAdd", ReportUtility
						.getOrgAdd(((UserInfoBean) session
								.getAttribute("USER_INFO")).getUserOrgId()));
				
				DateBeans dBean = new DateBeans();
				dBean.setStartDate(startDate);
				dBean.setEndDate(endDate);
				if ("current Day".equalsIgnoreCase(totaltime)) {
					dBean.setReportType(totaltime);
				} else {
					dBean.setReportType("");
				}
				session.setAttribute("datebean", dBean);
			} else {
				return NONE;
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;
	}


	public String execute() throws LMSException {
		
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

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

}
