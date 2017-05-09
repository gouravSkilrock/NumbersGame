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
import com.skilrock.lms.dge.gameconstants.KenoFiveConstants;
import com.skilrock.lms.web.drawGames.common.Util;

public class KenoFiveHelper {

	Log logger =  LogFactory.getLog(KenoFiveHelper.class);
	
	public KenoPurchaseBean commonPurchseProcess(UserInfoBean userBean,
			KenoPurchaseBean kenoPurchaseBean) throws LMSException, SQLException {
				
		
		kenoPurchaseBean.setPromotkt(false);
		kenoPurchaseBean = kenoPurchaseTicket(userBean, kenoPurchaseBean);
		String saleStatus = kenoPurchaseBean.getSaleStatus();
		if ("SUCCESS".equalsIgnoreCase(saleStatus)) {
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			kenoPurchaseBean = helper.commonPromoPurchaseProcess(kenoPurchaseBean,
					userBean);
			// FortunePurchaseBean fortunePurchseben=
			// kenoPurchaseBean.getFortunePurchaseBean();
			if ("SUCCESS".equalsIgnoreCase(kenoPurchaseBean
					.getPromoSaleStatus())) {
				return kenoPurchaseBean;
			} else {
				helper.cancelTicket(kenoPurchaseBean.getTicket_no()
						+ kenoPurchaseBean.getReprintCount(), kenoPurchaseBean
						.getPurchaseChannel(), kenoPurchaseBean
						.getDrawIdTableMap(), kenoPurchaseBean.getGame_no(),
						kenoPurchaseBean.getPartyId(), kenoPurchaseBean
								.getPartyType(), kenoPurchaseBean
								.getRefMerchantId(), userBean, kenoPurchaseBean
								.getRefTransId());
			}
		}
		return kenoPurchaseBean;

	}
	
	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}
	
	public KenoPurchaseBean kenoPurchaseTicket(UserInfoBean userBean,
			KenoPurchaseBean kenoPurchaseBean) throws LMSException {
		kenoPurchaseBean.setSaleStatus("FAILED");
		Connection con=null;
        long balDed=0;
        String status = "";
        double oldTotalPurchaseAmt = 0.0;
		double modifiedTotalPurchaseAmt = 0.0;
		//ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.KENOFIVE_MGMT);
		sReq.setServiceMethod(ServiceMethodName.KENOFIVE_PURCHASE_TICKET);
		KenoRequestBean kenoRequestBean = new KenoRequestBean();
		sReq.setServiceData(kenoRequestBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		try {
		if (isDrawAvailable(kenoPurchaseBean.getGameId())
				|| DrawGameRPOSHelper.chkFreezeTimeSale(kenoPurchaseBean.getGameId())) {
			kenoPurchaseBean.setSaleStatus("NO_DRAWS");
			return kenoPurchaseBean;
		}
		//needs to be optimized in case of multiple validations
		if(!kenoValidateData(kenoPurchaseBean)){
			logger.debug("Data Validation returned false");
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
			} else if (playType.equalsIgnoreCase("perm2") || playType.equalsIgnoreCase("DC-perm2") || playType.equalsIgnoreCase("MN-perm2") || playType.equalsIgnoreCase("4By90-Perm2") || playType.equalsIgnoreCase("3By90-Perm2") || playType.equalsIgnoreCase("2By90-Perm2")) {
				noOfLines[i] = n[0] * (n[0] - 1) / 2;
			} else if (playType.equalsIgnoreCase("perm3") || playType.equalsIgnoreCase("DC-perm3") || playType.equalsIgnoreCase("MN-perm3")) {
				noOfLines[i] = n[0] * (n[0] - 1) * (n[0] - 2) / 6;
			} else if (playType.equalsIgnoreCase("Banker1AgainstAll") || playType.equalsIgnoreCase("MN-Banker1AgainstAll")) {
				noOfLines[i] = 89;
			} else if (playType.equalsIgnoreCase("banker") || playType.equalsIgnoreCase("MN-banker")) {
				noOfLines[i] = n[0] * n[1];
			}

			unitPriceArr[i] = Util.getUnitPrice(kenoPurchaseBean.getGameId(),
					playTypeArr[i]);
			//logger.debug("----unitPrice--" + unitPriceArr[i]);
			//logger.debug("----playTypeArr--" + playTypeArr[i]);
			totPurAmt += noOfLines[i] * unitPriceArr[i]
					* kenoPurchaseBean.getNoOfDraws() * betAmtMulArr[i];
		}

		kenoPurchaseBean.setUnitPrice(unitPriceArr);

		kenoPurchaseBean.setNoOfLines(noOfLines);

		//logger.debug("Total Purchase Amount:"	+ totPurAmt);

		if (totPurAmt <= 0) {
			kenoPurchaseBean.setSaleStatus("FAILED");// Error Draw
			return kenoPurchaseBean;
		}
		kenoPurchaseBean.setTotalPurchaseAmt(totPurAmt);
           
		
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			
			int autoCancelHoldDays=Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS")); 
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			helper.checkLastPrintedTicketStatusAndUpdate(userBean,Long.parseLong(kenoPurchaseBean.getLastSoldTicketNo()),"TERMINAL",kenoPurchaseBean.getRefMerchantId(),autoCancelHoldDays,kenoPurchaseBean.getActionName(), kenoPurchaseBean.getLastGameId(),con);

			logger.debug("SALE_AUTO_CANCEL_LOGS:SALE Continue for the new ticket");
		
			
			//Add condition in single if method transfer in this method checkLastPrintedTicketStatusAndUpdate in DrawgameRPOSHelper.java
			/*if(kenoPurchaseBean.getLastSoldTicketNo()!=null ){
				if(!"0".equalsIgnoreCase(kenoPurchaseBean.getLastSoldTicketNo())){
				//If below insertion throw an error or no row updated then stop sale
					Util.insertLastSoldTicketTeminal(kenoPurchaseBean.getUserId(), kenoPurchaseBean.getLastSoldTicketNo(),kenoPurchaseBean.getGameId(),con);
			}
			}*/
			
			// rg calling
			boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE",
					totPurAmt + "",con);
			if (!isFraud) {			
				 balDed = orgOnLineSaleCreditUpdation
						.drawTcketSaleBalDeduction(userBean, kenoPurchaseBean.getGameId(), kenoPurchaseBean
								.getTotalPurchaseAmt(),kenoPurchaseBean.getPlrMobileNumber(),con);
				 //check valDed value for  >0 else return error
				oldTotalPurchaseAmt = kenoPurchaseBean.getTotalPurchaseAmt();
				//logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"	+ oldTotalPurchaseAmt);
				if (balDed > 0) {
					//logger.debug("in keno if----------------");
					kenoPurchaseBean.setRefTransId(balDed + "");
					con.commit();
				}else {

					if (balDed == -1) {
						status = "AGT_INS_BAL";// Agent has insufficient
						// Balance
					} else if (balDed == -2) {
						status = "FAILED";// Error LMS
					} else if (balDed == 0) {
						status = "RET_INS_BAL";// Retailer has insufficient
						// Balance
					}
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
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}finally{
			DBConnect.closeCon(con);
		}
				
				
				
				try{
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

					//sRes = delegate.getResponse(sReq);
					//String responseString = sRes.getResponseData().toString();
					//Type elementType = new TypeToken<KenoPurchaseBean>(){}.getType();
					String responseString = delegate.getResponseString(sReq);
					KenoResponseBean kenoResponseBean = new Gson().fromJson(responseString, KenoResponseBean.class);
					if (kenoResponseBean.getIsSuccess()) {
						//kenoPurchaseBean = new Gson().fromJson(responseString, elementType);
						kenoPurchaseBean.setSaleStatus(kenoResponseBean.getSaleStatus());
						kenoPurchaseBean.setTicket_no(kenoResponseBean.getTicketNo());
						kenoPurchaseBean.setBarcodeCount(kenoResponseBean.getBarcodeCount());
						kenoPurchaseBean.setNoOfDraws(kenoResponseBean.getNoOfDraws());
						kenoPurchaseBean.setPurchaseTime(kenoResponseBean.getPurchaseTime());
						kenoPurchaseBean.setReprintCount(kenoResponseBean.getReprintCount());
						kenoPurchaseBean.setPlayerData(kenoResponseBean.getPlayerData());
						kenoPurchaseBean.setTotalPurchaseAmt(kenoResponseBean.getTotalPurchaseAmt());
						kenoPurchaseBean.setDrawDateTime(kenoResponseBean.getDrawDateTime());
						modifiedTotalPurchaseAmt = kenoPurchaseBean
								.getTotalPurchaseAmt();
						//logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"	+ modifiedTotalPurchaseAmt);
						con=DBConnect.getConnection();
						con.setAutoCommit(false);
						if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {
							
							balDed = orgOnLineSaleCreditUpdation
									.drawTcketSaleBalDedUpdate(userBean,
											kenoPurchaseBean.getGameId(),
											modifiedTotalPurchaseAmt,
											oldTotalPurchaseAmt, balDed,con);

						}

						
						
						int tickUpd = orgOnLineSaleCreditUpdation
								.drawTicketSaleTicketUpdate(balDed,
										kenoPurchaseBean.getTicket_no(),
										kenoPurchaseBean.getGameId(), userBean,kenoPurchaseBean.getPurchaseChannel(),con);

						if (tickUpd == 1) {
							/*AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
							ajxHelper.getAvlblCreditAmt(userBean,con);*/
							AjaxRequestHelper.getLiveAmt(userBean, con);
							//kenoPurchaseBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(),
							//		kenoPurchaseBean.getGame_no(), "PLAYER", "SALE", "DG"));
							kenoPurchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), kenoPurchaseBean.getGameId()));

							status = "SUCCESS";
							kenoPurchaseBean.setSaleStatus(status);							
							if (!"applet".equals(kenoPurchaseBean.getBarcodeType())) {
								IDBarcode.getBarcode(kenoPurchaseBean
										.getTicket_no()
										+ kenoPurchaseBean.getReprintCount());
							}
							con.commit();
							
							return kenoPurchaseBean;
						} else {
						//	con.rollback();
						//	DBConnect.closeCon(con);
							
							
							status = "FAILED";
							kenoPurchaseBean.setSaleStatus(status);
							// Code for cancelling the Ticket to be send to Draw
							// Game Engine
							new DrawGameRPOSHelper().cancelTicket(kenoPurchaseBean.getTicket_no()
									+ kenoPurchaseBean.getReprintCount(),
									kenoPurchaseBean.getPurchaseChannel(),
									kenoPurchaseBean.getDrawIdTableMap(),
									kenoPurchaseBean.getGame_no(),
									kenoPurchaseBean.getPartyId(),
									kenoPurchaseBean.getPartyType(),
									kenoPurchaseBean.getRefMerchantId(),
									userBean, kenoPurchaseBean.getRefTransId());

							return kenoPurchaseBean;
						}
					} else {
						//kenoPurchaseBean = new Gson().fromJson(responseString, elementType);
						kenoPurchaseBean.setSaleStatus(kenoResponseBean.getSaleStatus());
						if(kenoPurchaseBean.getSaleStatus() == null){
							kenoPurchaseBean.setSaleStatus("FAILED");// Error
							orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
									userBean, kenoPurchaseBean.getGame_no(),
									"FAILED", balDed);
							return kenoPurchaseBean;
						}else if("ERROR_TICKET_LIMIT".equalsIgnoreCase(kenoPurchaseBean.getSaleStatus())){
							orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
									userBean, kenoPurchaseBean.getGame_no(),
									"FAILED", balDed);
							return kenoPurchaseBean;
						}else{
							orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
									userBean, kenoPurchaseBean.getGame_no(),
									"FAILED", balDed);
							return kenoPurchaseBean;
						}						
					}			
				}catch (Exception se) {
					se.printStackTrace();
					if(kenoPurchaseBean.getSaleStatus() == null) {
						kenoPurchaseBean.setSaleStatus("FAILED");// Error
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(),"FAILED", balDed);
					}else{
						orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, kenoPurchaseBean.getGame_no(),"FAILED", balDed);
					}
				}finally{
					DBConnect.closeCon(con);
				}
		return kenoPurchaseBean;
	}
	
	public boolean kenoValidateData(KenoPurchaseBean kenoPurchaseBean) {//this method needs to be updated later
		if(kenoPurchaseBean != null){
			if(kenoPurchaseBean.getNoOfDraws()<1){
				logger.debug("insufficient no of draws");
				return false;
			}
			if(kenoPurchaseBean.getNoOfPanel()<1){
				logger.debug("insufficient no of panels");
				return false;
			}
			
			//Kenya project is not in  operation
			//boolean isKenya = "Kenya".equalsIgnoreCase((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED"));
			int noOfPanel = kenoPurchaseBean.getNoOfPanel();
			String playTypeArr[] = kenoPurchaseBean.getPlayType();
			//String[] noPickStr = kenoPurchaseBean.getNoPicked();			
			//String[] pickedNumbers = kenoPurchaseBean.getPlayerData();
			int[] qp = kenoPurchaseBean.getIsQuickPick();
		/*	for (int i = 0; i < noOfPanel; i++) {
				String playType = playTypeArr[i];
				String[] noPick = noPickStr[i].split(",");
				int[] n = new int[noPick.length];
				for (int j = 0; j < noPick.length; j++) {
					n[j] = Integer.parseInt(noPick[j]);
				}
				if(!"QP".equalsIgnoreCase(pickedNumbers[i])){
					if (playType.contains("Direct1")) {
						if(n[0]!=1 || qp[i]!=2 || pickedNumbers[i].split(",").length !=1 ){
							return false;
						}
					} else if (playType.equalsIgnoreCase("Direct2")) {
						if(n[0]!=2 || qp[i]!=2 || pickedNumbers[i].split(",").length !=2 ){
							return false;
						}
					}else if (playType.equalsIgnoreCase("Direct3")) {
						if(n[0]!=3 || qp[i]!=2 || pickedNumbers[i].split(",").length !=3 ){
							return false;
						}
					} else if (playType.equalsIgnoreCase("Direct4")) {
						if(n[0]!=4 || qp[i]!=2 || pickedNumbers[i].split(",").length !=4 ){
							return false;
						}				
						
					} else if (playType.equalsIgnoreCase("Direct5")) {
						if(n[0]!=5 || qp[i]!=2 || pickedNumbers[i].split(",").length !=5 ){
							return false;
						}				
						
					} else if (playType.equalsIgnoreCase("Perm2")) {
						//if(!isKenya){
						if(n[0]<3 || qp[i]!=2 || pickedNumbers[i].split(",").length <3 ){
							return false;
						}				
						//}
					} else if (playType.equalsIgnoreCase("Perm3")) {
						if(n[0]<4 || qp[i]!=2 || pickedNumbers[i].split(",").length <4 ){
							return false;
						}				
						
					} 
				}else{
					if (playType.contains("Direct1")) {
						if(n[0]!=1 || qp[i]!=1){
							return false;
						}
					} else if (playType.equalsIgnoreCase("Direct2")) {
						if(n[0]!=2 || qp[i]!=1 ){
							return false;
						}
					}else if (playType.equalsIgnoreCase("Direct3")) {
						if(n[0]!=3 || qp[i]!=1 ){
							return false;
						}
					} else if (playType.equalsIgnoreCase("Direct4")) {
						if(n[0]!=4 || qp[i]!=1 ){
							return false;
						}				
						
					} else if (playType.equalsIgnoreCase("Direct5")) {
						if(n[0]!=5 || qp[i]!=1 ){
							return false;
						}				
						
					} else if (playType.equalsIgnoreCase("Perm2")) {
						if(n[0]<3 || qp[i]!=1 ){
							return false;
						}				
						
					} else if (playType.equalsIgnoreCase("Perm3")) {
						if(n[0]<4 || qp[i]!=1 ){
							return false;
						}				
						
					} 
				}
				
				
			}*/
			
			
			boolean isValid = true;
			String[] pickedNumbersArr = kenoPurchaseBean.getPlayerData();
			//int noOfPanel = pickedNumbersArr.length;
			String[] noPickedArr = kenoPurchaseBean.getNoPicked();
			for (int i = 0; i < noOfPanel; i++) {
				
				String playerData = pickedNumbersArr[i];
				if (!"QP".equals(playerData)) {
				if (playTypeArr[i].contains("Direct")
						|| "Banker1AgainstAll".equals(playTypeArr[i]) || "MN-Banker1AgainstAll".equals(playTypeArr[i])) {
					int pickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]);
					if(qp[i]!=2 || playerData.split(",").length !=pickValue || Integer.parseInt(noPickedArr[i])!=pickValue){
						isValid=false;
						break;
					}
				/*	isValid = noPickedArr[i].equals(pickValue);
					logger.debug("-Direct---" + playTypeArr[i] + "---"
							+ noPickedArr[i]);
					if (!isValid) {
						break;
					}*/
				} else if (playTypeArr[i].contains("Perm")) {
					int minPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"MIN");
					int maxPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"MAX");
					int selPick =Integer.parseInt(noPickedArr[i]);
					//logger.debug("-Perm---" + playTypeArr[i] + "---"+ noPickedArr[i]);
					if (qp[i]!=2 || minPickValue > playerData.split(",").length
							|| maxPickValue < playerData.split(",").length ||minPickValue > selPick
							|| maxPickValue < selPick) {
						isValid = false;
						break;
					}
				} else if ("Banker".equals(playTypeArr[i]) || "MN-Banker".equals(playTypeArr[i])) {
					logger.debug("-Banker---" + playTypeArr[i] + "---"
							+ noPickedArr[i]);
					
					int minULPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"ULMIN");
					int maxULPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"ULMAX");
					int minBLPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"BLMIN");
					int maxBLPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"BLMAX");
					String selPick[] = noPickedArr[i].split(",");
					int selUL=Integer.parseInt(selPick[0]);
					int selBL=Integer.parseInt(selPick[1]);
					
					int pickedUL=playerData.substring(0,playerData.indexOf(",UL,")).split(",").length;
					int pickedBL=playerData.substring(playerData.indexOf(",UL,")+4,playerData.indexOf(",BL")).split(",").length;
						
					// for upper line & below line
					if (qp[i]!=2 || minULPickValue >pickedUL
							|| maxULPickValue < pickedUL
							|| minBLPickValue > pickedBL
							|| maxBLPickValue < pickedBL 
							||minULPickValue >selUL
							|| maxULPickValue < selUL
							|| minBLPickValue > selBL
							|| maxBLPickValue < selBL) {
						isValid = false;
						break;
					}
				}
							isValid = Util
							.validateNumber(KenoFiveConstants.START_RANGE,
									KenoFiveConstants.END_RANGE, playerData.replace(
											",UL,", ",").replace(",BL", ""), false);
					//logger.debug("-Data---" + playTypeArr[i] + "---"	+ noPickedArr[i] + "---" + playerData);
					if (!isValid) {
						break;
					}
				}else{
					if (playTypeArr[i].contains("Direct")
							|| "Banker1AgainstAll".equals(playTypeArr[i]) ||  "MN-Banker1AgainstAll".equals(playTypeArr[i])) {
						int pickValue = KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]);
						if(qp[i]!=1 || Integer.parseInt(noPickedArr[i])!=pickValue){
							isValid=false;
							break;
						}
					/*	isValid = noPickedArr[i].equals(pickValue);
						logger.debug("-Direct---" + playTypeArr[i] + "---"
								+ noPickedArr[i]);
						if (!isValid) {
							break;
						}*/
					} else if (playTypeArr[i].contains("Perm")) {
						int minPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"MIN");
						int maxPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"MAX");
						int selPick =Integer.parseInt(noPickedArr[i]);
						//logger.debug("-Perm---" + playTypeArr[i] + "---"+ noPickedArr[i]);
						if (qp[i]!=1 ||minPickValue > selPick
								|| maxPickValue < selPick) {
							isValid = false;
							break;
						}
					} else if ("Banker".equals(playTypeArr[i]) || "MN-Banker".equals(playTypeArr[i])) {
						logger.debug("-Banker---" + playTypeArr[i] + "---"
								+ noPickedArr[i]);
						
						int minULPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"ULMIN");
						int maxULPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"ULMAX");
						int minBLPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"BLMIN");
						int maxBLPickValue=KenoFiveConstants.BET_TYPE_MAP.get(playTypeArr[i]+"BLMAX");
						String selPick[] = noPickedArr[i].split(",");
						int selUL=Integer.parseInt(selPick[0]);
						int selBL=Integer.parseInt(selPick[1]);
						
								
						// for upper line & below line
						if (qp[i]!=1 ||minULPickValue >selUL
								|| maxULPickValue < selUL
								|| minBLPickValue > selBL
								|| maxBLPickValue < selBL) {
							isValid = false;
							break;
						}
					}
				}
			}

			if (!isValid) {
				kenoPurchaseBean.setSaleStatus("INVALID_INPUT");// Error Draw
				//setKenoPurchaseBean(kenoPurchaseBean);
				logger.error("-----------Keno Validation Error-------------------"
						+ kenoPurchaseBean.getSaleStatus());
				return false;
			}
			
			
		}else{
			logger.debug("keno bean null");
			return false;
		}
		return true;
		
	}
	
	
}
