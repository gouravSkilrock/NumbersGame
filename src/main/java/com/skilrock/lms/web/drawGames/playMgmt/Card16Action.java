package com.skilrock.lms.web.drawGames.playMgmt;

import java.util.Arrays;
import java.util.Date;
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

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class Card16Action extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final List sunSign = Arrays.asList("", "ace_spade",
			"ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart",
			"king_diamond", "king_club", "queen_spade", "queen_heart",
			"queen_diamond", "queen_club", "jack_spade", "jack_heart",
			"jack_diamond", "jack_club");
	private String[] drawIdArr;

	private String errMsg;
	private FortunePurchaseBean fortuneBean;
	private int gameNumber = Util.getGameId("Card16");
	private int isAdvancedPlay;
	private int isQuickPick;
	Log logger = LogFactory.getLog(Card16Action.class);
	private int noOfDraws;
	private String noOfPanels;
	private HttpServletRequest request;
	private HttpServletResponse response;
	HttpSession session = null;
	private String symbols;
	private int totalNoOfPanels;

	private String totalPurchaseAmt;
	private String plrMobileNumber;

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public String[] getDrawIdArr() {
		return drawIdArr;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public FortunePurchaseBean getFortuneBean() {
		return fortuneBean;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public int getIsQuickPick() {
		return isQuickPick;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public String getNoOfPanels() {
		return noOfPanels;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getSymbols() {
		return symbols;
	}

	public int getTotalNoOfPanels() {
		return totalNoOfPanels;
	}

	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public String purchaseTicketProcess() throws Exception {
		logger.debug("Inside purchaseTicketProcess");
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String purchaseChannel = "LMS_Web";
		logger.debug("amount***in aciton****" + totalPurchaseAmt);
		/*
		 * int[] panel_id = null; int[] betAmountMultiple =null; Integer[]
		 * isQuickPickNew = null; List<Integer> playerPicked = new ArrayList<Integer>();
		 * if (isQuickPick==1) { int index = 0; Map<Integer,Integer> qpData =
		 * new HashMap<Integer,Integer>(); for (int i = 0; i < totalNoOfPanels;
		 * i++) { index = Util.getRandomNo(1, sunSign.size()-1);
		 * qpData.put(index, qpData.get(index)==null?1:qpData.get(index)+1); }
		 * panel_id = new int[qpData.size()]; betAmountMultiple = new
		 * int[qpData.size()]; isQuickPickNew = new Integer[qpData.size()];
		 * 
		 * logger.debug(qpData+"Fortune---------"); Iterator<Integer> iter =
		 * qpData.keySet().iterator(); int i=0;int value =0; while
		 * (iter.hasNext()) { value = iter.next(); panel_id[i] = i + 1;
		 * betAmountMultiple[i] = qpData.get(value); isQuickPickNew[i] = 1;
		 * playerPicked.add(value); i++; } } else{ String[] noOfPanel =
		 * noOfPanels.split(","); String []pickedNumber = symbols.split(",");
		 * panel_id = new int[noOfPanel.length]; betAmountMultiple = new
		 * int[noOfPanel.length]; isQuickPickNew = new
		 * Integer[noOfPanel.length]; for (int i = 0; i < noOfPanel.length; i++) {
		 * playerPicked.add(sunSign.indexOf(pickedNumber[i])); panel_id[i] = i +
		 * 1; betAmountMultiple[i] = Integer.parseInt(noOfPanel[i]);
		 * isQuickPickNew[i] = isQuickPick; } } Collections.sort(playerPicked);
		 */
		ServletContext sc = ServletActionContext.getServletContext();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		logger.debug("merchant id***" + refMerchantId);
		// logger.debug("merchant id***" + refMerchantId);
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		logger.debug("map************" + drawIdTableMap);
		// logger.debug("map************" + drawIdTableMap);
		FortunePurchaseBean drawGamePurchaseBean = new FortunePurchaseBean();

		drawGamePurchaseBean.setIsQP(isQuickPick);
		drawGamePurchaseBean.setTotalNoOfPanels(totalNoOfPanels);
		drawGamePurchaseBean.setSymbols(symbols);
		drawGamePurchaseBean.setNoOfPanels(noOfPanels);

		// drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setGame_no(gameNumber);
		drawGamePurchaseBean.setGameDispName(Util
				.getGameDisplayName(gameNumber));
		// drawGamePurchaseBean.setIsQuickPick(isQuickPickNew);
		drawGamePurchaseBean.setNoOfDraws(noOfDraws);
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		// drawGamePurchaseBean.setPanel_id(panel_id);
		// drawGamePurchaseBean.setPickedNumbers(playerPicked);
		// drawGamePurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));

		if (totalNoOfPanels < 1) {
			drawGamePurchaseBean.setSaleStatus("ERROR");
			setFortuneBean(drawGamePurchaseBean);
			return SUCCESS;
		}

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		FortunePurchaseBean fortuneBean = helper.card16PurchaseTicket(userBean,
				drawGamePurchaseBean);
		logger.debug("amount*******" + fortuneBean.getTotalPurchaseAmt());
		// logger.debug("amount*******" +
		// fortuneBean.getTotalPurchaseAmt());
		setFortuneBean(fortuneBean);
		if (!"SUCCESS".equalsIgnoreCase(fortuneBean.getSaleStatus())) {
			setErrMsg(DGErrorMsg.buyErrMsg(fortuneBean.getSaleStatus()));
			return ERROR;
		}
		return SUCCESS;

	}

	public String reprintTicket() throws Exception {
		logger.debug("Inside reprintTicket");
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		logger.debug("Before--" + new Date());
		// logger.debug("Before--" + new Date());
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		setFortuneBean((FortunePurchaseBean) helper.reprintTicket(userInfoBean));

		return SUCCESS;
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setFortuneBean(FortunePurchaseBean fortuneBean) {
		this.fortuneBean = fortuneBean;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setIsQuickPick(int isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setNoOfPanels(String noOfPanels) {
		this.noOfPanels = noOfPanels;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

	public void setTotalNoOfPanels(int totalNoOfPanels) {
		this.totalNoOfPanels = totalNoOfPanels;
	}

	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

}
