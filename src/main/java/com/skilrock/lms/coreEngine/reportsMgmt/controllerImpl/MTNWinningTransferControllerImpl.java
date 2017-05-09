package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.lang.reflect.Type;
import java.util.List;

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
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.PendingWinningTransferDataBean;

public class MTNWinningTransferControllerImpl {
	
	static Log logger = LogFactory.getLog(ReconciliationReportControllerImpl.class);

	private static MTNWinningTransferControllerImpl instance;

	private MTNWinningTransferControllerImpl(){}

	public static MTNWinningTransferControllerImpl getInstance() {
		if (instance == null) {
			synchronized (MTNWinningTransferControllerImpl.class) {
				if (instance == null) {
					instance = new MTNWinningTransferControllerImpl();
				}
			}
		}

		return instance;
	}

	public List<PendingWinningTransferDataBean> fetchPendingData(String gameNo,
			String startDate, String endDate) throws LMSException {
		IServiceDelegate delegate = ServiceDelegate.getInstance();
			ServiceRequest request = new ServiceRequest();
			ServiceResponse response = new ServiceResponse();

			List<PendingWinningTransferDataBean> pendingWinningTransferDrawData= null;
			try {
				JsonObject requestObject = new JsonObject();
				requestObject.addProperty("gameNo", gameNo);
				requestObject.addProperty("startDate", startDate);
				requestObject.addProperty("endDate", endDate);
				request.setServiceData(requestObject);
				request.setServiceName(ServiceName.REPORTS_MGMT);
				request.setServiceMethod(ServiceMethodName.FETCH_PENDING_WINNING_TRANSFER_TRANSACTIONS);

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
				
				Type type = new TypeToken<List<PendingWinningTransferDataBean>>(){}.getType();
				pendingWinningTransferDrawData = new Gson().fromJson((JsonElement)response.getResponseData(), type);
			} catch (LMSException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				
			}

			return pendingWinningTransferDrawData;
	}

	public void pushPendingWinning(String gameNo, String drawId) {
		
		IServiceDelegate delegate = ServiceDelegate.getInstance() ;
		ServiceRequest request = new ServiceRequest();
		ServiceResponse response = new ServiceResponse() ;
		
		try {
			JsonObject requestObject = new JsonObject() ;
			requestObject.addProperty("gameId", gameNo) ;
			requestObject.addProperty("drawId", drawId) ;
			request.setServiceData(requestObject) ;
			request.setServiceName(ServiceName.REPORTS_MGMT);
			request.setServiceMethod(ServiceMethodName.PUSH_PENDING_WINNINGS);
			
			response = delegate.getResponse(request) ;
			if(response.getResponseData() == null)
				throw new LMSException(LMSErrors.DATA_UNAVAILABLE_ERROR_CODE, LMSErrors.DATA_UNAVAILABLE_ERROR_MESSAGE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
