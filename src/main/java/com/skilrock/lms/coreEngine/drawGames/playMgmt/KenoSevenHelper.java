package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.itg.IDBarcode.IDBarcode;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoRequestBean;
import com.skilrock.lms.dge.beans.KenoResponseBean;
import com.skilrock.lms.dge.gameconstants.KenoSevenConstants;
import com.skilrock.lms.web.drawGames.common.Util;

public class KenoSevenHelper {
	Log logger =  LogFactory.getLog(KenoSevenHelper.class);

	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}

	public KenoPurchaseBean commonPurchseProcess(UserInfoBean userBean, KenoPurchaseBean kenoPurchaseBean) throws LMSException, SQLException {
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		kenoPurchaseBean.setPromotkt(false);
		kenoPurchaseBean = kenoPurchaseTicket(userBean, kenoPurchaseBean);
		String saleStatus = kenoPurchaseBean.getSaleStatus();
		if ("SUCCESS".equalsIgnoreCase(saleStatus)) {
			kenoPurchaseBean = helper.commonPromoPurchaseProcess(kenoPurchaseBean, userBean);
			if ("SUCCESS".equalsIgnoreCase(kenoPurchaseBean.getPromoSaleStatus())) {
				return kenoPurchaseBean;
			} else {
				helper.cancelTicket(kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount(), kenoPurchaseBean.getPurchaseChannel(), kenoPurchaseBean.getDrawIdTableMap(), kenoPurchaseBean.getGame_no(), kenoPurchaseBean.getPartyId(), kenoPurchaseBean.getPartyType(), kenoPurchaseBean.getRefMerchantId(), userBean, kenoPurchaseBean.getRefTransId());
			}
		}

		return kenoPurchaseBean;
	}

	public KenoPurchaseBean kenoPurchaseTicket(UserInfoBean userBean, KenoPurchaseBean kenoPurchaseBean) throws LMSException {
		kenoPurchaseBean.setSaleStatus("FAILED");
		Connection connection = null;
        long balDed = 0;
        String status = "";
        double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;

		try {
			if (isDrawAvailable(kenoPurchaseBean.getGameId()) || DrawGameRPOSHelper.chkFreezeTimeSale(kenoPurchaseBean.getGameId())) {
				kenoPurchaseBean.setSaleStatus("NO_DRAWS");
				return kenoPurchaseBean;
			}

			if(!kenoValidateData(kenoPurchaseBean)) {
				logger.info("Data Validation returned false");
				return kenoPurchaseBean;
			}

			double totPurAmt = 0.0;
			int noOfPanel = kenoPurchaseBean.getNoOfPanel();
			String playTypeArr[] = kenoPurchaseBean.getPlayType();
			String[] noPickStr = kenoPurchaseBean.getNoPicked();
			int noOfLines[] = new int[noOfPanel];
			int betAmtMulArr[] = kenoPurchaseBean.getBetAmountMultiple();
			double unitPriceArr[] = new double[noOfPanel];
			for (int i=0; i<noOfPanel; i++) {
				String playType = playTypeArr[i];
				String[] noPick = noPickStr[i].split(",");
				int[] n = new int[noPick.length];
				for (int j = 0; j < noPick.length; j++) {
					n[j] = Integer.parseInt(noPick[j]);
				}

				if (playType.contains("Direct"))
					noOfLines[i] = 1;

				unitPriceArr[i] = Util.getUnitPrice(kenoPurchaseBean.getGameId(), playTypeArr[i]);
				totPurAmt += noOfLines[i] * unitPriceArr[i] * kenoPurchaseBean.getNoOfDraws() * betAmtMulArr[i];
			}
			kenoPurchaseBean.setUnitPrice(unitPriceArr);
			kenoPurchaseBean.setNoOfLines(noOfLines);

			if (totPurAmt <= 0) {
				kenoPurchaseBean.setSaleStatus("FAILED");
				return kenoPurchaseBean;
			}
			kenoPurchaseBean.setTotalPurchaseAmt(totPurAmt);

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS")); 
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			helper.checkLastPrintedTicketStatusAndUpdate(userBean,Long.parseLong(kenoPurchaseBean.getLastSoldTicketNo()),"TERMINAL", kenoPurchaseBean.getRefMerchantId(), autoCancelHoldDays,kenoPurchaseBean.getActionName(), kenoPurchaseBean.getLastGameId(), connection);

			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", String.valueOf(totPurAmt), connection);
			if (!isFraud) {
				balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, kenoPurchaseBean.getGameId(), kenoPurchaseBean.getTotalPurchaseAmt(), kenoPurchaseBean.getPlrMobileNumber(), connection);
				oldTotalPurchaseAmt = kenoPurchaseBean.getTotalPurchaseAmt();
				if (balDed > 0) {
					kenoPurchaseBean.setRefTransId(balDed + "");
					connection.commit();
				} else {
					if (balDed == -1)
						status = "AGT_INS_BAL";
					else if (balDed == -2)
						status = "FAILED";
					else if (balDed == 0)
						status = "RET_INS_BAL";

					kenoPurchaseBean.setSaleStatus(status);
					return kenoPurchaseBean;
				}
			} else {
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
			DBConnect.closeCon(connection);
		}

		KenoRequestBean kenoRequestBean = new KenoRequestBean();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.KENOSEVEN_MGMT);
		sReq.setServiceMethod(ServiceMethodName.KENOSEVEN_PURCHASE_TICKET);
		sReq.setServiceData(kenoRequestBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

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

			String responseString = delegate.getResponseString(sReq);
			KenoResponseBean kenoResponseBean = new Gson().fromJson(responseString, KenoResponseBean.class);
			if (kenoResponseBean.getIsSuccess()) {
				kenoPurchaseBean.setSaleStatus(kenoResponseBean.getSaleStatus());
				kenoPurchaseBean.setTicket_no(kenoResponseBean.getTicketNo());
				kenoPurchaseBean.setBarcodeCount(kenoResponseBean.getBarcodeCount());
				kenoPurchaseBean.setNoOfDraws(kenoResponseBean.getNoOfDraws());
				kenoPurchaseBean.setPurchaseTime(kenoResponseBean.getPurchaseTime());
				kenoPurchaseBean.setReprintCount(kenoResponseBean.getReprintCount());
				kenoPurchaseBean.setPlayerData(kenoResponseBean.getPlayerData());
				kenoPurchaseBean.setTotalPurchaseAmt(kenoResponseBean.getTotalPurchaseAmt());
				kenoPurchaseBean.setDrawDateTime(kenoResponseBean.getDrawDateTime());
				modifiedTotalPurchaseAmt = kenoPurchaseBean.getTotalPurchaseAmt();

				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);
				if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean, kenoPurchaseBean.getGameId(), modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed, connection);
				}

				int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed, kenoPurchaseBean.getTicket_no(), kenoPurchaseBean.getGameId(), userBean,kenoPurchaseBean.getPurchaseChannel(), connection);

				if (tickUpd == 1) {
					AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
					ajxHelper.getAvlblCreditAmt(userBean,connection);
					kenoPurchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), kenoPurchaseBean.getGameId()));
					status = "SUCCESS";
					kenoPurchaseBean.setSaleStatus(status);							
					if (!"applet".equals(kenoPurchaseBean.getBarcodeType())) {
						IDBarcode.getBarcode(kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount());
					}
					connection.commit();

					return kenoPurchaseBean;
				} else {
					status = "FAILED";
					kenoPurchaseBean.setSaleStatus(status);
					new DrawGameRPOSHelper().cancelTicket(kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount(), kenoPurchaseBean.getPurchaseChannel(), kenoPurchaseBean.getDrawIdTableMap(), kenoPurchaseBean.getGame_no(), kenoPurchaseBean.getPartyId(), kenoPurchaseBean.getPartyType(), kenoPurchaseBean.getRefMerchantId(), userBean, kenoPurchaseBean.getRefTransId());
					return kenoPurchaseBean;
				}
			} else {
				kenoPurchaseBean.setSaleStatus(kenoResponseBean.getSaleStatus());
				if(kenoPurchaseBean.getSaleStatus() == null) {
					kenoPurchaseBean.setSaleStatus("FAILED");
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED", balDed);
					return kenoPurchaseBean;
				} else if("ERROR_TICKET_LIMIT".equalsIgnoreCase(kenoPurchaseBean.getSaleStatus())) {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED", balDed);
					return kenoPurchaseBean;
				} else {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(), "FAILED", balDed);
					return kenoPurchaseBean;
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return kenoPurchaseBean;
	}

	public boolean kenoValidateData(KenoPurchaseBean kenoPurchaseBean) {
		if(kenoPurchaseBean != null) {
			if(kenoPurchaseBean.getNoOfDraws()<1) {
				logger.info("Insufficient No of Draws");
				return false;
			}
			if(kenoPurchaseBean.getNoOfPanel()<1) {
				logger.info("Insufficient No of Panels");
				return false;
			}

			int noOfPanel = kenoPurchaseBean.getNoOfPanel();
			String playTypeArr[] = kenoPurchaseBean.getPlayType();
			int[] qp = kenoPurchaseBean.getIsQuickPick();
			boolean isValid = true;
			String[] pickedNumbersArr = kenoPurchaseBean.getPlayerData();
			String[] noPickedArr = kenoPurchaseBean.getNoPicked();
			for (int i=0; i<noOfPanel; i++) {
				String playerData = pickedNumbersArr[i];
				if (!"QP".equals(playerData)) {
					if (playTypeArr[i].contains("Direct")) {
						int pickValue = KenoSevenConstants.BET_TYPE_MAP.get(playTypeArr[i]);
						if(qp[i]!=2 || playerData.split(",").length != pickValue || Integer.parseInt(noPickedArr[i])!= pickValue) {
							isValid = false;
							break;
						}
					}

					isValid = Util.validateNumber(KenoSevenConstants.START_RANGE, KenoSevenConstants.END_RANGE, playerData.replace(",UL,", ",").replace(",BL", ""), false);
					if (!isValid)
						break;
				} else {
					if (playTypeArr[i].contains("Direct")) {
						int pickValue = KenoSevenConstants.BET_TYPE_MAP.get(playTypeArr[i]);
						if(qp[i]!=1 || Integer.parseInt(noPickedArr[i])!=pickValue) {
							isValid = false;
							break;
						}
					}
				}
			}

			if (!isValid) {
				kenoPurchaseBean.setSaleStatus("INVALID_INPUT");
				return false;
			}
		} else {
			logger.info("Keno Seven Bean is Null");
			return false;
		}

		return true;
	}
}