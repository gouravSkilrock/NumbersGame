package com.skilrock.lms.embedded.drawGames.common;


import java.util.List;

import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.UtilApplet;
public class CommonMethods {
	
	public static String buildRaffleData(
			List<RafflePurchaseBean> rafflePurchaseBeanList) {

		/*
		 * String raffleData = ""; String raffleTicketType=null; String
		 * raffleTicket_no=null; String raffleDrawDateTime=null;
		 * if(rafflePurchaseBeanList!=null){ for(int i=0;i<rafflePurchaseBeanList.size();i++){
		 * RafflePurchaseBean rafflePurchaseBean =
		 * rafflePurchaseBeanList.get(i); raffleTicketType =
		 * rafflePurchaseBean.getRaffleTicketType(); raffleTicket_no =
		 * rafflePurchaseBean.getRaffleTicket_no(); raffleDrawDateTime =
		 * rafflePurchaseBean.getDrawDateTime(); raffleData = raffleData +
		 * "RaffleData:"+raffleTicketType+","+raffleTicket_no+","+raffleDrawDateTime+","+"Nxt**"+"|"; }
		 * if(rafflePurchaseBeanList.size()>0) { raffleData =
		 * raffleData.substring(0,raffleData.length() -1); }
		 *  }
		 */

		StringBuilder raffData = new StringBuilder("");
		RafflePurchaseBean raffPurBean = null;
		if (rafflePurchaseBeanList != null) {
			for (int i = 0; i < rafflePurchaseBeanList.size(); i++) {
				raffPurBean = rafflePurchaseBeanList.get(i);
				raffData.append("RaffleData:"
						+ raffPurBean.getRaffleTicketType() + ","
						+ raffPurBean.getRaffleTicket_no()+raffPurBean.getReprintCount()+","
						+ raffPurBean.getDrawDateTime());

				StringBuilder promoTopAdvMsg = new StringBuilder("");
				StringBuilder promoBottomAdvMsg = new StringBuilder("");
				UtilApplet.getAdvMsgs(raffPurBean.getAdvMsg(), promoTopAdvMsg, promoBottomAdvMsg, 100);
				
				if(promoTopAdvMsg.length() > 0){
					raffData.append(",promoTopAdvMsg:" + promoTopAdvMsg);
				}
				
				if(promoTopAdvMsg.length() > 0 && promoBottomAdvMsg.length() > 0){
					raffData.append("Nxt**promoBottomAdvMsg:" + promoBottomAdvMsg);
				} else if(promoBottomAdvMsg.length() > 0){
					raffData.append(",promoBottomAdvMsg:" + promoBottomAdvMsg);
				}
				
				raffData.append("|");
			}
		}

		return raffData.toString();
	}
	
	

}
