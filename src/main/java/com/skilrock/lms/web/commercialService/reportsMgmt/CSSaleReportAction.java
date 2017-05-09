package com.skilrock.lms.web.commercialService.reportsMgmt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.skilrock.lms.beans.CSSaleReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.SendReportMailerMain;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.CSSaleReportHelper;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.CSSaleReportHelperSP;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.CSSaleReportIF;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.WriteExcelForCSSaleReport;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class CSSaleReportAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	/**
	 * Default Serial Version Id
	 */
	private static final long serialVersionUID = 1L;
	private int agentOrgId;
	private int retOrgId;
	private int catId;
	private String end_Date;
	Log logger = LogFactory.getLog(CSSaleReportAction.class);
	private String reportType;
	private String totalTime;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String lastDate;
	
	@Override
	public String execute() throws Exception {
		logger.debug("--CS Sale Report----");
		
		ServletContext sc =  LMSUtility.sc;
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		int gameId=0;
		long lastPrintedTicket=0;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try{
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		return SUCCESS;
	}

	public List<CSSaleReportBean> fetchReportAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.CSSaleAgentWise(startDate, endDate);
		HttpSession session = request.getSession();
		session.setAttribute("csRepList", reportList);
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.debug("---reportList---" + reportList);
		return reportList;
	}
	
	public List<CSSaleReportBean> fetchReportRetailerWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.CSSaleRetailerWise(startDate, endDate,agentOrgId);
		HttpSession session = request.getSession();
		session.setAttribute("csRepList", reportList);
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<CSSaleReportBean> fetchReportAgentWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.CSSaleProductWiseAgentWise(startDate, endDate,
				agentOrgId);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<CSSaleReportBean> fetchReportRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.CSSaleProductWiseRetailerWise(startDate, endDate,
				retOrgId);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<CSSaleReportBean> getReportRetailerWise(
			Timestamp startDate, Timestamp endDate,int retOrgId) throws SQLException {
		logger.info("------Agent Org Id---" + agentOrgId);
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.getCSSaleRetailerWise(startDate, endDate,
				retOrgId);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}

	public List<CSSaleReportBean> fetchReportGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.CSSaleCategoryWise(startDate, endDate);
		HttpSession session = request.getSession();
		session.setAttribute("csRepList", reportList);
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.debug("---reportList---" + reportList);
		return reportList;
	}
	
	public List<CSSaleReportBean> fetchReportGameWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.CSSaleProductWise(startDate, endDate, catId);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public List<CSSaleReportBean> CSSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agtOrgId) throws SQLException {
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> reportList = null;
		reportList = helper.CSSaleProductWiseAgentWise(startDate, endDate, agentOrgId);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", helper
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
	public void exportExcel(){
		HttpSession session = request.getSession();
		CSSaleReportIF helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new CSSaleReportHelperSP();
		}else{
			helper = new CSSaleReportHelper();
		}
		List<CSSaleReportBean> data = new ArrayList<CSSaleReportBean>();
		Map<String, List<CSSaleReportBean>> dataMap = new TreeMap<String, List<CSSaleReportBean>>();
		Map<Integer, List<String>> orgAddMap = null; 
		reportType = (String)session.getAttribute("filter");
		data = (ArrayList) request.getSession().getAttribute("csRepList");
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
			if ("Category Wise".equals(reportType)) {
				Map<Integer, String> catMap = helper.fetchActiveCategoryMap();
				for(int catId:catMap.keySet()){
					this.catId = catId;
					dataMap.put(catId+"_"+catMap.get(catId), fetchReportGameWiseExpand(startDate, endDate));
				}
			}else if ("Agent Wise".equals(reportType)) {
				orgAddMap = helper.fetchOrgAddMap("AGENT", 0);
				for(int agtId:orgAddMap.keySet()){
					this.agentOrgId = agtId;
					dataMap.put(agtId+"_"+orgAddMap.get(agtId).get(0), fetchReportAgentWiseExpand(startDate, endDate));
				}
			}
			else if ("Retailer Wise".equals(reportType)) {
				orgAddMap = helper.fetchOrgAddMap("RETAILER", agentOrgId);
				for(int retId:orgAddMap.keySet()){
					dataMap.put(retId+"_"+orgAddMap.get(retId).get(0), fetchReportRetailerWiseExpand(startDate, endDate));
				}
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=CSSaleReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForCSSaleReport excel = new WriteExcelForCSSaleReport(
					(DateBeans) session.getAttribute("datebean"));
				excel.write(data, dataMap, w, (String) session.getAttribute("orgName"),
						(String) session.getAttribute("orgAdd"),"BO",orgAddMap,
						(String) request.getSession().getServletContext()
							.getAttribute("CURRENCY_SYMBOL"), (String) session
							.getAttribute("filter"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		logger.debug("--CS Sale Report Search ----" + reportType + "---");
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		DateBeans dateBean1 = new DateBeans();
		if ("Date Wise".equalsIgnoreCase(totalTime)) {
			dateBean1 = GetDate.getDate(start_date, end_Date);
		} else {
			dateBean1 = GetDate.getDate(totalTime);
		}
		dateBean1.setReportType(reportType);
		dateBean1.setReportday(new java.util.Date());
		session.setAttribute("datebean", dateBean1);
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
			session.setAttribute("filter",reportType);
			if ("Category Wise".equals(reportType)) {
				session.setAttribute("reportList", fetchReportGameWise(
						startDate, endDate));
				session.setAttribute("excelData",
						(List<CSSaleReportBean>) session
								.getAttribute("reportList"));
				return "CATEGORY_WISE";
			}else if ("Agent Wise".equals(reportType)) {
				session.setAttribute("reportList", fetchReportAgentWise(
						startDate, endDate));
				session.setAttribute("excelData",
						(List<CSSaleReportBean>) session
								.getAttribute("reportList"));
				return "AGENT_WISE";
			}
			else if ("Retailer Wise".equals(reportType)) {
				session.setAttribute("reportList", fetchReportRetailerWise(
						startDate, endDate));
				session.setAttribute("excelData",
						(List<CSSaleReportBean>) session
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
	}

	public String searchExpand() throws LMSException, ParseException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		boolean isExpand = false;
		try {
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
			if(!isExpand){
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			session.setAttribute("reportList", null);
			if ("Product Wise".equals(reportType)) {
				session.setAttribute("reportList", fetchReportGameWiseExpand(
						startDate, endDate));
				session.setAttribute("excelData",
						(List<CSSaleReportBean>) session
								.getAttribute("reportList"));
				return "PRODUCT_WISE"; 
			} else if ("Agent Wise Expand".equals(reportType)) {
				session.setAttribute("reportList", fetchReportAgentWiseExpand(
						startDate, endDate));
				return "PRODUCT_WISE";
			}
			 else if ("Retailer Wise Expand".equals(reportType)) {
					session.setAttribute("reportList", fetchReportRetailerWiseExpand(
							startDate, endDate));
					return "PRODUCT_WISE";
			}
			}else{
				return "PRODUCT_WISE";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Date Format Error");
		}
		return ERROR;
	}
	public String searchProductRetailerWise() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		int retailerOrgId = 0;
		UserInfoBean userBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			int gameId=0;
			long lastPrintedTicket=0;
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			
			String actionName=ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
			try{
				long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName());
				if(LSTktNo !=0){
					lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
					gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				}
				drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
			}catch(Exception e){
				//e.printStackTrace();
			}
			
			retailerOrgId = userBean.getUserOrgId();
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			session.setAttribute("reportList", null);
			session.setAttribute("reportList", getReportRetailerWise(startDate, endDate,retailerOrgId));
				return SUCCESS;
			}
		 catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Date Format Error");
		}
	}
	
	

	
	/*public String searchByAgent() throws LMSException {
		logger.info("--CS Sale Report Search ----" + reportType + "---");
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
				session.setAttribute("reportList", fetchReportGameWiseForAgent(startDate, endDate));
				session.setAttribute("excelData",
						(List<CSSaleReportBean>) session
								.getAttribute("reportList"));
				return "GAME_WISE";
			} else if ("Retailer Wise".equals(reportType)) {
				session.setAttribute("reportList", fetchReportRetailerWise(
						startDate, endDate));
				session.setAttribute("excelData",
						(List<CSSaleReportBean>) session
								.getAttribute("reportList"));
				return "RETAILER_WISE";
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
		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			session.setAttribute("reportList", null);
			if ("Game Wise Expand".equals(reportType)) {
				session.setAttribute("reportList", fetchReportGameWiseExpandForAgent(startDate, endDate));
				return SUCCESS;
			} else if ("Retailer Wise Expand".equals(reportType)) {
				session.setAttribute("reportList", fetchReportRetailerWiseExpand(
						startDate, endDate));
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Date Format Error");
		}
		return ERROR;
	}
	
	public void exportExcel() {
		HttpSession session = request.getSession();
		List<CSSaleReportBean> data = new ArrayList<CSSaleReportBean>();
		List<CSSaleReportBean> dataExpended = new ArrayList<CSSaleReportBean>();
		ServletContext sc = session.getServletContext();
		CSSaleReportHelper helper = new CSSaleReportHelper();
		String dateFormat = (String) sc.getAttribute("date_format");
		data = (ArrayList) session.getAttribute("excelData");

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=CSSaleReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			Timestamp startDate = null;
			Timestamp endDate = null;
			try {
				startDate = new Timestamp((new SimpleDateFormat(dateFormat))
						.parse(start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat))
						.parse(end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				WriteExcelForDrawSaleReport excel = new WriteExcelForCSSaleReport(
						startDate, endDate, reportType);
				if ("Game Wise".equalsIgnoreCase(reportType)) {
					dataExpended = fetchReportProductWiseExpand(startDate, endDate);
					excel.writeProductWise(data, dataExpended, w, (String) session
							.getAttribute("orgName"), (String) session
							.getAttribute("orgAdd"), "BO", (String) request
							.getSession().getServletContext().getAttribute(
									"CURRENCY_SYMBOL"));
				}else if("Game Wise Expand".equalsIgCSSaleReportHelper helper = new CSSaleReportHelper();noreCase(reportType)){
					dataExpended = fetchReportProductWiseExpandForAgent(startDate, endDate);
					excel.writeProductWise(data, dataExpended, w, (String) session
							.getAttribute("orgName"), (String) session
							.getAttribute("orgAdd"), "AGENT", (String) request
							.getSession().getServletContext().getAttribute(
									"CURRENCY_SYMBOL"));

				} else if ("Agent Wise".equalsIgnoreCase(reportType) || "Retailer Wise".equalsIgnoreCase(reportType)) {
					Map<Integer, List<String>> addMap = new TreeMap<Integer, List<String>>();
					addMap = helper.fetchOrgAddMap(reportType.split(" ")[0].toUpperCase(),agentOrgId);
					Iterator<Map.Entry<Integer, List<String>>> it = addMap
							.entrySet().iterator();
					Map<Integer, List<CSSaleReportBean>> tempMap = new TreeMap<Integer, List<CSSaleReportBean>>();
					while (it.hasNext()) {
						Map.Entry<Integer, List<String>> pair = it.next();
						setAgentOrgId(pair.getKey());
						if("Agent Wise".equalsIgnoreCase(reportType)){
							tempMap.put(pair.getKey(), fetchReportAgentWiseExpand(startDate, endDate));
						}else if("Retailer Wise".equalsIgnoreCase(reportType)){
							tempMap.put(pair.getKey(), fetchReportRetailerWiseExpand(startDate, endDate));
						}
					}
					excel.writeAgentWise(data, tempMap, w, addMap,
							(String) session.getAttribute("orgName"),
							(String) session.getAttribute("orgAdd"), "BO",
							(String) request.getSession().getServletContext()
									.getAttribute("CURRENCY_SYMBOL"));
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
	}*/

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

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}
	public int getRetOrgId() {
		return retOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	
	
}
