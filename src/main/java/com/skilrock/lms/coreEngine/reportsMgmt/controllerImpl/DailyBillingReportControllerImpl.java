package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.sql.Timestamp;

import net.sf.json.JSONObject;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;

public class DailyBillingReportControllerImpl {

	public Object fetchDailyBilligReportData(Timestamp startTime, Timestamp endTime, String reportType) throws LMSException {
		ServiceRequest serReq = new ServiceRequest();
		ServiceResponse serResp = new ServiceResponse();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		Object respObject = null;
		try {
			JSONObject requestObject = new JSONObject();
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_DAILY_BILLING_REPORT_RANBOW);
			requestObject.put("startDate", String.valueOf(startTime));
			requestObject.put("endDate", String.valueOf(endTime));
			requestObject.put("reportType", reportType);
			serReq.setServiceData(requestObject);
			serResp = delegate.getResponse(serReq);
			respObject = (Object) serResp.getResponseData();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return respObject;
	}

}
