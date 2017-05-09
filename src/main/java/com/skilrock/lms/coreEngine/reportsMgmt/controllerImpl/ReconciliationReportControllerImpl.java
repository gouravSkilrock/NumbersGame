package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.ReconcileBean;

public class ReconciliationReportControllerImpl {
	static Log logger = LogFactory.getLog(ReconciliationReportControllerImpl.class);

	private static ReconciliationReportControllerImpl instance;

	private ReconciliationReportControllerImpl(){}

	public static ReconciliationReportControllerImpl getInstance() {
		if (instance == null) {
			synchronized (ReconciliationReportControllerImpl.class) {
				if (instance == null) {
					instance = new ReconciliationReportControllerImpl();
				}
			}
		}

		return instance;
	}

	public Map<String, List<String>> fetchMerchantWalletData() {
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		ServiceRequest request = new ServiceRequest();
		ServiceResponse response = new ServiceResponse();

		Map<String, List<String>> merchantWalletMap = null;
		try {
			request.setServiceName(ServiceName.REPORTS_MGMT);
			request.setServiceMethod(ServiceMethodName.FETCH_MERCHANT_WALLET_DATA);
			response = delegate.getResponse(request);
			if(response.getResponseData() == null)
				throw new LMSException(LMSErrors.DATA_UNAVAILABLE_ERROR_CODE, LMSErrors.DATA_UNAVAILABLE_ERROR_MESSAGE);
			
			Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
			merchantWalletMap = new Gson().fromJson((JsonElement)response.getResponseData(), type);
		} catch (LMSException e) {
			logger.info(e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return merchantWalletMap;
	}

	public List<ReconcileBean> fetchMerchantTransactions(String merchantName, String transactionType, String walletType, String startDate, String endDate, String status) throws LMSException {
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		ServiceRequest request = new ServiceRequest();
		ServiceResponse response = new ServiceResponse();

		List<ReconcileBean> reconcileList = null;
		try {
			JsonObject requestObject = new JsonObject();
			requestObject.addProperty("merchantName", merchantName);
			requestObject.addProperty("transactionType", transactionType);
			requestObject.addProperty("walletType", walletType);
			requestObject.addProperty("startDate", startDate);
			requestObject.addProperty("endDate", endDate);
			requestObject.addProperty("status", status);
			request.setServiceData(requestObject);
			request.setServiceName(ServiceName.REPORTS_MGMT);
			request.setServiceMethod(ServiceMethodName.FETCH_MERCHANT_TRANSACTIONS);

			response = delegate.getResponse(request);
			
			if(!response.getIsSuccess()){
				String responseData =  response.getResponseData().toString();
				logger.info("response object : " + responseData);
				if(responseData.contains("4052")){
					throw new LMSException(Integer.parseInt(responseData.split(":")[0].replaceAll("\"", "").trim()), responseData.split(":")[1].replaceAll("\"", "").trim());
				}
			}
			
			if(response.getResponseData() == null)
				throw new LMSException(LMSErrors.DATA_UNAVAILABLE_ERROR_CODE, LMSErrors.DATA_UNAVAILABLE_ERROR_MESSAGE);
			
			Type type = new TypeToken<List<ReconcileBean>>(){}.getType();
			reconcileList = new Gson().fromJson((JsonElement)response.getResponseData(), type);
		} catch (LMSException e) {
			logger.info(e.getErrorMessage());
			throw e;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reconcileList;
	}

	public void processRequest(String merchantName, String action, Map<Long, String> transMap) {
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		ServiceRequest request = new ServiceRequest();
		ServiceResponse response = new ServiceResponse();
		try {
			JsonObject requestObject = new JsonObject();
			requestObject.addProperty("merchantName", merchantName);
			requestObject.addProperty("action", action);
			requestObject.add("transIds", new Gson().toJsonTree(transMap));
			request.setServiceData(requestObject);
			request.setServiceName(ServiceName.REPORTS_MGMT);
			request.setServiceMethod(ServiceMethodName.PROCESS_RECONCILIATION_DATA);

			response = delegate.getResponse(request);
			logger.info("Response - "+response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}