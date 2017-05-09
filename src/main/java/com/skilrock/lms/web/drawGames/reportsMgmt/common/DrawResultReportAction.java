package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DrawResultReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(DGSaleReportAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end_Date;
	private String gameNo;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String drawName;
	private String drawNameCol;
	private String depDate;

	public String getDepDate() {
		return depDate;
	}
	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}
	public String fetchDrawResultData() {
		HttpSession session = getRequest().getSession();
		DrawDataBean drawDataBean = new DrawDataBean();
		drawDataBean.setGameNo(Integer.parseInt(gameNo));
		drawDataBean.setFromDate(start_date + " 00:00:00");
		drawDataBean.setToDate(end_Date + " 23:59:59");
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String)sc.getAttribute("raffle_ticket_type");
		try {
			session.setAttribute("DRAW_DATA_LIST", helper.fetchDrawData(
					drawDataBean,raffleTktType).getRepGameBean().getRepDrawBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String fetchDrawMachineResultData() {
		System.out.println("fetchDrawMachineResultData");
		HttpSession session = getRequest().getSession();
		DrawDataBean drawDataBean = new DrawDataBean();
		drawDataBean.setGameNo(Integer.parseInt(gameNo));
		drawDataBean.setFromDate(start_date + " 00:00:00");
		drawDataBean.setToDate(end_Date + " 23:59:59");
		drawDataBean.setDrawName(drawName);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String)sc.getAttribute("raffle_ticket_type");
		try {
			session.setAttribute("DRAW_DATA_LIST", helper.fetchDrawMachineData(
					drawDataBean,raffleTktType).getRepGameBean().getRepDrawBean());
			session.removeAttribute("DRAW_NAME");
			session.setAttribute("GAME_NAME", Util.getGameDisplayName(Integer.parseInt(gameNo)));
			if(drawName != null){
				drawName = drawName.trim();
				if(!"null".equalsIgnoreCase(drawName) && !"All".equalsIgnoreCase(drawName) && !"".equalsIgnoreCase(drawName)){
					session.setAttribute("DRAW_NAME", drawName);
				} else {
					session.setAttribute("DRAW_NAME", null);
				}
			} else {
				session.setAttribute("DRAW_NAME", null);
			}
			
			if(drawNameCol != null){
				drawNameCol = drawNameCol.trim();
				if(!"null".equalsIgnoreCase(drawNameCol) &&  !"".equalsIgnoreCase(drawNameCol)){
					session.setAttribute("DRAW_NAME_COLUMN", drawNameCol);
				} else {
					session.setAttribute("DRAW_NAME_COLUMN", null);
				}
			} else {
				session.setAttribute("DRAW_NAME_COLUMN", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String fetchDrawRankResultData() {
		HttpSession session = getRequest().getSession();
		DrawDataBean drawDataBean = new DrawDataBean();
		drawDataBean.setGameNo(Integer.parseInt(gameNo));
		drawDataBean.setFromDate(start_date + " 00:00:00");
		drawDataBean.setToDate(end_Date + " 23:59:59");
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String)sc.getAttribute("raffle_ticket_type");
		try {
			session.setAttribute("DRAW_DATA_LIST", helper.fetchDrawData(
					drawDataBean,raffleTktType).getRepGameBean().getRepDrawBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String fetchGameListMenu() {
		HttpSession session = getRequest().getSession();
		ServletContext sc = session.getServletContext();
		
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		
		setDepDate(CommonMethods.convertDateInGlobalFormat((String)sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)sc.getAttribute("date_format")));
		try {
			session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String fetchGameMachineDataMenu() {
		HttpSession session = getRequest().getSession();
		ServletContext sc = session.getServletContext();
		
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30);
		String calDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(cal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
		setDepDate(calDate);
//		setDepDate(CommonMethods.convertDateInGlobalFormat((String)sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)sc.getAttribute("date_format")));
		try {
			session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}


	public String getEnd_Date() {
		return end_Date;
	}

	public String getGameNo() {
		return gameNo;
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

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
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

	public String weeklyReport() {
		System.out.println("inside weekly report method");
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			HttpSession session = getRequest().getSession();
			Map m = new DrawGameMgmtHelper().weeklyReport(reportStatusBean);
			session.setAttribute("RETAILER_COUNT", m.get("RETAILER"));
			session.setAttribute("WEEK_REPORT", m.get("REPORT"));
		} else
			return "RESULT_TIMING_RESTRICTION";

		return SUCCESS;
	}
	public String getDrawName() {
		return drawName;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	public String getDrawNameCol() {
		return drawNameCol;
	}
	public void setDrawNameCol(String drawNameCol) {
		this.drawNameCol = drawNameCol;
	}
}
