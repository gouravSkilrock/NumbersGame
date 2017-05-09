package com.skilrock.lms.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;

	protected Log logger;
	protected HttpServletRequest request;
	protected HttpServletResponse response;


	@SuppressWarnings("unchecked")
	public BaseAction(Class clazz) {
		logger = LogFactory.getLog(clazz);
	}

	public BaseAction(String className) {
		logger = LogFactory.getLog(className);
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	@SuppressWarnings("unchecked")
	public HttpSession getSession(String userName) throws LMSException {
		//ServletContext sc = ServletActionContext.getServletContext();
		try{
			Map currentUserSessionMap = (Map) LMSUtility.sc.getAttribute("LOGGED_IN_USERS");
			return (HttpSession) currentUserSessionMap.get(userName);
		} catch(Exception e){
			throw new LMSException(SLEErrors.SESSION_TIME_OUT_ERROR_CODE , SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
		}
	}

	public UserInfoBean getUserBean() throws LMSException {
		HttpSession session = getSession();
		 if(session != null) {
	            return (UserInfoBean) session.getAttribute("USER_INFO");
	     } else {
	            throw new LMSException(SLEErrors.SESSION_TIME_OUT_ERROR_CODE , SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
	     }
	}

	public UserInfoBean getUserBean(String userName) throws LMSException {
        HttpSession session = getSession(userName);
        if(session != null) {
            return (UserInfoBean) session.getAttribute("USER_INFO");
        } else {
            throw new LMSException(SLEErrors.SESSION_TIME_OUT_ERROR_CODE , SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
        }
    }

	public int getAuditTrailID() {
		return ((Integer)request.getAttribute("AUDIT_ID")).intValue();
	}
}