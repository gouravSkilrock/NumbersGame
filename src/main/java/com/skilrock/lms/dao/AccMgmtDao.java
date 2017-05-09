package com.skilrock.lms.dao;

import java.sql.Connection;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.TPResponseBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;

public interface AccMgmtDao {

	TPResponseBean manageAccountsForSale(UserInfoBean userBean , TPTxRequestBean tpTransactionBean) throws LMSException;
	TPResponseBean manageAccountsForCancel(UserInfoBean userBean , TPTxRequestBean tpTransactionBean) throws LMSException;
	TPResponseBean manageRGForReprint(UserInfoBean userBean , TPTxRequestBean tpTransactionBean) throws LMSException;
	TPResponseBean manageAccountsForBoCancel(UserInfoBean userBean , TPTxRequestBean tpTransactionBean) throws LMSException;
	TPResponseBean manageRGForInvalidPWT(UserInfoBean userBean , TPTxRequestBean tpTransactionBean) throws LMSException;
	public double fetchOrgBalance(long userId, Connection con) throws LMSException;
}