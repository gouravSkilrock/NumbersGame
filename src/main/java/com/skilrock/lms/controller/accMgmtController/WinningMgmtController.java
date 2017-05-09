package com.skilrock.lms.controller.accMgmtController;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.TPRequestBean;
import com.skilrock.lms.rest.services.bean.TPResponseBean;

public interface WinningMgmtController {
	String checkTicketPWTStatus(double winningAmount) throws LMSException;
	String checkRetailerClaimStatus(double winningAmount, UserInfoBean userBean) throws LMSException;
	boolean checkPayoutLimits(String ticketNumber, UserInfoBean userBean, double winningAmount) throws LMSException;
	TPResponseBean manageWinning(UserInfoBean userBean, TPRequestBean requestBean) throws LMSException;
}