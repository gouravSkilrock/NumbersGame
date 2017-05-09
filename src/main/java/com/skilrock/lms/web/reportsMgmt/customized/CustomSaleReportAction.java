package com.skilrock.lms.web.reportsMgmt.customized;





import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.customized.CustomSaleReportHelper;



public class CustomSaleReportAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startDate;
	private String endDate ;
	private int agtOrgId ;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<Integer,CollectionReportOverAllBean> agentDataMap;
	private Map<Integer,String> gameMap;
	public String execute() throws Exception {
		HttpSession session = request.getSession();
		session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
		//session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		return SUCCESS;
	}
	public String fetchSlotSaleData(){
		
		Timestamp start_date = null;
		Timestamp end_date = null;
		try{
			gameMap = new LinkedHashMap<Integer, String>();
			agentDataMap=CustomSaleReportHelper.fetchSlotSaleData(startDate, endDate, agtOrgId,gameMap);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			start_date=new Timestamp(sdf.parse(startDate+" 00:00:00").getTime());
			end_date=new Timestamp(sdf.parse(endDate+" 23:59:59").getTime());
			OrganizationTerminateReportHelper.getTerminateAgentListForRep(start_date, end_date);
			List<Integer> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdIntTypeList;
	        
			Set<Integer> agentListSet=agentDataMap.keySet();
			agentListSet.removeAll(terminateAgentList);
		}
		catch (ParseException e) {
			addActionError(e.getMessage());
		}
		catch (LMSException e) {
			addActionError(e.getErrorMessage());
		}
		
		
		return SUCCESS;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public int getAgtOrgId() {
		return agtOrgId;
	}
	public Map<Integer, CollectionReportOverAllBean> getAgentDataMap() {
		return agentDataMap;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}
	public void setAgentDataMap(
			Map<Integer, CollectionReportOverAllBean> agentDataMap) {
		this.agentDataMap = agentDataMap;
	}
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	public Map<Integer, String> getGameMap() {
		return gameMap;
	}
	public void setGameMap(Map<Integer, String> gameMap) {
		this.gameMap = gameMap;
	}
	
	
	
}
