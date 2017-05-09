package com.skilrock.lms.embedded.drawGames.playMgmt;

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

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class BonusBallTwoAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	static Log logger = LogFactory.getLog(BonusBallTwoAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int betAmountMultiple;
	private String[] drawIdArr;
	private int gameNumber = Util.getGameId("BonusBallTwo");
	private int isAdvancedPlay;
	private LottoPurchaseBean lottoPurchaseBean;
	private int noOfDraws;
	private String playType;
	private int noPicked;
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

	public int getBetAmountMultiple() {
		return betAmountMultiple;
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
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		/*
		 * String startRange=(String)session.getAttribute("START_RANGE"); String
		 * endRange=(String)session.getAttribute("END_RANGE"); String
		 * winNum=(String)session.getAttribute("WIN_NUMBERS");
		 */
		String purchaseChannel = "LMS_Terminal";
		String[] picknumbers = pickedNumbers.split("Nxt");

		LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
		lottoPurchaseBean.setBetAmtMultiple(betAmountMultiple);
		lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		lottoPurchaseBean.setGame_no(gameNumber);
		lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameNumber));
		// lottoPurchaseBean.setIsQuickPick(isQuickPick);
		lottoPurchaseBean.setNoOfDraws(noOfDraws);
		lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
		lottoPurchaseBean.setPartyType(userBean.getUserType());
		lottoPurchaseBean.setUserId(userBean.getUserId());
		lottoPurchaseBean.setPlayType(playType);
		lottoPurchaseBean.setNoPicked(noPicked);
		lottoPurchaseBean.setPickedNumbers(pickedNumbers);
		lottoPurchaseBean.setRefMerchantId(refMerchantId);
		lottoPurchaseBean.setPurchaseChannel(purchaseChannel);
		lottoPurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		lottoPurchaseBean.setPlrMobileNumber(plrMobileNumber);
		if (drawIdArr != null) {
			lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		// lottoPurchaseBean.setPanel_id(panel_id);
		lottoPurchaseBean.setPlayerPicked(playerPicked);
		// lottoPurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));

		lottoPurchaseBean.setPicknumbers(picknumbers);
		
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
			cancelTicketBean.setHoldAutoCancel(true);
			cancelTicketBean.setAutoCancelHoldDays(autoCancelHoldDays);
			cancelTicketBean = helper.cancelTicket(cancelTicketBean,
					userBean, true,"CANCEL_MISMATCH");
		}
		
		String finalPurchaseData = null;

		if (picknumbers.length < 1) {
			lottoPurchaseBean.setSaleStatus("ERROR");
			// return SUCCESS;
			finalPurchaseData = "ErrorMsg:"
					+ EmbeddedErrors.PURCHSE_INVALID_DATA;
			System.out.println("FINAL PURCHASE DATA BONUSBALLTWO:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}
		for (int i = 0; i < picknumbers.length; i++) {
			if (!"QP".equals(picknumbers[i])) {
				if (!Util.validateNumber(
						ConfigurationVariables.ZIMLOTTOBONUS_START_RANGE,
						ConfigurationVariables.ZIMLOTTOBONUS_END_RANGE, picknumbers[i],
						false)) {
					lottoPurchaseBean.setSaleStatus("ERROR");
					// return SUCCESS;
					finalPurchaseData = "ErrorMsg:"
							+ EmbeddedErrors.PURCHSE_INVALID_DATA;
					System.out.println("FINAL PURCHASE DATA BONUSBALLTWO:"
							+ finalPurchaseData);
					response.getOutputStream().write(
							finalPurchaseData.getBytes());
					return;
				}
			}
		}

		setLottoPurchaseBean(helper.bonusBallTwoPurchaseTicket(userBean,
				lottoPurchaseBean));

		String saleStatus = getLottoPurchaseBean().getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)
					+ "|";
			System.out.println("FINAL PURCHASE DATA BONUSBALLTWO:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}

		String time = lottoPurchaseBean.getPurchaseTime()
				.replace(" ", "|Time:").replace(".0", "");

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		
		String balance = nf.format(bal).replaceAll(",", "");
		
		/*String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);*/
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
			stBuilder.append("|QP:"+lottoPurchaseBean.getIsQuickPick()[i]);
		}

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
		
		List<RafflePurchaseBean> rafflePurchaseBeanList = lottoPurchaseBean
		.getRafflePurchaseBeanList();
        String raffleData = CommonMethods
		  .buildRaffleData(rafflePurchaseBeanList);

		finalPurchaseData = "TicketNo:" + lottoPurchaseBean.getTicket_no()
				+ lottoPurchaseBean.getReprintCount() +lottoPurchaseBean.getBarcodeCount()+ "|Date:" + time + ""
				+ "|PlayType:" + lottoPurchaseBean.getPlayType()
				+ stBuilder.toString() 
				+ "|unitPrice:" + lottoPurchaseBean.getUnitPrice()*lottoPurchaseBean.getBetAmtMultiple()
				+ "|TicketCost:"
				+ lottoPurchaseBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() + "|balance:" + balance + "|" +  raffleData 
				+ advtMsg;

		System.out.println("FINAL PURCHASE DATA BONUSBALLLOTTO:" + finalPurchaseData);
		response.getOutputStream().write(finalPurchaseData.getBytes());
	}

	public void setBetAmountMultiple(int betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
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

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public int getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(int noPicked) {
		this.noPicked = noPicked;
	}

}
