package com.skilrock.ola.commonMethods.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OLAPlrLotteryHelper;
import com.skilrock.lms.coreEngine.ola.OLARummyHelper;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.javaBeans.CountryDataBean;
import com.skilrock.ola.javaBeans.OlaWalletBean;
import com.skilrock.ola.userMgmt.controllerImpl.OlaPlrRegistrationControllerImpl;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;


public class OlaCommonMethodAction extends ActionSupport implements ServletRequestAware,
ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(OlaCommonMethodAction.class);
	HttpServletRequest request;
	HttpServletResponse response;
	private String refCode;
	private String requestData;
	private String walletName;
	private String userName;
	private String phone;
	private String email;
	
	
	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
    
	public void getWalletDetail(){
		int mobileNumberlimit;
		PrintWriter out = null;
		response.setContentType("application/json");
		List<OlaWalletBean> walletBeanList = null;
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject js = new JSONObject();
		try{
			out = response.getWriter();
			mobileNumberlimit = Integer.parseInt((String)sc.getAttribute("OLA_PLAYER_MOBILE_NUMBER_LIMIT"));
			walletBeanList = new ArrayList<OlaWalletBean>(new OlaCommonMethodControllerImpl().getWalletDetails().values());
			if (walletBeanList.size()>0 && mobileNumberlimit!=0) {
				String jsdetails = new Gson().toJson(walletBeanList);
				js.put("isSuccess", true);
				js.put("responseCode", 0);
				js.put("walletDetail", jsdetails);
				js.put("mobilelimit", mobileNumberlimit);
			}else {
				js.put("isSuccess", false);
				js.put("errorCode", 500);
				js.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			js.put("isSuccess", false);
			js.put("errorCode", 500);
			js.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(js);
		out.flush();
		out.close();
		
	}
	
	public void verifyRefCode() {
		PrintWriter out = null;
		JSONObject resObject=new JSONObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
				
				OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
				boolean isSuccess=olaCommonMethodController.verifyRefCode(refCode, walletName);
				resObject.put("isSuccess", isSuccess);
				if(isSuccess){
					resObject.put("message",!"ALA_WALLET".equalsIgnoreCase(walletName)?LMSErrors.VALID_PHONE_NUMBER_MESSAGE:LMSErrors.VALID_CARD_NUMBER_MESSAGE);				
				}else{
					resObject.put("message",!"ALA_WALLET".equalsIgnoreCase(walletName)?LMSErrors.INVALID_PHONE_NUMBER_MESSAGE:LMSErrors.INVALID_CARD_NUMBER_MESSAGE);					
				}			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LMSException e) {
				String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
				resObject.put("message",errorMessage);
				logger.info(e.getErrorCode()+errorMessage);
			}catch(GenericException e){
				e.getStackTrace();
				String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
				resObject.put("message",errorMessage);
				resObject.put("isSuccess", true);
				logger.info(e.getErrorCode()+errorMessage);
			}catch(Exception e){
				e.getStackTrace();
				resObject.put("message",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				resObject.put("isSuccess", true);
			}
			
			out.print(resObject);
			out.flush();
			out.close();
	}
	
	public void checkUserName() throws LMSException {
		PrintWriter out = null;
		JSONObject resObject=new JSONObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
				
				OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
				boolean isSuccess=olaCommonMethodController.verifyUserName(userName, walletName);
				resObject.put("isSuccess", isSuccess);
				if(isSuccess){
					resObject.put("message",LMSErrors.USER_NAME_ALREADY_EXIST_MESSAGE);
				}else{
					resObject.put("message",LMSErrors.USER_NAME_AVAL_MESSAGE);					
				}			
			} catch (IOException e) {
				e.printStackTrace();
				
			} catch (LMSException e) {
				System.out.println(e.getErrorCode());
				String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
				resObject.put("isSuccess", true);
				resObject.put("message",errorMessage);
				logger.info(e.getErrorCode()+errorMessage);
			}
			out.print(resObject);
			out.flush();
			out.close();
	}
	
	public void checkMobileNum() throws LMSException {
		PrintWriter out = null;
		JSONObject resObject=new JSONObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
				
				OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
				if(phone.startsWith("0")){
					resObject.put("message",LMSErrors.INVALID_PHONE_NUMBER_ERROR_MESSAGE);
					resObject.put("isSuccess", true);
				}else{
					boolean isSuccess=olaCommonMethodController.verifyMobileNum(phone,walletName);
					resObject.put("isSuccess", isSuccess);
					if(isSuccess){
						resObject.put("message",LMSErrors.PHONE_NUM_ALREADY_EXIST_MESSAGE);
					}else{
						resObject.put("message",LMSErrors.PHONE_NUM_AVAL_MESSAGE);					
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				
			} catch (LMSException e) {
				//System.out.println(e.getErrorCode());
				String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
				resObject.put("isSuccess", true);
				resObject.put("message",errorMessage);
				logger.info(e.getErrorCode()+errorMessage);
			}
			out.print(resObject);
			out.flush();
			out.close();
	}
	
	public void checkEmail() throws LMSException {
		PrintWriter out = null;
		JSONObject resObject=new JSONObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
				
			OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
			boolean isSuccess=olaCommonMethodController.verifyEmail(email,walletName);
			resObject.put("isSuccess", isSuccess);
			if(isSuccess){
				resObject.put("message",LMSErrors.EMAIL_ALREADY_EXIST_MESSAGE);
			}else{
				resObject.put("message",LMSErrors.EMAIL_EXIST_MESSAGE);					
			}
			} catch (IOException e) {
				e.printStackTrace();
				
			} catch (LMSException e) {
				//System.out.println(e.getErrorCode());
				String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
				resObject.put("isSuccess", true);
				resObject.put("message",errorMessage);
				logger.info(e.getErrorCode()+errorMessage);
			}
			out.print(resObject);
			out.flush();
			out.close();
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

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getWalletName() {
		return walletName;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}