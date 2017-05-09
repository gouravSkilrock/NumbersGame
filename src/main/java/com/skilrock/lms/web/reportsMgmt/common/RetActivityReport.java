package com.skilrock.lms.web.reportsMgmt.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.RetActivityBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetActivityReportHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;

public class RetActivityReport extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(RetActivityReport.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int agentOrgId;
	private String curRemaining;
	private HttpServletRequest request;
	Map<String, RetActivityBean> retActivityMap;
	private int retOrgId;
	private String start_date;
	private String end_Date;
	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}

	@Override
	public String execute() throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = request.getSession();
		RetActivityReportHelper helper = new RetActivityReportHelper();
		Map<String, String> serviceNameMap = helper.getServiceList();
		session.setAttribute("serviceNameMap", serviceNameMap);
		session.setAttribute("city", CommonFunctionsHelper
				.getCityNameList((String) sc.getAttribute("COUNTRY_DEPLOYED")));
		return SUCCESS;

	}

	public String fetchActivityRep() throws LMSException {
		RetActivityReportHelper helper = new RetActivityReportHelper();
		HttpSession session = request.getSession();
		boolean isOffline = false;
		if (((String) session.getServletContext().getAttribute("RET_OFFLINE"))
				.equalsIgnoreCase("NO")) {
			isOffline = true;
		}else if(((String) session.getServletContext().getAttribute("RET_OFFLINE"))
			.equalsIgnoreCase("yes")){
			isOffline = false;
		}
		session.setAttribute("RetActivityMap", helper.fetchActivityTrx(
				agentOrgId, isOffline));
		return SUCCESS;
	}

	public String fetchSoldBookEntry() throws LMSException {
		RetActivityReportHelper helper = new RetActivityReportHelper();
		HttpSession session = request.getSession();
		session.setAttribute("SoldBookMap", RetActivityReportHelper
				.fetchSoldBookEntry(retOrgId, curRemaining));
		return SUCCESS;
	}
	
	public String fetchActivityRepHistory() throws LMSException, ParseException {
		RetActivityReportHelper helper = new RetActivityReportHelper();
		HttpSession session = request.getSession();
		DateBeans dbean = new DateBeans();
		SimpleDateFormat sdf = new SimpleDateFormat((String)session.getServletContext().getAttribute("date_format"));
		dbean.setFirstdate(new java.sql.Date(sdf.parse(start_date).getTime()));
		dbean.setLastdate(new java.sql.Date(sdf.parse(end_Date).getTime()+ 24*60*60*1000));
		if(serviceName.equals("DG"))
		{
		session.setAttribute("retActHistMap", helper
				.fetchActRepHistoryForDrawGame(dbean));
		return "drawGame";
		}
		else if(serviceName.equals("CS"))
		{
			session.setAttribute("retActHistMapForCS", helper
					.fetchActRepHistoryForCS(dbean));
			return "CS";
		}else if(serviceName.equals("SLE")){
			session.setAttribute("retActHistMapForSL",helper.fetchActRepHistoryForSportsLottery(dbean));
			return "SLE";
		}
		else if(serviceName.equals("IW")){
			session.setAttribute("retActHistMapForIW",helper.fetchActRepHistoryForInstantWin(dbean));
			return "IW";
		}
		return SUCCESS;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public String getCurRemaining() {
		return curRemaining;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public Map<String, RetActivityBean> getRetActivityMap() {
		return retActivityMap;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setCurRemaining(String curRemaining) {
		this.curRemaining = curRemaining;
	}

	public void setRetActivityMap(Map<String, RetActivityBean> retActivityMap) {
		this.retActivityMap = retActivityMap;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}