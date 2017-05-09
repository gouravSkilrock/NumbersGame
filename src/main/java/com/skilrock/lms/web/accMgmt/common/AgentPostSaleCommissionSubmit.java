package com.skilrock.lms.web.accMgmt.common;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PostSalePwtCommissionBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.reportsMgmt.common.AgentPostSaleCommissionHelper;

public class AgentPostSaleCommissionSubmit extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private int month;

	private Integer year;
	private int agentOrgId;
	private String startDate;
	private String endDate;
	private String resStatus;
	Map<String, PostSalePwtCommissionBean> postSaleDepositAgentDateMap;

	public String agentPostSaleDepositCommMenu() {

		return SUCCESS;
	}

	public String agentPostSaleDepositCommSearch() {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date stDate = new java.sql.Date(cal.getTimeInMillis());
		System.out.println("start Date" + startDate);
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month + 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		Date enDate = new java.sql.Date(cal.getTimeInMillis());
		System.out.println("End Date" + endDate);
		AgentPostSaleCommissionHelper commHelper = new AgentPostSaleCommissionHelper();

		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		setStartDate(formatter.format(stDate));
		setEndDate(formatter.format(enDate));

		Date currentDate = new java.sql.Date(new java.util.Date().getTime());

		if (AgentPostSaleCommissionHelper.getZeroTimeDate(stDate).compareTo(
				AgentPostSaleCommissionHelper.getZeroTimeDate(currentDate)) > 0
				|| AgentPostSaleCommissionHelper.getZeroTimeDate(enDate)
						.compareTo(
								AgentPostSaleCommissionHelper
										.getZeroTimeDate(currentDate)) > 0) {
			return SUCCESS;

		}

		postSaleDepositAgentDateMap = commHelper.getAgentCommissionDetail(
				startDate, endDate, agentOrgId);
		setAgentOrgId(agentOrgId);

		return SUCCESS;
	}

	public String agentPostSaleDepositCommSubmit() {

		HttpSession session = request.getSession();

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		System.out.println(startDate + "endDate" + endDate + agentOrgId);
		AgentPostSaleCommissionHelper commHelper = new AgentPostSaleCommissionHelper();
		commHelper.updateAgentCommissionDetailStatus(startDate, endDate,
				agentOrgId, "CASH", userBean.getUserId());
		postSaleDepositAgentDateMap = commHelper.getAgentCommissionDetail(
				startDate, endDate, agentOrgId);
		session.setAttribute("STATUS", getText("msg.you.have.success.pay"));
		setResStatus(getText("msg.you.have.success.pay"));
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

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public Map<String, PostSalePwtCommissionBean> getPostSaleDepositAgentDateMap() {
		return postSaleDepositAgentDateMap;
	}

	public void setPostSaleDepositAgentDateMap(
			Map<String, PostSalePwtCommissionBean> postSaleDepositAgentDateMap) {
		this.postSaleDepositAgentDateMap = postSaleDepositAgentDateMap;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getResStatus() {
		return resStatus;
	}

	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}

}
