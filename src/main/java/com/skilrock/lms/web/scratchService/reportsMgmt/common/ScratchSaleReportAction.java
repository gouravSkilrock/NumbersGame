package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.IScratchSaleReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ScratchSaleReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ScratchSaleReportHelperSP;
import com.skilrock.lms.web.drawGames.reportsMgmt.common.WriteExcelForDrawSaleReport;

public class ScratchSaleReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private int agentOrgId;
	private String end_Date;
	Log logger = LogFactory.getLog(ScratchSaleReportAction.class);
	private String reportType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String lastDate;
	@Override
	public String execute() throws Exception {

		return SUCCESS;
	}

	public List<SalePwtReportsBean> fetchReportAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		IScratchSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP)
		{
			helper = new ScratchSaleReportHelperSP();
		}
		else
		{
			helper = new ScratchSaleReportHelper();
		}
		
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.scratchSaleAgentWise(startDate, endDate);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean)session.getAttribute("USER_INFO")).getOrgName());
		try{
			session.setAttribute("orgAdd",helper.getOrgAdd(((UserInfoBean)session.getAttribute("USER_INFO")).getUserOrgId()));
		}
		catch(LMSException ex){
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<SalePwtReportsBean> fetchReportAgentWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		IScratchSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP)
		{
			helper = new ScratchSaleReportHelperSP();
		}
		else
		{
			helper = new ScratchSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.scratchSaleAgentWiseExpand(startDate, endDate,
				agentOrgId);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean)session.getAttribute("USER_INFO")).getOrgName());
		try{
			session.setAttribute("orgAdd",helper.getOrgAdd(((UserInfoBean)session.getAttribute("USER_INFO")).getUserOrgId()));
		}
		catch(LMSException ex){
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<SalePwtReportsBean> fetchReportGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		IScratchSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP)
		{
			helper = new ScratchSaleReportHelperSP();
		}
		else
		{
			helper = new ScratchSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.scratchSaleGameWise(startDate, endDate);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean)session.getAttribute("USER_INFO")).getOrgName());
		try{
			session.setAttribute("orgAdd",helper.getOrgAdd(((UserInfoBean)session.getAttribute("USER_INFO")).getUserOrgId()));
		}
		catch(LMSException ex){
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<SalePwtReportsBean> fetchReportGameWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		IScratchSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP)
		{
			helper = new ScratchSaleReportHelperSP();
		}
		else
		{
			helper = new ScratchSaleReportHelper();
		}
		
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.scratchSaleGameWiseExpand(startDate, endDate);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean)session.getAttribute("USER_INFO")).getOrgName());
		try{
			session.setAttribute("orgAdd",helper.getOrgAdd(((UserInfoBean)session.getAttribute("USER_INFO")).getUserOrgId()));
		}
		catch(LMSException ex){
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public String getEnd_Date() {
		return end_Date;
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

	public String getStart_date() {
		return start_date;
	}

	public String search() throws LMSException {
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
			session.setAttribute("reportList", null);
			if ("Game Wise".equals(reportType)) {
				session.setAttribute("reportList", fetchReportGameWise(
						startDate, endDate));
				session.setAttribute("excelData", (List<SalePwtReportsBean>)session.getAttribute("reportList"));
				return "GAME_WISE";
			} else if ("Regional Office Wise".equals(reportType)) {
				session.setAttribute("reportList", fetchReportAgentWise(
						startDate, endDate));
				session.setAttribute("excelData", (List<SalePwtReportsBean>)session.getAttribute("reportList"));
				return "AGENT_WISE";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Date Format Error");
		}
		return ERROR;
	}

	public String searchExpand() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		boolean isExpand = false;
		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			session.setAttribute("reportList", null);
			lastDate = CommonMethods.getLastArchDate();
			System.out.println("last archieve date"+lastDate);
			SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
			Date oldDate = formatOld.parse(start_date);
			System.out.println("last archieve date"+lastDate);
			Calendar calStart = Calendar.getInstance();
			Calendar calLast = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date devLastDate = format.parse(lastDate);
			Date devStartDate = format.parse(format.format(oldDate));
			calStart.setTime(devStartDate);
			calLast.setTime(devLastDate);
			if(calStart.before(calLast) || calStart.equals(calLast))
			{
				isExpand = true;
			}
			else if(calStart.after(calLast))
			{
				isExpand = false;
			}
			session.setAttribute("isExpand", isExpand);
			if ("Game Wise Expand".equals(reportType)) {
				session.setAttribute("reportList", fetchReportGameWiseExpand(
						startDate, endDate));
				return SUCCESS;
			} else if ("Agent Wise Expand".equals(reportType)) {
				session.setAttribute("reportList", fetchReportAgentWiseExpand(
						startDate, endDate));
				if("SAFARIBET".equals(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
					reportType="Regional Wise Expand";
				}
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Date Format Error");
		}
		return ERROR;
	}
	
	public void exportExcel(){
		HttpSession session = request.getSession();
		List<SalePwtReportsBean> data = new ArrayList<SalePwtReportsBean>();
		List<SalePwtReportsBean> dataExpended = new ArrayList<SalePwtReportsBean>();
		ServletContext sc = session.getServletContext();
		DrawSaleReportHelper helper = new DrawSaleReportHelper();
		String dateFormat = (String) sc.getAttribute("date_format");
		data = (ArrayList) session.getAttribute("excelData");

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=ScratchSaleReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			Timestamp startDate = null;
			Timestamp endDate = null;
			try{
				startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				WriteExcelForScratchSaleReport excel = new WriteExcelForScratchSaleReport(
						startDate, endDate, reportType);
				if ("Game Wise".equalsIgnoreCase(reportType)) {
					dataExpended = fetchReportGameWiseExpand(startDate, endDate);
					excel.writeGameWise(data, dataExpended, w, (String) session.getAttribute("orgName"),
							(String) session.getAttribute("orgAdd"), "BO",
							(String) request.getSession().getServletContext()
									.getAttribute("CURRENCY_SYMBOL"));
				} else if ("Agent Wise".equalsIgnoreCase(reportType)) {
					Map<Integer, List<String>> addMap = new TreeMap<Integer, List< String>>();
					addMap = helper.fetchOrgAddMap(reportType.split(" ")[0].toUpperCase(),agentOrgId);
					Iterator<Map.Entry<Integer, List<String>>> it = addMap.entrySet().iterator();
					Map<Integer, List<SalePwtReportsBean>> tempMap = new TreeMap<Integer, List<SalePwtReportsBean>>();
					while(it.hasNext()){
						Map.Entry<Integer, List<String>> pair = it.next();
						setAgentOrgId(pair.getKey());
						if("Agent Wise".equalsIgnoreCase(reportType)|| "Retailer Wise".equalsIgnoreCase(reportType)){
							tempMap.put(pair.getKey(), fetchReportAgentWiseExpand(startDate, endDate));
						}else if("Retailer Wise".equalsIgnoreCase(reportType)){
							//tempMap.put(pair.getKey(), fetchReportRetailerWiseExpand(startDate, endDate));
						}
					}
					excel.writeAgentWise(data, tempMap, w, addMap, (String)session.getAttribute("orgName"),
							(String)session.getAttribute("orgAdd"),"BO",
							(String)request.getSession().getServletContext()
									.getAttribute("CURRENCY_SYMBOL"));
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new LMSException("Date Format Error");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
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

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
}
