package com.skilrock.lms.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationManagementHelper;

public class AjaxRequest extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	static Log logger = LogFactory.getLog(AjaxRequest.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentOrgName;
	private String retOrgName ;
	private String errTime;
	private String gameName;
	// created by arun , globally calling that function to get game list
	private String gameStatus;
	private String listType;
	private String message;
	private int orgId;
	private String orgType;
	private String ownerType;
	private HttpServletRequest request;
	private String retName;
	private String cityName;
	private String shiftGameStatus;

	public String getShiftGameStatus() {
		return shiftGameStatus;
	}

	public void setShiftGameStatus(String shiftGameStatus) {
		this.shiftGameStatus = shiftGameStatus;
	}

	private HttpServletResponse response;

	private String retType;

	private String serviceName;
	private String state;
	private String city;
	private String area;

	public void avlblCreditAmt() throws Exception {
		logger.debug("inside avlblCreditAmt =================== ");
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper helper = new AjaxRequestHelper();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		String selectedLocale = CommonMethods.getSelectedLocale();
		String value = "/com/skilrock/lms/web/loginMgmt/loggedOut.jsp?request_locale="+selectedLocale;

		HttpSession session = getRequest().getSession();
		UserInfoBean bean = null;

		if (session != null) {
			bean = (UserInfoBean) session.getAttribute("USER_INFO");
			if (bean != null) {
				value = helper.getAvlblCreditAmt(bean);
				session.setAttribute("USER_INFO", bean);
			}
		}

		out.print(value);
		logger.debug("out avlblCreditAmt =================== " + value);
	}

	public StringBuffer createMenu() throws UnsupportedEncodingException {
		response.setCharacterEncoding("UTF-8");
		StringBuffer menu = new StringBuffer("");
		PriviledgeBean privBean = null;
		LinkedHashMap actionServiceMap = (LinkedHashMap) request.getSession()
				.getAttribute("PRIV_MAP");
		Iterator itrMap = actionServiceMap.entrySet().iterator();
		while (itrMap.hasNext()) {
			Map.Entry pairs = (Map.Entry) itrMap.next();
			List privList = (List) pairs.getValue();
			menu.append("TABS-" + pairs.getKey());
			for (int i = 0; i < privList.size(); i++) {
				privBean = (PriviledgeBean) privList.get(i);
				menu.append("RT-" + privBean.getRelatedTo());
				menu.append(";" + privBean.getPrivTitle());
				menu.append(";" + privBean.getActionMapping());
			}
		}
		logger.debug("After Login" + menu);
		return menu;
	}

	public void ensurePass() throws Exception {
		PrintWriter out = getResponse().getWriter();
		if (message != null & message.equals("skilrock1234")) {
			out.print("true");
		} else {
			out.print("false");
		}
	}

	public void fetchAgtCrLimit() throws LMSException, IOException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		out.print(CommonMethods.chkCreditLimitAgt(userInfo.getUserOrgId(),
				Double.parseDouble(listType)));
	}

	public StringBuffer fetchCachedPages() {
		ServletContext sc = ServletActionContext.getServletContext();
		StringBuffer cachedDetails = new StringBuffer("");
		cachedDetails.append((HashMap) sc.getAttribute("CACHED_FILES_MAP"));
		return cachedDetails;
	}

	public void fetchChildOrgNGamenFmtSale() throws IOException, LMSException {

		logger.debug("Fetching " + ownerType + " Sale and Game and GameFormat");
		PrintWriter out = getResponse().getWriter();
		UserInfoBean uib = (UserInfoBean) request.getSession().getAttribute(
				"USER_INFO");
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		out.print(ajaxHelper.fetchChildOrgNGamenFmtSale(uib.getUserOrgId(),
				ownerType,shiftGameStatus));
	}

	public void fetchLimits() throws Exception {
		System.out.println("inside fetchLimits =================== ");
		UserInfoBean bean = new UserInfoBean();
		// bean.setUserOrgId(AjaxRequestHelper.getOrgUserId(agentOrgName));
		bean.setUserOrgId(Integer.parseInt(agentOrgName));
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper helper = new AjaxRequestHelper();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out.print(helper.getAvlblCreditAmt(bean));
	}
	
	public void outstandingBal() throws Exception {
		System.out.println("inside outstanding balance =================== ");
		UserInfoBean bean = new UserInfoBean();
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper helper = new AjaxRequestHelper();
		OrganizationManagementHelper agtHelper = new OrganizationManagementHelper() ;
		HttpSession session = request.getSession();
		session.setAttribute("ORG_SEARCH_RESULTS", null);
		// bean.setUserOrgId(AjaxRequestHelper.getOrgUserId(agentOrgName));
		if (agentOrgName == null )
		{

			out.print(FormatNumber.formatNumberForJSP(agtHelper.getRetOutstandingBal(Integer.parseInt(retOrgName), request, session, AjaxRequestHelper.getAgentOrgIdByRetailerOrgId(Integer.parseInt(retOrgName)))));
			
		}
		else
		{
			bean.setUserOrgId(Integer.parseInt(agentOrgName));
			out.print(FormatNumber.formatNumberForJSP(agtHelper.getAgentOutstandingBal(Integer.parseInt(agentOrgName), request, session)));
		}
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
	}
	
	public void retOutstandingBal() throws Exception {
		System.out.println("inside retailer outstanding balance =================== ");
		//UserInfoBean bean = new UserInfoBean();
		HttpSession session = request.getSession() ;
		UserInfoBean bean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		
		PrintWriter out = getResponse().getWriter();
		OrganizationManagementHelper helper = new OrganizationManagementHelper() ;
		out.print(FormatNumber.formatNumberForJSP(helper.getRetOutstandingBal(Integer.parseInt(retOrgName), request, session, AjaxRequestHelper.getAgentOrgIdByRetailerOrgId(Integer.parseInt(retOrgName)))));
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
	}

	

	public void fetchNamenLimit() throws IOException {

		logger.debug("Fetching Name and Menu Details");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = getResponse().getWriter();
		StringBuffer userDetails = new StringBuffer("");
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		userDetails.append(uib.getUserName());
		userDetails.append(":");

		// commented by arun
		/*
		 * userDetails.append(FormatNumber.formatNumber(uib.getAvailableCreditLimit
		 * ())+"=");
		 * userDetails.append(FormatNumber.formatNumber(uib.getClaimableBal
		 * ())+"=");
		 * userDetails.append(FormatNumber.formatNumber(uib.getUnclaimableBal
		 * ())+"=");
		 * userDetails.append(FormatNumber.formatNumber(uib.getAvailableCreditLimit
		 * ()-uib.getClaimableBal()));
		 */
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		userDetails.append(ajaxHelper.getAvlblCreditAmt(uib));

		session.setAttribute("USER_INFO", uib);
		userDetails.append(":");
		userDetails.append(uib.getUserType());
		userDetails.append("cacheBreakPoint" + fetchCachedPages());
		userDetails.append("cacheBreakPoint" + createMenu());
		out.print(userDetails);
	}

	public void fetchRetailerList() throws IOException, LMSException {
		logger.info("---fetchRetailerList---");
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		// int agtOrgId = AjaxRequestHelper.getOrgUserId(agentOrgName);
		// out.print(ajaxHelper.fetchRetailerNameList(agtOrgId, "ALL"));
		out.print(ajaxHelper.getUserIdList(orgId));
	}

	public void fetchRetailernGamenFmt() throws IOException, LMSException {

		logger.debug("Fetching Retailer and Game and GameFormat");
		PrintWriter out = getResponse().getWriter();
		UserInfoBean uib = (UserInfoBean) request.getSession().getAttribute(
				"USER_INFO");
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		out.print(ajaxHelper.fetchRetailernGameFmt(uib.getUserOrgId()));
	}

	public void fetchRoles() throws LMSException, IOException {
		PrintWriter out = getResponse().getWriter();

		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		out.print(ajaxHelper.fetchRoles());
	}

	public void fetchScrapNOrgLimit() throws LMSException {
		try {
			PrintWriter out = getResponse().getWriter();
			HttpSession session = request.getSession();
			UserInfoBean userInfo = (UserInfoBean) session
					.getAttribute("USER_INFO");
			ServletContext sc = ServletActionContext.getServletContext();
			StringBuilder sb = new StringBuilder();
			if ("BO".equalsIgnoreCase(userInfo.getUserType())) {
				logger.debug("fetchScrapNOrgLimit == Inside BO");
				sb.append("YES");
				sb.append("-").append(sc.getAttribute("verLimit"));
				sb.append("-").append(sc.getAttribute("appLimit"));
				sb.append("-").append(sc.getAttribute("payLimit"));
				sb.append("-").append(sc.getAttribute("scrapLimit"));
			} else if ("AGENT".equalsIgnoreCase(userInfo.getUserType())) {
				logger.debug("fetchScrapNOrgLimit == Inside BO");
				if ("YES".equalsIgnoreCase(userInfo.getPwtSacrap())) {
					sb.append("YES");
				} else {
					sb.append("NO");
				}
				sb.append("-").append(sc.getAttribute("retVerLimit"));
				sb.append("-").append(sc.getAttribute("retAppLimit"));
				sb.append("-").append(sc.getAttribute("retPayLimit"));
				sb.append("-").append(sc.getAttribute("retScrapLimit"));
				sb.append("-").append(sc.getAttribute("retDepositLimit"));
				sb.append("-").append(sc.getAttribute("retWithdrawalLimit"));
			}
			logger.debug(userInfo.getUserType() + " === fetchScrapNOrgLimit = "
					+ sb.toString());
			out.print(sb.toString());
		} catch (IOException e) {
			throw new LMSException(e);
		}
	}

	public void gameDetailAjaxForAgentSalePWTCommVar() throws Exception {
		PrintWriter out = getResponse().getWriter();
		StringBuffer gameNameDetails = new StringBuffer("");
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		logger.debug("game type is ======= " + gameName);
		List<String> gameList = ajaxHelper.getGameDetailForCommVar(gameName,
				agentOrgName);
		boolean flag = true;

		for (String gameName : gameList) {
			if (flag) {
				gameNameDetails.append(gameName);
				flag = false;
				continue;
			}
			gameNameDetails.append(":" + gameName);
		}

		out.print(gameNameDetails);
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public void getAgtCrLimit() throws LMSException, IOException {
		System.out.println("********fetch Agent Limits");
		PrintWriter out = getResponse().getWriter();
		out.print(CommonMethods.changeCreditLimitRet(orgId, Double
				.parseDouble(listType), message));
	}

	public void getAgtCrNotesLimit() throws LMSException, IOException {
		System.out.println("fetching agent limits for credit note ....."
				+ listType);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		out.print(CommonMethods.changeCrNoteBalRet(
				Double.parseDouble(listType), userInfo.getUserOrgId()));
	}

	public String getErrTime() {
		return errTime;
	}

	public String getGame(List ajaxList, String html) {

		html += "<select name=\"gameName\" class=\"option\" id=\"game_Name\" onblur=\"check()\" onchange=\"disableSubmit(),setGameDigit()\"><option class=\"option\" value=\"-1\">--Please Select--</option>";
		for (Iterator it = ajaxList.iterator(); it.hasNext();) {
			String name = (String) it.next();
			html += "<option class=\"option\" value=\"" + name + "\">" + name
					+ "</option>";
		}
		html += "</select>";
		return html;
	}

	public void getGameList() throws IOException, LMSException {
		logger.debug("serviceName = " + serviceName);
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String games = ajaxHelper.getGameListing(serviceName);
		logger.debug(games);
		response.setContentType("text/html");
		out.print(games);

	}

	public String getGameName() {
		return gameName;
	}

	public void getGameNameList() throws LMSException, IOException {
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper helper = new AjaxRequestHelper();
		String gameNames = helper.getGameNameList(gameStatus);
		out.print(gameNames);
		out.close();
		out.flush();
	}

	public void getGameNbrFormat() throws Exception {
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper helper = new AjaxRequestHelper();

		out.print(helper.getGameNbrFormat(getGameName()));
	}

	public void getGamePrizeList() throws Exception {
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper helper = new AjaxRequestHelper();

		out.print(helper.getGamePrizeList(getGameName()));
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void getListAjax() throws Exception {
		HttpSession session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		PrintWriter out = getResponse().getWriter();
		List ajaxList = new ArrayList();
		String html = "";
		String name = "";

		if (listType.equals("company")) {
			UserInfoBean bean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			AjaxRequestHelper helper = new AjaxRequestHelper();
			ajaxList = (ArrayList) helper.getOrganization(bean);

		} else if (listType.equals("agent") || listType.equals("agentPwt")) {
			AjaxRequestHelper helper = new AjaxRequestHelper();
			ajaxList = (ArrayList) helper.getAgents(listType, userInfo);

		} else if (listType.equals("country")) {
			AjaxRequestHelper helper = new AjaxRequestHelper();
			ajaxList = (ArrayList) helper.getCountry();

		} else if (listType.equals("supplier")) {
			AjaxRequestHelper helper = new AjaxRequestHelper();
			ajaxList = (ArrayList) helper.getSupplier();
			html += getGame(helper.getGameList("NEW"), html);

		}

		html += "<select id=\"ajaxList \" name=\"ajaxList\"><option class=\"option\" value=\"-1\">--Please Select--</option>";

		for (Iterator it = ajaxList.iterator(); it.hasNext();) {
			name = (String) it.next();
			html += "<option value=\"" + name + "\">" + name + "</option>";
		}
		html += "</select>";
		if (listType.equals("supplier")) {

		}
		response.setContentType("text/html");
		out.print(html);
	}

	public String getListType() {
		return listType;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * New Common Method
	 * 
	 * @throws IOException
	 * @throws LMSException
	 */
	public void getOrganizationNameIdList() throws IOException, LMSException {
		logger.debug("orgType = " + orgType);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		int userOrgId = 0;
		if (uib != null) {
			userOrgId = uib.getUserOrgId();
		}

		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String agtOrgType = ajaxHelper.getOrgIdList(userOrgId, orgType,uib.getRoleId());

		logger.debug(agtOrgType);
		response.setContentType("text/html");
		out.print(agtOrgType);

	}

	public void getAllOrganizationNameIdList() throws IOException, LMSException {
		logger.debug("orgType = " + orgType);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		int userOrgId = 0;
		if (uib != null) {
			userOrgId = uib.getUserOrgId();
		}

		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String agtOrgType = ajaxHelper.getAllOrgIdList(userOrgId, orgType);

		logger.debug(agtOrgType);
		response.setContentType("text/html");
		out.print(agtOrgType);

	}

	public void getOrganizationNameList() throws IOException, LMSException {
		logger.debug("orgType = " + orgType);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		// int userOrgId = uib.getUserOrgId();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String agtOrgType = ajaxHelper
				.getOrgIdList(uib.getUserOrgId(), orgType);

		logger.debug(agtOrgType);
		response.setContentType("text/html");
		out.print(agtOrgType);

	}

	public void getOrganizationUserNameIdList() throws IOException,
			LMSException {
		logger.debug("orgId = " + orgId);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		// int userOrgId = uib.getUserOrgId();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String agtOrgType = ajaxHelper.getOrgUserIdList(orgId);

		logger.debug(agtOrgType);
		response.setContentType("text/html");
		out.print(agtOrgType);

	}

	public void getUserNameIdList() throws IOException, LMSException {
		logger.info("orgId = " + orgId);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		// UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		// int userOrgId = uib.getUserOrgId();

		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String agtOrgType = ajaxHelper.getUserIdList(orgId);

		logger.info(agtOrgType);
		response.setContentType("text/html");
		out.print(agtOrgType);

	}

	public int getOrgId() {
		return orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void getPacknBookList() throws SQLException, IOException {
		PrintWriter out = getResponse().getWriter();

		AjaxRequestHelper helper = new AjaxRequestHelper();

		out.print(helper.getPacknBookList(agentOrgName, gameName));

	}

	public void getQrgNameHeadUserId() throws IOException, LMSException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String agtOrgType = ajaxHelper.getQrgNameHeadUserId(uib.getUserOrgId(),
				orgType);

		logger.debug(agtOrgType);
		response.setContentType("text/html");
		out.print(agtOrgType);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void getRetailerNameList() throws IOException, LMSException {
		logger.debug("Fetching Retailer Name & retType = " + retType);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		// UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		String endDate = CommonMethods.convertDateInGlobalFormat(
				(String) session.getAttribute("CHEQUE_END_DATE_START"),
				"yyyy-mm-dd", "mm/dd/yyyy");
		String startDate = CommonMethods.convertDateInGlobalFormat(
				(String) session.getAttribute("CHEQUE_START_DATE_START"),
				"yyyy-mm-dd", "mm/dd/yyyy");
		// AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		/*
		 * out.print(ajaxHelper.fetchRetailerNameList(uib.getUserOrgId(),
		 * retType) + ":" + startDate + "," + endDate);
		 */
		out.print(" " + ":" + startDate + "," + endDate);
	}

	public String getRetType() {
		return retType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void getUploadDetailAjax() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		PrintWriter out = getResponse().getWriter();
		StringBuffer uploadDetails = new StringBuffer("");

		uploadDetails.append((String) sc.getAttribute("AGT_SALE_COMM_RATE")
				+ ":");
		uploadDetails.append((String) sc.getAttribute("AGT_PWT_COMM_RATE")
				+ ":");
		uploadDetails.append((String) sc.getAttribute("RET_SALE_COMM_RATE")
				+ ":");
		uploadDetails.append((String) sc.getAttribute("RET_PWT_COMM_RATE")
				+ ":");
		uploadDetails.append((String) sc.getAttribute("GOVT_COMM_RULE"));

		out.print(uploadDetails);

	}

	public void logJavaScriptError() {
		String errTime = "";
		if (getErrTime() != "" && getErrTime() != null) {
			errTime = " at " + getErrTime();
		}

		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List to = new ArrayList();
		to.add("lms.error@skilrock.com");
		String errMsg = message + " received by " + agentOrgName
				+ " having IP Address as " + request.getRemoteAddr();
		logger.debug(errMsg);
		// MailSender ms = new
		// MailSender("lms.error@skilrock.com","skilrock",to, "Error Accessing
		// Server "+local+errTime, errMsg, "");
		// ms.setDaemon(true);
		// ms.start();
	}

	public void getOrgList() throws Exception {
		logger.debug("orgType = " + orgType);
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String orgList = ajaxHelper.getOrgListForAll(orgType);

		logger.debug(orgList);
		response.setContentType("text/html");
		out.print(orgList);
	}

	public void getBoAgtList() throws Exception {
		logger.info("orgType = " + orgType);
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		// String orgList = ajaxHelper.getBoAgtList(orgType);
		List<OrganizationBean> organizationList = ajaxHelper
				.getBoAgtList(orgType);
		String orgList = "";
		for (OrganizationBean bean : organizationList) {
			orgList += bean.getOrgId() + "|" + bean.getOrgName() + ":";
		}
		logger.info(orgList);
		response.setContentType("text/html");
		out.print(orgList);
	}

	public void getCountryListWithCode() throws Exception {
		logger.info("Get Country List With Country Code ");
		PrintWriter out = getResponse().getWriter();
		String orgList = AjaxRequestHelper.getCountryListWithCode();
		logger.info(orgList);
		response.setContentType("text/html");
		out.print(orgList);

	}

	public void getCityList() {
		try {
			response.getWriter().write(
					AjaxRequestHelper.getCityAndStateBuilder());
		} catch (IOException e) {
			logger.error("EXCEPTION :- ", e);
		}
		return;
	}

	public void getZoneList() {
		try {
			response.getWriter().write(
					AjaxRequestHelper.getZoneAndCodeBuilder(cityName.trim()));
		} catch (IOException e) {
			logger.error("EXCEPTION :- ", e);
		}
		return;
	}

	public void getOrgListForArea() throws Exception {
		logger.debug("orgType = " + orgType);
		PrintWriter out = getResponse().getWriter();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String orgList = ajaxHelper.getOrgListForArea(orgType, state, city,
				area);

		logger.debug(orgList);
		response.setContentType("text/html");
		out.print(orgList);

	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public void setErrTime(String errTime) {
		this.errTime = errTime;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public void setRetType(String retType) {
		this.retType = retType;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public String getRetName() {
		return retName;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getState() {
		return state;
	}

	public String getCity() {
		return city;
	}

	public String getArea() {
		return area;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	

	public String getRetOrgName() {
		return retOrgName;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void getOrganizationNameIdListStateAndCityWise() throws IOException, LMSException {
		logger.debug("orgType = " + orgType);
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		int userOrgId = 0;
		if (uib != null) {
			userOrgId = uib.getUserOrgId();
		}

		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		String agtOrgType = ajaxHelper.getOrgIdListStateAndCityWise(userOrgId, orgType, state, city);

		logger.debug(agtOrgType);
		response.setContentType("text/html");
		out.print(agtOrgType);
	}
	
}
