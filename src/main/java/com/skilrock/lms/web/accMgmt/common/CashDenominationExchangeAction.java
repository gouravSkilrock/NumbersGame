package com.skilrock.lms.web.accMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.accMgmt.common.CashRegisterHelper;

public class CashDenominationExchangeAction extends ActionSupport implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private String[] iMultiples;
	private String[] eMultiples;
	private String[] iDenoType;
	
	public String exchangeDenomination()
	{
		try{
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean)session.getAttribute("USER_INFO");
		CashRegisterHelper drawerHelper = new CashRegisterHelper();
		List<String> denoList = drawerHelper.getReceivedDenoList();
		String[] denoArray = (String[]) denoList.toArray(new String[denoList.size()]);
		CashDenominationExchangeHelper helper = new CashDenominationExchangeHelper();
		helper.saveExchangeMoneyData(iMultiples,eMultiples,denoArray,userBean,iDenoType);
		}
		catch(Exception e){
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

	public String[] getiMultiples() {
		return iMultiples;
	}

	public void setiMultiples(String[] iMultiples) {
		this.iMultiples = iMultiples;
	}

	public String[] geteMultiples() {
		return eMultiples;
	}

	public void seteMultiples(String[] eMultiples) {
		this.eMultiples = eMultiples;
	}

	public void setServletRequest(HttpServletRequest req) {
		request = req;
	}


	public String[] getiDenoType() {
		return iDenoType;
	}


	public void setiDenoType(String[] iDenoType) {
		this.iDenoType = iDenoType;
	}
	
}
