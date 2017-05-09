package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportOverAllHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICollectionReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;

public class CollectionReportOverAllAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(CollectionReportOverAllAction.class);
	private static final long serialVersionUID = 1L;
	private String end_Date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private String cityCode;
	private String stateCode;
	private Map<String, String> stateMap;

	public String collectionAgentWiseWithOpeningBal() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
		int roleId=((UserInfoBean) session.getAttribute("USER_INFO")).getRoleId();
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
			ICollectionReportOverAllHelper helper = null;

			if (LMSFilterDispatcher.isRepFrmSP) {
				helper = new CollectionReportOverAllHelperSP();
			} else {
				helper = new CollectionReportOverAllHelper();
			}
			Map<String, CollectionReportOverAllBean> resultMap = helper
					.collectionAgentWiseWithOpeningBal(deployDate, startDate,
							endDate, cityCode, stateCode,roleId);
			
			//remove terminate Agent
			OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
			List<String> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdStringTypeList;
	         logger.info("Terminate agent List::"+terminateAgentList);
			Set<String> agentListSet=resultMap.keySet();
			agentListSet.removeAll(terminateAgentList);
			
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
	public String collectionAgentWiseWithOpeningBalExpand() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			ICollectionReportOverAllHelper helper = null;

			if (LMSFilterDispatcher.isRepFrmSP) {
				helper = new CollectionReportOverAllHelperSP();
			} else {
				helper = new CollectionReportOverAllHelper();
			}
			Map<String, CollectionReportOverAllBean> agtMap = (Map<String, CollectionReportOverAllBean>) session
					.getAttribute("result");
			helper.collectionAgentWiseExpand(startDate, endDate, agtMap);
			session.setAttribute("gameList", ReportUtility.allGameMap(startDate));
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
		stateMap = CommonMethods.fetchStateList();
		session.setAttribute("isSE", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsScratch()));
		session.setAttribute("isDG", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw()));
		session.setAttribute("isCS", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS()));
		session.setAttribute("isOLA", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsOLA()));

		session.setAttribute("isSLE", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE()));
		session.setAttribute("isIW", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW()));
		session.setAttribute("isVS", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS()));
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

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Map<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}
	
}
