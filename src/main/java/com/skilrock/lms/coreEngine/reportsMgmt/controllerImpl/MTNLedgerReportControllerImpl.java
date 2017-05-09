package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawGameMtnDataBean;

public class MTNLedgerReportControllerImpl {
	
	@SuppressWarnings("unchecked")
	public Map<String, DrawGameMtnDataBean> fetchMtnLedgerData(DrawDataBean drawDataBean) throws LMSException{
		Map<String, DrawGameMtnDataBean> mtnDataMap = null;
		ServiceRequest serReq = new ServiceRequest();
		ServiceResponse serResp = new ServiceResponse();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		try {
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_MTN_LEDGER_DATA);
			serReq.setServiceData(drawDataBean);
			serResp = delegate.getResponse(serReq);
			if(!serResp.getIsSuccess()){
				String responseData =  serResp.getResponseData().toString();
				if(responseData.contains("4052")){
					throw new LMSException(Integer.parseInt(responseData.split(":")[0].replaceAll("\"", "").trim()), responseData.split(":")[1].replaceAll("\"", "").trim());
				}
			}
			if (serResp.getIsSuccess())
				mtnDataMap= (Map<String, DrawGameMtnDataBean>) new Gson().fromJson((JsonElement) serResp.getResponseData(), new TypeToken<Map<String, DrawGameMtnDataBean>>() {}.getType());
		}catch (LMSException e) {
			throw e;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return mtnDataMap;
	}

}
