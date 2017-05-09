package com.skilrock.lms.embedded.drawGames.common;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class KenoReprint extends BaseReprintController{

	@Override
	public String prepareFinalResponse(EmbeddedReprint embeddedReprint) {
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";
		String finalReprintData = null;
		String gameName = null;
		try{
			KenoPurchaseBean bean = (KenoPurchaseBean) embeddedReprint.getGameBean();
			UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,bottomMsgsStr, 10);
			if("KenoFive".equalsIgnoreCase(Util.getGameName(bean.getGameId())) || "KenoFour".equalsIgnoreCase(Util.getGameName(bean.getGameId())))
			if(Arrays.asList(bean.getPlayType()).toString().contains("DC-")){
				String dblChncMsg= com.skilrock.lms.common.Utility.getPropertyValue("MSG_FOR_DC");
				String posForDcMsg= com.skilrock.lms.common.Utility.getPropertyValue("POSITION_FOR_DC_MSG");
				if("BOTTOM".equalsIgnoreCase(posForDcMsg))
					bottomMsgsStr.append("~").append(dblChncMsg);
				else if("TOP".equalsIgnoreCase(posForDcMsg))
					topMsgsStr.append("~").append(dblChncMsg);
			}
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
			gameName = Util.getGameName(bean.getGameId());
			String reprintData = ReprintHepler.reprintKenoTicket(bean,gameName,embeddedReprint.getUserInfoBean().getTerminalBuildVersion(),embeddedReprint.getCountryDeployed());
			String promoReprintData = buildPromoReprintData(bean.getPromoPurchaseBean(),embeddedReprint.getUserInfoBean(),embeddedReprint.getCountryDeployed());
			String raffleDrawDate="";
			String raffleGameDataString= embeddedReprint.getRaffleGameDataString();
			raffleGameDataString=raffleGameDataString.substring(raffleGameDataString.indexOf(":")+1,raffleGameDataString.length());
			String isMobNo=null;
			double rAmount=0;
			boolean isRaffleGame=false;
			String raffleGameData=null;
	        if(!"".equals(raffleGameDataString)){
	            String[] raffGameArray=raffleGameDataString.split("#");
	            for(int i=0;i < raffGameArray.length;i++){
	                raffleGameData=raffGameArray[i];
	                if(gameName.equalsIgnoreCase(raffleGameData.substring(0,raffleGameData.indexOf("%")))){
	        			rAmount=Double.parseDouble(raffleGameData.substring(raffleGameData.indexOf("%")+1, raffleGameData.lastIndexOf("%")));
	        			isMobNo=raffleGameData.substring(raffleGameData.lastIndexOf("%")+1,raffleGameData.length() );
	        			isRaffleGame=true;
	        		}
	            }
			
	        }
			if(!"NA".equals(embeddedReprint.getRaffleDrawDay()) && bean.getTotalPurchaseAmt() >= rAmount && isRaffleGame){
				if("N".equals(isMobNo) || bean.getPlrMobileNumber() != null && bean.getPlrMobileNumber().length() > 9){
					Map<String, Integer> dayMap = new HashMap<String, Integer>();
					dayMap.put("SUNDAY", 1);
					dayMap.put("MONDAY", 2);
					dayMap.put("TUESDAY", 3);
					dayMap.put("WEDNESDAY", 4);
					dayMap.put("THURSDAY", 5);
					dayMap.put("FRIDAY", 6);
					dayMap.put("SATURDAY", 7);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date purchaseDate = format.parse(bean.getPurchaseTime());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(purchaseDate);
					format = new SimpleDateFormat("dd-MM-yyyy");
					int today = calendar.get(Calendar.DAY_OF_WEEK);
					int nxtDay = dayMap.get(embeddedReprint.getRaffleDrawDay());
					if(today != nxtDay) {
						int days = (Calendar.SATURDAY - today + nxtDay) % 7;  
					    calendar.add(Calendar.DAY_OF_YEAR, days);
					}else{
						calendar.add(Calendar.DAY_OF_YEAR, 7);
					}
					raffleDrawDate="RDate:"+format.format(calendar.getTime())+"|";
					if("Y".equals(isMobNo))
						raffleDrawDate=raffleDrawDate+"PNo:"+bean.getPlrMobileNumber()+"|";
				}
			}
			finalReprintData = reprintData + "|balance:" + embeddedReprint.getBalance() + "|QP:"+ bean.getIsQuickPick()[0] + "|"+raffleDrawDate + promoReprintData + advtMsg;
		}catch(Exception e){
			e.printStackTrace();
		}
		return finalReprintData;
	}

}
