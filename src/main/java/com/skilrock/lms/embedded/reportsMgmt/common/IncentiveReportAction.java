package com.skilrock.lms.embedded.reportsMgmt.common;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.skilrock.lms.beans.IncentiveReportDataBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.IncentiveReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class IncentiveReportAction implements ServletRequestAware,
		ServletResponseAware {

	private static Log logger = LogFactory.getLog(IncentiveReportAction.class);

	private String reportType;
	private String dateType;
	private String userName;
	private String startDate;
	private String endDate;
	private HttpServletRequest request;
	private HttpServletResponse response;
	HttpSession session = null;
	private String dateValue;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
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

	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public void getIncentiveReport() throws ParseException, IOException {
		StringBuilder finalData = new StringBuilder("");
		try {
			if(dateType != null && dateValue == null){
				prepareDate(reportType, dateType);
			}else if(dateType == null && dateValue != null){
				setStartDate(dateValue + " 00:00:00");
				setEndDate(dateValue + " 23:59:59");
			}
			IncentiveReportHelper helper = IncentiveReportHelper.getInstance();
			IncentiveReportDataBean bean = helper.fetchIncentiveData(
					getStartDate(), getEndDate(), userName, reportType);

			if (bean != null)
				finalData = finalData.append("Sale Amt:      "
						+ bean.getSaleAmt()
						+ "|Declared Win:  "
						+ bean.getWinningAmt()
						+ "|Non Win Amt:   "+ (bean.getNonWinAmt() > 0.00 ? bean.getNonWinAmt() : 0.00)
						+ "|Incentive Amt: "
						+ bean.getIncAmt()
						+ "|FromDate:"
						+ Util.changeFormat("yyyy-MM-dd hh:mm:ss",
								"dd-MM-yyyy", getStartDate())
						+ "|ToDate:"
						+ Util.changeFormat("yyyy-MM-dd hh:mm:ss",
								"dd-MM-yyyy", getEndDate()));
			else
				finalData = finalData.append("ErrorMsg:No Data Found");

		} catch (LMSException le) {
			finalData = finalData.append("ErrorMsg:" + le.getErrorMessage());
		} catch (Exception le) {
			finalData = finalData.append("ErrorMsg:"
					+ LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		logger.info("Final Data : " + finalData);
		response.getOutputStream().write(finalData.toString().getBytes());

	}

	private void prepareDate(String repType, String dateTyp)
			throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if ("DAILY".equalsIgnoreCase(repType)) {
			cal.add(Calendar.DATE, "ONEDAYPREV".equalsIgnoreCase(dateTyp) ? -1
					: -2);
			setStartDate(sdf.format(cal.getTime()) + " 00:00:00");
			setEndDate(sdf.format(cal.getTime()) + " 23:59:59");
		} else {
			cal.add(Calendar.DATE, "ONEWEEKPREV".equalsIgnoreCase(dateTyp) ? -7
					: -14);
			if (cal.get(Calendar.DAY_OF_WEEK) != 1)
				cal.add(Calendar.DAY_OF_WEEK,
						-(cal.get(Calendar.DAY_OF_WEEK) - 2));
			else
				cal.add(Calendar.DAY_OF_WEEK, -6);
			setStartDate(new Timestamp(sdf.parse(
					new java.sql.Date(cal.getTimeInMillis()).toString())
					.getTime()).toString());
			cal.add(Calendar.DAY_OF_MONTH, +6);
			setEndDate(new Timestamp(sdf.parse(
					new java.sql.Date(cal.getTimeInMillis()).toString())
					.getTime()
					+ 24 * 60 * 60 * 1000 - 1000).toString());
		}

	}

}
