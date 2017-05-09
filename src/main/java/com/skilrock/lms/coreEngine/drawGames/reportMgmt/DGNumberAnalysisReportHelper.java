package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.dge.beans.DrawDataBean;

public class DGNumberAnalysisReportHelper {
	public LinkedHashMap<String, ArrayList<String>> getNumberData(DrawDataBean dataBean){
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_NUMBER_BET_AMOUNT_DATA);
		sReq.setServiceData(dataBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if(sRes.getIsSuccess())
		{
			Type type = new TypeToken<LinkedHashMap<String, ArrayList<String>>>() {
			}.getType();
			return new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		}
		return null;
	}
	
	public String fetchDrawName(DrawDataBean dataBean){
		String str = null;
		
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_NAME_OF_DRAW_DATA);
		sReq.setServiceData(dataBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
		if(sRes.getIsSuccess())
		{
			Type type = new TypeToken<String>() {
			}.getType();
			str = new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		}
		return str;
	}

	public List<AnalysisBean> fetchDrawAnalysisData(AnalysisBean anaBean)
	{
		List<AnalysisBean> anaList = null;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_ANALYSIS_DATA);
		sReq.setServiceData(anaBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
		Type type = new TypeToken<ArrayList<AnalysisBean>>() {
		}.getType();
		anaList = new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		
		return anaList;
	}

	public Map<Integer, AnalysisBean> getDrawDataRetailerWise(AnalysisBean anaBean)
	{
		
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.GET_DRAW_DATA_RETAILER_WISE);
		sReq.setServiceData(anaBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		Type type = new TypeToken<Map<Integer, AnalysisBean>>() {}.getType();
		Map<Integer,AnalysisBean> mapData = new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		return mapData;
		
		
	}
	
	
}
