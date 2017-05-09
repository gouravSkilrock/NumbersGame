package com.skilrock.lms.rest.services.common;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.TpEBetDataBean;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;

@Path("/tpEBetMgmt")

public class TpEBetMgmt {
    private static Logger logger = LoggerFactory.getLogger(TpEBetMgmt.class);

	
    @Path("/saveSaleRequestData")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	/**
	 * @author Amit Kumar
	 * 
	 * @ API will be used to Save Sale Request from Tablet
	 * 
	 */
	 
	public String saveSaleRequestData(String requestData){
		JSONObject responseJson=new JSONObject();
		try{
			if(requestData==null || requestData.trim().isEmpty()){
				throw new LMSException(LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			JsonObject requestJson=new JsonParser().parse(requestData).getAsJsonObject();
			if(requestJson.get("serviceCode")==null || requestJson.get("serviceCode").getAsString().trim().isEmpty()){
				throw new LMSException(LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE,LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE);
			}
			if(requestJson.get("deviceId")==null || requestJson.get("deviceId").getAsString().trim().isEmpty()){
				throw new LMSException(LMSErrors.INVALID_DEVICEID_ERROR_CODE,LMSErrors.INVALID_DEVICEID_ERROR_MESSAGE);
			}
			if(requestJson.get("saleData")==null || requestJson.get("saleData").getAsJsonObject().isJsonNull()){
				throw new LMSException(LMSErrors.INVALID_SALE_ERROR_CODE,LMSErrors.INVALID_SALE_ERROR_MESSAGE);
			}
			
			TpEBetDataBean tpEBetDataBean = new TpEBetDataBean();
			tpEBetDataBean.setSaleType(requestJson.get("serviceCode").getAsString());
			tpEBetDataBean.setRequestData(requestJson.get("saleData").getAsJsonObject().toString());
			tpEBetDataBean.setDeviceId(requestJson.get("deviceId").getAsString());
			if(!(requestJson.get("mobileNumber") == null || requestJson.get("mobileNumber").getAsString().trim().isEmpty())){
				tpEBetDataBean.setMobileNumber(requestJson.get("mobileNumber").getAsString());
   			}
			TpEBetMgmtDaoImpl.getInstance().fetchEBetTokenId(tpEBetDataBean); 
			responseJson.put("errorCode",0);
			responseJson.put("errorMsg","");
			responseJson.put("tokenId",tpEBetDataBean.getTokenId());
			responseJson.put("isSuccess","true");

		}catch(LMSException e){
			logger.error("Exception {}",e);
			responseJson.put("errorCode",e.getErrorCode());
			responseJson.put("errorMsg",e.getErrorMessage());
			responseJson.put("isSuccess","false");
		}catch(Exception e){
			logger.error("Exception {}",e);
			responseJson.put("errorCode",LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseJson.put("errorMsg",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return new Gson().toJson(responseJson);
	}
    @Path("/fetchEBetSaleRequest")
   	@POST
   	@Produces(MediaType.APPLICATION_JSON)
   	@Consumes(MediaType.APPLICATION_JSON)
    	/**
	 * @author Amit Kumar
	 * 
	 * @ API will be used to Fetch Sale Request for retailer terminal
	 * @
	 * 
	 */
   	public JSONObject fetchSaleRequestData(String requestData){
   		JSONObject responseJson=new JSONObject();
   		try{
   			if(requestData==null || requestData.trim().isEmpty()){
   				throw new LMSException(LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
   			}
   			JsonObject requestJson=new JsonParser().parse(requestData).getAsJsonObject();
   			
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
   		}catch(LMSException e){
			logger.error("Exception {}",e);
			responseJson.put("errorCode",e.getErrorCode());
			responseJson.put("errorMsg",e.getErrorMessage());
			responseJson.put("isSuccess","false");
		}catch(Exception e){
   			logger.error("Exception {}",e);
   			responseJson.put("isSuccess","false");
   			responseJson.put("errorCode",LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
   			responseJson.put("errorMsg",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
   		}
   		return responseJson;
   	}

@Path("/cancelEBetSaleRequest")
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/**
 * 
 */
public String cancelSaleRequestData(String requestData) {

	
	JSONObject responseJson = new JSONObject();
	JsonObject requestJson = new JsonParser().parse(requestData).getAsJsonObject();
	
	try {
		if (requestData == null || requestData.trim().isEmpty()) {
			throw new LMSException(LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
		}
		if (requestJson.get("requestId").getAsString()==null||requestJson.get("requestId").getAsInt()==0){
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
		
	}
	return new Gson().toJson(responseJson);
}

}