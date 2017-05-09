package com.skilrock.lms.web.ola;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaRummyWithdrawalBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;

public class OlaRummyWithdrawalAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
private static final long serialVersionUID = 1L;
private String startDate ;
private String endDate;
private String transferMode;
private String status ;
private HttpServletRequest request;
private HttpServletResponse response;
private int []taskIds;
private InputStream fileInputStream;



public String approveWithdrawal(){
	OlaRummyWithdrawalHelper  helper = new OlaRummyWithdrawalHelper ();
	ArrayList<OlaRummyWithdrawalBean> withdrawalBeanList = new 	ArrayList<OlaRummyWithdrawalBean>();
	HttpSession session = getRequest().getSession();
	withdrawalBeanList = helper.rummyWithdrawalData(withdrawalBeanList,startDate,endDate,transferMode,status);
	session.setAttribute("WITHDRAWAL_DATA_LIST",withdrawalBeanList);
	
	return SUCCESS;
	
}
public void updateWithdrawal()throws LMSException, IOException{
	PrintWriter out = getResponse().getWriter();
	OlaRummyWithdrawalHelper  helper = new OlaRummyWithdrawalHelper ();
	
	String result = helper.updateRummyWithdrawal(taskIds);
	
	ArrayList<OlaRummyWithdrawalBean> withdrawalBeanList = new 	ArrayList<OlaRummyWithdrawalBean>();
	HttpSession session = getRequest().getSession();
	withdrawalBeanList=(ArrayList<OlaRummyWithdrawalBean>)( session.getAttribute("WITHDRAWAL_DATA_LIST"));
	
	for(int i=0;i<withdrawalBeanList.size();i++){
		OlaRummyWithdrawalBean withdrawalBean = new OlaRummyWithdrawalBean();
		withdrawalBean =withdrawalBeanList.get(i);
		for(int j=0;j<taskIds.length;j++){
			if(taskIds[j]==withdrawalBean.getTaskId()){
				withdrawalBean.setStatus("APPROVED");
			}
		}
		withdrawalBeanList.set(i,withdrawalBean);
	}
	session.setAttribute("WITHDRAWAL_DATA_LIST",withdrawalBeanList);	
	out.print(result);
}

public String exportExcel(){
	ArrayList<OlaRummyWithdrawalBean> withdrawalBeanList = new 	ArrayList<OlaRummyWithdrawalBean>();
	HttpSession session = getRequest().getSession();
	withdrawalBeanList=(ArrayList<OlaRummyWithdrawalBean>)( session.getAttribute("WITHDRAWAL_DATA_LIST"));
	OlaRummyWithdrawalHelper  helper = new OlaRummyWithdrawalHelper ();
	setFileInputStream(helper.writeDataIntoExcel(withdrawalBeanList));
	return SUCCESS;

	
	
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
public String getTransferMode() {
	return transferMode;
}
public void setTransferMode(String transferMode) {
	this.transferMode = transferMode;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public void setServletRequest(HttpServletRequest request) {
this.request = request;	
}

public void setServletResponse(HttpServletResponse response) {
this.response = response;
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
public int[] getTaskIds() {
	return taskIds;
}
public void setTaskIds(int[] taskIds) {
	this.taskIds = taskIds;
}
public InputStream getFileInputStream() {
	return fileInputStream;
}
public void setFileInputStream(InputStream fileInputStream) {
	this.fileInputStream = fileInputStream;
}



}
