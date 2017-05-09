package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.TaskBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;

/**
 * This class is used to provide methods for TDS Calculation
 * 
 * @author ABC
 * 
 */
public class TDSHelper {
	static Log logger = LogFactory.getLog(TDSHelper.class);

	public static void main(String[] args) {
		new TDSHelper().getLastDay(new Date());
		new TDSHelper().getFirstDay(new Date());
	}

	private java.util.Date getFirstDay(java.util.Date passDate) {
		Date firstDayDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(passDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		firstDayDate = cal.getTime();
		logger.debug(" firstDayDate " + firstDayDate.toString());
		return firstDayDate;
	}

	private java.util.Date getLastDay(java.util.Date passDate) {
		Date lastDayDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(passDate);
		cal.set(Calendar.DAY_OF_MONTH, cal
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		lastDayDate = cal.getTime();
		logger.debug(" lastDayDate " + lastDayDate.toString());
		return lastDayDate;
	}

	public List tdsSearchDG(Integer month, Integer year) throws LMSException {
		logger.debug(" tdsSearchDG called");
		double taxSum = 0;
		boolean tds = false;
		boolean isEntry = false;
		int yearTable = 0;
		int monthTable = 0;

		int latestyearTaxCal = 0;
		int latestmonthTaxCal = 0;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		SimpleDateFormat date = new SimpleDateFormat();

		Calendar rightNow = Calendar.getInstance();

		int YR = rightNow.get(Calendar.YEAR);
		int MTH = rightNow.get(Calendar.MONTH) + 1;

		int depYear = year;
		int depMonth = month;
		logger.debug("inside deploy date changed");

		// int MTH=11;
		logger.debug("month from system  " + MTH);

		logger.debug("not depriceted   " + YR);
		logger.debug("not depriceted   " + MTH);

		Connection con = null;
		String serviceName = "";
		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt5 = con.createStatement();
			Statement stmt6 = con.createStatement();
			ResultSet rs = null;
			ResultSet RSPLR = null;
			logger.debug("heeeeeee");
			rs = stmt6
					.executeQuery("select service_display_name from st_lms_service_master where service_code='DG'and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}
			ResultSet latestMonth = stmt5.executeQuery(QueryManager
					.getLatestMonthTDSDG());
			while (latestMonth.next()) {
				Calendar rightNow1 = Calendar.getInstance();

				Date dt = latestMonth.getDate("month");
				logger.debug("get date from db " + dt);
				rightNow1.setTime(dt);
				latestyearTaxCal = rightNow1.get(Calendar.YEAR);
				latestmonthTaxCal = rightNow1.get(Calendar.MONTH) + 1;
				logger.debug(" latest year " + latestyearTaxCal);
				logger.debug(" latest month " + latestmonthTaxCal);

				/*
				 * String latestDate=latestMonth.getString("month"); String
				 * latestyear=latestDate.substring(0,4); String
				 * latestmonth=latestDate.substring(5,7);
				 * latestyearTaxCal=Integer.parseInt(latestyear);
				 * latestmonthTaxCal=Integer.parseInt(latestmonth);
				 * 
				 * logger.debug("latest year from task table " + latestyear);
				 * logger.debug("latest month from task table " + latestmonth);
				 */
				isEntry = true;
				break;

			}

			if (isEntry == false) {
				logger.debug("when no entry for tds");
				latestyearTaxCal = depYear;
				if (depMonth == 01) {
					latestmonthTaxCal = 00;
				} else {
					latestmonthTaxCal = depMonth - 1;
				}

			}

			if (YR == latestyearTaxCal)

			{

				logger.debug("inside same year");
				for (int i = latestmonthTaxCal + 1; i < MTH; i++) {

					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = null;
					Date newdate = null;
					taxSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("9999999999999999999999");
					logger.debug(transaction_date_first);
					// logger.debug(transaction_date_last);

					String queryForPlayer = QueryManager
							.getST3LastMonthTransactionDG()
							+ " where  transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime()) + "' ";
					logger.debug("query----" + queryForPlayer);
					RSPLR = stmt2.executeQuery(queryForPlayer);
					while (RSPLR.next()) {

						double taxAmt = RSPLR
								.getDouble(TableConstants.TAX_AMOUNT);
						logger.debug("tax amount   from table" + taxAmt);
						taxSum = CommonMethods.fmtToTwoDecimal(taxSum + taxAmt);
						logger.debug("calculated tax  " + taxSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.debug("date to insert " + taskDate);

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ taxSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','DG')";
					stmt3.executeUpdate(queryTaskCreate);

				}

			}

			if (YR == latestyearTaxCal + 1) {
				logger.debug("when diff is one");
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");

				for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
					Date dd = null;
					Date ddd = null;
					taxSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10) {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}

					String queryForPlayer = QueryManager
							.getST3LastMonthTransactionDG()
							+ " where  transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime()) + "' ";
					logger.debug("query----" + queryForPlayer);
					RSPLR = stmt3.executeQuery(queryForPlayer);
					while (RSPLR.next()) {

						double taxAmt = RSPLR
								.getDouble(TableConstants.TAX_AMOUNT);
						logger.debug("tax amount   from table" + taxAmt);
						taxSum = CommonMethods.fmtToTwoDecimal(taxSum + taxAmt);
						logger.debug("calculated tax  " + taxSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ taxSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','DG')";
					stmt3.executeUpdate(queryTaskCreate);

				}

				for (int i = 1; i < MTH; i++) {
					Date dd = null;
					Date ddd = null;
					taxSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) + "-"
						// + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					} else {

						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) +
						// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("result not  found in task table ");
					String queryForPlayer = QueryManager
							.getST3LastMonthTransactionDG()
							+ " where  transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime()) + "' ";
					logger.debug("query-----" + queryForPlayer);
					RSPLR = stmt4.executeQuery(queryForPlayer);
					while (RSPLR.next()) {

						double taxAmt = RSPLR
								.getDouble(TableConstants.TAX_AMOUNT);
						logger.debug("tax amount   from table" + taxAmt);
						taxSum = CommonMethods.fmtToTwoDecimal(taxSum + taxAmt);
						logger.debug("calculated tax  " + taxSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + (latestyearTaxCal + 1);
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ taxSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','DG')";
					stmt3.executeUpdate(queryTaskCreate);

				}
			}

			if (YR > latestyearTaxCal + 1) {
				logger.debug("when diff is more than one");
				for (int j = latestyearTaxCal; j <= YR; j++) {
					logger.debug("j::" + j + "  latestyearTaxCal::"
							+ latestyearTaxCal);
					if (j == latestyearTaxCal) {
						logger.debug("j == latestyearTaxCal"
								+ (j == latestyearTaxCal));
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;

						for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
							taxSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + "0" + i + "-" + "01";
							} else {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + i + "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-" + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";

							}

							String queryForPlayer = QueryManager
									.getST3LastMonthTransactionDG()
									+ " where  transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' ";
							RSPLR = stmt3.executeQuery(queryForPlayer);
							while (RSPLR.next()) {

								double taxAmt = RSPLR
										.getDouble(TableConstants.TAX_AMOUNT);
								logger
										.debug("tax amount   from table"
												+ taxAmt);
								taxSum = CommonMethods.fmtToTwoDecimal(taxSum
										+ taxAmt);
								logger.debug("calculated tax  " + taxSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + latestyearTaxCal;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ taxSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','DG')";
							stmt3.executeUpdate(queryTaskCreate);
						}

					}

					if (j == YR) {
						logger.debug("j == YR" + (j == YR));
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i < MTH; i++) {
							taxSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + YR + "-" + "0"
										+ i + "-" + "01";
							} else {
								transaction_date_first = "" + YR + "-" + i
										+ "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +YR + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +YR + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}

							System.out
									.println("result not  found in task table ");
							String queryForPlayer = QueryManager
									.getST3LastMonthTransactionDG()
									+ " where  transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' ";
							RSPLR = stmt4.executeQuery(queryForPlayer);
							while (RSPLR.next()) {

								double taxAmt = RSPLR
										.getDouble(TableConstants.TAX_AMOUNT);
								logger
										.debug("tax amount   from table"
												+ taxAmt);
								taxSum = CommonMethods.fmtToTwoDecimal(taxSum
										+ taxAmt);
								logger.debug("calculated tax  " + taxSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + YR;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ taxSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','DG')";
							stmt3.executeUpdate(queryTaskCreate);
						}
					}

					if (j != YR && j != latestyearTaxCal) {
						logger.debug("j != YR" + (j != YR));
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i <= 12; i++) {
							taxSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + j + "-" + "0" + i
										+ "-" + "01";
							} else {
								transaction_date_first = "" + j + "-" + i + "-"
										+ "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +j + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";

							} else {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +j + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}
							System.out
									.println("result not  found in task table ");
							String queryForPlayer = QueryManager
									.getST3LastMonthTransactionDG()
									+ " where  transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' ";
							RSPLR = stmt4.executeQuery(queryForPlayer);
							while (RSPLR.next()) {

								double taxAmt = RSPLR
										.getDouble(TableConstants.TAX_AMOUNT);
								logger
										.debug("tax amount   from table"
												+ taxAmt);
								taxSum = CommonMethods.fmtToTwoDecimal(taxSum
										+ taxAmt);
								logger.debug("calculated tax  " + taxSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + j;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ taxSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','DG')";
							stmt3.executeUpdate(queryTaskCreate);

						}
					}

				}
			}

			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();
			logger.debug("333333333333333333hhhhhhhhhhh");
			ResultSet rs3 = stmt4.executeQuery(QueryManager
					.getUnapprovedTDSDG());

			while (rs3.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs3.getDouble("amount"));

				// SimpleDateFormat date1 = new
				// SimpleDateFormat("dd-MM-yyyy");//it would be the format set
				// by Vineet
				// taskBean.setMonth(date1.format(rs3.getDate("month")));
				// logger.debug("heeeeeeeeeeeeellllllll
				// "+date1.format(rs3.getDate("month")) );
				taskBean.setMonth(rs3.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs3
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs3.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs3.getInt(TableConstants.TASK_ID));
				taskBean.setServiceCode("DG");
				taskBean.setServiceName(serviceName);
				logger.debug("taskid is   " + rs3.getInt("task_id"));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(taskBean);
			}

			con.commit();
			// removed by arun // con.close(); // added by yogesh at the time of
			// self review
			return searchResults;

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);

			// added by arun
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// return null;

	}

	/**
	 * This method is used to calculate TDS for a particular month
	 * 
	 * @param month
	 * @param year
	 * @returns the List of TaskBean type
	 * @throws LMSException
	 */
	public List tdsSearchSE(Integer month, Integer year) throws LMSException {
		logger.debug(" tdsSearchSE called");
		double taxSum = 0;
		boolean tds = false;
		boolean isEntry = false;
		int yearTable = 0;
		int monthTable = 0;

		int latestyearTaxCal = 0;
		int latestmonthTaxCal = 0;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		SimpleDateFormat date = new SimpleDateFormat();

		Calendar rightNow = Calendar.getInstance();

		int YR = rightNow.get(Calendar.YEAR);
		int MTH = rightNow.get(Calendar.MONTH) + 1;

		int depYear = year;
		int depMonth = month;
		logger.debug("inside deploy date changed");

		// int MTH=11;
		logger.debug("month from system  " + MTH);

		logger.debug("not depriceted   " + YR);
		logger.debug("not depriceted   " + MTH);

		Connection con = null;
		String serviceName = "";
		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt5 = con.createStatement();
			Statement stmt6 = con.createStatement();
			ResultSet rs = null;
			ResultSet RSPLR = null;
			logger.debug("heeeeeee");
			rs = stmt6
					.executeQuery("select service_display_name from st_lms_service_master where service_code='SE' and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}
			ResultSet latestMonth = stmt5.executeQuery(QueryManager
					.getLatestMonthTDSSE());
			while (latestMonth.next()) {
				Calendar rightNow1 = Calendar.getInstance();

				Date dt = latestMonth.getDate("month");
				logger.debug("get date from db " + dt);
				rightNow1.setTime(dt);
				latestyearTaxCal = rightNow1.get(Calendar.YEAR);
				latestmonthTaxCal = rightNow1.get(Calendar.MONTH) + 1;
				logger.debug(" latest year " + latestyearTaxCal);
				logger.debug(" latest month " + latestmonthTaxCal);

				/*
				 * String latestDate=latestMonth.getString("month"); String
				 * latestyear=latestDate.substring(0,4); String
				 * latestmonth=latestDate.substring(5,7);
				 * latestyearTaxCal=Integer.parseInt(latestyear);
				 * latestmonthTaxCal=Integer.parseInt(latestmonth);
				 * 
				 * logger.debug("latest year from task table " + latestyear);
				 * logger.debug("latest month from task table " + latestmonth);
				 */
				isEntry = true;
				break;

			}

			if (isEntry == false) {
				logger.debug("when no entry for tds");
				latestyearTaxCal = depYear;
				if (depMonth == 01) {
					latestmonthTaxCal = 00;
				} else {
					latestmonthTaxCal = depMonth - 1;
				}

			}

			if (YR == latestyearTaxCal)

			{

				logger.debug("inside same year");
				for (int i = latestmonthTaxCal + 1; i < MTH; i++) {

					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = null;
					Date newdate = null;
					taxSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							logger.error("Exception:" + e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							logger.error("Exception:" + e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("9999999999999999999999");
					logger.debug(transaction_date_first);
					// logger.debug(transaction_date_last);

					String queryForPlayer = QueryManager
							.getST3LastMonthTransactionSE()
							+ " where  transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime()) + "' ";
					logger.debug("query----" + queryForPlayer);
					RSPLR = stmt2.executeQuery(queryForPlayer);
					while (RSPLR.next()) {

						double taxAmt = RSPLR
								.getDouble(TableConstants.TAX_AMOUNT);
						logger.debug("tax amount   from table" + taxAmt);
						taxSum = CommonMethods.fmtToTwoDecimal(taxSum + taxAmt);
						logger.debug("calculated tax  " + taxSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						logger.error("Exception:" + e);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.debug("date to insert " + taskDate);

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ taxSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','SE')";
					stmt3.executeUpdate(queryTaskCreate);

				}

			}

			if (YR == latestyearTaxCal + 1) {
				logger.debug("when diff is one");
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");

				for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
					Date dd = null;
					Date ddd = null;
					taxSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10) {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error("Exception:" + e);
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							logger.error("Exception:" + e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}

					String queryForPlayer = QueryManager
							.getST3LastMonthTransactionSE()
							+ " where  transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime()) + "' ";
					logger.debug("query----" + queryForPlayer);
					RSPLR = stmt3.executeQuery(queryForPlayer);
					while (RSPLR.next()) {

						double taxAmt = RSPLR
								.getDouble(TableConstants.TAX_AMOUNT);
						logger.debug("tax amount   from table" + taxAmt);
						taxSum = CommonMethods.fmtToTwoDecimal(taxSum + taxAmt);
						logger.debug("calculated tax  " + taxSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						logger.error("Exception:" + e);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ taxSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','SE')";
					stmt3.executeUpdate(queryTaskCreate);

				}

				for (int i = 1; i < MTH; i++) {
					Date dd = null;
					Date ddd = null;
					taxSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) + "-"
						// + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					} else {

						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) +
						// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("result not  found in task table ");
					String queryForPlayer = QueryManager
							.getST3LastMonthTransactionSE()
							+ " where  transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime()) + "' ";
					logger.debug("query-----" + queryForPlayer);
					RSPLR = stmt4.executeQuery(queryForPlayer);
					while (RSPLR.next()) {

						double taxAmt = RSPLR
								.getDouble(TableConstants.TAX_AMOUNT);
						logger.debug("tax amount   from table" + taxAmt);
						taxSum = CommonMethods.fmtToTwoDecimal(taxSum + taxAmt);
						logger.debug("calculated tax  " + taxSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + (latestyearTaxCal + 1);
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ taxSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','SE')";
					stmt3.executeUpdate(queryTaskCreate);

				}
			}

			if (YR > latestyearTaxCal + 1) {
				logger.debug("when diff is more than one");
				for (int j = latestyearTaxCal; j <= YR; j++) {
					if (j == latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;

						for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
							taxSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + "0" + i + "-" + "01";
							} else {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + i + "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-" + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";

							}

							String queryForPlayer = QueryManager
									.getST3LastMonthTransactionSE()
									+ " where  transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' ";
							RSPLR = stmt3.executeQuery(queryForPlayer);
							while (RSPLR.next()) {

								double taxAmt = RSPLR
										.getDouble(TableConstants.TAX_AMOUNT);
								logger
										.debug("tax amount   from table"
												+ taxAmt);
								taxSum = CommonMethods.fmtToTwoDecimal(taxSum
										+ taxAmt);
								logger.debug("calculated tax  " + taxSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + latestyearTaxCal;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ taxSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','SE')";
							stmt3.executeUpdate(queryTaskCreate);
						}

					}

					if (j == YR) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i < MTH; i++) {
							taxSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + YR + "-" + "0"
										+ i + "-" + "01";
							} else {
								transaction_date_first = "" + YR + "-" + i
										+ "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +YR + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +YR + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}

							System.out
									.println("result not  found in task table ");
							String queryForPlayer = QueryManager
									.getST3LastMonthTransactionSE()
									+ " where  transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' ";
							RSPLR = stmt4.executeQuery(queryForPlayer);
							while (RSPLR.next()) {

								double taxAmt = RSPLR
										.getDouble(TableConstants.TAX_AMOUNT);
								logger
										.debug("tax amount   from table"
												+ taxAmt);
								taxSum = CommonMethods.fmtToTwoDecimal(taxSum
										+ taxAmt);
								logger.debug("calculated tax  " + taxSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + YR;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ taxSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','SE')";
							stmt3.executeUpdate(queryTaskCreate);
						}
					}

					if (j != YR && j != latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i <= 12; i++) {
							taxSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + j + "-" + "0" + i
										+ "-" + "01";
							} else {
								transaction_date_first = "" + j + "-" + i + "-"
										+ "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +j + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";

							} else {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +j + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}
							System.out
									.println("result not  found in task table ");
							String queryForPlayer = QueryManager
									.getST3LastMonthTransactionSE()
									+ " where  transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' ";
							RSPLR = stmt4.executeQuery(queryForPlayer);
							while (RSPLR.next()) {

								double taxAmt = RSPLR
										.getDouble(TableConstants.TAX_AMOUNT);
								logger
										.debug("tax amount   from table"
												+ taxAmt);
								taxSum = CommonMethods.fmtToTwoDecimal(taxSum
										+ taxAmt);
								logger.debug("calculated tax  " + taxSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + j;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ taxSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','TDS','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','SE')";
							stmt3.executeUpdate(queryTaskCreate);

						}
					}

				}
			}

			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();
			logger.debug("333333333333333333hhhhhhhhhhh");
			ResultSet rs3 = stmt4.executeQuery(QueryManager
					.getUnapprovedTDSSE());

			while (rs3.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs3.getDouble("amount"));

				// SimpleDateFormat date1 = new
				// SimpleDateFormat("dd-MM-yyyy");//it would be the format set
				// by Vineet
				// taskBean.setMonth(date1.format(rs3.getDate("month")));
				// logger.debug("heeeeeeeeeeeeellllllll
				// "+date1.format(rs3.getDate("month")) );
				taskBean.setMonth(rs3.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs3
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs3.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs3.getInt(TableConstants.TASK_ID));
				taskBean.setServiceCode("SE");
				taskBean.setServiceName(serviceName);
				logger.debug("taskid is   " + rs3.getInt("task_id"));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(taskBean);
			}

			con.commit();
			// removed by arun // con.close(); // added by yogesh at the time of
			// self review
			return searchResults;

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);

			// added by arun
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// return null;

	}

	public List vatSearchDG(Integer month, Integer year) throws LMSException {
		logger.debug("vatSearchDG called");
		double vatSum = 0;
		boolean tds = false;
		boolean isEntry = false;
		int yearTable = 0;
		int monthTable = 0;

		int latestyearTaxCal = 0;
		int latestmonthTaxCal = 0;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		SimpleDateFormat date = new SimpleDateFormat();

		Calendar rightNow = Calendar.getInstance();

		int YR = rightNow.get(Calendar.YEAR);
		int MTH = rightNow.get(Calendar.MONTH) + 1;

		int depYear = year;
		int depMonth = month;
		logger.debug("inside deploy date changed");

		// int MTH=11;
		logger.debug("month from system  " + MTH);

		logger.debug("not depriceted   " + YR);
		logger.debug("not depriceted   " + MTH);

		Connection con = null;
		String serviceName = "";
		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt5 = con.createStatement();
			PreparedStatement ps6 = con
					.prepareStatement("select service_display_name from st_lms_service_master where service_code='DG' and status='ACTIVE'");

			ResultSet rs = null, rs6 = null;
			rs6 = ps6.executeQuery();
			if (rs6.next()) {
				serviceName = rs6.getString("service_display_name");
			} else {
				serviceName = "";
			}

			ResultSet RSPLR = null;
			logger.debug("heeeeeee");

			ResultSet latestMonth = stmt5.executeQuery(QueryManager
					.getLatestMonthVATDG());
			while (latestMonth.next()) {
				Calendar rightNow1 = Calendar.getInstance();

				Date dt = latestMonth.getDate("month");
				logger.debug("get date from db " + dt);
				rightNow1.setTime(dt);
				latestyearTaxCal = rightNow1.get(Calendar.YEAR);
				latestmonthTaxCal = rightNow1.get(Calendar.MONTH) + 1;
				logger.debug(" latest year " + latestyearTaxCal);
				logger.debug(" latest month " + latestmonthTaxCal);

				/*
				 * String latestDate=latestMonth.getString("month"); String
				 * latestyear=latestDate.substring(0,4); String
				 * latestmonth=latestDate.substring(5,7);
				 * latestyearTaxCal=Integer.parseInt(latestyear);
				 * latestmonthTaxCal=Integer.parseInt(latestmonth);
				 * 
				 * logger.debug("latest year from task table " + latestyear);
				 * logger.debug("latest month from task table " + latestmonth);
				 */
				isEntry = true;
				break;

			}

			if (isEntry == false) {
				logger.debug("when no entry for VAT");
				latestyearTaxCal = depYear;
				if (depMonth == 01) {
					latestmonthTaxCal = 00;
				} else {
					latestmonthTaxCal = depMonth - 1;
				}

			}

			if (YR == latestyearTaxCal)

			{
				logger.debug("inside same year");
				for (int i = latestmonthTaxCal + 1; i < MTH; i++) {

					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = null;
					Date newdate = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}

					String queryForBoAgtVat = "select sum(vatAmt) vatAmt from (select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale_refund sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and sbat.transaction_id=sbtm.transaction_id) sale_ret) union all select (sum(sale_vat-ref_vat)) vatAmt from st_rep_dg_bo where finaldate >= '"+ transaction_date_first+ "' and finaldate< '2012-11-01 00:00:00.0')sd";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.debug("date to insert " + taskDate);

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','DG')";
					stmt3.executeUpdate(queryTaskCreate);

				}

			}

			if (YR == latestyearTaxCal + 1) {
				logger.debug("when diff is one");
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");

				for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
					Date dd = null;
					Date ddd = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10) {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}

					// String queryForBoAgtVat="select (sale.vat_amt -
					// sale_ret.vat_amt) vatAmt from ((select sum(sbat.vat_amt)
					// vat_amt from st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// sale,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)
					// sale_ret)";
					String queryForBoAgtVat = "select sum(vatAmt) vatAmt from (select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale_refund sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and sbat.transaction_id=sbtm.transaction_id) sale_ret) union all select (sum(sale_vat)-sum(ref_vat)) vatAmt from st_rep_dg_bo where finaldate >=  '"+ transaction_date_first+ "' and finaldate< '"+ new java.sql.Timestamp(dd.getTime())+ "')sd";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','DG')";
					stmt3.executeUpdate(queryTaskCreate);

				}

				for (int i = 1; i < MTH; i++) {
					Date dd = null;
					Date ddd = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) + "-"
						// + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					} else {

						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) +
						// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("result not  found in task table ");
					// String queryForBoAgtVat="select (sale.vat_amt -
					// sale_ret.vat_amt) vatAmt from ((select sum(sbat.vat_amt)
					// vat_amt from st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// sale,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)
					// sale_ret)";
					String queryForBoAgtVat = "select sum(vatAmt) vatAmt from (select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale_refund sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and sbat.transaction_id=sbtm.transaction_id) sale_ret) union all select (sum(sale_vat)-sum(ref_vat)) vatAmt from st_rep_dg_bo where finaldate >=  '"+ transaction_date_first+ "' and finaldate< '"+ new java.sql.Timestamp(dd.getTime())+ "')sd";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + (latestyearTaxCal + 1);
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','DG')";
					stmt3.executeUpdate(queryTaskCreate);

				}
			}

			if (YR > latestyearTaxCal + 1) {
				logger.debug("when diff is more then one");
				for (int j = latestyearTaxCal; j <= YR; j++) {
					if (j == latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;

						for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + "0" + i + "-" + "01";
							} else {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + i + "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-" + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";

							}
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select sum(vatAmt) vatAmt from (select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale_refund sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and sbat.transaction_id=sbtm.transaction_id) sale_ret) union all select (sum(sale_vat)-sum(ref_vat)) vatAmt from st_rep_dg_bo where finaldate >=  '"+ transaction_date_first+ "' and finaldate< '"+ new java.sql.Timestamp(dd.getTime())+ "')sd";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + latestyearTaxCal;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','DG')";
							stmt3.executeUpdate(queryTaskCreate);
						}

					}

					if (j == YR) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i < MTH; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + YR + "-" + "0"
										+ i + "-" + "01";
							} else {
								transaction_date_first = "" + YR + "-" + i
										+ "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +YR + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +YR + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}

							System.out
									.println("result not  found in task table ");
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select sum(vatAmt) vatAmt from (select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale_refund sbat where sbtm.transaction_date >= '"	+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and sbat.transaction_id=sbtm.transaction_id) sale_ret) union all select (sum(sale_vat)-sum(ref_vat)) vatAmt from st_rep_dg_bo where finaldate >=  '"+ transaction_date_first+ "' and finaldate< '"+ new java.sql.Timestamp(dd.getTime())+ "')sd";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + YR;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','DG')";
							stmt3.executeUpdate(queryTaskCreate);
						}
					}

					if (j != YR && j != latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i <= 12; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + j + "-" + "0" + i
										+ "-" + "01";
							} else {
								transaction_date_first = "" + j + "-" + i + "-"
										+ "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +j + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";

							} else {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +j + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}
							System.out
									.println("result not  found in task table ");
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select sum(vatAmt) vatAmt from (select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_dg_bo_sale_refund sbat where sbtm.transaction_date >= '"	+ transaction_date_first+ "' and transaction_date < '"+ new java.sql.Timestamp(dd.getTime())+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and sbat.transaction_id=sbtm.transaction_id) sale_ret) union all select (sum(sale_vat)-sum(ref_vat)) vatAmt from st_rep_dg_bo where finaldate >=  '"+ transaction_date_first+ "' and finaldate< '"+ new java.sql.Timestamp(dd.getTime())+ "')sd";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + j;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','DG')";
							stmt3.executeUpdate(queryTaskCreate);

						}
					}

				}
			}

			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();
			logger.debug("333333333333333333hhhhhhhhhhh");
			ResultSet rs3 = stmt4.executeQuery(QueryManager
					.getUnapprovedVATDG());

			while (rs3.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs3.getDouble("amount"));

				// SimpleDateFormat date1 = new
				// SimpleDateFormat("dd-MM-yyyy");//it would be the format set
				// by Vineet
				// taskBean.setMonth(date1.format(rs3.getDate("month")));
				// logger.debug("heeeeeeeeeeeeellllllll
				// "+date1.format(rs3.getDate("month")) );
				taskBean.setMonth(rs3.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs3
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs3.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs3.getInt(TableConstants.TASK_ID));
				taskBean.setServiceCode(rs3.getString("service_code"));
				taskBean.setServiceName(serviceName);
				logger.debug("taskid is   " + rs3.getInt("task_id"));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(taskBean);
			}

			con.commit();
			con.close(); // added by yogesh at the time of self review
			return searchResults;

		} catch (SQLException e) {
			logger.error("Exception:" + e);
			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				logger.error("Exception:" + se);
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);

		}
		// return null;

	}

	//

	public List vatSearchSE(Integer month, Integer year) throws LMSException,
			ParseException {
		logger.debug("vatSearchSE called");
		double vatSum = 0;
		boolean tds = false;
		boolean isEntry = false;
		int yearTable = 0;
		int monthTable = 0;

		int latestyearTaxCal = 0;
		int latestmonthTaxCal = 0;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		SimpleDateFormat date = new SimpleDateFormat();

		Calendar rightNow = Calendar.getInstance();

		int YR = rightNow.get(Calendar.YEAR);
		int MTH = rightNow.get(Calendar.MONTH) + 1;

		int depYear = year;
		int depMonth = month;
		logger.debug("inside deploy date changed");

		// int MTH=11;
		logger.debug("month from system  " + MTH);

		logger.debug("not depriceted   " + YR);
		logger.debug("not depriceted   " + MTH);

		Connection con = null;
		String serviceName = "";
		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt5 = con.createStatement();
			PreparedStatement ps6 = con
					.prepareStatement("select service_display_name from st_lms_service_master where service_code='SE' and status='ACTIVE'");
			ResultSet rs = null, rs6 = null;

			ResultSet RSPLR = null;
			logger.debug("heeeeeee");

			rs6 = ps6.executeQuery();
			if (rs6.next()) {
				serviceName = rs6.getString("service_display_name");
			} else {
				serviceName = "";
			}
			ResultSet latestMonth = stmt5.executeQuery(QueryManager
					.getLatestMonthVATSE());
			while (latestMonth.next()) {
				Calendar rightNow1 = Calendar.getInstance();
				Date dt = latestMonth.getDate("month");
				logger.debug("get date from db " + dt);
				rightNow1.setTime(dt);
				latestyearTaxCal = rightNow1.get(Calendar.YEAR);
				latestmonthTaxCal = rightNow1.get(Calendar.MONTH) + 1;
				logger.debug(" latest year " + latestyearTaxCal);
				logger.debug(" latest month " + latestmonthTaxCal);

				/*
				 * String latestDate=latestMonth.getString("month"); String
				 * latestyear=latestDate.substring(0,4); String
				 * latestmonth=latestDate.substring(5,7);
				 * latestyearTaxCal=Integer.parseInt(latestyear);
				 * latestmonthTaxCal=Integer.parseInt(latestmonth);
				 * 
				 * logger.debug("latest year from task table " + latestyear);
				 * logger.debug("latest month from task table " + latestmonth);
				 */
				isEntry = true;
				break;

			}

			if (isEntry == false) {
				logger.debug("when no entry for VAT");
				latestyearTaxCal = depYear;
				if (depMonth == 01) {
					latestmonthTaxCal = 00;
				} else {
					latestmonthTaxCal = depMonth - 1;
				}

			}

			if (YR == latestyearTaxCal)

			{

				logger.debug("inside same year");
				for (int i = latestmonthTaxCal + 1; i < MTH; i++) {

					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = null;
					Date newdate = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("9999999999999999999999");
					logger.debug(transaction_date_first);
					// logger.debug(transaction_date_last);

					String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";

					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.debug("date to insert " + taskDate);

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','SE')";
					stmt3.executeUpdate(queryTaskCreate);

				}

			}

			if (YR == latestyearTaxCal + 1) {
				logger.debug("when diff is one");
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");

				for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
					Date dd = null;
					Date ddd = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10) {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}

					// String queryForBoAgtVat="select (sale.vat_amt -
					// sale_ret.vat_amt) vatAmt from ((select sum(sbat.vat_amt)
					// vat_amt from st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// sale,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)
					// sale_ret)";
					String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','SE')";
					stmt3.executeUpdate(queryTaskCreate);

				}

				for (int i = 1; i < MTH; i++) {
					Date dd = null;
					Date ddd = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) + "-"
						// + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					} else {

						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) +
						// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("result not  found in task table ");
					// String queryForBoAgtVat="select (sale.vat_amt -
					// sale_ret.vat_amt) vatAmt from ((select sum(sbat.vat_amt)
					// vat_amt from st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// sale,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)
					// sale_ret)";
					String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + (latestyearTaxCal + 1);
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','SE')";
					stmt3.executeUpdate(queryTaskCreate);

				}
			}

			if (YR > latestyearTaxCal + 1) {
				logger.debug("when diff is more then one");
				for (int j = latestyearTaxCal; j <= YR; j++) {
					if (j == latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;

						for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + "0" + i + "-" + "01";
							} else {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + i + "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-" + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";

							}
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + latestyearTaxCal;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','SE')";
							stmt3.executeUpdate(queryTaskCreate);
						}

					}

					if (j == YR) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i < MTH; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + YR + "-" + "0"
										+ i + "-" + "01";
							} else {
								transaction_date_first = "" + YR + "-" + i
										+ "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +YR + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +YR + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}

							System.out
									.println("result not  found in task table ");
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + YR;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','SE')";
							stmt3.executeUpdate(queryTaskCreate);
						}
					}

					if (j != YR && j != latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i <= 12; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + j + "-" + "0" + i
										+ "-" + "01";
							} else {
								transaction_date_first = "" + j + "-" + i + "-"
										+ "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +j + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";

							} else {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +j + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}
							System.out
									.println("result not  found in task table ");
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + j;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','SE')";
							stmt3.executeUpdate(queryTaskCreate);

						}
					}

				}
			}

			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();
			logger.debug("333333333333333333hhhhhhhhhhh");
			ResultSet rs3 = stmt4.executeQuery(QueryManager
					.getUnapprovedVATSE());
			Calendar cal = Calendar.getInstance();
			while (rs3.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs3.getDouble("amount"));

				// SimpleDateFormat date1 = new
				// SimpleDateFormat("dd-MM-yyyy");//it would be the format set
				// by Vineet
				// taskBean.setMonth(date1.format(rs3.getDate("month")));
				// logger.debug("heeeeeeeeeeeeellllllll
				// "+date1.format(rs3.getDate("month")) );

				// cal.setTime(new
				// SimpleDateFormat("yyyy-MM-dd").parse(rs3.getString(TableConstants.MONTH_TASK)));
				// taskBean.setMonth(monthArr[cal.get(Calendar.MONTH)]+","+cal.get(Calendar.YEAR));

				taskBean.setMonth(rs3.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs3
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs3.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs3.getInt(TableConstants.TASK_ID));
				taskBean.setServiceCode(rs3.getString("service_code"));
				taskBean.setServiceName(serviceName);
				logger.debug("taskid is   " + rs3.getInt("task_id"));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(taskBean);
			}

			con.commit();
			con.close(); // added by yogesh at the time of self review
			return searchResults;

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);

		}
		// return null;

	}

	public List vatSearchIW(Integer month, Integer year) throws LMSException,
	ParseException {
		logger.debug("vatSearchIW called");
		double vatSum = 0;
		boolean tds = false;
		boolean isEntry = false;
		int yearTable = 0;
		int monthTable = 0;

		int latestyearTaxCal = 0;
		int latestmonthTaxCal = 0;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		SimpleDateFormat date = new SimpleDateFormat();

		Calendar rightNow = Calendar.getInstance();

		int YR = rightNow.get(Calendar.YEAR);
		int MTH = rightNow.get(Calendar.MONTH) + 1;

		int depYear = year;
		int depMonth = month;
		logger.debug("inside deploy date changed");

		// int MTH=11;
		logger.debug("month from system  " + MTH);

		logger.debug("not depriceted   " + YR);
		logger.debug("not depriceted   " + MTH);

		Connection con = null;
		String serviceName = "";
		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt5 = con.createStatement();
			PreparedStatement ps6 = con
					.prepareStatement("select service_display_name from st_lms_service_master where service_code='IW' and status='ACTIVE'");
			ResultSet rs = null, rs6 = null;

			ResultSet RSPLR = null;
			logger.debug("heeeeeee");

			rs6 = ps6.executeQuery();
			if (rs6.next()) {
				serviceName = rs6.getString("service_display_name");
			} else {
				serviceName = "";
			}
			ResultSet latestMonth = stmt5.executeQuery(QueryManager
					.getLatestMonthVATIW());
			while (latestMonth.next()) {
				Calendar rightNow1 = Calendar.getInstance();
				Date dt = latestMonth.getDate("month");
				logger.debug("get date from db " + dt);
				rightNow1.setTime(dt);
				latestyearTaxCal = rightNow1.get(Calendar.YEAR);
				latestmonthTaxCal = rightNow1.get(Calendar.MONTH) + 1;
				logger.debug(" latest year " + latestyearTaxCal);
				logger.debug(" latest month " + latestmonthTaxCal);

				/*
				 * String latestDate=latestMonth.getString("month"); String
				 * latestyear=latestDate.substring(0,4); String
				 * latestmonth=latestDate.substring(5,7);
				 * latestyearTaxCal=Integer.parseInt(latestyear);
				 * latestmonthTaxCal=Integer.parseInt(latestmonth);
				 * 
				 * logger.debug("latest year from task table " + latestyear);
				 * logger.debug("latest month from task table " + latestmonth);
				 */
				isEntry = true;
				break;

			}

			if (isEntry == false) {
				logger.debug("when no entry for VAT");
				latestyearTaxCal = depYear;
				if (depMonth == 01) {
					latestmonthTaxCal = 00;
				} else {
					latestmonthTaxCal = depMonth - 1;
				}

			}

			if (YR == latestyearTaxCal)

			{

				logger.debug("inside same year");
				for (int i = latestmonthTaxCal + 1; i < MTH; i++) {

					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = null;
					Date newdate = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);
						try {
							newdate = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(newdate);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("9999999999999999999999");
					logger.debug(transaction_date_first);
					// logger.debug(transaction_date_last);

					String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and sbtm.transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and sbtm.transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";

					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.debug("date to insert " + taskDate);

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','IW')";
					stmt3.executeUpdate(queryTaskCreate);

				}

			}

			if (YR == latestyearTaxCal + 1) {
				logger.debug("when diff is one");
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dtfmt = new SimpleDateFormat("yyyy-MM-dd");

				for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
					Date dd = null;
					Date ddd = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + latestyearTaxCal + "-"
								+ i + "-" + "01";
					}

					if (i < 10) {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-" +
						// "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";

					} else {
						int day = 1;
						cal.set(latestyearTaxCal, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal, i - 1, totalday);

						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();

						// transaction_date_last="" +latestyearTaxCal + "-"+i
						// +"-"+ "31"+""+"23:"+"59:"+"59";
					}

					// String queryForBoAgtVat="select (sale.vat_amt -
					// sale_ret.vat_amt) vatAmt from ((select sum(sbat.vat_amt)
					// vat_amt from st_lms_bo_transaction_master
					// sbtm,st_iw_bo_sale sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// sale,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_iw_bo_sale sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)
					// sale_ret)";
					String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and sbtm.transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and sbtm.transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + latestyearTaxCal;
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','IW')";
					stmt3.executeUpdate(queryTaskCreate);

				}

				for (int i = 1; i < MTH; i++) {
					Date dd = null;
					Date ddd = null;
					vatSum = 0.0;
					String transaction_date_first;
					String transaction_date_last;
					if (i < 10) {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + "0" + i + "-" + "01";
					} else {
						transaction_date_first = "" + (latestyearTaxCal + 1)
								+ "-" + i + "-" + "01";
					}

					if (i < 10)

					{
						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) + "-"
						// + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					} else {

						int day = 1;
						cal.set(latestyearTaxCal + 1, i - 1, day);
						int totalday = cal
								.getActualMaximum(Calendar.DAY_OF_MONTH);
						cal.set(latestyearTaxCal + 1, i - 1, totalday);
						try {
							ddd = dtfmt.parse(dtfmt.format(cal.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cal.setTime(ddd);
						long ms = cal.getTimeInMillis() + 1000 * 24 * 60 * 60;
						cal.setTimeInMillis(ms);
						dd = cal.getTime();
						// transaction_date_last="" +(latestyearTaxCal+1) +
						// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";
					}
					logger.debug("result not  found in task table ");
					// String queryForBoAgtVat="select (sale.vat_amt -
					// sale_ret.vat_amt) vatAmt from ((select sum(sbat.vat_amt)
					// vat_amt from st_lms_bo_transaction_master
					// sbtm,st_iw_bo_sale sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// sale,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_iw_bo_sale sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and sbtm.transaction_type in
					// ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)
					// sale_ret)";
					String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and sbtm.transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					System.out
							.println("query---- for vat calculation ???????????  "
									+ queryForBoAgtVat);
					RSPLR = stmt2.executeQuery(queryForBoAgtVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						System.out
								.println("calculated vat is    @@  " + vatSum);

					}

					/*
					 * try{ taxSum=Double.parseDouble(nf.format(taxSum)); }
					 * catch(NumberFormatException e) {}
					 */

					String monthfortask;
					if (i < 10) {
						monthfortask = "-" + "0" + i;
					} else {
						monthfortask = "-" + i;
					}
					String yearfortask = "" + (latestyearTaxCal + 1);
					String taskDate1 = yearfortask.concat(monthfortask).concat(
							"-01");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					Date taskDate = null;
					try {
						taskDate = d.parse(taskDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String queryTaskCreate = QueryManager.getST3CreateTask()
							+ "values('"
							+ vatSum
							+ "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
							+ new java.sql.Date(getFirstDay(taskDate).getTime())
							+ "' ,'"
							+ new java.sql.Date(getLastDay(taskDate).getTime())
							+ "','IW')";
					stmt3.executeUpdate(queryTaskCreate);

				}
			}

			if (YR > latestyearTaxCal + 1) {
				logger.debug("when diff is more then one");
				for (int j = latestyearTaxCal; j <= YR; j++) {
					if (j == latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;

						for (int i = latestmonthTaxCal + 1; i <= 12; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + "0" + i + "-" + "01";
							} else {
								transaction_date_first = "" + latestyearTaxCal
										+ "-" + i + "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-" + "0"+i +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(latestyearTaxCal, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(latestyearTaxCal, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +latestyearTaxCal +
								// "-"+i +"-"+ "31"+""+"23:"+"59:"+"59";

							}
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_iw_bo_sale sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_iw_bo_sale sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and sbtm.transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and sbtm.transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + latestyearTaxCal;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','IW')";
							stmt3.executeUpdate(queryTaskCreate);
						}

					}

					if (j == YR) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i < MTH; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + YR + "-" + "0"
										+ i + "-" + "01";
							} else {
								transaction_date_first = "" + YR + "-" + i
										+ "-" + "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +YR + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";
							} else {
								int day = 1;
								cal.set(YR, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(YR, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +YR + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}

							System.out
									.println("result not  found in task table ");
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_iw_bo_sale sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_iw_bo_sale sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and sbtm.transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and sbtm.transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + YR;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','IW')";
							stmt3.executeUpdate(queryTaskCreate);
						}
					}

					if (j != YR && j != latestyearTaxCal) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dtfmt = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = null;
						Date ddd = null;
						for (int i = 1; i <= 12; i++) {
							vatSum = 0;
							String transaction_date_first;
							String transaction_date_last;
							if (i < 10) {
								transaction_date_first = "" + j + "-" + "0" + i
										+ "-" + "01";
							} else {
								transaction_date_first = "" + j + "-" + i + "-"
										+ "01";
							}

							if (i < 10) {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();

								// transaction_date_last="" +j + "-" + "0"+i
								// +"-"+ "31"+""+"23:"+"59:"+"59";

							} else {
								int day = 1;
								cal.set(j, i - 1, day);
								int totalday = cal
										.getActualMaximum(Calendar.DAY_OF_MONTH);
								cal.set(j, i - 1, totalday);
								try {
									ddd = dtfmt.parse(dtfmt.format(cal
											.getTime()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cal.setTime(ddd);
								long ms = cal.getTimeInMillis() + 1000 * 24
										* 60 * 60;
								cal.setTimeInMillis(ms);
								dd = cal.getTime();
								// transaction_date_last="" +j + "-"+i +"-"+
								// "31"+""+"23:"+"59:"+"59";
							}
							System.out
									.println("result not  found in task table ");
							// String queryForBoAgtVat="select (sale.vat_amt -
							// sale_ret.vat_amt) vatAmt from ((select
							// sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_iw_bo_sale sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_iw_bo_sale sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and sbtm.transaction_type
							// in ('SALE_RET') and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_ret)";
							String queryForBoAgtVat = "select (sale.vat_amt - sale_ret.vat_amt) vatAmt from ((select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and sbtm.transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'  and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id) sale,(select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_bo_transaction_master sbtm,st_iw_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and sbtm.transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id) sale_ret)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							System.out
									.println("query---- for vat calculation ???????????  "
											+ queryForBoAgtVat);
							RSPLR = stmt2.executeQuery(queryForBoAgtVat);
							while (RSPLR.next()) {

								double vatAmt = RSPLR.getDouble("vatAmt");
								logger
										.debug("vat amount   from table"
												+ vatAmt);
								vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
								logger.debug("calculated vat is    @@  "
										+ vatSum);

							}

							/*
							 * try{
							 * taxSum=Double.parseDouble(nf.format(taxSum)); }
							 * catch(NumberFormatException e) {}
							 */

							String monthfortask;
							if (i < 10) {
								monthfortask = "-" + "0" + i;
							} else {
								monthfortask = "-" + i;
							}
							String yearfortask = "" + j;
							String taskDate1 = yearfortask.concat(monthfortask)
									.concat("-01");
							SimpleDateFormat d = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date taskDate = null;
							try {
								taskDate = d.parse(taskDate1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String queryTaskCreate = QueryManager
									.getST3CreateTask()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE, '"
									+ new java.sql.Date(getFirstDay(taskDate)
											.getTime())
									+ "' ,'"
									+ new java.sql.Date(getLastDay(taskDate)
											.getTime()) + "','IW')";
							stmt3.executeUpdate(queryTaskCreate);

						}
					}

				}
			}

			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();
			logger.debug("333333333333333333hhhhhhhhhhh");
			ResultSet rs3 = stmt4.executeQuery(QueryManager
					.getUnapprovedVATIW());
			Calendar cal = Calendar.getInstance();
			while (rs3.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs3.getDouble("amount"));

				// SimpleDateFormat date1 = new
				// SimpleDateFormat("dd-MM-yyyy");//it would be the format set
				// by Vineet
				// taskBean.setMonth(date1.format(rs3.getDate("month")));
				// logger.debug("heeeeeeeeeeeeellllllll
				// "+date1.format(rs3.getDate("month")) );

				// cal.setTime(new
				// SimpleDateFormat("yyyy-MM-dd").parse(rs3.getString(TableConstants.MONTH_TASK)));
				// taskBean.setMonth(monthArr[cal.get(Calendar.MONTH)]+","+cal.get(Calendar.YEAR));

				taskBean.setMonth(rs3.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs3
						.getString(TableConstants.TASK_TYPE));
				taskBean.setStatus(rs3.getString(TableConstants.TASK_STATUS));
				taskBean.setTaskId(rs3.getInt(TableConstants.TASK_ID));
				taskBean.setServiceCode(rs3.getString("service_code"));
				taskBean.setServiceName(serviceName);
				logger.debug("taskid is   " + rs3.getInt("task_id"));
				// userBean.setRegisterDate(resultSet.getDate(TableConstants.Register_DATE));
				searchResults.add(taskBean);
			}

			con.commit();
			con.close(); // added by yogesh at the time of self review
			return searchResults;

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);

		}
		// return null;

}

}
