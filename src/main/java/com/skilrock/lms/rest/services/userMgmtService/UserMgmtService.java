package com.skilrock.lms.rest.services.userMgmtService;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.controller.userMgmtController.UserMgmtController;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgNUserRegHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.UserManagementHelper;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.services.bean.CreateUserResponseBean;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.EditUserRequestBean;
import com.skilrock.lms.rest.services.bean.EditUserResponseBean;
import com.skilrock.lms.rest.services.bean.ResponseDataBean;
import com.skilrock.lms.rest.services.bean.TPResponseBean;
import com.skilrock.lms.rest.services.bean.TpCommonStatusBean;
import com.skilrock.lms.rest.services.bean.UserRegistrationRequest;
import com.skilrock.lms.rest.services.common.TpUtility;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;

@Path("/userMgmt")
public class UserMgmtService {
	Logger logger = LoggerFactory.getLogger(UserMgmtService.class);
	
	private static final String SECURITY_ANSWER = "Cricket";
	private static final String SECURITY_QUESTION = "Which is your fav game?";
	
	UserInfoBean userInfoBean;
	private ServletContext serveletContext;
	private UserRegistrationRequest userRegistrationBean;
	private ScratchDaoImpl scratchdao;
	private CreateUserResponseBean respBean;
	private UserManagementHelper userMgmtHlpr;
	private TpUtility tpUtil;
	
	public UserMgmtService() {
		this.userMgmtHlpr = new UserManagementHelper();
		this.scratchdao = new ScratchDaoImpl();
		this.tpUtil = new TpUtility();
	}
	
	public UserMgmtService(UserManagementHelper userMgmtHlpr,ScratchDaoImpl scratchdao,TpUtility tpUtil){
		this.userMgmtHlpr = userMgmtHlpr;
		this.scratchdao = scratchdao;
		this.tpUtil = tpUtil;
	}

	@Path("/getUserInfo")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserInfo(@Context HttpServletRequest request, String requestData){
		String userName = null;
		TPResponseBean responseBean = null;
		UserDataBean userBean = null;
		try {
			logger.info("LMS reqData - " + requestData);
			logger.info("AUDIT_ID_{}_@@_LMS reqData {}",TransactionManager.getAuditId(), requestData);

			JsonObject data = new JsonParser().parse(requestData).getAsJsonObject();
			userName = data.get("userName").getAsString();
			userBean = UserMgmtController.getInstance().getUserInfo(userName);
			if(userBean == null) {
				throw new SLEException(SLEErrors.SESSION_TIME_OUT_ERROR_CODE , SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
			}

			responseBean = new TPResponseBean(0, "SUCCESS", userBean);
		} catch (SLEException e) {
			if(responseBean == null){
				responseBean = new TPResponseBean();
				responseBean.setResponseCode(e.getErrorCode());
			}
			logger.error(e.getErrorMessage());
		} catch (Exception e) {
			if(responseBean == null) {
				responseBean = new TPResponseBean();
				responseBean.setResponseCode(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			}
			e.printStackTrace();
		}
		Gson gson = new Gson();
		String json = gson.toJson(responseBean);
		logger.info("LMS Response data {}" + json);
		return json;
	}
	
	@Path("/logOutAllRetailers")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String logOutAllRetailers(@Context HttpServletRequest request) {
		try {
			UserMgmtController.getInstance().logOutAllRetailers();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}
	
	@Path("/validateRGLimit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String validateRGLimits(@Context HttpServletRequest request, String requestData){
		TPResponseBean responseBean = null;
		TpCommonStatusBean statusBean = null ;
		try {
			logger.info("AUDIT_ID_{}_@@_LMS reqData {}",TransactionManager.getAuditId(), requestData);

			JsonObject data = new JsonParser().parse(requestData).getAsJsonObject();
			String userName = data.get("userName").getAsString();
			String criteria = data.get("criteria").getAsString();
			String amount = data.get("amount").getAsString();

			statusBean = UserMgmtController.getInstance().validateRGLimit(userName, criteria, amount) ;
			
			responseBean = new TPResponseBean(0, "SUCCESS", statusBean);
		} catch (Exception e) {
			e.printStackTrace();

			if(responseBean == null) {
				responseBean = new TPResponseBean(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE, new TpCommonStatusBean(false, "LIMIT REACHED"));
			}
		}
		String json = new Gson().toJson(responseBean);
		logger.info("LMS Response data - " + json);

		return json;
	}
	
	@Path("/registerUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String createUser(String requestData) {
		// TODO Auto-generated method stub
		JsonObject requestObj = null;
		CreateUserResponseBean respBean = null;
		try{
			
			if (requestData == null || requestData.trim().length() < 1) {
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			
			requestObj = new JsonParser().parse(requestData).getAsJsonObject();
			
			userRegistrationBean = getUserRegistrationBean(requestObj);
			
			validateMandatoryParamsForUserRegistration(userRegistrationBean);
			
			checkForDuplicateTpUserId();
			
			userInfoBean = TpUtility.createParentUserBean(userRegistrationBean.getParentUserId());
			
			checkForRetailerCreditLimit();
		
			respBean = new CreateUserResponseBean();
			respBean = getUserRegistrationResponse(userInfoBean,userRegistrationBean);
	
		}catch(LMSException le){
			respBean = new CreateUserResponseBean();
			respBean.setResponseCode(le.getErrorCode());
			respBean.setResponseMsg(le.getErrorMessage());
		}
		catch (Exception e) {
			respBean = new CreateUserResponseBean();
			respBean.setResponseCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			respBean.setResponseMsg(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			e.printStackTrace();
		}	
		return new Gson().toJson(respBean);
	}

	private void checkForRetailerCreditLimit() throws LMSException {
		String errMsg = CommonMethods.chkCreditLimitAgt(userInfoBean.getUserOrgId(), userRegistrationBean.getCreditLimit());
		if (!"TRUE".equals(errMsg)) {
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,errMsg);
		}
	}

	private void checkForDuplicateTpUserId() throws LMSException {
		
		boolean tpUserIdCheck = TpUtility.checkForDuplicateTpUserId(userRegistrationBean.getTpUserId());
		
		if(!tpUserIdCheck){
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,"TpUserId is duplicate");
		}
	}
	
	
	private UserRegistrationRequest getUserRegistrationBean(JsonObject requestObj) throws LMSException {
		
		serveletContext = LMSUtility.sc;
		userRegistrationBean = new UserRegistrationRequest();
		try {
			validateCreditLimitAndSecurityDeposit(requestObj);
			userRegistrationBean = new Gson().fromJson(requestObj, UserRegistrationRequest.class);
		} catch (JsonSyntaxException e) {
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,LMSErrors.INVALID_DATA_ERROR_MESSAGE);
		}catch(NumberFormatException ne){
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,LMSErrors.INVALID_DATA_ERROR_MESSAGE);
		}
		
		int mappingId[] = tpUtil.getServiceList();
		String statusTable[] = tpUtil.getStatusTableValues();
		
		userRegistrationBean.setAddrLine1(requestObj.get("address") == null?"":requestObj.get("address").getAsString());
		userRegistrationBean.setSecurity(requestObj.get("securityDeposit").getAsDouble());
		userRegistrationBean.setStatusTable(statusTable);
		userRegistrationBean.setId(mappingId);
		userRegistrationBean.setSelfClaim(Utility.getPropertyValue("SELF_CLAIM_RET"));
		userRegistrationBean.setOtherClaim(Utility.getPropertyValue("OTHER_CLAIM_RET"));
		userRegistrationBean.setMinClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_RET")));
		userRegistrationBean.setMaxClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_RET")));
		userRegistrationBean.setBlockAmt(Double.parseDouble(Utility.getPropertyValue("BLOCK_AMT")));
		userRegistrationBean.setBlockDays(Integer.parseInt(Utility.getPropertyValue("BLOCK_DAYS")));
		prepareUserRegistrationBeanFromServletContext();
		prepareUserRegistrationBeanAddManualParams();
		return userRegistrationBean;
	}

	private void validateCreditLimitAndSecurityDeposit(JsonObject requestObj) throws LMSException {
		if(requestObj.get("creditLimit") == null){
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,"Credit Limit Parameter should be Present");
		}
		if(requestObj.get("securityDeposit") == null || !Double.class.isInstance(requestObj.get("securityDeposit").getAsDouble())){
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,"Security Deposit Parameter should be Present");
		}
	}
	
	
	private void prepareUserRegistrationBeanAddManualParams() {	
		userRegistrationBean.setStatusorg("ACTIVE");
		userRegistrationBean.setOrgType("Retailer");
		userRegistrationBean.setBlockAction(Utility.getPropertyValue("BLOCK_ACTION"));
		userRegistrationBean.setStatus("ACTIVE");
		userRegistrationBean.setSecQues(SECURITY_QUESTION);
		userRegistrationBean.setSecAns(SECURITY_ANSWER);
		userRegistrationBean.setRegisterById(userRegistrationBean.getParentUserId());
	}
	
	private void prepareUserRegistrationBeanFromServletContext() {
		userRegistrationBean.setVerLimit(serveletContext.getAttribute("agtVerLimit").toString());
		userRegistrationBean.setAppLimit(serveletContext.getAttribute("agtAppLimit").toString());
		userRegistrationBean.setPayLimit(serveletContext.getAttribute("agtPayLimit").toString());
		userRegistrationBean.setScrapLimit(serveletContext.getAttribute("agtScrapLimit").toString());
		userRegistrationBean.setIsRetailerOnline((String) serveletContext.getAttribute("RET_ONLINE"));
		userRegistrationBean.setMaxPerDayPayLimit(Double.parseDouble((String) serveletContext.getAttribute("MAX_PER_DAY_PAY_LIMIT_FOR_RET")));
	}
	
	public CreateUserResponseBean getUserRegistrationResponse(UserInfoBean userInfoBean,
			UserRegistrationRequest userRegistrationBean) throws LMSException {
		
		Map<String, String> errorMap = null;
		respBean = new CreateUserResponseBean();
		try{
			
			errorMap = OrgNUserRegHelper.createNewRetailerOrgNUser(userInfoBean,userRegistrationBean, "Retailer");
			
			if(errorMap.containsKey("NewPassword") && errorMap.containsKey("orgCode")){
				respBean = setResponseBeanForUserRegistration(userRegistrationBean, errorMap);
			}else{
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
		}catch(LMSException le){
			if(le.getErrorCode()!=null){
				throw le;
			}else{
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return respBean;
	}
		

	private CreateUserResponseBean setResponseBeanForUserRegistration(UserRegistrationRequest userRegistrationBean,
			Map<String, String> errorMap) {
		int refUserId;
		ResponseDataBean rdBean = new ResponseDataBean();
		respBean.setRequestId(userRegistrationBean.getRequestId());
		respBean.setResponseCode(100);
		respBean.setResponseMsg("Success");
		
		refUserId = TpUtility.getUserIdForOrgCode(errorMap.get("orgCode"));
		
		rdBean.setRefUserId(refUserId);
		rdBean.setTpUserId(userRegistrationBean.getTpUserId());
		rdBean.setUserName(userRegistrationBean.getUserName());
		respBean.setRequestData(rdBean);
		
		return respBean;
	}
	
	@Path("/editUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String editUser(@Context HttpServletRequest requestContext, String reqData) {
		
		JsonObject requestObj = null;
		EditUserRequestBean editUserBean = null;
		EditUserResponseBean responseBean = new EditUserResponseBean();
		int userId;
		try{
			
			if (reqData == null || reqData.trim().length() < 1) {
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			
			requestObj = new JsonParser().parse(reqData).getAsJsonObject();
			
			editUserBean = new Gson().fromJson(requestObj, EditUserRequestBean.class);
			
			validateMandatoryParamsForEditUser(editUserBean);
			
			String requestIp = requestContext.getRemoteAddr();

			userId = getUserIdFromTPUserId(editUserBean.getTpUserId());
			
			if(userMgmtHlpr.editUserDetails(userId, Long.parseLong(editUserBean.getMobileNo()),Long.parseLong(editUserBean.getMobileNo()),editUserBean.getEmailId().trim(),editUserBean.getStatus(),11001,"Edit User For API",requestIp)){
				responseBean.setRequestId(editUserBean.getRequestId());
				responseBean.setResponseCode(100);
				responseBean.setTpUserId(editUserBean.getTpUserId());
				responseBean.setResponseData("User Edit Successfully");
			}else{
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			
		}catch(LMSException le){
			if(le.getErrorCode()!=null){
				responseBean.setResponseCode(le.getErrorCode());
				responseBean.setResponseData(le.getErrorMessage());
			}else{
				responseBean.setResponseCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
				responseBean.setResponseData(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}
		catch (Exception e) {
			responseBean.setRequestId(editUserBean.getRequestId());
			responseBean.setTpUserId(editUserBean.getTpUserId());
			responseBean.setResponseCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseBean.setResponseData(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}	
		return new Gson().toJson(responseBean);
	}

	private int getUserIdFromTPUserId(String tpUserId) throws LMSException {
		int userId = 0;
		DaoBean daoBean;
		try {
			daoBean = scratchdao.getUserOrgIdAndUserIdFromTpUserId(tpUserId);
			if(daoBean==null){
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			userId = daoBean.getUserId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userId;
	}

	private void validateMandatoryParamsForUserRegistration(UserRegistrationRequest userRegBean) throws LMSException {
		
		Validator validator = prepareValidator();
		Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(userRegBean);
		if(!violations.isEmpty()){
			ConstraintViolation<UserRegistrationRequest> firstViolation = violations.iterator().next();
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,firstViolation.getMessage());
		}		
	}

	private void validateMandatoryParamsForEditUser(EditUserRequestBean editUserBean) throws LMSException {
		Validator validator = prepareValidator();
		Set<ConstraintViolation<EditUserRequestBean>> violations = validator.validate(editUserBean);
					
		if(!violations.isEmpty()){
			ConstraintViolation<EditUserRequestBean> firstViolation = violations.iterator().next();
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE,firstViolation.getMessage());
		}
	}

	private Validator prepareValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator;
	}
	
}