package com.skilrock.lms.ebet.Action;

import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.TpEBetDataBean;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;

public class EbetPlayAction extends BaseAction {
	
	/**
	 * 
	 */
	private static Logger logger = LoggerFactory.getLogger(EbetPlayAction.class);
	private static final long serialVersionUID = -3181544472939481546L;
	private String requestData;

	public EbetPlayAction() {
		super(EbetPlayAction.class.getName());
		
	}
	public String getRequestData() {
		return requestData;
	}
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
	
	public void fetchActiveBetSlip() {
		JSONObject responseJson = new JSONObject();
		PrintWriter out = null;
		try {
			if(requestData == null || requestData.trim().isEmpty()){
   				throw new LMSException(LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
   			}
   			JsonObject requestJson = new JsonParser().parse(requestData).getAsJsonObject();
   			out = response.getWriter();
   			
   			if(requestJson.get("username")==null || requestJson.get("username").getAsString().trim().isEmpty()){
   				throw new LMSException(LMSErrors.USER_NAME_DOES_NOT_EXISTS_CODE,LMSErrors.USER_NAME_DOES_NOT_EXISTS_MESSAGE);
   			}
   			TpEBetDataBean tpEBetDataBean = new TpEBetDataBean();
   			if(requestJson.get("serviceCode")==null || requestJson.get("serviceCode").getAsString().trim().isEmpty()){
   			    tpEBetDataBean.setSaleType("");
   			}else{
   			    tpEBetDataBean.setSaleType(requestJson.get("serviceCode").getAsString());
   			}
   			tpEBetDataBean.setUserName(requestJson.get("username").getAsString());
   			if(requestJson.get("deviceId")==null || requestJson.get("deviceId").getAsString().trim().isEmpty()){
   			    tpEBetDataBean.setDeviceId("");
   			}else{
   			    tpEBetDataBean.setDeviceId(requestJson.get("deviceId").getAsString());
   			}
   			List<TpEBetDataBean> betDataList  = TpEBetMgmtDaoImpl.getInstance().fetchSaleData(tpEBetDataBean); 
   			responseJson.put("errorCode",0);
   			responseJson.put("errorMsg","");
   			responseJson.put("isSuccess","true");
   			responseJson.put("responseData",betDataList);
			
		} catch (LMSException e) {
			logger.error("Exception {}",e);
			responseJson.put("errorCode",e.getErrorCode());
			responseJson.put("errorMsg",e.getErrorMessage());
			responseJson.put("isSuccess","false");
		} catch (Exception e) {
			logger.error("Exception {}",e);
   			responseJson.put("isSuccess","false");
   			responseJson.put("errorCode",LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
   			responseJson.put("errorMsg",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			out.print(responseJson);
			out.close();
			out.flush();
			
		}
		
		
	}
	
	public void cancelBetSlip() {
		JSONObject responseJson = new JSONObject();
		JsonObject requestJson = null;
		PrintWriter out = null;
		try {
			if (requestData == null || requestData.trim().isEmpty()) {
				throw new LMSException(LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			requestJson = new JsonParser().parse(requestData).getAsJsonObject();
			out = response.getWriter();
			if (requestJson.get("requestId").getAsString()==null||requestJson.get("requestId").getAsInt()== 0) {
				throw new LMSException(LMSErrors.INVALID_REQUESTID_ERROR_CODE,LMSErrors.INVALID_REQUESTID_ERROR_MESSAGE);
			}
			int requestId =requestJson.get("requestId").getAsInt();
			TpEBetMgmtDaoImpl.getInstance().cancelEBetSaleData(requestId);
			responseJson.put("errorCode", 0);
			responseJson.put("errorMsg", "");
			responseJson.put("isSuccess", "true");
		} catch (LMSException e) {
			logger.info("Exception {}", e);
			responseJson.put("errorCode", e.getErrorCode());
			responseJson.put("errorMsg", e.getErrorMessage());
			responseJson.put("isSuccess", "false");
		} catch (Exception e) {
			logger.info("Exception {}", e);
			responseJson.put("isSuccess", "false");
			responseJson.put("errorCode",LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseJson.put("errorMsg",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			
		} finally {
			out.print(new Gson().toJson(responseJson));
			out.close();
			out.flush();
		}
		
		
	}
	

}
