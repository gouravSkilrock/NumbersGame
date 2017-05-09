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
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class PwtVerifyAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public PwtVerifyAction() {
		super(PwtVerifyAction.class);
	}

	@SuppressWarnings("unchecked")
	public void verifyTicket() {
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawDataBean = null;
		PrintWriter out = null;
		try {
			logger.info("Winning Request Data : " + request.getParameter("requestData"));
			JSONObject winningRequestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
	
			response.setContentType("application/json");
			out = response.getWriter();

			String userName = winningRequestData.getString("userName").trim();
			String ticketNumber = winningRequestData.getString("ticketNumber").trim();
			long lastTktNo = Long.parseLong(winningRequestData.getString("lastTktNo").trim());

			String isDraw = (String) sc.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Draw Game Not Available.");
				return;
			}

			//double beepAmt=Double.parseDouble((String)sc.getAttribute("AMOUNT_FOR_LONG_BEEP"));
			UserInfoBean userInfoBean = getUserBean(userName);
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			MainPWTDrawBean mainPwtBean = new MainPWTDrawBean();
			mainPwtBean.setInpType(Integer.parseInt((String) sc.getAttribute("InpType")));
			mainPwtBean.setTicketNo(String.valueOf(ticketNumber));

			AjaxRequestHelper ajaxRequestHelper = new AjaxRequestHelper();
			ajaxRequestHelper.getAvlblCreditAmt(userInfoBean);
			double balance1 = userInfoBean.getAvailableCreditLimit() - userInfoBean.getClaimableBal();

			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			MainPWTDrawBean mainWinningBean = helper.prizeWinningTicket(mainPwtBean, userInfoBean, refMerchantId);

			int autoCancelHoldDays = Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			long lastPrintedTicket = 0;
			int gameId = 0;
			if(lastTktNo != 0) {
				lastPrintedTicket = lastTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(lastTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTktNo)));
			}
			String actionName = ActionContext.getContext().getName();
			helper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);

			if ("ERROR".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Can Not Verify, Invalid PWT.");
				return;
			} else if ("FRAUD".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Can Not Verify, Invalid PWT.");
				return;
			} else if ("ERROR_INVALID".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Invalid Ticket. Device Would be Inactivated.");
				return;
			} else if ("CANCELLED".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Invalid Ticket. Device Would be Inactivated.");
				return;
			} else if ("UN_AUTH".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Can Not Verify, Invalid Sub-Agent.");
				return;
			} else if (mainWinningBean.getStatus() != null && mainWinningBean.getStatus().equalsIgnoreCase("OUT_VERIFY_LIMIT")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Can Not Verify, High Prize.");
				return;
			} else if ("HIGH_PRIZE".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Can Not Verify, High Prize.");
				return;
			} else if ("OUT_PAY_LIMIT".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Can Not Verify, High Prize.");
				return;
			} else if ("TICKET_EXPIRED".equalsIgnoreCase(mainWinningBean.getStatus())) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Expired or Invalid Ticket.");
				return;
			}

			double totTktAmt = 0.0;
			double totalClmPndAmt = 0.0;
			int totRegister = 0;

			//String raffleData = EmbeddedErrors.RAFFLE_DATA;
			/*
			if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
				double totalRaffleAmount = 0.0;
				PWTDrawBean pwtwinBean = mainWinningBean.getWinningBeanList().get(0);
				RaffleDrawIdBean raffleWinningBean = (RaffleDrawIdBean) pwtwinBean.getRaffleDrawIdBeanList().get(0);
				String dataArr[] = buildPWTRaffleData(raffleWinningBean).split("RWA*");
				String data = dataArr[0];
				if (dataArr.length > 1) {
					totalRaffleAmount = Double.parseDouble(dataArr[1]);
				}
				raffleData = raffleData + data + "|Total Pay:" + totalRaffleAmount + "|";
			} else */
			if (mainWinningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
				if ("FRAUD".equalsIgnoreCase(mainWinningBean.getStatus())) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Can Not Verify, Invalid PWT.");
					return;
				} else if ("TICKET_EXPIRED".equalsIgnoreCase(mainWinningBean.getStatus())) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Expired or Invalid Ticket.");
					return;
				} else if (!mainWinningBean.isValid()) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Invalid Ticket. Device Would be Inactivated.");
					return;
				} else if (mainWinningBean.getStatus() != null && mainWinningBean.getStatus().equalsIgnoreCase("OUT_VERIFY_LIMIT")) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Can Not Verify, High Prize.");
					return;
				} else if ("UN_AUTH".equalsIgnoreCase(mainWinningBean.getStatus())) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Can Not Verify, Invalid Sub-Agent.");
					return;
				}

				double totalRaffleAmount = 0.0;
				List<PWTDrawBean> pwtWinBeanlist = mainWinningBean.getWinningBeanList();
				/*
				for (int i = 0; i < pwtWinBeanlist.size(); i++) {
					PWTDrawBean pwtwinBean = pwtWinBeanlist.get(i);
					if (pwtwinBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
						List<RaffleDrawIdBean> raffleDrawIdBeanList = pwtwinBean.getRaffleDrawIdBeanList();
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
					}
				}
				raffleData = raffleData + "#";
				*/

				for (int k = 0; k < pwtWinBeanlist.size(); k++) {
					PWTDrawBean pwtwinBean = pwtWinBeanlist.get(k);
					if (pwtwinBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
						for (int i = 0; i < pwtwinBean.getDrawWinList().size(); i++) {
							DrawIdBean drawBean = pwtwinBean.getDrawWinList().get(i);

							int nonWin = 0;
							int win = 0;
							int clm = 0;
							int register = 0;
							int claim = 0;
							int outVerify = 0;
							int pndPay = 0;

							String message = null;

							String drawStatusForTicket = drawBean.getStatus();
							boolean isResAwaited = false;
							boolean isExpired = false;
							boolean isVerPending = false;
							boolean isClmPending = false;
							double drawAmt = 0.0;
							List<PanelIdBean> panelWinList = drawBean.getPanelWinList();
							if (panelWinList != null && !drawStatusForTicket.equals("DRAW_EXPIRED") ) {
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
							if(isExpired){
								//stBuild.append("|DrawTime:" + drawTime + "");
								//stBuild.append(",No:0,Message:DRAW EXP");
								message = "DRAW EXP";
							}else if (isVerPending) {
								//stBuild.append("|DrawTime:" + drawTime + "");
								//stBuild.append(",No:0,Message:VER PND");
								message = "VER PND";
							}else if (isResAwaited) {
								//stBuild.append("|DrawTime:" + drawTime + "");
								//stBuild.append(",No:0,Message:Awaited");
								message = "Awaited";
							} else {
								//stBuild.append("|DrawTime:" + drawTime + "");
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
									//stBuild.append(",No:" + clm + ",Message:CLM PND "+ Util.getBalInString(drawAmt) );
									message = "CLM PND "+ Util.getBalInString(drawAmt);
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
					}
				}

				totTktAmt = totTktAmt + totalRaffleAmount;

				if (totRegister != 0) {
					//stBuild.append("No:" + totRegister + ",Message:Reg. Req");
					drawDataBean = new JSONObject();
					drawDataBean.put("message", "Reg. Req");
					drawDataArray.add(drawDataBean);
				}
			}

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
			session.setAttribute("PWT_RES", mainWinningBean);
			currentUserSessionMap.put(userName, session);

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

				int gameNo = Util.getGamenoFromTktnumber(String.valueOf(ticketNumber));
				String gameName = Util.getGameName(Util.getGameIdFromGameNumber(gameNo));

				responseObject.put("gameName", gameName);
				responseObject.put("totalPay", totTktAmt);
				responseObject.put("totalClaimPending", totalClmPndAmt);
				responseObject.put("balance", balance2);
				responseObject.put("drawDataList", drawDataArray);
			}
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