package com.skilrock.lms.web.drawGames.common;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.dge.beans.CommonPurchaseBean;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;

public class UtilApplet extends ActionSupport implements ServletRequestAware{
	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public static int getAdvMsgs(Map<String, List<String>> advMsgMap, StringBuilder topMsgsStr, StringBuilder bottomMsgsStr, int appletHeight) {
		String advtMsgEnable = (String) ServletActionContext.getServletContext().getAttribute("ADVT_MSG");
		if (advtMsgEnable != null && !"NULL".equalsIgnoreCase(advtMsgEnable) && "YES".equalsIgnoreCase(advtMsgEnable.trim())) {
			if (advMsgMap != null) {
				List<String> topMsgsList = advMsgMap.get("TOP");
				List<String> bottomMsgsList = advMsgMap.get("BOTTOM");
				int msgLen = 0;

				if (topMsgsList != null) {
					if (topMsgsStr.length() == 1) {
						topMsgsStr.deleteCharAt(topMsgsStr.length() - 1);
					}
					for (int i = 0; i < topMsgsList.size(); i++) {
						topMsgsStr = topMsgsStr.append(topMsgsList.get(i) + "~");
						msgLen = topMsgsList.get(i).length();
						if (msgLen > 17) {
							appletHeight = appletHeight + 11 * (msgLen / 17) + 11;
						} else {
							appletHeight = appletHeight + 22;
						}
					}
					if (topMsgsStr.length() > 0) {
						topMsgsStr.deleteCharAt(topMsgsStr.length() - 1);
					}
				}

				if (bottomMsgsList != null) {
					if (bottomMsgsStr.length() == 1) {
						bottomMsgsStr.deleteCharAt(bottomMsgsStr.length() - 1);
					}
					for (int i = 0; i < bottomMsgsList.size(); i++) {
						bottomMsgsStr = bottomMsgsStr.append(bottomMsgsList
								.get(i)
								+ "~");
						msgLen = bottomMsgsList.get(i).length();
						if (msgLen > 17) {
							appletHeight = appletHeight + 11 * (msgLen / 17)
									+ 11;
						} else {
							appletHeight = appletHeight + 22;
						}
					}
					if (bottomMsgsStr.length() > 0) {
						bottomMsgsStr.deleteCharAt(bottomMsgsStr.length() - 1);
					}
				}
			}
		}
		return appletHeight;
	}

	public static int getRaffPurchaseData(List<RafflePurchaseBean> rafflePurchaseBeanList, StringBuilder raffleData, int appletHeight) {
		if(rafflePurchaseBeanList != null){
			RafflePurchaseBean bean = null;
			if (rafflePurchaseBeanList.size() > 0) {
				raffleData.deleteCharAt(raffleData.length() - 1);
			}

			for (int i = 0; i < rafflePurchaseBeanList.size(); i++) {
				bean = rafflePurchaseBeanList.get(i);
				StringBuilder raffTopAdvMsg = new StringBuilder(" ");
				StringBuilder raffBottomAdvMsg = new StringBuilder(" ");
				appletHeight = getAdvMsgs(bean.getAdvMsg(), raffTopAdvMsg, raffBottomAdvMsg, appletHeight);

				String raffTktType = bean.getRaffleTicketType();
				raffleData.append("raffTktType-sprtr-" + raffTktType
						+ "#raffTktNo-sprtr-" + bean.getRaffleTicket_no()
						+ "#raffDrawTime-sprtr-" + bean.getDrawDateTime()
						+ "#raffDispName-sprtr-" + bean.getGameDispName()
						+ "#raffTopAdvMsg-sprtr-" + raffTopAdvMsg
						+ "#raffBottomAdvMsg-sprtr-" + raffBottomAdvMsg + "Nxt");
				if ("ORIGINAL".equalsIgnoreCase(raffTktType)) {
					appletHeight = appletHeight + 150;
				} else if ("REFERENCE".equalsIgnoreCase(raffTktType)) {
					appletHeight = appletHeight + 44;
				}
			}
			if (bean != null) {
				raffleData.delete(raffleData.length() - 3, raffleData.length());
			}
		}
		return appletHeight;
	}

	public static int getRaffPWTData(List<RaffleDrawIdBean> raffleDrawIdBeanList, StringBuilder raffleData, int appletHeight) {
		RaffleDrawIdBean bean = null;
		if (raffleDrawIdBeanList.size() > 0) {
			raffleData.deleteCharAt(raffleData.length() - 1);
		}
		for (int i = 0; i < raffleDrawIdBeanList.size(); i++) {
			bean = raffleDrawIdBeanList.get(i);
			String status = bean.getStatus();
			String pwtStatus = bean.getPwtStatus();
			// double totalRaffleAmount = 0.0;

			if ("FRAUD".equalsIgnoreCase(status)) {
				raffleData.append("Cannot Verify.Invalid PWT");
			} else if ("TICKET_EXPIRED".equalsIgnoreCase(status)) {
				raffleData.append("Expired or Invalid Ticket");
			} else if ("RES_AWAITED".equalsIgnoreCase(status)) {
				raffleData.append("Awaited~" + bean.getDrawDateTime());
			} else if ("NON_WIN".equalsIgnoreCase(status)) {
				raffleData.append("TRY AGAIN~" + bean.getDrawDateTime());
			} else if ("NORMAL_PAY".equalsIgnoreCase(status)) {
				/*
				 * totalRaffleAmount = totalRaffleAmount +
				 * Double.parseDouble(bean.getWinningAmt());
				 */
				raffleData.append("WIN " + bean.getWinningAmt() + "~"
						+ bean.getDrawDateTime() + "~" + bean.getWinningAmt());
			} else if ("CLAIMED".equalsIgnoreCase(status)) {
				raffleData.append("CLAIMED~" + bean.getDrawDateTime());
			} else if ("PND_PAY".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			} else if ("HIGH_PRIZE".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			} else if ("OUT_VERIFY_LIMIT".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			} else if ("OUT_PAY_LIMIT".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			}
			raffleData.append("Nxt");
			appletHeight = appletHeight + 55;
		}
		if (bean != null) {
			raffleData.delete(raffleData.length() - 3, raffleData.length());
		}

		return appletHeight;
	}

	public static String getPromoData(Object bean, String promoOriginalData) {
		StringBuilder finalData = new StringBuilder("");
		if (bean != null) {
			if (bean instanceof List) {
				// To Be Implemented Later...
			} else {
				String gameName = "";
				String tktNo = "";
				String rpcCount = "";
				String saleStatus = "";
				String purchaseTime = "";
				String gameDispName = "";
				List drawDateList = null;
				StringBuilder drawDateTime = new StringBuilder("");
				StringBuilder pickNumStr = new StringBuilder("");
				int expiryPeriod = 0;
				int noOfDraws = 0;
				double totPurchaseAmt = 0.0;
				StringBuilder topMsgsStr = new StringBuilder(" ");
				StringBuilder bottomMsgsStr = new StringBuilder(" ");
				StringBuilder raffleData = new StringBuilder(" ");
				int totalQuantity = 0;
				StringBuilder gameDependentData = new StringBuilder("");

				if (bean instanceof FortunePurchaseBean) {
					FortunePurchaseBean fortuneBean = (FortunePurchaseBean) bean;
					gameName = Util.getGameName(fortuneBean.getGame_no());
					tktNo = fortuneBean.getTicket_no();
					rpcCount = fortuneBean.getReprintCount();
					saleStatus = fortuneBean.getSaleStatus();
					purchaseTime = fortuneBean.getPurchaseTime();
					gameDispName = fortuneBean.getGameDispName();
					drawDateList = fortuneBean.getDrawDateTime();

					List<Integer> pickedNumbers = fortuneBean
							.getPickedNumbers();
					pickNumStr = new StringBuilder("");
					for (int pickNum : pickedNumbers) {
						pickNumStr.append(pickNum + ",");
					}
					if (pickNumStr.length() > 0) {
						pickNumStr.deleteCharAt(pickNumStr.length() - 1);
					}

					int[] betAmtMulti = fortuneBean.getBetAmountMultiple();
					StringBuilder betAmtStr = new StringBuilder("");
					for (int betAmt : betAmtMulti) {
						betAmtStr.append(betAmt + ",");
						totalQuantity = totalQuantity + betAmt;
					}
					if (betAmtStr.length() > 0) {
						betAmtStr.deleteCharAt(betAmtStr.length() - 1);
					}

					expiryPeriod = Util.getGameMap().get(
							Util.getGameName(fortuneBean.getGame_no()))
							.getTicketExpiryPeriod();
					noOfDraws = fortuneBean.getNoOfDraws();
					totPurchaseAmt = fortuneBean.getTotalPurchaseAmt();

					int appHeight = 0;
					appHeight = getAdvMsgs(fortuneBean.getAdvMsg(),
							topMsgsStr, bottomMsgsStr, appHeight);

					if (fortuneBean.getRafflePurchaseBeanList() != null) {
						appHeight = getRaffPurchaseData(fortuneBean
								.getRafflePurchaseBeanList(), raffleData,
								appHeight);
					}

					gameDependentData.append("|isQP=" + fortuneBean.getIsQP()
							+ "|betAmountMultiple=" + betAmtStr
							+ "|totalQuantity=" + totalQuantity);

				} else if (bean instanceof LottoPurchaseBean) {
					LottoPurchaseBean lottoBean = (LottoPurchaseBean) bean;
					gameName = Util.getGameName(lottoBean.getGame_no());
					tktNo = lottoBean.getTicket_no();
					rpcCount = lottoBean.getReprintCount();
					saleStatus = lottoBean.getSaleStatus();
					purchaseTime = lottoBean.getPurchaseTime();
					gameDispName = lottoBean.getGameDispName();
					drawDateList = lottoBean.getDrawDateTime();

					List<String> pickedNumbers = lottoBean.getPlayerPicked();
					pickNumStr = new StringBuilder("");
					for (String pickNum : pickedNumbers) {
						pickNumStr.append(pickNum + ";");
					}
					if (pickNumStr.length() > 0) {
						pickNumStr.deleteCharAt(pickNumStr.length() - 1);
					}

					expiryPeriod = Util.getGameMap().get(
							Util.getGameName(lottoBean.getGame_no()))
							.getTicketExpiryPeriod();
					noOfDraws = lottoBean.getNoOfDraws();
					totPurchaseAmt = lottoBean.getTotalPurchaseAmt();

					int appHeight = 0;

					appHeight = getAdvMsgs(lottoBean.getAdvMsg(),
							topMsgsStr, bottomMsgsStr, appHeight);

					if (lottoBean.getRafflePurchaseBeanList() != null) {
						appHeight = getRaffPurchaseData(lottoBean
								.getRafflePurchaseBeanList(), raffleData,
								appHeight);
					}

					totalQuantity = lottoBean.getPanel_id().length;

					StringBuilder quickPickStr = new StringBuilder("");
					for (int quickPick : lottoBean.getIsQuickPick()) {
						quickPickStr.append(quickPick + ",");
					}
					if (quickPickStr.length() > 0) {
						quickPickStr.deleteCharAt(quickPickStr.length() - 1);
					}

					gameDependentData.append("isQuickPickArray=" + quickPickStr
							+ "|totalQuantity=" + totalQuantity);
				} else if (bean instanceof KenoPurchaseBean) {
					KenoPurchaseBean kenoBean = (KenoPurchaseBean) bean;
					gameName = Util.getGameName(kenoBean.getGame_no());
					tktNo = kenoBean.getTicket_no();
					rpcCount = kenoBean.getReprintCount();
					saleStatus = kenoBean.getSaleStatus();
					purchaseTime = kenoBean.getPurchaseTime();
					gameDispName = kenoBean.getGameDispName();
					drawDateList = kenoBean.getDrawDateTime();
					expiryPeriod = Util.getGameMap().get(
							Util.getGameName(kenoBean.getGame_no()))
							.getTicketExpiryPeriod();
					noOfDraws = kenoBean.getNoOfDraws();
					totPurchaseAmt = kenoBean.getTotalPurchaseAmt();

					int appHeight = 0;

					appHeight = getAdvMsgs(kenoBean.getAdvMsg(),
							topMsgsStr, bottomMsgsStr, appHeight);

					if (kenoBean.getRafflePurchaseBeanList() != null) {
						appHeight = getRaffPurchaseData(kenoBean
								.getRafflePurchaseBeanList(), raffleData,
								appHeight);
					}

					String[] playerDataApp = kenoBean.getPlayerData();
					String[] playType = kenoBean.getPlayType();
					int[] noOfLines = kenoBean.getNoOfLines();
					double[] unitPrice = kenoBean.getUnitPrice();
					int[] betAmtMul = kenoBean.getBetAmountMultiple();
					int[] isQP = kenoBean.getIsQuickPick();
					double[] panelPrice = new double[playType.length];

					StringBuilder playerDataStr = new StringBuilder(" ");
					StringBuilder playTypeStr = new StringBuilder(" ");
					StringBuilder noOfLinesStr = new StringBuilder(" ");
					StringBuilder unitPriceStr = new StringBuilder(" ");
					StringBuilder isQPStr = new StringBuilder(" ");
					StringBuilder panelPriceStr = new StringBuilder(" ");

					if (playerDataApp.length > 0) {
						playerDataStr.deleteCharAt(playerDataStr.length() - 1);
						playTypeStr.deleteCharAt(playTypeStr.length() - 1);
						noOfLinesStr.deleteCharAt(noOfLinesStr.length() - 1);
						unitPriceStr.deleteCharAt(unitPriceStr.length() - 1);
						isQPStr.deleteCharAt(isQPStr.length() - 1);
						panelPriceStr.deleteCharAt(panelPriceStr.length() - 1);

						for (int panelId = 0; panelId < playerDataApp.length; panelId++) {
							playerDataStr.append(playerDataApp[panelId] + "~");
							playTypeStr.append(playType[panelId] + "~");
							noOfLinesStr.append(noOfLines[panelId] + "~");
							unitPriceStr
									.append((unitPrice[panelId] * betAmtMul[panelId])
											+ "~");
							isQPStr.append(isQP[panelId] + "~");
							panelPriceStr
									.append((unitPrice[panelId]
											* betAmtMul[panelId]
											* noOfLines[panelId] * noOfDraws)
											+ "~");

						}

						playerDataStr.deleteCharAt(playerDataStr.length() - 1);
						playTypeStr.deleteCharAt(playTypeStr.length() - 1);
						noOfLinesStr.deleteCharAt(noOfLinesStr.length() - 1);
						unitPriceStr.deleteCharAt(unitPriceStr.length() - 1);
						isQPStr.deleteCharAt(isQPStr.length() - 1);
						panelPriceStr.deleteCharAt(panelPriceStr.length() - 1);
					}

					pickNumStr = playerDataStr;

					gameDependentData.append("|isQP=" + isQPStr
							+ "|totalQuantity=" + noOfLinesStr + "|playType="
							+ playTypeStr + "|unitPriceStr=" + unitPriceStr
							+ "|panelPriceStr=" + panelPriceStr);
				}
				for (int i = 0; i < drawDateList.size(); i++) {
					drawDateTime.append(drawDateList.get(i) + ",");
				}
				if (drawDateTime.length() > 0) {
					drawDateTime.deleteCharAt(drawDateTime.length() - 1);
				}

				finalData.append("data=" + tktNo + rpcCount + "|gameName="
						+ gameName + "|mode=Buy" + "|saleStatus=" + saleStatus
						+ promoOriginalData + "|reprintCount=" + rpcCount
						+ "|purchaseTime=" + purchaseTime + "|gameDispName="
						+ gameDispName + "|ticketNumber=" + tktNo
						+ "|drawDateTime=" + drawDateTime + "|pickedNumbers="
						+ pickNumStr + "|expiryPeriod=" + expiryPeriod
						+ "|noOfDraws=" + noOfDraws + "|totalPurchaseAmt="
						+ totPurchaseAmt + "|topAdvMsg=" + topMsgsStr
						+ "|bottomAdvMsg=" + bottomMsgsStr + "|raffleData="
						+ raffleData + gameDependentData);
			}
		}
		return finalData.toString();
	}

	public static int commonPurchaseDataForApplet(StringBuilder commonData, CommonPurchaseBean commonBean, int appletHeight) {
		if (commonBean != null) {
			ServletContext application = ServletActionContext.getServletContext();
			String drawDateTimeStr = null;
			List drawDateList = commonBean.getDrawDateTime();
			if (drawDateList != null) {
				drawDateTimeStr = Util.convertCollToStr(drawDateList).replaceAll(", ", ",");
				appletHeight = appletHeight + drawDateList.size() * 11;
				appletHeight = appletHeight + (drawDateTimeStr.split("\\*").length - 1) * 11; /* FOR DRAW NAME*/
			}

			StringBuilder topMsgsStr = new StringBuilder(" ");
			StringBuilder bottomMsgsStr = new StringBuilder(" ");

			appletHeight = getAdvMsgs(commonBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);

			StringBuilder raffleData = new StringBuilder(" ");
			if (commonBean.getRafflePurchaseBeanList() != null) {
				appletHeight = getRaffPurchaseData(commonBean.getRafflePurchaseBeanList(), raffleData, appletHeight);
			}

			commonData.append("data=" + commonBean.getTicket_no() + commonBean.getReprintCount()
					+ "|gameName=" + Util.getGameName(commonBean.getGame_no())
					+ "|mode=Buy|saleStatus=" + commonBean.getSaleStatus()
					+ "|reprintCount=" + commonBean.getReprintCount()
					+ "|purchaseTime=" + commonBean.getPurchaseTime()
					+ "|gameDispName=" + commonBean.getGameDispName()
					+ "|ticketNumber=" + commonBean.getTicket_no()
					+ "|drawDateTime=" + drawDateTimeStr
					+ "|expiryPeriod=" + Util.getGameMap().get(Util.getGameName(commonBean.getGame_no())).getTicketExpiryPeriod()
					+ "|noOfDraws=" + commonBean.getNoOfDraws()
					+ "|totalPurchaseAmt=" + commonBean.getTotalPurchaseAmt()
					+ "|topAdvMsg=" + topMsgsStr
					+ "|bottomAdvMsg=" + bottomMsgsStr
					+ "|raffleData=" + raffleData
					+ "|retailerName=" + Util.getOrgNameFromTktNo((commonBean.getTicket_no() + commonBean.getReprintCount()), (String) application.getAttribute("ORG_TYPE_ON_TICKET"))
					+ commonContextPurchaseData());
		}
		return appletHeight;
	}

	public static String commonContextPurchaseData() {
		ServletContext application = ServletActionContext.getServletContext();
		StringBuilder commonData = new StringBuilder("");
		commonData.append("|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL") 
						+ "|orgName=" + application.getAttribute("ORG_NAME_JSP") 
						+ "|advtMsg=" + application.getAttribute("ADVT_MSG") 
						+ "|orgAddress=" + application.getAttribute("ORG_ADDRESS") 
						+ "|orgMobile=" + application.getAttribute("ORG_MOBILE")
						+ "|ticketExpiryEnabled=" + application.getAttribute("TICKET_EXPIRY_ENABLED"));
		return commonData.toString();
	}
	
	/**
	 *  This method was commented because it was not in use in LMS anywhere - Sumit Date : 16April 2013 (Code Optimization)
	 * @param fmlyWiseData
	 * @param commonBean
	 * @param appletHeight
	 * @return
	 */

	/*public static int fmlyWisePurchaseDataForApplet(StringBuilder fmlyWiseData,
			CommonPurchaseBean commonBean, int appletHeight) {
		if (commonBean != null) {
			String fmlyType = Util.getGameType(commonBean.getGame_no());
			if ("Fortune".equalsIgnoreCase(fmlyType)) {
				appletHeight = fortuneFmlyPurchaseDataForApplet(fmlyWiseData,
						commonBean, appletHeight);
			} else if ("Lotto".equalsIgnoreCase(fmlyType)) {
				appletHeight = lottoFmlyPurchaseDataForApplet(fmlyWiseData,
						commonBean, appletHeight);
			} else if ("Keno".equalsIgnoreCase(fmlyType)) {
				appletHeight = kenoFmlyPurchaseDataForApplet(fmlyWiseData,
						commonBean, appletHeight);
			}
		}
		return appletHeight;
	}*/

	public static int fortuneFmlyPurchaseDataForApplet(
			StringBuilder fmlyWiseData, CommonPurchaseBean commonBean,
			int appletHeight) {
		FortunePurchaseBean fortuneBean = (FortunePurchaseBean) commonBean;
		StringBuilder betMulStr = new StringBuilder("");
		StringBuilder pickNumStr = new StringBuilder("");
		int totalQuantity = 0;

		List<Integer> pickNum = fortuneBean.getPickedNumbers();
		int[] betMul = fortuneBean.getBetAmountMultiple();

		for (int i = 0; i < pickNum.size(); i++) {
			pickNumStr.append(pickNum.get(i) + ",");
			betMulStr.append(betMul[i] + ",");
			totalQuantity = totalQuantity + betMul[i];
		}

		appletHeight = appletHeight + pickNum.size() * 11;
		fmlyWiseData.append("|pickedNumbers=" + pickNumStr.toString()
				+ "|betAmountMultiple=" + betMulStr.toString() + "|isQP="
				+ fortuneBean.getIsQP() + "|totalQuantity=" + totalQuantity);
		return appletHeight;
	}

	public static int lottoFmlyPurchaseDataForApplet(
			StringBuilder fmlyWiseData, CommonPurchaseBean commonBean,
			int appletHeight) {
		LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) commonBean;

		String pickNumStr = Util.convertCollToStr(
				lottoPurchaseBean.getPlayerPicked()).replaceAll(", ", ";");
		StringBuilder quickPickArray = new StringBuilder("");
		for (int i = 0; i < lottoPurchaseBean.getIsQuickPick().length; i++) {
			quickPickArray.append(lottoPurchaseBean.getIsQuickPick()[i] + ",");
		}

		int totalQuantity = lottoPurchaseBean.getPanel_id().length;
		if ("Perm6".equalsIgnoreCase(lottoPurchaseBean.getPlayType())) {
			totalQuantity = lottoPurchaseBean.getNoOfLines();
		}

		appletHeight = appletHeight
				+ lottoPurchaseBean.getPlayerPicked().size() * 11;
		fmlyWiseData.append("|pickedNumbers=" + pickNumStr
				+ "|isQuickPickArray=" + quickPickArray.toString()
				+ "|playType=" + lottoPurchaseBean.getPlayType()
				+ "|unitPrice=" + lottoPurchaseBean.getUnitPrice()
				+ "|totalQuantity=" + totalQuantity);
		return appletHeight;
	}

	/**
	 *  This method was commented because it was not in use in LMS anywhere - Sumit Date : 16April 2013 (Code Optimization)
	 * @param fmlyWiseData
	 * @param commonBean
	 * @param appletHeight
	 * @return
	 */
/*	public static int kenoFmlyPurchaseDataForApplet(StringBuilder fmlyWiseData,
			CommonPurchaseBean commonBean, int appletHeight) {
		KenoPurchaseBean kenoBean = (KenoPurchaseBean) commonBean;

		int noOfDraws = kenoBean.getNoOfDraws();

		String[] playerDataApp = kenoBean.getPlayerData();
		String[] playType = kenoBean.getPlayType();
		int[] noOfLines = kenoBean.getNoOfLines();
		double[] unitPrice = kenoBean.getUnitPrice();
		int[] betAmtMul = kenoBean.getBetAmountMultiple();
		int[] isQP = kenoBean.getIsQuickPick();

		StringBuilder playerDataStr = new StringBuilder(" ");
		StringBuilder playTypeStr = new StringBuilder(" ");
		StringBuilder noOfLinesStr = new StringBuilder(" ");
		StringBuilder unitPriceStr = new StringBuilder(" ");
		StringBuilder isQPStr = new StringBuilder(" ");
		StringBuilder panelPriceStr = new StringBuilder(" ");

		if (playerDataApp.length > 0) {
			playerDataStr.deleteCharAt(playerDataStr.length() - 1);
			playTypeStr.deleteCharAt(playTypeStr.length() - 1);
			noOfLinesStr.deleteCharAt(noOfLinesStr.length() - 1);
			unitPriceStr.deleteCharAt(unitPriceStr.length() - 1);
			isQPStr.deleteCharAt(isQPStr.length() - 1);
			panelPriceStr.deleteCharAt(panelPriceStr.length() - 1);

			for (int panelId = 0; panelId < playerDataApp.length; panelId++) {
				playerDataStr.append(playerDataApp[panelId] + "~");
				playTypeStr.append(playType[panelId].replaceAll("Perm", "Pick")
						+ "~");
				noOfLinesStr.append(noOfLines[panelId] + "~");
				if("KenoTwo".equalsIgnoreCase(Util.getGameName(commonBean.getGame_no()))){
					unitPriceStr.append(new DecimalFormat("#.#").format(unitPrice[panelId] * betAmtMul[panelId]) + "~");
				} else if("Keno".equalsIgnoreCase(Util.getGameName(commonBean.getGame_no()))){
					unitPriceStr.append((unitPrice[panelId] * betAmtMul[panelId]) + "~");
				}
				
				isQPStr.append(isQP[panelId] + "~");
				panelPriceStr.append((unitPrice[panelId] * betAmtMul[panelId]
						* noOfLines[panelId] * noOfDraws)
						+ "~");

				appletHeight = appletHeight + 66;
			}

			playerDataStr.deleteCharAt(playerDataStr.length() - 1);
			playTypeStr.deleteCharAt(playTypeStr.length() - 1);
			noOfLinesStr.deleteCharAt(noOfLinesStr.length() - 1);
			unitPriceStr.deleteCharAt(unitPriceStr.length() - 1);
			isQPStr.deleteCharAt(isQPStr.length() - 1);
			panelPriceStr.deleteCharAt(panelPriceStr.length() - 1);
		}

		String finalData = "|pickedNumbers=" + playerDataStr + "|isQP="
				+ isQPStr + "|totalQuantity=" + noOfLinesStr + "|playType="
				+ playTypeStr + "|unitPriceStr=" + unitPriceStr
				+ "|panelPriceStr=" + panelPriceStr;
		System.out.println("KENO TWO PURCHASE DATA: " + finalData);

		String promoData = "";

		if (kenoBean.getPromoPurchaseBean() != null) {
			finalData = finalData + "|ctr=" + (appletHeight + 100 + 300);
			promoData = getPromoData(kenoBean.getPromoPurchaseBean(),
					commonContextPurchaseData());
			if (promoData.length() > 0) {
				promoData = "PROMO" + promoData + "|ctr="
						+ (appletHeight + 100 + 300);
			}
		} else {
			finalData = finalData + "|ctr=" + (appletHeight + 100);
		}
		String appData = finalData + promoData;
		System.out.println("KENO TWO PURCHASE CUM PROMO DATA: " + appData);
		fmlyWiseData.append(appData);
		return appletHeight;
	}
	*/
	public static int getTempReceiptData(HttpSession session, StringBuilder tempRecptData, StringBuilder isTempRcpt, int appletHeight){
		Map pwtAppMap = (Map) session.getAttribute("plrPwtAppDetMap");
		boolean isAnonymous = true;
		PlayerBean plrBean = null;
		String recId = null;
		double netAmtToBePaid = 0.0;
				
		if(pwtAppMap != null && pwtAppMap.size() > 0){
			isAnonymous = (Boolean)pwtAppMap.get("isAnonymous");
			plrBean = (PlayerBean)pwtAppMap.get("plrBean");
			recId = (String)pwtAppMap.get("recId");
			netAmtToBePaid = (Double)pwtAppMap.get("NET_AMOUNT_PAID");
		}
		if(!isAnonymous && plrBean != null && recId != null && netAmtToBePaid > 0.0){
			isTempRcpt.delete(0, isTempRcpt.length());
			isTempRcpt.append("true");
			tempRecptData.append("|tempRecptData=true");
			
			NumberFormat numFrmt = NumberFormat.getInstance();
			numFrmt.setMinimumFractionDigits(2);
						
			tempRecptData.append("|receiptId=" + recId 
							+ "|playerName=" + plrBean.getFirstName() + " " + plrBean.getLastName()
							+ "|netAmtToBePaid=" + numFrmt.format(netAmtToBePaid).replaceAll(",", "")	
							+ "|playerCity=" + plrBean.getPlrCity()
							+ "|playerIdType=" + plrBean.getIdType()
							+ "|playerIdNo=" + plrBean.getIdNumber()
							+ "|rcptGenTime=" + new Timestamp(new Date().getTime()).toString()		
			);
			appletHeight = appletHeight + 225;
		} else {
			tempRecptData.append("|tempRecptData=false");
		}
		return appletHeight;
	}
	
	/**
	 * This method was commented because it was not in use in LMS anywhere - Sumit Date : 16April 2013 (Code Optimization)
	 * @param winningBean
	 * @param pwtData
	 * @param isReprint
	 * @param isFortune
	 * @param isTempRcpt
	 * @param appletHeight
	 * @return
	 */
	/*public static int pwtDataForApplet(MainPWTDrawBean mainPWTBean, StringBuilder finalData, int appletHeight){
		if(mainPWTBean != null){
			HttpSession session = new UtilApplet().getRequest().getSession();
			StringBuilder tempRecptData = new StringBuilder("");
			StringBuilder isTempRcpt = new StringBuilder("false");
			if(session != null){
				appletHeight = getTempReceiptData(session, tempRecptData, isTempRcpt, appletHeight);
			}
			
			boolean isReprint = mainPWTBean.isReprint();
			if(isReprint){
				CommonPurchaseBean commonBean = (CommonPurchaseBean) mainPWTBean.getPurchaseBean();
				StringBuilder purchaseReprintData = new StringBuilder("");
				StringBuilder fmlyWiseData = new StringBuilder("");
				
				appletHeight = commonPurchaseDataForApplet(purchaseReprintData, commonBean, appletHeight);
				appletHeight = fmlyWisePurchaseDataForApplet(fmlyWiseData, commonBean, appletHeight);
				
				finalData.append(purchaseReprintData);
			}
			
			List<PWTDrawBean> winningBeanList = mainPWTBean.getWinningBeanList();
			if(winningBeanList != null && winningBeanList.size() > 0){
				boolean isFortune = false;
				String fmlyType = Util.getGameType(2);
				if("Fortune".equalsIgnoreCase(fmlyType)){
					isFortune = true;
				}
				for (int k = 0; k < winningBeanList.size(); k++) {
					PWTDrawBean winningBean = winningBeanList.get(k);
					if(winningBean != null){
						String pwtTktType = winningBean.getPwtTicketType();
						if("DRAW".equalsIgnoreCase(pwtTktType)){
							StringBuilder pwtData = new StringBuilder("");
							appletHeight = commonPwtDataForApplet(winningBean, pwtData, isReprint, isFortune, isTempRcpt, appletHeight);
							finalData.append(pwtData);
						} else if("RAFFLE".equalsIgnoreCase(pwtTktType)){
							StringBuilder raffleData = new StringBuilder(" ");
							if (winningBean.getRaffleDrawIdBeanList() != null) {
								appletHeight = getRaffPWTData(winningBean.getRaffleDrawIdBeanList(), raffleData,appletHeight);
							}
							finalData.append("|raffPWTData=" + raffleData);
						}
					}
				}
			}
			finalData.append(tempRecptData);
		}
		return appletHeight;
	}*/
	
	public static int commonPwtDataForApplet(PWTDrawBean winningBean, StringBuilder pwtData, boolean isReprint, boolean isFortune, StringBuilder isTempRcpt, int appletHeight){
		ServletContext application = ServletActionContext.getServletContext();
		
		
		List<DrawIdBean> drawWinList = winningBean.getDrawWinList();
		int winSize = drawWinList.size();
		
		int[] nonWin = new int[winSize];
		int[] win = new int[winSize];
		int[] register = new int[winSize];
		int[] claim = new int[winSize];
		int[] outVerify = new int[winSize];
		int[] pndPay = new int[winSize];
		double[] drawAmt = new double[winSize];
		boolean[] isResAwaited = new boolean[winSize];
		
		int totRegister = 0;
		double totTktAmt = 0.0;
		
		StringBuilder nonWinStr = new StringBuilder("");
		StringBuilder winStr = new StringBuilder("");
		StringBuilder registerStr = new StringBuilder("");
		StringBuilder claimStr = new StringBuilder("");
		StringBuilder outVerifyStr = new StringBuilder("");
		StringBuilder pndPayStr = new StringBuilder("");
		StringBuilder resultAwaitedStr = new StringBuilder("");
		StringBuilder drawAmtStr = new StringBuilder("");
		StringBuilder drawDateTimeStr = new StringBuilder("");
		
		for (int i = 0; i < winSize; i++) {
			DrawIdBean drawBean = drawWinList.get(i);
			drawDateTimeStr.append(drawBean.getDrawDateTime() + ",");
			List<PanelIdBean> panelWinList = drawBean.getPanelWinList();
			if(panelWinList != null){
				for (int j = 0; j < panelWinList.size(); j++) {
					PanelIdBean panelBean = panelWinList.get(j);
					if(panelBean != null){
						String panelStatus = panelBean.getStatus();
						int betMultiple = 1;
						
						if(isFortune){
							betMultiple = panelBean.getBetAmtMultiple();
						}
						
						if("NON WIN".equalsIgnoreCase(panelBean.getStatus())) {
							nonWin[i] += betMultiple;
						} else if("NORMAL_PAY".equalsIgnoreCase(panelStatus)){
							drawAmt[i] = drawAmt[i] + panelBean.getWinningAmt();
							win[i] += betMultiple;
						} else if("CLAIMED".equalsIgnoreCase(panelStatus)){
							claim[i] += betMultiple;
						} else if("PND_PAY".equalsIgnoreCase(panelStatus)){
							pndPay[i] += betMultiple;
						} else if("HIGH_PRIZE".equalsIgnoreCase(panelStatus)){
							register[i] += betMultiple;
						} else if("OUT_PAY_LIMIT".equalsIgnoreCase(panelStatus)){
							register[i] += betMultiple;
						} else if("OUT_VERIFY_LIMIT".equalsIgnoreCase(panelStatus)){
							outVerify[i] += betMultiple;
						} 
					}
				}
				
				nonWinStr.append(nonWin[i] + ",");
				drawAmtStr.append(drawAmt[i] + ",");
				winStr.append(win[i] + ",");
				claimStr.append(claim[i] + ",");
				pndPayStr.append(pndPay[i] + ",");
				registerStr.append(register[i] + ",");
				outVerifyStr.append(outVerify[i] + ",");
				isResAwaited[i] = false;
				resultAwaitedStr.append(isResAwaited[i] + ",");
				totRegister = totRegister + register[i];
			} else {
				nonWinStr.append("NA,");
				drawAmtStr.append("NA,");
				winStr.append("NA,");
				claimStr.append("NA,");
				pndPayStr.append("NA,");
				registerStr.append("NA,");
				outVerifyStr.append("NA,");
				isResAwaited[i] = true;
				resultAwaitedStr.append(isResAwaited[i] + ",");
				
			}
			totTktAmt = drawAmt[i] + totTktAmt;
		}
		
		if(!"true".equalsIgnoreCase(isTempRcpt.toString())){
			StringBuilder topMsgsStrPwt = new StringBuilder(" ");
			StringBuilder bottomMsgsStrPwt = new StringBuilder(" ");
			appletHeight = UtilApplet.getAdvMsgs(winningBean.getAdvMsg(), topMsgsStrPwt, bottomMsgsStrPwt, appletHeight);	
			
			if(!isReprint){
				pwtData.append("data=" + winningBean.getTicketNo() + winningBean.getReprintCount());
			}

			pwtData.append("|reprintCountPWT=" + winningBean.getReprintCount()
						+ "|mode=PWT|orgName=" + application.getAttribute("ORG_NAME_JSP")
						+ "|advtMsg=" + application.getAttribute("ADVT_MSG")
						+ "|isValid=" + String.valueOf(winningBean.isValid())
						+ "|isReprint=" + String.valueOf(isReprint)
						+ "|gameName=" + Util.getGameName(winningBean.getGameNo())
						+ "|gameDispName=" + winningBean.getGameDispName()
						+ "|ticketNumber=" + winningBean.getTicketNo()
						+ "|drawDateTime=" + drawDateTimeStr.toString()
						+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL")
						+ "|status=" + winningBean.getStatus()
						+ "|topAdvMsgPwt=" + topMsgsStrPwt
						+ "|bottomAdvMsgPwt=" + bottomMsgsStrPwt
						+ "|nonWinStr=" + nonWinStr.toString()
						+ "|winStr=" + winStr.toString()
						+ "|registerStr=" + registerStr.toString()
						+ "|claimStr=" + claimStr.toString()
						+ "|outVerifyStr=" + outVerifyStr.toString()
						+ "|pndPayStr=" + pndPayStr.toString()
						+ "|resultAwaitedStr=" + resultAwaitedStr.toString()
						+ "|retailerName=" + Util.getOrgNameFromTktNo((winningBean.getTicketNo() + winningBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
						+ "|drawAmtStr=" + drawAmtStr.toString());
				
		} else {
			if(!isReprint){
				pwtData.append("data=" + winningBean.getTicketNo() + winningBean.getReprintCount() + "|");
				
				pwtData.append("mode=PWT|gameDispName=" + winningBean.getGameDispName()
						+ "|ticketNumber=" + winningBean.getTicketNo()
						+ "|orgName=" + application.getAttribute("ORG_NAME_JSP")
						+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL"));
			}
			
			pwtData.append("|reprintCountPWT=" + winningBean.getReprintCount()
					+ "|mode=PWT|isReprint=" + String.valueOf(isReprint)
					+ "|retailerName=" + Util.getOrgNameFromTktNo((winningBean.getTicketNo() + winningBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
					+ "|drawDateTime=" + drawDateTimeStr.toString());
		}
		System.out.println("**pwtData**isTempRcpt***" + pwtData + "***");
		return appletHeight;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

}
