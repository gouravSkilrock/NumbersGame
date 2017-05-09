package com.skilrock.lms.instantWin.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.coreEngine.instantWin.common.IWException;
import com.skilrock.lms.instantWin.javaBeans.IWDataFace;
import com.skilrock.lms.instantWin.javaBeans.VerifyTicketRequestBean;

public class NotifyIW extends Thread {
	static Log logger = LogFactory.getLog(NotifyIW.class);

	private int activityCode;
	private IWDataFace requestData;

	public NotifyIW(int activityCode, IWDataFace requestData) {
		this.activityCode = activityCode;
		this.requestData = requestData;
	}

	public IWDataFace asyncCall(NotifyIW notifyData) throws IWException {
		switch (activityCode) {
		case IW.Activity.FETCH_VERIFY_TKT_DATA:
			return IWNotificationManager.fetchVerifyTktData((VerifyTicketRequestBean) requestData);
		case IW.Activity.PAY_WINNING_TKT:
			return IWNotificationManager.claimWinningTkt((VerifyTicketRequestBean) requestData);
		default:
			break;
		}
		return null;
	}

	@Override
	public void run() {
		try {
			switch (activityCode) {
			default:
				break;
			}
		} catch (Exception e) {
			logger.info("Exception Occured - " + e.getMessage());
		}
	}
}