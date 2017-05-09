package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.dge.beans.DrawPanelSaleBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.drawMgmt.DrawGameMgmt;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DrawMgmtReport extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end_Date;
	private String gameNo;
	Log logger = LogFactory.getLog(DrawGameMgmt.class);
	private HttpServletResponse response;
	private HttpServletRequest servletRequest;
	private String start_date;
	private String lastPurgDate;
	private HashMap<Integer, String> gameDetails;
	private String presentDate;
	private List<DrawPanelSaleBean> drawPanelWiseSaleList;
	private String displaySd;
	private String displayEd;
	private String reportType;
	private int gameId;
	private String gameName;
	private String orgName;
	private String orgAddress;
	private String reportData;
	private String reportName;
	

	public void exportExcel() {
		HttpSession session = servletRequest.getSession();
		List<DrawPanelSaleBean> data = new ArrayList<DrawPanelSaleBean>();
		data = (List<DrawPanelSaleBean>) servletRequest.getSession()
				.getAttribute("DrawPanelWiseSaleList");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=PanelWiseSale_Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForPanelWiseSaleReport excel = new WriteExcelForPanelWiseSaleReport(
					(DateBeans) session.getAttribute("datebean"));
			excel.write(data, w, (String) session.getAttribute("orgName"),
					(String) session.getAttribute("orgAdd"), "BO", session
							.getServletContext()
							.getAttribute("CURRENCY_SYMBOL")
							+ "", Integer.parseInt((String) servletRequest
							.getSession().getAttribute("gameId")));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDrawPanelWiseSale() throws ParseException, LMSException {
		int gameId;
		 // ADDED ONE MORE gameNo coz if the Column width exceeds some limit it starts to give some error after exporting the report to EXCEL
		String gameNo = this.gameNo.split(",")[0].trim();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dataList = new ArrayList<String>();
		dataList.add(gameNo);
		dataList.add(start_date + " 00:00:00");
		dataList.add(end_Date + " 23:59:59");
		setLastPurgDate(null);
		DrawGameMgmtHelper dgmHelp = new DrawGameMgmtHelper();
		List<DrawPanelSaleBean> list = dgmHelp.DrawMgmtReport(dataList);
		HttpSession session = servletRequest.getSession();
		if(list!=null && list.size()>0 && list.get(0).isPurg() == true) 
						setLastPurgDate(list.get(0).getPurgLastDate());

		setDrawPanelWiseSaleList(list);
		gameId = Integer.parseInt(gameNo);
		setGameId(gameId);
		setGameNo(gameNo);
		setReportType("Date Wise");
		setGameName(Util.getGameName(gameId));
		setDisplaySd(GetDate.getConvertedDate(new Date(sdf.parse(start_date).getTime())));
		setDisplayEd(GetDate.getConvertedDate(new Date(sdf.parse(end_Date).getTime())));
		setOrgName(((UserInfoBean) session.getAttribute("USER_INFO")).getOrgName());
		setOrgAddress(ReportUtility.getOrgAdd(((UserInfoBean) session.getAttribute("USER_INFO")).getUserOrgId()));
		return SUCCESS;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getGameNo() {
		return gameNo;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public String getStart_date() {
		return start_date;
	}

	public HashMap<Integer, String> getGameDetails() {
		return gameDetails;
	}

	public void setGameDetails(HashMap<Integer, String> gameDetails) {
		this.gameDetails = gameDetails;
	}

	public String getPresentDate() {
		return presentDate;
	}

	public void setPresentDate(String presentDate) {
		this.presentDate = presentDate;
	}

	public String menuLanding() throws Exception {
		//HttpSession session = servletRequest.getSession();
		//session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
		//session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		setPresentDate( new java.sql.Date(new Date().getTime()).toString());
		setGameDetails(ReportUtility.fetchDrawDataMenu());
		return SUCCESS;
	}
	
	public void fetchDrawName() throws IOException{
		PrintWriter out = getResponse().getWriter();
		List<String> drawNameList = Util.drawNameListMap.get(Integer.parseInt(gameNo));
		if(drawNameList != null){
			drawNameList.remove(null);
			drawNameList.remove("null");
			response.setContentType("text/html");
			out.print(drawNameList.toString().replace("[", "").replace("]", "").replace(", ", ","));
		}
	}
	
	public void getDrawTime() throws IOException
	{
		PrintWriter pw = null;
		pw = getResponse().getWriter();
		DrawGameMgmtHelper dgmHelp = new DrawGameMgmtHelper();
		pw.write(dgmHelp.drawTime(gameNo));
			 
	}	

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getLastPurgDate() {
		return lastPurgDate;
	}

	public void setLastPurgDate(String lastPurgDate) {
		this.lastPurgDate = lastPurgDate;
	}

	public List<DrawPanelSaleBean> getDrawPanelWiseSaleList() {
		return drawPanelWiseSaleList;
	}

	public void setDrawPanelWiseSaleList(
			List<DrawPanelSaleBean> drawPanelWiseSaleList) {
		this.drawPanelWiseSaleList = drawPanelWiseSaleList;
	}

	public String getDisplaySd() {
		return displaySd;
	}

	public void setDisplaySd(String displaySd) {
		this.displaySd = displaySd;
	}

	public String getDisplayEd() {
		return displayEd;
	}

	public void setDisplayEd(String displayEd) {
		this.displayEd = displayEd;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	
}
