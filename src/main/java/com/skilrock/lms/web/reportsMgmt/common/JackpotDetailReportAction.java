package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.JackpotDetailReportControllerImpl;
import com.skilrock.lms.dge.beans.RainbowGameJackpotReportBean;

public class JackpotDetailReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public JackpotDetailReportAction() {
		super(JackpotDetailReportAction.class);
	}

	private String startDate;
	private String endDate;
	private RainbowGameJackpotReportBean reportBean;
	private int gameNo; 

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
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

	public RainbowGameJackpotReportBean getReportBean() {
		return reportBean;
	}

	public void setReportBean(RainbowGameJackpotReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public String reportMenu() {
		HttpSession session = request.getSession();
		try {
			session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String reportSearch() {
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startTime = new Timestamp(dateFormat.parse(startDate).getTime());
			Timestamp endTime = new Timestamp(dateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));

			reportBean = JackpotDetailReportControllerImpl.getInstance().fetchJackpotData(startTime, endTime,gameNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	/*public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=PriviledgeModificationReport.xls");
		PrintWriter out = response.getWriter();
		if (tableValue != null) {
			tableValue = tableValue.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write("<table border='1' width='100%' >" + tableValue + "</table>");
		}
	}*/
}