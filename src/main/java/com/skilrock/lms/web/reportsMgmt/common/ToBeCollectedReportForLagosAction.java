package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.skilrock.lms.coreEngine.reportsMgmt.common.ToBeCollectedReportForLagosHelper;

public class ToBeCollectedReportForLagosAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	Log logger = LogFactory.getLog(ToBeCollectedReportForLagosAction.class);
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String startDate;
	private String valueToSend;
	private String message;

	public String getToBeColletedReportForLagos() throws LMSException {
		HttpSession session = null;
		ServletContext sc = null;
		String dateFormat = null;
		String deploy_Date = null;
		Timestamp strtDate = null;
		Timestamp deployDate = null;
		boolean isDraw = false;
		try {
			session = request.getSession();
			isDraw = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw());
			session.setAttribute("isDG", isDraw);
			session.setAttribute("isIW", "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW()));
			sc = session.getServletContext();
			dateFormat = (String) sc.getAttribute("date_format");
			deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
			if (startDate != null) {
				strtDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd")
						.parse(startDate).getTime());
				
					String lastDate = CommonMethods.getLastArchDate();
					logger.info("last archieve date"+lastDate);
					setMessage(null);
					if(startDate.compareTo(lastDate)<=0){
						setMessage("For Details Please Choose start date after "+lastDate);
						return SUCCESS;
					}
				
				deployDate = new Timestamp((new SimpleDateFormat(dateFormat))
						.parse(deploy_Date).getTime());
				ToBeCollectedReportForLagosHelper helper = new ToBeCollectedReportForLagosHelper();
				Map<String, CollectionReportOverAllBean> resultMap = helper
						.fetchDataForAgent(deployDate, strtDate);

				session.setAttribute("resultExpand", resultMap);
				session.setAttribute("gameList", ReportUtility.allGameMap(new Timestamp(strtDate.getTime()-1000)));
				session.setAttribute("orgName", ((UserInfoBean) session
						.getAttribute("USER_INFO")).getOrgName());
				session.setAttribute("orgAdd", helper
						.getOrgAdd(((UserInfoBean) session
								.getAttribute("USER_INFO")).getUserOrgId()));
				DateBeans dBean = new DateBeans();
				dBean.setStartDate(strtDate);
				dBean.setEndDate(strtDate);
				dBean.setReportType("");
				session.setAttribute("datebean", dBean);
			} else {
				return NONE;
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;

	}

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=ToBeCollectedReport.xls");
		PrintWriter out = response.getWriter();
		if (valueToSend != null) {
			valueToSend = valueToSend.replaceAll("<tbody>", "").replaceAll(
					"</tbody>", "").trim();
			//out.write("<table border='1' width='100%' >" + valueToSend
					//+ "</table>");
			out.write(valueToSend);
		}
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getValueToSend() {
		return valueToSend;
	}

	public void setValueToSend(String valueToSend) {
		this.valueToSend = valueToSend;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
