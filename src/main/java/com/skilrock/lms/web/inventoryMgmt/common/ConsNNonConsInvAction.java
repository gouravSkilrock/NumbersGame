package com.skilrock.lms.web.inventoryMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ConsNNonConsBean;
import com.skilrock.lms.beans.InvDetailBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.inventoryMgmt.ConsNNonConsInvHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgNUserRegHelper;

public class ConsNNonConsInvAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	static Log logger = LogFactory.getLog(ConsNNonConsInvAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String addType;
	private int agtOrgId;
	private int brandId;

	// These text fields are used to hold data for create new inventory
	private String[] brandIdForModel;

	private String brandsId;
	// These text fields are used to hold data for create new Brand
	private String consAddType;
	private int[] consInvId;
	private int[] consModelId;
	private int[] consQty;
	private double cost;

	private double[] costPerUnit;
	private Integer count;
	private String end = null;

	private int invId;
	// These text fields are used to hold data for create new Brand
	private int[] invIdForBrand;
	private int[] invIdForModel;
	private int[] invIdForModSpec;

	private String[] invImg;
	private String invType;
	private String isNew;
	private String modelId;
	private String[] newBrandDesc;
	private String[] newBrandName;
	private String[] newInvDesc;

	// These text fields are used to hold data for create new inventory
	private String[] newInvName;

	private String[] newModelDesc;

	private String[] newModelName;

	private String[] newModSpecDesc;
	private int[] nonConsBrandId;
	private int[] nonConsInvId;

	private int[] nonConsModelId;

	private int orgId;
	private int ownerId;
	private String ownerType;
	private String quantity;

	private HttpServletRequest request;

	private HttpServletResponse response;

	// methods added by amit for agent end ...

	private String retInvDet;

	private int retOrgId;

	private String searchResultsAvailable;

	private String serNo[];

	private String serNoFileName;

	private HttpSession session;

	private String sign;

	private String[] specType;
	private String[] specUnit;
	private String[] specValue;
	private int start = 0;
	private String termId;
	private String ownerName;

	private String[] isReqOnReg;
	private String[] checkBindingLength;

	private Map<String, String> brandMap = null;
	private Map<String, String> modelMap = null;
	private Map<String, String> invMap = null;
	private Map<String, String> bindingLengthMap = null;
	private Map<String, String> invCodeMap = null;
	private String bind_len;

	private String invCode[];
	private String invSrNo;

	/**
	 * This Method is Used to Add new Inventory type and Their Brand and Model.
	 * for Consumable or Non Consumable Devices.
	 * 
	 * @throws LMSException
	 */
	public String addNewInv() throws LMSException {
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		String invUpdatedList = null;
		System.out.println(invType);
		if ("NON_CONS".equalsIgnoreCase(invType)) {
			if ("ADD_INV".equalsIgnoreCase(addType)) {
				invUpdatedList = helper.addNewInvDetails("NON_CONS",
						newInvName, newInvDesc, invImg);
			} else if ("ADD_BRAND".equalsIgnoreCase(addType)) {
				invUpdatedList = helper.addNewBrandDetails(invIdForBrand,
						newBrandName, newBrandDesc);
				System.out.println("invUpdatedList 11111 " + invUpdatedList);
			} else if ("ADD_MODEL".equalsIgnoreCase(addType)) {
				invUpdatedList = helper.addNewModelDetails(invIdForModel,
						brandIdForModel, newModelName, newModelDesc,
						isReqOnReg, checkBindingLength);
			}
		} else if ("CONS".equalsIgnoreCase(invType)) {
			if ("ADD_INV".equalsIgnoreCase(consAddType)) {
				invUpdatedList = helper.addNewInvDetails("CONS", newInvName,
						newInvDesc, invImg);
			} else if ("ADD_MODEL_SPEC".equalsIgnoreCase(consAddType)) {
				invUpdatedList = helper.addNewInvSpec(invIdForModSpec,
						specType, specUnit, specValue, costPerUnit,
						newModSpecDesc);
				System.out.println("invUpdatedList " + invUpdatedList);

			}
		}

		session = request.getSession();
		session.setAttribute("invListMsg", invUpdatedList.substring(0,
				invUpdatedList.indexOf("||")));
		session.setAttribute("invListMsg1", invUpdatedList
				.substring(invUpdatedList.indexOf("||") + 2));

		return SUCCESS;
	}

	public String agtNonConsInvReturn() throws LMSException {
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String status = "error";

		logger.debug("return Invetory of :" + invId + "With Brand :" + brandsId
				+ " With Model :" + modelId + " for retailer :" + retOrgId
				+ "having sr no :" + invSrNo);

		session.setAttribute("STATUS", status);
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		if (invSrNo != null && retOrgId != 0) {
			status = helper.agtNonConsInvReturnSave(retOrgId, invSrNo,
					userInfoBean.getUserOrgId(), modelId, userInfoBean
							.getUserType(), userInfoBean.getUserId());
		} else {
			System.out.println(">>>>" + retInvDet + "***" + retOrgId);
		}
		System.out.println("status>>>>>>>>>>>>>>" + status);
		session.setAttribute("STATUS", status);
		return SUCCESS;
	}

	public String agtReassignInvRet() throws LMSException {

		try {
			session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			String status = "error";
			logger.debug("Assign Invetory of :" + invId + "With Brand :"
					+ brandsId + " With Model :" + modelId + " for retailer :"
					+ retOrgId + "having sr no :" + invSrNo);
			if (retOrgId != 0 && invSrNo != null) {
				ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
				status = helper.reassignNonsRetSave(retOrgId, invSrNo,
						userInfoBean.getUserOrgId(), modelId, userInfoBean
								.getUserType(), userInfoBean.getUserId());
			}
			session.setAttribute("STATUS", status);
		} catch (LMSException e) {
			fetchAgtInv();
			addActionError(e.getErrorMessage());
			logger.error("LMS Excetion ", e);
			session.setAttribute("STATUS", getText("label.error"));
			return INPUT;
		} catch (Exception e) {
			logger.error("Excetion ", e);
			session.setAttribute("STATUS", getText("label.error"));
			return INPUT;
		}

		return SUCCESS;
	}

	public String consInvUpload() throws LMSException {

		session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		String UploadList = helper.consInvUpload(invId, modelId, quantity,
				userInfo.getUserId(), userInfo.getUserOrgId(), userInfo
						.getUserType());

		session.setAttribute("INV_NAME_LIST", UploadList);

		return SUCCESS;
	}

	public String consNonConsAsignInvSave() throws LMSException {

		System.out.println("ownerType = " + ownerType + ", agtOrgId = "
				+ agtOrgId + ", retOrgId = " + retOrgId + "\n nonConsInvId = "
				+ nonConsInvId + ", nonConsModelId = " + nonConsModelId
				+ ", nonConsBrandId = " + nonConsBrandId + "\n consInvId = "
				+ consInvId + ", consModelId = " + consModelId + ", consQty = "
				+ consQty + ", sno = " + serNo);

		if (nonConsInvId != null && nonConsInvId.length > 0
				&& ownerType != null && nonConsModelId != null
				&& nonConsInvId.length > 0
				&& nonConsInvId.length == nonConsInvId.length
				&& nonConsBrandId != null && nonConsBrandId.length > 0
				&& nonConsInvId.length == nonConsBrandId.length
				&& consInvId != null && consInvId.length > 0
				&& consModelId != null && consModelId.length > 0
				&& consInvId.length == consModelId.length && serNo != null
				&& serNo.length > 0 && nonConsInvId.length == serNo.length
				&& (agtOrgId > 0 || retOrgId > 0)) {

			session = request.getSession();
			UserInfoBean userInfo = (UserInfoBean) session
					.getAttribute("USER_INFO");
			ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
			int DNID = helper.assignConsNNonConsInv(userInfo.getUserOrgId(),
					userInfo.getUserId(), ownerType, agtOrgId, retOrgId,
					nonConsInvId, nonConsModelId, nonConsBrandId, serNo,
					consInvId, consModelId, consQty, userInfo.getUserType());
			session.setAttribute("DEL_NOTE_ID", DNID);
			// session.setAttribute("INV_WISE_DET_LIST", invDetList);

			return SUCCESS;
		} else {
			addActionError(getText("msg.entry.not.fill.proper"));
			return INPUT;
		}
	}

	public String consNonConsReturnInvMenu() {

		return null;
	}

	public String consNonConsReturnInvSave() throws LMSException {

		System.out.println("ownerType = " + ownerType + ", agtOrgId = "
				+ agtOrgId + ", retOrgId = " + retOrgId + "\n nonConsInvId = "
				+ nonConsInvId + ", nonConsModelId = " + nonConsModelId
				+ ", nonConsBrandId = " + nonConsBrandId + "\n consInvId = "
				+ consInvId + ", consModelId = " + consModelId + ", consQty = "
				+ consQty + ", sno = " + serNo);

		if (nonConsInvId != null && nonConsInvId.length > 0
				&& ownerType != null && nonConsModelId != null
				&& nonConsInvId.length > 0
				&& nonConsInvId.length == nonConsInvId.length
				&& nonConsBrandId != null && nonConsBrandId.length > 0
				&& nonConsInvId.length == nonConsBrandId.length
				&& consInvId != null && consInvId.length > 0
				&& consModelId != null && consModelId.length > 0
				&& consInvId.length == consModelId.length && serNo != null
				&& serNo.length > 0 && nonConsInvId.length == serNo.length
				&& (agtOrgId > 0 || retOrgId > 0)) {

			session = request.getSession();
			UserInfoBean userInfo = (UserInfoBean) session
					.getAttribute("USER_INFO");

			ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
			logger.debug(ownerType + " ----->>>>>>>>" + userInfo.getUserType());
			int DNID = helper.returnConsNNonConsInv(userInfo.getUserOrgId(),
					userInfo.getUserId(), ownerType, agtOrgId, retOrgId,
					nonConsInvId, nonConsModelId, nonConsBrandId, serNo,
					consInvId, consModelId, consQty, userInfo.getUserType());
			session.setAttribute("DEL_NOTE_ID", DNID);
			// session.setAttribute("INV_WISE_DET_LIST", invDetList);

			return SUCCESS;
		} else {
			addActionError(getText("msg.entry.not.fill.proper"));
			return INPUT;
		}
	}

	public String consNonConsSearchInvDetail() throws LMSException {
		System.out.println("inside consNonConsSearchInvDetail");
		session = request.getSession();
		List<ConsNNonConsBean> invDetList = null;

		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		invDetList = helper.fetchInvntoryWiseDetail(ownerId, invType, invId,
				brandId, modelId);
		session.setAttribute("INV_WISE_DET_LIST", invDetList);
		if ("NON_CONS".equalsIgnoreCase(invType.trim())) {
			return "noncons";
		} else {
			return "cons";
		}
	}

	public String consNonConsSearchTerminalInvMenu() throws LMSException {
		session = request.getSession();

		Map<String, TreeMap<String, String>> agentCountMap = new LinkedHashMap<String, TreeMap<String, String>>();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		// invDetList = helper.fetchTerminalInvntoryCount();
		agentCountMap = helper.fetchTerminalInvntoryCount();
		// List<String> modDetList=null;
		TreeMap<String, String> modDetMap = helper.fetchModelInvntory();
		// modDetList=helper.fetchModelInvntory();

		session.setAttribute("INV_DET_MAP", agentCountMap);
		session.setAttribute("MODEL_NAME_MAP", modDetMap);

		return SUCCESS;
	}

	public String consNonConsSearchAgentTerminal() throws LMSException {
		session = request.getSession();

		Map<String, TreeMap<String, String>> retailerCountMap = new LinkedHashMap<String, TreeMap<String, String>>();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		// invDetList = helper.fetchTerminalInvntoryCount();
		retailerCountMap = helper.fetchTerminalRetailerInvntoryCount(orgId);

		TreeMap<String, String> modDetMap = helper.fetchModelInvntory();

		session.setAttribute("INV_DET_MAP", retailerCountMap);
		session.setAttribute("MODEL_NAME_MAP", modDetMap);

		return SUCCESS;
	}

	public String consNonConsSearchModelSerial() throws LMSException {
		session = request.getSession();

		Map<String, String> agentModelSerialMap = new LinkedHashMap<String, String>();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();

		agentModelSerialMap = helper.fetchAgentModuleSerial(modelId, orgId);

		session.setAttribute("AGT_MODEL_SERIAL_MAP", agentModelSerialMap);

		return SUCCESS;
	}

	public String consNonConsSearchInvDetailAtAgent() throws LMSException {
		System.out.println("inside consNonConsSearchInvDetail");
		session = request.getSession();
		List<ConsNNonConsBean> invDetList = null;

		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		invDetList = helper.fetchInvntoryWiseDetail(ownerId, invType, invId,
				brandId, modelId);
		session.setAttribute("INV_WISE_DET_LIST", invDetList);
		if ("NON_CONS".equalsIgnoreCase(invType.trim())) {
			return "noncons";
		} else {
			return "cons";
		}
	}

	public String consNonConsSearchInvMenu() throws LMSException {

		session = request.getSession();

		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		Map<String, HashMap<String, String>> map = helper.fetchAgentNRetList();

		session.setAttribute("INV_NAME_LIST", map.get("nonConsInvMap"));
		session.setAttribute("CONS_INV_NAME_LIST", map.get("consInvMap"));
		session.setAttribute("BRAND_NAME_LIST", map.get("brandMap"));
		// session.setAttribute("AGENT_ORG_MAP", map.get("agentMap"));
		// session.setAttribute("RET_ORG_MAP", map.get("retMap"));
		session.setAttribute("MODEL_SPEC_LIST", map.get("invSpecMap"));
		session.setAttribute("MODEL_NAME_LIST", map.get("modelMap"));

		return SUCCESS;
	}

	public String consNonConsSearchInvMenuAtAgent() throws LMSException {

		session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		Map<String, Map<String, String>> map = helper
				.fetchAgentNRetListAtAgent(userBean);

		session.setAttribute("INV_NAME_LIST", map.get("nonConsInvMap"));
		session.setAttribute("CONS_INV_NAME_LIST", map.get("consInvMap"));
		session.setAttribute("BRAND_NAME_LIST", map.get("brandMap"));
		// session.setAttribute("RET_ORG_MAP", map.get("retMap"));
		session.setAttribute("MODEL_SPEC_LIST", map.get("invSpecMap"));
		session.setAttribute("MODEL_NAME_LIST", map.get("modelMap"));

		return SUCCESS;
	}

	public String consNonConsSearchInvMenuForAgt() throws LMSException {
		/*
		 * session = request.getSession(); UserInfoBean userInfoBean =
		 * (UserInfoBean) session .getAttribute("USER_INFO");
		 * ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		 * Map<String, String> map = helper.fetchRetList(userInfoBean
		 * .getUserOrgId()); session.setAttribute("RET_ORG_MAP", map);
		 */

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
		// setBindingLengthMap(invDetailsMap.get("modelMapBindingLength"));

		/*
		 * Map<String, String> map = helper.fetchRetListForAsnInv(userInfoBean
		 * .getUserOrgId());
		 */
		CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
		ArrayList<String> termIdList = null;
		termIdList = comFunHelper.fetchTerminalIds(userInfoBean.getUserOrgId());

		// session.setAttribute("RET_ORG_MAP", map);
		session.setAttribute("TEMINAL_LIST", termIdList);
		return SUCCESS;
	}

	public String consNonConsSearchInvNav() {
		int endValue = 0;
		int startValue = 0;
		HttpSession session = request.getSession();
		List ajaxList = (List) session.getAttribute("INV_DET_LIST");
		List ajaxSearchList = new ArrayList();

		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				startValue = 0;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				startValue = startValue - 10;
				if (startValue < 10) {
					startValue = 0;
				}
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				startValue = startValue + 10;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 10;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 10;

			}
			if (startValue == endValue) {
				startValue = endValue - 10;
			}

			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("INV_DET_LIST_NAV", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}

		return SUCCESS;
	}

	public String consNonConsSearchInvNavAtAgent() {
		int endValue = 0;
		int startValue = 0;
		HttpSession session = request.getSession();
		List ajaxList = (List) session.getAttribute("INV_DET_LIST");
		List ajaxSearchList = new ArrayList();

		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				startValue = 0;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				startValue = startValue - 10;
				if (startValue < 10) {
					startValue = 0;
				}
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				startValue = startValue + 10;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 10;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 10;

			}
			if (startValue == endValue) {
				startValue = endValue - 10;
			}

			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("INV_DET_LIST_NAV", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}

		return SUCCESS;
	}

	public String consNonConsSearchInvSearch() throws LMSException {
		session = request.getSession();
		List<ConsNNonConsBean> invDetList = null;

		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		invDetList = helper.fetchInvntoryCount(ownerType, agtOrgId, retOrgId,
				invType, invId, brandId, modelId, sign, count);

		session.setAttribute("INV_DET_LIST", invDetList);
		if (invDetList != null && invDetList.size() > 0) {
			session.setAttribute("INV_DET_LIST_NAV", invDetList);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			consNonConsSearchInvNav();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("INV_DET_LIST_NAV",
					new ArrayList<ConsNNonConsBean>());
		}
		return SUCCESS;
	}

	public String consNonConsSearchInvSearchAtAgent() throws LMSException {
		session = request.getSession();
		List<ConsNNonConsBean> invDetList = null;

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		invDetList = helper.fetchInvntoryCountAtAgent(ownerType, userBean
				.getUserOrgId(), retOrgId, invType, invId, brandId, modelId,
				sign, count);

		session.setAttribute("INV_DET_LIST", invDetList);
		if (invDetList != null && invDetList.size() > 0) {
			session.setAttribute("INV_DET_LIST_NAV", invDetList);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			consNonConsSearchInvNav();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("INV_DET_LIST_NAV",
					new ArrayList<ConsNNonConsBean>());
		}
		return SUCCESS;
	}

	public String execute() throws LMSException {
		session = request.getSession();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		Map<String, HashMap<String, String>> invDetailsMap = helper
				.fetchInvDetails();

		Map<String, String> nonConsInvMap = invDetailsMap.get("nonConsInvMap");
		Map<String, String> consInvMap = invDetailsMap.get("consInvMap");
		Map<String, String> brandNameMap = invDetailsMap.get("brandMap");

		session.setAttribute("INV_NAME_LIST", nonConsInvMap);
		session.setAttribute("CONS_INV_NAME_LIST", consInvMap);
		session.setAttribute("BRAND_NAME_LIST", brandNameMap);

		return SUCCESS;
	}

	public String fetchAgtInv() throws LMSException {
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

		/*
		 * Map<String, String> map = helper.fetchRetListForAsnInv(userInfoBean
		 * .getUserOrgId());
		 */
		CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
		ArrayList<String> termIdList = null;
		termIdList = comFunHelper.fetchTerminalIds(userInfoBean.getUserOrgId());

		// session.setAttribute("RET_ORG_MAP", map);
		session.setAttribute("TEMINAL_LIST", termIdList);

		return SUCCESS;
	}

	public void fetchRetList() {
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		try {
			PrintWriter out = response.getWriter();

			String retList = helper.fetchRetailerForReassignInv(userInfoBean
					.getUserOrgId());
			out.print(retList);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public String fetchConsInvNModelSpecDetail() throws LMSException {

		session = request.getSession();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String cntrlType = userInfoBean.getUserType().equalsIgnoreCase("ADMIN") ? "N"
				: "Y";
		Map<String, Map<String, String>> invDetailsMap = helper
				.fetchConsInvNModelSpecDetail("CONS", cntrlType);

		Map<String, String> invSpecMap = invDetailsMap.get("invSpecMap");
		Map<String, String> invMap = invDetailsMap.get("invMap");

		session.setAttribute("CONS_INV_SPEC_NAME_LIST", invSpecMap);
		session.setAttribute("CONS_INV_NAME_LIST", invMap);

		return SUCCESS;
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

	public Map<String, String> getBindingLengthMap() {
		return bindingLengthMap;
	}

	public void setBindingLengthMap(Map<String, String> bindingLengthMap) {
		this.bindingLengthMap = bindingLengthMap;
	}

	public String getBind_len() {
		return bind_len;
	}

	public void setBind_len(String bindLen) {
		bind_len = bindLen;
	}

	public String fetchNonConsInvNModelSpecDetail() throws LMSException {
		session = request.getSession();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String cntrlType = userInfoBean.getUserType().equalsIgnoreCase("ADMIN") ? "N"
				: "Y";
		Map<String, Map<String, String>> invDetailsMap = helper
				.fetchConsInvNModelSpecDetail("NON_CONS", cntrlType);

		/*
		 * Map<String, String> brandMap = null; Map<String, String> modelMap =
		 * null; Map<String, String> invMap = null; Map<String, String>
		 * bindingLengthMap = null;
		 */

		/*
		 * if(userInfoBean.getUserType().equalsIgnoreCase("ADMIN")){
		 * 
		 * 
		 * }else{
		 * 
		 * brandMap = invDetailsMap.get("brandMap"); modelMap =
		 * invDetailsMap.get("modelMap"); invMap = invDetailsMap.get("invMap");
		 * invMap.remove("1");// remove terminal Inventory bindingLengthMap =
		 * invDetailsMap.get("modelMapBindingLength"); }
		 */

		setModelMap(invDetailsMap.get("modelMap"));
		setBrandMap(invDetailsMap.get("brandMap"));
		setInvMap(invDetailsMap.get("invMap"));
		setBindingLengthMap(invDetailsMap.get("modelMapBindingLength"));
		setInvCodeMap(invDetailsMap.get("invCodeMap"));
		/*
		 * session.setAttribute("MODEL_NAME_LIST", modelMap);
		 * session.setAttribute("BRAND_NAME_LIST", brandMap);
		 * session.setAttribute("INV_NAME_LIST", invMap);
		 * session.setAttribute("MODEL_NAME_LIST_BINDING", bindingLengthMap);
		 */

		return SUCCESS;
	}

	public String fetchNonConsInvNModelSpecDetailForTrack() throws LMSException {

		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		String cntrlType = "All"; // get All Inventory where user cntrl or not
		Map<String, Map<String, String>> invDetailsMap = helper
				.fetchConsInvNModelSpecDetail("NON_CONS", cntrlType);

		brandMap = invDetailsMap.get("brandMap");
		modelMap = invDetailsMap.get("modelMap");
		invMap = invDetailsMap.get("invMap");
		bindingLengthMap = invDetailsMap.get("modelMapBindingLength");

		setModelMap(modelMap);
		setBrandMap(brandMap);
		setInvMap(invMap);
		setBindingLengthMap(bindingLengthMap);

		return SUCCESS;
	}

	public String getAddType() {
		return addType;
	}

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public int getBrandId() {
		return brandId;
	}

	public String[] getBrandIdForModel() {
		return brandIdForModel;
	}

	public String getBrandsId() {
		return brandsId;
	}

	public String getConsAddType() {
		return consAddType;
	}

	public int[] getConsInvId() {
		return consInvId;
	}

	public int[] getConsModelId() {
		return consModelId;
	}

	public int[] getConsQty() {
		return consQty;
	}

	public double getCost() {
		return cost;
	}

	public double[] getCostPerUnit() {
		return costPerUnit;
	}

	public Integer getCount() {
		return count;
	}

	public String getEnd() {
		return end;
	}

	public void getInvForRet() throws LMSException {
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		PrintWriter out;
		try {
			out = response.getWriter();

			ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
			String invList = helper.invListForRet(retOrgId);
			/*
			 * String html =
			 * " <select class=\"option\" name=\"retInvDet\" id ='retInvDet'><option value=-1>--PleaseSelect--</option>"
			 * ; for (String name : invList) { html +=
			 * "<option class=\"option\" value=\"" + name + "\">" + name +
			 * "</option>"; } html += "</select>";
			 */
			response.setContentType("text/html");
			out.print(invList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getInvId() {
		return invId;
	}

	public int[] getInvIdForBrand() {
		return invIdForBrand;
	}

	public int[] getInvIdForModel() {
		return invIdForModel;
	}

	public int[] getInvIdForModSpec() {
		return invIdForModSpec;
	}

	public String[] getInvImg() {
		return invImg;
	}

	public int[] getInvNameForBrand() {
		return invIdForBrand;
	}

	public String getInvType() {
		return invType;
	}

	public String getIsNew() {
		return isNew;
	}

	public String getModelId() {
		return modelId;
	}

	public String[] getNewBrandDesc() {
		return newBrandDesc;
	}

	public String[] getNewBrandName() {
		return newBrandName;
	}

	public String[] getNewInvDesc() {
		return newInvDesc;
	}

	public String[] getNewInvName() {
		return newInvName;
	}

	public String[] getNewModelDesc() {
		return newModelDesc;
	}

	public String[] getNewModelName() {
		return newModelName;
	}

	public String[] getNewModSpecDesc() {
		return newModSpecDesc;
	}

	public int[] getNonConsBrandId() {
		return nonConsBrandId;
	}

	public int[] getNonConsInvId() {
		return nonConsInvId;
	}

	public int[] getNonConsModelId() {
		return nonConsModelId;
	}

	public int getOrgId() {
		return orgId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getRetInvDet() {
		return retInvDet;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public String[] getSerNo() {
		return serNo;
	}

	public String getSerNoFileName() {
		return serNoFileName;
	}

	public String getSign() {
		return sign;
	}

	public String[] getSpecType() {
		return specType;
	}

	public String[] getSpecUnit() {
		return specUnit;
	}

	public String[] getSpecValue() {
		return specValue;
	}

	public int getStart() {
		return start;
	}

	public String getTermId() {
		return termId;
	}

	public String nonConsInvUpload() throws LMSException {

		session = request.getSession();
		UserInfoBean userInfo = null;
		UserInfoBean boUserBean = null;
		ConsNNonConsInvHelper helper = null;
		ArrayList arr = null;
		// List<Object> returnedList = null;
		HashSet<String> notAddedNos = null;
		// String[] serNoArr = null;
		// Map<String, HashSet<String>> nos = null;
		// List<String> allNos = null;
		Map<String, InvDetailBean> invMap = null;
		// List<Map<String, InvDetailBean>> mapList =null;

		try {

			if (modelId == null && "".equals(modelId.trim()) || cost <= 0) {
				addActionError(getText("msg.incorrect.detail.provide"));
				return ERROR;

			}
			// invMap = helper.fetchSerNoList(serNo,invCode,
			// serNoFileName,Integer.parseInt(bind_len),modelId);
			// mapList = helper.fetchInvMap(serNo,invCode,
			// serNoFileName,Integer.parseInt(modelId.split("-")[0]));
			// invMap = mapList.get(0);
			// duplicateInvMap =mapList.get(1);
			/*
			 * if(!duplicateInvMap.isEmpty()){
			 * fetchNonConsInvNModelSpecDetail();
			 * addActionError("Dupliate Serial Numbers Found !!"); return ERROR;
			 * }
			 */
			// serNoArr = allNos.toArray(new String[allNos.size()]);
			// check for empty nos.
			/*
			 * if(invMap.size() == 0){ fetchNonConsInvNModelSpecDetail();
			 * addActionError(
			 * "Please Provide atleast one serial Number throught interface or file."
			 * ); return ERROR; }
			 */
			// check for bindlen.
			/*
			 * for(int k = 0; k<serNoArr.length; k++){ if(serNoArr[k].length() <
			 * Integer.parseInt(bind_len)){ fetchNonConsInvNModelSpecDetail();
			 * addActionError
			 * ("In uploaded file serial No. length must have atleast size of "
			 * +bind_len); return ERROR; } }
			 */
			// check for duplicates.
			/*
			 * nos = helper.checkForDuplicates(allNos,
			 * Integer.parseInt(bind_len)); if(!nos.get("duplicate").isEmpty()){
			 * fetchNonConsInvNModelSpecDetail();
			 * addActionError("Dupliate Serial Numbers Found !!"); return ERROR;
			 * }
			 */
			// serNoArr = nos.get("correct").toArray(new
			// String[nos.get("correct").size()]);
			// getValidSerialNoArr
			// duplicateInvMap = helper.getDuplicateInvetoryMap(invMap,
			// Integer.parseInt(bind_len),Integer.parseInt(modelId));

			userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
			boUserBean = new UserInfoBean();
			// check added for Terminal Upload Restriction From BO
			helper = new ConsNNonConsInvHelper();
			if (userInfo.getUserType().equalsIgnoreCase("admin")) {
				OrgNUserRegHelper userhelper = new OrgNUserRegHelper();
				userhelper.changeUserBean("BO", boUserBean);
				arr = helper.nonConsInvUpload(serNo, invCode, serNoFileName,
						Integer.parseInt(modelId.split("-")[0]), cost, isNew,
						invMap, boUserBean.getUserId(), boUserBean
								.getUserOrgId(), boUserBean.getUserType());
			} else {
				arr = helper.nonConsInvUpload(serNo, invCode, serNoFileName,
						Integer.parseInt(modelId.split("-")[0]), cost, isNew,
						invMap, userInfo.getUserId(), userInfo.getUserOrgId(),
						userInfo.getUserType());
			}

			List<String> nwList = new ArrayList<String>();
			String notUploadedList = (String) arr.get(0);
			String emptySerialNoErr = (String) arr.get(1);
			String UploadList = (String) arr.get(2);
			// List<String> dupSerialNoList =
			// Arrays.asList(returnedList.get(1).toString().split(";"));
			nwList.addAll(Arrays.asList(notUploadedList.split(";")));
			// nwList.addAll(dupSerialNoList);
			notAddedNos = new HashSet<String>(nwList);

			if (emptySerialNoErr.indexOf("error") >= 0) {
				fetchNonConsInvNModelSpecDetail();
				if (notUploadedList.length() > 0) {
					addActionError("'" + notUploadedList
							+ getText("msg.not.added.in.db.duplicate") + " !!");
				}
				return ERROR;
			} else if (emptySerialNoErr.indexOf("fileError") >= 0) {
				fetchNonConsInvNModelSpecDetail();
				addActionError(getText("msg.wrong.file.upload.correct.file")
						+ ".");
				return ERROR;
			} else {
				session.setAttribute("INV_UPLOADED_LIST", UploadList);
				session.setAttribute("NOT_INV_UPLOADED_LIST", notAddedNos
						.toString().replace("[", "").replace("]", ""));
				return SUCCESS;
			}
		} catch (LMSException e) {
			logger.error("LMSException: ", e);
			fetchNonConsInvNModelSpecDetail();
			if (e.getErrorCode() == 6001) {
				addActionError(getText("msg.in.upload.file.sr.len.atleast.size")
						+ bind_len);
			} else {
				addActionError(e.getErrorMessage());
			}
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			return ERROR;
		}
	}

	public void setAddType(String addType) {
		this.addType = addType;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public void setBrandIdForModel(String[] brandIdForModel) {
		this.brandIdForModel = brandIdForModel;
	}

	public void setBrandsId(String brandsId) {
		this.brandsId = brandsId;
	}

	public void setConsAddType(String consAddType) {
		this.consAddType = consAddType;
	}

	public void setConsInvId(int[] consInvId) {
		this.consInvId = consInvId;
	}

	public void setConsModelId(int[] consModelId) {
		this.consModelId = consModelId;
	}

	public void setConsQty(int[] consQty) {
		this.consQty = consQty;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public void setCostPerUnit(double[] costPerUnit) {
		this.costPerUnit = costPerUnit;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setInvId(int invId) {
		this.invId = invId;
	}

	public void setInvIdForBrand(int[] invIdForBrand) {
		this.invIdForBrand = invIdForBrand;
	}

	public void setInvIdForModel(int[] invIdForModel) {
		this.invIdForModel = invIdForModel;
	}

	public void setInvIdForModSpec(int[] invIdForModSpec) {
		this.invIdForModSpec = invIdForModSpec;
	}

	public void setInvImg(String[] invImg) {
		this.invImg = invImg;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public void setNewBrandDesc(String[] newBrandDesc) {
		this.newBrandDesc = newBrandDesc;
	}

	public void setNewBrandName(String[] newBrandName) {
		this.newBrandName = newBrandName;
	}

	public void setNewInvDesc(String[] newInvDesc) {
		this.newInvDesc = newInvDesc;
	}

	public void setNewInvName(String[] newInvName) {
		this.newInvName = newInvName;
	}

	public void setNewModelDesc(String[] newModelDesc) {
		this.newModelDesc = newModelDesc;
	}

	public void setNewModelName(String[] newModelName) {
		this.newModelName = newModelName;
	}

	public void setNewModSpecDesc(String[] newModSpecDesc) {
		this.newModSpecDesc = newModSpecDesc;
	}

	public void setNonConsBrandId(int[] nonConsBrandId) {
		this.nonConsBrandId = nonConsBrandId;
	}

	public void setNonConsInvId(int[] nonConsInvId) {
		this.nonConsInvId = nonConsInvId;
	}

	public void setNonConsModelId(int[] nonConsModelId) {
		this.nonConsModelId = nonConsModelId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setRetInvDet(String retInvDet) {
		this.retInvDet = retInvDet;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setSerNo(String[] serNo) {
		this.serNo = serNo;
	}

	public void setSerNoFileName(String serNoFileName) {
		this.serNoFileName = serNoFileName;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setSpecType(String[] specType) {
		this.specType = specType;
	}

	public void setSpecUnit(String[] specUnit) {
		this.specUnit = specUnit;
	}

	public void setSpecValue(String[] specValue) {
		this.specValue = specValue;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public void verifyAssignInv() throws LMSException {
		System.out.println("12233==========");
		System.out.println("verification===========ownerType = " + ownerType
				+ ", agtOrgId = " + agtOrgId + ", retOrgId = " + retOrgId
				+ "\n nonConsInvId = " + nonConsInvId + ", nonConsModelId = "
				+ nonConsModelId + ", nonConsBrandId = " + nonConsBrandId
				+ "\n consInvId = " + consInvId + ", consModelId = "
				+ consModelId + ", consQty = " + consQty + ", sno = " + serNo);

		session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		String verifyConsNNonConsInv = helper.verifyConsNNonConsInv(userInfo
				.getUserOrgId(), nonConsInvId, nonConsModelId, nonConsBrandId,
				serNo, consInvId, consModelId, consQty, userInfo.getUserType());
		System.out.println("return res is    ========== "
				+ verifyConsNNonConsInv);
		try {
			PrintWriter out = response.getWriter();
			out.print(verifyConsNNonConsInv);
		} catch (IOException e) {
			e.printStackTrace();
			new LMSException(getText("msg.exp.due.to.pw"));
		}

	}

	public void verifyReturnInv() throws LMSException {
		System.out.println("verifyReturnInv==========");
		System.out.println("verification===========ownerType = " + ownerType
				+ ", OrgId = " + orgId + "\n nonConsInvId = " + nonConsInvId
				+ ", nonConsModelId = " + nonConsModelId
				+ ", nonConsBrandId = " + nonConsBrandId + "\n consInvId = "
				+ consInvId + ", consModelId = " + consModelId + ", consQty = "
				+ consQty + ", sno = " + serNo);

		session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");

		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		String verifyConsNNonConsInv = helper.verifyConsNNonConsInv(orgId,
				nonConsInvId, nonConsModelId, nonConsBrandId, serNo, consInvId,
				consModelId, consQty, userInfo.getUserType());
		System.out.println("return res is    ========== "
				+ verifyConsNNonConsInv);
		try {
			PrintWriter out = response.getWriter();
			out.print(verifyConsNNonConsInv);
		} catch (IOException e) {
			e.printStackTrace();
			new LMSException(getText("msg.exp.due.to.pw"));
		}
	}

	public String[] getIsReqOnReg() {
		return isReqOnReg;
	}

	public void setIsReqOnReg(String[] isReqOnReg) {
		this.isReqOnReg = isReqOnReg;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String[] getCheckBindingLength() {
		return checkBindingLength;
	}

	public void setCheckBindingLength(String[] checkBindingLength) {
		this.checkBindingLength = checkBindingLength;
	}

	// Gaurav

	public List<Object> getValidSerialNoArr(String[] serNo, int bindingLength)
			throws LMSException {

		// session = request.getSession();

		List<String> dbSerialNoList = null;
		Iterator<String> itr = null;
		String[] serialNoArr = null;
		String stringFromCompared = null;
		String stringToCompared = null;
		StringBuilder sb = new StringBuilder();
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		List<String> correctSerialNoList = new ArrayList<String>();
		List<Object> returnList = new ArrayList<Object>();

		try {
			dbSerialNoList = helper.fetchSerialNoList(modelId);
		} catch (Exception e) {
			logger.error("Exception : " + e);
		}

		int flag = 0;
		if (!(dbSerialNoList.isEmpty())) {
			for (int i = 0; i < serNo.length; i++) {
				if (serNo[i].length() > 0) {
					itr = dbSerialNoList.iterator();
					while (itr.hasNext()) {
						stringFromCompared = null;
						stringToCompared = null;
						stringFromCompared = (itr.next()).toString();
						if (bindingLength > 0
								&& stringFromCompared.length() >= bindingLength) {
							stringToCompared = serNo[i].substring(serNo[i]
									.length()
									- bindingLength, serNo[i].length());
							stringFromCompared = stringFromCompared
									.substring(stringFromCompared.length()
											- bindingLength, stringFromCompared
											.length());
						} else {
							stringToCompared = serNo[i];
						}

						if (stringFromCompared.length() >= bindingLength) {
							if ((stringToCompared.toUpperCase())
									.equals(stringFromCompared)) {
								flag = 1;
								break;
							} else {
								flag = 0;
							}
						} else {
							flag = 0;
						}
					}
				}
				if (flag == 1) {
					sb.append(serNo[i].trim() + ";");
				} else {
					correctSerialNoList.add(serNo[i]);
				}
			}
			serialNoArr = correctSerialNoList
					.toArray(new String[correctSerialNoList.size()]);
		} else {
			serialNoArr = serNo;
		}

		returnList.add(0, serialNoArr);
		returnList.add(1, sb);
		// logger.debug(returnList);
		return returnList;
	}

	public String[] getInvCode() {
		return invCode;
	}

	public void setInvCode(String[] invCode) {
		this.invCode = invCode;
	}

	public Map<String, String> getInvCodeMap() {
		return invCodeMap;
	}

	public void setInvCodeMap(Map<String, String> invCodeMap) {
		this.invCodeMap = invCodeMap;
	}

	public String getInvSrNo() {
		return invSrNo;
	}

	public void setInvSrNo(String invSrNo) {
		this.invSrNo = invSrNo;
	}

}
