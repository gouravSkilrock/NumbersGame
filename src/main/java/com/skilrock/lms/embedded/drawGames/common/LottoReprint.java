package com.skilrock.lms.embedded.drawGames.common;

import java.util.List;

import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class LottoReprint extends BaseReprintController{

	@Override
	public String prepareFinalResponse(EmbeddedReprint embeddedReprint) {
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";
		String finalReprintData = null;
		String gameName = null;
		LottoPurchaseBean bean = (LottoPurchaseBean) embeddedReprint.getGameBean();
		try{
			UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,	bottomMsgsStr, 10);
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
			String reprintData = ReprintHepler.reprintLottoTicket(bean, gameName);
			List<RafflePurchaseBean> rafflePurchaseBeanList = bean.getRafflePurchaseBeanList();
			String raffleData = CommonMethods.buildRaffleData(rafflePurchaseBeanList);
			if (raffleData.length() != 0) {
				raffleData = raffleData + "|";
			}
			String promoData="";
			if(bean.getPromoPurchaseBeanList()!=null){
				List<LottoPurchaseBean> promoPurchaseBeanList=bean.getPromoPurchaseBeanList();
				 promoData=ReprintHepler.buildSaleDataforZimlottothree(promoPurchaseBeanList,embeddedReprint.getUserInfoBean());
			}
			finalReprintData = reprintData + "|balance:" + embeddedReprint.getBalance() + "|"+ raffleData +advtMsg+ promoData;
		}catch(Exception e){
			e.printStackTrace();
		}
		return finalReprintData;
	}

}
