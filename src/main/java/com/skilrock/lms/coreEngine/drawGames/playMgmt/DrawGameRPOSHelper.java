package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.itg.IDBarcode.IDBarcode;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.api.beans.PWTApiBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.controller.pwtMgmtDao.pwtMgmtDaoImpl;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.drawGames.common.DGPromoScheme;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.RetailerPwtProcessHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.BetDetailsBean;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.CommonPurchaseBean;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.DrawWinningReportBean;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneThreePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoRequestBean;
import com.skilrock.lms.dge.beans.KenoRequestBeanBuilder;
import com.skilrock.lms.dge.beans.KenoResponseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoRequestBean;
import com.skilrock.lms.dge.beans.LottoResponseBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.OfflineRetailerBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.dge.beans.ReprintBean;
import com.skilrock.lms.dge.beans.ValidateTicketBean;
import com.skilrock.lms.dge.gameconstants.KenoConstants;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketBean;

import net.sf.json.JSONObject;

public class DrawGameRPOSHelper {
	static Log logger=LogFactory.getLog(DrawGameRPOSHelper.class);
	private double totalPurchaseAmount;
	private double modifiedTotalPurchaseAmount = 0.0;
	private double oldTotalPurchaseAmount = 0.0;
	private String status = "";
	private ServiceResponse serviceResponse = new ServiceResponse();
	private ServiceRequest serviceRequest = new ServiceRequest();
	IServiceDelegate delegate = null;
	AjaxRequestHelper ajaxHelper;

	public DrawGameRPOSHelper() {
		delegate = ServiceDelegate.getInstance();
		this.ajaxHelper = new AjaxRequestHelper();
	}


	public DrawGameRPOSHelper(IServiceDelegate iServiceDelegate, AjaxRequestHelper ajaxHelper) {
		delegate = iServiceDelegate;
		this.ajaxHelper = ajaxHelper;
	}

	private long balanceDeduction = 0;

	// Freeze time is checked for the sale Game only , means sale of other game
	// is allowed
	public static boolean chkFreezeTimeSale(int gameNo) {
		if (!Util.onfreezeSale) {
			Long curDate = new Date().getTime();
			TreeMap<Integer, List<List>> gameData = Util.gameData;
			List<List> gameDataList = gameData.get(gameNo); // can be removed
			List<Map<String, String>> frzNdrawTimeList = gameDataList.get(3);
			Map<String, String> frzNdrawTime = frzNdrawTimeList.get(0);
			Iterator<Map.Entry<String, String>> iter = frzNdrawTime.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> pair = iter.next();
				if (Long.parseLong(pair.getKey()) < curDate && Long.parseLong(pair.getValue()) > curDate) {
					return true;
				}
			}
		}
		return false;

	}

	public boolean cancelRaffleTicket(CancelTicketBean cancelTicketBean, UserInfoBean userInfoBean,
			String cancellationCharges, Connection con) {
		int barCodeCount = -1;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		List<String> promoTicketList = cancelTicketBean.getPromoTicketList();
		if (cancelTicketBean.getTicketNo().length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount)
			barCodeCount = Integer.parseInt(Util.getBarCodeCountFromTicketNumber(cancelTicketBean.getTicketNo()));
		cancelTicketBean.setBarCodeCount(barCodeCount);
		int rafleGmeNo = getGamenoFromTktnumber(promoTicketList.get(0) + "0");
		// here cancel raffle ticket
		long refTransid = orgOnLineSaleCreditUpdation.drawRaffleTicketCancel(userInfoBean, con, cancellationCharges,
				promoTicketList.get(0), Util.getGameIdFromGameNumber(rafleGmeNo));
		cancelTicketBean.setRefTransId(refTransid + "");
		cancelTicketBean.setTicketNo(promoTicketList.get(0) + "0");
		cancelTicketBean.setGameId(rafleGmeNo);
		if (refTransid > 0) {
			sRes = new ServiceResponse();
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.RAFFLE);
			sReq.setServiceMethod(ServiceMethodName.RAFFLE_CANCEL_TICKET);
			sReq.setServiceData(cancelTicketBean);
			sRes = delegate.getResponse(sReq);
			if (sRes.getIsSuccess()) {
				return true;
			} else {
				cancelTicketBean.setValid(false);
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean cancelStdTkt(UserInfoBean userInfoBean, CancelTicketBean cancelTicketBean, Connection con,
			String cancellationCharges) {

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PLAYGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CANCEL_TICKET);
		sReq.setServiceData(cancelTicketBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		double tickRefundAmt = orgOnLineSaleCreditUpdation.drawTicketCancel(userInfoBean, cancelTicketBean, con,
				cancellationCharges);
		logger.debug(tickRefundAmt + "*tickRefundAmt");

		if (tickRefundAmt >= 0) {

			cancelTicketBean.setTicketNo(cancelTicketBean.getTicketNo() + cancelTicketBean.getReprintCount());
			sRes = delegate.getResponse(sReq);

			String responseString = sRes.getResponseData().toString();
			Type elementType = new TypeToken<CancelTicketBean>() {
			}.getType();

			if (sRes.getIsSuccess()) {
				logger.debug("*Inside Cancel Ticket Success");
				cancelTicketBean.setRefundAmount(tickRefundAmt);
				CancelTicketBean cancelTicketBean1 = new Gson().fromJson(responseString, elementType);
				cancelTicketBean.setCancelTime(cancelTicketBean1.getCancelTime());
				return true;
			} else {
				cancelTicketBean.setValid(false);
				return false;
			}

		} else {
			if (tickRefundAmt == -3)
				return true;
			cancelTicketBean.setValid(false);
			return false;
		}

	}

	public CancelTicketBean cancelTicket(CancelTicketBean cancelTicketBean, UserInfoBean userInfoBean,
			boolean autoCancel, String cancelType) {
		logger.debug("autoCancel and cancelTicketBean:" + autoCancel + " " + cancelTicketBean);
		Connection con = DBConnect.getConnection();

		String tNum = cancelTicketBean.getTicketNo();

		try {
			// we check user id from user info bean directly
			/*
			 * PreparedStatement ps = con
			 * .prepareStatement("select organization_id from st_lms_user_master where user_id="
			 * + Util.getUserIdFromTicket(tNum)); ResultSet rs =
			 * ps.executeQuery(); int orgId = 0; if (rs.next()) { orgId =
			 * rs.getInt(1); }
			 */
			if (userInfoBean.getUserId() != Util.getUserIdFromTicket(tNum)) {
				cancelTicketBean.setValid(false);
				cancelTicketBean.setError(true);
				cancelTicketBean.setErrMsg(EmbeddedErrors.UNAUTH_RET_FOR_THIS_TKT_ERROR_MSG);
				return cancelTicketBean;
			}
			con.setAutoCommit(false);
			ValidateTicketBean tktBean = Util.validateTkt(tNum);
			if (tktBean.isValid()) {
				int gameId = Util.getGameIdFromGameNumber(tktBean.getGameNo());

				// rg calling
				// autoCancel is used to stop calling RG when ticket is not
				// printed
				cancelTicketBean.setDayOfTicket(tktBean.getDayOfTicket());
				// no need of calling advertising message on auto cancel ticket
				if (!autoCancel) {
					cancelTicketBean.setAdvMsg(
							Util.getAdvMessage(userInfoBean.getUserOrgId(), gameId, "PLAYER", "CANCEL", "DG"));
				}
				// logger.info("**********autoCancel" + autoCancel);

				boolean isFraud = autoCancel ? false
						: ResponsibleGaming.respGaming(userInfoBean, "DG_CANCEL", "1", con);
				// need to call responsible gaming for decrease the total sale
				// amount from sale

				if (!isFraud) {
					// cancelTicketBean.setTicketNo(tktBean.getTicketNumInDB());
					// cancelTicketBean.setReprintCount(tktBean.getReprintCount());
					cancelTicketBean.setGameNo(tktBean.getGameNo());

					if ("manual".equals(cancelType)) {
						cancelTicketBean.setAutoCancel("CANCEL_MANUAL");
					} else if ("responseTimeOut".equals(cancelType)) {
						cancelTicketBean.setAutoCancel("CANCEL_RTO");
					} else if ("paperOut".equals(cancelType)) {
						cancelTicketBean.setAutoCancel("CANCEL_PRINTER");
					} else if ("dataError".equals(cancelType)) {
						cancelTicketBean.setAutoCancel("CANCEL_DATA_ERROR");
					} else if ("CANCEL_MISMATCH".equals(cancelType)) {
						cancelTicketBean.setAutoCancel("CANCEL_MISMATCH");
					} else if ("CANCEL_SERVER".equals(cancelType)) {
						cancelTicketBean.setAutoCancel("CANCEL_SERVER");
					} else {
						cancelTicketBean.setAutoCancel("CANCEL_PRINTER");
					}

					String cancellationCharges = (String) LMSUtility.sc.getAttribute("DRAW_GAME_CANCELLATION_CHARGES");
					boolean isPromoCancelled = true;
					// cancelTicketBean.setTicketNo(tNum);
					// cancelStdTkt(userInfoBean, cancelTicketBean, con,
					// cancellationCharges);
					// get the promo ticket from promo tble

					// if sql exception in this function then cancel continue or
					// not
					List<String> promoTicketList = orgOnLineSaleCreditUpdation
							.getAssociatedPromoTicket(tktBean.getTicketNumInDB(), con);
					if (promoTicketList != null && promoTicketList.size() > 0) {
						for (int i = 0; i < promoTicketList.size(); i++) {
							int gameNo = getGamenoFromTktnumber(promoTicketList.get(i)
									+ Util.getRpcAppenderForTickets(promoTicketList.get(i).length()));
							String gameType = Util.getGameType(Util.getGameIdFromGameNumber(gameNo));

							if ("RAFFLE".equalsIgnoreCase(gameType)) {
								cancelTicketBean.setPromoTicketList(promoTicketList);
								cancelTicketBean.setReprintCount(tktBean.getReprintCount());
								cancelTicketBean.setGameNo(gameNo);
								cancelTicketBean.setGameId(Util.getGameIdFromGameNumber(gameNo));

								isPromoCancelled = cancelRaffleTicket(cancelTicketBean, userInfoBean,
										cancellationCharges, con);
							} else {
								cancelTicketBean.setTicketNo(promoTicketList.get(i));
								cancelTicketBean.setReprintCount(tktBean.getReprintCount());
								cancelTicketBean.setBarCodeCount(-1);
								cancelTicketBean.setGameNo(gameNo);
								cancelTicketBean.setGameId(Util.getGameIdFromGameNumber(gameNo));
								isPromoCancelled = cancelStdTkt(userInfoBean, cancelTicketBean, con,
										cancellationCharges);
							}

						}
					}
					// if and only if all the child ticket has been cancelled
					// then only parent ticket would be cancelled
					// After cancelling all the promo tickets main ticket would
					// be cancelled
					if (isPromoCancelled) {
						cancelTicketBean.setTicketNo(tktBean.getTicketNumInDB());
						cancelTicketBean.setReprintCount(tktBean.getReprintCount());
						cancelTicketBean.setBarCodeCount(-1);
						cancelTicketBean.setGameNo(tktBean.getGameNo());
						cancelTicketBean.setGameId(gameId);
						boolean isCancelled = cancelStdTkt(userInfoBean, cancelTicketBean, con, cancellationCharges);
						if (isCancelled)
							con.commit();
					}

				} else {
					// rg fail
					cancelTicketBean.setValid(false);
					cancelTicketBean.setError(true);
					cancelTicketBean.setErrMsg(EmbeddedErrors.CANCEL_TKT_LIMIT_EXCEED_ERROR_MSG + "ErrorCode:"
							+ EmbeddedErrors.CANCEL_TKT_LIMIT_EXCEED_ERROR_CODE + "|");
				}
			} else {
				cancelTicketBean.setValid(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			cancelTicketBean.setValid(false);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("*Inside Cancel Ticket Before Return");
		return cancelTicketBean;
	}

	public void cancelTicket(String ticketNo, String cancelChannel, Map<Integer, Map<Integer, String>> drawIdTableMap,
			int gameNo, int partyId, String partyType, String refMerchantId, UserInfoBean userBean, String refTransId)
			throws LMSException {
		CancelTicketBean cancelTicketBean = new CancelTicketBean();
		logger.debug("*Inside Cancel Ticket Failed Transaction");

		ValidateTicketBean tktBean = Util.validateTkt(ticketNo);
		cancelTicketBean.setTicketNo(tktBean.getTicketNumInDB());
		cancelTicketBean.setReprintCount(tktBean.getReprintCount());
		cancelTicketBean.setGameNo(tktBean.getGameNo());
		cancelTicketBean.setGameId(Util.getGameIdFromGameNumber(tktBean.getGameNo()));

		Connection con = DBConnect.getConnection();
		cancelTicketBean.setDrawIdTableMap(drawIdTableMap);

		cancelTicketBean.setPartyId(partyId);
		cancelTicketBean.setPartyType(partyType);
		cancelTicketBean.setCancelChannel(cancelChannel);
		cancelTicketBean.setRefMerchantId(refMerchantId);
		cancelTicketBean.setRefTransId(refTransId);
		// added by amit not present earlier
		cancelTicketBean.setAutoCancel(true);
		cancelTicketBean.setAutoCancel("CANCEL_SERVER");
		logger.debug("*Inside Cancel Ticket");

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PLAYGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CANCEL_TICKET);

		IServiceDelegate delegate = ServiceDelegate.getInstance();

		String tNum = cancelTicketBean.getTicketNo();

		try {
			con.setAutoCommit(false);

			double tickRefundAmt = orgOnLineSaleCreditUpdation.drawTicketTxCancel(cancelTicketBean, userBean, con);
			cancelTicketBean.setRefundAmount(tickRefundAmt);
			con.commit();
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userBean, con);
			DBConnect.closeCon(con);
			cancelTicketBean.setUserId(userBean.getUserId());
			cancelTicketBean.setTicketNo(ticketNo);
			sReq.setServiceData(cancelTicketBean);
			sRes = delegate.getResponse(sReq);
			if (sRes.getIsSuccess()) {
				logger.debug("*Inside Cancel Ticket Success");

			} else {
				logger.debug("*Inside Cancel Ticket Error" + cancelTicketBean.getRefTransId());
				// Mail to be sent to BO
			}

		} catch (Exception e) {
			e.printStackTrace();
			cancelTicketBean.setValid(false);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("*Inside Cancel Ticket Failed Trx");
	}

	public FortunePurchaseBean card12PurchaseTicket(UserInfoBean userBean, FortunePurchaseBean card12PurchaseBean)
			throws LMSException {

		// int balDed = 0;
		long balDed = 0;
		// int tickUpd = 0;
		int tickUpd = 0;
		String status = "";
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.CARD12);
		sReq.setServiceMethod(ServiceMethodName.CARD12_PURCHASE_TICKET);
		sReq.setServiceData(card12PurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		Connection con = null;
		try {
			if (isDrawAvailable(card12PurchaseBean.getGame_no())
					|| chkFreezeTimeSale(card12PurchaseBean.getGame_no())) {
				card12PurchaseBean.setSaleStatus("NO_DRAWS");
				return card12PurchaseBean;
			}
			if (fortuneDataValidation(card12PurchaseBean)) {
				logger.debug("Data validation returns true");
				// card12PurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
				// card12PurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
				card12PurchaseBean
						.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), card12PurchaseBean.getGame_no()));

				card12PurchaseBean.setSaleStatus("FAILED");
				double totPurAmt = card12PurchaseBean.getTotalNoOfPanels()
						* Util.getUnitPrice(card12PurchaseBean.getGame_no(), null) * card12PurchaseBean.getNoOfDraws();

				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);
				card12PurchaseBean.setTotalPurchaseAmt(totPurAmt);
				if (!userBean.getUserType().equals("RETAILER")) {
					return card12PurchaseBean;
				}
				logger.debug("Inside Card12 FE1*******");
				con = DBConnect.getConnection();

				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "", con);
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							card12PurchaseBean.getGame_no(), card12PurchaseBean.getTotalPurchaseAmt(),
							card12PurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = card12PurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					card12PurchaseBean.setSaleStatus("FRAUD");
				}

				logger.debug("Inside Card12 FE*******" + sRes);
				return card12PurchaseBean;
			} else {
				logger.debug("Data validation returns false");
				card12PurchaseBean.setSaleStatus("FAILED");
				return card12PurchaseBean;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			if (balDed > 0) {
				card12PurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					card12PurchaseBean = (FortunePurchaseBean) sRes.getResponseData();

					modifiedTotalPurchaseAmt = card12PurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);

					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								card12PurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							card12PurchaseBean.getTicket_no(), card12PurchaseBean.getGame_no(), userBean,
							card12PurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						card12PurchaseBean.setSaleStatus(status);
						if (!card12PurchaseBean.getBarcodeType().equals("applet")) {
							new IDBarcode().getBarcode(
									card12PurchaseBean.getTicket_no() + card12PurchaseBean.getReprintCount());
						}
						return card12PurchaseBean;
					} else {
						status = "FAILED";
						card12PurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(card12PurchaseBean.getTicket_no() + card12PurchaseBean.getReprintCount(),
								card12PurchaseBean.getPurchaseChannel(), card12PurchaseBean.getDrawIdTableMap(),
								card12PurchaseBean.getGame_no(), card12PurchaseBean.getPartyId(),
								card12PurchaseBean.getPartyType(), card12PurchaseBean.getRefMerchantId(), userBean,
								card12PurchaseBean.getRefTransId());
						return card12PurchaseBean;
					}
				} else {
					card12PurchaseBean = (FortunePurchaseBean) sRes.getResponseData();
					if (card12PurchaseBean.getSaleStatus() == null) {
						card12PurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, card12PurchaseBean.getGame_no(),
								"FAILED", balDed);
						return card12PurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(card12PurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, card12PurchaseBean.getGame_no(),
								"FAILED", balDed);
						return card12PurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, card12PurchaseBean.getGame_no(),
								"FAILED", balDed);
						return card12PurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				card12PurchaseBean.setSaleStatus(status);
				return card12PurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return card12PurchaseBean;
	}

	public FortunePurchaseBean card16PurchaseTicket(UserInfoBean userBean, FortunePurchaseBean card16PurchaseBean)
			throws LMSException {
		// int balDed = 0;
		long balDed = 0;
		int tickUpd = 0;
		String status = "";
		// int tickUpd = 0;
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.CARD16);
		sReq.setServiceMethod(ServiceMethodName.CARD16_PURCHASE_TICKET);
		sReq.setServiceData(card16PurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		Connection con = null;
		if (isDrawAvailable(card16PurchaseBean.getGame_no()) || chkFreezeTimeSale(card16PurchaseBean.getGame_no())) {
			card16PurchaseBean.setSaleStatus("NO_DRAWS");
			return card16PurchaseBean;
		}

		try {
			if (fortuneDataValidation(card16PurchaseBean)) {
				logger.debug("Data validation returns true");
				// card16PurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
				// card16PurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
				card16PurchaseBean
						.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), card16PurchaseBean.getGame_no()));

				card16PurchaseBean.setSaleStatus("FAILED");
				double totPurAmt = card16PurchaseBean.getTotalNoOfPanels()
						* Util.getUnitPrice(card16PurchaseBean.getGame_no(), null) * card16PurchaseBean.getNoOfDraws();

				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);
				card16PurchaseBean.setTotalPurchaseAmt(totPurAmt);
				if (!userBean.getUserType().equals("RETAILER")) {
					return card16PurchaseBean;
				}
				con = DBConnect.getConnection();
				logger.debug("Inside Card16 FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							card16PurchaseBean.getGame_no(), card16PurchaseBean.getTotalPurchaseAmt(),
							card16PurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = card16PurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);

				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					card16PurchaseBean.setSaleStatus("FRAUD");
				}

				logger.debug("Inside Card12 FE*******" + sRes);
				return card16PurchaseBean;
			} else {
				logger.debug("Data validation returns false");
				card16PurchaseBean.setSaleStatus("FAILED");
				return card16PurchaseBean;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {

			if (balDed > 0) {
				card16PurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					card16PurchaseBean = (FortunePurchaseBean) sRes.getResponseData();

					modifiedTotalPurchaseAmt = card16PurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								card16PurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							card16PurchaseBean.getTicket_no(), card16PurchaseBean.getGame_no(), userBean,
							card16PurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						card16PurchaseBean.setSaleStatus(status);
						if (!card16PurchaseBean.getBarcodeType().equals("applet")) {
							new IDBarcode().getBarcode(
									card16PurchaseBean.getTicket_no() + card16PurchaseBean.getReprintCount());
						}
						return card16PurchaseBean;
					} else {
						status = "FAILED";
						card16PurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(card16PurchaseBean.getTicket_no() + card16PurchaseBean.getReprintCount(),
								card16PurchaseBean.getPurchaseChannel(), card16PurchaseBean.getDrawIdTableMap(),
								card16PurchaseBean.getGame_no(), card16PurchaseBean.getPartyId(),
								card16PurchaseBean.getPartyType(), card16PurchaseBean.getRefMerchantId(), userBean,
								card16PurchaseBean.getRefTransId());
						return card16PurchaseBean;
					}
				} else {
					card16PurchaseBean = (FortunePurchaseBean) sRes.getResponseData();
					if (card16PurchaseBean.getSaleStatus() == null) {
						card16PurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, card16PurchaseBean.getGame_no(),
								"FAILED", balDed);
						return card16PurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(card16PurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, card16PurchaseBean.getGame_no(),
								"FAILED", balDed);
						return card16PurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, card16PurchaseBean.getGame_no(),
								"FAILED", balDed);
						return card16PurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				card16PurchaseBean.setSaleStatus(status);
				return card16PurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return card16PurchaseBean;
	}

	public LottoPurchaseBean commonPromoPurchaseProcess(LottoPurchaseBean lottoPurchaseBean, UserInfoBean userBean)
			throws LMSException, SQLException {

		List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
		CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();
		// String gameType = getGameType(kenoPurchaseBean.getGame_no());
		String gameName = Util.getGameName(lottoPurchaseBean.getGame_no());
		List<PromoGameBean> promoGameslist = DGPromoScheme.getAvailablePromoGamesNew(gameName,
				lottoPurchaseBean.getTotalPurchaseAmt(), null);

		for (int i = 0; i < promoGameslist.size(); i++) {
			PromoGameBean promobean = promoGameslist.get(i);
			if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
				lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
				RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(userBean,
						lottoPurchaseBean, true);
				rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
				if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
					lottoPurchaseBean.setPromoSaleStatus("FAILED");
					return lottoPurchaseBean;
				} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
					// here insert entry into the promo ticket mapping table
					int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
					orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, rafflePurchaseBean.getGame_no(),
							rafflePurchaseBean.getParentTktNo(),
							rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
					rafflePurchaseBeanList.add(rafflePurchaseBean);
				}
				lottoPurchaseBean.setPromoPurchaseBean(rafflePurchaseBeanList);
			} else if (promobean.getPromoGametype().equalsIgnoreCase("Zerotonine")) {
				// call winfast purchase process
				int gameId = Util.getGameId("Zerotonine");
				FortunePurchaseBean drawGamePurchaseBean = new FortunePurchaseBean();
				drawGamePurchaseBean.setIsQP(1);
				drawGamePurchaseBean.setTotalNoOfPanels(1);
				// drawGamePurchaseBean.setSymbols(symbols);
				drawGamePurchaseBean.setNoOfPanels("1");

				// drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
				drawGamePurchaseBean.setDrawIdTableMap(lottoPurchaseBean.getDrawIdTableMap());
				drawGamePurchaseBean.setGame_no(gameId);
				drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
				// drawGamePurchaseBean.setIsQuickPick(isQuickPickNew);
				drawGamePurchaseBean.setNoOfDraws(1);
				drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
				drawGamePurchaseBean.setPartyType(userBean.getUserType());
				drawGamePurchaseBean.setUserId(userBean.getUserId());
				drawGamePurchaseBean.setRefMerchantId(lottoPurchaseBean.getRefMerchantId());
				drawGamePurchaseBean.setPurchaseChannel(lottoPurchaseBean.getPurchaseChannel());
				drawGamePurchaseBean.setPromotkt(true);
				drawGamePurchaseBean.setPlrMobileNumber(lottoPurchaseBean.getPlrMobileNumber());
				// if (drawIdArr != null) {
				// drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
				// }
				drawGamePurchaseBean.setIsAdvancedPlay(0);
				FortunePurchaseBean fortuneBean = zeroToNinePurchaseTicket(userBean, drawGamePurchaseBean);
				if ("SUCCESS".equalsIgnoreCase(fortuneBean.getSaleStatus())) {
					orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, lottoPurchaseBean.getGame_no(),
							lottoPurchaseBean.getTicket_no(), fortuneBean.getTicket_no());
				} else {
					lottoPurchaseBean.setPromoSaleStatus("FAILED");
					return lottoPurchaseBean;
				}
				// kenoPurchaseBean.setFortunePurchaseBean(fortuneBean);
				lottoPurchaseBean.setPromoPurchaseBean(fortuneBean);
			}
			// kenoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
			// kenoPurchaseBean.setPromoPurchaseBean(rafflePurchaseBeanList);
		}

		return lottoPurchaseBean;
	}

	public KenoPurchaseBean commonPromoPurchaseProcess(KenoPurchaseBean kenoPurchaseBean, UserInfoBean userBean)
			throws LMSException, SQLException {

		List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();

		List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(kenoPurchaseBean.getGameId());
		java.util.Date date1 = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		Time currentTime = new Time(date1.getTime());
		if (promoGameslist != null) {
			for (int i = 0; i < promoGameslist.size(); i++) {
				PromoGameBean promobean = promoGameslist.get(i);
				if (promobean.getSaleStartTime() != null && promobean.getSaleStartTime().trim().length() > 1) {
					String timeArray[] = promobean.getSaleStartTime().split(":"); // HH:mm:ss
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0])); // Your
																					// hour
					cal.set(Calendar.MINUTE, Integer.parseInt(timeArray[1])); // Your
																				// Mintue
					cal.set(Calendar.SECOND, Integer.parseInt(timeArray[2])); // Your
																				// second
					Time saleStartTime = new Time(cal.getTime().getTime());
					timeArray = promobean.getSaleEndTime().split(":"); // HH:mm:ss
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0])); // Your
																					// hour
					cal.set(Calendar.MINUTE, Integer.parseInt(timeArray[1])); // Your
																				// Mintue
					cal.set(Calendar.SECOND, Integer.parseInt(timeArray[2])); // Your
																				// second
					Time saleEndTime = new Time(cal.getTime().getTime());
					logger.info("Promo Criteria currentTime" + currentTime + " saleStartTime " + saleStartTime
							+ " saleEndTime " + saleEndTime + " Amount " + promobean.getSaleTicketAmt());
					if (currentTime.before(saleStartTime) || currentTime.after(saleEndTime)
							|| kenoPurchaseBean.getTotalPurchaseAmt() < promobean.getSaleTicketAmt()) {
						logger.info("Promo Criteria does not match");
						kenoPurchaseBean.setPromoSaleStatus("SUCCESS");
						return kenoPurchaseBean;
					} else {
						logger.info("Promo Criteria match");
					}
				} else {
					logger.info("Promo Criteria Not Set");
				}

				if ("RAFFLE".equalsIgnoreCase(promobean.getPromoGametype())) {
					kenoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
					kenoPurchaseBean.setNoofDrawsForPromo(promobean.getNoOfDraws());
					RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(userBean,
							kenoPurchaseBean, true);
					rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
					if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
						kenoPurchaseBean.setPromoSaleStatus("FAILED");
						return kenoPurchaseBean;
					} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
						// here insert entry into the promo ticket mapping table
						int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
						orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, rafflePurchaseBean.getGame_no(),
								rafflePurchaseBean.getParentTktNo(), rafflePurchaseBean.getRaffleTicket_no());
						rafflePurchaseBeanList.add(rafflePurchaseBean);
					}
					kenoPurchaseBean.setPromoPurchaseBean(rafflePurchaseBeanList);
				} else if ("Zerotonine".equals(promobean.getPromoGametype())) {
					// call winfast purchase process
					int gameId = Util.getGameId("Zerotonine");
					FortunePurchaseBean drawGamePurchaseBean = new FortunePurchaseBean();
					drawGamePurchaseBean.setIsQP(1);
					drawGamePurchaseBean.setTotalNoOfPanels(1);
					// drawGamePurchaseBean.setSymbols(symbols);
					drawGamePurchaseBean.setNoOfPanels("1");

					// drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
					drawGamePurchaseBean.setDrawIdTableMap(kenoPurchaseBean.getDrawIdTableMap());
					drawGamePurchaseBean.setGame_no(gameId);
					drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
					// drawGamePurchaseBean.setIsQuickPick(isQuickPickNew);
					drawGamePurchaseBean.setNoOfDraws(1);
					drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
					drawGamePurchaseBean.setPartyType(userBean.getUserType());
					drawGamePurchaseBean.setUserId(userBean.getUserId());
					drawGamePurchaseBean.setRefMerchantId(kenoPurchaseBean.getRefMerchantId());
					drawGamePurchaseBean.setPurchaseChannel(kenoPurchaseBean.getPurchaseChannel());
					drawGamePurchaseBean.setPlrMobileNumber(kenoPurchaseBean.getPlrMobileNumber());
					drawGamePurchaseBean.setPromotkt(true);
					// if (drawIdArr != null) {
					// drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
					// }
					drawGamePurchaseBean.setIsAdvancedPlay(0);
					FortunePurchaseBean fortuneBean = zeroToNinePurchaseTicket(userBean, drawGamePurchaseBean);
					if ("SUCCESS".equalsIgnoreCase(fortuneBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, kenoPurchaseBean.getGame_no(),
								kenoPurchaseBean.getTicket_no(), fortuneBean.getTicket_no());
					} else {
						kenoPurchaseBean.setPromoSaleStatus("FAILED");
						return kenoPurchaseBean;
					}
					// kenoPurchaseBean.setFortunePurchaseBean(fortuneBean);
					kenoPurchaseBean.setPromoPurchaseBean(fortuneBean);
				} else if ("TwelveByTwentyFour".equals(promobean.getPromoGameName())
						&& kenoPurchaseBean.getTotalPurchaseAmt() >= Double
								.parseDouble(Utility.getPropertyValue("TwelveByTwentyFourFreeAmtLimit"))) {
					boolean isAvail = false;
					DateFormat df = new SimpleDateFormat("HH:mm:ss");
					DateFormat pdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						if (Utility.getPropertyValue("TwelveByTwentyFourFreeTime") != null) {
							String gameArr[] = Utility.getPropertyValue("TwelveByTwentyFourFreeTime").split("#");
							for (String gameData : gameArr) {
								String gameDataArr[] = gameData.split("~");
								if (Integer.parseInt(gameDataArr[0]) == kenoPurchaseBean.getGameId()) {
									String timeArr[] = gameDataArr[1].split("&");
									// new
									// SimpleDateFormat("HH:mm:ss").parse(new
									// SimpleDateFormat("HH:mm:ss").format(new
									// SimpleDateFormat("yyyy-MM-dd
									// HH:mm:ss").parse(str)))
									long ptime = df.parse(df.format(pdf.parse(kenoPurchaseBean.getPurchaseTime())))
											.getTime();
									// if(df.parse(kenoPurchaseBean.getPurchaseTime()).getTime()
									// >= df.parse(timeArr[0]).getTime() &&
									// df.parse(kenoPurchaseBean.getPurchaseTime()).getTime()
									// <= df.parse(timeArr[1]).getTime()) {
									if (ptime >= df.parse(timeArr[0]).getTime()
											&& ptime <= df.parse(timeArr[1]).getTime()) {
										isAvail = true;
									}
								}
							}
						}
						if (isAvail) {
							int gameId = Util.getGameId("TwelveByTwentyFour");

							KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
							drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
							drawGamePurchaseBean.setGameId(gameId);
							drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
							drawGamePurchaseBean.setBetAmountMultiple(new int[] { 1 });
							drawGamePurchaseBean.setIsQuickPick(new int[] { 1 });
							drawGamePurchaseBean.setPlayerData(new String[] { "QP" });
							drawGamePurchaseBean.setPlayType(new String[] { "Direct12" });
							drawGamePurchaseBean.setNoPicked(new String[] { "12" });
							drawGamePurchaseBean.setNoOfPanel(1);
							drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
							drawGamePurchaseBean.setPartyType(userBean.getUserType());
							drawGamePurchaseBean.setUserId(userBean.getUserId());
							drawGamePurchaseBean.setNoOfDraws(1);
							drawGamePurchaseBean.setIsAdvancedPlay(0);
							drawGamePurchaseBean.setRefMerchantId(kenoPurchaseBean.getRefMerchantId());
							drawGamePurchaseBean.setPurchaseChannel(kenoPurchaseBean.getPurchaseChannel());
							drawGamePurchaseBean.setPlrMobileNumber(kenoPurchaseBean.getPlrMobileNumber());
							drawGamePurchaseBean.setActionName(kenoPurchaseBean.getActionName());
							drawGamePurchaseBean.setLastGameId(kenoPurchaseBean.getLastGameId()); // check
																									// whether
																									// in
																									// use
							drawGamePurchaseBean.setDeviceType(kenoPurchaseBean.getDeviceType());
							drawGamePurchaseBean.setBarcodeType(kenoPurchaseBean.getBarcodeType());
							drawGamePurchaseBean.setUserMappingId(kenoPurchaseBean.getUserMappingId());
							drawGamePurchaseBean.setDrawIdTableMap(kenoPurchaseBean.getDrawIdTableMap());
							drawGamePurchaseBean.setLastSoldTicketNo("0");
							drawGamePurchaseBean.setServiceId(kenoPurchaseBean.getServiceId());
							drawGamePurchaseBean.setPromotkt(true);

							KenoPurchaseBean purchaseBean = new TwelveByTwentyFourHelper()
									.twelveByTwentyFourPurchaseTicket(userBean, drawGamePurchaseBean);

							if ("SUCCESS".equalsIgnoreCase(purchaseBean.getSaleStatus())) {
								int returnValue = orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(
										promobean.getSchemeId(), kenoPurchaseBean.getGame_no(),
										kenoPurchaseBean.getTicket_no(), purchaseBean.getTicket_no());
								if (returnValue == 0) {
									cancelPromoTkts(userBean, purchaseBean);
									kenoPurchaseBean.setSaleStatus("FAILED");
									return kenoPurchaseBean;
								}
							} else {
								kenoPurchaseBean.setPromoSaleStatus("FAILED");
								return kenoPurchaseBean;
							}
							kenoPurchaseBean.setPromoPurchaseBean(purchaseBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// kenoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
				// kenoPurchaseBean.setPromoPurchaseBean(rafflePurchaseBeanList);
			}
		}
		return kenoPurchaseBean;
	}


	public KenoPurchaseBean kenoTwoPurchaseTicket(UserInfoBean userBean,
			KenoPurchaseBean kenoTwoPurchaseBean) throws LMSException {
		KenoRequestBean kenoRequestBean=null;
		String status = null;
		double dgeTicketPrice = 0.0;
		double lmsTicketPrice = 0.0;
		long transId=0;
		Connection connection=null;
		ServiceRequest serviceRequest=null;
		try {
			connection=DBConnect.getConnection();
			connection.setAutoCommit(false);
			if (checkDrawAvailability(kenoTwoPurchaseBean.getGameId())) {
				kenoTwoPurchaseBean.setSaleStatus("NO_DRAWS");
				return kenoTwoPurchaseBean;
			}
			kenoTwoPurchaseBean.setSaleStatus("FAILED");
			dgeTicketPrice = prepareUnitPriceArrayAndCalculateDGETicketPrice(kenoTwoPurchaseBean);
			if (dgeTicketPrice <= 0) {
				kenoTwoPurchaseBean.setSaleStatus("FAILED");
				return kenoTwoPurchaseBean;
			}
			kenoTwoPurchaseBean.setTotalPurchaseAmt(dgeTicketPrice);
			lmsTicketPrice = CommonMethods.fmtToTwoDecimal(dgeTicketPrice);
			checkLastPrintedTicketForTerminal(userBean, kenoTwoPurchaseBean, connection);
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE",String.valueOf(lmsTicketPrice),connection);
			if (!isFraud) {
				transId = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,kenoTwoPurchaseBean.getGameId(),lmsTicketPrice,
						 kenoTwoPurchaseBean.getPlrMobileNumber(),connection);
				
				if(transId <= 0){
					kenoTwoPurchaseBean.setSaleStatus(getStatusIfErrorInTransaction(status, transId));
					return kenoTwoPurchaseBean;
				}
				kenoTwoPurchaseBean.setRefTransId(transId + "");
				connection.commit();
			} else {
				kenoTwoPurchaseBean.setSaleStatus("FRAUD");
				return kenoTwoPurchaseBean;
			}
			connection.commit();
         } catch (Exception e) {
			logger.error(e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(connection);
		}
				
		try{
			serviceRequest = createServiceRequest();
			serviceRequest.setServiceData(prepareKenoRequestBean(kenoTwoPurchaseBean));
			String dgeResponseString = delegate.getResponseString(serviceRequest);
			KenoResponseBean kenoResponseBean = new Gson().fromJson(dgeResponseString, KenoResponseBean.class);
			if (kenoResponseBean.getIsSuccess()) {
				setKenoTwoDGEResponseData(kenoTwoPurchaseBean, kenoResponseBean);
				double fromDgeTicketPrice = kenoTwoPurchaseBean.getTotalPurchaseAmt();
				kenoTwoPurchaseBean.setTotalPurchaseAmt(lmsTicketPrice);
				connection=DBConnect.getConnection();
				connection.setAutoCommit(false);
				if (fromDgeTicketPrice != dgeTicketPrice) {
					fromDgeTicketPrice = CommonMethods.roundDrawTktAmt(fromDgeTicketPrice);
					kenoTwoPurchaseBean.setTotalPurchaseAmt(fromDgeTicketPrice);
					transId = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,kenoTwoPurchaseBean.getGameId(),
									fromDgeTicketPrice,lmsTicketPrice, transId,connection);
				}
				int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(transId,
								kenoTwoPurchaseBean.getTicket_no(),kenoTwoPurchaseBean.getGameId(),
								userBean,kenoTwoPurchaseBean.getPurchaseChannel(),connection);
				if (tickUpd == 1) {
					doKenoTwoSaleAfterSuccesWork(userBean, kenoTwoPurchaseBean, connection);
					connection.commit();
					return kenoTwoPurchaseBean;
				} else {
					ifErrorOccurInKenoTwoTicketUpdation(userBean, kenoTwoPurchaseBean);
					return kenoTwoPurchaseBean;
				}
			} else {
				kenoTwoPurchaseBean.setSaleStatus(kenoResponseBean.getSaleStatus());
				failedKenoTwoTransactionIfFailedAtDGE(userBean, kenoTwoPurchaseBean, transId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			failedKenoTwoTrasactionIfAnyExceptionOccur(userBean, kenoTwoPurchaseBean, transId);
			
		}finally{
			DBConnect.closeCon(connection);
		}
		return kenoTwoPurchaseBean;
	}


	private ServiceRequest createServiceRequest() {
		ServiceRequest serviceRequest;
		serviceRequest = new ServiceRequest();
		serviceRequest.setServiceName(ServiceName.KENOTWO_MGMT);
		serviceRequest.setServiceMethod(ServiceMethodName.KENOTWO_PURCHASE_TICKET);
		return serviceRequest;
	}


	private void doKenoTwoSaleAfterSuccesWork(UserInfoBean userBean, KenoPurchaseBean kenoTwoPurchaseBean,
			Connection connection) throws SQLException {
		ajaxHelper.getAvlblCreditAmt(userBean,connection);
		kenoTwoPurchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), kenoTwoPurchaseBean.getGameId()));
		kenoTwoPurchaseBean.setSaleStatus("SUCCESS");
	}


	private void ifErrorOccurInKenoTwoTicketUpdation(UserInfoBean userBean, KenoPurchaseBean kenoTwoPurchaseBean)
			throws LMSException {
		kenoTwoPurchaseBean.setSaleStatus("FAILED");
		cancelTicket(kenoTwoPurchaseBean.getTicket_no()+ kenoTwoPurchaseBean.getReprintCount(),kenoTwoPurchaseBean.getPurchaseChannel(),
				kenoTwoPurchaseBean.getDrawIdTableMap(),kenoTwoPurchaseBean.getGame_no(),kenoTwoPurchaseBean.getPartyId(),
				kenoTwoPurchaseBean.getPartyType(),kenoTwoPurchaseBean.getRefMerchantId(),
				userBean, kenoTwoPurchaseBean.getRefTransId());
	}


	private void setKenoTwoDGEResponseData(KenoPurchaseBean kenoTwoPurchaseBean, KenoResponseBean kenoResponseBean) {
		kenoTwoPurchaseBean.setSaleStatus(kenoResponseBean.getSaleStatus());
		kenoTwoPurchaseBean.setTicket_no(kenoResponseBean.getTicketNo());
		kenoTwoPurchaseBean.setBarcodeCount(kenoResponseBean.getBarcodeCount());
		kenoTwoPurchaseBean.setNoOfDraws(kenoResponseBean.getNoOfDraws());
		kenoTwoPurchaseBean.setPurchaseTime(kenoResponseBean.getPurchaseTime());
		kenoTwoPurchaseBean.setReprintCount(kenoResponseBean.getReprintCount());
		kenoTwoPurchaseBean.setPlayerData(kenoResponseBean.getPlayerData());
		kenoTwoPurchaseBean.setTotalPurchaseAmt(kenoResponseBean.getTotalPurchaseAmt());
		kenoTwoPurchaseBean.setDrawDateTime(kenoResponseBean.getDrawDateTime());
	}


	private void failedKenoTwoTrasactionIfAnyExceptionOccur(UserInfoBean userBean, KenoPurchaseBean kenoTwoPurchaseBean,
			long transId) {
		if(kenoTwoPurchaseBean.getSaleStatus() == null){
			kenoTwoPurchaseBean.setSaleStatus("FAILED"); // Error
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
					userBean, kenoTwoPurchaseBean.getGameId(),
					"FAILED", transId);
		}else{
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
					userBean, kenoTwoPurchaseBean.getGameId(),
					"FAILED", transId);
		}
	}


	private void failedKenoTwoTransactionIfFailedAtDGE(UserInfoBean userBean, KenoPurchaseBean kenoTwoPurchaseBean,
			long transId) {
		if(kenoTwoPurchaseBean.getSaleStatus() == null){
			kenoTwoPurchaseBean.setSaleStatus("FAILED");// Error
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
					userBean, kenoTwoPurchaseBean.getGameId(),
					"FAILED", transId);
		}else if("ERROR_TICKET_LIMIT".equalsIgnoreCase(kenoTwoPurchaseBean.getSaleStatus())){
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
					userBean, kenoTwoPurchaseBean.getGameId(),
					"FAILED", transId);							
		}else{
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
					userBean, kenoTwoPurchaseBean.getGameId(),
					"FAILED", transId);
		}
	}


	private KenoRequestBean prepareKenoRequestBean(KenoPurchaseBean kenoTwoPurchaseBean) {
		KenoRequestBean	kenoRequestBean = new KenoRequestBeanBuilder().setBetAmountMultiple(kenoTwoPurchaseBean.getBetAmountMultiple()).setDrawIdTableMap(kenoTwoPurchaseBean.getDrawIdTableMap()).setDrawDateTime(kenoTwoPurchaseBean.getDrawDateTime())
				.setGame_no(kenoTwoPurchaseBean.getGame_no()).setGameId(kenoTwoPurchaseBean.getGameId()).setIsAdvancedPlay(kenoTwoPurchaseBean.getIsAdvancedPlay()).setIsQuickPick(kenoTwoPurchaseBean.getIsQuickPick()).setNoOfDraws(kenoTwoPurchaseBean.getNoOfDraws())
				.setPartyId(kenoTwoPurchaseBean.getPartyId()).setPartyType(kenoTwoPurchaseBean.getPartyType()).setPlayType(kenoTwoPurchaseBean.getPlayerData()).setPlayType(kenoTwoPurchaseBean.getPlayType()).setPurchaseChannel(kenoTwoPurchaseBean.getPurchaseChannel())
				.setRefMerchantId(kenoTwoPurchaseBean.getRefMerchantId()).setRefTransId(kenoTwoPurchaseBean.getRefTransId()).setUserId(kenoTwoPurchaseBean.getUserId()).setUserMappingId(kenoTwoPurchaseBean.getUserMappingId()).setServiceId(kenoTwoPurchaseBean.getServiceId())
				.setPromotkt(kenoTwoPurchaseBean.isPromotkt()).setUnitPrice(kenoTwoPurchaseBean.getUnitPrice()).setTotalPurchaseAmt(kenoTwoPurchaseBean.getTotalPurchaseAmt()).setQPPreGenerated(kenoTwoPurchaseBean.getQPPreGenerated()).setPlayerData(kenoTwoPurchaseBean.getPlayerData()).setNoPicked(kenoTwoPurchaseBean.getNoPicked())
				.preapreKenoRequestBean();
		return kenoRequestBean;
	}


	private String getStatusIfErrorInTransaction(String status, long transId) {
		if (transId == -1) {
			status = "AGT_INS_BAL";// Agent has insufficient balance
		} else if (transId == -2) {
			status = "FAILED";// Error LMS
		} else if (transId == 0) {
			status = "RET_INS_BAL";// Retailer has insufficient  Balance
		}
		return status;
	}


	private void checkLastPrintedTicketForTerminal(UserInfoBean userBean, KenoPurchaseBean kenoTwoPurchaseBean,
			Connection connection) throws LMSException {
		if("TERMINAL".equals(kenoTwoPurchaseBean.getDeviceType())) {
			int autoCancelHoldDays=Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS")); 
			checkLastPrintedTicketStatusAndUpdate(userBean,Long.parseLong(kenoTwoPurchaseBean.getLastSoldTicketNo()),kenoTwoPurchaseBean.getDeviceType(),kenoTwoPurchaseBean.getRefMerchantId(),autoCancelHoldDays,kenoTwoPurchaseBean.getActionName(), kenoTwoPurchaseBean.getLastGameId(),connection);
		}
	}


	private boolean checkDrawAvailability(int gameId) {
		return isDrawAvailable(gameId) || chkFreezeTimeSale(gameId);
	}


	private double prepareUnitPriceArrayAndCalculateDGETicketPrice(KenoPurchaseBean kenoTwoPurchaseBean) {
		double dgeTicketPrice=0.0;
		int noOfPanel = kenoTwoPurchaseBean.getNoOfPanel();
		int noOfLines[] = new int[noOfPanel];
		int betAmtMulArr[] = kenoTwoPurchaseBean.getBetAmountMultiple();
		double unitPriceArr[] = new double[noOfPanel];
		for (int i = 0; i < noOfPanel; i++) {
			preapareNoOfLinesArray(kenoTwoPurchaseBean, noOfLines, i);
			unitPriceArr[i] = Util.getUnitPrice(kenoTwoPurchaseBean.getGameId(), kenoTwoPurchaseBean.getPlayType()[i]);
			dgeTicketPrice += noOfLines[i] * unitPriceArr[i]* kenoTwoPurchaseBean.getNoOfDraws() * betAmtMulArr[i];
		}
		kenoTwoPurchaseBean.setUnitPrice(unitPriceArr);
		kenoTwoPurchaseBean.setNoOfLines(noOfLines);
		return dgeTicketPrice;
	}


	private void preapareNoOfLinesArray(KenoPurchaseBean kenoPurchaseBean, int[] noOfLines, int i) {
		String playType =  kenoPurchaseBean.getPlayType()[i];
		String[] noPick = kenoPurchaseBean.getNoPicked()[i].split(",");
		int[] n = new int[noPick.length];
		for (int j = 0; j < noPick.length; j++) {
			n[j] = Integer.parseInt(noPick[j]);
		}
		if (playType.contains("Direct")) {
			noOfLines[i] = 1;
		} else if (playType.equalsIgnoreCase("perm2")) {
			noOfLines[i] = n[0] * (n[0] - 1) / 2;
		} else if (playType.equalsIgnoreCase("perm3")) {
			noOfLines[i] = n[0] * (n[0] - 1) * (n[0] - 2) / 6;
		} else if (playType.equalsIgnoreCase("Banker1AgainstAll")) {
			noOfLines[i] = 89;
		} else if (playType.equalsIgnoreCase("banker")) {
			noOfLines[i] = n[0] * n[1];
		} else if(playType.equalsIgnoreCase("Perm1")){
			noOfLines[i] = n[0];
		}
	}

	public KenoPurchaseBean superTwoPurchaseTicket(UserInfoBean userBean, KenoPurchaseBean superTwoPurchaseBean)
			throws LMSException, SQLException {

		if (checkDrawAvailability(superTwoPurchaseBean.getGameId())) {
			superTwoPurchaseBean.setSaleStatus("NO_DRAWS");
			return superTwoPurchaseBean;
		}

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.SUPERTWO);
		sReq.setServiceMethod(ServiceMethodName.SUPERTWO_PURCHASE_TICKET);
		sReq.setServiceData(superTwoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		String status = "";
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;

		superTwoPurchaseBean.setSaleStatus("FAILED");
		long balDed = 0;
		double totPurAmt = 0.0;
		Connection con = null;
		try {
			int noOfPanel = superTwoPurchaseBean.getNoOfPanel();
			String playTypeArr[] = superTwoPurchaseBean.getPlayType();
			String[] noPickStr = superTwoPurchaseBean.getNoPicked();
			int noOfLines[] = new int[noOfPanel];
			int betAmtMulArr[] = superTwoPurchaseBean.getBetAmountMultiple();
			double unitPriceArr[] = new double[noOfPanel];
			for (int i = 0; i < noOfPanel; i++) {
				int n = Integer.parseInt(noPickStr[i]);
				if ("Perm-2 Position".equalsIgnoreCase(playTypeArr[i])) {
					noOfLines[i] = n * n;
				} else if ("Perm-2 Regular".equalsIgnoreCase(playTypeArr[i])) {
					noOfLines[i] = n * (n - 1) / 2 + n;
				} else if ("Perm-2".equalsIgnoreCase(playTypeArr[i])) {
					noOfLines[i] = n * (n - 1) / 2;
				} else {
					noOfLines[i] = 1;// in case of other bet types like Dir-2
										// and Banker
				}
				unitPriceArr[i] = Util.getUnitPrice(superTwoPurchaseBean.getGameId(), playTypeArr[i]);
				logger.debug("----unitPrice--" + unitPriceArr[i]);
				logger.debug("----playTypeArr--" + playTypeArr[i]);
				totPurAmt += noOfLines[i] * unitPriceArr[i] * superTwoPurchaseBean.getNoOfDraws() * betAmtMulArr[i];
			}

			superTwoPurchaseBean.setUnitPrice(unitPriceArr);

			superTwoPurchaseBean.setNoOfLines(noOfLines);

			logger.debug("!totPurAmt>>>superTwo" + totPurAmt);

			if (totPurAmt <= 0) {
				superTwoPurchaseBean.setSaleStatus("FAILED");// Error Draw
				return superTwoPurchaseBean;
			}
			superTwoPurchaseBean.setTotalPurchaseAmt(totPurAmt);

			con = getConnectionObject();

			if (superTwoPurchaseBean.getLastSoldTicketNo() != null) {
				if (!"0".equalsIgnoreCase(superTwoPurchaseBean.getLastSoldTicketNo())) {
					Util.insertLastSoldTicketTeminal(superTwoPurchaseBean.getUserId(),
							superTwoPurchaseBean.getLastSoldTicketNo(), superTwoPurchaseBean.getGameId(), con);
				}
			}
			logger.debug("Inside SuperTwo FE1*******");
			// rg calling
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "", con);
			if (!isFraud) {

				balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
						superTwoPurchaseBean.getGameId(), superTwoPurchaseBean.getTotalPurchaseAmt(),
						superTwoPurchaseBean.getPlrMobileNumber(), con);
				oldTotalPurchaseAmt = superTwoPurchaseBean.getTotalPurchaseAmt();
				logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
						+ oldTotalPurchaseAmt);
				con.commit();
			} else {
				logger.debug("Responsing Gaming not allowed to sale");
				superTwoPurchaseBean.setSaleStatus("FRAUD");
				return superTwoPurchaseBean;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		try {
			if (balDed > 0) {
				logger.debug("in superTwo if----------------");
				superTwoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					superTwoPurchaseBean = (KenoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = superTwoPurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = getConnectionObject();
					modifiedTotalPurchaseAmt = CommonMethods.roundDrawTktAmt(modifiedTotalPurchaseAmt);
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						superTwoPurchaseBean.setTotalPurchaseAmt(modifiedTotalPurchaseAmt);
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								superTwoPurchaseBean.getGameId(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}

					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							superTwoPurchaseBean.getTicket_no(), superTwoPurchaseBean.getGameId(), userBean,
							superTwoPurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						// superTwoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
						// superTwoPurchaseBean.getGame_no(), "PLAYER", "SALE",
						// "DG"));
						superTwoPurchaseBean.setAdvMsg(
								Util.getDGSaleAdvMessage(userBean.getUserOrgId(), superTwoPurchaseBean.getGameId()));
						AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
						ajxHelper.getAvlblCreditAmt(userBean, con);
						status = "SUCCESS";
						superTwoPurchaseBean.setSaleStatus(status);
						if (!superTwoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode.getBarcode(
									superTwoPurchaseBean.getTicket_no() + superTwoPurchaseBean.getReprintCount());
						}
						con.commit();
						return superTwoPurchaseBean;
					} else {
						status = "FAILED";
						superTwoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to Draw
						// Game Engine
						cancelTicket(superTwoPurchaseBean.getTicket_no() + superTwoPurchaseBean.getReprintCount(),
								superTwoPurchaseBean.getPurchaseChannel(), superTwoPurchaseBean.getDrawIdTableMap(),
								superTwoPurchaseBean.getGame_no(), superTwoPurchaseBean.getPartyId(),
								superTwoPurchaseBean.getPartyType(), superTwoPurchaseBean.getRefMerchantId(), userBean,
								superTwoPurchaseBean.getRefTransId());

						return superTwoPurchaseBean;
					}
				} else {
					superTwoPurchaseBean = (KenoPurchaseBean) sRes.getResponseData();
					if (superTwoPurchaseBean.getSaleStatus() == null) {
						superTwoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, superTwoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return superTwoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(superTwoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, superTwoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return superTwoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, superTwoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return superTwoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				superTwoPurchaseBean.setSaleStatus(status);
				return superTwoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DBConnect.closeCon(con);
		}
		return superTwoPurchaseBean;
	}

	// added for new pwt process for zimlottob two

	public LottoPurchaseBean commonPurchseProcess(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {

		lottoPurchaseBean.setPromotkt(false);
		lottoPurchaseBean = zimLottoTwoPurchaseTicket(userBean, lottoPurchaseBean);
		String saleStatus = lottoPurchaseBean.getSaleStatus();
		if ("SUCCESS".equalsIgnoreCase(saleStatus)) {
			lottoPurchaseBean = commonPromoPurchaseProcess(lottoPurchaseBean, userBean);
			// FortunePurchaseBean fortunePurchseben=
			// kenoPurchaseBean.getFortunePurchaseBean();
			if ("SUCCESS".equalsIgnoreCase(lottoPurchaseBean.getPromoSaleStatus())) {
				return lottoPurchaseBean;
			} else {
				cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
						lottoPurchaseBean.getPurchaseChannel(), lottoPurchaseBean.getDrawIdTableMap(),
						lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
						lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
						lottoPurchaseBean.getRefTransId());
			}
		}
		return lottoPurchaseBean;

	}

	// added for new pwt process

	public KenoPurchaseBean commonPurchseProcess(UserInfoBean userBean, KenoPurchaseBean kenoPurchaseBean)
			throws LMSException, SQLException {

		kenoPurchaseBean.setPromotkt(false);
		kenoPurchaseBean = kenoPurchaseTicket(userBean, kenoPurchaseBean);
		String saleStatus = kenoPurchaseBean.getSaleStatus();
		if ("SUCCESS".equalsIgnoreCase(saleStatus)) {
			kenoPurchaseBean = commonPromoPurchaseProcess(kenoPurchaseBean, userBean);
			// FortunePurchaseBean fortunePurchseben=
			// kenoPurchaseBean.getFortunePurchaseBean();
			if ("SUCCESS".equalsIgnoreCase(kenoPurchaseBean.getPromoSaleStatus())) {
				return kenoPurchaseBean;
			} else {
				cancelTicket(kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount(),
						kenoPurchaseBean.getPurchaseChannel(), kenoPurchaseBean.getDrawIdTableMap(),
						kenoPurchaseBean.getGame_no(), kenoPurchaseBean.getPartyId(), kenoPurchaseBean.getPartyType(),
						kenoPurchaseBean.getRefMerchantId(), userBean, kenoPurchaseBean.getRefTransId());
			}
		}
		return kenoPurchaseBean;

	}

	public KenoPurchaseBean commonPurchseProcessKenoTwo(UserInfoBean userBean,
			KenoPurchaseBean kenoPurchaseBean) throws LMSException  {
		try {
			kenoPurchaseBean.setPromotkt(false);
			kenoPurchaseBean = kenoTwoPurchaseTicket(userBean, kenoPurchaseBean);
			if ("SUCCESS".equalsIgnoreCase( kenoPurchaseBean.getSaleStatus())) {
				kenoPurchaseBean = commonPromoPurchaseProcess(kenoPurchaseBean,userBean);
				if (!"SUCCESS".equalsIgnoreCase(kenoPurchaseBean.getPromoSaleStatus())) {
					cancelTicket(kenoPurchaseBean.getTicket_no()+ kenoPurchaseBean.getReprintCount(), kenoPurchaseBean.getPurchaseChannel(), kenoPurchaseBean
							.getDrawIdTableMap(), kenoPurchaseBean.getGame_no(),kenoPurchaseBean.getPartyId(), kenoPurchaseBean.getPartyType(), kenoPurchaseBean.getRefMerchantId(), userBean, kenoPurchaseBean
									.getRefTransId());
				}
			}
		} catch (LMSException e) {
			throw e;
		}catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return kenoPurchaseBean;

	}

	public KenoPurchaseBean commonPurchseProcessSuperTwo(UserInfoBean userBean, KenoPurchaseBean superTwoPurchaseBean)
			throws Exception {

		superTwoPurchaseBean.setPromotkt(false);
		superTwoPurchaseBean = superTwoPurchaseTicket(userBean, superTwoPurchaseBean);
		String saleStatus = superTwoPurchaseBean.getSaleStatus();
		if ("SUCCESS".equalsIgnoreCase(saleStatus)) {
			superTwoPurchaseBean = commonPromoPurchaseProcess(superTwoPurchaseBean, userBean);
			// FortunePurchaseBean fortunePurchseben=
			// superTwoPurchaseBean.getFortunePurchaseBean();
			if ("SUCCESS".equalsIgnoreCase(superTwoPurchaseBean.getPromoSaleStatus())) {
				return superTwoPurchaseBean;
			} else {
				cancelTicket(superTwoPurchaseBean.getTicket_no() + superTwoPurchaseBean.getReprintCount(),
						superTwoPurchaseBean.getPurchaseChannel(), superTwoPurchaseBean.getDrawIdTableMap(),
						superTwoPurchaseBean.getGame_no(), superTwoPurchaseBean.getPartyId(),
						superTwoPurchaseBean.getPartyType(), superTwoPurchaseBean.getRefMerchantId(), userBean,
						superTwoPurchaseBean.getRefTransId());
			}
		}
		return superTwoPurchaseBean;

	}

	// Fast Lotto Data Validation
	public boolean fastLottoDataValidation(LottoPurchaseBean lottoPurchaseBean) {
		int noOfDraws = lottoPurchaseBean.getNoOfDraws();
		String[] picknumbers = lottoPurchaseBean.getPicknumbers();
		Set<Integer> picknumSet;
		String pickNum[];
		int noOfPanels = picknumbers.length;
		logger.debug("no of Panels: " + noOfPanels);
		if (noOfDraws > 0 && noOfPanels > 0) {
			for (int i = 0; i < noOfPanels; i++) {
				if (picknumbers[i].equalsIgnoreCase("QP")) {
					logger.debug("quick pick Selected");

				} else {
					logger.debug("not quick pick");
					logger.debug("Player picked Data:" + picknumbers[i]);
					pickNum = picknumbers[i].split(",");
					picknumSet = new HashSet<Integer>();
					for (String element : pickNum) {
						picknumSet.add(Integer.parseInt(element));
					}
					if (pickNum.length != picknumSet.size()
							|| pickNum.length > com.skilrock.lms.dge.gameconstants.FastlottoConstants.MAX_PLAYER_PICKED) {
						logger.debug("picNum.Length: " + pickNum.length + "Set length:  " + picknumSet.size());
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public LottoPurchaseBean fastLottoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {

		if (isDrawAvailable(lottoPurchaseBean.getGame_no()) || chkFreezeTimeSale(lottoPurchaseBean.getGame_no())) {
			lottoPurchaseBean.setSaleStatus("NO_DRAWS");
			return lottoPurchaseBean;
		}
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.FASTLOTTO);
		sReq.setServiceMethod(ServiceMethodName.FASTLOTTO_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;
		try {
			logger.debug("Inside  FE1*******");
			if (fastLottoDataValidation(lottoPurchaseBean)) {
				logger.debug("Data Validation Returns True");
				// lottoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
				// lottoPurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
				lottoPurchaseBean
						.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGame_no()));

				con = DBConnect.getConnection();
				// here insert the last sold tikcet on terminal or by third
				// party

				if (lottoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(),
								lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getGameId(), con);
					}
				}
				lottoPurchaseBean.setSaleStatus("FAILED");
				double totPurAmt = lottoPurchaseBean.getPicknumbers().length
						* Util.getUnitPrice(lottoPurchaseBean.getGame_no(), null) * lottoPurchaseBean.getNoOfDraws();
				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);
				lottoPurchaseBean.setUnitPrice(Util.getUnitPrice(lottoPurchaseBean.getGame_no(), null));
				lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);

				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTotalPurchaseAmt(),
							lottoPurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					lottoPurchaseBean.setSaleStatus("FRAUD");
				}
			} else {
				logger.debug("Data validation returns false");
				lottoPurchaseBean.setSaleStatus("FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
			 * String ticketNo = lottoPurchaseBean.getTicket_no(); String
			 * cancelChannel = lottoPurchaseBean.getPurchaseChannel();
			 * Map<Integer, Map<Integer, String>> drawIdTableMap =
			 * lottoPurchaseBean .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
		}

		try {
			if (balDed > 0) {
				lottoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								lottoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGame_no(), userBean,
							lottoPurchaseBean.getPurchaseChannel(), con);
					if (tickUpd == 1) {
						status = "SUCCESS";
						lottoPurchaseBean.setSaleStatus(status);
						if (!lottoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode
									.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
						}

						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGame_no());
						// String gameType =
						// getGameType(fortunePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, lottoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									lottoPurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
											lottoPurchaseBean.getPurchaseChannel(),
											lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
											lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
											lottoPurchaseBean.getRefMerchantId(), userBean,
											lottoPurchaseBean.getRefTransId());
									lottoPurchaseBean.setSaleStatus("FAILED");
									return lottoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}

						lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
						return lottoPurchaseBean;
					} else {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
								lottoPurchaseBean.getPurchaseChannel(), lottoPurchaseBean.getDrawIdTableMap(),
								lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
								lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
								lottoPurchaseBean.getRefTransId());

						return lottoPurchaseBean;
					}
				} else {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					if (lottoPurchaseBean.getSaleStatus() == null) {
						lottoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				lottoPurchaseBean.setSaleStatus(status);
				return lottoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lottoPurchaseBean;
	}
	// bonus ball two data validation

	public boolean bonusBallTwoDataValidation(LottoPurchaseBean lottoPurchaseBean) {
		int noOfDraws = lottoPurchaseBean.getNoOfDraws();
		String[] picknumbers = lottoPurchaseBean.getPicknumbers();
		Set<Integer> picknumSet;
		String pickNum[];
		int noOfPanels = picknumbers.length;
		logger.debug("no of Panels: " + noOfPanels);
		if (noOfPanels != 4 && lottoPurchaseBean.getPlayType().equalsIgnoreCase("direct5")) {
			return false;
		}
		if (noOfDraws > 0 && noOfPanels > 0 && lottoPurchaseBean.getPlayType().equalsIgnoreCase("direct5")) {
			for (int i = 0; i < noOfPanels; i++) {
				if (picknumbers[i].equalsIgnoreCase("QP")) {
					logger.debug("quick pick Selected");

				} else {
					logger.debug("not quick pick");
					logger.debug("Player picked Data:" + picknumbers[i]);
					pickNum = picknumbers[i].split(",");
					picknumSet = new HashSet<Integer>();
					for (String element : pickNum) {
						picknumSet.add(Integer.parseInt(element));
					}
					if (pickNum.length != picknumSet.size()
							|| pickNum.length > com.skilrock.lms.dge.gameconstants.BonusBalltwoConstants.MAX_PLAYER_PICKED) {
						logger.debug("picNum.Length: " + pickNum.length + "Set length:  " + picknumSet.size());
						return false;
					}
				}
			}
			return true;
		} else if ("Perm5".equalsIgnoreCase(lottoPurchaseBean.getPlayType())) {
			return true;

		}
		return false;

	}

	public LottoPurchaseBean bonusBallTwoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {

		if (isDrawAvailable(lottoPurchaseBean.getGame_no()) || chkFreezeTimeSale(lottoPurchaseBean.getGame_no())) {
			lottoPurchaseBean.setSaleStatus("NO_DRAWS");
			return lottoPurchaseBean;
		}
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.BUNUSTWOLOTTO);
		sReq.setServiceMethod(ServiceMethodName.BONUSBALLTWO_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		String status = "";
		long balDed = 0;
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		Connection con = null;
		try {
			logger.debug("Inside  FE1*******");
			if (bonusBallTwoDataValidation(lottoPurchaseBean)) {
				logger.debug("Data Validation Returns True");
				// lottoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
				// lottoPurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
				lottoPurchaseBean
						.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGame_no()));

				con = DBConnect.getConnection();
				lottoPurchaseBean.setSaleStatus("FAILED");
				// here insert the last sold tikcet on terminal or by third
				// party

				if (lottoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(),
								lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getGameId(), con);
					}
				}
				int noOfLines = lottoPurchaseBean.getPicknumbers().length;

				if ("Perm5".equals(lottoPurchaseBean.getPlayType())) {
					int n = lottoPurchaseBean.getNoPicked();
					if (n < 6) {
						lottoPurchaseBean.setSaleStatus("FAILED");
						return lottoPurchaseBean;
					}

					noOfLines = (n * (n - 1) * (n - 2) * (n - 3) * (n - 4)) / 120;

				}

				lottoPurchaseBean.setNoOfLines(noOfLines);

				double totPurAmt = noOfLines
						* Util.getUnitPrice(lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPlayType())
						* lottoPurchaseBean.getNoOfDraws()
						* (lottoPurchaseBean.getBetAmtMultiple() > 0 ? lottoPurchaseBean.getBetAmtMultiple() : 1);
				totPurAmt = CommonMethods.fmtToTwoDecimal(totPurAmt);
				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);
				// done to support generic coding of lotto games ...
				int noOfPanels = lottoPurchaseBean.getPicknumbers().length;
				int[] betAmountMul = new int[noOfPanels];
				for (int i = 0; i < noOfPanels; i++) {
					betAmountMul[i] = lottoPurchaseBean.getBetAmtMultiple();
				}

				lottoPurchaseBean.setBetAmountMultiple(betAmountMul);
				lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);
				lottoPurchaseBean.setUnitPrice(
						Util.getUnitPrice(lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPlayType()));

				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTotalPurchaseAmt(),
							lottoPurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					lottoPurchaseBean.setSaleStatus("FRAUD");
				}
			} else {
				logger.debug("Data validation returns false");
				lottoPurchaseBean.setSaleStatus("FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
			 * String ticketNo = lottoPurchaseBean.getTicket_no(); String
			 * cancelChannel = lottoPurchaseBean.getPurchaseChannel();
			 * Map<Integer, Map<Integer, String>> drawIdTableMap =
			 * lottoPurchaseBean .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
		}

		try {
			if (balDed > 0) {
				lottoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					// rouding

					modifiedTotalPurchaseAmt = CommonMethods.roundDrawTktAmt(modifiedTotalPurchaseAmt);
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						lottoPurchaseBean.setTotalPurchaseAmt(modifiedTotalPurchaseAmt);
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								lottoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGame_no(), userBean,
							lottoPurchaseBean.getPurchaseChannel(), con);
					if (tickUpd == 1) {
						status = "SUCCESS";
						lottoPurchaseBean.setSaleStatus(status);
						if (!lottoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode
									.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
						}

						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
						// CommonFunctionsHelper commonHelper = new
						// CommonFunctionsHelper();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGame_no());
						// String gameType =
						// getGameType(fortunePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, lottoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									lottoPurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
											lottoPurchaseBean.getPurchaseChannel(),
											lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
											lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
											lottoPurchaseBean.getRefMerchantId(), userBean,
											lottoPurchaseBean.getRefTransId());
									lottoPurchaseBean.setSaleStatus("FAILED");
									return lottoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}

						lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
						return lottoPurchaseBean;
					} else {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
								lottoPurchaseBean.getPurchaseChannel(), lottoPurchaseBean.getDrawIdTableMap(),
								lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
								lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
								lottoPurchaseBean.getRefTransId());

						return lottoPurchaseBean;
					}
				} else {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					if (lottoPurchaseBean.getSaleStatus() == null) {
						lottoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				lottoPurchaseBean.setSaleStatus(status);
				return lottoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lottoPurchaseBean;
	}

	private void fillRaffleBean(RafflePurchaseBean rafflePurchaseBean, Object gameBean) {
		if (gameBean instanceof FortunePurchaseBean) {
			logger.debug(" FortunePurchaseBean ");
			FortunePurchaseBean parentBean = (FortunePurchaseBean) gameBean;
			rafflePurchaseBean.setBetAmountMultiple(1);
			rafflePurchaseBean.setPartyId(parentBean.getPartyId());
			rafflePurchaseBean.setUserId(parentBean.getUserId());
			rafflePurchaseBean.setPartyType(parentBean.getPartyType());
			rafflePurchaseBean.setNoOfDraws(parentBean.getNoOfDraws());
			rafflePurchaseBean.setRefTransId(parentBean.getRefTransId());
			rafflePurchaseBean.setPurchaseChannel(parentBean.getPurchaseChannel());
			rafflePurchaseBean.setRefMerchantId(parentBean.getRefMerchantId());
			rafflePurchaseBean.setDrawIdTableMap(parentBean.getDrawIdTableMap());
			rafflePurchaseBean.setParentTktNo(parentBean.getTicket_no());
			rafflePurchaseBean.setGame_no(parentBean.getGame_no());
			// rafflePurchaseBean.setRaffleTicketType("ORIGINAL");
			rafflePurchaseBean.setRaffle_no(parentBean.getRaffleNo());
			rafflePurchaseBean.setPlrMobileNumber(parentBean.getPlrMobileNumber());
		} else if (gameBean instanceof LottoPurchaseBean) {

			LottoPurchaseBean parentBean = (LottoPurchaseBean) gameBean;
			rafflePurchaseBean.setBetAmountMultiple(1);
			rafflePurchaseBean.setPartyId(parentBean.getPartyId());
			rafflePurchaseBean.setUserId(parentBean.getUserId());
			rafflePurchaseBean.setPartyType(parentBean.getPartyType());
			rafflePurchaseBean.setNoOfDraws(parentBean.getNoOfDraws());
			rafflePurchaseBean.setRefTransId(parentBean.getRefTransId());
			rafflePurchaseBean.setPurchaseChannel(parentBean.getPurchaseChannel());
			rafflePurchaseBean.setRefMerchantId(parentBean.getRefMerchantId());
			rafflePurchaseBean.setDrawIdTableMap(parentBean.getDrawIdTableMap());
			rafflePurchaseBean.setParentTktNo(parentBean.getTicket_no());
			rafflePurchaseBean.setGame_no(parentBean.getGame_no());
			// rafflePurchaseBean.setRaffleTicketType("ORIGINAL");
			rafflePurchaseBean.setRaffle_no(parentBean.getRaffleNo());
			rafflePurchaseBean.setPlrMobileNumber(parentBean.getPlrMobileNumber());
		} else if (gameBean instanceof KenoPurchaseBean) {
			logger.debug(" kenoPurchaseBean ");
			KenoPurchaseBean parentBean = (KenoPurchaseBean) gameBean;
			rafflePurchaseBean.setBetAmountMultiple(1);
			rafflePurchaseBean.setPartyId(parentBean.getPartyId());
			rafflePurchaseBean.setUserId(parentBean.getUserId());
			rafflePurchaseBean.setPartyType(parentBean.getPartyType());
			rafflePurchaseBean.setNoOfDraws(parentBean.getNoofDrawsForPromo());
			rafflePurchaseBean.setNoOfDrawPlayedFor(parentBean.getNoofDrawsForPromo());
			rafflePurchaseBean.setRefTransId(parentBean.getRefTransId());
			rafflePurchaseBean.setPurchaseChannel(parentBean.getPurchaseChannel());
			rafflePurchaseBean.setRefMerchantId(parentBean.getRefMerchantId());
			rafflePurchaseBean.setDrawIdTableMap(parentBean.getDrawIdTableMap());
			rafflePurchaseBean.setParentTktNo(parentBean.getTicket_no());
			// rafflePurchaseBean.setGame_no(parentBean.getGame_no());
			rafflePurchaseBean.setGameId(parentBean.getRaffleNo());// Affle game
																	// Id
			// rafflePurchaseBean.setRaffleTicketType("ORIGINAL");
			rafflePurchaseBean.setRaffle_no(parentBean.getRaffleNo());
			rafflePurchaseBean.setPlrMobileNumber(parentBean.getPlrMobileNumber());
			rafflePurchaseBean.setUserMappingId(parentBean.getUserMappingId());
			rafflePurchaseBean.setServiceName("DG");
			rafflePurchaseBean.setServiceId(parentBean.getServiceId());

			// Will be implement later
		}

	}

	/**
	 * Data Validation method checks for
	 * noOfDraws,isQuikePic,BetAmountMultiple,SumofBetAmtMultiple etc,It also
	 * calculates Total Purchase Amt
	 * 
	 */
	public boolean fortuneDataValidation(FortunePurchaseBean card12PurchaseBean) {
		logger.debug("Inside Data Validation");
		int noOfDraw = card12PurchaseBean.getNoOfDraws();

		int isQP = card12PurchaseBean.getIsQP();
		int QPTotalNoPanels = 0;
		if (isQP == 1) {
			logger.debug("QP selected ....");
			QPTotalNoPanels = card12PurchaseBean.getTotalNoOfPanels();
			if (QPTotalNoPanels > 1000) {
				return false;
			}
			return true;
		} else {
			String noOfPanels = card12PurchaseBean.getNoOfPanels();
			String[] noOfPanel = noOfPanels.split(",");
			int betAmountMultiple[] = null;
			int totalNoOfPanels = 0;
			String symbols = card12PurchaseBean.getSymbols();
			String[] pickedNumber;
			Set<String> s = new HashSet<String>();
			betAmountMultiple = new int[noOfPanel.length];
			logger.debug("QP not selected ....");
			if (isQP == 2) {
				for (int i = 0; i < noOfPanel.length; i++) {
					betAmountMultiple[i] = Integer.parseInt(noOfPanel[i]);
					if (betAmountMultiple[i] > 1000) {
						return false;
					}
					totalNoOfPanels += betAmountMultiple[i];
				}
				pickedNumber = symbols.split(",");
				// Checks for unique Symbols
				for (String element : pickedNumber) {
					s.add(element);
				}
				if (noOfDraw > 0 && totalNoOfPanels <= 1000 && pickedNumber.length == s.size()) {
					return true;
				}
			}
			return false;
		}

	}

	public boolean fortuneTwoDataValidation(FortuneTwoPurchaseBean fortuneTwoPurchaseBean) {
		/*
		 * logger.debug("Inside Data Validation"); int noOfDraw =
		 * fortuneTwoPurchaseBean.getNoOfDraws();
		 * 
		 * int isQP = fortuneTwoPurchaseBean.getIsQP(); int QPTotalNoPanels = 0;
		 * if (isQP == 1) { logger.debug("QP selected ...."); QPTotalNoPanels =
		 * fortuneTwoPurchaseBean.getTotalNoOfPanels(); if (QPTotalNoPanels >
		 * 1000) { return false; } return true; } else { String noOfPanels =
		 * fortuneTwoPurchaseBean.getNoOfPanels(); String[] noOfPanel =
		 * noOfPanels.split(","); int betAmountMultiple[] = null; int
		 * totalNoOfPanels = 0; String symbols =
		 * fortuneTwoPurchaseBean.getSymbols(); String[] pickedNumber;
		 * Set<String> s = new HashSet<String>(); betAmountMultiple = new
		 * int[noOfPanel.length]; logger.debug("QP not selected ...."); if (isQP
		 * == 2) { for (int i = 0; i < noOfPanel.length; i++) {
		 * betAmountMultiple[i] = Integer.parseInt(noOfPanel[i]); if
		 * (betAmountMultiple[i] > 1000) { return false; } totalNoOfPanels +=
		 * betAmountMultiple[i]; } pickedNumber = symbols.split(","); // Checks
		 * for unique Symbols for (String element : pickedNumber) {
		 * s.add(element); } if (noOfDraw > 0 && totalNoOfPanels <= 1000 &&
		 * pickedNumber.length == s.size()) { return true; } } return false; }
		 * 
		 */return true;
	}

	public boolean fortuneThreeDataValidation(FortuneThreePurchaseBean fortuneThreePurchaseBean) {
		/*
		 * logger.debug("Inside Data Validation"); int noOfDraw =
		 * fortuneTwoPurchaseBean.getNoOfDraws();
		 * 
		 * int isQP = fortuneTwoPurchaseBean.getIsQP(); int QPTotalNoPanels = 0;
		 * if (isQP == 1) { logger.debug("QP selected ...."); QPTotalNoPanels =
		 * fortuneTwoPurchaseBean.getTotalNoOfPanels(); if (QPTotalNoPanels >
		 * 1000) { return false; } return true; } else { String noOfPanels =
		 * fortuneTwoPurchaseBean.getNoOfPanels(); String[] noOfPanel =
		 * noOfPanels.split(","); int betAmountMultiple[] = null; int
		 * totalNoOfPanels = 0; String symbols =
		 * fortuneTwoPurchaseBean.getSymbols(); String[] pickedNumber;
		 * Set<String> s = new HashSet<String>(); betAmountMultiple = new
		 * int[noOfPanel.length]; logger.debug("QP not selected ...."); if (isQP
		 * == 2) { for (int i = 0; i < noOfPanel.length; i++) {
		 * betAmountMultiple[i] = Integer.parseInt(noOfPanel[i]); if
		 * (betAmountMultiple[i] > 1000) { return false; } totalNoOfPanels +=
		 * betAmountMultiple[i]; } pickedNumber = symbols.split(","); // Checks
		 * for unique Symbols for (String element : pickedNumber) {
		 * s.add(element); } if (noOfDraw > 0 && totalNoOfPanels <= 1000 &&
		 * pickedNumber.length == s.size()) { return true; } } return false; }
		 * 
		 */return true;
	}

	public FortunePurchaseBean fortunePurchaseTicket(UserInfoBean userBean, FortunePurchaseBean fortunePurchaseBean)
			throws Exception {

		if (isDrawAvailable(fortunePurchaseBean.getGameId()) || chkFreezeTimeSale(fortunePurchaseBean.getGameId())) {
			fortunePurchaseBean.setSaleStatus("NO_DRAWS");
			return fortunePurchaseBean;
		}
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.FORTUNE_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FORTUNE_PURCHASE_TICKET);
		sReq.setServiceData(fortunePurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		fortunePurchaseBean.setSaleStatus("FAILED");
		long balDed = 0;
		int tickUpd = 0;
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		Connection con = null;
		try {
			if (fortuneDataValidation(fortunePurchaseBean)) {
				logger.debug("Data validation returns true");

				con = DBConnect.getConnection();

				// here insert the last sold tikcet on terminal or by third
				// party

				if (fortunePurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(fortunePurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(fortunePurchaseBean.getUserId(),
								fortunePurchaseBean.getLastSoldTicketNo(), fortunePurchaseBean.getGameId(), con);
					}
				}

				// fortunePurchaseBean.setAdvMsg(Util.getAdvMessage(
				// userBean.getUserOrgId(), fortunePurchaseBean.getGame_no(),
				// "PLAYER", "SALE", "DG"));
				fortunePurchaseBean
						.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), fortunePurchaseBean.getGame_no()));

				logger.debug(fortunePurchaseBean.getTotalNoOfPanels()
						* Util.getUnitPrice(fortunePurchaseBean.getGameId(), fortunePurchaseBean.getPlayType())
						* fortunePurchaseBean.getNoOfDraws());

				double totPurAmt = fortunePurchaseBean.getTotalNoOfPanels()
						* Util.getUnitPrice(fortunePurchaseBean.getGameId(), fortunePurchaseBean.getPlayType())
						* fortunePurchaseBean.getNoOfDraws();

				fortunePurchaseBean.setTotalPurchaseAmt(totPurAmt);
				if (!userBean.getUserType().equals("RETAILER")) {
					return fortunePurchaseBean;
				}
				boolean isPromoAssociated = false;

				logger.debug("Inside Fortune FE1*******  ");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "", con);
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							fortunePurchaseBean.getGameId(), fortunePurchaseBean.getTotalPurchaseAmt(),
							fortunePurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = fortunePurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);
					logger.debug("Bal deduction inside DrawGameRPOSHelper Just Before  getting Success :" + balDed);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					fortunePurchaseBean.setSaleStatus("FRAUD");
				}
			} else {

				logger.debug("Data validation returns false");
				fortunePurchaseBean.setSaleStatus("FAILED");
				return fortunePurchaseBean;

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			if (balDed > 0) {
				fortunePurchaseBean.setRefTransId(balDed + "");
				fortunePurchaseBean.setPromotkt(false);
				sRes = delegate.getResponse(sReq);

				Type elementType = new TypeToken<FortunePurchaseBean>() {
				}.getType();

				if (sRes.getIsSuccess()) {
					fortunePurchaseBean = new Gson().fromJson((JsonElement) sRes.getResponseData(), elementType);
					modifiedTotalPurchaseAmt = fortunePurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								fortunePurchaseBean.getGameId(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							fortunePurchaseBean.getTicket_no(), fortunePurchaseBean.getGameId(), userBean,
							fortunePurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						fortunePurchaseBean.setSaleStatus(status);
						if (!fortunePurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode.getBarcode(
									fortunePurchaseBean.getTicket_no() + fortunePurchaseBean.getReprintCount());
						}
						// call promo purchase process here
						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap
								.get(fortunePurchaseBean.getGame_no());
						// String gameType =
						// getGameType(fortunePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								fortunePurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, fortunePurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									fortunePurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(
											fortunePurchaseBean.getTicket_no() + fortunePurchaseBean.getReprintCount(),
											fortunePurchaseBean.getPurchaseChannel(),
											fortunePurchaseBean.getDrawIdTableMap(), fortunePurchaseBean.getGameId(),
											fortunePurchaseBean.getPartyId(), fortunePurchaseBean.getPartyType(),
											fortunePurchaseBean.getRefMerchantId(), userBean,
											fortunePurchaseBean.getRefTransId());
									fortunePurchaseBean.setSaleStatus("FAILED");
									return fortunePurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}

						fortunePurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
						return fortunePurchaseBean;
					} else {
						status = "FAILED";

						fortunePurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(fortunePurchaseBean.getTicket_no(), fortunePurchaseBean.getPurchaseChannel(),
								fortunePurchaseBean.getDrawIdTableMap(), fortunePurchaseBean.getGameId(),
								fortunePurchaseBean.getPartyId(), fortunePurchaseBean.getPartyType(),
								fortunePurchaseBean.getRefMerchantId(), userBean, fortunePurchaseBean.getRefTransId());

						return fortunePurchaseBean;
					}

				} else {
					fortunePurchaseBean = new Gson().fromJson((JsonElement) sRes.getResponseData(), elementType);
					if (fortunePurchaseBean.getSaleStatus() == null) {
						fortunePurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fortunePurchaseBean.getGame_no(),
								"FAILED", balDed);
						return fortunePurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(fortunePurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fortunePurchaseBean.getGame_no(),
								"FAILED", balDed);
						return fortunePurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fortunePurchaseBean.getGame_no(),
								"FAILED", balDed);
						return fortunePurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				fortunePurchaseBean.setSaleStatus(status);
				return fortunePurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		logger.debug("Inside Fortune FE*******" + sRes);
		return fortunePurchaseBean;
	}

	// new fortune game

	public FortuneTwoPurchaseBean fortuneTwoPurchaseTicket(UserInfoBean userBean,
			FortuneTwoPurchaseBean fortuneTwoPurchaseBean) throws Exception {

		if (isDrawAvailable(fortuneTwoPurchaseBean.getGame_no())
				|| chkFreezeTimeSale(fortuneTwoPurchaseBean.getGame_no())) {
			fortuneTwoPurchaseBean.setSaleStatus("NO_DRAWS");
			return fortuneTwoPurchaseBean;
		}
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.FORTUNETWO);
		sReq.setServiceMethod(ServiceMethodName.FORTUNE_TWO_PURCHASE_TICKET);
		sReq.setServiceData(fortuneTwoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		long balDed = 0;
		int tickUpd = 0;
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		Connection con = null;
		try {
			if (fortuneTwoDataValidation(fortuneTwoPurchaseBean)) {
				logger.debug("Data validation returns true");
				// fortuneTwoPurchaseBean.setAdvMsg(Util.getAdvMessage(
				// userBean.getUserOrgId(), fortuneTwoPurchaseBean.getGame_no(),
				// "PLAYER", "SALE", "DG"));
				fortuneTwoPurchaseBean.setAdvMsg(
						Util.getDGSaleAdvMessage(userBean.getUserOrgId(), fortuneTwoPurchaseBean.getGame_no()));

				fortuneTwoPurchaseBean.setSaleStatus("FAILED");
				int noOfPanels = fortuneTwoPurchaseBean.getNoOfPanel();
				double totPurAmt = 0.0;
				con = DBConnect.getConnection();
				// here insert the last sold tikcet on terminal or by third
				// party

				if (fortuneTwoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(fortuneTwoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(fortuneTwoPurchaseBean.getUserId(),
								fortuneTwoPurchaseBean.getLastSoldTicketNo(), fortuneTwoPurchaseBean.getGameId(), con);
					}
				}

				double[] unitPriceArr = new double[noOfPanels];
				String playTypeArr[] = fortuneTwoPurchaseBean.getPlayType();
				int[] noOfLines = new int[noOfPanels];
				for (int i = 0; i < noOfPanels; i++) {
					String playType = playTypeArr[i];
					if (playType.contains("Direct")) {
						noOfLines[i] = 1;
					} else if (playType.contains("Banker")) {
						noOfLines[i] = 12;// for banker2 and banker3
					}
					totPurAmt += noOfLines[i]
							* Util.getUnitPrice(fortuneTwoPurchaseBean.getGame_no(),
									fortuneTwoPurchaseBean.getPlayType()[i])
							* fortuneTwoPurchaseBean.getBetAmountMultiple()[i] * fortuneTwoPurchaseBean.getNoOfDraws();
					unitPriceArr[i] = Util.getUnitPrice(fortuneTwoPurchaseBean.getGame_no(),
							fortuneTwoPurchaseBean.getPlayType()[i]);
				}
				logger.debug("calculated total purchase amt:" + totPurAmt);

				/*
				 * totPurAmt = fortuneTwoPurchaseBean.getTotalNoOfPanels()
				 * Util.getUnitPrice(fortuneTwoPurchaseBean.getGame_no(), null)
				 * fortuneTwoPurchaseBean.getNoOfDraws();
				 */

				fortuneTwoPurchaseBean.setTotalPurchaseAmt(totPurAmt);
				fortuneTwoPurchaseBean.setUnitPrice(unitPriceArr);
				fortuneTwoPurchaseBean.setNoOfLines(noOfLines);
				if (!userBean.getUserType().equals("RETAILER")) {
					return fortuneTwoPurchaseBean;
				}

				boolean isPromoAssociated = false;

				logger.debug("Inside Fortune two  FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "", con);
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							fortuneTwoPurchaseBean.getGame_no(), fortuneTwoPurchaseBean.getTotalPurchaseAmt(),
							fortuneTwoPurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = fortuneTwoPurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);

					logger.debug("Bal deduction inside DrawGameRPOSHelper Just Before  getting Success :" + balDed);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					fortuneTwoPurchaseBean.setSaleStatus("FRAUD");
				}
			} else {

				logger.debug("Data validation returns false");
				fortuneTwoPurchaseBean.setSaleStatus("FAILED");
				return fortuneTwoPurchaseBean;

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			if (balDed > 0) {
				fortuneTwoPurchaseBean.setRefTransId(balDed + "");
				fortuneTwoPurchaseBean.setPromotkt(false);
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					fortuneTwoPurchaseBean = (FortuneTwoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = fortuneTwoPurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								fortuneTwoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt,
								balDed, con);

					}
					tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							fortuneTwoPurchaseBean.getTicket_no(), fortuneTwoPurchaseBean.getGame_no(), userBean,
							fortuneTwoPurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						fortuneTwoPurchaseBean.setSaleStatus(status);
						if (!fortuneTwoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode.getBarcode(
									fortuneTwoPurchaseBean.getTicket_no() + fortuneTwoPurchaseBean.getReprintCount());
						}
						// call promo purchase process here
						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap
								.get(fortuneTwoPurchaseBean.getGame_no());
						// String gameType =
						// getGameType(fortuneTwoPurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								fortuneTwoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, fortuneTwoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									fortuneTwoPurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(
											fortuneTwoPurchaseBean.getTicket_no()
													+ fortuneTwoPurchaseBean.getReprintCount(),
											fortuneTwoPurchaseBean.getPurchaseChannel(),
											fortuneTwoPurchaseBean.getDrawIdTableMap(),
											fortuneTwoPurchaseBean.getGame_no(), fortuneTwoPurchaseBean.getPartyId(),
											fortuneTwoPurchaseBean.getPartyType(),
											fortuneTwoPurchaseBean.getRefMerchantId(), userBean,
											fortuneTwoPurchaseBean.getRefTransId());
									fortuneTwoPurchaseBean.setSaleStatus("FAILED");
									return fortuneTwoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}

						fortuneTwoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
						return fortuneTwoPurchaseBean;
					} else {
						status = "FAILED";

						fortuneTwoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(fortuneTwoPurchaseBean.getTicket_no(), fortuneTwoPurchaseBean.getPurchaseChannel(),
								fortuneTwoPurchaseBean.getDrawIdTableMap(), fortuneTwoPurchaseBean.getGame_no(),
								fortuneTwoPurchaseBean.getPartyId(), fortuneTwoPurchaseBean.getPartyType(),
								fortuneTwoPurchaseBean.getRefMerchantId(), userBean,
								fortuneTwoPurchaseBean.getRefTransId());

						return fortuneTwoPurchaseBean;
					}

				} else {
					fortuneTwoPurchaseBean = (FortuneTwoPurchaseBean) sRes.getResponseData();
					if (fortuneTwoPurchaseBean.getSaleStatus() == null) {
						fortuneTwoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fortuneTwoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return fortuneTwoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(fortuneTwoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fortuneTwoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return fortuneTwoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fortuneTwoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return fortuneTwoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				fortuneTwoPurchaseBean.setSaleStatus(status);
				return fortuneTwoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return fortuneTwoPurchaseBean;
	}

	// fortune three by sumit
	public FortuneThreePurchaseBean fortuneThreePurchaseTicket(UserInfoBean userBean,
			FortuneThreePurchaseBean fortuneThreePurchaseBean) throws Exception {

		if (isDrawAvailable(fortuneThreePurchaseBean.getGame_no())
				|| chkFreezeTimeSale(fortuneThreePurchaseBean.getGame_no())) {
			fortuneThreePurchaseBean.setSaleStatus("NO_DRAWS");
			return fortuneThreePurchaseBean;
		}

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.FORTUNETHREE);
		sReq.setServiceMethod(ServiceMethodName.FORTUNE_THREE_PURCHASE_TICKET);
		sReq.setServiceData(fortuneThreePurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		long balDed = 0;
		int tickUpd = 0;
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		Connection con = null;
		String status = "";
		try {
			if (fortuneThreeDataValidation(fortuneThreePurchaseBean)) {
				logger.debug("Data validation returns true");
				// fortuneThreePurchaseBean.setAdvMsg(Util.getAdvMessage(
				// userBean.getUserOrgId(),
				// fortuneThreePurchaseBean.getGame_no(),
				// "PLAYER", "SALE", "DG"));
				fortuneThreePurchaseBean.setAdvMsg(
						Util.getDGSaleAdvMessage(userBean.getUserOrgId(), fortuneThreePurchaseBean.getGame_no()));
				con = DBConnect.getConnection();
				fortuneThreePurchaseBean.setSaleStatus("FAILED");

				// here insert the last sold tikcet on terminal or by third
				// party

				if (fortuneThreePurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(fortuneThreePurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(fortuneThreePurchaseBean.getUserId(),
								fortuneThreePurchaseBean.getLastSoldTicketNo(), fortuneThreePurchaseBean.getGameId(),
								con);
					}
				}

				int noOfPanels = fortuneThreePurchaseBean.getNoOfPanel();
				double totPurAmt = 0.0;
				double[] unitPriceArr = new double[noOfPanels];
				String playTypeArr[] = fortuneThreePurchaseBean.getPlayType();
				int[] noOfLines = new int[noOfPanels];
				for (int i = 0; i < noOfPanels; i++) {
					String playType = playTypeArr[i];
					if (playType.contains("Direct")) {
						noOfLines[i] = 1;
					} else if (playType.contains("Banker")) {
						noOfLines[i] = 12;// for banker2 and banker3
					}
					totPurAmt += noOfLines[i]
							* Util.getUnitPrice(fortuneThreePurchaseBean.getGame_no(),
									fortuneThreePurchaseBean.getPlayType()[i])
							* fortuneThreePurchaseBean.getBetAmountMultiple()[i]
							* fortuneThreePurchaseBean.getNoOfDraws();
					unitPriceArr[i] = Util.getUnitPrice(fortuneThreePurchaseBean.getGame_no(),
							fortuneThreePurchaseBean.getPlayType()[i]);
				}
				logger.debug("calculated total purchase amt:" + totPurAmt);

				/*
				 * totPurAmt = fortuneThreePurchaseBean.getTotalNoOfPanels()
				 * Util.getUnitPrice(fortuneThreePurchaseBean.getGame_no(),
				 * null) fortuneThreePurchaseBean.getNoOfDraws();
				 */

				fortuneThreePurchaseBean.setTotalPurchaseAmt(totPurAmt);
				fortuneThreePurchaseBean.setUnitPrice(unitPriceArr);
				fortuneThreePurchaseBean.setNoOfLines(noOfLines);
				if (!userBean.getUserType().equals("RETAILER")) {
					return fortuneThreePurchaseBean;
				}

				boolean isPromoAssociated = false;

				logger.debug("Inside Fortune three  FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							fortuneThreePurchaseBean.getGame_no(), fortuneThreePurchaseBean.getTotalPurchaseAmt(),
							fortuneThreePurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = fortuneThreePurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);

					logger.debug("Bal deduction inside DrawGameRPOSHelper Just Before  getting Success :" + balDed);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					fortuneThreePurchaseBean.setSaleStatus("FRAUD");
				}
			} else {

				logger.debug("Data validation returns false");
				fortuneThreePurchaseBean.setSaleStatus("FAILED");
				return fortuneThreePurchaseBean;

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			if (balDed > 0) {
				fortuneThreePurchaseBean.setRefTransId(balDed + "");
				fortuneThreePurchaseBean.setPromotkt(false);
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					fortuneThreePurchaseBean = (FortuneThreePurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = fortuneThreePurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								fortuneThreePurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt,
								balDed, con);

					}
					tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							fortuneThreePurchaseBean.getTicket_no(), fortuneThreePurchaseBean.getGame_no(), userBean,
							fortuneThreePurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						fortuneThreePurchaseBean.setSaleStatus(status);
						if (!fortuneThreePurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode.getBarcode(fortuneThreePurchaseBean.getTicket_no()
									+ fortuneThreePurchaseBean.getReprintCount());
						}
						// call promo purchase process here
						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap
								.get(fortuneThreePurchaseBean.getGame_no());
						// String gameType =
						// getGameType(fortuneThreePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								fortuneThreePurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, fortuneThreePurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									fortuneThreePurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(
											fortuneThreePurchaseBean.getTicket_no()
													+ fortuneThreePurchaseBean.getReprintCount(),
											fortuneThreePurchaseBean.getPurchaseChannel(),
											fortuneThreePurchaseBean.getDrawIdTableMap(),
											fortuneThreePurchaseBean.getGame_no(),
											fortuneThreePurchaseBean.getPartyId(),
											fortuneThreePurchaseBean.getPartyType(),
											fortuneThreePurchaseBean.getRefMerchantId(), userBean,
											fortuneThreePurchaseBean.getRefTransId());
									fortuneThreePurchaseBean.setSaleStatus("FAILED");
									return fortuneThreePurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}

						fortuneThreePurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
						return fortuneThreePurchaseBean;
					} else {
						status = "FAILED";

						fortuneThreePurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(fortuneThreePurchaseBean.getTicket_no(),
								fortuneThreePurchaseBean.getPurchaseChannel(),
								fortuneThreePurchaseBean.getDrawIdTableMap(), fortuneThreePurchaseBean.getGame_no(),
								fortuneThreePurchaseBean.getPartyId(), fortuneThreePurchaseBean.getPartyType(),
								fortuneThreePurchaseBean.getRefMerchantId(), userBean,
								fortuneThreePurchaseBean.getRefTransId());

						return fortuneThreePurchaseBean;
					}

				} else {
					fortuneThreePurchaseBean = (FortuneThreePurchaseBean) sRes.getResponseData();
					if (fortuneThreePurchaseBean.getSaleStatus() == null) {
						fortuneThreePurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean,
								fortuneThreePurchaseBean.getGame_no(), "FAILED", balDed);
						return fortuneThreePurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(fortuneThreePurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean,
								fortuneThreePurchaseBean.getGame_no(), "FAILED", balDed);
						return fortuneThreePurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean,
								fortuneThreePurchaseBean.getGame_no(), "FAILED", balDed);
						return fortuneThreePurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				fortuneThreePurchaseBean.setSaleStatus(status);
				return fortuneThreePurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return fortuneThreePurchaseBean;
	}

	public TreeMap<Integer, DrawWinningReportBean> getDrawGameData() {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_GAMEDATA);

		sReq.setServiceData(LMSFilterDispatcher.isMachineEnabled);

		logger.debug("2222222222222222 sd");
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);

		Type type = new TypeToken<TreeMap<Integer, DrawWinningReportBean>>() {
		}.getType();

		return (TreeMap<Integer, DrawWinningReportBean>) new Gson().fromJson((JsonElement) sRes.getResponseData(),
				type);

		// return (TreeMap) sRes.getResponseData();
	}

	public int getGamenoFromTktnumber(String tktNum) {

		int retIdLen = 0;
		int gameNo = 0;
		int tktLen = 0;
		String tktBuf = null;

		if (tktNum != null && (tktNum.length() == ConfigurationVariables.tktLenA
				|| tktNum.length() == ConfigurationVariables.tktLenB)) {
			if (tktNum.length() == ConfigurationVariables.tktLenA) {
				tktLen = ConfigurationVariables.tktLenA;
				retIdLen = ConfigurationVariables.retIdLenA;
				tktBuf = tktNum.substring(retIdLen, retIdLen + ConfigurationVariables.gameNoLenA);
			} else if (tktNum.length() == ConfigurationVariables.tktLenB) {
				tktLen = ConfigurationVariables.tktLenB;
				retIdLen = ConfigurationVariables.retIdLenB;
				tktBuf = tktNum.substring(retIdLen, retIdLen + ConfigurationVariables.gameNoLenB);
			}
			gameNo = Integer.parseInt(tktBuf);
		}
		return gameNo;

	}

	public String getLastSoldTicketNO(UserInfoBean userInfoBean, String interfaceType) {
		String ticketNum = null;
		String gameId = null;
		Connection con = DBConnect.getConnection();
		String gameNoQry = "select srtm.transaction_id,sdgm.game_id,interface from st_lms_retailer_transaction_master srtm,st_dg_game_master sdgm,st_lms_transaction_master tm where retailer_org_id="
				+ userInfoBean.getUserOrgId()
				+ " and srtm.game_id=sdgm.game_id and srtm.transaction_type like 'DG_%' and tm.transaction_id=srtm.transaction_id order by transaction_id desc limit 1";

		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(gameNoQry);
			long txId = 0;
			String purchInterFace = "";
			while (rs.next()) {
				gameId = rs.getString("game_id");
				txId = rs.getLong("transaction_id");
				purchInterFace = rs.getString("interface");
			}
			if (!interfaceType.equalsIgnoreCase(purchInterFace)) {
				return null;
			}

			// need to get all data from this here
			rs = stm.executeQuery(
					"select ticket_nbr,mrp_amt from st_dg_ret_sale_" + gameId + " where transaction_id=" + txId);
			while (rs.next()) {
				ticketNum = rs.getString("ticket_nbr");
				double mrpAmount = 0.0;
				mrpAmount = rs.getDouble("mrp_amt");
				if (mrpAmount > 0) {
					// it is sale ticket
				} else {
					// it is promo ticket go to the mapping table to get the
					// sale ticket
					rs = stm.executeQuery(
							"select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr="
									+ ticketNum);
					if (rs.next()) {
						ticketNum = rs.getString("sale_ticket_nbr");
					} else
						ticketNum = null;
				}

			}
			logger.debug("ticket no is :::::::::" + ticketNum);
			if (ticketNum == null) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBConnect.closeCon(con);
		}

		return ticketNum;
	}

	public static String getRaffleTktTypeFromgameNbr(int gameId, Connection con) {
		Statement stm;
		try {
			stm = con.createStatement();
			ResultSet rs = stm.executeQuery("select raffle_ticket_type from st_dg_game_master where game_id=" + gameId);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public MainPWTDrawBean getTotalPWTTkeAmt(MainPWTDrawBean mainPwtBean, String highPrizeCriteria, String highPrizeAmt,
			Connection connection, UserInfoBean userInfoBean) throws LMSException {
		// here see the limits after adding both for draw and promo

		double totalticketAmt = 0.0;
		List<PWTDrawBean> winningBeanList = mainPwtBean.getWinningBeanList();

		for (int i = 0; i < winningBeanList.size(); i++) {
			PWTDrawBean pwtBean = winningBeanList.get(i);
			totalticketAmt = totalticketAmt + pwtBean.getTotalAmount();
		}

		// get the retailer PWT Limits
		CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
		OrgPwtLimitBean orgPwtLimit = null;
		try {
			orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(), connection);
			if (orgPwtLimit == null) { // send mail to
				// backoffice
				throw new LMSException("PWT Limits Are Not defined Properly!!");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (totalticketAmt > 0) {
			if (totalticketAmt <= orgPwtLimit.getVerificationLimit()) {
				logger.debug("inside verification limit == ");
				boolean isHighPrizeFlag = highPrizeCriteria.equals("amt")
						&& totalticketAmt > Double.parseDouble(highPrizeAmt);
				// check for HIGH Prize Or is Approval Required

				if (totalticketAmt < orgPwtLimit.getMinClaimPerTicket()
						|| totalticketAmt > orgPwtLimit.getMaxClaimPerTicket()) {
					mainPwtBean.setStatus("OUT_ASSIGNED_LIMITS");
				} else if (totalticketAmt > orgPwtLimit.getApprovalLimit() || isHighPrizeFlag) {
					mainPwtBean.setStatus("HIGH_PRIZE");

				} else if (totalticketAmt <= orgPwtLimit.getPayLimit()) {
					mainPwtBean.setStatus("NORMAL_PAY");

				} else {
					mainPwtBean.setStatus("OUT_PAY_LIMIT");
				}
			} else {
				mainPwtBean.setStatus("OUT_VERIFY_LIMIT");

			}
		}
		mainPwtBean.setTotlticketAmount(totalticketAmt);
		return mainPwtBean;
	}

	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}

	public KenoPurchaseBean kenoPurchaseTicket(UserInfoBean userBean, KenoPurchaseBean kenoPurchaseBean)
			throws LMSException {
		kenoPurchaseBean.setSaleStatus("FAILED");
		Connection con = null;
		long balDed = 0;
		String status = "";
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		// ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.KENO_MGMT);
		sReq.setServiceMethod(ServiceMethodName.KENO_PURCHASE_TICKET);
		// sReq.setServiceData(kenoPurchaseBean);
		KenoRequestBean kenoRequestBean = new KenoRequestBean();
		sReq.setServiceData(kenoRequestBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		try {
			if (checkDrawAvailability(kenoPurchaseBean.getGameId())) {
				kenoPurchaseBean.setSaleStatus("NO_DRAWS");
				return kenoPurchaseBean;
			}
			// needs to be optimized in case of multiple validations
			if (!kenoValidateData(kenoPurchaseBean)) {
				// logger.debug("Data Validation returned false");
				return kenoPurchaseBean;
			}

			double totPurAmt = 0.0;
			int noOfPanel = kenoPurchaseBean.getNoOfPanel();
			String playTypeArr[] = kenoPurchaseBean.getPlayType();
			String[] noPickStr = kenoPurchaseBean.getNoPicked();
			int noOfLines[] = new int[noOfPanel];
			int betAmtMulArr[] = kenoPurchaseBean.getBetAmountMultiple();
			double unitPriceArr[] = new double[noOfPanel];
			for (int i = 0; i < noOfPanel; i++) {
				String playType = playTypeArr[i];
				String[] noPick = noPickStr[i].split(",");
				int[] n = new int[noPick.length];
				for (int j = 0; j < noPick.length; j++) {
					n[j] = Integer.parseInt(noPick[j]);
				}
				if (playType.contains("Direct")) {
					noOfLines[i] = 1;
				} else if (playType.equalsIgnoreCase("perm2")) {
					noOfLines[i] = n[0] * (n[0] - 1) / 2;
				} else if (playType.equalsIgnoreCase("perm3")) {
					noOfLines[i] = n[0] * (n[0] - 1) * (n[0] - 2) / 6;
				} else if (playType.equalsIgnoreCase("Banker1AgainstAll")) {
					noOfLines[i] = 89;
				} else if (playType.equalsIgnoreCase("banker")) {
					noOfLines[i] = n[0] * n[1];
				}

				unitPriceArr[i] = Util.getUnitPrice(kenoPurchaseBean.getGameId(), playTypeArr[i]);
				// logger.debug("----unitPrice--" + unitPriceArr[i]);
				// logger.debug("----playTypeArr--" + playTypeArr[i]);
				totPurAmt += noOfLines[i] * unitPriceArr[i] * kenoPurchaseBean.getNoOfDraws() * betAmtMulArr[i];
			}

			kenoPurchaseBean.setUnitPrice(unitPriceArr);

			kenoPurchaseBean.setNoOfLines(noOfLines);

			// logger.debug("Total Purchase Amount:" + totPurAmt);

			if (totPurAmt <= 0) {
				kenoPurchaseBean.setSaleStatus("FAILED");// Error Draw
				return kenoPurchaseBean;
			}
			kenoPurchaseBean.setTotalPurchaseAmt(totPurAmt);

			con = getConnectionObject();

			int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
			checkLastPrintedTicketStatusAndUpdate(userBean, Long.parseLong(kenoPurchaseBean.getLastSoldTicketNo()),
					kenoPurchaseBean.getDeviceType(), kenoPurchaseBean.getRefMerchantId(), autoCancelHoldDays,
					kenoPurchaseBean.getActionName(), kenoPurchaseBean.getLastGameId(), con);

			// Add condition in single if method transfer in this method
			// checkLastPrintedTicketStatusAndUpdate in DrawgameRPOSHelper.java
			/*
			 * if(kenoPurchaseBean.getLastSoldTicketNo()!=null ){
			 * if(!"0".equalsIgnoreCase(kenoPurchaseBean.getLastSoldTicketNo()))
			 * { //If below insertion throw an error or no row updated then stop
			 * sale
			 * Util.insertLastSoldTicketTeminal(kenoPurchaseBean.getUserId(),
			 * kenoPurchaseBean.getLastSoldTicketNo(),kenoPurchaseBean.getGameId
			 * (),con); } }
			 */

			// rg calling
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "", con);
			if (!isFraud) {
				balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, kenoPurchaseBean.getGameId(),
						kenoPurchaseBean.getTotalPurchaseAmt(), kenoPurchaseBean.getPlrMobileNumber(), con);
				// check valDed value for >0 else return error
				oldTotalPurchaseAmt = kenoPurchaseBean.getTotalPurchaseAmt();
				/*
				 * logger
				 * .debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
				 * + oldTotalPurchaseAmt);
				 */
				if (balDed > 0) {
					// logger.debug("in keno if----------------");
					kenoPurchaseBean.setRefTransId(balDed + "");
					con.commit();
				} else {

					status = getStatusIfErrorInTransaction(status, balDed);
					kenoPurchaseBean.setSaleStatus(status);
					return kenoPurchaseBean;
				}

			} else {
				logger.debug("Responsing Gaming not allowed to sale");
				kenoPurchaseBean.setSaleStatus("FRAUD");
				return kenoPurchaseBean;
			}

		} catch (SQLException se) {

			se.printStackTrace();
			throw new LMSException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(con);
		}

		try {
			kenoRequestBean.setBetAmountMultiple(kenoPurchaseBean.getBetAmountMultiple());
			kenoRequestBean.setDrawIdTableMap(kenoPurchaseBean.getDrawIdTableMap());
			kenoRequestBean.setDrawDateTime(kenoPurchaseBean.getDrawDateTime());
			kenoRequestBean.setGame_no(kenoPurchaseBean.getGame_no());
			kenoRequestBean.setGameId(kenoPurchaseBean.getGameId());
			kenoRequestBean.setIsAdvancedPlay(kenoPurchaseBean.getIsAdvancedPlay());
			kenoRequestBean.setIsQuickPick(kenoPurchaseBean.getIsQuickPick());
			kenoRequestBean.setNoOfDraws(kenoPurchaseBean.getNoOfDraws());
			kenoRequestBean.setNoPicked(kenoPurchaseBean.getNoPicked());
			kenoRequestBean.setPartyId(kenoPurchaseBean.getPartyId());
			kenoRequestBean.setPartyType(kenoPurchaseBean.getPartyType());
			kenoRequestBean.setPlayerData(kenoPurchaseBean.getPlayerData());
			kenoRequestBean.setPlayType(kenoPurchaseBean.getPlayType());
			kenoRequestBean.setPurchaseChannel(kenoPurchaseBean.getPurchaseChannel());
			kenoRequestBean.setRefMerchantId(kenoPurchaseBean.getRefMerchantId());
			kenoRequestBean.setRefTransId(kenoPurchaseBean.getRefTransId());
			kenoRequestBean.setUserId(kenoPurchaseBean.getUserId());
			kenoRequestBean.setUserMappingId(kenoPurchaseBean.getUserMappingId());
			kenoRequestBean.setServiceId(kenoPurchaseBean.getServiceId());
			kenoRequestBean.setPromotkt(kenoPurchaseBean.isPromotkt());
			kenoRequestBean.setUnitPrice(kenoPurchaseBean.getUnitPrice());
			kenoRequestBean.setTotalPurchaseAmt(kenoPurchaseBean.getTotalPurchaseAmt());

			/*
			 * sRes = delegate.getResponse(sReq); String responseString =
			 * sRes.getResponseData().toString(); Type elementType = new
			 * TypeToken<KenoPurchaseBean>(){}.getType();
			 */
			String responseString = delegate.getResponseString(sReq);
			KenoResponseBean kenoResponseBean = new Gson().fromJson(responseString, KenoResponseBean.class);

			if (kenoResponseBean.getIsSuccess()) {
				setKenoTwoDGEResponseData(kenoPurchaseBean, kenoResponseBean);

				// kenoPurchaseBean = new Gson().fromJson(responseString,
				// elementType);
				modifiedTotalPurchaseAmt = kenoPurchaseBean.getTotalPurchaseAmt();
				con = getConnectionObject();
				if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
							kenoPurchaseBean.getGameId(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed, con);

				}

				int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
						kenoPurchaseBean.getTicket_no(), kenoPurchaseBean.getGameId(), userBean,
						kenoPurchaseBean.getPurchaseChannel(), con);

				if (tickUpd == 1) {
					AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
					ajxHelper.getAvlblCreditAmt(userBean, con);
					// kenoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
					// kenoPurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
					kenoPurchaseBean
							.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), kenoPurchaseBean.getGameId()));

					status = "SUCCESS";
					kenoPurchaseBean.setSaleStatus(status);
					if (!"applet".equals(kenoPurchaseBean.getBarcodeType())) {
						IDBarcode.getBarcode(kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount());
					}
					con.commit();

					return kenoPurchaseBean;
				} else {
					// con.rollback();
					// DBConnect.closeCon(con);

					status = "FAILED";
					kenoPurchaseBean.setSaleStatus(status);
					// Code for cancelling the Ticket to be send to Draw
					// Game Engine
					cancelTicket(kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount(),
							kenoPurchaseBean.getPurchaseChannel(), kenoPurchaseBean.getDrawIdTableMap(),
							kenoPurchaseBean.getGame_no(), kenoPurchaseBean.getPartyId(),
							kenoPurchaseBean.getPartyType(), kenoPurchaseBean.getRefMerchantId(), userBean,
							kenoPurchaseBean.getRefTransId());

					return kenoPurchaseBean;
				}
			} else {
				// kenoPurchaseBean = new Gson().fromJson(responseString,
				// elementType);
				if (kenoPurchaseBean.getSaleStatus() == null) {
					kenoPurchaseBean.setSaleStatus("FAILED");// Error
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED",
							balDed);
					return kenoPurchaseBean;
				} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(kenoPurchaseBean.getSaleStatus())) {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED",
							balDed);
					return kenoPurchaseBean;
				} else {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED",
							balDed);
					return kenoPurchaseBean;
				}
			}
		} catch (Exception se) {
			se.printStackTrace();
			if (kenoPurchaseBean.getSaleStatus() == null) {
				kenoPurchaseBean.setSaleStatus("FAILED");// Error
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED",
						balDed);
			} else {
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED",
						balDed);
			}
		} finally {
			DBConnect.closeCon(con);
		}
		return kenoPurchaseBean;
	}

	public boolean kenoValidateData(KenoPurchaseBean kenoPurchaseBean) {// this
																		// method
																		// needs
																		// to be
																		// updated
																		// later
		if (kenoPurchaseBean != null) {
			if (kenoPurchaseBean.getNoOfDraws() < 1) {
				logger.debug("insufficient no of draws");
				return false;
			}
			if (kenoPurchaseBean.getNoOfPanel() < 1) {
				logger.debug("insufficient no of panels");
				return false;
			}

			// Kenya project is not in operation
			// boolean isKenya =
			// "Kenya".equalsIgnoreCase((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED"));
			int noOfPanel = kenoPurchaseBean.getNoOfPanel();
			String playTypeArr[] = kenoPurchaseBean.getPlayType();
			// String[] noPickStr = kenoPurchaseBean.getNoPicked();
			// String[] pickedNumbers = kenoPurchaseBean.getPlayerData();
			int[] qp = kenoPurchaseBean.getIsQuickPick();
			/*
			 * for (int i = 0; i < noOfPanel; i++) { String playType =
			 * playTypeArr[i]; String[] noPick = noPickStr[i].split(","); int[]
			 * n = new int[noPick.length]; for (int j = 0; j < noPick.length;
			 * j++) { n[j] = Integer.parseInt(noPick[j]); }
			 * if(!"QP".equalsIgnoreCase(pickedNumbers[i])){ if
			 * (playType.contains("Direct1")) { if(n[0]!=1 || qp[i]!=2 ||
			 * pickedNumbers[i].split(",").length !=1 ){ return false; } } else
			 * if (playType.equalsIgnoreCase("Direct2")) { if(n[0]!=2 ||
			 * qp[i]!=2 || pickedNumbers[i].split(",").length !=2 ){ return
			 * false; } }else if (playType.equalsIgnoreCase("Direct3")) {
			 * if(n[0]!=3 || qp[i]!=2 || pickedNumbers[i].split(",").length !=3
			 * ){ return false; } } else if
			 * (playType.equalsIgnoreCase("Direct4")) { if(n[0]!=4 || qp[i]!=2
			 * || pickedNumbers[i].split(",").length !=4 ){ return false; }
			 * 
			 * } else if (playType.equalsIgnoreCase("Direct5")) { if(n[0]!=5 ||
			 * qp[i]!=2 || pickedNumbers[i].split(",").length !=5 ){ return
			 * false; }
			 * 
			 * } else if (playType.equalsIgnoreCase("Perm2")) { //if(!isKenya){
			 * if(n[0]<3 || qp[i]!=2 || pickedNumbers[i].split(",").length <3 ){
			 * return false; } //} } else if
			 * (playType.equalsIgnoreCase("Perm3")) { if(n[0]<4 || qp[i]!=2 ||
			 * pickedNumbers[i].split(",").length <4 ){ return false; }
			 * 
			 * } }else{ if (playType.contains("Direct1")) { if(n[0]!=1 ||
			 * qp[i]!=1){ return false; } } else if
			 * (playType.equalsIgnoreCase("Direct2")) { if(n[0]!=2 || qp[i]!=1
			 * ){ return false; } }else if
			 * (playType.equalsIgnoreCase("Direct3")) { if(n[0]!=3 || qp[i]!=1
			 * ){ return false; } } else if
			 * (playType.equalsIgnoreCase("Direct4")) { if(n[0]!=4 || qp[i]!=1
			 * ){ return false; }
			 * 
			 * } else if (playType.equalsIgnoreCase("Direct5")) { if(n[0]!=5 ||
			 * qp[i]!=1 ){ return false; }
			 * 
			 * } else if (playType.equalsIgnoreCase("Perm2")) { if(n[0]<3 ||
			 * qp[i]!=1 ){ return false; }
			 * 
			 * } else if (playType.equalsIgnoreCase("Perm3")) { if(n[0]<4 ||
			 * qp[i]!=1 ){ return false; }
			 * 
			 * } }
			 * 
			 * 
			 * }
			 */

			boolean isValid = true;
			String[] pickedNumbersArr = kenoPurchaseBean.getPlayerData();
			// int noOfPanel = pickedNumbersArr.length;
			String[] noPickedArr = kenoPurchaseBean.getNoPicked();
			for (int i = 0; i < noOfPanel; i++) {

				String playerData = pickedNumbersArr[i];

				if (!"QP".equals(playerData)) {
					if (playTypeArr[i].contains("Direct") || "Banker1AgainstAll".equals(playTypeArr[i])) {
						int pickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i]);
						if (qp[i] != 2 || playerData.split(",").length != pickValue
								|| Integer.parseInt(noPickedArr[i]) != pickValue) {
							isValid = false;
							break;
						}
						/*
						 * isValid = noPickedArr[i].equals(pickValue);
						 * logger.debug("-Direct---" + playTypeArr[i] + "---" +
						 * noPickedArr[i]); if (!isValid) { break; }
						 */
					} else if (playTypeArr[i].contains("Perm")) {
						int minPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "MIN");
						int maxPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "MAX");
						int selPick = Integer.parseInt(noPickedArr[i]);
						logger.debug("-Perm---" + playTypeArr[i] + "---" + noPickedArr[i]);
						if (qp[i] != 2 || minPickValue > playerData.split(",").length
								|| maxPickValue < playerData.split(",").length || minPickValue > selPick
								|| maxPickValue < selPick) {
							isValid = false;
							break;
						}
					} else if ("Banker".equals(playTypeArr[i])) {
						logger.debug("-Banker---" + playTypeArr[i] + "---" + noPickedArr[i]);

						int minULPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "ULMIN");
						int maxULPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "ULMAX");
						int minBLPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "BLMIN");
						int maxBLPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "BLMAX");
						String selPick[] = noPickedArr[i].split(",");
						int selUL = Integer.parseInt(selPick[0]);
						int selBL = Integer.parseInt(selPick[1]);

						int pickedUL = playerData.substring(0, playerData.indexOf(",UL,")).split(",").length;
						int pickedBL = playerData.substring(playerData.indexOf(",UL,") + 4, playerData.indexOf(",BL"))
								.split(",").length;

						// for upper line & below line
						if (qp[i] != 2 || minULPickValue > pickedUL || maxULPickValue < pickedUL
								|| minBLPickValue > pickedBL || maxBLPickValue < pickedBL || minULPickValue > selUL
								|| maxULPickValue < selUL || minBLPickValue > selBL || maxBLPickValue < selBL) {
							isValid = false;
							break;
						}
					}
					isValid = Util.validateNumber(KenoConstants.START_RANGE, KenoConstants.END_RANGE,
							playerData.replace(",UL,", ",").replace(",BL", ""), false);
					logger.debug("-Data---" + playTypeArr[i] + "---" + noPickedArr[i] + "---" + playerData);
					if (!isValid) {
						break;
					}
				} else {
					if (playTypeArr[i].contains("Direct") || "Banker1AgainstAll".equals(playTypeArr[i])) {
						int pickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i]);
						if (qp[i] != 1 || Integer.parseInt(noPickedArr[i]) != pickValue) {
							isValid = false;
							break;
						}
						/*
						 * isValid = noPickedArr[i].equals(pickValue);
						 * logger.debug("-Direct---" + playTypeArr[i] + "---" +
						 * noPickedArr[i]); if (!isValid) { break; }
						 */
					} else if (playTypeArr[i].contains("Perm")) {
						int minPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "MIN");
						int maxPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "MAX");
						int selPick = Integer.parseInt(noPickedArr[i]);
						logger.debug("-Perm---" + playTypeArr[i] + "---" + noPickedArr[i]);
						if (qp[i] != 1 || minPickValue > selPick || maxPickValue < selPick) {
							isValid = false;
							break;
						}
					} else if ("Banker".equals(playTypeArr[i])) {
						logger.debug("-Banker---" + playTypeArr[i] + "---" + noPickedArr[i]);

						int minULPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "ULMIN");
						int maxULPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "ULMAX");
						int minBLPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "BLMIN");
						int maxBLPickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i] + "BLMAX");
						String selPick[] = noPickedArr[i].split(",");
						int selUL = Integer.parseInt(selPick[0]);
						int selBL = Integer.parseInt(selPick[1]);

						// for upper line & below line
						if (qp[i] != 1 || minULPickValue > selUL || maxULPickValue < selUL || minBLPickValue > selBL
								|| maxBLPickValue < selBL) {
							isValid = false;
							break;
						}
					}
				}
			}

			if (!isValid) {
				kenoPurchaseBean.setSaleStatus("INVALID_INPUT");// Error Draw
				// setKenoPurchaseBean(kenoPurchaseBean);
				logger.error("-----------Keno Validation Error-------------------" + kenoPurchaseBean.getSaleStatus());
				return false;
			}

		} else {
			logger.debug("keno bean null");
			return false;
		}
		return true;
		/*
		 * if (kenoPurchaseBean != null) { int noOfDraws =
		 * kenoPurchaseBean.getNoOfDraws(); if (noOfDraws < 1) {
		 * logger.debug("insufficient no of draws"); }
		 * 
		 * String pickedNumbers = kenoPurchaseBean.getPlayerData(); if
		 * (pickedNumbers == null) { logger.debug("invalid data"); } else {
		 * List<String> pickData = Arrays.asList(pickedNumbers.split(","));
		 * String playType = kenoPurchaseBean.getPlayType();
		 * 
		 * if (playType.equalsIgnoreCase("Banker")) { int[] noPicked =
		 * kenoPurchaseBean.getNoPicked(); String checkNum[] =
		 * KenoConstants.BET_TYPE_MAP .get(playType).split(",");
		 * 
		 * if ("QP".equalsIgnoreCase(pickData.get(0))) { logger.debug("---In
		 * QP---"); if (!(noPicked[0] >= Integer.parseInt(checkNum[0]) &&
		 * noPicked[0] <= Integer.parseInt(checkNum[1]) && noPicked[1] >=
		 * Integer.parseInt(checkNum[2]) && noPicked[1] <= Integer
		 * .parseInt(checkNum[3]))) { logger.debug("invalid UL data"); }
		 * kenoPurchaseBean.setNoOfLines(noPicked[0]*noPicked[1]); }else {
		 * String[] noPick = pickedNumbers.replace(",BL", "") .split(",UL,");
		 * String[] noPickedUL = noPick[0].split(","); if (noPickedUL.length <
		 * Integer.parseInt(checkNum[0]) || noPickedUL.length > Integer
		 * .parseInt(checkNum[1])) { logger.debug("invalid UL data"); }
		 * SortedSet<Integer> set = new TreeSet<Integer>(); for (int i = 0; i <
		 * noPickedUL.length; i++) { set.add(Integer.parseInt(noPickedUL[i])); }
		 * if (set.size() < noPickedUL.length) { logger.debug("duplicate UL
		 * data"); } if (set.first() < 1 || set.last() > 90) { logger.debug("UL
		 * data out of range."); }
		 * 
		 * String[] noPickedBL = noPick[1].split(","); if (noPickedBL.length <
		 * Integer.parseInt(checkNum[2]) || noPickedBL.length > Integer
		 * .parseInt(checkNum[3])) { logger.debug("invalid BL data"); }
		 * SortedSet<Integer> sortedSetBL = new TreeSet<Integer>(); for (int i =
		 * 0; i < noPickedBL.length; i++) {
		 * sortedSetBL.add(Integer.parseInt(noPickedBL[i])); } if
		 * (sortedSetBL.size() < noPickedBL.length) { logger.debug("duplicate BL
		 * data"); } if (sortedSetBL.first() < 1 || sortedSetBL.last() > 90) {
		 * logger.debug("BL data out of range."); }
		 * 
		 * TreeSet treeSet = new TreeSet(); treeSet.add(set);
		 * treeSet.add(sortedSetBL); if (treeSet.size() < set.size() +
		 * sortedSetBL.size()) { logger.debug("Both UL and BL can't contain the
		 * same values."); }
		 * 
		 * int noOfLines = noPickedUL.length * noPickedBL.length;
		 * kenoPurchaseBean.setNoOfLines(noOfLines); } } else if
		 * (playType.contains("Direct") ||
		 * playType.equalsIgnoreCase("Banker1AgainstAll")) { int checkNum =
		 * Integer.parseInt(KenoConstants.BET_TYPE_MAP .get(playType)); if
		 * ("QP".equalsIgnoreCase(pickData.get(0))) { int[] noPicked =
		 * kenoPurchaseBean.getNoPicked(); if (noPicked[0] != checkNum) {
		 * logger.debug("invalid data"); } } else { String[] pickedNum =
		 * pickedNumbers.split(","); if (pickedNum.length != checkNum) {
		 * logger.debug("invalid ata"); } SortedSet<Integer> set = new
		 * TreeSet<Integer>(); for (int i = 0; i < pickedNum.length; i++) {
		 * set.add(Integer.parseInt(pickedNum[i])); } if (set.size() <
		 * pickedNum.length) { logger.debug("duplicate data"); } if (set.first()
		 * < 1 || set.last() > 90) { logger.debug("data out of range."); } } }
		 * else if (playType.contains("Perm")) { String checkNum[] =
		 * KenoConstants.BET_TYPE_MAP .get(playType).split(","); if
		 * ("QP".equalsIgnoreCase(pickData.get(0))) { int[] noPicked =
		 * kenoPurchaseBean.getNoPicked(); if (!(noPicked[0] >=
		 * Integer.parseInt(checkNum[0]) && noPicked[0] <= Integer
		 * .parseInt(checkNum[1]))) { logger.debug("invalid data"); } } else {
		 * String[] pickedNum = pickedNumbers.split(","); if (pickedNum.length
		 * >= Integer.parseInt(checkNum[0]) || pickedNum.length <= Integer
		 * .parseInt(checkNum[1])) { logger.debug("invalid ata"); }
		 * SortedSet<Integer> set = new TreeSet<Integer>(); for (int i = 0; i <
		 * pickedNum.length; i++) { set.add(Integer.parseInt(pickedNum[i])); }
		 * if (set.size() < pickedNum.length) { logger.debug("duplicate data");
		 * } if (set.first() < 1 || set.last() > 90) {
		 * logger.debug("data out of range."); } } }
		 * 
		 * if (kenoPurchaseBean.getBetAmountMultiple() < 1) {
		 * logger.debug("invalid Bet Amount Multiple"); }
		 * 
		 * int noOfLines = 0; int[] noPicked = kenoPurchaseBean.getNoPicked();
		 * if (playType.contains("Direct")) { noOfLines = 1; } else if
		 * (playType.equalsIgnoreCase("perm2")) { noOfLines = (noPicked[0] *
		 * (noPicked[0] - 1)) / 2; } else if
		 * (playType.equalsIgnoreCase("perm3")) { noOfLines = (noPicked[0] *
		 * (noPicked[0] - 1) * (noPicked[0] - 2)) / 6; } else if
		 * (playType.equalsIgnoreCase("Banker1AgainstAll")) { noOfLines = 89; }
		 * kenoPurchaseBean.setNoOfLines(noOfLines);
		 * 
		 * double totalPurAmt = kenoPurchaseBean.getNoOfLines()
		 * Util.getUnitPrice(kenoPurchaseBean.getGame_no(),
		 * kenoPurchaseBean.getPlayType()) kenoPurchaseBean.getNoOfDraws()
		 * kenoPurchaseBean.getBetAmountMultiple(); if (totalPurAmt <= 0) {
		 * logger.debug("invalid purchase amount"); }
		 * kenoPurchaseBean.setTotalPurchaseAmt(totalPurAmt); } }
		 * 
		 */
	}

	public boolean lottoDataValidation(LottoPurchaseBean lottoPurchaseBean) {
		int noOfDraws = lottoPurchaseBean.getNoOfDraws();
		String[] picknumbers = lottoPurchaseBean.getPicknumbers();
		Set<Integer> picknumSet;
		String pickNum[];
		int noOfPanels = picknumbers.length;
		logger.debug("no of Panels: " + noOfPanels);
		if (noOfDraws > 0 && noOfPanels > 0) {
			for (int i = 0; i < noOfPanels; i++) {
				if (picknumbers[i].equalsIgnoreCase("QP")) {
					logger.debug("quick pick Selected");

				} else {
					logger.debug("not quick pick");
					logger.debug("Player picked Data:" + picknumbers[i]);
					pickNum = picknumbers[i].split(",");
					picknumSet = new HashSet<Integer>();
					for (String element : pickNum) {
						picknumSet.add(Integer.parseInt(element));
					}
					if (pickNum.length != picknumSet.size()
							|| pickNum.length > com.skilrock.lms.dge.gameconstants.LottoConstants.MAX_PLAYER_PICKED) {
						logger.debug("picNum.Length: " + pickNum.length + "Set length:  " + picknumSet.size());
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public LottoPurchaseBean lottoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.LOTTO);
		sReq.setServiceMethod(ServiceMethodName.LOTTO_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;
		if (isDrawAvailable(lottoPurchaseBean.getGame_no()) || chkFreezeTimeSale(lottoPurchaseBean.getGame_no())) {
			lottoPurchaseBean.setSaleStatus("NO_DRAWS");
			return lottoPurchaseBean;
		}
		try {
			if (lottoDataValidation(lottoPurchaseBean)) {
				logger.debug("Data Validation returns True");
				// lottoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
				// lottoPurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
				lottoPurchaseBean
						.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGame_no()));

				lottoPurchaseBean.setSaleStatus("FAILED");
				con = DBConnect.getConnection();
				if (lottoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(),
								lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getGameId(), con);
					}
				}

				double totPurAmt = lottoPurchaseBean.getPicknumbers().length
						* Util.getUnitPrice(lottoPurchaseBean.getGame_no(), null) * lottoPurchaseBean.getNoOfDraws();
				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);

				lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);

				logger.debug("Inside  FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTotalPurchaseAmt(),
							lottoPurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					lottoPurchaseBean.setSaleStatus("FRAUD");
				}
			} else {

				logger.debug("Data validation returns false");
				lottoPurchaseBean.setSaleStatus("FAILED");

			}
		} catch (Exception e) {
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
			 * String ticketNo = lottoPurchaseBean.getTicket_no(); String
			 * cancelChannel = lottoPurchaseBean.getPurchaseChannel();
			 * Map<Integer, Map<Integer, String>> drawIdTableMap =
			 * lottoPurchaseBean .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
			e.printStackTrace();
		}

		try {

			if (balDed > 0) {
				lottoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								lottoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGame_no(), userBean,
							lottoPurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						lottoPurchaseBean.setSaleStatus(status);
						if (!lottoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode
									.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
						}
						return lottoPurchaseBean;
					} else {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
								lottoPurchaseBean.getPurchaseChannel(), lottoPurchaseBean.getDrawIdTableMap(),
								lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
								lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
								lottoPurchaseBean.getRefTransId());

						return lottoPurchaseBean;
					}
				} else {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					if (lottoPurchaseBean.getSaleStatus() == null) {
						lottoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				lottoPurchaseBean.setSaleStatus(status);
				return lottoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lottoPurchaseBean;
	}

	public MainPWTDrawBean payPwtTicket(MainPWTDrawBean mainPwtBean, UserInfoBean userInfoBean,
			String highPrizeCriteria, String highPrizeAmt, String highPrizeScheme) throws Exception {
		List<Long> transIdList = new ArrayList<Long>();
		Connection connection = DBConnect.getConnection();
		boolean isResAwaited = false;
		double totPay = mainPwtBean.getTotlticketAmount();
		boolean isHighPrz = false;

		Type type = null;

		if ("HIGH_PRIZE".equalsIgnoreCase(mainPwtBean.getStatus())) {
			isHighPrz = true;
		}
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PWT_MGMT);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		try {
			type = new TypeToken<PWTDrawBean>() {
			}.getType();

			connection.setAutoCommit(false);

			/*
			 * String highPrizeCriteria = (String)
			 * LMSUtility.sc.getAttribute("HIGH_PRIZE_CRITERIA"); String
			 * highPrizeAmt = (String)
			 * LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT"); String
			 * highPrizeScheme = (String) LMSUtility.sc.getAttribute(
			 * "DRAW_GAME_HIGH_PRIZE_SCHEME");
			 */

			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
			if (highPrizeCriteria == null || highPrizeAmt == null || userInfoBean == null) {

				mainPwtBean.setStatus("ERROR");
				return mainPwtBean;
			}
			// rg calling
			boolean isFraudAmt = ResponsibleGaming.respGaming(userInfoBean, "DG_PWT",
					"" + mainPwtBean.getTotlticketAmount());
			boolean isFraudInvalid = ResponsibleGaming.respGaming(userInfoBean, "DG_PWT_INVALID", "" + 0);
			if (!(isFraudAmt || isFraudInvalid)) {

				if (mainPwtBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {
					PWTDrawBean winningBean = new PWTDrawBean();
					winningBean = mainPwtBean.getWinningBeanList().get(0);
					RetailerPwtProcessHelper pwtProcess = new RetailerPwtProcessHelper();
					pwtProcess.verifyDrawPwt(winningBean.getTicketNo(), winningBean.getGameId(), connection,
							userInfoBean, highPrizeCriteria, highPrizeAmt, winningBean, highPrizeScheme, true,
							winningBean.getPwtTicketType(), transIdList);

					RaffleDrawIdBean raffleDrawIdBean = (RaffleDrawIdBean) winningBean.getRaffleDrawIdBeanList().get(0);
					// Draw Game Updation of Ticket
					winningBean.setGameDispName(Util.getGameDisplayName(winningBean.getGameId()));

					if (highPrizeScheme.equalsIgnoreCase("DRAW_WISE")) {

						logger.debug(">>>>>>>>>>>>>>DRAW_WISE in rpos");
						if (winningBean.getDrawWinList() != null) {
							for (DrawIdBean drawIdBean : winningBean.getDrawWinList()) {

								if (drawIdBean.getStatus().equalsIgnoreCase("NORMAL_PAY")) {
									winningBean.setStatus("NORMAL_PAY");
									break;
								} else {
									winningBean.setStatus("SUCCESS");
								}
							}
						}

					} else if (highPrizeScheme.equalsIgnoreCase("PANEL_WISE")) {
						logger.debug(">>>>>>>>>>>>>>>>>>>>PANEL_WISE in rpos");
						winningBean.setStatus("NORMAL_PAY");
					} else if (highPrizeScheme.equalsIgnoreCase("TICKET_WISE")) {
						logger.debug(">>>>>>>>>>>>>>>>>>>>TICKET_WISE in rpos");
						if ("NORMAL_PAY".equalsIgnoreCase(raffleDrawIdBean.getPwtStatus())) {
							winningBean.setStatus("NORMAL_PAY");
						} else {
							winningBean.setStatus("SUCCESS");
						}
					}

					if (raffleDrawIdBean.getStatus().equalsIgnoreCase("NORMAL_PAY")) {
						// send request for raffle pyment
						sReq.setServiceName(ServiceName.PWT_MGMT);
						sReq.setServiceMethod(ServiceMethodName.RAFFLE_PWT_UPDATE);
						sReq.setServiceData(winningBean);
						sRes = delegate.getResponse(sReq);

						winningBean = (PWTDrawBean) new Gson().fromJson((JsonElement) sRes.getResponseData(), type);

						// winningBean = (PWTDrawBean) sRes.getResponseData();
						if (sRes.getIsSuccess()) {
							// connection.commit();
							// connection.close();
							if (winningBean.isResAwaited()) {
								isResAwaited = true;
							}
							winningBean.setStatus("SUCCESS");
							mainPwtBean.setStatus("SUCCESS");
						}

						else {
							// connection.rollback();
							// connection.close();
							winningBean.setStatus("ERROR");
							mainPwtBean.setStatus("ERROR");
						}
					}

				} else {

					for (int i = 0; i < mainPwtBean.getWinningBeanList().size(); i++) {
						PWTDrawBean winningBean = new PWTDrawBean();
						winningBean = mainPwtBean.getWinningBeanList().get(i);

						mainPwtBean.setAdvMsg(Util.getAdvMessage(userInfoBean.getUserOrgId(), winningBean.getGameId(),
								"PLAYER", "PWT", "DG"));

						if (winningBean.getPwtTicketType().equalsIgnoreCase("RAFFLE")) {

							RetailerPwtProcessHelper pwtProcess = new RetailerPwtProcessHelper();
							pwtProcess.verifyDrawPwt(winningBean.getTicketNo(), winningBean.getGameId(), connection,
									userInfoBean, highPrizeCriteria, highPrizeAmt, winningBean, highPrizeScheme, true,
									winningBean.getPwtTicketType(), transIdList);

							RaffleDrawIdBean raffleDrawIdBean = (RaffleDrawIdBean) winningBean.getRaffleDrawIdBeanList()
									.get(0);
							// Draw Game Updation of Ticket
							winningBean.setGameDispName(Util.getGameDisplayName(winningBean.getGameId()));

							if (highPrizeScheme.equalsIgnoreCase("DRAW_WISE")) {

								logger.debug(">>>>>>>>>>>>>>DRAW_WISE in rpos");
								if (winningBean.getDrawWinList() != null) {
									for (DrawIdBean drawIdBean : winningBean.getDrawWinList()) {

										if (drawIdBean.getStatus().equalsIgnoreCase("NORMAL_PAY")) {
											winningBean.setStatus("NORMAL_PAY");
											break;
										} else {
											winningBean.setStatus("SUCCESS");
										}
									}
								}

							} else if (highPrizeScheme.equalsIgnoreCase("PANEL_WISE")) {
								logger.debug(">>>>>>>>>>>>>>>>>>>>PANEL_WISE in rpos");
								winningBean.setStatus("NORMAL_PAY");
							} else if (highPrizeScheme.equalsIgnoreCase("TICKET_WISE")) {
								logger.debug(">>>>>>>>>>>>>>>>>>>>TICKET_WISE in rpos");
								if ("NORMAL_PAY".equalsIgnoreCase(raffleDrawIdBean.getPwtStatus())) {
									winningBean.setStatus("NORMAL_PAY");
								} else {
									winningBean.setStatus("SUCCESS");
								}
							}
							if (raffleDrawIdBean.isResAwaited()) {
								isResAwaited = true;
							}

							if (raffleDrawIdBean.getStatus().equalsIgnoreCase("NORMAL_PAY")) {
								// send request for raffle pyment
								sReq.setServiceName(ServiceName.PWT_MGMT);
								sReq.setServiceMethod(ServiceMethodName.RAFFLE_PWT_UPDATE);
								sReq.setServiceData(winningBean);
								sRes = delegate.getResponse(sReq);

								winningBean = (PWTDrawBean) new Gson().fromJson((JsonElement) sRes.getResponseData(),
										type);
								// winningBean = (PWTDrawBean) sRes
								// .getResponseData();
								if (sRes.getIsSuccess()) {
									// connection.commit();
									// connection.close();
									if (winningBean.isResAwaited()) {
										isResAwaited = true;
									}
									winningBean.setStatus("SUCCESS");
									mainPwtBean.setStatus("SUCCESS");
								}

								else {
									// connection.rollback();
									// connection.close();
									winningBean.setStatus("ERROR");
									mainPwtBean.setStatus("ERROR");
									return mainPwtBean;
								}
							}

						} else if (winningBean.getPwtTicketType().equalsIgnoreCase("DRAW")) {
							boolean isPay = true; // For 12/24 Rank 4 Only
							if ("TwelveByTwentyFour".equals(Util.getGameName(mainPwtBean.getGameId()))) {
								for (DrawIdBean drawIdWinningBean : winningBean.getDrawWinList()) {
									if (drawIdWinningBean.getRankId() == 4) {
										winningBean.setStatus("NORMAL_PAY");
										isPay = false;
									}
								}
							}

							RetailerPwtProcessHelper pwtProcess = new RetailerPwtProcessHelper();
							if (isPay)
								pwtProcess.verifyDrawPwt(winningBean.getTicketNo(), winningBean.getGameId(), connection,
										userInfoBean, highPrizeCriteria, highPrizeAmt, winningBean, highPrizeScheme,
										true, winningBean.getPwtTicketType(), transIdList);
							else {
								pwtProcess.verifyDrawPwt(winningBean.getTicketNo(), winningBean.getGameId(), connection,
										userInfoBean, highPrizeCriteria, highPrizeAmt, winningBean, highPrizeScheme,
										false, winningBean.getPwtTicketType(), transIdList);
								winningBean.setStatus("NORMAL_PAY");
							}
							// Draw Game Updation of Ticket
							winningBean.setGameDispName(Util.getGameDisplayName(winningBean.getGameId()));

							if (highPrizeScheme.equalsIgnoreCase("DRAW_WISE")) {

								logger.debug(">>>>>>>>>>>>>>DRAW_WISE in rpos");
								if (winningBean.getDrawWinList() != null) {
									for (DrawIdBean drawIdBean : winningBean.getDrawWinList()) {

										if (drawIdBean.getStatus().equalsIgnoreCase("NORMAL_PAY")) {
											winningBean.setStatus("NORMAL_PAY");
											break;
										} else {
											winningBean.setStatus("SUCCESS");
										}
									}
								}

							} else if (highPrizeScheme.equalsIgnoreCase("PANEL_WISE")) {
								logger.debug(">>>>>>>>>>>>>>>>>>>>PANEL_WISE in rpos");
								winningBean.setStatus("NORMAL_PAY");
							} else if (highPrizeScheme.equalsIgnoreCase("TICKET_WISE")) {
								logger.debug(">>>>>>>>>>>>>>>>>>>>TICKET_WISE in rpos");
								if ("NORMAL_PAY".equalsIgnoreCase(winningBean.getPwtStatus())) {
									winningBean.setStatus("NORMAL_PAY");
								} else {
									winningBean.setStatus("SUCCESS");
								}
							}

							/*
							 * DrawIdBean drawBean =
							 * winningBean.getDrawWinList().get(i);
							 * List<PanelIdBean> panelWinList =
							 * drawBean.getPanelWinList(); if (panelWinList ==
							 * null){ isResAwaited=true; }
							 */
							if (winningBean.isResAwaited()) {
								isResAwaited = true;
							}

							if (winningBean.getStatus().equalsIgnoreCase("NORMAL_PAY")) {
								// Insert last pwt time in
								// st_lms_ret_offline_master table
								Util.setHeartBeatAndSaleTime(userInfoBean.getUserOrgId(), "PWT", connection);
								// send request for main draw gamw payment

								sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
								sReq.setServiceData(winningBean);
								sRes = delegate.getResponse(sReq);
								if (sRes.getIsSuccess()) {
									// connection.commit();
									// connection.close();
									if (winningBean.isResAwaited()) {
										isResAwaited = true;
									}
									mainPwtBean.setBarcodeCount(winningBean.getBarCodeCount());
									winningBean.setStatus("SUCCESS");
									mainPwtBean.setStatus("SUCCESS");

								}

								else {
									// connection.rollback();
									// connection.close();
									winningBean.setStatus("ERROR");
									mainPwtBean.setStatus("ERROR");
									return mainPwtBean;
								}

							}

						}

					}

				}

				// do here the reprint process and call reprint function from
				// LMS only
				if (isResAwaited || isHighPrz) {
					if (isResAwaited && totPay > 0 || isHighPrz && totPay > 0 || isResAwaited && isHighPrz) {
						String ticketNumber = mainPwtBean.getTicketNo();
						int len = ticketNumber.length();
						Object gameBean = reprintTicket(userInfoBean,
								true, /*
										 * len==ConfigurationVariables.
										 * barcodeCount?ticketNumber.substring(
										 * 0, len - 4):ticketNumber.substring(0,
										 * len - 2)
										 */
								Util.getTktWithoutRpcNBarCodeCount(ticketNumber, len), false, null, null);
						mainPwtBean.setPurchaseBean(gameBean);
						mainPwtBean.setReprint(true);
						mainPwtBean.setStatus("SUCCESS");
					} else {
						mainPwtBean.setStatus("ERROR");
					}
				}
				mainPwtBean.setTransactionIdList(transIdList);
				connection.commit();
				connection.close();

			} else {
				if (isFraudAmt) {
					mainPwtBean.setStatus("PWT_LIMIT_EXCEED");
					mainPwtBean.setPwtStatus("Your Daliy PWT Limit of Responsible Gaming Exceed");
				} else {
					mainPwtBean.setStatus("INVALID_PWT_LIMIT_EXCEED");
					mainPwtBean.setPwtStatus("Your Daliy Invalid PWT Limit of Responsible Gaming Exceed");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mainPwtBean.setStatus("ERROR");
		}
		return mainPwtBean;
	}

	public MainPWTDrawBean prizeWinningTicket(MainPWTDrawBean mainPwtBean, UserInfoBean userInfoBean,
			String refMerchantId) throws SQLException {

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connection = null;

		int gameNo;
		int gameId = 0;
		int barCodeCount = -1;
		String gameType;
		String highPrizeCriteria;
		String highPrizeAmt;
		String highPrizeScheme;
		boolean byPassDates = false;
		List<PWTDrawBean> winningBeanList = null;

		Type type = null;

		// Get The Actual Ticket Number
		String ticketNumber = Util.getTicketNumber(mainPwtBean.getTicketNo(), mainPwtBean.getInpType());
		if (ticketNumber.equals("ERROR") || "".equals(ticketNumber)) {
			mainPwtBean.setStatus("ERROR_INVALID");
			return mainPwtBean;
		}
		// get game No from ticket
		gameNo = getGamenoFromTktnumber(ticketNumber);
		gameId = Util.getGameIdFromGameNumber(gameNo);
		if (gameNo <= 0) {
			mainPwtBean.setStatus("ERROR_INVALID");
			return mainPwtBean;
		}
		// get game type from ticket type
		gameType = Util.getGameType(gameId);
		if (gameType == null) {
			mainPwtBean.setStatus("ERROR_INVALID");
			return mainPwtBean;
		}
		// Get The BarCode From Ticket Number If Required
		if (mainPwtBean.getInpType() == 1 || (mainPwtBean.getInpType() == 3
				&& mainPwtBean.getTicketNo().length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount)) {
			barCodeCount = Integer.parseInt(Util.getBarCodeCountFromTicketNumber(mainPwtBean.getTicketNo()));
		}
		mainPwtBean.setMainTktGameNo(gameNo);
		mainPwtBean.setGameId(gameId);
		connection = DBConnect.getConnection();
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		try {
			type = new TypeToken<PWTDrawBean>() {
			}.getType();

			/*
			 * if ("NO".equalsIgnoreCase((String)
			 * LMSUtility.sc.getAttribute("PWT_CLAIM_EVERYWHERE"))) { ps =
			 * connection
			 * .prepareStatement("select organization_id from st_lms_user_master where user_id="
			 * + Util.getUserIdFromTicket(ticketNumber)); rs =
			 * ps.executeQuery(); int orgId = 0; if (rs.next()) { orgId =
			 * rs.getInt(1); }
			 * 
			 * if (userInfoBean.getUserOrgId() != orgId) {
			 * mainPwtBean.setStatus("UN_AUTH"); return mainPwtBean; } }
			 */

			if (!Util.canClaimRetailer(ticketNumber, userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId(),
					connection)) {
				mainPwtBean.setStatus("UN_AUTH");
				return mainPwtBean;
			}
			mainPwtBean.setAdvMsg(Util.getAdvMessage(userInfoBean.getUserOrgId(), gameId, "PLAYER", "PWT", "DG"));
			connection.setAutoCommit(false);
			highPrizeCriteria = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_CRITERIA");
			highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
			highPrizeScheme = (String) LMSUtility.sc.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
			winningBeanList = new ArrayList<PWTDrawBean>();
			logger.debug("highPrizeAmt.........." + highPrizeAmt);
			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
			if (highPrizeCriteria == null || highPrizeAmt == null || userInfoBean == null) {
				mainPwtBean.setStatus("ERROR");
				return mainPwtBean;
			}

			if (gameType.equalsIgnoreCase("RAFFLE")) {
				mainPwtBean.setPwtTicketType("RAFFLE");
				PWTDrawBean winBean = new PWTDrawBean();
				winBean.setTicketNo(mainPwtBean.getTicketNo());
				winBean.setPartyId(userInfoBean.getUserOrgId());
				winBean.setPartyType(userInfoBean.getUserType());
				winBean.setUserId(userInfoBean.getUserId());
				winBean.setRefMerchantId(refMerchantId);
				winBean.setPwtTicketType("ORIGINAL");
				winBean.setGameId(gameId);
				winBean.setGameDispName(Util.getGameDisplayName(gameId));
				ServiceResponse sRes = new ServiceResponse();
				ServiceRequest sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.PWT_MGMT);
				sReq.setServiceMethod(ServiceMethodName.RAFFLE_PRZE_WINNING_TICKET);
				sReq.setServiceData(winBean);
				sRes = delegate.getResponse(sReq);

				winBean = (PWTDrawBean) new Gson().fromJson((JsonElement) sRes.getResponseData(), type);

				// winBean = (PWTDrawBean) sRes.getResponseData();

				List raffleDrawIdBeanList = winBean.getRaffleDrawIdBeanList();
				if (raffleDrawIdBeanList.size() < 1) {
					mainPwtBean.setStatus("ERROR");
					return mainPwtBean;
				}

				RaffleDrawIdBean raffleWinningBean = (RaffleDrawIdBean) raffleDrawIdBeanList.get(0);

				logger.debug(raffleWinningBean.isValid() + "************Test***********");
				if (sRes.getIsRaffleSuccess()) {
					// LMS
					if (raffleWinningBean.getStatus().equalsIgnoreCase("ERROR")) {
						raffleWinningBean.setStatus("ERROR");
						mainPwtBean.setStatus("ERROR");
						return mainPwtBean;
					} else if (raffleWinningBean.getStatus().equalsIgnoreCase("CANCELLED")) {
						raffleWinningBean.setStatus("CANCELLED");
						mainPwtBean.setStatus("CANCELLED");
						return mainPwtBean;
					}

					RetailerPwtProcessHelper pwtProcess = new RetailerPwtProcessHelper();
					pwtProcess.verifyDrawPwt(winBean.getTicketNo(), winBean.getGameNo(), connection, userInfoBean,
							highPrizeCriteria, highPrizeAmt, winBean, highPrizeScheme, false, "RAFFLE", null);
					// connection.commit();
					// connection.close();
					winBean.setStatus("SUCCESS");
					mainPwtBean.setStatus("SUCCESS");
					// winningBeanList.add(winBean);
					winningBeanList.add(winBean);
					mainPwtBean.setWinningBeanList(winningBeanList);

				} else {

					if ("TICKET_EXPIRED".equalsIgnoreCase(raffleWinningBean.getStatus())) {
						logger.debug("Ticket has been expired.");
						raffleWinningBean.setStatus("TICKET_EXPIRED");
						winBean.setStatus("TICKET_EXPIRED");
						mainPwtBean.setStatus("TICKET_EXPIRED");
						winningBeanList.add(winBean);
						mainPwtBean.setWinningBeanList(winningBeanList);
						return mainPwtBean;

					} else {
						logger.debug("inside invalid ticket ");
						boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "DG_PWT_INVALID", "1");
						logger.debug("*****is fraud***** " + isFraud);
						if (isFraud) {
							raffleWinningBean.setStatus("FRAUD");
							winBean.setStatus("FRAUD");
							mainPwtBean.setStatus("FRAUD");
							winningBeanList.add(winBean);
							mainPwtBean.setWinningBeanList(winningBeanList);
							return mainPwtBean;
						} else {
							raffleWinningBean.setValid(false);
							raffleWinningBean.setStatus("ERROR");
							winBean.setStatus("ERROR");
							mainPwtBean.setStatus("ERROR");
							winningBeanList.add(winBean);
							mainPwtBean.setWinningBeanList(winningBeanList);
							return mainPwtBean;
						}
					}
				}

			} else {
				mainPwtBean.setPwtTicketType("DRAW");
				ServiceResponse sRes = new ServiceResponse();
				ServiceRequest sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.PWT_MGMT);
				sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PRZE_WINNING_TICKET);
				PWTDrawBean winBean = new PWTDrawBean();
				winBean.setByPassDates(byPassDates);
				winBean.setTicketNo(ticketNumber);
				winBean.setBarCodeCount(barCodeCount);
				winBean.setPartyId(userInfoBean.getUserOrgId());
				winBean.setPartyType(userInfoBean.getUserType());
				winBean.setUserId(userInfoBean.getUserId());
				winBean.setRefMerchantId(refMerchantId);
				winBean.setGameId(gameId);
				winBean.setGameDispName(Util.getGameDisplayName(gameId));
				sReq.setServiceData(winBean);

				/*
				 * if(ResponsibleGaming.respGaming( userInfoBean,
				 * "DG_PWT_COUNT", "1")){ winBean.setStatus("FRAUD");
				 * mainPwtBean.setStatus("FRAUD"); winningBeanList.add(winBean);
				 * mainPwtBean.setWinningBeanList(winningBeanList); return
				 * mainPwtBean; }
				 */

				sRes = delegate.getResponse(sReq);

				winBean = (PWTDrawBean) new Gson().fromJson((JsonElement) sRes.getResponseData(), type);
				winBean.setErrorCode(sRes.getErrorCode());
				winBean.setErrorMessage(sRes.getErrorMessage());
				// winBean = (PWTDrawBean) sRes.getResponseData();

				if (sRes.getIsSuccess()) {
					if (Boolean.parseBoolean(
							(String) com.skilrock.lms.common.Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_PWT_AMT")))
						CommonMethods.doRoundingForPwtAmt(winBean);

					RetailerPwtProcessHelper pwtProcess = new RetailerPwtProcessHelper();
					pwtProcess.verifyDrawPwt(winBean.getTicketNo(), winBean.getGameId(), connection, userInfoBean,
							highPrizeCriteria, highPrizeAmt, winBean, highPrizeScheme, false,
							winBean.getPwtTicketType(), null);
					connection.commit();
					// connection.close();
					winBean.setStatus("SUCCESS");

				} else {
					if (winBean.getErrorCode() == 122) {
						winBean.setValid(false);
						winBean.setStatus("ALREADY_CANCELLED");
						mainPwtBean.setStatus("ALREADY_CANCELLED");
						winningBeanList.add(winBean);
						mainPwtBean.setWinningBeanList(winningBeanList);
						return mainPwtBean;
					}
					if (winBean.getErrorCode() == 3001) {
						winBean.setValid(false);
						winBean.setStatus("ERROR_INVALID");
						mainPwtBean.setStatus("ERROR_INVALID");
						winningBeanList.add(winBean);
						mainPwtBean.setWinningBeanList(winningBeanList);
						return mainPwtBean;
					}

					if ("TICKET_EXPIRED".equalsIgnoreCase(winBean.getStatus())) {
						logger.debug("Ticket has been expired.");
						winBean.setStatus("TICKET_EXPIRED");
						mainPwtBean.setStatus("TICKET_EXPIRED");
						winningBeanList.add(winBean);
						mainPwtBean.setWinningBeanList(winningBeanList);
						return mainPwtBean;
					} else {
						logger.debug("inside invalid ticket ");
						boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "DG_PWT_INVALID", "1");
						logger.debug("*****is fraud***** " + isFraud);
						if (isFraud) {
							winBean.setStatus("FRAUD");
							mainPwtBean.setStatus("FRAUD");
							winningBeanList.add(winBean);
							mainPwtBean.setWinningBeanList(winningBeanList);
							return mainPwtBean;

						} else {
							winBean.setValid(false);
							winBean.setStatus("ERROR");
							mainPwtBean.setStatus("ERROR");
							winningBeanList.add(winBean);
							mainPwtBean.setWinningBeanList(winningBeanList);
							return mainPwtBean;
						}
					}
				}

				winningBeanList.add(winBean);

				// if and only if priomo tickrt is reference
				String raffleTktType = getRaffleTktTypeFromgameNbr(gameId, connection);
				if ("REFERENCE".equalsIgnoreCase(raffleTktType)) {
					// get the promo ticket from promo tble
					int len = ticketNumber.length();
					List<String> promoTicketList = orgOnLineSaleCreditUpdation
							.getAssociatedPromoTicket(ticketNumber.substring(0, len - 1));
					for (int i = 0; i < promoTicketList.size(); i++) {
						PWTDrawBean promoWinBean = new PWTDrawBean();
						promoWinBean.setTicketNo(promoTicketList.get(i) + "0");
						promoWinBean.setPartyId(userInfoBean.getUserOrgId());
						promoWinBean.setPartyType(userInfoBean.getUserType());
						promoWinBean.setUserId(userInfoBean.getUserId());
						promoWinBean.setRefMerchantId(refMerchantId);
						promoWinBean.setPwtTicketType("REFERENCE");
						// here identify the type of request for raffle or for
						// draw(standard)
						sReq.setServiceMethod(ServiceMethodName.RAFFLE_PRZE_WINNING_TICKET);
						sReq.setServiceData(promoWinBean);
						sRes = delegate.getResponse(sReq);

						promoWinBean = (PWTDrawBean) new Gson().fromJson((JsonElement) sRes.getResponseData(), type);
						// promoWinBean = (PWTDrawBean) sRes.getResponseData();

						if (sRes.getIsSuccess()) {
							RetailerPwtProcessHelper pwtProcess = new RetailerPwtProcessHelper();
							pwtProcess.verifyDrawPwt(promoWinBean.getTicketNo(), promoWinBean.getGameId(), connection,
									userInfoBean, highPrizeCriteria, highPrizeAmt, promoWinBean, highPrizeScheme, false,
									"RAFFLE", null);
							// connection.commit();
							// connection.close();
							promoWinBean.setStatus("SUCCESS");
							winningBeanList.add(promoWinBean);
						} else {
							promoWinBean.setValid(false);
							promoWinBean.setStatus("ERROR");
							winningBeanList.add(promoWinBean);
							mainPwtBean.setWinningBeanList(winningBeanList);
							return mainPwtBean;
						}
					}
				}

				mainPwtBean.setWinningBeanList(winningBeanList);

			}
			mainPwtBean = getTotalPWTTkeAmt(mainPwtBean, highPrizeCriteria, highPrizeAmt, connection, userInfoBean);
			connection.commit();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			mainPwtBean.setStatus("ERROR");
		}
		return mainPwtBean;
	}

	public RafflePurchaseBean rafflePurchaseTicket(UserInfoBean userBean, Object gameBean, boolean isPromo)
			throws LMSException, SQLException {
		RafflePurchaseBean rafflePurchaseBean = new RafflePurchaseBean();
		fillRaffleBean(rafflePurchaseBean, gameBean);
		Connection con = DBConnect.getConnection();
		long balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, rafflePurchaseBean.getRaffle_no(),
				0.0, rafflePurchaseBean.getPlrMobileNumber(), con);

		rafflePurchaseBean.setAdvMsg(
				Util.getAdvMessage(userBean.getUserOrgId(), rafflePurchaseBean.getRaffle_no(), "PLAYER", "SALE", "DG"));
		if (balDed > 0) {
			rafflePurchaseBean.setRefTransId(balDed + "");

			// ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.RAFFLE);
			sReq.setServiceMethod(ServiceMethodName.RAFFLE_PURCHASE_TICKET);
			if (isPromo) {
				rafflePurchaseBean.setPromotkt(true);
			} else {
				rafflePurchaseBean.setPromotkt(false);
			}

			sReq.setServiceData(rafflePurchaseBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();

			logger.debug("Inside rafflePurchaseTicket *******");
			String responseString = delegate.getResponseString(sReq);
			// long t1 = System.currentTimeMillis();
			// Type elementType = new TypeToken<KenoResponseBean>(){}.getType();
			rafflePurchaseBean = new Gson().fromJson(responseString, RafflePurchaseBean.class);
			fillRaffleBean(rafflePurchaseBean, gameBean);
			// sRes = delegate.getResponseString(sReq);
			rafflePurchaseBean.setGameDispName(Util.getGameDisplayName(rafflePurchaseBean.getRaffle_no()));
			if (rafflePurchaseBean.isSuccess()) {

				rafflePurchaseBean.setSaleStatus("SUCCESS");
				int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
				int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
						rafflePurchaseBean.getRaffleTicket_no(), rafflePurchaseBean.getRaffle_no(), userBean,
						rafflePurchaseBean.getPurchaseChannel(), con);

			} else {
				rafflePurchaseBean.setSaleStatus("FAILED");
			}

		} else {
			rafflePurchaseBean.setSaleStatus("FAILED");
		}

		return rafflePurchaseBean;

	}

	public PWTDrawBean registerPlayer(String firstName, String lastName, String idNumber, String idType,
			PWTDrawBean drawBean, UserInfoBean userInfoBean) {
		Connection connection = DBConnect.getConnection();
		try {
			connection.setAutoCommit(false);
			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.PWT_MGMT);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			drawBean.setStatus("REGISTRATION");
			String highPrizeScheme = (String) ServletActionContext.getServletContext()
					.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
			RetailerPwtProcessHelper retPwt = new RetailerPwtProcessHelper();
			retPwt.plrRegistrationAndApproval(firstName, lastName, idNumber, idType, connection, drawBean, userInfoBean,
					highPrizeScheme);
			// Draw Game Updation of Ticket
			sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
			sReq.setServiceData(drawBean);
			sRes = delegate.getResponse(sReq);
			logger.debug("Registration---" + sRes.getIsSuccess());
			if (sRes.getIsSuccess()) {
				connection.commit();
				drawBean.setStatus("SUCCESS");
			} else {
				drawBean.setStatus("ERROR");
			}
		} catch (Exception e) {
			e.printStackTrace();
			drawBean.setStatus("ERROR");
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return drawBean;

	}

	/**
	 * This method is used to reprit the promo ticket of standard game oly
	 */

	public Object reprintPromoStdTicket(int gameId, String ticketNo, boolean isPwt, boolean isPromo, Connection con,
			UserInfoBean userInfoBean) {

		String saleStatus = null;
		Object reprintBean = null;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		try {
			/*
			 * Statement stm = con.createStatement(); ResultSet rs = stm
			 * .executeQuery("select ticket_nbr from st_dg_ret_sale_refund_" +
			 * gameNo + " where ticket_nbr=" + ticketNo);
			 * logger.debug("Reprint****ticketNo****" + ticketNo);
			 */
			ReprintBean reprintbean = new ReprintBean();
			reprintbean.setPwt(isPwt);
			reprintbean.setTicketNumber(ticketNo + /* "00" */Util.getRpcAppenderForTickets(ticketNo.length())); // CHECK
																												// FOR
																												// NEW
																												// TKT
																												// NUMBER
			reprintbean.setGameId(gameId);
			sReq.setServiceData(reprintbean);
			// sReq.setServiceData(ticketNo + "00");// 00 is appended in case of
			// reprint to set the length
			// of actual ticket
			// This has no affect on functionality.
			String family = Util.getGameType(gameId);
			String gameName = Util.getGameName(gameId);
			// String pck = "com.skilrock.lms.beans.";
			// String pb = "PurchaseBean";
			// Class familyClass = Class.forName(pck + family + pb);
			/*
			 * if (rs.next() || ticketNo == null) { Method method =
			 * familyClass.getDeclaredMethod("setSaleStatus", String.class);
			 * method.invoke(familyClass.newInstance(), "REPRINT_FAIL"); return
			 * familyClass; }
			 */
			sReq.setServiceName("playMgmt");
			sReq.setServiceMethod(gameName.toLowerCase() + "ReprintTicket");

			// rg calling
			boolean isFraud = (isPwt || isPromo) ? false
					: ResponsibleGaming.respGaming(userInfoBean, "DG_REPRINT", "1", con);

			// boolean isFraud = ResponsibleGaming.respGaming(userInfoBean,
			// "DG_REPRINT", "1", con);
			if (!isFraud) {
				sRes = delegate.getResponse(sReq);

				String responseString = sRes.getResponseData().toString();
				Type elementType = null;

				if (sRes.getIsSuccess()) {

					// reprintBean = new Gson().fromJson(responseString,
					// elementType);

					if ("Fortune".equals(family)) {
						// FortunePurchaseBean winningBean =
						// (FortunePurchaseBean) reprintBean;
						// responseString = sRes.getResponseData().toString();
						elementType = new TypeToken<FortunePurchaseBean>() {
						}.getType();
						FortunePurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						if (winningBean.isPromotkt()) {
							winningBean.setTotalPurchaseAmt(0.0);
						}
						saleStatus = winningBean.getSaleStatus();
						// return (FortunePurchaseBean) reprintBean;
					} else if ("Lotto".equals(family)) {
						// LottoPurchaseBean winningBean = (LottoPurchaseBean)
						// reprintBean;
						// responseString = sRes.getResponseData().toString();
						elementType = new TypeToken<LottoPurchaseBean>() {
						}.getType();
						LottoPurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						saleStatus = winningBean.getSaleStatus();
						// return (LottoPurchaseBean) reprintBean;

					} else if ("Keno".equals(family)) {
						// KenoPurchaseBean winningBean = (KenoPurchaseBean)
						// reprintBean;
						// responseString = sRes.getResponseData().toString();
						elementType = new TypeToken<KenoPurchaseBean>() {
						}.getType();
						KenoPurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						saleStatus = winningBean.getSaleStatus();
						// return (KenoPurchaseBean) reprintBean;
					} else if ("FortuneTwo".equals(family)) {
						// FortuneTwoPurchaseBean winningBean =
						// (FortuneTwoPurchaseBean)reprintBean;
						// responseString = sRes.getResponseData().toString();
						elementType = new TypeToken<FortuneTwoPurchaseBean>() {
						}.getType();
						FortuneTwoPurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						saleStatus = winningBean.getSaleStatus();
					} else if ("FortuneThree".equals(family)) {
						// FortuneThreePurchaseBean winningBean =
						// (FortuneThreePurchaseBean)reprintBean;
						// responseString = sRes.getResponseData().toString();
						elementType = new TypeToken<FortuneThreePurchaseBean>() {
						}.getType();
						FortuneThreePurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						saleStatus = winningBean.getSaleStatus();
					} else if ("RAFFLE".equals(family)) {
						elementType = new TypeToken<RafflePurchaseBean>() {
						}.getType();
						RafflePurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						saleStatus = winningBean.getSaleStatus();
					} else if ("Rainbow".equals(family)) {
						elementType = new TypeToken<KenoPurchaseBean>() {
						}.getType();
						KenoPurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						saleStatus = winningBean.getSaleStatus();
					}

					logger.debug(saleStatus + "****reprintTicket rpos helper********");
					if ("LIMIT_EXCEEDED".equalsIgnoreCase(saleStatus)) {
						return new String("ErrorMsg:" + EmbeddedErrors.LIMIT_EXCEEDED + "ErrorCode:"
								+ EmbeddedErrors.LIMIT_EXCEEDED_ERROR_CODE + "|"); // Normal
					}
					if ("PERFORMED".equalsIgnoreCase(saleStatus)) {
						return new String("ErrorMsg:" + EmbeddedErrors.DRAW_PERFORMED + "ErrorCode:"
								+ EmbeddedErrors.DRAW_PERFORMED_ERROR_CODE + "|");
					}

					if ("Fortune".equals(family)) {
						elementType = new TypeToken<FortunePurchaseBean>() {
						}.getType();
						FortunePurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						return winningBean;
					} else if ("Lotto".equals(family)) {
						elementType = new TypeToken<LottoPurchaseBean>() {
						}.getType();
						LottoPurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						return winningBean;
					} else if ("Keno".equals(family)) {
						elementType = new TypeToken<KenoPurchaseBean>() {
						}.getType();
						KenoPurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						return winningBean;
					} else if ("FortuneTwo".equals(family)) {
						elementType = new TypeToken<FortuneThreePurchaseBean>() {
						}.getType();
						FortuneThreePurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						return winningBean;
					} else if ("FortuneThree".equalsIgnoreCase(family)) {
						elementType = new TypeToken<FortuneThreePurchaseBean>() {
						}.getType();
						FortuneThreePurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						return winningBean;
					} else if ("RAFFLE".equalsIgnoreCase(family)) {
						elementType = new TypeToken<RafflePurchaseBean>() {
						}.getType();
						RafflePurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						return winningBean;
					} else if ("Rainbow".equalsIgnoreCase(family)) {
						elementType = new TypeToken<KenoPurchaseBean>() {
						}.getType();
						KenoPurchaseBean winningBean = new Gson().fromJson(responseString, elementType);
						return winningBean;
					}
				}
			} else {
				// com.skilrock.lms.common.utility.CommonMethods
				// .fetchUpdatedReprintCount(gameNo, ticketNo, -1);
				return new String("RG_RPERINT"); // Responsible Gaming
				// Reprint Limit Exceed
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method is used to reprint the raflle ticket only
	 */
	public Object reprintRaffleTicket(boolean isFirstTime, Connection con, int gameId, boolean isPwt,
			String tktNumber) {

		String saleStatus;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		// Object reprintRaffleBean = null;
		ReprintBean reprintbean = new ReprintBean();
		String raffleTicketType = getRaffleTktTypeFromgameNbr(gameId, con);
		if (isFirstTime || raffleTicketType.equalsIgnoreCase("REFERENCE")) {
			reprintbean.setPwt(isPwt);
			reprintbean.setTicketNumber(tktNumber);
			reprintbean.setGameId(gameId);
			sReq.setServiceData(reprintbean);
			sReq.setServiceName(ServiceName.RAFFLE);
			sReq.setServiceMethod(ServiceMethodName.RAFFLE_REPRINT_TICKET);
			sRes = delegate.getResponse(sReq);
			String responseString = sRes.getResponseData().toString();
			Type elementType = null;
			if (sRes.getIsSuccess()) {
				elementType = new TypeToken<RafflePurchaseBean>() {
				}.getType();
				RafflePurchaseBean rafflePurchaseBean = new Gson().fromJson(responseString, elementType);
				// reprintRaffleBean = sRes.getResponseData();
				// RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean)
				// reprintRaffleBean;
				saleStatus = rafflePurchaseBean.getSaleStatus();
				if ("LIMIT_EXCEEDED".equalsIgnoreCase(saleStatus)) {
					return new String("Reprint Limit Exceed"); // Normal
				}
				if ("PERFORMED".equalsIgnoreCase(saleStatus)) {
					return new String("Draw Performed");
				}
				rafflePurchaseBean.setRaffleTicketType(raffleTicketType);
				return rafflePurchaseBean;
			}
		}
		return null;

	}

	public Object reprintTicket(UserInfoBean userInfoBean) {
		Connection con = DBConnect.getConnection();
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		Object reprintBean = null;
		int gameNo = 0;
		String saleStatus = null;
		String gameNoQry = "select transaction_id,game_nbr from st_lms_retailer_transaction_master srtm,st_dg_game_master sdgm where retailer_org_id="
				+ userInfoBean.getUserOrgId()
				+ " and srtm.game_id=sdgm.game_id and transaction_type like 'DG_%' order by transaction_id desc limit 1";
		logger.debug("Reprint**" + gameNoQry);
		try {
			con.setAutoCommit(false);
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(gameNoQry);
			String ticketNo = null;
			int txId = 0;

			while (rs.next()) {
				gameNo = rs.getShort("game_nbr");
				txId = rs.getInt("transaction_id");
			}
			rs = stm.executeQuery("select ticket_nbr from st_dg_ret_sale_" + gameNo + " where transaction_id=" + txId);
			while (rs.next()) {
				ticketNo = rs.getString("ticket_nbr");
			}
			rs = stm.executeQuery(
					"select ticket_nbr from st_dg_ret_sale_refund_" + gameNo + " where ticket_nbr=" + ticketNo);
			// check for pwt claimed for whole ticket
			//

			logger.debug("Reprint****ticketNo****" + ticketNo);
			sReq.setServiceData(ticketNo + "00");// 00 is appended in case of
			// reprint to set the length
			// of actual ticket
			// This has no affect on functionality.
			String family = Util.getGameType(gameNo);
			String gameName = Util.getGameName(gameNo);
			String pck = "com.skilrock.lms.beans.";
			String pb = "PurchaseBean";
			Class familyClass = Class.forName(pck + family + pb);
			if (rs.next() || ticketNo == null) {
				Method method = familyClass.getDeclaredMethod("setSaleStatus", String.class);
				method.invoke(familyClass.newInstance(), "REPRINT_FAIL");
				return familyClass;
			}
			sReq.setServiceName(gameName.toLowerCase() + "Module");
			sReq.setServiceMethod("reprintTicket");
			sRes = delegate.getResponse(sReq);
			if (sRes.getIsSuccess()) {
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "DG_REPRINT", "1", con);
				if (!isFraud) {
					reprintBean = sRes.getResponseData();

					// This code is just a backup, if the Applet Barcode
					// printing fails
					// There here is a duplicacy of fetching family type.

					if ("Fortune".equals(family)) {
						FortunePurchaseBean winningBean = (FortunePurchaseBean) reprintBean;
						saleStatus = winningBean.getSaleStatus();
					} else if ("Lotto".equals(family)) {
						LottoPurchaseBean winningBean = (LottoPurchaseBean) reprintBean;
						saleStatus = winningBean.getSaleStatus();

					} else if ("Keno".equals(family)) {
						KenoPurchaseBean winningBean = (KenoPurchaseBean) reprintBean;
						saleStatus = winningBean.getSaleStatus();

					} else if ("FortuneTwo".equals(family)) {
						FortuneTwoPurchaseBean winningBean = (FortuneTwoPurchaseBean) reprintBean;
						saleStatus = winningBean.getSaleStatus();
					} else if ("FortuneThree".equalsIgnoreCase(family)) {
						FortuneThreePurchaseBean winningBean = (FortuneThreePurchaseBean) reprintBean;
						saleStatus = winningBean.getSaleStatus();
					}

					logger.debug(saleStatus + "****reprintTicket rpos helper********");
					if ("LIMIT_EXCEEDED".equalsIgnoreCase(saleStatus)) {
						return new String("Reprint Limit Exceed"); // Normal
					}
					if ("PERFORMED".equalsIgnoreCase(saleStatus)) {
						return new String("Draw Performed");
					}
					con.commit();
					if ("Fortune".equals(family)) {
						return (FortunePurchaseBean) reprintBean;
					} else if ("Lotto".equals(family)) {
						return (LottoPurchaseBean) reprintBean;
					} else if ("Keno".equals(family)) {
						return (KenoPurchaseBean) reprintBean;
					} else if ("FortuneTwo".equals(family)) {
						return (FortuneTwoPurchaseBean) reprintBean;
					} else if ("FortuneThree".equalsIgnoreCase(family)) {
						return (FortuneThreePurchaseBean) reprintBean;
					}
				} else {
					return new String("RG_RPERINT"); // Responsible Gaming
					// Reprint Limit Exceed
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public Object reprintTicket(UserInfoBean userInfoBean, boolean isPwt, String tkNbrPwt, boolean isFirstTime,
			String txnId, String interfaceType) {

		Connection con = DBConnect.getConnection();
		int gameId = 0;
		String phoneNo = null;
		String gameNoQry = "select srtm.transaction_id,sdgm.game_id,interface from st_lms_retailer_transaction_master srtm,st_dg_game_master sdgm,st_lms_transaction_master tm where retailer_org_id="
				+ userInfoBean.getUserOrgId()
				+ " and srtm.game_id=sdgm.game_id and transaction_type like 'DG_%' and tm.transaction_id=srtm.transaction_id order by transaction_id desc limit 1";
		logger.debug("Reprint**" + gameNoQry);
		String ticketNo = null;
		try {
			con.setAutoCommit(false);

			if (txnId != null && Long.parseLong(txnId) > 0) {
				ticketNo = getTicketNumberFrmTxnId(txnId, userInfoBean.getUserOrgId());

			} else {

				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(gameNoQry);
				String purchInterFace = "";

				if (!isPwt) {
					long txId = 0;

					while (rs.next()) {
						gameId = rs.getInt("game_id");
						txId = rs.getLong("transaction_id");
						purchInterFace = rs.getString("interface");
					}
					if (!interfaceType.equalsIgnoreCase(purchInterFace)) {
						return null;
					}

					double mrpAmount = 0.0;
					rs = stm.executeQuery("select ticket_nbr,mrp_amt,player_mob_number from st_dg_ret_sale_" + gameId
							+ " where transaction_id=" + txId);
					while (rs.next()) {
						ticketNo = rs.getString("ticket_nbr");
						mrpAmount = rs.getDouble("mrp_amt");
						phoneNo = rs.getString("player_mob_number");
						if (mrpAmount > 0) {
							// it is sale ticket
						} else {
							// it is promo ticket go to the mapping table to get
							// the
							// sale ticket
							rs = stm.executeQuery(
									"select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr="
											+ ticketNo);
							while (rs.next()) {
								ticketNo = rs.getString("sale_ticket_nbr");
							}
						}
					}
				} else {
					ticketNo = tkNbrPwt;
					// gameId=Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(ticketNo+/*"00"*/Util.getRpcAppenderForTickets(ticketNo.length())));
					// // CHECK ON THE BASIS OF TKTA OR TKTB IN REPRINT
				}
			}

			if (ticketNo == null) {
				//return EmbeddedErrors.REPRINT_FAIL.replace("|", "");
				return "ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL+"ErrorCode:" + EmbeddedErrors.REPRINT_FAIL_ERROR_CODE+"|";

			}
			// gameNo = getGamenoFromTktnumber(ticketNo + "00");
			gameId = Util.getGameIdFromGameNumber(
					Util.getGamenoFromTktnumber(ticketNo + /* "00" */Util.getRpcAppenderForTickets(ticketNo.length()))); // CHECK
																															// ON
																															// THE
																															// BASIS
																															// OF
																															// TKTA
																															// OR
																															// TKTB
																															// IN
																															// REPRINT

			if (gameId == 0) {
				return "ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL_ERROR_MSG + "ErrorCode:"
						+ EmbeddedErrors.REPRINT_FAIL_ERR_CODE + "|";
			}

			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(
					"select ticket_nbr from st_dg_ret_sale_refund_" + gameId + " where ticket_nbr=" + ticketNo);
			logger.debug("Reprint****ticketNo****" + ticketNo);

			if (rs.next() || ticketNo == null) {

				return "ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL_ERROR_MSG + "ErrorCode:"
						+ EmbeddedErrors.REPRINT_FAIL_ERR_CODE + "|";
			}

			List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
			boolean isStdtkeRprtSuccess = false;
			Object gameBean = reprintPromoStdTicket(gameId, ticketNo, isPwt, false, con, userInfoBean);
			FortunePurchaseBean fortunebean = null;
			LottoPurchaseBean lottobean = null;
			KenoPurchaseBean kenobean = null;
			FortuneTwoPurchaseBean fortuneTwoBean = null;
			FortuneThreePurchaseBean fortuneThreeBean = null;
			RafflePurchaseBean rafflePurchaseBean = null;
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userInfoBean, con);
			// ---
			if (gameBean instanceof CommonPurchaseBean) {
				CommonPurchaseBean commonBean = (CommonPurchaseBean) gameBean;
				commonBean.setAdvMsg(Util.getAdvMessage(userInfoBean.getUserOrgId(), gameId, "PLAYER", "SALE", "DG"));
			}
			// ---
			if (gameBean instanceof RafflePurchaseBean) {
				rafflePurchaseBean = (RafflePurchaseBean) gameBean;
				isStdtkeRprtSuccess = true;
			} else if (gameBean instanceof FortunePurchaseBean) {
				fortunebean = (FortunePurchaseBean) gameBean;
				isStdtkeRprtSuccess = true;

			} else if (gameBean instanceof LottoPurchaseBean) {
				lottobean = (LottoPurchaseBean) gameBean;
				isStdtkeRprtSuccess = true;

			} else if (gameBean instanceof KenoPurchaseBean) {
				kenobean = (KenoPurchaseBean) gameBean;
				kenobean.setPlrMobileNumber(phoneNo);
				kenobean.setAdvMsg(Util.getAdvMessage(userInfoBean.getUserOrgId(), gameId, "PLAYER", "SALE", "DG"));
				isStdtkeRprtSuccess = true;

			} else if (gameBean instanceof FortuneTwoPurchaseBean) {
				fortuneTwoBean = (FortuneTwoPurchaseBean) gameBean;
				isStdtkeRprtSuccess = true;

			} else if (gameBean instanceof FortuneThreePurchaseBean) {
				fortuneThreeBean = (FortuneThreePurchaseBean) gameBean;
				isStdtkeRprtSuccess = true;
			}

			else if (gameBean instanceof String && "RG_RPERINT".equals(gameBean.toString())) {
				return new String("RG_RPERINT");
			} else if (gameBean instanceof String) {
				return gameBean.toString();
			} else {
				return new String("ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL_ERROR_MSG + "ErrorCode:"
						+ EmbeddedErrors.REPRINT_FAIL_ERR_CODE + "|");
			}

			if (isStdtkeRprtSuccess) {
				List<String> promoTicketList = orgOnLineSaleCreditUpdation.getAssociatedPromoTicket(ticketNo, con);
				if (promoTicketList != null && promoTicketList.size() > 0) {
					List<LottoPurchaseBean> lottoBeanList = new ArrayList<LottoPurchaseBean>();
					for (int i = 0; i < promoTicketList.size(); i++) {
						String proMoTktNumber = promoTicketList.get(i);
						gameId = Util.getGameIdFromGameNumber(getGamenoFromTktnumber(promoTicketList.get(i)
								+ Util.getRpcAppenderForTickets(promoTicketList.get(i).length())));
						String gameType = Util.getGameType(gameId);
						if (gameType == null) {
							return new String("ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL_ERROR_MSG + "ErrorCode:"
									+ EmbeddedErrors.REPRINT_FAIL_ERR_CODE + "|");
						}

						if ("RAFFLE".equalsIgnoreCase(gameType)) {
							Object raffleGameBean = reprintRaffleTicket(isFirstTime, con, gameId, isPwt,
									proMoTktNumber + Util.getRpcAppenderForTickets(proMoTktNumber.length()));
							if (raffleGameBean instanceof RafflePurchaseBean) {
								// RafflePurchaseBean rafflePurchaseBean =
								// (RafflePurchaseBean) raffleGameBean;
								rafflePurchaseBean = (RafflePurchaseBean) raffleGameBean;
								rafflePurchaseBeanList.add(rafflePurchaseBean);
								if (fortunebean != null) {
									fortunebean.setRafflePurchaseBeanList(rafflePurchaseBeanList); // has
									// to
									// be
									// change
									// like
									// keno
									// below
									fortunebean.setPromoPurchaseBean(rafflePurchaseBeanList);
									// return fortunebean;
								} else if (kenobean != null) {
									kenobean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
									kenobean.setPromoPurchaseBean(rafflePurchaseBeanList);
									// return kenobean;
								} else if (lottobean != null) {
									lottobean.setRafflePurchaseBeanList(rafflePurchaseBeanList);// has
									// to
									// be
									// change
									// like
									// keno
									// below
									lottobean.setPromoPurchaseBean(rafflePurchaseBeanList);
									// return lottobean;
								}

							} else if (raffleGameBean != null) {
								com.skilrock.lms.common.utility.CommonMethods.fetchUpdatedReprintCount(gameId, ticketNo,
										-1);
								return new String("ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL_ERROR_MSG + "ErrorCode:"
										+ EmbeddedErrors.REPRINT_FAIL_ERR_CODE + "|");
							}
						} else {
							if (isFirstTime) {
								Object promoGameBean = reprintPromoStdTicket(gameId, proMoTktNumber, isPwt, true, con,
										userInfoBean);

								if (promoGameBean instanceof String) {
									com.skilrock.lms.common.utility.CommonMethods.fetchUpdatedReprintCount(gameId,
											ticketNo, -1);
									return new String("ErrorMsg:" + EmbeddedErrors.REPRINT_FAIL_ERROR_MSG + "ErrorCode:"
											+ EmbeddedErrors.REPRINT_FAIL_ERR_CODE + "|");
								} else if (promoGameBean instanceof LottoPurchaseBean) {
									lottoBeanList.add((LottoPurchaseBean) promoGameBean);
									logger.debug("Promo Bean Data for zim lotto Three" + promoGameBean);
									// kenoPurchaseBean.setFortunePurchaseBean(fortuneBean);
									lottobean.setPromoPurchaseBeanList(lottoBeanList);

								} else {
									kenobean.setPromoPurchaseBean(promoGameBean);
								}
							}

						}
					}
				}
				con.commit();
				if (fortunebean != null) {
					return fortunebean;
				} else if (kenobean != null) {
					return kenobean;
				} else if (lottobean != null) {
					return lottobean;
				} else if (fortuneTwoBean != null) {
					return fortuneTwoBean;
				} else if (fortuneThreeBean != null) {
					return fortuneThreeBean;
				} else if (rafflePurchaseBean != null) {
					return rafflePurchaseBean;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	public Object reprintTicket(UserInfoBean userInfoBean, String ticketNo) {
		logger.debug("---reprintTicket with Number=" + ticketNo);

		Connection con = DBConnect.getConnection();
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		Object reprintBean = null;
		int gameNo = 0;
		String saleStatus = null;
		int retOrgId = 0;
		try {
			con.setAutoCommit(false);
			ResultSet rs = null;
			Statement stm = con.createStatement();
			ValidateTicketBean tktBean = Util.validateTkt(ticketNo);
			if (tktBean.isValid()) {
				gameNo = Integer.parseInt(ticketNo.substring(4, 5));

				rs = stm.executeQuery("select game_id,retailer_org_id from st_dg_ret_sale_" + gameNo
						+ " where ticket_nbr=" + ticketNo.substring(0, ticketNo.length() - 2));
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
				}

				if (retOrgId != userInfoBean.getUserOrgId()) {
					return "You Are not Authorize To Reprint This Ticket";
				}
				rs = stm.executeQuery("select ticket_nbr from st_dg_ret_sale_refund_" + gameNo + " where ticket_nbr="
						+ ticketNo.substring(0, ticketNo.length() - 2));
				// check for pwt claimed for whole ticket
				//

				logger.debug("Reprint****ticketNo****" + ticketNo);
				sReq.setServiceData(ticketNo);// 00 is appended in case of
				// reprint to set the length
				// of actual ticket
				// This has no affect on functionality.
				String family = Util.getGameType(gameNo);
				String gameName = Util.getGameName(gameNo);
				String pck = "com.skilrock.lms.beans.";
				String pb = "PurchaseBean";
				Class familyClass = Class.forName(pck + family + pb);
				if (rs.next() || ticketNo == null) {
					Method method = familyClass.getDeclaredMethod("setSaleStatus", String.class);
					method.invoke(familyClass.newInstance(), "REPRINT_FAIL");
					return familyClass;
				}
				sReq.setServiceName(gameName.toLowerCase() + "Module");
				sReq.setServiceMethod("reprintTicket");
				sRes = delegate.getResponse(sReq);
				if (sRes.getIsSuccess()) {
					// rg calling
					boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "DG_REPRINT", "1", con);
					if (!isFraud) {
						reprintBean = sRes.getResponseData();

						// This code is just a backup, if the Applet Barcode
						// printing fails
						// There here is a duplicacy of fetching family type.

						if ("Fortune".equals(family)) {
							FortunePurchaseBean winningBean = (FortunePurchaseBean) reprintBean;
							saleStatus = winningBean.getSaleStatus();
						} else if ("Lotto".equals(family)) {
							LottoPurchaseBean winningBean = (LottoPurchaseBean) reprintBean;
							saleStatus = winningBean.getSaleStatus();

						} else if ("Keno".equals(family)) {
							KenoPurchaseBean winningBean = (KenoPurchaseBean) reprintBean;
							saleStatus = winningBean.getSaleStatus();

						} else if ("FortuneTwo".equals(family)) {
							FortuneTwoPurchaseBean winningBean = (FortuneTwoPurchaseBean) reprintBean;
							saleStatus = winningBean.getSaleStatus();
						} else if ("FortuneThree".equals(family)) {
							FortuneThreePurchaseBean winningBean = (FortuneThreePurchaseBean) reprintBean;
							saleStatus = winningBean.getSaleStatus();
						}

						logger.debug(saleStatus + "****reprintTicket rpos helper********");
						if ("LIMIT_EXCEEDED".equalsIgnoreCase(saleStatus)) {

							return new String("Reprint Limit Exceed"); // Normal
						}
						if ("PERFORMED".equalsIgnoreCase(saleStatus)) {
							return new String("Draw Performed");
						}

						con.commit();

						if ("Fortune".equals(family)) {
							return (FortunePurchaseBean) reprintBean;
						} else if ("Lotto".equals(family)) {
							return (LottoPurchaseBean) reprintBean;
						} else if ("Keno".equals(family)) {
							return (KenoPurchaseBean) reprintBean;
						} else if ("FortuneTwo".equals(family)) {
							return (FortuneTwoPurchaseBean) reprintBean;
						} else if ("FortuneThree".equals(family)) {
							return (FortuneThreePurchaseBean) reprintBean;
						}
					} else {
						return new String("RG_RPERINT"); // Responsible
						// Gaming
						// Reprint Limit
						// Exceed
					}
				}
			} else {
				return new String("Invalid Ticket Number");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public FortunePurchaseBean zeroToNinePurchaseTicket(UserInfoBean userBean,
			FortunePurchaseBean zeroToNinePurchaseBean) throws LMSException {

		if (isDrawAvailable(zeroToNinePurchaseBean.getGame_no())
				|| chkFreezeTimeSale(zeroToNinePurchaseBean.getGame_no())) {
			zeroToNinePurchaseBean.setSaleStatus("NO_DRAWS");
			return zeroToNinePurchaseBean;
		}

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.ZEROTONINE);
		sReq.setServiceMethod(ServiceMethodName.ZEROTONINE_PURCHASE_TICKET);
		sReq.setServiceData(zeroToNinePurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		long balDed = 0;
		int tickUpd = 0;
		String status = "";
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		Connection con = null;
		try {
			if (fortuneDataValidation(zeroToNinePurchaseBean)) {

				logger.debug("Data validation returns true");
				zeroToNinePurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
						zeroToNinePurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));

				zeroToNinePurchaseBean.setSaleStatus("FAILED");
				double totPurAmt = 0.0;
				if (zeroToNinePurchaseBean.isPromotkt()) {
					totPurAmt = 0.0;
				} else {
					totPurAmt = zeroToNinePurchaseBean.getTotalNoOfPanels()
							* Util.getUnitPrice(zeroToNinePurchaseBean.getGame_no(), null)
							* zeroToNinePurchaseBean.getNoOfDraws();
				}
				zeroToNinePurchaseBean.setUnitPrice(Util.getUnitPrice(zeroToNinePurchaseBean.getGame_no(), null));

				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);

				zeroToNinePurchaseBean.setTotalPurchaseAmt(totPurAmt);
				if (!userBean.getUserType().equals("RETAILER")) {
					return zeroToNinePurchaseBean;
				}
				con = DBConnect.getConnection();

				if (zeroToNinePurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(zeroToNinePurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(zeroToNinePurchaseBean.getUserId(),
								zeroToNinePurchaseBean.getLastSoldTicketNo(), zeroToNinePurchaseBean.getGameId(), con);
					}
				}
				logger.debug("Inside ZERO TO NINE FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "", con);
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							zeroToNinePurchaseBean.getGame_no(), zeroToNinePurchaseBean.getTotalPurchaseAmt(),
							zeroToNinePurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = zeroToNinePurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);

				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					zeroToNinePurchaseBean.setSaleStatus("FRAUD");
				}

				logger.debug("Inside Zero To Nine FE*******" + sRes);
				return zeroToNinePurchaseBean;
			} else {
				logger.debug("Data validation returns false");
				zeroToNinePurchaseBean.setSaleStatus("FAILED");
				return zeroToNinePurchaseBean;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			if (balDed > 0) {
				zeroToNinePurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					zeroToNinePurchaseBean = (FortunePurchaseBean) sRes.getResponseData();

					modifiedTotalPurchaseAmt = zeroToNinePurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								zeroToNinePurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt,
								balDed, con);

					}
					tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							zeroToNinePurchaseBean.getTicket_no(), zeroToNinePurchaseBean.getGame_no(), userBean,
							zeroToNinePurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						zeroToNinePurchaseBean.setSaleStatus(status);
						if (!zeroToNinePurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode.getBarcode(
									zeroToNinePurchaseBean.getTicket_no() + zeroToNinePurchaseBean.getReprintCount());
						}
						return zeroToNinePurchaseBean;
					} else {
						status = "FAILED";
						zeroToNinePurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(zeroToNinePurchaseBean.getTicket_no() + zeroToNinePurchaseBean.getReprintCount(),
								zeroToNinePurchaseBean.getPurchaseChannel(), zeroToNinePurchaseBean.getDrawIdTableMap(),
								zeroToNinePurchaseBean.getGame_no(), zeroToNinePurchaseBean.getPartyId(),
								zeroToNinePurchaseBean.getPartyType(), zeroToNinePurchaseBean.getRefMerchantId(),
								userBean, zeroToNinePurchaseBean.getRefTransId());
						return zeroToNinePurchaseBean;
					}
				} else {
					zeroToNinePurchaseBean = (FortunePurchaseBean) sRes.getResponseData();
					if (zeroToNinePurchaseBean.getSaleStatus() == null) {
						zeroToNinePurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, zeroToNinePurchaseBean.getGame_no(),
								"FAILED", balDed);
						return zeroToNinePurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(zeroToNinePurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, zeroToNinePurchaseBean.getGame_no(),
								"FAILED", balDed);
						return zeroToNinePurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, zeroToNinePurchaseBean.getGame_no(),
								"FAILED", balDed);
						return zeroToNinePurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				zeroToNinePurchaseBean.setSaleStatus(status);
				return zeroToNinePurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return zeroToNinePurchaseBean;
	}

	// ZimLotto Data Validation
	public boolean zimLottoDataValidation(LottoPurchaseBean lottoPurchaseBean) {
		int noOfDraws = lottoPurchaseBean.getNoOfDraws();
		String[] picknumbers = lottoPurchaseBean.getPicknumbers();
		Set<Integer> picknumSet;
		String pickNum[];
		int noOfPanels = picknumbers.length;
		logger.debug("no of Panels: " + noOfPanels);
		if (noOfDraws > 0 && noOfPanels > 0) {
			for (int i = 0; i < noOfPanels; i++) {
				if (picknumbers[i].equalsIgnoreCase("QP")) {
					logger.debug("quick pick Selected");

				} else {
					logger.debug("not quick pick");
					logger.debug("Player picked Data:" + picknumbers[i]);
					pickNum = picknumbers[i].split(",");
					picknumSet = new HashSet<Integer>();
					for (String element : pickNum) {
						picknumSet.add(Integer.parseInt(element));
					}
					if (pickNum.length != picknumSet.size()
							|| pickNum.length > com.skilrock.lms.dge.gameconstants.ZimlottoConstants.MAX_PLAYER_PICKED) {
						logger.debug("picNum.Length: " + pickNum.length + "Set length:  " + picknumSet.size());
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	// tanzanialotto validation

	public boolean tanzaniaLottoDataValidation(LottoPurchaseBean lottoPurchaseBean) {
		int noOfDraws = lottoPurchaseBean.getNoOfDraws();
		String[] picknumbers = lottoPurchaseBean.getPicknumbers();
		Set<Integer> picknumSet;
		String pickNum[];
		int noOfPanels = picknumbers.length;
		logger.debug("no of Panels: " + noOfPanels);
		if (noOfPanels < 5 || noOfPanels % 5 != 0) {
			return false;
		}
		if (noOfDraws > 0 && noOfPanels > 0) {
			for (int i = 0; i < noOfPanels; i++) {
				if (picknumbers[i].equalsIgnoreCase("QP")) {
					logger.debug("quick pick Selected");

				} else {
					logger.debug("not quick pick");
					logger.debug("Player picked Data:" + picknumbers[i]);
					pickNum = picknumbers[i].split(",");
					picknumSet = new HashSet<Integer>();
					for (String element : pickNum) {
						picknumSet.add(Integer.parseInt(element));
					}
					if (pickNum.length != picknumSet.size()
							|| pickNum.length != com.skilrock.lms.dge.gameconstants.TanzanialottoConstants.MAX_PLAYER_PICKED) {
						logger.debug("picNum.Length: " + pickNum.length + "Set length:  " + picknumSet.size());
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public LottoPurchaseBean zimLottoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {
		// here insert the last sold tikcet on terminal or by third party

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.ZIMLOTTO);
		sReq.setServiceMethod(ServiceMethodName.ZIMLOTTO_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		lottoPurchaseBean.setSaleStatus("FAILED");
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;

		try {
			if (zimLottoDataValidation(lottoPurchaseBean)) {
				con = DBConnect.getConnection();
				if (lottoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(),
								lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getGameId(), con);
					}
				}

				double totPurAmt = lottoPurchaseBean.getPicknumbers().length
						* Util.getUnitPrice(lottoPurchaseBean.getGame_no(), null) * lottoPurchaseBean.getNoOfDraws();

				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);

				lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);

				logger.debug("Inside  FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTotalPurchaseAmt(),
							lottoPurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);

				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					lottoPurchaseBean.setSaleStatus("FRAUD");
				}

			} else {
				logger.debug("Data validation returns false");
				lottoPurchaseBean.setSaleStatus("FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
			 * String ticketNo = lottoPurchaseBean.getTicket_no(); String
			 * cancelChannel = lottoPurchaseBean.getPurchaseChannel();
			 * Map<Integer, Map<Integer, String>> drawIdTableMap =
			 * lottoPurchaseBean .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
		}

		try {
			if (balDed > 0) {
				lottoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								lottoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGame_no(), userBean,
							lottoPurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						// lottoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
						// lottoPurchaseBean.getGame_no(), "PLAYER", "SALE",
						// "DG"));
						lottoPurchaseBean.setAdvMsg(
								Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGame_no()));
						lottoPurchaseBean.setSaleStatus(status);
						if (!lottoPurchaseBean.getBarcodeType().equals("applet")) {
							new IDBarcode()
									.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
						}

						// call promo purchase process here
						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
						// CommonFunctionsHelper commonHelper = new
						// CommonFunctionsHelper();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGame_no());

						// String gameType =
						// getGameType(fortunePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, lottoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									lottoPurchaseBean.setSaleStatus(status);
									// Code for canceling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
											lottoPurchaseBean.getPurchaseChannel(),
											lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
											lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
											lottoPurchaseBean.getRefMerchantId(), userBean,
											lottoPurchaseBean.getRefTransId());
									lottoPurchaseBean.setSaleStatus("FAILED");
									return lottoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}
						/*
						 * String gameType =
						 * getGameType(lottoPurchaseBean.getGame_no());
						 * List<PromoGameBean> promoGameslist =
						 * commonHelper.getAvailablePromoGames(gameType);
						 * 
						 * for(int i=0;i<promoGameslist.size();i++){
						 * PromoGameBean promobean=promoGameslist.get(i);
						 * if(promobean.getPromoGametype().equalsIgnoreCase(
						 * "RAFFLE")){ lottoPurchaseBean.setRaffleNo(promobean.
						 * getPromoGameNo()); RafflePurchaseBean
						 * rafflePurchaseBean = (RafflePurchaseBean)
						 * rafflePurchaseTicket( userBean,
						 * lottoPurchaseBean,true);
						 * rafflePurchaseBean.setRaffleTicketType(promobean.
						 * getPromoTicketType()); if
						 * (rafflePurchaseBean.getSaleStatus()
						 * .equalsIgnoreCase("FAILED")) { status = "FAILED";
						 * lottoPurchaseBean .setSaleStatus(status); // Code for
						 * cancelling the Ticket to be send to Draw Game Engine
						 * cancelTicket(lottoPurchaseBean.getTicket_no(),
						 * lottoPurchaseBean.getPurchaseChannel(),
						 * lottoPurchaseBean.getDrawIdTableMap(),
						 * lottoPurchaseBean.getGame_no(),
						 * lottoPurchaseBean.getPartyId(),
						 * lottoPurchaseBean.getPartyType(),
						 * lottoPurchaseBean.getRefMerchantId(), userBean,
						 * lottoPurchaseBean .getRefTransId());
						 * lottoPurchaseBean.setSaleStatus("FAILED"); return
						 * lottoPurchaseBean; }else if
						 * (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase(
						 * "SUCCESS")){ //here insert entry into the promo
						 * ticket mapping table int
						 * tktlen=rafflePurchaseBean.getRaffleTicket_no().length
						 * (); orgOnLineSaleCreditUpdation.
						 * drawTicketNPromoMappigUpdate(1,
						 * rafflePurchaseBean.getGame_no(),
						 * rafflePurchaseBean.getParentTktNo(),
						 * (rafflePurchaseBean.getRaffleTicket_no()).substring(
						 * 0, tktlen - 2));
						 * rafflePurchaseBeanList.add(rafflePurchaseBean); } } }
						 */

						lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);

						return lottoPurchaseBean;
					} else {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getPurchaseChannel(),
								lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
								lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
								lottoPurchaseBean.getRefMerchantId(), userBean, lottoPurchaseBean.getRefTransId());

						return lottoPurchaseBean;
					}
				} else {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					if (lottoPurchaseBean.getSaleStatus() == null) {
						lottoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				lottoPurchaseBean.setSaleStatus(status);
				return lottoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lottoPurchaseBean;
	}

	// for tanzania lotto
	public LottoPurchaseBean tanzaniaLottoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.TANZANIALOTTO);
		sReq.setServiceMethod(ServiceMethodName.TANZANIALOTTO_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		lottoPurchaseBean.setSaleStatus("FAILED");
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;
		try {
			if (tanzaniaLottoDataValidation(lottoPurchaseBean)) {

				con = DBConnect.getConnection();
				// here insert the last sold tikcet on terminal or by third
				// party

				if (lottoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(),
								lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getGameId(), con);
					}
				}
				double totPurAmt = lottoPurchaseBean.getPicknumbers().length
						* Util.getUnitPrice(lottoPurchaseBean.getGame_no(), null) * lottoPurchaseBean.getNoOfDraws();

				logger.debug("!!totPurAmt!!!!" + totPurAmt);

				lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);
				lottoPurchaseBean.setUnitPrice(Util.getUnitPrice(lottoPurchaseBean.getGame_no(), null));

				logger.debug("Inside  FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTotalPurchaseAmt(),
							lottoPurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					lottoPurchaseBean.setSaleStatus("FRAUD");
				}
			} else {
				logger.debug("Data validation returns false");
				lottoPurchaseBean.setSaleStatus("FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
			 * String ticketNo = lottoPurchaseBean.getTicket_no(); String
			 * cancelChannel = lottoPurchaseBean.getPurchaseChannel();
			 * Map<Integer, Map<Integer, String>> drawIdTableMap =
			 * lottoPurchaseBean .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
		}

		try {

			if (balDed > 0) {
				lottoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();
					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								lottoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGame_no(), userBean,
							lottoPurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						lottoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
								lottoPurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
						lottoPurchaseBean.setSaleStatus(status);
						if (!lottoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode
									.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
						}

						// call promo purchase process here
						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
						// CommonFunctionsHelper commonHelper = new
						// CommonFunctionsHelper();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGame_no());

						// String gameType =
						// getGameType(fortunePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, lottoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									lottoPurchaseBean.setSaleStatus(status);
									// Code for canceling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
											lottoPurchaseBean.getPurchaseChannel(),
											lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
											lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
											lottoPurchaseBean.getRefMerchantId(), userBean,
											lottoPurchaseBean.getRefTransId());
									lottoPurchaseBean.setSaleStatus("FAILED");
									return lottoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}
						/*
						 * String gameType =
						 * getGameType(lottoPurchaseBean.getGame_no());
						 * List<PromoGameBean> promoGameslist =
						 * commonHelper.getAvailablePromoGames(gameType);
						 * 
						 * for(int i=0;i<promoGameslist.size();i++){
						 * PromoGameBean promobean=promoGameslist.get(i);
						 * if(promobean.getPromoGametype().equalsIgnoreCase(
						 * "RAFFLE")){ lottoPurchaseBean.setRaffleNo(promobean.
						 * getPromoGameNo()); RafflePurchaseBean
						 * rafflePurchaseBean = (RafflePurchaseBean)
						 * rafflePurchaseTicket( userBean,
						 * lottoPurchaseBean,true);
						 * rafflePurchaseBean.setRaffleTicketType(promobean.
						 * getPromoTicketType()); if
						 * (rafflePurchaseBean.getSaleStatus()
						 * .equalsIgnoreCase("FAILED")) { status = "FAILED";
						 * lottoPurchaseBean .setSaleStatus(status); // Code for
						 * cancelling the Ticket to be send to Draw Game Engine
						 * cancelTicket(lottoPurchaseBean.getTicket_no(),
						 * lottoPurchaseBean.getPurchaseChannel(),
						 * lottoPurchaseBean.getDrawIdTableMap(),
						 * lottoPurchaseBean.getGame_no(),
						 * lottoPurchaseBean.getPartyId(),
						 * lottoPurchaseBean.getPartyType(),
						 * lottoPurchaseBean.getRefMerchantId(), userBean,
						 * lottoPurchaseBean .getRefTransId());
						 * lottoPurchaseBean.setSaleStatus("FAILED"); return
						 * lottoPurchaseBean; }else if
						 * (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase(
						 * "SUCCESS")){ //here insert entry into the promo
						 * ticket mapping table int
						 * tktlen=rafflePurchaseBean.getRaffleTicket_no().length
						 * (); orgOnLineSaleCreditUpdation.
						 * drawTicketNPromoMappigUpdate(1,
						 * rafflePurchaseBean.getGame_no(),
						 * rafflePurchaseBean.getParentTktNo(),
						 * (rafflePurchaseBean.getRaffleTicket_no()).substring(
						 * 0, tktlen - 2));
						 * rafflePurchaseBeanList.add(rafflePurchaseBean); } } }
						 */

						lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);

						return lottoPurchaseBean;
					} else {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getPurchaseChannel(),
								lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
								lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
								lottoPurchaseBean.getRefMerchantId(), userBean, lottoPurchaseBean.getRefTransId());

						return lottoPurchaseBean;
					}
				} else {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					if (lottoPurchaseBean.getSaleStatus() == null) {
						lottoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					}

				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				lottoPurchaseBean.setSaleStatus(status);
				return lottoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lottoPurchaseBean;
	}

	// ZimLottotwo Data Validation
	public boolean zimLottoTwoDataValidation(LottoPurchaseBean lottoPurchaseBean) {
		int noOfDraws = lottoPurchaseBean.getNoOfDraws();
		String playType = lottoPurchaseBean.getPlayType();
		if ("Direct6".equals(playType)) {
			String[] picknumbers = lottoPurchaseBean.getPicknumbers();
			Set<Integer> picknumSet;
			String pickNum[];
			int noOfPanels = picknumbers.length;
			logger.debug("no of Panels: " + noOfPanels);
			if (noOfDraws > 0 && noOfPanels > 0) {
				for (int i = 0; i < noOfPanels; i++) {
					if (picknumbers[i].equalsIgnoreCase("QP")) {
						logger.debug("quick pick Selected");

					} else {
						logger.debug("not quick pick");
						logger.debug("Player picked Data:" + picknumbers[i]);
						pickNum = picknumbers[i].split(",");
						picknumSet = new HashSet<Integer>();
						for (String element : pickNum) {
							picknumSet.add(Integer.parseInt(element));
						}
						if (pickNum.length != picknumSet.size()
								|| pickNum.length > com.skilrock.lms.dge.gameconstants.ZimlottotwoConstants.MAX_PLAYER_PICKED) {
							logger.debug("picNum.Length: " + pickNum.length + "Set length:  " + picknumSet.size());
							return false;
						}
					}
				}
				return true;
			}
		} else if ("Perm6".equals(playType)) {
			return true;
		}
		return false;
	}

	// by sumit singla

	public LottoPurchaseBean zimLottoThreePurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.ZIMLOTTOTHREE);
		sReq.setServiceMethod(ServiceMethodName.ZIMLOTTOTHREE_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		lottoPurchaseBean.setSaleStatus("FAILED");
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;
		try {
			if (zimLottoTwoDataValidation(lottoPurchaseBean)) {

				con = DBConnect.getConnection();

				// here insert the last sold tikcet on terminal or by third
				// party

				if (lottoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(),
								lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getGameId(), con);
					}
				}
				int noOfLines = calculateNoOfLines(lottoPurchaseBean);
				double unitPrice = Util.getUnitPrice(lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPlayType());
				lottoPurchaseBean.setUnitPrice(unitPrice);
				double totPurAmt = 0.0;
				if (lottoPurchaseBean.isPromotkt()) {
					lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);
				} else {
					totPurAmt = noOfLines * unitPrice * lottoPurchaseBean.getNoOfDraws();
					totPurAmt = CommonMethods.fmtToTwoDecimal(totPurAmt);
				}

				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + totPurAmt);

				lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);
				if (!userBean.getUserType().equals("RETAILER")) {
					return lottoPurchaseBean;
				}

				logger.debug("Inside  FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "");
				if (!isFraud) {

					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTotalPurchaseAmt(),
							lottoPurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ oldTotalPurchaseAmt);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					lottoPurchaseBean.setSaleStatus("FRAUD");
				}
			} else {
				logger.debug("Data validation returns false");
				lottoPurchaseBean.setSaleStatus("FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
			 * String ticketNo = lottoPurchaseBean.getTicket_no(); String
			 * cancelChannel = lottoPurchaseBean.getPurchaseChannel();
			 * Map<Integer, Map<Integer, String>> drawIdTableMap =
			 * lottoPurchaseBean .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
		}

		try {
			if (balDed > 0) {
				lottoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					// rouding

					modifiedTotalPurchaseAmt = CommonMethods.roundDrawTktAmt(modifiedTotalPurchaseAmt);

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						lottoPurchaseBean.setTotalPurchaseAmt(modifiedTotalPurchaseAmt);
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								lottoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed,
								con);

					}
					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGame_no(), userBean,
							lottoPurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						// lottoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
						// lottoPurchaseBean.getGame_no(), "PLAYER", "SALE",
						// "DG"));
						lottoPurchaseBean.setAdvMsg(
								Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGame_no()));

						lottoPurchaseBean.setSaleStatus(status);
						if (!lottoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode
									.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
						}

						// call promo purchase process here
						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
						// CommonFunctionsHelper commonHelper = new
						// CommonFunctionsHelper();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGame_no());

						// String gameType =
						// getGameType(fortunePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, lottoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									lottoPurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
											lottoPurchaseBean.getPurchaseChannel(),
											lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
											lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
											lottoPurchaseBean.getRefMerchantId(), userBean,
											lottoPurchaseBean.getRefTransId());
									lottoPurchaseBean.setSaleStatus("FAILED");
									return lottoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							}
						}
						/*
						 * String gameType =
						 * getGameType(lottoPurchaseBean.getGame_no());
						 * List<PromoGameBean> promoGameslist =
						 * commonHelper.getAvailablePromoGames(gameType);
						 * 
						 * for(int i=0;i<promoGameslist.size();i++){
						 * PromoGameBean promobean=promoGameslist.get(i);
						 * if(promobean.getPromoGametype().equalsIgnoreCase(
						 * "RAFFLE")){ lottoPurchaseBean.setRaffleNo(promobean.
						 * getPromoGameNo()); RafflePurchaseBean
						 * rafflePurchaseBean = (RafflePurchaseBean)
						 * rafflePurchaseTicket( userBean,
						 * lottoPurchaseBean,true);
						 * rafflePurchaseBean.setRaffleTicketType(promobean.
						 * getPromoTicketType()); if
						 * (rafflePurchaseBean.getSaleStatus()
						 * .equalsIgnoreCase("FAILED")) { status = "FAILED";
						 * lottoPurchaseBean .setSaleStatus(status); // Code for
						 * cancelling the Ticket to be send to Draw Game Engine
						 * cancelTicket(lottoPurchaseBean.getTicket_no(),
						 * lottoPurchaseBean.getPurchaseChannel(),
						 * lottoPurchaseBean.getDrawIdTableMap(),
						 * lottoPurchaseBean.getGame_no(),
						 * lottoPurchaseBean.getPartyId(),
						 * lottoPurchaseBean.getPartyType(),
						 * lottoPurchaseBean.getRefMerchantId(), userBean,
						 * lottoPurchaseBean .getRefTransId());
						 * lottoPurchaseBean.setSaleStatus("FAILED"); return
						 * lottoPurchaseBean; }else if
						 * (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase(
						 * "SUCCESS")){ //here insert entry into the promo
						 * ticket mapping table int
						 * tktlen=rafflePurchaseBean.getRaffleTicket_no().length
						 * (); orgOnLineSaleCreditUpdation.
						 * drawTicketNPromoMappigUpdate(1,
						 * rafflePurchaseBean.getGame_no(),
						 * rafflePurchaseBean.getParentTktNo(),
						 * (rafflePurchaseBean.getRaffleTicket_no()).substring(
						 * 0, tktlen - 2));
						 * rafflePurchaseBeanList.add(rafflePurchaseBean); } } }
						 */

						lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);

						return lottoPurchaseBean;
					} else {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getPurchaseChannel(),
								lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
								lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
								lottoPurchaseBean.getRefMerchantId(), userBean, lottoPurchaseBean.getRefTransId());

						return lottoPurchaseBean;
					}
				} else {

					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					if (lottoPurchaseBean.getSaleStatus() == null) {
						lottoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				lottoPurchaseBean.setSaleStatus(status);
				return lottoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lottoPurchaseBean;
	}

	public LottoPurchaseBean zimLottoTwoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.ZIMLOTTOTWO);
		sReq.setServiceMethod(ServiceMethodName.ZIMLOTTOTWO_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		double lmsTicketPrice = 0.0;
		double dgeTicketPrice = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;

		try {
			if (zimLottoTwoDataValidation(lottoPurchaseBean)) {
				// lottoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
				// lottoPurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
				lottoPurchaseBean
						.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGame_no()));

				lottoPurchaseBean.setSaleStatus("FAILED");
				int noOfLines = lottoPurchaseBean.getPicknumbers().length;

				con = DBConnect.getConnection();

				// here insert the last sold tikcet on terminal or by third
				// party

				if (lottoPurchaseBean.getLastSoldTicketNo() != null) {
					if (!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())) {
						Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(),
								lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getGameId(), con);
					}
				}
				if ("Perm6".equals(lottoPurchaseBean.getPlayType())) {
					int n = lottoPurchaseBean.getNoPicked();
					if (n < 7) {
						lottoPurchaseBean.setSaleStatus("FAILED");
						return lottoPurchaseBean;
					}
					noOfLines = (n * (n - 1) * (n - 2) * (n - 3) * (n - 4) * (n - 5)) / 720;
				}
				lottoPurchaseBean.setNoOfLines(noOfLines);
				double unitPrice = Util.getUnitPrice(lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPlayType());
				lottoPurchaseBean.setUnitPrice(unitPrice);
				dgeTicketPrice = noOfLines * unitPrice * lottoPurchaseBean.getNoOfDraws();
				// rouding
				lmsTicketPrice = CommonMethods.roundDrawTktAmt(dgeTicketPrice);

				lmsTicketPrice = CommonMethods.fmtToTwoDecimal(lmsTicketPrice);

				logger.debug("DGE Ticket Price" + dgeTicketPrice);
				logger.debug("LMS Ticket Price" + lmsTicketPrice);
				lottoPurchaseBean.setTotalPurchaseAmt(dgeTicketPrice);

				logger.debug("Inside  FE1*******");
				// rg calling
				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", lmsTicketPrice + "");
				if (!isFraud) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
							lottoPurchaseBean.getGame_no(), lmsTicketPrice, lottoPurchaseBean.getPlrMobileNumber(),
							con);

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"
							+ lmsTicketPrice);
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					lottoPurchaseBean.setSaleStatus("FRAUD");
				}
			} else {
				logger.debug("Data validation returns false");
				lottoPurchaseBean.setSaleStatus("FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
			 * String ticketNo = lottoPurchaseBean.getTicket_no(); String
			 * cancelChannel = lottoPurchaseBean.getPurchaseChannel();
			 * Map<Integer, Map<Integer, String>> drawIdTableMap =
			 * lottoPurchaseBean .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
		}

		try {
			if (balDed > 0) {
				lottoPurchaseBean.setRefTransId(balDed + "");
				sRes = delegate.getResponse(sReq);

				if (sRes.getIsSuccess()) {
					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					modifiedTotalPurchaseAmt = lottoPurchaseBean.getTotalPurchaseAmt();

					logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
							+ modifiedTotalPurchaseAmt);
					con = DBConnect.getConnection();
					if (dgeTicketPrice != modifiedTotalPurchaseAmt) {

						// rouding

						modifiedTotalPurchaseAmt = CommonMethods.roundDrawTktAmt(modifiedTotalPurchaseAmt);

						lottoPurchaseBean.setTotalPurchaseAmt(modifiedTotalPurchaseAmt);
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
								lottoPurchaseBean.getGame_no(), modifiedTotalPurchaseAmt, lmsTicketPrice, balDed, con);

					}
					int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
							lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGame_no(), userBean,
							lottoPurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						lottoPurchaseBean.setSaleStatus(status);
						if (!lottoPurchaseBean.getBarcodeType().equals("applet")) {
							IDBarcode
									.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
						}

						// call promo purchase process here
						List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
						// CommonFunctionsHelper commonHelper = new
						// CommonFunctionsHelper();

						List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGame_no());

						// String gameType =
						// getGameType(fortunePurchaseBean.getGame_no());
						// List<PromoGameBean> promoGameslist =
						// commonHelper.getAvailablePromoGames(gameType);

						boolean isLoopAgain = true;

						for (int i = 0; i < promoGameslist.size(); i++) {
							PromoGameBean promobean = promoGameslist.get(i);
							List<LottoPurchaseBean> lottoBeanList = new ArrayList<LottoPurchaseBean>();
							for (int k = 0; k < promobean.getNoOfPromoTickets(); k++) {
								if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
									lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
									RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
											userBean, lottoPurchaseBean, true);
									rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
									if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
										status = "FAILED";
										lottoPurchaseBean.setSaleStatus(status);
										// Code for cancelling the Ticket to
										// be send to Draw Game Engine
										cancelTicket(
												lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
												lottoPurchaseBean.getPurchaseChannel(),
												lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
												lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
												lottoPurchaseBean.getRefMerchantId(), userBean,
												lottoPurchaseBean.getRefTransId());
										lottoPurchaseBean.setSaleStatus("FAILED");
										return lottoPurchaseBean;
									} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
										// here insert entry into the promo
										// ticket mapping table
										int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
										orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
												rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
												rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
										rafflePurchaseBeanList.add(rafflePurchaseBean);
									}
								} else if (promobean.getPromoGametype().equalsIgnoreCase("Zimlottothree")) {

									if (isLoopAgain) {
										// call winfast purchase process
										int gameId = Util.getGameId("Zimlottothree");
										LottoPurchaseBean drawGamePurchaseBean = new LottoPurchaseBean();
										// drawGamePurchaseBean.setIsQP(1);
										// drawGamePurchaseBean.setTotalNoOfPanels(1);
										// drawGamePurchaseBean.setSymbols(symbols);
										// drawGamePurchaseBean.setNoOfPanels("1");

										// drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
										drawGamePurchaseBean.setDrawIdTableMap(lottoPurchaseBean.getDrawIdTableMap());
										drawGamePurchaseBean.setGame_no(gameId);
										drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
										// drawGamePurchaseBean.setIsQuickPick(isQuickPickNew);
										drawGamePurchaseBean.setNoOfDraws(2);
										drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
										drawGamePurchaseBean.setPartyType(userBean.getUserType());
										drawGamePurchaseBean.setUserId(userBean.getUserId());
										drawGamePurchaseBean.setRefMerchantId(lottoPurchaseBean.getRefMerchantId());
										drawGamePurchaseBean.setPurchaseChannel(lottoPurchaseBean.getPurchaseChannel());
										drawGamePurchaseBean.setPromotkt(true);
										drawGamePurchaseBean.setNoPicked(lottoPurchaseBean.getNoPicked());
										drawGamePurchaseBean.setPlrMobileNumber(lottoPurchaseBean.getPlrMobileNumber());

										StringBuilder sb = new StringBuilder("QP");
										if ("Direct6".equalsIgnoreCase(lottoPurchaseBean.getPlayType())) {
											for (int m = 1; m < lottoPurchaseBean.getNoOfLines()
													* promobean.getNoOfPromoTickets(); m++) {
												sb.append("NxtQP");
											}
										}
										String pickedNumbers = sb.toString();
										String[] picknumbers = pickedNumbers.split("Nxt");
										drawGamePurchaseBean.setPickedNumbers(pickedNumbers);
										drawGamePurchaseBean.setPicknumbers(picknumbers);

										drawGamePurchaseBean.setPlayType(lottoPurchaseBean.getPlayType());
										// if (drawIdArr != null) {
										// drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
										// }
										drawGamePurchaseBean.setIsAdvancedPlay(0);
										LottoPurchaseBean lottoBean = zimLottoThreePurchaseTicket(userBean,
												drawGamePurchaseBean);
										if ("SUCCESS".equalsIgnoreCase(lottoBean.getSaleStatus())) {
											orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
													lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTicket_no(),
													lottoBean.getTicket_no());
										} else {
											lottoPurchaseBean.setPromoSaleStatus("FAILED");
											return lottoPurchaseBean;
										}

										lottoBeanList.add(lottoBean);
										logger.debug("Promo Bean Data for zim lotto Three" + lottoBean);
										// kenoPurchaseBean.setFortunePurchaseBean(fortuneBean);
										lottoPurchaseBean.setPromoPurchaseBeanList(lottoBeanList);

									}
									if ("Direct6".equalsIgnoreCase(lottoPurchaseBean.getPlayType()))
										isLoopAgain = false;
								}
							}
						}
						/*
						 * String gameType =
						 * getGameType(lottoPurchaseBean.getGame_no());
						 * List<PromoGameBean> promoGameslist =
						 * commonHelper.getAvailablePromoGames(gameType);
						 * 
						 * for(int i=0;i<promoGameslist.size();i++){
						 * PromoGameBean promobean=promoGameslist.get(i);
						 * if(promobean.getPromoGametype().equalsIgnoreCase(
						 * "RAFFLE")){ lottoPurchaseBean.setRaffleNo(promobean.
						 * getPromoGameNo()); RafflePurchaseBean
						 * rafflePurchaseBean = (RafflePurchaseBean)
						 * rafflePurchaseTicket( userBean,
						 * lottoPurchaseBean,true);
						 * rafflePurchaseBean.setRaffleTicketType(promobean.
						 * getPromoTicketType()); if
						 * (rafflePurchaseBean.getSaleStatus()
						 * .equalsIgnoreCase("FAILED")) { status = "FAILED";
						 * lottoPurchaseBean .setSaleStatus(status); // Code for
						 * cancelling the Ticket to be send to Draw Game Engine
						 * cancelTicket(lottoPurchaseBean.getTicket_no(),
						 * lottoPurchaseBean.getPurchaseChannel(),
						 * lottoPurchaseBean.getDrawIdTableMap(),
						 * lottoPurchaseBean.getGame_no(),
						 * lottoPurchaseBean.getPartyId(),
						 * lottoPurchaseBean.getPartyType(),
						 * lottoPurchaseBean.getRefMerchantId(), userBean,
						 * lottoPurchaseBean .getRefTransId());
						 * lottoPurchaseBean.setSaleStatus("FAILED"); return
						 * lottoPurchaseBean; }else if
						 * (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase(
						 * "SUCCESS")){ //here insert entry into the promo
						 * ticket mapping table int
						 * tktlen=rafflePurchaseBean.getRaffleTicket_no().length
						 * (); orgOnLineSaleCreditUpdation.
						 * drawTicketNPromoMappigUpdate(1,
						 * rafflePurchaseBean.getGame_no(),
						 * rafflePurchaseBean.getParentTktNo(),
						 * (rafflePurchaseBean.getRaffleTicket_no()).substring(
						 * 0, tktlen - 2));
						 * rafflePurchaseBeanList.add(rafflePurchaseBean); } } }
						 */

						lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);

						return lottoPurchaseBean;
					} else {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to be send to
						// Draw
						// Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getPurchaseChannel(),
								lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
								lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
								lottoPurchaseBean.getRefMerchantId(), userBean, lottoPurchaseBean.getRefTransId());

						return lottoPurchaseBean;
					}
				} else {

					lottoPurchaseBean = (LottoPurchaseBean) sRes.getResponseData();
					if (lottoPurchaseBean.getSaleStatus() == null) {
						lottoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGame_no(),
								"FAILED", balDed);
						return lottoPurchaseBean;
					}
				}
			} else {

				status = getStatusIfErrorInTransaction(status, balDed);
				lottoPurchaseBean.setSaleStatus(status);
				return lottoPurchaseBean;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lottoPurchaseBean;
	}

	public LottoPurchaseBean zimLottoBonusPurchaseTicketLATEST(UserInfoBean userBean,
			LottoPurchaseBean lottoPurchaseBean) throws Exception {

		// ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.ZIMLOTTOBONUS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.ZIMLOTTOBONUS_PURCHASE_TICKET);
		// sReq.setServiceData(lottoPurchaseBean);
		LottoRequestBean requestBean = new LottoRequestBean();
		sReq.setServiceData(requestBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		double dgeTicketPrice = 0.0;
		double lmsTicketPrice = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;

		try {

			lottoPurchaseBean.setSaleStatus("FAILED");
			int noOfLines = lottoPurchaseBean.getPicknumbers().length;
			con = getConnectionObject();

			if ("TERMINAL".equals(lottoPurchaseBean.getDeviceType())) {
				int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
				checkLastPrintedTicketStatusAndUpdate(userBean, Long.parseLong(lottoPurchaseBean.getLastSoldTicketNo()),
						lottoPurchaseBean.getDeviceType(), lottoPurchaseBean.getRefMerchantId(), autoCancelHoldDays,
						lottoPurchaseBean.getActionName(), lottoPurchaseBean.getLastGameId(), con);
			}

			if ("Perm6".equals(lottoPurchaseBean.getPlayType())) {
				int n = lottoPurchaseBean.getNoPicked();
				if (n < 7) {
					lottoPurchaseBean.setSaleStatus("FAILED");
					return lottoPurchaseBean;
				}
				noOfLines = (n * (n - 1) * (n - 2) * (n - 3) * (n - 4) * (n - 5)) / 720;
			}
			lottoPurchaseBean.setNoOfLines(noOfLines);
			double unitPrice = Util.getUnitPrice(lottoPurchaseBean.getGameId(), lottoPurchaseBean.getPlayType());
			lottoPurchaseBean.setUnitPrice(unitPrice);
			dgeTicketPrice = noOfLines * unitPrice * lottoPurchaseBean.getNoOfDraws();
			// rouding
			lmsTicketPrice = CommonMethods.roundDrawTktAmt(dgeTicketPrice);

			lmsTicketPrice = CommonMethods.fmtToTwoDecimal(lmsTicketPrice);

			logger.debug("Dge Purchase Amount - " + dgeTicketPrice);
			logger.debug("LMS Purchase Amount - " + lmsTicketPrice);

			lottoPurchaseBean.setTotalPurchaseAmt(dgeTicketPrice);

			logger.debug("Inside  FE1*******");
			// rg calling
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", lmsTicketPrice + "", con);
			if (!isFraud) {
				balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, lottoPurchaseBean.getGameId(),
						lmsTicketPrice, lottoPurchaseBean.getPlrMobileNumber(), con);

				logger.debug(
						"Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :" + lmsTicketPrice);

				if (balDed > 0) {
					logger.debug("in keno if----------------");
					lottoPurchaseBean.setRefTransId(balDed + "");
					con.commit();
				} else {
					status = getStatusIfErrorInTransaction(status, balDed);
					lottoPurchaseBean.setSaleStatus(status);
					return lottoPurchaseBean;
				}
			} else {
				logger.debug("Responsing Gaming not allowed to sale");
				lottoPurchaseBean.setSaleStatus("FRAUD");
				return lottoPurchaseBean;
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(con);
		}

		try {
			lottoPurchaseBean.setRefTransId(balDed + "");
			requestBean.setDrawIdTableMap(lottoPurchaseBean.getDrawIdTableMap());
			requestBean.setDrawDateTime(lottoPurchaseBean.getDrawDateTime());
			requestBean.setGame_no(lottoPurchaseBean.getGame_no());
			requestBean.setGameId(lottoPurchaseBean.getGameId());
			requestBean.setIsAdvancedPlay(lottoPurchaseBean.getIsAdvancedPlay());
			requestBean.setNoOfDraws(lottoPurchaseBean.getNoOfDraws());
			requestBean.setNoPicked(lottoPurchaseBean.getNoPicked());
			requestBean.setPartyId(lottoPurchaseBean.getPartyId());
			requestBean.setPartyType(lottoPurchaseBean.getPartyType());
			requestBean.setPlayerData(lottoPurchaseBean.getPickedNumbers());
			requestBean.setPickedNumbers(lottoPurchaseBean.getPickedNumbers());
			requestBean.setPlayType(lottoPurchaseBean.getPlayType());
			requestBean.setPurchaseChannel(lottoPurchaseBean.getPurchaseChannel());
			requestBean.setRefMerchantId(lottoPurchaseBean.getRefMerchantId());
			requestBean.setRefTransId(lottoPurchaseBean.getRefTransId());
			requestBean.setUserId(lottoPurchaseBean.getUserId());
			requestBean.setUserMappingId(lottoPurchaseBean.getUserMappingId());
			requestBean.setServiceId(lottoPurchaseBean.getServiceId());
			requestBean.setPicknumbers(lottoPurchaseBean.getPicknumbers());
			requestBean.setPromotkt(lottoPurchaseBean.isPromotkt());
			requestBean.setUnitPrice(lottoPurchaseBean.getUnitPrice());
			requestBean.setTotalPurchaseAmt(lottoPurchaseBean.getTotalPurchaseAmt());

			String responseString = delegate.getResponseString(sReq);
			LottoResponseBean responseBean = new Gson().fromJson(responseString, LottoResponseBean.class);

			if (responseBean.getIsSuccess()) {
				lottoPurchaseBean.setSaleStatus(responseBean.getSaleStatus());
				lottoPurchaseBean.setTicket_no(responseBean.getTicketNo());
				lottoPurchaseBean.setBarcodeCount(responseBean.getBarcodeCount());
				lottoPurchaseBean.setNoOfDraws(responseBean.getNoOfDraws());
				lottoPurchaseBean.setPurchaseTime(responseBean.getPurchaseTime());
				lottoPurchaseBean.setReprintCount(responseBean.getReprintCount());
				lottoPurchaseBean.setPlayerPicked(responseBean.getPlayerPicked());
				lottoPurchaseBean.setTotalPurchaseAmt(responseBean.getTotalPurchaseAmt());
				lottoPurchaseBean.setDrawDateTime(responseBean.getDrawDateTime());
				lottoPurchaseBean.setIsQuickPick(responseBean.getIsQuickPick());
				lottoPurchaseBean.setPanel_id(responseBean.getPanel_id());
				// lottoPurchaseBean = new Gson().fromJson(responseString,
				// elementType);
				double fromDgeTicketPrice = lottoPurchaseBean.getTotalPurchaseAmt();

				// rouding

				lottoPurchaseBean.setTotalPurchaseAmt(lmsTicketPrice);

				logger.debug(
						"Total Purchase Amt inside DrawGameRPOSHelper After getting Success :" + fromDgeTicketPrice);
				con = getConnectionObject();
				if (fromDgeTicketPrice != dgeTicketPrice) {
					fromDgeTicketPrice = CommonMethods.roundDrawTktAmt(fromDgeTicketPrice);
					lottoPurchaseBean.setTotalPurchaseAmt(fromDgeTicketPrice);
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
							lottoPurchaseBean.getGameId(), fromDgeTicketPrice, lmsTicketPrice, balDed, con);

				}
				int tickUpd = 1;// orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,lottoPurchaseBean.getTicket_no(),lottoPurchaseBean.getGameId(),
								// userBean,lottoPurchaseBean.getPurchaseChannel(),con);

				if (tickUpd == 1) {
					AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
					ajxHelper.getAvlblCreditAmt(userBean, con);
					status = "SUCCESS";
					lottoPurchaseBean.setSaleStatus(status);
					lottoPurchaseBean.setAdvMsg(
							Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGameId()));

					if (!"applet".equals(lottoPurchaseBean.getBarcodeType())) {
						IDBarcode.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
					}
					con.commit();
					// call promo purchase process here
					List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
					// CommonFunctionsHelper commonHelper = new
					// CommonFunctionsHelper();

					List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGameId());
					// String gameType =
					// getGameType(fortunePurchaseBean.getGame_no());
					// List<PromoGameBean> promoGameslist =
					// commonHelper.getAvailablePromoGames(gameType);
					boolean isLoopAgain = true;
					for (int i = 0; i < promoGameslist.size(); i++) {
						PromoGameBean promobean = promoGameslist.get(i);
						List<LottoPurchaseBean> lottoBeanList = new ArrayList<LottoPurchaseBean>();
						for (int k = 0; k < promobean.getNoOfPromoTickets(); k++) {
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, lottoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									lottoPurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
											lottoPurchaseBean.getPurchaseChannel(),
											lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
											lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
											lottoPurchaseBean.getRefMerchantId(), userBean,
											lottoPurchaseBean.getRefTransId());
									lottoPurchaseBean.setSaleStatus("FAILED");
									return lottoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							} /*
								 * else if (promobean.getPromoGameName().
								 * equalsIgnoreCase( "Zimlottobonusfree")) { //
								 * call winfast purchase process
								 * 
								 * if(isLoopAgain){ int gameNo =
								 * promobean.getPromoGameNo(); int gameId =
								 * Util.getGameIdFromGameNumber(gameNo);
								 * LottoPurchaseBean drawGamePurchaseBean = new
								 * LottoPurchaseBean();
								 * //drawGamePurchaseBean.setIsQP(1);
								 * //drawGamePurchaseBean.setTotalNoOfPanels(1);
								 * // drawGamePurchaseBean.setSymbols(symbols);
								 * //drawGamePurchaseBean.setNoOfPanels("1");
								 * 
								 * // drawGamePurchaseBean.setBetAmountMultiple(
								 * betAmountMultiple);
								 * drawGamePurchaseBean.setDrawIdTableMap(
								 * lottoPurchaseBean .getDrawIdTableMap());
								 * drawGamePurchaseBean.setGame_no(gameNo);
								 * drawGamePurchaseBean.setGameId(gameId);
								 * drawGamePurchaseBean.setGameDispName(Util
								 * .getGameMap().get(gameId).getGameName()); //
								 * drawGamePurchaseBean.setIsQuickPick(
								 * isQuickPickNew);
								 * drawGamePurchaseBean.setNoOfDraws(promobean.
								 * getNoOfDraws());
								 * drawGamePurchaseBean.setPartyId(userBean.
								 * getUserOrgId());
								 * drawGamePurchaseBean.setPartyType(userBean.
								 * getUserType());
								 * drawGamePurchaseBean.setUserId(userBean.
								 * getUserId());
								 * drawGamePurchaseBean.setRefMerchantId(
								 * lottoPurchaseBean .getRefMerchantId());
								 * drawGamePurchaseBean.setPurchaseChannel(
								 * lottoPurchaseBean .getPurchaseChannel());
								 * drawGamePurchaseBean.setPromotkt(true);
								 * drawGamePurchaseBean.setNoPicked(
								 * lottoPurchaseBean.getNoPicked());
								 * drawGamePurchaseBean.setServiceId(
								 * lottoPurchaseBean.getServiceId());
								 * drawGamePurchaseBean.setUserMappingId(
								 * lottoPurchaseBean.getUserMappingId());
								 * drawGamePurchaseBean.setPlrMobileNumber(
								 * lottoPurchaseBean.getPlrMobileNumber());
								 * StringBuilder sb=new StringBuilder("QP");
								 * if("Direct6".equalsIgnoreCase(
								 * lottoPurchaseBean.getPlayType())){ for(int
								 * m=1;m<lottoPurchaseBean.getNoOfLines()*
								 * promobean.getNoOfPromoTickets();m++){
								 * sb.append("NxtQP"); } }else
								 * if("Perm6".equalsIgnoreCase(lottoPurchaseBean
								 * .getPlayType())){ for(int
								 * m=1;m<promobean.getNoOfPromoTickets();m++){
								 * sb.append("NxtQP"); } } String
								 * pickedNumbers=sb.toString();isDrawAvailable(
								 * lottoPurchaseBean.getGameId()) String[]
								 * picknumbers = pickedNumbers.split("Nxt");
								 * drawGamePurchaseBean.setPickedNumbers(
								 * pickedNumbers);
								 * drawGamePurchaseBean.setPicknumbers(
								 * picknumbers);
								 * 
								 * drawGamePurchaseBean.setPlayType(
								 * lottoPurchaseBean.getPlayType()); // if
								 * (drawIdArr != null) { //
								 * drawGamePurchaseBean.setDrawDateTime(Arrays.
								 * asList(drawIdArr)); // }
								 * drawGamePurchaseBean.setIsAdvancedPlay(0);
								 * LottoPurchaseBean lottoBean =
								 * ZimLottoBonusFreeHelper.
								 * zimLottoBonusFreePurchaseTicket( userBean,
								 * drawGamePurchaseBean, lottoPurchaseBean); if
								 * ("SUCCESS".equalsIgnoreCase(lottoBean.
								 * getSaleStatus())) {
								 * orgOnLineSaleCreditUpdation
								 * .drawTicketNPromoMappigUpdate(1,
								 * lottoPurchaseBean .getGame_no(),
								 * lottoPurchaseBean .getTicket_no(),
								 * lottoBean.getTicket_no()); } else {
								 * lottoPurchaseBean.setPromoSaleStatus("FAILED"
								 * ); return lottoPurchaseBean; }
								 * 
								 * lottoBeanList.add(lottoBean); logger.
								 * debug("Promo Bean Data for zim lotto Three"
								 * +lottoBean); //
								 * kenoPurchaseBean.setFortunePurchaseBean(
								 * fortuneBean);
								 * lottoPurchaseBean.setPromoPurchaseBeanList(
								 * lottoBeanList); isLoopAgain=false;
								 * 
								 * } if("Direct6".equalsIgnoreCase(
								 * lottoPurchaseBean.getPlayType()))
								 * isLoopAgain=false; }
								 */
						}
					}
					lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);

					return lottoPurchaseBean;
				} else {
					status = "FAILED";
					lottoPurchaseBean.setSaleStatus(status);
					// Code for cancelling the Ticket to be send to
					// Draw
					// Game Engine
					cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
							lottoPurchaseBean.getPurchaseChannel(), lottoPurchaseBean.getDrawIdTableMap(),
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
							lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
							lottoPurchaseBean.getRefTransId());

					return lottoPurchaseBean;
				}
			} else {
				lottoPurchaseBean.setSaleStatus(responseBean.getSaleStatus());
				// lottoPurchaseBean = new Gson().fromJson(responseString,
				// elementType);
				if (lottoPurchaseBean.getSaleStatus() == null) {
					lottoPurchaseBean.setSaleStatus("FAILED");// Error
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
							balDed);
					return lottoPurchaseBean;
				} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
							balDed);
					return lottoPurchaseBean;
				} else {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
							balDed);
					return lottoPurchaseBean;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// lottoPurchaseBean = new Gson().fromJson(responseString,
			// elementType);
			if (lottoPurchaseBean.getSaleStatus() == null) {
				lottoPurchaseBean.setSaleStatus("FAILED");// Error
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
						balDed);
				return lottoPurchaseBean;
			} else {
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
						balDed);
				return lottoPurchaseBean;
			}

		} finally {
			DBConnect.closeCon(con);
		}
	}

	public LottoPurchaseBean zimLottoBonusPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {
		Connection connection = null;
		try {
			if (!checkDrawAvailabilityLotto(lottoPurchaseBean) || !validateZimLottoBonusData(lottoPurchaseBean)) {
				return lottoPurchaseBean;
			}
			lottoPurchaseBean.setSaleStatus("FAILED");
			connection = getConnectionObject();
			calculateNoOfLines(lottoPurchaseBean);
			if (calculateNoOfLines(lottoPurchaseBean) == -1) {
				return lottoPurchaseBean;
			}
			lottoPurchaseBean
					.setUnitPrice(Util.getUnitPrice(lottoPurchaseBean.getGameId(), lottoPurchaseBean.getPlayType()));
			totalPurchaseAmount = calculateTotalPurchaseAmount(lottoPurchaseBean);
			logger.debug("Inside  FE1*******");
			// Responsible Gaming calling
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totalPurchaseAmount + "", connection);
			if (isFraud) {
				logger.debug("Responsing Gaming not allowed to sale");
				lottoPurchaseBean.setSaleStatus("FRAUD");
				return lottoPurchaseBean;
			}
			if (!getSaleBalanceDeduction(userBean, lottoPurchaseBean, connection)) {
				return lottoPurchaseBean;
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(connection);
		}

		try {
			String responseString = prepareZimLottoBonusServiceResponse(lottoPurchaseBean);
			if (serviceResponse.getIsSuccess()) {
				return processSuccessfulServiceResponse(userBean, responseString);
			} else {
				lottoPurchaseBean = new Gson().fromJson(responseString, new TypeToken<LottoPurchaseBean>() {
				}.getType());
				updateSaleStatusWhenServiceResponseIsFalse(userBean, lottoPurchaseBean);
				return lottoPurchaseBean;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lottoPurchaseBean;
	}

	/**
	 * 
	 * @param userBean
	 * @param responseString
	 * @return
	 * @throws SQLException
	 * @throws LMSException
	 * @throws Exception
	 */
	private LottoPurchaseBean processSuccessfulServiceResponse(UserInfoBean userBean, String responseString)
			throws SQLException, LMSException, Exception {
		Connection serviceConnection = getConnectionObject();
		LottoPurchaseBean lottoPurchaseBean = new Gson().fromJson(responseString, new TypeToken<LottoPurchaseBean>() {
		}.getType());
		try {
			// LottoPurchaseBean lottoPurchaseBean;
			modifiedTotalPurchaseAmount = lottoPurchaseBean.getTotalPurchaseAmt();
			// rounding
			logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
					+ modifiedTotalPurchaseAmount);
			if (oldTotalPurchaseAmount != modifiedTotalPurchaseAmount) {
				updateSaleBalanceDeduction(userBean, lottoPurchaseBean, serviceConnection);
			}
			int ticketUpdate = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balanceDeduction,
					lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGameId(), userBean,
					lottoPurchaseBean.getPurchaseChannel(), serviceConnection);
			if (ticketUpdate == 1) {
				processSuccessfulTicketUpdate(userBean, lottoPurchaseBean, serviceConnection);
			} else {
				lottoPurchaseBean = cancelTicket(userBean, lottoPurchaseBean);
				return lottoPurchaseBean;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBConnect.closeCon(serviceConnection);
		}
		return lottoPurchaseBean;
	}

	/**
	 * 
	 * @param userBean
	 * @param lottoPurchaseBean
	 * @param connection2
	 * @throws SQLException
	 * @throws LMSException
	 * @throws Exception
	 */
	private void processSuccessfulTicketUpdate(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean,
			Connection connection2) throws SQLException, LMSException, Exception {
		ajaxHelper.getAvlblCreditAmt(userBean, connection2);
		status = "SUCCESS";
		lottoPurchaseBean.setSaleStatus(status);
		lottoPurchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGameId()));
		if (!"applet".equals(lottoPurchaseBean.getBarcodeType())) {
			IDBarcode.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
		}
		connection2.commit();
		// call promo purchase process here
		lottoPurchaseBean = promoPurchaseTicket(userBean, lottoPurchaseBean);
	}

	/**
	 * 
	 * @param lottoPurchaseBean
	 * @return
	 */
	private boolean validateZimLottoBonusData(LottoPurchaseBean lottoPurchaseBean) {
		if (!DrawGameValidation.zimLottoBonusDataValidation(lottoPurchaseBean)) {
			logger.debug("Data validation returns false");
			lottoPurchaseBean.setSaleStatus("FAILED");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param lottoPurchaseBean
	 * @return
	 */
	private boolean checkDrawAvailabilityLotto(LottoPurchaseBean lottoPurchaseBean) {
		if (isDrawAvailable(lottoPurchaseBean.getGameId()) || chkFreezeTimeSale(lottoPurchaseBean.getGameId())) {
			lottoPurchaseBean.setSaleStatus("NO_DRAWS");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param userBean
	 * @param lottoPurchaseBean
	 */
	private void updateSaleStatusWhenServiceResponseIsFalse(UserInfoBean userBean,
			LottoPurchaseBean lottoPurchaseBean) {
		if (lottoPurchaseBean.getSaleStatus() == null) {
			lottoPurchaseBean.setSaleStatus("FAILED");// Error
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
					balanceDeduction);
		} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
					balanceDeduction);
		} else {
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
					balanceDeduction);
		}
	}

	/**
	 * 
	 * @param userBean
	 * @param lottoPurchaseBean
	 * @return
	 * @throws LMSException
	 */
	private LottoPurchaseBean cancelTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws LMSException {
		status = "FAILED";
		lottoPurchaseBean.setSaleStatus(status);
		// Code for cancelling the Ticket to be send to Draw Game Engine
		cancelTicket(lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getPurchaseChannel(),
				lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
				lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
				lottoPurchaseBean.getRefTransId());

		return lottoPurchaseBean;
	}

	/**
	 * 
	 * @param userBean
	 * @param lottoPurchaseBean
	 * @param connection
	 */
	private void updateSaleBalanceDeduction(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean,
			Connection connection) {
		lottoPurchaseBean.setRefTransId(balanceDeduction + "");
		lottoPurchaseBean.setTotalPurchaseAmt(modifiedTotalPurchaseAmount);
		balanceDeduction = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
				lottoPurchaseBean.getGameId(), modifiedTotalPurchaseAmount, oldTotalPurchaseAmount, balanceDeduction,
				connection);
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnectionObject() throws SQLException {
		Connection connection;
		connection = DBConnect.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}

	/**
	 * 
	 * @param lottoPurchaseBean
	 * @return
	 */
	private String prepareZimLottoBonusServiceResponse(LottoPurchaseBean lottoPurchaseBean) {
		serviceRequest.setServiceName(ServiceName.ZIMLOTTOBONUS_MGMT);
		serviceRequest.setServiceMethod(ServiceMethodName.ZIMLOTTOBONUS_PURCHASE_TICKET);
		serviceRequest.setServiceData(lottoPurchaseBean);
		serviceResponse = delegate.getResponse(serviceRequest);
		String responseString = serviceResponse.getResponseData().toString();
		return responseString;
	}

	/**
	 * 
	 * @param userBean
	 * @param lottoPurchaseBean
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private boolean getSaleBalanceDeduction(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean,
			Connection connection) throws SQLException {

		balanceDeduction = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean,
				lottoPurchaseBean.getGameId(), lottoPurchaseBean.getTotalPurchaseAmt(),
				lottoPurchaseBean.getPlrMobileNumber(), connection);
		oldTotalPurchaseAmount = lottoPurchaseBean.getTotalPurchaseAmt();

		logger.debug(
				"Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :" + oldTotalPurchaseAmount);

		if (balanceDeduction > 0) {
			logger.debug("in keno if----------------");
			lottoPurchaseBean.setRefTransId(balanceDeduction + "");
			connection.commit();
		} else {
			if (balanceDeduction == -1) {
				status = "AGT_INS_BAL";// Agent has insufficient Balance
			} else if (balanceDeduction == -2) {
				status = "FAILED";// Error LMS
			} else if (balanceDeduction == 0) {
				status = "RET_INS_BAL";// Retailer has insufficient Balance
			}
			lottoPurchaseBean.setSaleStatus(status);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param lottoPurchaseBean
	 * @return
	 */
	private double calculateTotalPurchaseAmount(LottoPurchaseBean lottoPurchaseBean) {
		double totalPurchaseAmount = 0.0;
		double unitPrice = lottoPurchaseBean.getUnitPrice();
		int noOfLines = lottoPurchaseBean.getNoOfLines();
		if ("LMS_WEB".equalsIgnoreCase(lottoPurchaseBean.getPurchaseChannel())) {
			totalPurchaseAmount = noOfLines * unitPrice * lottoPurchaseBean.getNoOfDraws()
					* lottoPurchaseBean.getBetAmtMultiple();
		} else {
			totalPurchaseAmount = noOfLines * unitPrice * lottoPurchaseBean.getNoOfDraws();
		}
		totalPurchaseAmount = CommonMethods.fmtToTwoDecimal(totalPurchaseAmount);

		logger.debug("Total Purchase Amount - " + totalPurchaseAmount);

		lottoPurchaseBean.setTotalPurchaseAmt(totalPurchaseAmount);
		return totalPurchaseAmount;
	}

	/**
	 * 
	 * @param lottoPurchaseBean
	 * @return
	 */
	private int calculateNoOfLines(LottoPurchaseBean lottoPurchaseBean) {
		int noOfLines = lottoPurchaseBean.getPicknumbers().length;
		if ("Perm6".equals(lottoPurchaseBean.getPlayType())) {
			int n = lottoPurchaseBean.getNoPicked();
			if (n < 7) {
				lottoPurchaseBean.setSaleStatus("FAILED");
				return -1;
			}
			noOfLines = (n * (n - 1) * (n - 2) * (n - 3) * (n - 4) * (n - 5)) / 720;
		}
		lottoPurchaseBean.setNoOfLines(noOfLines);
		return noOfLines;
	}

	/**
	 * 
	 * @param userBean
	 * @param lottoPurchaseBean
	 * @return
	 * @throws LMSException
	 * @throws SQLException
	 * @throws Exception
	 */
	private LottoPurchaseBean promoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws LMSException, SQLException, Exception {
		String status;
		List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
		List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGameId());

		boolean isLoopAgain = true;
		for (int i = 0; i < promoGameslist.size(); i++) {
			PromoGameBean promobean = promoGameslist.get(i);
			List<LottoPurchaseBean> lottoBeanList = new ArrayList<LottoPurchaseBean>();
			for (int k = 0; k < promobean.getNoOfPromoTickets(); k++) {
				if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
					lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
					RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(userBean,
							lottoPurchaseBean, true);
					rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
					if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
						status = "FAILED";
						lottoPurchaseBean.setSaleStatus(status);
						// Code for cancelling the Ticket to
						// be send to Draw Game Engine
						cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
								lottoPurchaseBean.getPurchaseChannel(), lottoPurchaseBean.getDrawIdTableMap(),
								lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
								lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
								lottoPurchaseBean.getRefTransId());
						lottoPurchaseBean.setSaleStatus("FAILED");
						return lottoPurchaseBean;
					} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
						// here insert entry into the promo
						// ticket mapping table
						int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
						orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, rafflePurchaseBean.getGame_no(),
								rafflePurchaseBean.getParentTktNo(),
								rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
						rafflePurchaseBeanList.add(rafflePurchaseBean);
					}
				} else if (promobean.getPromoGameName().equalsIgnoreCase("Zimlottobonusfree")) {
					// call winfast purchase process

					if (isLoopAgain) {
						int gameNo = promobean.getPromoGameNo();
						int gameId = Util.getGameIdFromGameNumber(gameNo);
						LottoPurchaseBean drawGamePurchaseBean = new LottoPurchaseBean();
						// drawGamePurchaseBean.setIsQP(1);
						// drawGamePurchaseBean.setTotalNoOfPanels(1);
						// drawGamePurchaseBean.setSymbols(symbols);
						// drawGamePurchaseBean.setNoOfPanels("1");
						// drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
						drawGamePurchaseBean.setDrawIdTableMap(lottoPurchaseBean.getDrawIdTableMap());
						drawGamePurchaseBean.setGame_no(gameNo);
						drawGamePurchaseBean.setGameId(gameId);
						drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameNo));
						// drawGamePurchaseBean.setIsQuickPick(isQuickPickNew);
						drawGamePurchaseBean.setNoOfDraws(promobean.getNoOfDraws());
						drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
						drawGamePurchaseBean.setPartyType(userBean.getUserType());
						drawGamePurchaseBean.setUserId(userBean.getUserId());
						drawGamePurchaseBean.setRefMerchantId(lottoPurchaseBean.getRefMerchantId());
						drawGamePurchaseBean.setPurchaseChannel(lottoPurchaseBean.getPurchaseChannel());
						drawGamePurchaseBean.setPromotkt(true);
						drawGamePurchaseBean.setNoPicked(lottoPurchaseBean.getNoPicked());
						drawGamePurchaseBean.setServiceId(lottoPurchaseBean.getServiceId());
						drawGamePurchaseBean.setUserMappingId(lottoPurchaseBean.getUserMappingId());
						drawGamePurchaseBean.setPlrMobileNumber(lottoPurchaseBean.getPlrMobileNumber());
						StringBuilder sb = new StringBuilder("QP");
						if ("Direct6".equalsIgnoreCase(lottoPurchaseBean.getPlayType())) {
							for (int m = 1; m < lottoPurchaseBean.getNoOfLines()
									* promobean.getNoOfPromoTickets(); m++) {
								sb.append("NxtQP");
							}
						} else if ("Perm6".equalsIgnoreCase(lottoPurchaseBean.getPlayType())) {
							for (int m = 1; m < promobean.getNoOfPromoTickets(); m++) {
								sb.append("NxtQP");
							}
						}
						String pickedNumbers = sb.toString();
						String[] picknumbers = pickedNumbers.split("Nxt");
						drawGamePurchaseBean.setPickedNumbers(pickedNumbers);
						drawGamePurchaseBean.setPicknumbers(picknumbers);

						drawGamePurchaseBean.setPlayType(lottoPurchaseBean.getPlayType());
						// if (drawIdArr != null) {
						// drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
						// }
						drawGamePurchaseBean.setIsAdvancedPlay(0);
						LottoPurchaseBean lottoBean = ZimLottoBonusFreeHelper.zimLottoBonusFreePurchaseTicket(userBean,
								drawGamePurchaseBean);
						if ("SUCCESS".equalsIgnoreCase(lottoBean.getSaleStatus())) {
							orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1, lottoPurchaseBean.getGame_no(),
									lottoPurchaseBean.getTicket_no(), lottoBean.getTicket_no());
						} else {
							lottoPurchaseBean.setPromoSaleStatus("FAILED");
							return lottoPurchaseBean;
						}

						lottoBeanList.add(lottoBean);
						logger.debug("Promo Bean Data for zim lotto Three" + lottoBean);
						// kenoPurchaseBean.setFortunePurchaseBean(fortuneBean);
						lottoPurchaseBean.setPromoPurchaseBeanList(lottoBeanList);
						isLoopAgain = false;

					}
					if ("Direct6".equalsIgnoreCase(lottoPurchaseBean.getPlayType()))
						isLoopAgain = false;
				}
			}
		}

		lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);

		return lottoPurchaseBean;
	}

	public LottoPurchaseBean zimLottoBonusTwoPurchaseTicket(UserInfoBean userBean, LottoPurchaseBean lottoPurchaseBean)
			throws Exception {

		// ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.ZIMLOTTOBONUSTWO_MGMT);
		sReq.setServiceMethod(ServiceMethodName.ZIMLOTTOBONUSTWO_PURCHASE_TICKET);
		// sReq.setServiceData(lottoPurchaseBean);
		LottoRequestBean requestBean = new LottoRequestBean();
		sReq.setServiceData(requestBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		double dgeTicketPrice = 0.0;
		double lmsTicketPrice = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;

		try {

			lottoPurchaseBean.setSaleStatus("FAILED");
			int noOfLines = lottoPurchaseBean.getPicknumbers().length;
			con = getConnectionObject();

			if ("TERMINAL".equals(lottoPurchaseBean.getDeviceType())) {
				int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
				checkLastPrintedTicketStatusAndUpdate(userBean, Long.parseLong(lottoPurchaseBean.getLastSoldTicketNo()),
						lottoPurchaseBean.getDeviceType(), lottoPurchaseBean.getRefMerchantId(), autoCancelHoldDays,
						lottoPurchaseBean.getActionName(), lottoPurchaseBean.getLastGameId(), con);
			}

			if ("Perm6".equals(lottoPurchaseBean.getPlayType())) {
				int n = lottoPurchaseBean.getNoPicked();
				if (n < 7) {
					lottoPurchaseBean.setSaleStatus("FAILED");
					return lottoPurchaseBean;
				}
				noOfLines = (n * (n - 1) * (n - 2) * (n - 3) * (n - 4) * (n - 5)) / 720;
			}
			lottoPurchaseBean.setNoOfLines(noOfLines);
			double unitPrice = Util.getUnitPrice(lottoPurchaseBean.getGameId(), lottoPurchaseBean.getPlayType());
			lottoPurchaseBean.setUnitPrice(unitPrice);
			dgeTicketPrice = noOfLines * unitPrice * lottoPurchaseBean.getNoOfDraws();
			// rouding
			lmsTicketPrice = CommonMethods.roundDrawTktAmt(dgeTicketPrice);

			lmsTicketPrice = CommonMethods.fmtToTwoDecimal(lmsTicketPrice);

			logger.debug("Dge Purchase Amount - " + dgeTicketPrice);
			logger.debug("LMS Purchase Amount - " + lmsTicketPrice);

			lottoPurchaseBean.setTotalPurchaseAmt(dgeTicketPrice);

			logger.debug("Inside  FE1*******");
			// rg calling
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", lmsTicketPrice + "", con);
			if (!isFraud) {
				balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, lottoPurchaseBean.getGameId(),
						lmsTicketPrice, lottoPurchaseBean.getPlrMobileNumber(), con);

				logger.debug(
						"Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :" + lmsTicketPrice);

				if (balDed > 0) {
					logger.debug("in keno if----------------");
					lottoPurchaseBean.setRefTransId(balDed + "");
					con.commit();
				} else {
					status = getStatusIfErrorInTransaction(status, balDed);
					lottoPurchaseBean.setSaleStatus(status);
					return lottoPurchaseBean;
				}
			} else {
				logger.debug("Responsing Gaming not allowed to sale");
				lottoPurchaseBean.setSaleStatus("FRAUD");
				return lottoPurchaseBean;
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(con);
		}

		try {
			lottoPurchaseBean.setRefTransId(balDed + "");
			requestBean.setDrawIdTableMap(lottoPurchaseBean.getDrawIdTableMap());
			requestBean.setDrawDateTime(lottoPurchaseBean.getDrawDateTime());
			requestBean.setGame_no(lottoPurchaseBean.getGame_no());
			requestBean.setGameId(lottoPurchaseBean.getGameId());
			requestBean.setIsAdvancedPlay(lottoPurchaseBean.getIsAdvancedPlay());
			requestBean.setNoOfDraws(lottoPurchaseBean.getNoOfDraws());
			requestBean.setNoPicked(lottoPurchaseBean.getNoPicked());
			requestBean.setPartyId(lottoPurchaseBean.getPartyId());
			requestBean.setPartyType(lottoPurchaseBean.getPartyType());
			requestBean.setPlayerData(lottoPurchaseBean.getPickedNumbers());
			requestBean.setPickedNumbers(lottoPurchaseBean.getPickedNumbers());
			requestBean.setPlayType(lottoPurchaseBean.getPlayType());
			requestBean.setPurchaseChannel(lottoPurchaseBean.getPurchaseChannel());
			requestBean.setRefMerchantId(lottoPurchaseBean.getRefMerchantId());
			requestBean.setRefTransId(lottoPurchaseBean.getRefTransId());
			requestBean.setUserId(lottoPurchaseBean.getUserId());
			requestBean.setUserMappingId(lottoPurchaseBean.getUserMappingId());
			requestBean.setServiceId(lottoPurchaseBean.getServiceId());
			requestBean.setPicknumbers(lottoPurchaseBean.getPicknumbers());
			requestBean.setPromotkt(lottoPurchaseBean.isPromotkt());
			requestBean.setUnitPrice(lottoPurchaseBean.getUnitPrice());
			requestBean.setTotalPurchaseAmt(lottoPurchaseBean.getTotalPurchaseAmt());

			String responseString = delegate.getResponseString(sReq);
			LottoResponseBean responseBean = new Gson().fromJson(responseString, LottoResponseBean.class);

			if (responseBean.getIsSuccess()) {
				lottoPurchaseBean.setSaleStatus(responseBean.getSaleStatus());
				lottoPurchaseBean.setTicket_no(responseBean.getTicketNo());
				lottoPurchaseBean.setBarcodeCount(responseBean.getBarcodeCount());
				lottoPurchaseBean.setNoOfDraws(responseBean.getNoOfDraws());
				lottoPurchaseBean.setPurchaseTime(responseBean.getPurchaseTime());
				lottoPurchaseBean.setReprintCount(responseBean.getReprintCount());
				lottoPurchaseBean.setPlayerPicked(responseBean.getPlayerPicked());
				lottoPurchaseBean.setTotalPurchaseAmt(responseBean.getTotalPurchaseAmt());
				lottoPurchaseBean.setDrawDateTime(responseBean.getDrawDateTime());
				lottoPurchaseBean.setIsQuickPick(responseBean.getIsQuickPick());
				lottoPurchaseBean.setPanel_id(responseBean.getPanel_id());
				// lottoPurchaseBean = new Gson().fromJson(responseString,
				// elementType);
				double fromDgeTicketPrice = lottoPurchaseBean.getTotalPurchaseAmt();

				// rouding

				lottoPurchaseBean.setTotalPurchaseAmt(lmsTicketPrice);

				logger.debug(
						"Total Purchase Amt inside DrawGameRPOSHelper After getting Success :" + fromDgeTicketPrice);
				con = getConnectionObject();
				if (fromDgeTicketPrice != dgeTicketPrice) {
					fromDgeTicketPrice = CommonMethods.roundDrawTktAmt(fromDgeTicketPrice);
					lottoPurchaseBean.setTotalPurchaseAmt(fromDgeTicketPrice);
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
							lottoPurchaseBean.getGameId(), fromDgeTicketPrice, lmsTicketPrice, balDed, con);

				}
				int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,
						lottoPurchaseBean.getTicket_no(), lottoPurchaseBean.getGameId(), userBean,
						lottoPurchaseBean.getPurchaseChannel(), con);

				if (tickUpd == 1) {
					AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
					ajxHelper.getAvlblCreditAmt(userBean, con);
					status = "SUCCESS";
					lottoPurchaseBean.setSaleStatus(status);
					lottoPurchaseBean.setAdvMsg(
							Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGameId()));

					if (!"applet".equals(lottoPurchaseBean.getBarcodeType())) {
						IDBarcode.getBarcode(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount());
					}
					con.commit();
					// call promo purchase process here
					List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
					// CommonFunctionsHelper commonHelper = new
					// CommonFunctionsHelper();

					List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGameId());
					// String gameType =
					// getGameType(fortunePurchaseBean.getGame_no());
					// List<PromoGameBean> promoGameslist =
					// commonHelper.getAvailablePromoGames(gameType);
					boolean isLoopAgain = true;
					for (int i = 0; i < promoGameslist.size(); i++) {
						PromoGameBean promobean = promoGameslist.get(i);
						List<LottoPurchaseBean> lottoBeanList = new ArrayList<LottoPurchaseBean>();
						for (int k = 0; k < promobean.getNoOfPromoTickets(); k++) {
							if (promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")) {
								lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
								RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) rafflePurchaseTicket(
										userBean, lottoPurchaseBean, true);
								rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
								if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("FAILED")) {
									status = "FAILED";
									lottoPurchaseBean.setSaleStatus(status);
									// Code for cancelling the Ticket to
									// be send to Draw Game Engine
									cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
											lottoPurchaseBean.getPurchaseChannel(),
											lottoPurchaseBean.getDrawIdTableMap(), lottoPurchaseBean.getGame_no(),
											lottoPurchaseBean.getPartyId(), lottoPurchaseBean.getPartyType(),
											lottoPurchaseBean.getRefMerchantId(), userBean,
											lottoPurchaseBean.getRefTransId());
									lottoPurchaseBean.setSaleStatus("FAILED");
									return lottoPurchaseBean;
								} else if (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")) {
									// here insert entry into the promo
									// ticket mapping table
									int tktlen = rafflePurchaseBean.getRaffleTicket_no().length();
									orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
											rafflePurchaseBean.getGame_no(), rafflePurchaseBean.getParentTktNo(),
											rafflePurchaseBean.getRaffleTicket_no().substring(0, tktlen - 2));
									rafflePurchaseBeanList.add(rafflePurchaseBean);
								}
							} else if (promobean.getPromoGameName().equalsIgnoreCase("Zimlottobonustwofree")) {
								// call winfast purchase process

								if (isLoopAgain) {
									int gameNo = promobean.getPromoGameNo();
									int gameId = Util.getGameIdFromGameNumber(gameNo);
									LottoPurchaseBean drawGamePurchaseBean = new LottoPurchaseBean();
									// drawGamePurchaseBean.setIsQP(1);
									// drawGamePurchaseBean.setTotalNoOfPanels(1);
									// drawGamePurchaseBean.setSymbols(symbols);
									// drawGamePurchaseBean.setNoOfPanels("1");

									// drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
									drawGamePurchaseBean.setDrawIdTableMap(lottoPurchaseBean.getDrawIdTableMap());
									drawGamePurchaseBean.setGame_no(gameNo);
									drawGamePurchaseBean.setGameId(gameId);
									drawGamePurchaseBean.setGameDispName(Util.getGameMap().get(gameId).getGameName());
									// drawGamePurchaseBean.setIsQuickPick(isQuickPickNew);
									drawGamePurchaseBean.setNoOfDraws(promobean.getNoOfDraws());
									drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
									drawGamePurchaseBean.setPartyType(userBean.getUserType());
									drawGamePurchaseBean.setUserId(userBean.getUserId());
									drawGamePurchaseBean.setRefMerchantId(lottoPurchaseBean.getRefMerchantId());
									drawGamePurchaseBean.setPurchaseChannel(lottoPurchaseBean.getPurchaseChannel());
									drawGamePurchaseBean.setPromotkt(true);
									drawGamePurchaseBean.setNoPicked(lottoPurchaseBean.getNoPicked());
									drawGamePurchaseBean.setServiceId(lottoPurchaseBean.getServiceId());
									drawGamePurchaseBean.setUserMappingId(lottoPurchaseBean.getUserMappingId());
									drawGamePurchaseBean.setPlrMobileNumber(lottoPurchaseBean.getPlrMobileNumber());
									StringBuilder sb = new StringBuilder("QP");
									if ("Direct6".equalsIgnoreCase(lottoPurchaseBean.getPlayType())) {
										for (int m = 1; m < lottoPurchaseBean.getNoOfLines()
												* promobean.getNoOfPromoTickets(); m++) {
											sb.append("NxtQP");
										}
									} else if ("Perm6".equalsIgnoreCase(lottoPurchaseBean.getPlayType())) {
										for (int m = 1; m < promobean.getNoOfPromoTickets(); m++) {
											sb.append("NxtQP");
										}
									}
									String pickedNumbers = sb.toString();
									String[] picknumbers = pickedNumbers.split("Nxt");
									drawGamePurchaseBean.setPickedNumbers(pickedNumbers);
									drawGamePurchaseBean.setPicknumbers(picknumbers);

									drawGamePurchaseBean.setPlayType(lottoPurchaseBean.getPlayType());
									// if (drawIdArr != null) {
									// drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
									// }
									drawGamePurchaseBean.setIsAdvancedPlay(0);
									LottoPurchaseBean lottoBean = ZimLottoBonusTwoFreeHelper
											.zimLottoBonusTwoFreePurchaseTicket(userBean, drawGamePurchaseBean,
													lottoPurchaseBean);
									if ("SUCCESS".equalsIgnoreCase(lottoBean.getSaleStatus())) {
										orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
												lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getTicket_no(),
												lottoBean.getTicket_no());
									} else {
										lottoPurchaseBean.setPromoSaleStatus("FAILED");
										return lottoPurchaseBean;
									}

									lottoBeanList.add(lottoBean);
									logger.debug("Promo Bean Data for zim lotto Three" + lottoBean);
									// kenoPurchaseBean.setFortunePurchaseBean(fortuneBean);
									lottoPurchaseBean.setPromoPurchaseBeanList(lottoBeanList);
									isLoopAgain = false;

								}
								if ("Direct6".equalsIgnoreCase(lottoPurchaseBean.getPlayType()))
									isLoopAgain = false;
							}
						}
					}
					lottoPurchaseBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);

					return lottoPurchaseBean;
				} else {
					status = "FAILED";
					lottoPurchaseBean.setSaleStatus(status);
					// Code for cancelling the Ticket to be send to
					// Draw
					// Game Engine
					cancelTicket(lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount(),
							lottoPurchaseBean.getPurchaseChannel(), lottoPurchaseBean.getDrawIdTableMap(),
							lottoPurchaseBean.getGame_no(), lottoPurchaseBean.getPartyId(),
							lottoPurchaseBean.getPartyType(), lottoPurchaseBean.getRefMerchantId(), userBean,
							lottoPurchaseBean.getRefTransId());

					return lottoPurchaseBean;
				}
			} else {
				lottoPurchaseBean.setSaleStatus(responseBean.getSaleStatus());
				// lottoPurchaseBean = new Gson().fromJson(responseString,
				// elementType);
				if (lottoPurchaseBean.getSaleStatus() == null) {
					lottoPurchaseBean.setSaleStatus("FAILED");// Error
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
							balDed);
					return lottoPurchaseBean;
				} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())) {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
							balDed);
					return lottoPurchaseBean;
				} else {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
							balDed);
					return lottoPurchaseBean;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// lottoPurchaseBean = new Gson().fromJson(responseString,
			// elementType);
			if (lottoPurchaseBean.getSaleStatus() == null) {
				lottoPurchaseBean.setSaleStatus("FAILED");// Error
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
						balDed);
				return lottoPurchaseBean;
			} else {
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, lottoPurchaseBean.getGameId(), "FAILED",
						balDed);
				return lottoPurchaseBean;
			}

		} finally {
			DBConnect.closeCon(con);
		}
	}

	public static String fetchCurrentVersion(String userName) { // For Embedded
		// only...
		logger.debug("in fetchCurrentVersion() method");
		String version = null;
		Connection con = DBConnect.getConnection();
		try {
			String query = "SELECT rom.current_version FROM st_lms_ret_offline_master rom, st_lms_user_master um WHERE um.user_id = rom.user_id AND um.user_name = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				version = rs.getString("current_version");
			}
		} catch (Exception ex) {
			logger.debug(ex);
			ex.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return version;
	}

	public static String embdDgData(boolean isOffline, Map<Integer, Map<Integer, String>> drawIdTableMap, int userId,
			int orgId, double version) throws LMSException {
		Connection con = DBConnect.getConnection();
		String addQry = "";
		StringBuilder returnData = null;

		Map<Integer, StringBuilder> gameData = new HashMap<Integer, StringBuilder>();
		Map<Integer, Integer> offlineFzTimeMap = new HashMap<Integer, Integer>();
		Map<Integer, Double> retSaleComm = new HashMap<Integer, Double>();
		OfflineRetailerBean retBean = new OfflineRetailerBean();
		retBean.setVersion(version);
		try {
			boolean displayBetName = "PHILIP".equals(Utility.getPropertyValue("COUNTRY_DEPLOYED"));

			returnData = new StringBuilder("");
			boolean status = DrawGameRPOSHelper.checkMandatoryDownload(userId, version, con);
			if (!status) {
				returnData.append("NO_ENTRY");
				return returnData.toString();
			}

			PreparedStatement Offpstmt = con.prepareStatement(
					"select user_id from st_lms_ret_offline_master where user_id=? and is_offline='YES'");
			Offpstmt.setInt(1, userId);
			ResultSet offRs = Offpstmt.executeQuery();
			if (offRs.next()) {
				isOffline = true;
			}

			if (isOffline) {
				addQry = " (is_offline = 'Y' and game_nbr in(select promo_game_id from st_dg_promo_scheme ps inner join st_dg_game_master gm on sale_game_id=game_nbr where ps.status='ACTIVE')) or ";
			}

			String selQry = "select game_id,game_name_dev,game_name,game_nbr,offline_freeze_time,retailer_sale_comm_rate,ifnull(promo_game_type,'STANDARD') game_type,ifnull(raffle_ticket_type,0) raffle_ticket_type from st_dg_game_master left outer join st_dg_promo_scheme ps on game_id=promo_game_id and ps.status='ACTIVE' where "
					+ addQry + " game_status='OPEN' and is_sale_allowed_through_terminal='Y' order by game_nbr";

			PreparedStatement pstmt = con.prepareStatement(selQry);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String gameName = rs.getString("game_name_dev");
				if (version < 5.8 && "SuperTwo".equalsIgnoreCase(gameName)) {
					continue;
				}
				if (/* "Zerotonine".equalsIgnoreCase(gameName) || */ "OneToTwelve".equalsIgnoreCase(gameName)) {
					continue;
				}

				returnData = new StringBuilder("");
				int gameId = rs.getInt("game_id");

				returnData.append(gameName + ",");
				returnData.append(rs.getString("game_name") + ",");
				returnData.append(rs.getInt("game_nbr") + ",");
				String gameType = rs.getString("game_type");

				if ("STANDARD".equalsIgnoreCase(gameType)) {
					returnData.append("0,");
				} else if ("RAFFLE".equalsIgnoreCase(gameType)) {
					returnData.append("1,");
				} else if ("PROMO".equalsIgnoreCase(gameType)) {
					returnData.append("2,");
				}

				if (version > 5.6) {
					String raffleType = rs.getString("raffle_ticket_type");
					if ("REFERENCE".equalsIgnoreCase(raffleType)) {
						returnData.append("2,");
					} else if ("ORIGINAL".equalsIgnoreCase(raffleType)) {
						returnData.append("1,");
					} else {
						returnData.append("0,");
					}
				}
				returnData.append("totalDraw,"); // Number
				// of
				// Active
				// Draws

				if (version <= 6.5 && "FortuneTwo".equalsIgnoreCase(gameName)) {
					Map<String, BetDetailsBean> priceMap = Util.getGameMap().get(gameId).getPriceMap();
					Iterator<String> betTypeIter = priceMap.keySet().iterator();
					String betType = null;
					while (betTypeIter.hasNext()) {
						betType = betTypeIter.next();
						if (!(betType.contains("Banker"))) {
							returnData.append(priceMap.get(betType).getUnitPrice() + ":");
						}
					}
					returnData.deleteCharAt(returnData.length() - 1);
					returnData.append(",");

				} else if (version <= 6.5 && "FortuneThree".equalsIgnoreCase(gameName)) {
					Map<String, BetDetailsBean> priceMap = Util.getGameMap().get(gameId).getPriceMap();
					Iterator<String> betTypeIter = priceMap.keySet().iterator();
					String betType = null;
					while (betTypeIter.hasNext()) {
						betType = betTypeIter.next();
						if (!(betType.contains("Banker"))) {
							returnData.append(priceMap.get(betType).getUnitPrice() + ":");
						}
					}
					returnData.deleteCharAt(returnData.length() - 1);
					returnData.append(",");
				}

				else {
					// returnData.append(Util.convertCollToStr(new
					// TreeMap<String,
					// BetDetailsBean>(Util.getGameMap().get(gameName).getPriceMap()).values()).replace(",
					// ", ":") + ",");

					Map<String, BetDetailsBean> map1 = new TreeMap<String, BetDetailsBean>(
							Util.getGameMap().get(gameId).getPriceMap());
					ValueComparator comparator = new ValueComparator(map1);
					Map<String, BetDetailsBean> map2 = new TreeMap<String, BetDetailsBean>(comparator);
					map2.putAll(map1);
					Iterator iterator = map2.entrySet().iterator();
					// Iterator<String> iterator = map2.keySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry pair = (Map.Entry) iterator.next();
						BetDetailsBean bean = (BetDetailsBean) pair.getValue();
						if ("ACTIVE".equals(bean.getBetStatus())) {
							String betDispNameAppender = (displayBetName) ? "~" + bean.getBetDispName() : "";
							if (!("KenoTwo".equalsIgnoreCase(gameName) && pair.getKey().toString().contains("Direct")))
								// returnData.append(bean.getUnitPrice()).append("~").append(bean.getMaxBetAmtMultiple()).append("~").append((bean.getBetStatus()
								// ==
								// null)?"":(bean.getBetStatus().equals("ACTIVE"))?1:0).append("(").append(bean.getBetCode()).append("-").append(bean.getBetOrder()).append(")").append(":");
								returnData.append(bean.getBetCode()).append("~").append(bean.getUnitPrice()).append("~")
										.append(bean.getMaxBetAmtMultiple()).append(betDispNameAppender).append(":");
						}
					}
					returnData.deleteCharAt(returnData.length() - 1);
					returnData.append(",");
				}

				gameData.put(gameId, returnData);
				offlineFzTimeMap.put(gameId, rs.getInt("offline_freeze_time"));
				retSaleComm.put(gameId, Util.getSaleCommVariance(orgId, rs.getInt("game_id")));
			}

			retBean.setUserId(userId);
			retBean.setGameData(gameData);
			retBean.setOffline(isOffline);
			retBean.setOfflineFzTimeMap(offlineFzTimeMap);
			retBean.setRetSaleComm(retSaleComm);
			retBean.setPartyType("RETAILER");
			retBean.setStatusWithDrawInfo(
					Utility.getPropertyValue("COUNTRY_DEPLOYED").equalsIgnoreCase("GHANA") ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Some Error");
		} finally {
			DBConnect.closeCon(con);
		}

		try {
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			ServiceRequest req = new ServiceRequest();
			req.setServiceName(ServiceName.DRAWGAME);
			req.setServiceMethod(ServiceMethodName.FETCH_DG_DATA_OFFLINE);
			req.setServiceData(retBean);

			ServiceResponse resp = delegate.getResponse(req);

			String responseString = resp.getResponseData().toString();
			Type elementType = new TypeToken<OfflineRetailerBean>() {
			}.getType();

			if (resp.getIsSuccess()) {
				retBean = new Gson().fromJson(responseString, elementType);
			} else {
				throw new LMSException("Some Error");
			}

			gameData = retBean.getGameData();
			returnData = new StringBuilder("");
			Iterator<Map.Entry<Integer, StringBuilder>> itr = gameData.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, StringBuilder> pair = itr.next();
				returnData.append(pair.getValue() + "#");
			}
			returnData.deleteCharAt(returnData.length() - 1);

			if (isOffline) {
				try {
					con = DBConnect.getConnection();
					returnData.append("|PromoInfo:");
					String selQry = "select scheme_id,sale_game_id,promo_game_id,sale_ticket_amt,if(valid_for_draw='INDOOR',1,if(valid_for_draw='OUTDOOR',0,2)) valid_for_draw from st_dg_promo_scheme where status='ACTIVE'";
					PreparedStatement pstmt = con.prepareStatement(selQry);
					ResultSet rs = pstmt.executeQuery();
					boolean isPromo = false;
					while (rs.next()) {
						isPromo = true;
						returnData.append(rs.getString("scheme_id") + ",");
						returnData.append(rs.getString("sale_game_id") + ",");
						returnData.append(rs.getString("promo_game_id") + ",");
						returnData.append(rs.getString("sale_ticket_amt") + ",");
						returnData.append(rs.getString("valid_for_draw") + "#");
					}
					if (isPromo) {
						returnData.deleteCharAt(returnData.length() - 1);
					} else {
						returnData.delete(returnData.length() - "|PromoInfo:".length(), returnData.length());
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new LMSException("Some Error");
				} finally {
					DBConnect.closeCon(con);
				}
			}
			logger.debug("***Game Info for Ret User**" + userId + "****" + returnData + "****");
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Some Error");
		}
		return returnData.toString();
	}

	public String getTicketNumberFrmTxnId(String txnId, int userOrgId) {

		Connection connection = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int gameNo = 0;
		String ticketNumber = null;
		double mrpAmt = 0.0;
		try {
			String gameNoQry = "select game_id from st_lms_retailer_transaction_master where retailer_org_id="
					+ userOrgId + "  and  transaction_id =" + txnId;
			pstmt = connection.prepareStatement(gameNoQry);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				gameNo = rs.getInt("game_id");
			}
			pstmt = connection.prepareStatement(
					"select ticket_nbr,mrp_amt from st_dg_ret_sale_" + gameNo + " where transaction_id=" + txnId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ticketNumber = rs.getString("ticket_nbr");
				mrpAmt = rs.getDouble("mrp_amt");
			}
			if (mrpAmt > 0) {
				// sale ticket
			} else {
				rs = pstmt
						.executeQuery("select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr="
								+ ticketNumber);
				while (rs.next()) {
					ticketNumber = rs.getString("sale_ticket_nbr");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticketNumber;
	}

	public List<String> getTransactionType(List<Long> lmsTranxIdList) {
		List<String> returnList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			String returnType = null;
			int gameNo = 0;
			String gameNameDev = null;
			PreparedStatement pstmt = con.prepareStatement(
					"select transaction_type, game_id from st_lms_retailer_transaction_master where transaction_id = ?");

			for (int i = 0; i < lmsTranxIdList.size(); i++) {
				pstmt.setString(1, lmsTranxIdList.get(i) + "");
				logger.debug("***getTransactionType query : " + pstmt + "******");
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					returnType = rs.getString("transaction_type");
					gameNo = rs.getInt("game_id");
					gameNameDev = Util.getGameName(gameNo);
					returnType = returnType + "_" + gameNameDev.toUpperCase();
				}

				returnList.add(returnType);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return returnList;
	}

	public static Object getTransactionStatusAndData(UserInfoBean userBean, List<Long> lmsTranxIdList,
			List<String> lmsTranxIdTypeList) {
		if ("DG_SALE_KENO".equalsIgnoreCase(lmsTranxIdTypeList.get(0))) {
			KenoPurchaseBean kenoBean = kenoSaleTxnStatusAndData(userBean, lmsTranxIdList.get(0) + "");
			return kenoBean;
		} else if ("DG_SALE_TANZANIALOTTO".equalsIgnoreCase(lmsTranxIdTypeList.get(0))) {
			LottoPurchaseBean lottoBean = tanzanialottoSaleTxnStatusAndData(userBean, lmsTranxIdList.get(0) + "");
			return lottoBean;
		} else if ("DG_SALE_BONUSBALLLOTTO".equalsIgnoreCase(lmsTranxIdTypeList.get(0))) {
			LottoPurchaseBean lottoBean = bonusBalllottoSaleTxnStatusAndData(userBean, lmsTranxIdList.get(0) + "");
			return lottoBean;
		} else if (lmsTranxIdTypeList.get(0).contains("DG_REFUND_CANCEL")) {
			CancelTicketBean cancelBean = cancelTxnStatusAndData(userBean, lmsTranxIdList.get(0) + "");
			return cancelBean;
		} else if (lmsTranxIdTypeList.get(0).contains("PWT")) {
			PWTApiBean pwtBean = pwtTxnStatusAndData(userBean, lmsTranxIdList);
			return pwtBean;
		}

		return null;
	}

	public static List<Long> getLMSTxnIdList(int retOrgId, String refTxnId) {
		List<Long> returnList = new ArrayList<Long>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"select lms_txn_id, mobile_no from st_lms_tp_txn_mapping where retailer_org_id=? and tp_ref_txn_id=?");
			pstmt.setInt(1, retOrgId);
			pstmt.setString(2, refTxnId);
			logger.debug("******getLMSTxnIdAndMobileNo query:" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				returnList.add(rs.getLong("lms_txn_id"));
				// returnData = rs.getString("lms_txn_id") + "Nxt" +
				// rs.getString("mobile_no");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return returnList;
	}

	public static String getMobileNo(int retOrgId, String refTxnId) {
		String mobileNo = null;
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"select lms_txn_id, mobile_no from st_lms_tp_txn_mapping where retailer_org_id=? and tp_ref_txn_id=?");
			pstmt.setInt(1, retOrgId);
			pstmt.setString(2, refTxnId);
			logger.debug("******getMobileNo query:" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				mobileNo = rs.getString("mobile_no");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return mobileNo;
	}

	public static KenoPurchaseBean kenoSaleTxnStatusAndData(UserInfoBean userBean, String lmsTranxIdToReprint) {
		KenoPurchaseBean kenoBean = new KenoPurchaseBean();
		Connection con = DBConnect.getConnection();
		int gameNo = 0;
		String ticketNo = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"select retailer_user_id, retailer_org_id, game_id, transaction_date, transaction_type from st_lms_retailer_transaction_master where transaction_id=?");
			pstmt.setString(1, lmsTranxIdToReprint);
			logger.debug("kenoSaleTxnStatusAndData query :" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int retUserId = rs.getInt("retailer_user_id");
				int retOrgId = rs.getInt("retailer_org_id");
				gameNo = rs.getInt("game_id");
				String txnTime = rs.getString("transaction_date");

				kenoBean.setGame_no(gameNo);
				kenoBean.setGameDispName(Util.getGameDisplayName(gameNo));
				kenoBean.setPartyId(retOrgId);
				kenoBean.setUserId(retUserId);
			}

			pstmt = con.prepareStatement("select ticket_nbr from st_dg_ret_sale_? where transaction_id=?");
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, lmsTranxIdToReprint);
			logger.debug("kenoSaleTxnStatusAndData ticket query :" + pstmt);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				ticketNo = rs.getString("ticket_nbr");
				kenoBean.setTicket_no(ticketNo);
			}

			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.KENO);
			sReq.setServiceMethod(ServiceMethodName.KENO_SALE_TXN_STATUS_AND_DATA);
			sReq.setServiceData(kenoBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);

			if (sRes.getIsSuccess()) {
				kenoBean = (KenoPurchaseBean) sRes.getResponseData();
			}

			kenoBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(), gameNo, "PLAYER", "SALE", "DG"));

			// -- Raffle Data
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

			if (lmsTranxIdToReprint != null && Integer.parseInt(lmsTranxIdToReprint) > 0) {
				ticketNo = helper.getTicketNumberFrmTxnId(lmsTranxIdToReprint, userBean.getUserOrgId());

			}

			int mainTktGamenbr = helper.getGamenoFromTktnumber(ticketNo + "00");
			List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
			List<String> promoTicketList = orgOnLineSaleCreditUpdation.getAssociatedPromoTicket(ticketNo);

			if (promoTicketList != null && promoTicketList.size() > 0) {
				String proMoTktNumber = promoTicketList.get(0);
				mainTktGamenbr = helper.getGamenoFromTktnumber(promoTicketList.get(0) + "00");
				String gameType = Util.getGameType(mainTktGamenbr);

				if ("RAFFLE".equalsIgnoreCase(gameType)) {
					boolean isFirstTime = true;
					boolean isPwt = false;
					String tktNumber = proMoTktNumber + "00";

					String saleStatus;
					sRes = new ServiceResponse();
					sReq = new ServiceRequest();
					delegate = ServiceDelegate.getInstance();

					RafflePurchaseBean rafflePurchaseBean = null;
					// RafflePurchaseBean raffleGameBean = null;

					ReprintBean reprintbean = new ReprintBean();
					String raffleTicketType = getRaffleTktTypeFromgameNbr(mainTktGamenbr, con);
					if (isFirstTime || raffleTicketType.equalsIgnoreCase("REFERENCE")) {
						reprintbean.setPwt(isPwt);
						reprintbean.setTicketNumber(tktNumber);
						sReq.setServiceData(reprintbean);
						sReq.setServiceName("raffleGame" + "Module");
						sReq.setServiceMethod("getRaffleSaleTxnStatusAndData");
						sRes = delegate.getResponse(sReq);
						if (sRes.getIsSuccess()) {
							rafflePurchaseBean = (RafflePurchaseBean) sRes.getResponseData();
							saleStatus = rafflePurchaseBean.getSaleStatus();
							rafflePurchaseBean.setRaffleTicketType(raffleTicketType);
							rafflePurchaseBean.setGame_no(kenoBean.getGame_no()); // to
																					// be
																					// asked
						}
					}
					rafflePurchaseBeanList.add(rafflePurchaseBean);
				}
				kenoBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return kenoBean;
	}

	public static LottoPurchaseBean tanzanialottoSaleTxnStatusAndData(UserInfoBean userBean,
			String lmsTranxIdToReprint) {
		LottoPurchaseBean lottoBean = new LottoPurchaseBean();
		Connection con = DBConnect.getConnection();
		int gameNo = 0;
		String ticketNo = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"select retailer_user_id, retailer_org_id, game_id, transaction_date, transaction_type from st_lms_retailer_transaction_master where transaction_id=?");
			pstmt.setString(1, lmsTranxIdToReprint);
			logger.debug("tanzanialottoSaleTxnStatusAndData query :" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int retUserId = rs.getInt("retailer_user_id");
				int retOrgId = rs.getInt("retailer_org_id");
				gameNo = rs.getShort("game_id");
				// String txnTime = rs.getString("transaction_date");

				lottoBean.setGame_no(gameNo);
				lottoBean.setGameDispName(Util.getGameDisplayName(gameNo));
				lottoBean.setPartyId(retOrgId);
				lottoBean.setUserId(retUserId);
			}

			pstmt = con.prepareStatement("select ticket_nbr from st_dg_ret_sale_? where transaction_id=?");
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, lmsTranxIdToReprint);
			logger.debug("kenoSaleTxnStatusAndData ticket query :" + pstmt);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				ticketNo = rs.getString("ticket_nbr");
				lottoBean.setTicket_no(ticketNo);
			}

			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.TANZANIALOTTO);
			sReq.setServiceMethod(ServiceMethodName.TANZANIALOTTO_SALE_TXN_STATUS_AND_DATA);
			sReq.setServiceData(lottoBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);

			if (sRes.getIsSuccess()) {
				lottoBean = (LottoPurchaseBean) sRes.getResponseData();
			}

			lottoBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(), gameNo, "PLAYER", "SALE", "DG"));

			// -- Raffle Data
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

			if (lmsTranxIdToReprint != null && Integer.parseInt(lmsTranxIdToReprint) > 0) {
				ticketNo = helper.getTicketNumberFrmTxnId(lmsTranxIdToReprint, userBean.getUserOrgId());

			}

			int mainTktGamenbr = helper.getGamenoFromTktnumber(ticketNo + "00");
			List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
			List<String> promoTicketList = orgOnLineSaleCreditUpdation.getAssociatedPromoTicket(ticketNo);

			if (promoTicketList != null && promoTicketList.size() > 0) {
				String proMoTktNumber = promoTicketList.get(0);
				mainTktGamenbr = helper.getGamenoFromTktnumber(promoTicketList.get(0) + "00");
				String gameType = Util.getGameType(mainTktGamenbr);

				if ("RAFFLE".equalsIgnoreCase(gameType)) {
					boolean isFirstTime = true;
					boolean isPwt = false;
					String tktNumber = proMoTktNumber + "00";

					String saleStatus;
					sRes = new ServiceResponse();
					sReq = new ServiceRequest();
					delegate = ServiceDelegate.getInstance();

					RafflePurchaseBean rafflePurchaseBean = null;
					// RafflePurchaseBean raffleGameBean = null;

					ReprintBean reprintbean = new ReprintBean();
					String raffleTicketType = getRaffleTktTypeFromgameNbr(mainTktGamenbr, con);
					if (isFirstTime || raffleTicketType.equalsIgnoreCase("REFERENCE")) {
						reprintbean.setPwt(isPwt);
						reprintbean.setTicketNumber(tktNumber);
						sReq.setServiceData(reprintbean);
						sReq.setServiceName("raffleGame" + "Module");
						sReq.setServiceMethod("getRaffleSaleTxnStatusAndData");
						sRes = delegate.getResponse(sReq);
						if (sRes.getIsSuccess()) {
							rafflePurchaseBean = (RafflePurchaseBean) sRes.getResponseData();
							saleStatus = rafflePurchaseBean.getSaleStatus();
							rafflePurchaseBean.setRaffleTicketType(raffleTicketType);
							rafflePurchaseBean.setGame_no(lottoBean.getGame_no()); // to
																					// be
																					// asked
						}
					}
					rafflePurchaseBeanList.add(rafflePurchaseBean);
				}
				lottoBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return lottoBean;
	}

	public static LottoPurchaseBean bonusBalllottoSaleTxnStatusAndData(UserInfoBean userBean,
			String lmsTranxIdToReprint) {
		LottoPurchaseBean lottoBean = new LottoPurchaseBean();
		Connection con = DBConnect.getConnection();
		int gameNo = 0;
		String ticketNo = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"select retailer_user_id, retailer_org_id, game_id, transaction_date, transaction_type from st_lms_retailer_transaction_master where transaction_id=?");
			pstmt.setString(1, lmsTranxIdToReprint);
			logger.debug("bonusBalllottoSaleTxnStatusAndData query :" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int retUserId = rs.getInt("retailer_user_id");
				int retOrgId = rs.getInt("retailer_org_id");
				gameNo = rs.getInt("game_id");
				// String txnTime = rs.getString("transaction_date");

				lottoBean.setGame_no(gameNo);
				lottoBean.setGameDispName(Util.getGameDisplayName(gameNo));
				lottoBean.setPartyId(retOrgId);
				lottoBean.setUserId(retUserId);
			}

			pstmt = con.prepareStatement("select ticket_nbr from st_dg_ret_sale_? where transaction_id=?");
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, lmsTranxIdToReprint);
			logger.debug("bonusBalllottoSaleTxnStatusAndData ticket query :" + pstmt);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				ticketNo = rs.getString("ticket_nbr");
				lottoBean.setTicket_no(ticketNo);
			}

			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.BUNUSBALLLOTTO);
			sReq.setServiceMethod(ServiceMethodName.BONUSBALLLOTTO_SALE_TXN_STATUS_AND_DATA);
			sReq.setServiceData(lottoBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);

			if (sRes.getIsSuccess()) {
				lottoBean = (LottoPurchaseBean) sRes.getResponseData();
			}

			lottoBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(), gameNo, "PLAYER", "SALE", "DG"));

			// -- Raffle Data
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

			if (lmsTranxIdToReprint != null && Integer.parseInt(lmsTranxIdToReprint) > 0) {
				ticketNo = helper.getTicketNumberFrmTxnId(lmsTranxIdToReprint, userBean.getUserOrgId());

			}

			int mainTktGamenbr = helper.getGamenoFromTktnumber(ticketNo + "00");
			List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
			List<String> promoTicketList = orgOnLineSaleCreditUpdation.getAssociatedPromoTicket(ticketNo);

			if (promoTicketList != null && promoTicketList.size() > 0) {
				String proMoTktNumber = promoTicketList.get(0);
				mainTktGamenbr = helper.getGamenoFromTktnumber(promoTicketList.get(0) + "00");
				String gameType = Util.getGameType(mainTktGamenbr);

				if ("RAFFLE".equalsIgnoreCase(gameType)) {
					boolean isFirstTime = true;
					boolean isPwt = false;
					String tktNumber = proMoTktNumber + "00";

					String saleStatus;
					sRes = new ServiceResponse();
					sReq = new ServiceRequest();
					delegate = ServiceDelegate.getInstance();

					RafflePurchaseBean rafflePurchaseBean = null;
					// RafflePurchaseBean raffleGameBean = null;

					ReprintBean reprintbean = new ReprintBean();
					String raffleTicketType = getRaffleTktTypeFromgameNbr(mainTktGamenbr, con);
					if (isFirstTime || raffleTicketType.equalsIgnoreCase("REFERENCE")) {
						reprintbean.setPwt(isPwt);
						reprintbean.setTicketNumber(tktNumber);
						sReq.setServiceData(reprintbean);
						sReq.setServiceName("raffleGame" + "Module");
						sReq.setServiceMethod("getRaffleSaleTxnStatusAndData");
						sRes = delegate.getResponse(sReq);
						if (sRes.getIsSuccess()) {
							rafflePurchaseBean = (RafflePurchaseBean) sRes.getResponseData();
							saleStatus = rafflePurchaseBean.getSaleStatus();
							rafflePurchaseBean.setRaffleTicketType(raffleTicketType);
							rafflePurchaseBean.setGame_no(lottoBean.getGame_no()); // to
																					// be
																					// asked
						}
					}
					rafflePurchaseBeanList.add(rafflePurchaseBean);
				}
				lottoBean.setRafflePurchaseBeanList(rafflePurchaseBeanList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return lottoBean;
	}

	public static CancelTicketBean cancelTxnStatusAndData(UserInfoBean userBean, String lmsTranxIdToReprint) {
		CancelTicketBean cancelBean = new CancelTicketBean();
		Connection con = DBConnect.getConnection();
		int gameNo = 0;
		String ticketNo = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"select retailer_user_id, retailer_org_id, game_id, transaction_date, transaction_type from st_lms_retailer_transaction_master where transaction_id=?");
			pstmt.setString(1, lmsTranxIdToReprint);
			logger.debug("cancelTxnStatusAndData query :" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int retUserId = rs.getInt("retailer_user_id");
				int retOrgId = rs.getInt("retailer_org_id");
				gameNo = rs.getInt("game_id");
				String txnTime = rs.getString("transaction_date");

				cancelBean.setGameNo(gameNo);
			}

			pstmt = con.prepareStatement("select ticket_nbr from st_dg_ret_sale_refund_? where transaction_id=?");
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, lmsTranxIdToReprint);
			logger.debug("cancelTxnStatusAndData ticket query :" + pstmt);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				ticketNo = rs.getString("ticket_nbr");
				cancelBean.setTicketNo(ticketNo);
			}

			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.DRAWGAME);
			sReq.setServiceMethod(ServiceMethodName.CANCEL_TXN_STATUS_AND_DATA);
			sReq.setServiceData(cancelBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);

			if (sRes.getIsSuccess()) {
				cancelBean = (CancelTicketBean) sRes.getResponseData();
			}

			cancelBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(), gameNo, "PLAYER", "CANCEL", "DG"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return cancelBean;
	}

	public static PWTApiBean pwtTxnStatusAndData(UserInfoBean userBean, List<Long> lmsTranxIdList) {
		PWTApiBean pwtApiBean = new PWTApiBean();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = null;
			double pwtAmt = 0.0;
			int gameNo = 0;
			for (int i = 0; i < lmsTranxIdList.size(); i++) {
				pstmt = con.prepareStatement(
						"select retailer_user_id, retailer_org_id, game_id, transaction_date, transaction_type from st_lms_retailer_transaction_master where transaction_id=?");
				pstmt.setString(1, lmsTranxIdList.get(i) + "");
				logger.debug("pwtTxnStatusAndData query :" + pstmt);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					int retUserId = rs.getInt("retailer_user_id");
					int retOrgId = rs.getInt("retailer_org_id");
					gameNo = rs.getInt("game_id");
					String txnTime = rs.getString("transaction_date");
				}

				pstmt = con.prepareStatement(
						"select retailer_user_id, retailer_org_id, draw_id, panel_id, game_id, pwt_amt, status from st_dg_ret_pwt_? where transaction_id = ?");
				pstmt.setInt(1, gameNo);
				pstmt.setString(2, lmsTranxIdList.get(i) + "");
				logger.debug("pwtTxnStatusAndData ticket query :" + pstmt);
				rs = pstmt.executeQuery();

				if (rs.next()) {
					int drawId = rs.getInt("draw_id");
					int panelId = rs.getInt("panel_id");
					int gameId = rs.getInt("game_id");
					pwtAmt = pwtAmt + rs.getDouble("pwt_amt");
					String status = rs.getString("status");
				}
			}

			pwtApiBean.setTotalWinning(pwtAmt + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return pwtApiBean;
	}

	/**
	 * Use When Connection is not Available
	 * 
	 * @param userBean
	 * @param lastPrintedTicket
	 * @param interfaceType
	 * @param refMerchantId
	 * @param autoCancelHoldDays
	 * @param actionName
	 * @param gameId
	 * @throws LMSException
	 */
	public void checkLastPrintedTicketStatusAndUpdate(UserInfoBean userBean, long lastPrintedTicket,
			String interfaceType, String refMerchantId, int autoCancelHoldDays, String actionName, int gameId)
			throws LMSException {
		Connection con = null;
		/*
		 * PreparedStatement pstmt = null; ResultSet rs = null;
		 * PreparedStatement insPstmt=null;
		 * 
		 * long lastSoldTicket=0; String ticketstatus=null;
		 */

		try {
			con = getConnectionObject();
			/// Method Called With Connection
			checkLastPrintedTicketStatusAndUpdate(userBean, lastPrintedTicket, interfaceType, refMerchantId,
					autoCancelHoldDays, actionName, gameId, con);
			/*
			 * if("TERMINAL".equals(interfaceType)){ pstmt=con.
			 * prepareStatement("select terminal_ticket_number,terminal_ticket_status from st_dg_last_sold_ticket where ret_org_id=?"
			 * ); pstmt.setInt(1, userBean.getUserOrgId());
			 * rs=pstmt.executeQuery(); if(rs.next()){
			 * lastSoldTicket=rs.getLong("terminal_ticket_number");
			 * ticketstatus=rs.getString("terminal_ticket_status"); }else {
			 * throw new LMSException("DATA_ERROR"); }
			 * 
			 * }else if("WEB".equals(interfaceType)){ pstmt=con.
			 * prepareStatement("select web_ticket_number,web_ticket_status from st_dg_last_sold_ticket where ret_org_id=?"
			 * ); pstmt.setInt(1, userBean.getUserOrgId());
			 * rs=pstmt.executeQuery(); if(rs.next()){
			 * lastSoldTicket=rs.getLong("web_ticket_number");
			 * ticketstatus=rs.getString("web_ticket_status"); }else { throw new
			 * LMSException("DATA_ERROR"); // Next Sale not allowed } }
			 * 
			 * logger.debug("SALE_AUTO_CANCEL_LOGS: lastSoldTicket"
			 * +lastSoldTicket+" last printed tickets"+lastPrintedTicket);
			 * 
			 * 
			 * if(lastPrintedTicket !=0 && lastSoldTicket ==0 ){ throw new
			 * LMSException("DATA_ERROR"); // Next Sale not allowed }else
			 * if(lastPrintedTicket ==0 && lastSoldTicket !=0 ){ //No Action
			 * sale Continue }else if(lastPrintedTicket == lastSoldTicket &&
			 * "SOLD".equals(ticketstatus)){ insPstmt=con.
			 * prepareStatement("insert into st_dg_printed_tickets_?(retailer_org_id,ticket_nbr,channel,notification_time,action_name)values(?,?,?,?,?)"
			 * ); insPstmt.setInt(1, gameId); insPstmt.setInt(2,
			 * userBean.getUserOrgId()); insPstmt.setLong(3, lastPrintedTicket);
			 * insPstmt.setString(4, interfaceType); insPstmt.setTimestamp(5,
			 * Util.getCurrentTimeStamp()); insPstmt.setString(6, actionName);
			 * insPstmt.executeUpdate();
			 * 
			 * insPstmt=con.
			 * prepareStatement("update st_dg_last_sold_ticket set "
			 * +interfaceType.toLowerCase()
			 * +"_ticket_status='PRINTED' where ret_org_id=?");
			 * insPstmt.setInt(1, userBean.getUserOrgId());
			 * insPstmt.executeUpdate(); }else if(lastPrintedTicket !=
			 * lastSoldTicket && "SOLD".equals(ticketstatus)){ //auto cancel
			 * ticket CancelTicketBean cancelTicketBean = new
			 * CancelTicketBean();
			 * //cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
			 * cancelTicketBean.setTicketNo(lastSoldTicket +
			 * Util.getRpcAppenderForTickets(String.valueOf(lastSoldTicket).
			 * length())); cancelTicketBean.setPartyId(userBean.getUserOrgId());
			 * cancelTicketBean.setPartyType(userBean.getUserType());
			 * cancelTicketBean.setUserId(userBean.getUserId());
			 * cancelTicketBean.setCancelChannel("LMS_Terminal");
			 * cancelTicketBean.setRefMerchantId(refMerchantId);
			 * cancelTicketBean.setAutoCancel(true);
			 * cancelTicketBean.setHoldAutoCancel(true);
			 * cancelTicketBean.setAutoCancelHoldDays(autoCancelHoldDays); //
			 * cancel code pending for review and prefer create new method for
			 * auto cancel
			 * 
			 * cancelTicketBean = cancelTicket(cancelTicketBean, userBean,
			 * true,"CANCEL_MISMATCH"); logger.debug("SALE_AUTO_CANCEL_LOGS:" +
			 * "is cancelled " + cancelTicketBean.isValid() + " :Ticket_number"
			 * + lastSoldTicket);
			 * 
			 * 
			 * //insert last printed tickets insPstmt=con.
			 * prepareStatement("insert into st_dg_printed_tickets_?(retailer_org_id,ticket_nbr,channel,notification_time,action_name)values(?,?,?,?,?)"
			 * ); insPstmt.setInt(1, gameId); insPstmt.setInt(2,
			 * userBean.getUserOrgId()); insPstmt.setLong(3, lastPrintedTicket);
			 * insPstmt.setString(4, interfaceType); insPstmt.setTimestamp(5,
			 * Util.getCurrentTimeStamp()); insPstmt.setString(6, actionName);
			 * insPstmt.executeUpdate();
			 * 
			 * //status update cancelled insPstmt=con.
			 * prepareStatement("update st_dg_last_sold_ticket set "
			 * +interfaceType.toLowerCase()
			 * +"_ticket_status='CANCELLED' where ret_org_id=?");
			 * insPstmt.setInt(1, userBean.getUserOrgId());
			 * insPstmt.executeUpdate();
			 * 
			 * 
			 * } con.commit();
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("SQL_ERROR");
		} finally {
			DBConnect.closeCon(con);
		}

	}

	public void getUnsoldTkts(int gameNo, String startDate, String endDate) {
		logger.debug("data to getUnsoldTkts: " + gameNo + "," + startDate + "," + endDate);
		ArrayList<CancelTicketBean> tktList = new ArrayList<CancelTicketBean>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String refMerchantId = (String) LMSUtility.sc.getAttribute("REF_MERCHANT_ID");
		UserInfoBean userBean;
		CancelTicketBean cancelBean;
		try {
			String query = "select sale_tkt, retailer_user_id, retailer_org_id, parent_id, organization_type from (select sale_tkt, retailer_user_id, retailer_org_id, parent_id, organization_type from"
					+ " (select ticket_nbr sale_tkt, rtm.retailer_user_id, rtm.retailer_org_id, om.parent_id, um.organization_type from st_dg_ret_sale_"
					+ gameNo
					+ " sale inner join st_lms_retailer_transaction_master rtm inner join st_lms_transaction_master tm inner join st_lms_organization_master om inner join st_lms_user_master um on um.user_id = rtm.retailer_user_id and om.organization_id = rtm.retailer_org_id and sale.transaction_id=rtm.transaction_id and rtm.transaction_id=tm.transaction_id"
					+ " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate
					+ "' and interface='TERMINAL') sale left outer join (select ticket_nbr refund_tkt from st_dg_ret_sale_refund_"
					+ gameNo
					+ " sale inner join st_lms_retailer_transaction_master rtm inner join st_lms_transaction_master tm on sale.transaction_id=rtm.transaction_id and rtm.transaction_id=tm.transaction_id where transaction_date>='"
					+ startDate + "' and transaction_date<='" + endDate
					+ "' and interface='TERMINAL') refund on sale_tkt=refund_tkt where refund_tkt is null) sale left outer join (select ticket_nbr term_tkt from st_dg_terminal_sale_ticket_"
					+ gameNo + " where purchase_time>='" + startDate + "' and purchase_time<='" + endDate
					+ "') termSale on  sale_tkt=term_tkt where term_tkt is null";
			logger.debug("query to fetch unsold tickets: " + query);
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userBean = new UserInfoBean();
				cancelBean = new CancelTicketBean();
				String tktNo = rs.getString("sale_tkt");
				userBean.setUserId(rs.getInt("retailer_user_id"));
				userBean.setUserOrgId(rs.getInt("retailer_org_id"));
				userBean.setParentOrgId(rs.getInt("parent_id"));
				cancelBean.setTicketNo(tktNo + "00");
				cancelBean.setGameNo(gameNo);
				cancelBean.setPartyId(userBean.getUserOrgId());
				cancelBean.setPartyType(rs.getString("organization_type"));
				cancelBean.setUserId(userBean.getUserId());
				cancelBean.setCancelChannel("LMS_Terminal");
				cancelBean.setRefMerchantId(refMerchantId);
				cancelBean.setAutoCancel("CANCEL_PRINTER");
				cancelBean.setAutoCancel(true);
				if (!getLastSoldTicketNO(userBean, "LMS_Terminal").equals(tktNo)) {
					cancelTicket(cancelBean, userBean, true, "CANCEL_MISMATCH");
				}
			}
			/*
			 * if(!tktList.isEmpty()){ IServiceDelegate delegate =
			 * ServiceDelegate.getInstance(); ServiceRequest req = new
			 * ServiceRequest(); req.setServiceName(ServiceName.DRAWGAME);
			 * req.setServiceMethod(ServiceMethodName.DRAW_CANCEL_AFTER_FREEZE);
			 * req.setServiceData(tktList); delegate.getResponse(req); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertLastSoldTicket(int userOrgId, String ticketNo) {

		String query = "update st_dg_last_sold_ticket set ticket_number = " + ticketNo + " where ret_org_id = "
				+ userOrgId;
		int rowInserted = 0;
		PreparedStatement pstmt = null;
		try {
			Connection con = DBConnect.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();

			logger.debug("Last Sold ticket Insertion query >>" + pstmt);
			rowInserted = pstmt.executeUpdate();
			if (rowInserted > 0) {
				con.commit();
			}
			DBConnect.closeCon(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use with Connection Parameter
	 * 
	 * @param userBean
	 * @param lastPrintedTicket
	 * @param interfaceType
	 * @param refMerchantId
	 * @param autoCancelHoldDays
	 * @param actionName
	 * @param gameId
	 * @param con
	 * @throws LMSException
	 */
	public void checkLastPrintedTicketStatusAndUpdate(UserInfoBean userBean, long lastPrintedTicket,
			String interfaceType, String refMerchantId, int autoCancelHoldDays, String actionName, int gameId,
			Connection con) throws LMSException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement insPstmt = null;

		long lastSoldTicket = 0;
		String ticketstatus = null;

		try {

			if ("TERMINAL".equals(interfaceType)) {
				pstmt = con.prepareStatement(
						"select terminal_ticket_number,terminal_ticket_status from st_dg_last_sold_ticket where ret_org_id=?");
				pstmt.setInt(1, userBean.getUserOrgId());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					lastSoldTicket = rs.getLong("terminal_ticket_number");
					ticketstatus = rs.getString("terminal_ticket_status");
				} else {
					throw new LMSException("DATA_ERROR");
				}

			} else if ("WEB".equals(interfaceType)) {
				pstmt = con.prepareStatement(
						"select web_ticket_number,web_ticket_status from st_dg_last_sold_ticket where ret_org_id=?");
				pstmt.setInt(1, userBean.getUserOrgId());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					lastSoldTicket = rs.getLong("web_ticket_number");
					ticketstatus = rs.getString("web_ticket_status");
				} else {
					throw new LMSException("DATA_ERROR"); // Next Sale not
															// allowed
				}
			}

			logger.info("SALE_AUTO_CANCEL_LOGS: lastSoldTicket" + lastSoldTicket + " last printed tickets"
					+ lastPrintedTicket);

			if (lastPrintedTicket != 0 && lastSoldTicket == 0) {
				// throw new LMSException("DATA_ERROR"); // Next Sale not
				// allowed
				logger.info("ERROR_USER_NAME: " + userBean.getUserName());
				try {
					checkTicketIfValid(lastPrintedTicket, gameId, con);
				} catch (LMSException e) {
					logger.info("INSIDE LMS EXCEPTION  ERROR_USER_NAME: " + userBean.getUserName());
					if (e.getErrorCode() != LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE) {
						logger.info("INSIDE  LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE BLOCK  ERROR_USER_NAME: "
								+ userBean.getUserName());
						return; // Next Sale allowed
					}
				}
				throw new LMSException("DATA_ERROR"); // Next Sale not allowed
			} else if (lastPrintedTicket == 0 && lastSoldTicket != 0) {
				// No Action sale Continue
			} else if (lastPrintedTicket == lastSoldTicket && "SOLD".equals(ticketstatus)) {
				insPstmt = con.prepareStatement(
						"insert into st_dg_printed_tickets_?(retailer_org_id,ticket_nbr,channel,notification_time,action_name)values(?,?,?,?,?)");
				insPstmt.setInt(1, gameId);
				insPstmt.setInt(2, userBean.getUserOrgId());
				insPstmt.setLong(3, lastPrintedTicket);
				insPstmt.setString(4, interfaceType);
				insPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
				insPstmt.setString(6, actionName);
				insPstmt.executeUpdate();

				insPstmt = con.prepareStatement("update st_dg_last_sold_ticket set " + interfaceType.toLowerCase()
						+ "_ticket_status='PRINTED' where ret_org_id=?");
				insPstmt.setInt(1, userBean.getUserOrgId());
				insPstmt.executeUpdate();
			} else if (lastPrintedTicket != lastSoldTicket && "SOLD".equals(ticketstatus)) {

				// CHECK IF TKT EXISTS IN SYSTEM ??
				checkTicketIfValid(lastPrintedTicket, gameId, con);

				// auto cancel ticket
				CancelTicketBean cancelTicketBean = new CancelTicketBean();
				// cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
				cancelTicketBean.setTicketNo(
						lastSoldTicket + Util.getRpcAppenderForTickets(String.valueOf(lastSoldTicket).length()));
				cancelTicketBean.setPartyId(userBean.getUserOrgId());
				cancelTicketBean.setPartyType(userBean.getUserType());
				cancelTicketBean.setUserId(userBean.getUserId());
				cancelTicketBean.setCancelChannel("LMS_Terminal");
				cancelTicketBean.setRefMerchantId(refMerchantId);
				cancelTicketBean.setAutoCancel(true);
				cancelTicketBean.setHoldAutoCancel(true);
				cancelTicketBean.setAutoCancelHoldDays(autoCancelHoldDays);
				// cancel code pending for review and prefer create new method
				// for auto cancel

				cancelTicketBean = cancelTicket(cancelTicketBean, userBean, true, "CANCEL_MISMATCH");
				logger.debug("SALE_AUTO_CANCEL_LOGS:" + "is cancelled " + cancelTicketBean.isValid() + " :Ticket_number"
						+ lastSoldTicket);

				// insert last printed tickets
				insPstmt = con.prepareStatement(
						"insert into st_dg_printed_tickets_?(retailer_org_id,ticket_nbr,channel,notification_time,action_name)values(?,?,?,?,?)");
				insPstmt.setInt(1, gameId);
				insPstmt.setInt(2, userBean.getUserOrgId());
				insPstmt.setLong(3, lastPrintedTicket);
				insPstmt.setString(4, interfaceType);
				insPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
				insPstmt.setString(6, actionName);
				insPstmt.executeUpdate();

				// status update cancelled
				insPstmt = con.prepareStatement("update st_dg_last_sold_ticket set " + interfaceType.toLowerCase()
						+ "_ticket_status='CANCELLED' where ret_org_id=?");
				insPstmt.setInt(1, userBean.getUserOrgId());
				insPstmt.executeUpdate();

			}
			con.commit();
		} catch (Exception e) {
			logger.error("Exception ", e);
			// e.printStackTrace();

			try {
				con.rollback();
			} catch (SQLException er) {
				logger.error("SQLException ", er);
				// er.printStackTrace();
			}
			throw new LMSException("SQL_ERROR");
		} finally {
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(pstmt);
			DBConnect.closePstmt(insPstmt);
		}
	}

	public void insertEntryIntoPrintedTktTableForWeb(int gameId, int userOrgId, long lastPrintedTkt,
			String interfaceType, Timestamp currentTimestamp, String actionName) throws Exception {
		PreparedStatement insPstmt = null;
		PreparedStatement selPstmt = null;
		ResultSet rsSelPstmt = null;
		Connection con = null;
		try {

			if (lastPrintedTkt != 0) {
				// No Action sale Continue

				con = getConnectionObject();

				selPstmt = con.prepareStatement(
						"SELECT auto_id FROM st_dg_last_sold_ticket lst, st_dg_printed_tickets_? pt where lst.web_ticket_number = ? and  pt.ticket_nbr = ? and lst.web_ticket_status='PRINTED'");
				selPstmt.setInt(1, gameId);
				selPstmt.setLong(2, lastPrintedTkt);
				selPstmt.setLong(3, lastPrintedTkt);
				rsSelPstmt = selPstmt.executeQuery();

				if (!rsSelPstmt.next()) {
					insPstmt = con.prepareStatement(
							"insert into st_dg_printed_tickets_?(retailer_org_id,ticket_nbr,channel,notification_time,action_name)values(?,?,?,?,?)");
					insPstmt.setInt(1, gameId);
					insPstmt.setInt(2, userOrgId);
					insPstmt.setLong(3, lastPrintedTkt);
					insPstmt.setString(4, interfaceType);
					insPstmt.setTimestamp(5, currentTimestamp);
					insPstmt.setString(6, actionName);
					insPstmt.executeUpdate();

					insPstmt = con.prepareStatement("update st_dg_last_sold_ticket set " + interfaceType.toLowerCase()
							+ "_ticket_status='PRINTED' where ret_org_id=?");
					insPstmt.setInt(1, userOrgId);
					insPstmt.executeUpdate();
					con.commit();
					logger.debug("Cancel MISMATCH Case by WEB for userOrgId = " + userOrgId + "; timestamp = "
							+ currentTimestamp + "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBConnect.closeCon(con);
		}
	}

	private void checkTicketIfValid(long tktNbr, int gameId, Connection con) throws LMSException {

		ResultSet rs = null;
		String checkQuery = null;
		String serviceCode = null;
		PreparedStatement pstmt = null;
		try {
			// CAN ALSO CHECK VALIDATE TIKCET Util.validateTkt(tktNum);
			logger.info("INSIDE TKT VALIDATE... ");
			// GET SERVICE CODE FROM SERVICE ID AND DECIDE THE SALE TABLE (EX:-
			// if DG then , st_dg_ret_sale_GAMEID)
			serviceCode = "DG";
			if (serviceCode == null) {
				throw new LMSException(LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE,
						LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE);
			} else if (serviceCode.equalsIgnoreCase("DG")) {
				checkQuery = "select transaction_id from st_dg_ret_sale_? where ticket_nbr = ?";
			} else if (serviceCode.equalsIgnoreCase("SL")) {
				// DECIDE THE QUREY OR SALE TABLE HERE
			} else {
				throw new LMSException(LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE,
						LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE);
			}

			pstmt = con.prepareStatement(checkQuery);
			pstmt.setInt(1, gameId);
			pstmt.setString(2, String.valueOf(tktNbr));
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE, LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(pstmt);
		}
	}

	public static String embdDgData(boolean isOffline, Map<Integer, Map<Integer, String>> drawIdTableMap, int userId,
			int orgId, double version, Connection con) throws LMSException {
		/// Connection con = DBConnect.getConnection();
		String addQry = "";
		StringBuilder returnData = null;
		if (isOffline) {
			addQry = " (is_offline = 'Y' and game_nbr in(select promo_game_id from st_dg_promo_scheme ps inner join st_dg_game_master gm on sale_game_id=game_nbr where ps.status='ACTIVE')) or ";
		}

		Map<Integer, StringBuilder> gameData = new HashMap<Integer, StringBuilder>();
		Map<Integer, Integer> offlineFzTimeMap = new HashMap<Integer, Integer>();
		Map<Integer, Double> retSaleComm = new HashMap<Integer, Double>();
		OfflineRetailerBean retBean = new OfflineRetailerBean();
		retBean.setVersion(version);
		try {
			String selQry = "select game_id,game_name_dev,game_name,game_nbr,offline_freeze_time,retailer_sale_comm_rate,ifnull(promo_game_type,'STANDARD') game_type,ifnull(raffle_ticket_type,0) raffle_ticket_type from st_dg_game_master left outer join st_dg_promo_scheme ps on game_id=promo_game_id and ps.status='ACTIVE' where "
					+ addQry + " game_status!='SALE_CLOSE' order by game_nbr";

			PreparedStatement pstmt = con.prepareStatement(selQry);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String gameName = rs.getString("game_name_dev");
				if (version < 5.8 && "SuperTwo".equalsIgnoreCase(gameName)) {
					continue;
				}

				returnData = new StringBuilder("");
				int gameId = rs.getInt("game_id");
				returnData.append(gameName + ",");
				returnData.append(rs.getString("game_name") + ",");
				returnData.append(gameId + ",");
				String gameType = rs.getString("game_type");

				if ("STANDARD".equalsIgnoreCase(gameType)) {
					returnData.append("0,");
				} else if ("RAFFLE".equalsIgnoreCase(gameType)) {
					returnData.append("1,");
				} else if ("PROMO".equalsIgnoreCase(gameType)) {
					returnData.append("2,");
				}

				if (version > 5.6) {
					String raffleType = rs.getString("raffle_ticket_type");
					if ("REFERENCE".equalsIgnoreCase(raffleType)) {
						returnData.append("2,");
					} else if ("ORIGINAL".equalsIgnoreCase(raffleType)) {
						returnData.append("1,");
					} else {
						returnData.append("0,");
					}
				}
				returnData.append("totalDraw,"); // Number
				// of
				// Active
				// Draws

				if (version <= 6.5 && "FortuneTwo".equalsIgnoreCase(gameName)) {
					Map<String, BetDetailsBean> priceMap = Util.getGameMap().get(gameId).getPriceMap();
					Iterator<String> betTypeIter = priceMap.keySet().iterator();
					String betType = null;
					while (betTypeIter.hasNext()) {
						betType = betTypeIter.next();
						if (!(betType.contains("Banker"))) {
							returnData.append(priceMap.get(betType).getUnitPrice() + ":");
						}
					}
					returnData.deleteCharAt(returnData.length() - 1);
					returnData.append(",");

				} else if (version <= 6.5 && "FortuneThree".equalsIgnoreCase(gameName)) {
					Map<String, BetDetailsBean> priceMap = Util.getGameMap().get(gameId).getPriceMap();
					Iterator<String> betTypeIter = priceMap.keySet().iterator();
					String betType = null;
					while (betTypeIter.hasNext()) {
						betType = betTypeIter.next();
						if (!(betType.contains("Banker"))) {
							returnData.append(priceMap.get(betType).getUnitPrice() + ":");
						}
					}
					returnData.deleteCharAt(returnData.length() - 1);
					returnData.append(",");
				}

				else {
					// returnData.append(Util.convertCollToStr(new
					// TreeMap<String,
					// BetDetailsBean>(Util.getGameMap().get(gameName).getPriceMap()).values()).replace(",
					// ", ":") + ",");

					Map<String, BetDetailsBean> map1 = new TreeMap<String, BetDetailsBean>(
							Util.getGameMap().get(gameId).getPriceMap());
					ValueComparator comparator = new ValueComparator(map1);
					Map<String, BetDetailsBean> map2 = new TreeMap<String, BetDetailsBean>(comparator);
					map2.putAll(map1);
					Iterator<String> iterator = map2.keySet().iterator();
					while (iterator.hasNext()) {
						BetDetailsBean bean = map1.get(iterator.next());
						if ("ACTIVE".equals(bean.getBetStatus()))
							// returnData.append(bean.getUnitPrice()).append("~").append(bean.getMaxBetAmtMultiple()).append("~").append((bean.getBetStatus()
							// ==
							// null)?"":(bean.getBetStatus().equals("ACTIVE"))?1:0).append("(").append(bean.getBetCode()).append("-").append(bean.getBetOrder()).append(")").append(":");
							returnData.append(bean.getBetCode()).append("~").append(bean.getUnitPrice()).append("~")
									.append(bean.getMaxBetAmtMultiple()).append(":");
					}
					returnData.deleteCharAt(returnData.length() - 1);
					returnData.append(",");
				}

				gameData.put(gameId, returnData);
				offlineFzTimeMap.put(gameId, rs.getInt("offline_freeze_time"));
				retSaleComm.put(gameId, Util.getSaleCommVariance(orgId, rs.getInt("game_id")));
			}

			retBean.setUserId(userId);
			retBean.setGameData(gameData);
			retBean.setOffline(isOffline);
			retBean.setOfflineFzTimeMap(offlineFzTimeMap);
			retBean.setRetSaleComm(retSaleComm);
			retBean.setPartyType("RETAILER");
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			ServiceRequest req = new ServiceRequest();
			req.setServiceName(ServiceName.DRAWGAME);
			req.setServiceMethod(ServiceMethodName.FETCH_DG_DATA_OFFLINE);
			req.setServiceData(retBean);

			ServiceResponse resp = delegate.getResponse(req);
			if (resp.getIsSuccess()) {
				retBean = (OfflineRetailerBean) resp.getResponseData();
			} else {
				throw new LMSException("Some Error");
			}

			gameData = retBean.getGameData();
			returnData = new StringBuilder("");
			Iterator<Map.Entry<Integer, StringBuilder>> itr = gameData.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, StringBuilder> pair = itr.next();
				returnData.append(pair.getValue() + "#");
			}
			returnData.deleteCharAt(returnData.length() - 1);

			if (isOffline) {
				returnData.append("|PromoInfo:");
				selQry = "select scheme_id,sale_game_id,promo_game_id,sale_ticket_amt,if(valid_for_draw='INDOOR',1,if(valid_for_draw='OUTDOOR',0,2)) valid_for_draw from st_dg_promo_scheme where status='ACTIVE'";
				pstmt = con.prepareStatement(selQry);
				rs = pstmt.executeQuery();
				boolean isPromo = false;
				while (rs.next()) {
					isPromo = true;
					returnData.append(rs.getString("scheme_id") + ",");
					returnData.append(rs.getString("sale_game_id") + ",");
					returnData.append(rs.getString("promo_game_id") + ",");
					returnData.append(rs.getString("sale_ticket_amt") + ",");
					returnData.append(rs.getString("valid_for_draw") + "#");
				}
				if (isPromo) {
					returnData.deleteCharAt(returnData.length() - 1);
				} else {
					returnData.delete(returnData.length() - "|PromoInfo:".length(), returnData.length());
				}
			}
			logger.debug("***Game Info for Ret User**" + userId + "****" + returnData + "****");
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Some Error");
		} finally {
			// DBConnect.closeCon(con);
		}
		return returnData.toString();
	}

	public static Map<Integer, List<PromoGameBean>> getPromoGameBeanMap(Connection con) throws SQLException {

		Map<Integer, List<PromoGameBean>> promoGameBeanMap = new HashMap<Integer, List<PromoGameBean>>();
		Statement stmt = con.createStatement();
		ResultSet gameRs = stmt.executeQuery(
				"select game_id,game_name_dev,game_name from st_dg_game_master where game_status !='SALE_CLOSE'");
		while (gameRs.next()) {
			PreparedStatement promoPstmt = con.prepareStatement(
					"select promo_game_id,promo_ticket_type,promo_game_type,no_of_free_tickets,no_of_draws,game_name_dev as promo_game_name,game_name promo_display_name from st_dg_promo_scheme ps,st_dg_game_master gm where ps.scheme_id=gm.game_id and sale_game_id="
							+ gameRs.getShort("game_id") + "  and status='ACTIVE'");
			ResultSet rs = promoPstmt.executeQuery();
			PromoGameBean promoBean = null;
			List<PromoGameBean> promoGameList = new ArrayList<PromoGameBean>();
			String gameName = gameRs.getString("game_name");
			while (rs.next()) {

				promoBean = new PromoGameBean();
				promoBean.setPromoGameNo(rs.getInt("promo_game_id"));
				promoBean.setPromoGametype(rs.getString("promo_game_type"));
				promoBean.setPromoTicketType(rs.getString("promo_ticket_type"));
				promoBean.setPromoGameDisplayName(rs.getString("promo_display_name"));
				if (rs.getString("promo_game_name") != null) {
					promoBean.setPromoGameName(rs.getString("promo_game_name"));
				}
				if (rs.getString("no_of_free_tickets") != null) {
					promoBean.setNoOfPromoTickets(rs.getInt("no_of_free_tickets"));
				}
				if (rs.getString("no_of_draws") != null) {
					promoBean.setNoOfDraws(rs.getInt("no_of_draws"));
				}
				if (gameName != null) {
					promoBean.setMainGameName(gameName);
				}

				promoGameList.add(promoBean);
			}
			promoGameBeanMap.put(gameRs.getInt("game_id"), promoGameList);

		}
		return promoGameBeanMap;
	}

	public String buildTwelveByTwentyFourData(KenoPurchaseBean kenoPurchaseBean, UserInfoBean userBean) {
		String time = kenoPurchaseBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");
		double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		String advMsgString = Utility.getPropertyValue("ADV_MESSAGE_FOR_PROMO_GAME");
		String topAdvMsg = "";
		String bottomAdvMsg = "";
		String tempMsg = "";

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);

		String balance = nf.format(bal).replaceAll(",", "");

		int listSize = kenoPurchaseBean.getDrawDateTime().size();
		StringBuilder drawTimeBuild = new StringBuilder("");

		for (int i = 0; i < listSize; i++) {
			drawTimeBuild.append(("|DrawDate:" + kenoPurchaseBean.getDrawDateTime().get(i)).replaceFirst(" ", "#")
					.replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
		}
		StringBuilder finalData = new StringBuilder("");
		finalData.append("|PromoTkt:");

		// if (userBean.getTerminalBuildVersion() <
		// Double.parseDouble(Utility.getPropertyValue("CURRENT_TERMINAL_BUILD_VERSION"))
		// && "NIGERIA".equals(countryDeployed))
		// finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() +
		// kenoPurchaseBean.getReprintCount() +
		// ((kenoPurchaseBean.getTicket_no() +
		// kenoPurchaseBean.getReprintCount()).length() ==
		// ConfigurationVariables.tktLenB &&
		// LMSFilterDispatcher.isBarCodeRequired ?
		// kenoPurchaseBean.getBarcodeCount() : "") + "|Date:" + time);
		// else
		finalData.append("TicketNo:" + kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount() + "|brCd:"
				+ kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()
				+ ((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB
						&& LMSFilterDispatcher.isBarCodeRequired) ? kenoPurchaseBean.getBarcodeCount() : "")
				+ "|Date:" + time);
		int noOfPanels = kenoPurchaseBean.getNoOfPanel();
		String[] playTypeStr = kenoPurchaseBean.getPlayType();
		for (int i = 0; i < noOfPanels; i++) {
			String panelPrice = "|PanelPrice:"
					+ nf.format(kenoPurchaseBean.getBetAmountMultiple()[i] * kenoPurchaseBean.getUnitPrice()[i]
							* kenoPurchaseBean.getNoOfLines()[i] * kenoPurchaseBean.getNoOfDraws()).replaceAll(",", "");
			finalData.append("|PlayType:" + playTypeStr[i] + "|Num:" + kenoPurchaseBean.getPlayerData()[i] + panelPrice
					+ "|QP:" + kenoPurchaseBean.getIsQuickPick()[i]);
		}
		finalData.append("|TicketCost:").append(nf.format(kenoPurchaseBean.getTotalPurchaseAmt()).replaceAll(",", ""))
				.append(drawTimeBuild.toString()).append("|balance:").append(balance).append("|");
		// 15@TOP~Top Message1|Top Message2&Bottom~Bottom Message1|Bottom
		// Message2
		if (advMsgString != null) {
			String gameArr[] = advMsgString.split("#"); // 15@TOP~Top
														// Message1|Top
														// Message2&Bottom~Bottom
														// Message1|Bottom
														// Message2
			for (int iLoop = 0; iLoop < gameArr.length; iLoop++) {
				String gameWiseArr[] = gameArr[iLoop].split("@");
				if (Integer.parseInt(gameWiseArr[0]) == kenoPurchaseBean.getGameId()) { // TOP~Top
																						// Message1|Top
																						// Message2&Bottom~Bottom
																						// Message1|Bottom
																						// Message2
					String msgArr[] = gameWiseArr[1].split("&");
					for (int jLoop = 0; jLoop < msgArr.length; jLoop++) {
						tempMsg = "";
						String msgType[] = msgArr[jLoop].split("~");
						String msg[] = msgType[1].split("\\|");
						for (int kLoop = 0; kLoop < msg.length; kLoop++) {
							tempMsg += msg[kLoop] + "~";
						}
						tempMsg = tempMsg.substring(0, tempMsg.length() - 1);
						if ("TOP".equalsIgnoreCase(msgType[0])) {
							topAdvMsg = "topAdvMsg:" + tempMsg + "|";
						} else if ("Bottom".equalsIgnoreCase(msgType[0])) {
							bottomAdvMsg = "bottomAdvMsg:" + tempMsg + "|";
						}
					}
				}
			}
		}
		return finalData.toString() + topAdvMsg + bottomAdvMsg;
	}

	public void cancelPromoTkts(UserInfoBean userInfoBean, KenoPurchaseBean promoPurchaseBean) throws LMSException {
		CancelTicketBean cancelTicketBean = null;
		int barCodeCount = -1;
		String ticketNo = null;

		try {
			// Cancelling Promo Tkt
			ticketNo = promoPurchaseBean.getTicket_no()
					+ Util.getRpcAppenderForTickets(promoPurchaseBean.getTicket_no().length());
			cancelTicketBean = new CancelTicketBean();
			cancelTicketBean.setBarCodeCount(barCodeCount);
			cancelTicketBean.setTicketNo(ticketNo);
			cancelTicketBean.setPartyId(userInfoBean.getUserOrgId());
			cancelTicketBean.setPartyType(userInfoBean.getUserType());
			cancelTicketBean.setUserId(userInfoBean.getUserId());
			cancelTicketBean.setCancelChannel(promoPurchaseBean.getPurchaseChannel());
			cancelTicketBean.setRefMerchantId(promoPurchaseBean.getRefMerchantId());
			cancelTicketBean.setAutoCancel(true);
			cancelTicketBean = cancelTicket(cancelTicketBean, userInfoBean, true, "CANCEL_SERVER");

			/*
			 * //Cancelling Main Tkt, Will Also Cancel Attached Promo Tkt
			 * ticketNo = mainPurchaseBean.getTicket_no() +
			 * Util.getRpcAppenderForTickets(mainPurchaseBean.getTicket_no().
			 * length()); cancelTicketBean = new CancelTicketBean();
			 * cancelTicketBean.setBarCodeCount(barCodeCount);
			 * cancelTicketBean.setTicketNo(ticketNo);
			 * cancelTicketBean.setPartyId(userInfoBean.getUserOrgId());
			 * cancelTicketBean.setPartyType(userInfoBean.getUserType());
			 * cancelTicketBean.setUserId(userInfoBean.getUserId());
			 * cancelTicketBean.setCancelChannel(mainPurchaseBean.
			 * getPurchaseChannel());
			 * cancelTicketBean.setRefMerchantId(mainPurchaseBean.
			 * getRefMerchantId()); cancelTicketBean.setAutoCancel(true);
			 * cancelTicketBean = cancelTicket(cancelTicketBean, userInfoBean,
			 * true, "CANCEL_SERVER");
			 */

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static boolean checkMandatoryDownload(int userId, double terminal_version, Connection con) {

		boolean status = false;
		Statement st = null;
		ResultSet rs = null;
		String isMandatory = "";
		String isDownloadAvailable = "";
		double version = 0.00;

		try {
			String checkMandatoryDownload = "select hd.isMandatory mandatory, rom.is_download_available download_available, rom.expected_version current_version  from st_lms_ret_offline_master rom inner join st_lms_htpos_download hd on rom.expected_version = hd.version where rom.user_id = "
					+ userId
					+ " and device_id = (select device_id from st_lms_htpos_device_master where device_type = rom.device_type);";

			st = con.createStatement();

			rs = st.executeQuery(checkMandatoryDownload);

			if (rs.next()) {
				isMandatory = rs.getString("mandatory");
				isDownloadAvailable = rs.getString("download_available");
				version = rs.getDouble("current_version");
			}

			status = (terminal_version == version || terminal_version > version) ? true
					: ("YES".equalsIgnoreCase(isDownloadAvailable)
							? ("YES".equalsIgnoreCase(isMandatory) ? false : true) : true);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return status;
	}

	public static PwtVerifyTicketBean payTpPwt(String verCode, PwtVerifyTicketBean pwtBean, UserInfoBean userInfoBean)
			throws LMSException {
		JSONObject requestObject = new JSONObject();
		ServiceRequest sReq = null;
		Connection con = null;
		List<Long> refTransId;
		try {
			con = getConnectionObject();
			refTransId = pwtMgmtDaoImpl.getInstance().payTpPwtProcessAtLMS(verCode, pwtBean, userInfoBean, con);
			pwtBean.setRecieptNumber(String.valueOf(refTransId));
			boolean isSuccess = false;
			boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "DG_PWT", "" + pwtBean.getTotalWinAmt());
			if (!isFraud) {
				if (refTransId.size() > 0) {
					sReq = new ServiceRequest();
					sReq.setServiceName(ServiceName.PWT_MGMT);
					sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_TP_UPDATE);
					requestObject.put("ticketNumber", pwtBean.getTicketNumber());
					requestObject.put("verificationCode", verCode);
					// requestObject.put("merchantCode",
					// pwtBean.getMerchantCode());
					requestObject.put("merchantCode", "LMS"); // This need to
																// change and
																// get
																// merchant-Id
																// from
																// transaction
																// manager at
																// DGE.
					requestObject.put("refMerchantId", "WGRL");
					requestObject.put("partyId", userInfoBean.getUserOrgId());
					requestObject.put("channelType", userInfoBean.getChannel());
					requestObject.put("refTransId",
							refTransId.toString().replace("[", "").replace("]", "").replace(" ", ""));
					requestObject.put("userId", userInfoBean.getUserId());
					sReq.setServiceData(requestObject);
					IServiceDelegate delegate = ServiceDelegate.getInstance();
					String responseString = delegate.getResponseString(sReq);
					// String
					// responseString="{\"responseCode\":0,\"winningAmt\":45.0,\"purchaseTime\":\"2015-03-12
					// 15:15:52\",\"purchaseAmt\":5.5,\"refTxnId\":\"565\"}";
					JsonObject data = new JsonParser().parse(responseString).getAsJsonObject();
					if (data.get("responseCode").getAsInt() == 0) {
						isSuccess = pwtMgmtDaoImpl.getInstance().updatePlayerPwtMerchantTransaction(pwtBean,
								data.get("refTxnId").getAsString(), con);
						pwtMgmtDaoImpl.getInstance().updateMerchantCode(pwtBean, con);
						con.commit();
						if (!isSuccess) {
							throw new LMSException(LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_CODE,
									LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_MESSAGE);
						}
					} else {
						// con.rollback();
						throw new LMSException(data.get("responseCode").getAsInt(),
								data.get("responseMsg").getAsString());
					}
				}
			} else {
				throw new LMSException(EmbeddedErrors.PWT_LIMIT_EXCEED_ERROR_CODE, EmbeddedErrors.PWT_LIMIT_EXCEED);
			}
		} catch (LMSException el) {
			throw el;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return pwtBean;
	}
}

class ValueComparator implements Comparator<String> {
	Map<String, BetDetailsBean> map;

	public ValueComparator(Map<String, BetDetailsBean> map) {
		this.map = map;
	}

	public int compare(String a, String b) {
		if (map.get(a).getBetOrder() < map.get(b).getBetOrder()) {
			return -1;
		} else {
			return 1;
		}
	}
}
