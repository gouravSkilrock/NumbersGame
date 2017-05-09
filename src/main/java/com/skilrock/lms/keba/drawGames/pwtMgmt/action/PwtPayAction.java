package com.skilrock.lms.keba.drawGames.pwtMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.MpesaPaymentProcessHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class PwtPayAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public PwtPayAction() {
		super(PwtPayAction.class);
	}

	@SuppressWarnings("unchecked")
	public void payPwtTicket() {
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawDataBean = null;
		String gameName = null;
		PrintWriter out = null;
		try {
			logger.info("Pay Pwt Request Data : " + request.getParameter("requestData"));
			JSONObject payPwtRequestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
	
			response.setContentType("application/json");
			out = response.getWriter();

			String userName = payPwtRequestData.getString("userName").trim();
			String ticketNumber = payPwtRequestData.getString("ticketNumber").trim();
			long lastTktNo = Long.parseLong(payPwtRequestData.getString("lastTktNo").trim());
			String mPesa = payPwtRequestData.getString("mPesa").trim();
			String mobileNo = payPwtRequestData.getString("mobileNo").trim();

			String isDraw = (String) sc.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Draw Game Not Available.");
				return;
			}

			UserInfoBean userInfoBean = getUserBean(userName);

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
			MainPWTDrawBean mainWinningBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");

			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

			int autoCancelHoldDays = Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			long lastPrintedTicket = 0;
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int gameId = 0;
			if(lastTktNo != 0) {
				lastPrintedTicket = lastTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(lastTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTktNo)));
			}
			String actionName = ActionContext.getContext().getName();
			helper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);

			AjaxRequestHelper ajaxRequestHelper = new AjaxRequestHelper();
			ajaxRequestHelper.getAvlblCreditAmt(userInfoBean);
			double balance1 = userInfoBean.getAvailableCreditLimit() - userInfoBean.getClaimableBal();
			String highPrizeCriteria = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_CRITERIA");
			String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
			String highPrizeScheme = (String) LMSUtility.sc.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");

			if("Y".equalsIgnoreCase(mPesa) && mobileNo!=null){
				mainWinningBean.setIsmPesaEnable(true);
				mainWinningBean.setMobileNumber(mobileNo);
				UserInfoBean mPesaUserBean = new MpesaPaymentProcessHelper().payBymPesaAcc(mainWinningBean, userInfoBean);
				mainWinningBean = helper.payPwtTicket(mainWinningBean, mPesaUserBean,highPrizeCriteria,highPrizeAmt,highPrizeScheme);
			} else {
				mainWinningBean = helper.payPwtTicket(mainWinningBean, userInfoBean,highPrizeCriteria,highPrizeAmt,highPrizeScheme);
			}

			if ("PWT_LIMIT_EXCEED".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "PWT Limit Exceed.");
				return;
			}

			StringBuilder stBuiald = new StringBuilder("");
			double totalRaffleAmount = 0.0;
			double totTktAmt = 0.0;
			double totalClmPndAmt=0.0;

			/*
			String raffleData = EmbeddedErrors.RAFFLE_DATA;
			if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
				List<RaffleDrawIdBean> raffleDrawIdBeanList = mainWinningBean.getWinningBeanList().get(0).getRaffleDrawIdBeanList();
				if (raffleDrawIdBeanList != null) {
					for (int j = 0; j < raffleDrawIdBeanList.size(); j++) {
						if (j > 0) {
							raffleData = raffleData + ";";
						}
						RaffleDrawIdBean raffleWinningBean = raffleDrawIdBeanList.get(j);
						String dataArr[] = buildPWTRaffleData(raffleWinningBean).split("RWA");
						String data = dataArr[0];
						if (dataArr.length > 1)
							totalRaffleAmount = Double.parseDouble(dataArr[1]);

						raffleData = raffleData + data;
					}
				}
				raffleData = raffleData + "#" + "|Total Pay:" + totalRaffleAmount
				+ "|";
			} else
			*/
			if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
				List<PWTDrawBean> pwtWinBeanlist = mainWinningBean.getWinningBeanList();
				for (int k = 0; k < pwtWinBeanlist.size(); k++) {
					PWTDrawBean pwtWinningBean = pwtWinBeanlist.get(k);
					/*
					if (pwtWinningBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
						List<RaffleDrawIdBean> raffleDrawIdBeanList = pwtWinningBean.getRaffleDrawIdBeanList();
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
								+ EmbeddedErrors.PWT_TICKET_EXPIRED;

							} else if (raffleWinningBean.getStatus().equals(
							"RES_AWAITED")) {
								raffleData = raffleData
								+ raffleWinningBean.getDrawDateTime()
								+ "|Awaited";

							} else if (raffleWinningBean.getPwtStatus() != null
									&& raffleWinningBean.getPwtStatus()
									.equalsIgnoreCase("OUT_VERIFY_LIMIT")) {
								raffleData = raffleData + "ErrorMsg:"
								+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;

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
								raffleData = raffleData
								+ raffleWinningBean.getDrawDateTime()
								+ "|WIN "
								+ raffleWinningBean.getWinningAmt();

							} else if (raffleWinningBean.getStatus() != null
									&& raffleWinningBean.getStatus().equals(
									"CLAIMED")) {
								raffleData = raffleData
								+ raffleWinningBean.getDrawDateTime()
								+ "|CLAIMED";

							} else if (raffleWinningBean.getPwtStatus() != null
									&& raffleWinningBean.getPwtStatus().equals(
									"PND_PAY")) {
								raffleData = raffleData + "ErrorMsg:"
								+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;

							} else if (raffleWinningBean.getPwtStatus() != null
									&& raffleWinningBean.getPwtStatus().equals(
									"HIGH_PRIZE")) {
								raffleData = raffleData + "ErrorMsg:"
								+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;

							} else if (raffleWinningBean.getPwtStatus() != null
									&& raffleWinningBean.getPwtStatus().equals(
									"OUT_PAY_LIMIT")) {
								raffleData = raffleData + "ErrorMsg:"
								+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;

							} else if (raffleWinningBean.getPwtStatus() != null
									&& raffleWinningBean.getPwtStatus().equals(
									"OUT_VERIFY_LIMIT")) {
								raffleData = raffleData + "ErrorMsg:"
								+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;
							}

						}

					}
					*/
				}
				//raffleData = raffleData + "#";

				for (int k = 0; k < pwtWinBeanlist.size(); k++) {
					PWTDrawBean pwtWinningBean = pwtWinBeanlist.get(k);
					if (pwtWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
						int totRegister = 0;
						if (!pwtWinningBean.isValid()) {
							responseObject.put("isSuccess", false);
							responseObject.put("errorMsg", "Invalid Ticket.");
							return;
						} else if (pwtWinningBean.getStatus().equals("SUCCESS")) {
							for (int i = 0; i < pwtWinningBean.getDrawWinList().size(); i++) {
								DrawIdBean drawBean = pwtWinningBean.getDrawWinList().get(i);

								int nonWin = 0;
								int win = 0;
								int clm=0;
								int register = 0;
								int claim = 0;
								int outVerify = 0;
								int pndPay = 0;

								String message = null;

								boolean isExpired = false;
								boolean isVerPending = false;
								boolean isClmPending = false;
								String drawStatusForTicket = drawBean.getStatus();
								gameName = Util.getGameName(pwtWinningBean.getGameNo());

								boolean isResAwaited = false;
								double drawAmt = 0.0;
								List<PanelIdBean> panelWinList = drawBean.getPanelWinList();
								if (panelWinList != null && !drawStatusForTicket.equals("DRAW_EXPIRED")) {
									for (int j = 0; j < panelWinList.size(); j++) {
										PanelIdBean panelBean = panelWinList.get(j);
										if (panelBean.getStatus().equals("NON WIN")) {
											nonWin++;
										} else if (panelBean.getStatus().equals("NORMAL_PAY")) {
											drawAmt = panelBean.getWinningAmt() + drawAmt;
											win++;
										} else if (panelBean.getStatus().equals("CLAIMED")) {
											claim++;
										} else if (panelBean.getStatus().equals("PND_PAY")) {
											pndPay++;
										} else if (panelBean.getStatus().equals("HIGH_PRIZE")) {
											register++;
										} else if (panelBean.getStatus().equals("OUT_PAY_LIMIT")) {
											register++;
										} else if (panelBean.getStatus().equals("OUT_VERIFY_LIMIT")) {
											outVerify++;
										} else if (panelBean.getStatus().equals("CLAIM_PENDING")) {
											drawAmt = panelBean.getWinningAmt()+ drawAmt;
											totalClmPndAmt += panelBean.getWinningAmt();
											clm++;
										}
									}
								} else if (drawStatusForTicket.equals("DRAW_EXPIRED")) {
									isExpired = true;
								} else if (drawStatusForTicket.equals("VERIFICATION_PENDING")) {
									isVerPending = true;
								} else if (drawStatusForTicket.equals("CLAIM_PENDING")) {
									isClmPending = true;
								} else {
									isResAwaited = true;
								}
								totTktAmt = (drawStatusForTicket.equals("CLAIM_PENDING")?0.0:drawAmt) + totTktAmt;
								if (isExpired) {
									//stBuild.append(",No:0,Message:Draw EXP");
									message = "Draw EXP";
								} else if (isVerPending) {
									//stBuild.append(",No:0,Message:VER PEND");
									message = "VER PEND";
								} else if (isResAwaited) {
									//stBuild.append(",No:0,Message:Awaited");
									message = "Awaited";
								} else {
									if (nonWin != 0) {
										//stBuild.append(",No:" + nonWin + ",Message:TRY AGAIN");
										message = "TRY AGAIN";
									}
									if (win != 0) {
										//stBuild.append(",No:" + win + ",Message:WIN " + Util.getBalInString(drawAmt) + "");
										message = "WIN " + Util.getBalInString(drawAmt);
									}
									if (register != 0) {
										//stBuild.append(",No:" + register + ",Message:REG. REQ.");
										message = "REG. REQ.";
									}
									if (claim != 0) {
										//stBuild.append(",No:" + claim + ",Message:CLAIMED");
										message = "CLAIMED";
									}
									if (pndPay != 0) {
										//stBuild.append(",No:" + pndPay + ",Message:IN PROCESS");
										message = "IN PROCESS";
									}
									if (outVerify != 0) {
										//stBuild.append(",No:" + outVerify + ",Message:OUT OF VERIFY");
										message = "OUT OF VERIFY";
									}
									if(clm!=0){
										//stBuild.append(",No:" + clm + ",Message:CLM PND "+ Util.getBalInString(drawAmt));
										message = "CLM PND " + Util.getBalInString(drawAmt);
									}
								}
								totRegister = totRegister + register;

								String drawDateTime = drawBean.getDrawDateTime();
								drawDataBean = new JSONObject();
								drawDataBean.put("drawName", drawBean.getDrawname());
								drawDataBean.put("drawDate", drawDateTime.split(" ")[0]);
								drawDataBean.put("drawTime", drawDateTime.split(" ")[1].split("\\*")[0]);
								drawDataBean.put("panelWinning", win);
								drawDataBean.put("prizeAmount", drawAmt);
								drawDataBean.put("message", message);
								drawDataArray.add(drawDataBean);
							}
						} else if (pwtWinningBean.getStatus().equals("ERROR")) {
							responseObject.put("isSuccess", false);
							responseObject.put("errorMsg", "ERROR! Please Try Again.");
							return;
						}

						totTktAmt = totTktAmt + totalRaffleAmount;

						currentUserSessionMap.put(userName, session);
					}
				}
			}

			ajaxRequestHelper.getAvlblCreditAmt(userInfoBean);
			double balance2 = userInfoBean.getAvailableCreditLimit() - userInfoBean.getClaimableBal();
			double balance = balance2 - balance1;

			if (!mainWinningBean.isReprint()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				Date currentDate = new Date();
				responseObject.put("isSuccess", true);
				responseObject.put("errorMsg", "");
				if(ticketNumber.length() == 20)
					ticketNumber = ticketNumber.substring(0, ticketNumber.length()-2);
				responseObject.put("ticketNumber", ticketNumber);
				responseObject.put("currentDate", dateFormat.format(currentDate));
				responseObject.put("currentTime", timeFormat.format(currentDate));
				responseObject.put("gameName", gameName);
				responseObject.put("totalPay", totTktAmt);
				responseObject.put("totalClaimPending", totalClmPndAmt);
				responseObject.put("balance", balance2);
				responseObject.put("drawDataList", drawDataArray);
			}

			/*
			if (mainWinningBean.isReprint()) {
				Object Purchasebean = mainWinningBean.getPurchaseBean();
				if (Purchasebean instanceof FortunePurchaseBean) {
					int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean.getTicketNo());
					gameName = Util.getGameName(gamenbr);

					FortunePurchaseBean bean = (FortunePurchaseBean) mainWinningBean.getPurchaseBean();
					stBuild.append("RPRT:"
							+ ReprintHepler.reprintFortuneTicket(bean, gameName));
					String raffleDataRPRT = CommonMethods.buildRaffleData(bean
							.getRafflePurchaseBeanList());

					finalPwtData = raffleData + stBuild.toString() + "|Amt:"
					+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
					+ raffleDataRPRT;
				} else if (Purchasebean instanceof LottoPurchaseBean) {
					int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
							.getTicketNo());
					gameName = Util.getGameName(gamenbr);

					LottoPurchaseBean bean = (LottoPurchaseBean) mainWinningBean
					.getPurchaseBean();
					stBuild.append("RPRT:"
							+ ReprintHepler.reprintLottoTicket(bean, gameName));
					String raffleDataRPRT = CommonMethods.buildRaffleData(bean
							.getRafflePurchaseBeanList());

					finalPwtData = raffleData + stBuild.toString() + "|Amt:"
					+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
					+ raffleDataRPRT;
				} else if (Purchasebean instanceof KenoPurchaseBean) {
					int gamenbr = helper.getGamenoFromTktnumber(mainWinningBean
							.getTicketNo());
					gameName = Util.getGameName(gamenbr);

					KenoPurchaseBean bean = (KenoPurchaseBean) mainWinningBean
					.getPurchaseBean();
					stBuild.append("RPRT:"
							+ ReprintHepler.reprintKenoTicket(bean, gameName,userInfoBean.getTerminalBuildVersion(),countryDeployed));
					String raffleDataRPRT = CommonMethods.buildRaffleData(bean
							.getRafflePurchaseBeanList());

					finalPwtData = raffleData + stBuild.toString() + "|Amt:"
					+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
					+ raffleDataRPRT;
				} else if (Purchasebean instanceof FortuneTwoPurchaseBean) {
					int gameNbr = helper.getGamenoFromTktnumber(mainWinningBean
							.getTicketNo());
					gameName = Util.getGameName(gameNbr);
					FortuneTwoPurchaseBean bean = (FortuneTwoPurchaseBean) mainWinningBean
					.getPurchaseBean();
					stBuild
					.append("RPRT:"
							+ ReprintHepler.reprintFortuneTwoTicket(bean,
									gameName));
					String raffleDataRPRT = CommonMethods.buildRaffleData(bean
							.getRafflePurchaseBeanList());
					finalPwtData = raffleData + stBuild.toString() + "|Amt:"
					+ balance + "|QP:" + bean.getIsQuickPick()[0] + "|"
					+ raffleDataRPRT;
				} else if (Purchasebean instanceof FortuneThreePurchaseBean) {
					int gameNbr = helper.getGamenoFromTktnumber(mainWinningBean
							.getTicketNo());
					gameName = Util.getGameName(gameNbr);
					FortuneThreePurchaseBean bean = (FortuneThreePurchaseBean) mainWinningBean
					.getPurchaseBean();
					stBuild
					.append("RPRT:"
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
			*/
		} catch (IOException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "IOException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "Exception Occured.");
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorMsg", "Compile Time Error.");
				responseObject.put("isSuccess", false);
			}
			logger.info("Cancel Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
	}
}