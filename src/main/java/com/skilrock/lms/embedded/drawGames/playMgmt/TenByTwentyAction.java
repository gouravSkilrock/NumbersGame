package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.TenByTwentyHelper;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class TenByTwentyAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;

	private String betAmt;
	private String[] drawIdArr;
	private int gameId = Util.getGameId("TenByTwenty");
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
	static Log logger = LogFactory.getLog(TenByTwentyAction.class);

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getBetAmt() {
		return betAmt;
	}

	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
	}

	public String[] getDrawIdArr() {
		return drawIdArr;
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoPurchaseBean;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
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

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getQP() {
		return QP;
	}

	public void setQP(String qP) {
		QP = qP;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
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

	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void purchaseTicketProcess() {
		ServletContext sc = ServletActionContext.getServletContext();
		try {
			String isDraw = (String) sc.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE).getBytes());
				return;
			}
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");

			HttpSession session = (HttpSession) currentUserSessionMap.get(userName);

			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

			String purchaseChannel = "LMS_Terminal";

			String[] betAmtStr = betAmt.split("Nxt");
			String[] QPStr = QP.split("Nxt");
			String[] pickedNumbersArr = pickedNumbers.split("Nxt");
			String[] noPickedArr = noPicked.replaceAll(" ", "").split("Nxt");
			String[] playTypeArr = playType.split("Nxt");
			int noOfPanel = pickedNumbersArr.length;
			int[] betAmtArr = new int[noOfPanel];
			int[] QPArr = new int[noOfPanel];
			boolean [] qpPreGenerated = new boolean[noOfPanel];
			for (int i = 0; i < noOfPanel; i++) {
				betAmtArr[i] = Integer.parseInt(betAmtStr[i]);
				
				QPArr[i] =  playTypeArr[i].equalsIgnoreCase("AllOdd") || playTypeArr[i].equalsIgnoreCase("AllEven")
				||	playTypeArr[i].equalsIgnoreCase("First10") || playTypeArr[i].equalsIgnoreCase("Last10") || playTypeArr[i].equalsIgnoreCase("OddEven")
				|| playTypeArr[i].equalsIgnoreCase("EvenOdd") || playTypeArr[i].equalsIgnoreCase("JumpOddEven") || playTypeArr[i].equalsIgnoreCase("JumpEvenOdd")
				 ? Integer.parseInt(QPStr[i])+1 : Integer.parseInt(QPStr[i]);
				
				qpPreGenerated[i] = false;
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
			drawGamePurchaseBean.setQPPreGenerated(qpPreGenerated);
			long lastPrintedTicket = 0;
			int lstGameId = 0;

			int serviceId = Util.getServiceIdFormCode(request.getAttribute("code").toString());
			if (serviceId == 0 || userBean.getCurrentUserMappingId() == 0) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}

			if (LSTktNo != 0) {
				lastPrintedTicket = LSTktNo / Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			if (drawIdArr != null) {
				drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			}
			drawGamePurchaseBean.setLastSoldTicketNo(lastPrintedTicket + "");
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
			
			TransactionManager.setResponseData("true");

			String actionName = ActionContext.getContext().getName();

			drawGamePurchaseBean.setActionName(actionName);
			drawGamePurchaseBean.setLastGameId(lstGameId);
			drawGamePurchaseBean.setDeviceType("TERMINAL");

			TenByTwentyHelper helper = new TenByTwentyHelper();

			String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");

            kenoPurchaseBean = helper.commonPurchseProcess(userBean, drawGamePurchaseBean);
			setKenoPurchaseBean(kenoPurchaseBean);
			String finalPurchaseData = null;

			String saleStatus = getKenoPurchaseBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus) + "|";
				System.out.println("FINAL PURCHASE DATA Ten By Twenty:" + finalPurchaseData);
				response.getOutputStream().write(finalPurchaseData.getBytes());
				return;
			}

			String time = kenoPurchaseBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");

			double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(1);

			String balance = nf.format(bal).replaceAll(",", "");

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

			if (userBean.getTerminalBuildVersion() < Double.parseDouble(Utility.getPropertyValue("CURRENT_TERMINAL_BUILD_VERSION")) && "NIGERIA".equals(countryDeployed))
				finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + ((kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()).length() == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired ? kenoPurchaseBean.getBarcodeCount() : "") + "|Date:" + time);
			else
				finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + "|brCd:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired) ? kenoPurchaseBean.getBarcodeCount() : "") + "|Date:" + time);
			int noOfPanels = kenoPurchaseBean.getNoOfPanel();
			String[] playTypeStr = kenoPurchaseBean.getPlayType();
			for (int i = 0; i < noOfPanels; i++) {
				String panelPrice = "|PanelPrice:" + nf.format(kenoPurchaseBean.getBetAmountMultiple()[i] * kenoPurchaseBean.getUnitPrice()[i] * kenoPurchaseBean.getNoOfLines()[i] * kenoPurchaseBean.getNoOfDraws()).replaceAll(",", "");
				finalData.append("|PlayType:" + playTypeStr[i] + "|Num:" + kenoPurchaseBean.getPlayerData()[i] + panelPrice + "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);
			}

			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
			String advtMsg = "";

			UtilApplet.getAdvMsgs(kenoPurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);

			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}

			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
			}

			String raffleDrawDay = (String) sc.getAttribute("RAFFLE_GAME_DRAW_DAY");
			String raffleGameDataString = (String) sc.getAttribute("RAFFLE_GAME_DATA");
			raffleGameDataString = raffleGameDataString.substring(raffleGameDataString.indexOf(":") + 1, raffleGameDataString.length());
			double rAmount = 0;
			boolean isRaffleGame = false;
			String raffleGameData = null;
			if (!"".equals(raffleGameDataString)) {
				String[] raffGameArray = raffleGameDataString.split("#");
				for (int i = 0; i < raffGameArray.length; i++) {
					raffleGameData = raffGameArray[i];
					if ("KenoFive".equalsIgnoreCase(raffleGameData.substring(0, raffleGameData.indexOf("%")))) {
						rAmount = Double.parseDouble(raffleGameData.substring(raffleGameData.indexOf("%") + 1, raffleGameData.lastIndexOf("%")));
						isRaffleGame = true;
					}
				}
			}
			String raffleDrawDate = "";
			if (!"NA".equals(raffleDrawDay) && kenoPurchaseBean.getTotalPurchaseAmt() >= rAmount && isRaffleGame) {
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

				if (today != nxtDay) {
					int days = (Calendar.SATURDAY - today + nxtDay) % 7;
					calendar.add(Calendar.DAY_OF_YEAR, days);
				}
				raffleDrawDate = "RDate:" + format.format(calendar.getTime()) + "|";
				System.out.println(raffleDrawDate);
			}

			String promoTicketDta = "";
			Object promoPurchaseBeaan = kenoPurchaseBean.getPromoPurchaseBean();
		
			if (promoPurchaseBeaan instanceof List) {
				promoTicketDta = CommonMethods.buildRaffleData((List<RafflePurchaseBean>) promoPurchaseBeaan);
			}
			finalData.append("|TicketCost:"
					+ nf.format(kenoPurchaseBean.getTotalPurchaseAmt()).replaceAll(",", "") + drawTimeBuild.toString()
					+ "|balance:" + balance + "|" + advtMsg+ promoTicketDta);

			finalPurchaseData = finalData.toString();
			request.setAttribute("purchaseData", finalPurchaseData);
			if("SUCCESS".equalsIgnoreCase(kenoPurchaseBean.getSaleStatus())){
				String smsData = com.skilrock.lms.common.utility.CommonMethods.prepareSMSData(kenoPurchaseBean.getPlayerData(), kenoPurchaseBean.getPlayType(), new StringBuilder(String.valueOf(kenoPurchaseBean.getTotalPurchaseAmt())), new StringBuilder(kenoPurchaseBean.getTicket_no()+kenoPurchaseBean.getReprintCount()), kenoPurchaseBean.getDrawDateTime());
				com.skilrock.lms.common.utility.CommonMethods.sendSMS(smsData);
			}
			response.getOutputStream().write(finalPurchaseData.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FINAL PURCHASE DATA TEN BY TWENTY:Error!Try Again");
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}
	
}
