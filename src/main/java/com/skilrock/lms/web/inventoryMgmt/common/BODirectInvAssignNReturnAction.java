package com.skilrock.lms.web.inventoryMgmt.common;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.controller.invMgmtController.BODirectInvAssignNReturnControllerImpl;
import com.skilrock.lms.coreEngine.inventoryMgmt.ConsNNonConsInvHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;

public class BODirectInvAssignNReturnAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BODirectInvAssignNReturnAction() {
		super(BODirectInvAssignNReturnAction.class);
	}

	private Map<String, String> brandMap = null;
	private Map<String, String> modelMap = null;
	private Map<String, String> invMap = null;
	private HttpSession session;
	private int agtOrgId;
	private int retOrgId;
	private int invId;
	private String invSrNo;
	private String modelId;
	private String brandsId;
	private Map<String, String> bindingLengthMap = null;

	public Map<String, String> getBindingLengthMap() {
		return bindingLengthMap;
	}

	public void setBindingLengthMap(Map<String, String> bindingLengthMap) {
		this.bindingLengthMap = bindingLengthMap;
	}

	public String getBrandsId() {
		return brandsId;
	}

	public void setBrandsId(String brandsId) {
		this.brandsId = brandsId;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public int getInvId() {
		return invId;
	}

	public void setInvId(int invId) {
		this.invId = invId;
	}

	public String getInvSrNo() {
		return invSrNo;
	}

	public void setInvSrNo(String invSrNo) {
		this.invSrNo = invSrNo;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Map<String, String> getBrandMap() {
		return brandMap;
	}

	public void setBrandMap(Map<String, String> brandMap) {
		this.brandMap = brandMap;
	}

	public Map<String, String> getModelMap() {
		return modelMap;
	}

	public void setModelMap(Map<String, String> modelMap) {
		this.modelMap = modelMap;
	}

	public Map<String, String> getInvMap() {
		return invMap;
	}

	public void setInvMap(Map<String, String> invMap) {
		this.invMap = invMap;
	}

	public String nonConsDirectInvReturn() throws LMSException {
		try {
			session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
			ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
			String cntrlType = "All";
			Map<String, Map<String, String>> invDetailsMap = helper.fetchConsInvNModelSpecDetail("NON_CONS", cntrlType);

			setModelMap(invDetailsMap.get("modelMap"));
			setBrandMap(invDetailsMap.get("brandMap"));
			setInvMap(invDetailsMap.get("invMap"));

			CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
			ArrayList<String> termIdList = null;
			termIdList = comFunHelper.fetchTerminalIds(userInfoBean.getUserOrgId());

			session.setAttribute("TEMINAL_LIST", termIdList);
		} catch (LMSException le) {
			return "applicationError";
		} catch (Exception e) {
			e.printStackTrace();
			return "applicationError";
		}

		return SUCCESS;
	}

	public String nonConsDirectInvReturnProcess() {

		try {
			session = request.getSession();
			UserInfoBean userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
			if (retOrgId != 0 && userInfo != null && invSrNo != null && agtOrgId != 0 && modelId != null && brandsId != null && invId != 0) {
				int DNID = BODirectInvAssignNReturnControllerImpl.getInstance().retailerToBOInvReturnProcess(userInfo, retOrgId,
								invSrNo, agtOrgId, modelId, brandsId, invId);
				session.setAttribute("DEL_NOTE_ID", DNID);
				return SUCCESS;
			} else {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,getText("invalid.data.to.process"));
			}
		} catch (LMSException le) {
			logger.info("RESPONSE_CASH_PAYMENT_SUBMIT-: ErrorCode:"
					+ le.getErrorCode() + " ErrorMessage:"
					+ le.getErrorMessage());
			request.setAttribute("LMS_EXCEPTION", le.getErrorMessage());
			return "applicationLMSException";
		} catch (Exception e) {
			e.printStackTrace();
			return "applicationError";
		}

	}
	
	public String assignInvRet() throws LMSException {
		try {
			session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			int DNID = 0;
			logger.info("Assign Invetory of :" + invId + "With Brand :"
					+ brandsId + " With Model :" + modelId + " for retailer :"
					+ retOrgId + "having Serial No :" + invSrNo);
			if (agtOrgId != 0 && retOrgId != 0 && invSrNo != null) {
				DNID = BODirectInvAssignNReturnControllerImpl.getInstance()
						.assignInvBoToRetailer(agtOrgId, retOrgId, invId,
								modelId, brandsId, invSrNo, userInfoBean);
			}
			session.setAttribute("DEL_NOTE_ID", DNID);
		} catch (LMSException e) {
			logger.error("LMS Excetion ", e);
			request.setAttribute("LMS_EXCEPTION", e.getErrorMessage());
			return "applicationLMSException";
		} catch (Exception e) {
			logger.error("Excetion ", e);
			return INPUT;
		}
		return SUCCESS;
	}
	
	public String fetchBoInv() throws LMSException {
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		String cntrlType = "All";
		Map<String, Map<String, String>> invDetailsMap = helper
				.fetchConsInvNModelSpecDetail("NON_CONS", cntrlType);
		setModelMap(invDetailsMap.get("modelMap"));
		setBrandMap(invDetailsMap.get("brandMap"));
		setInvMap(invDetailsMap.get("invMap"));
		setBindingLengthMap(invDetailsMap.get("modelMapBindingLength"));
		CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
		ArrayList<String> termIdList = null;
		termIdList = comFunHelper.fetchTerminalIds(userInfoBean.getUserOrgId());
		session.setAttribute("TEMINAL_LIST", termIdList);
		return SUCCESS;
	}
}