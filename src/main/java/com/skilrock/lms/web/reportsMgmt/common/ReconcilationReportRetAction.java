package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LedgerHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ReconcilationReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ReconcilationReportRetHelper;

public class ReconcilationReportRetAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws SQLException, ParseException {
		ReconcilationReportRetAction rra = new ReconcilationReportRetAction();
		// rra.createReport();
	}

	private String agentName;
	Map<Integer, String> agentOrgIdMap;
	// private int orgMap;
	int agentOrgMap;
	Calendar calendar = Calendar.getInstance();
	Date date5 = null;
	SimpleDateFormat dateformat1 = null;
	Date dateFrDtParse = null;
	Date dateToDtParse = null;
	Timestamp dt = null;
	private String fromDate = null;
	Log logger = LogFactory.getLog(ReconcilationReportRetAction.class);
	Map<Integer, String> orgMapRet;
	private String reconRepType = null;
	private String reconViewType = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String retailerName = null;
	int retOrgMap;
	Timestamp startDate = null;

	private String toDate = null;

	public String createReport() throws LMSException {
		logger
				.info("============= inside ret action =========== getReconRepType() "
						+ getReconRepType() + " " + reconRepType);
		// System.out.println("============= inside ret action ===========
		// getReconRepType() "+getReconRepType() +" "+reconRepType);
		HttpSession session = getRequest().getSession();
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		String reconReportType = null;

		if ("Self".equalsIgnoreCase(getReconRepType())
				&& "detailed".equalsIgnoreCase(getReconViewType())) {
			logger.debug("reconRepType   ========= " + getReconRepType());
			// System.out.println("reconRepType ========= "+getReconRepType());
			ReconcilationReportAction reconcilationReportAction = new ReconcilationReportAction();
			reconcilationReportAction.setToDate(toDate);
			reconcilationReportAction.setFromDate(fromDate);
			reconcilationReportAction.setAgentOrgMap(agentOrgMap);

			String status = reconcilationReportAction.createReport(session,
					servletContext);
			logger.debug("=========status+Agt=========   " + status + "Agt");
			// System.out.println("=========status+Agt========= "+status+"Agt");
			if (agentOrgIdMap == null) {
				agentOrgIdMap = (Map) session.getAttribute("agentOrgIdMap");
			}
			setAgentName(agentOrgIdMap.get(agentOrgMap));

			return status + "Agt";
		} else {
			logger.debug("=========== createReport for RETAILER=========");
			logger
					.debug("============= inside ret action =========== getReconRepType() "
							+ getReconRepType() + " " + reconRepType);
			// System.out.println("=========== createReport for
			// RETAILER=========");
			// System.out.println("============= inside ret action ===========
			// getReconRepType() "+getReconRepType() +" "+reconRepType);

			UserInfoBean userBean;

			String formatString = (String) session.getAttribute("date_format");
			dateformat1 = new SimpleDateFormat(formatString);
			int userOrgId = 0;
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			try {
				String userType = userBean.getUserType();
				logger.debug("userType   +++=== " + userType + "  getToDate "
						+ getToDate());
				// System.out.println("userType +++=== "+userType+" getToDate
				// "+getToDate());
				if (userType.equalsIgnoreCase("AGENT")) {
					userOrgId = userBean.getUserOrgId();
				} else if (userType.equalsIgnoreCase("RETAILER")) {
					userOrgId = userBean.getParentOrgId();
				}
				dateFrDtParse = dateformat1.parse(getFromDate());
				startDate = new Timestamp(dateFrDtParse.getTime());

				if (!getToDate().equalsIgnoreCase("")) {

					dateToDtParse = dateformat1.parse(getToDate());
					calendar.setTime(dateToDtParse);
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					dateToDtParse = calendar.getTime();
					dt = new Timestamp(dateToDtParse.getTime());
					logger.debug(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 1111111111");
					// System.out
					// .println(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 1111111111");
					Calendar calendart = Calendar.getInstance();
					calendart.setTimeInMillis(dt.getTime());
					Calendar calendarnew = Calendar.getInstance();
					if (calendarnew.before(calendart)) {

						dt = new Timestamp(new Date().getTime());
					}

				} else {

					Calendar calendar2 = Calendar.getInstance();

					calendar.set(calendar2.get(Calendar.YEAR), calendar2
							.get(Calendar.MONTH), calendar2.get(Calendar.DATE),
							0, 0, 0);
					calendar.add(Calendar.DAY_OF_MONTH, 1);

					date5 = calendar.getTime();

					dt = new Timestamp(date5.getTime());
					logger.debug(dt
							+ "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");

					// System.out.println(dt
					// + "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");
				}

				LedgerHelper ledgerHelp = new LedgerHelper();
				ledgerHelp.ledgerAgentEntry(dt, userOrgId);

				ReconcilationReportRetHelper rrh = new ReconcilationReportRetHelper();
				logger.debug("dateformat1 " + dateformat1);
				logger.debug(" String date "
						+ (String) ServletActionContext.getServletContext()
								.getAttribute("DEPLOYMENT_DATE"));
				logger.debug("new java.sql.Date(fromTimeStamp.getTime()) "
						+ startDate);
				logger.debug("********************8  "
						+ new java.sql.Date(dateformat1.parse(
								(String) ServletActionContext
										.getServletContext().getAttribute(
												"DEPLOYMENT_DATE")).getTime())
						+ "   " + startDate);
				// System.out.println("dateformat1 "+dateformat1 );
				// System.out.println(" String date
				// "+(String)ServletActionContext.getServletContext().getAttribute("DEPLOYMENT_DATE"));
				// System.out.println("new
				// java.sql.Date(fromTimeStamp.getTime()) "+startDate);
				// System.out.println("********************8 "+new
				// java.sql.Date(dateformat1.parse((String)ServletActionContext.getServletContext().getAttribute("DEPLOYMENT_DATE")).getTime())+"
				// "+startDate);

				ReconcilationReportHelper reconcilationReportHelper = new ReconcilationReportHelper();
				Map agentOrgIdMap = reconcilationReportHelper
						.getAgentOrgMap(dt);
				Iterator agtOrgitr = agentOrgIdMap.entrySet().iterator();
				while (agtOrgitr.hasNext()) {
					Map.Entry<Integer, String> agtOrgpair = (Map.Entry<Integer, String>) agtOrgitr
							.next();
					int agtOrgId = agtOrgpair.getKey();
					reconReportType = reconcilationReportHelper
							.getReconRepType(agtOrgId);
					logger.debug(" recon typeeeeeeeee reconReportType "
							+ reconReportType + " agtOrgId " + agtOrgId
							+ " getAgentOrgMap " + getAgentOrgMap());
					// System.out.println(" recon typeeeeeeeee reconReportType
					// "+reconReportType +" agtOrgId "+agtOrgId+" getAgentOrgMap
					// "+getAgentOrgMap());
					reconcilationReportHelper.reconAgentTicketwiseEntry(
							agtOrgId, dt, "Ticket Wise Report");
					reconcilationReportHelper.reconAgentBookwiseEntry(agtOrgId,
							dt, "Book Wise Report");

				}
				Map retOrgIdMap = rrh.getRetOrgMap(userOrgId, dt);

				if ("detailed".equalsIgnoreCase(getReconViewType())) {
					double bal = rrh.getPreviousBalance(getRetOrgMap(),
							startDate, "userOrgId");
					logger.debug("Balance: " + bal);
					// System.out.println(bal+"!!!!!!!!!!!!!!!!!!!11111111111");
					session.setAttribute("PREVIOUS_BAL", bal);
					List list1 = new ReconcilationReportRetHelper()
							.CreateReport(getRetOrgMap(), startDate, dt,
									dateformat1);
					List listAftrlstPmnt = rrh.getDetailsAftrLstPmnt(
							getRetOrgMap(), startDate, dt, dateformat1);
					logger.debug(" List Size: " + list1.size());
					// System.out.println(" ^^^^^^^^^^"+list1.size());
					session.setAttribute("RECON_LIST", list1);
					session.setAttribute("RECON_AFTRLST_PMNT", listAftrlstPmnt);
					logger.debug("Recon List: "
							+ session.getAttribute("RECON_LIST"));
					// System.out.println(session.getAttribute("RECON_LIST"));
					if (retOrgIdMap != null) {
						setRetailerName((String) retOrgIdMap
								.get(getRetOrgMap()));
					}

				} else if ("consolidated".equalsIgnoreCase(getReconViewType())) {
					List list1 = new ReconcilationReportRetHelper()
							.createConsolidatedReport(retOrgIdMap, startDate,
									dt, dateformat1);
					session.setAttribute("RECON_LIST", list1);
					logger.debug("Recon List: "
							+ session.getAttribute("RECON_LIST"));
					// System.out.println(session.getAttribute("RECON_LIST"));
					// Iterator reconItr =
					// ((List)session.getAttribute("RECON_LIST")).listIterator();
					// System.out.println((List)session.getAttribute("RECON_LIST"));
					return "consolidated";
				}
				if (getRetailerName() != null
						&& !"".equalsIgnoreCase(getRetailerName())) {
					int retId = rrh.getUserId(getRetailerName());
					double bal = rrh.getPreviousBalance(retId, startDate,
							"Ticket Wise Report");
					logger.debug("Balance: " + bal);
					// System.out.println(bal+"!!!!!!!!!!!!!!!!!!!11111111111");
					session.setAttribute("PREVIOUS_BAL", bal);
					List list1 = new ReconcilationReportRetHelper()
							.CreateReport(retId, startDate, dt, dateformat1);
					List listAftrlstPmnt = rrh.getDetailsAftrLstPmnt(retId,
							startDate, dt, dateformat1);
					logger.debug(" List Size: " + list1.size());
					// System.out.println(" ^^^^^^^^^^"+list1.size());
					session.setAttribute("RECON_LIST", list1);
					session.setAttribute("RECON_AFTRLST_PMNT", listAftrlstPmnt);

				}
			} catch (Exception e) {
				throw new LMSException(e);
			}
		}
		// System.out.println("last************************");

		session.setAttribute("partyName", getRetailerName());
		return "SUCCESS";
	}

	public String createReportMenu() throws SQLException, ParseException {
		/*
		 * HttpSession session = getRequest().getSession(); UserInfoBean
		 * userBean= (UserInfoBean) session.getAttribute("USER_INFO"); int
		 * userOrgId = userBean.getUserOrgId(); orgMapRet = new
		 * ReconcilationReportRetHelper().getRetOrgMap(userOrgId);
		 */
		return SUCCESS;
	}

	public String createReportRetMenu() throws SQLException, ParseException {
		logger.info("Inside createReportRetMenu");
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userOrgId = userBean.getUserOrgId();
		String userOrgName = userBean.getOrgName();
		logger.debug(userOrgId + "----" + userOrgName);
		// System.out.println(userOrgId+"----"+userOrgName);
		orgMapRet = new TreeMap<Integer, String>();
		orgMapRet.put(userOrgId, userOrgName);
		return SUCCESS;
	}

	public String getAgentName() {
		return agentName;
	}

	public int getAgentOrgMap() {
		return agentOrgMap;
	}

	public String getFromDate() {
		return fromDate;
	}

	public Map<Integer, String> getOrgMapRet() {
		return orgMapRet;
	}

	public String getReconRepType() {
		return reconRepType;
	}

	public String getReconViewType() {
		return reconViewType;
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

	public int getRetOrgMap() {
		return retOrgMap;
	}

	public String getToDate() {
		return toDate;
	}

	public void retailerListAjax() {
		UserInfoBean userBean;
		HttpSession session = request.getSession();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		ReconcilationReportRetHelper rrh = new ReconcilationReportRetHelper();
		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map retOrgIdMap = rrh.getRetOrgMap(userBean.getUserOrgId(),
				new Timestamp(new Date().getTime()));

		String html = "";
		if (reconRepType.equals("Self")) {
			int userOrgId = userBean.getUserOrgId();
			String userOrgName = userBean.getOrgName();
			logger.debug("userOrgId: " + userOrgId + "----" + "userOrgName : "
					+ userOrgName);
			// System.out.println(userOrgId+"----"+userOrgName);
			agentOrgIdMap = new TreeMap<Integer, String>();
			agentOrgIdMap.put(userOrgId, userOrgName);
			html = "Agent Name : <select class=\"option\" name=\"agentOrgMap\">";

			Iterator agtOrgitr = agentOrgIdMap.entrySet().iterator();
			while (agtOrgitr.hasNext()) {
				Map.Entry<Integer, String> agtOrgpair = (Map.Entry<Integer, String>) agtOrgitr
						.next();
				String agtOrgName = agtOrgpair.getValue();
				html += "<option class=\"option\" value=\""
						+ agtOrgpair.getKey() + "\">" + agtOrgName
						+ "</option>";
			}
			html += "</select>";

		} else if (reconRepType.equals("RetailerWise")) {
			html = "Select Retailer: <select class=\"option\" name=\"retOrgMap\"><OPTION VALUE=-1>"
					+ "--Please Select--</OPTION>";

			Iterator retOrgitr = retOrgIdMap.entrySet().iterator();
			while (retOrgitr.hasNext()) {
				Map.Entry<Integer, String> retOrgpair = (Map.Entry<Integer, String>) retOrgitr
						.next();
				String retOrgName = retOrgpair.getValue();
				html += "<option class=\"option\" value=\""
						+ retOrgpair.getKey() + "\">" + retOrgName
						+ "</option>";
			}
			html += "</select>";

		}
		session = request.getSession();
		session.setAttribute("agentOrgIdMap", agentOrgIdMap);
		session.setAttribute("retOrgIdMap", retOrgIdMap);

		response.setContentType("text/html");
		out.print(html);

	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public void setAgentOrgMap(int agentOrgMap) {
		this.agentOrgMap = agentOrgMap;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setOrgMapRet(Map<Integer, String> orgMapRet) {
		this.orgMapRet = orgMapRet;
	}

	public void setReconRepType(String reconRepType) {
		this.reconRepType = reconRepType;
	}

	public void setReconViewType(String reconViewType) {
		this.reconViewType = reconViewType;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public void setRetOrgMap(int retOrgMap) {
		this.retOrgMap = retOrgMap;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
