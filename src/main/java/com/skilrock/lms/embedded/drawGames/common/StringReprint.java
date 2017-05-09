package com.skilrock.lms.embedded.drawGames.common;

import com.skilrock.lms.dge.beans.EmbeddedReprint;

public class StringReprint extends BaseReprintController{

	@Override
	public String prepareFinalResponse(EmbeddedReprint embeddedReprint) {
		String finalReprintData = null;
		if("RG_RPERINT".equals(embeddedReprint.getGameBean().toString())){
			finalReprintData = "ErrorMsg:" + EmbeddedErrors.REPRINT_FRAUD+"ErrorCode:" + EmbeddedErrors.REPRINT_FRAUD_ERROR_CODE+"|";
		}else{
			finalReprintData = embeddedReprint.getGameBean().toString();
		}
		return finalReprintData;
	}

}
