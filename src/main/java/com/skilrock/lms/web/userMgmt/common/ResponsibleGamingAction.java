package com.skilrock.lms.web.userMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.web.drawGames.common.Util;

public class ResponsibleGamingAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	String criAction[];
	String criLimit[];
	int criteriaId[];
	String criteriaType;
	Log logger = LogFactory.getLog(ResponsibleGamingAction.class);
	String orgType[];
	private HttpServletRequest request;
	private HttpServletResponse response;
	HttpSession session = null;
	String status[];

	public String fetchRGEnumList() {
		logger.debug("*******fetchRGCriteriaType*******");
		session = getRequest().getSession();
		ResponsibleGaming helper = new ResponsibleGaming();
		List mainList = helper.fetchRGEnumList();
		session.setAttribute("criTypeList", mainList.get(0));
		session.setAttribute("criStatusList", mainList.get(1));
		session.setAttribute("orgList", mainList.get(2));
		session.setAttribute("criActionList", mainList.get(3));
		return SUCCESS;
	}

	public String fetchRGLimits() {
		logger.debug("*******fetchRGLimits*******");
		session = getRequest().getSession();
		ResponsibleGaming helper = new ResponsibleGaming();
		List criteriaList = helper.fetchRGLimits(getCriteriaType());

		session.setAttribute("criteriaList", criteriaList);
		return SUCCESS;
	}

	public String[] getCriAction() {
		return criAction;
	}

	public String[] getCriLimit() {
		return criLimit;
	}

	public int[] getCriteriaId() {
		return criteriaId;
	}

	public String getCriteriaType() {
		return criteriaType;
	}

	public String[] getOrgType() {
		return orgType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return session;
	}

	public String[] getStatus() {
		return status;
	}

	public String saveRGLimits() {
		session = getRequest().getSession();
		ResponsibleGaming helper = new ResponsibleGaming();
		logger.debug("*****saveRGLimits");
		logger.debug("********criteriaId" + criteriaId.length);
		logger.debug("********criLimit" + criLimit.length);
		logger.debug("********criAction" + criAction.length);
		logger.debug("********orgType" + orgType.length);
		logger.debug("********status" + status.length);
		StringBuilder returnStr = helper.saveRGLimits(getCriteriaId(),
				getCriLimit(), getCriAction(), getStatus(), getCriteriaType());
		addActionMessage(returnStr.toString());
		Util.setRespGamingCriteriaStatusMap();
		/*
		 * if (helper.saveRGLimits(getCriteriaId(), getCriLimit(),
		 * getCriAction(), getStatus(), getCriteriaType())) return SUCCESS;
		 */
		return SUCCESS;
	}

	public void setCriAction(String[] criAction) {
		this.criAction = criAction;
	}

	public void setCriLimit(String[] criLimit) {
		this.criLimit = criLimit;
	}

	public void setCriteriaId(int[] criteriaId) {
		this.criteriaId = criteriaId;
	}

	public void setCriteriaType(String criteriaType) {
		this.criteriaType = criteriaType;
	}

	public void setOrgType(String[] orgType) {
		this.orgType = orgType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

}
