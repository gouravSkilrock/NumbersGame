package com.skilrock.lms.web.userMgmt.common;

import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentPayOutCenterHelper;


public class AgentPayOutCenterAction extends ActionSupport implements
		ServletRequestAware {
	Log logger = LogFactory.getLog(AgentPayOutCenterAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private HttpServletRequest request;
	private List<OrgPwtLimitBean> orgPwtLimitBeanList=null;
	private String orgPwtLimitResponse;
	public String payOutCenterMenu()throws Exception{
		
		try{
		AgentPayOutCenterHelper payOutHelper=new AgentPayOutCenterHelper();
		orgPwtLimitBeanList=payOutHelper.getAgentPwtLimitBeanList();
		} catch (LMSException le) {
			logger.info("RESPONSE_PAY_OUT_CENTER_MENU-: ErrorCode:"+le.getErrorCode()+" ErrorMessage:"+le.getErrorMessage());
        	request.setAttribute("LMS_EXCEPTION",le.getErrorMessage());
    		return "applicationException";
		} catch (Exception e) {
			logger.error("Exception",e);
			logger.info("RESPONSE_PAY_OUT_CENTER_MENU-: ErrorCode:"+LMSErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("LMS_EXCEPTION",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	    	return "applicationException";
		}
		return SUCCESS;
	}

	public String payOutCenterSubmit()throws Exception{
		logger.info("REQUEST_PAY_OUT_CENTER_SUBMIT-"+request.getAttribute("AUDIT_ID")+":"+orgPwtLimitResponse);
		HttpSession session = null;
		UserInfoBean userBean = null;
		try{
		session = getRequest().getSession();
		
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		AgentPayOutCenterHelper payOutHelper = new AgentPayOutCenterHelper();
		Type type = new TypeToken<List<OrgPwtLimitBean>>() {
		}.getType();
		List<OrgPwtLimitBean> orgPwtLimitList = new Gson().fromJson(orgPwtLimitResponse, type);
		orgPwtLimitBeanList=payOutHelper.updateOrgnizationLimit(orgPwtLimitList,userBean.getUserId());
		} catch (LMSException le) {
			logger.info("REQUEST_PAY_OUT_CENTER_SUBMIT-: ErrorCode:"+le.getErrorCode()+" ErrorMessage:"+le.getErrorMessage());
        	request.setAttribute("LMS_EXCEPTION",le.getErrorMessage());
    		return "applicationException";
		} catch (Exception e) {
			logger.error("Exception",e);
			logger.info("REQUEST_PAY_OUT_CENTER_SUBMIT-: ErrorCode:"+LMSErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("LMS_EXCEPTION",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	    	return "applicationException";
		}
		logger.info("REQUEST_PAY_OUT_CENTER_SUBMIT-"+request.getAttribute("AUDIT_ID")+":SUCCESSFULLY UPDATED");
		return SUCCESS;
	}
	
	

	public void setServletRequest(HttpServletRequest request) {
		this.setRequest(request);
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}



	public void setOrgPwtLimitBeanList(List<OrgPwtLimitBean> orgPwtLimitBeanList) {
		this.orgPwtLimitBeanList = orgPwtLimitBeanList;
	}



	public List<OrgPwtLimitBean> getOrgPwtLimitBeanList() {
		return orgPwtLimitBeanList;
	}

	public void setOrgPwtLimitResponse(String orgPwtLimitResponse) {
		this.orgPwtLimitResponse = orgPwtLimitResponse;
	}

	public String getOrgPwtLimitResponse() {
		return orgPwtLimitResponse;
	}


}