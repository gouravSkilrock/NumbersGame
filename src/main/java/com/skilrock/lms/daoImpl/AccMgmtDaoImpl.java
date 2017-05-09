package com.skilrock.lms.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.instantWin.common.IWErrors;
import com.skilrock.lms.coreEngine.instantWin.common.IWException;
import com.skilrock.lms.coreEngine.instantWin.common.IWUtil;
import com.skilrock.lms.coreEngine.instantWin.playMgmt.daoImpl.InstantWinGamePlayDaoImpl;
import com.skilrock.lms.coreEngine.sportsBetting.playMgmt.daoImpl.SportsBettingGamePlayDaoImpl;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.playMgmt.daoImpl.SportsLotteryGamePlayDaoImpl;
import com.skilrock.lms.dao.AccMgmtDao;
import com.skilrock.lms.rest.services.bean.TPResponseBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;
import com.skilrock.lms.rest.services.bean.TPTxResponseBean;
import com.skilrock.lms.rest.services.common.ReqResParser;
import com.skilrock.lms.web.drawGames.common.Util;

public final class AccMgmtDaoImpl implements AccMgmtDao {
	private static Logger logger = LoggerFactory.getLogger(AccMgmtDaoImpl.class);
	private volatile static AccMgmtDaoImpl accMgmtDaoImpl = null;

	private AccMgmtDaoImpl(){}
	public static AccMgmtDaoImpl getInstance() {
		if (accMgmtDaoImpl == null) {
			synchronized (AccMgmtDaoImpl.class) {
				if (accMgmtDaoImpl == null) {
					logger.info("getInstance(): First time getInstance invoked!");
					accMgmtDaoImpl = new AccMgmtDaoImpl();
				}
			}
		}
		return accMgmtDaoImpl;
	}
	
	// Service wise Sale Transactions
	@Override
	public TPResponseBean manageAccountsForSale(UserInfoBean userInfoBean , TPTxRequestBean tpTransactionBean) throws LMSException{
		
		long lmsTxId=0;
		int errorCode = 0;
		String errorMessage = null;
		TPTxResponseBean tpTxResponseBean = null;
		TPResponseBean tpResponseBean = null;
		Connection con = null;
		try {
			tpTxResponseBean = new TPTxResponseBean();
			tpResponseBean = new TPResponseBean();
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			

			if(tpTransactionBean.getServiceCode().equals("SLE")){
				boolean isFraud = ResponsibleGaming.respGaming(userInfoBean,"SLE_SALE", tpTransactionBean.getTxAmount() + "", con);
				if (!isFraud) {
					try{
						lmsTxId = SportsLotteryGamePlayDaoImpl.sportsLotteryPurchaseTicketDaoImpl(tpTransactionBean,userInfoBean, con);
					// Update Heart Beat After Sale Completion
					Util.setHeartBeatAndSaleTime(userInfoBean.getUserOrgId(),"SLE_SALE",con);
					con.commit();
					/// 
					
					if(tpTransactionBean.getTokenId()!=null &&!tpTransactionBean.getTokenId().trim().isEmpty()){
						Util.setEbetSaleRequestStatusDone(tpTransactionBean.getTokenId(), userInfoBean.getUserOrgId(), con) ;
						con.commit();
					
					}
					
				}catch (SLEException e) {
					errorCode = e.getErrorCode();
					errorMessage = e.getErrorMessage();
				}catch (Exception e) {
					errorCode = SLEErrors.GENERAL_EXCEPTION_ERROR_CODE;
					errorMessage = SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE;
				}
			}else{
				errorCode = SLEErrors.PURCHASE_FRAUD_ERROR_CODE;
				errorMessage = SLEErrors.PURCHASE_FRAUD_ERROR_MESSAGE;
			}
		}else if ("IW".equals(tpTransactionBean.getServiceCode())) {
//			boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "IW_SALE", tpTransactionBean.getTxAmount() + "", con);
			boolean isFraud = false;
			if (!isFraud) {
				try {
					tpTransactionBean.setGameId(IWUtil.getGameId("INSTANT_WIN"));
					lmsTxId = InstantWinGamePlayDaoImpl.instantWinPurchaseTicketDaoImpl(tpTransactionBean, userInfoBean, con);

					// Update Heart Beat After Sale Completion
					Util.setHeartBeatAndSaleTime(userInfoBean.getUserOrgId(), "IW_SALE", con);
					con.commit();
				} catch (IWException e) {
					errorCode = e.getErrorCode();
					errorMessage = e.getErrorMessage();
				} catch (Exception e) {
					errorCode = IWErrors.GENERAL_EXCEPTION_ERROR_CODE;
					errorMessage = IWErrors.GENERAL_EXCEPTION_ERROR_MESSAGE;
				}
			} else {
				errorCode = IWErrors.PURCHASE_FRAUD_ERROR_CODE;
				errorMessage = IWErrors.PURCHASE_FRAUD_ERROR_MESSAGE;
			}
		} else if(tpTransactionBean.getServiceCode().equals("DGE")){
			// PERFORM SALES FOR DGE
		}else if(tpTransactionBean.getServiceCode().equals("SBS")){
			boolean isFraud = false;
			if (!isFraud) {
				try{
					lmsTxId = SportsBettingGamePlayDaoImpl.sportsBettingPurchaseTicketDaoImpl(tpTransactionBean,userInfoBean, con);
				Util.setHeartBeatAndSaleTime(userInfoBean.getUserOrgId(),"SBS_SALE",con);
				con.commit();
			}catch (SLEException e) {
				errorCode = e.getErrorCode();
				errorMessage = e.getErrorMessage();
			}catch (Exception e) {
				errorCode = SLEErrors.GENERAL_EXCEPTION_ERROR_CODE;
				errorMessage = SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE;
			}
		}else{
			errorCode = SLEErrors.PURCHASE_FRAUD_ERROR_CODE;
			errorMessage = SLEErrors.PURCHASE_FRAUD_ERROR_MESSAGE;
		}
	}else{
			errorCode = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE;
			errorMessage = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE;
		}
				tpTxResponseBean.setMerTxId((lmsTxId>0)?lmsTxId:0);
				tpTxResponseBean.setResponseMessage(errorMessage);
				tpResponseBean.setResponseCode(errorCode);
				tpResponseBean.setResponseMessage(errorMessage);
				ReqResParser reqResParser = ReqResParser.getInstance();
				reqResParser.prepareResponseForSale(userInfoBean , tpTransactionBean ,  tpTxResponseBean, tpResponseBean, con);
			
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();// TEMP
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();// TEMP
		}finally{
			DBConnect.closeCon(con);
		}
	return tpResponseBean;
	}

	// Service wise Sale Refund Transactions
	@Override
	public TPResponseBean manageAccountsForCancel(UserInfoBean userInfoBean , TPTxRequestBean tpTransactionBean) throws LMSException {

		long lmsTxId = 0;
		int errorCode = 0;
		String errorMessage = "SUCCESS";
		
		Connection con = null;
		TPTxResponseBean tpTxResponseBean = null;
		TPResponseBean tpResponseBean = null;
		try {
			tpTxResponseBean = new TPTxResponseBean();
			tpResponseBean = new TPResponseBean();

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			
			
			if(tpTransactionBean.getServiceCode().equals("SLE")){
				// CONSIDER AUTO CANCEL FOR RG NO CANCEL LIMIT SHOULD BE INCREASED AND AMOUNT MUST BE DECREASED
				
				boolean isFraud = (tpTransactionBean.getTxType().equals("AUTO_WAGER_REFUND"))?false:ResponsibleGaming.respGaming(userInfoBean, "SLE_CANCEL", "1", con);
				if(!isFraud){
					//VALIDATE TICKET FOR USER 
					if (userInfoBean.getUserId() != getRetailerUserIdFromEngineSaleTxnId(tpTransactionBean,con)) {
						errorMessage = "Unautorized Retailer for This Ticket";
						errorCode = LMSErrors.INVALID_TICKET_ERROR_CODE ;
					}else{
						//CANCEL TICKET
						try {
							lmsTxId = SportsLotteryGamePlayDaoImpl.refundPurchaseTicket(tpTransactionBean,userInfoBean, con);

							//subtract cancel amount from sale in responsible Gaming
							ResponsibleGaming.respGaming(userInfoBean, "SLE_CANCEL_AMOUNT",String.valueOf(tpTransactionBean.getTxAmount()), con);

							con.commit();
						} catch (SLEException e) {
							if(e.getErrorCode() == SLEErrors.CANCEL_INVALID_ERROR_CODE){
								con.commit();
							}
							errorCode = e.getErrorCode();
							errorMessage = e.getErrorMessage();
					}
				}
			}else{
				if(userInfoBean.getUserSession() == null){
					errorCode = SLEErrors.SESSION_TIME_OUT_ERROR_CODE;
					errorMessage = SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE;
				}else{
					errorCode=LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE;
				}
			}
		}else if ("IW".equals(tpTransactionBean.getServiceCode())) {
			boolean isFraud = ("AUTO_WAGER_REFUND".equals(tpTransactionBean.getTxType())) ? false : ResponsibleGaming.respGaming(userInfoBean, "IW_CANCEL", "1", con);
			if (!isFraud) {
				// VALIDATE TICKET FOR USER
//				if (userInfoBean.getUserId() != Util.getUserIdFromTicket(tpTransactionBean.getTicketNumber())) {
//					errorMessage = "Unautorized Retailer for This Ticket";
//					errorCode = LMSErrors.INVALID_TICKET_ERROR_CODE;
//				} else {
					// CANCEL TICKET
					try {
						// Hard Coded as single game is used for every game in IW Engine
						tpTransactionBean.setGameId(IWUtil.getGameId("INSTANT_WIN"));
						lmsTxId = InstantWinGamePlayDaoImpl.refundPurchaseTicket(tpTransactionBean, userInfoBean, con);

						// subtract cancel amount from sale in responsible Gaming
//						ResponsibleGaming.respGaming(userInfoBean, "IW_CANCEL_AMOUNT", String.valueOf(tpTransactionBean.getTxAmount()), con);
						con.commit();
					} catch (IWException e) {
						if (e.getErrorCode() == IWErrors.CANCEL_INVALID_ERROR_CODE) {
							con.commit();
						}
						errorCode = e.getErrorCode();
						errorMessage = e.getErrorMessage();
					}
//				}
			} else {
				if (userInfoBean.getUserSession() == null) {
					errorCode = IWErrors.SESSION_TIME_OUT_ERROR_CODE;
					errorMessage = IWErrors.SESSION_TIME_OUT_ERROR_MESSAGE;
				} else {
					errorCode = LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE;
				}
			}
		} else if(tpTransactionBean.equals("DGE")){
			// PERFORM SALES REFUND FOR DGE
		}else{
			errorCode = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE;
			errorMessage = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE;
		}
			
			tpTxResponseBean.setMerTxId((lmsTxId>0)?lmsTxId:0);
			tpTxResponseBean.setResponseMessage(errorMessage);
			tpResponseBean.setResponseCode(errorCode);
			ReqResParser reqResParser = ReqResParser.getInstance();
			reqResParser.prepareResponseForSale(userInfoBean , tpTransactionBean ,  tpTxResponseBean, tpResponseBean, con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();// TEMP
		}finally{
			DBConnect.closeCon(con);
		}
		return tpResponseBean;
	}
	
	
	public int getRetailerUserIdFromEngineSaleTxnId(TPTxRequestBean tpTxRequestBean,Connection con){
		String retQry;
		Statement retstmt=null;
		ResultSet rs=null;
		int retUserId=0;
		try{
			retstmt=con.createStatement();
			retQry="select user_id from st_sle_ret_sale srs inner join st_lms_organization_master lom on srs.retailer_org_id=lom.organization_id  inner join st_lms_user_master lum on lum.organization_id=lom.organization_id   where engine_tx_id="+tpTxRequestBean.getEngineSaleTxId()+" and game_id="+tpTxRequestBean.getGameId()+" and game_type_id="+tpTxRequestBean.getGameTypeId()+"";
			logger.info("retailer Info for Ticket Cancellation"+retQry);
			rs=retstmt.executeQuery(retQry);
			if(rs.next()){
				retUserId=rs.getInt("user_id");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(retstmt, rs);
		}
		return retUserId;
	}
	
	//service wise reprint
	@Override
	public TPResponseBean manageRGForReprint(UserInfoBean userInfoBean , TPTxRequestBean tpTransactionBean) throws LMSException {

		int errorCode = 0;
		Connection con = null;
		TPResponseBean tpResponseBean = null;
		try {
			tpResponseBean = new TPResponseBean();
			con = DBConnect.getConnection();
			if(tpTransactionBean.getServiceCode().equals("SLE")){
					errorCode = SportsLotteryGamePlayDaoImpl.updateRgForTicketReprint(userInfoBean , tpTransactionBean ,con);
			} else if(tpTransactionBean.equals("DGE")){
				// PERFORM SALES REPRINT FOR DGE
			} else{
				errorCode = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE;
			}
			tpResponseBean.setResponseCode(errorCode);
			//get retailer balance and set in bean to get it at sle end
			String balString = new AjaxRequestHelper().getAvlblCreditAmt(userInfoBean, con);
			tpResponseBean.setResponseData(Double.parseDouble(balString.split("\\=")[3]));
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();// TEMP
		}finally{
			DBConnect.closeCon(con);
		}
		return tpResponseBean;
	}

	public TPResponseBean manageRGForInvalidPWT(UserInfoBean userBean , TPTxRequestBean tpTransactionBean) throws LMSException{
		int errorCode = 0;
		Connection con = null;
		TPResponseBean tpResponseBean = null;
		try {
			tpResponseBean = new TPResponseBean();
			con = DBConnect.getConnection();
			if(tpTransactionBean.getServiceCode().equals("SLE")){
					errorCode = SportsLotteryGamePlayDaoImpl.updateRGForInvalidPWT(userBean , tpTransactionBean ,con);
			} else if(tpTransactionBean.equals("DGE")){
				// PERFORM SALES REPRINT FOR DGE
			} else{
				errorCode = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE;
			}
			tpResponseBean.setResponseCode(errorCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();// TEMP
		}finally{
			DBConnect.closeCon(con);
		}
		return tpResponseBean;
	}

	public TPResponseBean manageAccountsForBoCancel(UserInfoBean userInfoBean , TPTxRequestBean tpTransactionBean) throws LMSException {
		long lmsTxId = 0;
		int errorCode = 0;
		String errorMessage = "SUCCESS";
		
		Connection con = null;
		TPTxResponseBean tpTxResponseBean = null;
		TPResponseBean tpResponseBean = null;
		try {
			tpTxResponseBean = new TPTxResponseBean();
			tpResponseBean = new TPResponseBean();

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			
			if(tpTransactionBean.getServiceCode().equals("SLE")){
				// CONSIDER AUTO CANCEL FOR RG NO CANCEL LIMIT SHOULD BE INCREASED AND AMOUNT MUST BE DECREASED
				
				boolean isFraud = (tpTransactionBean.getTxType().equals("AUTO_WAGER_REFUND"))?false:ResponsibleGaming.respGaming(userInfoBean, "SLE_CANCEL", "1", con);
				if(!isFraud){
						//CANCEL TICKET
						try {
							lmsTxId = SportsLotteryGamePlayDaoImpl.refundPurchaseTicket(tpTransactionBean,userInfoBean, con);

							//subtract cancel amount from sale in responsible Gaming
							ResponsibleGaming.respGaming(userInfoBean, "SLE_CANCEL_AMOUNT",String.valueOf(tpTransactionBean.getTxAmount()), con);

							con.commit();
						} catch (SLEException e) {
							if(e.getErrorCode() == SLEErrors.CANCEL_INVALID_ERROR_CODE){
								con.commit();
							}
							errorCode = e.getErrorCode();
							errorMessage = e.getErrorMessage();
					
				}
			}else{
					errorCode = LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE;
			}
		}else if(tpTransactionBean.equals("DGE")){
			// PERFORM SALES REFUND FOR DGE
		}else{
			errorCode = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE;
			errorMessage = LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE;
		}
			
			tpTxResponseBean.setMerTxId((lmsTxId>0)?lmsTxId:0);
			tpTxResponseBean.setResponseMessage(errorMessage);
			tpResponseBean.setResponseCode(errorCode);
			ReqResParser reqResParser = ReqResParser.getInstance();
			reqResParser.prepareResponseForSale(userInfoBean , tpTransactionBean ,  tpTxResponseBean, tpResponseBean, con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();// TEMP
		}finally{
			DBConnect.closeCon(con);
		}
		return tpResponseBean;
	}
	
	public double fetchOrgBalance(long userId, Connection con) throws LMSException {
		double balance = 0.0;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT (available_credit-claimable_bal) AS availbale_sale_bal FROM st_lms_organization_master orgMaster INNER JOIN st_lms_user_master userMaster ON orgMaster.organization_id = userMaster.organization_id WHERE user_id = " + userId);
			if (rs.next())
				balance = rs.getDouble("availbale_sale_bal");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return balance;
	}
	
}