package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.KenoFiveHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class KenoFiveAction extends ActionSupport implements ServletRequestAware , ServletResponseAware {

	static Log logger = LogFactory.getLog(KenoFiveAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final List numbers = Arrays.asList("", "Zero(0)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
			"Eight(8)", "Nine(9)");

	public static void main(String[] args) throws Exception {
		new KenoAction().purchaseTicketProcess();
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
	private int gameId = Util.getGameId("KenoFive");
	private int isAdvancedPlay;
	private KenoPurchaseBean kenoPurchaseBean;
	private int noOfDraws;
	private int noOfLines;
	private String noPicked;
	private String pickedNum;
	private String pickedNumbers;
	private String playType;
	private long LSTktNo;
	private String QP;
	private String plrMobileNumber;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private String totalPurchaseAmt;

	private String userName;

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
		return kenoPurchaseBean;
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
	
	

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public void purchaseTicketProcess() {
		ServletContext sc = ServletActionContext.getServletContext();
		try {
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
		
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		//int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
		
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
		drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
		drawGamePurchaseBean.setBetAmountMultiple(betAmtArr);
		drawGamePurchaseBean.setIsQuickPick(QPArr);
		drawGamePurchaseBean.setPlayerData(pickedNumbersArr);
		drawGamePurchaseBean.setNoPicked(noPickedArr);
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setNoOfDraws(noOfDraws);
		drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		long lastPrintedTicket=0;
		int lstGameId =0;
		
		int serviceId = Util.getServiceIdFormCode(request.getAttribute("code").toString());
		if(serviceId==0 || userBean.getCurrentUserMappingId() == 0){
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE ,  LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		drawGamePurchaseBean.setLastSoldTicketNo(lastPrintedTicket+"");
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setBonus("N");
		drawGamePurchaseBean.setPlayType(playTypeArr);
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setNoOfPanel(noOfPanel);
		drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
		String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
		drawGamePurchaseBean.setBarcodeType(barcodeType);
		drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
		drawGamePurchaseBean.setServiceId(serviceId);
		
		String actionName=ActionContext.getContext().getName();
		
		drawGamePurchaseBean.setActionName(actionName);
		drawGamePurchaseBean.setLastGameId(lstGameId);
		drawGamePurchaseBean.setDeviceType("TERMINAL");

		
		KenoFiveHelper helper = new KenoFiveHelper();
		// new DrawGameRPOSHelper().checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
		/*logger.debug("SALE_AUTO_CANCEL_LOGS:" + "lastSoldTktLMS : " + lastSoldTktLMS + " :lastSoldTicketNo " + lastSoldTicketNo);
        if(lastSoldTktLMS != Long.parseLong(lastSoldTicketNo) && lastSoldTktLMS != 0 && !"0".equals(LSTktNo) && LSTktNo!=null){
			logger.debug("SALE_AUTO_CANCEL_LOGS:" + "Inside Auto Cancellation if");
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
			logger.debug("SALE_AUTO_CANCEL_LOGS:" + "is cancelled " + cancelTicketBean.isValid() + " :Ticket_number" + lastSoldTktLMS);
		}
		logger.debug("SALE_AUTO_CANCEL_LOGS:" + "SALE Continue for the new ticket");*/
		
		String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");
		if ("NIGERIA".equals(countryDeployed)) {
			SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");

			String bettypeOffStartTime = (String) sc.getAttribute("BETTYPE_OFF_START_TIME");
			Date start = parser.parse(bettypeOffStartTime);
			// logger.info("BETTYPE_OFF_START_TIME - " + start);

			String bettypeOffEndTime = (String) sc.getAttribute("BETTYPE_OFF_END_TIME");
			Date end = parser.parse(bettypeOffEndTime);
			// logger.info("BETTYPE_OFF_END_TIME - " + end);

			Date userDate = parser.parse(parser.format(new Date()));
			// logger.info(userDate);
			if (userDate.after(start) && userDate.before(end)) {
				try {
					for (String betType : playTypeArr) {
						if ("Direct4".equals(betType) || "Direct5".equals(betType) || "MN-Direct5".equals(betType) || "MN-Direct4".equals(betType)) {
							response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
							logger.info("ErrorMsg:No Direct4 and Direct5 Bet Type Till " + bettypeOffEndTime);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
					logger.info("ErrorMsg:Some Internal Error " + bettypeOffEndTime);
					return;
				}
			}
			
			String newBetypeOffStartTime = (String) sc.getAttribute("NEW_BETTYPE_OFF_START_TIME");
			Date newStart = parser.parse(newBetypeOffStartTime);
			// logger.info("NEW_BETTYPE_OFF_START_TIME - " + start);

			String newBetypeOffEndTime = (String) sc.getAttribute("NEW_BETTYPE_OFF_END_TIME");
			Date newEnd = parser.parse(newBetypeOffEndTime);
			// logger.info("NEW_BETTYPE_OFF_END_TIME - " + end);

			// logger.info(userDate);
			if (userDate.after(newStart) && userDate.before(newEnd)) {
				try {
					for (String betType : playTypeArr) {
						if ("4By90-Direct2".equals(betType) || "3By90-Direct2".equals(betType) || "2By90-Direct2".equals(betType) || "4By90-Perm2".equals(betType) || "3By90-Perm2".equals(betType) || "2By90-Perm2".equals(betType)) {
							response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
							logger.info("ErrorMsg:No 4/90 3/90 2/90  Bet Type Till " + bettypeOffEndTime);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
					logger.info("ErrorMsg:Some Internal Error " + bettypeOffEndTime);
					return;
				}
			}


			String lagosExtendedBetStartTime = (String) sc.getAttribute("LAGOS_EXTENDED_BET_START_TIME");
			start = parser.parse(lagosExtendedBetStartTime);
			// logger.info("LAGOS_EXTENDED_BET_START_TIME - " + start);

			String lagosExtendedBetEndTime = (String) sc.getAttribute("LAGOS_EXTENDED_BET_END_TIME");
			end = parser.parse(lagosExtendedBetEndTime);
			// logger.info("LAGOS_EXTENDED_BET_END_TIME - " + end);
			if (userDate.after(start) && userDate.before(end)) {
				double lagosExtendedBetAmount = Integer.parseInt((String) sc.getAttribute("LAGOS_EXTENDED_BET_AMOUNT"));
				try {
					for (String betType : playTypeArr) {
						if(("Direct2".equals(betType) || "DC-Direct2".equals(betType) || "Perm2".equals(betType) || "DC-Perm2".equals(betType) || "MN-Perm2".equals(betType) || "MN-Direct2".equals(betType)) && Double.parseDouble(totalPurchaseAmt) > lagosExtendedBetAmount) {
							response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
							logger.info("ErrorMsg:No Direct2 and Perm2 Bet Type Till " + lagosExtendedBetEndTime + " For Amount " + lagosExtendedBetAmount);
							return;
						}
						if("Direct1".equals(betType) || "Direct3".equals(betType) || "DC-Direct3".equals(betType) || "Perm3".equals(betType) || "DC-Perm3".equals(betType) || "Banker".equals(betType) || "Banker1AgainstAll".equals(betType) || "MN-Banker".equals(betType) || "MN-Banker1AgainstAll".equals(betType) || "MN-Perm3".equals(betType) || "MN-Direct3".equals(betType) || "MN-Direct1".equals(betType)) {
							response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
							logger.info("ErrorMsg:No Direct3 and Perm3 Bet Type Till " + lagosExtendedBetEndTime);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
					logger.info("ErrorMsg:Some Internal Error " + bettypeOffEndTime);
					return;
				}
			}
		}

		kenoPurchaseBean = helper.commonPurchseProcess(userBean,
				drawGamePurchaseBean);
		setKenoPurchaseBean(kenoPurchaseBean);
		String finalPurchaseData = null;

		String saleStatus = getKenoPurchaseBean().getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)
					+ "|";
			System.out.println("FINAL PURCHASE DATA KENO:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}

		/*logger.debug(kenoPurchaseBean.getPlayerPicked() + "msg---------"
				+ kenoPurchaseBean.getTicket_no());*/

		String time = kenoPurchaseBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");

		
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		
		String balance = nf.format(bal).replaceAll(",", "");
		
		//String balance = bal + "00";
		//balance = balance.substring(0, balance.indexOf(".") + 3);
		int listSize = kenoPurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		
			for (int i = 0; i < listSize; i++) {
				String[] drawDateTimeNameId = kenoPurchaseBean.getDrawDateTime().get(i).split("\\&");
				String drawId = drawDateTimeNameId[1];
				String[] drawDateTimeName = drawDateTimeNameId[0].split("\\*");
				String drawDate = drawDateTimeName[0];
				String drawName = null;
				if(drawDateTimeName.length > 1)
					drawName = drawDateTimeName[1];
				drawTimeBuild.append("|DrawDate:" + (Util.changeFormat("dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", drawDate)).replaceFirst(" ", "#").replace("#", "|DrawTime:"));
				if(drawName != null)
					drawTimeBuild.append("*"+drawName);
				drawTimeBuild.append("|DrawId:"+drawId);
			}
		
		

		StringBuilder finalData = new StringBuilder("");

		// logger.debug(kenoPurchaseBean.getPlayType());
		// helper.insertLastSoldTicket(userBean.getUserOrgId(), kenoPurchaseBean.getTicket_no());
		
		
		// FOR BACKWARD COMPATIBILITY 
		if(userBean.getTerminalBuildVersion() < Double.parseDouble(Utility.getPropertyValue("CURRENT_TERMINAL_BUILD_VERSION")) && "NIGERIA".equals(countryDeployed))
			finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()).length()==ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired?kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);			
		else
			finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+"|brCd:"+kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);
				

		
		/*if(userBean.getTerminalBuildVersion()>=ConfigurationVariables.currentTerminalBuildVersion)
		finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+"|brCd:"+kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);
		else
		finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()).length()==ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired?kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);	
*/
		int noOfPanels = kenoPurchaseBean.getNoOfPanel();
		String[] playTypeStr = kenoPurchaseBean.getPlayType();
		for (int i = 0; i < noOfPanels; i++) {			
			
			String panelPrice = "|PanelPrice:" + nf.format(kenoPurchaseBean.getBetAmountMultiple()[i]* kenoPurchaseBean.getUnitPrice()[i]* kenoPurchaseBean.getNoOfLines()[i]* kenoPurchaseBean.getNoOfDraws()).replaceAll(",","");
			/*String panelPrice = "|PanelPrice:"
					+ kenoPurchaseBean.getBetAmountMultiple()[i]
					* kenoPurchaseBean.getUnitPrice()[i]
					* kenoPurchaseBean.getNoOfLines()[i]
					* kenoPurchaseBean.getNoOfDraws();*/
			       
			if ("Banker".equalsIgnoreCase(playTypeStr[i]) || "MN-Banker".equalsIgnoreCase(playTypeStr[i])) {
				//logger.debug("---------------BANKER---------------");

				String playerData = kenoPurchaseBean.getPlayerData()[i];
				String[] banker = playerData.replace(",BL", "").split(",UL,");
				finalData.append("|PlayType:" + playTypeStr[i] + "|UL:"
						+ banker[0] + "|BL:" + banker[1] + panelPrice + "|QP:"
						+ kenoPurchaseBean.getIsQuickPick()[i]);
			} else {

				//logger.debug("--------------OTHERS--------------");
				finalData.append("|PlayType:" + playTypeStr[i] + "|Num:"
						+ kenoPurchaseBean.getPlayerData()[i] + panelPrice
						+ "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);

			}
		}

		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(kenoPurchaseBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		
		if(Arrays.asList(playTypeArr).toString().contains("DC-")){
			String dblChncMsg= com.skilrock.lms.common.Utility.getPropertyValue("MSG_FOR_DC");
			String posForDcMsg= com.skilrock.lms.common.Utility.getPropertyValue("POSITION_FOR_DC_MSG");
			if("BOTTOM".equalsIgnoreCase(posForDcMsg))
				bottomMsgsStr.append("~").append(dblChncMsg);
			else if("TOP".equalsIgnoreCase(posForDcMsg))
				topMsgsStr.append("~").append(dblChncMsg);
		}
		
		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}

		

		String raffleDrawDay=(String) sc.getAttribute("RAFFLE_GAME_DRAW_DAY");
		String raffleGameDataString=(String) sc.getAttribute("RAFFLE_GAME_DATA");
		raffleGameDataString=raffleGameDataString.substring(raffleGameDataString.indexOf(":")+1,raffleGameDataString.length());
		double rAmount=0;
		boolean isRaffleGame=false;
		String raffleGameData=null;
        if(!"".equals(raffleGameDataString)){
            String[] raffGameArray=raffleGameDataString.split("#");
            for(int i=0;i < raffGameArray.length;i++){
                raffleGameData=raffGameArray[i];
                if("KenoFive".equalsIgnoreCase(raffleGameData.substring(0,raffleGameData.indexOf("%")))){
        			rAmount=Double.parseDouble(raffleGameData.substring(raffleGameData.indexOf("%")+1, raffleGameData.lastIndexOf("%")));
        			isRaffleGame=true;
        		}
            }
		
        }
		String raffleDrawDate="";
		if(!"NA".equals(raffleDrawDay) && kenoPurchaseBean.getTotalPurchaseAmt() >= rAmount && isRaffleGame){
		Map<String, Integer> dayMap = new HashMap<String, Integer>();
		dayMap.put("SUNDAY", 1);
		dayMap.put("MONDAY", 2);
		dayMap.put("TUESDAY", 3);
		dayMap.put("WEDNESDAY", 4);
		dayMap.put("THURSDAY", 5);
		dayMap.put("FRIDAY", 6);
		dayMap.put("SATURDAY", 7);

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		int today = calendar.get(Calendar.DAY_OF_WEEK);
		int nxtDay = dayMap.get(raffleDrawDay);

		if(today != nxtDay) {
			int days = (Calendar.SATURDAY - today + nxtDay) % 7;  
		    calendar.add(Calendar.DAY_OF_YEAR, days);
		}
		
		raffleDrawDate="RDate:"+format.format(calendar.getTime())+"|";
		System.out.println(raffleDrawDate);
		}
		
		
		
		// here build the data of promo ticket
		String promoTicketDta = "";
		Object promoPurchaseBeaan = kenoPurchaseBean.getPromoPurchaseBean();
		if (promoPurchaseBeaan instanceof FortunePurchaseBean) {
			promoTicketDta = buildSaleDataforWinfast(
					(FortunePurchaseBean) promoPurchaseBeaan, userBean);
		}
		if(promoPurchaseBeaan instanceof KenoPurchaseBean) {
			promoTicketDta = new DrawGameRPOSHelper().buildTwelveByTwentyFourData((KenoPurchaseBean)promoPurchaseBeaan, userBean);
		}
		if (promoPurchaseBeaan instanceof List) {
			promoTicketDta = CommonMethods
					.buildRaffleData((List<RafflePurchaseBean>) promoPurchaseBeaan);
		}
		/*
		 * // here build the final data for winfast String winfastData = ""; if
		 * (kenoPurchaseBean.getFortunePurchaseBean() != null) { winfastData =
		 * buildSaleDataforWinfast(kenoPurchaseBean .getFortunePurchaseBean(),
		 * userBean); }
		 */
		
		finalData.append("|TicketCost:"
				+ nf.format(kenoPurchaseBean.getTotalPurchaseAmt()).replaceAll(",", "")
				+ drawTimeBuild.toString() + "|balance:" + balance + "|"+raffleDrawDate
				+ advtMsg + promoTicketDta);
		
		/*finalData.append("|TicketCost:"
				+ kenoPurchaseBean.getTotalPurchaseAmt()
				+ drawTimeBuild.toString() + "|balance:" + balance + "|"
				+ advtMsg + promoTicketDta);*/

		finalPurchaseData = finalData.toString();
		//System.out.println("FINAL PURCHASE DATA KENO FIVE:" + finalPurchaseData);
		request.setAttribute("purchaseData", finalPurchaseData);
		response.getOutputStream().write(finalPurchaseData.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			
			System.out.println("FINAL PURCHASE DATA KENO FIVE:Error!Try Again");
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
			
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
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
					.get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:")
					.replace(".0", ""));
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

	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
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
