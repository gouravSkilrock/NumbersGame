package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.itg.IDBarcode.IDBarcode;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.FourDigitPurchaseBean;
import com.skilrock.lms.dge.gameconstants.FourDigitConstants;
import com.skilrock.lms.web.drawGames.common.Util;

public class FourDigitHelper {
	
Log logger =  LogFactory.getLog(KenoFiveHelper.class);

	Set<String> nonRepeatNumbers = new HashSet<String>();;
	
	public FourDigitPurchaseBean commonPurchseProcess(UserInfoBean userBean,FourDigitPurchaseBean fourDigitPurchaseBean) throws LMSException, SQLException {
				
			fourDigitPurchaseBean.setPromotkt(false);
			fourDigitPurchaseBean = fourDigitPurchaseTicket(userBean, fourDigitPurchaseBean);
			return fourDigitPurchaseBean;
	}
	
	private boolean isDrawAvailable(int gameNo) {
		return Util.drawIdTableMap.get(gameNo).isEmpty();
	}	
	
	public FourDigitPurchaseBean fourDigitPurchaseTicket(UserInfoBean userBean,FourDigitPurchaseBean fourDigitPurchaseBean) throws LMSException {
		
				fourDigitPurchaseBean.setSaleStatus("FAILED");
				Connection con=null;
				long balDed=0;
				double oldTotalPurchaseAmt = 0.0;
				double modifiedTotalPurchaseAmt = 0.0;
				String status = "";
				
				ServiceResponse sRes = new ServiceResponse();
				ServiceRequest sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.FOURDIGIT);
				sReq.setServiceMethod(ServiceMethodName.FOURDIGIT_PURCHASE_TICKET);
				sReq.setServiceData(fourDigitPurchaseBean);
				IServiceDelegate delegate = ServiceDelegate.getInstance();

				try {
						logger.debug("Game ID = "+fourDigitPurchaseBean.getGameId());
						logger.debug(isDrawAvailable(fourDigitPurchaseBean.getGameId()));
						logger.debug(DrawGameRPOSHelper.chkFreezeTimeSale(fourDigitPurchaseBean.getGameId()));
						if (isDrawAvailable(fourDigitPurchaseBean.getGameId())|| DrawGameRPOSHelper.chkFreezeTimeSale(fourDigitPurchaseBean.getGameId())) {
									fourDigitPurchaseBean.setSaleStatus("NO_DRAWS");
									return fourDigitPurchaseBean;
						}
						//needs to be optimized in case of multiple validations
						if(!fourDigitValidateData(fourDigitPurchaseBean)){
								logger.debug("Data Validation returned false");
								return fourDigitPurchaseBean;
						}
		
						double totPurAmt = 0.0;						
						int noOfPanel = fourDigitPurchaseBean.getNoOfPanel();
						int noOfLines[] = new int[noOfPanel];
						int bigForecastArr[] = fourDigitPurchaseBean.getBigForecost();
						int smallForecastArr[] = fourDigitPurchaseBean.getSmallForecost();
						double unitPriceArr[] = new double[noOfPanel];
						String playTypeArr[] = fourDigitPurchaseBean.getPlayType();
						String[] pickedNumberArr = fourDigitPurchaseBean.getPlayerData();
						
						for (int i = 0; i < noOfPanel; i++) {
									String playType = playTypeArr[i];
										logger.debug("PLAY TYPE :"+playType);
									
									if (playType.contains("Straight")) {
											noOfLines[i] = 1;
									}else if (playType.equalsIgnoreCase("Permute")) {
											permuteNumbersNonRepeat("",pickedNumberArr[i].replace(",","").trim());
											noOfLines[i] = nonRepeatNumbers.size();
									}else if (playType.equalsIgnoreCase("Roll")) {
											noOfLines[i] = FourDigitConstants.DIGIT_RANGE.size();
									}									
										logger.debug("no of lines will be : "+noOfLines[i]);

									unitPriceArr[i] = Util.getUnitPrice(fourDigitPurchaseBean.getGameId(),playTypeArr[i]);
										logger.debug("----unitPrice--" + unitPriceArr[i]);
										logger.debug("----playTypeArr--" + playTypeArr[i]);
									totPurAmt += noOfLines[i] * unitPriceArr[i]* fourDigitPurchaseBean.getNoOfDraws() * (bigForecastArr[i] + smallForecastArr[i]);
										logger.debug("---TotalAmount--" + totPurAmt);
						}

						fourDigitPurchaseBean.setUnitPrice(unitPriceArr);
						fourDigitPurchaseBean.setNoOfLines(noOfLines);
						logger.debug("Total Purchase Amount:"	+ totPurAmt);

						if (totPurAmt <= 0) {
							fourDigitPurchaseBean.setSaleStatus("FAILED");// Error Draw
							return fourDigitPurchaseBean;
						}
						fourDigitPurchaseBean.setTotalPurchaseAmt(totPurAmt);
						con=DBConnect.getConnection();
						con.setAutoCommit(false);
			
						// rg calling
						boolean isFraud = ResponsibleGaming.respGaming(userBean, "DG_SALE", totPurAmt + "",con);
						if (!isFraud) {			
								balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDeduction(userBean, fourDigitPurchaseBean.getGameId(), fourDigitPurchaseBean.getTotalPurchaseAmt(),fourDigitPurchaseBean.getPlrMobileNumber(),con);
								//check valDed value for  >0 else return error
								oldTotalPurchaseAmt = fourDigitPurchaseBean.getTotalPurchaseAmt();
								logger.debug("Total Purchase Amt inside DrawGameRPOSHelper Just Before  getting Success :"+ oldTotalPurchaseAmt);
								if (balDed > 0) {
									logger.debug("in FourDigit if----------------");
									fourDigitPurchaseBean.setRefTransId(balDed + "");
									con.commit();
								}else {
										if (balDed == -1) {
											status = "AGT_INS_BAL";// Agent has insufficient Balance
										} else if (balDed == -2) {
											status = "FAILED";// Error LMS
										} else if (balDed == 0) {
											status = "RET_INS_BAL";// Retailer has insufficient Balance
										}
										fourDigitPurchaseBean.setSaleStatus(status);
										return fourDigitPurchaseBean;
								}
						} else {
								logger.debug("Responsing Gaming not allowed to sale");
								fourDigitPurchaseBean.setSaleStatus("FRAUD");
								return fourDigitPurchaseBean;
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
						sRes = delegate.getResponse(sReq);
						
						if (sRes.getIsSuccess()) {
							fourDigitPurchaseBean = (FourDigitPurchaseBean) sRes.getResponseData();
							modifiedTotalPurchaseAmt = fourDigitPurchaseBean.getTotalPurchaseAmt();
							logger.debug("Total Purchase Amt inside DrawGameRPOSHelper After getting Success :"+ modifiedTotalPurchaseAmt);
							con=DBConnect.getConnection();
							con.setAutoCommit(false);
						
							if (oldTotalPurchaseAmt != modifiedTotalPurchaseAmt) {							
								balDed = orgOnLineSaleCreditUpdation.drawTcketSaleBalDedUpdate(userBean,fourDigitPurchaseBean.getGameId(),modifiedTotalPurchaseAmt,oldTotalPurchaseAmt, balDed,con);
							}					
						
							int tickUpd = orgOnLineSaleCreditUpdation.drawTicketSaleTicketUpdate(balDed,fourDigitPurchaseBean.getTicket_no(),fourDigitPurchaseBean.getGameId(), userBean,fourDigitPurchaseBean.getPurchaseChannel(),con);

							if (tickUpd == 1) {
								AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
								ajxHelper.getAvlblCreditAmt(userBean,con);
								fourDigitPurchaseBean.setAdvMsg(Util.getDGSaleAdvMessage(userBean.getUserOrgId(), fourDigitPurchaseBean.getGameId()));
								status = "SUCCESS";
								fourDigitPurchaseBean.setSaleStatus(status);							
								if (!"applet".equals(fourDigitPurchaseBean.getBarcodeType())) {
									IDBarcode.getBarcode(fourDigitPurchaseBean.getTicket_no()+ fourDigitPurchaseBean.getReprintCount());
								}
								con.commit();							
								return fourDigitPurchaseBean;
							} else {
						
								status = "FAILED";
								fourDigitPurchaseBean.setSaleStatus(status);
								// Code for cancelling the Ticket to be send to Draw
								// Game Engine
								new DrawGameRPOSHelper().cancelTicket(fourDigitPurchaseBean.getTicket_no()
									+ fourDigitPurchaseBean.getReprintCount(),
									fourDigitPurchaseBean.getPurchaseChannel(),
									fourDigitPurchaseBean.getDrawIdTableMap(),
									fourDigitPurchaseBean.getGame_no(),
									fourDigitPurchaseBean.getPartyId(),
									fourDigitPurchaseBean.getPartyType(),
									fourDigitPurchaseBean.getRefMerchantId(),
									userBean, fourDigitPurchaseBean.getRefTransId());

								return fourDigitPurchaseBean;
							}
						} else {
							fourDigitPurchaseBean = (FourDigitPurchaseBean) sRes.getResponseData();
							if(fourDigitPurchaseBean.getSaleStatus() == null){
									fourDigitPurchaseBean.setSaleStatus("FAILED");// Error
									orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fourDigitPurchaseBean.getGame_no(),"FAILED", balDed);
									return fourDigitPurchaseBean;
							}else if("ERROR_TICKET_LIMIT".equalsIgnoreCase(fourDigitPurchaseBean.getSaleStatus())){
									orgOnLineSaleCreditUpdation.drawTicketSaleRefund(
									userBean, fourDigitPurchaseBean.getGame_no(),"FAILED", balDed);
									return fourDigitPurchaseBean;
							}else{
									orgOnLineSaleCreditUpdation.drawTicketSaleRefund(userBean, fourDigitPurchaseBean.getGame_no(),"FAILED", balDed);
									return fourDigitPurchaseBean;
							}
						}
					
				}catch (SQLException se) {
					se.printStackTrace();
				}finally{
					DBConnect.closeCon(con);
				}
		return fourDigitPurchaseBean;
	}
	
	public void permuteNumbersNonRepeat(String beginningString, String endingString) {
		
	    if (endingString.length() <= 1){ 
	    	nonRepeatNumbers.add(beginningString + endingString);
	    }
	    else
	      for (int i = 0; i < endingString.length(); i++) {
	        try {
	          String newString = endingString.substring(0, i) + endingString.substring(i + 1);

	          permuteNumbersNonRepeat(beginningString + endingString.charAt(i), newString);
	        } catch (StringIndexOutOfBoundsException exception) {
	          exception.printStackTrace();
	        }
	      }
	}
	
	
	public boolean fourDigitValidateData(FourDigitPurchaseBean fourDigitPurchaseBean) {//this method needs to be updated later
		boolean isValid = true;	
		if(fourDigitPurchaseBean != null){
					if(fourDigitPurchaseBean.getNoOfDraws()<1){
								logger.debug("insufficient no of draws");
								return false;
					}
					if(fourDigitPurchaseBean.getNoOfPanel()<1){
							logger.debug("insufficient no of panels");
							return false;
					}
					int noOfPanel = fourDigitPurchaseBean.getNoOfPanel();
					String playTypeArr[] = fourDigitPurchaseBean.getPlayType();
					int[] qp = fourDigitPurchaseBean.getIsQuickPick();					
					String[] pickedNumbersArr = fourDigitPurchaseBean.getPlayerData();
					String[] noPickedArr = fourDigitPurchaseBean.getNoPicked();
					int[] bigForecastArr = fourDigitPurchaseBean.getBigForecost();
					int[] smallForecastArr = fourDigitPurchaseBean.getSmallForecost();
			
					for (int i = 0; i < noOfPanel; i++) {	
				
								String playerData = pickedNumbersArr[i];
								
								if (!"QP".equals(playerData)) {									
									
									int pickValue = FourDigitConstants.BET_TYPE_MAP.get(playTypeArr[i]);										
									if (playTypeArr[i].contains("Straight") || "Permute".equals(playTypeArr[i]) || "Roll".equals(playTypeArr[i])) {							
										if(qp[i]!=2 || playerData.length() !=pickValue || Integer.parseInt(noPickedArr[i])!=pickValue || (bigForecastArr[i] == 0 && smallForecastArr[i] == 0)){
											isValid=false;
											break;
										}
									}									
									if("Roll".equals(playTypeArr[i])){
										int count = 0;
										List<String> splitNumber = Arrays.asList(playerData.split(""));										
										for(int j =0; j<splitNumber.size(); j++){
											if(splitNumber.get(j).equals("R")){count++;}											
										}
										if(count!=1){
											isValid=false;
											break;
										}
									}									
									//isValid = Util.validateNumber(FourDigitConstants.START_RANGE,FourDigitConstants.END_RANGE, playerData.replace(",R,", ","), true);
									logger.debug("-Data---" + playTypeArr[i] + "---"+ noPickedArr[i] + "---" + playerData);
									if (!isValid) {break;}
								}else{
									int pickValue = FourDigitConstants.BET_TYPE_MAP.get(playTypeArr[i]);
									if(qp[i]!=1 || Integer.parseInt(noPickedArr[i])!=pickValue || (bigForecastArr[i] == 0 && smallForecastArr[i] == 0)){
										isValid=false;
										break;
									}
								}
					}
								if (!isValid) {
									fourDigitPurchaseBean.setSaleStatus("INVALID_INPUT");// Error Draw
									logger.error("-----------Four Digit Validation Error-------------------"+ fourDigitPurchaseBean.getSaleStatus());
									return false;
								}
					
			}else{
			//logger.debug("FourDigit bean null");
			return false;
		}
		return true;		
	}
}