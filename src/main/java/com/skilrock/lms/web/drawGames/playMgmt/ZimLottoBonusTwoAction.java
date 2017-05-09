package com.skilrock.lms.web.drawGames.playMgmt;

import java.util.Arrays;
import java.util.Date;
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
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.gameconstants.ZimLottoBonusTwoConstants;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.StringifyGameResponseForApplet;
import com.skilrock.lms.web.drawGames.common.Util;

public class ZimLottoBonusTwoAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	Log logger = LogFactory.getLog(ZimLottoBonusTwoAction.class);
	private static final long serialVersionUID = 1L;
	private final static String gameName = "ZimLottoBonusTwo";

	private int gameId;
	private int noPicked;
	private int noOfDraws;
	private int isAdvancedPlay;
	private int betAmountMultiple;
	
	private long LSTktNo;
    
	private String errMsg;
	private String playType;
	private String pickNumStr;
    private String plrMobileNumber;
    private String purchaseData;
	private String totalPurchaseAmt;

	private String[] drawIdArr;

	private String pickedNumbers;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private LottoPurchaseBean lottoPurchaseBean;
	
	@SuppressWarnings("unchecked")
	public String purchaseTicketProcess() throws Exception {
		
		LottoPurchaseBean lottoPurchaseBean = null;
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		int serviceId = Util.getServiceIdFormCode(request.getAttribute("code").toString());
		if(serviceId==0 || userBean.getCurrentUserMappingId() == 0){
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE ,  LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		gameId = Util.getGameId(gameName);
		if(gameId == 0){
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE ,  LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		if(isAdvancedPlay==1 && drawIdArr==null){
			setErrMsg(DGErrorMsg.buyErrMsg(""));
			return ERROR;
		}
		
		String[] picknumbers = pickedNumbers.split("Nxt");
		ServletContext sc = ServletActionContext.getServletContext();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
	
		
		if (!"Perm6".equals(playType)) {
			int noOfLines=picknumbers.length;
			if(noOfLines > ZimLottoBonusTwoConstants.MAX_LINES_DIRECT6){
				setErrMsg("You can not choose more than "+ZimLottoBonusTwoConstants.MAX_LINES_DIRECT6+" lines");
				System.out.println("You can not choose more than "+ZimLottoBonusTwoConstants.MAX_LINES_DIRECT6+" lines");
				return ERROR;
						
			}
		}
		
		
		lottoPurchaseBean = new LottoPurchaseBean();
		lottoPurchaseBean.setGameId(gameId);
		lottoPurchaseBean.setPlayType(playType);
		lottoPurchaseBean.setNoPicked(noPicked);
		lottoPurchaseBean.setNoOfDraws(noOfDraws);
		lottoPurchaseBean.setServiceId(serviceId);
		lottoPurchaseBean.setPicknumbers(picknumbers);
		lottoPurchaseBean.setPurchaseChannel("LMS_Web");
		lottoPurchaseBean.setUserId(userBean.getUserId());
		lottoPurchaseBean.setPickedNumbers(pickedNumbers);
		lottoPurchaseBean.setRefMerchantId(refMerchantId);
		lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		lottoPurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		lottoPurchaseBean.setPlrMobileNumber(plrMobileNumber);
		lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
		lottoPurchaseBean.setPartyType(userBean.getUserType());
		lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
		lottoPurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
		lottoPurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
		lottoPurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));
		
		if (drawIdArr != null) {
			lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		setLottoPurchaseBean(lottoPurchaseBean);
		if (picknumbers.length < 1) {
			lottoPurchaseBean.setSaleStatus("ERROR");
			return SUCCESS;
		}
		for (int i = 0; i < picknumbers.length; i++) {
			if (!"QP".equals(picknumbers[i])) {
				if (!Util.validateNumber(ConfigurationVariables.ZIMLOTTOBONUSTWO_START_RANGE,
										 ConfigurationVariables.ZIMLOTTOBONUSTWO_END_RANGE,picknumbers[i], false)) {
						lottoPurchaseBean.setSaleStatus("ERROR");
					return SUCCESS;
				}
			}
		}
		int lstGameId =0;
		long lastPrintedTicket=0;
		

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		String actionName=ActionContext.getContext().getName();
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		//helper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
		try{
			LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			helper.insertEntryIntoPrintedTktTableForWeb(lstGameId, userBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		setLottoPurchaseBean(helper.zimLottoBonusTwoPurchaseTicket(userBean,lottoPurchaseBean));
		lottoPurchaseBean = getLottoPurchaseBean();
		String saleStatus = getLottoPurchaseBean().getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			setErrMsg(DGErrorMsg.buyErrMsg(saleStatus));
			return ERROR;
		}
		//added for numbers wrong print in ticket
		StringBuilder sb=new StringBuilder();
		if(lottoPurchaseBean.getPlayerPicked().size()>0){
			for(int i=0;i<lottoPurchaseBean.getPlayerPicked().size();i++){
				sb.append(lottoPurchaseBean.getPlayerPicked().get(i)+ ";");
			}
		}
		setPickNumStr(sb.toString());
		CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, userBean.getUserName(), getLottoPurchaseBean().getTicket_no()+getLottoPurchaseBean().getReprintCount());
		setPurchaseData(StringifyGameResponseForApplet.stringifyGameResponseForApplet(userBean, lottoPurchaseBean));
		return SUCCESS;
	}

	public String reprintTicket() throws Exception {
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
	}
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(int noPicked) {
		this.noPicked = noPicked;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public int getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public void setBetAmountMultiple(int betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getPickNumStr() {
		return pickNumStr;
	}

	public void setPickNumStr(String pickNumStr) {
		this.pickNumStr = pickNumStr;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public String getPurchaseData() {
		return purchaseData;
	}

	public void setPurchaseData(String purchaseData) {
		this.purchaseData = purchaseData;
	}


	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public String[] getDrawIdArr() {
		return drawIdArr;
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public LottoPurchaseBean getLottoPurchaseBean() {
		return lottoPurchaseBean;
	}

	public void setLottoPurchaseBean(LottoPurchaseBean lottoPurchaseBean) {
		this.lottoPurchaseBean = lottoPurchaseBean;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
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
}
