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
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.OneToTwelveHelper;
import com.skilrock.lms.dge.beans.OneToTwelvePurchaseBean;
import com.skilrock.lms.dge.gameconstants.OneToTwelveConstants;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class OneToTwelveAction extends BaseAction{

	public OneToTwelveAction() {
		super(OneToTwelveAction.class.getName());
	}

	Log logger = LogFactory.getLog(OneToTwelveAction.class);
	
	public static final List numbers = Arrays.asList("", "Zero(0)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
			"Eight(8)", "Nine(9)","Ten(10)","Eleven(11)","Twelve(12)");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errMsg;
	private OneToTwelvePurchaseBean oneToTwelveBean;
	private int gameId = Util.getGameId("OneToTwelve");
	private long LSTktNo;
	private String json;
	
	public void purchaseTicketProcess() {
		String pickedData[] = null;
		String[] playType = null;
		List<String> drawDateTime = new ArrayList<String>();
		StringBuilder cost = null;
		StringBuilder ticket = null;
			
		logger.info("className: {} --In One to Twelve Purchase Purchase Ticket-- ");
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
			int panelSize = betTypeDataReq.size();
			int[] isQuickPick = new int[panelSize];
			pickedData = new String[panelSize];
			String noPicked[] = new String[panelSize];
			playType = new String[panelSize];
			int[] betAmountMultiple = new int[panelSize];
			boolean [] QPPreGenerated = new boolean[panelSize];
			for (int i = 0; i < panelSize; i++) {
				JSONObject panelData = betTypeDataReq.getJSONObject(i);
				isQuickPick[i] = panelData.getBoolean("isQp") == true ? 1 : 2;
				pickedData[i] = panelData.getString("pickedNumbers");
				noPicked[i] = panelData.getString("noPicked");
				playType[i] = panelData.getString("betName");
				betAmountMultiple[i] = panelData.getInt("betAmtMul");
				QPPreGenerated[i] = panelData.getBoolean("QPPreGenerated");
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
			
			OneToTwelvePurchaseBean drawGamePurchaseBean = new OneToTwelvePurchaseBean();
			drawGamePurchaseBean.setGameId(gameId);
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			
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
			drawGamePurchaseBean.setQPPreGenerated(QPPreGenerated) ;
			
			TransactionManager.setResponseData("true");
			
			if (panelSize < 1) {
				drawGamePurchaseBean.setSaleStatus("ERROR");
				setOneToTwelveBean(drawGamePurchaseBean);
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
					isValid = Util.validateNumber(OneToTwelveConstants.START_RANGE, OneToTwelveConstants.END_RANGE, playerData, false);
					if (!isValid)
						break;
				}
			}
	
			if (!isValid) {
				drawGamePurchaseBean.setSaleStatus("INVALID_INPUT");
				setOneToTwelveBean(drawGamePurchaseBean);
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			if ((String)requestData.get("tokenId") != null) {
				if (!TpEBetMgmtDaoImpl.getInstance().isBetSlipActive((String)requestData.get("tokenId"))) {
					throw new LMSException(LMSErrors.BET_SLIP_EXPIRED_ERROR_CODE, LMSErrors.BET_SLIP_EXPIRED_ERROR_MESSAGE);
				}
			}
			
	
			oneToTwelveBean = new OneToTwelveHelper().oneToTwelvePurchaseTicket(userBean, drawGamePurchaseBean);
			oneToTwelveBean = getOneToTwelveBean();
			String saleStatus = getOneToTwelveBean().getSaleStatus();
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
					betTypeDataRes.put("unitPrice", drawGamePurchaseBean.getUnitPrice());
					betTypeDataRes.put("noOfLines", drawGamePurchaseBean.getNoOfLines()[i]);
					betTypeDataRes.put("betAmtMul", panelData.getInt("betAmtMul"));
					double panelPrice = panelData.getInt("betAmtMul") * drawGamePurchaseBean.getUnitPrice() * drawGamePurchaseBean.getNoOfLines()[i] * drawGamePurchaseBean.getNoOfDraws();
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
				ticket = new StringBuilder(drawGamePurchaseBean.getTicket_no());
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
				mainData.put("advMessage", oneToTwelveBean.getAdvMsg());
				mainData.put("orgName", userBean.getOrgName());
				mainData.put("userName", userBean.getUserName());
				new  AjaxRequestHelper().getAvlblCreditAmt(userBean);
				mainData.put("AvlblCreditAmt",FormatNumber.formatNumber(userBean.getAvailableCreditLimit() - userBean.getClaimableBal()));
				mainData.put("parentOrgName", userBean.getParentOrgName());

				jsonResponse.put("isSuccess", true);
				jsonResponse.put("errorMsg", "");
				jsonResponse.put("mainData", mainData);
				jsonResponse.put("isPromo", false);
				if (requestData.get("tokenId") != null && !((String)requestData.get("tokenId")).trim().isEmpty()) {
			    	    Util.setEbetSaleRequestStatusDone((String)requestData.get("tokenId"), userBean.getUserOrgId());
				}
				CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, userBean.getUserName(), getOneToTwelveBean().getTicket_no() + getOneToTwelveBean().getReprintCount());
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
		
		logger.info("className: {} One To Twelve Sale Response Data : {}"+ jsonResponse);
		if("SUCCESS".equalsIgnoreCase(oneToTwelveBean.getSaleStatus())){
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

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
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
	
	public OneToTwelvePurchaseBean getOneToTwelveBean() {
		return oneToTwelveBean;
	}

	public void setOneToTwelveBean(OneToTwelvePurchaseBean oneToTwelveBean) {
		this.oneToTwelveBean = oneToTwelveBean;
	}

}
