package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
import net.sf.json.JSONObject;

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
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawPwtReportHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawPwtReportHelperSP;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.IDrawPwtReportHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DrawPwtReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private int agentOrgId;
	private String end_Date;
	Log logger = LogFactory.getLog(DrawPwtReportAction.class);
	private String reportType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String lastDate;
	private String countryCode;
	private String stateCode;
	private String cityCode;
	private Map<String, String> stateMap;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Map<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}

	public void fetchStateList() {
		stateMap = CommonMethods.fetchStateList();
	}
	
	@Override
	public String execute() throws Exception {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		fetchStateList();

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			return SUCCESS;
		} else {
			return "RESULT_TIMING_RESTRICTION";
		}
	}

	public void getCityList() throws Exception {
		Map<String, String> cityMap = CommonMethods.fetchCityListStateWise(stateCode);

		PrintWriter out = response.getWriter();

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("isSuccess", true);
		jsonObject.put("cityMap", cityMap);
		out.print(jsonObject);
		out.flush();
		out.close();
	}

	public List<SalePwtReportsBean> fetchReportAgentWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawPwtAgentWise(startDate, endDate, stateCode, cityCode, reportStatusBean);
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
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawPwtAgentWiseExpand(startDate, endDate, agentOrgId, reportStatusBean);
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
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawPwtGameWise(startDate, endDate, reportStatusBean, cityCode, stateCode);
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
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawPwtGameWiseExpand(startDate, endDate, reportStatusBean);
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
	
	public List<SalePwtReportsBean> fetchReportGameWiseForAgent(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper =  new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawPwtGameWiseForAgent(startDate, endDate, agentOrgId, reportStatusBean);
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
	
	public List<SalePwtReportsBean> fetchReportGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawPwtGameWiseExpandForAgent(startDate, endDate, agentOrgId, reportStatusBean);
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
	
	public List<SalePwtReportsBean> fetchReportGameWiseBODirPly(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawBODirPlyPwtGameWise(startDate, endDate, reportStatusBean);
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<SalePwtReportsBean> fetchReportGameWiseAgentDirPly(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawAgentDirPlyPwtGameWise(startDate, endDate, agentOrgId, reportStatusBean);
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<SalePwtReportsBean> fetchReportGameWiseBODirPlyExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawBODirPlyPwtGameWiseExpand(startDate, endDate, reportStatusBean);
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<SalePwtReportsBean> fetchReportGameWiseAgentDirPlyExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawAgentDirPlyPwtGameWiseExpand(startDate, endDate, agentOrgId, reportStatusBean);
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
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {

				if (stateCode == null)
					stateCode = "ALL";
				if (cityCode == null)
					cityCode = "ALL";

				if (start_date != null && end_Date != null) {
					startDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(start_date).getTime());
					endDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(end_Date).getTime()
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
								startDate, endDate, stateCode, cityCode, reportStatusBean));
						session.setAttribute("excelData",
								(List<SalePwtReportsBean>) session
										.getAttribute("reportList"));
						session.setAttribute("reportListBODirPly",
								fetchReportGameWiseBODirPly(startDate, endDate, reportStatusBean));
						session.setAttribute("excelDataDirPlr",
								(List<SalePwtReportsBean>) session
										.getAttribute("reportListBODirPly"));
						return "AGENT_WISE";
					} else if ("Retailer Wise".equals(reportType)) {
						session.setAttribute("reportList", fetchReportRetailerWise(
								startDate, endDate, stateCode, cityCode, reportStatusBean));
						session.setAttribute("excelData",
								(List<SalePwtReportsBean>) session
										.getAttribute("reportList"));
						session.setAttribute("reportListBODirPly",
								fetchReportGameWiseAgentDirPly(startDate, endDate, reportStatusBean));
						session.setAttribute("excelDataDirPlr",
								(List<SalePwtReportsBean>) session
										.getAttribute("reportListBODirPly"));
						return "RETAILER_WISE";
					}
				}
				else{
					return NONE;
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
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				if (start_date != null && end_Date != null) {
				startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				session.setAttribute("reportList", null);
				if ("Game Wise".equals(reportType)) {
					session.setAttribute("reportList", fetchReportGameWiseForAgent(startDate, endDate, reportStatusBean));
					session.setAttribute("excelData", (List<SalePwtReportsBean>)session.getAttribute("reportList"));
					return "GAME_WISE";
				} else if ("Retailer Wise".equals(reportType)) {
					session.setAttribute("reportList", fetchReportRetailerWise(startDate, endDate, "ALL", "ALL", reportStatusBean));
					session.setAttribute("excelData", (List<SalePwtReportsBean>)session.getAttribute("reportList"));
					session.setAttribute("reportListBODirPly", fetchReportGameWiseAgentDirPly(startDate, endDate, reportStatusBean));
					session.setAttribute("excelDataDirPlr", (List<SalePwtReportsBean>)session.getAttribute("reportListBODirPly"));
					return "RETAILER_WISE";
				}
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
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
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				if(stateCode == null)
					stateCode = "ALL";
				if(cityCode == null)
					cityCode = "ALL";

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
					isExpand = false;
				}
				else if(calStart.after(calLast))
				{
					isExpand = true;
				}
				session.setAttribute("isExpand", isExpand);
				if ("Game Wise Expand".equals(reportType)) {
					session.setAttribute("reportList", fetchReportGameWiseExpand(
							startDate, endDate, reportStatusBean));
					return SUCCESS;
				} else if ("Agent Wise Expand".equals(reportType)) {
					session.setAttribute("reportList", fetchReportAgentWiseExpand(
							startDate, endDate, reportStatusBean));
					return SUCCESS;
				} else if ("BO Wise Expand".equals(reportType)){
					session.setAttribute("reportList", fetchReportGameWiseBODirPlyExpand(
							startDate, endDate, reportStatusBean));
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
				
				if(stateCode == null || stateCode.trim().isEmpty())
					stateCode = "ALL";
				if(cityCode == null || cityCode.trim().isEmpty())
					cityCode = "ALL";

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
					session.setAttribute("reportList", fetchReportRetailerWiseExpand(startDate, endDate, reportStatusBean, stateCode, cityCode));
					return SUCCESS;
				} else if ("Agent Wise Expand".equals(reportType)){
					session.setAttribute("reportList", fetchReportGameWiseAgentDirPlyExpand(startDate, endDate, reportStatusBean));
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
	
	public void exportExcel(){

		HttpSession session = request.getSession();
		List<SalePwtReportsBean> data = new ArrayList<SalePwtReportsBean>();
		List<SalePwtReportsBean> dataExpended = new ArrayList<SalePwtReportsBean>();
		List<SalePwtReportsBean> dataDirPlr = new ArrayList<SalePwtReportsBean>();
		ServletContext sc = session.getServletContext();
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		String dateFormat = (String) sc.getAttribute("date_format");
		data = (ArrayList) session.getAttribute("excelData");
		dataDirPlr = (ArrayList) session.getAttribute("excelDataDirPlr");

		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=DrawPwtReport.xls");
				WritableWorkbook w = Workbook.createWorkbook(response
						.getOutputStream());
				Timestamp startDate = null;
				Timestamp endDate = null;

				if(stateCode == null || stateCode.trim().isEmpty()) 
					stateCode = "ALL";
				if(cityCode == null || cityCode.trim().isEmpty())
					cityCode = "ALL";

				try{
					startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
							start_date).getTime());
					endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
							end_Date).getTime()
							+ 24 * 60 * 60 * 1000 - 1000);
					WriteExcelForDrawPwtReport excel = new WriteExcelForDrawPwtReport(
							startDate, endDate, reportType);
					if ("Game Wise".equalsIgnoreCase(reportType)) {
					
						
					
						if(((UserInfoBean)session.getAttribute("USER_INFO")).getUserType().equalsIgnoreCase("AGENT")){
							if(!ReportUtility.isArchData(new java.sql.Date(startDate.getTime()))){
								dataExpended = fetchReportGameWiseExpandForAgent(startDate, endDate, reportStatusBean); 
							}
							
							excel.writeGameWise(data, dataExpended, w, (String) session.getAttribute("orgName"),
									(String) session.getAttribute("orgAdd"), "AGENT",
									(String) request.getSession().getServletContext()
											.getAttribute("CURRENCY_SYMBOL"), stateCode, cityCode);
						}else{
							if(!ReportUtility.isArchData(new java.sql.Date(startDate.getTime()))){
								dataExpended = fetchReportGameWiseExpand(startDate, endDate, reportStatusBean);
							}
							
							excel.writeGameWise(data, dataExpended, w, (String) session.getAttribute("orgName"),
									(String) session.getAttribute("orgAdd"), "BO",
									(String) request.getSession().getServletContext()
											.getAttribute("CURRENCY_SYMBOL"), stateCode, cityCode);
						}
			
					} else if ("Agent Wise".equalsIgnoreCase(reportType) || "Retailer Wise".equalsIgnoreCase(reportType)) {
						
						Map<Integer, List<String>> addMap = new TreeMap<Integer, List< String>>();
						addMap = helper.fetchOrgAddMap(reportType.split(" ")[0].toUpperCase(),agentOrgId);
						Iterator<Map.Entry<Integer, List<String>>> it = addMap.entrySet().iterator();
						Map<Integer, List<SalePwtReportsBean>> tempMap = new TreeMap<Integer, List<SalePwtReportsBean>>();
						List<SalePwtReportsBean> DataDirPlrExpnd = new ArrayList<SalePwtReportsBean>(); 
						while(it.hasNext()){
							Map.Entry<Integer, List<String>> pair = it.next();
							setAgentOrgId(pair.getKey());
							if("Agent Wise".equalsIgnoreCase(reportType)){
								tempMap.put(pair.getKey(), fetchReportAgentWiseExpand(startDate, endDate, reportStatusBean));
							}else if("Retailer Wise".equalsIgnoreCase(reportType)){
								tempMap.put(pair.getKey(), fetchReportRetailerWiseExpand(startDate, endDate, reportStatusBean, stateCode, cityCode));
							}
						}
						if("Agent Wise".equalsIgnoreCase(reportType)){
							DataDirPlrExpnd = fetchReportGameWiseBODirPlyExpand(startDate, endDate, reportStatusBean);
						}else if("Retailer Wise".equalsIgnoreCase(reportType)){
							DataDirPlrExpnd = fetchReportGameWiseAgentDirPlyExpand(startDate, endDate, reportStatusBean);
						}
						excel.writeAgentWise(data, tempMap, dataDirPlr,DataDirPlrExpnd,w, addMap, (String)session.getAttribute("orgName"),
								(String)session.getAttribute("orgAdd"),"BO",
								(String)request.getSession().getServletContext()
										.getAttribute("CURRENCY_SYMBOL"),reportType, stateCode, cityCode);
					}
				}catch(Exception e){
					e.printStackTrace();
					throw new LMSException(getText("msg.date.format.error"));
				}
			} else {
				throw new LMSException(getText("msg.res.timing.restriction"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public List<SalePwtReportsBean> fetchReportRetailerWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean=(UserInfoBean)session.getAttribute("USER_INFO");
		reportList = helper.drawPwtRetailerWise(startDate, endDate, stateCode, cityCode, agentOrgId, reportStatusBean);
		session.setAttribute("orgName", userInfoBean.getOrgName());
		try{
			session.setAttribute("orgAdd",helper.getOrgAdd(userInfoBean.getUserOrgId()));
		}
		catch(LMSException ex){
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<SalePwtReportsBean> fetchReportRetailerWiseExpand(Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean, String stateCode, String cityCode) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		IDrawPwtReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawPwtReportHelperSP();
		}else{
			helper = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawPwtRetailerWiseExpand(startDate, endDate, agentOrgId, reportStatusBean, stateCode, cityCode);
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
	public void setLastDate(String lastDate)
	{
		this.lastDate = lastDate;
	}
}
