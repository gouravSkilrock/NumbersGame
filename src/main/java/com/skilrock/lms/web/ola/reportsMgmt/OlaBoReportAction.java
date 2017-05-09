package com.skilrock.lms.web.ola.reportsMgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaAgentReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaBoReportHelper;

public class OlaBoReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware

{

	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
	private String walletName;
	private String start_date;
	private String end_Date;

	// mandy in side BO ACTION he hehahahah
	public String fetchOlaBoDepWithReportData() {
		int walletId = 0;
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
			for (int i = 0; i < walletArr.length; i++) {
				walletId = Integer.parseInt(walletArr[0]);
			}
		}
		OlaAgentReportBean olaReportBean = new OlaAgentReportBean();
		olaReportBean.setFromDate(start_date + " 00:00:00");
		olaReportBean.setToDate(end_Date + " 23:59:59");
		OlaBoReportHelper helper = new OlaBoReportHelper();
		try {
			List<OlaAgentReportBean> olaReportList = helper
					.fetchOlaBoDepWithReportData(olaReportBean, walletId,
							userInfoBean.getUserOrgId());
			session.setAttribute("OLA_DEP_WITH_DATA_LIST", olaReportList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
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

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

}
