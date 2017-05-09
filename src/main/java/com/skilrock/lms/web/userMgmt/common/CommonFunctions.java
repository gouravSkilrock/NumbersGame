package com.skilrock.lms.web.userMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

public class CommonFunctions extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentOrgId;
	private String country;
	Log logger = LogFactory.getLog(CommonFunctions.class);
	private String orgType;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private String state;

	private String userName;
	private String modelName;
	private String city;
	private String area;
	private long LSTktNo;

	public void fetchTerminalIds() throws Exception {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		try {
			PrintWriter out = getResponse().getWriter();

			CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
			ArrayList<String> termIdList = null;
			if (agentOrgId == null) {
				termIdList = comFunHelper.fetchTerminalIds(userInfoBean
						.getUserOrgId());
			} else {
				termIdList = comFunHelper.fetchTerminalIds(Integer
						.parseInt(agentOrgId));
			}
			/*
			 * String html =
			 * " <select class=\"option\" name=\"terminalId\" id ='termId'><option value=-1>--PleaseSelect--</option>"
			 * ; for (String name : termIdList) { html +=
			 * "<option class=\"option\" value=\"" + name + "\">" + name +
			 * "</option>"; } html += "</select>";
			 * response.setContentType("text/html");
			 */
			String html = termIdList.toString().replace("[", "").replace("]",
					"").replace(", ", ":");
			System.out.println("Terminal Ids:" + html);
			out.print(html);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	public void fetchTerminalModelNames() throws Exception {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		try {
			ArrayList<String> modelNamesList = null;
			if (agentOrgId == null || "null".equalsIgnoreCase(agentOrgId)) {
				modelNamesList = new CommonFunctionsHelper()
						.fetchTerminalModelNames(userInfoBean.getUserOrgId());
			} else {
				modelNamesList = new CommonFunctionsHelper()
						.fetchTerminalModelNames(Integer.parseInt(agentOrgId));
			}
			String modelNamesStr = modelNamesList.toString().replace("[", "")
					.replace("]", "").replace(", ", ":");
			System.out.println("Terminal Model Names List:" + modelNamesStr);
			getResponse().getWriter().print(modelNamesStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public void fetchInvModelNames() throws Exception {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		try {
			ArrayList<String> modelNamesList = null;
			if (agentOrgId == null) {
				modelNamesList = new CommonFunctionsHelper()
						.fetchInvModelNames(userInfoBean.getUserOrgId());
			} else {
				modelNamesList = new CommonFunctionsHelper()
						.fetchInvModelNames(Integer.parseInt(agentOrgId));
			}
			String modelNamesStr = modelNamesList.toString().replace("[", "")
					.replace("]", "").replace(", ", ":");
			logger.debug("Inv Model Names List:" + modelNamesStr);
			getResponse().getWriter().print(modelNamesStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public void fetchTerminalSerialNos() throws Exception {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		try {
			ArrayList<String> serialNoList = null;
			if (agentOrgId == null) {
				serialNoList = new CommonFunctionsHelper()
						.fetchTerminalSerialNos(userInfoBean.getUserOrgId(),
								modelName);
			} else {
				serialNoList = new CommonFunctionsHelper()
						.fetchTerminalSerialNos(Integer.parseInt(agentOrgId),
								modelName);
			}
			String serialNoStr = serialNoList.toString().replace("[", "")
					.replace("]", "").replace(", ", ":");
			System.out.println("Terminal Serial No List:" + serialNoStr);
			getResponse().getWriter().print(serialNoStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public String fetchUnClmablePwtDetail() throws LMSException {
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		logger.debug("fetchUnClmablePwtDetail = ");
		System.out.println("fetchUnClmablePwtDetail = ");
		CommonFunctionsHelper helper = new CommonFunctionsHelper();
		Map map = helper.fetchUnCLMPwtDetailsOfOrg(userInfoBean.getUserOrgId(),
				userInfoBean.getUserType(), "UNCLAIM_BAL");

		session.setAttribute("totalUnclmBal", map.get("totalUnclmBal"));
		session.setAttribute("unclmPwtDet", map.get("unclmPwtDet"));
		session.setAttribute("newDRGameWiseTotAmt", map
				.get("newDRGameWiseTotAmt"));
		session.setAttribute("DRClmPwtDetailMap", map.get("DRClmPwtDetailMap"));

		return SUCCESS;
	}

	public String getAgent() throws IOException {
		PrintWriter out = getResponse().getWriter();
		List<OrgBean> characters = new CommonFunctionsHelper()
				.getAgentOrgList();
		String html = "&nbsp;&nbsp;";
		html = "<select class=\"option\" name=\"agentOrgId\"  onchange = \"getRetailerList('getRetailerList.action','retlist',this.value)\"><option value=''>--PleaseSelect--</option>";

		for (OrgBean orgBean : characters) {
			html += "<option class=\"option\" value=\"" + orgBean.getOrgId()
					+ "\">" + orgBean.getOrgName() + "</option>";
		}

		html += "</select>";
		response.setContentType("text/html");
		out.print(html);
		return null;
	}

	public String getAgentForEditPriv() throws IOException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		int userOrgId = 0;
		if (uib != null) {
			userOrgId = uib.getUserOrgId();
		}

		String agtOrgList = "";
		try {
			agtOrgList = new AjaxRequestHelper().getOrgIdList(userOrgId,
					"AGENT");
		} catch (LMSException e) {

			e.printStackTrace();
		}
		/*
		 * String html = "&nbsp;&nbsp;"; html =
		 * "<select class=\"option\" name=\"agentOrgId\"  onchange = \"getRetailerList('bo_rep_searchReceipt_FetchRetailer.action','retlist',this.value)\"><option value=''>--PleaseSelect--</option>"
		 * ;
		 * 
		 * for (OrgBean orgBean : characters) { html +=
		 * "<option class=\"option\" value=\"" + orgBean.getOrgId() + "\">" +
		 * orgBean.getOrgName() + "</option>"; }
		 * 
		 * html += "</select>";
		 */
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out.print(agtOrgList);
		return null;

	}

	public String getAgentOrgId() {
		return agentOrgId;
	}

	public String getAgentWithOrgIdNACA() throws IOException, LMSException {
		PrintWriter out = getResponse().getWriter();
		String characters = new CommonFunctionsHelper().getAgentOrgWithIdNACA();

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out.print(characters);
		return null;
	}

	public void getCityNameList() throws LMSException {
		try {
			PrintWriter out = getResponse().getWriter();
			if (country == null) {
				country = "";
			}

			if (state == null) {
				state = "";
			}

			CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
			System.out.println("*****" + country + "***" + state + "******");
			ArrayList<String> cityNameList = comFunHelper.getCityNameList(
					country.trim(), state.trim());

			String html = " <select class=\"option\" name=\"city\" id ='plrCity' onchange=\"return fetchAreaList()\"><option value=-1>--PleaseSelect--</option>";
			for (String name : cityNameList) {
				html += "<option class=\"option\" value=\"" + name + "\">"
						+ name + "</option>";
			}
			html += "</select>";
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out.print(html);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public String getCountry() {
		return country;
	}

	public String getOrgType() {
		return orgType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getRetailerList() throws IOException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if ("AGENT".equalsIgnoreCase(infoBean.getUserType().trim())) {
			agentOrgId = Integer.toString(infoBean.getUserOrgId());
		}
		List<OrgBean> characters = new CommonFunctionsHelper()
				.getRetailerOrgList(agentOrgId);
		String html = "&nbsp;&nbsp;";
		html = "<select class=\"option\" name=\"retOrgId\" ><option value='-1'>--PleaseSelect--</option>";
		for (OrgBean orgBean : characters) {
			html += "<option class=\"option\" value=\"" + orgBean.getOrgId()
					+ "\">" + orgBean.getOrgName() + "</option>";
		}
		html += "</select>";
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out.print(html);
		return null;
	}

	public String getRetailerListForEditPriv() throws IOException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if ("AGENT".equalsIgnoreCase(infoBean.getUserType().trim())) {
			agentOrgId = Integer.toString(infoBean.getUserOrgId());
		}
		String retList = new AjaxRequestHelper().getUserIdList(Integer
				.parseInt(agentOrgId));
		/*
		 * List<OrgBean> characters = new CommonFunctionsHelper()
		 * .getRetailerOrgList(agentOrgId); String html = "&nbsp;&nbsp;"; html =
		 * "<select class=\"option\" name=\"retOrgId\" onchange = \"getOrgPriveledges(this.value)\" ><option value='-1'>--PleaseSelect--</option>"
		 * ; for (OrgBean orgBean : characters) { html +=
		 * "<option class=\"option\" value=\"" + orgBean.getOrgId() + "\">" +
		 * orgBean.getOrgName() + "</option>"; } html += "</select>";
		 */
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out.print(retList);
		return null;
	}

	public String getRetailerWithOrgIdNACA() throws IOException, LMSException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String characters = new CommonFunctionsHelper()
				.getRetailerOrgWithIdNACA(infoBean.getUserOrgId());

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out.print(characters);
		return null;
	}

	public String getState() {
		return state;
	}

	public void getStateNameList() throws LMSException {
		try {
			PrintWriter out = getResponse().getWriter();
			if (country == null) {
				country = "";
			}

			CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
			ArrayList<String> stateNameList = comFunHelper
					.getStateNameList(country.trim());

			String html = " <select class=\"option\" name=\"state\" id ='plrState' onchange=\"return fetchCityList()\"><option value=-1>--PleaseSelect--</option>";
			for (String name : stateNameList) {
				html += "<option class=\"option\" value=\"" + name + "\">"
						+ name + "</option>";
			}
			html += "</select>";
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out.print(html);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public void getCountryPhnCodeList() throws LMSException {

		try {
			PrintWriter out = getResponse().getWriter();
			if (country == null) {
				country = "";
			}

			CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();

			String phoneCode = comFunHelper.getCountryPhnCode(country.trim());
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out.print(phoneCode);

		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public void getCityCode() throws LMSException {
		String code = null;
		try {
			PrintWriter out = getResponse().getWriter();
			if (city == null) {
				city = "";
			}

			CommonFunctionsHelper comFunHelper = new CommonFunctionsHelper();
			code = comFunHelper.getCityCode(city.trim());

			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out.print(code);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUserName() {
		return userName;
	}

	public void setAgentOrgId(String agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void updateClmableBalOfOrg() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			HttpSession session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			String rootPath = (String) session.getAttribute("ROOT_PATH");
			String ledgerStatus = null;
			String amtStr = null;
			if ("YES".equalsIgnoreCase(LMSFilterDispatcher.claimByClick)) {

				CommonFunctionsHelper helper = new CommonFunctionsHelper();
				ledgerStatus = helper.updateClmableBalOfOrg(userInfoBean
						.getUserId(), userInfoBean.getUserOrgId(), userInfoBean
						.getUserType(), userInfoBean.getParentOrgId());
				logger.debug(" Ledger Status = " + ledgerStatus
						+ " for Organization = " + userInfoBean.getUserOrgId());
				System.out.println(" Ledger Status = " + ledgerStatus
						+ " for Organization = " + userInfoBean.getUserOrgId());
			}
			if (!"AlreadyUpdated".equalsIgnoreCase(ledgerStatus)) {
				AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
				amtStr = ajxHelper.getAvlblCreditAmt(userInfoBean);
			} else {
				amtStr = "AlreadyUpdated";
			}
			out.print(amtStr);

		} catch (Exception e) {
			e.printStackTrace();
			if (out != null) {
				out.print("");
			}
		}

	}

	public void updateClmableBalOfOrgEmbd() {
		String amtStr = null;
		try {

			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}
			/*
			 * logger.debug(" LOGGED_IN_USERS maps is " +
			 * currentUserSessionMap); System.out.println(" LOGGED_IN_USERS maps
			 * is " + currentUserSessionMap);
			 */
			logger.debug(" user name is " + userName);
			System.out.println(" user name is " + userName);

			HttpSession session = (HttpSession) currentUserSessionMap
					.get(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}
			Map virnMap = null;
			// HttpSession session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays = Integer.parseInt((String) sc
					.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			long lastPrintedTicket = 0;
			int gameId = 0;
			if (LSTktNo != 0) {
				lastPrintedTicket = LSTktNo
						/ Util.getDivValueForLastSoldTkt(String
								.valueOf(LSTktNo).length());
				;
				gameId = Util.getGameIdFromGameNumber(Util
						.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}

			String actionName = ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(
					userInfoBean, lastPrintedTicket, "TERMINAL", refMerchantId,
					autoCancelHoldDays, actionName, gameId);

			String rootPath = (String) session.getAttribute("ROOT_PATH");
			String ledgerStatus = null;

			if ("YES".equalsIgnoreCase(LMSFilterDispatcher.claimByClick)) {

				CommonFunctionsHelper helper = new CommonFunctionsHelper();
				ledgerStatus = helper.updateClmableBalOfOrg(userInfoBean
						.getUserId(), userInfoBean.getUserOrgId(), userInfoBean
						.getUserType(), userInfoBean.getParentOrgId());
				logger.debug(" Ledger Status = " + ledgerStatus
						+ " for Organization = " + userInfoBean.getUserOrgId());
				System.out.println(" Ledger Status = " + ledgerStatus
						+ " for Organization = " + userInfoBean.getUserOrgId());
			}
			if (!"AlreadyUpdated".equalsIgnoreCase(ledgerStatus)) {
				AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
				amtStr = ajxHelper.getAvlblCreditAmt(userInfoBean);
			} else {
				amtStr = "AlreadyUpdated";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// ------first data is actual balance..(AC-CB)-----
			if ("AlreadyUpdated".equalsIgnoreCase(amtStr)) {
				response.getOutputStream().write(amtStr.getBytes());
			} else {
				// String[] bal=amtStr.split("=");

				response.getOutputStream().write(
						"Ledger Updated Successfully".getBytes());
				// response.getOutputStream().write((FormatNumber.formatNumber(bal[0])+","+FormatNumber.formatNumber(bal[1])+","+FormatNumber.formatNumber(bal[2])+","+FormatNumber.formatNumber(bal[3])).getBytes());
			}
			/*
			 * response.getOutputStream() .write( (FormatNumber.formatNumber("")
			 * + "," + FormatNumber.formatNumber("") + "," + FormatNumber
			 * .formatNumber
			 * ("")+","+FormatNumber.formatNumber("")+","+FormatNumber
			 * .formatNumber("")).getBytes());
			 * 
			 * System.out.println(" send data is " +
			 * FormatNumber.formatNumber(availableCredit) + "," +
			 * FormatNumber.formatNumber(clmBal) + "," +
			 * FormatNumber.formatNumber(unClmBal)+","
			 * +FormatNumber.formatNumber(extendedCreditLimit)+","
			 * +FormatNumber.formatNumber(currentCreditAmount));
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void logoutAnyUserForcefully(int userOrgId) {
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session = null;
		List<String> userNameList = CommonFunctionsHelper
				.chkStatusAndFetchUsers(userOrgId);
		String userName = null;
		for (int i = 0; i < userNameList.size(); i++) {
			userName = userNameList.get(i);
			session = (HttpSession) currentUserSessionMap.get(userName);
			if (session != null) {
				currentUserSessionMap.remove(userName);
				session.removeAttribute("USER_INFO");
				session.removeAttribute("ACTION_LIST");
				session.removeAttribute("PRIV_MAP");
				session.invalidate();
				session = null;
			}
		}
	}

	public void getStateListWithCode() throws LMSException {
		try {
			logger
					.info("Get State  List With State Code for country"
							+ country);
			PrintWriter out = getResponse().getWriter();
			if (country == null) {
				country = "";
			}
			String stateListWithCode = CommonFunctionsHelper
					.getStateListWithCode(country);
			response.setContentType("text/html");
			out.print(stateListWithCode);
			logger.info("Get State  List :" + stateListWithCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public void getCityListWithCode() throws LMSException {
		try {
			logger.info("Get City  List With City Code For Country" + country
					+ "state" + state);
			PrintWriter out = getResponse().getWriter();
			if (country == null || state == null) {
				country = "";
				state = "";
			}
			String cityListWithCode = CommonFunctionsHelper
					.getCityListWithCode(country, state);
			response.setContentType("text/html");
			out.print(cityListWithCode);
			logger.info("City List:" + cityListWithCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public void getAreaListWithCode() throws LMSException {
		try {
			logger.info("Get Area  List With Area Code For Country" + country
					+ "state" + state + "city" + city);
			PrintWriter out = getResponse().getWriter();
			if (country == null || state == null || city == null) {
				country = "";
				state = "";
				city = "";
			}
			String areaListWithCode = CommonFunctionsHelper
					.getAreaListWithCode(country, state, city);
			response.setContentType("text/html");
			
			out.print(areaListWithCode);
			logger.info("City List:" + areaListWithCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public void getDivisionListWithCode() throws LMSException {
		try {
			logger.info("Get Area  List With Area Code For Country" + country
					+ "state" + state + "city" + city + "area" + area);

			PrintWriter out = getResponse().getWriter();
			if (country == null || state == null || city == null
					|| area == null) {
				country = "";
				state = "";
				city = "";
				area = "";
			}
			String divisionListWithCode = CommonFunctionsHelper
					.getDivisionListWithCode(country, state, city, area);
			response.setContentType("text/html");
			out.print(divisionListWithCode);
			logger.info("City List:" + divisionListWithCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}