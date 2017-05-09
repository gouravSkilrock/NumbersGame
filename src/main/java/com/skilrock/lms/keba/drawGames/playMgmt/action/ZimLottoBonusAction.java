package com.skilrock.lms.keba.drawGames.playMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

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
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class ZimLottoBonusAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public ZimLottoBonusAction() {
		super(ZimLottoBonusAction.class);
	}

	private int gameId = Util.getGameId("ZimLottoBonus");
	private LottoPurchaseBean lottoPurchaseBean;
	private String requestData;

	public LottoPurchaseBean getLottoPurchaseBean() {
		return lottoPurchaseBean;
	}

	public void setLottoPurchaseBean(LottoPurchaseBean lottoPurchaseBean) {
		this.lottoPurchaseBean = lottoPurchaseBean;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	@SuppressWarnings("unchecked")
	public void purchaseTicketProcess() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		try {
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
			JSONObject commonSaleDataReq = (JSONObject) requestData.get("commonSaleData");
			JSONArray betTypeDataReq = (JSONArray) requestData.get("betTypeData");
			String userName = (String) requestData.get("userName");

			logger.info("ZimLottoBonus Sale Request Data : " + requestData);
			logger.info("ZimLottoBonus Sale Request Data (commonSaleData) : " + commonSaleDataReq);
			logger.info("ZimLottoBonus Sale Request Data (betTypeData) : " + betTypeDataReq);

			response.setContentType("application/json");
			request.setAttribute("code", "MGMT");
			request.setAttribute("interfaceType", "TERMINAL");
			out = response.getWriter();

			String isDraw = (String) sc.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				responseObject.put("errorMsg", EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE);
				responseObject.put("isSuccess", false);
			}

			double totalPurchaseAmt = Double.parseDouble(commonSaleDataReq.getString("totalPurchaseAmt").trim());
			//int noOfPanel = Integer.parseInt(commonSaleDataReq.getString("noOfPanels").trim());
			int noOfDraws = Integer.parseInt(commonSaleDataReq.getString("noOfDraws").trim());
			String plrMobileNumber = (commonSaleDataReq.getString("plrMobileNumber")==null || commonSaleDataReq.getString("plrMobileNumber").length()==0) ? null : commonSaleDataReq.getString("plrMobileNumber").trim();
			int isAdvancedPlay = "false".equals(commonSaleDataReq.getString("isAdvancePlay").trim()) ? 0 : 1;
			long lastTktNo = Long.parseLong(commonSaleDataReq.getString("lastTktNo").trim());
			int panelSize = betTypeDataReq.size();
			int[] isQuickPick = new int[panelSize];
			String pickedData[] = new String[panelSize];
			String noPicked[] = new String[panelSize];
			String[] playType = new String[panelSize];
			int[] betAmountMultiple = new int[panelSize];
			for (int i = 0; i < panelSize; i++) {
				JSONObject panelData = betTypeDataReq.getJSONObject(i);
				isQuickPick[i] = panelData.getBoolean("isQp") == true ? 1 : 2;
				pickedData[i] = panelData.getString("pickedNumbers");
				noPicked[i] = panelData.getString("noPicked");
				playType[i] = panelData.getString("betName");
				betAmountMultiple[i] = panelData.getInt("betAmtMul");
			}
			JSONArray drawDataArr = commonSaleDataReq.getJSONArray("drawData");
			int drawSize = drawDataArr.size();
			String[] drawIdArr = new String[drawSize];
			for (int i = 0; i < drawSize; i++) {
				JSONObject drawData = drawDataArr.getJSONObject(i);
				drawIdArr[i] = String.valueOf(drawData.getInt("drawId"));
			}

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			List<String> playerPicked = new ArrayList<String>();
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays = Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
					.getAttribute("drawIdTableMap");

			String purchaseChannel = "LMS_Terminal";
			LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
			lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
			lottoPurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			lottoPurchaseBean.setGameId(gameId);
			lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			lottoPurchaseBean.setNoOfDraws(noOfDraws);
			lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
			lottoPurchaseBean.setPartyType(userBean.getUserType());
			lottoPurchaseBean.setUserId(userBean.getUserId());
			lottoPurchaseBean.setRefMerchantId(refMerchantId);
			lottoPurchaseBean.setPurchaseChannel(purchaseChannel);
			lottoPurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
			lottoPurchaseBean.setPlayType(playType[0]);
			lottoPurchaseBean.setNoPicked(Integer.parseInt(noPicked[0]));
			lottoPurchaseBean.setPickedNumbers(pickedData[0]);
			lottoPurchaseBean.setPlrMobileNumber(plrMobileNumber);
			String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
			lottoPurchaseBean.setBarcodeType(barcodeType);
			if (drawIdArr != null) {
				lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			}
			lottoPurchaseBean.setPlayerPicked(playerPicked);
			lottoPurchaseBean.setPicknumbers(pickedData);

			long lastPrintedTicket=0;
			int lstGameId =0;
			if(lastTktNo !=0){
				lastPrintedTicket = lastTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(lastTktNo).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTktNo)));
			}
			String actionName=ActionContext.getContext().getName();
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			helper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,lstGameId);

			if (pickedData.length < 1) {
				lottoPurchaseBean.setSaleStatus("ERROR");
				responseObject.put("errorMsg", EmbeddedErrors.PURCHSE_INVALID_DATA);
				responseObject.put("isSuccess", false);
				return;
			}

			for (int i = 0; i < pickedData.length; i++) {
				if (!"QP".equals(pickedData[i])) {
					if (!Util.validateNumber(
							ConfigurationVariables.ZIMLOTTOBONUS_START_RANGE,
							ConfigurationVariables.ZIMLOTTOBONUS_END_RANGE, pickedData[i],
							false)) {
						lottoPurchaseBean.setSaleStatus("ERROR");
						responseObject.put("errorMsg", EmbeddedErrors.PURCHSE_INVALID_DATA);
						return;
					}
				}
			}

			setLottoPurchaseBean(helper.zimLottoBonusPurchaseTicket(userBean, lottoPurchaseBean));

			String saleStatus = getLottoPurchaseBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				responseObject.put("errorMsg", DGErrorMsg.buyErrMsg(saleStatus));
				responseObject.put("isSuccess", false);
				return;
			}

			double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);

			/*
			int listSize = lottoPurchaseBean.getDrawDateTime().size();
			StringBuilder drawTimeBuild = new StringBuilder("");
			for (int i = 0; i < listSize; i++) {
				drawTimeBuild.append(("|DrawDate:" + lottoPurchaseBean
						.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace(
								"#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
			}
			StringBuilder stBuilder = new StringBuilder("");
			for (int i = 0; i < lottoPurchaseBean.getPlayerPicked().size(); i++) {
				stBuilder.append("|Num:"
						+ lottoPurchaseBean.getPlayerPicked().get(i) + "|QP:"
						+ lottoPurchaseBean.getIsQuickPick()[i]);
			}
	
			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
			String advtMsg = "";
	
			UtilApplet.getAdvMsgs(lottoPurchaseBean.getAdvMsg(), topMsgsStr,
					bottomMsgsStr, 10);
	
			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}
	
			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr;
			}
	
			List<RafflePurchaseBean> rafflePurchaseBeanList = lottoPurchaseBean
					.getRafflePurchaseBeanList();
			String raffleData = CommonMethods
					.buildRaffleData(rafflePurchaseBeanList);
	
			String promoData="";
			if(lottoPurchaseBean.getPromoPurchaseBeanList()!=null){
				List<LottoPurchaseBean> promoPurchaseBeanList=lottoPurchaseBean.getPromoPurchaseBeanList();
				 promoData=buildSaleDataforZimlottoBonusFree(promoPurchaseBeanList,userBean);
			
			}
			finalPurchaseData = "TicketNo:" + lottoPurchaseBean.getTicket_no()
					+ lottoPurchaseBean.getReprintCount()+"|brCd:"+lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? lottoPurchaseBean.getBarcodeCount():"")+"|Date:" + time
					+ "|PlayType:" + lottoPurchaseBean.getPlayType()
					+ stBuilder.toString() + "|TicketCost:"
					+ lottoPurchaseBean.getTotalPurchaseAmt()
					+ drawTimeBuild.toString() + "|balance:" + balance + "|"
					+ advtMsg +"|"+ raffleData +promoData;
			System.out.println("FINAL PURCHASE DATA ZIMLOTTOBONUS:" + finalPurchaseData);
			request.setAttribute("purchaseData", finalPurchaseData);
			//jsonObject.put("finalPurchaseData", finalPurchaseData);

			List<PanelResponseBean> panelList = new ArrayList<PanelResponseBean>();
			PanelResponseBean panelBean = null;
			for (int i = 0; i < panelSize; i++) {
				panelBean = new PanelResponseBean();
				JSONObject panelData = panelDataArr.getJSONObject(i);
				panelBean.setQp(panelData.getBoolean("isQp"));
				panelBean.setPickedNumbers(lottoPurchaseBean.getPlayerPicked().toString().replace("[", "").replace("]", ""));
				panelBean.setBetAmtMultiple(panelData.getInt("betAmtMultiple"));
				panelBean.setNoPicked(lottoPurchaseBean.getNoPicked());
				panelBean.setPlayType(lottoPurchaseBean.getPlayType());
				panelBean.setUnitPrice(lottoPurchaseBean.getUnitPrice());
				panelBean.setNoOfLines(lottoPurchaseBean.getNoOfLines());
				panelList.add(panelBean);
			}

			SaleResponseBean responseBean = new SaleResponseBean();
			responseBean.setSuccess(true);
			responseBean.setBalance(balance);
			responseBean.setTicketNo(lottoPurchaseBean.getTicket_no());
			responseBean.setGameName(lottoPurchaseBean.getGameDispName());
			responseBean.setPurchaseAmt(lottoPurchaseBean.getTotalPurchaseAmt());
			responseBean.setPurchaseTime(time);
			responseBean.setPanelData(panelList);
			jsonObject.put("responseData", responseBean);
			*/

			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
			String advtMsg = "";
	
			UtilApplet.getAdvMsgs(lottoPurchaseBean.getAdvMsg(), topMsgsStr,
					bottomMsgsStr, 10);
	
			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}
	
			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr;
			}

			JSONArray betTypeArray = new JSONArray();
			JSONObject betTypeDataRes = null;
			for (int i=0; i<panelSize; i++) {
				JSONObject panelData = betTypeDataReq.getJSONObject(i);
				betTypeDataRes = new JSONObject();
				betTypeDataRes.put("betName", lottoPurchaseBean.getPlayType());
				betTypeDataRes.put("isQp", panelData.getBoolean("isQp"));
				//betTypeDataRes.put("pickedNumbers", lottoPurchaseBean.getPlayerPicked().toString().replace("[", "").replace("]", ""));
				//betTypeDataRes.put("pickedNumbers", lottoPurchaseBean.getPlayerPicked().get(i));
				betTypeDataRes.put("pickedNumbers", formatPickedNumbers(lottoPurchaseBean.getPlayerPicked().get(i)));
				betTypeDataRes.put("betAmtMul", panelData.getInt("betAmtMul"));
				betTypeArray.add(betTypeDataRes);
			}

			int listSize = lottoPurchaseBean.getDrawDateTime().size();
			JSONArray drawDataArray = new JSONArray();
			JSONObject drawData = null;
			for (int i=0; i<listSize; i++) {
				String drawDataString = (String) lottoPurchaseBean.getDrawDateTime().get(i);
				//	2014-01-21 18:31:00*DRAWNAME&1701
				//	2014-01-22 19:45:00&1032
				drawData = new JSONObject();
				//drawData.put("drawId", Integer.parseInt(drawDataString.split("&")[1]));
				drawData.put("drawId", drawIdArr[i]);
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
			commonSaleDataRes.put("ticketNumber", lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
			commonSaleDataRes.put("brCd", lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? lottoPurchaseBean.getBarcodeCount():""));
			commonSaleDataRes.put("gameName", lottoPurchaseBean.getGameDispName());
			commonSaleDataRes.put("purchaseDate", lottoPurchaseBean.getPurchaseTime().split(" ")[0]);
			commonSaleDataRes.put("purchaseTime", lottoPurchaseBean.getPurchaseTime().split(" ")[1]);
			commonSaleDataRes.put("purchaseAmt", lottoPurchaseBean.getTotalPurchaseAmt());
			commonSaleDataRes.put("balance", bal);
			commonSaleDataRes.put("unitPrice", lottoPurchaseBean.getUnitPrice());
			commonSaleDataRes.put("drawData", drawDataArray);
			commonSaleDataRes.put("topAdvMsg",topMsgsStr );
			commonSaleDataRes.put("bottomAdvMsg",bottomMsgsStr );

			responseObject.put("isSuccess", true);
			responseObject.put("errorMsg", "");
			responseObject.put("commonSaleData", commonSaleDataRes);
			responseObject.put("betTypeData", betTypeArray);
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

	private static String formatPickedNumbers(String pickedNumbers) {
		String[] numString = pickedNumbers.split(",");
		StringBuilder returnString = new StringBuilder("");
		int number = 0;
		for(int i=0; i<numString.length; i++) {
			number = Integer.parseInt(numString[i]);
			returnString.append((number<10)?"0"+number:number).append(",");
		}

		return returnString.substring(0, returnString.length()-1);
	}

	/*
	public String buildSaleDataforZimlottoBonusFree(List<LottoPurchaseBean> promoPurchaseBeanList,
			UserInfoBean userBean) {
         StringBuilder finalPromoData=new StringBuilder();
		for(int j=0;j<promoPurchaseBeanList.size();j++){
			LottoPurchaseBean lottoBean=promoPurchaseBeanList.get(j);
		String time = lottoBean.getPurchaseTime()
		.replace(" ", "|Time:").replace(".0", "");

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		String balance = nf.format(bal).replaceAll(",", "");

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
//		}

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
				//+ ("Perm6".equals(lottoBean.getPlayType())?stBuilder.toString():"|PlayType:" + lottoBean.getPlayType()+ stBuilder.toString()) + "|TicketCost:"
				+ lottoBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance + "|"
				+ raffleData;
		finalPromoData.append(finalData);
		}
		return finalPromoData.toString();
	}
	*/
}