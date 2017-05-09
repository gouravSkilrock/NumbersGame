package com.skilrock.lms.web.accMgmt.common;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.QueryHelper;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.TwoWayCashDepositAtBOHelper;

public class TwoWayCashDepositAtBO extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private String orgName;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private double amount;
	private int agtOrgId;
	private int retOrgId;

	public void geRetOrgNames() throws Exception {
		try {
			QueryHelper qp = new QueryHelper();
			ArrayList<String> serialNoList = null;
			serialNoList = qp.searchOrganizationForAllRetailer();
			String serialNoStr = serialNoList.toString().replace("[", "")
					.replace("]", "").replace(", ", ":");
			getResponse().getWriter().print(serialNoStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public void getLmcId() throws Exception {
		try {
			QueryHelper qp = new QueryHelper();
			String lmcId = qp.getLmcIdOfRetailer(orgName);
			getResponse().getWriter().print(
					lmcId != null ? lmcId : getText("msg.incorrect.ret.id"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public String twoWayPaymentSubmit() throws LMSException {
		HttpSession session = null;
		session = getRequest().getSession();
		if (amount == 0) {
			session.setAttribute("status", getText("msg.amt.cannot.be.zero"));
			return SUCCESS;
		}
		if (agtOrgId <= 0 || retOrgId <= 0) {
			session.setAttribute("status", getText("msg.invalid.inp.for.pay"));
			return SUCCESS;
		}
		TwoWayCashDepositAtBOHelper helper = new TwoWayCashDepositAtBOHelper();
		String status = helper.twoWayPaymentSubmit(agtOrgId, retOrgId,
				(UserInfoBean) session.getAttribute("USER_INFO"), amount);
		if (status.equals("NONE")) {
			session.setAttribute("status", getText("msg.acc.update.success"));
			return SUCCESS;
		}
		session.setAttribute("status", status);
		return SUCCESS;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

}
