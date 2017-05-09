package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LedgerHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ReconcilationReportHelper;

public class ReconcilationReportAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(ReconcilationReportAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.set(calendar.get(Calendar.YEAR), calendar
				.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
		logger.debug("Inside Main : " + calendarNew.getTime());

		// System.out.println(calendarNew.getTime());

		try {
			new ReconcilationReportAction().quartzEntry();
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("==========================Done==============");
		// System.out.println("==========================Done==============");
	}

	private String agentName = null;
	int agentOrgMap;
	Calendar calendar = Calendar.getInstance();
	Date date5 = null;
	SimpleDateFormat dateformat1 = null;
	Date dateFrDtParse = null;
	Date dateToDtParse = null;
	Timestamp dt = null;
	private String fromDate = null;
	Map<Integer, String> orgMap;
	String reconRepType = null;
	private String reconViewType = null;
	private HttpServletRequest request;
	Timestamp startDate = null;

	private String toDate = null;

	public String createReport() throws LMSException {
		logger.info("+++++++++++++++ createReport ++++++++++++++");
		// System.out.println("+++++++++++++++ createReport ++++++++++++++");

		HttpSession session = getRequest().getSession();

		String formatString = (String) session.getAttribute("date_format");
		dateformat1 = new SimpleDateFormat(formatString);
		String reconReportType = null;
		double bal = 0.0;
		try {
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
						.get(Calendar.MONTH), calendar2.get(Calendar.DATE), 0,
						0, 0);
				calendar.add(Calendar.DAY_OF_MONTH, 1);

				date5 = calendar.getTime();

				dt = new Timestamp(date5.getTime());
				logger.debug(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");

				// System.out.println(dt
				// + "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");
			}

			LedgerHelper ledgerHelp = new LedgerHelper();
			ledgerHelp.ledgerBoEntry(dt);
			ReconcilationReportHelper rrh = new ReconcilationReportHelper();
			// System.out.println("dateformat1 "+dateformat1 );
			// System.out.println(" String date
			// "+(String)ServletActionContext.getServletContext().getAttribute("DEPLOYMENT_DATE"));
			// System.out.println("new java.sql.Date(fromTimeStamp.getTime())
			// "+startDate);
			// System.out.println("********************8 "+new
			// java.sql.Date(dateformat1.parse((String)ServletActionContext.getServletContext().getAttribute("DEPLOYMENT_DATE")).getTime())+"
			// "+startDate);

			Map agentOrgIdMap = rrh.getAgentOrgMap(dt);
			Iterator agtOrgitr = agentOrgIdMap.entrySet().iterator();
			while (agtOrgitr.hasNext()) {
				Map.Entry<Integer, String> agtOrgpair = (Map.Entry<Integer, String>) agtOrgitr
						.next();
				int agtOrgId = agtOrgpair.getKey();
				reconReportType = rrh.getReconRepType(agtOrgId);
				// System.out.println(" recon typeeeeeeeee reconReportType
				// "+reconReportType +" agtOrgId "+agtOrgId+" getAgentOrgMap
				// "+getAgentOrgMap());
				rrh.reconAgentTicketwiseEntry(agtOrgId, dt,
						"Ticket Wise Report");
				rrh.reconAgentBookwiseEntry(agtOrgId, dt, "Book Wise Report");

			}

			if ("detailed".equalsIgnoreCase(getReconViewType())) {
				reconReportType = rrh.getReconRepType(getAgentOrgMap());
				bal = rrh.getPreviousBalance(getAgentOrgMap(), startDate,
						reconReportType);

				// System.out.println(bal+"!!!!!!!!!!!!!!!!!!!11111111111");
				session.setAttribute("PREVIOUS_BAL", bal);
				List list1 = new ReconcilationReportHelper().CreateReport(
						getAgentOrgMap(), startDate, dt, dateformat1,
						reconReportType);
				List listAftrlstPmnt = rrh.getDetailsAftrLstPmnt(
						getAgentOrgMap(), startDate, dt, dateformat1);
				// System.out.println(" ^^^^^^^^^^"+list1.size());
				session.setAttribute("RECON_LIST", list1);
				session.setAttribute("RECON_AFTRLST_PMNT", listAftrlstPmnt);
				logger.debug("Recon List: "
						+ session.getAttribute("RECON_LIST"));
				// System.out.println(session.getAttribute("RECON_LIST"));
			} else if ("consolidated".equalsIgnoreCase(getReconViewType())) {
				logger.debug("consolidated ###########  startDate " + startDate
						+ "  dt " + dt);
				// System.out.println("consolidated ########### startDate
				// "+startDate+" dt "+dt );
				List list1 = new ReconcilationReportHelper()
						.createConsolidatedReport(agentOrgIdMap, startDate, dt,
								dateformat1);

				// System.out.println("&&&&&&&&&&&&&");
				// System.out.println(" ^^^^^^^^^^"+list1.size());
				session.setAttribute("RECON_LIST", list1);
				logger.debug("Recon List: "
						+ session.getAttribute("RECON_LIST"));
				// System.out.println(session.getAttribute("RECON_LIST"));
				// Iterator reconItr =
				// ((List)session.getAttribute("RECON_LIST")).listIterator();
				// System.out.println((List)session.getAttribute("RECON_LIST"));

				// System.out.println("done$$$$$$$$$$$$$$$");
				return "consolidated";

			}
			if (!"".equalsIgnoreCase(getAgentName()) && getAgentName() != null) {
				int agentID = rrh.getUserId(getAgentName());
				reconReportType = rrh.getReconRepType(agentID);
				bal = rrh.getPreviousBalance(agentID, startDate,
						reconReportType);

				// System.out.println(bal+"!!!Action!!!!!!11111111111 startdate
				// "+startDate+" dt "+dt);
				session.setAttribute("PREVIOUS_BAL", bal);

				List list1 = new ReconcilationReportHelper().CreateReport(
						agentID, startDate, dt, dateformat1, reconReportType);
				List listAftrlstPmnt = rrh.getDetailsAftrLstPmnt(agentID,
						startDate, dt, dateformat1);
				// System.out.println(" ^^^^^^^^^^"+list1.size());
				session.setAttribute("RECON_LIST", list1);
				session.setAttribute("RECON_AFTRLST_PMNT", listAftrlstPmnt);
				// System.out.println(session.getAttribute("RECON_LIST"));
			}

			if (agentOrgMap > 0) {
				setAgentName((String) agentOrgIdMap.get(agentOrgMap));
			}

		} catch (Exception e) {
			throw new LMSException(e);
		}

		session.setAttribute("partyName", getAgentName());

		return SUCCESS;
	}

	public String createReport(HttpSession session,
			ServletContext servletContext) throws LMSException {
		logger
				.info("+++++++++++++++++++++=======createReport 33333 =====++++++++++++++++");
		// System.out.println("+++++++++++++++++++++=======createReport 33333
		// =====++++++++++++++++");

		String formatString = (String) session.getAttribute("date_format");
		dateformat1 = new SimpleDateFormat(formatString);
		String reconReportType = null;
		double bal = 0.0;
		try {
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
						.get(Calendar.MONTH), calendar2.get(Calendar.DATE), 0,
						0, 0);
				calendar.add(Calendar.DAY_OF_MONTH, 1);

				date5 = calendar.getTime();

				dt = new Timestamp(date5.getTime());
				logger.debug(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");
				// System.out.println(dt
				// + "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");
			}

			LedgerHelper ledgerHelp = new LedgerHelper();
			ledgerHelp.ledgerBoEntry(dt);
			ReconcilationReportHelper rrh = new ReconcilationReportHelper();
			// System.out.println("dateformat1 "+dateformat1 );
			// System.out.println(" String date
			// "+(String)servletContext.getAttribute("DEPLOYMENT_DATE"));
			// System.out.println("new java.sql.Date(fromTimeStamp.getTime())
			// "+startDate);
			// System.out.println("********************8 "+new
			// java.sql.Date(dateformat1.parse((String)servletContext.getAttribute("DEPLOYMENT_DATE")).getTime())+"
			// "+startDate);

			Map agentOrgIdMap = rrh.getAgentOrgMap(dt);
			Iterator agtOrgitr = agentOrgIdMap.entrySet().iterator();
			while (agtOrgitr.hasNext()) {
				Map.Entry<Integer, String> agtOrgpair = (Map.Entry<Integer, String>) agtOrgitr
						.next();
				int agtOrgId = agtOrgpair.getKey();
				reconReportType = rrh.getReconRepType(agtOrgId);
				// System.out.println(" recon typeeeeeeeee reconReportType
				// "+reconReportType +" agtOrgId "+agtOrgId+" getAgentOrgMap
				// "+getAgentOrgMap());
				rrh.reconAgentTicketwiseEntry(agtOrgId, dt,
						"Ticket Wise Report");
				rrh.reconAgentBookwiseEntry(agtOrgId, dt, "Book Wise Report");

			}
			reconReportType = rrh.getReconRepType(getAgentOrgMap());
			bal = rrh.getPreviousBalance(getAgentOrgMap(), startDate,
					reconReportType);

			// System.out.println(bal+"!!!!!!!!!!!!!!!!!!!11111111111");
			session.setAttribute("PREVIOUS_BAL", bal);

			List list1 = new ReconcilationReportHelper().CreateReport(
					getAgentOrgMap(), startDate, dt, dateformat1,
					reconReportType);
			List listAftrlstPmnt = rrh.getDetailsAftrLstPmnt(getAgentOrgMap(),
					startDate, dt, dateformat1);
			// System.out.println(" ^^^^^^^^^^"+list1.size());
			session.setAttribute("RECON_LIST", list1);
			session.setAttribute("RECON_AFTRLST_PMNT", listAftrlstPmnt);

			// System.out.println("&&&&&&&&&&&&&");
			// .out.println(" ^^^^^^^^^^"+list1.size());
			session.setAttribute("RECON_LIST", list1);
			// System.out.println(session.getAttribute("RECON_LIST"));
		} catch (Exception e) {
			throw new LMSException(e);
		}

		return SUCCESS;
	}

	public String createReportMenu() throws SQLException, ParseException {

		orgMap = new ReconcilationReportHelper().getAgentOrgMap(new Timestamp(
				new Date().getTime()));
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

	public Map<Integer, String> getOrgMap() {
		return orgMap;
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

	public String getToDate() {
		return toDate;
	}

	public String quartzEntry() throws LMSException {
		logger
				.info("+++++++++++++++++++++=======Quartzzzzzz Recon =====++++++++++++++++");
		// System.out.println("+++++++++++++++++++++=======Quartzzzzzz Recon
		// =====++++++++++++++++");
		String reconReportType = null;

		try {
			Calendar calendar = Calendar.getInstance();
			Calendar calendarNew = Calendar.getInstance();
			calendarNew.set(calendar.get(Calendar.YEAR), calendar
					.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
			dt = new Timestamp(calendarNew.getTimeInMillis());
			LedgerHelper ledgerHelp = new LedgerHelper();
			ledgerHelp.ledgerBoEntry(dt);
			ReconcilationReportHelper rrh = new ReconcilationReportHelper();
			Map agentOrgIdMap = rrh.getAgentOrgMap(dt);
			Iterator agtOrgitr = agentOrgIdMap.entrySet().iterator();
			while (agtOrgitr.hasNext()) {
				Map.Entry<Integer, String> agtOrgpair = (Map.Entry<Integer, String>) agtOrgitr
						.next();
				int agtOrgId = agtOrgpair.getKey();
				reconReportType = rrh.getReconRepType(agtOrgId);
				// System.out.println(" recon typeeeeeeeee reconReportType
				// "+reconReportType +" agtOrgId "+agtOrgId+" getAgentOrgMap
				// "+getAgentOrgMap());
				rrh.reconAgentTicketwiseEntry(agtOrgId, dt,
						"Ticket Wise Report");
				rrh.reconAgentBookwiseEntry(agtOrgId, dt, "Book Wise Report");

			}

			// System.out.println(session.getAttribute("RECON_LIST"));
		} catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		}
		logger.info("RECON Entry Sucessfull..-----+++++++++++");
		// System.out.println("RECON Entry Sucessfull..-----+++++++++++");
		return SUCCESS;
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

	public void setOrgMap(Map<Integer, String> orgMap) {
		this.orgMap = orgMap;
	}

	public void setReconRepType(String reconRepType) {
		this.reconRepType = reconRepType;
	}

	public void setReconViewType(String reconViewType) {
		this.reconViewType = reconViewType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
