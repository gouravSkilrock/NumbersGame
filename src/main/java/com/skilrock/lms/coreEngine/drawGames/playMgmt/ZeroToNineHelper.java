package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.itg.IDBarcode.IDBarcode;
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
import com.skilrock.lms.dge.beans.ZeroToNinePurchaseBean;
import com.skilrock.lms.dge.beans.ZeroToNineRequestBean;
import com.skilrock.lms.dge.beans.ZeroToNineResponseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class ZeroToNineHelper {
	static Log logger = LogFactory.getLog(ZeroToNineHelper.class);

	private static ZeroToNineHelper classInstance = null;

	private ZeroToNineHelper() {
	}

	public static synchronized ZeroToNineHelper getInstance() {
		if (classInstance == null)
			classInstance = new ZeroToNineHelper();
		return classInstance;
	}

	public ZeroToNinePurchaseBean zeroToNinePurchaseTicket(UserInfoBean userBean, ZeroToNinePurchaseBean zeroToNinePurchaseBean) throws LMSException {

		if (isDrawAvailable(zeroToNinePurchaseBean.getGameId()) || DrawGameRPOSHelper.chkFreezeTimeSale(zeroToNinePurchaseBean.getGameId())) {
			zeroToNinePurchaseBean.setSaleStatus("NO_DRAWS");
			return zeroToNinePurchaseBean;
		}

		long balDed = 0;
		int tickUpd = 0;
		String status = "";
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		Connection con = null;
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		try {
			if (zeroToNineDataValidation(zeroToNinePurchaseBean)) {
				zeroToNinePurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(), zeroToNinePurchaseBean.getGameId(), "PLAYER", "SALE", "DG"));
				zeroToNinePurchaseBean.setSaleStatus("FAILED");
				double dgeTicketPrice = 0.0;
				double lmsTicketPrice = 0.0;
				int noOfPanel = zeroToNinePurchaseBean.getTotalNoOfPanels();
				int noOfLines[] = new int[noOfPanel];
				int betAmtMulArr[] = zeroToNinePurchaseBean.getBetAmountMultiple();
				double unitPrice = Util.getUnitPrice(zeroToNinePurchaseBean.getGameId(), zeroToNinePurchaseBean.getPlayType()[0]);
				for (int i = 0; i < noOfPanel; i++) {
					noOfLines[i] = 1;
					dgeTicketPrice += unitPrice * zeroToNinePurchaseBean.getNoOfDraws() * betAmtMulArr[i];
				}

				lmsTicketPrice = CommonMethods.roundDrawTktAmt(dgeTicketPrice);
				lmsTicketPrice = CommonMethods.fmtToTwoDecimal(lmsTicketPrice);
				logger.debug("DGE Purchase Amount - " + dgeTicketPrice);
				logger.debug("LMS Purchase Amount - " + lmsTicketPrice);
				zeroToNinePurchaseBean.setUnitPrice(unitPrice);
				zeroToNinePurchaseBean.setNoOfLines(noOfLines);

				zeroToNinePurchaseBean.setTotalPurchaseAmt(dgeTicketPrice);
				zeroToNinePurchaseBean.setUnitPrice(Util.getUnitPrice(zeroToNinePurchaseBean.getGameId(), zeroToNinePurchaseBean.getPlayType()[0]));
				if (!userBean.getUserType().equals("RETAILER")) {
					return zeroToNinePurchaseBean;
				}
				con = DBConnect.getConnection();
				con.setAutoCommit(false);
	
				int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
				helper.checkLastPrintedTicketStatusAndUpdate(userBean, Long.parseLong(zeroToNinePurchaseBean.getLastSoldTicketNo()), "TERMINAL", zeroToNinePurchaseBean.getRefMerchantId(), autoCancelHoldDays, zeroToNinePurchaseBean.getActionName(), zeroToNinePurchaseBean.getLastGameId(), con);

				boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", lmsTicketPrice + "", con);
				if (!isFraud) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, zeroToNinePurchaseBean.getGameId(), lmsTicketPrice, zeroToNinePurchaseBean.getPlrMobileNumber(), con);
					oldTotalPurchaseAmt = zeroToNinePurchaseBean.getTotalPurchaseAmt();
				} else {
					logger.debug("Responsing Gaming not allowed to sale");
					zeroToNinePurchaseBean.setSaleStatus("FRAUD");
					return zeroToNinePurchaseBean;
				}
			} else {
				logger.debug("Data validation returns false");
				zeroToNinePurchaseBean.setSaleStatus("FAILED");
				return zeroToNinePurchaseBean;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ServiceRequest sReq = null;
		try {
			if (balDed > 0) {
				zeroToNinePurchaseBean.setRefTransId(balDed + "");
				con.commit();

				ZeroToNineRequestBean requestBean = new ZeroToNineRequestBean();
				requestBean.setBetAmountMultiple(zeroToNinePurchaseBean.getBetAmountMultiple());
				requestBean.setDrawIdTableMap(zeroToNinePurchaseBean.getDrawIdTableMap());
				requestBean.setDrawDateTime(zeroToNinePurchaseBean.getDrawDateTime());
				requestBean.setGame_no(zeroToNinePurchaseBean.getGame_no());
				requestBean.setGameId(zeroToNinePurchaseBean.getGameId());
				requestBean.setIsAdvancedPlay(zeroToNinePurchaseBean.getIsAdvancedPlay());
				requestBean.setIsQP(zeroToNinePurchaseBean.getIsQP());
				requestBean.setIsQuickPick(zeroToNinePurchaseBean.getIsQuickPick());
				requestBean.setNoOfDraws(zeroToNinePurchaseBean.getNoOfDraws());
				requestBean.setNoOfLines(zeroToNinePurchaseBean.getNoOfLines());
				requestBean.setNoPicked(zeroToNinePurchaseBean.getNoPicked());
				requestBean.setPartyId(zeroToNinePurchaseBean.getPartyId());
				requestBean.setPartyType(zeroToNinePurchaseBean.getPartyType());
				requestBean.setPlayerData(zeroToNinePurchaseBean.getPlayerData());
				requestBean.setPlayType(zeroToNinePurchaseBean.getPlayType());
				requestBean.setPurchaseChannel(zeroToNinePurchaseBean.getPurchaseChannel());
				requestBean.setRefMerchantId(zeroToNinePurchaseBean.getRefMerchantId());
				requestBean.setRefTransId(zeroToNinePurchaseBean.getRefTransId());
				requestBean.setUserId(zeroToNinePurchaseBean.getUserId());
				requestBean.setUserMappingId(zeroToNinePurchaseBean.getUserMappingId());
				requestBean.setServiceId(zeroToNinePurchaseBean.getServiceId());
				requestBean.setUnitPrice(zeroToNinePurchaseBean.getUnitPrice());
				requestBean.setTotalPurchaseAmt(zeroToNinePurchaseBean.getTotalPurchaseAmt());
				requestBean.setTotalNoOfPanels(zeroToNinePurchaseBean.getTotalNoOfPanels());

				sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.ZEROTONINE_MGMT);
				sReq.setServiceMethod(ServiceMethodName.ZEROTONINE_PURCHASE_TICKET);
				sReq.setServiceData(requestBean);
				IServiceDelegate delegate = ServiceDelegate.getInstance();
				String responseString = delegate.getResponseString(sReq);
				ZeroToNineResponseBean responseBean = new Gson().fromJson(responseString, ZeroToNineResponseBean.class);

				if (responseBean.getIsSuccess()) {
					zeroToNinePurchaseBean.setSaleStatus(responseBean.getSaleStatus());
					zeroToNinePurchaseBean.setTicket_no(responseBean.getTicketNo());
					zeroToNinePurchaseBean.setBarcodeCount(responseBean.getBarcodeCount());
					zeroToNinePurchaseBean.setReprintCount(responseBean.getReprintCount());
					zeroToNinePurchaseBean.setNoOfDraws(responseBean.getNoOfDraws());
					zeroToNinePurchaseBean.setPurchaseTime(responseBean.getPurchaseTime());
					zeroToNinePurchaseBean.setIsQuickPick(responseBean.getIsQuickPick());
					zeroToNinePurchaseBean.setPickedNumbers(responseBean.getPickedNumbers());
					zeroToNinePurchaseBean.setTotalPurchaseAmt(responseBean.getTotalPurchaseAmt());
					zeroToNinePurchaseBean.setDrawDateTime(responseBean.getDrawDateTime());
					zeroToNinePurchaseBean.setBetAmountMultiple(responseBean.getBetAmountMultiple());
					zeroToNinePurchaseBean.setClaimEndTime(responseBean.getClaimEndTime());
					

					modifiedTotalPurchaseAmt = zeroToNinePurchaseBean.getTotalPurchaseAmt();

					con = DBConnect.getConnection();
					if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
						balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean, zeroToNinePurchaseBean.getGameId(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed, con);
					}
					tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed, zeroToNinePurchaseBean.getTicket_no(), zeroToNinePurchaseBean.getGameId(), userBean, zeroToNinePurchaseBean.getPurchaseChannel(), con);

					if (tickUpd == 1) {
						status = "SUCCESS";
						zeroToNinePurchaseBean.setSaleStatus(status);
						if (!zeroToNinePurchaseBean.getBarcodeType().equals("applet")) {
							 IDBarcode.getBarcode(zeroToNinePurchaseBean.getTicket_no() + zeroToNinePurchaseBean.getReprintCount());
						}
						return zeroToNinePurchaseBean;
					} else {
						status = "FAILED";
						zeroToNinePurchaseBean.setSaleStatus(status);
						helper.cancelTicket(zeroToNinePurchaseBean.getTicket_no() + zeroToNinePurchaseBean.getReprintCount(),
								zeroToNinePurchaseBean.getPurchaseChannel(),
								zeroToNinePurchaseBean.getDrawIdTableMap(),
								zeroToNinePurchaseBean.getGame_no(),
								zeroToNinePurchaseBean.getPartyId(),
								zeroToNinePurchaseBean.getPartyType(),
								zeroToNinePurchaseBean.getRefMerchantId(), userBean,
								zeroToNinePurchaseBean.getRefTransId());

						return zeroToNinePurchaseBean;
					}
				} else {
					if(zeroToNinePurchaseBean.getSaleStatus() == null) {
						zeroToNinePurchaseBean.setSaleStatus("FAILED");
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, zeroToNinePurchaseBean.getGame_no(), "FAILED", balDed);
						return zeroToNinePurchaseBean;
					} else if("ERROR_TICKET_LIMIT".equalsIgnoreCase(zeroToNinePurchaseBean.getSaleStatus())) {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, zeroToNinePurchaseBean.getGame_no(), "FAILED", balDed);
						return zeroToNinePurchaseBean;
					} else {
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, zeroToNinePurchaseBean.getGame_no(), "FAILED", balDed);
						return zeroToNinePurchaseBean;
					}
				}
			} else {
				if (balDed == -1) {
					status = "AGT_INS_BAL";
				} else if (balDed == -2) {
					status = "FAILED";
				} else if (balDed == 0) {
					status = "RET_INS_BAL";
				}
				zeroToNinePurchaseBean.setSaleStatus(status);
				return zeroToNinePurchaseBean;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return zeroToNinePurchaseBean;	
	}

	public boolean zeroToNineDataValidation(ZeroToNinePurchaseBean zeroToNinePurchaseBean) {
		int maxBetSize = Util.getGameMasterLMSBean(zeroToNinePurchaseBean.getGameId()).getPriceMap().get("zeroToNine").getMaxBetAmtMultiple();

		int isQP = zeroToNinePurchaseBean.getIsQP();
		if (isQP == 1) {
			if (zeroToNinePurchaseBean.getTotalNoOfPanels() > maxBetSize) {
				return false;
			}

			return true;
		} else {
			String[] pickedNumbers = zeroToNinePurchaseBean.getPlayerData();
			int pick = 0;
			for(int i=0; i<pickedNumbers.length; i++) {
				pick = Integer.parseInt(pickedNumbers[i]);
				if(pick<0 || pick>9) {
					return false;
				}
			}

			int betSize = 0;
			for(int i=0; i<zeroToNinePurchaseBean.getBetAmountMultiple().length; i++) {
				betSize += zeroToNinePurchaseBean.getBetAmountMultiple()[i];
			}
			if(betSize>maxBetSize) {
				return false;
			}

			return true;
		}
	}

	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}
}