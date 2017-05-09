package com.skilrock.lms.web.drawGames.common;

import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WebFortuneReprint extends WebBaseReprintController {

	@Override
	public JSONObject prepareReprintFinalResponse(WebReprint webReprintBean) {
		JSONObject jsonResponse = new JSONObject();
		FortunePurchaseBean fortuneBean = (FortunePurchaseBean) webReprintBean.getGameBean();

		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		boolean isQP = false;
		for (int i = 0; i < fortuneBean.getBetAmountMultiple().length; i++) {
			betTypeDataRes = new JSONObject();
			isQP = fortuneBean.getIsQuickPick()[i] == 1 ? true : false;
			betTypeDataRes.put("isQp", isQP);
			betTypeDataRes.put("betName", "oneToTwelve");
			betTypeDataRes.put("pickedNumbers", fortuneBean.getPickedNumbers().get(i).toString());
			betTypeDataRes.put("unitPrice", fortuneBean.getUnitPrice());
			betTypeDataRes.put("noOfLines", "1");
			betTypeDataRes.put("betAmtMul", fortuneBean.getBetAmountMultiple()[i]);
			double panelPrice = fortuneBean.getBetAmountMultiple()[i] * fortuneBean.getUnitPrice() * 1
					* fortuneBean.getNoOfDraws();
			betTypeDataRes.put("panelPrice", panelPrice);
			betTypeArray.add(betTypeDataRes);
		}
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		for (int i = 0; i < fortuneBean.getNoOfDraws(); i++) {
			String drawDataString = (String) fortuneBean.getDrawDateTime().get(i);
			drawData = new JSONObject();
			drawData.put("drawDate", drawDataString.split(" ")[0]);
			String drawName[] = drawDataString.split("\\*");
			if (drawName.length < 2) {
				drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
			} else {
				drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
				drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
			}
			drawDataArray.add(drawData);
		}
		JSONObject commonSaleDataRes = new JSONObject();
		commonSaleDataRes.put("ticketNumber", fortuneBean.getTicket_no() + fortuneBean.getReprintCount());
		commonSaleDataRes.put("reprintCount", fortuneBean.getReprintCount());
		commonSaleDataRes
				.put("barcodeCount",
						fortuneBean.getTicket_no() + fortuneBean.getReprintCount()
								+ ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB
										&& LMSFilterDispatcher.isBarCodeRequired) ? fortuneBean.getBarcodeCount()
												: ""));
		commonSaleDataRes.put("gameName", fortuneBean.getGameDispName());
		commonSaleDataRes.put("gameDevName", Util.getGameMap().get(fortuneBean.getGameId()).getGameNameDev());
		commonSaleDataRes.put("purchaseDate", fortuneBean.getPurchaseTime().split(" ")[0]);
		commonSaleDataRes.put("purchaseTime", fortuneBean.getPurchaseTime().split(" ")[1]);
		commonSaleDataRes.put("purchaseAmt", fortuneBean.getTotalPurchaseAmt());
		commonSaleDataRes.put("drawData", drawDataArray);

		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", fortuneBean.getAdvMsg());
		mainData.put("orgName", webReprintBean.getUserInfoBean().getOrgName());
		mainData.put("userName", webReprintBean.getUserInfoBean().getUserName());
		mainData.put("parentOrgName", webReprintBean.getUserInfoBean().getParentOrgName());

		jsonResponse.put("isSuccess", true);
		jsonResponse.put("errorMsg", "");
		jsonResponse.put("mainData", mainData);
		jsonResponse.put("isPromo", false);
		return jsonResponse;
	}

}
