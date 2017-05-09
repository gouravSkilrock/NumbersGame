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

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoTicketBean;
import com.skilrock.lms.dge.gameconstants.KenoTwoConstants;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class KenoTwoAction extends BaseAction {

	private DrawGameRPOSHelper rposHelper;
	private static final long serialVersionUID = 1L;

	private int gameId;
	private KenoPurchaseBean kenoTwoPurchaseBean;
	private long LSTktNo;
	private String json;
	private JSONObject testResponse;
	private List<String> allDrawDateTimeData; 
	private UserInfoBean userBean;
	
	

	public JSONObject getTestResponse() {
		return testResponse;
	}

	public void setTestResponse(JSONObject testResponse) {
		this.testResponse = testResponse;
	}

	public KenoTwoAction() {
		super(KenoTwoAction.class.getName());
		rposHelper = new DrawGameRPOSHelper();
	}

	public KenoTwoAction(DrawGameRPOSHelper rposHelper) {
		super(KenoTwoAction.class.getName());
		this.rposHelper = rposHelper;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public KenoPurchaseBean getKenoTwoPurchaseBean() {
		return kenoTwoPurchaseBean;
	}

	public void setKenoTwoPurchaseBean(KenoPurchaseBean kenoTwoPurchaseBean) {
		this.kenoTwoPurchaseBean = kenoTwoPurchaseBean;
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

	public void purchaseTicketProcess() {
		logger.info("className: {} --In Keno Two Sale Request Data-- "+ getJson());
		allDrawDateTimeData= new ArrayList<String>();
		StringBuilder cost = null;
		StringBuilder ticket = null;
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		try {
			setGameId( Util.getGameId("KenoTwo"));
			response.setContentType("application/json");
			out = response.getWriter();
			
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(getJson());
			JSONObject commonSaleDataReq = (JSONObject) requestData.get("commonSaleData");
			JSONArray betTypeDataReq = (JSONArray) requestData.get("betTypeData");

			String userName = (String) requestData.get("userName");
			userBean = (userName==null) ? getUserBean() : getUserBean(userName);

			checkForTicketCokieAndInsertEntryForPrintedTkt();
	
			int panelSize = betTypeDataReq.size();
			List<KenoTicketBean> kenoTickets = new ArrayList<KenoTicketBean>();
			for (int i = 0; i < panelSize; i++) {
				kenoTickets.add(setBetTypeData(betTypeDataReq, i));
			}

			KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
			
			prepareKenoPurchaseBean(requestData, kenoTickets, drawGamePurchaseBean);
			
			boolean isValid = true;
			for (KenoTicketBean kenoTicketBean : kenoTickets) {
				isValid = validatePickedNoAsPerBetType(kenoTicketBean);
				if (!isValid)
					break;
			}
			TransactionManager.setResponseData("true");
			ifPickedNoInvalidAsPerBetType(drawGamePurchaseBean, isValid);
			//for Ebet
			if ((String) requestData.get("tokenId") != null) {
				if (!TpEBetMgmtDaoImpl.getInstance().isBetSlipActive((String) requestData.get("tokenId"))) {
					throw new LMSException(LMSErrors.BET_SLIP_EXPIRED_ERROR_CODE, LMSErrors.BET_SLIP_EXPIRED_ERROR_MESSAGE);
				}
			}
			kenoTwoPurchaseBean = rposHelper.commonPurchseProcessKenoTwo(userBean, drawGamePurchaseBean);
			setKenoTwoPurchaseBean(kenoTwoPurchaseBean);
			String saleStatus = getKenoTwoPurchaseBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				checkIfSaleStatusIsNotSuccess(saleStatus);
			} else {
				JSONObject mainData = prepareMainData(commonSaleDataReq, betTypeDataReq);
				prepareFinalSaleResponseData(jsonResponse, mainData);
				//For Ebet
				if (requestData.get("tokenId") != null && !((String) requestData.get("tokenId")).trim().isEmpty()) {
					Util.setEbetSaleRequestStatusDone((String) requestData.get("tokenId"), userBean.getUserOrgId());
				}
				//Cookie Work
				CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, userBean.getUserName(),
					getKenoTwoPurchaseBean().getTicket_no() + getKenoTwoPurchaseBean().getReprintCount());
				//Send SMS After Sale
				ticket = new StringBuilder(kenoTwoPurchaseBean.getTicket_no()).append(kenoTwoPurchaseBean.getReprintCount());
				cost = new StringBuilder(String.valueOf(kenoTwoPurchaseBean.getTotalPurchaseAmt()));
				String smsData = CommonMethods.prepareSMSData(kenoTwoPurchaseBean.getPlayerData(),kenoTwoPurchaseBean.getPlayType(), cost, ticket, allDrawDateTimeData);
				CommonMethods.sendSMS(smsData);
			}
		} catch (LMSException e) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", e.getErrorCode());
			jsonResponse.put("errorMsg", e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		logger.info("className: {} Keno Two Sale Response Data : {}" + jsonResponse);
		setTestResponse(jsonResponse);
		out.print(jsonResponse);
		out.flush();
		out.close();
	}

	private void checkForTicketCokieAndInsertEntryForPrintedTkt() {
		try {
			long lastPrintedTicket = 0;
			int lstGameId = 0;
			ActionContext context = ActionContext.getContext();
			String actionName = context.getName();
			setLSTktNo( CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName()));
			if (getLSTktNo() != 0) {
				lastPrintedTicket = getLSTktNo() / Util.getDivValueForLastSoldTkt(String.valueOf(getLSTktNo()).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(getLSTktNo())));
			}
			rposHelper.insertEntryIntoPrintedTktTableForWeb(lstGameId, userBean.getUserOrgId(), lastPrintedTicket,
					"WEB", Util.getCurrentTimeStamp(), actionName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JSONObject prepareMainData(JSONObject commonSaleDataReq, JSONArray betTypeDataReq) throws LMSException {
		int panelSize=betTypeDataReq.size();
		JSONArray betTypeArray = new JSONArray();
		boolean isQP = false;
		JSONObject betTypeDataRes = null;
		String[] drawIdArr = setdrawIdArrIfInputisManual(commonSaleDataReq);
		int[] isQuickPick = kenoTwoPurchaseBean.getIsQuickPick();
		for (int i = 0; i < panelSize; i++) {
			JSONObject panelData = betTypeDataReq.getJSONObject(i);
			isQP = isQuickPick[i] == 1 ? true : false;
			betTypeDataRes = prepareBetTypeDataResponse(isQP, i, panelData);
			betTypeArray.add(betTypeDataRes);
		}
		
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		for (int i = 0; i < kenoTwoPurchaseBean.getDrawDateTime().size(); i++) {
			drawData = prepareDrawDataArray(commonSaleDataReq, drawIdArr, i);
			drawDataArray.add(drawData);
		}
		
		JSONObject commonSaleDataRes = prepareDrawDataResponse(drawDataArray);

		JSONObject mainData = prepareMainSaleDataResponse(userBean, betTypeArray, commonSaleDataRes);
		return mainData;
	}

	private void prepareFinalSaleResponseData(JSONObject jsonResponse, JSONObject mainData) {
		jsonResponse.put("isSuccess", true);
		jsonResponse.put("errorMsg", "");
		jsonResponse.put("mainData", mainData);
		jsonResponse.put("isPromo", false);
	}

	private JSONObject prepareDrawDataArray(JSONObject commonSaleDataReq, String[] drawIdArr, int i) throws LMSException {
		JSONObject drawData = new JSONObject();
		try {
			String drawDataString = (String) kenoTwoPurchaseBean.getDrawDateTime().get(i);
			if (!commonSaleDataReq.getBoolean("isDrawManual")) {
				drawData.put("drawId", drawIdArr[i]);
			}
			drawData.put("drawDate", drawDataString.split(" ")[0]);
			String drawName[] = drawDataString.split("\\*");
			if (drawName.length < 2) {
				drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
				allDrawDateTimeData.add(drawDataString.split(" ")[0] + " " + drawDataString.split("&")[0].split(" ")[1]);
			} else {
				if (!"null".equalsIgnoreCase(drawDataString.split("\\*")[1].split("&")[0]))
					drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
				drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
				allDrawDateTimeData.add(drawDataString.split(" ")[0] + " " + drawDataString.split("&")[0].split(" ")[1]);
			}
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return drawData;
	}

	private JSONObject prepareBetTypeDataResponse(boolean isQP, int i, JSONObject panelData) throws LMSException {
		JSONObject betTypeDataRes = new JSONObject();
		try {
			betTypeDataRes.put("isQp", isQP);
			betTypeDataRes.put("betName", kenoTwoPurchaseBean.getPlayType()[i]);
			betTypeDataRes.put("pickedNumbers", kenoTwoPurchaseBean.getPlayerData()[i]);
			betTypeDataRes.put("numberPicked", kenoTwoPurchaseBean.getNoPicked()[i]);
			betTypeDataRes.put("unitPrice", kenoTwoPurchaseBean.getUnitPrice()[i]);
			betTypeDataRes.put("noOfLines", kenoTwoPurchaseBean.getNoOfLines()[i]);
			betTypeDataRes.put("betAmtMul", panelData.getInt("betAmtMul"));
			double panelPrice = panelData.getInt("betAmtMul") * kenoTwoPurchaseBean.getUnitPrice()[i]
					* kenoTwoPurchaseBean.getNoOfLines()[i] * kenoTwoPurchaseBean.getNoOfDraws();
			betTypeDataRes.put("panelPrice", panelPrice);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return betTypeDataRes;
	}

	private JSONObject prepareMainSaleDataResponse(UserInfoBean userBean, JSONArray betTypeArray,
			JSONObject commonSaleDataRes) {
		JSONObject mainData = new JSONObject();
		mainData.put("commonSaleData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", kenoTwoPurchaseBean.getAdvMsg());
		mainData.put("orgName", userBean.getOrgName());
		mainData.put("userName", userBean.getUserName());
		mainData.put("parentOrgName", userBean.getParentOrgName());
		return mainData;
	}

	private void throwLMSExceptionAccordingToSaleStatus(int errorCode,String errorMessage) throws LMSException {
		throw new LMSException(errorCode,errorMessage);
	}
	
	private void checkIfSaleStatusIsNotSuccess(String saleStatus) throws LMSException {
		if ("AGT_INS_BAL".equalsIgnoreCase(saleStatus)){
			throwLMSExceptionAccordingToSaleStatus(LMSErrors.INSUFFICIENT_AGENT_BALANCE_ERROR_CODE,LMSErrors.INSUFFICIENT_AGENT_BALANCE_ERROR_MESSAGE);
		} else if ("RET_INS_BAL".equalsIgnoreCase(saleStatus)){
			throwLMSExceptionAccordingToSaleStatus(LMSErrors.INSUFFICIENT_RETAILER_BALANCE_ERROR_CODE,LMSErrors.INSUFFICIENT_RETAILER_BALANCE_ERROR_MESSAGE);
		} else if ("NO_DRAWS".equalsIgnoreCase(saleStatus)){
			throwLMSExceptionAccordingToSaleStatus(LMSErrors.TRANSACTION_FAILED_ERROR_CODE,LMSErrors.TRANSACTION_FAILED_ERROR_MESSAGE);
		} else if ("FRAUD".endsWith(saleStatus)){
			throwLMSExceptionAccordingToSaleStatus(LMSErrors.PURCHASE_FRAUD_ERROR_CODE, LMSErrors.PURCHASE_FRAUD_ERROR_MESSAGE);
		} else if ("UNAUTHORISED".endsWith(saleStatus)){
			throwLMSExceptionAccordingToSaleStatus(LMSErrors.UNAUTHORIZED_SALE_ERROR_CODE,LMSErrors.UNAUTHORIZED_SALE_ERROR_MESSAGE);
		} else if ("LIMIT_REACHED".endsWith(saleStatus)){
			throwLMSExceptionAccordingToSaleStatus(LMSErrors.LIMIT_REACHED_ERROR_CODE, LMSErrors.LIMIT_REACHED_ERROR_MESSAGE);
		} else{
			throwLMSExceptionAccordingToSaleStatus(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
		}
	}
	
	private JSONObject prepareDrawDataResponse(JSONArray drawDataArray) throws LMSException {
		JSONObject commonSaleDataRes = new JSONObject();
		try {
			commonSaleDataRes.put("ticketNumber", kenoTwoPurchaseBean.getTicket_no());
			commonSaleDataRes.put("barcodeCount",kenoTwoPurchaseBean.getTicket_no() + kenoTwoPurchaseBean.getReprintCount()
							+ ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB
									&& LMSFilterDispatcher.isBarCodeRequired)
											? kenoTwoPurchaseBean.getBarcodeCount() : ""));
			commonSaleDataRes.put("gameName", kenoTwoPurchaseBean.getGameDispName());
			commonSaleDataRes.put("purchaseDate", kenoTwoPurchaseBean.getPurchaseTime().split(" ")[0]);
			commonSaleDataRes.put("purchaseTime", kenoTwoPurchaseBean.getPurchaseTime().split(" ")[1]);
			commonSaleDataRes.put("purchaseAmt", kenoTwoPurchaseBean.getTotalPurchaseAmt());
			commonSaleDataRes.put("drawData", drawDataArray);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return commonSaleDataRes;
	}

	private void ifPickedNoInvalidAsPerBetType(KenoPurchaseBean drawGamePurchaseBean, boolean isValid) throws LMSException {
		if (!isValid) {
			drawGamePurchaseBean.setSaleStatus("INVALID_INPUT");
			setKenoTwoPurchaseBean(drawGamePurchaseBean);
			logger.error(
					"-----------KenoTwo Validation Error-------------------" + drawGamePurchaseBean.getSaleStatus());
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
		}
	}

	
	private boolean validatePickedNoAsPerBetType(KenoTicketBean kenoTicketBean ) {
		boolean isValid = true;
		String playerData = kenoTicketBean.getPickedNumbers();
		String pickValue = KenoTwoConstants.BET_TYPE_MAP.get(kenoTicketBean.getBetName());
		if (!kenoTicketBean.isQp()) {
			isValid = Util.validateNumber(KenoTwoConstants.START_RANGE, KenoTwoConstants.END_RANGE,
					playerData.replace(",UL,", ",").replace(",BL", ""), false);
			logger.debug("-Data---" +kenoTicketBean.getBetName() + "---" +kenoTicketBean.getBetName() + "---" + playerData);
		}
		if (kenoTicketBean.getBetName().contains("Direct") || "Banker1AgainstAll".equals(kenoTicketBean.getBetName())) {
			isValid = kenoTicketBean.getNoPicked().equals(pickValue);
			logger.debug("-Direct---" + kenoTicketBean.getBetName() + "---" +kenoTicketBean.getNoPicked());
			return isValid;
		} else if (kenoTicketBean.getBetName().contains("Perm")) {
			String defPick[] = pickValue.split(",");
			String selPick = kenoTicketBean.getNoPicked();
			logger.debug("-Perm---" +kenoTicketBean.getBetName()+ "---" + kenoTicketBean.getNoPicked());
			if (Integer.parseInt(defPick[0]) > Integer.parseInt(selPick)
					|| Integer.parseInt(defPick[1]) < Integer.parseInt(selPick)) {
				isValid = false;
			}
			return isValid;
		} else if ("Banker".equals(kenoTicketBean.getBetName())) {
			logger.debug("-Banker---" + kenoTicketBean.getBetName()+ "---" + kenoTicketBean.getNoPicked());
			String defPick[] = pickValue.split(",");
			String selPick[] = kenoTicketBean.getNoPicked().split(",");
			// for upper line & below line
			if (Integer.parseInt(defPick[0]) > Integer.parseInt(selPick[0])
					|| Integer.parseInt(defPick[1]) < Integer.parseInt(selPick[0])
					|| Integer.parseInt(defPick[2]) > Integer.parseInt(selPick[1])
					|| Integer.parseInt(defPick[3]) < Integer.parseInt(selPick[1])) {
				isValid = false;
			}
			return isValid;
		}
		return isValid;
	}


	private String[] setdrawIdArrIfInputisManual(JSONObject commonSaleDataReq) {
		JSONArray drawDataArr = commonSaleDataReq.getJSONArray("drawData");
		int drawSize = drawDataArr.size();
		String[] drawIdArr = new String[drawSize];
		if (!commonSaleDataReq.getBoolean("isDrawManual")) {
			for (int i = 0; i < drawSize; i++) {
				JSONObject drawData = drawDataArr.getJSONObject(i);
				drawIdArr[i] = String.valueOf(drawData.getInt("drawId"));
			}
		}
		return drawIdArr;
	}

	private void prepareKenoPurchaseBean(JSONObject requestData,
			List<KenoTicketBean> kenoTicketBeans, KenoPurchaseBean drawGamePurchaseBean) {
		
		JSONObject commonSaleDataReq = (JSONObject) requestData.get("commonSaleData");
		String totalPurchaseAmt = (String) requestData.get("totalPurchaseAmt");
		JSONArray betTypeDataReq = (JSONArray) requestData.get("betTypeData");
		int panelSize = betTypeDataReq.size();
		String[] drawIdArr = setdrawIdArrIfInputisManual(commonSaleDataReq);
		setArrayData(panelSize, kenoTicketBeans, drawGamePurchaseBean);
		int noOfDraws = Integer.parseInt(commonSaleDataReq.getString("noOfDraws").trim());
		int isAdvancedPlay = "false".equals(commonSaleDataReq.getString("isAdvancePlay").trim()) ? 0 : 1;
		ServletContext sc = ServletActionContext.getServletContext();
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		drawGamePurchaseBean.setGameId(gameId);
		drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(getGameId()));
		drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(getGameId()));
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(getGameId()));
		drawGamePurchaseBean.setNoOfDraws(noOfDraws);
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
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
		
	}

	private void setArrayData(int panelSize, List<KenoTicketBean> kenoTicketBeans,
			KenoPurchaseBean drawGamePurchaseBean) {
		int[] isQuickPick = new int[panelSize];
		String[] pickedData = new String[panelSize];
		String noPicked[] = new String[panelSize];
		String[] playType = new String[panelSize];
		boolean[] qpPreGenerated = new boolean[panelSize];
		int[] betAmountMultiple = new int[panelSize];
		int i = 0;
		for (KenoTicketBean kenoTicketBean : kenoTicketBeans) {
		
			isQuickPick[i] = kenoTicketBean.isQp() ? 1 : 2;
			pickedData[i] = kenoTicketBean.getPickedNumbers();
			noPicked[i] = kenoTicketBean.getNoPicked();
			playType[i] = kenoTicketBean.getBetName();
			betAmountMultiple[i] = kenoTicketBean.getBetAmtMul();
			qpPreGenerated[i] = kenoTicketBean.isQPPreGenerated();
			i++;
		}
		
		drawGamePurchaseBean.setIsQuickPick(isQuickPick);
		drawGamePurchaseBean.setPlayerData(pickedData);
		drawGamePurchaseBean.setNoPicked(noPicked);
		drawGamePurchaseBean.setPlayType(playType);
		drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
		drawGamePurchaseBean.setQPPreGenerated(qpPreGenerated);
	}

	private KenoTicketBean setBetTypeData(JSONArray betTypeDataReq, int i) {
		JSONObject panelData = betTypeDataReq.getJSONObject(i);
		KenoTicketBean kenoTicketBean = new Gson().fromJson(panelData.toString(), KenoTicketBean.class);
		return kenoTicketBean;
	}
}
