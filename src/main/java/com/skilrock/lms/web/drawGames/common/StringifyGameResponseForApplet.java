package com.skilrock.lms.web.drawGames.common;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;


public class StringifyGameResponseForApplet {
	static Log logger = LogFactory.getLog(StringifyGameResponseForApplet.class);

	
	@SuppressWarnings("unchecked")
	public static String stringifyGameResponseForApplet(UserInfoBean userInfoBean , LottoPurchaseBean lottoBean){
		
		logger.info("Preparing Purchase Data For LottoBonus GAME");
		int appletHeight = 0;
		int buyModeHeight = 0;
		int constantSize = 2260;
		
		String pickNumStr = "";
		String verifiedAt = null;
		String quickPickArray = null;
		StringBuilder drawDateTimeStr = null;
		StringBuilder finalPurchaseData = null;
		
	if ("SUCCESS".equals(lottoBean.getSaleStatus())) {
			String orgAdd = Utility.getPropertyValue("ORG_ADDRESS");
			String orgName = Utility.getPropertyValue("ORG_NAME_JSP");
			String orgMobile = Utility.getPropertyValue("ORG_MOBILE");
			String isAdvMessage = Utility.getPropertyValue("ADVT_MSG");
			String currencySymbol = Utility.getPropertyValue("CURRENCY_SYMBOL");
			verifiedAt = userInfoBean.getOrgName();
			String ticketExpiryEnabled = Utility.getPropertyValue("TICKET_EXPIRY_ENABLED");
			quickPickArray = Arrays.asList(lottoBean.getIsQuickPick()).toString().replace("[","").replace("]", "").replace(" ", "");	

			java.util.Iterator<String> it = lottoBean.getPlayerPicked().iterator();
			while(it.hasNext()){
				pickNumStr += it.next()+";";
			}
			drawDateTimeStr = new StringBuilder("");
			for(Object drawDateTime : lottoBean.getDrawDateTime()){
				drawDateTimeStr.append(drawDateTime.toString().split("&")[0] + ",");
			}
			buyModeHeight = lottoBean.getDrawDateTime().size()*11;
			appletHeight = buyModeHeight + constantSize	+ (pickNumStr.split(";").length * 11);
			appletHeight += (Integer.parseInt(lottoBean.getReprintCount()) > 0) ?16:0; 
			appletHeight += "YES".equalsIgnoreCase(ticketExpiryEnabled) ? 11 : 0;

			StringBuilder raffleData = new StringBuilder(" ");
			if(lottoBean.getRafflePurchaseBeanList() != null){
				logger.info("Preparing Raffle Data");
				appletHeight  = Util.getRaffData(lottoBean.getRafflePurchaseBeanList(), raffleData, appletHeight);
			}
			StringBuilder topMsgsStr = new StringBuilder(" ");
			StringBuilder bottomMsgsStr = new StringBuilder(" ");
			if(lottoBean.getAdvMsg() != null){
				logger.info("Preparing Adv. Messages");
				appletHeight = Util.getAdvMsgs(lottoBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);
			}
			String totalQuantity=lottoBean.getPanel_id().length +"";
			if("Perm6".equalsIgnoreCase(lottoBean.getPlayType())){
				totalQuantity=lottoBean.getNoOfLines()+"";
			}
			finalPurchaseData = new StringBuilder("");
			String ticketNumber  = lottoBean.getTicket_no() + lottoBean.getReprintCount() + 
							   (lottoBean.getBarcodeCount()!=-1 && "true".equalsIgnoreCase(Utility.getPropertyValue("IS_BARCODE_REQUIRED"))
							   ? lottoBean.getBarcodeCount() : "");

			String retName= Util.getOrgNameFromTktNo((lottoBean.getTicket_no() + lottoBean.getReprintCount()), 
																					Utility.getPropertyValue("ORG_TYPE_ON_TICKET"));

		finalPurchaseData.append("data=").append(ticketNumber).append("|userName=").append(userInfoBean.getUserName()).append("|gameName=Zimlottotwo").append("|mode=Buy|saleStatus=").
			append(lottoBean.getSaleStatus()).append("|reprintCount=").append(lottoBean.getReprintCount()).
			append("|purchaseTime=").append(lottoBean.getPurchaseTime()).append("|gameDispName=" + lottoBean.getGameDispName()).
			append("|ticketNumber=" + lottoBean.getTicket_no()).append("|drawDateTime=").append(drawDateTimeStr.toString()).
			append( "|pickedNumbers=") .append(pickNumStr).append("|isQuickPickArray=") .append(quickPickArray).
			append( "|expiryPeriod=") .append(Util.getGameMap().get(lottoBean.getGameId()).getTicketExpiryPeriod()).append( "|noOfDraws=").
			append( lottoBean.getNoOfDraws()).append( "|playType=") .append( lottoBean.getPlayType()).append( "|unitPrice=").
			append( lottoBean.getUnitPrice()).append( "|currSymbol=") .append(currencySymbol).append( "|totalPurchaseAmt=").
			append( lottoBean.getTotalPurchaseAmt()).append( "|orgName=") .append(orgName).append( "|advtMsg=").
			append(isAdvMessage).append( "|topAdvMsg=") .append( topMsgsStr.append( "|bottomAdvMsg=") .append( bottomMsgsStr).
			append( "|jackPotAmt=").append(((Map)ServletActionContext.getServletContext().getAttribute("jackPotMap")).get(lottoBean.getGameId())).
			append( "|raffleData=").append( raffleData).append( "|orgAddress=") .append(orgAdd).append( "|orgMobile=").
			append(orgMobile).append( "|ticketExpiryEnabled=") .append( ticketExpiryEnabled).append( "|totalQuantity=").
			append( totalQuantity).append("|retailerName=") .append(retName).append("|verifiedAt=") .append( verifiedAt).append( "|ctr=") .append( appletHeight));

			StringBuilder finalPromoData = new StringBuilder("");
			if(lottoBean.getPromoPurchaseBeanList() != null){
				String promoOriginalData = new StringBuilder("").append("|currSymbol=").append(currencySymbol).
					append("|orgName=").append(orgName).append("|advtMsg=").append(isAdvMessage).
					append("|orgAddress=").append(orgAdd).append("|orgMobile=").append(orgMobile).
					append("|retailerName=").append(retName).append("|retailerNameReprint=").append(retName).append("|verifiedAt=").append(verifiedAt).
					append("|ticketExpiryEnabled=").append(ticketExpiryEnabled).toString();
				
				int size = lottoBean.getPromoPurchaseBeanList().size();
				for(int k = 0 ; k < size ; k++){
						String promoData="";
						promoData = Util.getPromoData(lottoBean.getPromoPurchaseBeanList().get(k) , promoOriginalData);
						if(promoData.length() > 0){
							promoData = "PROMO" + promoData + "|ctr=" + (appletHeight + constantSize + 300);
							finalPromoData.append(promoData);
					}
				}
			}
				logger.info("ZIM LOTTO BONUS MAIN TICKET PURCHASE : " + finalPurchaseData.toString());
				finalPurchaseData.append(finalPromoData);
				long responseCRC=Util.getCheckSum(finalPurchaseData.toString());
				finalPurchaseData.append("|CRC=").append(responseCRC);
				logger.info("ZIM LOTTO BONUS PURCHASE CUM PROMO DATA WITH CRC: " + finalPurchaseData.toString());
			} else {
				finalPurchaseData = new StringBuilder("ERROR");
		}
			return finalPurchaseData.toString();
	}
	
	public static String stringifyGameResponseForApplet(UserInfoBean userInfoBean , KenoPurchaseBean kenoBean){
		StringBuilder finalPurchaseData = new StringBuilder();
		return finalPurchaseData.toString();
	}
	
}
