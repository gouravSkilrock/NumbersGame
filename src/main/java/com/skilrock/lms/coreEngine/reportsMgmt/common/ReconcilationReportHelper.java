package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.CashChqPmntBean;
import com.skilrock.lms.beans.ConsolidatedBean;
import com.skilrock.lms.beans.PWTPaymentsBean;
import com.skilrock.lms.beans.ReportBean;
import com.skilrock.lms.beans.SalePurchaseBean;
import com.skilrock.lms.beans.StockMrpDiscountBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

public class ReconcilationReportHelper {
	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Connection con = DBConnect.getConnection();
		CashChqPmntBean cashChqPmntBean = null;
		try {
			Map<String, CashChqPmntBean> map = new ReconcilationReportHelper()
					.getPaymentBeanList(con, 153, new Timestamp(dateFormat
							.parse("2009-12-20 00:00:00").getTime()),
							new Timestamp(dateFormat.parse(
									"2009-12-21 00:00:00").getTime()));
			Iterator saleItr = map.entrySet().iterator();
			while (saleItr.hasNext()) {
				Map.Entry<String, CashChqPmntBean> saleItrpair = (Map.Entry<String, CashChqPmntBean>) saleItr
						.next();
				cashChqPmntBean = saleItrpair.getValue();
				System.out.println("Key Type " + saleItrpair.getKey());
				System.out.println("Amount "
						+ cashChqPmntBean.getPaymentAmount());
				System.out.println("Type " + cashChqPmntBean.getPaymentType());
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private double closingBal = 0.0;
	private double closingBal2 = 0.0;
	private double finalclosingBal = 0.0;
	private double finalopeningBal = 0.0;
	private double openingBal = 0.0;
	private double openingBal2 = 0.0;
	private PreparedStatement prepstatement;
	// Date present_date = null;
	Timestamp presentDate = null;
	private ResultSet resultSet;
	private ResultSet resultSet2;
	private ResultSet resultSet3;
	// private Connection con;
	private Statement statement;
	private Statement statement2;
	private Statement statement3;
	Timestamp toDate = null;
	private double totalAmount = 0.0;
	double totalPurchAmount = 0.0;
	double totalSaleAmount = 0.0;
	int totalSaleBooks = 0;
	double totalSaleComm = 0.0;

	double totalSaleMrp = 0.0;

	public List createConReportAftrPmnt(int agtOrgId, Timestamp from,
			Timestamp toDate, SimpleDateFormat dateformat1) {
		double weeklyOpeningBal = 0.0;
		String reconReportTypeAgt = null;
		List consolidatedList = new ArrayList();
		ConsolidatedBean consolidatedBean = null;
		ReconcilationReportHelper agtreconhelper = new ReconcilationReportHelper();
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			System.out.println(" ======createConsolidatedReport222 ========");

			System.out.println("Agent org ID ===== " + agtOrgId);
			reconReportTypeAgt = agtreconhelper.getReconRepType(agtOrgId);
			weeklyOpeningBal = getPrvBalAftrpmnt(agtOrgId, from,
					reconReportTypeAgt);
			System.out.println("weeklyOpeningBal  ====== " + weeklyOpeningBal);
			consolidatedBean = getConsDetAftrPmt(con, agtOrgId, from, toDate,
					weeklyOpeningBal, reconReportTypeAgt);
			consolidatedBean.setWeeklyOpeningBal(weeklyOpeningBal);
			consolidatedList.add(consolidatedBean);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(toDate.getTime());
			if (!dateformat1.format(calendar.getTime()).equals(
					dateformat1.format(from))) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				System.out.println(" inside if calendar Agt "
						+ dateformat1.format(from) + "  cTo "
						+ dateformat1.format(calendar.getTime()));
			}
			System.out.println(" inside if calendar Agt "
					+ dateformat1.format(from) + "  cTo "
					+ dateformat1.format(calendar.getTime()));

			consolidatedList.add(dateformat1.format(from)); // 1 in list
			consolidatedList.add(dateformat1.format(calendar.getTime()));// 2
		} catch (LMSException e) {
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return consolidatedList;
	}

	public List createConsolidatedReport(Map agtOrgIdMap, Timestamp from,
			Timestamp toDate, SimpleDateFormat dateformat1) {
		double opLedbalance = 0.0;
		double closLedbalance = 0.0;
		double weeklyOpeningBal = 0.0;
		String reconReportTypeAgt = null;
		List consolidatedList = new ArrayList();
		ConsolidatedBean consolidatedBean = null;
		Timestamp regDate = null;
		Timestamp fromDate = null;
		ReconcilationReportHelper agtreconhelper = new ReconcilationReportHelper();
		Iterator agtOrgitr = agtOrgIdMap.entrySet().iterator();
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			System.out.println(" ======createConsolidatedReport ========");
			while (agtOrgitr.hasNext()) {
				Map.Entry<Integer, String> agtOrgpair = (Map.Entry<Integer, String>) agtOrgitr
						.next();
				int agtOrgId = agtOrgpair.getKey();
				statement = con.createStatement();
				resultSet = statement
						.executeQuery("select transaction_date from st_lms_bo_transaction_master where party_id='"
								+ agtOrgId
								+ "' order by transaction_date  limit 1");
				while (resultSet.next()) {
					regDate = resultSet.getTimestamp("transaction_date");
				}
				if (regDate != null) {
					if (regDate.compareTo(from) < 0) {
						fromDate = from;
					} else {
						fromDate = regDate;
					}
				} else {
					fromDate = from;
				}

				System.out.println("Agent org ID ===== " + agtOrgId);
				opLedbalance = 0.0;
				closLedbalance = 0.0;
				System.out.println("opLedbalance  before " + opLedbalance
						+ " before closLedbalance " + closLedbalance);
				opLedbalance = getLedgerBal(agtOrgId, fromDate, "opening");
				closLedbalance = getLedgerBal(agtOrgId, toDate, "closing");
				System.out.println("opLedbalance " + opLedbalance
						+ " closLedbalance " + closLedbalance);
				reconReportTypeAgt = agtreconhelper.getReconRepType(agtOrgId);
				weeklyOpeningBal = getPreviousBalance(agtOrgId, fromDate,
						reconReportTypeAgt);
				System.out.println("weeklyOpeningBal  ====== "
						+ weeklyOpeningBal);
				consolidatedBean = getConsolidatedDetails(con, agtOrgId,
						fromDate, toDate, weeklyOpeningBal, reconReportTypeAgt);
				consolidatedBean.setWeeklyOpeningBal(weeklyOpeningBal);
				consolidatedBean.setOpeningLedBal(opLedbalance);
				consolidatedBean.setClosingLedBal(closLedbalance);
				consolidatedList.add(consolidatedBean);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(toDate.getTime());
			if (!dateformat1.format(calendar.getTime()).equals(
					dateformat1.format(from))) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				System.out.println(" inside if calendar Agt "
						+ dateformat1.format(from) + "  cTo "
						+ dateformat1.format(calendar.getTime()));
			}
			System.out.println(" inside if calendar Agt "
					+ dateformat1.format(from) + "  cTo "
					+ dateformat1.format(calendar.getTime()));

			consolidatedList.add(dateformat1.format(from)); // 1 in list
			consolidatedList.add(dateformat1.format(calendar.getTime()));// 2
		} catch (LMSException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// TODO Auto-generated method stub
		return consolidatedList;
	}

	public List CreateReport(int agtOrgId, Timestamp from, Timestamp toDate,
			SimpleDateFormat dateformat1, String reconReportType) {
		System.out.println("!!! CreateReport !!!!!!11111111111 from " + from
				+ "  toDate " + toDate);

		Connection con = null;
		con = DBConnect.getConnection();
		List reconList = new ArrayList();
		ReportBean reportBean = new ReportBean();
		List<SalePurchaseBean> saleList;
		List<PWTPaymentsBean> pwtList;
		List<CashChqPmntBean> paymentList;
		Map<String, StockMrpDiscountBean> stockMrpDiscMap = null;
		Map<String, StockMrpDiscountBean> stockMrpDiscRetMap = null;
		Timestamp regDate = null;
		try {

			saleList = getSalePurchBeanBookWiseList(con, agtOrgId, from,
					toDate, reconReportType);
			pwtList = getPwtBeanBookWiseList(con, agtOrgId, from, toDate,
					reconReportType);
			paymentList = getPaymentBeanBookWiseList(con, agtOrgId, from,
					toDate, reconReportType);
			resultSet = statement
					.executeQuery("select transaction_date from st_lms_bo_transaction_master where party_id='"
							+ agtOrgId + "' order by transaction_date  limit 1");
			while (resultSet.next()) {
				regDate = resultSet.getTimestamp("transaction_date");
				if (regDate != null) {

					if ("Ticket Wise Report".equalsIgnoreCase(reconReportType)) {
						stockMrpDiscRetMap = getStockMrpDiscRetMap(con,
								agtOrgId, regDate, toDate);
					}
					stockMrpDiscMap = getStockMrpDiscountBeanMap(con, agtOrgId,
							regDate, toDate);

				}

			}
			reportBean.setSaleList(saleList);
			reportBean.setPwtList(pwtList);
			reportBean.setPaymentList(paymentList);
			reportBean.setStockMrpDiscMap(stockMrpDiscMap);
			reportBean.setReconReportType(reconReportType);
			reportBean.setStockMrpDiscRetMap(stockMrpDiscRetMap);

			resultSet = statement
					.executeQuery("select balance,account_type from st_lms_bo_ledger where (account_type ='"
							+ agtOrgId
							+ "' or account_type = '"
							+ agtOrgId
							+ "#') and transaction_date <'"
							+ from
							+ "' order by transaction_date desc, task_id desc limit 2");
			System.out
					.println("opning balance Agt select balance,account_type from st_lms_bo_ledger where (account_type ='"
							+ agtOrgId
							+ "' or account_type = '"
							+ agtOrgId
							+ "#') and transaction_date <'"
							+ from
							+ "' order by transaction_date desc, task_id desc limit 2");
			boolean count = false;
			int countt = 0;
			while (resultSet.next()) {

				if (resultSet.getString("account_type").equalsIgnoreCase(
						agtOrgId + "#")) {
					openingBal = resultSet.getDouble("balance");
					count = true;
				} else {
					countt++;
					openingBal2 = resultSet.getDouble("balance");
				}
				if (countt == 1) {
					break;
				}
			}
			if (count == true) {
				finalopeningBal = openingBal;
			} else {
				finalopeningBal = openingBal2;
			}

			resultSet = statement
					.executeQuery("select balance,account_type from st_lms_bo_ledger where (account_type = '"
							+ agtOrgId
							+ "' or account_type = '"
							+ agtOrgId
							+ "#') and transaction_date <='"
							+ toDate
							+ "' order by transaction_date desc,task_id desc limit 2");
			System.out
					.println(" closing balance  Agt  select balance,account_type from st_lms_bo_ledger where (account_type = '"
							+ agtOrgId
							+ "' or account_type = '"
							+ agtOrgId
							+ "#') and transaction_date <='"
							+ toDate
							+ "' order by transaction_date, task_id desc desc limit 2");
			boolean count2 = false;
			int countt2 = 0;
			while (resultSet.next()) {
				if (resultSet.getString("account_type").equalsIgnoreCase(
						agtOrgId + "#")) {
					closingBal = resultSet.getDouble("balance");
					count2 = true;
				} else {
					countt2++;
					closingBal2 = resultSet.getDouble("balance");
				}
				if (countt2 == 1) {
					break;
				}
			}
			if (count2 == true) {
				finalclosingBal = closingBal;
			} else {
				finalclosingBal = closingBal2;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(toDate.getTime());
			if (!dateformat1.format(calendar.getTime()).equals(
					dateformat1.format(from))) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				System.out.println(" inside if calendar Agt "
						+ dateformat1.format(from) + "  cTo "
						+ dateformat1.format(calendar.getTime()));
			}
			System.out.println(" inside if calendar Agt "
					+ dateformat1.format(from) + "  cTo "
					+ dateformat1.format(calendar.getTime()));

			reconList.add(reportBean);// 0
			reconList.add(totalAmount);// 1
			reconList.add(finalopeningBal);// 2
			reconList.add(finalclosingBal);// 3
			reconList.add(dateformat1.format(from)); // 4 in list
			reconList.add(dateformat1.format(calendar.getTime()));// 5
			reconList.add(totalSaleMrp);// 6
			reconList.add(totalSaleComm);// 7
			reconList.add(totalSaleAmount);// 8
			reconList.add(totalPurchAmount);// 9

			System.out.println("reconList size is " + reconList.size());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return reconList;
	}

	public Map<Integer, String> getAgentOrgMap(Timestamp dt) {
		Connection conn = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet result = null;
		Map<Integer, String> orgMap = new HashMap<Integer, String>();
		try {
			stmt = conn.createStatement();
			result = stmt
					.executeQuery("select name,om.organization_id from st_lms_organization_master om,st_lms_user_master um where om.organization_type='AGENT' and om.organization_id=um.organization_id and registration_date<'"
							+ dt + "' order by name");
			while (result.next()) {
				System.out.println("ID ################ "
						+ result.getInt(TableConstants.ORG_ID) + " Name "
						+ result.getString(TableConstants.NAME));
				orgMap.put(result.getInt(TableConstants.ORG_ID), result
						.getString(TableConstants.NAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("receipt search list : "+list);
		return orgMap;

	}

	private ConsolidatedBean getConsDetAftrPmt(Connection con, int agtOrgId,
			Timestamp from, Timestamp toDate, double weeklyOpeningBal,
			String reconReportTypeAgt) {
		double weeklyClosingBal = weeklyOpeningBal;
		ConsolidatedBean consolidatedBean = new ConsolidatedBean();

		System.out.println("getConsolidatedDetails ========== ");
		try {
			statement = con.createStatement();
			if ("Book Wise Report".equalsIgnoreCase(reconReportTypeAgt)) {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as purchAmount  from st_lms_agent_recon_bookwise arb where organization_id='"
								+ agtOrgId
								+ "' and type='NET_SALE' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");

			} else {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as purchAmount  from st_lms_agent_recon_ticketwise arb where organization_id='"
								+ agtOrgId
								+ "' and type='NET_SALE' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");
			}
			while (resultSet.next()) {
				consolidatedBean.setNetPayable(resultSet
						.getDouble("purchAmount"));
				weeklyClosingBal = weeklyClosingBal
						+ resultSet.getDouble("purchAmount");
			}
			System.out.println("getConsolidatedDetails ========== 2 0000");

			if ("Book Wise Report".equalsIgnoreCase(reconReportTypeAgt)) {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as totalPWT from st_lms_agent_recon_bookwise arb where organization_id='"
								+ agtOrgId
								+ "'  and type='PWT' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");

			} else {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as totalPWT from st_lms_agent_recon_ticketwise arb where organization_id='"
								+ agtOrgId
								+ "'  and type='PWT' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");
			}
			while (resultSet.next()) {
				consolidatedBean.setNetPWT(resultSet.getDouble("totalPWT"));
				weeklyClosingBal = weeklyClosingBal
						- resultSet.getDouble("totalPWT");
			}
			System.out.println("getConsolidatedDetails ========== 3 5555");

			if ("Book Wise Report".equalsIgnoreCase(reconReportTypeAgt)) {
				System.out.println("book wise ");
				resultSet = statement
						.executeQuery("select sum(amt_parent) as total,type  from st_lms_agent_recon_bookwise  where organization_id='"
								+ agtOrgId
								+ "' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' and game_id is null   group by type");

			} else {
				System.out
						.println("Ticket  wise select sum(amt_parent) as total,type  from st_lms_agent_recon_ticketwise  where organization_id='"
								+ agtOrgId
								+ "' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' and game_id is null   group by type");

				resultSet = statement
						.executeQuery("select sum(amt_parent) as total,type  from st_lms_agent_recon_ticketwise  where organization_id='"
								+ agtOrgId
								+ "' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' and game_id is null   group by type");

			}

			while (resultSet.next()) {
				String type = resultSet.getString("type");
				System.out.println(type + "26026025602925902602");
				if (type.equalsIgnoreCase("Cash")
						|| type.equalsIgnoreCase("Cheque")
						|| type.equalsIgnoreCase("Credit Note")) {
					consolidatedBean.setNetPayments(consolidatedBean
							.getNetPayments()
							+ resultSet.getDouble("total"));
					weeklyClosingBal = weeklyClosingBal
							- resultSet.getDouble("total");
				} else {
					consolidatedBean.setNetPayments(consolidatedBean
							.getNetPayments()
							- resultSet.getDouble("total"));
					weeklyClosingBal = weeklyClosingBal
							+ resultSet.getDouble("total");
				}

			}
			resultSet = statement
					.executeQuery("select name  from st_lms_organization_master  where organization_id='"
							+ agtOrgId + "' ");
			while (resultSet.next()) {
				consolidatedBean.setUserName(resultSet.getString("name"));
			}
			consolidatedBean.setWeeklyClosingBal(weeklyClosingBal);
			System.out.println("getConsolidatedDetails ========== 4 000");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return consolidatedBean;
	}

	private ConsolidatedBean getConsolidatedDetails(Connection con,
			int agtOrgId, Timestamp from, Timestamp toDate,
			double weeklyOpeningBal, String reconReportTypeAgt) {
		double weeklyClosingBal = weeklyOpeningBal;
		ConsolidatedBean consolidatedBean = new ConsolidatedBean();

		System.out.println("getConsolidatedDetails ========== ");
		try {
			statement = con.createStatement();
			if ("Book Wise Report".equalsIgnoreCase(reconReportTypeAgt)) {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as purchAmount  from st_lms_agent_recon_bookwise arb where organization_id='"
								+ agtOrgId
								+ "' and type='NET_SALE' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");

			} else {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as purchAmount  from st_lms_agent_recon_ticketwise arb where organization_id='"
								+ agtOrgId
								+ "' and type='NET_SALE' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");
			}
			while (resultSet.next()) {
				consolidatedBean.setNetPayable(resultSet
						.getDouble("purchAmount"));
				weeklyClosingBal = weeklyClosingBal
						+ resultSet.getDouble("purchAmount");
			}
			System.out.println("getConsolidatedDetails ========== 2");

			if ("Book Wise Report".equalsIgnoreCase(reconReportTypeAgt)) {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as totalPWT from st_lms_agent_recon_bookwise arb where organization_id='"
								+ agtOrgId
								+ "'  and type='PWT' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");

			} else {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as totalPWT from st_lms_agent_recon_ticketwise arb where organization_id='"
								+ agtOrgId
								+ "'  and type='PWT' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' group by organization_id");
			}
			while (resultSet.next()) {
				consolidatedBean.setNetPWT(resultSet.getDouble("totalPWT"));
				weeklyClosingBal = weeklyClosingBal
						- resultSet.getDouble("totalPWT");
			}
			System.out.println("getConsolidatedDetails ========== 3");

			if ("Book Wise Report".equalsIgnoreCase(reconReportTypeAgt)) {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as total,type  from st_lms_agent_recon_bookwise  where organization_id='"
								+ agtOrgId
								+ "' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' and game_id is null   group by type");

			} else {
				resultSet = statement
						.executeQuery("select sum(amt_parent) as total,type  from st_lms_agent_recon_ticketwise  where organization_id='"
								+ agtOrgId
								+ "' and recon_date>'"
								+ from
								+ "' and recon_date <='"
								+ toDate
								+ "' and game_id is null   group by type");
			}

			while (resultSet.next()) {
				if (resultSet.getString("type").equalsIgnoreCase("Cash")
						|| resultSet.getString("type").equalsIgnoreCase(
								"Cheque")
						|| resultSet.getString("type").equalsIgnoreCase(
								"Credit Note")) {
					consolidatedBean.setNetPayments(consolidatedBean
							.getNetPayments()
							+ resultSet.getDouble("total"));
					weeklyClosingBal = weeklyClosingBal
							- resultSet.getDouble("total");
				} else {
					consolidatedBean.setNetPayments(consolidatedBean
							.getNetPayments()
							- resultSet.getDouble("total"));
					weeklyClosingBal = weeklyClosingBal
							+ resultSet.getDouble("total");
				}

			}
			resultSet = statement
					.executeQuery("select name  from st_lms_organization_master  where organization_id='"
							+ agtOrgId + "' ");
			while (resultSet.next()) {
				consolidatedBean.setUserName(resultSet.getString("name"));
			}
			consolidatedBean.setWeeklyClosingBal(weeklyClosingBal);
			System.out.println("getConsolidatedDetails ========== 4");
			/*
			 * List< StockMrpDiscountBean>
			 * stockMrpDiscList=getStockMrpDiscountBeanList(con, agtOrgId, from,
			 * toDate); Iterator invItr = stockMrpDiscList.listIterator(); while
			 * (invItr.hasNext()) { StockMrpDiscountBean stockMrpDiscountBean =
			 * (StockMrpDiscountBean)invItr.next();
			 * consolidatedBean.setPurchPrcRemStk(consolidatedBean.getPurchPrcRemStk()+stockMrpDiscountBean.getStDiscPrice()); }
			 */
			Map<String, StockMrpDiscountBean> stockMrpDiscMap = getStockMrpDiscountBeanMap(
					con, agtOrgId, from, toDate);
			Iterator invItr = stockMrpDiscMap.entrySet().iterator();
			while (invItr.hasNext()) {
				Map.Entry<Integer, StockMrpDiscountBean> stockItrpair = (Map.Entry<Integer, StockMrpDiscountBean>) invItr
						.next();
				StockMrpDiscountBean stockMrpDiscountBean = stockItrpair
						.getValue();
				consolidatedBean.setPurchPrcRemStk(consolidatedBean
						.getPurchPrcRemStk()
						+ stockMrpDiscountBean.getStDiscPrice());
			}
			if ("Ticket Wise Report".equalsIgnoreCase(reconReportTypeAgt)) {
				Map<String, StockMrpDiscountBean> stockMrpDiscRetMap = getStockMrpDiscRetMap(
						con, agtOrgId, from, toDate);
				invItr = stockMrpDiscRetMap.entrySet().iterator();
				while (invItr.hasNext()) {
					Map.Entry<Integer, StockMrpDiscountBean> stockItrpair = (Map.Entry<Integer, StockMrpDiscountBean>) invItr
							.next();
					StockMrpDiscountBean stockMrpDiscountBean = stockItrpair
							.getValue();
					consolidatedBean.setPurchPrcRemRetStk(consolidatedBean
							.getPurchPrcRemRetStk()
							+ stockMrpDiscountBean.getStDiscPrice());
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return consolidatedBean;
	}

	public List getDetailsAftrLstPmnt(int agtOrgId, Timestamp from,
			Timestamp toDate, SimpleDateFormat dateformat1) {
		System.out.println("!!! getDetailsAftrLstPmnt !!!!!!11111111111 from "
				+ from + "  toDate " + toDate);

		String query = "select amount,transaction_date,transaction_type from(select 'Cash' as transaction_type,sbtm.transaction_id,amount,transaction_date from st_lms_bo_transaction_master sbtm,st_lms_bo_cash_transaction sbct where (transaction_type='CASH' )  and transaction_date>='"
				+ from
				+ "' and transaction_date<='"
				+ toDate
				+ "' and sbtm.transaction_id=sbct.transaction_id and party_id='"
				+ agtOrgId
				+ "' union select 'Cheque' as transaction_type,sbtm.transaction_id,cheque_amt as amount,transaction_date from st_lms_bo_transaction_master sbtm,st_lms_bo_sale_chq sbsc where ( sbtm.transaction_type='CHEQUE')  and transaction_date>='"
				+ from
				+ "' and transaction_date<='"
				+ toDate
				+ "' and sbtm.transaction_id=sbsc.transaction_id and party_id='"
				+ agtOrgId + "') final order by transaction_id desc limit 1";

		System.out.println("!!!!!!!  " + query);
		Connection con = null;
		con = DBConnect.getConnection();
		double amount = 0.0;
		Timestamp timestamp = null;
		List list1 = null;
		String transactionType = null;
		try {
			statement = con.createStatement();

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				amount = resultSet.getDouble("amount");
				timestamp = resultSet.getTimestamp("transaction_date");
				transactionType = resultSet.getString("transaction_type");
				System.out.println("amount ~~~~~~~~~~ " + amount
						+ "  timestamp  " + timestamp);
				list1 = (List) new ReconcilationReportHelper()
						.createConReportAftrPmnt(agtOrgId, timestamp, toDate,
								dateformat1);

			}
			if (list1 != null) {
				list1.add(amount); // 3
				list1.add(timestamp);// 4
				list1.add(transactionType);// 5
			} else {
				System.out.println("done$$$$$$$$$$$$$$$");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list1;
	}

	public double getLedgerBal(int agtOrgId, Timestamp time, String balType) {
		double ledgerBal = 0.0;
		double ledgerBal2 = 0.0;
		double finalLedgerBal = 0.0;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			statement = con.createStatement();
			if (balType.equalsIgnoreCase("opening")) {
				System.out
						.println("opning balance  select balance,account_type from st_lms_bo_ledger where (account_type ='"
								+ agtOrgId
								+ "' or account_type = '"
								+ agtOrgId
								+ "#') and transaction_date <'"
								+ time
								+ "' order by transaction_date desc, task_id desc limit 2");
				resultSet = statement
						.executeQuery("select balance,account_type from st_lms_bo_ledger where (account_type ='"
								+ agtOrgId
								+ "' or account_type = '"
								+ agtOrgId
								+ "#') and transaction_date <'"
								+ time
								+ "' order by transaction_date desc, task_id desc limit 2");
			} else {
				System.out
						.println("closing balance  select balance,account_type from st_lms_bo_ledger where (account_type ='"
								+ agtOrgId
								+ "' or account_type = '"
								+ agtOrgId
								+ "#') and transaction_date <'"
								+ time
								+ "' order by transaction_date desc, task_id desc limit 2");
				resultSet = statement
						.executeQuery("select balance,account_type from st_lms_bo_ledger where (account_type ='"
								+ agtOrgId
								+ "' or account_type = '"
								+ agtOrgId
								+ "#') and transaction_date <='"
								+ time
								+ "' order by transaction_date desc, task_id desc limit 2");
			}
			boolean count = false;
			int countt = 0;
			while (resultSet.next()) {

				if (resultSet.getString("account_type").equalsIgnoreCase(
						agtOrgId + "#")) {
					ledgerBal = resultSet.getDouble("balance");
					count = true;
				} else {
					countt++;
					ledgerBal2 = resultSet.getDouble("balance");
				}
				if (countt == 1) {
					break;
				}
			}
			if (count == true) {
				finalLedgerBal = ledgerBal;
			} else {
				finalLedgerBal = ledgerBal2;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return finalLedgerBal;
	}

	private List<CashChqPmntBean> getPaymentBeanBookWiseList(Connection con,
			int agtOrgId, Timestamp from, Timestamp toDate,
			String reconReportType) {
		CashChqPmntBean paymentBean;
		List<CashChqPmntBean> paymentList = new ArrayList<CashChqPmntBean>();
		String bookwiseQuery = "select type,sum(amt_parent) as total from st_lms_agent_recon_bookwise  where organization_id='"
				+ agtOrgId
				+ "' and recon_date>'"
				+ from
				+ "' and recon_date <='"
				+ toDate
				+ "' and game_id is null   group by type";
		String ticketWiseQuery = "select type,sum(amt_parent) as total from st_lms_agent_recon_ticketwise  where organization_id='"
				+ agtOrgId
				+ "' and recon_date>'"
				+ from
				+ "' and recon_date <='"
				+ toDate
				+ "' and game_id is null   group by type";
		String query;
		try {
			statement = con.createStatement();
			if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
				query = bookwiseQuery;
			} else {
				query = ticketWiseQuery;
			}
			resultSet = statement.executeQuery(query);
			System.out.println("queryyyyyyyyyyyyy  getPaymentBeanBookWiseList "
					+ query);

			while (resultSet.next()) {
				paymentBean = new CashChqPmntBean();
				paymentBean.setPaymentType(resultSet.getString("type"));
				paymentBean.setPaymentAmount(resultSet.getDouble("total"));
				paymentList.add(paymentBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paymentList;

	}

	public Map<String, CashChqPmntBean> getPaymentBeanList(Connection con,
			int agtOrgId, Timestamp from, Timestamp toDate) {
		String creditQuery = "select 'Cash' as transactionType,amount,sbct.transaction_id from st_lms_bo_cash_transaction sbct,st_lms_bo_transaction_master sbtm where transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbct.transaction_id=sbtm.transaction_id and party_id='"
				+ agtOrgId
				+ "' union select 'Cheque',cheque_amt as amount,sbsc.transaction_id from st_lms_bo_sale_chq sbsc,st_lms_bo_transaction_master sbtm  where sbtm.transaction_type='CHEQUE' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbsc.transaction_id=sbtm.transaction_id and  party_id='"
				+ agtOrgId
				+ "' union select 'Credit Note' as transactionType,amount,sbcn.transaction_id from st_lms_bo_credit_note sbcn,st_lms_bo_transaction_master sbtm where transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbcn.transaction_id=sbtm.transaction_id and party_id='"
				+ agtOrgId + "'";
		String debitQuery = "select 'Cheque Bounce' as transactionType,cheque_amt as amount,sbsc.transaction_id from st_lms_bo_sale_chq sbsc,st_lms_bo_transaction_master sbtm  where sbtm.transaction_type='CHQ_BOUNCE' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbsc.transaction_id=sbtm.transaction_id and  party_id='"
				+ agtOrgId
				+ "' union select 'Cheque Bounce Charges',amount,sbdn.transaction_id from st_lms_bo_debit_note sbdn,st_lms_bo_transaction_master sbtm  where sbtm.transaction_type='DR_NOTE' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbdn.transaction_id=sbtm.transaction_id and  party_id='"
				+ agtOrgId
				+ "' union select 'Debit Note',amount,sbdn.transaction_id from st_lms_bo_debit_note sbdn,st_lms_bo_transaction_master sbtm  where sbtm.transaction_type='DR_NOTE_CASH' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbdn.transaction_id=sbtm.transaction_id and  party_id='"
				+ agtOrgId + "'";
		String paymentType = null;
		double paymentAmount = 0.0;
		CashChqPmntBean paymentBean = null;
		Map<String, CashChqPmntBean> paymentmap = new HashMap<String, CashChqPmntBean>();
		System.out.println("Total amount in getPaymentBeanList  enter "
				+ totalAmount);
		try {
			statement = con.createStatement();
			resultSet = statement.executeQuery(creditQuery);
			while (resultSet.next()) {
				paymentType = resultSet.getString("transactionType");
				paymentAmount = resultSet.getDouble("amount");
				if (paymentmap.containsKey(paymentType)) {
					paymentBean = paymentmap.get(paymentType);
					paymentBean.setPaymentAmount(paymentBean.getPaymentAmount()
							+ paymentAmount);
					totalAmount = totalAmount - paymentAmount;
				} else {
					paymentBean = new CashChqPmntBean();
					paymentBean.setPaymentType(paymentType);
					paymentBean.setPaymentAmount(paymentAmount);
					paymentmap.put(paymentType, paymentBean);
					totalAmount = totalAmount - paymentAmount;
				}
			}
			resultSet = statement.executeQuery(debitQuery);

			while (resultSet.next()) {
				paymentType = resultSet.getString("transactionType");
				paymentAmount = resultSet.getDouble("amount");
				if (paymentmap.containsKey(paymentType)) {
					paymentBean = paymentmap.get(paymentType);
					paymentBean.setPaymentAmount(paymentBean.getPaymentAmount()
							+ paymentAmount);
					totalAmount = totalAmount + paymentAmount;
				} else {
					paymentBean = new CashChqPmntBean();
					paymentBean.setPaymentType(paymentType);
					paymentBean.setPaymentAmount(paymentAmount);
					paymentmap.put(paymentType, paymentBean);
					totalAmount = totalAmount + paymentAmount;
				}
			}
			System.out.println("creditQuery " + creditQuery);
			System.out.println(paymentmap.size() + " debitQuery " + debitQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getPaymentBeanList  exit "
				+ totalAmount);
		return paymentmap;
	}

	public double getPreviousBalance(int agtOrgId, Timestamp frDate,
			String reconReportType) throws LMSException {
		Connection con = null;
		con = DBConnect.getConnection();
		double balance = 0.0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(frDate.getTime());
		calendar.add(Calendar.MINUTE, 10);
		String ticketWiseQuery = "select recon_balance from st_lms_agent_recon_ticketwise where organization_id='"
				+ agtOrgId
				+ "' and recon_date< '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		String bookWiseQuery = "select recon_balance from st_lms_agent_recon_bookwise where organization_id='"
				+ agtOrgId
				+ "' and recon_date< '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		try {
			Statement statement = con.createStatement();
			if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
				resultSet = statement.executeQuery(bookWiseQuery);
				System.out.println("balance query111  " + bookWiseQuery);
			} else {
				resultSet = statement.executeQuery(ticketWiseQuery);
				System.out.println("balance query222  " + ticketWiseQuery);
			}

			while (resultSet.next()) {
				balance = resultSet.getDouble("recon_balance");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("balance 33  reconReportType " + reconReportType
				+ "  ----------" + balance);
		return balance;
	}

	public double getPrvBalAftrpmnt(int agtOrgId, Timestamp frDate,
			String reconReportType) throws LMSException {
		Connection con = null;
		con = DBConnect.getConnection();
		double balance = 0.0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(frDate.getTime());
		String ticketWiseQuery = "select recon_balance from st_lms_agent_recon_ticketwise where organization_id='"
				+ agtOrgId
				+ "' and recon_date <  '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		String bookWiseQuery = "select recon_balance from st_lms_agent_recon_bookwise where organization_id='"
				+ agtOrgId
				+ "' and recon_date <  '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		try {
			Statement statement = con.createStatement();
			if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
				resultSet = statement.executeQuery(bookWiseQuery);
				System.out.println("balance query111  " + bookWiseQuery);
			} else {
				resultSet = statement.executeQuery(ticketWiseQuery);
				System.out.println("balance query222  " + ticketWiseQuery);
			}

			while (resultSet.next()) {
				balance = resultSet.getDouble("recon_balance");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("balance 33  reconReportType " + reconReportType
				+ "  ----------" + balance);
		return balance;
	}

	private List<PWTPaymentsBean> getPwtBeanBookWiseList(Connection con,
			int agtOrgId, Timestamp from, Timestamp toDate,
			String reconReportType) {
		PWTPaymentsBean pwtPaymentsBean;
		List<PWTPaymentsBean> pwtList = new ArrayList<PWTPaymentsBean>();
		String bookwiseQuery = "select sgm.game_nbr,sgm.game_id,game_name,type,sum(amt_parent) as totalPWT,sum(amt_child) as netPWT ,sum(amt_parent-amt_child) as comm from st_lms_agent_recon_bookwise arb,st_se_game_master sgm where organization_id='"
				+ agtOrgId
				+ "' and sgm.game_id=arb.game_id and type='PWT' and recon_date>'"
				+ from + "' and recon_date <='" + toDate + "' group by game_id";
		String ticketWiseQuery = "select sgm.game_nbr,sgm.game_id,game_name,type,sum(amt_parent) as totalPWT,sum(amt_child) as netPWT ,sum(amt_parent-amt_child) as comm from st_lms_agent_recon_ticketwise arb,st_se_game_master sgm where organization_id='"
				+ agtOrgId
				+ "' and sgm.game_id=arb.game_id and type='PWT' and recon_date>'"
				+ from + "' and recon_date <='" + toDate + "' group by game_id";
		String query;
		try {
			statement = con.createStatement();
			if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
				query = bookwiseQuery;
			} else {
				query = ticketWiseQuery;
			}
			resultSet = statement.executeQuery(query);
			System.out.println("queryyyyyyyyyyyyy  getPwtBeanBookWiseList "
					+ query);
			while (resultSet.next()) {
				pwtPaymentsBean = new PWTPaymentsBean();
				pwtPaymentsBean.setGameNo(resultSet.getInt("game_nbr"));
				pwtPaymentsBean.setGameName(resultSet.getString("game_name"));
				pwtPaymentsBean.setTotalAmt(resultSet.getDouble("totalPWT"));
				pwtPaymentsBean.setNetPWT(resultSet.getDouble("netPWT"));
				pwtPaymentsBean.setNetCommAmt(resultSet.getDouble("comm"));

				pwtList.add(pwtPaymentsBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pwtList;
	}

	public List<PWTPaymentsBean> getPwtBeanList(Connection con, int agtOrgId,
			Timestamp from, Timestamp toDate) {
		String pwtQuery = "select sgm.game_id,sum(pwt_amt) as pwt_amt,sum(comm_amt) as comm_amt,sum(net_amt) as net_amt ,game_name from  st_se_bo_pwt sbp, st_lms_bo_transaction_master sbtm,st_se_game_master sgm where sbp.game_id=sgm.game_id and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbp.transaction_id=sbtm.transaction_id and party_id='"
				+ agtOrgId + "' group by game_id";
		int gameNo = 0;
		String gameName = null;
		double netPWT = 0.0;
		double netCommAmt = 0.0;
		double totalAmt = 0.0;
		PWTPaymentsBean bean = null;
		PWTPaymentsBean pwtBean = null;
		List<PWTPaymentsBean> pwtList = new ArrayList<PWTPaymentsBean>();
		System.out.println("Total amount in getPwtBeanList  enter "
				+ totalAmount);
		try {
			statement = con.createStatement();
			resultSet = statement.executeQuery(pwtQuery);
			while (resultSet.next()) {
				gameNo = resultSet.getInt("game_id");
				gameName = resultSet.getString("game_name");
				netPWT = resultSet.getDouble("pwt_amt");
				netCommAmt = resultSet.getDouble("comm_amt");
				totalAmt = resultSet.getDouble("net_amt");
				pwtBean = new PWTPaymentsBean();
				pwtBean.setGameNo(gameNo);
				pwtBean.setGameName(gameName);
				pwtBean.setNetPWT(netPWT);
				pwtBean.setNetCommAmt(netCommAmt);
				pwtBean.setTotalAmt(totalAmt);
				pwtList.add(pwtBean);
				totalAmount = totalAmount - totalAmt;
			}
			System.out.println(pwtList.size() + " pwt query " + pwtQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getPwtBeanList  exit "
				+ totalAmount);
		return pwtList;
	}

	public double getReconbalance(int agtOrgId, Connection connection,
			String reconReportType) {

		double balance = 0.0;
		String bookwiseQuery = "select recon_balance from st_lms_agent_recon_bookwise where organization_id='"
				+ agtOrgId + "'  order by recon_date desc,task_id desc limit 1";
		String ticketWiseQuery = "select recon_balance from st_lms_agent_recon_ticketwise where organization_id='"
				+ agtOrgId + "'  order by recon_date desc,task_id desc limit 1";
		String query = null;
		try {
			Statement statement = connection.createStatement();
			if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
				query = bookwiseQuery;
			} else {
				query = ticketWiseQuery;
			}
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				balance = resultSet.getDouble("recon_balance");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return balance;
	}

	public String getReconRepType(int agtOrgId) {
		Connection con = null;
		con = DBConnect.getConnection();
		String type = null;
		try {
			Statement statement = con.createStatement();
			resultSet = statement
					.executeQuery("select recon_report_type from st_lms_organization_master where organization_id='"
							+ agtOrgId + "' ");

			while (resultSet.next()) {
				type = resultSet.getString("recon_report_type");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return type;
	}

	public Map<String, SalePurchaseBean> getSalePurchaseDetails(Connection con,
			int agtOrgId, Timestamp from, Timestamp toDate) {
		String SaleQuery2 = "select  game_id,game_name,sum(mrp_amt) as mrp,'Sale' transaction_type, sum(net_amt) as saleprice,sum(purchprice)as purchprice,sum(net_amt-purchprice) as comm from (select  sgm.game_name,sgid.game_id,sgid.transaction_id,transaction_type,(net_amt/sart.nbr_of_books) as net_amt,(mrp_amt/sart.nbr_of_books) as mrp_amt,((100-transaction_purchase_comm_rate)*.01*mrp_amt/sart.nbr_of_books) as purchprice from st_se_game_inv_detail sgid,st_se_agent_retailer_transaction sart ,st_se_game_master sgm where sgm.game_id=sgid.game_id and current_owner='RETAILER' and transaction_at='AGENT'  and  sgid.transaction_date>'"
				+ from
				+ "'  and sgid.transaction_date <='"
				+ toDate
				+ "' and sart.transaction_type='SALE' and sart.transaction_id=sgid.transaction_id and sart.agent_org_id='"
				+ agtOrgId
				+ "'  order by transaction_id )sale  group by game_id";
		String SaleReturnQuery2 = "select   game_id,game_name,sum(mrp_amt) as mrp,'Sale Return' transaction_type, sum(net_amt) as saleprice,sum(purchprice)as purchprice,sum(net_amt-purchprice) as comm from (select  sgm.game_name,sgid.game_id,sgid.transaction_id,transaction_type,(net_amt/sart.nbr_of_books) as net_amt,(mrp_amt/sart.nbr_of_books) as mrp_amt,((100-transaction_purchase_comm_rate)*.01*mrp_amt/sart.nbr_of_books) as purchprice from st_se_game_inv_detail sgid,st_se_agent_retailer_transaction sart ,st_se_game_master sgm where sgm.game_id=sgid.game_id and current_owner='AGENT' and transaction_at='AGENT'  and  sgid.transaction_date>'"
				+ from
				+ "'  and sgid.transaction_date <='"
				+ toDate
				+ "' and sart.transaction_type='SALE_RET' and sart.transaction_id=sgid.transaction_id and sart.agent_org_id='"
				+ agtOrgId
				+ "'  order by transaction_id )saleret  group by game_id";

		String transactionType = null;

		System.out.println("Total amount in getSalePurchaseBeanList  enter "
				+ totalAmount);

		String gameName = null;
		double salePrice = 0.0;
		double mrp = 0.0;
		double purchPrice = 0.0;
		double comm = 0.0;
		int gameId = 0;
		SalePurchaseBean salePurchaseBean = null;
		Map<String, SalePurchaseBean> salemap = new HashMap<String, SalePurchaseBean>();

		try {

			statement = con.createStatement();
			System.out.println("SaleQuery2 " + SaleQuery2);
			resultSet = statement.executeQuery(SaleQuery2);

			while (resultSet.next()) {

				gameName = resultSet.getString("game_name");
				transactionType = resultSet.getString("transaction_type");
				mrp = resultSet.getDouble("mrp");
				salePrice = resultSet.getDouble("saleprice");
				purchPrice = resultSet.getDouble("purchprice");
				comm = resultSet.getDouble("comm");
				gameId = resultSet.getInt("game_id");
				if (salemap.containsKey(gameName)) {
					salePurchaseBean = salemap.get(gameName);
					System.out.println(" Sale Old Before ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);

					totalSaleMrp = totalSaleMrp + mrp;
					totalSaleAmount = totalSaleAmount + salePrice;
					totalSaleComm = totalSaleComm + comm;
					totalPurchAmount = totalPurchAmount + purchPrice;
					salePurchaseBean.setMrp(salePurchaseBean.getMrp() + mrp);
					salePurchaseBean.setComm(salePurchaseBean.getComm() + comm);
					salePurchaseBean.setSalePrice(salePurchaseBean
							.getSalePrice()
							+ salePrice);
					salePurchaseBean.setPurchprice(salePurchaseBean
							.getPurchprice()
							+ purchPrice);
					System.out.println("Sale Old gameName " + gameName
							+ " mrp " + mrp + " commAmount " + comm
							+ " sale price " + salePrice);
					System.out.println(" Sale Old After ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);

				} else {
					salePurchaseBean = new SalePurchaseBean();
					salePurchaseBean.setGameName(gameName);
					salePurchaseBean.setGameId(gameId);
					salePurchaseBean.setMrp(mrp);
					salePurchaseBean.setComm(comm);
					salePurchaseBean.setSalePrice(salePrice);
					salePurchaseBean.setPurchprice(purchPrice);
					salemap.put(gameName, salePurchaseBean);
					System.out.println("New  Before  ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);
					totalSaleMrp = totalSaleMrp + mrp;
					totalSaleAmount = totalSaleAmount + salePrice;
					totalSaleComm = totalSaleComm + comm;
					totalPurchAmount = totalPurchAmount + purchPrice;
					System.out.println("Sale New gameName " + gameName
							+ " mrp " + mrp + " commAmount " + comm
							+ " sale price " + salePrice);
					System.out.println(" Sale New After ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);

				}

			}
			statement = con.createStatement();
			System.out.println("SaleReturnQuery2 " + SaleReturnQuery2);
			resultSet = statement.executeQuery(SaleReturnQuery2);
			while (resultSet.next()) {

				gameName = resultSet.getString("game_name");
				transactionType = resultSet.getString("transaction_type");
				mrp = resultSet.getDouble("mrp");
				salePrice = resultSet.getDouble("saleprice");
				purchPrice = resultSet.getDouble("purchprice");
				comm = resultSet.getDouble("comm");
				gameId = resultSet.getInt("game_id");

				if (salemap.containsKey(gameName)) {
					salePurchaseBean = salemap.get(gameName);
					System.out
							.println("Sale Return  Old Before ======  totalSaleMrp "
									+ totalSaleMrp
									+ " totalSaleAmount "
									+ totalSaleAmount
									+ " totalSaleComm "
									+ totalSaleComm);

					totalSaleMrp = totalSaleMrp - mrp;
					totalSaleAmount = totalSaleAmount - salePrice;
					totalSaleComm = totalSaleComm - comm;
					totalPurchAmount = totalPurchAmount - purchPrice;
					salePurchaseBean.setMrp(salePurchaseBean.getMrp() - mrp);
					salePurchaseBean.setComm(salePurchaseBean.getComm() - comm);
					salePurchaseBean.setSalePrice(salePurchaseBean
							.getSalePrice()
							- salePrice);
					salePurchaseBean.setPurchprice(salePurchaseBean
							.getPurchprice()
							- purchPrice);
					System.out.println("Sale Return Old gameName " + gameName
							+ " mrp " + mrp + " commAmount " + comm
							+ " salePrice " + salePrice);
					System.out
							.println("Sale Return  Old After ======  totalSaleMrp "
									+ totalSaleMrp
									+ " totalSaleAmount "
									+ totalSaleAmount
									+ " totalSaleComm "
									+ totalSaleComm);

				} else {
					salePurchaseBean = new SalePurchaseBean();
					salePurchaseBean.setGameName(gameName);
					salePurchaseBean.setGameId(gameId);
					salePurchaseBean.setMrp(-mrp);
					salePurchaseBean.setComm(-comm);
					salePurchaseBean.setSalePrice(-salePrice);
					salePurchaseBean.setPurchprice(-purchPrice);
					salemap.put(gameName, salePurchaseBean);
					System.out
							.println("Sale Return  New  Before ======  totalSaleMrp "
									+ totalSaleMrp
									+ " totalSaleAmount "
									+ totalSaleAmount
									+ " totalSaleComm "
									+ totalSaleComm);

					totalSaleMrp = totalSaleMrp - mrp;
					totalSaleAmount = totalSaleAmount - salePrice;
					totalSaleComm = totalSaleComm - comm;
					totalPurchAmount = totalPurchAmount - purchPrice;
					System.out.println("Sale Return New gameName " + gameName
							+ " mrp " + mrp + " commAmount " + comm
							+ " salePrice " + salePrice);
					System.out
							.println("Sale Return  New After ======  totalSaleMrp "
									+ totalSaleMrp
									+ " totalSaleAmount "
									+ totalSaleAmount
									+ " totalSaleComm "
									+ totalSaleComm);

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getSalePurchaseBeanList  exit "
				+ totalAmount);
		System.out.println("salePurchList end " + salemap.size());
		return salemap;
	}

	public Map<String, SalePurchaseBean> getSalePurchaseTicketDetails(
			Connection con, int agtOrgId, Timestamp from, Timestamp toDate) {
		String SaleQuery2 = "select srrt.game_id,game_name ,recon_date,type,agent_org_id,sum(amt_parent) as purchPrice,sum(amt_child) as salePrice ,sum(mrp_amt) as mrp,sum(amt_child-amt_parent) as profit  from  st_lms_ret_recon_ticketwise srrt,st_se_game_master sgm where agent_org_id='"
				+ agtOrgId
				+ "' and type='NET_SALE' and recon_date>'"
				+ from
				+ "' and recon_date<='"
				+ toDate
				+ "' and sgm.game_id=srrt.game_id  group by agent_org_id,game_id";

		String transactionType = null;

		String gameName = null;
		double salePrice = 0.0;
		double mrp = 0.0;
		double purchPrice = 0.0;
		double comm = 0.0;
		int gameId = 0;
		SalePurchaseBean salePurchaseBean = null;
		Map<String, SalePurchaseBean> salemap = new HashMap<String, SalePurchaseBean>();

		try {

			statement = con.createStatement();
			System.out.println("SaleQuery2 " + SaleQuery2);
			resultSet = statement.executeQuery(SaleQuery2);

			while (resultSet.next()) {

				gameName = resultSet.getString("game_name");
				transactionType = resultSet.getString("type");
				mrp = resultSet.getDouble("mrp");
				salePrice = resultSet.getDouble("salePrice");
				purchPrice = resultSet.getDouble("purchPrice");
				comm = resultSet.getDouble("profit");
				gameId = resultSet.getInt("game_id");
				if (salemap.containsKey(gameName)) {
					salePurchaseBean = salemap.get(gameName);
					System.out.println(" Sale Old Before ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);

					totalSaleMrp = totalSaleMrp + mrp;
					totalSaleAmount = totalSaleAmount + salePrice;
					totalSaleComm = totalSaleComm + comm;
					totalPurchAmount = totalPurchAmount + purchPrice;
					salePurchaseBean.setMrp(salePurchaseBean.getMrp() + mrp);
					salePurchaseBean.setComm(salePurchaseBean.getComm() + comm);
					salePurchaseBean.setSalePrice(salePurchaseBean
							.getSalePrice()
							+ salePrice);
					salePurchaseBean.setPurchprice(salePurchaseBean
							.getPurchprice()
							+ purchPrice);
					System.out.println("Sale Old gameName " + gameName
							+ " mrp " + mrp + " commAmount " + comm
							+ " sale price " + salePrice);
					System.out.println(" Sale Old After ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);

				} else {
					salePurchaseBean = new SalePurchaseBean();
					salePurchaseBean.setGameName(gameName);
					salePurchaseBean.setGameId(gameId);
					salePurchaseBean.setMrp(mrp);
					salePurchaseBean.setComm(comm);
					salePurchaseBean.setSalePrice(salePrice);
					salePurchaseBean.setPurchprice(purchPrice);
					salemap.put(gameName, salePurchaseBean);
					System.out.println("New  Before  ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);
					totalSaleMrp = totalSaleMrp + mrp;
					totalSaleAmount = totalSaleAmount + salePrice;
					totalSaleComm = totalSaleComm + comm;
					totalPurchAmount = totalPurchAmount + purchPrice;
					System.out.println("Sale New gameName " + gameName
							+ " mrp " + mrp + " commAmount " + comm
							+ " sale price " + salePrice);
					System.out.println(" Sale New After ======  totalSaleMrp "
							+ totalSaleMrp + " totalSaleAmount "
							+ totalSaleAmount + " totalSaleComm "
							+ totalSaleComm);

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getSalePurchaseBeanList  exit "
				+ totalAmount);
		System.out.println("salePurchList end " + salemap.size());
		return salemap;
	}

	private List<SalePurchaseBean> getSalePurchBeanBookWiseList(Connection con,
			int agtOrgId, Timestamp from, Timestamp toDate,
			String reconReportType) {
		String bookwiseQuery = "select sgm.game_nbr,sgm.game_id,game_name,sum(mrp_amt) as mrp,type,sum(amt_parent) as purchAmount,sum(amt_child) as saleAmount ,sum(amt_child-amt_parent) as comm from st_lms_agent_recon_bookwise arb,st_se_game_master sgm where organization_id='"
				+ agtOrgId
				+ "' and sgm.game_id=arb.game_id and type='NET_SALE' and recon_date>'"
				+ from + "' and recon_date <='" + toDate + "' group by game_id";
		String ticketWiseQuery = "select sgm.game_nbr,sgm.game_id,game_name,sum(mrp_amt) as mrp,type,sum(amt_parent) as purchAmount,sum(amt_child) as saleAmount ,sum(amt_child-amt_parent) as comm from st_lms_agent_recon_ticketwise arb,st_se_game_master sgm where organization_id='"
				+ agtOrgId
				+ "' and sgm.game_id=arb.game_id and type='NET_SALE' and recon_date>'"
				+ from + "' and recon_date <='" + toDate + "' group by game_id";
		String query;
		SalePurchaseBean salePurchaseBean;
		List<SalePurchaseBean> saleList = new ArrayList<SalePurchaseBean>();
		try {
			statement = con.createStatement();
			if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
				query = bookwiseQuery;
			} else {
				query = ticketWiseQuery;
			}
			resultSet = statement.executeQuery(query);
			System.out
					.println("queryyyyyyyyyyyyy  getSalePurchBeanBookWiseList "
							+ query);
			while (resultSet.next()) {
				salePurchaseBean = new SalePurchaseBean();
				salePurchaseBean.setGameId(resultSet.getInt("game_nbr"));
				salePurchaseBean.setGameName(resultSet.getString("game_name"));
				salePurchaseBean.setMrp(resultSet.getDouble("mrp"));
				salePurchaseBean
						.setSalePrice(resultSet.getDouble("saleAmount"));
				salePurchaseBean.setPurchprice(resultSet
						.getDouble("purchAmount"));
				salePurchaseBean.setComm(resultSet.getDouble("comm"));
				saleList.add(salePurchaseBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return saleList;
	}

	/*
	 * public List<StockMrpDiscountBean> getStockMrpDiscountBeanList(Connection
	 * con,int agtOrgId, Timestamp from, Timestamp toDate){
	 * 
	 * String idOfGames="select game_id,game_nbr from st_se_game_master where
	 * game_status!='NEW'"; String noOfBooksQuery=null; StringBuilder
	 * stockMrpDiscQuery= null; StockMrpDiscountBean stockMrpDiscBean=null; List<StockMrpDiscountBean>
	 * stockMrpDiscList= new ArrayList<StockMrpDiscountBean>(); String
	 * stGameName=null; double stPercent=0; int stGameNo=0; double stMrp=0.0;
	 * double stDiscPrice=0.0; int stCountBooks=0; System.out.println("Total
	 * amount in getStockMrpDiscountBeanList enter "+totalAmount); try {
	 * statement=con.createStatement();
	 * resultSet=statement.executeQuery(idOfGames); while(resultSet.next()){
	 * String table="st_se_pwt_tickets_inv_"+resultSet.getString("game_nbr");
	 * noOfBooksQuery="select book_nbr from st_se_game_inv_status where
	 * current_owner_id=? and game_id = ? and book_nbr not in (select distinct
	 * book_nbr from "+table+" ) and book_nbr not in(select distinct book_nbr
	 * from "+table+" where game_id=?)"; stockMrpDiscQuery= new
	 * StringBuilder("select transaction_date,game_name,count( book_nbr) as
	 * noOfBooks ,'Purchase' as transaction_type,game_nbr," +
	 * "sum((mrp_amt/nbr_of_books)) as mrp,(net_amt/mrp_amt)*100 as
	 * percent,sum((net_amt/nbr_of_books))as salePrice ,transaction_id from" +
	 * "(select book_nbr,transaction_date,game_name,
	 * transaction_type,game_nbr,mrp_amt,nbr_of_books,net_amt,transaction_id
	 * from " + "(select book_nbr,transaction_date,game_name,
	 * transaction_type,game_nbr,mrp_amt,sbat.nbr_of_books,net_amt,sgid.transaction_id
	 * from " + "st_se_bo_agent_transaction sbat, st_se_game_inv_detail
	 * sgid,st_se_game_master sgm where sgm.game_id=sgid.game_id and
	 * transaction_at='BO' and " + "current_owner='AGENT' and
	 * sgid.transaction_id=sbat.transaction_id and (book_nbr='0'");
	 * 
	 * prepstatement=con.prepareStatement(noOfBooksQuery);
	 * prepstatement.setInt(1, agtOrgId); prepstatement.setString(2,
	 * resultSet.getString("game_id")); prepstatement.setString(3,
	 * resultSet.getString("game_id")); resultSet2=
	 * prepstatement.executeQuery(); while(resultSet2.next()){
	 * stockMrpDiscQuery.append(" or
	 * book_nbr='"+resultSet2.getString("book_nbr")+"'"); }
	 * stockMrpDiscQuery.append(") order by transaction_date desc)innermost
	 * group by book_nbr) outermost group by game_nbr,percent");
	 * 
	 * System.out.println("stockMrpDiscQuery "+stockMrpDiscQuery);
	 * statement3=con.createStatement();
	 * resultSet3=statement3.executeQuery(stockMrpDiscQuery.toString());
	 * while(resultSet3.next()){ stGameName=resultSet3.getString("game_name");
	 * stPercent=resultSet3.getDouble("percent");
	 * stGameNo=resultSet3.getInt("game_nbr");
	 * stMrp=resultSet3.getDouble("mrp");
	 * stCountBooks=resultSet3.getInt("noOfBooks");
	 * stDiscPrice=resultSet3.getInt("salePrice"); stockMrpDiscBean=new
	 * StockMrpDiscountBean(); stockMrpDiscBean.setStPercent(stPercent);
	 * stockMrpDiscBean.setStGameName(stGameName);
	 * stockMrpDiscBean.setStGameNo(stGameNo); stockMrpDiscBean.setStMrp(stMrp);
	 * stockMrpDiscBean.setStCountBooks(stCountBooks);
	 * stockMrpDiscBean.setStDiscPrice(stDiscPrice);
	 * stockMrpDiscList.add(stockMrpDiscBean); } } } catch (SQLException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * System.out.println("Total amount in getStockMrpDiscountBeanList exit
	 * "+totalAmount); return stockMrpDiscList; }
	 */
	public Map<String, StockMrpDiscountBean> getStockMrpDiscountBeanMap(
			Connection con, int agtOrgId, Timestamp from, Timestamp toDate) {

		String trIdQueryBoAgt = "select transaction_id from st_lms_bo_transaction_master where   transaction_date<='"
				+ toDate
				+ "' and party_id='"
				+ agtOrgId
				+ "' and (transaction_type='SALE' or transaction_type='SALE_RET')";
		String trIdQueryAgtSale = "select transaction_id from st_lms_agent_transaction_master where  transaction_date<='"
				+ toDate
				+ "'  and (transaction_type='SALE') and user_org_id='"
				+ agtOrgId + "'";
		String trIdQueryAgtSaleRet = "select transaction_id from st_lms_agent_transaction_master where  transaction_date<='"
				+ toDate
				+ "'  and (transaction_type='SALE_RET') and user_org_id='"
				+ agtOrgId + "'";

		System.out.println("trIdQueryAgtSale " + trIdQueryAgtSale);
		System.out.println("trIdQueryAgtSaleRet " + trIdQueryAgtSale);
		// String noOfBooksQuery=null;
		StringBuilder stockMrpDiscQuery = null;
		StockMrpDiscountBean stockMrpDiscBean = null;
		List<StockMrpDiscountBean> stockMrpDiscList = new ArrayList<StockMrpDiscountBean>();
		Map<String, StockMrpDiscountBean> stockmap = new HashMap<String, StockMrpDiscountBean>();
		String stGameName = null;
		double stPercent = 0;
		int stGameNo = 0;
		double stMrp = 0.0;
		double stDiscPrice = 0.0;
		int stCountBooks = 0;
		String transactionType = null;
		System.out
				.println("Total amount in getStockMrpDiscountBeanList  enter "
						+ totalAmount);
		try {
			statement = con.createStatement();
			resultSet = statement.executeQuery(trIdQueryBoAgt);
			stockMrpDiscQuery = new StringBuilder(
					"select game_nbr,game_name,sbat.game_id,agent_org_id,sum(sbat.nbr_of_books) as noOfBooks,sum(mrp_amt) as mrp,transaction_type,sum(net_amt)salePrice,(net_amt/mrp_amt)*100 as percent from st_se_bo_agent_transaction sbat,st_se_game_master sgm where sbat.game_id=sgm.game_id and (transaction_id=0 ");
			System.out.println("stockMrpDiscQuery " + stockMrpDiscQuery);
			while (resultSet.next()) {
				stockMrpDiscQuery.append(" or transaction_id='"
						+ resultSet.getString("transaction_id") + "'");

			}
			stockMrpDiscQuery
					.append("  )group by transaction_type,game_id,percent");

			System.out.println("stockMrpDiscQuery  " + stockMrpDiscQuery);
			statement3 = con.createStatement();
			resultSet3 = statement3.executeQuery(stockMrpDiscQuery.toString());
			while (resultSet3.next()) {
				stGameName = resultSet3.getString("game_name");
				stPercent = resultSet3.getDouble("percent");
				stGameNo = resultSet3.getInt("game_nbr");
				stMrp = resultSet3.getDouble("mrp");
				stCountBooks = resultSet3.getInt("noOfBooks");
				stDiscPrice = resultSet3.getInt("salePrice");
				transactionType = resultSet3.getString("transaction_type");
				if (stockmap.containsKey("" + stGameNo + stPercent)) {
					stockMrpDiscBean = (StockMrpDiscountBean) stockmap.get(""
							+ stGameNo + stPercent);

					if (transactionType.equalsIgnoreCase("SALE")) {
						stockMrpDiscBean.setStMrp(stockMrpDiscBean.getStMrp()
								+ stMrp);
						stockMrpDiscBean.setStCountBooks(stockMrpDiscBean
								.getStCountBooks()
								+ stCountBooks);
						stockMrpDiscBean.setStDiscPrice(stockMrpDiscBean
								.getStDiscPrice()
								+ stDiscPrice);
					} else {
						stockMrpDiscBean.setStMrp(stockMrpDiscBean.getStMrp()
								- stMrp);
						stockMrpDiscBean.setStCountBooks(stockMrpDiscBean
								.getStCountBooks()
								- stCountBooks);
						stockMrpDiscBean.setStDiscPrice(stockMrpDiscBean
								.getStDiscPrice()
								- stDiscPrice);
					}

				} else {
					stockMrpDiscBean = new StockMrpDiscountBean();
					stockMrpDiscBean.setStPercent(stPercent);
					stockMrpDiscBean.setStGameName(stGameName);
					stockMrpDiscBean.setStGameNo(stGameNo);
					stockMrpDiscBean.setStMrp(stMrp);
					stockMrpDiscBean.setStCountBooks(stCountBooks);
					stockMrpDiscBean.setStDiscPrice(stDiscPrice);
					stockmap.put("" + stGameNo + stPercent, stockMrpDiscBean);
				}
			}

			// ///// Agent Retailer transactions///////

			resultSet = statement.executeQuery(trIdQueryAgtSale);
			stockMrpDiscQuery = new StringBuilder(
					"select game_nbr,game_name,sbat.game_id,count(sbat.book_nbr) as noOfBooks,sum((nbr_of_tickets_per_book*ticket_price)) as mrp,sum((nbr_of_tickets_per_book*ticket_price)*0.01*(100-transaction_purchase_comm_rate))salePrice,(100-transaction_purchase_comm_rate) as percent from st_se_game_inv_detail sbat,st_se_game_master sgm where sbat.game_id=sgm.game_id and (transaction_id=0 ");
			System.out.println("stockMrpDiscQuery " + stockMrpDiscQuery);
			while (resultSet.next()) {
				stockMrpDiscQuery.append(" or transaction_id='"
						+ resultSet.getString("transaction_id") + "'");

			}
			stockMrpDiscQuery.append("  )group by game_id,percent");

			System.out.println("stockMrpDiscQuery  " + stockMrpDiscQuery);
			statement3 = con.createStatement();
			resultSet3 = statement3.executeQuery(stockMrpDiscQuery.toString());
			while (resultSet3.next()) {
				stGameName = resultSet3.getString("game_name");
				stPercent = resultSet3.getDouble("percent");
				stGameNo = resultSet3.getInt("game_nbr");
				stMrp = resultSet3.getDouble("mrp");
				stCountBooks = resultSet3.getInt("noOfBooks");
				stDiscPrice = resultSet3.getInt("salePrice");

				if (stockmap.containsKey("" + stGameNo + stPercent)) {
					stockMrpDiscBean = (StockMrpDiscountBean) stockmap.get(""
							+ stGameNo + stPercent);

					stockMrpDiscBean.setStMrp(stockMrpDiscBean.getStMrp()
							- stMrp);
					stockMrpDiscBean.setStCountBooks(stockMrpDiscBean
							.getStCountBooks()
							- stCountBooks);
					stockMrpDiscBean.setStDiscPrice(stockMrpDiscBean
							.getStDiscPrice()
							- stDiscPrice);

				} else {
					stockMrpDiscBean = new StockMrpDiscountBean();
					stockMrpDiscBean.setStPercent(stPercent);
					stockMrpDiscBean.setStGameName(stGameName);
					stockMrpDiscBean.setStGameNo(stGameNo);
					stockMrpDiscBean.setStMrp(stMrp);
					stockMrpDiscBean.setStCountBooks(stCountBooks);
					stockMrpDiscBean.setStDiscPrice(stDiscPrice);
					stockmap.put("" + stGameNo + stPercent, stockMrpDiscBean);
				}
			}

			resultSet = statement.executeQuery(trIdQueryAgtSaleRet);
			stockMrpDiscQuery = new StringBuilder(
					"select game_nbr,game_name,sbat.game_id,count(sbat.book_nbr) as noOfBooks,sum((nbr_of_tickets_per_book*ticket_price)) as mrp,sum((nbr_of_tickets_per_book*ticket_price)*0.01*(100-transaction_purchase_comm_rate))salePrice,(100-transaction_purchase_comm_rate) as percent from st_se_game_inv_detail sbat,st_se_game_master sgm where sbat.game_id=sgm.game_id and (transaction_id=0 ");
			System.out.println("stockMrpDiscQuery " + stockMrpDiscQuery);
			while (resultSet.next()) {
				stockMrpDiscQuery.append(" or transaction_id='"
						+ resultSet.getString("transaction_id") + "'");

			}
			stockMrpDiscQuery.append("  )group by game_id,percent");

			System.out.println("stockMrpDiscQuery  " + stockMrpDiscQuery);
			statement3 = con.createStatement();
			resultSet3 = statement3.executeQuery(stockMrpDiscQuery.toString());
			while (resultSet3.next()) {
				stGameName = resultSet3.getString("game_name");
				stPercent = resultSet3.getDouble("percent");
				stGameNo = resultSet3.getInt("game_nbr");
				stMrp = resultSet3.getDouble("mrp");
				stCountBooks = resultSet3.getInt("noOfBooks");
				stDiscPrice = resultSet3.getInt("salePrice");

				if (stockmap.containsKey("" + stGameNo + stPercent)) {
					stockMrpDiscBean = (StockMrpDiscountBean) stockmap.get(""
							+ stGameNo + stPercent);

					stockMrpDiscBean.setStMrp(stockMrpDiscBean.getStMrp()
							+ stMrp);
					stockMrpDiscBean.setStCountBooks(stockMrpDiscBean
							.getStCountBooks()
							+ stCountBooks);
					stockMrpDiscBean.setStDiscPrice(stockMrpDiscBean
							.getStDiscPrice()
							+ stDiscPrice);

				} else {
					stockMrpDiscBean = new StockMrpDiscountBean();
					stockMrpDiscBean.setStPercent(stPercent);
					stockMrpDiscBean.setStGameName(stGameName);
					stockMrpDiscBean.setStGameNo(stGameNo);
					stockMrpDiscBean.setStMrp(stMrp);
					stockMrpDiscBean.setStCountBooks(stCountBooks);
					stockMrpDiscBean.setStDiscPrice(stDiscPrice);
					stockmap.put("" + stGameNo + stPercent, stockMrpDiscBean);
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getStockMrpDiscountBeanList  exit "
				+ totalAmount);
		return stockmap;
	}

	public Map<String, StockMrpDiscountBean> getStockMrpDiscRetMap(
			Connection con, int agtOrgId, Timestamp from, Timestamp toDate) {
		StockMrpDiscountBean stockMrpDiscBean = null;

		StringBuilder stockRetTicketQuery;

		System.out
				.println("Total amount in getStockMrpDiscountBeanList  enter "
						+ totalAmount);
		Map<String, StockMrpDiscountBean> tktStockMap = new HashMap<String, StockMrpDiscountBean>();
		try {
			Map retOrgIdMap = new ReconcilationReportRetHelper().getRetOrgMap(
					agtOrgId, toDate);
			Iterator retOrgitr = retOrgIdMap.entrySet().iterator();
			while (retOrgitr.hasNext()) {
				String stGameName = null;
				double stPercent = 0;
				int stGameNo = 0;
				double stMrp = 0.0;
				double stDiscPrice = 0.0;
				int stCountTickets = 0;
				Map.Entry<Integer, String> retOrgpair = (Map.Entry<Integer, String>) retOrgitr
						.next();
				int retOrgId = retOrgpair.getKey();

				/*
				 * new discount method
				 */
				Map<String, Timestamp> map = new HashMap<String, Timestamp>();

				statement = con.createStatement();
				resultSet = statement
						.executeQuery("select date,book_nbr from st_se_game_ticket_inv_history where  current_owner_id='"
								+ retOrgId + "' and date<='" + toDate + "' ");
				while (resultSet.next()) {
					System.out.println("book_nbr "
							+ resultSet.getString("book_nbr"));
					System.out.println("date  "
							+ resultSet.getTimestamp("date"));
					map.put(resultSet.getString("book_nbr"), resultSet
							.getTimestamp("date"));
				}
				statement = con.createStatement();
				StringBuilder builder = new StringBuilder(
						"select date,book_nbr from st_se_game_ticket_inv_history where  (book_nbr='0' ");
				Iterator invItr = map.entrySet().iterator();
				while (invItr.hasNext()) {
					Map.Entry<String, Timestamp> bookItrpair = (Map.Entry<String, Timestamp>) invItr
							.next();
					String bookNo = bookItrpair.getKey();
					Timestamp date = bookItrpair.getValue();
					builder.append("or book_nbr='" + bookNo + "'");
				}
				builder.append(") and date<='" + toDate
						+ "' and current_owner='AGENT'");
				System.out.println("query=======  " + builder.toString());
				resultSet = statement.executeQuery(builder.toString());
				while (resultSet.next()) {
					System.out.println("book_nbr "
							+ resultSet.getString("book_nbr"));
					System.out.println("date  "
							+ resultSet.getTimestamp("date"));
					if (map.containsKey(resultSet.getString("book_nbr"))) {
						if (map.get(resultSet.getString("book_nbr")).before(
								resultSet.getTimestamp("date"))) {
							map.remove(resultSet.getString("book_nbr"));
						}
					}

				}
				System.out.println("map ======= " + map);

				/*
				 * end new method
				 */
				stockRetTicketQuery = new StringBuilder(
						"select cur_rem_tickets,game_id,game_name,book_nbr,date,sum(sold_tickets) soldTickets,(ticket_price*cur_rem_tickets)as mrp from (select sgtih.cur_rem_tickets,sgtih.game_id,game_name,sgtih.book_nbr,date,sold_tickets,ticket_price  from st_se_game_ticket_inv_history sgtih ,st_se_game_master sgm where   sgtih.game_id=sgm.game_id and  sgtih.current_owner_id='"
								+ retOrgId
								+ "' and date<='"
								+ toDate
								+ "'  and (book_nbr='0'");
				invItr = map.entrySet().iterator();
				while (invItr.hasNext()) {
					Map.Entry<String, Timestamp> bookItrpair = (Map.Entry<String, Timestamp>) invItr
							.next();
					String bookNo = bookItrpair.getKey();
					Timestamp date = bookItrpair.getValue();
					stockRetTicketQuery.append("or book_nbr='" + bookNo + "'");
				}
				stockRetTicketQuery
						.append(") order by task_id desc ) aa group by book_nbr,game_id");
				System.out.println("query=======  "
						+ stockRetTicketQuery.toString());
				statement = con.createStatement();
				resultSet = statement.executeQuery(stockRetTicketQuery
						.toString());
				while (resultSet.next()) {
					String bookNo = resultSet.getString("book_nbr");
					stGameName = resultSet.getString("game_name");
					stMrp = resultSet.getDouble("mrp");
					stCountTickets = resultSet.getInt("cur_rem_tickets");
					statement2 = con.createStatement();
					resultSet2 = statement2
							.executeQuery("select (100-transaction_purchase_comm_rate) as purchrate ,game_id ,book_nbr from st_se_game_inv_detail where book_nbr='"
									+ bookNo
									+ "' order by transaction_date desc limit 1");
					while (resultSet2.next()) {
						stDiscPrice = resultSet2.getDouble("purchrate") * 0.01
								* stMrp;
						if (tktStockMap.containsKey(resultSet2
								.getInt("game_id")
								+ "_" + resultSet2.getDouble("purchrate"))) {

							stockMrpDiscBean = (StockMrpDiscountBean) tktStockMap
									.get(resultSet2.getInt("game_id") + "_"
											+ resultSet2.getDouble("purchrate"));
							stockMrpDiscBean.setStMrp(stockMrpDiscBean
									.getStMrp()
									+ stMrp);
							stockMrpDiscBean.setStCountTickets(stockMrpDiscBean
									.getStCountTickets()
									+ stCountTickets);
							stockMrpDiscBean.setStDiscPrice(stockMrpDiscBean
									.getStDiscPrice()
									+ stDiscPrice);

						} else {
							stockMrpDiscBean = new StockMrpDiscountBean();
							stockMrpDiscBean.setStGameName(stGameName);
							stockMrpDiscBean.setStMrp(stMrp);
							stockMrpDiscBean.setStCountTickets(stCountTickets);
							stockMrpDiscBean.setStDiscPrice(stDiscPrice);
							stockMrpDiscBean.setStPercent(resultSet2
									.getDouble("purchrate"));

							tktStockMap.put(resultSet2.getInt("game_id") + "_"
									+ resultSet2.getDouble("purchrate"),
									stockMrpDiscBean);
						}

					}
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getStockMrpDiscountBeanList  exit "
				+ totalAmount);
		return tktStockMap;
	}

	public int getUserId(String agentName) {
		Connection con = null;
		con = DBConnect.getConnection();
		int agtId = 0;
		try {
			statement = con.createStatement();

			resultSet = statement
					.executeQuery("select organization_id  from st_lms_organization_master  where name='"
							+ agentName + "' ");

			while (resultSet.next()) {
				agtId = resultSet.getInt("organization_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return agtId;
	}

	public void insertAgentTicketWiseRecon(int agtOrgId, Timestamp pToDate,
			String reconReportType) {
		Connection con = null;
		con = DBConnect.getConnection();
		String dep_Date = new LedgerHelper().getDeployMentDate();
		DateFormat dateFormat1 = new java.text.SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date = null;
		List<Timestamp> frTimeList = new ArrayList<Timestamp>();
		List<Timestamp> toTimeList = new ArrayList<Timestamp>();
		presentDate = null;
		toDate = null;
		prepstatement = null;
		statement = null;
		resultSet = null;

		try {
			prepstatement = con.prepareStatement(QueryManager
					.getAgtReconTktwiseDate());
			prepstatement.setInt(1, agtOrgId);

			toDate = pToDate;
			resultSet = prepstatement.executeQuery();
			while (resultSet.next()) {
				presentDate = resultSet.getTimestamp("recon_date");

				break;
			}
			if (presentDate == null || presentDate.before(toDate)) {
				if (presentDate == null) {
					statement = con.createStatement();
					resultSet = statement
							.executeQuery("select transaction_date from st_lms_bo_transaction_master where  party_id='"
									+ agtOrgId
									+ "' order by transaction_date  limit 1");
					while (resultSet.next()) {
						presentDate = resultSet
								.getTimestamp("transaction_date");
					}
					if (presentDate == null) {
						System.out
								.println(" #######  No transaction between BO and Agent "
										+ agtOrgId);
						return;
					}

				}

				Calendar from = Calendar.getInstance();
				Calendar to = Calendar.getInstance();
				from.setTimeInMillis(presentDate.getTime());
				to.setTimeInMillis(toDate.getTime());

				Calendar manipulatedFrom = Calendar.getInstance();
				Calendar manipulatedTo = Calendar.getInstance();

				Calendar tempFrom = Calendar.getInstance();
				Calendar tempTo = Calendar.getInstance();

				manipulatedFrom.setTimeInMillis(from.getTimeInMillis());
				manipulatedTo.setTimeInMillis(to.getTimeInMillis());

				manipulatedFrom.set(manipulatedFrom.get(Calendar.YEAR),
						manipulatedFrom.get(Calendar.MONTH), manipulatedFrom
								.get(Calendar.DATE), 0, 0, 0);
				manipulatedTo.set(manipulatedTo.get(Calendar.YEAR),
						manipulatedTo.get(Calendar.MONTH), manipulatedTo
								.get(Calendar.DATE), 0, 0, 0);
				System.out.println(" from " + from.getTime() + "   to "
						+ to.getTime() + "  manipulatedTo "
						+ manipulatedTo.getTime());

				if (manipulatedFrom.get(Calendar.YEAR) == manipulatedTo
						.get(Calendar.YEAR)
						&& manipulatedFrom.get(Calendar.MONTH) == manipulatedTo
								.get(Calendar.MONTH)
						&& manipulatedFrom.get(Calendar.DATE) == manipulatedTo
								.get(Calendar.DATE)
						&& manipulatedFrom.get(Calendar.HOUR) == manipulatedTo
								.get(Calendar.HOUR)
						&& manipulatedFrom.get(Calendar.MINUTE) == manipulatedTo
								.get(Calendar.MINUTE)
						&& manipulatedFrom.get(Calendar.SECOND) == manipulatedTo
								.get(Calendar.SECOND)) {
					frTimeList.add(new Timestamp(from.getTimeInMillis()));
					toTimeList.add(new Timestamp(to.getTimeInMillis()));
					System.out
							.println(agtOrgId
									+ " ###insertAgentTicketWiseRecon  same date 1111 frTimeList "
									+ frTimeList);
					System.out
							.println(agtOrgId
									+ " ###insertAgentTicketWiseRecon same date  11111toTimeList "
									+ toTimeList);

				} else {

					tempFrom.setTimeInMillis(from.getTimeInMillis());
					tempTo.setTimeInMillis(manipulatedFrom.getTimeInMillis());
					tempTo.add(Calendar.DATE, 1);
					int j = 0;

					while (!(tempFrom.get(Calendar.YEAR) == manipulatedTo
							.get(Calendar.YEAR)
							&& tempFrom.get(Calendar.MONTH) == manipulatedTo
									.get(Calendar.MONTH)
							&& tempFrom.get(Calendar.DATE) == manipulatedTo
									.get(Calendar.DATE)
							&& tempFrom.get(Calendar.HOUR) == manipulatedTo
									.get(Calendar.HOUR)
							&& tempFrom.get(Calendar.MINUTE) == manipulatedTo
									.get(Calendar.MINUTE) && tempFrom
							.get(Calendar.SECOND) == manipulatedTo
							.get(Calendar.SECOND))) {
						j++;

						frTimeList
								.add(new Timestamp(tempFrom.getTimeInMillis()));
						toTimeList.add(new Timestamp(tempTo.getTimeInMillis()));

						tempFrom.setTimeInMillis(tempTo.getTimeInMillis());
						tempTo.add(Calendar.DATE, 1);

					}
					if (tempTo.compareTo(to) > 0) {
						tempTo.add(Calendar.DATE, -1);
						if (!(tempTo.get(Calendar.YEAR) == to
								.get(Calendar.YEAR)
								&& tempTo.get(Calendar.MONTH) == to
										.get(Calendar.MONTH)
								&& tempTo.get(Calendar.DATE) == to
										.get(Calendar.DATE)
								&& tempTo.get(Calendar.HOUR) == to
										.get(Calendar.HOUR)
								&& tempTo.get(Calendar.MINUTE) == to
										.get(Calendar.MINUTE) && tempTo
								.get(Calendar.SECOND) == to
								.get(Calendar.SECOND))) {
							frTimeList.add(new Timestamp(tempTo
									.getTimeInMillis()));
							toTimeList.add(new Timestamp(to.getTimeInMillis()));
						}
					}

					System.out
							.println(agtOrgId
									+ "** ###insertAgentTicketWiseRecon  same date 1111 frTimeList "
									+ frTimeList);
					System.out
							.println(agtOrgId
									+ "** ###insertAgentTicketWiseRecon same date  11111toTimeList "
									+ toTimeList);
				}

				if (frTimeList != null) {
					for (int i = 0; i < frTimeList.size(); i++) {
						insertSalePurchaseDetails(con, agtOrgId, frTimeList
								.get(i), toTimeList.get(i), reconReportType);
						insertPaymentDetails(con, agtOrgId, frTimeList.get(i),
								toTimeList.get(i), reconReportType);
						insertPWTDetails(con, agtOrgId, frTimeList.get(i),
								toTimeList.get(i), reconReportType);

					}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void insertPaymentDetails(Connection con, int agtOrgId,
			Timestamp from, Timestamp toDate, String reconReportType) {
		Map<String, CashChqPmntBean> paymentMap = getPaymentBeanList(con,
				agtOrgId, from, toDate);
		CashChqPmntBean chqPmntBean;
		String bookwiseQuery = "insert into st_lms_agent_recon_bookwise (organization_id,recon_date,type,amt_parent,recon_balance) values(?,?,?,?,?)";
		String ticketWiseQuery = "insert into st_lms_agent_recon_ticketwise (organization_id,recon_date,type,amt_parent,recon_balance) values(?,?,?,?,?)";
		String insertQuery = null;
		if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
			insertQuery = bookwiseQuery;

		} else {
			insertQuery = ticketWiseQuery;

		}
		PreparedStatement preparedStatement;
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(insertQuery);

			double prevReconBal = getReconbalance(agtOrgId, con,
					reconReportType);
			System.out.println("prevReconBal initial " + prevReconBal);
			if (paymentMap.size() != 0) {
				Iterator paymentItr = paymentMap.entrySet().iterator();
				while (paymentItr.hasNext()) {
					Map.Entry<String, CashChqPmntBean> paymentItrpair = (Map.Entry<String, CashChqPmntBean>) paymentItr
							.next();
					chqPmntBean = paymentItrpair.getValue();
					preparedStatement.setInt(1, agtOrgId);
					preparedStatement.setTimestamp(2, toDate);
					if (chqPmntBean.getPaymentType().equalsIgnoreCase(
							"Cheque Bounce Charges")) {
						preparedStatement.setString(3, "ChqBounce Charges");
					} else {
						preparedStatement.setString(3, chqPmntBean
								.getPaymentType());
					}
					preparedStatement.setDouble(4, chqPmntBean
							.getPaymentAmount());
					if (chqPmntBean.getPaymentType().equalsIgnoreCase("Cash")
							|| chqPmntBean.getPaymentType().equalsIgnoreCase(
									"Credit Note")
							|| chqPmntBean.getPaymentType().equalsIgnoreCase(
									"Cheque")) {
						prevReconBal = prevReconBal
								- chqPmntBean.getPaymentAmount();
						preparedStatement.setDouble(5, prevReconBal);
					} else if (chqPmntBean.getPaymentType().equalsIgnoreCase(
							"Cheque Bounce")
							|| chqPmntBean.getPaymentType().equalsIgnoreCase(
									"Cheque Bounce Charges")
							|| chqPmntBean.getPaymentType().equalsIgnoreCase(
									"Debit Note")) {

						prevReconBal = prevReconBal
								+ chqPmntBean.getPaymentAmount();
						preparedStatement.setDouble(5, prevReconBal);
					}
					preparedStatement.executeUpdate();
					con.commit();
					System.out.println("prevReconBal after commit "
							+ prevReconBal);
				}
			} else {
				preparedStatement.setInt(1, agtOrgId);
				preparedStatement.setTimestamp(2, toDate);
				preparedStatement.setString(3, "N.A.");
				preparedStatement.setDouble(4, 0.0);
				preparedStatement.setDouble(5, prevReconBal);
				preparedStatement.executeUpdate();
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertPWTDetails(Connection con, int agtOrgId, Timestamp from,
			Timestamp toDate, String reconReportType) {
		List<PWTPaymentsBean> pwtBeanList = getPwtBeanList(con, agtOrgId, from,
				toDate);
		PreparedStatement preparedStatement;
		PWTPaymentsBean pwtPaymentsBean;
		String insertQuery;
		String bookwiseQuery = "insert into st_lms_agent_recon_bookwise (organization_id,game_id,recon_date,type,amt_parent,amt_child,recon_balance) values(?,?,?,?,?,?,?)";
		String ticketWiseQuery = "insert into st_lms_agent_recon_ticketwise (organization_id,game_id,recon_date,type,amt_parent,amt_child,recon_balance) values(?,?,?,?,?,?,?)";
		if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
			insertQuery = bookwiseQuery;

		} else {
			insertQuery = ticketWiseQuery;

		}
		System.out.println("###########################PWT Enter");
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(insertQuery);

			double prevReconBal = getReconbalance(agtOrgId, con,
					reconReportType);
			if (pwtBeanList.size() != 0) {
				Iterator pwtBeanListIterator = pwtBeanList.listIterator();
				while (pwtBeanListIterator.hasNext()) {
					pwtPaymentsBean = (PWTPaymentsBean) pwtBeanListIterator
							.next();

					// preparedStatement=con.prepareStatement(insertQuery);
					preparedStatement.setInt(1, agtOrgId);

					preparedStatement.setInt(2, pwtPaymentsBean.getGameNo());
					preparedStatement.setTimestamp(3, toDate);
					preparedStatement.setString(4, "PWT");
					preparedStatement.setDouble(5, pwtPaymentsBean
							.getTotalAmt());
					preparedStatement.setDouble(6, pwtPaymentsBean.getNetPWT());
					prevReconBal = prevReconBal - pwtPaymentsBean.getTotalAmt();
					preparedStatement.setDouble(7, prevReconBal);
					preparedStatement.executeUpdate();
					con.commit();

				}
			} else {
				preparedStatement.setInt(1, agtOrgId);

				preparedStatement.setInt(2, 0);
				preparedStatement.setTimestamp(3, toDate);
				preparedStatement.setString(4, "PWT");
				preparedStatement.setDouble(5, 0.0);
				preparedStatement.setDouble(6, 0.0);
				preparedStatement.setDouble(7, prevReconBal);
				preparedStatement.executeUpdate();
				con.commit();
			}
			System.out.println("###########################PWT Exit");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertSalePurchaseDetails(Connection con, int agtOrgId,
			Timestamp from, Timestamp toDate, String reconReportType) {
		Map<String, SalePurchaseBean> salemap;
		String bookwiseQuery = "insert into st_lms_agent_recon_bookwise (organization_id,game_id,recon_date,type,amt_parent,amt_child,recon_balance,mrp_amt) values(?,?,?,?,?,?,?,?)";
		String ticketWiseQuery = "insert into st_lms_agent_recon_ticketwise (organization_id,game_id,recon_date,type,amt_parent,amt_child,recon_balance,mrp_amt) values(?,?,?,?,?,?,?,?)";
		String insertQuery = null;
		if (reconReportType.equalsIgnoreCase("Book Wise Report")) {
			insertQuery = bookwiseQuery;
			salemap = getSalePurchaseDetails(con, agtOrgId, from, toDate);
			System.out.println("Book Wise Report Agent");
		} else {
			insertQuery = ticketWiseQuery;
			salemap = getSalePurchaseTicketDetails(con, agtOrgId, from, toDate);
			System.out.println("Ticket Wise Report Agent");
		}

		PreparedStatement preparedStatement;
		SalePurchaseBean salePurchaseBean;
		double prevReconBal = getReconbalance(agtOrgId, con, reconReportType);
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(insertQuery);
			if (salemap.size() != 0) {
				Iterator saleItr = salemap.entrySet().iterator();
				while (saleItr.hasNext()) {
					Map.Entry<String, SalePurchaseBean> saleItrpair = (Map.Entry<String, SalePurchaseBean>) saleItr
							.next();
					salePurchaseBean = saleItrpair.getValue();

					preparedStatement.setInt(1, agtOrgId);

					preparedStatement.setInt(2, salePurchaseBean.getGameId());
					preparedStatement.setTimestamp(3, toDate);
					preparedStatement.setString(4, "NET_SALE");
					preparedStatement.setDouble(5, salePurchaseBean
							.getPurchprice());
					preparedStatement.setDouble(6, salePurchaseBean
							.getSalePrice());

					prevReconBal = salePurchaseBean.getPurchprice()
							+ prevReconBal;
					preparedStatement.setDouble(7, prevReconBal);
					preparedStatement.setDouble(8, salePurchaseBean.getMrp());

					preparedStatement.executeUpdate();
					con.commit();

				}
			} else {
				preparedStatement.setInt(1, agtOrgId);

				preparedStatement.setInt(2, 0);
				preparedStatement.setTimestamp(3, toDate);
				preparedStatement.setString(4, "NET_SALE");
				preparedStatement.setDouble(5, 0.0);
				preparedStatement.setDouble(6, 0.0);
				preparedStatement.setDouble(7, prevReconBal);
				preparedStatement.setDouble(8, 0.0);

				preparedStatement.executeUpdate();
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reconAgentBookwiseEntry(int agtOrgId, Timestamp pToDate,
			String reconReportType) {
		Connection con = null;
		con = DBConnect.getConnection();
		String dep_Date = new LedgerHelper().getDeployMentDate();
		DateFormat dateFormat1 = new java.text.SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date = null;
		List<Timestamp> frTimeList = new ArrayList<Timestamp>();
		List<Timestamp> toTimeList = new ArrayList<Timestamp>();
		presentDate = null;
		toDate = null;
		prepstatement = null;
		statement = null;
		resultSet = null;
		try {
			prepstatement = con.prepareStatement(QueryManager
					.getAgtReconBookwiseDate());
			prepstatement.setInt(1, agtOrgId);

			toDate = pToDate;
			resultSet = prepstatement.executeQuery();
			while (resultSet.next()) {
				// present_date = rs1.getDate("date");
				presentDate = resultSet.getTimestamp("recon_date");

				break;
			}
			if (presentDate == null || presentDate.before(toDate)) {
				if (presentDate == null) {
					statement = con.createStatement();
					resultSet = statement
							.executeQuery("select transaction_date from st_lms_bo_transaction_master where party_id='"
									+ agtOrgId
									+ "' order by transaction_date  limit 1");
					while (resultSet.next()) {
						presentDate = resultSet
								.getTimestamp("transaction_date");
						System.out.println("presentDate =======  "
								+ presentDate + " agtOrgId ====== " + agtOrgId);
					}
					if (presentDate == null) {
						return;
					}

				}

				Calendar from = Calendar.getInstance();
				Calendar to = Calendar.getInstance();
				from.setTimeInMillis(presentDate.getTime());
				to.setTimeInMillis(toDate.getTime());

				Calendar manipulatedFrom = Calendar.getInstance();
				Calendar manipulatedTo = Calendar.getInstance();

				Calendar tempFrom = Calendar.getInstance();
				Calendar tempTo = Calendar.getInstance();

				manipulatedFrom.setTimeInMillis(from.getTimeInMillis());
				manipulatedTo.setTimeInMillis(to.getTimeInMillis());

				manipulatedFrom.set(manipulatedFrom.get(Calendar.YEAR),
						manipulatedFrom.get(Calendar.MONTH), manipulatedFrom
								.get(Calendar.DATE), 0, 0, 0);
				manipulatedTo.set(manipulatedTo.get(Calendar.YEAR),
						manipulatedTo.get(Calendar.MONTH), manipulatedTo
								.get(Calendar.DATE), 0, 0, 0);

				if (manipulatedFrom.get(Calendar.YEAR) == manipulatedTo
						.get(Calendar.YEAR)
						&& manipulatedFrom.get(Calendar.MONTH) == manipulatedTo
								.get(Calendar.MONTH)
						&& manipulatedFrom.get(Calendar.DATE) == manipulatedTo
								.get(Calendar.DATE)
						&& manipulatedFrom.get(Calendar.HOUR) == manipulatedTo
								.get(Calendar.HOUR)
						&& manipulatedFrom.get(Calendar.MINUTE) == manipulatedTo
								.get(Calendar.MINUTE)
						&& manipulatedFrom.get(Calendar.SECOND) == manipulatedTo
								.get(Calendar.SECOND)) {
					frTimeList.add(new Timestamp(from.getTimeInMillis()));
					toTimeList.add(new Timestamp(to.getTimeInMillis()));
					System.out
							.println(agtOrgId
									+ " ###reconAgentBookwiseEntry  same date 1111 frTimeList "
									+ frTimeList);
					System.out
							.println(agtOrgId
									+ "###reconAgentBookwiseEntry same date  11111toTimeList "
									+ toTimeList);
				} else {

					tempFrom.setTimeInMillis(from.getTimeInMillis());
					tempTo.setTimeInMillis(manipulatedFrom.getTimeInMillis());
					tempTo.add(Calendar.DATE, 1);
					int j = 0;

					while (!(tempFrom.get(Calendar.YEAR) == manipulatedTo
							.get(Calendar.YEAR)
							&& tempFrom.get(Calendar.MONTH) == manipulatedTo
									.get(Calendar.MONTH)
							&& tempFrom.get(Calendar.DATE) == manipulatedTo
									.get(Calendar.DATE)
							&& tempFrom.get(Calendar.HOUR) == manipulatedTo
									.get(Calendar.HOUR)
							&& tempFrom.get(Calendar.MINUTE) == manipulatedTo
									.get(Calendar.MINUTE) && tempFrom
							.get(Calendar.SECOND) == manipulatedTo
							.get(Calendar.SECOND))) {
						j++;

						frTimeList
								.add(new Timestamp(tempFrom.getTimeInMillis()));
						toTimeList.add(new Timestamp(tempTo.getTimeInMillis()));

						tempFrom.setTimeInMillis(tempTo.getTimeInMillis());
						tempTo.add(Calendar.DATE, 1);

					}
					if (tempTo.compareTo(to) > 0) {
						tempTo.add(Calendar.DATE, -1);
						if (!(tempTo.get(Calendar.YEAR) == to
								.get(Calendar.YEAR)
								&& tempTo.get(Calendar.MONTH) == to
										.get(Calendar.MONTH)
								&& tempTo.get(Calendar.DATE) == to
										.get(Calendar.DATE)
								&& tempTo.get(Calendar.HOUR) == to
										.get(Calendar.HOUR)
								&& tempTo.get(Calendar.MINUTE) == to
										.get(Calendar.MINUTE) && tempTo
								.get(Calendar.SECOND) == to
								.get(Calendar.SECOND))) {
							frTimeList.add(new Timestamp(tempTo
									.getTimeInMillis()));
							toTimeList.add(new Timestamp(to.getTimeInMillis()));
						}
					}
					System.out
							.println(agtOrgId
									+ " ###reconAgentBookwiseEntry  Final 1111 frTimeList "
									+ frTimeList);
					System.out
							.println(agtOrgId
									+ " ###reconAgentBookwiseEntry Final  11111toTimeList "
									+ toTimeList);

				}

				for (int i = 0; i < frTimeList.size(); i++) {
					insertSalePurchaseDetails(con, agtOrgId, frTimeList.get(i),
							toTimeList.get(i), reconReportType);
					insertPaymentDetails(con, agtOrgId, frTimeList.get(i),
							toTimeList.get(i), reconReportType);
					insertPWTDetails(con, agtOrgId, frTimeList.get(i),
							toTimeList.get(i), reconReportType);

				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void reconAgentTicketwiseEntry(int agtOrgId, Timestamp pToDate,
			String reconReportType) {
		System.out.println("+++++++++++reconAgentTicketwiseEntry agtOrgId  "
				+ agtOrgId);
		List timeList = null;
		ReconcilationReportRetHelper rrh = new ReconcilationReportRetHelper();
		Map retOrgIdMap = rrh.getRetOrgMap(agtOrgId, pToDate);
		Iterator retOrgitr = retOrgIdMap.entrySet().iterator();
		while (retOrgitr.hasNext()) {
			Map.Entry<Integer, String> retOrgpair = (Map.Entry<Integer, String>) retOrgitr
					.next();
			int retOrgId = retOrgpair.getKey();
			new ReconcilationReportRetHelper().reconRetTktwiseEntry(retOrgId,
					pToDate, agtOrgId, reconReportType);
			System.out.println("After return time list" + timeList);
		}

		insertAgentTicketWiseRecon(agtOrgId, pToDate, reconReportType);

	}

}
