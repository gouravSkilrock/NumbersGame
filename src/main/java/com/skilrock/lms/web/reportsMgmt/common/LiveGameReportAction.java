package com.skilrock.lms.web.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ILiveGameReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LiveGameReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LiveGameReportHelperSP;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentSalePWTCommVarianceHelper;

public class LiveGameReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(LiveGameReportAction.class);
	private static final long serialVersionUID = 1L;
	private String agtName;
	private int agtOrgId;
	private String end_Date;
	private boolean noCash = false;
	private String reportType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String serviceName;
	private String start_date;

	@Override
	public String execute() throws LMSException {
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		AgentSalePWTCommVarianceHelper helper = new AgentSalePWTCommVarianceHelper();
		Map<String, String> serviceNameMap = helper.getServiceList();
		session.setAttribute("serviceNameMap", serviceNameMap);
		session.setAttribute("stDate", new java.sql.Date(new java.util.Date()
				.getTime()));
		AjaxRequestHelper requestHelper = new AjaxRequestHelper();
		String orgType = null;
		if ("BO".equalsIgnoreCase(userInfoBean.getUserType())) {
			orgType = "AGENT";
		} else if ("AGENT".equalsIgnoreCase(userInfoBean.getUserType())) {
			orgType = "RETAILER";
		} else {
			throw new LMSException("ERROR in Live Report");
		}
		String orgStr = requestHelper.getOrgIdList(userInfoBean.getUserOrgId(),
				orgType);
		Map<String, String> orgMap = new LinkedHashMap<String, String>();
		String orgArr[] = orgStr.split(":");
		for (String string : orgArr) {
			String org[] = string.split("\\|");
			orgMap.put(org[1], org[0]);
		}

		session.setAttribute("orgMap", orgMap);
		return SUCCESS;
	}

	public void exportExcel() {
		HttpSession session = request.getSession();
		Map<String, String> data = new LinkedHashMap<String, String>();
		data = (LinkedHashMap) request.getSession().getAttribute("reportMap");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=ConsLiveGame_Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForConsLiveGameReport excel = new WriteExcelForConsLiveGameReport(
					(DateBeans) session.getAttribute("datebean"));

			excel.write(data, w, (String) session.getAttribute("orgName"),
					(String) session.getAttribute("orgAdd"), "BO",
					(String) request.getSession().getServletContext()
							.getAttribute("CURRENCY_SYMBOL"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	public void exportAsExcel() {
		HttpSession session = request.getSession();
		List<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		ArrayList<String> gameList=new ArrayList<String>();
		
		data = (List<ArrayList<String>>) request.getSession().getAttribute("resultService");
		gameList=(ArrayList<String>)request.getSession().getAttribute("gameList");
		System.out.println(gameList);
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=ConsLiveGame_ReportExpand.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForConsLiveGameReport excel = new WriteExcelForConsLiveGameReport(
					(DateBeans) session.getAttribute("datebean"));

			excel.writeExcel(data, w, (String) session.getAttribute("orgName"),
					(String) session.getAttribute("orgAdd"), "BO",
					(String) request.getSession().getServletContext()
							.getAttribute("CURRENCY_SYMBOL"),gameList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getAgtName() {
		return agtName;
	}

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public String getConsolidateReport() throws LMSException {
		logger.debug("******getConsolidateReport*****");
		logger.debug("******Service*****" + serviceName);
		logger.debug("******Start Date*****" + start_date);
		logger.debug("******End Date*****" + end_Date);
		logger.debug("********agtOrgId****" + agtOrgId);
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String orgName = userInfoBean.getOrgName();
		ILiveGameReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP)
		{
			helper = new LiveGameReportHelperSP();
		}
		else
		{
			helper = new LiveGameReportHelper();	
		}
		String orgAdd = helper.getOrgAdd(userInfoBean.getUserOrgId());
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		DateBeans datebeans = new DateBeans();

		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			datebeans.setStartDate(new java.util.Date(startDate.getTime()));
			datebeans.setEndDate(new java.util.Date(endDate.getTime()));
			if (reportType.equalsIgnoreCase("Current Day")) {
				datebeans.setReportday(new java.util.Date(startDate.getTime()
						- 24 * 60 * 60 * 1000));
			}
			datebeans.setReportType(reportType);
			logger.debug("******Start Date Timestamp*****" + startDate);
			logger.debug("******End Date Timestamp*****" + endDate);
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		Map<String, String> reportMap = null;
		reportMap = helper.consolidateLiveGameReport(agtOrgId, startDate,
				endDate, isNoCash());
		session.setAttribute("reportMap", reportMap);
		session.setAttribute("datebean", datebeans);
		session.setAttribute("agtName", agtName);
		reportMap.put("agtName", agtName);
		session.setAttribute("orgName", orgName);
		session.setAttribute("orgAdd", orgAdd);
		session.setAttribute("agtDirPlrPwt", reportMap.get("dirPlrPwt"));
		session.setAttribute("reportType", reportType);
		return SUCCESS;
	}

	
	
	public String searchExpand() throws LMSException, SQLException {
		logger.debug("------Expand Consolidate Report------");
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);

			logger.debug("******Start Date Timestamp*****" + startDate);
			logger.debug("******End Date Timestamp*****" + endDate);
			LiveGameReportHelperSP helper = new LiveGameReportHelperSP();
				session.setAttribute("resultService", helper
					.consolidateReportExpand(startDate, endDate, agtOrgId,LMSFilterDispatcher.isRepFrmSP));
			session.setAttribute("gameList", helper.getGameList());
			session.setAttribute("orgName", ((UserInfoBean) session
					.getAttribute("USER_INFO")).getOrgName());
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}

		return SUCCESS;
	}
	
	public String getEnd_Date() {
		return end_Date;
	}

	public String getReport() throws LMSException {
		logger.debug("******getReport*****");
		logger.debug("******Service*****" + serviceName);
		logger.debug("******Start Date*****" + start_date);
		logger.debug("******End Date*****" + end_Date);
		logger.debug("********agtOrgId****" + agtOrgId);
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		LiveGameReportHelper helper = new LiveGameReportHelper();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;

		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);

			logger.debug("******Start Date Timestamp*****" + startDate);
			logger.debug("******End Date Timestamp*****" + endDate);
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		TreeMap<String, String> reportMap = null;
		if ("DG".equals(serviceName)) {
			reportMap = helper.drawReport(agtOrgId, startDate, endDate);
		} else if ("SE".equals(serviceName)) {
			reportMap = helper.scratchReport(agtOrgId, startDate, endDate);
		} else {
			throw new LMSException("ERROR in Live Report");
		}
		session.setAttribute("reportMap", reportMap);
		return SUCCESS;
	}

	public String getReportType() {
		return reportType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getStart_date() {
		return start_date;
	}

	public boolean isNoCash() {
		return noCash;
	}

	public void setAgtName(String agtName) {
		this.agtName = agtName;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setNoCash(boolean noCash) {
		this.noCash = noCash;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

}
