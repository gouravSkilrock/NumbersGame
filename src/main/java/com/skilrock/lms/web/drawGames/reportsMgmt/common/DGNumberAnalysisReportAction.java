package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGNumberAnalysisReportHelper;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DGNumberAnalysisReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String gameNo;
	private String dateOfDraw;
	private String drawName;
	private String message;
	private String archDate;
	
	
	public String getArchDate() {
		return archDate;
	}
	public void setArchDate(String archDate) {
		this.archDate = archDate;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getDateOfDraw() {
		return dateOfDraw;
	}
	public void setDateOfDraw(String dateOfDraw) {
		this.dateOfDraw = dateOfDraw;
	}
	public String getDrawName() {
		return drawName;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	
	public String fetchNumberData(){
		HttpSession session = getRequest().getSession();
		if (dateOfDraw != null) {
			String lastDate = CommonMethods.getLastArchDate();
			System.out.println("last archieve date"+lastDate);
			if(dateOfDraw.compareTo(lastDate)<=0){
				message="For Details Please Choose start date after "+lastDate;
				return SUCCESS;
			}
		}
		DGNumberAnalysisReportHelper helper = new DGNumberAnalysisReportHelper();
		DrawDataBean drawBean = new DrawDataBean();
		drawBean.setGameNo(Integer.parseInt(gameNo));
		drawBean.setDrawName(drawName);
		drawBean.setDrawTime(dateOfDraw);
		LinkedHashMap<String, ArrayList<String>> numberMap = helper.getNumberData(drawBean);
		session.setAttribute("numberMap", numberMap);
		return SUCCESS;
	}
	
	public String menuLanding() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date strtDate = format.parse(CommonMethods.getLastArchDate());
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(strtDate);
		cal1.add(Calendar.DATE, 1); 
		setArchDate(format.format(cal1.getTime()));
		return SUCCESS;
	}
	
	public void fetchDrawName() throws IOException{
		PrintWriter pw = null;
		pw = response.getWriter();
		DGNumberAnalysisReportHelper helper = new DGNumberAnalysisReportHelper();
		DrawDataBean drawBean = new DrawDataBean();
		drawBean.setGameNo(Integer.parseInt(gameNo));
		drawBean.setDrawTime(dateOfDraw);
		String res =  helper.fetchDrawName(drawBean);
		pw.print(res);
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	
}
