package com.skilrock.lms.embedded.drawGames.Bingo.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.embedded.drawGames.Bingo.Beans.BingoSeventyFive;
import com.skilrock.lms.embedded.drawGames.Bingo.Beans.BingoSeventyFiveResponse;
import com.skilrock.lms.web.drawGames.common.Util;

public class BingoService {
	DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
	static Log logger = LogFactory.getLog(BingoService.class);

	IServiceDelegate delegate = null;
	AjaxRequestHelper ajaxHelper;

	public BingoService() {
		delegate = ServiceDelegate.getInstance();
		this.ajaxHelper = new AjaxRequestHelper();
	}

	public BingoService(IServiceDelegate iServiceDelegate, AjaxRequestHelper ajaxHelper) {
		delegate = iServiceDelegate;
		this.ajaxHelper = ajaxHelper;
	}

	public BingoSeventyFive commonPurchseProcessBingoSeventyFive(UserInfoBean userBean,
			BingoSeventyFive bingoSeventyFive) throws LMSException {
		try {
			bingoSeventyFivePurchaseTicket(userBean, bingoSeventyFive);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return bingoSeventyFive;
	}

	/* BingoSeventyFive - Mukesh Sharma */
	private BingoSeventyFive bingoSeventyFivePurchaseTicket(UserInfoBean userBean, BingoSeventyFive bingoSeventyFive)
			throws LMSException {
		String status = null;
		double dgeTicketPrice = 0.0;
		double lmsTicketPrice = 0.0;
		long transId = 0;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			if (checkDrawAvailability(bingoSeventyFive.getGameId())) {
				bingoSeventyFive.setSaleStatus("NO_DRAWS");
				return bingoSeventyFive;
			}
			bingoSeventyFive.setSaleStatus("FAILED");
			dgeTicketPrice = prepareUnitPriceArrayAndCalculateBingoSeventyFiveTicketPrice(bingoSeventyFive);
			if (dgeTicketPrice <= 0) {
				bingoSeventyFive.setSaleStatus("FAILED");
				return bingoSeventyFive;
			}
			bingoSeventyFive.setTotalPurchaseAmt(dgeTicketPrice);
			lmsTicketPrice = CommonMethods.fmtToTwoDecimal(dgeTicketPrice);
			checkLastPrintedTicketForTerminalOfBingoSeventyFive(userBean, bingoSeventyFive, connection);
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", String.valueOf(lmsTicketPrice),
					connection);
			if (!isFraud) {
				transId = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, bingoSeventyFive.getGameId(),lmsTicketPrice, bingoSeventyFive.getPlrMobileNumber(), connection);

				if (transId <= 0) {
					bingoSeventyFive.setSaleStatus(getStatusIfErrorInTransaction(status, transId));
					return bingoSeventyFive;
				}
				bingoSeventyFive.setRefTransId(transId + "");
				connection.commit();
			} else {
				bingoSeventyFive.setSaleStatus("FRAUD");
				return bingoSeventyFive;
			}
			connection.commit();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		try {
			BingoSeventyFiveResponse bingoResponseBean = new Gson().fromJson(
					prepareBingoSeventyFiveServiceRequestForDGE(bingoSeventyFive), BingoSeventyFiveResponse.class);
			if (bingoResponseBean.getIsSuccess()) {
				setBingoSeventyFiveDGEResponseData(bingoSeventyFive, bingoResponseBean);
				double fromDgeTicketPrice = bingoSeventyFive.getTotalPurchaseAmt();
				bingoSeventyFive.setTotalPurchaseAmt(lmsTicketPrice);
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);
				if (fromDgeTicketPrice != dgeTicketPrice) {
					fromDgeTicketPrice = CommonMethods.roundDrawTktAmt(fromDgeTicketPrice);
					bingoSeventyFive.setTotalPurchaseAmt(fromDgeTicketPrice);
					transId = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,
							bingoSeventyFive.getGameId(), fromDgeTicketPrice, lmsTicketPrice, transId, connection);
				}
				int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(transId,
						bingoSeventyFive.getTicket_no(), bingoSeventyFive.getGameId(), userBean,
						bingoSeventyFive.getPurchaseChannel(), connection);
				if (tickUpd == 1) {
					doBingoSeventyFiveSaleAfterSuccesWork(userBean, bingoSeventyFive, connection);
					connection.commit();
					return bingoSeventyFive;
				} else {
					ifErrorOccurInBingoSeventyFiveTicketUpdation(userBean, bingoSeventyFive);
					return bingoSeventyFive;
				}
			} else {
				bingoSeventyFive.setSaleStatus(bingoResponseBean.getSaleStatus());
				failedBingoSeventyFiveTransactionIfFailedAtDGE(userBean, bingoSeventyFive, transId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			failedBingoSeventyFiveTrasactionIfAnyExceptionOccur(userBean, bingoSeventyFive, transId);

		} finally {
			DBConnect.closeCon(connection);
		}
		return bingoSeventyFive;
	}

	private boolean checkDrawAvailability(int gameId) {
		return isDrawAvailable(gameId) || chkFreezeTimeSale(gameId);
	}

	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}

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

	private String getStatusIfErrorInTransaction(String status, long transId) {
		if (transId == -1) {
			status = "AGT_INS_BAL";// Agent has insufficient balance
		} else if (transId == -2) {
			status = "FAILED";// Error LMS
		} else if (transId == 0) {
			status = "RET_INS_BAL";// Retailer has insufficient Balance
		}
		return status;
	}

	private double prepareUnitPriceArrayAndCalculateBingoSeventyFiveTicketPrice(BingoSeventyFive bingoSeventyFive) {
		double dgeTicketPrice = 0.0;
		int noOfPanel = bingoSeventyFive.getNoOfPanel();
		int betAmtMulArr[] = bingoSeventyFive.getBetAmountMultiple();
		double unitPriceArr[] = new double[noOfPanel];
		for (int i = 0; i < noOfPanel; i++) {
//			preapareNoOfLinesArrayOfBingoSeventyFive(bingoSeventyFive, noOfLines, i);
			unitPriceArr[i] = Util.getUnitPrice(bingoSeventyFive.getGameId(), bingoSeventyFive.getPlayType()[i]);
			dgeTicketPrice +=  unitPriceArr[i] * bingoSeventyFive.getNoOfDraws() * betAmtMulArr[i];
		}
		bingoSeventyFive.setUnitPrice(unitPriceArr);
//		bingoSeventyFive.setNoOfLines(noOfLines);
		return dgeTicketPrice;
	}

	private void checkLastPrintedTicketForTerminalOfBingoSeventyFive(UserInfoBean userBean,
			BingoSeventyFive bingoSeventyFive, Connection connection) throws LMSException {
		if ("TERMINAL".equals(bingoSeventyFive.getDeviceType())) {
			int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
			helper.checkLastPrintedTicketStatusAndUpdate(userBean,
					Long.parseLong(bingoSeventyFive.getLastSoldTicketNo()), bingoSeventyFive.getDeviceType(),
					bingoSeventyFive.getRefMerchantId(), autoCancelHoldDays, bingoSeventyFive.getActionName(),
					bingoSeventyFive.getLastGameId(), connection);
		}
	}

	
	// DG Call
	private String prepareBingoSeventyFiveServiceRequestForDGE(BingoSeventyFive bingoSeventyFive) {
		ServiceRequest serviceRequest;
		serviceRequest = new ServiceRequest();
		serviceRequest.setServiceName(ServiceName.BINGOSEVENTYFIVE_MGMT);
		serviceRequest.setServiceMethod(ServiceMethodName.BINGOSEVENTYFIVE_PURCHASE_TICKET);
		serviceRequest.setServiceData(bingoSeventyFive);
		String dgeResponseString = delegate.getResponseString(serviceRequest);
		return dgeResponseString;
	}

	private void setBingoSeventyFiveDGEResponseData(BingoSeventyFive bingoSeventyFive,
			BingoSeventyFiveResponse bingoResponseBean) {
		bingoSeventyFive.setSaleStatus(bingoResponseBean.getSaleStatus());
		bingoSeventyFive.setTicket_no(bingoResponseBean.getTicketNo());
		bingoSeventyFive.setBarcodeCount(bingoResponseBean.getBarcodeCount());
		bingoSeventyFive.setNoOfDraws(bingoResponseBean.getNoOfDraws());
		bingoSeventyFive.setPurchaseTime(bingoResponseBean.getPurchaseTime());
		bingoSeventyFive.setReprintCount(bingoResponseBean.getReprintCount());
		bingoSeventyFive.setPlayerData(bingoResponseBean.getPlayerData());
		bingoSeventyFive.setTotalPurchaseAmt(bingoResponseBean.getTotalPurchaseAmt());
		bingoSeventyFive.setDrawDateTime(bingoResponseBean.getDrawDateTime());
	}

	private void doBingoSeventyFiveSaleAfterSuccesWork(UserInfoBean userBean, BingoSeventyFive bingoSeventyFive,
			Connection connection) throws SQLException {
		ajaxHelper.getAvlblCreditAmt(userBean, connection);
		bingoSeventyFive.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), bingoSeventyFive.getGameId()));
		bingoSeventyFive.setSaleStatus("SUCCESS");
	}

	private void ifErrorOccurInBingoSeventyFiveTicketUpdation(UserInfoBean userBean, BingoSeventyFive bingoSeventyFive)
			throws LMSException {
		bingoSeventyFive.setSaleStatus("FAILED");
		helper.cancelTicket(bingoSeventyFive.getTicket_no() + bingoSeventyFive.getReprintCount(),
				bingoSeventyFive.getPurchaseChannel(), bingoSeventyFive.getDrawIdTableMap(),
				bingoSeventyFive.getGame_no(), bingoSeventyFive.getPartyId(), bingoSeventyFive.getPartyType(),
				bingoSeventyFive.getRefMerchantId(), userBean, bingoSeventyFive.getRefTransId());
	}

	private void failedBingoSeventyFiveTransactionIfFailedAtDGE(UserInfoBean userBean,
			BingoSeventyFive bingoSeventyFive, long transId) {
		if (bingoSeventyFive.getSaleStatus() == null) {
			bingoSeventyFive.setSaleStatus("FAILED");// Error
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, bingoSeventyFive.getGameId(), "FAILED", transId);
		} else if ("ERROR_TICKET_LIMIT".equalsIgnoreCase(bingoSeventyFive.getSaleStatus())) {
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, bingoSeventyFive.getGameId(), "FAILED", transId);
		} else {
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, bingoSeventyFive.getGameId(), "FAILED", transId);
		}
	}

	private void failedBingoSeventyFiveTrasactionIfAnyExceptionOccur(UserInfoBean userBean,
			BingoSeventyFive bingoSeventyFive, long transId) {
		if (bingoSeventyFive.getSaleStatus() == null) {
			bingoSeventyFive.setSaleStatus("FAILED"); // Error
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, bingoSeventyFive.getGameId(), "FAILED", transId);
		} else {
			orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, bingoSeventyFive.getGameId(), "FAILED", transId);
		}
	}

	/* BingoSeventyFive End - Mukesh Sharma */
}
