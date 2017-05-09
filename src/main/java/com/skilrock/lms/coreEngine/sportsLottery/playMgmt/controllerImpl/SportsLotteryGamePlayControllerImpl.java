package com.skilrock.lms.coreEngine.sportsLottery.playMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGamePlayBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils;

import com.skilrock.lms.coreEngine.sportsLottery.playMgmt.daoImpl.SportsLotteryGamePlayDaoImpl;


public class SportsLotteryGamePlayControllerImpl {

	public SportsLotteryGamePlayBean purchaseTicketControllerImpl(SportsLotteryGamePlayBean gamePlayBean,UserInfoBean userBean) throws SLEException{
		Connection con=null;
		SportsLotteryGamePlayBean gamePlayBeanResponse=null;
		try{
		
			
			if(gamePlayBean.getTotalPurchaseAmt() <= 0){
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			if(gamePlayBean.getTotalPurchaseAmt() > SportsLotteryUtils.gameInfoMap.get(gamePlayBean.getGameId()).getMaxTicketAmt()){
				throw new SLEException(SLEErrors.TICKET_SALE_LIMIT_REACHED_ERROR_CODE, SLEErrors.TICKET_SALE_LIMIT_REACHED_ERROR_MESSAGE);

			}
			
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
//			long transId=SportsLotteryGamePlayDaoImpl.sportsLotteryPurchaseTicketDaoImpl(gamePlayBean, userBean, con);
			long transId=SportsLotteryGamePlayDaoImpl.sportsLotteryPurchaseTicketDaoImpl(null, userBean, con);			
			if(transId > 0){
				gamePlayBean.setRefTransId(String.valueOf(transId));
				gamePlayBean.setIsPromoTicket(false);
				gamePlayBeanResponse=SportLotteryServiceIntegration.getSportsLotteryGamePlay(gamePlayBean);
			}
			
			SportsLotteryGamePlayDaoImpl.updatePurchaseTicket(gamePlayBeanResponse.getTicketNumber(), transId, gamePlayBeanResponse.getGameId(),gamePlayBeanResponse.getGameTypeId(), con);
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userBean,con);
			con.commit();
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

		}catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closeCon(con);
		}
		
		return gamePlayBeanResponse;
	}
}
