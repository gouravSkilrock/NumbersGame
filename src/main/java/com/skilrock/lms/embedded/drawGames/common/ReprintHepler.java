package com.skilrock.lms.embedded.drawGames.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneThreePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class ReprintHepler {
	public static Log logger = LogFactory.getLog(ReprintHepler.class);

	public static String reprintFortuneTicket(FortunePurchaseBean fortuneBean,
			String gameName) throws Exception {
		logger.info(" gameName in reprint helper " + gameName);

		List<String> finalSymbols = null;
		if (gameName.equalsIgnoreCase("card16")) {
			finalSymbols = Arrays.asList("", "ace_spade", "ace_heart",
					"ace_diamond", "ace_club", "king_spade", "king_heart",
					"king_diamond", "king_club", "queen_spade", "queen_heart",
					"queen_diamond", "queen_club", "jack_spade", "jack_heart",
					"jack_diamond", "jack_club");
		} else if (gameName.equalsIgnoreCase("zerotonine")) {

			finalSymbols = Arrays.asList("", "Zero(0)", "One(1)", "Two(2)",
					"Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
					"Eight(8)", "Nine(9)");
		} else if (gameName.equalsIgnoreCase("card12")) {
			finalSymbols = Arrays.asList("", "ace_spade", "ace_heart",
					"ace_diamond", "ace_club", "king_spade", "king_heart",
					"king_diamond", "king_club", "queen_spade", "queen_heart",
					"queen_diamond", "queen_club");
		} else if (gameName.equalsIgnoreCase("fortune")) {
			finalSymbols = Arrays.asList("", "Aries", "Taurus", "Gemini",
					"Cancer", "Leo", "Virgo", "Libra", "Scorpio",
					"Sagittarius", "Capricorn", "Aquarius", "Pisces");
		}

		String time = fortuneBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < fortuneBean.getPickedNumbers().size(); i++) {
			stBuilder.append(finalSymbols.get(fortuneBean.getPickedNumbers()
					.get(i)+1)
					+ ":" + fortuneBean.getBetAmountMultiple()[i] + "|");
		}
		int listSize = fortuneBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + fortuneBean.getDrawDateTime()
					.get(i)).replaceFirst(" ", "#").replace(
					"#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
		}
	  
		//String raffleData = CommonMethods.buildRaffleData(fortuneBean.getRafflePurchaseBeanList());
		StringBuilder finalData = new StringBuilder();

		if(ConfigurationVariables.currentTktLen == 18)
		{
			finalData.append("TicketNo:" + fortuneBean.getTicket_no() + fortuneBean.getReprintCount()
					+ "|brCd:" + fortuneBean.getTicket_no() + fortuneBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? fortuneBean.getBarcodeCount():"")
					+ "|Date:" + time + "|"
					+ stBuilder.toString() + "TicketCost:"
					+ fortuneBean.getTotalPurchaseAmt() + drawTimeBuild.toString());
		}
		else if(ConfigurationVariables.currentTktLen == 16)
		{
			finalData.append("TicketNo:" + fortuneBean.getTicket_no()
			+ fortuneBean.getReprintCount() + "|Date:" + time + "|"
			+ stBuilder.toString() + "TicketCost:"
			+ fortuneBean.getTotalPurchaseAmt() + drawTimeBuild.toString()+ "|");
		}

		System.out.println(" final " + gameName + " reprint data is "
				+ finalData.toString());
		return finalData.toString();

		/*System.out.println(" final " + gameName + " reprint data is "
				+ finalData);*/
	}
	public static String buildSaleDataforZimlottothree(List<LottoPurchaseBean> promoPurchaseBeanList,
			UserInfoBean userBean) {
         StringBuilder finalPromoData=new StringBuilder();
		for(int j=0;j<promoPurchaseBeanList.size();j++){
			LottoPurchaseBean lottoBean=promoPurchaseBeanList.get(j);
		String time = lottoBean.getPurchaseTime()
		.replace(" ", "|Time:").replace(".0", "");

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
		- userBean.getClaimableBal();
		String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);
		int listSize = lottoBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + lottoBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace(
							"#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
		}
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < lottoBean.getPlayerPicked().size(); i++) {
			stBuilder.append("|Num:"
					+ lottoBean.getPlayerPicked().get(i) + "|QP:"
					+ lottoBean.getIsQuickPick()[i]);
		}

		List<RafflePurchaseBean> rafflePurchaseBeanList = lottoBean
		.getRafflePurchaseBeanList();
		String raffleData = CommonMethods
		.buildRaffleData(rafflePurchaseBeanList);
		
		String finalData = "PromoTkt:TicketNo:"
				+ lottoBean.getTicket_no() + lottoBean.getReprintCount()
				+"|brCd:"+lottoBean.getTicket_no() + lottoBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? lottoBean.getBarcodeCount():"")
				+ "|Date:" + time 
				+ "|PlayType:" + lottoBean.getPlayType()
				+ stBuilder.toString()+ "|TicketCost:"
				+ lottoBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance  + "|"
				+ raffleData;
		finalPromoData.append(finalData);
		}
		return finalPromoData.toString();
	}
	public static String reprintKenoTicket(KenoPurchaseBean kenoPurchaseBean,
			String gameName,double version,String countryDeployed) throws Exception {
		logger.debug(" gameName in reprint helper " + gameName);
		String time = kenoPurchaseBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		int listSize = kenoPurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + kenoPurchaseBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:")
					.replace(".0", ""));
		}

		StringBuilder finalData = new StringBuilder("");

		logger.debug(kenoPurchaseBean.getPlayType());

		/*
		if(ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB)
		{
			finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no()
					+ kenoPurchaseBean.getReprintCount() +"|brCd:"+kenoPurchaseBean.getTicket_no()+kenoPurchaseBean.getReprintCount()+((kenoPurchaseBean.getTicket_no()+kenoPurchaseBean.getReprintCount()).length()==ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired?kenoPurchaseBean.getBarcodeCount():"")+ "|Date:" + time);
		}
		else if(ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenA)
		{
			finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no()
					+ kenoPurchaseBean.getReprintCount() + "|Date:" + time);
		}
		*/
		
		// FOR BACKWARD COMPATIBILITY 
		/*if(version>=ConfigurationVariables.currentTerminalBuildVersion)
		finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no()+ kenoPurchaseBean.getReprintCount()+"|brCd:"+kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+(((kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()).length() == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);
		else
		finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + "|Date:" + time);
		*/
		
		// FOR BACKWARD COMPATIBILITY 
		if(version< Double.parseDouble(Utility.getPropertyValue("CURRENT_TERMINAL_BUILD_VERSION")) && "NIGERIA".equals(countryDeployed))
			finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()).length()==ConfigurationVariables.tktLenB /*&& LMSFilterDispatcher.isBarCodeRequired*/?kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);			
		else
			finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+"|brCd:"+kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);
			

		int noOfPanels = kenoPurchaseBean.getPlayerData().length;
		String[] playTypeStr = kenoPurchaseBean.getPlayType();
		for (int i = 0; i < noOfPanels; i++) {
			String panelPrice = "|PanelPrice:"
					+ kenoPurchaseBean.getBetAmountMultiple()[i]
					* kenoPurchaseBean.getUnitPrice()[i]
					* kenoPurchaseBean.getNoOfLines()[i]
					* kenoPurchaseBean.getNoOfDraws();
			if("supertwo".equalsIgnoreCase(gameName)){
				double panelPri = kenoPurchaseBean.getBetAmountMultiple()[i]
                                  * kenoPurchaseBean.getNoOfLines()[i]				                                                          	
				                  * kenoPurchaseBean.getUnitPrice()[i]
				                  * kenoPurchaseBean.getNoOfDraws();
				panelPrice = "|PanelPrice:" + (new DecimalFormat("#.#").format(panelPri));
				if(playTypeStr[i].contains("Banker")){
					finalData.append("|PlayType:" + playTypeStr[i] + "|Num:"
							+ kenoPurchaseBean.getPlayerData()[i].replace("-1", "XX") + panelPrice
							+ "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);					
				}else{
					finalData.append("|PlayType:" + playTypeStr[i] + "|Num:"
							+ kenoPurchaseBean.getPlayerData()[i] + panelPrice
							+ "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);
				}
			} else if("rainbowgame".equalsIgnoreCase(gameName)) {
				finalData.append("|PlayType:" + kenoPurchaseBean.getPlayType()[i] + "|Num:" + kenoPurchaseBean.getPlayerData()[i] + panelPrice + "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);
			} else {
				if("kenotwo".equalsIgnoreCase(gameName)){
					double panelPri = kenoPurchaseBean.getBetAmountMultiple()[i] * kenoPurchaseBean.getUnitPrice()[i] * kenoPurchaseBean.getNoOfLines()[i] * kenoPurchaseBean.getNoOfDraws();
					panelPrice = "|PanelPrice:" + (new DecimalFormat("#.#").format(panelPri));				
				}
				if ("Banker".equalsIgnoreCase(playTypeStr[i]) || "MN-Banker".equalsIgnoreCase(playTypeStr[i])) {
				//logger.debug("---------------BANKER---------------");

				String playerData = kenoPurchaseBean.getPlayerData()[i];
				String[] banker = playerData.replace(",BL", "").split(",UL,");
				finalData.append("|PlayType:" + playTypeStr[i] + "|UL:"
						+ banker[0] + "|BL:" + banker[1] + panelPrice + "|QP:"
						+ kenoPurchaseBean.getIsQuickPick()[i]);
				} else {

					//logger.debug("--------------OTHERS--------------");
					finalData.append("|PlayType:" + playTypeStr[i] + "|Num:"
						+ kenoPurchaseBean.getPlayerData()[i] + panelPrice
						+ "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);

				}
			}
		}
		finalData.append("|TicketCost:"
				+ kenoPurchaseBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() );
		//logger.debug(" final keno data is " + finalData);
		return finalData.toString();
	}
    
	public static String reprintFortuneTwoTicket(FortuneTwoPurchaseBean fortuneTwoBean,String gameName){
		logger.debug(" gameName in reprint helper " + gameName);
		String time = fortuneTwoBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		int listSize = fortuneTwoBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + fortuneTwoBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:")
					.replace(".0", ""));
		}

		StringBuilder finalData = new StringBuilder("");

		//logger.debug(fortuneTwoBean.getPlayType());

		if(ConfigurationVariables.currentTktLen == 18)
		{
			finalData.append("TicketNo:" + fortuneTwoBean.getTicket_no()
					+ fortuneTwoBean.getReprintCount() +fortuneTwoBean.getBarcodeCount()+ "|Date:" + time);
		}
		else if(ConfigurationVariables.currentTktLen == 16)
		{
			finalData.append("TicketNo:" + fortuneTwoBean.getTicket_no()
					+ fortuneTwoBean.getReprintCount() + "|Date:" + time);
		}
		

		int noOfPanels = fortuneTwoBean.getPlayerData().length;
		String[] playTypeStr = fortuneTwoBean.getPlayType();
		for (int i = 0; i < noOfPanels; i++) {
			String panelPrice = "|PanelPrice:"
					+ fortuneTwoBean.getBetAmountMultiple()[i]
					* fortuneTwoBean.getUnitPrice()[i]
					* fortuneTwoBean.getNoOfDraws()
					* fortuneTwoBean.getNoOfLines()[i];
			
			finalData.append("|PlayType:" + playTypeStr[i].replace("Banker", "Group") + "|Num:"
						+ fortuneTwoBean.getPlayerData()[i] + panelPrice
						+ "|QP:" + fortuneTwoBean.getIsQuickPick()[i]);				
			
		}
		finalData.append("|TicketCost:"
				+ fortuneTwoBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() );
		//logger.debug(" final forutne two data is " + finalData);
		return finalData.toString();
	}
	public static String reprintFortuneThreeTicket(FortuneThreePurchaseBean fortuneThreeBean,String gameName){
		logger.debug(" gameName in reprint helper " + gameName);
		String time = fortuneThreeBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		int listSize = fortuneThreeBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + fortuneThreeBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:")
					.replace(".0", ""));
		}

		StringBuilder finalData = new StringBuilder("");
		logger.debug(fortuneThreeBean.getPlayType());
		//logger.debug(fortuneThreeBean.getPlayType());

		if(ConfigurationVariables.currentTktLen == 18)
		{
			finalData.append("TicketNo:" + fortuneThreeBean.getTicket_no()
					+ fortuneThreeBean.getReprintCount() +fortuneThreeBean.getBarcodeCount()+ "|Date:" + time);
		}
		else if(ConfigurationVariables.currentTktLen == 16)
		{
			finalData.append("TicketNo:" + fortuneThreeBean.getTicket_no()
					+ fortuneThreeBean.getReprintCount() + "|Date:" + time);
		}

		int noOfPanels = fortuneThreeBean.getPlayerData().length;
		String[] playTypeStr = fortuneThreeBean.getPlayType();
		for (int i = 0; i < noOfPanels; i++) {
			String panelPrice = "|PanelPrice:"
					+ fortuneThreeBean.getBetAmountMultiple()[i]
					* fortuneThreeBean.getUnitPrice()[i]
					* fortuneThreeBean.getNoOfDraws()
					* fortuneThreeBean.getNoOfLines()[i];
			
			finalData.append("|PlayType:" + playTypeStr[i].replace("Banker", "Group") + "|Num:"
						+ fortuneThreeBean.getPlayerData()[i] + panelPrice
						+ "|QP:" + fortuneThreeBean.getIsQuickPick()[i]);				
			
		}
		finalData.append("|TicketCost:"
				+ fortuneThreeBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() );
		//logger.debug(" final forutne three data is " + finalData);
		return finalData.toString();
	
		
	}
	
	public static String reprintLottoTicket(
			LottoPurchaseBean lottoPurchaseBean, String gameName)
			throws Exception {
		logger.debug(" gameName in reprint helper " + gameName);
		String time = lottoPurchaseBean.getPurchaseTime()
				.replace(" ", "|Time:").replace(".0", "");
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < lottoPurchaseBean.getPlayerPicked().size(); i++) {
			stBuilder.append("|Num:" + lottoPurchaseBean.getPlayerPicked().get(i) + "|QP:" + lottoPurchaseBean.getIsQuickPick()[i]);
		}
		int listSize = lottoPurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + lottoPurchaseBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace(
							"#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
		}
		
		String gameDevName = Util.getGameName(lottoPurchaseBean.getGame_no());

		/*
		String ticketNumber = "";
		if(ConfigurationVariables.currentTktLen == 18)
		{
			ticketNumber = "TicketNo:" + lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()+ lottoPurchaseBean.getBarcodeCount();
		}
		else if(ConfigurationVariables.currentTktLen == 16)
		{
			ticketNumber = "TicketNo:" + lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount();
		}
		*/

		String ticketNumber = "TicketNo:" + lottoPurchaseBean.getTicket_no()
		+ lottoPurchaseBean.getReprintCount()+"|brCd:"+lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()+(((lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()).length() == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? lottoPurchaseBean.getBarcodeCount():"");

		String finalData =null;
		if("BonusBalllotto".equalsIgnoreCase(gameDevName) || "Tanzanialotto".equalsIgnoreCase(gameDevName) ){
			finalData = ticketNumber 
			+ "|Date:" + time + stBuilder.toString()		
			+ "|TicketCost:" + lottoPurchaseBean.getTotalPurchaseAmt() + drawTimeBuild.toString();
		}else if("Bonusballtwo".equalsIgnoreCase(gameDevName)){
			finalData = ticketNumber
			+ "|Date:" + time 
			+ "|PlayType:" + lottoPurchaseBean.getPlayType() + stBuilder.toString() 
			+ "|unitPrice:" + lottoPurchaseBean.getUnitPrice()*lottoPurchaseBean.getBetAmtMultiple()
			+ "|TicketCost:" + lottoPurchaseBean.getTotalPurchaseAmt() + drawTimeBuild.toString();			
		}else{
		 finalData = ticketNumber + "|Date:" + time 
						+ "|PlayType:" + lottoPurchaseBean.getPlayType() + stBuilder.toString() 
						+ "|TicketCost:" + lottoPurchaseBean.getTotalPurchaseAmt() + drawTimeBuild.toString();
		}

		//logger.debug(" final " + gameName + " reprint data is " + finalData);
		return finalData;
	}
	
	public static String reprintKenoTicketJsonFormat(KenoPurchaseBean kenoPurchaseBean,
			String gameName, double balance) {

		JsonObject responseObj = new JsonObject();
		int listSize = kenoPurchaseBean.getDrawDateTime().size();
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		try{
			JsonObject tktDataJsonObject = new JsonObject();
			tktDataJsonObject.addProperty("gameCode", gameName);
			tktDataJsonObject.addProperty("ticketNumber", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount());
			tktDataJsonObject.addProperty("barCd", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired) ? kenoPurchaseBean.getBarcodeCount() : ""));
			
			tktDataJsonObject.addProperty("TicketCost", nf.format(kenoPurchaseBean.getTotalPurchaseAmt()));
			tktDataJsonObject.addProperty("gameName", gameName);
			tktDataJsonObject.addProperty("purchaseTime", (Util.changeFormat("dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", kenoPurchaseBean.getPurchaseTime())));
			
			JsonArray panelDataArray = new JsonArray();
			JsonArray drawDataArray = new JsonArray();
			tktDataJsonObject.add("panelData", panelDataArray);
			tktDataJsonObject.add("drawData", drawDataArray);
			
			JsonObject drawDataObject = null;
			for (int iLoop = 0; iLoop < listSize; iLoop++) {
				drawDataObject = new JsonObject();
				if (kenoPurchaseBean.getDrawDateTime().get(iLoop).contains("*")) {
					drawDataObject.addProperty("drawId", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[1]);
					drawDataObject.addProperty("drawDate", (Util.changeFormat("dd-MM-yyyy", "yyyy-MM-dd", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("\\*")[0].split(" ")[0])));
					drawDataObject.addProperty("drawTime", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("\\*")[0].split(" ")[1]);
					drawDataObject.addProperty("drawName", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("\\*")[1].split("&")[0]);
				} else {
					drawDataObject.addProperty("drawId", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[1]);
					drawDataObject.addProperty("drawDate", (Util.changeFormat("dd-MM-yyyy", "yyyy-MM-dd", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[0].split(" ")[0])));
					drawDataObject.addProperty("drawTime", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[0].split(" ")[1]);
				}
				drawDataArray.add(drawDataObject);
				drawDataObject = null;
			}
	
			String[] playTypeStr = kenoPurchaseBean.getBetDispName();
			JsonObject panelDataObject = null;
			int panelLength = kenoPurchaseBean.getBetAmountMultiple().length;
			for (int iLoop = 0; iLoop < panelLength; iLoop++) {
				panelDataObject = new JsonObject();
	
				panelDataObject.addProperty("betAmtMul", kenoPurchaseBean.getBetAmountMultiple()[iLoop]);
				panelDataObject.addProperty("pickedNumbers", kenoPurchaseBean.getPlayerData()[iLoop]);
				panelDataObject.addProperty("numberPicked", kenoPurchaseBean.getPlayerData()[iLoop].split(",").length);
				panelDataObject.addProperty("PlayType", playTypeStr[iLoop]);
				panelDataObject.addProperty("unitPrice", kenoPurchaseBean.getUnitPrice()[iLoop]);
				panelDataObject.addProperty("panelPrice", nf.format(kenoPurchaseBean.getBetAmountMultiple()[iLoop]* kenoPurchaseBean.getUnitPrice()[iLoop]* kenoPurchaseBean.getNoOfLines()[iLoop]* kenoPurchaseBean.getNoOfDraws()).replaceAll(",",""));
				panelDataObject.addProperty("QP", kenoPurchaseBean.getIsQuickPick()[iLoop]);
	
				panelDataArray.add(panelDataObject);
				panelDataObject = null;
			}
	
			responseObj.addProperty("responseCode", 0);
			responseObj.addProperty("saleTransId", kenoPurchaseBean.getRefTransId());
			responseObj.add("ticketData", tktDataJsonObject);
			responseObj.addProperty("availableBal", balance);
	
	//		responseObj.addProperty("", kenoPurchaseBean.get)
	
			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
	
			UtilApplet.getAdvMsgs(kenoPurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);
			responseObj.addProperty("topAdvMsg", new Gson().toJson(topMsgsStr.toString().split("~")));
			responseObj.addProperty("bottomAdvMsg", new Gson().toJson(bottomMsgsStr.toString().split("~")));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return responseObj.toString();
	}
	
	/*public static String reprintFortuneTicketNew(FortuneTwoPurchaseBean fortuneBean,
			String gameName) throws Exception {
		System.out.println(" gameName in reprint helper " + gameName);

		List<String> finalSymbols = null;
		if (gameName.equalsIgnoreCase("fortune")) {
			finalSymbols = Arrays.asList("", "Aries", "Taurus", "Gemini",
					"Cancer", "Leo", "Virgo", "Libra", "Scorpio",
					"Sagittarius", "Capricorn", "Aquarius", "Pisces");
		}

		String time = fortuneBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < fortuneBean.getPlayerData().length; i++) {
			stBuilder.append(fortuneBean.getPlayerData()[i]
					+ ":" + fortuneBean.getBetAmountMultiple()[i] + "|");
		}
		int listSize = fortuneBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + fortuneBean.getDrawDateTime()
					.get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace(".0", ""));
		}
	  
		//String raffleData = CommonMethods.buildRaffleData(fortuneBean.getRafflePurchaseBeanList());
		
		String finalData = "TicketNo:" + fortuneBean.getTicket_no()
				+ fortuneBean.getReprintCount() + "|Date:" + time + "|"
				+ stBuilder.toString() + "TicketCost:"
				+ fortuneBean.getTotalPurchaseAmt() + drawTimeBuild.toString()+ "|";
         
		System.out.println(" final " + gameName + " reprint data is "
				+ finalData);
		return finalData;
	}
*/
}
