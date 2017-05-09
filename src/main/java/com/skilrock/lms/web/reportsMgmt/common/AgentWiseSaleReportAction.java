package com.skilrock.lms.web.reportsMgmt.common;

import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.AgentWiseSaleReportHelper;
import com.skilrock.lms.beans.AgentWiseSaleBean;
public class AgentWiseSaleReportAction  extends BaseAction {

	private static final long serialVersionUID = 1L;
	public AgentWiseSaleReportAction() {
			super(AgentWiseSaleReportAction.class);
		}
	
	private String startDate;
	private String endDate;
	private Map<Integer,AgentWiseSaleBean> agentWiseSaleMap;
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

	public Map<Integer, AgentWiseSaleBean> getAgentWiseSaleMap() {
		return agentWiseSaleMap;
	}
	public void setAgentWiseSaleMap(Map<Integer, AgentWiseSaleBean> agentWiseSaleMap) {
		this.agentWiseSaleMap = agentWiseSaleMap;
	}
	public String fetchReportData(){
		try{
			agentWiseSaleMap=new HashMap<Integer, AgentWiseSaleBean>();
			AgentWiseSaleReportHelper helper=new AgentWiseSaleReportHelper();
			helper.fetchReportData(startDate+" 00:00:00",endDate+" 23:59:59",agentWiseSaleMap);
		}catch (LMSException el) {
			el.printStackTrace();
			request.setAttribute("LMS_EXCEPTION", getText("error.some.internal.server.error"));
			return "applicationLMSAjaxException";
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("LMS_EXCEPTION", getText("error.some.internal.server.error"));
			return "applicationLMSAjaxException";
		}
		return SUCCESS;
	}
}
