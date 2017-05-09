package com.skilrock.lms.android.drawGames.playMgmt.action;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.android.drawGames.common.AndroidErrors;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.MpesaPaymentProcessHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneThreePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.ReprintHepler;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class DrawGameRPOS extends BaseAction {
	private static final long serialVersionUID = 1L;

	public DrawGameRPOS() {
		super(DrawGameRPOS.class);
	}

	HttpSession session = null;
	private String userName;
	private String mobileNo;
	private long LSTktNo;
	private String mPesa;
	
	public void reprintTicketInJsonFormat() throws Exception {
		PrintWriter out = response.getWriter();
		JsonObject jsonResponse = new JsonObject();
		com.skilrock.lms.embedded.drawGames.common.DrawGameRPOS rpos = new  com.skilrock.lms.embedded.drawGames.common.DrawGameRPOS();

		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + AndroidErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + AndroidErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());

		} else {
			/*
			 * logger.debug(" LOGGED_IN_USERS maps is " +
			 * currentUserSessionMap);
			 */

			// logger.debug(" user name is " + userName);
			HttpSession session = (HttpSession) currentUserSessionMap
			.get(userName);

			UserInfoBean userInfoBean = (UserInfoBean) session
			.getAttribute("USER_INFO");
			// logger.debug("Before--" + new Date());
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			long lastPrintedTicket=0;
			int gameId = 0;
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}

			String actionName=ActionContext.getContext().getName();
			helper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);

			Object gameBean = helper.reprintTicket(userInfoBean, false, null,
					true,null,"TERMINAL");
			String gameName = null;
			// logger.debug("BEFORE REPRINT CALL");

			//AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			//ajxHelper.getAvlblCreditAmt(userInfoBean);

			double bal = userInfoBean.getAvailableCreditLimit()
			- userInfoBean.getClaimableBal();
			NumberFormat nFormat = NumberFormat.getInstance();
			nFormat.setMinimumFractionDigits(2);
			String balance =  nFormat.format(bal).replace(",", "");

			/*String balance = bal + "00";
			balance = balance.substring(0, balance.indexOf(".") + 3);*/
			String finalReprintData = null;

			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
			String advtMsg = "";

			if(gameBean instanceof RafflePurchaseBean) {
				RafflePurchaseBean bean = (RafflePurchaseBean) gameBean;

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

				StringBuilder finalData = new StringBuilder("RaffleData:");
				finalData.append("TicNo:").append(bean.getRaffleTicket_no()).append(bean.getReprintCount());
				finalData.append("|brCd:").append(bean.getRaffleTicket_no()).append(bean.getReprintCount()).append(bean.getBarcodeCount());
				finalData.append("|Date:").append(time);
				finalData.append("|GameName:").append(bean.getGameDispName());
				finalData.append("|DrawDate:").append(drawDateTime);
				finalData.append("|DrawName:").append(drawName);
				finalData.append(advtMsg);
				finalReprintData = finalData.toString();
			} else if (gameBean instanceof FortunePurchaseBean) {
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
					finalReprintData = "ErrorMsg:"
						+ AndroidErrors.DRAW_PERFORMED+"ErrorCode:" + AndroidErrors.DRAW_PERFORMED_ERROR_CODE+"|";
					// logger.debug("FINAL REPRINT DATA:" +
					// finalReprintData);
					response.getOutputStream().write(
							finalReprintData.getBytes());
					return;
				}
				gameName = Util.getGameName(bean.getGameId());
				// logger.debug("BEFORE REPRINT CALL--------" + gameName);
				String reprintData = ReprintHepler.reprintFortuneTicket(bean,
						gameName);
				
				String promoData = rpos.buildPromoReprintData(bean
						.getPromoPurchaseBean(),userInfoBean,countryDeployed);
				/*
				 * //here get the data for raffel games List<RafflePurchaseBean>
				 * rafflePurchaseBeanList= bean.getRafflePurchaseBeanList();
				 * String raffleData =
				 * CommonMethods.buildRaffleData(rafflePurchaseBeanList);
				 */

				finalReprintData = reprintData + "|balance:" + balance + "|QP:"
				+ bean.getIsQuickPick()[0] +"|ExpiryTime:"+bean.getClaimEndTime().replace(".0", "")+ "|" + advtMsg + promoData;
			} else if (gameBean instanceof LottoPurchaseBean) {
				LottoPurchaseBean bean = (LottoPurchaseBean) gameBean;
				UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr,
						bottomMsgsStr, 10);

				if (topMsgsStr.length() != 0) {
					advtMsg = "topAdvMsg:" + topMsgsStr + "|";
				}

				if (bottomMsgsStr.length() != 0) {
					advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
				}
				if (bean.getSaleStatus() == "PERFORMED") {
					finalReprintData = "ErrorMsg:"
						+ AndroidErrors.DRAW_PERFORMED+"ErrorCode:" + AndroidErrors.DRAW_PERFORMED_ERROR_CODE+"|";
					
					logger.debug("FINAL REPRINT DATA:" + finalReprintData);
					response.getOutputStream().write(
							finalReprintData.getBytes());
					return;
				}
				gameName = Util.getGameName(bean.getGame_no());
				// logger.debug("BEFORE REPRINT CALL--------" + gameName);
				String reprintData = ReprintHepler.reprintLottoTicket(bean,
						gameName);

				// String promoData
				// =buildPromoReprintData(bean.getPromoPurchaseBean());
				/*
				 * //here get the data for raffel games List<RafflePurchaseBean>
				 * rafflePurchaseBeanList= bean.getRafflePurchaseBeanList();
				 * String raffleData =
				 * CommonMethods.buildRaffleData(rafflePurchaseBeanList);
				 */
				/*
				 * response.getOutputStream().write( (reprintData + "|balance:"
				 * + bal + "|QP:" + bean.getIsQuickPick()[0] +
				 * "|"+promoData).getBytes());
				 */

				List<RafflePurchaseBean> rafflePurchaseBeanList = bean
				.getRafflePurchaseBeanList();
				String raffleData = CommonMethods
				.buildRaffleData(rafflePurchaseBeanList);
				if (raffleData.length() != 0) {
					raffleData = raffleData + "|";
				}
				String promoData="";
				if(bean.getPromoPurchaseBeanList()!=null){
					List<LottoPurchaseBean> promoPurchaseBeanList=bean.getPromoPurchaseBeanList();
					 promoData=ReprintHepler.buildSaleDataforZimlottothree(promoPurchaseBeanList,userInfoBean);
				
				}
				finalReprintData = reprintData + "|balance:" + balance + "|"
				+ raffleData +advtMsg+ promoData;
			} else if (gameBean instanceof KenoPurchaseBean) {
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);

				balance = nf.format(bal).replaceAll(",", "");
				KenoPurchaseBean kenoPurchaseBean = (KenoPurchaseBean) gameBean;
				
				int listSize = kenoPurchaseBean.getDrawDateTime().size();
				
				JsonObject tktDataJsonObject = new JsonObject();
				tktDataJsonObject.addProperty("gameCode", Util.getGameMasterLMSBean(kenoPurchaseBean.getGameId()).getGameNameDev());
				tktDataJsonObject.addProperty("ticketNumber", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount());
				tktDataJsonObject.addProperty("barCd", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired) ? kenoPurchaseBean.getBarcodeCount() : ""));
				
				tktDataJsonObject.addProperty("TicketCost", nf.format(kenoPurchaseBean.getTotalPurchaseAmt()));
				tktDataJsonObject.addProperty("gameName", Util.getGameDisplayName(kenoPurchaseBean.getGameId()));
				tktDataJsonObject.addProperty("purchaseTime", kenoPurchaseBean.getPurchaseTime());
				
				JsonArray panelDataArray = new JsonArray();
				JsonArray drawDataArray = new JsonArray();
				tktDataJsonObject.add("panelData", panelDataArray);
				tktDataJsonObject.add("drawData", drawDataArray);
				
				JsonObject drawDataObject = null;
				for (int iLoop = 0; iLoop < listSize; iLoop++) {
					drawDataObject = new JsonObject();
					if (kenoPurchaseBean.getDrawDateTime().get(iLoop).contains("*")) {
						drawDataObject.addProperty("drawId", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[1]);
						drawDataObject.addProperty("drawDate", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("\\*")[0].split(" ")[0]);
						drawDataObject.addProperty("drawTime", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("\\*")[0].split(" ")[1]);
						drawDataObject.addProperty("drawName", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("\\*")[1].split("&")[0]);
					} else {
						drawDataObject.addProperty("drawId", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[1]);
						drawDataObject.addProperty("drawDate", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[0].split(" ")[0]);
						drawDataObject.addProperty("drawTime", kenoPurchaseBean.getDrawDateTime().get(iLoop).split("&")[0].split(" ")[1]);
					}
					drawDataArray.add(drawDataObject);
					drawDataObject = null;
				}

				
				String[] playTypeStr = kenoPurchaseBean.getPlayType();
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

				jsonResponse.addProperty("responseCode", 0);
				jsonResponse.addProperty("saleTransId", kenoPurchaseBean.getRefTransId());
				jsonResponse.add("ticketData", tktDataJsonObject);
				jsonResponse.addProperty("availableBal", balance);

//				responseObj.addProperty("", kenoPurchaseBean.get)
		
		
				UtilApplet.getAdvMsgs(kenoPurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);
				JsonObject messageObject = null;
				JsonArray topArray = new JsonArray();
				for(String message : topMsgsStr.toString().split("~")) {
					messageObject = new JsonObject();
					messageObject.addProperty("msg", message);
					topArray.add(messageObject);
				}
				JsonArray bottomArray = new JsonArray();
				for(String message : bottomMsgsStr.toString().split("~")) {
					messageObject = new JsonObject();
					messageObject.addProperty("msg", message);
					bottomArray.add(messageObject);
				}

				jsonResponse.add("topAdvMsg", topArray);
				jsonResponse.add("bottomAdvMsg", bottomArray);
				//jsonResponse.addProperty("topAdvMsg", new Gson().toJson(topMsgsStr.toString().split("~")));
				//jsonResponse.addProperty("bottomAdvMsg", new Gson().toJson(bottomMsgsStr.toString().split("~")));
				
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
					finalReprintData = "ErrorMsg:"
						+ AndroidErrors.DRAW_PERFORMED+"ErrorCode:" + AndroidErrors.DRAW_PERFORMED_ERROR_CODE+"|";
					
					logger.debug("FINAL REPRINT DATA:" + finalReprintData);
					response.getOutputStream().write(
							finalReprintData.getBytes());
					return;
				}
				gameName = Util.getGameName(bean.getGame_no());
				String reprintData = ReprintHepler.reprintFortuneTwoTicket(
						bean, gameName);

				String promoReprintData = rpos.buildPromoReprintData(bean
						.getPromoPurchaseBean(),userInfoBean,countryDeployed);
				finalReprintData = reprintData + "|balance:" + balance + "|QP:"
				+ bean.getIsQuickPick()[0] + "|" + promoReprintData
				+ advtMsg;
			}else if(gameBean instanceof FortuneThreePurchaseBean){

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
					finalReprintData = "ErrorMsg:"
						+ AndroidErrors.DRAW_PERFORMED+"ErrorCode:" + AndroidErrors.DRAW_PERFORMED_ERROR_CODE+"|";
					
					logger.debug("FINAL REPRINT DATA:" + finalReprintData);
					response.getOutputStream().write(
							finalReprintData.getBytes());
					return;
				}
				gameName = Util.getGameName(bean.getGame_no());
				String reprintData = ReprintHepler.reprintFortuneThreeTicket(
						bean, gameName);

				String promoReprintData = rpos.buildPromoReprintData(bean
						.getPromoPurchaseBean(),userInfoBean,countryDeployed);
				finalReprintData = reprintData + "|balance:" + balance + "|QP:"
				+ bean.getIsQuickPick()[0] + "|" + promoReprintData
				+ advtMsg;
			
			}
			else if (gameBean instanceof String
					&& "RG_RPERINT".equals(gameBean.toString())) {
				//finalReprintData = "ErrorMsg:" + AndroidErrors.REPRINT_FRAUD+"ErrorCode:" + AndroidErrors.REPRINT_FRAUD_ERROR_CODE+"|";
				jsonResponse.addProperty("isSuccess", false);
				jsonResponse.addProperty("errorCode", AndroidErrors.REPRINT_FRAUD_ERROR_CODE);
				jsonResponse.addProperty("errorMsg", AndroidErrors.REPRINT_FRAUD);
			} else if (gameBean instanceof String) {
				//finalReprintData =gameBean.toString();
				//finalReprintData = "ErrorMsg:" + AndroidErrors.REPRINT_FAIL+"ErrorCode:" + AndroidErrors.REPRINT_FAIL_ERROR_CODE+"|";
				jsonResponse.addProperty("isSuccess", false);
				jsonResponse.addProperty("errorCode", AndroidErrors.REPRINT_FAIL_ERROR_CODE);
				jsonResponse.addProperty("errorMsg", AndroidErrors.REPRINT_FAIL);
			} else {
				//finalReprintData = "ErrorMsg:" + AndroidErrors.REPRINT_FAIL+"ErrorCode:" + AndroidErrors.REPRINT_FAIL_ERROR_CODE+"|";
				jsonResponse.addProperty("isSuccess", false);
				jsonResponse.addProperty("errorCode", AndroidErrors.REPRINT_FAIL_ERROR_CODE);
				jsonResponse.addProperty("errorMsg", AndroidErrors.REPRINT_FAIL);
			}
			logger.info("FINAL REPRINT DATA:" + jsonResponse);
			out.print(jsonResponse);
			out.flush();
			out.close();
		}

	}
	
	
	public void payPwtTicketJson() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		double govtCommAmt=0.0;
	//	response.setCharacterEncoding("UTF-8");
	//	response.setContentType("text/html");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + AndroidErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + AndroidErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

		MainPWTDrawBean mainWinningBean = (MainPWTDrawBean) session
		.getAttribute("PWT_RES");

		AjaxRequestHelper ajxHelper1 = new AjaxRequestHelper();
		ajxHelper1.getAvlblCreditAmt(userInfoBean);
		double bal1 = userInfoBean.getAvailableCreditLimit()
		- userInfoBean.getClaimableBal();
		String highPrizeCriteria = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
		String highPrizeScheme = (String) LMSUtility.sc.getAttribute(
						"DRAW_GAME_HIGH_PRIZE_SCHEME");
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		String promoTicketDta = "";
		logger.debug("Before--" + new Date());
		//code for Payment Through mPesa ...
		if("Y".equalsIgnoreCase(mPesa) && mobileNo!=null){
			mainWinningBean.setIsmPesaEnable(true);
			mainWinningBean.setMobileNumber(mobileNo);
			UserInfoBean mPesaUserBean = new MpesaPaymentProcessHelper().payBymPesaAcc(mainWinningBean, userInfoBean);
			mainWinningBean = helper.payPwtTicket(mainWinningBean, mPesaUserBean,highPrizeCriteria,highPrizeAmt,highPrizeScheme);
		}else{
			mainWinningBean = helper.payPwtTicket(mainWinningBean, userInfoBean,highPrizeCriteria,highPrizeAmt,highPrizeScheme);
		}		
		/*
		 *  logger.debug("---------------------------- AFTER HELPER
		 * CLASS----------------------------------" + new Date());
		 */

		String finalPwtData = null;
		if ("PWT_LIMIT_EXCEED".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + AndroidErrors.PWT_LIMIT_EXCEED+"ErrorCode:" + AndroidErrors.PWT_LIMIT_EXCEED_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		}if ("INVALID_PWT_LIMIT_EXCEED".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + AndroidErrors.INVALID_PWT_LIMIT_EXCEED+"ErrorCode:" + AndroidErrors.INVALID_PWT_LIMIT_EXCEED_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		}

		/*if(mainWinningBean.getWinningBeanList() != null) {
			for (int i = 0; i < mainWinningBean.getWinningBeanList().size(); i++) {
				PWTDrawBean winningBean = new PWTDrawBean();
				winningBean = mainWinningBean.getWinningBeanList().get(i);
				for(DrawIdBean drawIdBean : winningBean.getDrawWinList()) {
					if(drawIdBean.getRankId() == 4 && "TwelveByTwentyFour".equals(Util.getGameName(mainWinningBean.getGameId()))) {
						double tktCost = Double.parseDouble(drawIdBean.getWinningAmt())/2;
						for(int iLoop = 0; iLoop < 2; iLoop++) {
							double unitPrice = Util.getUnitPrice(mainWinningBean.getGameId(), "Direct12");
							int gameId = Util.getGameId("TwelveByTwentyFour");
							
							KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
							drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
							drawGamePurchaseBean.setGameId(gameId);
							drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
							drawGamePurchaseBean.setBetAmountMultiple(new int[]{(int) (tktCost/unitPrice)});
							drawGamePurchaseBean.setIsQuickPick(new int[]{1});
							drawGamePurchaseBean.setPlayerData(new String[]{"QP"});
							drawGamePurchaseBean.setPlayType(new String[]{"Direct12"});
							drawGamePurchaseBean.setNoPicked(new String[]{"12"});
							drawGamePurchaseBean.setNoOfPanel(1);
							drawGamePurchaseBean.setPartyId(userInfoBean.getUserOrgId());
							drawGamePurchaseBean.setPartyType(userInfoBean.getUserType());
							drawGamePurchaseBean.setUserId(userInfoBean.getUserId());
							drawGamePurchaseBean.setNoOfDraws(1);
							drawGamePurchaseBean.setIsAdvancedPlay(0);
							drawGamePurchaseBean.setRefMerchantId((String) sc.getAttribute("REF_MERCHANT_ID"));
							drawGamePurchaseBean.setPurchaseChannel("LMS_Terminal");
							drawGamePurchaseBean.setPlrMobileNumber("");
	//						drawGamePurchaseBean.setActionName(kenoPurchaseBean.getActionName());
	//						drawGamePurchaseBean.setLastGameId(kenoPurchaseBean.getLastGameId());
							drawGamePurchaseBean.setDeviceType("TERMINAL");
							drawGamePurchaseBean.setBarcodeType((String) LMSUtility.sc.getAttribute("BARCODE_TYPE"));
							drawGamePurchaseBean.setUserMappingId(userInfoBean.getCurrentUserMappingId());
							drawGamePurchaseBean.setDrawIdTableMap((Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap"));
							drawGamePurchaseBean.setLastSoldTicketNo("0");
							drawGamePurchaseBean.setServiceId(Util.getServiceIdFormCode("DG"));
							drawGamePurchaseBean.setPromotkt(true);
							
							KenoPurchaseBean purchaseBean = new TwelveByTwentyFourHelper().twelveByTwentyFourPurchaseTicket(userInfoBean, drawGamePurchaseBean);
							if ("SUCCESS".equalsIgnoreCase(purchaseBean.getSaleStatus())) {
								promoTicketDta += new DrawGameRPOSHelper().buildTwelveByTwentyFourData(purchaseBean, userInfoBean);
	//							orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, kenoPurchaseBean.getGame_no(), kenoPurchaseBean.getTicket_no(), purchaseBean.getTicket_no());
							} else {
	//							kenoPurchaseBean.setPromoSaleStatus("FAILED");
	//							return kenoPurchaseBean;
							}
	//						kenoPurchaseBean.setPromoPurchaseBean(purchaseBean);
						}
					}
				}
			}
		}*/
		
		String raffleData = AndroidErrors.RAFFLE_DATA;
		StringBuilder stBuild = new StringBuilder("");
		double totalRaffleAmount = 0.0;

		if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {

			List<RaffleDrawIdBean> raffleDrawIdBeanList = mainWinningBean
			.getWinningBeanList().get(0).getRaffleDrawIdBeanList();
			if (raffleDrawIdBeanList != null) {
				for (int j = 0; j < raffleDrawIdBeanList.size(); j++) {
					if (j > 0) {
						raffleData = raffleData + ";";
					}
					RaffleDrawIdBean raffleWinningBean = raffleDrawIdBeanList
					.get(j);
					String dataArr[] = buildPWTRaffleData(raffleWinningBean)
					.split("RWA");
					String data = dataArr[0];
					if (dataArr.length > 1)
						totalRaffleAmount = Double.parseDouble(dataArr[1]);

					raffleData = raffleData + data;
				}
			}
			raffleData = raffleData + "#" + "|Total Pay:" + totalRaffleAmount
			+ "|";

		} else if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
			// String raffleData=AndroidErrors.RAFFLE_DATA;
			// double totalRaffleAmount=0.0;
			List<PWTDrawBean> pwtWinBeanlist = mainWinningBean
			.getWinningBeanList();
			for (int k = 0; k < pwtWinBeanlist.size(); k++) {
				PWTDrawBean pwtWinningBean = pwtWinBeanlist.get(k);
				if (pwtWinningBean.getPwtTicketType()
						.equalsIgnoreCase("RAFFLE")) {
					List<RaffleDrawIdBean> raffleDrawIdBeanList = pwtWinningBean
					.getRaffleDrawIdBeanList();
					for (int i = 0; i < raffleDrawIdBeanList.size(); i++) {
						if (i > 0) {
							raffleData = raffleData + ";";
						}
						RaffleDrawIdBean raffleWinningBean = raffleDrawIdBeanList
						.get(i);

						if ("FRAUD".equalsIgnoreCase(raffleWinningBean
								.getStatus())) {

							raffleData = raffleData + "ErrorMsg:"
							+ AndroidErrors.PWT_FRAUD;

						} else if ("TICKET_EXPIRED"
								.equalsIgnoreCase(raffleWinningBean.getStatus())) {
							raffleData = raffleData + "ErrorMsg:"
							+ AndroidErrors.PWT_TICKET_EXPIRED+"ErrorCode:" + AndroidErrors.PWT_TICKET_EXPIRED_ERROR_CODE+"|";

						} else if (raffleWinningBean.getStatus().equals(
						"RES_AWAITED")) {
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ "|Awaited";

						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus()
								.equalsIgnoreCase("OUT_VERIFY_LIMIT")) {
							raffleData = raffleData + "ErrorMsg:"
							+ AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"NON_WIN")) {
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ "|TRY AGAIN";

						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"NORMAL_PAY")) {
							totalRaffleAmount = totalRaffleAmount
							+ Double.parseDouble(raffleWinningBean
									.getWinningAmt());
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ "|WIN "
							+ raffleWinningBean.getWinningAmt();

						} else if (raffleWinningBean.getStatus() != null
								&& raffleWinningBean.getStatus().equals(
								"CLAIMED")) {
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ "|CLAIMED";

						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"PND_PAY")) {
							raffleData = raffleData + "ErrorMsg:"
							+ AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"HIGH_PRIZE")) {
							raffleData = raffleData + "ErrorMsg:"
							+ AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"OUT_PAY_LIMIT")) {
							raffleData = raffleData + "ErrorMsg:"
							+ AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"OUT_VERIFY_LIMIT")) {
							raffleData = raffleData + "ErrorMsg:"
							+ AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
						}

					}

				}

			}
			raffleData = raffleData + "#";
			// StringBuilder stBuild = new StringBuilder("");
			double totalClmPndAmt=0.0;
			for (int k = 0; k < pwtWinBeanlist.size(); k++) {
				PWTDrawBean pwtWinningBean = pwtWinBeanlist.get(k);
				govtCommAmt=pwtWinningBean.getGovtTaxAmount();
				if (pwtWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
					double totTktAmt = 0.0;
					int totRegister = 0;
					if (!pwtWinningBean.isValid()) {
						stBuild.append("\nMsg:Invalid Ticket");

					} else if (pwtWinningBean.getStatus().equals("SUCCESS")) {
						for (int i = 0; i < pwtWinningBean.getDrawWinList()
						.size(); i++) {
							int nonWin = 0;
							int win = 0;
							int clm=0;
							int register = 0;
							int claim = 0;
							int outVerify = 0;
							int pndPay = 0;
							boolean isExpired= false;
							boolean isVerPending=false;
							boolean isClmPending=false;
							DrawIdBean drawBean = pwtWinningBean
							.getDrawWinList().get(i);
							String drawStatusForTicket=drawBean.getStatus();
							String drawTime = drawBean.getDrawDateTime();
							String gameName = Util.getGameName(pwtWinningBean
									.getGameNo());

							String[] drawTimeArr = drawTime.split("\\*");
							if (drawTimeArr.length > 1) {
								if (drawTimeArr[1] == null
										|| "NULL"
										.equalsIgnoreCase(drawTimeArr[1])) {
									drawTime = drawTimeArr[0];
								}
							}

							boolean isResAwaited = false;
							double drawAmt = 0.0;
							List<PanelIdBean> panelWinList = drawBean
							.getPanelWinList();
							if (panelWinList != null && !drawStatusForTicket.equals("DRAW_EXPIRED")) {

								for (int j = 0; j < panelWinList.size(); j++) {
									PanelIdBean panelBean = panelWinList.get(j);

									if (panelBean.getStatus().equals("NON WIN")) {
										nonWin++;
									} else if (panelBean.getStatus().equals(
									"NORMAL_PAY")) {
										drawAmt = panelBean.getWinningAmt()
										+ drawAmt;
										win++;
									} else if (panelBean.getStatus().equals(
									"CLAIMED")) {
										claim++;
									} else if (panelBean.getStatus().equals(
									"PND_PAY")) {
										pndPay++;
									} else if (panelBean.getStatus().equals(
									"HIGH_PRIZE")) {
										register++;
									} else if (panelBean.getStatus().equals(
									"OUT_PAY_LIMIT")) {
										register++;
									} else if (panelBean.getStatus().equals(
									"OUT_VERIFY_LIMIT")) {
										outVerify++;
									}else if(panelBean.getStatus().equals("CLAIM_PENDING")){
										drawAmt = panelBean.getWinningAmt()+ drawAmt;
										totalClmPndAmt+=panelBean.getWinningAmt();
										clm++;
									}
								}
							}else if(drawStatusForTicket.equals("DRAW_EXPIRED")){
								isExpired=true;
							}else if(drawStatusForTicket.equals("VERIFICATION_PENDING")){
								isVerPending=true;
							}else if(drawStatusForTicket.equals("CLAIM_PENDING")){
								isClmPending=true;
							}else{
								isResAwaited = true;
							}
							totTktAmt = (drawStatusForTicket.equals("CLAIM_PENDING")?0.0:drawAmt-govtCommAmt) + totTktAmt;
							if(isExpired){
								stBuild.append("|DrawTime:" + drawTime + "");
								stBuild.append(",No:0,Message:Draw Expired");
							}else if (isVerPending) {
								stBuild.append("|DrawTime:" + drawTime + "");
								stBuild.append(",No:0,Message:VER PEND");
							}else if (isResAwaited) {
								stBuild.append("|DrawTime:" + drawTime + "");
								stBuild.append(",No:0,Message:Awaited");
							} else {
								stBuild.append("|DrawTime:" + drawTime + "");
								/* As per clients' requirement, it's commented to omit the non-win data 
								 *  to reduce the size of ticket and to show only relevant data.
								 *  
								 * if (nonWin != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + nonWin
											+ ",Message:TRY AGAIN");
								}*/
								if (win != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + win + ",Message:WIN "
											+ Util.getBalInString(drawAmt) +"");
								}
								if (register != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + register
											+ ",Message:REG. REQ.");
								}
								if (claim != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + claim
											+ ",Message:CLAIMED");
								}
								if (pndPay != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + pndPay
											+ ",Message:IN PROCESS");
								}
								if (outVerify != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + outVerify
											+ ",Message:OUT OF VERIFY");
								}
								if(clm!=0){
									stBuild.append(",No:" + clm
											+ ",Message:CLM PND "+ Util.getBalInString(drawAmt-govtCommAmt));
								}
							}
							totRegister = totRegister + register;
						}
					} else if (pwtWinningBean.getStatus().equals("ERROR")) {
						finalPwtData = "ErrorMsg:" + AndroidErrors.PWT_ERROR+"ErrorCode:" + AndroidErrors.PWT_ERROR_CODE+"|";
						logger.debug("FINAL PWT DATA:" + finalPwtData);
						response.getOutputStream().write(
								finalPwtData.getBytes());
						return;
					}

					totTktAmt = totTktAmt + totalRaffleAmount;
 
					stBuild.append("|Total Pay:" + Util.getBalInString(totTktAmt) + "|");
					if(totalClmPndAmt>0)
						stBuild.append("Total CLM PEND:" + Util.getBalInString(totalClmPndAmt-govtCommAmt) + "|");
					else
						stBuild.append("Total CLM PEND:" + Util. getBalInString(totalClmPndAmt) + "|");
					if (totRegister != 0) {
						stBuild.append("No:" + totRegister
								+ ",Message:Reg. Req");
					}
					// session.setAttribute("PWT_RES", pwtWinningBean);
					currentUserSessionMap.put(userName, session);
					/*
					 * logger.debug(" STRING BUILER is before reprint " +
					 * stBuild);
					 * logger.debug("--------------------------GAME NUMBER "
					 * + pwtWinningBean.getGameNo());
					 */

				}
			}

		}

		AjaxRequestHelper ajxHelper2 = new AjaxRequestHelper();
		ajxHelper2.getAvlblCreditAmt(userInfoBean);
		double bal2 = userInfoBean.getAvailableCreditLimit()
		- userInfoBean.getClaimableBal();
		double bal = bal2 - bal1;
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");

		boolean isPromoPrint = false;
		if (mainWinningBean.isReprint()) {
			isPromoPrint = true;
			Object Purchasebean = mainWinningBean.getPurchaseBean();
			if (Purchasebean instanceof FortunePurchaseBean) {
				int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gamenbr);

				FortunePurchaseBean bean = (FortunePurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintFortuneTicket(bean, gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());

				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			} else if (Purchasebean instanceof LottoPurchaseBean) {
				int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gamenbr);

				LottoPurchaseBean bean = (LottoPurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintLottoTicket(bean, gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());

				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			} else if (Purchasebean instanceof KenoPurchaseBean) {
				int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gamenbr);

				KenoPurchaseBean bean = (KenoPurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintKenoTicketJsonFormat(bean, gameName, bal2));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());

				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			} else if (Purchasebean instanceof FortuneTwoPurchaseBean) {
				int gameNbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gameNbr);
				FortuneTwoPurchaseBean bean = (FortuneTwoPurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild
				.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintFortuneTwoTicket(bean,
								gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());
				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			}else if (Purchasebean instanceof FortuneThreePurchaseBean) {
				int gameNbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gameNbr);
				FortuneThreePurchaseBean bean = (FortuneThreePurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild
				.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintFortuneThreeTicket(bean,
								gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());
				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			}
		} else {
			finalPwtData = raffleData + stBuild.toString() + "Amt:" + balance
			+ "|";
		}
		//Mobile No:1111213132|Ref No:123135458|     to be added ...
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(mainWinningBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}

		finalPwtData = finalPwtData + advtMsg + ((isPromoPrint)?"":promoTicketDta);
		if(govtCommAmt>0)
			finalPwtData=finalPwtData+"Tax:"+Util.getBalInString(govtCommAmt)+"|";
		logger.debug("FINAL PWT DATA:" + finalPwtData);
		response.getOutputStream().write(finalPwtData.getBytes());
		return;
	}
	public HttpServletResponse getResponse() {
		return response;
	}


	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}


	public HttpSession getSession() {
		return session;
	}


	public void setSession(HttpSession session) {
		this.session = session;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public long getLSTktNo() {
		return LSTktNo;
	}


	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}


	public String getmPesa() {
		return mPesa;
	}


	public void setmPesa(String mPesa) {
		this.mPesa = mPesa;
	}


	public String buildPWTRaffleData(RaffleDrawIdBean raffleWinningBean) {

		// RaffleDrawIdBean
		// raffleWinningBean=(RaffleDrawIdBean)pwtWinningBean.getRaffleDrawIdBeanList().get(0);
		double totalRaffleAmount = 0.0;
		if ("FRAUD".equalsIgnoreCase(raffleWinningBean.getStatus())) {
			return "ErrorMsg:" + AndroidErrors.PWT_FRAUD+"ErrorCode:" + AndroidErrors.PWT_FRAUD_ERROR_CODE+"|";

		} else if ("TICKET_EXPIRED".equalsIgnoreCase(raffleWinningBean
				.getStatus())) {
			return "ErrorMsg:" + AndroidErrors.PWT_TICKET_EXPIRED+"ErrorCode:" + AndroidErrors.PWT_TICKET_EXPIRED_ERROR_CODE+"|";

		} else if (raffleWinningBean.getStatus().equals("RES_AWAITED")) {
			return raffleWinningBean.getDrawDateTime() + "|Awaited";

		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equalsIgnoreCase(
				"OUT_VERIFY_LIMIT")) {
			return "ErrorMsg:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

		} else if (raffleWinningBean.getStatus() != null
				&& raffleWinningBean.getStatus().equals("NON_WIN")) {
			return raffleWinningBean.getDrawDateTime() + "|TRY AGAIN";

		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("NORMAL_PAY")) {
			totalRaffleAmount = totalRaffleAmount
			+ Double.parseDouble(raffleWinningBean.getWinningAmt());
			return raffleWinningBean.getDrawDateTime() + "|WIN "
			+ raffleWinningBean.getWinningAmt() + "" + "RWA"
			+ totalRaffleAmount;

		} else if (raffleWinningBean.getStatus() != null
				&& raffleWinningBean.getStatus().equals("CLAIMED")) {
			return raffleWinningBean.getDrawDateTime() + "|CLAIMED";

		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("PND_PAY")) {
			return "ErrorMsg:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("HIGH_PRIZE")) {
			return "ErrorMsg:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("OUT_PAY_LIMIT")) {
			return "ErrorMsg:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("OUT_VERIFY_LIMIT")) {
			return "ErrorMsg:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + AndroidErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";

		}

		return "";
	}	
	
}
