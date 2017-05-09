package com.skilrock.lms.web.accMgmt.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PostSalePwtCommissionVarianceBean;
import com.skilrock.lms.beans.UserInfoBean;

import com.skilrock.lms.coreEngine.reportsMgmt.common.UpdateRetailerPostSaleCommVarHelper;

public class UpdateRetailerPostSaleCommissionVariance extends ActionSupport implements
ServletRequestAware, ServletResponseAware{
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int[] orgId;
	private double[] depositVar;
	private Map<Integer, PostSalePwtCommissionVarianceBean> depositOrgCommMap;
	public String retailerPostSaleCommView(){
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		UpdateRetailerPostSaleCommVarHelper helper = new UpdateRetailerPostSaleCommVarHelper();
		 setDepositOrgCommMap(helper.fetchRetailerDepositCommExp(userBean.getUserOrgId()));
		
		return SUCCESS;
	}
	
	public String retailerPostSaleCommUpdate(){
		UpdateRetailerPostSaleCommVarHelper helper = new UpdateRetailerPostSaleCommVarHelper();
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userId = userBean.getUserId();
		
		helper.updateRetailerDepositVar(orgId, depositVar, userId);
		 setDepositOrgCommMap(helper.fetchRetailerDepositCommExp(userBean.getUserOrgId()));
		return SUCCESS;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public void setDepositOrgCommMap(Map<Integer, PostSalePwtCommissionVarianceBean> depositOrgCommMap) {
		this.depositOrgCommMap = depositOrgCommMap;
	}

	public Map<Integer, PostSalePwtCommissionVarianceBean> getDepositOrgCommMap() {
		return depositOrgCommMap;
	}

	

	

	public int[] getOrgId() {
		return orgId;
	}

	public void setOrgId(int[] orgId) {
		this.orgId = orgId;
	}

	public double[] getDepositVar() {
		return depositVar;
	}

	public void setDepositVar(double[] depositVar) {
		this.depositVar = depositVar;
	}

	

}
