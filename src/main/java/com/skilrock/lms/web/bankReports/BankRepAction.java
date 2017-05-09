package com.skilrock.lms.web.bankReports;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.web.bankMgmt.beans.BankRepDataBean;
import com.skilrock.lms.web.bankMgmt.beans.BranchDetailsBean;



public class BankRepAction extends ActionSupport implements ServletRequestAware,
ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String currDate;
	private String startDate;
	private String endDate;
	private BranchDetailsBean branchDetailsBean ;
	private List<BankRepDataBean> bankRepDataBeanList;
	HttpServletRequest request;
	HttpServletResponse response;
	private String terminalId;
	private String repType;
	public String execute () {
		Calendar cal = Calendar.getInstance();
		setCurrDate(CommonMethods.convertDateInGlobalFormat(new java.sql.Date(cal
						.getTimeInMillis()).toString(), "yyyy-mm-dd",
						"yyyy-mm-dd"));
	
		return SUCCESS;
		
	
	}
	public String fetchCreditUpdateData(){
		BankRepHelper helper = new BankRepHelper();
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		setBankRepDataBeanList(helper.fetchCreditUpdateData(branchDetailsBean,startDate,endDate,userBean));
		return SUCCESS;
		
	}
	
	public String fetchCashierWiseTrnData(){
		BankRepHelper helper = new BankRepHelper();
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		
		setBankRepDataBeanList(helper.fetchCashierWiseTrnData(branchDetailsBean,startDate,endDate,userBean,terminalId,repType));
		
		return SUCCESS;
		
	}
	public String fetchWinningUpdateData(){
		BankRepHelper helper = new BankRepHelper();
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		setBankRepDataBeanList(helper.fetchWinningUpdateData(branchDetailsBean,startDate,endDate,userBean));
		return SUCCESS;
		
	}
	
	public String getCurrDate() {
		return currDate;
	}
	public void setCurrDate(String currDate) {
		this.currDate = currDate;
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
	public BranchDetailsBean getBranchDetailsBean() {
		return branchDetailsBean;
	}
	public void setBranchDetailsBean(BranchDetailsBean branchDetailsBean) {
		this.branchDetailsBean = branchDetailsBean;
	}
	public List<BankRepDataBean> getBankRepDataBeanList() {
		return bankRepDataBeanList;
	}
	public void setBankRepDataBeanList(List<BankRepDataBean> bankRepDataBeanList) {
		this.bankRepDataBeanList = bankRepDataBeanList;
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

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getRepType() {
		return repType;
	}
	public void setRepType(String repType) {
		this.repType = repType;
	}
}
