package com.skilrock.lms.web.ola;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import com.skilrock.lms.common.exception.LMSException;
/**
 * Action Class for OLA pin sale payment approve 
 * provide methods to fetch and approve pin payment data
 * @author Neeraj Jain
 *
 */
public class CalculateOLAPinSaleAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 1L;
	private String distributorType;
	private String endDate;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int[] check;
	static Log logger =LogFactory.getLog(CalculateOLAPinSaleAction.class);
/**
 * 
 * @return SUCCESS/ERROR(if selected date is today's date)
 */	
	public String fetchPinSaleData(){
		ServletContext sc = ServletActionContext.getServletContext();
		String deployDate = (String) sc.getAttribute("DEPLOYMENT_DATE");// Get deployment date 
		HttpSession session = getRequest().getSession();
		CalculateOLAPinSaleHelper helper = new CalculateOLAPinSaleHelper();
		ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentBeanList = new ArrayList<OlaPinSalePaymentBean>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		try{
			
			Date currentDate=new Date();
			Calendar calcurrent = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			calcurrent.setTime(format.parse(endDate));
			calEnd.setTime(format.parse(format.format(currentDate)));
			logger.info("in fetchPinSaleData Distributor :"+distributorType+"Deploy Date:"+deployDate+" end date:"+endDate+" currentDate:"+currentDate);
			//System.out.println(currDate.compareTo(taskEndDate));
	// check weather selected data is current date or not	
		if(calEnd.compareTo(calcurrent)<=0){
				System.out.println("Error: Selected date is current date ");
				addActionMessage("Please Select Date Before Today ");
				session.removeAttribute("PIN_PAYMENT_BEAN_LIST");
				return ERROR;
			}
		}catch(Exception e){
			System.out.println("Error: in date parsing ");
			addActionMessage("Some Error : Invalid Date ");
			session.removeAttribute("PIN_PAYMENT_BEAN_LIST");
			e.printStackTrace();
			return ERROR;
		}
	// call helper method to fetch data
		olaPinSalePaymentBeanList=helper.pinSaleData(distributorType,endDate,deployDate);
		session.setAttribute("PIN_PAYMENT_BEAN_LIST",olaPinSalePaymentBeanList);
		return SUCCESS;
	}
/**
 * approve checked payment records
 * @return SUCCESS/ERROR
 */	
	public String approvePinSaleData(){
		try {
			CalculateOLAPinSaleHelper helper = new CalculateOLAPinSaleHelper();
			HttpSession session =getRequest().getSession();
			// Get payment records form session attribute
			ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentBeanList = (ArrayList<OlaPinSalePaymentBean>)session.getAttribute("PIN_PAYMENT_BEAN_LIST");
			// Call helper to approve records
			boolean isSuccess=helper.saveApprovedData(olaPinSalePaymentBeanList,check);
			if(isSuccess){
				return SUCCESS;
			}
			else {
				addActionMessage("Some Error");
				return ERROR;
			}
		}  catch (LMSException e) {
			addActionMessage(e.getErrorMessage());
			return ERROR;
		}catch (Exception e) {
			addActionMessage("Some Error");
			return ERROR;
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

	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

}
