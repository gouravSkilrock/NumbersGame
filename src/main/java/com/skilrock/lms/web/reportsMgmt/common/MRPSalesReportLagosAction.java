package com.skilrock.lms.web.reportsMgmt.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.beans.CollectionReportOverAllBean;

import com.skilrock.lms.coreEngine.reportsMgmt.common.MRPSalesReportLagosHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;

import java.text.ParseException;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;

public class MRPSalesReportLagosAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	Log logger = LogFactory.getLog(MRPSalesReportLagosAction.class);
	private static final long serialVersionUID = 1L;
	private String end_Date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private String message;
	
	public String getGameWiseMRPDetailsForLagos() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
		Timestamp startDate = null;
		Timestamp endDate = null;
		Timestamp deployDate = null;
		try {
			UserInfoBean userBean=(UserInfoBean) session.getAttribute("USER_INFO");
			if(start_date!=null && end_Date!=null){

			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			deployDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(deploy_Date).getTime());

			String lastArchDate = CommonMethods.getLastArchDate();
			String startDateForArch = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
			if(startDateForArch.compareTo(lastArchDate)<=0){
				message = "Please Select Date After Archive Date.";
				return SUCCESS;
			}

			MRPSalesReportLagosHelper helper = new MRPSalesReportLagosHelper();

			Map<String, CollectionReportOverAllBean> resultMap = helper
					.getGameWiseMRPDetailsLagos(userBean.getUserOrgId(),deployDate, startDate,
							endDate, (Boolean) session.getAttribute("isDG"),
							(Boolean) session.getAttribute("isSE"),
							(Boolean) session.getAttribute("isCS"), (Boolean) session.getAttribute("isOLA"),(Boolean) session.getAttribute("isIW"));
			
			//remove terminate Agent
			OrganizationTerminateReportHelper.getTerminateRetailerListForRep(startDate, endDate,userBean.getUserOrgId());
			List<String> terminateRettList=OrganizationTerminateReportHelper.RetailerOrgIdStringTypeList;
			logger.info("Terminate Retailer List::"+terminateRettList);
			Set<String> retListSet=resultMap.keySet();
			retListSet.removeAll(terminateRettList);
			
			session.setAttribute("resultExpand", resultMap);
			session.setAttribute("gameList", helper.allGameMap(startDate));
			session.setAttribute("orgName", userBean.getOrgName());
			session.setAttribute("orgAdd", helper
					.getOrgAdd(userBean.getUserOrgId()));
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

	
	public String execute() throws LMSException {
		HttpSession session = request.getSession();
		session.setAttribute("isSE", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsScratch()));
		session.setAttribute("isDG", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw()));
		session.setAttribute("isCS", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS()));
		session.setAttribute("isOLA", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsOLA()));
		session.setAttribute("isIW", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW()));
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

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
}
