package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.sql.Timestamp;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.RainbowGameJackpotReportBean;

public class JackpotDetailReportControllerImpl {
	final static Log logger = LogFactory.getLog(JackpotDetailReportControllerImpl.class);

	private static JackpotDetailReportControllerImpl instance;

	private JackpotDetailReportControllerImpl() {
	}

	public static JackpotDetailReportControllerImpl getInstance() {
		if (instance == null) {
			synchronized (JackpotDetailReportControllerImpl.class) {
				if (instance == null) {
					instance = new JackpotDetailReportControllerImpl();
				}
			}
		}
		return instance;
	}

	public RainbowGameJackpotReportBean fetchJackpotData(Timestamp startTime,
			Timestamp endTime, int gameId) throws LMSException {
		ServiceRequest serReq = new ServiceRequest();
 		ServiceResponse serResp = new ServiceResponse();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		RainbowGameJackpotReportBean bean = null;
		try {
			JSONObject requestObject = new JSONObject();
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_JACKPOT_DETAIL_RAINBOW);
			requestObject.put("startDate", String.valueOf(startTime));
			requestObject.put("endDate", String.valueOf(endTime));
			requestObject.put("gameId", gameId);
			serReq.setServiceData(requestObject);
			serResp = delegate.getResponse(serReq);
			if (serResp.getIsSuccess())
				bean = (RainbowGameJackpotReportBean) new Gson().fromJson((JsonElement) serResp.getResponseData(),new TypeToken<RainbowGameJackpotReportBean>() {}.getType());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return bean;
	}
}