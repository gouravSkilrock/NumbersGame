package com.skilrock.lms.rest.scratchService.BaseController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.rest.services.bean.FailureJsonResponse;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;

public class BaseController {

	protected ScracthMgmtBean getScracthManagementBean(String requestData) {
		JsonObject reqJsonObject = (JsonObject) new JsonParser().parse(requestData);
		ScracthMgmtBean bean = new Gson().fromJson(reqJsonObject, new TypeToken<ScracthMgmtBean>() {
		}.getType());
		return bean;
	}
	
	protected FailureJsonResponse getFailureJsonResponse(int errorCode, String errorMessage) {
		FailureJsonResponse failureJsonResponse = new FailureJsonResponse(errorCode, errorMessage);
		return failureJsonResponse;
	}
}
