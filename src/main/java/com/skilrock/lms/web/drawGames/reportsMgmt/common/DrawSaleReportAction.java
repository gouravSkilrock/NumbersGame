package com.skilrock.lms.web.drawGames.reportsMgmt.common;

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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.SendReportMailerMain;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelperSP;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.IDrawSaleReportHelper;
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.RegionWiseDataBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DrawSaleReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private int agentOrgId;
	private String end_Date;
	Log logger = LogFactory.getLog(DrawSaleReportAction.class);
	private String reportType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String lastDate;
	private int gameId;
	private Map<String,RegionWiseDataBean> regionDataMap;
	private String stateCode;
	private String cityCode;
	private String[] area;
	private Map<String, String> stateMap;
	
	@Override
	public String execute() throws Exception {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			HttpSession session = request.getSession();
			session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
			session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
			stateMap = CommonMethods.fetchStateList();
		} else {
			return "RESULT_TIMING_RESTRICTION";
		}

		return SUCCESS;
	}

	public List<SalePwtReportsBean> fetchReportAgentWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleAgentWise(startDate, endDate, reportStatusBean, cityCode, stateCode);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<SalePwtReportsBean> fetchReportRetailerWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleRetailerWise(startDate, endDate,agentOrgId, reportStatusBean, cityCode, stateCode);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<SalePwtReportsBean> fetchReportAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean, String stateCode, String cityCode) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleAgentWiseExpand(startDate, endDate,
				agentOrgId, reportStatusBean, stateCode, cityCode);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<SalePwtReportsBean> fetchReportRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean, String stateCode, String cityCode) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleRetailerWiseExpand(startDate, endDate,
				agentOrgId, reportStatusBean, stateCode, cityCode);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<SalePwtReportsBean> fetchReportGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleGameWise(startDate, endDate, reportStatusBean, cityCode, stateCode);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<SalePwtReportsBean> fetchReportGameWiseForAgent(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleGameWiseForAgent(startDate, endDate,agentOrgId, reportStatusBean);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<SalePwtReportsBean> fetchReportGameWiseExpand(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleGameWiseExpand(startDate, endDate, reportStatusBean);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<SalePwtReportsBean> fetchReportGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleGameWiseExpandForAgent(startDate, endDate,agentOrgId, reportStatusBean);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
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
	public String getLastDate()
	{
		return lastDate;
	}
	public String search() throws LMSException {
		logger.info("--Draw Sale Report Search ----" + reportType + "---");

		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);
		
		if(cityCode == null)
			cityCode = "ALL";
		if(stateCode == null)
			stateCode = "ALL";

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			HttpSession session = request.getSession();
			ServletContext sc = session.getServletContext();
			String dateFormat = (String) sc.getAttribute("date_format");
			Timestamp startDate = null;
			Timestamp endDate = null;
			try {
				if(start_date!=null && end_Date!=null){
				startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				session.setAttribute("reportList", null);
				if ("Game Wise".equals(reportType)) {
					session.setAttribute("reportList", fetchReportGameWise(
							startDate, endDate, reportStatusBean, cityCode, stateCode));
					session.setAttribute("excelData",
							(List<SalePwtReportsBean>) session
									.getAttribute("reportList"));
					return "GAME_WISE";
				} else if ("Agent Wise".equals(reportType)) {
					session.setAttribute("reportList", fetchReportAgentWise(
							startDate, endDate, reportStatusBean, cityCode, stateCode));
					session.setAttribute("excelData",
							(List<SalePwtReportsBean>) session
									.getAttribute("reportList"));
					return "AGENT_WISE";
				}
				else if ("Retailer Wise".equals(reportType)) {
					session.setAttribute("reportList", fetchReportRetailerWise(
							startDate, endDate, reportStatusBean, cityCode, stateCode));
					session.setAttribute("excelData",
							(List<SalePwtReportsBean>) session
									.getAttribute("reportList"));
					return "RETAILER_WISE";
				}
				}else
				{
					return NONE;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new LMSException("Date Format Error");
			}
			return ERROR;
		} else
			return "RESULT_TIMING_RESTRICTION";
	}

	public String searchExpand() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		
		boolean isExpand = false;
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
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
							startDate, endDate, reportStatusBean));
					return SUCCESS;
				} else if ("Agent Wise Expand".equals(reportType)) {
					session.setAttribute("reportList", fetchReportAgentWiseExpand(
							startDate, endDate, reportStatusBean, "ALL", "ALL"));
					return SUCCESS;
				}
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Date Format Error");
		}
		return ERROR;
	}

	
	public String searchByAgent() throws LMSException {
		logger.info("--Draw Sale Report Search ----" + reportType + "---");
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		if (start_date != null && end_Date != null) {
			try {
				String actionName = ActionContext.getContext().getName();
				ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

				if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
					startDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(start_date).getTime());
					endDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(end_Date).getTime()
							+ 24 * 60 * 60 * 1000 - 1000);
					session.setAttribute("reportList", null);
					if ("Game Wise".equals(reportType)) {
						session.setAttribute("reportList",
								fetchReportGameWiseForAgent(startDate, endDate, reportStatusBean));
						session.setAttribute("excelData",
								(List<SalePwtReportsBean>) session
										.getAttribute("reportList"));
						return "GAME_WISE";
					} else if ("Retailer Wise".equals(reportType)) {
						session.setAttribute("reportList", fetchReportRetailerWise(
								startDate, endDate, reportStatusBean, cityCode, stateCode));
						session.setAttribute("excelData",
								(List<SalePwtReportsBean>) session
										.getAttribute("reportList"));
						return "RETAILER_WISE";
					}
				} else {
					return "RESULT_TIMING_RESTRICTION";
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new LMSException("Date Format Error");
			}
		}else{
			return NONE;
		}
		return ERROR;
	}

	public String searchExpandByAgent() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		boolean isExpand = false;
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
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
					session.setAttribute("reportList", fetchReportGameWiseExpandForAgent(startDate, endDate, reportStatusBean));
					return SUCCESS;
				} else if ("Retailer Wise Expand".equals(reportType)) {
					session.setAttribute("reportList", fetchReportRetailerWiseExpand(startDate, endDate, reportStatusBean, "ALL", "ALL"));
					return SUCCESS;
				}
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Date Format Error");
		}
		return ERROR;
	}
	
	public void exportExcel() {
		HttpSession session = request.getSession();
		List<SalePwtReportsBean> data = new ArrayList<SalePwtReportsBean>();
		List<SalePwtReportsBean> dataExpended = new ArrayList<SalePwtReportsBean>();
		ServletContext sc = session.getServletContext();
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		String dateFormat = (String) sc.getAttribute("date_format");
		data = (ArrayList) session.getAttribute("excelData");

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=DrawSaleReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			Timestamp startDate = null;
			Timestamp endDate = null;
			
			if(stateCode == null || stateCode.trim().isEmpty()) 
				stateCode = "ALL";
			if(cityCode == null || cityCode.trim().isEmpty())
				cityCode = "ALL";

			try {
				String actionName = ActionContext.getContext().getName();
				ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

				if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
					startDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(start_date).getTime());
					endDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(end_Date).getTime()
							+ 24 * 60 * 60 * 1000 - 1000);
					WriteExcelForDrawSaleReport excel = new WriteExcelForDrawSaleReport(
							startDate, endDate, reportType);
					if ("Game Wise".equalsIgnoreCase(reportType)) {
						dataExpended = fetchReportGameWiseExpand(startDate, endDate, reportStatusBean);
						excel.writeGameWise(data, dataExpended, w, (String) session
								.getAttribute("orgName"), (String) session
								.getAttribute("orgAdd"), "BO", (String) request
								.getSession().getServletContext().getAttribute(
										"CURRENCY_SYMBOL"), stateCode, cityCode);
					}else if("Game Wise Expand".equalsIgnoreCase(reportType)){
						dataExpended = fetchReportGameWiseExpandForAgent(startDate, endDate, reportStatusBean);
						excel.writeGameWise(data, dataExpended, w, (String) session
								.getAttribute("orgName"), (String) session
								.getAttribute("orgAdd"), "AGENT", (String) request
								.getSession().getServletContext().getAttribute(
										"CURRENCY_SYMBOL"), stateCode, cityCode);
	
					} else if ("Agent Wise".equalsIgnoreCase(reportType) || "Retailer Wise".equalsIgnoreCase(reportType)) {
						Map<Integer, List<String>> addMap = new TreeMap<Integer, List<String>>();
						addMap = helper.fetchOrgAddMap(reportType.split(" ")[0].toUpperCase(),agentOrgId);
						Iterator<Map.Entry<Integer, List<String>>> it = addMap
								.entrySet().iterator();
						Map<Integer, List<SalePwtReportsBean>> tempMap = new TreeMap<Integer, List<SalePwtReportsBean>>();
						while (it.hasNext()) {
							Map.Entry<Integer, List<String>> pair = it.next();
							setAgentOrgId(pair.getKey());
							if("Agent Wise".equalsIgnoreCase(reportType)){
								tempMap.put(pair.getKey(), fetchReportAgentWiseExpand(startDate, endDate, reportStatusBean, stateCode, cityCode));
							}else if("Retailer Wise".equalsIgnoreCase(reportType)){
								tempMap.put(pair.getKey(), fetchReportRetailerWiseExpand(startDate, endDate, reportStatusBean, stateCode, cityCode));
							}
						}
						excel.writeAgentWise(data, tempMap, w, addMap,
								(String) session.getAttribute("orgName"),
								(String) session.getAttribute("orgAdd"), "BO",
								(String) request.getSession().getServletContext()
										.getAttribute("CURRENCY_SYMBOL"), cityCode, stateCode);
					}
				} else {
					throw new LMSException("Result Timing Restriction");
				}
			} catch (Exception e) {
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
	public String fetchRegionWiseRetSaleData(){

		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			 RegionWiseDataBean dataBean=new RegionWiseDataBean();
			 if(gameId>0){
				 dataBean.setGameId(gameId);
				 dataBean.setStartDate(start_date+" 00:00:00");
				 dataBean.setEndDate(end_Date+" 23:59:59");
				 dataBean.setStateCode(stateCode);
				 dataBean.setCityCode(cityCode);
				 StringBuilder areaList =new StringBuilder();
				 if(area!=null&&area.length>0){
					 
					 for(int i=0;i<area.length;i++){
						 areaList.append(area[i]+",");
							 
					 }
				 }else{
					 
					 areaList.append("-1");
				 }
				 dataBean.setOrgId(agentOrgId);
				 dataBean.setAreaCode(areaList.toString());
				 //String actionName = ActionContext.getContext().getName();
				 //ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);
				// repType="DT";
				// DrawAnalysisReportRetailerWiseHelper helper=new DrawAnalysisReportRetailerWiseHelper();
					try {
						regionDataMap =DrawSaleReportHelper.fetchRegionWiseRetSaleData(dataBean,reportStatusBean);
					
						/*Iterator  itr = regionDataMap.entrySet().iterator();
						while(itr.hasNext()){
							Map.Entry<String,RegionWiseDataBean> entry = (Map.Entry<String,RegionWiseDataBean>)itr.next();
							System.out.println(entry.getKey()+" :"+entry.getValue().getSaleAmt());
							
						}*/
					}catch (LMSException e) {
						addActionError(e.getErrorMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
			 }else{
				 addActionError("Some Internal Error Please Try Again ");
			 }
		} else
			return "RESULT_TIMING_RESTRICTION";

		return SUCCESS;
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

	public void dailyReport() throws Exception {
		new SendReportMailerMain(null).dailyReport();
	}
	public void setLastDate(String lastDate)
	{
		this.lastDate = lastDate;
	}

	public int getGameId() {
		return gameId;
	}

	public Map<String, RegionWiseDataBean> getRegionDataMap() {
		return regionDataMap;
	}

	public String getStateCode() {
		return stateCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setRegionDataMap(Map<String, RegionWiseDataBean> regionDataMap) {
		this.regionDataMap = regionDataMap;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String[] getArea() {
		return area;
	}

	public void setArea(String[] area) {
		this.area = area;
	}

	public Map<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}
	
	

	
}
