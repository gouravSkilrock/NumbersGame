package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.dge.beans.CombiAnalysisBean;

public class CombinitionAnalysisDataDrawWiseHelper {
	static Logger logger = LoggerFactory.getLogger(CombinitionAnalysisDataDrawWiseHelper.class);

	public Map<String, List<CombiAnalysisBean>> getReport(AnalysisBean anaBean){
		
		ServiceRequest sReq = null;
		ServiceResponse sRes = null;
		Map<String, List<CombiAnalysisBean>> combiMap = null;
		try {
			sRes = new ServiceResponse();
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.REPORTS_MGMT);
			sReq.setServiceMethod(ServiceMethodName.FETCH_COMBINITION_DRAW_WISE);
			sReq.setServiceData(anaBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);
			if(sRes.getResponseData() == null)
				throw new LMSException(LMSErrors.DATA_UNAVAILABLE_ERROR_CODE, LMSErrors.DATA_UNAVAILABLE_ERROR_MESSAGE);
			
			Type type = new TypeToken<Map<String, List<CombiAnalysisBean>>>(){}.getType();
			combiMap = new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
			
		} catch (LMSException e) {
			logger.info(e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return combiMap;
	
	}
}
