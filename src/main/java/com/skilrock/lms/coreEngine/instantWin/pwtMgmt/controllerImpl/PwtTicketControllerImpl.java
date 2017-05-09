package com.skilrock.lms.coreEngine.instantWin.pwtMgmt.controllerImpl;

import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;

public class PwtTicketControllerImpl {

	public PwtVerifyTicketBean prizeWinningVerifyTicket(String merchantName,long ticketNumber) throws SLEException{
		PwtVerifyTicketBean pwtVerifyTicketBean=null;
		try{
			//Need to make class file for find out length
			if(String.valueOf(ticketNumber).length() == 18){
				pwtVerifyTicketBean=SportLotteryServiceIntegration.prizeWinningVerifyTicket(merchantName,ticketNumber);
				
				
			}else{
				throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE,SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
			
		}catch (SLEException e) {
			throw e;
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
		
		return pwtVerifyTicketBean;
		
	}
}
