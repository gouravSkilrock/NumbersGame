package com.skilrock.lms.keba.drawGames.playMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneThreePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.embedded.drawGames.common.ReprintHepler;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class ReprintTicketAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public ReprintTicketAction() {
		super(ReprintTicketAction.class);
	}

	private String requestData;

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public void reprintTicket() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		//SaleResponseBean responseBean = new SaleResponseBean();

		String finalReprintData = null;
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";
		try {
			JSONObject reprintRequestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
			logger.info("Reprint Request Data : " + reprintRequestData);

			response.setContentType("application/json");
			out = response.getWriter();

			String userName = reprintRequestData.getString("userName").trim();
			//long lastTktNo = Long.parseLong(reprintRequestData.getString("lastTktNo").trim());
			long lastTktNo = 0;

			String isDraw = (String) sc.getAttribute("IS_DRAW");
			String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");
			if (isDraw.equalsIgnoreCase("NO")) {
				responseObject.put("errorMsg", EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE);
				responseObject.put("isSuccess", false);
				return;
			} else {
				UserInfoBean userInfoBean = getUserBean(userName);
				DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

				String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
				int autoCancelHoldDays = Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
				long lastPrintedTicket = 0;
				int gameId = 0;
				if(lastTktNo != 0) {
					lastPrintedTicket = lastTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(lastTktNo).length());
					gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTktNo)));
				}
				String actionName = ActionContext.getContext().getName();
				helper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);

				Object gameBean = helper.reprintTicket(userInfoBean, false, null, true, null, "TERMINAL");
				String gameName = null;

				double bal = userInfoBean.getAvailableCreditLimit() - userInfoBean.getClaimableBal();
				NumberFormat nFormat = NumberFormat.getInstance();
				nFormat.setMinimumFractionDigits(2);
				String balance = nFormat.format(bal).replace(",", "");

				if (gameBean instanceof FortunePurchaseBean) {
				final List sunSign = Arrays.asList("", "Aries", "Taurus",
							"Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio",
							"Sagittarius", "Capricorn", "Aquarius", "Pisces");
					FortunePurchaseBean bean = (FortunePurchaseBean) gameBean;
					UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,
							bottomMsgsStr, 10);

					if (topMsgsStr.length() != 0) {
						advtMsg = "topAdvMsg:" + topMsgsStr + "|";
					}

					if (bottomMsgsStr.length() != 0) {
						advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
					}
					if (bean.getSaleStatus() == "PERFORMED") {
						responseObject.put("errorMsg", "Draw Performed.");
						responseObject.put("isSuccess", false);
						return;
					}
					gameName = Util.getGameName(bean.getGame_no());
				/*	String reprintData = ReprintHepler.reprintFortuneTicket(bean,
							gameName);*/
					/*StringBuilder stBuilder = new StringBuilder("");
					for (int i = 0; i < bean.getPlayerPicked().size(); i++) {
						stBuilder.append("|Num:" + bean.getPlayerPicked().get(i) + "|QP:" + bean.getIsQuickPick()[i]);
					}*/

					int listSize = bean.getDrawDateTime().size();
					StringBuilder drawTimeBuild = new StringBuilder("");
					for (int i = 0; i < listSize; i++) {
						drawTimeBuild.append(("|DrawDate:" + bean.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
					}

					JSONArray betTypeArray = new JSONArray();
					
					for (int i = 0; i < bean.getPickedNumbers().size(); i++) {
						JSONObject panelObj = new JSONObject();
						panelObj.put("pickedNumbers",sunSign.get(bean.getPickedNumbers().get(i)));
						panelObj.put("betAmtMul",  bean.getBetAmountMultiple()[i] );
						panelObj.put("isQp",bean.getIsQP()==1 ? true:false);
						panelObj.put("betName","Fortune");
						betTypeArray.add(panelObj);
						//stBuilder.append(sunSign.get(fortuneBean.getPickedNumbers().get(i))	+ ":" + fortuneBean.getBetAmountMultiple()[i] + "|");
					}
					JSONArray  drawDataArray = new JSONArray();
					for (int i = 0; i < listSize; i++) {
						JSONObject drawObject = new JSONObject();
						String drawData =(String)bean.getDrawDateTime().get(i) ;
						drawObject.put("drawId",Integer.parseInt(drawData.split("\\*")[0]));
						drawObject.put("drawDate",drawData.split("\\*")[1].split(" ")[0]);
						drawObject.put("drawTime", drawData.split("\\*")[1].split(" ")[1]);
						drawObject.put("drawName", drawData.split("\\*")[2]);
						drawDataArray.add(drawObject);
						/*drawDatearray.add(fortuneBean
								.getDrawDateTime().get(i).toString().split(" ")[0]);
						drawTimearray.add(fortuneBean
								.getDrawDateTime().get(i).toString().split(" ")[1]);
						drawTimeBuild.append(("|DrawDate:" + fortuneBean
								.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace(
								"#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));*/
					}
				/*	JSONArray drawDataArray = new JSONArray();
					JSONObject drawData = null;
					for (int i=0; i<listSize; i++) {
						String drawDataString = (String) bean.getDrawDateTime().get(i);
						//	2014-01-21 18:31:00*DRAWNAME&1701
						//	2014-01-22 19:45:00&1032
						drawData = new JSONObject();
						drawData.put("drawId", Integer.parseInt(drawDataString.split("&")[1]));
						drawData.put("drawDate", drawDataString.split(" ")[0]);
						String drawName[] = drawDataString.split("\\*");
						if(drawName.length < 2) {
							drawData.put("drawName", "");
							drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
						}
						else {
							drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
							drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
						}
						drawDataArray.add(drawData);
					}*/
					JSONObject commonSaleDataRes = new JSONObject();

					commonSaleDataRes.put("ticketNumber",bean.getTicket_no()+ bean.getReprintCount());
					commonSaleDataRes.put("brCd", bean.getTicket_no() + bean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? bean.getBarcodeCount():""));
					commonSaleDataRes.put("gameName", bean.getGameDispName());
					commonSaleDataRes.put("purchaseDate", bean.getPurchaseTime().split(" ")[0]);
					commonSaleDataRes.put("purchaseTime", bean.getPurchaseTime().split(" ")[1].split("\\.")[0]);
					commonSaleDataRes.put("purchaseAmt", bean.getTotalPurchaseAmt());
					commonSaleDataRes.put("balance", bal);
					commonSaleDataRes.put("unitPrice", bean.getUnitPrice());
					commonSaleDataRes.put("drawData", drawDataArray);

					responseObject.put("isSuccess", true);
					responseObject.put("errorMsg", "");
					responseObject.put("commonSaleData", commonSaleDataRes);
					responseObject.put("betTypeData", betTypeArray);

					String promoData = buildPromoReprintData(bean.getPromoPurchaseBean(), userInfoBean,countryDeployed);
					//finalReprintData = reprintData + "|balance:" + balance + "|QP:" + bean.getIsQuickPick()[0] + "|" + advtMsg + promoData;
				} else if (gameBean instanceof LottoPurchaseBean) {
					LottoPurchaseBean bean = (LottoPurchaseBean) gameBean;
					//responseBean.seterrorMsg(bean.getTicket_no());
					UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,
							bottomMsgsStr, 10);

					if (topMsgsStr.length() != 0) {
						advtMsg = "topAdvMsg:" + topMsgsStr + "|";
					}

					if (bottomMsgsStr.length() != 0) {
						advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
					}
					if (bean.getSaleStatus() == "PERFORMED") {
						responseObject.put("errorMsg", "Draw Performed.");
						responseObject.put("isSuccess", false);
						return;
					}

					//String reprintData = ReprintHepler.reprintLottoTicket(bean, gameName);

					StringBuilder stBuilder = new StringBuilder("");
					for (int i = 0; i < bean.getPlayerPicked().size(); i++) {
						stBuilder.append("|Num:" + bean.getPlayerPicked().get(i) + "|QP:" + bean.getIsQuickPick()[i]);
					}

					int listSize = bean.getDrawDateTime().size();
					StringBuilder drawTimeBuild = new StringBuilder("");
					for (int i = 0; i < listSize; i++) {
						drawTimeBuild.append(("|DrawDate:" + bean.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
					}

					JSONArray betTypeArray = new JSONArray();
					JSONObject betTypeDataRes = null;
					for (int i=0; i<bean.getPlayerPicked().size(); i++) {
						//JSONObject panelData = betTypeDataReq.getJSONObject(i);
						betTypeDataRes = new JSONObject();
						betTypeDataRes.put("betName", bean.getPlayType());
						betTypeDataRes.put("isQp", (bean.getIsQuickPick()[i]==1)?true:false);
						//betTypeDataRes.put("pickedNumbers", lottoPurchaseBean.getPlayerPicked().toString().replace("[", "").replace("]", ""));
						betTypeDataRes.put("pickedNumbers", bean.getPlayerPicked().get(i));
						betTypeDataRes.put("betAmtMul", 1);
						betTypeArray.add(betTypeDataRes);
					}

					JSONArray drawDataArray = new JSONArray();
					JSONObject drawData = null;
					for (int i=0; i<listSize; i++) {
						String drawDataString = (String) bean.getDrawDateTime().get(i);
						//	2014-01-21 18:31:00*DRAWNAME&1701
						//	2014-01-22 19:45:00&1032
						drawData = new JSONObject();
						drawData.put("drawId", Integer.parseInt(drawDataString.split("&")[1]));
						drawData.put("drawDate", drawDataString.split(" ")[0]);
						String drawName[] = drawDataString.split("\\*");
						if(drawName.length < 2) {
							drawData.put("drawName", "");
							drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
						}
						else {
							drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
							drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
						}
						drawDataArray.add(drawData);
					}
					JSONObject commonSaleDataRes = new JSONObject();
					commonSaleDataRes.put("ticketNumber", bean.getTicket_no() + bean.getReprintCount());
					commonSaleDataRes.put("brCd", bean.getTicket_no() + bean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? bean.getBarcodeCount():""));
					commonSaleDataRes.put("gameName", bean.getGameDispName());
					commonSaleDataRes.put("purchaseDate", bean.getPurchaseTime().split(" ")[0]);
					commonSaleDataRes.put("purchaseTime", bean.getPurchaseTime().split(" ")[1].split("\\.")[0]);
					commonSaleDataRes.put("purchaseAmt", bean.getTotalPurchaseAmt());
					commonSaleDataRes.put("balance", bal);
					commonSaleDataRes.put("unitPrice", bean.getUnitPrice());
					commonSaleDataRes.put("drawData", drawDataArray);
					commonSaleDataRes.put("topAdvMsg",topMsgsStr );
					commonSaleDataRes.put("bottomAdvMsg",bottomMsgsStr );

					responseObject.put("isSuccess", true);
					responseObject.put("errorMsg", "");
					responseObject.put("commonSaleData", commonSaleDataRes);
					responseObject.put("betTypeData", betTypeArray);

					//responseBean.set;

					List<RafflePurchaseBean> rafflePurchaseBeanList = bean.getRafflePurchaseBeanList();
					String raffleData = CommonMethods.buildRaffleData(rafflePurchaseBeanList);
					if (raffleData.length() != 0) {
						raffleData = raffleData + "|";
					}
					String promoData = "";
					if(bean.getPromoPurchaseBeanList()!=null) {
						List<LottoPurchaseBean> promoPurchaseBeanList=bean.getPromoPurchaseBeanList();
						promoData=ReprintHepler.buildSaleDataforZimlottothree(promoPurchaseBeanList,userInfoBean);
					}
					//finalReprintData = raffleData + advtMsg + promoData;
				} else if (gameBean instanceof KenoPurchaseBean) {
					KenoPurchaseBean bean = (KenoPurchaseBean) gameBean;
					UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,
							bottomMsgsStr, 10);

					if("KenoFive".equalsIgnoreCase(Util.getGameName(bean.getGameId()))) {
						if(Arrays.asList(bean.getPlayType()).toString().contains("DC-")){
							String dblChncMsg = com.skilrock.lms.common.Utility.getPropertyValue("MSG_FOR_DC");
							String posForDcMsg = com.skilrock.lms.common.Utility.getPropertyValue("POSITION_FOR_DC_MSG");
							if("BOTTOM".equalsIgnoreCase(posForDcMsg))
								bottomMsgsStr.append("~").append(dblChncMsg);
							else if("TOP".equalsIgnoreCase(posForDcMsg))
								topMsgsStr.append("~").append(dblChncMsg);
						}
					}

					if (topMsgsStr.length() != 0) {
						advtMsg = "topAdvMsg:" + topMsgsStr + "|";
					}
					if (bottomMsgsStr.length() != 0) {
						advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
					}
					if (bean.getSaleStatus() == "PERFORMED") {
						responseObject.put("errorMsg", "Draw Performed.");
						responseObject.put("isSuccess", false);
						return;
					}

					gameName = Util.getGameName(bean.getGameId());
					String reprintData = ReprintHepler.reprintKenoTicket(bean,
							gameName,userInfoBean.getTerminalBuildVersion(),countryDeployed);

					String promoReprintData = buildPromoReprintData(bean
							.getPromoPurchaseBean(),userInfoBean,countryDeployed);

					String raffleDrawDate="";

					String raffleDrawDay=(String) sc.getAttribute("RAFFLE_GAME_DRAW_DAY");
					String raffleGameDataString=(String) sc.getAttribute("RAFFLE_GAME_DATA");
					raffleGameDataString=raffleGameDataString.substring(raffleGameDataString.indexOf(":")+1,raffleGameDataString.length());

					String isMobNo=null;
					double rAmount=0;
					boolean isRaffleGame=false;
					String raffleGameData=null;
			        if(!"".equals(raffleGameDataString)) {
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

					if(!"NA".equals(raffleDrawDay) && bean.getTotalPurchaseAmt() >= rAmount && isRaffleGame) {
						if("N".equals(isMobNo) || bean.getPlrMobileNumber() != null && bean.getPlrMobileNumber().length() > 9) {	
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
							int nxtDay = dayMap.get(raffleDrawDay);

							if(today != nxtDay) {
								int days = (Calendar.SATURDAY - today + nxtDay) % 7;  
							    calendar.add(Calendar.DAY_OF_YEAR, days);
							}else{
								calendar.add(Calendar.DAY_OF_YEAR, 7);
							}

							raffleDrawDate="RDate:"+format.format(calendar.getTime())+"|";
							if("Y".equals(isMobNo)) {
								raffleDrawDate=raffleDrawDate+"PNo:"+bean.getPlrMobileNumber()+"|";
							}
							logger.info(raffleDrawDate);
						}
					}

					finalReprintData = reprintData + "|balance:" + balance + "|QP:"
					+ bean.getIsQuickPick()[0] + "|"+raffleDrawDate + promoReprintData
					+ advtMsg;
				} else if (gameBean instanceof FortuneTwoPurchaseBean) {
					FortuneTwoPurchaseBean bean = (FortuneTwoPurchaseBean) gameBean;
					UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,
							bottomMsgsStr, 10);

					if (topMsgsStr.length() != 0) {
						advtMsg = "topAdvMsg:" + topMsgsStr + "|";
					}
					if (bottomMsgsStr.length() != 0) {
						advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
					}
					if (bean.getSaleStatus() == "PERFORMED") {
						responseObject.put("errorMsg", "Draw Performed.");
						responseObject.put("isSuccess", false);
						return;
					}
					gameName = Util.getGameName(bean.getGame_no());
					String reprintData = ReprintHepler.reprintFortuneTwoTicket(
							bean, gameName);

					String promoReprintData = buildPromoReprintData(bean.getPromoPurchaseBean(),userInfoBean,countryDeployed);
					finalReprintData = reprintData + "|balance:" + balance + "|QP:"
					+ bean.getIsQuickPick()[0] + "|" + promoReprintData
					+ advtMsg;
				} else if(gameBean instanceof FortuneThreePurchaseBean) {

					FortuneThreePurchaseBean bean = (FortuneThreePurchaseBean) gameBean;
					UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,
							bottomMsgsStr, 10);

					if (topMsgsStr.length() != 0) {
						advtMsg = "topAdvMsg:" + topMsgsStr + "|";
					}
					if (bottomMsgsStr.length() != 0) {
						advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
					}
					if (bean.getSaleStatus() == "PERFORMED") {
						responseObject.put("errorMsg", "Draw Performed.");
						responseObject.put("isSuccess", false);
						return;
					}
					gameName = Util.getGameName(bean.getGame_no());
					String reprintData = ReprintHepler.reprintFortuneThreeTicket(bean, gameName);

					String promoReprintData = buildPromoReprintData(bean.getPromoPurchaseBean(),userInfoBean,countryDeployed);
					finalReprintData = reprintData + "|balance:" + balance + "|QP:"
					+ bean.getIsQuickPick()[0] + "|" + promoReprintData
					+ advtMsg;
				} else if (gameBean instanceof String && "RG_RPERINT".equals(gameBean.toString())) {
					responseObject.put("errorMsg", "Reprint Limit Reached.");
					responseObject.put("isSuccess", false);
					return;
				} else if (gameBean instanceof String) {
					responseObject.put("errorMsg", gameBean.toString());
					responseObject.put("isSuccess", false);
					return;
				} else {
					responseObject.put("errorMsg", "Last Transaction Not Sale.");
					responseObject.put("isSuccess", false);
					return;
				}
				logger.debug("FINAL REPRINT DATA:" + finalReprintData);
				//responseObject.put("isSuccess", true);
			}
		} catch (IOException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "IOException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (LMSException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "LMSException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "SQLException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "Exception Occured.");
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorMsg", "Compile Time Error.");
				responseObject.put("isSuccess", false);
			}
			logger.info("ZimLottoBonus Sale Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
	}

	@SuppressWarnings("unchecked")
	public String buildPromoReprintData(Object PromoPurchaseBean,UserInfoBean userInfoBean,String countryDeployed) throws Exception {

		if (PromoPurchaseBean instanceof FortunePurchaseBean) {
			FortunePurchaseBean fortunePurchaseBean = (FortunePurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(fortunePurchaseBean
					.getGame_no());
			return "PromoTkt:"
			+ ReprintHepler.reprintFortuneTicket(fortunePurchaseBean,
					promoGameName) + "QP:"
					+ fortunePurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof LottoPurchaseBean) {
			LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(lottoPurchaseBean
					.getGame_no());
			return "PromoTkt:"
			+ ReprintHepler.reprintLottoTicket(lottoPurchaseBean,
					promoGameName) + "QP:"
					+ lottoPurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof KenoPurchaseBean) {
			KenoPurchaseBean kenoPurchaseBean = (KenoPurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(kenoPurchaseBean
					.getGame_no());
			return "PromoTkt:"
			+ ReprintHepler.reprintKenoTicket(kenoPurchaseBean,
					promoGameName,userInfoBean.getTerminalBuildVersion(),countryDeployed) + "QP:"
					+ kenoPurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof FortuneTwoPurchaseBean) {
			FortuneTwoPurchaseBean fortuneTwoPurchaseBean = (FortuneTwoPurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(fortuneTwoPurchaseBean
					.getGame_no());
			return "PromoTkt:"
			+ ReprintHepler.reprintFortuneTwoTicket(
					fortuneTwoPurchaseBean, promoGameName) + "QP:"
					+ fortuneTwoPurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof FortuneThreePurchaseBean) {
			FortuneThreePurchaseBean fortuneThreePurchaseBean = (FortuneThreePurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(fortuneThreePurchaseBean
					.getGame_no());
			return "PromoTkt:"
			+ ReprintHepler.reprintFortuneThreeTicket(
					fortuneThreePurchaseBean, promoGameName) + "QP:"
					+ fortuneThreePurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof List) {
			List<RafflePurchaseBean> rafflePurchaseBeanList = (List<RafflePurchaseBean>) PromoPurchaseBean;
			return CommonMethods.buildRaffleData(rafflePurchaseBeanList);
		}
		return "";
	}
}