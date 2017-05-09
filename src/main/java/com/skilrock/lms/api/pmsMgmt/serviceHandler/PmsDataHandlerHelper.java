package com.skilrock.lms.api.pmsMgmt.serviceHandler;

import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.serviceImpl.ScratchGameDataServiceImpl;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.serviceImpl.ScratchTicketVerifyServiceImpl;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsCityDataBean;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsStateDataBean;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsUserDataBean;
import com.skilrock.lms.coreEngine.userMgmt.serviceImpl.LmsCityDataServiceImpl;
import com.skilrock.lms.coreEngine.userMgmt.serviceImpl.LmsStateDateServiceImpl;
import com.skilrock.lms.coreEngine.userMgmt.serviceImpl.LmsUserDataServiceImpl;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

public class PmsDataHandlerHelper {
	final static Log logger = LogFactory.getLog(PmsDataHandlerHelper.class);

	public JSONObject getLmsRetailerJson(JSONRPC2Request req) {
		logger
				.info("*****inside getLmsRetailerJson method of PmsDataHandlerHelper class");
		JSONObject jsonObject = new JSONObject();
		JSONObject reqJsonObject = null;
		List<LmsUserDataBean> userList = null;
		LmsUserDataServiceImpl lmsUserDataServiceImpl = null;
		try {
			reqJsonObject = (JSONObject) JSONSerializer.toJSON(req.getParams());
			lmsUserDataServiceImpl = new LmsUserDataServiceImpl();
			userList = lmsUserDataServiceImpl
					.getUserInfoData(reqJsonObject.getString("cityCode"),
							reqJsonObject.getString("userType"));
			jsonObject.put("userList", userList);
			jsonObject.put("isSuccess", true);
			jsonObject.put("errorMsg", "");
		} catch (LMSException pe) {
			pe.printStackTrace();
			jsonObject.put("errorCode", pe.getErrorCode());
			jsonObject.put("errorMsg", pe.getErrorMessage());
			jsonObject.put("isSuccess", false);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg",
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode", LMSErrors.COMPILE_TIME_ERROR_CODE);
				jsonObject
						.put("errorMsg", LMSErrors.COMPILE_TIME_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
		}
		logger.info("Json Response for getLmsRetailerJson is " + jsonObject);
		return jsonObject;
	}

	public JSONObject getScratchGameJson(JSONRPC2Request req) {
		logger
				.info("*****inside getScratchGameJson method of PmsDataHandlerHelper class");
		List<ScratchGameDataBean> scratchGameList = null;
		ScratchGameDataServiceImpl scratchGameDataServiceImpl = null;
		JSONObject jsonObject = new JSONObject();
		try {
			scratchGameDataServiceImpl = new ScratchGameDataServiceImpl();
			scratchGameList = scratchGameDataServiceImpl.getScratchGameList();

			jsonObject.put("scratchGameList", scratchGameList);
			jsonObject.put("isSuccess", true);
			jsonObject.put("errorMsg", "");
		} catch (LMSException pe) {
			pe.printStackTrace();
			jsonObject.put("errorCode", pe.getErrorCode());
			jsonObject.put("errorMsg", pe.getErrorMessage());
			jsonObject.put("isSuccess", false);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg",
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode", LMSErrors.COMPILE_TIME_ERROR_CODE);
				jsonObject
						.put("errorMsg", LMSErrors.COMPILE_TIME_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
		}
		logger.info("Json Response for getScratchGameJson is " + jsonObject);
		return jsonObject;
	}

	public JSONObject getLmsStateDataJson(JSONRPC2Request req) {
		logger
				.info("*****inside getLmsStateDataJson method of PmsDataHandlerHelper class");
		JSONObject jsonObject = new JSONObject();
		List<LmsStateDataBean> stateDataList = null;
		LmsStateDateServiceImpl lmsStateDateServiceImpl = null;
		try {
			lmsStateDateServiceImpl = new LmsStateDateServiceImpl();
			stateDataList = lmsStateDateServiceImpl.getLmsStateData();

			jsonObject.put("stateList", stateDataList);
			jsonObject.put("isSuccess", true);
			jsonObject.put("errorMsg", "");
		} catch (LMSException pe) {
			pe.printStackTrace();
			jsonObject.put("errorCode", pe.getErrorCode());
			jsonObject.put("errorMsg", pe.getErrorMessage());
			jsonObject.put("isSuccess", false);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg",
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode", LMSErrors.COMPILE_TIME_ERROR_CODE);
				jsonObject
						.put("errorMsg", LMSErrors.COMPILE_TIME_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
		}
		logger.info("Json Response for getLmsStateDataJson is " + jsonObject);
		return jsonObject;
	}

	public JSONObject getLmsCityDataJson(String key,
			List<LmsCityDataBean> stateList) {
		logger
				.info("*****inside getLmsCityDataJson method of PmsDataHandlerHelper class");
		JSONObject cityDataJson = new JSONObject();

		cityDataJson.put(key, stateList);
		cityDataJson.put("isSuccess", true);
		cityDataJson.put("errorMsg", "");
		logger.info("Json Response for getLmsCityDataJson is " + cityDataJson);
		return cityDataJson;
	}

	public JSONObject getLmsCityDataJson(JSONRPC2Request req) {
		logger
				.info("*****inside getLmsCityDataJson method of PmsDataHandlerHelper class");
		JSONObject jsonObject = new JSONObject();
		JSONObject reqJsonObject = null;
		List<LmsCityDataBean> cityDataList = null;
		LmsCityDataServiceImpl lmsCityDataServiceImpl = null;
		try {
			reqJsonObject = (JSONObject) JSONSerializer.toJSON(req.getParams());
			lmsCityDataServiceImpl = new LmsCityDataServiceImpl();
			cityDataList = lmsCityDataServiceImpl.getLmsCityData(reqJsonObject
					.getString("stateCode"));

			jsonObject.put("cityList", cityDataList);
			jsonObject.put("isSuccess", true);
			jsonObject.put("errorMsg", "");
		} catch (LMSException pe) {
			pe.printStackTrace();
			jsonObject.put("errorCode", pe.getErrorCode());
			jsonObject.put("errorMsg", pe.getErrorMessage());
			jsonObject.put("isSuccess", false);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg",
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode", LMSErrors.COMPILE_TIME_ERROR_CODE);
				jsonObject
						.put("errorMsg", LMSErrors.COMPILE_TIME_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
		}
		logger.info("Json Response for getLmsCityDataJson is " + jsonObject);
		return jsonObject;
	}

	public JSONObject validateScratchTicket(JSONRPC2Request req) {
		logger
				.info("*****inside validateScratchTicket method of PmsDataHandlerHelper class");
		JSONObject jsonObject = new JSONObject();
		JSONObject reqJsonObject = null;
		ScratchTicketVerifyServiceImpl scratchTicketVerifyServiceImpl = null;
		try {
			reqJsonObject = (JSONObject) JSONSerializer.toJSON(req.getParams());
			scratchTicketVerifyServiceImpl = new ScratchTicketVerifyServiceImpl();
			scratchTicketVerifyServiceImpl
					.verifyScratchTicket(reqJsonObject.getString("ticketNbr"),
							reqJsonObject.getString("virnNbr"));

			jsonObject.put("isSuccess", true);
			jsonObject.put("errorMsg", "");
		} catch (LMSException pe) {
			pe.printStackTrace();
			jsonObject.put("errorCode", pe.getErrorCode());
			jsonObject.put("errorMsg", pe.getErrorMessage());
			jsonObject.put("isSuccess", false);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg",
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode", LMSErrors.COMPILE_TIME_ERROR_CODE);
				jsonObject
						.put("errorMsg", LMSErrors.COMPILE_TIME_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
		}
		logger.info("Json Response for validateScratchTicket is " + jsonObject);
		return jsonObject;
	}
}
