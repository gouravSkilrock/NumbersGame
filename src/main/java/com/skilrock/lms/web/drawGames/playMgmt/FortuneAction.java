package com.skilrock.lms.web.drawGames.playMgmt;

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

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class FortuneAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] drawIdArr;
	private String errMsg;

	private FortunePurchaseBean fortuneBean;
	private int gameNumber = Util.getGameId("Fortune");
	private int isAdvancedPlay;
	private int isQuickPick;
	Log logger = LogFactory.getLog(FortuneAction.class);
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

	// public static final List sunSign =
	// Arrays.asList("","Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Sagittarius","Capricorn","Aquarius","Pisces"
	// );
	public String purchaseTicketProcess() throws Exception {
		logger.debug("Inside purchaseTicketProcess");
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String purchaseChannel = "LMS_Web";

		// logger.debug("----- ENTERING INTO FORTUNE PURCHSE
		// TICKET------");

		ServletContext sc = ServletActionContext.getServletContext();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		logger.debug("merchant id***" + refMerchantId);
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		logger.debug("Draw id Map....***" + drawIdTableMap);
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
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);

		// drawGamePurchaseBean.setPanel_id(panel_id);
		// drawGamePurchaseBean.setPickedNumbers(playerPicked);
		// drawGamePurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));

		if (totalNoOfPanels < 1) {
			drawGamePurchaseBean.setSaleStatus("ERROR");
			setFortuneBean(drawGamePurchaseBean);
			return SUCCESS;
		}

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		FortunePurchaseBean fortuneBean = helper.fortunePurchaseTicket(
				userBean, drawGamePurchaseBean);

		setFortuneBean(fortuneBean);
		if (!"SUCCESS".equalsIgnoreCase(fortuneBean.getSaleStatus())) {
			setErrMsg(DGErrorMsg.buyErrMsg(fortuneBean.getSaleStatus()));
			return ERROR;
		}
		return SUCCESS;

	}

	public String reprintTicket() throws Exception {
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		setFortuneBean((FortunePurchaseBean) helper.reprintTicket(userInfoBean));

		return SUCCESS;
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
