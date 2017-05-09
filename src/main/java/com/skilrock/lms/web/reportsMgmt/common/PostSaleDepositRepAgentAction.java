package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PostSaleDepositAgentBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.PostSaleDepositRepHelper;

public class PostSaleDepositRepAgentAction extends ActionSupport implements ServletRequestAware, ServletResponseAware
{
	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;
	private HttpServletResponse response;

	private int month;
	private int year;
	
	private String message;


	private List<PostSaleDepositAgentBean> postSaleRepBeanList  ;
	private String repType;

	public String search() throws LMSException
	{
		
		Calendar cal = Calendar.getInstance();
		if(year>cal.get(Calendar.YEAR)){
			addActionMessage("Commission not Calculated  Yet ..Please Select Previous Year");
		}else if((cal.get(Calendar.YEAR)==year)&&(cal.get(Calendar.MONTH)==(month-1))){
			addActionMessage("Commission not Calculated  Yet ..Please Select Previous Month ");
		}else{
			if(month>0 && year>0){
				
				postSaleRepBeanList=PostSaleDepositRepHelper.getInstance().getReportData(month,year,repType);
			}
		}
		
	

		return SUCCESS;
	}

	public void exportToExcel() throws IOException
	{
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Post_Sale_Deposit_Agent_ReportExpand.xls");
		PrintWriter out = response.getWriter();
		message = message.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
		//	out.write("<table><tbody><tr><th> Date/Time </th><th> Service </th><th> Particular </th><th> Amount </th><th> Available Balance </th><th> Remarks </th></tr><tr><td> </td><td> </td><td > Opening Balance(including CL/XCL) : </td><td> 0.0 </td><td> 992,217.25 </td><td> </td></tr></tbody></table>");
		out.write("<table border='1' width='100%' >"+message+"</table>");
	}

	@Override
	public String execute() throws LMSException
	{
		return SUCCESS;
	}
	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<PostSaleDepositAgentBean> getPostSaleRepBeanList() {
		return postSaleRepBeanList;
	}

	public void setPostSaleRepBeanList(
			List<PostSaleDepositAgentBean> postSaleRepBeanList) {
		this.postSaleRepBeanList = postSaleRepBeanList;
	}

	public String getRepType() {
		return repType;
	}

	public void setRepType(String repType) {
		this.repType = repType;
	}
}