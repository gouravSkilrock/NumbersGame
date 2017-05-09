package com.skilrock.lms.embedded.drawGames.common;

import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class RaffleReprint extends BaseReprintController{

	@Override
	public String prepareFinalResponse(EmbeddedReprint embeddedReprint) {
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";
		StringBuilder finalData = new StringBuilder("RaffleData:");
		try{
			RafflePurchaseBean bean = (RafflePurchaseBean) embeddedReprint.getGameBean();
			UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);
			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}
			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
			}
			String time = bean.getPurchaseTime().replace(" ","|Time:");
			String drawDateTime = bean.getDrawDateTime().split("\\*")[0].replace(" ","|Time:");
			String drawName = bean.getDrawDateTime().split("\\*")[1];
			finalData.append("TicNo:").append(bean.getRaffleTicket_no()).append(bean.getReprintCount());
			finalData.append("|brCd:").append(bean.getRaffleTicket_no()).append(bean.getReprintCount()).append(bean.getBarcodeCount());
			finalData.append("|Date:").append(time);
			finalData.append("|GameName:").append(bean.getGameDispName());
			finalData.append("|DrawDate:").append(drawDateTime);
			finalData.append("|DrawName:").append(drawName);
			finalData.append(advtMsg);
		}catch(Exception e){
			e.printStackTrace();
		}
		return finalData.toString();
	}

}
