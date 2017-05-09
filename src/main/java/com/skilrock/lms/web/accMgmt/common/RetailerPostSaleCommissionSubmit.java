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
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetailerPostSaleCommissionHelper;

public class RetailerPostSaleCommissionSubmit extends ActionSupport implements
ServletRequestAware, ServletResponseAware{
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
private int month;
	
	private Integer year;
	private int retailerOrgId;
	private String startDate;
	private String endDate;
	private String resStatus;
	Map<String, PostSalePwtCommissionBean> postSaleDepositAgentDateMap;
	Map<Integer, String> retailerNameList;
	public String retailerPostSaleDepositCommMenu() {
		HttpSession session=request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		
		
		RetailerPostSaleCommissionHelper retailerHelper=new RetailerPostSaleCommissionHelper();
		setRetailerNameList(retailerHelper.getRetailerNameList(userBean.getUserOrgId()));
		return SUCCESS;
	}

	public String retailerPostSaleDepositCommSearch() {
						
		Calendar cal = Calendar.getInstance();
	    cal.clear();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    Date stDate = new java.sql.Date(cal.getTimeInMillis());
	    System.out.println("start Date"+startDate);
	    cal.clear();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, month+1);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    
	    Date enDate = new java.sql.Date(cal.getTimeInMillis());
	    System.out.println("End Date"+endDate);
	    RetailerPostSaleCommissionHelper commHelper=new RetailerPostSaleCommissionHelper();
	    
	    Format formatter = new SimpleDateFormat("yyyy-MM-dd");
	    setStartDate(formatter.format(stDate));
	    setEndDate(formatter.format(enDate));
	     
	    Date currentDate=new java.sql.Date(new java.util.Date().getTime());
	    
	    if(RetailerPostSaleCommissionHelper.getZeroTimeDate(stDate).compareTo(RetailerPostSaleCommissionHelper.getZeroTimeDate(currentDate)) > 0 || RetailerPostSaleCommissionHelper.getZeroTimeDate(enDate).compareTo(RetailerPostSaleCommissionHelper.getZeroTimeDate(currentDate)) > 0  ){
			return SUCCESS;
	    	
	    }
	    
	    postSaleDepositAgentDateMap=commHelper.getRetailerCommissionDetail(startDate, endDate, retailerOrgId);
	    setRetailerOrgId(retailerOrgId);
	   
		return SUCCESS;
	}

	public String retailerPostSaleDepositCommSubmit() {

		HttpSession session = request.getSession();
		System.out.println(startDate + "endDate" + endDate + retailerOrgId);
		UserInfoBean userBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		RetailerPostSaleCommissionHelper commHelper = new RetailerPostSaleCommissionHelper();
		commHelper.updateRetailerCommissionDetailStatus(startDate, endDate,
				retailerOrgId,"CASH",userBean.getUserId());
		postSaleDepositAgentDateMap = commHelper.getRetailerCommissionDetail(
				startDate, endDate, retailerOrgId);
		session.setAttribute("STATUS", "You Have Successfully Paid");
		setResStatus("You Have Successfully Paid");
		return SUCCESS;
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

	public Map<String, PostSalePwtCommissionBean> getPostSaleDepositAgentDateMap() {
		return postSaleDepositAgentDateMap;
	}

	public void setPostSaleDepositAgentDateMap(
			Map<String, PostSalePwtCommissionBean> postSaleDepositAgentDateMap) {
		this.postSaleDepositAgentDateMap = postSaleDepositAgentDateMap;
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

	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}

	public int getRetailerOrgId() {
		return retailerOrgId;
	}

	public Map<Integer, String> getRetailerNameList() {
		return retailerNameList;
	}

	public void setRetailerNameList(Map<Integer, String> retailerNameList) {
		this.retailerNameList = retailerNameList;
	}
	
	
}
