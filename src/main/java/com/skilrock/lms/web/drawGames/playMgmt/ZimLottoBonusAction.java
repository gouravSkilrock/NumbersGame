package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ZimLottoBonusAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log logger = LogFactory.getLog(ZimLottoBonusAction.class);
	private int gameId = Util.getGameId("ZimLottoBonus");
	private LottoPurchaseBean zimLottoBonusPurchaseBean;
	private DrawGameRPOSHelper helper;
	private long lastTicketNumber;
	private String errorMessage;
	private String json;
	private String responceForTest;
	ZimLottoBonusRequest zimLottoBonusRequest = new ZimLottoBonusRequest(new JSONObject(), new ArrayList<String>());
	LottoPurchaseBean drawPurchaseBean;
	
	public ZimLottoBonusAction() {
		super(ZimLottoBonusAction.class.getName());
		this.helper = new DrawGameRPOSHelper();
	}
	
	public ZimLottoBonusAction(DrawGameRPOSHelper helper) {
		super(ZimLottoBonusAction.class.getName());
		this.helper = helper;
	}

	public LottoPurchaseBean getZimLottoBonusPurchaseBean() {
		return zimLottoBonusPurchaseBean;
	}

	public void setZimLottoBonusPurchaseBean(LottoPurchaseBean zimLottoBonusPurchaseBean) {
		this.zimLottoBonusPurchaseBean = zimLottoBonusPurchaseBean;
	}
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public long getLSTktNo() {
		return lastTicketNumber;
	}

	public void setLSTktNo(long lSTktNo) {
		lastTicketNumber = lSTktNo;
	}

	public String getErrMsg() {
		return errorMessage;
	}

	public void setErrMsg(String errMsg) {
		this.errorMessage = errMsg;
	}
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setDrawGameRPOSHelper(DrawGameRPOSHelper helper) {
		this.helper = helper;
	}

	public String getResponceForTest() {
		return responceForTest;
	}

	public void setResponceForTest(String responceForTest) {
		this.responceForTest = responceForTest;
	}

	public String purchaseTicketProcess(){
		logger.info("className: {} --In Zim Lotto Bonus Purchase Ticket-- ");
		logger.debug("Inside purchaseTicketProcess" +json);
		try{
			setDataFromRequest();
			if(isNoDrawIdForAdvancedPlay()){
				return ERROR;
			}
			createDrawPurchaseBean();
			if(!validatePickedNUmbersAndBet()){
				//Returning success but the sale status will be "ERROR"
				return SUCCESS;
			}
			zimLottoBonusPurchaseBean = helper.zimLottoBonusPurchaseTicket(zimLottoBonusRequest.getUserBean(),drawPurchaseBean);
			processResponseFromHelper();
		}catch(LMSException e){
			lmsExceptionOccured(e);
		}catch(Exception e){
			e.printStackTrace();
			generalExceptionOccured();
		}
		responceForTest = zimLottoBonusRequest.getJsonResponse().toString();
		System.out.println("responceForTest = " + responceForTest);
		checkForSaleSuccess();
		printResponce();
		return SUCCESS;
	}
	
	private void setDataFromRequest() throws LMSException {
		processDataFromRequest();
		setDrawRelatedVariables();
	}

	private void processDataFromRequest() throws LMSException {
		zimLottoBonusRequest.setUserBean(getUserBean());
		response.setContentType("application/json");
		//helper = new DrawGameRPOSHelper();
		extractRequestJson();
		processLastTicketNumber();
	}
	
	private void extractRequestJson() {
		JSONObject requestJson = (JSONObject) JSONSerializer.toJSON(json);
		zimLottoBonusRequest.setTokenId((String)requestJson.get("tokenId"));
		zimLottoBonusRequest.setCommonSaleData((JSONObject) requestJson.get("commonSaleData"));
		zimLottoBonusRequest.setBetTypeData((JSONArray) requestJson.get("betTypeData"));
		zimLottoBonusRequest.setTotalPurchaseAmount((String) requestJson.get("totalPurchaseAmt"));
	}
	
	private void processLastTicketNumber() {
		long lastPrintedTicket = 0;
		int lastGameId = 0;
		String actionName = ActionContext.getContext().getName();
		try {
			lastTicketNumber = CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, zimLottoBonusRequest.getUserBean().getUserName());
			if (lastTicketNumber != 0) {
				lastPrintedTicket = lastTicketNumber / Util.getDivValueForLastSoldTkt(String.valueOf(lastTicketNumber).length());
				lastGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTicketNumber)));
			}
			helper.insertEntryIntoPrintedTktTableForWeb(lastGameId, zimLottoBonusRequest.getUserBean().getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	private void setDrawRelatedVariables() {
		setUserPickedValues();
		setBetRelatedData();
		setDrawIdForNonManualDraw();
	}
	
	private void setUserPickedValues() {
		setDefaultValuesForFlagVariables();
		setArrays();
	}

	private void setArrays() {
		zimLottoBonusRequest.setNumbersPicked(new String[zimLottoBonusRequest.getBetTypeData().size()]);
		zimLottoBonusRequest.setDrawId(new String[zimLottoBonusRequest.getCommonSaleData().getJSONArray("drawData").size()]);
		zimLottoBonusRequest.setIsQuickPick(new Integer[zimLottoBonusRequest.getBetTypeData().size()]);
		zimLottoBonusRequest.setBetAmountMultiple(new int[zimLottoBonusRequest.getBetTypeData().size()]);
	}

	private void setDefaultValuesForFlagVariables() {
		zimLottoBonusRequest.setIsAdvancedPlay("false".equals(zimLottoBonusRequest.getCommonSaleData().getString("isAdvancePlay").trim()) ? 0 : 1);
		zimLottoBonusRequest.setNumberPicked(0);
		zimLottoBonusRequest.setQpPreGenerated(false);
	}
	
	private void setBetRelatedData() {
		for(int i=0; i<zimLottoBonusRequest.getBetTypeData().size(); i++){
			JSONObject panelData = zimLottoBonusRequest.getBetTypeData().getJSONObject(i);
			setUserPickedDataArrays(panelData, i);
			setFlagVariables(panelData);
		}
	}

	private void setUserPickedDataArrays(JSONObject panelData, int i) {
		zimLottoBonusRequest.getIsQuickPick()[i] = panelData.getBoolean("isQp") == true ? 1:2;
		zimLottoBonusRequest.getBetAmountMultiple()[i] = panelData.getInt("betAmtMul");
		zimLottoBonusRequest.getNumbersPicked()[i] = panelData.getString("pickedNumbers");
	}
	
	private void setFlagVariables(JSONObject panelData) {
		zimLottoBonusRequest.setNumberPicked(panelData.getInt("noPicked"));
		zimLottoBonusRequest.setPlayType(panelData.getString("betName"));
		zimLottoBonusRequest.setBetAmount(panelData.getInt("betAmtMul"));
		zimLottoBonusRequest.setQpPreGenerated(panelData.getBoolean("QPPreGenerated"));
	}
	
	private void setDrawIdForNonManualDraw() {
		if(!zimLottoBonusRequest.getCommonSaleData().getBoolean("isDrawManual"))
			for (int i = 0; i < zimLottoBonusRequest.getCommonSaleData().getJSONArray("drawData").size(); i++) {
				JSONObject drawData = zimLottoBonusRequest.getCommonSaleData().getJSONArray("drawData").getJSONObject(i);
				zimLottoBonusRequest.getDrawId()[i] = String.valueOf(drawData.getInt("drawId"));
			}
	}

	private boolean isNoDrawIdForAdvancedPlay(){
		if (zimLottoBonusRequest.getIsAdvancedPlay() == 1 && zimLottoBonusRequest.getDrawId() == null) {
			setErrMsg(DGErrorMsg.buyErrMsg(""));
			return true;
		}
		return false;
	}

	private boolean validatePickedNUmbersAndBet() throws LMSException {
		TransactionManager.setResponseData("true");
		if(!validatePickedNumbers())
			return false;
		isBetSlipExpired();
		return true;
	}

	private boolean validatePickedNumbers(){
		for (int i = 0; i < zimLottoBonusRequest.getNumbersPicked().length; i++) 
			if (!"QP".equals(zimLottoBonusRequest.getNumbersPicked()[i])) 
				if (!Util.validateNumber(ConfigurationVariables.ZIMLOTTOBONUS_START_RANGE,ConfigurationVariables.ZIMLOTTOBONUS_END_RANGE,zimLottoBonusRequest.getNumbersPicked()[i], false)) {
					drawPurchaseBean.setSaleStatus("ERROR");
					return false;		//return SUCCESS;
				}
		return true;
	}
	
	private void isBetSlipExpired() throws LMSException {
		if (zimLottoBonusRequest.getTokenId() != null) 
			if (!TpEBetMgmtDaoImpl.getInstance().isBetSlipActive(zimLottoBonusRequest.getTokenId())) 
				throw new LMSException(LMSErrors.BET_SLIP_EXPIRED_ERROR_CODE, LMSErrors.BET_SLIP_EXPIRED_ERROR_MESSAGE);
	}
	
	private void processResponseFromHelper() throws LMSException, ServletException, IOException {
		if (!"SUCCESS".equalsIgnoreCase(getZimLottoBonusPurchaseBean().getSaleStatus()))
			saleStatusUnsuccessful(getZimLottoBonusPurchaseBean().getSaleStatus());
		else
			saleStatusSuccessful();
	}
	
	private void saleStatusUnsuccessful(String saleStatus) throws LMSException {
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
		else if ("LIMIT_REACHED".endsWith(saleStatus))
			throw new LMSException(LMSErrors.LIMIT_REACHED_ERROR_CODE, LMSErrors.LIMIT_REACHED_ERROR_MESSAGE);
		
		throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
	}
	
	private void saleStatusSuccessful() throws ServletException, IOException {
		setResponceForSuccess(createMainData());
		setEbetSaleRequestStatus();
		CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, zimLottoBonusRequest.getUserBean().getUserName(), getZimLottoBonusPurchaseBean().getTicket_no() + getZimLottoBonusPurchaseBean().getReprintCount());
	}
	
	private void setResponceForSuccess(JSONObject mainData) {
		zimLottoBonusRequest.getJsonResponse().put("isSuccess", true);
		zimLottoBonusRequest.getJsonResponse().put("errorMsg", "");
		zimLottoBonusRequest.getJsonResponse().put("mainData", mainData);
		zimLottoBonusRequest.getJsonResponse().put("isPromo", false);
	}

	private void setEbetSaleRequestStatus() {
		if (zimLottoBonusRequest.getTokenId() != null && !(zimLottoBonusRequest.getTokenId()).trim().isEmpty())
		    Util.setEbetSaleRequestStatusDone(zimLottoBonusRequest.getTokenId(), zimLottoBonusRequest.getUserBean().getUserOrgId());
	}
	
	private void lmsExceptionOccured(LMSException e) {
		zimLottoBonusRequest.getJsonResponse().put("isSuccess", false);
		zimLottoBonusRequest.getJsonResponse().put("errorCode", e.getErrorCode());
		zimLottoBonusRequest.getJsonResponse().put("errorMsg", e.getErrorMessage());
	}

	private void generalExceptionOccured() {
		zimLottoBonusRequest.getJsonResponse().put("isSuccess", false);
		zimLottoBonusRequest.getJsonResponse().put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		zimLottoBonusRequest.getJsonResponse().put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}

	private void checkForSaleSuccess() {
		logger.info("className: {} Zim Lotto Bonus Sale Response Data : {}"+ zimLottoBonusRequest.getJsonResponse());
		if("SUCCESS".equalsIgnoreCase(zimLottoBonusPurchaseBean.getSaleStatus()))
			saleSuccessful();
	}
	
	private void saleSuccessful() {
		StringBuilder cost = new StringBuilder(String.valueOf(zimLottoBonusPurchaseBean.getTotalPurchaseAmt()));
		StringBuilder ticket = new StringBuilder(zimLottoBonusPurchaseBean.getTicket_no()).append(zimLottoBonusPurchaseBean.getReprintCount());
		String sms = CommonMethods.prepareSMSData(zimLottoBonusRequest.getNumbersPicked(), new String[]{zimLottoBonusRequest.getPlayType()}, cost, ticket, zimLottoBonusRequest.getDrawDateTime());
		CommonMethods.sendSMS(sms);
	}
	
	private void printResponce(){
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		printWriter.print(zimLottoBonusRequest.getJsonResponse());
		printWriter.flush();
		printWriter.close();
	}
	
	
	//**********************************************************************************************************************************************
	
	//THIS CODE CREATES JSON RESPONCE ARRAY TO BE RETURNED
	
	//**********************************************************************************************************************************************
	
	private JSONObject createMainData(){
		JSONObject mainData = new JSONObject();
		mainData.put("commonSaleData", createCommonSaleDataResponce());
		mainData.put("betTypeData", createBetTypeArray());
		mainData = setGeneralDetailForMainData(mainData);
		return mainData;
	}
	
	private JSONObject createCommonSaleDataResponce(){
		JSONObject commonSaleDataResponce = new JSONObject();
		commonSaleDataResponce = setGeneralDetailToCommonSaleDataResponce(commonSaleDataResponce);
		commonSaleDataResponce = setPurcahseDetailToCommonSaleDataResponce(commonSaleDataResponce);
		commonSaleDataResponce.put("drawData", createDrawDataArray());
		return commonSaleDataResponce;
	}

	private JSONObject setGeneralDetailToCommonSaleDataResponce(JSONObject commonSaleDataResponce) {
		commonSaleDataResponce.put("ticketNumber", zimLottoBonusPurchaseBean.getTicket_no());
		commonSaleDataResponce.put("barcodeCount", zimLottoBonusPurchaseBean.getTicket_no() + zimLottoBonusPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? zimLottoBonusPurchaseBean.getBarcodeCount():""));
		commonSaleDataResponce.put("gameName", zimLottoBonusPurchaseBean.getGameDispName());
		return commonSaleDataResponce;
	}

	private JSONObject setPurcahseDetailToCommonSaleDataResponce(JSONObject commonSaleDataResponce) {
		commonSaleDataResponce.put("purchaseDate", zimLottoBonusPurchaseBean.getPurchaseTime().split(" ")[0]);
		commonSaleDataResponce.put("purchaseTime", zimLottoBonusPurchaseBean.getPurchaseTime().split(" ")[1]);
		commonSaleDataResponce.put("purchaseAmt", zimLottoBonusPurchaseBean.getTotalPurchaseAmt());
		return commonSaleDataResponce;
	}
	
	private JSONArray createDrawDataArray(){
		JSONArray drawDataArray = new JSONArray();
		for (int i=0; i<zimLottoBonusPurchaseBean.getDrawDateTime().size(); i++) {
			drawDataArray.add(createDrawData(i));
		}
		return drawDataArray;
	}

	private JSONObject createDrawData(int i) {
		JSONObject drawData = new JSONObject();
		String drawDataString = (String) zimLottoBonusPurchaseBean.getDrawDateTime().get(i);
		drawData = setDrawIdForManualDraw(drawData, i, drawDataString);
		drawData = setDrawDataAndDrawDateTime(drawData, drawDataString);
		return drawData;
	}

	private JSONObject setDrawIdForManualDraw(JSONObject drawData, int i, String drawDataString) {
		if (!zimLottoBonusRequest.getCommonSaleData().getBoolean("isDrawManual"))
			drawData.put("drawId", zimLottoBonusRequest.getDrawId()[i]);
		drawData.put("drawDate", drawDataString.split(" ")[0]);
		return drawData;
	}
	
	private JSONObject setDrawDataAndDrawDateTime(JSONObject drawData, String drawDataString) {
		String drawName[] = drawDataString.split("\\*");
		if (drawName.length < 2) {
			drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
			zimLottoBonusRequest.getDrawDateTime().add(drawDataString.split(" ")[0] + " " + drawDataString.split("&")[0].split(" ")[1]);
		} else
			drawData = setDrawDataAndDrawDateTimeForLongDrawName(drawData, drawDataString);
		return drawData;
	}

	private JSONObject setDrawDataAndDrawDateTimeForLongDrawName(JSONObject drawData, String drawDataString) {
		if(!"null".equalsIgnoreCase(drawDataString.split("\\*")[1].split("&")[0]))
			drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
		drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
		zimLottoBonusRequest.getDrawDateTime().add(drawDataString.split(" ")[0] + " "+drawDataString.split("&")[0].split(" ")[1]);
		return drawData;
	}
	
	private JSONArray createBetTypeArray() {
		JSONArray betTypeArray = new JSONArray();
		betTypeArray.add(createBetTypeDataResponce());
		return betTypeArray;
	}

	private JSONObject createBetTypeDataResponce() {
		JSONObject betTypeDataResponce = new JSONObject();
		betTypeDataResponce = setQPToBetTypeDataResponce(betTypeDataResponce);
		betTypeDataResponce = setBetDetailToBetTypeDataResponce(betTypeDataResponce);
		betTypeDataResponce = setNumbersPickedDetailToBetTypeDataResponce(betTypeDataResponce);
		return betTypeDataResponce;
	}
	
	private JSONObject setQPToBetTypeDataResponce(JSONObject betTypeDataResponce) {
		boolean isQP=false;
		isQP = zimLottoBonusRequest.getIsQuickPick()[0] == 1 ? true:false; 
		betTypeDataResponce.put("isQp", isQP);
		return betTypeDataResponce;
	}

	private JSONObject setBetDetailToBetTypeDataResponce(JSONObject betTypeDataResponce) {
		betTypeDataResponce.put("betName", drawPurchaseBean.getPlayType());
		betTypeDataResponce.put("betAmtMul", drawPurchaseBean.getBetAmtMultiple());
		betTypeDataResponce.put("panelPrice", (double)(drawPurchaseBean.getBetAmtMultiple() * drawPurchaseBean.getUnitPrice() * drawPurchaseBean.getNoOfLines() * drawPurchaseBean.getNoOfDraws()));
		return betTypeDataResponce;
	}
	
	private JSONObject setNumbersPickedDetailToBetTypeDataResponce(JSONObject betTypeDataResponce) {
		betTypeDataResponce.put("pickedNumbers", drawPurchaseBean.getPicknumbers());
		betTypeDataResponce.put("numberPicked", drawPurchaseBean.getNoPicked());
		betTypeDataResponce.put("unitPrice", drawPurchaseBean.getUnitPrice());
		betTypeDataResponce.put("noOfLines", drawPurchaseBean.getNoOfLines());
		return betTypeDataResponce;
	}

	private JSONObject setGeneralDetailForMainData(JSONObject mainData) {
		mainData.put("advMessage", zimLottoBonusPurchaseBean.getAdvMsg());
		mainData.put("orgName", zimLottoBonusRequest.getUserBean().getOrgName());
		mainData.put("userName", zimLottoBonusRequest.getUserBean().getUserName());
		mainData.put("parentOrgName", zimLottoBonusRequest.getUserBean().getParentOrgName());
		return mainData;
	}
	
	//**********************************************************************************************************************************************
	
	
	
	//**********************************************************************************************************************************************

	//THIS CODE CREATES drawPurchaseBean
	
	//**********************************************************************************************************************************************
	
	
	private void createDrawPurchaseBean() {
		drawPurchaseBean = new LottoPurchaseBean();
		setGeneralDetailsForDrawPurchaseBean();
		setServletContextDetailsForDrawPurchaseBean();
		setUserBeanDetailsForDrawPurchaseBean();
		setUtilDetailsForDrawPurchaseBean();
	}
	
	private void setGeneralDetailsForDrawPurchaseBean() {
		setGameRelatedDetailsForDrawPurchaseBean();
		setQPRelatedDetailsForDrawPurchaseBean();
		setPickedNumbersRelatedDetailsForDrawPurchaseBean();
		setBetAmountRelatedDetailsForDrawPurchaseBean();
	}
	
	private void setGameRelatedDetailsForDrawPurchaseBean() {
		drawPurchaseBean.setGameId(gameId);
		drawPurchaseBean.setPlayType(zimLottoBonusRequest.getPlayType());
		drawPurchaseBean.setDrawDateTime(Arrays.asList(zimLottoBonusRequest.getDrawId()));
		drawPurchaseBean.setPurchaseChannel("LMS_Web");
	}
	
	private void setQPRelatedDetailsForDrawPurchaseBean() {
		drawPurchaseBean.setIsQuickPick(zimLottoBonusRequest.getIsQuickPick());
		drawPurchaseBean.setQPPreGenerated(zimLottoBonusRequest.isQpPreGenerated());
	}
	
	private void setPickedNumbersRelatedDetailsForDrawPurchaseBean() {
		drawPurchaseBean.setPicknumbers(zimLottoBonusRequest.getNumbersPicked());
		drawPurchaseBean.setNoPicked(zimLottoBonusRequest.getNumberPicked());
		drawPurchaseBean.setNoOfDraws(Integer.parseInt(zimLottoBonusRequest.getCommonSaleData().getString("noOfDraws").trim()));
		if("Perm6".equalsIgnoreCase(zimLottoBonusRequest.getPlayType()))
			drawPurchaseBean.setPickedNumbers(zimLottoBonusRequest.getNumbersPicked()[0]);
	}
	
	private void setBetAmountRelatedDetailsForDrawPurchaseBean() {
		drawPurchaseBean.setTotalPurchaseAmt(Double.parseDouble(zimLottoBonusRequest.getTotalPurchaseAmount()));
		drawPurchaseBean.setBetAmountMultiple(zimLottoBonusRequest.getBetAmountMultiple());
		drawPurchaseBean.setBetAmtMultiple(zimLottoBonusRequest.getBetAmount());
		drawPurchaseBean.setIsAdvancedPlay(zimLottoBonusRequest.getIsAdvancedPlay());
	}
	
	private void setServletContextDetailsForDrawPurchaseBean() {
		ServletContext servletContext = ServletActionContext.getServletContext();
		drawPurchaseBean.setDrawIdTableMap((Map<Integer, Map<Integer, String>>) servletContext.getAttribute("drawIdTableMap"));
		drawPurchaseBean.setRefMerchantId((String) servletContext.getAttribute("REF_MERCHANT_ID"));
	}

	private void setUserBeanDetailsForDrawPurchaseBean() {
		drawPurchaseBean.setUserId(zimLottoBonusRequest.getUserBean().getUserId());
		drawPurchaseBean.setPartyId(zimLottoBonusRequest.getUserBean().getUserOrgId());
		drawPurchaseBean.setPartyType(zimLottoBonusRequest.getUserBean().getUserType());
		drawPurchaseBean.setUserMappingId(zimLottoBonusRequest.getUserBean().getCurrentUserMappingId());
		drawPurchaseBean.setUserMappingId(zimLottoBonusRequest.getUserBean().getCurrentUserMappingId());
	}
	
	private void setUtilDetailsForDrawPurchaseBean() {
		drawPurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
		drawPurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
		drawPurchaseBean.setServiceId(Util.getServiceIdFormCode(request.getAttribute("code").toString()));
	}

	//**********************************************************************************************************************************************
	
	/*public String reprintTicket() throws Exception {
	logger.debug("Inside purchaseTicketProcess");
	// logger.debug("inside reprint");
	HttpSession session = request.getSession();
	UserInfoBean userInfoBean = (UserInfoBean) session
			.getAttribute("USER_INFO");
	logger.debug("Before--" + new Date());
	// logger.debug("Before--"+new Date());
	DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
	setLottoPurchaseBean((LottoPurchaseBean) helper
			.reprintTicket(userInfoBean));
	return SUCCESS;
}*/

}
