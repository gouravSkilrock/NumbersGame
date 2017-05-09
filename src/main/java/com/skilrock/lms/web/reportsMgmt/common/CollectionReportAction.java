package com.skilrock.lms.web.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CollectionReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GetUserOrganziationDetails;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportAgentHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportHelper;

public class CollectionReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String[] Type = { "Current Day", "Current Week",
			"Current Month" };

	private String end_Date;
	Log logger = LogFactory.getLog(CollectionReportAction.class);

	private String orgType;
	private HttpServletRequest request;

	private HttpServletResponse response;
	private String start_date;
	// private HttpServletResponse response;
	private String totaltime;

	String[] type = { "Daily", "Weekly", "Monthly" };

	public String agentCollectionDetails() throws ParseException {
		logger.debug("collection report for AGENT result is executed. ");
		// System.out.println("collection report for AGENT result is executed.
		// ");
		executeHelper("AGENT");
		return SUCCESS;
	}

	public String boCollectionDetails() throws ParseException {
		logger.debug("collection Report for Bo result is executed. ");
		// System.out.println("collection Report for Bo result is executed. ");
		executeHelper("BO");
		return SUCCESS;
	}

	@Override
	public String execute() {
		request.getSession().setAttribute("stDate",
				new java.sql.Date(new java.util.Date().getTime()));
		return SUCCESS;
	}

	public void executeHelper(String owner) throws ParseException {
		List<CollectionReportBean> list = null;
		DateBeans dateBeans = getDateDetails();
		Map<String, String> lastRowMap = null;
		Map<Integer, Double> mapForOpenBal = null;
		Timestamp deplDate = null;
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String deployDateString = (String) sc.getAttribute("DEPLOYMENT_DATE");
		String dateFormat = (String) sc.getAttribute("date_format");
		try {
			deplDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					deployDateString).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean isDraw = false;
		boolean isScratch = false;
		boolean isCS = false;
		boolean isOla = false;
		String draw = "";
		String scratch = "";
		String ola = "";
		draw = (String) session.getServletContext().getAttribute("IS_DRAW");
		scratch = (String) session.getServletContext().getAttribute(
				"IS_SCRATCH");
		ola = (String) session.getServletContext().getAttribute("IS_OLA");

		if (draw.equalsIgnoreCase("YES")) {
			isDraw = true;

		}
		if (scratch.equalsIgnoreCase("YES")) {
			isScratch = true;

		}
		if (ola.equalsIgnoreCase("YES")) {
			isOla = true;

		}

		if ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS())) {
			isCS = true;
		}
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		// get the list of BO details from database using helper
		if ("BO".equalsIgnoreCase(owner)) {
			logger.debug("start date is this:" + dateBeans.getFirstdate()
					+ "end date is this:" + dateBeans.getLastdate());
			CollectionReportHelper boHelper = new CollectionReportHelper(
					dateBeans);
			mapForOpenBal = boHelper.getAgentOpeningBalance(boHelper
					.getAgentOrgId(), isDraw, isScratch, isCS, deplDate);
			list = boHelper.getBOCollectionDetail(boHelper.getAgentOrgId(),
					isDraw, isScratch, isCS, false);
			list = boHelper.MergeOpenBal(list, mapForOpenBal);
			lastRowMap = boHelper.lastRowMap;
			// logger.debug(mapForOpenBal);
		}
		// get the list of AGENT details from database using helper
		else {
			CollectionReportAgentHelper boHelper = new CollectionReportAgentHelper(
					infoBean, dateBeans);
			mapForOpenBal = boHelper.getRetailerOpeningBalance(boHelper
					.getRetailerOrgId(), isDraw, isScratch, isOla,isCS, deplDate);
			list = boHelper.getAgentCollectionDetail(boHelper
					.getRetailerOrgId(), isDraw, isScratch, isOla,isCS, false);
			list = boHelper.MergeOpenBal(list, mapForOpenBal);
			lastRowMap = boHelper.lastRowMap;

		}

		// System.out.println("callled 2222222222222");

		// create the message to be display on jsp
		String collectionMsg = null;
		if ("".equals(dateBeans.getReportType().trim())) {
			collectionMsg = "Collection Summary report From Date "
					+ CollectionReportHelper.formatDate(dateBeans
							.getStartDate()) + " To "
					+ CollectionReportHelper.formatDate(dateBeans.getEndDate());
		} else {
			collectionMsg = "Collection Summary report of "
					+ "Last"
					+ dateBeans.getReportType().substring(7,
							dateBeans.getReportType().length());
		}

		// System.out.println("called 333333333333333333");
		// retrieve the details about the current user

		int userId = infoBean.getUserOrgId();
		String orgAdd = GetUserOrganziationDetails.getAddress(userId);
		String orgName = infoBean.getOrgCode();
		lastRowMap.put("orgName", orgName);
		lastRowMap.put("orgAdd", orgAdd);
		lastRowMap.put("collectionMsg", collectionMsg);

		// set the attributes in session
		session.setAttribute("datebean", dateBeans);
		session.setAttribute("collectionReport", list);
		session.setAttribute("lastRowMap", lastRowMap);

	}

	public String exportExcel() {

		ArrayList<CollectionReportBean> data = new ArrayList<CollectionReportBean>();
		data = (ArrayList<CollectionReportBean>) request.getSession()
				.getAttribute("collectionReport");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=Collection_Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForCollectionReport excel = new WriteExcelForCollectionReport(
					(DateBeans) request.getSession().getAttribute("datebean"));

			Map<String, String> lastrowMap = (Map<String, String>) request
					.getSession().getAttribute("lastRowMap");

			UserInfoBean userBean = new UserInfoBean();
			userBean = (UserInfoBean) request.getSession().getAttribute(
					"USER_INFO");
			excel.write(data, w, lastrowMap.get("orgName"), lastrowMap
					.get("orgAdd"), orgType, (String) request.getSession()
					.getServletContext().getAttribute("CURRENCY_SYMBOL"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private DateBeans getDateDetails() {
		DateBeans dateBeans = getDateForSchedular(totaltime, Calendar
				.getInstance());
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			HttpSession session = request.getSession();
			SimpleDateFormat utilDateFormat = new SimpleDateFormat(
					(String) session.getAttribute("date_format"));

			try {
				dateBeans.setStartDate(utilDateFormat.parse(start_date));
				dateBeans.setFirstdate(new java.sql.Date(utilDateFormat.parse(
						start_date).getTime()));
				dateBeans.setEndDate(new java.util.Date(utilDateFormat.parse(
						end_Date).getTime()));
				dateBeans.setLastdate(new java.sql.Date(utilDateFormat.parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000));
				dateBeans.setReportType("");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateBeans;
	}

	private DateBeans getDateForSchedular(String type, Calendar calendar) {
		int index = -1;
		DateBeans dateBean = new DateBeans();
		for (int i = 0; i < Type.length; i++) {
			if (Type[i].equalsIgnoreCase(type)) {
				index = i;
				dateBean.setReportType(Type[i]);
				break;
			}
		}

		switch (index) {
		case 0: {
			Calendar cal = Calendar.getInstance();
			cal.setTime(calendar.getTime());
			cal.add(Calendar.DATE, -1);
			dateBean.setFirstdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setReportday(cal.getTime());

			cal.setTime(calendar.getTime());
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));

			System.out.println("Query dates ===== First Date = "
					+ dateBean.getFirstdate() + "\t Last Date = "
					+ dateBean.getLastdate());
			System.out.println("Show in mail dates ====== start Date = "
					+ dateBean.getStartDate() + "\t End Date = "
					+ dateBean.getEndDate());

			return dateBean;

		}
		case 1: { // weekly reports

			Calendar cal = Calendar.getInstance();
			cal.setTime(calendar.getTime());

			// called when DAY_OF_WEAK is SUNDAY
			if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				cal.add(Calendar.DATE, -1);
			}

			// set the Last Date from DAY_OF_WEAK as MONDAY of current weak
			cal.set(Calendar.DAY_OF_WEEK, 2);
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));
			// set the actual end date of reports
			cal.add(Calendar.DATE, -1);
			// dateBean.setEndDate(cal.getTime());

			// undo the changes done in calculated Calendar date instance
			cal.add(Calendar.DATE, 1);

			// set the First Day of 'DAY_OF_WEAK' as MONDAY of previous week
			cal.add(Calendar.WEEK_OF_MONTH, -1);
			dateBean.setFirstdate(new java.sql.Date(cal.getTimeInMillis()));
			dateBean.setStartDate(cal.getTime());

			System.out.println("Query dates ===== First Date = "
					+ dateBean.getFirstdate() + "\t Last Date = "
					+ dateBean.getLastdate());
			System.out.println("Show in mail dates ====== start Date = "
					+ dateBean.getStartDate() + "\t End Date = "
					+ dateBean.getEndDate());

			return dateBean;

		}
		case 2: {

			Calendar cal = Calendar.getInstance();
			cal.setTime(calendar.getTime());
			cal.set(Calendar.DATE, 1);
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));

			cal.setTime(calendar.getTime());
			cal.set(Calendar.DATE, 1);
			cal.add(Calendar.MONTH, -1);
			dateBean.setFirstdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setStartDate(cal.getTime());

			System.out.println("Query dates ===== First Date = "
					+ dateBean.getFirstdate() + "\t Last Date = "
					+ dateBean.getLastdate());
			System.out.println("Show in mail dates ====== start Date = "
					+ dateBean.getStartDate() + "\t End Date = "
					+ dateBean.getEndDate());

			return dateBean;

		}
		}
		return dateBean;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getOrgType() {
		return orgType;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public void setEnd_Date(String end_Date) {
		logger.debug("end date called" + end_Date);
		// System.out.println("end date called"+end_Date);
		if (end_Date != null) {
			// this.end_Date = GetDate.getSqlToUtilFormatStr(end_Date);
			this.end_Date = end_Date;
		} else {
			this.end_Date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public void setStart_date(String start_date) {
		logger.debug("first date called" + start_date);
		// System.out.println("first date called"+start_date);
		if (start_date != null) {
			// this.start_date = GetDate.getSqlToUtilFormatStr(start_date);
			this.start_date = start_date;
		} else {
			this.start_date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

}
