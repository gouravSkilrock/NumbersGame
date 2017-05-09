
package com.skilrock.lms.web.drawGames.reportsMgmt.common;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.RetWiseSummaryTxnBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.RetWiseSummeryTxnReportHelper;

public class RetWiseSummeryTxnReportAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(RetWiseSummeryTxnReportAction.class);

	private String startDate;
	private String endDate;
	private List<RetWiseSummaryTxnBean> retWiseSummaryTxnBeans;
	private String status;
	private String reportData;

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String summeryTxnReportSearch() {
		SimpleDateFormat originFormat = null;
		SimpleDateFormat targetFormat = null;
		try {
			retWiseSummaryTxnBeans = new RetWiseSummeryTxnReportHelper().getSummeryTxnReport(startDate, endDate);

			originFormat = new SimpleDateFormat("yyyy-MM-dd");
			targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
			startDate = targetFormat.format(originFormat.parse(startDate));
			endDate = targetFormat.format(originFormat.parse(endDate));
		} catch (LMSException le) {
			logger.info("RESPONSE_RET_WISE_SUMMARY_TXN_REPORT_MENU-: ErrorCode:"+le.getErrorCode()+" ErrorMessage:"+le.getErrorMessage());
        	request.setAttribute("LMS_EXCEPTION",le.getErrorMessage());
        	if(le.getErrorCode() == 2012) {
        		status = le.getErrorMessage();
        		return SUCCESS;
        	}
    		return "applicationException";
		} catch (Exception e) {
			logger.info("RESPONSE_RET_WISE_SUMMARY_TXN_REPORT_MENU-: ErrorCode:"+LMSErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("LMS_EXCEPTION",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	    	return "applicationException";
		}

		return SUCCESS;
	}

	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Ret_wise_Summary_Txn_Report.xls");
		reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
		PrintWriter out = response.getWriter();
		out.write("<table border='1' width='100%' >"+reportData+"</table>");
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<RetWiseSummaryTxnBean> getRetWiseSummaryTxnBeans() {
		return retWiseSummaryTxnBeans;
	}

	public void setRetWiseSummaryTxnBeans(
			List<RetWiseSummaryTxnBean> retWiseSummaryTxnBeans) {
		this.retWiseSummaryTxnBeans = retWiseSummaryTxnBeans;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}