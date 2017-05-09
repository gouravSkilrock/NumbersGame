package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.PrintWriter;
import java.util.Arrays;
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
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.PickFourHelper;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class PickFourAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private int gameId = Util.getGameId("PickFour");
	private KenoPurchaseBean pickFourPurchaseBean;
	private long LSTktNo;
	private String errMsg;
	private String json;

	public PickFourAction() {
		super(PickFourAction.class.getName());
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public KenoPurchaseBean getPickFourPurchaseBean() {
		return pickFourPurchaseBean;
	}

	public void setPickFourPurchaseBean(KenoPurchaseBean pickThreePurchaseBean) {
		this.pickFourPurchaseBean = pickThreePurchaseBean;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}
	
	public String purchaseTicketProcess() {
		logger.info("className: {} --In Pick Four Purchase Ticket-- ");
		logger.debug("Inside purchaseTicketProcess");
		
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		try {
			UserInfoBean userBean = getUserBean();
			ServletContext sc = ServletActionContext.getServletContext();
			response.setContentType("application/json");
			out = response.getWriter();
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");

			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			JSONObject commonSaleDataReq = (JSONObject) requestData.get("commonSaleData");
			JSONArray betTypeDataReq = (JSONArray) requestData.get("betTypeData");
			String totalPurchaseAmt = (String) requestData.get("totalPurchaseAmt");
			
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
			if("L".equalsIgnoreCase(commonSaleDataReq.getString("drawFrequency").trim()))
			{
				for (int i = 0; i < drawSize; i++) {
					JSONObject drawData = drawDataArr.getJSONObject(i);
					drawIdArr[i] = String.valueOf(drawData.getInt("drawId"));
				}
			}

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			
			KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
			drawGamePurchaseBean.setGameId(gameId);
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			
			drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
			drawGamePurchaseBean.setIsQuickPick(isQuickPick);
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
			drawGamePurchaseBean.setUserId(userBean.getUserId());
			drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
			drawGamePurchaseBean.setPartyType(userBean.getUserType());
			drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
			drawGamePurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));
			
			if (isAdvancedPlay == 1 && drawIdArr == null) {
				setErrMsg(DGErrorMsg.buyErrMsg(""));
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}

			if (drawIdArr != null) {
				drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			}

			boolean isValid =new PickFourHelper().pickFourValidateData(drawGamePurchaseBean);

			if (!isValid) {
				drawGamePurchaseBean.setSaleStatus("INVALID_INPUT");
				setPickFourPurchaseBean(drawGamePurchaseBean);
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			

			pickFourPurchaseBean = new PickFourHelper().pickFourPurchaseTicket(userBean, drawGamePurchaseBean);
			//setKenoTwoPurchaseBean(helper.commonPurchseProcessKenoTwo(userBean, drawGamePurchaseBean));
			pickFourPurchaseBean = getPickFourPurchaseBean();
			String saleStatus = getPickFourPurchaseBean().getSaleStatus();
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
					//betTypeDataRes.put("unitPrice", drawGamePurchaseBean.getUnitPrice()[i]);
					betTypeDataRes.put("unitPrice", drawGamePurchaseBean.getUnitPrice()[i]);
					betTypeDataRes.put("noOfLines", drawGamePurchaseBean.getNoOfLines()[i]);
					betTypeDataRes.put("betAmtMul", panelData.getInt("betAmtMul"));
					double panelPrice = panelData.getInt("betAmtMul") * drawGamePurchaseBean.getUnitPrice()[i] * drawGamePurchaseBean.getNoOfLines()[i] * drawGamePurchaseBean.getNoOfDraws();
					betTypeDataRes.put("panelPrice", panelPrice);
					betTypeArray.add(betTypeDataRes);
				}

				int listSize = drawGamePurchaseBean.getDrawDateTime().size();
				JSONArray drawDataArray = new JSONArray();
				JSONObject drawData = null;
				for (int i=0; i<listSize; i++) {
					String drawDataString = (String) drawGamePurchaseBean.getDrawDateTime().get(i);
					drawData = new JSONObject();
					if("L".equalsIgnoreCase(commonSaleDataReq.getString("drawFrequency").trim())){ 
						drawData.put("drawId", drawIdArr[i]);
					}
					drawData.put("drawDate", drawDataString.split(" ")[0]);
					String drawName[] = drawDataString.split("\\*");
					if(drawName.length < 2) {
						//drawData.put("drawName", "");
						drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
					}
					else {
						if(!"null".equalsIgnoreCase(drawDataString.split("\\*")[1].split("&")[0]))
							drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
						drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
					}
					drawDataArray.add(drawData);
				}
				JSONObject commonSaleDataRes = new JSONObject();
				commonSaleDataRes.put("ticketNumber", drawGamePurchaseBean.getTicket_no());
				commonSaleDataRes.put("barcodeCount", drawGamePurchaseBean.getTicket_no() + drawGamePurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? drawGamePurchaseBean.getBarcodeCount():""));
				commonSaleDataRes.put("gameName", drawGamePurchaseBean.getGameDispName());
				commonSaleDataRes.put("purchaseDate", drawGamePurchaseBean.getPurchaseTime().split(" ")[0]);
				commonSaleDataRes.put("purchaseTime", drawGamePurchaseBean.getPurchaseTime().split(" ")[1]);
				commonSaleDataRes.put("purchaseAmt", drawGamePurchaseBean.getTotalPurchaseAmt());
				commonSaleDataRes.put("drawData", drawDataArray);

				JSONObject mainData = new JSONObject();
				mainData.put("commonSaleData", commonSaleDataRes);
				mainData.put("betTypeData", betTypeArray);
				mainData.put("advMessage", pickFourPurchaseBean.getAdvMsg());
				mainData.put("orgName", userBean.getOrgName());
				mainData.put("userName", userBean.getUserName());

				jsonResponse.put("isSuccess", true);
				jsonResponse.put("errorMsg", "");
				jsonResponse.put("mainData", mainData);
				jsonResponse.put("isPromo", false);
				
				CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, userBean.getUserName(), getPickFourPurchaseBean().getTicket_no() + getPickFourPurchaseBean().getReprintCount());
			}
		}catch(LMSException e){
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", e.getErrorCode());
			jsonResponse.put("errorMsg", e.getErrorMessage());
		}catch(Exception e){
			e.printStackTrace();
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		logger.info("className: {} Pick Four Sale Response Data : {}"+ jsonResponse);
		out.print(jsonResponse);
		out.flush();
		out.close();
		
		return SUCCESS;
	}
}
