package com.skilrock.lms.dao;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.TPPwtRequestBean;
import com.skilrock.lms.rest.services.bean.TPPwtResponseBean;

public interface WinningMgmtDao {
	TPPwtResponseBean boNormalPay(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException;
	TPPwtResponseBean boHighPrize(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException;

	TPPwtResponseBean agentNormalPay(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException;
	TPPwtResponseBean agentHighPrize(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException;

	TPPwtResponseBean retailerNormalPay(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException;

	TPPwtResponseBean masApprovalInit(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException;
	TPPwtResponseBean masApprovalDone(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException;
}