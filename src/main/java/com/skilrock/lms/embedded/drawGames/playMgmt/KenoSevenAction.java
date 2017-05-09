package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.KenoSevenHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class KenoSevenAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public KenoSevenAction() {
		super(KenoSevenAction.class);
	}

	private static final List<String> numbers = Arrays.asList("", "Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)");

	private int gameId = Util.getGameId("KenoSeven");
	private String userName;
	private String pickedNumbers;
	private String pickedNum;
	private String noPicked;
	private String playType;
	private String QP;
	private int isAdvancedPlay;
	private int noOfLines;
	private int noOfDraws;
	private String betAmt;
	private String totalPurchaseAmt;
	private String[] drawIdArr;
	private String plrMobileNumber;
	private long LSTktNo;
	private KenoPurchaseBean kenoPurchaseBean;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public String getPickedNum() {
		return pickedNum;
	}

	public void setPickedNum(String pickedNum) {
		this.pickedNum = pickedNum;
	}

	public String getNoPicked() {
		return noPicked;
	}

	public void setNoPicked(String noPicked) {
		this.noPicked = noPicked;
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

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public String getBetAmt() {
		return betAmt;
	}

	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
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

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoPurchaseBean;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
	}

	/*public static List<String> rec(int start, int elementToChoose, int returnCnt, String[] indexArr, String[] elements, StringBuffer stbuff, List<String> comboList, String qp) {

		if (returnCnt == elementToChoose)
			return comboList;

		returnCnt++;
		for (int i=start; i<elements.length; i++) {
			indexArr[returnCnt - 1] = String.valueOf(i);

			if (returnCnt == elementToChoose) {
				stbuff = new StringBuffer();
				for (String element : indexArr)
					stbuff.append("," + elements[Integer.parseInt(element)]);

				stbuff.delete(0, 1);
				comboList.add(stbuff.toString());
				if ("No".equalsIgnoreCase(qp))
					comboList.add("Nxt");
				else if ("Yes".equalsIgnoreCase(qp))
					comboList.add("QP");
			}

			rec(++start, elementToChoose, returnCnt, indexArr, elements, stbuff, comboList, qp);
		}

		return comboList;
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
	}*/

	@SuppressWarnings("unchecked")
	public void purchaseTicketProcess() {
		UserInfoBean userBean = null;
		try {
			String isDraw = Utility.getPropertyValue("IS_DRAW");
			if("NO".equalsIgnoreCase(isDraw)) {
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE).getBytes());
				return;
			}

			userBean = getUserBean(userName);

			int serviceId = Util.getServiceIdFormCode(request.getAttribute("code").toString());
			if(serviceId==0 || userBean.getCurrentUserMappingId() == 0)
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE ,  LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);

			ServletContext sc = ServletActionContext.getServletContext();
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
			String[] pickedNumbersArr = pickedNumbers.split("Nxt");
			String[] noPickedArr = noPicked.replaceAll(" ", "").split("Nxt");
			String[] playTypeArr = playType.split("Nxt");
			String[] QPStr = QP.split("Nxt");
			String[] betAmtStr = betAmt.split("Nxt");

			int noOfPanel = pickedNumbersArr.length;
			int[] QPArr = new int[noOfPanel];
			int[] betAmtArr = new int[noOfPanel];
			for (int i = 0; i < noOfPanel; i++) {
				betAmtArr[i] = Integer.parseInt(betAmtStr[i]);
				QPArr[i] = Integer.parseInt(QPStr[i]);
			}

			KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
			drawGamePurchaseBean.setGameId(gameId);
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
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
			drawGamePurchaseBean.setRefMerchantId((String)LMSUtility.sc.getAttribute("REF_MERCHANT_ID"));
			drawGamePurchaseBean.setPurchaseChannel("LMS_Terminal");
			drawGamePurchaseBean.setBonus("N");
			drawGamePurchaseBean.setPlayType(playTypeArr);
			drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
			drawGamePurchaseBean.setNoOfPanel(noOfPanel);
			drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
			drawGamePurchaseBean.setBarcodeType(Utility.getPropertyValue("barcodeType"));
			drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
			drawGamePurchaseBean.setServiceId(serviceId);
			drawGamePurchaseBean.setDeviceType("TERMINAL");

			long lastPrintedTicket = 0;
			int lstGameId = 0;
			if(LSTktNo != 0) {
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGamePurchaseBean.setLastSoldTicketNo(String.valueOf(lastPrintedTicket));
			drawGamePurchaseBean.setLastGameId(lstGameId);
			drawGamePurchaseBean.setActionName(ActionContext.getContext().getName());
			if (drawIdArr != null)
				drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));

			String countryDeployed = Utility.getPropertyValue("COUNTRY_DEPLOYED");
			/*if("NIGERIA".equals(countryDeployed)) {
				SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
				String bettypeOffStartTime = Utility.getPropertyValue("BETTYPE_OFF_START_TIME");
				Date start = parser.parse(bettypeOffStartTime);
				String bettypeOffEndTime = Utility.getPropertyValue("BETTYPE_OFF_END_TIME");
				Date end = parser.parse(bettypeOffEndTime);
				String currDateString = parser.format(Util.getCurrentTimeStamp());
				Date userDate = parser.parse(currDateString);

				if (userDate.after(start) && userDate.before(end)) {
					try {
						for (String betType : playTypeArr) {
							if ("Direct4".equals(betType) || "Direct5".equals(betType)) {
								response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
								logger.info("ErrorMessage - No Direct4 and Direct5 Bet Type Till - " + bettypeOffEndTime);
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
						logger.info("ErrorMessage - Some Internal Error");
						return;
					}
				}
			}*/

			KenoSevenHelper helper = new KenoSevenHelper();
			kenoPurchaseBean = helper.commonPurchseProcess(userBean, drawGamePurchaseBean);

			String finalPurchaseData = null;
			String saleStatus = getKenoPurchaseBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus) + "|";
				response.getOutputStream().write(finalPurchaseData.getBytes());
				logger.info("Final Purchase Keno Seven Data - " + finalPurchaseData);
				return;
			}

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
			String balance = nf.format(bal).replaceAll(",", "");
			int listSize = kenoPurchaseBean.getDrawDateTime().size();
			StringBuilder drawTimeBuild = new StringBuilder("");
			for (int i = 0; i < listSize; i++){
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

			String time = kenoPurchaseBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");

			StringBuilder finalData = new StringBuilder("");
			if(userBean.getTerminalBuildVersion() < Double.parseDouble(Utility.getPropertyValue("CURRENT_TERMINAL_BUILD_VERSION")) && "NIGERIA".equals(countryDeployed))
				finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()).length()==ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired?kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);			
			else
				finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+"|brCd:"+kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? kenoPurchaseBean.getBarcodeCount():"")+"|Date:" + time);

			int noOfPanels = kenoPurchaseBean.getNoOfPanel();
			String[] playTypeStr = kenoPurchaseBean.getPlayType();
			for (int i = 0; i < noOfPanels; i++) {			
				String panelPrice = "|PanelPrice:" + nf.format(kenoPurchaseBean.getBetAmountMultiple()[i]* kenoPurchaseBean.getUnitPrice()[i]* kenoPurchaseBean.getNoOfLines()[i]* kenoPurchaseBean.getNoOfDraws()).replaceAll(",","");

				/*if ("Banker".equalsIgnoreCase(playTypeStr[i])) {
					String playerData = kenoPurchaseBean.getPlayerData()[i];
					String[] banker = playerData.replace(",BL", "").split(",UL,");
					finalData.append("|PlayType:" + playTypeStr[i] + "|UL:" + banker[0] + "|BL:" + banker[1] + panelPrice + "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);
				} else*/
					finalData.append("|PlayType:" + playTypeStr[i] + "|Num:" + kenoPurchaseBean.getPlayerData()[i] + panelPrice + "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);
			}
	
			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
			String advtMsg = "";
			UtilApplet.getAdvMsgs(kenoPurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);
			/*if(Arrays.asList(playTypeArr).toString().contains("DC-")) {
				String dblChncMsg = Utility.getPropertyValue("MSG_FOR_DC");
				String posForDcMsg = Utility.getPropertyValue("POSITION_FOR_DC_MSG");
				if("BOTTOM".equalsIgnoreCase(posForDcMsg))
					bottomMsgsStr.append("~").append(dblChncMsg);
				else if("TOP".equalsIgnoreCase(posForDcMsg))
					topMsgsStr.append("~").append(dblChncMsg);
			}*/
			if (topMsgsStr.length() != 0)
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			if (bottomMsgsStr.length() != 0)
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";

			String raffleDrawDay = Utility.getPropertyValue("RAFFLE_GAME_DRAW_DAY");
			String raffleGameDataString = Utility.getPropertyValue("RAFFLE_GAME_DATA");
			raffleGameDataString = raffleGameDataString.substring(raffleGameDataString.indexOf(":")+1,raffleGameDataString.length());
			double rAmount = 0;
			boolean isRaffleGame = false;
			String raffleGameData = null;
	        if(!"".equals(raffleGameDataString)) {
	            String[] raffGameArray = raffleGameDataString.split("#");
	            for(int i=0; i<raffGameArray.length; i++) {
	                raffleGameData = raffGameArray[i];
	                if("KenoSeven".equalsIgnoreCase(raffleGameData.substring(0,raffleGameData.indexOf("%")))) {
	        			rAmount = Double.parseDouble(raffleGameData.substring(raffleGameData.indexOf("%")+1, raffleGameData.lastIndexOf("%")));
	        			isRaffleGame = true;
	        		}
	            }
	        }

	        String raffleDrawDate = "";
			if(!"NA".equals(raffleDrawDay) && kenoPurchaseBean.getTotalPurchaseAmt() >= rAmount && isRaffleGame) {
				Map<String, Integer> dayMap = new HashMap<String, Integer>();
				dayMap.put("SUNDAY", 1);
				dayMap.put("MONDAY", 2);
				dayMap.put("TUESDAY", 3);
				dayMap.put("WEDNESDAY", 4);
				dayMap.put("THURSDAY", 5);
				dayMap.put("FRIDAY", 6);
				dayMap.put("SATURDAY", 7);

				Calendar calendar = Calendar.getInstance();
				int today = calendar.get(Calendar.DAY_OF_WEEK);
				int nxtDay = dayMap.get(raffleDrawDay);

				if(today != nxtDay) {
					int days = (Calendar.SATURDAY - today + nxtDay) % 7;  
				    calendar.add(Calendar.DAY_OF_YEAR, days);
				}
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				raffleDrawDate = "RDate:"+format.format(calendar.getTime())+"|";
				logger.info("Raffle Draw Date - "+raffleDrawDate);
			}

			String promoTicketDta = "";
			Object promoPurchaseBeaan = kenoPurchaseBean.getPromoPurchaseBean();
			if (promoPurchaseBeaan instanceof FortunePurchaseBean)
				promoTicketDta = buildSaleDataforWinfast((FortunePurchaseBean) promoPurchaseBeaan, userBean);
			if(promoPurchaseBeaan instanceof KenoPurchaseBean)
				promoTicketDta = new DrawGameRPOSHelper().buildTwelveByTwentyFourData((KenoPurchaseBean)promoPurchaseBeaan, userBean);
			if (promoPurchaseBeaan instanceof List)
				promoTicketDta = CommonMethods.buildRaffleData((List<RafflePurchaseBean>) promoPurchaseBeaan);

			finalData.append("|TicketCost:" + nf.format(kenoPurchaseBean.getTotalPurchaseAmt()).replaceAll(",", "") + drawTimeBuild.toString() + "|balance:" + balance + "|" + raffleDrawDate + advtMsg + promoTicketDta);

			//finalData.append("|TicketCost:" + nf.format(kenoPurchaseBean.getTotalPurchaseAmt()).replaceAll(",", "") + drawTimeBuild.toString() + "|balance:" + balance + "|" + "" + advtMsg + "");

			finalPurchaseData = finalData.toString();
			request.setAttribute("purchaseData", finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Error in Final Purchase Data Keno Seven");
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			return;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			return;
		} catch (SQLException se) {
			se.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			return;
		}
	}

	private String buildSaleDataforWinfast(FortunePurchaseBean fortuneBean, UserInfoBean userBean) {

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
}