package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ActiveInactiveTerminalReportBean;
import com.skilrock.lms.common.exception.LMSException;

public class ActInactInvStatusReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	final static long oneDay = 1 * 24 * 60 * 60 * 1000;
	Log logger = LogFactory.getLog(ActInactInvStatusReportAction.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private int agentName;
	private int reportType;
	private String creteria;
	private double amount;
	private String endDate;
	private String cityName;
	private String agentCode;
	private String startDate;
	private String valueToSend;
	
	private String []cityNamesArray;
	private List<ActiveInactiveTerminalReportBean> actInactInvStatusList;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public String onMenuLoad() throws LMSException {
		HttpSession session = request.getSession();
		session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
		return SUCCESS;
	}

	public void getAgentMap() {
			try {
				response.getWriter().write(new ActInactInvStatusReportHelper().getAgentList(new Timestamp(sdf.parse(endDate).getTime() + oneDay - 1000)));
			} catch (LMSException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (ParseException e) {
				e.printStackTrace();
			}
		return;

	}

	public String fetchActInactInvStatusReport() {

		String status = null;
		Timestamp stDate = null;
		Timestamp enDate = null;
		ActInactInvStatusReportHelper helper = null;
		List<ActiveInactiveTerminalReportBean> actInactInvStatusList= null;
		try {
			helper = new ActInactInvStatusReportHelper();
		if(reportType==4){
			stDate = new Timestamp(sdf.parse(startDate).getTime() + oneDay - 1000);
			actInactInvStatusList = helper.getActInactInvStatus(stDate, cityNamesArray, creteria, amount);
		}else{
			stDate = new Timestamp(sdf.parse(startDate).getTime());
			enDate = new Timestamp(sdf.parse(endDate).getTime() + oneDay - 1000);
			if (stDate.after(enDate))
				throw new LMSException();
			actInactInvStatusList = helper.getActInactInvStatus(stDate, enDate ,reportType , agentName, cityNamesArray, creteria, amount);
				setAgentCode(getAgentCode());
		}
		String temp[]=startDate.split("-");
		setReportType(reportType);
		setStartDate(temp[2]+"-"+temp[1]+"-"+temp[0]);
		String temp1[]=endDate.split("-");
		setEndDate(temp1[2]+"-"+temp1[1]+"-"+temp1[0]);
		setCityName(cityName);
		status = SUCCESS;
		setActInactInvStatusList(actInactInvStatusList);

		} catch (LMSException e) {
			status = SUCCESS;
		} catch (Exception e) {
			status = SUCCESS;
		}
		return status;
	}

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=Active_Inactive_Status_Report.xls");
		PrintWriter out = response.getWriter();
		if (valueToSend != null) {
			valueToSend = valueToSend.replaceAll("<tbody>", "").replaceAll(
					"</tbody>", "").trim();
			//out.write("<table border='1' width='100%' >" + valueToSend
				//	+ "</table>");
			out.write(valueToSend);
		}
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

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public String getCreteria() {
		return creteria;
	}

	public void setCreteria(String creteria) {
		this.creteria = creteria;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public int getAgentName() {
		return agentName;
	}

	public void setAgentName(int agentName) {
		this.agentName = agentName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	
	public List<ActiveInactiveTerminalReportBean> getActInactInvStatusList() {
		return actInactInvStatusList;
	}

	public void setActInactInvStatusList(
			List<ActiveInactiveTerminalReportBean> actInactInvStatusList) {
		this.actInactInvStatusList = actInactInvStatusList;
	}

	public String[] getCityNamesArray() {
		return cityNamesArray;
	}

	public void setCityNamesArray(String[] cityNamesArray) {
		this.cityNamesArray = cityNamesArray;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getValueToSend() {
		return valueToSend;
	}

	public void setValueToSend(String valueToSend) {
		this.valueToSend = valueToSend;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}
}
