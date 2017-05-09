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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
/** Provide Cancel Pin  Functionality  and refund amount 
 * 
 * @author Neeraj Jain
 *
 */
public class OlaRummyRefundPinAction extends ActionSupport implements ServletResponseAware,ServletRequestAware{
	
private static final long serialVersionUID = 1L;
private long pin;
private long serial;
private double amount;
private String plrname;
private HttpServletRequest request;
private HttpServletResponse response;
private String walletName;



public String refundPin(){
	int 	walletId=0;
	ServletContext sc = ServletActionContext.getServletContext();
	String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
	String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
	if (walletName.equalsIgnoreCase("-1")
			|| walletName.equalsIgnoreCase("null")) {
		return ERROR;
	} else {
		String[] walletArr = walletName.split(":");
		walletId = Integer.parseInt(walletArr[0]);
			
	}
	OlaRummyRefundPinHelper helper = new OlaRummyRefundPinHelper();
	String cancelStatus =helper.initRefundPin(walletId, pin, serial, plrname.trim(), amount,desKey,propKey);
	if(cancelStatus.equalsIgnoreCase("success")){
		return SUCCESS;
	}
	else{
		addActionMessage(cancelStatus);
		return ERROR;
	}
	
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
public long getPin() {
	return pin;
}


public void setPin(long pin) {
	this.pin = pin;
}


public long getSerial() {
	return serial;
}


public void setSerial(long serial) {
	this.serial = serial;
}


public String getWalletName() {
	return walletName;
}


public void setWalletName(String walletName) {
	this.walletName = walletName;
}




}
