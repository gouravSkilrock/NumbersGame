package com.skilrock.lms.controller.accMgmtController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.dao.AccMgmtDao;
import com.skilrock.lms.dao.common.DaoFactory;
import com.skilrock.lms.rest.services.bean.TPResponseBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;

public class AccountMgmtController {
	private static Logger logger = LoggerFactory.getLogger(AccountMgmtController.class);
	private volatile static AccountMgmtController accountMgmtController = null;

	private AccountMgmtController(){}
	public static AccountMgmtController getInstance() {
		if (accountMgmtController == null) {
			synchronized (AccountMgmtController.class) {
				if (accountMgmtController == null) {
					logger.info("getInstance(): First time getInstance was invoked!");
					accountMgmtController = new AccountMgmtController();
				}
			}
		}
		return accountMgmtController;

	}
	
	
	public TPResponseBean manageAccounts(UserInfoBean userInfoBean , TPTxRequestBean tpTxRequestBean) throws Exception{
		logger.info("inside manageAccounts in Controller ...");
		long startTime = System.currentTimeMillis();
		AccMgmtDao accDao = null;
		TPResponseBean responseBean = null;
		try {
			accDao = DaoFactory.getAccMgmtDao();
			String txType = tpTxRequestBean.getTxType();
			if (txType.equals("WAGER")) {
				responseBean = accDao.manageAccountsForSale(userInfoBean,tpTxRequestBean);
			} else if (txType.equals("WAGER_REFUND") || txType.equals("AUTO_WAGER_REFUND")) {
				responseBean = accDao.manageAccountsForCancel(userInfoBean, tpTxRequestBean);
			} else if (txType.equals("WAGER_REFUND_BO")) {
				UserInfoBean retInfoBean=new UserInfoBean();
				getRetailerInfoFromEngineSaleTxnId(retInfoBean,tpTxRequestBean);
				responseBean = accDao.manageAccountsForBoCancel(retInfoBean, tpTxRequestBean);
			} else if (txType.equals("WAGER_REPRINT")) {
				responseBean = accDao.manageRGForReprint(userInfoBean, tpTxRequestBean);
			}else if(txType.equals("SLE_INVALID_PWT")){
				responseBean=accDao.manageRGForInvalidPWT(userInfoBean, tpTxRequestBean);
			}else{
				if(!"IW".equals(tpTxRequestBean.getServiceCode()))
					throw new SLEException(SLEErrors.CANCEL_INVALID_ERROR_CODE,SLEErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}
		}catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		long endTime = System.currentTimeMillis();
		logger.info("Time Taken in Controller is  {} seconds",((endTime-startTime)/1000));
		return responseBean;
	}
	public void getRetailerInfoFromEngineSaleTxnId(UserInfoBean retInfoBean, TPTxRequestBean tpTxRequestBean){
		Connection con;
		String retQry;
		Statement retstmt;
		ResultSet rs;
		try{
			con=DBConnect.getConnection();
			retstmt=con.createStatement();
			retQry="select  transaction_id,retailer_org_id,parent_id,user_id from st_sle_ret_sale srs inner join st_lms_organization_master lom on srs.retailer_org_id=lom.organization_id  inner join st_lms_user_master lum on lum.organization_id=lom.organization_id   where engine_tx_id="+tpTxRequestBean.getEngineSaleTxId()+" and game_id="+tpTxRequestBean.getGameId()+" and game_type_id="+tpTxRequestBean.getGameTypeId()+"";
			logger.info("retailer Info for Ticket Cancellation"+retQry);
			rs=retstmt.executeQuery(retQry);
			if(rs.next()){
				retInfoBean.setParentOrgId(rs.getInt("parent_id"));
				retInfoBean.setUserOrgId(rs.getInt("retailer_org_id"));
				retInfoBean.setUserId(rs.getInt("user_id"));
				retInfoBean.setUserType("RETAILER");
			}
	}catch (Exception e) {
		e.printStackTrace();
	}
	finally{
		}
	}
	
	public double fetchUserBalance(long userId) throws LMSException {
		double balance = 0.0;
		AccMgmtDao accDao = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			accDao = DaoFactory.getAccMgmtDao();
			balance = accDao.fetchOrgBalance(userId, con);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return balance;
	}
	
}