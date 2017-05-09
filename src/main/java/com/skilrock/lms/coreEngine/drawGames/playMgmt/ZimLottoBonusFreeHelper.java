package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.itg.IDBarcode.IDBarcode;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class ZimLottoBonusFreeHelper {

	static Log logger = LogFactory.getLog(ZimLottoBonusFreeHelper.class);

	public static LottoPurchaseBean zimLottoBonusFreePurchaseTicket(UserInfoBean userBean,
			LottoPurchaseBean lottoPurchaseBean) throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.ZIMLOTTOBONUSFREE_MGMT);
		sReq.setServiceMethod(ServiceMethodName.ZIMLOTTOBONUSFREE_PURCHASE_TICKET);
		sReq.setServiceData(lottoPurchaseBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		String status = "";
		long balDed = 0;
		Connection con = null;

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

		/*
		if(lottoPurchaseBean.getLastSoldTicketNo() != null){
			if(!"0".equalsIgnoreCase(lottoPurchaseBean.getLastSoldTicketNo())){
				Util.insertLastSoldTicketTeminal(lottoPurchaseBean.getUserId(), lottoPurchaseBean.getLastSoldTicketNo(), lottoPurchaseBean.getPurchaseTime());
			}
		}
		*/

		try {
			lottoPurchaseBean.setSaleStatus("FAILED");
			int noOfLines = lottoPurchaseBean.getPicknumbers().length;
			con=DBConnect.getConnection();
            con.setAutoCommit(false);

			if ("Perm6".equals(lottoPurchaseBean.getPlayType())) {
				int n = lottoPurchaseBean.getNoPicked();
				if (n < 7) {
					lottoPurchaseBean.setSaleStatus("FAILED");
					return lottoPurchaseBean;
				}
				noOfLines = (n * (n - 1) * (n - 2) * (n - 3) * (n - 4) * (n - 5)) / 720;
			}
			lottoPurchaseBean.setNoOfLines(noOfLines);
			double unitPrice = Util.getUnitPrice(
					lottoPurchaseBean.getGameId(), lottoPurchaseBean
							.getPlayType());
			lottoPurchaseBean.setUnitPrice(unitPrice);
			
			double totPurAmt=0.0;
			if(lottoPurchaseBean.isPromotkt()){
				lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);
			}else{	
			
			 totPurAmt = noOfLines * unitPrice
					* lottoPurchaseBean.getNoOfDraws();
			totPurAmt = CommonMethods.fmtToTwoDecimal(totPurAmt);
			}

			logger.debug("Total Purchase Amount - " + totPurAmt);

			lottoPurchaseBean.setTotalPurchaseAmt(totPurAmt);

			logger.debug("Inside  FE1*******");
			// rg calling
			boolean isFraud = ResponsibleGaming.respGaming(userBean,
					"DG_SALE", totPurAmt + "",con);
			if (!isFraud) {
				balDed = orgOnLineSaleCreditUpdation
						.drawTcketSaleBalDeduction(userBean,
								lottoPurchaseBean.getGameId(),
								lottoPurchaseBean.getTotalPurchaseAmt(),lottoPurchaseBean.getPlrMobileNumber(), con);
				oldTotalPurchaseAmt = lottoPurchaseBean
						.getTotalPurchaseAmt();

				logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :" + oldTotalPurchaseAmt);

				if (balDed > 0) {
					lottoPurchaseBean.setRefTransId(balDed + "");
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
					lottoPurchaseBean.setSaleStatus(status);
					return lottoPurchaseBean;
				}
			} else {
				System.out.println("Responsing Gaming not allowed to sale");
				lottoPurchaseBean.setSaleStatus("FRAUD");
				return lottoPurchaseBean;
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}finally{
			DBConnect.closeCon(con);
		}

		try {
			lottoPurchaseBean.setRefTransId(balDed + "");
			sRes = delegate.getResponse(sReq);
			
			String responseString = sRes.getResponseData().toString();
			Type elementType = new TypeToken<LottoPurchaseBean>(){}.getType();

			if (sRes.getIsSuccess()) {
				lottoPurchaseBean = new Gson().fromJson(responseString, elementType);
				modifiedTotalPurchaseAmt = lottoPurchaseBean
						.getTotalPurchaseAmt();

				// rouding

				modifiedTotalPurchaseAmt = CommonMethods
						.roundDrawTktAmt(modifiedTotalPurchaseAmt);

				logger
						.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
								+ modifiedTotalPurchaseAmt);
				con=DBConnect.getConnection();
                con.setAutoCommit(false);
				if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
					lottoPurchaseBean
							.setTotalPurchaseAmt(modifiedTotalPurchaseAmt);
					balDed = orgOnLineSaleCreditUpdation
							.drawTcketSaleBalDedUpdate(userBean,
									lottoPurchaseBean.getGameId(),
									modifiedTotalPurchaseAmt,
									oldTotalPurchaseAmt, balDed, con);

				}
				int tickUpd = orgOnLineSaleCreditUpdation
						.drawTicketSaleTicketUpdate(balDed,
								lottoPurchaseBean.getTicket_no(),
								lottoPurchaseBean.getGameId(),
								userBean,lottoPurchaseBean.getPurchaseChannel(), con);

				if (tickUpd == 1) {
					AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
					ajxHelper.getAvlblCreditAmt(userBean,con);
					status = "SUCCESS";
					lottoPurchaseBean.setSaleStatus(status);
					lottoPurchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), lottoPurchaseBean.getGameId()));
					
					if (!"applet".equals(lottoPurchaseBean.getBarcodeType())) {
						IDBarcode
								.getBarcode(lottoPurchaseBean
										.getTicket_no()
										+ lottoPurchaseBean
												.getReprintCount());
					}
					 con.commit();
					// call promo purchase process here
					List<RafflePurchaseBean> rafflePurchaseBeanList = new ArrayList<RafflePurchaseBean>();
					//CommonFunctionsHelper commonHelper = new CommonFunctionsHelper();

					/*
					String gameName = Util
							.getGameName(lottoPurchaseBean
									.getGame_no());
					List<PromoGameBean> promoGameslist = DGPromoScheme
							.getAvailablePromoGamesNew(gameName,
									totPurAmt, null);
					*/

					List<PromoGameBean> promoGameslist = Util.promoGameBeanMap.get(lottoPurchaseBean.getGameId());
					// String gameType =
					// getGameType(fortunePurchaseBean.getGame_no());
					// List<PromoGameBean> promoGameslist =
					// commonHelper.getAvailablePromoGames(gameType);
					boolean isLoopAgain=true;
					for (int i = 0; i < promoGameslist.size(); i++) {
						PromoGameBean promobean = promoGameslist
								.get(i);
						List<LottoPurchaseBean> lottoBeanList=new ArrayList<LottoPurchaseBean>();
						for(int k=0;k<promobean.getNoOfPromoTickets();k++){
						if (promobean.getPromoGametype()
								.equalsIgnoreCase("RAFFLE")) {
							lottoPurchaseBean.setRaffleNo(promobean
									.getPromoGameNo());
							RafflePurchaseBean rafflePurchaseBean = (RafflePurchaseBean) helper.rafflePurchaseTicket(
									userBean, lottoPurchaseBean,
									true);
							rafflePurchaseBean
									.setRaffleTicketType(promobean
											.getPromoTicketType());
							if (rafflePurchaseBean.getSaleStatus()
									.equalsIgnoreCase("FAILED")) {
								status = "FAILED";
								lottoPurchaseBean
										.setSaleStatus(status);
								// Code for cancelling the Ticket to
								// be send to Draw Game Engine
								helper.cancelTicket(
										lottoPurchaseBean
												.getTicket_no()
												+ lottoPurchaseBean
														.getReprintCount(),
										lottoPurchaseBean
												.getPurchaseChannel(),
										lottoPurchaseBean
												.getDrawIdTableMap(),
										lottoPurchaseBean
												.getGame_no(),
										lottoPurchaseBean
												.getPartyId(),
										lottoPurchaseBean
												.getPartyType(),
										lottoPurchaseBean
												.getRefMerchantId(),
										userBean, lottoPurchaseBean
												.getRefTransId());
								lottoPurchaseBean
										.setSaleStatus("FAILED");
								return lottoPurchaseBean;
							} else if (rafflePurchaseBean
									.getSaleStatus()
									.equalsIgnoreCase("SUCCESS")) {
								// here insert entry into the promo
								// ticket mapping table
								int tktlen = rafflePurchaseBean
										.getRaffleTicket_no()
										.length();
								orgOnLineSaleCreditUpdation
										.drawTicketNPromoMappigUpdate(
												1,
												rafflePurchaseBean
														.getGame_no(),
												rafflePurchaseBean
														.getParentTktNo(),
												rafflePurchaseBean
														.getRaffleTicket_no()
														.substring(
																0,
																tktlen - 2));
								rafflePurchaseBeanList
										.add(rafflePurchaseBean);
							}
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
					 * PromoGameBean
					 * promobean=promoGameslist.get(i);
					 * if(promobean.getPromoGametype().equalsIgnoreCase("RAFFLE")){
					 * lottoPurchaseBean.setRaffleNo(promobean.getPromoGameNo());
					 * RafflePurchaseBean rafflePurchaseBean =
					 * (RafflePurchaseBean) rafflePurchaseTicket(
					 * userBean, lottoPurchaseBean,true);
					 * rafflePurchaseBean.setRaffleTicketType(promobean.getPromoTicketType());
					 * if (rafflePurchaseBean.getSaleStatus()
					 * .equalsIgnoreCase("FAILED")) { status =
					 * "FAILED"; lottoPurchaseBean
					 * .setSaleStatus(status); // Code for
					 * cancelling the Ticket to be send to Draw Game
					 * Engine
					 * cancelTicket(lottoPurchaseBean.getTicket_no(),
					 * lottoPurchaseBean.getPurchaseChannel(),
					 * lottoPurchaseBean.getDrawIdTableMap(),
					 * lottoPurchaseBean.getGame_no(),
					 * lottoPurchaseBean.getPartyId(),
					 * lottoPurchaseBean.getPartyType(),
					 * lottoPurchaseBean.getRefMerchantId(),
					 * userBean, lottoPurchaseBean
					 * .getRefTransId());
					 * lottoPurchaseBean.setSaleStatus("FAILED");
					 * return lottoPurchaseBean; }else if
					 * (rafflePurchaseBean.getSaleStatus().equalsIgnoreCase("SUCCESS")){
					 * //here insert entry into the promo ticket
					 * mapping table int
					 * tktlen=rafflePurchaseBean.getRaffleTicket_no().length();
					 * orgOnLineSaleCreditUpdation.drawTicketNPromoMappigUpdate(1,
					 * rafflePurchaseBean.getGame_no(),
					 * rafflePurchaseBean.getParentTktNo(),
					 * (rafflePurchaseBean.getRaffleTicket_no()).substring(0,
					 * tktlen - 2));
					 * rafflePurchaseBeanList.add(rafflePurchaseBean); } } }
					 */

					lottoPurchaseBean
							.setRafflePurchaseBeanList(rafflePurchaseBeanList);

					return lottoPurchaseBean;
				} else {
					status = "FAILED";
					lottoPurchaseBean.setSaleStatus(status);
					// Code for cancelling the Ticket to be send to
					// Draw
					// Game Engine
					helper.cancelTicket(lottoPurchaseBean.getTicket_no(),
							lottoPurchaseBean.getPurchaseChannel(),
							lottoPurchaseBean.getDrawIdTableMap(),
							lottoPurchaseBean.getGame_no(),
							lottoPurchaseBean.getPartyId(),
							lottoPurchaseBean.getPartyType(),
							lottoPurchaseBean.getRefMerchantId(),
							userBean, lottoPurchaseBean
									.getRefTransId());

					return lottoPurchaseBean;
				}
			} else {
				
				lottoPurchaseBean = new Gson().fromJson(responseString, elementType);
				if(lottoPurchaseBean.getSaleStatus() == null){
					lottoPurchaseBean.setSaleStatus("FAILED");// Error
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
							userBean, lottoPurchaseBean.getGameId(),
							"FAILED", balDed);
					return lottoPurchaseBean;
				}else if("ERROR_TICKET_LIMIT".equalsIgnoreCase(lottoPurchaseBean.getSaleStatus())){
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
							userBean, lottoPurchaseBean.getGameId(),
							"FAILED", balDed);
					return lottoPurchaseBean;
				}else{
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
							userBean, lottoPurchaseBean.getGameId(),
							"FAILED", balDed);
					return lottoPurchaseBean;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commented by amit as not relevant here

			/*
			 * lottoPurchaseBean = (LottoPurchaseBean)
			 * sRes.getResponseData(); String ticketNo =
			 * lottoPurchaseBean.getTicket_no(); String cancelChannel =
			 * lottoPurchaseBean.getPurchaseChannel(); Map<Integer, Map<Integer,
			 * String>> drawIdTableMap = lottoPurchaseBean
			 * .getDrawIdTableMap(); int gameNo =
			 * lottoPurchaseBean.getGame_no(); int partyId =
			 * lottoPurchaseBean.getPartyId(); String partyType =
			 * lottoPurchaseBean.getPartyType(); String refTransId =
			 * lottoPurchaseBean.getRefTransId();
			 * 
			 * cancelTicket(ticketNo, cancelChannel, drawIdTableMap, gameNo,
			 * partyId, partyType, refTransId);
			 */
		}
		finally {
			DBConnect.closeCon(con);
		}
		/*
		} else {
			logger.debug("Data validation returns false");
			lottoPurchaseBean.setSaleStatus("FAILED");
		}
		*/

		return lottoPurchaseBean;
	}
}