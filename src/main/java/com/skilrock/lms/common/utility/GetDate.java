package com.skilrock.lms.common.utility;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.exception.LMSException;

public class GetDate {
	static Log logger = LogFactory.getLog(GetDate.class);

	private static String[] repType = { "Current Day", "Current Week",
			"Current Month" };

	private static final DateFormat sqlDateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final DateFormat utilDateFormatter = new SimpleDateFormat(
			"dd-MM-yyyy");

	public static Timestamp fetchTransDateTimeStampForAdocLedger(
			String transDate) {

		Calendar cal = Calendar.getInstance();
		int YYYY = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH) + 1;
		String MM = mm < 10 ? "0" + mm : mm + "";
		int dd = cal.get(Calendar.DATE);
		String DD = dd < 10 ? "0" + dd : dd + "";

		if (transDate.replaceAll("-", "").equals(YYYY + MM + DD + "")) {
			System.out.println(" -- inside if matched ======== current date "
					+ " ,,,, trans_date=" + transDate + ";;;;; current date = "
					+ (YYYY + MM + DD));
			return new java.sql.Timestamp(new java.util.Date().getTime());

		} else {
			System.out.println(" -- inside if matched ======== previouse date "
					+ " ,,,, trans_date=" + transDate);
			String dateArr[] = transDate.split("-");
			cal.clear();
			cal.set(Integer.parseInt(dateArr[0]),
					Integer.parseInt(dateArr[1]) - 1, Integer
							.parseInt(dateArr[2]), 23, 30, 0);
			return new java.sql.Timestamp(cal.getTimeInMillis());
		}

	}

	public static String getConvertedDate(java.util.Date utilDate) {

		SimpleDateFormat utilformat = new SimpleDateFormat("dd-MMM-yyyy");
		// logger.debug(utilformat.format(utilDate));

		return utilformat.format(utilDate);
	}

	public static DateBeans getDate(String type) {
		int index = -1;
		DateBeans dateBean = new DateBeans();

		Date date = new java.sql.Date(new java.util.Date().getTime());
		for (int i = 0; i < repType.length; i++) {
			if (repType[i].equalsIgnoreCase(type)) {
				index = i;
				dateBean.setReportType(repType[i]);
				break;
			}
		}
		Calendar cal = null;
		switch (index) {
		case 0: {
			cal = Calendar.getInstance();
			dateBean.setFirstdate(date);
			dateBean.setReportday(cal.getTime());
			cal.set(Calendar.DATE, (cal.get(Calendar.DATE) + 1));
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));

			System.out.print("start date : " + dateBean.getFirstdate());
			logger.debug("last date ===== " + dateBean.getLastdate());

			return dateBean;

		}
		case 1: {
			cal = Calendar.getInstance();
			// logger.debug("DAte "+cal.getTime()+" day of the week :
			// "+cal.get(Calendar.DAY_OF_WEEK)+" get date
			// "+Calendar.DAY_OF_MONTH);
			int dayOfWeek = 0;
			if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				dayOfWeek = 8;
			} else {
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			}
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - dayOfWeek + 2);
			date = new java.sql.Date(cal.getTime().getTime());
			dateBean.setFirstdate(date);
			dateBean.setStartDate(cal.getTime());

			cal = Calendar.getInstance();
			cal.set(Calendar.DATE, (cal.get(Calendar.DATE) + 1));
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setEndDate(Calendar.getInstance().getTime());

			System.out.print("start date : " + dateBean.getFirstdate());
			logger.debug("\tlast date ===== " + dateBean.getLastdate());
			return dateBean;
		}
		case 2: {
			cal = Calendar.getInstance();
			cal.set(Calendar.DATE, (cal.get(Calendar.DATE) + 1));
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));
			cal = Calendar.getInstance();
			// logger.debug("DAte "+cal.getTime()+" day of the week :
			// "+cal.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.DATE, 1);
			date = new java.sql.Date(cal.getTime().getTime());
			// logger.debug("DAte "+cal.getTime()+" day of the week :
			// "+cal.get(Calendar.DAY_OF_MONTH));
			dateBean.setFirstdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setStartDate(cal.getTime());

			System.out.print("start date : " + dateBean.getFirstdate());
			logger.debug("\tlast date ===== " + dateBean.getLastdate());

			return dateBean;
		}
		}
		return dateBean;
	}

	public static DateBeans getDate(String start_date, String end_Date)
			throws LMSException {
		DateBeans dateBeans = new DateBeans();
		SimpleDateFormat utilDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		logger.debug("inside get Date == ");
		try {
			dateBeans.setStartDate(utilDateFormat.parse(start_date));
			dateBeans.setFirstdate(new java.sql.Date(utilDateFormat.parse(
					start_date).getTime()));
			dateBeans.setEndDate(utilDateFormat.parse(end_Date));
			dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(
					utilDateFormat.parse(end_Date)).getTime()));
			dateBeans.setReportType("");
			logger.debug("after get Date == ");
		} catch (ParseException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
		return dateBeans;
	}

	public static java.util.Date getNextDayDate(java.util.Date utilDate) {
		java.util.Date nextDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(utilDate);
		cal.add(Calendar.DATE, 1);

		nextDate = cal.getTime();
		logger.debug(nextDate);

		return nextDate;
	}

	public static String getSqlToUtilFormatStr(String sqlTypeStr) {
		String str = null;
		try {
			java.sql.Date indate = new java.sql.Date(sqlDateFormatter.parse(
					sqlTypeStr).getTime());
			str = utilDateFormatter.format(sqlDateToutilDate(indate));
			logger.debug("formated date : " + str);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void main(String[] args) {
		String start_date = "2008-08-12";
		getSqlToUtilFormatStr(start_date);
		System.out.println(fetchTransDateTimeStampForAdocLedger("2008-08-15"));
	}

	public static java.util.Date sqlDateToutilDate(java.sql.Date sDate)
			throws ParseException {
		return utilDateFormatter.parse(utilDateFormatter.format(sDate));
	}

	public static java.sql.Date utilDateToSqlDate(java.util.Date uDate)
			throws ParseException {
		return java.sql.Date.valueOf(sqlDateFormatter.format(uDate));
	}

	public synchronized DateBeans getDateForSchedular(String type) {
		int index = -1;
		DateBeans dateBean = new DateBeans();

		Date date = new java.sql.Date(new java.util.Date().getTime());
		for (int i = 0; i < repType.length; i++) {
			if (repType[i].equalsIgnoreCase(type)) {
				index = i;
				dateBean.setReportType(repType[i]);
				break;
			}
		}

		switch (index) {
		case 0: {

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			dateBean.setFirstdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setStartDate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setReportday(cal.getTime());

			cal = Calendar.getInstance();
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setEndDate(new java.sql.Date(cal.getTime().getTime()));
			System.out.print(" Daily Dates  start date : "
					+ dateBean.getFirstdate());
			logger.debug("last date : " + dateBean.getLastdate());

			return dateBean;

		}
		case 1: {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);

			int dayOfWeek = 0;
			if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				dayOfWeek = 8;
			} else {
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			}
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - dayOfWeek + 2);
			date = new java.sql.Date(cal.getTime().getTime());
			dateBean.setFirstdate(date);
			dateBean.setStartDate(cal.getTime());

			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);

			cal.add(Calendar.DATE, 1);
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));

			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			dateBean.setEndDate(cal.getTime());

			System.out.print("weekly dates ====  start date : "
					+ dateBean.getFirstdate());
			logger.debug("\tlast date : " + dateBean.getLastdate());
			return dateBean;

		}
		case 2: {

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);

			cal.set(Calendar.DATE, (cal.get(Calendar.DATE) + 1));
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));

			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.DATE, 1);
			date = new java.sql.Date(cal.getTime().getTime());

			dateBean.setFirstdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setStartDate(cal.getTime());

			System.out.print("Monthly Dates ==== start date : "
					+ dateBean.getFirstdate());
			logger.debug("\tlast date : " + dateBean.getLastdate());

			return dateBean;

		}
		}
		return dateBean;
	}
}
