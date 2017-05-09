package com.skilrock.lms.embedded.sportsLottery.common;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.EventMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameTypeMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PrizeRankDrawWinningBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketDrawDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.RetGameDataReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGameDrawDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGameEventDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGamePlayBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.WinningResultReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils;
import com.skilrock.lms.web.drawGames.common.Util;

public class SportsLotteryResponseData {
	static Log logger = LogFactory.getLog(SportsLotteryResponseData.class);

	public static String generateDrawGameData(List<GameMasterBean> gameMasterList) {
		StringBuilder responseString = new StringBuilder("");
		responseString.append("sportsData:");
		for(GameMasterBean gameMasterBean : gameMasterList) {
			responseString.append(gameMasterBean.getGameId()).append(",")
				.append(gameMasterBean.getGameDevName()).append(",")
				.append(gameMasterBean.getGameDispName()).append(",")
				.append(gameMasterBean.getThersholdTickerAmt()).append(",")
				.append(gameMasterBean.getMaxTicketAmt()).append(",")
				.append(gameMasterBean.getMinBoardCount()).append(",")
				.append(gameMasterBean.getMaxBoardCount());
			List<GameTypeMasterBean> gameTypeMasterList = gameMasterBean.getGameTypeMasterList();
			for(GameTypeMasterBean gameTypeMasterBean : gameTypeMasterList) {
				String eventType = gameTypeMasterBean.getEventType();
				//List<String> eventOptionsList = null;
				String eventOptionsList = null;
				if("SIMPLE".equals(eventType)) {
					
					List<DrawMasterBean> drawMasterList=gameTypeMasterBean.getDrawMasterList();
					if(drawMasterList != null && drawMasterList.size() >0){
						eventOptionsList = drawMasterList.get(0).getEventMasterList().get(0).getEventOptionsList().toString();
					}else{
						eventOptionsList="";
					}
				}
				responseString.append("$").append(gameTypeMasterBean.getGameTypeId()).append(",")
					.append(gameTypeMasterBean.getGameTypeDevName()).append(",")
					.append(gameTypeMasterBean.getGameTypeDispName()).append(",")
					.append(gameTypeMasterBean.getUnitPrice()).append(",")
					.append(gameTypeMasterBean.getMaxBetAmtMultiple()).append(",")
					//.append(gameTypeMasterBean.getEventType());
					.append(eventOptionsList);
					//.append("&CD:");
				List<DrawMasterBean> drawMasterList = gameTypeMasterBean.getDrawMasterList();
				if(drawMasterList.size()>0) {
					responseString.append("&CD:");
					for(DrawMasterBean drawMasterBean : drawMasterList) {
						responseString.append(drawMasterBean.getDrawId()).append(",")
							.append(drawMasterBean.getDrawNo()).append(",")
							.append(drawMasterBean.getDrawDateTime()).append(",")
							//.append(drawMasterBean.getDrawFreezeTime()).append(",")
							.append("50,")
							.append( drawMasterBean.getDrawDisplayType());
						List<EventMasterBean> eventMasterList = drawMasterBean.getEventMasterList();
						for(EventMasterBean eventMasterBean : eventMasterList) {
							responseString.append("%").append(eventMasterBean.getEventId()).append(",")
								.append(eventMasterBean.getEventDisplay()).append(",");
							/*
							List<String> eventOptionsList = eventMasterBean.getEventOptionsList();
							responseString.append(eventOptionsList);
							*/
						}
						
						responseString.deleteCharAt(responseString.length()-1);
						responseString.append("|");
					}
					responseString.deleteCharAt(responseString.length()-1);
				}
			}

			responseString.append("#");
		}
		responseString.deleteCharAt(responseString.length()-1);
		return responseString.toString();
	}
	
	public static String generateSportsLotterySaleResponseData(SportsLotteryGamePlayBean gamePlayBeanResponse,String balance) throws SLEException {
		StringBuilder responseString = new StringBuilder("");
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		StringBuilder jackpotMsg=null;
		Map<Integer, PrizeRankDrawWinningBean> prizeDistributionMap=null;
		try{
		
	/*	currDate:2013:11:11|currTime:12:12:12|ticketNo=12453458584|brCd=54565465464612|ticketAmt=124.45|balance=456.12|
				gameId=124|gameTypeID=25|gameName=SOCCER|gameTypeName=Soccer8|
				drawId=<drawDate>,<drawTime>,<drawDisplay>,<drawId>,<unitPrice*betAmtMultiple>,<boardPrice>,<noOfLines>~<eventDescription@1,X>#<eventDescription@1,X>&
				drawId=<drawDate>,<drawTime>,<drawDisplay>,<drawId>,<unitPrice*betAmtMultiple>,<boardPrice>,<noOfLines>~<eventDescription@1,X>#<eventDescription@1,X>&
	*/	String time = gamePlayBeanResponse.getPurchaseTime().replace(" ", "|currTime:");
						responseString.append("currDate:").append(time);
						responseString.append("|ticketNo:"+gamePlayBeanResponse.getTicketNumber()+gamePlayBeanResponse.getReprintCount());
						responseString.append("|brCd:"+gamePlayBeanResponse.getTicketNumber()+gamePlayBeanResponse.getReprintCount()+(gamePlayBeanResponse.getBarcodeCount()>0?gamePlayBeanResponse.getBarcodeCount():""));
						String ticketAmt = nf.format(gamePlayBeanResponse.getTotalPurchaseAmt()).replaceAll(",", "");

						responseString.append("|ticketAmt:"+ticketAmt);
						responseString.append("|balance:"+balance);
						responseString.append("|gameId:"+gamePlayBeanResponse.getGameId());
						responseString.append("|gameTypeId:"+gamePlayBeanResponse.getGameTypeId());
						responseString.append("|gameName:"+SportsLotteryUtils.gameInfoMap.get(gamePlayBeanResponse.getGameId()).getGameDispName());
						responseString.append("|gameTypeName:"+SportsLotteryUtils.gameTypeInfoMap.get(gamePlayBeanResponse.getGameTypeId()).getGameTypeDispName());
						
						Map<Integer, String> drawDataMap=new LinkedHashMap<Integer, String>();
						Map<Integer, String> jackpotDataMap=new HashMap<Integer, String>();
						
						String displayType=Utility.getPropertyValue("SPORTS_LOTTERY_TICKET_TYPE");
						
						if(displayType.equals("DRAW_WISE")){
							
							for(int i=0;i<gamePlayBeanResponse.getNoOfBoard();i++){
								SportsLotteryGameDrawDataBean gameDrawDataBean= gamePlayBeanResponse.getGameDrawDataBeanArray()[i];
								
								if(drawDataMap.containsKey(gameDrawDataBean.getDrawId())){
									StringBuilder drawStringBuild=new StringBuilder(drawDataMap.get(gameDrawDataBean.getDrawId()));
									drawStringBuild.append("$");
									String betAmt = nf.format(gameDrawDataBean.getBetAmountMultiple()*SportsLotteryUtils.gameTypeInfoMap.get(gamePlayBeanResponse.getGameTypeId()).getUnitPrice()).replaceAll(",", "");
									
									drawStringBuild.append(betAmt+",");
									
									String boardPurchaseAmt = nf.format(gameDrawDataBean.getBoardPurchaseAmount()).replaceAll(",", "");
									
									
									drawStringBuild.append(boardPurchaseAmt+",");
																	
									drawStringBuild.append(gameDrawDataBean.getNoOfLines()+"~");
									
									for(int j=0;j<gameDrawDataBean.getGameEventDataBeanArray().length;j++){
										SportsLotteryGameEventDataBean eventDataBean =gameDrawDataBean.getGameEventDataBeanArray()[j];
										
										drawStringBuild.append(eventDataBean.getEventDescription()+"@");
									for(int k=0;k<eventDataBean.getSelectedOption().length;k++){
										drawStringBuild.append(eventDataBean.getSelectedOption()[k]+",");
									}
									
									drawStringBuild.deleteCharAt(drawStringBuild.length()-1);
									
									drawStringBuild.append("#");
									
									}
									drawStringBuild.deleteCharAt(drawStringBuild.length()-1);
									drawDataMap.put(gameDrawDataBean.getDrawId(), drawStringBuild.toString());
									
								}else{
									StringBuilder drawStringBuild=new StringBuilder();
									drawStringBuild.append("|drawInfo:");
									drawStringBuild.append(gameDrawDataBean.getDrawDateTime().replace(" ", ",")+",");
									drawStringBuild.append(gameDrawDataBean.getDrawDisplayname()+",");
									drawStringBuild.append(gameDrawDataBean.getDrawId()+",");
									
									
									
									
									//set jackpot message string
									//jackpotMsg:current winning amt@still groving$Match 10@10000.00&Match 9@10000.00&match 8@10000.00|
									jackpotMsg=new StringBuilder("");
										Map<Integer, PrizeRankDrawWinningBean> rankDistributionmap=gameDrawDataBean.getDrawPrizeRankMap();
										prizeDistributionMap=new TreeMap<Integer, PrizeRankDrawWinningBean>();
										prizeDistributionMap.putAll(rankDistributionmap);
									if(prizeDistributionMap !=null && prizeDistributionMap.size()>0){
										jackpotMsg.append("jackpotMsg:Current Winning Amount@Still Growing~");
										for(Map.Entry<Integer, PrizeRankDrawWinningBean> entry: prizeDistributionMap.entrySet()){
											
											String prizeVal = nf.format(entry.getValue().getPrizeValue()).replaceAll(",", "");

											
											jackpotMsg.append(entry.getValue().getRankName()).append("@").append(prizeVal).append("~");
										}
										jackpotMsg.deleteCharAt(jackpotMsg.length()-1);
										jackpotMsg.append("|");
									}
										
									jackpotDataMap.put(gameDrawDataBean.getDrawId(), jackpotMsg.toString());
									
									
									String betAmt = nf.format(gameDrawDataBean.getBetAmountMultiple()*SportsLotteryUtils.gameTypeInfoMap.get(gamePlayBeanResponse.getGameTypeId()).getUnitPrice()).replaceAll(",", "");
									
									drawStringBuild.append(betAmt+",");
									
									
									
									
									String boardPurchaseAmt = nf.format(gameDrawDataBean.getBoardPurchaseAmount()).replaceAll(",", "");
									
									
									drawStringBuild.append(boardPurchaseAmt+",");
									drawStringBuild.append(gameDrawDataBean.getNoOfLines()+"~");
									
									for(int j=0;j<gameDrawDataBean.getGameEventDataBeanArray().length;j++){
										SportsLotteryGameEventDataBean eventDataBean =gameDrawDataBean.getGameEventDataBeanArray()[j];
										
										drawStringBuild.append(eventDataBean.getEventDescription()+"@");
									for(int k=0;k<eventDataBean.getSelectedOption().length;k++){
										drawStringBuild.append(eventDataBean.getSelectedOption()[k]+",");
									}
									
									drawStringBuild.deleteCharAt(drawStringBuild.length()-1);
									
									drawStringBuild.append("#");
									
									}
									drawStringBuild.deleteCharAt(drawStringBuild.length()-1);
									drawDataMap.put(gameDrawDataBean.getDrawId(), drawStringBuild.toString());
								}
							
								
							}		
							
							for(Map.Entry<Integer, String> drawData: drawDataMap.entrySet()){
								responseString.append(drawData.getValue()+jackpotDataMap.get(drawData.getKey()));
							}
							
						}else{
							for(int i=0;i<gamePlayBeanResponse.getNoOfBoard();i++){
								SportsLotteryGameDrawDataBean gameDrawDataBean= gamePlayBeanResponse.getGameDrawDataBeanArray()[i];
								StringBuilder drawStringBuild=new StringBuilder();
								drawStringBuild.append("|drawInfo:");
								drawStringBuild.append(gameDrawDataBean.getDrawDateTime().replace(" ", ",")+",");
								drawStringBuild.append(gameDrawDataBean.getDrawDisplayname()+",");
								drawStringBuild.append(gameDrawDataBean.getDrawId()+",");
								
								String betAmt = nf.format(gameDrawDataBean.getBetAmountMultiple()*SportsLotteryUtils.gameTypeInfoMap.get(gamePlayBeanResponse.getGameTypeId()).getUnitPrice()).replaceAll(",", "");
								
								drawStringBuild.append(betAmt+",");
								
								
								
								
								String boardPurchaseAmt = nf.format(gameDrawDataBean.getBoardPurchaseAmount()).replaceAll(",", "");
								
								
								drawStringBuild.append(boardPurchaseAmt+",");
								drawStringBuild.append(gameDrawDataBean.getNoOfLines()+"~");
								
								for(int j=0;j<gameDrawDataBean.getGameEventDataBeanArray().length;j++){
									SportsLotteryGameEventDataBean eventDataBean =gameDrawDataBean.getGameEventDataBeanArray()[j];
									
									drawStringBuild.append(eventDataBean.getEventDescription()+"@");
								for(int k=0;k<eventDataBean.getSelectedOption().length;k++){
									drawStringBuild.append(eventDataBean.getSelectedOption()[k]+",");
								}
								
								drawStringBuild.deleteCharAt(drawStringBuild.length()-1);
								
								drawStringBuild.append("#");
								
								}
								drawStringBuild.deleteCharAt(drawStringBuild.length()-1);
								
								
								//set jackpot message string
								//jackpotMsg:current winning amt@still groving$Match 10@10000.00&Match 9@10000.00&match 8@10000.00|
								jackpotMsg=new StringBuilder("");
								Map<Integer, PrizeRankDrawWinningBean> rankDistributionmap=gameDrawDataBean.getDrawPrizeRankMap();
								prizeDistributionMap=new TreeMap<Integer, PrizeRankDrawWinningBean>();
								prizeDistributionMap.putAll(rankDistributionmap);
								if(prizeDistributionMap !=null && prizeDistributionMap.size()>0){
									jackpotMsg.append("jackpotMsg:Current Winning Amount@Still Growing~");
									for(Map.Entry<Integer, PrizeRankDrawWinningBean> entry: prizeDistributionMap.entrySet()){
										String prizeVal = nf.format(entry.getValue().getPrizeValue()).replaceAll(",", "");

										jackpotMsg.append(entry.getValue().getRankName()).append("@").append(prizeVal).append("~");
									}
									jackpotMsg.deleteCharAt(jackpotMsg.length()-1);
									jackpotMsg.append("|");
								}
									
								
								
								
								responseString.append(drawStringBuild.toString()+jackpotMsg.toString());
							}
						
						}
						
				
				
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return responseString.toString();
	}

	public static String generateSportsLotteryPwtResponseData(PwtVerifyTicketBean pwtVerifyTicketBean,String balance) throws SLEException {
		StringBuilder responseString = new StringBuilder("");
	/*	winData:drawTime:2013-04-29 15:00:00,drawName:CONTINENTAL,No:1,message:CLAIMED,prizeAmt:0.0|
	    drawTime:2013-04-29 15:00:00,drawName:CONTINENTAL,No:1,message:CLAIMED,prizeAmt:0.0|
	     
	totalPay:0.0|
	totalClmPend:1444.00|
	balance:0.0|*/
		try{
			responseString.append("winData:");
			String time = Util.getCurrentTimeString().replace(" ", "|currTime:");
			responseString.append("currDate:").append(time).append("|");
			responseString.append("ticketNo:").append(pwtVerifyTicketBean.getTicketNumber()).append("|");
			responseString.append("gameName:").append(pwtVerifyTicketBean.getGameName()).append("|");
			responseString.append("gameTypeName:").append(pwtVerifyTicketBean.getGameTypename()).append("|");
			
			for(int i=0;i<pwtVerifyTicketBean.getVerifyTicketDrawDataBeanArray().length;i++){
				PwtVerifyTicketDrawDataBean pwtVerifyDrawTicketBean=pwtVerifyTicketBean.getVerifyTicketDrawDataBeanArray()[i];
				responseString.append("drawTime:"+pwtVerifyDrawTicketBean.getDrawDateTime()+",");
				responseString.append("drawName:"+pwtVerifyDrawTicketBean.getDrawName()+",");
				responseString.append("No:"+pwtVerifyDrawTicketBean.getBoardCount()+",");
				responseString.append("message:"+pwtVerifyDrawTicketBean.getMessage()+",");
				responseString.append("prizeAmt:"+pwtVerifyDrawTicketBean.getDrawWinAmt()+"|");
				
				
			}
			
			responseString.append("totalPay:"+pwtVerifyTicketBean.getTotalWinAmt()+"|");
			responseString.append("totalClmPend:"+0.0+"|");
			responseString.append("balance:"+balance+"|");
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return responseString.toString();
	}

	public static final String generateGameDataReportDate(String retailerName, Map<String, List<RetGameDataReportBean>> gameDataReportMap, String reportDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date nowDate = new Date();
		String currentDate = dateFormat.format(nowDate);
		String currentTime = timeFormat.format(nowDate);

		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);

		double totalSaleAmt = 0;
		double totalPwtAmt = 0;

		StringBuilder responseDate = new StringBuilder("SlReportData:")
			.append("retName:").append(retailerName)
			.append("|Date:").append(currentDate)
			.append("|Time:").append(currentTime)
			.append("|ReportDate:").append(reportDate);
		for(String gameName : gameDataReportMap.keySet()) {
			responseDate.append("#gameName:").append(gameName);
			List<RetGameDataReportBean> gameDataReportList = gameDataReportMap.get(gameName);
			for(RetGameDataReportBean gameDataReportBean : gameDataReportList) {
				double saleAmt = gameDataReportBean.getSaleAmount();
				double pwtAmt = gameDataReportBean.getPwtAmount();
				totalSaleAmt += saleAmt;
				totalPwtAmt += pwtAmt;

				responseDate.append("$gameTypeName:").append(gameDataReportBean.getGameTypeName())
					.append(",GameS:").append(numberFormat.format(saleAmt).replaceAll(",", ""))
					.append(",GamePwt:").append(numberFormat.format(pwtAmt).replaceAll(",", ""));
			}
		}
		responseDate.append("|TotalSale:").append(numberFormat.format(totalSaleAmt).replaceAll(",", ""))
			.append("|TotalPWT:").append(numberFormat.format(totalPwtAmt).replaceAll(",", ""));

		return responseDate.toString();
	}

	public static String generateWinningResultReportData(String userName, List<WinningResultReportBean> winningResultReportList) {
		if(winningResultReportList.size()==0)
			return "ErrorMsg:Result Awaited.";

		WinningResultReportBean winningResultReportBean = winningResultReportList.get(0);
		StringBuilder responseData = new StringBuilder("gameName:").append(winningResultReportBean.getGameName()).append("|")
				.append("gameTypeName:").append(winningResultReportBean.getGameTypeName()).append("|")
				.append("drawDate:").append(winningResultReportBean.getDrawDate()).append("|")
				.append("drawTime:").append(winningResultReportBean.getDrawTime()).append("|")
				.append("drawName:").append(winningResultReportBean.getDrawName()).append("|")
				.append("eventInfo:");
		Map<String, String> eventOptionMap = winningResultReportBean.getEventOptionMap();
		if(eventOptionMap.size() == 0) {
			return "ErrorMsg:Result Awaited.";
			//responseDate.append("AWAITED");
		} else {
			Set<String> eventDisplaySet = eventOptionMap.keySet();
			for(String eventDisplay : eventDisplaySet) {
				String optionCode = eventOptionMap.get(eventDisplay);
				responseData.append(eventDisplay).append("@").append(optionCode).append("~");
			}
			responseData.deleteCharAt(responseData.length()-1);
		}
		return responseData.toString();
	}
}