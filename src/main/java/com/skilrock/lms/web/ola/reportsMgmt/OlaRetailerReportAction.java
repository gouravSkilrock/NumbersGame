package com.skilrock.lms.web.ola.reportsMgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import com.skilrock.lms.beans.OlaReportBean;
import com.skilrock.lms.beans.UserInfoBean;

import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaAgentReportHelper;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaRetailerReportHelper;


public class OlaRetailerReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware

{

	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
	private String walletName;
	private String start_date;
	private String end_Date;
	private String playerType;


	public String fetchOlaRetailerReportResultData() {
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
		OlaReportBean olaReportBean = new OlaReportBean();
		olaReportBean.setFromDate(start_date + " 00:00:00");
		olaReportBean.setToDate(end_Date + " 23:59:59");
		OlaRetailerReportHelper helper = new OlaRetailerReportHelper();
		try {
			List<OlaReportBean> olaReportList = helper
					.fetchOlaRetailerReportData(olaReportBean, walletId,
							userInfoBean.getUserOrgId(),playerType);
			session.setAttribute("OLA_DATA_LIST", olaReportList);
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

	public String getPlayerType() {
		return playerType;
	}


	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}


}
