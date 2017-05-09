package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.OneToTwelveRouletteHelper;
import com.skilrock.lms.dge.beans.OneToTwelvePurchaseBean;
import com.skilrock.lms.dge.beans.OneToTwelveRoulettePurchaseBean;
import com.skilrock.lms.dge.gameconstants.OneToTwelveRouletteConstants;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class OneToTwelveRouletteAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OneToTwelveRouletteAction() {
		super(OneToTwelveRouletteAction.class);
	}
	
	
	Log logger = LogFactory.getLog(OneToTwelveRouletteAction.class);
	public static final List numbers = Arrays.asList("Zero(0)", "DoubleZero(00)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
			"Eight(8)", "Nine(9)","Ten(10)","Eleven(11)","Twelve(12)");
	/**
	 * 
	 */
	private String errMsg;
	private OneToTwelveRoulettePurchaseBean oneToTwelveRouletteBean;
	//private int gameId = Util.getGameId("MiniRoulette");
	private long LSTktNo;
	private String json;
	
	public void purchaseTicketProcess() {
		String pickedData[] = null;
		String[] playType = null;
		List<String> drawDateTime = new ArrayList<String>();
		StringBuilder cost = null;
		StringBuilder ticket = null;
		logger.info("className: {} --In One to Twelve Roulette Purchase Purchase Ticket-- ");
		logger.debug("Inside purchaseTicketProcess");
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		try {
			ServletContext sc = ServletActionContext.getServletContext();
			response.setContentType("application/json");
			out = response.getWriter();
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
	
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			JSONObject commonSaleDataReq = (JSONObject) requestData.get("commonSaleData");
			JSONArray betTypeDataReq = (JSONArray) requestData.get("betTypeData");
			String totalPurchaseAmt = (String) requestData.get("totalPurchaseAmt");
			
			String userName = (String) requestData.get("userName");
			UserInfoBean userBean = userName == null ? getUserBean() : getUserBean(userName);
			
			long lastPrintedTicket = 0;
			int lstGameId = 0;
			String actionName = ActionContext.getContext().getName();
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			try {
				LSTktNo = CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName());
				if (LSTktNo != 0) {
					lastPrintedTicket = LSTktNo / Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
					lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				}
	
				helper.insertEntryIntoPrintedTktTableForWeb(lstGameId, userBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
			} catch (Exception e) {
				 e.printStackTrace();
			}
	
			int noOfDraws = Integer.parseInt(commonSaleDataReq.getString("noOfDraws").trim());
			int isAdvancedPlay = "false".equals(commonSaleDataReq.getString("isAdvancePlay").trim()) ? 0 : 1;
			String gameName = commonSaleDataReq.getString("gameName");
			int gameId = Util.getGameId(gameName);
			int panelSize = betTypeDataReq.size();
			int[] isQuickPick = new int[panelSize];
			pickedData = new String[panelSize];
			String noPicked[] = new String[panelSize];
			playType = new String[panelSize];
			boolean [] qpPreGenerated = new boolean[panelSize];
			int[] betAmountMultiple = new int[panelSize];
			double[] unitPrice = new double[panelSize];
			int[] noOfLines = new int[panelSize];
			for (int i = 0; i < panelSize; i++) {
				JSONObject panelData = betTypeDataReq.getJSONObject(i);
				isQuickPick[i] = panelData.getBoolean("isQp") == true ? 1 : 2;
				pickedData[i] = panelData.getString("pickedNumbers");
				noPicked[i] = panelData.getString("noPicked");
				playType[i] = panelData.getString("betName");
				betAmountMultiple[i] = panelData.getInt("betAmtMul");
				unitPrice[i] = panelData.getDouble("unitPrice");
				noOfLines[i] = panelData.getInt("noOfLines");
			}
			JSONArray drawDataArr = commonSaleDataReq.getJSONArray("drawData");
			int drawSize = drawDataArr.size();
			String[] drawIdArr = new String[drawSize];
			if(!commonSaleDataReq.getBoolean("isDrawManual")){
				for (int i = 0; i < drawSize; i++) {
					JSONObject drawData = drawDataArr.getJSONObject(i);
					drawIdArr[i] = String.valueOf(drawData.getInt("drawId"));
				}
			}
	
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			
			OneToTwelveRoulettePurchaseBean drawGamePurchaseBean = new OneToTwelveRoulettePurchaseBean();
			drawGamePurchaseBean.setGameId(gameId);
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			drawGamePurchaseBean.setGameDevName(Util.getGameName(gameId));
			
			drawGamePurchaseBean.setUnitPrice(unitPrice);
			drawGamePurchaseBean.setNoOfLines(noOfLines);
			
			drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
			drawGamePurchaseBean.setIsQuickPick(isQuickPick);
			drawGamePurchaseBean.setIsQP(isQuickPick[0]);
			drawGamePurchaseBean.setPlayerData(pickedData);
			drawGamePurchaseBean.setNoPicked(noPicked);
			drawGamePurchaseBean.setPlayType(playType);
			drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			drawGamePurchaseBean.setNoOfDraws(noOfDraws);
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
			drawGamePurchaseBean.setRefMerchantId(refMerchantId);
			drawGamePurchaseBean.setPurchaseChannel("LMS_Web");
			drawGamePurchaseBean.setBonus("N");
			drawGamePurchaseBean.setServiceId(Util.getServiceIdFormCode(request.getAttribute("code").toString()));
			drawGamePurchaseBean.setNoOfPanel(panelSize);
			drawGamePurchaseBean.setTotalNoOfPanels(panelSize);
			drawGamePurchaseBean.setUserId(userBean.getUserId());
			drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
			drawGamePurchaseBean.setPartyType(userBean.getUserType());
			drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
			drawGamePurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));
			//drawGamePurchaseBean.setQPPreGenerated(qpPreGenerated) ;
			if (panelSize < 1) {
				drawGamePurchaseBean.setSaleStatus("ERROR");
				setOneToTwelveRouletteBean(drawGamePurchaseBean);
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			
			if (isAdvancedPlay == 1 && drawIdArr == null) {
				setErrMsg(DGErrorMsg.buyErrMsg(""));
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
	
			if (drawIdArr != null) {
				drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			}
	
			boolean isValid = true;
			for (int i = 0; i < panelSize; i++) {
				String playerData = pickedData[i];
				if (!"QP".equals(playerData)) {
					isValid = Util.validateNumber(OneToTwelveRouletteConstants.class.getField(gameName.toUpperCase()+"_START_RANGE").getInt(null), OneToTwelveRouletteConstants.class.getField(gameName.toUpperCase()+"_END_RANGE").getInt(null), playerData, false);
					if (!isValid)
						break;
				}
			}
	
			if (!isValid) {
				drawGamePurchaseBean.setSaleStatus("INVALID_INPUT");
				setOneToTwelveRouletteBean(drawGamePurchaseBean);
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			
			if ((String)requestData.get("tokenId") != null) {
				if (!TpEBetMgmtDaoImpl.getInstance().isBetSlipActive((String)requestData.get("tokenId"))) {
					throw new LMSException(LMSErrors.BET_SLIP_EXPIRED_ERROR_CODE, LMSErrors.BET_SLIP_EXPIRED_ERROR_MESSAGE);
				}
			}
			
	
			oneToTwelveRouletteBean = new OneToTwelveRouletteHelper().oneToTwelveRoulettePurchaseTicket(userBean, drawGamePurchaseBean);
			oneToTwelveRouletteBean = getOneToTwelveRouletteBean();
			String saleStatus = getOneToTwelveRouletteBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				if ("AGT_INS_BAL".equalsIgnoreCase(saleStatus))
					throw new LMSException(LMSErrors.INSUFFICIENT_AGENT_BALANCE_ERROR_CODE, LMSErrors.INSUFFICIENT_AGENT_BALANCE_ERROR_MESSAGE);
				else if ("RET_INS_BAL".equalsIgnoreCase(saleStatus))
					throw new LMSException(LMSErrors.INSUFFICIENT_RETAILER_BALANCE_ERROR_CODE, LMSErrors.INSUFFICIENT_RETAILER_BALANCE_ERROR_MESSAGE);
				else if ("NO_DRAWS".equalsIgnoreCase(saleStatus))
					throw new LMSException(LMSErrors.TRANSACTION_FAILED_ERROR_CODE, LMSErrors.TRANSACTION_FAILED_ERROR_MESSAGE);
				else if ("FRAUD".endsWith(saleStatus))
					throw new LMSException(LMSErrors.PURCHASE_FRAUD_ERROR_CODE, LMSErrors.PURCHASE_FRAUD_ERROR_MESSAGE);
				else if ("UNAUTHORISED".endsWith(saleStatus))
					throw new LMSException(LMSErrors.UNAUTHORIZED_SALE_ERROR_CODE, LMSErrors.UNAUTHORIZED_SALE_ERROR_MESSAGE);
				
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}else {
				JSONArray betTypeArray = new JSONArray();
				JSONObject betTypeDataRes = null;
				boolean isQP = false ;
				for (int i=0; i<panelSize; i++) {
					JSONObject panelData = betTypeDataReq.getJSONObject(i);
					betTypeDataRes = new JSONObject();
					isQP = drawGamePurchaseBean.getIsQuickPick()[i] == 1 ? true : false; 
					betTypeDataRes.put("isQp", isQP);
					betTypeDataRes.put("betName", drawGamePurchaseBean.getPlayType()[i]);
					betTypeDataRes.put("pickedNumbers", drawGamePurchaseBean.getPlayerData()[i]);
					betTypeDataRes.put("numberPicked", drawGamePurchaseBean.getNoPicked()[i]);
					betTypeDataRes.put("unitPrice", drawGamePurchaseBean.getUnitPrice()[i]);
					betTypeDataRes.put("noOfLines", oneToTwelveRouletteBean.getNoOfLines()[i]);
					betTypeDataRes.put("betAmtMul", panelData.getInt("betAmtMul"));
					double panelPrice = drawGamePurchaseBean.getUnitPrice()[i] * drawGamePurchaseBean.getNoOfDraws();
					betTypeDataRes.put("panelPrice", panelPrice);
					betTypeArray.add(betTypeDataRes);
				}
	
				int listSize = drawGamePurchaseBean.getDrawDateTime().size();
				JSONArray drawDataArray = new JSONArray();
				JSONObject drawData = null;
				for (int i=0; i<listSize; i++) {
					String drawDataString = (String) drawGamePurchaseBean.getDrawDateTime().get(i);
					drawData = new JSONObject();
					if(!commonSaleDataReq.getBoolean("isDrawManual")){
						drawData.put("drawId", drawIdArr[i]);
					}
					//drawData.put("drawId", drawIdArr[i]);
					drawData.put("drawDate", drawDataString.split(" ")[0]);
					String drawName[] = drawDataString.split("\\*");
					if(drawName.length < 2) {
						//drawData.put("drawName", "");
						drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
						drawDateTime.add(drawDataString.split(" ")[0] + " "+drawDataString.split("&")[0].split(" ")[1]);
					}
					else {
						if(!"null".equalsIgnoreCase(drawDataString.split("\\*")[1].split("&")[0]))
							drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
						drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
						drawDateTime.add(drawDataString.split(" ")[0] + " "+drawDataString.split("&")[0].split(" ")[1]);
					}
					drawDataArray.add(drawData);
				}
				JSONObject commonSaleDataRes = new JSONObject();
				commonSaleDataRes.put("ticketNumber", drawGamePurchaseBean.getTicket_no());
				ticket = new StringBuilder(drawGamePurchaseBean.getTicket_no()).append(drawGamePurchaseBean.getReprintCount());
				commonSaleDataRes.put("barcodeCount", drawGamePurchaseBean.getTicket_no() + drawGamePurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? drawGamePurchaseBean.getBarcodeCount():""));
				commonSaleDataRes.put("gameName", drawGamePurchaseBean.getGameDispName());
				commonSaleDataRes.put("purchaseDate", drawGamePurchaseBean.getPurchaseTime().split(" ")[0]);
				commonSaleDataRes.put("purchaseTime", drawGamePurchaseBean.getPurchaseTime().split(" ")[1]);
				commonSaleDataRes.put("purchaseAmt", drawGamePurchaseBean.getTotalPurchaseAmt());
				cost = new StringBuilder(String.valueOf(drawGamePurchaseBean.getTotalPurchaseAmt()));
				commonSaleDataRes.put("drawData", drawDataArray);
	
				JSONObject mainData = new JSONObject();
				mainData.put("commonSaleData", commonSaleDataRes);
				mainData.put("betTypeData", betTypeArray);
				mainData.put("advMessage", oneToTwelveRouletteBean.getAdvMsg());
				mainData.put("orgName", userBean.getOrgName());
				mainData.put("userName", userBean.getUserName());
				mainData.put("parentOrgName", userBean.getParentOrgName());

				jsonResponse.put("isSuccess", true);
				jsonResponse.put("errorMsg", "");
				jsonResponse.put("mainData", mainData);
				jsonResponse.put("isPromo", false);
				if (requestData.get("tokenId") != null && !((String)requestData.get("tokenId")).trim().isEmpty()) {
		    	    Util.setEbetSaleRequestStatusDone((String)requestData.get("tokenId"), userBean.getUserOrgId());
				}	
				CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, userBean.getUserName(), getOneToTwelveRouletteBean().getTicket_no() + getOneToTwelveRouletteBean().getReprintCount());
			}
		} catch(LMSException e){
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", e.getErrorCode());
			jsonResponse.put("errorMsg", e.getErrorMessage());
		}catch(Exception e){
			e.printStackTrace();
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
		logger.info("className: {} One To Twelve Roulette Sale Response Data : {}"+ jsonResponse);
		if("SUCCESS".equalsIgnoreCase(oneToTwelveRouletteBean.getSaleStatus())){
			String smsData = CommonMethods.prepareSMSData(pickedData, playType, cost, ticket, drawDateTime);
			CommonMethods.sendSMS(smsData);
		}
		out.print(jsonResponse);
		out.flush();
		out.close();
		
		
	}
	
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}


	public OneToTwelveRoulettePurchaseBean getOneToTwelveRouletteBean() {
		return oneToTwelveRouletteBean;
	}


	public void setOneToTwelveRouletteBean(
			OneToTwelveRoulettePurchaseBean oneToTwelveRouletteBean) {
		this.oneToTwelveRouletteBean = oneToTwelveRouletteBean;
	}
	
	

}
