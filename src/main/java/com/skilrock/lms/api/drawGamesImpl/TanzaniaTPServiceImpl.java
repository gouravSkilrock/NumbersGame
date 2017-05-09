package com.skilrock.lms.api.drawGamesImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.codehaus.xfire.transport.http.XFireServletController;

import com.skilrock.lms.api.beans.CancelBean;
import com.skilrock.lms.api.beans.DrawDetailsBean;
import com.skilrock.lms.api.beans.GameDrawInfoBean;
import com.skilrock.lms.api.beans.GameInfoBean;
import com.skilrock.lms.api.beans.PWTApiBean;
import com.skilrock.lms.api.beans.PanelBean;
import com.skilrock.lms.api.beans.RaffleBean;
import com.skilrock.lms.api.beans.ReprintBean;
import com.skilrock.lms.api.beans.TPGameDetailsBean;
import com.skilrock.lms.api.beans.TPKenoPurchaseBean;
import com.skilrock.lms.api.beans.TPLottoPurchaseBean;
import com.skilrock.lms.api.beans.TPUserBalaanceBean;
import com.skilrock.lms.api.beans.TicketHeaderInfoBean;
import com.skilrock.lms.api.drawGames.ITanzaniaTPService;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;

public class TanzaniaTPServiceImpl implements ITanzaniaTPService {
	static Log logger = LogFactory.getLog(TanzaniaTPServiceImpl.class);

	public TPLottoPurchaseBean bonusBallLottoSale(
			TPLottoPurchaseBean bonusBallLottoBean) {
		String userName = bonusBallLottoBean.getUserName();
		String password = bonusBallLottoBean.getPassword();
		String ip = XFireServletController.getRequest().getRemoteHost();
		String usrReqStr = "TP REQUEST bonusBallLottoSale=userName:" + userName
				+ "|pasword:" + password + "|ip:" + ip + "|";
		String resStr = "TP RESPONSE bonusBallLottoSale=|userName:" + userName;
		StringBuilder raffleData = new StringBuilder("#Raffle Data:");
		StringBuilder panelArr = new StringBuilder("#Panel Data:");
		String dataStr = "|gameCode:" + bonusBallLottoBean.getGameCode()
				+ "|Balance:" + bonusBallLottoBean.getBalance() + "|ErrorCode:"
				+ bonusBallLottoBean.getErrorCode() + "|AdvancedPlay:"
				+ bonusBallLottoBean.getIsAdvancedPlay() + "|LmsTranxId:"
				+ bonusBallLottoBean.getLmsTranxId() + "|MobileNumber:"
				+ bonusBallLottoBean.getMobileNumber() + "|NoOfDraws:"
				+ bonusBallLottoBean.getNoOfDraws() + "|NoPicked:"
				+ bonusBallLottoBean.getNoPicked() + "|PurchaseTime:"
				+ bonusBallLottoBean.getPurchaseTime() + "|RefTransId:"
				+ bonusBallLottoBean.getRefTransId() + "|TicketCost:"
				+ bonusBallLottoBean.getTicketCost() + "|TicketNumber:"
				+ bonusBallLottoBean.getTicketNumber() + "|TotalPurchaseAmt:"
				+ bonusBallLottoBean.getTotalPurchaseAmt();
		for (int i = 0; i < bonusBallLottoBean.getPickedNumbers().length; i++) {
			panelArr.append("|Pick Numbers:"
					+ bonusBallLottoBean.getPickedNumbers()[i]);
		}
		if (bonusBallLottoBean.isRaffle()) {
			raffleData.append("|isRaffle:" + bonusBallLottoBean.isRaffle()
					+ "|RaffleTicketNumber:"
					+ bonusBallLottoBean.getRaffleData().getTicketNumber());
		}
		logger.debug(usrReqStr + dataStr + panelArr + raffleData);
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				HttpServletRequest request = XFireServletController
						.getRequest();
				request.setAttribute("code", "DG");
				request.setAttribute("interfaceType", "TERMINAL");
				ServletActionContext.setRequest(request);

				try {
					BonusBallLottoAction.purchaseTicketProcess(
							bonusBallLottoBean, userBean);
					if ("100".equalsIgnoreCase(bonusBallLottoBean
							.getErrorCode())) {
						StringBuilder raffleData1 = new StringBuilder(
								"#Raffle Data:");
						StringBuilder panelArr1 = new StringBuilder(
								"#Panel Data:");
						String finalResp = "|gameCode:"
								+ bonusBallLottoBean.getGameCode()
								+ "|Balance:" + bonusBallLottoBean.getBalance()
								+ "|ErrorCode:"
								+ bonusBallLottoBean.getErrorCode()
								+ "|AdvancedPlay:"
								+ bonusBallLottoBean.getIsAdvancedPlay()
								+ "|LmsTranxId:"
								+ bonusBallLottoBean.getLmsTranxId()
								+ "|MobileNumber:"
								+ bonusBallLottoBean.getMobileNumber()
								+ "|NoOfDraws:"
								+ bonusBallLottoBean.getNoOfDraws()
								+ "|DrawDateTime:"
								+ bonusBallLottoBean.getDrawDateTime()
								+ "|NoPicked:"
								+ bonusBallLottoBean.getNoPicked()
								+ "|PurchaseTime:"
								+ bonusBallLottoBean.getPurchaseTime()
								+ "|RefTransId:"
								+ bonusBallLottoBean.getRefTransId()
								+ "|TicketCost:"
								+ bonusBallLottoBean.getTicketCost()
								+ "|TicketNumber:"
								+ bonusBallLottoBean.getTicketNumber()
								+ "|TotalPurchaseAmt:"
								+ bonusBallLottoBean.getTotalPurchaseAmt()
								+ "|TopAdMessageList:"
								+ bonusBallLottoBean.getTopAdMessageList()
								+ "|BottomAdMessageList:"
								+ bonusBallLottoBean.getBottomAdMessageList();
						for (int i = 0; i < bonusBallLottoBean
								.getPickedNumbers().length; i++) {
							panelArr1.append("|Pick Numbers:"
									+ bonusBallLottoBean.getPickedNumbers()[i]
									+ "|QpStatus:"
									+ bonusBallLottoBean.getQpStatus()[i]);
						}
						if (bonusBallLottoBean.isRaffle()) {
							raffleData1.append("|isRaffle:"
									+ bonusBallLottoBean.isRaffle()
									+ "|RaffleTicketNumber:"
									+ bonusBallLottoBean.getRaffleData()
											.getTicketNumber());
						}
						logger.debug(resStr + finalResp + panelArr1
								+ raffleData1);
					} else {
						logger.debug(resStr + "|ErrorCode:"
								+ bonusBallLottoBean.getErrorCode());
					}
				} catch (Exception e) {
					e.printStackTrace();
					bonusBallLottoBean.setSuccess(false);
					bonusBallLottoBean.setErrorCode("500");// internal server
					// error
					logger.debug(resStr + "|ErrorCode:"
							+ bonusBallLottoBean.getErrorCode());
					return bonusBallLottoBean;
				}

			} else {
				bonusBallLottoBean.setSuccess(false);
				bonusBallLottoBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|ErrorCode:"
						+ bonusBallLottoBean.getErrorCode());
			}

		} else {
			bonusBallLottoBean.setSuccess(false);
			bonusBallLottoBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:"
					+ bonusBallLottoBean.getErrorCode());
			return bonusBallLottoBean;
		}
		return bonusBallLottoBean;
	}

	public CancelBean cancelTicket(String userName, String password,
			String lmsTranxIdToRefund, String refTransId, String mobileNumber) {
		CancelBean cancelBean = new CancelBean();
		String ip = XFireServletController.getRequest().getRemoteHost();
		logger.debug("TP REQUEST cancelTicket=userName:" + userName
				+ "|pasword:" + password + "|ip:" + ip + "|"
				+ "|lmsTranxIdToRefund:" + lmsTranxIdToRefund + "|refTransId:"
				+ refTransId + "|mobileNumber:" + mobileNumber);
		String resStr = "TP RESPONSE cancelTicket=|userName:" + userName;
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				HttpServletRequest request = XFireServletController
						.getRequest();
				request.setAttribute("code", "DG");
				request.setAttribute("interfaceType", "TERMINAL");
				ServletActionContext.setRequest(request);
				try {
					cancelBean = DrawGameAPIHelper.cancelTicket(userBean,
							lmsTranxIdToRefund, refTransId, mobileNumber);
					if ("100".equalsIgnoreCase(cancelBean.getErrorCode())) {
						String finalResp = "|ErrorCode:"
								+ cancelBean.getErrorCode() + "|RefundAmount:"
								+ cancelBean.getRefundAmount()
								+ "|TicketNumber:"
								+ cancelBean.getTicketNumber() + "|Balance:"
								+ cancelBean.getBalance() + "|LmsTransId:"
								+ cancelBean.getLmsTransId()
								+ "|LmsTranxIdToRefund:"
								+ cancelBean.getLmsTranxIdToRefund()
								+ "|refTransId:" + refTransId;
						logger.debug(resStr + finalResp);
					} else {
						logger.debug(resStr + "|ErrorCode:"
								+ cancelBean.getErrorCode());
					}
				} catch (Exception e) {
					e.printStackTrace();
					cancelBean.setErrorCode("500");
					cancelBean.setSuccess(false);
					logger.debug(resStr + "|ErrorCode:"
							+ cancelBean.getErrorCode());
					return cancelBean;
				}

			} else {
				cancelBean.setSuccess(false);
				cancelBean.setErrorCode("102");// Invalid User
				logger
						.debug(resStr + "|ErrorCode:"
								+ cancelBean.getErrorCode());
			}
		} else {
			cancelBean.setSuccess(false);
			cancelBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:" + cancelBean.getErrorCode());
		}

		return cancelBean;
	}

	public List<GameDrawInfoBean> drawResult(String userName, String password,
			String gameCode, String date) {
		String ip = XFireServletController.getRequest().getRemoteHost();
		logger.debug("TP REQUEST drawResult=userName:" + userName + "|pasword:"
				+ password + "|ip:" + ip + "|" + "|gameCode:" + gameCode
				+ "|date:" + date);
		String resStr = "TP RESPONSE drawResult=|userName:" + userName;
		List<GameDrawInfoBean> returnList = null;
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				returnList = DrawGameAPIHelper.drawResult(gameCode, date);
				if (returnList != null && returnList.size() > 0) {
					StringBuilder finalStr = new StringBuilder(
							"#Draw Result Data:");
					GameDrawInfoBean bean = null;
					for (int i = 0; i < returnList.size(); i++) {
						bean = returnList.get(i);
						finalStr.append("|GameCode:" + bean.getGameCode());
						if (bean.getDrawResultList() != null
								&& bean.getDrawResultList().size() > 0) {
							DrawDetailsBean drawBean = null;
							for (int j = 0; j < bean.getDrawResultList().size(); j++) {
								drawBean = bean.getDrawResultList().get(j);
								finalStr
										.append("|DrawId:"
												+ drawBean.getDrawId()
												+ "|DrawDateTime:"
												+ drawBean.getDrawDateTime()
												+ "|Symbols:"
												+ drawBean.getSymbols()
												+ "|DrawName:"
												+ drawBean.getDrawName());
							}
						}
					}
					logger.debug(resStr + finalStr);
				} else {
					logger.debug(resStr + "result list is null");
				}
				return returnList;
			}
		}

		return returnList;

	}

	@SuppressWarnings("unchecked")
	public TPGameDetailsBean gameInfo(String userName, String password) {
		String ip = XFireServletController.getRequest().getRemoteHost();
		logger.debug("TP REQUEST gameInfo=userName:" + userName + "|password:"
				+ password + "|ip:" + ip + "|");
		String resStr = "TP RESPONSE gameInfo=|userName:" + userName;
		TPGameDetailsBean tpGameReturnBean = new TPGameDetailsBean();
		if (APIUtility.isValidUser(userName, password, ip)) {
			ServletContext sc = LMSUtility.sc;
			System.out.println("Valid user with ip:" + ip);
			Map<String, UserInfoBean> currentUsersMap = (Map<String, UserInfoBean>) sc
					.getAttribute("API_LOGGED_IN_USERS_MAP");
			if (currentUsersMap == null) {
				currentUsersMap = new HashMap<String, UserInfoBean>();
			}

			UserInfoBean userBean = currentUsersMap.get(userName);
			if (userBean == null) {
				userBean = DrawGameAPIHelper.createUserData(userName, password);
				if (userBean == null) {
					tpGameReturnBean.setSuccess(false);
					tpGameReturnBean.setErrorCode("102");// Invalid User
					logger.debug(resStr + "|ErrorCode:"
							+ tpGameReturnBean.getErrorCode());
					return tpGameReturnBean;
				} else {
					currentUsersMap.put(userName, userBean);
					sc.setAttribute("API_LOGGED_IN_USERS_MAP", currentUsersMap);
				}
			}

			try {
				if (userBean != null) {
					tpGameReturnBean = DrawGameAPIHelper.apiDgData(userBean);
					if ("100".equalsIgnoreCase(tpGameReturnBean.getErrorCode())) {
						StringBuilder gameData = new StringBuilder(
								"#Game Data:");
						String finlResp = "|NextTimeTogGetInfo:"
								+ tpGameReturnBean.getNextTimeTogGetInfo()
								+ "|ErrorCode:"
								+ tpGameReturnBean.getErrorCode();
						if (tpGameReturnBean.getGameBeanList() != null
								&& tpGameReturnBean.getGameBeanList().size() > 0) {
							GameInfoBean gameBean = null;
							for (int i = 0; i < tpGameReturnBean
									.getGameBeanList().size(); i++) {
								StringBuilder drawData = new StringBuilder(
										"#Draw Data:");
								gameBean = tpGameReturnBean.getGameBeanList()
										.get(i);
								gameData.append("|GameCode:"
										+ gameBean.getGameCode()
										+ "|GameDisplayName:"
										+ gameBean.getGameDisplayName()
										+ "|GameNo:" + gameBean.getGameNo()
										+ "|JackpotAmount:"
										+ gameBean.getJackpotAmount()
										+ "|NoOfDraws:"
										+ gameBean.getNoOfDraws()
										+ "|Ticket_validity:"
										+ gameBean.getTicket_validity()
										+ "|Online_FTG:"
										+ gameBean.getOnline_FTG() + "|Type:"
										+ gameBean.getType()
										+ "|TypeSpecificInfo:"
										+ gameBean.getTypeSpecificInfo()
										+ "|Unit_price:"
										+ gameBean.getUnit_price());
								if (gameBean.getDrawBeanList() != null
										&& gameBean.getDrawBeanList().size() > 0) {
									for (int j = 0; j < gameBean
											.getDrawBeanList().size(); j++) {
										drawData.append("|DrawDateTime:"
												+ gameBean.getDrawBeanList()
														.get(j)
														.getDrawDateTime()
												+ "|DrawId:"
												+ gameBean.getDrawBeanList()
														.get(j).getDrawId()
												+ "|DrawName:"
												+ gameBean.getDrawBeanList()
														.get(j).getDrawName());

									}
								}
								gameData.append(drawData);

							}
						}
						logger.debug(resStr + finlResp + gameData);
					} else {
						logger.debug(resStr + "|ErrorCode:"
								+ tpGameReturnBean.getErrorCode());
					}
				}
			} catch (LMSException e) {
				e.printStackTrace();
				tpGameReturnBean.setSuccess(false);
				tpGameReturnBean.setErrorCode("500");// Internal server error
				logger.debug(resStr + "|ErrorCode:"
						+ tpGameReturnBean.getErrorCode());
				return tpGameReturnBean;
			}
		} else {
			tpGameReturnBean.setSuccess(false);
			tpGameReturnBean.setErrorCode("102");// Invalid User
			logger.debug(resStr + "|ErrorCode:"
					+ tpGameReturnBean.getErrorCode());
			return tpGameReturnBean;
		}
		tpGameReturnBean.setSuccess(true);
		tpGameReturnBean.setErrorCode("100");// success
		return tpGameReturnBean;
	}

	public TPUserBalaanceBean getBalanceInfo(String userName, String password) {
		String ip = XFireServletController.getRequest().getRemoteHost();
		logger.debug("TP REQUEST getBalanceInfo=userName:" + userName
				+ "|password:" + password + "|ip:" + ip + "|");
		String resStr = "TP RESPONSE getBalanceInfo=|userName:" + userName;
		TPUserBalaanceBean returnBalBean = new TPUserBalaanceBean();
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				logger.debug("valid user ... getting balance info...");
				returnBalBean = DrawGameAPIHelper.getUserBal(userBean);
				logger.debug(resStr + "|Balance:" + returnBalBean.getBalance()
						+ "|ErrorCode:" + returnBalBean.getErrorCode());
			} else {
				returnBalBean.setSuccess(false);
				returnBalBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|Balance:" + returnBalBean.getBalance()
						+ "|ErrorCode:" + returnBalBean.getErrorCode());
			}

		} else {
			System.out.println("user bean is null...");
			// System.out.println("Invalid user");
			returnBalBean.setSuccess(false);
			returnBalBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|Balance:" + returnBalBean.getBalance()
					+ "|ErrorCode:" + returnBalBean.getErrorCode());
		}

		return returnBalBean;
	}

	public TPKenoPurchaseBean kenoSale(TPKenoPurchaseBean kenoPurchaseBean) {
		String userName = kenoPurchaseBean.getUserName();
		String password = kenoPurchaseBean.getPassword();
		String ip = XFireServletController.getRequest().getRemoteHost();
		String usrReqStr = "TP REQUEST kenoSale=userName:" + userName
				+ "|pasword:" + password + "|ip:" + ip + "|";
		String resStr = "TP RESPONSE kenoSale=|userName:" + userName;
		StringBuilder panelStr = new StringBuilder("#Panel Data");
		String dataStr = "|gameCode:" + kenoPurchaseBean.getGameCode()
				+ "|Balance:" + kenoPurchaseBean.getBalance() + "|ErrorCode:"
				+ kenoPurchaseBean.getErrorCode() + "|LmsTranxId:"
				+ kenoPurchaseBean.getLmsTranxId() + "|MobileNumber:"
				+ "|IsAdvancePlay:" + kenoPurchaseBean.getIsAdvancePlay()
				+ kenoPurchaseBean.getMobileNumber() + "|NoOfDraws:"
				+ kenoPurchaseBean.getNoOfDraws() + "|DrawDateTimeList:"
				+ kenoPurchaseBean.getDrawDateTimeList() + "|PurchaseTime:"
				+ kenoPurchaseBean.getPurchaseTime() + "|RefTransId:"
				+ kenoPurchaseBean.getRefTransId() + "|TicketCost:"
				+ kenoPurchaseBean.getTicketCost() + "|TicketNumber:"
				+ kenoPurchaseBean.getTicketNumber() + "|TotalPurchaseAmt:"
				+ kenoPurchaseBean.getTotalPurchaseAmt() + "|TopAdMessageList:"
				+ kenoPurchaseBean.getTopAdMessageList()
				+ "|BottomAdMessageList:"
				+ kenoPurchaseBean.getBottomAdMessageList();
		if (kenoPurchaseBean.getPanelList() != null
				&& kenoPurchaseBean.getPanelList().size() > 0) {
			for (PanelBean panelBean : kenoPurchaseBean.getPanelList()) {
				panelStr.append("|PickedNumbers:"
						+ panelBean.getPickedNumbers() + "|NoPicked:"
						+ panelBean.getNoPicked() + "|PlayType:"
						+ panelBean.getPlayType() + "|BetAmountMultiple:"
						+ panelBean.getBetAmountMultiple() + "|IsQp:"
						+ panelBean.getIsQp());
			}
		}
		logger.debug(usrReqStr + panelStr + dataStr);
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				HttpServletRequest request = XFireServletController
						.getRequest();
				request.setAttribute("code", "DG");
				request.setAttribute("interfaceType", "TERMINAL");
				ServletActionContext.setRequest(request);
				KenoAction.purchaseTicketProcess(kenoPurchaseBean, userBean);
				if ("100".equalsIgnoreCase(kenoPurchaseBean.getErrorCode())) {
					StringBuilder panelStr1 = new StringBuilder("#Panel Data");
					StringBuilder raffleData = new StringBuilder(
							"#Raffle Data:");
					String finalResp = "|gameCode:"
							+ kenoPurchaseBean.getGameCode() + "|Balance:"
							+ kenoPurchaseBean.getBalance() + "|ErrorCode:"
							+ kenoPurchaseBean.getErrorCode() + "|LmsTranxId:"
							+ kenoPurchaseBean.getLmsTranxId()
							+ "|MobileNumber:"
							+ kenoPurchaseBean.getMobileNumber()
							+ "|NoOfDraws:" + kenoPurchaseBean.getNoOfDraws()
							+ "|IsAdvancePlay:"
							+ kenoPurchaseBean.getIsAdvancePlay()
							+ "|DrawDateTimeList:"
							+ kenoPurchaseBean.getDrawDateTimeList()
							+ "|PurchaseTime:"
							+ kenoPurchaseBean.getPurchaseTime()
							+ "|RefTransId:" + kenoPurchaseBean.getRefTransId()
							+ "|TicketCost:" + kenoPurchaseBean.getTicketCost()
							+ "|TicketNumber:"
							+ kenoPurchaseBean.getTicketNumber()
							+ "|TotalPurchaseAmt:"
							+ kenoPurchaseBean.getTotalPurchaseAmt()
							+ "|TopAdMessageList:"
							+ kenoPurchaseBean.getTopAdMessageList()
							+ "|BottomAdMessageList:"
							+ kenoPurchaseBean.getBottomAdMessageList();
					if (kenoPurchaseBean.getPanelList() != null
							&& kenoPurchaseBean.getPanelList().size() > 0) {
						for (PanelBean panelBean : kenoPurchaseBean
								.getPanelList()) {
							panelStr1.append("|PickedNumbers:"
									+ panelBean.getPickedNumbers()
									+ "|NoPicked:" + panelBean.getNoPicked()
									+ "|PlayType:" + panelBean.getPlayType()
									+ "|BetAmountMultiple:"
									+ panelBean.getBetAmountMultiple()
									+ "|IsQp:" + panelBean.getIsQp());
						}
					}
					if (kenoPurchaseBean.isRaffle()) {
						raffleData.append("|isRaffle:"
								+ kenoPurchaseBean.isRaffle()
								+ "|RaffleTicketNumber:"
								+ kenoPurchaseBean.getRaffleData()
										.getTicketNumber());
					}
					logger.debug(resStr + panelStr1 + finalResp + raffleData);
				} else {
					logger.debug(resStr + "|ErrorCode:"
							+ kenoPurchaseBean.getErrorCode());
				}

			} else {
				kenoPurchaseBean.setSuccess(false);
				kenoPurchaseBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|ErrorCode:"
						+ kenoPurchaseBean.getErrorCode());
			}
		} else {
			kenoPurchaseBean.setSuccess(false);
			kenoPurchaseBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:"
					+ kenoPurchaseBean.getErrorCode());
			return kenoPurchaseBean;
		}

		return kenoPurchaseBean;
	}

	public PWTApiBean pwtPayment(String userName, String password,
			String ticketNo, String refTransId, String mobileNumber) {
		PWTApiBean pwtBean = new PWTApiBean();
		String ip = XFireServletController.getRequest().getRemoteHost();
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		logger.debug("TP REQUEST pwtPayment=userName:" + userName + "|pasword:"
				+ password + "|ip:" + ip + "|refTransId:" + refTransId
				+ "|mobileNumber:" + mobileNumber + "|ticketNo:" + ticketNo);
		String resStr = "TP RESPONSE pwtPayment=|userName:" + userName;
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				HttpServletRequest request = XFireServletController
						.getRequest();
				request.setAttribute("code", "DG");
				request.setAttribute("interfaceType", "TERMINAL");
				ServletActionContext.setRequest(request);
				pwtBean.setTicketNo(ticketNo);
				pwtBean = DrawGameAPIHelper.pwtPayment(userBean, ticketNo,
						refTransId, mobileNumber);
				if ("100".equalsIgnoreCase(pwtBean.getErrorCode())) {
					StringBuilder panelStr = new StringBuilder("#Panel Data");
					StringBuilder drawStr = new StringBuilder("#Draw Str :");
					StringBuilder raffleStr = new StringBuilder("#Raffle Data :");
					String finalResp = "|gameCode:" + pwtBean.getGameCode()
							+ "|Balance:" + pwtBean.getBalance()
							+ "|ErrorCode:" + pwtBean.getErrorCode()
							+ "|RefTransId:" + pwtBean.getRefTransId()
							+ "|ReprintTicketGameCode:"
							+ pwtBean.getReprintTicketGameCode() + "|TicketNo:"
							+ pwtBean.getTicketNo() + "|TotalWinning:"
							+ pwtBean.getTotalWinning() + "|LmsTranxIdList:"
							+ pwtBean.getLmsTranxIdList() + "|ReprintBean:"
							+ pwtBean.getReprintBean();
					if (pwtBean.getDrawBeanList() != null
							&& pwtBean.getDrawBeanList().size() > 0) {
						DrawDetailsBean drawBean = null;
						for (int i = 0; i < pwtBean.getDrawBeanList().size(); i++) {
							drawBean = pwtBean.getDrawBeanList().get(i);
							drawStr.append("|DrawDateTime:"
									+ drawBean.getDrawDateTime());
							if (drawBean.getPanelBean() != null) {
								panelStr.append("|Message:"
										+ drawBean.getPanelBean().getMessage()
										+ "|NoOfPanels:"
										+ drawBean.getPanelBean()
												.getNoOfPanels());
							}
							drawStr.append(panelStr);
						}
					}
					 RaffleBean raffleApiBean=null;
                     if(pwtBean.getRaffleBean()!=null){
                    	 raffleApiBean=pwtBean.getRaffleBean();
	                 raffleStr.append("|DrawDateTime:" + raffleApiBean.getDrawDateTime()
	                		 + "|Message:"+raffleApiBean.getMessage()
	                		 +"|Raffle Ticket Number:"+raffleApiBean.getTicketNumber());
                             }
					logger.debug(resStr + finalResp + drawStr+raffleStr);
				} else {
					logger.debug(resStr + "|ErrorCode:"
							+ pwtBean.getErrorCode());
				}
			} else {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|ErrorCode:" + pwtBean.getErrorCode());
			}
		} else {
			pwtBean.setSuccess(false);
			pwtBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:" + pwtBean.getErrorCode());
			return pwtBean;
		}

		return pwtBean;
	}

	public PWTApiBean pwtVerification(String userName, String password,
			String ticketNo) {
		PWTApiBean pwtBean = new PWTApiBean();
		String ip = XFireServletController.getRequest().getRemoteHost();
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		logger.debug("TP REQUEST pwtVerification=userName:" + userName
				+ "|pasword:" + password + "|ip:" + ip + "|ticketNo:"
				+ ticketNo);
		String resStr = "TP RESPONSE pwtVerification=|userName:" + userName;
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				System.out.println("Valid User verifying PWT for " + ticketNo);
				pwtBean.setTicketNo(ticketNo);
				pwtBean = DrawGameAPIHelper.pwtVerification(userBean, ticketNo);
				if ("100".equalsIgnoreCase(pwtBean.getErrorCode())) {
					StringBuilder panelStr = new StringBuilder("#Panel Data");
					StringBuilder drawStr = new StringBuilder("#Draw Str :");
					StringBuilder raffleStr = new StringBuilder("#Raffle Data :");
					String finalResp = "|gameCode:" + pwtBean.getGameCode()
							+ "|Balance:" + pwtBean.getBalance()
							+ "|ErrorCode:" + pwtBean.getErrorCode()
							+ "|RefTransactionId:" + pwtBean.getRefTransId()
							+ "|ReprintTicketGameCode:"
							+ pwtBean.getReprintTicketGameCode() + "|TicketNo:"
							+ pwtBean.getTicketNo() + "|TotalWinning:"
							+ pwtBean.getTotalWinning();
					if (pwtBean.getDrawBeanList() != null
							&& pwtBean.getDrawBeanList().size() > 0) {
						DrawDetailsBean drawBean = null;
						for (int i = 0; i < pwtBean.getDrawBeanList().size(); i++) {
							drawBean = pwtBean.getDrawBeanList().get(i);
							drawStr.append("|DrawDateTime:"
									+ drawBean.getDrawDateTime());
							if (drawBean.getPanelBean() != null) {
								panelStr.append("|Message:"
										+ drawBean.getPanelBean().getMessage()
										+ "|NoOfPanels:"
										+ drawBean.getPanelBean()
												.getNoOfPanels());
							}

						}
						drawStr.append(panelStr);
					}
					 RaffleBean raffleApiBean=null;
                     if(pwtBean.getRaffleBean()!=null){
                    	 raffleApiBean=pwtBean.getRaffleBean();
	                 raffleStr.append("|DrawDateTime:" + raffleApiBean.getDrawDateTime()
	                		 + "|Message:"+raffleApiBean.getMessage()
	                		 +"|Raffle Ticket Number:"+raffleApiBean.getTicketNumber());
                             }
					logger.debug(resStr + finalResp + drawStr+raffleStr);
				} else {
					logger.debug(resStr + "|ErrorCode:"
							+ pwtBean.getErrorCode());
				}
			} else {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|ErrorCode:" + pwtBean.getErrorCode());
			}
		} else {
			pwtBean.setSuccess(false);
			pwtBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:" + pwtBean.getErrorCode());
		}

		return pwtBean;
	}

	public ReprintBean rePrintTicket(String userName, String password,
			String lmsTranxIdToReprint) {
		ReprintBean reprintBean = new ReprintBean();
		String ip = XFireServletController.getRequest().getRemoteHost();
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		logger.debug("TP REQUEST rePrintTicket=userName:" + userName
				+ "|pasword:" + password + "|ip:" + ip
				+ "|lmsTranxIdToReprint:" + lmsTranxIdToReprint);
		String resStr = "TP RESPONSE rePrintTicket=|userName:" + userName;
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				reprintBean = DrawGameAPIHelper.reprintTicket(userBean,
						lmsTranxIdToReprint);
				if ("100".equalsIgnoreCase(reprintBean.getErrorCode())) {
					StringBuilder panelArr = new StringBuilder("#Panel Data=");
					String finalResp = "|ErrorCode:"
							+ reprintBean.getErrorCode();
					StringBuilder gameStr = new StringBuilder("#Game Data");
					if (reprintBean.getKenoBean() != null) {
						TPKenoPurchaseBean kenoBean = reprintBean.getKenoBean();
						gameStr.append("|gameCode:" + kenoBean.getGameCode()
								+ "|Balance:" + kenoBean.getBalance()
								+ "|ErrorCode:" + kenoBean.getErrorCode()
								+ "|LmsTranxId:" + kenoBean.getLmsTranxId()
								+ "|MobileNumber:" + kenoBean.getMobileNumber()
								+ "|NoOfDraws:" + kenoBean.getNoOfDraws()
								+ "|DrawDateTimeList:"
								+ kenoBean.getDrawDateTimeList()
								+ "|PurchaseTime:" + kenoBean.getPurchaseTime()
								+ "|RefTransId:" + kenoBean.getRefTransId()
								+ "|TicketCost:" + kenoBean.getTicketCost()
								+ "|TicketNumber:" + kenoBean.getTicketNumber()
								+ "|TotalPurchaseAmt:"
								+ kenoBean.getTotalPurchaseAmt());
						if (kenoBean.getPanelList() != null
								&& kenoBean.getPanelList().size() > 0) {
							for (PanelBean panelBean : kenoBean.getPanelList()) {
								panelArr.append("|PickedNumbers:"
										+ panelBean.getPickedNumbers()
										+ "|NoPicked:"
										+ panelBean.getNoPicked()
										+ "|PlayType:"
										+ panelBean.getPlayType()
										+ "|BetAmountMultiple:"
										+ panelBean.getBetAmountMultiple()
										+ "|IsQp:" + panelBean.getIsQp());
							}
						}

					} else if (reprintBean.getLottoBean() != null) {
						gameStr.append("|gameCode:"
								+ reprintBean.getLottoBean().getGameCode()
								+ "|Balance:"
								+ reprintBean.getLottoBean().getBalance()
								+ "|ErrorCode:"
								+ reprintBean.getLottoBean().getErrorCode()
								+ "|AdvancedPlay:"
								+ reprintBean.getLottoBean()
										.getIsAdvancedPlay()
								+ "|DrawDateTimeList:"
								+ reprintBean.getLottoBean().getDrawDateTime()
								+ "|LmsTranxId:"
								+ reprintBean.getLottoBean().getLmsTranxId()
								+ "|MobileNumber:"
								+ reprintBean.getLottoBean().getMobileNumber()
								+ "|NoOfDraws:"
								+ reprintBean.getLottoBean().getNoOfDraws()
								+ "|NoPicked:"
								+ reprintBean.getLottoBean().getNoPicked()
								+ "|PurchaseTime:"
								+ reprintBean.getLottoBean().getPurchaseTime()
								+ "|RefTransId:"
								+ reprintBean.getLottoBean().getRefTransId()
								+ "|TicketCost:"
								+ reprintBean.getLottoBean().getTicketCost()
								+ "|TicketNumber:"
								+ reprintBean.getLottoBean().getTicketNumber()
								+ "|TotalPurchaseAmt:"
								+ reprintBean.getLottoBean()
										.getTotalPurchaseAmt());
						for (int i = 0; i < reprintBean.getLottoBean()
								.getPickedNumbers().length; i++) {

							panelArr
									.append("|Pick Numbers:"
											+ reprintBean.getLottoBean()
													.getPickedNumbers()[i]
											+ "|QpStatus:"
											+ reprintBean.getLottoBean()
													.getQpStatus()[i]);
						}
					}
					logger.debug(resStr + finalResp + gameStr + panelArr);
				} else {
					logger.debug(resStr + "|ErrorCode:"
							+ reprintBean.getErrorCode());
				}
			} else {
				reprintBean.setSuccess(false);
				reprintBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|ErrorCode:"
						+ reprintBean.getErrorCode());
			}
		} else {
			reprintBean.setSuccess(false);
			reprintBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:" + reprintBean.getErrorCode());
		}

		return reprintBean;
	}
	
	public ReprintBean getTransactionStatusAndData(String userName, String password,
			String refTxnId) {
		ReprintBean reprintBean = new ReprintBean();
		String ip = XFireServletController.getRequest().getRemoteHost();
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		logger.debug("TP REQUEST getTransactionStatusAndData=userName:" + userName
				+ "|pasword:" + password + "|ip:" + ip
				+ "|refTxnId:" + refTxnId);
		String resStr = "TP RESPONSE getTransactionStatusAndData=|userName:" + userName;
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				List<Long> lmsTxnIdList = DrawGameAPIHelper.getLMSTxnIdList(userBean.getUserOrgId(), refTxnId);
				String mobileNo = DrawGameAPIHelper.getMobileNo(userBean.getUserOrgId(), refTxnId);
				
				List<String> lmsTranxIdTypeList = DrawGameAPIHelper.getTransactionType(lmsTxnIdList);
				reprintBean = DrawGameAPIHelper.getTransactionStatusAndData(userBean, refTxnId, lmsTxnIdList, lmsTranxIdTypeList, mobileNo);
				
				if ("100".equalsIgnoreCase(reprintBean.getErrorCode())) {
					StringBuilder panelArr = new StringBuilder("#Panel Data=");
					String finalResp = "|ErrorCode:"
							+ reprintBean.getErrorCode();
					StringBuilder gameStr = new StringBuilder("#Game Data");
					if (reprintBean.getKenoBean() != null) {
						TPKenoPurchaseBean kenoBean = reprintBean.getKenoBean();
						gameStr.append("|gameCode:" + kenoBean.getGameCode()
								+ "|Balance:" + kenoBean.getBalance()
								+ "|ErrorCode:" + kenoBean.getErrorCode()
								+ "|LmsTranxId:" + kenoBean.getLmsTranxId()
								+ "|MobileNumber:" + kenoBean.getMobileNumber()
								+ "|NoOfDraws:" + kenoBean.getNoOfDraws()
								+ "|DrawDateTimeList:"
								+ kenoBean.getDrawDateTimeList()
								+ "|PurchaseTime:" + kenoBean.getPurchaseTime()
								+ "|RefTransId:" + kenoBean.getRefTransId()
								+ "|TicketCost:" + kenoBean.getTicketCost()
								+ "|TicketNumber:" + kenoBean.getTicketNumber()
								+ "|TotalPurchaseAmt:"
								+ kenoBean.getTotalPurchaseAmt());
						if (kenoBean.getPanelList() != null
								&& kenoBean.getPanelList().size() > 0) {
							for (PanelBean panelBean : kenoBean.getPanelList()) {
								panelArr.append("|PickedNumbers:"
										+ panelBean.getPickedNumbers()
										+ "|NoPicked:"
										+ panelBean.getNoPicked()
										+ "|PlayType:"
										+ panelBean.getPlayType()
										+ "|BetAmountMultiple:"
										+ panelBean.getBetAmountMultiple()
										+ "|IsQp:" + panelBean.getIsQp());
							}
						}

					} else if (reprintBean.getLottoBean() != null) {
						gameStr.append("|gameCode:"
								+ reprintBean.getLottoBean().getGameCode()
								+ "|Balance:"
								+ reprintBean.getLottoBean().getBalance()
								+ "|ErrorCode:"
								+ reprintBean.getLottoBean().getErrorCode()
								+ "|AdvancedPlay:"
								+ reprintBean.getLottoBean()
										.getIsAdvancedPlay()
								+ "|DrawDateTimeList:"
								+ reprintBean.getLottoBean().getDrawDateTime()
								+ "|LmsTranxId:"
								+ reprintBean.getLottoBean().getLmsTranxId()
								+ "|MobileNumber:"
								+ reprintBean.getLottoBean().getMobileNumber()
								+ "|NoOfDraws:"
								+ reprintBean.getLottoBean().getNoOfDraws()
								+ "|NoPicked:"
								+ reprintBean.getLottoBean().getNoPicked()
								+ "|PurchaseTime:"
								+ reprintBean.getLottoBean().getPurchaseTime()
								+ "|RefTransId:"
								+ reprintBean.getLottoBean().getRefTransId()
								+ "|TicketCost:"
								+ reprintBean.getLottoBean().getTicketCost()
								+ "|TicketNumber:"
								+ reprintBean.getLottoBean().getTicketNumber()
								+ "|TotalPurchaseAmt:"
								+ reprintBean.getLottoBean()
										.getTotalPurchaseAmt());
						for (int i = 0; i < reprintBean.getLottoBean()
								.getPickedNumbers().length; i++) {

							panelArr
									.append("|Pick Numbers:"
											+ reprintBean.getLottoBean()
													.getPickedNumbers()[i]
											+ "|QpStatus:"
											+ reprintBean.getLottoBean()
													.getQpStatus()[i]);
						}
					}
					logger.debug(resStr + finalResp + gameStr + panelArr);
				} else {
					logger.debug(resStr + "|ErrorCode:"
							+ reprintBean.getErrorCode());
				}
			} else {
				reprintBean.setSuccess(false);
				reprintBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|ErrorCode:"
						+ reprintBean.getErrorCode());
			}
		} else {
			reprintBean.setSuccess(false);
			reprintBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:" + reprintBean.getErrorCode());
		}

		return reprintBean;
	}

	public TPLottoPurchaseBean tanzaniaLottoSale(
			TPLottoPurchaseBean tanzaniaLottoBean) {
		String userName = tanzaniaLottoBean.getUserName();
		String password = tanzaniaLottoBean.getPassword();
		String ip = XFireServletController.getRequest().getRemoteHost();
		String usrReqStr = "TP REQUEST tanzaniaLottoSale=userName:" + userName
				+ "|pasword:" + password + "|ip:" + ip + "|";
		String resStr = "TP RESPONSE tanzaniaLottoSale=|userName:" + userName;
		StringBuilder panelArr = new StringBuilder("#Panel Data:");
		String dataStr = "|gameCode:" + tanzaniaLottoBean.getGameCode()
				+ "|Balance:" + tanzaniaLottoBean.getBalance() + "|ErrorCode:"
				+ tanzaniaLottoBean.getErrorCode() + "|AdvancedPlay:"
				+ tanzaniaLottoBean.getIsAdvancedPlay() + "|LmsTranxId:"
				+ tanzaniaLottoBean.getLmsTranxId() + "|MobileNumber:"
				+ tanzaniaLottoBean.getMobileNumber() + "|NoOfDraws:"
				+ tanzaniaLottoBean.getNoOfDraws() + "|NoPicked:"
				+ tanzaniaLottoBean.getNoPicked() + "|PurchaseTime:"
				+ tanzaniaLottoBean.getPurchaseTime() + "|RefTransId:"
				+ tanzaniaLottoBean.getRefTransId() + "|TicketCost:"
				+ tanzaniaLottoBean.getTicketCost() + "|TicketNumber:"
				+ tanzaniaLottoBean.getTicketNumber() + "|TotalPurchaseAmt:"
				+ tanzaniaLottoBean.getTotalPurchaseAmt();
		for (int i = 0; i < tanzaniaLottoBean.getPickedNumbers().length; i++) {
			panelArr.append("|Pick Numbers:"
					+ tanzaniaLottoBean.getPickedNumbers()[i]);
		}
		logger.debug(usrReqStr + dataStr + panelArr);
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				HttpServletRequest request = XFireServletController
						.getRequest();
				request.setAttribute("code", "DG");
				request.setAttribute("interfaceType", "TERMINAL");
				ServletActionContext.setRequest(request);
				TanzaniaLottoAction.purchaseTicketProcess(tanzaniaLottoBean,
						userBean);
				if ("100".equalsIgnoreCase(tanzaniaLottoBean.getErrorCode())) {
					StringBuilder panelArr1 = new StringBuilder("#Panel Data:");
					StringBuilder raffleData = new StringBuilder(
							"#Raffle Data:");
					String finalResp = "|gameCode:"
							+ tanzaniaLottoBean.getGameCode() + "|Balance:"
							+ tanzaniaLottoBean.getBalance() + "|ErrorCode:"
							+ tanzaniaLottoBean.getErrorCode()
							+ "|AdvancedPlay:"
							+ tanzaniaLottoBean.getIsAdvancedPlay()
							+ "|LmsTranxId:"
							+ tanzaniaLottoBean.getLmsTranxId()
							+ "|MobileNumber:"
							+ tanzaniaLottoBean.getMobileNumber()
							+ "|NoOfDraws:" + tanzaniaLottoBean.getNoOfDraws()
							+ "|DrawDateTime:"
							+ tanzaniaLottoBean.getDrawDateTime()
							+ "|NoPicked:" + tanzaniaLottoBean.getNoPicked()
							+ "|PurchaseTime:"
							+ tanzaniaLottoBean.getPurchaseTime()
							+ "|RefTransId:"
							+ tanzaniaLottoBean.getRefTransId()
							+ "|TicketCost:"
							+ tanzaniaLottoBean.getTicketCost()
							+ "|TicketNumber:"
							+ tanzaniaLottoBean.getTicketNumber()
							+ "|TotalPurchaseAmt:"
							+ tanzaniaLottoBean.getTotalPurchaseAmt()
							+ "|TopAdMessageList:"
							+ tanzaniaLottoBean.getTopAdMessageList()
							+ "|BottomAdMessageList:"
							+ tanzaniaLottoBean.getBottomAdMessageList();
					for (int i = 0; i < tanzaniaLottoBean.getPickedNumbers().length; i++) {
						panelArr1.append("|Pick Numbers:"
								+ tanzaniaLottoBean.getPickedNumbers()[i]
								+ "|QpStatus:"
								+ tanzaniaLottoBean.getQpStatus()[i]);
					}
					if (tanzaniaLottoBean.isRaffle()) {
						raffleData.append("|isRaffle:"
								+ tanzaniaLottoBean.isRaffle()
								+ "|RaffleTicketNumber:"
								+ tanzaniaLottoBean.getRaffleData()
										.getTicketNumber());
					}
					logger.debug(resStr + finalResp + panelArr1 + raffleData);
				} else {
					logger.debug(resStr + "|ErrorCode:"
							+ tanzaniaLottoBean.getErrorCode());
				}
			} else {

				tanzaniaLottoBean.setSuccess(false);
				tanzaniaLottoBean.setErrorCode("102");// Invalid User
				logger.debug(resStr + "|ErrorCode:"
						+ tanzaniaLottoBean.getErrorCode());
			}

		} else {
			tanzaniaLottoBean.setSuccess(false);
			tanzaniaLottoBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:"
					+ tanzaniaLottoBean.getErrorCode());
			return tanzaniaLottoBean;
		}
		return tanzaniaLottoBean;
	}

	// this method need to be reviewed later ...
	public TicketHeaderInfoBean getTicketHeaderInfo(String userName,
			String password) {
		String ip = XFireServletController.getRequest().getRemoteHost();
		logger.debug("TP REQUEST getTicketHeaderInfo=userName:" + userName
				+ "|password:" + password + "|ip:" + ip + "|");
		String resStr = "TP RESPONSE getTicketHeaderInfo=|userName:" + userName;
		TicketHeaderInfoBean returnBean = new TicketHeaderInfoBean();
		UserInfoBean userBean = DrawGameAPIHelper.getUserData(userName);
		if (userBean != null) {
			if (APIUtility.validateUser(userBean, ip, password)) {
				logger.debug("valid user ... getTicketHeaderInfo...");
				returnBean = DrawGameAPIHelper.getHeaderInfo();
				logger.debug(resStr + "|CompanyName:"
						+ returnBean.getCompanyName() + "|IsLogoDisplayOnTkt:"
						+ returnBean.getIsLogoDisplayOnTkt() + "|TicketType:"
						+ returnBean.getTicketType() + "|ErrorCode:"
						+ returnBean.getErrorCode());
			} else {
				returnBean.setSuccess(false);
				returnBean.setErrorCode("102");// Invalid User
				logger
						.debug(resStr + "|ErrorCode:"
								+ returnBean.getErrorCode());
			}

		} else {
			System.out.println("user bean is null...");
			// System.out.println("Invalid user");
			returnBean.setSuccess(false);
			returnBean.setErrorCode("103");// Please call gameInfo first
			logger.debug(resStr + "|ErrorCode:" + returnBean.getErrorCode());
		}

		return returnBean;
	}

}
