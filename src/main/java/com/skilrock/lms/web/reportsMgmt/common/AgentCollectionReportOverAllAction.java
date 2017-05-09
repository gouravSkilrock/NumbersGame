package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.AgentCollectionReportOverAllBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.reportsMgmt.common.AgentCollectionReportOverAllHelper;

public class AgentCollectionReportOverAllAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	
	private static final long serialVersionUID = 1L;
	private String end_Date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;

	public String collectionRetailerWiseWithOpeningBal() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		int userId=userInfoBean.getUserOrgId();
		
		String dateFormat = (String) sc.getAttribute("date_format");
		String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
		Timestamp startDate = null;
		Timestamp endDate = null;
		Timestamp deployDate = null;
		try {
			if(start_date!=null && end_Date!=null){
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			deployDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(deploy_Date).getTime());
			AgentCollectionReportOverAllHelper helper = null;
			helper = new AgentCollectionReportOverAllHelper();
			Map<String, AgentCollectionReportOverAllBean> resultMap = helper
					.collectionRetailerWiseWithOpeningBal(userId,deployDate, startDate,
							endDate);
			session.setAttribute("result", resultMap);
			session.setAttribute("orgName", ((UserInfoBean) session
					.getAttribute("USER_INFO")).getOrgName());
			session.setAttribute("orgAdd", ReportUtility.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
			DateBeans dBean = new DateBeans();
			dBean.setStartDate(startDate);
			dBean.setEndDate(endDate);
			if("current Day".equalsIgnoreCase(totaltime)){
				dBean.setReportType(totaltime);
			}else{
				dBean.setReportType("");
			}
			session.setAttribute("datebean", dBean);
			}else{
				return NONE;
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String collectionRetailerWiseWithOpeningBalExpand() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		int userId=userInfoBean.getUserOrgId();
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			AgentCollectionReportOverAllHelper helper = new AgentCollectionReportOverAllHelper();
			
			Map<String, AgentCollectionReportOverAllBean> agtMap = (Map<String, AgentCollectionReportOverAllBean>) session
					.getAttribute("result");
			helper.collectionRetailerWiseExpand(userId,startDate, endDate, agtMap);
			session.setAttribute("gameList", ReportUtility.allGameMap());
			session.setAttribute("resultExpand", agtMap);
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;
	
	}
	
	@Override
	public String execute() throws LMSException {
		HttpSession session = request.getSession();
		//CollectionReportOverAllHelper helper = new CollectionReportOverAllHelper();
		//Map<String, Boolean> serMap = helper.checkAvailableService();
		session.setAttribute("isSE", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsScratch()));
		session.setAttribute("isDG", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw()));
		session.setAttribute("isCS", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS()));
		session.setAttribute("isOLA", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsOLA()));
		return SUCCESS;
	}

	
	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
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

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}
}
