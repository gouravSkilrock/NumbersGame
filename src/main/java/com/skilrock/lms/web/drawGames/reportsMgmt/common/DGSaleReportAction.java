package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.LiveReportBean;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGSaleReportsHelper;
import com.skilrock.lms.dge.beans.DGConsolidateDrawBean;
import com.skilrock.lms.dge.beans.DGConsolidateGameDataBean;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.JackpotViewBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DGSaleReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(DGSaleReportAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Calendar c1 = Calendar.getInstance();
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1
				.get(Calendar.DATE), 0, 0, 0);
		logger.debug("Inside Main: " + calendarNew.getTime());
		// System.out.println(calendarNew.getTime());
	}

	private int AgentId;
	List<SaleReportBean> dgSaleDetail;
	private String drawId = null;
	private String end_Date;
	private String filter;
	private String gameName;
	List<JackpotViewBean> jackpotViewList;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private int gameNo;
	private int gameId;

	
	private String totaltime;
	
	private ArrayList<DGConsolidateDrawBean> drawDataBeanList;



	public void ajaxAgentList() throws LMSException, IOException {
/*		Map<Integer, String> map = new TreeMap<Integer, String>();
		DGSaleReportsHelper dgSale = new DGSaleReportsHelper();
		PrintWriter out = getResponse().getWriter();

		map = dgSale.ajaxAgentList();
		logger.debug("agent list ajax" + map);
		System.out.println("agent list ajax" + map);
		String html = "";
		html = "Select "
				+ ((Map<String, String>) ServletActionContext
						.getServletContext().getAttribute("TIER_MAP"))
						.get("AGENT")
				+ ": <select class=\"option\" id=\"agtId\" name=\"AgentId\"><OPTION VALUE=-1>--Please Select--";
		int i = 0;
		for (Object element : map.entrySet()) {
			Map.Entry<Integer, String> mypair = (Map.Entry<Integer, String>) element;
			i++;
			html += "<option class=\"option\" value=\"" + mypair.getKey()
					+ "\">" + mypair.getValue() + "</option>";
		}
		html += "</select>";
		response.setContentType("text/html");
		out.print(html);*/
		

		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		int userOrgId = 0;
		if(uib != null){
			userOrgId = uib.getUserOrgId();
		}
	
		String agtOrgList="";
		try {
			agtOrgList = new AjaxRequestHelper()
					.getOrgIdList(userOrgId,"AGENT");
		} catch (LMSException e) {
			
			e.printStackTrace();
		}

		response.setContentType("text/html");
		out.print(agtOrgList);
		
		

	
		
		
		
		
	}

	/**
	 * Added by Neeraj for Creating Jackpot View Reports
	 * 
	 * @return SUCCESS
	 * @throws LMSException
	 */
	public String createJackpotView() throws LMSException {
		DateBeans dateBean = new DateBeans();
		HttpSession session = getRequest().getSession();
		dateBean = GetDate.getDate(start_date, end_Date);
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DGSaleReportsHelper jackpotView = new DGSaleReportsHelper(userInfoBean,
				dateBean);
		logger.debug("Draw Id:***" + drawId + "******");
		// System.out.println("Draw Id:***"+drawId+"******");
		jackpotViewList = jackpotView
				.fetchDGJackpotViewDetail(drawId, Integer.parseInt(gameName));
		logger.debug("jackpotViewList:***" + jackpotViewList + "******");
		logger.debug("presentDate:***" + session.getAttribute("presentDate")
				+ "******");
		logger.debug("date_format:***" + session.getAttribute("date_format")
				+ "******");
		// System.out.println(jackpotViewList);
		// System.out.println(session.getAttribute("presentDate" ));
		// System.out.println(session.getAttribute("date_format"));
		session.setAttribute("datebean", dateBean);
		session.setAttribute("jackpotViewList", jackpotViewList);

		return SUCCESS;
	}

	public String createReport() throws LMSException, ParseException {
		logger.info("Inside createReport");
		DateBeans dateBean1 = new DateBeans();
		HttpSession session = getRequest().getSession();
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBean1 = GetDate.getDate(start_date, end_Date);
		} else {
			dateBean1 = GetDate.getDate(totaltime);
		}
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DGSaleReportsHelper dgSale = new DGSaleReportsHelper(userInfoBean,
				dateBean1);
		if ("Game Wise".equalsIgnoreCase(filter)) {
			dgSaleDetail = dgSale.fetchDGSaleDetailGameWise();
		}
		if ("Agent Wise".equalsIgnoreCase(filter)) {
			dgSaleDetail = dgSale.fetchDGSaleDetailAgentWise();
		}
		if ("Retailer Wise".equalsIgnoreCase(filter)) {
			dgSaleDetail = dgSale.fetchDGSaleDetailRetailerWise(AgentId);
			if (AgentId == -1) {

			}
		}
		logger.debug("dgSaleDetail :" + dgSaleDetail);
		logger.debug("presentDate :" + session.getAttribute("presentDate"));
		logger.debug("date_format :" + session.getAttribute("date_format"));
		logger.debug("filter :" + filter);
		// System.out.println(dgSaleDetail);
		// System.out.println(session.getAttribute("presentDate" ));
		// System.out.println(session.getAttribute("date_format"));
		// System.out.println(filter);
		session.setAttribute("datebean", dateBean1);
		session.setAttribute("searchResultRet", dgSaleDetail);
		session.setAttribute("orgName", userInfoBean.getOrgName());
		session.setAttribute("orgAdd", ReportUtility.getOrgAdd(userInfoBean
				.getUserOrgId()));
		session.setAttribute("filter", filter);
		return SUCCESS;
	}

	public void exportExcel() {
		HttpSession session = request.getSession();
		List<SaleReportBean> data = new ArrayList<SaleReportBean>();
		data = (ArrayList) request.getSession().getAttribute("searchResultRet");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=DGSaleReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForDGSaleReport excel = new WriteExcelForDGSaleReport(
					(DateBeans) session.getAttribute("datebean"));

			excel.write(data, w, (String) session.getAttribute("orgName"),
					(String) session.getAttribute("orgAdd"), "BO",
					(String) request.getSession().getServletContext()
							.getAttribute("CURRENCY_SYMBOL"), (String) session
							.getAttribute("filter"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public String getLiveReport() throws LMSException {
		DateBeans dateBean = new DateBeans();
		List<LiveReportBean> list = new ArrayList<LiveReportBean>();
		HttpSession session = getRequest().getSession();
		dateBean = GetDate.getDate(start_date, end_Date);
		DGSaleReportsHelper helper = new DGSaleReportsHelper();
		list = helper.getLiveReportData(AgentId, dateBean);
		session.setAttribute("LiveSalePwtReportList", list);
		session.setAttribute("datebean", dateBean);
		return SUCCESS;
	}

	

	public String setData() throws Exception {
		HttpSession session = getRequest().getSession();
		ServletContext sc = ServletActionContext.getServletContext();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date = Calendar.getInstance().getTime();
		String today = format.format(date);
		/*List drawNoNameList = (List) sc.getContext("/DrawGameWeb")
				.getAttribute("gameNameList");*/
		Map<Integer, String> gameMap = (Map)ReportUtility.fetchDrawDataMenu();
		/*List drawNoNameList = new ArrayList();		
		for (Map.Entry<Integer, String> entry : gameMap.entrySet()){
			drawNoNameList.add(entry.getKey()+"-"+entry.getValue());
		}		
		logger.debug("Game List: " + drawNoNameList);
		System.out.println("Game List: " + drawNoNameList);*/
		session.setAttribute("CURR_TIME", today);
		session.setAttribute("GAME_NOS", gameMap);
		return SUCCESS;
	}
	
	public String fetchGameListMenu() {
		HttpSession session = getRequest().getSession();
		
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		try {
			session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
/**
 * 	This Method Fetch Draw Wise Game Data For All Clients 
 * @return
 */
	public String fetchDrawGameConsolidateData(){
		System.out.println(gameNo+end_Date+start_date);
	try{
		if(gameNo>0 && start_date!=null ){
			DrawDataBean drawDataBean = new DrawDataBean();
			drawDataBean.setFromDate(start_date + " 00:00:00");
			drawDataBean.setToDate(end_Date + " 23:59:59");
			drawDataBean.setGameNo(gameNo);
			ServletContext sc = ServletActionContext.getServletContext();
			String raffleTktType = (String) sc.getAttribute("raffle_ticket_type");
			DGSaleReportsHelper  helper = new DGSaleReportsHelper();
			DGConsolidateGameDataBean gameDataBean = null  ;
			gameDataBean =helper.fetchDrawGameConsolidateData(drawDataBean,raffleTktType);
			if(gameDataBean!=null){
					drawDataBeanList=(ArrayList<DGConsolidateDrawBean>)gameDataBean.getDrawDataBeanList();
					//logger.info("Got Draw Game Data "+drawDataBeanList.size());
				return SUCCESS;
			}
		
			
		}else {
			logger.info("Incorrect Inputs");
			addActionMessage("Please Enter Correct Values");
			return ERROR;
		}

		
		
	}catch(Exception e){
		addActionMessage("Some Error In Draw Data ");
		e.printStackTrace();
	}
		
		return ERROR;
	}
	
	public ArrayList<DGConsolidateDrawBean> getDrawDataBeanList() {
		return drawDataBeanList;
	}

	public void setDrawDataBeanList(
			ArrayList<DGConsolidateDrawBean> drawDataBeanList) {
		this.drawDataBeanList = drawDataBeanList;
	}

	public int getAgentId() {
		return AgentId;
	}

	public String getDrawId() {
		return drawId;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getFilter() {
		return filter;
	}

	public String getGameName() {
		return gameName;
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

	public String getTotaltime() {
		return totaltime;
	}

	public void setAgentId(int agentId) {
		AgentId = agentId;
	}

	public void setDrawId(String drawId) {
		this.drawId = drawId;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
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

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}
	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}
	

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	
}