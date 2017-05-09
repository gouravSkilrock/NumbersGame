package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
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
import com.skilrock.lms.beans.SalePurchaseRetBean;
import com.skilrock.lms.beans.StockMrpDiscountBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

public class ReconcilationReportRetHelper {

	public static void main(String[] args) {
		// Map<Integer, SalePurchaseRetBean> salegameMap=new
		// ReconcilationReportRetHelper().getSalePurchaseDetails(con, retOrgId,
		// from, toDate);
		System.out.println("done@@@@@@");
	}

	private double closingBal = 0.0;
	private double closingBal2 = 0.0;
	private double finalclosingBal = 0.0;
	private double finalopeningBal = 0.0;
	private double openingBal = 0.0;
	private double openingBal2 = 0.0;
	private PreparedStatement prepstatement;
	Timestamp presentDate = null;
	private ResultSet resultSet;
	private ResultSet resultSet2;
	// private Connection con;
	private Statement statement;
	private Statement statement2;
	Timestamp toDate = null;
	private double totalAmount = 0.0;
	double totalPurchAmount = 0.0;
	double totalSaleAmount = 0.0;
	int totalSaleBooks = 0;
	double totalSaleComm = 0.0;

	double totalSaleMrp = 0.0;

	public List createConReportAftrPmnt(int retOrgId, Timestamp from,
			Timestamp toDate, SimpleDateFormat dateformat1) {
		// double opLedbalance=0.0;
		// double closLedbalance=0.0;
		double weeklyOpeningBal = 0.0;
		String reconReportTypeRet = null;
		List consolidatedList = new ArrayList();
		ConsolidatedBean consolidatedBean = null;
		ReconcilationReportHelper agtreconhelper = new ReconcilationReportHelper();
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			System.out.println(" ======createConsolidatedReport ========");

			System.out.println("Retailer org ID ===== " + retOrgId);
			// opLedbalance= getLedgerBal(retOrgId, from);
			// closLedbalance= getLedgerBal(retOrgId, toDate);
			reconReportTypeRet = agtreconhelper.getReconRepType(retOrgId);
			System.out.println("from " + from);
			weeklyOpeningBal = getPrevBalFtrpymnt(retOrgId, from,
					reconReportTypeRet);
			System.out.println("weeklyOpeningBal  ====== " + weeklyOpeningBal);
			consolidatedBean = getConDetAftrPmt(con, retOrgId, from, toDate,
					weeklyOpeningBal);
			consolidatedBean.setWeeklyOpeningBal(weeklyOpeningBal);
			// consolidatedBean.setOpeningLedBal(opLedbalance);
			// consolidatedBean.setClosingLedBal(closLedbalance);
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

	public List createConsolidatedReport(Map retOrgIdMap, Timestamp from,
			Timestamp toDate, SimpleDateFormat dateformat1) {
		double opLedbalance = 0.0;
		double closLedbalance = 0.0;
		double weeklyOpeningBal = 0.0;
		String reconReportTypeRet = null;
		List consolidatedList = new ArrayList();
		ConsolidatedBean consolidatedBean = null;
		ReconcilationReportHelper agtreconhelper = new ReconcilationReportHelper();
		Iterator retOrgitr = retOrgIdMap.entrySet().iterator();
		Timestamp regDate = null;
		Timestamp fromDate = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			System.out.println(" ======createConsolidatedReport ========");
			while (retOrgitr.hasNext()) {
				Map.Entry<Integer, String> retOrgpair = (Map.Entry<Integer, String>) retOrgitr
						.next();
				int retOrgId = retOrgpair.getKey();
				statement = con.createStatement();
				resultSet = statement
						.executeQuery("select registration_date from st_lms_user_master where organization_id='"
								+ retOrgId + "' and isrolehead='Y' ");
				while (resultSet.next()) {
					regDate = resultSet.getTimestamp("registration_date");
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
				System.out.println("Retailer org ID ===== " + retOrgId);
				opLedbalance = 0.0;
				closLedbalance = 0.0;
				opLedbalance = getLedgerBal(retOrgId, fromDate);

				closLedbalance = getLedgerBal(retOrgId, toDate);
				reconReportTypeRet = agtreconhelper.getReconRepType(retOrgId);
				weeklyOpeningBal = getPreviousBalance(retOrgId, fromDate,
						reconReportTypeRet);
				System.out.println("weeklyOpeningBal  ====== "
						+ weeklyOpeningBal);
				consolidatedBean = getConsolidatedDetails(con, retOrgId,
						fromDate, toDate, weeklyOpeningBal);
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

	public List CreateReport(int retOrgId, Timestamp from, Timestamp toDate,
			SimpleDateFormat dateformat1) {
		Connection con = null;
		con = DBConnect.getConnection();
		List reconList = new ArrayList();
		List<SalePurchaseBean> saleList = null;
		List<PWTPaymentsBean> pwtList = null;
		List<CashChqPmntBean> paymentList = null;
		Map<String, StockMrpDiscountBean> stockMrpDiscMap = null;
		ReportBean reportBean = new ReportBean();
		try {

			saleList = getSalePurchBeanTktWiseList(con, retOrgId, from, toDate);
			pwtList = getPwtBeanBookTktList(con, retOrgId, from, toDate);
			paymentList = getPaymentBeanTktWiseList(con, retOrgId, from, toDate);

			stockMrpDiscMap = getStockMrpDiscTktWiseMap(con, retOrgId, from,
					toDate);
			reportBean.setSaleList(saleList);
			reportBean.setPwtList(pwtList);
			reportBean.setPaymentList(paymentList);
			reportBean.setStockMrpDiscMap(stockMrpDiscMap);

			resultSet = statement
					.executeQuery("select balance,account_type from st_lms_agent_ledger where (account_type ='"
							+ retOrgId
							+ "' or account_type = '"
							+ retOrgId
							+ "#') and transaction_date <'"
							+ from
							+ "' order by transaction_date desc, task_id desc limit 2");
			System.out
					.println("opning balance Ret select balance,account_type from st_lms_agent_ledger where (account_type ='"
							+ retOrgId
							+ "' or account_type = '"
							+ retOrgId
							+ "#') and transaction_date <'"
							+ from
							+ "' order by transaction_date desc, task_id desc limit 2");
			boolean count = false;
			int countt = 0;
			while (resultSet.next()) {

				if (resultSet.getString("account_type").equalsIgnoreCase(
						retOrgId + "#")) {
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
					.executeQuery("select balance,account_type from st_lms_agent_ledger where (account_type = '"
							+ retOrgId
							+ "' or account_type = '"
							+ retOrgId
							+ "#') and transaction_date <='"
							+ toDate
							+ "' order by transaction_date desc, task_id desc limit 2");
			System.out
					.println("Closing balance Ret select balance,account_type from st_lms_agent_ledger where (account_type = '"
							+ retOrgId
							+ "' or account_type = '"
							+ retOrgId
							+ "#') and transaction_date <='"
							+ toDate
							+ "' order by transaction_date desc, task_id desc limit 2");
			boolean count2 = false;
			int countt2 = 0;
			while (resultSet.next()) {
				if (resultSet.getString("account_type").equalsIgnoreCase(
						retOrgId + "#")) {
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
				System.out.println(" inside if calendar Ret "
						+ dateformat1.format(from) + "  cTo "
						+ dateformat1.format(calendar.getTime()));
			}
			System.out.println(" inside if calendar Ret "
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

	private ConsolidatedBean getConDetAftrPmt(Connection con, int retOrgId,
			Timestamp from, Timestamp toDate, double weeklyOpeningBal) {
		double weeklyClosingBal = weeklyOpeningBal;
		ConsolidatedBean consolidatedBean = new ConsolidatedBean();

		System.out.println("getConsolidatedDetails ========== ");
		try {
			statement = con.createStatement();
			resultSet = statement
					.executeQuery("select sum(amt_child) as purchAmount ,sum(mrp-amt_child) as comm from st_lms_ret_recon_ticketwise arb where retailer_org_id='"
							+ retOrgId
							+ "' and type='NET_SALE' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' group by retailer_org_id");
			while (resultSet.next()) {
				consolidatedBean.setNetPayable(resultSet
						.getDouble("purchAmount"));
				weeklyClosingBal = weeklyClosingBal
						+ resultSet.getDouble("purchAmount");
			}
			System.out.println("getConsolidatedDetails ========== 2");

			resultSet = statement
					.executeQuery("select sum(amt_parent) as totalPWT from st_lms_ret_recon_ticketwise arb where retailer_org_id='"
							+ retOrgId
							+ "'  and type='PWT' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' group by retailer_org_id");
			while (resultSet.next()) {
				consolidatedBean.setNetPWT(resultSet.getDouble("totalPWT"));
				weeklyClosingBal = weeklyClosingBal
						- resultSet.getDouble("totalPWT");
			}
			System.out.println("getConsolidatedDetails ========== 3");

			resultSet = statement
					.executeQuery("select sum(amt_parent) as total,type  from st_lms_ret_recon_ticketwise  where retailer_org_id='"
							+ retOrgId
							+ "' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' and game_id is null   group by type");

			while (resultSet.next()) {
				if (resultSet.getString("type").equalsIgnoreCase("Cash")
						|| resultSet.getString("type").equalsIgnoreCase(
								"Cheque")) {
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
							+ retOrgId + "' ");
			while (resultSet.next()) {
				consolidatedBean.setUserName(resultSet.getString("name"));
			}
			consolidatedBean.setWeeklyClosingBal(weeklyClosingBal);
			System.out.println("getConsolidatedDetails ========== 4");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return consolidatedBean;
	}

	private ConsolidatedBean getConsolidatedDetails(Connection con,
			int retOrgId, Timestamp from, Timestamp toDate,
			double weeklyOpeningBal) {
		double weeklyClosingBal = weeklyOpeningBal;
		ConsolidatedBean consolidatedBean = new ConsolidatedBean();

		System.out.println(" Retailer getConsolidatedDetails ========== ");
		try {
			statement = con.createStatement();
			resultSet = statement
					.executeQuery("select sum(amt_child) as purchAmount ,sum(mrp-amt_child) as comm from st_lms_ret_recon_ticketwise arb where retailer_org_id='"
							+ retOrgId
							+ "' and type='NET_SALE' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' group by retailer_org_id");
			while (resultSet.next()) {
				consolidatedBean.setNetPayable(resultSet
						.getDouble("purchAmount"));
				weeklyClosingBal = weeklyClosingBal
						+ resultSet.getDouble("purchAmount");
			}
			System.out.println("Retailer getConsolidatedDetails ========== 2");

			resultSet = statement
					.executeQuery("select sum(amt_parent) as totalPWT from st_lms_ret_recon_ticketwise arb where retailer_org_id='"
							+ retOrgId
							+ "'  and type='PWT' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' group by retailer_org_id");
			while (resultSet.next()) {
				consolidatedBean.setNetPWT(resultSet.getDouble("totalPWT"));
				weeklyClosingBal = weeklyClosingBal
						- resultSet.getDouble("totalPWT");
			}
			System.out.println("Retailer getConsolidatedDetails ========== 3");

			resultSet = statement
					.executeQuery("select sum(amt_parent) as total,type  from st_lms_ret_recon_ticketwise  where retailer_org_id='"
							+ retOrgId
							+ "' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' and game_id is null   group by type");

			while (resultSet.next()) {
				if (resultSet.getString("type").equalsIgnoreCase("Cash")
						|| resultSet.getString("type").equalsIgnoreCase(
								"Cheque")) {
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
							+ retOrgId + "' ");
			while (resultSet.next()) {
				consolidatedBean.setUserName(resultSet.getString("name"));
			}
			consolidatedBean.setWeeklyClosingBal(weeklyClosingBal);
			System.out.println("Retailer getConsolidatedDetails ========== 4");

			Map<String, StockMrpDiscountBean> stockMrpDiscMap = getStockMrpDiscTktWiseMap(
					con, retOrgId, from, toDate);
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return consolidatedBean;
	}

	public List getDetailsAftrLstPmnt(int retOrgId, Timestamp from,
			Timestamp toDate, SimpleDateFormat dateformat1) {
		String query = "select amount,transaction_date,transaction_type from(select 'Cash' as transaction_type,sbtm.transaction_id,amount,transaction_date from st_lms_agent_transaction_master sbtm,st_lms_agent_cash_transaction sbct where (transaction_type='CASH' )  and transaction_date>='"
				+ from
				+ "' and transaction_date<='"
				+ toDate
				+ "' and sbtm.transaction_id=sbct.transaction_id and party_id='"
				+ retOrgId
				+ "' union select 'Cheque' as transaction_type,sbtm.transaction_id,cheque_amt as amount,transaction_date from st_lms_agent_transaction_master sbtm,st_lms_agent_sale_chq sbsc where (sbtm.transaction_type='CHEQUE')  and transaction_date>='"
				+ from
				+ "' and transaction_date<='"
				+ toDate
				+ "' and sbtm.transaction_id=sbsc.transaction_id and party_id='"
				+ retOrgId + "') final order by transaction_id desc limit 1";

		System.out.println(query);
		Connection con = null;
		con = DBConnect.getConnection();
		double amount = 0.0;
		Timestamp timestamp = null;
		List list1 = null;
		String transactionType = null;
		try {
			statement = con.createStatement();

			resultSet = statement.executeQuery(query);
			System.out
					.println("############### getDetailsAftrLstPmnt " + query);
			while (resultSet.next()) {
				amount = resultSet.getDouble("amount");
				timestamp = resultSet.getTimestamp("transaction_date");
				transactionType = resultSet.getString("transaction_type");
				list1 = (List) new ReconcilationReportRetHelper()
						.createConReportAftrPmnt(retOrgId, timestamp, toDate,
								dateformat1);

			}
			System.out.println("timestamp " + timestamp);
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

	public double getLedgerBal(int retOrgId, Timestamp time) {
		double ledgerBal = 0.0;
		double ledgerBal2 = 0.0;
		double finalLedgerBal = 0.0;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			statement = con.createStatement();
			resultSet = statement
					.executeQuery("select balance,account_type from st_lms_agent_ledger where (account_type ='"
							+ retOrgId
							+ "' or account_type = '"
							+ retOrgId
							+ "#') and transaction_date <'"
							+ time
							+ "' order by transaction_date desc, task_id desc limit 2");

			System.out
					.println("opning balance Ret select balance,account_type from st_lms_agent_ledger where (account_type ='"
							+ retOrgId
							+ "' or account_type = '"
							+ retOrgId
							+ "#') and transaction_date <'"
							+ time
							+ "' order by transaction_date desc, task_id desc limit 2");
			boolean count = false;
			int countt = 0;
			while (resultSet.next()) {

				if (resultSet.getString("account_type").equalsIgnoreCase(
						retOrgId + "#")) {
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

	public Map<String, CashChqPmntBean> getPaymentBeanList(Connection con,
			int retOrgId, Timestamp from, Timestamp toDate) {
		String creditQuery = "select 'Cash' as transactionType,amount,sbct.transaction_id from st_lms_agent_cash_transaction sbct,st_lms_agent_transaction_master sbtm where transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbct.transaction_id=sbtm.transaction_id and party_id='"
				+ retOrgId
				+ "' union select 'Cheque',cheque_amt as amount,sbsc.transaction_id from st_lms_agent_sale_chq sbsc,st_lms_agent_transaction_master sbtm  where sbtm.transaction_type='CHEQUE' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbsc.transaction_id=sbtm.transaction_id and  party_id='"
				+ retOrgId + "'";
		String debitQuery = "select 'Cheque Bounce' as transactionType,cheque_amt as amount,sbsc.transaction_id from st_lms_agent_sale_chq sbsc,st_lms_agent_transaction_master sbtm  where sbtm.transaction_type='CHQ_BOUNCE' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbsc.transaction_id=sbtm.transaction_id and  party_id='"
				+ retOrgId
				+ "' union select 'Cheque Bounce Charges',amount,sbdn.transaction_id from st_lms_agent_debit_note sbdn,st_lms_agent_transaction_master sbtm  where sbtm.transaction_type='DR_NOTE' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbdn.transaction_id=sbtm.transaction_id and  party_id='"
				+ retOrgId
				+ "' union select 'Debit Note',amount,sbdn.transaction_id from st_lms_agent_debit_note sbdn,st_lms_agent_transaction_master sbtm  where sbtm.transaction_type='DR_NOTE_CASH' and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbdn.transaction_id=sbtm.transaction_id and  party_id='"
				+ retOrgId + "'";
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

	private List<CashChqPmntBean> getPaymentBeanTktWiseList(Connection con,
			int retOrgId, Timestamp from, Timestamp toDate) {
		CashChqPmntBean paymentBean;
		List<CashChqPmntBean> paymentList = new ArrayList<CashChqPmntBean>();
		try {
			statement = con.createStatement();
			resultSet = statement
					.executeQuery("select type,sum(amt_parent) as total from st_lms_ret_recon_ticketwise  where retailer_org_id='"
							+ retOrgId
							+ "' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' and game_id is null   group by type");
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

	public double getPrevBalFtrpymnt(int retOrgId, Timestamp frDate,
			String reconReportTypeRet) throws LMSException {
		Connection con = null;
		con = DBConnect.getConnection();
		// resultSet=statement.executeQuery("select recon_balance from
		// st_lms_agent_recon_bookwise where org_id='"+retOrgId+"' and
		// recon_date< '"+frDate+"' order by recon_date desc,task_id desc limit
		// 1");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(frDate.getTime());
		calendar.add(Calendar.MINUTE, 10);
		double balance = 0.0;
		String bookwiseQuery = "select recon_balance from st_ret_recon_bookwise where retailer_org_id='"
				+ retOrgId
				+ "' and recon_date < '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		String ticketWiseQuery = "select recon_balance from st_lms_ret_recon_ticketwise where retailer_org_id='"
				+ retOrgId
				+ "' and recon_date < '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		String query;
		try {
			Statement statement = con.createStatement();
			if (reconReportTypeRet.equalsIgnoreCase("Book Wise Report")) {
				query = bookwiseQuery;
			} else {
				query = ticketWiseQuery;
			}
			resultSet = statement.executeQuery(query);
			System.out.println("balance query  " + query);

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
		System.out.println("balance " + balance);
		return balance;
	}

	public double getPreviousBalance(int retOrgId, Timestamp frDate,
			String reconReportTypeRet) throws LMSException {
		Connection con = null;
		con = DBConnect.getConnection();
		// resultSet=statement.executeQuery("select recon_balance from
		// st_lms_agent_recon_bookwise where org_id='"+retOrgId+"' and
		// recon_date< '"+frDate+"' order by recon_date desc,task_id desc limit
		// 1");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(frDate.getTime());
		calendar.add(Calendar.MINUTE, 10);
		double balance = 0.0;
		String bookwiseQuery = "select recon_balance from st_ret_recon_bookwise where retailer_org_id='"
				+ retOrgId
				+ "' and recon_date< '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		String ticketWiseQuery = "select recon_balance from st_lms_ret_recon_ticketwise where retailer_org_id='"
				+ retOrgId
				+ "' and recon_date< '"
				+ new Timestamp(calendar.getTimeInMillis())
				+ "' order by recon_date desc,task_id desc limit 1";
		String query;
		try {
			Statement statement = con.createStatement();
			if (reconReportTypeRet.equalsIgnoreCase("Book Wise Report")) {
				query = bookwiseQuery;
			} else {
				query = ticketWiseQuery;
			}
			resultSet = statement.executeQuery(query);
			System.out.println("balance query  " + query);

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
		System.out.println("balance " + balance);
		return balance;
	}

	private List<PWTPaymentsBean> getPwtBeanBookTktList(Connection con,
			int retOrgId, Timestamp from, Timestamp toDate) {
		PWTPaymentsBean pwtPaymentsBean;
		List<PWTPaymentsBean> pwtList = new ArrayList<PWTPaymentsBean>();
		try {
			statement = con.createStatement();
			resultSet = statement
					.executeQuery("select sgm.game_nbr,sgm.game_id,game_name,type,sum(amt_parent) as totalPWT,sum(amt_child) as netPWT ,sum(amt_parent-amt_child) as comm from st_lms_ret_recon_ticketwise arb,st_se_game_master sgm where retailer_org_id='"
							+ retOrgId
							+ "' and sgm.game_id=arb.game_id and type='PWT' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' group by game_id");
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

	public List<PWTPaymentsBean> getPwtBeanList(Connection con, int retOrgId,
			Timestamp from, Timestamp toDate) {
		String pwtQuery = "select sgm.game_id,sum(pwt_amt) as pwt_amt,sum(comm_amt) as comm_amt,sum(net_amt) as net_amt ,game_name from  st_se_agent_pwt sbp, st_lms_agent_transaction_master sbtm,st_se_game_master sgm where sbp.game_id=sgm.game_id and transaction_date>='"
				+ from
				+ "' and transaction_date <='"
				+ toDate
				+ "' and sbp.transaction_id=sbtm.transaction_id and party_id='"
				+ retOrgId + "' group by game_id";
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
			System.out.println(pwtList.size() + " pwt query " + pwtQuery
					+ "  totalAmt == " + totalAmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getPwtBeanList  exit ****** "
				+ totalAmount + " totalAmt ==== " + totalAmt);
		return pwtList;
	}

	public double getReconbalance(int retOrgId, Connection connection,
			String reconReportTypeRet) {

		double balance = 0.0;
		// String bookwiseQuery="select recon_balance from st_ret_recon_bookwise
		// where retailer_org_id='"+retOrgId+"' order by recon_date desc,task_id
		// desc limit 1";
		String ticketWiseQuery = "select recon_balance from st_lms_ret_recon_ticketwise where retailer_org_id='"
				+ retOrgId + "'  order by recon_date desc,task_id desc limit 1";
		String query;
		try {
			Statement statement = connection.createStatement();
			/*
			 * if(reconReportTypeRet.equalsIgnoreCase("Book Wise Report")){
			 * query=bookwiseQuery; }else{
			 */
			query = ticketWiseQuery;
			// }
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

	public Map<Integer, String> getRetOrgMap(int userOrgId, Timestamp toDate) {
		Connection conn = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet result = null;
		Map<Integer, String> orgMap = new HashMap<Integer, String>();
		try {
			stmt = conn.createStatement();
			result = stmt
					.executeQuery("select name,om.organization_id from st_lms_organization_master om,st_lms_user_master um where om.organization_type='RETAILER' and  parent_id='"
							+ userOrgId
							+ "' and om.organization_id=um.organization_id and registration_date<'"
							+ toDate + "' order by name");
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
		return orgMap;

	}

	public Map<Integer, SalePurchaseRetBean> getSalePurchaseDetails(
			Connection con, int retOrgId, Timestamp from, Timestamp toDate) {
		// String salebooklistQuery="select
		// sgtih.game_id,game_name,book_nbr,date,sum(sold_tickets)
		// soldTickets,(ticket_price*sum(sold_tickets))as totalmrp from
		// st_se_game_ticket_inv_history sgtih ,st_se_game_master sgm where
		// current_owner_id='"+retOrgId+"' and date>='"+from+"' and
		// date<='"+toDate+"' and (status='ACTIVE_CLOSE' or
		// status='CLAIMED_CLOSE') and sgtih.game_id=sgm.game_id group by
		// book_nbr,game_id";
		String salebooklistQuery = "select cur_rem_tickets,game_id,game_name,book_nbr,date,sum(sold_tickets) soldTickets,(ticket_price*sum(sold_tickets))as totalmrp from (select cur_rem_tickets,sgtih.game_id,game_name,book_nbr,date,sold_tickets,ticket_price  from st_se_game_ticket_inv_history sgtih ,st_se_game_master sgm where current_owner_id='"
				+ retOrgId
				+ "'  and date>='"
				+ from
				+ "' and date<='"
				+ toDate
				+ "' and (status='ACTIVE_CLOSE' or status='CLAIMED_CLOSE') and sgtih.game_id=sgm.game_id  order by task_id desc ) aa group by book_nbr,game_id";
		String saleBookCommDetails = "select game_id,(1-(transacrion_sale_comm_rate*.01)) as retpurch,(1-(transaction_purchase_comm_rate*.01)) as agtpurch from st_se_game_inv_detail where book_nbr=? order by  transaction_date desc limit 1";
		String saleDrawGamesDetails = "";

		Timestamp transactionDate = null;

		System.out.println("Total amount in getSalePurchaseBeanList  enter "
				+ totalAmount);

		String gameName = null;
		String bookNo = null;
		double salePrice = 0.0;
		double mrp = 0.0;
		double retpurchPrice = 0.0;
		double agtpurchPrice = 0.0;
		double comm = 0.0;
		int gameId = 0;
		int soldTickets = 0;
		int remTickets = 0;
		SalePurchaseRetBean salePurchaseRetBean = null;
		List<String> salebookList = new ArrayList<String>();
		Map<Integer, SalePurchaseRetBean> salegameMap = new HashMap<Integer, SalePurchaseRetBean>();

		try {

			statement = con.createStatement();
			System.out.println("salebooklistQuery " + salebooklistQuery);
			resultSet = statement.executeQuery(salebooklistQuery);

			while (resultSet.next()) {

				gameName = resultSet.getString("game_name");
				transactionDate = resultSet.getTimestamp("date");
				mrp = resultSet.getDouble("totalmrp");
				soldTickets = resultSet.getInt("soldTickets");
				bookNo = resultSet.getString("book_nbr");
				gameId = resultSet.getInt("game_id");
				remTickets = resultSet.getInt("cur_rem_tickets");

				salebookList.add(bookNo);
				if (salegameMap.containsKey(gameId)) {
					System.out.println("before cast");
					salePurchaseRetBean = (SalePurchaseRetBean) salegameMap
							.get(gameId);
					System.out.println("after cast");
					salePurchaseRetBean.setGameName(salePurchaseRetBean
							.getGameName());
					salePurchaseRetBean.setMrp(salePurchaseRetBean.getMrp()
							+ mrp);
					salePurchaseRetBean.setRetpurchPrice(salePurchaseRetBean
							.getRetpurchPrice()
							+ retpurchPrice);
					salePurchaseRetBean.setAgtpurchPrice(salePurchaseRetBean
							.getAgtpurchPrice()
							+ agtpurchPrice);
					salePurchaseRetBean.setComm(salePurchaseRetBean.getComm()
							+ comm);
					salePurchaseRetBean.setSoldTickets(salePurchaseRetBean
							.getSoldTickets()
							+ soldTickets);
					salePurchaseRetBean.setGameId(salePurchaseRetBean
							.getGameId());
					salePurchaseRetBean.setRemTickets(remTickets);
					System.out.println("entry udated ");

				} else {
					salePurchaseRetBean = new SalePurchaseRetBean();
					salePurchaseRetBean.setGameName(gameName);
					salePurchaseRetBean.setMrp(mrp);
					salePurchaseRetBean.setRetpurchPrice(retpurchPrice);
					salePurchaseRetBean.setAgtpurchPrice(agtpurchPrice);
					salePurchaseRetBean.setComm(comm);
					salePurchaseRetBean.setSoldTickets(soldTickets);
					salePurchaseRetBean.setGameId(gameId);
					salePurchaseRetBean.setRemTickets(remTickets);
					System.out.println("done 1111111111111 entry ");
					salegameMap.put(gameId, salePurchaseRetBean);
					System.out.println("putting in map  ");
				}

			}
			prepstatement = con.prepareStatement(saleBookCommDetails);
			Iterator saleBookItr = salebookList.iterator();
			while (saleBookItr.hasNext()) {
				prepstatement.setString(1, (String) saleBookItr.next());
				resultSet = prepstatement.executeQuery();
				while (resultSet.next()) {

					retpurchPrice = resultSet.getDouble("retpurch");
					agtpurchPrice = resultSet.getDouble("agtpurch");
					gameId = resultSet.getInt("game_id");
					if (salegameMap.containsKey(gameId)) {
						salePurchaseRetBean = (SalePurchaseRetBean) salegameMap
								.get(gameId);
						salePurchaseRetBean
								.setRetpurchPrice(salePurchaseRetBean.getMrp()
										* retpurchPrice);
						salePurchaseRetBean
								.setAgtpurchPrice(salePurchaseRetBean.getMrp()
										* agtpurchPrice);
						salePurchaseRetBean.setComm(salePurchaseRetBean
								.getMrp()
								- salePurchaseRetBean.getMrp() * retpurchPrice);

					}

				}

			}
			/*
			 * this is for draw games
			 */

			/*
			 * draw game sale end
			 */
			System.out.println("map size salebookMap " + salegameMap.size());
			/*
			 * saleBookItr = salegameMap.entrySet().iterator(); while
			 * (saleBookItr.hasNext()) { Map.Entry<String,SalePurchaseRetBean>
			 * saleBookItrpair = (Map.Entry<String,SalePurchaseRetBean>)saleBookItr.next();
			 * salePurchaseRetBean=saleBookItrpair.getValue(); }
			 */

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return salegameMap;
	}

	private List<SalePurchaseBean> getSalePurchBeanTktWiseList(Connection con,
			int retOrgId, Timestamp from, Timestamp toDate) {

		SalePurchaseBean salePurchaseBean;
		List<SalePurchaseBean> saleList = new ArrayList<SalePurchaseBean>();
		try {
			statement = con.createStatement();
			resultSet = statement
					.executeQuery("select sgm.game_nbr,game_name,sgm.game_id,  sum(mrp_amt) as mrp,type,sum(amt_child) as purchAmount ,sum(mrp-amt_child) as comm from st_lms_ret_recon_ticketwise arb,st_se_game_master sgm where retailer_org_id='"
							+ retOrgId
							+ "' and sgm.game_id=arb.game_id and type='NET_SALE' and recon_date>'"
							+ from
							+ "' and recon_date <='"
							+ toDate
							+ "' group by game_id");
			while (resultSet.next()) {
				salePurchaseBean = new SalePurchaseBean();
				salePurchaseBean.setGameId(resultSet.getInt("game_nbr"));
				salePurchaseBean.setGameName(resultSet.getString("game_name"));
				salePurchaseBean.setMrp(resultSet.getDouble("mrp"));
				salePurchaseBean.setSalePrice(resultSet.getDouble("mrp"));
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

	public Map<String, StockMrpDiscountBean> getStockMrpDiscTktWiseMap(
			Connection con, int retOrgId, Timestamp from, Timestamp toDate) {
		StockMrpDiscountBean stockMrpDiscBean = null;
		String stGameName = null;
		double stPercent = 0;
		int stGameNo = 0;
		double stMrp = 0.0;
		double stDiscPrice = 0.0;
		int stCountTickets = 0;
		StringBuilder stockRetTicketQuery;
		System.out
				.println("Total amount in getStockMrpDiscountBeanList  enter "
						+ totalAmount);
		Map<String, StockMrpDiscountBean> tktStockMap = new HashMap<String, StockMrpDiscountBean>();
		try {

			/*
			 * new discount method
			 */
			Map<String, Timestamp> map = new HashMap<String, Timestamp>();

			statement = con.createStatement();
			resultSet = statement
					.executeQuery("select date,book_nbr from st_se_game_ticket_inv_history where  current_owner_id='"
							+ retOrgId + "' and date<='" + toDate + "'");
			while (resultSet.next()) {
				System.out.println("book_nbr "
						+ resultSet.getString("book_nbr"));
				System.out.println("date  " + resultSet.getTimestamp("date"));
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
				System.out.println("date  " + resultSet.getTimestamp("date"));
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
							+ "' and sgtih.cur_rem_tickets>0 and (book_nbr='0'");
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
			resultSet = statement.executeQuery(stockRetTicketQuery.toString());
			while (resultSet.next()) {
				String bookNo = resultSet.getString("book_nbr");
				stGameName = resultSet.getString("game_name");
				stMrp = resultSet.getDouble("mrp");
				stCountTickets = resultSet.getInt("cur_rem_tickets");
				statement2 = con.createStatement();
				resultSet2 = statement2
						.executeQuery("select (100-transacrion_sale_comm_rate) as purchrate ,game_id ,book_nbr from st_se_game_inv_detail where book_nbr='"
								+ bookNo
								+ "' order by transaction_date desc limit 1");
				while (resultSet2.next()) {
					stDiscPrice = resultSet2.getDouble("purchrate") * 0.01
							* stMrp;
					if (tktStockMap.containsKey(resultSet2.getInt("game_id")
							+ "_" + resultSet2.getDouble("purchrate"))) {

						stockMrpDiscBean = (StockMrpDiscountBean) tktStockMap
								.get(resultSet2.getInt("game_id") + "_"
										+ resultSet2.getDouble("purchrate"));
						stockMrpDiscBean.setStMrp(stockMrpDiscBean.getStMrp()
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total amount in getStockMrpDiscountBeanList  exit "
				+ totalAmount);
		return tktStockMap;
	}

	public int getUserId(String retailerName) {
		Connection con = null;
		con = DBConnect.getConnection();
		int retId = 0;
		try {
			statement = con.createStatement();

			resultSet = statement
					.executeQuery("select organization_id  from st_lms_organization_master  where name='"
							+ retailerName + "' ");

			while (resultSet.next()) {
				retId = resultSet.getInt("organization_id");
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
		return retId;
	}

	public void insertPaymentDetails(Connection con, int retOrgId,
			Timestamp from, Timestamp toDate, int agentOrgId,
			String reconReportTypeRet) {
		Map<String, CashChqPmntBean> paymentMap = getPaymentBeanList(con,
				retOrgId, from, toDate);
		CashChqPmntBean chqPmntBean;

		String insertQuery = "insert into st_lms_ret_recon_ticketwise (agent_org_id,retailer_org_id,recon_date,type,amt_parent,recon_balance) values(?,?,?,?,?,?)";
		PreparedStatement preparedStatement;
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(insertQuery);

			double prevReconBal = getReconbalance(retOrgId, con,
					reconReportTypeRet);
			if (paymentMap.size() != 0) {
				Iterator paymentItr = paymentMap.entrySet().iterator();
				while (paymentItr.hasNext()) {
					Map.Entry<String, CashChqPmntBean> paymentItrpair = (Map.Entry<String, CashChqPmntBean>) paymentItr
							.next();
					chqPmntBean = paymentItrpair.getValue();
					preparedStatement.setInt(1, agentOrgId);
					preparedStatement.setInt(2, retOrgId);
					preparedStatement.setTimestamp(3, toDate);
					if (chqPmntBean.getPaymentType().equalsIgnoreCase(
							"Cheque Bounce Charges")) {
						preparedStatement.setString(4, "ChqBounce Charges");
					} else {
						preparedStatement.setString(4, chqPmntBean
								.getPaymentType());
					}
					preparedStatement.setDouble(5, chqPmntBean
							.getPaymentAmount());
					if (chqPmntBean.getPaymentType().equalsIgnoreCase("Cash")
							|| chqPmntBean.getPaymentType().equalsIgnoreCase(
									"Cheque")) {
						preparedStatement
								.setDouble(6, (prevReconBal - chqPmntBean
										.getPaymentAmount()));
						prevReconBal = prevReconBal
								- chqPmntBean.getPaymentAmount();
					} else if (chqPmntBean.getPaymentType().equalsIgnoreCase(
							"Cheque Bounce")
							|| chqPmntBean.getPaymentType().equalsIgnoreCase(
									"Cheque Bounce Charges")
							|| chqPmntBean.getPaymentType().equalsIgnoreCase(
									"Debit Note")) {
						preparedStatement
								.setDouble(6, (prevReconBal + chqPmntBean
										.getPaymentAmount()));
						prevReconBal = prevReconBal
								+ chqPmntBean.getPaymentAmount();
					}
					preparedStatement.executeUpdate();
					con.commit();
				}
			} else {
				preparedStatement.setInt(1, agentOrgId);
				preparedStatement.setInt(2, retOrgId);
				preparedStatement.setTimestamp(3, toDate);
				preparedStatement.setString(4, "N.A.");
				preparedStatement.setDouble(5, 0.0);
				preparedStatement.setDouble(6, prevReconBal);
				preparedStatement.executeUpdate();
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertPWTDetails(Connection con, int retOrgId, Timestamp from,
			Timestamp toDate, int agentOrgId, String reconReportTypeRet) {
		List<PWTPaymentsBean> pwtBeanList = getPwtBeanList(con, retOrgId, from,
				toDate);
		String insertQuery = "insert into st_lms_ret_recon_ticketwise (retailer_org_id,game_id,recon_date,type,amt_parent,amt_child,recon_balance,agent_org_id) values(?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement;
		PWTPaymentsBean pwtPaymentsBean;
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(insertQuery);

			double prevReconBal = getReconbalance(retOrgId, con,
					reconReportTypeRet);
			if (pwtBeanList.size() != 0) {
				Iterator pwtBeanListIterator = pwtBeanList.listIterator();
				while (pwtBeanListIterator.hasNext()) {
					pwtPaymentsBean = (PWTPaymentsBean) pwtBeanListIterator
							.next();
					preparedStatement.setInt(1, retOrgId);

					preparedStatement.setInt(2, pwtPaymentsBean.getGameNo());
					preparedStatement.setTimestamp(3, toDate);
					preparedStatement.setString(4, "PWT");
					preparedStatement.setDouble(5, pwtPaymentsBean
							.getTotalAmt());
					preparedStatement.setDouble(6, pwtPaymentsBean.getNetPWT());
					preparedStatement.setDouble(7,
							(prevReconBal - pwtPaymentsBean.getTotalAmt()));
					preparedStatement.setDouble(8, agentOrgId);
					prevReconBal = prevReconBal - pwtPaymentsBean.getTotalAmt();
					preparedStatement.executeUpdate();
					con.commit();

				}
			} else {
				preparedStatement.setInt(1, retOrgId);

				preparedStatement.setInt(2, 0);
				preparedStatement.setTimestamp(3, toDate);
				preparedStatement.setString(4, "PWT");
				preparedStatement.setDouble(5, 0.0);
				preparedStatement.setDouble(6, 0.0);
				preparedStatement.setDouble(7, prevReconBal);
				preparedStatement.setDouble(8, agentOrgId);
				preparedStatement.executeUpdate();
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertSalePurchaseDetails(Connection con, int retOrgId,
			Timestamp from, Timestamp toDate, int agentOrgId,
			String reconReportTypeRet) {
		Map<Integer, SalePurchaseRetBean> salegameMap = getSalePurchaseDetails(
				con, retOrgId, from, toDate);
		String insertQuery = "insert into st_lms_ret_recon_ticketwise (agent_org_id,retailer_org_id,game_id,recon_date,type,amt_parent,amt_child,recon_balance,mrp_amt) values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement;
		SalePurchaseRetBean salePurchaseRetBean;
		double prevReconBal = getReconbalance(retOrgId, con, reconReportTypeRet);

		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(insertQuery);
			if (salegameMap.size() != 0) {
				Iterator saleItr = salegameMap.entrySet().iterator();
				while (saleItr.hasNext()) {
					Map.Entry<String, SalePurchaseRetBean> saleItrpair = (Map.Entry<String, SalePurchaseRetBean>) saleItr
							.next();
					salePurchaseRetBean = saleItrpair.getValue();

					preparedStatement.setInt(1, agentOrgId);
					preparedStatement.setInt(2, retOrgId);
					preparedStatement
							.setInt(3, salePurchaseRetBean.getGameId());
					preparedStatement.setTimestamp(4, toDate);
					preparedStatement.setString(5, "NET_SALE");
					preparedStatement.setDouble(6, salePurchaseRetBean
							.getAgtpurchPrice());
					preparedStatement.setDouble(7, salePurchaseRetBean
							.getRetpurchPrice());
					preparedStatement.setDouble(8, (salePurchaseRetBean
							.getRetpurchPrice() + prevReconBal));
					preparedStatement
							.setDouble(9, salePurchaseRetBean.getMrp());
					prevReconBal = salePurchaseRetBean.getRetpurchPrice()
							+ prevReconBal;
					preparedStatement.executeUpdate();
					con.commit();

				}
			} else {
				preparedStatement.setInt(1, agentOrgId);
				preparedStatement.setInt(2, retOrgId);
				preparedStatement.setInt(3, 0);
				preparedStatement.setTimestamp(4, toDate);
				preparedStatement.setString(5, "NET_SALE");
				preparedStatement.setDouble(6, 0.0);
				preparedStatement.setDouble(7, 0.0);
				preparedStatement.setDouble(8, prevReconBal);
				preparedStatement.setDouble(9, 0.0);
				preparedStatement.executeUpdate();
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reconRetTktwiseEntry(int retOrgId, Timestamp pToDate,
			int userOrgId, String reconReportTypeRet) {
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
					.getRetReconTktwiseDate());
			prepstatement.setInt(1, userOrgId);
			prepstatement.setInt(2, retOrgId);
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
							.executeQuery("select transaction_date from st_lms_agent_transaction_master where user_org_id='"
									+ userOrgId
									+ "' and party_id='"
									+ retOrgId
									+ "' order by transaction_date  limit 1");
					while (resultSet.next()) {
						presentDate = resultSet
								.getTimestamp("transaction_date");
					}
					if (presentDate == null) {
						System.out
								.println("***********    No transaction between Agent "
										+ userOrgId
										+ " and Retailer "
										+ retOrgId);
						return;
					}
					// date = dateFormat1.parse(dep_Date);
					// presentDate = new java.sql.Timestamp(date.getTime());
					System.out.println("Present Date in if  is " + presentDate);

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
					to.add(Calendar.DATE, 1);
					frTimeList.add(new Timestamp(from.getTimeInMillis()));
					toTimeList.add(new Timestamp(to.getTimeInMillis()));
					System.out
							.println(retOrgId
									+ " ###reconRetTktwiseEntry  same date 1111 frTimeList "
									+ frTimeList);
					System.out
							.println(retOrgId
									+ " ###reconRetTktwiseEntry same date  11111toTimeList "
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
							.println(retOrgId
									+ "&& frTimeList.size()!=0** ###reconRetTktwiseEntry  Fianal 1111 frTimeList "
									+ frTimeList);
					System.out
							.println(retOrgId
									+ "** ###reconRetTktwiseEntry Fianal  11111toTimeList "
									+ toTimeList);

				}

				if (frTimeList != null) {
					for (int i = 0; i < frTimeList.size(); i++) {
						insertSalePurchaseDetails(con, retOrgId, frTimeList
								.get(i), toTimeList.get(i), userOrgId,
								reconReportTypeRet);
						insertPaymentDetails(con, retOrgId, frTimeList.get(i),
								toTimeList.get(i), userOrgId,
								reconReportTypeRet);
						insertPWTDetails(con, retOrgId, frTimeList.get(i),
								toTimeList.get(i), userOrgId,
								reconReportTypeRet);

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

}
