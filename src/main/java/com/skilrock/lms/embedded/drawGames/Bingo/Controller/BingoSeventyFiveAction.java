package com.skilrock.lms.embedded.drawGames.Bingo.Controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.embedded.drawGames.Bingo.Beans.BingoSeventyFive;
import com.skilrock.lms.embedded.drawGames.Bingo.Service.BingoService;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class BingoSeventyFiveAction extends ActionSupport implements ServletRequestAware , ServletResponseAware{

	/**
	 * @author Mukesh Sharma
	 */
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(BingoSeventyFiveAction.class);

	// Constant
	private static final String DEVICE_TYPE = "TERMINAL";
	private static final String LMS_TERMINAL = "LMS_Terminal";

	private ServletContext servletContext = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	BingoSeventyFive bingoSeventyFivePurchaseBean = null;

	private String betAmt;
	private String[] drawIdArr;
	private int gameId;
	private int isAdvancedPlay;
	private int noOfDraws;
	private int noOfLines;
	private String noPicked;
	private String pickedNum;
	private String pickedNumbers;
	private String playType;
	private String QP;
	private String totalPurchaseAmt;
	private String userName;
	private long LSTktNo;
	private String plrMobileNumber;

	private BingoService bingoServiceHelper;
	private String finalPurchaseData;
	private UserInfoBean userBean = null;
	BingoSeventyFive bingoSeventyFiveResponse = null;
	
	public BingoSeventyFiveAction() {
		bingoServiceHelper = new BingoService();
	}

	public BingoSeventyFiveAction(BingoService bingoService) {
		bingoServiceHelper =  new BingoService();
	}

	public BingoSeventyFive getBingoSeventyFiveResponse() {
		return bingoSeventyFiveResponse;
	}

	public void setBingoSeventyFiveResponse(BingoSeventyFive bingoSeventyFiveResponse) {
		this.bingoSeventyFiveResponse = bingoSeventyFiveResponse;
	}

	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

	public void setNoPicked(String noPicked) {
		this.noPicked = noPicked;
	}

	public void setPickedNum(String pickedNum) {
		this.pickedNum = pickedNum;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public void setQP(String qp) {
		QP = qp;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public void setFinalPurchaseData(String finalPurchaseData) {
		this.finalPurchaseData = finalPurchaseData;
	}

	public String getFinalPurchaseData() {
		return finalPurchaseData;
	}

	public int getGameId() {
		return gameId;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public String getBetAmt() {
		return betAmt;
	}

	public String[] getDrawIdArr() {
		return drawIdArr;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public String getNoPicked() {
		return noPicked;
	}

	public String getPickedNum() {
		return pickedNum;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
	}

	public String getPlayType() {
		return playType;
	}

	public String getQP() {
		return QP;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public String getUserName() {
		return userName;
	}

	public void purchaseTicketProcess() {
		servletContext = ServletActionContext.getServletContext();
		try {
			setGameId(Util.getGameId("BingoSeventyFive"));
			String isDraw = (String) servletContext.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				setResponseDataForIfDrawGameNotAvailable();
				return;
			}
          
			userBean = extractingUserInfoBeanFromSession(servletContext);
			int serviceId =  Util.getServiceIdFormCode(getRequest().getAttribute("code").toString());
			validatingServiceIdAndUserMappingIdShouldNotZero(serviceId);

			bingoSeventyFivePurchaseBean = new BingoSeventyFive();
			prepareBingoSeventyFivePurchaseBean();
			prepareDataInBingoSeventyFivePurchaseBean(serviceId);

			TransactionManager.setResponseData("true");
			setBingoSeventyFiveResponse(
					bingoServiceHelper.commonPurchseProcessBingoSeventyFive(userBean, bingoSeventyFivePurchaseBean));

			if (!isSaleStatusSuccess()) {
				setResponseDataIfSaleStatusNotSuccess();
				return;
			}

			String finalPurchaseData = null;
			finalPurchaseData = prepeareFinalResponseData();
			getRequest().setAttribute("purchaseData", finalPurchaseData);
			sendSmsToPlayerIfSaleStatusSuucess();
			setFinalPurchaseData(finalPurchaseData);
			setResponseDataToOutputStream(finalPurchaseData);
		} catch (Exception e) {
           e.printStackTrace();
		}
	}

	private void setResponseDataForIfDrawGameNotAvailable() throws IOException {
		this.finalPurchaseData = "ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE;
		response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE).getBytes());
	}

	private UserInfoBean extractingUserInfoBeanFromSession(ServletContext servletContext) {
		@SuppressWarnings("rawtypes")
		Map currentUserSessionMap = (Map) servletContext.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(getUserName());
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		return userBean;
	}

	private void validatingServiceIdAndUserMappingIdShouldNotZero(int serviceId) throws LMSException {
		if (serviceId == 0 || userBean.getCurrentUserMappingId() == 0) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	private void prepareBingoSeventyFivePurchaseBean() {
		prepareDataInBingoSeventyFivePurchaseBeanFromUtilClass();
		prepareBingoSeventyFivePurchaseBeanAfterSplitRequestDataByNxt();
		prepareDataInBingoSeventyFivePurchaseBeanFromUserBean();
		prepareDataInBingoSeventyFivePurchaseBeanFromServletContextClass();
	}

	private void prepareDataInBingoSeventyFivePurchaseBeanFromUtilClass() {
		long lastPrintedTicket = 0;
		int lstGameId = 0;
		if (LSTktNo != 0) {
			lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			lastPrintedTicket = LSTktNo / Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
		}
		bingoSeventyFivePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
		bingoSeventyFivePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
		bingoSeventyFivePurchaseBean.setLastSoldTicketNo(lastPrintedTicket + "");
		bingoSeventyFivePurchaseBean.setLastGameId(lstGameId);
	}

	private void prepareBingoSeventyFivePurchaseBeanAfterSplitRequestDataByNxt() {
		String[] playTypeArr = getPlayType().split("Nxt");
		String[] pickedNumbersArr = getPickedNumbers().split("Nxt");
		int noOfPanel = pickedNumbersArr.length;
		String[] noPickedArr = getNoPicked().replaceAll(" ", "").split("Nxt");
		String[] betAmtStr = getBetAmt().split("Nxt");
		int[] betAmtArr = new int[noOfPanel];
		String[] QPStr = getQP().split("Nxt");
		int[] QPArr = new int[noOfPanel];
		boolean[] qpPreGenerated = new boolean[noOfPanel];
		for (int i = 0; i < noOfPanel; i++) {
			betAmtArr[i] = Integer.parseInt(betAmtStr[i]);
			QPArr[i] = Integer.parseInt(QPStr[i]);
			qpPreGenerated[i] = false;
		}
		bingoSeventyFivePurchaseBean.setNoOfPanel(noOfPanel);
		bingoSeventyFivePurchaseBean.setBetAmountMultiple(betAmtArr);
		bingoSeventyFivePurchaseBean.setIsQuickPick(QPArr);
		bingoSeventyFivePurchaseBean.setQPPreGenerated(qpPreGenerated);
		bingoSeventyFivePurchaseBean.setNoPicked(noPickedArr);
		bingoSeventyFivePurchaseBean.setPlayerData(pickedNumbersArr);
		bingoSeventyFivePurchaseBean.setPlayType(playTypeArr);
	}

	private void prepareDataInBingoSeventyFivePurchaseBeanFromUserBean() {
		bingoSeventyFivePurchaseBean.setPartyId(userBean.getUserOrgId());
		bingoSeventyFivePurchaseBean.setPartyType(userBean.getUserType());
		bingoSeventyFivePurchaseBean.setUserId(userBean.getUserId());
		bingoSeventyFivePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
	}

	private void prepareDataInBingoSeventyFivePurchaseBeanFromServletContextClass() {
		@SuppressWarnings("unchecked")
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) servletContext
				.getAttribute("drawIdTableMap");
		bingoSeventyFivePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		bingoSeventyFivePurchaseBean.setBarcodeType((String) LMSUtility.sc.getAttribute("BARCODE_TYPE"));
		String refMerchantId = (String) servletContext.getAttribute("REF_MERCHANT_ID");
		bingoSeventyFivePurchaseBean.setRefMerchantId(refMerchantId);
	}

	private void prepareDataInBingoSeventyFivePurchaseBean(int serviceId) {
		bingoSeventyFivePurchaseBean.setIsAdvancedPlay(getIsAdvancedPlay());
		bingoSeventyFivePurchaseBean.setNoOfDraws(getNoOfDraws());
		bingoSeventyFivePurchaseBean.setPlrMobileNumber(getPlrMobileNumber());
		bingoSeventyFivePurchaseBean.setServiceId(serviceId);
		bingoSeventyFivePurchaseBean.setBonus("N");
		bingoSeventyFivePurchaseBean.setPurchaseChannel(LMS_TERMINAL);
		bingoSeventyFivePurchaseBean.setDeviceType(DEVICE_TYPE);
		if (drawIdArr != null) {
			bingoSeventyFivePurchaseBean.setDrawDateTime(Arrays.asList(getDrawIdArr()));
		}
		bingoSeventyFivePurchaseBean.setGameId(gameId);
		bingoSeventyFivePurchaseBean.setActionName(ActionContext.getContext().getName());
	}

	private boolean isSaleStatusSuccess() {
		return "SUCCESS".equalsIgnoreCase(bingoSeventyFiveResponse.getSaleStatus());
	}

	private void setResponseDataIfSaleStatusNotSuccess() throws IOException {
		String finalPurchaseData;
		finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(getBingoSeventyFiveResponse().getSaleStatus()) + "|";
		this.finalPurchaseData = finalPurchaseData;
		setResponseDataToOutputStream(finalPurchaseData);
	}

	private void setResponseDataToOutputStream(String responseData) throws IOException {
		System.out.println("FINAL PURCHASE DATA BingoSeventyFive:" + responseData);
		getResponse().getOutputStream().write(responseData.getBytes());
	}

	private String prepeareFinalResponseData() {
		StringBuilder finalData = new StringBuilder("");
		String time = bingoSeventyFiveResponse.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");
		finalData.append("TicketNo:" + bingoSeventyFiveResponse.getTicket_no()
				+ bingoSeventyFiveResponse.getReprintCount() + "|brCd:" + bingoSeventyFiveResponse.getTicket_no()
				+ bingoSeventyFiveResponse.getReprintCount()
				+ ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB
						&& LMSFilterDispatcher.isBarCodeRequired) ? bingoSeventyFiveResponse.getBarcodeCount() : "")
				+ "|Date:" + time);

		int noOfPanels = bingoSeventyFiveResponse.getNoOfPanel();
		String[] playTypeStr = bingoSeventyFiveResponse.getPlayType();
		for (int i = 0; i < noOfPanels; i++) {
			double panelPri = bingoSeventyFiveResponse.getBetAmountMultiple()[i] * bingoSeventyFiveResponse.getUnitPrice()[i] * bingoSeventyFiveResponse.getNoOfDraws();
			String panelPrice = "|PanelPrice:" + (new DecimalFormat("#.##").format(panelPri));
			if ("Banker".equalsIgnoreCase(playTypeStr[i])) {
				// logger.debug("---------------BANKER---------------");

				String playerData = bingoSeventyFiveResponse.getPlayerData()[i];
				String[] banker = playerData.replace(",BL", "").split(",UL,");
				finalData.append("|PlayType:" + playTypeStr[i] + "|UL:" + banker[0] + "|BL:" + banker[1] + panelPrice
						+ "|QP:" + bingoSeventyFiveResponse.getIsQuickPick()[i]);
			} else {

				// logger.debug("--------------OTHERS--------------");
				finalData.append("|PlayType:" + playTypeStr[i] + "|Num:" + bingoSeventyFiveResponse.getPlayerData()[i]
						+ panelPrice + "|QP:" + bingoSeventyFiveResponse.getIsQuickPick()[i]);

			}
		}
		// String promoTicketDta = settingPromoTicketData(userBean,
		// bingoSeventyFiveResponse);
		String advtMsg = settingAdvtMessage(bingoSeventyFiveResponse);
		StringBuilder drawTimeBuild = settingDrawDateTime();
		String balance = settingFormatedPlayerBalance(userBean);
		finalData.append("|TicketCost:" + bingoSeventyFiveResponse.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance + "|" + advtMsg);
		return finalData.toString();
	}

	private String settingAdvtMessage(BingoSeventyFive bingoSeventyFiveResponse) {
		String advtMsg = "";
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		UtilApplet.getAdvMsgs(bingoSeventyFiveResponse.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);
		advtMsg = topMsgsStr.length() != 0 ? "topAdvMsg:" + topMsgsStr + "|" : "";
		advtMsg = bottomMsgsStr.length() != 0 ? advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|" : "";
		return advtMsg;
	}

	private StringBuilder settingDrawDateTime() {
		int listSize = bingoSeventyFiveResponse.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + bingoSeventyFiveResponse.getDrawDateTime().get(i))
					.replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
		}
		return drawTimeBuild;
	}

	private String settingFormatedPlayerBalance(UserInfoBean userBean) {
		double balance = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		NumberFormat numberfFormat = NumberFormat.getInstance();
		numberfFormat.setMinimumFractionDigits(2);
		String formatedBalance = numberfFormat.format(balance).replaceAll(",", "");
		return formatedBalance;
	}

	private void sendSmsToPlayerIfSaleStatusSuucess() {
		if (isSaleStatusSuccess()) {
			String smsData = com.skilrock.lms.common.utility.CommonMethods.prepareSMSData(
					bingoSeventyFiveResponse.getPlayerData(), bingoSeventyFiveResponse.getPlayType(),
					new StringBuilder(String.valueOf(bingoSeventyFiveResponse.getTotalPurchaseAmt())),
					new StringBuilder(
							bingoSeventyFiveResponse.getTicket_no() + bingoSeventyFiveResponse.getReprintCount()),
					bingoSeventyFiveResponse.getDrawDateTime());
			com.skilrock.lms.common.utility.CommonMethods.sendSMS(smsData);
		}
	}

}
