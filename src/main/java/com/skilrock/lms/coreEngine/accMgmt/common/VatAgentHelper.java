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
public class VatAgentHelper {
	/**
	 * This method is used to calculate VAT for a particular month
	 * 
	 * @param month
	 * @param year
	 * @returns the List of TaskBean type
	 * @throws LMSException
	 */

	static Log logger = LogFactory.getLog(VatAgentHelper.class);

	public List vatSearchDG(Integer month, Integer year, Integer userOrgId,
			Integer agtUserId) throws LMSException {
		logger.debug("vatSearchDG called ");
		double vatSum = 0;
		boolean tds = false;
		boolean isEntry = false;
		int yearTable = 0;
		int monthTable = 0;

		int latestyearTaxCal = 0;
		int latestmonthTaxCal = 0;
		String serviceName = "";

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

		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt6 = con.createStatement();
			PreparedStatement stmt5;

			ResultSet rs = null;
			ResultSet RSPLR = null;
			logger.debug("heeeeeee");
			rs = stmt6
					.executeQuery("select service_display_name from st_lms_service_master where service_code='DG' and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}
			stmt5 = con.prepareStatement(QueryManager.getLatestMonthVATAgtDG());
			stmt5.setInt(1, userOrgId);
			ResultSet latestMonth = stmt5.executeQuery();
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

					// String queryForAgtRetVat="select
					// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
					// ((select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and
					// sbat.agent_org_id="+userOrgId+" and sbtm.transaction_type
					// in ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// purch,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_agent_transaction_master
					// sbtm,st_se_agent_retailer_transaction sbat where
					// sbtm.transaction_date >='"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.transaction_type in ('SALE') and
					// sbat.agent_id="+agtUserId+" and
					// sbat.transaction_id=sbtm.transaction_id) sale_retailer)";

					String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and            transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('DG_SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'and sbtm.transaction_type in ('DG_SALE') and   sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.transaction_type in ('DG_SALE_RET') and sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";

					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					logger.debug("query---- for vat calculation ???????????  "
							+ queryForAgtRetVat);
					RSPLR = stmt2.executeQuery(queryForAgtRetVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						logger.debug("calculated vat is    @@  " + vatSum);

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

					String queryTaskCreate = QueryManager.getST3CreateTaskAgt()
							+ "values('" + vatSum + "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE," + userOrgId
							+ ",'DG')";
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

					// String queryForAgtRetVat="select
					// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
					// ((select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and
					// sbat.agent_org_id="+userOrgId+" and sbtm.transaction_type
					// in ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// purch,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_agent_transaction_master
					// sbtm,st_se_agent_retailer_transaction sbat where
					// sbtm.transaction_date >='"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.transaction_type in ('SALE') and
					// sbat.agent_id="+agtUserId+" and
					// sbat.transaction_id=sbtm.transaction_id) sale_retailer)";
					String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and            transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('DG_SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'and sbtm.transaction_type in ('DG_SALE') and   sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.transaction_type in ('DG_SALE_RET') and sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					logger.debug("query---- for vat calculation ???????????  "
							+ queryForAgtRetVat);
					RSPLR = stmt2.executeQuery(queryForAgtRetVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						logger.debug("calculated vat is    @@  " + vatSum);

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

					String queryTaskCreate = QueryManager.getST3CreateTaskAgt()
							+ "values('" + vatSum + "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE," + userOrgId
							+ ",'DG')";
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
					// String queryForAgtRetVat="select
					// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
					// ((select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and
					// sbat.agent_org_id="+userOrgId+" and sbtm.transaction_type
					// in ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// purch,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_agent_transaction_master
					// sbtm,st_se_agent_retailer_transaction sbat where
					// sbtm.transaction_date >='"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.transaction_type in ('SALE') and
					// sbat.agent_id="+agtUserId+" and
					// sbat.transaction_id=sbtm.transaction_id) sale_retailer)";
					String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and            transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('DG_SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'and sbtm.transaction_type in ('DG_SALE') and   sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.transaction_type in ('DG_SALE_RET') and sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					logger.debug("query---- for vat calculation ???????????  "
							+ queryForAgtRetVat);
					RSPLR = stmt2.executeQuery(queryForAgtRetVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						logger.debug("calculated vat is    @@  " + vatSum);

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
					String queryTaskCreate = QueryManager.getST3CreateTaskAgt()
							+ "values('" + vatSum + "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE," + userOrgId
							+ ",'DG')";
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
							// String queryForAgtRetVat="select
							// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
							// ((select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and
							// sbat.agent_org_id="+userOrgId+" and
							// sbtm.transaction_type in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// purch,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_agent_transaction_master
							// sbtm,st_se_agent_retailer_transaction sbat where
							// sbtm.transaction_date
							// >='"+transaction_date_first+"' and
							// transaction_date < '"+new
							// java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.transaction_type in ('SALE') and
							// sbat.agent_id="+agtUserId+" and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_retailer)";
							String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and            transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('DG_SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'and sbtm.transaction_type in ('DG_SALE') and   sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.transaction_type in ('DG_SALE_RET') and sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							logger
									.debug("query---- for vat calculation ???????????  "
											+ queryForAgtRetVat);
							RSPLR = stmt2.executeQuery(queryForAgtRetVat);
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
									.getST3CreateTaskAgt()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE,"
									+ userOrgId + ",'DG')";
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

							logger.debug("result not  found in task table ");
							// String queryForAgtRetVat="select
							// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
							// ((select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and
							// sbat.agent_org_id="+userOrgId+" and
							// sbtm.transaction_type in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// purch,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_agent_transaction_master
							// sbtm,st_se_agent_retailer_transaction sbat where
							// sbtm.transaction_date
							// >='"+transaction_date_first+"' and
							// transaction_date < '"+new
							// java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.transaction_type in ('SALE') and
							// sbat.agent_id="+agtUserId+" and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_retailer)";
							String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and            transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('DG_SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'and sbtm.transaction_type in ('DG_SALE') and   sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.transaction_type in ('DG_SALE_RET') and sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							logger
									.debug("query---- for vat calculation ???????????  "
											+ queryForAgtRetVat);
							RSPLR = stmt2.executeQuery(queryForAgtRetVat);
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
									.getST3CreateTaskAgt()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE,"
									+ userOrgId + ",'DG')";
							stmt3.executeUpdate(queryTaskCreate);
						}
					}

					if (j != YR) {
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
							logger.debug("result not  found in task table ");
							// String queryForAgtRetVat="select
							// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
							// ((select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and
							// sbat.agent_org_id="+userOrgId+" and
							// sbtm.transaction_type in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// purch,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_agent_transaction_master
							// sbtm,st_se_agent_retailer_transaction sbat where
							// sbtm.transaction_date
							// >='"+transaction_date_first+"' and
							// transaction_date < '"+new
							// java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.transaction_type in ('SALE') and
							// sbat.agent_id="+agtUserId+" and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_retailer)";
							String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('DG_SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_dg_bo_sale sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and            transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('DG_SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'and sbtm.transaction_type in ('DG_SALE') and   sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_dg_agt_sale sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.transaction_type in ('DG_SALE_RET') and sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							logger
									.debug("query---- for vat calculation ???????????  "
											+ queryForAgtRetVat);
							RSPLR = stmt2.executeQuery(queryForAgtRetVat);
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
									.getST3CreateTaskAgt()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE,"
									+ userOrgId + ",'DG')";
							stmt3.executeUpdate(queryTaskCreate);

						}
					}

				}
			}

			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();
			logger.debug("333333333333333333hhhhhhhhhhh");
			ResultSet rs3 = stmt4.executeQuery(QueryManager
					.getUnapprovedVATAgtDG()
					+ userOrgId);

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

	public List vatSearchSE(Integer month, Integer year, Integer userOrgId,
			Integer agtUserId) throws LMSException {
		logger.debug("vatSearchSE called ");
		double vatSum = 0;
		boolean tds = false;
		boolean isEntry = false;
		int yearTable = 0;
		int monthTable = 0;

		int latestyearTaxCal = 0;
		int latestmonthTaxCal = 0;
		String serviceName = "";

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

		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt6 = con.createStatement();
			PreparedStatement stmt5;

			ResultSet rs = null;
			ResultSet RSPLR = null;
			logger.debug("heeeeeee");
			rs = stmt6
					.executeQuery("select service_display_name from st_lms_service_master where service_code='SE' and status='ACTIVE'");
			if (rs.next()) {
				serviceName = rs.getString("service_display_name");
			}
			stmt5 = con.prepareStatement(QueryManager.getLatestMonthVATAgtSE());
			stmt5.setInt(1, userOrgId);
			ResultSet latestMonth = stmt5.executeQuery();
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

					// String queryForAgtRetVat="select
					// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
					// ((select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and
					// sbat.agent_org_id="+userOrgId+" and sbtm.transaction_type
					// in ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// purch,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_agent_transaction_master
					// sbtm,st_se_agent_retailer_transaction sbat where
					// sbtm.transaction_date >='"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.transaction_type in ('SALE') and
					// sbat.agent_id="+agtUserId+" and
					// sbat.transaction_id=sbtm.transaction_id) sale_retailer)";

					String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and            transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'and sbtm.transaction_type in ('SALE') and   sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.transaction_type in ('SALE_RET') and sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";

					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					logger.debug("query---- for vat calculation ???????????  "
							+ queryForAgtRetVat);
					RSPLR = stmt2.executeQuery(queryForAgtRetVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						logger.debug("calculated vat is    @@  " + vatSum);

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

					String queryTaskCreate = QueryManager.getST3CreateTaskAgt()
							+ "values('" + vatSum + "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE," + userOrgId
							+ ",'SE')";
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

					// String queryForAgtRetVat="select
					// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
					// ((select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and
					// sbat.agent_org_id="+userOrgId+" and sbtm.transaction_type
					// in ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// purch,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_agent_transaction_master
					// sbtm,st_se_agent_retailer_transaction sbat where
					// sbtm.transaction_date >='"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.transaction_type in ('SALE') and
					// sbat.agent_id="+agtUserId+" and
					// sbat.transaction_id=sbtm.transaction_id) sale_retailer)";
					String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and            transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'and sbtm.transaction_type in ('SALE') and   sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.transaction_type in ('SALE_RET') and sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					logger.debug("query---- for vat calculation ???????????  "
							+ queryForAgtRetVat);
					RSPLR = stmt2.executeQuery(queryForAgtRetVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						logger.debug("calculated vat is    @@  " + vatSum);

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

					String queryTaskCreate = QueryManager.getST3CreateTaskAgt()
							+ "values('" + vatSum + "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE," + userOrgId
							+ ",'SE')";
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
					// String queryForAgtRetVat="select
					// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
					// ((select sum(sbat.vat_amt) vat_amt from
					// st_lms_bo_transaction_master
					// sbtm,st_se_bo_agent_transaction sbat where
					// sbtm.transaction_date >= '"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.party_type='AGENT' and
					// sbat.agent_org_id="+userOrgId+" and sbtm.transaction_type
					// in ('SALE') and sbat.transaction_id=sbtm.transaction_id)
					// purch,(select sum(sbat.vat_amt) vat_amt from
					// st_lms_agent_transaction_master
					// sbtm,st_se_agent_retailer_transaction sbat where
					// sbtm.transaction_date >='"+transaction_date_first+"' and
					// transaction_date < '"+new
					// java.sql.Timestamp(dd.getTime())+"' and
					// sbtm.transaction_type in ('SALE') and
					// sbat.agent_id="+agtUserId+" and
					// sbat.transaction_id=sbtm.transaction_id) sale_retailer)";
					String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
							+ transaction_date_first
							+ "' and            transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
							+ userOrgId
							+ " and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "'and sbtm.transaction_type in ('SALE') and   sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
							+ transaction_date_first
							+ "' and transaction_date < '"
							+ new java.sql.Timestamp(dd.getTime())
							+ "' and sbtm.transaction_type in ('SALE_RET') and sbat.agent_org_id="
							+ userOrgId
							+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
					// String
					// queryForPlayer=QueryManager.getST3LastMonthTransaction()
					// + " where transaction_date >=
					// '"+transaction_date_first+"' and transaction_date <
					// '"+new java.sql.Timestamp(dd.getTime())+"' ";
					logger.debug("query---- for vat calculation ???????????  "
							+ queryForAgtRetVat);
					RSPLR = stmt2.executeQuery(queryForAgtRetVat);
					while (RSPLR.next()) {

						double vatAmt = RSPLR.getDouble("vatAmt");
						logger.debug("vat amount   from table" + vatAmt);
						vatSum = CommonMethods.fmtToTwoDecimal(vatAmt);
						logger.debug("calculated vat is    @@  " + vatSum);

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
					String queryTaskCreate = QueryManager.getST3CreateTaskAgt()
							+ "values('" + vatSum + "','"
							+ new java.sql.Date(taskDate.getTime())
							+ "','VAT','UNAPPROVED',CURRENT_DATE," + userOrgId
							+ ",'SE')";
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
							// String queryForAgtRetVat="select
							// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
							// ((select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and
							// sbat.agent_org_id="+userOrgId+" and
							// sbtm.transaction_type in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// purch,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_agent_transaction_master
							// sbtm,st_se_agent_retailer_transaction sbat where
							// sbtm.transaction_date
							// >='"+transaction_date_first+"' and
							// transaction_date < '"+new
							// java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.transaction_type in ('SALE') and
							// sbat.agent_id="+agtUserId+" and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_retailer)";
							String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and            transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'and sbtm.transaction_type in ('SALE') and   sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.transaction_type in ('SALE_RET') and sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							logger
									.debug("query---- for vat calculation ???????????  "
											+ queryForAgtRetVat);
							RSPLR = stmt2.executeQuery(queryForAgtRetVat);
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
									.getST3CreateTaskAgt()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE,"
									+ userOrgId + ",'SE')";
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

							logger.debug("result not  found in task table ");
							// String queryForAgtRetVat="select
							// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
							// ((select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and
							// sbat.agent_org_id="+userOrgId+" and
							// sbtm.transaction_type in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// purch,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_agent_transaction_master
							// sbtm,st_se_agent_retailer_transaction sbat where
							// sbtm.transaction_date
							// >='"+transaction_date_first+"' and
							// transaction_date < '"+new
							// java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.transaction_type in ('SALE') and
							// sbat.agent_id="+agtUserId+" and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_retailer)";
							String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and            transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'and sbtm.transaction_type in ('SALE') and   sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.transaction_type in ('SALE_RET') and sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							logger
									.debug("query---- for vat calculation ???????????  "
											+ queryForAgtRetVat);
							RSPLR = stmt2.executeQuery(queryForAgtRetVat);
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
									.getST3CreateTaskAgt()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE,"
									+ userOrgId + ",'SE')";
							stmt3.executeUpdate(queryTaskCreate);
						}
					}

					if (j != YR) {
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
							logger.debug("result not  found in task table ");
							// String queryForAgtRetVat="select
							// (sale_retailer.vat_amt-purch.vat_amt) vatAmt from
							// ((select sum(sbat.vat_amt) vat_amt from
							// st_lms_bo_transaction_master
							// sbtm,st_se_bo_agent_transaction sbat where
							// sbtm.transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.party_type='AGENT' and
							// sbat.agent_org_id="+userOrgId+" and
							// sbtm.transaction_type in ('SALE') and
							// sbat.transaction_id=sbtm.transaction_id)
							// purch,(select sum(sbat.vat_amt) vat_amt from
							// st_lms_agent_transaction_master
							// sbtm,st_se_agent_retailer_transaction sbat where
							// sbtm.transaction_date
							// >='"+transaction_date_first+"' and
							// transaction_date < '"+new
							// java.sql.Timestamp(dd.getTime())+"' and
							// sbtm.transaction_type in ('SALE') and
							// sbat.agent_id="+agtUserId+" and
							// sbat.transaction_id=sbtm.transaction_id)
							// sale_retailer)";
							String queryForAgtRetVat = "select (sale_retailer.vatAmt-purchvat.vatAmt) vatAmt from  (( select (purchSalevat.vat_amt-purchSaleretvat.vat_amt) vatAmt from ( select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT'  and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('SALE') and sbat.transaction_id=sbtm.transaction_id)purchSalevat,(select ifnull(sum(sbat.vat_amt),0) vat_amt from  st_lms_bo_transaction_master sbtm,st_se_bo_agent_transaction sbat where sbtm.transaction_date >= '"
									+ transaction_date_first
									+ "' and            transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.party_type='AGENT' and sbat.agent_org_id="
									+ userOrgId
									+ " and sbtm.transaction_type in ('SALE_RET') and sbat.transaction_id=sbtm.transaction_id)purchSaleretvat) purchvat, (select (saleVat.vat_amt-saleRetVat.vat_amt) vatAmt from (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "'and sbtm.transaction_type in ('SALE') and   sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleVat, (select ifnull(sum(sbat.vat_amt),0) vat_amt from st_lms_agent_transaction_master sbtm,st_se_agent_retailer_transaction sbat where sbtm.transaction_date >='"
									+ transaction_date_first
									+ "' and transaction_date < '"
									+ new java.sql.Timestamp(dd.getTime())
									+ "' and sbtm.transaction_type in ('SALE_RET') and sbat.agent_org_id="
									+ userOrgId
									+ " and sbat.transaction_id=sbtm.transaction_id)saleRetVat) sale_retailer)";
							// String
							// queryForPlayer=QueryManager.getST3LastMonthTransaction()
							// + " where transaction_date >=
							// '"+transaction_date_first+"' and transaction_date
							// < '"+new java.sql.Timestamp(dd.getTime())+"' ";
							logger
									.debug("query---- for vat calculation ???????????  "
											+ queryForAgtRetVat);
							RSPLR = stmt2.executeQuery(queryForAgtRetVat);
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
									.getST3CreateTaskAgt()
									+ "values('"
									+ vatSum
									+ "','"
									+ new java.sql.Date(taskDate.getTime())
									+ "','VAT','UNAPPROVED',CURRENT_DATE,"
									+ userOrgId + ",'SE')";
							stmt3.executeUpdate(queryTaskCreate);

						}
					}

				}
			}

			TaskBean taskBean = null;

			List<TaskBean> searchResults = new ArrayList<TaskBean>();
			logger.debug("333333333333333333hhhhhhhhhhh");
			ResultSet rs3 = stmt4.executeQuery(QueryManager
					.getUnapprovedVATAgtSE()
					+ userOrgId);

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
