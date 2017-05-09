package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoRequestBean;
import com.skilrock.lms.dge.beans.KenoResponseBean;
import com.skilrock.lms.dge.gameconstants.TwelveByTwentyFourConstants;
import com.skilrock.lms.web.drawGames.common.Util;

public class TwelveByTwentyFourHelper {

	Log logger = LogFactory.getLog(TwelveByTwentyFourHelper.class);

	public KenoPurchaseBean commonPurchseProcess(UserInfoBean userBean, KenoPurchaseBean purchaseBean)
			throws LMSException, SQLException {
		purchaseBean.setPromotkt(false);
		purchaseBean = twelveByTwentyFourPurchaseTicket(userBean, purchaseBean);
		String saleStatus = purchaseBean.getSaleStatus();
		if ("SUCCESS".equalsIgnoreCase(saleStatus)) {
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			purchaseBean = helper.commonPromoPurchaseProcess(purchaseBean, userBean);
			if ("SUCCESS".equalsIgnoreCase(purchaseBean.getPromoSaleStatus())) {
				return purchaseBean;
			} else {
				helper.cancelTicket(purchaseBean.getTicket_no()
						+ purchaseBean.getReprintCount(),
						purchaseBean.getPurchaseChannel(),
						purchaseBean.getDrawIdTableMap(),
						purchaseBean.getGame_no(),
						purchaseBean.getPartyId(),
						purchaseBean.getPartyType(),
						purchaseBean.getRefMerchantId(), userBean,
						purchaseBean.getRefTransId());
			}
		}
		return purchaseBean;
	}

	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}

	public KenoPurchaseBean twelveByTwentyFourPurchaseTicket(UserInfoBean userBean, KenoPurchaseBean purchaseBean) throws LMSException {
		purchaseBean.setSaleStatus("FAILED");
		Connection con = null;
		long balDed = 0;
		double dgeTotPurAmt = 0.0;
		double lmsTotPurAmt = 0.0;
		String status = "";
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.TWELVEBYTWENTYFOUR_MGMT);
		sReq.setServiceMethod(ServiceMethodName.TWELVEBYTWENTYFOUR_PURCHASE_TICKET);
		KenoRequestBean requestBean = new KenoRequestBean();
		sReq.setServiceData(requestBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		try {
			if (isDrawAvailable(purchaseBean.getGameId())) {
				purchaseBean.setSaleStatus("NO_DRAWS");
				return purchaseBean;
			}
			
			if(!purchaseBean.isPromotkt() && DrawGameRPOSHelper.chkFreezeTimeSale(purchaseBean.getGameId())) {
				purchaseBean.setSaleStatus("NO_DRAWS");
				return purchaseBean;
			}

			if (!twelveByTwentyFourValidateData(purchaseBean)) {
				logger.info("Data Validation returned false");
				return purchaseBean;
			}

			int noOfPanel = purchaseBean.getNoOfPanel();
			String playTypeArr[] = purchaseBean.getPlayType();
			String[] noPickStr = purchaseBean.getNoPicked();
			int noOfLines[] = new int[noOfPanel];
			int betAmtMulArr[] = purchaseBean.getBetAmountMultiple();
			double unitPriceArr[] = new double[noOfPanel];
			for (int i = 0; i < noOfPanel; i++) {
				String playType = playTypeArr[i];
				String[] noPick = noPickStr[i].split(",");
				int[] n = new int[noPick.length];
				for (int j = 0; j < noPick.length; j++) {
					n[j] = Integer.parseInt(noPick[j]);
				}
				if (playType.equalsIgnoreCase("Direct12") || playType.equalsIgnoreCase("AllOdd") || playType.equalsIgnoreCase("AllEven")
					||	playType.equalsIgnoreCase("First12") || playType.equalsIgnoreCase("Last12") || playType.equalsIgnoreCase("OddEven")
					|| playType.equalsIgnoreCase("EvenOdd") || playType.equalsIgnoreCase("JumpOddEven") || playType.equalsIgnoreCase("JumpEvenOdd")
					|| playType.equalsIgnoreCase("Match10")) {
					
					noOfLines[i] = 1;
				}
				if (playType.equalsIgnoreCase("Perm12")) {
					noOfLines[i] = "13".equals(noPickStr[i]) ? 13 : 91 ;
				}
				unitPriceArr[i] = Util.getUnitPrice(purchaseBean.getGameId(), playTypeArr[i]);
				dgeTotPurAmt += noOfLines[i] * unitPriceArr[i] * purchaseBean.getNoOfDraws() * betAmtMulArr[i];
			}
			purchaseBean.setUnitPrice(unitPriceArr);
			purchaseBean.setNoOfLines(noOfLines);

			String doMathRounding = Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_SALE_AMT");
			if("YES".equals(doMathRounding)) {
				lmsTotPurAmt = CommonMethods.roundDrawTktAmt(dgeTotPurAmt);
				lmsTotPurAmt = CommonMethods.fmtToTwoDecimal(lmsTotPurAmt);
			} else {
				lmsTotPurAmt = CommonMethods.fmtToTwoDecimal(dgeTotPurAmt);
			}

			logger.debug("DGE Purchase Amount - " + dgeTotPurAmt);
			logger.debug("LMS Purchase Amount - " + lmsTotPurAmt);

			if (dgeTotPurAmt <= 0) {
				purchaseBean.setSaleStatus("FAILED");
				return purchaseBean;
			}
			if(purchaseBean.isPromotkt())
				purchaseBean.setTotalPurchaseAmt(0);
			else
				purchaseBean.setTotalPurchaseAmt(dgeTotPurAmt);

			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			if("TERMINAL".equals(purchaseBean.getDeviceType())) {
				int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
				DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
				helper.checkLastPrintedTicketStatusAndUpdate(userBean, Long.parseLong(purchaseBean.getLastSoldTicketNo()), purchaseBean.getDeviceType(), purchaseBean.getRefMerchantId(), autoCancelHoldDays, purchaseBean.getActionName(), purchaseBean.getLastGameId(), con);
			}

			logger.debug("SALE_AUTO_CANCEL_LOGS:SALE Continue for the new ticket");

			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", lmsTotPurAmt + "", con);
			//boolean isFraud = false;
			if (!isFraud) {
				balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, purchaseBean.getGameId(), lmsTotPurAmt, purchaseBean.getPlrMobileNumber(), con);
				oldTotalPurchaseAmt = purchaseBean.getTotalPurchaseAmt();
				if (balDed > 0) {
					purchaseBean.setRefTransId(balDed + "");
					con.commit();
				} else {
					if (balDed == -1) {
						status = "AGT_INS_BAL";// Agent has insufficient
						// Balance
					} else if (balDed == -2) {
						status = "FAILED";// Error LMS
					} else if (balDed == 0) {
						status = "RET_INS_BAL";// Retailer has insufficient
						// Balance
					}
					else if (balDed == -5) {
						status = "UNAUTHORISED";// Retailer status is INACTIVE, cannot sale ticket  						
					}
					purchaseBean.setSaleStatus(status);
					return purchaseBean;
				}
			} else {
				logger.debug("Responsing Gaming not allowed to sale");
				purchaseBean.setSaleStatus("FRAUD");
				return purchaseBean;
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
			requestBean.setBetAmountMultiple(purchaseBean.getBetAmountMultiple());
			requestBean.setDrawIdTableMap(purchaseBean.getDrawIdTableMap());
			requestBean.setDrawDateTime(purchaseBean.getDrawDateTime());
			requestBean.setGame_no(purchaseBean.getGame_no());
			requestBean.setGameId(purchaseBean.getGameId());
			requestBean.setIsAdvancedPlay(purchaseBean.getIsAdvancedPlay());
			requestBean.setIsQuickPick(purchaseBean.getIsQuickPick());
			requestBean.setNoOfDraws(purchaseBean.getNoOfDraws());
			requestBean.setNoPicked(purchaseBean.getNoPicked());
			requestBean.setPartyId(purchaseBean.getPartyId());
			requestBean.setPartyType(purchaseBean.getPartyType());
			requestBean.setPlayerData(purchaseBean.getPlayerData());
			requestBean.setPlayType(purchaseBean.getPlayType());
			requestBean.setPurchaseChannel(purchaseBean.getPurchaseChannel());
			requestBean.setRefMerchantId(purchaseBean.getRefMerchantId());
			requestBean.setRefTransId(purchaseBean.getRefTransId());
			requestBean.setUserId(purchaseBean.getUserId());
			requestBean.setUserMappingId(purchaseBean.getUserMappingId());
			requestBean.setServiceId(purchaseBean.getServiceId());
			requestBean.setPromotkt(purchaseBean.isPromotkt());
			requestBean.setUnitPrice(purchaseBean.getUnitPrice());
			requestBean.setTotalPurchaseAmt(purchaseBean.getTotalPurchaseAmt());
			requestBean.setQPPreGenerated(purchaseBean.getQPPreGenerated());

			String responseString = delegate.getResponseString(sReq);
			KenoResponseBean responseBean = new Gson().fromJson(responseString, KenoResponseBean.class);
			if (responseBean.getIsSuccess()) {
				purchaseBean.setSaleStatus(responseBean.getSaleStatus());
				purchaseBean.setTicket_no(responseBean.getTicketNo());
				purchaseBean.setBarcodeCount(responseBean.getBarcodeCount());
				purchaseBean.setNoOfDraws(responseBean.getNoOfDraws());
				purchaseBean.setPurchaseTime(responseBean.getPurchaseTime());
				purchaseBean.setReprintCount(responseBean.getReprintCount());
				purchaseBean.setPlayerData(responseBean.getPlayerData());
				//purchaseBean.setTotalPurchaseAmt(responseBean.getTotalPurchaseAmt());
				purchaseBean.setTotalPurchaseAmt(lmsTotPurAmt);
				purchaseBean.setDrawDateTime(responseBean.getDrawDateTime());
				modifiedTotalPurchaseAmt = responseBean.getTotalPurchaseAmt();
				con = DBConnect.getConnection();
				con.setAutoCommit(false);
				if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean, purchaseBean.getGameId(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed, con);
				}

				int tickUpd = orgOnLineSaleCreditUpdation.drawPromoTicketSaleTicketUpdate(balDed,
								purchaseBean.getTicket_no(), purchaseBean.getGameId(), userBean, purchaseBean.getPurchaseChannel(), con, purchaseBean.isPromotkt());
				if (tickUpd == 1) {
					AjaxRequestHelper.getLiveAmt(userBean, con);
					purchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), purchaseBean.getGameId()));

					status = "SUCCESS";
					purchaseBean.setSaleStatus(status);
					/*if (!"applet".equals(purchaseBean.getBarcodeType())) {
						IDBarcode.getBarcode(purchaseBean.getTicket_no() + purchaseBean.getReprintCount());
					}*/
					con.commit();
					return purchaseBean;
				} else {
					status = "FAILED";
					purchaseBean.setSaleStatus(status);
					// Code for cancelling the Ticket to be send to Draw
					// Game Engine
					new DrawGameRPOSHelper().cancelTicket(purchaseBean.getTicket_no() + purchaseBean.getReprintCount(),
									purchaseBean.getPurchaseChannel(), purchaseBean.getDrawIdTableMap(), purchaseBean.getGame_no(),
									purchaseBean.getPartyId(), purchaseBean.getPartyType(), purchaseBean.getRefMerchantId(), userBean,
									purchaseBean.getRefTransId());
					return purchaseBean;
				}
			} else {
				purchaseBean.setSaleStatus(responseBean.getSaleStatus());
				if (purchaseBean.getSaleStatus() == null) {
					if(responseBean.getErrorCode().equalsIgnoreCase("600")){
						purchaseBean.setSaleStatus("LIMIT_REACHED");// Error
					}
					else{
						purchaseBean.setSaleStatus("FAILED");// Error
					}
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, purchaseBean.getGameId(), "FAILED", balDed);
					return purchaseBean;
				} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(purchaseBean.getSaleStatus())) {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, purchaseBean.getGameId(), "FAILED", balDed);
					return purchaseBean;
				} else {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, purchaseBean.getGameId(), "FAILED", balDed);
					return purchaseBean;
				}
			}
		} catch (Exception se) {
			se.printStackTrace();
			if (purchaseBean.getSaleStatus() == null) {
				purchaseBean.setSaleStatus("FAILED");// Error
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, purchaseBean.getGame_no(), "FAILED", balDed);
			} else {
				orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, purchaseBean.getGame_no(), "FAILED", balDed);
			}
		} finally {
			DBConnect.closeCon(con);
		}
		return purchaseBean;
	}

	public boolean twelveByTwentyFourValidateData(KenoPurchaseBean purchaseBean) {
		if (purchaseBean != null) {
			if (purchaseBean.getNoOfDraws() < 1) {
				logger.debug("insufficient no of draws");
				return false;
			}
			if (purchaseBean.getNoOfPanel() < 1) {
				logger.debug("insufficient no of panels");
				return false;
			}

			int noOfPanel = purchaseBean.getNoOfPanel();
			String playTypeArr[] = purchaseBean.getPlayType();
			int[] qp = purchaseBean.getIsQuickPick();
			boolean[] qpPreGenerated = purchaseBean.getQPPreGenerated();
			boolean isValid = true;
			String[] pickedNumbersArr = purchaseBean.getPlayerData();
			String[] noPickedArr = purchaseBean.getNoPicked();			
			for (int i = 0; i < noOfPanel; i++) {
				String playerData = pickedNumbersArr[i];
				if (!"QP".equals(playerData)) {
					if (playTypeArr[i].contains("Direct12")) {
						int pickValue = Integer.parseInt(TwelveByTwentyFourConstants.BET_TYPE_MAP.get(playTypeArr[i]));
						if (!qpPreGenerated[i] && qp[i] != 2 || playerData.split(",").length != pickValue || Integer.parseInt(noPickedArr[i]) != pickValue) {
							isValid = false;
							break;
						}
					} /*else if (playTypeArr[i].contains("Perm")) {
						int minPickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i] + "MIN");
						int maxPickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i] + "MAX");
						int selPick = Integer.parseInt(noPickedArr[i]);
						if (qp[i] != 2 || minPickValue > playerData.split(",").length || maxPickValue < playerData.split(",").length || minPickValue > selPick || maxPickValue < selPick) {
							isValid = false;
							break;
						}
					} else if ("Banker".equals(playTypeArr[i])) {
						logger.debug("-Banker---" + playTypeArr[i] + "---" + noPickedArr[i]);

						int minULPickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i] + "ULMIN");
						int maxULPickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i] + "ULMAX");
						int minBLPickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i] + "BLMIN");
						int maxBLPickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i] + "BLMAX");
						String selPick[] = noPickedArr[i].split(",");
						int selUL = Integer.parseInt(selPick[0]);
						int selBL = Integer.parseInt(selPick[1]);

						int pickedUL = playerData.substring(0, playerData.indexOf(",UL,")).split(",").length;
						int pickedBL = playerData.substring(playerData.indexOf(",UL,") + 4, playerData.indexOf(",BL")).split(",").length;

						if (qp[i] != 2 || minULPickValue > pickedUL || maxULPickValue < pickedUL || minBLPickValue > pickedBL || maxBLPickValue < pickedBL || minULPickValue > selUL || maxULPickValue < selUL || minBLPickValue > selBL || maxBLPickValue < selBL) {
							isValid = false;
							break;
						}
					}*/
					isValid = Util.validateNumber(TwelveByTwentyFourConstants.START_RANGE, TwelveByTwentyFourConstants.END_RANGE, playerData, false);
					if (!isValid) {
						break;
					}
				} else {
					if (playTypeArr[i].contains("Direct12")) {
						int pickValue = Integer.parseInt(TwelveByTwentyFourConstants.BET_TYPE_MAP.get(playTypeArr[i]));
						if (qp[i] != 1 || Integer.parseInt(noPickedArr[i]) != pickValue) {
							isValid = false;
							break;
						}
					}
				}
			}

			if (!isValid) {
				purchaseBean.setSaleStatus("INVALID_INPUT");// Error Draw
				// setKenoPurchaseBean(kenoPurchaseBean);
				logger.info("-----------Twelve By Twenty Four Validation Error-------------------" + purchaseBean.getSaleStatus());
				return false;
			}
		} else {
			logger.debug("Twelve By Twenty Four Data Error : Null Bean Recieved ");
			return false;
		}
		return true;
	}

}
