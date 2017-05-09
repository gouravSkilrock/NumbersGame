package com.skilrock.lms.web.ola.reportsMgmt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaPlayerDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
/**
 * OlaBoSearchPlayerAction is a Action class to search Player at BO,Agent,Retailer End
 * In case of Retailer retOrgId value assign from current user session
 *  
 * @author neerajjain
 *
 */
public class OlaBoSearchPlayerAction extends ActionSupport implements ServletRequestAware,
ServletResponseAware{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;

private  int retOrgId;
private String walletName;
private String regToDate;
private String regFromDate;
private ArrayList<OlaPlayerDetailsBean> plrDetailsList;
private String alias;
private String depDate;
private String regType;



HttpServletRequest request;
HttpServletResponse response;
/**
 * this method search Player(s) under a retailer
 * when walletId=-1 is search Player For all Wallets
 * @return Success
 */
public String searchPlr(){
	int walletId = 0;
	if ( walletName.equalsIgnoreCase("null")||regType.equalsIgnoreCase("null")||walletName==null||regType==null) {
		return ERROR;
	}else if(walletName.equalsIgnoreCase("-1")){
		walletId=-1;
		
	}else {
			String[] walletArr = walletName.split(":");
			walletId = Integer.parseInt(walletArr[0]);
			
	}
	HttpSession session =  getRequest().getSession();
	UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
	if(userBean.getUserType().equalsIgnoreCase("Retailer")){
		retOrgId=userBean.getUserOrgId();
	}
	OlaBoSearchPlayerHelper helper = new OlaBoSearchPlayerHelper();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calFrom = Calendar.getInstance();
	Calendar calTo= Calendar.getInstance();
	try{
		calFrom.setTime(sf.parse(regFromDate));
		calTo.setTime(sf.parse(regToDate));
		if(calFrom.compareTo(calTo)>0){
			addActionMessage("Invalid Date Selection : To Date Should be after From Date ");
			return SUCCESS;
		}
	}catch(Exception e){
		addActionMessage("Not a Valid Date");
		return SUCCESS;
	}
	
	regToDate=regToDate+" 23:59:59";
	regFromDate=regFromDate+" 00:00:00";
	plrDetailsList=helper.searchPlr(retOrgId,walletId,regToDate,regFromDate,alias.trim(),regType);
	return SUCCESS;
}

/**
 * this Method convert Deployment date format
 */
@Override
	public String execute() throws Exception {
		
	ServletContext sc = ServletActionContext.getServletContext();
	String deployDate = (String) sc.getAttribute("DEPLOYMENT_DATE");
	SimpleDateFormat formatOld = new SimpleDateFormat((String) sc.getAttribute("date_format"));
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Date oldDate = formatOld.parse(deployDate);
	Calendar cal1 = Calendar.getInstance();
	cal1.setTime(oldDate);
	
	depDate = format.format(cal1.getTime());
		return SUCCESS;
	}

	

public String getRegType() {
	return regType;
}

public void setRegType(String regType) {
	this.regType = regType;
}


public String getDepDate() {
	return depDate;
}


public void setDepDate(String depDate) {
	this.depDate = depDate;
}


public int getRetOrgId() {
	return retOrgId;
}


public void setRetOrgId(int retOrgId) {
	this.retOrgId = retOrgId;
}


public String getWalletName() {
	return walletName;
}


public void setWalletName(String walletName) {
	this.walletName = walletName;
}


public String getRegToDate() {
	return regToDate;
}


public void setRegToDate(String regToDate) {
	this.regToDate = regToDate;
}


public String getRegFromDate() {
	return regFromDate;
}


public void setRegFromDate(String regFromDate) {
	this.regFromDate = regFromDate;
}




public ArrayList<OlaPlayerDetailsBean> getPlrDetailsList() {
	return plrDetailsList;
}


public void setPlrDetailsList(ArrayList<OlaPlayerDetailsBean> plrDetailsList) {
	this.plrDetailsList = plrDetailsList;
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

public String getAlias() {
	return alias;
}


public void setAlias(String alias) {
	this.alias = alias;
}



}
