package com.skilrock.ola.web.userMgmt.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
import com.skilrock.lms.coreEngine.ola.common.OLAConstants;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.javaBeans.CountryDataBean;
import com.skilrock.ola.javaBeans.OlaWalletBean;
import com.skilrock.ola.userMgmt.controllerImpl.OlaPlrRegistrationControllerImpl;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationResponseBean;


public class RetailerPlayerRegistrationAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(RetailerPlayerRegistrationAction.class);
	HttpServletRequest request;
	HttpServletResponse response;	
	private String walletName;	
	private String username;
	private String email;
	private String userPhone;
	private String countryData;
	private OLADepositRequestBean depositReqBean;
	private OlaPlayerRegistrationRequestBean playerBean;
	private Map<Integer, OlaWalletBean> walletDetailsMap=null;
	private int mobileNumberlimit;
	private String regReqData;
	
	public String fetchPlayerRegisterMenu(){				
		List<CountryDataBean> countryBeanList = null;
		ServletContext sc = ServletActionContext.getServletContext();
		try {
			setWalletDetailsMap(new OlaCommonMethodControllerImpl().getWalletDetails());
			countryBeanList = new OlaPlrRegistrationControllerImpl().getCountryListMap();
			String js = new Gson().toJson(countryBeanList);
			setCountryData(js);
			mobileNumberlimit = Integer.parseInt((String)sc.getAttribute("OLA_PLAYER_MOBILE_NUMBER_LIMIT"));
		} catch (LMSException e) {
			System.out.println(e.getErrorCode());
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);				
		}catch (GenericException e){
			System.out.println(e.getErrorCode());
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);
		}
		return SUCCESS;
	}
	
	public void checkAvlOrgUser() throws LMSException {
		PrintWriter out = null;
		JSONObject resObject=new JSONObject();
		boolean isSuccess = false;
		try {
				response.setContentType("application/json");
				out = response.getWriter();
				OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
			
				if(walletName.equalsIgnoreCase("KhelPlayRummy")){
					/*OlaHelper olaHelper = new OlaHelper();
					if(username!=null){
						errorMap=olaHelper.verifyPlrName(username,walletId,"USER_AVAILABILITY");
					}
					if(email!=null){
						errorMap = helper.verifyEmailForKpRummy(email,walletId);
					}
					if(phone!=null){
						errorMap = helper.verifyPhoneForKpRummy(phone, walletId);
					}*/
				}else if(walletName.equalsIgnoreCase("RUMMY")){
					/*if(username!=null){
						errorMap = helper.verifyOrgName(username);
					}
					if(email!=null){
						 errorMap = helper.verifyEmail(email);
					}
					 */
				}else if(walletName.equalsIgnoreCase("PLAYER_LOTTERY")){
					if(username != null){
						isSuccess=olaCommonMethodController.verifyUserName(username,walletName);
					}
				}
				resObject.put("isSuccess", isSuccess);
				if(isSuccess){
					resObject.put("message",LMSErrors.USER_NAME_ALREADY_EXIST_MESSAGE);
				}else{
					resObject.put("message",LMSErrors.USER_NAME_AVAL_MESSAGE);					
				}
			} catch (IOException e) {
				e.printStackTrace();		
			} catch (LMSException e) {}
			out.print(resObject);
			out.flush();
			out.close();
	}
	
	
	
	public void retPlayerRegistration() throws IOException{
		//String rootPath = (String) session.getAttribute("ROOT_PATH");
		OlaPlayerRegistrationResponseBean regRespBean = null;
		int walletId=0;
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		JsonObject js=null;
		try{
			response.setContentType("application/json");
			out=response.getWriter();
			ServletContext sc = ServletActionContext.getServletContext();		
			HttpSession session = getRequest().getSession();
			js=new JsonParser().parse(regReqData).getAsJsonObject();
			setPlayerBean(new Gson().fromJson(js, OlaPlayerRegistrationRequestBean.class));
			setDepositReqBean(new Gson().fromJson(js.get("depositData").getAsJsonObject(), OLADepositRequestBean.class));
			walletId=OLAUtility.getWalletId(playerBean.getWalletName());
			String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			double olaMinDepositLimit = OLAUtility.getOlaWalletDataMap().get(walletId).getMinDeposit();
				//settingData IntoDepositRequestBean
				depositReqBean.setDepositAnyWhere(depositAnyWhere);
				depositReqBean.setWalletDevName(playerBean.getWalletName());
				
					OlaPlrRegistrationControllerImpl plrRegController = new OlaPlrRegistrationControllerImpl();
					if(("TabletGaming".equalsIgnoreCase(playerBean.getWalletName()) ||"GroupRummy".equalsIgnoreCase(playerBean.getWalletName()) || "PLAYER_LOTTERY".equalsIgnoreCase(playerBean.getWalletName())||"KhelPlayRummy".equalsIgnoreCase(playerBean.getWalletName()) || "ALA_WALLET".equalsIgnoreCase(playerBean.getWalletName())) && depositReqBean.getDepositAmt()!=0 && depositReqBean.getDepositAmt() < olaMinDepositLimit){
						jsonResponse.put("isSuccess", false);
						jsonResponse.put("responseCode",500);
						jsonResponse.put("responseMsg", LMSErrors.MIN_DEPOSIT_LIMIT_ERROR_MESSAGE+" "+olaMinDepositLimit);
					}else{
						depositReqBean.setDeviceType("WEB");
						playerBean.setWalletId(walletId);
						depositReqBean.setWalletId(walletId);
						if("TabletGaming".equalsIgnoreCase(playerBean.getWalletName()) || "GroupRummy".equalsIgnoreCase(playerBean.getWalletName()) ||"KhelPlayRummy".equalsIgnoreCase(playerBean.getWalletName()) || "ALA_WALLET".equalsIgnoreCase(playerBean.getWalletName())){
							playerBean.setCountry(OLAConstants.OLA_TABLETGAMING_COUNTRY_CODE);
							playerBean.setRequestIp(request.getRemoteAddr());
						}
						regRespBean = plrRegController.registerPlayer(depositReqBean, userBean, playerBean);
						if(regRespBean.isSuccess()){
							jsonResponse.put("isSuccess", true);
							jsonResponse.put("responseCode",0);
							jsonResponse.put("username", playerBean.getUsername());
							jsonResponse.put("phone", playerBean.getPhone());
							jsonResponse.put("password", playerBean.getPassword());
							jsonResponse.put("plrRegDate", playerBean.getPlrRegDate());
							jsonResponse.put("depositAmt",depositReqBean.getDepositAmt()>0?depositReqBean.getDepositAmt():0);
							jsonResponse.put("email", playerBean.getEmail());
							jsonResponse.put("retailerName", userBean.getUserName());
							jsonResponse.put("responseMsg", "");
						}else{
							jsonResponse.put("isSuccess", false);
							jsonResponse.put("responseCode",500);
							jsonResponse.put("responseMsg", "");
						}
					}
					
							
		}catch (LMSException e) {
			String errorMessage=e.getErrorCode()==0?e.getErrorMessage():LMSErrorProperty.getPropertyValue(e.getErrorCode());
			if(e.getErrorCode() == 10001){
				errorMessage = errorMessage+" "+e.getErrorMessage()+" ) !!";
			}				
			logger.info(e.getErrorCode()+errorMessage);
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("responseCode",e.getErrorCode());
			jsonResponse.put("responseMsg", errorMessage);			
		}catch (GenericException e){
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("responseCode",e.getErrorCode());
			jsonResponse.put("responseMsg", errorMessage);	
		}
		logger.info("className: {} OLA Registration Response Data : {}"+ jsonResponse);
		out.print(jsonResponse);
		out.flush();
		out.close();
	}
	
	
	public void setRegistrationMenuData() throws LMSException, GenericException{
		List<CountryDataBean> countryBeanList = null;
		setWalletDetailsMap(new OlaCommonMethodControllerImpl().getWalletDetails());
		countryBeanList = new OlaPlrRegistrationControllerImpl().getCountryListMap();
		String js = new Gson().toJson(countryBeanList);
		setCountryData(js);
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
	
	public void setCountryData(String countryData) {
		this.countryData = countryData;
	}

	public String getCountryData() {
		return countryData;
	}
	
	

	public OlaPlayerRegistrationRequestBean getPlayerBean() {
		return playerBean;
	}

	public void setPlayerBean(OlaPlayerRegistrationRequestBean playerBean) {
		this.playerBean = playerBean;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public void setDepositReqBean(OLADepositRequestBean depositReqBean) {
		this.depositReqBean = depositReqBean;
	}

	public OLADepositRequestBean getDepositReqBean() {
		return depositReqBean;
	}

	public void setMobileNumberlimit(int mobileNumberlimit) {
		this.mobileNumberlimit = mobileNumberlimit;
	}

	public int getMobileNumberlimit() {
		return mobileNumberlimit;
	}

	public String getRegReqData() {
		return regReqData;
	}

	public void setRegReqData(String regReqData) {
		this.regReqData = regReqData;
	}
	
	
}
