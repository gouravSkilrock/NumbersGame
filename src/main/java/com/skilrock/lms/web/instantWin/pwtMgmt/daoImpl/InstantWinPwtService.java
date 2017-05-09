package com.skilrock.lms.web.instantWin.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.text.SimpleDateFormat;

import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.controllerImpl.WinningMgmtControllerImplIW;
import com.skilrock.lms.coreEngine.instantWin.common.IWErrors;
import com.skilrock.lms.coreEngine.instantWin.common.IWException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PlayerVerifyHelperForApp;
import com.skilrock.lms.instantWin.common.IW;
import com.skilrock.lms.instantWin.common.NotifyIW;
import com.skilrock.lms.instantWin.javaBeans.VerifyTicketRequestBean;
import com.skilrock.lms.instantWin.javaBeans.VerifyTicketResponseBean;

public class InstantWinPwtService {
	public VerifyTicketResponseBean fetchVerifyTicketData(String tktNbr, UserInfoBean userInfoBean) throws IWException {
		VerifyTicketRequestBean verifyTicketRequestBean = new VerifyTicketRequestBean();
		VerifyTicketResponseBean verifyTicketResponseBean = null;
		try {
			verifyTicketRequestBean.setTicketNbr(tktNbr);
//			verifyTicketRequestBean.setUserType(userType);
			NotifyIW notifyIw = new NotifyIW(IW.Activity.FETCH_VERIFY_TKT_DATA, verifyTicketRequestBean);

//			verifyTicketResponseBean = new VerifyTicketResponseBean();
//			verifyTicketResponseBean.setClaimTime("2015-12-01 10:58:23");
//			verifyTicketResponseBean.setPaymentTime("2015-12-01 10:58:25");
//			verifyTicketResponseBean.setPurchasedFrom("TERMINAL");
//			verifyTicketResponseBean.setPurchaseTime("2015-11-01 08:58:23");
//			verifyTicketResponseBean.setTktData("Match Three");
//			verifyTicketResponseBean.setTktNbr("47586936251417283900");
//			verifyTicketResponseBean.setTktStatus("CLAIM ALLOW");
//			verifyTicketResponseBean.setPaymentAllowed(true);
//			verifyTicketResponseBean.setWinningAmt((double) 100.00);
//			verifyTicketResponseBean.setErrorCode(0);

//			verifyTicketResponseBean.setPaymentAllowed(false);
//			if ("CLAIM ALLOW".equals(verifyTicketResponseBean.getTktStatus()) && IW.Status.NORMAL_PAY.equals(WinningMgmtControllerImplIW.getInstance().checkTicketPWTStatus(verifyTicketResponseBean.getWinningAmt()))) {
//				verifyTicketResponseBean.setPaymentAllowed(true);
//			}
//
//			System.out.println(verifyTicketResponseBean);

			verifyTicketResponseBean = (VerifyTicketResponseBean) notifyIw.asyncCall(notifyIw);

			if (verifyTicketResponseBean.getErrorCode() == 0) {
				if("CLAIM ALLOW".equals(verifyTicketResponseBean.getTktStatus())) {
					if (IW.Status.NORMAL_PAY.equals(WinningMgmtControllerImplIW.getInstance().checkTicketPWTStatus(verifyTicketResponseBean.getWinningAmt()))) {
						boolean isValid = true;
						if ("AGENT".equals(userInfoBean.getUserType())) {
							if (!WinningMgmtControllerImplIW.getInstance().checkPayoutLimits(verifyTicketResponseBean.getTktTxnId(), userInfoBean, verifyTicketResponseBean.getWinningAmt())) {
								isValid = false;
							}
						}
						if(isValid) {
							verifyTicketResponseBean.setPurchaseTime((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(verifyTicketResponseBean.getPurchaseTime()))));
							verifyTicketResponseBean.setPaymentAllowed(true);
							verifyTicketResponseBean.setPlayerReg(false);
						} else {
							verifyTicketResponseBean.setPlayerReg(false);
							verifyTicketResponseBean.setErrorCode(-1);
							verifyTicketResponseBean.setErrorMsg("You are not Authorized To Claim !!!");
						}
					} else {
						if("AGENT".equals(userInfoBean.getUserType())) {
							verifyTicketResponseBean.setPlayerReg(false);
							verifyTicketResponseBean.setErrorCode(-1);
							verifyTicketResponseBean.setErrorMsg("Ticket Can't be claimed At Agent End, Please Contact to BO");
						} else if("BO".equals(userInfoBean.getUserType())) {
							verifyTicketResponseBean.setPlayerReg(true);
						}
					}
				}
			}
		} catch (IWException e) {
			throw e;
		} catch (Exception e) {
			throw new IWException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, IWErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return verifyTicketResponseBean;
	}

	public VerifyTicketResponseBean payPwtTicket(String tktNbr, int playerId, UserInfoBean userInfoBean) throws IWException {
		VerifyTicketRequestBean verifyTicketRequestBean = new VerifyTicketRequestBean();
		VerifyTicketResponseBean verifyTicketResponseBean = null;
		try {
			verifyTicketRequestBean.setTicketNbr(tktNbr);
			verifyTicketRequestBean.setUserType(userInfoBean.getUserType());
			verifyTicketRequestBean.setUserName(userInfoBean.getUserName());
			verifyTicketRequestBean.setMerchantSessionId(userInfoBean.getUserSession());
			verifyTicketRequestBean.setLmsPlayerId(String.valueOf(playerId));
			if(playerId == 0) {
				verifyTicketRequestBean.setWinType(IW.Status.NORMAL_PAY);
			} else {
				verifyTicketRequestBean.setWinType(IW.Status.HIGH_PRIZE);
			}
			NotifyIW notifyIw = new NotifyIW(IW.Activity.PAY_WINNING_TKT, verifyTicketRequestBean);
			verifyTicketResponseBean = (VerifyTicketResponseBean) notifyIw.asyncCall(notifyIw);

//			 verifyTicketResponseBean = new VerifyTicketResponseBean();
//			 verifyTicketResponseBean.setErrorCode(0);
//			 verifyTicketResponseBean.setWinningAmt(100);

		} catch (IWException e) {
			throw e;
		} catch (Exception e) {
			throw new IWException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, IWErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return verifyTicketResponseBean;
	}

	public int registerPlayer(PlayerBean playerBean) throws IWException {
		Connection con = null;
		int playerId = -1;
		try {
			con = DBConnect.getConnection();
			playerId = new PlayerVerifyHelperForApp().registerPlayer(playerBean, con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IWException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, IWErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return playerId;
	}

}
