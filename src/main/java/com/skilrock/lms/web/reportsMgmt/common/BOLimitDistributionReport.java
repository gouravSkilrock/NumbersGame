package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.LimitDistributionReportBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.BOLimitDistributionReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.SearchAgentHelper;
import com.skilrock.lms.web.drawGames.reportsMgmt.common.DGSaleReportAction;

public class BOLimitDistributionReport extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(DGSaleReportAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Map<String, OrganizationBean> agtBalDisMap;
	private HttpServletRequest request;
	private int userOrgId;
	private int agtOrgId;
	private String agtName;
	private String start_date;
	private String end_Date;

	public String fetchAgtBalDistribution() {
		SearchAgentHelper agtBalDistHelper = new SearchAgentHelper();
		HttpSession session = request.getSession();
		UserInfoBean userBean = new UserInfoBean();
		userBean.setUserOrgId(userOrgId);
		agtBalDisMap = agtBalDistHelper.fetchAgtBalDistributionHelper(userBean);
		session.setAttribute("agtBalDistMap", agtBalDisMap);
		return SUCCESS;
	}

	public Map<String, OrganizationBean> getAgtBalDisMap() {
		return agtBalDisMap;
	}

	/*public String getLimitDistributionForBO() throws LMSException {
		BOLimitDistributionReportHelper helper = new BOLimitDistributionReportHelper();
		HttpSession session = request.getSession();
		
			 Map<String, LimitDistributionReportBean> LimitDistributionOfAgentsForBO=null;
			 SimpleDateFormat sdf=null;
			 Timestamp startDate=null;
			 Timestamp endDate=null;
	    try{
	    LimitDistributionOfAgentsForBO=helper.LimitDistributionOfAgentsForBO();
		start_date=Utility.getPropertyValue("DEPLOYMENT_DATE");
		sdf=new SimpleDateFormat("dd-MM-yyyy");
		startDate=new Timestamp(sdf.parse(start_date).getTime());
		endDate=new Timestamp(new Date().getTime());
		OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
		List<String> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdStringTypeList;
        Set<String> agentListSet=LimitDistributionOfAgentsForBO.keySet();
		agentListSet.removeAll(terminateAgentList);
	    }catch (Exception e) {
			e.printStackTrace();
		}
        session.setAttribute("BOLimitDistribList",LimitDistributionOfAgentsForBO);
		return SUCCESS;
	}*/
	

	public String getLimitDistributionForBO() throws LMSException {
		BOLimitDistributionReportHelper helper = new BOLimitDistributionReportHelper();
		HttpSession session = request.getSession();
		session.setAttribute("BOLimitDistribList", helper
				.LimitDistributionOfAgentsForBO());
		return SUCCESS;
	}
	
	public String orgCreditUpdateReport()throws LMSException, ParseException{
		BOLimitDistributionReportHelper helper = new BOLimitDistributionReportHelper();
		HttpSession session = request.getSession();
		SimpleDateFormat sf = new SimpleDateFormat((String)session.getAttribute("date_format"));
		DateBeans dBean = new DateBeans();
		dBean.setFirstdate(new java.sql.Date(sf.parse(start_date).getTime()));
		dBean.setLastdate(new java.sql.Date(sf.parse(end_Date).getTime()));
		session.setAttribute("retCreditUpdateReportList", helper
				.fetchRetCreditUpdateReport(dBean,agtOrgId));
		return SUCCESS;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getUserOrgId() {
		return userOrgId;
	}

	public void setAgtBalDisMap(Map<String, OrganizationBean> agtBalDisMap) {
		this.agtBalDisMap = agtBalDisMap;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
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

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getAgtName() {
		return agtName;
	}

	public void setAgtName(String agtName) {
		this.agtName = agtName;
	}
}
