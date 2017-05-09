package com.skilrock.lms.coreEngine.sportsLottery.pwtMgmt.controllerImpl;

import java.sql.Connection;
import java.util.Map;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PayPwtTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryPayPwtBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;
import com.skilrock.lms.coreEngine.sportsLottery.pwtMgmt.daoImpl.PayPwtTicketDaoImpl;

public class PayPwtTicketControllerImpl {

	public PwtVerifyTicketBean payPwtTicket(SportsLotteryPayPwtBean payPwtTicketBean,UserInfoBean userBean) throws SLEException{
		PwtVerifyTicketBean pwtVerifyTicketBean=null;
		Connection con=null;
		PayPwtTicketBean payPwtTicketRequestBean=null;
		try{
			//Need to make class file for find out length
			if(String.valueOf(payPwtTicketBean.getTicketNumber()).length() == 18){
				pwtVerifyTicketBean=SportLotteryServiceIntegration.prizeWinningVerifyTicket(payPwtTicketBean.getMerchantName(),payPwtTicketBean.getTicketNumber());
				
				
				con=DBConnect.getConnection();
				con.setAutoCommit(false);
				Map<Integer, Map<Integer, String>> refDrawTransmap=PayPwtTicketDaoImpl.sportsLotteryPayPwtTicketDaoImpl(pwtVerifyTicketBean,payPwtTicketBean, userBean, con);
				payPwtTicketRequestBean=new PayPwtTicketBean();
				payPwtTicketRequestBean.setDrawRefTransMap(refDrawTransmap);
				payPwtTicketRequestBean.setMerchantName(payPwtTicketBean.getMerchantName());
				payPwtTicketRequestBean.setUserId(userBean.getUserId());
				payPwtTicketRequestBean.setUserType(userBean.getUserType());
				payPwtTicketRequestBean.setWinningChannel("TERMINAL");
				payPwtTicketRequestBean.setTicketNumber(payPwtTicketBean.getTicketNumber());
				
				SportLotteryServiceIntegration.payPrizeWinningTicket(payPwtTicketRequestBean);

			}else{
				throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE,SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userBean,con);
			con.commit();
		}catch (SLEException e) {
			throw e;
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(con);
		}
		
		
		return pwtVerifyTicketBean;
		
	}
}
