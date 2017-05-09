package com.skilrock.lms.web.reportsMgmt.common;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.AgentWiseRetActivityBean;
import com.skilrock.lms.beans.GameSaleDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;

public class AgentWiseRetailerActivityAction  extends ActionSupport implements ServletRequestAware, ServletResponseAware{

private static final long serialVersionUID = 1L;
public String currDate;
public String startDate;

private HttpServletRequest request;
private HttpServletResponse response;

//public List<AgentWiseRetActivityBean> retActivityList ;
public Map<String, AgentWiseRetActivityBean> retActivityMap;
public List<GameSaleDetailsBean> gameSaleDataListMain;
public String execute () throws LMSException{
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	setCurrDate(CommonMethods.convertDateInGlobalFormat(new java.sql.Date(cal
					.getTimeInMillis()).toString(), "yyyy-mm-dd",
					"dd-MM-yyyy"));
	return SUCCESS;
	
}

	public String fetchRetAcitivtyData() throws LMSException {
		Timestamp start_date = null;
		Timestamp endDate = null;

		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");

		try {
			start_date = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(startDate).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					startDate).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);

			setRetActivityMap(new AgentWiseRetailerActivityHelper()
					.fetchRetAcitivtyData(start_date.toString()));

			OrganizationTerminateReportHelper.getTerminateAgentListForRep(
					start_date, endDate);

			Set<String> agentListSet=getRetActivityMap().keySet();
			agentListSet.removeAll(OrganizationTerminateReportHelper.AgentOrgIdStringTypeList);
			Set<String> key = retActivityMap.keySet();
			if(retActivityMap.size() > 0)
				gameSaleDataListMain = retActivityMap.get(key.toArray()[0]).getGameSaleDataList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public Map<String, AgentWiseRetActivityBean> getRetActivityMap() {
		return retActivityMap;
	}

	public void setRetActivityMap(
			Map<String, AgentWiseRetActivityBean> retActivityMap) {
		this.retActivityMap = retActivityMap;
	}

	public List<GameSaleDetailsBean> getGameSaleDataList() {
		return gameSaleDataListMain;
	}

	public void setGameSaleDataList(List<GameSaleDetailsBean> gameSaleDataListMain) {
		this.gameSaleDataListMain = gameSaleDataListMain;
	}

public String getCurrDate() {
	return currDate;
}

public void setCurrDate(String currDate) {
	this.currDate = currDate;
}

public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}

public HttpServletRequest getRequest() {
	return request;
}

public HttpServletResponse getResponse() {
	return response;
}

@Override
public void setServletResponse(HttpServletResponse response) {
	this.response = response;
	
}
@Override
public void setServletRequest(HttpServletRequest request) {
	this.request = request;
	
}

}
