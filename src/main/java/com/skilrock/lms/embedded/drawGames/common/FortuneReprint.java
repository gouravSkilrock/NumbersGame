package com.skilrock.lms.embedded.drawGames.common;

import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class FortuneReprint extends BaseReprintController{

	@Override
	public String prepareFinalResponse(EmbeddedReprint embeddedReprint) {
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";
		String finalReprintData = null;
		String gameName = null;
		try{
			FortunePurchaseBean bean = (FortunePurchaseBean) embeddedReprint.getGameBean();
			UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,	bottomMsgsStr, 10);
			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}
			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
			}
			if (bean.getSaleStatus() == "PERFORMED") {
				finalReprintData = "ErrorMsg:" + EmbeddedErrors.DRAW_PERFORMED+"ErrorCode:" + EmbeddedErrors.DRAW_PERFORMED_ERROR_CODE+"|";
				return finalReprintData;
			}
			gameName = Util.getGameName(bean.getGameId());
			String reprintData = ReprintHepler.reprintFortuneTicket(bean,gameName);
			String promoData;
			promoData = buildPromoReprintData(bean.getPromoPurchaseBean(),embeddedReprint.getUserInfoBean(),embeddedReprint.getCountryDeployed());
			finalReprintData = reprintData + "|balance:" + embeddedReprint.getBalance() + "|QP:"	+ bean.getIsQuickPick()[0] +"|ExpiryTime:"+bean.getClaimEndTime().replace(".0", "")+ "|" + advtMsg + promoData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalReprintData;
	}

}
