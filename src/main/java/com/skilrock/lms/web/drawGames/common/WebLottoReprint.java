package com.skilrock.lms.web.drawGames.common;

import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WebLottoReprint extends WebBaseReprintController {

	@Override
	public JSONObject prepareReprintFinalResponse(WebReprint webReprintBean) {
		JSONObject jsonResponse = new JSONObject();
		LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) webReprintBean.getGameBean();
		StringBuilder sb=new StringBuilder();
		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		boolean isQP = false ;
		for (int i=0; i<lottoPurchaseBean.getPlayerPicked().size(); i++) {
			betTypeDataRes = new JSONObject();
			isQP = lottoPurchaseBean.getIsQuickPick()[i] == 1 ? true : false;
			betTypeDataRes.put("isQp", isQP);
			betTypeDataRes.put("betName", lottoPurchaseBean.getPlayType());
			betTypeDataRes.put("pickedNumbers", lottoPurchaseBean.getPlayerPicked());
			betTypeDataRes.put("unitPrice", lottoPurchaseBean.getUnitPrice());
			betTypeDataRes.put("noOfLines", lottoPurchaseBean.getNoOfLines());
			betTypeDataRes.put("betAmtMul", lottoPurchaseBean.getBetAmountMultiple()[i]);
			double panelPrice =  lottoPurchaseBean.getUnitPrice() * lottoPurchaseBean.getNoOfDraws();
			betTypeDataRes.put("panelPrice", panelPrice);
			betTypeArray.add(betTypeDataRes);
		}
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		for (int i=0; i<lottoPurchaseBean.getNoOfDraws(); i++) {
			String drawDataString = (String) lottoPurchaseBean.getDrawDateTime().get(i);
			drawData = new JSONObject();
			drawData.put("drawDate", drawDataString.split(" ")[0]);
			String drawName[] = drawDataString.split("\\*");
			if(drawName.length < 2) {
				drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
			}
			else {
				drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
				drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
			}
			drawDataArray.add(drawData);
		}
		JSONObject commonSaleDataRes = new JSONObject();
		commonSaleDataRes.put("ticketNumber", lottoPurchaseBean.getTicket_no()+lottoPurchaseBean.getReprintCount());
		commonSaleDataRes.put("reprintCount", lottoPurchaseBean.getReprintCount());
		commonSaleDataRes.put("barcodeCount", lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? lottoPurchaseBean.getBarcodeCount():""));
		commonSaleDataRes.put("gameName", lottoPurchaseBean.getGameDispName());
		commonSaleDataRes.put("gameDevName", Util.getGameMap().get(lottoPurchaseBean.getGameId()).getGameNameDev());
		commonSaleDataRes.put("purchaseDate", lottoPurchaseBean.getPurchaseTime().split(" ")[0]);
		commonSaleDataRes.put("purchaseTime", lottoPurchaseBean.getPurchaseTime().split(" ")[1]);
		commonSaleDataRes.put("purchaseAmt", lottoPurchaseBean.getTotalPurchaseAmt());
		commonSaleDataRes.put("drawData", drawDataArray);

		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", lottoPurchaseBean.getAdvMsg());
		mainData.put("orgName", webReprintBean.getUserInfoBean().getOrgName());
		mainData.put("userName", webReprintBean.getUserInfoBean().getUserName());
		mainData.put("parentOrgName",webReprintBean.getUserInfoBean().getParentOrgName());

		jsonResponse.put("isSuccess", true);
		jsonResponse.put("errorMsg", "");
		jsonResponse.put("mainData", mainData);
		jsonResponse.put("isPromo", false);
		return jsonResponse;
	}

}
