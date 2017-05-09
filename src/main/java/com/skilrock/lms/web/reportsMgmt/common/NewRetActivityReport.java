package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.NewRetActivityBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.RetActivityColumnStatusBean;
import com.skilrock.lms.beans.RetailerActivityHistoryBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.reportsMgmt.common.NewRetActivityReportHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;

public class NewRetActivityReport extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(NewRetActivityReport.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int agentOrgId;
	private HttpServletRequest request;
	private HttpServletResponse response;
	Map<Integer, NewRetActivityBean> retActivityMap;
	private String selectMode;
	private String activityType;
	private ArrayList<String>  city;
	private RetActivityColumnStatusBean columnStatus;
	private List<String> terminalType;
	private List<String> conDevice;

	private String start_date;
	private String end_Date;
	private Map<String, RetailerActivityHistoryBean> retCountMap;
	private Map<String, RetailerActivityHistoryBean> transVolumeMap;
	private Map<String, List<String>> deviceMap;
	private Map<String, List<Integer>> versionMap;
	private Map<String, List<Integer>> connModeMap;
	private Map<String, List<Integer>> locationMap;
	private List<String> connModeNameList;
	private String selectType;
	private String selectState;
	private String selectDevice;
	private List<String> versionList;
	private List<String> serviceList;
	private String selectService;
	private List<String> cityList;
	private List<String> cityCodeList;
	private List<String> stateList;
	private String orgCode;
	private int orgId;
	private String reportData;

	@Override
	public String execute() throws LMSException {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			setCity(CommonFunctionsHelper.getCityNameList((String) LMSUtility.sc
				.getAttribute("COUNTRY_DEPLOYED")));
			setConDevice(CommonFunctionsHelper.getTerminalTypeList(true));
			setTerminalType(CommonFunctionsHelper.getTerminalList());
			stateList = CommonFunctionsHelper.getStateList();
			serviceList = CommonFunctionsHelper.getServiceList();
			HttpSession session = request.getSession();
			UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
			setOrgCode(uib.getOrgCode());
			setOrgId(uib.getUserOrgId());
		} else
			return "RESULT_TIMING_RESTRICTION";

		return SUCCESS;

	}


	public String fetchActivityRep() throws LMSException {
		NewRetActivityReportHelper helper = new NewRetActivityReportHelper();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);
		if(uib.getUserType().equalsIgnoreCase("AGENT")){
			agentOrgId =uib.getUserOrgId();
		}
		setRetActivityMap(helper.fetchActivityTrx(agentOrgId, selectMode, reportStatusBean));
		setColumnStatus(helper.setActReportColumnStatus(activityType,
				selectMode));
		return SUCCESS;
	}

	public String fetchActivityRepHistory() throws LMSException,
			ParseException, SQLException {

		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			NewRetActivityReportHelper helper = new NewRetActivityReportHelper();
			start_date=start_date+" 00:00:00";
			end_Date=end_Date+" 23:59:59";

			if (selectType.equals("1")) {
				terminalType = CommonFunctionsHelper.getTerminalList();
				deviceMap = helper.fetchRetDeviceHistory(start_date, end_Date,
						terminalType, reportStatusBean);
				return "DAV";
			}
			if (selectType.equals("2")) {
				conDevice = CommonFunctionsHelper.getTerminalTypeList(false);
				connModeMap = helper.fetchRetConnModeHistory(start_date, end_Date,
						conDevice, reportStatusBean);
				return "CM";
			}
			if (selectType.equals("3")) {
				retCountMap = helper.fetchActRepHistoryForDrawGame(start_date,
						end_Date, reportStatusBean);
				return "RAC";
			}
			if (selectType.equals("4")) {
				transVolumeMap = helper.fetchRetTransactionVolumeHistory(
						start_date, end_Date, reportStatusBean);
				return "TV";
			}
			if (selectType.equals("5")) {
				cityList = CommonFunctionsHelper.getCityCodeAndNameList(selectState);
				cityCodeList = CommonFunctionsHelper.getCityCodeList(selectState);
				locationMap = helper.fetchRetLocationHistory(start_date, end_Date,
						cityList, selectService, reportStatusBean);
				return "LOC";
			}
		} else
			return "RESULT_TIMING_RESTRICTION";

		return SUCCESS;
	}

	public String fetchRetPosVersionHistory() throws LMSException {
		NewRetActivityReportHelper helper = new NewRetActivityReportHelper();
	/*	versionList = CommonFunctionsHelper
				.getTerminalBulidVersionList(selectDevice);*/
		versionList =	new ArrayList<String>();
		versionMap = helper.fetchRetVersionHistory(start_date, selectDevice,
				versionList);
		return SUCCESS;
	}

	private String orgNameText;
	private String locationText;
	private String terminalTypeText;
	private String connModeText;
	private String modeText;
	private String activityText;
	private String activeCount;
	private String idleCount;
	private String noSaleCount;
	private String newLoginCount;
	private String timeA;
	private String timeB;

	public String getOrgNameText() {
		return orgNameText;
	}

	public void setOrgNameText(String orgNameText) {
		this.orgNameText = orgNameText;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public String getTerminalTypeText() {
		return terminalTypeText;
	}

	public void setTerminalTypeText(String terminalTypeText) {
		this.terminalTypeText = terminalTypeText;
	}

	public String getConnModeText() {
		return connModeText;
	}

	public void setConnModeText(String connModeText) {
		this.connModeText = connModeText;
	}

	public String getModeText() {
		return modeText;
	}

	public void setModeText(String modeText) {
		this.modeText = modeText;
	}

	public String getActivityText() {
		return activityText;
	}

	public void setActivityText(String activityText) {
		this.activityText = activityText;
	}

	public String getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(String activeCount) {
		this.activeCount = activeCount;
	}

	public String getIdleCount() {
		return idleCount;
	}

	public void setIdleCount(String idleCount) {
		this.idleCount = idleCount;
	}

	public String getNoSaleCount() {
		return noSaleCount;
	}

	public void setNoSaleCount(String noSaleCount) {
		this.noSaleCount = noSaleCount;
	}

	public String getNewLoginCount() {
		return newLoginCount;
	}

	public void setNewLoginCount(String newLoginCount) {
		this.newLoginCount = newLoginCount;
	}

	public String getTimeA() {
		return timeA;
	}

	public void setTimeA(String timeA) {
		this.timeA = timeA;
	}

	public String getTimeB() {
		return timeB;
	}

	public void setTimeB(String timeB) {
		this.timeB = timeB;
	}

	public void exportToExcel() throws IOException, ParseException {
		String noSaleTime = timeB.split(":")[0];

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Retailer_Activity_Status_Report.xls");
		PrintWriter out = response.getWriter();
		StringBuilder headerData = new StringBuilder();
		headerData.append("<h3>Retailer Activity Status Report </h3>")
			.append("<table border=1px bordercolor=\"#CCCCCC\" cellpadding=\"2\" cellspacing=\"0\" >")
			.append("<tr><td>Agent Organization</td><td>").append(orgNameText).append("</td></tr>")
			.append("<tr><td>Location</td><td>").append(locationText).append("</td></tr>")
			.append("<tr><td>Terminal Type</td><td>").append(terminalTypeText).append("</td></tr>")
			.append("<tr><td>Connectivity Mode</td><td>").append(connModeText).append("</td></tr>")
			.append("<tr><td>Mode</td><td>").append(modeText).append("</td></tr>")
			.append("<tr><td>Activity</td><td>").append(activityText).append("</td></tr>")
			.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>")
			.append("<tr><td>No. of Live Retailer</td><td>").append(activeCount).append("</td></tr>")
			.append("<tr><td>No. of Idle Retailer</td><td>").append(idleCount).append("</td></tr>")
			.append("<tr><td>No. of No Sale Retailer</td><td>").append(noSaleCount).append("</td></tr>")
			.append("<tr><td>No. of New Login Retailer</td><td>").append(newLoginCount).append("</td></tr>")
			.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>")
			.append("<tr><td>Live Hours Selected</td><td>").append(timeA).append("</td></tr>")
			.append("<tr><td>Idle Hours Selected</td><td>").append(timeB).append("</td></tr>")
			.append("<tr><td>No Sale Hours Selected</td><td align=right>>").append(noSaleTime).append("</td></tr>")
			.append("</table>");
		reportData = headerData.toString()+"<br/>"+reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
		out.write(reportData);
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public Map<Integer, NewRetActivityBean> getRetActivityMap() {
		return retActivityMap;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setRetActivityMap(
			Map<Integer, NewRetActivityBean> retActivityMap) {
		this.retActivityMap = retActivityMap;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public List<String> getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(List<String> terminalType) {
		this.terminalType = terminalType;
	}

	public List<String> getConDevice() {
		return conDevice;
	}

	public void setConDevice(List<String> conDevice) {
		this.conDevice = conDevice;
	}

	public ArrayList<String> getCity() {
		return city;
	}

	public void setCity(ArrayList<String> city) {
		this.city = city;
	}

	public RetActivityColumnStatusBean getColumnStatus() {
		return columnStatus;
	}

	public void setColumnStatus(RetActivityColumnStatusBean columnStatus) {
		this.columnStatus = columnStatus;
	}

	public String getSelectMode() {
		return selectMode;
	}

	public void setSelectMode(String selectMode) {
		this.selectMode = selectMode;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
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

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Map<String, RetailerActivityHistoryBean> getRetCountMap() {
		return retCountMap;
	}

	public void setRetCountMap(
			Map<String, RetailerActivityHistoryBean> retCountMap) {
		this.retCountMap = retCountMap;
	}

	public Map<String, RetailerActivityHistoryBean> getTransVolumeMap() {
		return transVolumeMap;
	}

	public void setTransVolumeMap(
			Map<String, RetailerActivityHistoryBean> transVolumeMap) {
		this.transVolumeMap = transVolumeMap;
	}

	public List<String> getConnModeNameList() {
		return connModeNameList;
	}

	public void setConnModeNameList(List<String> connModeNameList) {
		this.connModeNameList = connModeNameList;
	}

	public Map<String, List<Integer>> getConnModeMap() {
		return connModeMap;
	}

	public void setConnModeMap(Map<String, List<Integer>> connModeMap) {
		this.connModeMap = connModeMap;
	}

	public String getSelectDevice() {
		return selectDevice;
	}

	public void setSelectDevice(String selectDevice) {
		this.selectDevice = selectDevice;
	}

	public List<String> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<String> versionList) {
		this.versionList = versionList;
	}

	public Map<String, List<String>> getDeviceMap() {
		return deviceMap;
	}

	public void setDeviceMap(Map<String, List<String>> deviceMap) {
		this.deviceMap = deviceMap;
	}

	public Map<String, List<Integer>> getVersionMap() {
		return versionMap;
	}

	public void setVersionMap(Map<String, List<Integer>> versionMap) {
		this.versionMap = versionMap;
	}

	public Map<String, List<Integer>> getLocationMap() {
		return locationMap;
	}

	public void setLocationMap(Map<String, List<Integer>> locationMap) {
		this.locationMap = locationMap;
	}

	public String getSelectService() {
		return selectService;
	}

	public void setSelectService(String selectService) {
		this.selectService = selectService;
	}

	public List<String> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

	public List<String> getCityList() {
		return cityList;
	}

	public void setCityList(List<String> cityList) {
		this.cityList = cityList;
	}

	public List<String> getCityCodeList() {
		return cityCodeList;
	}

	public void setCityCodeList(List<String> cityCodeList) {
		this.cityCodeList = cityCodeList;
	}

	public List<String> getStateList() {
		return stateList;
	}

	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}

	public String getSelectState() {
		return selectState;
	}

	public void setSelectState(String selectState) {
		this.selectState = selectState;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}
}