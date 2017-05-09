package com.skilrock.lms.coreEngine.virtualSport.common.serviceImpl;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPSaleRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPTxRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;
import com.skilrock.lms.coreEngine.virtualSport.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.playMgmt.controllerImpl.VirtualSportGamePlayControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.pwtMgmt.controllerImpl.PayPwtTicketControllerImpl;
import com.skilrock.lms.rest.services.bean.TPPwtRequestBean;
import com.skilrock.lms.rest.services.bean.TPPwtResponseBean;
import com.skilrock.lms.rest.services.common.ReqResParser;

@Path("/virtualBetting")
public class TpVirtualSportsServiceImpl{

	private static Logger logger = LoggerFactory.getLogger(TpVirtualSportsServiceImpl.class);

	/**
	 * 
	 * @param data
	 * @param sign
	 * @return resObj
	 * @author Rishi
	 */
	@POST
	@Path("/getCredit")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getCredit(@FormParam("t") String data,@FormParam("sign") String sign) {
		CommonMethodsControllerImpl controllerObj = null;
		String reqId = "";
		JSONObject innerRespObj = new JSONObject();
		JSONObject resObj = new JSONObject();
		double credit = 0.0;
		int unitId = 0;
		JsonObject reqObj = null;
		try {
			logger.info("Get Credit API Request Data: " + data.toString());
			controllerObj = CommonMethodsControllerImpl.getInstance();
			if (!controllerObj.authenticateRequest(data, sign)) {
				throw new VSException(VSErrors.AUTHENTICATION_FAILED_ERROR_CODE,VSErrors.AUTHENTICATION_FAILED_ERROR_MESSAGE);
			}
			
			if(data == null || data.trim().length()<1){
				throw new VSException(VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE); }
			 
			reqObj = new JsonParser().parse(data).getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entries = reqObj.entrySet();
			for (Map.Entry<String, JsonElement> entry : entries) {
				reqId = entry.getKey();
			}
			if (reqObj.getAsJsonObject(reqId).get("unit_id") == null || reqObj.getAsJsonObject(reqId).get("unit_id").toString().replace("\"", "").isEmpty()) {
				throw new VSException(VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE,VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
			}
			unitId = reqObj.getAsJsonObject(reqId).get("unit_id").getAsInt();
			
			credit = CommonMethodsControllerImpl.getInstance().fetchUserCredit(unitId);
			
			innerRespObj.put("result", "success");
			innerRespObj.put("credit", credit);
			innerRespObj.put("ext_id", "0");
			innerRespObj.put("unit_id", unitId);
			innerRespObj.put("currency",LMSUtility.sc.getAttribute("CURRENCY_SYMBOL"));
			resObj.put(reqId, innerRespObj);
		} catch (VSException vb) {
			vb.printStackTrace();
			innerRespObj.put("result", "error");
			innerRespObj.put("error_id", vb.getErrorCode());
			innerRespObj.put("error_message", vb.getErrorMessage());
			resObj.put(reqId, innerRespObj);
		} catch (Exception e) {
			e.printStackTrace();
			innerRespObj.put("result", "error");
			innerRespObj.put("error_id", VSErrors.INTERNAL_SYSTEM_ERROR_CODE);
			innerRespObj.put("error_message",VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
			resObj.put(reqId, innerRespObj);
		}
		logger.info("Get Credit API Response Data: " + resObj.toString());
		return resObj.toString();
	}
	
	/**
	 * 
	 * @param reqData
	 * @param sign
	 * @return resObj1
	 * @author Nikhil K. Bansal
	 */
	@POST
	@Path("/spend")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String spend(@FormParam("t") String reqData,@FormParam("sign") String sign) {
		CommonMethodsControllerImpl controllerObj=null;
		UserInfoBean userInfoBean = null;
		JsonArray arr=new JsonArray();
		JsonObject js=new JsonObject();
		try {
			logger.info("Spend Request data" + reqData);
			controllerObj = CommonMethodsControllerImpl.getInstance();
			if (!controllerObj.authenticateRequest(reqData, sign)) {
				throw new VSException(VSErrors.AUTHENTICATION_FAILED_ERROR_CODE,VSErrors.AUTHENTICATION_FAILED_ERROR_MESSAGE);
			}
			
			if(reqData == null || reqData.trim().isEmpty()){
				throw new VSException(VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE); 
			}
			ReqResParser parSer = ReqResParser.getInstance();
			TPSaleRequestBean tpTransactionBean = parSer.fetchReqForVBTx(reqData);
			
			VirtualSportGamePlayControllerImpl.getInstance().virtualSportsPurchaseTicket(userInfoBean, tpTransactionBean);
			js.addProperty("result", "success");
			js.addProperty("new_credit", tpTransactionBean.getRetBalanceAfterSale());
			js.addProperty("old_credit", tpTransactionBean.getRetBalanceBeforeSale());
			js.addProperty("unit_id", tpTransactionBean.getUnitId());
			js.addProperty("tmp_id", tpTransactionBean.getTmpId());
			arr.add(js);
			logger.info("Spend response Data"+new Gson().toJson(arr));
		} catch (VSException e) {
			js.addProperty("result", "error");
			js.addProperty("error_id", e.getErrorCode());
			js.addProperty("error_message",e.getErrorMessage());
			arr.add(js);
			
		}catch (Exception e) {
			js.addProperty("result", "error");
			js.addProperty("error_id", VSErrors.GAME_NOT_AVAILABLE_ERROR_CODE);
			js.addProperty("error_message",VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			arr.add(js);
			
		}
		return new Gson().toJson(arr);
	}

	@POST
	@Path("/confirm")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String confirm(@FormParam("t") String data,@FormParam("sign") String sign) {
		JsonObject reqObj = null;
		CommonMethodsControllerImpl controllerObj = null;
		JSONObject innerRespObj = null;
		JSONObject respObj = null;
		String ticketNumber = "";
		TPTxRequestBean reqBean = null;
		boolean isLoginReq = false;
		try {
			logger.info("Confirm API request data - " + data);
			reqBean = new TPTxRequestBean();
			innerRespObj = new JSONObject();
			respObj = new JSONObject();
			controllerObj = CommonMethodsControllerImpl.getInstance();
			if (!controllerObj.authenticateRequest(data, sign)) {
				throw new VSException(VSErrors.AUTHENTICATION_FAILED_ERROR_CODE,VSErrors.AUTHENTICATION_FAILED_ERROR_MESSAGE);
			}
			
			if (data == null || data.trim().length() < 1) {
				throw new VSException(VSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,VSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			reqObj = new JsonParser().parse(data).getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entries = reqObj.entrySet();
			for (Map.Entry<String, JsonElement> entry : entries) {
				ticketNumber = entry.getKey();
			}
			reqBean.setTicketNumber(ticketNumber);
			reqBean.setEngineTxId(reqObj.getAsJsonObject(ticketNumber).get("tmp_id").getAsString());
			reqBean.setTxAmount(reqObj.getAsJsonObject(ticketNumber).get("amount").getAsDouble());
			reqBean.setEstimatedMaxWin(reqObj.getAsJsonObject(ticketNumber).get("estimated_max_win")==null?0.0:reqObj.getAsJsonObject(ticketNumber).get("estimated_max_win").getAsDouble());
			reqBean.setEventInfoArray(reqObj.getAsJsonObject(ticketNumber).get("events")==null?null:reqObj.getAsJsonObject(ticketNumber).get("events").getAsJsonArray());
			if(reqObj.getAsJsonObject(ticketNumber).get("unit_id").isJsonNull()){
				reqBean.setUnitId(reqObj.getAsJsonObject(ticketNumber).get("staff_id").getAsInt());
			} else{
				isLoginReq = true;
				reqBean.setUnitId(reqObj.getAsJsonObject(ticketNumber).get("unit_id").getAsInt());
			}
			// Update ticket number and status
			VirtualSportGamePlayControllerImpl.getInstance().updateTicketInfo(reqBean,isLoginReq);
			innerRespObj.put("result", "success");
			respObj.put(ticketNumber, innerRespObj);
		} catch (VSException e) {
			e.printStackTrace();
			innerRespObj.put("result", "error");
			innerRespObj.put("error_id", e.getErrorCode());
			innerRespObj.put("error_message", e.getErrorMessage());
			respObj.put(ticketNumber, innerRespObj);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			innerRespObj.put("result", "error");
			innerRespObj.put("error_id", VSErrors.INTERNAL_SYSTEM_ERROR_CODE);
			innerRespObj.put("error_message",VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
			respObj.put(ticketNumber, innerRespObj);
		}
		logger.info("Confirm API response data  - " + respObj.toString());
		return respObj.toString();
	}

	/**
	 * 
	 * @param data
	 * @param sign
	 * @return resObj1
	 * @author Rishi
	 */
	@POST
	@Path("/payout")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String payout(@FormParam("t") String data,@FormParam("sign") String sign) {
		CommonMethodsControllerImpl controllerObj = null;
		JSONObject innerRespObj = null;
		JSONObject respObj = null;
		double totalAmount = 0.0;
		int unitId = 0;
		String ticketNumber = null;
		TPPwtRequestBean requestBean = null;
		TPPwtResponseBean responseBean = null;
		TPSaleRequestBean saleRequestBean = null;
		UserInfoBean userInfoBean = null;
		String status = null;
		try {
			innerRespObj = new JSONObject();
			respObj = new JSONObject();
			logger.info("Payout API Request Data: "+data.toString());
			controllerObj = CommonMethodsControllerImpl.getInstance();
			if (!controllerObj.authenticateRequest(data, sign)) {
				throw new VSException(VSErrors.AUTHENTICATION_FAILED_ERROR_CODE,VSErrors.AUTHENTICATION_FAILED_ERROR_MESSAGE);
			}
			if(data == null || data.trim().length()<1){
				throw new VSException(VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
			}
			JsonObject reqObj = new JsonParser().parse(data).getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entries = reqObj.entrySet();
			for (Map.Entry<String, JsonElement> entry : entries) {
				ticketNumber = entry.getKey();
			}
			if(reqObj.getAsJsonObject(ticketNumber).get("unit_id") == null || reqObj.getAsJsonObject(ticketNumber).get("unit_id").toString().replace("\"", "").isEmpty() || reqObj.getAsJsonObject(ticketNumber).get("amount") == null || reqObj.getAsJsonObject(ticketNumber).get("amount").toString().replace("\"", "").isEmpty() || reqObj.getAsJsonObject(ticketNumber).get("status") == null || reqObj.getAsJsonObject(ticketNumber).get("status").toString().replace("\"", "").isEmpty()){
				throw new VSException(VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, VSErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
			}
			totalAmount = reqObj.getAsJsonObject(ticketNumber).get("amount").isJsonNull()?0.0:reqObj.getAsJsonObject(ticketNumber).get("amount").getAsDouble();
			unitId = reqObj.getAsJsonObject(ticketNumber).get("unit_id").getAsInt();
			status = reqObj.getAsJsonObject(ticketNumber).get("status").getAsString();
			if("won".equalsIgnoreCase(status)){
	
				requestBean = new TPPwtRequestBean();
				requestBean.setTicketNumber(ticketNumber);
				requestBean.setTotalAmount(totalAmount);
				responseBean = new PayPwtTicketControllerImpl().retailerNormalPay(unitId, requestBean, "VS", "WEB");
				
				innerRespObj.put("result", "success");
				innerRespObj.put("ext_id", "0");
				innerRespObj.put("unit_id", unitId);
				innerRespObj.put("currency", LMSUtility.sc.getAttribute("CURRENCY_SYMBOL"));
				innerRespObj.put("amount", totalAmount);
				innerRespObj.put("taxes", "0.00");
				innerRespObj.put("new_credit", responseBean.getBalance());
				innerRespObj.put("old_credit", responseBean.getOldBalance());
				respObj.put(ticketNumber, innerRespObj);
			} else if("cancel".equalsIgnoreCase(status) || "rollback".equalsIgnoreCase(status)){
				//refund sale transaction
				saleRequestBean = new TPSaleRequestBean();
				saleRequestBean.setUnitId(unitId);
				//saleRequestBean.setAmount(totalAmount);
				saleRequestBean.setTicketNumber(ticketNumber);
				VirtualSportGamePlayControllerImpl.getInstance().virtualSportsRefund(userInfoBean, saleRequestBean);
				
				innerRespObj.put("result", "success");
				innerRespObj.put("ext_id", "0");
				innerRespObj.put("unit_id", unitId);
				innerRespObj.put("currency", LMSUtility.sc.getAttribute("CURRENCY_SYMBOL"));
				innerRespObj.put("amount", totalAmount);
				innerRespObj.put("taxes", "0.00");
				innerRespObj.put("new_credit", saleRequestBean.getRetBalanceAfterSale());
				innerRespObj.put("old_credit", saleRequestBean.getRetBalanceBeforeSale());
				respObj.put(ticketNumber, innerRespObj);
			}
			
		} catch (VSException vb){
			vb.printStackTrace();
			innerRespObj.put("result","error");
			innerRespObj.put("error_id", vb.getErrorCode());
			innerRespObj.put("error_message", vb.getErrorMessage());
			innerRespObj.put("ext_id", "0");
			innerRespObj.put("unit_id", unitId);
			innerRespObj.put("currency", LMSUtility.sc.getAttribute("CURRENCY_SYMBOL"));
			innerRespObj.put("amount", totalAmount);
			respObj.put(ticketNumber, innerRespObj);
		} catch (Exception e) {
			e.printStackTrace();
			innerRespObj.put("result","error");
			innerRespObj.put("error_id", VSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			innerRespObj.put("error_message", VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			innerRespObj.put("ext_id", "0");
			innerRespObj.put("unit_id", unitId);
			innerRespObj.put("currency", LMSUtility.sc.getAttribute("CURRENCY_SYMBOL"));
			innerRespObj.put("amount", totalAmount);
			respObj.put(ticketNumber, innerRespObj);
		} finally{
			if (respObj.isEmpty()) {
				innerRespObj.put("result","error");
				innerRespObj.put("error_id", VSErrors.GENERAL_EXCEPTION_ERROR_CODE);
				innerRespObj.put("error_message", VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				innerRespObj.put("ext_id", "0");
				innerRespObj.put("unit_id", unitId);
				innerRespObj.put("currency", LMSUtility.sc.getAttribute("CURRENCY_SYMBOL"));
				innerRespObj.put("amount", totalAmount);
				respObj.put(ticketNumber, innerRespObj);
			}
			logger.info("Payout API Response Data: "+respObj.toString());
		}
		return respObj.toString();
	}

	
}
