package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.skilrock.lms.beans.CustomTransactionReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CustomTransactionReportHelper;

public class CustomTransactionReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory
	.getLog(CustomTransactionReportAction.class);
	private static final long serialVersionUID = 1L;
	private String end_Date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private String orgName;
	private int retOrgId;
	private int agentOrgId;
	private String message;

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String collectionTransactionWiseWithOpeningBal() throws LMSException {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			HttpSession session = request.getSession();
			ServletContext sc = session.getServletContext();
			String dateFormat = (String) sc.getAttribute("date_format");
			String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
			SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startDate = null;
			Timestamp endDate = null;
			Timestamp deployDate = null;
			logger.info("retailer User Id>>>>>" + retOrgId);
			//For Details Please Choose start date after 2012-11-08 
			try {
				if (start_date != null && end_Date != null) {
					String lastDate = CommonMethods.getLastArchDate();
					System.out.println("last archieve date"+lastDate);
					Map<String, CustomTransactionReportBean> resultMap=null;
					CustomTransactionReportHelper helper=new CustomTransactionReportHelper();
					deployDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(deploy_Date).getTime());
					startDate = new Timestamp( sd
							.parse(start_date).getTime());
					endDate = new Timestamp(sd
							.parse(end_Date).getTime()
							+ 24 * 60 * 60 * 1000 - 1000);
					
					if(start_date.compareTo(lastDate)>0){
						// int retOrganisationId1=helper.getOrgId(retOrgId);
						resultMap = helper.collectionTransactionWiseWithOpeningBal(deployDate, startDate, endDate, retOrgId, agentOrgId, (Boolean) session.getAttribute("isDG"), (Boolean) session.getAttribute("isSE"), (Boolean) session.getAttribute("isCS"), (Boolean) session.getAttribute("isSLE"), (Boolean) session.getAttribute("isIW"), (Boolean) session.getAttribute("isVS"), reportStatusBean);
					}else{
						message="For Details Please Choose start date after "+lastDate;
					}
					
					session.setAttribute("result", resultMap);
					session.setAttribute("orgName", ((UserInfoBean) session
							.getAttribute("USER_INFO")).getOrgName());
					session.setAttribute("orgAdd", helper
							.getOrgAdd(((UserInfoBean) session
									.getAttribute("USER_INFO")).getUserOrgId(), reportStatusBean));
					
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
		} else
			return "RESULT_TIMING_RESTRICTION";

		return SUCCESS;
	}

/*	@SuppressWarnings("unchecked")
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
			helper.collectionAgentWiseExpand(startDate, endDate,
					(Boolean) session.getAttribute("isDG"), (Boolean) session
							.getAttribute("isSE"), (Boolean) session
							.getAttribute("isCS"), agtMap);
			session.setAttribute("gameList", helper.allGameMap());
			session.setAttribute("resultExpand", agtMap);
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;
	}*/

	@Override
	public String execute() throws LMSException {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			HttpSession session = request.getSession();
			// CollectionReportOverAllHelper helper = new
			// CollectionReportOverAllHelper();
			// Map<String, Boolean> serMap = helper.checkAvailableService();
			session.setAttribute("isSE", "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsScratch()));
			session.setAttribute("isDG", "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsDraw()));
			session.setAttribute("isCS", "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsCS()));
			session.setAttribute("isSLE", "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsSLE()));
			session.setAttribute("isIW", "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsIW()));
			session.setAttribute("isVS",
					"YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS()));
		} else
			return "RESULT_TIMING_RESTRICTION";

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

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
