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
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.UssdSubscriberDataBean;

public class UssdSubscriberWiseReportControllerImpl {
	static Log logger = LogFactory.getLog(UssdSubscriberWiseReportControllerImpl.class);
	
	public List<UssdSubscriberDataBean> fetchUssdSubscriberData(String merchantName, String mobileNbr, String startDate, String endDate,int gameId,String drawName,String winStatus) throws LMSException, Exception {
		List<UssdSubscriberDataBean> mtnCustomerCenterBeans= null;
		JsonObject reqObj = new JsonObject();
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_USSD_SUBSCRIBER_DATA);

		reqObj.addProperty("merchantName", merchantName);
		reqObj.addProperty("mobileNbr", mobileNbr);
		reqObj.addProperty("startDate", startDate);
		reqObj.addProperty("endDate", endDate);
		reqObj.addProperty("gameId", gameId);
		reqObj.addProperty("drawName", drawName);
		reqObj.addProperty("winStatus", winStatus);

		sReq.setServiceData(reqObj);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
		if(!sRes.getIsSuccess()){
			String responseData =  sRes.getResponseData().toString();
			logger.info("response object : " + responseData);
			if(responseData.contains("4052")){
				throw new LMSException(Integer.parseInt(responseData.split(":")[0].replaceAll("\"", "").trim()), responseData.split(":")[1].replaceAll("\"", "").trim());
			}
		}

		if(sRes.getIsSuccess()) {
			Type type = new TypeToken<List<UssdSubscriberDataBean>>(){}.getType();
			mtnCustomerCenterBeans= new Gson().fromJson((JsonElement) sRes.getResponseData(), type);
		}
		return mtnCustomerCenterBeans;
	}
}
