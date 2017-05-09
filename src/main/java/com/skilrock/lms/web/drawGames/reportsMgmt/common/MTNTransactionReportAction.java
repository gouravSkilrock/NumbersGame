package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.dge.beans.MerchantTransactioDataBean;
import com.skilrock.lms.dge.beans.MtnCustomerCenterBean;

public class MTNTransactionReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private Map<Integer, String> reportTypeMap;
	private String mobileNbr;
	private String startDate;
	private String endDate;
	private String reportData;
	private List<MtnCustomerCenterBean> mtnCustomerCenterBeans;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Map<Integer, String> getReportTypeMap() {
		return reportTypeMap;
	}

	public void setReportTypeMap(Map<Integer, String> reportTypeMap) {
		this.reportTypeMap = reportTypeMap;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
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

	public List<MtnCustomerCenterBean> getMtnCustomerCenterBeans() {
		return mtnCustomerCenterBeans;
	}

	public void setMtnCustomerCenterBeans(
			List<MtnCustomerCenterBean> mtnCustomerCenterBeans) {
		this.mtnCustomerCenterBeans = mtnCustomerCenterBeans;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String fetchMtnTxnsMenu() {
		return SUCCESS;
	}

	public String fetchMtnTxnsSearch() {
		try {
			startDate += " 00:00:00";
			endDate += " 23:59:59";
			mtnCustomerCenterBeans = new DrawGameMgmtHelper().fetchMerchantWiseTxns("MTN", mobileNbr, startDate, endDate);
			System.out.println(mtnCustomerCenterBeans);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return SUCCESS;
	}
	
	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=MTN_Transaction_Report.xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write(reportData);
		}
		out.flush();
		out.close();
	}

}
