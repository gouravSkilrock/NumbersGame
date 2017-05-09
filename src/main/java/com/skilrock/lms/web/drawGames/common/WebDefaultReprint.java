package com.skilrock.lms.web.drawGames.common;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONObject;

public class WebDefaultReprint extends WebBaseReprintController {

	@Override
	public JSONObject prepareReprintFinalResponse(WebReprint webReprintBean) {
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("isSuccess", false);
		jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		if (webReprintBean.getErrorMessage() == null) {
			jsonResponse.put("errorMsg", "No Sale Transaction For Reprint");
		} else {
			jsonResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return jsonResponse;
	}

}
