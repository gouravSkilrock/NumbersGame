package com.skilrock.lms.embedded.drawGames.playMgmt;

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

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class LottoAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	static Log logger = LogFactory.getLog(LottoAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] drawIdArr;
	private int gameNumber = Util.getGameId("Lotto");
	private int isAdvancedPlay;
	private LottoPurchaseBean lottoPurchaseBean;
	private int noOfDraws;
	private String pickedNumbers;
	private HttpServletRequest request;

	private HttpServletResponse response;
	private String totalPurchaseAmt;
	private String userName;
	
	private String LSTktNo;
	private String plrMobileNumber;
	
	
	
	
	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public String getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(String lSTktNo) {
		LSTktNo = lSTktNo;
	}
	
	public String[] getDrawIdArr() {
		return drawIdArr;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public LottoPurchaseBean getLottoPurchaseBean() {
		return lottoPurchaseBean;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
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

		List<String> playerPicked = new ArrayList<String>();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		/*
		 * String startRange=(String)session.getAttribute("START_RANGE"); String
		 * endRange=(String)session.getAttribute("END_RANGE"); String
		 * winNum=(String)session.getAttribute("WIN_NUMBERS");
		 */
		String purchaseChannel = "LMS_Terminal";
		String[] picknumbers = pickedNumbers.split("Nxt");
		//logger.debug("pick numbers*******" + pickedNumbers);

		LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
		// lottoPurchaseBean.setBetAmountMultiple(betAmountMultiples);
		lottoPurchaseBean.setPlrMobileNumber(plrMobileNumber);
		lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		lottoPurchaseBean.setGame_no(gameNumber);
		lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameNumber));
		// lottoPurchaseBean.setIsQuickPick(isQuickPick);
		lottoPurchaseBean.setNoOfDraws(noOfDraws);
		lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
		lottoPurchaseBean.setPartyType(userBean.getUserType());
		lottoPurchaseBean.setUserId(userBean.getUserId());
		lottoPurchaseBean.setRefMerchantId(refMerchantId);
		lottoPurchaseBean.setPurchaseChannel(purchaseChannel);
		lottoPurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		if (drawIdArr != null) {
			lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		// lottoPurchaseBean.setPanel_id(panel_id);
		lottoPurchaseBean.setPlayerPicked(playerPicked);
		// lottoPurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));

		lottoPurchaseBean.setPicknumbers(picknumbers);
		String finalPurchaseData = null;
		
		String lastSoldTicketNo = "0";
		if(!"0".equals(LSTktNo) && LSTktNo!=null){
			lastSoldTicketNo = LSTktNo.substring(0, LSTktNo.length()-2);
		}
		
		lottoPurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		String lastSoldTktLMS = helper.getLastSoldTicketNO(userBean,"TERMINAL");
		if(lastSoldTktLMS != lastSoldTicketNo && lastSoldTktLMS != null && !"0".equals(LSTktNo) && LSTktNo!=null){
			CancelTicketBean cancelTicketBean = new CancelTicketBean();
			cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
			cancelTicketBean.setTicketNo(lastSoldTktLMS + "00");
			cancelTicketBean.setPartyId(userBean.getUserOrgId());
			cancelTicketBean.setPartyType(userBean.getUserType());
			cancelTicketBean.setUserId(userBean.getUserId());
			cancelTicketBean.setCancelChannel("LMS_Terminal");
			cancelTicketBean.setRefMerchantId(refMerchantId);
			cancelTicketBean.setAutoCancel(true);
			cancelTicketBean = helper.cancelTicket(cancelTicketBean,
					userBean, true, "CANCEL_MISMATCH");
		}
		
		if (picknumbers.length < 1) {
			lottoPurchaseBean.setSaleStatus("ERROR");
			// return SUCCESS;
			finalPurchaseData = "ErrorMsg:"
					+ EmbeddedErrors.PURCHSE_INVALID_DATA;
			System.out.println("FINAL PURCHASE DATA LOTTO:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}
		for (int i = 0; i < picknumbers.length; i++) {
			if (!"QP".equals(picknumbers[i])) {
				if (!Util.validateNumber(
						ConfigurationVariables.LOTTO_START_RANGE,
						ConfigurationVariables.LOTTO_END_RANGE, picknumbers[i],
						false)) {
					lottoPurchaseBean.setSaleStatus("ERROR");
					// return SUCCESS;

					finalPurchaseData = "ErrorMsg:"
							+ EmbeddedErrors.PURCHSE_INVALID_DATA;
					System.out.println("FINAL PURCHASE DATA LOTTO:"
							+ finalPurchaseData);
					response.getOutputStream().write(
							finalPurchaseData.getBytes());
					return;
				}
			}
		}

		setLottoPurchaseBean(helper.lottoPurchaseTicket(userBean,
				lottoPurchaseBean));
		String saleStatus = getLottoPurchaseBean().getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)
					+ "|";
			System.out.println("FINAL PURCHASE DATA LOTTO:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}
		String time = lottoPurchaseBean.getPurchaseTime()
				.replace(" ", "|Time:").replace(".0", "");

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);
		int listSize = lottoPurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + lottoPurchaseBean
					.getDrawDateTime().get(i)).replace(" ", "|DrawTime:")
					.replace(".0", ""));
		}
		StringBuilder stBuilder = new StringBuilder("");
		for (int i = 0; i < lottoPurchaseBean.getPlayerPicked().size(); i++) {
			stBuilder.append("|Num:"
					+ lottoPurchaseBean.getPlayerPicked().get(i));
		}

		//logger.debug("+++++++++++++++" + stBuilder.toString());

		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(lottoPurchaseBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}

		finalPurchaseData = "TicketNo:" + lottoPurchaseBean.getTicket_no()
				+ lottoPurchaseBean.getReprintCount() + "|Date:" + time + ""
				+ stBuilder.toString() + "|TicketCost:"
				+ lottoPurchaseBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() + "|balance:" + balance + "|QP:"
				+ lottoPurchaseBean.getIsQuickPick()[0] + "|" + advtMsg;

		System.out.println("FINAL PURCHASE DATA LOTTO:" + finalPurchaseData);
		response.getOutputStream().write(finalPurchaseData.getBytes());
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setLottoPurchaseBean(LottoPurchaseBean lottoPurchaseBean) {
		this.lottoPurchaseBean = lottoPurchaseBean;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
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

}
