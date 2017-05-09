package com.skilrock.lms.instantWin.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.coreEngine.instantWin.common.IWException;
import com.skilrock.lms.coreEngine.service.ServiceDelegateIW;
import com.skilrock.lms.coreEngine.service.instantWin.ServiceNameMethod;
import com.skilrock.lms.instantWin.javaBeans.IWDataFace;
import com.skilrock.lms.instantWin.javaBeans.VerifyTicketRequestBean;
import com.skilrock.lms.instantWin.javaBeans.VerifyTicketResponseBean;

class IWNotificationManager {
	private static Log logger = LogFactory.getLog(NotifyIW.class);

	private static ServiceRequest sReq = null;

	static {
		sReq = new ServiceRequest();
	}

	private IWNotificationManager() {
	}

	static IWDataFace fetchVerifyTktData(VerifyTicketRequestBean verifyTicketBean) throws IWException {
		String responseString = null;
		VerifyTicketResponseBean verifyTicketResponseBean = null;
		logger.info("*****Inside fetchVerifyTktData Method of IWNotificationManager*****");
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.DATA_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.FETCH_VERIFY_TKT_DATA);
			sReq.setServiceData(verifyTicketBean);
			responseString = ServiceDelegateIW.getInstance().getResponseString(sReq);

			if (responseString == null) {
				throw new IWException(IW.Errors.GENERAL_EXCEPTION_CODE, IW.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			verifyTicketResponseBean = new Gson().fromJson(responseString, new TypeToken<VerifyTicketResponseBean>() {}.getType());
		} catch (Exception ex) {
			throw new IWException(IW.Errors.GENERAL_EXCEPTION_CODE, IW.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return verifyTicketResponseBean;
	}
	
	static IWDataFace claimWinningTkt(VerifyTicketRequestBean verifyTicketBean) throws IWException {
		String responseString = null;
		VerifyTicketResponseBean verifyTicketResponseBean = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.DATA_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.PAY_WINNING_TKT);
			sReq.setServiceData(verifyTicketBean);
			responseString = ServiceDelegateIW.getInstance().getResponseString(sReq);

			if (responseString == null) {
				throw new IWException(IW.Errors.GENERAL_EXCEPTION_CODE, IW.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			verifyTicketResponseBean = new Gson().fromJson(responseString, new TypeToken<VerifyTicketResponseBean>() {}.getType());
		} catch (Exception ex) {
			throw new IWException(IW.Errors.GENERAL_EXCEPTION_CODE, IW.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return verifyTicketResponseBean;
	}

}