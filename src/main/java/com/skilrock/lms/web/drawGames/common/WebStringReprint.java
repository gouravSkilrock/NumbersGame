package com.skilrock.lms.web.drawGames.common;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONObject;

public class WebStringReprint extends WebBaseReprintController {

	@Override
	public JSONObject prepareReprintFinalResponse(WebReprint webReprintBean) {
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("isSuccess", false);
		jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		if ("RG_RPERINT".equals(webReprintBean.getGameBean().toString())) {
			jsonResponse.put("errorMsg", DGErrorMsg.buyErrMsg(webReprintBean.getGameBean().toString()));
		} else if ("Last Transaction Not Sale".equals(webReprintBean.getGameBean().toString())) {
			jsonResponse.put("errorMsg", DGErrorMsg.buyErrMsg("LAST_TXN_NOT_SALE"));
		} else if (webReprintBean.getGameBean().toString().contains("Performed")) {
			jsonResponse.put("errorMsg", DGErrorMsg.buyErrMsg("PERFORMED"));
		} else {
			jsonResponse.put("errorMsg", "Reprint Fail");
		}
		return jsonResponse;
	}

}
