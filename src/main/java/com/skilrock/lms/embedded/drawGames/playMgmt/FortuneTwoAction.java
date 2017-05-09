			package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import rng.RNGUtilities;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class FortuneTwoAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(FortuneTwoAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final List sunSign = Arrays.asList("", "Aries", "Taurus",
			"Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio",
			"Sagittarius", "Capricorn", "Aquarius", "Pisces");
	private String[] drawIdArr;
	private FortuneTwoPurchaseBean fortuneTwoBean;
	private int gameNumber = Util.getGameId("FortuneTwo");
	private int isAdvancedPlay;
	private int isQuickPick;
	private int noOfDraws;
	private int noOfPanels;
	private HttpServletRequest request;
	private HttpServletResponse response;
	HttpSession session = null;
	private String symbols;
	private int totalNoOfPanels;
	private String totalPurchaseAmt;
	
	
	private String noPicked;
	private String pickedNum;
	private String pickedNumbers;
	private String playType;
	private String QP;
	private String betAmt;
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
	

	public String getBetAmt() {
		return betAmt;
	}

	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
	}

	public String getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(String noPicked) {
		this.noPicked = noPicked;
	}

	public String getPickedNum() {
		return pickedNum;
	}

	public void setPickedNum(String pickedNum) {
		this.pickedNum = pickedNum;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}
	
	

	public FortuneTwoPurchaseBean getFortuneTwoBean() {
		return fortuneTwoBean;
	}

	public void setFortuneTwoBean(FortuneTwoPurchaseBean fortuneTwoBean) {
		this.fortuneTwoBean = fortuneTwoBean;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getQP() {
		return QP;
	}

	public void setQP(String qP) {
		QP = qP;
	}

	private String userName;

	public String[] getDrawIdArr() {
		return drawIdArr;
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

	public int getNoOfPanels() {
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

	public String getUserName() {
		return userName;
	}

	public void purchaseTicketProcess() throws Exception {
		logger.debug(" inside fortuen two action ---");
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

		if (session == null) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}

		// ----------------------------------

		if (stopSale()) {
			response.getOutputStream()
					.write(
							("ErrorMsg:Your Sale is Stopped for Some Time|")
									.getBytes());
			return;
		} // for remove later added for kenya

		// ---------------------------------------------

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String purchaseChannel = "LMS_Terminal";

		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		FortuneTwoPurchaseBean drawGamePurchaseBean = new FortuneTwoPurchaseBean();
		String[] betAmtStr = null;
		String[] playTypeArr = null;
		String[] QPStr =null;
		String[] pickedNumbersArr = null;
		String[] noPickedStr = null;		
		int noOfPanel =0;
		String[] tempQPStr = QP.split("Nxt");
		String[] tempBetAmtStr = betAmt.split("Nxt");
		if(playType.indexOf("Direct1")!= -1 && Integer.parseInt((tempQPStr[0]))==1){
			int index = 0;
			Map<Integer, Integer> qpData = new TreeMap<Integer, Integer>();
			for (int i = 0; i < Integer.parseInt(tempBetAmtStr[0]); i++) {
				index = RNGUtilities
						.generateRandomNumber(1, sunSign.size() - 1);
				qpData.put(index, qpData.get(index) == null ? 1 : qpData
						.get(index) + 1);
			}
			Iterator<Integer> itr = qpData.keySet().iterator();
			int value = 0;
			int i= 0;
			playTypeArr =new String[qpData.size()];
			betAmtStr = new String[qpData.size()];
			QPStr = new String[qpData.size()];
			pickedNumbersArr = new String[qpData.size()];
			noPickedStr = new String[qpData.size()];
			noOfPanel = qpData.size();
			while(itr.hasNext()){
				value = itr.next();
				playTypeArr[i] = "Direct1";
				QPStr[i] = "1";
				betAmtStr[i] = qpData.get(value)+"";
				pickedNumbersArr[i]=(String) sunSign.get(value);
				noPickedStr[i] = "1";
				i++;
				
			}
			noOfPanels=noOfPanel;
			
		}else{
		
		betAmtStr = betAmt.split("Nxt");
		playTypeArr = playType.split("Nxt");
		QPStr = QP.split("Nxt");
		pickedNumbersArr = pickedNumbers.split("Nxt");
		noPickedStr = noPicked.replaceAll(" ", "").split("Nxt");		
		noOfPanel = pickedNumbersArr.length;
		
		}
		int[] betAmtArr = new int[noOfPanel];
		int[] QPArr = new int[noOfPanel];
		int[] noPickedArr = new int[noOfPanel];
		
		for (int i = 0; i < noOfPanel; i++) {
			betAmtArr[i] = Integer.parseInt(betAmtStr[i]);
			QPArr[i] = Integer.parseInt(QPStr[i]);
			noPickedArr[i] = Integer.parseInt(noPickedStr[i]);
		}
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		drawGamePurchaseBean.setGame_no(gameNumber);
		drawGamePurchaseBean.setGameDispName(Util
				.getGameDisplayName(gameNumber));
		drawGamePurchaseBean.setNoOfDraws(noOfDraws);
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		drawGamePurchaseBean.setBetAmountMultiple(betAmtArr);
		drawGamePurchaseBean.setIsQuickPick(QPArr);
		drawGamePurchaseBean.setPlayerData(pickedNumbersArr);
		drawGamePurchaseBean.setNoPicked(noPickedArr);
		drawGamePurchaseBean.setPlayType(playTypeArr);
		drawGamePurchaseBean.setNoOfPanel(noOfPanels);
		drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
		
		String finalPurchaseData = null;

		String lastSoldTicketNo = "0";
		if(!"0".equals(LSTktNo) && LSTktNo!=null){
			lastSoldTicketNo = LSTktNo.substring(0, LSTktNo.length()-2);
		}
		
		drawGamePurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
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
		
		if (noOfPanels < 1) {
			drawGamePurchaseBean.setSaleStatus("ERROR");
			setFortuneTwoBean(drawGamePurchaseBean);
			finalPurchaseData = "ErrorMsg:"
					+ EmbeddedErrors.PURCHSE_INVALID_DATA;
			System.out.println("FINAL PURCHASE DATA FORTUNE TWO:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}

		FortuneTwoPurchaseBean fortuneTwoBean = helper.fortuneTwoPurchaseTicket(
				userBean, drawGamePurchaseBean);

		/*if("Direct1".equalsIgnoreCase(playTypeArr[0])){
			String s = CommonMethods.buildFortuneData(fortuneTwoBean, userBean);
			System.out.println("Final New Data:: " +  s);
			return;
		}*/
		
		setFortuneTwoBean(fortuneTwoBean);
		String saleStatus = getFortuneTwoBean().getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)
					+ "|";
			System.out.println("FINAL PURCHASE DATA FORTUNE TWO:" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}
		//logger.debug(fortuneTwoBean.getPurchaseTime());

		String time = fortuneTwoBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		StringBuilder finalData = new StringBuilder("");
		int betAmountMultiple[] = fortuneTwoBean.getBetAmountMultiple();
		int listSize = fortuneTwoBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");
		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + fortuneTwoBean.getDrawDateTime()
					.get(i)).replace(" ", "|DrawTime:").replace(".0", ""));
		}

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		String balance =nf.format(bal).replaceAll(",", "");
		//balance = balance.substring(0, balance.indexOf(".") + 3);

		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(fortuneTwoBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}
		List<RafflePurchaseBean> rafflePurchaseBeanList = fortuneTwoBean
				.getRafflePurchaseBeanList();
		String raffleData = CommonMethods
				.buildRaffleData(rafflePurchaseBeanList);

		finalData.append("TicketNo:" + fortuneTwoBean.getTicket_no()
				+ fortuneTwoBean.getReprintCount() + "|Date:" + time);
		
		for (int i= 0; i<noOfPanel ; i++){
			String panelPrice = "|PanelPrice:"
				+ fortuneTwoBean.getBetAmountMultiple()[i]
				* fortuneTwoBean.getUnitPrice()[i]
                * fortuneTwoBean.getNoOfLines()[i]				                                
				* fortuneTwoBean.getNoOfDraws();
			finalData.append("|PlayType:" + fortuneTwoBean.getPlayType()[i].replace("Banker", "Group") + "|Num:"
					+ fortuneTwoBean.getPlayerData()[i] + panelPrice
					+ "|QP:" + fortuneTwoBean.getIsQuickPick()[i]);
		}
		finalData.append("|TicketCost:"
				+ fortuneTwoBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
				+ "|balance:" + balance + "|"
				+ raffleData + advtMsg);

		System.out.println("FINAL PURCHASE DATA FORTUNE TWO:" + finalData);
		response.getOutputStream().write(finalData.toString().getBytes());
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

	public void setIsQuickPick(int isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setNoOfPanels(int noOfPanels) {
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private boolean stopSale() throws ParseException {

		Calendar cal = Calendar.getInstance();

		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String currDate = new java.sql.Date(cal.getTimeInMillis()).toString();

		String strt = currDate + " 23:05:00";
		String end = currDate + " 23:15:00";

		String satStart = currDate + " 18:00:00";
		String satEnd = currDate + " 18:30:00";

		long currTime = cal.getTimeInMillis();

		long strtTime = frmt.parse(strt).getTime();
		long endTime = frmt.parse(end).getTime();
		long satStartTime = frmt.parse(satStart).getTime();
		long satEndTime = frmt.parse(satEnd).getTime();

		if (weekDay > 1 && weekDay < 7) {
			// System.out.println("***week days******");
			if (currTime > strtTime && currTime < endTime) {
				System.out.println("stopSale() method has been called");
				System.out.println("***current date:***" + currDate + "***");
				System.out.println("***current time:***" + cal.getTime()
						+ "***");
				System.out.println("**currTime:**" + currTime + "***");
				System.out.println("**strtTime:**" + strtTime + "***");
				System.out.println("**endTime:**" + endTime + "***");
				return true;
			}
		} else if (weekDay == 7) {
			// System.out.println("***saturday******");
			if (currTime > satStartTime && currTime < satEndTime) {
				System.out.println("stopSale() method has been called");
				System.out.println("***current date:***" + currDate + "***");
				System.out.println("***current time:***" + cal.getTime()
						+ "***");
				System.out.println("**currTime:**" + currTime + "***");
				System.out.println("**satStartTime:**" + satStartTime + "***");
				System.out.println("**satEndTime:**" + satEndTime + "***");
				return true;
			}
		}

		return false;
	}

}
