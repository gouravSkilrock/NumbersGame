package com.skilrock.lms.web.drawGames.reportsMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.DailyBillingReportControllerImpl;
import com.skilrock.lms.dge.beans.RainbowBillingReportDataBean;
import com.skilrock.lms.dge.beans.RainbowWinReportDataBean;

public class DailyBillingReport extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(DailyBillingReport.class);
	private HttpServletRequest request;
	private HttpServletResponse response;

	private String startDate;
	private String endDate;
	private int maxDays;
	private String reportType;
	private Map<String, String> reportTypeMap;
	private Map<String, RainbowWinReportDataBean> rainbowWinReportDataMap;
	private List<RainbowBillingReportDataBean> rainbowBillingReportDataBeans;
	private String reportData;
	private String sDate;
	private String eDate;

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

	public int getMaxDays() {
		return maxDays;
	}

	public void setMaxDays(int maxDays) {
		this.maxDays = maxDays;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Map<String, String> getReportTypeMap() {
		return reportTypeMap;
	}

	public void setReportTypeMap(Map<String, String> reportTypeMap) {
		this.reportTypeMap = reportTypeMap;
	}

	public Map<String, RainbowWinReportDataBean> getRainbowWinReportDataMap() {
		return rainbowWinReportDataMap;
	}

	public void setRainbowWinReportDataMap(
			Map<String, RainbowWinReportDataBean> rainbowWinReportDataMap) {
		this.rainbowWinReportDataMap = rainbowWinReportDataMap;
	}
	
	public List<RainbowBillingReportDataBean> getRainbowBillingReportDataBeans() {
		return rainbowBillingReportDataBeans;
	}

	public void setRainbowBillingReportDataBeans(List<RainbowBillingReportDataBean> rainbowBillingReportDataBeans) {
		this.rainbowBillingReportDataBeans = rainbowBillingReportDataBeans;
	}
	
	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}
	
	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String geteDate() {
		return eDate;
	}

	public void seteDate(String eDate) {
		this.eDate = eDate;
	}

	public String dailyBillingReportAction() {
		reportTypeMap = new LinkedHashMap<String, String>();
		reportTypeMap.put("CONSOLIDATED", "CONSOLIDATED");
		reportTypeMap.put("DRAW_WISE", "Draw Wise");

		setReportTypeMap(reportTypeMap);

		setMaxDays(30);

		return SUCCESS;
	}

	public String dailyBillingReportData() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		JsonObject respObj = null;
		JsonArray respArray = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			sDate = sdf.format(dateFormat.parse(startDate));
			eDate = sdf.format(dateFormat.parse(endDate));
			Timestamp startTime = new Timestamp(dateFormat.parse(startDate).getTime());
			Timestamp endTime = new Timestamp(dateFormat.parse(endDate).getTime() + (24 * 60 * 60 * 1000 - 1000));
			if ("CONSOLIDATED".equals(reportType)) {
				respObj = (JsonObject) new DailyBillingReportControllerImpl().fetchDailyBilligReportData(startTime, endTime, reportType);
				if (respObj != null) {
					RainbowWinReportDataBean basicBean = new Gson().fromJson(respObj.get("dataBasic").getAsJsonObject(), RainbowWinReportDataBean.class);
					RainbowWinReportDataBean powerBean = new Gson().fromJson(respObj.get("dataPower").getAsJsonObject(), RainbowWinReportDataBean.class);

					rainbowWinReportDataMap = new LinkedHashMap<String, RainbowWinReportDataBean>();
					rainbowWinReportDataMap.put("BASIC", basicBean);
					rainbowWinReportDataMap.put("POWER", powerBean);
				}
			} else if ("DRAW_WISE".equals(reportType)) {
				respArray = (JsonArray) new DailyBillingReportControllerImpl().fetchDailyBilligReportData(startTime, endTime, reportType);
				if(respArray != null) {
					Type type = new TypeToken<List<RainbowBillingReportDataBean>>() {}.getType();
					rainbowBillingReportDataBeans = new Gson().fromJson(respArray, type);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void exportExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Daily_Billing_Report.xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write(reportData);
		}
		out.flush();
		out.close();
	}
	
}
