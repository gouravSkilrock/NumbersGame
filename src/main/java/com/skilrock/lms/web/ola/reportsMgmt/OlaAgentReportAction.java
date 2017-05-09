package com.skilrock.lms.web.ola.reportsMgmt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import com.skilrock.lms.beans.OlaAgentReportBean;
import com.skilrock.lms.beans.OlaReportBean;
import com.skilrock.lms.beans.UserInfoBean;

import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaAgentReportHelper;
import com.skilrock.lms.web.ola.WriteExcelForOlaAgentReport;

public class OlaAgentReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware

{

	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
	private String walletName;
	private String start_date;
	private String end_Date;

	// mandy
	public String fetchOlaAgentDepWithReportData() {
		int walletId = 0;
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
			for (int i = 0; i < walletArr.length; i++) {
				walletId = Integer.parseInt(walletArr[0]);
			}
		}
		OlaReportBean olaReportBean = new OlaReportBean();
		olaReportBean.setFromDate(start_date + " 00:00:00");
		olaReportBean.setToDate(end_Date + " 23:59:59");
		OlaAgentReportHelper helper = new OlaAgentReportHelper();
		try {
			List<OlaReportBean> olaReportList = helper
					.fetchOlaAgentDepWithReportData(olaReportBean, walletId,
							userInfoBean.getUserOrgId());
			session.setAttribute("OLA_DEP_WITH_DATA_LIST", olaReportList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String fetchOlaAgentReportResultData() {
		int walletId = 0;
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
			for (int i = 0; i < walletArr.length; i++) {
				walletId = Integer.parseInt(walletArr[0]);
			}
		}
		String startDate = start_date;
		String endDate = end_Date;
		OlaAgentReportHelper helper = new OlaAgentReportHelper();
		try {
			List<OlaAgentReportBean> olaReportList = helper
					.fetchOlaAgentReportData(startDate, endDate, walletId,
							userInfoBean.getUserOrgId());
			session.setAttribute("OLA_AGENT_DATA_LIST", olaReportList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}

	public void exportExcel() {

		HttpSession session = getRequest().getSession();
		System.out.println("hello");
		OlaAgentReportBean olaAgentReportBean = new OlaAgentReportBean();
		List<OlaAgentReportBean> reportDetail = (List<OlaAgentReportBean>) session
				.getAttribute("OLA_AGENT_DATA_LIST");
		System.out.println("reporthjh" + reportDetail);
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=Ola Agent Report.xls");
			System.out.println("dfsdfsdgfsd");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForOlaAgentReport excel = new WriteExcelForOlaAgentReport();
			excel.writeAgentExcelRetailerWise(reportDetail, w);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
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

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
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

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

}
