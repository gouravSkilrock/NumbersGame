package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.ZeroToNineHelper;
import com.skilrock.lms.dge.beans.ZeroToNinePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class ZeroToNineAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private int gameId = Util.getGameId("Zerotonine");
	private static final List<String> NUMBERS = Arrays.asList("", "Zero(0)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
			"Eight(8)", "Nine(9)");
	
	private String noOfPanels;
	private int isQuickPick;
	private String symbols;
	private int noOfDraws;
	private String userName;
	private int totalNoOfPanels;
	private double totalPurchaseAmt;
	private long LSTktNo;

	public ZeroToNineAction() {
		super(ZeroToNineAction.class);
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

	public String getNoOfPanels() {
		return noOfPanels;
	}

	public void setNoOfPanels(String noOfPanels) {
		this.noOfPanels = noOfPanels;
	}

	public int getIsQuickPick() {
		return isQuickPick;
	}

	public void setIsQuickPick(int isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public String getSymbols() {
		return symbols;
	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getTotalNoOfPanels() {
		return totalNoOfPanels;
	}

	public void setTotalNoOfPanels(int totalNoOfPanels) {
		this.totalNoOfPanels = totalNoOfPanels;
	}

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	@SuppressWarnings("unchecked")
	public void purchaseTicketProcess() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|").getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");

		String[] betAmtData = noOfPanels.split(",");

		int isQP = isQuickPick;

		int totalNoOfPanels = 0;
		if(isQP == 0)
			totalNoOfPanels = betAmtData.length;
		else
			totalNoOfPanels = Integer.parseInt(noOfPanels);

		String noPicked[] = new String[totalNoOfPanels];
		int[] isQuickPick = new int[totalNoOfPanels];
		String[] playType = new String[totalNoOfPanels];
		String pickedData[] = new String[totalNoOfPanels];
		int[] betAmountMultiple = new int[totalNoOfPanels];
		if(isQP == 0) {
			String[] pickedSymbols = symbols.split(",");
			for (int i = 0; i < totalNoOfPanels; i++) {
				noPicked[i] = "1";					
				isQuickPick[i] = 2;
				playType[i] = "zeroToNine";
				//pickedData[i] = pickedSymbols[i];
				pickedData[i] = String.valueOf(NUMBERS.indexOf(pickedSymbols[i]) - 1);
				betAmountMultiple[i] = Integer.parseInt(betAmtData[i]);
			}
		} else {
			for (int i = 0; i < totalNoOfPanels; i++) {
				noPicked[i] = "1";					
				isQuickPick[i] = 2;
				playType[i] = "zeroToNine";
				//pickedData[i] = pickedSymbols[i];
				pickedData[i] = "QP";
				betAmountMultiple[i] = 1;
			}
		}

		long lastPrintedTicket=0;
		int lstGameId =0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}

		ZeroToNinePurchaseBean drawGamePurchaseBean = new ZeroToNinePurchaseBean();
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setIsQuickPick(isQuickPick);
		//drawGamePurchaseBean.setIsQP(2);
		drawGamePurchaseBean.setIsQP(isQP);
		drawGamePurchaseBean.setPlayerData(pickedData);
		drawGamePurchaseBean.setNoPicked(noPicked);
		drawGamePurchaseBean.setPlayType(playType);
		drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
		drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
		drawGamePurchaseBean.setGameId(gameId);
		drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
		drawGamePurchaseBean.setNoOfDraws(noOfDraws);
		drawGamePurchaseBean.setIsAdvancedPlay(0);
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel("LMS_Terminal");
		drawGamePurchaseBean.setTotalNoOfPanels(totalNoOfPanels);
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
		drawGamePurchaseBean.setServiceId(Util.getServiceIdFormCode(request.getAttribute("code").toString()));
		drawGamePurchaseBean.setBarcodeType((String) LMSUtility.sc.getAttribute("BARCODE_TYPE"));

		String actionName=ActionContext.getContext().getName();
		drawGamePurchaseBean.setLastSoldTicketNo(String.valueOf(lastPrintedTicket));
		drawGamePurchaseBean.setActionName(actionName);
		drawGamePurchaseBean.setLastGameId(lstGameId);
		drawGamePurchaseBean.setDeviceType("TERMINAL");
		
		TransactionManager.setResponseData("true");

		ZeroToNineHelper helper = ZeroToNineHelper.getInstance();
		drawGamePurchaseBean = helper.zeroToNinePurchaseTicket(userBean, drawGamePurchaseBean);

		String saleStatus = drawGamePurchaseBean.getSaleStatus();
		String finalPurchaseData = null;
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)+ "|" + "ErrorCode:" + DGErrorMsg.buyErrorCode(saleStatus) + "|";
			logger.info("FINAL PURCHASE DATA ZEROTONINE:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}
		String time = drawGamePurchaseBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < drawGamePurchaseBean.getPickedNumbers().size(); i++) {
			stBuilder.append(NUMBERS.get(drawGamePurchaseBean.getPickedNumbers().get(i) + 1)+ ":"+ drawGamePurchaseBean.getBetAmountMultiple()[i]+ "|");
		}

		int listSize = drawGamePurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + drawGamePurchaseBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace(
					"#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
		}

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		String bal = FormatNumber.formatNumber(userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal());
		String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);

		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(drawGamePurchaseBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}

		finalPurchaseData = "TicketNo:" + drawGamePurchaseBean.getTicket_no() + drawGamePurchaseBean.getReprintCount()
				+ "|brCd:"+drawGamePurchaseBean.getTicket_no() + drawGamePurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? drawGamePurchaseBean.getBarcodeCount():"")
				+ "|Date:" + time + "|"
				+ stBuilder.toString() + "TicketCost:"
				+ drawGamePurchaseBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance + "|QP:" + isQP +"|ExpiryTime:"+drawGamePurchaseBean.getClaimEndTime().replace(".0","")+ "|"
				+ advtMsg;
		System.out.println("FINAL PURCHASE DATA ZEROTONINE:" + finalPurchaseData);
		if("SUCCESS".equalsIgnoreCase(drawGamePurchaseBean.getSaleStatus())){
			String smsData = com.skilrock.lms.common.utility.CommonMethods.prepareSMSData(drawGamePurchaseBean.getPickedNumbers().toArray(new String[drawGamePurchaseBean.getPickedNumbers().size()]), drawGamePurchaseBean.getPlayType(), new StringBuilder(String.valueOf(drawGamePurchaseBean.getTotalPurchaseAmt())), new StringBuilder(drawGamePurchaseBean.getTicket_no()+drawGamePurchaseBean.getReprintCount()), Arrays.asList(time));
			com.skilrock.lms.common.utility.CommonMethods.sendSMS(smsData);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getOutputStream().write(finalPurchaseData.getBytes());
	}
}