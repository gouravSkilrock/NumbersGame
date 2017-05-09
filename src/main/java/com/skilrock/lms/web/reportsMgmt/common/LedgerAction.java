package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LedgerHelper;

/**
 * This class is for generation of Agent and Back Office ledger.
 * 
 * @author Skilrock Technologies
 * 
 */
public class LedgerAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accountName;
	private String accountType;
	String addQuery = null;
	private String agentLedger;
	private String agentName;
	String boAddQuery = null;
	Calendar calendar = Calendar.getInstance();
	String consolidatedAcct = null;
	String consolidatedDay = null;
	String consolidatedDetailed = null;
	List consolidatedList = null;
	Date date5 = null;
	SimpleDateFormat dateformat = null;
	Date dateFrDtParse = null;
	Date dateToDtParse = null;
	Timestamp dt = null;
	String file = null;
	String formatString = null;
	private Date frDate;
	private String fromDate;
	Timestamp fromTimeStamp = null;
	GraphReportHelper graphHelper = new GraphReportHelper();
	// LedgerHelper ledgerHelper=new LedgerHelper();
	LedgerHelper ledger = null;

	private String ledgerType;

	Log logger = LogFactory.getLog(LedgerAction.class);
	String orgAdd = null;
	String orgName = null;
	HashMap parameterMap = null;
	byte[] path = null;
	String query = null;
	String rcptType = "";
	/*
	 * These were the duplicate variable which have been moved to global
	 * variables 07-03-
	 */
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String retailerName;
	String rootPath = null;
	private List selectedGames;
	private List selectGames;
	private String selectMonth;
	HttpSession session = null;
	private String showAgtLedger;
	private Date tDate;
	private String toDate;
	private String type;
	UserInfoBean userBean = null;
	boolean isReceiptWise = true;

	public String adhocUpdateLedger() {
		ServletContext sc = ServletActionContext.getServletContext();
		Boolean isAdhocRunning = (Boolean) sc.getAttribute("IS_ADHOC_UPDATE");

		if (isAdhocRunning == null || isAdhocRunning == false) {
			sc.setAttribute("IS_ADHOC_UPDATE", true);
			LedgerHelper ledgerHelper = new LedgerHelper();
			request.setAttribute("code", "MGMT");
			request.setAttribute("interfaceType", "WEB");
			ledgerHelper.adhocUpdateClmBal();
			sc.setAttribute("IS_ADHOC_UPDATE", false);

		}
		return SUCCESS;
	}

	/**
	 * This method is for entering the data into agent ledger.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String agentLedger() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		boolean isReceiptWise = Boolean.parseBoolean((String) sc
				.getAttribute("IS_RECEIPT_WISE"));
		if (isReceiptWise) {
			rcptType = "Rcpt";
		}
		ledger = new LedgerHelper();
		int userOrgId = 0;
		String backOffName = null;
		parameterMap = new HashMap();
		// Timestamp dt =null;
		// Timestamp fromTimeStamp=
		// Timestamp.valueOf(dateformat.format(getFromDate()));

		session = request.getSession();
		// (String)session.getAttribute("date_format");
		formatString = (String) session.getAttribute("date_format");

		dateformat = new SimpleDateFormat(formatString);
		dateFrDtParse = dateformat.parse(getFromDate());
		fromTimeStamp = new Timestamp(dateFrDtParse.getTime());
		// LedgerHelper ledger = new LedgerHelper();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String textForTax = (String) ServletActionContext.getServletContext()
				.getAttribute("TEXT_FOR_TAX");
		if (getAgentName() == null) {
			userOrgId = userBean.getUserOrgId();
		} else {
			userOrgId = ledger.id(getAgentName());
		}
		logger.debug("User Org Id" + userOrgId);
		
		logger.debug("getLedgerType" + getLedgerType() + "getAccountType"
				+ getAccountType() + "getFromDate" + getFromDate());
		
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// Date dt = null;
		logger.debug("Formatted From Date " + getFromDate());
		
		accountType = getAccountType();
		rootPath = (String) session.getAttribute("ROOT_PATH");
		rootPath = rootPath.replace("\\", "/");
		logger.debug("Formatted From Date " + getFromDate() + "  to date --"
				+ getToDate());
		
		if (accountType != null) {
			if (accountType.equals("AGNT_BANK_ACC")) {
				accountName = "Bank Receipts & Payments";

			} else if (accountType.equals("AGNT_PWT_PAY")) {
				accountName = "PWT Payable ";
			} else if (accountType.equals("AGNT_PWT_RCV")) {
				accountName = "PWT Receivable ";
			} else if (accountType.equals("AGNT_SALE_ACC")) {
				accountName = "Sale";
			} else if (accountType.equals("AGNT_SALE_RET_ACC")) {
				accountName = "Sale Return ";
			}

			else if (accountType.equals("AGNT_PRCHSE_ACC")) {
				accountName = "Purchase ";
			} else if (accountType.equals("AGNT_PURCHS_RET_ACC")) {
				accountName = "Purchase Return ";
			} else if (accountType.equals("AGNT_PWT_CHARGES_RCV")) {
				accountName = "PWT Charges Receivable";
			} else if (accountType.equals("AGENT_PWT_CHARGES")) {
				accountName = "PWT Collection Charges ";
			} else if (accountType.equals("AGENT_VAT_PAY")) {
				accountName = "VAT Payable ";
			} else if (accountType.equals("AGNT_PLAYER_CAS")) {
				accountName = "Player Cash ";
			} else if (accountType.equals("AGNT_PLAYER_PWT")) {
				accountName = "Player PWT ";
			} else if (accountType.equals("AGNT_PLAYER_TDS")) {
				accountName = "Player " + textForTax;
			}

		}
		addQuery = QueryManager.getST6AddressQuery();
		orgAdd = ledger.getAddress(addQuery, "" + userOrgId, null);

		orgName = userBean.getOrgName();
		if (getAgentName() != null) {
			orgName = getAgentName();
			parameterMap.put("agtLgrAtBo", "To Be Viewed At Back Office ");

		}
		parameterMap.put("agentComp", getAgentName());
		logger.debug("getAgentName******" + getAgentName());

		if (!getToDate().equals("")) {
			dateToDtParse = dateformat.parse(getToDate());
			dt = new Timestamp(dateToDtParse.getTime() + 1000 * 60 * 60 * 24l);
			
		} else {
			date5 = new Date();
			dt = new Timestamp(date5.getTime());
			toDate = dateformat.format(new Date());
		}
		logger.debug(dt);
		if (!ledgerType.equalsIgnoreCase("Retailer Self")) {
			ledger.ledgerAgentEntry(dt, userOrgId);
		}
		frDate = dateformat.parse(fromDate);
		tDate = dateformat.parse(toDate);
		parameterMap.put("orgName", orgName);
		parameterMap.put("orgAdd", orgAdd);
		parameterMap.put("from_date", frDate);
		parameterMap.put("to_date", tDate);
		parameterMap.put("formatString", formatString);
		logger.debug("FROM DATE   " + fromDate + " and TO DATE   " + toDate);
		if (ledgerType.equalsIgnoreCase("Accountwise")) {
			backOffName = ledger.getParentOrgName(userOrgId);
			query = QueryManager.getST6AccountWiseLedgerAgt();
			parameterMap.put("type", "Accountwise");
			parameterMap.put("bo", accountName);
			parameterMap.put("backOffName", backOffName);
			logger.debug(query
					+ "  Account wise *****************  Agent ledger");
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt + " userId " + userOrgId);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/agentLedgerAccountWise.jasper";
			path = graphHelper.generateReport(query, file, parameterMap,
					accountType, fromTimeStamp, dt, "" + userOrgId);
		} else if (ledgerType.equalsIgnoreCase("RetailerWise")) {
			parameterMap.put("type", "Retailerwise");
			parameterMap.put("bo", getRetailerName());
			query = QueryManager.getST6RetWiseLedgerAgt(isReceiptWise);
			logger.debug(query
					+ "  Retailer wise *****************  Agent ledger");
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt + " userId " + userOrgId);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/agentLedgerRetWise"
					+ rcptType + ".jasper";
			path = graphHelper.generateReport(query, file, parameterMap,
					getRetailerName(), fromTimeStamp, dt, null);
		}

		else if (ledgerType.equalsIgnoreCase("All")) {
			parameterMap.put("type", "Journal");
			query = QueryManager.getST6JournalLedgerAgt();
			logger.debug(query + "  All *****************  Agent ledger");
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt + " userId " + userOrgId);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/agentledgernew.jasper";
			path = graphHelper.generateReport(query, file, parameterMap, null,
					fromTimeStamp, dt, "" + userOrgId);
		} else if (ledgerType.equalsIgnoreCase("Agent Self")) {
			boLedger();
		} else if (ledgerType.equalsIgnoreCase("Retailer Self")) {
			String retOrgName = orgName;
			parameterMap.put("bo", retOrgName);
			String agtAddressQuery = "select addr_line1,addr_line2,city,st_lms_state_master.name,st_lms_country_master.name from st_lms_organization_master,st_lms_state_master,st_lms_country_master where st_lms_organization_master.organization_id= (select parent_id from st_lms_organization_master where organization_id=?)and st_lms_organization_master.country_code=st_lms_country_master.country_code and  st_lms_organization_master.state_code=st_lms_state_master.state_code ";
			orgAdd = ledger.getAddress(agtAddressQuery, userOrgId + "", null);
			String agtOrgName = ledger.getParentOrgName(userOrgId);
			int parentOrgId = ledger.getParentOrgId(userOrgId);
			ledger.ledgerAgentEntry(dt, parentOrgId);
			logger.debug("Retailer Self ++++++++ " + orgAdd + " agtOrgName "
					+ agtOrgName + " parentOrgId " + parentOrgId);
			parameterMap.put("orgAdd", orgAdd);
			parameterMap.put("orgName", agtOrgName);
			query = QueryManager.getST6RetWiseLedgerAgt(isReceiptWise);
			logger.debug(query
					+ "  Retailer wise *****************  Agent ledger");
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt + " userId " + userOrgId);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/agentLedgerRetWise"
					+ rcptType + ".jasper";
			path = graphHelper.generateReport(query, file, parameterMap,
					retOrgName, fromTimeStamp, dt, null);
		} else if (ledgerType.equalsIgnoreCase("consolidated")) {
			consolidatedList = new ArrayList();
			consolidatedList.add(frDate);// 1
			consolidatedList.add(tDate);// 2
			consolidatedList.add(orgName);// 3
			consolidatedList.add(formatString);// 4

			query = QueryManager.getST6RetWiseLedgerAgt(isReceiptWise);
			logger.debug(" Agent Ret Wise Query" + query);
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt);
			consolidatedList = graphHelper.generateConsolidatedReportAgt(query,
					consolidatedList, getRetailerName(), fromTimeStamp, dt);
			consolidatedList.add(getRetailerName()); // 13 15
			session.setAttribute("CONSOLIDATED_LED_LIST", consolidatedList);
			return "consolidated";
		}
		logger.debug("In Agent Ledger");
		return generatePDF(path);

	}

	/**
	 * This method is used for fetching data for ajax request from agent ledger
	 * page
	 * 
	 * @throws Exception
	 */

	public void agentLedgerAjax() throws Exception {
		LedgerHelper ledgerHelper = new LedgerHelper();
		PrintWriter out = getResponse().getWriter();
		String ledger = getLedgerType();
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String textForTax = (String) ServletActionContext.getServletContext()
				.getAttribute("TEXT_FOR_TAX");
		String vat = (String) ServletActionContext.getServletContext()
				.getAttribute("VAT_APPLICABLE");

		int id = 0;
		if (getAgentName() == null) {
			id = userBean.getUserOrgId();
		} else {
			id = ledgerHelper.id(getAgentName());
		}
		logger.debug("Agent Name is ----" + getAgentName());
		ArrayList selectedList = ledgerHelper.getList(ledger, "agent", id);
		logger.debug("Ledger agent ajax" + selectedList);
		String html = "";
		if (ledgerType.equals("Accountwise")) {
			if (vat.trim().equalsIgnoreCase("yes")) {
				html = "Select Account: <select class=\"option\" name=\"accountType\"><OPTION VALUE=-1>--Please Select--<OPTION VALUE=\"AGNT_BANK_ACC\">Bank Receipts & Payments Account<OPTION VALUE=\"AGNT_PLAYER_CAS\">Net Paid PWT To Player<OPTION VALUE=\"AGNT_PLAYER_PWT\">Player PWT	<OPTION VALUE=\"AGNT_PLAYER_TDS\">Player "
						+ textForTax
						+ "<OPTION VALUE=\"AGNT_PWT_PAY\">PWT Payable<OPTION VALUE=\"AGNT_PWT_RCV\">PWT Receivable<OPTION VALUE=\"AGNT_SALE_ACC\">Sale Account<OPTION class=\"option\" VALUE=\"AGNT_SALE_RET_ACC\">Sale Return Account	<OPTION class=\"option\" VALUE=\"AGNT_PWT_CHARGES_RCV\">PWT Charges Receivable<OPTION class=\"option\" VALUE=\"AGENT_PWT_CHARGES\">PWT Collection Charges<OPTION VALUE=\"AGENT_VAT_PAY\">VAT Payable<OPTION class=\"option\" VALUE=\"AGNT_PRCHSE_ACC\">Purchase<OPTION class=\"option\" VALUE=\"AGNT_PURCHS_RET_ACC\">Purchase Return</SELECT>";
			} else {
				html = "Select Account: <select class=\"option\" name=\"accountType\"><OPTION VALUE=-1>--Please Select--<OPTION VALUE=\"AGNT_BANK_ACC\">Bank Receipts & Payments Account<OPTION VALUE=\"AGNT_PLAYER_CAS\">Net Paid PWT To Player<OPTION VALUE=\"AGNT_PLAYER_PWT\">Player PWT	<OPTION VALUE=\"AGNT_PLAYER_TDS\">Player "
						+ textForTax
						+ "<OPTION VALUE=\"AGNT_PWT_PAY\">PWT Payable<OPTION VALUE=\"AGNT_PWT_RCV\">PWT Receivable<OPTION VALUE=\"AGNT_SALE_ACC\">Sale Account<OPTION class=\"option\" VALUE=\"AGNT_SALE_RET_ACC\">Sale Return Account	<OPTION class=\"option\" VALUE=\"AGNT_PWT_CHARGES_RCV\">PWT Charges Receivable<OPTION class=\"option\" VALUE=\"AGENT_PWT_CHARGES\">PWT Collection Charges<OPTION class=\"option\" VALUE=\"AGNT_PRCHSE_ACC\">Purchase<OPTION class=\"option\" VALUE=\"AGNT_PURCHS_RET_ACC\">Purchase Return</SELECT>";
			}
			logger.debug("htmlllllllllllll" + html);
		} else if (ledgerType.equals("Retailerwise")
				|| ledgerType.equals("consolidated")) {
			html = "Select "
					+ ((Map<String, String>) ServletActionContext
							.getServletContext().getAttribute("TIER_MAP"))
							.get("RETAILER")
					+ ": <select class=\"option\" name=\"retailerName\"><OPTION VALUE=-1>--Please Select--";
			int i = 0;
			for (Iterator it = selectedList.iterator(); it.hasNext();) {
				String name = (String) it.next();
				i++;
				html += "<option class=\"option\" value=\"" + name + "\">"
						+ name + "</option>";
			}
			html += "</select>";
		}
		response.setContentType("text/html");

		out.print(html);
	}

	/**
	 * This method is for entering the data into back office ledger.
	 * 
	 * @return String
	 * @throws Exception
	 */

	public String boLedger() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		boolean isReceiptWise = Boolean.parseBoolean((String) sc
				.getAttribute("IS_RECEIPT_WISE"));
		if (isReceiptWise) {
			rcptType = "Rcpt";
		}
		parameterMap = new HashMap();
		logger.info("Inside boLedger");
		ledger = new LedgerHelper();
		session = request.getSession();
		formatString = (String) session.getAttribute("date_format");
		dateformat = new SimpleDateFormat(formatString);
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String textForTax = (String) ServletActionContext.getServletContext()
				.getAttribute("TEXT_FOR_TAX");
		int id = userBean.getUserOrgId();
		addQuery = QueryManager.getST6AddressQuery();
		orgAdd = ledger.getAddress(addQuery, "" + id, null);
		orgName = userBean.getOrgName();

		logger.debug("Ledger Type--->" + getLedgerType()
				+ " Account Type----->" + getAccountType() + "Agent Name--"
				+ getAgentName());
		logger.debug("From Date--->" + getFromDate() + "  To Date --->"
				+ getToDate());
		dateFrDtParse = dateformat.parse(getFromDate());
		fromTimeStamp = new Timestamp(dateFrDtParse.getTime());
		logger.debug("Formatted From Date " + getFromDate() + "  to date --"
				+ getToDate());
		accountType = getAccountType();
		if (accountType != null) {
			if (accountType.equals("BANK_ACC")) {
				accountName = "Bank Receipts & Payments";

			} else if (accountType.equals("SALE_ACC")) {
				accountName = "Sale";
			} else if (accountType.equals("SALE_RET")) {
				accountName = "Sale Return ";
			} else if (accountType.equals("PLAYER_CAS")) {
				accountName = "Player Cash ";
			} else if (accountType.equals("PLAYER_PWT")) {
				accountName = "Player PWT ";
			} else if (accountType.equals("PLAYER_TDS")) {
				accountName = "Player " + textForTax;
			} else if (accountType.equals("PWT_PAY")) {
				accountName = "PWT Payable ";
			} else if (accountType.equals("TDS_PAY")) {
				accountName = textForTax + " Payable ";
			} else if (accountType.equals("UNCLM_GOVT")) {
				accountName = "Unclaimed to Government ";
			} else if (accountType.equals("GOVT_COMM")) {
				accountName = "Good Cause ";
			} else if (accountType.equals("VAT_PAY")) {
				accountName = "VAT Payable ";
			} else if (accountType.equals("PWT_CHARGES")) {
				accountName = "PWT Collection Charges ";
			}

		}

		if (!getToDate().equals("")) {
			dateToDtParse = dateformat.parse(getToDate());
			calendar.setTime(dateToDtParse);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			dateToDtParse = calendar.getTime();
			dt = new Timestamp(dateToDtParse.getTime());
			logger.debug("To date" + dt);

		} else {

			Calendar calendar2 = Calendar.getInstance();

			calendar
					.set(calendar2.get(Calendar.YEAR), calendar2
							.get(Calendar.MONTH), calendar2.get(Calendar.DATE),
							0, 0, 0);

			calendar.add(Calendar.DAY_OF_MONTH, 1);

			date5 = calendar.getTime();

			dt = new Timestamp(date5.getTime());
			logger.debug(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");
			toDate = dateformat.format(new Date());
		}
		ledger.ledgerBoEntry(dt);
		frDate = dateformat.parse(fromDate);
		tDate = dateformat.parse(toDate);
		parameterMap.put("from_date", frDate);
		parameterMap.put("to_date", tDate);
		parameterMap.put("orgName", orgName);
		parameterMap.put("orgAdd", orgAdd);
		parameterMap.put("formatString", formatString);

		rootPath = (String) session.getAttribute("ROOT_PATH");

		rootPath = rootPath.replace("\\", "/");
		logger.debug("FROM DATE   " + fromDate + " and TO DATE   " + toDate);
		if (ledgerType.equalsIgnoreCase("Accountwise")) {
			parameterMap.put("type", "Accountwise");
			parameterMap.put("bo", accountName);
			query = QueryManager.getST6AccountWiseLedgerBO();
			logger.debug("Bo Account Wise Query" + query);
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/boLedgerAccountWise.jasper";
			path = graphHelper.generateReport(query, file, parameterMap,
					accountType, fromTimeStamp, dt, null);
		} else if (ledgerType.equalsIgnoreCase("Agentwise")) {
			logger.debug("agtwise++++++++" + orgAdd);
			parameterMap.put("type", "Agentwise");
			parameterMap.put("bo", getAgentName());
			parameterMap.put("orgName", orgName);
			query = QueryManager.getST6AgentWiseLedgerBO(isReceiptWise);
			logger.debug("Bo Agent Wise Query" + query);
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/boLedgerAgentWise"
					+ rcptType + ".jasper";
			path = graphHelper.generateReport(query, file, parameterMap,
					getAgentName(), fromTimeStamp, dt, null);
		}

		else if (ledgerType.equalsIgnoreCase("All")) {
			parameterMap.put("type", "Journal");
			query = QueryManager.getST6JournalLedgerBO();
			logger.debug("Bo All Query" + query);
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/ledger.jasper";
			path = graphHelper.generateReport(query, file, parameterMap, null,
					fromTimeStamp, dt, null);
		} else if (ledgerType.equalsIgnoreCase("Agent Self")) {

			boAddQuery = "select addr_line1,addr_line2,city,st_lms_state_master.name,st_lms_country_master.name from st_lms_organization_master,st_lms_state_master,st_lms_country_master where st_lms_organization_master.organization_id= (select parent_id from st_lms_organization_master where organization_id=?)and st_lms_organization_master.country_code=st_lms_country_master.country_code and  st_lms_organization_master.state_code=st_lms_state_master.state_code ";
			orgAdd = ledger.getAddress(boAddQuery, id + "", null);
			String backOffName = ledger.getParentOrgName(id);
			logger.debug("agtwise++++++++" + orgAdd);
			parameterMap.put("orgAdd", orgAdd);
			parameterMap.put("bo", orgName);
			parameterMap.put("orgName", backOffName);
			query = QueryManager.getST6AgentWiseLedgerBO(isReceiptWise);
			logger.debug("Bo Agent Wise Query" + query);
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt);
			file = rootPath
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/boLedgerAgentWise"
					+ rcptType + ".jasper";
			path = graphHelper.generateReport(query, file, parameterMap,
					orgName, fromTimeStamp, dt, null);
		}

		else if (ledgerType.equalsIgnoreCase("consolidated")) {
			consolidatedList = new ArrayList();
			logger.debug("agtwise++++++++" + orgAdd);
			consolidatedList.add(frDate);// 1
			consolidatedList.add(tDate);// 2
			consolidatedList.add(orgName);// 3
			consolidatedList.add(formatString);// 4

			query = QueryManager.getST6AgentWiseLedgerBO(isReceiptWise);
			logger.debug("Bo Agent Wise Query" + query);
			logger.debug("accountType " + accountType + " fromTimeStamp "
					+ fromTimeStamp + " dt " + dt);
			consolidatedList = graphHelper.generateConsolidatedReport(query,
					consolidatedList, getAgentName(), fromTimeStamp, dt);
			consolidatedList.add(getAgentName()); // 13 15
			session.setAttribute("CONSOLIDATED_LED_LIST", consolidatedList);
			return "consolidated";
		}
		return generatePDF(path);
	}

	/**
	 * This method is used for fetching data for ajax request from back office
	 * ledger page
	 * 
	 * @throws Exception
	 */

	public void boLedgerAjax() throws Exception {
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		LedgerHelper ledgerHelper = new LedgerHelper();
		String textForTax = (String) ServletActionContext.getServletContext()
				.getAttribute("TEXT_FOR_TAX");
		String vat = (String) ServletActionContext.getServletContext()
				.getAttribute("VAT_APPLICABLE");

		int id = userBean.getUserOrgId();
		PrintWriter out = getResponse().getWriter();
		String ledger = getLedgerType();

		ArrayList selectedList = ledgerHelper.getList(ledger, "BO", id);
		logger.debug("Ledger BO ajax" + selectedList);
		String html = "";
		if (ledgerType.equals("Accountwise")) {
			if (vat.trim().equalsIgnoreCase("yes")) {
				html = "Select Account: <select class=\"option\" name=\"accountType\"><OPTION VALUE=-1>--Please Select--<OPTION VALUE=\"BANK_ACC\">Bank Receipts & Payments Account<OPTION VALUE=\"GOVT_COMM\">Good Cause<OPTION VALUE=\"PLAYER_CAS\">Net Paid PWT To Player<OPTION VALUE=\"PLAYER_PWT\">Player PWT	<OPTION VALUE=\"PLAYER_TDS\">Player "
						+ textForTax
						+ "<OPTION VALUE=\"PWT_PAY\">PWT Payable<OPTION VALUE=\"SALE_ACC\">Sale Account<OPTION VALUE=\"SALE_RET\">Sale Return Account<OPTION VALUE=\"PWT_CHARGES\">PWT Collection Charges<OPTION VALUE=\"VAT_PAY\">VAT Payable<OPTION class=\"option\" VALUE=\"TDS_PAY\">"
						+ textForTax + "</SELECT>";
			} else {
				html = "Select Account: <select class=\"option\" name=\"accountType\"><OPTION VALUE=-1>--Please Select--<OPTION VALUE=\"BANK_ACC\">Bank Receipts & Payments Account<OPTION VALUE=\"GOVT_COMM\">Good Cause<OPTION VALUE=\"PLAYER_CAS\">Net Paid PWT To Player<OPTION VALUE=\"PLAYER_PWT\">Player PWT	<OPTION VALUE=\"PLAYER_TDS\">Player "
						+ textForTax
						+ "<OPTION VALUE=\"PWT_PAY\">PWT Payable<OPTION VALUE=\"SALE_ACC\">Sale Account<OPTION VALUE=\"SALE_RET\">Sale Return Account<OPTION VALUE=\"PWT_CHARGES\">PWT Collection Charges<OPTION class=\"option\" VALUE=\"TDS_PAY\">"
						+ textForTax + "</SELECT>";
			}
		} else if (ledgerType.equals("Agentwise")
				|| ledgerType.equals("consolidated")) {
			html = "Select "
					+ ((Map<String, String>) ServletActionContext
							.getServletContext().getAttribute("TIER_MAP"))
							.get("AGENT")
					+ ": <select class=\"option\" name=\"agentName\"><OPTION VALUE=-1>--Please Select--";

			int i = 0;
			for (Iterator it = selectedList.iterator(); it.hasNext();) {
				String name = (String) it.next();
				i++;
				html += "<option class=\"option\" value=\"" + name + "\">"
						+ name + "</option>";
			}
			html += "</select>";
		}
		response.setContentType("text/html");
		out.print(html);
	}

	public String displayLedger() {

		return SUCCESS;
	}

	/**
	 * This method is for Generating the PDF Reports
	 * 
	 * @param data
	 *            Byte Array generated by Jasper Compiler
	 * @return String
	 * @throws Exception
	 */
	public String generatePDF(byte[] data) throws Exception {
		logger.info("In PDF Generation in Ledger Action");
		int lengthOfFile = 0;
		try {

			lengthOfFile = data.length;
			if (lengthOfFile > 800) {
				response.setContentType("application/pdf");
				OutputStream OutStrm = response.getOutputStream();
				logger.debug("Written data to stream: " + data);
				OutStrm.write(data);
				// response.sendError (response.SC_UNSUPPORTED_MEDIA_TYPE);

				// response.SC_UNSUPPORTED_MEDIA_TYPE;
				OutStrm.flush();
				OutStrm.close();
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * PrintWriter out = getResponse().getWriter();
		 * response.setContentType("application/pdf"); out.print(data);
		 * out.flush();
		 */
		/*
		 * if(lengthOfFile>0){ return null; }
		 */
		return SUCCESS;
	}

	public String getAccountType() {
		return accountType;
	}

	/*
	 * public void generatePDF(byte [] data)throws Exception{ PrintWriter out =
	 * getResponse().getWriter(); response.setContentType("application/pdf");
	 * out.print(data); }
	 */

	public String getAgentLedger() {
		return agentLedger;
	}

	public String getAgentName() {
		return agentName;
	}

	public String getConsolidatedAcct() {
		return consolidatedAcct;
	}

	public String getConsolidatedDay() {
		return consolidatedDay;
	}

	public String getConsolidatedDetailed() {
		return consolidatedDetailed;
	}

	public List getConsolidatedList() {
		return consolidatedList;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getLedgerType() {
		return ledgerType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public List getSelectedGames() {
		return selectedGames;
	}

	public List getSelectGames() {
		return selectGames;
	}

	public String getSelectMonth() {
		return selectMonth;
	}

	public String getShowAgtLedger() {
		return showAgtLedger;
	}

	public String getToDate() {
		return toDate;
	}

	public String getType() {
		return type;
	}

	public String retLedger() throws Exception {

		if (ledgerType.equalsIgnoreCase("Retailer Self")) {
			agentLedger();

		}
		logger.debug("In Ret Ledger");
		return generatePDF(path);

	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setAgentLedger(String agentLedger) {
		this.agentLedger = agentLedger;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public void setConsolidatedAcct(String consolidatedAcct) {
		this.consolidatedAcct = consolidatedAcct;
	}

	public void setConsolidatedDay(String consolidatedDay) {
		this.consolidatedDay = consolidatedDay;
	}

	public void setConsolidatedDetailed(String consolidatedDetailed) {
		this.consolidatedDetailed = consolidatedDetailed;
	}

	public void setConsolidatedList(List consolidatedList) {
		this.consolidatedList = consolidatedList;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setLedgerType(String ledgerType) {
		this.ledgerType = ledgerType;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public void setSelectedGames(List selectedGames) {
		this.selectedGames = selectedGames;
	}

	public void setSelectGames(List selectGames) {
		this.selectGames = selectGames;
	}

	public void setSelectMonth(String selectMonth) {
		this.selectMonth = selectMonth;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setShowAgtLedger(String showAgtLedger) {
		this.showAgtLedger = showAgtLedger;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setType(String type) {
		this.type = type;
	}

}