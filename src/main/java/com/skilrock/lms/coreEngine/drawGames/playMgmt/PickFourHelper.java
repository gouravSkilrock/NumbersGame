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
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoRequestBean;
import com.skilrock.lms.dge.beans.KenoResponseBean;
import com.skilrock.lms.dge.gameconstants.PickFourConstants;
import com.skilrock.lms.web.drawGames.common.Util;

public class PickFourHelper {

	Log logger = LogFactory.getLog(PickFourHelper.class);

	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}

	public KenoPurchaseBean pickFourPurchaseTicket(UserInfoBean userBean, KenoPurchaseBean purchaseBean) throws LMSException {
		purchaseBean.setSaleStatus("FAILED");
		Connection con = null;
		long balDed = 0;
		String status = "";
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PICKFOUR_MGMT);
		sReq.setServiceMethod(ServiceMethodName.PICKFOUR_PURCHASE_TICKET);
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

			double totPurAmt = 0.0;
			int noOfPanel = purchaseBean.getNoOfPanel();
			String playTypeArr[] = purchaseBean.getPlayType();
			int noOfLines[] = new int[noOfPanel];
			int betAmtMulArr[] = purchaseBean.getBetAmountMultiple();
			double unitPriceArr[] = new double[noOfPanel];
			for (int i = 0; i < noOfPanel; i++) {
				String playType = playTypeArr[i];
				String[] pickedNumbersArr = purchaseBean.getPlayerData();
				
				if (playType.equalsIgnoreCase("Straight") || playType.equalsIgnoreCase("Box")) 
					noOfLines[i] = 1;
				else if (playType.equalsIgnoreCase("Combination")){ 
					if (!"QP".equals(pickedNumbersArr[i])) {
						String[] numbers = new String[4];
						numbers[0]=pickedNumbersArr[0].split(",")[0];
						numbers[1]=pickedNumbersArr[0].split(",")[1];
						numbers[2]=pickedNumbersArr[0].split(",")[2];
						numbers[3]=pickedNumbersArr[0].split(",")[3];
						
						boolean val = false;
						int ctr = 0, flag = 0;
						int[][] array = new int[4][2];
						for (int t = 0; t < 4; t++) {
							array[t][0] = 99;
							array[t][1] = 99;
						}
	
						for (int q = 0; q < 4; q++) {
							ctr = 0;
							for (int w = q; w < 4; w++) {
								if (Integer.parseInt(numbers[q]) == Integer.parseInt(numbers[w]))
									ctr++;
							}
							val = false;
							for (int e = 0; e < array.length; e++) {
								if (array[e][0] == Integer.parseInt(numbers[q])) {
									val = true;
									break;
								}
							}
							if (val == false) {
								array[flag][0] = Integer.parseInt(numbers[q]);
								array[flag][1] = ctr;
								flag++;
							}
						}
			
						if (array[0][0] != 99 && array[1][0] != 99 && array[2][0] != 99 && array[3][0] != 99)
							noOfLines[i] = 24;
						else if (array[0][0] != 99 && array[1][0] != 99 && array[2][0] != 99 && array[3][0] == 99)
							noOfLines[i] = 12;
						else if (array[0][0] != 99 && array[1][0] != 99
								& array[2][0] == 99 & array[3][0] == 99
								&& array[0][1] == 1 && array[1][1] == 1)
							noOfLines[i] = 6;
						else if (array[0][0] != 99 && array[1][0] != 99
								& array[2][0] == 99 & array[3][0] == 99
								&& array[0][1] != 1 && array[1][1] != 1)
							noOfLines[i] = 4;
					}else
						noOfLines[i] = 24;
					

				}else if (playType.equalsIgnoreCase("StraightBox")) 
					noOfLines[i] = 1;
				
				unitPriceArr[i] = Util.getUnitPrice(purchaseBean.getGameId(), playTypeArr[i]);
				totPurAmt += noOfLines[i] * unitPriceArr[i] * purchaseBean.getNoOfDraws() * betAmtMulArr[i];
			}
			purchaseBean.setUnitPrice(unitPriceArr);
			purchaseBean.setNoOfLines(noOfLines);

			if (totPurAmt <= 0) {
				purchaseBean.setSaleStatus("FAILED");
				return purchaseBean;
			}
			if(purchaseBean.isPromotkt())
				purchaseBean.setTotalPurchaseAmt(0);
			else
				purchaseBean.setTotalPurchaseAmt(totPurAmt);

			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			if("TERMINAL".equals(purchaseBean.getDeviceType())) {
				int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
				DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
				helper.checkLastPrintedTicketStatusAndUpdate(userBean, Long.parseLong(purchaseBean.getLastSoldTicketNo()), purchaseBean.getDeviceType(), purchaseBean.getRefMerchantId(), autoCancelHoldDays, purchaseBean.getActionName(), purchaseBean.getLastGameId(), con);
			}

			/*int autoCancelHoldDays = Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"));
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			
			helper.checkLastPrintedTicketStatusAndUpdate(userBean, Long.parseLong(purchaseBean.getLastSoldTicketNo()), purchaseBean.getDeviceType(), purchaseBean.getRefMerchantId(),
					autoCancelHoldDays, purchaseBean.getActionName(), purchaseBean.getLastGameId(), con);

			logger.debug("SALE_AUTO_CANCEL_LOGS:SALE Continue for the new ticket");*/

			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "", con);
			//boolean isFraud = false;
			if (!isFraud) {
				balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, purchaseBean.getGameId(), purchaseBean.getTotalPurchaseAmt(), purchaseBean.getPlrMobileNumber(), con);
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
					}else if (balDed == -5) {
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
				purchaseBean.setTotalPurchaseAmt(responseBean.getTotalPurchaseAmt());
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
					purchaseBean.setSaleStatus("FAILED");// Error
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

	public boolean pickFourValidateData(KenoPurchaseBean purchaseBean) {
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

			boolean isValid = true;
			String[] pickedNumbersArr = purchaseBean.getPlayerData();
			for (int i = 0; i < noOfPanel; i++) {
				String playerData = pickedNumbersArr[i];
				if (!"QP".equals(playerData)) {
					isValid = Util.validateNumber(PickFourConstants.START_RANGE, PickFourConstants.END_RANGE, playerData, PickFourConstants.IS_DUPLICATE);
					if (!isValid) 
						break;
					
					String[] numbers = new String[4];
					numbers[0]=pickedNumbersArr[0].split(",")[0];
					numbers[1]=pickedNumbersArr[0].split(",")[1];
					numbers[2]=pickedNumbersArr[0].split(",")[2];
					numbers[3]=pickedNumbersArr[0].split(",")[3];

					if("Combination".equalsIgnoreCase(purchaseBean.getPlayType()[i]) || "Box".equalsIgnoreCase(purchaseBean.getPlayType()[i]) ||
							"StraightBox".equalsIgnoreCase(purchaseBean.getPlayType()[i])){
						if(numbers[0]==numbers[1] && numbers[1]==numbers[2] && numbers[2]==numbers[3]){
							isValid=false;
							break;
						}
					}
				} 
			}

			if (!isValid) {
				purchaseBean.setSaleStatus("INVALID_INPUT");// Error Draw
				// setKenoPurchaseBean(kenoPurchaseBean);
				logger.info("-----------Pick Four Validation Error-------------------" + purchaseBean.getSaleStatus());
				return false;
			}
		} else {
			logger.debug("Pick Four Data Error : Null Bean Recieved ");
			return false;
		}
		return true;
	}

}
