package com.skilrock.lms.web.roleMgmt.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ServiceDataBean;
import com.skilrock.lms.beans.ServiceInterfaceBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.roleMgmt.common.AgentServiceManagementHelper;

public class AgentServiceManagementAction extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {
	static Log logger = LogFactory.getLog(AgentServiceManagementAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] chkChildOrg;
	private List interfaceList;
	private String[] interfaceStatus;
	private String[] interfaceStatusNew;
	private String[] interfaceStatusPrev;
	private int parentOrgId;
	private HttpServletRequest request;
	private HttpServletResponse response;
	public Map serviceDataMap;
	private int serviceId;
	private String tierLevel;
	private List tierList;
	private String tierValue;

	public String createService() throws LMSException {
		logger.info("in crreate service");
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean) session.getAttribute("USER_INFO");
		logger.debug("id of login person" + user.getUserId() + "tierid"
				+ user.getTierId());

		ServiceDataBean serviceDataBean = AgentServiceManagementHelper
				.getServiceData(user.getTierId());
		setServiceDataMap(serviceDataBean.getServiceDataMap());
		setTierList(serviceDataBean.getTierList());
		return SUCCESS;
	}

	public void fetchChildOrgList() throws LMSException, IOException {
		Map<String, String> childOrg = new TreeMap<String, String>();
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean) session.getAttribute("USER_INFO");

		logger.debug("Parent Org Id :: " + parentOrgId);
		childOrg = AgentServiceManagementHelper.getChildOrg(parentOrgId);
		logger.debug("childOrg :: " + childOrg);
		response.getWriter().write(childOrg.toString());

	}

	public void fetchParentOrgList() throws LMSException, IOException {

		Map<String, String> parentOrg = new TreeMap<String, String>();
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean) session.getAttribute("USER_INFO");

		logger.debug("Tier Level :: " + tierLevel);
		parentOrg = AgentServiceManagementHelper.getParentOrg(tierLevel, user
				.getUserType(), user.getUserOrgId());
		logger.debug("parentOrg :: " + parentOrg);
		response.getWriter().write(parentOrg.toString());
	}

	public int[] getChkChildOrg() {
		return chkChildOrg;
	}

	public String getInterface() throws LMSException, IOException {
		List<ServiceInterfaceBean> interList = new ArrayList<ServiceInterfaceBean>();
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean) session.getAttribute("USER_INFO");

		interList = AgentServiceManagementHelper.getInterfaceList(serviceId,
				tierLevel, user.getUserId());
		setInterfaceList(interList);
		logger.debug("Interface List ====" + getInterfaceList());
		return SUCCESS;
	}

	public List getInterfaceList() {
		return interfaceList;
	}

	public String[] getInterfaceStatus() {
		return interfaceStatus;
	}

	public String[] getInterfaceStatusNew() {
		return interfaceStatusNew;
	}

	public String[] getInterfaceStatusPrev() {
		return interfaceStatusPrev;
	}

	public int getParentOrgId() {
		return parentOrgId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Map getServiceDataMap() {
		return serviceDataMap;
	}

	public int getServiceId() {
		return serviceId;
	}

	public String getTierLevel() {
		return tierLevel;
	}

	public List getTierList() {
		return tierList;
	}

	public String getTierValue() {
		return tierValue;
	}

	public void setChkChildOrg(int[] chkChildOrg) {
		this.chkChildOrg = chkChildOrg;
	}

	public void setInterfaceList(List interfaceList) {
		this.interfaceList = interfaceList;
	}

	public void setInterfaceStatus(String[] interfaceStatus) {
		this.interfaceStatus = interfaceStatus;
	}

	public void setInterfaceStatusNew(String[] interfaceStatusNew) {
		this.interfaceStatusNew = interfaceStatusNew;
	}

	public void setInterfaceStatusPrev(String[] interfaceStatusPrev) {
		this.interfaceStatusPrev = interfaceStatusPrev;
	}

	public void setParentOrgId(int parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public void setServiceDataMap(Map serviceDataMap) {
		this.serviceDataMap = serviceDataMap;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setTierLevel(String tierLevel) {
		this.tierLevel = tierLevel;
	}

	public void setTierList(List tierList) {
		this.tierList = tierList;
	}

	public void setTierValue(String tierValue) {
		this.tierValue = tierValue;
	}

	public String updateService() throws LMSException, IOException {

		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean) session.getAttribute("USER_INFO");

		if (tierLevel.equals("-1")) {
			addActionError("You have not selected anything");
			return ERROR;
		}
		if (interfaceStatus == null) {
			addActionError("No Service status Available");
			return ERROR;
		}

		// PrintWriter out = getResponse().getWriter();
		AgentServiceManagementHelper.updateServiceNew(serviceId, tierLevel,
				interfaceStatusNew, interfaceStatusPrev, tierValue,
				chkChildOrg, user.getUserOrgId());
		logger.info("after call update service");
		return SUCCESS;
	}

}
