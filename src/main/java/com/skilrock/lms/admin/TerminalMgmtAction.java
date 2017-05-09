package com.skilrock.lms.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.admin.common.TerminalMgmtHelper;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.inventoryMgmt.ConsNNonConsInvHelper;

public class TerminalMgmtAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private String search_type;
	private String[] retName;
	private String version;
	private String modelId;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private Map<String, String> modelMap = null;
	
	
	public String execute() throws LMSException {
		HttpSession session = servletRequest.getSession();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		String cntrlType = "All";
		Map<String, Map<String, String>> invDetailsMap = helper.fetchConsInvNModelSpecDetail("NON_CONS", cntrlType);

		setModelMap(invDetailsMap.get("modelMap"));
		session.setAttribute("BRAND", TerminalMgmtHelper.fetchTerminalBrand());

		return SUCCESS ;
	}
	
	
	
	public String fetchRetList() throws Exception {
		HttpSession session = servletRequest.getSession();
		TerminalMgmtHelper helper = new TerminalMgmtHelper();
		session.setAttribute("VER_LIST", helper.fetchVersionDetails(modelId));
		session.setAttribute("RET_MAP", helper.fetchRetList(search_type, modelId));
		session.setAttribute("DEV_LIST", helper.fetchDeviceList());
		ConsNNonConsInvHelper helper1 = new ConsNNonConsInvHelper();
		String cntrlType = "All";
		Map<String, Map<String, String>> invDetailsMap = helper1.fetchConsInvNModelSpecDetail("NON_CONS", cntrlType);
		session.setAttribute("BRAND", TerminalMgmtHelper.fetchTerminalBrand());
		setModelMap(invDetailsMap.get("modelMap"));
		return SUCCESS;
	}
	
	public String saveTerminalDetails() throws Exception {
		TerminalMgmtHelper helper = new TerminalMgmtHelper();
		String result = helper.saveTerminalDetails(retName,version);
		return SUCCESS;
	}
	
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String searchType) {
		search_type = searchType;
	}
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	public String[] getRetName() {
		return retName;
	}
	public void setRetName(String[] retName) {
		this.retName = retName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, String> getModelMap() {
		return modelMap;
	}

	public void setModelMap(Map<String, String> modelMap) {
		this.modelMap = modelMap;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	
	
}
