package com.skilrock.lms.admin;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.admin.common.GenSecurityCodeHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

	public class GenSecurityCodeAction extends ActionSupport implements ServletRequestAware , ServletResponseAware{
		Log logger = LogFactory.getLog(GenSecurityCodeAction.class);
		
		int userId;
		HttpServletRequest request;
		HttpServletResponse response;
		
		private static final long serialVersionUID = 1L;
		public void genNewSecurityCode(){
			
			PrintWriter pw =null;
			//ServletContext sc= null;
			
			String status = " PLEASE RECTIFY THE DATA USING ICS ...";
			String requesInitiateTime = null;
			try {
				pw = response.getWriter();
				requesInitiateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
				logger.info("Initiated @ "+ requesInitiateTime);
				//sc=  LMSUtility.sc;
				int doneByUserId = ((UserInfoBean)request.getSession().getAttribute("USER_INFO")).getUserId();
				//int codeExpiryDays =  Integer.parseInt((String) sc.getAttribute("USER_MAPPING_ID_EXPIRY"));
				boolean isGenPlaceLMS = "true".equalsIgnoreCase(com.skilrock.lms.common.Utility.getPropertyValue("MAPPING_ID_GEN_BY_THIRD_PARTY").trim());
				int noOfExpDays = Integer.parseInt(com.skilrock.lms.common.Utility.getPropertyValue("USER_MAPPING_ID_EXPIRY").trim());
				status =  new GenSecurityCodeHelper().checkAndGenerateNewSecurityCode(0 ,doneByUserId ,/*codeExpiryDays,*/ true ,isGenPlaceLMS ,noOfExpDays ,requesInitiateTime, "MANUAL_GEN") +" !!! "+ status;
			}catch (Exception e) {
				status = "ERROR !!! PLEASE CONTACT BACK OFFICE";
				logger.error("EXCEPTION :- " , e);
			}
			pw.write(status);
		}
		
		public void genNewSecurityCodeForSpecificUsers(){
			
			PrintWriter pw =null;
			//ServletContext sc= null;
			
			String status = " PLEASE RECTIFY THE DATA USING ICS ...";
			String requesInitiateTime = null;
			try {
				pw = response.getWriter();
				if(userId == 0){
					throw new LMSException(LMSErrors.INVALIDATE_RETAILER_ERROR_CODE ,LMSErrors.INVALIDATE_RETAILER_ERROR_MESSAGE);
				}
				requesInitiateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
				logger.info("Initiated @ "+ requesInitiateTime);
				//sc=  LMSUtility.sc;
				int doneByUserId = ((UserInfoBean)request.getSession().getAttribute("USER_INFO")).getUserId();
				//int codeExpiryDays =  Integer.parseInt(com.skilrock.lms.common.Utility.getPropertyValue("USER_MAPPING_ID_EXPIRY"));
				boolean isGenPlaceLMS = "true".equalsIgnoreCase(com.skilrock.lms.common.Utility.getPropertyValue("MAPPING_ID_GEN_BY_THIRD_PARTY").trim());
				int noOfExpDays = Integer.parseInt(com.skilrock.lms.common.Utility.getPropertyValue("USER_MAPPING_ID_EXPIRY").trim());
				status =  new GenSecurityCodeHelper().checkAndGenerateNewSecurityCode(userId ,doneByUserId ,/*codeExpiryDays,*/ false ,isGenPlaceLMS ,noOfExpDays ,requesInitiateTime, "MANUAL_GEN") +" !!! "+ status;
			}catch (LMSException e) {
				status = e.getErrorMessage();
				logger.error("EXCEPTION :- " , e);
			}catch (Exception e) {
				status = "ERROR !!! PLEASE CONTACT BACK OFFICE";
				logger.error("EXCEPTION :- " , e);
			}
			pw.write(status);
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
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
		public void setServletResponse(HttpServletResponse response) {
			this.response = response;
		}

		public void setServletRequest(HttpServletRequest request) {
			this.request = request;
		}
}
