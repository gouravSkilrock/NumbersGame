package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.FourDigitHelper;
import com.skilrock.lms.dge.beans.FourDigitPurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;



public class FourDigitAction extends ActionSupport implements ServletRequestAware , ServletResponseAware {

	static Log logger = LogFactory.getLog(FourDigitAction.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) throws Exception {
		new FourDigitAction().purchaseTicketProcess();
	}
	
	private String betAmt;
	private String[] drawIdArr;
	private int gameId = Util.getGameId("FourDigit");
	private int isAdvancedPlay;
	private FourDigitPurchaseBean fourDigitPurchaseBean;
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
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	public String getBetAmt() {
		return betAmt;
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

	public FourDigitPurchaseBean getFourDigitPurchaseBean() {
		return fourDigitPurchaseBean;
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

	public long getLSTktNo() {
		return LSTktNo;
	}

	public String getQP() {
		return QP;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public String getUserName() {
		return userName;
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
					// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
					HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
					UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
					Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
					String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
					int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
		
					String purchaseChannel = "LMS_Terminal";
					//logger.debug(betAmt);
					String[] betAmtStr = betAmt.split("Nxt");
					String[] QPStr = QP.split("Nxt");
					String[] pickedNumbersArr = pickedNumbers.split("Nxt");
					String[] noPickedArr = noPicked.replaceAll(" ", "").split("Nxt");
					String[] playTypeArr = playType.split("Nxt");
					String[] forecastArr= null;
					int noOfPanel = pickedNumbersArr.length;
					int[] QPArr = new int[noOfPanel];
					int[] bigForecast = new int[noOfPanel];
					int[] smallForecast=new int[noOfPanel];
					
					for (int i = 0; i < noOfPanel; i++) {
							 forecastArr = betAmtStr[i].split(",");
							 logger.debug(forecastArr[0]);
							 bigForecast[i] = Integer.parseInt(forecastArr[0]);
							 smallForecast[i] = Integer.parseInt(forecastArr[1]);
							 QPArr[i] = Integer.parseInt(QPStr[i]);
					}
					
					FourDigitPurchaseBean drawGamePurchaseBean = new FourDigitPurchaseBean();
					drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
					drawGamePurchaseBean.setGameId(gameId);
					drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
					drawGamePurchaseBean.setBigForecost(bigForecast);
					drawGamePurchaseBean.setSmallForecost(smallForecast);
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
					if(LSTktNo !=0){
							lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
							lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
					}
					
					if (drawIdArr != null)
					drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));					
					drawGamePurchaseBean.setRefMerchantId(refMerchantId);
					drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
					drawGamePurchaseBean.setBonus("N");
					drawGamePurchaseBean.setPlayType(playTypeArr);
					drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
					drawGamePurchaseBean.setNoOfPanel(noOfPanel);
					drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
		
					String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
					drawGamePurchaseBean.setBarcodeType(barcodeType);
		
					String actionName=ActionContext.getContext().getName();
					FourDigitHelper helper = new FourDigitHelper();
					new DrawGameRPOSHelper().checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
		
					/*String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");
					if("NIGERIA".equals(countryDeployed)){
								SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
								String bettypeOffStartTime=(String) sc.getAttribute("BETTYPE_OFF_START_TIME");
								logger.info("BETTYPE_OFF_START_TIME::"+bettypeOffStartTime);
								Date start = parser.parse(bettypeOffStartTime);
								String bettypeOffEndTime=(String) sc.getAttribute("BETTYPE_OFF_END_TIME");
								logger.info("BETTYPE_OFF_END_TIME::"+bettypeOffEndTime);
								Date end = parser.parse(bettypeOffEndTime);
								String currDateString= parser.format(new Date());
								Date userDate = parser.parse(currDateString);
								logger.info(currDateString);
 			
								if (userDate.after(start) && userDate.before(end)) {
									try{     
										for(String betType:playTypeArr){
											if("Direct4".equals(betType) || "Direct5".equals(betType)){
												response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
												logger.info("ErrorMsg:No Direct4 and Direct5 Bet Type Till "+bettypeOffEndTime);
												return;
											}
										}
									}
									catch (Exception e) {
										e.printStackTrace();
										response.getOutputStream().write(("ErrorMsg:Server Busy Amount Not Deducted").getBytes());
										logger.info("ErrorMsg:Some Internal Error "+bettypeOffEndTime);
										return;
									}
								}
					}*/
		 
					fourDigitPurchaseBean = helper.commonPurchseProcess(userBean,drawGamePurchaseBean);
					setFourDigitPurchaseBean(fourDigitPurchaseBean);
					String finalPurchaseData = null;
					String saleStatus = getFourDigitPurchaseBean().getSaleStatus();
					if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
							finalPurchaseData = "ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)+ "|";
							System.out.println("FINAL PURCHASE DATA FourDigit:" + finalPurchaseData);
							response.getOutputStream().write(finalPurchaseData.getBytes());
							return;
					}

					String time = fourDigitPurchaseBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");
					double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(2);		
					String balance = nf.format(bal).replaceAll(",", "");
					int listSize = fourDigitPurchaseBean.getDrawDateTime().size();
					StringBuilder drawTimeBuild = new StringBuilder("");
		
					for (int i = 0; i < listSize; i++) {
						drawTimeBuild.append(("|DrawDate:" + fourDigitPurchaseBean.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
					}	
		
						StringBuilder finalData = new StringBuilder("");
						// FOR BACKWARD COMPATIBILITY 
						if(userBean.getTerminalBuildVersion() < Double.parseDouble(Utility.getPropertyValue("CURRENT_TERMINAL_BUILD_VERSION")) /*&& "NIGERIA".equals(countryDeployed)*/)
							finalData.append("TicketNo:" + fourDigitPurchaseBean.getTicket_no() + fourDigitPurchaseBean.getReprintCount()+((fourDigitPurchaseBean.getTicket_no() + fourDigitPurchaseBean.getReprintCount()).length()==ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired?fourDigitPurchaseBean.getBarcodeCount():"")+"|Date:" + time);			
						else
							finalData.append("TicketNo:" + fourDigitPurchaseBean.getTicket_no() + fourDigitPurchaseBean.getReprintCount()+"|brCd:"+fourDigitPurchaseBean.getTicket_no() + fourDigitPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? fourDigitPurchaseBean.getBarcodeCount():"")+"|Date:" + time);
				
						int noOfPanels = fourDigitPurchaseBean.getNoOfPanel();
						String[] playTypeStr = fourDigitPurchaseBean.getPlayType();
						for (int i = 0; i < noOfPanels; i++) {			
							String panelPrice = "|PanelPrice:" + nf.format(((fourDigitPurchaseBean.getBigForecost())[i] + (fourDigitPurchaseBean.getSmallForecost())[i])* fourDigitPurchaseBean.getUnitPrice()[i]* fourDigitPurchaseBean.getNoOfLines()[i]* fourDigitPurchaseBean.getNoOfDraws()).replaceAll(",","");
							String forecasts = "|Big:" + bigForecast[i]+"|Small:"+smallForecast[i];
							//logger.debug("--------------OTHERS--------------");
							finalData.append("|PlayType:" + playTypeStr[i] + "|Num:"+ fourDigitPurchaseBean.getPlayerData()[i] + forecasts+panelPrice+ "|QP:" + fourDigitPurchaseBean.getIsQuickPick()[i]);
						}

						StringBuilder topMsgsStr = new StringBuilder("");
						StringBuilder bottomMsgsStr = new StringBuilder("");
						String advtMsg = "";
						UtilApplet.getAdvMsgs(fourDigitPurchaseBean.getAdvMsg(), topMsgsStr,bottomMsgsStr, 10);

						
						if (topMsgsStr.length() != 0) {
									advtMsg = "topAdvMsg:" + topMsgsStr + "|";
						}
						if (bottomMsgsStr.length() != 0) {
								advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
						}	
		
						finalData.append("|TicketCost:"+ nf.format(fourDigitPurchaseBean.getTotalPurchaseAmt()).replaceAll(",", "")+ drawTimeBuild.toString() + "|balance:" + balance + "|"+ advtMsg);
						finalPurchaseData = finalData.toString();
						System.out.println("FINAL PURCHASE DATA FOUR DIGIT:" + finalPurchaseData);
						request.setAttribute("purchaseData", finalPurchaseData);
						response.getOutputStream().write(finalPurchaseData.getBytes());
						
			} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FINAL PURCHASE DATA FOUR DIGIT:Error!Try Again");
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
	
	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setFourDigitPurchaseBean(FourDigitPurchaseBean fourDigitPurchaseBean) {
		this.fourDigitPurchaseBean = fourDigitPurchaseBean;
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

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public void setQP(String qP) {
		QP = qP;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
	}
	
}