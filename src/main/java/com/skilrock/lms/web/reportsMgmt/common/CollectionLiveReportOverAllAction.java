package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
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

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionLiveReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionLiveReportOverAllHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICollectionLiveReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;

public class CollectionLiveReportOverAllAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private String end_Date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
    private String message;
	
	public void exportToExcel() throws IOException{
		 response.setContentType("application/vnd.ms-excel");

	     response.setHeader("Content-Disposition", 
	    "attachment; filename=Live_Detail_Collection_Report.xls");
	     PrintWriter pw=response.getWriter();
	    
	     message=message.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
	    
	    
	   //  pw.write("<table><tbody><tr><th> Date/Time </th><th> Service </th><th> Particular </th><th> Amount </th><th> Available Balance </th><th> Remarks </th></tr><tr><td> </td><td> </td><td > Opening Balance(including CL/XCL) : </td><td> 0.0 </td><td> 992,217.25 </td><td> </td></tr></tbody></table>");
	pw.write("<table border='1' width='100%' >"+message+"</table>");
	}
	
	
	
	
	public String collectionAgentWiseWithOpeningBal() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
		Timestamp startDate = null;
		Timestamp endDate = null;
		Timestamp deployDate = null;
		try {
			if(start_date!=null && end_Date!=null){
				String actionName = ActionContext.getContext().getName();
				ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

				if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
					startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
							start_date).getTime());
					endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
							end_Date).getTime()
							+ 24 * 60 * 60 * 1000 - 1000);
					deployDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(deploy_Date).getTime());
					ICollectionLiveReportOverAllHelper helper = null;
		
					if (LMSFilterDispatcher.isRepFrmSP) {
						helper = new CollectionLiveReportOverAllHelperSP();
					} else {
						helper = new CollectionLiveReportOverAllHelper();
					}
					Map<String, CollectionReportOverAllBean> resultMap = helper
							.collectionAgentWiseWithOpeningBal(deployDate, startDate,
									endDate, (Boolean) session.getAttribute("isDG"),
									(Boolean) session.getAttribute("isSE"),
									(Boolean) session.getAttribute("isCS"), (Boolean) session.getAttribute("isOLA"), (Boolean) session.getAttribute("isSLE"), reportStatusBean);
					
					//remove terminate Agent
					OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
					List<String> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdStringTypeList;
			          System.out.println("Terminate agent List::"+terminateAgentList);
					Set<String> agentListSet=resultMap.keySet();
					agentListSet.removeAll(terminateAgentList);
					
					session.setAttribute("result", resultMap);
					session.setAttribute("orgName", ((UserInfoBean) session
							.getAttribute("USER_INFO")).getOrgCode());
					session.setAttribute("orgAdd", helper
							.getOrgAdd(((UserInfoBean) session
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
				} else {
					return "RESULT_TIMING_RESTRICTION";
				}
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
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				ICollectionLiveReportOverAllHelper helper = null;
	
				if (LMSFilterDispatcher.isRepFrmSP) {
					helper = new CollectionLiveReportOverAllHelperSP();
				} else {
					helper = new CollectionLiveReportOverAllHelper();
				}
				Map<String, CollectionReportOverAllBean> agtMap = (Map<String, CollectionReportOverAllBean>) session
						.getAttribute("result");
				helper.collectionAgentWiseExpand(startDate, endDate,
						(Boolean) session.getAttribute("isDG"), (Boolean) session
								.getAttribute("isSE"), (Boolean) session
								.getAttribute("isCS"), (Boolean) session.getAttribute("isOLA"), (Boolean) session.getAttribute("isSLE"), agtMap, reportStatusBean);
				session.setAttribute("gameList", helper.allGameMap());
				session.setAttribute("resultExpand", agtMap);
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;
	}

	@Override
	public String execute() throws LMSException {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			HttpSession session = request.getSession();
			//CollectionReportOverAllHelper helper = new CollectionReportOverAllHelper();
			//Map<String, Boolean> serMap = helper.checkAvailableService();
			session.setAttribute("isSE", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsScratch()));
			session.setAttribute("isDG", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw()));
			session.setAttribute("isCS", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS()));
			session.setAttribute("isOLA", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsOLA()));
			session.setAttribute("isSLE", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE()));
			session.setAttribute("isIW", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW()));
		} else {
			return "RESULT_TIMING_RESTRICTION";
		}

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
