package com.skilrock.lms.web.ola;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaRummyResendSMSBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;

public class OlaRummyResendSmsAction extends ActionSupport implements ServletResponseAware,ServletRequestAware{
private static final long serialVersionUID = 1L;
private String pinType;
private double amount;
private String plrname;
private String depositDate;
private HttpServletRequest request;
private HttpServletResponse response;
private int taskId ;
private String retailer;
public String searchOLADeposit(){
	ArrayList<OlaRummyResendSMSBean> smsBeanList = new ArrayList<OlaRummyResendSMSBean>();
	HttpSession session = getRequest().getSession();
	OlaRummyResendSmsHelper helper = new OlaRummyResendSmsHelper();
	smsBeanList =helper.searchDeposit(smsBeanList,amount,pinType,plrname,depositDate,retailer);
	session.setAttribute("SMS_BEAN_LIST",smsBeanList);
	return SUCCESS;
}
public void  sendOlaDepositSMS() throws LMSException{
	ServletContext sc = ServletActionContext.getServletContext();
	String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
	String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
	try {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		OlaRummyResendSmsHelper helper = new OlaRummyResendSmsHelper();
		ArrayList<OlaRummyResendSMSBean> smsBeanList =( ArrayList<OlaRummyResendSMSBean>) session.getAttribute("SMS_BEAN_LIST");
		String result=null;
		for(int i=0;i<smsBeanList.size();i++){
			if(taskId==smsBeanList.get(i).getId()){
				result = helper.sendSMS(smsBeanList.get(i),desKey,propKey);
			}
		}
		System.out.println("sdsads"+result);
		System.out.println(taskId);
		out.print(result+"N@xt"+taskId);
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
}	
	

	
public void getOrgList()throws LMSException{
	try {
		PrintWriter out = getResponse().getWriter();
		CommonFunctionsHelper  helper = new CommonFunctionsHelper ();
		List<OrgBean> orgList=helper.getRetailerOrgList("-1");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<orgList.size();i++){
			sb.append(orgList.get(i).getOrgName()+"N@xt");
		}
		sb.delete(sb.lastIndexOf("N@xt"), sb.length());
		out.print(sb.toString());
		
	}
	catch(Exception e){
		e.printStackTrace();
	}

	
}


public String getDepositDate() {
	return depositDate;
}
public void setDepositDate(String depositDate) {
	this.depositDate = depositDate;
}

public String getPinType() {
	return pinType;
}
public void setPinType(String pinType) {
	this.pinType = pinType;
}
public double getAmount() {
	return amount;
}
public void setAmount(double amount) {
	this.amount = amount;
}
public String getPlrname() {
	return plrname;
}
public void setPlrname(String plrname) {
	this.plrname = plrname;
}
public void setServletResponse(HttpServletResponse response) {
this.response =response;
}
public void setServletRequest(HttpServletRequest request) {
this.request=request;
	
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
public int getTaskId() {
	return taskId;
}
public void setTaskId(int taskId) {
	this.taskId = taskId;
}
public String getRetailer() {
	return retailer;
}
public void setRetailer(String retailer) {
	this.retailer = retailer;
}


}
