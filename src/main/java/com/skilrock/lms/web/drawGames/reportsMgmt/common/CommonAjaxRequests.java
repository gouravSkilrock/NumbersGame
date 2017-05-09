package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGNumberAnalysisReportHelper;
import com.skilrock.lms.dge.beans.DrawDataBean;

public class CommonAjaxRequests extends ActionSupport implements ServletRequestAware, ServletResponseAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameNo;
	private String startDate;
	private String endDate;
	private String drawName;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	
	public void fetchDrawName() throws IOException{
		PrintWriter pw = null;
		pw = response.getWriter();
		DGNumberAnalysisReportHelper helper = new DGNumberAnalysisReportHelper();
		DrawDataBean drawBean = new DrawDataBean();
		drawBean.setGameNo(Integer.parseInt(gameNo));
		drawBean.setFromDate(startDate+" 00:00:00");
		drawBean.setToDate((endDate!=null && !"null".equalsIgnoreCase(endDate))?endDate+" 23:59:59":startDate+" 23:59:59");
		String res =  helper.fetchDrawName(drawBean);
		pw.print(res);
	}
	
	
	
	
	
	
	public String getGameNo() {
		return gameNo;
	}

	public void setGameNo(String gameNo) {
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

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}

	
}
