package com.skilrock.lms.android.drawGames.playMgmt.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.RainBowNumberHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class RainBowNumberAction extends ActionSupport implements ServletRequestAware , ServletResponseAware {

	static Log logger = LogFactory.getLog(RainBowNumberAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final List numbers = Arrays.asList("", "Zero(0)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
			"Eight(8)", "Nine(9)");

	public static List<String> rec(int start, int elementToChoose,
			int returnCnt, String[] indexArr, String[] elements,
			StringBuffer stbuff, List<String> comboList, String qp) {

		if (returnCnt == elementToChoose) {
			return comboList;
		}
		returnCnt++;
		for (int i = start; i < elements.length; i++) {

			indexArr[returnCnt - 1] = "" + i;
			if (returnCnt == elementToChoose) {

				stbuff = new StringBuffer();
				for (String element : indexArr) {
					stbuff.append("," + elements[Integer.parseInt(element)]);
				}
				stbuff.delete(0, 1);
				comboList.add(stbuff.toString());
				if ("No".equalsIgnoreCase(qp)) {
					comboList.add("Nxt");
				} else if ("Yes".equalsIgnoreCase(qp)) {
					comboList.add("QP");
				}
			}

			rec(++start, elementToChoose, returnCnt, indexArr, elements,
					stbuff, comboList, qp);
		}
		return comboList;
	}
	
	private String requestData;

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	private int gameId = Util.getGameId("RainbowGame");
	private KenoPurchaseBean kenoPurchaseBean;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private String userName;

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoPurchaseBean;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getUserName() {
		return userName;
	}

	public void purchaseTicketProcess() {
		ServletContext sc = ServletActionContext.getServletContext();
		JsonObject requestObj = (JsonObject) new JsonParser().parse(requestData);
		JsonObject responseObj = new JsonObject();;
		
		try {
			String isDraw = (String) sc.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				responseObj.addProperty("responseCode", -1);
				responseObj.addProperty("responseMsg", EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE);

				response.getOutputStream().write(new Gson().toJson(responseObj).getBytes());
				return;
			}
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
	
			HttpSession session = (HttpSession) currentUserSessionMap.get(requestObj.get("userName").getAsString());
	
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
	
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			//int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
			
			String purchaseChannel = "LMS_Terminal";
			JsonArray panelDataJsonArr = requestObj.get("panelData").getAsJsonArray();
			int noOfDraws = requestObj.get("noOfDraws").getAsInt();

			int[] betAmtArr = new int[panelDataJsonArr.size()];
			int[] QPArr = new int[panelDataJsonArr.size()];
			String[] pickedNumbersArr = new String[panelDataJsonArr.size()];
			String[] noPickedArr = new String[panelDataJsonArr.size()];
			String[] playTypeArr = new String[panelDataJsonArr.size()];
			for (int iLoop = 0; iLoop < panelDataJsonArr.size(); iLoop++) {
				betAmtArr[iLoop] = panelDataJsonArr.get(iLoop).getAsJsonObject().get("betAmt").getAsInt();
				QPArr[iLoop] = panelDataJsonArr.get(iLoop).getAsJsonObject().get("QP").getAsInt();
				pickedNumbersArr[iLoop] = panelDataJsonArr.get(iLoop).getAsJsonObject().get("pickedNumbers").getAsString();
				noPickedArr[iLoop] = panelDataJsonArr.get(iLoop).getAsJsonObject().get("noPicked").getAsString();
				playTypeArr[iLoop] = panelDataJsonArr.get(iLoop).getAsJsonObject().get("playType").getAsString();
			}
			
			/*JsonArray drawDataJsonArr = requestObj.get("drawData").getAsJsonArray();
			String[] drawIdArr = new String[drawDataJsonArr.size()];
			for (int iLoop = 0; iLoop < drawDataJsonArr.size(); iLoop++) {
				drawIdArr[iLoop] = drawDataJsonArr.get(iLoop).getAsJsonObject().get("drawId").getAsString();
			}*/

			KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			drawGamePurchaseBean.setGameId(gameId);
			drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			drawGamePurchaseBean.setBetAmountMultiple(betAmtArr);
			drawGamePurchaseBean.setIsQuickPick(QPArr);
			drawGamePurchaseBean.setPlayerData(pickedNumbersArr);
			drawGamePurchaseBean.setNoPicked(noPickedArr);
			drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
			drawGamePurchaseBean.setPartyType(userBean.getUserType());
			drawGamePurchaseBean.setUserId(userBean.getUserId());
			drawGamePurchaseBean.setNoOfDraws(noOfDraws);
			drawGamePurchaseBean.setIsAdvancedPlay(requestObj.get("isAdvancePlay").getAsInt());
			long lastPrintedTicket = 0;
			int lstGameId = 0;

//			int serviceId = Util.getServiceIdFormCode(request.getAttribute("code").toString());
			int serviceId = Util.getServiceIdFormCode("DG");
			if(serviceId==0 || userBean.getCurrentUserMappingId() == 0){
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE ,  LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
			long lstTktNo = requestObj.get("LSTktNo").getAsLong();
			if(lstTktNo !=0){
				lastPrintedTicket = lstTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(lstTktNo).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lstTktNo)));
			}
			/*if (drawIdArr != null) {
				drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			}*/
			drawGamePurchaseBean.setLastSoldTicketNo(lastPrintedTicket+"");
			drawGamePurchaseBean.setRefMerchantId(refMerchantId);
			drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
			drawGamePurchaseBean.setBonus("N");
			drawGamePurchaseBean.setPlayType(playTypeArr);
			drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
			drawGamePurchaseBean.setNoOfPanel(panelDataJsonArr.size());
//			drawGamePurchaseBean.setPlrMobileNumber(requestObj.get("plrMobileNumber").getAsString());
			drawGamePurchaseBean.setPlrMobileNumber("9811881291");
			String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
			drawGamePurchaseBean.setBarcodeType(barcodeType);
			drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
			drawGamePurchaseBean.setServiceId(serviceId);
			
			String actionName=ActionContext.getContext().getName();
			
			drawGamePurchaseBean.setActionName(actionName);
			drawGamePurchaseBean.setLastGameId(lstGameId);
			drawGamePurchaseBean.setDeviceType("TERMINAL");
	
			RainBowNumberHelper helper = new RainBowNumberHelper();
			// new DrawGameRPOSHelper().checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
			/*logger.debug("SALE_AUTO_CANCEL_LOGS:" + "lastSoldTktLMS : " + lastSoldTktLMS + " :lastSoldTicketNo " + lastSoldTicketNo);
	        if(lastSoldTktLMS != Long.parseLong(lastSoldTicketNo) && lastSoldTktLMS != 0 && !"0".equals(LSTktNo) && LSTktNo!=null){
				logger.debug("SALE_AUTO_CANCEL_LOGS:" + "Inside Auto Cancellation if");
				CancelTicketBean cancelTicketBean = new CancelTicketBean();
				cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
				cancelTicketBean.setTicketNo(lastSoldTktLMS + "00");
				cancelTicketBean.setPartyId(userBean.getUserOrgId());
				cancelTicketBean.setPartyType(userBean.getUserType());
				cancelTicketBean.setUserId(userBean.getUserId());
				cancelTicketBean.setCancelChannel("LMS_Terminal");
				cancelTicketBean.setRefMerchantId(refMerchantId);
				cancelTicketBean.setAutoCancel(true);
				cancelTicketBean.setHoldAutoCancel(true);
				cancelTicketBean.setAutoCancelHoldDays(autoCancelHoldDays);
				cancelTicketBean = helper.cancelTicket(cancelTicketBean,
						userBean, true,"CANCEL_MISMATCH");
				logger.debug("SALE_AUTO_CANCEL_LOGS:" + "is cancelled " + cancelTicketBean.isValid() + " :Ticket_number" + lastSoldTktLMS);
			}
			logger.debug("SALE_AUTO_CANCEL_LOGS:" + "SALE Continue for the new ticket");*/
	
			String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");
	
			kenoPurchaseBean = helper.commonPurchseProcess(userBean, drawGamePurchaseBean);
			setKenoPurchaseBean(kenoPurchaseBean);
	
			String saleStatus = getKenoPurchaseBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				responseObj.addProperty("responseCode", 0);
				responseObj.addProperty("responseMsg", DGErrorMsg.buyErrMsg(saleStatus));
				System.out.println("FINAL PURCHASE DATA KENO:" + responseObj);
				response.getOutputStream().write(new Gson().toJson(responseObj).getBytes());
				return;
			}
	
			/*logger.debug(kenoPurchaseBean.getPlayerPicked() + "msg---------" + kenoPurchaseBean.getTicket_no());*/
	
			double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);

			String balance = nf.format(bal).replaceAll(",", "");

			//String balance = bal + "00";
			//balance = balance.substring(0, balance.indexOf(".") + 3);
			int listSize = kenoPurchaseBean.getDrawDateTime().size();
			
			JsonObject tktDataJsonObject = new JsonObject();
			tktDataJsonObject.addProperty("gameCode", Util.getGameMasterLMSBean(gameId).getGameNameDev());
			tktDataJsonObject.addProperty("ticketNumber", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount());
			tktDataJsonObject.addProperty("barCd", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired) ? kenoPurchaseBean.getBarcodeCount() : ""));
			
			tktDataJsonObject.addProperty("TicketCost", nf.format(kenoPurchaseBean.getTotalPurchaseAmt()));
			tktDataJsonObject.addProperty("gameName", Util.getGameDisplayName(gameId));
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

//			responseObj.addProperty("", kenoPurchaseBean.get)
	
			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
	
			UtilApplet.getAdvMsgs(kenoPurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			Gson gson = builder.create();

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

			responseObj.add("topAdvMsg", topArray);
			responseObj.add("bottomAdvMsg", bottomArray);

			// Not Considered Yet, Check If Free Game is Available
			/*String raffleDrawDay=(String) sc.getAttribute("RAFFLE_GAME_DRAW_DAY");
			String raffleGameDataString=(String) sc.getAttribute("RAFFLE_GAME_DATA");
			raffleGameDataString=raffleGameDataString.substring(raffleGameDataString.indexOf(":")+1,raffleGameDataString.length());
			double rAmount=0;
			boolean isRaffleGame=false;
			String raffleGameData=null;
	        if(!"".equals(raffleGameDataString)){
	            String[] raffGameArray=raffleGameDataString.split("#");
	            for(int i=0;i < raffGameArray.length;i++){
	                raffleGameData=raffGameArray[i];
	                if("KenoFive".equalsIgnoreCase(raffleGameData.substring(0,raffleGameData.indexOf("%")))){
	        			rAmount=Double.parseDouble(raffleGameData.substring(raffleGameData.indexOf("%")+1, raffleGameData.lastIndexOf("%")));
	        			isRaffleGame=true;
	        		}
	            }
	        }
			String raffleDrawDate="";
			if (!"NA".equals(raffleDrawDay) && kenoPurchaseBean.getTotalPurchaseAmt() >= rAmount && isRaffleGame) {
				Map<String, Integer> dayMap = new HashMap<String, Integer>();
				dayMap.put("SUNDAY", 1);
				dayMap.put("MONDAY", 2);
				dayMap.put("TUESDAY", 3);
				dayMap.put("WEDNESDAY", 4);
				dayMap.put("THURSDAY", 5);
				dayMap.put("FRIDAY", 6);
				dayMap.put("SATURDAY", 7);

				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

				int today = calendar.get(Calendar.DAY_OF_WEEK);
				int nxtDay = dayMap.get(raffleDrawDay);

				if (today != nxtDay) {
					int days = (Calendar.SATURDAY - today + nxtDay) % 7;
					calendar.add(Calendar.DAY_OF_YEAR, days);
				}

				raffleDrawDate = "RDate:" + format.format(calendar.getTime()) + "|";
				System.out.println(raffleDrawDate);
			}
			
			
			
			String promoTicketDta = "";
			Object promoPurchaseBeaan = kenoPurchaseBean.getPromoPurchaseBean();
			if (promoPurchaseBeaan instanceof FortunePurchaseBean) {
				promoTicketDta = buildSaleDataforWinfast(
						(FortunePurchaseBean) promoPurchaseBeaan, userBean);
			}
			if(promoPurchaseBeaan instanceof KenoPurchaseBean) {
				promoTicketDta = new DrawGameRPOSHelper().buildTwelveByTwentyFourData((KenoPurchaseBean)promoPurchaseBeaan, userBean);
			}
			if (promoPurchaseBeaan instanceof List) {
				promoTicketDta = CommonMethods
						.buildRaffleData((List<RafflePurchaseBean>) promoPurchaseBeaan);
			}*/
			
			response.getOutputStream().write(gson.toJson(responseObj).getBytes());
		} catch (IOException e) {
			e.printStackTrace();

			System.out.println("FINAL PURCHASE DATA KENO FIVE:Error!Try Again");
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
	}
	
	public String buildSaleDataforWinfast(FortunePurchaseBean fortuneBean,
			UserInfoBean userBean) {

		String time = fortuneBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < fortuneBean.getPickedNumbers().size(); i++) {
			stBuilder.append(numbers.get(fortuneBean.getPickedNumbers().get(i))
					+ ":" + fortuneBean.getBetAmountMultiple()[i] + "|");
		}
		int listSize = fortuneBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + fortuneBean.getDrawDateTime()
					.get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:")
					.replace(".0", ""));
		}

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);

		List<RafflePurchaseBean> rafflePurchaseBeanList = fortuneBean
				.getRafflePurchaseBeanList();
		String raffleData = CommonMethods
				.buildRaffleData(rafflePurchaseBeanList);

		String finalData = "|PromoTkt:" + "TicketNo:"
				+ fortuneBean.getTicket_no() + fortuneBean.getReprintCount()
				+ "|Date:" + time + "|" + stBuilder.toString() + "TicketCost:"
				+ fortuneBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance + "|QP:" + fortuneBean.getIsQP() + "|"
				+ raffleData;

		return finalData;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

}
