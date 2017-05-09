package com.skilrock.lms.web.drawGames.common;

import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WebKenoReprint extends WebBaseReprintController {

	@Override
	public JSONObject prepareReprintFinalResponse(WebReprint webReprintBean) {
		JSONObject jsonResponse = new JSONObject();
		KenoPurchaseBean kenoPurchaseBean = (KenoPurchaseBean) webReprintBean.getGameBean();
		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		boolean isQP = false;
		for (int i = 0; i < kenoPurchaseBean.getPlayerData().length; i++) {
			betTypeDataRes = new JSONObject();
			isQP = kenoPurchaseBean.getIsQuickPick()[i] == 1 ? true : false;
			betTypeDataRes.put("isQp", isQP);
			betTypeDataRes.put("betName", kenoPurchaseBean.getPlayType()[i]);
			betTypeDataRes.put("pickedNumbers", kenoPurchaseBean.getPlayerData()[i]);
			betTypeDataRes.put("unitPrice", kenoPurchaseBean.getUnitPrice()[i]);
			betTypeDataRes.put("noOfLines", kenoPurchaseBean.getNoOfLines()[i]);
			betTypeDataRes.put("betAmtMul", kenoPurchaseBean.getBetAmountMultiple()[i]);
			double panelPrice = kenoPurchaseBean.getUnitPrice()[i] * kenoPurchaseBean.getNoOfDraws();
			betTypeDataRes.put("panelPrice", panelPrice);
			betTypeArray.add(betTypeDataRes);
		}

		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		for (int i = 0; i < kenoPurchaseBean.getNoOfDraws(); i++) {
			String drawDataString = (String) kenoPurchaseBean.getDrawDateTime().get(i);
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
		commonSaleDataRes.put("ticketNumber", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount());
		commonSaleDataRes.put("reprintCount", kenoPurchaseBean.getReprintCount());
		commonSaleDataRes
				.put("barcodeCount",
						kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()
								+ ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB
										&& LMSFilterDispatcher.isBarCodeRequired) ? kenoPurchaseBean.getBarcodeCount()
												: ""));
		commonSaleDataRes.put("gameName", kenoPurchaseBean.getGameDispName());
		commonSaleDataRes.put("gameDevName", Util.getGameMap().get(kenoPurchaseBean.getGameId()).getGameNameDev());
		commonSaleDataRes.put("purchaseDate", kenoPurchaseBean.getPurchaseTime().split(" ")[0]);
		commonSaleDataRes.put("purchaseTime", kenoPurchaseBean.getPurchaseTime().split(" ")[1]);
		commonSaleDataRes.put("purchaseAmt", kenoPurchaseBean.getTotalPurchaseAmt());
		commonSaleDataRes.put("drawData", drawDataArray);

		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", kenoPurchaseBean.getAdvMsg());
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
