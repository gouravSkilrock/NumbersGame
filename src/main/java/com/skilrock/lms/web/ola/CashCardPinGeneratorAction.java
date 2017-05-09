package com.skilrock.lms.web.ola;


import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CashCardPinBean;
import com.skilrock.lms.beans.UserInfoBean;


public class CashCardPinGeneratorAction extends ActionSupport implements ServletRequestAware,
ServletResponseAware{

	private static final long serialVersionUID = 1L;
	private String distributorType;
	private int denoType;
	private int pinQuantity;
	private String partyType;
	private String fileName;
	private InputStream fileInputStream;
	private String expirydate;
	HttpServletRequest request;
	HttpServletResponse response;
	private HttpServletRequest servletRequest;
	SimpleDateFormat dateformat = null;
	SimpleDateFormat frm = null;
	private double commRate;// Commission Rate For Pins

	public synchronized String pinGenerator(){
		CashCardPurchaseAction.isPinPurchAllow=false;//stop cash card purchase during pin generation 
		ServletContext sc = ServletActionContext.getServletContext();
		String ip =request.getRemoteHost();
		CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
		CashCardPinBean pinBean = new CashCardPinBean();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		int walletId =0;
		if (partyType.equalsIgnoreCase("-1")
				|| partyType.equalsIgnoreCase("null")) {
			pinBean.setReturnType("Invalid Party Type");
			return ERROR;
		} else {
			String[] walletArr = partyType.split(":");
				walletId = Integer.parseInt(walletArr[0]);
				partyType = walletArr[1];
			
		}
		session = request.getSession();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		dateformat =  new SimpleDateFormat("yyyy-MM-dd");	
		String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
		String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
		try {
			if(commRate>0){
				pinBean =helper.cashCardPinGenerator(distributorType,denoType,pinQuantity,partyType,walletId,expirydate,rootPath,pinBean,desKey,propKey,userBean.getUserName(),ip,commRate);
			}
			else{
				return ERROR;
			}
			
			if(pinBean.isSuccess()){		
				session.setAttribute("cashCardPinData",pinBean);
				CashCardPurchaseAction.isPinPurchAllow=true;
				return SUCCESS;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			CashCardPurchaseAction.isPinPurchAllow=true;
			return ERROR;
		}
		     CashCardPurchaseAction.isPinPurchAllow=true;
			return ERROR;
	}
	
	public String pinDownload(){
		
		CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
		CashCardPinBean pinBean = new CashCardPinBean();
		HttpSession session = getRequest().getSession();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		try {
			 pinBean =(CashCardPinBean)session.getAttribute("cashCardPinData");
			 setFileInputStream(helper.cashCardPinsDownload(pinBean,rootPath));// set pin file to File Input Stream
			 String str[]= pinBean.getFilePath().toString().split("_");
			 int  ln = str.length;
			 String str1 = str[ln-1];
			 //str1=str[ln-2]+"_"+str1+".txt";
			 str1=str[ln-2]+"_"+str1;
			 setFileName(str1);  
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	
	}
	
	
	public String getDistributorType() {
		return distributorType;
	}


	public void setDistributorType(String distributorType) {
		this.distributorType = distributorType;
	}	
	
	public int getDenoType() {
		return denoType;
	}
	public void setDenoType(int denoType) {
		this.denoType = denoType;
	}
	public int getPinQuantity() {
		return pinQuantity;
	}
	public void setPinQuantity(int pinQuantity) {
		this.pinQuantity = pinQuantity;
	}
	public String getPartyType() {
		return partyType;
	}
	public void setPartyType(String partyType) {
		this.partyType = partyType;
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
	public HttpServletRequest getServletRequest() {
		return request;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}

	public double getCommRate() {
		return commRate;
	}

	public void setCommRate(double commRate) {
		this.commRate = commRate;
	}

	

	

	

}
