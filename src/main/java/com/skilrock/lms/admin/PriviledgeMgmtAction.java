package com.skilrock.lms.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.admin.common.PriviledgeMgmtHelper;
import com.skilrock.lms.common.exception.LMSException;

public class PriviledgeMgmtAction extends ActionSupport implements
		ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	String privTlbName;
	int[] privId;
	int[] actionId;
	String[] manuDispName;
	String[] status;
	
	@Override
	public String execute() throws Exception {
		PriviledgeMgmtHelper privMgmtHelper = new PriviledgeMgmtHelper();
		HttpSession session = getRequest().getSession();
		session.setAttribute("PRIV_TLB_MAP", privMgmtHelper.fetchTableName());
		return SUCCESS;
	}
	
	public String fetchPriviledge() throws LMSException {
		PriviledgeMgmtHelper privMgmtHelper = new PriviledgeMgmtHelper();
		HttpSession session = getRequest().getSession();
		session.setAttribute("PRIV_LIST", privMgmtHelper.fetchPriviledge(privTlbName));
		return SUCCESS;
	}
	
	public String savePriviledge() throws LMSException {
		PriviledgeMgmtHelper privMgmtHelper = new PriviledgeMgmtHelper();
		privMgmtHelper.savePriviledge(privId, manuDispName, status,privTlbName,actionId);
		return SUCCESS;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getPrivTlbName() {
		return privTlbName;
	}
	public void setPrivTlbName(String privTlbName) {
		this.privTlbName = privTlbName;
	}

	public int[] getPrivId() {
		return privId;
	}

	public void setPrivId(int[] privId) {
		this.privId = privId;
	}

	public String[] getManuDispName() {
		return manuDispName;
	}

	public void setManuDispName(String[] manuDispName) {
		this.manuDispName = manuDispName;
	}

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public int[] getActionId() {
		return actionId;
	}

	public void setActionId(int[] actionId) {
		this.actionId = actionId;
	}
	
}
