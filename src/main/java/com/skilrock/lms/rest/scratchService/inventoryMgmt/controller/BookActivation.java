package com.skilrock.lms.rest.scratchService.inventoryMgmt.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import javax.ws.rs.ext.Provider;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BookRecieveRegistrationRetailerHelper;
import com.skilrock.lms.rest.scratchService.BaseController.BaseController;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.beans.GetDlChallanDetailReqBean;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.beans.GetDlChallanRequestDataBean;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.beans.GetDlChallanResponseBean;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.service.BookActivationService;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.serviceImpl.BookActivationServiceImpl;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;

@Provider
@Path("invMgmt")
public class BookActivation extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(BookActivation.class);
	
	private BookActivationService bookActivationService;
	private BookRecieveRegistrationRetailerHelper bookRecieveRegistrationRetailerHelper;
	private ScratchDaoImpl scratchdao;
	
	public BookActivation(){
		this.setBookActivationService(new BookActivationServiceImpl());
		this.scratchdao = new ScratchDaoImpl();
		this.bookRecieveRegistrationRetailerHelper = new BookRecieveRegistrationRetailerHelper();
	}
	
	public BookActivation(BookActivationService bookActivationService){
		this.bookActivationService = bookActivationService;
	}

	public BookActivation(BookActivationService bookActivationService,BookRecieveRegistrationRetailerHelper bookRecieveRegistrationRetailerHelper,ScratchDaoImpl scratchDao){
		this.setBookActivationService(bookActivationService);
		this.bookRecieveRegistrationRetailerHelper = bookRecieveRegistrationRetailerHelper;
		this.scratchdao = scratchDao;
	}
	
	private JsonObject requestObj = null;
	private GetDlChallanDetailReqBean dlChallanBean = null;
	private GetDlChallanResponseBean dlRespBean = null;
	private List<GetDlChallanRequestDataBean> reqDataBean = null;
	private Map<String, List<String>> gameBookMap = null;
	
	@Path("/bookActivate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getBookActivated(String requestData){
		try{
			ScracthMgmtBean bean = getScracthManagementBean(requestData);
			if(isValidRequestForBookActivation(bean)){
				return getResponseForValidBookNumbers(bean);
			}else{
				return new Gson().toJson(getFailureJsonResponse(101, "Mandatory parameteres are not provided"));
			}
		}catch (Exception e) {
			logger.error("Some Internal Error Occured");
			return new Gson().toJson(getFailureJsonResponse(102, "Some Internal Error Occured"));
		}
	}
	
	
	@Path("/soldTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String ticketByTicketSale(String requestData){
		try{
			ScracthMgmtBean bean = getScracthManagementBean(requestData);
			if(isValidRequestForTicketByTicketSale(bean)){
				return markTicketAsSold(bean);
			}else{
				return new Gson().toJson(getFailureJsonResponse(101, "Mandatory parameteres are not provided"));
			}
		}catch (Exception e) {
			logger.error("Some Internal Error Occured");
			return new Gson().toJson(getFailureJsonResponse(102, "Some Internal Error Occured"));
		}
	}
	
	@Path("/unsoldTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String ticketByTicketUnSold(String requestData){
		try{
			ScracthMgmtBean bean = getScracthManagementBean(requestData);
			if(isValidRequestForTicketByTicketSale(bean)){
				return markTicketAsUnSold(bean);
			}else{
				return new Gson().toJson(getFailureJsonResponse(101, "Mandatory parameteres are not provided"));
			}
		}catch (Exception e) {
			logger.error("Some Internal Error Occured");
			return new Gson().toJson(getFailureJsonResponse(102, "Some Internal Error Occured"));
		}
	}

	private String markTicketAsSold(ScracthMgmtBean bean) throws JSONException {
		String response = getBookActivationService().ticketByTicketSale(bean);
		if(!"FAIL".equalsIgnoreCase(response)){
			return generateSuccessResponseForTicketByTicketSale(bean,response);
		}
		return new Gson().toJson(getFailureJsonResponse(1008,"Invalid Ticket"));
	}
	
	private String markTicketAsUnSold(ScracthMgmtBean bean) throws JSONException {
		String response = getBookActivationService().ticketByTicketUnSold(bean);
		if(!"FAIL".equalsIgnoreCase(response)){
			return generateSuccessResponseForTicketByTicketUnSold(bean,response);
		}
		return new Gson().toJson(getFailureJsonResponse(1008,"Invalid Ticket"));
	}
	
	private boolean isValidRequestForBookActivation(ScracthMgmtBean bean) {
		return bean.getBookNumber() != null && !bean.getBookNumber().equals("") 
				&& bean.getTpUserId() != null && !bean.getTpUserId().equals("");
	}

	private boolean isValidRequestForTicketByTicketSale(ScracthMgmtBean bean) {
		return bean.getTicketNumber() != null && !bean.getTicketNumber().equals("") 
				&& null != bean.getTpTransactionId() && !bean.getTpTransactionId().equals("");
	}
	
	private String getResponseForValidBookNumbers(ScracthMgmtBean bean) throws JSONException {
		if (getBookActivationService().isBookNumberValid(bean)) {
			return markBookNumbersReceived(bean);
		} else {
			return new Gson().toJson(getFailureJsonResponse(103, "Book Number is incorrect."));
		}
	}
	
	private String markBookNumbersReceived(ScracthMgmtBean bean) throws JSONException {
		String getStatus = getBookActivationService().updateBookNumberStatus(bean);
		String response = "";
		if ("SUCCESS".equals(getStatus)) {
			response = generateResponseForSuccess(bean);
			return response.toString();
		}
		return new Gson().toJson(getFailureJsonResponse(102,"Some Internal Error Occured"));
	}
	
	private String generateResponseForSuccess(ScracthMgmtBean bean) throws JSONException {
		JSONObject responseObject = new JSONObject();
		responseObject.put("requestId", bean.getRequestId());
		responseObject.put("responseCode", 100);
		JSONObject json = new JSONObject();
		json.put("bookNumber", bean.getBookNumber());
		json.put("status", "ACTIVATED");
		responseObject.put("responseData",json);
		return responseObject.toString();
	}
	
	private String generateSuccessResponseForTicketByTicketSale(ScracthMgmtBean bean,String refTransactionId) throws JSONException {
		JSONObject responseObject = new JSONObject();
		responseObject.put("requestId", bean.getRequestId());
		responseObject.put("responseCode", 100);
		JSONObject json = new JSONObject();
		json.put("ticketNumber", bean.getTicketNumber());
		json.put("status", "MARKED_AS_SOLD");
		json.put("refTransactionId",refTransactionId);
		responseObject.put("responseData",json);
		return responseObject.toString();
	}
	
	private String generateSuccessResponseForTicketByTicketUnSold(ScracthMgmtBean bean,String refTransactionId) throws JSONException {
		JSONObject responseObject = new JSONObject();
		responseObject.put("requestId", bean.getRequestId());
		responseObject.put("responseCode", 100);
		JSONObject json = new JSONObject();
		json.put("ticketNumber", bean.getTicketNumber());
		json.put("status", "MARKED_AS_UNSOLD");
		json.put("refTransactionId",refTransactionId);
		responseObject.put("responseData",json);
		return responseObject.toString();
	}
	
	public BookActivationService getBookActivationService() {
		return bookActivationService;
	}
	
	public void setBookActivationService(BookActivationService bookActivationService) {
		this.bookActivationService = bookActivationService;
	}
	
	@Path("/getDlDetails")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getDlChallanDetail(String reqData) {
		dlRespBean = new GetDlChallanResponseBean();
		try {
			validateRequestData(reqData);
			requestObj = new JsonParser().parse(reqData).getAsJsonObject();
			dlChallanBean = new Gson().fromJson(requestObj, GetDlChallanDetailReqBean.class);
			validateMandatoryParamsOfDlDetailRequest();
			int orgId = getOrgIdFromTpUserId();
			if(orgId == 0){
				setResponseForInvalidOrdId();
			}else{
				getGameBookMapForDlChallan(orgId);
			}
		} catch (LMSException e) {
			setRespCodeOfLmsExceptionForDlDetails(e);
		} catch (Exception e) {
			setRespCodeOfGeneralExceptionForDlDetails(e);
		}
		return new Gson().toJson(dlRespBean);
	}

	private void setResponseForInvalidOrdId() {
		dlRespBean.setRequestId(dlChallanBean.getRequestId());
		dlRespBean.setResponseCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		dlRespBean.setResponseMsg("Invalid TpUserId for the request");
	}

	private void getGameBookMapForDlChallan(int orgId) {
		gameBookMap = bookRecieveRegistrationRetailerHelper.getBooks(orgId, dlChallanBean.getDlNumber());

		if (gameBookMap.size() > 0) {
			prepareDlRespBeanForSuccess();
		} else {
			prepareDlRespBeanForInvalidChallanId();
		}
	}

	private int getOrgIdFromTpUserId() throws LMSException {
		int orgId = 0;
		DaoBean daoBean;
		try {
			daoBean = scratchdao.getUserOrgIdAndUserIdFromTpUserId(dlChallanBean.getTpUserId());
			if(daoBean==null){
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			orgId = (Integer) daoBean.getUserOrgId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orgId;
	}

	private void validateRequestData(String reqData) throws LMSException {
		if (reqData == null || reqData.trim().length() < 1) {
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
		}
	}

	private void prepareDlRespBeanForSuccess() {
		reqDataBean = new ArrayList<GetDlChallanRequestDataBean>();
		for (Entry<String, List<String>> str : gameBookMap.entrySet()) {
			prepareRequestDataBeanListForDlDetails(str);
		}
		prepareDlRespBeanForGameMap();
	}

	private void validateMandatoryParamsOfDlDetailRequest() throws LMSException {
		Validator validator = prepareValidator();
		Set<ConstraintViolation<GetDlChallanDetailReqBean>> violations = validator.validate(dlChallanBean);
		if (!violations.isEmpty()) {
			ConstraintViolation<GetDlChallanDetailReqBean> firstViolation = violations.iterator().next();
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,firstViolation.getMessage());
		}
	}

	private void setRespCodeOfGeneralExceptionForDlDetails(Exception e) {
		logger.error("Exception occurred", e);
		dlRespBean.setResponseCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		dlRespBean.setResponseMsg(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}

	private void setRespCodeOfLmsExceptionForDlDetails(LMSException e) {
		logger.error("Exception occurred", e);
		dlRespBean.setResponseCode(e.getErrorCode());
		dlRespBean.setResponseMsg(e.getErrorMessage());
	}

	private void prepareRequestDataBeanListForDlDetails(Entry<String, List<String>> str) {
		GetDlChallanRequestDataBean rdBean = new GetDlChallanRequestDataBean();
		String gameName = str.getKey().split("-")[2];
		List<String> bookList = str.getValue();
		rdBean.setBookList(bookList);
		rdBean.setGameName(gameName);
		reqDataBean.add(rdBean);
	}

	private void prepareDlRespBeanForInvalidChallanId() {
		dlRespBean.setRequestId(dlChallanBean.getRequestId());
		dlRespBean.setResponseCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		dlRespBean.setResponseMsg("Invalid Challan Id For InTransit Items");
	}

	private void prepareDlRespBeanForGameMap() {
		dlRespBean.setRequestData(reqDataBean);
		dlRespBean.setRequestId(dlChallanBean.getRequestId());
		dlRespBean.setResponseCode(100);
	}

	private Validator prepareValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator;
	}
	
}
