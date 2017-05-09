package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
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
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.dge.gameconstants.ZimLottoBonusConstants;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class ZimLottoBonusAction extends ActionSupport implements
ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log logger =LogFactory.getLog(ZimLottoBonusAction.class);
	private int betAmountMultiple;
	private String[] drawIdArr;
	private int gameId = Util.getGameId("ZimLottoBonus");
	private int isAdvancedPlay;
	private LottoPurchaseBean lottoPurchaseBean;
	private int noOfDraws;
	private String pickedNumbers;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String totalPurchaseAmt;
	private String userName;
	private String playType;
	private int noPicked;
    private long LSTktNo;
    private String plrMobileNumber;
    
    public void purchaseTicketProcess(){
    	ServletContext sc = ServletActionContext.getServletContext();
    	try{
			String isDraw = (String) sc.getAttribute("IS_DRAW");
    		if (isDraw.equalsIgnoreCase("NO")) {
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE).getBytes());
				return;
			}
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			List<String> playerPicked = new ArrayList<String>();
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
			
			int serviceId = Util.getServiceIdFormCode(request.getAttribute("code").toString());
			if(serviceId==0 || userBean.getCurrentUserMappingId() == 0){
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE ,  LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}

			String purchaseChannel = "LMS_Terminal";
			String[] picknumbers = pickedNumbers.split("Nxt");
			Integer[] isQuickPick = new Integer[1];
			int [] betAmountMultiple = new int[picknumbers.length]; //int [] betAmountMultiple = {1};
			for(int k=0;k<picknumbers.length;k++)
				betAmountMultiple[k] = 1;
			
			isQuickPick[0] = 2;
			if (!"Perm6".equals(playType)) {
				int noOfLines=picknumbers.length;
				if(noOfLines > ZimLottoBonusConstants.MAX_LINES_DIRECT6){
					
					String finalPurchaseData = "ErrorMsg: You can not choose more than "+ZimLottoBonusConstants.MAX_LINES_DIRECT6+" lines|";
					System.out.println("FINAL PURCHASE DATA ZIMLOTTOBonus:" + finalPurchaseData);
					response.getOutputStream().write(finalPurchaseData.getBytes());
					return;
				}
			}
			
			LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
			lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
			lottoPurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			lottoPurchaseBean.setGameId(gameId);
			lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			lottoPurchaseBean.setNoOfDraws(noOfDraws);
			lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
			lottoPurchaseBean.setPartyType(userBean.getUserType());
			lottoPurchaseBean.setUserId(userBean.getUserId());
			lottoPurchaseBean.setRefMerchantId(refMerchantId);
			lottoPurchaseBean.setPurchaseChannel(purchaseChannel);
			lottoPurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
			lottoPurchaseBean.setPlayType(playType);
			lottoPurchaseBean.setNoPicked(noPicked);
			lottoPurchaseBean.setPickedNumbers(pickedNumbers);
			lottoPurchaseBean.setPlrMobileNumber(plrMobileNumber);
			lottoPurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
			lottoPurchaseBean.setServiceId(serviceId);
			String barcodeType = (String)sc.getAttribute("BARCODE_TYPE");
			lottoPurchaseBean.setBarcodeType(barcodeType);
			lottoPurchaseBean.setQPPreGenerated(false);
			lottoPurchaseBean.setIsQuickPick(isQuickPick);
			lottoPurchaseBean.setBetAmountMultiple(betAmountMultiple);
			
			TransactionManager.setResponseData("true");
			
			
			if (drawIdArr != null) {
				lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			}
			lottoPurchaseBean.setPlayerPicked(playerPicked);
			lottoPurchaseBean.setPicknumbers(picknumbers);
			String finalPurchaseData = null;
			long lastPrintedTicket=0;
			int lstGameId =0;
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			String actionName=ActionContext.getContext().getName();
			lottoPurchaseBean.setLastSoldTicketNo(String.valueOf(lastPrintedTicket));
			lottoPurchaseBean.setActionName(actionName);
			lottoPurchaseBean.setLastGameId(lstGameId);
			lottoPurchaseBean.setDeviceType("TERMINAL");
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			//helper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
			if (picknumbers.length < 1) {
				lottoPurchaseBean.setSaleStatus("ERROR");
				// return SUCCESS;
				finalPurchaseData = "ErrorMsg:"+ EmbeddedErrors.PURCHSE_INVALID_DATA;
				logger.info("FINAL PURCHASE DATA ZIMLOTTOBonus:" + finalPurchaseData);
				response.getOutputStream().write(finalPurchaseData.getBytes());
				return;
			}
			for (int i = 0; i < picknumbers.length; i++) {
				if (!"QP".equals(picknumbers[i])) {
					if (!Util.validateNumber(ConfigurationVariables.ZIMLOTTOBONUS_START_RANGE,ConfigurationVariables.ZIMLOTTOBONUS_END_RANGE, picknumbers[i],false)) {
						lottoPurchaseBean.setSaleStatus("ERROR");
						finalPurchaseData = "ErrorMsg:"+ EmbeddedErrors.PURCHSE_INVALID_DATA;
						logger.info("FINAL PURCHASE DATA ZIMLOTTOBONUS:"+ finalPurchaseData);
						response.getOutputStream().write(finalPurchaseData.getBytes());
						return;
					}
				}
			}
			setLottoPurchaseBean(helper.zimLottoBonusPurchaseTicket(userBean,lottoPurchaseBean));
			lottoPurchaseBean = getLottoPurchaseBean();
			String saleStatus = getLottoPurchaseBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)+ "|";
				System.out.println("FINAL PURCHASE DATA ZIMLOTTOBonus:" + finalPurchaseData);
				response.getOutputStream().write(finalPurchaseData.getBytes());
				return;
			}
			String time = lottoPurchaseBean.getPurchaseTime()
			.replace(" ", "|Time:").replace(".0", "");

	
			double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
	
			String balance = nf.format(bal).replaceAll(",", "");
			int listSize = lottoPurchaseBean.getDrawDateTime().size();
			StringBuilder drawTimeBuild = new StringBuilder("");
			for (int i = 0; i < listSize; i++) {
					drawTimeBuild.append(("|DrawDate:" + lottoPurchaseBean.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
				}
			StringBuilder stBuilder = new StringBuilder("");
			for (int i = 0; i < lottoPurchaseBean.getPlayerPicked().size(); i++) {
						stBuilder.append("|Num:"+ lottoPurchaseBean.getPlayerPicked().get(i) + "|QP:"+ lottoPurchaseBean.getIsQuickPick()[i]);
			}

			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
			String advtMsg = "";

			UtilApplet.getAdvMsgs(lottoPurchaseBean.getAdvMsg(), topMsgsStr,bottomMsgsStr, 10);

			if (topMsgsStr.length() != 0) {
					advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}

			if (bottomMsgsStr.length() != 0) {
					advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr;
			}

			List<RafflePurchaseBean> rafflePurchaseBeanList = lottoPurchaseBean.getRafflePurchaseBeanList();
			String raffleData = CommonMethods.buildRaffleData(rafflePurchaseBeanList);

			String promoData="";
			if(lottoPurchaseBean.getPromoPurchaseBeanList()!=null){
				List<LottoPurchaseBean> promoPurchaseBeanList=lottoPurchaseBean.getPromoPurchaseBeanList();
				promoData=buildSaleDataforZimlottoBonusFree(promoPurchaseBeanList,userBean);
			}
	finalPurchaseData = "TicketNo:" + lottoPurchaseBean.getTicket_no()
			+ lottoPurchaseBean.getReprintCount()+"|brCd:"+lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? lottoPurchaseBean.getBarcodeCount():"")+"|Date:" + time
			+ "|PlayType:" + lottoPurchaseBean.getPlayType()
			+ stBuilder.toString() + "|TicketCost:"
			+ lottoPurchaseBean.getTotalPurchaseAmt()
			+ drawTimeBuild.toString() + "|balance:" + balance + "|"
			+ advtMsg +"|"+ raffleData +promoData;

	logger.info("FINAL PURCHASE DATA ZIMLOTTOBONUS:" + finalPurchaseData);
	request.setAttribute("purchaseData", finalPurchaseData);
	if("SUCCESS".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())){
		String smsData = com.skilrock.lms.common.utility.CommonMethods.prepareSMSData(new String[]{lottoPurchaseBean.getPickedNumbers()}, new String[]{lottoPurchaseBean.getPlayType()}, new StringBuilder(String.valueOf(lottoPurchaseBean.getTotalPurchaseAmt())), new StringBuilder(lottoPurchaseBean.getTicket_no()+lottoPurchaseBean.getReprintCount()), lottoPurchaseBean.getDrawDateTime());
		com.skilrock.lms.common.utility.CommonMethods.sendSMS(smsData);
	}
	response.getOutputStream().write(finalPurchaseData.getBytes());
	}catch (LMSException e) {
		e.printStackTrace();
		try {
			response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return;
	}catch (Exception e) {
		logger.info("FINAL PURCHASE DATA ZIMLOTTOBONUS:Error!Try Again",e);
		try {
			response.getOutputStream().write("Error!Try Again".getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			logger.info("IOError",e1);
		}
		return;
	}
    	
    	
    }
    
    public String buildSaleDataforZimlottoBonusFree(List<LottoPurchaseBean> promoPurchaseBeanList,UserInfoBean userBean) {
         StringBuilder finalPromoData=new StringBuilder();
         
		for(int j=0;j<promoPurchaseBeanList.size();j++){
			LottoPurchaseBean lottoBean=promoPurchaseBeanList.get(j);
			String time = lottoBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");

			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userBean);
			double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			String balance = nf.format(bal).replaceAll(",", "");
			int listSize = lottoBean.getDrawDateTime().size();
			StringBuilder drawTimeBuild = new StringBuilder("");
			for (int i = 0; i < listSize; i++) {
					drawTimeBuild.append(("|DrawDate:" + lottoBean.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
				}
		StringBuilder stBuilder = new StringBuilder("");

		for (int i = 0; i < lottoBean.getPlayerPicked().size(); i++) {
			stBuilder.append("|Num:"+ lottoBean.getPlayerPicked().get(i) + "|QP:"+ lottoBean.getIsQuickPick()[i]);
		}

		List<RafflePurchaseBean> rafflePurchaseBeanList = lottoBean.getRafflePurchaseBeanList();
		String raffleData = CommonMethods.buildRaffleData(rafflePurchaseBeanList);
		
		String finalData = "PromoTkt:TicketNo:"
				+ lottoBean.getTicket_no() + lottoBean.getReprintCount()
				+"|brCd:"+lottoBean.getTicket_no() + lottoBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? lottoBean.getBarcodeCount():"")
				+ "|Date:" + time 
				+ "|PlayType:" + lottoBean.getPlayType()
				+ stBuilder.toString()+ "|TicketCost:"
				//+ ("Perm6".equals(lottoBean.getPlayType())?stBuilder.toString():"|PlayType:" + lottoBean.getPlayType()+ stBuilder.toString()) + "|TicketCost:"
				+ lottoBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance + "|"
				+ raffleData;
		finalPromoData.append(finalData);
		}
		return finalPromoData.toString();
	}
    
	public int getBetAmountMultiple() {
		return betAmountMultiple;
	}
	public String[] getDrawIdArr() {
		return drawIdArr;
	}
	public int getGameId() {
		return gameId;
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
	public String getPlayType() {
		return playType;
	}
	public int getNoPicked() {
		return noPicked;
	}
	public long getLSTktNo() {
		return LSTktNo;
	}
	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}
	public void setBetAmountMultiple(int betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}
	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
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
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}              
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public void setNoPicked(int noPicked) {
		this.noPicked = noPicked;
	}
	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}
	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}
    
    
	
}
