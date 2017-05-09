package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;

public class DateFormatConverter extends ActionSupport {
	static Log logger = LogFactory.getLog(DateFormatConverter.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */

	public synchronized static String convertDateInGlobalFormat(String strDate) {
		ServletContext sc = ServletActionContext.getServletContext();
		return convertDateInGlobalFormat(strDate, (String) sc
				.getAttribute("date_format"));
	}

	public synchronized static String convertDateInGlobalFormat(String strDate,
			String formate) {
		// Let Assume Date in yyyy-mm-dd formate coming from Database
		String newDate = null;
		String time=null;
		if(strDate.indexOf(':')>0){
			time = " "+strDate.split(" ")[1];
			strDate= strDate.split(" ")[0];
		}
		String newDateArr[] = new String[3];
		String formateParser = CommonMethods.findParserString(formate);
		try {
			String dateArr[] = strDate.split("-");
			String formateArr[] = formate.split(formateParser);
			for (int i = 0; i < formateArr.length; i++) {
				if (formateArr[i].toLowerCase().contains("dd")) {
					newDateArr[i] = dateArr[2];
				} else if (formateArr[i].toLowerCase().contains("mm")) {
					newDateArr[i] = dateArr[1];
				} else if (formateArr[i].toLowerCase().contains("yy")) {
					newDateArr[i] = dateArr[0];
				}
			}
			newDate = Arrays.asList(newDateArr).toString().replace("[", "")
					.replace("]", "").replace(", ", formateParser);
			if(time!=null)
				newDate=newDate+time;
			System.out.println("***newDate**" + newDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newDate;
	}

	public static void main(String[] args) {

		SimpleDateFormat format = new SimpleDateFormat("MMM-yyyy");
		java.util.Date date = new java.util.Date();

		logger.debug(format.format(date));

		/*
		 * DateFormatConverter obj=new DateFormatConverter();
		 * obj.getGameDetail();
		 */

	}

	/**
	 * It convert a <code>java.sql.Date</code> to <code>String</code> of
	 * <b>DD-MMM-YYYY</b> format.
	 * 
	 * @param java.sql.Date
	 * @return String, It return date in String format.
	 */

	public static String parseDateToString(Date sqldate) {
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		if (sqldate != null) {
			String[] datearray = sqldate.toString().split("-");
			String day = datearray[2];
			String month = months[Integer.parseInt(datearray[1]) - 1];
			String year = datearray[0];
			String date = day + "-" + month + "-" + year;
			logger.debug("==========SQL Date To String Date===========");
			logger.debug("SQL Date : " + sqldate);
			logger.debug("String date is == " + date);

			logger.debug("-------------------------------------------------\n");
			parseStringToSQLDate(date);
			return date;
		}
		return null;

	}

	/**
	 * It convert DD-MMM-YYYY date into <code>java.sql.Date</code>
	 * 
	 * @param String
	 * @return java.sql.Date
	 */

	public static Date parseStringToSQLDate(String strdate) {
		Date sqlDate = null;
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		if (strdate != null) {
			String[] datearray = strdate.toString().split("-");
			int year = Integer.parseInt(datearray[2]);
			int day = Integer.parseInt(datearray[0]);
			int month = -1;
			for (int i = 0; i < months.length; i++) {
				if (datearray[1].equalsIgnoreCase(months[i])) {
					month = i;
					break;
				}
			}
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);

			sqlDate = new Date(calendar.getTimeInMillis());
			logger
					.debug("=====================String Date To SQL Date=============");
			logger.debug("String Date is : " + strdate);
			logger.debug("SQL date is : " + sqlDate
					+ "\n---------------------------------------\n");
			return sqlDate;
		} else {
			logger.debug("Enter String is null");
		}
		return sqlDate;
	}

	public static void test() {
		Calendar c = Calendar.getInstance();

		logger.debug("" + c.getTime());

		logger.debug("dayyy:::" + c.get(Calendar.DAY_OF_MONTH));

		logger.debug("min::" + c.getMinimum(Calendar.DAY_OF_MONTH));

		c.add(Calendar.MONTH, 1);

		logger.debug("min::" + c.getTime());

		logger.debug("-------" + c.get(Calendar.DAY_OF_MONTH));

		c.add(Calendar.DAY_OF_MONTH, -(c.get(Calendar.DAY_OF_MONTH) - 1));

		logger.debug("New time::" + c.getTime());

		logger.debug(c.get(Calendar.DAY_OF_WEEK));

		// c.add(Calendar.DAY_OF_WEEK, -3);
		//		
		// logger.debug(c.getTime());
	}

	/**
	 * This method is used to parse the SQL date into <B>'DD-MMM-YYYY'</B>
	 * format.
	 * 
	 * @param sqldate
	 * @return String
	 */

	private Connection con;

	private PreparedStatement pstmt;

	private ResultSet resultset;

	public void getGameDetail() {

		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(QueryManager
					.getST_INVENTORY_GAME_SEARCH());
			resultset = pstmt.executeQuery();

			while (resultset.next()) {
				parseDateToString(resultset.getDate(5));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
