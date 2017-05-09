package com.skilrock.lms.rest.services.accMgmtService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.controller.accMgmtController.AccountMgmtController;
import com.skilrock.lms.controller.accMgmtController.WinningMgmtController;
import com.skilrock.lms.controllerImpl.WinningMgmtControllerImplIW;
import com.skilrock.lms.controllerImpl.WinningMgmtControllerImplSLE;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.instantWin.common.IW;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.rest.services.bean.TPRequestBean;
import com.skilrock.lms.rest.services.bean.TPResponseBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;
import com.skilrock.lms.rest.services.common.ReqResParser;
import com.skilrock.lms.rest.services.common.TpUtility;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;
import com.skilrock.lms.web.drawGames.common.Util;

@Path("/accMgmt")
public class AccountMgmtService extends BaseAction {
	
	Logger log = LoggerFactory.getLogger(AccountMgmtService.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountMgmtService() {
		super(AccountMgmtService.class.getName());
	}
	
	@Path("/manageAccounts")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String manageAccounts(@Context HttpServletRequest request , String reqData){
		
		TPResponseBean responseBean = null;
		try{
			log.info("LMS reqData {}" + reqData);
			log.info("AUDIT_ID_{}_@@_LMS reqData {}",TransactionManager.getAuditId(), reqData);
			ReqResParser parSer = ReqResParser.getInstance();
			UserInfoBean userInfoBean = null;
			TPTxRequestBean tpTransactionBean = parSer.fetchReqForTx(reqData);
			try{
				userInfoBean = getUserBean(tpTransactionBean.getUserName());
			}catch(Exception e){
				if ("IW".equals(tpTransactionBean.getServiceCode()))
					throw new LMSException(01, SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
				else
					throw new LMSException(SLEErrors.SESSION_TIME_OUT_ERROR_CODE, SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
			}
			TpUtility.validateSessions(userInfoBean , tpTransactionBean.getSessionId(), tpTransactionBean.getServiceCode());
			if (tpTransactionBean.getTokenId() != null) {
				if (!TpEBetMgmtDaoImpl.getInstance().isBetSlipActive(tpTransactionBean.getTokenId())) {
					throw new LMSException(LMSErrors.BET_SLIP_EXPIRED_ERROR_CODE, LMSErrors.BET_SLIP_EXPIRED_ERROR_MESSAGE);
				}
			}
			
			responseBean = AccountMgmtController.getInstance().manageAccounts(userInfoBean , tpTransactionBean);
		}catch (SLEException e) {
			if(responseBean == null){
				responseBean = new TPResponseBean();
				responseBean.setResponseCode(e.getErrorCode());
				responseBean.setResponseMessage(e.getErrorMessage());
			}
			log.error(e.getErrorMessage());
		}catch (LMSException e) {
			if(responseBean == null){
				responseBean = new TPResponseBean();
				responseBean.setResponseCode(e.getErrorCode());
				responseBean.setResponseMessage(e.getErrorMessage());
			}
			log.error(e.getErrorMessage());
		}catch (Exception e) {
			if(responseBean == null){
				responseBean = new TPResponseBean();
				responseBean.setResponseCode(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				responseBean.setResponseMessage(SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			e.printStackTrace();
		}
		Gson gson = new Gson();
		String json = gson.toJson(responseBean);
		logger.info("LMS Response data {}" + json);
		return json;
	}

	@Path("/checkTicketPWTStatus")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String checkTicketPWTStatus(@Context HttpServletRequest request, String requestData) throws LMSException {
		log.info("AUDIT_ID-{} : LMS Request Data-{}", TransactionManager.getAuditId(), requestData);

		UserInfoBean userBean = null;
		JsonObject responseObject = new JsonObject();
		try {
			JsonObject requestObject = (JsonObject) new JsonParser().parse(requestData);
			String userName = requestObject.get("userName").getAsString();
			String userSession = requestObject.get("userSession").getAsString();
			String ticketNumber = null;
			double winningAmount = requestObject.get("winningAmount").getAsDouble();
			String serviceCode = requestObject.get("serviceCode").getAsString();

			userBean = getUserBean(userName);
			TpUtility.validateSessions(userBean, userSession, serviceCode);

			WinningMgmtController winController = null;
			if ("SLE".equals(serviceCode)) {
				winController = WinningMgmtControllerImplSLE.getInstance();
			} else if ("IW".equals(serviceCode)) {
				ticketNumber = requestObject.get("ticketNumber").getAsString();
				winController = WinningMgmtControllerImplIW.getInstance();
			}

			String pwtStatus = winController.checkTicketPWTStatus(winningAmount);

			boolean claimStatus = false;
			if("IW".equals(serviceCode) && IW.Status.NORMAL_PAY.equals(pwtStatus))
				claimStatus = winController.checkPayoutLimits(ticketNumber, userBean, winningAmount);

			if ("SLE".equals(serviceCode)) {
				responseObject.addProperty("responseCode", 0);
				responseObject.addProperty("pwtStatus", pwtStatus);
			} else if ("IW".equals(serviceCode)) {
				responseObject.addProperty("responseCode", 0);
				responseObject.addProperty("responseMessage", "SUCCESS");
				JsonObject tempObj = new JsonObject();
				if(IW.Status.NORMAL_PAY.equals(pwtStatus)) {
					tempObj.addProperty("status", true && claimStatus);
					tempObj.addProperty("message", claimStatus ? "Valid For PWT" : "Can Not Verify or High Prize");
					responseObject.add("responseData", tempObj);
				} else {
					tempObj.addProperty("status", false);
					tempObj.addProperty("message", "InValid For PWT");
					responseObject.add("responseData", tempObj);
				}
			}
		} catch (LMSException le) {
			responseObject.addProperty("responseCode", le.getErrorCode());
			responseObject.addProperty("responseMessage", le.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			//throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseObject.addProperty("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseObject.addProperty("responseMessage", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		String json = new Gson().toJson(responseObject);
		logger.info("LMS Response Data-{}"+json);

		return json;
	}

	@Path("/checkRetailerClaimStatus")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String checkRetailerClaimStatus(@Context HttpServletRequest request, String requestData) throws LMSException {
		log.info("AUDIT_ID-{} : LMS Request Data-{}", TransactionManager.getAuditId(), requestData);

		UserInfoBean userBean = null;
		JsonObject responseObject = new JsonObject();
		try {
			JsonObject requestObject = (JsonObject) new JsonParser().parse(requestData);
			String userName = requestObject.get("userName").getAsString();
			String userSession = requestObject.get("userSession").getAsString();
			String serviceCode = requestObject.get("serviceCode").getAsString();
			double winningAmount = requestObject.get("winningAmount").getAsDouble();

			userBean = getUserBean(userName);
			TpUtility.validateSessions(userBean, userSession, serviceCode);

			WinningMgmtController winController = null;
			if ("SLE".equals(serviceCode)) {
				winController = WinningMgmtControllerImplSLE.getInstance();
			} else if ("IW".equals(serviceCode)) {
				winController = WinningMgmtControllerImplIW.getInstance();
			}
			
			String statusMsg = winController.checkRetailerClaimStatus(winningAmount, userBean);
			responseObject.addProperty("responseCode", 0);
			responseObject.addProperty("statusMsg", statusMsg);
		} catch (LMSException le) {
			responseObject.addProperty("responseCode", le.getErrorCode());
			responseObject.addProperty("responseMessage", le.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		String json = new Gson().toJson(responseObject);
		logger.info("LMS Response Data-{}"+json);

		return json;
	}

	@Path("/manageWinning")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String manageWinning(@Context HttpServletRequest request, String requestData) throws LMSException {
		log.info("AUDIT_ID-{} : LMS Request Data-{}", TransactionManager.getAuditId(), requestData);

		TPRequestBean requestBean = null;
		UserInfoBean userInfoBean = null;
		TPResponseBean responseBean = null;
		try {
			JsonObject reqJsonObject = (JsonObject) new JsonParser().parse(requestData);
			requestBean = new Gson().fromJson(reqJsonObject, new TypeToken<TPRequestBean>() {}.getType());

			try {
				if ("IW".equals(requestBean.getServiceCode()) && "WRAPPER".equals(requestBean.getUserType())) {
					userInfoBean = new UserInfoBean();
					userInfoBean.setUserName(requestBean.getUserName());
					userInfoBean.setUserOrgId(requestBean.getUserOrgId());
//					userInfoBean.setUserId(requestBean.getUserId());
					userInfoBean.setUserId(Util.fetchUserIdFromUserName(requestBean.getUserName()));
					userInfoBean.setUserType("BO");
				} else
					userInfoBean = getUserBean(requestBean.getUserName());
			} catch (Exception e) {
				throw new LMSException(SLEErrors.SESSION_TIME_OUT_ERROR_CODE, SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
			}
			if ("IW".equals(requestBean.getServiceCode()) && "WRAPPER".equals(requestBean.getUserType())) {
				// No Session Check
			} else
				TpUtility.validateSessions(userInfoBean, requestBean.getUserSession(), requestBean.getServiceCode());

			WinningMgmtController winController = null;
			if ("SLE".equals(requestBean.getServiceCode())) {
				winController = WinningMgmtControllerImplSLE.getInstance();
			} else if ("IW".equals(requestBean.getServiceCode())) {
				winController = WinningMgmtControllerImplIW.getInstance();
			}
			responseBean = winController.manageWinning(userInfoBean, requestBean);
		} catch (LMSException le) {
			responseBean = new TPResponseBean(le.getErrorCode());
			responseBean.setResponseMessage(le.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		String json = new Gson().toJson(responseBean);
		logger.info("LMS Response Data-{}" + json);
		return json;
	}

	@Path("/fetchUserBalance")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchOrgBalance(String reqData) {
		log.info("AUDIT_ID-{} : LMS Request Data-{}", TransactionManager.getAuditId(), reqData);
		double bal = 0.0;
		JSONObject responseObj = new JSONObject();
		String responseString = null;
		try {
			JsonObject reqJsonObject = (JsonObject) new JsonParser().parse(reqData);
			bal = AccountMgmtController.getInstance().fetchUserBalance(reqJsonObject.get("userId").getAsLong());
			responseObj.put("balance", bal);
			responseObj.put("responseCode", 0);

			responseString = new Gson().toJson(responseObj);
			logger.info("LMS Response Data for fetchOrgBalance-{}" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(responseObj.isEmpty()) {
				responseObj.put("responseCode", -1);
			}
			responseString = new Gson().toJson(responseObj);
			logger.info("LMS Response Data for fetchOrgBalance-{}" + responseString);
		}
		return responseString;
	}
	
}