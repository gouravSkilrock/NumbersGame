package com.skilrock.lms.web.reportsMgmt.drawables;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.drawables.GraphicalRepOfActInactInvStatusReportHelper;

public class GraphicalRepOfActInactInvStatusReportAction extends ActionSupport implements ServletRequestAware , ServletResponseAware {
	
	final static long oneDay = 1 * 24 * 60 * 60 * 1000;
	Log logger = LogFactory.getLog(GraphicalRepOfActInactInvStatusReportAction.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final long serialVersionUID = 1L;
	private int reportType;
	private String endDate;
	private String regionName;
	private String cityName;
	private String startDate;
	private String reportItem;
	private String []zoneNamesArray;
	private String chartData;
	private String chartType;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public String onMenuLoad() throws LMSException {
		HttpSession session = request.getSession();
		session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
		return SUCCESS;
	}
	
	public String fetchActInactInvStatusReport() {
		String status = null;
		String data = null;
		Timestamp stDate = null;
		Timestamp enDate = null;
		try {
			stDate = new Timestamp(sdf.parse(startDate).getTime());
			enDate = new Timestamp(sdf.parse(endDate).getTime() + oneDay - 1000);
			if (stDate.after(enDate))
				throw new LMSException();
			data = new GraphicalRepOfActInactInvStatusReportHelper().getActInactInvStatus(stDate, enDate , reportItem, regionName,zoneNamesArray,cityName,chartType);
			setChartData(data);
			setChartType(chartType);
			status = SUCCESS;
		} catch (LMSException e) {
			status = SUCCESS;
		} catch (Exception e) {
			status = SUCCESS;
		}
		return status;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String[] getZoneNamesArray() {
		return zoneNamesArray;
	}

	public void setZoneNamesArray(String[] zoneNamesArray) {
		this.zoneNamesArray = zoneNamesArray;
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

	public String getReportItem() {
		return reportItem;
	}

	public void setReportItem(String reportItem) {
		this.reportItem = reportItem;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	
	
}
