package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PayoutBean;
import com.skilrock.lms.coreEngine.reportsMgmt.common.AgentWisePayOutCenterReportHelper;

public class AgentWisePayOutCenterReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	Log logger = LogFactory.getLog(AgentWisePayOutCenterReportAction.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String message;
	private List<PayoutBean> payoutListAgentWise;
	private String valueToSend;
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public String getPayoutCenterReportForAgent(){
		logger.info("Inside getPayoutCenterReportForAgent");
		AgentWisePayOutCenterReportHelper helper = new AgentWisePayOutCenterReportHelper();
		List<PayoutBean> payoutBeanList =helper.fetchPayoutDataAgentWise();
		setMessage(null);
		if(payoutBeanList.isEmpty())
			setMessage("NO DATA FOUND");
		else
			setPayoutListAgentWise(payoutBeanList);
		return SUCCESS;
	}

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=PayOutCenterReport.xls");
		PrintWriter out = response.getWriter();
		if (valueToSend != null) {
			valueToSend = valueToSend.replaceAll("<tbody>", "").replaceAll(
					"</tbody>", "").trim();
			//out.write("<table border='1' width='100%' >" + valueToSend
				//	+ "</table>");
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<PayoutBean> getPayoutListAgentWise() {
		return payoutListAgentWise;
	}

	public void setPayoutListAgentWise(List<PayoutBean> payoutListAgentWise) {
		this.payoutListAgentWise = payoutListAgentWise;
	}

	public String getValueToSend() {
		return valueToSend;
	}

	public void setValueToSend(String valueToSend) {
		this.valueToSend = valueToSend;
	}
	
	

}
