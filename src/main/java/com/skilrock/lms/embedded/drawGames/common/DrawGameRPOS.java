package com.skilrock.lms.embedded.drawGames.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.MpesaPaymentProcessHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.ServerStartUpData;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.DrawWinningReportBean;
import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneThreePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.dge.beans.ReportDrawBean;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class DrawGameRPOS extends ActionSupport implements ServletRequestAware,
ServletResponseAware {
	static Log logger = LogFactory.getLog(DrawGameRPOS.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DrawGameRPOS() {
		rposHelper = new DrawGameRPOSHelper();
		reprintFactory = new ReprintFactory();
	}
	
	public DrawGameRPOS(DrawGameRPOSHelper rposHelper, ReprintFactory reprintFactory) {
		this.rposHelper = rposHelper;
		this.reprintFactory = reprintFactory;
	}

	public static void main(String[] args) throws Exception {
		// ServiceRequest req = new ServiceRequest();
		// DrawGameRPOS dg = new DrawGameRPOS();
		// dg.prizeWinningTicket();

		Date d = new Date(1267522200000l);
		logger.debug(d);
	}
	
	private DrawGameRPOSHelper rposHelper;
	/*public DrawGameRPOS() {
		
	}*/

	private boolean autoCancel;
	TreeMap drawGameData;
	private String drawGameNewData;
	private String drawTime;
	private String firstName;
	private String gameName;
	private int gameNo;
	private String idNumber;
	private String idType;
	private String lastName;
	private HttpServletResponse response;
	private HttpServletRequest servletRequest;
	HttpSession session = null;
	private String startDate;
	private String ticketNo;
	private String isNewURL;
	private String userName;
	private String mobileNo;
	private long LSTktNo;
	private String mPesa;
	private String endDate;
	private double version;//Terminal Version 
    private String cancelType;
    private String curDate;
    private String countryDeployed;
    private String balance;
    private String isDrawAvailable;
    private Object gameBean;
    private UserInfoBean userInfoBean;
    private String gameBeanType;
    private EmbeddedReprint embeddedReprint;
    private ServletContext servletContext;
    String finalReprintData;
    private ReprintFactory reprintFactory;
    private Map currentUserSessionMap;
	private String refernceMerchantId;
	private String actionName;
	private NumberFormat numberFormat;
	private int autoCancelHoldDays;
	private long lastPrintedTicket;
	private int gameId ;
	private double userBalance;
    
	public void cancelTicketNew() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		int barCodeCount=-1;
		int inpType=Integer.parseInt((String) sc.getAttribute("InpType"));
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|").getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		// logger.debug("LOGGED_IN_USERS map is: " + currentUserSessionMap);
		// logger.debug("user name is: " + userName);
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		/*int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId = 0;
		if(LSTktNo != 0){
			lastPrintedTicket = LSTktNo/100;
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
*/
		String cancelChannel = "LMS_Terminal";
		CancelTicketBean cancelTicketBean = new CancelTicketBean();
		/*Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getContext("/DrawGameWeb").getAttribute("drawIdTableMap");*/
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
		// logger.debug(" ticketNo = " + ticketNo + " drawIdTableMap in
		// cancel ticket " + drawIdTableMap);
		cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
		String finalCancelData = null;
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		
		cancelTicketBean.setCancelDuaraion("true".equalsIgnoreCase((String)sc.getAttribute("IS_CANCEL_DURATION")));
		cancelTicketBean.setCancelDuration(Integer.parseInt((String)sc.getAttribute("CANCEL_DURATION")));
		cancelTicketBean.setCancelType((String) sc.getAttribute("CANCEL_TYPE"));
		if ("LAST_SOLD_TICKET".equalsIgnoreCase((String) sc
				.getAttribute("CANCEL_TYPE")) || isAutoCancel()) {
			ticketNo = helper.getLastSoldTicketNO(userInfoBean,"TERMINAL");
			
			if (ticketNo != null) {
				ticketNo = ticketNo + Util.getRpcAppenderForTickets(ticketNo.length());
			}else{
				finalCancelData = "ErrorMsg:"+EmbeddedErrors.CANCEL_INVALID+"ErrorCode:" + EmbeddedErrors.CANCEL_INVALID_ERROR_CODE+"|";
				logger.debug("FINAL CANCEL DATA:" + finalCancelData);
				response.getOutputStream().write(finalCancelData.getBytes());
				return;
			}
			
			
			/*if("LAST_SOLD_TICKET".equalsIgnoreCase((String) sc
					.getAttribute("CANCEL_TYPE"))){
				cancelTicketBean.setCancelType("LAST_SOLD_TICKET");		
			}*/
			
			
			if (isAutoCancel()){
				if(ticketNo.equals(String.valueOf(LSTktNo))){
					finalCancelData = "Last request could not be processed. Try again|";
					logger.debug("FINAL CANCEL DATA:" + finalCancelData);
					response.getOutputStream().write(finalCancelData.getBytes());
					return;
				}
				//cancelTicketBean.setCancelType("TICKET_NO");
			}
		}else if("TICKET_NO".equalsIgnoreCase((String) sc
				.getAttribute("CANCEL_TYPE"))){
			// Get The BarCode From Ticket Number If Required  
			if(ticketNo.length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount){
				barCodeCount = Integer
						.parseInt(Util.getBarCodeCountFromTicketNumber(ticketNo));
				ticketNo=Util.getTicketNumber(ticketNo, inpType);
			}
		}	
		if (ticketNo == null && LSTktNo == 0) {
			finalCancelData = "ErrorMsg:" + EmbeddedErrors.CANCEL_INVALID+"ErrorCode:" + EmbeddedErrors.CANCEL_INVALID_ERROR_CODE+"|";
			logger.debug("FINAL CANCEL DATA:" + finalCancelData);
			response.getOutputStream().write(finalCancelData.getBytes());
			return;
		}
		/*else if((helper.getLastSoldTicketNO(userInfoBean)).equals(LSTktNo) && isAutoCancel()){
			finalCancelData = "ErrorMsg:" + EmbeddedErrors.CANCEL_INVALID;
			logger.debug("FINAL CANCEL DATA:" + finalCancelData);
			response.getOutputStream().write(finalCancelData.getBytes());
		}*/
		else {
			/*if (isAutoCancel()){
				ticketNo = helper.getLastSoldTicketNO(userInfoBean);
				if (ticketNo != null) {
					ticketNo = ticketNo + "00";
				}
				cancelTicketBean.setCancelType("TICKET_NO");
			}*/
			
			cancelTicketBean.setBarCodeCount(barCodeCount);
			cancelTicketBean.setTicketNo(ticketNo);
			cancelTicketBean.setPartyId(userInfoBean.getUserOrgId());
			cancelTicketBean.setPartyType(userInfoBean.getUserType());
			cancelTicketBean.setUserId(userInfoBean.getUserId());
			cancelTicketBean.setCancelChannel(cancelChannel);
			cancelTicketBean.setRefMerchantId(refMerchantId);
			cancelTicketBean.setAutoCancel(isAutoCancel()); //  for manual cancel change to auto cancel
			//cancelTicketBean.setAutoCancel(true);
			cancelTicketBean = helper.cancelTicket(cancelTicketBean,
					userInfoBean, isAutoCancel(),cancelType);
			// logger.debug("@@@@@@@@@@@@@@@@@@@@@@"+cancelTicketBean.isPerformed());
			// -----Calculation of balance for cancel ticket----------
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userInfoBean);
			double bal = userInfoBean.getAvailableCreditLimit()
			- userInfoBean.getClaimableBal();
			/*String balance = bal + "00";
			balance = balance.substring(0, balance.indexOf(".") + 3);*/
			NumberFormat nFormat = NumberFormat.getInstance();
			nFormat.setMinimumFractionDigits(2);
			String balance = nFormat.format(bal).replaceAll(",", "");
			// logger.debug("------reprint Count" +
			// cancelTicketBean.getReprintCount());
			if (cancelTicketBean.isValid()) {
				String advtMsg = "";
				if (cancelTicketBean.getRefundAmount() > 0) {
					StringBuilder topMsgsStr = new StringBuilder("");
					StringBuilder bottomMsgsStr = new StringBuilder("");
					UtilApplet.getAdvMsgs(cancelTicketBean.getAdvMsg(),
							topMsgsStr, bottomMsgsStr, 10);
					if (topMsgsStr.length() != 0) {
						advtMsg = "topAdvMsg:" + topMsgsStr + "|";
					}
					if (bottomMsgsStr.length() != 0) {
						advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr
						+ "|";
					}
				}
				finalCancelData = "RfdA:"
					+ cancelTicketBean.getRefundAmount()
					+ "|TktN:"
					+ Util.getTktWithoutRpcNBarCodeCount(cancelTicketBean.getTicketNo(), cancelTicketBean.getTicketNo().length())
							+ cancelTicketBean.getReprintCount() + "|Balance:"
							+ balance + "|" + advtMsg;
			} else if (cancelTicketBean.isError()) { // RG Limit fail
				finalCancelData = "ErrorMsg:" + cancelTicketBean.getErrMsg()
				+ "|";
			} else if(isAutoCancel()){
				finalCancelData = "Last request could not be processed. Try again|";
			}
			else {
				finalCancelData = "ErrorMsg:" + EmbeddedErrors.CANCEL_INVALID+"ErrorCode:" + EmbeddedErrors.CANCEL_INVALID_ERROR_CODE+"|";
			}
			logger.debug("FINAL CANCEL DATA:" + finalCancelData);
			
			TransactionManager.setResponseData("true");
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getOutputStream().write(finalCancelData.getBytes());
		}
	}
	public void cancelTicket() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		// logger.debug("LOGGED_IN_USERS map is: " + currentUserSessionMap);
		// logger.debug("user name is: " + userName);
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		String cancelChannel = "LMS_Terminal";
		CancelTicketBean cancelTicketBean = new CancelTicketBean();
		/*Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getContext("/DrawGameWeb").getAttribute("drawIdTableMap");*/
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
		// logger.debug(" ticketNo = " + ticketNo + " drawIdTableMap in
		// cancel ticket " + drawIdTableMap);
		cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
		String finalCancelData = null;
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		cancelTicketBean.setCancelDuaraion("true".equalsIgnoreCase((String)sc.getAttribute("IS_CANCEL_DURATION")));
		cancelTicketBean.setCancelDuration(Integer.parseInt((String)sc.getAttribute("CANCEL_DURATION")));
		if ("LAST_SOLD_TICKET".equalsIgnoreCase((String) sc
				.getAttribute("CANCEL_TYPE"))) {
			ticketNo = helper.getLastSoldTicketNO(userInfoBean,"TERMINAL");
			if (ticketNo != null) {
				ticketNo = ticketNo + "00";
			}
			cancelTicketBean.setCancelType("LAST_SOLD_TICKET");
		}
		if (ticketNo == null) {
			finalCancelData = "ErrorMsg:" + EmbeddedErrors.CANCEL_INVALID+"ErrorCode:" + EmbeddedErrors.CANCEL_INVALID_ERROR_CODE+"|";
			logger.debug("FINAL CANCEL DATA:" + finalCancelData);
			response.getOutputStream().write(finalCancelData.getBytes());
		} else {
			cancelTicketBean.setTicketNo(ticketNo);
			cancelTicketBean.setPartyId(userInfoBean.getUserOrgId());
			cancelTicketBean.setPartyType(userInfoBean.getUserType());
			cancelTicketBean.setUserId(userInfoBean.getUserId());
			cancelTicketBean.setCancelChannel(cancelChannel);
			cancelTicketBean.setRefMerchantId(refMerchantId);
			cancelTicketBean = helper.cancelTicket(cancelTicketBean,
					userInfoBean, isAutoCancel(),cancelType);
			// logger.debug("@@@@@@@@@@@@@@@@@@@@@@"+cancelTicketBean.isPerformed());
			// -----Calculation of balance for cancel ticket----------
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userInfoBean);
			double bal = userInfoBean.getAvailableCreditLimit()
			- userInfoBean.getClaimableBal();
			/*String balance = bal + "00";
			balance = balance.substring(0, balance.indexOf(".") + 3);*/
			NumberFormat nFormat = NumberFormat.getInstance();
			nFormat.setMinimumFractionDigits(2);
			String balance = nFormat.format(bal).replaceAll(",", "");
			// logger.debug("------reprint Count" +
			// cancelTicketBean.getReprintCount());
			if (cancelTicketBean.isValid()) {
				String advtMsg = "";
				if (cancelTicketBean.getRefundAmount() > 0) {
					StringBuilder topMsgsStr = new StringBuilder("");
					StringBuilder bottomMsgsStr = new StringBuilder("");
					UtilApplet.getAdvMsgs(cancelTicketBean.getAdvMsg(),
							topMsgsStr, bottomMsgsStr, 10);
					if (topMsgsStr.length() != 0) {
						advtMsg = "topAdvMsg:" + topMsgsStr + "|";
					}
					if (bottomMsgsStr.length() != 0) {
						advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr
						+ "|";
					}
				}
				finalCancelData = "RfdA:"
					+ cancelTicketBean.getRefundAmount()
					+ "|TktN:"
					+ cancelTicketBean.getTicketNo().substring(0,
							cancelTicketBean.getTicketNo().length() - 2)
							+ cancelTicketBean.getReprintCount() + "|Balance:"
							+ balance + "|" + advtMsg;
			} else if (cancelTicketBean.isError()) { // RG Limit fail
				finalCancelData = "ErrorMsg:" + cancelTicketBean.getErrMsg()
				+ "|";
			} else {
				finalCancelData = "ErrorMsg:" + EmbeddedErrors.CANCEL_INVALID+"ErrorCode:" + EmbeddedErrors.CANCEL_INVALID_ERROR_CODE+"|";
			}
			logger.debug("FINAL CANCEL DATA:" + finalCancelData);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getOutputStream().write(finalCancelData.getBytes());
		}
	}
	public void cancelTicketOld() throws Exception {
		logger.debug("Before--" + new Date());
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					"Draw Game Not Avaialbe".getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		// logger.debug(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);
		logger.debug(" user name is " + userName);
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		CancelTicketBean cancelTicketBean = new CancelTicketBean();
		/*Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getContext("/DrawGameWeb").getAttribute("drawIdTableMap");*/
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
		logger.debug(" ticketNo = " + ticketNo
				+ " drawIdTableMap in cancel ticket " + drawIdTableMap);
		cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
		cancelTicketBean.setTicketNo(ticketNo);
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		cancelTicketBean = helper.cancelTicket(cancelTicketBean, userInfoBean,
				isAutoCancel(),cancelType);
		logger.debug("is valid---------" + cancelTicketBean.isValid());
		if (cancelTicketBean.isValid()) {
			response.getOutputStream().write("Ticket cancelled".getBytes());
		} else {
			response.getOutputStream()
			.write("Ticket Number inValid".getBytes());
		}
		logger.debug("After--" + new Date());
	}
	public void fetchDrawGameData() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		logger.debug("Before--" + new Date());
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId = 0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
			return;
		}
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
			return;
		}
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		helper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		setDrawGameData(helper.getDrawGameData());
		sc.setAttribute("GAME_DATA", drawGameData);
		logger.debug("After--" + new Date() + getDrawGameData());
		Set keySet = drawGameData.keySet();
		Iterator iter = keySet.iterator();
		// Object gameName = gameName;
		DrawWinningReportBean drawWinningReportBean = null;
		List<String> drawResultList = null;
		String[] drawResult = null;
		int drawResultCount = 0;
		StringBuilder sBuilder = new StringBuilder("");
		// code to be changed later for multiple games
		/*
		 * while (iter.hasNext()) { gameName = iter.next(); list = (List)
		 * drawGameData.get(gameName);
		 * 
		 * drawResultList = (List<String>) list.get(1);
		 * 
		 * for (int i = 0; i < drawResultList.size(); i++) { drawResult =
		 * drawResultList.get(i).split("="); date = new
		 * Date(Long.parseLong(drawResult[0])); sBuilder.append("DrawDate:" +
		 * date.getDay() + "-" + date.getMonth() + "-" + date.getYear() +
		 * ",DrawTime:" + date.getHours() + ":" + date.getMinutes() + ":" +
		 * date.getSeconds() + ",Sign:" + drawResult[1]+"|"); } }
		 */
		// gameName = iter.next();
		// logger.debug(" -----------------drawGameData -- " +
		// drawGameData);
		// logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+Util.getGameNumber(gameName.toLowerCase()));
		int gameIdRep=Util.getGameId(gameName);
		drawWinningReportBean = (DrawWinningReportBean) drawGameData.get(Util.getGameMap().get(gameIdRep)
				.getGameId());
		// logger.debug(" result list is "+list);
		if (drawWinningReportBean == null) {
			logger.debug(" game name is " + gameName + " not found");
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.RESULT_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.RESULT_GAME_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}
		drawResultList = drawWinningReportBean.getWinningDataList();
		List<Integer> indexList=new ArrayList<Integer>();
		Iterator<String> drawResultListIterator=drawResultList.iterator();
		while (drawResultListIterator.hasNext()) {
			String string = (String) drawResultListIterator.next();
			if(new Timestamp(Long.parseLong(string.split("=")[0])).toString().substring(0,10).equals(curDate))
			indexList.add(drawResultList.indexOf(string));
			
		}
		
		if (drawResultList != null) {
			drawResultCount = drawResultList.size() > 5 ? 5 : drawResultList
					.size();
		}
		// drawResultCount =
		/*
		 * if(drawResultList == null){ logger.debug(" game name is " +
		 * gameName + " not found"); response.getOutputStream().write(
		 * ("ErrorMsg:" + EmbeddedErrors.RESULT_GAME_NOT_AVAILABLE)
		 * .getBytes()); return; }
		 */
		if (drawResultList != null && drawResultCount == 0) {
			logger.debug("No Draw Available");
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.RESULT_DRAW_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.RESULT_DRAW_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}
		List<DrawDetailsBean> drawDetailsList = drawWinningReportBean.getDrawDetailsList();
		for (int i = 0; i < drawResultCount; i++) {
			
			if(!"-1".equals(curDate)){
				if(!indexList.contains(i)){
					continue;
				}
			}
			
			drawResult = drawResultList.get(i).split("=");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(drawResult[0]));
			// sBuilder.append("DrawDate:" + date.getDay() + "-"
			// +( date.getMonth()+1 )+ "-" + date.getYear() + ",DrawTime:"
			// + date.getHours() + ":" + date.getMinutes() + ":"
			// + date.getSeconds() + ",Sign:" + drawResult[1]+"|");
			DrawDetailsBean drawBean = drawDetailsList.get(i);
			String drawName = drawBean.getDrawName();
			String machineResult = drawBean.getMachineResult();
			if("null".equalsIgnoreCase(drawResult[1])){
				drawResult[1] = null;
			}
			if(drawResult[1] == null && !"YES".equalsIgnoreCase(isNewURL)){
				continue;
			}
			if(drawResult[1] != null || machineResult != null){
				sBuilder.append("DrawTime:");
				sBuilder.append(cal.get(Calendar.YEAR)
						+ "-"
						+ (cal.get(Calendar.MONTH) + 1 > 9 ? cal
								.get(Calendar.MONTH) + 1 : "0"
									+ (cal.get(Calendar.MONTH) + 1))
									+ "-"
									+ (cal.get(Calendar.DAY_OF_MONTH) > 9 ? cal
											.get(Calendar.DAY_OF_MONTH) : "0"
												+ cal.get(Calendar.DAY_OF_MONTH))
												+ " "
												+ (cal.get(Calendar.HOUR_OF_DAY) > 9 ? cal
														.get(Calendar.HOUR_OF_DAY) : "0"
															+ cal.get(Calendar.HOUR_OF_DAY))
															+ ":"
															+ (cal.get(Calendar.MINUTE) > 9 ? cal.get(Calendar.MINUTE)
																	: "0" + cal.get(Calendar.MINUTE))
																	+ ":"
																	+ (cal.get(Calendar.SECOND) > 9 ? cal.get(Calendar.SECOND)
																			: "0" + cal.get(Calendar.SECOND)));
				if("YES".equalsIgnoreCase(isNewURL)){
					if(drawName != null && !"null".equalsIgnoreCase(drawName.trim()) && !"".equalsIgnoreCase(drawName.trim())){
						sBuilder.append("*" + drawName);
					}
				}
				sBuilder.append(",Sign:");
				if(drawResult[1] != null && !"null".equalsIgnoreCase(drawResult[1].trim()) && !"".equalsIgnoreCase(drawResult[1].trim())){
					if("FortuneTwo".equalsIgnoreCase(gameName)){
						String[] winResArr = drawResult[1].split(",");
						StringBuilder sbResult = new StringBuilder("");
						for (int j = 0; j < winResArr.length; j++) {
							winResArr[j] = winResArr[j].substring(0,3).toUpperCase();
							sbResult.append(winResArr[j] + ",");
						}
						if(sbResult.length() > 0){
							sbResult.deleteCharAt(sbResult.length() - 1);
						}
						drawResult[1] = sbResult.toString();
					}
					sBuilder.append(drawResult[1]);
				}
				if("YES".equalsIgnoreCase(isNewURL)){
					
					if(machineResult != null && !"null".equalsIgnoreCase(machineResult.trim()) && !"".equalsIgnoreCase(machineResult.trim())){
						sBuilder.append(";");
						sBuilder.append("machine:" + machineResult);
					}
				}
				sBuilder.append("|");
			}
		}
		if(sBuilder.length()==0){
			logger.debug("No Result Available");
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.RESULT_DRAW_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.RESULT_DRAW_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}else{
		logger.debug(" Draw result for fortune is " + sBuilder.toString());
		response.getOutputStream().write(sBuilder.toString().getBytes());
		}
	}
	public void fetchDrawGameUpdatedData() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		PrintWriter out = getResponse().getWriter();
		StringBuilder rData = new StringBuilder("");
		logger.debug("Front End Call**" + sc.getAttribute("GAME_DATA"));
		logger.debug(sc.getAttribute("GAME_DATA"));
		logger.debug(sc.getAttribute("drawIdTableMap"));
		Iterator iter = ((TreeMap) sc.getAttribute("GAME_DATA")).entrySet()
		.iterator();
		while (iter.hasNext()) {
			Map.Entry pair = (Map.Entry) iter.next();
			List<List> drawList = (List<List>) pair.getValue();
			String nxtDrwTime = drawList.get(0).toString().replace("[", "")
			.replace("]", "");
			List<String> winList = (List<String>) drawList.get(1);
			Iterator winItr = winList.iterator();
			String winData = "";
			while (winItr.hasNext()) {
				winData = winData + winItr.next() + " Nxt ";
			}
			rData.append(pair.getKey() + "UPD" + nxtDrwTime + "UPD" + winData
					+ "Game");
		}
		logger.debug("--Updated--" + rData);
		out.print(rData);
	}
	public void fetchDrawResultData() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			try {
				response
				.getOutputStream()
				.write(
						("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
						.getBytes());
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}
		int noOfDays = Integer.parseInt((String) sc
				.getAttribute("EMBED_REPORT_DAYS"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -noOfDays);
		StringBuilder winningResult = new StringBuilder("");
		String raffleTktType = (String) sc.getAttribute("raffle_ticket_type");
		if (!cal.getTime().after(
				(new SimpleDateFormat("yyyy-MM-dd")).parse(startDate))) {
			DrawDataBean drawDataBean = new DrawDataBean();
			drawDataBean.setGameNo(gameNo);
			drawDataBean.setFromDate(startDate + " 00:00:00");
			drawDataBean.setToDate(startDate + " 23:59:59");
			DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
			List<ReportDrawBean> winningResultList = helper.fetchDrawData(
					drawDataBean, raffleTktType).getRepGameBean()
					.getRepDrawBean();
			for (int i = 0; i < winningResultList.size(); i++) {
				ReportDrawBean bean = winningResultList.get(i);
				winningResult.append(bean.getDrawDateTime());
				if (bean.getWinningResult() == null) {
					winningResult.append("-N.A.|");
				} else {
					winningResult.append("-"
							+ bean.getWinningResult().toUpperCase() + "|");
				}
			}
		}
		if (winningResult.length() == 0) {
			winningResult.append("|");
		}
		logger.debug("Winning Result:" + winningResult + "|");
		response.getOutputStream().write(
				("Winning Result:" + winningResult).getBytes());
	}
	// -- This METHOD Fetches The Updated DRAW DATA 'GameNameDev Wise'
	public void fetchUpdatedDrawGameData() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
			return;
		}
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
			return;
		}
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId = 0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		/*
		 * Map<String, Map<Long, String>> drawIdDateMap = (TreeMap<String,
		 * Map<Long, String>>) ServletActionContext
		 * .getServletContext().getAttribute("drawIdDateMap");
		 */
		Map<Integer, Map<Integer, DrawDetailsBean>> drawDetailsMap = (Map<Integer, Map<Integer, DrawDetailsBean>>) ServletActionContext
		.getServletContext().getAttribute("drawDetailsMap");
		logger.debug("**drawDetailsMap****" + drawDetailsMap + "**");
		StringBuilder strBuild = new StringBuilder("");
		int gameIdTerminal=Util.getGameId(gameName);
		
		Map<Integer, DrawDetailsBean> innerMap = drawDetailsMap.get(gameIdTerminal);
		if (innerMap != null && innerMap.size() > 0) {
			Iterator<Map.Entry<Integer, DrawDetailsBean>> innerIter = innerMap
			.entrySet().iterator();
			DrawDetailsBean drawBean = null;
			String currentVersion = DrawGameRPOSHelper
			.fetchCurrentVersion(userName);
			while (innerIter.hasNext()) {
				Map.Entry<Integer, DrawDetailsBean> innerEntryMap = innerIter
				.next();
				drawBean = innerEntryMap.getValue();
				strBuild.append(drawBean.getDrawId() + ",");
				/*
				 * Timestamp drawTime = new Timestamp(innerEntryMap.getKey());
				 * if
				 * ("keno".equalsIgnoreCase(gameName)||"kenoTwo".equalsIgnoreCase
				 * (gameName)) { strBuild.append(Utility.fetchDrawName(
				 * Util.getGameNumber(gameName), drawTime) .split("\\*")[1] +
				 * ","); }
				 */
				if (drawBean.getDrawName() != null
						&& !"null".equalsIgnoreCase(drawBean.getDrawName())) {
					if (currentVersion != null
							&& (Double.parseDouble(currentVersion) > 4.6 || (Double
									.parseDouble(currentVersion) <= 4.6 && "Keno"
									.equalsIgnoreCase(gameName)))) {
						strBuild.append(drawBean.getDrawName() + ",");
					}
				}
				strBuild.append(drawBean.getDrawDateTime().toString().split(
				"\\.")[0]
				       + ",");
			}
			strBuild.deleteCharAt(strBuild.length() - 1);
		}
		strBuild.append("|");
		logger.debug("Updated Draw Game Data: " + strBuild.toString());
		response.getOutputStream().write(strBuild.toString().getBytes());
	}
	public void fetchWinningResult() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			try {
				response
				.getOutputStream()
				.write(
						("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
						.getBytes());
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId = 0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}
		/*
		 * TreeMap<Integer, List<List>> gameData = (TreeMap<Integer,
		 * List<List>>) sc .getAttribute("GAME_DATA");
		 */
		TreeMap<Integer, DrawWinningReportBean> gameData = new DrawGameRPOSHelper()
		.getDrawGameData();
		List<String> winningResultList = gameData.get(gameNo).getWinningDataList();
		String winningResult = "Result Awaited";
		for (int i = 0; i < winningResultList.size(); i++) {
			String[] result = winningResultList.get(i).split("=");
			Long time = Long.parseLong(result[0]);
			Timestamp t = new Timestamp(time);
			if (t.toString().split("\\.")[0].equalsIgnoreCase(drawTime)) {
				winningResult = result[1];
				/*
				 * if ("RaffleGame".equalsIgnoreCase(Util.getGameName(gameNo)))
				 * { winningResult = new
				 * RaffleHelper().swapRaffleResult(winningResult); }
				 */
			}
		}
		logger.debug("Winning Result:" + winningResult + "|");
		response.getOutputStream().write(
				("Winning Result:" + winningResult + "|").getBytes());
	}
	public TreeMap getDrawGameData() {
		return drawGameData;
	}
	public String getDrawGameNewData() {
		return drawGameNewData;
	}
	public String getDrawTime() {
		return drawTime;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getGameName() {
		return gameName;
	}
	public int getGameNo() {
		return gameNo;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public String getIdType() {
		return idType;
	}
	public String getLastName() {
		return lastName;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public String getUserName() {
		return userName;
	}
	public boolean isAutoCancel() {
		return autoCancel;
	}
	public void newData() {
		ServletContext sc = ServletActionContext.getServletContext();
		ServerStartUpData helper = new ServerStartUpData();
		helper.getUpdatedData(sc);
		//sc.setAttribute("GAME_DATA",
			//	(TreeMap<Integer, List<List>>) updatedDataList.get(1));
		//sc.setAttribute("drawIdTableMap",
				//(Map<Integer, Map<Integer, String>>) updatedDataList.get(0));
		// Util.setGameMap((TreeMap<String, Integer>) updatedDataList.get(2));
		/*
		 * logger.debug(" GAME_DATA is<<<<<<>>>>>-----" +
		 * updatedDataList.get(1));
		 * logger.debug(" drawIdTableMap<<<<<<>>>>>-----" +
		 * updatedDataList.get(0));
		 * logger.debug(" GAME_MAP is<<<<<<>>>>>-----" +
		 * updatedDataList.get(2));
		 */
	}
	public void registerPlayer() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}
		// logger.debug(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);
		logger.debug(" user name is " + userName);
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId = 0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		PWTDrawBean pwtBean = helper.registerPlayer(firstName, lastName,
				idNumber, idType,
				(PWTDrawBean) session.getAttribute("PWT_RES"), userInfoBean);
		if (pwtBean.getStatus().equals("SUCCESS")) {
			response.getOutputStream().write(
					"Registration Successful".getBytes());
		} else {
			response.getOutputStream().write("Error".getBytes());
		}
	}
	public void prizeWinningTicket() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		double taxAmt=0;
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}
		double beepAmt=Double.parseDouble((String)sc.getAttribute("AMOUNT_FOR_LONG_BEEP"));
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		MainPWTDrawBean mainPwtBean = new MainPWTDrawBean();
		String finalPwtData = null;
		mainPwtBean.setInpType(Integer.parseInt((String) sc.getAttribute("InpType")));
		mainPwtBean.setTicketNo(ticketNo);
		AjaxRequestHelper ajxHelper1 = new AjaxRequestHelper();
		ajxHelper1.getAvlblCreditAmt(userInfoBean);
		double bal1 = userInfoBean.getAvailableCreditLimit()
		- userInfoBean.getClaimableBal();
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		MainPWTDrawBean mainWinningBean = helper.prizeWinningTicket(
				mainPwtBean, userInfoBean, refMerchantId);
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId = 0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		String actionName=ActionContext.getContext().getName();
		helper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		if ("ERROR".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_FRAUD+"ErrorCode:" + EmbeddedErrors.PWT_FRAUD_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		} else if ("FRAUD".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_FRAUD+"ErrorCode:" + EmbeddedErrors.PWT_FRAUD_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return; // error
		} else if ("ERROR_INVALID"
				.equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_INVALID+"ErrorCode:" + EmbeddedErrors.PWT_INVALID_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		} else if ("CANCELLED".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_INVALID+"ErrorCode:" + EmbeddedErrors.PWT_INVALID_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;// "error";
		} else if ("UN_AUTH".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_UN_AUTH+"ErrorCode:" + EmbeddedErrors.PWT_UN_AUTH_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		} else if (mainWinningBean.getStatus() != null
				&& mainWinningBean.getStatus().equalsIgnoreCase(
				"OUT_VERIFY_LIMIT")) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		} else if ("PWT_LIMIT_EXCEED".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_LIMIT_EXCEED+"ErrorCode:" + EmbeddedErrors.PWT_LIMIT_EXCEED_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		} else if ("HIGH_PRIZE".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		} else if ("OUT_PAY_LIMIT"
				.equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		}else if ("OUT_ASSIGNED_LIMITS"
				.equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.OUT_ASSIGNED_LIMITS;
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		} else if ("TICKET_EXPIRED".equalsIgnoreCase(mainWinningBean
				.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_TICKET_EXPIRED+"ErrorCode:" + EmbeddedErrors.PWT_TICKET_EXPIRED_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		}
		StringBuilder stBuild = new StringBuilder("");
		String raffleData = "";
		if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
			double totalRaffleAmount = 0.0;
			PWTDrawBean pwtwinBean = mainWinningBean.getWinningBeanList()
			.get(0);
			RaffleDrawIdBean raffleWinningBean = (RaffleDrawIdBean) pwtwinBean
			.getRaffleDrawIdBeanList().get(0);
			String dataArr[] = buildPWTRaffleData(raffleWinningBean).split(
			"RWA*");
			String data = dataArr[0];
			if (dataArr.length > 1)
				totalRaffleAmount = Double.parseDouble(dataArr[1]);
			raffleData = raffleData + data + "|Total Pay:" + totalRaffleAmount
			+ "|";
		} else if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
			if ("FRAUD".equalsIgnoreCase(mainWinningBean.getStatus())) {
				finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_FRAUD+"ErrorCode:" + EmbeddedErrors.PWT_FRAUD_ERROR_CODE+"|";
				logger.debug("FINAL PWT DATA:" + finalPwtData);
				response.getOutputStream().write(finalPwtData.getBytes());
				return;// "error";
			} else if ("TICKET_EXPIRED".equalsIgnoreCase(mainWinningBean
					.getStatus())) {
				finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_TICKET_EXPIRED+"ErrorCode:" + EmbeddedErrors.PWT_TICKET_EXPIRED_ERROR_CODE+"|";
				logger.debug("FINAL PWT DATA:" + finalPwtData);
				response.getOutputStream().write(finalPwtData.getBytes());
				return;
			} else if (!mainWinningBean.isValid()) {
				finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_INVALID+"ErrorCode:" + EmbeddedErrors.PWT_INVALID_ERROR_CODE+"|";
				logger.debug("FINAL PWT DATA:" + finalPwtData);
				response.getOutputStream().write(finalPwtData.getBytes());
				return;
			} else if (mainWinningBean.getStatus() != null
					&& mainWinningBean.getStatus().equalsIgnoreCase(
					"OUT_VERIFY_LIMIT")) {
				finalPwtData = "ErrorMsg:"
					+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
				logger.debug("FINAL PWT DATA:" + finalPwtData);
				response.getOutputStream().write(finalPwtData.getBytes());
				return;
			} else if ("UN_AUTH".equalsIgnoreCase(mainWinningBean.getStatus())) {
				finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_UN_AUTH+"ErrorCode:" + EmbeddedErrors.PWT_UN_AUTH_ERROR_CODE+"|";
				logger.debug("FINAL PWT DATA:" + finalPwtData);
				response.getOutputStream().write(finalPwtData.getBytes());
				return;
			}
			// String raffleData=EmbeddedErrors.RAFFLE_DATA;
			double totalRaffleAmount = 0.0;
			List<PWTDrawBean> pwtWinBeanlist = mainWinningBean
			.getWinningBeanList();
			for (int i = 0; i < pwtWinBeanlist.size(); i++) {
				PWTDrawBean pwtwinBean = pwtWinBeanlist.get(i);
				if (pwtwinBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
					List<RaffleDrawIdBean> raffleDrawIdBeanList = pwtwinBean
					.getRaffleDrawIdBeanList();
					if (raffleDrawIdBeanList != null) {
						for (int j = 0; j < raffleDrawIdBeanList.size(); j++) {
							if (j > 0) {
								raffleData = raffleData + ";";
							}
							RaffleDrawIdBean raffleWinningBean = raffleDrawIdBeanList
							.get(j);
							String dataArr[] = buildPWTRaffleData(
									raffleWinningBean).split("RWA");
							String data = dataArr[0];
							if (dataArr.length > 1)
								totalRaffleAmount = Double
								.parseDouble(dataArr[1]);
							raffleData = raffleData + data;
						}
					}
				}
			}
			//raffleData = raffleData + "#";
			// StringBuilder stBuild = new StringBuilder("");
			double totTktAmt = 0.0;
			double totalClmPndAmt=0.0;
			int totRegister = 0;
			for (int k = 0; k < pwtWinBeanlist.size(); k++) {
				PWTDrawBean pwtwinBean = pwtWinBeanlist.get(k);
				taxAmt=pwtwinBean.getGovtTaxAmount();
				if (pwtwinBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
					for (int i = 0; i < pwtwinBean.getDrawWinList().size(); i++) {
						int nonWin = 0;
						int win = 0;
						int clm=0;
						int register = 0;
						int claim = 0;
						int outVerify = 0;
						int pndPay = 0;
						DrawIdBean drawBean = pwtwinBean.getDrawWinList().get(i);
						//String drawStatusForTicket=drawBean.getDrawStatusForTicket();
						String drawStatusForTicket=drawBean.getStatus();
						logger.info(drawStatusForTicket);
						String drawTime = drawBean.getDrawDateTime();
						boolean isResAwaited = false;
						boolean isExpired= false;
						boolean isVerPending=false;
						boolean isClmPending=false;
						double drawAmt = 0.0;
						List<PanelIdBean> panelWinList = drawBean
						.getPanelWinList();
						if (panelWinList != null && !drawStatusForTicket.equals("DRAW_EXPIRED") ) {
							for (int j = 0; j < panelWinList.size(); j++) {
								PanelIdBean panelBean = panelWinList.get(j);
								if (panelBean.getStatus().equals("NON WIN")) {
									nonWin++;
								} else if (panelBean.getStatus().equals(
								"NORMAL_PAY")) {
									drawAmt = panelBean.getWinningAmt()+ drawAmt;
									win++;
								} else if (panelBean.getStatus().equals(
								"CLAIMED")) {
									claim++;
								} else if (panelBean.getStatus().equals(
								"PND_PAY")) {
									pndPay++;
								} else if (panelBean.getStatus().equals(
								"HIGH_PRIZE")) {
									register++;
								} else if (panelBean.getStatus().equals(
								"OUT_PAY_LIMIT")) {
									register++;
								} else if (panelBean.getStatus().equals(
								"OUT_VERIFY_LIMIT")) {
									outVerify++;
								}else if(panelBean.getStatus().equals("CLAIM_PENDING")){
									drawAmt = panelBean.getWinningAmt()+ drawAmt;
									totalClmPndAmt+=panelBean.getWinningAmt();
									clm++;
								}
							}
						}else if(drawStatusForTicket.equals("DRAW_EXPIRED")){
										isExpired=true;
						}else if(drawStatusForTicket.equals("VERIFICATION_PENDING")){
							isVerPending=true;
						}else if(drawStatusForTicket.equals("CLAIM_PENDING")){
							isClmPending=true;
						}else{
							isResAwaited = true;
						}
						totTktAmt = (drawStatusForTicket.equals("CLAIM_PENDING")?0.0:drawAmt-taxAmt) + totTktAmt;
						if(isExpired){
							stBuild.append("|DrawTime:" + drawTime + "");
							stBuild.append(",No:0,Message:"+EmbeddedErrors.DRAW_EXP_MSG+"MsgCode:"+EmbeddedErrors.DRAW_EXP_MSG_CODE);
						}else if (isVerPending) {
							stBuild.append("|DrawTime:" + drawTime + "");
							stBuild.append(",No:0,Message:"+EmbeddedErrors.VER_PND_MSG+"MsgCode:"+EmbeddedErrors.VER_PND_MSG_CODE);
						}else if (isResAwaited) {
							stBuild.append("|DrawTime:" + drawTime + "");
							stBuild.append(",No:0,Message:"+EmbeddedErrors.AWAITED_MSG+"MsgCode:"+EmbeddedErrors.AWAITED_MSG_CODE);
						} else {
							stBuild.append("|DrawTime:" + drawTime + "");
							if (nonWin != 0) {
								// stBuild.append("\nDrawTime:" + drawTime +
								// "");
								stBuild.append(",No:" + nonWin
										+ ",Message:"+EmbeddedErrors.TRY_AGAIN_MSG+"MsgCode:"+EmbeddedErrors.TRY_AGAIN_MSG_CODE);
							}
							if (win != 0) {
								// stBuild.append("\nDrawTime:" + drawTime +
								// "");
								stBuild.append(",No:" + win + ",Message:WIN "
										+ Util.getBalInString(drawAmt) +"");
								
							}
							if (register != 0) {
								// stBuild.append("\nDrawTime:" + drawTime +
								// "");
								stBuild.append(",No:" + register
										+ ",Message:"+EmbeddedErrors.REG_REQ_MSG+"MsgCode:"+EmbeddedErrors.REG_REQ_MSG_CODE);
							}
							if (claim != 0) {
								// stBuild.append("\nDrawTime:" + drawTime +
								// "");
								stBuild.append(",No:" + claim
										+ ",Message:"+EmbeddedErrors.CLAIMED_MSG+"MsgCode:"+EmbeddedErrors.CLAIMED_MSG_CODE);
							}
							if (pndPay != 0) {
								// stBuild.append("\nDrawTime:" + drawTime +
								// "");
								stBuild.append(",No:" + pndPay
										+ ",Message:"+EmbeddedErrors.IN_PROCESS_MSG+"MsgCode:"+EmbeddedErrors.IN_PROCESS_MSG_CODE);
							}
							if (outVerify != 0) {
								// stBuild.append("\nDrawTime:" + drawTime +
								// "");
								stBuild.append(",No:" + outVerify
										+ ",Message:"+EmbeddedErrors.OUT_OF_VERIFY_MSG+"MsgCode:"+EmbeddedErrors.OUT_OF_VERIFY_MSG_CODE);
							}
							if(clm!=0){
								stBuild.append(",No:" + clm
										+ ",Message:CLM PND "+ Util.getBalInString(drawAmt-taxAmt) );
							}
						}
						totRegister = totRegister + register;
						// stBuild.append(";");
					}
				}
			}
			totTktAmt = totTktAmt + totalRaffleAmount;
			int beepVolume=-1;
			if(totTktAmt!=0){
				if(totTktAmt>=beepAmt)
					beepVolume=1;	
				 else
						beepVolume=0;
			}else if(totalClmPndAmt<=0){
				if(totalClmPndAmt>=beepAmt)
					beepVolume=1;	
				 else
					beepVolume=0;
			}
			
			stBuild.append("|Total Pay:" + Util.getBalInString(totTktAmt) + "|");
			if(totalClmPndAmt>0)
				stBuild.append("Total CLM PEND:" + Util.getBalInString(totalClmPndAmt-taxAmt) + "|");
			else
				stBuild.append("Total CLM PEND:" + Util.getBalInString(totalClmPndAmt) + "|");
			stBuild.append("BEEP VOL:" + beepVolume + "|");
			
			if (totRegister != 0) {
				stBuild.append("No:" + totRegister + ",Message:"+EmbeddedErrors.REG_REQ_MSG+EmbeddedErrors.REG_REQ_MSG_CODE+"|");
			}
		}
		session.setAttribute("PWT_RES", mainWinningBean);
		currentUserSessionMap.put(userName, session);
		// logger.debug(" STRING BUILER is before reprint " + stBuild);
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userInfoBean);
		double bal2 = userInfoBean.getAvailableCreditLimit()
		- userInfoBean.getClaimableBal();
		double bal = bal2 - bal1;
		String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);
		if (!mainWinningBean.isReprint()) {
			finalPwtData = raffleData + stBuild.toString() + "Amt:" + balance
			+ "|";
		}
		if(taxAmt>0)
			finalPwtData=finalPwtData+"Tax:"+Util.getBalInString(taxAmt) + "|";
		logger.debug("FINAL PWT DATA:" + finalPwtData);
		response.getOutputStream().write(finalPwtData.getBytes());
	}
		
	public void payPwtTicket() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		double govtCommAmt=0.0;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|")
					.getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		MainPWTDrawBean mainWinningBean = (MainPWTDrawBean) session
		.getAttribute("PWT_RES");
		AjaxRequestHelper ajxHelper1 = new AjaxRequestHelper();
		ajxHelper1.getAvlblCreditAmt(userInfoBean);
		double bal1 = userInfoBean.getAvailableCreditLimit()
		- userInfoBean.getClaimableBal();
		String highPrizeCriteria = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
		String highPrizeScheme = (String) LMSUtility.sc.getAttribute(
						"DRAW_GAME_HIGH_PRIZE_SCHEME");
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		String promoTicketDta = "";
		logger.debug("Before--" + new Date());
		//code for Payment Through mPesa ...
		if("Y".equalsIgnoreCase(mPesa) && mobileNo!=null){
			mainWinningBean.setIsmPesaEnable(true);
			mainWinningBean.setMobileNumber(mobileNo);
			UserInfoBean mPesaUserBean = new MpesaPaymentProcessHelper().payBymPesaAcc(mainWinningBean, userInfoBean);
			mainWinningBean = helper.payPwtTicket(mainWinningBean, mPesaUserBean,highPrizeCriteria,highPrizeAmt,highPrizeScheme);
		}else{
			mainWinningBean = helper.payPwtTicket(mainWinningBean, userInfoBean,highPrizeCriteria,highPrizeAmt,highPrizeScheme);
		}		
		/*
		 *  logger.debug("---------------------------- AFTER HELPER
		 * CLASS----------------------------------" + new Date());
		 */
		String finalPwtData = null;
		if ("PWT_LIMIT_EXCEED".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_LIMIT_EXCEED+"ErrorCode:" + EmbeddedErrors.PWT_LIMIT_EXCEED_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		}if ("INVALID_PWT_LIMIT_EXCEED".equalsIgnoreCase(mainWinningBean.getStatus())) {
			finalPwtData = "ErrorMsg:" + EmbeddedErrors.INVALID_PWT_LIMIT_EXCEED+"ErrorCode:" + EmbeddedErrors.INVALID_PWT_LIMIT_EXCEED_ERROR_CODE+"|";
			logger.debug("FINAL PWT DATA:" + finalPwtData);
			response.getOutputStream().write(finalPwtData.getBytes());
			return;
		}
		/*if(mainWinningBean.getWinningBeanList() != null) {
			for (int i = 0; i < mainWinningBean.getWinningBeanList().size(); i++) {
				PWTDrawBean winningBean = new PWTDrawBean();
				winningBean = mainWinningBean.getWinningBeanList().get(i);
				for(DrawIdBean drawIdBean : winningBean.getDrawWinList()) {
					if(drawIdBean.getRankId() == 4 && "TwelveByTwentyFour".equals(Util.getGameName(mainWinningBean.getGameId()))) {
						double tktCost = Double.parseDouble(drawIdBean.getWinningAmt())/2;
						for(int iLoop = 0; iLoop < 2; iLoop++) {
							double unitPrice = Util.getUnitPrice(mainWinningBean.getGameId(), "Direct12");
							int gameId = Util.getGameId("TwelveByTwentyFour");
							
							KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
							drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
							drawGamePurchaseBean.setGameId(gameId);
							drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
							drawGamePurchaseBean.setBetAmountMultiple(new int[]{(int) (tktCost/unitPrice)});
							drawGamePurchaseBean.setIsQuickPick(new int[]{1});
							drawGamePurchaseBean.setPlayerData(new String[]{"QP"});
							drawGamePurchaseBean.setPlayType(new String[]{"Direct12"});
							drawGamePurchaseBean.setNoPicked(new String[]{"12"});
							drawGamePurchaseBean.setNoOfPanel(1);
							drawGamePurchaseBean.setPartyId(userInfoBean.getUserOrgId());
							drawGamePurchaseBean.setPartyType(userInfoBean.getUserType());
							drawGamePurchaseBean.setUserId(userInfoBean.getUserId());
							drawGamePurchaseBean.setNoOfDraws(1);
							drawGamePurchaseBean.setIsAdvancedPlay(0);
							drawGamePurchaseBean.setRefMerchantId((String) sc.getAttribute("REF_MERCHANT_ID"));
							drawGamePurchaseBean.setPurchaseChannel("LMS_Terminal");
							drawGamePurchaseBean.setPlrMobileNumber("");
	//						drawGamePurchaseBean.setActionName(kenoPurchaseBean.getActionName());
	//						drawGamePurchaseBean.setLastGameId(kenoPurchaseBean.getLastGameId());
							drawGamePurchaseBean.setDeviceType("TERMINAL");
							drawGamePurchaseBean.setBarcodeType((String) LMSUtility.sc.getAttribute("BARCODE_TYPE"));
							drawGamePurchaseBean.setUserMappingId(userInfoBean.getCurrentUserMappingId());
							drawGamePurchaseBean.setDrawIdTableMap((Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap"));
							drawGamePurchaseBean.setLastSoldTicketNo("0");
							drawGamePurchaseBean.setServiceId(Util.getServiceIdFormCode("DG"));
							drawGamePurchaseBean.setPromotkt(true);
							
							KenoPurchaseBean purchaseBean = new TwelveByTwentyFourHelper().twelveByTwentyFourPurchaseTicket(userInfoBean, drawGamePurchaseBean);
							if ("SUCCESS".equalsIgnoreCase(purchaseBean.getSaleStatus())) {
								promoTicketDta += new DrawGameRPOSHelper().buildTwelveByTwentyFourData(purchaseBean, userInfoBean);
	//							orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, kenoPurchaseBean.getGame_no(), kenoPurchaseBean.getTicket_no(), purchaseBean.getTicket_no());
							} else {
	//							kenoPurchaseBean.setPromoSaleStatus("FAILED");
	//							return kenoPurchaseBean;
							}
	//						kenoPurchaseBean.setPromoPurchaseBean(purchaseBean);
						}
					}
				}
			}
		}*/
		
		String raffleData = "";
		StringBuilder stBuild = new StringBuilder("");
		double totalRaffleAmount = 0.0;
		if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
			List<RaffleDrawIdBean> raffleDrawIdBeanList = mainWinningBean
			.getWinningBeanList().get(0).getRaffleDrawIdBeanList();
			if (raffleDrawIdBeanList != null) {
				for (int j = 0; j < raffleDrawIdBeanList.size(); j++) {
					if (j > 0) {
						raffleData = raffleData + ";";
					}
					RaffleDrawIdBean raffleWinningBean = raffleDrawIdBeanList
					.get(j);
					String dataArr[] = buildPWTRaffleData(raffleWinningBean)
					.split("RWA");
					String data = dataArr[0];
					if (dataArr.length > 1)
						totalRaffleAmount = Double.parseDouble(dataArr[1]);
					raffleData = raffleData + data;
				}
			}
			raffleData = raffleData + "#" + "|Total Pay:" + totalRaffleAmount
			+ "|";
		} else if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
			// String raffleData=EmbeddedErrors.RAFFLE_DATA;
			// double totalRaffleAmount=0.0;
			List<PWTDrawBean> pwtWinBeanlist = mainWinningBean
			.getWinningBeanList();
			for (int k = 0; k < pwtWinBeanlist.size(); k++) {
				PWTDrawBean pwtWinningBean = pwtWinBeanlist.get(k);
				if (pwtWinningBean.getPwtTicketType()
						.equalsIgnoreCase("RAFFLE")) {
					List<RaffleDrawIdBean> raffleDrawIdBeanList = pwtWinningBean
					.getRaffleDrawIdBeanList();
					for (int i = 0; i < raffleDrawIdBeanList.size(); i++) {
						if (i > 0) {
							raffleData = raffleData + ";";
						}
						RaffleDrawIdBean raffleWinningBean = raffleDrawIdBeanList
						.get(i);
						if ("FRAUD".equalsIgnoreCase(raffleWinningBean
								.getStatus())) {
							raffleData = raffleData + "ErrorMsg:"
							+ EmbeddedErrors.PWT_FRAUD;
						} else if ("TICKET_EXPIRED"
								.equalsIgnoreCase(raffleWinningBean.getStatus())) {
							raffleData = raffleData + "ErrorMsg:"
							+ EmbeddedErrors.PWT_TICKET_EXPIRED+"ErrorCode:" + EmbeddedErrors.PWT_TICKET_EXPIRED_ERROR_CODE+"|";
						} else if (raffleWinningBean.getStatus().equals(
						"RES_AWAITED")) {
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ ",No:0,Message:Awaited";
						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus()
								.equalsIgnoreCase("OUT_VERIFY_LIMIT")) {
							raffleData = raffleData + "ErrorMsg:"
							+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"NON_WIN")) {
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ "|TRY AGAIN";
						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"NORMAL_PAY")) {
							totalRaffleAmount = totalRaffleAmount
							+ Double.parseDouble(raffleWinningBean
									.getWinningAmt());
							raffleData = EmbeddedErrors.RAFFLE_DATA;
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ "*Promotional Draw,No:1,Message:WIN "
							+ raffleWinningBean.getWinningAmt();
						} else if (raffleWinningBean.getStatus() != null
								&& raffleWinningBean.getStatus().equals(
								"CLAIMED")) {
							raffleData = raffleData
							+ raffleWinningBean.getDrawDateTime()
							+ "*Promotional Draw,No:1,Message:CLAIMED";
						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"PND_PAY")) {
							raffleData = raffleData + "ErrorMsg:"
							+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"HIGH_PRIZE")) {
							raffleData = raffleData + "ErrorMsg:"
							+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"OUT_PAY_LIMIT")) {
							raffleData = raffleData + "ErrorMsg:"
							+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
						} else if (raffleWinningBean.getPwtStatus() != null
								&& raffleWinningBean.getPwtStatus().equals(
								"OUT_VERIFY_LIMIT")) {
							raffleData = raffleData + "ErrorMsg:"
							+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
						}
					}
				}
			}
			//raffleData = raffleData + "#";
			// StringBuilder stBuild = new StringBuilder("");
			double totalClmPndAmt=0.0;
			for (int k = 0; k < pwtWinBeanlist.size(); k++) {
				PWTDrawBean pwtWinningBean = pwtWinBeanlist.get(k);
				govtCommAmt=pwtWinningBean.getGovtTaxAmount();
				if (pwtWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
					double totTktAmt = 0.0;
					int totRegister = 0;
					if (!pwtWinningBean.isValid()) {
						stBuild.append("\nMsg:Invalid Ticket");
					} else if (pwtWinningBean.getStatus().equals("SUCCESS")) {
						for (int i = 0; i < pwtWinningBean.getDrawWinList()
						.size(); i++) {
							int nonWin = 0;
							int win = 0;
							int clm=0;
							int register = 0;
							int claim = 0;
							int outVerify = 0;
							int pndPay = 0;
							boolean isExpired= false;
							boolean isVerPending=false;
							boolean isClmPending=false;
							DrawIdBean drawBean = pwtWinningBean
							.getDrawWinList().get(i);
							String drawStatusForTicket=drawBean.getStatus();
							String drawTime = drawBean.getDrawDateTime();
							String gameName = Util.getGameName(pwtWinningBean
									.getGameNo());
							String[] drawTimeArr = drawTime.split("\\*");
							if (drawTimeArr.length > 1) {
								if (drawTimeArr[1] == null
										|| "NULL"
										.equalsIgnoreCase(drawTimeArr[1])) {
									drawTime = drawTimeArr[0];
								}
							}
							boolean isResAwaited = false;
							double drawAmt = 0.0;
							List<PanelIdBean> panelWinList = drawBean
							.getPanelWinList();
							if (panelWinList != null && !drawStatusForTicket.equals("DRAW_EXPIRED")) {
								for (int j = 0; j < panelWinList.size(); j++) {
									PanelIdBean panelBean = panelWinList.get(j);
									if (panelBean.getStatus().equals("NON WIN")) {
										nonWin++;
									} else if (panelBean.getStatus().equals(
									"NORMAL_PAY")) {
										drawAmt = panelBean.getWinningAmt()
										+ drawAmt;
										win++;
									} else if (panelBean.getStatus().equals(
									"CLAIMED")) {
										claim++;
									} else if (panelBean.getStatus().equals(
									"PND_PAY")) {
										pndPay++;
									} else if (panelBean.getStatus().equals(
									"HIGH_PRIZE")) {
										register++;
									} else if (panelBean.getStatus().equals(
									"OUT_PAY_LIMIT")) {
										register++;
									} else if (panelBean.getStatus().equals(
									"OUT_VERIFY_LIMIT")) {
										outVerify++;
									}else if(panelBean.getStatus().equals("CLAIM_PENDING")){
										drawAmt = panelBean.getWinningAmt()+ drawAmt;
										totalClmPndAmt+=panelBean.getWinningAmt();
										clm++;
									}
								}
							}else if(drawStatusForTicket.equals("DRAW_EXPIRED")){
								isExpired=true;
							}else if(drawStatusForTicket.equals("VERIFICATION_PENDING")){
								isVerPending=true;
							}else if(drawStatusForTicket.equals("CLAIM_PENDING")){
								isClmPending=true;
							}else{
								isResAwaited = true;
							}
							totTktAmt = (drawStatusForTicket.equals("CLAIM_PENDING")?0.0:drawAmt-govtCommAmt) + totTktAmt;
							if(isExpired){
								stBuild.append("|DrawTime:" + drawTime + "");
								stBuild.append(",No:0,Message:Draw Expired");
							}else if (isVerPending) {
								stBuild.append("|DrawTime:" + drawTime + "");
								stBuild.append(",No:0,Message:VER PEND");
							}else if (isResAwaited) {
								stBuild.append("|DrawTime:" + drawTime + "");
								stBuild.append(",No:0,Message:Awaited");
							} else {
								/* As per clients' requirement, it's commented to omit the non-win data 
								 *  to reduce the size of ticket and to show only relevant data.
								 */
								stBuild.append("|DrawTime:" + drawTime + "");
								if (nonWin != 0) {
									//stBuild.append(",No:" + nonWin + ",Message:TRY AGAIN");
									continue;
								}
								
								if (win != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + win + ",Message:WIN "
											+ Util.getBalInString(drawAmt) +"");
								}
								if (register != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + register
											+ ",Message:REG. REQ.");
								}
								if (claim != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + claim
											+ ",Message:CLAIMED");
								}
								if (pndPay != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + pndPay
											+ ",Message:IN PROCESS");
								}
								if (outVerify != 0) {
									// stBuild.append("\nDrawTime:" + drawTime +
									// "");
									stBuild.append(",No:" + outVerify
											+ ",Message:OUT OF VERIFY");
								}
								if(clm!=0){
									stBuild.append(",No:" + clm
											+ ",Message:CLM PND "+ Util.getBalInString(drawAmt-govtCommAmt));
								}
							}
							totRegister = totRegister + register;
						}
					} else if (pwtWinningBean.getStatus().equals("ERROR")) {
						finalPwtData = "ErrorMsg:" + EmbeddedErrors.PWT_ERROR+"ErrorCode:" + EmbeddedErrors.PWT_ERROR_CODE+"|";
						logger.debug("FINAL PWT DATA:" + finalPwtData);
						response.getOutputStream().write(
								finalPwtData.getBytes());
						return;
					}
					totTktAmt = totTktAmt + totalRaffleAmount;
 
					stBuild.append("|Total Pay:" + Util.getBalInString(totTktAmt) + "|");
					if(totalClmPndAmt>0)
						stBuild.append("Total CLM PEND:" + Util.getBalInString(totalClmPndAmt-govtCommAmt) + "|");
					else
						stBuild.append("Total CLM PEND:" + Util. getBalInString(totalClmPndAmt) + "|");
					if (totRegister != 0) {
						stBuild.append("No:" + totRegister
								+ ",Message:Reg. Req");
					}
					// session.setAttribute("PWT_RES", pwtWinningBean);
					currentUserSessionMap.put(userName, session);
					/*
					 * logger.debug(" STRING BUILER is before reprint " +
					 * stBuild);
					 * logger.debug("--------------------------GAME NUMBER "
					 * + pwtWinningBean.getGameNo());
					 */
				}
			}
		}
		AjaxRequestHelper ajxHelper2 = new AjaxRequestHelper();
		ajxHelper2.getAvlblCreditAmt(userInfoBean);
		double bal2 = userInfoBean.getAvailableCreditLimit()
		- userInfoBean.getClaimableBal();
		double bal = bal2 - bal1;
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		boolean isPromoPrint = false;
		if (mainWinningBean.isReprint()) {
			isPromoPrint = true;
			Object Purchasebean = mainWinningBean.getPurchaseBean();
			if (Purchasebean instanceof FortunePurchaseBean) {
				int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gamenbr);
				FortunePurchaseBean bean = (FortunePurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintFortuneTicket(bean, gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());
				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			} else if (Purchasebean instanceof LottoPurchaseBean) {
				int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gamenbr);
				LottoPurchaseBean bean = (LottoPurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintLottoTicket(bean, gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());
				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			} else if (Purchasebean instanceof KenoPurchaseBean) {
				int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gamenbr);
				KenoPurchaseBean bean = (KenoPurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintKenoTicket(bean, gameName,userInfoBean.getTerminalBuildVersion(),countryDeployed));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());
				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			} else if (Purchasebean instanceof FortuneTwoPurchaseBean) {
				int gameNbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gameNbr);
				FortuneTwoPurchaseBean bean = (FortuneTwoPurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild
				.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintFortuneTwoTicket(bean,
								gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());
				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			}else if (Purchasebean instanceof FortuneThreePurchaseBean) {
				int gameNbr = helper.getGamenoFromTktnumber(mainWinningBean
						.getTicketNo());
				String gameName = Util.getGameName(gameNbr);
				FortuneThreePurchaseBean bean = (FortuneThreePurchaseBean) mainWinningBean
				.getPurchaseBean();
				stBuild
				.append(promoTicketDta + "RPRT:"
						+ ReprintHepler.reprintFortuneThreeTicket(bean,
								gameName));
				String raffleDataRPRT = CommonMethods.buildRaffleData(bean
						.getRafflePurchaseBeanList());
				finalPwtData = raffleData + stBuild.toString() + "|Amt:"
				+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
				+ raffleDataRPRT;
			}
		} else {
			finalPwtData = raffleData + stBuild.toString() + "Amt:" + balance
			+ "|";
		}
		//Mobile No:1111213132|Ref No:123135458|     to be added ...
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";
		UtilApplet.getAdvMsgs(mainWinningBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);
		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}
		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}
		finalPwtData = finalPwtData + advtMsg + ((isPromoPrint)?"":promoTicketDta);
		if(govtCommAmt>0)
			finalPwtData=finalPwtData+"Tax:"+Util.getBalInString(govtCommAmt)+"|";
		if(!"|DrawTime:".equalsIgnoreCase(finalPwtData.substring(0,10)))
			finalPwtData="|DrawTime:"+finalPwtData;
		logger.debug("FINAL PWT DATA:" + finalPwtData);
		response.getOutputStream().write(finalPwtData.getBytes());
		return;
	}
	public String buildPWTRaffleData(RaffleDrawIdBean raffleWinningBean) {
		// RaffleDrawIdBean
		// raffleWinningBean=(RaffleDrawIdBean)pwtWinningBean.getRaffleDrawIdBeanList().get(0);
		double totalRaffleAmount = 0.0;
		if ("FRAUD".equalsIgnoreCase(raffleWinningBean.getStatus())) {
			return "ErrorMsg:" + EmbeddedErrors.PWT_FRAUD+"ErrorCode:" + EmbeddedErrors.PWT_FRAUD_ERROR_CODE+"|";
		} else if ("TICKET_EXPIRED".equalsIgnoreCase(raffleWinningBean
				.getStatus())) {
			return "ErrorMsg:" + EmbeddedErrors.PWT_TICKET_EXPIRED+"ErrorCode:" + EmbeddedErrors.PWT_TICKET_EXPIRED_ERROR_CODE+"|";
		} else if (raffleWinningBean.getStatus().equals("RES_AWAITED")) {
			return raffleWinningBean.getDrawDateTime() + "|Awaited";
		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equalsIgnoreCase(
				"OUT_VERIFY_LIMIT")) {
			return "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
		} else if (raffleWinningBean.getStatus() != null
				&& raffleWinningBean.getStatus().equals("NON_WIN")) {
			return raffleWinningBean.getDrawDateTime() + "|TRY AGAIN";
		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("NORMAL_PAY")) {
			totalRaffleAmount = totalRaffleAmount
			+ Double.parseDouble(raffleWinningBean.getWinningAmt());
			return raffleWinningBean.getDrawDateTime() + "|WIN "
			+ raffleWinningBean.getWinningAmt() + "" + "RWA"
			+ totalRaffleAmount;
		} else if (raffleWinningBean.getStatus() != null
				&& raffleWinningBean.getStatus().equals("CLAIMED")) {
			return raffleWinningBean.getDrawDateTime() + "|CLAIMED";
		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("PND_PAY")) {
			return "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("HIGH_PRIZE")) {
			return "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("OUT_PAY_LIMIT")) {
			return "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
		} else if (raffleWinningBean.getPwtStatus() != null
				&& raffleWinningBean.getPwtStatus().equals("OUT_VERIFY_LIMIT")) {
			return "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT+"ErrorCode:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE+"|";
		}
		return "";
	}

	public void reprintTicket() throws Exception {
		try{
			servletContext = ServletActionContext.getServletContext();
			isDrawAvailable = (String) servletContext.getAttribute("IS_DRAW");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			if ("NO".equalsIgnoreCase(isDrawAvailable)) {
				reprintIfDrawNotAvailable();
			} else{
				reprintIfDrawAvailable(servletContext);
			}
			logger.debug("FINAL REPRINT DATA:" + finalReprintData);
		}catch(Exception e){
			logger.error("Error Occurred:", e);
			finalReprintData = "ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL+"ErrorCode:" + EmbeddedErrors.REPRINT_FAIL_ERROR_CODE+"|";
		}
		response.getOutputStream().write(finalReprintData.getBytes());
	}

	private void reprintIfDrawNotAvailable() throws IOException {
		finalReprintData = "ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE+"ErrorCode:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE_ERROR_CODE+"|";
	}
	
	private void reprintIfDrawAvailable(ServletContext servletContext) {
		try{
			getRequiredParametersForReprint(servletContext);
			rposHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refernceMerchantId,autoCancelHoldDays,actionName, gameId);
			gameBean = rposHelper.reprintTicket(userInfoBean, false, null,true,null,"TERMINAL");
			gameBeanType = gameBean.getClass().getSimpleName();
			setUserBalance();
			prepareEmbeddedReprintBean();
			fetchFinalReprintData();
		} catch(LMSException l){
			finalReprintData = "ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL+"ErrorCode:" + EmbeddedErrors.REPRINT_FAIL_ERROR_CODE+"|";
		} catch(Exception e){
			logger.error("Error Occurred:", e);
			finalReprintData = "ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL+"ErrorCode:" + EmbeddedErrors.REPRINT_FAIL_ERROR_CODE+"|";
		}
	}
	
	private void getRequiredParametersForReprint(ServletContext servletContext) {
		countryDeployed = (String)servletContext.getAttribute("COUNTRY_DEPLOYED");
		currentUserSessionMap = (Map)servletContext.getAttribute("LOGGED_IN_USERS");
		session = (HttpSession)currentUserSessionMap.get(userName);
		userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
		refernceMerchantId = (String) servletContext.getAttribute("REF_MERCHANT_ID");
		autoCancelHoldDays = Integer.parseInt((String) servletContext.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		if(LSTktNo != 0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		actionName = ActionContext.getContext().getName();
	}

	private void setUserBalance() {
		userBalance = userInfoBean.getAvailableCreditLimit() - userInfoBean.getClaimableBal();
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);
		balance =  numberFormat.format(userBalance).replace(",", "");
	}

	private void prepareEmbeddedReprintBean() {
		embeddedReprint = new EmbeddedReprint();
		embeddedReprint.setBalance(balance);
		embeddedReprint.setCountryDeployed(countryDeployed);
		embeddedReprint.setGameBean(gameBean);
		embeddedReprint.setUserInfoBean(userInfoBean);
		if("KenoPurchaseBean".equalsIgnoreCase(gameBeanType)){
			embeddedReprint.setRaffleDrawDay((String) servletContext.getAttribute("RAFFLE_GAME_DRAW_DAY"));
			embeddedReprint.setRaffleGameDataString((String) servletContext.getAttribute("RAFFLE_GAME_DATA"));
		}
	}

	private void fetchFinalReprintData() {
		ReprintContext reprintContext = null;
		try{
			reprintContext = reprintFactory.fetchReprintGameTypeInstance(gameBeanType);
			finalReprintData = reprintContext.reprintTicket(embeddedReprint);
		}catch(LMSException l){
			finalReprintData = "ErrorMsg:" + l.getErrorMessage()+"ErrorCode:" + l.getErrorCode()+"|";
		}
	}

	
	public String buildPromoReprintData(Object PromoPurchaseBean,UserInfoBean userInfoBean,String countryDeployed)
	throws Exception {
		
			if (PromoPurchaseBean instanceof FortunePurchaseBean) {
				FortunePurchaseBean fortunePurchaseBean = (FortunePurchaseBean) PromoPurchaseBean;
				String promoGameName = Util.getGameName(fortunePurchaseBean
						.getGame_no());
				return "PromoTkt:"
				+ ReprintHepler.reprintFortuneTicket(fortunePurchaseBean,
						promoGameName) + "QP:"
						+ fortunePurchaseBean.getIsQuickPick()[0] + "|";
			} else if (PromoPurchaseBean instanceof LottoPurchaseBean) {
				LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) PromoPurchaseBean;
				String promoGameName = Util.getGameName(lottoPurchaseBean
						.getGame_no());
				return "PromoTkt:"
				+ ReprintHepler.reprintLottoTicket(lottoPurchaseBean,
						promoGameName) + "QP:"
						+ lottoPurchaseBean.getIsQuickPick()[0] + "|";
			} else if (PromoPurchaseBean instanceof KenoPurchaseBean) {
				KenoPurchaseBean kenoPurchaseBean = (KenoPurchaseBean) PromoPurchaseBean;
				String promoGameName = Util.getGameName(kenoPurchaseBean
						.getGame_no());
				return "PromoTkt:"
				+ ReprintHepler.reprintKenoTicket(kenoPurchaseBean,
						promoGameName,userInfoBean.getTerminalBuildVersion(),countryDeployed) + "QP:"
						+ kenoPurchaseBean.getIsQuickPick()[0] + "|";
			} else if (PromoPurchaseBean instanceof FortuneTwoPurchaseBean) {
				FortuneTwoPurchaseBean fortuneTwoPurchaseBean = (FortuneTwoPurchaseBean) PromoPurchaseBean;
				String promoGameName = Util.getGameName(fortuneTwoPurchaseBean
						.getGame_no());
				return "PromoTkt:"
				+ ReprintHepler.reprintFortuneTwoTicket(
						fortuneTwoPurchaseBean, promoGameName) + "QP:"
						+ fortuneTwoPurchaseBean.getIsQuickPick()[0] + "|";
			} else if (PromoPurchaseBean instanceof FortuneThreePurchaseBean) {
				FortuneThreePurchaseBean fortuneThreePurchaseBean = (FortuneThreePurchaseBean) PromoPurchaseBean;
				String promoGameName = Util.getGameName(fortuneThreePurchaseBean
						.getGame_no());
				return "PromoTkt:"
				+ ReprintHepler.reprintFortuneThreeTicket(
						fortuneThreePurchaseBean, promoGameName) + "QP:"
						+ fortuneThreePurchaseBean.getIsQuickPick()[0] + "|";
			} else if (PromoPurchaseBean instanceof List) {
				List<RafflePurchaseBean> rafflePurchaseBeanList = (List<RafflePurchaseBean>) PromoPurchaseBean;
				return CommonMethods.buildRaffleData(rafflePurchaseBeanList);
			}
			return "";
		}
	
		public void fetchUnsoldTkts(){
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			helper.getUnsoldTkts(gameNo, startDate, endDate);
		}
	/**
	 * This Method fetch Game Info for terminal version greater or equal to 9.5
	 *@param version Terminal Version
	 * @author Neeraj Jain
	 * @return Response Success/ErrorMsg
	 */
		public void	fetchLoginDrawGameData(){
		
			try {
				
				logger.debug("--------In  Fetch Login Draw Game Data Action for EMBEDDED--------");
				logger.debug(" userName:"+userName+"Version:"+version);
				ServletContext sc = ServletActionContext.getServletContext();
				Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
				if (currentUserSessionMap == null) {
					try {
						response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED+"ErrorCode:"+EmbeddedErrors.SESSION_EXPIRED_ERROR_CODE+"|")
								.getBytes());
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
				if (!CommonFunctionsHelper.isSessionValid(session)) {
					response.getOutputStream().write(
									("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED+"ErrorCode:"+EmbeddedErrors.SESSION_EXPIRED_ERROR_CODE+"|")
											.getBytes());
					return;
				}
				UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
				String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
				int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
				long lastPrintedTicket=0;
				int gameId = 0;
				if(LSTktNo !=0){
					lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
					gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				}
				double minTerminalVersion = Double.parseDouble( (String) sc.getAttribute("MIN_TERMINAL_VERSION"));
				String actionName=ActionContext.getContext().getName();
				DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
				drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,gameId);
				boolean isOfflineUser = false;
				if(version < minTerminalVersion){
					response.getOutputStream().write(("ErrorMsg:" + "Please upgrade the terminal version").getBytes());
				}
				else{
					String gameInfo = "GameInfo:"+ DrawGameRPOSHelper.embdDgData(isOfflineUser,
								(Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap"),
								userInfoBean.getUserId(), userInfoBean.getUserOrgId(), version);
					String drawLimit = Utility.getPropertyValue("DRAW_LIMIT");
					gameInfo += "|drawLimit:"+drawLimit;
					if("NO_ENTRY".equalsIgnoreCase(gameInfo.substring(gameInfo.indexOf(":")+1))) 
					{
						response.getOutputStream().write(("ErrorMsg:" + "Please upgrade the terminal version").getBytes());
					}
					else
					{
						response.getOutputStream().write((gameInfo).getBytes());
					}
				}
				
				
			}
			catch(Exception e){			
				e.printStackTrace();
				try {
					response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA+"ErrorCode:"+EmbeddedErrors.PURCHSE_INVALID_DATA_ERROR_CODE+"|").getBytes());
				} catch (IOException e1) {
					logger.debug("Error In Setting Response");
					e1.printStackTrace();
				}
			}
				
			
			
			
		}
			
		public void setAutoCancel(boolean autoCancel) {
			this.autoCancel = autoCancel;
		}
	
		public void setDrawGameData(TreeMap drawGameData) {
			this.drawGameData = drawGameData;
		}
	
		public void setDrawGameNewData(String drawGameNewData) {
			this.drawGameNewData = drawGameNewData;
		}
	
		public void setDrawTime(String drawTime) {
			this.drawTime = drawTime;
		}
	
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
	
		public void setGameName(String gameName) {
			this.gameName = gameName;
		}
	
		public void setGameNo(int gameNo) {
			this.gameNo = gameNo;
		}
	
		public void setIdNumber(String idNumber) {
			this.idNumber = idNumber;
		}
	
		public void setIdType(String idType) {
			this.idType = idType;
		}
	
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	
		public void setServletRequest(HttpServletRequest servletRequest) {
			this.servletRequest = servletRequest;
		}
	
		public void setServletResponse(HttpServletResponse response) {
			this.response = response;
		}
	
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
	
		public void setTicketNo(String ticketNo) {
			this.ticketNo = ticketNo;
		}
	
		public void setUserName(String userName) {
			this.userName = userName;
		}
	
		public String getIsNewURL() {
			return isNewURL;
		}
	
		public void setIsNewURL(String isNewURL) {
			this.isNewURL = isNewURL;
		}
	
		public String getMobileNo() {
			return mobileNo;
		}
	
		public String getmPesa() {
			return mPesa;
		}
	
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}
	
		public void setmPesa(String mPesa) {
			this.mPesa = mPesa;
		}
	
		public long getLSTktNo() {
			return LSTktNo;
		}
	
		public void setLSTktNo(long lSTktNo) {
			LSTktNo = lSTktNo;
		}
	
		public String getEndDate() {
			return endDate;
		}
	
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
	
		public double getVersion() {
			return version;
		}
	
		public void setVersion(double version) {
			this.version = version;
		}
	
		public void setCancelType(String cancelType) {
			this.cancelType = cancelType;
		}
	
		public String getCancelType() {
			return cancelType;
		}
	
		public String getCurDate() {
			return curDate;
		}
	
		public void setCurDate(String curDate) {
			this.curDate = curDate;
		}


	
}