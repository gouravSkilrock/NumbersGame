package com.skilrock.lms.web.reportsMgmt.common;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.IncentiveReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.CommonValidation;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;


public class IncentiveSchemeAnalysisReportAction extends ActionSupport implements ServletRequestAware , ServletResponseAware{

	final static long oneDay = 1 * 24 * 60 * 60 * 1000;
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String startDate;
	private String endDate;
	Timestamp stDate;
	Timestamp enDate;
	private String gameList; 
	private String agentList;
	private String list;
	private String valueToSend;
	private String grtrThnAmt;
	private String lssThnAmt;
	private String orgName;
	private String orgAdd;
	private String message;
	private Map<Integer , String> gameMap;
	private Map<String, String> agentMap;
	private Map<Integer, IncentiveReportBean> incentiveReport;
	private Map<String, String> gameNameList;

	
	
	public String onMenuLoad() throws LMSException{
		HttpSession session =request.getSession();
		session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
		this.setGameMap(ReportUtility.fetchDrawDataMenu());
		this.setAgentMap(getAgtMap());
		return SUCCESS;
		
	}
	
	private TreeMap<String, String> getAgtMap() throws LMSException{
		AjaxRequestHelper helper=new AjaxRequestHelper();
		TreeMap<String, String> map=new TreeMap<String, String>();
		Iterator<String> it=Arrays.asList(helper.getOrgListForAll("AGENT").split(":")).iterator();
		while(it.hasNext()){
			String orgIdCode=it.next();
			int indexOfSpclChar=orgIdCode.indexOf('|');
			map.put(orgIdCode.substring(indexOfSpclChar+1,orgIdCode.length()).trim().toUpperCase(),orgIdCode.substring(0,indexOfSpclChar).trim());
		}	
		return map;
	}

	public String incentiveSchemeAnalysisReport() throws ParseException, LMSException{
		Enumeration<String> param = request.getParameterNames() ;
		String paramName = "" ;
		while(param.hasMoreElements())
		{
			paramName = param.nextElement(); 
			if(paramName.contains("gameList") && !(paramName.contains("checkbox"))){
				gameList += (String)request.getParameter(paramName) + "," ;
			}
		}
		gameList = gameList.replaceAll("false", "") ;
		System.out.println("Game List : " + gameList);
		
		HttpSession session =request.getSession();
		this.message=null;
		boolean isArchTablesReq=false;
		try {
			validateInputs(startDate,endDate,grtrThnAmt,lssThnAmt);
			if(startDate.compareTo(CommonMethods.getLastArchDate())<=0)
			isArchTablesReq=true;
		
		stDate = new Timestamp(sdf.parse(startDate).getTime());
		enDate = new Timestamp(sdf.parse(endDate).getTime()+oneDay-1000);
		
		IncentiveSchemeAnalysisReportHelper helper=new IncentiveSchemeAnalysisReportHelper();
		incentiveReport=helper.fetchIncentiveSchemeAnalysisReport(stDate,enDate,gameList,list,grtrThnAmt,lssThnAmt,isArchTablesReq);
		OrganizationTerminateReportHelper.getTerminateAgentListForRep(stDate, enDate);
		List<Integer> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdIntTypeList;
        Set<Integer> agentListSet=incentiveReport.keySet();
		agentListSet.removeAll(terminateAgentList);
		this.setIncentiveReport(incentiveReport);
		this.setGameNameList( helper.allGameMap(gameList,stDate));
		this.setOrgName(((UserInfoBean) session.getAttribute("USER_INFO")).getOrgName());
		this.setOrgAdd( helper.getOrgAdd(((UserInfoBean) session.getAttribute("USER_INFO")).getUserOrgId()));
		DateBeans dBean = new DateBeans();
		dBean.setStartDate(stDate);
		dBean.setEndDate(enDate);
		dBean.setReportType("");
		session.setAttribute("datebean", dBean);
		}
		catch (LMSException e) {
			message=e.getErrorMessage();
	   }catch (Exception e) {
		   message=e.getMessage();
	}
		return SUCCESS;
		
	}
	
	
	
	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=IncentiveSchemeAnalysisReport.xls");
		PrintWriter out = response.getWriter();
		if (valueToSend != null) {
			valueToSend = valueToSend.replaceAll("<tbody>", "").replaceAll(
					"</tbody>", "").trim();
			//out.write("<table border='1' width='100%' >" + valueToSend
				//	+ "</table>");
			out.write(valueToSend);
		}
	}
	
	
	private void validateInputs(String startDate, String endDate, String grtrThnAmt, String lssThnAmt) throws ParseException, LMSException{
		
		if(startDate==null || startDate.isEmpty() || endDate==null || endDate.isEmpty())
			throw new LMSException(LMSErrors.INVALID_DATE_INPUT_RANGE_ERROR_CODE,LMSErrors.INVALID_DATE_INPUT_RANGE_ERROR_MESSAGE);
		
		if((new Timestamp(sdf.parse(startDate).getTime())).after((new Timestamp(sdf.parse(endDate).getTime()))))
			throw new LMSException(LMSErrors.INVALID_DATE_INPUT_RANGE_ERROR_CODE,LMSErrors.INVALID_DATE_INPUT_RANGE_ERROR_MESSAGE);
		
		if(!CommonValidation.isEmpty(grtrThnAmt))
			if(!CommonValidation.isValidAmount(grtrThnAmt))
				throw new LMSException(LMSErrors.INVALID_INPUT_RANGE_ERROR_CODE,LMSErrors.INVALID_INPUT_RANGE_ERROR_MESSAGE);
		
		if(!CommonValidation.isEmpty(lssThnAmt))
			if(!CommonValidation.isValidAmount(lssThnAmt))
				throw new LMSException(LMSErrors.INVALID_INPUT_RANGE_ERROR_CODE,LMSErrors.INVALID_INPUT_RANGE_ERROR_MESSAGE);
		
		if(!CommonValidation.isEmpty(grtrThnAmt) && !CommonValidation.isEmpty(lssThnAmt))
			if(Double.parseDouble(grtrThnAmt)>Double.parseDouble(lssThnAmt))
				throw new LMSException(LMSErrors.INVALID_INPUT_RANGE_ERROR_CODE,LMSErrors.INVALID_INPUT_RANGE_ERROR_MESSAGE);
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}

	public String getGameList() {
		return gameList;
	}

	public void setGameList(String gameList) {
		this.gameList = gameList;
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

	public String getGrtrThnAmt() {
		return grtrThnAmt;
	}

	public void setGrtrThnAmt(String grtrThnAmt) {
		this.grtrThnAmt = grtrThnAmt;
	}

	public String getLssThnAmt() {
		return lssThnAmt;
	}

	public void setLssThnAmt(String lssThnAmt) {
		this.lssThnAmt = lssThnAmt;
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

	public String getAgentList() {
		return agentList;
	}

	public void setAgentList(String agentList) {
		this.agentList = agentList;
	}

	public String getValueToSend() {
		return valueToSend;
	}

	public void setValueToSend(String valueToSend) {
		this.valueToSend = valueToSend;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgAdd() {
		return orgAdd;
	}

	public void setOrgAdd(String orgAdd) {
		this.orgAdd = orgAdd;
	}

	public Map<Integer, String> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, String> gameMap) {
		this.gameMap = gameMap;
	}

	public Map<String, String> getAgentMap() {
		return agentMap;
	}

	public void setAgentMap(Map<String, String> agentMap) {
		this.agentMap = agentMap;
	}

	public Map<Integer, IncentiveReportBean> getIncentiveReport() {
		return incentiveReport;
	}

	public void setIncentiveReport(Map<Integer, IncentiveReportBean> incentiveReport) {
		this.incentiveReport = incentiveReport;
	}

	public Map<String, String> getGameNameList() {
		return gameNameList;
	}

	public void setGameNameList(Map<String, String> gameNameList) {
		this.gameNameList = gameNameList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
