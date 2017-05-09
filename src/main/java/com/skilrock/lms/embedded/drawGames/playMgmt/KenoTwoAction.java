package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class KenoTwoAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	private static final String DEVICE_TYPE = "TERMINAL";
	private static final String LMS_TERMINAL = "LMS_Terminal";
	private static final String ERROR_TRY_AGAIN = "Error!Try Again";
	private static Log logger = LogFactory.getLog(KenoTwoAction.class);
	private DrawGameRPOSHelper helper;
	private static final long serialVersionUID = 1L;
	private String betAmt;
	private String[] drawIdArr;
	private int gameId;
	private int isAdvancedPlay;
	private KenoPurchaseBean kenoTwoPurchaseBean;
	private int noOfDraws;
	private int noOfLines;
	private String noPicked;
	private String pickedNum;
	private String pickedNumbers;
	private String playType;
	private String QP;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String totalPurchaseAmt;
	private String userName;
	private long LSTktNo;
	private String plrMobileNumber;
	public static final List numbers = Arrays.asList("", "Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)",
			"Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)");

	private String finalPurchaseData;
	private KenoPurchaseBean drawGamePurchaseBean = null;
	private UserInfoBean userBean = null;
	private ServletContext servletContext = null;

	public KenoTwoAction() {
		helper = new DrawGameRPOSHelper();
	}

	public KenoTwoAction(DrawGameRPOSHelper drawGameRPOSHelper) {
		helper = drawGameRPOSHelper;
	}
/*
	public static void main(String[] args) throws Exception {
		new KenoTwoAction().purchaseTicketProcess();
	}
*/
	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean kenoTwoPurchaseBean) {
		this.kenoTwoPurchaseBean = kenoTwoPurchaseBean;
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

	//Code commented during refactoring beacuse no call hirachy found
/*	public void getLines() throws IOException {
		String[] indexArr = new String[2];
		StringBuffer stbuild = null;
		List<String> comboList = new ArrayList<String>();
		String[] numbArr = new String[Integer.parseInt(pickedNum)];
		comboList = rec(0, 2, 0, indexArr, numbArr, stbuild, comboList, "Line");
		PrintWriter out = getResponse().getWriter();
		logger.debug("lines******" + comboList.size());
		out.print(comboList.size());
	}*/

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

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoTwoPurchaseBean;
	}

	public void purchaseTicketProcess() throws Exception {
		servletContext = ServletActionContext.getServletContext();
		try {
			setGameId(Util.getGameId("KenoTwo"));
			String isDraw = (String) servletContext.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				setResponseDataForIfDrawGameNotAvailable();
				return;
			}
			userBean = extractingUserInfoBeanFromSession(servletContext);

			int serviceId = Util.getServiceIdFormCode(getRequest().getAttribute("code").toString());
			validatingServiceIdAndUserMappingIdShouldNotZero(serviceId);

			drawGamePurchaseBean = new KenoPurchaseBean();
			prepareDrawGamePurchaseDataBean();
			prepareDataInDrawGamePurchaseBean(serviceId);

			TransactionManager.setResponseData("true");
			setKenoPurchaseBean(helper.commonPurchseProcessKenoTwo(userBean, drawGamePurchaseBean));
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
		} catch (LMSException e) {
			setResponseDataToOutputStream(ERROR_TRY_AGAIN);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			setResponseDataToOutputStream(ERROR_TRY_AGAIN);
			return;
		}
	}

	private void prepareDrawGamePurchaseDataBean() {
		prepareDataInDrawGamePurchaseBeanFromUtilClass();
		prepareDrawGamePurchaseDataBeanAfterSplitRequestDataByNxt();
		prepareDataInDrawGamePurchaseBeanFromUserBean();
		prepareDataInDrawGamePurchaseBeanFromServletContextClass();
	}

	private UserInfoBean extractingUserInfoBeanFromSession(ServletContext servletContext) {
		Map currentUserSessionMap = (Map) servletContext.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(getUserName());
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		return userBean;
	}

	private void prepareDataInDrawGamePurchaseBeanFromServletContextClass() {
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) servletContext
				.getAttribute("drawIdTableMap");
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setBarcodeType((String) LMSUtility.sc.getAttribute("BARCODE_TYPE"));
		String refMerchantId = (String) servletContext.getAttribute("REF_MERCHANT_ID");
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
	}

	private void prepareDataInDrawGamePurchaseBean(int serviceId) {
		drawGamePurchaseBean.setIsAdvancedPlay(getIsAdvancedPlay());
		drawGamePurchaseBean.setNoOfDraws(getNoOfDraws());
		drawGamePurchaseBean.setPlrMobileNumber(getPlrMobileNumber());
		drawGamePurchaseBean.setServiceId(serviceId);
		drawGamePurchaseBean.setBonus("N");
		drawGamePurchaseBean.setPurchaseChannel(LMS_TERMINAL);
		drawGamePurchaseBean.setDeviceType(DEVICE_TYPE);
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(getDrawIdArr()));
		}
		drawGamePurchaseBean.setGameId(gameId);
		drawGamePurchaseBean.setActionName(ActionContext.getContext().getName());
	}

	private void prepareDrawGamePurchaseDataBeanAfterSplitRequestDataByNxt() {
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
		drawGamePurchaseBean.setNoOfPanel(noOfPanel);
		drawGamePurchaseBean.setBetAmountMultiple(betAmtArr);
		drawGamePurchaseBean.setIsQuickPick(QPArr);
		drawGamePurchaseBean.setQPPreGenerated(qpPreGenerated);
		drawGamePurchaseBean.setNoPicked(noPickedArr);
		drawGamePurchaseBean.setPlayerData(pickedNumbersArr);
		drawGamePurchaseBean.setPlayType(playTypeArr);
	}

	private void validatingServiceIdAndUserMappingIdShouldNotZero(int serviceId) throws LMSException {
		if (serviceId == 0 || userBean.getCurrentUserMappingId() == 0) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	private void setResponseDataToOutputStream(String responseData) throws IOException {
		System.out.println("FINAL PURCHASE DATA KENOTWO:" + responseData);
		getResponse().getOutputStream().write(responseData.getBytes());
	}

	private void prepareDataInDrawGamePurchaseBeanFromUtilClass() {
		long lastPrintedTicket = 0;
		int lstGameId = 0;
		if (LSTktNo != 0) {
			lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			lastPrintedTicket = LSTktNo / Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
		}
		drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
		drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
		drawGamePurchaseBean.setLastSoldTicketNo(lastPrintedTicket + "");
		drawGamePurchaseBean.setLastGameId(lstGameId);
	}

	private void prepareDataInDrawGamePurchaseBeanFromUserBean() {
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
	}

	private String prepeareFinalResponseData() {
		StringBuilder finalData = new StringBuilder("");
		String time = kenoTwoPurchaseBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");
		finalData.append("TicketNo:" + kenoTwoPurchaseBean.getTicket_no() + kenoTwoPurchaseBean.getReprintCount()
				+ "|brCd:" + kenoTwoPurchaseBean.getTicket_no() + kenoTwoPurchaseBean.getReprintCount()
				+ ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB
						&& LMSFilterDispatcher.isBarCodeRequired) ? kenoTwoPurchaseBean.getBarcodeCount() : "")
				+ "|Date:" + time);

		int noOfPanels = kenoTwoPurchaseBean.getNoOfPanel();
		String[] playTypeStr = kenoTwoPurchaseBean.getPlayType();
		for (int i = 0; i < noOfPanels; i++) {
			double panelPri = kenoTwoPurchaseBean.getBetAmountMultiple()[i] * kenoTwoPurchaseBean.getUnitPrice()[i]
					* kenoTwoPurchaseBean.getNoOfLines()[i] * kenoTwoPurchaseBean.getNoOfDraws();
			String panelPrice = "|PanelPrice:" + (new DecimalFormat("#.##").format(panelPri));
			if ("Banker".equalsIgnoreCase(playTypeStr[i])) {
				// logger.debug("---------------BANKER---------------");

				String playerData = kenoTwoPurchaseBean.getPlayerData()[i];
				String[] banker = playerData.replace(",BL", "").split(",UL,");
				finalData.append("|PlayType:" + playTypeStr[i] + "|UL:" + banker[0] + "|BL:" + banker[1] + panelPrice
						+ "|QP:" + kenoTwoPurchaseBean.getIsQuickPick()[i]);
			} else {

				// logger.debug("--------------OTHERS--------------");
				finalData.append("|PlayType:" + playTypeStr[i] + "|Num:" + kenoTwoPurchaseBean.getPlayerData()[i]
						+ panelPrice + "|QP:" + kenoTwoPurchaseBean.getIsQuickPick()[i]);

			}
		}
		String promoTicketDta = settingPromoTicketData(userBean, kenoTwoPurchaseBean);
		String advtMsg = settingAdvtMessage(kenoTwoPurchaseBean);
		StringBuilder drawTimeBuild = settingDrawDateTime();
		String balance = settingFormatedPlayerBalance(userBean);
		finalData.append("|TicketCost:" + kenoTwoPurchaseBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance + "|" + advtMsg + promoTicketDta);
		return finalData.toString();
	}

	private void setResponseDataIfSaleStatusNotSuccess() throws IOException {
		String finalPurchaseData;
		finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(getKenoPurchaseBean().getSaleStatus()) + "|";
		this.finalPurchaseData = finalPurchaseData;
		setResponseDataToOutputStream(finalPurchaseData);
	}

	private String settingFormatedPlayerBalance(UserInfoBean userBean) {
		double balance = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		NumberFormat numberfFormat = NumberFormat.getInstance();
		numberfFormat.setMinimumFractionDigits(2);
		String formatedBalance = numberfFormat.format(balance).replaceAll(",", "");
		return formatedBalance;
	}

	private StringBuilder settingDrawDateTime() {
		int listSize = kenoTwoPurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + kenoTwoPurchaseBean.getDrawDateTime().get(i)).replaceFirst(" ", "#")
					.replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
		}
		return drawTimeBuild;
	}

	private void sendSmsToPlayerIfSaleStatusSuucess() {
		if (isSaleStatusSuccess()) {
			String smsData = com.skilrock.lms.common.utility.CommonMethods.prepareSMSData(
					kenoTwoPurchaseBean.getPlayerData(), kenoTwoPurchaseBean.getPlayType(),
					new StringBuilder(String.valueOf(kenoTwoPurchaseBean.getTotalPurchaseAmt())),
					new StringBuilder(kenoTwoPurchaseBean.getTicket_no() + kenoTwoPurchaseBean.getReprintCount()),
					kenoTwoPurchaseBean.getDrawDateTime());
			com.skilrock.lms.common.utility.CommonMethods.sendSMS(smsData);
		}
	}

	private boolean isSaleStatusSuccess() {
		return "SUCCESS".equalsIgnoreCase(kenoTwoPurchaseBean.getSaleStatus());
	}

	private String settingPromoTicketData(UserInfoBean userBean, KenoPurchaseBean kenoTwoPurchaseBean) {
		String promoTicketData = "";
		Object promoPurchaseBeaan = kenoTwoPurchaseBean.getPromoPurchaseBean();
		if (promoPurchaseBeaan instanceof FortunePurchaseBean) {
			promoTicketData = buildSaleDataforWinfast((FortunePurchaseBean) promoPurchaseBeaan, userBean);
		}
		if (promoPurchaseBeaan instanceof List) {
			promoTicketData = CommonMethods.buildRaffleData((List<RafflePurchaseBean>) promoPurchaseBeaan);
		}
		return promoTicketData;
	}

	private String settingAdvtMessage(KenoPurchaseBean kenoTwoPurchaseBean) {
		String advtMsg = "";
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		UtilApplet.getAdvMsgs(kenoTwoPurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);
		advtMsg = topMsgsStr.length() != 0 ? "topAdvMsg:" + topMsgsStr + "|" : "";
		advtMsg = bottomMsgsStr.length() != 0 ? advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|" : "";
		return advtMsg;
	}

	private void setResponseDataForIfDrawGameNotAvailable() throws IOException {
		this.finalPurchaseData = "ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE;
		response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE).getBytes());
	}

	public String buildSaleDataforWinfast(FortunePurchaseBean fortuneBean, UserInfoBean userBean) {

		String time = fortuneBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < fortuneBean.getPickedNumbers().size(); i++) {
			stBuilder.append(numbers.get(fortuneBean.getPickedNumbers().get(i)) + ":"
					+ fortuneBean.getBetAmountMultiple()[i] + "|");
		}
		int listSize = fortuneBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(
					("|DrawDate:" + fortuneBean.getDrawDateTime().get(i)).replace(" ", "|DrawTime:").replace(".0", ""));
		}

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);

		List<RafflePurchaseBean> rafflePurchaseBeanList = fortuneBean.getRafflePurchaseBeanList();
		String raffleData = CommonMethods.buildRaffleData(rafflePurchaseBeanList);

		String finalData = "|PromoTkt:" + "TicketNo:" + fortuneBean.getTicket_no() + fortuneBean.getReprintCount()
				+ "|Date:" + time + "|" + stBuilder.toString() + "TicketCost:" + fortuneBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() + "|balance:" + balance + "|QP:" + fortuneBean.getIsQP() + "|" + raffleData;

		return finalData;
	}

	//Code commented during refactoring
	/*public static List<String> rec(int start, int elementToChoose, int returnCnt, String[] indexArr, String[] elements,
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

			rec(++start, elementToChoose, returnCnt, indexArr, elements, stbuff, comboList, qp);
		}
		return comboList;
	}
*/
}