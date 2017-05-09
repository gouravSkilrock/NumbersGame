package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class SuperTwoAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	static Log logger = LogFactory.getLog(SuperTwoAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final List numbers = Arrays.asList("", "Zero(0)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
			"Eight(8)", "Nine(9)");

	public static void main(String[] args) throws Exception {
		new SuperTwoAction().purchaseTicketProcess();
	}

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

	private String betAmt;
	private String[] drawIdArr;
	private int gameId = Util.getGameId("SuperTwo");
	private int isAdvancedPlay;
	private KenoPurchaseBean superTwoPurchaseBean;
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

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
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

	public KenoPurchaseBean getKenoPurchaseBean() {
		return superTwoPurchaseBean;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public void getLines() throws IOException {
		String[] indexArr = new String[2];
		StringBuffer stbuild = null;
		List<String> comboList = new ArrayList<String>();
		String[] numbArr = new String[Integer.parseInt(pickedNum)];
		comboList = rec(0, 2, 0, indexArr, numbArr, stbuild, comboList, "Line");
		PrintWriter out = getResponse().getWriter();
		logger.debug("lines******" + comboList.size());
		out.print(comboList.size());
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

	public void purchaseTicketProcess() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE)
							.getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		List<String> playerPicked = new ArrayList<String>();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
		String purchaseChannel = "LMS_Terminal";

		String[] betAmtStr = betAmt.split("Nxt");
		String[] QPStr = QP.split("Nxt");
		String[] pickedNumbersArr = pickedNumbers.split("Nxt");
		String[] noPickedArr = noPicked.replaceAll(" ", "").split("Nxt");
		String[] playTypeArr = playType.split("Nxt");
		int noOfPanel = pickedNumbersArr.length;
		int[] betAmtArr = new int[noOfPanel];
		int[] QPArr = new int[noOfPanel];
		for (int i = 0; i < noOfPanel; i++) {
			betAmtArr[i] = Integer.parseInt(betAmtStr[i]);
			QPArr[i] = Integer.parseInt(QPStr[i]);
		}

		KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
		drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
		drawGamePurchaseBean.setGameId(gameId);
		drawGamePurchaseBean.setGameDispName(Util
				.getGameDisplayName(gameId));
		drawGamePurchaseBean.setBetAmountMultiple(betAmtArr);
		drawGamePurchaseBean.setIsQuickPick(QPArr);
		drawGamePurchaseBean.setPlayerData(pickedNumbersArr);
		drawGamePurchaseBean.setNoPicked(noPickedArr);
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setNoOfDraws(noOfDraws);
		drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setBonus("N");
		drawGamePurchaseBean.setPlayType(playTypeArr);
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setNoOfPanel(noOfPanel);
		String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
		drawGamePurchaseBean.setBarcodeType(barcodeType);

		long lastPrintedTicket=0;
		int gameId = 0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}

		// super two validation

		/*
		 * boolean isValid = true; for (int i = 0; i < noOfPanel; i++){ String
		 * playerData = pickedNumbersArr[i]; if (!"QP".equals(playerData)) {
		 * isValid = Util .validateNumber(SuperTwoConstants.START_RANGE,
		 * SuperTwoConstants.END_RANGE, playerData, false);
		 * logger.debug("-Data---" + playTypeArr[i] + "---" + noPickedArr[i] +
		 * "---" + playerData); if (!isValid) { break; } } } if(!isValid){
		 * response.getOutputStream().write("INVALID_INPUT".getBytes()); return;
		 * }
		 */
		drawGamePurchaseBean.setLastSoldTicketNo(lastPrintedTicket+"");
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		String actionName=ActionContext.getContext().getName();
		helper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);

		superTwoPurchaseBean = helper.commonPurchseProcessSuperTwo(userBean,
				drawGamePurchaseBean);
		setKenoPurchaseBean(superTwoPurchaseBean);
		String saleStatus = getKenoPurchaseBean().getSaleStatus();
		String finalPurchaseData = null;

		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)
					+ "|";
			System.out.println("FINAL PURCHASE DATA SUPERTWO:"
					+ finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}

		/*
		 * logger.debug(kenoTwoPurchaseBean.getPlayerPicked() + "msg---------" +
		 * kenoTwoPurchaseBean.getTicket_no());
		 */

		String time = superTwoPurchaseBean.getPurchaseTime().replace(" ",
				"|Time:").replace(".0", "");

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		
		String balance = nf.format(bal).replaceAll(",", "");
		//String balance = bal + "00";
		//balance = balance.substring(0, balance.indexOf(".") + 3);
		int listSize = superTwoPurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + superTwoPurchaseBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace(
					"#", "|DrawTime:").replace(".0", ""));
		}

		StringBuilder finalData = new StringBuilder("");
		// helper.insertLastSoldTicket(userBean.getUserOrgId(), superTwoPurchaseBean.getTicket_no());
		finalData.append("TicketNo:" + superTwoPurchaseBean.getTicket_no()
				+ superTwoPurchaseBean.getReprintCount() +((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenA)?"":superTwoPurchaseBean.getBarcodeCount())+ "|Date:" + time);

		int noOfPanels = superTwoPurchaseBean.getNoOfPanel();
		String[] playTypeStr = superTwoPurchaseBean.getPlayType();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(1);
		for (int i = 0; i < noOfPanels; i++) {
			double panelPri = superTwoPurchaseBean.getBetAmountMultiple()[i]
					* superTwoPurchaseBean.getNoOfLines()[i]
					* superTwoPurchaseBean.getUnitPrice()[i]
					* superTwoPurchaseBean.getNoOfDraws();
			String panelPrice = "|PanelPrice:"
					+ (nFormat.format(panelPri).replaceAll(",", ""));
			// logger.debug("--------------OTHERS--------------");
			if (playTypeStr[i].contains("Banker")) {
				finalData.append("|PlayType:"
						+ playTypeStr[i]
						+ "|Num:"
						+ superTwoPurchaseBean.getPlayerData()[i].replace("-1",
								"XX") + panelPrice + "|QP:"
						+ superTwoPurchaseBean.getIsQuickPick()[i]);
			} else {
				finalData.append("|PlayType:" + playTypeStr[i] + "|Num:"
						+ superTwoPurchaseBean.getPlayerData()[i] + panelPrice
						+ "|QP:" + superTwoPurchaseBean.getIsQuickPick()[i]);
			}

		}

		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(superTwoPurchaseBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}

		// here build the data of promo ticket
		String promoTicketDta = "";
		Object promoPurchaseBeaan = superTwoPurchaseBean.getPromoPurchaseBean();
		if (promoPurchaseBeaan instanceof FortunePurchaseBean) {
			promoTicketDta = buildSaleDataforWinfast(
					(FortunePurchaseBean) promoPurchaseBeaan, userBean);
		}
		if (promoPurchaseBeaan instanceof List) {
			promoTicketDta = CommonMethods
					.buildRaffleData((List<RafflePurchaseBean>) promoPurchaseBeaan);
		}

		finalData.append("|TicketCost:"
				+ superTwoPurchaseBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() + "|balance:" + balance + "|"
				+ advtMsg + promoTicketDta);

		finalPurchaseData = finalData.toString();
		logger.info("FINAL PURCHASE DATA SUPERTWO:" + finalPurchaseData);
		
		request.setAttribute("purchaseData", finalPurchaseData);
			
		
		response.getOutputStream().write(finalPurchaseData.getBytes());
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
					.get(i)).replace(" ", "|DrawTime:").replace(".0", ""));
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

	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	
	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean superTwoPurchaseBean) {
		this.superTwoPurchaseBean = superTwoPurchaseBean;
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

	public int getGameId() {
		return gameId;
	}

}
