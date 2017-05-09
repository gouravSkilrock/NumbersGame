package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGFullReportHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGFullReportHelperSP;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.IDGFullReportHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DGFullReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(DGFullReportAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int AgentId;
	private String drawId = null;
	private String end_Date;
	private String filter;
	private String gameName;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private Map<String, String> stateMap;
	private String stateCode;
	private String cityCode;
	
	public String execute(){
		stateMap = CommonMethods.fetchStateList();
		
		return SUCCESS ;
		
	}

	public void exportExcel() {

		HttpSession session = request.getSession();
		Map<String, String> data = new LinkedHashMap<String, String>();
		data = (LinkedHashMap) request.getSession().getAttribute("searchResultRet");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=DrawSaleAndPWT_Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForDGFullReport excel = new WriteExcelForDGFullReport(
					(DateBeans) session.getAttribute("datebean"));

			excel.write(data, w, (String) session.getAttribute("orgName"),
					(String) session.getAttribute("orgAdd"),
					(Map<Integer, String>) session
							.getAttribute("ActiveGameNumMap"),(Map<Integer, String>) session
							.getAttribute("IWGameMap"), "BO");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

	public Map<String, String> getStateMap() {
		return stateMap;
	}



	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
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

	public String getFullReport() throws LMSException {
		logger.info("Inside createReport");
		DateBeans dateBean1 = new DateBeans();
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String orgName = userInfoBean.getOrgName();
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBean1 = GetDate.getDate(start_date, end_Date);
		} else {
			dateBean1 = GetDate.getDate(totaltime);
		}
		
		if(stateCode == null)
			stateCode = "ALL";
		if(cityCode == null)
			cityCode = "ALL";
		
		IDGFullReportHelper dgHelper = null;
		if (LMSFilterDispatcher.isRepFrmSP) {
			dgHelper=new DGFullReportHelperSP(dateBean1);
		} else {
			dgHelper=new DGFullReportHelper(dateBean1);
		}
		String orgAdd = ReportUtility.getOrgAdd(userInfoBean.getUserOrgId());
		session.setAttribute("datebean", dateBean1);
		session.setAttribute("orgName", orgName);
		session.setAttribute("orgAdd", orgAdd);
		session
				.setAttribute("ActiveGameNumMap", ReportUtility
						.getActiveGameNumMap(dateBean1.getFirstdate().toString()));
		session
		.setAttribute("IWGameMap", ReportUtility
				.getIWGameMap(new Timestamp(dateBean1.getFirstdate().getTime())));
		
		session.setAttribute("isIW", ReportUtility.isIW) ;
		
		session.setAttribute("searchResultRet", dgHelper.fetchDGFullReport(cityCode, stateCode));
		return SUCCESS;
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

}
