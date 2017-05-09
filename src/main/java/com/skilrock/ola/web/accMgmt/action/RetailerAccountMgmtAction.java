package com.skilrock.ola.web.accMgmt.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetDepositControllerImpl;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetWithdrawlControllerImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalResponseBean;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.javaBeans.OlaWalletBean;


public class RetailerAccountMgmtAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(RetailerAccountMgmtAction.class);
	HttpServletRequest request;
	HttpServletResponse response;
	
	private String walletName;
	
	private OLADepositRequestBean depositReqBean;
	private OLAWithdrawalRequestBean withdrawalReqBean;
	private Map<Integer, OlaWalletBean> walletDetailsMap=null;
	private int mobileNumberlimit;
	private String reqData;
	
	public String olaRetailerMenu(){		
		OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
		setWalletDetailsMap(olaCommonMethodController.getWalletDetails());
		mobileNumberlimit = Integer.parseInt((String)LMSUtility.sc.getAttribute("OLA_PLAYER_MOBILE_NUMBER_LIMIT"));
		return SUCCESS;
	}	
	
	public void depositMoney() throws IOException{
		int walletId=0;
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		JsonObject json=null;
		try{
			response.setContentType("application/json");
			out = response.getWriter();
			logger.info("className: {} OLA Deposit Request Data : {}"+ reqData);
			json=new JsonParser().parse(reqData).getAsJsonObject();
			setDepositReqBean(new Gson().fromJson(json, OLADepositRequestBean.class));
			walletId=OLAUtility.getWalletId(depositReqBean.getWalletDevName());
			ServletContext sc = ServletActionContext.getServletContext();
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");
			double olaMinDepositLimit = OLAUtility.getOlaWalletDataMap().get(walletId).getMinDeposit();
			logger.info("depositAnyWhere" + depositAnyWhere+"WalletName :"+depositReqBean.getWalletDevName());
			OlaRetDepositControllerImpl olaDepositController = new OlaRetDepositControllerImpl();
			if (depositReqBean.getDepositAmt() >= olaMinDepositLimit) {
				depositReqBean.setDepositAnyWhere(depositAnyWhere);
				depositReqBean.setDeviceType("WEB");
				depositReqBean.setWalletId(walletId);
				OLADepositResponseBean  depositResBean =olaDepositController.olaRetPlrDeposit(depositReqBean, userBean);
				if(depositResBean.isSuccess()){
					jsonResponse.put("isSuccess", true);
					jsonResponse.put("responseCode",0);
					jsonResponse.put("responseMsg", "");
				}else{
					jsonResponse.put("isSuccess", false);
					jsonResponse.put("responseCode", 500);
					jsonResponse.put("responseMsg", depositResBean.getErrorMsg());
				}					
			}else{
				jsonResponse.put("isSuccess", false);
				jsonResponse.put("responseCode", 500);
				jsonResponse.put("responseMsg", LMSErrors.MIN_DEPOSIT_LIMIT_ERROR_MESSAGE+" "+olaMinDepositLimit);
			}
		}catch (LMSException e) {
			String errorMessage=e.getErrorCode()==0?e.getErrorMessage():LMSErrorProperty.getPropertyValue(e.getErrorCode());
			if(e.getErrorCode() == 10001){
				errorMessage = errorMessage+" "+e.getErrorMessage()+" ) !!";
			}				
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("responseCode", e.getErrorCode());
			jsonResponse.put("responseMsg", errorMessage);		
		}catch (GenericException e){
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("responseCode", e.getErrorCode());
			jsonResponse.put("responseMsg", errorMessage);		
		}
		logger.info("className: {} OLA Deposit Response Data : {}"+ jsonResponse);
		out.print(jsonResponse);
		out.flush();
		out.close();
	}
	
	public void withdrawalMoney() throws LMSException, IOException {
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		JsonObject json=null;
		int walletId=0;
		try{
			response.setContentType("application/json");
			out = response.getWriter();
			logger.info("className: {} OLA Withdrawal Request Data : {}"+ reqData);
			json=new JsonParser().parse(reqData).getAsJsonObject();
			setWithdrawalReqBean(new Gson().fromJson(json, OLAWithdrawalRequestBean.class));
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			ServletContext sc = ServletActionContext.getServletContext();
			String withdrawlAnyWhere = (String) sc.getAttribute("OLA_WITHDRAWL_ANYWHERE");
			logger.info("WithdrawlAnyWhere" + withdrawlAnyWhere);
			walletId=OLAUtility.getWalletId(withdrawalReqBean.getDevWalletName());
			OlaRetWithdrawlControllerImpl olaWithdrawalController = new OlaRetWithdrawlControllerImpl();
			if (withdrawalReqBean.getWithdrawlAmt() > 0) {
				withdrawalReqBean.setWithdrawlAnyWhere(withdrawlAnyWhere);
				withdrawalReqBean.setDeviceType("WEB");
				withdrawalReqBean.setWalletId(walletId);
				OLAWithdrawalResponseBean withdrawalResBean = olaWithdrawalController.olaRetPlrWithdrawal(withdrawalReqBean, userBean);
				if(withdrawalResBean.isSuccess()){
					jsonResponse.put("isSuccess", true);
					jsonResponse.put("responseCode",0);
					jsonResponse.put("responseMsg", "");
				}else{
					jsonResponse.put("isSuccess", false);
					jsonResponse.put("responseCode", 500);
					jsonResponse.put("responseMsg", withdrawalResBean.getErrorMsg());
				}
			}
		}catch (LMSException e) {
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("responseCode", e.getErrorCode());
			jsonResponse.put("responseMsg",errorMessage);
		}catch (GenericException e){
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("responseCode", e.getErrorCode());
			jsonResponse.put("responseMsg",errorMessage);
		}
		logger.info("className: {} OLA withdrawal Response Data : {}"+ jsonResponse);
		out.print(jsonResponse);
		out.flush();
		out.close();
	}
	

	
	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
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

	

	public void setWalletDetailsMap(Map<Integer, OlaWalletBean> walletDetailsMap) {
		this.walletDetailsMap = walletDetailsMap;
	}

	public Map<Integer, OlaWalletBean> getWalletDetailsMap() {
		return walletDetailsMap;
	}
	
	public void setDepositReqBean(OLADepositRequestBean depositReqBean) {
		this.depositReqBean = depositReqBean;
	}

	public OLADepositRequestBean getDepositReqBean() {
		return depositReqBean;
	}

	public OLAWithdrawalRequestBean getWithdrawalReqBean() {
		return withdrawalReqBean;
	}

	public void setWithdrawalReqBean(OLAWithdrawalRequestBean withdrawalReqBean) {
		this.withdrawalReqBean = withdrawalReqBean;
	}
	
	public int getMobileNumberlimit() {
		return mobileNumberlimit;
	}

	public void setMobileNumberlimit(int mobileNumberlimit) {
		this.mobileNumberlimit = mobileNumberlimit;
	}

	public String getReqData() {
		return reqData;
	}

	public void setReqData(String reqData) {
		this.reqData = reqData;
	}

	
}
