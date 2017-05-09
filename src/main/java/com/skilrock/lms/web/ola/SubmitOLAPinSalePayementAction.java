package com.skilrock.lms.web.ola;


import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaPinSalePaymentBean;
import com.skilrock.lms.beans.UserInfoBean;
/**
 * This class provide methods to fetch approved payment records and make payment 
 * 
 * @author Neeraj Jain
 *
 */
public class SubmitOLAPinSalePayementAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 1L;
	private String distributorType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int[] check;
	private String recieptNo;
	static Log logger = LogFactory.getLog(SubmitOLAPinSalePayementAction.class);
/**
 * this method fetch the approved pin sale payment data 
 * @return SUCCESS
 */	
	public String fetchPinSalePaymentData(){
		HttpSession session = getRequest().getSession();
		SubmitOLAPinSalePayementHelper helper = new SubmitOLAPinSalePayementHelper();
		ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentSubmitList = new ArrayList<OlaPinSalePaymentBean>();
		olaPinSalePaymentSubmitList=helper.fetchPinSalePaymentTask(distributorType);
		session.setAttribute("SUBMIT_PIN_PAYMENT_BEAN_LIST",olaPinSalePaymentSubmitList);
		return SUCCESS;
	
		}
/**
 * this method make payment for checked records 
 * @return SUCCESS/ERROR
 */	
	
	public String makePinSalePayment(){
		SubmitOLAPinSalePayementHelper helper = new SubmitOLAPinSalePayementHelper();
		HttpSession session =getRequest().getSession();
		//get approved tasks from session 
		ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentSubmitList = (ArrayList<OlaPinSalePaymentBean>)session.getAttribute("SUBMIT_PIN_PAYMENT_BEAN_LIST");
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		// make payment
		recieptNo=helper.savePinSalePayment(olaPinSalePaymentSubmitList,check,userBean);
		System.out.println(getRecieptNo());
		if(recieptNo.equalsIgnoreCase("false")){
			addActionMessage("Some Error");
			return ERROR;
		}
		else {
			setRecieptNo(recieptNo);
			return SUCCESS;
		}
		
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request =request;
		
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}
	public String getDistributorType() {
		return distributorType;
	}
	public void setDistributorType(String distributorType) {
		this.distributorType = distributorType;
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

	public int[] getCheck() {
		return check;
	}

	public void setCheck(int[] check) {
		this.check = check;
	}

	public String getRecieptNo() {
		return recieptNo;
	}

	public void setRecieptNo(String recieptNo) {
		this.recieptNo = recieptNo;
	}

}
