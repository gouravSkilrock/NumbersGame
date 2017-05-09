package com.skilrock.lms.embedded.drawGames.common;

import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class FortuneTwoReprint extends BaseReprintController{

	@Override
	public String prepareFinalResponse(EmbeddedReprint embeddedReprint) {
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";
		String finalReprintData = null;
		String gameName = null;
		try{
			FortuneTwoPurchaseBean bean = (FortuneTwoPurchaseBean) embeddedReprint.getGameBean();
			UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,bottomMsgsStr, 10);
			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}
			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
			}
			if (bean.getSaleStatus() == "PERFORMED") {
				finalReprintData = "ErrorMsg:"+ EmbeddedErrors.DRAW_PERFORMED+"ErrorCode:" + EmbeddedErrors.DRAW_PERFORMED_ERROR_CODE+"|";
				return finalReprintData;
			}
			gameName = Util.getGameName(bean.getGame_no());
			String reprintData = ReprintHepler.reprintFortuneTwoTicket(bean, gameName);
			String promoReprintData = buildPromoReprintData(bean.getPromoPurchaseBean(),embeddedReprint.getUserInfoBean(),embeddedReprint.getCountryDeployed());
			finalReprintData = reprintData + "|balance:" + embeddedReprint.getBalance() + "|QP:"+ bean.getIsQuickPick()[0] + "|" + promoReprintData+ advtMsg;
		}catch(Exception e){
			e.printStackTrace();
		}
		return finalReprintData;
	}

}
