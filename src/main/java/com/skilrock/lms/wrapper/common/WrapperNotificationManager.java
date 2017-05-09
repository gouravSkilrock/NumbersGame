package com.skilrock.lms.wrapper.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.ServiceDelegateWrapper;
import com.skilrock.lms.coreEngine.service.sle.ServiceNameMethod;
import com.skilrock.lms.wrapper.javaBeans.UserRegistrationResponseBean;
import com.skilrock.lms.wrapper.javaBeans.WrapperDataFace;

class WrapperNotificationManager {
	private static Log logger = LogFactory.getLog(NotifyWrapper.class);

	private static ServiceRequest sReq = null;

	static {
		sReq = new ServiceRequest();
	}

	private WrapperNotificationManager()
	{
	}

	static UserRegistrationResponseBean userRegistration(WrapperDataFace dataBean) throws LMSException {
		String responseString = null;
		UserRegistrationResponseBean userRegistrationResponseBean = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.WRAPPER_USER_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.WRAPPER_USER_REGISTER);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateWrapper.getInstance().getResponseString(sReq);
			logger.info(responseString);

			userRegistrationResponseBean = (UserRegistrationResponseBean) new Gson().fromJson(responseString, UserRegistrationResponseBean.class);

		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return userRegistrationResponseBean;
	}

}