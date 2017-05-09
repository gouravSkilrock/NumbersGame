package com.skilrock.lms.wrapper.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.wrapper.javaBeans.UserRegistrationRequestBean;
import com.skilrock.lms.wrapper.javaBeans.UserRegistrationResponseBean;
import com.skilrock.lms.wrapper.javaBeans.WrapperDataFace;

public class NotifyWrapper extends Thread {
	static Log logger = LogFactory.getLog(NotifyWrapper.class);

	private int activityCode;
	private WrapperDataFace requestData;

	public NotifyWrapper(int activityCode, WrapperDataFace requestData) {
		this.activityCode = activityCode;
		this.requestData = requestData;
	}

	public WrapperDataFace asyncCall(NotifyWrapper notifyData) throws LMSException {
		switch (activityCode) {
			case Wraper.Activity.USER_REGISTER:
				return (UserRegistrationResponseBean) WrapperNotificationManager.userRegistration((UserRegistrationRequestBean) requestData);
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