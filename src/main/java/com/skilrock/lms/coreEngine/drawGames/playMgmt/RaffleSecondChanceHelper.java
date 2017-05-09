package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.itg.IDBarcode.IDBarcode;
import com.skilrock.lms.beans.RafflePurchaseBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.serviceImpl.ScratchTicketVerifyServiceImpl;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.web.drawGames.common.Util;

public class RaffleSecondChanceHelper {
	Log logger =  LogFactory.getLog(RaffleSecondChanceHelper.class);
	
	public String getSecondChanceGameName(String serviceName) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;

		try {
			Calendar calendar = Calendar.getInstance();
			String currentTime = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);

			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			query = "SELECT game_name FROM st_lms_second_chance_service_mapping sm INNER JOIN st_lms_second_chance_day_mapping dm ON sm.sc_service_id=dm.sc_service_id AND sm.service_id=(SELECT service_id FROM st_lms_service_master WHERE service_code='"+serviceName+"') AND sm.status='ACTIVE' AND dm.status='ACTIVE' AND DAY=UPPER(DAYNAME('"+Util.getCurrentTimeString()+"')) AND start_time <= '"+currentTime+"' AND '"+currentTime+"'<=end_time;";
			logger.info("validateRaffleService - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				return rs.getString("game_name");
			} else {
				return null;
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
	}

	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}

	public RafflePurchaseBean rafflePurchaseTicket(UserInfoBean userBean, RafflePurchaseBean rafflePurchaseBean) throws LMSException {
		rafflePurchaseBean.setSaleStatus("FAILED");
		Connection con=null;
        long balDed=0;
        String status = "";
        double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;

		ServiceRequest sReq = new ServiceRequest();
		ServiceResponse sRes = null;
		IServiceDelegate delegate = null;
		try {
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			/*
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "SC_MAX_ATTEMP", "0", con);
			if(isFraud) {
				logger.debug("Responsing Gaming not allowed to sale");
				rafflePurchaseBean.setSaleStatus("FRAUD");
				return rafflePurchaseBean;
			}
			*/

			if("DG".equals(rafflePurchaseBean.getServiceName())) {
				sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.DRAW_MGMT);
				sReq.setServiceMethod(ServiceMethodName.VERIFY_DRAW_GAME_TICKET);
				sReq.setServiceData(rafflePurchaseBean.getParentTktNo());
				delegate = ServiceDelegate.getInstance();
				sRes = delegate.getResponse(sReq);
				boolean responseStatus = new Gson().fromJson(sRes.getIsSuccess().toString(), Boolean.class);
				String responseData = new Gson().fromJson(sRes.getResponseData().toString(), String.class);
				if (!responseStatus) {
					//ResponsibleGaming.respGaming(userBean, "SC_MAX_ATTEMP", "1", con);
					rafflePurchaseBean.setSaleStatus(responseData);
					con.commit();
					return rafflePurchaseBean;
				} else {
					rafflePurchaseBean.setParentTktNo(responseData);
				}
			} else if("SE".equals(rafflePurchaseBean.getServiceName())) {
				ScratchTicketVerifyServiceImpl serviceImpl = new ScratchTicketVerifyServiceImpl();
				//boolean isValid = serviceImpl.verifyScratchTicket(rafflePurchaseBean.getParentTktNo(), rafflePurchaseBean.getVirnCode());
				serviceImpl.verifyScratchTicket(rafflePurchaseBean.getParentTktNo(), rafflePurchaseBean.getVirnCode());
				/*
				if(!isValid) {
					//ResponsibleGaming.respGaming(userBean, "SC_MAX_ATTEMP", "1", con);
					rafflePurchaseBean.setSaleStatus("INVALID_TKT");
					con.commit();
					return rafflePurchaseBean;
				}
				*/
			}
	
			if("DG".equals(rafflePurchaseBean.getServiceName())) {
				sReq.setServiceName(ServiceName.RAFFLE_SECOND_CHANCE_MGMT);
			} else if("SE".equals(rafflePurchaseBean.getServiceName())) {
				sReq.setServiceName(ServiceName.RAFFLE_SECOND_CHANCE_MGMT);
				String seTicketNumber = (rafflePurchaseBean.getParentTktNo()+rafflePurchaseBean.getVirnCode()).replace("-", "");
				rafflePurchaseBean.setParentTktNo(seTicketNumber);
			}
			sReq.setServiceMethod(ServiceMethodName.RAFFLE_PURCHASE_TICKET);
	
			sReq.setServiceData(rafflePurchaseBean);
			delegate = ServiceDelegate.getInstance();

			if (isDrawAvailable(rafflePurchaseBean.getGameId()) || DrawGameRPOSHelper.chkFreezeTimeSale(rafflePurchaseBean.getGameId())) {
				rafflePurchaseBean.setSaleStatus("NO_DRAWS");
				return rafflePurchaseBean;
			}

			double totPurAmt = 0.0;
			rafflePurchaseBean.setTotalPurchaseAmt(totPurAmt);
			logger.debug("Total Purchase Amount:"	+ totPurAmt);

			
			// Check for Raffle
			//boolean isFraud = false;	//	ResponsibleGaming.respGaming(userBean, "SC_MAX_ATTEMP", "1", con);

			 balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, rafflePurchaseBean.getGameId(), rafflePurchaseBean
						.getTotalPurchaseAmt(),rafflePurchaseBean.getPlrMobileNumber(),con);
			oldTotalPurchaseAmt = rafflePurchaseBean.getTotalPurchaseAmt();
			logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :" + oldTotalPurchaseAmt);
			if (balDed > 0) {
				rafflePurchaseBean.setRefTransId(String.valueOf(balDed));
				con.commit();
			} else {
				if (balDed == -1) {
					status = "AGT_INS_BAL";// Agent has insufficient
				} else if (balDed == -2) {
					status = "FAILED";// Error LMS
				} else if (balDed == 0) {
					status = "RET_INS_BAL";// Retailer has insufficient
				}
				rafflePurchaseBean.setSaleStatus(status);
				return rafflePurchaseBean;
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(con);
		}

		try {
			sRes = delegate.getResponse(sReq);
			boolean responseStatus = new Gson().fromJson(sRes.getIsSuccess().toString(), Boolean.class);
			if (responseStatus) {
				//rafflePurchaseBean = (RafflePurchaseBean) sRes.getResponseData();
				Type elementType = new TypeToken<RafflePurchaseBean>(){}.getType();
				rafflePurchaseBean = new Gson().fromJson(sRes.getResponseData().toString(), elementType);
				modifiedTotalPurchaseAmt = rafflePurchaseBean
						.getTotalPurchaseAmt();
				logger
						.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"
								+ modifiedTotalPurchaseAmt);
				con=DBConnect.getConnection();
				con.setAutoCommit(false);
				/*
				if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
					balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean, rafflePurchaseBean.getGameId(),
									modifiedTotalPurchaseAmt, oldTotalPurchaseAmt, balDed, con);
				}
				*/

				int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed, rafflePurchaseBean.getRaffleTicket_no(),
						rafflePurchaseBean.getGameId(), userBean,rafflePurchaseBean.getPurchaseChannel(), con);

				if (tickUpd == 1) {
					//AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
					//ajxHelper.getAvlblCreditAmt(userBean,con);
					rafflePurchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), rafflePurchaseBean.getGameId()));

					status = "SUCCESS";
					rafflePurchaseBean.setSaleStatus(status);							
					if (!"applet".equals(rafflePurchaseBean.getBarcodeType())) {
						IDBarcode.getBarcode(rafflePurchaseBean.getRaffleTicket_no() + rafflePurchaseBean.getReprintCount());
					}
					con.commit();
					
					return rafflePurchaseBean;
				} else {
					status = "FAILED";
					rafflePurchaseBean.setSaleStatus(status);
					// Code for cancelling the Ticket to be send to Draw
					// Game Engine
					new DrawGameRPOSHelper().cancelTicket(rafflePurchaseBean.getRaffleTicket_no()
							+ rafflePurchaseBean.getReprintCount(),
							rafflePurchaseBean.getPurchaseChannel(),
							rafflePurchaseBean.getDrawIdTableMap(),
							rafflePurchaseBean.getGame_no(),
							rafflePurchaseBean.getPartyId(),
							rafflePurchaseBean.getPartyType(),
							rafflePurchaseBean.getRefMerchantId(),
							userBean, rafflePurchaseBean.getRefTransId());

					return rafflePurchaseBean;
				}
			} else {
				if(rafflePurchaseBean.getSaleStatus() == null) {
					rafflePurchaseBean.setSaleStatus("FAILED");
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
							userBean, rafflePurchaseBean.getGame_no(),
							"FAILED", balDed);
					return rafflePurchaseBean;
				} else if("ERROR_TICKET_LIMIT".equalsIgnoreCase(rafflePurchaseBean.getSaleStatus())) {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
							userBean, rafflePurchaseBean.getGame_no(),
							"FAILED", balDed);
					return rafflePurchaseBean;
				} else {
					orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
							userBean, rafflePurchaseBean.getGame_no(),
							"FAILED", balDed);
					return rafflePurchaseBean;
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return rafflePurchaseBean;
	}
}