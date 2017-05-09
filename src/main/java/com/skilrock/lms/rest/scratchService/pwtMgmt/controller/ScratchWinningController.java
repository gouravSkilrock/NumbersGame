package com.skilrock.lms.rest.scratchService.pwtMgmt.controller;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.rest.scratchService.orderMgmt.service.ScratchService;
import com.skilrock.lms.rest.scratchService.orderMgmt.serviceImpl.ScratchServiceImpl;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchWinningDataBean;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchWinningPaymentBean;
import com.skilrock.lms.rest.scratchService.pwtMgmt.serviceImpl.ScratchWinningServiceImpl;
import com.skilrock.lms.rest.services.bean.ScratchWinningPaymentRequest;

import net.sf.json.JSONObject;

@Path("/scratch/winning")
public class ScratchWinningController {
	private static final String WIN = "WIN";
	private static Logger logger = LoggerFactory.getLogger(ScratchWinningController.class);
	private ScratchWinningPaymentRequest scratchWinningPaymentRequest;
	private ScratchService scratchService;
	private ScratchWinningServiceImpl scratchWinService;
	ScratchWinningPaymentBean scratchWinningPaymentBean;
	private JSONObject responseJson;
	
	public ScratchWinningController(ScratchService scratchService, ScratchWinningServiceImpl scratchWinService) {
		this.scratchService = scratchService;
		this.scratchWinService = scratchWinService;
	}

	public ScratchWinningController() {
		scratchService = new ScratchServiceImpl();
		scratchWinService = new ScratchWinningServiceImpl();
	}

	@Path("/verification")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public String verifyWinning(String requestData) {
		JsonObject reqData;
		try {
			responseJson = new JSONObject();
			if (requestData == null || requestData.trim().isEmpty()) {
				throw new LMSException(ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			} else {
				reqData = new JsonParser().parse(requestData).getAsJsonObject();
			}
			if (reqData.get("ticketNumber") == null || reqData.get("ticketNumber").getAsString().trim().isEmpty()) {
				throw new LMSException(ScratchErrors.TICKET_NUMBER_NOT_PROVIDED_ERROR_CODE,
						ScratchErrors.TICKET_NUMBER_NOT_PROVIDED_MESSAGE);
			}
			if (reqData.get("virnNumber") == null || reqData.get("virnNumber").getAsString().trim().isEmpty()) {
				throw new LMSException(ScratchErrors.VIRN_NUMBER_NOT_PROVIDED_ERROR_CODE,
						ScratchErrors.VIRN_NUMBER_NOT_PROVIDED_MESSAGE);
			}
			if (reqData.get("tpUserId") == null || reqData.get("tpUserId").getAsString().trim().isEmpty()) {
				throw new LMSException(ScratchErrors.USER_ID_NOT_PROVIDED_ERROR_CODE,
						ScratchErrors.USER_ID_NOT_PROVIDED_MESSAGE);
			}
			if (reqData.get("requestId") == null || reqData.get("requestId").getAsString().trim().isEmpty()) {
				throw new LMSException(ScratchErrors.REQUEST_ID_NOT_PROVIDED_ERROR_CODE,
						ScratchErrors.REQUEST_ID_NOT_PROVIDED_MESSAGE);
			}
			UserInfoBean userInfoBean = scratchService.getUserBeanFromTPUserId(reqData.get("tpUserId").getAsString().trim());
			if (userInfoBean == null) {
				throw new LMSException(ScratchErrors.INVALID_USER_ERROR_CODE, ScratchErrors.INVALID_USER_ERROR_MESSAGE);
			}

			ScratchWinningDataBean winningDataBean = scratchWinService.verifyTicketAndVirn(
					reqData.get("ticketNumber").getAsString().trim(), reqData.get("virnNumber").getAsString().trim(),
					userInfoBean);
			responseJson.put("responseCode", 0);
			responseJson.put("responseMsg", "SUCCESS");
			responseJson.put("responseData", winningDataBean);

		} catch (LMSException e) {
			responseJson.put("responseCode", e.getErrorCode());
			responseJson.put("responseMsg", e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.put("responseCode", LMSErrors.GENERAL_EXCEPTION_CODE);
			responseJson.put("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return new Gson().toJson(responseJson);

	}

	@Path("/payment")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String verifyAndPayWinning(String requestData) {
		try {
			responseJson = new JSONObject();
			if (requestData == null || requestData.trim().isEmpty()) {
				throw new LMSException(ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}else{
				scratchWinningPaymentRequest = getScratchWinningPaymentRequest(requestData);
			}
			scratchWinningPaymentBean = new ScratchWinningPaymentBean();
			validateRequest();
			UserInfoBean userInfoBean = scratchService.getUserBeanFromTPUserId(scratchWinningPaymentRequest.getTpUserId().trim());
			if (userInfoBean == null) {
				prepareFailureResponse(ScratchErrors.INVALID_USER_ERROR_CODE, ScratchErrors.INVALID_USER_ERROR_MESSAGE);
				return new Gson().toJson(responseJson);
			}
			userInfoBean.setParentOrgId(scratchService.getParentOrgId(userInfoBean.getParentUserId()));
			ScratchWinningDataBean winningDataBean = scratchWinService.verifyTicketAndVirn(scratchWinningPaymentRequest.getTicketNumber().trim(),scratchWinningPaymentRequest.getVirnNumber().trim(), userInfoBean);
			scratchWinningPaymentBean.setTicketNumber(scratchWinningPaymentRequest.getTicketNumber());
			scratchWinningPaymentBean.setVirnNumber(scratchWinningPaymentRequest.getVirnNumber());
			if("UNCLAIMED".equalsIgnoreCase(winningDataBean.getStatus()) && winningDataBean.getWinningAmount() > 0.0){
				scratchWinService.payPWTProcess(userInfoBean,scratchWinningPaymentRequest,winningDataBean);
				scratchWinningPaymentBean.setWinAmt(winningDataBean.getWinningAmount());
				scratchWinningPaymentBean.setWinStatus(WIN);
			}else{
				scratchWinningPaymentBean.setWinStatus(winningDataBean.getStatus());
			}
			prepareSuccessResponse(winningDataBean);
		} catch (LMSException l) {
			logger.error("Exception Occurred:", l);
			prepareFailureResponse(l.getErrorCode(), l.getErrorMessage());
		} catch (Exception e) {
			logger.error("Exception Occurred:", e);
			prepareFailureResponse(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return new Gson().toJson(responseJson);
	}

	private void prepareSuccessResponse(ScratchWinningDataBean winningDataBean) {
		responseJson.put("responseCode", 100);
		responseJson.put("responseMsg", "SUCCESS");
		responseJson.put("responseData", scratchWinningPaymentBean);
	}

	private void prepareFailureResponse(int errorCode, String errorMessage) {
		responseJson.put("responseCode", errorCode);
		responseJson.put("responseMsg", errorMessage);
	}

	private ScratchWinningPaymentRequest getScratchWinningPaymentRequest(String requestData) {
		JsonObject requestJson = new JsonParser().parse(requestData).getAsJsonObject();
		return new Gson().fromJson(requestJson, ScratchWinningPaymentRequest.class);
	}

	private void validateRequest() throws LMSException {
		Validator validator = prepareValidator();
		Set<ConstraintViolation<ScratchWinningPaymentRequest>> violations = validator
				.validate(scratchWinningPaymentRequest);
		if (!violations.isEmpty()) {
			ConstraintViolation<ScratchWinningPaymentRequest> constraintViolations = violations.iterator().next();
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, constraintViolations.getMessage());
		}
	}

	private Validator prepareValidator() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		return validator;
	}

}